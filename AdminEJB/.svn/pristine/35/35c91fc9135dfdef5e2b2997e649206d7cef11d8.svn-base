package com.eiw.device.concox;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;

import org.jboss.logging.Logger;

import com.eiw.device.handler.ConcoxDeviceHandler;
import com.eiw.device.ruptela.LoggingInputStream;

public class ConcoxByteWrapper {

	private static final int MSG_LOGIN = 0x01;
	private static final int MSG_GPS = 0x10;
	private static final int MSG_LBS = 0x11;
	public static final int MSG_GPS_LBS_1 = 0x12;
	public static final int MSG_GPS_LBS_2 = 0x22;
	public static final int MSG_STATUS = 0x13;
	private static final int MSG_SATELLITE = 0x14;
	public static final int MSG_STRING = 0x15;
	private static final int MSG_STRING2 = 0x21;
	public static final int MSG_GPS_LBS_STATUS_1 = 0x16;
	public static final int MSG_GPS_LBS_STATUS_2 = 0x26;
	public static final int MSG_GPS_LBS_STATUS_3 = 0x27;
	private static final int MSG_LBS_PHONE = 0x17;
	private static final int MSG_LBS_EXTEND = 0x18;
	private static final int MSG_LBS_STATUS = 0x19;
	private static final int MSG_GPS_PHONE = 0x1A;
	private static final int MSG_GPS_LBS_EXTEND = 0x1E;
	private static final int MSG_COMMAND_0 = 0x80;
	private static final int MSG_COMMAND_1 = 0x81;
	private static final int MSG_COMMAND_2 = 0x82;
	private static final int MSG_ADT = 0x94;
	public static final int MSG_HEARTBEAT = 0x23;
	private static final Logger LOGGER = Logger.getLogger("listener");
	private ConcoxGpsData concoxGpsData = new ConcoxGpsData();
	private int length;
	private byte command;
	private String imei;
	private int serialNo;
	private int crc;
	private int stopBit;
	public byte[] heartbeatPacket;
	private byte[] responseForCoommand;
	private String responseForCoommandStr;

	public ConcoxGpsData getConcoxGpsData() {
		return concoxGpsData;
	}

