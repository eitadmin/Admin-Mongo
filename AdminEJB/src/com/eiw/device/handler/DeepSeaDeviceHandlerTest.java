package com.eiw.device.handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;
import org.json.JSONObject;

import com.eiw.device.deepseaTest.DeepSeaByteWrapperTest;
import com.eiw.device.ejb.FleetTrackingDeviceListenerBORemote;
import com.eiw.device.ejb.VehicleComposite;
import com.eiw.device.handler.method.SKTHandlerMethods;
import com.eiw.device.listener.ListenerStarter;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.bo.BOFactory;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.Vehicleevent;
import com.eiw.server.fleettrackingpu.VehicleeventId;

public class DeepSeaDeviceHandlerTest extends DeviceHandler {
	private static final Logger LOGGER = Logger.getLogger("listener");

	FleetTrackingDeviceListenerBORemote fleetTrackingDeviceListenerBO = BOFactory
			.getFleetTrackingDeviceListenerBORemote();

	public static Map<String, List<DeepSeaByteWrapperTest>> avlDataMap = new HashMap<String, List<DeepSeaByteWrapperTest>>();
	public static String commandStatus;
	private SKTHandlerMethods sktHandlerMethods;

	@Override
	public void handleDevice() {
		LOGGER.info("Entered DeepSea five mins Handle Device:" + new Date());
		sktHandlerMethods = new SKTHandlerMethods();
		DataInputStream clientSocketDis = null;
		DataOutputStream dos = null;
		try {
			clientSocket.setSoTimeout(1500000);
			clientSocketDis = new DataInputStream(clientSocket.getInputStream());
			DeepSeaByteWrapperTest data = new DeepSeaByteWrapperTest(
					clientSocketDis);
			data.unwrapDataFromStream();
			LOGGER.info("Test 1 DeepSea");
			String imeiNo = data.getImei();
			VehicleComposite vehicleComposite = fleetTrackingDeviceListenerBO
					.getVehicle(imeiNo);
			if (vehicleComposite == null) {
				LOGGER.error("DeepSeaDeviceHandler: handleDevice: Received IMEI No is invalid... returning... "
						+ imeiNo);
				return;
			}
			super.deviceImei = imeiNo;
			ListenerStarter.deepseaTestMap.put(deviceImei, this);
			// LOGGER.info("Test 2:" + super.deviceImei);
			insertService(data, vehicleComposite, fleetTrackingDeviceListenerBO);
			while (true) {
				DeepSeaByteWrapperTest rawData = new DeepSeaByteWrapperTest(
						clientSocketDis);
				rawData.unwrapDataFromStream();
				LOGGER.info("Entered while");
				if (ListenerStarter.deepseaTestMap.get(deviceImei) == null) {
					LOGGER.info("No Handler Found for IMEI NO: " + deviceImei);
					ListenerStarter.deepseaTestMap.put(deviceImei, this);
				}
				insertService(rawData, vehicleComposite,
						fleetTrackingDeviceListenerBO);
			}
		} catch (SocketTimeoutException e) {
			LOGGER.error("SocketTimeoutException while receiving the Message "
					+ e);
		} catch (Exception e) {
			LOGGER.error("DeepSeaDeviceHandler:Exception while receiving the Message "
					+ e);
		} finally {
			if (deviceImei != null) {
				ListenerStarter.DeepSeaDeviceHandlerMap.remove(deviceImei);
			}
			cleanUpSockets(clientSocket, clientSocketDis, dos);
			LOGGER.info("DeepSeaDeviceHandler DeviceCommunicatorThread:DeviceCommunicator Completed");
		}
	}

