package com.eiw.client.gxtmodel;

import java.util.Date;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.maps.gwt.client.Marker;

public class ModelVehicleData extends BaseModel {

	public ModelVehicleData() {
	}

	public ModelVehicleData(String vehicleNo, String plateNo, Marker marker) {
		set("vehicleNo", vehicleNo);
		set("plateNo", plateNo);
		set("marker", marker);
	}

	public ModelVehicleData(String vehicleNo, Date warrantyExpiryDate,
			Date dateOfPurchase, Date insuranceExpiryDate,
			float warrantyPeriodYear, String imeiNo, String make,
			String plateNo, String group) {

		set("vehicleNo", vehicleNo);
		set("warrantyExpiryDate", warrantyExpiryDate);
		set("dateOfPurchase", dateOfPurchase);
		set("warrantyExpiryDate", warrantyExpiryDate);
		set("insuranceExpiryDate", insuranceExpiryDate);
		set("warrantyPeriodYear", warrantyPeriodYear);
		set("imeiNo", imeiNo);
		set("make", make);
		set("plateNo", plateNo);
		set("group", group);

	}

	public ModelVehicleData(String vehicleNo, String warrantyExpiryDate,
			String dateOfPurchase, String insuranceExpiryDate,
			float warrantyPeriodYear, String imeiNo, String make,
			String plateNo, String group) {

		set("vehicleNo", vehicleNo);
		set("warrantyExpiryDate", warrantyExpiryDate);
		set("dateOfPurchase", dateOfPurchase);
		set("warrantyExpiryDate", warrantyExpiryDate);
		set("insuranceExpiryDate", insuranceExpiryDate);
		set("warrantyPeriodYear", warrantyPeriodYear);
		set("imeiNo", imeiNo);
		set("make", make);
		set("plateNo", plateNo);
		set("group", group);

	}

	public ModelVehicleData(String vehicleNo, String warrantyExpiryDate,
			String dateOfPurchase, String insuranceExpiryDate,
			float warrantyPeriodYear, String imeiNo, String make,
			String plateNo, String group, String iconUrl) {

		set("vehicleNo", vehicleNo);
		set("warrantyExpiryDate", warrantyExpiryDate);
		set("dateOfPurchase", dateOfPurchase);
		set("warrantyExpiryDate", warrantyExpiryDate);
		set("insuranceExpiryDate", insuranceExpiryDate);
		set("warrantyPeriodYear", warrantyPeriodYear);
		set("imeiNo", imeiNo);
		set("make", make);
		set("plateNo", plateNo);
		set("group", group);
		set("iconUrl", iconUrl);

	}

	public ModelVehicleData(String vin, String plateNo, String group,
			String make, String model, Date timeStamp, String status) {
		set("vehicleNo", vin);
		set("plateNo", plateNo);
		set("group", group);
		set("make", make);
		set("model", model);
		set("timeStamp", timeStamp);
		set("status", status);

	}

	public ModelVehicleData(String plateNo, String vin, String group,
			String make, String model, String timeStampStr, String status,
			String type) {
		set("plateNo", plateNo);
		set("vehicleNo", vin);
		set("group", group);
		set("make", make);
		set("model", model);
		set("timeStampStr", timeStampStr);
		set("status", status);
		set("type", type);

	}

	public ModelVehicleData(String vin, String plateNo, String group,
			String make, String model, String timeStampStr, String status,
			String type, double lat, double lng, String latLng, String city) {
		set("vehicleNo", vin);
		set("plateNo", plateNo);
		set("group", group);
		set("make", make);
		set("model", model);
		set("timeStampStr", timeStampStr);
		set("status", status);
		set("type", type);
		set("lat", lat);
		set("lng", lng);
		set("latLng", latLng);
		set("city", city);
	}

	public ModelVehicleData(String vin, String plateNo, String group,
			String make, String model, String timeStampStr, String status,
			String type, double lat, double lng, String latLng, String city,
			String location) {
		set("vehicleNo", vin);
		set("plateNo", plateNo);
		set("group", group);
		set("make", make);
		set("model", model);
		set("timeStampStr", timeStampStr);
		set("status", status);
		set("type", type);
		set("lat", lat);
		set("lng", lng);
		set("latLng", latLng);
		set("city", city);
		set("location", location);

	}

