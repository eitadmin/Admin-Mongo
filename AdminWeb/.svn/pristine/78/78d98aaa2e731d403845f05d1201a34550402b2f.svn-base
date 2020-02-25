package com.eiw.client;

import java.util.List;

import com.eiw.client.dto.CompanyDataAdmin;
import com.eiw.client.dto.DashboardContentPerCB;
import com.eiw.client.dto.OperatorData;
import com.eiw.client.dto.VehicleData;
import com.eiw.client.dto.VehicleIMEIDto;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("adminportal")
public interface AdminPortalServiceAsync {

	public void authenticateUserAdmin(String corpId, String name, String pass,
			AsyncCallback<DashboardContentPerCB> asyncCallback);

	public void getCompanyDetails(String suffix,
			AsyncCallback<List<CompanyDataAdmin>> asyncCallback);

	public void getBWUtilReport(String suffix, VehicleData vehicleData,
			AsyncCallback<List<VehicleData>> asyncCallback);

	public void getCompanyNames(String suffix,
			AsyncCallback<List<CompanyDataAdmin>> asyncCallback);

	public void addCompanyFearures(List<CompanyDataAdmin> companyDataAdmins,
			List<CompanyDataAdmin> companyDataAdmins1, String companyId,
			AsyncCallback<String> asyncCallback);

	public void getCompanyFeatures(CompanyDataAdmin companyDataAdmin,
			AsyncCallback<List<CompanyDataAdmin>> asyncCallback);

	public void getErrorLogInfo(CompanyDataAdmin companyDataAdmin,
			AsyncCallback<List<CompanyDataAdmin>> asyncCallback);

	public void getLoginInfo(String suffix, CompanyDataAdmin companyDataAdmin,
			AsyncCallback<List<CompanyDataAdmin>> asyncCallback);

	public void sendEmail(String from, String to, String subject, String text,
			AsyncCallback<String> asyncCallback);

	public void updateSMSCount(VehicleData vehicleData,
			AsyncCallback<String> asyncCallback);

	public void getSmsHistory(VehicleData vehicleData,
			AsyncCallback<List<VehicleData>> asyncCallback);

	public void getSMSCount(String suffix,
			AsyncCallback<List<VehicleData>> asyncCallback);

	public void vehicleSummaryBulkExec(String fromDate, String toDate,
			String companyName, AsyncCallback<String> asyncCallback);

	public void getLiveVehicles(String suffix,
			AsyncCallback<List<VehicleData>> asyncCallback);

	public void getVehicleEventInfo(VehicleData vehicleData,
			AsyncCallback<List<VehicleData>> asyncCallback);

	public void validatecompanyId(AsyncCallback<List<String>> asyncCallback);

	public void getCountryList(String companyId,
			AsyncCallback<List<CompanyDataAdmin>> asyncCallback);

	public void addCompanyRegistration(CompanyDataAdmin companyData,
			AsyncCallback<String> asyncCallback);

	public void btnEnableDisable(AsyncCallback<List<VehicleData>> asyncCallback);

	public void startBatchListener(String userId,
			AsyncCallback<List<VehicleData>> asyncCallback);

	public void stopBatchListener(String userId,
			AsyncCallback<List<VehicleData>> asyncCallback);

	public void startSimulator(String imeiNo,
			AsyncCallback<String> ayAsyncCallback);

	public void stopSimulator(String imeiNo,
			AsyncCallback<String> ayAsyncCallback);

	public void updateLoginInfo(CompanyDataAdmin companyDataAdmin,
			AsyncCallback<String> asyncCallback);

	public void getCompanyRegistration(String suffix,
			AsyncCallback<List<CompanyDataAdmin>> asyncCallback);

	public void getOperatorTelNo(String compName, String branchName,
			String userName, AsyncCallback<List<OperatorData>> asyncCallback);

	public void sendSMS(String mobileNo, String msg,
			AsyncCallback<String> asyncCallback);

	public void getVehilceIMEI(String suffix,
			AsyncCallback<List<VehicleIMEIDto>> asyncCallback);

	public void stopApplication(AsyncCallback<String> asyncCallback);

	public void editCompanyRegistration(CompanyDataAdmin companyData,
			AsyncCallback<String> asyncCallback);

	public void deleteCompanyRegistration(CompanyDataAdmin companyData,
			AsyncCallback<String> asyncCallback);
	
	public void getCompanyIdDetails(String companyId, AsyncCallback<List<VehicleData>> asyncCallback);
	

}
