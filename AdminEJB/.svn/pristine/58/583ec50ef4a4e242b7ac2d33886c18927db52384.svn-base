package com.eiw.device.simulator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.jboss.logging.Logger;

import com.eiw.server.TimeZoneUtil;

public class DeviceTCPIPSimulator extends Thread {
	private static Socket clientSocket = null;
	private static DataInputStream clientSocketDis = null;
	private static DataOutputStream clientSocketDos = null;

	private String imei;
	private String dataTemplate = "0801#timestamp00#longitude#latitude006f00d604#speed0004030101150316030001460000015d00";

	private long[][] dataArray;
	private int interval;
	private boolean sendDataContinuously = true;

	private static final Logger LOGGER = Logger.getLogger("simulator");

	public DeviceTCPIPSimulator(String IMEI, long[][] dataArray, int interval) {
		this.imei = IMEI;
		this.dataArray = dataArray;
		this.interval = interval;
	}

	// Run the listen/accept loop forever
	public void run() {
		LOGGER.info("DeviceTCPIPSimulator: run: Start successfully" + "Imei"
				+ imei + "dataArray" + dataArray + "interval" + interval);
		try {
			LOGGER.info("DeviceTCPIPSimulator: run: client socket start successfully:");
			clientSocket = new Socket("localhost", 5419);
		} catch (IOException e) {
			LOGGER.error("DeviceTCPIPSimulator: run: Exception creating client socket "
					+ e);
			return;
		}
		simulate(this.imei, this.dataArray, this.interval);
	}

	public void simulate(String IMEI, long[][] dataArray, int interval) {
		LOGGER.info("DeviceTCPIPSimulator: simulate:Entered into simulate method "
				+ "IMEI"
				+ IMEI
				+ "dataArray"
				+ dataArray
				+ "interval"
				+ interval);
		try {
			LOGGER.info("DeviceTCPIPSimulator: simulate: "
					+ "Start to simulate");
			clientSocketDos = new DataOutputStream(
					clientSocket.getOutputStream());
			clientSocketDos.writeUTF(IMEI);
			clientSocketDis = new DataInputStream(clientSocket.getInputStream());
			boolean acknowedgementIndicator = clientSocketDis.readBoolean();
			LOGGER.info("DeviceTCPIPSimulator: simulate: Ack Indicator"
					+ acknowedgementIndicator);
			sendData(clientSocket);
		} catch (IOException e) {
			cleanUp();
			LOGGER.error("DeviceTCPIPSimulator: simulate: Error is :" + e);
		}
		LOGGER.info("DeviceTCPIPSimulator: simulate: End of Simulate method");
	}

