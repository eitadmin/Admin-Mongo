package com.eiw.alerts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.eiw.server.TimeZoneUtil;
import com.eiw.server.bo.BOFactory;
import com.eiw.server.fleettrackingpu.Alertconfig;
import com.eiw.server.fleettrackingpu.Vehiclealerts;

public class CheckMaintenance {
	AlertsEJBRemote alertsEJBRemote = BOFactory.getAlertsEJBRemote();
	AlertsManager alertsManager = new AlertsManager();
	Date lastUpdatedTime;
	private static final String STR_ODOM = "_ODOMETER",
			STR_AI_RANGE = "_ai_range", STR_AI_LEAKAGE = "_ai_leakage";
	SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public CheckMaintenance(String timeZone, Alertconfig alertConfig,
			String odometer, String plateNo) {
		manageAlert(timeZone, alertConfig, odometer, plateNo);
	}

	public void manageAlert(String timeZone, Alertconfig alertConfig,
			String odometer, String plateNo) {
		List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();
		try {
			Calendar calendar=TimeZoneUtil.getDateForTimeZones(new Date(),"Asia/Riyadh");
			calendar.add(Calendar.DATE, -1);
			Date date=calendar.getTime();
			String strLastAlertDateTime = TimeZoneUtil
					.getTimeINYYYYMMddss(date);
			Date datecurrentDateTime = sdfTime.parse(strLastAlertDateTime);
			lastUpdatedTime = datecurrentDateTime;
		} catch (Exception e) {
			// LOGGER.error("AlertConfigCacheEJB::getLastUpdateTime::Error in getting alert config for alert type="
			// + alertConfig.getId().getAlertType() + " : e");
			return;
		}
		int odometerConfigured, previousOdometer, odometerValue;
		String[] odo = alertConfig.getAlertRange().split("_");
		odometerConfigured = Integer.parseInt(odo[0]);
		previousOdometer = Integer.parseInt(odo[1]);
		odometerValue = (Integer.parseInt(odometer)) - previousOdometer;
		if (odometerValue >= odometerConfigured) {
			// if ((Integer.parseInt(odometer)) >= odometerConfigured) {
			String alertType;
			String description = null;
			String mobile2 = null;
			mobile2 = alertConfig.getSmsNumber();
			String eventTime = TimeZoneUtil.getStrTZDateTime(new Date());

			if (alertConfig.getId().getAlertType().contains(STR_ODOM)) {
				alertType = alertConfig.getId().getAlertType()
						.replace(STR_ODOM, "");

			} else if (alertConfig.getId().getAlertType()
					.contains(STR_AI_RANGE)) {
				alertType = alertConfig.getId().getAlertType()
						.replace(STR_AI_RANGE, "");

			} else if (alertConfig.getId().getAlertType()
					.contains(STR_AI_LEAKAGE)) {
				alertType = alertConfig.getId().getAlertType()
						.replace(STR_AI_LEAKAGE, "");

			} else {
				alertType = alertConfig.getId().getAlertType();

			}
			if (alertConfig.getId().getAlertType().contains(STR_ODOM)) {
				description = "Alert : Odometer Exceeds " + odometerValue
						+ " kms Current Reading : " + odometer
						+ " kms Plate No : " + plateNo;
				description.replace(" ", "+");
			} else {
				description = "Alert%0DType : " + alertType + " %0Dvehicle:"
						+ plateNo + "%0DTime:" + eventTime;
			}

			Vehiclealerts va = new Vehiclealerts();
			va.setAlerttype(alertConfig.getId().getAlertType());
			va.setDescription(description);
			va.setEventTimeStamp(TimeZoneUtil.getDateTimeZone(new Date(),
					timeZone));
			va.setSmsmobile(mobile2);
			va.setVin(alertConfig.getId().getVin());
			va.setServerTimeStamp(TimeZoneUtil.getDateTimeZone(new Date(),
					timeZone));
			va.setShowstatus(false);
			vehiclealerts.add(va);
		}
		if (!vehiclealerts.isEmpty()) {
			lastUpdatedTime = alertsManager.persistVehicleAlert(alertConfig,
					vehiclealerts, lastUpdatedTime);
		}
	}
}