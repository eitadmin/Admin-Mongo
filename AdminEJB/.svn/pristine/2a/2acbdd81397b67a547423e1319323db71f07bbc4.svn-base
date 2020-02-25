package com.eiw.cron.skywave;

import java.util.List;

import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.eiw.server.bo.BOFactory;

public class SkywavePollerThread extends Thread implements Job {
	private static final Logger LOGGER = Logger.getLogger("skywave");

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		LOGGER.info("SkywavePollerThread::Entered into this class");

		if (false) {
			return;
		}
		try {
			// execute poll data method of EJB
			SkywaveInjectingEJBRemote skywaveInjectionEJBRemote = BOFactory
					.getSkywaveInjectionEJBRemote();

			// calling SkywaveInjectionEJBRemote for lastskyevnt id
			int lastEvtId = skywaveInjectionEJBRemote.getLastSkyId();

			// calling poll method to retrieve skywave records after
			// lastId
			List<Object[]> resultList = skywaveInjectionEJBRemote
					.pollSkyToVehicleEvent(lastEvtId);

			// injecting new records to vehicleevent
			skywaveInjectionEJBRemote.injectSkyToVehicleEvent(resultList);

		} catch (Exception e) {

		}
	}

}
