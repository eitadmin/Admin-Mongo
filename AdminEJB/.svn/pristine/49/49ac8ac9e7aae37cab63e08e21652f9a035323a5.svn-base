package com.eiw.device.upsivetel;

import java.util.Date;

public class UpsIvetelData {
	private String fwVersion;
	private String deviceModel;
	private String sdkVersion;
	private Date dateTime;
	private String packetId;
	private int signalStrength;
	private String status;
	private float ipVoltage;
	private float ipFaultVolt;
	private float opVoltage;
	private int opCurrent;
	private float ipFrequency;
	private float upsBatVoltage;
	private float temperature;
	private String upsStatus;
	private float deviceBatVoltage;
	

	public String getFwVersion() {
		return fwVersion;
	}

	public void setFwVersion(String fwVersion) {
		this.fwVersion = fwVersion;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getSdkVersion() {
		return sdkVersion;
	}

	public void setSdkVersion(String sdkVersion) {
		this.sdkVersion = sdkVersion;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getPacketId() {
		return packetId;
	}

	public void setPacketId(String packetId) {
		this.packetId = packetId;
	}

	public int getSignalStrength() {
		return signalStrength;
	}

	public void setSignalStrength(int signalStrength) {
		this.signalStrength = signalStrength;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public float getIpVoltage() {
		return ipVoltage;
	}

	public void setIpVoltage(float ipVoltage) {
		this.ipVoltage = ipVoltage;
	}

	public float getIpFaultVolt() {
		return ipFaultVolt;
	}

	public void setIpFaultVolt(float ipFaultVolt) {
		this.ipFaultVolt = ipFaultVolt;
	}

	public float getOpVoltage() {
		return opVoltage;
	}

	public void setOpVoltage(float opVoltage) {
		this.opVoltage = opVoltage;
	}

	public int getOpCurrent() {
		return opCurrent;
	}

	public void setOpCurrent(int opCurrent) {
		this.opCurrent = opCurrent;
	}

	public float getIpFrequency() {
		return ipFrequency;
	}

	public void setIpFrequency(float ipFrequency) {
		this.ipFrequency = ipFrequency;
	}

	public float getUpsBatVoltage() {
		return upsBatVoltage;
	}

	public void setUpsBatVoltage(float upsBatVoltage) {
		this.upsBatVoltage = upsBatVoltage;
	}

	public float getTemperature() {
		return temperature;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	public String getUpsStatus() {
		return upsStatus;
	}

	public void setUpsStatus(String upsStatus) {
		this.upsStatus = upsStatus;
	}

	public float getDeviceBatVoltage() {
		return deviceBatVoltage;
	}

	public void setDeviceBatVoltage(float deviceBatVoltage) {
		this.deviceBatVoltage = deviceBatVoltage;
	}

	public void read(String[] deviceData) {
		
		this.fwVersion=deviceData[2];
		this.deviceModel=deviceData[3];
		this.sdkVersion=deviceData[4];
		System.out.println("Fw Version :"+fwVersion+" Device Model :"+deviceModel+" SDK Ver :"+sdkVersion);
		
		//To get Date and Time
		long epochTime=hex2decimal(deviceData[5]);
		this.dateTime=new Date(epochTime * 1000);
		System.out.println("DateTime: " + dateTime);
		
		this.packetId=deviceData[6];
		this.signalStrength=Integer.parseInt(deviceData[7]);
		System.out.println("Packet Id :"+packetId+" signalStrength :"+signalStrength);
		
		int statusInDec=hex2decimal(deviceData[8]);
		this.status=Integer.toBinaryString(statusInDec);
		System.out.println("Status in Decimal :" + statusInDec + " Status :"
				+ status);
		
		this.ipVoltage=Float.valueOf(deviceData[9]);
		System.out.println("I/P Voltage :"+ipVoltage);
		this.ipFaultVolt=Float.valueOf(deviceData[10]);
		System.out.println("I/P Fault Voltage :"+ipFaultVolt);
		
		this.opVoltage=Float.valueOf(deviceData[11]);
		this.opCurrent=Integer.parseInt(deviceData[12]);
		System.out.println("O/P Voltage :"+opVoltage+"O/P Current in percentage :"+opCurrent);
		
		this.ipFrequency=Float.valueOf(deviceData[13]);
		System.out.println("I/P Frequency :"+ipFrequency);
		
		this.upsBatVoltage=Float.valueOf(deviceData[14]);
		System.out.println("Ups Bat voltage :"+upsBatVoltage);
		
		this.temperature=Float.valueOf(deviceData[15]);
		System.out.println("Temperature :"+temperature);
		
		int upsStatusInDec=hex2decimal(deviceData[16]);
		this.upsStatus=Integer.toBinaryString(upsStatusInDec);
		System.out.println("Ups Status in Decimal :"+upsStatusInDec+"Ups Status :"+upsStatus);
		
		String btVolt[]=deviceData[17].split(";");
		this.deviceBatVoltage=Float.valueOf(btVolt[0]);
		System.out.println("Device Battery volt :"+deviceBatVoltage);
		

	}
	public static int hex2decimal(String s) {
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
