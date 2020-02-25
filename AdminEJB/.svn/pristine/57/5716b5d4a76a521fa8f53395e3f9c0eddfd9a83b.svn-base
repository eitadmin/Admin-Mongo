package com.eiw.cron.report;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import com.eiw.client.dto.ReportData;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.fleettrackingpu.Vehiclecompletesummary;
import com.eiw.server.fleettrackingpu.VehiclecompletesummaryId;
import com.eiw.server.fleettrackingpu.Vehicleevent;

public class VehicleSummaryBatch {

	private EntityManager em;
	private static final Logger LOGGER = Logger.getLogger("report");
	private static final String STR_TO_HR = "23:59:59";

	public VehicleSummaryBatch(EntityManager em1, String vin, String timeZone) {
		try {
			LOGGER.info("VehicleSummaryBatch::Entering into VehicleSummaryBatch class "
					+ "em1" + em1 + "vin" + vin + "timeZone" + timeZone);
			em = em1;

			String odometer = null, odometerWholeDay = null;

			SimpleDateFormat sdfTime = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");

			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

			Calendar calendar = TimeZoneUtil.getDateForTimeZones(new Date(),
					timeZone);
			calendar.add(Calendar.DATE, -1);

			Date vehicleSummaryDate = calendar.getTime();
			LOGGER.info("VehicleSummaryBatch::Entering VehicleSummary Batch Report vin = "
					+ vin);

			boolean alreadyExist = chkVehiSumm(vin,
					sdfDate.format(vehicleSummaryDate));

			if (alreadyExist) {
				// logic for vehicle summary starts
				LOGGER.debug("Vehiclecompletesummary Before Quey Execution");
				String strForDay = "select v from Vehicleevent v where v.id.vin=:vin and date(v.id.eventTimeStamp)=:eventTimeStamp order by v.id.eventTimeStamp";
				Query queryStrForDay = em.createQuery(strForDay);
				queryStrForDay.setParameter("vin", vin);
				queryStrForDay.setParameter("eventTimeStamp",
						vehicleSummaryDate);
				List<Vehicleevent> vehicleevents = (List<Vehicleevent>) queryStrForDay
						.getResultList();
				LOGGER.debug("VehicleSummaryBatch After Query Execution"
						+ queryStrForDay);
				boolean isFirstStartRecordDay = true, isFirstStopRecordDay = false;
				List<ReportData> reportDatas = new ArrayList<ReportData>();
				ReportData reportData = new ReportData();
				String[] strAvgMaxMinSpeed = (getAvgMaxMinSpeed(vin,
						sdfDate.format(vehicleSummaryDate))).split(",");
				String[] strAvgMaxMinTempBattery = (getAvgMaxMinTempBattrey(
						vin, sdfDate.format(vehicleSummaryDate))).split(",");
				boolean isFirstStart = true;
				LOGGER.debug("ReportDatas Preparation " + vehicleevents.size()
						+ " Vin" + vin);
				for (Vehicleevent vehicleeventSummary : vehicleevents) {
					if (isFirstStartRecordDay
							&& vehicleeventSummary.getSpeed() > 0) {
						try {

							// check for initial start time
							if (isFirstStart) {
								if (!(TimeZoneUtil
										.getDateTStr(vehicleeventSummary
												.getId().getEventTimeStamp()))
										.equalsIgnoreCase("00:00:00")) {
									isFirstStart = false;
									reportData = new ReportData();
									reportData.setHeaderTxt("Date=" + vin
											+ ";Avg Speed="
											+ strAvgMaxMinSpeed[0]
											+ ";Min Speed="
											+ strAvgMaxMinSpeed[2]
											+ ";Max Speed="
											+ strAvgMaxMinSpeed[1]);
									reportData.setStopTime(TimeZoneUtil
											.getStrDZ(vehicleeventSummary
													.getId()
													.getEventTimeStamp())
											+ " " + "00:00:00");
									reportData.setRunTime(TimeZoneUtil
											.getStrDZ(vehicleeventSummary
													.getId()
													.getEventTimeStamp())
											+ " " + "00:00:00");
									reportData.setLatitude(vehicleeventSummary
											.getLatitude());
									reportData.setLongitude(vehicleeventSummary
											.getLongitude());
									reportData.setEndDate(TimeZoneUtil
											.getStrDZ(vehicleeventSummary
													.getId()
													.getEventTimeStamp())
											+ " " + "00:00:00");
									String latStop = String
											.valueOf(vehicleeventSummary
													.getLatitude());
									String lngStop = String
											.valueOf(vehicleeventSummary
													.getLongitude());
									reportData.setSmsReminder(latStop + ","
											+ lngStop);
									reportData.setBatteryCurrent("");

									reportDatas.add(reportData);
								}
							}
							reportData = new ReportData();
							reportData.setHeaderTxt("Date=" + vin
									+ ";Avg Speed=" + strAvgMaxMinSpeed[0]
									+ ";Min Speed=" + strAvgMaxMinSpeed[2]
									+ ";Max Speed=" + strAvgMaxMinSpeed[1]);
							reportData.setRunTime(sdfTime
									.format(vehicleeventSummary.getId()
											.getEventTimeStamp()));
							reportData.setLatitude(vehicleeventSummary
									.getLatitude());
							reportData.setLongitude(vehicleeventSummary
									.getLongitude());
							reportData.setStartDate(TimeZoneUtil
									.getTimeINYYYYMMddssa(vehicleeventSummary
											.getId().getEventTimeStamp()));
							// find start address
							String latStart = String
									.valueOf(vehicleeventSummary.getLatitude());
							String lngStart = String
									.valueOf(vehicleeventSummary.getLongitude());
							reportData.setBatteryCurrent(latStart + ","
									+ lngStart);
							reportDatas.add(reportData);
							isFirstStartRecordDay = false;
							isFirstStopRecordDay = true;
						} catch (Exception e) {
							LOGGER.error("VehicleSummaryBatch::vehiclesummary logic Exception occured"
									+ e);
						}
					}
					if (isFirstStopRecordDay
							&& vehicleeventSummary.getSpeed() == 0) {
						int size = reportDatas.size();

						reportDatas.get(size - 1).setStopTime(
								sdfTime.format(vehicleeventSummary.getId()
										.getEventTimeStamp()));
						reportDatas.get(size - 1).setLatitude(
								vehicleeventSummary.getLatitude());
						reportDatas.get(size - 1).setLongitude(
								vehicleeventSummary.getLongitude());
						reportDatas
								.get(size - 1)
								.setEndDate(
										TimeZoneUtil
												.getTimeINYYYYMMddssa(vehicleeventSummary
														.getId()
														.getEventTimeStamp()));
						// find stop address
						String latStop = String.valueOf(vehicleeventSummary
								.getLatitude());
						String lngStop = String.valueOf(vehicleeventSummary
								.getLongitude());
						reportDatas.get(size - 1).setSmsReminder(
								latStop + "," + lngStop);

						isFirstStopRecordDay = false;
						isFirstStartRecordDay = true;
					}
				}
				int reportDataSize = reportDatas.size();
				for (int i = 0; i < reportDataSize; i++) {
					if (i == (reportDataSize - 1)) {
						long endTime;
						long startTime = sdfTime.parse(
								reportDatas.get(i).getRunTime()).getTime();
						if (reportDatas.get(i).getStopTime() == null) {
							endTime = sdfTime.parse(
									reportDatas.get(i).getRunTime()
											.substring(0, 10)
											+ " " + STR_TO_HR).getTime();
						} else {
							endTime = sdfTime.parse(
									reportDatas.get(i).getStopTime()).getTime();
						}
						reportDatas.get(i).setEndDate(
								reportDatas.get(i).getStartDate());
						long nextDateTime = sdfTime.parse(
								((reportDatas.get(i).getRunTime()).substring(0,
										10)) + " " + STR_TO_HR).getTime();
						int runDuration = (int) (endTime - startTime) / 1000;
						int stopDuration = (int) (nextDateTime - endTime) / 1000;
						// running duration is status
						reportDatas.get(i).setRunDur(
								formatIntoHHMMSSWithOutDay(runDuration));
						reportDatas.get(i).setStopDur(
								formatIntoHHMMSSWithOutDay(stopDuration));
					} else {

						long startTime;
						if (reportDatas.get(i).getRunTime() == null) {
							startTime = sdfTime.parse(
									((reportDatas.get(i).getStopTime())
											.substring(0, 10)) + " 00:00:00")
									.getTime();
						} else {
							startTime = sdfTime.parse(
									reportDatas.get(i).getRunTime()).getTime();
						}
						long endTime = sdfTime.parse(
								reportDatas.get(i).getStopTime()).getTime();
						long nextStartTime = sdfTime.parse(
								reportDatas.get(i + 1).getRunTime()).getTime();
						int runDuration = (int) (endTime - startTime) / 1000;
						int stopDuration = (int) (nextStartTime - endTime) / 1000;
						// running duration is status
						reportDatas.get(i).setRunDur(
								formatIntoHHMMSSWithOutDay(runDuration));
						reportDatas.get(i).setStopDur(
								formatIntoHHMMSSWithOutDay(stopDuration));
					}
				}
				int k = 0;
				LOGGER.debug("Before ForLoop Vehiclecompletesummary");
				odometerWholeDay = getOdometerWholeDay(
						sdfDate.format(vehicleSummaryDate), vin);

				for (ReportData reportData1 : reportDatas) {
					Vehiclecompletesummary vehiclecompletesummary = new Vehiclecompletesummary();
					VehiclecompletesummaryId vehiclecompletesummaryId = new VehiclecompletesummaryId();
					vehiclecompletesummaryId.setVin(vin);
					vehiclecompletesummaryId.setEventTimeStamp(sdfDate
							.format(vehicleSummaryDate));
					vehiclecompletesummaryId.setStartLocation(reportData1
							.getBatteryCurrent());
					vehiclecompletesummaryId.setStartedAt(reportData1
							.getRunTime());
					vehiclecompletesummaryId.setDrivingDuration(reportData1
							.getRunDur());
					vehiclecompletesummaryId.setStopLocation(reportData1
							.getSmsReminder());
					vehiclecompletesummaryId.setStoppedAt(reportData1
							.getStopTime());
					vehiclecompletesummaryId.setStopDuration(reportData1
							.getStopDur());

					if (reportData1.getRunTime() != null) {
						if (reportData1.getStopTime() != null) {
							odometer = getOdometer(reportData1.getRunTime(),
									reportData1.getStopTime(), vin);
						} else {

							odometer = getOdometer(reportData1.getRunTime(),
									(reportData1.getRunTime()).substring(0, 10)
											+ " " + STR_TO_HR, vin);
						}
					}

					vehiclecompletesummaryId.setIoDetails("AvgSpeed="
							+ strAvgMaxMinSpeed[0] + ";MinSpeed="
							+ strAvgMaxMinSpeed[2] + ";MaxSpeed="
							+ strAvgMaxMinSpeed[1] + ";Odometer=" + odometer
							+ ";MaxTemp1=" + strAvgMaxMinTempBattery[0]
							+ ";AvgTemp1=" + strAvgMaxMinTempBattery[1]
							+ ";MinTemp1=" + strAvgMaxMinTempBattery[2]
							+ ";MaxTemp2=" + strAvgMaxMinTempBattery[3]
							+ ";AvgTemp2=" + strAvgMaxMinTempBattery[4]
							+ ";MinTemp2=" + strAvgMaxMinTempBattery[5]
							+ ";MaxTemp3=" + strAvgMaxMinTempBattery[6]
							+ ";AvgTemp3=" + strAvgMaxMinTempBattery[7]
							+ ";MinTemp3=" + strAvgMaxMinTempBattery[8]
							+ ";MaxBatVolt=" + strAvgMaxMinTempBattery[9]
							+ ";AvgBatVolt=" + strAvgMaxMinTempBattery[10]
							+ ";MinBatVolt=" + strAvgMaxMinTempBattery[11]
							+ ";OdometerDay=" + odometerWholeDay);
					if (k == reportDatas.size() - 1) {
						if (reportDatas.get(k).getSmsReminder() != null) {
							vehiclecompletesummaryId
									.setStopLocation(reportDatas.get(k)
											.getSmsReminder());
						} else {
							vehiclecompletesummaryId.setStopLocation("");
						}
						if (reportDatas.get(k).getStopTime() != null) {
							vehiclecompletesummaryId.setStoppedAt(reportDatas
									.get(k).getStopTime());
						} else {
							vehiclecompletesummaryId.setStoppedAt((reportData1
									.getRunTime()).substring(0, 10)
									+ " "
									+ STR_TO_HR);
						}
					}
					vehiclecompletesummary.setId(vehiclecompletesummaryId);
					em.persist(vehiclecompletesummary);
					k++;
				}
				LOGGER.info("Exiting VehicleSummary Batch Report vin = " + vin);
			}
		} catch (Exception e) {
			LOGGER.error("VehicleSummaryBatch Method Exception occured" + e);
		}
	}

