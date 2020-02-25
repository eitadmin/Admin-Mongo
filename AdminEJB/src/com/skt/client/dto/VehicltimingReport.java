package com.skt.client.dto;

import java.io.Serializable;
import java.sql.Date;

public class VehicltimingReport implements Serializable {
	private String vin;
	private int routeId;
	private String pickupstarttime;
	private String pickupendtime;
	private String dropstarttime;
	private String dropendtime;
	private Date lastupdatedDate;

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public int getRouteId() {
		return routeId;
	}

	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}

	public String getPickupstarttime() {
		return pickupstarttime;
	}

	public void setPickupstarttime(String pickupstarttime) {
		this.pickupstarttime = pickupstarttime;
	}

	public String getPickupendtime() {
		return pickupendtime;
	}

	public void setPickupendtime(String pickupendtime) {
		this.pickupendtime = pickupendtime;
	}

	public String getDropstarttime() {
		return dropstarttime;
	}

	public void setDropstarttime(String dropstarttime) {
		this.dropstarttime = dropstarttime;
	}

	public String getDropendtime() {
		return dropendtime;
	}

	public void setDropendtime(String dropendtime) {
		this.dropendtime = dropendtime;
	}

	public Date getLastupdatedDate() {
		return lastupdatedDate;
	}

	public void setLastupdatedDate(Date lastupdatedDate) {
		this.lastupdatedDate = lastupdatedDate;
	}
}
