package com.eiw.cron;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.eiw.cron.report.SummaryEJBRemote;
import com.eiw.noTransmissionOverride.DeviceStarterImpl;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.bo.BOFactory;
import com.eiw.server.fleettrackingpu.Notransmissionoverride;

public class NoTransOverrideFromTable implements Job {
	@EJB
	DeviceStarterImpl DeviceStarter;
	private EntityManager em;
	private static final Logger LOGGER = Logger
			.getLogger("NoTransmission Override");
	private static final String STR_REGION = "Asia/Riyadh";
	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		try {
			SummaryEJBRemote summaryEJB = BOFactory.getSummaryEJBRemote();
			LOGGER.info("NoTransOverrideFromTable::Entering NoTransOverrideFromTable Class");
			Calendar calendar = TimeZoneUtil.getDateForTimeZones(new Date(),
					STR_REGION);
			calendar.add(Calendar.DATE, 0);
			String currDate = TimeZoneUtil.getDate(calendar.getTime());
			String statusForCall = summaryEJB
					.getRemoveFromNotransmissionOverride(currDate);

		} catch (Exception e) {
			LOGGER.error("NoTransOverrideFromTable " + e.getMessage());
			e.printStackTrace();

		}
	}

}
