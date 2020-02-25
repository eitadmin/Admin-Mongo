package com.eiw.client.gxtmodel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.eiw.client.dashboard.LoginDashboardModule;
import com.eiw.client.dto.DeviceData;
import com.eiw.client.dto.GeoFencingData;
import com.eiw.client.dto.OperatorData;
import com.eiw.client.dto.ReportData;
import com.eiw.client.dto.UserPreferenceData;
import com.eiw.client.dto.VehicleData;
import com.google.gwt.i18n.client.DateTimeFormat;

public class DataReader {

	public static List<ModelVehicleData> getVehicleDataList(
			List<VehicleData> data) {
		List<ModelVehicleData> vData = new ArrayList<ModelVehicleData>();
		String vehicleNo, make, plateNo, group;
		String warrantyExpiryDate, dateOfPurchase, insuranceExpiryDate, iconUrl;
		float warrantyPeriodYear;
		String imeiNo;
		for (int i = 0; i < data.size(); i++) {
			vehicleNo = data.get(i).getVin();
			if (data.get(i).getWarrantyExpiryDate() != null) {
				warrantyExpiryDate = data.get(i).getWarrantyExpiryDate();
			} else
				warrantyExpiryDate = null;
			if (data.get(i).getDateOfPurchase() != null) {
				dateOfPurchase = data.get(i).getDateOfPurchase();
			} else
				dateOfPurchase = null;
			if (data.get(i).getInsuranceExpiryDate() != null) {
				insuranceExpiryDate = data.get(i).getInsuranceExpiryDate();
			} else
				insuranceExpiryDate = null;
			warrantyPeriodYear = data.get(i).getWarrantyPeriodYear();
			imeiNo = data.get(i).getImeiNo();
			make = data.get(i).getModel();
			plateNo = data.get(i).getPlateNo();
			group = data.get(i).getType();
			iconUrl = data.get(i).getIcon();
			vData.add(new ModelVehicleData(vehicleNo, warrantyExpiryDate,
					dateOfPurchase, insuranceExpiryDate, warrantyPeriodYear,
					imeiNo, make, plateNo, group, iconUrl));
		}
		return vData;

	}

	public static List<ModelDeviceData> getDeviceDataList(List<DeviceData> data) {
		List<ModelDeviceData> deviceData = new ArrayList<ModelDeviceData>();
		String imeiNo, vehicleNo, companyId, manufacturerName, modelName, vinNo;
		String dop, warrantyExpiryDate, simCardNo, serialNo, plateNo;
		Double cost;
		for (int i = 0; i < data.size(); i++) {
			imeiNo = data.get(i).getImeiNo();
			vehicleNo = data.get(i).getPlateNo();
			companyId = data.get(i).getCompanyId();
			if (data.get(i).getWarrantyExpiryDate() != null) {
				warrantyExpiryDate = data.get(i).getWarrantyExpiryDate();
			} else
				warrantyExpiryDate = null;
			if (data.get(i).getDop() != null) {
				dop = data.get(i).getDop();
			} else
				dop = null;
			cost = data.get(i).getCost();
			manufacturerName = data.get(i).getManufacturerName();
			modelName = data.get(i).getModelName();
			vinNo = data.get(i).getVin();
			simCardNo = data.get(i).getSimCardNo();
			serialNo = data.get(i).getSerialNumber();
			plateNo = data.get(i).getPlateNo();
			deviceData.add(new ModelDeviceData(imeiNo, vehicleNo, companyId,
					dop, warrantyExpiryDate, cost, manufacturerName, modelName,
					vinNo, simCardNo, serialNo, plateNo));
		}
		return deviceData;

	}

