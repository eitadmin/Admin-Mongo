package com.eiw.cron.archival;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.logging.Logger;

import com.eiw.client.dto.RamcoConstants;
import com.eiw.client.dto.VehicleEventPushData;
import com.eiw.client.dto.VehiclePushData;
import com.eiw.server.MiscHttpRequest;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.archivalpu.Archivalvehicleevent;
import com.eiw.server.archivalpu.ArchivalvehicleeventId;
import com.eiw.server.fleettrackingpu.GeocodeAddress;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.Vehicleevent;
import com.eiw.server.fleettrackingpu.VehicleeventId;

@Stateless
public class ArchivalEJB implements ArchivalEJBRemote {

	@PersistenceContext(unitName = "ltmsfleettrackingpu")
	protected EntityManager em;
	@PersistenceContext(unitName = "archivepu")
	private EntityManager emArchive;
	private static final Logger LOGGER = Logger.getLogger("archival");
	private static final String STR_YYMMDD = "yyyy-MM-dd",
			STR_YYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss", STR_FROM_DT = "fromDt",
			STR_TO_DT = "toDate", STR_FROM_HR = "00:00:00",
			STR_TO_HR = "23:59:59";
	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	static Map<String, String> GEO_ADDRESS = new TreeMap<String, String>();

	public long numberOfRecords(String timeZone) {
		LOGGER.info("ArchivalEJB::numberOfRecords::" + "Timezone" + timeZone);
		long records = 0;
		try {
			Calendar calendar = TimeZoneUtil.getDateForTimeZones(new Date(),
					timeZone);
			calendar.add(calendar.MONTH, -2);
			Date archivalDate = calendar.getTime();
			String dateNew = TimeZoneUtil.getDate(archivalDate);
			Date dateNew1 = TimeZoneUtil.getDate(dateNew);

			System.out.println("archivalDate---" + archivalDate);
			System.out.println("dateNew-----" + dateNew);
			System.out.println("dateNew1-----" + dateNew1);

			LOGGER.debug("ArchivalEJB Before Query Execution");
			String strPollSkyDB = "SELECT COUNT(vin) FROM Vehicleevent evt WHERE DATE(eventTimeStamp) < :date";
			Query qPollSkyDB = em.createQuery(strPollSkyDB);
			qPollSkyDB.setParameter("date", dateNew1);
			records = (Long) qPollSkyDB.getSingleResult();
			LOGGER.info("ArchivalEJB After Query Execution : records = "
					+ records);
		} catch (Exception e) {
			LOGGER.error("ArchivalEJB::numberOfRecords::ArchivalEJB Exception"
					+ e);
		}
		return records;
	}

