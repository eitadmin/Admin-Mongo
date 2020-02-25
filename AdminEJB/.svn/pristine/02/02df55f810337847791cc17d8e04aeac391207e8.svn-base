package com.eiw.alerts;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import com.eiw.device.ejb.VehicleComposite;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.fleettrackingpu.Alertconfig;
import com.eiw.server.fleettrackingpu.Heartbeatevent;
import com.eiw.server.fleettrackingpu.VehicleHasIo;
import com.eiw.server.fleettrackingpu.Vehiclealerts;
import com.eiw.server.fleettrackingpu.Vehicleevent;

public class CheckFuelAlert implements CheckAlerts {

	public int litres = 0;
	AlertsEJBRemote alertsEJBRemote = null;
	AlertsManager alertsManager;
	String unit, alerttypename;
	Alertconfig alertConfig = null;
	Date lastUpdatedTime;
	public List<Float> fuelLiters = new ArrayList<Float>();
	int millivolts = 0;
	float diffLit = 0.0F;
	float[] fuelvalues;
	float initLiter, endLiter;

	public CheckFuelAlert(AlertsManager alertsManager1) {
		alertsManager = alertsManager1;
		alertsEJBRemote = alertsManager.alertsEJBRemote;
	}

	public CheckFuelAlert(Alertconfig alertConfig, Date lastUpdatedTime) {
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
		}

		// io = 9;

		for (VehicleHasIo hasIo : vehicleHasIo) {
			io = hasIo.getId().getIo();
			unit = hasIo.getUnit();
			alerttypename = hasIo.getIoname();
			break;
		}

		mobile2 = alertConfig.getSmsNumber();
		for (Vehicleevent ve : vehicleevents) {
			// int speed = 0;
			int speed = ve.getSpeed();
			boolean engine = ve.getEngine();

			// Without Engine

			if ((engine) && (speed == 0)) {
				float ioVE = 0;
				if ((io == 9) && (ve.getAi1() != null)
						&& (vehicleevent.getAi1() != null)) {
					ioVE = alertsEJBRemote.getanalogReading(vin, io,
							ve.getAi1());

					preLit = alertsEJBRemote.getanalogReading(vin, io,
							vehicleevent.getAi1());

					// preLit = alertsEJBRemote.getmaxanalogReading(vin, io,
					// engine);
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
					if ((diffLit >= 5)) {
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

	public static String getTimeINYYYYMMddss(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(date);
	}

	public float getNearByone(float[] data) {
		TreeMap<Float, Float> finalData = new TreeMap<Float, Float>();
		for (int i = 0; i < data.length; i++) {
			if (i == data.length - 1) {
				finalData.put(Math.abs(data[i] - data[0]),
						(data[i] + data[0]) / 2);
			} else {
				finalData.put(Math.abs(data[i] - data[i + 1]),
						(data[i] + data[i + 1]) / 2);
			}
		}

		return finalData.get(finalData.firstKey());
	}

	@Override
	public String manageAlerts(List<Vehicleevent> vehicleEvents, String vin,
			String plateNo, List<VehicleHasIo> vehicleHasIo, int id,
			VehicleComposite vehicleComposite) {

		List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();
		String[] range = null;
		NumberFormat nmbrformat = new DecimalFormat("#0.00");
		int io = 0;
		String mobile2 = null;
		if (alertConfig.getAlertRange() != null) {
			range = alertConfig.getAlertRange().split("_");
			litres = Integer.valueOf(range[0]);
		}

		for (VehicleHasIo hasIo : vehicleHasIo) {
			io = hasIo.getId().getIo();
			unit = hasIo.getUnit();
			alerttypename = hasIo.getIoname();
			break;
		}
		int minutes1 = Integer.valueOf(range[0]);
		mobile2 = alertConfig.getSmsNumber();
		for (Vehicleevent ve : vehicleEvents) {
			String StrEventTimeStamp = TimeZoneUtil.getTimeINYYYYMMddss(ve
					.getId().getEventTimeStamp());
			int speed = ve.getSpeed();
			if (speed == 0) {
				if (fuelLiters.size() <= 9) {
					switch (io) {
					case 9:
						millivolts = ve.getAi1();
						break;
					case 10:
						millivolts = ve.getAi2();
						break;
					case 11:
						millivolts = ve.getAi3();
						break;
					case 19:
						millivolts = ve.getAi4();
						break;
					}
					fuelLiters.add(alertsEJBRemote.getanalogReading(vin, io,
							millivolts));
				}

				if (fuelLiters.size() == 10) {
					float[] initValues = { fuelLiters.get(1),
							fuelLiters.get(2), fuelLiters.get(3) };
					initLiter = getNearByone(initValues);
					float[] endValues = { fuelLiters.get(7), fuelLiters.get(8),
							fuelLiters.get(9) };
					endLiter = getNearByone(endValues);
					diffLit = endLiter - initLiter;
					if (diffLit >= litres) {
						String eventTime = TimeZoneUtil.getStrTZDateTime(ve
								.getId().getEventTimeStamp());
						String description = "Alert%0DType :" + alerttypename
								+ " Leakage%0Dvehicle:" + plateNo + "%0DValue:"
								+ nmbrformat.format(Math.abs(diffLit)) + " "
								+ unit + "%0DRange:" + litres + unit
								+ " %0DTime:" + eventTime;
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
					diffLit = 0;
					fuelLiters.clear();
				}
			} else {
				fuelLiters.clear();
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