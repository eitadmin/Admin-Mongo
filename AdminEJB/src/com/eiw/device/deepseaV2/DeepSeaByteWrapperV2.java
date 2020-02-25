package com.eiw.device.deepseaV2;

import java.io.DataInputStream;
import java.io.IOException;

import org.jboss.logging.Logger;

import com.eiw.device.deepseaTest.ModbusDataTest;
import com.eiw.device.handler.DeepSeaDeviceHandlerTest;
import com.eiw.device.handler.DeepseaDeviceHandlerV2;

public class DeepSeaByteWrapperV2 {

	private DataInputStream dis = null;
	private String imei;
	private String rawData;
	private String[] deviceData;
	private static final Logger LOGGER = Logger.getLogger("listener");
	private ModbusDataV2 modbusData = new ModbusDataV2();

	public DeepSeaByteWrapperV2(DataInputStream in) {
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

	public DataInputStream getDis() {
		return dis;
	}

	public void setDis(DataInputStream dis) {
		this.dis = dis;
	}

	public ModbusDataV2 getModbusData() {
		return modbusData;
	}

	public void setModbusData(ModbusDataV2 modbusData) {
		this.modbusData = modbusData;
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
				DeepseaDeviceHandlerV2.commandStatus = deviceData[2];
				return;
			} else if (deviceData[0].equalsIgnoreCase("<Invalid Request")) {
				DeepseaDeviceHandlerV2.commandStatus = "Invalid Request";
				return;
			} else if (deviceData[3].equalsIgnoreCase("RQ")) {
				this.imei = this.deviceData[5];
				DeepseaDeviceHandlerV2.commandStatus = "RQ";
				return;
			}

			if (deviceData[0].equalsIgnoreCase("<3")) {
				this.imei = this.deviceData[5];
				this.modbusData.read(this.deviceData);
			} else if (deviceData[0].contains("CONFIG")) {
				DeepseaDeviceHandlerV2.commandStatus = "CONFIG";
				this.modbusData.read(this.deviceData);
				this.imei = this.deviceData[1];
			} else if (deviceData[0].contains("<WRITE")) {
				this.imei = this.deviceData[1];

				if (deviceData[4].equalsIgnoreCase("1")) {
					DeepseaDeviceHandlerV2.commandStatus = "success";
				} else {
					DeepseaDeviceHandlerV2.commandStatus = "failure";
				}
			} else {

				this.imei = this.deviceData[2];
				this.modbusData.read(this.rawData.split(":"));
			}

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
