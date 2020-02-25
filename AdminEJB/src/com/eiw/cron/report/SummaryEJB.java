package com.eiw.cron.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eit.dcframework.server.bo.OSValidator;
import com.eiw.client.dto.ReportData;
import com.eiw.cron.AlertConfigEJB;
import com.eiw.server.AmazonSMTPMail;
import com.eiw.server.EmailSendHttpClient;
import com.eiw.server.SMSSendHttpClient;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.ZohoSMTPMail;
import com.eiw.server.companyadminpu.Provider;
import com.eiw.server.companyadminpu.Smsconfig;
import com.eiw.server.companyadminpu.SmsconfigId;
import com.eiw.server.companyadminpu.Smssent;
import com.eiw.server.fleettrackingpu.Alertconfig;
import com.eiw.server.fleettrackingpu.Assetsummaryreport;
import com.eiw.server.fleettrackingpu.AssetsummaryreportId;
import com.eiw.server.fleettrackingpu.Consolidatesummarybydayreport;
import com.eiw.server.fleettrackingpu.ConsolidatesummarybydayreportId;
import com.eiw.server.fleettrackingpu.Consolidatesummaryreport;
import com.eiw.server.fleettrackingpu.ConsolidatesummaryreportId;
import com.eiw.server.fleettrackingpu.Emailvalidation;
import com.eiw.server.fleettrackingpu.Employeesummarybyday;
import com.eiw.server.fleettrackingpu.EmployeesummarybydayId;
import com.eiw.server.fleettrackingpu.Neighbourhoodviolation;
import com.eiw.server.fleettrackingpu.Notransmissionoverride;
import com.eiw.server.fleettrackingpu.Overspeedsummaryreport;
import com.eiw.server.fleettrackingpu.OverspeedsummaryreportId;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.VehicleHasIo;
import com.eiw.server.fleettrackingpu.VehicleHasOdometer;
import com.eiw.server.fleettrackingpu.VehicleHasOdometerId;
import com.eiw.server.fleettrackingpu.Vehiclealerts;
import com.eiw.server.fleettrackingpu.Vehicleevent;
import com.eiw.server.fleettrackingpu.Vehiclesummarybyday;
import com.eiw.server.fleettrackingpu.VehiclesummarybydayId;
import com.eiw.server.studenttrackingpu.Alerttypes;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfDiv;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Stateless
public class SummaryEJB implements SummaryEJBRemote {

	// @EJB
	// DeviceStarterImpl DeviceStarter;

	@PersistenceContext(unitName = "ltmsfleettrackingpu")
	private EntityManager em;

	@PersistenceContext(unitName = "ltmscompanyadminpu")
	protected EntityManager emAdmin;

	@PersistenceContext(unitName = "studenttrackingpu")
	private EntityManager emStudent;

	String checkmins = "";
	private static final Logger LOGGER = Logger.getLogger("report");
	private static final String STR_FROM_HR = "00:00:00",
			STR_TO_HR = "23:59:59";
	private static final String NMBR_FORMAT = "#0.00";
	NumberFormat nmbrformat = new DecimalFormat(NMBR_FORMAT);
	private static final String START_TIME = "00:00:00", END_TIME = "23:59:59";
	private static final String EVENTTIMESTAMP = "eventTimeStamp",
			TO_EVENTTIMESTAMP = "toeventTimeStamp";
	private static final String ALERT_TYPE = "alertType",
			SUB_ALERT_TYPE = "subAlertType";
	private static final String DATE_YYYYMMDD = "yyyy-MM-dd";
	private static final String TIMEZONEID = "Asia/Riyadh";
	SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdfHHMMSS = new SimpleDateFormat("HH:mm:ss");
	SimpleDateFormat sdfDate = new SimpleDateFormat(DATE_YYYYMMDD);
	SimpleDateFormat df = new SimpleDateFormat("HH:mm");
	private static final String STR_IMMOVABLE = "Immovable",
			STR_DEEPSEA_GENERATOR = "DEEPSEA GENERATOR", STR_EMPTY = "---",
			COMP_ID = "companyId", CORP_ID = "corpId", KEY = "key",
			STR_UNION = "UNION";

	@Override
	public List<Object[]> vehicleList(String timeZone) {
		return vehicleList(timeZone, null);
	}

	@Override
	public List<Object[]> getTodayOdometer(String timeZone) {

		List<Object[]> listVehicleevents = null;
		try {
			LOGGER.info("SummaryEJB::vehicleList::Entered into method:: timezone"
					+ timeZone);
			Calendar calendar = TimeZoneUtil.getDateForTimeZones(new Date(),
					timeZone);
			calendar.add(Calendar.DATE, -1);
			String dt = TimeZoneUtil.getDate(calendar.getTime());
			Query query = null;

			query = em
					.createNativeQuery("SELECT "
							+ dt
							+ ",vin,SUM(odometer) FROM vehicleevent WHERE eventtimestamp BETWEEN :fromDate  AND :toDate GROUP BY vin;");
			query.setParameter("fromDate",
					sdfTime.parse(dt + " " + STR_FROM_HR));
			query.setParameter("toDate", sdfTime.parse(dt + " " + STR_TO_HR));
			listVehicleevents = (List<Object[]>) query.getResultList();
		} catch (Exception e) {
			LOGGER.error("SummaryEJB::vehicleList::VehicleList :: Exception occured"
					+ e);
			e.printStackTrace();
		}
		return listVehicleevents;
	}

	public List<Object[]> vehicleList(String timeZone, String mode) {
		List<Object[]> listVehicleevents = null;
		try {
			LOGGER.info("SummaryEJB::vehicleList::Entered into method:: timezone"
					+ timeZone);
			Calendar calendar = TimeZoneUtil.getDateForTimeZones(new Date(),
					timeZone);
			calendar.add(Calendar.DATE, -1);
			String dt = TimeZoneUtil.getDate(calendar.getTime());
			Query query = null;
			if (mode != null) {
				query = em
						.createNativeQuery("SELECT DATE(ve.eventTimeStamp),ve.vin,v.plateNo,v.companyId,v.branchId FROM vehicleevent ve ,vehicle v WHERE ve.eventTimeStamp "
								+ "BETWEEN  :fromDate AND :toDate AND ve.vin = v.vin AND v.companyId IN(SELECT cs.`company_companyId` FROM ltmscompanyadmindb.companysettings cs WHERE cs.`appsettings_key`='Neighbourhood') GROUP BY DATE(ve.eventTimeStamp),ve.vin");
			} else {
				query = em
						.createNativeQuery("SELECT DATE(ve.eventTimeStamp),ve.vin FROM vehicleevent ve ,vehicle v WHERE ve.eventTimeStamp "
								+ "BETWEEN  :fromDate AND :toDate AND ve.vin = v.vin GROUP BY DATE(ve.eventTimeStamp),ve.vin");
			}
			query.setParameter("fromDate",
					sdfTime.parse(dt + " " + STR_FROM_HR));
			query.setParameter("toDate", sdfTime.parse(dt + " " + STR_TO_HR));
			LOGGER.info("Before Execute Query query" + query);
			listVehicleevents = (List<Object[]>) query.getResultList();
			LOGGER.info("After Execute Query" + query);

		} catch (Exception e) {
			LOGGER.error("SummaryEJB::vehicleList::VehicleList :: Exception occured"
					+ e);
		}
		LOGGER.info("SummaryEJB::vehicleList::Leaving from this method successfully");
		return listVehicleevents;
	}

