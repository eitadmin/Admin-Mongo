package com.eiw.alerts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.eiw.device.ejb.VehicleComposite;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.fleettrackingpu.Alertconfig;
import com.eiw.server.fleettrackingpu.Heartbeatevent;
import com.eiw.server.fleettrackingpu.VehicleHasIo;
import com.eiw.server.fleettrackingpu.Vehiclealerts;
import com.eiw.server.fleettrackingpu.Vehicleevent;

public class CheckStop implements CheckAlerts {
	AlertsEJBRemote alertsEJBRemote = null;

	AlertsManager alertsManager;
	List<Vehicleevent> listOfEvent;
	Alertconfig alertConfig = null;
	Date lastUpdatedTime;

	public CheckStop(AlertsManager alertsManager2) {
		alertsManager = alertsManager2;
		alertsEJBRemote = alertsManager.alertsEJBRemote;

	}

	public CheckStop(Alertconfig alertConfig, Date lastUpdatedTime) {
		this.alertConfig = alertConfig;
		this.lastUpdatedTime = lastUpdatedTime;

	}

	public void manageAlert(List<Vehicleevent> listOfEvent1,
			Alertconfig alertConfig, String vin, String plateNo) {
		listOfEvent = listOfEvent1;
		List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();
		String range = null, mobile = null;
		int stopInterval = 0;
		// alert config
		range = alertConfig.getAlertRange();
		stopInterval = Integer.valueOf(range);
		mobile = alertConfig.getSmsNumber();

		List<Date> dates = new ArrayList<Date>();
		for (Vehicleevent evt : listOfEvent) {
			dates.add(evt.getId().getEventTimeStamp());
		}
		Collections.sort(dates);

		// If engin ON and Speed zero
		AlertsEJB.LOGGER.debug("Inside CheckStop Alert ->");

		// Take the first Vehicleevent[0] and Chk for last 20 mins
		for (Date date : dates) {
			try {
				Vehicleevent ve = getVE(date);
				// No engine wire connected
				if (ve.getEngine() == null) {
					return;
				}
				// Engine is OFF
				if (!ve.getEngine()) {
					return;
				}
				// Speed is not zero means Running
				if ((ve.getSpeed() != null) && (ve.getSpeed() != 0)) {
					return;
				}

				List<Vehicleevent> vehicleevents = (List<Vehicleevent>) alertsEJBRemote
						.getStopVehicleevents(ve);
				String eventTime = TimeZoneUtil.getTimeINYYYYMMddss(ve.getId()
						.getEventTimeStamp());
				long curTs = ve.getId().getEventTimeStamp().getTime();
				for (Vehicleevent velocal : vehicleevents) {
					int speed = velocal.getSpeed();
					boolean engine = velocal.getEngine();
					long evtTs = velocal.getId().getEventTimeStamp().getTime();
					long dateDiff = (curTs - evtTs) / 1000;
					if ((speed == 0) && (engine)) {
						if (dateDiff > stopInterval) {
							String description = "Alert%0DType : Engine Stop Time%0Dvehicle:"
									+ plateNo
									+ "%0DLimit:"
									+ getSecInMin(stopInterval)
									+ "min %0DTime:" + eventTime;

							Vehiclealerts va = new Vehiclealerts();
							va.setAlerttype(AlertsManager.enumAlerts.STOP
									.name());
							va.setDescription(description);
							va.setEventTimeStamp(ve.getId().getEventTimeStamp());
							va.setLatlng(ve.getLatitude() + ","
									+ ve.getLongitude());
							va.setSmsmobile(mobile);
							va.setVin(vin);
							va.setShowstatus(false);
							vehiclealerts.add(va);
							break;
						}
					} else {
						return;
					}
				}
				break;

			} catch (Exception e) {

			}

		}
		if (!vehiclealerts.isEmpty()) {
			lastUpdatedTime = alertsManager.persistVehicleAlert(alertConfig,
					vehiclealerts, lastUpdatedTime);
		}
	}

	private Vehicleevent getVE(Date date) {
		for (Vehicleevent ve : listOfEvent) {
			long a = ve.getId().getEventTimeStamp().getTime();
			long b = date.getTime();
			if (a == b) {
				return ve;
			}
		}
		return null;
	}

	private int getSecInMin(int sec) {
		return sec / 60;
	}

	@Override
	public String manageAlerts(List<Vehicleevent> vehicleEvents, String vin,
			String plateNo, List<VehicleHasIo> vehicleHasIo, int id,
			VehicleComposite vehicleComposite) {

		listOfEvent = vehicleEvents;
		List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();
		String range = null, mobile = null;
		int stopInterval = 0;
		// alert config
		range = alertConfig.getAlertRange();
		stopInterval = Integer.valueOf(range);
		mobile = alertConfig.getSmsNumber();

		List<Date> dates = new ArrayList<Date>();
		for (Vehicleevent evt : listOfEvent) {
			dates.add(evt.getId().getEventTimeStamp());
		}
		Collections.sort(dates);

		// If engin ON and Speed zero
		AlertsEJB.LOGGER.debug("Inside CheckStop Alert ->");

		// Take the first Vehicleevent[0] and Chk for last 20 mins
		for (Date date : dates) {
			try {
				Vehicleevent ve = getVE(date);
				// No engine wire connected
				if (ve.getEngine() == null) {
					return null;
				}
				// Engine is OFF
				if (ve.getEngine()) {
					return null;
				}
				// Speed is not zero means Running
				if ((ve.getSpeed() != null) && (ve.getSpeed() != 0)) {
					return null;
				}

				List<Vehicleevent> vehicleevents = (List<Vehicleevent>) alertsEJBRemote
						.getStopVehicleevents(ve);
				String eventTime = TimeZoneUtil.getTimeINYYYYMMddss(ve.getId()
						.getEventTimeStamp());
				long curTs = ve.getId().getEventTimeStamp().getTime();
				for (Vehicleevent velocal : vehicleevents) {
					int speed = velocal.getSpeed();
					boolean engine = velocal.getEngine();
					long evtTs = velocal.getId().getEventTimeStamp().getTime();
					long dateDiff = (curTs - evtTs) / 1000;
					if ((speed == 0) && (engine == false)) {
						if (dateDiff > stopInterval) {
							String description = "Alert%0DType : Engine Stop Time exceeds vehicle:"
									+ plateNo
									+ " %0DLimit: "
									+ getSecInMin(stopInterval)
									+ "min %0DTime: " + eventTime;

							Vehiclealerts va = new Vehiclealerts();
							va.setAlerttype(AlertsManager.enumAlerts.STOP
									.name());
							va.setDescription(description);
							va.setEventTimeStamp(ve.getId().getEventTimeStamp());
							va.setLatlng(ve.getLatitude() + ","
									+ ve.getLongitude());
							va.setSmsmobile(mobile);
							va.setVin(vin);
							va.setShowstatus(false);
							vehiclealerts.add(va);
							break;
						}
					} else {
						return null;
					}
				}
				break;

			} catch (Exception e) {

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

}
