package com.eiw.server.bo;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import com.eiw.device.handler.RuptelaDeviceHandler;
import com.eiw.device.listener.ListenerStarter;

@LocalBean
@Stateless
public class RuptelaDeviceMgmt implements RuptelaDeviceMgmtRemote {

	private static final Logger LOGGER = Logger.getLogger("listener");

	public String sendCommand(String password, String imeiNo, String command) {
		LOGGER.info("Entered RuptelaDeviceMgmt...");
		String status = null;
		try {
			RuptelaDeviceHandler ruptelaDeviceHandler = ListenerStarter.ruptelaDeviceHandlerMap
					.get(imeiNo);
			if (ruptelaDeviceHandler != null) {
				LOGGER.info("Sending command to device...");
				status = ruptelaDeviceHandler.sendCommand(password, command
						+ "," + imeiNo, ruptelaDeviceHandler.getClientSocket());
				LOGGER.info("Reply from device..." + status);
			}
		} catch (Exception e) {
			LOGGER.error("RuptelaDeviceMgmt : " + e);
			e.printStackTrace();
		}
		return status;
	}
	public String sendNewCommand(String password, String imeiNo, String command) {
		LOGGER.info("Entered New RuptelaDeviceMgmt...");
		String status = null;
		try {
			RuptelaDeviceHandler ruptelaDeviceHandler = ListenerStarter.ruptelaDeviceHandlerMap
					.get(imeiNo);
			if (ruptelaDeviceHandler != null) {
				LOGGER.info("Sending command to device...");
				status = ruptelaDeviceHandler.sendNewCommand(password, command,
						ruptelaDeviceHandler.getClientSocket());
				LOGGER.info("Reply from device..." + status);
			}
		} catch (Exception e) {
			LOGGER.error("RuptelaDeviceMgmt : " + e);
			e.printStackTrace();
		}
		return status;
	}
}
