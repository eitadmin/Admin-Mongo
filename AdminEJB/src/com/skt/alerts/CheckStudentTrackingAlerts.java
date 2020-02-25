package com.skt.alerts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.eiw.alerts.AlertsEJBRemote;
import com.eiw.device.ejb.FleetTrackingDeviceListenerBORemote;
import com.eiw.device.ejb.VehicleComposite;
import com.eiw.device.handler.MeiTrackDeviceHandler;
import com.eiw.device.handler.method.SKTHandlerMethods;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.bo.BOFactory;
import com.eiw.server.companyadminpu.User;
import com.eiw.server.fleettrackingpu.Freeformgeo;
import com.eiw.server.fleettrackingpu.Route;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.Vehiclealerts;
import com.eiw.server.fleettrackingpu.Vehicleevent;
import com.eiw.server.studenttrackingpu.Alertevents;
import com.eiw.server.studenttrackingpu.Alerttypes;
import com.eiw.server.studenttrackingpu.Routetrip;
import com.eiw.server.studenttrackingpu.Schoolroute;
import com.eiw.server.studenttrackingpu.StudentGeozone;
import com.eiw.server.studenttrackingpu.Studentalertsubscription;
import com.eiw.server.studenttrackingpu.Studentdetails;
import com.eiw.server.studenttrackingpu.Studentevent;
import com.eiw.server.studenttrackingpu.StudenteventId;
import com.eiw.server.studenttrackingpu.Tripstops;
import com.skt.client.dto.LatLng;
import com.skt.client.dto.StudentData;

public class CheckStudentTrackingAlerts {
	AlertsEJBRemote alertsEJBRemote = BOFactory.getAlertsEJBRemote();

	FleetTrackingDeviceListenerBORemote flBoRemote = BOFactory
			.getFleetTrackingDeviceListenerBORemote();

	SKTAlertsEJBremote sktAlertsEJBremote = BOFactory
			.getStudentalertEJBremote();
	SKTAlertsManager sktAlertsManager;
	SKTHandlerMethods sktHandlerMethods =new SKTHandlerMethods();
	Map<Integer, Boolean> prevHMap = null, currHMap = null;
	Map<String, Boolean> prevHMap1 = null, currHMap1 = null;
	Integer currentStopIdOfBus = null;
	Map<Integer, Tripstops> tripStopMap = null;
	String vin;
	Date eventTime;
	List<Vehiclealerts> vehiclealerts = null;
	int routestopid;
	int nextRouteStopId = -1;
	int nextBusstopordernumber;
	Map<String, StudentData> studentDetailssMap = null;
	List<Studentalertsubscription> studentAlertSubscriptions = null;
	boolean validity = false;
	List<Tripstops> vehicleHasTripStops;
	Vehicleevent vehicleEvent;
	Set<Studentdetails> listofStudentDetailsForAlert = new HashSet<Studentdetails>();
	List<Tripstops> vehicleHasRoutestops;
	List<StudentData> studentsForThisStopId;
	List<StudentData> studentForNextStopId;
	List<Alertevents> alerteventsList;
	List<Alertevents> alertListwitoutdupicate;
	String duplicatecontactNo = "";
	String entryPoint = "";
	Vehicle sktVehicle;
	boolean prevStatusForSchool = false;
	List<String> alertsTypesSBT = Arrays.asList("BATA", "BLS", "BLB", "BAS",
			"BAB", "LA");
	List<String> alertsTypesSKT = Arrays.asList("NSPFB", "NSDAB", "SDAB",
			"SDAS", "SEB", "SLB", "SPFS", "SPFB");

	// SKTAlertsEJB studentalertEJB = new SKTAlertsEJB();
	CommonUtil commonUtil = new CommonUtil();
	private Studentevent studentevent;

	private boolean isSameBus = true;

	private Routetrip routetrip;

	private VehicleComposite vehicleComposite;

	private Tripstops currentStop;

	private boolean currStatusForSchool = false;
	
	private boolean currStatusForShed = false;
	
	private boolean prevStatusForShed = false;

	private Studentdetails studentdetails;
	String plateNo, vinGeo;
	List<StudentGeozone> studentGeozones = null;
	public static Map<String, Integer> rfidCountMap = new HashMap<String, Integer>();

	public CheckStudentTrackingAlerts(SKTAlertsManager sktAlertsManager) {
		this.sktAlertsManager = sktAlertsManager;
		// sktAlertsEJBremote = sktAlertsManager.sktAlertsEJBRemote;
		// alertsEJBRemote = sktAlertsManager.alertsEJBRemote;
	}

	private Vehicleevent getVE(Date date, Vehicleevent vehicleEvent) {
		long a = vehicleEvent.getId().getEventTimeStamp().getTime();
		long b = date.getTime();
		if (a == b) {
			return vehicleEvent;
		}
		return null;
	}

	private Integer getCurrentStopOfBus(Vehicleevent preVE) {
		for (Entry<Integer, Tripstops> singleRouteStop : tripStopMap.entrySet()) {
			Double routeStopLatitude = Double.parseDouble(singleRouteStop
					.getValue().getLatlng().split(",")[0]);
			Double routeStopLongitude = Double.parseDouble(singleRouteStop
					.getValue().getLatlng().split(",")[1]);

			if (distanceBetweenTwoPoints(preVE.getLatitude().floatValue(),
					preVE.getLongitude().floatValue(),
					routeStopLatitude.floatValue(),
					routeStopLongitude.floatValue()) < 130) {
				return singleRouteStop.getKey();
			}
		}
		return 0;
	}

	private Tripstops getCurrentStopOfBus(Routetrip routetrip,
			Studentevent studentevent) {
		for (Tripstops tripstop : routetrip.getTripstopses()) {
			Double routeStopLatitude = Double.parseDouble(tripstop.getLatlng()
					.split(",")[0]);
			Double routeStopLongitude = Double.parseDouble(tripstop.getLatlng()
					.split(",")[1]);

			if (distanceBetweenTwoPoints(studentevent.getLatitude()
					.floatValue(), studentevent.getLongitude().floatValue(),
					routeStopLatitude.floatValue(),
					routeStopLongitude.floatValue()) < 130) {
				return tripstop;
			}
		}
		return null;
	}

	public float distanceBetweenTwoPoints(float lat1, float lng1, float lat2,
			float lng2) {
		double earthRadius = 6371000; // meters
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		float dist = (float) (earthRadius * c);

		return dist;
	}