	public ModelVehicleData(String plateNo, String vehicleNo, String type,
			String model) {
		set("plateNo", plateNo);
		set("vehicleNo", vehicleNo);
		set("type", type);
		set("model", model);
	}

	public ModelVehicleData(String plateNo, String vin, String make,
			String model, String range, String interval, String status,
			String smsThrough, String emailThrough, String smsNumber,
			String emailAddress, String alertType, String statusED,
			String format, String reqInterval, String intervalFormat) {
		set("plateNo", plateNo);
		set("vehicleNo", vin);
		set("make", make);
		set("model", model);
		set("range", range);
		set("interval", interval);
		set("status", status);
		set("smsThrough", smsThrough);
		set("emailThrough", emailThrough);
		set("smsNumber", smsNumber);
		set("emailAddress", emailAddress);
		set("alertType", alertType);
		set("statusED", statusED);
		set("format", format);
		set("reqInterval", reqInterval);
		set("intervalFormat", intervalFormat);

	}

	public ModelVehicleData(String vehicleNo, String timeStamp, double lat,
			double lng, int speed, String plateNo, String operatorName,
			String contactNo, String iconUrl, String imageUrl,
			String operatorImage, String location, String status) {

		set("vehicleNo", vehicleNo);
		set("timeStamp", timeStamp);
		set("lat", lat);
		set("lng", lng);
		set("speed", speed);
		set("plateNo", plateNo);
		set("operatorName", operatorName);
		set("contactNo", contactNo);
		set("iconUrl", iconUrl);
		set("imageUrl", imageUrl);
		set("operatorImage", operatorImage);
		set("location", location);
		set("status", status);

	}

	public ModelVehicleData(String plateNo, String vehicleNo, String type,
			String model, String user, Date dateOfPurchase,
			Date warrantyExpiryDate, Date additionalWarrantyExpiryDate,
			Date regDate, Date regExpDate, String contNum,
			Date insuranceExpiryDate) {
		set("plateNo", plateNo);
		set("vehicleNo", vehicleNo);
		set("type", type);
		set("model", model);
		set("user", user);
		set("dateOfPurchase", dateOfPurchase);
		set("warrantyExpiryDate", warrantyExpiryDate);
		set("additionalWarrantyExpiryDate", additionalWarrantyExpiryDate);
		set("regDate", regDate);
		set("regExpDate", regExpDate);
		set("contNum", contNum);
		set("insuranceExpiryDate", insuranceExpiryDate);
	}

	public ModelVehicleData(String plateNo, String vehicleNo, String type,
			String model, String userId) {
		set("plateNo", plateNo);
		set("vehicleNo", vehicleNo);
		set("type", type);
		set("model", model);
		set("userId", userId);
	}

	public String getVehicleNo() {
		String vehicleNo = (String) get("vehicleNo");
		return vehicleNo;
	}

	public String getMake() {
		String make = (String) get("make");
		return make;
	}

	public Marker getMarker() {
		return (Marker) get("marker");
	}

	public Date getTimeStamp() {
		return (Date) get("timeStamp");
	}

	public String getTimeStampStr() {
		String timeStampStr = (String) get("timeStampStr");
		return timeStampStr;
	}

	public double getLat() {
		double lat = get("lat");
		return lat;

	}

	public double getLng() {
		double lng = get("lng");
		return lng;

	}

	public String getLatLng() {
		String latLng = (String) get("latLng");
		return latLng;
	}

	public String getCity() {
		String city = (String) get("city");
		return city;
	}

	public String getLocation() {
		String location = (String) get("location");
		return location;
	}

	public String getIconUrl() {
		String iconUrl = (String) get("iconUrl");
		return iconUrl;
	}

	public String getInterval() {
		String interval = (String) get("interval");
		return interval;
	}

	public String getSmsThrough() {
		String smsThrough = (String) get("smsThrough");
		return smsThrough;
	}

	public String getEmailThrough() {
		String emailThrough = (String) get("emailThrough");
		return emailThrough;
	}

	public String getSmsNumber() {
		String smsNumber = (String) get("smsNumber");
		return smsNumber;
	}

