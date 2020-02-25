package com.eiw.server;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.websocket.CloseReason;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.eiw.admin.ejb.EMSAdminPortal;

@Stateless
@ServerEndpoint("/ticketmanagementendpoint")
public class TicketManagementEndpoint {

	@EJB 
	EMSAdminPortal emsAdmin;
	
	@OnMessage
	public void recieveMessage(String message, Session session) {
		emsAdmin.registerTicketManagementSession(session, message);
	}

	public void closeSession(Session session, CloseReason reason) {
		emsAdmin.closeTicketManagementSession(session, reason);
	}

}