	private void alertsForSteppedinandSteppedoutStudents(
			Integer currentStopIdOfBus, String cardHit) {
		try {

			SKTAlertsManager.LOGGER
					.info("SKTLog: CheckStudentTrackingAlerts::alertsForSteppedinandSteppedoutStudents::Entered into this method::"
							+ cardHit);
			String busstopName = "";
			String location = "";
			List<Alertevents> alertEventsListforParents = new ArrayList<Alertevents>();
			if (currentStopIdOfBus != 0) {
				busstopName = tripStopMap.get(currentStopIdOfBus).getStopName();
			} else {

				try {
					location = sktAlertsManager.getAddressFromLatlng(
							String.valueOf(vehicleEvent.getLatitude()),
							String.valueOf(vehicleEvent.getLongitude()));
					location = commonUtil.getSimpleAddress(location);
				} catch (Exception e) {
					SKTAlertsManager.LOGGER
							.error("SKTLog: CheckStudentTrackingAlerts : getAddressFromLatlng = "
									+ e.getMessage());
				}
			}
			String tripType = vehicleHasTripStops.get(0).getRoutetrip()
					.getType();
			StudentData studentData = studentDetailssMap.get(cardHit);

			Studentdetails studentdetails = sktAlertsManager.setStudentdetails(
					studentDetailssMap, cardHit, vehicleEvent, studentData);

			String parentname = sktAlertsEJBremote.getParent(studentdetails)
					.getFirstName();

			if (!alertEventsListforParents.isEmpty()) {
				SKTAlertsManager.LOGGER
						.info("SKTLog: CheckStudentTrackingAlerts::manageSBTAlerts::alertEventsListforParents");

				sktAlertsManager.persistStudentsAlert(
						alertEventsListforParents, studentDetailssMap,
						studentAlertSubscriptions, vin, vehicleHasTripStops
								.get(0).getRoutetrip());

			}

		} catch (Exception e) {
			SKTAlertsManager.LOGGER
					.error("SKTLog: CheckStudentTrackingAlerts::alertsForSteppedinandSteppedoutStudents:: Exception = "
							+ e.getMessage());
			e.printStackTrace();
		}
	}

	public void manageSBTAlerts(Vehicleevent vehicleEvent, String cardHit,
			Map<String, StudentData> studentDeailsNStatusMap,
			List<Studentalertsubscription> studentalertsubscriptions2,
			Vehicle vehicle, Map<String, String> alerttypesDescriptionMap) {
		try {
			// Not real/live location
			// vehicleEvent =
			// sktAlertsManager.getSimulatedVehicleEvent(vehicleEvent);
			this.studentDetailssMap = studentDeailsNStatusMap;
			// this.alertTypesMap = alerttypesDescriptionMap;
			this.sktVehicle = vehicle;
			this.studentAlertSubscriptions = studentalertsubscriptions2;
			this.vin = sktVehicle.getVin();
			this.vehicleEvent = vehicleEvent;

			SKTAlertsManager.LOGGER
					.info("SKTLog: CheckStudentTrackingAlerts::manageSBTAlerts::cardHit :: "
							+ cardHit);
			vehicleHasTripStops = sktAlertsEJBremote.getSKTTripStops(vin,
					sktVehicle.getCompanyId(), sktVehicle.getBranchId());
			if (vehicleHasTripStops.isEmpty()) {
				return;
			}
			SKTAlertsManager.LOGGER
					.info("SKTLog: CheckStudentTrackingAlerts::manageSBTAlerts:: vehicleHasTripStops size ====="
							+ vehicleHasTripStops.size());

			eventTime = vehicleEvent.getId().getEventTimeStamp();

			// Getting GeoId, Geozone
			tripStopMap = getRouteStopsAsMap(vehicleHasTripStops);

			currentStopIdOfBus = getCurrentStopOfBus(vehicleEvent);

			// Alerts for BATA - Bus About To Arrive
			Vehicleevent prevVE = sktAlertsEJBremote.getGeoPrevVe(vin);
			if (tripStopMap.isEmpty() || prevVE == null) {
				SKTAlertsManager.LOGGER
						.info("SKTLog: CheckStudentTrackingAlerts::manageSBTAlerts:: tripStopMap.isEmpty() || prevVE == null return"
								+ prevVE);

				return;
			}
			Date prevTimeStamp = prevVE.getId().getEventTimeStamp();
			Date currTimeStamp = vehicleEvent.getId().getEventTimeStamp();

			if (currTimeStamp.getTime() < prevTimeStamp.getTime()) {
				SKTAlertsManager.LOGGER
						.info("SKTLog: CheckStudentTrackingAlerts::manageSBTAlerts:: currTimeStamp.getTime() < prevTimeStamp.getTime()");
				return;
			}

			int id = sktAlertsEJBremote.getSchoolGeoZoneId(sktVehicle
					.getCompanyId());
			if (id != -1) {
				prevStatusForSchool = alertsEJBRemote.getFreeformGeoStatus(
						prevVE, id);
				currStatusForSchool = alertsEJBRemote.getFreeformGeoStatus(
						vehicleEvent, id);
			}

			alertsForSteppedinandSteppedoutStudents(currentStopIdOfBus, cardHit);

		} catch (Exception e) {
			SKTAlertsManager.LOGGER
					.error(vehicleEvent.getTags()
							+ " = SKTLog: CheckStudentTrackingAlerts : manageSBTAlerts Exception = "
							+ e.getMessage());
			e.printStackTrace();
		}
	}

	public boolean isBusInsideTheRoute(Vehicleevent vehicleEvent2,
			List<Route> routes) {
		for (Route route : routes) {
			Double routeLat = Double.parseDouble(route.getId().getLatlng()
					.split(",")[0]);
			Double routeLan = Double.parseDouble(route.getId().getLatlng()
					.split(",")[1]);
			if (distanceBetweenTwoPoints(vehicleEvent2.getLatitude()
					.floatValue(), vehicleEvent2.getLongitude().floatValue(),
					routeLat.floatValue(), routeLan.floatValue()) < 200) {
				return true;
			}
		}
		return false;
	}