	private String getAvgMaxMinSpeed(String vin, String date) {
		String returnVal = "0,0,0";
		try {
			LOGGER.debug("Entering getAvgMaxMinSpeed Method vin = " + vin
					+ " date = " + date);
			String minSpeedQuery = "SELECT MAX(speed),AVG(speed),MIN(speed) FROM vehicleevent t WHERE vin=:selectedVh AND DATE(eventTimeStamp)=:dateChk and t.speed != 0";
			Query query2 = em.createNativeQuery(minSpeedQuery);
			query2.setParameter("selectedVh", vin);
			query2.setParameter("dateChk", date);
			LOGGER.info("Before Query Execute" + query2);
			List<Object[]> speedList = query2.getResultList();
			LOGGER.info("After Query execute" + query2);
			Object[] obj1 = (Object[]) speedList.get(0);
			returnVal = ((obj1[1] == null) ? 0 : obj1[1].toString()) + ","
					+ ((obj1[0] == null) ? 0 : obj1[0].toString()) + ","
					+ ((obj1[2] == null) ? 0 : obj1[2].toString());
			LOGGER.debug("Exiting getAvgMaxMinSpeed Methodvin = " + vin
					+ " date = " + date);
		} catch (Exception e) {
			LOGGER.error("VehicleSummaryBatch::getAvgMaxMinSpeed" + e);
		}
		LOGGER.info("VehicleSummaryBatch::getAvgMaxMinSpeed::Leaving method successfully");
		return returnVal;
	}

