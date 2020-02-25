package com.eiw.cron;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.eiw.server.bo.BOFactory;
import com.skt.alerts.SKTAlertsEJBremote;

public class CronJobforStudent implements Job {

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		SKTAlertsEJBremote studentEJBremote = BOFactory
				.getStudentalertEJBremote();
		studentEJBremote.manageStudentAbsence();
	}

}
