package com.eiw.device.ivetel;

import java.util.Date;

public class IvetelGpsData {
	private Date dateTime;
	private String gsmSignal;
	private String status;
	private String rfid;
	private int accBit;
	private boolean acc;
	private float latitude;
	private float longitude;
	private int angle;
	private String satellites;
	private float altitude;
	private float internalBattery;
	private int batteryInMv;
	private int speed;
	private float hdop;
	private int adc1;
	private int fuel1;
	private int fuel2;
	private int fuel3;

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getGsmSignal() {
		return gsmSignal;
	}

	public void setGsmSignal(String gsmSignal) {
		this.gsmSignal = gsmSignal;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getAccBit() {
		return accBit;
	}

	public void setAccBit(int accBit) {
		this.accBit = accBit;
	}

	public boolean getAcc() {
		return acc;
	}

	public void setAcc(boolean acc) {
		this.acc = acc;
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

	public int getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}

	public String getSatellites() {
		return satellites;
	}

	public void setSatellites(String satellites) {
		this.satellites = satellites;
	}

	public float getAltitude() {
		return altitude;
	}

	public void setAltitude(float altitude) {
		this.altitude = altitude;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public float getHdop() {
		return hdop;
	}

	public void setHdop(float hdop) {
		this.hdop = hdop;
	}

	public float getInternalBattery() {
		return internalBattery;
	}

	public void setInternalBattery(float internalBattery) {
		this.internalBattery = internalBattery;
	}

	public int getBatteryInMv() {
		return batteryInMv;
	}

	public void setBatteryInMv(int batteryInMv) {
		this.batteryInMv = batteryInMv;
	}

	public int getAdc1() {
		return adc1;
	}

	public void setAdc1(int adc1) {
		this.adc1 = adc1;
	}

	public int getFuel1() {
		return fuel1;
	}

	public void setFuel1(int fuel1) {
		this.fuel1 = fuel1;
	}

	public int getFuel2() {
		return fuel2;
	}

	public void setFuel2(int fuel2) {
		this.fuel2 = fuel2;
	}

	public int getFuel3() {
		return fuel3;
	}

	public void setFuel3(int fuel3) {
		this.fuel3 = fuel3;
	}

	public String getRfid() {
		return rfid;
	}

	public void setRfid(String rfid) {
		this.rfid = rfid;
	}

	public void read(String[] deviceData) {

		// To get Date and Time
		long epochTime = hex2decimal(deviceData[4]);
		this.dateTime = new Date(epochTime * 1000);
		System.out.println("DateTime: " + dateTime);

		// GSM Signal
		this.gsmSignal = deviceData[6];
		System.out.println("GSM Sigal Strength in percentage :" + gsmSignal);

		// Device sends status in hex,convert to decimal to perform bitwise AND
		// operations
		int statusInDec = hex2decimal(deviceData[7]);
		this.status = Integer.toBinaryString(statusInDec);

		// To get ACC
		this.accBit = statusInDec & 0x0001;
		this.acc = (statusInDec & 0x0001) != 0;
		System.out.println("Status in Decimal :" + statusInDec + " Status :"
				+ status);
		System.out.println("ACC Bit :" + accBit + " ACC :" + acc);

		// To get latitude,longitude,altitude
		this.latitude = Float.valueOf(deviceData[9]);
		this.longitude = Float.valueOf(deviceData[10]);
		System.out
				.println("Latitude :" + latitude + " Longitude :" + longitude);

		// To get angle
		if (!deviceData[11].equalsIgnoreCase("")) {
			this.angle = (int) Math.round(Float.parseFloat(deviceData[11]));
			System.out.println("Angle :" + angle);
		}

		// To get satellites,Altitude
		this.satellites = deviceData[12];
		this.altitude = Float.valueOf(deviceData[13]);
		System.out.println("Satellites :" + satellites + " Altitude :"
				+ altitude);

		// To get speed
		this.speed = Integer.parseInt(deviceData[14]);
		System.out.println("Speed :" + speed);

		// To get internal battery in Volts and millivolts
		this.internalBattery = Float.valueOf(deviceData[18]) / 10;
		this.batteryInMv = Integer.parseInt(deviceData[18]) * 100;
		System.out.println("Internal Battery in volts and mv:"
				+ internalBattery + " " + batteryInMv);

		// To get analog Input1
		// this.adc1 = Integer.parseInt(deviceData[19]);
		this.adc1 = hex2decimal(deviceData[19]);
		System.out.println("ADC1 :" + adc1);

		if (!deviceData[21].equalsIgnoreCase("")) {
			this.fuel1 = Integer.parseInt(deviceData[21]);
		}
		if (!deviceData[22].equalsIgnoreCase("")) {
			this.fuel2 = Integer.parseInt(deviceData[22]);
		}
		if (!deviceData[23].equalsIgnoreCase("")) {
			this.fuel3 = Integer.parseInt(deviceData[23]);
		}
		if (!deviceData[25].equalsIgnoreCase("")) {
			this.rfid = deviceData[25];
		}
		System.out.println("Fuel1 :" + fuel1 + " Fuel2 :" + fuel2 + " Fuel3 :"
				+ fuel3 + " deviceData[21] :" + deviceData[21]);

	}

	public int hex2decimal(String s) {
		String digits = "0123456789ABCDEF";
		s = s.toUpperCase();
		int val = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			int d = digits.indexOf(c);
			val = 16 * val + d;
		}
		return val;
	}
}
