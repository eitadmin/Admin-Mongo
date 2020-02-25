package com.skt.alerts;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.jboss.logging.Logger;

import com.eiw.alerts.AlertsEJBRemote;
import com.eiw.alerts.AlertsManager;
import com.eiw.device.ejb.FleetTrackingDeviceListenerBORemote;
import com.eiw.device.ejb.VehicleComposite;
import com.eiw.device.meitrack.Position;
import com.eiw.server.EmailSendHttpClient;
import com.eiw.server.MiscHttpRequest;
import com.eiw.server.SMSSendHttpClient;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.bo.BOFactory;
import com.eiw.server.companyadminpu.Provider;
import com.eiw.server.companyadminpu.Pushnotificationdevices;
import com.eiw.server.companyadminpu.Smssent;
import com.eiw.server.companyadminpu.User;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.Vehicleevent;
import com.eiw.server.studenttrackingpu.Alertevents;
import com.eiw.server.studenttrackingpu.Alerttypes;
import com.eiw.server.studenttrackingpu.Classdetails;
import com.eiw.server.studenttrackingpu.ClassdetailsId;
import com.eiw.server.studenttrackingpu.Routetrip;
import com.eiw.server.studenttrackingpu.Studentalertsubscription;
import com.eiw.server.studenttrackingpu.Studentdetails;
import com.eiw.server.studenttrackingpu.Studentevent;
import com.eiw.server.studenttrackingpu.Tagdetails;
import com.eiw.server.studenttrackingpu.Tripstops;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Sender;
import com.skt.client.dto.StudentData;

public class SKTAlertsManager extends AlertsManager {

	public SKTAlertsEJBremote sktAlertsEJBRemote = BOFactory
			.getStudentalertEJBremote();

	FleetTrackingDeviceListenerBORemote flBoRemote = BOFactory
			.getFleetTrackingDeviceListenerBORemote();

	AlertsEJBRemote alertsEJBRemote = BOFactory.getAlertsEJBRemote();
	List<Studentalertsubscription> alertsSubscribed;
	String vinnumber = null;
	static final Logger LOGGER = Logger.getLogger("listener");

	Alertevents alertEvent;

	public static enum enumStudentAlerts {
		BNC, BRC, BTC, DA, LAPP, NSPFB, NSPFS, PPM, RV, SA, SDAB, SDAS, SEB, SLB, SPFB, SPFS
	}

	String contactNo;
	// CheckStudentsStatus checkStudentsStatus;
	CheckStudentTrackingAlerts checkStudentTrackingAlerts;

	public SKTAlertsManager() {
		// checkStudentsStatus = new CheckStudentsStatus(this);
		checkStudentTrackingAlerts = new CheckStudentTrackingAlerts(this);
	}

