package com.eiw.alerts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.eiw.server.TimeZoneUtil;
import com.eiw.server.bo.BOFactory;
import com.eiw.server.fleettrackingpu.Alertconfig;
import com.eiw.server.fleettrackingpu.VehicleHasOdometer;
import com.eiw.server.fleettrackingpu.Vehiclealerts;

public class CheckFuelConsumption {
	List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();
	AlertsEJBRemote alertsEJBRemote = BOFactory.getAlertsEJBRemote();
	AlertsManager alertsManager = new AlertsManager();
	Date lastUpdatedTime;
	SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

	public CheckFuelConsumption(Alertconfig fuelConsumption, String timeZone,
			String vin, String plateNo) {
		manageAlert(fuelConsumption, timeZone, vin, plateNo);
	}

	public void manageAlert(Alertconfig fuelConsumption, String timeZone,
			String vin, String plateNo) {
		String predate = "";
		try {
			Calendar calendar = TimeZoneUtil.getDateForTimeZones(new Date(),
					"Asia/Riyadh");
			calendar.add(Calendar.DATE, -1);
			Date date = calendar.getTime();
			String strLastAlertDateTime = TimeZoneUtil
					.getTimeINYYYYMMddss(date);
			Date datecurrentDateTime = sdfTime.parse(strLastAlertDateTime);
			predate = sdfDate.format(datecurrentDateTime);
			lastUpdatedTime = datecurrentDateTime;
		} catch (Exception e) {
			// LOGGER.error("AlertConfigCacheEJB::getLastUpdateTime::Error in getting alert config for alert type="
			// + alertConfig.getId().getAlertType() + " : e");
			return;
		}
		List<VehicleHasOdometer> overAllvalues = alertsEJBRemote
				.getOverAllOdometer(vin, predate);
		if (!overAllvalues.isEmpty() && overAllvalues.size() != 0) {
			for (int i = 0; i < overAllvalues.size(); i++) {
				String alertType;
				String description = null;
				String mobile2 = null;
				mobile2 = fuelConsumption.getSmsNumber();
				String eventTime = TimeZoneUtil.getStrTZDateTime(new Date());
				// String odometervin =
				// overAllvalues.get(i).getId().getVin();
				String cumulativeValue = overAllvalues.get(i).getOdometer();
				int perDayValue = Integer.parseInt(overAllvalues.get(i)
						.getOdometerPerDay());
				String alerttypename = "FUELCONSUMPTION";
				String alertRange = fuelConsumption.getAlertRange();
				float alertRange1 = Integer.parseInt(alertRange.split("_")[0]);
				float alertRange2 = Integer.parseInt(alertRange.split("_")[1]);
				float val1 = alertRange1 / 100;
				float fuelConsumpValue = Math.round((perDayValue * (alertRange1 / 100)));
				// float fuelConsumpValue = ((perDayValue / 100) * alertRange1);
				System.out.println(fuelConsumpValue);
				description = "Alert%0DType :" + alerttypename + " Vehicle:"
						+ plateNo + " %0DCumulative%0DOdometer%0DValue:"
						+ cumulativeValue + "%0DYesterDay%0DOdometer%0DValue:"
						+ perDayValue + " Fuel%0DConsumption%0DValue:"
						+ fuelConsumpValue;
				System.out.println(description);
				Vehiclealerts va = new Vehiclealerts();
				va.setAlerttype(fuelConsumption.getId().getAlertType());
				va.setDescription(description);
				va.setEventTimeStamp(TimeZoneUtil.getDateTimeZone(new Date(),
						timeZone));
				va.setSmsmobile(mobile2);
				va.setVin(fuelConsumption.getId().getVin());
				va.setServerTimeStamp(TimeZoneUtil.getDateTimeZone(new Date(),
						timeZone));
				va.setShowstatus(false);
				vehiclealerts.add(va);
				if (!vehiclealerts.isEmpty()) {
					lastUpdatedTime = alertsManager.persistVehicleAlert(
							fuelConsumption, vehiclealerts, lastUpdatedTime);
				}
			}
		}
	}
}
