package com.eiw.cron.archival;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import com.eiw.server.TimeZoneUtil;

@Singleton
@Startup
public class ArchivalVehicleSummaryEJB
		implements ArchivalVehicleSummaryEJBRemote {

	@PersistenceContext(unitName = "ltmsfleettrackingpu")
	private EntityManager em;
	private static final Logger LOGGER = Logger.getLogger("archival");
	private static final String STR_YYMMDD = "yyyy-MM-dd";

	@PostConstruct
	public int numberOfRecords(String timeZone) {
		LOGGER.info("ArchivalEJB::numberOfRecords::" + "Timezone" + timeZone);
		int records = 0;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					STR_YYMMDD);
			Calendar calendar = TimeZoneUtil.getDateForTimeZones(new Date(),
					timeZone);
			calendar.add(Calendar.MONTH, -3);
			Date archivalDate = calendar.getTime();
			LOGGER.debug("ArchivalVehicleSummaryEJB Before Query Execution");
			String strVehSumCount = "SELECT COUNT(vin) FROM Vehiclecompletesummary vcs WHERE eventTimeStamp < :date";
			Query qVehSumCount = em.createQuery(strVehSumCount);
			qVehSumCount.setParameter("date", archivalDate);
			LOGGER.debug("ArchivalVehicleSummaryEJB After Query Execution");
			records = ((Long) qVehSumCount.getSingleResult()).intValue();
		} catch (Exception e) {
			LOGGER.error(
					"ArchivalVehicleSummaryEJB::numberOfRecords::ArchivalEJB Exception"
							+ e);
		}
		return records;
	}

	@Override
	public void startVehicleSummaryArchive(String timeZone) {
		new ArchivalVehicleSummary(em, timeZone);
	}

}
