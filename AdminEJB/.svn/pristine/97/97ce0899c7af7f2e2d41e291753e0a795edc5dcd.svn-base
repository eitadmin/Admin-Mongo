package com.eiw.alerts;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.eiw.device.ejb.VehicleComposite;
import com.eiw.server.fleettrackingpu.Alertconfig;
import com.eiw.server.fleettrackingpu.Heartbeatevent;
import com.eiw.server.fleettrackingpu.Hourmeter;
import com.eiw.server.fleettrackingpu.Maintenancedue;
import com.eiw.server.fleettrackingpu.MaintenancedueId;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.VehicleHasIo;
import com.eiw.server.fleettrackingpu.Vehiclealerts;
import com.eiw.server.fleettrackingpu.Vehicleevent;

public class CheckScheduledMaintenanceDue implements CheckAlerts {

	AlertsEJBRemote alertsEJBRemote = null;
	AlertsManager alertsManager;
	Alertconfig alertConfig = null;
	Date lastUpdatedTime;
	// public static Map<String, String> Scheduled_Maintenance = new
	// HashMap<String, String>();
	public static Map<String, Integer> prevHour = new HashMap<String, Integer>();

	public CheckScheduledMaintenanceDue(AlertsManager alertsManager1) {
		alertsManager = alertsManager1;
		alertsEJBRemote = alertsManager.alertsEJBRemote;
	}

	public CheckScheduledMaintenanceDue(Alertconfig alertConfig,
			Date lastUpdatedTime) {
		this.alertConfig = alertConfig;
		this.lastUpdatedTime = lastUpdatedTime;
	}

