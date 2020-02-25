package com.eiw.client.fleetmgmt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.eiw.client.dashboard.LoginDashboardModule;
import com.eiw.client.dto.CompanyDataAdmin;
import com.eiw.client.dto.DeviceData;
import com.eiw.client.dto.OperatorData;
import com.eiw.client.dto.SimcardData;
import com.eiw.client.dto.VehicleData;
import com.eiw.client.gxtmodel.Maintenance;
import com.eiw.client.gxtmodel.ModelCompanyData;
import com.eiw.client.gxtmodel.ModelDeviceData;
import com.eiw.client.gxtmodel.ModelOperatorData;
import com.eiw.client.gxtmodel.ModelSimcardData;
import com.eiw.client.gxtmodel.ModelVehicleData;

public class DataReaderAdmin {
	public static List<ModelVehicleData> getVehicleDataList(
			List<VehicleData> data) {
		List<ModelVehicleData> vData = new ArrayList<ModelVehicleData>();
		String vehicleNo, plateNo, model, type, mileage, alertSetting, group, fleetmanager, dateOfPurchaseStr = null, insuranceExpiryDateStr = null, dateOfRegistrationStr = null, registrationExpiryStr = null, additionalWarrantyStr = null, warrantyExpiryDateStr = null, status, imageUrl;
		Date warrantyExpiryDate, dateOfPurchase, insuranceExpiryDate, additionalWarranty, dateOfRegistration, registrationExpiry;
		float warrantyPeriodYear;
		String imeiNo;
		for (int i = 0; i < data.size(); i++) {
			vehicleNo = data.get(i).getVin();
			plateNo = data.get(i).getPlateNo();

			if (data.get(i).getAdditionalWarranty() != null) {
				additionalWarranty = LoginDashboardModule.defTimeZone
						.parse(data.get(i).getAdditionalWarranty());
				additionalWarrantyStr = data.get(i).getAdditionalWarranty()
						.substring(0, 10);
			} else {
				additionalWarranty = null;
				additionalWarrantyStr = null;
			}
			if (data.get(i).getWarrantyExpiryDate() != null) {
				warrantyExpiryDate = LoginDashboardModule.defTimeZone
						.parse(data.get(i).getWarrantyExpiryDate());
				warrantyExpiryDateStr = data.get(i).getWarrantyExpiryDate()
						.substring(0, 10);
			} else {
				warrantyExpiryDate = null;
				warrantyExpiryDateStr = null;
			}
			if (data.get(i).getDateOfPurchase() != null) {
				dateOfPurchase = LoginDashboardModule.defTimeZone.parse(data
						.get(i).getDateOfPurchase());
				dateOfPurchaseStr = data.get(i).getDateOfPurchase()
						.substring(0, 10);
			} else {
				dateOfPurchase = null;
				dateOfPurchaseStr = null;
			}
			if (data.get(i).getInsuranceExpiryDate() != null) {
				insuranceExpiryDate = LoginDashboardModule.defTimeZone
						.parse(data.get(i).getInsuranceExpiryDate());
				insuranceExpiryDateStr = data.get(i).getInsuranceExpiryDate()
						.substring(0, 10);
			} else {
				insuranceExpiryDate = null;
				insuranceExpiryDateStr = null;
			}
			warrantyPeriodYear = data.get(i).getWarrantyPeriodYear();
			imeiNo = data.get(i).getImeiNo();
			model = data.get(i).getModel();
			type = data.get(i).getType();
			group = data.get(i).getGroup();
			if (data.get(i).getRegDate() != null) {
				dateOfRegistration = LoginDashboardModule.defTimeZone
						.parse(data.get(i).getRegDate());
				dateOfRegistrationStr = data.get(i).getRegDate()
						.substring(0, 10);
			} else {
				dateOfRegistration = null;
				dateOfRegistrationStr = null;
			}
			if (data.get(i).getRegExpDate() != null) {
				registrationExpiry = LoginDashboardModule.defTimeZone
						.parse(data.get(i).getRegExpDate());
				registrationExpiryStr = data.get(i).getRegExpDate()
						.substring(0, 10);
			} else {
				registrationExpiry = null;
				registrationExpiryStr = null;
			}

			mileage = data.get(i).getMileageInit();
			alertSetting = data.get(i).getContactNo();
			// alertSetting = data.get(i).getAlertTypes();
			if (data.get(i).getStatus() != null) {
				status = data.get(i).getStatus();
			} else
				status = null;
			imageUrl = data.get(i).getImageUrl();
			// if (alertSetting != null && !alertSetting.equalsIgnoreCase("")) {
			// String str = data.get(i).getAlertTypes();
			// String a[] = str.split(",");
			// String ref[] = a[2].split("=");
			// if (ref.length > 1 && ref[1] != null) {
			// System.out.println(ref[0] + " " + ref[1]);
			// alertSetting = ref[1];
			// }
			// } else {
			// alertSetting = "";
			// }
			fleetmanager = data.get(i).getFleetManager();
			vData.add(new ModelVehicleData(vehicleNo, warrantyExpiryDate,
					warrantyExpiryDateStr, dateOfPurchase, dateOfPurchaseStr,
					insuranceExpiryDate, insuranceExpiryDateStr,
					warrantyPeriodYear, imeiNo, plateNo, model, type, mileage,
					additionalWarranty, additionalWarrantyStr, alertSetting,
					group, dateOfRegistration, dateOfRegistrationStr,
					registrationExpiry, registrationExpiryStr, status,
					fleetmanager, imageUrl));
		}
		return vData;
	}

