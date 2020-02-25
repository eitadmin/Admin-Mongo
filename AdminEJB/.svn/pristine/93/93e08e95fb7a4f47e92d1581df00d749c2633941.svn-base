package com.eiw.noTransmissionOverride;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import com.eiw.device.ejb.FleetTrackingDeviceListenerBORemote;
import com.eiw.device.simulator.DeviceTCPIPSimulatorWOSocket;

@LocalBean
@Stateless
public class DeviceStarterImpl implements DeviceStarter {
	private static final Logger LOGGER = Logger.getLogger("listener");
	private Map<String, SimulateHandler> simulateHandlerMap = new HashMap<String, SimulateHandler>();
	@EJB
	private FleetTrackingDeviceListenerBORemote fleetTrackingDeviceListener;

	public String StartDevice(String imeiNo, String nearByVins) {
		String STR_SUCCESS = "Added";
		LOGGER.info("DeviceStarterImpl::StartDevice::" + imeiNo
				+ "Started Successfully");
		SimulateHandler simulateHandler = simulateHandlerMap.get(imeiNo);
		if (simulateHandler == null) {
			simulateHandler = new SimulateHandler(imeiNo, nearByVins, 150000,
					fleetTrackingDeviceListener);
			simulateHandlerMap.put(imeiNo, simulateHandler);
			simulateHandler.start();
			return STR_SUCCESS;
		}

		return null;

	}

	public String deActivateDeviceForNoTrnsDevice(String imeiNo) {
		LOGGER.info("SimulatorStarterImpl::deActivateDevice::Entered into this method"
				+ imeiNo);
		SimulateHandler simulateHandlerForStop = simulateHandlerMap.get(imeiNo);

		if (simulateHandlerForStop != null) {
			simulateHandlerForStop.setSendDataContinuously(false);
			simulateHandlerMap.remove(imeiNo);
			LOGGER.info("SimulatorStarterImpl::deActivateDevice::stopped"
					+ imeiNo);
			return "Device " + imeiNo + " Deactivated";
		}
		LOGGER.info("SimulatorStarterImpl::deActivateDevice::Leaving from method is Success");
		return "Device " + imeiNo + " already Deactivated";
	}

}