	public void manageAlert(List<Vehicleevent> vehicleevents,
			Alertconfig alertConfig, String vin, String plateNo) {
		Vehicle vehicle = alertsEJBRemote.getVehicle(vin);
		String description = null;
		String type = vehicle.getVehicletype().getVehicleType();
		boolean checkEngineHour = false;
		Hourmeter hourmeter = null;
		int currentEngineRunHour = 0;
		List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();
		AlertsEJB.LOGGER.debug("Inside CheckScheduledMaintenanceDue Alert ->");
		for (Vehicleevent vehi : vehicleevents) {
			if (type.equalsIgnoreCase("DEEPSEA GENERATOR")) {
				checkEngineHour = vehi.getIoevent() != null
						&& !vehi.getIoevent().equalsIgnoreCase("{}");
			} else {
				hourmeter = alertsEJBRemote.getHourMeter(vin);
				if (hourmeter != null)
					checkEngineHour = true;
			}
			if (checkEngineHour) {
				// if (Scheduled_Maintenance.get(vin) == null) {
				// Scheduled_Maintenance.put(vin,
				// vehicle.getScheduledMaintenance());
				// }
				try {
					if (type.equalsIgnoreCase("DEEPSEA GENERATOR")) {
						JSONObject ioevent = new JSONObject(vehi.getIoevent());
						currentEngineRunHour = Integer.parseInt(ioevent.get(
								"engineRunTime").toString()) / 60;
					} else {
						currentEngineRunHour = Integer.parseInt(hourmeter
								.getTotRunningDuration());
					}
					// if (prevHour.get(vin) == null
					// || currentEngineRunHour > prevHour.get(vin)) {
					MaintenancedueId maintenancedueId = new MaintenancedueId();
					maintenancedueId.setVin(vin);
					maintenancedueId.setMaintenanceType("ScheduledMaintenance");
					Maintenancedue maintenancedue = alertsEJBRemote
							.getMaintanceDue(maintenancedueId);
					if (maintenancedue == null) {
						Maintenancedue maintenance = new Maintenancedue();
						maintenance.setId(maintenancedueId);
						maintenance.setLastMaintenanceHours(String
								.valueOf(currentEngineRunHour));
						maintenance.setTimeToNextMaintenance(vehicle
								.getScheduledMaintenance().split(",")[0]);
						maintenance.setLastUpdDt(vehi.getId()
								.getEventTimeStamp());
						alertsEJBRemote.updateMaintenanceDue(maintenance,
								"insert");
					} else {
						int nextMaintenanceHour = Integer.parseInt(vehicle
								.getScheduledMaintenance().split(",")[1])
								- (currentEngineRunHour - Integer
										.parseInt(maintenancedue
												.getLastMaintenanceHours()));
						maintenancedue.setTimeToNextMaintenance(String
								.valueOf(nextMaintenanceHour));
						maintenancedue.setLastUpdDt(vehi.getId()
								.getEventTimeStamp());
						alertsEJBRemote.updateMaintenanceDue(maintenancedue,
								"update");
						if (nextMaintenanceHour == Integer.parseInt(vehicle
								.getScheduledMaintenance().split(",")[0])) {
							description = vehicle.getVehicletype()
									+ " : "
									+ vehicle.getPlateNo()
									+ " Scheduled Maintenance Alert! Remaining Hours ="
									+ nextMaintenanceHour / 60;
							String mobile2 = null;
							mobile2 = alertConfig.getSmsNumber();
							Vehiclealerts va = new Vehiclealerts();
							va.setAlerttype("ScheduledMaintenance");
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
					// prevHour.put(vin, currentEngineRunHour);
					// }
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
		boolean checkEngineHour = false;
		Hourmeter hourmeter = null;
		int currentEngineRunHour = 0;
		List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();
		AlertsEJB.LOGGER.debug("Inside CheckScheduledMaintenanceDue Alert ->");
		for (Vehicleevent vehi : vehicleEvents) {
			if (type.equalsIgnoreCase("DEEPSEA GENERATOR")) {
				checkEngineHour = vehi.getIoevent() != null
						&& !vehi.getIoevent().equalsIgnoreCase("{}");
			} else {
				hourmeter = alertsEJBRemote.getHourMeter(vin);
				if (hourmeter != null)
					checkEngineHour = true;
			}
			if (checkEngineHour) {
				// if (Scheduled_Maintenance.get(vin) == null) {
				// String due = alertsEJBRemote.getPreferencesData(
				// "Scheduled_Maintenance", vin);
				// Scheduled_Maintenance.put(vin,
				// vehicle.getScheduledMaintenance());
				// }
				try {
					if (type.equalsIgnoreCase("DEEPSEA GENERATOR")) {
						JSONObject ioevent = new JSONObject(vehi.getIoevent());
						currentEngineRunHour = Integer.parseInt(ioevent.get(
								"engineRunTime").toString()) / 60;
					} else {
						currentEngineRunHour = Integer.parseInt(hourmeter
								.getTotRunningDuration());
					}
					// if (prevHour.get(vin) == null
					// || currentEngineRunHour > prevHour.get(vin)) {
					MaintenancedueId maintenancedueId = new MaintenancedueId();
					maintenancedueId.setVin(vin);
					maintenancedueId.setMaintenanceType("ScheduledMaintenance");
					Maintenancedue maintenancedue = alertsEJBRemote
							.getMaintanceDue(maintenancedueId);
					if (maintenancedue == null) {
						Maintenancedue maintenance = new Maintenancedue();
						maintenance.setId(maintenancedueId);
						maintenance.setLastMaintenanceHours(String
								.valueOf(currentEngineRunHour));
						maintenance.setTimeToNextMaintenance(vehicle
								.getScheduledMaintenance().split(",")[0]);
						maintenance.setLastUpdDt(vehi.getId()
								.getEventTimeStamp());
						alertsEJBRemote.updateMaintenanceDue(maintenance,
								"insert");
					} else {
						int nextMaintenanceHour = Integer.parseInt(vehicle
								.getScheduledMaintenance().split(",")[0])
								- (currentEngineRunHour - Integer
										.parseInt(maintenancedue
												.getLastMaintenanceHours()));
						maintenancedue.setTimeToNextMaintenance(String
								.valueOf(nextMaintenanceHour));
						maintenancedue.setLastUpdDt(vehi.getId()
								.getEventTimeStamp());
						alertsEJBRemote.updateMaintenanceDue(maintenancedue,
								"update");
						if (nextMaintenanceHour == Integer.parseInt(vehicle
								.getScheduledMaintenance().split(",")[1])) {
							description = vehicle.getVehicletype()
									.getVehicleType()
									+ " : "
									+ vehicle.getPlateNo()
									+ " Scheduled Maintenance Alert! Remaining Hours ="
									+ nextMaintenanceHour / 60;
							String mobile2 = null;
							mobile2 = alertConfig.getSmsNumber();
							Vehiclealerts va = new Vehiclealerts();
							va.setAlerttype("ScheduledMaintenance");
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
					// prevHour.put(vin, currentEngineRunHour);
					// }
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
