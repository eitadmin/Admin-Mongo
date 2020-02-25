package com.eiw.cron;

import javax.ejb.LocalBean;
import javax.websocket.CloseReason;
import javax.websocket.Session;

@LocalBean
public interface AlertConfigEJBRemote {

	void startJobImplemented(String vin);

	void startCompanySettingCache();

	void addAlertsSession(Session session, String content);

	void removeAlertsSession(Session session, CloseReason reason);
}