	private Map<Integer, Tripstops> getRouteStopsAsMap(
			List<Tripstops> vehicleHasRoutestops) {
		// TODO Auto-generated method stub
		Map<Integer, Tripstops> map = new HashMap<Integer, Tripstops>();
		for (int i = 0; i < vehicleHasRoutestops.size(); i++) {
			map.put(((Long) vehicleHasRoutestops.get(i).getStopPointId())
					.intValue(), vehicleHasRoutestops.get(i));
		}
		return map;
	}

	private boolean isCardSwipedFirsttime(Routetrip routetrip) {
		Date lastUpdDate = studentdetails.getLatestEventTime();
		boolean isToday = CommonUtil.isSameDay(lastUpdDate, new Date());
		long studentLastEventTime = CommonUtil
				.getTimeValueFromDate(lastUpdDate);
		Date tripStart = routetrip.getTripStarttime();
		Date tripEnd = routetrip.getTripEndtime();
		tripStart = CommonUtil.timeWithGrace(tripStart, -15);
		tripEnd = CommonUtil.timeWithGrace(tripEnd, 15);

		long tripStartTime = tripStart.getTime();

		long tripEndTime = tripEnd.getTime();
		if (isToday && tripStartTime <= studentLastEventTime
				&& studentLastEventTime <= tripEndTime) {
			return false;
		} else
			return true;
	}

	public Alertevents generateStudentAlert(User parent, String cardHit,
			String location, String alertType, String locationforalertEvent,
			Integer status) {
		SKTAlertsManager.LOGGER
				.info("SKTLog: CheckStudentTrackingAlerts::generateStudentAlert::Entered into this method::");
		String description;
		String language = studentdetails.getPushLng();
		if (language != null && language.equalsIgnoreCase("Tamil")) {
			alertType = alertType + "T";
			description = MeiTrackDeviceHandler.getSKTalerttypesMap()
					.get(alertType).toString()
					+ "#TM";
		} else {
			description = MeiTrackDeviceHandler.getSKTalerttypesMap()
					.get(alertType).toString();
		}
		description = description.replace("parentname", parent.getFirstName());
		description = description.replace("studentname",
				studentdetails.getFirstName());
		description = description.replace("busstopname", location);
		description = description.replace("eventtime",
				getDateTimeInFormat(studentevent.getId().getEventTimeStamp()));
		if (!isSameBus) {
			String routeName = routetrip.getSchoolroute().getName();
			int onIndex = description.indexOf(" on ");
			if (onIndex != -1)
				description = description.substring(0, onIndex)
						+ " in Bus Route named " + routeName
						+ description.substring(onIndex, description.length());
		}
		SKTAlertsManager.LOGGER
				.info("SKTLog: CheckStudentTrackingAlerts::generateStudentAlert:: description = "
						+ description);

		Alertevents altevents = new Alertevents();
		altevents.setStudentdetails(studentdetails);
		altevents.setEventTimeStamp(studentevent.getId().getEventTimeStamp());
		altevents.setDescription(description);
		altevents.setId(status);
		Alerttypes alerttypes = new Alerttypes(alertType);
		altevents.setAlerttypes(alerttypes);
		altevents.setLatlong(studentdetails.getLastUpdLatlng());
		altevents.setVin(vehicleComposite.getVehicle().getVin());
		altevents.setLocation(locationforalertEvent);
		altevents.setTagId(cardHit);
		if (status == -1)
			altevents.setContactNo("MISSWIPE");
		else
			altevents.setContactNo("NOALERT");
		if (routetrip != null) {
			altevents.setTripid(routetrip.getTripid());
		}
		return altevents;
	}

	private String getDateTimeInFormat(Date date) {
		SimpleDateFormat sdfTime = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
		return sdfTime.format(date);
	}

	public void prepareStudentAlerts(Studentevent studentevent,
			VehicleComposite vehicleComposite, Routetrip routetrip,
			Studentdetails studentdetails) {
		this.studentevent = studentevent;
		this.routetrip = routetrip;
		this.vehicleComposite = vehicleComposite;
		this.studentdetails = studentdetails;
		try {
			if (routetrip == null) {
				boolean isGateDevice = sktAlertsEJBremote
						.checkIsGateDevice(vehicleComposite
								.getCompanytrackDevice().getImeiNo());
				if (isGateDevice)
					prepareGateAlerts(studentevent, vehicleComposite,
							studentdetails);
				return;
			}

			Long tripId = null;
			if (routetrip.getType().equalsIgnoreCase("Pickup")) {
				Tripstops ts = sktAlertsEJBremote
						.getTripStopById(studentdetails
								.getTripstopsByPickupid().getStopPointId());
				if (ts != null)
					tripId = ts.getRoutetrip().getTripid();
			} else {
				Tripstops ts = sktAlertsEJBremote
						.getTripStopById(studentdetails.getTripstopsByDropid()
								.getStopPointId());
				if (ts != null)
					tripId = ts.getRoutetrip().getTripid();
			}
			if (!routetrip.getTripid().equals(tripId)) {
				this.isSameBus = false;
			}

			String rfid = studentevent.getId().getTagId();
			SKTAlertsManager.LOGGER
					.info("SKTLog: CheckStudentTrackingAlerts::prepareStudentAlerts::cardHit :: "
							+ rfid);

			currentStop = getCurrentStopOfBus(routetrip, studentevent);

			currStatusForSchool = sktAlertsEJBremote.getFreeformGeoStatus(
					studentevent.getLatitude(), studentevent.getLongitude(),
					MeiTrackDeviceHandler.getSchoolGeoZone(vehicleComposite
							.getCompanytrackDevice().getCompanyId()));

			alertsForSteppedinandSteppedoutStudents();
		} catch (Exception e) {
			SKTAlertsManager.LOGGER
					.error(studentevent.getId().getTagId()
							+ " = SKTLog: CheckStudentTrackingAlerts : manageSBTAlerts Exception = "
							+ e.getMessage());
			e.printStackTrace();
		}
	}

	private void prepareGateAlerts(Studentevent studentevent,
			VehicleComposite vehicleComposite, Studentdetails studentdetails) {
		User parent = sktAlertsEJBremote.getParent(studentdetails);
		if (parent == null) {
			SKTAlertsManager.LOGGER
					.error("Error Getting Parent details of student stin: "
							+ studentdetails.getStin()
							+ " Parent Id: "
							+ studentdetails.getParentId()
							+ " CompanyId: "
							+ studentdetails.getClassdetails().getId()
									.getSchoolId());
		} else {
			String cardHit = studentevent.getId().getTagId();
			String busstopName = "gate";
			isSameBus = true;
			Alertevents alertevent = generateStudentAlert(parent, cardHit,
					busstopName, "SCG", busstopName, 0);
			sktAlertsManager.persistStudentsAlert(alertevent, studentevent,
					vehicleComposite, routetrip, studentdetails, parent);
		}
	}

