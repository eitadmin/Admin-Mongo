package com.eiw.device.listener;

//Minimal TCP/IP Server
import java.io.IOException;
import java.net.ServerSocket;

import org.jboss.logging.Logger;

import com.eiw.device.teltonika.AvlData;
import com.eiw.device.teltonika.AvlDataFM4;
import com.eiw.device.teltonika.AvlDataGH;
import com.eiw.device.teltonika.CodecStore;

public class ListenerServer {

	private ServerSocket[] serverSockets;

	private static final Logger LOGGER = Logger.getLogger("listener");

	public void startServer(Devices[] deviceTypesUsed, String userId) {
		for (Devices deviceType : deviceTypesUsed) {
			if (deviceType == Devices.TELTONIKA) {
				// register supported codecs
				CodecStore.getInstance().register(AvlData.getCodec());
				CodecStore.getInstance().register(AvlDataFM4.getCodec());
				CodecStore.getInstance().register(AvlDataGH.getCodec());
				LOGGER.info("Codecs registered for Teltonika");
			}
			startServer(deviceType);
		}
	}

	private String startServer(Devices deviceType) {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(deviceType.getPortNumber());
			LOGGER.info("startServer:server socket created for "
					+ deviceType.name() + " " + deviceType.getPortNumber());
		} catch (IOException e) {
			LOGGER.error("startServer:Exception creating server socket for "
					+ deviceType.name() + e);
			return "Exception creating server socket for " + deviceType.name();
		}

		// Run the listen/accept loop forever
		ListenerServerThread serverStarterThread = new ListenerServerThread(
				serverSocket, deviceType);
		serverStarterThread.start();
		LOGGER.info("startServer: Server for " + deviceType.name()
				+ " started successfully");
		return "Server for " + deviceType.name() + " started successfully";
	}

	private void cleanUpSocketsServer() {
		try {
			for (ServerSocket serverSocket : serverSockets) {
				if (serverSocket != null) {
					serverSocket.close();
					serverSocket = null;
				}
			}
		} catch (IOException ie2) {
			LOGGER.error("cleanUpSockets:Exception in cleanUpSocket" + ie2);
		} finally {
			serverSockets = null;
		}
	}

	public void stopServer(String userId) {
		cleanUpSocketsServer();
		LOGGER.info("stopServer:TCP/IP servers are stopped");
	}
}