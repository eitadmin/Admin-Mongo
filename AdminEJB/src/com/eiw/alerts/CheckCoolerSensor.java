package com.eiw.alerts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.eiw.device.ejb.VehicleComposite;
import com.eiw.device.handler.MeiTrackDeviceHandler;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.bo.BOFactory;
import com.eiw.server.bo.RuptelaDeviceMgmtRemote;
import com.eiw.server.fleettrackingpu.Alertconfig;
import com.eiw.server.fleettrackingpu.Companytrackdevice;
import com.eiw.server.fleettrackingpu.Heartbeatevent;
import com.eiw.server.fleettrackingpu.VehicleHasIo;
import com.eiw.server.fleettrackingpu.Vehiclealerts;
import com.eiw.server.fleettrackingpu.Vehicleevent;

public class CheckCoolerSensor implements CheckAlerts {

	AlertsManager alertsManager;
	private static final String STR_TEMP = "TEMPERATURESENSOR";
	Alertconfig alertConfig = null;
	Date lastUpdatedTime;
	public RuptelaDeviceMgmtRemote ruptelaDeviceMgmtRemote = BOFactory
			.getRuptelaDeviceMgmtEJBRemote();

	public CheckCoolerSensor(AlertsManager alertsManager1) {
		alertsManager = alertsManager1;
	}

	public CheckCoolerSensor(Alertconfig alertConfig, Date lastUpdatedTime) {
		this.alertConfig = alertConfig;
		this.lastUpdatedTime = lastUpdatedTime;
	}