	@Override
	public void startArchiveProcess(String timeZone) {
		// new Archival(em, timeZone);

		// this.em = em1;
		Calendar calendar = TimeZoneUtil.getDateForTimeZones(new Date(),
				timeZone);
		calendar.add(calendar.MONTH, -2);
		Date archivalDate = calendar.getTime();
		String dateNew = TimeZoneUtil.getDate(archivalDate);
		Date dateNew1 = TimeZoneUtil.getDate(dateNew);
		try {
			int maxResults = 80000;
			LOGGER.info("Before Query Execution Archival maxResults "
					+ maxResults);
			String strPollSkyDB = "SELECT evt from Vehicleevent evt where date(eventTimeStamp) < :date";
			Query qPollSkyDB = em.createQuery(strPollSkyDB);
			qPollSkyDB.setParameter("date", dateNew1);
			qPollSkyDB.setFirstResult(0);
			qPollSkyDB.setMaxResults(maxResults);
			List<Vehicleevent> resultList = (List<Vehicleevent>) qPollSkyDB
					.getResultList();
			LOGGER.info("After Query Execution Archival maxResults "
					+ maxResults);
			maxResults = 0;
			for (Vehicleevent ve : resultList) {
				boolean isPersist;
				try {
					isPersist = true;
					Archivalvehicleevent archivalvehicleevent = new Archivalvehicleevent();
					ArchivalvehicleeventId archivalvehicleeventId = new ArchivalvehicleeventId();
					archivalvehicleevent.setArchivalBy("System");
					archivalvehicleevent.setArchivalDt(new Date());
					archivalvehicleevent.setSkyid(ve.getSkyid());
					archivalvehicleeventId.setVin(ve.getId().getVin());
					archivalvehicleeventId.setEventTimeStamp(ve.getId()
							.getEventTimeStamp());
					archivalvehicleevent.setServerTimeStamp(ve
							.getServerTimeStamp());
					archivalvehicleevent.setLatitude(ve.getLatitude());
					archivalvehicleevent.setLongitude(ve.getLongitude());
					archivalvehicleevent.setSpeed(ve.getSpeed());
					archivalvehicleevent.setPriority(ve.getPriority());
					archivalvehicleevent.setIoevent(ve.getIoevent());
					archivalvehicleevent.setBytesTrx(ve.getBytesTrx());
					archivalvehicleevent.setEngine(ve.getEngine());
					archivalvehicleevent.setTempSensor1(ve.getTempSensor1());
					archivalvehicleevent.setTempSensor2(ve.getTempSensor2());
					archivalvehicleevent.setTempSensor3(ve.getTempSensor3());
					archivalvehicleevent.setOdometer(ve.getOdometer());
					archivalvehicleevent.setBattery(ve.getBattery());
					archivalvehicleevent.setEventSource(ve.getEventSource());
					archivalvehicleevent.setAi1(ve.getAi1());
					archivalvehicleevent.setAi2(ve.getAi2());
					archivalvehicleevent.setAi3(ve.getAi3());
					archivalvehicleevent.setAi4(ve.getAi4());
					archivalvehicleevent.setDi1(ve.getDi1());
					archivalvehicleevent.setDi2(ve.getDi2());
					archivalvehicleevent.setDi3(ve.getDi3());
					archivalvehicleevent.setDi4(ve.getDi4());
					archivalvehicleevent.setId(archivalvehicleeventId);
					Archivalvehicleevent archivalvehicleeventchk = em.find(
							Archivalvehicleevent.class, archivalvehicleeventId);
					if (archivalvehicleeventchk == null) {
						em.persist(archivalvehicleevent);
						LOGGER.info("ArchivalVehicleevent Data Persisted");
					}
				} catch (Exception e) {
					LOGGER.error("Archival::Archivalvehicleevent::Exception occured"
							+ e);
					e.printStackTrace();
					isPersist = false;
					return;
				}
				if (isPersist) {
					maxResults++;
					em.remove(ve);
					em.flush();
				}
			}
			CronArchival.records -= maxResults;
		} catch (Exception e) {
			LOGGER.error("Exception in ArchivalEJB startArchiveProcess " + e);
			e.printStackTrace();
		}
	}

	@Override
	public List<String> getVins() {
		LOGGER.info("ArchivalEJB::getVins::Entered into this method");
		Query query = em.createQuery("Select v.vin from Vehicle v");
		LOGGER.info("Before Query execute");
		List<String> listOfVehicles = (List<String>) query.getResultList();
		LOGGER.info("After query execute");
		return listOfVehicles;
	}

	public int numberOfOldArchivedRecords(String timeZone) {
		LOGGER.info("ArchivalEJB::numberOfRecords::" + "Timezone" + timeZone);
		int records = 0;
		try {
			Calendar calendar = TimeZoneUtil.getDateForTimeZones(new Date(),
					timeZone);
			calendar.add(Calendar.MONTH, -12);
			Date archivalDate = calendar.getTime();
			LOGGER.debug("ArchivalEJB Before Query Execution");
			String strArchDB = "SELECT COUNT(vin) FROM Archivalvehicleevent evt WHERE DATE(eventTimeStamp) < :date";
			Query qPollSkyDB = emArchive.createQuery(strArchDB);
			qPollSkyDB.setParameter("date", archivalDate);
			LOGGER.debug("ArchivalEJB After Query Execution");
			records = ((Long) qPollSkyDB.getSingleResult()).intValue();
		} catch (Exception e) {
			LOGGER.error("ArchivalEJB::numberOfRecords::ArchivalEJB Exception"
					+ e);
		}
		return records;
	}

	@Override
	public void deleteOldArchivedRecords(String timeZone) {

		LOGGER.info("ArchivalEJB::deleteOldArchivedRecords::" + "Timezone"
				+ timeZone);
		try {
			Calendar calendar = TimeZoneUtil.getDateForTimeZones(new Date(),
					timeZone);
			calendar.add(Calendar.MONTH, -3);
			Date archivalDate = calendar.getTime();
			LOGGER.debug("ArchivalEJB.deleteOldArchivedRecords Before Query Execution");
			String archivalDelete = "DELETE FROM archivaldb.archivalvehicleevent WHERE DATE(eventTimeStamp) < :date";
			Query queryArchi = emArchive.createNativeQuery(archivalDelete);
			queryArchi.setParameter("date", archivalDate);
			queryArchi.executeUpdate();
			LOGGER.info("ArchivalEJB.deleteOldArchivedRecords After Query Execution ");
		} catch (Exception e) {
			LOGGER.error("ArchivalEJB::deleteOldArchivedRecords::ArchivalEJB Exception"
					+ e);
		}
	}

