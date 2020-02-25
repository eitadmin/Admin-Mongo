package com.eiw.alerts;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import com.eiw.cron.AlertConfigEJB;
import com.eiw.device.ejb.VehicleComposite;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.fleettrackingpu.Alertconfig;
import com.eiw.server.fleettrackingpu.Heartbeatevent;
import com.eiw.server.fleettrackingpu.VehicleHasIo;
import com.eiw.server.fleettrackingpu.Vehiclealerts;
import com.eiw.server.fleettrackingpu.Vehicleevent;

public class CheckOverspeed implements CheckAlerts {
	AlertsManager alertsManager;
	AlertsEJBRemote alertsEJBRemote = null;
	Alertconfig alertConfig = null;
	Date lastUpdatedTime;

	public CheckOverspeed(AlertsManager alertsManager1) {
		alertsManager = alertsManager1;
		alertsEJBRemote = alertsManager.alertsEJBRemote;
	}

	public CheckOverspeed(Alertconfig alertConfig, Date lastUpdatedTime) {
		this.alertConfig = alertConfig;
		this.lastUpdatedTime = lastUpdatedTime;
	}

	public void manageAlert(List<Vehicleevent> vehicleEvents,
			Alertconfig alertConfig, String vin, String plateNo) {
		List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();
		Date speedTimeStamp = null;
		int speedActual;
		int speedLimit = 0, intSpeedLimit = 0;
		int speedMax = 0;
		String speedLatLng = null, address = null;
		int speedtemp;
		String mobile = null;
		boolean isMax = true;

		mobile = alertConfig.getSmsNumber();
		speedtemp = Integer.parseInt(alertConfig.getAlertRange());
		AlertsEJB.LOGGER.debug("Inside CheckOverspeed Alert ->");
		// Getting Max Speed
		for (Vehicleevent e : vehicleEvents) {
			if (isMax) {
				speedLimit = speedtemp;
				intSpeedLimit = speedtemp;
			}
			speedActual = e.getSpeed();
			if (speedActual > speedLimit) {
				speedMax = speedActual;
				speedLimit = speedMax;
				speedTimeStamp = e.getId().getEventTimeStamp();
				speedLatLng = e.getLatitude() + "," + e.getLongitude();
				// As per discussion,Address fetching disabled
				// address = alertsEJBRemote.getAddress(e.getLatitude() + "",
				// e.getLongitude() + "");
				isMax = false;
			}
		}
		if (speedMax > intSpeedLimit) {
			isMax = true;
			String speedTime = TimeZoneUtil.getStrTZDateTime(speedTimeStamp);
			// For Checking
			// if (address.length() > 50) {
			// address = address.substring(0, 50);
			// }
			String description = "Speed: Vehicle " + plateNo + " speed is "
					+ speedMax + "km  " + speedLatLng + "  at " + speedTime
					+ " Speed limit is " + intSpeedLimit;

			Vehiclealerts va = new Vehiclealerts();
			va.setAlerttype(AlertsManager.enumAlerts.OVERSPEED.name());
			va.setDescription(description);
			va.setEventTimeStamp(speedTimeStamp);
			va.setLatlng(speedLatLng);
			va.setSmsmobile(mobile);
			va.setVin(vin);
			va.setShowstatus(false);
			vehiclealerts.add(va);
		}
		if (!vehiclealerts.isEmpty()) {
			lastUpdatedTime = alertsManager.persistVehicleAlert(alertConfig,
					vehiclealerts, lastUpdatedTime);
		}
	}

