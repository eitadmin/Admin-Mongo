package com.eiw.admin.ejb;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.websocket.CloseReason;
import javax.websocket.Session;

import org.apache.commons.io.FileUtils;
import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eiw.client.dto.CompanyDataAdmin;
import com.eiw.client.dto.DashboardContentPerCB;
import com.eiw.client.dto.OperatorData;
import com.eiw.client.dto.ReportData;
import com.eiw.client.dto.VehicleData;
import com.eiw.client.dto.VehicleIMEIDto;
import com.eiw.device.ejb.FleetTrackingDeviceListenerBO;
import com.eiw.device.ejb.FleetTrackingDeviceListenerBORemote;
import com.eiw.device.simulator.SimulatorStarter;
import com.eiw.server.OSValidator;
import com.eiw.server.SMSSendHttpClient;
import com.eiw.server.TimeZoneUtil;
import com.eiw.server.companyadminpu.Address;
import com.eiw.server.companyadminpu.Applicationsettings;
import com.eiw.server.companyadminpu.Company;
import com.eiw.server.companyadminpu.CompanyHasFeatures;
import com.eiw.server.companyadminpu.CompanyHasFeaturesId;
import com.eiw.server.companyadminpu.Companybranch;
import com.eiw.server.companyadminpu.CompanybranchId;
import com.eiw.server.companyadminpu.Companybranchuser;
import com.eiw.server.companyadminpu.CompanybranchuserId;
import com.eiw.server.companyadminpu.Companybranchusercompanyrole;
import com.eiw.server.companyadminpu.CompanybranchusercompanyroleId;
import com.eiw.server.companyadminpu.Country;
import com.eiw.server.companyadminpu.Errorlog;
import com.eiw.server.companyadminpu.Features;
import com.eiw.server.companyadminpu.FeaturesId;
import com.eiw.server.companyadminpu.Provider;
import com.eiw.server.companyadminpu.Role;
import com.eiw.server.companyadminpu.Smsconfig;
import com.eiw.server.companyadminpu.SmsconfigId;
import com.eiw.server.companyadminpu.Smshistory;
import com.eiw.server.companyadminpu.SmshistoryId;
import com.eiw.server.companyadminpu.User;
import com.eiw.server.companyadminpu.UserId;
import com.eiw.server.companyadminpu.Userlogin;
import com.eiw.server.fleettrackingpu.Operator;
import com.eiw.server.fleettrackingpu.Pushtotalk;
import com.eiw.server.fleettrackingpu.TicketInfo;
import com.eiw.server.fleettrackingpu.TicketSupervisor;
import com.eiw.server.fleettrackingpu.TicketWorkshop;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.VehicleHasOperator;
import com.eiw.server.fleettrackingpu.VehicleHasOperatorId;
import com.eiw.server.fleettrackingpu.VehicleHasOperatorevents;
import com.eiw.server.fleettrackingpu.VehicleHasOperatoreventsId;
import com.eiw.server.fleettrackingpu.VehicleHasUser;
import com.eiw.server.fleettrackingpu.Vehiclecompletesummary;
import com.eiw.server.fleettrackingpu.VehiclecompletesummaryId;
import com.eiw.server.fleettrackingpu.Vehicleevent;

@Stateless
public class EMSAdminPortalImpl implements EMSAdminPortal {

	@PersistenceContext(unitName = "ltmsfleettrackingpu")
	private EntityManager em;

	@PersistenceContext(unitName = "ltmscompanyadminpu")
	private EntityManager emAdmin;

	private static Map<String, String> keyMap = new HashMap<String, String>();

	private static final Logger LOGGER = Logger.getLogger("admin");
	private static final String CORP_ID = "corpId";
	private static final String STR_FROM_DATE = "fromDate",
			STR_TO_DATE = "toDate", STR_COMP_ADMIN = "CompanyAdmin",
			STR_CORP_ID = "corpId";
	private static final String STR_PERSISTED = "persisted",
			STR_TO_HOUR = "23:59:59", STR_FROM_HOUR = "00:00:00";
	private static final String QRY_COMP = "SELECT c FROM Company c";
	private static final String SUFFIX = "suffix";

	@EJB
	private SimulatorStarter simulator;

	@EJB
	private FleetTrackingDeviceListenerBORemote fleetTrackingDeviceListnerBoRemote;

	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public DashboardContentPerCB authenticateUserAdmin(String corpId,
			String name, String pass) {
		LOGGER.info("EMSAdminPortalImpl::authenticateUserAdmin::Entered into this method"
				+ STR_CORP_ID + corpId + "name" + name + "pass" + pass);
		DashboardContentPerCB dbContentPerCB = new DashboardContentPerCB();
		Map<String, Boolean> isMiniHashMap = new HashMap<String, Boolean>();

		Map<String, String> ProviderHashMap = new HashMap<String, String>();

		Map<String, Boolean> isWftSspHashMap = new HashMap<String, Boolean>();
		try {
			String sqlforUser = "SELECT a FROM User a where a.id.companyCompanyId = :corpId and a.id.emailAddress = :name and BINARY(a.userPasswd)= :pass";

			Query query1 = emAdmin.createQuery(sqlforUser);
			query1.setParameter(STR_CORP_ID, corpId);
			query1.setParameter("name", name);
			query1.setParameter("pass", pass);
			LOGGER.info("Before Execute query" + query1);
			User user = (User) query1.getSingleResult();
			dbContentPerCB.setSuffix(user.getCompany().getSuffix());
			dbContentPerCB.setIsMiniApps(user.getCompany().getIsMiniApps());
			LOGGER.info("After Execute Query" + query1);
		} catch (Exception e) {
			LOGGER.error("EMSAdminPortalImpl::authenticateUserAdmin::User::Exception Occured"
					+ e);
			System.out
					.println("EMSAdminPortalImpl::authenticateUserAdmin::User::Exception Occured=="
							+ e);
			return null;
		}
		try {
			String sql = "SELECT cbu FROM Companybranchusercompanyrole cbu WHERE cbu.id.companybranchuserUserEmailAddress = :name and cbu.id.companybranchuserUserCompanyCompanyId= :corpId";
			Query query = emAdmin.createQuery(sql);
			query.setParameter("name", name);
			query.setParameter(STR_CORP_ID, corpId);
			LOGGER.info("Before Execute Query" + query);
			Companybranchusercompanyrole cbu = (Companybranchusercompanyrole) query
					.getSingleResult();
			LOGGER.info("After Execute Query" + query);

			Role companyRole = cbu.getRole();

			String companyId = cbu.getId().getCompanybranchuserCbCompanyId();
			String branchId = cbu.getId().getCompanybranchuserCbBranchId();
			String userId = cbu.getCompanybranchuser().getUser().getId()
					.getEmailAddress();

			dbContentPerCB.setCompanyID(companyId);
			dbContentPerCB.setCompanyName(cbu.getCompanybranchuser()
					.getCompanybranch().getCompany().getCompanyName());
			dbContentPerCB.setBranchID(branchId);
			dbContentPerCB.setBranchName(cbu.getCompanybranchuser()
					.getCompanybranch().getBranchName());

			dbContentPerCB.setCompanyRole(companyRole.getRoleName());
			dbContentPerCB.setContactNo(cbu.getCompanybranchuser().getUser()
					.getContactNo());

			dbContentPerCB.setUserImage(cbu.getCompanybranchuser().getUser()
					.getImgUrl());
			String ourlogo = cbu.getCompanybranchuser().getCompanybranch()
					.getCompany().getSuffix();
			if (ourlogo != null) {
				Provider pro = em
						.find(Provider.class, Integer.valueOf(ourlogo));
				dbContentPerCB.setOurLogoUrl(pro.getLogoUrl());
				dbContentPerCB.setBucketName(pro.getBucketName());
			}
			try {
				// Get user preference
				Map<String, String> hashMap = new HashMap<String, String>();
				String userPrefQuery = "SELECT CONCAT(TYPE,'=',NAME) AS userpref,id FROM fleettrackingdb.companybranchuserpreferences WHERE companyId = '"
						+ companyId
						+ "' AND branchId='"
						+ branchId
						+ "' AND userId='" + userId + "'";
				Query query2 = em.createNativeQuery(userPrefQuery);
				LOGGER.info("Before Execute Query" + userPrefQuery);
				List<Object[]> userPreferences = (List<Object[]>) query2
						.getResultList();
				LOGGER.info("After Execute Query" + userPrefQuery);
				for (int i = 0; i < userPreferences.size(); i++) {
					Object[] row = (Object[]) userPreferences.get(i);

					String preference = row[0].toString();
					String[] userPref = preference.split("=");
					hashMap.put(userPref[0], userPref[1]);

					dbContentPerCB.setHashMap(hashMap);

				}
			} catch (Exception e) {
				LOGGER.error("EMSAdminPortalImpl::authenticateUserAdmin::fleettrackingdb.companybranchuserpreferences::Exception Occured"
						+ e);
			}

			try {
				// Get company features
				String compFeaturesQuery = "SELECT chf FROM CompanyHasFeatures chf WHERE id.companyCompanyId = '"
						+ corpId + "'";
				Query query2 = emAdmin.createQuery(compFeaturesQuery);
				LOGGER.info("before Execute  Query" + query2);
				List<CompanyHasFeatures> compFeatures = (List<CompanyHasFeatures>) query2
						.getResultList();
				List<CompanyHasFeatures> compFeaturesName = (List<CompanyHasFeatures>) query2
						.getResultList();
				LOGGER.info("After Execute Query" + query2);
				String feature = null;
				boolean isFirst = true;
				SortedMap<String, List<String>> hashMapFeatures = new TreeMap<String, List<String>>();
				for (CompanyHasFeatures companyHasFeature : compFeatures) {

					List<String> listOfFeatures = new ArrayList<String>();
					if (isFirst) {
						feature = companyHasFeature.getId()
								.getFeaturesFeature();
						for (CompanyHasFeatures companyHasFeature1 : compFeaturesName) {
							if (companyHasFeature1.getId().getFeaturesFeature()
									.equalsIgnoreCase(feature)) {
								listOfFeatures.add(companyHasFeature1.getId()
										.getFeaturesFeatureName());
							}
						}
						hashMapFeatures.put(feature, listOfFeatures);
						isFirst = false;
					} else if (!(feature.equalsIgnoreCase(companyHasFeature
							.getId().getFeaturesFeature()))) {
						feature = companyHasFeature.getId()
								.getFeaturesFeature();
						for (CompanyHasFeatures companyHasFeature1 : compFeaturesName) {
							if (companyHasFeature1.getId().getFeaturesFeature()
									.equalsIgnoreCase(feature)) {
								listOfFeatures.add(companyHasFeature1.getId()
										.getFeaturesFeatureName());
							}
						}
						hashMapFeatures.put(feature, listOfFeatures);

					}
				}
				dbContentPerCB.setCompFeatures(hashMapFeatures);
				String selectQuery = "select companySetting from ltmscompanyadmindb.company where companyId = '"
						+ corpId + "'";
				Query query1 = emAdmin.createNativeQuery(selectQuery);
				LOGGER.info("Before Execute Query" + query1);
				String result = (String) query1.getSingleResult();
				LOGGER.info("After Execute Query" + query1);
				dbContentPerCB.setCompanyLogoUrl(result);
			} catch (Exception e) {
				LOGGER.error("EMSAdminPortalImpl::authenticateUserAdmin::CompanyHasFeatures::Exception Occured"
						+ e);
			}

			// capture userlogin date/time

			try {
				Userlogin userlogin = new Userlogin();
				userlogin.setCompanyId(companyId);
				userlogin.setBranchId(branchId);
				userlogin.setUserId(userId);
				userlogin.setRole(companyRole.getRoleName());
				userlogin.setLoginTime(TimeZoneUtil.getDateInTimeZone());
				userlogin.setLastUpdatedBy(userId);
				userlogin.setLastUpdatedDate(TimeZoneUtil.getDateInTimeZone());
				// Find lastlogindate
				try {
					String getLastLogin = "SELECT MAX(id) FROM Userlogin WHERE companyId = '"
							+ corpId
							+ "' AND branchId = '"
							+ branchId
							+ "' AND userId = '" + name + "'";
					Query lastLoginQuery = emAdmin.createQuery(getLastLogin);
					LOGGER.info("before Execute Query" + getLastLogin);
					long loginId = (Long) lastLoginQuery.getSingleResult();
					LOGGER.info("After Execute Query" + getLastLogin);
					Userlogin userlogin2 = emAdmin.find(Userlogin.class,
							loginId);
					if (userlogin2.getLogoutTime() == null) {
						userlogin2.setLogoutTime(TimeZoneUtil
								.getDateInTimeZone());
					}
					dbContentPerCB.setLastLoginTime(TimeZoneUtil
							.getStrTZ(userlogin2.getLoginTime()));
				} catch (Exception e) {
					LOGGER.error("EMSAdminPortalImpl::authenticateUserAdmin::Userlogin::LastLoginNotFound"
							+ e);
				}

				emAdmin.persist(userlogin);
				dbContentPerCB
						.setUserLoginId(String.valueOf(userlogin.getId()));
			} catch (Exception e) {
				LOGGER.error("EMSAdminPortalImpl::authenticateUserAdmin::loginId"
						+ e);
			}

			// get Mini Apps Companies

			try {
				String miniSql = "SELECT companyId,isMiniApps FROM company c WHERE c.suffix=:suffix AND `isMiniApps`='1' ";
				Query queryForMini = emAdmin.createNativeQuery(miniSql);
				queryForMini.setParameter(SUFFIX, dbContentPerCB.getSuffix());
				List<Object[]> resultList = (List<Object[]>) queryForMini
						.getResultList();
				LOGGER.info("After Execute Query" + query);
				for (int i = 0; i < resultList.size(); i++) {
					Object[] obj = (Object[]) resultList.get(i);
					System.out.println(obj[0]);
					System.out.println(obj[1]);
					isMiniHashMap.put(obj[0].toString(), true);
				}
				dbContentPerCB.setIsMiniHashMap(isMiniHashMap);

				// get Wft/Ssp Companies
				String wftSql = "SELECT companyId,isSspWft FROM company c WHERE c.suffix=:suffix AND isSspWft='1' ";
				Query queryForwft = emAdmin.createNativeQuery(wftSql);
				queryForwft.setParameter(SUFFIX, dbContentPerCB.getSuffix());
				List<Object[]> wftResultList = (List<Object[]>) queryForwft
						.getResultList();
				for (int i = 0; i < wftResultList.size(); i++) {
					Object[] obj = (Object[]) wftResultList.get(i);
					isWftSspHashMap.put(obj[0].toString(), true);
				}
				dbContentPerCB.setIsWftSspHashMap(isWftSspHashMap);

			} catch (Exception e) {
				LOGGER.error("EMSAdminPortalImpl::authenticateUserAdmin::GetMiniAppDetails::loginId"
						+ e);
			}

			try {
				String providerSql = "SELECT id,providerName FROM provider";
				Query queryForProvider = emAdmin.createNativeQuery(providerSql);
				// queryForMini.setParameter(SUFFIX,
				// dbContentPerCB.getSuffix());
				List<Object[]> resultList = (List<Object[]>) queryForProvider
						.getResultList();
				for (int i = 0; i < resultList.size(); i++) {
					Object[] obj = (Object[]) resultList.get(i);
					System.out.println(obj[0]);
					System.out.println(obj[1]);
					ProviderHashMap.put(obj[0].toString(), obj[1].toString());
				}
				dbContentPerCB.setProvider(ProviderHashMap);

			} catch (Exception e) {
				LOGGER.error("EMSAdminPortalImpl::authenticateUserAdmin::GetMiniAppDetails::loginId"
						+ e);
			}

			LOGGER.info("EMSAdminPortalImpl::authenticateUserAdmin::Leaving from this method successfully");
			return dbContentPerCB;
		} catch (Exception e) {
			LOGGER.error("EMSAdminPortalImpl::authenticateUserAdmin::Companybranchusercompanyrole::loginId"
					+ e);
			return null;
		}

	}