	private String getAvgMaxMinTempBattrey(String vin, String date) {
		String returnVal = "0,0,0,0,0,0,0,0,0,0,0,0";
		try {
			LOGGER.debug("VehicleSummaryBatch::Entering getAvgMaxMinTempBattrey Method vin = "
					+ vin + " date = " + date);
			String minSpeedQuery = "SELECT MAX(tempSensor1),AVG(tempSensor1),MIN(tempSensor1),MAX(tempSensor2),AVG(tempSensor2),MIN(tempSensor2),"
					+ "MAX(tempSensor3),AVG(tempSensor3),MIN(tempSensor3),MAX(battery),AVG(battery),MIN(battery) "
					+ "FROM vehicleevent t WHERE vin=:vin AND DATE(eventTimeStamp)=:date";
			Query query2 = em.createNativeQuery(minSpeedQuery);
			query2.setParameter("vin", vin);
			query2.setParameter("date", date);
			LOGGER.info("Before Query Execute" + query2);
			List<Object[]> speedList = query2.getResultList();
			LOGGER.info("After query Execute" + query2);
			Object[] obj1 = (Object[]) speedList.get(0);
			returnVal = ((obj1[0] == null) ? 0 : obj1[0].toString()) + ","
					+ ((obj1[1] == null) ? 0 : obj1[1].toString()) + ","
					+ ((obj1[2] == null) ? 0 : obj1[2].toString()) + ","
					+ ((obj1[3] == null) ? 0 : obj1[3].toString()) + ","
					+ ((obj1[4] == null) ? 0 : obj1[4].toString()) + ","
					+ ((obj1[5] == null) ? 0 : obj1[5].toString()) + ","
					+ ((obj1[6] == null) ? 0 : obj1[6].toString()) + ","
					+ ((obj1[7] == null) ? 0 : obj1[7].toString()) + ","
					+ ((obj1[8] == null) ? 0 : obj1[8].toString()) + ","
					+ ((obj1[9] == null) ? 0 : obj1[9].toString()) + ","
					+ ((obj1[10] == null) ? 0 : obj1[10].toString()) + ","
					+ ((obj1[11] == null) ? 0 : obj1[11].toString());
			LOGGER.debug("Exiting getAvgMaxMinTempBattrey Method vin = " + vin
					+ " date = " + date);
		} catch (Exception e) {
			LOGGER.error("VehicleSummaryBatch::getAvgMaxMinTempBattrey" + e);
		}
		LOGGER.info("VehicleSummaryBatch::getAvgMaxMinTempBattrey::Leaving from this method is successfully");
		return returnVal;
	}

