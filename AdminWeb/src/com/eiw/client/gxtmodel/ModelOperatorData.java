package com.eiw.client.gxtmodel;

import java.util.Date;

import com.extjs.gxt.ui.client.data.BaseModel;

public class ModelOperatorData extends BaseModel {

	public ModelOperatorData() {
	}

	public ModelOperatorData(String operatorId, String name, String surname,
			String designation, String address, String telNo, String faxNo,
			String email, String emergency, String licenseNo,
			Date licenseIssue, String licenseIssueStr, Date licenseExpiry,
			String licenseExpiryStr, String insuranceNo, Date insuranceIssue,
			String insuranceIssueStr, Date insuranceExpiry,
			String insuranceExpiryStr, String iqamahNo, String language,
			String langArabic, String langUrdu, String langEnglish,
			String useMobile, String useBarcode, String nationality,
			String notes, String model, String type, Date effFrom,
			String address2, String emergencycontactname, String vin) {

		set("operatorId", operatorId);
		set("name", name);
		set("surname", surname);
		set("designation", designation);
		set("address", address);
		set("telno", telNo);
		set("faxno", faxNo);
		set("emailaddress", email);
		set("emergencycontactno", emergency);
		set("operatorId", operatorId);
		set("licenseNo", licenseNo);
		set("licenseIssue", licenseIssue);
		set("licenseIssueStr", licenseIssueStr);
		set("licenseExpiry", licenseExpiry);
		set("licenseExpiryStr", licenseExpiryStr);
		set("insuranceNo", insuranceNo);
		set("insuranceIssue", insuranceIssue);
		set("insuranceIssueStr", insuranceIssueStr);
		set("insuranceExpiry", insuranceExpiry);
		set("insuranceExpiryStr", insuranceExpiryStr);
		set("iqamahNo", iqamahNo);
		set("language", language);
		set("langArabic", langArabic);
		set("langUrdu", langUrdu);
		set("langEnglish", langEnglish);
		set("useMobile", useMobile);
		set("useBarcode", useBarcode);
		set("nationality", nationality);
		set("notes", notes);
		set("model", model);
		set("type", type);
		set("effFrom", effFrom);
		set("address2", address2);
		set("emergencycontactname", emergencycontactname);
		set("vin", vin);

	}

	public ModelOperatorData(String operatorId, String name, String surname,
			String designation, String address, String telNo, String plateNo,
			String email, String emergency, String licenseNo,
			Date licenseIssue, String licenseIssueStr, Date licenseExpiry,
			String licenseExpiryStr, String insuranceNo, Date insuranceIssue,
			String insuranceIssueStr, Date insuranceExpiry,
			String insuranceExpiryStr, String iqamahNo, String language,
			String langArabic, String langUrdu, String langEnglish,
			String useMobile, String useBarcode, String nationality,
			String notes, String model, String type, Date effFrom,
			String address2, String emergencycontactname, String vin,
			String faxNo) {

		set("operatorId", operatorId);
		set("name", name);
		set("surname", surname);
		set("designation", designation);
		set("address", address);
		set("telno", telNo);
		set("plateNo", plateNo);
		set("emailaddress", email);
		set("emergencycontactno", emergency);
		set("operatorId", operatorId);
		set("licenseNo", licenseNo);
		set("licenseIssue", licenseIssue);
		set("licenseIssueStr", licenseIssueStr);
		set("licenseExpiry", licenseExpiry);
		set("licenseExpiryStr", licenseExpiryStr);
		set("insuranceNo", insuranceNo);
		set("insuranceIssue", insuranceIssue);
		set("insuranceIssueStr", insuranceIssueStr);
		set("insuranceExpiry", insuranceExpiry);
		set("insuranceExpiryStr", insuranceExpiryStr);
		set("iqamahNo", iqamahNo);
		set("language", language);
		set("langArabic", langArabic);
		set("langUrdu", langUrdu);
		set("langEnglish", langEnglish);
		set("useMobile", useMobile);
		set("useBarcode", useBarcode);
		set("nationality", nationality);
		set("notes", notes);
		set("model", model);
		set("type", type);
		set("effFrom", effFrom);
		set("address2", address2);
		set("emergencycontactname", emergencycontactname);
		set("vin", vin);
		set("faxno", faxNo);
	}