	public static List<ModelDeviceData> getDeviceDataList(List<DeviceData> data) {
		List<ModelDeviceData> deviceData = new ArrayList<ModelDeviceData>();
		String imeiNo, vehicleNo, companyId, manufacturerName, modelName, plateNo, simcardNo, serialNumber, warrantyExpiryDateStr = null, dopStr = null, additionalWarrantyStr = null;
		Date dop, warrantyExpiryDate, additionalWarranty;
		Double cost;
		for (int i = 0; i < data.size(); i++) {
			imeiNo = data.get(i).getImeiNo();
			vehicleNo = data.get(i).getVin();
			plateNo = data.get(i).getPlateNo();
			companyId = data.get(i).getCompanyId();
			if (data.get(i).getWarrantyExpiryDate() != null) {
				warrantyExpiryDate = LoginDashboardModule.defTimeZone
						.parse(data.get(i).getWarrantyExpiryDate());
				warrantyExpiryDateStr = data.get(i).getWarrantyExpiryDate()
						.substring(0, 10);
			} else
				warrantyExpiryDate = null;
			if (data.get(i).getDop() != null) {
				dop = LoginDashboardModule.defTimeZone.parse(data.get(i)
						.getDop());
				dopStr = data.get(i).getDop().substring(0, 10);
			} else
				dop = null;

			cost = data.get(i).getCost();
			manufacturerName = data.get(i).getManufacturerName();
			modelName = data.get(i).getModelName();
			simcardNo = data.get(i).getSimCardNo();
			serialNumber = data.get(i).getSerialNumber();
			if (data.get(i).getAdditionalWarranty() != null) {
				additionalWarranty = LoginDashboardModule.defTimeZone
						.parse(data.get(i).getAdditionalWarranty());
				additionalWarrantyStr = data.get(i).getAdditionalWarranty()
						.substring(0, 10);
			} else
				additionalWarranty = null;
			deviceData.add(new ModelDeviceData(imeiNo, vehicleNo, companyId,
					dop, dopStr, warrantyExpiryDate, warrantyExpiryDateStr,
					cost, manufacturerName, modelName, plateNo, serialNumber,
					additionalWarranty, additionalWarrantyStr));
		}
		return deviceData;

	}

