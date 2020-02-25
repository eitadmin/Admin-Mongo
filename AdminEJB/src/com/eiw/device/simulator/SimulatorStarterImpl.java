package com.eiw.device.simulator;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import com.eiw.device.ejb.FleetTrackingDeviceListenerBORemote;

@Stateless
public class SimulatorStarterImpl implements SimulatorStarter {

	private Map<String, DeviceTCPIPSimulatorWOSocket> deviceTCPIPSimulators = new HashMap<String, DeviceTCPIPSimulatorWOSocket>();
	private Map<String, SimulatorData.SimData> deviceData = new HashMap<String, SimulatorData.SimData>();
	@EJB
	private FleetTrackingDeviceListenerBORemote fleetTrackingDeviceListener;
	private static final Logger LOGGER = Logger.getLogger("simulator");

	public SimulatorStarterImpl() {
		populateData();
	}

	public String activateAndStartDevice(String imeiNo) {
		LOGGER.info("SimulatorStarterImpl::activateAndStartDevice::" + imeiNo
				+ "Started Successfully");
		DeviceTCPIPSimulatorWOSocket deviceTCPIPSimulatorWOSocket = deviceTCPIPSimulators
				.get(imeiNo);
		if (deviceTCPIPSimulatorWOSocket == null) {
			SimulatorData.SimData simData = this.deviceData.get(imeiNo);
			if (simData == null) {
				return "Please Check Imei No";
			}
			LOGGER.info("SimulatorStarterImpl::activateAndStartDevice::simData.dataArray"
					+ simData.dataArray);
			LOGGER.info("SimulatorStarterImpl::activateAndStartDevice::simData.interval"
					+ simData.interval);
			deviceTCPIPSimulatorWOSocket = new DeviceTCPIPSimulatorWOSocket(
					imeiNo, simData.dataArray, simData.interval,
					fleetTrackingDeviceListener);
			deviceTCPIPSimulators.put(imeiNo, deviceTCPIPSimulatorWOSocket);
			deviceTCPIPSimulatorWOSocket.start();
			return "Device " + imeiNo + " activated and running";
		}
		return "Device " + imeiNo + " already activated and running";
	}

	public String deActivateDevice(String imeiNo) {
		LOGGER.info("SimulatorStarterImpl::deActivateDevice::Entered into this method"
				+ imeiNo);
		DeviceTCPIPSimulatorWOSocket deviceTCPIPSimulator = deviceTCPIPSimulators
				.get(imeiNo);
		if (deviceTCPIPSimulator != null) {
			deviceTCPIPSimulator.setSendDataContinuously(false);
			deviceTCPIPSimulators.remove(imeiNo);
			LOGGER.info("SimulatorStarterImpl::deActivateDevice::stopped"
					+ imeiNo);
			return "Device " + imeiNo + " Deactivated";
		}
		LOGGER.info("SimulatorStarterImpl::deActivateDevice::Leaving from method is Success");
		return "Device " + imeiNo + " already Deactivated";
	}

	public void deActivateDevices() {
		LOGGER.info("SimulatorStarterImpl::deActivateDevices::Entered into this method");
		for (Map.Entry<String, DeviceTCPIPSimulatorWOSocket> imeiSimulatorPair : deviceTCPIPSimulators
				.entrySet()) {
			if (imeiSimulatorPair != null) {
				imeiSimulatorPair.getValue().setSendDataContinuously(false);
				deviceTCPIPSimulators.remove(imeiSimulatorPair.getValue());
			}
		}
		LOGGER.info("SimulatorStarterImpl::deActivateDevices::Leaving from this method is success");
	}

	private void populateData() {
		LOGGER.info("SimulatorStarterImpl::populateData::Entered into method is Success");
		SimulatorData.SimData simData1 = new SimulatorData.SimData();
		simData1.dataArray = SimulatorData.SIM1_dataArray;
		simData1.interval = SimulatorData.SIM1_interval;
		this.deviceData.put(SimulatorData.SIM1_IMEI_No, simData1);

		SimulatorData.SimData simData2 = new SimulatorData.SimData();
		simData2.dataArray = SimulatorData.SIM2_dataArray;
		simData2.interval = SimulatorData.SIM2_interval;
		this.deviceData.put(SimulatorData.SIM2_IMEI_No, simData2);

		SimulatorData.SimData simData3 = new SimulatorData.SimData();
		simData3.dataArray = SimulatorData.SIM3_dataArray;
		simData3.interval = SimulatorData.SIM3_interval;
		this.deviceData.put(SimulatorData.SIM3_IMEI_No, simData3);

		LOGGER.info("SimulatorStarterImpl::populateData::Leaving from method is Success");
	}

	private String dataToHex(long deviceDatas) {
		LOGGER.info("SimulatorStarterImpl::dataToHex::Entered into method is Success:: DeviceDatas"
				+ deviceDatas);
		long datas = deviceDatas;
		String datasAsBinary = Long.toBinaryString(datas);
		String datasAsBinaryString = "0" + datasAsBinary;
		long i = Long.parseLong(datasAsBinaryString, 2);
		String hexString = Long.toHexString(i);
		for (int j = hexString.length(); j < 8; j++) {
			hexString = "0" + hexString;
		}
		LOGGER.info("SimulatorStarterImpl::dataToHex::leaving from method is success");
		return hexString;
	}
}