	private String formatIntoHHMMSSWithOutDay(int secsIn) {
		int remainder = secsIn % 86400;
		int hours = remainder / 3600, rem = remainder % 3600, minutes = rem / 60, seconds = rem % 60;
		return ((hours < 10 ? "0" : "") + hours + ":"
				+ (minutes < 10 ? "0" : "") + minutes + ":"
				+ (seconds < 10 ? "0" : "") + seconds);
	}

	public String getOdometer(String runTime, String stopTime, String vin) {
		String status = "";
		try {
			LOGGER.debug("VehicleSummaryBatch::Entering getOdometer Method runTime ="
					+ runTime + " stopTime=" + stopTime + " vin= " + vin);
			String odometer = "Select SUM(odometer) from vehicleevent where vin=:vin and eventTimeStamp between :runTime and :stopTime";
			Query query = em.createNativeQuery(odometer);
			query.setParameter("vin", vin);
			query.setParameter("runTime", runTime);
			query.setParameter("stopTime", stopTime);
			LOGGER.info("Before Query execute" + query);
			BigDecimal odometerValue = (BigDecimal) query.getSingleResult();
			LOGGER.info("After query Execute" + query);
			status = String.valueOf(odometerValue);
		} catch (Exception e) {
			LOGGER.error("VehicleSummaryBatch::getOdometer Method Exception occured"
					+ e);
		}
		LOGGER.debug("VehicleSummaryBatch::Exiting getOdometer Method runTime ="
				+ runTime + " stopTime=" + stopTime + " vin= " + vin);
		return status;
	}

