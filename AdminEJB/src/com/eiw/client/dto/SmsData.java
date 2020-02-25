package com.eiw.client.dto;

import java.util.ArrayList;
import java.util.List;

public class SmsData implements java.io.Serializable {

	private String companyId;
	private String branchId;
	private String userId;
	private String mobileNo;
	private String message;
	private String module;
	private List<String> listSMS = new ArrayList<String>();

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public List<String> getListSMS() {
		return listSMS;
	}

	public void setListSMS(List<String> listSMS) {
		this.listSMS = listSMS;
	}

}