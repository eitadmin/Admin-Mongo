package com.eiw.device.apmkt.ais1401A;

import org.jboss.logging.Logger;

public class APMKT_AIS1401AHealthData {

	private static final Logger LOGGER = Logger.getLogger("listener");

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
	private String voltageLevel;

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

	public String getVoltageLevel() {
		return voltageLevel;
	}

	public void setVoltageLevel(String voltageLevel) {
		this.voltageLevel = voltageLevel;
	}

	public void read(String[] deviceData) {
		try {
			this.packetType = "HEALTH";
			this.vendorId = deviceData[2];
			this.fwVersion = deviceData[3];
			this.batteryPercent = Integer.parseInt(deviceData[5]);
			this.lowBatteryThreshold = (int) Double.parseDouble(deviceData[6]);
			this.memoryPercent1 = Float.valueOf(deviceData[7]);
			this.ignitionONInterval = deviceData[8];
			this.ignitionOFFInterval = deviceData[9];
			String[] di = deviceData[10].split("");
			this.di1 = Integer.parseInt(di[3]);
			this.di2 = Integer.parseInt(di[2]);
			this.di3 = Integer.parseInt(di[1]);
			this.di4 = Integer.parseInt(di[0]);
			this.ai1 = Float.valueOf(deviceData[11]);
			this.ai2 = Float.valueOf(deviceData[12]);
			if (this.batteryPercent >= 90) {
				this.voltageLevel = "Very High";
			} else if (this.batteryPercent >= 75 && this.batteryPercent < 90) {
				this.voltageLevel = "High";
			} else if (this.batteryPercent >= 40 && this.batteryPercent < 75) {
				this.voltageLevel = "Medium";
			} else if (this.batteryPercent >= 20 && this.batteryPercent < 40) {
				this.voltageLevel = "Low";
			} else if (this.batteryPercent >= 10 && this.batteryPercent < 20) {
				this.voltageLevel = "Very Low";
			} else if (this.batteryPercent >= 1 && this.batteryPercent < 10) {
				this.voltageLevel = "Extremely Low";
			} else {
				this.voltageLevel = "No Power";
			}
		} catch (Exception e) {
			this.packetType = "Unknown";
			LOGGER.error("Exception at Parsing..");
		}
	}

}
