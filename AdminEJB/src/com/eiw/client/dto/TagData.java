package com.eiw.client.dto;

public class TagData implements java.io.Serializable {
	
	private String studentName;
	private String oldtagId;
	private String tagId;
	private String tagType;
	private String schoolid;
	private String branchid;
	private String lastupdatedBy;
	private String lastUpdatedDate;
	private boolean isCorrectBus;

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getOldtagId() {
		return oldtagId;
	}

	public void setOldtagId(String oldtagId) {
		this.oldtagId = oldtagId;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getTagType() {
		return tagType;
	}

	public void setTagType(String tagType) {
		this.tagType = tagType;
	}

	public String getSchoolid() {
		return schoolid;
	}

	public void setSchoolid(String schoolid) {
		this.schoolid = schoolid;
	}

	public String getBranchid() {
		return branchid;
	}

	public void setBranchid(String branchid) {
		this.branchid = branchid;
	}

	public String getLastupdatedBy() {
		return lastupdatedBy;
	}

	public void setLastupdatedBy(String lastupdatedBy) {
		this.lastupdatedBy = lastupdatedBy;
	}

	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public boolean isCorrectBus() {
		return isCorrectBus;
	}

	public void setCorrectBus(boolean isCorrectBus) {
		this.isCorrectBus = isCorrectBus;
	}
}
