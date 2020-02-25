package com.eiw.client.gxtmodel;

import java.util.Date;

import com.extjs.gxt.ui.client.data.BaseModel;

public class ModelSimcardData extends BaseModel {

	public ModelSimcardData() {
	}

	public ModelSimcardData(String simCardNo, Date dop, String dopStr,
			Date expiryDate, String expiryDateStr, int pincodes,
			String companyId, String provider, String plan, String serialNo,
			String pukCode, String supportNo) {

		set("simCardNo", simCardNo);
		set("companyId", companyId);
		set("dop", dop);
		set("dopStr", dopStr);
		set("expiryDate", expiryDate);
		set("expiryDateStr", expiryDateStr);
		set("pincodes", pincodes);
		set("provider", provider);
		set("plan", plan);
		set("serialNo", serialNo);
		set("pukCode", pukCode);
		set("supportNo", supportNo);

	}

	public String getSimCardNo() {
		return (String) get("simCardNo");
	}

	public Integer getPincodes() {
		return (Integer) get("pincodes");
	}

	public Date getWarrantyExpiryDate() {
		return (Date) get("warrantyExpiryDate");
	}

	public Date getExpiryDate() {
		return (Date) get("expiryDate");
	}

	public Date getDOP() {
		return (Date) get("dop");
	}

	public String getCompanyId() {
		return (String) get("companyId");
	}

	public String getProvider() {
		return (String) get("provider");
	}

	public String getPlan() {
		return (String) get("plan");
	}

	public String getserialNo() {
		return (String) get("serialNo");
	}

	public String getpukCode() {
		return (String) get("pukCode");
	}

	public String getsupportNo() {
		return (String) get("supportNo");
	}

	public String getDopStr() {
		return (String) get("dopStr");
	}

	public String getExpiryDateStr() {
		return (String) get("expiryDateStr");
	}
}
