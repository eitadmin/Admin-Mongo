package com.eiw.device.ais140v1;

import java.io.DataInputStream;
import java.io.IOException;

import org.jboss.logging.Logger;

public class AIS140ByteWrapperV1 {
	private DataInputStream dis = null;
	private String imei;
	private String rawData;
	private String[] deviceData;
	private static final Logger LOGGER = Logger.getLogger("listener");
	private AIS140GpsDataV1 ais140GpsDataV1 = new AIS140GpsDataV1();
	private Ais140HealthDataV1 ais140HealthDataV1 = new Ais140HealthDataV1();
	private int length;

	public AIS140ByteWrapperV1(DataInputStream in) {
		this.dis = in;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public AIS140GpsDataV1 getAis140GpsDataV1() {
		return ais140GpsDataV1;
	}

	public void setAis140GpsDataV1(AIS140GpsDataV1 ais140GpsDataV1) {
		this.ais140GpsDataV1 = ais140GpsDataV1;
	}

	public Ais140HealthDataV1 getAis140HealthDataV1() {
		return ais140HealthDataV1;
	}

	public void setAis140HealthDataV1(Ais140HealthDataV1 ais140HealthDataV1) {
		this.ais140HealthDataV1 = ais140HealthDataV1;
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
			LOGGER.info("Entered Ais140V1 wrapper");
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
			LOGGER.error("Ais140V1 Data = " + this.rawData);
			this.deviceData = this.rawData.split(",");
			this.length = this.deviceData.length;
			if (this.length > 50) {
				this.imei = this.deviceData[6];
				LOGGER.info("------------ Ais140V1 basic Data ------------ ");
				this.ais140GpsDataV1.read(this.deviceData);
			} else if (deviceData[2].equalsIgnoreCase("EMR")) {
				this.imei = this.deviceData[3];
				LOGGER.info("-----------Ais140V1 basic EMR Data  -------- ");
				this.ais140GpsDataV1.reademr(this.deviceData);
			} else if (this.length == 13) {
				this.imei = this.deviceData[3];
				LOGGER.info("------------ Ais140V1 Health Data ------------ ");
				this.ais140HealthDataV1.read(this.deviceData);
			} else {
				this.imei = this.deviceData[3];
				LOGGER.info("-----------Ais140V1 basic Login data Data  -------- ");
			}

		} catch (IOException e) {
			LOGGER.error("Ais140ByteWrapperV1 : " + e);
			e.printStackTrace();
			throw new IOException("Ais140ByteWrapperV1");
		}
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
}
