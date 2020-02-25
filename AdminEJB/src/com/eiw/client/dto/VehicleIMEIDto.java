package com.eiw.client.dto;

import java.io.Serializable;

public class VehicleIMEIDto implements Serializable {
	private String companyId;
	private String plateNo;
	private String imeiNo;
	private String fleetUser;
	

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getPlateNo() {
		return plateNo;
	}

	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}

	public String getImeiNo() {
		return imeiNo;
	}

	public void setImeiNo(String imeiNo) {
		this.imeiNo = imeiNo;
	}

	public String getFleetUser() {
		return fleetUser;
	}

	public void setFleetUser(String fleetUser) {
		this.fleetUser = fleetUser;
	}

}
