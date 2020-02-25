package com.eiw.alerts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eiw.cron.AlertConfigEJB;
import com.eiw.device.ejb.VehicleComposite;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.fleettrackingpu.Alertconfig;
import com.eiw.server.fleettrackingpu.Heartbeatevent;
import com.eiw.server.fleettrackingpu.VehicleHasIo;
import com.eiw.server.fleettrackingpu.Vehiclealerts;
import com.eiw.server.fleettrackingpu.Vehicleevent;

public class CheckSeatBeltAlert implements CheckAlerts {
	private static final String DATE_DDHHMMSS = "yyyy-MM-dd HH:mm:ss";
	SimpleDateFormat sdfTime = new SimpleDateFormat(DATE_DDHHMMSS);
	public static Map<String, List<Vehicleevent>> seatBeltVehicleeventMap = new HashMap<String, List<Vehicleevent>>();
	AlertsManager alertsManager;
	AlertsEJBRemote alertsEJBRemote = null;
	List<Vehicleevent> listOfSeatBeltEvent = new ArrayList<Vehicleevent>();
	Alertconfig alertConfig = null;
	Date lastUpdatedTime;
	boolean firstEnter = false;

	public CheckSeatBeltAlert(AlertsManager alertsManager1) {
		alertsManager = alertsManager1;
		alertsEJBRemote = alertsManager.alertsEJBRemote;
	}

	public CheckSeatBeltAlert(Alertconfig alertConfig, Date lastUpdatedTime) {
		this.alertConfig = alertConfig;
		this.lastUpdatedTime = lastUpdatedTime;
	}

	@Override
	public String manageAlerts(List<Vehicleevent> vehicleEvents, String vin,
			String plateNo, List<VehicleHasIo> vehicleHasIo, int id,
			VehicleComposite vehicleComposite) {
		String range = null, mobile = null, description = null;
		boolean flag = false;
		int io = 0, diVal = 0;
		long secBetTime = 0, seatSecVal = 0, seatSpeedVal = 0;
		String seatVal = "", compSetVal = "";
		List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();
		mobile = alertConfig.getSmsNumber();
		range = alertConfig.getAlertRange();

		if (!AlertConfigEJB.secInIgnoremapforSeatbelt.isEmpty()
				&& AlertConfigEJB.secInIgnoremapforSeatbelt
						.get(vehicleComposite.getVehicle().getCompanyId()) != null) {
			seatVal = AlertConfigEJB.secInIgnoremapforSeatbelt
					.get(vehicleComposite.getVehicle().getCompanyId());
		} else {
			seatVal = alertsEJBRemote.getPreferencesData(
					"secondsInIgnoreForSeatBelt", vehicleComposite.getVehicle()
							.getCompanyId());
			AlertConfigEJB.secInIgnoremapforSeatbelt.put(vehicleComposite
					.getVehicle().getCompanyId(), seatVal);
		}

		for (VehicleHasIo hasIo : vehicleHasIo) {
			if (hasIo.getIoname().equalsIgnoreCase("SeatBelt")) {
				io = hasIo.getId().getIo();
				flag = true;
				break;
			}
		}
		if (flag) {
			for (Vehicleevent e : vehicleEvents) {

				switch (io) {
				case 2:
					if (e.getDi2() != null) {
						diVal = e.getDi2();
						break;
					}
				case 3:
					if (e.getDi3() != null) {
						diVal = e.getDi3();
						break;
					}
				case 4:
					if (e.getDi4() != null) {
						diVal = e.getDi4();
						break;
					}
				}

				Date speedTimeStamp = e.getId().getEventTimeStamp();
				long timdiff = 0;
				if (AlertConfigEJB.seatBeltTimeCalc.get(vin) != null) {
					long seatCurrVal = getConvertSec(TimeZoneUtil
							.getTimeINYYYYMMddss(speedTimeStamp));
					long seatPreVal = AlertConfigEJB.seatBeltTimeCalc.get(vin);
					timdiff = seatCurrVal - seatPreVal;

				} else {
					AlertConfigEJB.seatBeltTimeCalc.put(vin,
							getConvertSec(TimeZoneUtil
									.getTimeINYYYYMMddss(speedTimeStamp)));
				}

				if (diVal == 0 && Integer.parseInt(range) < e.getSpeed()
						&& e.getEngine() && timdiff > Integer.parseInt(seatVal)) {

					AlertConfigEJB.seatBeltTimeCalc.remove(vin);
					description = "SeatBeltAlert: Vehicle :"
							+ plateNo
							+ " speed is "
							+ e.getSpeed()
							+ "km  "
							+ e.getLatitude()
							+ ","
							+ e.getLongitude()
							+ "  at "
							+ TimeZoneUtil.getStrTZDateTime(e.getId()
									.getEventTimeStamp()) + "=" + e.getSpeed()
							+ "=";

					Vehiclealerts va = new Vehiclealerts();
					va.setAlerttype("SEATBELT");
					va.setDescription(description);
					va.setEventTimeStamp(e.getId().getEventTimeStamp());
					va.setLatlng(e.getLatitude() + "," + e.getLongitude());
					va.setSmsmobile(mobile);
					va.setVin(vin);
					va.setShowstatus(false);
					vehiclealerts.add(va);
					if (!vehiclealerts.isEmpty()) {
						lastUpdatedTime = alertsManager.persistVehicleAlert(
								alertConfig, vehiclealerts, lastUpdatedTime);
					}

				}
			}
			return "success";
		}
		return null;
	}

	@Override
	public void addAlertManager(AlertsManager alertsManager) {
		this.alertsManager = alertsManager;
		this.alertsEJBRemote = alertsManager.alertsEJBRemote;
		// TODO Auto-generated method stub

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

	public long getConvertSec(String speedTimeStamp) {
		Date d1 = TimeZoneUtil.getDateYYYYMMDDHHMMSS(speedTimeStamp);
		long diff = d1.getTime();
		long diffSeconds = diff / 1000;
		return diffSeconds;
	}
}
