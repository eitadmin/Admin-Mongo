package com.eiw.alerts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.eiw.device.ejb.VehicleComposite;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.fleettrackingpu.Alertconfig;
import com.eiw.server.fleettrackingpu.Companytrackdevice;
import com.eiw.server.fleettrackingpu.Heartbeatevent;
import com.eiw.server.fleettrackingpu.VehicleHasIo;
import com.eiw.server.fleettrackingpu.Vehiclealerts;
import com.eiw.server.fleettrackingpu.Vehicleevent;

public class CheckBTCoolerSensor implements CheckAlerts {

	AlertsManager alertsManager;
	private static final String STR_BTTEMP = "BTTEMPERATURESENSOR";
	private static final String STR_BTBATTERY = "BLEBATTERY";
	private static final String STR_BTHUMIDITY = "BLEHUMIDITY";
	Alertconfig alertConfig = null;
	Date lastUpdatedTime;

	public CheckBTCoolerSensor(AlertsManager alertsManager1) {
		alertsManager = alertsManager1;
	}

	public CheckBTCoolerSensor(Alertconfig alertConfig, Date lastUpdatedTime) {
		this.alertConfig = alertConfig;
		this.lastUpdatedTime = lastUpdatedTime;
	}

	@Override
	public String manageAlerts(List<Vehicleevent> vehicleEvents, String vin,
			String plateNo, List<VehicleHasIo> vehicleHasIo, int id,
			VehicleComposite vehicleComposite) {
		// TODO Auto-generated method stub
		String commonStr = "";
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
			boolean buzzerCheckForBT = true;

			/*
			 * if (alertConfig.getSubAlertType() != null &&
			 * alertConfig.getSubAlertType().equalsIgnoreCase(
			 * "TEMPERATURESENSOR_BUZZER") && ve0.getDi2() == 0) {
			 * buzzerCheckForBT = false; Vehicleevent prevVE =
			 * MeiTrackDeviceHandler.getPrevVE(vin); if (prevVE != null &&
			 * prevVE.getDi2() == 1)
			 * ruptelaDeviceMgmtRemote.sendCommand("12345",
			 * companytrackDevice.getImeiNo(), "buzzerOffwithLED"); }
			 */

			if (buzzerCheckForBT) {

				try {
					JSONObject tempJson;
					String tags = ve0.getTags();
					if (tags != null && tags.contains("BLEtemp")) {
						commonStr = tags;
						tempJson = new JSONObject(tags);
						switch (id) {
						case 1:
							if (tempJson.has("BLEtemp1")) {
								initLow = tempJson.getInt("BLEtemp1");
								initHigh = tempJson.getInt("BLEtemp1");
							}
							break;
						case 2:
							if (tempJson.has("BLEtemp2")) {
								initLow = tempJson.getInt("BLEtemp2");
								initHigh = tempJson.getInt("BLEtemp2");
							}
							break;
						case 3:
							if (tempJson.has("BLEtemp3")) {
								initLow = tempJson.getInt("BLEtemp3");
								initHigh = tempJson.getInt("BLEtemp3");
							}
							break;
						case 4:
							if (tempJson.has("BLEtemp4")) {
								initLow = tempJson.getInt("BLEtemp4");
								initHigh = tempJson.getInt("BLEtemp4");
							}
							break;
						default:
							break;
						}
					} else if (tags != null && tags.contains("BLEBatVolt")) {
						commonStr = tags;
						tempJson = new JSONObject(tags);
						switch (id) {
						case 1:
							if (tempJson.has("BLEBatVolt1")) {
								initLow = tempJson.getInt("BLEBatVolt1");
								initHigh = tempJson.getInt("BLEBatVolt1");
							}
							break;
						case 2:
							if (tempJson.has("BLEBatVolt2")) {
								initLow = tempJson.getInt("BLEBatVolt2");
								initHigh = tempJson.getInt("BLEBatVolt2");
							}
							break;
						case 3:
							if (tempJson.has("BLEBatVolt3")) {
								initLow = tempJson.getInt("BLEBatVolt3");
								initHigh = tempJson.getInt("BLEBatVolt3");
							}
							break;
						case 4:
							if (tempJson.has("BLEBatVolt4")) {
								initLow = tempJson.getInt("BLEBatVolt4");
								initHigh = tempJson.getInt("BLEBatVolt4");
							}
							break;
						default:
							break;
						}

					} else {

						commonStr = tags;
						tempJson = new JSONObject(tags);
						switch (id) {
						case 1:
							if (tempJson.has("BLEHumidity1")) {
								initLow = tempJson.getInt("BLEHumidity1");
								initHigh = tempJson.getInt("BLEHumidity1");
							}
							break;
						case 2:
							if (tempJson.has("BLEHumidity2")) {
								initLow = tempJson.getInt("BLEHumidity2");
								initHigh = tempJson.getInt("BLEHumidity2");
							}
							break;
						case 3:
							if (tempJson.has("BLEHumidity3")) {
								initLow = tempJson.getInt("BLEHumidity3");
								initHigh = tempJson.getInt("BLEHumidity3");
							}
							break;
						case 4:
							if (tempJson.has("BLEHumidity4")) {
								initLow = tempJson.getInt("BLEHumidity4");
								initHigh = tempJson.getInt("BLEHumidity4");
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
						JSONObject tempJson;
						String tempSensors = ve.getTags();
						if (tempSensors != null
								&& tempSensors.contains("BLEtemp")) {
							commonStr = tempSensors;
							tempJson = new JSONObject(tempSensors);
							switch (id) {
							case 1:
								if (tempJson.has("BLEtemp1"))
									val = tempJson.getInt("BLEtemp1");
								break;
							case 2:
								if (tempJson.has("BLEtemp2"))
									val = tempJson.getInt("BLEtemp2");
								break;
							case 3:
								if (tempJson.has("BLEtemp3"))
									val = tempJson.getInt("BLEtemp3");
								break;
							case 4:
								if (tempJson.has("BLEtemp4"))
									val = tempJson.getInt("BLEtemp4");
								break;
							default:
								break;
							}
						} else if (tempSensors != null
								&& tempSensors.contains("BLEBatVolt")) {
							commonStr = tempSensors;
							tempJson = new JSONObject(tempSensors);
							switch (id) {
							case 1:
								if (tempJson.has("BLEBatVolt1"))
									val = tempJson.getInt("BLEBatVolt1");
								break;
							case 2:
								if (tempJson.has("BLEBatVolt2"))
									val = tempJson.getInt("BLEBatVolt2");
								break;
							case 3:
								if (tempJson.has("BLEBatVolt3"))
									val = tempJson.getInt("BLEBatVolt3");
								break;
							case 4:
								if (tempJson.has("BLEBatVolt4"))
									val = tempJson.getInt("BLEBatVolt4");
								break;
							default:
								break;
							}
						} else {

							commonStr = tempSensors;
							tempJson = new JSONObject(tempSensors);
							switch (id) {
							case 1:
								if (tempJson.has("BLEHumidity1"))
									val = tempJson.getInt("BLEHumidity1");
								break;
							case 2:
								if (tempJson.has("BLEHumidity2"))
									val = tempJson.getInt("BLEHumidity2");
								break;
							case 3:
								if (tempJson.has("BLEHumidity3"))
									val = tempJson.getInt("BLEHumidity3");
								break;
							case 4:
								if (tempJson.has("BLEHumidity4"))
									val = tempJson.getInt("BLEHumidity4");
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
						JSONObject tempJson;
						String tags = ve0.getTags();
						if (tags != null && tags.contains("BLEtemp")) {
							commonStr = tags;
							tempJson = new JSONObject(tags);
							switch (id) {
							case 1:
								if (tempJson.has("BLEtemp1"))
									val = tempJson.getInt("BLEtemp1");
								break;
							case 2:
								if (tempJson.has("BLEtemp2"))
									val = tempJson.getInt("BLEtemp2");
								break;
							case 3:
								if (tempJson.has("BLEtemp3"))
									val = tempJson.getInt("BLEtemp3");
								break;
							case 4:
								if (tempJson.has("BLEtemp4"))
									val = tempJson.getInt("BLEtemp4");
								break;
							default:
								break;
							}
						} else if (tags != null && tags.contains("BLEBatVolt")) {
							commonStr = tags;
							tempJson = new JSONObject(tags);
							switch (id) {
							case 1:
								if (tempJson.has("BLEBatVolt1"))
									val = tempJson.getInt("BLEBatVolt1");
								break;
							case 2:
								if (tempJson.has("BLEBatVolt2"))
									val = tempJson.getInt("BLEBatVolt2");
								break;
							case 3:
								if (tempJson.has("BLEBatVolt3"))
									val = tempJson.getInt("BLEBatVolt3");
								break;
							case 4:
								if (tempJson.has("BLEBatVolt4"))
									val = tempJson.getInt("BLEBatVolt4");
								break;
							default:
								break;
							}
						} else {

							commonStr = tags;
							tempJson = new JSONObject(tags);
							switch (id) {
							case 1:
								if (tempJson.has("BLEHumidity1"))
									val = tempJson.getInt("BLEHumidity1");
								break;
							case 2:
								if (tempJson.has("BLEHumidity2"))
									val = tempJson.getInt("BLEHumidity2");
								break;
							case 3:
								if (tempJson.has("BLEHumidity3"))
									val = tempJson.getInt("BLEHumidity3");
								break;
							case 4:
								if (tempJson.has("BLEHumidity4"))
									val = tempJson.getInt("BLEHumidity4");
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
						String description = "";
						Date date = veLow.getId().getEventTimeStamp();
						String latlng = veLow.getLatitude() + ","
								+ veLow.getLongitude();
						String time = TimeZoneUtil.getStrTZDateTime(date);
						Vehiclealerts va = new Vehiclealerts();
						if (commonStr.equalsIgnoreCase("BLEtemp")) {
							description = "Alert%0DType : BlueTooth Temperature Sensor "
									+ id
									+ "- Low%0Dvehicle:"
									+ plateNo
									+ "%0DValue: "
									+ initLow
									+ " &deg; C%0DLimit : "
									+ min
									+ " C - "
									+ max + " C %0DTime:" + time;
							va.setAlerttype(STR_BTTEMP + id);
						} else if (commonStr.equalsIgnoreCase("BLEBatVolt")) {
							description = "Alert%0DType : BlueTooth Battery Voltage "
									+ id
									+ "- Low%0Dvehicle:"
									+ plateNo
									+ "%0DValue: "
									+ initLow
									+ " &deg; % %0DLimit : "
									+ min
									+ " % - "
									+ max + " % %0DTime:" + time;
							va.setAlerttype(STR_BTBATTERY + id);
						} else {
							description = "Alert%0DType : BlueTooth Humidity "
									+ id + "- Low%0Dvehicle:" + plateNo
									+ "%0DValue: " + initLow
									+ " &deg; F%0DLimit : " + min + " F - "
									+ max + " F %0DTime:" + time;
							va.setAlerttype(STR_BTHUMIDITY + id);
						}
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
						String description = "";
						Date date = veHigh.getId().getEventTimeStamp();
						String latlng = veHigh.getLatitude() + ","
								+ veHigh.getLongitude();
						String time = TimeZoneUtil.getStrTZDateTime(date);

						Vehiclealerts va = new Vehiclealerts();
						if (commonStr.equalsIgnoreCase("BLEtemp")) {
							description = "Alert%0DType : BlueTooth Temperature Sensor "
									+ id
									+ "- High%0Dvehicle:"
									+ plateNo
									+ "%0DValue: "
									+ initHigh
									+ " C%0DLimit : "
									+ min
									+ " C - "
									+ max
									+ " C %0DTime:"
									+ time;
							va.setAlerttype(STR_BTTEMP + id);
						} else if (commonStr.equalsIgnoreCase("BLEBatVolt")) {
							description = "Alert%0DType : BlueTooth  Battery Voltage"
									+ id
									+ "- High%0Dvehicle:"
									+ plateNo
									+ "%0DValue: "
									+ initHigh
									+ " % %0DLimit : "
									+ min
									+ " % - "
									+ max
									+ " % %0DTime:" + time;
							va.setAlerttype(STR_BTBATTERY + id);
						} else {
							description = "Alert%0DType : BlueTooth Humidity "
									+ id + "- High%0Dvehicle:" + plateNo
									+ "%0DValue: " + initHigh + " C%0DLimit : "
									+ min + " F - " + max + " F %0DTime:"
									+ time;
							va.setAlerttype(STR_BTHUMIDITY + id);
						}

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
		// TODO Auto-generated method stub
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