	public static List<ModelDeviceData> getAssociatedDeviceDataList(
			List<DeviceData> data) {
		List<ModelDeviceData> deviceData = new ArrayList<ModelDeviceData>();
		String imeiNo, companyId, manufacturerName, modelName, simcardNo, dopStr = null, warrantyExpiryDateStr = null;
		Date dop, warrantyExpiryDate;
		Double cost;
		for (int i = 0; i < data.size(); i++) {
			imeiNo = data.get(i).getImeiNo();
			if (data.get(i).getWarrantyExpiryDate() != null) {
				warrantyExpiryDate = LoginDashboardModule.defTimeZone
						.parse(data.get(i).getWarrantyExpiryDate());
				warrantyExpiryDateStr = data.get(i).getWarrantyExpiryDate()
						.substring(0, 10);
			} else
				warrantyExpiryDate = null;
			if (data.get(i).getDop() != null) {
				dop = LoginDashboardModule.defTimeZone.parse(data.get(i)
						.getDop());
				dopStr = data.get(i).getDop().substring(0, 10);
			} else
				dop = null;
			cost = data.get(i).getCost();
			manufacturerName = data.get(i).getManufacturerName();
			modelName = data.get(i).getModelName();
			simcardNo = data.get(i).getSimCardNo();

			deviceData.add(new ModelDeviceData(imeiNo, dop, dopStr,
					warrantyExpiryDate, warrantyExpiryDateStr, cost,
					manufacturerName, modelName, simcardNo));
		}
		return deviceData;

	}

	public static List<ModelDeviceData> getDeviceDataWithoutSimcardList(
			List<DeviceData> data) {
		List<ModelDeviceData> deviceData = new ArrayList<ModelDeviceData>();
		String imeiNo, companyId, manufacturerName, modelName, plateNo, warrantyExpiryDateStr = null, dopStr = null;
		Date dop, warrantyExpiryDate;
		Double cost;
		for (int i = 0; i < data.size(); i++) {
			imeiNo = data.get(i).getImeiNo();
			// companyId = data.get(i).getCompanyId();
			if (data.get(i).getWarrantyExpiryDate() != null) {
				warrantyExpiryDate = LoginDashboardModule.defTimeZone
						.parse(data.get(i).getWarrantyExpiryDate());
				warrantyExpiryDateStr = data.get(i).getWarrantyExpiryDate()
						.substring(0, 10);
			} else
				warrantyExpiryDate = null;
			if (data.get(i).getDop() != null) {
				dop = LoginDashboardModule.defTimeZone.parse(data.get(i)
						.getDop());
				dopStr = data.get(i).getDop().substring(0, 10);
			} else
				dop = null;
			cost = data.get(i).getCost();
			manufacturerName = data.get(i).getManufacturerName();
			modelName = data.get(i).getModelName();

			deviceData.add(new ModelDeviceData(imeiNo, dop, dopStr,
					warrantyExpiryDate, warrantyExpiryDateStr, cost,
					manufacturerName, modelName));
		}
		return deviceData;

	}

	public static List<ModelSimcardData> getSimcardDataList(
			List<SimcardData> data) {
		List<ModelSimcardData> sData = new ArrayList<ModelSimcardData>();
		String simCardNo, companyId, provider, plan, serialNo, pukCode, supportNo, dopStr = null, expiryDateStr = null;
		Date dop, expiryDate;
		int pincodes;
		for (int i = 0; i < data.size(); i++) {
			simCardNo = data.get(i).getSimCardNo();
			companyId = data.get(i).getCompanyId();
			if (data.get(i).getDop() != null) {
				dop = LoginDashboardModule.defTimeZone.parse(data.get(i)
						.getDop());
				dopStr = data.get(i).getDop().substring(0, 10);
			} else
				dop = null;
			if (data.get(i).getExpiryDate() != null) {
				expiryDate = LoginDashboardModule.defTimeZone.parse(data.get(i)
						.getExpiryDate());
				expiryDateStr = data.get(i).getExpiryDate().substring(0, 10);
			} else
				expiryDate = null;
			provider = data.get(i).getProvider();
			plan = data.get(i).getPlan();
			pincodes = data.get(i).getPincodes();
			serialNo = data.get(i).getserialNo();
			pukCode = data.get(i).getpukCode();
			supportNo = data.get(i).getsupportNo();

			sData.add(new ModelSimcardData(simCardNo, dop, dopStr, expiryDate,
					expiryDateStr, pincodes, companyId, provider, plan,
					serialNo, pukCode, supportNo));
		}
		return sData;

	}

