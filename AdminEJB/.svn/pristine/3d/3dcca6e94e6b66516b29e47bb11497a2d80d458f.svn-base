package com.eiw.device.apmkt.ais1401A;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.jboss.logging.Logger;

import com.eiw.device.handler.APMKT_AIS1401ADeviceHandler;
import com.google.gwt.util.tools.shared.StringUtils;

public class APMKT_AIS1401AByteWrapper {
	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	private String[] deviceData;
	private String rawData;
	private int length;
	private String imei;
	private static final Logger LOGGER = Logger.getLogger("listener");
	private APMKT_AIS1401AGpsData APMKT_AIS1401AGpsData = new APMKT_AIS1401AGpsData();
	private APMKT_AIS1401AHealthData APMKT_AIS1401AHealthData = new APMKT_AIS1401AHealthData();
	private APMKT_AIS1401ADeviceHandler apmkt_AIS1401ADeviceHandler = null;

	public APMKT_AIS1401AByteWrapper(DataInputStream in, DataOutputStream out,
			APMKT_AIS1401ADeviceHandler apmkt_AIS1401ADeviceHandler) {
		this.dis = in;
		this.dos = out;
		this.apmkt_AIS1401ADeviceHandler = apmkt_AIS1401ADeviceHandler;
	}

	public String[] getDeviceData() {
		return deviceData;
	}

	public void setDeviceData(String[] deviceData) {
		this.deviceData = deviceData;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public APMKT_AIS1401AGpsData getAPMKT_AIS1401AGpsData() {
		return APMKT_AIS1401AGpsData;
	}

	public void setAPMKT_AIS1401AGpsData(
			APMKT_AIS1401AGpsData aPMKT_AIS1401AGpsData) {
		APMKT_AIS1401AGpsData = aPMKT_AIS1401AGpsData;
	}

	public APMKT_AIS1401AHealthData getAPMKT_AIS1401AHealthData() {
		return APMKT_AIS1401AHealthData;
	}

	public void setAPMKT_AIS1401AHealthData(
			APMKT_AIS1401AHealthData aPMKT_AIS1401AHealthData) {
		APMKT_AIS1401AHealthData = aPMKT_AIS1401AHealthData;
	}

	public void unwrapDataFromStream() throws IOException {
		LOGGER.info("Entered APMKT_AIS1401A wrapper");

		StringBuilder sb = new StringBuilder();
		while (true) {
			sb.append((char) this.dis.readByte());
			if (sb.toString().contains("*") || sb.toString().contains("\n")) {
				int count = this.dis.available();
				byte[] junk = new byte[count];
				this.dis.readFully(junk);
				break;
			}
		}

		LOGGER.error("APMKT_AIS1401A Data = " + sb.toString());
		this.rawData = sb.toString();
		this.deviceData = this.rawData.split(",");
		this.length = this.deviceData.length;
		try {

			if (this.length <= 1) {
				this.deviceData = this.rawData.split("\\$");
				this.imei = this.deviceData[1];
			}

			else if (APMKT_AIS1401ADeviceHandler.packetType
					.contains(this.deviceData[4])) {
				LOGGER.error("------------ APMKT_AIS1401A GPS Data ------------ ");
				this.imei = this.deviceData[7];
				this.APMKT_AIS1401AGpsData.read(this.deviceData);
			}

			else if (this.deviceData[4].equalsIgnoreCase("NM")) {
				this.imei = this.deviceData[3];
				LOGGER.error("-----------APMKT_AIS1401A EMR Data  -------- ");
				this.APMKT_AIS1401AGpsData.reademr(this.deviceData);
			}

			else if (this.deviceData[1].equalsIgnoreCase("101")) {
				this.imei = this.deviceData[4];
				LOGGER.error("------------ APMKT_AIS1401A Health Data ------------ ");
				this.APMKT_AIS1401AHealthData.read(this.deviceData);
			} else if (this.deviceData[4].equalsIgnoreCase("PC")) {
				if (this.deviceData[5].equalsIgnoreCase("RL1")
						&& this.deviceData[49].equalsIgnoreCase("10")) {
					apmkt_AIS1401ADeviceHandler.commandStatus = "cutOffEngine";
				} else if (this.deviceData[5].equalsIgnoreCase("RL1")
						&& this.deviceData[49].equalsIgnoreCase("00")) {
					apmkt_AIS1401ADeviceHandler.commandStatus = "restoreEngine";
				} else if (this.deviceData[5].equalsIgnoreCase("RL2")
						&& this.deviceData[49].equalsIgnoreCase("01")) {
					apmkt_AIS1401ADeviceHandler.commandStatus = "cutOffAc";
				} else if (this.deviceData[5].equalsIgnoreCase("RL2")
						&& this.deviceData[49].equalsIgnoreCase("00")) {
					apmkt_AIS1401ADeviceHandler.commandStatus = "restoreAc";
				}
			}

			else {

				LOGGER.error("------------ APMKT_AIS1401A Packet Data ------------ ");
				LOGGER.error(this.deviceData.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