	public String getEmailAddress() {
		String emailAddress = (String) get("emailAddress");
		return emailAddress;
	}

	public String getAlertType() {
		String alertType = (String) get("alertType");
		return alertType;
	}

	public String getRange() {
		String range = (String) get("range");
		return range;
	}

	public String getStatusED() {
		String statusED = (String) get("statusED");
		return statusED;
	}

	public String getFormat() {
		String format = (String) get("format");
		return format;
	}

	public String getReqInterval() {
		String reqInterval = (String) get("reqInterval");
		return reqInterval;
	}

	public String getIntervalFormat() {
		String intervalFormat = (String) get("intervalFormat");
		return intervalFormat;
	}

	public String getSpeed() {
		String speed = (String) get("speed");
		return speed;
	}

	public String getOperatorName() {
		String operatorName = (String) get("operatorName");
		return operatorName;
	}

	public String getContactNo() {
		String contactNo = (String) get("contactNo");
		return contactNo;
	}

	public String getOperatorImage() {
		String operatorImage = (String) get("operatorImage");
		return operatorImage;
	}

	public String getUser() {
		String user = (String) get("user");
		return user;
	}

	public Date getAdditionalWarrantyExpiryDate() {
		return (Date) get("additionalWarrantyExpiryDate");
	}

	public Date getRegDate() {
		return (Date) get("regDate");
	}

	public Date getRegExpDate() {
		return (Date) get("regExpDate");
	}

	public String getContNum() {
		String contNum = (String) get("contNum");
		return contNum;
	}

	public String getUserId() {
		String userId = (String) get("userId");
		return userId;
	}

	// admin
	public ModelVehicleData(String vin, Date warrantyExpiryDate,
			String warrantyExpiryDateStr, Date dateOfPurchase,
			String dateOfPurchaseStr, Date insuranceExpiryDate,
			String insuranceExpiryDateStr, float warrantyPeriodYear,
			String imeiNo, String plateNo, String model, String type,
			String mileage, Date additionalWarranty,
			String additionalWarrantyStr, String alertSetting, String group,
			Date dateOfRegistration, String dateOfRegistrationStr,
			Date registrationExpiry, String registrationExpiryStr,
			String status, String fleetmanager, String imageUrl) {

		set("vin", vin);
		set("plateNo", plateNo);
		set("warrantyExpiryDate", warrantyExpiryDate);
		set("warrantyExpiryDateStr", warrantyExpiryDateStr);
		set("dateOfPurchase", dateOfPurchase);
		set("dateOfPurchaseStr", dateOfPurchaseStr);
		set("insuranceExpiryDate", insuranceExpiryDate);
		set("insuranceExpiryDateStr", insuranceExpiryDateStr);
		set("warrantyPeriodYear", warrantyPeriodYear);
		set("imeiNo", imeiNo);
		set("model", model);
		set("type", type);
		set("mileage", mileage);
		set("additionalWarranty", additionalWarranty);
		set("additionalWarrantyStr", additionalWarrantyStr);
		set("alertSetting", alertSetting);
		set("group", group);
		set("dateOfRegistration", dateOfRegistration);
		set("dateOfRegistrationStr", dateOfRegistrationStr);
		set("registrationExpiry", registrationExpiry);
		set("registrationExpiryStr", registrationExpiryStr);
		set("status", status);
		set("fleetmanager", fleetmanager);
		set("imageUrl", imageUrl);
	}

	public ModelVehicleData(String vehicleNo, String clientName,
			String transmission, String timeStampStr, String imei,
			int bytesTrx, String plateNo) {
		set("vehicleNo", vehicleNo);
		set("clientName", clientName);
		set("transmission", transmission);
		set("timeStampStr", timeStampStr);
		set("imei", imei);
		set("bytesTrx", bytesTrx);
		set("plateNo", plateNo);
	}

