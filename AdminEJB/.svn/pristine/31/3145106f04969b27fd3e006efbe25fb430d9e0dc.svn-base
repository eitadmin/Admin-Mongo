package com.eiw.device.Auxus;

import java.util.Calendar;
import java.util.Date;

public class AuxusGpsData {
	private Date dateTime;
	private float latitude;
	private float longitude;
	private int speed;
	private int direction;
	private int altitude;
	private Long battery;
	private String alarm;

	public Long getBattery() {
		return battery;
	}

	public void setBattery(Long battery) {
		this.battery = battery;
	}

	public String getAlarm() {
		return alarm;
	}

	public void setAlarm(String alarm) {
		this.alarm = alarm;
	}

	public int getAltitude() {
		return altitude;
	}

	public void setAltitude(int altitude) {
		this.altitude = altitude;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
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

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public void read(String[] deviceData) {
		if (deviceData[1].equalsIgnoreCase("ZC07")
				|| deviceData[1].equalsIgnoreCase("BP05")) {
			if (deviceData[3].equalsIgnoreCase("A")) {
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.YEAR, 2000 + Integer.parseInt(deviceData[2]
						.substring(4, 6).trim()));
				cal.set(Calendar.MONTH,
						Integer.parseInt(deviceData[2].substring(2, 4).trim()) - 1);
				cal.set(Calendar.DAY_OF_MONTH,
						Integer.parseInt(deviceData[2].substring(0, 2).trim()));
				cal.set(Calendar.HOUR_OF_DAY,
						Integer.parseInt(deviceData[7].substring(0, 2).trim()));
				cal.set(Calendar.MINUTE,
						Integer.parseInt(deviceData[7].substring(2, 4).trim()));
				cal.set(Calendar.SECOND,
						Integer.parseInt(deviceData[7].substring(4, 6).trim()));
				this.dateTime = cal.getTime();
				String lat = deviceData[4].substring(0,
						deviceData[4].length() - 2);
				String lng = deviceData[5].substring(0,
						deviceData[5].length() - 2);
				this.latitude = Double.valueOf(Float.parseFloat(lat) / 100)
						.intValue()
						+ (Float.parseFloat(lat) - Double.valueOf(
								Float.parseFloat(lat) / 100).intValue() * 100)
						/ 60;
				this.longitude = Double.valueOf(Float.parseFloat(lng) / 100)
						.intValue()
						+ (Float.parseFloat(lng) - Double.valueOf(
								Float.parseFloat(lng) / 100).intValue() * 100)
						/ 60;
				if (!deviceData[4].substring(deviceData[4].length() - 1)
						.equalsIgnoreCase("N")) {
					this.latitude = -this.latitude;
				}
				if (!deviceData[5].substring(deviceData[5].length() - 1)
						.equalsIgnoreCase("E")) {
					this.longitude = -this.longitude;
				}
				this.speed = (int) (Float.valueOf(deviceData[6]) * 1.852);
				this.direction = (int) (Float.valueOf(deviceData[8]) * 1);
				this.altitude = (int) (Float.valueOf(deviceData[9]) * 1);
			} else {
				return;
			}
		} else if (deviceData[1].equalsIgnoreCase("ZC20")) {
			this.battery = Long.valueOf(deviceData[5]) * 10;
		}
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