	public static List<ModelAlertData> getAlertTypesList(List<ReportData> data) {
		List<ModelAlertData> alertData = new ArrayList<ModelAlertData>();
		String alertType, description, htmldescription, htmlVehicleNo, plateNo, htmlPlateNo, htmlDescription, upperLimit = null, lowerLimit = null, alertFrom = null;
		String subject = null, content = null, location;
		int id;
		// Date timeStamp, tripDate;
		String vehicleNo, timeStamp, tripDate;
		for (int i = 0; i < data.size(); i++) {
			id = 0;
			if (data.get(i).getIcon() != null) {
				id = Integer.parseInt(data.get(i).getIcon().trim());
			}
			alertType = data.get(i).getAlertTypes();
			timeStamp = data.get(i).getTimeStamp();
			vehicleNo = data.get(i).getVin();
			description = data.get(i).getDescription();
			plateNo = data.get(i).getPlateNo();
			tripDate = data.get(i).getTripDate();
			htmlPlateNo = data.get(i).getHtmlVehicleNo();
			htmlDescription = data.get(i).getHtmlDescription();
			subject = data.get(i).getSubject();
			content = data.get(i).getContent();
			if ((data.get(i).getAlertTypes()
					.equalsIgnoreCase("GeozonePreferredAreaLeftAlert"))
					|| (data.get(i).getAlertTypes()
							.equalsIgnoreCase("GeozoneRestrictedAreaEnteredAlert"))
					|| (data.get(i).getAlertTypes().equalsIgnoreCase("Panic"))
					|| (data.get(i).getAlertTypes().equalsIgnoreCase("Theft"))) {
				alertFrom = "Hardware";

			} else if ((data.get(i).getAlertTypes()
					.equalsIgnoreCase("OverSpeedAlert"))
					|| (data.get(i).getAlertTypes()
							.equalsIgnoreCase("LowFuelLevelAlert"))) {
				alertFrom = "Software";
			}
			if (data.get(i).getAlertTypes().equalsIgnoreCase("OverSpeedAlert")) {
				upperLimit = String.valueOf(data.get(i).getSpeed());
				lowerLimit = data.get(i).getSubject();
			} else {
				upperLimit = "null";
				lowerLimit = "null";
			}

			// System.out.println("Alertssssss" + alertType + description
			// + vehicleNo);
			location = data.get(i).getUserEmailAddress();
			System.out.println("DATA::::" + timeStamp);
			alertData.add(new ModelAlertData(vehicleNo, timeStamp, alertType,
					description, tripDate, plateNo, htmlPlateNo,
					htmlDescription, upperLimit, lowerLimit, alertFrom,
					subject, content, location, id));
		}
		return alertData;

	}

	public static List<ModelAlertData> getGeoAlertTypesList(
			List<ReportData> data) {
		List<ModelAlertData> alertData = new ArrayList<ModelAlertData>();
		String alertType, description, htmldescription, htmlVehicleNo, plateNo, htmlPlateNo, htmlDescription, upperLimit = null, lowerLimit = null, alertFrom = null;
		String subject = null, content = null, lastUpdBy, area;
		// Date timeStamp, tripDate;
		double latitude, longitude;
		String vehicleNo, timeStamp, tripDate, location;
		for (int i = 0; i < data.size(); i++) {
			alertType = data.get(i).getAlertTypes();
			timeStamp = data.get(i).getTimeStamp();
			vehicleNo = data.get(i).getVin();
			description = data.get(i).getDescription();
			plateNo = data.get(i).getPlateNo();
			tripDate = data.get(i).getTripDate();
			htmlPlateNo = data.get(i).getHtmlVehicleNo();
			htmlDescription = data.get(i).getHtmlDescription();
			subject = data.get(i).getSubject();
			content = data.get(i).getContent();
			latitude = data.get(i).getLatitude();
			longitude = data.get(i).getLongitude();
			lastUpdBy = String.valueOf(LoginDashboardModule.mapGeoFenceId
					.get(Integer.parseInt(data.get(i).getLastUpdUser())));
			area = data.get(i).getContactNo();
			if ((data.get(i).getAlertTypes()
					.equalsIgnoreCase("GeozonePreferredAreaLeftAlert"))
					|| (data.get(i).getAlertTypes()
							.equalsIgnoreCase("GeozoneRestrictedAreaEnteredAlert"))
					|| (data.get(i).getAlertTypes().equalsIgnoreCase("Panic"))
					|| (data.get(i).getAlertTypes().equalsIgnoreCase("Theft"))) {
				alertFrom = "Hardware";

			} else if ((data.get(i).getAlertTypes()
					.equalsIgnoreCase("OverSpeedAlert"))
					|| (data.get(i).getAlertTypes()
							.equalsIgnoreCase("LowFuelLevelAlert"))) {
				alertFrom = "Software";
			}
			if (data.get(i).getAlertTypes().equalsIgnoreCase("OverSpeedAlert")) {
				upperLimit = String.valueOf(data.get(i).getSpeed());
				lowerLimit = data.get(i).getSubject();
			} else {
				upperLimit = "null";
				lowerLimit = "null";
			}

			// System.out.println("Alertssssss" + alertType + description
			// + vehicleNo);
			location = data.get(i).getUserEmailAddress();
			System.out.println("DATA::::" + timeStamp);
			alertData.add(new ModelAlertData(vehicleNo, timeStamp, alertType,
					description, tripDate, plateNo, htmlPlateNo,
					htmlDescription, upperLimit, lowerLimit, alertFrom,
					subject, content, latitude, longitude, lastUpdBy, area,
					location));
		}
		return alertData;

	}

