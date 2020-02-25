package com.eiw.alerts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.eiw.server.TimeZoneUtil;
import com.eiw.server.fleettrackingpu.Alertconfig;
import com.eiw.server.fleettrackingpu.VehicleHasIo;
import com.eiw.server.fleettrackingpu.Vehiclealerts;
import com.eiw.server.fleettrackingpu.Vehicleevent;

public class CheckFuelLeakage {
	AlertsEJBRemote alertsEJBRemote = null;
	AlertsManager alertsManager;
	Date lastUpdatedTime;

	public CheckFuelLeakage(AlertsManager alertsManager1) {
		alertsManager = alertsManager1;
		alertsEJBRemote = alertsManager.alertsEJBRemote;
	}

	public void manageAlert(List<Vehicleevent> vehicleevents,
			Alertconfig alertConfig, String vin, String plateNo,
			List<VehicleHasIo> vehicleHasIo, Vehicleevent vehicleevent) {
		List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();
		String[] range = null;
		int preLit = 0, diffLit = 0;
		int litres = 0, minutes = 0, io = 0;
		String mobile2 = null;
		if (alertConfig.getAlertRange() != null) {
			range = alertConfig.getAlertRange().split("_");
			litres = Integer.valueOf(range[0]);
			minutes = Integer.valueOf(range[1]);
		}

		for (VehicleHasIo hasIo : vehicleHasIo) {
			if (hasIo.getIoname().equalsIgnoreCase("FUEL")) {
				io = hasIo.getId().getIo();
				break;
			}
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
				int ioVE = 0;
				if ((io == 9) && (ve.getAi1() != null)
						&& (vehicleevent.getAi1() != null)) {
					ioVE = ve.getAi1();
					preLit = vehicleevent.getAi1();
				} else if ((io == 10) && (ve.getAi2() != null)
						& (vehicleevent.getAi2() != null)) {
					ioVE = ve.getAi2();
					preLit = vehicleevent.getAi2();
				} else if ((io == 11) && (ve.getAi3() != null)
						&& (vehicleevent.getAi3() != null)) {
					ioVE = ve.getAi3();
					preLit = vehicleevent.getAi3();
				} else if ((io == 19) && (ve.getAi4() != null)
						&& (vehicleevent.getAi4() != null)) {
					ioVE = ve.getAi4();
					preLit = vehicleevent.getAi4();
				}
				if (ioVE != 0 && preLit != 0) {
					diffLit = preLit - ioVE;
					if ((diff <= (minutes * 60)) && (diffLit >= litres)) {
						String eventTime = TimeZoneUtil.getStrTZDateTime(ve
								.getId().getEventTimeStamp());
						String description = "Alert%0DType : Fuel Leakage%0Dvehicle:"
								+ plateNo
								+ "%0DValue:"
								+ ioVE
								+ "%0DRange:"
								+ litres
								+ " L in "
								+ minutes
								+ "min%0DTime:"
								+ eventTime;
						Vehiclealerts va = new Vehiclealerts();
						va.setAlerttype("Fuel Leakage");
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
}