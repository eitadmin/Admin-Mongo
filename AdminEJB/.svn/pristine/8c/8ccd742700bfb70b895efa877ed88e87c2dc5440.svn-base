package com.eiw.device.itrac;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.eiw.server.TimeZoneUtil;

public class ItracProtocolDecoder1 {

	private static final int MESSAGE_LENGTH = 32;

	public Position decode(DataInputStream dis) throws Exception {

		String marker = Character.toString((char) dis.read());

		while (!marker.equals("*") && !marker.equals("$")
				&& dis.available() > 0) {
			// dis.skipBytes(1);
			// if (dis.available() > 0) {
			marker = Character.toString(dis.readChar());
			// }
		}

		if (marker.equals("*")) {

			// Return text message
			int length = dis.available();
			return decodeText("*" + find(dis, length, "#"));

		} else if (marker.equals("$")) {

			// Return binary message
			if (dis.available() >= MESSAGE_LENGTH - 1) {
				return decodeBinary(dis);
			}

		}
		return null;
	}

	private double readCoordinate(DataInputStream dis, boolean lon)
			throws IOException {

		int degrees = readHexInteger(dis, 2);
		int temp = degrees;
		if (lon) {
			// changed getUnsignedByte(dis.readerIndex())
			degrees = degrees * 10 + (temp >> 4);
		}

		double result = 0;
		if (lon) {
			result = dis.readUnsignedByte() & 0x0f;
		}
		result = result * 10 + readHexInteger(dis, lon ? 5 : 6) * 0.0001;

		result /= 60;
		result += degrees;

		return result;
	}

	private Position decodeBinary(DataInputStream dis) throws IOException {

		// Create new position
		Position position = new Position();
		// ExtendedInfoFormatter extendedInfo = new ExtendedInfoFormatter(
		// getProtocol());

		// buf.readByte(); // marker($)

		// Identification
		String id = readHexString(dis, 10);
		try {
			// position.setDeviceId(getDataManager().getDeviceByImei(id).getId());
			position.setDeviceId(Long.valueOf(id));
		} catch (Exception error) {
			System.out.println("Unknown device - " + id);
			return null;
		}

		// Time
		Calendar time = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		time.clear();
		time.set(Calendar.HOUR_OF_DAY, readHexInteger(dis, 2));
		time.set(Calendar.MINUTE, readHexInteger(dis, 2));
		time.set(Calendar.SECOND, readHexInteger(dis, 2));
		time.set(Calendar.DAY_OF_MONTH, readHexInteger(dis, 2));
		time.set(Calendar.MONTH, readHexInteger(dis, 2) - 1);
		time.set(Calendar.YEAR, 2000 + readHexInteger(dis, 2));
		position.setTime(time.getTime());

		// Location
		double latitude = readCoordinate(dis, false);
		int x = dis.readByte(); // reserved
		double longitude = readCoordinate(dis, true);
		int flags = dis.readUnsignedByte() & 0x0f;
		position.setValid((flags & 0x02) != 0);
		if ((flags & 0x04) == 0)
			latitude = -latitude;
		if ((flags & 0x08) == 0)
			longitude = -longitude;
		position.setLatitude(latitude);
		position.setLongitude(longitude);
		position.setAltitude(0.0);

		// Speed and course
		position.setSpeed((double) readHexInteger(dis, 3));
		position.setCourse((dis.readUnsignedByte() & 0x0f) * 100.0
				+ readHexInteger(dis, 2));

		// Status
		// extendedInfo.set("status", ChannelBufferTools.readHexString(buf, 8));
		position.setStatus(readHexString(dis, 8));

		// position.setExtendedInfo(extendedInfo.toString());
		String firstByte = hexToBin(position.getStatus().substring(0, 2));
		String secondByte = hexToBin(position.getStatus().substring(2, 4));
		String thirdByte = hexToBin(position.getStatus().substring(4, 6));
		String fourthByte = hexToBin(position.getStatus().substring(6, 8));

		System.out.println("Sentence : " + id + "," + time.getTime() + ","
				+ latitude + "," + longitude + "," + position.getSpeed() + ","
				+ position.getCourse() + "," + position.getStatus());
		position.setAcc(Integer.valueOf(thirdByte.substring(5, 6)));

		return position;
	}

	private static final Pattern pattern = Pattern.compile("\\*..," + // Manufacturer
			"(\\d+)," + // IMEI
			"V\\d," + // Version?
			".*" + "(\\d{2})(\\d{2})(\\d{2})," + // Time (HHMMSS)
			"([AV])," + // Validity
			"(\\d+)(\\d{2}.\\d+)," + // Latitude (DDMM.MMMM)
			"([NS])," + "(\\d+)(\\d{2}.\\d+)," + // Longitude (DDMM.MMMM)
			"([EW])," + "(\\d+.?\\d*)," + // Speed
			"(\\d+.?\\d*)?," + // Course
			"(\\d{2})(\\d{2})(\\d{2})," + // Date (DDMMYY)
			"(\\p{XDigit}{8})" + // Status
			".*");

