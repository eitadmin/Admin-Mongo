package com.eiw.alerts;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
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

public class CheckAnalogInputLeakage implements CheckAlerts {
	AlertsEJBRemote alertsEJBRemote = null;
	AlertsManager alertsManager;
	String unit, alerttypename;
	Alertconfig alertConfig = null;
	Date lastUpdatedTime;

	public CheckAnalogInputLeakage(AlertsManager alertsManager1) {
		alertsManager = alertsManager1;
		alertsEJBRemote = alertsManager.alertsEJBRemote;
	}

	public CheckAnalogInputLeakage(Alertconfig alertConfig, Date lastUpdatedTime) {
		this.alertConfig = alertConfig;
		this.lastUpdatedTime = lastUpdatedTime;
	}

	public void manageAlert(List<Vehicleevent> vehicleevents,
			Alertconfig alertConfig, String vin, String plateNo,
			List<VehicleHasIo> vehicleHasIo, Vehicleevent vehicleevent) {
		List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();
		String[] range = null;
		NumberFormat nmbrformat = new DecimalFormat("#0.00");
		float preLit = 0, diffLit = 0;
		int litres = 0, minutes = 0, io = 0;
		String mobile2 = null;
		if (alertConfig.getAlertRange() != null) {
			range = alertConfig.getAlertRange().split("_");
			litres = Integer.valueOf(range[0]);
			minutes = Integer.valueOf(range[1]);
		}

		for (VehicleHasIo hasIo : vehicleHasIo) {
			io = hasIo.getId().getIo();
			unit = hasIo.getUnit();
			alerttypename = hasIo.getIoname();
			break;
		}
		int hr = addy(getTimeINYYYYMMddss(vehicleevent.getId()
				.getEventTimeStamp()));
		mobile2 = alertConfig.getSmsNumber();
		for (Vehicleevent ve : vehicleevents) {
			int curVal = addy(getTimeINYYYYMMddss(ve.getId()
					.getEventTimeStamp()));
			int diff = curVal - hr;
			// Without Engine
			if ((true) && (io > 0)) {
				float ioVE = 0;
				if ((io == 9) && (ve.getAi1() != null)
						&& (vehicleevent.getAi1() != null)) {
					ioVE = alertsEJBRemote.getanalogReading(vin, io,
							ve.getAi1());
					preLit = alertsEJBRemote.getanalogReading(vin, io,
							vehicleevent.getAi1());
				} else if ((io == 10) && (ve.getAi2() != null)
						& (vehicleevent.getAi2() != null)) {
					ioVE = alertsEJBRemote.getanalogReading(vin, io,
							ve.getAi2());
					preLit = alertsEJBRemote.getanalogReading(vin, io,
							vehicleevent.getAi2());
				} else if ((io == 11) && (ve.getAi3() != null)
						&& (vehicleevent.getAi3() != null)) {
					ioVE = alertsEJBRemote.getanalogReading(vin, io,
							ve.getAi3());
					preLit = alertsEJBRemote.getanalogReading(vin, io,
							vehicleevent.getAi3());
				} else if ((io == 19) && (ve.getAi4() != null)
						&& (vehicleevent.getAi4() != null)) {
					ioVE = alertsEJBRemote.getanalogReading(vin, io,
							ve.getAi4());
					preLit = alertsEJBRemote.getanalogReading(vin, io,
							vehicleevent.getAi4());
				}
				if (ioVE != 0 && preLit != 0) {
					diffLit = preLit - ioVE;
					if ((diff <= (minutes * 60)) && (diffLit >= litres)) {
						String eventTime = TimeZoneUtil.getStrTZDateTime(ve
								.getId().getEventTimeStamp());
						String description = "Alert%0DType :" + alerttypename
								+ " Leakage%0Dvehicle:" + plateNo + "%0DValue:"
								+ nmbrformat.format(diffLit) + " " + unit
								+ "%0DRange:" + litres + unit + " in "
								+ minutes + " min%0DTime:" + eventTime;
						Vehiclealerts va = new Vehiclealerts();
						va.setAlerttype(alertConfig.getId().getAlertType());
						va.setDescription(description);
						va.setEventTimeStamp(ve.getId().getEventTimeStamp());
						va.setLatlng(ve.getLatitude() + "," + ve.getLongitude());
						va.setSmsmobile(mobile2);
						va.setVin(vin);
						va.setShowstatus(false);
						vehiclealerts.add(va);
					}
				}
			}
		}
		if (!vehiclealerts.isEmpty()) {
			lastUpdatedTime = alertsManager.persistVehicleAlert(alertConfig,
					vehiclealerts, lastUpdatedTime);
		}
	}

