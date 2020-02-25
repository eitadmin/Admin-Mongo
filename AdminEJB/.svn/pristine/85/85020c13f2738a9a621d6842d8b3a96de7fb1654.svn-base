package com.eiw.alerts;

import java.util.Date;
import java.util.List;

import com.eiw.device.ejb.VehicleComposite;
import com.eiw.server.fleettrackingpu.Heartbeatevent;
import com.eiw.server.fleettrackingpu.VehicleHasIo;
import com.eiw.server.fleettrackingpu.Vehicleevent;

public interface CheckAlerts {

	String manageAlerts(List<Vehicleevent> vehicleEvents, String vin,
			String plateNo, List<VehicleHasIo> vehicleHasIo, int id,
			VehicleComposite vehicleComposite);

	void addAlertManager(AlertsManager alertsManager);

	public void setLastUpdatedTime(Date lastUpdated);

	String manageHbAlerts(Heartbeatevent heartbeatevent, String vin,
			String PlateNo, VehicleComposite vehicleComposite);
}
