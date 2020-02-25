package com.eiw.cron.skywave;

import java.util.List;

import javax.ejb.Local;

@Local
public interface SkywaveInjectingEJBRemote {
	String injectSkyToVehicleEvent(List<Object[]> obj);

	int getLastSkyId();

	List<Object[]> pollSkyToVehicleEvent(int obj);
}
