package com.eiw.alerts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.eiw.device.ejb.VehicleComposite;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.fleettrackingpu.Alertconfig;
import com.eiw.server.fleettrackingpu.Heartbeatevent;
import com.eiw.server.fleettrackingpu.VehicleHasIo;
import com.eiw.server.fleettrackingpu.Vehiclealerts;
import com.eiw.server.fleettrackingpu.Vehicleevent;

public class CheckFatigue implements CheckAlerts {
	/*
	 * Check For last stop i.e Engine is OFF and speed =0 Check current Time -
	 * last stop time is greater than user defined fatigue time.
	 */
	AlertsEJBRemote alertsEJBRemote = null;
	AlertsManager alertsManager;
	Alertconfig alertConfig = null;
	Date lastUpdatedTime;

	public CheckFatigue(AlertsManager alertsManager1) {
		alertsManager = alertsManager1;
		alertsEJBRemote = alertsManager.alertsEJBRemote;
	}

	public CheckFatigue(Alertconfig alertConfig, Date lastUpdatedTime) {
		this.alertConfig = alertConfig;
		this.lastUpdatedTime = lastUpdatedTime;
	}

	public void manageAlert(List<Vehicleevent> vehicleevents,
			Alertconfig alertConfig, String vin, String plateNo) {
		List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();

		String range = null, mobile2 = null;
		range = alertConfig.getAlertRange();
		mobile2 = alertConfig.getSmsNumber();
		int assignedHrs = Integer.valueOf(range) * 60 * 60;

		// check for vehicle stop. if so, no need to chk for fatigue
		for (Vehicleevent ve : vehicleevents) {
			boolean engine = ve.getEngine();
			if (!engine) {
				return;
			}
		}
		AlertsEJB.LOGGER.debug("Inside CheckFatigue Alert ->");
		Vehicleevent ve = vehicleevents.get(0);
		boolean engine = ve.getEngine();
		if (engine) {
			Date cur = ve.getId().getEventTimeStamp();
			long curT = cur.getTime() / 1000;
			// checking for last stopped time
			Vehicleevent vehicleevent = alertsEJBRemote
					.getFatigueVehicleevents(ve.getId().getVin());
			if (vehicleevent == null) {
				return;
			}
			Date prev = vehicleevent.getId().getEventTimeStamp();
			long prevT = prev.getTime() / 1000;
			long diff = curT - prevT;
			if (diff >= assignedHrs) {
				// then fatigue haappens
				String eventTime = TimeZoneUtil.getStrTZDateTime(ve.getId()
						.getEventTimeStamp());
				String description = "Alert%0DType : Fatigue%0Dvehicle:"
						+ plateNo + "Limit: " + range + "  Hours%0DTime:"
						+ eventTime;
				Vehiclealerts va = new Vehiclealerts();
				va.setAlerttype(AlertsManager.enumAlerts.FATIGUE.name());
				va.setDescription(description);
				va.setEventTimeStamp(ve.getId().getEventTimeStamp());
				va.setLatlng(ve.getLatitude() + "," + ve.getLongitude());
				va.setSmsmobile(mobile2);
				va.setVin(vin);
				va.setShowstatus(false);
				vehiclealerts.add(va);
			}

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

		String range = null, mobile2 = null;
		range = alertConfig.getAlertRange();
		mobile2 = alertConfig.getSmsNumber();
		int assignedHrs = Integer.valueOf(range) * 60 * 60;

		// check for vehicle stop. if so, no need to chk for fatigue
		for (Vehicleevent ve : vehicleEvents) {
			boolean engine = ve.getEngine();
			if (!engine) {
				return null;
			}
		}
		AlertsEJB.LOGGER.debug("Inside CheckFatigue Alert ->");
		Vehicleevent ve = vehicleEvents.get(0);
		boolean engine = ve.getEngine();
		if (engine) {
			Date cur = ve.getId().getEventTimeStamp();
			long curT = cur.getTime() / 1000;
			// checking for last stopped time
			Vehicleevent vehicleevent = alertsEJBRemote
					.getFatigueVehicleevents(ve.getId().getVin());
			if (vehicleevent == null) {
				return null;
			}
			Date prev = vehicleevent.getId().getEventTimeStamp();
			long prevT = prev.getTime() / 1000;
			long diff = curT - prevT;
			if (diff >= assignedHrs) {
				// then fatigue haappens
				String eventTime = TimeZoneUtil.getStrTZDateTime(ve.getId()
						.getEventTimeStamp());
				String description = "Alert%0DType : Fatigue%0Dvehicle:"
						+ plateNo + "Limit: " + range + "  Hours%0DTime:"
						+ eventTime;
				Vehiclealerts va = new Vehiclealerts();
				va.setAlerttype(AlertsManager.enumAlerts.FATIGUE.name());
				va.setDescription(description);
				va.setEventTimeStamp(ve.getId().getEventTimeStamp());
				va.setLatlng(ve.getLatitude() + "," + ve.getLongitude());
				va.setSmsmobile(mobile2);
				va.setVin(vin);
				va.setShowstatus(false);
				vehiclealerts.add(va);
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