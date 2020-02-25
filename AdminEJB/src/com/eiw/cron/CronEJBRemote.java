package com.eiw.cron;

import javax.ejb.Local;

@Local
public interface CronEJBRemote {
	void startCron();

}
