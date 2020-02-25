package com.eiw.cron;

import java.util.List;

import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.eiw.server.bo.BOFactory;

public class CronJobIND implements Job {

	private static final Logger LOGGER = Logger.getLogger("cron");
	private static final String STR_REGION = "Asia/Kolkata";

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		LOGGER.info("Entering CronJobIND Class");
		DispatcherEJBRemote dispatcherEJB = BOFactory.getDispatcherEJBRemote();
		List<String> vehicles = dispatcherEJB.vehicleList(STR_REGION);
		for (String singleVehicle : vehicles) {
			try {
				dispatcherEJB.dispatcher(singleVehicle, STR_REGION);
			} catch (Exception e) {
				LOGGER.error("Error Occured CronJobIND " + singleVehicle);
			}
		}
		LOGGER.info("Exiting CronJobIND Class");
	}

}
