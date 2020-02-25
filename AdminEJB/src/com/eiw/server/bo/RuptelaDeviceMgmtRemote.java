package com.eiw.server.bo;

import javax.ejb.LocalBean;

@LocalBean
public interface RuptelaDeviceMgmtRemote {
	
	String sendCommand(String password, String imeiNo, String command);

}