	public ModelVehicleData(String vin, String fromDate, String toDate,
			String eventTimeStamp, double lat, double lng, int bytesTrx,
			int speed, String ioEvent, String groupByDate,
			String ioEventDetails, String serverTimeStamp, String eventSource) {

		set("vin", vin);
		set("fromDate", fromDate);
		set("toDate", toDate);
		set("eventTimeStamp", eventTimeStamp);
		set("lat", lat);
		set("lng", lng);
		set("bytesTrx", bytesTrx);
		set("speed", speed);
		set("ioEvent", ioEvent);
		set("groupByDate", groupByDate);
		set("ioEventDetails", ioEventDetails);
		set("serverTimeStamp", serverTimeStamp);
		set("eventSource", eventSource);

	}

	public ModelVehicleData(String companyId, String branchId, String status,
			String lastUpdBy, long smsCount) {
		// TODO Auto-generated constructor stub
		set("companyId", companyId);
		set("branchId", branchId);
		set("status", status);
		set("lastUpdBy", lastUpdBy);
		set("smsCount", smsCount);
	}

	public String getVin() {
		return (String) get("vin");
	}

	public String getInsuranceExpiryDateStr() {
		return (String) get("insuranceExpiryDateStr");
	}

	public String getWarrantyExpiryDateStr() {
		return (String) get("warrantyExpiryDateStr");
	}

	public String getAdditionalWarrantyStr() {
		return (String) get("additionalWarrantyStr");
	}

	public String getDateOfRegistrationStr() {
		return (String) get("dateOfRegistrationStr");
	}

	public String getRegistrationExpiryStr() {
		return (String) get("registrationExpiryStr");
	}

	public String getDateOfPurchaseStr() {
		return (String) get("dateOfPurchaseStr");
	}

	public String getPlateNo() {
		return (String) get("plateNo");
	}

	public Date getWarrantyExpiryDate() {
		return (Date) get("warrantyExpiryDate");
	}

	public Date getDateOfPurchase() {
		return (Date) get("dateOfPurchase");
	}

	public Date getInsuranceExpiryDate() {
		return (Date) get("insuranceExpiryDate");
	}

	public float getWarrantyPeriodYear() {
		Float warrantyPeriodYear = (Float) get("warrantyPeriodYear");
		return warrantyPeriodYear.floatValue();
	}

	public String getImeiNo() {
		return (String) get("imeiNo");
	}

	public String getModel() {
		return (String) get("model");
	}

	public String getType() {
		return (String) get("type");
	}

	public String getMileage() {
		return (String) get("mileage");
	}

	public Date getadditionalWarranty() {
		return (Date) get("additionalWarranty");
	}

	public String getAlertSetting() {
		return (String) get("alertSetting");
	}

	public String getGroup() {
		return (String) get("group");
	}

	public Date getRegistrationExpiry() {
		return (Date) get("registrationExpiry");
	}

	public Date getDateOfRegistration() {
		return (Date) get("dateOfRegistration");
	}

	public String getStatus() {
		return (String) get("status");
	}

	public String getFleetManager() {
		return (String) get("fleetmanager");
	}

	public String getImageUrl() {
		return (String) get("imageUrl");
	}

	public String getClientName() {
		String clientName = (String) get("clientName");
		return clientName;
	}

	public String getTransmission() {
		String transmission = (String) get("transmission");
		return transmission;
	}

	public String getImei() {
		String imei = (String) get("imei");
		return imei;
	}

	public int getBytesTrx() {
		int bytesTrx = (Integer) get("bytesTrx");
		return bytesTrx;
	}

	public String getFromDate() {
		return (String) get("fromDate");
	}

	public String getToDate() {
		return (String) get("toDate");
	}

	public String getIOEvent() {
		return (String) get("ioEvent");
	}

	public String getGroupByDate() {
		return (String) get("groupByDate");
	}

	public String getIoEventDetails() {
		return (String) get("ioEventDetails");
	}

	public String getCompanyId() {
		return (String) get("companyId");
	}

	public String getBranchId() {
		return (String) get("branchId");
	}

	public String getLastUpdBy() {
		return (String) get("lastUpdBy");
	}

	public long getSmsCount() {
		long smsCount = (Long) get("smsCount");
		return smsCount;
	}

	public String getServerTimeStamp() {
		return (String) get("serverTimeStamp");
	}

	public String getEventTimeStamp() {
		return (String) get("eventTimeStamp");
	}

	public String getEventSource() {
		return (String) get("eventSource");
	}
}
