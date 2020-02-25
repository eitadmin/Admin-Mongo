package com.eiw.device.handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jboss.logging.Logger;

import com.eiw.device.ejb.FleetTrackingDeviceListenerBORemote;
import com.eiw.device.ejb.VehicleComposite;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.bo.BOFactory;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.Vehicleevent;
import com.eiw.server.fleettrackingpu.VehicleeventId;

public class ItracDeviceHandlerForGPSWatch extends DeviceHandler {

	private static final Logger LOGGER = Logger.getLogger("listener");

	public void handleDevice() {
		LOGGER.info("handleDevice: Entered ItracDeviceHandlerForGPSWatch Handle Device:"
				+ new Date());
		FleetTrackingDeviceListenerBORemote entityManagerService = BOFactory
				.getFleetTrackingDeviceListenerBORemote();
		DataInputStream clientSocketDis = null;
		DataOutputStream dos = null;
		try {
			this.clientSocket.setSoTimeout(1500000);
			clientSocketDis = new DataInputStream(
					this.clientSocket.getInputStream());

			while (true) {
				LOGGER.info("Entered loop to read data recursively");
				if (clientSocketDis.readByte() != 0x23) {
					break;
				}
				String deviceData = clientSocketDis.readLine();
				LOGGER.info("Data received from device :: " + deviceData);
				String[] rawData = deviceData.split("#");
				String imeiNo = rawData[0];
				VehicleComposite vehicleComposite = entityManagerService
						.getVehicle(imeiNo);

				if (vehicleComposite == null) {
					LOGGER.error("ItracDeviceHandlerForGPSWatch: handleDevice: Received IMEI No is invalid... returning... "
							+ imeiNo);
					return;
				}

				insertService(vehicleComposite, rawData, entityManagerService,
						imeiNo);

			}
		} catch (Exception e) {
			LOGGER.error("ItracDeviceHandlerForGPSWatch: Close client socket"
					+ e);
		} finally {
			cleanUpSockets(this.clientSocket, clientSocketDis, dos);
			LOGGER.info("DeviceCommunicatorThread:DeviceCommunicator Completed");
		}
		LOGGER.info("ItracDeviceHandlerForGPSWatch: Ended successfully::: ");
	}

	private void insertService(VehicleComposite vehicleComposite,
			String[] rawData,
			FleetTrackingDeviceListenerBORemote fleetTrackingDeviceListenerBO,
			String imeiNo) {
		try {
			List<Vehicleevent> vehicleEvents = prepareVehicleEvents(
					vehicleComposite.getVehicle(), rawData,
					fleetTrackingDeviceListenerBO);
			if (vehicleEvents == null) {
				LOGGER.error("Vehicle Events Null ");
				return;
			}
			LOGGER.info("VehicleEvents prepared for vin="
					+ vehicleEvents.get(0).getId().getVin() + " at "
					+ new Date());
			fleetTrackingDeviceListenerBO.persistDeviceData(vehicleEvents,
					vehicleComposite);

			LOGGER.info("VehicleEvents persisted");
		} catch (Exception e) {
			LOGGER.error("Exception while persisting data :: " + e);
		}
	}

	private List<Vehicleevent> prepareVehicleEvents(Vehicle vehicle,
			String[] rawData,
			FleetTrackingDeviceListenerBORemote entityManagerService) {
		List<Vehicleevent> vehicleEvents = new ArrayList<Vehicleevent>();
		try {
			String region = entityManagerService.getTimeZoneRegion(vehicle
					.getVin());
			if (!rawData[1].equalsIgnoreCase("")) {
				String userName = rawData[1];
			}
			String status = rawData[2];
			String password = rawData[3];
			String dataType = rawData[4];
			System.out.println("Decoded Data :: " + status + " " + dataType);
			VehicleeventId vehicleeventId = new VehicleeventId();
			String baseStationInfo = rawData[6] + " " + rawData[7];
			System.out.println("Decoded Data :: " + baseStationInfo);
			if (dataType.equalsIgnoreCase("AUT")) {
				int dataVolume = Integer.valueOf(rawData[5]);
				int i = 6;
				for (int j = 1; j <= dataVolume; j++) {
					Vehicleevent vehicleEvent = new Vehicleevent();
					if (rawData[i + 1].equalsIgnoreCase("V")) {
						String[] gprmc = rawData[i + 2].split(",");
						double longitude = Double.valueOf(
								Float.parseFloat(gprmc[0]) / 100).intValue()
								+ (Float.parseFloat(gprmc[0]) - Double.valueOf(
										Float.parseFloat(gprmc[0]) / 100)
										.intValue() * 100) / 60;
						double latitude = Double.valueOf(
								Float.parseFloat(gprmc[2]) / 100).intValue()
								+ (Float.parseFloat(gprmc[2]) - Double.valueOf(
										Float.parseFloat(gprmc[2]) / 100)
										.intValue() * 100) / 60;
						if (!gprmc[1].equalsIgnoreCase("E")) {
							longitude = -longitude;
						}
						if (!gprmc[3].equalsIgnoreCase("N")) {
							latitude = -latitude;
						}
						int speed = Float.valueOf(gprmc[4]).intValue();
						int direction = Integer.parseInt(gprmc[5]);
						System.out.println("Lat :: " + latitude + " Long :: "
								+ longitude + " Speed :: " + speed
								+ " Direction :: " + direction);
						vehicleEvent.setLongitude(longitude);
						vehicleEvent.setLatitude(latitude);
						vehicleEvent.setSpeed(speed);

						Calendar cal = Calendar.getInstance();
						cal.set(Calendar.YEAR,
								2000 + Integer.parseInt(rawData[i + 3]
										.substring(4, 6).trim()));
						cal.set(Calendar.MONTH, Integer.parseInt(rawData[i + 3]
								.substring(2, 4).trim()) - 1);
						cal.set(Calendar.DAY_OF_MONTH,
								Integer.parseInt(rawData[i + 3].substring(0, 2)
										.trim()));
						cal.set(Calendar.HOUR_OF_DAY,
								Integer.parseInt(rawData[i + 4].substring(0, 2)
										.trim()));
						cal.set(Calendar.MINUTE,
								Integer.parseInt(rawData[i + 4].substring(2, 4)
										.trim()));
						cal.set(Calendar.SECOND,
								Integer.parseInt(rawData[i + 4].substring(4, 6)
										.trim()));
						Date eventTimeStamp = cal.getTime();
						System.out.println("Date " + eventTimeStamp);
						vehicleeventId.setVin(vehicle.getVin());
						vehicleEvent.setId(vehicleeventId);
						vehicleeventId.setEventTimeStamp(TimeZoneUtil
								.getDateTimeZone(eventTimeStamp, region));
						vehicleEvent.setServerTimeStamp(TimeZoneUtil
								.getDateInTimeZone());
						vehicleEvent.setEngine(true);
						vehicleEvent.setOdometer(0L);
						vehicleEvents.add(vehicleEvent);
						i += 5;
					}
				}
			}

		} catch (Exception e) {
			LOGGER.error(
					"ItracDeviceHandlerForGPSWatch: PreparevehicleEvents:", e);
		}
		return vehicleEvents;
	}
}
