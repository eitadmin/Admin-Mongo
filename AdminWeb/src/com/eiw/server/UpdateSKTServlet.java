package com.eiw.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;

import com.eiw.device.ejb.FleetTrackingDeviceListenerBORemote;
import com.eiw.server.bo.BOFactory;
import com.eiw.server.studenttrackingpu.Studentdetails;
import com.skt.alerts.SKTAlertsEJBremote;
import com.skt.alerts.SKTStartup;

/**
 * Servlet implementation class AlertConfigServlet
 */
public class UpdateSKTServlet extends HttpServlet {
	@EJB
	SKTAlertsEJBremote sktAlertsEJBremote;
	FleetTrackingDeviceListenerBORemote fleetTrackingDeviceListenerBO = BOFactory
			.getFleetTrackingDeviceListenerBORemote();
	SKTAlertsEJBremote sktAlertsManagerBO = BOFactory
			.getStudentalertEJBremote();

	// SKTAlertsManager sktAlertsManagerBO = new SKTAlertsManager();

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	protected synchronized void doPost(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/plain");
		String method = req.getParameter("method");
		if (method.equalsIgnoreCase("updateAlertTypes")) {
			SKTStartup.getSKTinitData().setAlerttypesDescriptionMap(
					sktAlertsEJBremote.getSKTalerttypesMap());
			Map<String, String> sMap = SKTStartup.getSKTinitData()
					.getAlerttypesDescriptionMap();
			for (Entry<String, String> entry : sMap.entrySet())
				System.out.println(entry.getKey() + " and " + entry.getValue());
		} else if (method.equalsIgnoreCase("updateStaticSchoolRouteDetails")) {
			String routeId = req.getParameter("routeId");
			String mode = req.getParameter("mode");
			System.out.println("routeId routeId = " + routeId);
			String status = sktAlertsEJBremote.updateStaticSchoolRouteDetails(
					routeId, mode);
			System.out.println("he can write = " + status);
		} else if (method.equalsIgnoreCase("updateVehicleDetails")) {
			String vin = req.getParameter("vin");
			String mode = req.getParameter("mode");
			System.out.println("vin vin = " + vin);
			String status = sktAlertsEJBremote.updateVehicleDetails(vin, mode);
			System.out.println("he can write = " + status);
		} else if (method.equalsIgnoreCase("updateStudentDetails")) {
			String tagId = req.getParameter("tagId");
			String mode = req.getParameter("mode");
			System.out.println("tagId tagId = " + tagId);
			String status = sktAlertsEJBremote
					.updateStudentDetails(tagId, mode);
			System.out.println("he can write = " + status);
		} else if (method.equalsIgnoreCase("presentalert")) {
			String value = req.getParameter("value");
			try {
				List<Studentdetails> studentdetails = new ArrayList<Studentdetails>();
				JSONArray jsonArray = new JSONArray(value);
				for (int i = 0; i < jsonArray.length(); i++) {
					Studentdetails studentdetail = fleetTrackingDeviceListenerBO
							.getStudentDetails(String.valueOf(jsonArray.get(i)));
					studentdetails.add(studentdetail);
				}
				sktAlertsManagerBO.alertForAttendence(studentdetails, "BES");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (method.equalsIgnoreCase("absentalert")) {
			String value = req.getParameter("value");
			try {
				List<Studentdetails> studentdetails = new ArrayList<Studentdetails>();
				JSONArray jsonArray = new JSONArray(value);
				for (int i = 0; i < jsonArray.length(); i++) {
					Studentdetails studentdetail = fleetTrackingDeviceListenerBO
							.getStudentDetails(String.valueOf(jsonArray.get(i)));
					studentdetails.add(studentdetail);
				}
				sktAlertsManagerBO.alertForAttendence(studentdetails, "ABS");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		resp.getWriter().print("Done");
	}
}
