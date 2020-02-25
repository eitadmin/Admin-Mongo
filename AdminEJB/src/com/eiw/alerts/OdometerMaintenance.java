package com.eiw.alerts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.eiw.cron.report.SummaryEJBRemote;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.bo.BOFactory;
import com.eiw.server.fleettrackingpu.Alertconfig;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.Vehicleevent;
import com.eiw.server.fleettrackingpu.VehicleeventId;

public class OdometerMaintenance implements Job {

	private static final Logger LOGGER = Logger.getLogger("report");
	AlertsEJBRemote alertsEJBRemote = BOFactory.getAlertsEJBRemote();
	SummaryEJBRemote summaryEJB = BOFactory.getSummaryEJBRemote();
	String timeZone, odometer;
	Vehicle vehicle;
	int today;
	boolean curTime, validity = false;
	List<Vehicleevent> vehicleEvents = new ArrayList<Vehicleevent>();

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		LOGGER.info("OdometerMaintenance::Entering CronJobKSA Class");
		Vehicleevent vehicleevent = new Vehicleevent();
		VehicleeventId id = new VehicleeventId();
		id.setEventTimeStamp(TimeZoneUtil.getDateInTimeZone());
		vehicleevent.setId(id);
		vehicleEvents.add(vehicleevent);

		List<Object[]> listVehicleevents = summaryEJB
				.vehicleList("Asia/Riyadh");
		for (int i = 0; i < listVehicleevents.size(); i++) {
			Object[] obj = (Object[]) listVehicleevents.get(i);
			String vin = (String) obj[1];
			vehicle = alertsEJBRemote.getVehicle(vin);
			timeZone = alertsEJBRemote.getTimeZoneRegion(vin);
			today = alertsEJBRemote.getDay(new Date(), timeZone);
			List<Alertconfig> alertConfigs = alertsEJBRemote
					.getAlertConfigOdometer(vin);
			List<Alertconfig> overAllalertConfigs = alertsEJBRemote
					.getOverAllAlertConfig(vin, "FUELCONSUMPTION");
			odometer = alertsEJBRemote.getOdometer(vin);

			if (odometer != null) {
				for (Alertconfig alertConfig : alertConfigs) {
					SortedMap<Integer, Boolean> test = getDayAlert(alertConfig
							.getAlertDay());
					curTime = alertsEJBRemote.getTime(vehicleEvents,
							alertConfig.getAlertTime());
					if (alertConfig.getValidityExp() != null) {
						validity = alertsEJBRemote.getValidity(vehicleEvents,
								alertConfig.getValidityExp());
					} else {
						validity = true;
					}
					if ((test.get(today)) && (curTime) && (validity)) {
						if ((alertConfig.getSubAlertType() != null)
								&& (alertConfig.getSubAlertType()
										.equalsIgnoreCase("ODOMETER")))
							new CheckMaintenance(timeZone, alertConfig,
									odometer, vehicle.getPlateNo());
					}
				}
			}
			if (overAllalertConfigs != null && !overAllalertConfigs.isEmpty()) {
				for (Alertconfig fuelConsumption : overAllalertConfigs) {
					new CheckFuelConsumption(fuelConsumption, timeZone,
							vehicle.getVin(), vehicle.getPlateNo());
				}
			}			
		}
		LOGGER.info("OdometerMaintenance::Exiting CronJobKSA Class");
	}

	private SortedMap<Integer, Boolean> getDayAlert(int alertDay) {
		SortedMap<Integer, Boolean> hmap = new TreeMap<Integer, Boolean>();
		if (alertDay / 64 == 1) {
			alertDay = alertDay % 64;
			hmap.put(1, true);
		} else {
			hmap.put(1, false);
		}
		if (alertDay / 32 == 1) {
			alertDay = alertDay % 32;
			hmap.put(2, true);
		} else {
			hmap.put(2, false);
		}
		if (alertDay / 16 == 1) {
			alertDay = alertDay % 16;
			hmap.put(3, true);
		} else {
			hmap.put(3, false);
		}
		if (alertDay / 8 == 1) {
			alertDay = alertDay % 8;
			hmap.put(4, true);
		} else {
			hmap.put(4, false);
		}
		if (alertDay / 4 == 1) {
			alertDay = alertDay % 4;
			hmap.put(5, true);
		} else {
			hmap.put(5, false);
		}
		if (alertDay / 2 == 1) {
			alertDay = alertDay % 2;
			hmap.put(6, true);
		} else {
			hmap.put(6, false);
		}
		if (alertDay / 1 == 1) {
			alertDay = alertDay % 1;
			hmap.put(7, true);
		} else {
			hmap.put(7, false);
		}
		return hmap;
	}

}
