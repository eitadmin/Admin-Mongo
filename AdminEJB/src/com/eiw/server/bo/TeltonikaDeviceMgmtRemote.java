package com.eiw.server.bo;

import javax.ejb.LocalBean;

@LocalBean
public interface TeltonikaDeviceMgmtRemote {

	String sendCommand(String password, String imeiNo, String model,
			String command);

}
