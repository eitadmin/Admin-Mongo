package com.eiw.cron.archival;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import com.eiw.server.TimeZoneUtil;
import com.eiw.server.archivalpu.Archivalvehicleevent;
import com.eiw.server.archivalpu.ArchivalvehicleeventId;
import com.eiw.server.fleettrackingpu.Vehicleevent;

public class Archival {

	// private EntityManager em;
	private static final Logger LOGGER = Logger.getLogger("archival");

	public Archival(EntityManager em, String timeZone) {
		// this.em = em1;
		Calendar calendar = TimeZoneUtil.getDateForTimeZones(new Date(),
				timeZone);
		calendar.add(calendar.MONTH, -2);
		Date archivalDate = calendar.getTime();
		String dateNew = TimeZoneUtil.getDate(archivalDate);
		Date dateNew1 = TimeZoneUtil.getDate(dateNew);
		try {
			int maxResults = 80000;
			LOGGER.info("Before Query Execution Archival maxResults "
					+ maxResults);
			String strPollSkyDB = "SELECT evt from Vehicleevent evt where date(eventTimeStamp) < :date";
			Query qPollSkyDB = em.createQuery(strPollSkyDB);
			qPollSkyDB.setParameter("date", dateNew1);
			qPollSkyDB.setFirstResult(0);
			qPollSkyDB.setMaxResults(maxResults);
			List<Vehicleevent> resultList = (List<Vehicleevent>) qPollSkyDB
					.getResultList();
			LOGGER.info("After Query Execution Archival maxResults "
					+ maxResults);
			maxResults = 0;
			for (Vehicleevent ve : resultList) {
				boolean isPersist;
				try {
					isPersist = true;
					Archivalvehicleevent archivalvehicleevent = new Archivalvehicleevent();
					ArchivalvehicleeventId archivalvehicleeventId = new ArchivalvehicleeventId();
					archivalvehicleevent.setArchivalBy("System");
					archivalvehicleevent.setArchivalDt(new Date());
					archivalvehicleevent.setSkyid(ve.getSkyid());
					archivalvehicleeventId.setVin(ve.getId().getVin());
					archivalvehicleeventId.setEventTimeStamp(ve.getId()
							.getEventTimeStamp());
					archivalvehicleevent.setServerTimeStamp(ve
							.getServerTimeStamp());
					archivalvehicleevent.setLatitude(ve.getLatitude());
					archivalvehicleevent.setLongitude(ve.getLongitude());
					archivalvehicleevent.setSpeed(ve.getSpeed());
					archivalvehicleevent.setPriority(ve.getPriority());
					archivalvehicleevent.setIoevent(ve.getIoevent());
					archivalvehicleevent.setBytesTrx(ve.getBytesTrx());
					archivalvehicleevent.setEngine(ve.getEngine());
					archivalvehicleevent.setTempSensor1(ve.getTempSensor1());
					archivalvehicleevent.setTempSensor2(ve.getTempSensor2());
					archivalvehicleevent.setTempSensor3(ve.getTempSensor3());
					archivalvehicleevent.setOdometer(ve.getOdometer());
					archivalvehicleevent.setBattery(ve.getBattery());
					archivalvehicleevent.setEventSource(ve.getEventSource());
					archivalvehicleevent.setAi1(ve.getAi1());
					archivalvehicleevent.setAi2(ve.getAi2());
					archivalvehicleevent.setAi3(ve.getAi3());
					archivalvehicleevent.setAi4(ve.getAi4());
					archivalvehicleevent.setDi1(ve.getDi1());
					archivalvehicleevent.setDi2(ve.getDi2());
					archivalvehicleevent.setDi3(ve.getDi3());
					archivalvehicleevent.setDi4(ve.getDi4());
					archivalvehicleevent.setId(archivalvehicleeventId);
					Archivalvehicleevent archivalvehicleeventchk = em.find(
							Archivalvehicleevent.class, archivalvehicleeventId);
					if (archivalvehicleeventchk == null) {
						em.persist(archivalvehicleevent);
						LOGGER.info("ArchivalVehicleevent Data Persisted");
					}
				} catch (Exception e) {
					LOGGER.error("Archival::Archivalvehicleevent::Exception occured"
							+ e);
					isPersist = false;
					return;
				}
				if (isPersist) {
					maxResults++;
					em.remove(ve);
					em.flush();
				}
			}
			CronArchival.records -= maxResults;
		} catch (Exception e) {
			LOGGER.error("Exception in Archival Class " + e);
		}
	}
}
