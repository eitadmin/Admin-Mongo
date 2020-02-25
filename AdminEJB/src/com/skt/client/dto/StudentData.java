package com.skt.client.dto;

import java.util.Date;

public class StudentData implements java.io.Serializable {
	private String stin;
	private int rollNo;
	private String firstName;
	private String lastName;
	private String gender;
	private Date dateOfBirth;
	private String address;
	private String city;
	private int pin;
	private String state;
	private String schoolId;
	private String brannchId;
	private String classId;
	private String sectionId;
	private String parentId;
	private String parentName;
	private String contactNo;
	private String emailAddress;
	private int alertMode;
	private String tagId;
	private String vin;
	private long pickuptopId;
	private long dropstopId;
	private int status;
	private Date eventTime;
	private String latlng;
	private String lastUpdBy;
	private Date lastUpdDt;
	private String lastKnownLocation;
	private String present;
	private String absent;
	private String tagType;
	private String userId;
	private boolean isCorrectBus;
	private String RouteName;

	public String getTagType() {
		return tagType;
	}

	public void setTagType(String tagType) {
		this.tagType = tagType;
	}

	public String getPresent() {
		return present;
	}

	public void setPresent(String present) {
		this.present = present;
	}

	public String getAbsent() {
		return absent;
	}

	public void setAbsent(String absent) {
		this.absent = absent;
	}

	public String getStin() {
		return stin;
	}

	public void setStin(String stin) {
		this.stin = stin;
	}

	public int getRollNo() {
		return rollNo;
	}

	public void setRollNo(int i) {
		this.rollNo = i;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getPin() {
		return pin;
	}

	public void setPin(int integer) {
		this.pin = integer;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getBrannchId() {
		return brannchId;
	}

	public void setBrannchId(String brannchId) {
		this.brannchId = brannchId;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getSectionId() {
		return sectionId;
	}

	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public int getAlertMode() {
		return alertMode;
	}

	public void setAlertMode(int alertMode) {
		this.alertMode = alertMode;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int integer) {
		this.status = integer;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}

	public String getLatlng() {
		return latlng;
	}

	public void setLatlng(String latlng) {
		this.latlng = latlng;
	}

	public String getLastUpdBy() {
		return lastUpdBy;
	}

	public void setLastUpdBy(String lastUpdBy) {
		this.lastUpdBy = lastUpdBy;
	}

	public Date getLastUpdDt() {
		return lastUpdDt;
	}

	public void setLastUpdDt(Date lastUpdDt) {
		this.lastUpdDt = lastUpdDt;
	}

	public String getLastKnownLocation() {
		return lastKnownLocation;
	}

	public void setLastKnownLocation(String lastKnownLocation) {
		this.lastKnownLocation = lastKnownLocation;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public long getPickuptopId() {
		return pickuptopId;
	}

	public void setPickuptopId(long pickuptopId) {
		this.pickuptopId = pickuptopId;
	}

	public long getDropstopId() {
		return dropstopId;
	}

	public void setDropstopId(long dropstopId) {
		this.dropstopId = dropstopId;
	}

	public String getRouteName() {
		return RouteName;
	}

	public void setRouteName(String routeName) {
		RouteName = routeName;
	}

	public boolean isCorrectBus() {
		return isCorrectBus;
	}

	public void setCorrectBus(boolean isCorrectBus) {
		this.isCorrectBus = isCorrectBus;
	}
}
