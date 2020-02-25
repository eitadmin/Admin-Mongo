package com.eiw.cron.archival;

import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.eiw.server.bo.BOFactory;

public class DataPushCron implements Job {

	private static final Logger LOGGER = Logger.getLogger("archival");

	public void execute(JobExecutionContext argo) throws JobExecutionException {

		LOGGER.info("Entering DataPushCronArchival Class");

		ArchivalEJBRemote archivalEJB = BOFactory.getArchivalEJBRemote();

		String json = archivalEJB
				.getAllRamCoBackTrackingDetailsNew("Asia/Kolkata");
		System.out.println(json);
		LOGGER.info("Leaving DataPushCronArchival Class" + json);
	}
}