	public static List<ModelOperatorData> getOperatorDataList(
			List<OperatorData> data) {
		List<ModelOperatorData> operatorData = new ArrayList<ModelOperatorData>();
		String operatorId, name, surname, designation, address, telNo, faxNo, email, emergency, vin, plateNo;
		Date dop, warrantyExpiryDate;
		Double cost;
		for (int i = 0; i < data.size(); i++) {
			vin = data.get(i).getVin();
			plateNo = data.get(i).getPlateNo();
			operatorId = data.get(i).getOperatorID();
			name = data.get(i).getName();
			surname = data.get(i).getSurName();
			designation = data.get(i).getDesignation();
			address = data.get(i).getAddress();
			telNo = data.get(i).getTelNo();
			faxNo = data.get(i).getfaxNo();
			email = data.get(i).geteMailAddress();
			emergency = data.get(i).getEmergencyContactNo();
			operatorData.add(new ModelOperatorData(operatorId, name, surname,
					designation, address, telNo, faxNo, email, emergency, vin,
					plateNo));
		}
		return operatorData;

	}

	public static List<ModelGeoFenceData> getGeoFenceList(
			List<GeoFencingData> data) {
		List<ModelGeoFenceData> geoData = new ArrayList<ModelGeoFenceData>();
		String zone, vin, area, shape, latlng, status, plateNo, geoNo;
		int id;
		for (int i = 0; i < data.size(); i++) {
			zone = data.get(i).getZone();
			vin = data.get(i).getVin();
			area = data.get(i).getArea();
			shape = data.get(i).getShape();
			latlng = data.get(i).getLatlng();
			status = data.get(i).getStatus();
			plateNo = data.get(i).getPlateNo();
			id = data.get(i).getId();
			geoNo = String.valueOf(LoginDashboardModule.mapGeoFenceId
					.get(Integer.parseInt(data.get(i).getDescription())));
			geoData.add(new ModelGeoFenceData(id, zone, vin, area, shape,
					latlng, status, plateNo, geoNo));
		}
		return geoData;
	}

	public static List<ModelVehicleData> getVehicleStatusReport(
			List<VehicleData> data) {
		DateTimeFormat dtf = DateTimeFormat
				.getFormat("EEE, MMM dd, yyyy hh:mm aaa");
		List<ModelVehicleData> modelVehicleDatas = new ArrayList<ModelVehicleData>();
		String vin, plateNo, type, make, model, group, status, city, location;
		double lat, lng;
		String timeStamp;
		for (int i = 0; i < data.size(); i++) {
			vin = data.get(i).getVin();
			plateNo = data.get(i).getPlateNo();
			group = data.get(i).getGroup();
			make = data.get(i).getMake();
			type = data.get(i).getType();
			model = data.get(i).getModel();
			timeStamp = data.get(i).getTimeStamp();
			System.out.println("TIMESTAMP::::::" + timeStamp);
			status = data.get(i).getStatus();
			lat = data.get(i).getLatitude();
			lng = data.get(i).getLongitude();
			city = data.get(i).getSubject();
			String latLng = String.valueOf(lat + "," + lng);
			location = data.get(i).getUserEmailAddress();
			modelVehicleDatas.add(new ModelVehicleData(vin, plateNo, group,
					make, model, timeStamp, status, type, lat, lng, latLng,
					city, location));
		}
		return modelVehicleDatas;
	}

