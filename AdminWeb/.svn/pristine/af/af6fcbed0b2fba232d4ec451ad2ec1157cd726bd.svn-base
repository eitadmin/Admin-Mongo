package com.eiw.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.eiw.admin.ejb.EMSAdminPortal;
import com.eiw.device.handler.AndroidDeviceHandler;

public class AndroidDeviceServlet extends HttpServlet {

	private Logger logger = Logger.getLogger("admin");
	@EJB
	EMSAdminPortal emsAdmin;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String mode = request.getParameter("mode");
		String value = request.getParameter("value");

		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String responseData = null;
		switch (mode) {
		case "createticket":
			responseData = emsAdmin.createTicket(value);
			break;
		case "addpttid":
			responseData = emsAdmin.addPptId(value);
			break;
		case "rfidregister":
			responseData = emsAdmin.rfidRegister(value);
			break;
		case "programkeys":
			responseData = emsAdmin.programKeys(value);
			break;
		case "getopenticket":
			responseData = emsAdmin.getOpenTicket(value);
			break;
		}

		out.write(responseData);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		logger.error("Success post Android");
		PrintWriter out = response.getWriter();
		StringBuilder stringBuilder = new StringBuilder("");
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = request.getReader();
			char[] charBuffer = new char[128];
			int bytesRead = -1;
			while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
				stringBuilder.append(charBuffer, 0, bytesRead);
			}
		} catch (Exception ex) {
			logger.error("Exception at reader " + ex);
			ex.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					logger.error("Exception at reader " + ex);
					ex.printStackTrace();
				}
			}
		}
		String params = stringBuilder.toString();
		logger.error("#JSON> " + params);
		if (!params.equalsIgnoreCase("")) {
			AndroidDeviceHandler androidDeviceHandler = new AndroidDeviceHandler(
					params);
			JSONObject data = new JSONObject();
			try {
				int result = androidDeviceHandler.callFromServlet(params);
				data.put("errorcode", result);
				data.put("iserror", result == 0 ? false : true);
				data.put("result", new JSONObject());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			out.print(data.toString());
		}
	}
}
