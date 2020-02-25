package com.eiw.cron;

import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.eiw.server.bo.BOFactory;

public class AlertConfValidityChk implements Job {

	private static final Logger LOGGER = Logger.getLogger("cron");

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		LOGGER.info("Entering AlertConfValidityChk Class");
		DispatcherEJBRemote dispatcherEJB = BOFactory.getDispatcherEJBRemote();
		// AlertConfigEJBRemote alertConfigEJB = BOFactory
		// .getAlertConfigEJBRemote();
		try {
			dispatcherEJB.getAlertConfigData();
		} catch (Exception e) {
			LOGGER.error("Error Occured in AlertConfValidityChk");
		}
		// String vin = null;
		// alertConfigEJB.startJobImplemented(vin);
		LOGGER.info("Exiting AlertConfValidityChk Class");
	}

}
