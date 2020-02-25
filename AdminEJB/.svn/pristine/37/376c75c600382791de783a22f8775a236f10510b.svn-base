package com.eiw.device.Ushetel;

import java.io.DataInputStream;
import java.io.IOException;

import org.jboss.logging.Logger;

public class UshetelByteWrapper {
	private DataInputStream dis = null;
	private String imei;
	private String rawData;
	private String[] deviceData;
	private static final Logger LOGGER = Logger.getLogger("listener");
	private UshetelGpsData UshetelGpsData = new UshetelGpsData();

	public UshetelByteWrapper(DataInputStream in) {
		this.dis = in;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public UshetelGpsData getUshetelGpsData() {
		return UshetelGpsData;
	}

	public void setUshetelGpsData(UshetelGpsData UshetelGpsData) {
		this.UshetelGpsData = UshetelGpsData;
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
		try {
			LOGGER.info("Entered wrapper");
			StringBuilder sb = new StringBuilder();
			while (true) {
				// String.format("%02x", this.dis.readByte());
				sb.append((char) this.dis.readByte());
				if (sb.toString().contains("VER-")) {
					int count = this.dis.available();
					LOGGER.info("Available = " + count);
					byte[] junk = new byte[count];
					this.dis.readFully(junk);
					break;
				}
			}
			this.rawData = sb.toString();
			LOGGER.info("Ushetel Data = " + this.rawData);
			this.deviceData = this.rawData.split(",");
			this.imei = this.deviceData[1];
			this.UshetelGpsData.read(this.deviceData);
		} catch (IOException e) {
			LOGGER.error("UshetelByteWrapper : " + e);
			throw new IOException("UshetelByteWrapper");
		}
	}
}
