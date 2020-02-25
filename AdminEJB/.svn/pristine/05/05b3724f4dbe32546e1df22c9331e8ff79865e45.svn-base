package com.eiw.alerts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.eiw.device.ejb.VehicleComposite;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.fleettrackingpu.Alertconfig;
import com.eiw.server.fleettrackingpu.Heartbeatevent;
import com.eiw.server.fleettrackingpu.VehicleHasIo;
import com.eiw.server.fleettrackingpu.Vehiclealerts;
import com.eiw.server.fleettrackingpu.Vehicleevent;

public class CheckSOS implements CheckAlerts {
	AlertsEJBRemote alertsEJBRemote = null;
	AlertsManager alertsManager;
	Alertconfig alertConfig = null;
	Date lastUpdatedTime;

	public CheckSOS(AlertsManager alertsManager1) {
		alertsManager = alertsManager1;
		alertsEJBRemote = alertsManager.alertsEJBRemote;
	}

	public CheckSOS(Alertconfig alertConfig, Date lastUpdatedTime) {
		this.alertConfig = alertConfig;
		this.lastUpdatedTime = lastUpdatedTime;
	}

	public void manageAlert(List<Vehicleevent> vehicleevents,
			Alertconfig alertConfig, String vin, String plateNo) {

		List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();
		String address = null;
		String description = null;
		String latLngLink = null;
		AlertsEJB.LOGGER.debug("Inside CheckSOS Alert ->");
		for (Vehicleevent vehi : vehicleevents) {
			if (vehi.getIoevent() != null) {
				String[] ioevent = vehi.getIoevent().split(",");
				for (int i = 0; i < ioevent.length; i++) {
					String[] ioSource = ioevent[i].split("=");
					if (ioSource[0].equalsIgnoreCase("600")) {

						String io = ioSource[1];
						if (io.equalsIgnoreCase("SOS")) {
							String mobile2 = null;
							mobile2 = alertConfig.getSmsNumber();
							for (Vehicleevent ve : vehicleevents) {
								String eventTime = TimeZoneUtil
										.getStrTZDateTime(ve.getId()
												.getEventTimeStamp());
								address = alertsEJBRemote.getAddress(
										ve.getLatitude() + "",
										ve.getLongitude() + "");
								latLngLink = "http://maps.google.com/maps?q="
										+ ve.getLatitude() + ","
										+ ve.getLongitude();
								if (address.length() > 65) {
									address = address.substring(0, 65);
									address += "..";
								}
								if (!address
										.equalsIgnoreCase("OVER_QUERY_LIMIT")) {
									description = "Alert%0DType : SOS%0DVehicle:"
											+ plateNo
											+ "%0D%0DTime:"
											+ eventTime
											+ "%0D%0DAddress:"
											+ address;
								} else {
									description = "Alert%0DType : SOS%0DVehicle:"
											+ plateNo
											+ "%0D%0DTime:"
											+ eventTime
											+ "%0D%0DLat,Lng:"
											+ latLngLink;
								}
								Vehiclealerts va = new Vehiclealerts();
								va.setAlerttype("SOS");
								va.setDescription(description);
								va.setEventTimeStamp(ve.getId()
										.getEventTimeStamp());
								va.setLatlng(ve.getLatitude() + ","
										+ ve.getLongitude());
								va.setSmsmobile(mobile2);
								va.setVin(vin);
								va.setShowstatus(false);
								vehiclealerts.add(va);
							}
						}
					}

				}
			}
		}

		if (!vehiclealerts.isEmpty()) {
			lastUpdatedTime = alertsManager.persistVehicleAlert(alertConfig,
					vehiclealerts, lastUpdatedTime);
		}
	}

	@Override
	public String manageAlerts(List<Vehicleevent> vehicleEvents, String vin,
			String plateNo, List<VehicleHasIo> vehicleHasIo, int id,
			VehicleComposite vehicleComposite) {
		List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();
		String address = null;
		String description = null;
		String latLngLink = null;
		AlertsEJB.LOGGER.debug("Inside CheckSOS Alert ->");
		for (Vehicleevent vehi : vehicleEvents) {
			if (vehi.getIoevent() != null) {
				String[] ioevent = vehi.getIoevent().split(",");
				for (int i = 0; i < ioevent.length; i++) {
					String[] ioSource = ioevent[i].split("=");
					if (ioSource[0].equalsIgnoreCase("600")) {

						String io = ioSource[1];
						if (io.equalsIgnoreCase("SOS") || io.equalsIgnoreCase("SOS,")) {
							String mobile2 = null;
							mobile2 = alertConfig.getSmsNumber();
							for (Vehicleevent ve : vehicleEvents) {
								String eventTime = TimeZoneUtil
										.getStrTZDateTime(ve.getId()
												.getEventTimeStamp());
								address = alertsEJBRemote.getAddress(
										ve.getLatitude() + "",
										ve.getLongitude() + "");
								latLngLink = "http://maps.google.com/maps?q="
										+ ve.getLatitude() + ","
										+ ve.getLongitude();
								if (address.length() > 65) {
									address = address.substring(0, 65);
									address += "..";
								}
								if (!address
										.equalsIgnoreCase("OVER_QUERY_LIMIT")
										&& !address
												.equalsIgnoreCase("REQUEST_DENIED")) {
									description = "Alert%0DType : SOS%0DVehicle:"
											+ plateNo
											+ "%0D%0DTime:"
											+ eventTime
											+ "%0D%0DAddress:"
											+ address;
								} else {
									description = "Alert%0DType : SOS%0DVehicle:"
											+ plateNo
											+ "%0D%0DTime:"
											+ eventTime
											+ "%0D%0DLat,Lng:"
											+ latLngLink;
								}
								Vehiclealerts va = new Vehiclealerts();
								va.setAlerttype("SOS");
								va.setDescription(description);
								va.setEventTimeStamp(ve.getId()
										.getEventTimeStamp());
								va.setLatlng(ve.getLatitude() + ","
										+ ve.getLongitude());
								va.setSmsmobile(mobile2);
								va.setVin(vin);
								va.setShowstatus(false);
								vehiclealerts.add(va);
							}
						}
					}

				}
			}
		}

		if (!vehiclealerts.isEmpty()) {
			lastUpdatedTime = alertsManager.persistVehicleAlert(alertConfig,
					vehiclealerts, lastUpdatedTime);
		}

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
