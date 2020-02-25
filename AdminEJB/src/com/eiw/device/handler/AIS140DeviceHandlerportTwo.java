package com.eiw.device.handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;
import org.json.JSONObject;

import com.eiw.device.Ais140.AIS140ByteWrapper;
import com.eiw.device.ais140v1.AIS140ByteWrapperV1;
import com.eiw.device.ejb.FleetTrackingDeviceListenerBORemote;
import com.eiw.device.ejb.VehicleComposite;
import com.eiw.device.handler.method.SKTHandlerMethods;
import com.eiw.device.meitrack.Position;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.bo.BOFactory;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.Vehicleevent;
import com.eiw.server.fleettrackingpu.VehicleeventId;

public class AIS140DeviceHandlerportTwo extends DeviceHandler {
	private static final Logger LOGGER = Logger.getLogger("listener");
	FleetTrackingDeviceListenerBORemote fleetTrackingDeviceListenerBO = BOFactory
			.getFleetTrackingDeviceListenerBORemote();
	private SKTHandlerMethods sktHandlerMethods;

	@Override
	public void handleDevice() {
		LOGGER.info("Entered Ais140 5445 five mins Handle Device:" + new Date());
		sktHandlerMethods = new SKTHandlerMethods();
		DataInputStream clientSocketDis = null;
		DataOutputStream dos = null;
		try {
			clientSocket.setSoTimeout(1500000);
			clientSocketDis = new DataInputStream(clientSocket.getInputStream());
			AIS140ByteWrapperV1 data = new AIS140ByteWrapperV1(clientSocketDis);
			data.unwrapDataFromStream();
			LOGGER.error("Ais140 5445 Data = " + data.getRawData());
			LOGGER.info("Test 1 AIS140");

			while (true) {
				AIS140ByteWrapperV1 rawData = new AIS140ByteWrapperV1(
						clientSocketDis);
				rawData.unwrapDataFromStream();
				LOGGER.error("Ais140  5445 Data = " + rawData.getRawData());

			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			LOGGER.error("SocketTimeoutException while receiving the Message "
					+ e);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Ais140 5445:Exception while receiving the Message "
					+ e);
		} finally {
			cleanUpSockets(clientSocket, clientSocketDis, dos);
			LOGGER.info("DeviceCommunicatorThread:DeviceCommunicator Completed");
		}
		LOGGER.info("Ais140 5445: Ended successfully::: ");
	}

