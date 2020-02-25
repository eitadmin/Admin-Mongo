package com.eiw.device.handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jboss.logging.Logger;

import com.eiw.device.ejb.FleetTrackingDeviceListenerBORemote;
import com.eiw.device.ejb.VehicleComposite;
import com.eiw.device.tzone.TzoneAVL09Data;
import com.eiw.device.tzone.TzoneByteWrapper;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.bo.BOFactory;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.Vehicleevent;
import com.eiw.server.fleettrackingpu.VehicleeventId;

public class TZoneDeviceHandler extends DeviceHandler {

	private static final Logger LOGGER = Logger.getLogger("listener");
	FleetTrackingDeviceListenerBORemote entityManagerService;

	public void handleDevice() {
		LOGGER.info("TZONEDeviceProtocolHandler: handleDevice: Entered Simulator five mins Handle Device:"
				+ new Date());
		entityManagerService = BOFactory
				.getFleetTrackingDeviceListenerBORemote();
		DataInputStream clientSocketDis = null;
		DataOutputStream clientSocketDos = null;
		try {
			clientSocket.setSoTimeout(300000);
			LOGGER.info("TZONEDeviceProtocolHandler: handleDevice:Inside DOS try block");
			clientSocketDis = new DataInputStream(clientSocket.getInputStream());

			List<byte[]> datasList = TzoneByteWrapper
					.unwrapFromStream(clientSocketDis);
			for (byte[] datas : datasList) {
				if (Arrays.equals(datas, TzoneByteWrapper.ERROR)) {
					LOGGER.error("TZONEDeviceProtocolHandler: handleDevice: Received invalid datas... returning... "
							+ TzoneByteWrapper.asciiBytesToString(datas));
					continue;
				}
				String str = TzoneByteWrapper.asciiBytesToString(datas);
				LOGGER.info("TZONEDeviceProtocolHandler: handleDevice: &&&"
						+ str);

				clientSocketDos = new DataOutputStream(
						clientSocket.getOutputStream());

				TzoneAVL09Data avlDatas = new TzoneAVL09Data(str);

				// To get Imei No
				String imeiNo = avlDatas.getImei();
				VehicleComposite vehicleComposite = entityManagerService
						.getVehicle(imeiNo);

				if (vehicleComposite == null) {
					LOGGER.error("TZONEDeviceProtocolHandler: handleDevice: Received IMEI No is invalid... returning... ");
					clientSocketDos.writeBytes("$ACKPOS,false");
					LOGGER.info("TZONEDeviceProtocolHandler: handleDevice: writeBoolean(false).........");
					return;
				}
				startProcessingDatas(vehicleComposite, avlDatas,
						entityManagerService);
			}
		} catch (IOException e) {
			LOGGER.error("TZONEDeviceProtocolHandler occured", e);
		} finally {
			cleanUpSockets(clientSocket, clientSocketDis, clientSocketDos);
			LOGGER.info("DeviceCommunicatorThread:DeviceCommunicator Completed");
		}

	}

	private List<Vehicleevent> prepareVehicleEvents(Vehicle vehicle,
			TzoneAVL09Data avlDatas,
			FleetTrackingDeviceListenerBORemote entityManagerService) {
		List<Vehicleevent> vehicleEvents = new ArrayList<Vehicleevent>();
		try {
			String region = entityManagerService.getTimeZoneRegion(vehicle
					.getVin());

			Vehicleevent vehicleEvent = new Vehicleevent();
			VehicleeventId vehicleeventId = new VehicleeventId();
			vehicleeventId.setEventTimeStamp(TimeZoneUtil.getDateTimeZone(
					avlDatas.getEventTimeStamp(), region));
			vehicleEvent.setServerTimeStamp(TimeZoneUtil.getDateInTimeZone());
			vehicleEvent.setLongitude(avlDatas.getLongitude());
			vehicleEvent.setLatitude(avlDatas.getLatitude());
			vehicleEvent.setSpeed(((Double) avlDatas.getSpeed()).intValue());
			vehicleeventId.setVin(vehicle.getVin());
			vehicleEvent.setId(vehicleeventId);
			vehicleEvent.setEngine("1".equalsIgnoreCase(String.valueOf(avlDatas
					.getDI1())) ? true : false);
			vehicleEvent.setDi1(avlDatas.getDI1());
			vehicleEvent.setDi2(avlDatas.getDI2());
			vehicleEvent.setOdometer(((Double) avlDatas.getOdometer())
					.longValue());
			vehicleEvent.setAi1(avlDatas.getAI1());
			vehicleEvent.setAi2(avlDatas.getAI2());
			vehicleEvent.setTempSensor1(((Float) avlDatas.getTemp())
					.longValue());
			vehicleEvent
					.setBattery(((Float) avlDatas.getBattery()).longValue());
			vehicleEvents.add(vehicleEvent);

		} catch (Exception e) {
			LOGGER.error("TZONEDeviceProtocolHandler: PreparevehicleEvents:", e);
		}
		return vehicleEvents;
	}

	private void startProcessingDatas(VehicleComposite vehicleComposite,
			TzoneAVL09Data avlDatas,
			FleetTrackingDeviceListenerBORemote entityManagerService) {
		try {
			Vehicle vehicle = vehicleComposite.getVehicle();
			List<Vehicleevent> vehicleEvents = prepareVehicleEvents(vehicle,
					avlDatas, entityManagerService);
			LOGGER.info("TZONEDeviceProtocolHandler: handleDevice: VehicleEvents prepared for vin="
					+ vehicleEvents.get(0).getId().getVin()
					+ " at "
					+ new Date());
			entityManagerService.persistDeviceData(vehicleEvents,
					vehicleComposite);
		} catch (Exception e) {
			LOGGER.error(
					"TZONEDeviceProtocolHandler: handleDevice: Exception in device communicator start method ",
					e);
		}
	}
}