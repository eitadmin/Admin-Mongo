package com.eiw.device.ivetel;

import java.io.DataInputStream;
import java.io.IOException;

import org.jboss.logging.Logger;

public class IvetelByteWrapper {
	private DataInputStream dis = null;
	private String imei;
	private String rawData;
	private String[] deviceData;
	private static final Logger LOGGER = Logger.getLogger("listener");
	private IvetelGpsData ivetelGpsData = new IvetelGpsData();

	public IvetelByteWrapper(DataInputStream in) {
		this.dis = in;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public IvetelGpsData getIvetelGpsData() {
		return ivetelGpsData;
	}

	public void setIvetelGpsData(IvetelGpsData ivetelGpsData) {
		this.ivetelGpsData = ivetelGpsData;
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
				if (sb.toString().contains(";")) {
					int count = this.dis.available();
					LOGGER.info("Available = " + count);
					byte[] junk = new byte[count];
					this.dis.readFully(junk);
					break;
				}
			}
			this.rawData = sb.toString();
			LOGGER.info("Ivetel Data = " + this.rawData);
			this.deviceData = this.rawData.split(",");
			this.imei = this.deviceData[1];
			this.ivetelGpsData.read(this.deviceData);
		} catch (IOException e) {
			LOGGER.error("IvetelByteWrapper : " + e);
			throw new IOException("IvetelByteWrapper");
		}
	}
}
