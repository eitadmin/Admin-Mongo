package com.eiw.server;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eiw.cron.AlertConfigEJB;

/**
 * Servlet implementation class AlertConfigServlet
 */
public class AlertConfigServlet extends HttpServlet {
	@EJB
	AlertConfigEJB alertConfigEJB;

	protected synchronized void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected synchronized void doPost(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/plain");
		String imeiNo = req.getParameter("vin");
		String method = req.getParameter("method");
		switch (method) {
		case "alertconfigCache":
			alertConfigEJB.startJobImplemented(imeiNo);
			break;
		case "companySettingCache":
			alertConfigEJB.startCompanySettingCache();
			break;
		default:
			alertConfigEJB.getOtherAlertVins(imeiNo);
			break;
		}
		resp.getWriter().print("Done");
	}
}
