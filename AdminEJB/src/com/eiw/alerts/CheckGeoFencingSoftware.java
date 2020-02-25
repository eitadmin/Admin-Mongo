package com.eiw.alerts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import com.eiw.server.TimeZoneUtil;
import com.eiw.server.fleettrackingpu.Alertconfig;
import com.eiw.server.fleettrackingpu.AlertconfigId;
import com.eiw.server.fleettrackingpu.Geozones;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.VehicleHasGeozones;
import com.eiw.server.fleettrackingpu.Vehiclealerts;
import com.eiw.server.fleettrackingpu.Vehicleevent;
import com.skt.client.dto.LatLng;

public class CheckGeoFencingSoftware {
	/*
	 * Geofence Entered/Leved Alerts
	 */

	AlertsEJBRemote alertsEJBRemote = null;
	AlertsManager alertsManager;
	List<Vehicleevent> vehicleevents;
	List<VehicleHasGeozones> vehicleHasGeozones = null;
	Map<Integer, Boolean> prevHMap = null, currHMap = null;
	Map<Integer, Geozones> geoMap = null;
	String plateNo, vin;
	Vehicle vehicleDetail;
	List<Vehiclealerts> vehiclealerts = null;
	boolean validity = false;

	public CheckGeoFencingSoftware(AlertsManager alertsManager1) {
		alertsManager = alertsManager1;
		alertsEJBRemote = alertsManager.alertsEJBRemote;
	}

	public void manageAlert(List<Vehicleevent> vehicleevents1, Vehicle vehicle,
			String plateNo1) {

		plateNo = plateNo1;
		vin = vehicle.getVin();
		vehicleDetail = vehicle;
		vehicleevents = vehicleevents1;

		// Getting Geozones Associated for this Vehicle
		vehicleHasGeozones = alertsEJBRemote.getGeoConfig(vin, "GEOSOFT");
		if (vehicleHasGeozones.isEmpty()) {
			return;
		}

		// Getting GeoId, Geozone
		geoMap = getGeozoneDetail(vehicleHasGeozones);

		List<Date> dates = new ArrayList<Date>();
		for (Vehicleevent evt : vehicleevents) {
			dates.add(evt.getId().getEventTimeStamp());
		}
		Collections.sort(dates);

		Vehicleevent prevVE = alertsEJBRemote.getGeoPrevVe(vin);
		if (prevVE == null) {
			return;
		}
		prevHMap = getGeoStatusHashMap(prevVE);
		if (prevHMap.isEmpty())
			return;

		for (int i = 0; i < dates.size(); i++) {
			Vehicleevent currVE = getVE(dates.get(i));

			// omitting older records coming in Transmission
			Date currDate = currVE.getId().getEventTimeStamp();
			Date prevDate = prevVE.getId().getEventTimeStamp();
			if (currDate.getTime() < prevDate.getTime()) {
				continue;
			}

			currHMap = getGeoStatusHashMap(currVE);
			prepareAlert(currHMap, prevHMap, currVE);
			prevVE = currVE;
			prevHMap = currHMap;
		}

	}

	private Vehicleevent getVE(Date date) {
		for (Vehicleevent ve : vehicleevents) {
			long a = ve.getId().getEventTimeStamp().getTime();
			long b = date.getTime();
			if (a == b) {
				return ve;
			}
		}
		return null;
	}

	// private Map<Integer, Boolean> getGeoStatusHashMap(Vehicleevent evt) {
	// StringBuilder qryStr = new StringBuilder();
	// for (int i = 0; i < vehicleHasGeozones.size(); i++) {
	// Geozones geozones = vehicleHasGeozones.get(i).getGeozones();
	// String[] latLng = geozones.getLatlng().split("\\|");
	// String latCen = latLng[0].replace(",", " ");
	// String latRad = latLng[1].replace(",", " ");
	// String shape = geozones.getShape();
	// Vehicleevent ve = evt;
	// String latMar = (ve.getLatitude() + "," + ve.getLongitude())
	// .replace(",", " ");
	// if (shape.equalsIgnoreCase("Circle")) {
	// qryStr.append("SELECT "
	// + geozones.getId()
	// + " ,IF(((SELECT GLENGTH(GEOMFROMTEXT  ('LineString("
	// + latCen
	// + ","
	// + latRad
	// + ")')) AS distance)   > (SELECT GLENGTH(GEOMFROMTEXT('LineString("
	// + latCen + "," + latMar
	// + ")'))   AS distance)),'true','false') AS output");
	// } else {
	// String[] ab = latLng[0].split(",");
	// String[] cd = latLng[1].split(",");
	// qryStr.append("SELECT " + geozones.getId()
	// + " ,CASE WHEN(MBRCONTAINS(GEOMFROMTEXT('POLYGON(("
	// + ab[0] + " " + ab[1] + "," + cd[0] + " " + ab[1] + ","
	// + cd[0] + " " + cd[1] + "," + ab[0] + " " + cd[1] + ","
	// + ab[0] + " " + ab[1] + "))'),GEOMFROMTEXT('Point("
	// + latMar + ")')))=1 THEN 'true' ELSE 'false' END");
	// }
	// if (i != (vehicleHasGeozones.size() - 1)) {
	// qryStr.append(" union all ");
	// }
	// }
	// return alertsEJBRemote.getGeoStatusHashMap(qryStr.toString());
	// }

