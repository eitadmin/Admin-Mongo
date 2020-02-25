package com.eiw.cron;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import com.eiw.alerts.AlertsEJBRemote;
import com.eiw.client.dto.ReportData;
import com.eiw.device.handler.MeiTrackDeviceHandler;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.bo.BOFactory;
import com.eiw.server.fleettrackingpu.Alertconfig;
import com.eiw.server.fleettrackingpu.Calendarevent;
import com.eiw.server.fleettrackingpu.Emailvalidation;
import com.eiw.server.fleettrackingpu.Vehiclealerts;
import com.eiw.server.fleettrackingpu.Vehiclecompletesummary;
import com.eiw.server.fleettrackingpu.VehiclecompletesummaryId;
import com.eiw.server.fleettrackingpu.Vehicleevent;

@Stateless
public class DispatcherEJB implements DispatcherEJBRemote {

	@PersistenceContext(unitName = "ltmsfleettrackingpu")
	private EntityManager em;

	@PersistenceContext(unitName = "ltmscompanyadminpu")
	private EntityManager emAdmin;

	@PersistenceContext(unitName = "studenttrackingpu")
	private EntityManager emStudentdetails;

	private static final Logger LOGGER = Logger.getLogger("cron");
	private static final String STR_DATE_CHK = "dateChk",
			STR_FROM_HR = "00:00:00", STR_TO_DATE_CHK = "todateChk",
			STR_TO_HR = "23:59:59";

	@Override
	public void dispatcher(String vin, String timeZone) {
		try {
			LOGGER.info("Entering dispatcher method vin = " + vin);
			vehicleSummaryBatch(vin, timeZone);
			LOGGER.info("Exiting dispatcher method vin = " + vin);
		} catch (Exception e) {
			LOGGER.error("Exception in dispatcher method " + e);
		}

	}

	@Override
	public List<String> vehicleList(String timeZone) {
		LOGGER.debug("Entering Dispatcher vehicleList method, Before Query Execution"
				+ "timezone" + timeZone);
		Query query = emAdmin
				.createQuery("SELECT v.vin FROM Companybranch cb,Vehicle v where v.companyId =cb.id.companyId "
						+ "and v.branchId = cb.id.branchId and cb.region =:branchSetting");
		query.setParameter("branchSetting", timeZone);
		List<String> listOfVehicles = (List<String>) query.getResultList();
		LOGGER.debug("Exiting Dispatcher vehicleList method, After Query Execution listOfVehicles="
				+ listOfVehicles.size());
		return listOfVehicles;
	}

