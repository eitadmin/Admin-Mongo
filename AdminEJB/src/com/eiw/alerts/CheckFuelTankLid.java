package com.eiw.alerts;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.eiw.device.ejb.VehicleComposite;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.fleettrackingpu.Alertconfig;
import com.eiw.server.fleettrackingpu.Fueltanklid;
import com.eiw.server.fleettrackingpu.FueltanklidId;
import com.eiw.server.fleettrackingpu.Heartbeatevent;
import com.eiw.server.fleettrackingpu.VehicleHasIo;
import com.eiw.server.fleettrackingpu.Vehiclealerts;
import com.eiw.server.fleettrackingpu.Vehicleevent;

public class CheckFuelTankLid implements CheckAlerts {
	AlertsEJBRemote alertsEJBRemote = null;
	AlertsManager alertsManager;
	String unit, alerttypename;
	int io;
	Alertconfig alertConfig = null;
	Date lastUpdatedTime;

	public CheckFuelTankLid(AlertsManager alertsManager1) {
		alertsManager = alertsManager1;
		alertsEJBRemote = alertsManager.alertsEJBRemote;
	}

	public CheckFuelTankLid(Alertconfig alertConfig, Date lastUpdatedTime) {
		this.alertConfig = alertConfig;
		this.lastUpdatedTime = lastUpdatedTime;
	}

	public void manageAlert(List<Vehicleevent> vehicleevents, int ioVal,
			String vin, String plateNo, Alertconfig alertconfig,
			List<VehicleHasIo> vehicleHasIo, Vehicleevent vehicleevent,
			Fueltanklid preFueltanklid) {

		for (VehicleHasIo hasIo : vehicleHasIo) {
			io = hasIo.getId().getIo();
			unit = hasIo.getUnit();
			alerttypename = hasIo.getIoname();
			break;
		}

		List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();
		List<Fueltanklid> fueltanklids = new ArrayList<Fueltanklid>();
		boolean isFirstEnabled = true, isFirstDisabled = true;
		NumberFormat nmbrformat = new DecimalFormat("#0.00");
		float ioVE = 0;
		int ioMV = 0;

		for (Vehicleevent vehi : vehicleevents) {
			String[] ioevent = vehi.getIoevent().split(",");
			for (int i = 0; i < ioevent.length; i++) {
				String[] ioSource = ioevent[i].split("=");
				if (ioSource[0].equalsIgnoreCase(String.valueOf(ioVal))) {
					int ioV = Integer.parseInt(ioSource[1]);
					if (io > 0) {
						if ((io == 9) && (vehicleevent.getAi1() != null)) {
							ioVE = alertsEJBRemote.getanalogReading(vin, io,
									vehicleevent.getAi1());
							ioMV = vehicleevent.getAi1();
						} else if ((io == 10)
								&& (vehicleevent.getAi2() != null)) {
							ioVE = alertsEJBRemote.getanalogReading(vin, io,
									vehicleevent.getAi2());
							ioMV = vehicleevent.getAi2();
						} else if ((io == 11)
								&& (vehicleevent.getAi3() != null)) {
							ioVE = alertsEJBRemote.getanalogReading(vin, io,
									vehicleevent.getAi3());
							ioMV = vehicleevent.getAi3();
						} else if ((io == 19)
								&& (vehicleevent.getAi4() != null)) {
							ioVE = alertsEJBRemote.getanalogReading(vin, io,
									vehicleevent.getAi4());
							ioMV = vehicleevent.getAi4();
						}
					}
					if ((ioV == 1) && (isFirstEnabled) && (ioVE != 0)) {
						if (preFueltanklid == null) {
							Fueltanklid fueltanklid = new Fueltanklid();
							FueltanklidId id = new FueltanklidId();
							id.setVin(vehi.getId().getVin());
							id.setOpenEventTimeStamp(vehi.getId()
									.getEventTimeStamp());
							id.setOpenMillivolts(ioMV);
							id.setOpenLitres(ioVE);
							id.setLastUpdDt(vehi.getId().getEventTimeStamp());
							id.setLastUpdBy(alertconfig.getId().getCompanyId());
							id.setLidStatus("Open");
							fueltanklid.setId(id);
							fueltanklids.add(fueltanklid);
							isFirstEnabled = false;
						}
					} else if ((ioV == 0) && (isFirstDisabled) && (ioVE != 0)) {
						if (preFueltanklid != null) {
							Fueltanklid fueltanklid = new Fueltanklid();
							FueltanklidId id = new FueltanklidId();
							id.setVin(vehi.getId().getVin());
							id.setOpenEventTimeStamp(preFueltanklid.getId()
									.getOpenEventTimeStamp());
							id.setOpenMillivolts(preFueltanklid.getId()
									.getOpenMillivolts());
							id.setOpenLitres(preFueltanklid.getId()
									.getOpenLitres());
							id.setCloseEventTimeStamp(vehi.getId()
									.getEventTimeStamp());
							id.setCloseMillivolts(ioMV);
							id.setCloseLitres(ioVE);
							id.setLastUpdDt(vehi.getId().getEventTimeStamp());
							id.setLastUpdBy(alertconfig.getId().getCompanyId());
							id.setLidStatus("Close");
							fueltanklid.setId(id);
							fueltanklids.add(fueltanklid);
							isFirstDisabled = false;
							String eventTime = TimeZoneUtil
									.getStrTZDateTime(vehi.getId()
											.getEventTimeStamp());
							String mobile2 = alertconfig.getSmsNumber();
							String description = "Alert%0DType : Fuel Tank Fill%0Dvehicle:"
									+ plateNo
									+ "%0D%0DTime:"
									+ eventTime
									+ "%0DLitres: "
									+ (nmbrformat.format(ioVE
											- preFueltanklid.getId()
													.getOpenLitres()));
							Vehiclealerts va = new Vehiclealerts();
							va.setAlerttype("FUEL FILL");
							va.setDescription(description);
							va.setEventTimeStamp(vehi.getId()
									.getEventTimeStamp());
							va.setLatlng(vehi.getLatitude() + ","
									+ vehi.getLongitude());
							va.setSmsmobile(mobile2);
							va.setVin(vin);
							va.setShowstatus(false);
							vehiclealerts.add(va);
						}
					}
					break;
				}
			}
		}
		if (!fueltanklids.isEmpty()) {
			alertsEJBRemote.insertFuelTankLid(fueltanklids);
		}
		if (!vehiclealerts.isEmpty()) {
			lastUpdatedTime = alertsManager.persistVehicleAlert(alertconfig,
					vehiclealerts, lastUpdatedTime);
		}
	}

	@Override
	public String manageAlerts(List<Vehicleevent> vehicleEvents, String vin,
			String plateNo, List<VehicleHasIo> vehicleHasIo, int id,
			VehicleComposite vehicleComposite) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addAlertManager(AlertsManager alertsManager) {
		this.alertsManager = alertsManager;
		this.alertsEJBRemote = alertsManager.alertsEJBRemote;

	}

	@Override
	public void setLastUpdatedTime(Date lastUpdated) {
		// TODO Auto-generated method stub

	}

	@Override
	public String manageHbAlerts(Heartbeatevent heartbeatevent, String vin,
			String PlateNo, VehicleComposite vehicleComposite) {
		// TODO Auto-generated method stub
		return null;
	}
}