	private void alertsForSteppedinandSteppedoutStudents() {
		String cardHit = studentevent.getId().getTagId();
		try {

			SKTAlertsManager.LOGGER
					.info("SKTLog: CheckStudentTrackingAlerts::alertsForSteppedinandSteppedoutStudents::Entered into this method::"
							+ cardHit);
			String busstopName = "";
			String location = "";
			Alertevents alertevent;
			if (currentStop != null) {
				busstopName = currentStop.getStopName();
			} else {

				try {
					location = sktAlertsManager.getAddressFromLatlng(
							String.valueOf(studentevent.getLatitude()),
							String.valueOf(studentevent.getLongitude()));
					location = commonUtil.getSimpleAddress(location);
				} catch (Exception e) {
					SKTAlertsManager.LOGGER
							.error("SKTLog: CheckStudentTrackingAlerts : getAddressFromLatlng = "
									+ e.getMessage());
				}
			}
			String tripType = routetrip.getType();
			User parent = sktAlertsEJBremote.getParent(studentdetails);
			if (parent == null) {
				SKTAlertsManager.LOGGER
						.error("Error Getting Parent details of student stin: "
								+ studentdetails.getStin()
								+ " Parent Id: "
								+ studentdetails.getParentId()
								+ " CompanyId: "
								+ studentdetails.getClassdetails().getId()
										.getSchoolId());
			} else {
				String isdemo = alertsEJBRemote.getPreferencesData("IsDemo",
						studentdetails.getClassdetails().getId().getSchoolId());
				if (tripType.equalsIgnoreCase("Pickup")) {
					if (isdemo.equalsIgnoreCase("true")) {
						if (rfidCountMap.get(cardHit) == null
								|| rfidCountMap.get(cardHit) == 0) {
							rfidCountMap.put(cardHit, 1);
							alertevent = generateStudentAlert(parent, cardHit,
									"School", "SEB", "School", 0);
						} else if (rfidCountMap.get(cardHit) == 1) {
							rfidCountMap.put(cardHit, 2);
							alertevent = generateStudentAlert(parent, cardHit,
									"School", "SDAS", "School", 0);
						} else if (rfidCountMap.get(cardHit) == 2) {
							rfidCountMap.put(cardHit, 3);
							alertevent = generateStudentAlert(parent, cardHit,
									"School", "SPFS", "School", 0);
						} else {
							rfidCountMap.put(cardHit, 0);
							alertevent = generateStudentAlert(parent, cardHit,
									"School", "SLB", "School", 0);
						}
					} else if (currStatusForSchool) {
						// SDAS
						alertevent = generateStudentAlert(parent, cardHit,
								"School", "SDAS", "School", 0);
					} else if (currentStop != null) {
						boolean isCardSwipedFirsttime = isCardSwipedFirsttime(routetrip);
						if (isCardSwipedFirsttime) {
							// SPFB
							alertevent = generateStudentAlert(parent, cardHit,
									busstopName, "SPFB", busstopName, 0);
						} else {
							alertevent = generateStudentAlert(parent, cardHit,
									busstopName, "SDAB", busstopName, -1);
						}
					} else {
						boolean isCardSwipedFirsttime = isCardSwipedFirsttime(routetrip);
						if (isCardSwipedFirsttime) {
							// SEB
							alertevent = generateStudentAlert(parent, cardHit,
									location, "SEB", "out of stop", 0);
						} else {
							// SLB
							alertevent = generateStudentAlert(parent, cardHit,
									location, "SLB", "out of stop", -1);
						}
					}
				} else {
					if (isdemo.equalsIgnoreCase("true")) {
						if (rfidCountMap.get(cardHit) == null
								|| rfidCountMap.get(cardHit) == 0) {
							rfidCountMap.put(cardHit, 1);
							alertevent = generateStudentAlert(parent, cardHit,
									"School", "SEB", "School", 0);
						} else if (rfidCountMap.get(cardHit) == 1) {
							rfidCountMap.put(cardHit, 2);
							alertevent = generateStudentAlert(parent, cardHit,
									"School", "SDAS", "School", 0);
						} else if (rfidCountMap.get(cardHit) == 2) {
							rfidCountMap.put(cardHit, 3);
							alertevent = generateStudentAlert(parent, cardHit,
									"School", "SPFS", "School", 0);
						} else {
							rfidCountMap.put(cardHit, 0);
							alertevent = generateStudentAlert(parent, cardHit,
									"School", "SLB", "School", 0);
						}
					} else if (currStatusForSchool) {
						// SPFS
						alertevent = generateStudentAlert(parent, cardHit,
								"School", "SPFS", "School", 0);
					} else if (currentStop != null) {
						// SDAB
						alertevent = generateStudentAlert(parent, cardHit,
								busstopName, "SDAB", busstopName, 0);
					} else {
						alertevent = generateStudentAlert(parent, cardHit,
								location, "SLB", "out of stop", 0);
					}
				}

				if (alertevent != null) {
					SKTAlertsManager.LOGGER
							.info("SKTLog: CheckStudentTrackingAlerts::manageSBTAlerts::alertEventsListforParents");

					sktAlertsManager.persistStudentsAlert(alertevent,
							studentevent, vehicleComposite, routetrip,
							studentdetails, parent);

				}
			}

		} catch (Exception e) {
			SKTAlertsManager.LOGGER
					.error("SKTLog: CheckStudentTrackingAlerts::alertsForSteppedinandSteppedoutStudents:: Exception = "
							+ e.getMessage());
			e.printStackTrace();
		}

	}