	public static List<ModelCompanyData> getCompanyDataList(
			List<CompanyDataAdmin> data) {
		List<ModelCompanyData> companyData = new ArrayList<ModelCompanyData>();
		String companyName, password, companyId, branchName, branchId, address, addressLine1, addressLine2, addressCity, addressCountryCode, addressPhone, fax, contact, email, userName, roleName, countryName, imgUrl;
		for (int i = 0; i < data.size(); i++) {
			companyName = data.get(i).getCompanyName();
			companyId = data.get(i).getCompanyId();
			branchName = data.get(i).getBranchName();
			branchId = data.get(i).getBranchId();
			address = data.get(i).getAddress();
			addressLine1 = data.get(i).getAddressLine1();
			addressLine2 = data.get(i).getAddressLine2();
			addressCity = data.get(i).getAddressCity();
			addressCountryCode = data.get(i).getAddressCountryCode();
			addressPhone = data.get(i).getAddressPhone();
			fax = data.get(i).getFax();
			email = data.get(i).getEmail();
			userName = data.get(i).getUserName();
			password = data.get(i).getPwd();
			contact = data.get(i).getContact();
			roleName = data.get(i).getRoleName();
			countryName = data.get(i).getCountryName();
			imgUrl = data.get(i).getImageUrl();
			companyData.add(new ModelCompanyData(companyName, companyId,
					branchName, branchId, address, addressLine1, addressLine2,
					addressCity, addressCountryCode, addressPhone, contact,
					fax, email, userName, password, roleName, countryName,
					imgUrl));
		}
		return companyData;

	}

	public static List<ModelOperatorData> getOperatorList(
			List<OperatorData> data) {
		List<ModelOperatorData> operatorData = new ArrayList<ModelOperatorData>();
		String operatorId, name, surname, designation, address, telNo, plateNo, faxNo, address2, email, emergency, iqamahNo, language, nationality, useMobile, useBarcode, insuranceNo, licenseNo, langArabic, langUrdu, langEnglish, addNotes, model, type, emergencycontactname, vin, insuranceIssueStr = null, insuranceExpiryStr = null, licenseIssueStr = null, licenseExpiryStr = null;
		Date insuranceIssue, insuranceExpiry, licenseIssue, licenseExpiry, effFrom;
		for (int i = 0; i < data.size(); i++) {

			operatorId = data.get(i).getOperatorID();
			name = data.get(i).getName();
			surname = data.get(i).getSurName();
			designation = data.get(i).getDesignation();
			address = data.get(i).getAddress();
			telNo = data.get(i).getTelNo();
			address2 = data.get(i).getAddress2();
			System.out.println("in datareader" + data.get(i).getAddress2());
			email = data.get(i).geteMailAddress();
			emergency = data.get(i).getEmergencyContactNo();
			iqamahNo = data.get(i).getIqamahNo();
			language = data.get(i).getLanguage();
			nationality = data.get(i).getNationality();
			useMobile = data.get(i).getUseMobile();
			useBarcode = data.get(i).getUseBarcode();
			insuranceNo = data.get(i).getInsuranceNo();
			if (data.get(i).getInsuranceIssue() != null) {
				insuranceIssue = LoginDashboardModule.defTimeZone.parse(data
						.get(i).getInsuranceIssue());
				insuranceIssueStr = data.get(i).getInsuranceIssue()
						.substring(0, 10);
			} else
				insuranceIssue = null;
			if (data.get(i).getInsuranceExpiry() != null) {
				insuranceExpiry = LoginDashboardModule.defTimeZone.parse(data
						.get(i).getInsuranceExpiry());
				insuranceExpiryStr = data.get(i).getInsuranceExpiry()
						.substring(0, 10);
			} else
				insuranceExpiry = null;
			licenseNo = data.get(i).getLicenseNo();
			if (data.get(i).getLicenseIssue() != null) {
				licenseIssue = LoginDashboardModule.defTimeZone.parse(data.get(
						i).getLicenseIssue());
				licenseIssueStr = data.get(i).getLicenseIssue()
						.substring(0, 10);
			} else
				licenseIssue = null;
			if (data.get(i).getLicenseExpiry() != null) {
				licenseExpiry = LoginDashboardModule.defTimeZone.parse(data
						.get(i).getLicenseExpiry());
				licenseExpiryStr = data.get(i).getLicenseExpiry();
			} else
				licenseExpiry = null;
			langArabic = data.get(i).getLangArabic();
			langUrdu = data.get(i).getLangUrdu();
			langEnglish = data.get(i).getLangEnglish();
			addNotes = data.get(i).getNotes();
			model = data.get(i).getVehicleModel();
			type = data.get(i).getVehicleType();
			if (data.get(i).geteffFrom() != null) {
				effFrom = LoginDashboardModule.defTimeZone.parse(data.get(i)
						.geteffFrom());
			} else
				effFrom = null;
			plateNo = data.get(i).getPlateNo();
			faxNo = data.get(i).getfaxNo();
			vin = data.get(i).getVin();
			System.out.println("vinnnnnnnnnnnnn" + vin);
			emergencycontactname = data.get(i).getEmergencyContactName();
			operatorData.add(new ModelOperatorData(operatorId, name, surname,
					designation, address, telNo, plateNo, email, emergency,
					licenseNo, licenseIssue, licenseIssueStr, licenseExpiry,
					licenseExpiryStr, insuranceNo, insuranceIssue,
					insuranceIssueStr, insuranceExpiry, insuranceExpiryStr,
					iqamahNo, language, langArabic, langUrdu, langEnglish,
					useMobile, useBarcode, nationality, addNotes, model, type,
					effFrom, address2, emergencycontactname, vin, faxNo));
		}
		return operatorData;

	}

