package com.eiw.device.Auxus;

import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;

import org.jboss.logging.Logger;

public class AuxusByteWrapper {
	private DataInputStream dis = null;
	private String imei;
	private String rawData;
	private String[] deviceData;
	private static final Logger LOGGER = Logger.getLogger("listener");
	private AuxusGpsData AuxusGpsData = new AuxusGpsData();

	public AuxusByteWrapper(DataInputStream in) {
		this.dis = in;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public AuxusGpsData getAuxusGpsData() {
		return AuxusGpsData;
	}

	public void setAuxusGpsData(AuxusGpsData AuxusGpsData) {
		this.AuxusGpsData = AuxusGpsData;
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
				int count = this.dis.available();
				if (count > 0) {
					sb.append((char) this.dis.readByte());
					if (sb.toString().contains(")")) {
						int count2 = this.dis.available();
						LOGGER.info("Available = " + count2);
						byte[] junk = new byte[count2];
						this.dis.readFully(junk);
						break;
					}
				}
			}
			this.rawData = sb.toString();
			LOGGER.info("Auxus Data = " + this.rawData);
			String data = this.rawData.substring(1, rawData.length() - 1);
			this.deviceData = data.split(",");
			this.imei = this.deviceData[0];
			this.AuxusGpsData.read(this.deviceData);
		} catch (IOException e) {
			LOGGER.error("AuxusByteWrapper : " + e);
			e.printStackTrace();
			throw new IOException("AuxusByteWrapper");
		}
	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		}
		return data;
	}
}
