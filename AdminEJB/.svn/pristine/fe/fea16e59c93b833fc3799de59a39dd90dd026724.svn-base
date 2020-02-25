package com.eiw.device.cantrack;

import java.io.DataInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CantrackGpsData {
	private Date dateTime;
	private double latitude;
	private double longitude;
	private int speed;
	private int course;
	private int MCC;
	private int MNC;
	private int LAC;
	private int cellID;
	private int gps;
	private int gsm;
	private boolean acc;
	private String arm;
	private String alarmMode;
	private String oilElectric;
	private String charger;
	private int battryVoltage;

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
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

	public int getCourse() {
		return course;
	}

	public void setCourse(int course) {
		this.course = course;
	}

	public int getMCC() {
		return MCC;
	}

	public void setMCC(int mCC) {
		MCC = mCC;
	}

	public int getMNC() {
		return MNC;
	}

	public void setMNC(int mNC) {
		MNC = mNC;
	}

	public int getLAC() {
		return LAC;
	}

	public void setLAC(int lAC) {
		LAC = lAC;
	}

	public int getCellID() {
		return cellID;
	}

	public void setCellID(int cellID) {
		this.cellID = cellID;
	}

	public int getGps() {
		return gps;
	}

	public void setGps(int gps) {
		this.gps = gps;
	}

	public int getGsm() {
		return gsm;
	}

	public void setGsm(int gsm) {
		this.gsm = gsm;
	}

	public boolean getAcc() {
		return acc;
	}

	public void setAcc(Boolean acc) {
		this.acc = acc;
	}

	public String getArm() {
		return arm;
	}

	public void setArm(String arm) {
		this.arm = arm;
	}

	public String getAlarmMode() {
		return alarmMode;
	}

	public void setAlarmMode(String alarmMode) {
		this.alarmMode = alarmMode;
	}

	public String getOilElectric() {
		return oilElectric;
	}

	public void setOilElectric(String oilElectric) {
		this.oilElectric = oilElectric;
	}

	public String getCharger() {
		return charger;
	}

	public void setCharger(String charger) {
		this.charger = charger;
	}

	public int getBattryVoltage() {
		return battryVoltage;
	}

	public void setBattryVoltage(int battryVoltage) {
		this.battryVoltage = battryVoltage;
	}

	public void read(DataInputStream dis, String type) throws Exception {
		// Get Date And Time
		int dateInSec = dis.readInt();
		this.dateTime = getDateAndTime(dateInSec);

		// Get Latitude And Longitude
		this.latitude = dis.readInt() / (60.0 * 30000.0);
		this.longitude = dis.readInt() / (60.0 * 30000.0);

		// Get Speed
		this.speed = dis.readUnsignedByte();

		// Get Course
		this.course = dis.readShort();

		// Get MCC MNC LAC
		this.MCC = dis.readShort();
		this.MNC = dis.readShort();
		this.LAC = dis.readShort();

		// Get Cell Id
		byte[] b = new byte[3];
		this.cellID = dis.read(b);

		// Get Location Status
		int locationStatus = dis.readByte();
		this.gps = (locationStatus & 1) == 1 ? 1:0;
		this.acc = ((locationStatus >> 1) & 1) == 1 ? true:false;
		/*String statusbits = Integer.toBinaryString(locationStatus);
		String[] bitsArray = statusbits.split("");
		this.gps = Integer.parseInt(bitsArray[0]);*/
		// this.acc =Boolean.parseBoolean(bitsArray[1]);

		// Get AlarmData
		if (type.equalsIgnoreCase("4")) {
			int modeValue = dis.readByte();
			if (modeValue == 1) {
				this.alarmMode = "powerCut";
			} else if (modeValue == 4) {
				this.alarmMode = "vibrationAlarm";
			}

		}
	}

	public void readHbd(DataInputStream dis, String type) throws Exception {

		// Status
		//int status = dis.readShort();
		/*String statusbits = Integer.toBinaryString(status);
		String[] bitsArray = statusbits.split("");
		this.gps = Integer.parseInt(bitsArray[0]);
		if (Integer.parseInt(bitsArray[1]) == 10) {
			this.acc = false;
		} else {
			this.acc = true;
		}
		if (Integer.parseInt(bitsArray[3]) == 10) {
			this.arm = "DisArm";
		} else {
			this.arm = "Arm";
		}
		if (Integer.parseInt(bitsArray[5]) == 10) {
			this.oilElectric = "disconnected";
		} else {
			this.oilElectric = "connected";
		}
		if (Integer.parseInt(bitsArray[6]) == 10) {
			this.charger = "disconnected";
		} else {
			this.charger = "connected";
		}*/
		
		int status = dis.readShort();
		this.gps = (status & 1) == 1 ? 1:0;
		this.acc = ((status >> 1) & 1) == 1 ? true:false;
		this.arm = ((status >> 3) & 1) == 1 ? "Arm":"DisArm"	;
		this.oilElectric = ((status >> 5) & 1) == 1 ? "connected":"disconnected";
		this.charger = ((status >> 7) & 1) == 1 ? "ON":"OFF";
		
			
			// GSM signal
		int gsm = dis.readByte();
		if (gsm == 0x00) {
			this.gsm = 0;
		} else if (gsm == 0x01) {
			this.gsm = 1;
		} else if (gsm == 0x02) {
			this.gsm = 2;
		} else if (gsm == 0x03) {
			this.gsm = 3;
		} else if (gsm == 0x04) {
			this.gsm = 4;
		}
		this.battryVoltage = hex2decimal(Integer.toHexString(dis.readByte()));
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

	public Date getDateAndTime(int sec) throws Exception {
		SimpleDateFormat formate1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long seconds = (long) sec;
		Date date = new Date(seconds * 1000);
		String java_date = formate1.format(date);
		return formate1.parse(java_date);
	}

	public void getTerminalInfo(byte locationStatus, int courseStatus) {

		this.acc = (locationStatus & 0x02) != 0 ? true : false;
		this.gps = (locationStatus & 0x01) != 0 ? 1 : 0;
	}
}
