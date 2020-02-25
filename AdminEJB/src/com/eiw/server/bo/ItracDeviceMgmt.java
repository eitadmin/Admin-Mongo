package com.eiw.server.bo;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import com.eiw.device.handler.ItracDeviceHandler1;
import com.eiw.device.listener.ListenerStarter;

@LocalBean
@Stateless
public class ItracDeviceMgmt {

	private static final Logger LOGGER = Logger.getLogger("listener");

	public String sendCommand(String pass, String imeiNo, String command) {
		LOGGER.info("Entered ItracDeviceMgmt...");
		String status = null;
		try {
			ItracDeviceHandler1 itracDeviceHandler = ListenerStarter.itracDeviceHandlerMap1
					.get(imeiNo);
			if (itracDeviceHandler != null) {
				LOGGER.info("Sending command to device...");
				status = itracDeviceHandler.sendCommand(imeiNo, command,
						itracDeviceHandler.getClientSocket());
				LOGGER.info("Reply from device..." + status);
			}
		} catch (Exception e) {
			LOGGER.error("ItracDeviceMgmt : " + e);
			e.printStackTrace();
		}
		return status;
	}
}
