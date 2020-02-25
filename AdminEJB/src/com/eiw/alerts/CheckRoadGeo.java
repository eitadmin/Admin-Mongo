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
import com.eiw.server.fleettrackingpu.Route;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.VehicleHasGeozones;
import com.eiw.server.fleettrackingpu.VehicleHasRoute;
import com.eiw.server.fleettrackingpu.Vehiclealerts;
import com.eiw.server.fleettrackingpu.Vehicleevent;

public class CheckRoadGeo {
	AlertsEJBRemote alertsEJBRemote = null;
	AlertsManager alertsManager;
	List<Vehicleevent> vehicleevents;
	List<VehicleHasRoute> vehicleHasRoute = null;
	Map<Integer, Boolean> prevHMap = null, currHMap = null;
	Map<Integer, String> prevSubHMap = null, currSubHMap = null;
	Map<Integer, Route> geoMap = null;
	String plateNo, vin;
	Vehicle vehicleDetail;
	List<Vehiclealerts> vehiclealerts = null;
	boolean validity = false;

	public CheckRoadGeo(AlertsManager alertsManager1) {
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
		vehicleHasRoute = alertsEJBRemote.getRoadGeoConfig(vin);
		if (vehicleHasRoute.isEmpty()) {
			return;
		}
		// Getting GeoId, Geozone
		geoMap = getGeozoneDetail(vehicleHasRoute);

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
		prevSubHMap = getSubStationStatus(prevVE);
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
			currSubHMap = getSubStationStatus(currVE);
			prepareAlert(currHMap, prevHMap, currVE);
			prepareSubStationAlert(currSubHMap, prevSubHMap, currVE);
			prevVE = currVE;
			prevHMap = currHMap;
			prevSubHMap = currSubHMap;
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

	private Map<Integer, Boolean> getGeoStatusHashMap(Vehicleevent evt) {
		Map<Integer, Boolean> hashMap = new HashMap<Integer, Boolean>();
		for (int i = 0; i < vehicleHasRoute.size(); i++) {
			int id = vehicleHasRoute.get(i).getId().getRouteid();
			boolean status = alertsEJBRemote.getRoadGeoStatus(evt, id);
			hashMap.put(id, status);
		}
		return hashMap;
	}

	// ///////////////////////////////////////////////////////////////////////

	private Map<Integer, String> getSubStationStatus(Vehicleevent evt) {
		Map<Integer, String> subStationHMap = new HashMap<Integer, String>();
		for (int i = 0; i < vehicleHasRoute.size(); i++) {
			int id = vehicleHasRoute.get(i).getId().getRouteid();
			String status = alertsEJBRemote.getRoadGeoSubStationStatus(evt, id);
			subStationHMap.put(id, status);
		}
		return subStationHMap;
	}

	// /////////////////////////////////////////////////////////////////////////

	private void prepareAlert(Map<Integer, Boolean> currHMap,
			Map<Integer, Boolean> prevHMap, Vehicleevent currVE) {
		int today;
		boolean curTime;
		String timeZone = alertsEJBRemote.getTimeZoneRegion(vin);
		for (Map.Entry<Integer, Boolean> preMap : prevHMap.entrySet()) {
			List<Vehicleevent> vehicleeventList = new ArrayList<Vehicleevent>();
			if (preMap.getValue() != currHMap.get(preMap.getKey())) {
				VehicleHasRoute vhg = getVhgFromGeozoneId(preMap.getKey());
				boolean onLeave = vhg.getOnLeave();
				boolean onEnter = vhg.getOnEnter();
				String mobile = vhg.getMobileNumber();
				Route route = geoMap.get(preMap.getKey());
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
							String description = "Alert%0DType : RoadGEO - OnEnter Alert%0Dvehicle:"
									+ plateNo
									+ " Geo Name:%0D"
									+ route.getRoutename()
									+ "%0DTime:"
									+ eventTime;

							Vehiclealerts va = new Vehiclealerts();
							va.setAlerttype(AlertsManager.enumAlerts.ONENTER
									.name());
							va.setSubalerttype(AlertsManager.enumAlerts.GEOROAD
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

							String description = "Alert%0DType : RoadGEO - OnLeave Alert%0Dvehicle:"
									+ plateNo
									+ " Geo Name:%0D"
									+ route.getRoutename()
									+ "%0DTime:"
									+ eventTime;

							Vehiclealerts va = new Vehiclealerts();
							va.setAlerttype(AlertsManager.enumAlerts.ONLEAVE
									.name());
							va.setSubalerttype(AlertsManager.enumAlerts.GEOROAD
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

	private void prepareSubStationAlert(Map<Integer, String> currHMap,
			Map<Integer, String> prevHMap, Vehicleevent currVE) {
		int today;
		boolean curTime;
		String timeZone = alertsEJBRemote.getTimeZoneRegion(vin);
		for (Map.Entry<Integer, String> preMap : prevHMap.entrySet()) {
			List<Vehicleevent> vehicleeventList = new ArrayList<Vehicleevent>();
			if (preMap.getValue() != currHMap.get(preMap.getKey())) {
				VehicleHasRoute vhg = getVhgFromGeozoneId(preMap.getKey());
				String subStationName = preMap.getValue().split(",")[1];
				boolean onLeave = vhg.getOnLeave();
				boolean onEnter = vhg.getOnEnter();
				String mobile = vhg.getMobileNumber();
				Route route = geoMap.get(preMap.getKey());
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
					if ((preMap.getValue().split(",")[0])
							.equalsIgnoreCase("true")) {
						if (onEnter) {
							String eventTime = TimeZoneUtil
									.getStrTZDateTime(currVE.getId()
											.getEventTimeStamp());

							String description = "GEO: Vehicle %0D" + plateNo
									+ " %0D Entered  Zone %0D" + subStationName
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
									+ " %0D Exited Zone %0D" + subStationName
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

	private Map<Integer, Route> getGeozoneDetail(
			List<VehicleHasRoute> vehicleHasRoute) {
		Map<Integer, Route> map = new HashMap<Integer, Route>();
		for (int i = 0; i < vehicleHasRoute.size(); i++) {
			int id = vehicleHasRoute.get(i).getId().getRouteid();
			map.put(id, alertsEJBRemote.getRoadGeo(id));
		}
		return map;
	}

	private VehicleHasRoute getVhgFromGeozoneId(int id) {
		for (int i = 0; i < vehicleHasRoute.size(); i++) {
			if (id == vehicleHasRoute.get(i).getId().getRouteid()) {
				return vehicleHasRoute.get(i);
			}
		}
		return null;
	}
}