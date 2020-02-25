package com.eiw.cron;

import java.util.List;

import javax.ejb.Local;

import com.eiw.server.fleettrackingpu.Alertconfig;

@Local
public interface DispatcherEJBRemote {

	void dispatcher(String vin, String timeZone);

	List<String> vehicleList(String timeZone);

	void getAlertConfigData();

	List<Alertconfig> fmList(String neighbourhoodreport);

	List<String> userHasActiveVins(String companyId, String userId,
			String branchId);

	List<Object[]> getStudentData(String branchId);

	List<String> getUserMail();

	List<Object[]> getMaitenanceRenewalData();

	List<Object[]> getMaitenanceServiceData();

	List<String> getUserId(String vin);

	void subOdometersInServiceData();

	void setRunnedOutKms(String eventId);

	void persistVehicleAlerts(String vin, String maintenance,
			String maintenanceType, String description);
	
	boolean isEmailValidOrNot(String mail);
}
