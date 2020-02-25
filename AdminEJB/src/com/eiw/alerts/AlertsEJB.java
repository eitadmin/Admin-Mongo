package com.eiw.alerts;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.websocket.CloseReason;
import javax.websocket.Session;

import org.jboss.logging.Logger;
import org.json.JSONObject;

import com.eiw.client.dto.CompanyData;
import com.eiw.device.ejb.VehicleComposite;
import com.eiw.server.MiscHttpRequest;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.companyadminpu.Provider;
import com.eiw.server.companyadminpu.Pushnotificationdevices;
import com.eiw.server.companyadminpu.Serverkey;
import com.eiw.server.companyadminpu.ServerkeyId;
import com.eiw.server.companyadminpu.Smsconfig;
import com.eiw.server.companyadminpu.SmsconfigId;
import com.eiw.server.companyadminpu.Smssent;
import com.eiw.server.fleettrackingpu.Alertconfig;
import com.eiw.server.fleettrackingpu.Calendarevent;
import com.eiw.server.fleettrackingpu.Emailvalidation;
import com.eiw.server.fleettrackingpu.Freeformgeo;
import com.eiw.server.fleettrackingpu.Fueltanklid;
import com.eiw.server.fleettrackingpu.FueltanklidId;
import com.eiw.server.fleettrackingpu.Geozones;
import com.eiw.server.fleettrackingpu.Heartbeatevent;
import com.eiw.server.fleettrackingpu.Hourmeter;
import com.eiw.server.fleettrackingpu.Landmarks;
import com.eiw.server.fleettrackingpu.Maintenancedue;
import com.eiw.server.fleettrackingpu.MaintenancedueId;
import com.eiw.server.fleettrackingpu.Neighbourhoodviolation;
import com.eiw.server.fleettrackingpu.Operator;
import com.eiw.server.fleettrackingpu.Route;
import com.eiw.server.fleettrackingpu.TicketInfo;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.VehicleHasFreeform;
import com.eiw.server.fleettrackingpu.VehicleHasGeozones;
import com.eiw.server.fleettrackingpu.VehicleHasIo;
import com.eiw.server.fleettrackingpu.VehicleHasLandmark;
import com.eiw.server.fleettrackingpu.VehicleHasOdometer;
import com.eiw.server.fleettrackingpu.VehicleHasRoute;
import com.eiw.server.fleettrackingpu.Vehiclealerts;
import com.eiw.server.fleettrackingpu.Vehicleevent;
import com.eiw.server.fleettrackingpu.VehicleeventId;
import com.eiw.server.studenttrackingpu.Alertevents;
import com.eiw.server.studenttrackingpu.Studentalertsubscription;
import com.eiw.server.studenttrackingpu.Studentevent;
import com.skt.client.dto.LatLng;

import flexjson.JSONSerializer;

@Stateless
public class AlertsEJB implements AlertsEJBRemote {

	@PersistenceContext(unitName = "ltmsfleettrackingpu")
	protected EntityManager em;

	@PersistenceContext(unitName = "ltmscompanyadminpu")
	protected EntityManager emAdmin;

	// org.hibernate.SessionFactory ss2 = new Configuration().configure()
	// .buildSessionFactory();
	// @PersistenceContext(unitName = "studenttrackingpu", type =
	// PersistenceContextType.EXTENDED)
	// org.hibernate.Session sessionStudent = ss2.openSession();

	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	public static final Logger LOGGER = Logger.getLogger("alerts");
	private static final String STR_LID_STATUS = "lidStatus";
	public static Map<String, List<VehicleHasFreeform>> vehicleHasFreeformMap = new HashMap<String, List<VehicleHasFreeform>>();
	public static Map<String, List<VehicleHasFreeform>> neighbourhoodVehicleHasFreeformMap = new HashMap<String, List<VehicleHasFreeform>>();
	public static Map<Integer, Freeformgeo> freeFormMap = new HashMap<Integer, Freeformgeo>();
	public static Map<Integer, List<Freeformgeo>> freeFormMapByPos = new HashMap<Integer, List<Freeformgeo>>();
	private static final String CORP_ID = "corpId", STR_VIN = "vin",
			STR_TIMESTAMP = "timeStamp";
	public static Map<String, CompanyData> antiTheftMap = new HashMap<String, CompanyData>();

	public static Map<Session, String> TicketMangementMap = new HashMap<Session, String>();

	@Override
	public Vehicle getVehicle(String vin) {
		return em.find(Vehicle.class, vin);
	}

