package com.eiw.client.dto;

import java.util.Map;

import com.skt.client.dto.StudentData;

public class DashBoardData implements java.io.Serializable {
	private Map<String, VehicleData> staticVariableData;
	private Map<String, VehicleData> liveDatas;
	private Map<String, StudentData> studentDatas;
	private Map<String, VehicleData> waitingForTransDatas;

	public Map<String, VehicleData> getStaticVariableData() {
		return staticVariableData;
	}

	public void setStaticVariableData(
			Map<String, VehicleData> staticVariableData) {
		this.staticVariableData = staticVariableData;
	}

	public Map<String, VehicleData> getLiveDatas() {
		return liveDatas;
	}

	public void setLiveDatas(Map<String, VehicleData> liveDatas) {
		this.liveDatas = liveDatas;
	}

	public Map<String, StudentData> getStudentDatas() {
		return studentDatas;
	}

	public void setStudentDatas(Map<String, StudentData> studentsData) {
		this.studentDatas = studentsData;
	}

	public Map<String, VehicleData> getWaitingForTransDatas() {
		return waitingForTransDatas;
	}

	public void setWaitingForTransDatas(
			Map<String, VehicleData> waitingForTransDatas) {
		this.waitingForTransDatas = waitingForTransDatas;
	}

}
