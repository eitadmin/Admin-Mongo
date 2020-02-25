package com.eiw.server.bo;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import com.eiw.device.handler.DeepSeaDeviceHandler;
import com.eiw.device.listener.ListenerStarter;

@LocalBean
@Stateless
public class DeepseaDeviceMgmt {

	private static final Logger LOGGER = Logger.getLogger("listener");

	public String sendCommand(String imeiNo, String command) {
		LOGGER.info("Entered DeepseaDeviceMgmt...");
		String status = null;
		try {
			DeepSeaDeviceHandler DeepseaDeviceHandler = ListenerStarter.DeepSeaDeviceHandlerMap
					.get(imeiNo);
			if (DeepseaDeviceHandler != null) {
				LOGGER.info("Sending command to device...");
				status = DeepseaDeviceHandler.sendCommand(
						command + "," + imeiNo,
						DeepseaDeviceHandler.getClientSocket());
				LOGGER.info("Reply from device..." + status);
			}
		} catch (Exception e) {
			LOGGER.error("DeepseaDeviceMgmt : " + e);
			e.printStackTrace();
		}
		return status;
	}
}
