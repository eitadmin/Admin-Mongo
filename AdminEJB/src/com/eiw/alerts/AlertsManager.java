package com.eiw.alerts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javapns.Push;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import javapns.notification.ResponsePacket;

import javax.ejb.EJB;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.websocket.Session;

import org.apache.log4j.BasicConfigurator;
import org.jboss.logging.Logger;
import org.json.JSONObject;

import com.eiw.client.dto.CompanyData;
import com.eiw.cron.AlertConfigEJB;
import com.eiw.cron.AlertDetails;
import com.eiw.device.ejb.VehicleComposite;
import com.eiw.device.handler.MeiTrackDeviceHandler;
import com.eiw.server.AmazonSMTPMail;
import com.eiw.server.EmailSendHttpClient;
import com.eiw.server.SMSSendHttpClient;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.ZohoSMTPMail;
import com.eiw.server.bo.BOFactory;
import com.eiw.server.bo.RuptelaDeviceMgmtRemote;
import com.eiw.server.bo.TeltonikaDeviceMgmtRemote;
import com.eiw.server.companyadminpu.Provider;
import com.eiw.server.companyadminpu.Pushnotificationdevices;
import com.eiw.server.companyadminpu.Smssent;
import com.eiw.server.fleettrackingpu.Alertconfig;
import com.eiw.server.fleettrackingpu.Companytrackdevice;
import com.eiw.server.fleettrackingpu.Heartbeatevent;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.VehicleHasIo;
import com.eiw.server.fleettrackingpu.Vehiclealerts;
import com.eiw.server.fleettrackingpu.Vehicleevent;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Sender;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class AlertsManager {
	int count = 0;

	@EJB
	AlertConfigEJB alertConfigEJB;

	// @PersistenceContext(unitName = "ltmsfleettrackingpu")
	// protected EntityManager em;
	// @PersistenceContext(unitName = "ltmscompanyadminpu")
	// protected EntityManager emCompany;

	public AlertsEJBRemote alertsEJBRemote = BOFactory.getAlertsEJBRemote();
	public RuptelaDeviceMgmtRemote ruptelaDeviceMgmtRemote = BOFactory
			.getRuptelaDeviceMgmtEJBRemote();
	public TeltonikaDeviceMgmtRemote teltonikaDeviceMgmtRemote = BOFactory
			.getTeltonikaDeviceMgmtEJBRemote();
	private List<Vehicleevent> vehicleevent = null;
	private static final String STR_WOKE_UP = "WOKEUP",
			STR_AI_RANGE = "_ai_range", STR_AI_LEAKAGE = "_ai_leakage",
			STR_ODOMETER = "_ODOMETER",
			STR_FUELCONSUMPTION = "FUELCONSUMPTION",
			STR_ENGINESTATUS = "ENGINESTATUS", STR_SEATBELT = "SEATBELT",
			STR_PANIC = "PANIC", STR_POWERCUT = "POWERCUT",
			STR_OVERSPEED = "OVERSPEED",
			STR_PREVENTIVE_MAINTAIANCE = "PreventiveMaintenance",
			STR_EVENTALERT = "EVENTALERT";
	static SortedMap<String, List<Vehicleevent>> hmap = new TreeMap<String, List<Vehicleevent>>();
	private static final String STR_IMMOVABLE = "IMMOVABLE",
			STR_CONCOX = "Concox", STR_WETRACK = "wetrack";
	private Heartbeatevent heartbeatevents;

	public static enum enumAlerts {
		DEVICETEMPERD, SOS, OVERSPEED, BATTERY, IDLE, TOWED, FATIGUE, OPTIME, GEOSOFT, ONENTER, ONLEAVE, GEOHARD, GEOREST, GEOPREF, GEOZONE, GEOFREE, GEOROAD, GEOLANDMARK, COOLERSENSOR1, COOLERSENSOR2, COOLERSENSOR3, STOP, HARSHBRAKING, DRIFT, ANTITHEFT, FUELCONSUMPTION, MOVEMENT, ENGINESTATUS, TEMPERATURESENSOR1, TEMPERATURESENSOR2, TEMPERATURESENSOR3, TEMPERATURESENSOR4, POWERCUT, SEATBELT, PANIC, BTTEMPERATURESENSOR1, BTTEMPERATURESENSOR2, BTTEMPERATURESENSOR3, BTTEMPERATURESENSOR4, BLEBATTERY1, BLEBATTERY2, BLEBATTERY3, BLEBATTERY4, BLEHUMIDITY1, BLEHUMIDITY2, BLEHUMIDITY3, BLEHUMIDITY4, HARSHACCELERATION, FUELALERT, EVENTALERT
	}

	private static final Logger LOGGER = Logger.getLogger("alerts");

	// Engine

	// Without engine
	CheckGeoFencingSoftware checkGeoFencingSoftware;
	CheckGeoFencingHardware checkGeoFencingHardware;
	CheckFreeFormGeo checkFreeFormGeo;
	CheckRoadGeo checkRoadGeo;
	CheckLandmarkGeo checkLandmarkGeo;

	String vin, timeZone, imei, manufacturer, assetType, deviceModel;
	Vehicle vehicle;
	int today, ioVal;
	boolean curTime, validity = false;
	boolean alertTimeFlag = false;
	String mode1 = null;

	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public AlertsManager() {
		checkGeoFencingSoftware = new CheckGeoFencingSoftware(this);
		checkGeoFencingHardware = new CheckGeoFencingHardware(this);
		checkFreeFormGeo = new CheckFreeFormGeo(this);
		checkRoadGeo = new CheckRoadGeo(this);
		checkLandmarkGeo = new CheckLandmarkGeo(this);

	}

	public void manageAlerts(List<Vehicleevent> vehicleEvents,
			VehicleComposite vehicleComposite) {
		// Get Vehicle
		this.vehicleevent = vehicleEvents;
		LOGGER.info("AlertsManager::manageAlertsEntered into this method::"
				+ "vehicleEvents" + vehicleEvents);
		vin = vehicleComposite.getVehicle().getVin();
		manufacturer = vehicleComposite.getCompanytrackDevice()
				.getCompanytrackdevicemodels().getId().getManufacturerName();
		imei = vehicleComposite.getCompanytrackDevice().getImeiNo();
		vehicle = vehicleComposite.getVehicle();
		// timeZone = alertsEJBRemote.getTimeZoneRegion(vin);
		timeZone = vehicleComposite.getTimeZoneRegion();
		today = alertsEJBRemote.getDay(vehicleEvents.get(0).getId()
				.getEventTimeStamp(), timeZone);
		String vehicleType = vehicle.getVehicletype().getVehicleType();

		CompanyData compSetttings = alertsEJBRemote.getCompanySettings(vehicle
				.getCompanyId());
		assetType = vehicleComposite.getVehicle().getVehiclemodel()
				.getRemarks();
		String plateNo = vehicle.getPlateNo();
		deviceModel = vehicleComposite.getCompanytrackDevice()
				.getCompanytrackdevicemodels().getId().getModelName();
		try {
			// Get Alertconfig
			// List<Alertconfig> alertConfigs = alertsEJBRemote
			// .getAlertConfig(vin);
			AlertDetails alertDetails = alertConfigEJB.map.get(vin);
			if (alertDetails != null) {
				Map<String, CheckAlerts> alertsMap = alertDetails
						.getAllAlertConfigDetails();
				List<VehicleHasIo> vehicleHasIo = null;
				if (alertsMap.isEmpty() || alertsMap == null) {
					LOGGER.info("No Alerts Configured for vin=" + vin);
				} else {
					for (Map.Entry<String, CheckAlerts> entry : alertsMap
							.entrySet()) {
						String key = entry.getKey();
						if (key.equalsIgnoreCase(enumAlerts.TEMPERATURESENSOR1
								.name())) {
							try {
								CheckAlerts checkAlerts = entry.getValue();
								checkAlerts.addAlertManager(this);
								checkAlerts.manageAlerts(vehicleEvents, vin,
										plateNo, vehicleHasIo, 1,
										vehicleComposite);
							} catch (Exception e) {
								LOGGER.error("AlertsManager COOLERSENSOR1 = "
										+ e);
							}
						} else if (key
								.equalsIgnoreCase(enumAlerts.TEMPERATURESENSOR2
										.name())) {
							try {
								CheckAlerts checkAlerts = entry.getValue();
								checkAlerts.addAlertManager(this);
								checkAlerts.manageAlerts(vehicleEvents, vin,
										plateNo, vehicleHasIo, 2,
										vehicleComposite);
							} catch (Exception e) {
								LOGGER.error("AlertsManager COOLERSENSOR2 = "
										+ e);
							}
						} else if (key
								.equalsIgnoreCase(enumAlerts.TEMPERATURESENSOR3
										.name())) {
							try {
								CheckAlerts checkAlerts = entry.getValue();
								checkAlerts.addAlertManager(this);
								checkAlerts.manageAlerts(vehicleEvents, vin,
										plateNo, vehicleHasIo, 3,
										vehicleComposite);
							} catch (Exception e) {
								LOGGER.error("AlertsManager COOLERSENSOR3 = "
										+ e);
							}
						} else if (key
								.equalsIgnoreCase(enumAlerts.TEMPERATURESENSOR4
										.name())) {
							try {
								CheckAlerts checkAlerts = entry.getValue();
								checkAlerts.addAlertManager(this);
								checkAlerts.manageAlerts(vehicleEvents, vin,
										plateNo, vehicleHasIo, 4,
										vehicleComposite);
							} catch (Exception e) {
								LOGGER.error("AlertsManager COOLERSENSOR3 = "
										+ e);
							}
						} else if (key
								.equalsIgnoreCase(enumAlerts.TOWED.name())) {
							try {
								// Added Sep-12
								boolean isTowed = checkTowedStatus(vin,
										vehicleType);
								if (isTowed) {
									CheckAlerts checkAlerts = entry.getValue();
									checkAlerts.addAlertManager(this);
									checkAlerts.manageAlerts(vehicleEvents,
											vin, plateNo, vehicleHasIo, 0,
											vehicleComposite);
								}
							} catch (Exception e) {
								LOGGER.error("AlertsManager TOWED = " + e);
							}
						} else if (key.equalsIgnoreCase(enumAlerts.ANTITHEFT
								.name())) {
							// if ((vehicleType.equalsIgnoreCase("Bikeapp") ||
							// vehicleType
							// .equalsIgnoreCase("GENERATOR"))) {
							try {

								LOGGER.info(" INFO Vin Entering in Antitheft Alert Confiq "
										+ vin);

								checkAntiTheft(vehicleEvents, vin, vehicleType,
										compSetttings.getAntiTheftDuration(),
										compSetttings.getAantiTheftCount(),
										assetType);

								if (hmap.get(vin) != null) {
									// if ((hmap.get(vin).size() == 5)) {
									if ((hmap.get(vin).size() == compSetttings
											.getAntiTheftTotalCount())) {

										LOGGER.info(" INFO Generating Alerts  when data size = totalCount(Configurable) "
												+ " before enterting checkantitheft class Vin number "
												+ vin);

										CheckAlerts checkAlerts = entry
												.getValue();
										checkAlerts.addAlertManager(this);
										checkAlerts.manageAlerts(hmap.get(vin),
												vin, plateNo, vehicleHasIo,
												compSetttings
														.getAantiTheftCount(),
												vehicleComposite);
										hmap.remove(vin);
									}
								}
							} catch (Exception e) {
								LOGGER.error("AlertsManager class method antitheft = "
										+ e);
								e.printStackTrace();
							}
							// }
						} else if (key.equalsIgnoreCase(enumAlerts.MOVEMENT
								.name())) {
							Companytrackdevice companytrackDevice = vehicleComposite
									.getCompanytrackDevice();
							if (companytrackDevice
									.getCompanytrackdevicemodels().getId()
									.getManufacturerName()
									.equalsIgnoreCase("concox")) {
								try {
									CheckAlerts checkAlerts = entry.getValue();
									checkAlerts.addAlertManager(this);
									checkAlerts.manageAlerts(vehicleEvents,
											vin, plateNo, vehicleHasIo, 0,
											vehicleComposite);
								} catch (Exception e) {
									LOGGER.error("AlertsManager method MOVEMENT Immovable="
											+ e);
								}
							} else {
								// if ((vehicleType.equalsIgnoreCase("Bikeapp")
								// ||
								// vehicleType
								// .equalsIgnoreCase("GENERATOR"))) {
								try {

									LOGGER.info(" INFO Vin Entering in Antitheft Alert Confiq "
											+ vin);

									checkAntiTheft(vehicleEvents, vin,
											vehicleType,
											compSetttings
													.getAntiTheftDuration(),
											compSetttings.getAantiTheftCount(),
											assetType);

									if (hmap.get(vin) != null) {
										// if ((hmap.get(vin).size() == 5)) {
										if ((hmap.get(vin).size() >= compSetttings
												.getAntiTheftTotalCount())) {

											LOGGER.info(" INFO Generating Alerts  when data size = totalCount(Configurable) "
													+ " before enterting checkantitheft class Vin number "
													+ vin);

											CheckAlerts checkAlerts = entry
													.getValue();
											checkAlerts.addAlertManager(this);
											checkAlerts
													.manageAlerts(
															hmap.get(vin),
															vin,
															plateNo,
															vehicleHasIo,
															compSetttings
																	.getAantiTheftCount(),
															vehicleComposite);
											hmap.remove(vin);
										}
									}
								} catch (Exception e) {
									LOGGER.error("AlertsManager class method antitheft = "
											+ e);
									e.printStackTrace();
								}
								// }
							}
						} else if (key.equalsIgnoreCase(enumAlerts.FUELALERT
								.name())) {
							try {
								String alerttype = "Fuel Sensor";
								vehicleHasIo = alertsEJBRemote
										.getVehicleHasIos(vin, alerttype);
								CheckAlerts checkAlerts = entry.getValue();
								checkAlerts.addAlertManager(this);
								checkAlerts.manageAlerts(vehicleEvents, vin,
										plateNo, vehicleHasIo, 0,
										vehicleComposite);
							} catch (Exception e) {
								LOGGER.error("AlertsManager Fuel Alert = " + e);
								e.printStackTrace();
							}
						} else if (key.endsWith(STR_AI_RANGE)) {
							try {
								String alerttype = key
										.replace(STR_AI_RANGE, "");
								vehicleHasIo = alertsEJBRemote
										.getVehicleHasIos(vin, alerttype);
								if (!vehicleHasIo.isEmpty()) {
									CheckAlerts checkAlerts = entry.getValue();
									checkAlerts.addAlertManager(this);
									checkAlerts.manageAlerts(vehicleEvents,
											vin, plateNo, vehicleHasIo, 0,
											vehicleComposite);

								}
							} catch (Exception e) {
								LOGGER.error("AlertsManager STR_AI_RANGE = "
										+ e);
							}
						} else if (key.endsWith(STR_AI_LEAKAGE)) {
							try {
								String alerttype = key.replace(STR_AI_LEAKAGE,
										"");
								CheckAnalogInputLeakage values = (CheckAnalogInputLeakage) entry
										.getValue();

								vehicleHasIo = alertsEJBRemote
										.getVehicleHasIos(vin, alerttype);
								if (!vehicleHasIo.isEmpty()) {
									CheckAlerts checkAlerts = entry.getValue();
									checkAlerts.addAlertManager(this);
									checkAlerts.manageAlerts(vehicleEvents,
											vin, plateNo, vehicleHasIo, 0,
											vehicleComposite);
								}
							} catch (Exception e) {
								LOGGER.error("AlertsManager STR_AI_LEAKAGE = "
										+ e);
							}
						} else if (key.equalsIgnoreCase(enumAlerts.ENGINESTATUS
								.name())) {
							if (!manufacturer.equalsIgnoreCase(STR_CONCOX)) {
								try {
									CheckAlerts checkAlerts = entry.getValue();
									checkAlerts.addAlertManager(this);
									checkAlerts.manageAlerts(vehicleEvents,
											vin, plateNo, vehicleHasIo, 0,
											vehicleComposite);
								} catch (Exception e) {
									LOGGER.error("AlertsManager ENGINESTATUS= "
											+ e);
								}
							}
						} else if (key.equalsIgnoreCase(enumAlerts.POWERCUT
								.name())) {
							if (deviceModel.equalsIgnoreCase(STR_WETRACK)
									|| manufacturer
											.equalsIgnoreCase("Teltonika")) {
								try {
									CheckAlerts checkAlerts = entry.getValue();
									checkAlerts.addAlertManager(this);
									checkAlerts.manageAlerts(vehicleEvents,
											vin, plateNo, vehicleHasIo, 0,
											vehicleComposite);
								} catch (Exception e) {
									LOGGER.error("AlertsManager POWERCUT= " + e);
								}
							}
						} else if (key.equalsIgnoreCase(STR_SEATBELT)) {
							try {
								vehicleHasIo = alertsEJBRemote
										.getVehicleHasIos(vin, key);
								CheckAlerts checkAlerts = entry.getValue();
								checkAlerts.addAlertManager(this);
								checkAlerts.manageAlerts(vehicleEvents, vin,
										plateNo, vehicleHasIo, 0,
										vehicleComposite);
							} catch (Exception e) {
								LOGGER.error("AlertsManager SEATBELT= " + e);
							}
						} else if (key.equalsIgnoreCase(STR_PANIC)) {
							try {
								vehicleHasIo = alertsEJBRemote
										.getVehicleHasIos(vin, "Panic Alert");
								CheckAlerts checkAlerts = entry.getValue();
								checkAlerts.addAlertManager(this);
								checkAlerts.manageAlerts(vehicleEvents, vin,
										plateNo, vehicleHasIo, 0,
										vehicleComposite);
							} catch (Exception e) {
								LOGGER.error("AlertsManager Panic= " + e);
							}
						} else if (key
								.equalsIgnoreCase(enumAlerts.BTTEMPERATURESENSOR1
										.name())) {
							try {
								CheckAlerts checkAlerts = entry.getValue();
								checkAlerts.addAlertManager(this);
								checkAlerts.manageAlerts(vehicleEvents, vin,
										plateNo, vehicleHasIo, 1,
										vehicleComposite);
							} catch (Exception e) {
								LOGGER.error("AlertsManager BTCOOLERSENSOR1 = "
										+ e);
							}
						}

						else if (key
								.equalsIgnoreCase(enumAlerts.BTTEMPERATURESENSOR2
										.name())) {
							try {
								CheckAlerts checkAlerts = entry.getValue();
								checkAlerts.addAlertManager(this);
								checkAlerts.manageAlerts(vehicleEvents, vin,
										plateNo, vehicleHasIo, 2,
										vehicleComposite);
							} catch (Exception e) {
								LOGGER.error("AlertsManager BTCOOLERSENSOR2 = "
										+ e);
							}
						} else if (key
								.equalsIgnoreCase(enumAlerts.BTTEMPERATURESENSOR3
										.name())) {
							try {
								CheckAlerts checkAlerts = entry.getValue();
								checkAlerts.addAlertManager(this);
								checkAlerts.manageAlerts(vehicleEvents, vin,
										plateNo, vehicleHasIo, 3,
										vehicleComposite);
							} catch (Exception e) {
								LOGGER.error("AlertsManager BTCOOLERSENSOR3 = "
										+ e);
							}
						} else if (key
								.equalsIgnoreCase(enumAlerts.BTTEMPERATURESENSOR4
										.name())) {
							try {
								CheckAlerts checkAlerts = entry.getValue();
								checkAlerts.addAlertManager(this);
								checkAlerts.manageAlerts(vehicleEvents, vin,
										plateNo, vehicleHasIo, 4,
										vehicleComposite);
							} catch (Exception e) {
								LOGGER.error("AlertsManager BTCOOLERSENSOR4 = "
										+ e);
							}
						}

						else if (key.equalsIgnoreCase(enumAlerts.BLEBATTERY1
								.name())) {
							try {
								CheckAlerts checkAlerts = entry.getValue();
								checkAlerts.addAlertManager(this);
								checkAlerts.manageAlerts(vehicleEvents, vin,
										plateNo, vehicleHasIo, 1,
										vehicleComposite);
							} catch (Exception e) {
								LOGGER.error("AlertsManager BTCOOLERSENSORFORBATTERY1 = "
										+ e);
							}
						} else if (key.equalsIgnoreCase(enumAlerts.BLEBATTERY2
								.name())) {
							try {
								CheckAlerts checkAlerts = entry.getValue();
								checkAlerts.addAlertManager(this);
								checkAlerts.manageAlerts(vehicleEvents, vin,
										plateNo, vehicleHasIo, 2,
										vehicleComposite);
							} catch (Exception e) {
								LOGGER.error("AlertsManager BTCOOLERSENSORFORBATTERY2 = "
										+ e);
							}
						} else if (key.equalsIgnoreCase(enumAlerts.BLEBATTERY3
								.name())) {
							try {
								CheckAlerts checkAlerts = entry.getValue();
								checkAlerts.addAlertManager(this);
								checkAlerts.manageAlerts(vehicleEvents, vin,
										plateNo, vehicleHasIo, 3,
										vehicleComposite);
							} catch (Exception e) {
								LOGGER.error("AlertsManager BTCOOLERSENSORFORBATTERY3 = "
										+ e);
							}
						} else if (key.equalsIgnoreCase(enumAlerts.BLEBATTERY4
								.name())) {
							try {
								CheckAlerts checkAlerts = entry.getValue();
								checkAlerts.addAlertManager(this);
								checkAlerts.manageAlerts(vehicleEvents, vin,
										plateNo, vehicleHasIo, 4,
										vehicleComposite);
							} catch (Exception e) {
								LOGGER.error("AlertsManager BTCOOLERSENSORFORBATTERY4 = "
										+ e);
							}
						}

						else if (key.equalsIgnoreCase(enumAlerts.BLEHUMIDITY1
								.name())) {
							try {
								CheckAlerts checkAlerts = entry.getValue();
								checkAlerts.addAlertManager(this);
								checkAlerts.manageAlerts(vehicleEvents, vin,
										plateNo, vehicleHasIo, 1,
										vehicleComposite);
							} catch (Exception e) {
								LOGGER.error("AlertsManager BTCOOLERSENSORFORHUMIDITY1 = "
										+ e);
							}
						} else if (key.equalsIgnoreCase(enumAlerts.BLEHUMIDITY2
								.name())) {
							try {
								CheckAlerts checkAlerts = entry.getValue();
								checkAlerts.addAlertManager(this);
								checkAlerts.manageAlerts(vehicleEvents, vin,
										plateNo, vehicleHasIo, 2,
										vehicleComposite);
							} catch (Exception e) {
								LOGGER.error("AlertsManager BTCOOLERSENSORFORHUMIDITY2 = "
										+ e);
							}
						} else if (key.equalsIgnoreCase(enumAlerts.BLEHUMIDITY3
								.name())) {
							try {
								CheckAlerts checkAlerts = entry.getValue();
								checkAlerts.addAlertManager(this);
								checkAlerts.manageAlerts(vehicleEvents, vin,
										plateNo, vehicleHasIo, 3,
										vehicleComposite);
							} catch (Exception e) {
								LOGGER.error("AlertsManager BTCOOLERSENSORFORHUMIDITY3 = "
										+ e);
							}
						} else if (key.equalsIgnoreCase(enumAlerts.BLEHUMIDITY4
								.name())) {
							try {
								CheckAlerts checkAlerts = entry.getValue();
								checkAlerts.addAlertManager(this);
								checkAlerts.manageAlerts(vehicleEvents, vin,
										plateNo, vehicleHasIo, 4,
										vehicleComposite);
							} catch (Exception e) {
								LOGGER.error("AlertsManager BTCOOLERSENSORFORHUMIDITY4 = "
										+ e);
							}
						} else if (key
								.equalsIgnoreCase(enumAlerts.DEVICETEMPERD
										.name())) {
							try {
								CheckAlerts checkAlerts = entry.getValue();
								checkAlerts.addAlertManager(this);
								checkAlerts.manageAlerts(vehicleEvents, vin,
										plateNo, vehicleHasIo, 4,
										vehicleComposite);
							} catch (Exception e) {
								LOGGER.error("AlertsManager BTCOOLERSENSORFORHUMIDITY4 = "
										+ e);
							}
						} else if (key.equalsIgnoreCase(enumAlerts.BATTERY
								.name())) {
							try {
								CheckAlerts checkAlerts = entry.getValue();
								checkAlerts.addAlertManager(this);
								checkAlerts.manageAlerts(vehicleEvents, vin,
										plateNo, vehicleHasIo, 4,
										vehicleComposite);
							} catch (Exception e) {
								LOGGER.error("AlertsManager Battery= " + e);
							}
						}

						else {
							try {
								CheckAlerts checkAlerts = entry.getValue();
								checkAlerts.addAlertManager(this);
								checkAlerts.manageAlerts(vehicleEvents, vin,
										plateNo, vehicleHasIo, 0,
										vehicleComposite);
							} catch (Exception e) {
								LOGGER.error("AlertsManager else part key = "
										+ key + " exception" + e);
							}
						}

					}

				}
			}
			// Comment By Manigandan -->not working on Ticket_Management
			// Map<String, String> keyMap = new HashMap<String, String>();
			// keyMap.put(vehicleComposite.getVehicle().getCompanyId(),
			// "ticketManagementEnabled");
			// String value = null;
			// value = AlertConfigEJB.companySettingMap.get(keyMap);
			// if (value != null && value.equalsIgnoreCase("true")) {
			// try {
			// List<VehicleHasIo> vehicleHasIo = alertsEJBRemote
			// .getVehicleHasIos(vin, "Panic Alert");
			// CheckAlerts checkAlerts = new CheckPanicAlert(this);
			// checkAlerts.manageAlerts(vehicleEvents, vin, plateNo,
			// vehicleHasIo, 1, vehicleComposite);
			// } catch (Exception e) {
			// LOGGER.error("AlertsManager Panic= " + e);
			// }
			// }
			try {
				checkGeoFencingSoftware.manageAlert(vehicleEvents, vehicle,
						plateNo);
			} catch (Exception e) {
				LOGGER.error("checkGeoFencingSoftware =" + e);
			}
			// try {
			// checkGeoFencingHardware.manageAlert(vehicleEvents, vehicle,
			// plateNo);
			// } catch (Exception e) {
			// LOGGER.error("checkGeoFencingHardware =" + e);
			// }

			try {
				checkFreeFormGeo.manageAlert(vehicleEvents, vehicle, plateNo,
						timeZone, null);
			} catch (Exception e) {
				LOGGER.error("checkFreeFormGeo =" + e);
			}
			try {
				checkRoadGeo.manageAlert(vehicleEvents, vehicle, plateNo);
			} catch (Exception e) {
				LOGGER.error("checkRoadGeo =" + e);
			}
			try {
				checkLandmarkGeo.manageAlert(vehicleEvents, vehicle, plateNo);
			} catch (Exception e) {
				LOGGER.error("checkLandmarkGeo =" + e);
			}
		} catch (Exception e) {
			LOGGER.error("Check Alerts Method for vin ="
					+ vehicleEvents.get(0).getId().getVin() + "   \n"
					+ e.getMessage());
			e.printStackTrace();
		}
		LOGGER.info("AlertsManager::manageAlerts::Leaving from this method successfully");
	}

	public void manageHbAlerts(Heartbeatevent heartbeatevent,
			VehicleComposite vehicleComposite) {
		this.heartbeatevents = heartbeatevent;
		LOGGER.info("AlertsManager::manageHbAlerts Entered into this method");
		vin = vehicleComposite.getVehicle().getVin();
		timeZone = vehicleComposite.getTimeZoneRegion();
		String plateNo = vehicleComposite.getVehicle().getPlateNo();
		LOGGER.info("vin: " + vin + " plateNo: " + plateNo);
		deviceModel = vehicleComposite.getCompanytrackDevice()
				.getCompanytrackdevicemodels().getId().getModelName();
		try {
			AlertDetails alertDetails = alertConfigEJB.map.get(vin);
			if (alertDetails != null) {
				Map<String, CheckAlerts> alertsMap = alertDetails
						.getAllAlertConfigDetails();
				if (alertsMap.isEmpty() || alertsMap == null) {
					LOGGER.info("No Alerts Configured for vin=" + vin);
				} else {
					for (Map.Entry<String, CheckAlerts> entry : alertsMap
							.entrySet()) {
						String key = entry.getKey();
						if (key.equalsIgnoreCase(enumAlerts.ENGINESTATUS.name())) {
							try {
								CheckAlerts checkAlerts = entry.getValue();
								checkAlerts.addAlertManager(this);
								checkAlerts.manageHbAlerts(heartbeatevent, vin,
										plateNo, vehicleComposite);
							} catch (Exception e) {
								LOGGER.error("AlertsManager manageHbAlerts ENGINESTATUS= "
										+ e);
							}

						} else if (key.equalsIgnoreCase(enumAlerts.POWERCUT
								.name())) {
							if (!deviceModel.equalsIgnoreCase(STR_WETRACK)) {
								try {
									CheckAlerts checkAlerts = entry.getValue();
									checkAlerts.addAlertManager(this);
									checkAlerts.manageHbAlerts(heartbeatevent,
											vin, plateNo, vehicleComposite);
								} catch (Exception e) {
									LOGGER.error("AlertsManager manageHbAlerts POWERCUT= "
											+ e);
								}
							}

						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("CheckAlerts manageHbAlerts:" + e);
			e.printStackTrace();
		}
	}

	public Date persistHbAlert(Alertconfig alertConfig,
			List<Vehiclealerts> vehiclealerts, Date lastUpdatedTime) {
		LOGGER.info("AlertsManager::persistHbAlert::Entered into this method::"
				+ "lastUpdatedTime" + lastUpdatedTime);
		if (timeZone == null) {
			timeZone = alertsEJBRemote.getTimeZoneRegion(alertConfig.getId()
					.getVin());
		}
		if (vin == null) {
			vin = alertConfig.getId().getVin();
		}
		Date currentStartDateTime = TimeZoneUtil.getDateTimeZone(new Date(),
				timeZone);
		String[] through = alertConfig.getThroughSms().split("#");
		String through1, through2;
		through1 = through[0];
		if (through.length > 1) {
			through2 = through[1];
		} else {
			through2 = "0";
		}
		boolean isSMS1Disabled = (through1.equalsIgnoreCase("0")) ? true
				: false;
		boolean isSMS2Disabled = (through2.equalsIgnoreCase("0")) ? true
				: false;
		String[] throughEmail = alertConfig.getThroughEmail().split("#");
		String throughEmail1, throughEmail2;
		throughEmail1 = throughEmail[0];
		if (throughEmail.length > 1) {
			throughEmail2 = throughEmail[1];
		} else {
			throughEmail2 = "0";
		}
		boolean isEmail1Disabled = (throughEmail1.equalsIgnoreCase("0")) ? true
				: false;
		boolean isEmail2Disabled = (throughEmail2.equalsIgnoreCase("0")) ? true
				: false;
		boolean isAlertAlreadySent = false, isEmailAlreadySent = false;

		currentStartDateTime = lastUpdatedTime;
		curTime = alertsEJBRemote.getAlertTime(heartbeatevents,
				alertConfig.getAlertTime());
		if (alertConfig.getValidityExp() != null) {
			validity = alertsEJBRemote.getAlertValidity(heartbeatevents,
					alertConfig.getValidityExp());
		} else {
			validity = true;
		}
		if (curTime && validity) {
			alertTimeFlag = true;
		}
		if (!isAlertAlreadySent) {
			for (Vehiclealerts vehiclealert : vehiclealerts) {
				if (timeZone != null) {
					vehiclealert.setServerTimeStamp(TimeZoneUtil
							.getDateTimeZone(new Date(), timeZone));
				}
				if (alertTimeFlag) {
					callSMSService(vehiclealert, alertConfig, isSMS1Disabled,
							isSMS2Disabled);
				}
			}
		}

		if (!isEmailAlreadySent) {
			for (Vehiclealerts vehiclealert : vehiclealerts) {
				if (timeZone != null) {
					vehiclealert.setServerTimeStamp(TimeZoneUtil
							.getDateTimeZone(new Date(), timeZone));
				}
				if (alertTimeFlag) {
					callEmailService(vehiclealert, alertConfig,
							isEmail1Disabled, isEmail2Disabled);
				}
			}
		}
		alertTimeFlag = false;
		LOGGER.info("AlertsManager::persistHbAlert::Leaving from this method successfully");
		return currentStartDateTime;
	}

	public Date persistVehicleAlert(Alertconfig alertConfig,
			List<Vehiclealerts> vehiclealerts, Date lastUpdatedTime) {

		System.out.println("lastUpdatedTime" + lastUpdatedTime);
		LOGGER.info("AlertsManager::persistVehicleAlert::Entered into this method::"
				+ "alertConfig" + alertConfig + "vehiclealerts" + vehiclealerts);

		if (timeZone == null) {
			timeZone = alertsEJBRemote.getTimeZoneRegion(alertConfig.getId()
					.getVin());
		}
		if (vin == null) {
			vin = alertConfig.getId().getVin();
		}
		if ((alertConfig.getId().getAlertType().equalsIgnoreCase("OVERSPEED")
				|| alertConfig.getId().getAlertType()
						.equalsIgnoreCase(enumAlerts.TEMPERATURESENSOR1.name())
				|| alertConfig.getId().getAlertType()
						.equalsIgnoreCase(enumAlerts.TEMPERATURESENSOR2.name())
				|| alertConfig.getId().getAlertType()
						.equalsIgnoreCase(enumAlerts.TEMPERATURESENSOR3.name()) || alertConfig
				.getId().getAlertType()
				.equalsIgnoreCase(enumAlerts.TEMPERATURESENSOR4.name()))
				&& (alertConfig.getSubAlertType() != null)
				&& (alertConfig.getSubAlertType().equalsIgnoreCase("BUZZER") || alertConfig
						.getSubAlertType().equalsIgnoreCase(
								"TEMPERATURESENSOR_BUZZER"))) {
			try {
				if (manufacturer.equalsIgnoreCase("Ruptela")) {
					ruptelaDeviceMgmtRemote.sendCommand("12345", imei,
							"buzzerOn");
					Thread.sleep(5000);
					ruptelaDeviceMgmtRemote.sendCommand("12345", imei,
							"buzzerOff");
				} else if (manufacturer.equalsIgnoreCase("Teltonika")) {
					teltonikaDeviceMgmtRemote.sendCommand("12345", imei,
							deviceModel, "buzzerOn");
					Thread.sleep(5000);
					teltonikaDeviceMgmtRemote.sendCommand("12345", imei,
							deviceModel, "buzzerOff");
				}
			} catch (Exception e) {
				LOGGER.error("Buzzer :" + e);
			}
		}
		String interval = alertConfig.getRefInterval();

		Date currentStartDateTime = TimeZoneUtil.getDateTimeZone(new Date(),
				timeZone);
		String strexpectedTime = TimeZoneUtil
				.getTimeINYYYYMMddss(currentStartDateTime);

		String strLastAlertDateTime = TimeZoneUtil
				.getTimeINYYYYMMddss(lastUpdatedTime);

		String[] through = alertConfig.getThroughSms().split("#");
		String through1, through2;
		through1 = through[0];
		if (through.length > 1) {
			through2 = through[1];
		} else {
			through2 = "0";
		}
		boolean isSMS1Disabled = (through1.equalsIgnoreCase("0")) ? true
				: false;
		boolean isSMS2Disabled = (through2.equalsIgnoreCase("0")) ? true
				: false;
		String[] throughEmail = alertConfig.getThroughEmail().split("#");
		String throughEmail1, throughEmail2;
		throughEmail1 = throughEmail[0];
		if (throughEmail.length > 1) {
			throughEmail2 = throughEmail[1];
		} else {
			throughEmail2 = "0";
		}
		boolean isEmail1Disabled = (throughEmail1.equalsIgnoreCase("0")) ? true
				: false;
		boolean isEmail2Disabled = (throughEmail2.equalsIgnoreCase("0")) ? true
				: false;
		boolean isAlertAlreadySent = false, isEmailAlreadySent = false;

		try {
			Date dateLastAlertDateTime = sdfTime.parse(strexpectedTime);
			Date datecurrentDateTime = sdfTime.parse(strLastAlertDateTime);

			Date dateLastAlertDate = sdfDate.parse(strexpectedTime);
			Date datecurrentDate = sdfDate.parse(strLastAlertDateTime);

			long diff = dateLastAlertDate.getTime() - datecurrentDate.getTime();

			long diffDays = diff / (24 * 60 * 60 * 1000);

			long result = ((dateLastAlertDateTime.getTime() / 60000) - (datecurrentDateTime
					.getTime() / 60000));

			long pretime = Long.parseLong(interval);

			String alertType = alertConfig.getId().getAlertType();
			if (alertType.equalsIgnoreCase(STR_WOKE_UP)
					|| alertType.endsWith(STR_ODOMETER)
					|| alertType.equalsIgnoreCase(STR_FUELCONSUMPTION)
					|| alertType.equalsIgnoreCase(STR_PREVENTIVE_MAINTAIANCE)) {

				if (diffDays == 0) {
					isEmailAlreadySent = true;
					isAlertAlreadySent = true;
					currentStartDateTime = lastUpdatedTime;

				} else {
					isEmailAlreadySent = false;
					isAlertAlreadySent = false;
				}

			} else if ((alertType.equalsIgnoreCase(STR_WOKE_UP) || alertType
					.equalsIgnoreCase(STR_PREVENTIVE_MAINTAIANCE))
					&& isAlertAlreadySent) {
				isEmailAlreadySent = true;
				isAlertAlreadySent = true;
				currentStartDateTime = lastUpdatedTime;
			} else if (alertType.equalsIgnoreCase(STR_ENGINESTATUS)) {
				isEmailAlreadySent = false;
				isAlertAlreadySent = false;
				currentStartDateTime = lastUpdatedTime;
			} else if (alertType.equalsIgnoreCase(STR_POWERCUT)) {
				isEmailAlreadySent = false;
				isAlertAlreadySent = false;
				currentStartDateTime = lastUpdatedTime;
			} else if (alertType.equalsIgnoreCase("MOVEMENT")) {
				isEmailAlreadySent = false;
				isAlertAlreadySent = false;
				currentStartDateTime = lastUpdatedTime;
			} else if (alertType.equalsIgnoreCase(STR_EVENTALERT)) {
				isEmailAlreadySent = false;
				isAlertAlreadySent = false;
				currentStartDateTime = lastUpdatedTime;
			} else {
				if (result > pretime) {
					isEmailAlreadySent = false;
					isAlertAlreadySent = false;
				} else {
					isEmailAlreadySent = true;
					isAlertAlreadySent = true;
					currentStartDateTime = lastUpdatedTime;
				}

			}
			// else if (alertType.equalsIgnoreCase(STR_WOKE_UP)
			// && isAlertAlreadySent) {
			// isEmailAlreadySent = true;
			// }
			// }

		} catch (ParseException e) {
			e.printStackTrace();
		}

		// if (alertConfig.getId() != null) {
		// String alertType = alertConfig.getId().getAlertType();
		// // chk for last alert sent with interval
		// isAlertAlreadySent = alertsEJBRemote.isAlertAlreadySent(vin,
		// expectedTime, interval, alertType);
		//
		// // chk for email already sent(only once/day as per Mahdi's request.
		// // Not depend on Interval)
		// if (!alertType.equalsIgnoreCase(STR_WOKE_UP)) {
		// isEmailAlreadySent = alertsEJBRemote.isAlertAlreadySent(vin,
		// expectedTime, "EMAIL", alertType);
		// } else if (alertType.equalsIgnoreCase(STR_WOKE_UP)
		// && isAlertAlreadySent) {
		// isEmailAlreadySent = true;
		// }
		// }
		if (alertConfig.getId().getAlertType()
				.equalsIgnoreCase(STR_FUELCONSUMPTION)) {
			curTime = true;
		} else {
			curTime = alertsEJBRemote.getTime(vehicleevent,
					alertConfig.getAlertTime());
		}
		if (alertConfig.getId().getAlertType().equalsIgnoreCase(STR_WOKE_UP)
				|| alertConfig.getId().getAlertType()
						.equalsIgnoreCase(STR_PREVENTIVE_MAINTAIANCE)) {
			if (!curTime) {
				currentStartDateTime = lastUpdatedTime;
			}
		}
		if (alertConfig.getValidityExp() != null) {
			validity = alertsEJBRemote.getValidity(vehicleevent,
					alertConfig.getValidityExp());
		} else {
			validity = true;
		}
		if (curTime && validity) {
			alertTimeFlag = true;
		}

		if (!isAlertAlreadySent) {
			for (Vehiclealerts vehiclealert : vehiclealerts) {
				if (timeZone != null) {
					vehiclealert.setServerTimeStamp(TimeZoneUtil
							.getDateTimeZone(new Date(), timeZone));
				}
				// LOGGER.error("Checking alertTimeFlag "+alertTimeFlag);
				if (alertTimeFlag) {
					try {
						for (Map.Entry<Session, String> e : alertConfigEJB.alertsSessions
								.entrySet()) {
							if (e.getValue().equalsIgnoreCase(
									alertConfig.getId().getVin())) {
								Session session = e.getKey();

								if (session.isOpen()) {
									for (Vehiclealerts ve : vehiclealerts) {
										JSONObject data = new JSONObject();
										data.put("vin", ve.getVin());
										data.put("alertType", ve.getAlerttype());
										data.put("description",
												ve.getDescription());
										data.put(
												"alertCount",
												alertsEJBRemote
														.getOverAllAlertCount(alertConfig
																.getId()
																.getCompanyId()));
										session.getBasicRemote().sendText(
												data.toString());
									}
								} else {
									alertConfigEJB.alertsSessions
											.remove(session);
								}
							}
						}
					} catch (Exception e1) {
						LOGGER.error("Error While pushing AlertManager socket as "
								+ e1);
						e1.printStackTrace();
					}
					callSMSService(vehiclealert, alertConfig, isSMS1Disabled,
							isSMS2Disabled);
				}
			}
		}

		if (!isEmailAlreadySent) {
			for (Vehiclealerts vehiclealert : vehiclealerts) {
				if (timeZone != null) {
					vehiclealert.setServerTimeStamp(TimeZoneUtil
							.getDateTimeZone(new Date(), timeZone));
				}
				if (alertTimeFlag) {
					callEmailService(vehiclealert, alertConfig,
							isEmail1Disabled, isEmail2Disabled);
				}
			}
		}
		alertTimeFlag = false;
		LOGGER.info("AlertsManager::persistVehicleAlert::Leaving from this method successfully");
		return currentStartDateTime;

	}

	public void persistVehicleAlert(Alertconfig alertConfig,
			List<Vehiclealerts> vehiclealerts) {
		persistVehicleAlert(alertConfig, vehiclealerts, mode1);
	}

	public void persistVehicleAlert(Alertconfig alertConfig,
			List<Vehiclealerts> vehiclealerts, String mode) {
		LOGGER.info("AlertsManager::persistVehicleAlert::Entered into this method::"
				+ "alertConfig" + alertConfig + "vehiclealerts" + vehiclealerts);

		if (timeZone == null) {
			timeZone = alertsEJBRemote.getTimeZoneRegion(alertConfig.getId()
					.getVin());
		}
		if (vin == null) {
			vin = alertConfig.getId().getVin();
		}
		String interval = alertConfig.getRefInterval();
		Date startDate = TimeZoneUtil.getDateTimeZone(new Date(), timeZone);
		String expectedTime = TimeZoneUtil.getTimeINYYYYMMddss(startDate);
		String[] through = alertConfig.getThroughSms().split("#");
		String through1, through2;
		through1 = through[0];
		if (through.length > 1) {
			through2 = through[1];
		} else {
			through2 = "0";
		}
		boolean isSMS1Disabled = (through1.equalsIgnoreCase("0")) ? true
				: false;
		boolean isSMS2Disabled = (through2.equalsIgnoreCase("0")) ? true
				: false;
		String[] throughEmail = alertConfig.getThroughEmail().split("#");
		String throughEmail1, throughEmail2;
		throughEmail1 = throughEmail[0];
		if (throughEmail.length > 1) {
			throughEmail2 = throughEmail[1];
		} else {
			throughEmail2 = "0";
		}
		boolean isEmail1Disabled = (throughEmail1.equalsIgnoreCase("0")) ? true
				: false;
		boolean isEmail2Disabled = (throughEmail2.equalsIgnoreCase("0")) ? true
				: false;
		boolean isAlertAlreadySent = false, isEmailAlreadySent = false;
		if (alertConfig.getId() != null) {
			String alertType = alertConfig.getId().getAlertType();
			// chk for last alert sent with interval
			isAlertAlreadySent = alertsEJBRemote.isAlertAlreadySent(vin,
					expectedTime, interval, alertType);

			// chk for email already sent(only once/day as per Mahdi's request.
			// Not depend on Interval)
			if (!alertType.equalsIgnoreCase(STR_WOKE_UP)
					&& !alertType.equalsIgnoreCase(STR_PREVENTIVE_MAINTAIANCE)) {
				isEmailAlreadySent = alertsEJBRemote.isAlertAlreadySent(vin,
						expectedTime, "EMAIL", alertType);
			} else if ((alertType.equalsIgnoreCase(STR_WOKE_UP) || alertType
					.equalsIgnoreCase(STR_PREVENTIVE_MAINTAIANCE))
					&& isAlertAlreadySent) {
				isEmailAlreadySent = true;
			}
		}

		if (!isAlertAlreadySent) {
			for (Vehiclealerts vehiclealert : vehiclealerts) {
				if (timeZone != null) {
					vehiclealert.setServerTimeStamp(TimeZoneUtil
							.getDateTimeZone(new Date(), timeZone));
				}
				callSMSService(vehiclealert, alertConfig, isSMS1Disabled,
						isSMS2Disabled, mode);
			}
		}

		if (!isEmailAlreadySent) {
			for (Vehiclealerts vehiclealert : vehiclealerts) {
				if (timeZone != null) {
					vehiclealert.setServerTimeStamp(TimeZoneUtil
							.getDateTimeZone(new Date(), timeZone));
				}
				callEmailService(vehiclealert, alertConfig, isEmail1Disabled,
						isEmail2Disabled);
			}
		}
		LOGGER.info("AlertsManager::persistVehicleAlert::Leaving from this method successfully");
	}

	public void callSMSService(Vehiclealerts vehiclealert,
			Alertconfig alertConfig, boolean isSMS1Disabled,
			boolean isSMS2Disabled) {
		callSMSService(vehiclealert, alertConfig, isSMS1Disabled,
				isSMS2Disabled, null);
	}

	public void callSMSService(Vehiclealerts vehiclealert,
			Alertconfig alertConfig, boolean isSMS1Disabled,
			boolean isSMS2Disabled, String mode) {
		/************* SMS Validation *****************************************/
		Provider provider = alertsEJBRemote.getProviderDetails(alertConfig
				.getId().getCompanyId());
		String smsHttpStatus = "not sent";
		boolean smsStatus = false;
		boolean isSmsBalAvail;
		String operatorName = alertsEJBRemote.getOperatorName(alertConfig
				.getId().getVin());
		if (operatorName != null) {
			vehiclealert.setDescription(vehiclealert.getDescription()
					+ " Operator ID: " + operatorName);
		}
		String[] through = alertConfig.getThroughSms().split("#");
		for (int i = 0; i < through.length; i++) {
			if (through[i].equalsIgnoreCase("1")) {
				String[] mobile = alertConfig.getSmsNumber().split("#");
				if (mobile[i] != null) {
					isSmsBalAvail = alertsEJBRemote.isSmsBalAvail(vehiclealert);
					if (isSmsBalAvail) {
						smsHttpStatus = SMSSendHttpClient.sendSMS(provider
								.getSmsApi(), mobile[i], vehiclealert
								.getDescription().replaceAll("%0D", " "));

						if (smsHttpStatus.equalsIgnoreCase("OK")) {
							smsStatus = true;
							// insert sms sent table
							try {
								alertsEJBRemote.insertSMS1(prepareSmsSent(
										vehiclealert, alertConfig.getId()
												.getUserId()));
							} catch (Exception e) {
								LOGGER.error("I am here at exp" + e);
							}
						}
					}
				}
			}
		}
		/************* SMS Validation *****************************************/
		// update vehiclealerts smssent status and insert
		vehiclealert.setSmsstatus(smsStatus);
		alertsEJBRemote.insertVehicleAlert(vehiclealert, mode);
		// LOGGER.error("InspectInspect : vehicleAlert"
		// + vehiclealert.getDescription());
		if (mode == null) {
			pushNotification(alertConfig.getId().getCompanyId(), alertConfig
					.getId().getUserId(), vehiclealert.getDescription()
					.replaceAll("%0D", " "));
		}

	}

	public void callEmailService(Vehiclealerts vehiclealert,
			Alertconfig alertConfig, boolean isEmail1Disabled,
			boolean isEmail2Disabled) {
		/*************
		 * Email Validation
		 *****************************************/
		Provider provider = alertsEJBRemote.getProviderDetails(alertConfig
				.getId().getCompanyId());
		String emailHttpStatus = "failure";
		String[] through = alertConfig.getThroughEmail().split("#");
		for (int i = 0; i < through.length; i++) {
			if (through[i].equalsIgnoreCase("1")) {
				String[] email = alertConfig.getEmailAddress().split("#");
				if (email[i] != null) {
					if (alertsEJBRemote.isEmailValidOrNot(email[i])) {
						if (provider.getMailMode().equalsIgnoreCase("gmail")) {
							emailHttpStatus = EmailSendHttpClient.send(
									email[i],
									"Alert "
											+ alertConfig.getId()
													.getAlertType(),
									vehiclealert.getDescription().replaceAll(
											"%0D", " "),
									provider.getAlertMailId(),
									provider.getAlertMailPasscode());
						} else if (provider.getMailMode().equalsIgnoreCase(
								"amazon")) {
							AmazonSMTPMail amazonSMTPMail = new AmazonSMTPMail();
							try {
								amazonSMTPMail.sendEmail(provider
										.getAmazonVerifiedFromEmail(),
										email[i], "Alert "
												+ alertConfig.getId()
														.getAlertType(),
										vehiclealert.getDescription()
												.replaceAll("%0D", " "),
										provider.getAmazonSmtpUserName(),
										provider.getAmazonSmtpPassword(),
										provider.getAmazonHostAddress(),
										provider.getAmazonPort());
							} catch (MessagingException e) {
								LOGGER.info("Email Exception" + e.getMessage());
								e.printStackTrace();
							}
						} else if (provider.getMailMode().equalsIgnoreCase(
								"Zoho")) {
							emailHttpStatus = ZohoSMTPMail.sendEmail(
									email[i],
									"Alert "
											+ alertConfig.getId()
													.getAlertType(),
									vehiclealert.getDescription().replaceAll(
											"%0D", " "),
									provider.getAlertMailId(),
									provider.getAlertMailPasscode());

						}
						if (emailHttpStatus.equalsIgnoreCase("message")) {
							LOGGER.info("Email Sent Successfully");
						}
					}
				}
			}
		}
	}

	private Smssent prepareSmsSent(Vehiclealerts va, String userId) {
		LOGGER.info("AlertsManager::prepareSmsSent::Entered into this method"
				+ "va=" + va);
		if (vehicle == null) {
			vehicle = alertsEJBRemote.getVehicle(va.getVin());
		}
		String alertType;
		if (va.getAlerttype().contains("_ODOMETER")) {
			alertType = va.getAlerttype().replace("_ODOMETER", "");
		} else if (va.getAlerttype().contains(STR_AI_RANGE)) {
			alertType = va.getAlerttype().replace(STR_AI_RANGE, "");
		} else if (va.getAlerttype().contains(STR_AI_LEAKAGE)) {
			alertType = va.getAlerttype().replace(STR_AI_LEAKAGE, "");
		} else {
			alertType = va.getAlerttype();
		}
		Smssent smssent = new Smssent();
		smssent.setCompanyid(vehicle.getCompanyId());
		smssent.setBranchId(vehicle.getBranchId());
		smssent.setUserId(userId);
		smssent.setVin(va.getVin());
		smssent.setCategory("Alert");
		smssent.setFromMobile("KT Alert");
		smssent.setMisc(alertType);
		smssent.setMsg(va.getDescription());
		smssent.setToMobile(va.getSmsmobile());
		smssent.setEventTimeStamp(va.getEventTimeStamp());
		smssent.setServerTimeStamp(va.getServerTimeStamp());
		LOGGER.info("AlertsManager::prepareSmsSent::Leaving from this method successfully");
		return smssent;
	}

	private SortedMap<Integer, Boolean> getDayAlert(int alertDay) {
		SortedMap<Integer, Boolean> hmap = new TreeMap<Integer, Boolean>();
		if (alertDay / 64 == 1) {
			alertDay = alertDay % 64;
			hmap.put(1, true);
		} else {
			hmap.put(1, false);
		}
		if (alertDay / 32 == 1) {
			alertDay = alertDay % 32;
			hmap.put(2, true);
		} else {
			hmap.put(2, false);
		}
		if (alertDay / 16 == 1) {
			alertDay = alertDay % 16;
			hmap.put(3, true);
		} else {
			hmap.put(3, false);
		}
		if (alertDay / 8 == 1) {
			alertDay = alertDay % 8;
			hmap.put(4, true);
		} else {
			hmap.put(4, false);
		}
		if (alertDay / 4 == 1) {
			alertDay = alertDay % 4;
			hmap.put(5, true);
		} else {
			hmap.put(5, false);
		}
		if (alertDay / 2 == 1) {
			alertDay = alertDay % 2;
			hmap.put(6, true);
		} else {
			hmap.put(6, false);
		}
		if (alertDay / 1 == 1) {
			alertDay = alertDay % 1;
			hmap.put(7, true);
		} else {
			hmap.put(7, false);
		}
		return hmap;
	}

	static SortedMap<Integer, Boolean> getMinMaxValue(int value) {
		SortedMap<Integer, Boolean> hmap = new TreeMap<Integer, Boolean>();
		if (value / 2 == 1) {
			value = value % 2;
			hmap.put(2, true);
		} else {
			hmap.put(2, false);
		}
		if (value / 1 == 1) {
			// value = value % 1;
			hmap.put(1, true);
		} else {
			hmap.put(1, false);
		}
		return hmap;
	}

	public String strip(String s) {
		String st = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String result = "";
		for (int i = 0; i < s.length(); i++) {
			if (st.indexOf(s.charAt(i)) >= 0) {
				result += s.charAt(i);
			}
		}
		return result;
	}

	public boolean checkTowedStatus(String vin, String vehicleType) {
		boolean isTowed = false;
		List<Vehicleevent> ve = alertsEJBRemote.getPrevValues(vin);
		if (vehicleType.equalsIgnoreCase("BikeApp")) {
			for (int k = 0; k < 1; k++) {
				if (!ve.get(k).getEngine() && ve.get(k).getSpeed() > 5) {
					isTowed = true;
				} else {
					isTowed = false;
					break;
				}
			}
		} else {
			for (int k = 0; k < ve.size(); k++) {
				if (!ve.get(k).getEngine() && ve.get(k).getSpeed() > 0) {
					isTowed = true;
				} else {
					isTowed = false;
					break;
				}
			}
		}
		return isTowed;
	}

	public SortedMap<String, List<Vehicleevent>> checkAntiTheft(
			List<Vehicleevent> vehicleEvents, String vin, String vehicleType,
			int antiTheftDuration, int antiTheftCount, String assetType) {

		try {
			for (Vehicleevent ve : vehicleEvents) {

				LOGGER.info(" INFO Entering in CheckAntiTheft Method Printing Engine Status"
						+ ve.getEngine());

				Vehicleevent vehiclePreData = MeiTrackDeviceHandler
						.getPrevVEForMovement(vin);

				// String strLastAlertDateTime = TimeZoneUtil
				// .getTimeINYYYYMMddss();
				// LOGGER.error("previ time stamp " + strLastAlertDateTime);

				// LOGGER.error("current  time stamp " +
				// dateCurDateTime.getTime());

				Date prevVehicleEvent = vehiclePreData.getId()
						.getEventTimeStamp();
				Vehicleevent currVehicleEvent = vehicleEvents.get(0);
				long result = ((currVehicleEvent.getId().getEventTimeStamp()
						.getTime() - prevVehicleEvent.getTime()) / 1000);

				boolean engine = ve.getEngine();
				int speed = ve.getSpeed();
				// LOGGER.error("Info Hash Map" + hmap);
				LOGGER.info("INFO Anti theft duration and Result for particular vin "
						+ antiTheftDuration + " and " + result);
				if ((assetType != null && assetType
						.equalsIgnoreCase(STR_IMMOVABLE)) ? ((!hmap
						.containsKey(vin)) && (speed > 0)) : ((!hmap
						.containsKey(vin)) && (!engine) && (speed > 0))) {
					LOGGER.info("INFO Putting Data first time in Hmap for particular vin "
							+ vin);
					hmap.put(vin, vehicleEvents);

				} else if (hmap.containsKey(vin)) {
					LOGGER.info("INFO Putting Data second time and more in Hmap for particular vin "
							+ vin);

					List<Vehicleevent> ve1 = hmap.get(vin);
					ve1.add(ve);
					hmap.put(vin, ve1);

				} else if (hmap.get(vin) != null && result >= 1800) {
					hmap.remove(vin);
				}
			}

		} catch (Exception e) {
			LOGGER.error("AlertsManager checkAntiTheft = " + e);
			e.printStackTrace();
		}
		return hmap;

	}

	public void sendPushNotification(String deviceId, String userMessage,
			String googleServerKey) {
		Sender sender = new Sender(googleServerKey);
		Message message = new Message.Builder().timeToLive(86400)
				.delayWhileIdle(true).addData("success", userMessage).build();
		try {
			sender.send(message, deviceId, 1);
		} catch (IOException e) {
			LOGGER.error("AlertsManager:sendPushNotification: " + e);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendIosPushNotification(String deviceId, String appName,
			String userMessage, String iosCertificateKey) {

		try {
			if (appName.equalsIgnoreCase("armoron")
					|| appName.equalsIgnoreCase("gpscop")) {
				LOGGER.info("Enter into the armoron application");
				if (userMessage.contains("Vehicle AntiTheft")
						|| userMessage.contains("Engine Turned ON")
						|| userMessage.contains("SOS")
						|| userMessage.contains("GEO")
						|| userMessage.contains("PowerCut")
						|| userMessage.contains("Movement")) {
					BasicConfigurator.configure();
					Push.combined(userMessage, 0, "EmergencyAlert.mp3",
							iosCertificateKey, "armoron18", false, deviceId);
				} else {
					BasicConfigurator.configure();
					Push.combined(userMessage, 0, null, iosCertificateKey,
							"armoron18", false, deviceId);
				}
			} else {

				BasicConfigurator.configure();
				/*
				 * Push.combined(userMessage, 0, "beep.caf", iosCertificateKey,
				 * "password", true, deviceId);
				 */
				Push.combined(userMessage, 0, "beep.caf", iosCertificateKey,
						"EitWorks@321", true, deviceId);
			}
		} catch (Exception e) {
			LOGGER.error("IOS Pushnotification exception :" + e.getMessage());
			System.out.println("IosPushNotify " + e);

		}
	}

	public void sendIonicPushNotification(String deviceId, String userMessage,
			String googleServerKey, String appName) {
		// LOGGER.error("Entered sendIonicPushNotification:");
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost("https://fcm.googleapis.com/fcm/send");
		post.setHeader("Content-type", "application/json");
		// FCM server Key
		post.setHeader("Authorization", "key=" + googleServerKey);
		JSONObject message = new JSONObject();
		try {
			// Device Token
			message.put("to", deviceId);
			message.put("priority", "high");
			JSONObject notification = new JSONObject();
			notification.put("title", appName); // overspeed ,
			notification.put("body", userMessage);
			message.put("notification", notification);

		} catch (Exception e) {
			LOGGER.error("Ionic-PushNotification_Error-token:" + deviceId
					+ ", msg:" + userMessage + ", serverkey:" + googleServerKey
					+ ", appName:" + appName);
		}
		post.setEntity(new StringEntity(message.toString(), "UTF-8"));
		HttpResponse response;
		try {
			response = client.execute(post);
			System.out.println(response);
		} catch (ClientProtocolException e) {
			LOGGER.error("Ionic-PushNotification_Error-token:" + deviceId
					+ ", msg:" + userMessage + ", serverkey:" + googleServerKey
					+ ", appName:" + appName);
			e.printStackTrace();
		} catch (IOException e) {
			LOGGER.error("Ionic-PushNotification_Error-token:" + deviceId
					+ ", msg:" + userMessage + ", serverkey:" + googleServerKey
					+ ", appName:" + appName);
			e.printStackTrace();
		}

	}

	public void pushNotification(String companyId, String userId,
			String description) {

		/*
		 * LOGGER.error("Enter into the pushNotification : " + companyId +
		 * " and " + userId);
		 */

		List<Pushnotificationdevices> pushnotificationdevices = alertsEJBRemote
				.getAllActiveDeviceId(companyId, userId);

		if (pushnotificationdevices != null) {
			for (int i = 0; i < pushnotificationdevices.size(); i++) {
				if (pushnotificationdevices.get(i).getOs()
						.equalsIgnoreCase("Android")) {
					String serverkey = alertsEJBRemote
							.getServerKey(pushnotificationdevices.get(i)
									.getId().getAppName(),
									pushnotificationdevices.get(i).getOs());
					if (serverkey != null) {
						sendPushNotification(pushnotificationdevices.get(i)
								.getDeviceId(), description, serverkey);
					}

				} else if (pushnotificationdevices.get(i).getOs()
						.equalsIgnoreCase("ios")) {
					String serverkey = alertsEJBRemote
							.getServerKey(pushnotificationdevices.get(i)
									.getId().getAppName(),
									pushnotificationdevices.get(i).getOs());
					if (serverkey != null) {
						sendIosPushNotification(pushnotificationdevices.get(i)
								.getDeviceId(), pushnotificationdevices.get(i)
								.getId().getAppName(), description, serverkey);
					}
				} else if (pushnotificationdevices.get(i).getOs()
						.equalsIgnoreCase("ionic")) {

					String serverkey = alertsEJBRemote
							.getServerKey(pushnotificationdevices.get(i)
									.getId().getAppName(),
									pushnotificationdevices.get(i).getOs());
					if (serverkey != null) {

						/*
						 * LOGGER.error("Ionic-PushNotification-token:" +
						 * pushnotificationdevices.get(i).getDeviceId() +
						 * ", msg:" + description + ", serverkey:" + serverkey +
						 * ", appName:" + pushnotificationdevices.get(i).getId()
						 * .getAppName());
						 */

						sendIonicPushNotification(pushnotificationdevices
								.get(i).getDeviceId(), description, serverkey,
								pushnotificationdevices.get(i).getId()
										.getAppName());
					}
				} else if (pushnotificationdevices.get(i).getOs()
						.equalsIgnoreCase("fcm")) {

					String serverkey = alertsEJBRemote
							.getServerKey(pushnotificationdevices.get(i)
									.getId().getAppName(),
									pushnotificationdevices.get(i).getOs());
					if (serverkey != null) {
						fcmPushNotification(pushnotificationdevices.get(i)
								.getDeviceId(), description, serverkey,
								pushnotificationdevices.get(i).getId()
										.getAppName());
					}
				}

			}
		}
	}

	public void alarmIosPushNotification(String defaultSound, String deviceId,
			String appName, String userMessage, String iosCertificateKey) {
		try {
			PushNotificationPayload payload = new PushNotificationPayload();
			payload.addAlert(userMessage);
			payload.addBadge(1);
			payload.addSound(defaultSound);
			payload.addCustomDictionary("id", "1");
			System.out.println(payload.toString());
			List<PushedNotification> NOTIFICATIONS = Push.payload(payload,
					iosCertificateKey, null, false, deviceId);
			for (PushedNotification NOTIFICATION : NOTIFICATIONS) {
				if (NOTIFICATION.isSuccessful()) {
					/* APPLE ACCEPTED THE NOTIFICATION AND SHOULD DELIVER IT */
					System.out
							.println("PUSH NOTIFICATION SENT SUCCESSFULLY TO: "
									+ NOTIFICATION.getDevice().getToken());
					/* STILL NEED TO QUERY THE FEEDBACK SERVICE REGULARLY */
				} else {
					String INVALIDTOKEN = NOTIFICATION.getDevice().getToken();
					/*
					 * ADD CODE HERE TO REMOVE INVALIDTOKEN FROM YOUR DATABASE
					 */
					/* FIND OUT MORE ABOUT WHAT THE PROBLEM WAS */
					Exception THEPROBLEM = NOTIFICATION.getException();
					THEPROBLEM.printStackTrace();
					/*
					 * IF THE PROBLEM WAS AN ERROR-RESPONSE PACKET RETURNED BY
					 * APPLE, GET IT
					 */
					ResponsePacket THEERRORRESPONSE = NOTIFICATION
							.getResponse();
					if (THEERRORRESPONSE != null) {
						System.out.println(THEERRORRESPONSE.getMessage());
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void fcmPushNotification(String deviceId, String userMessage,
			String googleServerKey, String appName) {
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost("https://fcm.googleapis.com/fcm/send");
		post.setHeader("Content-type", "application/json");
		// FCM server Key
		post.setHeader("Authorization", "key=" + googleServerKey);
		JSONObject message = new JSONObject();
		try {
			// Device Token
			message.put("to", deviceId);
			message.put("priority", "high");
			JSONObject notification = new JSONObject();
			notification.put("title", appName); // overspeed ,
			notification.put("body", userMessage);
			message.put("notification", notification);

		} catch (Exception e) {

		}
		post.setEntity(new StringEntity(message.toString(), "UTF-8"));
		HttpResponse response;
		try {
			response = client.execute(post);
			System.out.println(response);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}