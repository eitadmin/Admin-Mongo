package com.eiw.device.Prime07;

import java.io.DataInputStream;
import java.io.IOException;

import org.jboss.logging.Logger;

public class Prime07ByteWrapper {
	private DataInputStream dis = null;
	private String imei;
	private String rawData;
	private String[] deviceData;
	


	private static final Logger LOGGER = Logger.getLogger("listener");
	private Prime07GpsData prime07GpsData = new Prime07GpsData();

	public Prime07ByteWrapper(DataInputStream in) {
		this.dis = in;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public Prime07GpsData getPrime07GpsData() {
		return prime07GpsData;
	}

	public void setPrime07GpsData(Prime07GpsData prime07GpsData) {
		this.prime07GpsData = prime07GpsData;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}
	public String[] getDeviceData() {
		return deviceData;
	}

	public void setDeviceData(String[] deviceData) {
		this.deviceData = deviceData;
	}

	
	public void unwrapDataFromStream() throws IOException {
		LOGGER.info("Entered wrapper");
		StringBuilder sb = new StringBuilder();
		while (true) {
			// String.format("%02x", this.dis.readByte());
			sb.append((char) this.dis.readByte());
			if (sb.toString().contains("#")) {
				int count = this.dis.available();
				LOGGER.info("Available = " + count);
				byte[] junk = new byte[count];
				this.dis.readFully(junk);
				break;
			}
		}
		this.rawData = sb.toString();
		LOGGER.info("Prime07 Data = " + this.rawData);
		this.deviceData = this.rawData.split(",");
		this.imei = this.deviceData[1];
		this.prime07GpsData.read(this.deviceData);
	}
}
