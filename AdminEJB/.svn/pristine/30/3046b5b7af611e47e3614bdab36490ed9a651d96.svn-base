package com.eiw.cron.report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.eiw.client.dto.ReportData;
import com.eiw.server.bo.BOFactory;

public class SummaryDayReport implements Job {

	private static final Logger LOGGER = Logger.getLogger("report");
	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		LOGGER.info("SummaryDayReport::Entering CronJobKSA Class");
		SummaryEJBRemote summaryEJB = BOFactory.getSummaryEJBRemote();
		List<Object[]> listVehicleevents = summaryEJB
				.getTodayOdometer("Asia/Riyadh");
		for (int i = 0; i < listVehicleevents.size(); i++) {
			Object[] obj = (Object[]) listVehicleevents.get(i);

			String eventDate = (String) obj[0];
			String vin = (String) obj[1];
			long totOdometerPerDay = (long) obj[2];

			long prevOdometer = 0L;
			String prevEngineHour = "00:00:00";
			Object[] odometers = summaryEJB.getPreviousOdometerValue(vin);
			if (odometers[0] != null) {
				prevOdometer = (long) odometers[0];
			}
			if (odometers[1] != null) {
				prevEngineHour = (String) odometers[1];
			}
			List<ReportData> reportDatasDay = summaryEJB.getSummaryDayReport(
					vin, eventDate);
			int engineHoursPerDay = summaryEJB.insertVehicleDaySummary(vin,
					eventDate, reportDatasDay, totOdometerPerDay);
			summaryEJB.insertVehicleHasOdometer(vin, totOdometerPerDay,
					prevOdometer, engineHoursPerDay, prevEngineHour);
		}
		summaryEJB.resetEngineHours();

		LOGGER.info("SummaryDayReport::Exiting CronJobKSA Class");
	}

}
