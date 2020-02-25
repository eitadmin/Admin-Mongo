package com.eiw.device.handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;

import com.eiw.device.ejb.FleetTrackingDeviceListenerBORemote;
import com.eiw.device.ejb.VehicleComposite;
import com.eiw.device.tzone.TzoneByteWrapperForSchoolBus;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.bo.BOFactory;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.Vehicleevent;
import com.eiw.server.fleettrackingpu.VehicleeventId;
import com.skt.client.dto.StudentData;
import com.skt.client.dto.Vehicledetails;

public class TZoneDeviceHandlerForSchoolBus extends DeviceHandler {

	private static final Logger LOGGER = Logger.getLogger("listener");
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	FleetTrackingDeviceListenerBORemote entityManagerService = BOFactory
			.getFleetTrackingDeviceListenerBORemote();

	public void handleDevice() {

		LOGGER.info("TZoneDeviceHandlerForSchoolBus: handleDevice: Entered Simulator five mins Handle Device:"
				+ new Date());
		DataInputStream clientSocketDis = null;
		DataOutputStream clientSocketDos = null;
		try {
			clientSocket.setSoTimeout(300000);
			LOGGER.info("TZoneDeviceHandlerForSchoolBus: handleDevice:Inside DOS try block");
			clientSocketDis = new DataInputStream(clientSocket.getInputStream());
			clientSocketDos = new DataOutputStream(
					clientSocket.getOutputStream());
			// int count = 0;
			// do {
			// System.out.println("In Loop : " + count++);
			TzoneByteWrapperForSchoolBus initialData = new TzoneByteWrapperForSchoolBus(
					clientSocketDis);
			String result = initialData.read();
			LOGGER.info("TZoneDeviceHandlerForSchoolBus: handleDevice: Formatted Code : "
					+ result);
			String[] resultArray = result.split("\\|");

			// To get Imei No
			String dataArray = initialData.getImeiNo();
			VehicleComposite vehicleComposite = entityManagerService
					.getVehicle(dataArray);
			Vehicle vehicle = vehicleComposite.getVehicle();

			if (vehicle == null) {
				LOGGER.error("TZoneDeviceHandlerForSchoolBus: handleDevice: Received IMEI No is invalid... returning... ");
				return;
			}
			Map<String, StudentData> studentDetailsNStatusMap = null;
			// Map<String, StudentData> studentDetailsNStatusMap =
			// entityManagerService
			// .getStudentDetailsMap(vehicle);
			Map<String, Vehicledetails> vehicledetails = entityManagerService
					.getvehicleattenderdetails(vehicle);
			List<Vehicleevent> vehicleEvents = prepareVehicleEvents(vehicle,
					resultArray, studentDetailsNStatusMap, vehicledetails);
			LOGGER.info("TZoneDeviceHandlerForSchoolBus: handleDevice: VehicleEvents prepared for vin="
					+ vehicleEvents.get(0).getId().getVin()
					+ " at "
					+ new Date());
			entityManagerService.persistDeviceData(vehicleEvents,
					vehicleComposite);
			while (true) {
				TzoneByteWrapperForSchoolBus data = new TzoneByteWrapperForSchoolBus(
						clientSocketDis);
				String nextresult = data.read();
				LOGGER.info("TZoneDeviceHandlerForSchoolBus: handleDevice: Formatted Code : "
						+ result);
				String[] nextresultArray = nextresult.split("\\|");

				// Map<String, StudentData> nextstudentDetailsNStatusMap =
				// entityManagerService
				// .getStudentDetailsMap(vehicle);
				Map<String, StudentData> nextstudentDetailsNStatusMap = null;
				Map<String, Vehicledetails> nextvehicledetails = entityManagerService
						.getvehicleattenderdetails(vehicle);
				List<Vehicleevent> nextvehicleEvents = prepareVehicleEvents(
						vehicle, nextresultArray, nextstudentDetailsNStatusMap,
						nextvehicledetails);
				LOGGER.info("TZoneDeviceHandlerForSchoolBus: handleDevice: VehicleEvents prepared for vin="
						+ nextvehicleEvents.get(0).getId().getVin()
						+ " at "
						+ new Date());
				entityManagerService.persistDeviceData(nextvehicleEvents,
						vehicleComposite);
			}
			// if (count > 10)
			// break;
			// } while (clientSocketDis.available() != 0);
		} catch (Exception e) {
			LOGGER.error("TZoneDeviceHandlerForSchoolBus occured" + e);
		} finally {
			cleanUpSockets(clientSocket, clientSocketDis, clientSocketDos);
			LOGGER.info("DeviceCommunicatorThread:DeviceCommunicator Completed");
		}
	}