	public void manageAlert(List<Vehicleevent> listOfEvents,
			Alertconfig alertConfig, String vin, String plateNo, int id) {
		List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();
		String mobile = null;
		int min = -1, max = -1;

		// alert config for battery voltage Alert
		String[] range = alertConfig.getAlertRange().split("_");
		mobile = alertConfig.getSmsNumber();
		if (range.length > 1) {
			min = Integer.valueOf(range[0]);
			max = Integer.valueOf(range[1]);
		} else {
			return;
		}

		Vehicleevent veLow = null, veHigh = null;
		Vehicleevent ve0 = (Vehicleevent) listOfEvents.get(0);

		int initLow = -1, initHigh = -1;
		try {
			switch (id) {
			case 1:
				initLow = (ve0.getTempSensor1()).intValue();
				initHigh = (ve0.getTempSensor1()).intValue();
				break;
			case 2:
				initLow = (ve0.getTempSensor2()).intValue();
				initHigh = (ve0.getTempSensor2()).intValue();
				break;
			case 3:
				initLow = (ve0.getTempSensor3()).intValue();
				initHigh = (ve0.getTempSensor3()).intValue();
				break;
			default:
				break;
			}

		} catch (Exception e) {
			return;
		}

		// For Low
		for (int i = 0; i < listOfEvents.size(); i++) {
			Vehicleevent ve = (Vehicleevent) listOfEvents.get(i);
			int val = -1;
			try {
				switch (id) {
				case 1:
					val = (ve.getTempSensor1()).intValue();
					break;
				case 2:
					val = (ve.getTempSensor2()).intValue();
					break;
				case 3:
					val = (ve.getTempSensor3()).intValue();
					break;
				default:
					break;
				}

			} catch (Exception e) {
				return;
			}

			if (val == -1) {
				return;
			}

			if (val <= initLow) {
				initLow = val;
				veLow = ve;
			}
		}
		initLow = initLow / 10;
		// For High
		for (int i = 0; i < listOfEvents.size(); i++) {
			Vehicleevent ve = (Vehicleevent) listOfEvents.get(i);
			int val = -1;
			try {
				switch (id) {
				case 1:
					val = (ve.getTempSensor1()).intValue();
					break;
				case 2:
					val = (ve.getTempSensor2()).intValue();
					break;
				case 3:
					val = (ve.getTempSensor3()).intValue();
					break;
				default:
					break;
				}

			} catch (Exception e) {
				return;
			}

			if (val == -1) {
				return;
			}
			if (val >= initHigh) {
				initHigh = val;
				veHigh = ve;
			}
		}
		initHigh = initHigh / 10;
		if ((initLow == -1) || (initHigh == -1)) {
			return;
		}

		// Low Alert
		if ((veLow != null) && (initLow < min)) {
			try {
				Date date = veLow.getId().getEventTimeStamp();
				String latlng = veLow.getLatitude() + ","
						+ veLow.getLongitude();
				String time = TimeZoneUtil.getStrTZDateTime(date);

				String description = "Alert%0DType : Temperature Sensor " + id
						+ "- Low%0Dvehicle:" + plateNo + "%0DValue: " + initLow
						+ " &deg; C%0DLimit : " + min + " C - " + max
						+ " C %0DTime:" + time;
				Vehiclealerts va = new Vehiclealerts();
				va.setAlerttype(STR_TEMP + id);
				va.setDescription(description);
				va.setEventTimeStamp(date);
				va.setLatlng(latlng);
				va.setSmsmobile(mobile);
				va.setVin(vin);
				va.setShowstatus(false);
				vehiclealerts.add(va);
			} catch (Exception e) {
				return;
			}
		}

		// High Alert
		if ((veHigh != null) && (initHigh > max)) {
			try {
				Date date = veHigh.getId().getEventTimeStamp();
				String latlng = veHigh.getLatitude() + ","
						+ veHigh.getLongitude();
				String time = TimeZoneUtil.getStrTZDateTime(date);

				String description = "Alert%0DType : Temperature Sensor " + id
						+ "- High%0Dvehicle:" + plateNo + "%0DValue: "
						+ initHigh + " C%0DLimit : " + min + " C - " + max
						+ " C %0DTime:" + time;
				Vehiclealerts va = new Vehiclealerts();
				va.setAlerttype(STR_TEMP + id);
				va.setDescription(description);
				va.setEventTimeStamp(date);
				va.setLatlng(latlng);
				va.setSmsmobile(mobile);
				va.setVin(vin);
				va.setShowstatus(false);
				vehiclealerts.add(va);
			} catch (Exception e) {
				return;
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
		String mobile = null;
		int min = -1, max = -1;
		String errorCode = alertsManager.alertsEJBRemote
				.getPreferencesData("deviceErrorCode", vehicleComposite
						.getVehicle().getCompanyId());

		JSONObject deviceErrorcode;
		try {
			deviceErrorcode = new JSONObject(errorCode);

			JSONObject jsonErrorcode = null;

			Companytrackdevice companytrackDevice = vehicleComposite
					.getCompanytrackDevice();
			String deviceType = companytrackDevice
					.getCompanytrackdevicemodels().getId()
					.getManufacturerName() == null ? "NULL"
					: companytrackDevice.getCompanytrackdevicemodels().getId()
							.getManufacturerName();
			if (deviceErrorcode.has(deviceType)) {
				jsonErrorcode = new JSONObject(
						deviceErrorcode.getString(deviceType));

			}

			// alert config for battery voltage Alert
			String[] range = alertConfig.getAlertRange().split("_");
			mobile = alertConfig.getSmsNumber();
			if (range.length > 1) {
				min = Integer.valueOf(range[0]);
				max = Integer.valueOf(range[1]);
			} else {
				return null;
			}

			Vehicleevent veLow = null, veHigh = null;
			Vehicleevent ve0 = (Vehicleevent) vehicleEvents.get(0);

			int initLow = -1, initHigh = -1;
			boolean buzzerCheck = true;
			if (alertConfig.getSubAlertType() != null
					&& alertConfig.getSubAlertType().equalsIgnoreCase(
							"TEMPERATURESENSOR_BUZZER") && ve0.getDi2() == 0) {
				buzzerCheck = false;
				Vehicleevent prevVE = MeiTrackDeviceHandler.getPrevVE(vin);
				if (prevVE != null && prevVE.getDi2() == 1)
					ruptelaDeviceMgmtRemote.sendCommand("12345",
							companytrackDevice.getImeiNo(), "buzzerOffwithLED");
			}
			if (buzzerCheck) {

				try {
					String tags = ve0.getTags();
					if (tags != null && tags.contains("TEMP")) {
						JSONObject tempJson = new JSONObject(tags);
						switch (id) {
						case 1:
							if (tempJson.has("TEMP1")) {
								initLow = tempJson.getInt("TEMP1");
								initHigh = tempJson.getInt("TEMP1");
							}
							break;
						case 2:
							if (tempJson.has("TEMP2")) {
								initLow = tempJson.getInt("TEMP2");
								initHigh = tempJson.getInt("TEMP2");
							}
							break;
						case 3:
							if (tempJson.has("TEMP3")) {
								initLow = tempJson.getInt("TEMP3");
								initHigh = tempJson.getInt("TEMP3");
							}
							break;
						case 4:
							if (tempJson.has("TEMP4")) {
								initLow = tempJson.getInt("TEMP4");
								initHigh = tempJson.getInt("TEMP4");
							}
							break;
						default:
							break;
						}
					}
				} catch (Exception e) {
					return null;
				}

				// For Low
				for (int i = 0; i < vehicleEvents.size(); i++) {
					Vehicleevent ve = (Vehicleevent) vehicleEvents.get(i);
					int val = -1;

					try {
						String tempSensors = ve.getTags();
						if (tempSensors != null && tempSensors.contains("TEMP")) {
							JSONObject tempJson = new JSONObject(tempSensors);
							switch (id) {
							case 1:
								if (tempJson.has("TEMP1"))
									val = tempJson.getInt("TEMP1");
								break;
							case 2:
								if (tempJson.has("TEMP2"))
									val = tempJson.getInt("TEMP2");
								break;
							case 3:
								if (tempJson.has("TEMP3"))
									val = tempJson.getInt("TEMP3");
								break;
							case 4:
								if (tempJson.has("TEMP4"))
									val = tempJson.getInt("TEMP4");
								break;
							default:
								break;
							}
						}
					} catch (Exception e) {
						return null;
					}

					if (val == -1 || jsonErrorcode.has(String.valueOf(val))) {
						return null;
					}

					if (val <= initLow) {
						initLow = val;
						veLow = ve;
					}
				}
				initLow = initLow / 10;
				// For High
				for (int i = 0; i < vehicleEvents.size(); i++) {
					Vehicleevent ve = (Vehicleevent) vehicleEvents.get(i);
					int val = -1;
					try {
						String tags = ve0.getTags();
						if (tags != null && tags.contains("TEMP")) {
							JSONObject tempJson = new JSONObject(tags);
							switch (id) {
							case 1:
								if (tempJson.has("TEMP1"))
									val = tempJson.getInt("TEMP1");
								break;
							case 2:
								if (tempJson.has("TEMP2"))
									val = tempJson.getInt("TEMP2");
								break;
							case 3:
								if (tempJson.has("TEMP3"))
									val = tempJson.getInt("TEMP3");
								break;
							case 4:
								if (tempJson.has("TEMP4"))
									val = tempJson.getInt("TEMP4");
								break;
							default:
								break;
							}
						}
					} catch (Exception e) {
						return null;
					}

					if (val == -1 || jsonErrorcode.has(String.valueOf(val))) {
						return null;
					}
					if (val >= initHigh) {
						initHigh = val;
						veHigh = ve;
					}
				}
				initHigh = initHigh / 10;
				if ((initLow == -1) || (initHigh == -1)) {
					return null;
				}

				// Low Alert
				if ((veLow != null) && (initLow < min)) {
					try {
						Date date = veLow.getId().getEventTimeStamp();
						String latlng = veLow.getLatitude() + ","
								+ veLow.getLongitude();
						String time = TimeZoneUtil.getStrTZDateTime(date);

						String description = "Alert%0DType : Temperature Sensor "
								+ id
								+ "- Low%0Dvehicle:"
								+ plateNo
								+ "%0DValue: "
								+ initLow
								+ " &deg; C%0DLimit : "
								+ min
								+ " C - "
								+ max
								+ " C %0DTime:" + time;
						Vehiclealerts va = new Vehiclealerts();
						va.setAlerttype(STR_TEMP + id);
						va.setDescription(description);
						va.setEventTimeStamp(date);
						va.setLatlng(latlng);
						va.setSmsmobile(mobile);
						va.setVin(vin);
						va.setShowstatus(false);
						vehiclealerts.add(va);
					} catch (Exception e) {
						return null;
					}
				}

				// High Alert
				if ((veHigh != null) && (initHigh > max)) {
					try {
						Date date = veHigh.getId().getEventTimeStamp();
						String latlng = veHigh.getLatitude() + ","
								+ veHigh.getLongitude();
						String time = TimeZoneUtil.getStrTZDateTime(date);

						String description = "Alert%0DType : Temperature Sensor "
								+ id
								+ "- High%0Dvehicle:"
								+ plateNo
								+ "%0DValue: "
								+ initHigh
								+ " C%0DLimit : "
								+ min + " C - " + max + " C %0DTime:" + time;
						Vehiclealerts va = new Vehiclealerts();
						va.setAlerttype(STR_TEMP + id);
						va.setDescription(description);
						va.setEventTimeStamp(date);
						va.setLatlng(latlng);
						va.setSmsmobile(mobile);
						va.setVin(vin);
						va.setShowstatus(false);
						vehiclealerts.add(va);
					} catch (Exception e) {
						return null;
					}
				}

				if (!vehiclealerts.isEmpty()) {
					lastUpdatedTime = alertsManager.persistVehicleAlert(
							alertConfig, vehiclealerts, lastUpdatedTime);
				}
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}

	@Override
	public void addAlertManager(AlertsManager alertsManager) {
		this.alertsManager = alertsManager;

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