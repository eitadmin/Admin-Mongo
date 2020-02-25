package com.eiw.cron.report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;

import javax.mail.MessagingException;

import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.eiw.alerts.AlertsEJBRemote;
import com.eiw.alerts.CheckMaintenance;
import com.eiw.server.AmazonSMTPMail;
import com.eiw.server.EmailSendHttpClient;
import com.eiw.server.SMSSendHttpClient;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.bo.BOFactory;
import com.eiw.server.companyadminpu.Provider;
import com.eiw.server.fleettrackingpu.Alertconfig;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.Vehiclealerts;

public class NoTransmissionAlert implements Job {
	private static final Logger LOGGER = Logger.getLogger("report");
	private static final String TIMEZONEID = "Asia/Riyadh";
	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	AlertsEJBRemote alertsEJBRemote = BOFactory.getAlertsEJBRemote();
	SummaryEJBRemote summaryEJB = BOFactory.getSummaryEJBRemote();
	String timeZone = "", odometer = "";
	Vehicle vehicle;
	int today;
	boolean curTime, validity = false;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();
		LOGGER.error("NoTransmissionAlert::Entering CronJobKSA Class");

		try {
			List<Object[]> noTransList = summaryEJB.getnoTransAlertVins();
			if (noTransList != null && !noTransList.isEmpty()
					&& noTransList.size() != 0) {
				for (int i = 0; i < noTransList.size(); i++) {

					Object[] obj = (Object[]) noTransList.get(i);
					String vin = obj[0].toString();
					String lstTransDate = TimeZoneUtil
							.getTimeINYYYYMMddss((Date) obj[3]);
					String serverTime = TimeZoneUtil
							.getTimeINYYYYMMddss((Date) obj[4]);
					String latlang = obj[1].toString() + ","
							+ obj[2].toString();
					vehicle = alertsEJBRemote.getVehicle(vin);
					timeZone = alertsEJBRemote.getTimeZoneRegion(vin);
					String noTransheartBeat = summaryEJB
							.getnoTransHeartBeatEvent(vin);
					/*
					 * alertCount =
					 * alertsEJBRemote.getNoTransmissionAlertCnt(vin, curDt);
					 */
					Provider providerNoTrans = alertsEJBRemote
							.getProviderDetails(vehicle.getCompanyId());
					// today = alertsEJBRemote.getDay(new Date(), timeZone);

					String operatorName = summaryEJB.getOperatorName(vin);
					List<Alertconfig> alertConfigs = summaryEJB
							.getAlertConfigNoTransmission(vin);
					for (Alertconfig alertConfig : alertConfigs) {
						String Result = summaryEJB.generateNoTransAlert(
								timeZone, alertConfig, vin,
								vehicle.getPlateNo(), lstTransDate, latlang,
								providerNoTrans, operatorName, serverTime,
								noTransheartBeat);
					}

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
