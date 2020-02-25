package com.skt.alerts;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.eiw.alerts.AlertsEJB;
import com.eiw.alerts.AlertsEJBRemote;
import com.eiw.client.dto.VehicleData;
import com.eiw.device.ejb.VehicleComposite;
import com.eiw.device.handler.MeiTrackDeviceHandler;
import com.eiw.server.companyadminpu.Applicationsettings;
import com.eiw.server.companyadminpu.Companybranch;
import com.eiw.server.companyadminpu.CompanybranchId;
import com.eiw.server.companyadminpu.Provider;
import com.eiw.server.companyadminpu.Pushnotificationdevices;
import com.eiw.server.companyadminpu.User;
import com.eiw.server.companyadminpu.UserId;
import com.eiw.server.fleettrackingpu.Freeformgeo;
import com.eiw.server.fleettrackingpu.Route;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.VehicleHasCompanytrackdevice;
import com.eiw.server.studenttrackingpu.Alertevents;
import com.eiw.server.studenttrackingpu.Alerttypes;
import com.eiw.server.studenttrackingpu.Demoalerts;
import com.eiw.server.studenttrackingpu.Gatehasdevice;
import com.eiw.server.studenttrackingpu.Routetrip;
import com.eiw.server.studenttrackingpu.Schoolroute;
import com.eiw.server.studenttrackingpu.StudentGeozone;
import com.eiw.server.studenttrackingpu.Studentalertsubscription;
import com.eiw.server.studenttrackingpu.Studentdetails;
import com.eiw.server.studenttrackingpu.Studentevent;
import com.eiw.server.studenttrackingpu.Tripstops;
import com.eiw.server.studenttrackingpu.Vehicletimingreport;
import com.eiw.server.studenttrackingpu.VehicletimingreportId;
import com.eiw.server.studenttrackingpu.Vehicletimings;
import com.skt.client.dto.RouteAndVehicleData;
import com.skt.client.dto.SktInitData;
import com.skt.client.dto.StudentData;
import com.skt.client.dto.VehicleTripTimeDto;
import com.skt.client.dto.VehicltimingReport;

@Stateless
public class SKTAlertsEJB extends AlertsEJB implements SKTAlertsEJBremote {

	@PersistenceContext(unitName = "studenttrackingpu")
	protected EntityManager emStudent;

	List<Alertevents> alerteventsList = new ArrayList<Alertevents>();
	@EJB
	public AlertsEJBRemote alertsEJBRemote;
	SKTAlertsManager sktAlertsManager = new SKTAlertsManager();

	public void manageStudentAbsence() {
		// SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
		//
		// Query query = emStudentdetails
		// .createQuery("SELECT stud FROM  Studentdetails stud ,Studentalertsubscription stsub "
		// + "WHERE DATE(stud.lastUpdDt) !='"
		// + sdfDate.format(new Date())
		// +
		// "' AND stud.stin = stsub.id.stin AND stsub.id.alertsubscribed='SA'");
		// List<Studentdetails> studentlist = query.getResultList();
		// Map<String, StudentData> stdmap = new HashMap<>();
		// for (Studentdetails stDetails : studentlist) {
		// StudentData studentData = setStudentdata(stDetails);
		//
		// Alertevents altevents = new Alertevents();
		// altevents.setStudentdetails(stDetails);
		// altevents.setEventTimeStamp(new Date());
		// altevents.setDescription("Student abscent");
		// // altevents.setVin(studentDetails.getVin());
		// Alerttypes alerttypes = new Alerttypes("SA");
		// altevents.setAlerttypes(alerttypes);
		// altevents.setLatlong(stDetails.getLastUpdLatlng());
		//
		// alerteventsList.add(altevents);
		// stdmap.put(stDetails.getTagdetails().getTagId(), studentData);
		// }
		// sktAlertsManager.persistStudentsAlert(alerteventsList, stdmap, null,
		// "", null);
	}

	public void studentStatusUpdate() {
		try {
			// String sql = "UPDATE studentdetails set status = '0'";
			// Query query = emStudent.createNativeQuery(sql);
			// query.executeUpdate();
		} catch (Exception e) {
			LOGGER.error("SKTAlertsEJB::studentStatusUpdate::Error occured = "
					+ e);
		}
	}