	public List<ReportData> getSummaryDayReport(String vin, String eventDate) {
		List<ReportData> reportDatas = new ArrayList<ReportData>();
		try {
			LOGGER.info("SummaryEJB::getSummaryDayReport::Entered into method::"
					+ "vin" + vin + "eventDate" + eventDate);
			String strForDay = "SELECT v FROM Vehicleevent v WHERE v.id.vin =:vin AND v.id.eventTimeStamp BETWEEN  CONCAT(:eventDate,' 00:00:00')  AND CONCAT(:eventDate,' 23:59:59') ORDER BY v.id.eventTimeStamp";
			Query queryStrForDay = em.createQuery(strForDay);
			queryStrForDay.setParameter("vin", vin);
			queryStrForDay.setParameter("eventDate", eventDate);
			LOGGER.info("before Execute Query" + "query" + queryStrForDay);
			List<Vehicleevent> vehicleevents = (List<Vehicleevent>) queryStrForDay
					.getResultList();
			LOGGER.info("after Execute Query" + "queryStrForDay"
					+ queryStrForDay);
			Vehicle vehicle = em.find(Vehicle.class, vin);
			String type = vehicle.getVehiclemodel().getRemarks();
			boolean isImmovable = false;
			if (type != null && type.equalsIgnoreCase(STR_IMMOVABLE))
				isImmovable = true;
			boolean isFirstStartRecordDay = true, isFirstStopRecordDay = true, isFirstIdleRecordDay = true, isFirstTowedRecordDay = true;
			ReportData reportData = null;
			boolean isFirstStart = true;
			for (int i = 0; i < vehicleevents.size(); i++) {
				if (isFirstStart) {
					reportData = new ReportData();
					long defaultStartValue = addy(STR_FROM_HR);
					long firstStartValue = addy(sdfHHMMSS.format(vehicleevents
							.get(i).getId().getEventTimeStamp()));
					int defaultStopValue = (int) (firstStartValue - defaultStartValue);
					reportData
							.setStopDur(formatIntoHHMMSSWithOutDay(defaultStopValue));
					reportDatas.add(reportData);
					if (!(TimeZoneUtil.getDateTStr(vehicleevents.get(i).getId()
							.getEventTimeStamp()))
							.equalsIgnoreCase(STR_FROM_HR)) {
						isFirstStart = false;
						reportData = new ReportData();
						if (isImmovable) {
							if (vehicleevents.get(i).getEngine()) {
								isFirstStartRecordDay = false;
								reportData.setRunTime(sdfHHMMSS
										.format(vehicleevents.get(i).getId()
												.getEventTimeStamp()));
							} else if (vehicleevents.get(i).getSpeed() > 0
									&& (!vehicleevents.get(i).getEngine())) {
								isFirstTowedRecordDay = false;
								reportData.setTowedTime(sdfHHMMSS
										.format(vehicleevents.get(i).getId()
												.getEventTimeStamp()));
							} else {
								if (vehicle
										.getVehicletype()
										.getVehicleType()
										.equalsIgnoreCase(STR_DEEPSEA_GENERATOR)) {
									if (!vehicleevents.get(i).getIoevent()
											.equalsIgnoreCase("{}")) {
										reportData.setIdleTime(sdfHHMMSS
												.format(vehicleevents.get(i)
														.getId()
														.getEventTimeStamp()));
										isFirstIdleRecordDay = false;
									} else {
										isFirstStopRecordDay = false;
										reportData.setStopTime(sdfHHMMSS
												.format(vehicleevents.get(i)
														.getId()
														.getEventTimeStamp()));
									}
								} else {
									isFirstStopRecordDay = false;
									reportData.setStopTime(sdfHHMMSS
											.format(vehicleevents.get(i)
													.getId()
													.getEventTimeStamp()));
								}
							}
							reportDatas.add(reportData);
						} else {
							if (vehicleevents.get(i).getSpeed() > 0
									&& vehicleevents.get(i).getEngine()) {
								isFirstStartRecordDay = false;
								reportData.setRunTime(sdfHHMMSS
										.format(vehicleevents.get(i).getId()
												.getEventTimeStamp()));
							} else if (vehicleevents.get(i).getSpeed() > 0
									&& (!vehicleevents.get(i).getEngine())) {
								isFirstTowedRecordDay = false;
								reportData.setTowedTime(sdfHHMMSS
										.format(vehicleevents.get(i).getId()
												.getEventTimeStamp()));
							} else if (vehicleevents.get(i).getSpeed() == 0
									&& vehicleevents.get(i).getEngine()) {
								reportData.setIdleTime(sdfHHMMSS
										.format(vehicleevents.get(i).getId()
												.getEventTimeStamp()));
								isFirstIdleRecordDay = false;
							} else {
								isFirstStopRecordDay = false;
								reportData.setStopTime(sdfHHMMSS
										.format(vehicleevents.get(i).getId()
												.getEventTimeStamp()));
							}
							reportDatas.add(reportData);
						}
					}
				} else {
					reportData = new ReportData();
					if (isImmovable) {
						if (isFirstStartRecordDay
								&& vehicleevents.get(i).getEngine()) {
							reportData.setRunTime(sdfHHMMSS
									.format(vehicleevents.get(i).getId()
											.getEventTimeStamp()));
							isFirstStartRecordDay = false;
							isFirstStopRecordDay = true;
							isFirstIdleRecordDay = true;
							isFirstTowedRecordDay = true;
							reportDatas.add(reportData);
						} else if (isFirstTowedRecordDay
								&& vehicleevents.get(i).getSpeed() > 0
								&& (!vehicleevents.get(i).getEngine())) {
							reportData.setTowedTime(sdfHHMMSS
									.format(vehicleevents.get(i).getId()
											.getEventTimeStamp()));
							isFirstStartRecordDay = true;
							isFirstStopRecordDay = true;
							isFirstIdleRecordDay = true;
							isFirstTowedRecordDay = false;
							reportDatas.add(reportData);
						} else if (isFirstStopRecordDay
								&& vehicleevents.get(i).getSpeed() == 0
								&& (!vehicleevents.get(i).getEngine())) {
							if (vehicle.getVehicletype().getVehicleType()
									.equalsIgnoreCase(STR_DEEPSEA_GENERATOR)) {
								if (!vehicleevents.get(i).getIoevent()
										.equalsIgnoreCase("{}")) {
									reportData.setIdleTime(sdfHHMMSS
											.format(vehicleevents.get(i)
													.getId()
													.getEventTimeStamp()));
									isFirstStartRecordDay = true;
									isFirstStopRecordDay = true;
									isFirstIdleRecordDay = false;
									isFirstTowedRecordDay = true;
								} else {
									reportData.setStopTime(sdfHHMMSS
											.format(vehicleevents.get(i)
													.getId()
													.getEventTimeStamp()));
									isFirstStartRecordDay = true;
									isFirstStopRecordDay = false;
									isFirstIdleRecordDay = true;
									isFirstTowedRecordDay = true;
									reportDatas.add(reportData);
								}
							} else {
								reportData.setStopTime(sdfHHMMSS
										.format(vehicleevents.get(i).getId()
												.getEventTimeStamp()));
								isFirstStartRecordDay = true;
								isFirstStopRecordDay = false;
								isFirstIdleRecordDay = true;
								isFirstTowedRecordDay = true;
								reportDatas.add(reportData);
							}
						}
					} else {
						if (isFirstStartRecordDay
								&& vehicleevents.get(i).getSpeed() > 0
								&& vehicleevents.get(i).getEngine()) {
							reportData.setRunTime(sdfHHMMSS
									.format(vehicleevents.get(i).getId()
											.getEventTimeStamp()));
							isFirstStartRecordDay = false;
							isFirstStopRecordDay = true;
							isFirstIdleRecordDay = true;
							isFirstTowedRecordDay = true;
							reportDatas.add(reportData);
						} else if (isFirstTowedRecordDay
								&& vehicleevents.get(i).getSpeed() > 0
								&& (!vehicleevents.get(i).getEngine())) {
							reportData.setTowedTime(sdfHHMMSS
									.format(vehicleevents.get(i).getId()
											.getEventTimeStamp()));
							isFirstStartRecordDay = true;
							isFirstStopRecordDay = true;
							isFirstIdleRecordDay = true;
							isFirstTowedRecordDay = false;
							reportDatas.add(reportData);
						} else if (isFirstIdleRecordDay
								&& vehicleevents.get(i).getEngine()
								&& vehicleevents.get(i).getSpeed() == 0) {
							reportData.setIdleTime(sdfHHMMSS
									.format(vehicleevents.get(i).getId()
											.getEventTimeStamp()));
							isFirstStartRecordDay = true;
							isFirstStopRecordDay = true;
							isFirstIdleRecordDay = false;
							isFirstTowedRecordDay = true;
							reportDatas.add(reportData);
						} else if (isFirstStopRecordDay
								&& vehicleevents.get(i).getSpeed() == 0
								&& (!vehicleevents.get(i).getEngine())) {
							reportData.setStopTime(sdfHHMMSS
									.format(vehicleevents.get(i).getId()
											.getEventTimeStamp()));
							isFirstStartRecordDay = true;
							isFirstStopRecordDay = false;
							isFirstIdleRecordDay = true;
							isFirstTowedRecordDay = true;
							reportDatas.add(reportData);
						}
					}
				}
			}
			int reportDataSize = reportDatas.size();

			for (int i = 0; i < reportDataSize; i++) {

				long startTime = 0, idleTime = 0, endTime = 0, towedTime = 0, nextStartValue = 0, nextStopValue = 0, nextIdleValue = 0, nextTowedValue = 0;
				int runDuration = 0, stopDuration = 0, idleDuration = 0, towedDuration = 0;
				if (i == reportDataSize - 1) {
					nextStartValue = addy(STR_TO_HR);
					nextStopValue = addy(STR_TO_HR);
					nextIdleValue = addy(STR_TO_HR);
					nextTowedValue = addy(STR_TO_HR);

					if (reportDatas.get(i).getRunTime() != null) {
						startTime = addy(reportDatas.get(i).getRunTime());
						if (nextStopValue > 0) {
							runDuration = (int) (nextStopValue - startTime);
						} else if (nextIdleValue > 0) {
							runDuration = (int) (nextIdleValue - startTime);
						} else if (nextTowedValue > 0) {
							runDuration = (int) (nextTowedValue - startTime);
						}
					}
					if (reportDatas.get(i).getTowedTime() != null) {
						towedTime = addy(reportDatas.get(i).getTowedTime());
						if (nextStopValue > 0) {
							towedDuration = (int) (nextStopValue - towedTime);
						} else if (nextIdleValue > 0) {
							towedDuration = (int) (nextIdleValue - towedTime);
						} else if (nextStartValue > 0) {
							towedDuration = (int) (nextStartValue - towedTime);
						}
					}
					if (reportDatas.get(i).getIdleTime() != null) {
						idleTime = addy(reportDatas.get(i).getIdleTime());
						if (nextStopValue > 0) {
							idleDuration = (int) (nextStopValue - idleTime);
						} else if (nextStartValue > 0) {
							idleDuration = (int) (nextStartValue - idleTime);
						} else if (nextTowedValue > 0) {
							idleDuration = (int) (nextTowedValue - idleTime);
						}
					}
					if (reportDatas.get(i).getStopTime() != null) {
						endTime = addy(reportDatas.get(i).getStopTime());
						if (nextIdleValue > 0) {
							stopDuration = (int) (nextIdleValue - endTime);
						} else if (nextStartValue > 0) {
							stopDuration = (int) (nextStartValue - endTime);
						} else if (nextTowedValue > 0) {
							stopDuration = (int) (nextTowedValue - endTime);
						}
					}
					reportDatas.get(i).setRunDur(
							formatIntoHHMMSSWithOutDay(runDuration));
					reportDatas.get(i).setStopDur(
							formatIntoHHMMSSWithOutDay(stopDuration));
					reportDatas.get(i).setIdleDur(
							formatIntoHHMMSSWithOutDay(idleDuration));
					reportDatas.get(i).setTowedDur(
							formatIntoHHMMSSWithOutDay(towedDuration));
				} else {

					if (reportDatas.get(i + 1).getRunTime() != null) {
						nextStartValue = addy(reportDatas.get(i + 1)
								.getRunTime());
					} else if (reportDatas.get(i + 1).getStopTime() != null) {
						nextStopValue = addy(reportDatas.get(i + 1)
								.getStopTime());
					} else if (reportDatas.get(i + 1).getIdleTime() != null) {
						nextIdleValue = addy(reportDatas.get(i + 1)
								.getIdleTime());
					} else if (reportDatas.get(i + 1).getTowedTime() != null) {
						nextTowedValue = addy(reportDatas.get(i + 1)
								.getTowedTime());
					}

					if (reportDatas.get(i).getRunTime() != null) {
						startTime = addy(reportDatas.get(i).getRunTime());
						if (nextStopValue > 0) {
							runDuration = (int) (nextStopValue - startTime);
							reportDatas.get(i).setRunDur(
									formatIntoHHMMSSWithOutDay(runDuration));
							continue;
						} else if (nextIdleValue > 0) {
							runDuration = (int) (nextIdleValue - startTime);
							reportDatas.get(i).setRunDur(
									formatIntoHHMMSSWithOutDay(runDuration));
							continue;
						} else if (nextTowedValue > 0) {
							runDuration = (int) (nextTowedValue - startTime);
							reportDatas.get(i).setRunDur(
									formatIntoHHMMSSWithOutDay(runDuration));
							continue;
						}
					}
					if (reportDatas.get(i).getTowedTime() != null) {
						towedTime = addy(reportDatas.get(i).getTowedTime());
						if (nextStopValue > 0) {
							towedDuration = (int) (nextStopValue - towedTime);
							reportDatas.get(i).setTowedDur(
									formatIntoHHMMSSWithOutDay(towedDuration));
							continue;
						} else if (nextIdleValue > 0) {
							towedDuration = (int) (nextIdleValue - towedTime);
							reportDatas.get(i).setTowedDur(
									formatIntoHHMMSSWithOutDay(towedDuration));
							continue;
						} else if (nextStartValue > 0) {
							towedDuration = (int) (nextStartValue - towedTime);
							reportDatas.get(i).setTowedDur(
									formatIntoHHMMSSWithOutDay(towedDuration));
							continue;
						}
					}
					if (reportDatas.get(i).getIdleTime() != null) {
						idleTime = addy(reportDatas.get(i).getIdleTime());
						if (nextStopValue > 0) {
							idleDuration = (int) (nextStopValue - idleTime);
							reportDatas.get(i).setIdleDur(
									formatIntoHHMMSSWithOutDay(idleDuration));
							continue;
						} else if (nextStartValue > 0) {
							idleDuration = (int) (nextStartValue - idleTime);
							reportDatas.get(i).setIdleDur(
									formatIntoHHMMSSWithOutDay(idleDuration));
							continue;
						} else if (nextTowedValue > 0) {
							idleDuration = (int) (nextTowedValue - idleTime);
							reportDatas.get(i).setIdleDur(
									formatIntoHHMMSSWithOutDay(idleDuration));
							continue;
						}
					}
					if (reportDatas.get(i).getStopTime() != null) {
						endTime = addy(reportDatas.get(i).getStopTime());
						if (nextIdleValue > 0) {
							stopDuration = (int) (nextIdleValue - endTime);
							reportDatas.get(i).setStopDur(
									formatIntoHHMMSSWithOutDay(stopDuration));
							continue;
						} else if (nextStartValue > 0) {
							stopDuration = (int) (nextStartValue - endTime);
							reportDatas.get(i).setStopDur(
									formatIntoHHMMSSWithOutDay(stopDuration));
							continue;
						} else if (nextTowedValue > 0) {
							stopDuration = (int) (nextTowedValue - endTime);
							reportDatas.get(i).setStopDur(
									formatIntoHHMMSSWithOutDay(stopDuration));
							continue;
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("SummaryEJB::getSummaryDayReport::Exception Occured:: "
					+ e);
		}
		return reportDatas;
	}

	private int addy(String durationString) {
		int hours;
		int minutes;
		int seconds;
		String[] durationArray = durationString.split(":");
		hours = (Integer.valueOf(durationArray[0])) * 3600;
		minutes = (Integer.valueOf(durationArray[1])) * 60;
		seconds = Integer.valueOf(durationArray[2]);
		return hours + minutes + seconds;
	}

	private String formatIntoHHMMSSWithOutDay(int secsIn) {
		int remainder = secsIn % 86400;
		int hours = remainder / 3600, rem = remainder % 3600, minutes = rem / 60, seconds = rem % 60;
		return ((hours < 10 ? "0" : "") + hours + ":"
				+ (minutes < 10 ? "0" : "") + minutes + ":"
				+ (seconds < 10 ? "0" : "") + seconds);
	}

	private String formatIntoHHMMSSWithDay(int secsIn) {
		int day = secsIn / 86400;
		int remainder = secsIn % 86400;
		int hours = remainder / 3600, rem = remainder % 3600, minutes = rem / 60, seconds = rem % 60;
		return (day + ":" + (hours < 10 ? "0" : "") + hours + ":"
				+ (minutes < 10 ? "0" : "") + minutes + ":"
				+ (seconds < 10 ? "0" : "") + seconds);
	}

	public long getOdometerPerDay(String vin, String eventDate) {
		long odometerValue = 0L;
		try {
			LOGGER.info("SummaryEJB::getOdometerPerDay::" + "vin" + vin
					+ "eventDate" + eventDate);
			Query query = em
					.createQuery("Select SUM(v.odometer) from Vehicleevent v where v.id.vin =:vin and v.id.eventTimeStamp BETWEEN  CONCAT(:eventDate,' 00:00:00')  AND CONCAT(:eventDate,' 23:59:59')");
			query.setParameter("vin", vin);
			query.setParameter("eventDate", eventDate);
			if (query.getSingleResult() != null) {
				LOGGER.info("Before Execute Query" + query);
				odometerValue = (Long) query.getSingleResult();
				LOGGER.info("After Execute Query" + query);
			}
		} catch (Exception e) {
			LOGGER.error("SummaryEJB::getOdometerPerDay::Exception occured : "
					+ e);
		}
		LOGGER.info("SummaryEJB::getOdometerPerDay::Leaving from this method successfully");
		return odometerValue;
	}

	@Override
	public int insertVehicleDaySummary(String vin, String eventDate,
			List<ReportData> reportDatas, long totOdometer) {
		LOGGER.info("SummaryEJB::insertVehicleDaySummary::Entered into this method"
				+ "vin"
				+ vin
				+ "eventDate"
				+ eventDate
				+ "reportDatas"
				+ reportDatas + "totOdometer" + totOdometer);
		int engineHoursPerDay = 0;
		try {
			int totRunDur = 0, totStopDur = 0, totIdleDur = 0, totTowedDur = 0;
			String runningDur, stopDuration, idleDuration, towedDuration;
			for (ReportData reData : reportDatas) {
				if (reData.getRunDur() != null) {
					totRunDur = totRunDur + addy(reData.getRunDur());
				}
				if (reData.getStopDur() != null) {
					totStopDur = totStopDur + addy(reData.getStopDur());
				}
				if (reData.getIdleDur() != null) {
					totIdleDur = totIdleDur + addy(reData.getIdleDur());
				}
				if (reData.getTowedDur() != null) {
					totTowedDur = totTowedDur + addy(reData.getTowedDur());
				}
			}
			runningDur = formatIntoHHMMSSWithOutDay(totRunDur);
			stopDuration = formatIntoHHMMSSWithOutDay(totStopDur);
			idleDuration = formatIntoHHMMSSWithOutDay(totIdleDur);
			towedDuration = formatIntoHHMMSSWithOutDay(totTowedDur);
			engineHoursPerDay = totRunDur + totIdleDur;
			Vehiclesummarybyday vehiclesummarybyday = new Vehiclesummarybyday();
			VehiclesummarybydayId id = new VehiclesummarybydayId();
			id.setVin(vin);
			id.setFromDate(eventDate);
			id.setRunningDur(runningDur);
			id.setStopDur(stopDuration);
			id.setIdleDur(idleDuration);
			id.setTowDur(towedDuration);
			id.setOdometer(String.valueOf(totOdometer / 1000));
			vehiclesummarybyday.setId(id);
			em.persist(vehiclesummarybyday);
		} catch (Exception e) {
			LOGGER.error("SummaryEJB::insertVehicleDaySummary::Exception occured in this method insertVehicleDaySummary "
					+ e);
		}
		LOGGER.info("SummaryEJB::insertVehicleDaySummary::Leaving from this method is successfully");
		return engineHoursPerDay;
	}

	@Override
	public void insertVehicleHasOdometer(String vin, long totalOdometerPerDay,
			long prevOdometer, int engineHoursPerDay, String prevEngineHour) {
		try {
			long totOdometer = (totalOdometerPerDay / 1000) + prevOdometer;
			VehicleHasOdometer odometer = new VehicleHasOdometer();
			VehicleHasOdometerId id = new VehicleHasOdometerId();
			id.setVin(vin);
			id.setLastUpdDt(TimeZoneUtil.getDateInTimeZone(new Date()));
			odometer.setId(id);
			odometer.setOdometerPerDay(String
					.valueOf(totalOdometerPerDay / 1000));
			odometer.setOdometer(String.valueOf(totOdometer));
			odometer.setLastUpdBy("SYSTEM");
			int prevEng = TimeZoneUtil.addyWithOutDay(prevEngineHour);
			int totEnghrs = engineHoursPerDay + prevEng;
			String totengineHour = formatIntoHHMMSSWithDay(totEnghrs);
			String engineHourPerDay = formatIntoHHMMSSWithOutDay(engineHoursPerDay);
			odometer.setEngineHoursPerDay(engineHourPerDay);
			odometer.setEngineHours(totengineHour);
			em.persist(odometer);
		} catch (Exception e) {
			LOGGER.error("SummaryEJB:: insertVehicleHasOdometer:: Exception occured "
					+ e);
			e.printStackTrace();
		}
	}

	@Override
	public Object[] getPreviousOdometerValue(String vin) {
		Object[] odometerValue = null;
		try {
			Query odometerQuery = em
					.createQuery("Select odometer,engineHours from VehicleHasOdometer vho where vho.id.vin=:vin AND vho.id.lastUpdDt=(Select MAX(vh.id.lastUpdDt) from VehicleHasOdometer vh where vh.id.vin=:vin)");
			odometerQuery.setParameter("vin", vin);
			List<Object[]> odometerValues = (List<Object[]>) odometerQuery
					.getResultList();
			if (odometerValues != null && odometerValues.size() > 0) {
				odometerValue = odometerValues.get(0);
			}
		} catch (PersistenceException e) {
			LOGGER.error("SummaryEJB:: getPreviousOdometerValue:: PersistenceException occured "
					+ e);
		} catch (Exception e) {
			LOGGER.error("SummaryEJB:: getPreviousOdometerValue:: Exception occured "
					+ e);
			e.printStackTrace();
		}
		return odometerValue;
	}

	@Override
	public List<Object> employeeList(String timeZone) {
		List<Object> listEmployeeevents = null;
		try {
			LOGGER.info("SummaryEJB::employeeList::Entered into method:: timezone"
					+ timeZone);
			// Calendar calendar = TimeZoneUtil.getDateForTimeZones(new Date(),
			// timeZone);
			// calendar.add(Calendar.DATE, -1);
			// String empdt = TimeZoneUtil.getDate(calendar.getTime());
			// Query query = em
			// .createNativeQuery("SELECT DATE(ve.eventTimeStamp),ve.vin FROM
			// vehicleevent ve ,vehicle v WHERE ve.eventTimeStamp "
			// +
			// "BETWEEN :fromDate AND :toDate AND ve.vin = v.vin AND
			// vehicletype_vehicleType = 'Employee' GROUP BY
			// DATE(ve.eventTimeStamp),ve.vin");
			// query.setParameter("fromDate",
			// sdfTime.parse(empdt + " " + STR_FROM_HR));
			// query.setParameter("toDate", sdfTime.parse(empdt + " " +
			// STR_TO_HR));
			Query query = em
					.createNativeQuery("SELECT vin FROM vehicle  WHERE  vehicletype_vehicleType = 'Employee' AND status IS NULL");

			LOGGER.info("Before Execute Query query" + query);
			listEmployeeevents = (List<Object>) query.getResultList();
			LOGGER.info("After Execute Query" + query);
		} catch (Exception e) {
			LOGGER.error("SummaryEJB::employeeList::employeeList :: Exception occured"
					+ e);
		}
		LOGGER.info("SummaryEJB::employeeList::Leaving from this method successfully");
		return listEmployeeevents;
	}

	@Override
	public List<ReportData> getEmployeeSummaryDayReport(String employeeId,
			String eventDate) {
		List<ReportData> empreportDatas = new ArrayList<ReportData>();
		Map<String, String> hashMap = new HashMap<String, String>();
		List<ReportData> reportDatas1 = new ArrayList<ReportData>();
		int day = 0;
		Calendar calendar = Calendar.getInstance(TimeZone
				.getTimeZone(TIMEZONEID));
		day = calendar.get(Calendar.DAY_OF_WEEK);
		if (day == 1) {
			day = 7;
		} else {
			day = day - 1;
		}
		LOGGER.info("Current Day:" + day);
		LOGGER.info("SummaryEJB::getEmployeeSummaryDayReport::Entered into method::"
				+ "vin" + employeeId + "eventDate" + eventDate);
		// String shiftForDay =
		// "SELECT sd.shiftStartTime, sd.shiftEndTime, sd.shiftId FROM
		// employeehasshift ems LEFT JOIN fleettrackingdb.shiftdetails sd ON
		// ems.shiftId = sd.shiftId WHERE ems.empId =:vin AND
		// DATE(ems.effFrom)<= :eventDate AND CASE WHEN DATE(effTill) IS NULL
		// THEN DATE(CURDATE()) ELSE DATE(effTill) END >= :eventDate";
		String shiftForDay = "SELECT sd.shiftStartTime, sd.shiftEndTime, sd.shiftId, v.restday FROM vehicle v  LEFT JOIN fleettrackingdb.employeehasshift ems ON v.vin = ems.empId LEFT JOIN fleettrackingdb.shiftdetails sd ON ems.shiftId = sd.shiftId WHERE v.vin =:vin AND  DATE(ems.effFrom)<= :eventDate AND CASE WHEN DATE(effTill) IS NULL THEN DATE(CURDATE()) ELSE DATE(effTill) END >= :eventDate";
		Query queryShiftForDay = em.createNativeQuery(shiftForDay);
		queryShiftForDay.setParameter("vin", employeeId);
		queryShiftForDay.setParameter("eventDate", eventDate);
		LOGGER.info("before Execute Query" + "query" + queryShiftForDay);
		List<Object[]> shifttimes = (List<Object[]>) queryShiftForDay
				.getResultList();
		LOGGER.info("after Execute Query" + "queryStrForDay" + queryShiftForDay);

		for (int i = 0; i < shifttimes.size(); i++) {
			try {
				int restDay;
				boolean flag = false;
				boolean tag = false;
				ReportData reportdata2 = null;
				int zoneout = 0, totalRunDur = 0, totalIdleDur = 0, runDuration = 0, idleDuration = 0, outzodezone = 0;
				String shiftSTime = "", shiftETime = "", onenterTime = "", onleaveTime = "", vehleave = "", vehenter = "", etime = "", ltime = "";
				String entag = "", letag = "";
				String diffTimeOE = "", diffTimeOL = "", lastLeave = "";
				Date enteralerttime, leavealerttime, empstimestamp;
				long entermillitime = 0, leavemillitime = 0, diffsecOE = 0, diffsecOL = 0, lastLeavefinal = 0;
				// long firstenter = 0, firstleave = 0;
				int totaltime1 = 0, totaltime = 0, finalEleminateOE = 0, finalEleminateOL = 0;
				List<Vehicleevent> finaltimelist = new ArrayList<Vehicleevent>();
				// ReportData reportdata;
				Object[] row = (Object[]) shifttimes.get(i);
				String fd = eventDate;
				shiftSTime = String.valueOf(row[0]);
				shiftETime = String.valueOf(row[1]);
				restDay = (int) row[3];
				long shiftId = Long.parseLong(String.valueOf(row[2]));
				long empfdate = sdfTime.parse(eventDate + " " + shiftSTime)
						.getTime();
				long emptdate = sdfTime.parse(eventDate + " " + shiftETime)
						.getTime();
				totaltime1 = (int) (emptdate - empfdate) / 1000;

				// String shiftSTime1 = String.valueOf(shiftSTime + ","
				// + "fromDate");
				// String shiftSTime = addmin(shiftSTime1);
				// String empendtime1 = String
				// .valueOf(shiftETime + "," + "toDate");
				// String empendtime = addmin(empendtime1);
				checkmins = timeCompare(shiftSTime, shiftETime);
				String strForDay = "select va from Vehiclealerts va where va.id.vin=:vin and va.id.subalerttype IS NOT NULL and va.id.eventTimeStamp between :eventTimeStamp and :toeventTimeStamp order by va.id.eventTimeStamp, va.id.alerttype ASC";
				// String strForDay =
				// "SELECT v FROM Vehicle v, Vehiclealerts va WHERE v.vin =
				// va.id.vin AND va.id.subalerttype IS NOT NULL AND
				// va.id.eventTimeStamp BETWEEN :eventTimeStamp AND
				// :toeventTimeStamp ORDER BY va.id.eventTimeStamp,
				// va.id.alerttype ASC";
				Query queryStrForDay = em.createQuery(strForDay);
				queryStrForDay.setParameter("vin", employeeId);
				if (checkmins.equalsIgnoreCase("0")
						|| checkmins.equalsIgnoreCase("-1")) {
					String fromDate = fd + " " + shiftSTime;
					String toDate1 = fd + " " + shiftETime;
					queryStrForDay.setParameter("vin", employeeId);
					queryStrForDay.setParameter(EVENTTIMESTAMP,
							sdfTime.parse(fromDate));
					queryStrForDay.setParameter(TO_EVENTTIMESTAMP,
							sdfTime.parse(toDate1));
					LOGGER.debug(" Before query Execute Query "
							+ queryStrForDay);
				} else if (checkmins.equalsIgnoreCase("1")) {
					String fromDate = fd + " " + shiftSTime;
					String toDate1 = fd;
					Calendar cal = Calendar.getInstance();
					Date d = (Date) sdfDate.parse(toDate1);
					cal.setTime(d);
					cal.add(Calendar.DATE, 1);
					Date date2 = cal.getTime();
					toDate1 = sdfDate.format(date2) + " " + shiftETime;
					queryStrForDay.setParameter("vin", employeeId);
					queryStrForDay.setParameter(EVENTTIMESTAMP,
							sdfTime.parse(fromDate));
					queryStrForDay.setParameter(TO_EVENTTIMESTAMP,
							sdfTime.parse(toDate1));
					LOGGER.debug(" Before query Execute Query "
							+ queryStrForDay);
				}

				List<Vehiclealerts> vehiclealerts = (List<Vehiclealerts>) queryStrForDay
						.getResultList();
				LOGGER.debug(" Before query Execute Query "
						+ vehiclealerts.size());
				reportdata2 = new ReportData();
				if (!vehiclealerts.isEmpty() && vehiclealerts.size() != 0) {
					String Latsalerttype = vehiclealerts.get(
							vehiclealerts.size() - 1).getAlerttype();
					for (int n = 0; n < vehiclealerts.size(); n++) {

						String alerttype1 = vehiclealerts.get(n).getAlerttype();
						if (alerttype1.equalsIgnoreCase("ONLEAVE")) {
							zoneout++;
						}
						if (n == 0
								&& vehiclealerts.get(0).getAlerttype()
										.equalsIgnoreCase("ONLEAVE")) {
							vehenter = "true";
							vehleave = "true";
							etime = eventDate + " " + shiftSTime;
							ltime = sdfTime.format(vehiclealerts.get(n)
									.getEventTimeStamp());
						} else {
							if (alerttype1.equalsIgnoreCase("ONENTER")) {
								vehenter = "true";
								etime = sdfTime.format(vehiclealerts.get(n)
										.getEventTimeStamp());

							} else if (alerttype1.equalsIgnoreCase("ONLEAVE")) {
								vehleave = "true";
								ltime = sdfTime.format(vehiclealerts.get(n)
										.getEventTimeStamp());
							}
						}

						if (vehenter.equalsIgnoreCase("true")
								&& vehleave.equalsIgnoreCase("true")) {
							vehenter = "false";
							vehleave = "false";
							long firstenter = sdfTime.parse(etime).getTime();
							long firstleave = sdfTime.parse(ltime).getTime();
							long test = firstleave - firstenter;
							int insideZone = (int) (firstleave - firstenter) / 1000;
							totaltime += insideZone;
							tag = true;
						}
					}

					if (!vehleave.equalsIgnoreCase("true")
							&& Latsalerttype.equalsIgnoreCase("ONENTER")) {
						vehleave = "true";
						ltime = eventDate + " " + shiftETime;
						long firstenter = sdfTime.parse(etime).getTime();
						long firstleave = sdfTime.parse(ltime).getTime();
						int insideZone = (int) (firstleave - firstenter) / 1000;
						totaltime += insideZone;
						tag = true;
					}

					if (tag == true) {
						// reportdata = new ReportData();
						// long empfdate = sdfTime.parse(
						// eventDate + " " + shiftSTime).getTime();
						// long emptdate = sdfTime.parse(
						// eventDate + " " + shiftETime).getTime();
						// totaltime1 = (int) (emptdate - empfdate) / 1000;
						outzodezone = totaltime1 - totaltime;

						String eventForDay = "SELECT v FROM Vehicleevent v WHERE v.id.vin =:vin AND v.id.eventTimeStamp BETWEEN  :eventTimeStamp  AND :toeventTimeStamp ORDER BY v.id.eventTimeStamp";
						Query queryEventForDay = em.createQuery(eventForDay);
						queryEventForDay.setParameter("vin", employeeId);
						queryEventForDay.setParameter(EVENTTIMESTAMP,
								sdfTime.parse(eventDate + " " + shiftSTime));
						queryEventForDay.setParameter(TO_EVENTTIMESTAMP,
								sdfTime.parse(eventDate + " " + shiftETime));
						LOGGER.info("before Execute Query" + "query"
								+ queryEventForDay);
						List<Vehicleevent> employeeevents = (List<Vehicleevent>) queryEventForDay
								.getResultList();
						String Leventalerttype = vehiclealerts.get(
								vehiclealerts.size() - 1).getAlerttype();
						String flage = "", prvflag = "";
						int curcnt = 0, prvcnt = 0, runcnt = 0, idlecnt = 0;
						int diffcnt = 1;
						for (int l = 0; l < vehiclealerts.size(); l++) {
							flage = "";
							prvflag = "";
							curcnt = 1;
							prvcnt = 1;
							runcnt = 1;
							idlecnt = 1;
							String alerttype = vehiclealerts.get(l)
									.getAlerttype();
							if (l == 0
									&& vehiclealerts.get(0).getAlerttype()
											.equalsIgnoreCase("ONLEAVE")) {
								enteralerttime = sdfTime.parse(eventDate + " "
										+ shiftSTime);
								entermillitime = enteralerttime.getTime() / 1000;
								leavealerttime = sdfTime.parse(TimeZoneUtil
										.getTimeINYYYYMMddss(vehiclealerts.get(
												l).getEventTimeStamp()));
								leavemillitime = leavealerttime.getTime() / 1000;
							} else {
								if (entermillitime == 0
										&& alerttype
												.equalsIgnoreCase("ONENTER")) {
									enteralerttime = sdfTime
											.parse(TimeZoneUtil
													.getTimeINYYYYMMddss(vehiclealerts
															.get(l)
															.getEventTimeStamp()));
									entermillitime = enteralerttime.getTime() / 1000;

								} else if (leavemillitime == 0
										&& alerttype
												.equalsIgnoreCase("ONLEAVE")) {
									leavealerttime = sdfTime
											.parse(TimeZoneUtil
													.getTimeINYYYYMMddss(vehiclealerts
															.get(l)
															.getEventTimeStamp()));
									leavemillitime = leavealerttime.getTime() / 1000;
								}
							}

							if (entermillitime != 0 && leavemillitime != 0) {
								finaltimelist.clear();
								for (int m = 0; m < employeeevents.size(); m++) {
									empstimestamp = sdfTime.parse(TimeZoneUtil
											.getTimeINYYYYMMddss(employeeevents
													.get(m).getId()
													.getEventTimeStamp()));
									long empmilitime = empstimestamp.getTime() / 1000;
									if (entermillitime <= empmilitime
											&& leavemillitime >= empmilitime) {

										finaltimelist
												.add(employeeevents.get(m));
										flag = true;
									}
								}
								if (flag == true) {
									flag = false;
									LOGGER.info("after Execute Query"
											+ "queryStrForDay"
											+ queryEventForDay);
									entermillitime = 0;
									leavemillitime = 0;
									String lastTime = sdfHHMMSS
											.format(finaltimelist
													.get(finaltimelist.size() - 1)
													.getId()
													.getEventTimeStamp());
									for (int j = 0; j < finaltimelist.size(); j++) {
										prvflag = flage;
										if (finaltimelist.get(j).getEngine()) {
											flage = "Running";
											if (prvflag
													.equalsIgnoreCase("Idle")) {
												prvcnt = curcnt;
												hashMap.put(
														"End IdleTime"
																+ diffcnt
																+ prvcnt,
														sdfTime.format(finaltimelist
																.get(j)
																.getId()
																.getEventTimeStamp()));
												curcnt++;
												idlecnt = 1;
											}
											if (runcnt == 1) {
												hashMap.put(
														"Start RunTime"
																+ diffcnt
																+ curcnt,
														sdfTime.format(finaltimelist
																.get(j)
																.getId()
																.getEventTimeStamp()));
												runcnt++;
											}
										}

										if (!finaltimelist.get(j).getEngine()) {
											flage = "Idle";

											if (prvflag
													.equalsIgnoreCase("Running")) {
												prvcnt = curcnt;
												hashMap.put(
														"End RunTime" + diffcnt
																+ prvcnt,
														sdfTime.format(finaltimelist
																.get(j)
																.getId()
																.getEventTimeStamp()));
												curcnt++;
												runcnt = 1;
											}
											if (idlecnt == 1) {
												hashMap.put(
														"Start IdleTime"
																+ diffcnt
																+ curcnt,
														sdfTime.format(finaltimelist
																.get(j)
																.getId()
																.getEventTimeStamp()));
												idlecnt++;
											}
										}
									}
									int reportDataSize = hashMap.size();
									for (int n = 1; n <= reportDataSize; n++) {
										String SRuntime = "", ERuntime = "", SIdletime = "", EIdletime = "";
										// reportData = new ReportData();
										if (hashMap.get("Start RunTime"
												+ diffcnt + n) != null) {
											SRuntime = hashMap
													.get("Start RunTime"
															+ diffcnt + n);
											ERuntime = hashMap
													.get("End RunTime"
															+ diffcnt + n);
										}
										if (hashMap.get("Start IdleTime"
												+ diffcnt + n) != null) {
											SIdletime = hashMap
													.get("Start IdleTime"
															+ diffcnt + n);
											EIdletime = hashMap
													.get("End IdleTime"
															+ diffcnt + n);
										}
										if (SRuntime != null && SRuntime != "") {
											long endRuntime = 0;
											long startRuntime = sdfTime.parse(
													SRuntime).getTime();
											if (ERuntime != null) {
												endRuntime = sdfTime.parse(
														ERuntime).getTime();
											} else {
												ERuntime = eventDate + " "
														+ lastTime;
												endRuntime = sdfTime.parse(
														ERuntime).getTime();
											}
											runDuration += (int) (endRuntime - startRuntime) / 1000;
										}
										if (SIdletime != null
												&& SIdletime != "") {
											long endIdletime = 0;
											long startIdletime = sdfTime.parse(
													SIdletime).getTime();
											if (EIdletime != null) {
												endIdletime = sdfTime.parse(
														EIdletime).getTime();
											} else {
												EIdletime = eventDate + " "
														+ lastTime;
												endIdletime = sdfTime.parse(
														EIdletime).getTime();
											}
											idleDuration += (int) (endIdletime - startIdletime) / 1000;
										}
									}
									diffcnt++;
									hashMap.clear();
								}
							}
						}
						// Do not get Leave alert
						if (Leventalerttype.equalsIgnoreCase("ONENTER")
								&& entermillitime != 0 && leavemillitime == 0) {
							leavealerttime = sdfTime.parse(eventDate + " "
									+ shiftETime);
							leavemillitime = leavealerttime.getTime() / 1000;

							finaltimelist.clear();
							for (int m = 0; m < employeeevents.size(); m++) {
								empstimestamp = sdfTime.parse(TimeZoneUtil
										.getTimeINYYYYMMddss(employeeevents
												.get(m).getId()
												.getEventTimeStamp()));
								long empmilitime = empstimestamp.getTime() / 1000;
								if (entermillitime <= empmilitime
										&& leavemillitime >= empmilitime) {

									finaltimelist.add(employeeevents.get(m));
									flag = true;
								}
							}
							if (flag == true) {
								flag = false;
								LOGGER.info("after Execute Query"
										+ "queryStrForDay" + queryEventForDay);
								entermillitime = 0;
								leavemillitime = 0;
								String lastTime = sdfHHMMSS
										.format(finaltimelist
												.get(finaltimelist.size() - 1)
												.getId().getEventTimeStamp());
								for (int j = 0; j < finaltimelist.size(); j++) {
									prvflag = flage;
									if (finaltimelist.get(j).getEngine()) {
										flage = "Running";
										if (prvflag.equalsIgnoreCase("Idle")) {
											prvcnt = curcnt;
											hashMap.put(
													"End IdleTime" + diffcnt
															+ prvcnt,
													sdfTime.format(finaltimelist
															.get(j)
															.getId()
															.getEventTimeStamp()));
											curcnt++;
											idlecnt = 1;
										}
										if (runcnt == 1) {
											hashMap.put(
													"Start RunTime" + diffcnt
															+ curcnt,
													sdfTime.format(finaltimelist
															.get(j)
															.getId()
															.getEventTimeStamp()));
											runcnt++;
										}
									}

									if (!finaltimelist.get(j).getEngine()) {
										flage = "Idle";

										if (prvflag.equalsIgnoreCase("Running")) {
											prvcnt = curcnt;
											hashMap.put(
													"End RunTime" + diffcnt
															+ prvcnt,
													sdfTime.format(finaltimelist
															.get(j)
															.getId()
															.getEventTimeStamp()));
											curcnt++;
											runcnt = 1;
										}
										if (idlecnt == 1) {
											hashMap.put(
													"Start IdleTime" + diffcnt
															+ curcnt,
													sdfTime.format(finaltimelist
															.get(j)
															.getId()
															.getEventTimeStamp()));
											idlecnt++;
										}
									}

								}
								int reportDataSize = hashMap.size();
								for (int n = 1; n <= reportDataSize; n++) {
									String SRuntime = "", ERuntime = "", SIdletime = "", EIdletime = "";
									// reportData = new ReportData();
									if (hashMap.get("Start RunTime" + diffcnt
											+ n) != null) {
										SRuntime = hashMap.get("Start RunTime"
												+ diffcnt + n);
										ERuntime = hashMap.get("End RunTime"
												+ diffcnt + n);
									}
									if (hashMap.get("Start IdleTime" + diffcnt
											+ n) != null) {
										SIdletime = hashMap
												.get("Start IdleTime" + diffcnt
														+ n);
										EIdletime = hashMap.get("End IdleTime"
												+ diffcnt + n);
									}
									if (SRuntime != null && SRuntime != "") {
										long endRuntime = 0;
										long startRuntime = sdfTime.parse(
												SRuntime).getTime();
										if (ERuntime != null) {
											endRuntime = sdfTime
													.parse(ERuntime).getTime();
										} else {
											ERuntime = eventDate + " "
													+ lastTime;
											endRuntime = sdfTime
													.parse(ERuntime).getTime();
										}
										runDuration += (int) (endRuntime - startRuntime) / 1000;
									}
									if (SIdletime != null && SIdletime != "") {
										long endIdletime = 0;
										long startIdletime = sdfTime.parse(
												SIdletime).getTime();
										if (EIdletime != null) {
											endIdletime = sdfTime.parse(
													EIdletime).getTime();
										} else {
											EIdletime = eventDate + " "
													+ lastTime;
											endIdletime = sdfTime.parse(
													EIdletime).getTime();
										}
										idleDuration += (int) (endIdletime - startIdletime) / 1000;
									}
								}
								diffcnt++;
								hashMap.clear();
							}
						}
					}
				}
				System.out.println(totaltime);
				// Total Inside Time
				if (day != restDay) {
					if (totaltime != 0) {
						reportdata2
								.setTimeStamp(formatIntoHHMMSSWithOutDay(totaltime));
					} else {
						reportdata2.setTimeStamp("00:00:00");
					}
					// Total Shift Time
					if (totaltime1 != 0) {
						reportdata2
								.setBatteryCurrent(formatIntoHHMMSSWithOutDay(totaltime1));
					} else {
						reportdata2.setBatteryCurrent("00:00:00");
					}
					// Total OutSide Time
					if (outzodezone != 0) {
						reportdata2
								.setDescription(formatIntoHHMMSSWithOutDay(outzodezone));
					} else {
						reportdata2.setDescription("00:00:00");
					}

					if (runDuration != 0) {
						reportdata2
								.setRunDur(formatIntoHHMMSSWithOutDay(runDuration));
					} else {
						reportdata2.setRunDur("00:00:00");
					}
					if (idleDuration != 0) {
						reportdata2
								.setIdleDur(formatIntoHHMMSSWithOutDay(idleDuration));
					} else {
						reportdata2.setIdleDur("00:00:00");
					}
					if (zoneout != 0) {
						reportdata2.setRowNum(zoneout);
					} else {
						reportdata2.setRowNum(0);
					}
				} else {
					reportdata2.setTimeStamp("Rest Day");
					reportdata2
							.setBatteryCurrent(formatIntoHHMMSSWithOutDay(totaltime1));
					reportdata2.setDescription("Rest Day");
					reportdata2.setRunDur("Rest Day");
					reportdata2.setIdleDur("Rest Day");
					reportdata2.setRowNum(0);
				}
				reportdata2.setEventId(shiftId);
				empreportDatas.add(reportdata2);
				totaltime = 0;
				totaltime1 = 0;
				outzodezone = 0;
				runDuration = 0;
				idleDuration = 0;
				zoneout = 0;
			} catch (Exception e) {
				LOGGER.error("SummaryEJB::getEmployeeSummaryDayReport::Exception occured"
						+ e);
			}

		}
		return empreportDatas;
	}

	@Override
	public void insertEmployeeDaySummary(String employeeId, String eventDate,
			List<ReportData> empreportDatas) {
		LOGGER.info("SummaryEJB::insertVehicleDaySummary::Entered into this method"
				+ "vin"
				+ employeeId
				+ "eventDate"
				+ eventDate
				+ "reportDatas"
				+ empreportDatas);
		try {
			int reportdatasize = empreportDatas.size();
			for (int i = 0; i < reportdatasize; i++) {
				Employeesummarybyday employeesummarybyday = new Employeesummarybyday();
				EmployeesummarybydayId id = new EmployeesummarybydayId();
				id.setEmployeeId(employeeId);
				id.setEventDate(eventDate);
				id.setWorkingDur(empreportDatas.get(i).getRunDur());
				id.setIdleDur(empreportDatas.get(i).getIdleDur());
				id.setTotalDur(empreportDatas.get(i).getBatteryCurrent());
				id.setInsideZone(empreportDatas.get(i).getTimeStamp());
				id.setOutsideZone(empreportDatas.get(i).getDescription());
				id.setShiftId(empreportDatas.get(i).getEventId());
				id.setViolationCount(empreportDatas.get(i).getRowNum());
				employeesummarybyday.setId(id);
				em.persist(employeesummarybyday);
			}
		} catch (Exception e) {
			LOGGER.error("SummaryEJB::insertVehicleDaySummary::Exception occured in this method insertVehicleDaySummary "
					+ e);
		}
		LOGGER.info("SummaryEJB::insertVehicleDaySummary::Leaving from this method is successfully");
	}

	@Override
	public List<Vehicleevent> getVehicleEvents(String vin, String eventDate) {
		List<Vehicleevent> vehicleEvents = new ArrayList<Vehicleevent>();
		try {
			LOGGER.info("SummaryEJB::getSummaryDayReport::Entered into method::"
					+ "vin" + vin + "eventDate" + eventDate);
			String strForDay = "SELECT v FROM Vehicleevent v WHERE v.id.vin =:vin AND v.id.eventTimeStamp BETWEEN  CONCAT(:eventDate,' 00:00:00')  AND CONCAT(:eventDate,' 23:59:59') ORDER BY v.id.eventTimeStamp";
			Query queryStrForDay = em.createQuery(strForDay);
			queryStrForDay.setParameter("vin", vin);
			queryStrForDay.setParameter("eventDate", eventDate);
			LOGGER.info("before Execute Query" + "query" + queryStrForDay);
			vehicleEvents = (List<Vehicleevent>) queryStrForDay.getResultList();
			LOGGER.info("after Execute Query" + "queryStrForDay"
					+ queryStrForDay);
		} catch (Exception e) {
			LOGGER.error("SummaryEJB::getSummaryDayReport::Exception Occured:: "
					+ e);
		}
		return vehicleEvents;
	}

	public String addmin(String min) {
		String min1 = min.split(",")[0];
		String min2 = min.split(",")[1];
		String newTime = "";
		if (min2.equalsIgnoreCase("fromDate")) {
			try {
				Date d = df.parse(min1);
				Calendar cal = Calendar.getInstance();
				cal.setTime(d);
				cal.add(Calendar.MINUTE, -30);
				newTime = df.format(cal.getTime()) + ":" + "00";
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (min2.equalsIgnoreCase("toDate")) {
			try {
				Date d = df.parse(min1);
				Calendar cal = Calendar.getInstance();
				cal.setTime(d);
				cal.add(Calendar.MINUTE, 30);
				newTime = df.format(cal.getTime()) + ":" + "00";
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return newTime;
	}

	public String timeCompare(String t1, String t2) {
		String[] parts, parts1;
		parts = t1.split(":");
		parts1 = t2.split(":");
		// // returns 1 if greater, -1 if less and 0 if the same
		if (Integer.valueOf(parts[0]) > Integer.valueOf(parts1[0])) {
			return "1";
		} else if (Integer.valueOf(parts[0]) < Integer.valueOf(parts1[0])) {
			return "-1";
		} else {
			return "0";
		}

	}

	public String getOdometerValue(String runTime, String stopTime, String vin) {
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

	@Override
	public String getNewVehicleSumaryReportDay(String vin, String perdate) {
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArray_overSpeed = new JSONArray();

		try {
			String strForDay = "select v from Vehicleevent v where v.id.vin=:vin and v.id.eventTimeStamp between :eventTimeStamp and :toeventTimeStamp order by v.id.eventTimeStamp";
			Query queryStrForDay = em.createQuery(strForDay);
			queryStrForDay.setParameter("vin", vin);
			queryStrForDay.setParameter(EVENTTIMESTAMP,
					sdfTime.parse(perdate + " " + START_TIME));
			queryStrForDay.setParameter(TO_EVENTTIMESTAMP,
					sdfTime.parse(perdate + " " + END_TIME));
			LOGGER.debug(" Before strForDay Execute Query " + strForDay);
			List<Vehicleevent> vehicleevents = (List<Vehicleevent>) queryStrForDay
					.getResultList();
			LOGGER.debug("After strForDay Execute Query "
					+ vehicleevents.size());
			Vehicle vehicles = em.find(Vehicle.class, vin.trim());
			if (vehicleevents.isEmpty()) {

				String heartBeatMaxMIn = getMaxandMinHeartbeatevent(perdate,
						vin);
				if (heartBeatMaxMIn != null) {

					String heartbeatevent = "SELECT latitude,longitude FROM vehicleevent v WHERE vin='"
							+ vin + "' ORDER BY eventtimestamp DESC LIMIT 1;";
					Query query = em.createNativeQuery(heartbeatevent);
					List<Object[]> objlist = query.getResultList();
					if (!(objlist.isEmpty())) {
						Object[] obj = objlist.get(0);
						JSONObject json = new JSONObject(commonJson());
						json.put("Plate No", vehicles.getPlateNo());
						json.put("Begin", heartBeatMaxMIn.split(",")[1]);
						json.put("Begin At", obj[0] + "," + obj[1]);
						json.put("End At", obj[0] + "," + obj[1]);
						json.put("End", heartBeatMaxMIn.split(",")[0]);
						json.put("Odometer", "---");

						json.put(
								"Stop",
								formatIntoHHMMSSWithOutDay((addy(sdfHHMMSS
										.format(sdfTime.parse(heartBeatMaxMIn
												.split(",")[0]))) - (addy(sdfHHMMSS
										.format(sdfTime.parse(heartBeatMaxMIn
												.split(",")[1])))))));

						jsonArray.put(json);
					}
				}

			}
			String prevStaus = "", preLatlong = "", curStatus = "", curLatLong = "";
			Date preDate = null, curDate = null;
			long odometoe = 0;

			boolean isgenerator = (vehicles.getVehicletype().getVehicleType()
					.equalsIgnoreCase(STR_DEEPSEA_GENERATOR));
			boolean isImmovable = ((vehicles.getVehiclemodel().getRemarks() == null ? "null"
					: vehicles.getVehiclemodel().getRemarks())
					.equalsIgnoreCase(STR_IMMOVABLE));
			String plateno = vehicles.getPlateNo();

			// this Code for overspeed summary
			String strForalertcofig = "select ac from Alertconfig ac where ac.id.vin=:vin and ac.id.alertType='OVERSPEED'";
			Query querystrForalertcofig = em.createQuery(strForalertcofig);
			querystrForalertcofig.setParameter("vin", vin);
			List<Alertconfig> alertconfig = (List<Alertconfig>) querystrForalertcofig
					.getResultList();
			String preLatlong_OverSpeed = "", curLatLong_OverSpeed = "";
			Date preDate_OverSpeed = null, cureDate_OverSpeed = null;
			long sum = 0;
			int min = 0, max = 0, count = 0, configSpeed;
			boolean isOverspeed = false;
			if (!alertconfig.isEmpty())
				configSpeed = Integer.parseInt(alertconfig.get(0)
						.getAlertRange());
			else
				configSpeed = 60;

			for (int i = 0; i < vehicleevents.size(); i++) {

				Vehicleevent ve = vehicleevents.get(i);
				boolean isStandBy = ((ve.getIoevent() == null ? "null" : ve
						.getIoevent()).equalsIgnoreCase("{}"));
				if (i == 0) {
					if (ve.getSpeed() > 0 && ve.getEngine()) {
						prevStaus = "Running";
					} else if (ve.getSpeed() == 0 && ve.getEngine()) {
						prevStaus = "Idle";
						if (isImmovable) {
							prevStaus = "Running";
						}
					} else if (ve.getSpeed() == 0 && !ve.getEngine()) {
						prevStaus = "Stop";
						if (!isStandBy && isgenerator) {
							prevStaus = "Idle";// this Standby status
						}
					} else {
						prevStaus = "Towed";
					}
					preLatlong = ve.getLatitude() + "," + ve.getLongitude();
					preDate = ve.getId().getEventTimeStamp();

					if (addy(sdfHHMMSS.format(preDate)) < 60
							&& !prevStaus.equalsIgnoreCase("Stop")) {
						preDate = sdfTime.parse(sdfDate.format(preDate) + " "
								+ STR_FROM_HR);
					} else {
						if (!prevStaus.equalsIgnoreCase("Stop")) {

							JSONObject json = new JSONObject(commonJson());
							json.put("Plate No", plateno);
							json.put("Begin", sdfDate.format(preDate) + " "
									+ STR_FROM_HR);
							json.put("Begin At", preLatlong);
							json.put("End At", preLatlong);
							json.put("End", sdfTime.format(preDate));
							json.put("Odometer", "---");

							json.put("Stop",
									formatIntoHHMMSSWithOutDay((addy(sdfHHMMSS
											.format(preDate)) - (addy(sdfHHMMSS
											.format(sdfTime.parse(sdfDate
													.format(preDate)
													+ " "
													+ STR_FROM_HR)))))));

							jsonArray.put(json);

						} else {
							preDate = sdfTime.parse(sdfDate.format(preDate)
									+ " " + STR_FROM_HR);
						}

					}
					odometoe = (ve.getOdometer() == null ? 0 : ve.getOdometer());

				} else {

					if (ve.getSpeed() > 0 && ve.getEngine()) {
						curStatus = "Running";
					} else if (ve.getSpeed() == 0 && ve.getEngine()) {
						curStatus = "Idle";
						if (isImmovable) {
							curStatus = "Running";
						}
					} else if (ve.getSpeed() == 0 && !ve.getEngine()) {
						curStatus = "Stop";
						if (!isStandBy && isgenerator) {
							curStatus = "Idle";// this Standby status
						}
					} else {
						curStatus = "Towed";
					}

					curLatLong = ve.getLatitude() + "," + ve.getLongitude();
					curDate = ve.getId().getEventTimeStamp();
					odometoe = odometoe
							+ (ve.getOdometer() == null ? 0 : ve.getOdometer());
					if (!curStatus.equalsIgnoreCase(prevStaus)) {
						JSONObject json = new JSONObject(commonJson());
						json.put("Plate No", plateno);
						json.put("Begin", sdfTime.format(preDate));
						json.put("Begin At", preLatlong);
						json.put("End At", curLatLong);
						json.put("End", sdfTime.format(curDate));
						json.put(
								"Odometer",
								odometoe == 0 ? "---" : String.format("%.1f",
										odometoe / 1000f));
						if (prevStaus == "Idle" || prevStaus == "Stop") {
							json.put("Begin At", preLatlong);
							json.put("End At", preLatlong);
							json.put("Odometer", "---");
						}

						json.put(prevStaus,
								formatIntoHHMMSSWithOutDay((addy(sdfHHMMSS
										.format(curDate)) - (addy(sdfHHMMSS
										.format(preDate))))));

						jsonArray.put(json);
						prevStaus = curStatus;
						preLatlong = curLatLong;
						preDate = ve.getId().getEventTimeStamp();
						odometoe = (ve.getOdometer() == null ? 0 : ve
								.getOdometer());
					}
				}
				if (i == vehicleevents.size() - 1) {

					JSONObject json = new JSONObject(commonJson());
					json.put("Plate No", plateno);
					json.put("Begin", sdfTime.format(preDate));
					json.put("Begin At", preLatlong);
					json.put("End", sdfTime.format(curDate));
					json.put("End At", curLatLong);
					json.put(
							"Odometer",
							odometoe == 0 ? "---" : String.format("%.1f",
									odometoe / 1000f));
					if (prevStaus == "Idle" || prevStaus == "Stop") {
						json.put("Begin At", preLatlong);
						json.put("End At", preLatlong);
						json.put("Odometer", "---");
						if (prevStaus.equalsIgnoreCase("Stop")) {
							curDate = sdfTime.parse(sdfDate.format(curDate)
									+ " " + STR_TO_HR);
							json.put("End", sdfTime.format(curDate));
						}

					}

					if (!prevStaus.equalsIgnoreCase("Stop")
							&& addy(sdfHHMMSS.format(curDate)) > addy(sdfHHMMSS
									.format(sdfTime.parse(sdfDate
											.format(curDate) + " " + STR_TO_HR))) - 90) {
						curDate = sdfTime.parse(sdfDate.format(curDate) + " "
								+ STR_TO_HR);
						json.put("End", sdfTime.format(curDate));
					}

					json.put(prevStaus,
							formatIntoHHMMSSWithOutDay((addy(sdfHHMMSS
									.format(curDate)) - (addy(sdfHHMMSS
									.format(preDate))))));

					jsonArray.put(json);

					if (!prevStaus.equalsIgnoreCase("Stop")
							&& !(addy(sdfHHMMSS.format(curDate)) > addy(sdfHHMMSS
									.format(sdfTime.parse(sdfDate
											.format(curDate) + " " + STR_TO_HR))) - 90)) {
						json = new JSONObject(commonJson());
						json.put("Plate No", plateno);
						json.put("Begin", sdfTime.format(curDate));
						json.put("Begin At", curLatLong);
						json.put("End", sdfDate.format(curDate) + " "
								+ STR_TO_HR);
						json.put("End At", curLatLong);
						json.put("Odometer", "---");
						json.put(
								"Stop",
								formatIntoHHMMSSWithOutDay((addy(sdfHHMMSS
										.format(sdfTime.parse(sdfDate
												.format(curDate)
												+ " "
												+ STR_TO_HR))) - (addy(sdfHHMMSS
										.format(curDate))))));
						jsonArray.put(json);

					}

				}

				// OverSpeed summary Logic
				if (i == 0 && ve.getSpeed() >= configSpeed) {
					preLatlong_OverSpeed = ve.getLatitude() + ","
							+ ve.getLongitude();
					preDate_OverSpeed = ve.getId().getEventTimeStamp();
					min = ve.getSpeed();
					max = ve.getSpeed();
					sum += ve.getSpeed();
					count++;
					isOverspeed = true;
				} else {
					if (ve.getSpeed() >= configSpeed && !isOverspeed) {
						preLatlong_OverSpeed = ve.getLatitude() + ","
								+ ve.getLongitude();
						preDate_OverSpeed = ve.getId().getEventTimeStamp();
						min = ve.getSpeed();
						max = ve.getSpeed();
						sum += ve.getSpeed();
						count++;
						isOverspeed = true;

					} else if (ve.getSpeed() >= configSpeed && isOverspeed) {

						if (min > ve.getSpeed())
							min = ve.getSpeed();
						if (max < ve.getSpeed())
							max = ve.getSpeed();
						sum += ve.getSpeed();
						count++;

					} else if (!(ve.getSpeed() >= configSpeed) && isOverspeed) {

						curLatLong_OverSpeed = ve.getLatitude() + ","
								+ ve.getLongitude();
						cureDate_OverSpeed = ve.getId().getEventTimeStamp();

						JSONObject json = new JSONObject();
						json.put("Plate No", plateno);
						json.put("Begin", sdfTime.format(preDate_OverSpeed));
						json.put("Begin At", preLatlong_OverSpeed);
						json.put("End At", curLatLong_OverSpeed);
						json.put("End", sdfTime.format(cureDate_OverSpeed));
						json.put(
								"OverDuration",
								formatIntoHHMMSSWithOutDay((addy(sdfHHMMSS
										.format(cureDate_OverSpeed)) - (addy(sdfHHMMSS
										.format(preDate_OverSpeed))))));
						json.put("MaxSpeed", max);
						json.put("MinSpeed", min);
						json.put("AvgSpeed", sum / count);
						jsonArray_overSpeed.put(json);
						isOverspeed = false;
						count = 0;
						sum = 0;
					}
				}

			}

		} catch (Exception e) {
			LOGGER.error("consolidatesummaryreport ::" + e);
			// e.printStackTrace();
		}

		JSONObject resultjson = new JSONObject();
		try {
			resultjson.put("Summary", jsonArray);
			resultjson.put("overspeed", jsonArray_overSpeed);

		} catch (Exception e) {
			// TODO Auto-generated catch block

			LOGGER.error("consolidatesummaryreport ::" + e);
			// e.printStackTrace();
		}
		return resultjson.toString();

	}

	@Override
	public void insertNewVehicleDaySummary(String vin, String eventDate,
			String reportDatas) {
		LOGGER.info("SummaryEJB::insertVehicleDaySummary::Entered into this method"
				+ "vin"
				+ vin
				+ "eventDate"
				+ eventDate
				+ "reportDatas"
				+ reportDatas);
		try {
			// int totRunDur = 0, totStopDur = 0, totIdleDur = 0, totTowedDur =
			// 0;

			JSONArray jsonArray = new JSONArray(reportDatas);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = new JSONObject(jsonArray.get(i)
						.toString());

				Consolidatesummaryreport consolidatesummaryreport = new Consolidatesummaryreport();
				ConsolidatesummaryreportId id = new ConsolidatesummaryreportId();
				id.setVin(vin);
				id.setFromDate(eventDate);
				id.setBegin(jsonObject.getString("Begin"));
				id.setBeginLocation(jsonObject.getString("Begin At"));
				id.setEnd(jsonObject.getString("End"));
				id.setEndLocation(jsonObject.getString("End At"));
				id.setRunningDur(jsonObject.getString("Running"));
				id.setStopDur(jsonObject.getString("Stop"));
				id.setIdleDur(jsonObject.getString("Idle"));
				id.setTowDur(jsonObject.getString("Towed"));
				id.setOdometer(jsonObject.getString("Odometer"));
				consolidatesummaryreport.setId(id);
				em.persist(consolidatesummaryreport);
			}

		} catch (Exception e) {
			LOGGER.error("SummaryEJB::insertVehicleDaySummary::Exception occured in this method insertVehicleDaySummary "
					+ e);
		}
		LOGGER.info("SummaryEJB::insertVehicleDaySummary::Leaving from this method is successfully");
	}

	public static String commonJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("Plate No", "");
			json.put("Begin", "");
			json.put("Begin At", "");
			json.put("End", "");
			json.put("End At", "");
			json.put("Odometer", "");
			json.put("Running", STR_EMPTY);
			json.put("Idle", STR_EMPTY);
			json.put("Stop", STR_EMPTY);
			json.put("Towed", STR_EMPTY);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return json.toString();
	}

	@Override
	public String getNewVehicleSumaryStatusReportDay(String vin, String perdate) {
		JSONArray jsonArray = new JSONArray();

		try {
			String strForDay = "select v from Vehicleevent v where v.id.vin=:vin and v.id.eventTimeStamp between :eventTimeStamp and :toeventTimeStamp order by v.id.eventTimeStamp";
			Query queryStrForDay = em.createQuery(strForDay);
			queryStrForDay.setParameter("vin", vin);
			queryStrForDay.setParameter(EVENTTIMESTAMP,
					sdfTime.parse(perdate + " " + START_TIME));
			queryStrForDay.setParameter(TO_EVENTTIMESTAMP,
					sdfTime.parse(perdate + " " + END_TIME));
			LOGGER.debug(" Before strForDay Execute Query " + strForDay);
			List<Vehicleevent> vehicleevents = (List<Vehicleevent>) queryStrForDay
					.getResultList();
			LOGGER.debug("After strForDay Execute Query "
					+ vehicleevents.size());
			Vehicle vehicles = em.find(Vehicle.class, vin.trim());

			Query queryvhasio = em
					.createQuery("select vhio from VehicleHasIo vhio where vhio.id.vin='"
							+ vin + "'");
			List<VehicleHasIo> vehicleHasIo = queryvhasio.getResultList();

			String prevStaus = "", preLatlong = "", curStatus = "", curLatLong = "";
			Date preDate = null, curDate = null;
			long odometoe = 0;

			boolean isgenerator = (vehicles.getVehicletype().getVehicleType()
					.equalsIgnoreCase(STR_DEEPSEA_GENERATOR));
			boolean isImmovable = ((vehicles.getVehiclemodel().getRemarks() == null ? "null"
					: vehicles.getVehiclemodel().getRemarks())
					.equalsIgnoreCase(STR_IMMOVABLE));
			String plateno = vehicles.getPlateNo();
			String suffStatus = "";
			for (int i = 0; i < vehicleevents.size(); i++) {

				Vehicleevent ve = vehicleevents.get(i);
				boolean isStandBy = ((ve.getIoevent() == null ? "null" : ve
						.getIoevent()).equalsIgnoreCase("{}"));
				String tmpStatus1 = "";
				for (int j = 0; j < vehicleHasIo.size(); j++) {
					VehicleHasIo io = vehicleHasIo.get(j);

					int status = io.getStatus() == null ? 0 : io.getStatus();
					if (io.getId().getIo() == 2
							&& status == (ve.getDi2() == null ? 0 : ve.getDi2())) {
						tmpStatus1 += io.getIoname();

					} else if (io.getId().getIo() == 3
							&& status == (ve.getDi3() == null ? 0 : ve.getDi3())) {
						tmpStatus1 += io.getIoname();
					} /*
					 * else if (io.getId().getIo() == 4 && status ==
					 * (ve.getDi4() == null ? 0 : ve.getDi4())) { tmpStatus +=
					 * "(" + io.getIoname() + ")";
					 * 
					 * }
					 */
					suffStatus = tmpStatus1;
				}
				if (i == 0) {
					String tempStatus = "";
					if (ve.getSpeed() > 0 && ve.getEngine()) {
						tempStatus = "Running";
					} else if (ve.getSpeed() == 0 && ve.getEngine()) {
						tempStatus = "Idle";
						if (isImmovable) {
							tempStatus = "Running";
						}
					} else if (ve.getSpeed() == 0 && !ve.getEngine()) {
						tempStatus = "Stop";
						if (!isStandBy && isgenerator) {
							tempStatus = "Idle";// this Standby status
						}
					} else {
						tempStatus = "Towed";
					}

					if (suffStatus.equalsIgnoreCase("")) {
						prevStaus = tempStatus;
					} else {
						prevStaus = suffStatus;
					}

					preLatlong = ve.getLatitude() + "," + ve.getLongitude();
					preDate = ve.getId().getEventTimeStamp();
					odometoe = (ve.getOdometer() == null ? 0 : ve.getOdometer());

				} else {
					String tempStatus = "";

					if (ve.getSpeed() > 0 && ve.getEngine()) {
						tempStatus = "Running";
					} else if (ve.getSpeed() == 0 && ve.getEngine()) {
						tempStatus = "Idle";
						if (isImmovable) {
							tempStatus = "Running";
						}
					} else if (ve.getSpeed() == 0 && !ve.getEngine()) {
						tempStatus = "Stop";
						if (!isStandBy && isgenerator) {
							tempStatus = "Idle";// this Standby status
						}
					} else {
						tempStatus = "Towed";
					}

					if (suffStatus.equalsIgnoreCase("")) {
						curStatus = tempStatus;
					} else {
						curStatus = suffStatus;
					}

					// curStatus = tempStatus + suffStatus;
					curLatLong = ve.getLatitude() + "," + ve.getLongitude();
					curDate = ve.getId().getEventTimeStamp();
					odometoe = odometoe
							+ (ve.getOdometer() == null ? 0 : ve.getOdometer());
					if (!curStatus.equalsIgnoreCase(prevStaus)) {
						JSONObject json = new JSONObject();
						json.put("Plate No", plateno);
						json.put("Begin", sdfTime.format(preDate));
						json.put("Begin At", preLatlong);
						json.put("End At", curLatLong);
						json.put("End", sdfTime.format(curDate));
						json.put(
								"Odometer",
								odometoe == 0 ? "0" : String.format("%.1f",
										odometoe / 1000f));
						if (prevStaus == "Idle" || prevStaus == "Stop") {
							json.put("Begin At", preLatlong);
							json.put("End At", preLatlong);
							json.put("Odometer", "0");
						}
						json.put("Status", prevStaus);
						suffStatus = "";
						json.put("Duration",
								formatIntoHHMMSSWithOutDay((addy(sdfHHMMSS
										.format(curDate)) - (addy(sdfHHMMSS
										.format(preDate))))));

						jsonArray.put(json);
						prevStaus = curStatus;
						preLatlong = curLatLong;
						preDate = ve.getId().getEventTimeStamp();
						odometoe = (ve.getOdometer() == null ? 0 : ve
								.getOdometer());
					} else if (i == vehicleevents.size() - 1) {
						JSONObject json = new JSONObject(commonJson());
						json.put("Plate No", plateno);
						json.put("Begin", sdfTime.format(preDate));
						json.put("Begin At", preLatlong);
						json.put("End", sdfTime.format(curDate));
						json.put("End At", curLatLong);
						json.put(
								"Odometer",
								odometoe == 0 ? "0" : String.format("%.1f",
										odometoe / 1000f));
						if (prevStaus == "Idle" || prevStaus == "Stop") {
							json.put("Begin At", preLatlong);
							json.put("End At", preLatlong);
							json.put("Odometer", "0");

						}
						json.put("Status", prevStaus);
						suffStatus = "";
						json.put("Duration",
								formatIntoHHMMSSWithOutDay((addy(sdfHHMMSS
										.format(curDate)) - (addy(sdfHHMMSS
										.format(preDate))))));

						jsonArray.put(json);

					}

				}

			}

		} catch (Exception e) {
			LOGGER.error("consolidatesummaryreport ::" + e);
			e.printStackTrace();
		}

		return jsonArray.toString();

	}

	@Override
	public void insertNewVehicleSummaryStatus(String vin, String format,
			String newreportDatasDay) {
		LOGGER.info("SummaryEJB::insertVehicleDaySummary::Entered into this method"
				+ "vin"
				+ vin
				+ "eventDate"
				+ format
				+ "reportDatas"
				+ newreportDatasDay);
		try {
			// int totRunDur = 0, totStopDur = 0, totIdleDur = 0, totTowedDur =
			// 0;

			JSONArray jsonArray = new JSONArray(newreportDatasDay);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = new JSONObject(jsonArray.get(i)
						.toString());
				Assetsummaryreport assetSummaryReport = new Assetsummaryreport();
				AssetsummaryreportId assetSummaryReportId = new AssetsummaryreportId();

				// Consolidatesummaryreport consolidatesummaryreport = new
				// Consolidatesummaryreport();
				// ConsolidatesummaryreportId id = new
				// ConsolidatesummaryreportId();
				assetSummaryReportId.setVin(vin);
				assetSummaryReportId.setAssetId(jsonObject
						.getString("Plate No"));
				assetSummaryReportId.setFromDate(sdfDate.parse(format));
				assetSummaryReportId.setBegin(sdfTime.parse(jsonObject
						.getString("Begin")));
				assetSummaryReportId.setBeginLocation(jsonObject
						.getString("Begin At"));
				assetSummaryReportId.setEnd(sdfTime.parse(jsonObject
						.getString("End")));
				assetSummaryReportId.setEndLocation(jsonObject
						.getString("End At"));
				assetSummaryReportId.setStatus(jsonObject.getString("Status"));
				assetSummaryReportId.setDuration(jsonObject
						.getString("Duration"));
				assetSummaryReportId.setOdometer(jsonObject
						.getString("Odometer"));
				assetSummaryReport.setId(assetSummaryReportId);
				em.persist(assetSummaryReport);
			}

		} catch (Exception e) {
			LOGGER.error("SummaryEJB::insertVehicleDaySummary::Exception occured in this method insertVehicleDaySummary "
					+ e);
		}
		LOGGER.info("SummaryEJB::insertVehicleDaySummary::Leaving from this method is successfully");
	}

	@Override
	public String getRemoveFromNotransmissionOverride(String currDate) {
		// TODO Auto-generated method stub
		String noTransOverrideDetails = " SELECT nt.imeiNo, nt.empName, nt.zoneId FROM notransmissionoverride nt WHERE nt.date = '"
				+ currDate + "'";
		LOGGER.info("before Execute Query" + "query" + noTransOverrideDetails);
		Query quer111 = em.createNativeQuery(noTransOverrideDetails);
		List<Object[]> finalNoTransOverrideData = (List<Object[]>) quer111
				.getResultList();
		LOGGER.info("after Execute Query" + "queryStrForDay"
				+ finalNoTransOverrideData);
		for (int i = 0; i < finalNoTransOverrideData.size(); i++) {
			Object[] row = (Object[]) finalNoTransOverrideData.get(i);
			Notransmissionoverride notransoverride = em.find(
					Notransmissionoverride.class, row[0]);

			if (notransoverride != null) {
				if ((row[2].toString()).equalsIgnoreCase("0")) {
					em.remove(notransoverride);
					// DeviceStarter.deActivateDeviceForNoTrnsDevice((row[0]
					// .toString()).trim());
				}
			}
		}
		return null;
	}

	public Provider getProviderDetailsByCompanyId(String companyId) {
		Provider provider = null;
		try {
			Query query = em
					.createQuery("SELECT pro FROM Provider pro WHERE pro.id = (SELECT c.suffix FROM Company c WHERE c.companyId= :companyId)");
			query.setParameter("companyId", companyId);

			List<Provider> objects = (List<Provider>) query.getResultList();
			if (!objects.isEmpty() && objects.size() != 0) {
				provider = objects.get(0);
			} else {
				LOGGER.error("AlertsEJB :: getProviderDetails: No result for "
						+ companyId + " and query = " + query.toString());
			}
		} catch (Exception e) {
			LOGGER.error("AlertsEJB :: getProviderDetails for " + companyId
					+ " is " + e.getMessage());
		}
		return provider;
	}

	@Override
	public String sendReportMail(JSONArray json, String title, String emails,
			String CompId, JSONArray format, String reportType, String fileName) {
		LOGGER.info("Entering Send EMAIL REPORT");
		try {
			Provider provider = getProviderDetailsByCompanyId(CompId);
			String mail[];
			String documentType = "";

			if (reportType.equalsIgnoreCase("Excel")) {
				documentType = buildExcelDocumentr(json, title, emails,
						provider, format, fileName);
			} else {
				documentType = buildPDFDocumentr(json, title, emails, provider,
						format, fileName);
			}

			File xls = new File(documentType);
			List<File> xlsList = new ArrayList<File>();
			xlsList.add(xls);
			mail = emails.split(",");
			if (provider != null) {
				if (provider.getMailMode().equalsIgnoreCase("gmail")) {
					for (String mailone : mail) {
						EmailSendHttpClient.send(mailone, title, "",
								provider.getAlertMailId(),
								provider.getAlertMailPasscode(), xlsList);
					}
				} else if (provider.getMailMode().equalsIgnoreCase("amazon")) {
					AmazonSMTPMail amazonSMTPMail = new AmazonSMTPMail();
					try {
						for (String mailone : mail) {
							amazonSMTPMail.sendEmail(
									provider.getAmazonVerifiedFromEmail(),
									mailone, title, "Report",
									provider.getAmazonSmtpUserName(),
									provider.getAmazonSmtpPassword(),
									provider.getAmazonHostAddress(),
									provider.getAmazonPort(), xlsList);
						}
					} catch (MessagingException e) {
						LOGGER.error(" Error on sendReportMail() Email Exception"
								+ e.getMessage());

					}
				} else if (provider.getMailMode().equalsIgnoreCase("Zoho")) {
					ZohoSMTPMail zohoSMTPMail = new ZohoSMTPMail();
					try {
						for (String mailone : mail) {
							zohoSMTPMail.sendEmail(mailone, title, "",
									provider.getAlertMailId(),
									provider.getAlertMailPasscode(), xlsList);
						}
					} catch (Exception e) {
						e.printStackTrace();
						LOGGER.error(" Error on sendReportMail() Email Exception"
								+ e.getMessage());

					}
				}
			}
			return "SUCCESS";
		} catch (Exception e) {
			LOGGER.error("Error on sendReportMail()::" + e);
			return "FAIL";
		}
	}

	@Override
	public List<Object[]> getsummaryVins(String compId) {
		List<Object[]> listVin = null;
		Query sumVin = em
				.createNativeQuery("SELECT v.vin,v.userId FROM vehicle v WHERE v.companyId ='"
						+ compId + "' AND v.active = 1 AND v.status IS NULL");
		listVin = (List<Object[]>) sumVin.getResultList();
		return listVin;
	}

	/*
	 * @Override public String getsummaryMail(String compIdR) { Query sumMail =
	 * em .createNativeQuery(
	 * "SELECT emailAddress FROM alertconfig WHERE alertType = 'SUMMARYREPORT' AND companyId ='"
	 * + compIdR + "'"); return (String) sumMail.getSingleResult(); }
	 */

	@Override
	public List<Object[]> getcompID(String alertType) {
		Query sumCompId = em
				.createNativeQuery("SELECT ac.companyId,ac.emailAddress FROM alertconfig ac WHERE ac.alertType = '"
						+ alertType + "' ");
		return (List<Object[]>) sumCompId.getResultList();
	}

	@Override
	public JSONArray getConsolidateSummaryReport(String vin, String userId) {
		// TODO Auto-generated method stub
		JSONArray jsonArray = new JSONArray();
		JSONObject json = null;
		int runDur = 0, stopdur = 0, idleDur = 0, towedDur = 0;
		float odometer = 0;
		try {
			Vehicle vehicles = em.find(Vehicle.class, vin);
			LOGGER.info("SummaryEJB::vehicleList::Entered into method:: timezone"
					+ TIMEZONEID);
			Calendar calendar = TimeZoneUtil.getDateForTimeZones(new Date(),
					TIMEZONEID);
			calendar.add(Calendar.DATE, -1);
			String preDate = TimeZoneUtil.getDate(calendar.getTime());

			Query consolData = em
					.createNativeQuery("SELECT vin,fromDate,begin,beginLocation,end,endLocation,runningDur,stopDur,idleDur,towDur,odometer FROM consolidatesummaryreport WHERE vin ='"
							+ vin + "' AND fromDate = '" + preDate + "'");
			List<Object[]> consolidatesummaryData = (List<Object[]>) consolData
					.getResultList();

			for (int i = 0; i < consolidatesummaryData.size(); i++) {
				Object[] rowConsolidate = (Object[]) consolidatesummaryData
						.get(i);
				json = new JSONObject();
				json.put("Plate No", vehicles.getPlateNo().toString());
				json.put("Asset Code", userId);
				json.put("Date", rowConsolidate[1].toString());
				json.put("Begin Time", rowConsolidate[2].toString());
				json.put("Begin At", rowConsolidate[3].toString());
				json.put("End Time", rowConsolidate[4].toString());
				json.put("End At", rowConsolidate[5].toString());
				json.put("Running Dur", rowConsolidate[6].toString());
				json.put("Stop Dur", rowConsolidate[7].toString());
				json.put("Idle dur", rowConsolidate[8].toString());
				json.put("Towed Dur", rowConsolidate[9].toString());
				json.put("Odometer", rowConsolidate[10].toString());
				if (!(rowConsolidate[6].toString()).equalsIgnoreCase("---")) {
					runDur += TimeZoneUtil.addyWithOutDay(rowConsolidate[6]
							.toString());
				}
				if (!(rowConsolidate[7].toString()).equalsIgnoreCase("---")) {
					stopdur += TimeZoneUtil.addyWithOutDay(rowConsolidate[7]
							.toString());
				}
				if (!(rowConsolidate[8].toString()).equalsIgnoreCase("---")) {
					idleDur += TimeZoneUtil.addyWithOutDay(rowConsolidate[8]
							.toString());
				}
				if (!(rowConsolidate[9].toString()).equalsIgnoreCase("---")) {
					towedDur += TimeZoneUtil.addyWithOutDay(rowConsolidate[9]
							.toString());
				}
				if (!(rowConsolidate[10].toString()).equalsIgnoreCase("---")) {
					odometer += Float.parseFloat(rowConsolidate[10].toString());
				}
				jsonArray.put(json);
				if (i == consolidatesummaryData.size() - 1) {
					json = new JSONObject();
					json.put("Plate No", "");
					json.put("Asset Code", "");
					json.put("Date", "");
					json.put("Begin Time", "");
					json.put("Begin At", "");
					json.put("End Time", "");
					json.put("End At", "");
					json.put("Running Dur", "Total:"
							+ formatIntoHHMMSSWithOutDay(runDur));
					json.put("Stop Dur", "Total:"
							+ formatIntoHHMMSSWithOutDay(stopdur));
					json.put("Idle dur", "Total:"
							+ formatIntoHHMMSSWithOutDay(idleDur));
					json.put("Towed Dur", "Total:"
							+ formatIntoHHMMSSWithOutDay(towedDur));
					json.put("Odometer",
							"Total:" + String.valueOf(Math.round(odometer)));
					jsonArray.put(json);
				}

			}
			/*
			 * if (consolidatesummaryData.size() == 0) { json = new
			 * JSONObject(); String pNo = vehicles.getPlateNo();
			 * json.put("Plate No", pNo); json.put("Date", "");
			 * json.put("Begin", ""); json.put("Begin At",
			 * "Vehicle Not Working"); json.put("End", ""); json.put("End At",
			 * ""); json.put("Running Dur", ""); json.put("Stop Dur",
			 * "Vehicle Not Working"); json.put("Idle dur", "");
			 * json.put("Towed Dur", ""); json.put("Odometer", "");
			 * jsonArray.put(json); }
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonArray;
	}

	@Override
	public String sendReportMailMultifiles(JSONArray json, String title,
			String emails, String CompId, JSONArray format, String plateno) {
		LOGGER.info("Entering Send EMAIL REPORT");
		try {
			Provider provider = getProviderDetailsByCompanyId(CompId);
			Font titleFont = FontFactory.getFont(FontFactory.COURIER_BOLD, 15,
					BaseColor.BLACK);
			Font headerFont = FontFactory.getFont(FontFactory.COURIER_BOLD, 8,
					BaseColor.BLACK);
			Font cellFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 6,
					BaseColor.BLACK);
			final String FONT = "KacstNaskh.ttf";
			Font f = FontFactory.getFont(FONT, BaseFont.IDENTITY_H,
					BaseFont.EMBEDDED);
			Document document = new Document();
			document.setMargins(0, 0, 5, 5);

			File makefile = null;
			String Filename = plateno + ".pdf";
			if (OSValidator.isWindows()) {
				makefile = new File("C:\\Windows\\Temp\\Report\\" + Filename);
			} else if (OSValidator.isUnix()) {
				makefile = new File("/home/ubuntu/imagesdirectory/Report/"
						+ Filename);

			}

			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(makefile));
			document.open();
			String logoURl = "http://" + provider.getBucketName()
					+ ".s3.amazonaws.com/Company/" + provider.getLogoUrl();
			Image img = Image.getInstance(logoURl);
			img.setAlignment(Element.ALIGN_CENTER);
			Paragraph p1 = new Paragraph("", titleFont);
			PdfDiv pdfDiv = new PdfDiv();
			p1.add(new Chunk(title, f));
			p1.setAlignment(Element.ALIGN_CENTER);
			pdfDiv.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
			pdfDiv.addElement(p1);
			JSONArray jsonarray = format;

			PdfPTable table = new PdfPTable(jsonarray.length());
			List<String> perfectKeys = new ArrayList<String>();

			for (int i = 0; i < jsonarray.length(); i++) {
				String headerKey = jsonarray.get(i).toString();
				perfectKeys.add(headerKey);
				Paragraph cellParagraph = new Paragraph("", headerFont);
				PdfDiv cellDiv = new PdfDiv();
				cellParagraph.add(new Chunk(headerKey, f));
				cellDiv.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				cellDiv.addElement(cellParagraph);
				PdfPCell cell = new PdfPCell();
				cell.addElement(cellDiv);
				cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
				table.addCell(cell);
			}

			for (int i = 0; i < json.length(); i++) {
				JSONObject Obj = json.getJSONObject(i);
				for (int j = 0; j < perfectKeys.size(); j++) {
					String cellContent = Obj.getString(perfectKeys.get(j));
					Paragraph innerCellParagraph = new Paragraph("", cellFont);
					PdfDiv innerCellDiv = new PdfDiv();
					innerCellParagraph.add(new Chunk(cellContent, f));
					innerCellDiv.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
					innerCellDiv.addElement(innerCellParagraph);
					PdfPCell cell = new PdfPCell();
					cell.addElement(innerCellDiv);
					table.addCell(cell);
				}
			}
			document.add(img);
			document.add(pdfDiv);
			document.add(Chunk.NEWLINE);
			document.add(table);
			document.close();
			return makefile.getPath();
		} catch (Exception e) {
			LOGGER.error("SEND REPORT MAIL: Error at");
			e.printStackTrace();
			return "FAIL";
		}
	}

	@Override
	public String SendMail(String companyId, String Filepath, String sendermail) {
		Provider provider = getProviderDetailsByCompanyId(companyId);
		File xls = new File(Filepath);
		List<File> xlsList = new ArrayList<File>();
		xlsList.add(xls);
		String[] mail = sendermail.split(",");
		if (provider != null) {
			if (provider.getMailMode().equalsIgnoreCase("gmail")) {
				for (String mailone : mail) {
					EmailSendHttpClient.send(mailone, "Report", "",
							provider.getAlertMailId(),
							provider.getAlertMailPasscode(), xlsList);
				}
			} else if (provider.getMailMode().equalsIgnoreCase("amazon")) {
				AmazonSMTPMail amazonSMTPMail = new AmazonSMTPMail();
				try {
					for (String mailone : mail) {
						amazonSMTPMail.sendEmail(
								provider.getAmazonVerifiedFromEmail(), mailone,
								"Report", "Report",
								provider.getAmazonSmtpUserName(),
								provider.getAmazonSmtpPassword(),
								provider.getAmazonHostAddress(),
								provider.getAmazonPort(), xlsList);
					}
				} catch (MessagingException e) {
					LOGGER.info("Email Exception" + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return sendermail;

	}

	@Override
	public String converToZip(String Filelist, String outoutfilepath) {

		byte[] buffer = new byte[1024];

		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			String SourceFolre = "";

			if (OSValidator.isWindows()) {
				SourceFolre = "C:\\Windows\\Temp\\Report\\";
			} else if (OSValidator.isUnix()) {
				SourceFolre = "/home/ubuntu/imagesdirectory/Report/";

			}
			String source = new File(SourceFolre).getName();
			fos = new FileOutputStream(outoutfilepath);
			zos = new ZipOutputStream(fos);

			System.out.println("Output to Zip : C:\\Windows\\Temp\\Report\\");
			FileInputStream in = null;

			JSONArray FileZie;

			FileZie = new JSONArray(Filelist);

			for (int i = 0; i < FileZie.length(); i++) {

				System.out.println("File Added : " + FileZie.getString(i));
				ZipEntry ze = new ZipEntry(FileZie.getString(i));
				zos.putNextEntry(ze);
				try {
					in = new FileInputStream(FileZie.getString(i));
					int len;
					while ((len = in.read(buffer)) > 0) {
						zos.write(buffer, 0, len);
					}
				} finally {
					in.close();
				}

			}

			zos.closeEntry();
			System.out.println("Folder successfully compressed");
			return outoutfilepath;

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				zos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return outoutfilepath;

	}

	public String getPreferencesData(String keyValue, String corpId) {
		String result;
		String sqlforCmpSetting = "SELECT app.values AS appDefault, comp.values AS companyPref FROM ltmscompanyadmindb.applicationsettings app LEFT JOIN ltmscompanyadmindb.companysettings comp ON app.key=comp.appsettings_key AND comp.company_companyId = :corpId WHERE app.key = :key ";
		LOGGER.info("Brfore exec qry 1");
		Query queryCmpSetting = em.createNativeQuery(sqlforCmpSetting);
		queryCmpSetting.setParameter(CORP_ID, corpId);
		queryCmpSetting.setParameter(KEY, keyValue);
		Object[] obj = (Object[]) queryCmpSetting.getSingleResult();

		if (obj[1] != null) {
			result = (String) obj[1];
		} else {
			result = (String) obj[0];
		}
		return result;
	}

	@Override
	public JSONArray getConsolidateSummaryReportTotal(String vinTot,
			String userId) {

		JSONArray jsonArray = new JSONArray();
		JSONObject json = null;
		int runDur = 0, stopdur = 0, idleDur = 0, towedDur = 0;
		float odometer = 0;
		try {
			Vehicle vehicles = em.find(Vehicle.class, vinTot);
			LOGGER.info("SummaryEJB::vehicleList::Entered into method:: timezone"
					+ TIMEZONEID);
			Calendar calendar = TimeZoneUtil.getDateForTimeZones(new Date(),
					TIMEZONEID);
			calendar.add(Calendar.DATE, -1);
			String preDate = TimeZoneUtil.getDate(calendar.getTime());

			Query consolData = em
					.createNativeQuery("SELECT vin,fromDate,begin,beginLocation,end,endLocation,runningDur,stopDur,idleDur,towDur,odometer FROM consolidatesummaryreport WHERE vin ='"
							+ vinTot + "' AND fromDate = '" + preDate + "'");
			List<Object[]> consolidatesummaryData = (List<Object[]>) consolData
					.getResultList();
			String begin = "", beginAt = "", end = "", endAt = "";
			for (int i = 0; i < consolidatesummaryData.size(); i++) {
				Object[] rowConsolidate = (Object[]) consolidatesummaryData
						.get(i);
				if (i == 0) {
					begin = rowConsolidate[2].toString();
					beginAt = rowConsolidate[3].toString();
				}

				if (i == consolidatesummaryData.size() - 1) {
					end = rowConsolidate[4].toString();
					endAt = rowConsolidate[5].toString();
				}

				if (!(rowConsolidate[6].toString()).equalsIgnoreCase("---")) {
					runDur += TimeZoneUtil.addyWithOutDay(rowConsolidate[6]
							.toString());
				}
				if (!(rowConsolidate[7].toString()).equalsIgnoreCase("---")) {
					stopdur += TimeZoneUtil.addyWithOutDay(rowConsolidate[7]
							.toString());
				}
				if (!(rowConsolidate[8].toString()).equalsIgnoreCase("---")) {
					idleDur += TimeZoneUtil.addyWithOutDay(rowConsolidate[8]
							.toString());
				}
				if (!(rowConsolidate[9].toString()).equalsIgnoreCase("---")) {
					towedDur += TimeZoneUtil.addyWithOutDay(rowConsolidate[9]
							.toString());
				}
				if (!(rowConsolidate[10].toString()).equalsIgnoreCase("---")) {
					odometer += Float.parseFloat(rowConsolidate[10].toString());
				}
				if (i == consolidatesummaryData.size() - 1) {
					json = new JSONObject();
					json.put("Plate No", vehicles.getPlateNo());
					json.put("Asset Code", userId);
					json.put("Date", rowConsolidate[1].toString());
					json.put("Begin Time", begin);
					json.put("Begin At", beginAt);
					json.put("End Time", end);
					json.put("End At", endAt);
					json.put("Running Dur", formatIntoHHMMSSWithOutDay(runDur));
					json.put("Stop Dur", formatIntoHHMMSSWithOutDay(stopdur));
					json.put("Idle dur", formatIntoHHMMSSWithOutDay(idleDur));
					json.put("Towed Dur", formatIntoHHMMSSWithOutDay(towedDur));
					json.put("Odometer",
							String.valueOf(nmbrformat.format(odometer)));
					jsonArray.put(json);
				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return jsonArray;
	}

	public String buildPDFDocumentr(JSONArray json, String title,
			String emails, Provider provider, JSONArray format,
			String reportName) {
		String fileName = reportName + ".pdf";
		try {

			Font titleFont = FontFactory.getFont(FontFactory.COURIER_BOLD, 15,
					BaseColor.BLACK);
			Font headerFont = FontFactory.getFont(FontFactory.COURIER_BOLD, 8,
					BaseColor.BLACK);
			Font cellFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 6,
					BaseColor.BLACK);
			final String FONT = "KacstNaskh.ttf";
			Font f = FontFactory.getFont(FONT, BaseFont.IDENTITY_H,
					BaseFont.EMBEDDED);
			Document document = new Document();
			document.setMargins(0, 0, 5, 5);

			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(fileName));
			document.open();
			String logoURl = "http://" + provider.getBucketName()
					+ ".s3.amazonaws.com/Company/" + provider.getLogoUrl();
			Image img = Image.getInstance(logoURl);
			img.setAlignment(Element.ALIGN_CENTER);
			Paragraph p1 = new Paragraph("", titleFont);
			PdfDiv pdfDiv = new PdfDiv();
			p1.add(new Chunk(title, f));
			p1.setAlignment(Element.ALIGN_CENTER);
			pdfDiv.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
			pdfDiv.addElement(p1);

			JSONArray jsonarray = format;

			PdfPTable table = new PdfPTable(jsonarray.length());
			List<String> perfectKeys = new ArrayList<String>();

			for (int i = 0; i < jsonarray.length(); i++) {
				String headerKey = jsonarray.get(i).toString();
				perfectKeys.add(headerKey);
				Paragraph cellParagraph = new Paragraph("", headerFont);
				PdfDiv cellDiv = new PdfDiv();
				cellParagraph.add(new Chunk(headerKey, f));
				cellDiv.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				cellDiv.addElement(cellParagraph);
				PdfPCell cell = new PdfPCell();
				cell.addElement(cellDiv);
				cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
				table.addCell(cell);
			}

			for (int i = 0; i < json.length(); i++) {
				JSONObject Obj = json.getJSONObject(i);
				for (int j = 0; j < perfectKeys.size(); j++) {
					String cellContent = Obj.getString(perfectKeys.get(j));
					Paragraph innerCellParagraph = new Paragraph("", cellFont);
					PdfDiv innerCellDiv = new PdfDiv();
					innerCellParagraph.add(new Chunk(cellContent, f));
					innerCellDiv.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
					innerCellDiv.addElement(innerCellParagraph);
					PdfPCell cell = new PdfPCell();
					cell.addElement(innerCellDiv);
					table.addCell(cell);
				}
			}
			document.add(img);
			document.add(pdfDiv);
			document.add(Chunk.NEWLINE);
			document.add(table);
			document.close();
		} catch (Exception e) {
			LOGGER.error("Error on buildPDFDocumentr:: " + e);
		}
		return fileName;

	}

	public String buildExcelDocumentr(JSONArray json, String title,
			String emails, Provider provider, JSONArray format,
			String reportName) {
		String fileName = reportName + ".xls";
		try {
			HSSFWorkbook work = new HSSFWorkbook();

			HSSFSheet sheet = work.createSheet("Report Data");
			Row rowHeade = sheet.createRow(0);
			Cell cellHeaderSno = rowHeade.createCell(0);
			cellHeaderSno.setCellValue("S.NO");
			for (int i = 0; i < format.length(); i++) {
				Cell cellHeader = rowHeade.createCell(i + 1);
				cellHeader.setCellValue(format.getString(i));

			}

			int rownum = 1;
			for (int k = 0; k < json.length(); k++) {
				JSONObject Obj = json.getJSONObject(k);

				Row row = sheet.createRow(rownum++);
				Cell cellser = row.createCell(0);
				cellser.setCellValue((k + 1));
				int cellnum = 1;
				for (int j = 0; j < format.length(); j++) {
					Cell cell = row.createCell(cellnum++);
					cell.setCellValue(Obj.getString(format.getString(j)));

				}

			}

			FileOutputStream out = new FileOutputStream(new File(fileName));
			work.write(out);
			out.close();

		} catch (Exception e) {
			LOGGER.error("Error on buildExcelDocumentr:: " + e);
		}
		return fileName;

	}

	private String getMaxandMinHeartbeatevent(String Date, String vin) {

		String returnVal = null;
		try {

			String heartbeateventmax = "Select TIMESTAMP(timeStamp) from fleettrackingdb.heartbeatevent where vin=:vin and Date(timeStamp)=:forDate order by timeStamp desc limit 1";
			Query query = em.createNativeQuery(heartbeateventmax);
			query.setParameter("vin", vin);
			query.setParameter("forDate", Date);
			List<Object> objmax = query.getResultList();

			String heartbeateventmin = "Select TIMESTAMP(timeStamp) from fleettrackingdb.heartbeatevent where vin=:vin and Date(timeStamp)=:forDate order by timeStamp  limit 1";
			Query query1 = em.createNativeQuery(heartbeateventmin);
			query1.setParameter("vin", vin);
			query1.setParameter("forDate", Date);
			List<Object> objmin = query1.getResultList();

			if (!(objmin.isEmpty() & objmax.isEmpty())) {

				returnVal = sdfTime.format(sdfTime.parse(objmax.get(0)
						.toString()))
						+ ","
						+ sdfTime.format(sdfTime
								.parse(objmin.get(0).toString()));
			}

		} catch (Exception e) {
			LOGGER.error("Error on getMaxandMinHeartbeatevent():: " + e);
		}
		return returnVal;

	}

	@Override
	public List<Object> vehicleListVin() {
		List<Object> listEmployeeevents = null;
		try {

			Query query = em
					.createNativeQuery("SELECT vin FROM vehicle  WHERE  vehicletype_vehicleType != 'Employee' AND status IS NULL");

			LOGGER.info("Before Execute Query query" + query);
			listEmployeeevents = (List<Object>) query.getResultList();
		} catch (Exception e) {
			LOGGER.error("Error on vehicleListVin():: " + e);
		}
		return listEmployeeevents;
	}

	@Override
	public String getConsolidateSummaryByDayReport(String vin, String eventDate) {

		JSONObject json = new JSONObject();
		int runDur = 0, stopdur = 0, idleDur = 0, towedDur = 0, maxSpeedVal = 0;
		float odometer = 0;
		try {

			Query consolData = em
					.createNativeQuery("SELECT vin,fromDate,begin,beginLocation,end,endLocation,runningDur,stopDur,idleDur,towDur,odometer FROM consolidatesummaryreport WHERE vin ='"
							+ vin + "' AND fromDate = '" + eventDate + "'");
			List<Object[]> consolidatesummaryData = (List<Object[]>) consolData
					.getResultList();

			if (consolidatesummaryData != null
					&& !consolidatesummaryData.isEmpty()) {
				Query maxSpeed = em
						.createNativeQuery("SELECT MAX(speed) FROM vehicleevent WHERE vin ='"
								+ vin
								+ "' AND DATE(eventTimeStamp) = '"
								+ eventDate + "'");
				maxSpeedVal = (int) maxSpeed.getSingleResult();
			}

			for (int i = 0; i < consolidatesummaryData.size(); i++) {
				Object[] rowConsolidate = (Object[]) consolidatesummaryData
						.get(i);

				if (i == 0) {
					json.put("vin", vin);
					json.put("Date", rowConsolidate[1].toString());
					json.put("Begin", rowConsolidate[2].toString());
					json.put("Begin At", rowConsolidate[3].toString());
				}

				if (!(rowConsolidate[6].toString()).equalsIgnoreCase("---")) {
					runDur += TimeZoneUtil.addyWithOutDay(rowConsolidate[6]
							.toString());
				}
				if (!(rowConsolidate[7].toString()).equalsIgnoreCase("---")) {
					stopdur += TimeZoneUtil.addyWithOutDay(rowConsolidate[7]
							.toString());
				}
				if (!(rowConsolidate[8].toString()).equalsIgnoreCase("---")) {
					idleDur += TimeZoneUtil.addyWithOutDay(rowConsolidate[8]
							.toString());
				}
				if (!(rowConsolidate[9].toString()).equalsIgnoreCase("---")) {
					towedDur += TimeZoneUtil.addyWithOutDay(rowConsolidate[9]
							.toString());
				}
				if (!(rowConsolidate[10].toString()).equalsIgnoreCase("---")) {
					odometer += Float.parseFloat(rowConsolidate[10].toString());
				}

				if (i == consolidatesummaryData.size() - 1) {
					json.put("End", rowConsolidate[4].toString());
					json.put("End At", rowConsolidate[5].toString());
					json.put("Running", formatIntoHHMMSSWithOutDay(runDur));
					json.put("Stop", formatIntoHHMMSSWithOutDay(stopdur));
					json.put("Idle", formatIntoHHMMSSWithOutDay(idleDur));
					json.put("Towed", formatIntoHHMMSSWithOutDay(towedDur));
					json.put("Odometer", String.valueOf(Math.round(odometer)));
					json.put("maxspeed", String.valueOf(maxSpeedVal));

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();

	}

	@Override
	public String generateNoTransAlert(String timeZone,
			Alertconfig alertConfig, String vin, String plateNo,
			String lstTransDate, String latlang, Provider providerNoTrans,
			String OperatorName, String serverTime, String heartBeat) {
		try {
			List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();
			String curDtTim = "", curDt = "";
			String[] alertConfigSize = (alertConfig.getRefInterval())
					.split("_");
			int alertConSz = alertConfigSize.length;
			int alertCount = 0, min = 0;
			long LastTransdiff = 0, LastTransdiffSecondsonlySec = 0, LastAlertTransdiff = 0, LastAlertdiffSecondsonlysec = 0;
			Calendar calendar = TimeZoneUtil.getDateForTimeZones(new Date(),
					timeZone);
			curDtTim = TimeZoneUtil.getTimeINYYYYMMddss(calendar.getTime());
			curDt = TimeZoneUtil.getDate(calendar.getTime());

			alertCount = getNoTransmissionAlertCnt(vin, curDt);
			Date d1 = TimeZoneUtil.getDateYYYYMMDDHHMMSS(curDtTim);
			Date serverT = TimeZoneUtil.getDateYYYYMMDDHHMMSS(serverTime);
			Date d2 = TimeZoneUtil.getDateYYYYMMDDHHMMSS(lstTransDate);
			Date d3 = TimeZoneUtil
					.getDateYYYYMMDDHHMMSS(getlastAlertTimeForVin(vin));
			Date lstHeatBetEvent = TimeZoneUtil
					.getDateYYYYMMDDHHMMSS(heartBeat);
			if (d2 != null && lstHeatBetEvent != null || d2 == null
					&& lstHeatBetEvent != null) {
				LastTransdiff = d1.getTime() - lstHeatBetEvent.getTime();
				LastTransdiffSecondsonlySec = LastTransdiff / 1000;
			}
			if (d2 != null && lstHeatBetEvent == null) {
				LastTransdiff = d1.getTime() - d2.getTime();
				LastTransdiffSecondsonlySec = LastTransdiff / 1000;
			}

			if ((alertCount + 1) == 1 && alertConSz == 1
					|| (alertCount + 1) == 1 && alertConSz == 2
					|| (alertCount + 1) == 1 && alertConSz == 3) {
				min = 0;
			} else if ((alertCount + 1) == 2 && alertConSz == 2
					|| (alertCount + 1) == 2 && alertConSz == 3) {
				min = Integer
						.valueOf((alertConfig.getRefInterval()).split("_")[1]) * 60;
			} else if ((alertCount + 1) == 3 && alertConSz == 3) {
				min = Integer
						.valueOf((alertConfig.getRefInterval()).split("_")[2]) * 60;
			}
			if (d3 != null) {
				LastAlertTransdiff = d1.getTime() - d3.getTime();
				LastAlertdiffSecondsonlysec = LastAlertTransdiff / 1000;
			} else {
				LastAlertdiffSecondsonlysec = min;
			}
			if (LastTransdiffSecondsonlySec > 3600 && (alertCount + 1) < 4) {
				if (LastAlertdiffSecondsonlysec >= min) {
					String description = "";
					if (lstHeatBetEvent == null) {
						description = "No Transmisson: Vehicle: " + plateNo
								+ " Current Time:" + curDtTim
								+ ", Last Transmission Time " + lstTransDate;
					} else {
						description = "No Transmisson: Vehicle: " + plateNo
								+ " Current Time:" + curDtTim
								+ ", Last Transmission Time " + heartBeat;
					}
					String mobile2 = null;
					mobile2 = alertConfig.getSmsNumber();
					Vehiclealerts va = new Vehiclealerts();
					va.setAlerttype("NOTRANSMISSION");
					va.setDescription(description);
					va.setEventTimeStamp(d1);
					va.setServerTimeStamp(serverT);
					va.setLatlng(latlang);
					va.setSmsmobile(mobile2);
					va.setVin(vin);
					va.setShowstatus(false);
					vehiclealerts.add(va);
				}
			}
			if (!vehiclealerts.isEmpty()) {
				Date currentStartDateTimeNoTrans = TimeZoneUtil
						.getDateTimeZone(new Date(), timeZone);
				String strexpectedTime = TimeZoneUtil
						.getTimeINYYYYMMddss(currentStartDateTimeNoTrans);
				String mode = null;
				for (Vehiclealerts vehiclealert : vehiclealerts) {
					/************* SMS Validation *****************************************/
					String smsHttpStatus = "not sent";
					boolean smsStatus = false;
					boolean isSmsBalAvail;
					if (OperatorName != null) {
						vehiclealert.setDescription(vehiclealert
								.getDescription()
								+ " Operator ID: "
								+ OperatorName);
					}
					String[] through = alertConfig.getThroughSms().split("#");
					for (int j = 0; j < through.length; j++) {
						if (through[j].equalsIgnoreCase("1")) {
							String[] mobile = alertConfig.getSmsNumber().split(
									"#");
							if (mobile[j] != null) {

								isSmsBalAvail = isSmsBalAvail(vehiclealert);
								if (isSmsBalAvail) {
									smsHttpStatus = SMSSendHttpClient.sendSMS(
											providerNoTrans.getSmsApi(),
											mobile[j], vehiclealert
													.getDescription()
													.replaceAll("%0D", " "));

									if (smsHttpStatus.equalsIgnoreCase("OK")) {
									}
								}

							}
						}
					}
					/************* SMS Validation *****************************************/
					vehiclealert.setSmsstatus(smsStatus);
					insertVehicleAlert(vehiclealert);
					// em.persist(vehiclealert);

					if (mode == null) {
						/*
						 * pushNotification(alertConfig.getId().getCompanyId(),
						 * alertConfig.getId().getUserId(), vehiclealert
						 * .getDescription() .replaceAll("%0D", " "));
						 */
					}

					/*************
					 * Email Validation
					 *****************************************/
					String[] email = null, fstSpltmail = null, scndSpltmail = null;
					String emailHttpStatus = "failure";
					String[] throughNoTrans = alertConfig.getThroughEmail()
							.split("#");
					for (int k = 0; k < throughNoTrans.length; k++) {
						if (throughNoTrans[k].equalsIgnoreCase("1")) {
							fstSpltmail = alertConfig.getEmailAddress().split(
									"_");
						}
					}

					for (int l = 0; l < fstSpltmail.length; l++) {
						if ((alertCount + 1) == 1) {
							if (l == 0)
								scndSpltmail = fstSpltmail[l].split(":");
						} else if ((alertCount + 1) == 2) {
							if (l == 1)
								scndSpltmail = fstSpltmail[l].split(":");
						} else if ((alertCount + 1) == 3) {
							if (l == 2)
								scndSpltmail = fstSpltmail[l].split(":");
						}

					}

					for (int m = 0; m < scndSpltmail.length; m++) {
						if (m == 1) {
							if (scndSpltmail[m].contains("#")) {
								email = scndSpltmail[m].split("#");
							} else {
								email = scndSpltmail;
							}
						}

					}

					for (int d = 0; d < email.length; d++) {
						if (email[d] != null) {
							if (isEmailValidOrNot(email[d])) {
								if (providerNoTrans.getMailMode()
										.equalsIgnoreCase("gmail")) {
									emailHttpStatus = EmailSendHttpClient.send(
											email[d], "Alert "
													+ alertConfig.getId()
															.getAlertType(),
											vehiclealert.getDescription()
													.replaceAll("%0D", " "),
											providerNoTrans.getAlertMailId(),
											providerNoTrans
													.getAlertMailPasscode());
								} else if (providerNoTrans.getMailMode()
										.equalsIgnoreCase("amazon")) {
									AmazonSMTPMail amazonSMTPMail = new AmazonSMTPMail();
									try {
										amazonSMTPMail
												.sendEmail(
														providerNoTrans
																.getAmazonVerifiedFromEmail(),
														email[d],
														"Alert "
																+ alertConfig
																		.getId()
																		.getAlertType(),
														vehiclealert
																.getDescription()
																.replaceAll(
																		"%0D",
																		" "),
														providerNoTrans
																.getAmazonSmtpUserName(),
														providerNoTrans
																.getAmazonSmtpPassword(),
														providerNoTrans
																.getAmazonHostAddress(),
														providerNoTrans
																.getAmazonPort());
									} catch (MessagingException e) {
										// LOGGER.info("Email Exception"+
										// e.getMessage());
										e.printStackTrace();
									}
								}
								if (emailHttpStatus.equalsIgnoreCase("message")) {
									// LOGGER.info("Email Sent Successfully");
								}
							}
						}
					}

				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public void insertConsolidateSummaryByDayReport(String vin,
			String eventDate, String reportDatasByDay) {

		LOGGER.info("SummaryEJB::insertConsolidateSummaryByDayReport::Entered into this method"
				+ "vin"
				+ vin
				+ "eventDate"
				+ eventDate
				+ "reportDatas"
				+ reportDatasByDay);
		try {

			JSONObject jsonObject = new JSONObject(reportDatasByDay);

			Consolidatesummarybydayreport consolidatesummarybydayreport = new Consolidatesummarybydayreport();
			ConsolidatesummarybydayreportId id = new ConsolidatesummarybydayreportId();
			id.setVin(vin);
			id.setFromDate(eventDate);
			id.setBegin(jsonObject.getString("Begin"));
			id.setBeginLocation(jsonObject.getString("Begin At"));
			id.setEnd(jsonObject.getString("End"));
			id.setEndLocation(jsonObject.getString("End At"));
			id.setRunningDur(jsonObject.getString("Running"));
			id.setStopDur(jsonObject.getString("Stop"));
			id.setIdleDur(jsonObject.getString("Idle"));
			id.setTowDur(jsonObject.getString("Towed"));
			id.setOdometer(jsonObject.getString("Odometer"));
			id.setMaxSpeed(jsonObject.getInt("maxspeed"));
			consolidatesummarybydayreport.setId(id);
			em.persist(consolidatesummarybydayreport);

		} catch (Exception e) {
			LOGGER.error("SummaryEJB::ConsolidateSummaryByDayReport::Exception occured in this method insertConsolidateSummaryByDayReport "
					+ e);
		}
		LOGGER.info("SummaryEJB::insertConsolidateSummaryByDayReport::Leaving from this method is successfully");

	}

	@Override
	public String getConsolidateSummaryByDayReportTotal(String vin,
			String userId, String fromDate, String todate) {
		// TODO Auto-generated method stub

		JSONObject json = null;
		long runDur = 0, stopdur = 0, idleDur = 0, towedDur = 0;
		float odometer = 0;
		try {
			Vehicle vehicles = em.find(Vehicle.class, vin);
			LOGGER.info("SummaryEJB::vehicleList::Entered into method:: getConsolidateSummaryByDayReportTotal"
					+ TIMEZONEID);

			Query consolData = em
					.createNativeQuery("SELECT vin,fromDate,BEGIN,beginLocation,END,endLocation,runningDur,stopDur,idleDur,towDur,odometer FROM consolidatesummarybydayreport WHERE vin ='"
							+ vin
							+ "' AND fromDate BETWEEN '"
							+ fromDate
							+ "' AND '" + todate + "'");
			List<Object[]> consolidatesummaryData = (List<Object[]>) consolData
					.getResultList();
			json = new JSONObject();
			for (int i = 0; i < consolidatesummaryData.size(); i++) {
				Object[] rowConsolidate = (Object[]) consolidatesummaryData
						.get(i);
				if (i == 0) {
					json.put("Plate No", vehicles.getPlateNo().toString());
					json.put("Asset Code", userId);
					json.put("Date", rowConsolidate[1].toString());
					json.put("Begin Time", rowConsolidate[2].toString());
					json.put("Begin At", rowConsolidate[3].toString());
				}

				if (!(rowConsolidate[6].toString()).equalsIgnoreCase("---")) {
					runDur += TimeZoneUtil.addyWithOutDay(rowConsolidate[6]
							.toString());
				}
				if (!(rowConsolidate[7].toString()).equalsIgnoreCase("---")) {
					stopdur += TimeZoneUtil.addyWithOutDay(rowConsolidate[7]
							.toString());
				}
				if (!(rowConsolidate[8].toString()).equalsIgnoreCase("---")) {
					idleDur += TimeZoneUtil.addyWithOutDay(rowConsolidate[8]
							.toString());
				}
				if (!(rowConsolidate[9].toString()).equalsIgnoreCase("---")) {
					towedDur += TimeZoneUtil.addyWithOutDay(rowConsolidate[9]
							.toString());
				}
				if (!(rowConsolidate[10].toString()).equalsIgnoreCase("---")) {
					odometer += Float.parseFloat(rowConsolidate[10].toString());
				}

				if (i == consolidatesummaryData.size() - 1) {
					json.put("End Time", rowConsolidate[4].toString());
					json.put("End At", rowConsolidate[5].toString());
					json.put("Running Dur", sumofHours(runDur));
					json.put("Stop Dur", sumofHours(stopdur));
					json.put("Idle dur", sumofHours(idleDur));
					json.put("Towed Dur", sumofHours(towedDur));
					json.put("Odometer", String.valueOf(Math.round(odometer)));
				}

			}

		} catch (Exception e) {
			LOGGER.error("SummaryEJB::getConsolidateSummaryByDayReportTotal::Exception occured in this method getConsolidateSummaryByDayReportTotal "
					+ e);
		}
		return json.toString();
	}

	@Override
	public List<Alertconfig> getAlertConfigNoTransmission(String vin) {
		LOGGER.info("AlertsEJB::getAlertConfig::Entered into this method"
				+ "vin" + vin);
		try {
			String getVehicleConfig = "SELECT ac FROM Alertconfig ac WHERE vin = '"
					+ vin + "' AND alertType = 'NOTRANSMISSION'";
			Query vehiConfigQuery = em.createQuery(getVehicleConfig);
			LOGGER.info("Before Query Excecute Query::" + vehiConfigQuery);
			List<Alertconfig> alertConfigs = (List<Alertconfig>) vehiConfigQuery
					.getResultList();
			return alertConfigs;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::getAlertConfig::Error in getting alert config for vin="
					+ vin + " : e");
			return null;
		}
	}

	@Override
	public String getOperatorName(String vin) {
		// TODO Auto-generated method stub
		try {
			Query opNameQry = em
					.createNativeQuery("SELECT op.name FROM operator op WHERE operatorid =(SELECT operator_operatorid FROM vehicle_has_operator WHERE vehicle_vin='"
							+ vin + "' AND effTo IS NULL)");
			List<Object> objs = (List<Object>) opNameQry.getResultList();
			for (Object object : objs) {
				if (object != null) {
					String opName = (String) object;
					return opName;
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in getOperatorName:: " + e);
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Object[]> getnoTransAlertVins() {
		String stmt = "";
		int cnt = 0;
		List<Object[]> finalData = null;
		try {
			Query noTransVin = em
					.createNativeQuery("SELECT ac.vin,ac.companyId FROM alertconfig ac LEFT JOIN vehicle v ON ac.vin = v.vin WHERE v.status IS NULL AND ac.alertType = 'NOTRANSMISSION'");
			List<Object[]> listnoTransVin = (List<Object[]>) noTransVin
					.getResultList();
			LOGGER.error("NoTransmissionAlert::Entering CronJobKSA Class Summary only vins Count : "
					+ listnoTransVin.size());
			if (listnoTransVin != null && !listnoTransVin.isEmpty()
					&& listnoTransVin.size() != 0) {
				for (int i = 0; i < listnoTransVin.size(); i++) {
					Object[] rowConfigvin = (Object[]) listnoTransVin.get(i);
					String vin = rowConfigvin[0].toString();

					stmt = stmt
							+ "(SELECT v.vin,v.latitude,v.longitude,v.eventTimeStamp,v.serverTimeStamp FROM vehicleevent v WHERE v.vin ='"
							+ vin
							+ "' AND eventTimeStamp = (SELECT MAX(eventTimeStamp) FROM vehicleevent WHERE vin='"
							+ vin + "' ORDER BY eventTimeStamp DESC))";

					if (cnt < listnoTransVin.size() - 1) {
						stmt = stmt + " " + STR_UNION + " ";
					}
					cnt++;
				}
				if (!stmt.equalsIgnoreCase("")) {
					Query query2 = em.createNativeQuery(stmt);
					finalData = (List<Object[]>) query2.getResultList();
					LOGGER.error("NoTransmissionAlert::Entering CronJobKSA Class Summary event and vins Count : "
							+ finalData.size());
				}
			}

		} catch (Exception e) {
			LOGGER.error("NoTransmissionAlert::Entering CronJobKSA Class Summary"
					+ e);
		}
		return finalData;
	}

	@Override
	public int getNoTransmissionAlertCnt(String vin, String curDate) {
		int count = 0;
		LOGGER.info("AlertsEJB::getNoTransmissionAlertCnt::Entered into this method"
				+ "vin" + vin);
		try {
			String getAlertCnt = "SELECT COUNT(vin) FROM vehiclealerts WHERE vin = '"
					+ vin
					+ "' AND alertType = 'NOTRANSMISSION' AND DATE(eventTimeStamp) = '"
					+ curDate + "'";
			Query getAlertcnt1 = em.createNativeQuery(getAlertCnt);
			count = Integer.valueOf(getAlertcnt1.getSingleResult().toString());
			return count;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::getAlertConfig::Error in getting alert config for vin="
					+ vin + " : e");
			return 0;
		}

	}

	public boolean isSmsBalAvail(Vehiclealerts vehiclealerts) {
		LOGGER.info("AlertsEJB::isSmsBalAvail::" + "vehiclealerts"
				+ vehiclealerts);
		String vin = vehiclealerts.getVin();
		Vehicle vehicle = getVehicle(vin);
		String chkSmsCntBalance = "SELECT smsCnt FROM ltmscompanyadmindb.smsconfig WHERE companyId ='"
				+ vehicle.getCompanyId()
				+ "' and branchId ='"
				+ vehicle.getBranchId()
				+ "' and status=1 and smsCnt is not null";

		Query query = emAdmin.createNativeQuery(chkSmsCntBalance);
		int smsCnt = 0;
		try {
			LOGGER.info("Before Execute Query query" + query);
			List<BigInteger> objects = (List<BigInteger>) query.getResultList();
			if (!objects.isEmpty() && objects.size() != 0) {
				smsCnt = objects.get(0).intValue();
				LOGGER.info("After execute Query query" + query);
				LOGGER.info("AlertsEJB::isSmsBalAvail:: Company ="
						+ vehicle.getCompanyId() + " and smsCnt =" + smsCnt);
			}
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::isSmsBalAvail::Exception occured sms cnt balance error is "
					+ e);
			return false;
		}
		return (smsCnt > 0) ? true : false;

	}

	public Vehicle getVehicle(String vin) {
		return em.find(Vehicle.class, vin);
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

	@Override
	public boolean insertVehicleAlert(Vehiclealerts vehiclealerts) {
		LOGGER.info("AlertsEJB::insertVehicleAlert::" + "vehiclealerts"
				+ vehiclealerts);
		try {
			em.persist(vehiclealerts);
			return true;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::insertVehicleAlert::error in persisting VehicleAlert"
					+ e);
			return false;
		}
	}

	public String getlastAlertTimeForVin(String vin) {
		String lastAlTmeresult = "";
		try {
			Query query = em
					.createNativeQuery("SELECT MAX(va.eventTimeStamp) AS lastTransTime FROM vehiclealerts va WHERE va.vin = '"
							+ vin
							+ "' AND va.alerttype = 'NOTRANSMISSION' ORDER BY va.eventTimeStamp DESC");
			Object lastAlTme = (Object) query.getSingleResult();
			if (lastAlTme != null)
				lastAlTmeresult = lastAlTme.toString();
			else
				lastAlTmeresult = "";
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::insertVehicleAlert::error in persisting VehicleAlert"
					+ e);
			return "fail";
		}
		return lastAlTmeresult;

	}

	@Override
	public String getnoTransHeartBeatEvent(String vin) {
		String maxHeartBeatEvent = "";
		try {
			Query query = em
					.createNativeQuery("SELECT MAX(he.timeStamp) FROM heartbeatevent he WHERE he.vin = '"
							+ vin + "' ORDER BY he.timeStamp DESC");
			Object maxBeat = (Object) query.getSingleResult();
			if (maxBeat != null)
				maxHeartBeatEvent = maxBeat.toString();
			else
				maxHeartBeatEvent = "";
		} catch (Exception e) {
			LOGGER.error("SummaryEJB::getnoTransHeartBeatEvent::error in persisting VehicleAlert"
					+ e);
			return "fail";
		}
		return maxHeartBeatEvent;
	}

	private String sumofHours(long secTotal) {
		int hours = (int) secTotal / 3600;
		int remainder = (int) secTotal - hours * 3600;
		int minutes = remainder / 60;
		remainder = remainder - minutes * 60;
		int seconds = remainder;
		return hours + ":" + minutes + ":" + seconds;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getdataMigrationVins() {
		List<String> listnoTransVin = null;
		try {
			Query noTransVin = em
					.createNativeQuery("SELECT v.vin FROM vehicle v WHERE v.status IS NULL AND active = 1");
			listnoTransVin = (List<String>) noTransVin.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listnoTransVin;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getLatestDataandMigrate(String migrationVin) {
		// TODO Auto-generated method stub
		try {
			Query query = em
					.createNativeQuery("select v.vin,v.eventTimeStamp,v.serverTimeStamp,v.latitude,v.longitude,v.speed,v.engine,v.ioevent,v.tempSensor1,v.tempSensor2,v.tempSensor3,v.odometer,v.battery,"
							+ "v.ai1,v.ai2,v.ai3,v.ai4,v.di1,v.di2,v.di3,v.di4,v.direction,v.tags from vehicleevent v where v.eventTimeStamp = "
							+ "(select max(t.eventTimeStamp) from vehicleevent t where t.vin='"
							+ migrationVin
							+ "') AND v.vin='"
							+ migrationVin
							+ "'");

			List<Object[]> migrateEventData = (List<Object[]>) query
					.getResultList();
			if (!migrateEventData.isEmpty() && migrateEventData.size() != 0
					&& migrateEventData != null) {
				for (int i = 0; i < migrateEventData.size(); i++) {
					Object[] migrateObj = (Object[]) migrateEventData.get(i);
					String checkAvailVin = migrateObj[0].toString();
					Vehicle vehicle_VE = em.find(Vehicle.class, checkAvailVin);
					if (vehicle_VE != null) {
						vehicle_VE.setVEventTimeStamp((Date) migrateObj[1]);
						vehicle_VE.setVServerTimeStamp((Date) migrateObj[2]);
						vehicle_VE.setVLatitude(Double
								.parseDouble(migrateObj[3].toString()));
						vehicle_VE.setVLongitude(Double
								.parseDouble(migrateObj[4].toString()));
						vehicle_VE.setVSpeed(Integer.parseInt(migrateObj[5]
								.toString()));

						// Get Previous Odometer
						long totOdometerPerDay = getOdometerPerDay(
								migrationVin, sdfDate.format(new Date()));

						long prevOdometer_KM = 0L;
						Object[] odometers = getPreviousOdometerValue(migrationVin);
						if (odometers[0] != null) {
							prevOdometer_KM = Long.parseLong(odometers[0]
									.toString());
						}

						vehicle_VE
								.setVOdometer((long) (totOdometerPerDay + (prevOdometer_KM * 1000)));

						vehicle_VE.setVEngine((Boolean) migrateObj[6]);

						vehicle_VE
								.setVIoevent(migrateObj[7] != null ? migrateObj[7]
										.toString() : "");

						if (migrateObj[8] != null) {
							vehicle_VE.setVTempSensor1(Long
									.parseLong(migrateObj[8].toString()));
						}
						if (migrateObj[9] != null) {
							vehicle_VE.setVTempSensor2(Long
									.parseLong(migrateObj[9].toString()));
						}
						if (migrateObj[10] != null) {
							vehicle_VE.setVTempSensor3(Long
									.parseLong(migrateObj[10].toString()));
						}

						if (migrateObj[12] != null) {
							vehicle_VE.setVBattery(Long
									.parseLong(migrateObj[12].toString()));
						}
						if (migrateObj[13] != null) {
							vehicle_VE.setVAi1((Integer) (migrateObj[13]));
						}
						if (migrateObj[14] != null) {
							vehicle_VE.setVAi2((Integer) (migrateObj[14]));
						}
						if (migrateObj[15] != null) {
							vehicle_VE.setVAi3((Integer) (migrateObj[15]));
						}
						if (migrateObj[16] != null) {
							vehicle_VE.setVAi4((Integer) (migrateObj[16]));
						}
						if (migrateObj[17] != null) {
							vehicle_VE.setVDi1((Integer) (migrateObj[17]));
						}
						if (migrateObj[18] != null) {
							vehicle_VE.setVDi2((Integer) (migrateObj[18]));
						}
						if (migrateObj[19] != null) {
							vehicle_VE.setVDi3((Integer) (migrateObj[19]));
						}
						if (migrateObj[20] != null) {
							vehicle_VE.setVDi4((Integer) (migrateObj[20]));
						}
						if (migrateObj[21] != null) {
							vehicle_VE
									.setVDirection((Integer) (migrateObj[21]));
						}
						if (migrateObj[22] != null) {
							vehicle_VE.setVTags(migrateObj[22].toString());
						}
						em.persist(vehicle_VE);
						LOGGER.error("SummaryEJB::Data Migration :: Persis Into Event Vehicle Table"
								+ checkAvailVin);
					} else {
						LOGGER.error("SummaryEJB::Data Migration :: Vin Not In Vehicle Table"
								+ checkAvailVin);
					}
				}

			}

			Query query12 = em
					.createNativeQuery("select vin,timeStamp,engine,gps,gsm,batteryVoltage,powerSupply,remarks from heartbeatevent v where v.timeStamp = (select max(t.timeStamp) from heartbeatevent t where t.vin='"
							+ migrationVin
							+ "') AND v.vin='"
							+ migrationVin
							+ "'");
			List<Object[]> hearbetTableData = (List<Object[]>) query12
					.getResultList();
			if (!hearbetTableData.isEmpty() && hearbetTableData.size() != 0
					&& hearbetTableData != null) {
				for (int t = 0; t < hearbetTableData.size(); t++) {
					Object[] migrateObjNoheart = (Object[]) hearbetTableData
							.get(t);

					if (migrateObjNoheart != null) {
						String heartVin = migrateObjNoheart[0].toString();
						Vehicle vehicle_HB = em.find(Vehicle.class, heartVin);
						if (vehicle_HB != null) {
							if (migrateObjNoheart[1] != null) {
								vehicle_HB
										.setVTimeStamp((Date) migrateObjNoheart[1]);
							}

							if (migrateObjNoheart[2] != null) {
								vehicle_HB
										.setVHeartBeatEngine((Boolean) migrateObjNoheart[2]);
							}

							if (migrateObjNoheart[3] != null) {
								vehicle_HB.setVGps(migrateObjNoheart[3]
										.toString());
							}

							if (migrateObjNoheart[4] != null) {
								vehicle_HB.setVGsm(Integer
										.parseInt(migrateObjNoheart[4]
												.toString()));
							}

							if (migrateObjNoheart[5] != null) {
								vehicle_HB
										.setVBatteryVoltage(migrateObjNoheart[5]
												.toString());
							}

							if (migrateObjNoheart[6] != null) {
								vehicle_HB.setVPowerSupply(migrateObjNoheart[6]
										.toString());
							}
							if (migrateObjNoheart[7] != null) {
								vehicle_HB.setVRemarks(migrateObjNoheart[7]
										.toString());
							}

							em.persist(vehicle_HB);
							LOGGER.error("SummaryEJB::Data Migration :: Persis Into HeartBeatEvent Vehicle Table"
									+ heartVin);
						}
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Success";
	}

	@Override
	public String getSpeedSumaryReportDay(String vin, String eventDate) {
		JSONArray jsonArray_overSpeed = new JSONArray();

		try {
			String strForDay = "select v from Vehicleevent v where v.id.vin=:vin and v.id.eventTimeStamp between :eventTimeStamp and :toeventTimeStamp order by v.id.eventTimeStamp";
			Query queryStrForDay = em.createQuery(strForDay);
			queryStrForDay.setParameter("vin", vin);
			queryStrForDay.setParameter(EVENTTIMESTAMP,
					sdfTime.parse(eventDate + " " + START_TIME));
			queryStrForDay.setParameter(TO_EVENTTIMESTAMP,
					sdfTime.parse(eventDate + " " + END_TIME));
			LOGGER.debug(" Before strForDay Execute Query " + strForDay);
			List<Vehicleevent> vehicleevents = (List<Vehicleevent>) queryStrForDay
					.getResultList();
			LOGGER.debug("After strForDay Execute Query "
					+ vehicleevents.size());
			Vehicle vehicles = em.find(Vehicle.class, vin.trim());

			String strForalertcofig = "select ac from Alertconfig ac where ac.id.vin=:vin and ac.id.alertType='OVERSPEED'";
			Query querystrForalertcofig = em.createQuery(strForalertcofig);
			querystrForalertcofig.setParameter("vin", vin);
			List<Alertconfig> alertconfig = (List<Alertconfig>) querystrForalertcofig
					.getResultList();
			String prevStaus_OverSpeed = "", preLatlong_OverSpeed = "", curLatLong_OverSpeed = "";
			Date preDate_OverSpeed = null, cureDate_OverSpeed = null;
			long sum = 0;
			int min = 0, max = 0, count = 0, configSpeed;
			boolean isOverspeed = false;

			String plateno = vehicles.getPlateNo();
			if (!alertconfig.isEmpty())
				configSpeed = Integer.parseInt(alertconfig.get(0)
						.getAlertRange());
			else
				configSpeed = 60;

			for (int i = 0; i < vehicleevents.size(); i++) {

				Vehicleevent ve = vehicleevents.get(i);

				if (i == 0 && ve.getSpeed() >= configSpeed) {
					preLatlong_OverSpeed = ve.getLatitude() + ","
							+ ve.getLongitude();
					preDate_OverSpeed = ve.getId().getEventTimeStamp();
					min = ve.getSpeed();
					max = ve.getSpeed();
					sum += ve.getSpeed();
					count++;
					isOverspeed = true;
				} else {
					if (ve.getSpeed() >= configSpeed && !isOverspeed) {
						preLatlong_OverSpeed = ve.getLatitude() + ","
								+ ve.getLongitude();
						preDate_OverSpeed = ve.getId().getEventTimeStamp();
						min = ve.getSpeed();
						max = ve.getSpeed();
						sum += ve.getSpeed();
						count++;
						isOverspeed = true;

					} else if (ve.getSpeed() >= configSpeed && isOverspeed) {

						if (min > ve.getSpeed())
							min = ve.getSpeed();
						if (max < ve.getSpeed())
							max = ve.getSpeed();
						sum += ve.getSpeed();
						count++;

					} else if (!(ve.getSpeed() >= configSpeed) && isOverspeed) {

						curLatLong_OverSpeed = ve.getLatitude() + ","
								+ ve.getLongitude();
						cureDate_OverSpeed = ve.getId().getEventTimeStamp();

						JSONObject json = new JSONObject();
						json.put("Plate No", plateno);
						json.put("Begin", sdfTime.format(preDate_OverSpeed));
						json.put("Begin At", preLatlong_OverSpeed);
						json.put("End At", curLatLong_OverSpeed);
						json.put("End", sdfTime.format(cureDate_OverSpeed));
						json.put(
								"OverDuration",
								formatIntoHHMMSSWithOutDay((addy(sdfHHMMSS
										.format(cureDate_OverSpeed)) - (addy(sdfHHMMSS
										.format(preDate_OverSpeed))))));
						json.put("MaxSpeed", max);
						json.put("MinSpeed", min);
						json.put("AvgSpeed", sum / count);
						jsonArray_overSpeed.put(json);
						isOverspeed = false;
						count = 0;

					}

				}

			}

		} catch (Exception e) {
			LOGGER.error("consolidatesummaryreport ::" + e);
			e.printStackTrace();
		}

		return jsonArray_overSpeed.toString();

	}

	@Override
	public void insertOverSpeedSummary(String vin, String eventDate,
			String newreportDatasDay) {
		LOGGER.info("SummaryEJB::insertOverSpeedSummary::Entered into this method"
				+ "vin"
				+ vin
				+ "eventDate"
				+ eventDate
				+ "reportDatas"
				+ newreportDatasDay);
		try {
			// int totRunDur = 0, totStopDur = 0, totIdleDur = 0, totTowedDur =
			// 0;

			JSONArray jsonArray = new JSONArray(newreportDatasDay);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = new JSONObject(jsonArray.get(i)
						.toString());

				Overspeedsummaryreport summaryreport = new Overspeedsummaryreport();
				OverspeedsummaryreportId id = new OverspeedsummaryreportId();
				id.setVin(vin);
				id.setFromDate(eventDate);
				id.setBegin(jsonObject.getString("Begin"));
				id.setBeginLocation(jsonObject.getString("Begin At"));
				id.setEnd(jsonObject.getString("End"));
				id.setEndLocation(jsonObject.getString("End At"));
				id.setOverSpeedDuration(jsonObject.getString("OverDuration"));
				id.setMax(jsonObject.getString("MaxSpeed"));
				id.setMin(jsonObject.getString("MinSpeed"));
				id.setAvg(jsonObject.getString("AvgSpeed"));

				summaryreport.setId(id);
				em.persist(summaryreport);
			}

		} catch (Exception e) {
			LOGGER.error("SummaryEJB::insertOverSpeedSummary::Exception occured in this method insertVehicleDaySummary "
					+ e);
		}
		LOGGER.info("SummaryEJB::insertOverSpeedSummary::Leaving from this method is successfully");
	}

	@Override
	public void resetEngineHours() {
		try {
			Query query = em
					.createNativeQuery("UPDATE fleettrackingdb.vehicle SET EngineHours = '0'");
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public JSONObject getSmsReportForSchool(String compID, String currentDate) {
		JSONObject resultObj = new JSONObject();
		JSONArray heading;
		JSONArray data;
		try {
			heading = new JSONArray();
			data = new JSONArray();
			JSONObject alerttypes = getAlertTypes();
			/*
			 * Query queryForHeading = emStudent .createNativeQuery(
			 * "SELECT GROUP_CONCAT(CASE WHEN(TIME_TO_SEC(al.eventTimeStamp)< '43200') THEN CONCAT(alertType) END) AS morning, GROUP_CONCAT(CASE WHEN(TIME_TO_SEC(al.eventTimeStamp)> '43200') THEN CONCAT(alertType) END) AS evening FROM `studenttrackingdb`.`alertevents` AS al INNER JOIN `studenttrackingdb`.`schoolroute` AS v ON al.vin=v.vin LEFT JOIN `studenttrackingdb`.`studentdetails` AS sd ON al.stin=sd.stin WHERE v.compId='"
			 * + compID + "' AND al.eventTimeStamp BETWEEN '" + currentDate +
			 * " 00:00:00' AND '" + currentDate +
			 * " 23:59:59' AND al.contactNo NOT IN('AAG','NAM') GROUP BY sd.`schoolId`"
			 * ); List<Object[]> resultForHeaders = (List<Object[]>)
			 * queryForHeading .getResultList(); Object[] colObj = (Object[])
			 * resultForHeaders.get(0);
			 */
			String headers = getPreferencesData("smsMailHeaders", compID);
			JSONObject headerObj = new JSONObject(headers);
			JSONArray morningTemp = headerObj.getJSONArray("morning");
			JSONArray eveningTemp = headerObj.getJSONArray("evening");

			String sql1 = "SELECT sd.firstName AS StudentName,";
			heading.put("StudentName");

			if (morningTemp.length() != 0) {
				// String morning = colObj[0].toString();

				// LinkedHashSet<String> morningArray = new
				// LinkedHashSet<String>();

				// String temp1[] = morning.split(",");

				// for (int i = 0; i < morningTemp.length; i++) {
				// morningArray.add(temp1[i]);
				// }

				// List<String> details1 = new ArrayList<>(morningArray);

				for (int j = 0; j < morningTemp.length(); j++) {
					sql1 += " GROUP_CONCAT(CASE WHEN(TIME_TO_SEC(al.eventTimeStamp)< '43200' AND al.alerttype='"
							+ morningTemp.getString(j)
							+ "') THEN 'send' END) AS Morning_"
							+ alerttypes.getString(morningTemp.getString(j))
							+ ",";
					heading.put("Morning_"
							+ alerttypes.getString(morningTemp.getString(j)));
				}
			}
			if (eveningTemp.length() != 0) {

				/*
				 * String evening = colObj[1] == null ? "" :
				 * colObj[1].toString();
				 * 
				 * LinkedHashSet<String> eveningArray = new
				 * LinkedHashSet<String>();
				 * 
				 * String temp2[] = evening.split(","); for (int i = 0; i <
				 * temp2.length; i++) { eveningArray.add(temp2[i]); }
				 * List<String> details2 = new ArrayList<>(eveningArray);
				 */
				for (int j = 0; j < eveningTemp.length(); j++) {
					sql1 += " GROUP_CONCAT(CASE WHEN(TIME_TO_SEC(al.eventTimeStamp)> '43200' AND al.alerttype='"
							+ eveningTemp.getString(j)
							+ "') THEN 'send' END) AS Evening_"
							+ alerttypes.getString(eveningTemp.getString(j))
							+ ",";
					heading.put("Evening_"
							+ alerttypes.getString(eveningTemp.getString(j)));
				}
			}

			sql1 += " al.contactNo AS ContactNo FROM `studenttrackingdb`.`alertevents` AS al INNER JOIN `studenttrackingdb`.`schoolroute` AS v ON al.vin=v.vin LEFT JOIN `studenttrackingdb`.`studentdetails` AS sd ON al.stin=sd.stin WHERE v.compId='"
					+ compID
					+ "' AND al.eventTimeStamp BETWEEN '"
					+ currentDate
					+ " 00:00:00' AND '"
					+ currentDate
					+ " 23:59:59' AND al.contactNo NOT IN('AAG','NAM') GROUP BY sd.firstName";
			heading.put("ContactNo");
			System.out.println("Exceuting Query  " + sql1);
			Query queryForData = emStudent.createNativeQuery(sql1);
			List<Object[]> resultForData = (List<Object[]>) queryForData
					.getResultList();
			for (int i = 0; i < resultForData.size(); i++) {
				Object[] row = (Object[]) resultForData.get(i);
				JSONObject iterator = new JSONObject();
				iterator.put(heading.getString(0), row[0] == null ? "---"
						: row[0].toString());
				for (int j = 1; j < row.length; j++) {
					iterator.put(heading.getString(j),
							row[j] == null ? "Not send" : row[j].toString());
				}
				iterator.put(heading.getString(row.length - 1),
						row[row.length - 1] == null ? "---"
								: row[row.length - 1].toString());
				data.put(iterator);
			}
			if (eveningTemp.length() == 0 && morningTemp.length() == 0) {
				resultObj.put("heading", new JSONArray());
				resultObj.put("data", new JSONArray());
			} else {
				resultObj.put("heading", heading);
				resultObj.put("data", data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultObj;
	}

	public JSONObject getAlertTypes() {
		JSONObject alerttypesTitle = new JSONObject();
		try {
			Query query = emStudent
					.createQuery("SELECT at FROM  Alerttypes at");
			List<Alerttypes> alertTypesList = query.getResultList();
			if (!alertTypesList.isEmpty() && alertTypesList.size() != 0) {
				for (Alerttypes alerttypes : alertTypesList) {
					alerttypesTitle.put(alerttypes.getAlerttype(),
							alerttypes.getShortdescription());
				}
			}
		} catch (Exception e) {
			LOGGER.error("SummaryEJB : getalerttypes exception = " + e);
		}
		return alerttypesTitle;
	}
}
