package com.eiw.device.tzone;

import java.io.DataInputStream;
import java.io.IOException;

import org.jboss.logging.Logger;

public class TzoneByteWrapperForSchoolBus {

	private static final Logger LOGGER = Logger.getLogger("listener");
	private DataInputStream dis = null;
	private String imeiNo;

	public String getImeiNo() {
		return imeiNo;
	}

	public void setImeiNo(String imeiNo) {
		this.imeiNo = imeiNo;
	}

	public TzoneByteWrapperForSchoolBus(DataInputStream clientSocketDis) {
		this.dis = clientSocketDis;
	}

	public String read() {
		StringBuilder rawData = new StringBuilder();
		try {
			StringBuilder sb = new StringBuilder();
			byte[] startMark = new byte[2];
			dis.readFully(startMark);
			sb.append(byteArrayToString(startMark));

			byte[] length = new byte[2];
			dis.readFully(length);
			sb.append(byteArrayToString(length));
			int dataLength = Integer.parseInt(sb.substring(4, 8), 16);

			if (dataLength > 0xFFFF) {
				// most likely incoming data is invalid
				throw new IOException("Data packet too large (>0xffff)");
			}

			byte[] data = new byte[dataLength];
			dis.readFully(data);

			sb.append(byteArrayToString(data));

			byte[] endMark = new byte[2];
			dis.readFully(endMark);
			sb.append(byteArrayToString(endMark));

			String str = sb.toString();
			LOGGER.info("TzoneByteWrapperForSchoolBus: Hex Code : " + str);

			/* Framing understandable format for raw data */
			rawData.append(TzoneByteWrapper.asciiBytesToString(startMark));
			rawData.append(dataLength);
			rawData.append("|");
			rawData.append(TzoneByteWrapper.asciiBytesToString(str.substring(8,
					12).getBytes()));
			rawData.append("|");
			rawData.append(hexToDecimal(str.substring(12, 16), ""));
			rawData.append("|");
			rawData.append(Integer.parseInt(str.substring(16, 18)));
			rawData.append(".");
			rawData.append(hexToDecimal(str.substring(18, 24), ""));
			rawData.append("|");
			rawData.append(str.substring(23, 40));
			this.imeiNo = str.substring(25, 40);
			rawData.append("|");
			int currPosition = 40;
			/* Timestamp */
			rawData.append(hexToDecimal(
					str.substring(currPosition, currPosition += 12), ":"));
			rawData.append("|");
			/* GPS Info */
			int gpsLength = Integer.parseInt(
					str.substring(currPosition, currPosition += 4), 16);
			rawData.append(gpsLength);
			if (gpsLength != 0) {
				rawData.append(getGpsInfo(str, currPosition));
				currPosition += gpsLength * 2;
			}
			rawData.append("|");
			/* LBS Info */
			int lbsLength = Integer.parseInt(
					str.substring(currPosition, currPosition += 4), 16);
			rawData.append(lbsLength);
			if (lbsLength != 0) {
				rawData.append(getLbsInfo(str, currPosition));
				currPosition += lbsLength * 2;
			}
			rawData.append("|");
			/* Status Info */
			int statusLength = Integer.valueOf(
					str.substring(currPosition, currPosition += 4), 16);
			rawData.append(statusLength);
			if (statusLength != 0) {
				rawData.append(getStatusInfo(str, currPosition));
				currPosition += statusLength * 2;
			}
			rawData.append("|");

			/* 125K Card Info */
			int cardLength = Integer.parseInt(
					str.substring(currPosition, currPosition += 4), 16);
			rawData.append(cardLength);
			if (cardLength != 0) {
				rawData.append(getCardInfo(
						str.substring(currPosition, currPosition + cardLength
								* 2), cardLength));
				currPosition += cardLength * 2;
			}
			rawData.append("|");
			/* UHF Card Info */
			cardLength = Integer.parseInt(
					str.substring(currPosition, currPosition += 4), 16);
			rawData.append(cardLength);
			if (cardLength != 0) {
				rawData.append(getCardInfo(
						str.substring(currPosition, currPosition + cardLength
								* 2), cardLength));
				currPosition += cardLength * 2;
			}
			rawData.append("|");
			/* M1 Card Info */
			cardLength = Integer.parseInt(
					str.substring(currPosition, currPosition += 4), 16);
			rawData.append(cardLength);
			if (cardLength != 0) {
				rawData.append(getCardInfo(
						str.substring(currPosition, currPosition + cardLength
								* 2), cardLength));
				currPosition += cardLength * 2;
			}
			rawData.append("|");
			/* 2.4G Card Info */
			cardLength = Integer.parseInt(
					str.substring(currPosition, currPosition += 4), 16);
			rawData.append(cardLength);
			if (cardLength != 0) {
				rawData.append(getCardInfo(
						str.substring(currPosition, currPosition + cardLength
								* 2), cardLength));
				currPosition += cardLength * 2;
			}
			rawData.append("|");
			/* Temperature Transmitter */
			cardLength = Integer.parseInt(
					str.substring(currPosition, currPosition += 4), 16);
			rawData.append(cardLength);
			if (cardLength != 0) {
				rawData.append(getCardInfo(
						str.substring(currPosition, currPosition + cardLength
								* 2), cardLength));
				currPosition += cardLength * 2;
			}
			rawData.append("|");
			/* 2.4G E-Lock */
			cardLength = Integer.parseInt(
					str.substring(currPosition, currPosition += 4), 16);
			rawData.append(cardLength);
			if (cardLength != 0) {
				rawData.append(getCardInfo(
						str.substring(currPosition, currPosition + cardLength
								* 2), cardLength));
				currPosition += cardLength * 2;
			}
			rawData.append("|");
			/* Number of kids moving in and out */
			cardLength = Integer.parseInt(
					str.substring(currPosition, currPosition += 4), 16);
			rawData.append(cardLength);
			if (cardLength != 0) {
				rawData.append(Integer.parseInt(
						str.substring(currPosition, currPosition + 6), 16));
				rawData.append(Integer.parseInt(
						str.substring(currPosition, currPosition + 6), 16));
				currPosition += cardLength * 2;
			}
			rawData.append("|");
			/* Serial Number */
			rawData.append(Integer.parseInt(
					str.substring(currPosition, currPosition += 4), 16));
			rawData.append("|");
			/* Checksum */
			rawData.append(str.substring(currPosition, currPosition += 4));
		} catch (Exception e) {

		}
		return rawData.toString();
	}

