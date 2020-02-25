package com.eiw.device.Ais140;

import java.io.DataInputStream;
import java.io.IOException;

import org.jboss.logging.Logger;

public class AIS140ByteWrapper {
	private DataInputStream dis = null;
	private String imei;
	private String rawData;
	private String[] deviceData;
	private static final Logger LOGGER = Logger.getLogger("listener");
	private AIS140GpsData Ais140GpsData = new AIS140GpsData();
	private int length;

	public AIS140ByteWrapper(DataInputStream in) {
		this.dis = in;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public AIS140GpsData getAis140GpsData() {
		return Ais140GpsData;
	}

	public void setAis140GpsData(AIS140GpsData Ais140GpsData) {
		this.Ais140GpsData = Ais140GpsData;
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

	public void unwrapDataFromStream(String port
			) throws IOException {
		try {
			LOGGER.info("Entered wrapper");
			StringBuilder sb = new StringBuilder();
			while (true) {
				// String.format("%02x", this.dis.readByte());
				sb.append((char) this.dis.readByte());
				if (sb.toString().contains("*")) {
					int count = this.dis.available();
					LOGGER.info("Available = " + count);
					byte[] junk = new byte[count];
					this.dis.readFully(junk);
					break;
				}
			}
			this.rawData = sb.toString();
			LOGGER.error("Ais140 Data "+port+" :=" + this.rawData);
			this.deviceData = this.rawData.split(",");
			this.length=this.deviceData.length;
			if (this.length > 50) {
				this.imei = this.deviceData[6];
				LOGGER.error("------------ Ais140 basic Data ------------ " );
				this.Ais140GpsData.read(this.deviceData);
			} else if(deviceData[2].equalsIgnoreCase("EMR")){
				this.imei = this.deviceData[3];
				LOGGER.error("-----------Ais140 basic EMR Data  -------- " );
				this.Ais140GpsData.reademr(this.deviceData);
			}
			else {
				this.imei = this.deviceData[3];
				LOGGER.error("-----------Ais140 basic Login data Data  -------- " );
			}

		} catch (IOException e) {
			LOGGER.error("Ais140ByteWrapper : " + e);
			e.printStackTrace();
			throw new IOException("Ais140ByteWrapper");
		}
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
}
