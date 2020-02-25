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

import com.eiw.device.ais140v1.AIS140ByteWrapperV1;
import com.eiw.device.ejb.FleetTrackingDeviceListenerBORemote;
import com.eiw.device.ejb.VehicleComposite;
import com.eiw.device.handler.method.SKTHandlerMethods;
import com.eiw.device.meitrack.Position;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.bo.BOFactory;
import com.eiw.server.fleettrackingpu.Ais140Emergency;
import com.eiw.server.fleettrackingpu.Ais140EmergencyId;
import com.eiw.server.fleettrackingpu.Ais140Health;
import com.eiw.server.fleettrackingpu.Ais140HealthId;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.Vehicleevent;
import com.eiw.server.fleettrackingpu.VehicleeventId;

public class AIS140DeviceHandlerV1 extends DeviceHandler {
	private static final Logger LOGGER = Logger.getLogger("listener");
	FleetTrackingDeviceListenerBORemote fleetTrackingDeviceListenerBO = BOFactory
			.getFleetTrackingDeviceListenerBORemote();
	private SKTHandlerMethods sktHandlerMethods;

	@Override
	public void handleDevice() {
		LOGGER.info("Entered AIS140V1 five mins Handle Device:" + new Date());
		sktHandlerMethods = new SKTHandlerMethods();
		DataInputStream clientSocketDis = null;
		DataOutputStream dos = null;
		try {
			clientSocket.setSoTimeout(1500000);
			clientSocketDis = new DataInputStream(clientSocket.getInputStream());
			AIS140ByteWrapperV1 data = new AIS140ByteWrapperV1(clientSocketDis);
			data.unwrapDataFromStream();
			LOGGER.info("Test 1 AIS140V1");
			String imeiNo = data.getImei();
			VehicleComposite vehicleComposite = fleetTrackingDeviceListenerBO
					.getVehicle(imeiNo);
			if (vehicleComposite == null) {
				LOGGER.error("AIS140V1: handleDevice: Received IMEI No is invalid... returning... "
						+ imeiNo);
				return;
			}
			super.deviceImei = imeiNo;
			LOGGER.info("-----------Ais140V1 basicLeng1  -------- "
					+ data.getLength());
			if (data.getLength() > 14
					&& data.getAis140GpsDataV1().getPacketType() != null
					&& !data.getAis140GpsDataV1().getPacketType()
							.equalsIgnoreCase("EMR")) {
				insertService(data, vehicleComposite,
						fleetTrackingDeviceListenerBO);
			} else if (data.getAis140HealthDataV1().getPacketType() != null
					&& data.getAis140HealthDataV1().getPacketType()
							.equalsIgnoreCase("HEALTH")) {
				prepareAndPersistHealthEvents(data, vehicleComposite);
			} else if (data.getLength() > 14
					&& data.getAis140GpsDataV1().getPacketType() != null
					&& data.getAis140GpsDataV1().getPacketType()
							.equalsIgnoreCase("EMR")) {
				prepareAndPersistEmergencyEvents(data, vehicleComposite);
			}

			while (true) {
				AIS140ByteWrapperV1 rawData = new AIS140ByteWrapperV1(
						clientSocketDis);
				rawData.unwrapDataFromStream();
				LOGGER.info("-----------Ais140V1 basicLeng2  -------- "
						+ rawData.getLength());
				if (rawData.getLength() > 14
						&& rawData.getAis140GpsDataV1().getPacketType() != null
						&& !rawData.getAis140GpsDataV1().getPacketType()
								.equalsIgnoreCase("EMR")) {
					insertService(rawData, vehicleComposite,
							fleetTrackingDeviceListenerBO);
				} else if (rawData.getAis140HealthDataV1().getPacketType() != null
						&& rawData.getAis140HealthDataV1().getPacketType()
								.equalsIgnoreCase("HEALTH")) {
					prepareAndPersistHealthEvents(rawData, vehicleComposite);
				} else if (rawData.getLength() > 14
						&& rawData.getAis140GpsDataV1().getPacketType() != null
						&& rawData.getAis140GpsDataV1().getPacketType()
								.equalsIgnoreCase("EMR")) {
					prepareAndPersistEmergencyEvents(rawData, vehicleComposite);
				}

			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			LOGGER.error("SocketTimeoutException while receiving the Message "
					+ e);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("AIS140V1:Exception while receiving the Message " + e);
		} finally {
			cleanUpSockets(clientSocket, clientSocketDis, dos);
			LOGGER.info("DeviceCommunicatorThread:DeviceCommunicator Completed");
		}
		LOGGER.info("AIS140V1: Ended successfully::: ");
	}

	private void insertService(AIS140ByteWrapperV1 rawData,
			VehicleComposite vehicleComposite,
			FleetTrackingDeviceListenerBORemote fleetTrackingDeviceListenerBO) {
		try {
			Vehicle vehicle = vehicleComposite.getVehicle();
			Vehicleevent vehicleEvent = prepareVehicleEvents(vehicle, rawData,
					fleetTrackingDeviceListenerBO, vehicleComposite);
			LOGGER.info("AIS140DeviceProtocolHandlerV1: handleDevice: VehicleEvents prepared for vin="
					+ vehicleEvent.getId().getVin() + " at " + new Date());
			/**
			 * For SKT
			 */
			Position position = getPositionObject(vehicleEvent, rawData);
			String imeiNo = String.valueOf(position.getDeviceId());
			LOGGER.info("AIS140V1 : Time:: " + position.getTime()
					+ " and AIS140V1Imei = " + position.getDeviceId()
					+ " and RFID = " + position.getRfid() + " and Company = "
					+ vehicleComposite.getVehicle().getCompanyId()
					+ " and Branch = "
					+ vehicleComposite.getVehicle().getBranchId());
			sktHandlerMethods.persistEventAndGenerateAlert(position,
					vehicleComposite, imeiNo, "AIS140V1", vehicleEvent);
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
			AIS140ByteWrapperV1 rawData) {
		Position position = new Position();
		position.setDeviceId(Long.valueOf(deviceImei));
		position.setRfid("1");
		position.setTime(rawData.getAis140GpsDataV1().getDateTime());
		position.setLatitude(Double.valueOf(vehicleEvent.getLatitude()));
		position.setLongitude(Double.valueOf(vehicleEvent.getLongitude()));
		position.setSpeed(Double.valueOf(vehicleEvent.getSpeed()));
		return position;
	}

	private Vehicleevent prepareVehicleEvents(Vehicle vehicle,
			AIS140ByteWrapperV1 avlData,
			FleetTrackingDeviceListenerBORemote entityManagerService,
			VehicleComposite vehicleComposite) {
		Vehicleevent vehicleEvent = new Vehicleevent();
		try {
			JSONObject deviceParameters = new JSONObject();
			String region = vehicleComposite.getTimeZoneRegion();
			VehicleeventId vehicleeventId = new VehicleeventId();
			// vehicleeventId.setEventTimeStamp(TimeZoneUtil.getDateTimeZone(
			// avlData.getAis140GpsData().getDateTime(), region));

			vehicleeventId.setEventTimeStamp(avlData.getAis140GpsDataV1()
					.getDateTime());
			LOGGER.info("AIS140V1 EventTimeStamp :"
					+ vehicleeventId.getEventTimeStamp() + " vin :"
					+ vehicleComposite.getVehicle().getVin());

			vehicleEvent.setServerTimeStamp(TimeZoneUtil.getDateTimeZone(
					new Date(), region));

			double latitude = avlData.getAis140GpsDataV1().getLatitude();
			latitude = (double) Math.round(latitude * 10000) / 10000;
			double longitude = avlData.getAis140GpsDataV1().getLongitude();
			longitude = (double) Math.round(longitude * 10000) / 10000;

			vehicleEvent.setLongitude(longitude);
			vehicleEvent.setLatitude(latitude);
			vehicleEvent.setSpeed(avlData.getAis140GpsDataV1().getSpeed());
			vehicleeventId.setVin(vehicle.getVin());
			vehicleEvent.setId(vehicleeventId);
			String ioEvent = "21="
					+ avlData.getAis140GpsDataV1().getGsmSignal() + ",69="
					+ avlData.getAis140GpsDataV1().getSatellites() + ",30="
					+ avlData.getAis140GpsDataV1().getBatteryLevel();
			vehicleEvent.setIoevent(ioEvent);
			vehicleEvent.setEngine(avlData.getAis140GpsDataV1().getAcc());
			vehicleEvent.setDi1(avlData.getAis140GpsDataV1().getAcc() ? 1 : 0);
			vehicleEvent.setAi1((int) avlData.getAis140GpsDataV1().getAi1());
			vehicleEvent.setAi2((int) avlData.getAis140GpsDataV1().getAi2());
			vehicleEvent.setBattery(avlData.getAis140GpsDataV1()
					.getMainInputVoltage());
			deviceParameters
					.put("gps", avlData.getAis140GpsDataV1().isGpsFix());
			deviceParameters.put("gsm", avlData.getAis140GpsDataV1()
					.getGsmSignal());
			deviceParameters.put("satellites", avlData.getAis140GpsDataV1()
					.getSatellites());
			deviceParameters.put("altitude", avlData.getAis140GpsDataV1()
					.getAltitude());
			deviceParameters.put("internalBattery", avlData
					.getAis140GpsDataV1().getBatteryLevel());
			deviceParameters.put("mainVoltage", avlData.getAis140GpsDataV1()
					.getMainInputVoltage());
			deviceParameters.put("powerSupply", avlData.getAis140GpsDataV1()
					.getMainPowerSupply());
			deviceParameters.put("direction", avlData.getAis140GpsDataV1()
					.getDirection());
			deviceParameters.put("packetType", avlData.getAis140GpsDataV1()
					.getPacketType());
			deviceParameters.put("packetStatus", avlData.getAis140GpsDataV1()
					.getPacketStatus());
			deviceParameters.put("messageId", avlData.getAis140GpsDataV1()
					.getMessageId());
			deviceParameters.put("diStatus", avlData.getAis140GpsDataV1()
					.getDiStatus());
			vehicleEvent.setTags(deviceParameters.toString());

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
			if (ve != null
					&& (!ve.getEngine() || ve.getSpeed() == 0)
					&& (!vehicleEvent.getEngine() || vehicleEvent.getSpeed() == 0)) {
				vehicleEvent.setDirection(ve.getDirection());
			} else {
				vehicleEvent.setDirection(avlData.getAis140GpsDataV1()
						.getDirection());
			}

		} catch (Exception e) {
			LOGGER.error("Ais140DeviceProtocolHandlerV1: PreparevehicleEvents:"
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

	private void prepareAndPersistHealthEvents(AIS140ByteWrapperV1 rawData,
			VehicleComposite vehicleComposite) {
		Ais140Health ais140Health = new Ais140Health();
		try {
			String region = vehicleComposite.getTimeZoneRegion();
			Ais140HealthId ais140HealthId = new Ais140HealthId();
			ais140HealthId.setTimeStamp(TimeZoneUtil.getDateTimeZone(
					new Date(), region));
			ais140HealthId.setVin(vehicleComposite.getVehicle().getVin());
			ais140Health.setVendorId(rawData.getAis140HealthDataV1()
					.getVendorId());
			ais140Health.setFirmwareVersion(rawData.getAis140HealthDataV1()
					.getFwVersion());
			ais140Health.setBatteryPercentage(rawData.getAis140HealthDataV1()
					.getBatteryPercent());
			ais140Health.setLowBatteryThreshold(rawData.getAis140HealthDataV1()
					.getLowBatteryThreshold());
			ais140Health.setMemoryPercentage1(rawData.getAis140HealthDataV1()
					.getMemoryPercent1());
			ais140Health.setMemoryPercentage2(rawData.getAis140HealthDataV1()
					.getMemoryPercent2());
			ais140Health.setTimerIgnitionOn(rawData.getAis140HealthDataV1()
					.getIgnitionONInterval());
			ais140Health.setTimerIgnitionOff(rawData.getAis140HealthDataV1()
					.getIgnitionOFFInterval());
			ais140Health.setAi1(rawData.getAis140HealthDataV1().getAi1());
			ais140Health.setAi2(rawData.getAis140HealthDataV1().getAi2());
			ais140Health.setId(ais140HealthId);
			fleetTrackingDeviceListenerBO.persistHealthEvent(ais140Health,
					vehicleComposite);
		} catch (Exception e) {
			LOGGER.error("Ais140V1ProtocolHandler: prepareAndPersistHealthEvents:"
					+ e);
		}
	}

	private void prepareAndPersistEmergencyEvents(AIS140ByteWrapperV1 rawData,
			VehicleComposite vehicleComposite) {
		Ais140Emergency ais140Emergency = new Ais140Emergency();
		try {
			String region = vehicleComposite.getTimeZoneRegion();
			Ais140EmergencyId ais140EmergencyId = new Ais140EmergencyId();
			ais140EmergencyId.setEventTimeStamp(rawData.getAis140GpsDataV1()
					.getDateTime());
			ais140Emergency.setServerTimeStamp(TimeZoneUtil.getDateTimeZone(
					new Date(), region));
			ais140Emergency.setPacketStatus(rawData.getAis140GpsDataV1()
					.getPacketStatus());
			ais140Emergency.setGpsValidity(rawData.getAis140GpsDataV1()
					.getGpsValidity());
			double latitude = rawData.getAis140GpsDataV1().getLatitude();
			latitude = (float) Math.round(latitude * 10000) / 10000;
			double longitude = rawData.getAis140GpsDataV1().getLongitude();
			longitude = (float) Math.round(longitude * 10000) / 10000;

			ais140Emergency.setLongitude(longitude);
			ais140Emergency.setLatitude(latitude);
			ais140EmergencyId.setVin(vehicleComposite.getVehicle().getVin());
			ais140Emergency.setAltitude(rawData.getAis140GpsDataV1()
					.getAltitude());
			ais140Emergency.setSpeed(rawData.getAis140GpsDataV1().getSpeed());
			ais140Emergency.setDistance(rawData.getAis140GpsDataV1()
					.getOdometer());
			ais140Emergency.setGpsProvider(rawData.getAis140GpsDataV1()
					.getGpsProvider());
			ais140Emergency.setVehRegNumber(rawData.getAis140GpsDataV1()
					.getVehRegNo());
			ais140Emergency.setId(ais140EmergencyId);
			fleetTrackingDeviceListenerBO.persistEmergencyEvent(
					ais140Emergency, vehicleComposite);
		} catch (Exception e) {
			LOGGER.error("Ais140V1ProtocolHandler: prepareAndPersistEmergencyEvents:"
					+ e);
		}
	}
}
