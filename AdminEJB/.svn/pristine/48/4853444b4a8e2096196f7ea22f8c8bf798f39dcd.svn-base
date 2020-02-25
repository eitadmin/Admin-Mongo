package com.eiw.device.Tk103;

import java.io.DataInputStream;
import java.io.IOException;

import org.jboss.logging.Logger;

public class Tk103ByteWrapper {
	private DataInputStream dis = null;
	private String imei;
	private String rawData;
	private String deviceData;
	private static final Logger LOGGER = Logger.getLogger("listener");
	private Tk103GpsData Tk103GpsData = new Tk103GpsData();

	public Tk103ByteWrapper(DataInputStream in) {
		this.dis = in;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public Tk103GpsData getTk103GpsData() {
		return Tk103GpsData;
	}

	public void setTk103GpsData(Tk103GpsData Tk103GpsData) {
		this.Tk103GpsData = Tk103GpsData;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public String getDeviceData() {
		return deviceData;
	}

	public void setDeviceData(String deviceData) {
		this.deviceData = deviceData;
	}

	public void unwrapDataFromStream() throws IOException {
		try {
			LOGGER.info("Entered wrapper");
			StringBuilder sb = new StringBuilder();
			while (true) {
				// String.format("%02x", this.dis.readByte());
				sb.append((char) this.dis.readByte());
				if (sb.toString().contains(")")) {
					int count = this.dis.available();
					LOGGER.info("Available = " + count);
					byte[] junk = new byte[count];
					this.dis.readFully(junk);
					break;
				}
			}
			this.rawData = sb.toString();
			LOGGER.info("Tk03 Data = " + this.rawData);
			this.deviceData = this.rawData;
			this.imei = "8696"+this.deviceData.substring(2, 13);
			this.Tk103GpsData.read(this.deviceData);
		} catch (IOException e) {
			LOGGER.error("Tk103ByteWrapper : " + e);
			throw new IOException("Tk103ByteWrapper");
		}
	}
}
