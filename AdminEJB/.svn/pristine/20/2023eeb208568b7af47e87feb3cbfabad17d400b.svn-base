package com.eiw.cron;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.eiw.client.dto.ReportData;
import com.eiw.cron.report.SummaryEJBRemote;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.bo.BOFactory;

public class EmployeeSummaryDayReport implements Job {

	private static final Logger LOGGER = Logger
			.getLogger("Employee Summary Report");
	private static final String STR_REGION = "Asia/Riyadh";
	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		LOGGER.info("EmplpoyeeSummaryDayReport::Entering EmployeeSummaryReport Class");
		Calendar calendar = TimeZoneUtil.getDateForTimeZones(new Date(),
				STR_REGION);
		calendar.add(Calendar.DATE, -1);
		String empdt = TimeZoneUtil.getDate(calendar.getTime());
		SummaryEJBRemote summaryEJB = BOFactory.getSummaryEJBRemote();
		List<Object> listofEmployeeevents = summaryEJB.employeeList(STR_REGION);
		for (int i = 0; i < listofEmployeeevents.size(); i++) {
			Object obj = (Object) listofEmployeeevents.get(i);
			String employeeId = (String) obj;
			// Date eventDate = (Date) obj[0];
			List<ReportData> employeeDatasDay = summaryEJB
					.getEmployeeSummaryDayReport(employeeId, empdt);
			summaryEJB.insertEmployeeDaySummary(employeeId, empdt,
					employeeDatasDay);
		}

	}

}
