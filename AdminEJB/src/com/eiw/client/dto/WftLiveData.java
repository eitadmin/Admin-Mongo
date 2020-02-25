package com.eiw.client.dto;

@SuppressWarnings("serial")
public class WftLiveData implements java.io.Serializable {

	private String vin;
	private String timeStamp;
	private double latitude;
	private double longitude;
	private int speed;
	private String status;
	private int direction;
	private String locationTimeStamp;
	private boolean gps;
	private String gsmSignalStrength;
	private String powerSupplyVoltage;
	private String batteryvoltage;

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public String getLocationTimeStamp() {
		return locationTimeStamp;
	}

	public void setLocationTimeStamp(String locationTimeStamp) {
		this.locationTimeStamp = locationTimeStamp;
	}

	public boolean isGps() {
		return gps;
	}

	public void setGps(boolean gps) {
		this.gps = gps;
	}

	public String getGsmSignalStrength() {
		return gsmSignalStrength;
	}

	public void setGsmSignalStrength(String gsmSignalStrength) {
		this.gsmSignalStrength = gsmSignalStrength;
	}

	public String getPowerSupplyVoltage() {
		return powerSupplyVoltage;
	}

	public void setPowerSupplyVoltage(String powerSupplyVoltage) {
		this.powerSupplyVoltage = powerSupplyVoltage;
	}

	public String getBatteryvoltage() {
		return batteryvoltage;
	}

	public void setBatteryvoltage(String batteryvoltage) {
		this.batteryvoltage = batteryvoltage;
	}

}
