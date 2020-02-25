package com.skt.alerts;

import java.util.Date;

import org.jboss.logging.Logger;

import com.eiw.device.ejb.FleetTrackingDeviceListenerBORemote;
import com.eiw.device.ejb.VehicleComposite;
import com.eiw.device.handler.MeiTrackDeviceHandler;
import com.eiw.device.meitrack.Position;
import com.eiw.server.bo.BOFactory;

public class MeitrackSimulator {

	private static final Logger LOGGER = Logger.getLogger("listener");
	FleetTrackingDeviceListenerBORemote fleetTrackingDeviceListenerBO = BOFactory
			.getFleetTrackingDeviceListenerBORemote();

	public void handleDevice1(String deviceId, Date time, boolean valid,
			double latitude, double longitude, double altitude, double speed,
			double course, String rfid, String engine) {

		LOGGER.info("Entered MeiTrack five mins Handle Device:" + new Date());

		try {

			Position position = new Position("0", time, valid, latitude,
					longitude, altitude, speed, course);
			position.setDeviceId1(deviceId);
			position.setRfid(rfid);
			position.setMileage("100");
			position.setDI3(Integer.parseInt(engine));
			LOGGER.info("Decoded data ::: " + position);

			/* Obtaining IMEI NO. from rawData */
			String imeiNo = deviceId;
			if (imeiNo.length() < 15 && imeiNo.startsWith("1")) {
				imeiNo = "0" + imeiNo;
			}
			VehicleComposite vehicleComposite = fleetTrackingDeviceListenerBO
					.getVehicle(imeiNo);

			LOGGER.info("SKTLog:MeiTrackDeviceHandler (1):Time::: "
					+ position.getTime() + " and MEITRACKimei = "
					+ position.getDeviceId() + " and RFID = "
					+ position.getRfid() + " and Company = "

					+ vehicleComposite.getVehicle().getCompanyId()
					+ " and Branch = "
					+ vehicleComposite.getVehicle().getBranchId());

			MeiTrackDeviceHandler meiTrackDeviceHandler = new MeiTrackDeviceHandler();

			meiTrackDeviceHandler.prepareStaticElements(vehicleComposite);
			meiTrackDeviceHandler.getSktHandlHanlerMethods()
					.persistEventAndGenerateAlert(position, vehicleComposite,
							imeiNo, "meitrack", null);

		} catch (Exception e) {
			LOGGER.error("Exception while receiving the Message "
					+ e.getMessage());
			e.printStackTrace();
		} finally {
			LOGGER.info("DeviceCommunicatorThread:DeviceCommunicator Completed");
		}
	}
}