	public void stuentsAlertsByBusMove(VehicleComposite vehicleComposite,
			Routetrip routetrip, final Vehicleevent vehicleEvent) {
		SKTAlertsManager.LOGGER
				.info("SKTLog: CheckStudentTrackingAlerts::stuentsAlertsByBusMove:: Entered");
		this.routetrip = routetrip;
		this.vehicleComposite = vehicleComposite;
		this.plateNo = vehicleComposite.getVehicle().getPlateNo();
		this.vinGeo = vehicleEvent.getId().getVin();
		this.vehicleEvent = vehicleEvent;
		String type = routetrip.getType();
		Vehicleevent prevVE = MeiTrackDeviceHandler.getPrevVE(vinGeo);
		if (prevVE == null) {
			return;
		}
		try {
			LatLng curLatlng = new LatLng();
			curLatlng.setLat(vehicleEvent.getLatitude().toString());
			curLatlng.setLng(vehicleEvent.getLongitude().toString());
			LatLng prevLatlng = new LatLng();
			prevLatlng.setLat(prevVE.getLatitude().toString());
			prevLatlng.setLng(prevVE.getLongitude().toString());

			if(!type.equalsIgnoreCase("Wokeup")){
				List<LatLng> polygon = new ArrayList<LatLng>();
				for (Freeformgeo tr : MeiTrackDeviceHandler
						.getSchoolGeoZone(vehicleComposite.getCompanytrackDevice()
								.getCompanyId())) {
					String[] latAndLng = tr.getId().getLatlng().split(",");
					LatLng latlng = new LatLng();
					latlng.setLat(latAndLng[0]);
					latlng.setLng(latAndLng[1]);
					polygon.add(latlng);
				}
				currStatusForSchool = IsPointInPolygon(polygon, curLatlng);
				prevStatusForSchool = IsPointInPolygon(polygon, prevLatlng);
				SKTAlertsManager.LOGGER
						.info("SKTLog: CheckStudentTrackingAlerts::stuentsAlertsByBusMove:: currStatusForSchool & prevStatusForSchool = "
								+ currStatusForSchool + " & " + prevStatusForSchool);
				if (currStatusForSchool && !prevStatusForSchool) {
					if (type.equalsIgnoreCase("Pickup")) {
						alertForBLSBES("BES");
					}
				} else if (!currStatusForSchool && prevStatusForSchool) {
					if (type.equalsIgnoreCase("Drop")) {
						alertForBLSBES("BLS");
					}
				}
			} else {
				List<LatLng> polygon = new ArrayList<LatLng>();
				for (Freeformgeo tr : MeiTrackDeviceHandler
						.getSchoolGeoZone(vehicleEvent.getId().getVin())) {
					String[] latAndLng = tr.getId().getLatlng().split(",");
					LatLng latlng = new LatLng();
					latlng.setLat(latAndLng[0]);
					latlng.setLng(latAndLng[1]);
					polygon.add(latlng);
				}
				
				currStatusForShed = IsPointInPolygon(polygon, curLatlng);
				prevStatusForShed = IsPointInPolygon(polygon, prevLatlng);
				if(!currStatusForShed && prevStatusForShed){
					if (type.equalsIgnoreCase("WokeUp")) {
						alertForBSS("BSS",vehicleEvent.getId().getVin());
					}
				}
			}
			
		} catch (Exception e) {
			SKTAlertsManager.LOGGER
					.error("SKTLog: CheckStudentTrackingAlerts : stuentsAlertsByBusMove Exception = "
							+ e.getMessage());
			e.printStackTrace();
		}

		studentGeozones = sktAlertsEJBremote.getStudentGeozones(vinGeo);
		SKTAlertsManager.LOGGER
				.info("SKTLog: CheckStudentTrackingAlerts : stuentsAlertsByBusMove studentGeozones = "
						+ studentGeozones.size());
		if (studentGeozones == null || studentGeozones.isEmpty()) {
			return;
		}

		List<Date> dates = new ArrayList<Date>();
		dates.add(vehicleEvent.getId().getEventTimeStamp());
		Collections.sort(dates);
		if (MeiTrackDeviceHandler.prevGeoMap.get(vinGeo) != null)
			prevHMap1 = MeiTrackDeviceHandler.prevGeoMap.get(vinGeo);
		else
			prevHMap1 = getGeoStatusHashMap(prevVE);
		if (prevHMap1.isEmpty())
			return;

		for (int i = 0; i < dates.size(); i++) {
			Vehicleevent currVE = getVE(dates.get(i), vehicleEvent);

			// omitting older records coming in Transmission
			Date currDate = currVE.getId().getEventTimeStamp();
			Date prevDate = prevVE.getId().getEventTimeStamp();
			if (currDate.getTime() < prevDate.getTime()) {
				continue;
			}
			currHMap1 = getGeoStatusHashMap(currVE);
			prepareStudentAlert(currHMap1, prevHMap1, type, routetrip);
			prevVE = currVE;
			prevHMap1 = currHMap1;
			MeiTrackDeviceHandler.prevGeoMap.put(vinGeo, currHMap1);
		}
	}

	private void alertForBSS(String type, String vin) {
		
		try {
				List<Studentdetails> stdDetails = sktAlertsEJBremote.getStudentDetailsByVin(vin);
				if (stdDetails != null) {
					for (Studentdetails studentdetail : stdDetails) {
						if(sktAlertsEJBremote.isAlertSubscribed(studentdetail.getStin(), type)){
						alertForBLSBESStudent(studentdetail, type);
						}
					}
				}
		} catch (Exception e) {
			SKTAlertsManager.LOGGER
					.info("CheckStudentTrackingAlerts alertForBSS Exception= "
							+ e.getMessage());
			e.printStackTrace();
		}
		
	}