	@Override
	public String getAllRamCoBackTrackingDetails(String timeZone) {
		LOGGER.info("ArchivalEJB::getAllRamCoBackTrackingDetails::"
				+ "Timezone" + timeZone);
		String maper = null;
		try {
			Calendar calendar = TimeZoneUtil.getDateForTimeZones(new Date(),
					timeZone);
			calendar.add(Calendar.DATE, -1);
			Date archivalDate = calendar.getTime();
			String dateNew = TimeZoneUtil.getDate(archivalDate);
			String companyId = "demo";
			String branchId = "demo";
			String userId = "demo";
			Query query = null;
			List<VehicleEventPushData> vehicleDatas = null;
			VehiclePushData vehiclePushData;
			VehicleEventPushData vehicleEventPushData;
			Map<String, VehiclePushData> finalObject = new TreeMap<String, VehiclePushData>();
			try {
				query = em
						.createQuery("Select v from Vehicle v, VehicleHasUser vhu where v.vin=vhu.id.vin AND v.companyId=:companyId AND v.branchId=:branchId AND vhu.id.userId=:userId");
				query.setParameter("userId", userId);
				query.setParameter("companyId", companyId);
				query.setParameter("branchId", branchId);
				List<Vehicle> vehicles = query.getResultList();
				for (Vehicle vehicle : vehicles) {
					System.out.println("vehicle.getVin()" + vehicle.getVin());
					Query query1 = em
							.createNativeQuery("SELECT MAX(v.eventTimeStamp) FROM vehicleevent v WHERE v.vin =:vin AND DATE(v.eventTimeStamp) =:fromDt AND eventTimeStamp > CONCAT(:fromDt,' 23:55:00')");
					query1.setParameter("vin", vehicle.getVin());
					query1.setParameter("fromDt", dateNew);
					Date date = (Date) query1.getSingleResult();
					System.out.println("records" + date);
					if (date == null) {
						continue;
					}

					vehiclePushData = new VehiclePushData();
					vehiclePushData.setVehicle_id(vehicle.getVin());
					vehiclePushData.setVehicle_regno(vehicle.getPlateNo());
					vehiclePushData.setVendor("KingsTrack");

					Query queryEvent = em
							.createNativeQuery("SELECT v.eventTimeStamp,v.latitude,v.longitude,v.speed,v.odometer FROM vehicleevent v WHERE v.vin =:vin AND DATE(v.eventTimeStamp) =:fromDt ");
					queryEvent.setParameter("vin", vehicle.getVin());
					queryEvent.setParameter("fromDt", dateNew);
					List<Object[]> resultList = queryEvent.getResultList();
					vehicleDatas = new ArrayList<VehicleEventPushData>();
					for (int i = 0; i < resultList.size(); i++) {

						vehicleEventPushData = new VehicleEventPushData();
						Object[] row = (Object[]) resultList.get(i);
						vehicleEventPushData
								.setVehicle_gpsDateTime((Date) row[0]);
						vehicleEventPushData.setVehicle_location("Chennai");
						vehicleEventPushData.setVehicle_latitude(Float
								.valueOf(row[1].toString()));
						vehicleEventPushData.setVehicle_longitude(Float
								.valueOf(row[2].toString()));
						vehicleEventPushData.setVehicle_speed(Integer
								.parseInt(row[3].toString()));
						vehicleEventPushData
								.setVehicle_distance(row[4] == null ? 0 : Long
										.valueOf(row[4].toString()));
						vehicleDatas.add(vehicleEventPushData);
					}
					vehiclePushData.setVehicleEventPushData(vehicleDatas);
					finalObject.put(vehicle.getVin(), vehiclePushData);
				}
				ObjectMapper obj = new ObjectMapper();
				maper = obj.writeValueAsString(finalObject);
				System.out.println("---------------------------" + maper);
				ObjectMapper mapper = new ObjectMapper();
				mapper.writeValue(new File("c:\\temp\\user.json"), maper);
			} catch (Exception e) {
				LOGGER.error("getVehicleDetails: " + e);
			}

		} catch (Exception e) {
			LOGGER.error("ArchivalEJB::deleteOldArchivedRecords::ArchivalEJB Exception"
					+ e);
		}
		return maper;
	}

