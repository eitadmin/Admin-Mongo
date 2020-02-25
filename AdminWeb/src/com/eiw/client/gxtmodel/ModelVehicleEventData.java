/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007-2009, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.eiw.client.gxtmodel;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.PropertyChangeEvent;

public class ModelVehicleEventData extends BaseModel {

	private static final long serialVersionUID = 2103699184769341265L;

	public ModelVehicleEventData(String vehicleNo, int speed) {
		setVehicleNo(vehicleNo);
		setSpeed(speed);

		setAvgSpeed();
	}

	public String getVehicleNo() {
		return get("vehicleNo");
	}

	public int getSpeed() {
		return (Integer) get("speed");
	}

	@Override
	public void notify(ChangeEvent evt) {
		super.notify(evt);

		PropertyChangeEvent e = (PropertyChangeEvent) evt;
		if (!e.getName().equals("speed")) {
			setAvgSpeed();
		}
	}

	public void setAvgSpeed() {

		if (get("speed") != null) {
			double avg = 50;
			set("avgspeed", 50);
		}
	}

	public void setVehicleNo(String vehicleNo) {
		set("vehicleNo", vehicleNo);
	}

	public void setSpeed(int speed) {
		set("speed", speed);
	}

}