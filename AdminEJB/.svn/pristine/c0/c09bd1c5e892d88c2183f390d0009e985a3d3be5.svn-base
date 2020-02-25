package com.eiw.device.handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;
import org.json.JSONObject;

import com.eiw.device.cantrack.CantrackByteWapper;
import com.eiw.device.ejb.FleetTrackingDeviceListenerBORemote;
import com.eiw.device.ejb.VehicleComposite;
import com.eiw.device.handler.method.SKTHandlerMethods;
import com.eiw.device.listener.ListenerStarter;
import com.eiw.device.meitrack.Position;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.bo.BOFactory;
import com.eiw.server.fleettrackingpu.Companytrackdevice;
import com.eiw.server.fleettrackingpu.Heartbeatevent;
import com.eiw.server.fleettrackingpu.HeartbeateventId;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.Vehicleevent;
import com.eiw.server.fleettrackingpu.VehicleeventId;

public class CantrackDeviceHandler extends DeviceHandler {

	private static final Logger LOGGER = Logger.getLogger("listener");
	FleetTrackingDeviceListenerBORemote fleetTrackingDeviceListenerBO = BOFactory
			.getFleetTrackingDeviceListenerBORemote();
	private SKTHandlerMethods sktHandlerMethods;

	@Override
	protected void handleDevice() {

		sktHandlerMethods = new SKTHandlerMethods();
		LOGGER.error("Enter into Cantrack Device Handler Date : " + new Date());
		DataInputStream clientSocketDis = null;
		DataOutputStream clientSocketDos = null;
		try {
			clientSocket.setSoTimeout(1500000);
			clientSocketDis = new DataInputStream(clientSocket.getInputStream());
			clientSocketDos = new DataOutputStream(
					clientSocket.getOutputStream());
			dis = clientSocketDis;
			dos = clientSocketDos;
			CantrackByteWapper rawData = new CantrackByteWapper();
			rawData.unwrapDataFromStream(clientSocketDis, clientSocketDos, this);
			String imeiNo = rawData.getImeiNo();

			VehicleComposite vehicleComposite = fleetTrackingDeviceListenerBO
					.getVehicle(imeiNo);
			if (vehicleComposite == null) {
				LOGGER.error("CantrackDeviceHandler: handleDevice: Received IMEI No is invalid... returning... "
						+ imeiNo);
				return;
			}
			super.deviceImei = imeiNo;
			if (ListenerStarter.cantrackDeviceHandlerMap.get(deviceImei) == null) {
				ListenerStarter.cantrackDeviceHandlerMap.put(deviceImei, this);
			} else {
				cleanUpSockets(
						ListenerStarter.cantrackDeviceHandlerMap
								.get(deviceImei).getClientSocket(),
						ListenerStarter.cantrackDeviceHandlerMap
								.get(deviceImei).getDis(),
						ListenerStarter.cantrackDeviceHandlerMap
								.get(deviceImei).getDos());
				LOGGER.error("  CleanUp Socket in CantrackDevice :: ImeiNo :  "
						+ deviceImei);
				ListenerStarter.cantrackDeviceHandlerMap.put(deviceImei, this);
			}
			while (true) {
				LOGGER.error("Enter into Cantrack Device Handler Date : ");
				rawData.unwrapDataFromStream(clientSocketDis, clientSocketDos,
						this);
				if (ListenerStarter.cantrackDeviceHandlerMap.get(deviceImei) == null) {
					ListenerStarter.cantrackDeviceHandlerMap.put(deviceImei,
							this);
				}
				if (rawData.getType().equalsIgnoreCase(CantrackByteWapper.MSG_GPS)
						|| rawData.getType().equalsIgnoreCase(CantrackByteWapper.MSG_ALARM)) {
					insertService(rawData, vehicleComposite,
							fleetTrackingDeviceListenerBO);
				} else if (rawData.getType().equalsIgnoreCase(CantrackByteWapper.MSG_HBD)) {
					updateHeartBeatInfo(rawData, vehicleComposite,
							fleetTrackingDeviceListenerBO);
				}

			}
		} catch (Exception j) {
			j.printStackTrace();
		}
	}

