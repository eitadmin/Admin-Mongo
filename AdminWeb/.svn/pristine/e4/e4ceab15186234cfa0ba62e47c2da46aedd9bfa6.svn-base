package com.eiw.server;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONException;
import org.json.JSONObject;

import com.eiw.cron.AlertConfigEJB;
import com.eiw.cron.AlertConfigEJBRemote;

@Stateless
@ServerEndpoint("/alertsmanagerendpoint")
public class AlertsManagerEndPoint {

	@EJB
	AlertConfigEJB alertConfigEJB;

	@OnMessage
	public void recieveMessage(String content, Session session) {
		try {
			JSONObject alertJson = new JSONObject(content);
			if (!alertJson.getString("oldVin").equalsIgnoreCase("")) {
				alertConfigEJB.alertsSessions.remove(alertJson
						.getString("oldVin"));
				alertConfigEJB.alertsSessions.put(session,
						alertJson.getString("newVin"));
			} else {
				alertConfigEJB.addAlertsSession(session,
						alertJson.getString("newVin"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@OnClose
	public void closeSession(Session session, CloseReason reason) {
		alertConfigEJB.removeAlertsSession(session, reason);
	}
}
