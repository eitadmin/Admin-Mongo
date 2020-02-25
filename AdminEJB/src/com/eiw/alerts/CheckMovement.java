package com.eiw.alerts;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;
import org.json.JSONObject;

import com.eiw.client.dto.CompanyData;
import com.eiw.device.ejb.VehicleComposite;
import com.eiw.device.handler.MeiTrackDeviceHandler;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.fleettrackingpu.Alertconfig;
import com.eiw.server.fleettrackingpu.Companytrackdevice;
import com.eiw.server.fleettrackingpu.Heartbeatevent;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.VehicleHasIo;
import com.eiw.server.fleettrackingpu.Vehiclealerts;
import com.eiw.server.fleettrackingpu.Vehicleevent;

public class CheckMovement implements CheckAlerts {

	AlertsEJBRemote alertsEJBRemote = null;
	AlertsManager alertsManager;
	Alertconfig alertConfig = null;
	Date lastUpdatedTime;
	Vehicle vehicle;
	String assetType;
	public static Map<String, List<Vehicleevent>> vehicleeventMap = new HashMap<String, List<Vehicleevent>>();
	SimpleDateFormat sdfTime = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
	SimpleDateFormat sdfTime2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final Logger LOGGER = Logger.getLogger("alerts");

	public CheckMovement(AlertsManager alertsManager1) {
		alertsManager = alertsManager1;
		alertsEJBRemote = alertsManager.alertsEJBRemote;
	}

	public CheckMovement(Alertconfig alertConfig, Date lastUpdatedTime) {
		this.alertConfig = alertConfig;
		this.lastUpdatedTime = lastUpdatedTime;
	}

