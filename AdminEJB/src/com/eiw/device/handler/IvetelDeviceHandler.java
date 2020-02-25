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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.eiw.device.ejb.FleetTrackingDeviceListenerBORemote;
import com.eiw.device.ejb.VehicleComposite;
import com.eiw.device.handler.method.SKTHandlerMethods;
import com.eiw.device.ivetel.IvetelByteWrapper;
import com.eiw.device.meitrack.Position;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.bo.BOFactory;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.Vehicleevent;
import com.eiw.server.fleettrackingpu.VehicleeventId;

public class IvetelDeviceHandler extends DeviceHandler {
	private static final Logger LOGGER = Logger.getLogger("listener");
	FleetTrackingDeviceListenerBORemote fleetTrackingDeviceListenerBO = BOFactory
			.getFleetTrackingDeviceListenerBORemote();
	private SKTHandlerMethods sktHandlerMethods;

	@Override
	public void handleDevice() {
		LOGGER.info("Entered Ivetel five mins Handle Device:" + new Date());
		sktHandlerMethods = new SKTHandlerMethods();
		DataInputStream clientSocketDis = null;
		DataOutputStream dos = null;
		try {
			clientSocket.setSoTimeout(1500000);
			clientSocketDis = new DataInputStream(clientSocket.getInputStream());
			IvetelByteWrapper data = new IvetelByteWrapper(clientSocketDis);
			data.unwrapDataFromStream();
			LOGGER.info("Test 1 Ivetel");
			String imeiNo = data.getImei();
			VehicleComposite vehicleComposite = fleetTrackingDeviceListenerBO
					.getVehicle(imeiNo);
			if (vehicleComposite == null) {
				LOGGER.error("IvetelDeviceHandler: handleDevice: Received IMEI No is invalid... returning... "
						+ imeiNo);
				return;
			}
			super.deviceImei = imeiNo;
			// LOGGER.info("Test 2:" + super.deviceImei);
			insertService(data, vehicleComposite, fleetTrackingDeviceListenerBO);
			while (true) {
				IvetelByteWrapper rawData = new IvetelByteWrapper(
						clientSocketDis);
				rawData.unwrapDataFromStream();
				insertService(rawData, vehicleComposite,
						fleetTrackingDeviceListenerBO);
			}
		} catch (SocketTimeoutException e) {
			LOGGER.error("SocketTimeoutException while receiving the Message "
					+ e);
		} catch (Exception e) {
			LOGGER.error("IvetelDeviceHandler:Exception while receiving the Message "
					+ e);
		} finally {
			cleanUpSockets(clientSocket, clientSocketDis, dos);
			LOGGER.info("DeviceCommunicatorThread:DeviceCommunicator Completed");
		}
		LOGGER.info("IvetelDeviceHandler: Ended successfully::: ");
	}

