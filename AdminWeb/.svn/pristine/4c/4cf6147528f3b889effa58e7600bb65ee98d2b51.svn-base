package com.eiw.server;

import java.util.List;

import javax.ejb.EJB;

import com.eiw.admin.ejb.EMSAdminPortal;
import com.eiw.client.AdminPortalService;
import com.eiw.client.dto.CompanyDataAdmin;
import com.eiw.client.dto.DashboardContentPerCB;
import com.eiw.client.dto.OperatorData;
import com.eiw.client.dto.VehicleData;
import com.eiw.client.dto.VehicleIMEIDto;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class AdminPortalServiceImpl extends RemoteServiceServlet implements
		AdminPortalService {

	@EJB
	EMSAdminPortal ems;

	public DashboardContentPerCB authenticateUserAdmin(String corpId,
			String name, String pass) {
		return ems.authenticateUserAdmin(corpId, name, pass);
	}

	public List<CompanyDataAdmin> getCompanyDetails(String suffix) {
		return ems.getCompanyDetails(suffix);
	}

	public List<VehicleData> getBWUtilReport(String suffix,
			VehicleData vehicleData) {
		return ems.getBWUtilReport(suffix, vehicleData);
	}

	public List<CompanyDataAdmin> getCompanyNames(String suffix) {
		return ems.getCompanyNames(suffix);
	}

	public String addCompanyFearures(List<CompanyDataAdmin> companyDataAdmins,
			List<CompanyDataAdmin> companyDataAdmins1, String companyId) {
		return ems.addCompanyFearures(companyDataAdmins, companyDataAdmins1,
				companyId);
	}

	public List<CompanyDataAdmin> getCompanyFeatures(
			CompanyDataAdmin companyDataAdmin) {
		return ems.getCompanyFeatures(companyDataAdmin);
	}

	public List<CompanyDataAdmin> getErrorLogInfo(
			CompanyDataAdmin companyDataAdmin) {
		return ems.getErrorLogInfo(companyDataAdmin);
	}

	public List<CompanyDataAdmin> getLoginInfo(String suffix,
			CompanyDataAdmin companyDataAdmin) {
		return ems.getLoginInfo(suffix, companyDataAdmin);
	}

	public String sendEmail(String from, String to, String subject, String text) {
		return ems.sendEmail(from, to, subject, text);
	}

	public String updateSMSCount(VehicleData vehicleData) {
		return ems.updateSMSCount(vehicleData);
	}

	public List<VehicleData> getSmsHistory(VehicleData vehicleData) {
		return ems.getSmsHistory(vehicleData);
	}

	public List<VehicleData> getSMSCount(String suffix) {
		return ems.getSMSCount(suffix);
	}

	public String vehicleSummaryBulkExec(String fromDate, String toDate,
			String companyName) {
		return ems.vehicleSummaryBulkExec(fromDate, toDate, companyName);
	}

	public List<VehicleData> getLiveVehicles(String suffix) {
		return ems.getLiveVehicles(suffix);
	}

	public List<VehicleData> getVehicleEventInfo(VehicleData vehicleData) {
		return ems.getVehicleEventInfo(vehicleData);
	}

	public List<String> validatecompanyId() {
		return ems.validatecompanyId();
	}

	public List<CompanyDataAdmin> getCountryList(String companyId) {
		return ems.getCountryList(companyId);
	}

	public String addCompanyRegistration(CompanyDataAdmin companyData) {
		return ems.addCompanyRegistration(companyData);
	}

	public List<VehicleData> btnEnableDisable() {
		return ems.btnEnableDisable();
	}

	public List<VehicleData> startBatchListener(String userId) {
		return ems.startBatchListener(userId);
	}

	public List<VehicleData> stopBatchListener(String userId) {
		return ems.stopBatchListener(userId);
	}

	public String startSimulator(String imeiNo) {
		return ems.startSimulator(imeiNo);
	}

	public String stopSimulator(String imeiNo) {
		return ems.stopSimulator(imeiNo);
	}

	public String updateLoginInfo(CompanyDataAdmin companyDataAdmin) {
		return ems.updateLoginInfo(companyDataAdmin);
	}

	public List<CompanyDataAdmin> getCompanyRegistration(String suffix) {
		return ems.getCompanyRegistration(suffix);
	}

	public List<OperatorData> getOperatorTelNo(String compName,
			String branchName, String userName) {
		return ems.getOperatorTelNo(compName, branchName, userName);
	}

	public String sendSMS(String mobileNo, String msg) {
		return ems.sendSMS(mobileNo, msg);
	}

	public List<VehicleIMEIDto> getVehilceIMEI(String suffix) {
		return ems.getVehilceIMEI(suffix);
	}

	@Override
	public String stopApplication() {
		return ems.stopApplication();
	}

	public String editCompanyRegistration(CompanyDataAdmin companyData) {
		return ems.editCompanyRegistration(companyData);
	}

	public String deleteCompanyRegistration(CompanyDataAdmin companyData) {
		return ems.deleteCompanyRegistration(companyData);
	}

	@Override
	public List<VehicleData> getCompanyIdDetails(String companyId) {
		return ems.getCompanyIdDetails(companyId);
	}
}