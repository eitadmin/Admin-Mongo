package com.eiw.device.Prime07;



import java.text.SimpleDateFormat;
import java.util.Date;



public class Prime07GpsData {
	private Date dateTime;
	private boolean acc;
	private float latitude;
	private float longitude;
	private boolean internalBattery;
	private int speed;
	private int direction;
	private float distance;
	private float odometer;
	private String status;
	private int ac;
	private boolean	gpsFix;

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

	public void read(String[] deviceData) {

		SimpleDateFormat fromUser = new SimpleDateFormat("ddMMyy HHmmss");
		SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String lat = deviceData[5];
		String lang = deviceData[7];
		String time = deviceData[3];
		this.gpsFix=deviceData[4].equalsIgnoreCase("A")?true:false;
		String Date = deviceData[12];
		this.status=deviceData[13];
		this.odometer=Float.parseFloat(deviceData[14]);
		this.distance=Float.parseFloat(deviceData[15]);
		

		String reformattedStr = "";
		String Datatetime = Date + " " + time;
		try {

			reformattedStr = myFormat.format(fromUser.parse(Datatetime));

			this.speed = (int) Float.parseFloat(deviceData[9]);
			this.direction =  Integer.parseInt(deviceData[10]);
//			this.internalBattery = deviceData.substring(77, 78)
//					.equalsIgnoreCase("1") ? true : false;
			
//					: false;
//			this.ac = Integer.parseInt(deviceData.substring(79, 80));
			long  courseStatus=hex2decimal(status);
		
			System.out.println("Status:"+status+"HEXtodec"+courseStatus+"HEXtoBinary"+Long.toBinaryString(hex2decimal(status)));
			
			
			this.acc = ((courseStatus & 0x0400) != 0) ? true:false;
			
			this.latitude = (float) decimalToDMS(Double.parseDouble(lat));
			this.longitude = (float) decimalToDMS(Double.parseDouble(lang));
			this.dateTime = myFormat.parse(reformattedStr);

			System.out.println("----------------");
			System.out.println(status);
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

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public float getOdometer() {
		return odometer;
	}

	public void setOdometer(float odometer) {
		this.odometer = odometer;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isGpsFix() {
		return gpsFix;
	}

	public void setGpsFix(boolean gpsFix) {
		this.gpsFix = gpsFix;
	}

	public static long hex2decimal(String s) {
		String digits = "0123456789ABCDEF";
		s = s.toUpperCase();
		long val = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			int d = digits.indexOf(c);
			val = 16 * val + d;
		}
		return val;
	}

}

