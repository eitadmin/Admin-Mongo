package com.eiw.device.Ais140;

import java.util.Calendar;
import java.util.Date;

public class AIS140GpsData {
	private String statusData;
	private String PowerStatus;
	private String emgStatus;
	private boolean d1Status;
	private boolean d2Status;
	private int direction;

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

	public String getStatusData() {
		return statusData;
	}

	public void setStatusData(String statusData) {
		this.statusData = statusData;
	}

	public String getPowerStatus() {
		return PowerStatus;
	}

	public void setPowerStatus(String powerStatus) {
		PowerStatus = powerStatus;
	}

	public String getEmgStatus() {
		return emgStatus;
	}

	public void setEmgStatus(String emgStatus) {
		this.emgStatus = emgStatus;
	}

	public boolean isD1Status() {
		return d1Status;
	}

	public void setD1Status(boolean d1Status) {
		this.d1Status = d1Status;
	}

	public boolean isD2Status() {
		return d2Status;
	}

	public void setD2Status(boolean d2Status) {
		this.d2Status = d2Status;
	}

	public String getTamperingStatus() {
		return tamperingStatus;
	}

	public void setTamperingStatus(String tamperingStatus) {
		this.tamperingStatus = tamperingStatus;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public void reademr(String[] deviceData) {
		this.header = deviceData[2];
		this.statusData = deviceData[4];

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR,
				Integer.parseInt(deviceData[5].substring(4, 8).trim()));
		cal.set(Calendar.MONTH,
				Integer.parseInt(deviceData[5].substring(2, 4).trim()) - 1);
		cal.set(Calendar.DAY_OF_MONTH,
				Integer.parseInt(deviceData[5].substring(0, 2).trim()));
		cal.set(Calendar.HOUR_OF_DAY,
				Integer.parseInt(deviceData[5].substring(8, 10).trim()));
		cal.set(Calendar.MINUTE,
				Integer.parseInt(deviceData[5].substring(10, 12).trim()));
		cal.set(Calendar.SECOND,
				Integer.parseInt(deviceData[5].substring(12, 14).trim()));
		this.dateTime = cal.getTime();
		this.gpsFix = deviceData[14].equalsIgnoreCase("G") ? true : false;

		this.latitude = Float.valueOf(deviceData[7]);
		this.longitude = Float.valueOf(deviceData[9]);

		float speedf = Float.parseFloat(deviceData[12]);
		this.speed = (Math.round(speedf));

		// this.odometer = Long.parseLong(deviceData[]);

	}

	public void read(String[] deviceData) {

		this.header = deviceData[4];
		this.statusData = deviceData[6];

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR,
				Integer.parseInt(deviceData[9].substring(4, 8).trim()));
		cal.set(Calendar.MONTH,
				Integer.parseInt(deviceData[9].substring(2, 4).trim()) - 1);
		cal.set(Calendar.DAY_OF_MONTH,
				Integer.parseInt(deviceData[9].substring(0, 2).trim()));
		cal.set(Calendar.HOUR_OF_DAY,
				Integer.parseInt(deviceData[9].substring(0, 2).trim()));
		cal.set(Calendar.MINUTE,
				Integer.parseInt(deviceData[10].substring(2, 4).trim()));
		cal.set(Calendar.SECOND,
				Integer.parseInt(deviceData[10].substring(4, 6).trim()));
		this.dateTime = cal.getTime();
		this.gpsFix = deviceData[8].equalsIgnoreCase("1") ? true : false;

		this.latitude = Float.valueOf(deviceData[11]);
		this.longitude = Float.valueOf(deviceData[13]);
		// this.latitude = deviceData[11];
		// String longitude = deviceData[13].substring(0,
		// deviceData[13].length() - 2);
		// this.longitude = Double.valueOf(Float.parseFloat(lat) /
		// 100).intValue()
		// + (Float.parseFloat(lat) - Double.valueOf(
		// Float.parseFloat(lat) / 100).intValue() * 100) / 60;
		// this.longitude = Double.valueOf(Float.parseFloat(lng) /
		// 100).intValue()
		// + (Float.parseFloat(lng) - Double.valueOf(
		// Float.parseFloat(lng) / 100).intValue() * 100) / 60;
		// if (!deviceData[12].equalsIgnoreCase("N")) {
		// this.latitude = -this.latitude;
		// }
		// if (!deviceData[14].equalsIgnoreCase("E")) {
		// this.longitude = -this.longitude;
		// }

		float speedf = Float.parseFloat(deviceData[15]);
		this.speed = (Math.round(speedf));
		this.heading = deviceData[16];
		this.cellId = deviceData[21];
		this.gsmSignal = deviceData[28];
		this.satellites = deviceData[17];
		float bat = Float.parseFloat(deviceData[25]) * 10;
		this.batteryLevel = (long) bat;
		this.acc = deviceData[22].equalsIgnoreCase("1") ? true : false;
		this.diStatus = deviceData[15];
		this.tamperingStatus = deviceData[16];
		this.diStatus = deviceData[17];
		this.ai1 = Float.parseFloat(deviceData[48]) * 10;
		this.ai2 = Float.parseFloat(deviceData[49]) * 10;
		this.deviceId = deviceData[20];
		this.direction = Integer.parseInt(deviceData[16]);
		// this.odometer = Long.parseLong(deviceData[22]);
	}
}
