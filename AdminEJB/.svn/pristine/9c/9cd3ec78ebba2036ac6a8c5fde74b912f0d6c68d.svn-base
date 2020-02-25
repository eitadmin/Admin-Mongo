package com.eiw.cron;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class AlertConfigCacheEJB {
	@EJB
	AlertConfigEJB alertConfigEJB;

	@PostConstruct
	public void loadCache() {
		String vin = null;
		alertConfigEJB.startJobImplemented(vin);
		System.out.println("hi AlertConfigCacheEJB1");
		alertConfigEJB.startCompanySettingCache();
		alertConfigEJB.addInventory("null", "null");
	}

}
