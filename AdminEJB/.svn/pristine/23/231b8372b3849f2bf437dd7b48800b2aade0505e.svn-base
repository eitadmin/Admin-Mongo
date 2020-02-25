package com.eiw.device.Ushetel;

import java.util.Calendar;
import java.util.Date;

public class UshetelGpsData {
	private String header;
	private Date dateTime;
	private boolean gpsFix;
	private float latitude;
	private float longitude;
	private int speed;
	private String heading;
	private String cellId;
	private String gsmSignal;
	private String satellites;
	private long batteryLevel;
	private boolean acc;
	private String diStatus;
	private String tamperingStatus;
	private String outputStatus;
	private float ai1;
	private float ai2;
	private String deviceId;
	private Long odometer;

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public boolean isGpsFix() {
		return gpsFix;
	}

	public void setGpsFix(boolean gpsFix) {
		this.gpsFix = gpsFix;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public String getCellId() {
		return cellId;
	}

	public void setCellId(String cellId) {
		this.cellId = cellId;
	}

	public String getGsmSignal() {
		return gsmSignal;
	}

	public void setGsmSignal(String gsmSignal) {
		this.gsmSignal = gsmSignal;
	}

	public String getSatellites() {
		return satellites;
	}

	public void setSatellites(String satellites) {
		this.satellites = satellites;
	}

	public long getBatteryLevel() {
		return batteryLevel;
	}

	public void setBatteryLevel(long batteryLevel) {
		this.batteryLevel = batteryLevel;
	}

	public boolean getAcc() {
		return acc;
	}

	public void setAcc(boolean acc) {
		this.acc = acc;
	}

	public String getDiStatus() {
		return diStatus;
	}

	public void setDiStatus(String diStatus) {
		this.diStatus = diStatus;
	}

	public String gettamperingStatus() {
		return tamperingStatus;
	}

	public void settamperingStatus(String tamperingStatus) {
		this.tamperingStatus = tamperingStatus;
	}

	public String getOutputStatus() {
		return outputStatus;
	}

	public void setOutputStatus(String outputStatus) {
		this.outputStatus = outputStatus;
	}

	public float getAi1() {
		return ai1;
	}

	public void setAi1(float ai1) {
		this.ai1 = ai1;
	}

	public float getAi2() {
		return ai2;
	}

	public void setAi2(float ai2) {
		this.ai2 = ai2;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Long getOdometer() {
		return odometer;
	}

	public void setOdometer(Long odometer) {
		this.odometer = odometer;
		}

	public void read(String[] deviceData) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR,
				Integer.parseInt(deviceData[2].substring(4, 8).trim()));
		cal.set(Calendar.MONTH,
				Integer.parseInt(deviceData[2].substring(2, 4).trim()) - 1);
		cal.set(Calendar.DAY_OF_MONTH,
				Integer.parseInt(deviceData[2].substring(0, 2).trim()));
		cal.set(Calendar.HOUR_OF_DAY,
				Integer.parseInt(deviceData[3].substring(0, 2).trim()));
		cal.set(Calendar.MINUTE,
				Integer.parseInt(deviceData[3].substring(2, 4).trim()));
		cal.set(Calendar.SECOND,
				Integer.parseInt(deviceData[3].substring(4, 6).trim()));
		this.dateTime = cal.getTime();
		this.gpsFix = deviceData[4].equalsIgnoreCase("1") ? true : false;
		String lat = deviceData[5].substring(0, deviceData[5].length() - 2);
		String lng = deviceData[7].substring(0, deviceData[7].length() - 2);
		this.latitude = Double.valueOf(Float.parseFloat(lat) / 100).intValue()
				+ (Float.parseFloat(lat) - Double.valueOf(
						Float.parseFloat(lat) / 100).intValue() * 100) / 60;
		this.longitude = Double.valueOf(Float.parseFloat(lng) / 100).intValue()
				+ (Float.parseFloat(lng) - Double.valueOf(
						Float.parseFloat(lng) / 100).intValue() * 100) / 60;
		if (!deviceData[6].equalsIgnoreCase("N")) {
			this.latitude = -this.latitude;
		}
		if (!deviceData[8].equalsIgnoreCase("E")) {
			this.longitude = -this.longitude;
		}

		float speedf = Float.parseFloat(deviceData[9]);
		this.speed = (Math.round(speedf));
		this.heading = deviceData[10];
		this.cellId = deviceData[11];
		this.gsmSignal = deviceData[12];
		this.satellites = deviceData[13];
		this.batteryLevel = Long.parseLong(deviceData[14]);
		this.acc = deviceData[15].equalsIgnoreCase("1") ? true : false;
		this.diStatus = deviceData[16];
		this.tamperingStatus = deviceData[17];
		this.diStatus = deviceData[18];
		this.ai1 = Float.parseFloat(deviceData[19]) * 10;
		this.ai2 = Float.parseFloat(deviceData[20]) * 10;
		this.deviceId = deviceData[21];
		this.odometer = Long.parseLong(deviceData[22]);
		}
	}