	private List<Vehicleevent> prepareVehicleEvents(Vehicle vehicle,
			String[] resultArray,
			Map<String, StudentData> studentDetailsNStatusMap,
			Map<String, Vehicledetails> nextvehicledetails) {
		List<Vehicleevent> vehicleEvents = new ArrayList<Vehicleevent>();
		try {
			String region = entityManagerService.getTimeZoneRegion(vehicle
					.getVin());
			// To get Timestamp
			String[] timestamp = resultArray[5].split(":");
			int yr = 2000 + Integer.parseInt(timestamp[0]);
			int month = Integer.parseInt(timestamp[1]);
			int day = Integer.parseInt(timestamp[2]);
			int hr = Integer.parseInt(timestamp[3]);
			int min = Integer.parseInt(timestamp[4]);
			int sec = Integer.parseInt(timestamp[5]);

			String event = yr + "-" + month + "-" + day + " " + hr + ":" + min
					+ ":" + sec;
			System.out.println(event + " "
					+ dateFormat.format(dateFormat.parse(event)));

			// To get LatLng
			// latitude,longitude: convert hex to decimal,then Divided by
			// 60, finally, Divided by 10000.
			NumberFormat number = NumberFormat.getNumberInstance();
			number.setMaximumFractionDigits(6);
			String[] gpsData = resultArray[6].split(",");
			String latitude = number.format(Float.parseFloat(gpsData[2]));
			String longitude = number.format(Float.parseFloat(gpsData[3]));
			System.out.println("LAT:: " + latitude + " LNG:: " + longitude);

			// To get Speed
			// GPS speed: Upload speed is nautical miles, Divided by 100 to
			// convert . (Nautical miles should be multiplied by 1.852 to
			// convert)
			double speed = Double.parseDouble(gpsData[5]);
			System.out.println(speed);

			String[] ioData = resultArray[8].split(",");
			// To get Digital Inputs
			int di1 = Integer.valueOf(ioData[3].substring(7, 8));
			int di2 = Integer.valueOf(ioData[4].substring(7, 8));

			// To get Odometer Value (GPS Mileage)
			double odometer = Double.parseDouble(gpsData[5]);
			System.out.println("ODOM:::: " + odometer);
			System.out.println("Date : "
					+ TimeZoneUtil.getDateTimeZone(new Date(), "Asia/Kolkata"));

			// To get Analog Inputs (ADC)
			float AI1 = Float.parseFloat(ioData[7]);
			float AI2 = Float.parseFloat(ioData[8]);

			Vehicleevent vehicleEvent = new Vehicleevent();
			VehicleeventId vehicleeventId = new VehicleeventId();
			Date d1 = TimeZoneUtil.getDateTimeZone(dateFormat.parse(event),
					region);
			vehicleeventId.setEventTimeStamp(TimeZoneUtil.getDateTimeZone(
					dateFormat.parse(event), region));
			vehicleEvent.setServerTimeStamp(TimeZoneUtil
					.getDateInTimeZoneforSKT(region));
			vehicleEvent.setLongitude(Double.parseDouble(longitude));
			vehicleEvent.setLatitude(Double.parseDouble(latitude));
			vehicleEvent.setEngine((di1 == 1) ? true : false);
			vehicleEvent.setSpeed(((Double) speed).intValue());
			vehicleeventId.setVin(vehicle.getVin());
			vehicleEvent.setId(vehicleeventId);
			vehicleEvent.setDi1(di1);
			vehicleEvent.setDi2(di2);
			vehicleEvent.setOdometer(((Double) odometer).longValue());
			vehicleEvent.setAi1(((Float) AI1).intValue());
			vehicleEvent.setAi2(((Float) AI2).intValue());
			// Commented code is using RFID ...
			/*
			 * if (ioData[1].equalsIgnoreCase("AA")) { List<String>
			 * totalStudentsActiveTagsInBus = new ArrayList<String>();
			 * List<String> currentPassiveCardHit = new ArrayList<String>();
			 * List<String> currenttagIdintheBus = new ArrayList<>();
			 * Set<String> totalstudentsCurrentlyInBus = new HashSet<>();
			 * String[] passiveTags = resultArray[9].split(","); if
			 * (passiveTags.length != 1) {
			 * System.out.println("Passive Tags : "); for (int i = 3; i <
			 * passiveTags.length; i++) {
			 * currentPassiveCardHit.add(passiveTags[i]);
			 * System.out.print(passiveTags[i] + "\t"); }
			 * 
			 * for (Entry<String, StudentData> studentData :
			 * studentDetailsNStatusMap .entrySet()) { if
			 * (studentData.getValue().getStatus() == 1 &&
			 * studentData.getValue().getTagType() .equalsIgnoreCase("Passive"))
			 * { currenttagIdintheBus.add(studentData.getValue() .getTagId()); }
			 * 
			 * }
			 * 
			 * for (String singlePassiveTag : currentPassiveCardHit) { int
			 * status = studentDetailsNStatusMap.get(
			 * singlePassiveTag).getStatus(); if (status == 0) {
			 * currenttagIdintheBus.add(singlePassiveTag); } else {
			 * currenttagIdintheBus.remove(singlePassiveTag); }
			 * 
			 * } totalstudentsCurrentlyInBus.addAll(currenttagIdintheBus); }
			 * 
			 * else { for (Entry<String, StudentData> studentData :
			 * studentDetailsNStatusMap .entrySet()) { if
			 * (studentData.getValue().getStatus() == 1 &&
			 * studentData.getValue().getTagType() .equalsIgnoreCase("Passive"))
			 * currenttagIdintheBus.add(studentData.getValue() .getTagId()); }
			 * totalstudentsCurrentlyInBus.addAll(currenttagIdintheBus); }
			 * 
			 * String[] activeTags = resultArray[12].split(","); if
			 * (activeTags.length != 1) { System.out.println("Active Tags : ");
			 * for (int i = 3; i < activeTags.length; i++) {
			 * totalStudentsActiveTagsInBus.add(activeTags[i]);
			 * System.out.print(activeTags[i] + "\t"); }
			 * totalstudentsCurrentlyInBus
			 * .addAll(totalStudentsActiveTagsInBus); } List<String>
			 * finaltotalstudentsIntheBus = new ArrayList<>(); if
			 * (totalstudentsCurrentlyInBus.size() != 0) {
			 * finaltotalstudentsIntheBus .addAll(totalstudentsCurrentlyInBus);
			 * 
			 * } vehicleEvent.setTags(activeTags.toString());
			 * 
			 * List<String> studentsAlreadyInBus = getStudentsAlreadyInBus(
			 * studentDetailsNStatusMap, 1);
			 * 
			 * // prepareStudentEvents(vehicleEvent, studentsAlreadyInBus, //
			 * finaltotalstudentsIntheBus, studentDetailsNStatusMap, //
			 * vehicle); }
			 */

			vehicleEvents.add(vehicleEvent);
			// entityManagerService.busAlertsForParents(vehicleEvent,
			// studentDetailsNStatusMap, vehicle);
		} catch (ParseException e) {
			LOGGER.error("Date Parse occured" + e);
		}
		return vehicleEvents;
	}