	private int addy(String durationString) {
		int hours;
		int minutes;
		int seconds;
		String[] durationArray = durationString.split(":");
		hours = (Integer.valueOf(durationArray[0])) * 3600;
		minutes = (Integer.valueOf(durationArray[1])) * 60;
		seconds = Integer.valueOf(durationArray[2]);
		return hours + minutes + seconds;
	}

	public static String getTimeINYYYYMMddss(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(date);
	}

	@Override
	public String manageAlerts(List<Vehicleevent> vehicleEvents, String vin,
			String plateNo, List<VehicleHasIo> vehicleHasIo, int id,
			VehicleComposite vehicleComposite) {

		List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();
		String[] range = null;
		NumberFormat nmbrformat = new DecimalFormat("#0.00");
		float preLit = 0, diffLit = 0;
		int litres = 0, minutes = 0, io = 0;
		String mobile2 = null;
		if (alertConfig.getAlertRange() != null) {
			range = alertConfig.getAlertRange().split("_");
			litres = Integer.valueOf(range[0]);
			minutes = Integer.valueOf(range[1]);
		}

		for (VehicleHasIo hasIo : vehicleHasIo) {
			io = hasIo.getId().getIo();
			unit = hasIo.getUnit();
			alerttypename = hasIo.getIoname();
			break;
		}
		int minutes1 = Integer.valueOf(range[1]);
		Vehicleevent vehicleevent = alertsEJBRemote.getLastVEData(
				vin,
				TimeZoneUtil.getTimeINYYYYMMddssa(vehicleEvents.get(0).getId()
						.getEventTimeStamp()), minutes1);
		if (vehicleevent == null) {
			return null;
		}

		int hr = addy(getTimeINYYYYMMddss(vehicleevent.getId()
				.getEventTimeStamp()));
		mobile2 = alertConfig.getSmsNumber();
		for (Vehicleevent ve : vehicleEvents) {
			int curVal = addy(getTimeINYYYYMMddss(ve.getId()
					.getEventTimeStamp()));
			int diff = curVal - hr;
			// Without Engine
			if ((true) && (io > 0)) {
				float ioVE = 0;
				if ((io == 9) && (ve.getAi1() != null)
						&& (vehicleevent.getAi1() != null)) {
					ioVE = alertsEJBRemote.getanalogReading(vin, io,
							ve.getAi1());
					preLit = alertsEJBRemote.getanalogReading(vin, io,
							vehicleevent.getAi1());
				} else if ((io == 10) && (ve.getAi2() != null)
						& (vehicleevent.getAi2() != null)) {
					ioVE = alertsEJBRemote.getanalogReading(vin, io,
							ve.getAi2());
					preLit = alertsEJBRemote.getanalogReading(vin, io,
							vehicleevent.getAi2());
				} else if ((io == 11) && (ve.getAi3() != null)
						&& (vehicleevent.getAi3() != null)) {
					ioVE = alertsEJBRemote.getanalogReading(vin, io,
							ve.getAi3());
					preLit = alertsEJBRemote.getanalogReading(vin, io,
							vehicleevent.getAi3());
				} else if ((io == 19) && (ve.getAi4() != null)
						&& (vehicleevent.getAi4() != null)) {
					ioVE = alertsEJBRemote.getanalogReading(vin, io,
							ve.getAi4());
					preLit = alertsEJBRemote.getanalogReading(vin, io,
							vehicleevent.getAi4());
				}
				if (ioVE != 0 && preLit != 0) {
					diffLit = preLit - ioVE;
					if ((diff <= (minutes * 60)) && (diffLit >= litres)) {
						String eventTime = TimeZoneUtil.getStrTZDateTime(ve
								.getId().getEventTimeStamp());
						String description = "Alert%0DType :" + alerttypename
								+ " Leakage%0Dvehicle:" + plateNo + "%0DValue:"
								+ nmbrformat.format(diffLit) + " " + unit
								+ "%0DRange:" + litres + unit + " in "
								+ minutes + " min%0DTime:" + eventTime;
						Vehiclealerts va = new Vehiclealerts();
						va.setAlerttype(alertConfig.getId().getAlertType());
						va.setDescription(description);
						va.setEventTimeStamp(ve.getId().getEventTimeStamp());
						va.setLatlng(ve.getLatitude() + "," + ve.getLongitude());
						va.setSmsmobile(mobile2);
						va.setVin(vin);
						va.setShowstatus(false);
						vehiclealerts.add(va);
					}
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