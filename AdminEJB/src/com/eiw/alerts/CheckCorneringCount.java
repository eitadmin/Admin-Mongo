package com.eiw.alerts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.eiw.client.dto.VehicleData;
import com.eiw.device.ejb.VehicleComposite;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.fleettrackingpu.Alertconfig;
import com.eiw.server.fleettrackingpu.Heartbeatevent;
import com.eiw.server.fleettrackingpu.VehicleHasIo;
import com.eiw.server.fleettrackingpu.Vehiclealerts;
import com.eiw.server.fleettrackingpu.Vehicleevent;

public class CheckCorneringCount implements CheckAlerts {
	AlertsEJBRemote alertsEJBRemote = null;
	AlertsManager alertsManager;
	Alertconfig alertConfig = null;
	Date lastUpdatedTime;

	public CheckCorneringCount(AlertsManager alertsManager1) {
		alertsManager = alertsManager1;
		alertsEJBRemote = alertsManager.alertsEJBRemote;
	}

	public CheckCorneringCount(Alertconfig alertConfig, Date lastUpdatedTime) {
		this.alertConfig = alertConfig;
		this.lastUpdatedTime = lastUpdatedTime;
	}

	@Override
	public String manageAlerts(List<Vehicleevent> vehicleEvents, String vin,
			String plateNo, List<VehicleHasIo> vehicleHasIo, int id,
			VehicleComposite vehicleComposite) {
		List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();
		String mobile = null;
		int corneringCount = 0;
		mobile = alertConfig.getSmsNumber();
		Vehicleevent ve = vehicleEvents.get(0);
		if (vehicleComposite.getCompanytrackDevice()
				.getCompanytrackdevicemodels().getId().getManufacturerName()
				.equalsIgnoreCase("Ruptela")) {
			if (vehicleComposite.getVehicleData() != null) {
				VehicleData data = vehicleComposite.getVehicleData();
				corneringCount = data.getCorneringLimit() != null ? Integer
						.parseInt(data.getCorneringLimit()) : 0;
			}
			if (corneringCount != 0) {
				try {
					String eventTime = TimeZoneUtil.getStrTZDateTime(ve.getId()
							.getEventTimeStamp());
					String latlng = ve.getLatitude() + "," + ve.getLongitude();
					String description = "Drift Alert%0DPlateNo:" + plateNo
							+ "%0DCornering Count:" + corneringCount
							+ "%0D%0DTime:" + eventTime + "%0D%0DLocation"
							+ latlng + "=" + ve.getSpeed() + "=";
					Vehiclealerts va = new Vehiclealerts();
					va.setAlerttype(AlertsManager.enumAlerts.DRIFT.name());
					va.setDescription(description);
					va.setLatlng(latlng);
					va.setEventTimeStamp(ve.getId().getEventTimeStamp());
					va.setSmsmobile(mobile);
					va.setVin(vin);
					va.setShowstatus(false);
					vehiclealerts.add(va);
				} catch (Exception e) {
					return null;
				}
			}
		} else if (vehicleComposite.getCompanytrackDevice()
				.getCompanytrackdevicemodels().getId().getManufacturerName()
				.equalsIgnoreCase("Teltonika")) {
			if (ve.getIoevent() != null) {
				String[] ioevent = ve.getIoevent().split(",");
				for (int i = 0; i < ioevent.length; i++) {
					String[] ioSource = ioevent[i].split("=");
					if (ioSource[0].equalsIgnoreCase("253")) {
						String io = ioSource[1];
						if (io.equalsIgnoreCase("3")) {
							try {
								String eventTime = TimeZoneUtil
										.getStrTZDateTime(ve.getId()
												.getEventTimeStamp());
								String latlng = ve.getLatitude() + ","
										+ ve.getLongitude();
								String description = "Drift Alert%0DPlateNo:"
										+ plateNo + "%0D%0DTime:" + eventTime
										+ "%0D%0DLocation:" + latlng + "="
										+ ve.getSpeed() + "=";
								Vehiclealerts va = new Vehiclealerts();
								va.setAlerttype(AlertsManager.enumAlerts.DRIFT
										.name());
								va.setDescription(description);
								va.setLatlng(latlng);
								va.setEventTimeStamp(ve.getId()
										.getEventTimeStamp());
								va.setSmsmobile(mobile);
								va.setVin(vin);
								va.setShowstatus(false);
								vehiclealerts.add(va);
							} catch (Exception e) {
								return null;
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