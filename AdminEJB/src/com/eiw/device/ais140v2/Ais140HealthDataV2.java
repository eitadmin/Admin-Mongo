package com.eiw.device.ais140v2;

public class Ais140HealthDataV2 {
	private String packetType;
	private String vendorId;
	private String fwVersion;
	private int batteryPercent;
	private int lowBatteryThreshold;
	private float memoryPercent1;
	private float memoryPercent2;
	private String ignitionONInterval;
	private String ignitionOFFInterval;
	private int di1;
	private int di2;
	private int di3;
	private int di4;
	private float ai1;
	private float ai2;

	public String getPacketType() {
		return packetType;
	}

	public void setPacketType(String packetType) {
		this.packetType = packetType;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getFwVersion() {
		return fwVersion;
	}

	public void setFwVersion(String fwVersion) {
		this.fwVersion = fwVersion;
	}

	public int getBatteryPercent() {
		return batteryPercent;
	}

	public void setBatteryPercent(int batteryPercent) {
		this.batteryPercent = batteryPercent;
	}

	public int getLowBatteryThreshold() {
		return lowBatteryThreshold;
	}

	public void setLowBatteryThreshold(int lowBatteryThreshold) {
		this.lowBatteryThreshold = lowBatteryThreshold;
	}

	public float getMemoryPercent1() {
		return memoryPercent1;
	}

	public void setMemoryPercent1(float memoryPercent1) {
		this.memoryPercent1 = memoryPercent1;
	}

	public float getMemoryPercent2() {
		return memoryPercent2;
	}

	public void setMemoryPercent2(float memoryPercent2) {
		this.memoryPercent2 = memoryPercent2;
	}

	public String getIgnitionONInterval() {
		return ignitionONInterval;
	}

	public void setIgnitionONInterval(String ignitionONInterval) {
		this.ignitionONInterval = ignitionONInterval;
	}

	public String getIgnitionOFFInterval() {
		return ignitionOFFInterval;
	}

	public void setIgnitionOFFInterval(String ignitionOFFInterval) {
		this.ignitionOFFInterval = ignitionOFFInterval;
	}

	public int getDi1() {
		return di1;
	}

	public void setDi1(int di1) {
		this.di1 = di1;
	}

	public int getDi2() {
		return di2;
	}

	public void setDi2(int di2) {
		this.di2 = di2;
	}

	public int getDi3() {
		return di3;
	}

	public void setDi3(int di3) {
		this.di3 = di3;
	}

	public int getDi4() {
		return di4;
	}

	public void setDi4(int di4) {
		this.di4 = di4;
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

	public void read(String[] deviceData) {
		this.packetType = "HEALTH";
		this.vendorId = deviceData[2];
		this.fwVersion = deviceData[3];
		this.batteryPercent = Integer.parseInt(deviceData[5]);
		this.lowBatteryThreshold = Integer.parseInt(deviceData[6]);
		this.memoryPercent1 = Float.valueOf(deviceData[7]);
		this.ignitionONInterval = deviceData[8];
		this.ignitionOFFInterval = deviceData[9];
		this.ai1 = Float.valueOf(deviceData[12]);
		this.ai2 = Float.valueOf(deviceData[13]);
		System.out.println("PacketType :" + packetType);

	}
}
