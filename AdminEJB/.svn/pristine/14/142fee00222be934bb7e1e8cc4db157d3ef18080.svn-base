package com.eiw.server.bo;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import com.eiw.device.handler.DeepseaDeviceHandlerV2;
import com.eiw.device.listener.ListenerStarter;

@LocalBean
@Stateless
public class DeeseaDeviceNewMgmt {
	private static final Logger LOGGER = Logger.getLogger("listener");

	public String sendCommand(String imeiNo, String command, String slaveId,
			String commandType) {
		LOGGER.info("Entered DeepseaDeviceMgmt...");
		String status = null;
		try {
			DeepseaDeviceHandlerV2 depseaDeviceHandlerV2 = ListenerStarter.deepseaDeviceHandlerV2Map
					.get(imeiNo);
			if (depseaDeviceHandlerV2 != null) {
				LOGGER.info("Sending command to device...");
				if (commandType.equalsIgnoreCase("gprs")) {
					status = depseaDeviceHandlerV2.sendGPRsCommand(imeiNo,
							command, depseaDeviceHandlerV2.getClientSocket());
				} else if (commandType.equalsIgnoreCase("write")) {
					depseaDeviceHandlerV2.sendWriteCommand(imeiNo, slaveId,
							command, depseaDeviceHandlerV2.getClientSocket());
				} else {
					depseaDeviceHandlerV2.checkConfigDataLength(imeiNo);
				}
				LOGGER.info("Reply from device..." + status);
			}
		} catch (Exception e) {
			LOGGER.error("DeepseaNewDeviceMgmt : " + e);
			e.printStackTrace();
		}
		return status;
	}
}
