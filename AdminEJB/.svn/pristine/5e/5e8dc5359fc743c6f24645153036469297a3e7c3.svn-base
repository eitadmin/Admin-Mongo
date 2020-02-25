package com.eiw.cron.archival;

import javax.persistence.EntityManager;

import org.jboss.logging.Logger;

public class ArchivalVehicleSummary {

	private EntityManager em;
	private static final Logger LOGGER = Logger.getLogger("archival");

	public ArchivalVehicleSummary(EntityManager em1, String timeZone) {
		// this.em = em1;
		// Calendar calendar = TimeZoneUtil.getDateForTimeZones(new Date(),
		// timeZone);
		// SimpleDateFormat simpleDateFormat = new
		// SimpleDateFormat("yyyy-MM-dd");
		// calendar.add(Calendar.MONTH, -3);
		// Date archivalDate = calendar.getTime();
		// try {
		// if (false) {
		// return;
		// }
		// LOGGER.info("Before Query Execution For Archival Vehicle Summary");
		// String strArchVehSumm =
		// "SELECT vcs from Vehiclecompletesummary vcs where eventTimeStamp < :date";
		// Query qArchVehSumm = em.createQuery(strArchVehSumm);
		// qArchVehSumm.setParameter("date", archivalDate);
		// qArchVehSumm.setFirstResult(0);
		// qArchVehSumm.setMaxResults(5000);
		// List<Vehiclecompletesummary> resultList =
		// (List<Vehiclecompletesummary>) qArchVehSumm.getResultList();
		// LOGGER.info("After Query Execution For Archival Vehicle Summary");
		// for (Vehiclecompletesummary vcs : resultList) {
		// boolean isPersist;
		// try {
		// isPersist = true;
		// Archivalvehiclecompletesummary archivalvehiclecompletesummary = new
		// Archivalvehiclecompletesummary();
		// ArchivalvehiclecompletesummaryId archivalvehiclecompletesummaryId =
		// new ArchivalvehiclecompletesummaryId();
		// archivalvehiclecompletesummaryId.setArchivalBy("System");
		// archivalvehiclecompletesummaryId.setArchivalDt(new Date());
		// archivalvehiclecompletesummaryId.setVin(vcs.getId().getVin());
		// archivalvehiclecompletesummaryId.setEventTimeStamp(vcs.getId().getEventTimeStamp());
		// archivalvehiclecompletesummaryId.setStartLocation(vcs.getId().getStartLocation());
		// archivalvehiclecompletesummaryId.setStartedAt(vcs.getId().getStartedAt());
		// archivalvehiclecompletesummaryId.setDrivingDuration(vcs.getId().getDrivingDuration());
		// archivalvehiclecompletesummaryId.setStopLocation(vcs.getId().getStopLocation());
		// archivalvehiclecompletesummaryId.setStoppedAt(vcs.getId().getStoppedAt());
		// archivalvehiclecompletesummaryId.setStopDuration(vcs.getId().getStopDuration());
		// archivalvehiclecompletesummary.setId(archivalvehiclecompletesummaryId);
		// em.persist(archivalvehiclecompletesummary);
		// LOGGER.info("ArchivalVehiclecompletesummary Data Persisted");
		// } catch (Exception e) {
		// LOGGER.error("Archival::ArchivalVehiclecompletesummary::Exception occured"
		// + e);
		// isPersist = false;
		// return;
		// }
		// if (isPersist) {
		// em.remove(vcs);
		// em.flush();
		// }
		// }
		// } catch (Exception e) {
		// LOGGER.error("Exception in Archival Vehicle Summary Class " + e);
		// }
	}

}
