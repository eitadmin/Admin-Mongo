package com.eiw.client.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public class DashboardContentPerCB implements java.io.Serializable {

	private String companyID;
	private String companyName;
	private String branchID;
	private String branchName;
	private String companyRole;
	private Integer isMasterYN;
	private List<String> featureNames = new ArrayList<String>();
	private List<String> transactions;
	// to be used for Application ShortCuts
	private List<String> appShortcuts;
	// Table
	private String contactNo;
	private String truckImage;
	private Map<String, String> hashMap;
	private String userImage;
	private String companyLogoUrl;
	private String userLoginId;
	private String lastLoginTime;
	private String ourLogoUrl;
	Map<String, List<String>> compFeatures;
	Map<String, VehicleData> vehicleDatas;
	Map<String, List<GeoFencingData>> geoVinDatas;
	Map<String, List<GeoFencingData>> geoVinFreeformGeoDatas,
			geoVinRoadGeoDatas, geoVinLandmarkDatas;
	Map<String, List<String>> userFeatures;
	List<SortedMap> listOfSortedMap = new ArrayList<SortedMap>();
	private Map<String, String> fmsHashMap;
	private String suffix;
	private String bucketName;
	private String iconFolderName;
	private Boolean isMiniApps;
	private Map<String, Boolean> isMiniHashMap;
	private String preferredLanguage;
	private Map<String, Boolean> isWftSspHashMap;
	private Map<String, String> Provider;

	public String getCompanyID() {
		return companyID;
	}

	public void setCompanyID(String companyID) {
		this.companyID = companyID;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getBranchID() {
		return branchID;
	}

	public void setBranchID(String branchID) {
		this.branchID = branchID;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public Integer getIsMasterYN() {
		return isMasterYN;
	}

	public void setIsMasterYN(Integer isMasterYN) {
		this.isMasterYN = isMasterYN;
	}

	public List<String> getFeatureNames() {
		return featureNames;
	}

	public void setFeatureNames(List<String> featureNames) {
		this.featureNames = featureNames;
	}

	public List<String> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<String> transactions) {
		this.transactions = transactions;
	}

	public List<String> getAppShortcuts() {
		return appShortcuts;
	}

	public void setAppShortcuts(List<String> appShortcuts) {
		this.appShortcuts = appShortcuts;
	}

	public String getCompanyRole() {
		return companyRole;
	}

	public void setCompanyRole(String companyRole) {
		this.companyRole = companyRole;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getTruckImage() {
		return truckImage;
	}

	public void setTruckImage(String truckImage) {
		this.truckImage = truckImage;
	}

	public Map<String, String> getHashMap() {
		return hashMap;
	}

	public void setHashMap(Map<String, String> hashMap) {
		this.hashMap = hashMap;
	}

	public String getCompanyLogoUrl() {
		return companyLogoUrl;
	}

	public void setCompanyLogoUrl(String companyLogoUrl) {
		this.companyLogoUrl = companyLogoUrl;
	}

	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

	public String getUserLoginId() {
		return userLoginId;
	}

	public void setUserLoginId(String userLoginId) {
		this.userLoginId = userLoginId;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Map<String, List<String>> getCompFeatures() {
		return compFeatures;
	}

	public void setCompFeatures(SortedMap<String, List<String>> compFeatures) {
		this.compFeatures = compFeatures;
	}

	public Map<String, VehicleData> getVehicleDatas() {
		return vehicleDatas;
	}

	public void setVehicleDatas(SortedMap<String, VehicleData> vehicleDatas) {
		this.vehicleDatas = vehicleDatas;
	}

	public void setGeoVinDatas(
			SortedMap<String, List<GeoFencingData>> geoVinDatas) {
		this.geoVinDatas = geoVinDatas;
	}

	public Map<String, List<GeoFencingData>> getGeoVinDatas() {
		return geoVinDatas;
	}

	public void setGeoVinFreeformGeoDatas(
			SortedMap<String, List<GeoFencingData>> geoVinFreeformGeoDatas) {
		this.geoVinFreeformGeoDatas = geoVinFreeformGeoDatas;
	}

	public Map<String, List<GeoFencingData>> getGeoVinFreeformGeoDatas() {
		return geoVinFreeformGeoDatas;
	}

	public void setGeoVinRoadGeoDatas(
			SortedMap<String, List<GeoFencingData>> geoVinRoadGeoDatas) {
		this.geoVinRoadGeoDatas = geoVinRoadGeoDatas;
	}

	public Map<String, List<GeoFencingData>> getGeoVinRoadGeoDatas() {
		return geoVinRoadGeoDatas;
	}

	public void setGeoVinLandmarkGeoDatas(
			SortedMap<String, List<GeoFencingData>> geoVinLandmarkDatas) {
		this.geoVinLandmarkDatas = geoVinLandmarkDatas;
	}

	public Map<String, List<GeoFencingData>> getGeoVinLandmarkDatas() {
		return geoVinLandmarkDatas;
	}

	public List<SortedMap> getListOfSortedMap() {
		return listOfSortedMap;
	}

	public void setListOfSortedMap(List<SortedMap> listOfSortedMap) {
		this.listOfSortedMap = listOfSortedMap;
	}

	public Map<String, String> getFmsHashMap() {
		return fmsHashMap;
	}

	public void setFmsHashMap(Map<String, String> fmsHashMap) {
		this.fmsHashMap = fmsHashMap;
	}

	public Map<String, List<String>> getUserFeatures() {
		return userFeatures;
	}

	public void setUserFeatures(SortedMap<String, List<String>> userFeatures) {
		this.userFeatures = userFeatures;
	}

	public String getOurLogoUrl() {
		return ourLogoUrl;
	}

	public void setOurLogoUrl(String ourLogoUrl) {
		this.ourLogoUrl = ourLogoUrl;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getIconFolderName() {
		return iconFolderName;
	}

	public void setIconFolderName(String iconFolderName) {
		this.iconFolderName = iconFolderName;
	}

	public Boolean getIsMiniApps() {
		return isMiniApps;
	}

	public void setIsMiniApps(Boolean isMiniApps) {
		this.isMiniApps = isMiniApps;
	}

	public Map<String, Boolean> getIsMiniHashMap() {
		return isMiniHashMap;
	}

	public void setIsMiniHashMap(Map<String, Boolean> isMiniHashMap) {
		this.isMiniHashMap = isMiniHashMap;
	}

	public String getPreferredLanguage() {
		return preferredLanguage;
	}

	public void setPreferredLanguage(String preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
	}

	public Map<String, Boolean> getIsWftSspHashMap() {
		return isWftSspHashMap;
	}

	public void setIsWftSspHashMap(Map<String, Boolean> isWftSspHashMap) {
		this.isWftSspHashMap = isWftSspHashMap;
	}

	public Map<String, String> getProvider() {
		return Provider;
	}

	public void setProvider(Map<String, String> provider) {
		Provider = provider;
	}

}