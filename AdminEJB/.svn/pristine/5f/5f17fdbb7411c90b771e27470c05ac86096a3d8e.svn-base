package com.eiw.server.bo;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import com.eiw.device.handler.ConcoxDeviceHandler;
import com.eiw.device.listener.ListenerStarter;

@LocalBean
@Stateless
public class ConcoxDeviceMgmt {

	private static final Logger LOGGER = Logger.getLogger("listener");

	public String sendCommand(String imeiNo, String command) {
		LOGGER.info("Entered ConcoxDeviceMgmt...");
		String status = null;
		try {
			ConcoxDeviceHandler concoxDeviceHandler = ListenerStarter.concoxDeviceHandlerMap
					.get(imeiNo);
			if (concoxDeviceHandler != null) {
				LOGGER.info("Sending command to device...");
				status = concoxDeviceHandler.sendCommand(
						command + "," + imeiNo,
						concoxDeviceHandler.getClientSocket());
				LOGGER.info("Reply from device..." + status);
			}
		} catch (Exception e) {
			LOGGER.error("ConcoxDeviceMgmt : " + e);
			e.printStackTrace();
		}
		return status;
	}

	public String sendNewConcoxCommand(String imeiNo, String command) {
		LOGGER.info("Entered into ConcoxDeviceMgmt....with new concox....");
		String status = null;
		try {
			ConcoxDeviceHandler concoxDeviceHandler = ListenerStarter.concoxDeviceHandlerMap
					.get(imeiNo);
			if (concoxDeviceHandler != null) {
				LOGGER.info("Sending command to device...");

				status = concoxDeviceHandler.sendNewCommand(imeiNo, command,
						concoxDeviceHandler.getClientSocket());				
				LOGGER.info("Reply from device..." + status);
				System.out.println(status);

			}
		} catch (Exception e) {
			LOGGER.error("ConcoxDeviceMgmtNewConcox : " + e);
			e.printStackTrace();

		}
		return status;

	}
}