	private void insertService(IvetelByteWrapper rawData,
			VehicleComposite vehicleComposite,
			FleetTrackingDeviceListenerBORemote fleetTrackingDeviceListenerBO) {
		try {
			Vehicle vehicle = vehicleComposite.getVehicle();
			Vehicleevent vehicleEvent = prepareVehicleEvents(vehicle, rawData,
					fleetTrackingDeviceListenerBO, vehicleComposite);
			LOGGER.info("IvetelDeviceProtocolHandler: handleDevice: VehicleEvents prepared for vin="
					+ vehicleEvent.getId().getVin() + " at " + new Date());
			/**
			 * For SKT
			 */
			Position position = getPositionObject(vehicleEvent, rawData);
			String imeiNo = String.valueOf(position.getDeviceId());
			LOGGER.info("IvetelDeviceHandler : Time:: " + position.getTime()
					+ " and ivetelImei = " + position.getDeviceId()
					+ " and RFID = " + position.getRfid() + " and Company = "
					+ vehicleComposite.getVehicle().getCompanyId()
					+ " and Branch = "
					+ vehicleComposite.getVehicle().getBranchId());
			sktHandlerMethods.persistEventAndGenerateAlert(position,
					vehicleComposite, imeiNo, "ivetel", vehicleEvent);
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
			IvetelByteWrapper rawData) {
		Position position = new Position();
		position.setDeviceId(Long.valueOf(deviceImei));
		if (vehicleEvent.getTags() != null)
			position.setRfid(vehicleEvent.getTags());
		else
			position.setRfid("1");
		position.setTime(rawData.getIvetelGpsData().getDateTime());
		position.setLatitude(Double.valueOf(vehicleEvent.getLatitude()));
		position.setLongitude(Double.valueOf(vehicleEvent.getLongitude()));
		position.setSpeed(Double.valueOf(vehicleEvent.getSpeed()));
		return position;
	}

	private Vehicleevent prepareVehicleEvents(Vehicle vehicle,
			IvetelByteWrapper avlData,
			FleetTrackingDeviceListenerBORemote entityManagerService,
			VehicleComposite vehicleComposite) {
		Vehicleevent vehicleEvent = new Vehicleevent();
		try {
			// String region = entityManagerService.getTimeZoneRegion(vehicle
			// .getVin());
			String region = vehicleComposite.getTimeZoneRegion();
			Vehicleevent ve = entityManagerService.getPrevVe(vehicle.getVin());
			VehicleeventId vehicleeventId = new VehicleeventId();
			vehicleeventId.setEventTimeStamp(TimeZoneUtil.getDateTimeZone(
					avlData.getIvetelGpsData().getDateTime(), region));
			vehicleEvent.setServerTimeStamp(TimeZoneUtil.getDateInTimeZone());
			double latitude = avlData.getIvetelGpsData().getLatitude();
			latitude = (double) Math.round(latitude * 10000) / 10000;
			double longitude = avlData.getIvetelGpsData().getLongitude();
			longitude = (double) Math.round(longitude * 10000) / 10000;

			vehicleEvent.setLongitude(longitude);
			vehicleEvent.setLatitude(latitude);
			vehicleEvent.setSpeed(avlData.getIvetelGpsData().getSpeed());
			vehicleeventId.setVin(vehicle.getVin());
			vehicleEvent.setId(vehicleeventId);
			String ioEvent = "21=" + avlData.getIvetelGpsData().getGsmSignal()
					+ ",69=" + avlData.getIvetelGpsData().getSatellites()
					+ ",30=" + avlData.getIvetelGpsData().getBatteryInMv()
					+ ",901=" + avlData.getIvetelGpsData().getFuel1() + ",902="
					+ avlData.getIvetelGpsData().getFuel2() + ",903="
					+ avlData.getIvetelGpsData().getFuel3();
			vehicleEvent.setIoevent(ioEvent);
			vehicleEvent.setEngine(avlData.getIvetelGpsData().getAcc());
			vehicleEvent.setDi1(avlData.getIvetelGpsData().getAcc() ? 1 : 0);
			vehicleEvent.setAi1(avlData.getIvetelGpsData().getAdc1());
			vehicleEvent.setBattery(13000L);
			vehicleEvent.setTags(avlData.getIvetelGpsData().getRfid());
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
			if (ve != null
					&& (!ve.getEngine() || ve.getSpeed() == 0)
					&& (!vehicleEvent.getEngine() || vehicleEvent.getSpeed() == 0)) {
				vehicleEvent.setDirection(ve.getDirection());
			} else {
				vehicleEvent
						.setDirection(avlData.getIvetelGpsData().getAngle());
			}

		} catch (Exception e) {
			LOGGER.error("IvetelDeviceProtocolHandler: PreparevehicleEvents:"
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
			String encoding = con.getContentEncoding();
			encoding = encoding == null ? "UTF-8" : encoding;
			String statusText = IOUtils.toString(in, encoding);
			if (statusText == null || !statusText.contains("distance")) {
				return distance(lat1, lng1, lat2, lng2);
			}
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(statusText);
			String rows = json.get("rows").toString();
			JSONObject rowObject = (JSONObject) parser.parse(rows.substring(1,
					rows.length() - 1));
			String elements = rowObject.get("elements").toString();
			JSONObject elementsObject = (JSONObject) parser.parse(elements
					.substring(1, elements.length() - 1));
			String distance = elementsObject.get("distance").toString();
			JSONObject distanceObject = (JSONObject) parser.parse(distance);
			return Float.valueOf(distanceObject.get("value").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return distance(lat1, lng1, lat2, lng2);
	}
}
