package com.eiw.client.dashboard;

import java.util.Date;

import com.eit.dcframework.client.DisplayConfigController;
import com.eiw.client.icons.ImagesBundle;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.custom.Portal;
import com.extjs.gxt.ui.client.widget.custom.Portlet;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DashBoard extends VerticalPanel {

	public DashBoard() {

		Portal portal = new Portal(8);
		portal.setBorders(true);
		portal.setStyleAttribute("backgroundColor", "white");
		portal.setSize(Window.getClientWidth() - 5, (BodyPanel.bodyHeight + 65));
		portal.setColumnWidth(0, .33);
		portal.setColumnWidth(1, .33);
		portal.setColumnWidth(2, .33);
		portal.setColumnWidth(3, .33);
		portal.setColumnWidth(4, .33);
		portal.setColumnWidth(5, .33);
		portal.setColumnWidth(6, .33);
		portal.setColumnWidth(7, .33);
		// int j = 0;

		for (int i = 0; i < 6; i++) {
			Portlet portlet = new Portlet();
			portlet.setHeaderVisible(true);
			portlet.setCollapsible(false);
			portlet.setIcon(ImagesBundle.Util.get("ICON-notify"));
			portlet.setLayout(new FitLayout());
			portlet.setHeight((BodyPanel.bodyHeight) / 2 + 35);
			portlet.add(getClassFile(LoginDashboardModule.adminDashboard.get(i)));
			portlet.setHeading(LoginDashboardModule.adminDashboard.get(i));
			configPanel(portlet);
			portal.add(portlet, i);

		}
		add(portal);
		dashboardrefresh();

	}

	private void configPanel(final ContentPanel panel) {
		panel.setCollapsible(true);
		panel.setAnimCollapse(true);
	}

	private static DisplayConfigController getClassFile(String featureName) {
		DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy:MM:dd");
		JSONObject object = null;
		String rptDate = null;
		DisplayConfigController dashboardGrid = null;
		if (featureName.equalsIgnoreCase("Demo Companies")
				|| featureName.equalsIgnoreCase("deomComps")) {
			object = new JSONObject();
			rptDate = dtf.format(new Date());
			object.put("Report Date", new JSONString(rptDate));
			String whereCond = "WHERE isDemo IS TRUE AND suffix = '"
					+ LoginDashboardModule.suffix + "' AND isDeleted IS FALSE";
			dashboardGrid = new DisplayConfigController("deomComps", whereCond,
					"dashboard", "no", "deomComps", object.toString());
		} else if (featureName.equalsIgnoreCase("Followup Companies")
				|| featureName.equalsIgnoreCase("followUpComps")) {
			object = new JSONObject();
			rptDate = dtf.format(new Date());
			object.put("Report Date", new JSONString(rptDate));

			String whereCond = "WHERE followUp IS TRUE AND suffix = '"
					+ LoginDashboardModule.suffix + "' AND isDeleted IS FALSE";
			dashboardGrid = new DisplayConfigController("followUpComps",
					whereCond, "dashboard", "no", "followUpComps",
					object.toString());
		} 
//		else if (featureName
//				.equalsIgnoreCase("72 Hours No Transmission Report")
//				|| featureName.equalsIgnoreCase("notransrptdboard")) {
//			object = new JSONObject();
//			rptDate = dtf.format(new Date());
//			object.put("Report Date", new JSONString(rptDate));
//			String whereCond = "WHERE ve.lastTransmissionDate < DATE_SUB( NOW( ) , INTERVAL 3 DAY ) AND comp.suffix = '"
//					+ LoginDashboardModule.suffix + "' AND isDeleted IS FALSE";
//			dashboardGrid = new DisplayConfigController("notransrptdboard",
//					whereCond, "dashboard", "no", "notransrptdboard",
//					object.toString());
//		}
		else if (featureName.equalsIgnoreCase("Lost Deal")
				|| featureName.equalsIgnoreCase("lostdealreport")) {
			object = new JSONObject();
			rptDate = dtf.format(new Date());
			object.put("Report Date", new JSONString(rptDate));
			String whereCond = "WHERE isLostDeal IS TRUE AND suffix = '"
					+ LoginDashboardModule.suffix + "'";
			dashboardGrid = new DisplayConfigController("lostdealreport",
					whereCond, "dashboard", "no", "lostdealreport",
					object.toString());
		} else if (featureName.equalsIgnoreCase("Deleted Company Report")
				|| featureName.equalsIgnoreCase("deletedcompanyreport")) {
			object = new JSONObject();
			rptDate = dtf.format(new Date());
			object.put("Report Date", new JSONString(rptDate));
			String whereCond = "WHERE isDeleted IS TRUE AND suffix = '"
					+ LoginDashboardModule.suffix + "'";
			dashboardGrid = new DisplayConfigController("deletedcompanyreport",
					whereCond, "dashboard", "no", "deletedcompanyreport",
					object.toString());
		} else {
			object = new JSONObject();
			rptDate = dtf.format(new Date());
			object.put("Report Date", new JSONString(rptDate));
			String whereCond = "WHERE com.suffix = '"
					+ LoginDashboardModule.suffix
					+ "' and sms.smsCnt <'100' AND isDeleted IS FALSE GROUP BY sms.companyId,sms.branchId";
			dashboardGrid = new DisplayConfigController("smsBalDboard",
					whereCond, "dashboard", "no", "smsBalDboard",
					object.toString());
		}
		return dashboardGrid;
	}

	public static void dashboardGrid(String formName) {
		DashBoard.getClassFile(formName);
	}

	public static native void dashboardrefresh()
	/*-{
		$wnd.dashboardRefresh = $entry(@com.eiw.client.dashboard.DashBoard::dashboardGrid(Ljava/lang/String;));
	}-*/;
}