	public void vehicleSummaryBatch(String vin, String timeZone) {
		LOGGER.info("DispatcherEJB::VehicleSummaryBatch::Entered into method::"
				+ "vin" + vin + "Timezone" + timeZone);

		try {

			String odometer = null, odometerWholeDay = null;

			SimpleDateFormat sdfTime = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");

			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

			Calendar calendar = TimeZoneUtil.getDateForTimeZones(new Date(),
					timeZone);
			calendar.add(Calendar.DATE, -1);

			Date vehicleSummaryDate = calendar.getTime();
			LOGGER.info("DispatcherEJB::VehicleSummaryBatch::Entering VehicleSummary Batch Report vin = "
					+ vin);

			boolean alreadyExist = chkVehiSumm(vin,
					sdfDate.format(vehicleSummaryDate));

			String dateChk = sdfDate.format(vehicleSummaryDate);

			if (!alreadyExist) {
				// logic for vehicle summary starts
				LOGGER.debug("DispatcherEJB::VehicleSummaryBatch::Vehiclecompletesummary Before Quey Execution");
				String strForDay = "select v from Vehicleevent v where v.id.vin=:vin and v.id.eventTimeStamp between :dateChk and :todateChk order by v.id.eventTimeStamp";
				Query queryStrForDay = em.createQuery(strForDay);
				queryStrForDay.setParameter("vin", vin);
				queryStrForDay.setParameter(STR_DATE_CHK,
						sdfTime.parse(dateChk + " " + STR_FROM_HR));
				queryStrForDay.setParameter(STR_TO_DATE_CHK,
						sdfTime.parse(dateChk + " " + STR_TO_HR));
				List<Vehicleevent> vehicleevents = (List<Vehicleevent>) queryStrForDay
						.getResultList();
				boolean isFirstStartRecordDay = true, isFirstStopRecordDay = false;
				List<ReportData> reportDatas = new ArrayList<ReportData>();
				ReportData reportData = new ReportData();
				String[] strAvgMaxMinSpeed = (getAvgMaxMinSpeed(vin,
						sdfDate.format(vehicleSummaryDate))).split(",");
				String[] strAvgMaxMinTempBattery = (getAvgMaxMinTempBattrey(
						vin, sdfDate.format(vehicleSummaryDate))).split(",");
				boolean isFirstStart = true;
				LOGGER.debug("DispatcherEJB::VehicleSummaryBatch::ReportDatas Preparation "
						+ vehicleevents.size() + " Vin" + vin);
				for (Vehicleevent vehicleeventSummary : vehicleevents) {
					if (isFirstStartRecordDay
							&& (vehicleeventSummary.getSpeed() > 0 || vehicleeventSummary
									.getEngine())) {
						try {

							// check for initial start time
							if (isFirstStart) {
								if (!(TimeZoneUtil
										.getDateTStr(vehicleeventSummary
												.getId().getEventTimeStamp()))
										.equalsIgnoreCase(STR_FROM_HR)) {
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
											+ " " + STR_FROM_HR);
									reportData.setRunTime(TimeZoneUtil
											.getStrDZ(vehicleeventSummary
													.getId()
													.getEventTimeStamp())
											+ " " + STR_FROM_HR);
									reportData.setLatitude(vehicleeventSummary
											.getLatitude());
									reportData.setLongitude(vehicleeventSummary
											.getLongitude());
									reportData.setEndDate(TimeZoneUtil
											.getStrDZ(vehicleeventSummary
													.getId()
													.getEventTimeStamp())
											+ " " + STR_FROM_HR);
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
							LOGGER.error("DispatcherEJB::VehicleSummaryBatch::vehiclesummary logic"
									+ e);
						}
					}
					if (isFirstStopRecordDay
							&& vehicleeventSummary.getSpeed() == 0
							&& !vehicleeventSummary.getEngine()) {
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
				LOGGER.debug("DispatcherEJB::VehicleSummaryBatch::Before ForLoop Vehiclecompletesummary");
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
				LOGGER.info("DispatcherEJB::VehicleSummaryBatch::Exiting VehicleSummary Batch Report vin = "
						+ vin);
			}
		} catch (Exception e) {
			LOGGER.error("DispatcherEJB::VehicleSummaryBatch::VehicleSummaryBatch Method Exception Error"
					+ e);
		}

	}

	public boolean chkVehiSumm(String vin, String date) {

		LOGGER.debug("DispatcherEJB::chkVehiSumm::Entering chkVehiSumm Method vin ="
				+ vin + " date = " + date);
		Query queryChkVehiSumm = em
				.createQuery("SELECT vcs.id.vin,vcs.id.eventTimeStamp FROM Vehiclecompletesummary vcs WHERE vcs.id.eventTimeStamp = '"
						+ date + "' AND vin = '" + vin + "'");
		LOGGER.info("DispatcherEJB::chkVehiSumm::Before Query execute::"
				+ queryChkVehiSumm);
		int size = queryChkVehiSumm.getResultList().size();
		LOGGER.info("DispatcherEJB::chkVehiSumm::After Query execute::"
				+ queryChkVehiSumm);
		LOGGER.debug("DispatcherEJB::chkVehiSumm::queryChkVehiSumm.getResultList().size() = "
				+ queryChkVehiSumm.getResultList().size());
		LOGGER.debug("DispatcherEJB::chkVehiSumm::Exiting chkVehiSumm Method vin ="
				+ vin + " date = " + date);
		if (size > 0) {
			return true;
		} else {
			return false;
		}
	}

	private String getAvgMaxMinSpeed(String vin, String date) {
		String returnVal = "0,0,0";
		try {
			LOGGER.debug("DispatcherEJB::Entering getAvgMaxMinSpeed Method vin = "
					+ vin + " date = " + date);
			String minSpeedQuery = "SELECT MAX(speed),AVG(speed),MIN(speed) FROM fleettrackingdb.vehicleevent t WHERE vin=:selectedVh AND eventTimeStamp between :dateChk and :todateChk and t.speed != 0";
			Query query2 = em.createNativeQuery(minSpeedQuery);
			query2.setParameter("selectedVh", vin);
			query2.setParameter(STR_DATE_CHK, date + " " + STR_FROM_HR);
			query2.setParameter(STR_TO_DATE_CHK, date + " " + STR_TO_HR);
			LOGGER.info("DispatcherEJB::getAvgMaxMinSpeed::Before Query execute::"
					+ query2);
			List<Object[]> speedList = query2.getResultList();
			LOGGER.info("DispatcherEJB::getAvgMaxMinSpeed::After query execute::"
					+ query2);
			Object[] obj1 = (Object[]) speedList.get(0);
			returnVal = ((obj1[1] == null) ? 0 : obj1[1].toString()) + ","
					+ ((obj1[0] == null) ? 0 : obj1[0].toString()) + ","
					+ ((obj1[2] == null) ? 0 : obj1[2].toString());
			LOGGER.debug("Exiting getAvgMaxMinSpeed Methodvin = " + vin
					+ " date = " + date);
		} catch (Exception e) {
			LOGGER.error("DispatcherEJB::getAvgMaxMinSpeed::Exception Occured in getAvgMaxMinSpeed"
					+ e);
		}
		LOGGER.info("DispatcherEJB::getAvgMaxMinSpeed::Leaving from this method");
		return returnVal;
	}

	private String getAvgMaxMinTempBattrey(String vin, String date) {
		String returnVal = "0,0,0,0,0,0,0,0,0,0,0,0";
		try {
			LOGGER.debug("DispatcherEJB::getAvgMaxMinTempBattrey::Entering into getAvgMaxMinTempBattrey Method vin = "
					+ vin + " date = " + date);
			String minSpeedQuery = "SELECT MAX(tempSensor1),AVG(tempSensor1),MIN(tempSensor1),MAX(tempSensor2),AVG(tempSensor2),MIN(tempSensor2),"
					+ "MAX(tempSensor3),AVG(tempSensor3),MIN(tempSensor3),MAX(battery),AVG(battery),MIN(battery) "
					+ "FROM fleettrackingdb.vehicleevent t WHERE vin=:vin AND eventTimeStamp between :date and :todate";
			Query query2 = em.createNativeQuery(minSpeedQuery);
			query2.setParameter("vin", vin);
			query2.setParameter("date", date + " " + STR_FROM_HR);
			query2.setParameter("todate", date + " " + STR_TO_HR);
			LOGGER.info("DispatcherEJB::getAvgMaxMinTempBattrey::before Query Execute::query"
					+ query2);
			List<Object[]> speedList = query2.getResultList();
			LOGGER.info("DispatcherEJB::getAvgMaxMinTempBattrey::after query execute::query::"
					+ query2);
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
			LOGGER.error("DispatcherEJB::getAvgMaxMinTempBattrey::Exception Occured in getAvgMaxMinTempBattrey"
					+ e);
		}
		return returnVal;
	}

	private String formatIntoHHMMSSWithOutDay(int secsIn) {
		int x = Math.abs(secsIn);
		int remainder = x % 86400;
		int hours = remainder / 3600, rem = remainder % 3600, minutes = rem / 60, seconds = rem % 60;
		return ((hours < 10 ? "0" : "") + hours + ":"
				+ (minutes < 10 ? "0" : "") + minutes + ":"
				+ (seconds < 10 ? "0" : "") + seconds);
	}

	public String getOdometer(String runTime, String stopTime, String vin) {
		String status = "";
		try {
			LOGGER.info("DispatcherEJB::Entering getOdometer Method runTime ="
					+ runTime + " stopTime=" + stopTime + " vin= " + vin);
			String odometer = "Select SUM(odometer) from fleettrackingdb.vehicleevent where vin=:vin and eventTimeStamp between :runTime and :stopTime";
			Query query = em.createNativeQuery(odometer);
			query.setParameter("vin", vin);
			query.setParameter("runTime", runTime);
			query.setParameter("stopTime", stopTime);
			BigDecimal odometerValue = (BigDecimal) query.getSingleResult();
			status = String.valueOf(odometerValue);
		} catch (Exception e) {
			LOGGER.error("DispatcherEJB::Exception in getOdometer Method" + e);
		}
		LOGGER.debug("DispatcherEJB::Exiting getOdometer Method runTime ="
				+ runTime + " stopTime=" + stopTime + " vin= " + vin);
		return status;
	}

	public String getOdometerWholeDay(String date, String vin) {
		String status = "";
		try {
			LOGGER.debug("DispatcherEJB::Entering getOdometerWholeDay Method date="
					+ date + " vin=" + vin);
			String odometer = "SELECT SUM(odometer) FROM fleettrackingdb.vehicleevent WHERE vin=:vin AND eventTimeStamp between :date and :todate";
			Query query = em.createNativeQuery(odometer);
			query.setParameter("vin", vin);
			query.setParameter("date", date + " " + STR_FROM_HR);
			query.setParameter("todate", date + " " + STR_TO_HR);
			BigDecimal odometerValue = (BigDecimal) query.getSingleResult();
			status = String.valueOf(odometerValue);
		} catch (Exception e) {
			LOGGER.error("DispatcherEJB::getOdometerWholeDay::Exception Occured "
					+ e);
		}
		LOGGER.debug("DispatcherEJB::Exiting getOdometerWholeDay Method date="
				+ date + " vin=" + vin);
		return status;
	}

	@Override
	public void getAlertConfigData() {
		try {
			if (!MeiTrackDeviceHandler.vehicleTripTimeMap.isEmpty()) {
				MeiTrackDeviceHandler.vehicleTripTimeMap.clear();
			}
			if (!MeiTrackDeviceHandler.stopCountMap.isEmpty()) {
				MeiTrackDeviceHandler.stopCountMap.clear();
			}
			LOGGER.debug("DispatcherEJB::Entering getAlertConfigData Method");
			Calendar calendar = TimeZoneUtil.getDateForTimeZones(new Date(),
					"Asia/Kolkata");
			Query query = em
					.createQuery("Select a from Alertconfig a WHERE a.alertStatus = 1 AND a.validityExp IS NOT NULL");
			List<Alertconfig> alertconfigs = query.getResultList();
			for (Alertconfig alertconfig : alertconfigs) {
				if (alertconfig.getValidityExp().before(calendar.getTime())) {
					alertconfig.setAlertStatus(0);
				}
			}
		} catch (Exception e) {
			LOGGER.error("DispatcherEJB::getAlertConfigData::Exception Occured "
					+ e);
		}
	}

	@Override
	public List<Alertconfig> fmList(String neighbourhoodreport) {
		try {
			Query query = em
					.createQuery("SELECT a from Alertconfig a where a.id.alertType=:neighbourhood");
			query.setParameter("neighbourhood", neighbourhoodreport);
			List<Alertconfig> listOfFM = (List<Alertconfig>) query
					.getResultList();
			System.out.println("list of FM size :" + listOfFM.size());
			return listOfFM;
		} catch (Exception e) {
			LOGGER.error("DispatcherEJB::fmList::Exception Occured " + e);
		}
		return null;
	}

	@Override
	public List<String> userHasActiveVins(String companyId, String userId,
			String branchId) {
		try {
			Query query = em
					.createNativeQuery("SELECT DISTINCT vhu.vin, v.plateNo FROM vehicle_has_user vhu,alertconfig ac,vehicle v,"
							+ "vehiclealerts va WHERE vhu.userId=:userId AND ac.companyId=:companyId AND "
							+ "ac.branchId=:branchId AND vhu.vin=v.vin AND va.vin=vhu.vin AND "
							+ "DATE(va.eventTimeStamp) = DATE_SUB(CURDATE(), INTERVAL 1 DAY)");
			query.setParameter("userId", userId);
			query.setParameter("companyId", companyId);
			query.setParameter("branchId", branchId);
			List<Object[]> listOfVins = (List<Object[]>) query.getResultList();
			LOGGER.info("list of vins size :" + listOfVins.size());
			List<String> result = new ArrayList<String>();
			for (int i = 0; i < listOfVins.size(); i++) {
				Object[] obj = (Object[]) listOfVins.get(i);
				result.add(obj[0] + "," + obj[1]);
			}
			return result;
		} catch (Exception e) {
			LOGGER.error("DispatcherEJB::userHasActiveVins::Exception Occured"
					+ e);
		}
		return null;
	}

	@Override
	public List<String> getUserMail() {
		try {
			List<String> userMail = new ArrayList<String>();
			LOGGER.info("Entering dispatcher method and Get USER and E-mailId");
			Query query = emAdmin
					.createNativeQuery("SELECT emailaddress,fax FROM user where userid='1'; ");
			List<Object[]> resulList = (List<Object[]>) query.getResultList();
			if (!resulList.isEmpty() || resulList.size() == 0) {
				for (int i = 0; i < resulList.size(); i++) {
					Object[] obj = (Object[]) resulList.get(i);
					String value = (String) obj[0] + "#" + (String) obj[1];
					userMail.add(value);
				}
				return userMail;
			} else {
				return null;
			}
		} catch (Exception e) {
			LOGGER.info("Entering dispatcher method and Get USER and E-mailId");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Object[]> getStudentData(String branchId) {
		try {

			LOGGER.info("Entering dispatcher method and Get Student");
			Query query = emStudentdetails
					.createNativeQuery("SELECT firstname,tagid from studentdetails where status='0' and branchid='"
							+ branchId + "' ;");
			List<Object[]> resulList = (List<Object[]>) query.getResultList();
			if (!resulList.isEmpty() || resulList.size() == 0) {
				return resulList;
			}
		} catch (Exception e) {
			LOGGER.error("Entering dispatcher method and Get StudentData");
			e.printStackTrace();
			return null;
		}
		return null;
	}

	@Override
	public List<Object[]> getMaitenanceRenewalData() {
		try {
			LOGGER.info("Entering Dispatcher method And Get Maintenance Renewal Data");
			Query query = em
					.createNativeQuery("select v.plateNo,ca.maintenanceType,ca.subject,ca.content,ca.fromDate,"
							+ " ca.toDate,ca.description,ca.emailId,ca.notificationType,ca.companyId,ca.vehicle_vin,ca.eventId"
							+ " FROM calendarevent ca inner join vehicle v on ca.vehicle_vin=v.vin "
							+ " where ca.status='Open' and ca.smsReminder='1' and  ca.alertBy <= curdate() and ca.todate >= curdate() ");
			List<Object[]> resulList = (List<Object[]>) query.getResultList();
			if (!resulList.isEmpty() || resulList.size() == 0) {
				return resulList;
			}
		} catch (Exception e) {
			LOGGER.error("Error dispatcher method and get Maitenance Renewal Data");
			e.printStackTrace();
			return null;
		}
		return null;
	}

	@Override
	public List<Object[]> getMaitenanceServiceData() {
		try {
			LOGGER.info("Entering Dispatcher method And get Maitenance Service Data");
			Query query = em
					.createNativeQuery("select v.plateNo,ca.maintenanceType,ca.subject,ca.content,ca.fromDate,"
							+ " ca.toDate,ca.description,ca.emailId,ca.notificationType,ca.companyId,ca.vehicle_vin,ca.eventId"
							+ " FROM calendarevent ca inner join vehicle v on ca.vehicle_vin=v.vin "
							+ " where ca.status='Open' and ca.smsReminder='0' and  ( ( ca.alertBy <= curdate() and ca.todate >= curdate() ) or ( ca.content > 0  and ca.content < 1000  ) ) ");
			List<Object[]> resulList = (List<Object[]>) query.getResultList();
			if (!resulList.isEmpty() || resulList.size() == 0) {
				return resulList;
			}
		} catch (Exception e) {
			LOGGER.error("Error dispatcher method and Get Maitenance Service Data");
			e.printStackTrace();
			return null;
		}
		return null;
	}

	@Override
	public void subOdometersInServiceData() {
		try {
			LOGGER.info("Entering Dispatcher method And Sub Odometers In Service Data");
			Query query = em
					.createNativeQuery("select  distinct vehicle_vin from calendarevent where smsReminder='0' and status='Open' and content > 0 ");
			List<String> resultList = (List<String>) query.getResultList();
			if (!resultList.isEmpty() && resultList.size() != 0) {
				String vin = "(";
				for (int i = 0; i < resultList.size(); i++) {
					String data = resultList.get(i);
					if (i < resultList.size() - 1)
						vin += "'" + data + "' ,";
					else
						vin += "'" + data + "')";
				}
				Query query1 = em
						.createNativeQuery("select vin,odometerPerDay from vehicle_has_odometer where  date(lastUpdDt)=curdate() and vin in  "
								+ vin);
				List<Object[]> resultList1 = (List<Object[]>) query1
						.getResultList();
				if (!resultList1.isEmpty() && resultList1.size() != 0) {
					for (int i = 0; i < resultList1.size(); i++) {
						Object[] data1 = resultList1.get(i);
						Query query2 = em
								.createNativeQuery("update  calendarevent  set content = content - "
										+ String.valueOf(data1[1])
										+ "  where smsReminder='0' and status='Open' and content >= 1000  and vehicle_vin='"
										+ String.valueOf(data1[0]) + "' ");
						query2.executeUpdate();
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error dispatcher Sub Odometers Methods In Service Data");
			e.printStackTrace();
		}
	}

	@Override
	public List<String> getUserId(String vin) {
		try {
			LOGGER.info("Entering Dispatcher method and Get UserId assinged to Vin");
			Query query = em
					.createNativeQuery("select userId from vehicle_has_user where vin='"
							+ vin + "'");
			List<String> resulList = (List<String>) query.getResultList();
			if (!resulList.isEmpty() || resulList.size() == 0) {
				return resulList;
			}
		} catch (Exception e) {
			LOGGER.error("Error Dispatcher method and Get UserId assinged to Vin");
			e.printStackTrace();
			return null;
		}
		return null;
	}

	public void setRunnedOutKms(String data) {
		try {
			LOGGER.info("Entering Dispatcher method and Set Runned Out To Kms Data");
			long eventId = Long.parseLong(data);
			Calendarevent calendarevent = em.find(Calendarevent.class, eventId);
			calendarevent.setContent("0");
			em.merge(calendarevent);
		} catch (Exception e) {
			LOGGER.info("Error Dispatcher method and Set Runned Out To Kms Data");

		}
	}

	public void persistVehicleAlerts(String vin, String maintenance,
			String maintenanceType, String description) {
		try {
			LOGGER.info("Entering Dispatcher method and persistVehicleAlerts");
			Vehiclealerts vehiclealerts = new Vehiclealerts();
			vehiclealerts.setVin(vin);
			AlertsEJBRemote alertsEJBRemote = BOFactory.getAlertsEJBRemote();
			String region = alertsEJBRemote.getTimeZoneRegion(vin);
			vehiclealerts.setEventTimeStamp(TimeZoneUtil.getDateTimeZone(
					new Date(), region));
			vehiclealerts.setSubalerttype(maintenanceType);
			vehiclealerts.setDescription(description);
			vehiclealerts.setAlerttype(maintenance);
			em.persist(vehiclealerts);
		} catch (Exception e) {
			LOGGER.info("Error Dispatcher method and persistVehicleAlerts");

		}

	}
	
	@Override
	public boolean isEmailValidOrNot(String mail) {
		boolean status = false;
		try {
			Emailvalidation validation = em.find(Emailvalidation.class, mail);
			if (validation != null && validation.getIsValid()) {
				status = true;
			} 
		} catch (Exception e) {
			LOGGER.error("Error in isEmailValidOrNot:: " + e);
			e.printStackTrace();
		}
		return status;
	}

}
