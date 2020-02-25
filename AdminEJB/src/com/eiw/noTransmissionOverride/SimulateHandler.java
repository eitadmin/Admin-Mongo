package com.eiw.noTransmissionOverride;

import java.util.Date;

import org.jboss.logging.Logger;

import com.eiw.device.ejb.FleetTrackingDeviceListenerBORemote;
import com.eiw.device.ejb.VehicleComposite;
import com.eiw.device.handler.method.SKTHandlerMethods;
import com.eiw.device.meitrack.Position;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.Vehicleevent;
import com.eiw.server.fleettrackingpu.VehicleeventId;

public class SimulateHandler extends Thread {

	private String imei;
	private int interval;
	private String nearByVins;
	private boolean sendDataContinuously = true;
	private FleetTrackingDeviceListenerBORemote fleetTrackingDeviceListenerBO = null;
	private static final Logger LOGGER = Logger.getLogger("listener");
	private SKTHandlerMethods sktHandlerMethods;
	private static final String STR_SIMULATOR = "simulator";

	public SimulateHandler(String imeiNo, String nearByVins, int interval,
			FleetTrackingDeviceListenerBORemote fleetTrackingDeviceListenerBO) {
		this.imei = imeiNo;
		this.nearByVins = nearByVins;
		this.interval = interval;
		this.fleetTrackingDeviceListenerBO = fleetTrackingDeviceListenerBO;
	}

	// Run the listen/accept loop forever
	public void run() {
		simulate(this.imei, this.nearByVins, this.interval);
	}

	public void simulate(String imeiNo, String nearByVins, int interval) {
		sktHandlerMethods = new SKTHandlerMethods();
		LOGGER.info("SimulateHandler::simulate::" + "IMEI" + imeiNo);
		LOGGER.info("IM IN SimulateHandler::" + "IMEI" + imeiNo);
		VehicleComposite vehicleComposite = fleetTrackingDeviceListenerBO
				.getVehicle(imeiNo);
		if (vehicleComposite == null) {
			return;
		}
		while (sendDataContinuously) {
			insertService(imeiNo, nearByVins, vehicleComposite);
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				LOGGER.error("SimulateHandler::simulate::ERROR Occured" + e);
			}
		}
		LOGGER.info("SimulateHandler::simulate:: leaving from the method succesfully"
				+ imeiNo);
	}

	private void insertService(String imeiNo, String nearByVins,
			VehicleComposite vehicleComposite) {

		try {
			Vehicle vehicle = vehicleComposite.getVehicle();
			Vehicleevent vehicleEvent = prepareVehicleEvents(vehicle,
					nearByVins, vehicleComposite);
			if (vehicleEvent != null) {
				LOGGER.error("IM IM IN SimulateHandler: vin="
						+ vehicleEvent.getId().getVin() + " at " + new Date());
				LOGGER.info("SimulateHandler: handleDevice: VehicleEvents prepared for vin="
						+ vehicleEvent.getId().getVin() + " at " + new Date());

				/**
				 * For SKT
				 */
				Position position = getPositionObject(vehicleEvent);

				sktHandlerMethods.persistEventAndGenerateAlert(position,
						vehicleComposite, imeiNo, "simulate", vehicleEvent);
			}
		} catch (Exception e) {
			LOGGER.error("Exception while persisting data :: " + e);
		}

	}

	private Position getPositionObject(Vehicleevent vehicleEvent) {
		Position position = new Position();
		position.setRfid("1");
		position.setTime(vehicleEvent.getId().getEventTimeStamp());
		position.setLatitude(Double.valueOf(vehicleEvent.getLatitude()));
		position.setLongitude(Double.valueOf(vehicleEvent.getLongitude()));
		position.setSpeed(Double.valueOf(vehicleEvent.getSpeed()));
		return position;
	}

	private Vehicleevent prepareVehicleEvents(Vehicle vehicle,
			String nearByVins, VehicleComposite vehicleComposite) {
		Vehicleevent vehicleEvent = new Vehicleevent();
		try {
			LOGGER.info("vin :" + vehicle.getVin());
			Vehicleevent latestEvent = fleetTrackingDeviceListenerBO
					.getLatestVe(nearByVins);
			VehicleeventId vehicleeventId = new VehicleeventId();
			vehicleeventId.setEventTimeStamp(latestEvent.getId()
					.getEventTimeStamp());
			vehicleeventId.setVin(vehicle.getVin());
			vehicleEvent.setLatitude(latestEvent.getLatitude());
			vehicleEvent.setLongitude(latestEvent.getLongitude());
			vehicleEvent.setEngine(latestEvent.getEngine());
			vehicleEvent.setSpeed(latestEvent.getSpeed());
			vehicleEvent.setIoevent(latestEvent.getIoevent());
			vehicleEvent.setDirection(latestEvent.getDirection());
			vehicleEvent.setServerTimeStamp(latestEvent.getServerTimeStamp());
			LOGGER.info("EventTimeStamp "
					+ latestEvent.getId().getEventTimeStamp() + " vin :"
					+ vehicle.getVin());
			vehicleEvent.setTags(STR_SIMULATOR);
			vehicleEvent.setId(vehicleeventId);

		} catch (Exception e) {
			LOGGER.error("SimulateHandler: PreparevehicleEvents:" + e
					+ " vin :" + vehicle.getVin());
			e.printStackTrace();
		}
		return vehicleEvent;
	}

	public void setSendDataContinuously(boolean sendDataContinuously) {
		this.sendDataContinuously = sendDataContinuously;
	}
}
