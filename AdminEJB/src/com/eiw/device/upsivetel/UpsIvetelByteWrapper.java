package com.eiw.device.upsivetel;

import java.io.DataInputStream;
import java.io.IOException;

import org.jboss.logging.Logger;

public class UpsIvetelByteWrapper {
	private DataInputStream dis = null;
	private String imei;
	private String rawData;
	private String[] deviceData;
	private static final Logger LOGGER = Logger.getLogger("listener");
	private UpsIvetelData upsIvetelData = new UpsIvetelData();

	public UpsIvetelByteWrapper(DataInputStream in) {
		this.dis = in;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
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

	public UpsIvetelData getUpsIvetelData() {
		return upsIvetelData;
	}

	public void setUpsIvetelData(UpsIvetelData upsIvetelData) {
		this.upsIvetelData = upsIvetelData;
	}

	public void unwrapDataFromStream() throws IOException {
		try {
			LOGGER.info("Entered UPS wrapper");
			StringBuilder sb = new StringBuilder();
			while (true) {
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
			LOGGER.info("UPS Data = " + this.rawData);
			this.deviceData = this.rawData.split(",");
			this.imei = this.deviceData[1];
			this.upsIvetelData.read((this.deviceData));
		} catch (IOException e) {
			LOGGER.error("UpsIvetelByteWrapper : " + e);
			throw new IOException("UpsIvetelByteWrapper");
		}
	}
}
