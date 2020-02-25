package com.eiw.client.dto;

import java.util.Date;

public class VehicleEventPushData implements java.io.Serializable {

	private String vehicle_location;
	private Date vehicle_gpsDateTime;
	private Float vehicle_latitude;
	private Float vehicle_longitude;
	private Integer vehicle_speed;
	private Long vehicle_distance;
	private String vehicle_id;
	private String vehicle_regno;
	private String vehicle_alias;
	private String vendor;

	public String getVehicle_location() {
		return vehicle_location;
	}

	public void setVehicle_location(String vehicle_location) {
		this.vehicle_location = vehicle_location;
	}

	public Date getVehicle_gpsDateTime() {
		return vehicle_gpsDateTime;
	}

	public void setVehicle_gpsDateTime(Date vehicle_gpsDateTime) {
		this.vehicle_gpsDateTime = vehicle_gpsDateTime;
	}

	public Float getVehicle_latitude() {
		return vehicle_latitude;
	}

	public void setVehicle_latitude(Float vehicle_latitude) {
		this.vehicle_latitude = vehicle_latitude;
	}

	public Float getVehicle_longitude() {
		return vehicle_longitude;
	}

	public void setVehicle_longitude(Float vehicle_longitude) {
		this.vehicle_longitude = vehicle_longitude;
	}

	public Integer getVehicle_speed() {
		return vehicle_speed;
	}

	public void setVehicle_speed(Integer vehicle_speed) {
		this.vehicle_speed = vehicle_speed;
	}

	public Long getVehicle_distance() {
		return vehicle_distance;
	}

	public void setVehicle_distance(Long vehicle_distance) {
		this.vehicle_distance = vehicle_distance;
	}

	public String getVehicle_id() {
		return vehicle_id;
	}

	public void setVehicle_id(String vehicle_id) {
		this.vehicle_id = vehicle_id;
	}

	public String getVehicle_regno() {
		return vehicle_regno;
	}

	public void setVehicle_regno(String vehicle_regno) {
		this.vehicle_regno = vehicle_regno;
	}

	public String getVehicle_alias() {
		return vehicle_alias;
	}

	public void setVehicle_alias(String vehicle_alias) {
		this.vehicle_alias = vehicle_alias;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
}