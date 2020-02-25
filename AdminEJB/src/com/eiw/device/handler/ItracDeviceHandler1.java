package com.eiw.device.handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jboss.logging.Logger;

import com.eiw.device.ejb.FleetTrackingDeviceListenerBORemote;
import com.eiw.device.ejb.VehicleComposite;
import com.eiw.device.itrac.ItracProtocolDecoder1;
import com.eiw.device.itrac.Position;
import com.eiw.device.listener.ListenerStarter;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.bo.BOFactory;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.Vehicleevent;
import com.eiw.server.fleettrackingpu.VehicleeventId;

public class ItracDeviceHandler1 extends DeviceHandler {

	private static final Logger LOGGER = Logger.getLogger("listener");
	private static String commandStatus;

	@Override
	protected void handleDevice() {
		LOGGER.info("Entered ItracBasic five mins Handle Device:" + new Date());
		FleetTrackingDeviceListenerBORemote fleetTrackingDeviceListenerBO = BOFactory
				.getFleetTrackingDeviceListenerBORemote();
		DataInputStream clientSocketDis = null;
		DataOutputStream dos = null;
		try {
			clientSocket.setSoTimeout(1500000);
			clientSocketDis = new DataInputStream(clientSocket.getInputStream());
			ItracProtocolDecoder1 itracProtocolDecoder1 = new ItracProtocolDecoder1();
			Position position = itracProtocolDecoder1.decode(clientSocketDis);
			LOGGER.info("Decoded data ::: " + position);

			/* Obtaining IMEI NO. from rawData */
			String imeiNo = String.valueOf(position.getDeviceId());
			VehicleComposite vehicleComposite = fleetTrackingDeviceListenerBO
					.getVehicle(imeiNo);

			if (vehicleComposite == null) {
				LOGGER.error("ItracDeviceHandler: handleDevice: Received IMEI No is invalid... returning... "
						+ imeiNo);
				return;
			}
			super.deviceImei = imeiNo;
			ListenerStarter.itracDeviceHandlerMap1.put(deviceImei, this);
			insertService(vehicleComposite, position,
					fleetTrackingDeviceListenerBO);

			while (true) {
				Position pos = itracProtocolDecoder1.decode(clientSocketDis);
				if (pos != null) {
					insertService(vehicleComposite, pos,
							fleetTrackingDeviceListenerBO);
				}
			}

		} catch (SocketTimeoutException e) {
			LOGGER.error("SocketTimeoutExceptiontion while receiving the Message "
					+ e);
		} catch (Exception e) {
			LOGGER.error("Exception while receiving the Message " + e);
		} finally {
			cleanUpSockets(clientSocket, clientSocketDis, dos);
			LOGGER.info("DeviceCommunicatorThread:DeviceCommunicator Completed");
		}
	}

	private List<Vehicleevent> prepareVehicleEvents(Vehicle vehicle,
			Position position,
			FleetTrackingDeviceListenerBORemote entityManagerService) {
		List<Vehicleevent> vehicleEvents = new ArrayList<Vehicleevent>();
		try {
			String region = entityManagerService.getTimeZoneRegion(vehicle
					.getVin());
			Vehicleevent vehicleEvent = new Vehicleevent();
			Vehicleevent ve = entityManagerService.getPrevVe(vehicle.getVin());
			if (ve != null) {
				long diffInMillisecond = (position.getTime().getTime() - ve
						.getId().getEventTimeStamp().getTime())
						/ (60 * 60 * 1000);
				long odometer = (position.getSpeed().intValue() * diffInMillisecond) * 1000;
				vehicleEvent.setOdometer(odometer);
			}
			VehicleeventId vehicleeventId = new VehicleeventId();
			vehicleeventId.setEventTimeStamp(position.getTime());
			vehicleEvent.setServerTimeStamp(TimeZoneUtil.getDateInTimeZone());
			vehicleEvent.setLongitude(position.getLongitude().doubleValue());
			vehicleEvent.setLatitude(position.getLatitude().doubleValue());
			vehicleEvent.setSpeed(position.getSpeed().intValue());
			vehicleeventId.setVin(vehicle.getVin());
			vehicleEvent.setId(vehicleeventId);
			vehicleEvent.setEngine(String.valueOf(position.getAcc())
					.equalsIgnoreCase("1") ? true : false);
			vehicleEvent.setDi1(position.getAcc());
			vehicleEvents.add(vehicleEvent);

		} catch (Exception e) {
			LOGGER.error("ItracDeviceHandler: PreparevehicleEvents:" + e);
		}
		return vehicleEvents;
	}

	private void insertService(VehicleComposite vehicleComposite,
			Position position,
			FleetTrackingDeviceListenerBORemote fleetTrackingDeviceListenerBO) {
		Vehicle vehicle = vehicleComposite.getVehicle();
		List<Vehicleevent> vehicleEvents = prepareVehicleEvents(vehicle,
				position, fleetTrackingDeviceListenerBO);
		LOGGER.info("ItracDeviceHandler: handleDevice: VehicleEvents prepared for vin="
				+ vehicleEvents.get(0).getId().getVin() + " at " + new Date());
		fleetTrackingDeviceListenerBO.persistDeviceData(vehicleEvents,
				vehicleComposite);
	}

	public String sendCommand(String imeiNo, String command,
			Socket concoxDeviceSocket) {
		String result = null;
		try {
			DataOutputStream out = new DataOutputStream(
					concoxDeviceSocket.getOutputStream());
			if (command.equalsIgnoreCase("cutOffEngine")) {
				String cutOffCommand = ItracProtocolDecoder1
						.getCutOffCommand(imeiNo);
				/* Converting response text from String to array of bytes */
				byte[] responseBytes = ItracProtocolDecoder1
						.hexStringToByteArray(cutOffCommand);
				/* Sending response message to the device */
				out.write(responseBytes, 0, responseBytes.length);
				int i = 0;
				while (i < 120) {
					if (commandStatus != null) {
						// result = ItracProtocolDecoder
						// .hexStringToASCIIString(commandStatus
						// .substring(18,
						// commandStatus.length() - 16));
						commandStatus = null;
						break;
					} else {
						Thread.sleep(1000);
					}
					i++;
				}
			} else if (command.equalsIgnoreCase("restoreEngine")) {
				String restoreCommand = ItracProtocolDecoder1
						.getRestoreCommand(imeiNo);
				/* Converting response text from String to array of bytes */
				byte[] responseBytes = ItracProtocolDecoder1
						.hexStringToByteArray(restoreCommand);
				/* Sending response message to the device */
				out.write(responseBytes, 0, responseBytes.length);
				int i = 0;
				while (i < 120) {
					if (commandStatus != null) {
						// result = ItracProtocolDecoder
						// .hexStringToASCIIString(commandStatus
						// .substring(18,
						// commandStatus.length() - 16));
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
		}
		return result;
	}
}
