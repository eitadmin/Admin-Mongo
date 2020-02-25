package com.eiw.device.handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;
import org.json.JSONObject;

import com.eiw.device.Auxus.AuxusByteWrapper;
import com.eiw.device.ejb.FleetTrackingDeviceListenerBORemote;
import com.eiw.device.ejb.VehicleComposite;
import com.eiw.device.handler.method.SKTHandlerMethods;
import com.eiw.device.meitrack.Position;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.bo.BOFactory;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.Vehicleevent;
import com.eiw.server.fleettrackingpu.VehicleeventId;

public class AuxusDeviceHandler extends DeviceHandler {
	private static final Logger LOGGER = Logger.getLogger("listener");
	FleetTrackingDeviceListenerBORemote fleetTrackingDeviceListenerBO = BOFactory
			.getFleetTrackingDeviceListenerBORemote();
	public static String commandStatus;
	private SKTHandlerMethods sktHandlerMethods;

	@Override
	public void handleDevice() {
		LOGGER.info("Entered Auxus five mins Handle Device:" + new Date());
		sktHandlerMethods = new SKTHandlerMethods();
		DataInputStream clientSocketDis = null;
		DataOutputStream dos = null;
		try {
			clientSocket.setSoTimeout(1500000);
			clientSocketDis = new DataInputStream(clientSocket.getInputStream());
			AuxusByteWrapper data = new AuxusByteWrapper(clientSocketDis);
			data.unwrapDataFromStream();
			LOGGER.info("Test 1 Auxus");
			String imeiNo = data.getImei();
			VehicleComposite vehicleComposite = fleetTrackingDeviceListenerBO
					.getVehicle(imeiNo);
			if (vehicleComposite == null) {
				LOGGER.error("AuxusDeviceHandler: handleDevice: Received IMEI No is invalid... returning... "
						+ imeiNo);
				return;
			}
			super.deviceImei = imeiNo;
			// LOGGER.info("Test 2:" + super.deviceImei);
			insertService(data, vehicleComposite, fleetTrackingDeviceListenerBO);
			while (true) {
				AuxusByteWrapper rawData = new AuxusByteWrapper(clientSocketDis);
				rawData.unwrapDataFromStream();
				insertService(rawData, vehicleComposite,
						fleetTrackingDeviceListenerBO);
			}
		} catch (SocketTimeoutException e) {
			LOGGER.error("SocketTimeoutException while receiving the Message "
					+ e);
		} catch (Exception e) {
			LOGGER.error("AuxusDeviceHandler:Exception while receiving the Message "
					+ e);
		} finally {
			cleanUpSockets(clientSocket, clientSocketDis, dos);
			LOGGER.info("DeviceCommunicatorThread:DeviceCommunicator Completed");
		}
		LOGGER.info("AuxusDeviceHandler: Ended successfully::: ");
	}

	private void insertService(AuxusByteWrapper rawData,
			VehicleComposite vehicleComposite,
			FleetTrackingDeviceListenerBORemote fleetTrackingDeviceListenerBO) {
		try {
			Vehicle vehicle = vehicleComposite.getVehicle();
			Vehicleevent vehicleEvent = prepareVehicleEvents(vehicle, rawData,
					fleetTrackingDeviceListenerBO);
			if (vehicleEvent != null) {
				LOGGER.info("AuxusDeviceProtocolHandler: handleDevice: VehicleEvents prepared for vin="
						+ vehicleEvent.getId().getVin() + " at " + new Date());
				/**
				 * For SKT
				 */
				Position position = getPositionObject(vehicleEvent);
				String imeiNo = String.valueOf(position.getDeviceId());
				LOGGER.info("AuxusDeviceHandler : Time:: " + position.getTime()
						+ " and AuxusImei = " + position.getDeviceId()
						+ " and RFID = " + position.getRfid()
						+ " and Company = "
						+ vehicleComposite.getVehicle().getCompanyId()
						+ " and Branch = "
						+ vehicleComposite.getVehicle().getBranchId());
				sktHandlerMethods.persistEventAndGenerateAlert(position,
						vehicleComposite, imeiNo, "Auxus", vehicleEvent);
			}
		} catch (Exception e) {
			LOGGER.error("Exception while persisting data :: " + e);
			e.printStackTrace();
		}
	}

