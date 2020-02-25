package com.eiw.device.itrac;

import java.util.Date;

/**
 * Data without location
 */
public class Data {

	/**
	 * Id
	 */
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Device
	 */
	private Long deviceId;

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * Server time (UTC)
	 */
	private Date serverTime;

	public Date getServerTime() {
		return serverTime;
	}

	public void setServerTime(Date serverTime) {
		this.serverTime = serverTime;
	}

	/**
	 * Extended information in XML format
	 */
	private String extendedInfo;

	public String getExtendedInfo() {
		return extendedInfo;
	}

	public void setExtendedInfo(String extendedInfo) {
		this.extendedInfo = extendedInfo;
	}

}
