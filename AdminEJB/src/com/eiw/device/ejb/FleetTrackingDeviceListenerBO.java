package com.eiw.device.ejb;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.websocket.Session;

import org.jboss.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import com.eiw.alerts.AlertsManager;
import com.eiw.client.dto.DashBoardData;
import com.eiw.client.dto.VehicleData;
import com.eiw.client.dto.WftLiveData;
import com.eiw.cron.AlertConfigEJB;
import com.eiw.cron.report.SummaryEJB;
import com.eiw.cron.report.SummaryEJBRemote;
import com.eiw.device.android.Position;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.bo.APMKTDeviceMgmt;
import com.eiw.server.bo.AndroidDeviceMgmt;
import com.eiw.server.bo.BOFactory;
import com.eiw.server.bo.ConcoxDeviceMgmt;
import com.eiw.server.bo.RuptelaDeviceMgmt;
import com.eiw.server.companyadminpu.Errorlog;
import com.eiw.server.companyadminpu.User;
import com.eiw.server.companyadminpu.UserId;
import com.eiw.server.fleettrackingpu.Ais140Emergency;
import com.eiw.server.fleettrackingpu.Ais140EmergencyId;
import com.eiw.server.fleettrackingpu.Ais140Health;
import com.eiw.server.fleettrackingpu.Ais140HealthId;
import com.eiw.server.fleettrackingpu.Alertconfig;
import com.eiw.server.fleettrackingpu.AlertconfigId;
import com.eiw.server.fleettrackingpu.Companytrackdevice;
import com.eiw.server.fleettrackingpu.Fingerprintevent;
import com.eiw.server.fleettrackingpu.FingerprinteventId;
import com.eiw.server.fleettrackingpu.Heartbeatevent;
import com.eiw.server.fleettrackingpu.HeartbeateventId;
import com.eiw.server.fleettrackingpu.Hourmeter;
import com.eiw.server.fleettrackingpu.Hourmeterhistory;
import com.eiw.server.fleettrackingpu.HourmeterhistoryId;
import com.eiw.server.fleettrackingpu.Odometercalc;
import com.eiw.server.fleettrackingpu.TicketSupervisor;
import com.eiw.server.fleettrackingpu.TicketWorkshop;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.VehicleHasCompanytrackdevice;
import com.eiw.server.fleettrackingpu.VehicleHasUser;
import com.eiw.server.fleettrackingpu.Vehicleevent;
import com.eiw.server.fleettrackingpu.VehicleeventId;
import com.eiw.server.studenttrackingpu.Routetrip;
import com.eiw.server.studenttrackingpu.Schoolroute;
import com.eiw.server.studenttrackingpu.Studentalertsubscription;
import com.eiw.server.studenttrackingpu.Studentdetails;
import com.eiw.server.studenttrackingpu.Studentevent;
import com.eiw.server.studenttrackingpu.StudenteventId;
import com.eiw.server.studenttrackingpu.Tripstops;
import com.eiw.server.studenttrackingpu.VehicleDisableTrips;
import com.eiw.server.studenttrackingpu.VehicleHasAttender;
import com.eiw.client.dto.WftLiveData;
import com.eiw.device.android.Position;
import com.eiw.device.apmkt.ais1401A.APMKT_AIS1401AByteWrapper;
import com.skt.alerts.SKTAlertsManager;
import com.skt.client.dto.StudentData;
import com.skt.client.dto.Vehicledetails;

import flexjson.JSONSerializer;

/**
 * Session Bean implementation class FleetTrackingDeviceListenerBO
 */