	// /////////Azar////////////////////////////////////////////

	public List<CompanyDataAdmin> getCompanyDetails(String suffix) {
		LOGGER.info("EMSAdminPortalImpl::getCompanyDetails::Entered into this method");
		List<CompanyDataAdmin> companyDatas = new ArrayList<CompanyDataAdmin>();
		Query query = emAdmin
				.createNativeQuery("SELECT cb.companyId,cb.branchId,cbu.user_emailAddress,v.vin,v.plateNo FROM ltmscompanyadmindb.companybranch cb "
						+ "LEFT OUTER JOIN ltmscompanyadmindb.companybranchuser cbu ON cb.companyId = cbu.cbCompanyId AND cb.branchId = cbu.cbBranchId "
						+ "LEFT OUTER JOIN fleettrackingdb.vehicle v ON cb.companyid = v.companyid and cb.branchid = v.branchid AND v.status IS NULL "
						+ "LEFT OUTER JOIN ltmscompanyadmindb.company c ON cb.companyId = c.companyId WHERE c.suffix=:suffix order by cb.companyId");
		query.setParameter(SUFFIX, suffix);
		LOGGER.info("Before Execute Query" + query);
		List<Object[]> companybranchsList = query.getResultList();
		LOGGER.info("After Execute Query" + query);
		for (int i = 0; i < companybranchsList.size(); i++) {
			Object[] obj = (Object[]) companybranchsList.get(i);
			CompanyDataAdmin companyDataAdmin = new CompanyDataAdmin();
			companyDataAdmin.setCompanyName(obj[0].toString());
			companyDataAdmin.setBranchName(obj[1].toString());
			if (obj[2] != null) {
				companyDataAdmin.setUserName(obj[2].toString());
			}
			if (obj[3] != null) {
				companyDataAdmin.setVin(obj[3].toString());
			}
			if (obj[4] != null) {
				companyDataAdmin.setPlateNo(obj[4].toString());
			}
			companyDatas.add(companyDataAdmin);
		}
		LOGGER.info("EMSAdminPortalImpl::getCompanyDetails::Leaving from this method successfully");
		return companyDatas;
	}

