package com.eiw.alerts;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.eiw.device.ejb.VehicleComposite;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.fleettrackingpu.Alertconfig;
import com.eiw.server.fleettrackingpu.Heartbeatevent;
import com.eiw.server.fleettrackingpu.VehicleHasIo;
import com.eiw.server.fleettrackingpu.Vehiclealerts;
import com.eiw.server.fleettrackingpu.Vehicleevent;

public class CheckOperationTime implements CheckAlerts {
	/*
	 * When Engine is ON beyond the Operation Time, then the vehicle is
	 * considered to be Alerted. Beware there is Night shift Logic in this Alert
	 */
	AlertsEJBRemote alertsEJBRemote = null;
	AlertsManager alertsManager;
	Alertconfig alertConfig = null;
	Date lastUpdatedTime;

	public CheckOperationTime(AlertsManager alertsManager1) {
		alertsManager = alertsManager1;
		alertsEJBRemote = alertsManager.alertsEJBRemote;
	}

	public CheckOperationTime(Alertconfig alertConfig, Date lastUpdatedTime) {
		this.alertConfig = alertConfig;
		this.lastUpdatedTime = lastUpdatedTime;
	}

	public void manageAlert(List<Vehicleevent> vehicleevents,
			Alertconfig alertConfig, String vin, String plateNo) {
		List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();
		String range = null, mobile2 = null;
		range = alertConfig.getAlertRange();
		mobile2 = alertConfig.getSmsNumber();

		int startTime, stopTime, a, b;
		boolean isDayShift, isAlert = false;
		String[] range1 = range.split("_");
		a = Integer.valueOf(range1[0]);
		b = Integer.valueOf(range1[1]);

		if (a < b) {
			startTime = a;
			stopTime = b;
			isDayShift = true;
		} else {
			startTime = b;
			stopTime = a;
			isDayShift = false;
		}

		AlertsEJB.LOGGER.debug("Inside CheckOperationTime Alert ->");
		for (Vehicleevent ve : vehicleevents) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(ve.getId().getEventTimeStamp());
			int hour = calendar.get(calendar.HOUR_OF_DAY);
			// No engine wire connected or Engine is OFF
			if (ve.getEngine() == null || !ve.getEngine()) {
				return;
			}
			if (!((startTime < hour) && (hour < stopTime))) {
				if (isDayShift) {
					isAlert = true;
				}
			} else {
				if (!isDayShift) {
					isAlert = true;
				}
			}
			if (isAlert) {
				String eventTime = TimeZoneUtil.getStrTZDateTime(ve.getId()
						.getEventTimeStamp());
				if (isDayShift) {

					String description = "Operation Time%0D Vehicle:" + plateNo
							+ "%0D Engine Start Time: " + eventTime
							+ "%0D Limit: " + getDate12(startTime)
							+ (startTime < 12 ? " AM" : " PM") + " to"
							+ getDate12(stopTime)
							+ (stopTime < 12 ? " AM" : " PM") + "%0D Time:"
							+ eventTime;
					Vehiclealerts va = new Vehiclealerts();
					va.setAlerttype(AlertsManager.enumAlerts.OPTIME.name());
					va.setDescription(description);
					va.setEventTimeStamp(ve.getId().getEventTimeStamp());
					va.setLatlng(ve.getLatitude() + "," + ve.getLongitude());
					va.setSmsmobile(mobile2);
					va.setVin(vin);
					va.setShowstatus(false);
					vehiclealerts.add(va);
				} else {

					String description = "Operation Time%0D Vehicle:" + plateNo
							+ "%0D Engine Start Time: " + eventTime
							+ "%0D Limit: " + getDate12(stopTime)
							+ (stopTime < 12 ? " AM" : " PM") + " to"
							+ getDate12(startTime)
							+ (startTime < 12 ? " AM" : " PM") + "%0D Time:"
							+ eventTime;
					Vehiclealerts va = new Vehiclealerts();
					va.setAlerttype(AlertsManager.enumAlerts.OPTIME.name());
					va.setDescription(description);
					va.setEventTimeStamp(ve.getId().getEventTimeStamp());
					va.setLatlng(ve.getLatitude() + "," + ve.getLongitude());
					va.setSmsmobile(mobile2);
					va.setVin(vin);
					va.setShowstatus(false);
					vehiclealerts.add(va);
				}
				break;
			}
		}
		if (!vehiclealerts.isEmpty()) {
			lastUpdatedTime = alertsManager.persistVehicleAlert(alertConfig,
					vehiclealerts, lastUpdatedTime);
		}
	}

	private int getDate12(int date24) {
		if (date24 > 12) {
			return date24 - 12;
		} else {
			return date24;
		}
	}

	@Override
	public String manageAlerts(List<Vehicleevent> vehicleEvents, String vin,
			String plateNo, List<VehicleHasIo> vehicleHasIo, int id,
			VehicleComposite vehicleComposite) {

		List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();
		String range = null, mobile2 = null;
		range = alertConfig.getAlertRange();
		mobile2 = alertConfig.getSmsNumber();

		int startTime, stopTime, a, b;
		boolean isDayShift, isAlert = false;
		String[] range1 = range.split("_");
		a = Integer.valueOf(range1[0]);
		b = Integer.valueOf(range1[1]);

		if (a < b) {
			startTime = a;
			stopTime = b;
			isDayShift = true;
		} else {
			startTime = b;
			stopTime = a;
			isDayShift = false;
		}

		AlertsEJB.LOGGER.debug("Inside CheckOperationTime Alert ->");
		for (Vehicleevent ve : vehicleEvents) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(ve.getId().getEventTimeStamp());
			int hour = calendar.get(calendar.HOUR_OF_DAY);
			// No engine wire connected or Engine is OFF
			if (ve.getEngine() == null || !ve.getEngine()) {
				return null;
			}
			if (!((startTime < hour) && (hour < stopTime))) {
				if (isDayShift) {
					isAlert = true;
				}
			} else {
				if (!isDayShift) {
					isAlert = true;
				}
			}
			if (isAlert) {
				String eventTime = TimeZoneUtil.getStrTZDateTime(ve.getId()
						.getEventTimeStamp());
				if (isDayShift) {

					String description = "Operation Time%0D Vehicle:" + plateNo
							+ "%0D Engine Start Time: " + eventTime
							+ "%0D Limit: " + getDate12(startTime)
							+ (startTime < 12 ? " AM" : " PM") + " to"
							+ getDate12(stopTime)
							+ (stopTime < 12 ? " AM" : " PM") + "%0D Time:"
							+ eventTime;
					Vehiclealerts va = new Vehiclealerts();
					va.setAlerttype(AlertsManager.enumAlerts.OPTIME.name());
					va.setDescription(description);
					va.setEventTimeStamp(ve.getId().getEventTimeStamp());
					va.setLatlng(ve.getLatitude() + "," + ve.getLongitude());
					va.setSmsmobile(mobile2);
					va.setVin(vin);
					va.setShowstatus(false);
					vehiclealerts.add(va);
				} else {

					String description = "Operation Time%0D Vehicle:" + plateNo
							+ "%0D Engine Start Time: " + eventTime
							+ "%0D Limit: " + getDate12(stopTime)
							+ (stopTime < 12 ? " AM" : " PM") + " to"
							+ getDate12(startTime)
							+ (startTime < 12 ? " AM" : " PM") + "%0D Time:"
							+ eventTime;
					Vehiclealerts va = new Vehiclealerts();
					va.setAlerttype(AlertsManager.enumAlerts.OPTIME.name());
					va.setDescription(description);
					va.setEventTimeStamp(ve.getId().getEventTimeStamp());
					va.setLatlng(ve.getLatitude() + "," + ve.getLongitude());
					va.setSmsmobile(mobile2);
					va.setVin(vin);
					va.setShowstatus(false);
					vehiclealerts.add(va);
				}
				break;
			}
		}
		if (!vehiclealerts.isEmpty()) {
			lastUpdatedTime = alertsManager.persistVehicleAlert(alertConfig,
					vehiclealerts, lastUpdatedTime);
		}

		return null;
	}

	@Override
	public void addAlertManager(AlertsManager alertsManager) {
		this.alertsManager = alertsManager;
		this.alertsEJBRemote = alertsManager.alertsEJBRemote;

	}

	@Override
	public void setLastUpdatedTime(Date lastUpdated) {
		// TODO Auto-generated method stub

	}

	@Override
	public String manageHbAlerts(Heartbeatevent heartbeatevent, String vin,
			String PlateNo, VehicleComposite vehicleComposite) {
		// TODO Auto-generated method stub
		return null;
	}
}