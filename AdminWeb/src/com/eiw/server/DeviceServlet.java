package com.eiw.server;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.eiw.server.bo.APMKTDeviceMgmt;
import com.eiw.server.bo.AndroidDeviceMgmt;
import com.eiw.server.bo.ConcoxDeviceMgmt;
import com.eiw.server.bo.DeepseaDeviceMgmt;
import com.eiw.server.bo.RuptelaDeviceMgmt;
import com.eiw.server.bo.TeltonikaDeviceMgmt;

public class DeviceServlet extends HttpServlet {

	@EJB
	ConcoxDeviceMgmt cdm;

	@EJB
	RuptelaDeviceMgmt rdm;

	@EJB
	DeepseaDeviceMgmt Ddm;

	@EJB
	TeltonikaDeviceMgmt tdm;

	@EJB
	AndroidDeviceMgmt adm;
	
	@EJB
	APMKTDeviceMgmt apmkt;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/plain");
		req.setCharacterEncoding("utf-8");
		String imeiNo = req.getParameter("imeiNo");
		String manufacturer = req.getParameter("manufacturer");
		String command = req.getParameter("command");
		String pass = req.getParameter("pass");
		String result = null;

		if (manufacturer.equalsIgnoreCase("Concox")) {
			result = cdm.sendCommand(imeiNo, command);
		} else if (manufacturer.equalsIgnoreCase("Concoxnew")) {
			result = cdm.sendNewConcoxCommand(imeiNo, command);
		} else if (manufacturer.equalsIgnoreCase("Ruptela")) {
			result = rdm.sendCommand(pass, imeiNo, command);
		} else if (manufacturer.equalsIgnoreCase("DeepSea")) {
			result = Ddm.sendCommand(imeiNo, command);
		} else if (manufacturer.equalsIgnoreCase("Ruptelanew")) {
			result = rdm.sendNewCommand(pass, imeiNo, command);
		} else if (manufacturer.equalsIgnoreCase("Teltonika")) {
			result = tdm.sendCommand(command.split("_")[2], imeiNo,
					command.split("_")[1], command.split("_")[0]);
		} else if (manufacturer.equalsIgnoreCase("Android")) {
			JSONObject jsObjforticket = new JSONObject();
			try {
				jsObjforticket.put("ticketId", pass);
				result = adm.sendCommand(imeiNo, jsObjforticket);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (manufacturer.equalsIgnoreCase("APMKT")){
			result = apmkt.sendCommand(imeiNo, command);
		}
		resp.getWriter().print(result);
	}
}