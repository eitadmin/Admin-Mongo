package com.eiw.cron;

import java.util.List;

import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.eiw.alerts.AlertsManager;
import com.eiw.server.bo.BOFactory;
import com.eiw.server.fleettrackingpu.Alertconfig;
import com.eiw.server.fleettrackingpu.AlertconfigId;
import com.eiw.server.fleettrackingpu.Vehiclealerts;

public class CronJobMaintenance implements Job {

	private static final Logger LOGGER = Logger.getLogger("cron");

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			DispatcherEJBRemote dispatcherEJB = BOFactory
					.getDispatcherEJBRemote();
			dispatcherEJB.subOdometersInServiceData();
			List<Object[]> renewalDatas = dispatcherEJB
					.getMaitenanceRenewalData();
			List<Object[]> serviceDatas = dispatcherEJB
					.getMaitenanceServiceData();
			if (!renewalDatas.isEmpty() && !(renewalDatas.size() == 0))
				configAlert(renewalDatas, "Renewal");
			if (!serviceDatas.isEmpty() && !(serviceDatas.size() == 0))
				configAlert(serviceDatas, "Service");
		} catch (Exception e) {
			LOGGER.error("Cron Job Maintenance :Error at");
			e.printStackTrace();
		}
	}

	public void configAlert(List<Object[]> datas, String maintenance) {
		try {
			AlertsManager alertsManager = new AlertsManager();
			for (int i = 0; i < datas.size(); i++) {
				DispatcherEJBRemote dispatcherEJB = BOFactory
						.getDispatcherEJBRemote();
				String plateNo, maintenanceType, subject, content, fromDate, toDate, description, emailId, notificationType, companyId, vin, eventId, througMail, emailAddress, msg = "";
				String notificationTypes[];
				Object[] data = datas.get(i);
				plateNo = String.valueOf(data[0]);
				maintenanceType = String.valueOf(data[1]);
				subject = String.valueOf(data[2]);
				content = String.valueOf(data[3]);
				fromDate = String.valueOf(data[4]);
				toDate = String.valueOf(data[5]);
				description = String.valueOf(data[6]);
				emailId = String.valueOf(data[7]);
				notificationType = String.valueOf(data[8]);
				companyId = String.valueOf(data[9]);
				vin = String.valueOf(data[10]);
				eventId = String.valueOf(data[11]);
				notificationTypes = notificationType.split("#");
				if (emailId.contains(",")) {
					througMail = "1#1";
				} else {
					througMail = "1";
				}
				emailAddress = emailId.replaceAll(",", "#");
				if (maintenance.equalsIgnoreCase("Renewal")) {
					msg = "Company name: " + companyId + " |   Your "
							+ maintenanceType + " is going to expirer at "
							+ toDate + "   |   Plate No: " + plateNo + "   |  "
							+ maintenanceType + " No: " + subject + "  |  "
							+ maintenanceType + " Provider : " + content
							+ " | " + maintenanceType + " Register On: "
							+ fromDate;
					if (description != "") {
						msg += "  | Description : " + description;
					}
				} else if (maintenance.equalsIgnoreCase("Service")) {
					if (!content.equalsIgnoreCase("0")) {
						dispatcherEJB.setRunnedOutKms(eventId);
						msg = "Company name: "
								+ companyId
								+ " |    "
								+ maintenanceType
								+ " Alert   | Your vehicle had crossed the given kilometers.   | "
								+ " Plate No: " + plateNo;
					} else if (!subject.equalsIgnoreCase("NIL")) {
						msg = "Company name: "
								+ companyId
								+ "   |   "
								+ maintenanceType
								+ " Alert    | Your vehicle had crossed the specified date: "
								+ toDate + "  | " + " Plate No: " + plateNo;
					}
					if (description != "") {
						msg += "  | Description : " + description;
					}
				}
				dispatcherEJB.persistVehicleAlerts(vin, maintenance,
						maintenanceType, msg);
				for (int j = 0; j < notificationTypes.length; j++) {

					if (notificationTypes[j].equalsIgnoreCase("email")) {
						Vehiclealerts vehiclealert = new Vehiclealerts();
						Alertconfig alertConfig = new Alertconfig();
						AlertconfigId alertconfigId = new AlertconfigId();
						vehiclealert.setDescription(msg);
						alertconfigId.setAlertType(maintenance);
						alertconfigId.setCompanyId(companyId);
						alertConfig.setId(alertconfigId);
						alertConfig.setEmailAddress(emailAddress);
						alertConfig.setThroughEmail(througMail);
						alertsManager.callEmailService(vehiclealert,
								alertConfig, true, true);
					}

					else if (notificationTypes[j].equalsIgnoreCase("push")) {
						List<String> userids = dispatcherEJB.getUserId(vin);
						if (!userids.isEmpty() && userids.size() != 0) {
							for (String user : userids) {
								alertsManager.pushNotification(companyId, user,
										msg);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}