	public List<VehicleData> getBWUtilReport(String suffix,
			VehicleData vehicleData) {
		LOGGER.info("EMSAdminPortalImpl::getBWUtilReport::Entered into this method"
				+ "vehicleData" + vehicleData);
		List<VehicleData> VehicleDataList = new ArrayList<VehicleData>();
		try {
			String companyId = vehicleData.getCompanyId();
			String branchId = vehicleData.getBranchId();
			String vehicleNo = vehicleData.getVin();
			String fromDate = vehicleData.getfromDate();
			String toDate = vehicleData.getToDate();
			String sql = "SELECT DISTINCT(DATE(servertimestamp)),v.vin,v.companyId,v.branchId,v.plateNo "
					+ "FROM fleettrackingdb.vehicleevent ve, fleettrackingdb.vehicle v,ltmscompanyadmindb.company c WHERE v.vin=ve.vin AND v.companyId=c.companyId";

			if (!(companyId.equalsIgnoreCase("All"))) {
				sql = sql + " AND v.companyId = '" + companyId + "'";
			}
			if (!(branchId.equalsIgnoreCase("All"))) {
				sql = sql + " AND v.branchId = '" + branchId + "'";
			}
			if (!(vehicleNo.equalsIgnoreCase("All"))) {
				sql = sql + " AND v.vin = '" + vehicleNo + "'";
			}
			sql = sql
					+ " AND c.suffix=:suffix AND DATE(servertimestamp) BETWEEN :fromDate AND :toDate";
			Query query = em.createNativeQuery(sql);
			query.setParameter(SUFFIX, suffix);
			query.setParameter(STR_FROM_DATE, fromDate);
			query.setParameter(STR_TO_DATE, toDate);
			String prevVin = null;
			String imeiNo = null;
			LOGGER.info("Before Execute Query" + query);
			List<Object[]> resultList = (List<Object[]>) query.getResultList();
			LOGGER.info("After Execute Query" + query);
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				VehicleData vdata = new VehicleData();
				vdata.setVin(obj[1].toString());
				vdata.setTimeStamp(obj[0].toString());
				vdata.setCompanyId(obj[2].toString());
				vdata.setBranchId(obj[3].toString());
				vdata.setPlateNo(obj[4].toString());
				String dateObj = obj[0].toString();
				String vin = obj[1].toString();
				String companyIdObj = obj[2].toString();
				if (i == 0) {
					prevVin = obj[1].toString();
					List<VehicleData> imeiList = getIMEI(obj[1].toString());
					if (!imeiList.isEmpty()) {
						imeiNo = imeiList.get(imeiList.size() - 1).getImeiNo();
					}
				} else if (!(prevVin.equalsIgnoreCase(obj[1].toString()))) {
					List<VehicleData> imeiList = getIMEI(obj[1].toString());
					if (!imeiList.isEmpty()) {
						imeiNo = imeiList.get(imeiList.size() - 1).getImeiNo();
					}
					prevVin = obj[1].toString();
				}
				vdata.setImeiNo(imeiNo);
				long bytesTrx = getBytesTrx(companyIdObj, vin, dateObj);
				if (bytesTrx != 0) {
					vdata.setEventId(bytesTrx);
				}
				VehicleDataList.add(vdata);

			}

		} catch (Exception e) {
			LOGGER.error("EMSAdminPortalImpl::getBWUtilReport::Exception occured"
					+ e);
		}
		LOGGER.info("EMSAdminPortalImpl::getBWUtilReport::Leaving from this method successfully");
		return VehicleDataList;
	}

	public List<CompanyDataAdmin> getCompanyNames(String suffix) {
		LOGGER.info("EMSAdminPortalImpl::getCompanyNames::Entered into this method");
		List<CompanyDataAdmin> companyDataAdmins = new ArrayList<CompanyDataAdmin>();
		try {
			String companyQuery = QRY_COMP
					+ " WHERE c.suffix = :suffix AND isDeleted IS FALSE ORDER BY companyName";
			Query query = emAdmin.createQuery(companyQuery);
			query.setParameter(SUFFIX, suffix);
			LOGGER.info("Before Execute Query:" + query);
			List<Company> companyList = (List<Company>) query.getResultList();
			LOGGER.info("After Execute Query:" + query);
			for (Company company : companyList) {
				CompanyDataAdmin dataAdmin = new CompanyDataAdmin();
				dataAdmin.setCompanyName(company.getCompanyName());
				dataAdmin.setCompanyId(company.getCompanyId());
				dataAdmin.setIsMiniApps(company.getIsMiniApps());
				companyDataAdmins.add(dataAdmin);
			}
		} catch (Exception e) {
			LOGGER.error("EMSAdminPortalImpl::getCompanyNames::Exception Occured"
					+ e);
		}
		LOGGER.info("EMSAdminPortalImpl::getCompanyNames::Leaving from this method");
		return companyDataAdmins;
	}

	public String addCompanyFearures(List<CompanyDataAdmin> companyDataAdmins,
			List<CompanyDataAdmin> companyDataAdmins1, String companyId) {
		LOGGER.info("EMSAdminPortalImpl::addCompanyFearures::Entered into this method companyDataAdmins "
				+ companyDataAdmins
				+ " companyDataAdmins1 "
				+ companyDataAdmins1 + " companyId " + companyId);
		String status = null;
		String userId = null;
		String UserDataMenu1 = "";

		List<String> listOfFeatures = new ArrayList<String>();

		User user = null;
		try {
			try {
				String sqlforUser = "SELECT cbucr.companybranchuser_user_emailAddress,cbucr.crRoleName FROM ltmscompanyadmindb.companybranchusercompanyrole cbucr WHERE cbucr.crRoleName='CompanyAdmin' AND cbucr.companybranchuser_cbCompanyId='"
						+ companyId + "'";

				Query query = em.createNativeQuery(sqlforUser);
				LOGGER.info("Before Execute Query" + query);
				List<Object[]> resultList = (List<Object[]>) query
						.getResultList();
				LOGGER.info("After execute Query" + query);

				for (int i = 0; i < resultList.size(); i++) {
					Object[] row = (Object[]) resultList.get(i);
					userId = String.valueOf(row[0]);
					String userRoe = String.valueOf(row[1]);
				}

				UserId userids = new UserId();
				userids.setEmailAddress(userId);
				userids.setCompanyCompanyId(companyId);

				user = em.find(User.class, userids);

			} catch (Exception e) {
				LOGGER.error("User Details for CA" + e);
			}

			for (int i = 0; i < companyDataAdmins.size(); i++) {
				String featureName = companyDataAdmins.get(i).getFeatureName();
				featureName = featureName.replaceAll("\\(.*\\)", "").trim()
						.replaceAll(" ", "_");

				listOfFeatures.add(featureName);

				Company company = emAdmin.find(Company.class, companyId);

				FeaturesId featuresId = new FeaturesId();
				featuresId.setFeature(companyDataAdmins.get(i)
						.getCompanyFeature());
				featuresId.setFeatureName(companyDataAdmins.get(i)
						.getFeatureName());

				Features features = emAdmin.find(Features.class, featuresId);

				CompanyHasFeaturesId companyHasFeaturesId = new CompanyHasFeaturesId();
				companyHasFeaturesId.setCompanyCompanyId(companyId);
				companyHasFeaturesId.setFeaturesFeature(companyDataAdmins
						.get(i).getCompanyFeature());
				companyHasFeaturesId.setFeaturesFeatureName(companyDataAdmins
						.get(i).getFeatureName());

				CompanyHasFeatures companyHasFeatures = emAdmin.find(
						CompanyHasFeatures.class, companyHasFeaturesId);
				if (companyHasFeatures == null) {
					CompanyHasFeatures companyHasFeatures1 = new CompanyHasFeatures();
					companyHasFeatures1.setCompany(company);
					companyHasFeatures1.setFeatures(features);
					companyHasFeatures1.setMenuId(Integer
							.valueOf(companyDataAdmins.get(i).getContact()));
					companyHasFeatures1.setMenuRefId(Integer
							.valueOf(companyDataAdmins.get(i).getFax()));
					companyHasFeatures1.setId(companyHasFeaturesId);
					emAdmin.persist(companyHasFeatures1);
				}
			}
			UserDataMenu1 = "[";
			for (String s : listOfFeatures) {
				UserDataMenu1 += "'" + s + "',";
			}
			UserDataMenu1 += "'company_management']";

			if (user != null) {
				user.setMainMenu1(UserDataMenu1);
			}
		} catch (Exception e) {
			LOGGER.error("EMSAdminPortalImpl::addCompanyFearures::for companyDataAdmins::Exception Occured"
					+ e);
		}
		try {
			for (int i = 0; i < companyDataAdmins1.size(); i++) {
				FeaturesId featuresId = new FeaturesId();
				featuresId.setFeature(companyDataAdmins1.get(i)
						.getCompanyFeature());
				featuresId.setFeatureName(companyDataAdmins1.get(i)
						.getFeatureName());

				CompanyHasFeaturesId companyHasFeaturesId = new CompanyHasFeaturesId();
				companyHasFeaturesId.setCompanyCompanyId(companyId);
				companyHasFeaturesId.setFeaturesFeature(companyDataAdmins1.get(
						i).getCompanyFeature());
				companyHasFeaturesId.setFeaturesFeatureName(companyDataAdmins1
						.get(i).getFeatureName());

				CompanyHasFeatures companyHasFeatures = emAdmin.find(
						CompanyHasFeatures.class, companyHasFeaturesId);
				if (companyHasFeatures != null) {
					emAdmin.remove(companyHasFeatures);
				}
			}
		} catch (Exception e) {
			LOGGER.error("EMSAdminPortalImpl::addCompanyFearures::for companyDataAdmins1:: Exception Occured"
					+ e);
		}
		LOGGER.info("EMSAdminPortalImpl::addCompanyFearures::Leaving from method is successfully");
		return status;
	}

	public List<CompanyDataAdmin> getCompanyFeatures(CompanyDataAdmin dataAdmin) {
		LOGGER.info("EMSAdminPortalImpl::getCompanyFeatures::Entered into this method"
				+ "dataAdmin" + dataAdmin);
		List<CompanyDataAdmin> companyDatas = new ArrayList<CompanyDataAdmin>();
		String companyId = dataAdmin.getCompanyName();
		String where = "";
		SortedMap<String, List<String>> hashMapFeatures = new TreeMap<String, List<String>>();

		try {
			if (dataAdmin.getIsMiniApps()) {
				where = " f.isMiniApps='1' AND ";
			} else if (dataAdmin.getIsSspWft()) {
				where = " f.isSspWft='1' AND ";

			}

			String featuresQuery = "SELECT f.feature,f.featureName,f.moduleCode,chf.company_companyId,(CASE chf.company_companyId WHEN '"
					+ companyId
					+ "' THEN 'true' ELSE 'false' END),f.MenuId,f.MenuRefId FROM ltmscompanyadmindb.features f "
					+ " LEFT JOIN ltmscompanyadmindb.company_has_features chf ON (f.featureName=chf.features_featureName AND f.feature=chf.features_feature "
					+ " AND chf.company_companyId = '"
					+ companyId
					+ "') WHERE"
					+ where
					+ " f.moduleCode IS NULL OR f.moduleCode ='new' order by f.Menuid";
			Query query = emAdmin.createNativeQuery(featuresQuery);
			LOGGER.info("Before Execute Query" + query);
			List<Object[]> mainFeatures = query.getResultList();
			List<Object[]> featuresName = query.getResultList();
			LOGGER.info("After Execute Query" + query);
			for (int i = 0; i < mainFeatures.size(); i++) {
				Object[] obj1 = mainFeatures.get(i);
				CompanyDataAdmin companyDataAdmin = new CompanyDataAdmin();
				List<String> listOfFeatures = new ArrayList<String>();
				String feature = obj1[0].toString();
				for (int j = 0; j < featuresName.size(); j++) {
					Object[] obj2 = featuresName.get(j);
					if (obj2[0].toString().equalsIgnoreCase(feature)) {
						listOfFeatures.add(obj2[1].toString() + ","
								+ obj2[4].toString() + "," + obj2[5].toString()
								+ "," + obj2[6].toString());
					}
				}
				hashMapFeatures.put(feature, listOfFeatures);
				companyDataAdmin.setCompFeatures(hashMapFeatures);
				companyDatas.add(companyDataAdmin);
			}

		} catch (Exception e) {
			LOGGER.error("EMSAdminPortalImpl::getCompanyFeatures::Exception Occured"
					+ e);
		}
		return companyDatas;
	}

	public List<CompanyDataAdmin> getErrorLogInfo(CompanyDataAdmin companyData) {
		LOGGER.info("EMSAdminPortalImpl::getErrorLogInfo::Entered into this method"
				+ "companyData" + companyData);
		List<CompanyDataAdmin> listOfCompData = new ArrayList<CompanyDataAdmin>();
		String formDate = companyData.getFromDate();
		String toDate = companyData.getToDate();
		String stmt = "SELECT ul FROM Errorlog ul WHERE DATE(serverdatetime) BETWEEN '"
				+ formDate
				+ "' AND '"
				+ toDate
				+ "'ORDER BY serverdatetime DESC";
		Query query = emAdmin.createQuery(stmt);
		LOGGER.info("Before Execute Query" + query);
		List<Errorlog> errorlogList = (List<Errorlog>) query.getResultList();
		LOGGER.info("After Execute Query" + query);
		for (Errorlog userlogin : errorlogList) {
			CompanyDataAdmin companyDataAdmin = new CompanyDataAdmin();
			companyDataAdmin.setDescription(userlogin.getDescription());
			companyDataAdmin.setModule(userlogin.getModule());
			companyDataAdmin.setVin(userlogin.getVin());
			companyDataAdmin.setServerTime(TimeZoneUtil.getStrTZ(userlogin
					.getServerdatetime()));
			companyDataAdmin.setServerDate(TimeZoneUtil.getStrDZ(userlogin
					.getServerdatetime()));
			Vehicle vehicle = em.find(Vehicle.class, userlogin.getVin());
			companyDataAdmin.setPlateNo(vehicle.getPlateNo());
			if (userlogin.getDetails() != null) {
				String[] details = userlogin.getDetails().split(",");
				companyDataAdmin.setTimeConnected(details[0]);
				companyDataAdmin.setEmail(details[1]);
			}
			listOfCompData.add(companyDataAdmin);
		}
		LOGGER.info("EMSAdminPortalImpl::getErrorLogInfo::Leaving from this method");
		return listOfCompData;
	}

	public List<CompanyDataAdmin> getLoginInfo(String suffix,
			CompanyDataAdmin companyData) {
		LOGGER.info("EMSAdminPortalImpl::getLoginInfo::Entered into this method"
				+ "companyData" + companyData);
		List<CompanyDataAdmin> listOfCompData = new ArrayList<CompanyDataAdmin>();
		long diff = 0;
		String compName = companyData.getCompanyName();
		String brchName = companyData.getBranchName();
		String userName = companyData.getUserName();
		String formDate = companyData.getFromDate();
		String toDate = companyData.getToDate();
		String stmt = "SELECT ul FROM Userlogin ul , Company c  WHERE DATE(loginTime) BETWEEN '"
				+ formDate
				+ "' AND '"
				+ toDate
				+ "' AND ul.companyId=c.companyId ";
		if (!(compName.equalsIgnoreCase("All"))) {
			stmt = stmt + "AND ul.companyId = '" + compName + "'";
		}
		if (!(brchName.equalsIgnoreCase("All"))) {
			stmt = stmt + "AND ul.branchId = '" + brchName + "'";
		}
		if (!(userName.equalsIgnoreCase("All"))) {
			stmt = stmt + "AND ul.userId = '" + userName + "'";
		}
		stmt = stmt + " AND c.suffix=:suffix ORDER BY logintime DESC";
		Query query = emAdmin.createQuery(stmt);
		query.setParameter(SUFFIX, suffix);
		LOGGER.info("Before Execute Query" + query);
		List<Userlogin> userLoginList = (List<Userlogin>) query.getResultList();
		LOGGER.info("After Execute Query" + query);
		for (Userlogin userlogin : userLoginList) {
			CompanyDataAdmin companyDataAdmin = new CompanyDataAdmin();
			companyDataAdmin.setCompanyName(userlogin.getCompanyId());
			companyDataAdmin.setBranchName(userlogin.getBranchId());
			companyDataAdmin.setUserName(userlogin.getUserId());
			companyDataAdmin.setRoleName(userlogin.getRole());
			companyDataAdmin.setMacIp(userlogin.getMacIp());
			companyDataAdmin.setLoginTime(TimeZoneUtil.getStrTZ(userlogin
					.getLoginTime()));
			if (userlogin.getLogoutTime() != null) {
				companyDataAdmin.setLogoutTime(TimeZoneUtil.getStrTZ(userlogin
						.getLogoutTime()));
				diff = ((userlogin.getLogoutTime().getTime()) - (userlogin
						.getLoginTime().getTime())) / 1000;
				companyDataAdmin.setTimeConnected(formatIntoHHMMSS((int) diff));

			} else {
				companyDataAdmin.setLogoutTime("null");
				companyDataAdmin.setTimeConnected("null");
			}
			listOfCompData.add(companyDataAdmin);
		}
		LOGGER.info("EMSAdminPortalImpl::getLoginInfo::Leaving from this method Successfully");
		return listOfCompData;
	}

	private String formatIntoHHMMSS(int secsIn) {
		int days = secsIn / 86400, remainder = secsIn % 86400;
		int hours = remainder / 3600, rem = remainder % 3600, minutes = rem / 60, seconds = rem % 60;
		return ((days < 10 ? "0" : "") + days + ":" + (hours < 10 ? "0" : "")
				+ hours + ":" + (minutes < 10 ? "0" : "") + minutes + ":"
				+ (seconds < 10 ? "0" : "") + seconds);
	}

	private String formatIntoHHMMSSWithOutDay(int secsIn) {
		int remainder = secsIn % 86400;
		int hours = remainder / 3600, rem = remainder % 3600, minutes = rem / 60, seconds = rem % 60;
		return ((hours < 10 ? "0" : "") + hours + ":"
				+ (minutes < 10 ? "0" : "") + minutes + ":"
				+ (seconds < 10 ? "0" : "") + seconds);
	}

	public String sendEmail(String from, String to, String subject, String text) {
		return null;
	}

	public String updateSMSCount(VehicleData vehicleData) {
		LOGGER.info("EMSAdminPortalImpl::updateSMSCount::Entered into this method"
				+ "vehicleData" + vehicleData);

		SmsconfigId smsconfigId = new SmsconfigId();
		smsconfigId.setCompanyid(vehicleData.getCompanyId());
		smsconfigId.setBranchId(vehicleData.getBranchId());
		Smsconfig smsconfig = emAdmin.find(Smsconfig.class, smsconfigId);
		smsconfig.setSmsCnt(vehicleData.getEventId());

		Smshistory smshistory = new Smshistory();
		SmshistoryId smshistoryId = new SmshistoryId();
		smshistoryId.setCompanyId(vehicleData.getCompanyId());
		smshistoryId.setBranchId(vehicleData.getBranchId());
		smshistoryId.setSmsCnt(Long.valueOf(vehicleData.getSmsNumber()));
		smshistoryId.setLastUpdDt(new Date());
		smshistory.setId(smshistoryId);
		emAdmin.persist(smshistory);
		LOGGER.info("EMSAdminPortalImpl::updateSMSCount::Leaving from this method");
		return "success";
	}

	public List<VehicleData> getSmsHistory(VehicleData vehicleData) {
		LOGGER.info("EMSAdminPortalImpl::getSmsHistory::Entered into this method"
				+ "vehicleData" + vehicleData);
		List<VehicleData> userVehicle = new ArrayList<VehicleData>();
		String companyId = vehicleData.getCompanyId();
		String branchId = vehicleData.getBranchId();
		try {

			String sqlforUser = "select s from Smshistory s where companyId=:companyId AND branchId=:branchId";
			Query query1 = emAdmin.createQuery(sqlforUser);
			query1.setParameter("companyId", companyId);
			query1.setParameter("branchId", branchId);
			LOGGER.info("Before Execute Query" + query1);
			List<Smshistory> resultList = (List<Smshistory>) query1
					.getResultList();
			LOGGER.info("After Execute Query" + query1);
			for (Smshistory smshistory : resultList) {
				VehicleData vehicleData1 = new VehicleData();
				vehicleData1.setCompanyId(smshistory.getId().getCompanyId());
				vehicleData1.setBranchId(smshistory.getId().getBranchId());
				vehicleData1.setSmsNumber(String.valueOf(smshistory.getId()
						.getSmsCnt()));
				vehicleData1
						.setLastUpdDate(TimeZoneUtil
								.getTimeINYYYYMMddss(smshistory.getId()
										.getLastUpdDt()));
				userVehicle.add(vehicleData1);
			}

		} catch (Exception e) {
			LOGGER.error("EMSAdminPortalImpl::getSmsHistory::Exception occured::"
					+ e);
		}
		LOGGER.info("EMSAdminPortalImpl::Leaving from this method");
		return userVehicle;
	}

	public List<VehicleData> getSMSCount(String suffix) {
		LOGGER.info("EMSAdminPortalImpl::getSMSCount::Entered into this method");

		List<VehicleData> vehicleDataList = new ArrayList<VehicleData>();
		try {
			String companyQuery = "SELECT s FROM Smsconfig s,Company c where c.suffix=:suffix and s.id.companyid=c.companyId";
			Query query = emAdmin.createQuery(companyQuery);
			query.setParameter(SUFFIX, suffix);
			LOGGER.info("Before Execute Query" + query);
			List<Smsconfig> smsList = (List<Smsconfig>) query.getResultList();
			LOGGER.info("After Execute Query" + query);
			for (Smsconfig smsconfig : smsList) {
				VehicleData vehicleData = new VehicleData();
				vehicleData.setCompanyId(smsconfig.getId().getCompanyid());
				vehicleData.setBranchId(smsconfig.getId().getBranchId());
				vehicleData.setEventId(smsconfig.getSmsCnt());
				vehicleData.setStatus(String.valueOf(smsconfig.getStatus()));
				if (smsconfig.getLastUpdDt() != null) {
					vehicleData.setLastUpdDate(TimeZoneUtil.getDate(smsconfig
							.getLastUpdDt()));
				}
				vehicleDataList.add(vehicleData);
			}
		} catch (Exception e) {
			LOGGER.error("EMSAdminPortalImpl::getSMSCount::Exception occured"
					+ e);
		}
		LOGGER.info("EMSAdminPortalImpl::getSMSCount::Leaving from this method Successfully");
		return vehicleDataList;

	}

	public String vehicleSummaryBulkExec(String fromDate, String toDate,
			String companyName) {
		LOGGER.info("EMSAdminPortalImpl::vehicleSummaryBulkExec::Entered into this method::"
				+ "fromDate"
				+ fromDate
				+ STR_TO_DATE
				+ toDate
				+ "companyName"
				+ companyName);
		try {
			Query queryExceDate = em
					.createNativeQuery("SELECT DATE(ve.eventTimeStamp),ve.vin FROM vehicleevent ve ,vehicle v WHERE DATE(ve.eventTimeStamp) "
							+ " BETWEEN :fromDate and :toDate AND ve.vin = v.vin AND v.companyId =:companyName "
							+ " GROUP BY DATE(ve.eventTimeStamp),ve.vin");

			queryExceDate.setParameter(STR_FROM_DATE, sdfDate.parse(fromDate));
			queryExceDate.setParameter(STR_TO_DATE, sdfDate.parse(toDate));
			queryExceDate.setParameter("companyName", companyName);
			LOGGER.info("Before Execute Query" + queryExceDate);
			List<Object[]> listVehicleevents = (List<Object[]>) queryExceDate
					.getResultList();
			LOGGER.info("After Execute Query" + queryExceDate);
			for (int i = 0; i < listVehicleevents.size(); i++) {
				Object[] obj = (Object[]) listVehicleevents.get(i);
				String vin = (String) obj[1];
				Date eventDate = (Date) obj[0];
				// check if the vin and date already exist in vehicle summary
				// table
				boolean alreadyExist = chkVehiSumm(vin,
						sdfDate.format(eventDate));

				if (alreadyExist) {
					List<ReportData> reportDatas = new ArrayList<ReportData>();
					// call summary logic method
					reportDatas = getVehicleSummaryDayReport(vin,
							sdfDate.format(eventDate));
					if (!reportDatas.isEmpty()) {
						addVehicleSummaryBulk(reportDatas, vin,
								sdfDate.format(eventDate));
					}

				}
			}
		} catch (Exception e) {
			LOGGER.error("EMSAdminPortalImpl::vehicleSummaryBulkExec::Exception occrured"
					+ e);
		}
		String status = null;
		LOGGER.info("EMSAdminPortalImpl::vehicleSummaryBulkExec::Leaving from this method is successfully");
		return status;
	}

	public List<VehicleData> getLiveVehicles(String suffix) {
		LOGGER.info("EMSAdminPortalImpl::getLiveVehicles::Entered into this method");

		List<VehicleData> vehicleEventList = new ArrayList<VehicleData>();
		try {

			String sqlforVehicle = "SELECT v.plateNo,v.vin from fleettrackingdb.vehicle v,fleettrackingdb.vehicleevent evt,ltmscompanyadmindb.company c WHERE v.vin=evt.vin AND v.companyId=c.companyId AND c.suffix=:suffix AND v.status IS NULL group by plateNo";

			Query query = em.createNativeQuery(sqlforVehicle);
			query.setParameter(SUFFIX, suffix);
			LOGGER.info("Before Execute Query" + query);
			List<Object[]> resultList = (List<Object[]>) query.getResultList();
			LOGGER.info("After execute Query" + query);
			for (int i = 0; i < resultList.size(); i++) {
				Object[] row = (Object[]) resultList.get(i);
				VehicleData vehicleData = new VehicleData();
				vehicleData.setPlateNo(String.valueOf(row[0]));
				vehicleData.setVin(String.valueOf(row[1]));
				vehicleEventList.add(vehicleData);
			}
		} catch (Exception e) {
			LOGGER.error("EMSAdminPortalImpl::getLiveVehicles::Exception Occcured"
					+ e);
		}
		LOGGER.info("EMSAdminPortalImpl::getLiveVehicles::Leaving from this method");
		return vehicleEventList;

	}

	public List<VehicleData> getVehicleEventInfo(VehicleData vehicleData) {
		LOGGER.info("EMSAdminPortalImpl::getVehicleEventInfo::Entered into this method"
				+ "vehicleData" + vehicleData);
		List<VehicleData> vehicleDatas = new ArrayList<VehicleData>();
		String vin = vehicleData.getVin();
		String fromDate = vehicleData.getfromDate();
		String toDate = vehicleData.getToDate();
		String ioeventValue = vehicleData.getEventStatus();
		String[] ioEvent = null, ioValue = null;
		try {
			String sqlQuery = "Select vin,eventtimestamp,latitude,longitude,speed,ioevent,bytesTrx,date(eventTimestamp),servertimestamp,eventSource from vehicleevent where vin=:vin and date(eventTimestamp) between :fromDate AND :toDate ORDER BY eventtimestamp DESC";
			Query query = em.createNativeQuery(sqlQuery);
			query.setParameter("vin", vin);
			query.setParameter(STR_FROM_DATE, TimeZoneUtil.getDate(fromDate));
			query.setParameter(STR_TO_DATE, TimeZoneUtil.getDate(toDate));
			LOGGER.info("before Execute Query" + query);
			List<Object[]> resultList = (List<Object[]>) query.getResultList();
			LOGGER.info("After execute Query" + query);
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				VehicleData vehi = new VehicleData();
				vehi.setVin(obj[0].toString());
				vehi.setTimeStamp(TimeZoneUtil.getStrTZ((Date) obj[1]));
				vehi.setLatitude(Double.parseDouble(obj[2].toString()));
				vehi.setLongitude(Double.parseDouble(obj[3].toString()));

				if (ioeventValue.equalsIgnoreCase("All")) {
					vehi.setIoEvent(obj[5].toString());
				} else {
					ioEvent = obj[5].toString().trim().split(",");
					for (int j = 0; j < ioEvent.length; j++) {
						ioValue = ioEvent[j].split("=");
						if (ioValue[0].equalsIgnoreCase(ioeventValue)) {
							vehi.setIoEvent(obj[5].toString());
							break;
						}
					}
				}
				vehi.setSubject("1=Digital Input Status 1,2=Digital Input Status 2,3=Digital Input Status 3,4=Digital Input 4,9=Analog Input 1,10=Analog Input 2,11=Analog Input 3,19=Analog Input 4,21=GSM Signal Strength,22=Current Profile,23=Accelerometer data,24=GPS speed,66=Power Supply Voltage,67=Battery Voltage,68=Battery Current,69=GPS Power,70=PCB Temperature,72=Temperature Sensor1,73=Temperature Sensor2,74=Temperature Sensor3,76=Fuel Counter,78=iButton Input,145=CAN 0,146=CAN 1,147=CAN 2,148=CAN 3,149=CAN 4,150=CAN 5,151=CAN 6,152=CAN 7,153=CAN 8,154=CAN 9,155=Geozone 01,156=Geozone 02,157=Geozone 03,158=Geozone 04,159=Geozone 05,160=Geozone 06,161=Geozone 07,162=Geozone 08,163=Geozone 09,164=Geozone 10,165=Geozone 11,166=Geozone 12,167=Geozone 13,168=Geozone 14,169=Geozone 15,170=Geozone 16,171=Geozone 17,172=Geozone 18,173=Geozone 19,174=Geozone 20,199=Virtual Odometer,240=Movement");
				vehi.setSpeed(Integer.valueOf(obj[4].toString()));
				if (obj[6] != null) {
					vehi.setContent(obj[6].toString());
				}
				vehi.setDescription(obj[7].toString());
				vehi.setAlertTime(TimeZoneUtil.getStrTZ((Date) obj[8]));
				if (obj[9] != null) {
					vehi.setEventStatus(obj[9].toString());
				}
				if (vehi.getIoEvent() != null) {
					vehicleDatas.add(vehi);
				}
			}
		} catch (Exception e) {
			LOGGER.error("EMSAdminPortalImpl::getVehicleEventInfo::Exception occured"
					+ e);
		}
		LOGGER.info("EMSAdminPortalImpl::getVehicleEventInfo::Leaving from this method");
		return vehicleDatas;
	}

	public List<String> validatecompanyId() {
		LOGGER.info("EMSAdminPortalImpl::validatecompanyId::Entered into this method");
		List<String> companyId = new ArrayList<String>();
		try {
			String validateQuery = "SELECT x FROM Company x";
			Query query = emAdmin.createQuery(validateQuery);
			LOGGER.info("Before Execute Query" + query);
			List<Company> cids = (List<Company>) query.getResultList();
			LOGGER.info("After Execute Query" + query);
			for (Company cid : cids) {
				companyId.add(cid.getCompanyId());
			}
		} catch (Exception e) {
			LOGGER.error("EMSAdminPortalImpl::validatecompanyId::Exception Occured"
					+ e);
		}
		LOGGER.info("EMSAdminPortalImpl::validatecompanyId::Leaving from this method successfully");
		return companyId;
	}

	public List<CompanyDataAdmin> getCountryList(String companyId) {
		LOGGER.info("EMSAdminPortalImpl::getCountryList::Entered into this method"
				+ companyId);
		List<CompanyDataAdmin> companyDatas = new ArrayList<CompanyDataAdmin>();
		Query query = emAdmin.createQuery("select ctry from Country ctry");
		LOGGER.info("Before Execute Query" + query);
		List<Country> countries = (List<Country>) query.getResultList();
		LOGGER.info("After Execute Query" + query);
		for (Country country : countries) {
			CompanyDataAdmin companyData = new CompanyDataAdmin();
			companyData.setCountryCode(country.getCountryCode());
			companyData.setCountryName(country.getCountryName());
			companyDatas.add(companyData);
		}
		LOGGER.info("EMSAdminPortalImpl::getCountryList::Leaving from this method");
		return companyDatas;
	}

	public String addCompanyRegistration(CompanyDataAdmin companyData) {
		LOGGER.info("EMSAdminPortalImpl::addCompanyRegistration::Entered into this method"
				+ "companyData" + companyData);
		String status = null;
		try {

			Company company = new Company();
			company.setCompanyId(companyData.getCompanyId());
			company.setCompanyName(companyData.getCompanyName());
			company.setSuffix(companyData.getSuffix());
			company.setIsDemo(companyData.getIsdemo());
			company.setFollowUp(companyData.getIsfollowup());
			company.setRemarks(companyData.getRemarks());
			company.setNotransmissionrptskip(companyData
					.getIsNoTransmissionSkip());
			company.setIsMiniApps(companyData.getIsMiniApps());
			company.setSalesPerson(companyData.getSalesPerson());
			company.setDateCreated(new Date());
			company.setIsLostDeal(companyData.getIsLostDeal());
			company.setIsDeleted(false);
			company.setIsSspWft(companyData.getIsSspWft());
			company.setMobileAppShorterLink(companyData
					.getMobileAppShortenLink());

			emAdmin.persist(company);

			Address address = new Address();
			address.setAddressLine1(companyData.getAddressLine1());
			address.setAddressLine2(companyData.getAddressLine2());
			address.setAddressCity(companyData.getAddressCity());
			Country country = emAdmin.find(Country.class,
					companyData.getCountryCode());
			address.setCountry(country);
			emAdmin.persist(address);

			Companybranch companybranch = new Companybranch();
			CompanybranchId companybranchId = new CompanybranchId();
			companybranchId.setBranchId(companyData.getCompanyId());
			companybranchId.setCompanyId(companyData.getCompanyId());
			companybranch.setId(companybranchId);
			companybranch.setRegion(companyData.getModule());
			companybranch.setBranchName(companyData.getCompanyId());
			companybranch.setCompany(company);
			companybranch.setAddress(address);
			emAdmin.persist(companybranch);

			Address address1 = new Address();
			address1.setAddressLine1(companyData.getAddressLine1());
			address1.setAddressLine2(companyData.getAddressLine2());
			address1.setAddressCity(companyData.getAddressCity());
			Country country1 = emAdmin.find(Country.class,
					companyData.getCountryCode());
			address1.setCountry(country1);
			emAdmin.persist(address1);

			User user = new User();
			UserId userId = new UserId();
			userId.setCompanyCompanyId(companyData.getCompanyId());
			userId.setEmailAddress(companyData.getUserName());
			user.setAddress(address1);
			user.setCompany(company);
			user.setFirstName(companyData.getUserName());
			user.setLastName("");
			user.setId(userId);
			user.setUserPasswd(companyData.getPwd());
			String jsonMenu = "{ \"id\":\"100\",\"parentid\":\"-1\",\"text\":\"<table cellpadding='0' cellspacing='0'><tr><td><img src='Menu_Icons/Company_Management.png' width='14' height='14' style='padding-right:4px;' /></td><td style='font-family: Verdana; color: #ffffff; font-size: 12px;'>&nbsp;<b>Company Management</b></td></tr></table>\",\"subMenuWidth\": \"240px\"},{ \"id\":\"101\",\"parentid\":\"100\",\"text\":\"<table cellpadding='0' cellspacing='0'><tr><td><img src='Menu_Icons/Branch_Details.png' width='14' height='14' style='padding-right:4px;' /></td><td style='font-family: Verdana; color: #ffffff; font-size: 12px;'>&nbsp;<b>Branch Details</b></td></tr></table>\",\"subMenuWidth\": \"240px\"},{ \"id\":\"102\",\"parentid\":\"100\",\"text\":\"<table cellpadding='0' cellspacing='0'><tr><td><img src='Menu_Icons/Add_or_Modify_Images.png' width='14' height='14' style='padding-right:4px;' /></td><td style='font-family: Verdana; color: #ffffff; font-size: 12px;'>&nbsp;<b>Add or Modify Images</b></td></tr></table>\",\"subMenuWidth\": \"240px\"},{ \"id\":\"103\",\"parentid\":\"100\",\"text\":\"<table cellpadding='0' cellspacing='0'><tr><td><img src='Menu_Icons/User_Features_Details.png' width='14' height='14' style='padding-right:4px;' /></td><td style='font-family: Verdana; color: #ffffff; font-size: 12px;'>&nbsp;<b>User Features Details</b></td></tr></table>\",\"subMenuWidth\": \"240px\"}";
			String jsonMenu1 = "['company_management']";
			user.setMainMenu(jsonMenu);
			user.setMainMenu1(jsonMenu1);
			emAdmin.persist(user);

			Companybranchuser companybranchuser = new Companybranchuser();
			CompanybranchuserId companybranchuserId = new CompanybranchuserId();
			companybranchuserId.setCbBranchId(companyData.getCompanyId());
			companybranchuserId.setCbCompanyId(companyData.getCompanyId());
			companybranchuserId.setUserEmailAddress(companyData.getUserName());
			companybranchuserId.setUserCompanyCompanyId(companyData
					.getCompanyId());
			companybranchuser.setId(companybranchuserId);
			companybranchuser.setUser(user);
			companybranchuser.setCompanybranch(companybranch);
			emAdmin.persist(companybranchuser);

			Role role = emAdmin.find(Role.class, STR_COMP_ADMIN);
			Companybranchusercompanyrole companybranchusercompanyrole = new Companybranchusercompanyrole();
			CompanybranchusercompanyroleId companybranchusercompanyroleId = new CompanybranchusercompanyroleId();
			companybranchusercompanyroleId
					.setCompanybranchuserCbBranchId(companyData.getCompanyId());
			companybranchusercompanyroleId
					.setCompanybranchuserCbCompanyId(companyData.getCompanyId());
			companybranchusercompanyroleId
					.setCompanybranchuserUserCompanyCompanyId(companyData
							.getCompanyId());
			companybranchusercompanyroleId
					.setCompanybranchuserUserEmailAddress(companyData
							.getUserName());
			companybranchusercompanyroleId.setCrRoleName(role.getRoleName());
			companybranchusercompanyrole.setId(companybranchusercompanyroleId);
			companybranchusercompanyrole.setRole(role);
			companybranchusercompanyrole
					.setCompanybranchuser(companybranchuser);
			emAdmin.persist(companybranchusercompanyrole);

			Smsconfig smsconfig = new Smsconfig();
			SmsconfigId smsconfigId = new SmsconfigId();
			smsconfigId.setCompanyid(companyData.getCompanyId());
			smsconfigId.setBranchId(companyData.getCompanyId());
			smsconfig.setStatus(true);
			smsconfig.setId(smsconfigId);
			smsconfig.setSmsCnt(Long.valueOf(0));
			emAdmin.persist(smsconfig);

			status = STR_PERSISTED;
		} catch (Exception e) {
			LOGGER.error("EMSAdminPortalImpl::addCompanyRegistration::Exception Occured"
					+ e);
			status = " not persisted";
		}
		LOGGER.info("EMSAdminPortalImpl::addCompanyRegistration::Leaving from this method Successfully");
		return status;

	}

	public String startSimulator(String imeiNo) {
		return simulator.activateAndStartDevice(imeiNo);
	}

	public String stopSimulator(String imeiNo) {
		return simulator.deActivateDevice(imeiNo);
	}

	public List<VehicleData> startBatchListener(String userId) {
		return null;
	}

	public List<VehicleData> btnEnableDisable() {
		return null;
	}

	public List<VehicleData> stopBatchListener(String userId) {
		return null;
	}

	public List<OperatorData> getOperatorTelNo(String compName,
			String branchName, String userName) {
		return null;
	}

	public String sendSMS(String mobileNo, String msg) {
		return null;
	}

	public String updateLoginInfo(CompanyDataAdmin companyDataAdmin) {
		LOGGER.info("EMSAdminPortalImpl::updateLoginInfo::Entered into this method"
				+ "companyDataAdmin" + companyDataAdmin);
		String status = null;
		long loginId = Long.valueOf(companyDataAdmin.getUserLoginId());
		try {
			Userlogin userlogin = emAdmin.find(Userlogin.class, loginId);
			userlogin.setLogoutTime(TimeZoneUtil.getDateInTimeZone());
			userlogin.setLastUpdatedBy(companyDataAdmin.getUserName());
			userlogin.setLastUpdatedDate(TimeZoneUtil.getDateInTimeZone());
			status = STR_PERSISTED;
		} catch (Exception e) {
			LOGGER.error("EMSAdminPortalImpl::updateLoginInfo::Exception Occured"
					+ e);
		}
		LOGGER.info("EMSAdminPortalImpl::updateLoginInfo::Leaving from this method successfully");
		return status;
	}

	public List<CompanyDataAdmin> getCompanyRegistration(String suffix) {
		LOGGER.info("EMSAdminPortalImpl::getCompanyRegistration::Entered into this method");
		List<CompanyDataAdmin> companyDatas = new ArrayList<CompanyDataAdmin>();
		String sql = QRY_COMP
				+ " WHERE c.suffix = :suffix and isDeleted IS FALSE";
		Query query = emAdmin.createQuery(sql);
		query.setParameter(SUFFIX, suffix);
		LOGGER.info("before Query Execute query" + query);
		List<Company> company = (List<Company>) query.getResultList();
		LOGGER.info("After Execute Query" + query);
		for (Company comp : company) {
			Set<Companybranch> companyBranches = comp.getCompanybranchs();
			for (Companybranch compBranch : companyBranches) {
				Set<Companybranchuser> companybranchusers = compBranch
						.getCompanybranchusers();
				for (Companybranchuser companybranchuser : companybranchusers) {
					Set<Companybranchusercompanyrole> companybranchusercompanyroles = companybranchuser
							.getCompanybranchusercompanyroles();
					for (Companybranchusercompanyrole companybranchusercompanyrole : companybranchusercompanyroles) {
						if (companybranchusercompanyrole.getId()
								.getCrRoleName()
								.equalsIgnoreCase(STR_COMP_ADMIN)) {
							CompanyDataAdmin companyDataAdmin = new CompanyDataAdmin();

							companyDataAdmin.setCompanyId(comp.getCompanyId());
							companyDataAdmin.setCompanyName(comp
									.getCompanyName());
							companyDataAdmin.setBranchId(compBranch.getId()
									.getBranchId());
							companyDataAdmin.setBranchName(compBranch
									.getBranchName());
							companyDataAdmin.setAddressId(compBranch
									.getAddress().getAddressId());
							companyDataAdmin.setAddressLine1(compBranch
									.getAddress().getAddressLine1());
							companyDataAdmin.setAddressLine2(compBranch
									.getAddress().getAddressLine2());
							companyDataAdmin.setAddressCity(compBranch
									.getAddress().getAddressCity());
							companyDataAdmin
									.setCountryName(compBranch.getAddress()
											.getCountry().getCountryName());
							companyDataAdmin
									.setUserName(companybranchusercompanyrole
											.getId()
											.getCompanybranchuserUserEmailAddress());
							companyDataAdmin.setModule(compBranch.getRegion());
							companyDataAdmin.setRemarks(comp.getRemarks());
							companyDataAdmin.setPwd(companybranchuser.getUser()
									.getUserPasswd());
							companyDataAdmin.setIsfollowup(comp.getFollowUp());
							companyDataAdmin.setIsdemo(comp.getIsDemo());
							companyDataAdmin.setIsNoTransmissionSkip(comp
									.getNotransmissionrptskip());

							companyDataAdmin
									.setIsMiniApps(comp.getIsMiniApps());
							companyDataAdmin.setSalesPerson(comp
									.getSalesPerson());
							companyDataAdmin
									.setIsLostDeal(comp.getIsLostDeal());

							// get wft/ssp
							companyDataAdmin.setIsSspWft(comp.getIsSspWft());

							// get shorter link for mobile app
							companyDataAdmin.setMobioleAppShortenLink(comp
									.getMobileAppShorterLink());
							companyDatas.add(companyDataAdmin);

						}

					}
				}
			}

		}

		LOGGER.info("EMSAdminPortalImpl::getCompanyRegistration::Leaving from this method");
		return companyDatas;
	}

	private List<VehicleData> getIMEI(String vin) {
		LOGGER.info("EMSAdminPortalImpl::getIMEI::Entered into this method"
				+ "vin" + vin);
		List<VehicleData> imeiList = new ArrayList<VehicleData>();
		String sql = "SELECT companytrackdevice_imeiNo,vehicle_vin FROM vehicle_has_companytrackdevice WHERE vehicle_vin=:vin";
		Query query = em.createNativeQuery(sql);
		query.setParameter("vin", vin);
		LOGGER.info("Before Execute Query" + query);
		List<Object[]> resultList = (List<Object[]>) query.getResultList();
		LOGGER.info("After Execute Query" + query);
		for (int i = 0; i < resultList.size(); i++) {
			Object[] obj = (Object[]) resultList.get(i);
			VehicleData vedata = new VehicleData();
			vedata.setImeiNo(obj[0].toString());
			vedata.setVin(obj[1].toString());
			imeiList.add(vedata);
		}
		LOGGER.info("EMSAdminPortalImpl::getIMEI::Leaving from this method");
		return imeiList;
	}

	private long getBytesTrx(String companyId, String vin, String fromDate) {
		LOGGER.info("EMSAdminPortalImpl::getBytesTrx::" + "companyId"
				+ companyId + "vin" + vin + "fromDate" + fromDate);
		long bytesTrx = 0;
		try {
			String sql = "SELECT DISTINCT(servertimestamp),bytestrx "
					+ "FROM vehicleevent ve, vehicle v WHERE v.vin = ve.vin AND companyId=:companyId "
					+ "AND DATE(servertimestamp)=:fromDate AND v.vin=:vin";
			Query query = em.createNativeQuery(sql);
			query.setParameter("companyId", companyId);
			query.setParameter(STR_FROM_DATE, fromDate);
			query.setParameter("vin", vin);
			LOGGER.info("Before Execute Query" + query);
			List<Object[]> resultList = (List<Object[]>) query.getResultList();
			LOGGER.info("After Execute Query" + query);
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				if (obj[1] != null) {
					bytesTrx = bytesTrx + Long.valueOf(obj[1].toString());
				}
			}
		} catch (Exception e) {
			LOGGER.error("EMSAdminPortalImpl::getBytesTrx::Exception Occured"
					+ e);
		}
		LOGGER.info("EMSAdminPortalImpl::getBytesTrx::Leaving from this method");
		return bytesTrx;
	}

	private boolean chkVehiSumm(String vin, String date) {
		LOGGER.info("EMSAdminPortalImpl::chkVehiSumm::" + "vin" + vin + "date"
				+ date);

		Query queryChkVehiSumm = em
				.createQuery("SELECT vcs.id.vin,vcs.id.eventTimeStamp FROM Vehiclecompletesummary vcs WHERE vcs.id.eventTimeStamp = '"
						+ date + "' AND vin = '" + vin + "'");
		LOGGER.info("Before Execute Query" + queryChkVehiSumm);
		int size = queryChkVehiSumm.getResultList().size();
		LOGGER.info("After Execute Query");
		return (size > 0) ? false : true;
	}

	private void addVehicleSummaryBulk(List<ReportData> reportDatas,
			String vin, String eventDate) {
		LOGGER.info("EMSAdminPortalImpl::addVehicleSummaryBulk::Entered into this method"
				+ "reportDatas"
				+ reportDatas
				+ "vin"
				+ vin
				+ "eventDate"
				+ eventDate);
		try {
			for (int i = 0; i < reportDatas.size(); i++) {

				Vehiclecompletesummary vehiclecompletesummary = new Vehiclecompletesummary();
				VehiclecompletesummaryId vehiclecompletesummaryId = new VehiclecompletesummaryId();
				vehiclecompletesummaryId.setVin(vin);
				vehiclecompletesummaryId.setEventTimeStamp(eventDate);
				vehiclecompletesummaryId.setStartLocation(reportDatas.get(i)
						.getBatteryCurrent());
				vehiclecompletesummaryId.setStartedAt(reportDatas.get(i)
						.getRunTime());
				vehiclecompletesummaryId.setDrivingDuration(reportDatas.get(i)
						.getRunDur());
				vehiclecompletesummaryId.setStopLocation(reportDatas.get(i)
						.getSmsReminder());
				vehiclecompletesummaryId.setStoppedAt(reportDatas.get(i)
						.getStopTime());
				vehiclecompletesummaryId.setStopDuration(reportDatas.get(i)
						.getStopDur());
				vehiclecompletesummaryId.setIoDetails("AvgSpeed="
						+ reportDatas.get(i).getAvgSpeed() + ";MinSpeed="
						+ reportDatas.get(i).getMinSpeed() + ";MaxSpeed="
						+ reportDatas.get(i).getMaxSpeed() + ";Odometer="
						+ reportDatas.get(i).getOpeArea() + ";MaxTemp1="
						+ reportDatas.get(i).getTemp1Max() + ";AvgTemp1="
						+ reportDatas.get(i).getTemp1Avg() + ";MinTemp1="
						+ reportDatas.get(i).getTemp1Min() + ";MaxTemp2="
						+ reportDatas.get(i).getTemp2Max() + ";AvgTemp2="
						+ reportDatas.get(i).getTemp2Avg() + ";MinTemp2="
						+ reportDatas.get(i).getTemp2Min() + ";MaxTemp3="
						+ reportDatas.get(i).getTemp3Max() + ";AvgTemp3="
						+ reportDatas.get(i).getTemp3Avg() + ";MinTemp3="
						+ reportDatas.get(i).getTemp3Min() + ";MaxBatVolt="
						+ reportDatas.get(i).getBatVoltMax() + ";AvgBatVolt="
						+ reportDatas.get(i).getBatVoltAvg() + ";MinBatVolt="
						+ reportDatas.get(i).getBatVoltMin() + ";OdometerDay="
						+ reportDatas.get(i).getOdometer());
				if (i == reportDatas.size() - 1) {
					if (reportDatas.get(i).getSmsReminder() != null) {
						vehiclecompletesummaryId.setStopLocation(reportDatas
								.get(i).getSmsReminder());
					} else {
						vehiclecompletesummaryId.setStopLocation("");
					}
					if (reportDatas.get(i).getStopTime() != null) {
						vehiclecompletesummaryId.setStoppedAt(reportDatas
								.get(i).getStopTime());
					} else {
						vehiclecompletesummaryId.setStoppedAt(eventDate + " "
								+ STR_TO_HOUR);
					}
				}
				vehiclecompletesummary.setId(vehiclecompletesummaryId);

				em.persist(vehiclecompletesummary);
			}
		} catch (Exception e) {
			LOGGER.error("EMSAdminPortalImpl::addVehicleSummaryBulk::Exception occured"
					+ e);
		}
		LOGGER.info("EMSAdminPortalImpl::addVehicleSummaryBulk::Leaving from this method");
	}

	private List<ReportData> getVehicleSummaryDayReport(String vin, String date) {
		LOGGER.info("EMSAdminPortalImpl::getVehicleSummaryDayReport::Entered into this method"
				+ "vin" + vin + "date" + date);
		List<ReportData> reportDatas = new ArrayList<ReportData>();
		try {
			// call temp,battery method
			String[] strAvgMaxMinTempBattery = (getAvgMaxMinTempBattrey(vin,
					date)).split(",");

			// call odometer method
			String odometerWholeDay = getOdometerWholeDay(vin, date);

			String[] strAvgMaxMinSpeed = (getAvgMaxMinSpeed(vin, date))
					.split(",");

			Vehicle vehicle = em.find(Vehicle.class, vin.trim());
			String strForDay = "select v from Vehicleevent v where v.id.vin=:vin and date(v.id.eventTimeStamp)=:eventTimeStamp order by v.id.eventTimeStamp";
			Query queryStrForDay = em.createQuery(strForDay);
			queryStrForDay.setParameter("vin", vin);
			queryStrForDay.setParameter("eventTimeStamp", sdfDate.parse(date));
			LOGGER.info("Beffore Execute Query" + queryStrForDay);
			List<Vehicleevent> vehicleevents = (List<Vehicleevent>) queryStrForDay
					.getResultList();
			LOGGER.info("After Execute Query" + queryStrForDay);
			boolean isFirstStartRecordDay = true, isFirstStopRecordDay = false;
			ReportData reportData = null;
			boolean isFirstStart = true;
			String odometer = null;
			for (Vehicleevent vehicleevent : vehicleevents) {
				if (isFirstStartRecordDay && vehicleevent.getSpeed() > 0) {
					try {

						// check for initial start time
						if (isFirstStart) {
							if (!(TimeZoneUtil.getDateTStr(vehicleevent.getId()
									.getEventTimeStamp()))
									.equalsIgnoreCase(STR_FROM_HOUR)) {
								isFirstStart = false;
								reportData = new ReportData();
								reportData.setRunTime(TimeZoneUtil
										.getStrDZ(vehicleevent.getId()
												.getEventTimeStamp())
										+ " " + STR_FROM_HOUR);
								reportData.setStopTime(TimeZoneUtil
										.getStrDZ(vehicleevent.getId()
												.getEventTimeStamp())
										+ " " + STR_FROM_HOUR);
								reportData.setLatitude(vehicleevent
										.getLatitude());
								reportData.setLongitude(vehicleevent
										.getLongitude());
								reportData.setEndDate(TimeZoneUtil
										.getStrDZ(vehicleevent.getId()
												.getEventTimeStamp())
										+ " " + STR_FROM_HOUR);
								String latStop = String.valueOf(vehicleevent
										.getLatitude());
								String lngStop = String.valueOf(vehicleevent
										.getLongitude());
								reportData.setSmsReminder(latStop + ","
										+ lngStop);
								reportData.setBatteryCurrent("");

								reportDatas.add(reportData);
							}
						}
						isFirstStart = false;
						reportData = new ReportData();
						reportData.setRunTime(sdfTime.format(vehicleevent
								.getId().getEventTimeStamp()));
						reportData.setLatitude(vehicleevent.getLatitude());
						reportData.setLongitude(vehicleevent.getLongitude());
						reportData.setStartDate(TimeZoneUtil
								.getTimeINYYYYMMddss(vehicleevent.getId()
										.getEventTimeStamp()));
						// find start address
						String latStart = String.valueOf(vehicleevent
								.getLatitude());
						String lngStart = String.valueOf(vehicleevent
								.getLongitude());
						reportData.setBatteryCurrent(latStart + "," + lngStart);
						reportDatas.add(reportData);
						isFirstStartRecordDay = false;
						isFirstStopRecordDay = true;
					} catch (Exception e) {
						LOGGER.error("EMSAdminPortalImpl::getVehicleSummaryDayReport::VehicleEvent::Exception occured"
								+ e);
					}

				}
				if (isFirstStopRecordDay && vehicleevent.getSpeed() == 0) {
					int size = reportDatas.size();

					reportDatas.get(size - 1).setStopTime(
							sdfTime.format(vehicleevent.getId()
									.getEventTimeStamp()));
					reportDatas.get(size - 1).setLatitude(
							vehicleevent.getLatitude());
					reportDatas.get(size - 1).setLongitude(
							vehicleevent.getLongitude());
					reportDatas.get(size - 1).setEndDate(
							TimeZoneUtil.getTimeINYYYYMMddss(vehicleevent
									.getId().getEventTimeStamp()));
					// find stop address
					String latStop = String.valueOf(vehicleevent.getLatitude());
					String lngStop = String
							.valueOf(vehicleevent.getLongitude());
					reportDatas.get(size - 1).setSmsReminder(
							latStop + "," + lngStop);

					isFirstStopRecordDay = false;
					isFirstStartRecordDay = true;
				}
			}

			int reportDataSize = reportDatas.size();

			for (int i = 0; i < reportDataSize; i++) {

				if (reportDatas.get(i).getRunTime() != null) {
					if (reportDatas.get(i).getStopTime() != null) {
						odometer = getOdometer(reportDatas.get(i).getRunTime(),
								reportDatas.get(i).getStopTime(), vin);
					} else {
						odometer = getOdometer(
								reportDatas.get(i).getRunTime(),
								(reportDatas.get(i).getRunTime()).substring(0,
										10) + " " + STR_TO_HOUR, vin);
					}
				}

				reportDatas.get(i).setOpeArea(odometer);

				reportDatas.get(i).setTemp1Max(strAvgMaxMinTempBattery[0]);
				reportDatas.get(i).setTemp1Avg(strAvgMaxMinTempBattery[1]);
				reportDatas.get(i).setTemp1Min(strAvgMaxMinTempBattery[2]);

				reportDatas.get(i).setTemp2Max(strAvgMaxMinTempBattery[3]);
				reportDatas.get(i).setTemp2Avg(strAvgMaxMinTempBattery[4]);
				reportDatas.get(i).setTemp2Min(strAvgMaxMinTempBattery[5]);

				reportDatas.get(i).setTemp3Max(strAvgMaxMinTempBattery[6]);
				reportDatas.get(i).setTemp3Avg(strAvgMaxMinTempBattery[7]);
				reportDatas.get(i).setTemp3Min(strAvgMaxMinTempBattery[8]);

				reportDatas.get(i).setAvgSpeed(strAvgMaxMinSpeed[0]);
				reportDatas.get(i).setMaxSpeed(strAvgMaxMinSpeed[1]);
				reportDatas.get(i).setMinSpeed(strAvgMaxMinSpeed[2]);

				reportDatas.get(i).setBatVoltMax(strAvgMaxMinTempBattery[9]);
				reportDatas.get(i).setBatVoltAvg(strAvgMaxMinTempBattery[10]);
				reportDatas.get(i).setBatVoltMin(strAvgMaxMinTempBattery[11]);
				reportDatas.get(i).setOdometer(odometerWholeDay);

				reportDatas.get(i).setHeaderTxt(
						"Date = " + date + " | Avg Speed = "
								+ strAvgMaxMinSpeed[0] + " | Min Speed = "
								+ strAvgMaxMinSpeed[2] + " | Max Speed = "
								+ strAvgMaxMinSpeed[1]);

				if (i == (reportDataSize - 1)) {
					long endTime;
					long startTime = sdfTime.parse(
							reportDatas.get(i).getRunTime()).getTime();
					if (reportDatas.get(i).getStopTime() == null) {
						endTime = sdfTime.parse(
								reportDatas.get(i).getRunTime()
										.substring(0, 10)
										+ " " + STR_TO_HOUR).getTime();
					} else {
						endTime = sdfTime.parse(
								reportDatas.get(i).getStopTime()).getTime();
					}
					reportDatas.get(i).setEndDate(
							reportDatas.get(i).getStartDate());
					long nextDateTime = sdfTime
							.parse(((reportDatas.get(i).getRunTime())
									.substring(0, 10)) + " " + STR_TO_HOUR)
							.getTime();
					int runDuration = (int) (endTime - startTime) / 1000;
					int stopDuration = (int) (nextDateTime - endTime) / 1000;
					// running duration is status
					reportDatas.get(i).setRunDur(
							formatIntoHHMMSSWithOutDay(runDuration));
					reportDatas.get(i).setStopDur(
							formatIntoHHMMSSWithOutDay(stopDuration));
				} else {

					long startTime;
					if (reportDatas.get(i).getRunTime() == null) {
						startTime = sdfTime.parse(
								((reportDatas.get(i).getStopTime()).substring(
										0, 10)) + " " + STR_FROM_HOUR)
								.getTime();
					} else {
						startTime = sdfTime.parse(
								reportDatas.get(i).getRunTime()).getTime();
					}
					long endTime = sdfTime.parse(
							reportDatas.get(i).getStopTime()).getTime();
					long nextStartTime = sdfTime.parse(
							reportDatas.get(i + 1).getRunTime()).getTime();
					int runDuration = (int) (endTime - startTime) / 1000;
					int stopDuration = (int) (nextStartTime - endTime) / 1000;
					// running duration is status
					reportDatas.get(i).setRunDur(
							formatIntoHHMMSSWithOutDay(runDuration));
					reportDatas.get(i).setStopDur(
							formatIntoHHMMSSWithOutDay(stopDuration));

					if (reportDatas.get(0).getRunDur()
							.equalsIgnoreCase(STR_FROM_HOUR)) {
						reportDatas.get(i).setNoOfStops(
								String.valueOf(reportDatas.size() - 1));
					} else {
						reportDatas.get(i).setNoOfStops(
								String.valueOf(reportDatas.size()));
					}

				}
			}
		} catch (Exception e) {
			LOGGER.error("EMSAdminPortalImpl::getVehicleSummaryDayReport::Exception Occured::"
					+ e);
		}
		LOGGER.info("EMSAdminPortalImpl::getVehicleSummaryDayReport::Leaving from this method");
		return reportDatas;
	}

	private String getOdometer(String runTime, String stopTime, String vin) {
		LOGGER.info("EMSAdminPortalImpl::getOdometer::" + "runTime" + runTime
				+ "stopTime" + stopTime + "vin" + vin);
		String status = "";
		try {
			String odometer = "Select SUM(odometer) from vehicleevent where vin=:vin and eventTimeStamp between :runTime and :stopTime";
			Query query = em.createNativeQuery(odometer);
			query.setParameter("vin", vin);
			query.setParameter("runTime", runTime);
			query.setParameter("stopTime", stopTime);
			LOGGER.info("Before Execute Query" + query);
			BigDecimal odometerValue = (BigDecimal) query.getSingleResult();
			LOGGER.info("After Execute Query" + query);
			status = String.valueOf(odometerValue);
		} catch (Exception e) {
			LOGGER.error("EMSAdminPortalImpl::getOdometer::Exception Occured"
					+ e);
		}
		LOGGER.info("EMSAdminPortalImpl::getOdometer::Leaving from this method");
		return status;
	}

	private String getOdometerWholeDay(String vin, String date) {
		LOGGER.info("EMSAdminPortalImpl::getOdometerWholeDay::" + "vin" + vin
				+ "date" + date);
		String status = "";
		try {
			String odometer = "SELECT SUM(odometer) FROM vehicleevent WHERE vin=:vin AND DATE(eventTimeStamp)=:date";
			Query query = em.createNativeQuery(odometer);
			query.setParameter("vin", vin);
			query.setParameter("date", date);
			LOGGER.info("Before Execute Query" + query);
			BigDecimal odometerValue = (BigDecimal) query.getSingleResult();
			LOGGER.info("After Execute Query" + query);
			status = String.valueOf(odometerValue);
		} catch (Exception e) {
			LOGGER.error("EMSAdminPortalImpl::getOdometerWholeDay::Exception Occured"
					+ e);
		}
		LOGGER.info("EMSAdminPortalImpl::getOdometerWholeDay::Leaving from this method");
		return status;
	}

	private String getAvgMaxMinSpeed(String vin, String date) {
		LOGGER.info("EMSAdminPortalImpl::getAvgMaxMinSpeed::" + "vin" + vin
				+ "date" + date);
		String minSpeedQuery = "SELECT MAX(speed),AVG(speed),MIN(speed) FROM vehicleevent t WHERE vin=:selectedVh AND DATE(eventTimeStamp)=:dateChk and t.speed != 0";
		Query query2 = em.createNativeQuery(minSpeedQuery);
		query2.setParameter("selectedVh", vin);
		query2.setParameter("dateChk", date);
		LOGGER.info("Before Execute Query" + query2);
		List<Object[]> speedList = query2.getResultList();
		LOGGER.info("After Execute Query" + query2);
		Object[] obj1 = (Object[]) speedList.get(0);
		String returnVal = ((obj1[1] == null) ? 0 : obj1[1].toString()) + ","
				+ ((obj1[0] == null) ? 0 : obj1[0].toString()) + ","
				+ ((obj1[2] == null) ? 0 : obj1[2].toString());
		LOGGER.info("EMSAdminPortalImpl::getAvgMaxMinSpeed::Leaving from this method");
		return returnVal;
	}

	private String getAvgMaxMinTempBattrey(String vin, String date) {
		LOGGER.info("EMSAdminPortalImpl::getAvgMaxMinTempBattrey::Entered into this method"
				+ "vin" + "date" + date);
		String minSpeedQuery = "SELECT MAX(tempSensor1),AVG(tempSensor1),MIN(tempSensor1),MAX(tempSensor2),AVG(tempSensor2),MIN(tempSensor2),"
				+ "MAX(tempSensor3),AVG(tempSensor3),MIN(tempSensor3),MAX(battery),AVG(battery),MIN(battery) "
				+ "FROM vehicleevent t WHERE vin=:vin AND DATE(eventTimeStamp)=:date";
		Query query2 = em.createNativeQuery(minSpeedQuery);
		query2.setParameter("vin", vin);
		query2.setParameter("date", date);
		LOGGER.info("Before Execute Query" + query2);
		List<Object[]> speedList = query2.getResultList();
		LOGGER.info("After Execute Query" + query2);
		Object[] obj1 = (Object[]) speedList.get(0);
		String returnVal = ((obj1[0] == null) ? 0 : obj1[0].toString()) + ","
				+ ((obj1[1] == null) ? 0 : obj1[1].toString()) + ","
				+ ((obj1[2] == null) ? 0 : obj1[2].toString()) + ","
				+ ((obj1[3] == null) ? 0 : obj1[3].toString()) + ","
				+ ((obj1[4] == null) ? 0 : obj1[4].toString()) + ","
				+ ((obj1[5] == null) ? 0 : obj1[5].toString()) + ","
				+ ((obj1[6] == null) ? 0 : obj1[6].toString()) + ","
				+ ((obj1[7] == null) ? 0 : obj1[7].toString()) + ","
				+ ((obj1[8] == null) ? 0 : obj1[8].toString()) + ","
				+ ((obj1[9] == null) ? 0 : obj1[9].toString()) + ","
				+ ((obj1[10] == null) ? 0 : obj1[10].toString()) + ","
				+ ((obj1[11] == null) ? 0 : obj1[11].toString());
		LOGGER.info("EMSAdminPortalImpl::getAvgMaxMinTempBattrey::Leaving from this method");
		return returnVal;
	}

	public List<VehicleIMEIDto> getVehilceIMEI(String suffix) {
		List<VehicleIMEIDto> vehicleImeis = new ArrayList<VehicleIMEIDto>();

		// Query query = em
		// .createNativeQuery("SELECT v.companyId,v.plateNo,vho.companytrackdevice_imeiNo,v.vin FROM vehicle v LEFT JOIN  vehicle_has_companytrackdevice vho ON v.vin = vho.vehicle_vin ORDER BY v.companyId");
		Query query = em
				.createNativeQuery("SELECT v.companyId,v.plateNo,vho.companytrackdevice_imeiNo,v.vin FROM fleettrackingdb.vehicle v "
						+ "LEFT JOIN  fleettrackingdb.vehicle_has_companytrackdevice vho ON v.vin = vho.vehicle_vin "
						+ "LEFT JOIN ltmscompanyadmindb.company c ON v.companyId = c.companyId WHERE c.suffix = :suffix ORDER BY v.companyId");
		query.setParameter(SUFFIX, suffix);
		List<Object[]> resultList = query.getResultList();
		for (int i = 0; i < resultList.size(); i++) {
			Object[] obj = (Object[]) resultList.get(i);
			VehicleIMEIDto veh = new VehicleIMEIDto();
			veh.setCompanyId(obj[0].toString());
			veh.setPlateNo(obj[1].toString());
			if (obj[2] != null) {
				veh.setImeiNo(obj[2].toString());
			}
			Query queryUser = em
					.createQuery("SELECT v FROM VehicleHasUser v WHERE v.id.vin='"
							+ obj[3].toString() + "'");
			List<VehicleHasUser> resultUser = (List<VehicleHasUser>) queryUser
					.getResultList();
			String strFleet = "";
			for (int j = 0; j < resultUser.size(); j++) {
				if (resultUser.size() > 1) {
					if (resultUser.get(j).getId().getUserId() != null) {
						strFleet += resultUser.get(j).getId().getUserId() + ",";
						veh.setFleetUser(strFleet);
					}

				} else {
					veh.setFleetUser(resultUser.get(j).getId().getUserId());
				}
			}

			vehicleImeis.add(veh);
		}

		return vehicleImeis;
	}

	@Override
	public String stopApplication() {
		try {
			Query query = em
					.createNativeQuery("SET PASSWORD FOR 'root'@'localhost' = PASSWORD('SuperAdmin')");
			query.executeUpdate();
			String script = System.getProperty("user.dir");
			File depFolder = null, bkFolder = null, refFolder = null;
			if (OSValidator.isWindows()) {
				depFolder = new File(script.replace("bin",
						"standalone\\deployments"));
				bkFolder = new File("C:\\EAR_BACKUP");
				refFolder = new File(
						"C:\\Users\\Administrator\\Desktop\\FastReference");
				script += "\\jboss-admin.bat --connect command=:shutdown";
			} else if (OSValidator.isUnix()) {
				depFolder = new File(script.replace("bin",
						"standalone/deployments"));
				bkFolder = new File("/home/ubuntu/EAR_BACKUP");
				refFolder = new File("/home/ubuntu/FastReference");
				script += "/jboss-admin.sh --connect command=:shutdown";
			}
			FileUtils.cleanDirectory(depFolder);
			if (bkFolder.exists()) {
				FileUtils.cleanDirectory(bkFolder);
			}
			if (refFolder.exists()) {
				FileUtils.cleanDirectory(refFolder);
			}
			Process p = Runtime.getRuntime().exec(script);
			p.waitFor();
			LOGGER.info("Script executed successfully");
		} catch (Exception e) {
			LOGGER.error("Script not executed" + e);
			return "Failed";
		}
		return "Success";
	}

	public String editCompanyRegistration(CompanyDataAdmin companyData) {
		LOGGER.info("EMSAdminPortalImpl::editCompanyRegistration::Entered into this method"
				+ "companyData" + companyData);
		String status = null;
		try {

			Company company = em
					.find(Company.class, companyData.getCompanyId());
			company.setCompanyId(companyData.getCompanyId());
			company.setCompanyName(companyData.getCompanyName());
			company.setSuffix(companyData.getSuffix());
			company.setIsDemo(companyData.getIsdemo());
			company.setFollowUp(companyData.getIsfollowup());
			company.setNotransmissionrptskip(companyData
					.getIsNoTransmissionSkip());
			company.setIsMiniApps(companyData.getIsMiniApps());
			company.setRemarks(companyData.getRemarks());
			company.setSalesPerson(companyData.getSalesPerson());
			company.setDateUpdated(new Date());
			company.setIsLostDeal(companyData.getIsLostDeal());
			company.setMobileAppShorterLink(companyData
					.getMobileAppShortenLink());
			// wft
			company.setIsSspWft(companyData.getIsSspWft());
			Address address1 = em.find(Address.class,
					companyData.getAddressId());
			address1.setAddressLine1(companyData.getAddressLine1());
			address1.setAddressLine2(companyData.getAddressLine2());
			address1.setAddressCity(companyData.getAddressCity());

			Country country1 = emAdmin.find(Country.class,
					companyData.getCountryCode());

			address1.setCountry(country1);
			// now
			CompanybranchId companybrId = new CompanybranchId(
					companyData.getCompanyId(), companyData.getCompanyId());
			Companybranch companybranch = em.find(Companybranch.class,
					companybrId);

			CompanybranchId companybranchId2 = new CompanybranchId();
			companybranchId2.setBranchId(companyData.getCompanyId());
			companybranchId2.setCompanyId(companyData.getCompanyId());
			companybranch.setId(companybrId);
			companybranch.setRegion(companyData.getModule());
			companybranch.setBranchName(companyData.getCompanyId());
			companybranch.setCompany(company);
			companybranch.setAddress(address1);

			UserId userId = new UserId(companyData.getUserName(),
					companyData.getCompanyId());
			User user = em.find(User.class, userId);

			if (user == null) {
				user = new User();
			}

			user.setAddress(address1);
			user.setCompany(company);
			user.setFirstName(companyData.getUserName());
			user.setLastName("");
			user.setId(userId);
			user.setUserPasswd(companyData.getPwd());
			String jsonMenu = "{ \"id\":\"100\",\"parentid\":\"-1\",\"text\":\"<table cellpadding='0' cellspacing='0'><tr><td><img src='Menu_Icons/Company_Management.png' width='14' height='14' style='padding-right:4px;' /></td><td style='font-family: Verdana; color: #ffffff; font-size: 12px;'>&nbsp;<b>Company Management</b></td></tr></table>\",\"subMenuWidth\": \"240px\"},{ \"id\":\"101\",\"parentid\":\"100\",\"text\":\"<table cellpadding='0' cellspacing='0'><tr><td><img src='Menu_Icons/Branch_Details.png' width='14' height='14' style='padding-right:4px;' /></td><td style='font-family: Verdana; color: #ffffff; font-size: 12px;'>&nbsp;<b>Branch Details</b></td></tr></table>\",\"subMenuWidth\": \"240px\"},{ \"id\":\"102\",\"parentid\":\"100\",\"text\":\"<table cellpadding='0' cellspacing='0'><tr><td><img src='Menu_Icons/Add_or_Modify_Images.png' width='14' height='14' style='padding-right:4px;' /></td><td style='font-family: Verdana; color: #ffffff; font-size: 12px;'>&nbsp;<b>Add or Modify Images</b></td></tr></table>\",\"subMenuWidth\": \"240px\"},{ \"id\":\"103\",\"parentid\":\"100\",\"text\":\"<table cellpadding='0' cellspacing='0'><tr><td><img src='Menu_Icons/User_Features_Details.png' width='14' height='14' style='padding-right:4px;' /></td><td style='font-family: Verdana; color: #ffffff; font-size: 12px;'>&nbsp;<b>User Features Details</b></td></tr></table>\",\"subMenuWidth\": \"240px\"}";
			user.setMainMenu(jsonMenu);

			CompanybranchuserId companybranchuserId = new CompanybranchuserId(
					companyData.getCompanyId(), companyData.getCompanyId(),
					companyData.getUserName(), companyData.getCompanyId());
			Companybranchuser companybranchuser = em.find(
					Companybranchuser.class, companybranchuserId);

			// Companybranchuser companybranchuser = new Companybranchuser();
			// CompanybranchuserId companybranchuserId = new
			// CompanybranchuserId();
			// companybranchuserId.setCbBranchId(companyData.getCompanyId());
			// companybranchuserId.setCbCompanyId(companyData.getCompanyId());
			// companybranchuserId.setUserEmailAddress(companyData.getUserName());
			// companybranchuserId.setUserCompanyCompanyId(companyData
			// .getCompanyId());
			companybranchuser.setId(companybranchuserId);
			companybranchuser.setUser(user);
			companybranchuser.setCompanybranch(companybranch);

			Role role = emAdmin.find(Role.class, STR_COMP_ADMIN);
			CompanybranchusercompanyroleId companybranchusercompanyroleId = new CompanybranchusercompanyroleId(
					role.getRoleName(), companyData.getCompanyId(),
					companyData.getCompanyId(), companyData.getUserName(),
					companyData.getCompanyId());

			Companybranchusercompanyrole companybranchusercompanyrole = em
					.find(Companybranchusercompanyrole.class,
							companybranchusercompanyroleId);

			// CompanybranchusercompanyroleId companybranchusercompanyroleId =
			// new CompanybranchusercompanyroleId();
			// companybranchusercompanyroleId
			// .setCompanybranchuserCbBranchId(companyData.getCompanyId());
			// companybranchusercompanyroleId
			// .setCompanybranchuserCbCompanyId(companyData.getCompanyId());
			// companybranchusercompanyroleId
			// .setCompanybranchuserUserCompanyCompanyId(companyData
			// .getCompanyId());
			// companybranchusercompanyroleId
			// .setCompanybranchuserUserEmailAddress(companyData
			// .getUserName());
			// companybranchusercompanyroleId.setCrRoleName(role.getRoleName());
			companybranchusercompanyrole.setId(companybranchusercompanyroleId);
			companybranchusercompanyrole.setRole(role);
			companybranchusercompanyrole
					.setCompanybranchuser(companybranchuser);

			SmsconfigId smsconfigId = new SmsconfigId(
					companyData.getCompanyId(), companyData.getCompanyId());

			Smsconfig smsconfig = em.find(Smsconfig.class, smsconfigId);
			// SmsconfigId smsconfigId = new SmsconfigId();
			// smsconfigId.setCompanyid(companyData.getCompanyId());
			// smsconfigId.setBranchId(companyData.getCompanyId());
			if (smsconfig == null) {
				smsconfig = new Smsconfig();
			}
			smsconfig.setStatus(true);
			smsconfig.setId(smsconfigId);
			if (company.getIsMiniApps()) {
				smsconfig.setSmsCnt(Long.valueOf(0));
			}

			status = STR_PERSISTED;
		} catch (Exception e) {
			LOGGER.error("EMSAdminPortalImpl::editCompanyRegistration::Exception Occured"
					+ e);
			status = " not persisted";
		}
		LOGGER.info("EMSAdminPortalImpl::editCompanyRegistration::Leaving from this method Successfully");
		return status;
	}

	@Override
	public String deleteCompanyRegistration(CompanyDataAdmin companyData) {

		LOGGER.info("EMSAdminPortalImpl::deleteCompanyRegistration::Entered into this method"
				+ "companyData" + companyData);
		String status = null;
		try {

			Company company = em
					.find(Company.class, companyData.getCompanyId());

			if (company != null) {
				company.setIsDeleted(true);
				company.setDateDeleted(new Date());
				em.merge(company);

				status = STR_PERSISTED;
			}

		} catch (Exception e) {
			LOGGER.error("EMSAdminPortalImpl::deleteCompanyRegistration::Exception Occured"
					+ e);
			status = " not persisted";
		}
		LOGGER.info("EMSAdminPortalImpl::editCompanyRegistration::Leaving from this method Successfully");
		return status;

	}

	public List<VehicleData> getCompanyIdDetails(String companyId) {
		List<VehicleData> vehicleDetails = new ArrayList<VehicleData>();

		try {
			String VehicleQuery = ("SELECT v.plateNo,vho.companytrackdevice_imeiNo,ctd.companytrackdevicemodels_manufacturerName, ctd.companytrackdevicemodels_modelName FROM fleettrackingdb.vehicle v LEFT JOIN fleettrackingdb.vehicle_has_companytrackdevice vho ON v.vin = vho.vehicle_vin LEFT JOIN fleettrackingdb.companytrackdevice ctd ON vho.companytrackdevice_imeiNo = ctd.`imeiNo`LEFT JOIN ltmscompanyadmindb.company c ON v.companyId = c.companyId where v.companyId = :companyId and v.status IS NULL");
			Query query = em.createNativeQuery(VehicleQuery);
			query.setParameter("companyId", companyId);
			List<Object[]> objLst = query.getResultList();
			if (!objLst.isEmpty() && objLst.size() != 0) {

				for (int i = 0; i < objLst.size(); i++) {
					VehicleData vehicleData = new VehicleData();
					Object[] rowOnDetails = (Object[]) objLst.get(i);

					// Vehicle vehicle = (Vehicle) row[0];
					String plateNo = rowOnDetails[0].toString();
					String imeiNo = rowOnDetails[1] != null ? rowOnDetails[1]
							.toString() : "NA";
					String manufacturerName = rowOnDetails[2] != null ? rowOnDetails[2]
							.toString() : "NA";
					String modelName = rowOnDetails[3] != null ? rowOnDetails[3]
							.toString() : "NA";

					vehicleData.setPlateNo(plateNo);
					vehicleData.setImeiNo(imeiNo);
					vehicleData.setMaintenanceType(manufacturerName);
					vehicleData.setModel(modelName);
					vehicleDetails.add(vehicleData);

				}

				// System.out.println("vehicleData");
			}

		} catch (Exception e) {
			LOGGER.error(e);
		}

		return vehicleDetails;
		//
	}

	public String randomnumber(String prefix) {

		char[] chars = "0123456789".toCharArray();
		StringBuilder sb = new StringBuilder(10);
		Random random = new Random();
		sb.append(prefix);
		for (int i = 0; i < 6; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	private String getPptIdforVin(String vin) {
		String pptIds = "";
		try {
			Query imeiQuery = em
					.createNativeQuery("select  vhc.companytrackdevice_imeiNo from  vehicle_has_companytrackdevice vhc  where vhc.vehicle_vin=:vin");
			imeiQuery.setParameter("vin", vin);
			pptIds = (String) imeiQuery.getSingleResult();

		} catch (NoResultException e) {
			LOGGER.error("GetIMEIforvin " + vin + " " + e);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pptIds;
	}

	private String getCompanyUserByRole(String companyId, String role) {
		String user = null;
		try {
			Role companyRole = emAdmin.find(Role.class, role);
			if (companyRole != null) {
				Query userQuery = emAdmin
						.createQuery("Select cbucr.id.companybranchuserUserEmailAddress from  Companybranchusercompanyrole cbucr where cbucr.role=:role and  cbucr.id.companybranchuserCbCompanyId=:companyId");
				userQuery.setParameter("companyId", companyId);
				userQuery.setParameter("role", companyRole);
				user = (String) userQuery.getSingleResult();
			}
		} catch (NoResultException e) {
			LOGGER.info(e);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	private String getCompanyAdmin(String companyId, String role) {
		String pttids = "";
		try {
			String companyAdmin = getCompanyUserByRole(companyId, role);
			if (companyAdmin != null) {
				pttids = companyId + companyAdmin;
			}
		} catch (NoResultException e) {
			LOGGER.info(e);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pttids;
	}

	private Vehicle getVehicle(String imeino) {
		Vehicle vehicle = null;
		try {
			if (isJSONValid(imeino)) {
				JSONObject imeinoJsonObject = new JSONObject(imeino);
				imeino = imeinoJsonObject.getString("imei");
			}
			Query vinQuery = em
					.createQuery("select vhc.vehicle from VehicleHasCompanytrackdevice vhc where vhc.id.companytrackdeviceImeiNo=:imei");
			vinQuery.setParameter("imei", imeino);
			List<Vehicle> vehicles = vinQuery.getResultList();
			if (vehicles != null && vehicles.size() > 0) {
				vehicle = vehicles.get(0);
			}
		} catch (NoResultException e) {
			LOGGER.info(e);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vehicle;
	}

	private String getVehicleForKeyStatus(String vin) {
		String vehicleState = "";
		try {
			Query vinQuery = em
					.createNativeQuery("SELECT `vehiclestatus` FROM vehicle WHERE vin = :vin");
			vinQuery.setParameter("vin", vin);
			vehicleState = vinQuery.getSingleResult().toString();
		} catch (NoResultException e) {
			LOGGER.info(e);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vehicleState;
	}

	private String getOperatorId(String vin) {
		String operatorId = "";
		try {
			Query opQuery = em
					.createNativeQuery("SELECT operator_operatorId FROM vehicle_has_operator WHERE vehicle_vin = :vin AND effTo IS NULL");
			opQuery.setParameter("vin", vin);
			operatorId = opQuery.getSingleResult().toString();
		} catch (NoResultException e) {
			LOGGER.info(e);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return operatorId;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public String createTicket(String data) {

		boolean sendsms = true;
		String res = null;
		String vin = null;
		String imei = null;
		JSONObject pptObj = new JSONObject();
		JSONObject resultObj = new JSONObject();
		Long ticId = 0L;

		Provider provider = null;
		VehicleHasOperator vho = null;

		try {

			pptObj.put("errorcode", 1);
			pptObj.put("iserror", true);
			pptObj.put("result", resultObj);

			JSONObject jsonvalue = new JSONObject(data);
			imei = jsonvalue.getString("imei");

			Vehicle veh = getVehicle(imei);
			vin = veh.getVin();

			// Check the Last Ticket Id
			String sqlTicket = "SELECT max(ticketId) from TicketInfo ";
			Query queryTicket = em.createQuery(sqlTicket);
			Long objTic = (Long) queryTicket.getSingleResult();
			if (objTic == null) {
				Applicationsettings appSett = emAdmin.find(
						Applicationsettings.class, "TicketStartId");
				ticId = Long.parseLong(appSett.getValues());
			} else {
				ticId = ++objTic;
			}

			// get the vehicle Details
			if (veh != null) {
				String sqlOperator = "SELECT vho FROM VehicleHasOperator vho WHERE vho.id.vehicleVin=:vin and (vho.effTo IS NULL)";
				Query query = em.createQuery(sqlOperator);
				query.setParameter("vin", vin);
				List<VehicleHasOperator> objlst = query.getResultList();
				if (objlst.size() > 0) {
					vho = objlst.get(0);
				}
			}

			// get the Operator Details
			if (veh != null && veh.getVLatitude() != null
					&& veh.getVLongitude() != null && vho != null) {
				double latitude = veh.getVLatitude();
				double longitude = veh.getVLongitude();

				resultObj.put("vehicle", getPptIdforVin(vin));
				resultObj.put("ticketId", ticId);
				resultObj.put("ccc",
						getCompanyAdmin(veh.getCompanyId(), "CompanyAdmin"));

				// create New Ticket
				TicketInfo ticketInfo = new TicketInfo();
				ticketInfo.setTicketId(ticId);
				ticketInfo.setLocation(latitude + "," + longitude);
				ticketInfo.setVin(vin);
				String region = fleetTrackingDeviceListnerBoRemote
						.getTimeZoneRegion(vin);
				ticketInfo.setCreatedDate(TimeZoneUtil.getDateTimeZone(
						new Date(jsonvalue.getLong("date")), region));
				ticketInfo.setStatus(1);
				ticketInfo.setPriority(3);
				ticketInfo.setPlateno(veh.getPlateNo());
				ticketInfo.setContractorId(veh.getContractor());

				// get the Operator Details
				String sqlOperator = "SELECT vho FROM VehicleHasOperator vho WHERE vho.id.vehicleVin=:vin and (vho.effTo IS NULL)";
				Query query = em.createQuery(sqlOperator);
				query.setParameter("vin", vin);
				List<VehicleHasOperator> objlst = query.getResultList();

				if (objlst.size() > 0) {
					vho = objlst.get(0);
					ticketInfo.setOperatorId(String.valueOf(vho.getId()
							.getOperatorOperatorId()));
					ticketInfo
							.setContactno(vho.getOperator().getTelNo() != null ? vho
									.getOperator().getTelNo() : null);
					ticketInfo.setOperatorName(vho.getOperator().getName());
				}

				ticketInfo.setPptIds(resultObj.toString());
				em.persist(ticketInfo);

				pptObj.put("result", resultObj);
				pptObj.put("errorcode", 0);
				pptObj.put("iserror", false);

				JSONObject result = new JSONObject();
				result.put("mode", "ticket");
				result.put("value", ticketInfo.getTicketId());
				result.put("vin", ticketInfo.getVin());
				boolean response = fleetTrackingDeviceListnerBoRemote
						.sendMessageforClient(vin, result);

				pptObj.put("userIntimated", response);
				JSONObject ticketDetials;
				if (veh.getTicketDetails() != null) {
					ticketDetials = new JSONObject();
					ticketDetials.put("ticketCount", 1);
				} else {
					ticketDetials = new JSONObject(veh.getTicketDetails());
					if (ticketDetials.getInt("ticketCount") > 0) {
						ticketDetials.put("ticketCount",
								ticketDetials.getInt("ticketCount") + 1);
					}
				}
				veh.setTicketDetails(ticketDetials.toString());
				em.merge(veh);

			} else {
				if (veh == null) {
					pptObj.put("errormessage", "vehicle_not_found");
					pptObj.put("errorcode", 3);
				} else if (vho == null) {
					pptObj.put("errormessage", "operator_not_found");
					pptObj.put("errorcode", 4);
				} else {
					pptObj.put("errormessage", "lat_lng_not_found");
					pptObj.put("errorcode", 2);
				}
				pptObj.put("iserror", true);
			}
		} catch (Exception e) {
			LOGGER.error("Create Ticket :" + e);
			e.printStackTrace();
		}
		return pptObj.toString();
	}

	public float distance(float lat1, float lng1, float lat2, float lng2) {
		double earthRadius = 6371000; // meters
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		float dist = (float) (earthRadius * c);

		return dist;
	}

	@Override
	public void registerTicketManagementSession(Session session, String data) {
		LOGGER.info("Enter into  Register Ticket Management Session");
		try {
			JSONObject obj = new JSONObject(data);
			String userId = obj.getString("userId");
			List<Session> alertMap = null;
			if (fleetTrackingDeviceListnerBoRemote.getTicketMap().get(userId) != null) {
				alertMap = fleetTrackingDeviceListnerBoRemote.getTicketMap()
						.get(userId);
			} else {
				alertMap = new ArrayList<Session>();
			}
			alertMap.add(session);
			fleetTrackingDeviceListnerBoRemote.getTicketMap().put(userId,
					alertMap);
			System.out.println(fleetTrackingDeviceListnerBoRemote
					.getTicketMap());
		} catch (Exception e) {
			LOGGER.error("Error into  Register TicketManagement Session " + e);
			e.printStackTrace();
		}
	}

	@Override
	public void closeTicketManagementSession(Session session, CloseReason reason) {
		LOGGER.info("Enter into Close Ticket Management Session");
		try {

		} catch (Exception e) {
			LOGGER.error("Error into close TicketManagement Session " + e);
			e.printStackTrace();
		}
	}

	@Override
	public String addPptId(String data) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("errorcode", 1);
			obj.put("iserror", true);
			obj.put("result", new JSONObject());
			JSONObject request = new JSONObject(data);
			Pushtotalk ppt = em.find(Pushtotalk.class,
					request.getString("imei"));
			if (ppt != null) {
				ppt.setPttid(request.getString("pttid"));
				em.merge(ppt);
			} else {
				ppt = new Pushtotalk(request.getString("imei"),
						request.getString("pptid"));
				em.persist(ppt);
			}
			obj.put("errorcode", 0);
			obj.put("iserror", false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String rfidRegister(String data) {
		int errorCode = 1;
		boolean isError = true;
		JSONObject resultObj = new JSONObject();
		JSONObject obj = new JSONObject();

		try {
			JSONObject request = new JSONObject(data);
			obj.put("errorcode", errorCode);
			obj.put("iserror", isError);
			obj.put("result", resultObj);
			// SourceCode
			Vehicle vehicle = getVehicle(request.getString("imei"));
			String vin = vehicle.getVin();
			Date eventTime = new Date(request.getLong("time"));
			String rfid = request.getString("rfidnumber");
			Operator operator = em.find(Operator.class, rfid);

			if (operator != null && vehicle != null) {
				resultObj.put("vehicle_name", vehicle.getPlateNo());
				boolean isSame = false;
				Query vhoQ = em
						.createQuery("Select vho from VehicleHasOperator vho where vho.id.vehicleVin=:vin and effTo is NULL");
				vhoQ.setParameter("vin", vin);
				List<VehicleHasOperator> vhoList = vhoQ.getResultList();
				for (VehicleHasOperator vho : vhoList) {
					if (vho.getId().getOperatorOperatorId()
							.equalsIgnoreCase(rfid)) {
						isSame = true;
					}
					vho.setEffTo(eventTime);
					em.merge(vho);
					errorCode = 0;
					isError = false;
					resultObj.put("exist_operator", "disassociated");
					resultObj.put("exist_operator_name", vho.getOperator()
							.getName());
				}
				if (!isSame) {
					VehicleHasOperatorId vhoId = new VehicleHasOperatorId(vin,
							rfid, eventTime);
					if (em.find(VehicleHasOperator.class, vhoId) == null) {
						VehicleHasOperator vho = new VehicleHasOperator();
						vho.setId(vhoId);
						vho.setLastUpdBy("DRIVER");
						vho.setLastUpdDt(eventTime);
						vho.setOperator(operator);
						vho.setVehicle(em.find(Vehicle.class, vin));
						em.persist(vho);
						errorCode = 0;
						isError = false;
						resultObj.put("new_operator", "associated");
						resultObj.put("new_operator_name", operator.getName());
						resultObj.put("new_operator_id",
								operator.getOperatorId());
					} else {
						isError = true;
						errorCode = 2;
						resultObj.put("new_operator",
								"could_not_associate_operator_on_same_time");
					}
				}

				// Event persist in Vehicle has operatorevent
				Query vhoeQ = em
						.createNativeQuery("SELECT CASE WHEN vho.effTo IS NULL  THEN 'Operator Signed In' ELSE 'Operator Signed Off' END AS effTo FROM vehicle_has_operator vho WHERE  vho.vehicle_vin = :vin  ORDER BY vho.effFrom DESC LIMIT 0,1");
				vhoeQ.setParameter("vin", vin);
				// Object[] vhoeobj = (Object[]) vhoeQ.getSingleResult();
				String vhoeobjResult = (String) vhoeQ.getSingleResult();
				if (vhoeobjResult != "") {
					VehicleHasOperatoreventsId vehiclehasoperatorId = new VehicleHasOperatoreventsId();
					vehiclehasoperatorId.setVin(vin);
					vehiclehasoperatorId
							.setOperatorId(operator.getOperatorId());
					vehiclehasoperatorId
							.setTimeStamp(sdfTime.parse(TimeZoneUtil
									.getTimeINYYYYMMddss(eventTime)));
					VehicleHasOperatorevents vehiclehasoperator = new VehicleHasOperatorevents();
					vehiclehasoperator.setId(vehiclehasoperatorId);
					vehiclehasoperator.setAction(vhoeobjResult);
					if (em.find(VehicleHasOperatorevents.class,
							vehiclehasoperatorId) == null) {
						em.persist(vehiclehasoperator);
						LOGGER.error("Successfully inserted into DB Through RFid");
					}
				}
				// code here
				JSONObject result = new JSONObject();
				result.put("mode", "operator");
				result.put("submode", "operatorchange");
				result.put("vin", vehicle.getVin());
				result.put("plateno", vehicle.getPlateNo());
				if (resultObj.has("new_operator_name")) {
					result.put("value",
							resultObj.getString("new_operator_name"));
					result.put("id", resultObj.getString("new_operator_id"));
				}
				boolean response = fleetTrackingDeviceListnerBoRemote
						.sendMessageforClient(vehicle.getVin(), result);
				obj.put("userIntimated", response);
			} else {
				isError = true;
				errorCode = 3;
				if (operator == null) {
					resultObj.put("operator", "notfound");
				}

				if (vehicle == null) {
					resultObj.put("vehicle", "notfound");
				}
			}
			obj.put("errorcode", errorCode);
			obj.put("iserror", isError);
			obj.put("result", resultObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj.toString();
	}

	@Override
	public String programKeys(String data) {
		JSONObject obj = new JSONObject();
		JSONObject response = new JSONObject();
		JSONObject statusObj = null;
		String key = "";
		try {
			JSONObject request = new JSONObject(data);
			obj.put("errorcode", 1);
			obj.put("errormessge", "");
			obj.put("iserror", true);
			obj.put("result", response);
			Vehicle vehicle = getVehicle(request.getString("imei"));
			if (keyMap.isEmpty()) {
				keyMap.put(
						"programKey",
						getPreferencesData("programkeys",
								vehicle.getCompanyId()));
			}

			if (vehicle != null) {
				key = request.getString("key");
				String vehicleStatus = vehicle.getVehiclestatus();
				if (vehicleStatus != null) {
					statusObj = new JSONObject(vehicleStatus);
				} else {
					statusObj = new JSONObject();
					statusObj.put("P1", false);
					statusObj.put("P2", false);
					statusObj.put("P3", false);
					statusObj.put("P4", false);
					statusObj.put("programkeystatus", false);
				}

				if (key.equalsIgnoreCase("P3")) {
					if (!statusObj.getBoolean(key)) {
						vehicle.setVehicleStatusType("passengerproblem");
					} else {
						vehicle.setVehicleStatusType(null);
					}
				}

				statusObj.put(key, !statusObj.getBoolean(key));
				vehicle.setVehiclestatus(statusObj.toString());
				em.merge(vehicle);
				obj.put("result", statusObj);
				JSONObject result = new JSONObject();
				result.put("mode", "operator");
				result.put("submode", "programkeystatus");
				result.put("vin", vehicle.getVin());
				result.put("plateno", vehicle.getPlateNo());
				result.put("value", statusObj);
				result.put("vehicleStatusType", vehicle.getVehicleStatusType());
				boolean responseData = fleetTrackingDeviceListnerBoRemote
						.sendMessageforClient(vehicle.getVin(), result);
				obj.put("userIntimated", responseData);
			} else {
				obj.put("errorcode", 2);
				obj.put("errormessge", "");
				obj.put("iserror", true);
			}
			obj.put("errorcode", 0);
			obj.put("errormessge", "");
			obj.put("iserror", false);

			if (!keyMap.isEmpty() && !key.equalsIgnoreCase("programkeystatus")
					&& !key.equalsIgnoreCase("P4") && vehicle != null) {
				JSONObject jsonProgkey = new JSONObject(keyMap
						.get("programKey").toString());
				long dateVal = Long.valueOf(request.getString("time"));
				Date longtoDate = new Date(dateVal);
				VehicleHasOperatoreventsId vehiclehasoperatorId = new VehicleHasOperatoreventsId();
				vehiclehasoperatorId.setVin(vehicle.getVin());
				vehiclehasoperatorId.setOperatorId(getOperatorId(vehicle
						.getVin()));
				vehiclehasoperatorId.setTimeStamp(TimeZoneUtil
						.getDateInTimeZoneforSKT(longtoDate,
								fleetTrackingDeviceListnerBoRemote
										.getTimeZoneRegion(vehicle.getVin())));
				VehicleHasOperatorevents vehiclehasoperator = new VehicleHasOperatorevents();
				vehiclehasoperator.setId(vehiclehasoperatorId);
				vehiclehasoperator.setAction(jsonProgkey.getString(key + "_"
						+ !statusObj.getBoolean(key)));
				if (em.find(VehicleHasOperatorevents.class,
						vehiclehasoperatorId) == null) {
					em.persist(vehiclehasoperator);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj.toString();
	}

	@Override
	public String getOpenTicket(String data) {
		JSONObject obj = new JSONObject();
		JSONArray response = new JSONArray();
		try {
			obj.put("errorcode", 1);
			obj.put("iserror", true);
			obj.put("result", response);
			Vehicle vehicle = getVehicle(data);
			if (vehicle != null) {
				Query ticketQ = em
						.createQuery("Select ti.pptIds from TicketInfo ti where  ti.vin=:vin and ti.status <= 3 order by createdDate desc");
				ticketQ.setParameter("vin", vehicle.getVin());
				List<String> ticketInfos = ticketQ.getResultList();
				for (String ti : ticketInfos) {
					try {
						response.put(new JSONObject(ti));
					} catch (Exception e) {
						LOGGER.error("getOpenTicket exception : "
								+ e.getMessage() + " and object : " + ti);
						e.printStackTrace();
					}
				}
			} else {
				obj.put("errorcode", 2);
				obj.put("iserror", true);
			}
			obj.put("errorcode", 0);
			obj.put("iserror", false);
			obj.put("result", response);
		} catch (Exception e) {
			LOGGER.error("getOpenTicket exception : " + e.getMessage());
			e.printStackTrace();
		}
		return obj.toString();

	}

	public String getPreferencesData(String keyValue, String corpId) {
		try {
			/*
			 * LOGGER.error(">getPreferencesData to find " + keyValue +
			 * " and Company= " + corpId);
			 */
			String result;
			String sqlforCmpSetting = "SELECT app.values AS appDefault, comp.values AS companyPref FROM ltmscompanyadmindb.applicationsettings app LEFT JOIN ltmscompanyadmindb.companysettings comp ON app.key=comp.appsettings_key AND comp.company_companyId = :corpId WHERE app.key = :key ";
			LOGGER.info("Brfore exec qry 1");
			Query queryCmpSetting = em.createNativeQuery(sqlforCmpSetting);
			queryCmpSetting.setParameter(CORP_ID, corpId);
			queryCmpSetting.setParameter("key", keyValue);
			Object[] obj = (Object[]) queryCmpSetting.getSingleResult();

			if (obj[1] != null) {
				result = (String) obj[1];
			} else {
				result = (String) obj[0];
			}
			return result;
		} catch (Exception e) {
			LOGGER.error("Error on finding " + keyValue
					+ " in  getPreferences Data as " + e);
			return "Error";
		}

	}

	public boolean isJSONValid(String test) {
		try {
			new JSONObject(test);
		} catch (JSONException ex) {
			try {
				new JSONArray(test);
			} catch (JSONException ex1) {
				return false;
			}
		}
		return true;
	}
}