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
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.bo.BOFactory;
import com.eiw.server.fleettrackingpu.Vehicle;

public class SummaryDayReportToMail implements Job {

	private static final Logger LOGGER = Logger.getLogger("report");
	private static final String TIMEZONEID = "Asia/Riyadh";
	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		String SourceFolre = "";
		Calendar calendar = TimeZoneUtil.getDateForTimeZones(new Date(),
				TIMEZONEID);
		calendar.add(Calendar.DATE, -1);
		String preDt = TimeZoneUtil.getDate(calendar.getTime());

		// TODO Auto-generated method stub
		LOGGER.info("SummaryDayReportToMail::Entering CronJobKSA Class");
		SummaryEJBRemote summaryEJB = BOFactory.getSummaryEJBRemote();
		try {
			List<Object[]> listCompany = summaryEJB.getcompID("SUMMARYREPORT");
			for (int i = 0; i < listCompany.size(); i++) {
				Object[] compRow = (Object[]) listCompany.get(i);
				String compID = compRow[0].toString();
				String sumMail = compRow[1].toString();
				String keyVal = summaryEJB.getPreferencesData("ConsolSummary",
						compID);
				String reportType = summaryEJB.getPreferencesData(
						"MailReportType", compID);
				if (OSValidator.isWindows()) {
					SourceFolre = "C:\\Windows\\Temp\\" + compID + "Report"
							+ preDt + ".zip";
				} else if (OSValidator.isUnix()) {
					SourceFolre = "/home/ubuntu/imagesdirectory/Report/"
							+ compID + "Report" + preDt + ".zip";
				}

				// String sumMail = summaryEJB.getsummaryMail(compID);
				List<Object[]> listvins = summaryEJB.getsummaryVins(compID);
				JSONArray finalarray = new JSONArray();
				if (keyVal.equalsIgnoreCase("Detailed")) {
					for (int j = 0; j < listvins.size(); j++) {
						String uId = "";
						Object[] rowVin = (Object[]) listvins.get(j);
						if (rowVin[1] != null) {
							uId = rowVin[1].toString();
						}
						JSONArray completeMailData = summaryEJB
								.getConsolidateSummaryReport(
										rowVin[0].toString(), uId);
						if (completeMailData.length() != 0) {
							String plateno = new JSONObject(completeMailData
									.get(0).toString()).getString("Plate No");
							String source = "[Plate No,Asset Code,Date,Begin Time,Begin At,End Time,End At,Running Dur,Stop Dur,Idle dur,Towed Dur,Odometer]";
							JSONArray format = new JSONArray(source);
							finalarray.put(summaryEJB.sendReportMailMultifiles(
									completeMailData, "Summary Report",
									sumMail, compID, format, plateno));
							if (j != 0 && j % 200 == 0) {
								String zippath = summaryEJB.converToZip(
										finalarray.toString(), SourceFolre);

								summaryEJB.SendMail(compID, zippath, sumMail);
								finalarray = new JSONArray();
							}
						}

					}

					System.out.println("--------------------------------"
							+ finalarray.toString());
					String zippath = summaryEJB.converToZip(
							finalarray.toString(), SourceFolre);
					summaryEJB.SendMail(compID, zippath, sumMail);
					// String source =
					// "[Plate No,Date,Begin,Begin At,End,End At,Running Dur,Stop Dur,Idle dur,Towed Dur,Odometer]";
					// JSONArray format = new JSONArray(source);
					// summaryEJB.sendReportMailMultifiles(finalarray,
					// "Summary Report", sumMail, compID, format);
				} else {
					for (int k = 0; k < listvins.size(); k++) {
						String uId = "";
						Object[] rowVin = (Object[]) listvins.get(k);
						if (rowVin[1] != null) {
							uId = rowVin[1].toString();
						}

						JSONArray completeMailDataOnlyTotal = summaryEJB
								.getConsolidateSummaryReportTotal(
										rowVin[0].toString(), uId);
						if (completeMailDataOnlyTotal != null)
							for (int l = 0; l < completeMailDataOnlyTotal
									.length(); l++) {
								finalarray
										.put(completeMailDataOnlyTotal.get(l));
							}

					}
					String source = "[Plate No,Asset Code,Date,Begin Time,Begin At,End Time,End At,Running Dur,Stop Dur,Idle dur,Towed Dur,Odometer]";
					JSONArray format = new JSONArray(source);
					summaryEJB.sendReportMail(finalarray, "Summary Report",
							sumMail, compID, format, reportType,compID+"_DailyReport");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
