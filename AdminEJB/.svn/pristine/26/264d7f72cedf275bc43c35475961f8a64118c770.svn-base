package com.eiw.alerts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.eiw.device.ejb.VehicleComposite;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.fleettrackingpu.Alertconfig;
import com.eiw.server.fleettrackingpu.Heartbeatevent;
import com.eiw.server.fleettrackingpu.VehicleHasIo;
import com.eiw.server.fleettrackingpu.Vehiclealerts;
import com.eiw.server.fleettrackingpu.Vehicleevent;

public class CheckEngineStatusChange implements CheckAlerts {
	AlertsEJBRemote alertsEJBRemote = null;
	AlertsManager alertsManager;
	Alertconfig alertConfig = null;
	Date lastUpdatedTime;

	public CheckEngineStatusChange(AlertsManager alertsManager1) {
		alertsManager = alertsManager1;
		alertsEJBRemote = alertsManager.alertsEJBRemote;
	}

	public CheckEngineStatusChange(Alertconfig alertConfig, Date lastUpdatedTime) {
		this.alertConfig = alertConfig;
		this.lastUpdatedTime = lastUpdatedTime;

	}

	public void manageAlert(List<Vehicleevent> vehicleevents,
			Alertconfig alertConfig, String vin, String plateNo) {
		List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();

		String mobile2 = null;
		mobile2 = alertConfig.getSmsNumber();
		AlertsEJB.LOGGER.debug("Inside CheckEngineStatusChange Alert ->");
		Vehicleevent prevVe = alertsEJBRemote.getPreviousVE(vin, null);
		boolean prevEngine = prevVe.getEngine();
		for (Vehicleevent ve : vehicleevents) {
			boolean engine = ve.getEngine();
			String deviceData = ve.getTags();
			String packetType = "";
			if (deviceData.startsWith("{")) {
				JSONObject obj;

				try {
					obj = new JSONObject(deviceData);
					if (obj.has("packetType")) {

						packetType = obj.getString("packetType");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			if ((!prevEngine && engine) || packetType.equalsIgnoreCase("IN")) {
				String eventTime = TimeZoneUtil.getStrTZDateTime(ve.getId()
						.getEventTimeStamp());
				String description = "Alert%0DType : Engine Turned ON%0Dvehicle:"
						+ plateNo + "%0DTime:" + eventTime;
				Vehiclealerts va = new Vehiclealerts();
				va.setAlerttype("ENGINESTATUS");
				va.setDescription(description);
				va.setEventTimeStamp(ve.getId().getEventTimeStamp());
				va.setLatlng(ve.getLatitude() + "," + ve.getLongitude());
				va.setSmsmobile(mobile2);
				va.setVin(vin);
				va.setShowstatus(false);
				vehiclealerts.add(va);
				prevEngine = engine;
			} else if ((prevEngine && !engine)
					|| packetType.equalsIgnoreCase("IF")) {
				String eventTime = TimeZoneUtil.getStrTZDateTime(ve.getId()
						.getEventTimeStamp());
				String description = "Alert%0DType : Engine Turned OFF%0Dvehicle:"
						+ plateNo + "%0DTime:" + eventTime;
				Vehiclealerts va = new Vehiclealerts();
				va.setAlerttype("ENGINESTATUS");
				va.setDescription(description);
				va.setEventTimeStamp(ve.getId().getEventTimeStamp());
				va.setLatlng(ve.getLatitude() + "," + ve.getLongitude());
				va.setSmsmobile(mobile2);
				va.setVin(vin);
				va.setShowstatus(false);
				vehiclealerts.add(va);
				prevEngine = engine;
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
		AlertsEJB.LOGGER.debug("Inside CheckEngineStatusChange Alert ->");
		Vehicleevent prevVe = alertsEJBRemote.getPreviousVE(vin, null);
		boolean prevEngine = prevVe.getEngine();
		if (alertConfig.getSubAlertType() == null) {
			alertConfig.setSubAlertType("0");
		}
		for (Vehicleevent ve : vehicleEvents) {
			boolean engine = ve.getEngine();
			String deviceData = ve.getTags();
			String packetType = "";
			if (deviceData.startsWith("{")) {
				JSONObject obj;

				try {
					obj = new JSONObject(deviceData);
					if (obj.has("packetType")) {

						packetType = obj.getString("packetType");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			if ((!prevEngine && engine) || packetType.equalsIgnoreCase("IN")) {
				if (alertConfig.getSubAlertType().equalsIgnoreCase("1")
						|| alertConfig.getSubAlertType().equalsIgnoreCase("0")) {
					String eventTime = TimeZoneUtil.getStrTZDateTime(ve.getId()
							.getEventTimeStamp());
					String description = "Alert%0DType : Engine Turned ON%0Dvehicle:"
							+ plateNo + "%0DTime:" + eventTime;
					Vehiclealerts va = new Vehiclealerts();
					va.setAlerttype("ENGINESTATUS");
					va.setDescription(description);
					va.setEventTimeStamp(ve.getId().getEventTimeStamp());
					va.setLatlng(ve.getLatitude() + "," + ve.getLongitude());
					va.setSmsmobile(mobile2);
					va.setVin(vin);
					va.setShowstatus(false);
					vehiclealerts.add(va);
					prevEngine = engine;
				}
			} else if ((prevEngine && !engine)
					|| packetType.equalsIgnoreCase("IF")) {
				if (alertConfig.getSubAlertType().equalsIgnoreCase("2")
						|| alertConfig.getSubAlertType().equalsIgnoreCase("0")) {
					String eventTime = TimeZoneUtil.getStrTZDateTime(ve.getId()
							.getEventTimeStamp());
					String description = "Alert%0DType : Engine Turned OFF%0Dvehicle:"
							+ plateNo + "%0DTime:" + eventTime;
					Vehiclealerts va = new Vehiclealerts();
					va.setAlerttype("ENGINESTATUS");
					va.setDescription(description);
					va.setEventTimeStamp(ve.getId().getEventTimeStamp());
					va.setLatlng(ve.getLatitude() + "," + ve.getLongitude());
					va.setSmsmobile(mobile2);
					va.setVin(vin);
					va.setShowstatus(false);
					vehiclealerts.add(va);
					prevEngine = engine;
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
			String plateNo, VehicleComposite vehicleComposite) {
		List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();
		String mobile2 = null;
		mobile2 = alertConfig.getSmsNumber();
		AlertsEJB.LOGGER
				.info("Inside CheckEngineStatusChange manageHbAlerts ->");
		Heartbeatevent prevHb = alertsEJBRemote.getPrevHeartbeatEvent(vin,
				heartbeatevent.getId().getTimeStamp());
		boolean prevEngine = prevHb.getEngine();
		if (alertConfig.getSubAlertType() == null) {
			alertConfig.setSubAlertType("0");
		}
		boolean engine = heartbeatevent.getEngine();
		if (!prevEngine && engine) {
			if (alertConfig.getSubAlertType().equalsIgnoreCase("1")
					|| alertConfig.getSubAlertType().equalsIgnoreCase("0")) {
				String eventTime = TimeZoneUtil.getStrTZDateTime(heartbeatevent
						.getId().getTimeStamp());
				String description = "Alert%0DType : Engine Turned ON%0Dvehicle:"
						+ plateNo + "%0DTime:" + eventTime;
				Vehiclealerts va = new Vehiclealerts();
				va.setAlerttype(AlertsManager.enumAlerts.ENGINESTATUS.name());
				va.setDescription(description);
				va.setEventTimeStamp(heartbeatevent.getId().getTimeStamp());
				va.setSmsmobile(mobile2);
				va.setVin(vin);
				va.setShowstatus(false);
				vehiclealerts.add(va);
				prevEngine = engine;
			}
		} else if (prevEngine && !engine) {
			if (alertConfig.getSubAlertType().equalsIgnoreCase("2")
					|| alertConfig.getSubAlertType().equalsIgnoreCase("0")) {
				String eventTime = TimeZoneUtil.getStrTZDateTime(heartbeatevent
						.getId().getTimeStamp());
				String description = "Alert%0DType : Engine Turned OFF%0Dvehicle:"
						+ plateNo + "%0DTime:" + eventTime;
				Vehiclealerts va = new Vehiclealerts();
				va.setAlerttype(AlertsManager.enumAlerts.ENGINESTATUS.name());
				va.setDescription(description);
				va.setEventTimeStamp(heartbeatevent.getId().getTimeStamp());
				va.setSmsmobile(mobile2);
				va.setVin(vin);
				va.setShowstatus(false);
				vehiclealerts.add(va);
				prevEngine = engine;
			}
		}
		if (!vehiclealerts.isEmpty()) {
			lastUpdatedTime = alertsManager.persistHbAlert(alertConfig,
					vehiclealerts, lastUpdatedTime);
		}
		return null;
	}
}