@Stateless
public class FleetTrackingDeviceListenerBO implements
		FleetTrackingDeviceListenerBORemote {

	private static final Logger LOGGER = Logger.getLogger("listener");

	public static Map<String, Integer> towedCountMap = new HashMap<String, Integer>();

	public static Map<String, Integer> towedStatusCount = new HashMap<String, Integer>();

	public static Map<String, List<Vehicleevent>> towedVehicleeventMap = new HashMap<String, List<Vehicleevent>>();

	public static Map<String, Map<Session, String>> vehicleSessionMap = new HashMap<String, Map<Session, String>>();

	public static Map<String, List<Session>> ticketMap = new HashMap<String, List<Session>>();

	public static Map<String, Date> engineTimeMap = new HashMap<String, Date>();

	@PersistenceContext(unitName = "ltmsfleettrackingpu")
	private EntityManager em;

	@PersistenceContext(unitName = "ltmscompanyadminpu")
	private EntityManager emAdmin;

	@PersistenceContext(unitName = "studenttrackingpu")
	private EntityManager emStudent;

	// org.hibernate.SessionFactory ss = new Configuration().configure()
	// .buildSessionFactory();
	// @PersistenceContext(unitName = "ltmsfleettrackingpu", type =
	// PersistenceContextType.EXTENDED)
	// org.hibernate.Session sessionFleet = ss.openSession();
	// @PersistenceContext(unitName = "studenttrackingpu", type =
	// PersistenceContextType.EXTENDED)
	// org.hibernate.Session sessionStudent = ss.openSession();
	@EJB
	AlertConfigEJB alertConfigEJB;

	AlertsManager alertsManager = new AlertsManager();
	SKTAlertsManager sktAlertsManager = new SKTAlertsManager();
	ConcoxDeviceMgmt concoxMgmt = new ConcoxDeviceMgmt();
	RuptelaDeviceMgmt ruptelaMgmt = new RuptelaDeviceMgmt();
	AndroidDeviceMgmt androidMgmt = new AndroidDeviceMgmt();

	@EJB
	APMKTDeviceMgmt apmkt;

	private static final String STR_VIN = "vin", STR_TIMESTAMP = "timeStamp",
			STR_VALIDCARD = "validCard", STR_INVALIDCARD = "invalidCard",
			STR_TMT250 = "TMT250";
	SimpleDateFormat sdfHHMMSS = new SimpleDateFormat("HH:mm:ss");
	SummaryEJBRemote summaryEJB = BOFactory.getSummaryEJBRemote();
	private static boolean stopEvent = false;
	private static long stopEventTime = 0L;
	private static boolean startEvent = true;
	private static long startEventTime = 0L;

	public FleetTrackingDeviceListenerBO() {

	}

	@Override
	public Map<String, List<Session>> getTicketMap() {
		return ticketMap;
	}

	public VehicleComposite getVehicle(String imeiNo) {
		LOGGER.debug("FleetTrackingDeviceListenerBO::getVehicle::Entered into this method::"
				+ "imeiNo" + imeiNo);
		try {
			LOGGER.debug("FleetTrackingDeviceListenerBO::getVehicle::Start Execute Query");
			VehicleComposite vehicleComposite = new VehicleComposite();
			Query query = em
					.createQuery("select v,vhc,c from Vehicle v, VehicleHasCompanytrackdevice vhc, Companytrackdevice c where v.vin = vhc.id.vehicleVin"
							+ " and c.imeiNo = vhc.id.companytrackdeviceImeiNo and c.imeiNo = :imeiNo");
			query.setParameter("imeiNo", imeiNo);
			// query.setCacheable(true);

			List<Object[]> objects = (List<Object[]>) query.getResultList();
			if (!objects.isEmpty() && objects.size() != 0) {
				LOGGER.debug("Before Execute Query" + "query" + query);
				Object[] objs = objects.get(0);
				Vehicle vehicle = (Vehicle) objs[0];
				VehicleHasCompanytrackdevice vhc = (VehicleHasCompanytrackdevice) objs[1];
				Companytrackdevice ctd = (Companytrackdevice) objs[2];
				vehicleComposite.setVehicle(vehicle);
				vehicleComposite.setVehicleHasCompanytrackDevice(vhc);
				vehicleComposite.setCompanytrackDevice(ctd);
				vehicleComposite.setTimeZoneRegion(getTimeZoneRegion(vehicle
						.getVin()));
				vehicleComposite.setDeviceModel(getDeviceModelName(vehicle
						.getVin()));
				return vehicleComposite;
			} else {
				LOGGER.error("FleetTrackingDeviceListenerBO::getVehicle:: No entity found");
				return null;
			}
		} catch (Exception e) {
			LOGGER.error("FleetTrackingDeviceListenerBO::getVehicle::VehcileComposite error::"
					+ e);
			return null;
		}

	}

	public int persistDeviceData(List<Vehicleevent> vehicleEvents,
			VehicleComposite vehicleComposite) {
		int response = 1;
		LOGGER.info("SKTLog:FleetTrackingDeviceListenerBO::persistDeviceData::Entered into method");
		Vehicle vehicleEventData = new Vehicle();
		boolean blnExists = AlertConfigEJB.map.containsKey(vehicleComposite
				.getVehicle().getVin());
		Vehicle vehicleData = vehicleComposite.getVehicle();
		boolean blnExists1 = AlertConfigEJB.alertVinMap
				.containsKey(vehicleComposite.getVehicle().getVin());
		/* this if condition is used to identify active devices */
		if (!vehicleComposite.getVehicle().isActive()) {
			Vehicle vehicle = em.find(Vehicle.class, vehicleComposite
					.getVehicle().getVin());
			vehicle.setActive(true);
		}

		if (blnExists) {
			try {
				alertsManager.manageAlerts(vehicleEvents, vehicleComposite);

			} catch (Exception e) {
				LOGGER.error("SKTLog:FleetTrackingDeviceListenerBO::persistDeviceData::AlertsManager::manageAlerts::Error in alerts ejb :: "
						+ e.getMessage());
				e.printStackTrace();
			}
		} else if (blnExists1) {
			try {
				alertsManager.manageAlerts(vehicleEvents, vehicleComposite);

			} catch (Exception e) {
				LOGGER.error("SKTLog:FleetTrackingDeviceListenerBO::persistDeviceData::AlertsManager::manageAlerts::Error in alerts ejb :: "
						+ e.getMessage());
				e.printStackTrace();
			}
		} else {
			LOGGER.info("No alert Found For this vin ---> "
					+ vehicleComposite.getVehicle().getVin()
					+ " Calling Ticket Management");
			alertsManager.manageAlerts(vehicleEvents, vehicleComposite);
		}

		for (Vehicleevent ve : vehicleEvents) {
			try {

				VehicleeventId id = new VehicleeventId(ve.getId().getVin(), ve
						.getId().getEventTimeStamp());
				Vehicleevent veChk = em.find(Vehicleevent.class, id);
				if (vehicleData != null) {
					Date eventTime = vehicleData.getVEventTimeStamp();
					vehicleData.setVEventTimeStamp(ve.getId()
							.getEventTimeStamp());
					vehicleData.setVServerTimeStamp(ve.getServerTimeStamp());
					vehicleData.setVLatitude(ve.getLatitude());
					vehicleData.setVLongitude(ve.getLongitude());
					vehicleData.setVSpeed(ve.getSpeed());
					vehicleData.setVPriority(ve.getPriority());
					vehicleData.setVIoevent(ve.getIoevent());
					vehicleData.setVBytesTrx(ve.getBytesTrx());
					vehicleData.setVEngine(ve.getEngine());
					vehicleData.setVTempSensor1(ve.getTempSensor1());
					vehicleData.setVTempSensor2(ve.getTempSensor2());
					vehicleData.setVTempSensor3(ve.getTempSensor3());

					Long vehicleOdometer = vehicleData.getVOdometer() != null ? vehicleData
							.getVOdometer() : 0L;
					vehicleData
							.setVOdometer(vehicleOdometer
									+ (ve.getOdometer() == null ? 0L : ve
											.getOdometer()));

					String curVin = ve.getId().getVin();
					if (ve.getEngine() && !engineTimeMap.containsKey(curVin)) {
						engineTimeMap.put(curVin, ve.getId()
								.getEventTimeStamp());
					} else if (!ve.getEngine()
							&& engineTimeMap.containsKey(curVin)) {
						long engineTime = ve.getId().getEventTimeStamp()
								.getTime()
								- engineTimeMap.get(curVin).getTime();
						vehicleData.setEngineHours((engineTime / 1000)
								+ (vehicleData.getEngineHours() == null ? 0L
										: vehicleData.getEngineHours()));
						vehicleData
								.setTotalEngineHours((engineTime / 1000)
										+ (vehicleData.getTotalEngineHours() == null ? 0L
												: vehicleData
														.getTotalEngineHours()));
						engineTimeMap.remove(curVin);
					}

					int alertrange = getOverspeeedAlertRange(
							vehicleData.getVin(), vehicleData.getCompanyId());
					String status = "";
					if (!ve.getEngine() && ve.getSpeed() == 0) {
						status = "Stop";
					} else if (ve.getEngine() && ve.getSpeed() == 0) {
						status = "Idle";
					} else if (ve.getEngine() && ve.getSpeed() > 0) {
						if (ve.getSpeed() > alertrange) {
							status = "Overspeed";
						} else {
							status = "Running";
						}
					} else if (!ve.getEngine() && ve.getSpeed() > 0) {
						if (towedStatusCount.containsKey(curVin)) {
							if (towedStatusCount.get(curVin) < 5) {
								towedStatusCount.put(curVin,
										towedStatusCount.get(curVin) + 1);
								status = "Running";
							} else {
								status = "Towed";
							}
						} else {
							towedStatusCount.put(curVin, 1);
						}
					}
					if ((vehicleData.geteventStatus() == null ? ""
							: vehicleData.geteventStatus())
							.equalsIgnoreCase(status)) {
						vehicleData.setStatusDuration(((ve.getId()
								.getEventTimeStamp().getTime() - eventTime
								.getTime()) / 1000)
								+ vehicleData.getStatusDuration());
					} else {
						vehicleData.setStatusDuration(0l);
					}
					vehicleData.seteventStatus(status);
					vehicleData.setVBattery(ve.getBattery());
					vehicleData.setVDirection(ve.getDirection());
					vehicleData.setVEventSource(ve.getEventSource());
					vehicleData.setVAi1(ve.getAi1());
					vehicleData.setVAi2(ve.getAi2());
					vehicleData.setVAi3(ve.getAi3());
					vehicleData.setVAi4(ve.getAi4());
					vehicleData.setVDi1(ve.getDi1());
					vehicleData.setVDi2(ve.getDi2());
					vehicleData.setVDi3(ve.getDi3());
					vehicleData.setVDi4(ve.getDi4());
					vehicleData.setVTags(ve.getTags());
					em.merge(vehicleData);
				}
				if (veChk == null) {
					em.persist(ve);
					LOGGER.info("FleetTrackingDeviceListenerBO::persistDeviceData::Vehicleevent persisted");
				} else {
					veChk.setTags(ve.getTags());
					em.merge(veChk);
					LOGGER.info("FleetTrackingDeviceListenerBO::persistDeviceData::Vehicleevent merged");
				}
				response = 0;
			} catch (PersistenceException e) {
				response = 2;
				LOGGER.error("FleetTrackingDeviceListenerBO::persistDeviceData::NonUniqueObjectException occured"
						+ e);
				em.flush();
				// insertDuplicateRecordIssue(ve);
			} catch (Exception e) {
				response = 1;
				LOGGER.error("FleetTrackingDeviceListenerBO::persistDeviceData::Exception occured"
						+ e);
				e.printStackTrace();
			}
		}
		return response;
	}

	public Vehicleevent persistDeviceData(Vehicleevent ve,
			VehicleComposite vehicleComposite) {
		LOGGER.info("SKTLog:FleetTrackingDeviceListenerBO::persistDeviceData::Entered into method");
		List<Vehicleevent> vehicleEvents = new ArrayList<Vehicleevent>();
		// Vehicle vehicleData = vehicleComposite.getVehicle();
		// Vehicle vehicleData = em.find(Vehicle.class, vehicleComposite
		// .getVehicle().getVin());

		Vehicle vehicleData = em.find(Vehicle.class, ve.getId().getVin());

		// Vehicle vehicleData = em.find(Vehicle.class, ve.getId().getVin());
		vehicleEvents.add(ve);
		boolean blnExists = AlertConfigEJB.map.containsKey(vehicleComposite
				.getVehicle().getVin());

		boolean blnExists1 = AlertConfigEJB.alertVinMap
				.containsKey(vehicleComposite.getVehicle().getVin());
		/* this if condition is used to identify active devices */
		if (!vehicleComposite.getVehicle().isActive()) {
			Vehicle vehicle = em.find(Vehicle.class, vehicleComposite
					.getVehicle().getVin());
			vehicle.setActive(true);
		}

		if (blnExists) {
			try {
				alertsManager.manageAlerts(vehicleEvents, vehicleComposite);

			} catch (Exception e) {
				LOGGER.error("SKTLog:FleetTrackingDeviceListenerBO::persistDeviceData::AlertsManager::manageAlerts::Error in alerts ejb :: "
						+ e.getMessage());
			}
		} else if (blnExists1) {
			try {
				alertsManager.manageAlerts(vehicleEvents, vehicleComposite);

			} catch (Exception e) {
				LOGGER.error("SKTLog:FleetTrackingDeviceListenerBO::persistDeviceData::AlertsManager::manageAlerts::Error in alerts ejb :: "
						+ e.getMessage());
			}
		} else {
			LOGGER.info("No alert Found For this vin ---> "
					+ vehicleComposite.getVehicle().getVin());
		}

		try {
			ve.setEngine(ve.getEngine() == null ? vehicleData.getVEngine() : ve
					.getEngine());
			VehicleeventId id = new VehicleeventId(ve.getId().getVin(), ve
					.getId().getEventTimeStamp());
			Vehicleevent veChk = em.find(Vehicleevent.class, id);
			if (veChk == null) {
				if ((!ve.getEngine() && ve.getSpeed() != null)
						&& (!ve.getEngine() && ve.getSpeed() > 0)) {
					List<Vehicleevent> VehicleeventList = new ArrayList<Vehicleevent>();
					VehicleeventList.add(ve);
					if (towedCountMap.get(ve.getId().getVin()) != null) {
						towedCountMap.put(ve.getId().getVin(),
								towedCountMap.get(ve.getId().getVin()) + 1);
						VehicleeventList.addAll(towedVehicleeventMap.get(ve
								.getId().getVin()));
						towedVehicleeventMap.put(ve.getId().getVin(),
								VehicleeventList);
					} else {
						towedCountMap.put(ve.getId().getVin(), 1);
						towedVehicleeventMap.put(ve.getId().getVin(),
								VehicleeventList);
					}
				} else {
					if (towedCountMap.get(ve.getId().getVin()) != null) {
						if (towedCountMap.get(ve.getId().getVin()) < 5) {
							updateVehicleevent(towedVehicleeventMap.get(ve
									.getId().getVin()), "towed",
									vehicleComposite);
						}
						towedCountMap.remove(ve.getId().getVin());
						towedVehicleeventMap.remove(ve.getId().getVin());
					}
				}
			}

			if (vehicleData != null) {
				Date eventTime = vehicleData.getVEventTimeStamp();
				vehicleData.setVEventTimeStamp(ve.getId().getEventTimeStamp());
				vehicleData.setVServerTimeStamp(ve.getServerTimeStamp());
				vehicleData.setVLatitude(ve.getLatitude());
				vehicleData.setVLongitude(ve.getLongitude());
				vehicleData.setVSpeed(ve.getSpeed());
				vehicleData.setVPriority(ve.getPriority());
				vehicleData.setVIoevent(ve.getIoevent());
				vehicleData.setVBytesTrx(ve.getBytesTrx());
				vehicleData.setVEngine(ve.getEngine());
				vehicleData.setVTempSensor1(ve.getTempSensor1());
				vehicleData.setVTempSensor2(ve.getTempSensor2());
				vehicleData.setVTempSensor3(ve.getTempSensor3());

				Long vehicleOdometer = vehicleData.getVOdometer() != null ? vehicleData
						.getVOdometer() : 0L;
				vehicleData.setVOdometer(vehicleOdometer
						+ (ve.getOdometer() == null ? 0L : ve.getOdometer()));

				String curVin = ve.getId().getVin();
				if (ve.getEngine() && !engineTimeMap.containsKey(curVin)) {
					engineTimeMap.put(curVin, ve.getId().getEventTimeStamp());
				} else if (!ve.getEngine() && engineTimeMap.containsKey(curVin)) {
					long engineTime = ve.getId().getEventTimeStamp().getTime()
							- engineTimeMap.get(curVin).getTime();
					vehicleData.setEngineHours((engineTime / 1000)
							+ (vehicleData.getEngineHours() == null ? 0L
									: vehicleData.getEngineHours()));
					vehicleData.setTotalEngineHours((engineTime / 1000)
							+ (vehicleData.getTotalEngineHours() == null ? 0L
									: vehicleData.getTotalEngineHours()));
					engineTimeMap.remove(curVin);
				}

				int alertrange = getOverspeeedAlertRange(vehicleData.getVin(),
						vehicleData.getCompanyId());
				String status = "";
				if (!ve.getEngine() &&  ve.getSpeed() !=null && ve.getSpeed() == 0)  {
					status = "Stop";
				} else if (ve.getEngine() && ve.getSpeed() !=null && ve.getSpeed() == 0) {
					status = "Idle";
				} else if (ve.getEngine() && ve.getSpeed() !=null && ve.getSpeed() > 0) {
					if (ve.getSpeed() > alertrange) {
						status = "Overspeed";
					} else {
						status = "Running";
					}
				} else if (!ve.getEngine()&& ve.getSpeed() !=null && ve.getSpeed() > 0) {
					if (towedStatusCount.containsKey(curVin)) {
						if (towedStatusCount.get(curVin) < 5) {
							towedStatusCount.put(curVin,
									towedStatusCount.get(curVin) + 1);
							status = "Running";
						} else {
							status = "Towed";
						}
					} else {
						towedStatusCount.put(curVin, 1);
					}
				}
				if ((vehicleData.geteventStatus() == null ? "" : vehicleData
						.geteventStatus()).equalsIgnoreCase(status)) {
					vehicleData.setStatusDuration(((ve.getId()
							.getEventTimeStamp().getTime() - eventTime
							.getTime()) / 1000)
							+ vehicleData.getStatusDuration());
				} else {
					vehicleData.setStatusDuration(0l);
				}
				vehicleData.seteventStatus(status);
				vehicleData.setVBattery(ve.getBattery());
				vehicleData.setVDirection(ve.getDirection());
				vehicleData.setVEventSource(ve.getEventSource());
				vehicleData.setVAi1(ve.getAi1());
				vehicleData.setVAi2(ve.getAi2());
				vehicleData.setVAi3(ve.getAi3());
				vehicleData.setVAi4(ve.getAi4());
				vehicleData.setVDi1(ve.getDi1());
				vehicleData.setVDi2(ve.getDi2());
				vehicleData.setVDi3(ve.getDi3());
				vehicleData.setVDi4(ve.getDi4());
				vehicleData.setVTags(ve.getTags());
				em.merge(vehicleData);
			}

			if (veChk == null) {
				em.persist(ve);
				LOGGER.info("FleetTrackingDeviceListenerBO::persistDeviceData::Vehicleevent persisted");
				return ve;
			} else {
				veChk.setTags(ve.getTags());
				em.merge(veChk);
				return null;
			}
		} catch (PersistenceException e) {
			LOGGER.error("FleetTrackingDeviceListenerBO::persistDeviceData::NonUniqueObjectException occured"
					+ e);
			em.flush();
			// insertDuplicateRecordIssue(ve);
			return null;
		} catch (Exception e) {
			LOGGER.error("FleetTrackingDeviceListenerBO::persistDeviceData::Exception occured:: vin: "
					+ vehicleComposite.getVehicle().getVin());
			e.printStackTrace();
			return null;
		}
	}

	public void updateVehicleevent(List<Vehicleevent> Vehicleeventlist,
			String status, VehicleComposite vehicleComposite) {
		// TODO Auto-generated method stub

		try {
			for (Vehicleevent ve : Vehicleeventlist) {
				VehicleeventId id = new VehicleeventId(ve.getId().getVin(), ve
						.getId().getEventTimeStamp());
				Vehicleevent veChk = em.find(Vehicleevent.class, id);
				// Vehicle vehicleData = vehicleComposite.getVehicle();
				Vehicle vehicleData = em.find(Vehicle.class, vehicleComposite
						.getVehicle().getVin());
				if (veChk == null) {
					em.persist(ve);
					if (vehicleData != null) {
						vehicleData.seteventStatus("Online");
						vehicleData.setVIoevent(ve.getIoevent());
						em.merge(vehicleData);
					}
					LOGGER.info("FleetTrackingDeviceListenerBO::updateVehicleevent::Vehicleevent persisted");
				} else {
					if (status.equalsIgnoreCase("towed")) {
						vehicleData.seteventStatus("Towed");
						veChk.setTags(ve.getTags());
						veChk.setSpeed(0);
					} else if (status.equalsIgnoreCase("hbd")) {
						veChk.setIoevent(ve.getIoevent());
					} else if (status.equalsIgnoreCase("hbdAIS140")) {
						veChk.setAi1(ve.getAi1());
						veChk.setAi2(ve.getAi2());
						veChk.setDi1(ve.getDi1());
						veChk.setIoevent(ve.getIoevent());
					}
					em.merge(veChk);
					if (vehicleData != null) {
						vehicleData.setVAi1(ve.getAi1());
						vehicleData.setVAi2(ve.getAi2());
						vehicleData.setVDi1(ve.getDi1());
						vehicleData.setVIoevent(ve.getIoevent());
						em.merge(vehicleData);
					}
					LOGGER.info("FleetTrackingDeviceListenerBO::updateVehicleevent::Vehicleevent merged");
				}
			}
		} catch (Exception e) {
			LOGGER.error("FleetTrackingDeviceListenerBO::updateVehicleevent::Exception occured"
					+ e);
			e.printStackTrace();
		}

	}

	private void insertDuplicateRecordIssue(Vehicleevent vehicleEvent) {
		LOGGER.info("FleetTrackingDeviceListenerBO::insertDuplicateRecordIssue"
				+ "Vehicleevent" + vehicleEvent);

		try {
			Errorlog errorLog = new Errorlog();
			errorLog.setModule("EJB Listener");
			errorLog.setDescription("Duplicate Record Issue");
			errorLog.setVin(vehicleEvent.getId().getVin());
			errorLog.setServerdatetime(vehicleEvent.getId().getEventTimeStamp());

			String[] ioEvent = null;
			if (vehicleEvent.getIoevent() != null) {
				ioEvent = vehicleEvent.getIoevent().split(",");
			}
			String event = null;

			for (int k = 0; k < ioEvent.length; k++) {
				String[] eventSource = ioEvent[k].split("=");
				if (eventSource.length > 1) {
					if (eventSource[0].equalsIgnoreCase(String
							.valueOf(vehicleEvent.getEventSource()))) {
						event = eventSource[1];
					}
				}
			}

			errorLog.setDetails(TimeZoneUtil.getTimeINYYYYMMddss(vehicleEvent
					.getServerTimeStamp())
					+ ","
					+ vehicleEvent.getEventSource() != null ? vehicleEvent
					.getEventSource() + "=" + event.trim() : "");

			emAdmin.persist(errorLog);
		} catch (Exception e) {
			LOGGER.info("FleetTrackingDeviceListenerBO::insertDuplicateRecordIssue::Exception Occured"
					+ e);
		}

	}

	public String getTimeZoneRegion(String vin) {
		LOGGER.info("FleetTrackingDeviceListenerBO::getTimeZoneRegion" + "Vin"
				+ vin);
		String region = null;
		try {
			Query query = emAdmin
					.createQuery("SELECT cb.region FROM Companybranch cb,Vehicle v where v.companyId =cb.id.companyId "
							+ "and v.branchId = cb.id.branchId and v.vin =:vin");
			query.setParameter("vin", vin);
			LOGGER.info("Before Execute Query" + "query" + query);
			region = (String) query.getSingleResult();
			LOGGER.info("After Execute Query" + "query" + query);
		} catch (Exception e) {
			LOGGER.error("FleetTrackingDeviceListenerBO::getTimeZoneRegion::Exception occured"
					+ e);
			e.printStackTrace();
		}
		return region;
	}

	public String getDeviceModelName(String vin) {
		LOGGER.info("FleetTrackingDeviceListenerBO::getDeviceModelName" + "Vin"
				+ vin);
		String modelName = null;
		try {
			Query query = em
					.createQuery("SELECT ctd.companytrackdevicemodels.id.modelName FROM Companytrackdevice ctd,VehicleHasCompanytrackdevice vhctd where vhctd.id.companytrackdeviceImeiNo = ctd.imeiNo "
							+ "and vhctd.vehicle.vin =:vin");
			query.setParameter("vin", vin);
			LOGGER.info("Before Execute Query" + "query" + query);
			modelName = (String) query.getSingleResult();
			LOGGER.info("After Execute Query" + "query" + query);
		} catch (Exception e) {
			LOGGER.error("FleetTrackingDeviceListenerBO::getDeviceModelName::Exception occured"
					+ e);
		}
		return modelName;
	}

	@Override
	public String previousOdometer(String vin, String imeiNo) {
		// LOGGER.error(message);
		String odometer = "";
		try {
			Query query = em
					.createQuery("SELECT o.odometerActual FROM Odometercalc o WHERE o.id.vin=:vin AND o.id.imeiNo=:imeiNo");
			query.setParameter("vin", vin);
			query.setParameter("imeiNo", imeiNo);
			List<Object> objects = (List<Object>) query.getResultList();
			if (objects != null && objects.size() > 0) {
				if (objects.get(0) != null) {
					odometer = String.valueOf(objects.get(0));
				}
			}
		} catch (PersistenceException e) {
			LOGGER.error("FleetTrackingDeviceListenerBO:::previousOdometer::Exception occured "
					+ e);
			e.printStackTrace();
		} catch (Exception e) {
			LOGGER.error("Exception--> odometer value=" + odometer
					+ " FleetTrackingDeviceListenerBO:previousOdometer:" + e);

			e.printStackTrace();
		}
		return odometer;
	}

	@Override
	public String persistOdometerCalc(Odometercalc odometercalc) {
		try {
			// Update OdometerCal Table with currentOdometer Value
			Query query = em
					.createQuery("DELETE FROM Odometercalc o WHERE o.id.vin=:vin AND o.id.imeiNo=:imeiNo");
			query.setParameter("vin", odometercalc.getId().getVin());
			query.setParameter("imeiNo", odometercalc.getId().getImeiNo());
			query.executeUpdate();
			// Insert in OdometerCalc Table
			em.persist(odometercalc);
			return "Success";
		} catch (Exception e) {
			LOGGER.error("FleetTrackingDeviceListenerBO:::updateOdometerCalc:::Exception occured "
					+ e);
			e.printStackTrace();
			return "Failure";
		}
	}

	@Override
	public Map<String, Vehicledetails> getvehicleattenderdetails(Vehicle vehicle) {
		Map<String, Vehicledetails> vehiclestatusdetailsmap = new HashMap<String, Vehicledetails>();
		try {
			Query query = emStudent
					.createQuery("SELECT vh FROM VehicleHasAttender vh ,Tagdetails tg WHERE vh.vin = :vin AND tg.tagType = 'Driver' ");
			query.setParameter("vin", vehicle.getVin());
			List<VehicleHasAttender> vehicleHasAttenders = query
					.getResultList();
			for (VehicleHasAttender vehicleHasAttender : vehicleHasAttenders) {
				Vehicledetails vehicledetails = setVehiclestatusData(vehicleHasAttender);
				vehiclestatusdetailsmap.put(vehicleHasAttender.getVin(),
						vehicledetails);
			}

		} catch (Exception e) {
			System.out.println("Exception e = " + e.getMessage());
		}
		return vehiclestatusdetailsmap;
	}

	@Override
	public void updateStudentData(Studentdetails studentdetails) {
		LOGGER.info("SKTLog: FleetTrackingDeviceListenerBO::updateStudentData::Entered into method");

		try {
			if (studentdetails != null) {
				emStudent.merge(studentdetails);
				LOGGER.debug("FleetTrackingDeviceListenerBO::updateStudentData::Vehicleevent updated");
			}

		} catch (PersistenceException e) {
			LOGGER.error("SKTLog:FleetTrackingDeviceListenerBO::updateStudentData::NonUniqueObjectException occured"
					+ e.getMessage());
			emStudent.flush();
		} catch (Exception e) {
			LOGGER.error("SKTLog:FleetTrackingDeviceListenerBO::updateStudentData::Exception occured"
					+ e.getMessage());
		}

	}

	@Override
	public Map<String, StudentData> getStudentDetailsMap(String vin,
			String compId, String branchId) {

		LOGGER.info("FleetTrackingDeviceListenerBO: getStudentDetailsMap :Entered ");

		Map<String, StudentData> studentDetailsMap = new HashMap<String, StudentData>();
		List<Routetrip> routetrips = new ArrayList<Routetrip>();
		String triptype;
		try {
			Query query = emStudent
					.createQuery("SELECT vh FROM  Schoolroute vh WHERE vh.vin = '"
							+ vin
							+ "' AND  compId= '"
							+ compId
							+ "' AND branchId = '" + branchId + "'");

			LOGGER.debug("getStudentDetailsMap Query ==" + query.toString());

			List<Schoolroute> vehicleHasRoutes = query.getResultList();
			LOGGER.debug("getStudentDetailsMap vehicleHasRoutes size =="
					+ vehicleHasRoutes.size());
			if (!vehicleHasRoutes.isEmpty() && vehicleHasRoutes.size() != 0) {
				for (Schoolroute singleroute : vehicleHasRoutes) {
					try {
						Set<Routetrip> listoftrips = singleroute
								.getRoutetrips();
						routetrips.addAll(listoftrips);
						if (routetrips.size() != 0) {
							for (Routetrip singlevehicleTrip : routetrips) {
								triptype = singlevehicleTrip.getType();
								Set<Tripstops> tripstops = singlevehicleTrip
										.getTripstopses();
								List<Tripstops> tripstopslist = new ArrayList<Tripstops>();
								tripstopslist.addAll(tripstops);
								if (tripstopslist.size() != 0) {
									for (Tripstops singlebusstops : tripstops) {
										if (triptype.equalsIgnoreCase("pickup")) {
											Set<Studentdetails> listofStudentDetails = (Set<Studentdetails>) getStudentDetailsByStop(singlebusstops
													.getStopPointId());
											if (listofStudentDetails.size() != 0) {
												for (Studentdetails singleStudentdata : listofStudentDetails) {
													StudentData studentData = setStudentdata(singleStudentdata);
													studentData
															.setCorrectBus(true);
													if (singleStudentdata
															.getTagdetails() != null)
														studentDetailsMap
																.put(singleStudentdata
																		.getTagdetails()
																		.getTagId(),
																		studentData);
												}
											}
										} else if (triptype
												.equalsIgnoreCase("drop")) {
											Set<Studentdetails> listofStudentDetails = (Set<Studentdetails>) getStudentDetailsByStop(singlebusstops
													.getStopPointId());
											if (listofStudentDetails.size() != 0) {
												for (Studentdetails singleStudentdata : listofStudentDetails) {
													StudentData studentData = setStudentdata(singleStudentdata);
													studentData
															.setCorrectBus(true);
													if (singleStudentdata
															.getTagdetails() != null)
														studentDetailsMap
																.put(singleStudentdata
																		.getTagdetails()
																		.getTagId(),
																		studentData);
												}
											}
										}
									}
								}
							}

						}
					} catch (Exception e) {
						System.out.println("" + e);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.info("FleetTrackingDeviceListenerBO: getStudentDetailsMap :"
					+ e.getMessage());
			e.printStackTrace();
		}
		// LOGGER.info("FleetTrackingDeviceListenerBO: getStudentDetailsMap : studentDetailsMap :"
		// + studentDetailsMap.toString());
		return studentDetailsMap;
	}

	@Override
	public Vehicleevent getPrevVe(String vin) {
		LOGGER.debug("FleetTrackingDeviceListenerBO::getPrevVe::Entered into this method::vin"
				+ vin);
		try {
			Query query = em
					.createQuery("select e from Vehicleevent e where e.id.vin=:vin order by e.id.eventTimeStamp desc");
			query.setMaxResults(1);
			query.setParameter("vin", vin);
			LOGGER.debug("Before Execute Query query" + query);

			List<Vehicleevent> objects = (List<Vehicleevent>) query
					.getResultList();
			if (!objects.isEmpty() && objects.size() != 0) {
				Vehicleevent vehicleevent = objects.get(0);
				LOGGER.debug("After Excecute Query" + query);
				LOGGER.debug("FleetTrackingDeviceListenerBO::getPrevVe::Leaving from this method successfully");
				return vehicleevent;
			}
			return null;
		} catch (Exception e) {
			LOGGER.error("FleetTrackingDeviceListenerBO::getPrevVe::Exception occured::Geo Qry error="
					+ e);
			return null;
		}
	}

	// Method is for setting the student details to StudentDTO //
	public StudentData setStudentdata(Studentdetails studentDetails) {
		StudentData studentData = new StudentData();
		UserId parentId = new UserId();
		parentId.setEmailAddress(studentDetails.getParentId());
		parentId.setCompanyCompanyId(studentDetails.getClassdetails().getId()
				.getSchoolId());
		User parent = em.find(User.class, parentId);
		studentData.setStin(studentDetails.getStin());
		studentData.setRollNo(studentDetails.getRollNo());
		studentData.setFirstName(studentDetails.getFirstName());
		studentData.setLastName(studentDetails.getLastName());
		studentData.setGender(studentDetails.getSex());
		studentData.setDateOfBirth(studentDetails.getDateOfBirth());
		studentData.setAddress(studentDetails.getAddress());
		studentData.setCity(studentDetails.getCity());
		studentData.setPin(studentDetails.getPin());
		studentData.setState(studentDetails.getState());
		studentData.setSchoolId(studentDetails.getClassdetails().getId()
				.getSchoolId());
		studentData.setBrannchId(studentDetails.getClassdetails().getId()
				.getBranchId());
		studentData.setClassId(studentDetails.getClassdetails().getId()
				.getClassId());
		studentData.setSectionId(studentDetails.getClassdetails().getId()
				.getSectionId());
		if (studentDetails.getTagdetails() != null)
			studentData.setTagId(studentDetails.getTagdetails().getTagId());
		// studentData.setVin(studentDetails.getVin());
		studentData.setAlertMode(studentDetails.getAlertMode());
		studentData.setStatus(studentDetails.getStatus());
		if (studentDetails.getTripstopsByPickupid() != null)
			studentData.setPickuptopId(studentDetails.getTripstopsByPickupid()
					.getStopPointId());
		if (studentDetails.getTripstopsByDropid() != null)
			studentData.setDropstopId(studentDetails.getTripstopsByDropid()
					.getStopPointId());
		studentData.setEventTime(studentDetails.getLatestEventTime());
		studentData.setLatlng(studentDetails.getLastUpdLatlng());
		studentData.setLastUpdBy(studentDetails.getLastUpdBy());
		studentData.setLastUpdDt(studentDetails.getLastUpdDt());
		if (parent != null) {
			studentData.setParentName(studentDetails.getParentId());
			studentData.setContactNo(parent.getContactNo());
			studentData.setEmailAddress(parent.getFax());
			studentData.setUserId(parent.getUserId());
		}
		return studentData;
	}

	// Method end //

	public Vehicledetails setVehiclestatusData(
			VehicleHasAttender vehicleHasAttender) {
		Vehicledetails vehicledetails = new Vehicledetails();
		vehicledetails.setId(vehicleHasAttender.getId());
		vehicledetails.setStatus(vehicleHasAttender.getStatus());
		return vehicledetails;
	}

	@Override
	public void test(Vehicleevent vehicleEvents, Vehicle vehicle) {

	}

	@Override
	public Boolean checkDateAndTime(Vehicle vehicle) {
		try {
			String sql = "SELECT * FROM `vehicle_disable_trips` WHERE NOW() BETWEEN `fromDate` AND `toDate` AND `vin` = '"
					+ vehicle.getVin() + "'";
			Query query = emStudent.createNativeQuery(sql);
			List<VehicleDisableTrips> vehicleDisableTrips = query
					.getResultList();
			if (!vehicleDisableTrips.isEmpty()
					&& vehicleDisableTrips.size() != 0)
				return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public StudentData getStudentData(String singlePassiveTag) {
		try {
			LOGGER.info("SKTLog:FleetTrackingDeviceListenerBO::getStudentData::Entered = "
					+ singlePassiveTag);
			String sqlQuerytoRouteName = "SELECT f.name,a FROM studenttrackingdb.studentdetails AS a INNER JOIN ltmscompanyadmindb.user AS b ON a.parentId=b.emailAddress INNER JOIN tripstops AS c ON a.pickupid=c.stopPointId INNER JOIN  tripstops AS d ON a.dropid=d.stopPointId INNER JOIN routetrip AS e ON c.tripid=e.tripid INNER JOIN schoolroute AS f ON e.routeid=f.id  WHERE a.tagid='"
					+ singlePassiveTag + "' ORDER BY c.stopPointId";
			Query query = emStudent.createNativeQuery(sqlQuerytoRouteName);
			List<Object[]> objects = (List<Object[]>) query.getResultList();
			if (!objects.isEmpty() && objects.size() != 0) {
				Studentdetails studentdetails = (Studentdetails) objects.get(0)[1];
				StudentData studentData = setStudentdata(studentdetails);
				studentData.setRouteName((String) objects.get(0)[0]);
				studentData.setCorrectBus(false);
				return studentData;
			} else {
				LOGGER.info("SKTLog:FleetTrackingDeviceListenerBO::getStudentData::No entry found = "
						+ singlePassiveTag);
				return null;
			}
		} catch (Exception e) {
			LOGGER.error("SKTLog:FleetTrackingDeviceListenerBO::getStudentData::Exception = "
					+ e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Studentdetails getStudentDetails(String singlePassiveTag) {
		try {
			LOGGER.info("SKTLog:FleetTrackingDeviceListenerBO::getStudentDetails::Entered = "
					+ singlePassiveTag);

			String sqlQuerytoStudentDetails = "SELECT sd from Studentdetails sd where sd.tagdetails.tagId='"
					+ singlePassiveTag + "'";
			Query query = emStudent.createQuery(sqlQuerytoStudentDetails);
			Studentdetails studentdetails;
			List<Studentdetails> objects = (List<Studentdetails>) query
					.getResultList();
			if (!objects.isEmpty() && objects.size() != 0) {
				studentdetails = objects.get(0);
			} else {
				LOGGER.info("SKTLog:FleetTrackingDeviceListenerBO::getStudentDetails::No entry found = "
						+ singlePassiveTag);
				studentdetails = new Studentdetails();
				studentdetails.setState("noRFIDfound");
			}
			return studentdetails;
		} catch (Exception e) {
			LOGGER.error("SKTLog:FleetTrackingDeviceListenerBO::getStudentDetails::Exception = "
					+ e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Studentalertsubscription> getAlertsSubscribed(String companyId,
			String branchId) {
		LOGGER.debug("FleetTrackingDeviceListenerBO::getAlertsSubscribed::Entered into this method");
		try {
			String alertsSubscribed = "SELECT altSub FROM Studentalertsubscription altSub WHERE altSub.id.companyId='"
					+ companyId
					+ "' AND altSub.id.branchId='"
					+ branchId
					+ "' GROUP BY altSub.id.stin,altSub.id.alertsubscribed";
			Query alertsSubscribedQuery = em.createQuery(alertsSubscribed);
			LOGGER.debug("Before Query Excecute Query::" + alertsSubscribed);
			List<Studentalertsubscription> alertSubscribed = (List<Studentalertsubscription>) alertsSubscribedQuery
					.getResultList();
			return alertSubscribed;
		} catch (Exception e) {
			LOGGER.error("FleetTrackingDeviceListenerBO::getAlertsSubscribed::Error in getting alert subsribed = "
					+ e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean persistStudentEvent(Studentevent studentevent) {
		LOGGER.info("FleetTrackingDeviceListenerBO::persistStudentEvent::Entered into method");

		try {
			StudenteventId studenteventId = studentevent.getId();
			Studentevent seCheck = em.find(Studentevent.class, studenteventId);

			if (seCheck == null) {
				emStudent.persist(studentevent);
				LOGGER.info("FleetTrackingDeviceListenerBO::persistStudentEvent::Studentevent persisted");
				return true;

			} else {
				LOGGER.info("FleetTrackingDeviceListenerBO::persistStudentEvent::Studentevent exists");
			}
		} catch (PersistenceException e) {
			LOGGER.error("FleetTrackingDeviceListenerBO::persistStudentEvent::NonUniqueObjectException occured"
					+ e);
			emStudent.flush();
			// insertDuplicateRecordIssue(ve);
		} catch (Exception e) {
			LOGGER.error("FleetTrackingDeviceListenerBO::persistStudentEvent::Exception occured"
					+ e);
		}

		return false;
	}

	public List<Studentdetails> getStudentDetailsByStop(Long stopPointId) {
		// TODO Auto-generated method stub
		try {
			Query query = emStudent
					.createQuery("select pro FROM Studentdetails pro WHERE pro.tripstopsByDropid.stopPointId =:stopPointId OR pro.tripstopsByPickupid.stopPointId =:stopPointId");
			query.setParameter("stopPointId", stopPointId);
			return query.getResultList();
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::getStudentDetailsByStop::Error Occured in Studentdetails="
					+ e);
			return null;
		}
	}

	@Override
	public void persistFingerPrintEvent(Fingerprintevent fingerPrintEvent) {
		LOGGER.info("FleetTrackingDeviceListenerBO::persistFingerPrintEvent::Entered into method");

		try {
			FingerprinteventId fingerprinteventId = fingerPrintEvent.getId();
			Fingerprintevent fpCheck = em.find(Fingerprintevent.class,
					fingerprinteventId);

			if (fpCheck == null) {
				em.persist(fingerPrintEvent);
				LOGGER.info("FleetTrackingDeviceListenerBO::persistFingerPrintEvent::FingerPrintEvent persisted");

			} else {
				LOGGER.info("FleetTrackingDeviceListenerBO::persistFingerPrintEvent::FingerPrintEvent exists");
			}
		} catch (PersistenceException e) {
			LOGGER.error("FleetTrackingDeviceListenerBO::persistFingerPrintEvent::NonUniqueObjectException occured"
					+ e);
			em.flush();
		} catch (Exception e) {
			LOGGER.error("FleetTrackingDeviceListenerBO::persistFingerPrintEvent::Exception occured"
					+ e);
		}

	}

	/**
	 * This method will invoke when iButton placed in iButton holder. Who is the
	 * Operator for today or this Vehicle(vin)
	 */
	@Override
	public String persistIbutton(String iButtonValue, String vin,
			String companyId, String branchId) {
		LOGGER.info("Entering iButton persist method :: iButtonValue = "
				+ iButtonValue + " :: vin = " + vin);
		String oldVinVin = "", oldVin = "", oldOp = "", currVin = "", currOpId = "", currOpName = "", status = "";
		try {
			String sqlFindQuery = "select operatorId from operator where surName='"
					+ iButtonValue
					+ "' and companyId='"
					+ companyId
					+ "' and branchId='" + branchId + "'";
			Query query = em.createNativeQuery(sqlFindQuery);
			List<Object[]> listValue = (List<Object[]>) query.getResultList();
			LOGGER.info("isEmpty ::: " + listValue.size());
			if (!listValue.isEmpty()) {
				LOGGER.info("isNotEmpty ::: " + query.getSingleResult());
				String operatorId = String.valueOf(listValue.get(0));
				String sqlFindQuery1 = "select vho.`vehicle_vin`, vho.`operator_operatorId`, op.`name` from vehicle_has_operator vho LEFT JOIN `operator` op ON vho.`operator_operatorId` = op.`operatorId` where vho.vehicle_vin='"
						+ vin
						+ "' and vho.operator_operatorId='"
						+ operatorId
						+ "' and vho.effTo is Null";
				query = em.createNativeQuery(sqlFindQuery1);
				List<Object[]> listValue1 = (List<Object[]>) query
						.getResultList();
				for (int m = 0; m < listValue1.size(); m++) {
					Object[] curVinRow = (Object[]) listValue1.get(m);
					currVin = curVinRow[0].toString();
					currOpId = curVinRow[1].toString();
					currOpName = curVinRow[2].toString();
				}
				LOGGER.info("isEmpty1 ::: " + listValue1.size());
				if (listValue1.isEmpty()) {
					String sqlFindQuery2 = "select * from vehicle_has_operator where operator_operatorId='"
							+ operatorId + "' and effTo is null";
					query = em.createNativeQuery(sqlFindQuery2);
					List<Object[]> listValue2 = (List<Object[]>) query
							.getResultList();
					LOGGER.info("isEmpty2 ::: " + listValue2.size());
					if (!listValue2.isEmpty()) {
						LOGGER.info("isNotEmpty2 ::: "
								+ query.getSingleResult());
						String sqlUpdateQuery = "UPDATE vehicle_has_operator SET effTo=Now() where operator_operatorId='"
								+ operatorId + "' and effTo is null";
						query = em.createNativeQuery(sqlUpdateQuery);
						query.executeUpdate();
						LOGGER.info("execute update if 1::: ");

						String sqlUpdateQuery1 = "UPDATE vehicle_has_operator SET effTo=Now() where vehicle_vin='"
								+ vin + "' and effTo is null";
						query = em.createNativeQuery(sqlUpdateQuery1);
						query.executeUpdate();

						oldVinVin = vin;

						LOGGER.info("execute update if 2::: ");
						String sqlInsertQuery = "Insert into vehicle_has_operator (vehicle_vin, operator_operatorId, "
								+ "effFrom, effTo, lastUpdBy, lastUpdDt) values('"
								+ vin
								+ "', '"
								+ operatorId
								+ "', NOW(), "
								+ "NULL, '" + companyId + "', NOW())";
						query = em.createNativeQuery(sqlInsertQuery);
						query.executeUpdate();
						LOGGER.info("execute update if 3::: ");

						String sqlFindQuery5 = "select vho.`vehicle_vin`, vho.`operator_operatorId`, op.`name` from vehicle_has_operator vho LEFT JOIN `operator` op ON vho.`operator_operatorId` = op.`operatorId` where vho.vehicle_vin='"
								+ vin
								+ "' and vho.operator_operatorId='"
								+ operatorId + "' and vho.effTo is Null";
						query = em.createNativeQuery(sqlFindQuery5);
						List<Object[]> listValue5 = (List<Object[]>) query
								.getResultList();
						for (int k = 0; k < listValue5.size(); k++) {
							Object[] row5 = (Object[]) listValue5.get(k);
							currVin = row5[0].toString();
							currOpId = row5[1].toString();
							currOpName = row5[2].toString();
						}

						String sqlFindQuery8 = "SELECT vho.vehicle_vin FROM vehicle_has_operator vho WHERE  vho.operator_operatorId='"
								+ operatorId
								+ "' AND vho.`lastUpdDt` < (SELECT MAX(vho.effTo) FROM vehicle_has_operator vho WHERE  vho.operator_operatorId='"
								+ operatorId
								+ "')ORDER BY vho.`lastUpdDt` DESC LIMIT 0,1";
						query = em.createNativeQuery(sqlFindQuery8);
						oldVinVin = (String) query.getSingleResult();
						if (!oldVinVin.equalsIgnoreCase(currVin)) {
							oldVin = oldVinVin;
						}

					} else {
						LOGGER.info("isEmpty2 else::: " + listValue2.size());
						String sqlUpdateQuery1 = "UPDATE vehicle_has_operator SET effTo=Now() where vehicle_vin='"
								+ vin + "' and effTo is null";
						query = em.createNativeQuery(sqlUpdateQuery1);
						query.executeUpdate();
						LOGGER.info("execute update if 3::: ");

						String sqlInsertQuery = "Insert into vehicle_has_operator (vehicle_vin, operator_operatorId, "
								+ "effFrom, effTo, lastUpdBy, lastUpdDt) values('"
								+ vin
								+ "', '"
								+ operatorId
								+ "', NOW(), "
								+ "NULL, '" + companyId + "', NOW())";
						query = em.createNativeQuery(sqlInsertQuery);
						query.executeUpdate();
						LOGGER.info("execute update if 4::: ");

						String sqlFindQuery6 = "select vho.`vehicle_vin`, vho.`operator_operatorId`, op.`name` from vehicle_has_operator vho LEFT JOIN `operator` op ON vho.`operator_operatorId` = op.`operatorId` where vho.vehicle_vin='"
								+ vin
								+ "' and vho.operator_operatorId='"
								+ operatorId + "' and vho.effTo is Null";
						query = em.createNativeQuery(sqlFindQuery6);
						List<Object[]> listValue6 = (List<Object[]>) query
								.getResultList();
						for (int k = 0; k < listValue6.size(); k++) {
							Object[] row6 = (Object[]) listValue6.get(k);
							currVin = row6[0].toString();
							currOpId = row6[1].toString();
							currOpName = row6[2].toString();
						}

						String sqlFindQuery7 = "SELECT vho.vehicle_vin FROM vehicle_has_operator vho WHERE  vho.operator_operatorId='"
								+ operatorId
								+ "' AND vho.`lastUpdDt` < (SELECT MAX(vho.effTo) FROM vehicle_has_operator vho WHERE  vho.operator_operatorId='"
								+ operatorId
								+ "')ORDER BY vho.`lastUpdDt` DESC LIMIT 0,1";
						query = em.createNativeQuery(sqlFindQuery7);

						oldVinVin = (String) query.getSingleResult();
						if (!oldVinVin.equalsIgnoreCase(currVin)) {
							oldVin = oldVinVin;
						}

					}
					LOGGER.info("Updated iButton persist method :: iButtonValue = "
							+ iButtonValue
							+ " :: vin = "
							+ vin
							+ " :: Operator Id = " + operatorId);
				}
				status = STR_VALIDCARD;
				JSONObject data = new JSONObject();
				data.put("mode", "operator");
				data.put("submode", "operatorchange-teltonika");
				data.put("currentVin", currVin);
				data.put("currentOperatorName", currOpName);
				data.put("currentOperatorId", currOpId);
				data.put("oldVin ", oldVin);
				sendMessageforClient(currVin, data);
			}
			status = STR_INVALIDCARD;

		} catch (Exception e) {
			LOGGER.error("FleetTrackingDeviceListenerBO::persistIbutton::Exception occured"
					+ e);
		}
		return status;
	}

	@Override
	public String getCompanySettings(String keyValue, String companyId) {
		// TODO Auto-generated method stub
		String result = "0";
		try {
			String sqlforCmpSetting = "SELECT comp.values AS companyPref FROM ltmscompanyadmindb.companysettings comp  WHERE comp.company_companyId = :corpId and comp.appsettings_key = :key ";
			LOGGER.info("Brfore exec qry 1");
			Query queryCmpSetting = em.createNativeQuery(sqlforCmpSetting);
			queryCmpSetting.setParameter("corpId", companyId);
			queryCmpSetting.setParameter("key", keyValue);
			if (!queryCmpSetting.getResultList().isEmpty()
					&& queryCmpSetting.getResultList().size() != 0) {
				result = (String) queryCmpSetting.getResultList().get(0);
			} else {
				result = "0";
			}
		} catch (Exception e) {
			LOGGER.error("FleetTrackingDeviceListenerBO::getCompanySettings::Exception occured"
					+ e);
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void persistHourMeter(Vehicleevent vehicleevent, int runningduration) {
		// TODO Auto-generated method stub
		String vin = vehicleevent.getId().getVin();
		Date eventTimeStamp = vehicleevent.getId().getEventTimeStamp();
		Vehicle vehicle = em.find(Vehicle.class, vin);
		String type = vehicle.getVehicletype().getVehicleType();
		boolean engineRunTime = false;
		int engineRunTimeValue = 0;
		if (type.equalsIgnoreCase("DEEPSEA GENERATOR")
				&& vehicleevent.getIoevent() != null
				&& vehicleevent.getIoevent().contains("engineRunTime")) {
			try {
				JSONObject ioevent = new JSONObject(vehicleevent.getIoevent());
				engineRunTimeValue = Integer.parseInt(ioevent.get(
						"engineRunTime").toString()) / 60;
				engineRunTime = true;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		Hourmeter hourmeter = em.find(Hourmeter.class, vin);
		if (hourmeter == null) {
			Hourmeter hourmtr = new Hourmeter();
			hourmtr.setVin(vin);
			hourmtr.setFromDate(eventTimeStamp);
			hourmtr.setRunningDuration(String.valueOf(runningduration));
			if (engineRunTime)
				hourmtr.setTotRunningDuration(String
						.valueOf(engineRunTimeValue));
			else
				hourmtr.setTotRunningDuration(String.valueOf(runningduration));
			hourmtr.setLastUpdDt(new Date());
			em.persist(hourmtr);

		} else {
			if (eventTimeStamp.getDate() > hourmeter.getFromDate().getDate()) {
				HourmeterhistoryId hourmeterhistoryid = new HourmeterhistoryId();
				hourmeterhistoryid.setFromDate(hourmeter.getFromDate());
				hourmeterhistoryid.setVin(vin);
				hourmeterhistoryid.setLastUpdDt(new Date());
				hourmeterhistoryid.setRunningDuration(hourmeter
						.getRunningDuration());
				hourmeterhistoryid.setTotRunningDuration(hourmeter
						.getTotRunningDuration());
				Hourmeterhistory hourmtrhistory = new Hourmeterhistory();
				hourmtrhistory.setId(hourmeterhistoryid);
				em.persist(hourmtrhistory);
				int totRunningDuration = runningduration
						+ Integer.parseInt(hourmeter.getTotRunningDuration());
				hourmeter.setFromDate(eventTimeStamp);
				hourmeter.setLastUpdDt(new Date());
				if (engineRunTime)
					hourmeter.setTotRunningDuration(String
							.valueOf(engineRunTimeValue));
				else
					hourmeter.setTotRunningDuration(String
							.valueOf(totRunningDuration));
				hourmeter.setRunningDuration(String.valueOf(runningduration));
				em.merge(hourmeter);
			} else {
				int totRunningDuration = runningduration
						+ Integer.parseInt(hourmeter.getTotRunningDuration());
				runningduration = runningduration
						+ Integer.parseInt(hourmeter.getRunningDuration());
				hourmeter.setLastUpdDt(new Date());
				hourmeter.setFromDate(eventTimeStamp);
				if (engineRunTime)
					hourmeter.setTotRunningDuration(String
							.valueOf(engineRunTimeValue));
				else
					hourmeter.setTotRunningDuration(String
							.valueOf(totRunningDuration));
				hourmeter.setRunningDuration(String.valueOf(runningduration));
				em.merge(hourmeter);
			}
		}
	}

	@Override
	public Vehicleevent getPrevVeConcox(String vin, Date eventTimeStamp) {
		LOGGER.debug("FleetTrackingDeviceListenerBO::getPrevVeConcox::Entered into this method::vin"
				+ vin);
		try {
			Query query = em
					.createQuery("select e from Vehicleevent e where e.id.vin=:vin and e.id.eventTimeStamp < :eventTimeStamp order by e.id.eventTimeStamp desc");
			query.setMaxResults(1);
			query.setParameter("vin", vin);
			query.setParameter("eventTimeStamp", eventTimeStamp);
			LOGGER.debug("Before Execute Query query" + query);

			List<Vehicleevent> objects = (List<Vehicleevent>) query
					.getResultList();
			if (!objects.isEmpty() && objects.size() != 0) {
				Vehicleevent vehicleevent = objects.get(0);
				LOGGER.debug("After Excecute Query" + query);
				LOGGER.info("FleetTrackingDeviceListenerBO::getPrevVeConcox::Leaving from this method successfully");
				return vehicleevent;
			}
			return null;
		} catch (Exception e) {
			LOGGER.error("FleetTrackingDeviceListenerBO::getPrevVeConcox::Exception occured::Geo Qry error="
					+ e);
			return null;
		}
	}

	@Override
	public List<Vehicleevent> checkhourmeter(String vin, Date eventDate) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
		Hourmeter hourmeter = em.find(Hourmeter.class, vin);
		if (hourmeter == null
				|| !sdfDate.format(eventDate).equalsIgnoreCase(
						sdfDate.format(hourmeter.getFromDate()))) {
			String strForDay = "SELECT v FROM Vehicleevent v WHERE v.id.vin =:vin AND v.id.eventTimeStamp BETWEEN  CONCAT(:eventDate,' 00:00:00')  AND CONCAT(:eventDate,' 23:59:59') ORDER BY v.id.eventTimeStamp";
			Query queryStrForDay = em.createQuery(strForDay);
			queryStrForDay.setParameter("vin", vin);
			queryStrForDay.setParameter("eventDate", sdfDate.format(eventDate));
			LOGGER.info("before Execute Query" + "query" + queryStrForDay);
			List<Vehicleevent> vehicleevents = (List<Vehicleevent>) queryStrForDay
					.getResultList();
			return vehicleevents;
		}
		return null;
	}

	@Override
	public void updateLockStatus(Vehicle vehicle, int status) {
		// TODO Auto-generated method stub
		try {
			LOGGER.info("Entered UpdateLockStatus Method");
			Vehicle ve = em.find(Vehicle.class, vehicle.getVin());
			ve.setLockStatus(status);
			em.merge(ve);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getPreferencesData(String keyValue, String corpId) {
		String result;
		String sqlforCmpSetting = "SELECT app.values AS appDefault, comp.values AS companyPref FROM ltmscompanyadmindb.applicationsettings app LEFT JOIN ltmscompanyadmindb.companysettings comp ON app.key=comp.appsettings_key AND comp.company_companyId = :corpId WHERE app.key = :key ";
		LOGGER.info("Brfore exec qry 1");
		Query queryCmpSetting = em.createNativeQuery(sqlforCmpSetting);
		queryCmpSetting.setParameter("corpId", corpId);
		queryCmpSetting.setParameter("key", keyValue);
		Object[] obj = (Object[]) queryCmpSetting.getSingleResult();

		if (obj[1] != null) {
			result = (String) obj[1];
		} else {
			result = (String) obj[0];
		}
		return result;
	}

	@Override
	public void persistHeartBeatEvent(Heartbeatevent heartbeatevent,
			VehicleComposite vehicleComposite) {
		LOGGER.info("Entered persistHeartBeatEvent Method");
		boolean vinExists = AlertConfigEJB.map.containsKey(vehicleComposite
				.getVehicle().getVin());
		Heartbeatevent hbtCheck = null;
		try {
			HeartbeateventId heartbeateventId = heartbeatevent.getId();
			hbtCheck = em.find(Heartbeatevent.class, heartbeateventId);
		} catch (Exception e) {
			LOGGER.error("FleetTrackingDeviceListenerBO::persistHeartBeatEvent::AlertsManager::manageHbAlerts::Find HB Events "
					+ e.getMessage());

		}
		if (vinExists && hbtCheck == null) {
			try {
				alertsManager.manageHbAlerts(heartbeatevent, vehicleComposite);
			} catch (Exception e) {
				LOGGER.error("FleetTrackingDeviceListenerBO::persistHeartBeatEvent::AlertsManager::manageHbAlerts:: "
						+ e.getMessage());
			}
		}
		try {
			if (hbtCheck == null) {
				em.persist(heartbeatevent);
				// Vehicle vehicleHeatData = vehicleComposite.getVehicle();
				Vehicle vehicleHeatData = em.find(Vehicle.class,
						vehicleComposite.getVehicle().getVin());
				if (vehicleHeatData != null) {
					vehicleHeatData.setVTimeStamp(heartbeatevent.getId()
							.getTimeStamp());
					vehicleHeatData.setVHeartBeatEngine(heartbeatevent
							.getEngine());
					vehicleHeatData.setVGps(heartbeatevent.getGps());
					vehicleHeatData.setVGsm(heartbeatevent.getGsm());
					vehicleHeatData.setVBatteryVoltage(heartbeatevent
							.getBatteryVoltage());
					vehicleHeatData.setVPowerSupply(heartbeatevent
							.getPowerSupply());
					vehicleHeatData.setVRemarks(heartbeatevent.getRemarks());
					em.merge(vehicleHeatData);
				}
				LOGGER.info("FleetTrackingDeviceListenerBO::persistHeartBeatEvent::HeartbeatEvent persisted");
			}

		} catch (PersistenceException e) {
			LOGGER.error("FleetTrackingDeviceListenerBO::persistHeartBeatEvent::NonUniqueObjectException occured"
					+ e);
			em.flush();
		} catch (Exception e) {
			LOGGER.error("FleetTrackingDeviceListenerBO::persistHeartBeatEvent::Exception occured"
					+ e);
		}
	}

	public void updatevehicle(Vehicle ve) {
		em.merge(ve);
	}

	@Override
	public void registerSession(String message, Session session) {
		// TODO Auto-generated method stub
		try {
			JSONObject dboradjson = new JSONObject(message);
			String vin = dboradjson.getString("dashboardVin");
			Map<Session, String> sessions = new HashMap<Session, String>();
			sessions.put(session, message);
			if (vehicleSessionMap.get(vin) != null) {
				sessions.putAll(vehicleSessionMap.get(vin));
			}
			vehicleSessionMap.put(vin, sessions);
			String command = "";
			if (dboradjson.getString("make").equalsIgnoreCase("Concox")) {
				if (dboradjson.getString("model").equalsIgnoreCase("KT-Mini"))
					command = "#6666#SMT#" + dboradjson.getString("delay")
							+ "#";
				else if (dboradjson.getString("model")
						.equalsIgnoreCase("GT300"))
					command = "TIMER,2," + dboradjson.getString("delay")
							+ ",5#";
				else
					command = "TIMER," + dboradjson.getString("delay") + ",60#";
				concoxMgmt.sendNewConcoxCommand(dboradjson.getString("ImeiNo"),
						command);
			} else if (dboradjson.getString("make").equalsIgnoreCase("Ruptela")) {
				command = " setcfg 1125 " + dboradjson.getString("delay");
				ruptelaMgmt.sendNewCommand(" ", dboradjson.getString("ImeiNo"),
						command);
			} else if (dboradjson.getString("make").equalsIgnoreCase("Android")) {
				LOGGER.error("Websocket for Android and vin" + vin + " ");
				JSONObject jobj = new JSONObject();
				JSONObject jobjforMode = new JSONObject();
				jobjforMode.put("mode", "interval");
				jobjforMode.put("interval", dboradjson.getString("delay"));
				jobj.put("errorcode", 0);
				jobj.put("iserror", false);
				jobj.put("result", jobjforMode);

				androidMgmt.sendCommand(dboradjson.getString("ImeiNo"), jobj);
			} else if (dboradjson.getString("make").equalsIgnoreCase("APMKT")) {
				command = "^INO " + dboradjson.getString("delay") + ",#";
				apmkt.sendCommand(dboradjson.getString("ImeiNo"), command);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void liveTrackingDataPusher(Vehicleevent vehicleevent,
			VehicleComposite vehicleComposite) {
		// TODO Auto-generated method stub
		String vin = vehicleComposite.getVehicle().getVin();
		try {
			if (vehicleSessionMap.get(vin) != null) {
				Map<Session, String> sessions = new HashMap<Session, String>();
				sessions.putAll(vehicleSessionMap.get(vin));
				for (Map.Entry<Session, String> peer : sessions.entrySet()) {
					try {
						Session session = peer.getKey();
						JSONObject dboradjson = new JSONObject(peer.getValue());
						String entryPoint = dboradjson.getString("entryPoint");
						if (session.isOpen()) {
							String dashboarddata = "";
							if (entryPoint.equalsIgnoreCase("ArmorOn")) {
								List<WftLiveData> liveData = getWFTLivetrackingData(
										entryPoint, vehicleevent,
										vehicleComposite);
								dashboarddata = new JSONSerializer().exclude(
										"*.class").deepSerialize(liveData);
							} else {
								DashBoardData dashboardData = new DashBoardData();
								dashboardData = getLivetrackingData(entryPoint,
										vehicleevent, vehicleComposite);
								dashboarddata = new JSONSerializer().exclude(
										"*.class").deepSerialize(dashboardData);
							}
							if (session.isOpen() && dashboarddata != null
									&& !dashboarddata.equalsIgnoreCase("")) {
								try {
									session.getBasicRemote().sendText(
											dashboarddata);
								} catch (Exception e) {
									LOGGER.error("Session Error occured in LiveTracking");
								}

							}
						} else {
							vehicleSessionMap.get(vin).remove(session);
							if (vehicleSessionMap.get(vin) == null
									|| vehicleSessionMap.get(vin).isEmpty()
									|| vehicleSessionMap.get(vin).size() == 0) {
								String command = "";
								String timer = "60";
								if (dboradjson.has("defaultInterval")) {
									timer = dboradjson
											.getString("defaultInterval");
								}
								if (entryPoint.equalsIgnoreCase("ArmorOn"))
									timer = "20";
								if (dboradjson.getString("make")
										.equalsIgnoreCase("Concox")) {
									if (dboradjson.getString("model")
											.equalsIgnoreCase("KT-Mini")) {
										if (entryPoint
												.equalsIgnoreCase("ArmorOn"))
											command = "#6666#SMT#" + timer
													+ "#";
										else
											command = "#6666#SMT#" + timer
													+ "#";
									} else if (dboradjson.getString("model")
											.equalsIgnoreCase("GT300"))
										command = "TIMER,2,120,5#";
									else
										command = "TIMER," + timer + ",60#";
									concoxMgmt.sendNewConcoxCommand(
											dboradjson.getString("ImeiNo"),
											command);
								} else if (dboradjson.getString("make")
										.equalsIgnoreCase("Ruptela")) {
									command = " setcfg 1125 " + timer;
									ruptelaMgmt.sendNewCommand(" ",
											dboradjson.getString("ImeiNo"),
											command);
								} else if (dboradjson.getString("make")
										.equalsIgnoreCase("Android")) {
									JSONObject jsObj = new JSONObject();
									JSONObject jobjforReset = new JSONObject();
									jobjforReset.put("mode", "interval");
									jobjforReset.put("type", 3);
									jobjforReset.put("interval", timer);
									jsObj.put("errorcode", 0);
									jsObj.put("iserror", false);
									jsObj.put("result", jobjforReset);
									androidMgmt.sendCommand(
											dboradjson.getString("ImeiNo"),
											jsObj);

								} else if (dboradjson.getString("make")
										.equalsIgnoreCase("APMKT")) {
									command = "^INO " + timer + ",#";
									apmkt.sendCommand(
											dboradjson.getString("ImeiNo"),
											command);
								}
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<WftLiveData> getWFTLivetrackingData(String entryPoint,
			Vehicleevent vehicleevent, VehicleComposite vehicleComposite) {
		String NO_TRANS = "No Transmission", STR_OVERSPEED = "Overspeed", STR_RUNNING = "Running", STR_TOWED = "Towed";
		SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<WftLiveData> liveDatas = new ArrayList<WftLiveData>();
		try {
			Date today = TimeZoneUtil.getDateInTimeZone(new Date());
			long todayInSec = today.getTime() / 1000;
			long diff = Long.valueOf(getPreferencesData(NO_TRANS,
					vehicleComposite.getVehicle().getCompanyId()));
			String dashboardVin = vehicleComposite.getVehicle().getVin();
			WftLiveData liveData = new WftLiveData();
			Date eventTimeStamp = sdfTime.parse(TimeZoneUtil
					.getTimeINYYYYMMddss(vehicleevent.getId()
							.getEventTimeStamp()));
			long eventTimeStampInSec = eventTimeStamp.getTime() / 1000;
			long differenceInSec = todayInSec - eventTimeStampInSec;
			liveData.setSpeed(vehicleevent.getSpeed());
			String SpeedLimit = "50";
			if (diff < differenceInSec) {
				liveData.setSpeed(0);
				liveData.setStatus(NO_TRANS);
			} else {
				if (vehicleevent.getEngine() != null) {
					boolean engine;
					if (vehicleevent.getEngine() instanceof Boolean) {
						engine = !vehicleevent.getEngine();
					} else {
						engine = vehicleevent.getEngine();
					}
					if (engine) {
						if (vehicleevent.getSpeed() == 0) {
							liveData.setStatus("Stop");
						} else {
							boolean isTowed = checkTowedStatus(dashboardVin);
							if (!isTowed) {
								liveData.setSpeed(0);
								liveData.setStatus("Stop");
							} else {
								liveData.setStatus(STR_TOWED);
							}
							// liveData.setStatus(STR_TOWED);
						}
					} else {
						if (SpeedLimit.equals("null")) {
							if (vehicleevent.getSpeed() == 0) {
								liveData.setStatus("Idle");
							} else {
								liveData.setStatus(STR_RUNNING);
							}
						} else {
							if (vehicleevent.getSpeed() == 0) {
								liveData.setStatus("Idle");
							} else if (vehicleevent.getSpeed() > (Integer
									.parseInt(SpeedLimit))) {
								liveData.setStatus(STR_OVERSPEED);
							} else {
								liveData.setStatus(STR_RUNNING);
							}
						}
					}
				} else {
					if (vehicleevent.getSpeed() == 0) {
						liveData.setStatus("Stop");
					} else {
						boolean isTowed = checkTowedStatus(dashboardVin);
						if (!isTowed) {
							liveData.setSpeed(0);
							liveData.setStatus("Stop");
						} else {
							liveData.setStatus(STR_TOWED);
						}

					}
				}
			}
			liveData.setVin(dashboardVin);
			liveData.setTimeStamp(TimeZoneUtil.getStrTZ(vehicleevent.getId()
					.getEventTimeStamp()));
			liveData.setLocationTimeStamp(TimeZoneUtil.getStrTZ(vehicleevent
					.getId().getEventTimeStamp()));
			liveData.setLatitude(vehicleevent.getLatitude());
			liveData.setLongitude(vehicleevent.getLongitude());
			liveData.setDirection(vehicleevent.getDirection() == null ? 0
					: vehicleevent.getDirection());
			if (vehicleevent.getIoevent() != null) {
				String[] ioEvents = vehicleevent.getIoevent().split(",");
				for (String ioEventArr : ioEvents) {
					String[] ioEvent = ioEventArr.split("=");
					if (ioEvent[0].equalsIgnoreCase("hbd")) {
						String hbio[] = ioEvent[1].split(";");
						Date hbdeventTimeStamp = sdfTime.parse(hbio[2]);
						liveData.setGps(hbio[1].equalsIgnoreCase("ON") ? true
								: false);
						liveData.setGsmSignalStrength(hbio[3]);
						liveData.setBatteryvoltage(hbio[5]);
						liveData.setPowerSupplyVoltage(hbio[6]);
						long diffSeconds = (hbdeventTimeStamp.getTime() - eventTimeStamp
								.getTime()) / 1000;
						if (hbdeventTimeStamp.after(eventTimeStamp)
								&& diffSeconds > 120) {
							liveData.setTimeStamp(TimeZoneUtil
									.getStrTZ(hbdeventTimeStamp));
							long nhdeventTimeStampInSec = hbdeventTimeStamp
									.getTime() / 1000;
							long differenceSec = todayInSec
									- nhdeventTimeStampInSec;
							if (diff < differenceSec) {
								liveData.setStatus(NO_TRANS);
							} else if (hbio[4].equalsIgnoreCase("true")) {
								liveData.setStatus("Idle");
							} else {
								liveData.setStatus("Stop");
							}
							liveData.setSpeed(0);
						}
					}
				}
			}
			liveDatas.add(liveData);

		} catch (Exception e) {
			LOGGER.error("liveData " + e);
		}
		return liveDatas;
	}

	private DashBoardData getLivetrackingData(String entryPoint,
			Vehicleevent vehicleevent, VehicleComposite vehicleComposite) {
		String NO_TRANS = "No Transmission", STR_OVERSPEED = "Overspeed", STR_RUNNING = "Running", STR_TOWED = "Towed";
		SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date today = TimeZoneUtil.getDateInTimeZone(new Date());
		long todayInSec = today.getTime() / 1000;
		try {
			long diff = Long.valueOf(getPreferencesData(NO_TRANS,
					vehicleComposite.getVehicle().getCompanyId()));
			DashBoardData dashboardData = new DashBoardData();
			Map<String, VehicleData> liveDataMap = new TreeMap<String, VehicleData>();
			Map<String, String> canData = new TreeMap<String, String>();
			String dashboardVin = vehicleComposite.getVehicle().getVin();
			Vehicle vehicle = em.find(Vehicle.class, dashboardVin);
			VehicleData vehicleData = new VehicleData();
			Date eventTimeStamp = sdfTime.parse(TimeZoneUtil
					.getTimeINYYYYMMddss(vehicleevent.getId()
							.getEventTimeStamp()));
			long eventTimeStampInSec = eventTimeStamp.getTime() / 1000;
			long differenceInSec = todayInSec - eventTimeStampInSec;
			vehicleData.setSpeed(vehicleevent.getSpeed());
			String SpeedLimit = "50";
			if (diff < differenceInSec) {
				vehicleData.setSpeed(0);
				vehicleData.setStatus(NO_TRANS);
			} else {
				if (vehicleevent.getEngine() != null) {
					if (!vehicleevent.getEngine()) {
						vehicleData.setEngineId("false");
						if (vehicleevent.getSpeed() == 0) {
							vehicleData.setStatus("Stop");
						} else {
							boolean isTowed = checkTowedStatus(dashboardVin);
							if (!isTowed) {
								vehicleData.setSpeed(0);
								vehicleData.setStatus("Stop");
							} else {
								vehicleData.setStatus(STR_TOWED);
							}
							// vehicleData.setStatus(STR_TOWED);
						}
					} else {
						vehicleData.setEngineId("true");
						if (SpeedLimit.equals("null")) {
							if (vehicleevent.getSpeed() == 0) {
								vehicleData.setStatus("Idle");
							} else {
								vehicleData.setStatus(STR_RUNNING);
							}
						} else {
							if (vehicleevent.getSpeed() == 0) {
								vehicleData.setStatus("Idle");
							} else if (vehicleevent.getSpeed() > (Integer
									.parseInt(SpeedLimit))) {
								vehicleData.setStatus(STR_OVERSPEED);
							} else {
								vehicleData.setStatus(STR_RUNNING);
							}
						}
					}
				} else {
					vehicleData.setEngineId("false");
					if (vehicleevent.getSpeed() == 0) {
						vehicleData.setStatus("Stop");
					} else {
						boolean isTowed = checkTowedStatus(dashboardVin);
						if (!isTowed) {
							vehicleData.setSpeed(0);
							vehicleData.setStatus("Stop");
						} else {
							vehicleData.setStatus(STR_TOWED);
						}
					}
				}
			}
			vehicleData.setVin(dashboardVin);
			String curDate = TimeZoneUtil.getDate(vehicleevent.getId()
					.getEventTimeStamp());
			int odometer = getTodayOdometer(dashboardVin, curDate);
			LOGGER.error(" Vin : " + dashboardVin + " ToadyOdometer :"
					+ odometer);
			vehicleData.setOdometer(String.valueOf(odometer));
			long FinalOdometer = vehicleComposite.getVehicle().getVOdometer() == null ? 0
					: vehicleComposite.getVehicle().getVOdometer();
			vehicleData.setFinalOdometer(String.valueOf(FinalOdometer / 1000));
			vehicleData.setVehicleStatusType(vehicleComposite.getVehicle()
					.getVehicleStatusType() != null ? vehicleComposite
					.getVehicle().getVehicleStatusType() : "working");
			vehicleData.setTimeStamp(TimeZoneUtil.getStrTZ(vehicleevent.getId()
					.getEventTimeStamp()));
			vehicleData.setLastUpdDate(TimeZoneUtil.getStrTZ(vehicleevent
					.getId().getEventTimeStamp()));
			vehicleData.setLatitude(vehicleevent.getLatitude());
			vehicleData.setLongitude(vehicleevent.getLongitude());
			if (vehicleevent.getBattery() != null)
				vehicleData.setBatteryvoltage(vehicleevent.getBattery()
						.toString());
			vehicleData.setDirection(vehicleevent.getDirection() == null ? 0
					: vehicleevent.getDirection());
			vehicleData.setLockstatus(vehicle.getLockStatus() == null ? 0
					: vehicle.getLockStatus());
			vehicleData.setTemp1Max(String.valueOf(vehicleevent.getTags()));
			vehicleData.setTemp2Max(String.valueOf(vehicleevent
					.getTempSensor2()));
			vehicleData.setTemp3Max(String.valueOf(vehicleevent
					.getTempSensor3()));

			float f = 0;
			if (String.valueOf(vehicleevent.getAi1()) != null
					&& !String.valueOf(vehicleevent.getAi1()).equalsIgnoreCase(
							"null")) {
				vehicleData.setaimv1(vehicleevent.getAi1());
				f = getanalogReading(dashboardVin, 9, vehicleevent.getAi1());
				if (f != -1) {
					vehicleData.setAnalogInput1(f + "");
				}
			}
			if (String.valueOf(vehicleevent.getAi2()) != null
					&& !String.valueOf(vehicleevent.getAi2()).equalsIgnoreCase(
							"null")) {
				vehicleData.setaimv2(vehicleevent.getAi2());
				f = getanalogReading(dashboardVin, 10, vehicleevent.getAi2());
				if (f != -1) {
					vehicleData.setAnalogInput2(f + "");
				}
			}
			if (String.valueOf(vehicleevent.getAi3()) != null
					&& !String.valueOf(vehicleevent.getAi3()).equalsIgnoreCase(
							"null")) {
				vehicleData.setaimv3(vehicleevent.getAi3());
				f = getanalogReading(dashboardVin, 11, vehicleevent.getAi3());
				if (f != -1) {
					vehicleData.setAnalogInput3(f + "");
				}
			}
			if (String.valueOf(vehicleevent.getAi4()) != null
					&& !String.valueOf(vehicleevent.getAi4()).equalsIgnoreCase(
							"null")) {
				vehicleData.setaimv4(vehicleevent.getAi4());
				f = getanalogReading(dashboardVin, 19, vehicleevent.getAi4());
				if (f != -1) {
					vehicleData.setAnalogInput4(f + "");
				}
			}

			// Digital Input
			if (String.valueOf(vehicleevent.getDi1()) != null
					&& !String.valueOf(vehicleevent.getDi1()).equalsIgnoreCase(
							"null")) {
				vehicleData
						.setDigitalInput1((vehicleevent.getDi1() == 1) ? "ON"
								: "OFF");
			}
			if (String.valueOf(vehicleevent.getDi2()) != null
					&& !String.valueOf(vehicleevent.getDi2()).equalsIgnoreCase(
							"null")) {
				vehicleData
						.setDigitalInput2((vehicleevent.getDi2() == 1) ? "ON"
								: "OFF");
			}
			if (String.valueOf(vehicleevent.getDi3()) != null
					&& !String.valueOf(vehicleevent.getDi3()).equalsIgnoreCase(
							"null")) {
				vehicleData
						.setDigitalInput3((vehicleevent.getDi3() == 1) ? "ON"
								: "OFF");
			}
			if (String.valueOf(vehicleevent.getDi4()) != null
					&& !String.valueOf(vehicleevent.getDi4()).equalsIgnoreCase(
							"null")) {
				vehicleData
						.setDigitalInput4((vehicleevent.getDi4() == 1) ? "ON"
								: "OFF");
			}
			if (vehicleevent.getIoevent() != null) {
				String[] ioEvents = vehicleevent.getIoevent().split(",");
				for (String ioEventArr : ioEvents) {
					String[] ioEvent = ioEventArr.split("=");
					if (ioEvent[0].equalsIgnoreCase("98")) {
						vehicleData.setObdFuelLevel(ioEvent[1] == null ? 0
								: Integer.parseInt(ioEvent[1]));
					} else if (ioEvent[0].equalsIgnoreCase("97")) {
						vehicleData.setObdAmbientTemp(ioEvent[1] == null ? 0
								: Integer.parseInt(ioEvent[1]));
					} else if (ioEvent[0].equalsIgnoreCase("102")) {
						vehicleData.setObdDistance(ioEvent[1] == null ? 0
								: Integer.parseInt(ioEvent[1]));
					} else if (ioEvent[0].equalsIgnoreCase("96")) {
						vehicleData.setObdECT(ioEvent[1] == null ? 0 : Integer
								.parseInt(ioEvent[1]));
					} else if (ioEvent[0].equalsIgnoreCase("100")) {
						vehicleData.setObdEFR(ioEvent[1] == null ? 0 : Float
								.parseFloat(ioEvent[1]));
					} else if (ioEvent[0].equalsIgnoreCase("94")) {
						vehicleData.setObdRPM(ioEvent[1] == null ? 0 : Float
								.parseFloat(ioEvent[1]));
					} else if (ioEvent[0].equalsIgnoreCase("95")) {
						vehicleData.setObdVehicleSpeed(ioEvent[1] == null ? 0
								: Integer.parseInt(ioEvent[1]));
					} else if (ioEvent[0].equalsIgnoreCase("27")) {
						if (ioEvent[1] != null
								&& Integer.parseInt(ioEvent[1]) != 100
								&& Integer.parseInt(ioEvent[1]) != 255) {
							vehicleData.setGsmSignalStrength(String
									.valueOf(Integer.parseInt(ioEvent[1]) / 6));
						} else {
							vehicleData.setGsmSignalStrength("0");
						}
					} else if (ioEvent[0].equalsIgnoreCase("29")) {
						if (ioEvent[1] != null
								&& Integer.parseInt(ioEvent[1]) > 1000)
							vehicleData.setPowerSupplyVoltage("ON");
						else
							vehicleData.setPowerSupplyVoltage("OFF");
					} else if (ioEvent[0].equalsIgnoreCase("30")) {
						if (ioEvent[1] != null) {
							if (Integer.parseInt(ioEvent[1]) > 60000)
								vehicleData.setBatteryvoltage("Very High");
							else if (Integer.parseInt(ioEvent[1]) > 40000)
								vehicleData.setBatteryvoltage("High");
							else if (Integer.parseInt(ioEvent[1]) > 20000)
								vehicleData.setBatteryvoltage("Medium");
							else
								vehicleData.setBatteryvoltage("Low");
						} else {
							vehicleData.setBatteryvoltage("Low");
						}
					} else if (ioEvent[0].equalsIgnoreCase("176")) {
						if (ioEvent[1] != null)
							vehicleData.setGps(true);
						else
							vehicleData.setGps(false);
					}

					else if (ioEvent[0].equalsIgnoreCase("85")) {
						canData.put("rpm", ioEvent[1]);
					} else if (ioEvent[0].equalsIgnoreCase("81")) {
						canData.put("currentSpeed", ioEvent[1]);
					} else if (ioEvent[0].equalsIgnoreCase("135")) {
						canData.put("wheelSpeed", ioEvent[1]);
					} else if (ioEvent[0].equalsIgnoreCase("84")) {
						canData.put("fuelLitre", ioEvent[1]);
					} else if (ioEvent[0].equalsIgnoreCase("89")) {
						canData.put("fuelPercentage", ioEvent[1]);
					} else if (ioEvent[0].equalsIgnoreCase("103")) {
						canData.put("engineHours", String.valueOf(Integer
								.parseInt(ioEvent[1]) / 60));
					} else if (ioEvent[0].equalsIgnoreCase("87")) {
						canData.put("totDistance", String.valueOf(Integer
								.parseInt(ioEvent[1]) / 1000));
					} else if (ioEvent[0].equalsIgnoreCase("107")) {
						canData.put("fuelConsumed", String.valueOf(Integer
								.parseInt(ioEvent[1]) / 10));
					} else if (ioEvent[0].equalsIgnoreCase("151")) {
						canData.put("batteryTemperature", String
								.valueOf(Integer.parseInt(ioEvent[1]) / 10));
					} else if (ioEvent[0].equalsIgnoreCase("152")) {
						canData.put("batteryLevel",
								String.valueOf(Integer.parseInt(ioEvent[1])));
					} else if (ioEvent[0].equalsIgnoreCase("82")) {
						canData.put("pedalPosition", ioEvent[1]);
					} else if (ioEvent[0].equalsIgnoreCase("235")) {
						canData.put("oilPressure", ioEvent[1]);
					} else if (ioEvent[0].equalsIgnoreCase("112")) {
						canData.put("adBlueLevel", String.valueOf(Integer
								.parseInt(ioEvent[1]) / 10));
					} else if (ioEvent[0].equalsIgnoreCase("132")) {
						String securityState[] = ioEvent[1].split(";");
						for (String value : securityState) {
							canData.put(value.split(":")[0],
									value.split(":")[1]);
						}
					} else if (ioEvent[0].equalsIgnoreCase("hbd")) {
						String hbio[] = ioEvent[1].split(";");
						Date hbdeventTimeStamp = sdfTime.parse(hbio[2]);
						if (hbio.length > 5) {
							vehicleData
									.setGps(hbio[1].equalsIgnoreCase("ON") ? true
											: false);
							vehicleData.setGsmSignalStrength(hbio[3]);
							vehicleData.setBatteryvoltage(hbio[5]);
							vehicleData.setPowerSupplyVoltage(hbio[6]);
						}
						long diffSeconds = (hbdeventTimeStamp.getTime() - eventTimeStamp
								.getTime()) / 1000;
						if (hbdeventTimeStamp.after(eventTimeStamp)
								&& diffSeconds > 120) {
							vehicleData.setTimeStamp(TimeZoneUtil
									.getStrTZ(hbdeventTimeStamp));
							long nhdeventTimeStampInSec = hbdeventTimeStamp
									.getTime() / 1000;
							long differenceSec = todayInSec
									- nhdeventTimeStampInSec;
							if (diff < differenceSec) {
								vehicleData.setStatus(NO_TRANS);
							} else if (hbio[4].equalsIgnoreCase("true")) {
								vehicleData.setStatus("Idle");
							} else {
								vehicleData.setStatus("Stop");
							}
							vehicleData.setSpeed(0);
						}
					}
				}
			}
			vehicleData.setCanbus(canData);
			vehicleData.setSubject(vehicleevent.getIoevent());
			liveDataMap.put(dashboardVin, vehicleData);
			dashboardData.setLiveDatas(liveDataMap);
			return dashboardData;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean checkTowedStatus(String vin) {
		List<Vehicleevent> ve = getPrevValues(vin);
		boolean isTowed = false;
		for (int k = 0; k < ve.size(); k++) {
			if (!ve.get(k).getEngine() && ve.get(k).getSpeed() > 0) {
				isTowed = true;
			} else {
				isTowed = false;
				break;
			}
		}
		return isTowed;
	}

	public List<Vehicleevent> getPrevValues(String vin) {
		LOGGER.info("AlertsEJB::getGeoPrevVe::Entered into this method::vin"
				+ vin);
		try {
			Query query = em
					.createQuery("select e from Vehicleevent e where e.id.vin=:vin order by e.id.eventTimeStamp desc");
			query.setMaxResults(5);
			query.setParameter("vin", vin);
			LOGGER.info("Before Execute Query query" + query);
			List<Vehicleevent> vehicleevent = (List<Vehicleevent>) query
					.getResultList();
			LOGGER.info("After Excecute Query" + query);
			LOGGER.info("FleetTrackingDeviceListenerBO::getPrevVe::Leaving from this method successfully");
			return vehicleevent;
		} catch (Exception e) {
			LOGGER.error("FleetTrackingDeviceListenerBO::getPrevVe::Exception occured::Geo Qry error="
					+ e);
			return null;
		}
	}

	private float getanalogReading(String vin, int type, int millivolts) {
		float num = -1;
		try {

			Query q = em
					.createNativeQuery("select litres FROM vehiclefuelreading where vin = :vin and type = :type and millivolts = "
							+ "(select max(millivolts) from vehiclefuelreading where millivolts <= :millivolts and vin=:vin and type=:type)");
			q.setParameter("vin", vin);
			q.setParameter("millivolts", millivolts);
			q.setParameter("type", type);
			List<Object> list = q.getResultList();
			if (!list.isEmpty()) {
				num = Float.parseFloat(list.get(0).toString());
			}

		} catch (Exception e) {
			LOGGER.error("getanalogReading : " + e);
			// return -1;
		}
		return num;
	}

	@Override
	public Heartbeatevent getPrevHeartbeatEvent(String vin, Date timeStamp) {
		/*
		 * LOGGER.error(
		 * "INFO FleetTrackingDeviceListenerBO::getPrevHeartbeatEvent::Entered into this method::vin"
		 * + vin);
		 */
		try {
			Query query = em
					.createQuery("select hb from Heartbeatevent hb where hb.id.vin=:vin and hb.id.timeStamp < :timeStamp order by hb.id.timeStamp desc");
			query.setMaxResults(1);
			query.setParameter(STR_VIN, vin);
			query.setParameter(STR_TIMESTAMP, timeStamp);
			LOGGER.debug("Before Execute Query query" + query);

			List<Heartbeatevent> objects = (List<Heartbeatevent>) query
					.getResultList();
			if (!objects.isEmpty() && objects.size() != 0) {
				Heartbeatevent heartbeatevent = objects.get(0);
				LOGGER.debug("After Excecute Query" + query);
				LOGGER.info("FleetTrackingDeviceListenerBO::getPrevHeartbeatEvent::Leaving from this method successfully");
				return heartbeatevent;
			}
			return null;
		} catch (Exception e) {
			LOGGER.error("FleetTrackingDeviceListenerBO::getPrevHeartbeatEvent::Exception occured::Geo Qry error="
					+ e);
			return null;
		}
	}

	@Override
	public Vehicleevent getLatestVe(String nearByVins) {
		String vinArr[] = nearByVins.split(",");
		String stmt = "";
		try {
			for (int i = 0; i < vinArr.length; i++) {
				stmt += "(SELECT v.`vin`,v.`eventTimeStamp`,v.`serverTimeStamp`,v.`latitude`,v.`longitude`,v.`speed`,v.`ioevent`,v.`engine`,v.`direction` FROM vehicleevent AS v WHERE v.eventTimeStamp = (SELECT MAX(t.eventTimeStamp) FROM vehicleevent t WHERE t.vin= '"
						+ vinArr[i] + "') AND v.vin = '" + vinArr[i] + "')";
				if (i < vinArr.length - 1) {
					stmt += " UNION ";
				}
			}
			stmt += " ORDER BY eventTimeStamp desc LIMIT 1";
			Query query = em.createNativeQuery(stmt);
			List<Object[]> listVehicleevents = (List<Object[]>) query
					.getResultList();
			if (!listVehicleevents.isEmpty() && listVehicleevents.size() != 0) {
				Vehicleevent vehicleevent = new Vehicleevent();
				VehicleeventId vehicleeventId = new VehicleeventId();
				Object[] obj = (Object[]) listVehicleevents.get(0);
				vehicleeventId.setVin((String) obj[0]);
				vehicleeventId.setEventTimeStamp((Date) obj[1]);
				vehicleevent.setServerTimeStamp((Date) obj[2]);
				vehicleevent.setLatitude((Double) obj[3]);
				vehicleevent.setLongitude((Double) obj[4]);
				vehicleevent.setSpeed((Integer) obj[5]);
				vehicleevent.setIoevent((String) obj[6]);
				vehicleevent.setEngine(((Byte) obj[7]) == 1);
				vehicleevent.setDirection((Integer) obj[8]);
				vehicleevent.setId(vehicleeventId);
				LOGGER.debug("After Excecute Query" + query);
				LOGGER.info("FleetTrackingDeviceListenerBO::getLatestVe::Leaving from this method successfully");
				return vehicleevent;
			}
			return null;
		} catch (Exception e) {
			LOGGER.error("FleetTrackingDeviceListenerBO::getLatestVe::Exception occured::error="
					+ e);
			return null;
		}

	}

	@Override
	public void persistHealthEvent(Ais140Health ais140Health,
			VehicleComposite vehicleComposite) {
		LOGGER.info("Entered persistHealthEvent Method");
		Ais140Health healthCheck = null;
		try {
			Ais140HealthId ais140HealthId = ais140Health.getId();
			healthCheck = em.find(Ais140Health.class, ais140HealthId);
		} catch (Exception e) {
			LOGGER.error("FleetTrackingDeviceListenerBO::persistHealthEvent: "
					+ e.getMessage());

		}
		try {
			if (healthCheck == null) {
				em.persist(ais140Health);
				LOGGER.info("FleetTrackingDeviceListenerBO::persistHealthEhvent::HealthEvent persisted");
			}

		} catch (PersistenceException e) {
			LOGGER.error("FleetTrackingDeviceListenerBO::persistHealthEvent::NonUniqueObjectException occured"
					+ e);
			em.flush();
		} catch (Exception e) {
			LOGGER.error("FleetTrackingDeviceListenerBO::persistHealthEvent::Exception occured"
					+ e);
		}
	}

	@Override
	public void persistEmergencyEvent(Ais140Emergency ais140Emergency,
			VehicleComposite vehicleComposite) {
		LOGGER.info("Entered persistEmergencyEvent Method");
		Ais140Emergency emgCheck = null;
		try {
			Ais140EmergencyId ais140EmergencyId = ais140Emergency.getId();
			emgCheck = em.find(Ais140Emergency.class, ais140EmergencyId);
		} catch (Exception e) {
			LOGGER.error("FleetTrackingDeviceListenerBO::persistEmergencyEvent: "
					+ e.getMessage());
		}
		try {
			if (emgCheck == null) {
				em.persist(ais140Emergency);
				LOGGER.info("FleetTrackingDeviceListenerBO::persistEmergencyEvent::EmergencyEvent persisted");
			}

		} catch (PersistenceException e) {
			LOGGER.error("FleetTrackingDeviceListenerBO::persistEmergencyEvent::NonUniqueObjectException occured"
					+ e);
			em.flush();
		} catch (Exception e) {
			LOGGER.error("FleetTrackingDeviceListenerBO::persistEmergencyEvent::Exception occured"
					+ e);
		}

	}

	@Override
	public void updateSupervisor(String vin, Position position) {
		try {
			TicketSupervisor ts = em.find(TicketSupervisor.class, vin);
			if (ts != null) {
				ts.setLocation(position.getLatitude() + ","
						+ position.getLongitude());
				em.merge(ts);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateWorkshop(String vin, Position position) {
		try {
			TicketWorkshop tw = em.find(TicketWorkshop.class, vin);
			if (tw != null) {
				tw.setLocation(position.getLatitude() + ","
						+ position.getLongitude());
				em.merge(tw);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int addy(String durationString) {
		int hours;
		int minutes;
		int seconds;
		String[] durationArray = durationString.split(":");
		hours = (Integer.valueOf(durationArray[0])) * 3600;
		minutes = (Integer.valueOf(durationArray[1])) * 60;
		seconds = Integer.valueOf(durationArray[2]);
		return hours + minutes + seconds;
	}

	@Override
	public boolean sendMessageforClient(String vin, JSONObject obj) {
		boolean status = false;
		try {
			Query q1 = em
					.createQuery("Select vhu from VehicleHasUser vhu where vhu.id.vin=:vin");
			q1.setParameter("vin", vin);
			List<VehicleHasUser> users = q1.getResultList();
			if (users != null) {
				for (VehicleHasUser vhu : users) {
					if (ticketMap.containsKey(vhu.getId().getUserId())) {
						List<Session> sessions = ticketMap.get(vhu.getId()
								.getUserId());
						for (Iterator<Session> iterator = sessions.iterator(); iterator
								.hasNext();) {
							Session session = iterator.next();
							if (session.isOpen()) {
								session.getBasicRemote().sendText(
										obj.toString());
								status = true;
							} else {
								iterator.remove();
							}
						}
						ticketMap.put(vhu.getId().getUserId(), sessions);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	public String getrifdStatus(String vin) {
		String stmt = "SELECT opreAre FROM vehicle WHERE vin = :vin";
		Query query = em.createNativeQuery(stmt);
		query.setParameter("vin", vin);
		String enableDisable = (((String) query.getSingleResult()).split("|")[0])
				.split("_")[1];
		return enableDisable;
	}

	public int getOverspeeedAlertRange(String vin, String compID) {

		int overSpeedRange = 60;
		Query alertRange = em
				.createQuery("SELECT ac from Alertconfig ac WHERE ac.id.vin=:vin AND ac.id.companyId=:compId And ac.id.alertType=:alertType");
		alertRange.setMaxResults(1);
		alertRange.setParameter("vin", vin);
		alertRange.setParameter("compId", compID);
		alertRange.setParameter("alertType", "OVERSPEED");

		List<Alertconfig> alertconfigs = alertRange.getResultList();
		if (alertconfigs != null && alertconfigs.size() > 0) {
			Alertconfig alertconfig = alertconfigs.get(0);
			overSpeedRange = Integer.parseInt(alertconfig.getAlertRange());
		}
		return overSpeedRange;
	}

	public int getTodayOdometer(String vin, String eventTimeStamp) {
		int odometerValue = 0;
		try {
			String getOdometerReading = "select SUM(odometer) from vehicleevent v WHERE v.vin=:vNo and v.eventTimeStamp between '"
					+ eventTimeStamp
					+ " 00:00:00' AND '"
					+ eventTimeStamp
					+ " 23:59:59'";
			Query query = em.createNativeQuery(getOdometerReading);
			query.setParameter("vNo", vin);
			Object object = query.getSingleResult();
			if (object != null) {
				odometerValue = ((BigDecimal) object).intValueExact();
			}
		} catch (Exception e) {
			LOGGER.error("Odometer" + e);
		}
		return odometerValue;
	}

	private long distance(double lat1, double lng1, double lat2, double lng2) {
		double earthRadius = 6371000; // meters
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return (long) (earthRadius * c);
	}

	@Override
	public void persistHistoryEvent(Vehicleevent vehicleEvent) {
		VehicleeventId id = new VehicleeventId(vehicleEvent.getId().getVin(),
				vehicleEvent.getId().getEventTimeStamp());
		Vehicleevent veChk = em.find(Vehicleevent.class, id);
		if (veChk == null) {
			em.persist(vehicleEvent);
		}
	}
}