	@Override
	public List<Alertconfig> getAlertConfig(String vin) {
		LOGGER.info("AlertsEJB::getAlertConfig::Entered into this method"
				+ "vin" + vin);
		try {
			String getVehicleConfig = "SELECT ac FROM Alertconfig ac WHERE vin = '"
					+ vin + "' AND alertStatus = '1'";
			Query vehiConfigQuery = em.createQuery(getVehicleConfig);
			LOGGER.info("Before Query Excecute Query::" + vehiConfigQuery);
			List<Alertconfig> alertConfigs = (List<Alertconfig>) vehiConfigQuery
					.getResultList();
			return alertConfigs;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::getAlertConfig::Error in getting alert config for vin="
					+ vin + " : e");
			return null;
		}
	}

	@Override
	public List<VehicleHasGeozones> getGeoConfig(String vin, String mode) {
		LOGGER.info("AlertsEJB::getGeoConfig::Entered into this method" + "Vin"
				+ vin + "mode" + mode);
		try {

			Query query = em
					.createQuery("SELECT vhg FROM VehicleHasGeozones vhg WHERE vhg.id.vehicleVin = :vin  and vhg.mode = :mode and vhg.status is true");
			query.setParameter("vin", vin);
			query.setParameter("mode", mode);
			LOGGER.debug("Before Execute Query::" + query);
			List<VehicleHasGeozones> vehicleHasGeozones = query.getResultList();
			LOGGER.debug("After Execute Query" + query);
			for (VehicleHasGeozones vhg : vehicleHasGeozones) {
				long id = vhg.getId().getGeozonesId();
				Geozones geozones = em.find(Geozones.class, id);
				vhg.setGeozones(geozones);
			}
			LOGGER.info("AlertsEJB::getGeoConfig::Leaving from this method successfully");
			return vehicleHasGeozones;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::getGeoConfig::Error Occured in Vehicle has Geo="
					+ e);
			return null;
		}
	}

	// Freeform
	@Override
	public List<VehicleHasFreeform> getFreeformGeoConfig(String vin, String mode) {
		LOGGER.info("AlertsEJB::getFreeformGeoConfig::Entered into this method::"
				+ "vin" + vin);
		try {
			if (mode != null) {
				if (neighbourhoodVehicleHasFreeformMap.get(vin) == null) {
					Query query = em
							.createQuery("SELECT vhg FROM VehicleHasFreeform vhg WHERE vhg.id.vin = :vin  and vhg.status is true and vhg.cronReport is true");
					query.setParameter("vin", vin);
					LOGGER.info("Before Execute Query::" + query);
					List<VehicleHasFreeform> vehicleHasFreeforms = query
							.getResultList();
					if (!vehicleHasFreeforms.isEmpty()
							&& vehicleHasFreeforms.size() != 0) {
						neighbourhoodVehicleHasFreeformMap.put(vin,
								vehicleHasFreeforms);
					}
					LOGGER.info("AlertsEJB::getFreeformGeoConfig::Leaving from sucessfully");
					return vehicleHasFreeforms;
				} else {
					return neighbourhoodVehicleHasFreeformMap.get(vin);
				}
			} else {
				if (vehicleHasFreeformMap.get(vin) == null) {
					Query query = em
							.createQuery("SELECT vhg FROM VehicleHasFreeform vhg WHERE vhg.id.vin = :vin  and vhg.status is true and vhg.cronReport is false");
					query.setParameter("vin", vin);
					LOGGER.info("Before Execute Query::" + query);
					List<VehicleHasFreeform> vehicleHasFreeforms = query
							.getResultList();
					if (!vehicleHasFreeforms.isEmpty()
							&& vehicleHasFreeforms.size() != 0) {
						vehicleHasFreeformMap.put(vin, vehicleHasFreeforms);
					}
					LOGGER.info("After Execute Query" + query);
					LOGGER.info("AlertsEJB::getFreeformGeoConfig::Leaving from sucessfully");
					return vehicleHasFreeforms;
				} else {
					return vehicleHasFreeformMap.get(vin);
				}
			}
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::getFreeformGeoConfig::Error Occured in veh has freeform::"
					+ e);
			return null;
		}
	}

	@Override
	public Freeformgeo getFreeformGeo(int id) {
		LOGGER.info("AlertsEJB::getFreeformGeo::Entered into this method::"
				+ "id" + id);
		try {
			if (freeFormMap.get(id) == null) {
				Query query = em
						.createQuery("SELECT f FROM Freeformgeo f WHERE f.id.id = :id");
				query.setParameter("id", id);
				LOGGER.info("Before Execute Query::" + query);
				Freeformgeo f = (Freeformgeo) query.getResultList().get(0);
				freeFormMap.put(id, f);
				LOGGER.info("After Execute Query::" + query);
				LOGGER.info("AlertsEJB::getFreeformGeo::leaving from this method successfully");
				return f;
			} else {
				return freeFormMap.get(id);
			}
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::getFreeformGeo::Error Occured in free form geo="
					+ e);
			return null;
		}
	}

	@Override
	public boolean getFreeformGeoStatus(Vehicleevent vehicleEvent, int id) {
		LatLng curLatlng = new LatLng();
		String latVe = String.valueOf(vehicleEvent.getLatitude());
		String lngVe = String.valueOf(vehicleEvent.getLongitude());
		curLatlng.setLat(latVe);
		curLatlng.setLng(lngVe);

		LOGGER.info("AlertsEJB::getFreeformGeoStatus::Entered into this method::"
				+ "vehicleEvent" + vehicleEvent + "id" + id);
		List<Freeformgeo> trs;
		try {
			if (freeFormMapByPos.get(id) == null) {
				Query q = em
						.createQuery("select tr from Freeformgeo tr where tr.id.id = :id order by tr.pos");
				q.setParameter("id", id);
				LOGGER.info("Before Execute Query::q" + q);
				trs = q.getResultList();
				freeFormMapByPos.put(id, trs);
				LOGGER.info("After Execute Query::q" + q);
			} else {
				trs = freeFormMapByPos.get(id);
			}
			// StringBuilder sbuf = new StringBuilder();
			// boolean isFirst = true;
			// String firstVal = "";
			// for (Freeformgeo tr : trs) {
			// String latlngCen = tr.getId().getLatlng().replace(",", " ");
			// if (isFirst) {
			// firstVal = latlngCen;
			// isFirst = false;
			// }
			// sbuf.append(latlngCen + ",");
			// }
			// sbuf.append(firstVal);
			//
			// Query q1 = em
			// .createNativeQuery("SELECT fleettrackingdb.myWithin(GEOMFROMTEXT('Point("
			// + latVe
			// + " "
			// + lngVe
			// + ")'),GEOMFROMTEXT('POLYGON(("
			// + sbuf.toString()
			// + "))'));");
			// LOGGER.info("Before Execute Query::q1" + q1);
			// Object object = (Object) q1.getSingleResult();
			// LOGGER.info("After Execute Queryq1" + q1);
			// String str = String.valueOf(object);
			// LOGGER.info("AlertsEJB::getFreeformGeoStatus::Leaving from this method successfully");
			List<LatLng> polygon = new ArrayList<LatLng>();
			for (Freeformgeo tr : trs) {
				String[] latAndLng = tr.getId().getLatlng().split(",");
				LatLng latlng = new LatLng();
				latlng.setLat(latAndLng[0]);
				latlng.setLng(latAndLng[1]);
				polygon.add(latlng);
			}
			return IsPointInPolygon(polygon, curLatlng);
			// return str.trim().equalsIgnoreCase("0") ? false : true;

		} catch (Exception e) {
			LOGGER.error("AlertsEJB::getFreeformGeoStatus::Geo query Error="
					+ e);
			e.printStackTrace();
			return false;
		}
	}

	// Road geo
	@Override
	public List<VehicleHasRoute> getRoadGeoConfig(String vin) {
		LOGGER.info("AlertsEJB::getRoadGeoConfig::vin" + vin);
		try {

			Query query = em
					.createQuery("SELECT vhg FROM VehicleHasRoute vhg WHERE vhg.id.vin = :vin  and vhg.status is true");
			query.setParameter("vin", vin);
			LOGGER.info("Before Execute Query::q1" + query);
			List<VehicleHasRoute> vehicleHasFreeforms = query.getResultList();
			LOGGER.info("After Execute Queryq1" + query);
			LOGGER.info("AlertsEJB::getRoadGeoConfig::Leaving from this method is Successfully");
			return vehicleHasFreeforms;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::getRoadGeoConfig::Error in veh has Route=::"
					+ e);
			return null;
		}
	}

	@Override
	public Route getRoadGeo(int id) {
		LOGGER.info("AlertsEJB::getRoadGeo::Entered into this method:: id" + id);
		try {

			Query query = em
					.createQuery("SELECT f FROM Route f WHERE f.id.id = :id");
			query.setParameter("id", id);
			LOGGER.info("Before Execute Query::query" + query);
			Route f = (Route) query.getResultList().get(0);
			LOGGER.info("After Execute Query::query" + query);
			LOGGER.info("AlertsEJB::getRoadGeo::Leaving from this method successfully");
			return f;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::getRoadGeo::Error occured in route=" + e);
			return null;
		}
	}

	@Override
	public boolean getRoadGeoStatus(Vehicleevent vehicleEvent, int id) {
		LOGGER.info("AlertsEJB::getRoadGeoStatus::Entered into this method::"
				+ "vehicleEvent" + vehicleEvent + "id" + id);
		int isGeo = 0;
		boolean status = false;
		String latVe = String.valueOf(vehicleEvent.getLatitude());
		String lngVe = String.valueOf(vehicleEvent.getLongitude());
		try {
			Query q = em
					.createQuery("select tr from Route tr where tr.id.id = :id order by tr.pos");
			q.setParameter("id", id);
			LOGGER.info("Before Execute Query::q" + q);
			List<Route> trs = q.getResultList();
			LOGGER.info("After Execute Query::q" + q);
			for (Route tr : trs) {

				JSONObject radious = new JSONObject(tr.getAdditionalValues());
				double radious11 = Double.valueOf(radious.getString("width"));
				String latlngCen = tr.getId().getLatlng().replace(",", " ");
				String latlngRad = getLatLngCircle(tr.getId().getLatlng(),
						radious11);

				Query query = em
						.createNativeQuery("SELECT IF(((SELECT GLENGTH(GEOMFROMTEXT  ('LineString("
								+ latlngCen
								+ ","
								+ latlngRad
								+ ")')) AS distance)   > (SELECT GLENGTH(GEOMFROMTEXT('LineString("
								+ latlngCen
								+ ","
								+ latVe
								+ " "
								+ lngVe
								+ ")'))   AS distance)),'true','false') AS output");
				LOGGER.info("Before Execute Query query" + query);
				Object obj = (Object) query.getSingleResult();
				LOGGER.info("After Execute Query query" + query);

				isGeo = (String.valueOf(obj)).equalsIgnoreCase("true") ? 1 : 2;
				if (isGeo == 1) {
					status = true;
					break;
				}
			}
			if (isGeo == 2)
				status = false;
			return status;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::getRoadGeoStatus::Error Occured in Geo Qry error="
					+ e);
			return false;
		}
	}

	@Override
	public String getRoadGeoSubStationStatus(Vehicleevent vehicleEvent, int id) {
		LOGGER.info("AlertsEJB::getRoadGeoSubStationStatus::Entered into this method::"
				+ "vehicleEvent" + vehicleEvent + "id" + id);
		int isGeo = 0;
		Map<Integer, String> hashMap = new HashMap<Integer, String>();
		String status = "false";
		String latVe = String.valueOf(vehicleEvent.getLatitude());
		String lngVe = String.valueOf(vehicleEvent.getLongitude());
		try {
			Query q = em
					.createNativeQuery("SELECT subStations FROM route WHERE id =:id ORDER BY pos");
			q.setParameter("id", id);
			q.setMaxResults(1);
			LOGGER.info("Before Execute Query::q" + q);
			String subLatlngStr = (String) q.getSingleResult();
			JSONObject subLatlngsJobj = new JSONObject(subLatlngStr);
			LOGGER.info("After Execute Query::q" + q);
			String[] sublatlngsNames = JSONObject.getNames(subLatlngsJobj);
			for (int i = 0; i < sublatlngsNames.length; i++) {
				String[] subLatlngArray = subLatlngsJobj.getString(
						sublatlngsNames[i]).split("\\|");
				String latCen = subLatlngArray[0];
				String latRad = subLatlngArray[1];
				double circleDistance = distanceBetweenTwoPoints(
						Double.valueOf(latCen.split(",")[0]),
						Double.valueOf(latCen.split(",")[1]),
						Double.valueOf(latRad.split(",")[0]),
						Double.valueOf(latRad.split(",")[1]));
				double distance = distanceBetweenTwoPoints(
						Double.valueOf(latCen.split(",")[0]),
						Double.valueOf(latCen.split(",")[1]),
						Double.valueOf(latVe), Float.valueOf(lngVe));
				if (distance <= circleDistance) {
					hashMap.put(id, "true," + sublatlngsNames[i]);
				} else {
					hashMap.put(id, "false");
				}
			}

			return status;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::getRoadGeoStatus::Error Occured in Geo Qry error="
					+ e);
			return "false";
		}
	}

	// /////////////////////

	// Landmark geo
	@Override
	public List<VehicleHasLandmark> getLandmarkGeoConfig(String vin) {
		LOGGER.info("AlertsEJB::getLandmarkGeoConfig::Entered into this method::"
				+ "vin" + vin);
		try {

			Query query = em
					.createQuery("SELECT vhg FROM VehicleHasLandmark vhg WHERE vhg.id.vin = :vin  and vhg.status is true");
			query.setParameter("vin", vin);
			LOGGER.info("Before Execute Query query" + query);
			List<VehicleHasLandmark> vehicleHasFreeforms = query
					.getResultList();
			LOGGER.info("After Execute Query query" + query);
			LOGGER.info("AlertsEJB::getLandmarkGeoConfig::Leaving from this method Successfully");
			return vehicleHasFreeforms;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::getLandmarkGeoConfig::error in vehicle has landmark::"
					+ e);
			return null;
		}
	}

	@Override
	public Landmarks getLandmarkGeo(String id) {
		LOGGER.info("AlertsEJB::getLandmarkGeo::Entered into this method"
				+ "id" + id);
		try {

			Query query = em
					.createQuery("SELECT f FROM Landmarks f WHERE f.id.landmarkName = :id");
			query.setParameter("id", id);
			LOGGER.info("Before Execute Query query" + query);
			Landmarks f = (Landmarks) query.getResultList().get(0);
			LOGGER.info("After Execute Query query" + query);
			LOGGER.info("AlertsEJB::getLandmarkGeo::Leaving from this method successfully");
			return f;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::getLandmarkGeo::Exception Error in landmarks"
					+ e);
			return null;
		}
	}

	@Override
	public Map<String, Boolean> getLandmarkGeoStatusHashMap(String queryStr) {
		LOGGER.info("AlertsEJB::getLandmarkGeoStatusHashMap::" + "queryStr"
				+ queryStr);
		try {
			Map<String, Boolean> hashMap = new HashMap<String, Boolean>();
			Query query = em.createNativeQuery(queryStr);
			LOGGER.info("Before Execute Query query" + query);
			List<Object[]> objs = (List<Object[]>) query.getResultList();
			LOGGER.info("After Execute Query query" + query);
			for (Object[] objects : objs) {
				hashMap.put(String.valueOf(objects[0]),
						Boolean.parseBoolean(String.valueOf(objects[1])));
			}
			LOGGER.info("AlertsEJB::getLandmarkGeoStatusHashMap::Leaving from this method is successfully");
			return hashMap;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::getLandmarkGeoStatusHashMap::Exception occured::"
					+ e);
			return null;
		}
	}

	private String getLatLngCircle(String center, double rad) {
		LOGGER.info("AlertsEJB::getLatLngCircle::Entered into this method"
				+ "center" + center + "rad" + rad);
		String[] latng = center.split(",");
		double pi = 3.1415;
		double d = rad / 6378.8;
		double lat1 = (pi / 180) * Double.parseDouble(latng[0]);
		double lng1 = (pi / 180) * Double.parseDouble(latng[1]);
		double tc = (pi / 180) * 0;
		double y = Math.asin(Math.sin(lat1) * Math.cos(d) + Math.cos(lat1)
				* Math.sin(d) * Math.cos(tc));
		double dlng = Math.atan2(Math.sin(tc) * Math.sin(d) * Math.cos(lat1),
				Math.cos(d) - Math.sin(lat1) * Math.sin(y));
		double x = ((lng1 - dlng + pi) % (2 * pi)) - pi;
		String point = (y * (180 / pi) + " " + x * (180 / pi));
		LOGGER.info("AlertsEJB::getLatLngCircle::Leaving from this method successfully");
		return point;
	}

	@Override
	public boolean isAlertAlreadySent(String vin, String expectedTime,
			String interval, String alertType) {
		LOGGER.info("AlertsEJB::isAlertAlreadySent" + "vin" + vin
				+ "expectedTime" + expectedTime + "interval" + interval
				+ "alerttype" + alertType);
		try {
			String chkInterval = null;
			if (alertType.equalsIgnoreCase("WOKEUP")) {
				String timeStamp = sdfDate.format(sdfDate.parse(expectedTime));
				chkInterval = "SELECT * FROM vehiclealerts va WHERE vin = '"
						+ vin + "' AND alerttype = '" + alertType
						+ "' AND DATE(eventTimeStamp) ='" + timeStamp + "' ";
			} else if (alertType.equalsIgnoreCase("ONLEAVE")
					|| alertType.equalsIgnoreCase("ONENTER")) {
				return false;
			} else if (interval.equalsIgnoreCase("EMAIL")) {
				String timeStamp = sdfDate.format(sdfDate.parse(expectedTime));
				chkInterval = "SELECT * FROM vehiclealerts va WHERE vin = '"
						+ vin + "' AND alerttype = '" + alertType
						+ "' AND DATE(eventTimeStamp) ='" + timeStamp + "' ";
			} else {
				chkInterval = "SELECT * FROM vehiclealerts va WHERE vin = '"
						+ vin + "' AND alerttype = '" + alertType
						+ "' AND eventTimeStamp >= ('" + expectedTime
						+ "'-INTERVAL " + interval + " MINUTE)";
			}
			Query query = em.createNativeQuery(chkInterval);
			List<Object[]> alertList = query.getResultList();
			return (!alertList.isEmpty()) ? true : false;

		} catch (Exception e) {
			LOGGER.error("AlertsEJB::isAlertAlreadySent::Error in isAlertAlreadySent for vin="
					+ vin + "\nalertType= " + alertType + e);
			return false;
		}
	}

	@Override
	public boolean isSmsBalAvail(Vehiclealerts vehiclealerts) {
		LOGGER.info("AlertsEJB::isSmsBalAvail::" + "vehiclealerts"
				+ vehiclealerts);
		String vin = vehiclealerts.getVin();
		Vehicle vehicle = getVehicle(vin);
		String chkSmsCntBalance = "SELECT smsCnt FROM ltmscompanyadmindb.smsconfig WHERE companyId ='"
				+ vehicle.getCompanyId()
				+ "' and branchId ='"
				+ vehicle.getBranchId()
				+ "' and status=1 and smsCnt is not null";

		Query query = emAdmin.createNativeQuery(chkSmsCntBalance);
		int smsCnt = 0;
		try {
			LOGGER.info("Before Execute Query query" + query);
			List<BigInteger> objects = (List<BigInteger>) query.getResultList();
			if (!objects.isEmpty() && objects.size() != 0) {
				smsCnt = objects.get(0).intValue();
				LOGGER.info("After execute Query query" + query);
				LOGGER.info("AlertsEJB::isSmsBalAvail:: Company ="
						+ vehicle.getCompanyId() + " and smsCnt =" + smsCnt);
			}
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::isSmsBalAvail::Exception occured sms cnt balance error is "
					+ e);
			return false;
		}
		return (smsCnt > 0) ? true : false;

	}

	@Override
	public boolean isSmsBalAvail(List<String> listDatas) {
		LOGGER.info("AlertsEJB::isSmsBalAvail::entered into this method"
				+ listDatas);
		String companyId = listDatas.get(0);
		String branchId = listDatas.get(1);

		String chkSmsCntBalance = "SELECT smsCnt FROM ltmscompanyadmindb.smsconfig WHERE companyId ='"
				+ companyId
				+ "' and branchId ='"
				+ branchId
				+ "' and status=1 and smsCnt is not null";

		Query query = emAdmin.createNativeQuery(chkSmsCntBalance);
		int smsCnt = 0;
		try {
			LOGGER.info("Before Execute Query" + query);
			List<BigInteger> objects = (List<BigInteger>) query.getResultList();
			if (!objects.isEmpty() && objects.size() != 0) {
				smsCnt = objects.get(0).intValue();
				LOGGER.info("After execute Query query" + query);
			}
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::isSmsBalAvail::Exception occured::sms cnt balance error is"
					+ e);
			return false;
		}
		return (smsCnt > 0) ? true : false;

	}

	@Override
	public boolean insertSMS(List<String> listDatas) {
		LOGGER.info("AlertsEJB::insertSMS::Entered into this method::listDatas"
				+ listDatas);
		String companyId = listDatas.get(0);
		String branchId = listDatas.get(1);
		String mobileNo = listDatas.get(2);
		String userId = listDatas.get(4);
		String category = listDatas.get(5);
		String misc = listDatas.get(6);
		String fromNo = listDatas.get(7);
		String msg = listDatas.get(8);

		// need to add --
		SmsconfigId smsconfigId = new SmsconfigId();
		smsconfigId.setCompanyid(companyId);
		smsconfigId.setBranchId(branchId);
		Smsconfig smsconfig = emAdmin.find(Smsconfig.class, smsconfigId);
		long totSmsCnt = smsconfig.getSmsCnt();
		long actSmsCnt = totSmsCnt - 1;
		smsconfig.setSmsCnt(actSmsCnt);

		// capture in smssent table
		try {
			Smssent smssent = new Smssent();
			smssent.setCompanyid(companyId);
			smssent.setBranchId(branchId);
			smssent.setUserId(userId);
			smssent.setCategory(category);
			smssent.setMisc(misc);
			smssent.setFromMobile(fromNo);
			smssent.setToMobile(mobileNo);
			smssent.setMsg(msg);
			smssent.setMsgType("SMS");
			emAdmin.persist(smssent);
			LOGGER.info("AlertsEJB::insertSMS::Leaving from this method successfully");
			return true;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::insertSMS::error in persisting SMS" + e);
			return false;

		}
	}

	@Override
	public boolean insertVehicleAlert(Vehiclealerts vehiclealerts, String mode) {
		LOGGER.info("AlertsEJB::insertVehicleAlert::" + "vehiclealerts"
				+ vehiclealerts);
		try {
			if (mode != null) {
				Neighbourhoodviolation nv = new Neighbourhoodviolation();
				nv.setAlerttype(vehiclealerts.getAlerttype());
				nv.setSubalerttype(vehiclealerts.getSubalerttype());
				nv.setDescription(vehiclealerts.getDescription());
				nv.setEventTimeStamp(vehiclealerts.getEventTimeStamp());
				nv.setLatlng(vehiclealerts.getLatlng());
				nv.setSmsmobile(vehiclealerts.getSmsmobile());
				nv.setVin(vehiclealerts.getVin());
				nv.setShowstatus(vehiclealerts.getShowstatus());
				nv.setServerTimeStamp(vehiclealerts.getServerTimeStamp());
				nv.setSmsstatus(vehiclealerts.getSmsstatus());
				em.persist(nv);
			} else {
				em.persist(vehiclealerts);
			}
			return true;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::insertVehicleAlert::error in persisting VehicleAlert"
					+ e);
			return false;
		}
	}

	@Override
	public List<Vehicleevent> getIdleVehicleevents(Vehicleevent ve) {
		LOGGER.info("AlertsEJB::getIdleVehicleevents::Entered into this method::ve"
				+ ve);
		try {
			String eventTime = TimeZoneUtil.getTimeINYYYYMMddss(ve.getId()
					.getEventTimeStamp());
			String getEsEvent = "SELECT e.vin,e.eventTimeStamp,e.latitude,e.longitude,e.speed,e.engine"
					+ " from vehicleevent e WHERE e.vin = :vin  AND e.eventTimeStamp > ('"
					+ eventTime
					+ "'- INTERVAL 35 MINUTE) ORDER BY e.eventTimeStamp DESC";
			Query queryForEs = em.createNativeQuery(getEsEvent);
			queryForEs.setParameter("vin", ve.getId().getVin());
			LOGGER.info("Before Execute Query" + queryForEs);
			List<Objects[]> objects = queryForEs.getResultList();
			LOGGER.info("After Execute query" + queryForEs);
			if (objects == null) {
				return null;
			}
			List<Vehicleevent> vehicleevents = new ArrayList<Vehicleevent>();
			for (Object[] object : objects) {
				Vehicleevent vehicleevent = new Vehicleevent();
				VehicleeventId vehicleeventId = new VehicleeventId();
				vehicleeventId.setVin(String.valueOf(object[0]));
				vehicleeventId.setEventTimeStamp((Date) (object[1]));
				vehicleevent.setId(vehicleeventId);
				vehicleevent.setLatitude(Double.parseDouble(object[2]
						.toString()));
				vehicleevent.setLongitude(Double.parseDouble(object[3]
						.toString()));
				vehicleevent.setSpeed((Integer) object[4]);
				vehicleevent.setEngine((Boolean) (object[5]));
				vehicleevents.add(vehicleevent);
			}
			LOGGER.info("AlertsEJB::getIdleVehicleevents::Leaving from this method successfully");
			return vehicleevents;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::getIdleVehicleevents::Idle Interval Qry error="
					+ e);
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Vehicleevent getFatigueVehicleevents(String vin) {
		LOGGER.info("AlertsEJB::getFatigueVehicleevents::Entered into this method::vin"
				+ vin);
		try {
			Query query = em
					.createNativeQuery("SELECT vin,eventTimeStamp,latitude,longitude,speed,engine"
							+ " FROM vehicleevent WHERE vin = '"
							+ vin
							+ "' AND DATE(eventTimeStamp) > (CURDATE()-1) AND "
							+ "`engine` = 0 ORDER BY eventTimeStamp DESC limit 1");
			LOGGER.info("Before Execute Query" + query);
			List<Objects[]> objects = query.getResultList();
			LOGGER.info("After Execute Query" + query);
			if ((objects == null) || (objects.isEmpty())) {
				return null;
			}
			Object[] object = (Object[]) objects.get(0);

			Vehicleevent vehicleevent = new Vehicleevent();
			VehicleeventId vehicleeventId = new VehicleeventId();
			vehicleeventId.setVin(String.valueOf(object[0]));
			vehicleeventId.setEventTimeStamp((Date) (object[1]));
			vehicleevent.setId(vehicleeventId);
			vehicleevent.setLatitude((Double) object[2]);
			vehicleevent.setLongitude((Double) object[3]);
			vehicleevent.setSpeed((Integer) object[4]);
			vehicleevent.setEngine((Boolean) (object[5]));
			LOGGER.info("AlertsEJB::getFatigueVehicleevents::Leaving from this method is successfully");
			return vehicleevent;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::getFatigueVehicleevents::Idle Interval Qry error="
					+ e);
			return null;
		}
	}

	@Override
	public Vehicleevent getGeoPrevVe(String vin) {
		LOGGER.info("AlertsEJB::getGeoPrevVe::Entered into this method::vin"
				+ vin);
		try {
			Query query = em
					.createQuery("select e from Vehicleevent e where e.id.vin=:vin order by e.id.eventTimeStamp desc");
			query.setMaxResults(1);
			query.setParameter("vin", vin);
			LOGGER.info("Before Execute Query query" + query);
			List<Vehicleevent> objects = (List<Vehicleevent>) query
					.getResultList();
			if (!objects.isEmpty() && objects.size() != 0) {
				LOGGER.info("After Excecute Query" + query);
				LOGGER.info("AlertsEJB::getGeoPrevVe::Leaving from this method successfully");
				return objects.get(0);
			}
			return null;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::getGeoPrevVe::Exception occured::Geo Qry error="
					+ e);
			return null;
		}
	}

	@Override
	public boolean isGeoTrue(String latCen, String latRad, String latMar) {
		LOGGER.info("AlertsEJB::isGeoTrue::Entered into this method::"
				+ "latcen" + latCen + "latRad" + latRad + "latMar" + latMar);
		try {
			Query query = em
					.createNativeQuery("SELECT IF(((SELECT GLENGTH(GEOMFROMTEXT  ('LineString("
							+ latCen
							+ ","
							+ latRad
							+ ")')) AS distance)   > (SELECT GLENGTH(GEOMFROMTEXT('LineString("
							+ latCen
							+ ","
							+ latMar
							+ ")'))   AS distance)),'true','false') AS output");
			LOGGER.info("Before Execute Query query" + query);
			Object obj = (Object) query.getSingleResult();
			LOGGER.info("After Excecute Query" + query);
			return (String.valueOf(obj)).equalsIgnoreCase("true") ? true
					: false;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::isGeoTrue::Exception error occured::Geo Qry error="
					+ e);
			return false;
		}
	}

	@Override
	public Map<Integer, Boolean> getGeoStatusHashMap(String queryStr) {
		LOGGER.info("AlertsEJB::getGeoStatusHashMap::" + "queryStr" + queryStr);
		try {
			Map<Integer, Boolean> hashMap = new HashMap<Integer, Boolean>();
			Query query = em.createNativeQuery(queryStr);
			LOGGER.info("Before Execute Query query" + query);
			List<Object[]> objs = (List<Object[]>) query.getResultList();
			LOGGER.info("After Excecute Query" + query);
			for (Object[] objects : objs) {
				hashMap.put(Integer.parseInt(String.valueOf(objects[0])),
						Boolean.parseBoolean(String.valueOf(objects[1])));
			}
			return hashMap;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::getGeoStatusHashMap::Exception occured::"
					+ e);
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Map<String, Boolean> strGetGeoStatusHashMap(String queryStr) {
		LOGGER.info("AlertsEJB::getGeoStatusHashMap::" + "queryStr" + queryStr);
		try {
			Map<String, Boolean> hashMap = new HashMap<String, Boolean>();
			Query query = em.createNativeQuery(queryStr);
			LOGGER.info("Before Execute Query query" + query);
			List<Object[]> objs = (List<Object[]>) query.getResultList();
			LOGGER.info("After Excecute Query" + query);
			for (Object[] objects : objs) {
				hashMap.put(String.valueOf(objects[0]),
						Boolean.parseBoolean(String.valueOf(objects[1])));
			}
			return hashMap;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::strGetGeoStatusHashMap::Exception occured::"
					+ e);
			e.printStackTrace();
			return null;
		}
	}

	public boolean insertSMS1(Smssent smssent) {
		try {
			emAdmin.persist(smssent);
			try {
				// need to add --
				SmsconfigId smsconfigId = new SmsconfigId();
				smsconfigId.setCompanyid(smssent.getCompanyid());
				smsconfigId.setBranchId(smssent.getBranchId());
				Smsconfig smsconfig = emAdmin
						.find(Smsconfig.class, smsconfigId);
				long totSmsCnt = smsconfig.getSmsCnt();
				long actSmsCnt = totSmsCnt - 1;
				smsconfig.setSmsCnt(actSmsCnt);
			} catch (Exception e) {
				LOGGER.error("AlertsEJB::insertSMS1::Decrement Error in insert SMS1"
						+ e);
			}

			return true;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::insertSMS1::error in persisting SMS" + e);
			return false;

		}
	}

	public String getTimeZoneRegion(String vin) {
		LOGGER.info("AlertsEJB::getTimeZoneRegion::Entered int othis method::vin"
				+ vin);
		String region = null;
		try {
			Query query = emAdmin
					.createQuery("SELECT cb.region FROM Companybranch cb,Vehicle v where v.companyId =cb.id.companyId "
							+ "and v.branchId = cb.id.branchId and v.vin =:vin");
			query.setParameter("vin", vin);
			LOGGER.info("Before Execute Query query" + query);

			List<Object> list = query.getResultList();
			if (!list.isEmpty() && list.size() != 0) {
				region = list.get(0).toString();
			}
			// region = (String) query.getSingleResult();
			LOGGER.info("After Excecute Query" + query);
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::getTimeZoneRegion::Exception occured" + e);
		}
		return region;
	}

	@Override
	public int getDay(Date eventtimeStamp, String timeZone) {
		LOGGER.info("AlertsEJB::getDay::Entered into this method::"
				+ "eventtimestamp" + eventtimeStamp + "TimeZone" + timeZone);
		Calendar mbCal = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
		mbCal.setTime(eventtimeStamp);
		return mbCal.get(Calendar.DAY_OF_WEEK);
	}

	@Override
	public boolean getTime(List<Vehicleevent> vehicleEvents, String range) {
		LOGGER.info("AlertsEJB::getTime::Enterd into method::"
				+ "vehicleEvents" + vehicleEvents + "range" + range);
		int startTime, stopTime, startHour, stopHour, startMin, stopMin;
		boolean isDayShift, isAlert = false;
		for (Vehicleevent vehicleevent : vehicleEvents) {
			Date eventTimeStamp = vehicleevent.getId().getEventTimeStamp();

			if (range != null) {
				if (range.indexOf(":") != -1) {

					if (!(range.equalsIgnoreCase("1:00_1:00"))) {
						String[] range1 = range.split("_");
						String[] withoutMin1 = range1[0].split(":");
						String[] withoutMin2 = range1[1].split(":");
						startHour = Integer.valueOf(withoutMin1[0]);
						startMin = Integer.valueOf(withoutMin1[1]);
						stopHour = Integer.valueOf(withoutMin2[0]);
						stopMin = Integer.valueOf(withoutMin2[1]);

						if (startHour <= stopHour) {
							startTime = startHour;
							stopTime = stopHour;
							isDayShift = true;
						} else {
							startTime = stopHour;
							stopTime = startHour;
							isDayShift = false;
						}

						Calendar calendar = Calendar.getInstance();
						calendar.setTime(eventTimeStamp);
						int hour = calendar.get(calendar.HOUR_OF_DAY);
						int minute = calendar.get(calendar.MINUTE);

						if ((startTime <= hour) && (hour <= stopTime)) {

							if (startHour == stopTime) {
								if ((minute >= startMin) && (minute <= stopMin)) {
									if (isDayShift) {
										isAlert = true;
									}
								}
								break;
							}

							if (startHour == hour) {
								if (minute >= startMin) {
									if (isDayShift) {
										isAlert = true;
									}
								}
								break;
							}

							if (stopHour == hour) {
								if (minute <= stopMin) {
									if (isDayShift) {
										isAlert = true;
									}
								}
								break;
							}

							if (isDayShift) {
								isAlert = true;
							}
							break;

						} else {
							if (!isDayShift) {
								isAlert = true;
							}
							break;
						}
					}

					else {
						isAlert = true;
						break;
					}

				} else {
					if (!(range.equalsIgnoreCase("1_1"))) {
						String[] range1 = range.split("_");
						startHour = Integer.valueOf(range1[0]);
						stopHour = Integer.valueOf(range1[1]);

						if (startHour <= stopHour) {
							startTime = startHour;
							stopTime = stopHour;
							isDayShift = true;
						} else {
							startTime = stopHour;
							stopTime = startHour;
							isDayShift = false;
						}

						Calendar calendar = Calendar.getInstance();
						calendar.setTime(eventTimeStamp);
						int hour = calendar.get(calendar.HOUR_OF_DAY);
						if ((startTime <= hour) && (hour <= stopTime)) {
							if (isDayShift) {
								isAlert = true;
							}
							break;
						} else {
							if (!isDayShift) {
								isAlert = true;
							}
							break;
						}
					} else {
						isAlert = true;
						break;
					}
				}
			}

		}
		LOGGER.info("AlertsEJB::getTime::Leaving from this method" + isAlert);
		return isAlert;
	}

	@Override
	public List<VehicleHasIo> getVehicleHasIos(String vin, String alertsubtype) {
		List<VehicleHasIo> vehicleHasIos = new ArrayList<VehicleHasIo>();
		try {
			Query query = em
					.createQuery("Select vhio from VehicleHasIo vhio where vhio.id.vin=:vin and vhio.ioname = :alertsubtype");
			query.setParameter("vin", vin);
			query.setParameter("alertsubtype", alertsubtype);
			vehicleHasIos = query.getResultList();
			LOGGER.info("After Excecute Query" + query);
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::getVehicleHasIos::Exception occured::GetVehicleHasIO : "
					+ e);
		}
		return vehicleHasIos;
	}

	@Override
	public SortedMap<Integer, Boolean> getDayAlert(int alertDay) {
		LOGGER.info("AlertsEJB::getDayAlert::Entered into this method"
				+ "alertDay" + alertDay);
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
		LOGGER.info("AlertsEJB::getDayAlert::Leaving from this method successfully");
		return hmap;
	}

	@Override
	public float getanalogReading(String vin, int type, int millivolts) {
		LOGGER.info("AlertsEJB::getanalogReading::Entered into this method"
				+ "vin" + vin + "type" + type + "millivolts" + millivolts);
		float a = 0;
		try {
			Query q = em
					.createNativeQuery("select litres FROM vehiclefuelreading where vin = :vin and type = :type and millivolts = "
							+ "(select max(millivolts) from vehiclefuelreading where millivolts <= :millivolts and vin=:vin and type=:type)");
			q.setParameter("vin", vin);
			q.setParameter("millivolts", millivolts);
			q.setParameter("type", type);
			LOGGER.info("####Before Execute Query" + q);

			if (q.getResultList() != null && !q.getResultList().isEmpty()) {
				a = (Float) q.getSingleResult();
			}

			LOGGER.info("After Excecute Query" + q);
			LOGGER.info("AlertsEJB::getanalogReading::Leaving from this method");

		} catch (Exception e) {
			LOGGER.error("AlertsEJB::getanalogReading::Exception Occured" + e);
		}
		return a;
	}

	@Override
	public Vehicleevent getLastVEData(String vin, String eventtimestamp,
			int interval) {
		LOGGER.info("AlertsEJB::getLastVEData::Entered into this method"
				+ "vin" + vin + "eventtimestamp" + eventtimestamp + "interval"
				+ interval);
		Vehicleevent vehicleevent = null;
		try {
			Query query1 = em.createNativeQuery("Select MIN(t.eventTimeStamp) "
					+ "from vehicleevent t where t.vin='" + vin
					+ "' and t.eventTimeStamp >= (STR_TO_DATE('"
					+ eventtimestamp + "','%Y-%m-%d %h:%i:%s %p') - INTERVAL "
					+ interval + " MINUTE)");
			LOGGER.info("Before Execute Query" + query1);
			final Date newDt = (Date) query1.getSingleResult();
			LOGGER.info("After Excecute Query" + query1);
			if (newDt != null) {
				Query query = em
						.createQuery("Select ve from Vehicleevent ve where ve.id.vin=:vin and ve.id.eventTimeStamp=:dt");
				query.setParameter("vin", vin);
				query.setParameter("dt", newDt);
				LOGGER.info("Before Execute Query" + query);
				vehicleevent = (Vehicleevent) query.getSingleResult();
				LOGGER.info("After Excecute Query" + query);
				LOGGER.info("AlertsEJB::getLastVEData::Leaving from this method::");
			}
			return vehicleevent;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::getLastVEData::Exception error Occured::"
					+ e);
			return null;
		}
	}

	@Override
	public boolean getValidity(List<Vehicleevent> vehicleevents,
			Date validityExp) {
		LOGGER.info("AlertsEJB::getValidity::" + "vehiclevents" + vehicleevents
				+ "validityExp" + validityExp);
		boolean validity = false;
		Calendar veCalendar = Calendar.getInstance();
		Calendar validityCal = Calendar.getInstance();
		validityCal.setTime(validityExp);
		for (Vehicleevent vehicleevent : vehicleevents) {
			veCalendar.setTime(vehicleevent.getId().getEventTimeStamp());
			if (veCalendar.before(validityCal)) {
				validity = true;
				break;
			}
		}
		LOGGER.info("AlertsEJB::getValidity::Leaving from this method");
		return validity;
	}

	@Override
	public String getOdometer(String vin) {
		String odometer = null;
		try {
			Query odometerQuery = em
					.createQuery("Select odometer from VehicleHasOdometer vho where vho.id.vin=:vin AND vho.id.lastUpdDt=(Select MAX(vh.id.lastUpdDt) from VehicleHasOdometer vh where vh.id.vin=:vin)");
			odometerQuery.setParameter("vin", vin);
			List<String> objects = (List<String>) odometerQuery.getResultList();
			if (!objects.isEmpty() && objects.size() != 0) {
				odometer = objects.get(0);
			}
		} catch (PersistenceException e) {

		} catch (Exception e) {
			LOGGER.error("Odometer Value : " + e);
		}
		return odometer;
	}

	@Override
	public List<Alertconfig> getAlertConfigOdometer(String vin) {
		LOGGER.info("AlertsEJB::getAlertConfig::Entered into this method"
				+ "vin" + vin);
		try {
			String getVehicleConfig = "SELECT ac FROM Alertconfig ac WHERE vin = '"
					+ vin
					+ "' AND alertStatus = '1' AND subAlertType = 'ODOMETER'";
			Query vehiConfigQuery = em.createQuery(getVehicleConfig);
			LOGGER.info("Before Query Excecute Query::" + vehiConfigQuery);
			List<Alertconfig> alertConfigs = (List<Alertconfig>) vehiConfigQuery
					.getResultList();
			return alertConfigs;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::getAlertConfig::Error in getting alert config for vin="
					+ vin + " : e");
			return null;
		}
	}

	@Override
	public int getVehicleHasIO(String vin) {
		int ioVal = 0;
		try {
			Query query = em
					.createQuery("Select vhio.id.io FROM VehicleHasIo vhio WHERE vhio.id.vin=:vin AND vhio.ioname=:ioname");
			query.setParameter("vin", vin);
			query.setParameter("ioname", "Fuel Tank Lid");
			ioVal = (Integer) query.getSingleResult();
		} catch (Exception e) {
			LOGGER.error("AlertsEJB:: getVehicleHasIO :: Exception " + e);
		}
		return ioVal;
	}

	@Override
	public Fueltanklid getFueltanklid(String vin) {
		Fueltanklid fueltanklid = new Fueltanklid();
		try {
			Query query = em
					.createNativeQuery("Select * FROM fueltanklid ftl WHERE ftl.vin=:vin AND ftl.lidStatus=:lidStatus");
			query.setParameter("vin", vin);
			query.setParameter(STR_LID_STATUS, "Open");
			Object[] object = (Object[]) query.getSingleResult();
			FueltanklidId fueltanklidId = new FueltanklidId();
			fueltanklidId.setVin((String) object[0]);
			fueltanklidId.setOpenEventTimeStamp((Date) object[1]);
			fueltanklidId.setOpenLitres((Float) object[4]);
			fueltanklidId.setOpenMillivolts((Integer) object[3]);
			fueltanklidId.setLidStatus((String) object[2]);
			fueltanklid.setId(fueltanklidId);
			return fueltanklid;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB:: getFueltanklid:: Exception " + e);
		}
		return null;
	}

	@Override
	public boolean insertFuelTankLid(List<Fueltanklid> fueltanklids) {
		try {
			Query query = em
					.createQuery("DELETE FROM Fueltanklid ftl WHERE ftl.id.vin=:vin AND ftl.id.lidStatus=:lidStatus");
			query.setParameter("vin", fueltanklids.get(0).getId().getVin());
			query.setParameter(STR_LID_STATUS, "Open");
			query.executeUpdate();
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::: Delete Previous in FuelTankLid: " + e);
		}
		try {
			for (Fueltanklid fueltanklid : fueltanklids) {
				em.persist(fueltanklid);
			}
			return true;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::insertFuelTankLid::error in persisting Fueltanklid"
					+ e);
			return false;
		}
	}

	@Override
	public String getAddress(String lat, String lng) {
		String address = null;
		try {
			address = MiscHttpRequest.invokeGoecoder(lat, lng);
		} catch (Exception e) {
			LOGGER.error("AlertsEJB:: getAddress:: error " + e);
		}
		return address;
	}

	@Override
	public Vehicleevent getPreviousVE(String vin, String eventDate) {
		Vehicleevent vehicleevent = null;
		try {
			Query query = null;
			if (eventDate != null) {
				query = em
						.createQuery("SELECT ve FROM Vehicleevent ve WHERE ve.id.vin=:vin AND "
								+ "ve.id.eventTimeStamp=(SELECT MAX(v.id.eventTimeStamp) FROM Vehicleevent v WHERE v.id.vin=:vin and v.id.eventTimeStamp BETWEEN "
								+ "CONCAT(:eventDate,' 00:00:00') AND CONCAT(:eventDate,' 23:59:59'))");
				query.setParameter("eventDate", eventDate);
			} else {
				query = em
						.createQuery("SELECT ve FROM Vehicleevent ve WHERE ve.id.vin=:vin AND "
								+ "ve.id.eventTimeStamp=(SELECT MAX(v.id.eventTimeStamp) FROM Vehicleevent v WHERE v.id.vin=:vin)");
			}
			query.setParameter("vin", vin);
			List<Vehicleevent> objects = (List<Vehicleevent>) query
					.getResultList();
			if (!objects.isEmpty() && objects.size() != 0) {
				vehicleevent = objects.get(0);
			}
			return vehicleevent;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB:: getPreviousVE:: error " + e);
		}
		return null;
	}

	@Override
	public List<Studentalertsubscription> getAlertsSubscribed(String vin) {
		LOGGER.info("AlertsEJB::getAlertConfig::Entered into this method"
				+ "vin" + vin);
		try {
			String alertsSubscribed = "SELECT altSub FROM Studentalertsubscription altSub, Studentdetails st "
					+ "WHERE st.stin = altSub.id.stin GROUP BY altSub.id.stin,altSub.id.alertsubscribed";
			Query alertsSubscribedQuery = em.createQuery(alertsSubscribed);
			alertsSubscribedQuery.setParameter("vin", vin);
			LOGGER.info("Before Query Excecute Query::" + alertsSubscribed);
			List<Studentalertsubscription> alertSubscribed = (List<Studentalertsubscription>) alertsSubscribedQuery
					.getResultList();
			return alertSubscribed;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::getAlertsSubscribed::Error in getting alert config for vin="
					+ vin + " = " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Provider getProviderDetails(String companyId) {
		Provider provider = null;
		try {
			Query query = em
					.createQuery("SELECT pro FROM Provider pro WHERE pro.id = (SELECT c.suffix FROM Company c WHERE c.companyId= :companyId)");
			query.setParameter("companyId", companyId);

			List<Provider> objects = (List<Provider>) query.getResultList();
			if (!objects.isEmpty() && objects.size() != 0) {
				provider = objects.get(0);
			} else {
				LOGGER.error("AlertsEJB :: getProviderDetails: No result for "
						+ companyId + " and query = " + query.toString());
			}
		} catch (Exception e) {
			LOGGER.error("AlertsEJB :: getProviderDetails for " + companyId
					+ " is " + e.getMessage());
		}
		return provider;
	}

	@Override
	public boolean insertVehicleAlerts(Alertevents alertEvent) {
		LOGGER.info("AlertsEJB::insertAlertevents::" + "alertEvent"
				+ alertEvent);
		try {
			Vehiclealerts vehicleAlert = new Vehiclealerts();
			vehicleAlert.setVin(alertEvent.getVin());
			vehicleAlert.setEventTimeStamp(alertEvent.getEventTimeStamp());
			vehicleAlert
					.setAlerttype(alertEvent.getAlerttypes().getAlerttype());
			vehicleAlert.setDescription(alertEvent.getDescription());
			vehicleAlert.setLatlng(alertEvent.getLocation());
			vehicleAlert.setServerTimeStamp(alertEvent.getEventTimeStamp());
			vehicleAlert.setSmsmobile(String.valueOf(alertEvent.getId()));
			em.persist(vehicleAlert);
			return true;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::insertVehicleAlert::error in persisting alertEvent"
					+ e);
			return false;
		}
	}

	@Override
	public void insertTagintoDB(String tagid) {
		try {
			String querytoInserttag = "INSERT INTO `studenttrackingdb`.`tagdataentry` 	(`id`,	`tagid`	)	VALUES	(0,	'"
					+ tagid + "')";
			Query query = em.createNativeQuery(querytoInserttag);
			query.executeUpdate();
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::insertTagintoDB::error in persisting "
					+ e.getMessage());
			e.printStackTrace();
		}

	}

	public List<Calendarevent> getMaintanceAlert() {
		List<Calendarevent> resultUser = new ArrayList<Calendarevent>();
		try {
			Query queryUser = em
					.createQuery("SELECT ce FROM Calendarevent ce WHERE ce.fromDate = CURDATE()");
			// + "   AND CURDATE() = ce.toDate");
			resultUser = (List<Calendarevent>) queryUser.getResultList();

		} catch (Exception e) {
			LOGGER.error("EMSAdminPortalImpl::getMaintanceAlert::Exception Occured"
					+ e);
			return null;
		}

		return resultUser;
	}

	@Override
	public List<Provider> getProvidersDetails(String companyId) {

		List<Provider> providers = new ArrayList<Provider>();
		try {
			Query query = emAdmin
					.createNativeQuery("SELECT `values` FROM `applicationsettings` WHERE `key`='"
							+ companyId + "'");
			List<String> objects = (List<String>) query.getResultList();
			if (!objects.isEmpty() && objects.size() != 0) {
				String provider1 = objects.get(0);
				String[] providersId = provider1.split(",");
				for (String providerid : providersId) {

					query = em
							.createQuery("SELECT pro FROM Provider pro WHERE pro.id=:providerid");
					query.setParameter("providerid",
							Integer.valueOf(providerid));

					List<Provider> providers2 = (List<Provider>) query
							.getResultList();
					if (!providers2.isEmpty() && providers2.size() != 0) {
						providers.add(providers2.get(0));
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("AlertsEJB :: getProviderDetails " + e.getMessage());
		}
		return providers;
	}

	public List<Vehicleevent> getPrevValues(String vin) {
		LOGGER.info("AlertsEJB::getGeoPrevVe::Entered into this method::vin"
				+ vin);
		try {
			Query query = em
					.createQuery("select e from Vehicleevent e where e.id.vin=:vin order by e.id.eventTimeStamp desc");
			String towedCount = getPreferencesData("Towed_Count", "eitworks");
			if (towedCount != null)
				query.setMaxResults(Integer.parseInt(towedCount));
			else
				query.setMaxResults(6);
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

	@Override
	public List<Pushnotificationdevices> getAllActiveDeviceId(String companyId,
			String userId) {
		LOGGER.info("AlertsEJB::getAllActiveDeviceId::Entered into this method::companyId"
				+ companyId);
		try {
			Query query = emAdmin
					.createQuery("SELECT pro FROM Pushnotificationdevices pro WHERE pro.company.companyId = :companyId and pro.userId= :userId and pro.status='1'");
			query.setParameter("companyId", companyId);
			query.setParameter("userId", userId);
			List<Pushnotificationdevices> pushnotificationdevices = (List<Pushnotificationdevices>) query
					.getResultList();
			LOGGER.info("After Excecute Query" + query);
			LOGGER.info("AlertsEJB::getAllActiveDeviceId::Leaving from this method successfully");
			return pushnotificationdevices;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::getAllActiveDeviceId::Exception occured::Geo Qry error="
					+ e);
			return null;
		}
	}

	@Override
	public boolean getFreeformGeoStatus(Studentevent studentevent, int id) {
		String latVe = String.valueOf(studentevent.getLatitude());
		String lngVe = String.valueOf(studentevent.getLongitude());
		LOGGER.info("AlertsEJB::getFreeformGeoStatus::Entered into this method::"
				+ "vehicleEvent" + studentevent + "id" + id);
		try {
			Query q = em
					.createQuery("select tr from Freeformgeo tr where tr.id.id = :id order by tr.pos");
			q.setParameter("id", id);
			LOGGER.info("Before Execute Query::q" + q);
			List<Freeformgeo> trs = q.getResultList();
			LOGGER.info("After Execute Query::q" + q);
			StringBuilder sbuf = new StringBuilder();
			boolean isFirst = true;
			String firstVal = "";
			for (Freeformgeo tr : trs) {
				String latlngCen = tr.getId().getLatlng().replace(",", " ");
				if (isFirst) {
					firstVal = latlngCen;
					isFirst = false;
				}
				sbuf.append(latlngCen + ",");
			}
			sbuf.append(firstVal);

			Query q1 = em
					.createNativeQuery("SELECT fleettrackingdb.myWithin(GEOMFROMTEXT('Point("
							+ latVe
							+ " "
							+ lngVe
							+ ")'),GEOMFROMTEXT('POLYGON(("
							+ sbuf.toString()
							+ "))'));");
			LOGGER.info("Before Execute Query::q1" + q1);
			Object object = (Object) q1.getSingleResult();
			LOGGER.info("After Execute Queryq1" + q1);
			String str = String.valueOf(object);
			LOGGER.info("AlertsEJB::getFreeformGeoStatus::Leaving from this method successfully");
			return str.trim().equalsIgnoreCase("0") ? false : true;

		} catch (Exception e) {
			LOGGER.error("AlertsEJB::getFreeformGeoStatus::Geo query Error="
					+ e);
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public float getmaxanalogReading(String vin, int type, boolean engine) {
		LOGGER.info("AlertsEJB::getanalogReading::Entered into this method"
				+ "vin" + vin + "type" + type + "millivolts" + engine);

		float a = 0;
		Date date = null;
		try {
			Query q0 = em
					.createNativeQuery("select Max(eventTimeStamp) from vehicleevent  where  vin= :vin ");
			q0.setParameter("vin", vin);
			LOGGER.info("Before Execute Query::q0" + q0);

			if (q0.getResultList() != null && !q0.getResultList().isEmpty()) {
				date = (Date) q0.getSingleResult();
				LOGGER.info("dateeeeeeeeeeeeeeeeeeeeee" + date);
			}

			Query q = em
					.createNativeQuery("SELECT CASE vio.io WHEN 9 THEN ve.ai1 WHEN 10 THEN ve.ai2 WHEN 11 THEN ai3 WHEN 19 THEN ai4 ELSE NULL"
							+ " END AS 'MV',(SELECT litres FROM fleettrackingdb.vehiclefuelreading vr WHERE vr.vin = ve.vin AND millivolts =(SELECT MAX( millivolts ) FROM"
							+ " fleettrackingdb.vehiclefuelreading WHERE TYPE = vio.io AND vin = ve.vin AND millivolts <= MV)) AS litres,TIME(eventtimestamp) AS eventTime"
							+ " FROM fleettrackingdb.vehicleevent ve"
							+ " INNER JOIN fleettrackingdb.vehicle_has_io vio ON ve.vin = vio.vin AND (ve.battery LIKE '13%' OR ve.battery LIKE '26%') "
							+ " AND ve.vin =:vin AND (ve.ENGINE =:engine OR ve.ENGINE =:engine) AND vio.ioname =:type AND ve.speed = '0' AND ve.speed = '1' AND DATE(eventtimestamp) =:date "
							+ " ORDER BY ve.eventtimestamp DESC LIMIT 1");

			// Ve.speed = '0'
			q.setParameter("vin", vin);
			q.setParameter("engine", engine);
			q.setParameter("type", type);
			q.setParameter("date", sdfDate.format(date));
			LOGGER.error("####Before Execute Query" + q);

			if (q.getResultList() != null && !q.getResultList().isEmpty()) {
				a = (Float) q.getSingleResult();
			}

			LOGGER.info("After Excecute Query" + q);
			LOGGER.info("AlertsEJB::getanalogReading::Leaving from this method");

		} catch (Exception e) {
			LOGGER.error("AlertsEJB::getanalogReading::Exception Occured" + e);
		}
		return a;
	}

	@Override
	public List<Vehicleevent> getStopVehicleevents(Vehicleevent ve) {
		LOGGER.info("AlertsEJB::getStopVehicleevents::Entered into this method::ve"
				+ ve);
		try {
			String eventTime = TimeZoneUtil.getTimeINYYYYMMddss(ve.getId()
					.getEventTimeStamp());
			String getEsEvent = "SELECT e.vin,e.eventTimeStamp,e.latitude,e.longitude,e.speed,e.engine"
					+ " from vehicleevent e WHERE e.vin = :vin  AND e.eventTimeStamp > ('"
					+ eventTime
					+ "'- INTERVAL 35 MINUTE) ORDER BY e.eventTimeStamp DESC";
			Query queryForEs = em.createNativeQuery(getEsEvent);
			queryForEs.setParameter("vin", ve.getId().getVin());
			LOGGER.info("Before Execute Query" + queryForEs);
			List<Objects[]> objects = queryForEs.getResultList();
			LOGGER.info("After Execute query" + queryForEs);
			if (objects == null) {
				return null;
			}
			List<Vehicleevent> vehicleevents = new ArrayList<Vehicleevent>();
			for (Object[] object : objects) {
				Vehicleevent vehicleevent = new Vehicleevent();
				VehicleeventId vehicleeventId = new VehicleeventId();
				vehicleeventId.setVin(String.valueOf(object[0]));
				vehicleeventId.setEventTimeStamp((Date) (object[1]));
				vehicleevent.setId(vehicleeventId);
				vehicleevent.setLatitude((Double) object[2]);
				vehicleevent.setLongitude((Double) object[3]);
				vehicleevent.setSpeed((Integer) object[4]);
				vehicleevent.setEngine((Boolean) (object[5]));
				vehicleevents.add(vehicleevent);
			}
			LOGGER.info("AlertsEJB::getStopVehicleevents::Leaving from this method successfully");
			return vehicleevents;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::getStopVehicleevents::Stop Interval Qry error="
					+ e);
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getServerKey(String appName, String deviceOs) {
		LOGGER.info("AlertsEJB::getServerKey::Entered into this method"
				+ "appName" + appName + "deviceOs" + deviceOs);
		String serverKey = null;
		try {
			ServerkeyId serverkeyId = new ServerkeyId();
			serverkeyId.setAppName(appName);
			serverkeyId.setOs(deviceOs);
			Serverkey serverkey = emAdmin.find(Serverkey.class, serverkeyId);
			if (serverkey != null) {
				serverKey = serverkey.getServerkey();
			}

		} catch (Exception e) {
			LOGGER.error("AlertsEJB:: getServerKey:: error " + e);
			e.printStackTrace();
		}
		return serverKey;
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

	@Override
	public CompanyData getCompanySettings(String companyId) {
		CompanyData alertDetails = null;
		try {
			if (antiTheftMap.get(companyId) == null) {
				alertDetails = new CompanyData();
				alertDetails.setAantiTheftCount(Integer
						.parseInt(getPreferencesData("AntiTheftCount",
								companyId)));
				alertDetails.setAntiTheftDuration(Integer
						.parseInt(getPreferencesData("AntiTheftDuration",
								companyId)));
				alertDetails.setAntiTheftMeter(Integer
						.parseInt(getPreferencesData("AntiTheftMeter",
								companyId)));
				alertDetails.setAntiTheftTotalCount(Integer
						.parseInt(getPreferencesData("AntiTheftTotalCount",
								companyId)));
				alertDetails
						.setMovementCount(Integer.parseInt(getPreferencesData(
								"MovementCount", companyId)));
				alertDetails.setMovementDuration(Integer
						.parseInt(getPreferencesData("MovementDuration",
								companyId)));
				alertDetails
						.setMovementMeter(Integer.parseInt(getPreferencesData(
								"MovementMeter", companyId)));
				antiTheftMap.put(companyId, alertDetails);
				return alertDetails;
			} else {
				return antiTheftMap.get(companyId);
			}

		} catch (Exception e) {
			LOGGER.error("AlertsEJB :: getCompanysettings for " + companyId
					+ " is " + e.getMessage());
			return null;
		}
		// return alertDetails;
	}

	public String getPreferencesData(String keyValue, String corpId) {
		String result = null;
		try {
			/*
			 * LOGGER.error(">getPreferencesData to find " + keyValue +
			 * " and Company= " + corpId);
			 */
			String sqlforCmpSetting = "SELECT app.values AS appDefault, comp.values AS companyPref FROM ltmscompanyadmindb.applicationsettings app LEFT JOIN ltmscompanyadmindb.companysettings comp ON app.key=comp.appsettings_key AND comp.company_companyId = :corpId WHERE app.key = :key ";
			LOGGER.info("Brfore exec qry 1");
			Query queryCmpSetting = em.createNativeQuery(sqlforCmpSetting);
			queryCmpSetting.setParameter(CORP_ID, corpId);
			queryCmpSetting.setParameter("key", keyValue);
			Object[] obj = (Object[]) queryCmpSetting.getSingleResult();

			if (obj[1] != null) {
				result = (String) obj[1];
			} else {
				result = (String) obj[0];
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	@Override
	public Maintenancedue getMaintanceDue(MaintenancedueId maintenancedueId) {
		// TODO Auto-generated method stub
		try {
			return em.find(Maintenancedue.class, maintenancedueId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public void updateMaintenanceDue(Maintenancedue maintenance, String type) {
		// TODO Auto-generated method stub
		try {
			if (type.equalsIgnoreCase("insert"))
				em.persist(maintenance);
			else
				em.merge(maintenance);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Hourmeter getHourMeter(String vin) {
		// TODO Auto-generated method stub
		try {
			Hourmeter hourmeter = em.find(Hourmeter.class, vin);
			return hourmeter;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<VehicleHasOdometer> getOverAllOdometer(String vin, String dt) {
		try {
			Query odometerQuery = em
					.createQuery("SELECT vho FROM VehicleHasOdometer vho WHERE vho.id.vin=:vin AND DATE(vho.id.lastUpdDt)=:dt ");
			odometerQuery.setParameter("vin", vin);
			odometerQuery.setParameter("dt", sdfDate.parse(dt));
			List<VehicleHasOdometer> vehicehasodomter = (List<VehicleHasOdometer>) odometerQuery
					.getResultList();
			return vehicehasodomter;
		} catch (Exception e) {
			LOGGER.error("OverAllOdometer Value : " + e);
			return null;
		}

	}

	@Override
	public List<Alertconfig> getOverAllAlertConfig(String vin, String alertType) {
		LOGGER.info("AlertsEJB::getAlertConfig::Entered into this method"
				+ "vin" + vin);
		try {
			String getVehicleConfig = "SELECT ac FROM Alertconfig ac WHERE vin = '"
					+ vin
					+ "' AND alertType = '"
					+ alertType
					+ "'AND alertStatus = '1'";
			Query vehiConfigQuery = em.createQuery(getVehicleConfig);
			LOGGER.info("Before Query Excecute Query::" + vehiConfigQuery);
			List<Alertconfig> alertConfigs = (List<Alertconfig>) vehiConfigQuery
					.getResultList();
			return alertConfigs;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::getAlertConfig::Error in getting alert config for vin="
					+ vin + " : e");
			return null;
		}
	}

	@Override
	public Heartbeatevent getPrevHeartbeatEvent(String vin, Date timeStamp) {
		LOGGER.debug("AlertsEJB::getPrevHeartbeatEvent::Entered into this method::vin"
				+ vin);
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
				LOGGER.info("AlertsEJB::getPrevHeartbeatEvent::Leaving from this method successfully");
				return heartbeatevent;
			}
			return null;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::getPrevHeartbeatEvent::Exception occured::Geo Qry error="
					+ e);
			return null;
		}
	}

	@Override
	public boolean getAlertTime(Heartbeatevent heartbeatevent, String range) {
		LOGGER.info("AlertsEJB::getAlertTime::Enterd into method::" + "range"
				+ range);
		int startTime, stopTime, startHour, stopHour, startMin, stopMin;
		boolean isDayShift, isAlert = false;
		Date eventTimeStamp = heartbeatevent.getId().getTimeStamp();

		if (range != null) {
			if (range.indexOf(":") != -1) {

				if (!(range.equalsIgnoreCase("1:00_1:00"))) {
					String[] range1 = range.split("_");
					String[] withoutMin1 = range1[0].split(":");
					String[] withoutMin2 = range1[1].split(":");
					startHour = Integer.valueOf(withoutMin1[0]);
					startMin = Integer.valueOf(withoutMin1[1]);
					stopHour = Integer.valueOf(withoutMin2[0]);
					stopMin = Integer.valueOf(withoutMin2[1]);

					if (startHour <= stopHour) {
						startTime = startHour;
						stopTime = stopHour;
						isDayShift = true;
					} else {
						startTime = stopHour;
						stopTime = startHour;
						isDayShift = false;
					}

					Calendar calendar = Calendar.getInstance();
					calendar.setTime(eventTimeStamp);
					int hour = calendar.get(calendar.HOUR_OF_DAY);
					int minute = calendar.get(calendar.MINUTE);

					if ((startTime <= hour) && (hour <= stopTime)) {

						if (startHour == stopTime) {
							if ((minute >= startMin) && (minute <= stopMin)) {
								if (isDayShift) {
									isAlert = true;
								}
							}
						}

						if (startHour == hour) {
							if (minute >= startMin) {
								if (isDayShift) {
									isAlert = true;
								}
							}
						}

						if (stopHour == hour) {
							if (minute <= stopMin) {
								if (isDayShift) {
									isAlert = true;
								}
							}
						}

						if (isDayShift) {
							isAlert = true;
						}

					} else {
						if (!isDayShift) {
							isAlert = true;
						}
					}
				}

				else {
					isAlert = true;
				}

			} else {
				if (!(range.equalsIgnoreCase("1_1"))) {
					String[] range1 = range.split("_");
					startHour = Integer.valueOf(range1[0]);
					stopHour = Integer.valueOf(range1[1]);

					if (startHour <= stopHour) {
						startTime = startHour;
						stopTime = stopHour;
						isDayShift = true;
					} else {
						startTime = stopHour;
						stopTime = startHour;
						isDayShift = false;
					}

					Calendar calendar = Calendar.getInstance();
					calendar.setTime(eventTimeStamp);
					int hour = calendar.get(calendar.HOUR_OF_DAY);
					if ((startTime <= hour) && (hour <= stopTime)) {
						if (isDayShift) {
							isAlert = true;
						}
					} else {
						if (!isDayShift) {
							isAlert = true;
						}
					}
				} else {
					isAlert = true;
				}
			}
		}

		LOGGER.info("AlertsEJB::getAlertTime::Leaving from this method"
				+ isAlert);
		return isAlert;
	}

	@Override
	public boolean getAlertValidity(Heartbeatevent heartbeatevent,
			Date validityExp) {
		LOGGER.info("AlertsEJB::getAlertValidity::" + "validityExp"
				+ validityExp);
		boolean validity = false;
		Calendar hbCalendar = Calendar.getInstance();
		Calendar validityCal = Calendar.getInstance();
		validityCal.setTime(validityExp);
		hbCalendar.setTime(heartbeatevent.getId().getTimeStamp());
		if (hbCalendar.before(validityCal)) {
			validity = true;
		}
		LOGGER.info("AlertsEJB::getAlertValidity::Leaving from this method");
		return validity;
	}

	@Override
	public int getStoppedTime(String vin, String eventtimestamp) {
		int timeDiffer = 0;
		try {
			Query query1 = em
					.createNativeQuery("SELECT time_to_sec(TIMEDIFF('"
							+ eventtimestamp
							+ "',eventTimeStamp))  as timeDiff FROM  vehicleevent    WHERE vin='"
							+ vin
							+ "' and  eventTimeStamp <'"
							+ eventtimestamp
							+ "' and speed >=1 order by eventTimeStamp desc limit 1");

			List<BigInteger> objects = (List<BigInteger>) query1
					.getResultList();
			if (!objects.isEmpty() && objects.size() != 0) {
				timeDiffer = objects.get(0).intValue();
			}
		} catch (Exception e) {
			LOGGER.error("AlertsEJB:: Get Stop event Time::Exception error Occured::"
					+ e);
			e.printStackTrace();
		}
		return timeDiffer;
	}

	@Override
	public String getOperatorName(String vin) {
		// TODO Auto-generated method stub
		try {
			Query opNameQry = em
					.createNativeQuery("SELECT op.name FROM operator op WHERE operatorid =(SELECT operator_operatorid FROM vehicle_has_operator WHERE vehicle_vin='"
							+ vin + "' AND effTo IS NULL)");
			List<Object> objs = (List<Object>) opNameQry.getResultList();
			for (Object object : objs) {
				if (object != null) {
					String opName = (String) object;
					return opName;
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in getOperatorName:: " + e);
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean isEmailValidOrNot(String mail) {
		boolean status = false;
		try {
			Emailvalidation validation = em.find(Emailvalidation.class, mail);
			if (validation != null && validation.getIsValid()) {
				status = true;
			}
		} catch (Exception e) {
			LOGGER.error("Error in isEmailValidOrNot:: " + e);
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public void insertTicket(Vehicleevent ve, String vin, String plateNo,
			VehicleComposite vehicleComposite) {
		try {
			LOGGER.info("Enter into insert Ticket");
			Operator op = getOperatorForVehicle(vin);
			TicketInfo ti = new TicketInfo();
			ti.setVin(ve.getId().getVin());
			ti.setLocation(ve.getLatitude() + "," + ve.getLongitude());
			ti.setCreatedDate(ve.getId().getEventTimeStamp());
			ti.setLastUpdatedDate(ve.getId().getEventTimeStamp());
			ti.setLastUpdatedBy("Device");
			ti.setDescription(null);
			ti.setPriority(2);
			ti.setStatus(1);
			ti.setSubject(null);
			ti.setPlateno(plateNo);
			if (op != null) {
				ti.setContactno(op.getEmergencyContactNo());
				ti.setOperatorId(String.valueOf(op.getOperatorId()));
				ti.setOperatorName(op.getName());
			}
			em.persist(ti);
			String ticketinfo = new JSONSerializer().exclude("*.class")
					.deepSerialize(ti);
			for (Map.Entry<Session, String> e : TicketMangementMap.entrySet()) {
				JSONObject obj = new JSONObject(e.getValue());
				if (vehicleComposite.getVehicle().getCompanyId()
						.equalsIgnoreCase(obj.getString("companyId"))) {
					Session session = e.getKey();
					session.getBasicRemote().sendText(ticketinfo);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error at insert Ticket " + e);
			e.printStackTrace();
		}
	}

	@Override
	public Operator getOperatorForVehicle(String vin) {
		Operator op = null;
		try {
			LOGGER.info("Enter into Get OpertorForVehicle");
			Query vhoQ = em
					.createNativeQuery("SELECT operator_operatorId FROM fleettrackingdb.vehicle_has_operator where vehicle_vin=:vin and effTo is NULL");
			vhoQ.setParameter("vin", vin);
			Long operatorId = Long.valueOf(vhoQ.getSingleResult().toString());
			op = em.find(Operator.class, operatorId);
		} catch (Exception e) {
			LOGGER.error("Error at get operator for vehicle " + e);
			e.printStackTrace();
		}
		return op;
	}

	@Override
	public void registerTicketManagementSession(Session session, String data) {
		LOGGER.info("Enter into  Register Ticket Management Session");
		try {
			TicketMangementMap.put(session, data);
		} catch (Exception e) {
			LOGGER.error("Error into  Register TicketManagement Session " + e);
			e.printStackTrace();
		}
	}

	@Override
	public void closeTicketManagementSession(Session session, CloseReason reason) {
		LOGGER.info("Enter into Close Ticket Management Session");
		try {
			TicketMangementMap.remove(session);
		} catch (Exception e) {
			LOGGER.error("Error into close TicketManagement Session " + e);
			e.printStackTrace();
		}
	}

	@Override
	public String getApplicationSetting(String key) {
		// TODO Auto-generated method stub
		String appSetVal = "";
		try {
			Query query = emAdmin
					.createNativeQuery("SELECT aps.values FROM applicationsettings aps WHERE aps.key ='"
							+ key + "'");
			Object compSetVal = (Object) query.getSingleResult();
			if (compSetVal != null)
				appSetVal = compSetVal.toString();
			else
				appSetVal = "";
			return appSetVal;
		} catch (Exception e) {
			LOGGER.error("Error into close getApplicationSetting " + e);
			e.printStackTrace();
			return null;
		}

	}

	public long getBetweenSec(String speedTimeStamp, String lastUpdatedTime) {
		Date d1 = TimeZoneUtil.getDateYYYYMMDDHHMMSS(speedTimeStamp);
		Date d2 = TimeZoneUtil.getDateYYYYMMDDHHMMSS(lastUpdatedTime);
		long diff = d1.getTime() - d2.getTime();
		long diffSeconds = diff / 1000;
		return diffSeconds;
	}

	private double distanceBetweenTwoPoints(double lat1, double lng1,
			double lat2, double lng2) {
		double earthRadius = 6371000; // meters
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = (double) (earthRadius * c);

		return dist;
	}

	@Override
	public int getOverAllAlertCount(String companyId) {

		Date today = TimeZoneUtil.getDateInTimeZoneforSKT(new Date(),
				getRegionByCompany(companyId));
		String todayStr = TimeZoneUtil.getStrDZ(today);
		Query alertQuery = null;
		try {
			alertQuery = em
					.createNativeQuery("SELECT COUNT(va.vin) FROM vehiclealerts va LEFT JOIN vehicle v ON v.vin = va.vin WHERE v.companyId ='"
							+ companyId
							+ "' AND va.eventTimeStamp BETWEEN '"
							+ todayStr
							+ " 00:00:00' AND '"
							+ todayStr
							+ " 23:59:59' ");
			// String alertCount = alertQuery.getSingleResult().toString();
		} catch (Exception e) {
			LOGGER.error("AlertEJB::getOverAllAlertCount::Exception occured"
					+ e);
		}
		return Integer.parseInt(alertQuery.getSingleResult().toString()) + 1;
	}

	@Override
	public String getRegionByCompany(String companyId) {
		LOGGER.info("FleetTrackingDeviceListenerBO::getTimeZoneRegion" + "Vin"
				+ companyId);
		String region = null;
		try {
			Query query = emAdmin
					.createQuery("SELECT cb.region FROM Companybranch cb  where cb.id.companyId=:companyId");
			query.setParameter("companyId", companyId);
			LOGGER.info("Before Execute Query" + "query" + query);
			List<String> objects = (List<String>) query.getResultList();
			if (!objects.isEmpty() && objects.size() != 0)
				region = objects.get(0);
			LOGGER.info("After Execute Query" + "query" + query);
		} catch (Exception e) {
			LOGGER.error("FleetTrackingDeviceListenerBO::getTimeZoneRegion::Exception occured"
					+ e);
		}
		return region;
	}
}