	private void prepareStudentAlert(Map<String, Boolean> currHMap12,
			Map<String, Boolean> prevHMap12, String type, Routetrip routetrip) {
		List<Studentdetails> studentdetailses = new ArrayList<Studentdetails>();
		// TODO Auto-generated method stub
		for (Map.Entry<String, Boolean> preMap : prevHMap12.entrySet()) {
			SKTAlertsManager.LOGGER
					.info("Studentdetails prepareAlert preMap.getKey() = "
							+ preMap.getKey() + " preMap.getValue() = "
							+ preMap.getValue()
							+ " and currHMap.get(preMap.getKey()) "
							+ currHMap12.get(preMap.getKey()));
			if (!preMap.getValue() && currHMap12.get(preMap.getKey())) {
				List<Studentdetails> studentdetails = sktAlertsEJBremote
						.getStudentsDetails(preMap.getKey(), routetrip);
				if (studentdetails != null)
					studentdetailses.addAll(studentdetails);
			}
		}
		for (Studentdetails studentdtls : studentdetailses) {
			String busStopName = "";
			this.studentdetails = studentdtls;
			String alertType = "BATA";
			User parent = sktAlertsEJBremote.getParent(studentdtls);
			if (parent == null) {
				SKTAlertsManager.LOGGER
						.error("Error Getting Parent details of student stin: "
								+ studentdtls.getStin()
								+ " Parent Id: "
								+ studentdtls.getParentId()
								+ " CompanyId: "
								+ studentdtls.getClassdetails().getId()
										.getSchoolId());
			} else {
				Tripstops ts;
				if (type.equalsIgnoreCase("pickup")) {
					ts = sktAlertsEJBremote.getTripStopById(studentdetails
							.getTripstopsByPickupid().getStopPointId());
				} else {
					ts = sktAlertsEJBremote.getTripStopById(studentdetails
							.getTripstopsByDropid().getStopPointId());
				}
				if (ts != null)
					busStopName = ts.getStopName();

				studentevent = new Studentevent();
				StudenteventId studenteventId = new StudenteventId();
				studenteventId.setEventTimeStamp(vehicleEvent.getId()
						.getEventTimeStamp());
				studentevent.setId(studenteventId);
				isSameBus = true;
				Alertevents alertvents = generateStudentAlert(parent, null,
						busStopName, alertType, busStopName, 0);
				alertvents.setStudentdetails(studentdtls);
				sktAlertsManager.persistStudentsAlert(alertvents, studentevent,
						vehicleComposite, routetrip, studentdtls, parent);
			}
		}
	}

	private void alertForBLSBES(String type) {
		try {
			if (type.equalsIgnoreCase("BES")) {
				String schoolId = routetrip.getSchoolId();
				boolean isSchoolAttendence = sktAlertsEJBremote
						.checkIsSchoolAttendence(schoolId);
				if (isSchoolAttendence)
					return;
			}
			for (Tripstops tripstops : routetrip.getTripstopses()) {
				List<Studentdetails> stdDetails = sktAlertsEJBremote
						.getStudentDetailsByStop(tripstops.getStopPointId());
				if (stdDetails != null) {
					for (Studentdetails studentdetail : stdDetails) {
						alertForBLSBESStudent(studentdetail, type);
					}
				}
			}
		} catch (Exception e) {
			SKTAlertsManager.LOGGER
					.info("CheckStudentTrackingAlerts alertForBLS Exception= "
							+ e.getMessage());
			e.printStackTrace();
		}
	}

	public void alertForBLSBESStudent(Studentdetails studentdetails, String type) {
		if (type.equalsIgnoreCase("BLS")) {
			String status = sktAlertsEJBremote.checkIsPresent(studentdetails);
			if (status.equalsIgnoreCase("Absent"))
				return;
		}
		this.studentdetails = studentdetails;
		User parent = sktAlertsEJBremote.getParent(studentdetails);
		if (parent == null) {
			SKTAlertsManager.LOGGER
					.error("Error Getting Parent details of student stin: "
							+ studentdetails.getStin()
							+ " Parent Id: "
							+ studentdetails.getParentId()
							+ " CompanyId: "
							+ studentdetails.getClassdetails().getId()
									.getSchoolId());
		} else {
			Studentevent studentevent = new Studentevent();
			StudenteventId studenteventId = new StudenteventId();
			studenteventId.setEventTimeStamp(vehicleEvent.getId()
					.getEventTimeStamp());
			studentevent.setId(studenteventId);
			this.studentevent = studentevent;
			this.isSameBus = true;
			studentdetails.setLastUpdLatlng(vehicleEvent.getLatitude() + ","
					+ vehicleEvent.getLongitude());
			Alertevents alertevent = generateStudentAlert(parent, null,
					"School", type, "School", 0);
			sktAlertsManager.persistStudentsAlert(alertevent, studentevent,
					vehicleComposite, routetrip, studentdetails, parent);

		}
	}

	public void alertForAttendence(Studentdetails studentdetails,
			String alertType) {

		this.studentdetails = studentdetails;
		User parent = sktAlertsEJBremote.getParent(studentdetails);
		if (parent == null) {
			SKTAlertsManager.LOGGER
					.error("Error Getting Parent details of student stin: "
							+ studentdetails.getStin()
							+ " Parent Id: "
							+ studentdetails.getParentId()
							+ " CompanyId: "
							+ studentdetails.getClassdetails().getId()
									.getSchoolId());
		} else {
			this.isSameBus = true;
			String description;
			String language = studentdetails.getPushLng();
			if (language != null && language.equalsIgnoreCase("Tamil")) {
				alertType = alertType + "T";
				description = MeiTrackDeviceHandler.getSKTalerttypesMap()
						.get(alertType).toString()
						+ "#TM";
			} else {
				description = MeiTrackDeviceHandler.getSKTalerttypesMap()
						.get(alertType).toString();
			}
			description = description.replace("parentname",
					parent.getFirstName());
			description = description.replace("studentname",
					studentdetails.getFirstName());
			description = description.replace("busstopname", "School");
			Date dateNow = new Date();
			description = description.replace(" on eventtime", "");
			Alertevents altevents = new Alertevents();
			altevents.setStudentdetails(studentdetails);
			altevents.setEventTimeStamp(dateNow);
			altevents.setDescription(description);
			altevents.setId(0);
			Alerttypes alerttypes = new Alerttypes(alertType);
			altevents.setAlerttypes(alerttypes);
			altevents.setLatlong(studentdetails.getLastUpdLatlng());
			altevents.setVin(null);
			altevents.setLocation("School");
			altevents.setTagId(null);
			Routetrip routetrip = new Routetrip();
			Date date = new Date();
			date.setMinutes(01);
			date.setHours(00);
			routetrip.setTripStarttime(date);
			Date date2 = new Date();
			date2.setMinutes(59);
			date2.setHours(23);
			routetrip.setTripEndtime(date2);
			sktAlertsManager.persistAttendenceAlerts(altevents, studentdetails,
					parent, routetrip);

		}
	}