	public static List<ModelUserPreferenceData> getPreferenceList(
			List<UserPreferenceData> userPreferenceData) {
		// TODO Auto-generated method stub
		List<ModelUserPreferenceData> modelUserPreferenceDatas = new ArrayList<ModelUserPreferenceData>();
		int id = 0;
		String name, title, description, shape, latlng, status, plateNo;
		for (int i = 0; i < userPreferenceData.size(); i++) {
			name = userPreferenceData.get(i).getName();
			description = userPreferenceData.get(i).getDescription();
			title = userPreferenceData.get(i).getTitle();
			latlng = userPreferenceData.get(i).getLatlng();
			modelUserPreferenceDatas.add(new ModelUserPreferenceData(id, name,
					title, description, null, latlng, null, null));
		}
		return modelUserPreferenceDatas;
	}

	public static List<ModelVehicleData> getUnsubVehi(
			List<VehicleData> vehicleDatas) {
		List<ModelVehicleData> modelVehicleDatas = new ArrayList<ModelVehicleData>();
		String plateNo, vin, type, model, userId;
		for (int i = 0; i < vehicleDatas.size(); i++) {
			plateNo = vehicleDatas.get(i).getPlateNo();
			vin = vehicleDatas.get(i).getVin();
			type = vehicleDatas.get(i).getType();
			model = vehicleDatas.get(i).getModel();
			userId = vehicleDatas.get(i).getUserEmailAddress();
			modelVehicleDatas.add(new ModelVehicleData(plateNo, vin, type,
					model, userId));

		}
		return modelVehicleDatas;
	}

	public static List<ModelVehicleData> getSubVehi(
			List<VehicleData> vehicleDatas) {
		List<ModelVehicleData> modelVehicleDatas = new ArrayList<ModelVehicleData>();
		String plateNo, vin, make, model, range, interval, status, smsThrough, emailThrough, smsNumber, emailAddress, alertType, statusED, format, intervalFormat;
		String reqInterval;
		for (int i = 0; i < vehicleDatas.size(); i++) {
			plateNo = vehicleDatas.get(i).getPlateNo();
			vin = vehicleDatas.get(i).getVin();
			make = vehicleDatas.get(i).getMake();
			model = vehicleDatas.get(i).getModel();
			alertType = vehicleDatas.get(i).getAlertTypes();
			if ((alertType.equalsIgnoreCase("Operation Time"))) {
				String limit[] = (vehicleDatas.get(i).getRange()).split("_");
				range = limit[0] + " To " + limit[1];
			} else
				range = vehicleDatas.get(i).getRange();

			interval = vehicleDatas.get(i).getInterval();
			format = getInteFmt(interval);
			reqInterval = chkInteFmt(format, interval);
			intervalFormat = reqInterval + " " + format;

			status = vehicleDatas.get(i).getStatus();
			if (status.equalsIgnoreCase("0")) {
				statusED = "Disable";
			} else
				statusED = "Enable";
			smsThrough = vehicleDatas.get(i).getThroughSms();
			emailThrough = vehicleDatas.get(i).getThroughEmail();
			smsNumber = vehicleDatas.get(i).getSmsNumber();
			emailAddress = vehicleDatas.get(i).getEmailAddress();
			modelVehicleDatas.add(new ModelVehicleData(plateNo, vin, make,
					model, range, interval, status, smsThrough, emailThrough,
					smsNumber, emailAddress, alertType, statusED, format,
					reqInterval, intervalFormat));

		}
		return modelVehicleDatas;
	}

	// public static List<ModelVehicleData> getEnabledVehi(
	// List<VehicleData> vehicleDatas) {
	// List<ModelVehicleData> modelVehicleDatas = new
	// ArrayList<ModelVehicleData>();
	// String plateNo, vin, make, model, range, interval, status, smsThrough,
	// emailThrough, smsNumber, emailAddress, alertType, statusED;
	// for (int i = 0; i < vehicleDatas.size(); i++) {
	// status = vehicleDatas.get(i).getStatus();
	// if (status.equalsIgnoreCase("1")) {
	// statusED = "Enable";
	// plateNo = vehicleDatas.get(i).getPlateNo();
	// vin = vehicleDatas.get(i).getVin();
	// make = vehicleDatas.get(i).getMake();
	// model = vehicleDatas.get(i).getModel();
	// alertType = vehicleDatas.get(i).getAlertTypes();
	// if ((alertType.equalsIgnoreCase("Operation Time"))) {
	// String limit[] = (vehicleDatas.get(i).getRange())
	// .split("_");
	// range = limit[0] + " To " + limit[1];
	// } else
	// range = vehicleDatas.get(i).getRange();
	//
	// interval = vehicleDatas.get(i).getInterval();
	// smsThrough = vehicleDatas.get(i).getThroughSms();
	// emailThrough = vehicleDatas.get(i).getThroughEmail();
	// smsNumber = vehicleDatas.get(i).getSmsNumber();
	// emailAddress = vehicleDatas.get(i).getEmailAddress();
	// modelVehicleDatas.add(new ModelVehicleData(plateNo, vin, make,
	// model, range, interval, status, smsThrough,
	// emailThrough, smsNumber, emailAddress, alertType,
	// statusED));
	// }
	//
	// }
	// return modelVehicleDatas;
	// }