	/**
	 * This method for set data in Position Object from VehicleEvents object for
	 * SKT
	 * 
	 * @param vehicleEvent
	 * @return position
	 */
	private Position getPositionObject(Vehicleevent vehicleEvent) {
		Position position = new Position();
		position.setDeviceId(Long.valueOf(deviceImei));
		if (vehicleEvent.getTags() != null)
			position.setRfid(vehicleEvent.getTags());
		else
			position.setRfid("1");
		position.setTime(vehicleEvent.getId().getEventTimeStamp());
		position.setLatitude(Double.valueOf(vehicleEvent.getLatitude()));
		position.setLongitude(Double.valueOf(vehicleEvent.getLongitude()));
		position.setSpeed(Double.valueOf(vehicleEvent.getSpeed()));
		return position;
	}

	private Vehicleevent prepareVehicleEvents(Vehicle vehicle,
			AuxusByteWrapper rawData,
			FleetTrackingDeviceListenerBORemote entityManagerService) {
		LOGGER.info("AuxusDeviceProtocolHandler Entered prepareVehicleEvents");
		Vehicleevent vehicleEvent = new Vehicleevent();
		try {
			String region = entityManagerService.getTimeZoneRegion(vehicle
					.getVin());
			Vehicleevent ve = entityManagerService.getPrevVe(vehicle.getVin());
			VehicleeventId vehicleeventId = new VehicleeventId();
			if (rawData.getAuxusGpsData().getDateTime() == null)
				return null;
			vehicleeventId.setEventTimeStamp(TimeZoneUtil.getDateTimeZone(
					rawData.getAuxusGpsData().getDateTime(), region));
			vehicleEvent.setServerTimeStamp(TimeZoneUtil.getDateInTimeZone());
			double latitude = rawData.getAuxusGpsData().getLatitude();
			latitude = (double) Math.round(latitude * 10000) / 10000;
			double longitude = rawData.getAuxusGpsData().getLongitude();
			longitude = (double) Math.round(longitude * 10000) / 10000;

			vehicleEvent.setLongitude(longitude);
			vehicleEvent.setLatitude(latitude);
			vehicleEvent.setSpeed(rawData.getAuxusGpsData().getSpeed());
			vehicleeventId.setVin(vehicle.getVin());
			vehicleEvent.setId(vehicleeventId);
			vehicleEvent.setEngine(true);
			if (ve != null && vehicleEvent.getEngine()
					&& vehicleEvent.getSpeed() != 0) {
				double odometer = distanceMatrix(ve.getLatitude(),
						ve.getLongitude(), vehicleEvent.getLatitude(),
						vehicleEvent.getLongitude());
				vehicleEvent.setOdometer((long) odometer);
			}
			vehicleEvent.setDirection(rawData.getAuxusGpsData().getDirection());
			if (rawData.getAuxusGpsData().getBattery() != null) {
				vehicleEvent.setBattery(rawData.getAuxusGpsData().getBattery());
			}

		} catch (Exception e) {
			LOGGER.error("AuxusDeviceProtocolHandler: PreparevehicleEvents:"
					+ e);
			e.printStackTrace();
		}
		return vehicleEvent;
	}

	public double distance(double lat1, double lng1, double lat2, double lng2) {
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

	public double distanceMatrix(double lat1, double lng1, double lat2, double lng2) {
		try {
			URL url1 = new URL(
					"https://maps.googleapis.com/maps/api/distancematrix/json?origins="
							+ lat1
							+ ","
							+ lng1
							+ "&destinations="
							+ lat2
							+ ","
							+ lng2
							+ "&mode=driving&language=en-EN&sensor=false&key=AIzaSyAb94T4OXJmVss7ArXmkKHb11PQ0fw6lyA");
			URLConnection con = url1.openConnection();
			InputStream in = con.getInputStream();
			String encoding = "UTF-8";
			String statusText = IOUtils.toString(in, encoding);
			if (statusText == null || !statusText.contains("distance")) {
				return distance(lat1, lng1, lat2, lng2);
			}
			JSONObject json = new JSONObject(statusText);
			String rows = json.getString("rows");
			JSONObject rowObject = new JSONObject(rows.substring(1,
					rows.length() - 1));
			String elements = rowObject.getString("elements");
			JSONObject elementsObject = new JSONObject(elements.substring(1,
					elements.length() - 1));
			String distance = elementsObject.getString("distance");
			JSONObject distanceObject = new JSONObject(distance);
			return Float.valueOf(distanceObject.getString("value"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return distance(lat1, lng1, lat2, lng2);
	}
}