	@Override
	public String getAllRamCoBackTrackingDetailsNew(String timeZone) {
		LOGGER.info("ArchivalEJB::getAllRamCoBackTrackingDetails::"
				+ "Timezone" + timeZone);
		String maper = null;
		try {
			Calendar calendar = TimeZoneUtil.getDateForTimeZones(new Date(),
					timeZone);
			calendar.add(Calendar.DATE, -1);
			Date archivalDate = calendar.getTime();
			String dateNew = TimeZoneUtil.getDate(archivalDate);
			String companyId = "demo";
			String branchId = "demo";
			String userId = "demo";
			Query query = null;
			List<VehicleEventPushData> vehicleDatas = null;
			VehicleEventPushData vehicleEventPushData;
			try {
				// Provider Table RamCo =5
				query = em
						.createNativeQuery("SELECT ve.vin,ve.plateNo FROM fleettrackingdb.vehicle ve LEFT OUTER JOIN ltmscompanyadmindb.company com ON ve.companyId=com.companyId WHERE com.suffix='5' AND ve.status IS NULL AND active='1'");
				// query = em
				// .createQuery("Select v from Vehicle v, VehicleHasUser vhu where v.vin=vhu.id.vin AND v.companyId=:companyId AND v.branchId=:branchId AND vhu.id.userId=:userId");
				// query.setParameter("userId", userId);
				// query.setParameter("companyId", companyId);
				// query.setParameter("branchId", branchId);
				List<Object[]> vehicleResultList = query.getResultList();
				fetchGeocodeAddress();
				vehicleDatas = new ArrayList<VehicleEventPushData>();
				for (int i = 0; i < vehicleResultList.size(); i++) {

					Object[] vehicleRow = (Object[]) vehicleResultList.get(i);
					// for (Vehicle vehicle : vehicles) {
					System.out.println("vehicle.getVin()"
							+ (String) vehicleRow[0]);
					Query query1 = em
							.createNativeQuery("SELECT MAX(v.eventTimeStamp) FROM vehicleevent v WHERE v.vin =:vin AND DATE(v.eventTimeStamp) =:fromDt AND eventTimeStamp > CONCAT(:fromDt,' 23:55:00')");
					query1.setParameter("vin", (String) vehicleRow[0]);
					query1.setParameter("fromDt", dateNew);
					Date date = (Date) query1.getSingleResult();
					System.out.println("records" + date);
					if (date == null) {
						continue;
					}
					Query queryEvent = em
							.createNativeQuery("SELECT v.eventTimeStamp,v.latitude,v.longitude,v.speed,v.odometer FROM vehicleevent v WHERE v.vin =:vin AND DATE(v.eventTimeStamp) =:fromDt ");
					queryEvent.setParameter("vin", (String) vehicleRow[0]);
					queryEvent.setParameter("fromDt", dateNew);
					List<Object[]> resultList = queryEvent.getResultList();

					for (int j = 0; j < resultList.size(); j++) {
						vehicleEventPushData = new VehicleEventPushData();
						Object[] row = (Object[]) resultList.get(j);

						vehicleEventPushData
								.setVehicle_id((String) vehicleRow[0]);
						vehicleEventPushData
								.setVehicle_regno((String) vehicleRow[1]);
						vehicleEventPushData
								.setVehicle_alias((String) vehicleRow[1]);
						vehicleEventPushData.setVendor("KingsTrack");

						vehicleEventPushData
								.setVehicle_gpsDateTime((Date) row[0]);

						vehicleEventPushData.setVehicle_location("");
						String lat = row[1].toString();
						String lng = row[2].toString();
						String formattedAddress = getAddressFromLatlng(lat, lng);
						vehicleEventPushData
								.setVehicle_location(formattedAddress);

						vehicleEventPushData.setVehicle_latitude(Float
								.valueOf(row[1].toString()));
						vehicleEventPushData.setVehicle_longitude(Float
								.valueOf(row[2].toString()));
						vehicleEventPushData.setVehicle_speed(Integer
								.parseInt(row[3].toString()));
						vehicleEventPushData
								.setVehicle_distance(row[4] == null ? 0 : Long
										.valueOf(row[4].toString()) / 1000);
						vehicleDatas.add(vehicleEventPushData);
					}

				}
				String status = addRamcoServer(vehicleDatas);
				System.out.println("status" + status);
			} catch (Exception e) {
				LOGGER.error("getVehicleDetails: " + e);
			}

		} catch (Exception e) {
			LOGGER.error("ArchivalEJB::getAllRamCoBackTrackingDetailsNew::ArchivalEJB Exception"
					+ e);
		}
		return maper;
	}

