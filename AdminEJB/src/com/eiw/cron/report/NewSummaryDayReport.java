package com.eiw.cron.report;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jboss.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.eiw.client.dto.ReportData;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.bo.BOFactory;

public class NewSummaryDayReport implements Job {

	private static final Logger LOGGER = Logger.getLogger("report");
	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			LOGGER.info("NewSummaryDayReport::Entering CronJobKSA Class");
			SummaryEJBRemote summaryEJB = BOFactory.getSummaryEJBRemote();
			List<Object> listVehicleevents = summaryEJB.vehicleListVin();

			Calendar calendar = TimeZoneUtil.getDateForTimeZones(new Date(),
					"Asia/Riyadh");
			calendar.add(Calendar.DATE, -1);
			String eventDate = TimeZoneUtil.getDate(calendar.getTime());
			for (int i = 0; i < listVehicleevents.size(); i++) {
				Object obj = listVehicleevents.get(i);
				String vin = (String) obj;
				String NewreportDatasDay = summaryEJB
						.getNewVehicleSumaryReportDay(vin, eventDate);
				JSONObject resultjson = new JSONObject(NewreportDatasDay);
				summaryEJB.insertNewVehicleDaySummary(vin, eventDate,
						resultjson.getString("Summary"));
				summaryEJB.insertOverSpeedSummary(vin, eventDate,
						resultjson.getString("overspeed"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