	private void updateHeartBeatInfo(CantrackByteWapper rawData,
			VehicleComposite vehicleComposite,
			FleetTrackingDeviceListenerBORemote fleetTrackingDeviceListenerBO) {
		Vehicle vehicle = vehicleComposite.getVehicle();
		VehicleeventId vehicleeventId = new VehicleeventId();
		String region = vehicleComposite.getTimeZoneRegion();
		vehicleeventId.setEventTimeStamp(TimeZoneUtil.getDateTimeZone(
				new Date(), region));
		Vehicleevent vehicleEvent = fleetTrackingDeviceListenerBO
				.getPrevVeConcox(vehicle.getVin(),
						vehicleeventId.getEventTimeStamp());
		if (vehicleEvent != null) {
			String hbd = "hbd"
					+ rawData.getCantrackGpsData().getAlarmMode()
					+ ";"
					+ rawData.getCantrackGpsData().getGps()
					+ ";"
					+ TimeZoneUtil.getTimeINYYYYMMddss(
							TimeZoneUtil.getDateTimeZone(new Date(), region))
							.toString() + ";"
					+ rawData.getCantrackGpsData().getGsm() + ";"
					+ rawData.getCantrackGpsData().getAcc() + ":"
					+ rawData.getCantrackGpsData().getBattryVoltage() + ";"
					+ rawData.getCantrackGpsData().getCharger();
			
			String ioevent = "";
			if(vehicleEvent.getIoevent() != null){
			for (String ioe : vehicleEvent.getIoevent().split(",")) {
				if (ioe.startsWith("hbd")) {
					break;
				}
				if (ioevent.equalsIgnoreCase(""))
					ioevent += ioe;
				else
					ioevent += "," + ioe;
			}
			}
			String io = "";
			if (ioevent.equalsIgnoreCase(""))
				io += hbd;
			else
				io += ioevent + "," + hbd;
			vehicleEvent.setIoevent(io);
			
			List<Vehicleevent> vehicleEvents = new ArrayList<Vehicleevent>();
			vehicleEvents.add(vehicleEvent);
			fleetTrackingDeviceListenerBO.updateVehicleevent(vehicleEvents,
					"hbd", vehicleComposite);
			
			try{
				String hbdChecking;
				if (!SKTHandlerMethods.hbdCheck.isEmpty()
						&& SKTHandlerMethods.hbdCheck.get(vehicle
								.getCompanyId()) != null) {
					hbdChecking = SKTHandlerMethods.hbdCheck.get(vehicle
							.getCompanyId());
				} else {
					hbdChecking = fleetTrackingDeviceListenerBO.getCompanySettings(
							"heartbeatCheck", vehicle.getCompanyId());
					SKTHandlerMethods.hbdCheck.put(vehicle.getCompanyId(),
							hbdChecking);
				}
				
				if (hbdChecking.equalsIgnoreCase("1")) {
					
					if (!rawData.getCantrackGpsData().getAcc()
							&& vehicleEvent.getEngine()) {
						Vehicleevent vehiEvent = new Vehicleevent();	
						vehicleeventId.setVin(vehicle.getVin());
						vehiEvent.setServerTimeStamp(vehicleeventId
								.getEventTimeStamp());
						vehiEvent.setEngine(rawData.getCantrackGpsData()
								.getAcc());
						vehiEvent.setSpeed(0);
						vehiEvent.setIoevent(io);
						vehiEvent.setLatitude(vehicleEvent.getLatitude());
						vehiEvent.setLongitude(vehicleEvent.getLongitude());
						vehiEvent.setDirection(vehicleEvent.getDirection());
						vehiEvent.setDi1(rawData.getCantrackGpsData()
								.getAcc() ? 1 : 0);
						vehiEvent.setTags("hbd");
						long differenceInSec = (vehicleeventId
								.getEventTimeStamp().getTime() - vehicleEvent.getId()
								.getEventTimeStamp().getTime()) / 1000;
						if (differenceInSec > 1200) {
							vehicleeventId.setEventTimeStamp(vehicleEvent
									.getServerTimeStamp());
							vehiEvent.setTags("NT");
						}
						vehiEvent.setId(vehicleeventId);
						Position position = getPositionObject(vehiEvent);
						String imeiNo = String.valueOf(position.getDeviceId());
						sktHandlerMethods.persistEventAndGenerateAlert(
								position, vehicleComposite, imeiNo, "cantrack",
								vehiEvent);
					}
				}
				
				
				
				
			}catch(Exception e){
				LOGGER.error("CantrackDeviceProtocolHandler: prepareAndPersistHeartBeatEvents:"
						+ e);
			}
		}
		prepareAndPersistHeartBeatEvents(vehicle, rawData, region,
				vehicleComposite);

	}

