package com.eiw.server.bo;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import com.eiw.device.handler.APMKT_AIS1401ADeviceHandler;
import com.eiw.device.listener.ListenerStarter;

@LocalBean
@Stateless
public class APMKTDeviceMgmt {

	private static final Logger LOGGER = Logger.getLogger("APMKTDeviceMgmt");

	public String sendCommand(String imeiNo, String command) {
		LOGGER.info("Entered APMKTDeviceMgmt...");
		String status = null;
		try {
			APMKT_AIS1401ADeviceHandler  apmkt_AIS1401ADeviceHandler = ListenerStarter.apmkt_AIS1401ADeviceHandlerMap.get(imeiNo);
			if (apmkt_AIS1401ADeviceHandler != null) {
				LOGGER.info("Sending command to device...");
				status = apmkt_AIS1401ADeviceHandler.sendCommand(command,imeiNo, apmkt_AIS1401ADeviceHandler.getClientSocket());
				LOGGER.info("Reply from device..." + status);
			}
		} catch (Exception e) {
			LOGGER.error("APMKTDeviceMgmt : " + e);
			e.printStackTrace();
		}
		return status;
	}
}
