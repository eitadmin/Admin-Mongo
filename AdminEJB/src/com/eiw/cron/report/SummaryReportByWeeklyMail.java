package com.eiw.cron.report;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.eit.dcframework.server.bo.OSValidator;
import com.eiw.cron.report.SummaryEJBRemote;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.bo.BOFactory;

public class SummaryReportByWeeklyMail implements Job {

	private static final Logger LOGGER = Logger.getLogger("report");
	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		String SourceFolre = "";
		Calendar calendar = TimeZoneUtil.getDateForTimeZones(new Date(),
				"Asia/Riyadh");
		calendar.add(Calendar.DATE, -7);
		String fromDate = TimeZoneUtil.getDate(calendar.getTime());
		String todate = sdfDate.format(new Date());
		LOGGER.info("SummaryDayReportToMail::Entering CronJobKSA Class");
		SummaryEJBRemote summaryEJB = BOFactory.getSummaryEJBRemote();

		try {
			List<Object[]> listCompany = summaryEJB
					.getcompID("WEEKLYSUMMARYREPORT");
			for (int i = 0; i < listCompany.size(); i++) {
				Object[] compRow = (Object[]) listCompany.get(i);
				String compID = compRow[0].toString();
				String sumMail = compRow[1].toString();

				String reportType = summaryEJB.getPreferencesData(
						"MailReportType", compID);

				// String sumMail = summaryEJB.getsummaryMail(compID);
				List<Object[]> listvins = summaryEJB.getsummaryVins(compID);
				JSONArray finalarray = new JSONArray();

				for (int k = 0; k < listvins.size(); k++) {
					String uId = "";
					Object[] rowVin = (Object[]) listvins.get(k);
					if (rowVin[1] != null) {
						uId = rowVin[1].toString();
					}

					String completeMailDataOnlyTotal = summaryEJB
							.getConsolidateSummaryByDayReportTotal(
									rowVin[0].toString(), uId, fromDate, todate);
					if (!completeMailDataOnlyTotal.equals("{}"))
						finalarray
								.put(new JSONObject(completeMailDataOnlyTotal));

				}
				String source = "[Plate No,Asset Code,Date,Begin Time,Begin At,End Time,End At,Running Dur,Stop Dur,Idle dur,Towed Dur,Odometer]";
				JSONArray format = new JSONArray(source);
				summaryEJB.sendReportMail(finalarray, "Weekly Summary Report",
						sumMail, compID, format, reportType, compID
								+ "_WeeklyReport");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}