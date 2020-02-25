package com.eiw.device.tzone;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class TzoneByteWrapper {
	/**
	 * Unwraps from input stream. input stream can throw exception to prevent
	 * blocking.
	 * 
	 * @param is
	 *            Input stream to read from
	 * @return Returns read buffer
	 * @throws IOException
	 *             Exception from input stream or if crc test fails
	 */
	public static byte[] ERROR = "Invalid_Data".getBytes();
	private static List<byte[]> tzoneData = new ArrayList<byte[]>();

	private TzoneByteWrapper() {
		// private constructor to hide utility class.
	}

	public static List<byte[]> unwrapFromStream(DataInputStream is)
			throws IOException {
		tzoneData.clear();
		int zeroCount = 0;
		tzoneData.add(getAVLData(is));
		while (is.available() != 0) {
			tzoneData.add(getAVLData(is));
			if (zeroCount > 5) {
				break;
			}
			zeroCount++;
		}
		return tzoneData;
	}

	private static byte[] getAVLData(DataInputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();
		int count = 0;
		char[] header = new char[2];
		while (count < 2) {
			header[count] = (char) is.read();
			sb.append(String.format("%02x", (int) header[count]));
			count++;
		}

		count = 0;
		char[] length = new char[2];
		while (count < 2) {
			length[count] = (char) is.read();
			sb.append(String.format("%02x", (int) length[count]));
			count++;
		}
		int dataLength = Integer.parseInt("" + length[0] + length[1], 16);

		if (dataLength > 0xFFFF) {
			// most likely incoming data is invalid - we would not use such big
			// packets?
			throw new IOException("Data packet too large (>0xffff)");
		}

		byte[] data = new byte[dataLength - 4];
		is.readFully(data);

		for (byte b : data) {
			sb.append(String.format("%02x", b));
		}

		String str = sb.toString();
		String correctCRC = getCrc16ModBus(str.substring(0, str.length() - 8));
		String crc = hexToASCII(str.substring(str.length() - 8));

		count = 0;
		char[] endChars = new char[2];
		while (count < 2) {
			endChars[count] = (char) is.read();
			sb.append(String.format("%02x", (int) endChars[count]));
			count++;
		}
		if (!crc.equalsIgnoreCase(correctCRC)) {
			return ERROR;
		}

		return data;
	}

	public static String byteArrayToString(byte[] data) {
		StringBuilder sb = new StringBuilder();
		for (byte b : data) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}

	public static String asciiBytesToString(byte[] bytes) {
		if ((bytes == null) || (bytes.length == 0)) {
			return "";
		}

		char[] result = new char[bytes.length];

		for (int i = 0; i < bytes.length; i++) {
			result[i] = (char) bytes[i];
		}

		return new String(result);
	}

	public static String hexToBin(String hexVal) {
		String bin = "";
		String binFragment = "";
		int iHex;
		String hex = hexVal.trim();
		hex = hex.replaceFirst("0x", "");

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

	public static String hexToASCII(String hex) {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < hex.length(); i += 2) {
			String str = hex.substring(i, i + 2);
			output.append((char) Integer.parseInt(str, 16));
		}
		return output.toString();
	}

	private static String getCrc16ModBus(String buf) {
		int crc = 0xFFFF;

		for (int pos = 0; pos < buf.length(); pos = pos + 2) {
			crc ^= Integer.parseInt(buf.substring(pos, pos + 2), 16);

			for (int i = 8; i != 0; i--) {
				if ((crc & 0x0001) != 0) {
					crc >>= 1;
					crc ^= 0xA001;
				} else {
					crc >>= 1;
				}
			}
		}
		// Note, this number has low and high bytes swapped, so use it
		// accordingly (or swap bytes)
		crc = crc & 0xFFFF;
		String result = String.format("%02x", crc);
		if (result.length() == 3) {
			result = "0" + result;
		} else if (result.length() == 2) {
			result = "00" + result;
		}
		return result;
	}

}