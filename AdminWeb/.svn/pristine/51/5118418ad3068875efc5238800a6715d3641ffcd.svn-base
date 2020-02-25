package com.eiw.server;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;

import com.eiw.noTransmissionOverride.DeviceStarterImpl;

@WebServlet("/NoTransConfigServletForCaching")
public class NoTransConfigServletForCaching extends HttpServlet {
	private static final Logger LOGGER = Logger.getLogger("Servlet");
	String responceString = "";
	private static final long serialVersionUID = 1L;
	@EJB
	DeviceStarterImpl DeviceStarter;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");
		String imeiNo = request.getParameter("imeiNo");
		String method = request.getParameter("method");
		String nearByvins = request.getParameter("nearByvins");
		if (method.equalsIgnoreCase("addMethod")) {
			responceString = DeviceStarter.StartDevice(imeiNo, nearByvins);
		} else {
			responceString = DeviceStarter
					.deActivateDeviceForNoTrnsDevice(imeiNo);
		}
		response.getWriter().print(responceString);
	}

}
