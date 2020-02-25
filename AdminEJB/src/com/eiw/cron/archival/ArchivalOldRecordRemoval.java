package com.eiw.cron.archival;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import com.eiw.server.TimeZoneUtil;
import com.eiw.server.archivalpu.Archivalvehicleevent;

public class ArchivalOldRecordRemoval {
	private EntityManager em;
	private static final Logger LOGGER = Logger.getLogger("archival");

	public ArchivalOldRecordRemoval(EntityManager em1, String timeZone) {
		this.em = em1;
		Calendar calendar = TimeZoneUtil.getDateForTimeZones(new Date(),
				timeZone);
		calendar.add(Calendar.MONTH, -3);
		Date archivalDate = calendar.getTime();
		try {
			int maxResults = 8000;
			LOGGER.info("Before Query Execution ArchivalOldRecordRemoval maxResults "
					+ maxResults);
			String strPollSkyDB = "SELECT evt from Archivalvehicleevent evt where date(eventTimeStamp) < :date";
			Query archDB = em.createQuery(strPollSkyDB);
			archDB.setParameter("date", archivalDate);
			archDB.setFirstResult(0);
			archDB.setMaxResults(maxResults);
			List<Archivalvehicleevent> resultList = (List<Archivalvehicleevent>) archDB
					.getResultList();
			LOGGER.info("After Query Execution ArchivalOldRecordRemoval maxResults "
					+ maxResults);
			maxResults = 0;
			for (Archivalvehicleevent arVe : resultList) {
				em.remove(arVe);
				em.flush();
				maxResults++;
			}
			CronArchival.oldArchivedRecords -= maxResults;
		} catch (Exception e) {
			LOGGER.error("Exception in ArchivalOldRecordRemoval Class " + e);
		}
	}
}