	private void prepareAndPersistHeartBeatEvents(Vehicle vehicle,
			CantrackByteWapper rawData, String region,
			VehicleComposite vehicleComposite) {
		Heartbeatevent heartBeatEvent = new Heartbeatevent();
		try {
			HeartbeateventId heartbeateventId = new HeartbeateventId();
			heartbeateventId.setTimeStamp(TimeZoneUtil.getDateTimeZone(
					new Date(), region));
			heartbeateventId.setVin(vehicle.getVin());
			heartBeatEvent.setEngine(rawData.getCantrackGpsData().getAcc());
			heartBeatEvent.setGps(String.valueOf(rawData.getCantrackGpsData().getGps()));
			heartBeatEvent.setGsm(Integer.valueOf(rawData.getCantrackGpsData().getGsm()));
			heartBeatEvent.setBatteryVoltage(String.valueOf(rawData.getCantrackGpsData().getBattryVoltage()));
			heartBeatEvent.setPowerSupply(rawData.getCantrackGpsData().getCharger());
			heartBeatEvent.setId(heartbeateventId);
			fleetTrackingDeviceListenerBO.persistHeartBeatEvent(heartBeatEvent,
					vehicleComposite);
		} catch (Exception e) {
			LOGGER.error("CantrackDeviceProtocolHandler: prepareAndPersistHeartBeatEvents:"
					+ e);
		}		
	}

	private void insertService(CantrackByteWapper rawData,
			VehicleComposite vehicleComposite,
			FleetTrackingDeviceListenerBORemote fleetTrackingDeviceListenerBO2) {
		try {
			Vehicle vehicle = vehicleComposite.getVehicle();
			Companytrackdevice companyTrackDevice = vehicleComposite
					.getCompanytrackDevice();
			Vehicleevent vehicleEvent = prepareVehicleEvents(vehicle,
					companyTrackDevice, rawData, vehicleComposite,
					fleetTrackingDeviceListenerBO2);
			if (vehicleEvent != null) {
				Position position = getPositionObject(vehicleEvent);
				String imeiNo = String.valueOf(position.getDeviceId());
				sktHandlerMethods.persistEventAndGenerateAlert(position,
						vehicleComposite, imeiNo, "cantrack", vehicleEvent);
			}
		} catch (Exception j) {
			j.printStackTrace();
		}

	}

	private Position getPositionObject(Vehicleevent vehicleEvent) {
		Position position = new Position();
		position.setDeviceId(Long.valueOf(deviceImei));
		position.setRfid("1");
		position.setTime(vehicleEvent.getId().getEventTimeStamp());
		position.setLatitude(Double.valueOf(vehicleEvent.getLatitude()));
		position.setLongitude(Double.valueOf(vehicleEvent.getLongitude()));
		position.setSpeed(Double.valueOf(vehicleEvent.getSpeed()));
		return position;
	}

