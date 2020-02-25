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

public class CheckEngineTowed implements CheckAlerts {

	/*
	 * When Engine is OFF and speed > 5 then the vehicle is considered to be
	 * Towed
	 */
	AlertsManager alertsManager;
	Alertconfig alertConfig = null;
	Date lastUpdatedTime;

	public CheckEngineTowed(AlertsManager alertsManager1) {
		alertsManager = alertsManager1;
	}

	public CheckEngineTowed(Alertconfig alertConfig, Date lastUpdatedTime) {
		this.alertConfig = alertConfig;
		this.lastUpdatedTime = lastUpdatedTime;
	}

	public void manageAlert(List<Vehicleevent> listOfEvent,
			Alertconfig alertConfig, String vin, String plateNo) {
		List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();

		String mobile2 = alertConfig.getSmsNumber();

		for (Vehicleevent ve : listOfEvent) {
			boolean engine = ve.getEngine();
			int speed = ve.getSpeed();

			if ((!engine) && (speed > 5)) {
				String eventTime = TimeZoneUtil.getStrTZDateTime(ve.getId()
						.getEventTimeStamp());

				String description = "Alert%0DType : Vehicle Towed%0Dvehicle:"
						+ plateNo + "%0DSpeed: " + speed + " kmph%0DTime:"
						+ eventTime;
				Vehiclealerts va = new Vehiclealerts();
				va.setAlerttype(AlertsManager.enumAlerts.TOWED.name());
				va.setDescription(description);
				va.setEventTimeStamp(ve.getId().getEventTimeStamp());
				va.setLatlng(ve.getLatitude() + "," + ve.getLongitude());
				va.setSmsmobile(mobile2);
				va.setVin(vin);
				va.setShowstatus(false);
				vehiclealerts.add(va);
				break;
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

		String mobile2 = alertConfig.getSmsNumber();

		for (Vehicleevent ve : vehicleEvents) {
			boolean engine = ve.getEngine();
			int speed = ve.getSpeed();

			if ((!engine) && (speed > 5)) {
				String eventTime = TimeZoneUtil.getStrTZDateTime(ve.getId()
						.getEventTimeStamp());

				String description = "Alert%0DType : Vehicle Towed%0Dvehicle:"
						+ plateNo + "%0DSpeed: " + speed + " kmph%0DTime:"
						+ eventTime + "%0Dvin:" + vin;
				Vehiclealerts va = new Vehiclealerts();
				va.setAlerttype(AlertsManager.enumAlerts.TOWED.name());
				va.setDescription(description);
				va.setEventTimeStamp(ve.getId().getEventTimeStamp());
				va.setLatlng(ve.getLatitude() + "," + ve.getLongitude());
				va.setSmsmobile(mobile2);
				va.setVin(vin);
				va.setShowstatus(false);
				vehiclealerts.add(va);
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