	public String getOdometerWholeDay(String date, String vin) {
		String status = "";
		try {
			LOGGER.debug("VehicleSummaryBatch::Entering getOdometerWholeDay Method date="
					+ date + " vin=" + vin);
			String odometer = "SELECT SUM(odometer) FROM vehicleevent WHERE vin=:vin AND DATE(eventTimeStamp)=:date";
			Query query = em.createNativeQuery(odometer);
			query.setParameter("vin", vin);
			query.setParameter("date", date);
			LOGGER.info("Before Query Execute");
			BigDecimal odometerValue = (BigDecimal) query.getSingleResult();
			LOGGER.info("After Query Execute");
			status = String.valueOf(odometerValue);
		} catch (Exception e) {
			LOGGER.error("VehicleSummaryBatch::getOdometerWholeDay Exception Occured"
					+ e);
		}
		LOGGER.debug("VehicleSummaryBatch::Exiting getOdometerWholeDay Method date="
				+ date + " vin=" + vin);
		return status;
	}

	public boolean chkVehiSumm(String vin, String date) {

		LOGGER.debug("VehicleSummaryBatch::Entering chkVehiSumm Method vin ="
				+ vin + " date = " + date);
		Query queryChkVehiSumm = em
				.createQuery("SELECT vcs.id.vin,vcs.id.eventTimeStamp FROM Vehiclecompletesummary vcs WHERE vcs.id.eventTimeStamp = '"
						+ date + "' AND vin = '" + vin + "'");
		int size = queryChkVehiSumm.getResultList().size();
		LOGGER.debug("queryChkVehiSumm.getResultList().size() = "
				+ queryChkVehiSumm.getResultList().size());
		LOGGER.debug("Exiting chkVehiSumm Method vin =" + vin + " date = "
				+ date);
		return (size > 0) ? false : true;
	}
}
