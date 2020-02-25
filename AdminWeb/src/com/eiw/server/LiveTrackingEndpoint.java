package com.eiw.server;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.websocket.CloseReason;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.eiw.device.ejb.FleetTrackingDeviceListenerBORemote;

@Stateless
@ServerEndpoint("/livetrackingendpoint")
public class LiveTrackingEndpoint {
	@EJB
	private FleetTrackingDeviceListenerBORemote bo;

	@OnMessage
	public void receiveMessage(String message, Session session) {
		bo.registerSession(message, session);
	}

	public void open(Session session) {
		System.out.println("Open session:" + session.getId());
		session.setMaxIdleTimeout(3600000);// session timeout 60 mins
	}

	public void close(Session session, CloseReason c) {
		System.out.println("Closing:" + session.getId());
	}
}
