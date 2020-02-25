package com.eiw.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.eiw.client.dto.VehicleData;
import com.skt.alerts.SKTAlertsEJBremote;
import com.skt.client.dto.VehicleTripTimeDto;

import flexjson.JSONSerializer;

/**
 * Servlet implementation class SKTServlet
 */
public class SKTServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@EJB
	SKTAlertsEJBremote sktAlertsEJBremote;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String companyId = "", branchId = "";
		String data = request.getParameter("value");
		String mode = request.getParameter("mode");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String callBackJavaScripMethodName = request.getParameter("callback");
		try {
			if (mode.equalsIgnoreCase("vehicleTripTime")) {
				JSONObject dboradjson = new JSONObject(data);
				companyId = dboradjson.getString("companyID");
				branchId = dboradjson.getString("branchID");
				List<VehicleTripTimeDto> vehicleTripTimeDto = sktAlertsEJBremote
						.getVehicleTripTime(companyId, branchId);
				String vehicleTripTimeData = new JSONSerializer().exclude(
						"*.class").deepSerialize(vehicleTripTimeDto);
				String jsonOutPut = callBackJavaScripMethodName + "("
						+ vehicleTripTimeData + ");";
				out.println(jsonOutPut);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
