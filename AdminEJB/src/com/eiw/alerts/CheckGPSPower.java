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

public class CheckGPSPower implements CheckAlerts {
	AlertsEJBRemote alertsEJBRemote = null;
	AlertsManager alertsManager;
	Alertconfig alertConfig = null;
	Date lastUpdatedTime;

	public CheckGPSPower(AlertsManager alertsManager1) {
		alertsManager = alertsManager1;
		alertsEJBRemote = alertsManager.alertsEJBRemote;
	}

	public CheckGPSPower(Alertconfig alertConfig, Date lastUpdatedTime) {
		this.alertConfig = alertConfig;
		this.lastUpdatedTime = lastUpdatedTime;
	}

	public void manageAlert(List<Vehicleevent> vehicleevents,
			Alertconfig alertConfig, String vin, String plateNo) {
		List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();

		String mobile2 = null;
		mobile2 = alertConfig.getSmsNumber();
		int gpsPwr = 1;
		for (Vehicleevent ve : vehicleevents) {
			if (ve.getIoevent() != null) {
				String[] ioEvent = ve.getIoevent().split(",");
				for (int i = 0; i < ioEvent.length; i++) {
					String[] ioValue = ioEvent[i].split("=");
					String paramId = ioValue[0];
					if (paramId.equalsIgnoreCase("69")) {
						gpsPwr = Integer.parseInt(ioValue[1]);
						break;
					}
				}
			}
			// Without Engine
			if ((true) && (gpsPwr == 0)) {
				String eventTime = TimeZoneUtil.getStrTZDateTime(ve.getId()
						.getEventTimeStamp());
				String description = "Alert%0DType : GPSPower%0Dvehicle:"
						+ plateNo + "%0D%0DTime:" + eventTime;
				Vehiclealerts va = new Vehiclealerts();
				va.setAlerttype("GPSPOWER");
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

		String mobile2 = null;
		mobile2 = alertConfig.getSmsNumber();
		int gpsPwr = 1;
		for (Vehicleevent ve : vehicleEvents) {
			if (ve.getIoevent() != null) {
				String[] ioEvent = ve.getIoevent().split(",");
				for (int i = 0; i < ioEvent.length; i++) {
					String[] ioValue = ioEvent[i].split("=");
					String paramId = ioValue[0];
					if (paramId.equalsIgnoreCase("69")) {
						gpsPwr = Integer.parseInt(ioValue[1]);
						break;
					}
				}
			}
			// Without Engine
			if ((true) && (gpsPwr == 0)) {
				String eventTime = TimeZoneUtil.getStrTZDateTime(ve.getId()
						.getEventTimeStamp());
				String description = "Alert%0DType : GPSPower%0Dvehicle:"
						+ plateNo + "%0D%0DTime:" + eventTime;
				Vehiclealerts va = new Vehiclealerts();
				va.setAlerttype("GPSPOWER");
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