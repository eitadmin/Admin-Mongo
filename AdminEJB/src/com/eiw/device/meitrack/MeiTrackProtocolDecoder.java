package com.eiw.device.meitrack;

import java.io.DataInputStream;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.logging.Logger;

public class MeiTrackProtocolDecoder {

	static final Logger LOGGER = Logger.getLogger("listener");
	private static final Pattern pattern = Pattern
			.compile("\\$\\$.\\d+,(\\d+),\\p{XDigit}{3},(?:\\d+,)?(\\d+),(-?\\d+\\.\\d+),(-?\\d+\\.\\d+),(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2}),([AV]),(\\d+),(\\d+),(\\d+),(\\d+),(\\d+\\.?\\d*),(-?\\d+),(\\d+),(\\d+),(\\d+\\|\\d+\\|\\p{XDigit}+\\|\\p{XDigit}+),(\\p{XDigit}+),(\\p{XDigit}+)?\\|(\\p{XDigit}+)?\\|(\\p{XDigit}+)?\\|(\\p{XDigit}+)\\|(\\p{XDigit}+),.*(\r\n)?");
	private static final Pattern patternForRfid = Pattern
			.compile("\\$\\$.\\d+,(\\d+),\\p{XDigit}{3},(?:\\d+,)?(\\d+),(-?\\d+\\.\\d+),(-?\\d+\\.\\d+),(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2}),([AV]),(\\d+),(\\d+),(\\d+),(\\d+),(\\d+\\.?\\d*),(-?\\d+),(\\d+),(\\d+),(\\d+\\|\\d+\\|\\p{XDigit}+\\|\\p{XDigit}+),(\\p{XDigit}+),(\\p{XDigit}+)?\\|(\\p{XDigit}+)?\\|(\\p{XDigit}+)?\\|(\\p{XDigit}+)\\|(\\p{XDigit}+),(\\p{XDigit}+),.*(\r\n)?");

	public Position decode(DataInputStream dis) throws Exception {

		StringBuilder sb = new StringBuilder();
		// sb.append((char) dis.read()).append((char) dis.read());
		sb.append(dis.readByte()).append(dis.readByte());

		// if (!sb.toString().equalsIgnoreCase("$$")) {
		if (!sb.toString().equalsIgnoreCase("3636")) {
			LOGGER.info("MeiTrackProtocolDecoder : decode " + sb.toString());
			return null;
		}

		sb.replace(0, sb.toString().length(), "$$");
		// Append data identifier
		sb.append((char) dis.read());
		/*
		 * Find the length of the data packet, bytes after <data identifier till
		 * first ',' represents the length of the data
		 */
		boolean match = true;
		StringBuilder dataLength = new StringBuilder();
		while (match) {
			char c = (char) dis.read();
			if (c == ',') {
				sb.append(dataLength).append(c);
				match = false;
				break;

			} else {
				dataLength.append(c);
			}

		}

		int length = Integer.valueOf(dataLength.toString());
		byte[] byteArray = new byte[length - 1];
		dis.readFully(byteArray);
		sb.append(asciiBytesToString(byteArray));
		return parseMessageReceived(sb.toString());
	}

	private Position parseMessageReceived(String message) {
		Matcher parser = pattern.matcher(message);
		LOGGER.info("MeiTrackProtocolDecoder : parseMessageReceived " + message);
		if (!parser.matches()) {
			LOGGER.error("MeiTrackProtocolDecoder: parseMessageReceived: parser not matches "
					+ message);
		}

		// Create new position
		Position position = new Position();

		Integer index = 1;

		position.setMessage(message);
		// Identification
		String imei = parser.group(index++);
		try {
			// position.setDeviceId(Long.valueOf(imei));
			position.setDeviceId1(imei);
		} catch (Exception error) {
			LOGGER.error("Unknown device - " + imei);
			return null;
		}

		// Event
		position.setEvent(parser.group(index++));

		// Coordinates
		position.setLatitude(Double.valueOf(parser.group(index++)));
		position.setLongitude(Double.valueOf(parser.group(index++)));

		// Time
		Calendar time = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		time.clear();
		time.set(Calendar.YEAR, 2000 + Integer.valueOf(parser.group(index++)));
		time.set(Calendar.MONTH, Integer.valueOf(parser.group(index++)) - 1);
		time.set(Calendar.DAY_OF_MONTH, Integer.valueOf(parser.group(index++)));
		time.set(Calendar.HOUR_OF_DAY, Integer.valueOf(parser.group(index++)));
		time.set(Calendar.MINUTE, Integer.valueOf(parser.group(index++)));
		time.set(Calendar.SECOND, Integer.valueOf(parser.group(index++)));
		position.setTime(time.getTime());

		// Validity
		position.setValid(parser.group(index++).compareTo("A") == 0);

		// Satellites
		position.setSatellites(parser.group(index++));

		// GSM Signal
		position.setGsmSignal(parser.group(index++));

		if (position.getEvent().equalsIgnoreCase("255")
				&& position.getSatellites().equalsIgnoreCase("255")
				&& position.getGsmSignal().equalsIgnoreCase("255")) {
			return null;
		}

		// Speed
		position.setSpeed(Double.valueOf(parser.group(index++))); // * 0.539957

		// Course
		position.setCourse(Double.valueOf(parser.group(index++)));

		// HDOP
		position.setHdop(parser.group(index++));

		// Altitude
		position.setAltitude(Double.valueOf(parser.group(index++)));

		position.setMileage(parser.group(index++));
		position.setRuntime(parser.group(index++));
		position.setCell(parser.group(index++));
		String state = parser.group(index++);
		position.setState(state);

		String dio = hexToBin(state);
		position.setDI3(Integer.parseInt(dio.substring(5, 6)));
		position.setDI2(Integer.parseInt(dio.substring(6, 7)));
		position.setDI1(Integer.parseInt(dio.substring(7, 8)));

		// ADC
		String adc1 = parser.group(index++);
		if (adc1 != null) {
			position.setADC1(Double.valueOf(
					((Integer.parseInt(adc1, 16) * 6.6) / 4096) * 1000)
					.intValue());
		}
		String adc2 = parser.group(index++);
		if (adc2 != null) {
			position.setADC2(Double.valueOf(
					((Integer.parseInt(adc2, 16) * 6.6) / 4096) * 1000)
					.intValue());
		}
		String adc3 = parser.group(index++);
		if (adc3 != null) {
			position.setADC3(Double.valueOf(
					((Integer.parseInt(adc3, 16) * 6.6) / 4096) * 1000)
					.intValue());
		}
		position.setBattery(Integer.parseInt(parser.group(index++), 16));
		position.setPower(Integer.parseInt(parser.group(index++), 16));
		if (position.getEvent().equalsIgnoreCase("37")) {
			parser.usePattern(patternForRfid);
			parser = patternForRfid.matcher(message);
			LOGGER.info("MeiTrackProtocolDecoder: RFID Message " + message);
			if (!parser.matches()) {
				LOGGER.error("MeiTrackProtocolDecoder: parseMessageReceived: parser not matches for RFID Message "
						+ message);
			}
			position.setRfid(String.valueOf(Integer.parseInt(
					parser.group(index++), 16)));
		}
		return position;
	}

	/* Converts hexadecimal string to binary format */
	private String hexToBin(String hex) {
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

	private String asciiBytesToString(byte[] bytes) {
		if ((bytes == null) || (bytes.length == 0)) {
			return "";
		}

		char[] result = new char[bytes.length];

		for (int i = 0; i < bytes.length; i++) {
			result[i] = (char) bytes[i];
		}

		return new String(result);
	}
}
