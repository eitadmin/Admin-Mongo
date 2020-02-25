package com.eiw.cron;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.eiw.alerts.AlertsManager;
import com.eiw.alerts.CheckFreeFormGeo;
import com.eiw.cron.report.SummaryEJBRemote;
import com.eiw.server.bo.BOFactory;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.Vehicleevent;
import com.eiw.server.fleettrackingpu.Vehicletype;

public class NeighbourhoodAlerts implements Job {
	private static final Logger LOGGER = Logger.getLogger("report");
	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	AlertsManager alertsManager = new AlertsManager();
	CheckFreeFormGeo checkFreeFormGeo = new CheckFreeFormGeo(alertsManager);
	private static final String MODE = "Neighbourhood", VEHICLE = "Vehicle";
	private static final String STR_REGION = "Asia/Riyadh";

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		LOGGER.info("NeighbourhoodAlerts::Entering NeighbourhoodAlerts Class");
		SummaryEJBRemote summaryEJB = BOFactory.getSummaryEJBRemote();
		List<Object[]> vehicleList = summaryEJB
				.vehicleList(STR_REGION, MODE);
		for (int i = 0; i < vehicleList.size(); i++) {
			Object[] obj = (Object[]) vehicleList.get(i);
			Vehicle vehicle = new Vehicle();
			Vehicletype vehicleType = new Vehicletype();
			String vin = (String) obj[1];
			Date eventDate = (Date) obj[0];
			String plateNo = (String) obj[2];
			vehicle.setVin(vin);
			vehicle.setCompanyId((String) obj[3]);
			vehicle.setBranchId((String) obj[4]);
			vehicleType.setVehicleType(VEHICLE);
			vehicle.setVehicletype(vehicleType);
			List<Vehicleevent> vehicleEventList = summaryEJB.getVehicleEvents(
					vin, sdfDate.format(eventDate));
			checkFreeFormGeo.manageAlert(vehicleEventList, vehicle, plateNo,
					STR_REGION, MODE);

		}
		LOGGER.info("NeighbourhoodAlerts::Exiting NeighbourhoodAlerts Class");
	}

}