	private void insertService(AIS140ByteWrapper rawData,
			VehicleComposite vehicleComposite,
			FleetTrackingDeviceListenerBORemote fleetTrackingDeviceListenerBO) {
		try {
			Vehicle vehicle = vehicleComposite.getVehicle();
			Vehicleevent vehicleEvent = prepareVehicleEvents(vehicle, rawData,
					fleetTrackingDeviceListenerBO, vehicleComposite);
			LOGGER.info("AIS140DeviceProtocolHandler: handleDevice: VehicleEvents prepared for vin="
					+ vehicleEvent.getId().getVin() + " at " + new Date());
			/**
			 * For SKT
			 */
			Position position = getPositionObject(vehicleEvent, rawData);
			String imeiNo = String.valueOf(position.getDeviceId());
			LOGGER.info("Ais140 5445 : Time:: " + position.getTime()
					+ " and AIS140Imei = " + position.getDeviceId()
					+ " and RFID = " + position.getRfid() + " and Company = "
					+ vehicleComposite.getVehicle().getCompanyId()
					+ " and Branch = "
					+ vehicleComposite.getVehicle().getBranchId());
			sktHandlerMethods.persistEventAndGenerateAlert(position,
					vehicleComposite, imeiNo, "AIS140", vehicleEvent);
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
			AIS140ByteWrapper rawData) {
		Position position = new Position();
		position.setDeviceId(Long.valueOf(deviceImei));
		position.setRfid("1");
		position.setTime(rawData.getAis140GpsData().getDateTime());
		position.setLatitude(Double.valueOf(vehicleEvent.getLatitude()));
		position.setLongitude(Double.valueOf(vehicleEvent.getLongitude()));
		position.setSpeed(Double.valueOf(vehicleEvent.getSpeed()));
		return position;
	}

	private Vehicleevent prepareVehicleEvents(Vehicle vehicle,
			AIS140ByteWrapper avlData,
			FleetTrackingDeviceListenerBORemote entityManagerService,
			VehicleComposite vehicleComposite) {
		Vehicleevent vehicleEvent = new Vehicleevent();
		try {
			String region = vehicleComposite.getTimeZoneRegion();
			VehicleeventId vehicleeventId = new VehicleeventId();
			// vehicleeventId.setEventTimeStamp(TimeZoneUtil.getDateTimeZone(
			// avlData.getAis140GpsData().getDateTime(), region));

			vehicleeventId.setEventTimeStamp((TimeZoneUtil.getDateTimeZone(
					new Date(), region)));

			vehicleEvent.setServerTimeStamp(TimeZoneUtil.getDateInTimeZone());

			double latitude = avlData.getAis140GpsData().getLatitude();
			latitude = (double) Math.round(latitude * 10000) / 10000;
			double longitude = avlData.getAis140GpsData().getLongitude();
			longitude = (double) Math.round(longitude * 10000) / 10000;

			vehicleEvent.setLongitude(longitude);
			vehicleEvent.setLatitude(latitude);
			vehicleEvent.setSpeed(avlData.getAis140GpsData().getSpeed());
			vehicleeventId.setVin(vehicle.getVin());
			vehicleEvent.setId(vehicleeventId);
			String ioEvent = "21=" + avlData.getAis140GpsData().getGsmSignal()
					+ ",69=" + avlData.getAis140GpsData().getSatellites()
					+ ",30=" + avlData.getAis140GpsData().getBatteryLevel();
			vehicleEvent.setIoevent(ioEvent);
			vehicleEvent.setEngine(avlData.getAis140GpsData().getAcc());
			vehicleEvent.setDi1(avlData.getAis140GpsData().getAcc() ? 1 : 0);
			vehicleEvent.setAi1((int) avlData.getAis140GpsData().getAi1());
			vehicleEvent.setAi2((int) avlData.getAis140GpsData().getAi2());
			vehicleEvent.setBattery(avlData.getAis140GpsData()
					.getBatteryLevel());

			String towedOdometer;
			if (!SKTHandlerMethods.odometerForTowed.isEmpty()
					&& SKTHandlerMethods.odometerForTowed.get(vehicle
							.getCompanyId()) != null) {
				towedOdometer = SKTHandlerMethods.odometerForTowed.get(vehicle
						.getCompanyId());
			} else {
				towedOdometer = entityManagerService.getCompanySettings(
						"towedCalc", vehicle.getCompanyId());
				SKTHandlerMethods.odometerForTowed.put(vehicle.getCompanyId(),
						towedOdometer);
			}
			Vehicleevent ve = entityManagerService.getPrevVeConcox(
					vehicle.getVin(), vehicleeventId.getEventTimeStamp());
			if (towedOdometer.equalsIgnoreCase("1")) {
				if (vehicleEvent.getSpeed() != 0) {
					double odometer = distanceMatrix(ve.getLatitude(),
							ve.getLongitude(), vehicleEvent.getLatitude(),
							vehicleEvent.getLongitude());
					vehicleEvent.setOdometer((long) odometer);
				}
			} else {
				if (ve != null && vehicleEvent.getEngine()
						&& vehicleEvent.getSpeed() != 0) {
					double odometer = distanceMatrix(ve.getLatitude(),
							ve.getLongitude(), vehicleEvent.getLatitude(),
							vehicleEvent.getLongitude());
					vehicleEvent.setOdometer((long) odometer);
				}
			}

		} catch (Exception e) {
			LOGGER.error("Ais140DeviceProtocolHandler: PreparevehicleEvents:"
					+ e);
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

	public double distanceMatrix(double lat1, double lng1, double lat2,
			double lng2) {
		try {
			URL url1 = new URL(
					"http://maps.googleapis.com/maps/api/distancematrix/json?origins="
							+ lat1 + "," + lng1 + "&destinations=" + lat2 + ","
							+ lng2
							+ "&mode=driving&language=en-EN&sensor=false");
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
