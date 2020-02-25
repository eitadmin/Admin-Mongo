package com.eiw.cron.archival;

import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CronRestoreArchival implements Job {

	private static final Logger LOGGER = Logger.getLogger("archival");

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// LOGGER.info("Entering CronRestoreArchival Class");
		// ArchivalEJBRemote archivalEJB = BOFactory.getArchivalEJBRemote();
		// while (true) {
		// List<Datarestorestatus> list = archivalEJB.getRestoreData();
		// for (Datarestorestatus datarestorestatus : list) {
		// archivalEJB.deleteRestoreArchivedData(datarestorestatus);
		// }
		// LOGGER.info("Exiting CronRestoreArchival Class");
		// break;
		// }
	}
}
