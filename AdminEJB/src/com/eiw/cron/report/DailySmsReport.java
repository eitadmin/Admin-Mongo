package com.eiw.cron.report;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.eiw.server.TimeZoneUtil;
import com.eiw.server.bo.BOFactory;

public class DailySmsReport implements Job {

	private static final Logger LOGGER = Logger.getLogger("report");
	private static final String TIMEZONEID = "Asia/Riyadh";

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Calendar calendar = TimeZoneUtil.getDateForTimeZones(new Date(),
				TIMEZONEID);
		String currentDate = TimeZoneUtil.getDate(calendar.getTime());
		SummaryEJBRemote summaryEJB = BOFactory.getSummaryEJBRemote();
		try {
			LOGGER.error("Enter into DailySmsReport");
			List<Object[]> schoolList = summaryEJB.getcompID("DAILYSMSREPORT");
			for (int i = 0; i < schoolList.size(); i++) {
				Object[] compRow = (Object[]) schoolList.get(i);
				String compID = compRow[0].toString();
				String sumMail = compRow[1].toString();
				String reportType = "Pdf";
				JSONObject companySmsReportList = summaryEJB
						.getSmsReportForSchool(compID, currentDate);
				JSONArray resultdata = companySmsReportList
						.getJSONArray("data");
				JSONArray format = companySmsReportList.getJSONArray("heading");
				String status = summaryEJB.sendReportMail(resultdata,
						"SMS REPORT", sumMail, compID, format, reportType,
						compID + "_DailySMSReport");
				LOGGER.error(" Daily SMS Report CompanyId : " + compID
						+ "  Status : " + status);
			}
			LOGGER.error("Exit From DailySmsReport");
		} catch (Exception e) {
			LOGGER.error("Error on DailySmsReport" + e);
			e.printStackTrace();
		}
	}
}
