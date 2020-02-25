package com.skt.alerts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.jboss.logging.Logger;

import com.eiw.device.handler.DeviceHandler;
import com.eiw.server.bo.BOFactory;
import com.skt.client.dto.SktInitData;

//@Startup
//@Singleton
public class SKTStartup {
	private static final Logger LOGGER = Logger.getLogger("listener");
	private SKTAlertsEJBremote sktAlertsEJBRemote = BOFactory
			.getStudentalertEJBremote();
	private static SktInitData sktInitData;
	private boolean suspended = true;

	@PostConstruct
	void atStartup() {
		LOGGER.info("SKTStartup : On start");
		// getDBforSKT();
	}

	public void getDBforSKT() {
		sktInitData = sktAlertsEJBRemote.getDBforSKT();
		suspended = false;
		LOGGER.info("SktInitData Ready");
	}

	public static SktInitData getSKTinitData() {
		return sktInitData;
	}

	@PreDestroy
	void atShutdown() {
	}

	public void isSKTinitDataReady(DeviceHandler deviceHandler) {
		try {
			synchronized (deviceHandler) {
				while (suspended) {
					deviceHandler.wait(1000);
				}
				deviceHandler.notify();
			}
		} catch (Exception e) {
			LOGGER.error("SKTStartup : isSKTinitDataReady " + e);
			e.printStackTrace();
		}
	}
}
