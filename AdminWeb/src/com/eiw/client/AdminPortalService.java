package com.eiw.client;

import java.util.List;

import com.eiw.client.dto.CompanyDataAdmin;
import com.eiw.client.dto.DashboardContentPerCB;
import com.eiw.client.dto.OperatorData;
import com.eiw.client.dto.VehicleData;
import com.eiw.client.dto.VehicleIMEIDto;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("adminportal")
public interface AdminPortalService extends RemoteService {

	DashboardContentPerCB authenticateUserAdmin(String corpId, String name,
			String pass);

	List<CompanyDataAdmin> getCompanyDetails(String suffix);

	List<VehicleData> getBWUtilReport(String suffix, VehicleData vehicleData);

	List<CompanyDataAdmin> getCompanyNames(String suffix);

	String addCompanyFearures(List<CompanyDataAdmin> companyDataAdmins,
			List<CompanyDataAdmin> companyDataAdmins1, String companyId);

	List<CompanyDataAdmin> getCompanyFeatures(CompanyDataAdmin companyDataAdmin);

	List<CompanyDataAdmin> getErrorLogInfo(CompanyDataAdmin companyDataAdmin);

	List<CompanyDataAdmin> getLoginInfo(String suffix,
			CompanyDataAdmin companyDataAdmin);

	String sendEmail(String from, String to, String subject, String text);

	String updateSMSCount(VehicleData vehicleData);

	List<VehicleData> getSmsHistory(VehicleData vehicleData);

	List<VehicleData> getSMSCount(String suffix);

	String vehicleSummaryBulkExec(String fromDate, String toDate,
			String companyName);

	List<VehicleData> getLiveVehicles(String suffix);

	List<VehicleData> getVehicleEventInfo(VehicleData vehicleData);

	List<String> validatecompanyId();

	List<CompanyDataAdmin> getCountryList(String companyId);

	String addCompanyRegistration(CompanyDataAdmin companyData);

	List<VehicleData> btnEnableDisable();

	List<VehicleData> startBatchListener(String userId);

	List<VehicleData> stopBatchListener(String userId);

	String startSimulator(String imeiNo);

	String stopSimulator(String imeiNo);

	String updateLoginInfo(CompanyDataAdmin companyDataAdmin);

	List<CompanyDataAdmin> getCompanyRegistration(String suffix);

	List<OperatorData> getOperatorTelNo(String compName, String branchName,
			String userName);

	String sendSMS(String mobileNo, String msg);

	List<VehicleIMEIDto> getVehilceIMEI(String suffix);

	String stopApplication();

	String editCompanyRegistration(CompanyDataAdmin companyData);

	String deleteCompanyRegistration(CompanyDataAdmin companyData);
	
	List<VehicleData> getCompanyIdDetails(String companyId);
}