	public static List<ModelOperatorData> getVehicleOperatorList(
			List<OperatorData> data) {
		List<ModelOperatorData> vehiOpeData = new ArrayList<ModelOperatorData>();
		String operatorId, name, surname, designation, address, telNo, faxNo, email, emergency, iqamahNo, language, langArabicRead, langArabicWrite, langUrduWrite, langUrduRead, langEnglishRead, langEnglishWrite, useMobile, useBarcode, insuranceNo, licenseNo, langArabic, langUrdu, langEnglish;
		String nationality = null, notes;
		Date dop, warrantyExpiryDate, insuranceIssue, insuranceExpiry, licenseIssue, licenseExpiry, effFrom;
		Double cost;
		for (int i = 0; i < data.size(); i++) {
			name = data.get(i).getVin();
			operatorId = data.get(i).getOperatorID();
			telNo = data.get(i).getTelNo();
			name = data.get(i).getName();
			surname = data.get(i).getSurName();
			effFrom = LoginDashboardModule.defTimeZone.parse(data.get(i)
					.geteffFrom());
			// model=data.get(i).getmo

			// vehiOpeData.add(new ModelOperatorData(operatorId, name, surname,
			// designation, address, telNo, faxNo, email, emergency,
			// licenseNo, licenseIssue, licenseExpiry, insuranceNo,
			// insuranceIssue, insuranceExpiry, iqamahNo, language,
			// langArabic, langUrdu, langEnglish, useMobile, useBarcode,
			// nationality, notes));
		}
		return vehiOpeData;

	}

	public static List<ModelCompanyData> getLoginDetails(
			List<CompanyDataAdmin> modelCompanyDatas) {
		List<ModelCompanyData> modelDatas = new ArrayList<ModelCompanyData>();
		String compName, brchName, userName, userRole, loginTime, macIp, logoutTime, timeConnected;
		for (int i = 0; i < modelCompanyDatas.size(); i++) {
			compName = modelCompanyDatas.get(i).getCompanyName();
			brchName = modelCompanyDatas.get(i).getBranchName();
			userName = modelCompanyDatas.get(i).getUserName();
			userRole = modelCompanyDatas.get(i).getRoleName();
			loginTime = modelCompanyDatas.get(i).getLoginTime();
			macIp = modelCompanyDatas.get(i).getMacIp();
			logoutTime = modelCompanyDatas.get(i).getLogoutTime();
			timeConnected = modelCompanyDatas.get(i).getTimeConnected();
			modelDatas.add(new ModelCompanyData(compName, brchName, userName,
					userRole, loginTime, macIp, logoutTime, timeConnected));

		}

		return modelDatas;
	}

