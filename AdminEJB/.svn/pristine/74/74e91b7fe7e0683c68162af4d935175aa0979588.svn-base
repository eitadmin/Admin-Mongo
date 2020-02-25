package com.eiw.alerts;

import java.util.ArrayList;
import java.util.Date;
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

public class CheckGeoFencingHardware {
	/*
	 * Geofence Entered/Leved Alerts thru Hardware IO's
	 */
	AlertsEJBRemote alertsEJBRemote = null;
	AlertsManager alertsManager;
	List<VehicleHasGeozones> vehicleHasGeozones = null;
	Map<Integer, Boolean> prevHMap = null, currHMap = null;
	Map<Integer, Geozones> geoMap = null;
	String plateNo, vin;
	Vehicle vehicleDetail;
	List<Vehiclealerts> vehiclealerts = null;
	boolean validity = false;
	private static final String STR_ON_ENTER = "OnEnter";

	public CheckGeoFencingHardware(AlertsManager alertsManager1) {
		alertsManager = alertsManager1;
		alertsEJBRemote = alertsManager.alertsEJBRemote;
	}

	public void manageAlert(List<Vehicleevent> vehicleevents, Vehicle vehicle,
			String plateNo1) {

		plateNo = plateNo1;
		vin = vehicle.getVin();
		vehicleDetail = vehicle;

		// Getting Geozones Associated for this Vehicle
		vehicleHasGeozones = alertsEJBRemote.getGeoConfig(vin, "GEOHARD");
		AlertsEJB.LOGGER.debug("Inside CheckGeoFencingHardware Alert ->");
		// First level chk
		boolean isGeoEventComes = false;
		for (Vehicleevent ve : vehicleevents) {
			if (ve.getIoevent() != null) {
				String ioEvent = ve.getIoevent();
				String[] ioStatus = ioEvent.split(",");
				for (int j = 0; j < ioStatus.length; j++) {
					String[] engineStatus = ioStatus[j].split("=");
					if (engineStatus.length > 1) {
						int a = Integer.parseInt(engineStatus[0]);
						if ((a > 154) && (a < 175)) {
							isGeoEventComes = true;
							break;
						}
					}

				}
				if (isGeoEventComes) {
					break;
				}
			}
		}

		if (!isGeoEventComes) {
			return;
		}

		// If Geo Event comes is confirmed then iterate and alert accordingly
		for (Vehicleevent ve : vehicleevents) {
			if (ve.getIoevent() != null) {
				String ioEvent = ve.getIoevent();
				String[] ioStatus = ioEvent.split(",");

				for (int j = 0; j < ioStatus.length; j++) {
					String[] engineStatus = ioStatus[j].split("=");
					if (engineStatus.length > 1) {
						for (int k = 0; k < vehicleHasGeozones.size(); k++) {
							if (engineStatus[0]
									.equalsIgnoreCase(vehicleHasGeozones.get(k)
											.getDeviceGeoId() + "")) {
								sendHardwareAlert(engineStatus[1], ve,
										vehicleHasGeozones.get(k));
							}
						}

					}

				}
			}
		}
	}

	private void sendHardwareAlert(String val, Vehicleevent ve,
			VehicleHasGeozones vhg) {
		List<Vehicleevent> vehicleevents = new ArrayList<Vehicleevent>();
		String eventTime = TimeZoneUtil.getStrTZDateTime(ve.getId()
				.getEventTimeStamp());
		Geozones geozones = vhg.getGeozones();
		// Date and Time Check
		int today;
		boolean curTime;
		String timeZone = alertsEJBRemote.getTimeZoneRegion(vin);
		SortedMap<Integer, Boolean> getDay = alertsEJBRemote.getDayAlert(vhg
				.getAlertDay());
		today = alertsEJBRemote
				.getDay(ve.getId().getEventTimeStamp(), timeZone);
		vehicleevents.add(ve);
		curTime = alertsEJBRemote.getTime(vehicleevents, vhg.getAlertTime());
		if (vhg.getValidityExp() != null) {
			validity = alertsEJBRemote.getValidity(vehicleevents,
					vhg.getValidityExp());
		} else {
			validity = true;
		}
		if ((getDay.get(today)) && (curTime) && (validity)) {
			int valInt = Integer.parseInt(val);
			String alertType = STR_ON_ENTER;
			if (valInt == 0) {
				alertType = "OnLeave";
			}

			String description = "Alert%0DType : Geofence - " + alertType
					+ " Alert%0Dvehicle:" + plateNo + "Mode: "
					+ geozones.getArea() + " Name:%0D" + geozones.getZoneName()
					+ "%0DTime:" + eventTime;

			Vehiclealerts va = new Vehiclealerts();
			if (alertType.equalsIgnoreCase(STR_ON_ENTER)) {
				va.setAlerttype(AlertsManager.enumAlerts.ONENTER.name());
			} else {
				va.setAlerttype(AlertsManager.enumAlerts.ONLEAVE.name());
			}
			va.setSubalerttype(AlertsManager.enumAlerts.GEOHARD.name());
			va.setDescription(description);
			va.setEventTimeStamp(ve.getId().getEventTimeStamp());
			va.setLatlng(ve.getLatitude() + "," + ve.getLongitude());
			va.setSmsmobile(vhg.getMobileNumber());
			va.setVin(vin);
			va.setShowstatus(false);

			Alertconfig alertConfig = new Alertconfig();
			alertConfig.setSmsNumber(vhg.getMobileNumber() + "#"
					+ vhg.getMobileNumber1());
			alertConfig.setThroughSms("" + (vhg.getSmsThrough() ? 1 : 0) + "#"
					+ "" + (vhg.getSmsThrough1() ? 1 : 0));
			alertConfig.setEmailAddress(vhg.getEmailAddress() + "#"
					+ vhg.getEmailAddress1());
			alertConfig.setThroughEmail("" + (vhg.getThroughEmail() ? 1 : 0)
					+ "#" + "" + (vhg.getThroughEmail1() ? 1 : 0));
			AlertconfigId alertConfigId = new AlertconfigId();
			alertConfigId.setCompanyId(vehicleDetail.getCompanyId());
			alertConfigId.setBranchId(vehicleDetail.getBranchId());
			alertConfigId.setUserId(vhg.getLastUpdBy());
			alertConfigId.setVin(vehicleDetail.getVin());
			alertConfig.setId(alertConfigId);
			vehiclealerts = new ArrayList<Vehiclealerts>();
			vehiclealerts.add(va);
			alertsManager.persistVehicleAlert(alertConfig, vehiclealerts);
		}
	}
}