	public ModelOperatorData(String operatorId, String name, String surname,
			String telNo, String type, String model, String faxNo,
			String email, String emergencyNo, String designation) {

		set("operatorId", operatorId);
		set("name", name);
		set("surname", surname);
		set("telno", telNo);
		set("faxno", faxNo);
		set("model", model);
		set("type", type);
		set("email", email);
		set("emergencyNo", emergencyNo);
		set("designation", designation);
	}

	public String getOperatorId() {
		return (String) get("operatorId");
	}

	public String getName() {
		return (String) get("name");
	}

	public String getSurname() {
		return (String) get("surname");
	}

	public String getDesignation() {
		return (String) get("designation");
	}

	public String getAddress() {
		return (String) get("address");
	}

	public String getTelNo() {
		return (String) get("telno");

	}

	public String getFaxNo() {
		return (String) get("faxno");
	}

	public String getEMail() {
		return (String) get("emailaddress");
	}

	public String getEmergency() {
		return (String) get("emergencycontactno");
	}

	public String getLicenseNo() {
		return (String) get("licenseNo");
	}

	public Date getLicenseIssue() {
		return (Date) get("licenseIssue");
	}

	public Date getLicenseExpiry() {
		return (Date) get("licenseExpiry");
	}

	public String getInsuranceNo() {
		return (String) get("insuranceNo");
	}

	public Date getInsuranceIssue() {
		return (Date) get("insuranceIssue");

	}

	public Date getInsuranceExpiry() {
		return (Date) get("insuranceExpiry");
	}

	public String getIqamahNo() {
		return (String) get("iqamahNo");
	}

	public String getLanguage() {
		return (String) get("language");
	}

	public String getLangArabic() {
		return (String) get("langArabic");
	}

	// public String getLangArabicWrite() {
	// return (String) get("langArabicWrite");
	// }

	public String getLangUrdu() {
		return (String) get("langUrdu");
	}

	// public String getLangUrduRead() {
	// return (String) get("langUrduRead");
	//
	// }

	public String getLangEnglish() {
		return (String) get("langEnglish");
	}

	// public String getLangEnglishWrite() {
	// return (String) get("langEnglishWrite");
	// }

	public String getUseMobile() {
		return (String) get("useMobile");
	}

	public String getUseBarcode() {
		return (String) get("useBarcode");
	}

	public String getNationality() {
		return (String) get("nationality");
	}

	public String getNotes() {
		return (String) get("notes");
	}

	public String getModel() {
		return (String) get("model");
	}

	public String getType() {
		return (String) get("type");
	}

	public Date getEffFrom() {
		return (Date) get("effFrom");

	}

	public String getEmail() {
		return (String) get("email");
	}

	public String getEmergencyNo() {
		return (String) get("emergencyNo");
	}

	public String getAddress2() {
		return (String) get("address2");
	}

	public String getEmergencyContactName() {
		return (String) get("emergencycontactname");
	}

	public String getVin() {
		return (String) get("vin");
	}

	public String getLicenseIssueStr() {
		return (String) get("licenseIssueStr");
	}

	public String getLicenseExpiryStr() {
		return (String) get("licenseExpiryStr");
	}

	public String getInsuranceIssueStr() {
		return (String) get("insuranceIssueStr");
	}

	public String getInsuranceExpiryStr() {
		return (String) get("insuranceExpiryStr");
	}

	public String getPlateNo() {
		return (String) get("plateNo");
	}

	public ModelOperatorData(String operatorId, String name, String surname,
			String designation, String address, String telNo, String faxNo,
			String email, String emergency, String vin, String plateNo) {

		set("operatorId", operatorId);
		set("name", name);
		set("surname", surname);
		set("designation", designation);
		set("address", address);
		set("telno", telNo);
		set("faxno", faxNo);
		set("emailaddress", email);
		set("emergencycontactno", emergency);
		set("vin", vin);
		set("plateNo", plateNo);

	}
}
