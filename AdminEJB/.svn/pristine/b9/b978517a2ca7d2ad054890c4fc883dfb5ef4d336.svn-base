package com.eiw.server.bo;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;
import org.json.JSONObject;

import com.eiw.device.handler.AndroidDeviceHandler;
import com.eiw.device.listener.ListenerStarter;

@LocalBean
@Stateless
public class AndroidDeviceMgmt {
	private static final Logger LOGGER = Logger.getLogger("listener");

	public String sendCommand(String imeiNo, JSONObject jsObj) {
		LOGGER.info("Entered AndroidDeviceMgmt...");
		String status = null;
		try {
			AndroidDeviceHandler androidDeviceHandler = ListenerStarter.androidDeviceHandlerMap
					.get(imeiNo);
			System.out.println(androidDeviceHandler + "  " + imeiNo);

			if (androidDeviceHandler != null) {
				LOGGER.info("Sending command to device...");
				status = androidDeviceHandler.sendCommand(jsObj,
						androidDeviceHandler.getClientSocket());
				LOGGER.info("Reply from device..." + status);
			}
		} catch (Exception e) {
			LOGGER.error("AndroidDeviceMgmt : " + e);
			e.printStackTrace();
		}
		return status;
	}
}