	public void setConcoxGpsData(ConcoxGpsData concoxGpsData) {
		this.concoxGpsData = concoxGpsData;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getCommand() {
		return command;
	}

	public void setCommand(byte command) {
		this.command = command;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public int getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}

	public int getCrc() {
		return crc;
	}

	public void setCrc(int crc) {
		this.crc = crc;
	}

	public int getStopBit() {
		return stopBit;
	}

	public void setStopBit(int stopBit) {
		this.stopBit = stopBit;
	}

	public int getCRC_ITU(byte[] buf) {
		int[] table = { 0x0000, 0x1189, 0x2312, 0x329B, 0x4624, 0x57AD, 0x6536,
				0x74BF, 0x8C48, 0x9DC1, 0xAF5A, 0xBED3, 0xCA6C, 0xDBE5, 0xE97E,
				0xF8F7, 0x1081, 0x0108, 0x3393, 0x221A, 0x56A5, 0x472C, 0x75B7,
				0x643E, 0x9CC9, 0x8D40, 0xBFDB, 0xAE52, 0xDAED, 0xCB64, 0xF9FF,
				0xE876, 0x2102, 0x308B, 0x0210, 0x1399, 0x6726, 0x76AF, 0x4434,
				0x55BD, 0xAD4A, 0xBCC3, 0x8E58, 0x9FD1, 0xEB6E, 0xFAE7, 0xC87C,
				0xD9F5, 0x3183, 0x200A, 0x1291, 0x0318, 0x77A7, 0x662E, 0x54B5,
				0x453C, 0xBDCB, 0xAC42, 0x9ED9, 0x8F50, 0xFBEF, 0xEA66, 0xD8FD,
				0xC974, 0x4204, 0x538D, 0x6116, 0x709F, 0x0420, 0x15A9, 0x2732,
				0x36BB, 0xCE4C, 0xDFC5, 0xED5E, 0xFCD7, 0x8868, 0x99E1, 0xAB7A,
				0xBAF3, 0x5285, 0x430C, 0x7197, 0x601E, 0x14A1, 0x0528, 0x37B3,
				0x263A, 0xDECD, 0xCF44, 0xFDDF, 0xEC56, 0x98E9, 0x8960, 0xBBFB,
				0xAA72, 0x6306, 0x728F, 0x4014, 0x519D, 0x2522, 0x34AB, 0x0630,
				0x17B9, 0xEF4E, 0xFEC7, 0xCC5C, 0xDDD5, 0xA96A, 0xB8E3, 0x8A78,
				0x9BF1, 0x7387, 0x620E, 0x5095, 0x411C, 0x35A3, 0x242A, 0x16B1,
				0x0738, 0xFFCF, 0xEE46, 0xDCDD, 0xCD54, 0xB9EB, 0xA862, 0x9AF9,
				0x8B70, 0x8408, 0x9581, 0xA71A, 0xB693, 0xC22C, 0xD3A5, 0xE13E,
				0xF0B7, 0x0840, 0x19C9, 0x2B52, 0x3ADB, 0x4E64, 0x5FED, 0x6D76,
				0x7CFF, 0x9489, 0x8500, 0xB79B, 0xA612, 0xD2AD, 0xC324, 0xF1BF,
				0xE036, 0x18C1, 0x0948, 0x3BD3, 0x2A5A, 0x5EE5, 0x4F6C, 0x7DF7,
				0x6C7E, 0xA50A, 0xB483, 0x8618, 0x9791, 0xE32E, 0xF2A7, 0xC03C,
				0xD1B5, 0x2942, 0x38CB, 0x0A50, 0x1BD9, 0x6F66, 0x7EEF, 0x4C74,
				0x5DFD, 0xB58B, 0xA402, 0x9699, 0x8710, 0xF3AF, 0xE226, 0xD0BD,
				0xC134, 0x39C3, 0x284A, 0x1AD1, 0x0B58, 0x7FE7, 0x6E6E, 0x5CF5,
				0x4D7C, 0xC60C, 0xD785, 0xE51E, 0xF497, 0x8028, 0x91A1, 0xA33A,
				0xB2B3, 0x4A44, 0x5BCD, 0x6956, 0x78DF, 0x0C60, 0x1DE9, 0x2F72,
				0x3EFB, 0xD68D, 0xC704, 0xF59F, 0xE416, 0x90A9, 0x8120, 0xB3BB,
				0xA232, 0x5AC5, 0x4B4C, 0x79D7, 0x685E, 0x1CE1, 0x0D68, 0x3FF3,
				0x2E7A, 0xE70E, 0xF687, 0xC41C, 0xD595, 0xA12A, 0xB0A3, 0x8238,
				0x93B1, 0x6B46, 0x7ACF, 0x4854, 0x59DD, 0x2D62, 0x3CEB, 0x0E70,
				0x1FF9, 0xF78F, 0xE606, 0xD49D, 0xC514, 0xB1AB, 0xA022, 0x92B9,
				0x8330, 0x7BC7, 0x6A4E, 0x58D5, 0x495C, 0x3DE3, 0x2C6A, 0x1EF1,
				0x0F78, };
		int crcX = 0xFFFF;
		int cr1 = 0xFF;

		for (int i = 0; i < buf.length; i++) {
			int j = (crcX ^ buf[i]) & cr1;
			crcX = (crcX >> 8) ^ table[j];
		}

		return crcX ^ 0xFFFF;
	}

	/* This unwraps the InputStream and converts it to hexadecimal values */
	public void unwrapDataFromStream(DataInputStream dis, DataOutputStream dos,
			ConcoxDeviceHandler concoxDeviceHandler) throws IOException {
		ByteArrayOutputStream loggingStream = null;
		try {
			if (dis.available() != -1) {
				if (dis.readByte() != 0x78 || dis.readByte() != 0x78) {
					if (dis.readByte() == 0x79) {
						dis.readByte();
						this.length = dis.readByte();
						if (length == 0) {
							this.length = dis.readByte();
						}
						int cmd = dis.readUnsignedByte();
						if (cmd == MSG_ADT) {
							int type = dis.readByte();
							if ((type == 0) || (type == 5)) {
								this.concoxGpsData.readADT(dis, type);
								this.serialNo = dis.readUnsignedShort();
								this.crc = dis.readUnsignedShort();
								this.stopBit = dis.readShort();
							}
						} else if (cmd == MSG_STRING2) {
							LOGGER.info("*****Response from device for command sent from GT800*****");
							int serverFlagBit = dis.readInt();
							int commandLength = dis.readByte();
							this.responseForCoommand = new byte[length - 10];
							dis.readFully(responseForCoommand);
							this.serialNo = dis.readUnsignedShort();
							this.crc = dis.readUnsignedShort();
							this.stopBit = dis.readShort();
							concoxDeviceHandler.commandStatus = asciiBytesToString(this.responseForCoommand);
						}
					}
					return;
				}
				loggingStream = new ByteArrayOutputStream();
				dis = new DataInputStream(new LoggingInputStream(loggingStream,
						dis));
				if (dis.available() != -1) {
					this.length = dis.readByte();
					this.command = dis.readByte();
					if (this.command == MSG_LOGIN) {
						LOGGER.info("*****Login Packet from device*****");
						this.imei = readImei(dis);
						if (dis.available() > 6) {
							LOGGER.info("Type identifier " + dis.readShort()
									+ "Time Zone " + dis.readShort());
						}
						this.serialNo = dis.readUnsignedShort();
						int correctCRC = getCRC_ITU(loggingStream.toByteArray());
						this.crc = dis.readUnsignedShort();
						this.stopBit = dis.readShort();

						if (correctCRC != this.crc) {
							LOGGER.error("CRC ERROR " + this.crc
									+ " calculatedCRC " + correctCRC);
							return;
						}
						dos.write(getResponseMessage());
						LOGGER.info("Login Response to device : Sent");
					} else if (this.command == MSG_STATUS
							|| this.command == MSG_HEARTBEAT) {
						LOGGER.info("*****Heartbeat Packet from device*****"
								+ this.command);
						heartbeatPacket = new byte[this.length - 1];
						dis.readFully(heartbeatPacket);
						if (this.command == MSG_STATUS) {
							concoxGpsData.getTerminalInfo(heartbeatPacket, 0);
						} else {
							concoxGpsData.getHeartBeatInfo(heartbeatPacket, 0);
						}
						this.stopBit = dis.readShort();
						dos.write(getResponseMessage());
						LOGGER.info("Heartbeat Response to device : Sent");
					} else if (this.command == MSG_GPS_LBS_1
							|| this.command == MSG_GPS_LBS_2
							|| this.command == MSG_GPS_LBS_STATUS_1
							|| this.command == MSG_GPS_LBS_STATUS_2
							|| this.command == MSG_GPS_LBS_STATUS_3
							|| this.command == MSG_GPS_PHONE) {
						LOGGER.info("*****GPS Packet from device*****"
								+ this.command);
						this.concoxGpsData.read(dis, heartbeatPacket,
								this.command);
						if (this.command == MSG_GPS_LBS_2) {
							this.concoxGpsData.setAcc(dis.readByte() != 0);
							LOGGER.info("ACC MSG_GPS_LBS_2 "
									+ this.concoxGpsData.getAcc());
							LOGGER.info("MSG_GPS_LBS_2 Data upload mode "
									+ dis.readByte() + "GPS Real time upload "
									+ dis.readByte());
						}
						this.serialNo = dis.readUnsignedShort();
						int correctCRC = getCRC_ITU(loggingStream.toByteArray());
						this.crc = dis.readUnsignedShort();
						this.stopBit = dis.readShort();

						if (correctCRC != this.crc) {
							LOGGER.error("CRC ERROR " + this.crc
									+ " calculatedCRC " + correctCRC);
							return;
						}
					} else if (this.command == MSG_STRING) {
						LOGGER.info("*****Response from device for command sent*****");
						int commandLength = dis.readByte();
						int serverFlagBit = dis.readInt();
						this.responseForCoommand = new byte[commandLength - 4];
						dis.readFully(responseForCoommand);
						int language = dis.readShort();
						this.serialNo = dis.readUnsignedShort();
						int correctCRC = getCRC_ITU(loggingStream.toByteArray());
						this.crc = dis.readUnsignedShort();
						this.stopBit = dis.readShort();
						this.responseForCoommandStr = asciiBytesToString(this.responseForCoommand);
						LOGGER.error("*****Response from device for command sent*****>>>>>>>"
								+ this.imei
								+ "=======>"
								+ this.responseForCoommandStr);
						concoxDeviceHandler.commandStatus = this.responseForCoommandStr;
					}
				} else {
					return;
				}
			} else {
				return;
			}
		} catch (IOException e) {
			throw new IOException("ConcoxByteWrapper I/O Exce " + e);
		} finally {
			if (loggingStream != null) {
				loggingStream.close();
			}
		}
	}

	/*
	 * This method generates the response message to be sent to the terminal.
	 */
	private byte[] getResponseMessage() {
		byte[] response = new byte[10];
		response[0] = 0x78;
		response[1] = 0x78;
		response[2] = 0x05;
		response[3] = this.command;
		byte[] serialNoArr = toBytes(this.serialNo);
		response[4] = serialNoArr[0];
		response[5] = serialNoArr[1];
		byte[] crcCalculate = new byte[4];
		crcCalculate[0] = response[2];
		crcCalculate[1] = response[3];
		crcCalculate[2] = response[4];
		crcCalculate[3] = response[5];
		byte[] crcArr = toBytes(getCRC_ITU(crcCalculate));
		response[6] = crcArr[0];
		response[7] = crcArr[1];
		response[8] = (byte) 0x0D;
		response[9] = (byte) 0x0A;
		return response;
	}

	/* Converts the string in hexadecimal format to array of bytes */
	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	public String asciiBytesToString(byte[] bytes) {
		if ((bytes == null) || (bytes.length == 0)) {
			return "";
		}

		char[] result = new char[bytes.length];

		for (int i = 0; i < bytes.length; i++) {
			result[i] = (char) bytes[i];
		}

		return new String(result);
	}

	/* Converts hexadecimal string to binary format */
	public String hexToBin(String hex) {
		String bin = "";
		String binFragment = "";
		int iHex;

		for (int i = 0; i < hex.length(); i++) {
			iHex = Integer.parseInt("" + hex.charAt(i), 16);
			binFragment = Integer.toBinaryString(iHex);

			while (binFragment.length() < 4) {
				binFragment = "0" + binFragment;
			}
			bin += binFragment;
		}
		return bin;
	}

	public String hexStringToASCIIString(String hexCode) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < hexCode.length() - 1; i += 2) {
			// grab the hex in pairs
			// convert hex to decimal
			int decimal = Integer.parseInt(hexCode.substring(i, i + 2), 16);
			// convert the decimal to character
			sb.append((char) decimal);
		}
		return sb.toString();
	}

	private byte[] toBytes(int i) {
		byte[] result = new byte[2];

		result[0] = (byte) (i >> 8);
		result[1] = (byte) (i /* >> 0 */);

		return result;
	}

	private String readImei(DataInputStream buf) {
		int b;
		StringBuilder sbImei = new StringBuilder();
		try {
			b = buf.readUnsignedByte();
			sbImei.append(b & 0x0F);
			for (int i = 0; i < 7; i++) {
				b = buf.readUnsignedByte();
				sbImei.append((b & 0xF0) >> 4);
				sbImei.append(b & 0x0F);
			}
		} catch (IOException e) {
			LOGGER.error("ConcxByteWrapper - readImei " + e);
		}
		return sbImei.toString();
	}

	public static String getCutOffCommand() {
		return "787812800C0011427852454C41592C31230000D2020D0A";
	}

	public static String getRestoreCommand() {
		return "787812800C0011428052454C41592C30230000BEC20D0A";
	}

	public static String getBuzzerCommand() {
		return "787811800B0011428046494e442300020000BEC20D0A";
	}

	public static String getCutOffCommandKT_Mini() {
		return "787815800F0001A9584459442c3030303030302300A0DCF10D0A";
	}

	public static String getRestoreCommandKT_Mini() {
		return "78781680100001A958484659442c3030303030302300A0DCF10D0A";
	}
	
	public static String getCutOffCommandTK003() {
		return "787815800F0001A9584459442c3030303030302300A0DCF10D0A";
	}

	public static String getRestoreCommandTK003() {
		return "78781680100001A958484659442c3030303030302300A0DCF10D0A";
	}

	public static String getAngleCommand() {
		return "78781880120001A958233636363623616e676c6523302300A0DCF10D0A";
	}

	public static String getTimeDelayCommand(String sec) {
		String hexSec = String
				.format("%02x", new BigInteger(1, sec.getBytes()));
		return "78781780110001A958233636363623534d5423" + hexSec
				+ "2300A0DCF10D0A";
	}

	public static String getLanguageCommand() {
		return "78782180150001A9582336363636234c616e677561676523322300A0DCF10D0A";
	}

	public static String getAdminCommand(String contactNo) {
		String hexstringContactno = String.format("%010x", new BigInteger(1,
				contactNo.getBytes()));
		return "787821801B0001A958233636363623414144233123"
				+ hexstringContactno + "2300A0DCF10D0A";
	}

	public static String getSleepCommand() {
		return "78781580110001A9582336363636235353542336302300A0DCF10D0A";
	}

	public static String getResetCommand() {
		return "78781680100001A95823363636362352657365742300A0DCF10D0A";
	}

	public static String crcCalculation(byte[] pData, int nLength) {
		String output = null;
		short crctab16[] = { (short) 0x0000, (short) 0x1189, (short) 0x2312,
				(short) 0x329b, (short) 0x4624, (short) 0x57ad, (short) 0x6536,
				(short) 0x74bf, (short) 0x8c48, (short) 0x9dc1, (short) 0xaf5a,
				(short) 0xbed3, (short) 0xca6c, (short) 0xdbe5, (short) 0xe97e,
				(short) 0xf8f7, (short) 0x1081, (short) 0x0108, (short) 0x3393,
				(short) 0x221a, (short) 0x56a5, (short) 0x472c, (short) 0x75b7,
				(short) 0x643e, (short) 0x9cc9, (short) 0x8d40, (short) 0xbfdb,
				(short) 0xae52, (short) 0xdaed, (short) 0xcb64, (short) 0xf9ff,
				(short) 0xe876, (short) 0x2102, (short) 0x308b, (short) 0x0210,
				(short) 0x1399, (short) 0x6726, (short) 0x76af, (short) 0x4434,
				(short) 0x55bd, (short) 0xad4a, (short) 0xbcc3, (short) 0x8e58,
				(short) 0x9fd1, (short) 0xeb6e, (short) 0xfae7, (short) 0xc87c,
				(short) 0xd9f5, (short) 0x3183, (short) 0x200a, (short) 0x1291,
				(short) 0x0318, (short) 0x77a7, (short) 0x662e, (short) 0x54b5,
				(short) 0x453c, (short) 0xbdcb, (short) 0xac42, (short) 0x9ed9,
				(short) 0x8f50, (short) 0xfbef, (short) 0xea66, (short) 0xd8fd,
				(short) 0xc974, (short) 0x4204, (short) 0x538d, (short) 0x6116,
				(short) 0x709f, (short) 0x0420, (short) 0x15a9, (short) 0x2732,
				(short) 0x36bb, (short) 0xce4c, (short) 0xdfc5, (short) 0xed5e,
				(short) 0xfcd7, (short) 0x8868, (short) 0x99e1, (short) 0xab7a,
				(short) 0xbaf3, (short) 0x5285, (short) 0x430c, (short) 0x7197,
				(short) 0x601e, (short) 0x14a1, (short) 0x0528, (short) 0x37b3,
				(short) 0x263a, (short) 0xdecd, (short) 0xcf44, (short) 0xfddf,
				(short) 0xec56, (short) 0x98e9, (short) 0x8960, (short) 0xbbfb,
				(short) 0xaa72, (short) 0x6306, (short) 0x728f, (short) 0x4014,
				(short) 0x519d, (short) 0x2522, (short) 0x34ab, (short) 0x0630,
				(short) 0x17b9, (short) 0xef4e, (short) 0xfec7, (short) 0xcc5c,
				(short) 0xddd5, (short) 0xa96a, (short) 0xb8e3, (short) 0x8a78,
				(short) 0x9bf1, (short) 0x7387, (short) 0x620e, (short) 0x5095,
				(short) 0x411c, (short) 0x35a3, (short) 0x242a, (short) 0x16b1,
				(short) 0x0738, (short) 0xffcf, (short) 0xee46, (short) 0xdcdd,
				(short) 0xcd54, (short) 0xb9eb, (short) 0xa862, (short) 0x9af9,
				(short) 0x8b70, (short) 0x8408, (short) 0x9581, (short) 0xa71a,
				(short) 0xb693, (short) 0xc22c, (short) 0xd3a5, (short) 0xe13e,
				(short) 0xf0b7, (short) 0x0840, (short) 0x19c9, (short) 0x2b52,
				(short) 0x3adb, (short) 0x4e64, (short) 0x5fed, (short) 0x6d76,
				(short) 0x7cff, (short) 0x9489, (short) 0x8500, (short) 0xb79b,
				(short) 0xa612, (short) 0xd2ad, (short) 0xc324, (short) 0xf1bf,
				(short) 0xe036, (short) 0x18c1, (short) 0x0948, (short) 0x3bd3,
				(short) 0x2a5a, (short) 0x5ee5, (short) 0x4f6c, (short) 0x7df7,
				(short) 0x6c7e, (short) 0xa50a, (short) 0xb483, (short) 0x8618,
				(short) 0x9791, (short) 0xe32e, (short) 0xf2a7, (short) 0xc03c,
				(short) 0xd1b5, (short) 0x2942, (short) 0x38cb, (short) 0x0a50,
				(short) 0x1bd9, (short) 0x6f66, (short) 0x7eef, (short) 0x4c74,
				(short) 0x5dfd, (short) 0xb58b, (short) 0xa402, (short) 0x9699,
				(short) 0x8710, (short) 0xf3af, (short) 0xe226, (short) 0xd0bd,
				(short) 0xc134, (short) 0x39c3, (short) 0x284a, (short) 0x1ad1,
				(short) 0x0b58, (short) 0x7fe7, (short) 0x6e6e, (short) 0x5cf5,
				(short) 0x4d7c, (short) 0xc60c, (short) 0xd785, (short) 0xe51e,
				(short) 0xf497, (short) 0x8028, (short) 0x91a1, (short) 0xa33a,
				(short) 0xb2b3, (short) 0x4a44, (short) 0x5bcd, (short) 0x6956,
				(short) 0x78df, (short) 0x0c60, (short) 0x1de9, (short) 0x2f72,
				(short) 0x3efb, (short) 0xd68d, (short) 0xc704, (short) 0xf59f,
				(short) 0xe416, (short) 0x90a9, (short) 0x8120, (short) 0xb3bb,
				(short) 0xa232, (short) 0x5ac5, (short) 0x4b4c, (short) 0x79d7,
				(short) 0x685e, (short) 0x1ce1, (short) 0x0d68, (short) 0x3ff3,
				(short) 0x2e7a, (short) 0xe70e, (short) 0xf687, (short) 0xc41c,
				(short) 0xd595, (short) 0xa12a, (short) 0xb0a3, (short) 0x8238,
				(short) 0x93b1, (short) 0x6b46, (short) 0x7acf, (short) 0x4854,
				(short) 0x59dd, (short) 0x2d62, (short) 0x3ceb, (short) 0x0e70,
				(short) 0x1ff9, (short) 0xf78f, (short) 0xe606, (short) 0xd49d,
				(short) 0xc514, (short) 0xb1ab, (short) 0xa022, (short) 0x92b9,
				(short) 0x8330, (short) 0x7bc7, (short) 0x6a4e, (short) 0x58d5,
				(short) 0x495c, (short) 0x3de3, (short) 0x2c6a, (short) 0x1ef1,
				(short) 0x0f78 };
		short fcs = (short) 0xffff; // Initialization
		int i;
		for (i = 0; i < nLength; i++) {
			fcs = ((short) (((fcs & 0xFFFF) >>> 8) ^ crctab16[(fcs ^ pData[i]) & 0xff]));
			short value = (short) ~fcs;
			output = Integer.toHexString(value & 0Xffff);
		}
		return output;

	}

	public String getResponseForCoommandStr() {
		return responseForCoommandStr;
	}

	public void setResponseForCoommandStr(String responseForCoommandStr) {
		this.responseForCoommandStr = responseForCoommandStr;
	}
}