	private void sendData(Socket clientSocket) throws IOException {
		LOGGER.info("DeviceTCPIPSimulator: sendData: I am inside senddata method"
				+ "clientSocket" + clientSocket);
		int i = 0;
		while (sendDataContinuously) {
			if (i == dataArray.length) {
				i = 0;
			}
			LOGGER.info("DeviceTCPIPSimulator: sendData: I amd inside senddata No1");
			long[] singleRecord = dataArray[i++];
			String latInHexa = dataToHex(singleRecord[0]);
			String longInHexa = dataToHex(singleRecord[1]);
			String speed = "00" + String.valueOf(singleRecord[2]);
			String data = dataTemplate.replaceFirst("#latitude", latInHexa);
			data = data.replaceFirst("#longitude", longInHexa);
			String currentTimestampInHexa = dateToHex();
			data = data.replaceFirst("#timestamp", currentTimestampInHexa);
			data = data.replaceFirst("#speed", speed);
			clientSocketDos.writeBytes(data);
			LOGGER.info("DeviceTCPIPSimulator: sendData: data wriiten is ....."
					+ data.getBytes());
			String sentDataNumberInHexa = data.substring(2, 4);
			long sentDataNumber = Long.parseLong(sentDataNumberInHexa, 16);
			LOGGER.info("DeviceTCPIPSimulator: sendData: sendDataNumber :"
					+ sentDataNumber);
			int receivedDataNumber = clientSocketDis.readInt();
			LOGGER.info("DeviceTCPIPSimulator: sendData: receivedDataNumber :"
					+ receivedDataNumber);
			while (sentDataNumber != receivedDataNumber) {
				clientSocketDos.writeUTF(data);
				LOGGER.info("DeviceTCPIPSimulator: sendData: resending the data..");
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					LOGGER.error("DeviceTCPIPSimulator: sendData: Error Occured in catch"
							+ e);
				}
			}
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				LOGGER.error("DeviceTCPIPSimulator: sendData: Interrupted Exception"
						+ e);
			}
		}
		LOGGER.info("exit" + imei);
		LOGGER.info("DeviceTCPIPSimulator: sendData: im leaving from senddata method");
	}

	private String dataToHex(long deviceDatas) {
		LOGGER.info("DeviceTCPIPSimulator: dataToHex:Entered into that method "
				+ "deviceDatas" + deviceDatas);
		long datas = deviceDatas;
		String datasAsBinary = Long.toBinaryString(datas);
		String datasAsBinaryString = "0" + datasAsBinary;
		int i = Integer.parseInt(datasAsBinaryString, 2);
		String hexString = Integer.toHexString(i);
		for (int j = hexString.length(); j < 8; j++) {
			hexString = "0" + hexString;
		}
		LOGGER.info("DeviceTCPIPSimulator: dataToHex: successfully leaving from dataToHex method"
				+ "hexString" + hexString);
		return hexString;
	}

	private String dateToHex() {
		LOGGER.info("DeviceTCPIPSimulator: dateToHex:: successfully Entered");
		Date date = TimeZoneUtil.getDateInTimeZone();
		SimpleDateFormat gmtFormat = new SimpleDateFormat();
		TimeZone gmtTime = TimeZone.getTimeZone("GMT");
		gmtFormat.setTimeZone(gmtTime);
		long dateInLong = date.getTime();
		String hexString = prefixZeros(Long.toHexString(dateInLong), 16);
		LOGGER.info("DeviceTCPIPSimulator:dateToHex::Leaving Successfully"
				+ "hexString" + hexString);
		return hexString;
	}

	private String prefixZeros(String string, int noOfDigits) {
		LOGGER.info("DeviceTCPIPSimulator:prefixZeros:: Entered into method"
				+ "string value" + string + "No of Digits" + noOfDigits);
		for (int j = string.length(); j < noOfDigits; j++) {
			string = "0" + string;
		}
		LOGGER.info("DeviceTCPIPSimulator:prefizZeros::Leaving from method Successfully");
		return string;
	}

	public String getIMEI() {
		return imei;
	}

	public void setIMEI(String iMEI) {
		imei = iMEI;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public void setSendDataContinuously(boolean sendDataContinuously) {
		this.sendDataContinuously = sendDataContinuously;
	}

	private void cleanUp() {
		LOGGER.info("DeviceTCPIPSimulator::cleanUp:: Cleaning start");
		try {
			LOGGER.info("DeviceTCPIPSimulator: clientSocketDis::"
					+ clientSocketDis);
			if (clientSocketDis != null) {
				clientSocketDis.close();
				clientSocketDis = null;
			}
		} catch (IOException ie1) {
			LOGGER.error("DeviceTCPIPSimulator: clientSocketDis::" + ie1);
			clientSocketDis = null;
		}
		try {
			LOGGER.info("DeviceTCPIPSimulator::clientSocketDos::"
					+ clientSocketDos);
			if (clientSocketDos != null) {
				clientSocketDos.close();
				clientSocketDos = null;
			}
		} catch (IOException ie2) {
			LOGGER.error("DeviceTCPIPSimulator:clientSocketDos::" + ie2);
			clientSocketDos = null;
		}
		try {
			LOGGER.info("DeviceTCPIPSimulator:clientSocket::" + clientSocket);
			if (clientSocket != null) {
				clientSocket.close();
				clientSocket = null;
			}
		} catch (IOException ie1) {
			LOGGER.error("DeviceTCPIPSimulator:clientSocket::" + ie1);
			clientSocket = null;
		}
		LOGGER.info("DeviceTCPIPSimulator:: cleanUp:: Cleaning Successfully completed");
	}
}