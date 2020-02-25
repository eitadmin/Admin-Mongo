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
import com.eiw.server.fleettrackingpu.Landmarks;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.VehicleHasLandmark;
import com.eiw.server.fleettrackingpu.Vehiclealerts;
import com.eiw.server.fleettrackingpu.Vehicleevent;

public class CheckLandmarkGeo {
	AlertsEJBRemote alertsEJBRemote = null;
	AlertsManager alertsManager;
	List<Vehicleevent> vehicleevents;
	List<VehicleHasLandmark> vehicleHasLandmark = null;
	Map<String, Boolean> prevHMap = null, currHMap = null;
	Map<String, Landmarks> geoMap = null;
	String plateNo, vin;
	Vehicle vehicleDetail;
	List<Vehiclealerts> vehiclealerts = null;
	boolean validity = false;

	public CheckLandmarkGeo(AlertsManager alertsManager1) {
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
		vehicleHasLandmark = alertsEJBRemote.getLandmarkGeoConfig(vin);
		if (vehicleHasLandmark.isEmpty()) {
			return;
		}
		// Getting GeoId, Geozone
		geoMap = getGeozoneDetail(vehicleHasLandmark);

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
		if (prevHMap.isEmpty()) {
			return;
		}

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

	private Map<String, Boolean> getGeoStatusHashMap(Vehicleevent evt) {
		StringBuilder qryStr = new StringBuilder();
		for (int i = 0; i < vehicleHasLandmark.size(); i++) {
			Landmarks landmark = alertsEJBRemote
					.getLandmarkGeo(vehicleHasLandmark.get(i).getId()
							.getLandmark());

			String latlngLandmark = landmark.getLatlng().replace(",", " ");

			// Radius Marker
			// Rad = 500m
			String[] latCen = landmark.getLatlng().split("\\,");
			double actualRadiusValue = 0.300 / 111;
			double newLatitude = Double.parseDouble(latCen[0])
					+ actualRadiusValue;
			double newLongitude = Double.parseDouble(latCen[1])
					+ actualRadiusValue;
			String newLatLng = newLatitude + ", " + newLongitude;
			String latRad = newLatLng.replace(",", " ");

			// latlng Marker
			Vehicleevent ve = evt;
			String latMar = (ve.getLatitude() + "," + ve.getLongitude())
					.replace(",", " ");

			qryStr.append("SELECT '"
					+ landmark.getId().getLandmarkName()
					+ "' ,IF(((SELECT GLENGTH(GEOMFROMTEXT  ('LineString("
					+ latlngLandmark
					+ ","
					+ latRad
					+ ")')) AS distance)   > (SELECT GLENGTH(GEOMFROMTEXT('LineString("
					+ latlngLandmark + "," + latMar
					+ ")'))   AS distance)),'true','false') AS output");
			if (i != (vehicleHasLandmark.size() - 1)) {
				qryStr.append(" union all ");
			}
		}
		return alertsEJBRemote.getLandmarkGeoStatusHashMap(qryStr.toString());
	}

	private void prepareAlert(Map<String, Boolean> currHMap,
			Map<String, Boolean> prevHMap, Vehicleevent currVE) {
		int today;
		boolean curTime;
		String timeZone = alertsEJBRemote.getTimeZoneRegion(vin);
		for (Map.Entry<String, Boolean> preMap : prevHMap.entrySet()) {
			List<Vehicleevent> vehicleeventList = new ArrayList<Vehicleevent>();
			if (preMap.getValue() != currHMap.get(preMap.getKey())) {
				VehicleHasLandmark vhg = getVhgFromGeozoneId(preMap.getKey());
				boolean onLeave = vhg.getOnLeave();
				boolean onEnter = vhg.getOnEnter();
				String mobile = vhg.getMobileNumber();
				Landmarks landmarks = geoMap.get(preMap.getKey());
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
							String description = "Alert%0DType : Landmark Geo - OnEnter Alert%0Dvehicle:"
									+ plateNo
									+ " Geo Name:%0D"
									+ landmarks.getId().getLandmarkName()
									+ "%0DTime:" + eventTime;

							Vehiclealerts va = new Vehiclealerts();
							va.setAlerttype(AlertsManager.enumAlerts.ONENTER
									.name());
							va.setSubalerttype(AlertsManager.enumAlerts.GEOLANDMARK
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
							String description = "Alert%0DType : LandmarkGeo - OnLeave Alert%0Dvehicle:"
									+ plateNo
									+ " Geo Name:%0D"
									+ landmarks.getId().getLandmarkName()
									+ "%0DTime:" + eventTime;
							Vehiclealerts va = new Vehiclealerts();
							va.setAlerttype(AlertsManager.enumAlerts.ONLEAVE
									.name());
							va.setSubalerttype(AlertsManager.enumAlerts.GEOLANDMARK
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

	private Map<String, Landmarks> getGeozoneDetail(
			List<VehicleHasLandmark> vehicleHasLandmark) {
		Map<String, Landmarks> map = new HashMap<String, Landmarks>();
		for (int i = 0; i < vehicleHasLandmark.size(); i++) {
			String id = vehicleHasLandmark.get(i).getId().getLandmark();
			map.put(id, alertsEJBRemote.getLandmarkGeo(id));
		}
		return map;
	}

	private VehicleHasLandmark getVhgFromGeozoneId(String id) {
		for (int i = 0; i < vehicleHasLandmark.size(); i++) {
			if (id.equalsIgnoreCase(vehicleHasLandmark.get(i).getId()
					.getLandmark())) {
				return vehicleHasLandmark.get(i);
			}
		}
		return null;
	}
}