	private Map<String, Boolean> getGeoStatusHashMap(Vehicleevent evt) {
		LatLng curLatLng = new LatLng();
		curLatLng.setLat(evt.getLatitude().toString());
		curLatLng.setLng(evt.getLongitude().toString());
		Map<String, Boolean> hashMap = new HashMap<String, Boolean>();
		for (int i = 0; i < studentGeozones.size(); i++) {
			StudentGeozone studentGeoZones = studentGeozones.get(i);
			String[] latLng = studentGeoZones.getZonelatlng().split("\\|");
			String latCen = latLng[0];
			String latRad = latLng[1];
			String shape = studentGeoZones.getZoneshape();
			if (shape.equalsIgnoreCase("Circle")) {
				float circleDistance = distanceBetweenTwoPoints(
						Float.valueOf(latCen.split(",")[0]),
						Float.valueOf(latCen.split(",")[1]),
						Float.valueOf(latRad.split(",")[0]),
						Float.valueOf(latRad.split(",")[1]));
				float distance = distanceBetweenTwoPoints(
						Float.valueOf(latCen.split(",")[0]),
						Float.valueOf(latCen.split(",")[1]),
						Float.valueOf(curLatLng.getLat()),
						Float.valueOf(curLatLng.getLng()));
				if (distance <= circleDistance) {
					hashMap.put(String.valueOf(studentGeoZones.getId()), true);
				} else {
					hashMap.put(String.valueOf(studentGeoZones.getId()), false);
				}
			} else if (shape.equalsIgnoreCase("Rectangle")) {
				String[] ab = latLng[0].split(",");
				String[] cd = latLng[1].split(",");
				String rectLatLng = ab[0] + "," + ab[1] + "|" + cd[0] + ","
						+ ab[1] + "|" + cd[0] + "," + cd[1] + "|" + ab[0] + ","
						+ cd[1] + "|" + ab[0] + "," + ab[1];
				String[] latLngRectFinal = rectLatLng.split("\\|");
				List<LatLng> polygon = new ArrayList<LatLng>();
				for (int j = 0; j < latLngRectFinal.length; j++) {
					LatLng latlng = new LatLng();
					latlng.setLat(latLngRectFinal[j].split(",")[0]);
					latlng.setLng(latLngRectFinal[j].split(",")[1]);
					polygon.add(latlng);
				}
				boolean status = IsPointInPolygon(polygon, curLatLng);
				hashMap.put(String.valueOf(studentGeoZones.getId()), status);
			} else if (shape.equalsIgnoreCase("FreeForm")) {
				List<LatLng> polygon = new ArrayList<LatLng>();
				for (int j = 0; j < latLng.length; j++) {
					LatLng latlng = new LatLng();
					latlng.setLat(latLng[j].split(",")[0]);
					latlng.setLng(latLng[j].split(",")[1]);
					polygon.add(latlng);
				}
				boolean status = IsPointInPolygon(polygon, curLatLng);
				hashMap.put(String.valueOf(studentGeoZones.getId()), status);
			}
		}
		return hashMap;
	}

	boolean IsPointInPolygon(List<LatLng> poly, LatLng point) {
		int i, j;
		boolean c = false;
		for (i = 0, j = poly.size() - 1; i < poly.size(); j = i++) {
			if ((((Double.valueOf(poly.get(i).getLat()) <= Double.valueOf(point
					.getLat())) && (Double.valueOf(point.getLat()) < Double
					.valueOf(poly.get(j).getLat()))) || ((Double.valueOf(poly
					.get(j).getLat()) <= Double.valueOf(point.getLat())) && (Double
					.valueOf(point.getLat()) < Double.valueOf(poly.get(i)
					.getLat()))))
					&& (Double.valueOf(point.getLng()) < (Double.valueOf(poly
							.get(j).getLng()) - Double.valueOf(poly.get(i)
							.getLng()))
							* (Double.valueOf(point.getLat()) - Double
									.valueOf(poly.get(i).getLat()))
							/ (Double.valueOf(poly.get(j).getLat()) - Double
									.valueOf(poly.get(i).getLat()))
							+ Double.valueOf(poly.get(i).getLng())))
				c = !c;
		}

		return c;
	}

