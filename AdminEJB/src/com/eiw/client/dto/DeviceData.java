package com.eiw.client.dto;

public class DeviceData implements java.io.Serializable {
	private String imeiNo;
	private String vin;
	private String plateNo;
	private String companyId;
	private String dop;
	private String warrantyExpiryDate;
	private Double cost;
	private String manufacturerName;
	private String modelName;
	private int rowNum;
	private String simCardNo;
	private String simCardDop;
	private String simCardExpiryDate;
	private String simCardProvider;
	private String simCardPlan;
	private String simCardPincode;
	private String defaultWarranty;
	private String serialNumber;
	private String additionalWarranty;
	private String userName;
	private String geozoneCnt;
	private int defProfileID;
	private String trxType;

	public String getImeiNo() {
		return imeiNo;
	}

	public void setImeiNo(String imeiNo) {
		this.imeiNo = imeiNo;
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

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getWarrantyExpiryDate() {
		return warrantyExpiryDate;
	}

	public void setWarrantyExpiryDate(String warrantyExpiryDate) {
		this.warrantyExpiryDate = warrantyExpiryDate;
	}

	public Double getCost() {
		if (cost == null) {
			return 0.0;
		}
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public String getPlateNo() {
		return plateNo;
	}

	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}

	public String getSimCardNo() {
		return simCardNo;
	}

	public void setSimCardNo(String simCardNo) {
		this.simCardNo = simCardNo;
	}

	public String getSimCardProvider() {
		return simCardProvider;
	}

	public void setSimCardProvider(String simCardProvider) {
		this.simCardProvider = simCardProvider;
	}

	public String getSimCardPlan() {
		return simCardPlan;
	}

	public void setSimCardPlan(String simCardPlan) {
		this.simCardPlan = simCardPlan;
	}

	public String getSimCardPincodes() {
		return simCardPincode;
	}

	public void setSimCardPincodes(String simCardPincode) {
		this.simCardPincode = simCardPincode;
	}

	public String getSimCardDop() {
		return simCardDop;
	}

	public void setSimCardDop(String simCardDop) {
		this.simCardDop = simCardDop;
	}

	public String getSimCardExpDate() {
		return simCardExpiryDate;
	}

	public void setSimCardExpDate(String simCardExpiryDate) {
		this.simCardExpiryDate = simCardExpiryDate;
	}

	public String getDefaultWarranty() {
		return defaultWarranty;
	}

	public void setDefaultWarranty(String defaultWarranty) {
		this.defaultWarranty = defaultWarranty;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getAdditionalWarranty() {
		return additionalWarranty;
	}

	public void setAdditionalWarranty(String additionalWarranty) {
		this.additionalWarranty = additionalWarranty;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGeozoneCnt() {
		return geozoneCnt;
	}

	public void setGeozoneCnt(String geozoneCnt) {
		this.geozoneCnt = geozoneCnt;
	}

	public int getDefProfileID() {
		return defProfileID;
	}

	public void setDefProfileID(int defProfileID) {
		this.defProfileID = defProfileID;
	}

	public String getTrxType() {
		return trxType;
	}

	public void setTrxType(String trxType) {
		this.trxType = trxType;
	}
}
