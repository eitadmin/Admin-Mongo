package com.eiw.device.tzone;

import java.util.Calendar;
import java.util.Date;

public class TzoneAVL09Data {
	private String imei;
	private Date eventTimeStamp;
	private double latitude;
	private double longitude;
	private double speed;
	private double odometer;
	private int ai1;
	private int ai2;
	private int di1;
	private int di2;
	private int di3;
	private int di4;
	private float temp;
	private float battery;
	private String gpsData;
	public static String GPS_ERROR = "Invalid_Location";

	public TzoneAVL09Data(String str) {
		// To split | separated...
		String[] dataArray = str.split("\\|");

		// To get Imei No
		this.imei = dataArray[0].substring(0, 15);

		// To get Timestamp
		String dateTime = dataArray[6];
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR,
				Integer.parseInt(dateTime.substring(0, 4).trim()));
		cal.set(Calendar.MONTH,
				Integer.parseInt(dateTime.substring(4, 6).trim()) - 1);
		cal.set(Calendar.DAY_OF_MONTH,
				Integer.parseInt(dateTime.substring(6, 8).trim()));
		cal.set(Calendar.HOUR_OF_DAY,
				Integer.parseInt(dateTime.substring(8, 10).trim()));
		cal.set(Calendar.MINUTE,
				Integer.parseInt(dateTime.substring(10, 12).trim()));
		cal.set(Calendar.SECOND,
				Integer.parseInt(dateTime.substring(12, 14).trim()));
		this.eventTimeStamp = cal.getTime();
		System.out.println("Date " + eventTimeStamp);

		String[] latArray = dataArray[1].split(",");
		if (latArray.length < 2) {
			this.gpsData = GPS_ERROR;
			return;
		} else {
			this.gpsData = dataArray[1];
		}

		// To get Latitude
		double latInt = Double.parseDouble(latArray[3]) / 100;
		String[] latStr = String.valueOf(latInt).split("\\.");
		double pointsLat = Double.parseDouble(latStr[1]) / 60;
		String[] ptLat = String.valueOf(pointsLat).split("\\.");
		while (ptLat[0].length() < 4) {
			ptLat[0] = "0" + ptLat[0];
		}
		this.latitude = Double.valueOf(latStr[0] + "." + ptLat[0]);
		System.out.println(latitude);

		// To get Longitude
		double longInt = Double.parseDouble(latArray[5]) / 100;
		String[] longStr = String.valueOf(longInt).split("\\.");
		double pointsLong = Double.parseDouble(longStr[1]) / 60;
		String[] ptLong = String.valueOf(pointsLong).split("\\.");
		while (ptLong[0].length() < 4) {
			ptLong[0] = "0" + ptLong[0];
		}
		this.longitude = Double.valueOf(longStr[0] + "." + ptLong[0]);
		System.out.println(longitude);

		// To get Speed - nm/h, multiplied by by 1.852 to convert km/h
		this.speed = Double.parseDouble(latArray[7]) * 1.852;
		System.out.println("Speed " + speed);

		// To get Odometer Value (Mile - Meter)
		String[] odometerStr = dataArray[11].split("\\.");
		this.odometer = Double.parseDouble(odometerStr[0]) * 1000
				+ Double.parseDouble(odometerStr[1]) / 10;
		System.out.println("Odometer " + odometer);

		// To get Analog Inputs (ADC - 8 Bytes)
		this.ai1 = Integer.parseInt(dataArray[8].substring(0, 4)) * 10;
		System.out.println("AI1 " + ai1);
		this.ai2 = Integer.parseInt(dataArray[8].substring(4, 8)) * 10;
		System.out.println("AI2 " + ai2);

		// To get Digital Inputs - contains 8 DIs totally, 4 Negative, 2
		// Positive, 2 reserved. Need to take 4 negative values alone
		this.di1 = Integer.valueOf(dataArray[5].substring(4, 5).trim());
		this.di2 = Integer.valueOf(dataArray[5].substring(5, 6).trim());
		System.out.println("DIs " + di1 + " " + di2);

		// To get Temperature Values - 4 Bytes, Divided by 10
		this.temp = Float.valueOf(dataArray[10]) / 10;
		System.out.println("Temp " + temp);

		// To get Battery Voltage - Check Document, Voltage (8 Bytes) -
		// Format:ABBBIIII
		this.battery = Float.valueOf(dataArray[7].substring(1, 4)) / 100;
		System.out.println("Battery " + battery);
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public Date getEventTimeStamp() {
		return eventTimeStamp;
	}

	public void setEventTimeStamp(Date eventTimeStamp) {
		this.eventTimeStamp = eventTimeStamp;
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

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getOdometer() {
		return odometer;
	}

	public void setOdometer(double odometer) {
		this.odometer = odometer;
	}

	public int getAI1() {
		return ai1;
	}

	public void setAI1(int aI1) {
		ai1 = aI1;
	}

	public int getAI2() {
		return ai2;
	}

	public void setAI2(int aI2) {
		ai2 = aI2;
	}

	public int getDI1() {
		return di1;
	}

	public void setDI1(int dI1) {
		di1 = dI1;
	}

	public int getDI2() {
		return di2;
	}

	public void setDI2(int dI2) {
		di2 = dI2;
	}

	public int getDI3() {
		return di3;
	}

	public void setDI3(int dI3) {
		di3 = dI3;
	}

	public int getDI4() {
		return di4;
	}

	public void setDI4(int dI4) {
		di4 = dI4;
	}

	public float getTemp() {
		return temp;
	}

	public void setTemp(float temp) {
		this.temp = temp;
	}

	public float getBattery() {
		return battery;
	}

	public void setBattery(float battery) {
		this.battery = battery;
	}

	public String getGpsData() {
		return gpsData;
	}

	public void setGpsData(String gpsData) {
		this.gpsData = gpsData;
	}
}
