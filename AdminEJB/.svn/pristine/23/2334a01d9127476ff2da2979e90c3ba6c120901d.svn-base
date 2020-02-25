package com.eiw.cron.archival;

import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.eiw.server.bo.BOFactory;

public class CronArchivalVehicleSummary implements Job {

    private static final Logger LOGGER = Logger.getLogger("archival");
    private static final String STR_REGION = "Asia/Riyadh";

    @Override
    public void execute(JobExecutionContext arg0)
        throws JobExecutionException {
        LOGGER.info("Entering CronArchivalVehicleSummary Class");
        ArchivalVehicleSummaryEJBRemote archivalVehSummEJB = BOFactory.getArhivalVehicleSummaryEJBRemote();
        while (true) {
            int records = archivalVehSummEJB.numberOfRecords(STR_REGION);
            archivalVehSummEJB.startVehicleSummaryArchive(STR_REGION);
            if(records == 0) {
                break;
            }
            LOGGER.info("Exiting CronArchivalVehicleSummary Class");
        }
    }

}
