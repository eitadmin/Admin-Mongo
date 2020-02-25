package com.eiw.cron.report;

import java.text.SimpleDateFormat;
import java.util.List;

import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.eiw.alerts.AlertsEJBRemote;
import com.eiw.server.bo.BOFactory;
import com.eiw.server.fleettrackingpu.Vehicle;

public class DataMigration implements Job {

	private static final Logger LOGGER = Logger.getLogger("Data Migration");
	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	AlertsEJBRemote alertsEJBRemote = BOFactory.getAlertsEJBRemote();
	SummaryEJBRemote summaryEJB = BOFactory.getSummaryEJBRemote();

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		try {
			String condition = summaryEJB.getPreferencesData("datamigration",
					"demo");
			if (condition.equalsIgnoreCase("yes")) {
				List<String> migationVinList = summaryEJB
						.getdataMigrationVins();
				LOGGER.error("Data Migration::Entering vin Count : "
						+ migationVinList.size());
				if (!migationVinList.isEmpty()) {
					for (int i = 0; i < migationVinList.size(); i++) {
						String migrationVin = migationVinList.get(i);
						LOGGER.error("Processing -> vin= " + migrationVin);
						summaryEJB.getLatestDataandMigrate(migrationVin);
					}
					LOGGER.error("Data Migration::Entering SuccessFully Finiched : ");
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
