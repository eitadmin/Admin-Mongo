package com.eiw.client.gxtmodel;

import java.util.Date;

import com.extjs.gxt.ui.client.data.BaseModel;

public class ModelData1 extends BaseModel {
	public ModelData1(String vehicleNo, Date dueDate, String msg) {
		set("vehicleNo", vehicleNo);
		set("dueDate", dueDate);
		set("msg", msg);
	}

}