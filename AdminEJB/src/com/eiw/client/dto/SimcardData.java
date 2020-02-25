package com.eiw.client.dto;

public class SimcardData implements java.io.Serializable {
	private String simCardNo;
	private String companyId;
	private String dop;
	private String expiryDate;
	private int pincodes;
	private String provider;
	private String plan;
	private String imeiNo;
	private String serialNo;
	private String pukCode;
	private String supportNo;
	private String userName;

	public String getSimCardNo() {
		return simCardNo;
	}

	public void setSimCardNo(String simCardNo) {
		this.simCardNo = simCardNo;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getDop() {
		return dop;
	}

	public void setDop(String dop) {
		this.dop = dop;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Integer getPincodes() {
		return pincodes;
	}

	public void setPincodes(Integer pincodes) {
		this.pincodes = pincodes;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public String getImeiNo() {
		return imeiNo;
	}

	public void setImeiNo(String imeiNo) {
		this.imeiNo = imeiNo;
	}

	public String getserialNo() {
		return serialNo;
	}

	public void setserialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getpukCode() {
		return pukCode;
	}

	public void setpukCode(String pukCode) {
		this.pukCode = pukCode;
	}

	public String getsupportNo() {
		return supportNo;
	}

	public void setsupportNo(String supportNo) {
		this.supportNo = supportNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
