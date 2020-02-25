package com.eiw.alerts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONObject;

import com.eiw.device.ejb.VehicleComposite;
import com.eiw.server.fleettrackingpu.Alertconfig;
import com.eiw.server.fleettrackingpu.Heartbeatevent;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.VehicleHasIo;
import com.eiw.server.fleettrackingpu.Vehiclealerts;
import com.eiw.server.fleettrackingpu.Vehicleevent;

public class CheckPanelAlarms implements CheckAlerts {

	AlertsEJBRemote alertsEJBRemote = null;
	AlertsManager alertsManager;
	Alertconfig alertConfig = null;
	Date lastUpdatedTime;

	public CheckPanelAlarms(AlertsManager alertsManager1) {
		alertsManager = alertsManager1;
		alertsEJBRemote = alertsManager.alertsEJBRemote;
	}

	public CheckPanelAlarms(Alertconfig alertConfig, Date lastUpdatedTime) {
		this.alertConfig = alertConfig;
		this.lastUpdatedTime = lastUpdatedTime;
	}

	public void manageAlert(List<Vehicleevent> vehicleevents,
			Alertconfig alertConfig, String vin, String plateNo) {
		Vehicle vehicle = alertsEJBRemote.getVehicle(vin);
		String description = null;
		String type = vehicle.getVehicletype().getVehicleType();
		boolean checkIoevent = false;
		String alarms = "";
		List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();
		AlertsEJB.LOGGER.debug("Inside CheckPanelAlarms Alert ->");
		for (Vehicleevent vehi : vehicleevents) {
			if (type.equalsIgnoreCase("DEEPSEA GENERATOR")) {
				checkIoevent = vehi.getIoevent() != null
						&& !vehi.getIoevent().equalsIgnoreCase("{}");
			}
			if (checkIoevent) {
				try {
					JSONObject ioevent = new JSONObject(vehi.getIoevent());
					if (ioevent.has("alarm"))
						alarms = ioevent.get("alarm").toString();
					if (!alarms.equalsIgnoreCase("")) {
						for (String alarm : alarms.split(",")) {

							description = vehicle.getVehicletype()
									.getVehicleType()
									+ " : "
									+ vehicle.getPlateNo()
									+ ", Alarm = "
									+ alarm;
							String mobile2 = null;
							mobile2 = alertConfig.getSmsNumber();
							Vehiclealerts va = new Vehiclealerts();
							va.setAlerttype("PanelAlarms");
							va.setDescription(description);
							va.setEventTimeStamp(vehi.getId()
									.getEventTimeStamp());
							va.setLatlng(vehi.getLatitude() + ","
									+ vehi.getLongitude());
							va.setSmsmobile(mobile2);
							va.setVin(vin);
							va.setShowstatus(false);
							vehiclealerts.add(va);
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

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
		Vehicle vehicle = alertsEJBRemote.getVehicle(vin);
		String description = null;
		String type = vehicle.getVehicletype().getVehicleType();
		boolean checkIoevent = false;
		String alarms = "";
		List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();
		AlertsEJB.LOGGER.debug("Inside CheckPanelAlarms Alert ->");
		for (Vehicleevent vehi : vehicleEvents) {
			if (type.equalsIgnoreCase("DEEPSEA GENERATOR")) {
				checkIoevent = vehi.getIoevent() != null
						&& !vehi.getIoevent().equalsIgnoreCase("{}");
			}
			if (checkIoevent) {
				try {
					JSONObject ioevent = new JSONObject(vehi.getIoevent());
					if (ioevent.has("alarm"))
						alarms = ioevent.get("alarm").toString();
					if (!alarms.equalsIgnoreCase("")) {
						for (String alarm : alarms.split(",")) {

							description = vehicle.getVehicletype()
									.getVehicleType()
									+ " : "
									+ vehicle.getPlateNo()
									+ ", Alarm = "
									+ alarm;
							String mobile2 = null;
							mobile2 = alertConfig.getSmsNumber();
							Vehiclealerts va = new Vehiclealerts();
							va.setAlerttype("PanelAlarms");
							va.setDescription(description);
							va.setEventTimeStamp(vehi.getId()
									.getEventTimeStamp());
							va.setLatlng(vehi.getLatitude() + ","
									+ vehi.getLongitude());
							va.setSmsmobile(mobile2);
							va.setVin(vin);
							va.setShowstatus(false);
							vehiclealerts.add(va);
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

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
