package com.skt.client.dto;

import java.util.HashMap;
import java.util.Map;

import com.eiw.device.ejb.VehicleComposite;
import com.eiw.server.studenttrackingpu.Schoolroute;
import com.eiw.server.studenttrackingpu.Studentdetails;

public class RouteAndVehicleData {
	private Schoolroute schoolRoute;
	private VehicleComposite vehicleComposite;
	private Map<String, Studentdetails> studentDetailsMap = new HashMap<String, Studentdetails>();

	public Schoolroute getSchoolRoute() {
		return schoolRoute;
	}

	public void setSchoolRoute(Schoolroute schoolRoute) {
		this.schoolRoute = schoolRoute;
	}

	public VehicleComposite getVehicleComposite() {
		return vehicleComposite;
	}

	public void setVehicleComposite(VehicleComposite vehicleComposite) {
		this.vehicleComposite = vehicleComposite;
	}

	public Map<String, Studentdetails> getStudentDetailsMap() {
		return studentDetailsMap;
	}

	public void setStudentDetailsMap(
			Map<String, Studentdetails> studentDetailsMap) {
		this.studentDetailsMap = studentDetailsMap;
	}
}
