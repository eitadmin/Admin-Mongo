package com.eiw.device.Tk103;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Tk103GpsData {
	private Date dateTime;
	private boolean acc;
	private float latitude;
	private float longitude;
	private boolean internalBattery;
	private int speed;
	private int direction;
	private int ac;

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
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

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public boolean isInternalBattery() {
		return internalBattery;
	}

	public void setInternalBattery(boolean internalBattery) {
		this.internalBattery = internalBattery;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getAc() {
		return ac;
	}

	public void setAc(int ac) {
		this.ac = ac;
	}

	public void read(String deviceData) {

		SimpleDateFormat fromUser = new SimpleDateFormat("yyMMdd HHmmss");
		SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String imei = deviceData.substring(2, 13);
		String lat = deviceData.substring(39, 48);
		String lang = deviceData.substring(49, 59);
		String time = deviceData.substring(65, 71);
		String Date = deviceData.substring(32, 38);

		String reformattedStr = "";
		String Datatetime = Date + " " + time;
		try {

			reformattedStr = myFormat.format(fromUser.parse(Datatetime));

			this.speed = (int) Float.parseFloat(deviceData.substring(60, 65));
			this.direction = (int) Float.parseFloat(deviceData
					.substring(71, 77));
			this.internalBattery = deviceData.substring(77, 78)
					.equalsIgnoreCase("1") ? true : false;
			this.acc = deviceData.substring(78, 79).equalsIgnoreCase("1") ? true
					: false;
			this.ac = Integer.parseInt(deviceData.substring(79, 80));

			this.speed = 0;
			this.acc = false;
			this.latitude = (float) decimalToDMS(Double.parseDouble(lat));
			this.longitude = (float) decimalToDMS(Double.parseDouble(lang));
			this.dateTime = myFormat.parse(reformattedStr);

			System.out.println("----------------");
			System.out.println("8696" + imei);
			System.out.println(reformattedStr);

			System.out.println(decimalToDMS(Double.parseDouble(lat)));

			System.out.println(decimalToDMS(Double.parseDouble(lang)));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public double decimalToDMS(double value) {

		double degValue = value / 100;
		int degrees = (int) degValue;
		double decMinutesSeconds = ((value - degrees * 100));
		double minuteValue = decMinutesSeconds / 60;

		return degrees + minuteValue;
	}

}
