package com.eiw.cron.archival;

import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.eiw.server.bo.BOFactory;

public class CronArchival implements Job {

	private static final Logger LOGGER = Logger.getLogger("archival");
	private static final String STR_REGION = "Asia/Riyadh";
	public static long records;
	public static long oldArchivedRecords;

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		LOGGER.info("Entering CronArchival Class");
		ArchivalEJBRemote archivalEJB = BOFactory.getArchivalEJBRemote();
		records = archivalEJB.numberOfRecords(STR_REGION);
		while (true) {
			// long records = archivalEJB.numberOfRecords(STR_REGION);
			archivalEJB.startArchiveProcess(STR_REGION);
//			if (records == 0) {
//				List<Datarestorestatus> list = archivalEJB
//						.numberOfRestoreRecords();
//				for (int i = 0; i < list.size(); i++) {
//					archivalEJB.reInsertInVE(list.get(i));
//				}
//				LOGGER.info("Exiting CronArchival Class");
//				break;
//			}
			break;
		}
		archivalEJB.deleteOldArchivedRecords(STR_REGION);
		// oldArchivedRecords =
		// archivalEJB.numberOfOldArchivedRecords(STR_REGION);
		// while (true) {
		// archivalEJB.deleteOldArchivedRecords(STR_REGION);
		// if (oldArchivedRecords == 0) {
		// break;
		// }
		// }
	}
}