	private void prepareStudentEvents(Vehicleevent vehicleEvent,
			List<String> studentsAlreadyInBus, List<String> totalStudentsInBus,
			Map<String, StudentData> studentDetailsNStatusMap, Vehicle vehicle) {
		List<String> tempStudentsAlreadyInBus = new ArrayList<String>(
				studentsAlreadyInBus), tempStudentsEnteringBus = new ArrayList<String>(
				totalStudentsInBus);
		LOGGER.info("Real : " + totalStudentsInBus.size()
				+ " = currsize and prevsize = " + studentsAlreadyInBus.size());
		for (int k = 0; k < studentsAlreadyInBus.size(); k++) {
			boolean isStudentIN = totalStudentsInBus
					.contains(studentsAlreadyInBus.get(k));
			LOGGER.info(isStudentIN + " = boolean and tagid = "
					+ studentsAlreadyInBus.get(k));
			if (isStudentIN) {
				tempStudentsAlreadyInBus.remove(studentsAlreadyInBus.get(k));
				tempStudentsEnteringBus.remove(studentsAlreadyInBus.get(k));
			}
		}

		LOGGER.info(tempStudentsEnteringBus.size()
				+ " = currsize and prevsize = "
				+ tempStudentsAlreadyInBus.size());
		// tempStudentsAlreadyInBus means Students Stepped out.. Because we have
		// removed students inside bus from tempStudentsAlreadyInBus list
		// entityManagerService.persistStudentData(vehicleEvent,
		// tempStudentsAlreadyInBus, tempStudentsEnteringBus,
		// studentDetailsNStatusMap, vehicle, totalStudentsInBus);
	}

	private List<String> getStudentsAlreadyInBus(
			Map<String, StudentData> studentStatusMap2, int prevStatus) {
		ArrayList<String> studentsIn = new ArrayList<String>();

		for (Map.Entry<String, StudentData> mapEntry : studentStatusMap2
				.entrySet()) {
			StudentData statusData = mapEntry.getValue();
			if (statusData.getStatus() == prevStatus) {
				// LOGGER.info("Students already in : " +
				// statusData.getTagId());
				studentsIn.add((String) mapEntry.getKey());
			}
		}
		return studentsIn;
	}

}