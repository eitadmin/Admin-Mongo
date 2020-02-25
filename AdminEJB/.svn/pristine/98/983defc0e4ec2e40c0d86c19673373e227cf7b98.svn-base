package com.eiw.device.handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.jboss.logging.Logger;

public class SolarTimeStampHandler extends DeviceHandler {
	private static final Logger LOGGER = Logger.getLogger("listener");

	@Override
	protected void handleDevice() {
		LOGGER.info("Entered SolarTimeStampHandler five mins Handle Device:"
				+ new Date());
		DataInputStream clientSocketDis = null;
		DataOutputStream clientSocketDos = null;
		try {
			clientSocket.setSoTimeout(5000);
			clientSocketDis = new DataInputStream(clientSocket.getInputStream());
			clientSocketDos = new DataOutputStream(
					clientSocket.getOutputStream());
			LOGGER.info("date = " + new Date());

			SimpleDateFormat dateFormatGmt = new SimpleDateFormat(
					"yyyy-MMM-dd HH:mm:ss");
			dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));

			// Local time zone
			SimpleDateFormat dateFormatLocal = new SimpleDateFormat(
					"yyyy-MMM-dd HH:mm:ss");

			// Time in GMT
			Date dat = dateFormatLocal.parse(dateFormatGmt.format(new Date()));

			String resp = "57707 yy-mm-dd hh:me:ss 00 0 0 521.2 UTC(NIST) *";

			Calendar calendarCurDate = Calendar.getInstance();
			calendarCurDate.setTime(dat);
			resp = resp.replace("yy",
					String.valueOf(calendarCurDate.get(Calendar.YEAR))
							.substring(2));
			resp = resp.replace("mm", String.format("%02d",
					calendarCurDate.get(Calendar.MONTH) + 1));
			resp = resp.replace(
					"dd",
					String.format("%02d",
							calendarCurDate.get(Calendar.DAY_OF_MONTH)));

			resp = resp.replace(
					"hh",
					String.format("%02d",
							calendarCurDate.get(Calendar.HOUR_OF_DAY)));
			resp = resp
					.replace(
							"me",
							String.format("%02d",
									calendarCurDate.get(Calendar.MINUTE)));
			resp = resp
					.replace(
							"ss",
							String.format("%02d",
									calendarCurDate.get(Calendar.SECOND)));
			clientSocketDos.writeBytes("\n");
			clientSocketDos.writeBytes(resp);
			clientSocketDos.close();
		} catch (SocketTimeoutException e) {
			LOGGER.error("SocketTimeoutException while receiving the Message "
					+ e);
			e.printStackTrace();
		} catch (Exception e) {
			LOGGER.error("SolarPanelDeviceHandler:Exception while receiving the Message "
					+ e);
			e.printStackTrace();
		} finally {
			cleanUpSockets(clientSocket, clientSocketDis, clientSocketDos);
			LOGGER.info("DeviceCommunicatorThread:DeviceCommunicator Completed");
		}
		LOGGER.info("SolarTimeStampHandler: Ended successfully::: ");
	}
}