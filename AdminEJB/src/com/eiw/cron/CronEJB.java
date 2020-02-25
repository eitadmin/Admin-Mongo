package com.eiw.cron;

import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.eiw.alerts.OdometerMaintenance;
import com.eiw.cron.report.AssetStatusSummaryReport;
import com.eiw.cron.report.ConsolidateSummaryByDayReport;
import com.eiw.cron.report.DataMigration;
import com.eiw.cron.report.NewSummaryDayReport;
import com.eiw.cron.report.NoTransmissionAlert;
import com.eiw.cron.report.SummaryDayReport;
import com.eiw.cron.report.SummaryDayReportToMail;
import com.eiw.cron.report.SummaryReportByMonthlyMail;
import com.eiw.cron.report.SummaryReportByWeeklyMail;
import com.eiw.cron.report.DailySmsReport;
import com.eiw.cron.report.VehicleEventBackup;

@Singleton
@Startup
public class CronEJB implements CronEJBRemote {
	private static final String STR_GROUP = "group1";

	@PostConstruct
	public void startCron() {

		try {

			// Student Status Update in 0
			// JobDetail jdSTUD = new JobDetail("jdSTUD", "gropuStud",
			// CronJobStudStatusUpdate.class);
			// CronExpression cronExpressionSTUD = new CronExpression(
			// "0 0 1 * * ?");
			// cronExpressionSTUD
			// .setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
			// CronTrigger ctSTUD = new CronTrigger("cronTriggerjdSTUD",
			// "gropuStud");
			// ctSTUD.setCronExpression(cronExpressionSTUD);
			// SchedulerFactory sfSTUD = new StdSchedulerFactory();
			// Scheduler schedSTUD = sfSTUD.getScheduler();
			// schedSTUD.scheduleJob(jdSTUD, ctSTUD);
			// schedSTUD.start();

			// Vehicle Summary
			// For Branches in IND
			// JobDetail jdIND = new JobDetail("jdIND", STR_GROUP,
			// CronJobIND.class);
			// CronExpression cronExpressionIND = new
			// CronExpression("0 0 1 * * ?");
			// cronExpressionIND.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
			// CronTrigger ctIND = new CronTrigger("cronTriggerjdIND",
			// STR_GROUP);
			// ctIND.setCronExpression(cronExpressionIND);
			// SchedulerFactory sfIND = new StdSchedulerFactory();
			// Scheduler schedIND = sfIND.getScheduler();
			// schedIND.scheduleJob(jdIND, ctIND);
			// schedIND.start();

			// For Branches in KSA
			// JobDetail jdKSA = new JobDetail("jobjdKSA", STR_GROUP,
			// CronJobKSA.class);
			// CronExpression cronExpressionKSA = new
			// CronExpression("0 0 1 * * ?");
			// cronExpressionKSA.setTimeZone(TimeZone.getTimeZone("Asia/Riyadh"));
			// CronTrigger ctKSA = new CronTrigger("cronTriggerjdKSA",
			// STR_GROUP);
			// ctKSA.setCronExpression(cronExpressionKSA);
			// SchedulerFactory sfKSA = new StdSchedulerFactory();
			// Scheduler schedKSA = sfKSA.getScheduler();
			// schedKSA.scheduleJob(jdKSA, ctKSA);
			// schedKSA.start();

			// For Branches in DUB
			// JobDetail jdDUB = new JobDetail("jobjdDUB", STR_GROUP,
			// CronJobDUB.class);
			// CronExpression cronExpressionDUB = new
			// CronExpression("0 0 1 * * ?");
			// cronExpressionDUB.setTimeZone(TimeZone.getTimeZone("Asia/Dubai"));
			// CronTrigger ctDUB = new CronTrigger("cronTriggerjdDUB",
			// STR_GROUP);
			// ctDUB.setCronExpression(cronExpressionDUB);
			// SchedulerFactory sfDUB = new StdSchedulerFactory();
			// Scheduler schedDUB = sfDUB.getScheduler();
			// schedDUB.scheduleJob(jdDUB, ctDUB);
			// schedDUB.start();

			// For Archival
			// JobDetail jdARCH = new JobDetail("jobjdARCH", STR_GROUP,
			// CronArchival.class);
			// CronExpression cronExpressionARCH = new CronExpression(
			// "0 45 2 * * ?");
			// cronExpressionARCH.setTimeZone(TimeZone.getTimeZone("Asia/Riyadh"));
			// CronTrigger ctARCH = new CronTrigger("cronTriggerjdARCH",
			// STR_GROUP);
			// ctARCH.setCronExpression(cronExpressionARCH);
			// SchedulerFactory sfARCH = new StdSchedulerFactory();
			// Scheduler schedARCH = sfARCH.getScheduler();
			// schedARCH.scheduleJob(jdARCH, ctARCH);
			// schedARCH.start();

			// For Vehicle Summary Archival
			// JobDetail jdVehSummArch = new JobDetail("jobjdVehSummArch",
			// "group2", CronArchivalVehicleSummary.class);
			// CronExpression cronExpressiondVehSummArch = new
			// CronExpression("0 40 15 * * ?");
			// cronExpressiondVehSummArch.setTimeZone(TimeZone.getTimeZone("Asia/Riyadh"));
			// CronTrigger ctVehSummArch = new
			// CronTrigger("cronTriggerjdVehSummArch", "group2");
			// ctVehSummArch.setCronExpression(cronExpressiondVehSummArch);
			// SchedulerFactory sfVehSummArch = new StdSchedulerFactory();
			// Scheduler schedVehSummArch = sfVehSummArch.getScheduler();
			// schedVehSummArch.scheduleJob(jdVehSummArch, ctVehSummArch);
			// schedVehSummArch.start();

			// For Deleting Restored Archive Data
			// JobDetail jdResArch = new JobDetail("jdResArch", "STR_GROUP",
			// CronRestoreArchival.class);
			// CronExpression cronExpressionResArch = new CronExpression(
			// "0 11 13 * * ?");
			// cronExpressionResArch.setTimeZone(TimeZone
			// .getTimeZone("Asia/Riyadh"));
			// CronTrigger ctResArch = new CronTrigger("cronTriggerjdResArch",
			// "STR_GROUP");
			// ctResArch.setCronExpression(cronExpressionResArch);
			// SchedulerFactory sfResArch = new StdSchedulerFactory();
			// Scheduler schedResArch = sfResArch.getScheduler();
			// schedResArch.scheduleJob(jdResArch, ctResArch);
			// schedResArch.start();

			// For Skywave
			// JobDetail jdSky = new JobDetail("jobjdSky", STR_GROUP,
			// SkywavePollerThread.class);
			// CronExpression cronExpressionSky = new CronExpression(
			// "0 */10 * * * ?");
			// CronTrigger ctSky = new CronTrigger("cronTriggerjdSky",
			// STR_GROUP);
			// ctSky.setCronExpression(cronExpressionSky);
			// SchedulerFactory sfSky = new StdSchedulerFactory();
			// Scheduler schedSky = sfSky.getScheduler();
			// schedSky.scheduleJob(jdSky, ctSky);
			// schedSky.start();

			// For Daily Summary Report and to update Vehicle has Odometer
			JobDetail jdSummary = new JobDetail("jobjdSummary", STR_GROUP,
					SummaryDayReport.class);
			CronExpression cronExpressionSummary = new CronExpression(
					"0 0 6 * * ?");
			cronExpressionSummary.setTimeZone(TimeZone
					.getTimeZone("Asia/Kolkata"));
			CronTrigger ctSummary = new CronTrigger("cronTriggerjdSummary",
					STR_GROUP);
			ctSummary.setCronExpression(cronExpressionSummary);
			SchedulerFactory sfSummary = new StdSchedulerFactory();
			Scheduler schedSummary = sfSummary.getScheduler();
			schedSummary.scheduleJob(jdSummary, ctSummary);
			schedSummary.start();

			// Odometer Maintenance Alerts
			JobDetail jdMaint = new JobDetail("jdMaint", STR_GROUP,
					OdometerMaintenance.class);
			CronExpression cronExpressionMaint = new CronExpression(
					"0 0 10 * * ?");
			cronExpressionMaint
					.setTimeZone(TimeZone.getTimeZone("Asia/Riyadh"));
			CronTrigger ctMaint = new CronTrigger("cronTriggerjdMaint",
					STR_GROUP);
			ctMaint.setCronExpression(cronExpressionMaint);
			SchedulerFactory sfMaint = new StdSchedulerFactory();
			Scheduler schedMaint = sfMaint.getScheduler();
			schedMaint.scheduleJob(jdMaint, ctMaint);
			schedMaint.start();

			// Alert Configuration Disable if validity expires..
			JobDetail jdalertConfig = new JobDetail("jdalertConfig", STR_GROUP,
					AlertConfValidityChk.class);
			CronExpression cronExpressionAlert = new CronExpression(
					"0 10 3 * * ?");
			cronExpressionAlert.setTimeZone(TimeZone
					.getTimeZone("Asia/Kolkata"));
			CronTrigger ctAlert = new CronTrigger("cronTriggerjdAlert",
					STR_GROUP);
			ctAlert.setCronExpression(cronExpressionAlert);
			SchedulerFactory sfAlert = new StdSchedulerFactory();
			Scheduler schedAlert = sfAlert.getScheduler();
			schedAlert.scheduleJob(jdalertConfig, ctAlert);
			schedAlert.start();

			// For Checking for students presence
			// JobDetail jobstudent = new JobDetail("jdstudent", "gropu5",
			// CronJobforStudent.class);
			// CronExpression cronExpressionstudent = new CronExpression(
			// "0 0 1 * * ?");
			// cronExpressionIND.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
			// CronTrigger ctStudent = new CronTrigger("cronTriggerjdstudent",
			// "gropu5");
			// ctStudent.setCronExpression(cronExpressionstudent);
			// SchedulerFactory student = new StdSchedulerFactory();
			// Scheduler studentpresence = student.getScheduler();
			// studentpresence.scheduleJob(jobstudent, ctStudent);
			// studentpresence.start();

			// For Maintenance Alerts

			// JobDetail jdMaintAlert = new JobDetail("jobjdMaintAlert",
			// STR_GROUP, CronMaintAlert.class);
			// CronExpression cronExpressionMaintAlert = new CronExpression(
			// "0 0 3 * * ?");
			// cronExpressionMaintAlert.setTimeZone(TimeZone
			// .getTimeZone("Asia/Kolkata"));
			// CronTrigger ctMaintAlert = new CronTrigger(
			// "cronTriggerjdMaintAlert", STR_GROUP);
			// ctMaintAlert.setCronExpression(cronExpressionMaintAlert);
			// SchedulerFactory sfMaintAlert = new StdSchedulerFactory();
			// Scheduler schedMaintAlert = sfMaintAlert.getScheduler();
			// schedMaintAlert.scheduleJob(jdMaintAlert, ctMaintAlert);
			// schedMaintAlert.start();

			// For Ramco Db insert
			// JobDetail jdMaintAlert1 = new JobDetail("jobjdMaintAlert1",
			// STR_GROUP, DataPushCron.class);
			// CronExpression cronExpressionMaintAlert1 = new CronExpression(
			// "0 30 2 * * ?");
			// cronExpressionMaintAlert1.setTimeZone(TimeZone
			// .getTimeZone("Asia/Kolkata"));
			// CronTrigger ctMaintAlert1 = new CronTrigger(
			// "cronTriggerjdMaintAlert1", STR_GROUP);
			// ctMaintAlert1.setCronExpression(cronExpressionMaintAlert1);
			// SchedulerFactory sfMaintAlert1 = new StdSchedulerFactory();
			// Scheduler schedMaintAlert1 = sfMaintAlert1.getScheduler();
			// schedMaintAlert1.scheduleJob(jdMaintAlert1, ctMaintAlert1);
			// schedMaintAlert1.start();

			// For AutomatedReport and Send Email
			// JobDetail automatedReport = new JobDetail("jobautomatedreport",
			// STR_GROUP, AutomatedReport.class);
			// CronExpression cronExpautomatedreport = new CronExpression(
			// "0 15 5 * * ?");
			// cronExpautomatedreport.setTimeZone(TimeZone
			// .getTimeZone("Asia/Kolkata"));
			// CronTrigger ctautomatedreport = new CronTrigger(
			// "cronTriggerautomatedreport", STR_GROUP);
			// ctautomatedreport.setCronExpression(cronExpautomatedreport);
			// SchedulerFactory sfautomatedreport = new StdSchedulerFactory();
			// Scheduler schedautomatedreport =
			// sfautomatedreport.getScheduler();
			// schedautomatedreport
			// .scheduleJob(automatedReport, ctautomatedreport);
			// schedautomatedreport.start();

			// For Daily Employee Summary Report
			JobDetail EmployeeSummary = new JobDetail("jobEmployeeSummary",
					STR_GROUP, EmployeeSummaryDayReport.class);
			CronExpression cronEmployeeSummary = new CronExpression(
					"0 45 2 * * ?");
			cronEmployeeSummary.setTimeZone(TimeZone
					.getTimeZone("Asia/Kolkata"));
			CronTrigger ctEmployeeSummary = new CronTrigger(
					"cronTriggerEmployeeSummary", STR_GROUP);
			ctEmployeeSummary.setCronExpression(cronEmployeeSummary);
			SchedulerFactory sfEmployeeSummary = new StdSchedulerFactory();
			Scheduler schedEmployeeSummary = sfEmployeeSummary.getScheduler();
			schedEmployeeSummary
					.scheduleJob(EmployeeSummary, ctEmployeeSummary);
			schedEmployeeSummary.start();

			// For Neighbourhood violation
			// JobDetail jdNeighbourhood = new JobDetail("jobjdNeighbourhood",
			// STR_GROUP, NeighbourhoodAlerts.class);
			// CronExpression cronExpressionNeighbourhood = new CronExpression(
			// "0 15 4 * * ?");
			// cronExpressionNeighbourhood.setTimeZone(TimeZone
			// .getTimeZone("Asia/Kolkata"));
			// CronTrigger ctNeighbourhood = new CronTrigger(
			// "cronTriggerjdNeighbourhood", STR_GROUP);
			// ctNeighbourhood.setCronExpression(cronExpressionNeighbourhood);
			// SchedulerFactory sfNeighbourhood = new StdSchedulerFactory();
			// Scheduler schedNeighbourhood = sfNeighbourhood.getScheduler();
			// schedNeighbourhood.scheduleJob(jdNeighbourhood, ctNeighbourhood);
			// schedNeighbourhood.start();

			// For New Daily Summary Report
			JobDetail NewSummary = new JobDetail("jobNewSummary", STR_GROUP,
					NewSummaryDayReport.class);
			CronExpression cronExpressionNewSummary = new CronExpression(
					"0 05 0 * * ?");
			cronExpressionNewSummary.setTimeZone(TimeZone
					.getTimeZone("Asia/Riyadh"));
			CronTrigger ctNewSummary = new CronTrigger("cronTriggerNewSummary",
					STR_GROUP);
			ctNewSummary.setCronExpression(cronExpressionNewSummary);
			SchedulerFactory sfNewSummary = new StdSchedulerFactory();
			Scheduler schedNewSummary = sfNewSummary.getScheduler();
			schedNewSummary.scheduleJob(NewSummary, ctNewSummary);
			schedNewSummary.start();

			JobDetail assetStatusReport = new JobDetail(
					"jobAssetStatusSummary", STR_GROUP,
					AssetStatusSummaryReport.class);
			CronExpression cronExpressionassetStatusReport = new CronExpression(
					"0 30 7 * * ?");
			cronExpressionassetStatusReport.setTimeZone(TimeZone
					.getTimeZone("Asia/Kolkata"));
			CronTrigger ctAssetStatusReport = new CronTrigger(
					"cronTriggerAssetStatusReport", STR_GROUP);
			ctAssetStatusReport
					.setCronExpression(cronExpressionassetStatusReport);
			SchedulerFactory sfAssetStatusSummaryReport = new StdSchedulerFactory();
			Scheduler schedctAssetStatusReport = sfAssetStatusSummaryReport
					.getScheduler();
			schedctAssetStatusReport.scheduleJob(assetStatusReport,
					ctAssetStatusReport);
			schedctAssetStatusReport.start();

			JobDetail jdMaintenance = new JobDetail("jdMaintenance", STR_GROUP,
					CronJobMaintenance.class);
			CronExpression cronExpressionMaintenance = new CronExpression(
					"0 01 09 * * ?");
			cronExpressionMaintenance.setTimeZone(TimeZone
					.getTimeZone("Asia/Kolkata"));
			CronTrigger ctMaintenance = new CronTrigger(
					"cronTriggerjdMaintenance", STR_GROUP);
			ctMaintenance.setCronExpression(cronExpressionMaintenance);
			SchedulerFactory sfMaintenance = new StdSchedulerFactory();
			Scheduler schedMaintenance = sfMaintenance.getScheduler();
			schedMaintenance.scheduleJob(jdMaintenance, ctMaintenance);
			schedMaintenance.start();

			// For NoTransOverrideFromTable Cron Remove Data From
			// NoTransOverrideTable
			// Report
			/*
			 * JobDetail NoTransOverrideFromTable = new JobDetail(
			 * "jobNewSummaryByDay", STR_GROUP, NoTransOverrideFromTable.class);
			 * CronExpression cronExpressionNoTransOverrideFromTable = new
			 * CronExpression( "0 30 23 * * ?");
			 * cronExpressionNoTransOverrideFromTable.setTimeZone(TimeZone
			 * .getTimeZone("Asia/Kolkata")); CronTrigger
			 * ctNoTransOverrideFromTable = new CronTrigger(
			 * "cronTriggerNewSummaryByDay", STR_GROUP);
			 * ctNoTransOverrideFromTable
			 * .setCronExpression(cronExpressionNoTransOverrideFromTable);
			 * SchedulerFactory sfNoTransOverrideFromTable = new
			 * StdSchedulerFactory(); Scheduler schedNoTransOverrideFromTable =
			 * sfNoTransOverrideFromTable .getScheduler();
			 * schedNoTransOverrideFromTable
			 * .scheduleJob(NoTransOverrideFromTable,
			 * ctNoTransOverrideFromTable);
			 * schedNoTransOverrideFromTable.start();
			 */

			// Summary Report Send Mail every Day
			JobDetail NewSummaryForMail = new JobDetail("jobNewSummaryForMail",
					STR_GROUP, SummaryDayReportToMail.class);
			CronExpression cronExpressionNewSummaryForMail = new CronExpression(
					"0 30 4 * * ?");
			cronExpressionNewSummaryForMail.setTimeZone(TimeZone
					.getTimeZone("Asia/Kolkata"));
			CronTrigger ctNewSummaryForMail = new CronTrigger(
					"cronTriggerNewSummaryForMail", STR_GROUP);
			ctNewSummaryForMail
					.setCronExpression(cronExpressionNewSummaryForMail);
			SchedulerFactory sfNewSummaryForMail = new StdSchedulerFactory();
			Scheduler schedNewSummaryForMail = sfNewSummaryForMail
					.getScheduler();
			schedNewSummaryForMail.scheduleJob(NewSummaryForMail,
					ctNewSummaryForMail);
			schedNewSummaryForMail.start();

			// For New Daily ConsolidateSummaryByDayReport or New Summary By Day
			// Report
			JobDetail newSummaryByDay = new JobDetail("jobNewSummaryByDay",
					STR_GROUP, ConsolidateSummaryByDayReport.class);
			CronExpression cronExpressionNewSummaryByDay = new CronExpression(
					"0 30 3 * * ?");
			cronExpressionNewSummaryByDay.setTimeZone(TimeZone
					.getTimeZone("Asia/Kolkata"));
			CronTrigger ctNewSummaryByDay = new CronTrigger(
					"cronTriggerNewSummaryByDay", STR_GROUP);
			ctNewSummaryByDay.setCronExpression(cronExpressionNewSummaryByDay);
			SchedulerFactory sfNewSummaryByDay = new StdSchedulerFactory();
			Scheduler schedNewSummaryByDay = sfNewSummaryByDay.getScheduler();
			schedNewSummaryByDay
					.scheduleJob(newSummaryByDay, ctNewSummaryByDay);
			schedNewSummaryByDay.start();

			// SummaryBy Day Report Send Mail weekly Once
			JobDetail weeklySummaryReporty = new JobDetail(
					"jobWeeklySummaryReporty", STR_GROUP,
					SummaryReportByWeeklyMail.class);
			CronExpression cronExpressionWeeklySummaryReporty = new CronExpression(
					"0 15 5 ? * MON");
			cronExpressionWeeklySummaryReporty.setTimeZone(TimeZone
					.getTimeZone("Asia/Kolkata"));
			CronTrigger ctWeeklySummaryReporty = new CronTrigger(
					"cronTriggerWeeklySummaryReporty", STR_GROUP);
			ctWeeklySummaryReporty
					.setCronExpression(cronExpressionWeeklySummaryReporty);
			SchedulerFactory sfWeeklySummaryReporty = new StdSchedulerFactory();
			Scheduler schedWeeklySummaryReporty = sfWeeklySummaryReporty
					.getScheduler();
			schedWeeklySummaryReporty.scheduleJob(weeklySummaryReporty,
					ctWeeklySummaryReporty);
			schedWeeklySummaryReporty.start();

			// Temporal remove for GE
			// No Transmission Alert 0 0 10/1 ? * *");
			// JobDetail NoTransmissionAlert = new JobDetail(
			// "jobNoTransmissionAlert", STR_GROUP,
			// NoTransmissionAlert.class);
			// CronExpression cronExpressionNoTransmissionAlert = new
			// CronExpression(
			// "0 0 10/1 ? * *");
			// cronExpressionNoTransmissionAlert.setTimeZone(TimeZone
			// .getTimeZone("Asia/Kolkata"));
			// CronTrigger ctNoTransmissionAlert = new CronTrigger(
			// "cronTriggerNoTransmissionAlert", STR_GROUP);
			// ctNoTransmissionAlert
			// .setCronExpression(cronExpressionNoTransmissionAlert);
			// SchedulerFactory sfNoTransmissionAlert = new
			// StdSchedulerFactory();
			// Scheduler schedNoTransmissionAlert = sfNoTransmissionAlert
			// .getScheduler();
			// schedNoTransmissionAlert.scheduleJob(NoTransmissionAlert,
			// ctNoTransmissionAlert);
			// schedNoTransmissionAlert.start();

			// SummaryBy Day Report Send Mail monthly Once
			JobDetail monthlySummaryReporty = new JobDetail(
					"jobmonthlySummaryReporty", STR_GROUP,
					SummaryReportByMonthlyMail.class);
			CronExpression cronExpressionmonthlySummaryReporty = new CronExpression(
					"0 45 5 1 * ?");
			cronExpressionmonthlySummaryReporty.setTimeZone(TimeZone
					.getTimeZone("Asia/Kolkata"));
			CronTrigger ctmonthlySummaryReporty = new CronTrigger(
					"cronTriggermonthlySummaryReporty", STR_GROUP);
			ctmonthlySummaryReporty
					.setCronExpression(cronExpressionmonthlySummaryReporty);
			SchedulerFactory sfmonthlySummaryReporty = new StdSchedulerFactory();
			Scheduler schedmonthlySummaryReporty = sfmonthlySummaryReporty
					.getScheduler();
			schedmonthlySummaryReporty.scheduleJob(monthlySummaryReporty,
					ctmonthlySummaryReporty);
			schedmonthlySummaryReporty.start();

			// Table Data migration Cron
			// JobDetail DataMigration = new JobDetail("jobDataMigration",
			// STR_GROUP, DataMigration.class);
			// CronExpression cronExpressionDataMigration = new CronExpression(
			// "0 50 8 * * ?");
			// cronExpressionDataMigration.setTimeZone(TimeZone
			// .getTimeZone("Asia/Kolkata"));
			// CronTrigger ctDataMigration = new CronTrigger(
			// "cronTriggerDataMigration", STR_GROUP);
			// ctDataMigration.setCronExpression(cronExpressionDataMigration);
			// SchedulerFactory sfDataMigration = new StdSchedulerFactory();
			// Scheduler schedDataMigration = sfDataMigration.getScheduler();
			// schedDataMigration.scheduleJob(DataMigration, ctDataMigration);
			// schedDataMigration.start();

			// Daily Sms Report for Schools
			// Report Generating Time : UTC = 13:35 , IST = 07:05 PM , Riyadh =
			// 16:35
			JobDetail newSmsReport = new JobDetail("smsReports", STR_GROUP,
					DailySmsReport.class);
			CronExpression cronExpressionSmsReport = new CronExpression(
					"0 05 19 * * ?");
			cronExpressionSmsReport.setTimeZone(TimeZone
					.getTimeZone("Asia/Kolkata"));
			CronTrigger cronTriggerSmsReport = new CronTrigger("ctSmsReport",
					STR_GROUP);
			cronTriggerSmsReport.setCronExpression(cronExpressionSmsReport);
			SchedulerFactory sfSmsReport = new StdSchedulerFactory();
			Scheduler schedularSmsReport = sfSmsReport.getScheduler();
			schedularSmsReport.scheduleJob(newSmsReport, cronTriggerSmsReport);
			schedularSmsReport.start();

			// VehicleEvent Backup
			/*
			 * JobDetail VEBackup = new
			 * JobDetail("smsReports",STR_GROUP,VehicleEventBackup.class);
			 * CronExpression cronExpressionVeBackup = new
			 * CronExpression("0 05 19 * * ?");
			 * cronExpressionVeBackup.setTimeZone
			 * (TimeZone.getTimeZone("Asia/Kolkata")); CronTrigger
			 * cronTriggerVeBackup = new CronTrigger("ctVeBackup", STR_GROUP);
			 * cronTriggerVeBackup.setCronExpression(cronExpressionVeBackup);
			 * SchedulerFactory sfVEBackup = new StdSchedulerFactory();
			 * Scheduler schedularsfVEBackup = sfVEBackup.getScheduler();
			 * schedularsfVEBackup.scheduleJob(VEBackup, cronTriggerVeBackup);
			 * schedularsfVEBackup.start();
			 */
		} catch (Exception e) {

		}
	}
}
