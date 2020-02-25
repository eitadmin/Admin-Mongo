package com.eiw.device.listener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.jboss.logging.Logger;

import com.eiw.device.handler.DeviceHandler;

public class ListenerServerThread extends Thread {

	private static final Logger LOGGER = Logger.getLogger("listener");

	private ServerSocket serverSocket = null;
	private Devices deviceType = null;

	ListenerServerThread(ServerSocket serverSocket, Devices deviceType) {
		this.serverSocket = serverSocket;
		this.deviceType = deviceType;
		this.setName("My::ServerStarterThread" + "::" + this.getName());
	}

	@SuppressWarnings("unchecked")
	public void run() {
		while (true) {
			Socket clientSocket = null;
			try {
				clientSocket = serverSocket.accept();
			} catch (IOException e) {
				LOGGER.error("IO Exception accepting the client socket "
						+ e.getMessage());
				break;
			}

			DeviceHandler deviceHandler = null;
			try {
				deviceHandler = (DeviceHandler) (deviceType
						.getDeviceHandlerClass().newInstance());
			} catch (Exception e) {
				e.printStackTrace();
			}
			deviceHandler.setClientSocket(clientSocket);
			deviceHandler.start();// start as Thread
		}
	}

}