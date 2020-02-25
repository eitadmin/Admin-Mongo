package com.eiw.cron;

import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.eiw.cron.archival.ArchivalEJBRemote;
import com.eiw.server.bo.BOFactory;

public class DataPushCron implements Job {

	private static final Logger LOGGER = Logger.getLogger("cron");

	public void execute(JobExecutionContext argo) throws JobExecutionException {

		ArchivalEJBRemote archivalEJB = BOFactory.getArchivalEJBRemote();

		String json = archivalEJB
				.getAllRamCoBackTrackingDetailsNew("Asia/Kolkata");
		System.out.println(json);
	}
}
