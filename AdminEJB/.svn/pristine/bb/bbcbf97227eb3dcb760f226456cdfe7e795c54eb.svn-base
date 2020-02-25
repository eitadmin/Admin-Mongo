package com.eiw.device.ais140v2;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AIS140GpsDataV2 {
	private String messageId;
	private String packetType;
	private String packetStatus;
	private String vehRegNo;
	private String mainPowerSupply;
	private long mainInputVoltage;
	private String emgStatus;
	private boolean d1Status;
	private boolean d2Status;
	private int direction;

	private String header;
	private Date dateTime;
	private boolean gpsFix;
	private double latitude;
	private String latitudeDir;
	private double longitude;
	private String longitudeDir;
	private int speed;
	private String cellId;
	private String gsmSignal;
	private String satellites;
	private double altitude;
	private long batteryLevel;
	private boolean acc;
	private String diStatus;
	private String tamperingStatus;
	private String doStatus;
	private float ai1;
	private float ai2;
	private String deviceId;
	private Long odometer;
	private String gpsValidity;
	private String gpsProvider;

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getPacketType() {
		return packetType;
	}

	public void setPacketType(String packetType) {
		this.packetType = packetType;
	}

	public String getPacketStatus() {
		return packetStatus;
	}

	public void setPacketStatus(String packetStatus) {
		this.packetStatus = packetStatus;
	}

	public String getVehRegNo() {
		return vehRegNo;
	}

	public void setVehRegNo(String vehRegNo) {
		this.vehRegNo = vehRegNo;
	}

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

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getLatitudeDir() {
		return latitudeDir;
	}

	public void setLatitudeDir(String latitudeDir) {
		this.latitudeDir = latitudeDir;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getLongitudeDir() {
		return longitudeDir;
	}

	public void setLongitudeDir(String longitudeDir) {
		this.longitudeDir = longitudeDir;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
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

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
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

	public String getDoStatus() {
		return doStatus;
	}

	public void setDoStatus(String doStatus) {
		this.doStatus = doStatus;
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

	public String getGpsValidity() {
		return gpsValidity;
	}

	public void setGpsValidity(String gpsValidity) {
		this.gpsValidity = gpsValidity;
	}

	public String getMainPowerSupply() {
		return mainPowerSupply;
	}

	public void setMainPowerSupply(String mainPowerSupply) {
		this.mainPowerSupply = mainPowerSupply;
	}

	public long getMainInputVoltage() {
		return mainInputVoltage;
	}

	public void setMainInputVoltage(long mainInputVoltage) {
		this.mainInputVoltage = mainInputVoltage;
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

	public String getGpsProvider() {
		return gpsProvider;
	}

	public void setGpsProvider(String gpsProvider) {
		this.gpsProvider = gpsProvider;
	}

	public void reademr(String[] deviceData) {
		this.packetType = deviceData[2];
		this.packetStatus = deviceData[4];

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(deviceData[7]));
		cal.set(Calendar.MONTH, Integer.parseInt(deviceData[6]) - 1);
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(deviceData[5]));
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(deviceData[8]));
		cal.set(Calendar.MINUTE, Integer.parseInt(deviceData[9]));
		cal.set(Calendar.SECOND, Integer.parseInt(deviceData[10]));
		this.dateTime = cal.getTime();
		System.out.println("DateTime EMR Packet :" + dateTime);
		this.gpsValidity = deviceData[11];
		System.out.println("GPS Validity EMR Packet:" + gpsValidity);
		this.latitude = Double.valueOf(deviceData[12]);
		this.latitudeDir = deviceData[13];
		this.longitude = Float.valueOf(deviceData[14]);
		this.longitudeDir = deviceData[15];
		if (!deviceData[13].equalsIgnoreCase("N")) {
			this.latitude = -this.latitude;
		}
		if (!deviceData[15].equalsIgnoreCase("E")) {
			this.longitude = -this.longitude;
		}
		this.altitude = Float.valueOf(deviceData[16]);
		float speedf = Float.parseFloat(deviceData[17]);
		this.speed = (Math.round(speedf));
		System.out.println("EMR Altitude :" + altitude + " Speed :" + speed);
		this.odometer = Long.parseLong(deviceData[18]);
		this.gpsFix = deviceData[19].equalsIgnoreCase("G") ? true : false;
		this.gpsProvider = deviceData[19];
		this.vehRegNo = deviceData[20];
	}

	public void read(String[] deviceData) {

		this.packetType = deviceData[4];
		this.messageId = deviceData[5];
		this.packetStatus = deviceData[6];
		this.vehRegNo = deviceData[8];
		this.gpsFix = deviceData[9].equalsIgnoreCase("1") ? true : false;

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(deviceData[12]));
		cal.set(Calendar.MONTH, Integer.parseInt(deviceData[11]) - 1);
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(deviceData[10]));
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(deviceData[13]));
		cal.set(Calendar.MINUTE, Integer.parseInt(deviceData[14]));
		cal.set(Calendar.SECOND, Integer.parseInt(deviceData[15]));

		this.dateTime = cal.getTime();
		System.out.println("DateTime Tracking Packet :" + dateTime);

		this.latitude = Float.valueOf(deviceData[16]);
		this.latitudeDir = deviceData[17];
		this.longitude = Float.valueOf(deviceData[18]);
		this.longitudeDir = deviceData[19];
		if (!deviceData[17].equalsIgnoreCase("N")) {
			this.latitude = -this.latitude;
		}
		if (!deviceData[19].equalsIgnoreCase("E")) {
			this.longitude = -this.longitude;
		}

		float speedf = Float.parseFloat(deviceData[20]);
		this.speed = (Math.round(speedf));
		this.direction = (int) Float.parseFloat(deviceData[21]);
		this.satellites = deviceData[22];
		this.altitude = Float.valueOf(deviceData[23]);
		this.acc = deviceData[27].equalsIgnoreCase("1") ? true : false;
		this.mainPowerSupply = deviceData[28];
		float mainIpVolt = Float.parseFloat(deviceData[29]) * 1000;
		this.mainInputVoltage = (long) mainIpVolt;
		System.out.println("Main Input voltage in volts :" + deviceData[29]
				+ " in millvolts" + mainInputVoltage);
		System.out.println("Main Power Supply :" + mainPowerSupply);
		float bat = Float.parseFloat(deviceData[30]) * 1000;
		this.batteryLevel = (long) bat;
		System.out.println("Internal battery voltage in volts :"
				+ deviceData[30] + " in millvolts :" + batteryLevel);
		this.emgStatus = deviceData[31];
		this.tamperingStatus = deviceData[32];
		this.gsmSignal = deviceData[33];
		System.out.println("Satellites :" + satellites + " Gsm Signal :"
				+ gsmSignal + " Emergency Status :" + emgStatus + "Altitude :"
				+ altitude);
		this.cellId = deviceData[37];
		this.diStatus = deviceData[50];
		this.doStatus = deviceData[51];

		System.out.println("Direction :" + direction);
	}

	public Date getDateDDMMYYYYHHMMSS(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
		try {
			Date timeStampStr1 = sdf.parse(str);
			return timeStampStr1;
		} catch (Exception e) {
			return null;
		}

	}
}