	private void fetchGeocodeAddress() {
		Query query1 = em.createQuery("SELECT gd FROM GeocodeAddress gd");
		List<GeocodeAddress> geocodeAddresses = query1.getResultList();
		for (GeocodeAddress geocodeAddress : geocodeAddresses) {
			GEO_ADDRESS.put(geocodeAddress.getLatlng(),
					geocodeAddress.getAddress());
		}
	}

	private String getAddressFromLatlng(String latitude, String longitude)
			throws Exception {

		String latlng = latitude + "," + longitude;
		String formattedAddressStop;
		if (GEO_ADDRESS.containsKey(latlng)) {
			formattedAddressStop = GEO_ADDRESS.get(latlng);
		} else {
			formattedAddressStop = MiscHttpRequest.invokeGoecoder(latitude,
					longitude);
			if (formattedAddressStop.equalsIgnoreCase("OVER_QUERY_LIMIT")) {
				Thread.sleep(100);
				formattedAddressStop = MiscHttpRequest.invokeGoecoder(latitude,
						longitude);
				formattedAddressStop = formattedAddressStop
						.equalsIgnoreCase("OVER_QUERY_LIMIT") ? ""
						: formattedAddressStop;
				if (!formattedAddressStop.equalsIgnoreCase("OVER_QUERY_LIMIT")) {
					insertIntoGeocodeAddress(latlng, formattedAddressStop);
				}
			} else {
				insertIntoGeocodeAddress(latlng, formattedAddressStop);
			}
		}
		return formattedAddressStop;

	}

	private void insertIntoGeocodeAddress(String latlng, String formattedAddress) {
		if (formattedAddress != null && !formattedAddress.equalsIgnoreCase("")) {
			GeocodeAddress gcdAddr = new GeocodeAddress();
			gcdAddr.setLatlng(latlng);
			gcdAddr.setAddress(formattedAddress.trim());
			gcdAddr.setCreatedDate(TimeZoneUtil.getDateTimeZone(new Date(),
					"Asia/Riyadh"));
			em.persist(gcdAddr);
			GEO_ADDRESS.put(latlng, formattedAddress);
		}

	}

	private String addRamcoServer(List<VehicleEventPushData> vehicleDatas)
			throws SQLException {
		String status = "failed";
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName(RamcoConstants.driverName);
			con = DriverManager.getConnection(RamcoConstants.url);

			for (int i = 0; i < vehicleDatas.size(); i++) {

				String queryNew = "INSERT INTO Kings_History_Push (vehicle_id,vehicle_rto_no,vehicle_alias,vendor,vehicle_location,vehicle_gpsdatetime,vehicle_lattitude,vehicle_longitude,vehicle_speed,vehicle_distance) values(?, ?, ?,?, ?, ?,?, ?, ?,?)";

				pstmt = con.prepareStatement(queryNew); // create a statement
				pstmt.setString(1, vehicleDatas.get(i).getVehicle_id());
				pstmt.setString(2, vehicleDatas.get(i).getVehicle_regno());
				pstmt.setString(3, vehicleDatas.get(i).getVehicle_alias());
				pstmt.setString(4, vehicleDatas.get(i).getVendor());
				pstmt.setString(5, vehicleDatas.get(i).getVehicle_location());
				pstmt.setDate(6, (java.sql.Date) vehicleDatas.get(i)
						.getVehicle_gpsDateTime());
				pstmt.setFloat(7, vehicleDatas.get(i).getVehicle_latitude());
				pstmt.setFloat(8, vehicleDatas.get(i).getVehicle_longitude());
				pstmt.setInt(9, vehicleDatas.get(i).getVehicle_speed());
				pstmt.setLong(10, vehicleDatas.get(i).getVehicle_distance());
				pstmt.execute();

				status = "sucess";

			}
		} catch (Exception e) {
			System.out.println("e------------" + e);
		} finally {
			pstmt.close();
			con.close();
			LOGGER.error("Database connection terminated");
			System.out.println("Database connection terminated");

		}
		return status;
	}
}
