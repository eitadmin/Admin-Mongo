package com.eiw.alerts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eiw.device.ejb.VehicleComposite;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.fleettrackingpu.Alertconfig;
import com.eiw.server.fleettrackingpu.Heartbeatevent;
import com.eiw.server.fleettrackingpu.VehicleHasIo;
import com.eiw.server.fleettrackingpu.Vehiclealerts;
import com.eiw.server.fleettrackingpu.Vehicleevent;

import org.json.JSONException;
import org.json.JSONObject;

public class CheckPanicAlert implements CheckAlerts {
	private static final String DATE_DDHHMMSS = "yyyy-MM-dd HH:mm:ss";
	SimpleDateFormat sdfTime = new SimpleDateFormat(DATE_DDHHMMSS);
	public static Map<String, List<Vehicleevent>> panicAlertVehicleeventMap = new HashMap<String, List<Vehicleevent>>();
	AlertsManager alertsManager;
	AlertsEJBRemote alertsEJBRemote = null;
	List<Vehicleevent> listOfPanicAlertEvent = new ArrayList<Vehicleevent>();
	Alertconfig alertConfig = null;
	Date lastUpdatedTime;
	boolean firstEnter = false;

	public CheckPanicAlert(AlertsManager alertsManager1) {
		alertsManager = alertsManager1;
		alertsEJBRemote = alertsManager.alertsEJBRemote;
	}

	public CheckPanicAlert(Alertconfig alertConfig, Date lastUpdatedTime) {
		// TODO Auto-generated constructor stub
		this.alertConfig = alertConfig;
		this.lastUpdatedTime = lastUpdatedTime;
	}

	@Override
	public String manageAlerts(List<Vehicleevent> vehicleEvents, String vin,
			String plateNo, List<VehicleHasIo> vehicleHasIo, int id,
			VehicleComposite vehicleComposite) {
		// TODO Auto-generated method stub
		String range = null, mobile = null;
		String address = null;
		String description = null;
		String latLngLink = null;
		boolean flag = false;
		int io = 0, diVal = 0;
		List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();

		for (VehicleHasIo hasIo : vehicleHasIo) {
			if (hasIo.getIoname().equalsIgnoreCase("Panic Alert")) {
				io = hasIo.getId().getIo();
				flag = true;
				break;
			}
		}
		/*
		 * for (Vehicleevent e : vehicleEvents) {
		 * alertsEJBRemote.insertTicket(e, vin, plateNo, vehicleComposite); }
		 */
		if (flag) {
			for (Vehicleevent e : vehicleEvents) {

				switch (io) {
				case 2:
					if (e.getDi2() != null) {
						diVal = e.getDi2();
						break;
					}
				case 3:
					if (e.getDi3() != null) {
						diVal = e.getDi3();
						break;
					}
				case 4:
					if (e.getDi4() != null) {
						diVal = e.getDi4();
						break;
					}
				}

				if (diVal == 1 && id == 0) {

					mobile = alertConfig.getSmsNumber();
					range = alertConfig.getAlertRange();
					String eventTime = TimeZoneUtil.getStrTZDateTime(e.getId()
							.getEventTimeStamp());

					latLngLink = "http://maps.google.com/maps?q="
							+ e.getLatitude() + "," + e.getLongitude();

					description = "Panic Alert%0DVehicle:" + plateNo
							+ "%0D%0DTime:" + eventTime + "%0D%0DAddress: "
							+ latLngLink;

					Vehiclealerts va = new Vehiclealerts();
					va.setAlerttype("PANIC");
					va.setDescription(description);
					va.setEventTimeStamp(e.getId().getEventTimeStamp());
					va.setLatlng(e.getLatitude() + "," + e.getLongitude());
					va.setSmsmobile(mobile);
					va.setVin(vin);
					va.setShowstatus(false);
					vehiclealerts.add(va);
					if (!vehiclealerts.isEmpty()) {
						lastUpdatedTime = alertsManager.persistVehicleAlert(
								alertConfig, vehiclealerts, lastUpdatedTime);
					}
				} else if (diVal == 1 && id == 1) {
					alertsEJBRemote.insertTicket(e, vin, plateNo,
							vehicleComposite);
				}
			}
			return "success";
		} else {
			if (vehicleComposite.getDeviceModel().equalsIgnoreCase("AIS140")) {
				for (Vehicleevent e : vehicleEvents) {
					for (Vehicleevent ve : vehicleEvents) {
						String deviceData = ve.getTags();
						if (deviceData.startsWith("{")) {
							try {
								JSONObject obj = new JSONObject(deviceData);

								if (obj.getString("packetType")
										.equalsIgnoreCase("EA")) {
									String mobile2 = null;
									mobile2 = alertConfig.getSmsNumber();
									String eventTime = TimeZoneUtil
											.getStrTZDateTime(ve.getId()
													.getEventTimeStamp());
									address = alertsEJBRemote.getAddress(
											ve.getLatitude() + "",
											ve.getLongitude() + "");
									latLngLink = "http://maps.google.com/maps?q="
											+ ve.getLatitude()
											+ ","
											+ ve.getLongitude();
									if (address.length() > 65) {
										address = address.substring(0, 65);
										address += "..";
									}
									if (!address
											.equalsIgnoreCase("OVER_QUERY_LIMIT")) {
										description = "Alert%0DType : PANIC%0DVehicle:"
												+ plateNo
												+ "%0D%0DTime:"
												+ eventTime
												+ "%0D%0DAddress:"
												+ address;
									} else {
										description = "Alert%0DType : PANIC%0DVehicle:"
												+ plateNo
												+ "%0D%0DTime:"
												+ eventTime
												+ "%0D%0DLat,Lng:"
												+ latLngLink;
									}
									Vehiclealerts va = new Vehiclealerts();
									va.setAlerttype("PANIC");
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
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}
				if (!vehiclealerts.isEmpty()) {
					lastUpdatedTime = alertsManager.persistVehicleAlert(
							alertConfig, vehiclealerts, lastUpdatedTime);
				}

			}
		}
		return null;
	}

	@Override
	public void addAlertManager(AlertsManager alertsManager) {
		this.alertsManager = alertsManager;
		this.alertsEJBRemote = alertsManager.alertsEJBRemote;
		// TODO Auto-generated method stub

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
