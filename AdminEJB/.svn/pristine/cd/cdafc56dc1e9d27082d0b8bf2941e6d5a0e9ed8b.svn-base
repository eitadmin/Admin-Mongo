package com.eiw.device.concox;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class ConcoxGpsData {
	private Date dateTime;
	private double latitude;
	private double longitude;
	private int gpsInfo;
	private int satellites;
	private int speed;
	private String status;
	private int course;
	private int MCC;
	private int MNC;
	private int LAC;
	private int cellID;
	private String oilElectricity;
	private String gpsTracking;
	private String alarm;
	private String charge;
	private String defense;
	private boolean acc;
	private boolean hbdacc;
	private String alarmStatus;
	private String voltageLevel;
	private int gsmSignal;
	private int gpsSignal;
	private String alarmLanguage;
	private int lbsLength;
	private int fenceId;

	public String getDoorStatus() {
		return doorStatus;
	}

	public void setDoorStatus(String doorStatus) {
		this.doorStatus = doorStatus;
	}

	private int adtValue;
	private String doorStatus;

	public int getAdtValue() {
		return adtValue;
	}

	public void setAdtValue(int adtValue) {
		this.adtValue = adtValue;
	}

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

	public int getGpsInfo() {
		return gpsInfo;
	}

	public void setGpsInfo(int gpsInfo) {
		this.gpsInfo = gpsInfo;
	}

	public int getSatellites() {
		return satellites;
	}

	public void setSatellites(int satellites) {
		this.satellites = satellites;
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

	public int getCourse() {
		return course;
	}

	public void setCourse(int course) {
		this.course = course;
	}

	public int getMCC() {
		return MCC;
	}

	public void setMCC(int mcc) {
		MCC = mcc;
	}

	public int getMNC() {
		return MNC;
	}

	public void setMNC(int mnc) {
		MNC = mnc;
	}

	public int getLAC() {
		return LAC;
	}

	public void setLAC(int lac) {
		LAC = lac;
	}

	public int getCellID() {
		return cellID;
	}

	public void setCellID(int cellID) {
		this.cellID = cellID;
	}

	public String getOilElectricity() {
		return oilElectricity;
	}

	public void setOilElectricity(String oilElectricity) {
		this.oilElectricity = oilElectricity;
	}

	public String getGpsTracking() {
		return gpsTracking;
	}

	public void setGpsTracking(String gpsTracking) {
		this.gpsTracking = gpsTracking;
	}

	public String getAlarm() {
		return alarm;
	}

	public void setAlarm(String alarm) {
		this.alarm = alarm;
	}

	public String getCharge() {
		return charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

	public String getDefense() {
		return defense;
	}

	public void setDefense(String defense) {
		this.defense = defense;
	}

	public boolean getAcc() {
		return acc;
	}

	public void setAcc(boolean acc) {
		this.acc = acc;
	}

	public String getAlarmStatus() {
		return alarmStatus;
	}

	public void setAlarmStatus(String alarmStatus) {
		this.alarmStatus = alarmStatus;
	}

	public String getVoltageLevel() {
		return voltageLevel;
	}

	public void setVoltageLevel(String voltageLevel) {
		this.voltageLevel = voltageLevel;
	}

	public int getGsmSignal() {
		return gsmSignal;
	}

	public void setGsmSignal(int gsmSignal) {
		this.gsmSignal = gsmSignal;
	}

	public int getGpsSignal() {
		return gpsSignal;
	}

	public void setGpsSignal(int gpsSignal) {
		this.gpsSignal = gpsSignal;
	}

	public String getAlarmLanguage() {
		return alarmLanguage;
	}

	public void setAlarmLanguage(String alarmLanguage) {
		this.alarmLanguage = alarmLanguage;
	}

	public int getLbsLength() {
		return lbsLength;
	}

	public void setLbsLength(int lbsLength) {
		this.lbsLength = lbsLength;
	}

	public int getFenceId() {
		return fenceId;
	}

	public void setFenceId(int fenceId) {
		this.fenceId = fenceId;
	}

	public boolean getHbdacc() {
		return hbdacc;
	}

	public void setHbdacc(boolean hbdacc) {
		this.hbdacc = hbdacc;
	}

	public void read(DataInputStream in, byte[] heartbeatMsg, int type)
			throws IOException {
		// To get Timestamp
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2000 + in.readUnsignedByte());
		// In the Gregorian and Julian calendars JANUARY is 0
		cal.set(Calendar.MONTH, in.readUnsignedByte() - 1);
		cal.set(Calendar.DAY_OF_MONTH, in.readUnsignedByte());
		cal.set(Calendar.HOUR_OF_DAY, in.readUnsignedByte());
		cal.set(Calendar.MINUTE, in.readUnsignedByte());
		cal.set(Calendar.SECOND, in.readUnsignedByte());
		this.dateTime = cal.getTime();
		System.out.println("DateTime: " + dateTime);

		// GPS Info and Satellites
		this.gpsInfo = in.readUnsignedByte();
		this.satellites = this.gpsInfo & 0xf;
		this.gpsInfo = this.gpsInfo >> 4;
		System.out.println("GPS Info: Length = " + gpsInfo + "Satellites = "
				+ satellites);

		// To get Latitude
		this.latitude = in.readInt() / (60.0 * 30000.0);
		System.out.println("Latitude: " + latitude);

		// To get Longitude
		this.longitude = in.readInt() / (60.0 * 30000.0);
		System.out.println("Longitude: " + longitude);

		// To get Speed
		this.speed = in.readUnsignedByte();
		System.out.println("Speed: " + speed);

		// To get course status
		int courseStatus = in.readUnsignedShort();
		this.status = Integer.toBinaryString(courseStatus);
		this.gpsSignal = courseStatus & 0x1000;
		this.course = courseStatus & 0x03FF;
		System.out.println("Course Status: " + status + " " + course);

		if ((courseStatus & 0x0800) != 0) {
			this.longitude = -longitude;
			System.out.println("Longitude Parsed" + longitude);
		}

		if ((courseStatus & 0x0400) == 0) {
			this.latitude = -latitude;
			System.out.println("latitude Parsed" + latitude);
		}

		// ACC
		if ((courseStatus & 0x4000) != 0) {
			this.acc = (courseStatus & 0x8000) != 0;
			System.out.println("ACC : " + acc);
		} else if (heartbeatMsg != null) {
			getTerminalInfo(heartbeatMsg, courseStatus);
		}

		if ((type == ConcoxByteWrapper.MSG_GPS_LBS_STATUS_1)
				|| (type == ConcoxByteWrapper.MSG_GPS_LBS_STATUS_2)
				|| (type == ConcoxByteWrapper.MSG_GPS_LBS_STATUS_3)) {
			this.lbsLength = in.readByte();
			System.out.println("LBS Length :" + lbsLength);
		}

		// MCC, MNC and LAC
		this.MCC = in.readShort();
		this.MNC = in.readByte();
		this.LAC = in.readShort();
		System.out.println("MCC " + MCC + " MNC " + MNC + " LAC " + LAC);

		// cell ID
		byte[] b = new byte[3];
		this.cellID = in.read(b);
		System.out.println("Cell ID: " + cellID);

		if ((type == ConcoxByteWrapper.MSG_GPS_LBS_STATUS_1)
				|| (type == ConcoxByteWrapper.MSG_GPS_LBS_STATUS_2)
				|| (type == ConcoxByteWrapper.MSG_GPS_LBS_STATUS_3)) {
			byte[] terminalData = new byte[5];
			in.readFully(terminalData);
			getTerminalInfo(terminalData, courseStatus);
		}

		if (type == ConcoxByteWrapper.MSG_GPS_LBS_STATUS_3) {
			this.fenceId = in.readByte();
			System.out.println("Fence Id:" + fenceId);
		}
	}

	public void getTerminalInfo(byte[] packet, int courseStatus) {
		// To get oil and electricity connection status
		this.oilElectricity = (packet[0] & 0x80) != 0 ? "Disconnected"
				: "Connected";
		System.out.println("OilElectricity Connection : " + oilElectricity);
		// To get GPS tracking status
		this.gpsTracking = (packet[0] & 0x40) != 0 ? "ON" : "OFF";
		System.out.println("GpsTracking Connection : " + gpsTracking);
		// To get alarm value;
		int alarmValue = packet[0] & 0x38;
		if (alarmValue == 0x20) { // 4
			this.alarm = "SOS";
		} else if (alarmValue == 0x18) { // 3
			this.alarm = "LowBattery";
		} else if (alarmValue == 0x10) { // 2
			this.alarm = "PowerCut";
		} else if (alarmValue == 0x08) { // 1
			this.alarm = "Shock";
		} else {
			this.alarm = "Normal";
		}
		System.out.println("Alarm : " + alarm);
		// Charge
		this.charge = (packet[0] & 0x04) != 0 ? "ON" : "OFF";
		System.out.println("Charge : " + charge);
		if ((courseStatus & 0x4000) != 1) {
			this.acc = (packet[0] & 0x02) != 0;
			System.out.println("Terminal ACC : " + this.acc);
		}
		this.hbdacc = (packet[0] & 0x02) != 0;

		// Terminal status
		this.defense = (packet[0] & 0x01) != 0 ? "Activated" : "Deactivated";
		System.out.println("Status : " + status);
		// Voltage level
		int voltage = packet[1];
		if (voltage == 6) {
			this.voltageLevel = "Very High";
		} else if (voltage == 5) {
			this.voltageLevel = "High";
		} else if (voltage == 4) {
			this.voltageLevel = "Medium";
		} else if (voltage == 3) {
			this.voltageLevel = "Low";
		} else if (voltage == 2) {
			this.voltageLevel = "Very Low";
		} else if (voltage == 1) {
			this.voltageLevel = "Extremely Low";
		} else {
			this.voltageLevel = "No Power";
		}
		System.out.println("VoltageLevel : " + voltageLevel);
		// GSM signal
		this.gsmSignal = packet[2];
		System.out.println("GsmSignal : " + gsmSignal);
		this.alarmLanguage = packet[4] == 0x01 ? "Chinese" : "English";
		System.out.println("Alarm Language : " + alarmLanguage);
	}

	public void readADT(DataInputStream dis, int type) {
		// TODO Auto-generated method stub
		try {
			if (type == 0) {
				this.adtValue = dis.readShort();
			} else if (type == 5) {
				int door = dis.readByte();
				int trigger = (door & 0x02) != 0 ? 1 : 0;
				if (trigger == 1) {
					this.doorStatus = (door & 0x01) != 0 ? "1" : "0";
				} else {
					this.doorStatus = (door & 0x01) == 0 ? "1" : "0";
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Heartbeat Packet for AT4
	public void getHeartBeatInfo(byte[] packet, int courseStatus) {
		System.out.println("Entered into heartbeat method AT4");
		this.gpsTracking = (packet[0] & 0x40) != 0 ? "ON" : "OFF";
		System.out.println("GpsTracking Connection : " + gpsTracking);
		// To get alarm value;
		int alarmValue = packet[0] & 0x38;
		if (alarmValue == 0x20) { // 4
			this.alarm = "SOS";
		} else if (alarmValue == 0x18) { // 3
			this.alarm = "LowBattery";
		} else if (alarmValue == 0x10) { // 2
			this.alarm = "PowerCut";
		} else if (alarmValue == 0x08) { // 1
			this.alarm = "Shock";
		} else {
			this.alarm = "Normal";
		}
		System.out.println("Alarm : " + alarm);
		// Charge
		this.charge = (packet[0] & 0x04) != 0 ? "ON" : "OFF";
		System.out.println("Charge : " + charge);
		if ((courseStatus & 0x4000) != 1) {
			this.acc = (packet[0] & 0x02) != 0;
			System.out.println("Terminal ACC : " + this.acc);
		}
		this.hbdacc = (packet[0] & 0x02) != 0;

		int batVoltage = hex2decimal(Integer.toHexString(packet[1])
				+ Integer.toHexString(packet[2]));
		double voltage = batVoltage * 0.01;
		if (voltage >= 4) {
			this.voltageLevel = "Very High";
		} else if (voltage >= 3.5) {
			this.voltageLevel = "High";
		} else if (voltage >= 3) {
			this.voltageLevel = "Medium";
		} else if (voltage >= 2) {
			this.voltageLevel = "Extremely Low";
		} else {
			this.voltageLevel = "No Power";
		}
		System.out.println("VoltageLevel : " + voltageLevel);

		System.out.println("Battery voltage AT4 :" + voltage);
		// GSM signal
		this.gsmSignal = packet[3];
		System.out.println("GsmSignal : " + gsmSignal);
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
