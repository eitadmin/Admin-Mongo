package com.skt.client.dto;

public class VehicleTripTimeDto {
	String vin;
	private String pickUpTripStarttime;
	private String pickUpTripEndtime;
	private String dropTripStarttime;
	private String dropTripEndtime;
	private String plateNo;

	public String getPlateNo() {
		return plateNo;
	}

	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getPickUpTripStarttime() {
		return pickUpTripStarttime;
	}

	public void setPickUpTripStarttime(String pickUpTripStarttime) {
		this.pickUpTripStarttime = pickUpTripStarttime;
	}

	public String getPickUpTripEndtime() {
		return pickUpTripEndtime;
	}

	public void setPickUpTripEndtime(String pickUpTripEndtime) {
		this.pickUpTripEndtime = pickUpTripEndtime;
	}

	public String getDropTripStarttime() {
		return dropTripStarttime;
	}

	public void setDropTripStarttime(String dropTripStarttime) {
		this.dropTripStarttime = dropTripStarttime;
	}

	public String getDropTripEndtime() {
		return dropTripEndtime;
	}

	public void setDropTripEndtime(String dropTripEndtime) {
		this.dropTripEndtime = dropTripEndtime;
	}
}