	@Override
	public String manageAlerts(List<Vehicleevent> vehicleEvents, String vin,
			String plateNo, List<VehicleHasIo> vehicleHasIo, int id,
			VehicleComposite vehicleComposite) {
		List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();
		Date speedTimeStamp = null;
		int speedActual;
		int speedLimit = 0, intSpeedLimit = 0;
		int speedMax = 0;
		int secIgnorVal = 0;
		String speedLatLng = null, address = null;
		int speedtemp;
		String mobile = null;
		boolean isMax = true;
		long secBetTime = 0, timdiff = 0, overSpdCurVal = 0, overSpdPreVal = 0;
		mobile = alertConfig.getSmsNumber();
		speedtemp = Integer.parseInt(alertConfig.getAlertRange());
		AlertsEJB.LOGGER.debug("Inside CheckOverspeed Alert ->");
		// Getting Max Speed
		for (Vehicleevent e : vehicleEvents) {
			if (isMax) {
				speedLimit = speedtemp;
				intSpeedLimit = speedtemp;
			}
			speedActual = e.getSpeed();
			if (speedActual > speedLimit) {
				speedMax = speedActual;
				speedLimit = speedMax;
				speedTimeStamp = e.getId().getEventTimeStamp();
				speedLatLng = e.getLatitude() + "," + e.getLongitude();
				// As per discussion,Address fetching disabled
				// address = alertsEJBRemote.getAddress(e.getLatitude() + "",
				// e.getLongitude() + "");
				isMax = false;
				if (!AlertConfigEJB.secInIgnoremap.isEmpty()
						&& AlertConfigEJB.secInIgnoremap.get(vehicleComposite
								.getVehicle().getCompanyId()) != null) {
					secIgnorVal = AlertConfigEJB.secInIgnoremap
							.get(vehicleComposite.getVehicle().getCompanyId());
				} else {
					secIgnorVal = Integer.parseInt(alertsEJBRemote
							.getPreferencesData("secondsInIgnore",
									vehicleComposite.getVehicle()
											.getCompanyId()));
					AlertConfigEJB.secInIgnoremap.put(vehicleComposite
							.getVehicle().getCompanyId(), secIgnorVal);
				}
				if (AlertConfigEJB.overSpeedTimeCalc.get(vin) != null) {
					overSpdCurVal = getConvertSec(TimeZoneUtil
							.getTimeINYYYYMMddss(speedTimeStamp));
					overSpdPreVal = AlertConfigEJB.overSpeedTimeCalc.get(vin);
					timdiff = overSpdCurVal - overSpdPreVal;
				} else {
					AlertConfigEJB.overSpeedTimeCalc.put(vin,
							getConvertSec(TimeZoneUtil
									.getTimeINYYYYMMddss(speedTimeStamp)));
				}
			} else {
				AlertConfigEJB.overSpeedTimeCalc.remove(vin);
			}
		}

		if (timdiff != 0 && timdiff > secIgnorVal) {
			if (speedMax > intSpeedLimit) {
				AlertConfigEJB.overSpeedTimeCalc.put(vin, overSpdCurVal);
				isMax = true;
				String speedTime = TimeZoneUtil
						.getStrTZDateTime(speedTimeStamp);
				// For Checking

				// if (address.length() > 50) {
				// address = address.substring(0, 50);
				// address += "...";
				// }
				String description = "Speed: Vehicle " + plateNo + " speed is "
						+ speedMax + "km  " + speedLatLng + " at " + speedTime
						+ " Speed limit is " + intSpeedLimit + "=" + speedMax
						+ "=";
				Vehiclealerts va = new Vehiclealerts();
				va.setAlerttype(AlertsManager.enumAlerts.OVERSPEED.name());
				va.setDescription(description);
				va.setEventTimeStamp(speedTimeStamp);
				va.setLatlng(speedLatLng);
				va.setSmsmobile(mobile);
				va.setVin(vin);
				va.setShowstatus(false);
				vehiclealerts.add(va);
			}
		}
		if (!vehiclealerts.isEmpty()) {
			lastUpdatedTime = alertsManager.persistVehicleAlert(alertConfig,
					vehiclealerts, lastUpdatedTime);
		}
		return "success";
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

	public long getBetweenSec(String speedTimeStamp, String lastUpdatedTime) {
		Date d1 = TimeZoneUtil.getDateYYYYMMDDHHMMSS(speedTimeStamp);
		Date d2 = TimeZoneUtil.getDateYYYYMMDDHHMMSS(lastUpdatedTime);
		long diff = d1.getTime() - d2.getTime();
		long diffSeconds = diff / 1000;
		return diffSeconds;
	}

	public long getConvertSec(String speedTimeStamp) {
		Date d1 = TimeZoneUtil.getDateYYYYMMDDHHMMSS(speedTimeStamp);
		long diff = d1.getTime();
		long diffSeconds = diff / 1000;
		return diffSeconds;
	}
}