	public void persistStudentsAlert(List<Alertevents> alertEvents,
			Map<String, StudentData> statusMapForStudent,
			List<Studentalertsubscription> alertsSubscribed, String vin,
			Routetrip routetrip) {
		LOGGER.info("SKTLog: SKTAlertsManager::persistStudentsAlert::Entered into this method::");

		try {
			if (alertEvents != null) {
				boolean isSMSDisabled;
				boolean isEmailDisabled;
				String companyId = "";
				String emailId = "";
				String userId = "";
				String branchId = "";
				List<StudentData> studentDatas = new ArrayList<StudentData>();
				for (Alertevents alertEvent1 : alertEvents) {
					int tripId = alertEvent1.getId();
					alertEvent = alertEvent1;
					String alerttype = alertEvent.getAlerttypes()
							.getAlerttype();
					String vehicleUserId = sktAlertsEJBRemote
							.getVehicleUserId(vin);
					companyId = alertEvent.getStudentdetails()
							.getClassdetails().getId().getSchoolId();
					if (vehicleUserId != null)
						alertEvent.setDescription(vehicleUserId + " "
								+ alertEvent.getDescription());
					else
						alertEvent.setDescription(companyId + " "
								+ alertEvent.getDescription());
					alertEvent.setTagId(alertEvent.getStudentdetails()
							.getTagdetails().getTagId());
					alertEvent.setContactNo("NOALERT");
					if (routetrip != null)
						alertEvent.setTripid(routetrip.getTripid());

					if (alertEvent.getAlerttypes().getAlerttype()
							.equalsIgnoreCase("BATA"))
						return;

					boolean isAlertAlreadygenerated;
					if (routetrip == null || alerttype.equalsIgnoreCase("SLB")
							|| alerttype.equalsIgnoreCase("SDAB"))
						isAlertAlreadygenerated = false;
					else
						isAlertAlreadygenerated = sktAlertsEJBRemote
								.isAlertAlreadyGenerated(alertEvent, routetrip);
					LOGGER.info("SKTLog: SKTAlertsManager::persistStudentsAlert::isAlertAlreadygenerated: "
							+ isAlertAlreadygenerated);

					alertEvent = sktAlertsEJBRemote.insertAlertevents(
							alertEvent, "insert");
					LOGGER.info("SKTLog: SKTAlertsManager::persistStudentsAlert::isAlertInserted::"
							+ alertEvent);

					if (alertEvent == null)
						return;
					boolean isAlertSubscribed = false;
					// checkAlertSubsribed(alertEvent
					// .getStudentdetails().getStin(), alerttype,
					// alertsSubscribed);
					LOGGER.info("SKTLog: SKTAlertsManager::persistStudentsAlert::isAlertSubscribed::"
							+ isAlertSubscribed);

					if (tripId == -1)
						return;
					if (isAlertSubscribed && !isAlertAlreadygenerated) {
						LOGGER.info("SKTLog: SKTAlertsManager::persistStudentsAlert::alertEvent.getId()::"
								+ tripId);
						isSMSDisabled = (alertEvent.getStudentdetails()
								.getAlertMode() == 1) ? false : true;
						isEmailDisabled = (alertEvent.getStudentdetails()
								.getAlertMode() == 2) ? false : true;
						if (alertEvent.getStudentdetails().getAlertMode() == 3) {
							isSMSDisabled = false;
							isEmailDisabled = false;
						}
						/************* SMS Validation *****************************************/
						boolean smsStatus = false;
						boolean isSmsBalAvail;
						// If alertEvent=0, SLB in pickup trip. Dont send sms
						// until
						// Bus
						// reached school

						if (!isSMSDisabled) {
							// SKT with TAGID
							User parent = sktAlertsEJBRemote
									.getParentname(alertEvent);
							if (parent != null) {

								branchId = alertEvent.getStudentdetails()
										.getClassdetails().getId()
										.getBranchId();
								emailId = parent.getFax();
								userId = parent.getId().getEmailAddress();
								contactNo = parent.getContactNo();
								alertEvent.setContactNo(contactNo);

							}

							// Check sms balance
							List<String> listData = new ArrayList<>();
							listData.add(companyId);
							listData.add(branchId);
							isSmsBalAvail = alertsEJBRemote
									.isSmsBalAvail(listData);
							if (isSmsBalAvail) {
								new Thread() {
									public void run() {
										String companyId = alertEvent
												.getStudentdetails()
												.getClassdetails().getId()
												.getSchoolId();
										List<Provider> providers = alertsEJBRemote
												.getProvidersDetails(companyId);
										for (Provider provider : providers) {
											String smsId = sendSMSandReturnSMSid(
													provider, contactNo,
													alertEvent.getDescription());
											try {
												sleep(30000);
											} catch (InterruptedException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}
											String deliveryStatus = getDeliverystatus(
													smsId, provider);
											if (deliveryStatus
													.equalsIgnoreCase("delivered"))
												break;
										}
									}
								}.start();

								StudentData studentData = new StudentData();
								studentData.setContactNo(contactNo);
								studentData.setAddress(alertEvent
										.getDescription());
								studentDatas.add(studentData);
								// if (smsHttpStatus.equalsIgnoreCase("OK")) {
								smsStatus = true;
								// insert sms sent table
								try {
									alertsEJBRemote.insertSMS1(prepareSmsSent(
											alertEvent, companyId, branchId,
											userId));
								} catch (Exception e) {
									LOGGER.error("I am here at exp" + e);
								}
							}
						}

						/************* Email Validation *****************************************/

						if (!isEmailDisabled) {
							String emailHttpStatus = "failure";

							// String emailAddress = statusMapForStudent.get(
							// alertEvent.getStudentdetails().getTagdetails()
							// .getTagId()).getEmailAddress();
							String emailAddress = emailId;
							if (sktAlertsEJBRemote
									.isEmailValidOrNot(emailAddress)) {
								emailHttpStatus = EmailSendHttpClient.send(
										emailAddress,
										"Alert "
												+ alertEvent.getAlerttypes()
														.getDescription(),
										alertEvent.getDescription().replaceAll(
												"%0D", ""));

							}
							if (emailHttpStatus.equalsIgnoreCase("message")) {
								LOGGER.error("Email Sent Successfully");
							}

						}
						sktAlertsEJBRemote.insertAlertevents(alertEvent,
								"update");

					}

					/************* Persist *****************************************/
					// Commented for SBT going to use vehiclealerts instead of
					// alerts
					// if (check) {
					// alertsEJBRemote.insertVehicleAlertevents(alertEvent);
					// check = false;
					// }
				}
			}
		} catch (Exception e) {
			LOGGER.error("SKTLog: SKTAlertsManager::persistStudentsAlert::Exception="
					+ e.getMessage());
			e.printStackTrace();
		}
	}

