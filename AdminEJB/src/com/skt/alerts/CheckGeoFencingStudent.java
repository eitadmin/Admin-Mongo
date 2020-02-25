//package com.skt.alerts;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.SortedMap;
//
//import com.eiw.alerts.AlertsEJBRemote;
//import com.eiw.alerts.AlertsManager;
//import com.eiw.server.TimeZoneUtil;
//import com.eiw.server.fleettrackingpu.Alertconfig;
//import com.eiw.server.fleettrackingpu.AlertconfigId;
//import com.eiw.server.fleettrackingpu.Geozones;
//import com.eiw.server.fleettrackingpu.VehicleHasGeozones;
//import com.eiw.server.fleettrackingpu.Vehiclealerts;
//import com.eiw.server.fleettrackingpu.Vehicleevent;
//import com.eiw.server.studenttrackingpu.Routestops;
//
//public class CheckGeoFencingStudent {
//	/*
//	 * Geofence Entered/Leved Alerts
//	 */
//
//	AlertsEJBRemote alertsEJBRemote = null;
//	AlertsManager alertsManager;
//	List<Vehicleevent> vehicleevents;
//	List<VehicleHasGeozones> vehicleHasGeozones = null;
//	Map<Integer, Boolean> prevHMap = null, currHMap = null;
//	Map<Integer, Geozones> geoMap = null;
//	String plateNo, vin;
//	List<Vehiclealerts> vehiclealerts = null;
//	boolean validity = false;
//
//	public CheckGeoFencingStudent(AlertsManager alertsManager1) {
//		alertsManager = alertsManager1;
//		alertsEJBRemote = alertsManager.alertsEJBRemote;
//	}
//
//	public void manageAlert(List<Vehicleevent> vehicleevents1, String vin1,
//			String plateNo1) {
//
//		plateNo = plateNo1;
//		vin = vin1;
//		vehicleevents = vehicleevents1;
//
//		// Getting Geozones Associated for this Vehicle
//		vehicleHasGeozones = alertsEJBRemote.getGeoConfig(vin, "GEOSOFT");
//		if (vehicleHasGeozones.isEmpty()) {
//			return;
//		}
//
//		// Getting GeoId, Geozone
//		geoMap = getGeozoneDetail(vehicleHasGeozones);
//
//		List<Date> dates = new ArrayList<Date>();
//		for (Vehicleevent evt : vehicleevents) {
//			dates.add(evt.getId().getEventTimeStamp());
//		}
//		Collections.sort(dates);
//
//		Vehicleevent prevVE = alertsEJBRemote.getGeoPrevVe(vin);
//		if (prevVE == null) {
//			return;
//		}
//		prevHMap = getGeoStatusHashMap(prevVE);
//		if (prevHMap.isEmpty())
//			return;
//
//		for (int i = 0; i < dates.size(); i++) {
//			Vehicleevent currVE = getVE(dates.get(i));
//
//			// omitting older records coming in Transmission
//			Date currDate = currVE.getId().getEventTimeStamp();
//			Date prevDate = prevVE.getId().getEventTimeStamp();
//			if (currDate.getTime() < prevDate.getTime()) {
//				continue;
//			}
//
//			currHMap = getGeoStatusHashMap(currVE);
//			prepareAlert(currHMap, prevHMap, currVE);
//			prevVE = currVE;
//			prevHMap = currHMap;
//		}
//
//	}
//
//	private Vehicleevent getVE(Date date) {
//		for (Vehicleevent ve : vehicleevents) {
//			long a = ve.getId().getEventTimeStamp().getTime();
//			long b = date.getTime();
//			if (a == b) {
//				return ve;
//			}
//		}
//		return null;
//	}
//
//	private Map<Integer, Boolean> getGeoStatusHashMap(Vehicleevent evt) {
//
//		getRoutes(evt);
//
//		StringBuilder qryStr = new StringBuilder();
//		// for (int i = 0; i < vehicleHasGeozones.size(); i++) {
//		// Geozones geozones = vehicleHasGeozones.get(i).getGeozones();
//		// String[] latLng = geozones.getLatlng().split("\\|");
//		// String latCen = latLng[0].replace(",", " ");
//		// String latRad = latLng[1].replace(",", " ");
//		// String shape = geozones.getShape();
//		// Vehicleevent ve = evt;
//		// String latMar = (ve.getLatitude() + "," + ve.getLongitude())
//		// .replace(",", " ");
//		// if (shape.equalsIgnoreCase("Circle")) {
//		// qryStr.append("SELECT "
//		// + geozones.getId()
//		// + " ,IF(((SELECT GLENGTH(GEOMFROMTEXT  ('LineString("
//		// + latCen
//		// + ","
//		// + latRad
//		// + ")')) AS distance)   > (SELECT GLENGTH(GEOMFROMTEXT('LineString("
//		// + latCen + "," + latMar
//		// + ")'))   AS distance)),'true','false') AS output");
//		// } else {
//		// String[] ab = latLng[0].split(",");
//		// String[] cd = latLng[1].split(",");
//		// qryStr.append("SELECT " + geozones.getId()
//		// + " ,CASE WHEN(MBRCONTAINS(GEOMFROMTEXT('POLYGON(("
//		// + ab[0] + " " + ab[1] + "," + cd[0] + " " + ab[1] + ","
//		// + cd[0] + " " + cd[1] + "," + ab[0] + " " + cd[1] + ","
//		// + ab[0] + " " + ab[1] + "))'),GEOMFROMTEXT('Point("
//		// + latMar + ")')))=1 THEN 'true' ELSE 'false' END");
//		// }
//		// if (i != (vehicleHasGeozones.size() - 1)) {
//		// qryStr.append(" union all ");
//		// }
//		// }
//		return alertsEJBRemote.getGeoStatusHashMap(qryStr.toString());
//	}
//
//	private void getRoutes(Vehicleevent evt) {
//
//		String query = "SELECT rs FROM studenttrackingdb.routestops rs,fleettrackingdb.vehicle_has_route vhr where vhr.routeid=rs.routeid and vhr.vin='"
//				+ evt.getId().getVin() + "'";
//		List<Routestops> routestops =new ArrayList<>();
////		List<Routestops> routestops = alertsEJBRemote.getSKTRouteStops(query);
//
//		for (int i = 0; i < routestops.size(); i++) {
//
//			Double lat = Double.parseDouble(routestops.get(i).getLatlng()
//					.split(",")[0]);
//			Double longi = Double.parseDouble(routestops.get(i).getLatlng()
//					.split(",")[1]);
//
//			if (distFrom(evt.getLatitude().floatValue(), evt.getLongitude()
//					.floatValue(), lat.floatValue(), longi.floatValue()) < 10.00) {
//
//			}
//
//		}
//
//	}
//
//	public static float distFrom(float lat1, float lng1, float lat2, float lng2) {
//		double earthRadius = 6371000; // meters
//		double dLat = Math.toRadians(lat2 - lat1);
//		double dLng = Math.toRadians(lng2 - lng1);
//		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
//				+ Math.cos(Math.toRadians(lat1))
//				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
//				* Math.sin(dLng / 2);
//		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//		float dist = (float) (earthRadius * c);
//
//		return dist;
//	}
//
//	private void prepareAlert(Map<Integer, Boolean> currHMap,
//			Map<Integer, Boolean> prevHMap, Vehicleevent currVE) {
//		int today;
//		boolean curTime;
//		String timeZone = alertsEJBRemote.getTimeZoneRegion(vin);
//		for (Map.Entry<Integer, Boolean> preMap : prevHMap.entrySet()) {
//			List<Vehicleevent> vehicleeventList = new ArrayList<Vehicleevent>();
//			if (preMap.getValue() != currHMap.get(preMap.getKey())) {
//				Geozones geozones = geoMap.get(preMap.getKey());
//				VehicleHasGeozones vhg = getVhgFromGeozoneId(preMap.getKey());
//				boolean onLeave = vhg.getOnLeave();
//				boolean onEnter = vhg.getOnEnter();
//				String mobile = vhg.getMobileNumber();
//				// Date and Time Check
//				SortedMap<Integer, Boolean> getDay = alertsEJBRemote
//						.getDayAlert(vhg.getAlertDay());
//				today = alertsEJBRemote.getDay(currVE.getId()
//						.getEventTimeStamp(), timeZone);
//				vehicleeventList.add(currVE);
//				curTime = alertsEJBRemote.getTime(vehicleeventList,
//						vhg.getAlertTime());
//				if (vhg.getValidityExp() != null) {
//					validity = alertsEJBRemote.getValidity(vehicleeventList,
//							vhg.getValidityExp());
//				} else {
//					validity = true;
//				}
//				if ((getDay.get(today)) && (curTime) && (validity)) {
//					if (!preMap.getValue()) {
//						if (onEnter) {
//							String eventTime = TimeZoneUtil
//									.getStrTZDateTime(currVE.getId()
//											.getEventTimeStamp());
//
//							String description = "LTS GEO: Vehicle %0D"
//									+ plateNo + " %0D Entered "
//									+ geozones.getArea() + " Zone %0D"
//									+ geozones.getZoneName() + " %0D At:"
//									+ eventTime;
//
//							Vehiclealerts va = new Vehiclealerts();
//							va.setAlerttype(AlertsManager.enumAlerts.ONENTER
//									.name());
//							va.setSubalerttype(AlertsManager.enumAlerts.GEOSOFT
//									.name());
//							va.setDescription(description);
//							va.setEventTimeStamp(currVE.getId()
//									.getEventTimeStamp());
//							va.setLatlng(currVE.getLatitude() + ","
//									+ currVE.getLongitude());
//							va.setSmsmobile(mobile);
//							va.setVin(vin);
//							va.setShowstatus(false);
//
//							Alertconfig alertConfig = new Alertconfig();
//							alertConfig.setSmsNumber(vhg.getMobileNumber()
//									+ "#" + vhg.getMobileNumber1());
//							alertConfig.setThroughSms(""
//									+ (vhg.getSmsThrough() ? 1 : 0) + "#" + ""
//									+ (vhg.getSmsThrough1() ? 1 : 0));
//							alertConfig.setEmailAddress(vhg.getEmailAddress()
//									+ "#" + vhg.getEmailAddress1());
//							alertConfig.setThroughEmail(""
//									+ (vhg.getThroughEmail() ? 1 : 0) + "#"
//									+ "" + (vhg.getThroughEmail1() ? 1 : 0));
//							AlertconfigId id = new AlertconfigId();
//							id.setUserId(vhg.getLastUpdBy());
//							id.setAlertType(va.getAlerttype());
//							alertConfig.setId(id);
//							vehiclealerts = new ArrayList<Vehiclealerts>();
//							vehiclealerts.add(va);
//							alertsManager.persistVehicleAlert(alertConfig,
//									vehiclealerts);
//
//						}
//					} else {
//						if (onLeave) {
//							String eventTime = TimeZoneUtil
//									.getStrTZDateTime(currVE.getId()
//											.getEventTimeStamp());
//
//							String description = "LTS GEO: Vehicle %0D"
//									+ plateNo + " %0D Exited "
//									+ geozones.getArea() + " Zone %0D"
//									+ geozones.getZoneName() + " %0D At:"
//									+ eventTime;
//
//							Vehiclealerts va = new Vehiclealerts();
//							va.setAlerttype(AlertsManager.enumAlerts.ONLEAVE
//									.name());
//							va.setSubalerttype(AlertsManager.enumAlerts.GEOSOFT
//									.name());
//							va.setDescription(description);
//							va.setEventTimeStamp(currVE.getId()
//									.getEventTimeStamp());
//							va.setLatlng(currVE.getLatitude() + ","
//									+ currVE.getLongitude());
//							va.setSmsmobile(mobile);
//							va.setVin(vin);
//							va.setShowstatus(false);
//
//							Alertconfig alertConfig = new Alertconfig();
//							alertConfig.setSmsNumber(vhg.getMobileNumber()
//									+ "#" + vhg.getMobileNumber1());
//							alertConfig.setThroughSms(""
//									+ (vhg.getSmsThrough() ? 1 : 0) + "#" + ""
//									+ (vhg.getSmsThrough1() ? 1 : 0));
//							alertConfig.setEmailAddress(vhg.getEmailAddress()
//									+ "#" + vhg.getEmailAddress1());
//							alertConfig.setThroughEmail(""
//									+ (vhg.getThroughEmail() ? 1 : 0) + "#"
//									+ "" + (vhg.getThroughEmail1() ? 1 : 0));
//							AlertconfigId id = new AlertconfigId();
//							id.setUserId(vhg.getLastUpdBy());
//							id.setAlertType(va.getAlerttype());
//							alertConfig.setId(id);
//							vehiclealerts = new ArrayList<Vehiclealerts>();
//							vehiclealerts.add(va);
//							alertsManager.persistVehicleAlert(alertConfig,
//									vehiclealerts);
//						}
//					}
//				}
//			}
//		}
//	}
//
//	private Map<Integer, Geozones> getGeozoneDetail(
//			List<VehicleHasGeozones> vehicleHasGeozones) {
//		Map<Integer, Geozones> map = new HashMap<Integer, Geozones>();
//		for (int i = 0; i < vehicleHasGeozones.size(); i++) {
//			map.put(((Long) vehicleHasGeozones.get(i).getGeozones().getId())
//					.intValue(), vehicleHasGeozones.get(i).getGeozones());
//		}
//		return map;
//	}
//
//	private VehicleHasGeozones getVhgFromGeozoneId(int id) {
//		for (int i = 0; i < vehicleHasGeozones.size(); i++) {
//			if (id == vehicleHasGeozones.get(i).getGeozones().getId()) {
//				return vehicleHasGeozones.get(i);
//			}
//		}
//		return null;
//	}
//}