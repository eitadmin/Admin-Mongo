package com.eiw.server.bo;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import com.eiw.device.handler.TeltonikaDeviceHandler;
import com.eiw.device.listener.ListenerStarter;

@LocalBean
@Stateless
public class TeltonikaDeviceMgmt implements TeltonikaDeviceMgmtRemote {

	private static final Logger LOGGER = Logger.getLogger("listener");

	public String sendCommand(String password, String imeiNo, String model,
			String command) {
		LOGGER.info("Entered TeltonikaDeviceMgmt...");
		String status = null;
		try {
			TeltonikaDeviceHandler teltonikaDeviceHandler = ListenerStarter.teltonikaDeviceHandlerMap
					.get(imeiNo);
			if (teltonikaDeviceHandler != null) {
				LOGGER.info("Sending command to device... " + imeiNo);
				status = teltonikaDeviceHandler.sendCommand(password, model,
						command, teltonikaDeviceHandler.getClientSocket());
				LOGGER.info("Reply from device..." + status);
			}
		} catch (Exception e) {
			LOGGER.error("TeltonikaDeviceMgmt : " + e);
		}
		return status;
	}
}
