package com.eiw.alerts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import com.eiw.server.TimeZoneUtil;
import com.eiw.server.fleettrackingpu.Alertconfig;
import com.eiw.server.fleettrackingpu.VehicleHasIo;
import com.eiw.server.fleettrackingpu.Vehiclealerts;
import com.eiw.server.fleettrackingpu.Vehicleevent;

public class CheckOil {
	AlertsEJBRemote alertsEJBRemote = null;
	AlertsManager alertsManager;
	private static final String STR_OIL = "Oil Pressure";
	Date lastUpdatedTime;

	public CheckOil(AlertsManager alertsManager1) {
		alertsManager = alertsManager1;
		alertsEJBRemote = alertsManager.alertsEJBRemote;
	}

	public void manageAlert(List<Vehicleevent> vehicleevents,
			Alertconfig alertConfig, String vin, String plateNo,
			List<VehicleHasIo> vehicleHasIo,
			SortedMap<Integer, Boolean> minmaxValue) {
		List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();
		Boolean isMin = false, isMax = false;
		String[] range = null;
		int min = 0, max = 0, io = 0;
		String mobile2 = null;
		if (alertConfig.getAlertRange() != null) {
			range = alertConfig.getAlertRange().split("_");
			min = Integer.valueOf(range[0]);
			max = Integer.valueOf(range[1]);
		}

		for (VehicleHasIo hasIo : vehicleHasIo) {
			if (hasIo.getIoname().equalsIgnoreCase("OIL")) {
				io = hasIo.getId().getIo();
				break;
			}
		}

		for (Map.Entry<Integer, Boolean> e : minmaxValue.entrySet()) {
			if (e.getKey() == 1) {
				isMin = e.getValue();
			}
			if (e.getKey() == 2) {
				isMax = e.getValue();
			}
		}

		mobile2 = alertConfig.getSmsNumber();
		for (Vehicleevent ve : vehicleevents) {
			// Without Engine
			if ((true) && (io > 0)) {
				float ioVE = 0;
				if ((io == 9) && (ve.getAi1() != null)) {
					ioVE = alertsEJBRemote.getanalogReading(vin, io,
							ve.getAi1());
				} else if ((io == 10) && (ve.getAi2() != null)) {
					ioVE = alertsEJBRemote.getanalogReading(vin, io,
							ve.getAi2());
				} else if ((io == 11) && (ve.getAi3() != null)) {
					ioVE = alertsEJBRemote.getanalogReading(vin, io,
							ve.getAi3());
				} else if ((io == 19) && (ve.getAi4() != null)) {
					ioVE = alertsEJBRemote.getanalogReading(vin, io,
							ve.getAi4());
				}
				if (ioVE != 0) {
					if ((ioVE < min) && (isMin)) {
						String eventTime = TimeZoneUtil.getStrTZDateTime(ve
								.getId().getEventTimeStamp());
						String description = "Alert%0DType:Oil Low Level%0Dvehicle:"
								+ plateNo
								+ "%0DValue:"
								+ (int) ioVE
								+ "L%0DRange:"
								+ min
								+ "L-"
								+ max
								+ "L%0DTime:"
								+ eventTime;
						Vehiclealerts va = new Vehiclealerts();
						va.setAlerttype(STR_OIL);
						va.setDescription(description);
						va.setEventTimeStamp(ve.getId().getEventTimeStamp());
						va.setLatlng(ve.getLatitude() + "," + ve.getLongitude());
						va.setSmsmobile(mobile2);
						va.setVin(vin);
						va.setShowstatus(false);
						vehiclealerts.add(va);
					} else if ((ioVE > max) && (isMax)) {
						String eventTime = TimeZoneUtil.getStrTZDateTime(ve
								.getId().getEventTimeStamp());
						String description = "Alert%0DType:Oil High Level%0Dvehicle:"
								+ plateNo
								+ "%0DValue:"
								+ (int) ioVE
								+ "L%0DRange:"
								+ min
								+ "L-"
								+ max
								+ "L%0DTime:"
								+ eventTime;
						Vehiclealerts va = new Vehiclealerts();
						va.setAlerttype(STR_OIL);
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
}