	private Vehicleevent prepareVehicleEvents(Vehicle vehicle,
			Companytrackdevice companyTrackDevice, CantrackByteWapper rawData,
			VehicleComposite vehicleComposite,
			FleetTrackingDeviceListenerBORemote fleetTrackingDeviceListenerBO) {
		Vehicleevent vehicleEvent = new Vehicleevent();
		try {
			String region = vehicleComposite.getTimeZoneRegion();
			VehicleeventId vehicleeventId = new VehicleeventId();
			vehicleeventId.setEventTimeStamp(TimeZoneUtil.getDateTimeZone(
					rawData.getCantrackGpsData().getDateTime(), region));
			vehicleEvent.setServerTimeStamp(TimeZoneUtil.getDateTimeZone(
					new Date(), region));
			Vehicleevent preVe = fleetTrackingDeviceListenerBO.getPrevVeConcox(
					vehicle.getVin(), vehicleeventId.getEventTimeStamp());
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(vehicleEvent.getServerTimeStamp());
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(vehicleeventId.getEventTimeStamp());
			if (!(cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR))) {
				return null;
			}

			double latitude = (double) rawData.getCantrackGpsData()
					.getLatitude();
			latitude = (double) Math.round(latitude * 10000) / 10000;
			double longitude = (double) rawData.getCantrackGpsData()
					.getLongitude();
			longitude = (double) Math.round(longitude * 10000) / 10000;

			if (latitude == 0.0 || longitude == 0.0
					|| (latitude < 0 && longitude < 0)) {
				return null;
			}
			vehicleEvent.setLongitude(longitude);
			vehicleEvent.setLatitude(latitude);
			vehicleEvent.setSpeed(rawData.getCantrackGpsData().getSpeed());
			vehicleeventId.setVin(vehicle.getVin());
			vehicleEvent.setId(vehicleeventId);
			vehicleEvent.setEngine(rawData.getCantrackGpsData().getAcc());
			vehicleEvent.setTags("Alarm="
					+ rawData.getCantrackGpsData().getAlarmMode());
			String accCheck;
			if (!SKTHandlerMethods.accForSpeed.isEmpty()
					&& SKTHandlerMethods.accForSpeed
							.get(vehicle.getCompanyId()) != null) {
				accCheck = SKTHandlerMethods.accForSpeed.get(vehicle
						.getCompanyId());
			} else {
				accCheck = fleetTrackingDeviceListenerBO.getCompanySettings(
						"accSetting", vehicle.getCompanyId());
				SKTHandlerMethods.accForSpeed.put(vehicle.getCompanyId(),
						accCheck);
			}
			if (vehicleEvent.getSpeed() > 0 && accCheck.equalsIgnoreCase("1")) {
				vehicleEvent.setEngine(true);
			}
			vehicleEvent.setDi1(rawData.getCantrackGpsData().getAcc() ? 1 : 0);
			String towedOdometer;
			if (!SKTHandlerMethods.odometerForTowed.isEmpty()
					&& SKTHandlerMethods.odometerForTowed.get(vehicle
							.getCompanyId()) != null) {
				towedOdometer = SKTHandlerMethods.odometerForTowed.get(vehicle
						.getCompanyId());
			} else {
				towedOdometer = fleetTrackingDeviceListenerBO
						.getCompanySettings("towedCalc", vehicle.getCompanyId());
				SKTHandlerMethods.odometerForTowed.put(vehicle.getCompanyId(),
						towedOdometer);
			}
			if (towedOdometer.equalsIgnoreCase("1")) {
				if (vehicleEvent.getSpeed() != 0) {
					double odometer = distanceMatrix(preVe.getLatitude(),
							preVe.getLongitude(), vehicleEvent.getLatitude(),
							vehicleEvent.getLongitude());
					if (odometer >= 20000) {
						return null;
					}
					vehicleEvent.setOdometer((long) odometer);
				}
			} else {
				if (preVe != null && vehicleEvent.getEngine()
						&& vehicleEvent.getSpeed() != 0) {
					double odometer = distanceMatrix(preVe.getLatitude(),
							preVe.getLongitude(), vehicleEvent.getLatitude(),
							vehicleEvent.getLongitude());
					if (odometer >= 20000) {
						return null;
					}
					vehicleEvent.setOdometer((long) odometer);
				}
			}

			if (preVe != null
					&& (!preVe.getEngine() || preVe.getSpeed() == 0)
					&& (!vehicleEvent.getEngine() || vehicleEvent.getSpeed() == 0)) {
				vehicleEvent.setDirection(preVe.getDirection());
			} else {
				vehicleEvent.setDirection(rawData.getCantrackGpsData()
						.getCourse());
			}

			String prevVeCheck;
			if (!SKTHandlerMethods.hbdCheck.isEmpty()
					&& SKTHandlerMethods.hbdCheck.get(vehicle.getCompanyId()) != null) {
				prevVeCheck = SKTHandlerMethods.hbdCheck.get(vehicle
						.getCompanyId());
			} else {
				prevVeCheck = fleetTrackingDeviceListenerBO.getCompanySettings(
						"heartbeatCheck", vehicle.getCompanyId());
				SKTHandlerMethods.hbdCheck.put(vehicle.getCompanyId(),
						prevVeCheck);
			}
			if (prevVeCheck.equalsIgnoreCase("1")) {
				if (preVe != null && preVe.getSpeed() != 0) {
					long timeDiffInsec = (vehicleeventId.getEventTimeStamp()
							.getTime() - preVe.getId().getEventTimeStamp()
							.getTime()) / 1000;
					if (timeDiffInsec > 1200) {
						vehicleEvent.setEngine(false);
						vehicleEvent.setSpeed(0);
						vehicleEvent.setIoevent(preVe.getIoevent());
						vehicleEvent.setLatitude(preVe.getLatitude());
						vehicleEvent.setLongitude(preVe.getLongitude());
						vehicleEvent.setDirection(preVe.getDirection());
						vehicleEvent.setOdometer(0L);
						vehicleEvent.setDi1(0);
						vehicleEvent.setTags("NTVE");
						Calendar cal = Calendar.getInstance();
						cal.setTime(preVe.getId().getEventTimeStamp());
						cal.add(Calendar.SECOND, 5);
						vehicleEvent.getId().setEventTimeStamp(cal.getTime());
					}
				}
			}
		} catch (Exception j) {
			LOGGER.error("CantrackDeviceProtocolHandler: PreparevehicleEvents:"
					+ j);
			j.printStackTrace();
			return null;
		}
		return vehicleEvent;
	}

	private double distanceMatrix(double lat1, double lng1, double lat2,
			double lng2) {
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
			float finalValue = Float.valueOf(distanceObject.getString("value"));
			if (finalValue >= 3000) {
				return distance(lat1, lng1, lat2, lng2);
			} else {
				return finalValue;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return distance(lat1, lng1, lat2, lng2);
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

}
