package com.eiw.cron.report;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.eiw.server.TimeZoneUtil;
import com.eiw.server.bo.BOFactory;

public class VehicleEventBackup implements Job{

	
	private static final Logger LOGGER = Logger.getLogger("report");
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		
		LOGGER.info("NewSummaryDayReport::Entering CronJobKSA Class");
		SummaryEJBRemote summaryEJB = BOFactory.getSummaryEJBRemote();
		List<Object> listVehicles = summaryEJB
				.vehicleListVin();
		
		Calendar calendar = TimeZoneUtil.getDateForTimeZones(new Date(),
				"Asia/Riyadh");
		calendar.add(Calendar.DATE, -1);
		String eventDate = TimeZoneUtil.getDate(calendar.getTime());
	}
}