	// Method is for setting the student details to StudentDTO //
	@Override
	public StudentData setStudentdata(Studentdetails studentDetails) {
		StudentData studentData = new StudentData();
		try {

			UserId parentId = new UserId();
			parentId.setEmailAddress(studentDetails.getParentId());
			parentId.setCompanyCompanyId(studentDetails.getClassdetails()
					.getId().getSchoolId());
			User parent = em.find(User.class, parentId);
			studentData.setStin(studentDetails.getStin());
			studentData.setRollNo(studentDetails.getRollNo());
			studentData.setFirstName(studentDetails.getFirstName());
			studentData.setLastName(studentDetails.getLastName());
			studentData.setGender(studentDetails.getSex());
			studentData.setDateOfBirth(studentDetails.getDateOfBirth());
			studentData.setAddress(studentDetails.getAddress());
			studentData.setCity(studentDetails.getCity());
			studentData.setPin(studentDetails.getPin());
			studentData.setState(studentDetails.getState());
			studentData.setSchoolId(studentDetails.getClassdetails().getId()
					.getSchoolId());
			studentData.setBrannchId(studentDetails.getClassdetails().getId()
					.getBranchId());
			studentData.setClassId(studentDetails.getClassdetails().getId()
					.getClassId());
			studentData.setSectionId(studentDetails.getClassdetails().getId()
					.getSectionId());
			if (studentDetails.getTagdetails() != null)
				studentData.setTagId(studentDetails.getTagdetails().getTagId());
			// studentData.setVin(studentDetails.getVin());
			studentData.setAlertMode(studentDetails.getAlertMode());
			studentData.setStatus(studentDetails.getStatus());
			if (studentDetails.getTripstopsByPickupid() != null)
				studentData.setPickuptopId(studentDetails
						.getTripstopsByPickupid().getStopPointId());
			if (studentDetails.getTripstopsByDropid() != null)
				studentData.setDropstopId(studentDetails.getTripstopsByDropid()
						.getStopPointId());
			studentData.setEventTime(studentDetails.getLatestEventTime());
			studentData.setLatlng(studentDetails.getLastUpdLatlng());
			studentData.setLastUpdBy(studentDetails.getLastUpdBy());
			studentData.setLastUpdDt(studentDetails.getLastUpdDt());
			if (parent != null) {
				studentData.setParentName(parent.getFirstName());
				studentData.setContactNo(parent.getContactNo());
				studentData.setEmailAddress(parent.getFax());
				studentData.setUserId(parent.getUserId());
				studentData.setParentId(studentDetails.getParentId());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return studentData;
	}

	// Method end //

	public List<Route> getRoute(int id) {
		List<Route> routeforId = new ArrayList<Route>();
		try {
			Query query = em
					.createQuery("SELECT f FROM Route f WHERE f.id.id = :id");
			query.setParameter("id", id);
			routeforId = query.getResultList();
		} catch (Exception e) {
			LOGGER.error("SKTLog: SKTAlertsEJB::getRoute::Error occured in route="
					+ e.getMessage());
		}
		return routeforId;
	}

	@Override
	public void persistvehicletimingreport(VehicltimingReport vehicltimingReport) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");

		Query query = emStudent
				.createQuery("SELECT vt FROM  Vehicletimingreport vt "
						+ "WHERE DATE(vt.id.lastupdatedDate) ='"
						+ simpleDateFormat.format(new Date())
						+ "' AND vt.id.vin = '" + vehicltimingReport.getVin()
						+ "'");
		java.util.Date date = new java.util.Date();
		String lastupdateddate = simpleDateFormat.format(date);

		List<Object> objects = (List<Object>) query.getResultList();
		if (!objects.isEmpty() && objects.size() != 0) {
			try {
				VehicletimingreportId vehicletimingreportId = new VehicletimingreportId();
				vehicletimingreportId.setVin(vehicltimingReport.getVin());
				vehicletimingreportId.setLastupdatedDate(simpleDateFormat
						.parse(lastupdateddate));
				Vehicletimingreport vehicletimingreport = new Vehicletimingreport();
				vehicletimingreport.setId(vehicletimingreportId);
				if (!vehicltimingReport.getPickupstarttime().equalsIgnoreCase(
						"0")) {
					java.util.Date pickupstart = dateFormat
							.parse(vehicltimingReport.getPickupstarttime());
					vehicletimingreport.setPickupstarttime(pickupstart);
				}
				if (!vehicltimingReport.getPickupendtime()
						.equalsIgnoreCase("0")) {
					java.util.Date pickupend = dateFormat
							.parse(vehicltimingReport.getPickupendtime());
					vehicletimingreport.setPickupreachtime(pickupend);

				}
				if (!vehicltimingReport.getDropstarttime()
						.equalsIgnoreCase("0")) {
					java.util.Date dropstart = dateFormat
							.parse(vehicltimingReport.getDropstarttime());
					vehicletimingreport.setDropstarttime(dropstart);
				}
				if (!vehicltimingReport.getDropendtime().equalsIgnoreCase("0")) {
					java.util.Date dropend = dateFormat
							.parse(vehicltimingReport.getDropendtime());
					vehicletimingreport.setDropreachtime(dropend);
				}
				emStudent.persist(vehicletimingreport);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Vehicletimingreport singlevehicletimingreport = emStudent.find(
					Vehicletimingreport.class, vehicltimingReport.getVin());
			try {
				if (!vehicltimingReport.getPickupstarttime().equalsIgnoreCase(
						"0")) {
					java.util.Date pickupstart = dateFormat
							.parse(vehicltimingReport.getPickupstarttime());
					singlevehicletimingreport.setPickupstarttime(pickupstart);
				}
				if (!vehicltimingReport.getPickupendtime()
						.equalsIgnoreCase("0")) {
					java.util.Date pickupend = dateFormat
							.parse(vehicltimingReport.getPickupendtime());
					singlevehicletimingreport.setPickupreachtime(pickupend);

				}
				if (!vehicltimingReport.getDropstarttime()
						.equalsIgnoreCase("0")) {
					java.util.Date dropstart = dateFormat
							.parse(vehicltimingReport.getDropstarttime());
					singlevehicletimingreport.setDropstarttime(dropstart);
				}
				if (!vehicltimingReport.getDropendtime().equalsIgnoreCase("0")) {
					java.util.Date dropend = dateFormat
							.parse(vehicltimingReport.getDropendtime());
					singlevehicletimingreport.setDropreachtime(dropend);
				}
				emStudent.persist(singlevehicletimingreport);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Map<String, StudentData> getStudentDetailsMap(String vin,
			String compId, String branchId) {
		Map<String, StudentData> strudentDetailsMap = new HashMap<String, StudentData>();
		List<Routetrip> routetrips = new ArrayList<Routetrip>();
		String triptype;
		try {
			Query query = emStudent
					.createQuery("SELECT vh FROM  Schoolroute vh WHERE vh.vin = '"
							+ vin
							+ "' AND  compId= '"
							+ compId
							+ "' AND branchId = '" + branchId + "'");
			List<Schoolroute> vehicleHasRoutes = query.getResultList();
			if (vehicleHasRoutes.size() != 0 && !vehicleHasRoutes.isEmpty()) {
				for (Schoolroute singleroute : vehicleHasRoutes) {
					try {
						Set<Routetrip> listoftrips = singleroute
								.getRoutetrips();
						routetrips.addAll(listoftrips);
						if (routetrips.size() != 0) {
							for (Routetrip singlevehicleTrip : routetrips) {
								triptype = singlevehicleTrip.getType();
								Set<Tripstops> tripstops = singlevehicleTrip
										.getTripstopses();
								List<Tripstops> tripstopslist = new ArrayList<Tripstops>();
								tripstopslist.addAll(tripstops);
								if (tripstopslist.size() != 0) {
									for (Tripstops singlebusstops : tripstops) {
										if (triptype.equalsIgnoreCase("pickup")) {
											Set<Studentdetails> listofStudentDetails = (Set<Studentdetails>) getStudentDetailsByStop(singlebusstops
													.getStopPointId());
											if (listofStudentDetails.size() != 0) {
												for (Studentdetails singleStudentdata : listofStudentDetails) {
													StudentData studentData = setStudentdata(singleStudentdata);
													strudentDetailsMap.put(
															singleStudentdata
																	.getStin(),
															studentData);
												}
											}
										} else if (triptype
												.equalsIgnoreCase("drop")) {
											Set<Studentdetails> listofStudentDetails = (Set<Studentdetails>) getStudentDetailsByStop(singlebusstops
													.getStopPointId());
											if (listofStudentDetails.size() != 0) {
												for (Studentdetails singleStudentdata : listofStudentDetails) {
													StudentData studentData = setStudentdata(singleStudentdata);
													strudentDetailsMap.put(
															singleStudentdata
																	.getStin(),
															studentData);
												}
											}
										}
									}
								}
							}

						}
					} catch (Exception e) {
						LOGGER.error("SKTLog: SKTAlertsEJB getStudentDetailsMap Exception = "
								+ e.getMessage());
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("SKTLog : SKTAlertsEJB : getStudentDetailsMap Exception e = "
					+ e.getMessage());
		}
		return strudentDetailsMap;
	}

	@Override
	public List<Integer> getLastCrossedBusStop(String vin, boolean isSchool,
			int tripId) {
		List<Integer> orderNos = new ArrayList<Integer>();
		try {
			String queryStringSchool = "";
			if (isSchool)
				queryStringSchool = " OR alerttype='BAS' OR alerttype='BLS'";
			Query queryToGetPreviousBusStop = em
					.createNativeQuery("SELECT smsmobile FROM `vehiclealerts` "
							+ "WHERE (alerttype='BAB' OR alerttype='BATA' OR alerttype='BLB'"
							+ queryStringSchool + ")"
							+ " AND DATE(eventTimeStamp) = CURDATE() AND vin='"
							+ vin + "' AND subalerttype = '" + tripId
							+ "' ORDER BY eventTimeStamp ASC");
			List<Object> vehiclealerts = (List<Object>) queryToGetPreviousBusStop
					.getResultList();

			// List<Integer> vehiclealerts = (List<Integer>)
			// queryToGetPreviousBusStop
			// .getResultList();
			if (!vehiclealerts.isEmpty() && vehiclealerts.size() != 0) {
				for (Object orderno : vehiclealerts) {
					orderNos.add(Integer.parseInt(orderno + ""));
				}
			}

		} catch (Exception e) {
			LOGGER.error("SKTLog : SKTAlertsEJB ::getLastCrossedBusStop::Error occured ="
					+ e);
		}
		return orderNos;

	}

	/*
	 * get School geozone id
	 */
	@Override
	public int getSchoolGeoZoneId(String companyId) {
		int geoId = -1;
		try {
			Query query = emStudent
					.createQuery("SELECT fg FROM  Freeformgeo fg WHERE fg.category = '"
							+ companyId + "' ");
			List<Freeformgeo> freeformgeos = query.getResultList();
			if (!freeformgeos.isEmpty() && freeformgeos.size() != 0) {
				geoId = freeformgeos.get(0).getId().getId();
			}
		} catch (Exception e) {
			LOGGER.error("SKTLog : SKTAlertsEJB : getSchoolGeoZoneId ="
					+ e.getMessage());
			e.printStackTrace();
		}
		return geoId;
	}

	@Override
	public List<Tripstops> getSKTTripStops(String vin, String compId,
			String branchId) {

		LOGGER.info("SKTLog : SKTAlertsEJB : Entered getSKTTripStops Method skt");

		List<Tripstops> tripstopList = new ArrayList<Tripstops>();
		List<Routetrip> routetrips = new ArrayList<Routetrip>();
		try {
			String sql = "SELECT vh FROM  Schoolroute vh WHERE vh.vin = '"
					+ vin + "' AND  compId= '" + compId + "' AND branchId = '"
					+ branchId + "'";
			LOGGER.debug("SKTLog : SKTAlertsEJB : getSKTTripStops Query===="
					+ sql);
			Query query = em.createQuery(sql);
			LOGGER.debug("SKTLog : SKTAlertsEJB : getSKTTripStops Query===="
					+ query.toString());
			List<Schoolroute> vehicleHasRoutes = query.getResultList();
			LOGGER.debug("SKTLog : SKTAlertsEJB : getSKTTripStops vehicleHasRoutes size===="
					+ vehicleHasRoutes.size());
			if (!vehicleHasRoutes.isEmpty() && vehicleHasRoutes.size() != 0) {
				for (Schoolroute singleroute : vehicleHasRoutes) {
					try {
						Set<Routetrip> listoftrips = singleroute
								.getRoutetrips();
						routetrips.addAll(listoftrips);
						LOGGER.info("SKTLog : SKTAlertsEJB : getSKTTripStops routetrips size===="
								+ routetrips.size());
						if (routetrips.size() != 0) {
							for (Routetrip singleVehicletrip : routetrips) {
								int enabledtripId = getEnabledTripId(
										singleVehicletrip, compId, branchId);
								if (enabledtripId != -1) {
									Set<Tripstops> tripstops = singleVehicletrip
											.getTripstopses();
									tripstopList.addAll(tripstops);
									return tripstopList;
								}
							}
						}
					} catch (Exception e) {
						LOGGER.error("SKTLog : SKTAlertsEJB : getSKTTripStops Exception = "
								+ e.getMessage());
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("SKTLog : SKTAlertsEJB : getSKTTripStops Exception= "
					+ e.getMessage());
		}
		return tripstopList;

	}

	private int getEnabledTripId(Routetrip singleVehicletrip, String compId,
			String branchId) {
		int tripId;
		Query queryfortrip = emStudent
				.createNativeQuery("SELECT tripid FROM routetrip WHERE TIME(NOW()) BETWEEN SUBTIME(tripStarttime,'00:15:00') AND ADDTIME(tripEndtime, '00:15:00') AND schoolId= '"
						+ compId
						+ "' AND branchId = '"
						+ branchId
						+ "' AND tripid = '"
						+ singleVehicletrip.getTripid()
						+ "'");
		List<BigInteger> objects = (List<BigInteger>) queryfortrip
				.getResultList();
		if (!objects.isEmpty() && objects.size() != 0) {
			BigInteger stopPointId = objects.get(0);
			LOGGER.info("SKTLog: SKTAlertsEJB::getEnabledTripId ===="
					+ stopPointId);
			long id = stopPointId.longValue();
			tripId = (int) id;
		} else {
			tripId = -1;
		}

		return tripId;
	}

	@Override
	public Provider getSimulatedLatLong(int id) {
		Provider pro = emAdmin.find(Provider.class, Integer.valueOf(9999));
		return pro;
	}

	/**
 * 
 */
	@Override
	public User getParentname(Alertevents alertevents) {
		UserId parentid = new UserId();
		parentid.setEmailAddress(alertevents.getStudentdetails().getParentId());
		parentid.setCompanyCompanyId(alertevents.getStudentdetails()
				.getClassdetails().getId().getSchoolId());
		return em.find(User.class, parentid);

	}

	@Override
	public User getParent(Studentdetails studentdetails) {
		UserId parentId = new UserId();
		parentId.setEmailAddress(studentdetails.getParentId());
		parentId.setCompanyCompanyId(studentdetails.getClassdetails().getId()
				.getSchoolId());
		return (User) em.find(User.class, parentId);
		// if (parent != null) {
		// return parent;
		// }

	}

	@Override
	public Map<String, String> getSKTalerttypesMap() {
		LOGGER.info("SKTLog : SKTAlertsEJB : getSKTalerttypesMap ");
		Map<String, String> alerttypesDescriptionMap = new HashMap<String, String>();
		try {
			Query query = emStudent
					.createQuery("SELECT at FROM  Alerttypes at");
			List<Alerttypes> alertTypesList = query.getResultList();
			if (!alertTypesList.isEmpty() && alertTypesList.size() != 0) {
				for (Alerttypes alerttypes : alertTypesList) {
					alerttypesDescriptionMap.put(alerttypes.getAlerttype(),
							alerttypes.getDescription());
				}
			}
		} catch (Exception e) {
			LOGGER.error("SKTLog : SKTAlertsEJB : getSKTalerttypesMap exception = "
					+ e.getMessage());
		}
		return alerttypesDescriptionMap;
	}

	@Override
	public Companybranch getbranchId(User parent) {
		CompanybranchId companybranchId = new CompanybranchId();
		companybranchId.setCompanyId(parent.getCompany().getCompanyId());
		Companybranch companybranch = em.find(Companybranch.class,
				companybranchId);
		return companybranch;
	}

	@Override
	public Alertevents getLastAlertEvent(String stin, String alertType) {
		LOGGER.debug("SKTLog: SKTAlertsEJB : getLastAlertEvent entered");
		try {
			Query query = emStudent
					.createQuery("SELECT ae FROM Alertevents ae WHERE ae.studentdetails.stin='"
							+ stin
							+ "' AND "
							+ alertType
							+ " DATE(eventTimeStamp)=DATE(NOW()) ORDER BY eventTimeStamp DESC");
			List<Alertevents> alerteventsList = query.getResultList();
			if (!alerteventsList.isEmpty() && alerteventsList.size() != 0) {
				return alerteventsList.get(0);
			} else
				return null;
		} catch (Exception e) {
			LOGGER.error("SKTLog: SKTAlertsEJB : getLastAlertEvent exception "
					+ e.getMessage());
			return null;
		}
	}

	@Override
	public List<Schoolroute> getRouteTrips(String vin, String companyId,
			String branchId) {
		LOGGER.info("SKTLog: SKTAlertsEJB : Entered getRouteTrips Method skt");
		List<Schoolroute> vehicleHasRoutes = null;
		try {
			String sql = "SELECT vh FROM  Schoolroute vh WHERE vh.vin = '"
					+ vin + "' AND  compId= '" + companyId
					+ "' AND branchId = '" + branchId + "'";
			LOGGER.debug("SKTLog: SKTAlertsEJB : getRouteTrips Query====" + sql);
			Query query = em.createQuery(sql);
			LOGGER.debug("SKTLog: SKTAlertsEJB : getRouteTrips Query===="
					+ query.toString());
			vehicleHasRoutes = query.getResultList();
			LOGGER.info("SKTLog: SKTAlertsEJB : getRouteTrips vehicleHasRoutes size===="
					+ vehicleHasRoutes.size());

		} catch (Exception e) {
			LOGGER.error("SKTLog: SKTAlertsEJB : getRouteTrips Exception "
					+ e.getMessage());
		}
		return vehicleHasRoutes;

	}

	@Override
	public boolean isAlertAlreadyGenerated(Alertevents alertEvent,
			Routetrip routetrip) {
		LOGGER.debug("SKTLog: SKTAlertsEJB : isAlertAlreadyGenerated entered");
		try {
			String stin = alertEvent.getStudentdetails().getStin(), alertType = alertEvent
					.getAlerttypes().getAlerttype();
			Query query = emStudent
					.createQuery("SELECT ae FROM Alertevents ae WHERE ae.studentdetails.stin='"
							+ stin
							+ "' AND ae.alerttypes.alerttype='"
							+ alertType
							+ "' AND DATE(eventTimeStamp)=DATE(NOW()) ORDER BY eventTimeStamp DESC");
			List<Alertevents> alerteventsList = query.getResultList();
			if (alerteventsList.isEmpty() || alerteventsList.size() == 0)
				return false;
			Date eventTimeStamp = alerteventsList.get(0).getEventTimeStamp();
			// long tripStartTime = routetrip.getTripStarttime().getTime(),
			// tripEndTime = routetrip
			// .getTripEndtime().getTime();

			// long MILLIS_PER_DAY = 24 * 60 * 60 * 1000;
			// long eventTime = eventTimeStamp.getTime() % MILLIS_PER_DAY;

			// if (tripStartTime < eventTime && eventTime < tripEndTime)
			// return true;
			// else
			// return false;

			Date tripStartTime = routetrip.getTripStarttime();
			Date tripEndTime = routetrip.getTripEndtime();

			boolean isCurrentTrip = isTimeInRange(tripStartTime, tripEndTime,
					eventTimeStamp);
			LOGGER.info("SKTLog:MeiTrackDeviceHandler prepareEventAndAlert time = "
					+ tripStartTime
					+ " and "
					+ tripEndTime
					+ " and  "
					+ eventTimeStamp.toString());
			if (isCurrentTrip)
				return true;
			else
				return false;

		} catch (Exception e) {
			LOGGER.error("SKTLog:SKTAlertsEJB: isAlertAlreadyGenerated Exception = "
					+ e.getMessage());
			return false;
		}

	}

	@Override
	public Provider getDevicesForPushNotificatoinDemo(String imei) {
		try {
			Query query = em
					.createQuery("SELECT p FROM Provider p WHERE p.smsApi = :imei");
			query.setParameter("imei", imei);
			List<Provider> providers = (List<Provider>) query.getResultList();
			if (!providers.isEmpty() && providers.size() != 0)
				return providers.get(0);
			else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public User getParent(String mobile, String fullName) {
		try {

			UserId parentid = new UserId();
			parentid.setEmailAddress(mobile);
			parentid.setCompanyCompanyId(fullName);
			User parent = em.find(User.class, parentid);
			return parent;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public Studentdetails getStudentDetails(String owner) {
		try {
			Studentdetails studentdetails = em
					.find(Studentdetails.class, owner);
			return studentdetails;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public void updateProvider(Provider provider) {
		// TODO Auto-generated method stub
		try {
			em.merge(provider);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public SktInitData getDBforSKT() {
		SktInitData sktInitData = new SktInitData();
		LOGGER.info("SKTLog : SKTAlertsEJB : Entered getDBforSKT Method");
		try {
			sktInitData.setAlerttypesDescriptionMap(getSKTalerttypesMap());
			sktInitData.setSchoolRoutes(getRouteAndVehicleData());
			sktInitData.setStudentGeoZoneMap(getStudentGeoZones());
			return sktInitData;
		} catch (Exception e) {
			LOGGER.error("SKTLog : SKTAlertsEJB : getDBforSKT Exception= "
					+ e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public Map<String, VehicleComposite> getVehiclesInfo() {
		LOGGER.info("SKTLog : SKTAlertsEJB : Entered into getVehiclesInfo");
		Map<String, VehicleComposite> vehicleCompositeMap = null;
		try {
			String sqlforUser = "SELECT v,vhctd FROM Vehicle v LEFT JOIN v.vehicleHasCompanytrackdevices vhctd WHERE v.vin=vhctd.id.vehicleVin";
			Query query1 = em.createQuery(sqlforUser);
			LOGGER.debug("SKTLog : SKTAlertsEJB : Before sqlforUser Execute Query "
					+ sqlforUser);
			List<Object[]> resultList = query1.getResultList();
			LOGGER.debug("SKTLog : SKTAlertsEJB : After sqlforUser Execute Query "
					+ resultList.size());
			vehicleCompositeMap = new HashMap<String, VehicleComposite>();
			for (int i = 0; i < resultList.size(); i++) {

				Object[] row = (Object[]) resultList.get(i);
				if (row[1] != null) {
					Vehicle vehicle = (Vehicle) row[0];
					VehicleComposite vehicleComposite = new VehicleComposite();
					vehicleComposite.setVehicle(vehicle);
					vehicleComposite
							.setTimeZoneRegion(getTimeZoneRegion(vehicle
									.getVin()));
					vehicleComposite.setDeviceModel(getDeviceModelName(vehicle
							.getVin()));
					VehicleHasCompanytrackdevice vhctd = (VehicleHasCompanytrackdevice) row[1];
					String imeiNo = String.valueOf(vhctd
							.getCompanytrackdevice().getImeiNo());
					vehicleCompositeMap.put(imeiNo, vehicleComposite);
				}
			}

		} catch (Exception e) {
			LOGGER.error("SKTLog : SKTAlertsEJB : getVehiclesInfo "
					+ e.getMessage());
			e.printStackTrace();

		}
		LOGGER.info("SKTLog : SKTAlertsEJB : Exit getVehiclesInfo Method ");
		LOGGER.debug("SKTLog : SKTAlertsEJB : vehicleComposites size="
				+ vehicleCompositeMap.size());
		return vehicleCompositeMap;
	}

	public List<VehicleData> getVehiclesInfo1() {
		LOGGER.info("SKTLog : SKTAlertsEJB : Entered into getVehiclesInfo");
		List<VehicleData> userVehicle = null;
		try {

			String sqlforUser = "SELECT v,vhctd FROM Vehicle v LEFT JOIN v.vehicleHasCompanytrackdevices vhctd WHERE v.vin=vhctd.id.vehicleVin";
			Query query1 = em.createQuery(sqlforUser);
			LOGGER.debug("SKTLog : SKTAlertsEJB : Before sqlforUser Execute Query "
					+ sqlforUser);
			List<Object[]> resultList = query1.getResultList();
			LOGGER.debug("SKTLog : SKTAlertsEJB : After sqlforUser Execute Query "
					+ resultList.size());
			userVehicle = new ArrayList<VehicleData>();
			if (!resultList.isEmpty() && resultList.size() != 0) {
				for (int i = 0; i < resultList.size(); i++) {
					Object[] row = (Object[]) resultList.get(i);
					VehicleData vehicleData = new VehicleData();
					Vehicle vehicle = (Vehicle) row[0];
					String vNo = vehicle.getVin();
					vehicleData.setVin(vNo);
					vehicleData.setPlateNo(vehicle.getPlateNo());
					vehicleData.setBranchId(vehicle.getBranchId());
					vehicleData.setCompanyId(vehicle.getCompanyId());

					if (row[1] != null) {
						VehicleHasCompanytrackdevice vhctd = (VehicleHasCompanytrackdevice) row[1];
						vehicleData.setImeiNo(String.valueOf(vhctd
								.getCompanytrackdevice().getImeiNo()));
					}
					userVehicle.add(vehicleData);
				}
			}
		} catch (Exception e) {
			LOGGER.error("SKTLog : SKTAlertsEJB : getVehiclesInfo " + e);
		}
		LOGGER.info("SKTLog : SKTAlertsEJB : Exit getVehiclesInfo Method ");
		LOGGER.debug("SKTLog : SKTAlertsEJB : userVehicle size="
				+ userVehicle.size());
		return userVehicle;
	}

	public Map<String, RouteAndVehicleData> getRouteAndVehicleData() {
		Map<String, RouteAndVehicleData> routeAndVehicleDataMap = new HashMap<String, RouteAndVehicleData>();
		try {
			Map<String, VehicleComposite> vehicleComposites = getVehiclesInfo();
			for (Entry<String, VehicleComposite> vehicleCompositeMap : vehicleComposites
					.entrySet()) {
				Map<String, Studentdetails> studentDetailsMap = new HashMap<String, Studentdetails>();
				String vin = vehicleCompositeMap.getValue().getVehicle()
						.getVin();
				String sqlQuery = "SELECT vh FROM  Schoolroute vh WHERE vh.vin = '"
						+ vin + "'";
				LOGGER.debug("SKTLog : SKTAlertsEJB : getDBforSKT Query===="
						+ sqlQuery);
				Query query = em.createQuery(sqlQuery);
				LOGGER.debug("SKTLog : SKTAlertsEJB : getDBforSKT Query===="
						+ query.toString());
				RouteAndVehicleData routeAndVehicleData = new RouteAndVehicleData();

				List<Schoolroute> schoolroutes = (List<Schoolroute>) query
						.getResultList();
				if (!schoolroutes.isEmpty() && schoolroutes.size() != 0) {

					Schoolroute schoolroute = schoolroutes.get(0);
					for (Routetrip routetrip : schoolroute.getRoutetrips()) {
						for (Tripstops tripstops : routetrip.getTripstopses()) {
							for (Studentdetails studentdetails : (Set<Studentdetails>) getStudentDetailsByStop(tripstops
									.getStopPointId())) {
								if (studentdetails.getTagdetails() != null)
									studentDetailsMap.put(studentdetails
											.getTagdetails().getTagId(),
											studentdetails);
							}

							for (Studentdetails studentdetails : (Set<Studentdetails>) getStudentDetailsByStop(tripstops
									.getStopPointId())) {
								if (studentdetails.getTagdetails() != null)
									studentDetailsMap.put(studentdetails
											.getTagdetails().getTagId(),
											studentdetails);
							}
						}
					}
					routeAndVehicleData.setSchoolRoute(schoolroute);
					routeAndVehicleData.setStudentDetailsMap(studentDetailsMap);
				}
				routeAndVehicleData.setVehicleComposite(vehicleCompositeMap
						.getValue());
				routeAndVehicleDataMap.put(vehicleCompositeMap.getKey(),
						routeAndVehicleData);
			}
			return routeAndVehicleDataMap;
		} catch (Exception e) {
			LOGGER.error("SKTLog : SKTAlertsEJB : getVehiclesInfo " + e);
			return null;
		}
	}

	private Map<String, StudentGeozone> getStudentGeoZones() {
		Map<String, StudentGeozone> aboutToArriveMap = new HashMap<String, StudentGeozone>();
		try {
			TypedQuery<StudentGeozone> queryGeo = em.createQuery(
					"select c from StudentGeozone c", StudentGeozone.class);
			List<StudentGeozone> geoResult = queryGeo.getResultList();
			if (!geoResult.isEmpty() && geoResult.size() != 0) {
				for (StudentGeozone vehiclegeoIterator : geoResult) {
					Studentdetails studentdetails = (Studentdetails) vehiclegeoIterator
							.getStudentdetailses();
					aboutToArriveMap.put(studentdetails.getStin(),
							vehiclegeoIterator);
				}
			}
		} catch (Exception e) {
			LOGGER.error("SKTLog : SKTAlertsEJB : getStudentGeoZones " + e);
			return null;
		}
		return aboutToArriveMap;
	}

	public String getTimeZoneRegion(String vin) {
		LOGGER.info("FleetTrackingDeviceListenerBO::getTimeZoneRegion" + "Vin"
				+ vin);
		String region = null;
		try {
			Query query = emAdmin
					.createQuery("SELECT cb.region FROM Companybranch cb,Vehicle v where v.companyId =cb.id.companyId "
							+ "and v.branchId = cb.id.branchId and v.vin =:vin");
			query.setParameter("vin", vin);
			LOGGER.info("Before Execute Query" + "query" + query);
			List<String> objects = (List<String>) query.getResultList();
			if (!objects.isEmpty() && objects.size() != 0)
				region = objects.get(0);
			LOGGER.info("After Execute Query" + "query" + query);
		} catch (Exception e) {
			LOGGER.error("FleetTrackingDeviceListenerBO::getTimeZoneRegion::Exception occured"
					+ e);
		}
		return region;
	}

	public String getDeviceModelName(String vin) {
		LOGGER.info("SKTAlertsEJB::getDeviceModelName" + "Vin" + vin);
		String modelName = null;
		try {
			Query query = em
					.createQuery("SELECT ctd.companytrackdevicemodels.id.modelName FROM Companytrackdevice ctd,VehicleHasCompanytrackdevice vhctd where vhctd.id.companytrackdeviceImeiNo = ctd.imeiNo "
							+ "and vhctd.vehicle.vin =:vin");
			query.setParameter("vin", vin);
			LOGGER.info("Before Execute Query" + "query" + query);
			List<String> objects = (List<String>) query.getResultList();
			if (!objects.isEmpty() && objects.size() != 0)
				modelName = objects.get(0);
			LOGGER.info("After Execute Query" + "query" + query);
		} catch (Exception e) {
			LOGGER.error("SKTAlertsEJB::getDeviceModelName::Exception occured"
					+ e);
		}
		return modelName;
	}

	@Override
	public void persistDemoAlerts(Demoalerts demoAlerts) {
		try {
			LOGGER.error("SKTAlertsEJB::persistDemoAlerts::demoAlerts ="
					+ " and " + demoAlerts.getAlerttype() + " and "
					+ demoAlerts.getContactNo() + " and "
					+ demoAlerts.getDescription() + " and "
					+ demoAlerts.getLatlong() + " and "
					+ demoAlerts.getLocation() + " and " + demoAlerts.getStin()
					+ " and " + demoAlerts.getTagId() + " and "
					+ demoAlerts.getVin() + " and " + demoAlerts.getId()
					+ " and " + demoAlerts.getTripid() + " and "
					+ demoAlerts.getEventTimeStamp());
			emStudent.persist(demoAlerts);
		} catch (Exception e) {
			LOGGER.error("SKTAlertsEJB::persistDemoAlerts::Exception occured");
			e.printStackTrace();
		}

	}

	@Override
	public boolean checkIsGateDevice(String imeiNo) {
		Query query = emStudent
				.createQuery("SELECT ghd FROM Gatehasdevice ghd WHERE ghd.deviceimei='"
						+ imeiNo + "'");
		List<Gatehasdevice> gatehasdevices = query.getResultList();
		if (!gatehasdevices.isEmpty() && gatehasdevices.size() != 0)
			return true;
		else
			return false;
	}

	@Override
	public List<Alertevents> getStudentsNotDAS(String vin) {
		LOGGER.info("SKTAlertsEJB::getStudentsNotDAS::inside method");

		List<Alertevents> alerteventsNDAS = new ArrayList<Alertevents>();

		Query query = emStudent
				.createNativeQuery("SELECT ae1.* FROM alertevents ae1 INNER JOIN (SELECT ae.stin as aestin, MAX(ae.eventTimeStamp) as aetime "
						+ "FROM alertevents ae  WHERE ae.vin ='"
						+ vin
						+ "' AND DATE(ae.eventTimeStamp) = CURDATE() AND ae.contactNo ='MISSWIPE' GROUP BY ae.stin) aee "
						+ " where ae1.eventTimeStamp=aee.aetime and ae1.stin= aee.aestin");

		List<Object[]> alerteventsList = (List<Object[]>) query.getResultList();
		LOGGER.info("SKTAlertsEJB::getStudentsNotDAS::size = "
				+ alerteventsList.size());
		for (Object[] object : alerteventsList) {
			Alertevents alertevent = getAlertEvent(object);
			String stin = alertevent.getStudentdetails().getStin();
			query = emStudent
					.createQuery("SELECT ae FROM Alertevents ae WHERE ae.studentdetails.stin ='"
							+ stin
							+ "' ORDER BY ae.eventTimeStamp DESC LIMIT 1");
			query.setMaxResults(1);

			List<Alertevents> alertevents = (List<Alertevents>) query
					.getResultList();
			if (!alertevents.isEmpty() && alertevents.size() != 0) {
				Alertevents alertevents1 = alertevents.get(0);
				LOGGER.info("SKTAlertsEJB::getStudentsNotDAS::SDAS = " + stin
						+ " = " + alertevents1.getAlerttypes().getAlerttype());
				if (!alertevents1.getAlerttypes().getAlerttype()
						.equalsIgnoreCase("SDAS"))
					alerteventsNDAS.add(alertevent);
			}
		}
		return alerteventsNDAS;

	}

	@Override
	public List<Alertevents> getStudentsNotDAB(String vin, Tripstops tripstops) {
		List<Alertevents> alerteventsNDAB = new ArrayList<Alertevents>();
		try {
			Query query = emStudent
					.createNativeQuery("SELECT ae1.* FROM alertevents ae1 INNER JOIN (SELECT ae.stin as aestin, MAX(ae.eventTimeStamp) as aetime "
							+ "FROM alertevents ae  WHERE ae.vin ='"
							+ vin
							+ "' AND DATE(ae.eventTimeStamp) = CURDATE() AND ae.contactNo ='MISSWIPE' GROUP BY ae.stin) aee "
							+ " ON ae1.eventTimeStamp=aee.aetime and ae1.stin= aee.aestin INNER JOIN studentdetails sd ON sd.stin=ae1.stin and sd.dropid='"
							+ tripstops.getStopPointId() + "'");

			List<Object[]> alerteventsList = (List<Object[]>) query
					.getResultList();
			LOGGER.info("SKTAlertsEJB::getStudentsNotDAB::size = "
					+ alerteventsList.size());
			for (Object[] object : alerteventsList) {
				Alertevents alertevent = getAlertEvent(object);
				String stin = alertevent.getStudentdetails().getStin();
				query = emStudent
						.createQuery("SELECT ae FROM Alertevents ae WHERE ae.studentdetails.stin ='"
								+ stin
								+ "' ORDER BY ae.eventTimeStamp DESC LIMIT 1");
				query.setMaxResults(1);

				List<Alertevents> alertevents = (List<Alertevents>) query
						.getResultList();
				if (!alertevents.isEmpty() && alertevents.size() != 0) {

					Alertevents alertevents1 = alertevents.get(0);
					LOGGER.info("SKTAlertsEJB::getStudentsNotDAB::SDAB = "
							+ stin + " = "
							+ alertevents1.getAlerttypes().getAlerttype());
					if (!alertevents1.getAlerttypes().getAlerttype()
							.equalsIgnoreCase("SDAB")) {
						SimpleDateFormat localDateFormat = new SimpleDateFormat(
								"HH:mm:ss");

						Date eventTimeStamp = localDateFormat
								.parse(localDateFormat.format(alertevent
										.getEventTimeStamp()));
						boolean isAfterTripStartTime = tripstops.getRoutetrip()
								.getTripStarttime().before(eventTimeStamp);
						boolean isBeforeTripEndTime = tripstops.getRoutetrip()
								.getTripEndtime().after(eventTimeStamp);
						if (isAfterTripStartTime && isBeforeTripEndTime)
							alerteventsNDAB.add(alertevent);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("SKTAlertsEJB::getStudentsNotDAB::Exception occured");
			e.printStackTrace();
		}
		return alerteventsNDAB;
	}

	private Alertevents getAlertEvent(Object[] object) {
		Alertevents alertevents = new Alertevents();
		alertevents.setId((Integer) object[0]);
		Studentdetails studentdetails = new Studentdetails();
		studentdetails.setStin((String) object[1]);
		alertevents.setStudentdetails(studentdetails);
		alertevents.setEventTimeStamp((Date) object[2]);
		alertevents.setAlerttypes(new Alerttypes((String) object[3]));
		alertevents.setDescription((String) object[4]);
		alertevents.setLatlong((String) object[5]);
		alertevents.setLocation((String) object[6]);
		alertevents.setVin((String) object[7]);
		alertevents.setTagId((String) object[8]);
		alertevents.setContactNo((String) object[9]);
		alertevents.setTripid(Long.valueOf(String.valueOf(object[10])));
		return alertevents;
	}

	@Override
	public List<Pushnotificationdevices> getPushNotificationDevice(
			String contactNo, String companyId) {
		try {
			Query query = emAdmin
					.createQuery("SELECT pnd FROM Pushnotificationdevices pnd WHERE pnd.userId ='"
							+ contactNo
							+ "' AND pnd.company.companyId='"
							+ companyId + "'AND status='1'");
			return query.getResultList();
		} catch (Exception e) {
			LOGGER.error("SKTAlertsEJB::getPushNotificationDevice::Exception occured");
			e.printStackTrace();
			return null;
		}

	}

	public Map<String, VehicleComposite> getVehicleInfo(String vin) {
		LOGGER.info("SKTLog : SKTAlertsEJB : Entered into getVehicleInfo");
		try {
			Map<String, VehicleComposite> vehicleMap = new HashMap<String, VehicleComposite>();
			String sqlforUser = "SELECT v,vhctd FROM Vehicle v LEFT JOIN v.vehicleHasCompanytrackdevices vhctd "
					+ "WHERE v.vin=vhctd.id.vehicleVin and v.vin='" + vin + "'";
			Query query1 = em.createQuery(sqlforUser);

			List<Object[]> objects = (List<Object[]>) query1.getResultList();
			if (!objects.isEmpty() && objects.size() != 0) {
				Object[] result = objects.get(0);
				Vehicle vehicle = (Vehicle) result[0];
				VehicleComposite vehicleComposite = new VehicleComposite();
				vehicleComposite.setVehicle(vehicle);
				vehicleComposite.setTimeZoneRegion(getTimeZoneRegion(vehicle
						.getVin()));
				vehicleComposite.setDeviceModel(getDeviceModelName(vehicle
						.getVin()));
				VehicleHasCompanytrackdevice vhctd = (VehicleHasCompanytrackdevice) result[1];
				String imeiNo = String.valueOf(vhctd.getCompanytrackdevice()
						.getImeiNo());
				vehicleMap.put(imeiNo, vehicleComposite);
			}
			return vehicleMap;
		} catch (Exception e) {
			LOGGER.error("SKTLog : SKTAlertsEJB : getVehiclesInfo "
					+ e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public String getVehicleImei(String vin) {
		LOGGER.info("SKTLog : SKTAlertsEJB : Entered into getVehicleInfo");
		try {
			String sqlforVehicle = "SELECT vhctd.if.companytrackdeviceImeiNo FROM VehicleHasCompanytrackdevice vhctd WHERE vhctd.id.vehicleVin='"
					+ vin + "'";
			Query query1 = em.createQuery(sqlforVehicle);
			LOGGER.debug("SKTLog : SKTAlertsEJB : Before sqlforUser Execute Query "
					+ sqlforVehicle);
			List<String> objects = (List<String>) query1.getResultList();
			if (!objects.isEmpty() && objects.size() != 0) {
				return objects.get(0);
			}
			return null;
		} catch (Exception e) {
			LOGGER.error("SKTLog : SKTAlertsEJB : getVehicleInfo "
					+ e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String updateStaticSchoolRouteDetails(String routeId, String mode) {
		try {
			if (mode.equalsIgnoreCase("delete")) {

				String imei = getVehicleImei(routeId);
				SKTStartup.getSKTinitData().getSchoolRoutes().get(imei)
						.setSchoolRoute(null);
				SKTStartup.getSKTinitData().getSchoolRoutes().get(imei)
						.setStudentDetailsMap(null);
				RouteAndVehicleData sData = SKTStartup.getSKTinitData()
						.getSchoolRoutes().get(imei);
				System.out.println();
			} else if (mode.equalsIgnoreCase("add")
					|| mode.equalsIgnoreCase("associate")) {

				Schoolroute schoolroute = em.find(Schoolroute.class,
						Long.parseLong(routeId));
				Map<String, Studentdetails> studentDetailsMap = new HashMap<String, Studentdetails>();
				if (schoolroute != null) {
					for (Routetrip routetrip : schoolroute.getRoutetrips()) {
						for (Tripstops tripstops : routetrip.getTripstopses()) {
							for (Studentdetails studentdetails : (Set<Studentdetails>) getStudentDetailsByStop(tripstops
									.getStopPointId())) {
								if (studentdetails.getTagdetails() != null)
									studentDetailsMap.put(studentdetails
											.getTagdetails().getTagId(),
											studentdetails);
							}

							for (Studentdetails studentdetails : tripstops
									.getStudentdetailsesForDropid()) {
								if (studentdetails.getTagdetails() != null)
									studentDetailsMap.put(studentdetails
											.getTagdetails().getTagId(),
											studentdetails);
							}
						}
					}
					String imei = getVehicleImei(schoolroute.getVin());
					SKTStartup.getSKTinitData().getSchoolRoutes().get(imei)
							.setSchoolRoute(schoolroute);
					SKTStartup.getSKTinitData().getSchoolRoutes().get(imei)
							.setStudentDetailsMap(studentDetailsMap);
				}

			} else if (mode.equalsIgnoreCase("disassociate")) {

				String imei = getVehicleImei(routeId);
				SKTStartup.getSKTinitData().getSchoolRoutes().get(imei)
						.setSchoolRoute(null);
				SKTStartup.getSKTinitData().getSchoolRoutes().get(imei)
						.setStudentDetailsMap(null);
			}

			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String updateVehicleDetails(String vin, String mode) {
		try {
			if (mode.equalsIgnoreCase("add")) {
				Map<String, VehicleComposite> vehicleComposites = getVehicleInfo(vin);
				for (Entry<String, VehicleComposite> vehicleCompositeMap : vehicleComposites
						.entrySet()) {

					RouteAndVehicleData routeAndVehicleData = new RouteAndVehicleData();
					System.out.println(vehicleCompositeMap.getKey()
							+ " and "
							+ vehicleCompositeMap.getValue().getVehicle()
									.getPlateNo());

					Map<String, Studentdetails> studentDetailsMap = new HashMap<String, Studentdetails>();
					String sqlQuery = "SELECT vh FROM  Schoolroute vh WHERE vh.vin = '"
							+ vin + "'";
					Query query = em.createQuery(sqlQuery);

					List<Schoolroute> schoolroutes = (List<Schoolroute>) query
							.getResultList();
					if (!schoolroutes.isEmpty() && schoolroutes.size() != 0) {
						Schoolroute schoolroute = schoolroutes.get(0);
						for (Routetrip routetrip : schoolroute.getRoutetrips()) {
							for (Tripstops tripstops : routetrip
									.getTripstopses()) {
								for (Studentdetails studentdetails : (Set<Studentdetails>) getStudentDetailsByStop(tripstops
										.getStopPointId())) {
									if (studentdetails.getTagdetails() != null)
										studentDetailsMap.put(studentdetails
												.getTagdetails().getTagId(),
												studentdetails);
								}

								for (Studentdetails studentdetails : tripstops
										.getStudentdetailsesForDropid()) {
									if (studentdetails.getTagdetails() != null)
										studentDetailsMap.put(studentdetails
												.getTagdetails().getTagId(),
												studentdetails);
								}
							}
						}
						routeAndVehicleData.setSchoolRoute(schoolroute);
						routeAndVehicleData
								.setStudentDetailsMap(studentDetailsMap);
					}
					routeAndVehicleData.setVehicleComposite(vehicleCompositeMap
							.getValue());

					SKTStartup
							.getSKTinitData()
							.getSchoolRoutes()
							.put(vehicleCompositeMap.getKey(),
									routeAndVehicleData);
					System.out.println(SKTStartup.getSKTinitData()
							.getSchoolRoutes().size());
					for (Entry<String, RouteAndVehicleData> sEntries : SKTStartup
							.getSKTinitData().getSchoolRoutes().entrySet()) {
						System.out.println("sEntries.getKey()="
								+ sEntries.getKey());
						RouteAndVehicleData routeAndVehicleData2 = sEntries
								.getValue();
						System.out.println();
					}

				}
			} else if (mode.equalsIgnoreCase("delete")) {
				String imei = vin;
				SKTStartup.getSKTinitData().getSchoolRoutes().remove(imei);
				System.out.println(SKTStartup.getSKTinitData()
						.getSchoolRoutes().size());
				for (Entry<String, RouteAndVehicleData> sEntries : SKTStartup
						.getSKTinitData().getSchoolRoutes().entrySet()) {
					System.out
							.println("sEntries.getKey()=" + sEntries.getKey());
					RouteAndVehicleData routeAndVehicleData2 = sEntries
							.getValue();
					System.out.println();
				}
			}

			return "success";
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String updateStudentDetails(String tagId, String mode) {
		try {
			if (mode.equalsIgnoreCase("add")) {
				String[] strArray = tagId.split("###");
				tagId = strArray[0];
				updateExistingStudentDetails(strArray[1]);
				updateExistingStudentDetails(strArray[2]);

				Query query = emStudent
						.createQuery("SELECT stud FROM  Studentdetails stud "
								+ "WHERE stud.tagdetails.tagId='" + tagId + "'");
				List<Studentdetails> studentdetailsList = (List<Studentdetails>) query
						.getResultList();
				if (!studentdetailsList.isEmpty()
						&& studentdetailsList.size() != 0) {
					Studentdetails studentdetails = studentdetailsList.get(0);
					if (studentdetails.getTripstopsByPickupid() != null) {
						String stopPointId = String.valueOf(studentdetails
								.getTripstopsByPickupid().getStopPointId());
						updateExistingStudentDetails(stopPointId);
					}
					if (studentdetails.getTripstopsByDropid() != null) {
						String stopPointId = String.valueOf(studentdetails
								.getTripstopsByDropid().getStopPointId());
						updateExistingStudentDetails(stopPointId);
					}
					// String studentVehicleVin = studentdetails
					// .getTripstopsByDropid().getRoutetrip()
					// .getSchoolroute().getVin();
					// String imei = getVehicleImei(studentVehicleVin);
					// SKTStartup.getSKTinitData().getSchoolRoutes().get(imei)
					// .getStudentDetailsMap().put(tagId, studentdetails);
					// LOGGER.error(SKTStartup.getSKTinitData().getSchoolRoutes()
					// .get(imei).getStudentDetailsMap().size());

					query = emStudent
							.createQuery("SELECT vhsg FROM StudentGeozone vhsg WHERE vhsg.id='"
									+ studentdetails.getStudentGeozone()
											.getId() + "'");

					List<StudentGeozone> studentGeozones = (List<StudentGeozone>) query
							.getResultList();
					if (!studentGeozones.isEmpty()
							&& studentGeozones.size() != 0) {
						StudentGeozone StudentGeozone = studentGeozones.get(0);
						SKTStartup.getSKTinitData().getStudentGeoZoneMap()
								.put(studentdetails.getStin(), StudentGeozone);
						LOGGER.error(SKTStartup.getSKTinitData()
								.getStudentGeoZoneMap().size());

						return "success";
					} else
						return null;
				}
			} else if (mode.equalsIgnoreCase("delete")) {
				String[] tagIdnVin = tagId.split("###");
				tagId = tagIdnVin[0];
				String imei = getVehicleImei(tagIdnVin[1]);
				Map<String, Studentdetails> sMap = SKTStartup.getSKTinitData()
						.getSchoolRoutes().get(imei).getStudentDetailsMap();
				String stin = sMap.get(tagId).getStin();
				sMap.remove(tagId);
				System.out.println(sMap.size());

				SKTStartup.getSKTinitData().getStudentGeoZoneMap().remove(stin);
				System.out.println(SKTStartup.getSKTinitData()
						.getStudentGeoZoneMap().size());
				return "success";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void updateExistingStudentDetails(String stopPointId) {
		try {
			if (!stopPointId.equalsIgnoreCase("null")) {
				Tripstops tripstops = em.find(Tripstops.class,
						Long.parseLong(stopPointId));
				Long routeId = tripstops.getRoutetrip().getSchoolroute()
						.getId();
				updateStaticSchoolRouteDetails(String.valueOf(routeId), "add");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void persistDemoAlerts(Alertevents alertevents) {
		try {
			LOGGER.info("SKTAlertsEJB: persistDemoAlerts "
					+ alertevents.getDescription());
			em.persist(alertevents);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Routetrip getCurrentRouteTrip(String vin, String compId,
			String branchId) {

		LOGGER.info("SKTLog : SKTAlertsEJB : Entered getSKTTripStops Method skt");
		List<Routetrip> routetrips = new ArrayList<Routetrip>();
		try {
			String sql = "SELECT vh FROM  Schoolroute vh WHERE vh.vin = '"
					+ vin + "' AND  compId= '" + compId + "' AND branchId = '"
					+ branchId + "'";
			LOGGER.debug("SKTLog : SKTAlertsEJB : getSKTTripStops Query===="
					+ sql);
			Query query = em.createQuery(sql);
			LOGGER.debug("SKTLog : SKTAlertsEJB : getSKTTripStops Query===="
					+ query.toString());

			List<Schoolroute> schoolrouteList = (List<Schoolroute>) query
					.getResultList();
			if (!schoolrouteList.isEmpty() && schoolrouteList.size() != 0) {

				Schoolroute schoolroute = schoolrouteList.get(0);
				try {
					Set<Routetrip> listoftrips = schoolroute.getRoutetrips();
					routetrips.addAll(listoftrips);
					LOGGER.info("SKTLog : SKTAlertsEJB : getSKTTripStops routetrips size===="
							+ routetrips.size());
					if (routetrips.size() != 0) {
						for (Routetrip singleRouteTrip : routetrips) {
							int enabledtripId = getEnabledTripId(
									singleRouteTrip, compId, branchId);
							if (enabledtripId != -1) {
								return singleRouteTrip;
							}
						}
					}
				} catch (Exception e) {
					LOGGER.error("SKTLog : SKTAlertsEJB : getSKTTripStops Exception = "
							+ e.getMessage());
				}
			}
		} catch (Exception e) {
			LOGGER.error("SKTLog : SKTAlertsEJB : getSKTTripStops Exception= "
					+ e.getMessage());
		}
		return null;

	}

	@Override
	public List<Freeformgeo> getSchoolGeoZone(String companyId) {

		try {
			Query query = emStudent
					.createQuery("SELECT fg FROM  Freeformgeo fg WHERE fg.category = '"
							+ companyId + "'  order by fg.pos");
			return query.getResultList();
		} catch (Exception e) {
			LOGGER.error("SKTLog : SKTAlertsEJB : getSchoolGeoZoneId ="
					+ e.getMessage());
		}
		return null;
	}

	@Override
	public boolean getFreeformGeoStatus(Float latitude, Float longitude,
			List<Freeformgeo> schoolGeozone) {
		String latVe = String.valueOf(latitude);
		String lngVe = String.valueOf(longitude);
		LOGGER.info("SKTAlertsEJB::getFreeformGeoStatus::Entered into this method::");
		try {
			StringBuilder sbuf = new StringBuilder();
			boolean isFirst = true;
			String firstVal = "";
			for (Freeformgeo tr : schoolGeozone) {
				String latlngCen = tr.getId().getLatlng().replace(",", " ");
				if (isFirst) {
					firstVal = latlngCen;
					isFirst = false;
				}
				sbuf.append(latlngCen + ",");
			}
			sbuf.append(firstVal);

			Query q1 = em
					.createNativeQuery("SELECT fleettrackingdb.myWithin(GEOMFROMTEXT('Point("
							+ latVe
							+ " "
							+ lngVe
							+ ")'),GEOMFROMTEXT('POLYGON(("
							+ sbuf.toString()
							+ "))'));");
			LOGGER.info("Before Execute Query::q1" + q1);
			List<Object> objects = (List<Object>) q1.getResultList();
			if (!objects.isEmpty() && objects.size() != 0) {

				Object object = objects.get(0);
				LOGGER.info("After Execute Queryq1" + q1);
				String str = String.valueOf(object);
				LOGGER.info("AlertsEJB::getFreeformGeoStatus::Leaving from this method successfully");
				return str.trim().equalsIgnoreCase("0") ? false : true;
			} else
				return false;
		} catch (Exception e) {
			LOGGER.error("AlertsEJB::getFreeformGeoStatus::Geo query Error="
					+ e);
			return false;
		}
	}

	public boolean isTimeInRange(Date tripStartTime, Date tripEndTime,
			Date eventTimeStamp) {

		Calendar calendarEventTime = Calendar.getInstance();
		calendarEventTime.setTime(eventTimeStamp);
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(tripStartTime);
		calendar.set(Calendar.DATE, calendarEventTime.get(Calendar.DATE));
		calendar.set(Calendar.MONTH, calendarEventTime.get(Calendar.MONTH));
		calendar.set(Calendar.YEAR, calendarEventTime.get(Calendar.YEAR));

		tripStartTime = calendar.getTime();
		calendar.setTime(tripEndTime);
		calendar.set(Calendar.DATE, calendarEventTime.get(Calendar.DATE));
		calendar.set(Calendar.MONTH, calendarEventTime.get(Calendar.MONTH));
		calendar.set(Calendar.YEAR, calendarEventTime.get(Calendar.YEAR));

		tripEndTime = calendar.getTime();
		LOGGER.info("SKTLog:MeiTrackDeviceHandler isTimeInRange "
				+ tripStartTime.toString() + " and " + tripEndTime.toString()
				+ " and " + eventTimeStamp.toString());

		boolean isCurrentTrip = CommonUtil.isTimeInRange(tripStartTime,
				tripEndTime, eventTimeStamp);
		return isCurrentTrip;
	}

	@Override
	public Routetrip getUpcomingTrip(String vin, String companyId,
			String branchId, Date eventTimeStamp) {
		// TODO Auto-generated method stub
		LOGGER.info("SKTLog : SKTAlertsEJB : Entered getUpcomingTrip Method skt");
		List<Routetrip> routetrips = new ArrayList<Routetrip>();
		try {
			String sql = "SELECT vh FROM  Schoolroute vh WHERE vh.vin = '"
					+ vin + "' AND  compId= '" + companyId
					+ "' AND branchId = '" + branchId + "'";
			LOGGER.debug("SKTLog : SKTAlertsEJB : getUpcomingTrip Query===="
					+ sql);
			Query query = em.createQuery(sql);
			LOGGER.debug("SKTLog : SKTAlertsEJB : getUpcomingTrip Query===="
					+ query.toString());
			if (!query.getResultList().isEmpty()
					&& query.getResultList().size() != 0) {
				List<Schoolroute> objects = (List<Schoolroute>) query
						.getResultList();
				Schoolroute schoolroute = objects.get(0);
				try {
					Set<Routetrip> listoftrips = schoolroute.getRoutetrips();
					routetrips.addAll(listoftrips);
					Collections.sort(routetrips, new Comparator<Routetrip>() {
						public int compare(Routetrip o1, Routetrip o2) {
							if (o1.getTripStarttime() == o2.getTripStarttime())
								return 0;
							return o1.getTripStarttime().before(
									o2.getTripStarttime()) ? -1 : 1;
						}
					});
					LOGGER.info("SKTLog : SKTAlertsEJB : getUpcomingTrip routetrips size===="
							+ routetrips.size());
					if (routetrips.size() != 0) {
						for (Routetrip singleRouteTrip : routetrips) {
							String curTime = getAsString(eventTimeStamp
									.getHours())
									+ ":"
									+ getAsString(eventTimeStamp.getMinutes())
									+ ":"
									+ getAsString(eventTimeStamp.getSeconds());
							Query queryfortrip = emStudent
									.createNativeQuery("SELECT tripid FROM routetrip WHERE ('"
											+ curTime
											+ "' BETWEEN tripStarttime AND tripEndtime OR '"
											+ curTime
											+ "' < tripStarttime) AND tripid = '"
											+ singleRouteTrip.getTripid() + "'");

							if (queryfortrip.getResultList().size() != 0) {
								LOGGER.info("SKTLog: SKTAlertsEJB::getUpcomingTrip singleRouteTrip = "
										+ singleRouteTrip.getTripid());
								return singleRouteTrip;
							}
						}
					}
				} catch (Exception e) {
					LOGGER.error("SKTLog : SKTAlertsEJB : getSKTTripStops Exception = "
							+ e.getMessage());
				}
			} else {
				Routetrip routetrip = new Routetrip();
				routetrip.setTripid(null);
				return routetrip;
			}
		} catch (Exception e) {
			LOGGER.error("SKTLog : SKTAlertsEJB : getUpcomingTrip Exception= "
					+ e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Routetrip getUpcomingDayTrip(String vin, String companyId,
			String branchId) {
		// TODO Auto-generated method stub
		LOGGER.info("SKTLog : SKTAlertsEJB : Entered getUpcomingDayTrip Method skt");
		List<Routetrip> routetrips = new ArrayList<Routetrip>();
		try {
			String sql = "SELECT vh FROM  Schoolroute vh WHERE vh.vin = '"
					+ vin + "' AND  compId= '" + companyId
					+ "' AND branchId = '" + branchId + "'";
			LOGGER.debug("SKTLog : SKTAlertsEJB : getUpcomingDayTrip Query===="
					+ sql);
			Query query = em.createQuery(sql);
			LOGGER.debug("SKTLog : SKTAlertsEJB : getUpcomingDayTrip Query===="
					+ query.toString());
			if (!query.getResultList().isEmpty()
					&& query.getResultList().size() != 0) {
				List<Schoolroute> objects = (List<Schoolroute>) query
						.getResultList();
				Schoolroute schoolroute = objects.get(0);
				try {
					Set<Routetrip> listoftrips = schoolroute.getRoutetrips();
					routetrips.addAll(listoftrips);
					Collections.sort(routetrips, new Comparator<Routetrip>() {
						public int compare(Routetrip o1, Routetrip o2) {
							if (o1.getTripStarttime() == o2.getTripStarttime())
								return 0;
							return o1.getTripStarttime().before(
									o2.getTripStarttime()) ? -1 : 1;
						}
					});
					LOGGER.info("SKTLog : SKTAlertsEJB : getUpcomingDayTrip routetrips size===="
							+ routetrips.get(0).getTripid());
					return routetrips.get(0);
				} catch (Exception e) {
					LOGGER.error("SKTLog : SKTAlertsEJB : getUpcomingDayTrip Exception = "
							+ e.getMessage());
				}
			}
		} catch (Exception e) {
			LOGGER.error("SKTLog : SKTAlertsEJB : getUpcomingDayTrip Exception= "
					+ e.getMessage());
		}
		return null;
	}

	public String getAsString(int integer) {
		if (integer < 10) {
			return "0" + integer;
		}
		return String.valueOf(integer);

	}

	@Override
	public Tripstops getTripStopById(Long stopPointId) {
		// TODO Auto-generated method stub
		try {
			Query query = emStudent
					.createQuery("SELECT ts FROM Tripstops ts WHERE ts.stopPointId='"
							+ stopPointId + "'");
			// query.setCacheable(true);
			List<Tripstops> objects = (List<Tripstops>) query.getResultList();
			if (!objects.isEmpty() && objects.size() != 0) {
				return objects.get(0);
			}
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public boolean isAlertSubscribed(String stin, String alerttype) {
		try {
			Query query = emStudent
					.createQuery("SELECT sas from Studentalertsubscription sas where sas.id.stin=:stin and sas.id.alertsubscribed=:alertsubscribed");
			query.setParameter("alertsubscribed", alerttype);
			query.setParameter("stin", stin);
			List<Studentalertsubscription> objects = (List<Studentalertsubscription>) query
					.getResultList();
			if (!objects.isEmpty() && objects.size() != 0)
				return true;
			else
				return false;
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	public String getVehicleUserId(String vin) {
		// TODO Auto-generated method stub
		try {
			Vehicle vehicle = em.find(Vehicle.class, vin);
			return vehicle.getUserId();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean getApplicationStatus() {
		// TODO Auto-generated method stub
		try {
			Query query = emAdmin
					.createQuery("SELECT app FROM Applicationsettings app WHERE app.key='IsSKT'");
			List<Applicationsettings> objects = (List<Applicationsettings>) query
					.getResultList();
			if (objects.isEmpty() && objects.size() == 0) {
				return false;
			} else {
				String result = objects.get(0).getValues();
				if (result.equalsIgnoreCase("true"))
					return true;
				else
					return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean checkEnableOrDisableDay(Date eventTimeStamp,
			String companyId, String type) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String eventDate = sdf.format(eventTimeStamp);
		String sql = "SELECT type FROM school_disable_date WHERE '" + eventDate
				+ "' BETWEEN fromDate AND toDate and companyId='" + companyId
				+ "' and type='" + type + "'";
		Query query1 = emStudent.createNativeQuery(sql);
		if (!query1.getResultList().isEmpty() && query1.getResultList() != null) {
			String res = (String) query1.getResultList().get(0);
			if (res != null && !res.equalsIgnoreCase("")) {
				if (res.equalsIgnoreCase("Enable")) {
					return true;
				} else {
					return false;
				}
			}
		}
		if (type.equalsIgnoreCase("Enable"))
			return false;
		else
			return true;
	}

	@Override
	public String checkIsPresent(Studentdetails studentdetails) {
		// TODO Auto-generated method stub
		String result = "Present";
		boolean IsSchoolAttendence = checkIsSchoolAttendence(studentdetails
				.getClassdetails().getId().getSchoolId());
		if (IsSchoolAttendence) {
			try {
				Query query1 = emStudent
						.createNativeQuery("SELECT stin FROM `studentattendance` "
								+ " where DATE(eventTimeStamp) = CURDATE() AND stin='"
								+ studentdetails.getStin()
								+ "' ORDER BY eventTimeStamp ASC");
				if (!query1.getResultList().isEmpty()
						&& query1.getResultList() != null) {
					return result;
				}
				result = "Absent";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public boolean checkIsSchoolAttendence(String schoolId) {
		// TODO Auto-generated method stub
		try {
			Query query = emAdmin
					.createQuery("SELECT app FROM Applicationsettings app WHERE app.key='schoolAttendence'");
			List<Applicationsettings> objects = (List<Applicationsettings>) query
					.getResultList();
			if (objects.isEmpty() && objects.size() == 0) {
				return false;
			} else {
				String result = objects.get(0).getValues();
				if (!result.equalsIgnoreCase("")) {
					String schools[] = result.split(",");
					for (String school : schools) {
						if (schoolId.equalsIgnoreCase(school))
							return true;
					}
				}
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<VehicleTripTimeDto> getVehicleTripTime(String companyId,
			String branchId) {
		// TODO Auto-generated method stub
		List<VehicleTripTimeDto> vehicleTripTimeDtoList = new ArrayList<VehicleTripTimeDto>();
		try {
			Query query = emStudent
					.createQuery("SELECT sr FROM Schoolroute sr WHERE  compId ='"
							+ companyId
							+ "' and branchId ='"
							+ branchId
							+ "' AND vin<>'null'");
			if (!query.getResultList().isEmpty()
					&& query.getResultList() != null) {
				List<Schoolroute> resultList = query.getResultList();
				for (int i = 0; i < resultList.size(); i++) {
					Schoolroute schoolroute = (Schoolroute) resultList.get(i);
					if (MeiTrackDeviceHandler.vehicleTripTimeMap
							.get(schoolroute.getVin()) != null) {
						VehicleTripTimeDto vehicleTripTimeDto = new VehicleTripTimeDto();
						vehicleTripTimeDto = MeiTrackDeviceHandler.vehicleTripTimeMap
								.get(schoolroute.getVin());
						vehicleTripTimeDtoList.add(vehicleTripTimeDto);
					}
				}
			}
			return vehicleTripTimeDtoList;
		} catch (Exception ex) {
			LOGGER.error("getVehicleTripTime :" + ex);
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public void alertForAttendence(List<Studentdetails> studentdetails,
			String type) {
		// TODO Auto-generated method stub
		sktAlertsManager.alertForAttendence(studentdetails, type);

	}

	@Override
	public List<Routetrip> getRouteTrip(String vin, String companyId,
			String branchId, Date eventTimeStamp) {
		// TODO Auto-generated method stub
		LOGGER.info("SKTLog : SKTAlertsEJB : Entered getUpcomingTrip Method skt");
		List<Routetrip> routetrips = new ArrayList<Routetrip>();
		try {
			String sql = "SELECT vh FROM  Schoolroute vh WHERE vh.vin = '"
					+ vin + "' AND  compId= '" + companyId
					+ "' AND branchId = '" + branchId + "'";
			LOGGER.debug("SKTLog : SKTAlertsEJB : getUpcomingTrip Query===="
					+ sql);
			Query query = em.createQuery(sql);
			LOGGER.debug("SKTLog : SKTAlertsEJB : getUpcomingTrip Query===="
					+ query.toString());
			List<Schoolroute> objects = (List<Schoolroute>) query
					.getResultList();
			if (!objects.isEmpty() && objects.size() != 0) {
				Schoolroute schoolroute = objects.get(0);
				try {
					Set<Routetrip> listoftrips = schoolroute.getRoutetrips();
					routetrips.addAll(listoftrips);
					Collections.sort(routetrips, new Comparator<Routetrip>() {
						public int compare(Routetrip o1, Routetrip o2) {
							if (o1.getTripStarttime() == o2.getTripStarttime())
								return 0;
							return o1.getTripStarttime().before(
									o2.getTripStarttime()) ? -1 : 1;
						}
					});
					return routetrips;
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				return routetrips;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return routetrips;
	}

	@Override
	public Alertevents insertAlertevents(Alertevents alertEvent, String mode) {
		LOGGER.info("SKTAlertsEJB::insertAlertevents::" + "alertEvent"
				+ alertEvent);
		try {
			if (mode.equalsIgnoreCase("update")) {
				emStudent.merge(alertEvent);
				return alertEvent;
			} else {
				alertEvent.setId(null);
				emStudent.persist(alertEvent);
				return alertEvent;
			}
		} catch (Exception e) {
			LOGGER.error("SKTAlertsEJB::insertVehicleAlert::error in persisting alertEvent"
					+ e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Vehicletimings getVehicleTimings(Vehicle vehicle) {
		Vehicletimings vehicletimings = null;
		try {
			Query query = emStudent
					.createQuery("SELECT vt FROM Vehicletimings vt WHERE vt.vin=:vin");
			query.setParameter("vin", vehicle.getVin());
			vehicletimings = (Vehicletimings) query.getSingleResult();
		} catch (Exception e) {
			LOGGER.error("SKTAlertsEJB:: getVehicleTimings:: error " + e);
		}
		return vehicletimings;
	}

	@Override
	public List<StudentGeozone> getStudentGeoConfig(String vinGeo, String type) {
		LOGGER.info("SKTAlertsEJB::getStudentGeoConfig::Entered into this method"
				+ "Vin" + vinGeo + "type" + type);
		try {
			String sql = "";
			if (type.equalsIgnoreCase("pickup")) {
				sql = "SELECT DISTINCT gz.* FROM `student_geozone` AS gz INNER JOIN studentdetails AS sd ON gz.id=sd.geozoneId INNER JOIN tripstops AS ts ON sd.pickupid=ts.stopPointId INNER JOIN  `routetrip` AS rt ON ts.tripid = rt.tripId INNER JOIN schoolroute sr ON sr.id = rt.routeid WHERE sr.vin = :vin";
			} else {
				sql = "SELECT DISTINCT gz.* FROM `student_geozone` AS gz INNER JOIN studentdetails AS sd ON gz.id=sd.geozoneId INNER JOIN tripstops AS ts ON sd.dropid=ts.stopPointId INNER JOIN  `routetrip` AS rt ON ts.tripid = rt.tripId INNER JOIN schoolroute sr ON sr.id = rt.routeid WHERE sr.vin = :vin";
			}
			Query query = emStudent.createNativeQuery(sql);
			query.setParameter("vin", vinGeo);
			LOGGER.debug("Before Execute Query::" + query);
			List<Object[]> obj = query.getResultList();
			LOGGER.debug("After Execute Query" + query);
			LOGGER.info("SKTAlertsEJB::getStudentGeoConfig::Leaving from this method successfully");
			List<StudentGeozone> StudentGeozones = new ArrayList<StudentGeozone>();
			for (Object[] objects : obj) {
				StudentGeozone studentGeozone = new StudentGeozone();
				studentGeozone.setId((Integer) objects[0]);
				studentGeozone.setZonename(objects[1].toString());
				studentGeozone.setZoneshape(objects[2].toString());
				studentGeozone.setZonelatlng(objects[3].toString());
				studentGeozone.setCompanyId(objects[4].toString());
				studentGeozone.setBranchId(objects[5].toString());
				StudentGeozones.add(studentGeozone);
			}
			return StudentGeozones;
		} catch (Exception e) {
			LOGGER.error("SKTAlertsEJB::getStudentGeoConfig::Error Occured in Vehicle has Geo="
					+ e);
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<StudentGeozone> getStudentGeozones(String vin) {
		// TODO Auto-generated method stub
		LOGGER.info("SKTAlertsEJB::getStudentGeozones::Entered into this method"
				+ "Vin" + vin);
		List<StudentGeozone> StudentGeozones = new ArrayList<StudentGeozone>();
		try {
			Query query = emStudent
					.createQuery("SELECT sr FROM Schoolroute sr WHERE sr.vin = :vin");
			query.setParameter("vin", vin);
			// query.setCacheable(true);

			List<Schoolroute> objects = (List<Schoolroute>) query
					.getResultList();
			if (!objects.isEmpty() && objects.size() != 0) {
				Schoolroute schoolroute = objects.get(0);
				Set<Routetrip> listoftrips = schoolroute.getRoutetrips();
				List<Routetrip> routetrips = new ArrayList<Routetrip>();
				routetrips.addAll(listoftrips);
				LOGGER.info("SKTLog : SKTAlertsEJB : getSKTTripStops routetrips size===="
						+ routetrips.size());
				if (routetrips.size() != 0) {
					for (Routetrip singleRouteTrip : routetrips) {
						Set<Tripstops> tripstopses = singleRouteTrip
								.getTripstopses();
						List<Tripstops> tripstops = new ArrayList<Tripstops>();
						tripstops.addAll(tripstopses);
						if (tripstops.size() != 0) {
							for (Tripstops trstop : tripstops) {
								Query query2 = emStudent
										.createQuery("select v from StudentGeozone v where v.tripstops.stopPointId= :stopid");
								query2.setParameter("stopid",
										trstop.getStopPointId());
								// query2.setMaxResults(1);
								LOGGER.debug("Before Execute Query::" + query2);
								// query2.setCacheable(true);
								List<StudentGeozone> objects1 = (List<StudentGeozone>) query2
										.getResultList();
								if (!objects1.isEmpty() && objects1.size() != 0) {
									StudentGeozone studentGeozone = objects1
											.get(0);
									StudentGeozones.add(studentGeozone);
								}
							}
						}
					}
				}
			}
			return StudentGeozones;
		} catch (Exception e) {
			LOGGER.error("SKTAlertsEJB::getStudentGeozones::Error Occured in Vehicle has Geo="
					+ e);
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Studentdetails> getStudentsDetails(String zoneid,
			Routetrip routetrip) {
		// TODO Auto-generated method stub
		List<Studentdetails> studentdetailses = new ArrayList<Studentdetails>();
		try {
			LOGGER.info("SKTAlertsEJB::getStudentsDetails:: zoneid=" + zoneid);
			Set<Tripstops> tripstopses = routetrip.getTripstopses();
			List<Tripstops> tripstops = new ArrayList<Tripstops>();
			tripstops.addAll(tripstopses);
			if (tripstops.size() != 0) {
				for (Tripstops trstop : tripstops) {
					Query query = em
							.createQuery("select pro FROM Studentdetails pro WHERE pro.studentGeozone.id=:zoneid and (pro.tripstopsByPickupid.stopPointId=:stopId or pro.tripstopsByDropid.stopPointId=:stopId)");
					query.setParameter("zoneid", Integer.valueOf(zoneid));
					query.setParameter("stopId", trstop.getStopPointId());
					// query.setCacheable(true);
					List<Studentdetails> studentdetailse = query
							.getResultList();
					studentdetailses.addAll(studentdetailse);
				}
			}
			return studentdetailses;
		} catch (Exception e) {
			LOGGER.error("SKTAlertsEJB::getStudentsDetails::Error Occured in Studentdetails="
					+ e);
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Studentdetails> getStudentDetailsByStop(Long stopPointId) {
		// TODO Auto-generated method stub
		try {
			Query query = em
					.createQuery("select pro FROM Studentdetails pro WHERE pro.tripstopsByDropid.stopPointId =:stopPointId OR pro.tripstopsByPickupid.stopPointId =:stopPointId");
			query.setParameter("stopPointId", stopPointId);
			// query.setCacheable(true);
			return query.getResultList();
		} catch (Exception e) {
			LOGGER.error("SKTAlertsEJB::getStudentDetailsByStop::Error Occured in Studentdetails="
					+ e);
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Schoolroute insertRoute(Schoolroute schoolroute) {
		// TODO Auto-generated method stub
		try {
			emStudent.persist(schoolroute);
			return schoolroute;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Routetrip insertTrip(Routetrip routetrip, String mode) {
		try {
			if (mode.equalsIgnoreCase("insert"))
				emStudent.persist(routetrip);
			else
				emStudent.merge(routetrip);
			return routetrip;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Tripstops insertStop(Tripstops tripStop) {
		// TODO Auto-generated method stub
		try {
			emStudent.persist(tripStop);
			return tripStop;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public StudentGeozone insertGeozone(StudentGeozone studentGeozone) {
		// TODO Auto-generated method stub
		try {
			emStudent.persist(studentGeozone);
			return studentGeozone;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void updateStudentDetails(Studentdetails studentdetails) {
		// TODO Auto-generated method stub
		try {
			emStudent.merge(studentdetails);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Tripstops getTripStopByLatLng(Routetrip routeTrip,
			Studentevent studentevent) {
		// TODO Auto-generated method stub
		String latlng = studentevent.getLatitude() + ","
				+ studentevent.getLongitude();
		try {
			Query query = emStudent
					.createQuery("SELECT ts FROM Tripstops ts WHERE ts.latlng='"
							+ latlng
							+ "' and ts.routetrip.tripid="
							+ routeTrip.getTripid() + "");
			List<Tripstops> objects = (List<Tripstops>) query.getResultList();
			if (!objects.isEmpty() && objects.size() != 0) {
				return objects.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public StudentGeozone getGeozoneByStopId(Long stopPointId) {
		// TODO Auto-generated method stub
		try {
			Query query = emStudent
					.createQuery("SELECT sg FROM StudentGeozone sg WHERE sg.tripstops.stopPointId="
							+ stopPointId + "");
			// query.setCacheable(true);
			List<StudentGeozone> objects = (List<StudentGeozone>) query
					.getResultList();
			if (!objects.isEmpty() && objects.size() != 0) {
				return objects.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean isTripOver(Routetrip routeTrip) {
		// TODO Auto-generated method stub
		String alerttype = "BES";
		try {
			Query query = emStudent
					.createQuery("SELECT ae FROM Alertevents ae WHERE ae.tripid='"
							+ routeTrip.getTripid()
							+ "' and ae.alerttypes.alerttype='"
							+ alerttype
							+ "' and Date(eventTimeStamp)=curdate()");
			// query.setCacheable(true);
			List<Alertevents> objects = (List<Alertevents>) query
					.getResultList();
			if (!objects.isEmpty() && objects.size() != 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Routetrip getDropTrip(String vin) {
		// TODO Auto-generated method stub
		try {
			Query query = emStudent
					.createQuery("SELECT vh FROM  Schoolroute vh WHERE vh.vin = '"
							+ vin + "'");
			List<Schoolroute> vehicleHasRoutes = query.getResultList();
			if (vehicleHasRoutes.size() != 0 && !vehicleHasRoutes.isEmpty()) {
				List<Routetrip> routetrips = new ArrayList<Routetrip>();
				routetrips.addAll(vehicleHasRoutes.get(0).getRoutetrips());
				Collections.sort(routetrips, new Comparator<Routetrip>() {
					public int compare(Routetrip o1, Routetrip o2) {
						if (o1.getTripStarttime() == o2.getTripStarttime())
							return 0;
						return o1.getTripStarttime().before(
								o2.getTripStarttime()) ? -1 : 1;
					}
				});
				for (Routetrip singlevehicleTrip : routetrips) {
					if (singlevehicleTrip.getType().equalsIgnoreCase("Drop")) {
						return singlevehicleTrip;
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Studentdetails> getStudentDetailsByVin(String vin) {
		try {
			Query query = em
					.createQuery("SELECT stud FROM Studentdetails stud WHERE stud.tripstopsByPickupid.routetrip.schoolroute.vin=:Vin");
			query.setParameter("Vin", vin);
			return query.getResultList();
		} catch (Exception e) {
			LOGGER.error("SKTAlertsEJB::getStudentDetailsByStop::Error Occured in Studentdetails="
					+ e);
			e.printStackTrace();
			return null;
		}
	}
}