	protected String sendSMSandReturnSMSid(Provider provider, String contactNo,
			String description) {
		LOGGER.info("SKTAlertsManager sendSMSandReturnSMSid :"
				+ provider.getProviderName() + " contactNo = " + contactNo);
		try {
			if (provider.getProviderName().equalsIgnoreCase("SKT_smsachariya")) {
				String[] templateidAndMessage = description.split(Pattern
						.quote("^"));
				String templateId = templateidAndMessage[0];
				String message = templateidAndMessage[1];
				message = URLEncoder.encode(message, "UTF-8");

				String url = provider.getSmsApi();
				url = url.replace("msgtext", message);
				url = url.replace("toNumber", contactNo);
				url = url.replace("templateId", templateId);
				return SMSSendHttpClient.requestandgetResponse(url);
			} else if (provider.getProviderName().equalsIgnoreCase(
					"SKT_mobilogi")) {
				return "";
			} else if (provider.getProviderName().equalsIgnoreCase(
					"SKT_b2bHTTP")) {
				return "";
			} else if (provider.getProviderName().equalsIgnoreCase(
					"SKT_b2bSMPP")) {
				sktBTBSMPP(contactNo, description);
				return "SKT_b2bSMPP";
			} else if (provider.getProviderName()
					.equalsIgnoreCase("teleshoppe")) {
				String message = URLEncoder.encode(description, "UTF-8");
				String url = provider.getSmsApi();
				url = url.replace("msgtext", message);
				url = url.replace("toNumber", contactNo);
				SMSSendHttpClient.requestandgetResponse(url);
				return "teleshoppe";
			} else if (provider.getProviderName().equalsIgnoreCase("SKT_b2b")) {
				String[] msg = description.split("#");
				String url = "";
				if (msg[1].equalsIgnoreCase("EN")) {
					url = provider.getSmsApi();
				} else {
					url = provider.getSmsApi() + "&route_id=21&msgtype=unicode";
				}
				String message = URLEncoder.encode(msg[0], "UTF-8");
				url = url.replace("msgtext", message);
				url = url.replace("toNumber", contactNo);
				return SMSSendHttpClient.requestandgetResponse(url);
			} else {
				String message = URLEncoder.encode(description, "UTF-8");
				String url = provider.getSmsApi();
				url = url.replace("msgtext", message);
				url = url.replace("toNumber", contactNo);
				return SMSSendHttpClient.requestandgetResponse(url);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "";
	}

	private void sktBTBSMPP(String contactNo, String description) {
		List<StudentData> studentDatas = new ArrayList<StudentData>();
		StudentData studentData = new StudentData();
		studentData.setContactNo(contactNo);
		studentData.setAddress(description);
		studentDatas.add(studentData);
		sendListofSMS(studentDatas);
	}

	protected String getDeliverystatus(String smsId, Provider provider) {
		String providerName = provider.getProviderName();
		if (providerName.equalsIgnoreCase("SKT_smsachariya")) {
			String status = SMSSendHttpClient
					.requestandgetResponse("http://smsalertbox.com/api/dlr.php?uid=656974776f726b73&pin=1d53ef7419622d21212bd90e300dbdf0&msgid="
							+ smsId);
			if (status.equalsIgnoreCase("Delivered,"))
				return "delivered";
			else
				return status;
		} else if (providerName.equalsIgnoreCase("SKT_b2bHTTP"))
			return "not delivered";
		else if (providerName.equalsIgnoreCase("SKT_mobilogi"))
			return "not delivered";
		else if (providerName.equalsIgnoreCase("SKT_b2bSMPP"))
			return "delivered";
		else if (providerName.equalsIgnoreCase("teleshoppe"))
			return "delivered";

		return "not delivered";
	}

	public void sendListofSMS(List<StudentData> studentDatas) {
		String DEFAULT_PASSWORD = "welc0meR";
		String DEFAULT_SYSID = "eitworks";
		String DEFAULT_SOURCEADDR = "kTRACK";
		String DEFAULT_HOST = "210.16.103.238";

		Integer DEFAULT_PORT = 6060;

		SendSMPPMessage sendSMPPMessage = new SendSMPPMessage();
		LOGGER.info("SKTLog: SKTAlertsManager::sendListofSMS:: "
				+ studentDatas.get(0).getContactNo());

		sendSMPPMessage.sendSMPPMessage(DEFAULT_HOST, DEFAULT_PORT,
				DEFAULT_SYSID, DEFAULT_PASSWORD, DEFAULT_SOURCEADDR,
				studentDatas);
	}

	public Smssent prepareSmsSent(Alertevents altevents, String companyId,
			String branchId, String userId) {
		try {
			LOGGER.debug("SKTLog: SKTAlertsManager::prepareSmsSent::Entered into this method");
			String alertType = altevents.getAlerttypes().getAlerttype();
			Smssent smssent = new Smssent();
			smssent.setCompanyid(companyId);
			smssent.setBranchId(branchId);
			smssent.setUserId(userId);
			smssent.setCategory("Alert");
			smssent.setFromMobile("SKT Alert");
			smssent.setMisc(alertType);
			smssent.setMsg(altevents.getDescription());
			smssent.setToMobile(contactNo);
			smssent.setMsgType(altevents.getStudentdetails().getStin());
			smssent.setEventTimeStamp(altevents.getEventTimeStamp());
			smssent.setServerTimeStamp(altevents.getEventTimeStamp());
			return smssent;
		} catch (Exception e) {
			LOGGER.error("SKTLog: SKTAlertsManager::prepareSmsSent::Exception = "
					+ e.getMessage());
			e.printStackTrace();
			return null;
		}

	}

	public String getAddressFromLatlng(String latitude, String longitude)
			throws Exception {
		String formattedAddressStop;
		formattedAddressStop = MiscHttpRequest.invokeGoecoder(latitude,
				longitude);
		if (formattedAddressStop.equalsIgnoreCase("OVER_QUERY_LIMIT")) {
			Thread.sleep(100);
			formattedAddressStop = MiscHttpRequest.invokeGoecoder(latitude,
					longitude);
			formattedAddressStop = formattedAddressStop
					.equalsIgnoreCase("OVER_QUERY_LIMIT") ? ""
					: formattedAddressStop;
		}
		return formattedAddressStop;
	}

	public boolean checkAlertSubsribed(Studentdetails studentdetails,
			String alerttype) {

		for (Studentalertsubscription studentalertsubscription : studentdetails
				.getStudentalertsubscriptions()) {
			if (studentalertsubscription.getAlerttypes().getAlerttype()
					.equalsIgnoreCase(alerttype))
				return true;
		}
		return false;
	}

	public boolean isTimeExceeds(long firstTime, long secondTime, long graceTime) {
		long MAX_DURATION = TimeUnit.MILLISECONDS.convert(graceTime,
				TimeUnit.MINUTES) / (60 * 1000);
		long diffrencebetweentwoTimes = diffrenceBetweenTimeStamps(firstTime,
				secondTime);
		if (-MAX_DURATION <= diffrencebetweentwoTimes
				&& diffrencebetweentwoTimes <= MAX_DURATION) {
			return true;
		}
		return false;
	}

	public String timeDiffrenceinhours(long timeDifference) {
		long hours = timeDifference / 60;
		long minutes = timeDifference % 60;
		String timeDiffrence = hours + "Hours" + ":" + minutes + "minutes";

		return timeDiffrence;
	}

	public long diffrenceBetweenTimeStamps(long firstTime, long secondTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		String currTimeStr = sdf.format(secondTime);
		long currTime = 0;
		try {
			currTime = sdf.parse(currTimeStr).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long diffrencebetweentwoTimes = (currTime - firstTime) / (60 * 1000);
		return diffrencebetweentwoTimes;
	}

	public List<StudentData> getStudentsForThisRouteStop(int routestopid,
			Map<String, StudentData> studentDetailsNStatusMap) {
		// TODO Auto-generated method stub
		List<StudentData> studentsForThisStop = new ArrayList<StudentData>();
		for (Map.Entry<String, StudentData> studentDetailsNStatus : studentDetailsNStatusMap
				.entrySet()) {
			long pickupid = studentDetailsNStatus.getValue().getPickuptopId();
			long dropid = studentDetailsNStatus.getValue().getDropstopId();
			if ((pickupid == routestopid) || (dropid == routestopid)) {
				studentsForThisStop.add(studentDetailsNStatus.getValue());
			}
		}
		return studentsForThisStop;
	}

	public void manageSchoolAlerts(Vehicleevent vehicleEvent, String cardHit,
			Map<String, StudentData> studentDetailsNStatusMap,
			List<Studentalertsubscription> studentalertsubscriptions,
			Vehicle vehicle) {

		LOGGER.debug("SKTLog: SKTAlertsManager: manageSchoolAlerts method entered");

		// alertsSubscribed = alertsEJBRemote
		// .getAlertsSubscribed(vehicle.getVin());
		Map<String, String> alerttypesDescriptionMap = sktAlertsEJBRemote
				.getSKTalerttypesMap();
		// if ((alertsSubscribed == null) || (alertsSubscribed.isEmpty()))
		// return;
		checkStudentTrackingAlerts.manageSBTAlerts(vehicleEvent, cardHit,
				studentDetailsNStatusMap, studentalertsubscriptions, vehicle,
				alerttypesDescriptionMap);
	}

	public void getLastCrossedBusStop(String vin) {
		// sktAlertsEJBRemote.getLastCrossedBusStop(vin);
		// sktAlertsEJBRemote.getSKTTripStops(vin);
	}

	public Provider getSimulatedVehicleEvent() {
		Provider provider = sktAlertsEJBRemote.getSimulatedLatLong(9999);
		return provider;
	}

	public void test() {
		// return sktAlertsEJBRemote.getSKTTripStops("sri0000", "srikrish",
		// "srikrish");
		sktAlertsEJBRemote.getPushNotificationDevice("9940596910",
				"Firstschool");
	}

	public void test1() {

		Date eventTimeStamp = new Date();
		Routetrip routetrip = null;

		Calendar calendarEventTime = Calendar.getInstance();
		calendarEventTime.setTime(eventTimeStamp);
		// Date tripStartTime = new Date(eventTimeStamp.getTime());
		System.out.println(routetrip.getTripEndtime().toString());
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(routetrip.getTripStarttime());
		calendar.set(Calendar.DATE, calendarEventTime.get(Calendar.DATE));
		calendar.set(Calendar.MONTH, calendarEventTime.get(Calendar.MONTH));
		calendar.set(Calendar.YEAR, calendarEventTime.get(Calendar.YEAR));

		Date tripStartTime = calendar.getTime();
		calendar.setTime(routetrip.getTripEndtime());
		calendar.set(Calendar.DATE, calendarEventTime.get(Calendar.DATE));
		calendar.set(Calendar.MONTH, calendarEventTime.get(Calendar.MONTH));
		calendar.set(Calendar.YEAR, calendarEventTime.get(Calendar.YEAR));

		Date tripEndTime = calendar.getTime();

		boolean isCurrentTrip = CommonUtil.isTimeInRange(tripStartTime,
				tripEndTime, eventTimeStamp);

	}

	public void insertTagintoDB(String tagid) {
		alertsEJBRemote.insertTagintoDB(tagid);
	}

	public void manageStudentDeviationAlerts(Vehicleevent vehicleEvent,
			Map<String, StudentData> studentDetailsNStatusMap,
			List<Studentalertsubscription> studentalertsubscriptions,
			Vehicle vehicle) {
		LOGGER.debug("SKTLog: SKTAlertsManager:Entered manageStudentDeviationAlerts");

		Map<String, String> alerttypesDescriptionMap = new HashMap<String, String>();
		alerttypesDescriptionMap = sktAlertsEJBRemote.getSKTalerttypesMap();
		// checkStudentTrackingAlerts.manageStudentDeviationAlerts(vehicleEvent,
		// studentDetailsNStatusMap, studentalertsubscriptions, vehicle,
		// alerttypesDescriptionMap);
	}

	public Provider getDevicesForPushNotificatoinDemo(String imeiNo) {
		Provider providers = sktAlertsEJBRemote
				.getDevicesForPushNotificatoinDemo(imeiNo);
		return providers;
	}

	public void sendPushMessage(String userMessage, String contactNo,
			String pushlanguage, String companyId) {
		// TODO Auto-generated method stub
		try {
			LOGGER.error("SKTLog: SKTAlertsManager : sendPushMessage : "
					+ contactNo + " and " + companyId + " and " + userMessage);
			List<Pushnotificationdevices> pushnotificationdevices = sktAlertsEJBRemote
					.getPushNotificationDevice(contactNo, companyId);
			if (pushnotificationdevices != null) {
				for (Pushnotificationdevices pushnotificationdevice : pushnotificationdevices) {

					LOGGER.error("SKTLog: SKTAlertsManager : sendPushMessage : imei= "
							+ pushnotificationdevice.getId().getImei());

					if (pushnotificationdevice.getOs().equalsIgnoreCase(
							"Android")) {
						String serverkey = alertsEJBRemote.getServerKey(
								pushnotificationdevice.getId().getAppName(),
								pushnotificationdevice.getOs());
						if (serverkey != null) {
							sendPushNotification(
									pushnotificationdevice.getDeviceId(),
									userMessage, serverkey);
						}
					} else if (pushnotificationdevice.getOs().equalsIgnoreCase(
							"ios")) {
						Provider provider = alertsEJBRemote
								.getProviderDetails(companyId);
						LOGGER.info("SKTLog: SKTAlertsManager : sendPushMessage : ios = pushnotificationdevice.getDeviceId() = "
								+ pushnotificationdevice.getDeviceId());
						LOGGER.info("SKTLog: SKTAlertsManager : sendPushMessage : ios = userMessage = "
								+ userMessage);
						LOGGER.info("SKTLog: SKTAlertsManager : sendPushMessage : ios = provider.getIosCertificateKey() = "
								+ provider.getIosCertificateKey());
						String serverkey = alertsEJBRemote.getServerKey(
								pushnotificationdevice.getId().getAppName(),
								pushnotificationdevice.getOs());
						if (serverkey != null) {
							sendIosPushNotification(
									pushnotificationdevice.getDeviceId(),
									pushnotificationdevice.getId().getAppName(),
									userMessage, serverkey);
						}
					} else if (pushnotificationdevice.getOs().equalsIgnoreCase(
							"ionic")) {

						String serverkey = alertsEJBRemote.getServerKey(
								pushnotificationdevice.getId().getAppName(),
								pushnotificationdevice.getOs());
						if (serverkey != null) {
							sendIonicPushNotification(
									pushnotificationdevice.getDeviceId(),
									userMessage, serverkey,
									pushnotificationdevice.getId().getAppName());
						}
					}

				}
			}
		} catch (Exception e) {
			LOGGER.error("SKTLog: SKTAlertsManager : sendPushMessage : "
					+ e.getMessage());
			e.printStackTrace();
		}
	}

	public Studentdetails setStudentdetails(
			Map<String, StudentData> studentDetailsNStatusMap, String tagIdIN,
			Vehicleevent vehicleEvent, StudentData studentData) {
		Studentdetails studentdetails = new Studentdetails();
		try {

			Tagdetails tagdetails = new Tagdetails();
			tagdetails.setTagId(tagIdIN);
			studentdetails.setTagdetails(tagdetails);
			studentdetails.setStin(studentDetailsNStatusMap.get(tagIdIN)
					.getStin());
			studentdetails.setAlertMode(studentDetailsNStatusMap.get(tagIdIN)
					.getAlertMode());
			Date eventTime = vehicleEvent.getId().getEventTimeStamp();
			studentdetails.setLastUpdDt(eventTime);

			studentdetails.setLastUpdLatlng((String.valueOf(vehicleEvent
					.getLatitude() + "," + vehicleEvent.getLongitude())));
			studentdetails.setParentId(studentDetailsNStatusMap.get(tagIdIN)
					.getParentName());
			ClassdetailsId classdetailsId = new ClassdetailsId();
			classdetailsId.setSchoolId(studentDetailsNStatusMap.get(tagIdIN)
					.getSchoolId());
			classdetailsId.setBranchId(studentDetailsNStatusMap.get(tagIdIN)
					.getBrannchId());
			classdetailsId.setClassId(studentDetailsNStatusMap.get(tagIdIN)
					.getClassId());
			classdetailsId.setSectionId(studentDetailsNStatusMap.get(tagIdIN)
					.getSectionId());
			Classdetails classdetails = new Classdetails();
			classdetails.setId(classdetailsId);
			studentdetails.setClassdetails(classdetails);
			studentdetails.setAddress(studentData.getAddress());
			studentdetails.setFirstName(studentData.getFirstName());
			studentdetails.setLastName(studentData.getLastName());
			studentdetails.setLatestEventTime(studentData.getEventTime());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return studentdetails;
	}

	public void prepareStudentAlerts(Studentevent studentevent,
			VehicleComposite vehicleComposite, Routetrip currRoutetrip,
			Studentdetails studentdetails) {
		checkStudentTrackingAlerts.prepareStudentAlerts(studentevent,
				vehicleComposite, currRoutetrip, studentdetails);
	}

	public void persistStudentsAlert(Alertevents alertEvent,
			Studentevent studentevent, VehicleComposite vehicleComposite,
			Routetrip routetrip, Studentdetails studentdetails, User parent) {

		LOGGER.info("SKTLog: SKTAlertsManager::persistStudentsAlert::Entered into this method::");

		try {
			if (alertEvent != null) {
				boolean isSMSDisabled;
				boolean isEmailDisabled;
				String emailId = "";
				String companyId = vehicleComposite.getVehicle().getCompanyId();
				String branchId = vehicleComposite.getVehicle().getBranchId();

				int tripId = new Integer(alertEvent.getId());
				String alerttype = alertEvent.getAlerttypes().getAlerttype();
				String userId = vehicleComposite.getVehicle().getUserId();
				if (userId != null)
					alertEvent.setDescription(userId + ": "
							+ alertEvent.getDescription());
				else
					alertEvent.setDescription(companyId + ": "
							+ alertEvent.getDescription());

				// if (alerttype.equalsIgnoreCase("BATA"))
				// return;

				boolean isAlertAlreadygenerated;
				String isdemo = alertsEJBRemote.getPreferencesData("Isdemo",
						studentdetails.getClassdetails().getId().getSchoolId());
				if (routetrip == null || alerttype.equalsIgnoreCase("SLB")
						|| alerttype.equalsIgnoreCase("SDAB")
						|| alerttype.equalsIgnoreCase("SCG")
						|| isdemo.equalsIgnoreCase("true"))
					isAlertAlreadygenerated = false;
				else
					isAlertAlreadygenerated = sktAlertsEJBRemote
							.isAlertAlreadyGenerated(alertEvent, routetrip);
				LOGGER.info("SKTLog: SKTAlertsManager::persistStudentsAlert::isAlertAlreadygenerated: "
						+ isAlertAlreadygenerated);
				LOGGER.info("SKTLog: SKTAlertsManager::persistStudentsAlert::isAlertInserted::"
						+ alertEvent);

				if (alertEvent == null || tripId == -1)
					return;

				boolean isAlertSubscribed = sktAlertsEJBRemote
						.isAlertSubscribed(studentdetails.getStin(), alerttype);

				LOGGER.info("SKTLog: SKTAlertsManager::persistStudentsAlert::isAlertSubscribed::"
						+ isAlertSubscribed);
				if (!isAlertSubscribed)
					alertEvent.setContactNo("ANS");
				if (isAlertAlreadygenerated)
					alertEvent.setContactNo("AAG");
				if (isAlertSubscribed && !isAlertAlreadygenerated) {
					LOGGER.info("SKTLog: SKTAlertsManager::persistStudentsAlert::alertEvent.getId()::"
							+ tripId);
					isSMSDisabled = (alertEvent.getStudentdetails()
							.getAlertMode() == 1) ? false : true;
					isEmailDisabled = (alertEvent.getStudentdetails()
							.getAlertMode() == 2) ? false : true;
					if (alertEvent.getStudentdetails().getAlertMode() == 3) {
						isSMSDisabled = false;
						isEmailDisabled = false;
					}
					/************* SMS Validation *****************************************/
					// If alertEvent=0, SLB in pickup trip. Dont send sms
					// until
					// Bus
					// reached school

					if (parent != null) {

						emailId = parent.getFax();
						userId = parent.getId().getEmailAddress();
						alertEvent.setContactNo("NAM");
						contactNo = parent.getContactNo();
						sendPushMessage(alertEvent.getDescription(), contactNo,
								"", companyId);

						if (!isSMSDisabled) {
							alertEvent.setContactNo(contactNo);
							sendSMS(companyId, branchId, userId, contactNo,
									alertEvent);
						}

						/************* Email Validation *****************************************/

						if (!isEmailDisabled) {
							if (emailId != null)
								sendEmail(emailId, alertEvent);
						}
						sktAlertsEJBRemote.insertAlertevents(alertEvent,
								"insert");
					}
				} else {
					sktAlertsEJBRemote.insertAlertevents(alertEvent, "insert");
				}
			}
		} catch (Exception e) {
			LOGGER.error("SKTLog: SKTAlertsManager::persistStudentsAlert::Exception="
					+ e.getMessage());
			e.printStackTrace();
		}

	}

	public void sendEmail(String emailAddress, Alertevents alertEvent) {

		String emailHttpStatus = "failure";
		if (sktAlertsEJBRemote.isEmailValidOrNot(emailAddress)) {
			emailHttpStatus = EmailSendHttpClient.send(emailAddress, "Alert "
					+ alertEvent.getAlerttypes().getDescription(), alertEvent
					.getDescription().replaceAll("%0D", ""));
		}
		if (emailHttpStatus.equalsIgnoreCase("message")) {
			LOGGER.error("Email Sent Successfully");
		}

	}

	public StudentData sendSMS(String companyId, String branchId,
			String userId, final String contactNo, final Alertevents alertevent) {
		final String description = alertevent.getDescription();
		LOGGER.info("SKTAlertsManager sendSMS :1 contactNo = " + contactNo
				+ " and description " + description);

		final List<Provider> providers = alertsEJBRemote
				.getProvidersDetails(companyId);
		if (providers.size() != 0) {
			LOGGER.info("SKTAlertsManager sendSMS : providers = "
					+ providers.get(0).getId());

			// Check sms balance
			List<String> listData = new ArrayList<>();
			listData.add(companyId);
			listData.add(branchId);
			boolean isSmsBalAvail = alertsEJBRemote.isSmsBalAvail(listData);
			if (isSmsBalAvail) {
				new Thread() {
					public void run() {
						for (Provider provider : providers) {
							LOGGER.info("SKTAlertsManager sendSMS :2 contactNo = "
									+ contactNo
									+ " and description "
									+ description);
							String smsId = sendSMSandReturnSMSid(provider,
									contactNo, description);
							LOGGER.info("SKTAlertsManager sendSMS :smsId= "
									+ smsId);
							try {
								sleep(30000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch
								// block
								e.printStackTrace();
							}
							String deliveryStatus = getDeliverystatus(smsId,
									provider);
							if (deliveryStatus.equalsIgnoreCase("delivered"))
								break;
						}
					}
				}.start();

				StudentData studentData = new StudentData();
				studentData.setContactNo(contactNo);
				studentData.setAddress(description);
				try {
					LOGGER.info("SKTAlertsManager sendSMS : insertSMS1 = ");
					alertsEJBRemote.insertSMS1(prepareSmsSent(alertevent,
							companyId, branchId, userId));
				} catch (Exception e) {
					LOGGER.error("I am here at exp" + e.getMessage());
				}
				return studentData;
			}
		}
		return null;
	}

	public void setPushNotificationForDemo(Provider provider,
			Position position, Studentdetails studentdetails) {
		LOGGER.info("SKTLog: SKTAlertsManager : setPushNotificationForDemo studentdetails = "
				+ studentdetails);
		try {

			String[] alerttypes = provider.getLogoUrl().split(",");
			int indexOfLastMessage = Integer.parseInt(provider.getEmail());
			indexOfLastMessage = indexOfLastMessage % alerttypes.length;

			String alerttype = alerttypes[indexOfLastMessage];

			User parent = sktAlertsEJBRemote.getParent(provider.getMobile(),
					provider.getFullName());

			Map<String, String> alerttypesDescriptionMap = sktAlertsEJBRemote
					.getSKTalerttypesMap();
			String description = alerttypesDescriptionMap.get(alerttype)
					.toString();

			description = description.replace("parentname",
					parent.getFirstName());
			String studentName = studentdetails.getFirstName();
			if (studentdetails.getLastName() != null)
				studentName += " " + studentdetails.getLastName();
			description = description.replace("studentname", studentName);

			String location = "";
			if (alerttype.equalsIgnoreCase("SPFS")
					|| alerttype.equalsIgnoreCase("SDAS"))
				location = "school";
			else if (alerttype.equalsIgnoreCase("SPFB")) {
				Tripstops ts = sktAlertsEJBRemote
						.getTripStopById(studentdetails
								.getTripstopsByPickupid().getStopPointId());
				if (ts != null)
					location = ts.getStopName();
			} else if (alerttype.equalsIgnoreCase("SDAB")) {
				Tripstops ts = sktAlertsEJBRemote
						.getTripStopById(studentdetails.getTripstopsByDropid()
								.getStopPointId());
				if (ts != null)
					location = ts.getStopName();
			}
			description = description.replace("busstopname", location);

			Date date = TimeZoneUtil.getDateInTimeZoneforSKT(provider
					.getAlertMailId());

			description = description.replace("eventtime",
					getDateTimeInFormat(date));

			// description = description.replace("eventtime", position.getTime()
			// .toString());

			sendDemoPushMessage(description, provider);

			indexOfLastMessage = (indexOfLastMessage + 1) % alerttypes.length;
			provider.setEmail(String.valueOf(indexOfLastMessage));
			sktAlertsEJBRemote.updateProvider(provider);
			// Demoalerts demoAlerts = new Demoalerts();
			// demoAlerts.setAlerttype(alerttype);
			// demoAlerts.setContactNo(parent.getContactNo());
			// demoAlerts.setDescription(description);
			// demoAlerts.setEventTimeStamp(position.getTime());
			// StringBuilder stringBuilder = new StringBuilder();
			// stringBuilder.append(position.getLatitude()).append(",")
			// .append(position.getLongitude());
			// demoAlerts.setLatlong(stringBuilder.toString());
			// demoAlerts.setLocation(location);
			// demoAlerts.setStin(studentdetails.getStin());
			// demoAlerts.setTagId(position.getRfid());
			// demoAlerts.setVin(routeAndVehicleData.getVehicleComposite()
			// .getVehicle().getVin());
			// sktAlertsEJBRemote.persistDemoAlerts(demoAlerts);
		} catch (Exception e) {
			LOGGER.error("SKTLog: SKTAlertsManager : setPushNotificationForDemo Exception = "
					+ e.getMessage());
			e.printStackTrace();
		}
	}

	public void stuentsAlertsByBusMove(VehicleComposite vehicleComposite,
			Routetrip routetrip, Vehicleevent vehicleEvent) {
		checkStudentTrackingAlerts.stuentsAlertsByBusMove(vehicleComposite,
				routetrip, vehicleEvent);
	}

	public void persistPendingStudentsAlert(List<Alertevents> alertevents) {
		try {
			for (Alertevents alertevent : alertevents) {
				LOGGER.info("SKTLog: SKTAlertsManager::persistPendingStudentsAlert::alertevent::"
						+ alertevent.getEventTimeStamp());
				Studentdetails studentdetails = alertevent.getStudentdetails();
				Alerttypes alerttype = alertevent.getAlerttypes();
				boolean isAlertSubscribed = sktAlertsEJBRemote
						.isAlertSubscribed(studentdetails.getStin(),
								alerttype.getAlerttype());
				LOGGER.info("SKTLog: SKTAlertsManager::persistPendingStudentsAlert::isAlertSubscribed::"
						+ isAlertSubscribed);
				alertevent.setContactNo("ANS");
				if (isAlertSubscribed) {
					LOGGER.info("SKTLog: SKTAlertsManager::persistPendingStudentsAlert::alertEvent.getId()::");
					boolean isSMSDisabled = (alertevent.getStudentdetails()
							.getAlertMode() == 1) ? false : true;
					boolean isEmailDisabled = (alertevent.getStudentdetails()
							.getAlertMode() == 2) ? false : true;
					if (alertevent.getStudentdetails().getAlertMode() == 3) {
						isSMSDisabled = false;
						isEmailDisabled = false;
					}
					/************* SMS Validation *****************************************/
					// If alertEvent=0, SLB in pickup trip. Dont send sms
					// until
					// Bus
					// reached school
					User parent = sktAlertsEJBRemote.getParent(studentdetails);

					if (parent != null) {

						String emailId = parent.getFax();
						String userId = parent.getId().getEmailAddress();
						contactNo = parent.getContactNo();
						alertevent.setContactNo("NAM");
						sendPushMessage(alertevent.getDescription(), contactNo,
								"", studentdetails.getClassdetails().getId()
										.getSchoolId());

						if (!isSMSDisabled) {
							alertevent.setContactNo(contactNo);
							sendSMS(studentdetails.getClassdetails().getId()
									.getSchoolId(), studentdetails
									.getClassdetails().getId().getBranchId(),
									userId, contactNo, alertevent);
						}

						/************* Email Validation *****************************************/

						if (!isEmailDisabled) {
							if (emailId != null)
								sendEmail(emailId, alertevent);
						}
					}

				}
				sktAlertsEJBRemote.insertAlertevents(alertevent, "update");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendDemoPushMessage(String userMessage, Provider provider) {
		try {

			String contactNo = provider.getMobile();
			String companyId = provider.getAppServerKey();
			String stin = provider.getAlertMailPasscode();
			String timezone = provider.getAlertMailId();
			LOGGER.info("SKTLog: SKTAlertsManager : sendDemoPushMessage : "
					+ contactNo + " and " + companyId + " and " + userMessage);
			String GOOGLE_SERVER_KEY = provider.getIosCertificateKey();
			String deviceId = "";
			List<Pushnotificationdevices> pushnotificationdevices = sktAlertsEJBRemote
					.getPushNotificationDevice(contactNo, companyId);
			if (pushnotificationdevices != null) {
				for (Pushnotificationdevices pushnotificationdevice : pushnotificationdevices) {
					LOGGER.info("SKTLog: SKTAlertsManager : sendDemoPushMessage : imei= "
							+ pushnotificationdevice.getId().getImei());
					deviceId = pushnotificationdevice.getDeviceId();
					Sender sender = new Sender(GOOGLE_SERVER_KEY);
					Message message = new Message.Builder().timeToLive(86400)
							.delayWhileIdle(true)
							.addData("success", userMessage).build();
					sender.send(message, deviceId, 1);
				}
			}
			Date date = TimeZoneUtil.getDateInTimeZoneforSKT(timezone);
			// SimpleDateFormat f = new
			// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// f.setTimeZone(TimeZone.getTimeZone("Asia/Riyadh"));
			LOGGER.info("SKTLog: SKTAlertsManager : 1");
			Alertevents alertevents = new Alertevents();
			Studentdetails studentdetails = new Studentdetails();
			studentdetails.setStin(stin);
			alertevents.setStudentdetails(studentdetails);
			// String dateStr = f.format(date);
			alertevents.setEventTimeStamp(date);
			alertevents.setDescription(userMessage);
			alertevents.setContactNo(contactNo);
			LOGGER.info("SKTLog: SKTAlertsManager : 2" + alertevents);

			sktAlertsEJBRemote.persistDemoAlerts(alertevents);

		} catch (Exception e) {
			LOGGER.error("SKTLog: SKTAlertsManager : sendDemoPushMessage : "
					+ e.getMessage());
			e.printStackTrace();
		}
	}

	private String getDateTimeInFormat(Date date) {
		SimpleDateFormat sdfTime = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
		return sdfTime.format(date);
	}

	public void alertForAttendence(List<Studentdetails> studentdetails,
			String type) {
		for (Studentdetails Studentdetail : studentdetails)
			checkStudentTrackingAlerts.alertForAttendence(Studentdetail, type);
	}

	public void persistAttendenceAlerts(Alertevents alertevents,
			Studentdetails studentdetail, User parent, Routetrip routetrip) {
		LOGGER.info("SKTLog: SKTAlertsManager::persistAttendenceAlert::Entered into this method::");

		try {
			boolean isSMSDisabled;
			boolean isEmailDisabled;
			String emailId = "";
			String companyId = studentdetail.getClassdetails().getId()
					.getSchoolId();
			String branchId = studentdetail.getClassdetails().getId()
					.getBranchId();
			String alerttype = alertevents.getAlerttypes().getAlerttype();
			alertevents.setDescription(companyId + ": "
					+ alertevents.getDescription());

			// if (alerttype.equalsIgnoreCase("BATA"))
			// return;

			boolean isAlertAlreadygenerated;
			isAlertAlreadygenerated = sktAlertsEJBRemote
					.isAlertAlreadyGenerated(alertevents, routetrip);
			LOGGER.info("SKTLog: SKTAlertsManager::persistAttendenceAlert::isAlertAlreadygenerated: "
					+ isAlertAlreadygenerated);

			alertevents = sktAlertsEJBRemote.insertAlertevents(alertevents,
					"insert");
			LOGGER.info("SKTLog: SKTAlertsManager::persistAttendenceAlert::isAlertInserted::"
					+ alertevents);
			boolean isAlertSubscribed = sktAlertsEJBRemote.isAlertSubscribed(
					studentdetail.getStin(), alerttype);

			LOGGER.info("SKTLog: SKTAlertsManager::persistAttendenceAlert::isAlertSubscribed::"
					+ isAlertSubscribed);
			if (!isAlertSubscribed)
				alertevents.setContactNo("ANS");
			if (isAlertAlreadygenerated)
				alertevents.setContactNo("AAG");
			if (isAlertSubscribed && !isAlertAlreadygenerated) {
				isSMSDisabled = (alertevents.getStudentdetails().getAlertMode() == 1) ? false
						: true;
				isEmailDisabled = (alertevents.getStudentdetails()
						.getAlertMode() == 2) ? false : true;
				if (alertevents.getStudentdetails().getAlertMode() == 3) {
					isSMSDisabled = false;
					isEmailDisabled = false;
				}
				/************* SMS Validation *****************************************/
				// If alertEvent=0, SLB in pickup trip. Dont send sms
				// until
				// Bus
				// reached school

				if (parent != null) {

					emailId = parent.getFax();
					String userId = parent.getId().getEmailAddress();
					alertevents.setContactNo("NAM");
					contactNo = parent.getContactNo();
					sendPushMessage(alertevents.getDescription(), contactNo,
							"", companyId);

					if (!isSMSDisabled) {
						alertevents.setContactNo(contactNo);
						sendSMS(companyId, branchId, userId, contactNo,
								alertevents);
					}

					/************* Email Validation *****************************************/

					if (!isEmailDisabled) {
						if (emailId != null)
							sendEmail(emailId, alertevents);
					}
				}
			}
			sktAlertsEJBRemote.insertAlertevents(alertevents, "update");
		} catch (Exception e) {
			LOGGER.error("SKTLog: SKTAlertsManager::persistAttendenceAlert::Exception="
					+ e.getMessage());
			e.printStackTrace();
		}

	}

	public void studentGeozoneCreation(Studentevent studentevent,
			VehicleComposite vehicleComposite, Routetrip routetrip,
			Studentdetails studentdetails) {
		// TODO Auto-generated method stub
		checkStudentTrackingAlerts.studentGeozoneCreation(studentevent,
				vehicleComposite, routetrip, studentdetails);
	}
}