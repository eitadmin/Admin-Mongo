package com.eiw.cron.report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.eiw.server.bo.BOFactory;

public class AssetStatusSummaryReport implements Job {

	private static final Logger LOGGER = Logger.getLogger("report");
	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		LOGGER.info("AssetStatusSummaryReport::Entering CronJobKSA Class");
		SummaryEJBRemote summaryEJB = BOFactory.getSummaryEJBRemote();
		List<Object[]> listVehicleevents = summaryEJB
				.vehicleList("Asia/Riyadh");
		for (int i = 0; i < listVehicleevents.size(); i++) {
			Object[] obj = (Object[]) listVehicleevents.get(i);
			String vin = (String) obj[1];
			Date eventDate = (Date) obj[0];
			String NewreportDatasDay = summaryEJB
					.getNewVehicleSumaryStatusReportDay(vin,
							sdfDate.format(eventDate));
			summaryEJB.insertNewVehicleSummaryStatus(vin,
					sdfDate.format(eventDate), NewreportDatasDay);
		}

	}

}
