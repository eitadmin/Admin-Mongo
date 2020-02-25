package com.eiw.client.dto;

import java.util.List;

public class AlertOptionData implements java.io.Serializable {

	private List<String> vins;
	private List<VehicleData> vehicleDatas;
	private int days;
	private String time, alerttype, range, companyId, branchId, userId,
			createdBy, createdDt, lastUpdBy, lastUpdDt;
	private String interval;
	private int validity;
	private String throughSMS, throughEmail;
	private String mobile, email, mode, status;
	private String subRange;

	public String getmode() {
		return mode;
	}

	public void setmode(String mode) {
		this.mode = mode;
	}

	public String getlastUpdDt() {
		return lastUpdDt;
	}

	public void setlastUpdDt(String lastUpdDt) {
		this.lastUpdDt = lastUpdDt;
	}

	public String getlastUpdBy() {
		return lastUpdBy;
	}

	public void setlastUpdBy(String lastUpdBy) {
		this.lastUpdBy = lastUpdBy;
	}

	public String getcreatedDt() {
		return createdDt;
	}

	public void setcreatedDt(String createdDt) {
		this.createdDt = createdDt;
	}

	public String getcreatedBy() {
		return createdBy;
	}

	public void setcreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getuserId() {
		return userId;
	}

	public void setuserId(String userId) {
		this.userId = userId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public void setvins(List<String> vins) {
		this.vins = vins;
	}

	public List<String> getvins() {
		return vins;
	}

	public void setdays(int days) {
		this.days = days;
	}

	public int getdays() {
		return days;
	}

	public void setalerttype(String alerttype) {
		this.alerttype = alerttype;
	}

	public String getalerttype() {
		return alerttype;
	}

	public void settime(String time) {
		this.time = time;
	}

	public String gettime() {
		return time;
	}

	public void setvalidity(int validity) {
		this.validity = validity;
	}

	public int getvalidity() {
		return validity;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public String getinterval() {
		return interval;
	}

	public void setThroughSMS(String throughSMS) {
		this.throughSMS = throughSMS;
	}

	public String getThroughSMS() {
		return throughSMS;
	}

	public void setThroughEmail(String throughEmail) {
		this.throughEmail = throughEmail;
	}

	public String getThroughEmail() {
		return throughEmail;
	}

	public void setemail(String email) {
		this.email = email;
	}

	public String getemail() {
		return email;
	}

	public void setmobile(String mobile) {
		this.mobile = mobile;
	}

	public String getmobile() {
		return mobile;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSubRange() {
		return subRange;
	}

	public void setSubRange(String subRange) {
		this.subRange = subRange;
	}

	public List<VehicleData> getVehicleDatas() {
		return vehicleDatas;
	}

	public void setVehicleDatas(List<VehicleData> vehicleDatas) {
		this.vehicleDatas = vehicleDatas;
	}

}