	public static String byteArrayToString(byte[] data) {
		StringBuilder sb = new StringBuilder();
		for (byte b : data) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}

	private static String hexToBin(String hex) {
		String bin = "";
		String binFragment = "";
		int iHex;
		hex = hex.trim();
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

	public static String hexToDecimal(String hex, String format) {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < hex.length(); i += 2) {
			String str = hex.substring(i, i + 2);
			output.append(Integer.parseInt(str, 16));
			if (!(i == hex.length() - 2))
				output.append(format);
		}
		return output.toString();
	}

	public static int binaryToDecimal(String binString) {
		int dec = 0, k = 1;
		int bin = Integer.parseInt(binString);
		while (bin != 0) {
			dec = dec + (bin % 10) * k;
			k *= 2;
			bin /= 10;
		}
		return dec;
	}

	public static String getGpsInfo(String str, int currPosition) {
		StringBuilder gpsInfo = new StringBuilder();
		gpsInfo.append(",");
		gpsInfo.append(Integer.parseInt(
				str.substring(currPosition, currPosition += 2), 16));
		gpsInfo.append(",");
		gpsInfo.append((double) Integer.parseInt(
				str.substring(currPosition, currPosition += 8), 16) / 600000);
		gpsInfo.append(",");
		gpsInfo.append((double) Integer.parseInt(
				str.substring(currPosition, currPosition += 8), 16) / 600000);
		gpsInfo.append(",");
		gpsInfo.append(hexToDecimal(
				str.substring(currPosition, currPosition += 12), ":"));
		gpsInfo.append(",");
		gpsInfo.append((Integer.valueOf(
				str.substring(currPosition, currPosition += 4), 16) / 100) * 1.852);
		gpsInfo.append(",");
		gpsInfo.append(Integer.valueOf(
				str.substring(currPosition, currPosition += 6), 16));
		gpsInfo.append(",");
		gpsInfo.append(Integer.parseInt(
				str.substring(currPosition, currPosition += 4), 16));
		return gpsInfo.toString();
	}

	public static String getLbsInfo(String str, int currPosition) {
		StringBuilder lbsInfo = new StringBuilder();
		lbsInfo.append(",");
		lbsInfo.append(str.substring(currPosition, currPosition += 4));
		lbsInfo.append(",");
		lbsInfo.append(str.substring(currPosition, currPosition += 4));
		return lbsInfo.toString();
	}

	public static String getStatusInfo(String str, int currPosition) {
		StringBuilder statusInfo = new StringBuilder();
		statusInfo.append(",");
		statusInfo.append(str.substring(currPosition, currPosition += 2));
		statusInfo.append(",");
		statusInfo.append(hexToBin(str.substring(currPosition,
				currPosition += 2)));
		statusInfo.append(",");
		statusInfo.append(hexToBin(str.substring(currPosition,
				currPosition += 2)));
		statusInfo.append(",");
		statusInfo.append(hexToBin(str.substring(currPosition,
				currPosition += 2)));
		statusInfo.append(",");
		statusInfo.append(Integer.parseInt(
				str.substring(currPosition, currPosition += 2), 16));
		statusInfo.append(",");
		statusInfo.append(hexToBin(str.substring(currPosition,
				currPosition += 2)));
		statusInfo.append(",");
		statusInfo.append(Integer.parseInt(
				str.substring(currPosition, currPosition += 4), 16));
		statusInfo.append(",");
		statusInfo.append(Integer.parseInt(
				str.substring(currPosition, currPosition += 4), 16));
		statusInfo.append(",");
		statusInfo.append(Integer.parseInt(
				str.substring(currPosition, currPosition += 4), 16));
		statusInfo.append(",");
		statusInfo.append(Integer.parseInt(
				str.substring(currPosition, currPosition += 4), 16));
		statusInfo.append(",");
		String tempFirstByte = hexToBin(str.substring(currPosition,
				currPosition += 2));
		String tempSecondByte = hexToBin(str.substring(currPosition,
				currPosition += 2));
		statusInfo.append(binaryToDecimal(tempFirstByte.substring(2)
				+ tempSecondByte));
		return statusInfo.toString();
	}

	public static String getCardInfo(String cardData, int cardLength) {
		StringBuilder cardInfo = new StringBuilder();
		cardInfo.append(",");
		int numberOfCards = Integer.parseInt(cardData.substring(0, 2), 16);
		cardInfo.append(numberOfCards);
		cardInfo.append(",");
		int cardDigits = Integer.parseInt(cardData.substring(2, 4), 16);
		cardInfo.append(cardDigits);
		int beginIndex = 4;
		for (int loop = 0; loop < numberOfCards; loop++) {
			cardInfo.append(",");
			cardInfo.append(cardData.substring(beginIndex,
					beginIndex += cardDigits));
		}
		return cardInfo.toString();
	}
}
