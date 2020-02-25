package com.eiw.cron;

import java.util.List;

import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.eiw.alerts.AlertsEJBRemote;
import com.eiw.alerts.AlertsManager;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.bo.BOFactory;
import com.eiw.server.fleettrackingpu.Alertconfig;
import com.eiw.server.fleettrackingpu.AlertconfigId;
import com.eiw.server.fleettrackingpu.Calendarevent;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.Vehiclealerts;

public class CronMaintAlert implements Job {
	// EMSAdminPortal emsadminportal = new

	private static final Logger LOGGER = Logger.getLogger("cron");

	public void execute(JobExecutionContext argo) throws JobExecutionException {
		AlertsEJBRemote alertEJBremote = BOFactory.getAlertsEJBRemote();
		List<Calendarevent> calEvents = alertEJBremote.getMaintanceAlert();
		LOGGER.info(calEvents.size());

		AlertsManager alertsManager = new AlertsManager();
		for (Calendarevent calEvent : calEvents) {
			Vehicle vehicle = alertEJBremote.getVehicle(calEvent.getVehicle()
					.getVin());
			alertsManager.callSMSService(getVehicleAlert(calEvent, vehicle),
					getAlertConfig(calEvent, vehicle), false, true);
 			alertsManager.callEmailService(getVehicleAlert(calEvent, vehicle),
					getAlertConfig(calEvent, vehicle), false, true);
		}
	}

	private Alertconfig getAlertConfig(Calendarevent calEvents, Vehicle vehicle) {
		Alertconfig alertConfig = new Alertconfig();
		AlertconfigId id = new AlertconfigId();
		id.setAlertType(calEvents.getMaintenanceType());
		id.setBranchId(calEvents.getBranchId());
		id.setCompanyId(calEvents.getCompanyId());
		id.setUserId(calEvents.getCompanyId());
		id.setVin(calEvents.getVehicle().getVin());
		alertConfig.setId(id);
		alertConfig.setEmailAddress(calEvents.getEmailId()); 
		return alertConfig;
	}

	private Vehiclealerts getVehicleAlert(Calendarevent calEvents,
			Vehicle vehicle) {
		Vehiclealerts vehiAlert = new Vehiclealerts();
		vehiAlert.setAlerttype(calEvents.getMaintenanceType());
		vehiAlert.setVin(calEvents.getVehicle().getVin());
		vehiAlert.setSmsmobile(calEvents.getPhoneNo());
		vehiAlert.setDescription("Maintanences Alert : "
				+ calEvents.getMaintenanceType() + " is due for this vehicle "
				+ vehicle.getPlateNo() + " from today");
		vehiAlert.setEventTimeStamp(TimeZoneUtil.getDateInTimeZone());
		return vehiAlert;
	}

}