	private Map<Integer, Boolean> getGeoStatusHashMap(Vehicleevent evt) {
		LatLng curLatLng = new LatLng();
		curLatLng.setLat(evt.getLatitude().toString());
		curLatLng.setLng(evt.getLongitude().toString());
		Map<Integer, Boolean> hashMap = new HashMap<Integer, Boolean>();
		for (int i = 0; i < vehicleHasGeozones.size(); i++) {
			Geozones geozones = vehicleHasGeozones.get(i).getGeozones();
			String[] latLng = geozones.getLatlng().split("\\|");
			String latCen = latLng[0];
			String latRad = latLng[1];
			String shape = geozones.getShape();
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
					hashMap.put(
							Integer.parseInt(String.valueOf(geozones.getId())),
							true);
				} else {
					hashMap.put(
							Integer.parseInt(String.valueOf(geozones.getId())),
							false);
				}
			} else {
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
				hashMap.put(Integer.parseInt(String.valueOf(geozones.getId())),
						status);
			}
		}
		return hashMap;
	}

	private void prepareAlert(Map<Integer, Boolean> currHMap,
			Map<Integer, Boolean> prevHMap, Vehicleevent currVE) {
		int today;
		boolean curTime;
		String timeZone = alertsEJBRemote.getTimeZoneRegion(vin);
		for (Map.Entry<Integer, Boolean> preMap : prevHMap.entrySet()) {
			List<Vehicleevent> vehicleeventList = new ArrayList<Vehicleevent>();
			if (preMap.getValue() != currHMap.get(preMap.getKey())) {
				Geozones geozones = geoMap.get(preMap.getKey());
				VehicleHasGeozones vhg = getVhgFromGeozoneId(preMap.getKey());
				boolean onLeave = vhg.getOnLeave();
				boolean onEnter = vhg.getOnEnter();
				String mobile = vhg.getMobileNumber();
				// Date and Time Check
				SortedMap<Integer, Boolean> getDay = alertsEJBRemote
						.getDayAlert(vhg.getAlertDay());
				today = alertsEJBRemote.getDay(currVE.getId()
						.getEventTimeStamp(), timeZone);
				vehicleeventList.add(currVE);
				curTime = alertsEJBRemote.getTime(vehicleeventList,
						vhg.getAlertTime());
				if (vhg.getValidityExp() != null) {
					validity = alertsEJBRemote.getValidity(vehicleeventList,
							vhg.getValidityExp());
				} else {
					validity = true;
				}
				if ((getDay.get(today)) && (curTime) && (validity)) {
					if (!preMap.getValue()) {
						if (onEnter) {
							String eventTime = TimeZoneUtil
									.getStrTZDateTime(currVE.getId()
											.getEventTimeStamp());

							String description = "GEO: Vehicle %0D" + plateNo
									+ " %0D Entered " + geozones.getArea()
									+ " Zone %0D" + geozones.getZoneName()
									+ " %0D At:" + eventTime;

							Vehiclealerts va = new Vehiclealerts();
							va.setAlerttype(AlertsManager.enumAlerts.ONENTER
									.name());
							va.setSubalerttype(AlertsManager.enumAlerts.GEOSOFT
									.name());
							va.setDescription(description);
							va.setEventTimeStamp(currVE.getId()
									.getEventTimeStamp());
							va.setLatlng(currVE.getLatitude() + ","
									+ currVE.getLongitude());
							va.setSmsmobile(mobile);
							va.setVin(vin);
							va.setShowstatus(false);

							Alertconfig alertConfig = new Alertconfig();
							alertConfig.setSmsNumber(vhg.getMobileNumber()
									+ "#" + vhg.getMobileNumber1());
							alertConfig.setThroughSms(""
									+ (vhg.getSmsThrough() ? 1 : 0) + "#" + ""
									+ (vhg.getSmsThrough1() ? 1 : 0));
							alertConfig.setEmailAddress(vhg.getEmailAddress()
									+ "#" + vhg.getEmailAddress1());
							alertConfig.setThroughEmail(""
									+ (vhg.getThroughEmail() ? 1 : 0) + "#"
									+ "" + (vhg.getThroughEmail1() ? 1 : 0));
							AlertconfigId id = new AlertconfigId();
							id.setCompanyId(vehicleDetail.getCompanyId());
							id.setBranchId(vehicleDetail.getBranchId());
							id.setUserId(vhg.getLastUpdBy());
							id.setVin(vehicleDetail.getVin());
							id.setAlertType(va.getAlerttype());
							alertConfig.setId(id);
							vehiclealerts = new ArrayList<Vehiclealerts>();
							vehiclealerts.add(va);
							alertsManager.persistVehicleAlert(alertConfig,
									vehiclealerts);

						}
					} else {
						if (onLeave) {
							String eventTime = TimeZoneUtil
									.getStrTZDateTime(currVE.getId()
											.getEventTimeStamp());

							String description = "GEO: Vehicle %0D" + plateNo
									+ " %0D Exited " + geozones.getArea()
									+ " Zone %0D" + geozones.getZoneName()
									+ " %0D At:" + eventTime;

							Vehiclealerts va = new Vehiclealerts();
							va.setAlerttype(AlertsManager.enumAlerts.ONLEAVE
									.name());
							va.setSubalerttype(AlertsManager.enumAlerts.GEOSOFT
									.name());
							va.setDescription(description);
							va.setEventTimeStamp(currVE.getId()
									.getEventTimeStamp());
							va.setLatlng(currVE.getLatitude() + ","
									+ currVE.getLongitude());
							va.setSmsmobile(mobile);
							va.setVin(vin);
							va.setShowstatus(false);

							Alertconfig alertConfig = new Alertconfig();
							alertConfig.setSmsNumber(vhg.getMobileNumber()
									+ "#" + vhg.getMobileNumber1());
							alertConfig.setThroughSms(""
									+ (vhg.getSmsThrough() ? 1 : 0) + "#" + ""
									+ (vhg.getSmsThrough1() ? 1 : 0));
							alertConfig.setEmailAddress(vhg.getEmailAddress()
									+ "#" + vhg.getEmailAddress1());
							alertConfig.setThroughEmail(""
									+ (vhg.getThroughEmail() ? 1 : 0) + "#"
									+ "" + (vhg.getThroughEmail1() ? 1 : 0));
							AlertconfigId id = new AlertconfigId();
							id.setCompanyId(vehicleDetail.getCompanyId());
							id.setBranchId(vehicleDetail.getBranchId());
							id.setUserId(vhg.getLastUpdBy());
							id.setVin(vehicleDetail.getVin());
							id.setAlertType(va.getAlerttype());
							alertConfig.setId(id);
							vehiclealerts = new ArrayList<Vehiclealerts>();
							vehiclealerts.add(va);
							alertsManager.persistVehicleAlert(alertConfig,
									vehiclealerts);
						}
					}
				}
			}
		}
	}

	private Map<Integer, Geozones> getGeozoneDetail(
			List<VehicleHasGeozones> vehicleHasGeozones) {
		Map<Integer, Geozones> map = new HashMap<Integer, Geozones>();
		for (int i = 0; i < vehicleHasGeozones.size(); i++) {
			map.put(((Long) vehicleHasGeozones.get(i).getGeozones().getId())
					.intValue(), vehicleHasGeozones.get(i).getGeozones());
		}
		return map;
	}

	private VehicleHasGeozones getVhgFromGeozoneId(int id) {
		for (int i = 0; i < vehicleHasGeozones.size(); i++) {
			if (id == vehicleHasGeozones.get(i).getGeozones().getId()) {
				return vehicleHasGeozones.get(i);
			}
		}
		return null;
	}

	private float distanceBetweenTwoPoints(float lat1, float lng1, float lat2,
			float lng2) {
		double earthRadius = 6371; // meters
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
}