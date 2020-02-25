package com.eiw.device.handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jboss.logging.Logger;

import com.eiw.device.ejb.FleetTrackingDeviceListenerBORemote;
import com.eiw.server.OSValidator;
import com.eiw.server.bo.BOFactory;
import com.eiw.server.fleettrackingpu.Vehicleevent;
import com.eiw.server.fleettrackingpu.VehicleeventId;

public class TestDeviceHandler extends DeviceHandler {

	private static final Logger LOGGER = Logger.getLogger("listener");

	public void handleDevice() {
		LOGGER.info("handleDevice: Entered TestDeviceHandler_5428 Handle Device:"
				+ new Date());
		FleetTrackingDeviceListenerBORemote entityManagerService = BOFactory
				.getFleetTrackingDeviceListenerBORemote();
		DataInputStream clientSocketDis = null;
		DataOutputStream dos = null;
		FileWriter writer = null;
		try {
			File temp = null;
			String path = "";
			this.clientSocket.setSoTimeout(1500000);
			LOGGER.info("handleDevice:Inside DOS trry block");
			clientSocketDis = new DataInputStream(
					this.clientSocket.getInputStream());

			LOGGER.info("handleDevice:Inside imei read UTF try block");
			// String imeiNo = clientSocketDis.readUTF();
			// LOGGER.info("handleDevice:exits imei read UTF try block");
			// VehicleComposite vehicleComposite = entityManagerService
			// .getVehicle(imeiNo);
			if (OSValidator.isWindows()) {
				path = "C:\\Windows\\Temp\\";
			} else if (OSValidator.isUnix()) {
				path = "/home/ubuntu/imagesdirectory/";
			}

			temp = new File(path + "DeviceTest_5428.txt");
			writer = new FileWriter(temp, true);
			do {
				String deviceMsg = clientSocketDis.readLine();
				if (deviceMsg != null) {
					writer.append(deviceMsg);
					writer.append('\n');
					dos = new DataOutputStream(
							this.clientSocket.getOutputStream());
					dos.writeUTF("Done");
					writer.append("Done");
					writer.flush();
				}
			} while (true);
			// dos.flush();

		} catch (Exception e) {
			LOGGER.error("TestDeviceHandler_5428: Close client socket" + e);
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cleanUpSockets(this.clientSocket, clientSocketDis, dos);
			LOGGER.info("DeviceCommunicatorThread:DeviceCommunicator Completed");
		}
		LOGGER.info("TestDeviceHandler_5428: Ended successfully::: ");
	}

}