	public void studentGeozoneCreation(Studentevent studentevent,
			VehicleComposite vehicleComposite, Routetrip currTrip,
			Studentdetails studentdetails) {
		// TODO Auto-generated method stub
		LatLng curLatlng = new LatLng();
		curLatlng.setLat(studentevent.getLatitude().toString());
		curLatlng.setLng(studentevent.getLongitude().toString());
		List<LatLng> polygon = new ArrayList<LatLng>();
		for (Freeformgeo tr : MeiTrackDeviceHandler
				.getSchoolGeoZone(vehicleComposite.getCompanytrackDevice()
						.getCompanyId())) {
			String[] latAndLng = tr.getId().getLatlng().split(",");
			LatLng latlng = new LatLng();
			latlng.setLat(latAndLng[0]);
			latlng.setLng(latAndLng[1]);
			polygon.add(latlng);
		}
		boolean currStatusForSchool = IsPointInPolygon(polygon, curLatlng);
		if (!currStatusForSchool) {
			boolean isPickup = false;
			Vehicle vehicle = vehicleComposite.getVehicle();
			try {
				Routetrip pickupTrip = null;
				Routetrip dropTrip = null;
				if (currTrip == null) {
					Schoolroute schoolroute = new Schoolroute();
					schoolroute.setName(vehicle.getPlateNo());
					schoolroute.setCompId(vehicle.getCompanyId());
					schoolroute.setBranchId(vehicle.getBranchId());
					schoolroute.setLastUpdBy(vehicle.getBranchId());
					schoolroute.setLastUpdDt(new Date());
					schoolroute.setVin(vehicle.getVin());
					schoolroute = sktAlertsEJBremote.insertRoute(schoolroute);
					Routetrip pickupRoutetrip = prepareRoutetrip("Pickup",
							"MorningTrip", schoolroute, vehicle, null);
					pickupTrip = sktAlertsEJBremote.insertTrip(pickupRoutetrip,
							"insert");
					Routetrip dropRoutetrip = prepareRoutetrip("Drop",
							"EveningTrip", schoolroute, vehicle, null);
					dropTrip = sktAlertsEJBremote.insertTrip(dropRoutetrip,
							"insert");
					
					// to put trip trip route in currentORupcomingRTMap
					sktHandlerMethods.getCurrentORupcomingRT(sktAlertsManager,
							vehicleComposite, studentevent.getId().getEventTimeStamp());
					isPickup = true;
				} else {
					// Why we Create Multiple Route Trip at TripOver[BES]
					if (currTrip.getType().equalsIgnoreCase("Pickup")) {
						boolean isTripOver = sktAlertsEJBremote
								.isTripOver(currTrip);
						//What behind in the isTripOver
						if (isTripOver) {
							SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
							String endTime = String.valueOf(studentevent
									.getId().getEventTimeStamp().getHours())
									+ ":"
									+ String.valueOf(studentevent.getId()
											.getEventTimeStamp().getMinutes());
							currTrip.setTripEndtime(sdf.parse(endTime));
							sktAlertsEJBremote.insertTrip(currTrip, "update");
							
							// Why to create RouteTrip fo another time
							/*Routetrip pickupRoutetrip = prepareRoutetrip(
									"Pickup", "MorningTrip",
									currTrip.getSchoolroute(), vehicle,
									sdf.parse(endTime));
							pickupTrip = sktAlertsEJBremote.insertTrip(
									pickupRoutetrip, "insert");
							
							Routetrip dropRoutetrip = prepareRoutetrip("Drop",
									"EveningTrip", currTrip.getSchoolroute(),
									vehicle, null);
							dropTrip = sktAlertsEJBremote.insertTrip(
									dropRoutetrip, "insert");*/
							
							
							MeiTrackDeviceHandler.currentORupcomingRTMap.put(
									vehicle.getVin(), currTrip);
							
						} else {
							pickupTrip = currTrip;
							dropTrip = sktAlertsEJBremote.getDropTrip(vehicle
									.getVin());
						}
						isPickup = true;
					}
				}
				if (isPickup) {
					Tripstops pickupStop = sktAlertsEJBremote
							.getTripStopByLatLng(pickupTrip, studentevent);
					Tripstops dropStop = sktAlertsEJBremote
							.getTripStopByLatLng(dropTrip, studentevent);
					StudentGeozone pickupStudentGeozone, dropStudentGeozone;
					if (pickupStop == null) {
						pickupStop = prepareTripstop(pickupTrip, studentevent);
						pickupStop = sktAlertsEJBremote.insertStop(pickupStop);
						pickupStudentGeozone = prepareStudentGeozone(
								pickupTrip, pickupStop);
						pickupStudentGeozone = sktAlertsEJBremote
								.insertGeozone(pickupStudentGeozone);
					} else {
						pickupStudentGeozone = sktAlertsEJBremote
								.getGeozoneByStopId(pickupStop.getStopPointId());
					}
					if (dropStop == null) {
						dropStop = prepareTripstop(dropTrip, studentevent);
						dropStop = sktAlertsEJBremote.insertStop(dropStop);
						dropStudentGeozone = prepareStudentGeozone(dropTrip,
								dropStop);
						dropStudentGeozone = sktAlertsEJBremote
								.insertGeozone(dropStudentGeozone);
					} else {
						dropStudentGeozone = sktAlertsEJBremote
								.getGeozoneByStopId(dropStop.getStopPointId());
					}
					studentdetails.setTripstopsByPickupid(pickupStop);
					studentdetails.setTripstopsByDropid(dropStop);
					studentdetails.setStatus(1);
					studentdetails.setStudentGeozone(pickupStudentGeozone);
					sktAlertsEJBremote.updateStudentDetails(studentdetails);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private Routetrip prepareRoutetrip(String type, String tripName,
			Schoolroute schoolroute, Vehicle vehicle, Date startTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		try {
			int size = schoolroute.getRoutetrips().size() / 2;
			String count = String.valueOf(size + 1);
			Routetrip routetrip = new Routetrip();
			routetrip.setTripName(tripName + count);
			routetrip.setSchoolroute(schoolroute);
			routetrip.setTripDay(63);
			if (type.equalsIgnoreCase("Pickup")) {
				if (startTime != null)
					routetrip.setTripStarttime(startTime);
				else
					routetrip.setTripStarttime(sdf.parse("06:00"));
				routetrip.setTripEndtime(sdf.parse("10:00"));
			} else {
				routetrip.setTripStarttime(sdf.parse("15:00"));
				routetrip.setTripEndtime(sdf.parse("19:00"));
			}
			routetrip.setType(type);
			routetrip.setSchoolId(vehicle.getCompanyId());
			routetrip.setBranchId(vehicle.getBranchId());
			routetrip.setLastUpdBy(vehicle.getBranchId());
			routetrip.setLastUpdDt(new Date());
			return routetrip;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Tripstops prepareTripstop(Routetrip routeTrip,
			Studentevent studentevent) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		try {
			Tripstops tripStop = new Tripstops();
			tripStop.setPos(1);
			tripStop.setLatlng(studentevent.getLatitude() + ","
					+ studentevent.getLongitude());
			tripStop.setRoutetrip(routeTrip);
			tripStop.setStopName("BusStop");
			tripStop.setArrivalTime(sdf.parse("00:00:00"));
			tripStop.setLastUpdBy(routeTrip.getSchoolId());
			tripStop.setLastUpdDt(TimeZoneUtil.getDateInTimeZone());
			return tripStop;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private StudentGeozone prepareStudentGeozone(Routetrip routeTrip,
			Tripstops tripStop) {
		try {
			StudentGeozone studentGeozone = new StudentGeozone();
			studentGeozone.setZonename("BATA");
			studentGeozone.setZoneshape("Circle");
			studentGeozone.setTripstops(tripStop);
			studentGeozone.setCompanyId(routeTrip.getSchoolId());
			studentGeozone.setBranchId(routeTrip.getBranchId());
			studentGeozone.setZonelatlng(getlatlng(tripStop.getLatlng()));
			return studentGeozone;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	private String getlatlng(String latlng) {
		// TODO Auto-generated method stub
		String tempLatLng = latlng;
		tempLatLng = (tempLatLng.replace("(", "")).replace(")", "");

		double lat = Double.valueOf(tempLatLng.split(",")[0]);
		double lng = Double.valueOf(tempLatLng.split(",")[1]);

		double actualRadius = Double.valueOf(1.25);
		double actualRadiusValue = actualRadius / 111;

		double newLatitude = lat + actualRadiusValue;
		double newLongitude = lng + actualRadiusValue;
		String newLatLng = newLatitude + "," + newLongitude;
		String temp = lat + "," + lng;
		StringBuilder stringBuilderForRadius = new StringBuilder();
		stringBuilderForRadius.append(temp);
		stringBuilderForRadius.append("|" + newLatLng);
		String latLng = stringBuilderForRadius.toString();
		return latLng;
	}
}