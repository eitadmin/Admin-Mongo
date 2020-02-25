package com.eiw.client.gxtmodel;

import com.extjs.gxt.ui.client.data.BaseModel;

public class ModelAlertData extends BaseModel {

	public ModelAlertData() {
	}

	public ModelAlertData(String vehicleNo, String alertTime, String alertType,
			String description, String tripDate, String plateNo,
			String htmlPlateNo, String htmlDescription, String upperLimit,
			String lowerLimit, String alertFrom, String subject, String content) {

		set("vehicleNo", vehicleNo);
		set("content", content);
		set("subject", subject);
		set("timeStamp", alertTime);
		set("alertType", alertType);
		set("description", description);
		set("tripDate", tripDate);
		set("plateNo", plateNo);
		set("htmlPlateNo", htmlPlateNo);
		set("htmlDescription", htmlDescription);
		set("upperLimit", upperLimit);
		set("lowerLimit", lowerLimit);
		set("alertFrom", alertFrom);

	}

	public ModelAlertData(String vehicleNo, String alertTime, String alertType,
			String description, String tripDate, String plateNo,
			String htmlPlateNo, String htmlDescription, String upperLimit,
			String lowerLimit, String alertFrom, String subject,
			String content, String location, int id) {

		set("vehicleNo", vehicleNo);
		set("content", content);
		set("subject", subject);
		set("timeStamp", alertTime);
		set("alertType", alertType);
		set("description", description);
		set("tripDate", tripDate);
		set("plateNo", plateNo);
		set("htmlPlateNo", htmlPlateNo);
		set("htmlDescription", htmlDescription);
		set("upperLimit", upperLimit);
		set("lowerLimit", lowerLimit);
		set("alertFrom", alertFrom);
		set("location", location);
		set("id", id);

	}

	public ModelAlertData(String vehicleNo, String alertTime, String alertType,
			String description, String tripDate, String plateNo,
			String htmlPlateNo, String htmlDescription, String upperLimit,
			String lowerLimit, String alertFrom, String subject,
			String content, double latitude, double longitude,
			String lastUpdBy, String area) {

		set("vehicleNo", vehicleNo);
		set("content", content);
		set("subject", subject);
		set("timeStamp", alertTime);
		set("alertType", alertType);
		set("description", description);
		set("tripDate", tripDate);
		set("plateNo", plateNo);
		set("htmlPlateNo", htmlPlateNo);
		set("htmlDescription", htmlDescription);
		set("upperLimit", upperLimit);
		set("lowerLimit", lowerLimit);
		set("alertFrom", alertFrom);
		set("latitude", latitude);
		set("longitude", longitude);
		set("lastUpdBy", lastUpdBy);
		set("area", area);
	}

	public ModelAlertData(String vehicleNo, String alertTime, String alertType,
			String description, String tripDate, String plateNo,
			String htmlPlateNo, String htmlDescription, String upperLimit,
			String lowerLimit, String alertFrom, String subject,
			String content, double latitude, double longitude,
			String lastUpdBy, String area, String location) {

		set("vehicleNo", vehicleNo);
		set("content", content);
		set("subject", subject);
		set("timeStamp", alertTime);
		set("alertType", alertType);
		set("description", description);
		set("tripDate", tripDate);
		set("plateNo", plateNo);
		set("htmlPlateNo", htmlPlateNo);
		set("htmlDescription", htmlDescription);
		set("upperLimit", upperLimit);
		set("lowerLimit", lowerLimit);
		set("alertFrom", alertFrom);
		set("latitude", latitude);
		set("longitude", longitude);
		set("lastUpdBy", lastUpdBy);
		set("area", area);
		set("location", location);
	}

	public String getVehicleNo() {
		return (String) get("vehicleNo");
	}

	public String getContent() {
		return (String) get("content");
	}

	public String getSubject() {
		return (String) get("subject");
	}

	public String getAlertTime() {
		return (String) get("timeStamp");
	}

	public String getAlertType() {
		return (String) get("alertType");
	}

	public String getDescription() {
		return (String) get("description");
	}

	public String getTripDate() {
		return (String) get("tripDate");
	}

	public String getPlateNo() {
		return (String) get("plateNo");
	}

	public String getHtmlPlateNo() {
		return (String) get("htmlPlateNo");
	}

	public String getHtmlDescription() {
		return (String) get("htmlDescription");
	}

	public String getUpperLimit() {
		return (String) get("upperLimit");
	}

	public String getLowerLimit() {
		return (String) get("lowerLimit");
	}

	public String getAlertFrom() {
		return (String) get("alertFrom");
	}

	public double getLatitude() {
		double latitude = get("latitude");
		return latitude;

	}

	public double getLongitude() {
		double longitude = get("longitude");
		return longitude;

	}

	public String getLastUpdBy() {
		return (String) get("lastUpdBy");
	}

	public String getArea() {
		return (String) get("area");
	}

	public String getLocation() {
		return (String) get("location");
	}

	public int getId() {
		return (Integer) get("id");
	}
}
