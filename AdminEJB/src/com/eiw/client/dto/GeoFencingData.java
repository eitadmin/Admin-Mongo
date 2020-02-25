package com.eiw.client.dto;

import java.util.Map;

public class GeoFencingData implements java.io.Serializable {

	private String zone;
	private String vin;
	private String plateNo;
	private String area;
	private String shape;
	private String latlng;
	private String status;
	private String companyId;
	private String branchId;
	private String userId;
	private String title;
	private String description;
	private String name;
	private String userName;
	private int id, alertDay;
	private String parameterId;
	private String mobileNum, emailAddress, alertTime;
	private boolean onEnter;
	private boolean onLeave;
	private boolean throughSms, throughEmail;
	private boolean alertStatus;
	private String alertType;
	private String latlngCenter;
	private String latlngRadius;
	private String latlngMarker;
	private boolean isMarkerInside;
	private Map<Integer, String> latlngs;
	private AlertOptionData alertOptionData;
	private String endDate;

	public int getAlertDay() {
		return alertDay;
	}

	public void setAlertDay(int alertDay) {
		this.alertDay = alertDay;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAlertTime() {
		return alertTime;
	}

	public void setAlertTime(String alertTime) {
		this.alertTime = alertTime;
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

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getShape() {
		return shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

	public String getLatlng() {
		return latlng;
	}

	public void setLatlng(String latlng) {
		this.latlng = latlng;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPlateNo() {
		return plateNo;
	}

	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getParameterId() {
		return parameterId;
	}

	public void setParameterId(String parameterId) {
		this.parameterId = parameterId;
	}

	public String getMobileNum() {
		return mobileNum;
	}

	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public boolean getOnLeave() {
		return onLeave;
	}

	public void setOnLeave(boolean onLeave) {
		this.onLeave = onLeave;
	}

	public boolean getOnEnter() {
		return onEnter;
	}

	public void setOnEnter(boolean onEnter) {
		this.onEnter = onEnter;
	}

	public boolean getThroughSms() {
		return throughSms;
	}

	public void setThroughSms(boolean throughSms) {
		this.throughSms = throughSms;
	}

	public boolean getThroughEmail() {
		return throughEmail;
	}

	public void setThroughEmail(boolean throughEmail) {
		this.throughEmail = throughEmail;
	}

	public boolean getAlertStatus() {
		return alertStatus;
	}

	public void setAlertStatus(boolean alertStatus) {
		this.alertStatus = alertStatus;
	}

	public String getAlertType() {
		return alertType;
	}

	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}

	public String getLatlngCenter() {
		return latlngCenter;
	}

	public void setLatlngCenter(String latlngCenter) {
		this.latlngCenter = latlngCenter;
	}

	public String getLatlngRadius() {
		return latlngRadius;
	}

	public void setLatlngRadius(String latlngRadius) {
		this.latlngRadius = latlngRadius;
	}

	public String getLatlngMarker() {
		return latlngMarker;
	}

	public void setLatlngMarker(String latlngMarker) {
		this.latlngMarker = latlngMarker;
	}

	public boolean getIsMarkerInside() {
		return isMarkerInside;
	}

	public void setIsMarkerInside(boolean isMarkerInside) {
		this.isMarkerInside = isMarkerInside;
	}

	public Map<Integer, String> getLatlngs() {
		return latlngs;
	}

	public void setLatlngs(Map<Integer, String> latlngs) {
		this.latlngs = latlngs;
	}

	public AlertOptionData getAlertOptionData() {
		return alertOptionData;
	}

	public void setAlertOptionData(AlertOptionData alertOptionData) {
		this.alertOptionData = alertOptionData;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