	private void insertService(DeepSeaByteWrapperTest rawData,
			VehicleComposite vehicleComposite,
			FleetTrackingDeviceListenerBORemote fleetTrackingDeviceListenerBO) {
		try {
			LOGGER.info("Entered insert service");
			Vehicle vehicle = vehicleComposite.getVehicle();
			Vehicleevent vehicleEvent = prepareVehicleEvents(vehicle, rawData,
					fleetTrackingDeviceListenerBO);
			List<Vehicleevent> vehicleEvents = new ArrayList<Vehicleevent>();
			if (vehicleEvent != null)
				vehicleEvents.add(vehicleEvent);
			if (!vehicleEvents.isEmpty() && vehicleEvents.size() != 0) {
				for (Vehicleevent vehicleevent : vehicleEvents) {
					sktHandlerMethods.persistEventAndGenerateAlert(null,
							vehicleComposite, rawData.getImei(), "deepsea",
							vehicleevent);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception while persisting data :: " + e);
		}
	}

	private Vehicleevent prepareVehicleEvents(Vehicle vehicle,
			DeepSeaByteWrapperTest avl,
			FleetTrackingDeviceListenerBORemote fleetTrackingDeviceListenerBO) {
		Vehicleevent vehicleEvent = new Vehicleevent();
		List<DeepSeaByteWrapperTest> avlDataList = new ArrayList<DeepSeaByteWrapperTest>();
		avlDataList.add(avl);
		LOGGER.info("Entered preparedVehicleevent, vin:" + vehicle.getVin());
		try {
			if (!avlDataMap.isEmpty() && avlDataMap.size() != 0) {
				if (avlDataMap.get(vehicle.getVin()) != null
						&& !avlDataMap.get(vehicle.getVin()).isEmpty()
						&& avlDataMap.get(vehicle.getVin()).size() != 0) {
					avlDataList.addAll(avlDataMap.get(vehicle.getVin()));
				}
			}
			avlDataMap.put(vehicle.getVin(), avlDataList);
			if (avlDataMap.get(vehicle.getVin()) != null
					&& avlDataMap.get(vehicle.getVin()).size() >= 6) {
				DeepSeaByteWrapperTest avlData = avlDataMap.get(
						vehicle.getVin()).get(0);
				String region = fleetTrackingDeviceListenerBO
						.getTimeZoneRegion(vehicle.getVin());
				VehicleeventId vehicleeventId = new VehicleeventId();
				LOGGER.info("Deepsea Modbus: " + avlData.getModbusData());
				LOGGER.info("Deepsea Gettime: "
						+ avlData.getModbusData().getDateTime());
				vehicleeventId.setEventTimeStamp(TimeZoneUtil.getDateTimeZone(
						avlData.getModbusData().getDateTime(), region));
				vehicleEvent.setServerTimeStamp(TimeZoneUtil
						.getDateInTimeZone());
				vehicleeventId.setVin(vehicle.getVin());
				vehicleEvent.setId(vehicleeventId);
				JSONObject ioevent = new JSONObject();
				boolean isFirstReq = false, isSecondReq = false, isThirdReq = false, isFourthReq = false, isFifthReq = false, isSixthReq = false, isSeventhReq = false;
				if (avlDataMap.get(vehicle.getVin()).get(0).getModbusData()
						.getLattitude() == null
						|| avlDataMap.get(vehicle.getVin()).get(0)
								.getModbusData().getLongitude() == null
						|| Float.valueOf(avlDataMap.get(vehicle.getVin())
								.get(0).getModbusData().getLattitude()) < 1
						|| Float.valueOf(avlDataMap.get(vehicle.getVin())
								.get(0).getModbusData().getLongitude()) < 1) {
					Vehicleevent preVE = fleetTrackingDeviceListenerBO
							.getPrevVe(vehicle.getVin());
					if (preVE != null) {
						vehicleEvent.setLatitude(preVE.getLatitude());
						vehicleEvent.setLongitude(preVE.getLongitude());
					} else {
						LOGGER.info("Return vin:" + vehicle.getVin());
						return null;
					}

				} else {
					vehicleEvent.setLatitude(Double.valueOf(avlDataMap
							.get(vehicle.getVin()).get(0).getModbusData()
							.getLattitude()));
					vehicleEvent.setLongitude(Double.valueOf(avlDataMap
							.get(vehicle.getVin()).get(0).getModbusData()
							.getLongitude()));
				}
				vehicleEvent.setSpeed(avlDataMap.get(vehicle.getVin()).get(0)
						.getModbusData().getSpeed());
				vehicleEvent
						.setAi1(Integer.parseInt(avlDataMap
								.get(vehicle.getVin()).get(0).getModbusData()
								.getAIP1()));
				vehicleEvent
						.setAi2(Integer.parseInt(avlDataMap
								.get(vehicle.getVin()).get(0).getModbusData()
								.getAIP2()));
				vehicleEvent
						.setAi3(Integer.parseInt(avlDataMap
								.get(vehicle.getVin()).get(0).getModbusData()
								.getAIP3()));
				vehicleEvent
						.setAi4(Integer.parseInt(avlDataMap
								.get(vehicle.getVin()).get(0).getModbusData()
								.getAIP4()));
				vehicleEvent
						.setDi1(Integer.parseInt(avlDataMap
								.get(vehicle.getVin()).get(0).getModbusData()
								.getDIP1()));
				vehicleEvent
						.setDi2(Integer.parseInt(avlDataMap
								.get(vehicle.getVin()).get(0).getModbusData()
								.getDIP2()));
				JSONObject vehicleAdditionalData = new JSONObject();
				vehicleAdditionalData.put("gpsCOG",
						avlDataMap.get(vehicle.getVin()).get(0).getModbusData()
								.getGpsCOG());
				vehicleAdditionalData.put("gpsAltitude",
						avlDataMap.get(vehicle.getVin()).get(0).getModbusData()
								.getGpsAltitude());
				vehicleAdditionalData.put("noOfSatlite",
						avlDataMap.get(vehicle.getVin()).get(0).getModbusData()
								.getNoOfSatlite());
				vehicleAdditionalData.put("battVolt",
						avlDataMap.get(vehicle.getVin()).get(0).getModbusData()
								.getBattVolt());
				vehicleAdditionalData.put("battVolt",
						avlDataMap.get(vehicle.getVin()).get(0).getModbusData()
								.getBattVolt());
				vehicleAdditionalData.put("onWire",
						avlDataMap.get(vehicle.getVin()).get(0).getModbusData()
								.getOnWire());
				vehicleEvent.setTags(vehicleAdditionalData.toString());

				for (DeepSeaByteWrapperTest avlDataIo : avlDataMap.get(vehicle
						.getVin())) {
					if (avlDataIo.getModbusData().getRequestId() == 1) {
						if (avlDataIo.getModbusData().getEngineSpeed() != null
								&& !avlDataIo.getModbusData().getEngineSpeed()
										.equalsIgnoreCase("0"))
							vehicleEvent.setEngine(true);
						else
							vehicleEvent.setEngine(false);
					}

					switch (avlDataIo.getModbusData().getRequestId()) {
					case 1:
						isFirstReq = true;
						ioevent.put("oilPressure", avlDataIo.getModbusData()
								.getOilPressure());
						ioevent.put("coolandTemperature", avlDataIo
								.getModbusData().getCoolandTemperature());
						ioevent.put("oilTemperature", avlDataIo.getModbusData()
								.getOilTemperature());
						ioevent.put("fuelLeval", avlDataIo.getModbusData()
								.getFuelLeval());
						ioevent.put("chargeAlternatorVoltage", avlDataIo
								.getModbusData().getChargeAlternatorVoltage());
						ioevent.put("batteryVoltage", avlDataIo.getModbusData()
								.getBatteryVoltage());
						ioevent.put("engineSpeed", avlDataIo.getModbusData()
								.getEngineSpeed());
						ioevent.put("generatorFrequency", avlDataIo
								.getModbusData().getGeneratorFrequency());
						ioevent.put("genL1_N_V", avlDataIo.getModbusData()
								.getGenL1_N_V());
						ioevent.put("genL2_N_V", avlDataIo.getModbusData()
								.getGenL2_N_V());
						ioevent.put("genL3_N_V", avlDataIo.getModbusData()
								.getGenL3_N_V());
						ioevent.put("genL1_L2_V", avlDataIo.getModbusData()
								.getGenL1_L2_V());
						ioevent.put("genL2_L3_V", avlDataIo.getModbusData()
								.getGenL2_L3_V());
						ioevent.put("genL3_L1_V", avlDataIo.getModbusData()
								.getGenL3_L1_V());
						ioevent.put("genL1C", avlDataIo.getModbusData()
								.getGenL1C());
						ioevent.put("genL2C", avlDataIo.getModbusData()
								.getGenL2C());
						ioevent.put("genL3C", avlDataIo.getModbusData()
								.getGenL3C());
						ioevent.put("genEarthC", avlDataIo.getModbusData()
								.getGenEarthC());
						ioevent.put("genL1W", avlDataIo.getModbusData()
								.getGenL1W());
						ioevent.put("genL2W", avlDataIo.getModbusData()
								.getGenL2W());
						ioevent.put("genL3W", avlDataIo.getModbusData()
								.getGenL3W());
						ioevent.put("genC_lag", avlDataIo.getModbusData()
								.getGenC_lag());
						ioevent.put("mainsFrequency", avlDataIo.getModbusData()
								.getMainsFrequency());
						ioevent.put("mainsL1_N_V", avlDataIo.getModbusData()
								.getMainsL1_N_V());
						ioevent.put("mainsL2_N_V", avlDataIo.getModbusData()
								.getMainsL2_N_V());
						ioevent.put("mainsL3_N_V", avlDataIo.getModbusData()
								.getMainsL3_N_V());
						ioevent.put("mainsL1L2_N_V", avlDataIo.getModbusData()
								.getMainsL1L2_N_V());
						ioevent.put("mainsL2L3_N_V", avlDataIo.getModbusData()
								.getMainsL2L3_N_V());
						ioevent.put("mainsL3L1_N_V", avlDataIo.getModbusData()
								.getMainsL3L1_N_V());
						ioevent.put("mainsV_lag", avlDataIo.getModbusData()
								.getMainsV_lag());
						ioevent.put("gen_P_R", avlDataIo.getModbusData()
								.getGen_P_R());
						ioevent.put("mains_P_R", avlDataIo.getModbusData()
								.getMains_P_R());
						ioevent.put("mainsC_lag", avlDataIo.getModbusData()
								.getMainsC_lag());
						ioevent.put("mainsL1C", avlDataIo.getModbusData()
								.getMainsL1C());
						ioevent.put("mainsL2C", avlDataIo.getModbusData()
								.getMainsL2C());
						ioevent.put("mainsL3C", avlDataIo.getModbusData()
								.getMainsL3C());
						ioevent.put("mainsEarthC", avlDataIo.getModbusData()
								.getMainsEarthC());
						ioevent.put("mainsL1W", avlDataIo.getModbusData()
								.getMainsL1W());
						ioevent.put("mainsL2W", avlDataIo.getModbusData()
								.getMainsL2W());
						ioevent.put("mainsL3W", avlDataIo.getModbusData()
								.getMainsL3W());
						break;
					case 2:
						isSecondReq = true;
						ioevent.put("busC_lag", avlDataIo.getModbusData()
								.getBusC_lag());
						ioevent.put("busFrequency", avlDataIo.getModbusData()
								.getBusFrequency());
						ioevent.put("busL1_N_V", avlDataIo.getModbusData()
								.getBusL1_N_V());
						ioevent.put("busL2_N_V", avlDataIo.getModbusData()
								.getBusL2_N_V());
						ioevent.put("busL3_N_V", avlDataIo.getModbusData()
								.getBusL3_N_V());
						ioevent.put("busL1L2_N_V", avlDataIo.getModbusData()
								.getBusL1L2_N_V());
						ioevent.put("busL2L3_N_V", avlDataIo.getModbusData()
								.getBusL2L3_N_V());
						ioevent.put("busL3L1_N_V", avlDataIo.getModbusData()
								.getBusL3L1_N_V());
						ioevent.put("busL1C", avlDataIo.getModbusData()
								.getBusL1C());
						ioevent.put("busL2C", avlDataIo.getModbusData()
								.getBusL2C());
						ioevent.put("busL3C", avlDataIo.getModbusData()
								.getBusL3C());
						ioevent.put("busEarthC", avlDataIo.getModbusData()
								.getBusEarthC());
						if (avlDataIo.getModbusData().getBusL1W() != null)
							ioevent.put("busL1W", avlDataIo.getModbusData()
									.getBusL1W());
						if (avlDataIo.getModbusData().getBusL2W() != null)
							ioevent.put("busL2W", avlDataIo.getModbusData()
									.getBusL2W());
						if (avlDataIo.getModbusData().getBusL3W() != null)
							ioevent.put("busL3W", avlDataIo.getModbusData()
									.getBusL3W());
						if (avlDataIo.getModbusData().getBus_P_R() != null)
							ioevent.put("bus_P_R", avlDataIo.getModbusData()
									.getBus_P_R());
						break;
					case 3:
						isThirdReq = true;
						ioevent.put("gen_tot_W", avlDataIo.getModbusData()
								.getGen_tot_W());
						ioevent.put("genL1VA", avlDataIo.getModbusData()
								.getGenL1VA());
						ioevent.put("genL2VA", avlDataIo.getModbusData()
								.getGenL2VA());
						ioevent.put("genL3VA", avlDataIo.getModbusData()
								.getGenL3VA());
						ioevent.put("gentotVA", avlDataIo.getModbusData()
								.getGentotVA());
						ioevent.put("genL1VAr", avlDataIo.getModbusData()
								.getGenL1VAr());
						ioevent.put("genL2VAr", avlDataIo.getModbusData()
								.getGenL2VAr());
						ioevent.put("genL3VAr", avlDataIo.getModbusData()
								.getGenL3VAr());
						ioevent.put("gentotVAr", avlDataIo.getModbusData()
								.getGentotVAr());
						ioevent.put("genPFL1", avlDataIo.getModbusData()
								.getGenPFL1());
						ioevent.put("genPFL2", avlDataIo.getModbusData()
								.getGenPFL2());
						ioevent.put("genPFL3", avlDataIo.getModbusData()
								.getGenPFL3());
						ioevent.put("genPFAvg", avlDataIo.getModbusData()
								.getGenPFAvg());
						ioevent.put("genFullPower", avlDataIo.getModbusData()
								.getGenFullPower());
						ioevent.put("genFullVAr", avlDataIo.getModbusData()
								.getGenFullVAr());
						ioevent.put("mains_tot_W", avlDataIo.getModbusData()
								.getMains_tot_W());
						ioevent.put("mainsL1VA", avlDataIo.getModbusData()
								.getMainsL1VA());
						ioevent.put("mainsL2VA", avlDataIo.getModbusData()
								.getMainsL2VA());
						ioevent.put("mainsL3VA", avlDataIo.getModbusData()
								.getMainsL3VA());
						ioevent.put("mainstotVA", avlDataIo.getModbusData()
								.getMainstotVA());
						ioevent.put("mainsL1VAr", avlDataIo.getModbusData()
								.getMainsL1VAr());
						ioevent.put("mainsL2VAr", avlDataIo.getModbusData()
								.getMainsL2VAr());
						ioevent.put("mainsL3VAr", avlDataIo.getModbusData()
								.getMainsL3VAr());
						ioevent.put("mainstotVAr", avlDataIo.getModbusData()
								.getMainstotVAr());
						ioevent.put("mainsPFL1", avlDataIo.getModbusData()
								.getMainsPFL1());
						ioevent.put("mainsPFL2", avlDataIo.getModbusData()
								.getMainsPFL2());
						ioevent.put("mainsPFL3", avlDataIo.getModbusData()
								.getMainsPFL3());
						ioevent.put("mainsPFAvg", avlDataIo.getModbusData()
								.getMainsPFAvg());
						break;
					case 4:
						isFourthReq = true;
						ioevent.put("genL1lag", avlDataIo.getModbusData()
								.getGenL1lag());
						ioevent.put("genL2lag", avlDataIo.getModbusData()
								.getGenL2lag());
						ioevent.put("genL3lag", avlDataIo.getModbusData()
								.getGenL3lag());
						ioevent.put("gentotlag", avlDataIo.getModbusData()
								.getGentotlag());
						ioevent.put("genL1FullPower", avlDataIo.getModbusData()
								.getGenL1FullPower());
						ioevent.put("genL2FullPower", avlDataIo.getModbusData()
								.getGenL2FullPower());
						ioevent.put("genL3FullPower", avlDataIo.getModbusData()
								.getGenL3FullPower());
						break;
					case 5:
						isFifthReq = true;
						ioevent.put("currentTime", avlDataIo.getModbusData()
								.getCurrentTime());
						ioevent.put("timeToNextEngineMainatenance", avlDataIo
								.getModbusData()
								.getTimeToNextEngineMainatenance());
						ioevent.put("timeOfNextEngineMainatenance", avlDataIo
								.getModbusData()
								.getTimeOfNextEngineMainatenance());
						ioevent.put("engineRunTime", avlDataIo.getModbusData()
								.getEngineRunTime());
						ioevent.put("genPositiveKWH", avlDataIo.getModbusData()
								.getGenPositiveKWH());
						ioevent.put("genNegativeKWH", avlDataIo.getModbusData()
								.getGenNegativeKWH());
						ioevent.put("genKVAH", avlDataIo.getModbusData()
								.getGenKVAH());
						ioevent.put("genKVArH", avlDataIo.getModbusData()
								.getGenKVArH());
						ioevent.put("numberOfStart", avlDataIo.getModbusData()
								.getNumberOfStart());
						ioevent.put("mainsPositiveKWH", avlDataIo
								.getModbusData().getMainsPositiveKWH());
						ioevent.put("mainsNegativeKWH", avlDataIo
								.getModbusData().getMainsNegativeKWH());
						ioevent.put("mainsKVAH", avlDataIo.getModbusData()
								.getMainsKVAH());
						ioevent.put("mainsKVArH", avlDataIo.getModbusData()
								.getMainsKVArH());
						ioevent.put("busPositiveKWH", avlDataIo.getModbusData()
								.getBusPositiveKWH());
						ioevent.put("busNegativeKWH", avlDataIo.getModbusData()
								.getBusNegativeKWH());
						ioevent.put("busKVAH", avlDataIo.getModbusData()
								.getBusKVAH());
						ioevent.put("busKVArH", avlDataIo.getModbusData()
								.getBusKVArH());
						ioevent.put("fuelUsed", avlDataIo.getModbusData()
								.getFuelUsed());
						ioevent.put("maxPositiveMains_ROCOF", avlDataIo
								.getModbusData().getMaxPositiveMains_ROCOF());
						ioevent.put("maxNegativeMains_ROCOF", avlDataIo
								.getModbusData().getMaxNegativeMains_ROCOF());
						ioevent.put("maxPositiveMains_Vector", avlDataIo
								.getModbusData().getMaxPositiveMains_Vector());
						ioevent.put("maxNegativeMains_Vector", avlDataIo
								.getModbusData().getMaxNegativeMains_Vector());
						ioevent.put(
								"timeToNextEngineMainatenanceAlerm1",
								avlDataIo
										.getModbusData()
										.getTimeToNextEngineMainatenanceAlerm1());
						ioevent.put(
								"timeOfNextEngineMainatenanceAlerm1",
								avlDataIo
										.getModbusData()
										.getTimeOfNextEngineMainatenanceAlerm1());
						ioevent.put(
								"timeToNextEngineMainatenanceAlerm2",
								avlDataIo
										.getModbusData()
										.getTimeToNextEngineMainatenanceAlerm2());
						ioevent.put(
								"timeOfNextEngineMainatenanceAlerm2",
								avlDataIo
										.getModbusData()
										.getTimeOfNextEngineMainatenanceAlerm2());
						ioevent.put(
								"timeToNextEngineMainatenanceAlerm3",
								avlDataIo
										.getModbusData()
										.getTimeToNextEngineMainatenanceAlerm3());
						break;
					case 6:
						isSixthReq = true;
						ioevent.put("alarm", avlDataIo.getModbusData()
								.getAlarm());
						break;
					case 7:
						isSeventhReq = true;
						break;
					}

				}
				vehicleEvent.setIoevent(ioevent.toString());
				avlDataMap.get(vehicle.getVin()).clear();
				if (!(isFirstReq && isSecondReq && isThirdReq && isFourthReq
						&& isFifthReq && isSixthReq && isSeventhReq))
					vehicleEvent.setIoevent("{}");
				// if (avlDataMap.get(vehicle.getVin()).get(0).getModbusData()
				// .getSpeed() == 0)
				// vehicleEvent.setEngine(false);
				// else
				// vehicleEvent.setEngine(true);

			} else {
				LOGGER.info("Return vin:" + vehicle.getVin());
				LOGGER.info("Map Size: "
						+ avlDataMap.get(vehicle.getVin()).size());
				return null;
			}
		} catch (Exception e) {
			LOGGER.error("DeepSeaDeviceProtocolHandler: PreparevehicleEvents:"
					+ e);
			e.printStackTrace();
		}
		return vehicleEvent;
	}

	public float distance(float lat1, float lng1, float lat2, float lng2) {
		double earthRadius = 6371000; // meters
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		float dist = (float) (earthRadius * c);

		return dist;
	}

	public String sendCommand(String command, Socket DeepseaDeviceSocket) {
		String result = null;
		LOGGER.info("Entered send command:" + command);
		try {
			DataOutputStream out = new DataOutputStream(
					DeepseaDeviceSocket.getOutputStream());
			String type = command.split(",")[0];
			VehicleComposite vehicleComposite = fleetTrackingDeviceListenerBO
					.getVehicle(command.split(",")[1]);
			LOGGER.info("type:" + type);
			if (type.split("_")[0].equalsIgnoreCase("cutOffEngine")) {
				String cutOffCommand = "<LOCK," + command.split(",")[1] + ";";
				LOGGER.info(cutOffCommand);
				out.write(cutOffCommand.getBytes(), 0, cutOffCommand.length());
				int i = 0;
				while (i < 120) {
					if (commandStatus != null) {
						if (commandStatus.startsWith("ACK")) {
							result = "cutOffEngine OK";
							fleetTrackingDeviceListenerBO.updateLockStatus(
									vehicleComposite.getVehicle(), 1);
						} else if (commandStatus.startsWith("NACK"))
							result = null;
						else if (commandStatus
								.equalsIgnoreCase("Invalid Request"))
							result = null;
						else
							result = commandStatus;
						commandStatus = null;
						break;
					} else {
						Thread.sleep(1000);
					}
					i++;
				}
			} else if (type.split("_")[0].equalsIgnoreCase("restoreEngine")) {
				String cutOffCommand = "<UNLOCK," + command.split(",")[1] + ";";
				LOGGER.info(cutOffCommand);
				out.write(cutOffCommand.getBytes(), 0, cutOffCommand.length());
				int i = 0;
				while (i < 120) {
					if (commandStatus != null) {
						if (commandStatus.startsWith("ACK")) {
							result = "restoreEngine OK";
							fleetTrackingDeviceListenerBO.updateLockStatus(
									vehicleComposite.getVehicle(), 0);
						} else if (commandStatus.startsWith("NACK"))
							result = null;
						else if (commandStatus
								.equalsIgnoreCase("Invalid Request"))
							result = null;
						else
							result = commandStatus;
						commandStatus = null;
						break;
					} else {
						Thread.sleep(1000);
					}
					i++;
				}
			}
		} catch (Exception e) {
			LOGGER.error("SendCommand : " + e);
			e.printStackTrace();
		}
		return result;
	}
}