	private static String getInteFmt(String duration) {
		String format = null;
		int interval = Integer.valueOf(duration);
		if (interval >= 60 && interval < 1440) {
			format = "Hours";
		} else if (interval >= 1440) {
			format = "Days";
		} else {
			format = "Minutes";
		}
		return format;
	}

	private static String chkInteFmt(String lbFmt, String duration) {
		int interval = Integer.valueOf(duration);
		String reqInterval = null;
		if (lbFmt.equalsIgnoreCase("Days")) {
			reqInterval = String.valueOf(interval / (24 * 60));
		} else if (lbFmt.equalsIgnoreCase("Hours")) {
			reqInterval = String.valueOf(interval / 60);
		} else {
			reqInterval = String.valueOf(interval);
		}
		return reqInterval;
	}

	public static List<ModelVehicleData> getMultiTrackVehicle(
			List<VehicleData> data) {
		List<ModelVehicleData> vData = new ArrayList<ModelVehicleData>();
		String vehicleNo, status, plateNo, group, timeStamp, operatorName, contactNo;
		int speed;
		String iconUrl, imageUrl, operatorImage, location;
		double latitude, longitude;
		for (int i = 0; i < data.size(); i++) {
			vehicleNo = data.get(i).getVin();
			timeStamp = data.get(i).getStatus();
			latitude = data.get(i).getLatitude();
			longitude = data.get(i).getLongitude();
			speed = data.get(i).getSpeed();
			if (speed > 0) {
				status = "Running";
			} else
				status = "Stop";
			plateNo = data.get(i).getPlateNo();
			operatorName = data.get(i).getOperatorName();
			contactNo = data.get(i).getContactNo();
			iconUrl = data.get(i).getIcon();
			imageUrl = data.get(i).getImageUrl();
			operatorImage = data.get(i).getOperatorImage();
			location = data.get(i).getDescription();
			vData.add(new ModelVehicleData(vehicleNo, timeStamp, latitude,
					longitude, speed, plateNo, operatorName, contactNo,
					iconUrl, imageUrl, operatorImage, location, status));
		}
		return vData;

	}

	public static List<ModelGeoFenceData> getGeozonesForAlret(
			List<GeoFencingData> geoDatas) {
		List<ModelGeoFenceData> modelGeoFenceDatas = new ArrayList<ModelGeoFenceData>();
		String plateNo, vin, parameterId, mobileNum, zone, shape, area, latLng;
		boolean alertStatus, onLeave, onEnter, throughSms;
		int id;
		for (int i = 0; i < geoDatas.size(); i++) {
			plateNo = geoDatas.get(i).getPlateNo();
			vin = geoDatas.get(i).getVin();
			parameterId = geoDatas.get(i).getParameterId();
			mobileNum = geoDatas.get(i).getMobileNum();
			zone = geoDatas.get(i).getZone();
			shape = geoDatas.get(i).getShape();
			area = geoDatas.get(i).getArea();
			latLng = geoDatas.get(i).getLatlng();
			id = geoDatas.get(i).getId();
			alertStatus = geoDatas.get(i).getAlertStatus();
			onLeave = geoDatas.get(i).getOnLeave();
			onEnter = geoDatas.get(i).getOnEnter();
			throughSms = geoDatas.get(i).getThroughSms();
			modelGeoFenceDatas.add(new ModelGeoFenceData(plateNo, vin,
					parameterId, mobileNum, zone, shape, area, latLng, id,
					alertStatus, onLeave, onEnter, throughSms));

		}
		return modelGeoFenceDatas;
	}
}