	private Position decodeText(String sentence) {
		System.out.println("Sentence : " + sentence);
		// Parse message;
		Matcher parser = pattern.matcher(sentence);
		if (!parser.matches()) {
			return null;
		}

		// Create new position
		Position position = new Position();
		// ExtendedInfoFormatter extendedInfo = new ExtendedInfoFormatter(
		// getProtocol());

		Integer index = 1;

		// Get device by IMEI
		String imei = parser.group(index++);
		try {
			// position.setDeviceId(getDataManager().getDeviceByImei(imei).getId());
			position.setDeviceId(Long.valueOf(imei));
		} catch (Exception error) {
			System.out.println("Unknown device - " + imei);
			return null;
		}

		// Time
		Calendar time = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		time.clear();
		time.set(Calendar.HOUR_OF_DAY, Integer.valueOf(parser.group(index++)));
		time.set(Calendar.MINUTE, Integer.valueOf(parser.group(index++)));
		time.set(Calendar.SECOND, Integer.valueOf(parser.group(index++)));

		// Validity
		position.setValid(parser.group(index++).compareTo("A") == 0);

		// Latitude
		Double latitude = Double.valueOf(parser.group(index++));
		latitude += Double.valueOf(parser.group(index++)) / 60;
		if (parser.group(index++).compareTo("S") == 0)
			latitude = -latitude;
		position.setLatitude(latitude);

		// Longitude
		Double longitude = Double.valueOf(parser.group(index++));
		longitude += Double.valueOf(parser.group(index++)) / 60;
		if (parser.group(index++).compareTo("W") == 0)
			longitude = -longitude;
		position.setLongitude(longitude);

		// Altitude
		position.setAltitude(0.0);

		// Speed
		position.setSpeed(Double.valueOf(parser.group(index++)));

		// Course
		String course = parser.group(index++);
		if (course != null) {
			position.setCourse(Double.valueOf(course));
		} else {
			position.setCourse(0.0);
		}

		// Date
		time.set(Calendar.DAY_OF_MONTH, Integer.valueOf(parser.group(index++)));
		time.set(Calendar.MONTH, Integer.valueOf(parser.group(index++)) - 1);
		time.set(Calendar.YEAR, 2000 + Integer.valueOf(parser.group(index++)));
		position.setTime(time.getTime());

		// Status
		// extendedInfo.set("status", parser.group(index++));

		position.setStatus(parser.group(index++));

		String firstByte = hexToBin(position.getStatus().substring(0, 2));
		String secondByte = hexToBin(position.getStatus().substring(2, 4));
		String thirdByte = hexToBin(position.getStatus().substring(4, 6));
		String fourthByte = hexToBin(position.getStatus().substring(6, 8));

		position.setAcc(Integer.valueOf(thirdByte.substring(5, 6)));

		return position;
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

	public String find(DataInputStream dis, Integer length, String subString)
			throws IOException {

		boolean match = true;
		StringBuilder sb = new StringBuilder();

		for (int index = 0; index < length; index++) {

			char c = (char) dis.read();
			sb.append(c);
			if (c == subString.charAt(0)) {
				match = false;
				break;
			}
		}

		if (!match) {
			return sb.toString();
		}

		return null;
	}

	public int readHexInteger(DataInputStream dis, int length)
			throws IOException {

		int result = 0;
		int temp = 0;

		for (int i = 0; i < length / 2; i++) {
			int b = dis.readUnsignedByte();
			temp = b;
			result *= 10;
			result += b >>> 4;
			result *= 10;
			result += b & 0x0f;
		}

		if (length % 2 == 1) {
			// changed getUnsignedByte(dis.readerIndex());
			int b = temp;
			result *= 10;
			result += b >>> 4;
		}

		return result;
	}

	public String readHexString(DataInputStream dis, int length)
			throws IOException {

		StringBuilder result = new StringBuilder();
		Formatter formatter = new Formatter(result);
		int temp = 0;

		for (int i = 0; i < length / 2; i++) {
			int b = dis.readByte();
			temp = b;
			formatter.format("%02x", b);
		}

		if (length % 2 == 1) {
			// changed getUnsignedByte(dis.readerIndex());
			int b = temp;
			formatter.format("%01x", b >>> 4);
		}

		return result.toString();
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

	public static String asciiToHex(String ascii) {
		StringBuilder hex = new StringBuilder();
		for (int i = 0; i < ascii.length(); i++) {
			hex.append(Integer.toHexString(ascii.charAt(i)));
		}
		return hex.toString();
	}

	public static String getCutOffCommand(String imeiNo) {
		StringBuilder sb = new StringBuilder();
		sb.append("*HQ,");
		sb.append(imeiNo);
		sb.append(",S20,");
		sb.append(TimeZoneUtil.getDateTStr(new Date()).replaceAll(":", ""));
		sb.append(",1,10#");
		return asciiToHex(sb.toString());
	}

	public static String getRestoreCommand(String imeiNo) {
		StringBuilder sb = new StringBuilder();
		sb.append("*HQ,");
		sb.append(imeiNo);
		sb.append(",S20,");
		sb.append(TimeZoneUtil.getDateTStr(new Date()).replaceAll(":", ""));
		sb.append(",0,0#");
		return asciiToHex(sb.toString());
	}
}
