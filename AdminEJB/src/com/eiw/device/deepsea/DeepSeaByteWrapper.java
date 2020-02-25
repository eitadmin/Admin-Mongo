package com.eiw.device.deepsea;

import java.io.DataInputStream;
import java.io.IOException;

import org.jboss.logging.Logger;

import com.eiw.device.handler.DeepSeaDeviceHandler;

public class DeepSeaByteWrapper {
	private DataInputStream dis = null;
	private String imei;
	private String rawData;
	private String[] deviceData;
	private static final Logger LOGGER = Logger.getLogger("listener");
	private ModbusData modbusData = new ModbusData();

	public DeepSeaByteWrapper(DataInputStream in) {
		this.dis = in;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public ModbusData getmodbusData() {
		return modbusData;
	}

	public void setmodbusData(ModbusData modbusData) {
		this.modbusData = modbusData;
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
			LOGGER.error("DeepSea Data = " + this.rawData); 
			this.deviceData = this.rawData.split(",");
			if (deviceData[0].equalsIgnoreCase("<LOCK")
					|| deviceData[0].equalsIgnoreCase("<UNOCK")
					|| deviceData[0].equalsIgnoreCase("<UNLOCK")) {
				DeepSeaDeviceHandler.commandStatus = deviceData[2];
				return;
			} else if (deviceData[0].equalsIgnoreCase("<Invalid Request")) {
				DeepSeaDeviceHandler.commandStatus = "Invalid Request";
				return;
			}
			this.imei = this.deviceData[1];
			this.modbusData.read(this.deviceData);
		} catch (IOException e) {
			LOGGER.error("DeepSeaByteWrapper : " + e);
			throw new IOException("DeepSeaByteWrapper");
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