	public static List<ModelVehicleData> getBWUtilList(
			List<VehicleData> vehicleData) {
		List<ModelVehicleData> modelVehicleDatas = new ArrayList<ModelVehicleData>();
		String vin, clientName, transmission, timeStamp, imei, plateNo;
		int bytesTrx;

		for (int i = 0; i < vehicleData.size(); i++) {
			vin = vehicleData.get(i).getVin();
			clientName = vehicleData.get(i).getCompanyId();
			timeStamp = vehicleData.get(i).getTimeStamp();
			transmission = vehicleData.get(i).getCount();
			imei = vehicleData.get(i).getImeiNo();
			bytesTrx = (int) vehicleData.get(i).getEventId();
			plateNo = vehicleData.get(i).getPlateNo();
			modelVehicleDatas.add(new ModelVehicleData(vin, clientName,
					transmission, timeStamp, imei, bytesTrx, plateNo));
		}
		return modelVehicleDatas;
	}

	public static List<ModelCompanyData> getErrorLogDetails(
			List<CompanyDataAdmin> modelCompanyDatas) {
		List<ModelCompanyData> modelDatas = new ArrayList<ModelCompanyData>();
		String vin, description, module, serverTime, serverDate, plateNo, serverTimeStamp, eventSource, local = null;
		for (int i = 0; i < modelCompanyDatas.size(); i++) {
			vin = modelCompanyDatas.get(i).getVin();
			module = modelCompanyDatas.get(i).getModule();
			description = modelCompanyDatas.get(i).getDescription();
			serverTime = modelCompanyDatas.get(i).getServerTime();
			serverDate = modelCompanyDatas.get(i).getServerDate();
			plateNo = modelCompanyDatas.get(i).getPlateNo();
			serverTimeStamp = modelCompanyDatas.get(i).getTimeConnected();
			eventSource = modelCompanyDatas.get(i).getEmail();
			modelDatas.add(new ModelCompanyData(vin, module, description,
					serverTime, serverDate, plateNo, serverTimeStamp,
					eventSource, local));

		}

		return modelDatas;
	}

	public static List<Maintenance> getCompFeatures(
			List<CompanyDataAdmin> modelCompanyDatas) {
		List<Maintenance> modelDatas = new ArrayList<Maintenance>();
		Map<String, List<String>> featurefeaturesName;
		String feature, featureName = null;
		boolean isEnabled = true;
		// for (int i = 0; i < modelCompanyDatas.size(); i++) {
		List<String> listOfFeatures = new ArrayList<String>();
		featurefeaturesName = modelCompanyDatas.get(0).getCompFeatures();
		for (Map.Entry<String, List<String>> e1 : featurefeaturesName
				.entrySet()) {
			feature = e1.getKey();
			for (Map.Entry<String, List<String>> e2 : featurefeaturesName
					.entrySet()) {
				if (e2.getKey().equalsIgnoreCase(feature)) {
					listOfFeatures = e2.getValue();
				}
			}
			for (int j = 0; j < listOfFeatures.size(); j++) {
				featureName = listOfFeatures.get(j);
				String featureStatus[] = featureName.split(",");
				modelDatas.add(new Maintenance(feature, featureStatus[0],
						featureStatus[1], featureStatus[2], featureStatus[3]));
			}
		}

		// }

		return modelDatas;
	}

	public static List<ModelVehicleData> getVehicleEventData(
			List<VehicleData> vehicleData) {
		List<ModelVehicleData> modelVehicleDatas = new ArrayList<ModelVehicleData>();
		String vin, fromDate, toDate, timeStamp, ioEvent, groupByDate, ioEventValues, serverTimeStamp, eventSource;
		double lat, lng;
		int speed, bytesTrx = 0;

		for (int i = 0; i < vehicleData.size(); i++) {
			vin = vehicleData.get(i).getVin();
			groupByDate = vehicleData.get(i).getDescription();
			timeStamp = vehicleData.get(i).getTimeStamp();
			fromDate = vehicleData.get(i).getfromDate();
			toDate = vehicleData.get(i).getToDate();
			lat = vehicleData.get(i).getLatitude();
			lng = vehicleData.get(i).getLongitude();
			ioEvent = vehicleData.get(i).getIoEvent();
			ioEventValues = vehicleData.get(i).getSubject();
			serverTimeStamp = vehicleData.get(i).getAlertTime();
			if (vehicleData.get(i).getContent() != null) {
				bytesTrx = Integer.valueOf(vehicleData.get(i).getContent());
			}
			speed = vehicleData.get(i).getSpeed();
			eventSource = vehicleData.get(i).getEventStatus();
			modelVehicleDatas.add(new ModelVehicleData(vin, fromDate, toDate,
					timeStamp, lat, lng, bytesTrx, speed, ioEvent, groupByDate,
					ioEventValues, serverTimeStamp, eventSource));
		}
		return modelVehicleDatas;
	}

