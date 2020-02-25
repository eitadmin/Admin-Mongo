package com.eiw.cron;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.FileUtils;
import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.eiw.server.EmailSendHttpClient;
import com.eiw.server.OSValidator;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.bo.BOFactory;
import com.eiw.server.fleettrackingpu.Alertconfig;

public class AutomatedReport implements Job {

	private static final Logger LOGGER = Logger.getLogger("cron");
	private static final String STR_REGION = "Asia/Riyadh";
	private static final String STR_NEIGHBOURHOODREPORT = "NEIGHBOURHOODREPORT";
	File folder;

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		LOGGER.info("Entering AutomatedReport Class");
		DispatcherEJBRemote dispatcherEJB = BOFactory.getDispatcherEJBRemote();
		// List<String> vehicles = dispatcherEJB.vehicleList(STR_REGION);
		// for (String singleVehicle : vehicles) {
		// try {
		// dispatcherEJB.dispatcher(singleVehicle, STR_REGION);
		// } catch (Exception e) {
		// LOGGER.error("Error Occured AutomatedReport " + singleVehicle);
		// }
		// }
		List<Alertconfig> listOfFMs = dispatcherEJB
				.fmList(STR_NEIGHBOURHOODREPORT);
		for (Alertconfig ac : listOfFMs) {
			try {
				String companyId = ac.getId().getCompanyId();
				String userId = ac.getId().getUserId();
				String branchId = ac.getId().getBranchId();
				String vin = "All Vehicles";
				String plateNo = "All Vehicles";
				LOGGER.info("vin : " + vin);
				generatePdf(companyId, branchId, userId, vin, plateNo);

				if (OSValidator.isWindows()) {
					folder = new File("C:\\DEV4\\reports");
				} else if (OSValidator.isUnix()) {
					folder = new File("/home/ubuntu/automatedreport");
				}
				File[] listOfFiles = folder.listFiles();
				List<File> files = new ArrayList<File>();
				for (int i = 0; i < listOfFiles.length; i++) {
					if (listOfFiles[i].isFile()) {
						files.add(listOfFiles[i]);
					}
					if (dispatcherEJB.isEmailValidOrNot(ac.getEmailAddress())) {
						EmailSendHttpClient email = new EmailSendHttpClient();
						email.send(
								ac.getEmailAddress(),
								"Neighbourhoodreport Report",
								"Please find the attachment for fleet neighbourhood report",
								files);
					}
					files.clear();

				}
				LOGGER.info("Email sent");
				FileUtils.cleanDirectory(folder);
			} catch (Exception e) {
				LOGGER.error("Error Occured userHasActiveVins " + ac);
			}
		}
		LOGGER.info("Exiting AutomatedReport Class");
	}

	private String generatePdf(String companyId, String branchId,
			String userId, String vin, String plateNo) {
		LOGGER.debug("vin inside generatePdf :" + vin);
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = TimeZoneUtil.getDateForTimeZones(new Date(),
				STR_REGION);
		calendar.add(Calendar.DATE, -1);
		Date neighbourhoodsummeryDate = calendar.getTime();
		String dateChk = sdfDate.format(neighbourhoodsummeryDate);
		// Need to remove port while deploye in Live
		String url = "http://localhost/fleettracking/generatePDFReportServlet";
		NameValuePair nvp1 = new NameValuePair("name",
				"TabukMunicipalityReports");
		NameValuePair nvp2 = new NameValuePair("fromDate", dateChk);
		NameValuePair nvp3 = new NameValuePair("toDate", dateChk);
		NameValuePair nvp4 = new NameValuePair("compName", companyId);
		NameValuePair nvp5 = new NameValuePair("brnchName", branchId);
		NameValuePair nvp6 = new NameValuePair("userName", userId);
		NameValuePair nvp7 = new NameValuePair("alertType", "All");
		NameValuePair nvp8 = new NameValuePair("selectedVehicle", vin);
		NameValuePair nvp9 = new NameValuePair("selectedVehiPlateNo", plateNo);
		NameValuePair nvp10 = new NameValuePair("ourlogo",
				"logo_goldenelement.png");
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(url);
		method.setQueryString(new NameValuePair[] { nvp1, nvp2, nvp3, nvp4,
				nvp5, nvp6, nvp7, nvp8, nvp9, nvp10 });
		String output = null;
		try {
			int statusCode = client.executeMethod(method);
			output = method.getResponseBodyAsString();
			LOGGER.info("Response Code : " + statusCode);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("AutomatedReport :: generatePdf " + e);
		}
		method.releaseConnection();
		return output;
	}

}
