package com.eiw.device.handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.SocketTimeoutException;
import java.util.Date;

import org.jboss.logging.Logger;

import com.eiw.device.Prime07.Prime07ByteWrapper;
import com.eiw.device.ejb.FleetTrackingDeviceListenerBORemote;
import com.eiw.device.ejb.VehicleComposite;
import com.eiw.device.handler.method.SKTHandlerMethods;
import com.eiw.device.meitrack.Position;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.bo.BOFactory;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.Vehicleevent;
import com.eiw.server.fleettrackingpu.VehicleeventId;

public class Prime07DeviceHandler extends DeviceHandler {
	private static final Logger LOGGER = Logger.getLogger("listener");
	FleetTrackingDeviceListenerBORemote fleetTrackingDeviceListenerBO = BOFactory
			.getFleetTrackingDeviceListenerBORemote();
	private SKTHandlerMethods sktHandlerMethods;

	@Override
	public void handleDevice() {
		LOGGER.info("Entered Prime07 five mins Handle Device:" + new Date());
		sktHandlerMethods = new SKTHandlerMethods();
		DataInputStream clientSocketDis = null;
		DataOutputStream dos = null;
		try {
			clientSocket.setSoTimeout(1500000);
			clientSocketDis = new DataInputStream(clientSocket.getInputStream());
			Prime07ByteWrapper data = new Prime07ByteWrapper(clientSocketDis);
			data.unwrapDataFromStream();
			LOGGER.info("Test 1 Prime07");
			String imeiNo = data.getImei();
			VehicleComposite vehicleComposite = fleetTrackingDeviceListenerBO
					.getVehicle(imeiNo);
			if (vehicleComposite == null) {
				LOGGER.error("Prime07DeviceHandler: handleDevice: Received IMEI No is invalid... returning... "
						+ imeiNo);
				return;
			}
			super.deviceImei = imeiNo;
			insertService(data, vehicleComposite, fleetTrackingDeviceListenerBO);
			while (true) {
				Prime07ByteWrapper rawData = new Prime07ByteWrapper(
						clientSocketDis);
				rawData.unwrapDataFromStream();
				insertService(rawData, vehicleComposite,
						fleetTrackingDeviceListenerBO);
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			LOGGER.error("SocketTimeoutException while receiving the Message "
					+ e);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Prime07DeviceHandler:Exception while receiving the Message "
					+ e);
		} finally {
			cleanUpSockets(clientSocket, clientSocketDis, dos);
			LOGGER.info("DeviceCommunicatorThread:DeviceCommunicator Completed");
		}
		LOGGER.info("Prime07DeviceHandler: Ended successfully::: ");
	}

	private void insertService(Prime07ByteWrapper rawData,
			VehicleComposite vehicleComposite,
			FleetTrackingDeviceListenerBORemote fleetTrackingDeviceListenerBO) {
		try {
			Vehicle vehicle = vehicleComposite.getVehicle();
			Vehicleevent vehicleEvent = prepareVehicleEvents(vehicle, rawData,
					fleetTrackingDeviceListenerBO, vehicleComposite);
			LOGGER.info("Prime07DeviceProtocolHandler: handleDevice: VehicleEvents prepared for vin="
					+ vehicleEvent.getId().getVin() + " at " + new Date());
			/**
			 * For SKT
			 */
			Position position = getPositionObject(vehicleEvent, rawData);
			String imeiNo = String.valueOf(position.getDeviceId());
			LOGGER.info("Prime07DeviceHandler : Time:: " + position.getTime()
					+ " and Prime07Imei = " + position.getDeviceId()
					+ " and RFID = " + position.getRfid() + " and Company = "
					+ vehicleComposite.getVehicle().getCompanyId()
					+ " and Branch = "
					+ vehicleComposite.getVehicle().getBranchId());
			sktHandlerMethods.persistEventAndGenerateAlert(position,
					vehicleComposite, imeiNo, "Prime07", vehicleEvent);
		} catch (Exception e) {
			LOGGER.error("Exception while persisting data :: " + e);
		}
	}

	/**
	 * This method for set data in Position Object from VehicleEvents object for
	 * SKT
	 * 
	 * @param vehicleEvent
	 * @param rawData
	 * @return position
	 */
	private Position getPositionObject(Vehicleevent vehicleEvent,
			Prime07ByteWrapper rawData) {
		Position position = new Position();
		position.setDeviceId(Long.valueOf(deviceImei));
		position.setRfid("1");
		position.setTime(rawData.getPrime07GpsData().getDateTime());
		position.setLatitude(Double.valueOf(vehicleEvent.getLatitude()));
		position.setLongitude(Double.valueOf(vehicleEvent.getLongitude()));
		position.setSpeed(Double.valueOf(vehicleEvent.getSpeed()));
		return position;
	}

	private Vehicleevent prepareVehicleEvents(Vehicle vehicle,
			Prime07ByteWrapper avlData,
			FleetTrackingDeviceListenerBORemote entityManagerService,
			VehicleComposite vehicleComposite) {
		Vehicleevent vehicleEvent = new Vehicleevent();
		try {
			String region = vehicleComposite.getTimeZoneRegion();
			VehicleeventId vehicleeventId = new VehicleeventId();

			vehicleeventId.setEventTimeStamp(TimeZoneUtil.getDateTimeZone(
					avlData.getPrime07GpsData().getDateTime(), region));
			
			vehicleEvent.setServerTimeStamp(TimeZoneUtil.getDateTimeZone(
					new Date(), region));
			double latitude = avlData.getPrime07GpsData().getLatitude();
			latitude = (double) Math.round(latitude * 10000) / 10000;
			double longitude = avlData.getPrime07GpsData().getLongitude();
			longitude = (double) Math.round(longitude * 10000) / 10000;

			vehicleEvent.setLongitude(longitude);
			vehicleEvent.setLatitude(latitude);
			vehicleEvent.setSpeed(avlData.getPrime07GpsData().getSpeed());
			vehicleeventId.setVin(vehicle.getVin());
			vehicleEvent.setId(vehicleeventId);
			vehicleEvent.setEngine(avlData.getPrime07GpsData().getAcc());
			vehicleEvent.setDi1(avlData.getPrime07GpsData().getAcc() ? 1 : 0);
			vehicleEvent.setSpeed(avlData.getPrime07GpsData().getSpeed());
			vehicleEvent.setAi1(avlData.getPrime07GpsData().getAc());
			vehicleEvent.setDirection(avlData.getPrime07GpsData()
					.getDirection());
			vehicleEvent.setOdometer((long) avlData.getPrime07GpsData()
					.getDirection());

		} catch (Exception e) {
			LOGGER.error("Prime07DeviceProtocolHandler: PreparevehicleEvents:"
					+ e);
		}
		return vehicleEvent;
	}
}