	public static List<ModelVehicleData> getSMSCount(
			List<VehicleData> vehicleData) {
		List<ModelVehicleData> modelVehicleDatas = new ArrayList<ModelVehicleData>();
		String companyId, branchId, status, lastUpdBy;
		long smsCount;
		for (int i = 0; i < vehicleData.size(); i++) {
			companyId = vehicleData.get(i).getCompanyId();
			branchId = vehicleData.get(i).getBranchId();
			if (vehicleData.get(i).getStatus().equalsIgnoreCase("true")) {
				status = "Enabled";
			} else {
				status = "Disabled";
			}
			lastUpdBy = vehicleData.get(i).getLastUpdDate();
			smsCount = vehicleData.get(i).getEventId();
			modelVehicleDatas.add(new ModelVehicleData(companyId, branchId,
					status, lastUpdBy, smsCount));
		}
		return modelVehicleDatas;
	}

	public static List<ModelCompanyData> getCountry(List<CompanyDataAdmin> data) {
		List<ModelCompanyData> companyData = new ArrayList<ModelCompanyData>();
		String countryCode, countryName;
		for (int i = 0; i < data.size(); i++) {
			countryName = data.get(i).getCountryName();
			countryCode = data.get(i).getCountryCode();
			companyData.add(new ModelCompanyData(countryName, countryCode));
		}
		return companyData;

	}

	public static List<ModelCompanyData> getCompanyDetails(
			List<CompanyDataAdmin> companyDataAdmins) {
		List<ModelCompanyData> modelCompanyDatas = new ArrayList<ModelCompanyData>();

		String companyId, companyName, addressLine1, addressLine2, addressCity, countryName, loginId, remarks, region, pwd, saleperson, shorterlink = null;
		Boolean isdemo = null, isfollowup = null, isNoTransRptSkip = null, isMiniApps = null, isLostDeal = null, isSspWft = null;
		Long addresssid = 0l;
		for (int i = 0; i < companyDataAdmins.size(); i++) {
			companyId = companyDataAdmins.get(i).getCompanyId();
			companyName = companyDataAdmins.get(i).getCompanyName();
			addressLine1 = companyDataAdmins.get(i).getAddressLine1();
			addressLine2 = companyDataAdmins.get(i).getAddressLine2();
			addressCity = companyDataAdmins.get(i).getAddressCity();
			countryName = companyDataAdmins.get(i).getCountryName();
			loginId = companyDataAdmins.get(i).getUserName();
			remarks = companyDataAdmins.get(i).getRemarks();
			region = companyDataAdmins.get(i).getModule();
			isdemo = companyDataAdmins.get(i).getIsdemo();
			isfollowup = companyDataAdmins.get(i).getIsfollowup();
			isNoTransRptSkip = companyDataAdmins.get(i)
					.getIsNoTransmissionSkip();
			isMiniApps = companyDataAdmins.get(i).getIsMiniApps();
			addresssid = companyDataAdmins.get(i).getAddressId();
			pwd = companyDataAdmins.get(i).getPwd();
			saleperson = companyDataAdmins.get(i).getSalesPerson();
			isLostDeal = companyDataAdmins.get(i).getIsLostDeal();
			isSspWft = companyDataAdmins.get(i).getIsSspWft();
			shorterlink = companyDataAdmins.get(i).getMobileAppShortenLink();
			modelCompanyDatas.add(new ModelCompanyData(companyId, companyName,
					addressLine1, addressLine2, addressCity, countryName,
					loginId, remarks, region, isdemo, isfollowup,
					isNoTransRptSkip, addresssid, pwd, isMiniApps, saleperson,
					isLostDeal, isSspWft, shorterlink));
		}
		return modelCompanyDatas;
	}
}