	@Override
	public String manageAlerts(List<Vehicleevent> vehicleEvents, String vin,
			String plateNo, List<VehicleHasIo> vehicleHasIo,
			int antiTheftCount, VehicleComposite vehicleComposite) {
		LOGGER.info("Inside CheckMovementCheck vin= " + vin);
		Companytrackdevice companytrackDevice = vehicleComposite
				.getCompanytrackDevice();
		if (companytrackDevice.getCompanytrackdevicemodels().getId()
				.getManufacturerName().equalsIgnoreCase("concox")) {
			Vehicle vehicle = alertsEJBRemote.getVehicle(vin);
			String description = null;
			CompanyData compSetttings = alertsEJBRemote
					.getCompanySettings(vehicle.getCompanyId());
			List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();
			LOGGER.info("Inside CheckMovementCheck Alert -> vin= " + vin);
			try {
				for (Vehicleevent ve : vehicleEvents) {
					if (!ve.getEngine() && ve.getSpeed() > 0
							&& ve.getTags().equalsIgnoreCase("Alarm=Shock")) {
						List<Vehicleevent> VehicleeventList = new ArrayList<Vehicleevent>();
						VehicleeventList.add(ve);
						if (vehicleeventMap.get(ve.getId().getVin()) != null) {
							List<Vehicleevent> vehicleeventList = vehicleeventMap
									.get(ve.getId().getVin());
							Collections.sort(vehicleeventList,
									new Comparator<Vehicleevent>() {
										public int compare(Vehicleevent o1,
												Vehicleevent o2) {
											if (o1.getId().getEventTimeStamp() == o2
													.getId()
													.getEventTimeStamp())
												return 0;
											return o1
													.getId()
													.getEventTimeStamp()
													.before(o2
															.getId()
															.getEventTimeStamp()) ? -1
													: 1;
										}
									});
							long diff = ve.getId().getEventTimeStamp()
									.getTime()
									- vehicleeventList
											.get(vehicleeventList.size() - 1)
											.getId().getEventTimeStamp()
											.getTime();
							long diffMinutes = diff / (60 * 1000);
							if (diffMinutes <= 2) {
								VehicleeventList.addAll(vehicleeventMap.get(ve
										.getId().getVin()));
								vehicleeventMap.put(ve.getId().getVin(),
										VehicleeventList);
								if (vehicleeventMap.get(ve.getId().getVin())
										.size() >= compSetttings
										.getMovementCount()) {
									boolean curMeter = checkingForAntiTheft(
											vehicleeventMap.get(ve.getId()
													.getVin()), 0,
											compSetttings.getMovementMeter());
									if (curMeter) {
										Date eventTimeStamp = ve.getId()
												.getEventTimeStamp();
										String lastTImeStamp = TimeZoneUtil
												.getStrTZDateTime(ve.getId()
														.getEventTimeStamp());

										description = "Alert%0DType : Movement Detected!%0DPlate No:"
												+ plateNo
												+ "%0DTime:"
												+ lastTImeStamp
												+ "%0Dvin:"
												+ vin;
										String mobile2 = null;
										mobile2 = alertConfig.getSmsNumber();
										Vehiclealerts va = new Vehiclealerts();
										va.setAlerttype("Movement");
										va.setDescription(description);
										va.setEventTimeStamp(eventTimeStamp);
										va.setLatlng(ve.getLatitude() + ","
												+ ve.getLongitude());
										va.setSmsmobile(mobile2);
										va.setVin(vin);
										va.setShowstatus(false);
										vehiclealerts.add(va);
									}
									vehicleeventMap.remove(ve.getId().getVin());
								}
							} else {
								vehicleeventMap.remove(ve.getId().getVin());
							}
						} else {
							vehicleeventMap.put(ve.getId().getVin(),
									VehicleeventList);
						}
					} else {
						if (vehicleeventMap.get(ve.getId().getVin()) != null) {
							vehicleeventMap.remove(ve.getId().getVin());
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (!vehiclealerts.isEmpty()) {
				lastUpdatedTime = alertsManager.persistVehicleAlert(
						alertConfig, vehiclealerts, lastUpdatedTime);
			}

			return null;
		} else {
			return checkAntitheft(vehicleEvents, vin, plateNo, vehicleHasIo,
					antiTheftCount, vehicleComposite);
		}
	}

	private String checkAntitheft(List<Vehicleevent> vehicleEvents, String vin,
			String plateNo, List<VehicleHasIo> vehicleHasIo,
			int antiTheftCount, VehicleComposite vehicleComposite) {

		vehicle = vehicleComposite.getVehicle();
		CompanyData compSetttings = alertsEJBRemote.getCompanySettings(vehicle
				.getCompanyId());

		String mobile2 = alertConfig.getSmsNumber();
		String latlng = null;
		String lastTImeStamp = null;
		Vehicleevent vehiclePreData = MeiTrackDeviceHandler.getPrevVE(vin);
		BigDecimal numberRoundLat = new BigDecimal(vehiclePreData.getLatitude());
		numberRoundLat = numberRoundLat.setScale(4, BigDecimal.ROUND_HALF_UP);

		BigDecimal numberRoundLang = new BigDecimal(
				vehiclePreData.getLongitude());
		numberRoundLang = numberRoundLang.setScale(4, BigDecimal.ROUND_HALF_UP);

		List<Vehicleevent> meterCalList = new ArrayList<Vehicleevent>();
		Vehicleevent meterCal;
		int count = 0;
		assetType = vehicleComposite.getVehicle().getVehiclemodel()
				.getRemarks();

		for (int i = 0; i < vehicleEvents.size(); i++) {
			boolean engine = vehicleEvents.get(i).getEngine();
			int speed = vehicleEvents.get(i).getSpeed();

			LOGGER.info("  INFO  antitheft count before " + count);

			if ((numberRoundLat.doubleValue() == vehicleEvents.get(i)
					.getLatitude())
					&& (numberRoundLang.doubleValue() == vehicleEvents.get(i)
							.getLongitude())) {
				LOGGER.info("  INFO  lat and lang comparing"
						+ numberRoundLat.doubleValue()
						+ vehicleEvents.get(i).getLatitude()
						+ numberRoundLang.doubleValue()
						+ vehicleEvents.get(i).getLongitude());

				break;
			} else {
				if ((assetType != null && assetType
						.equalsIgnoreCase("IMMOVABLE")) ? (speed > 0)
						: ((!engine) && (speed > 0))) {

					LOGGER.info("  INFO  antitheft (!engine) && (speed > 0) satisfing "
							+ speed + engine);

					meterCal = new Vehicleevent();
					meterCal.setLatitude(vehicleEvents.get(i).getLatitude());
					meterCal.setLongitude(vehicleEvents.get(i).getLongitude());

					meterCalList.add(meterCal);
					latlng = vehicleEvents.get(i).getLatitude() + ","
							+ vehicleEvents.get(i).getLongitude();
					lastTImeStamp = TimeZoneUtil.getStrTZDateTime(vehicleEvents
							.get(i).getId().getEventTimeStamp());
					count++;
					LOGGER.info("  INFO  antitheft count after " + count);
				}

			}
		}
		if (meterCalList != null && meterCalList.size() >= antiTheftCount) {
			LOGGER.info(" before Entering checkFinalAntiTheftStatus "
					+ meterCalList.size());
			checkFinalAntiTheftStatus(count, meterCalList, antiTheftCount, vin,
					plateNo, mobile2, latlng, lastTImeStamp,
					compSetttings.getAntiTheftMeter());
		}
		return null;
	}

	public void checkFinalAntiTheftStatus(int count,
			List<Vehicleevent> meterCalList, int antiTheftCount, String vin,
			String plateNo, String mobile2, String latlng,
			String lastTImeStamp, int antiTheftMeter) {
		List<Vehiclealerts> vehiclealerts = new ArrayList<Vehiclealerts>();
		try {
			if (count >= antiTheftCount) {

				LOGGER.info("   INFO  antitheft count AND AntitheftCount satisfing "
						+ count + antiTheftCount);
				boolean checkTheftStatus = checkingForAntiTheft(meterCalList,
						antiTheftCount, antiTheftMeter);

				if (checkTheftStatus) {
					// Date eventTimeStamp = lastTImeStamp;
					String description = "Alert%0DType : Movement Detected!%0DPlate No:"
							+ plateNo
							+ "%0DTime:"
							+ lastTImeStamp
							+ "%0Dvin:"
							+ vin;
					String mobile = null;
					mobile = alertConfig.getSmsNumber();
					Vehiclealerts va = new Vehiclealerts();
					va.setAlerttype("Movement");
					va.setDescription(description);
					String dateTime = TimeZoneUtil.getTimeINYYYYMMddss(sdfTime
							.parse(lastTImeStamp));
					va.setEventTimeStamp(sdfTime2.parse(dateTime));
					va.setLatlng(latlng);
					va.setSmsmobile(mobile);
					va.setVin(vin);
					va.setShowstatus(false);
					vehiclealerts.add(va);
					if (!vehiclealerts.isEmpty()) {
						LOGGER.error("  INFO   Data Persisting from Antitheft for alertconfig "
								+ vehiclealerts.isEmpty());
						lastUpdatedTime = alertsManager.persistVehicleAlert(
								alertConfig, vehiclealerts, lastUpdatedTime);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("persistance exception = " + e);
		}

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

	private boolean checkingForAntiTheft(List<Vehicleevent> vehicleEvents,
			int antiTheftCount, int antiTheftMeter) {

		double kilometer = 0;
		double lat1 = 0;
		double lang1 = 0;
		double lat2 = 0;
		double lang2 = 0;
		for (int i = 0; i < vehicleEvents.size(); i++) {
			if (i == 0) {
				lat1 = vehicleEvents.get(i).getLatitude();
				lang1 = vehicleEvents.get(i).getLongitude();
			} else {
				lat2 = vehicleEvents.get(i).getLatitude();
				lang2 = vehicleEvents.get(i).getLongitude();
				kilometer += distanceMatrix(lat1, lang1, lat2, lang2);
				lat1 = lat2;
				lang1 = lang2;
			}

		}
		LOGGER.info("  INFO  antitheft Meters  checking kilometers  and antitheftMeter  "
				+ kilometer + " and" + antiTheftMeter);
		int meter = 0;
		if (antiTheftCount == 3)
			meter = antiTheftMeter;
		else if (antiTheftCount == 5)
			meter = antiTheftMeter;
		else if (antiTheftCount == 0)
			meter = antiTheftMeter;
		return kilometer >= meter ? true : false;
	}

	public Double distanceMatrix(double lat1, double lng1, double lat2,
			double lng2) {
		try {
			URL url1 = new URL(
					"https://maps.googleapis.com/maps/api/distancematrix/json?origins="
							+ lat1
							+ ","
							+ lng1
							+ "&destinations="
							+ lat2
							+ ","
							+ lng2
							+ "&mode=driving&language=en-EN&sensor=false&key=AIzaSyAb94T4OXJmVss7ArXmkKHb11PQ0fw6lyA");
			URLConnection con = url1.openConnection();
			InputStream in = con.getInputStream();
			String encoding = "UTF-8";
			String statusText = IOUtils.toString(in, encoding);
			if (statusText == null || !statusText.contains("distance")) {
				return distance(lat1, lng1, lat2, lng2);
			}
			JSONObject json = new JSONObject(statusText);
			String rows = json.getString("rows");
			JSONObject rowObject = new JSONObject(rows.substring(1,
					rows.length() - 1));
			String elements = rowObject.getString("elements");
			JSONObject elementsObject = new JSONObject(elements.substring(1,
					elements.length() - 1));
			String distance = elementsObject.getString("distance");
			JSONObject distanceObject = new JSONObject(distance);
			// return Float.valueOf(distanceObject.getString("value"));
			double finalValue = Double.valueOf(distanceObject
					.getString("value"));
			if (finalValue >= 3000) {
				return distance(lat1, lng1, lat2, lng2);
			} else {
				return finalValue;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return distance(lat1, lng1, lat2, lng2);
	}

	public Double distance(double lat1, double lng1, double lat2, double lng2) {
		double earthRadius = 6371000; // meters
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		return dist;
	}

	@Override
	public String manageHbAlerts(Heartbeatevent heartbeatevent, String vin,
			String PlateNo, VehicleComposite vehicleComposite) {
		// TODO Auto-generated method stub
		return null;
	}
}
