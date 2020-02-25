package com.eiw.client.adminportal;

import java.util.Date;
import java.util.Map;

import com.eit.dcframework.client.DisplayConfigController;
import com.eiw.client.AdminPortalService;
import com.eiw.client.AdminPortalServiceAsync;
import com.eiw.client.dashboard.LoginDashboardModule;
import com.eiw.client.gxtmodel.DisplayProcessor;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.DateWrapper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;

public class ResellerDetails extends DisplayProcessor {
	public AdminPortalServiceAsync fleetMgmtService = GWT
			.create(AdminPortalService.class);
	VerticalPanel vp = new VerticalPanel();
	HorizontalPanel hp = new HorizontalPanel();
	ContentPanel cp = new ContentPanel();
	FlexTable searchFields;
	Label lblfrmdate;
	DateField fromDatefield;

	Label lbltodate;
	DateField toDatefield;
	Label lblreseller;
	public ListBox listBoxReseller;
	Label lblmonth;
	public ListBox listBoxIntervel;
	Label lblcategories;
	public ListBox listBoxcategories;
	Button btnSearch;
	DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd");
	Date date1 = new Date();
	// Date date2 = new Date();
	DateWrapper date = new DateWrapper(new Date());
	Image box1 = new Image("img/loading12.gif");
	String reseller = "", intervel = "", categories = "", fromdate = "",
			todate = "", Key = "";
	String header = "Reseller Details";

	public ResellerDetails() {

		searchFields = new FlexTable();
		lblreseller = new Label("Reseller Names :");
		listBoxReseller = new ListBox();

		if (LoginDashboardModule.suffix.equalsIgnoreCase("3")) {
			providerlist();

		} else {
			String reseller = LoginDashboardModule.ProviderHashMap
					.get(LoginDashboardModule.suffix);

			listBoxReseller.addItem(reseller, LoginDashboardModule.suffix);
		}

		lblmonth = new Label("Intervel :");
		listBoxIntervel = new ListBox();
		listBoxIntervel.addItem("All");
		listBoxIntervel.addItem("One Month");
		listBoxIntervel.addItem("Two Month");
		listBoxIntervel.addItem("Three Month");
		lblcategories = new Label("Categories :");
		listBoxcategories = new ListBox();
		listBoxcategories.addItem("All");
		listBoxcategories.addItem("Demo");
		listBoxcategories.addItem("Non-Demo");
		lblfrmdate = new Label("From Date :");
		fromDatefield = new DateField();

		fromDatefield.setValue(date1);
		fromDatefield.setMaxValue(new Date());
		fromDatefield.setWidth(100);

		lbltodate = new Label("To Date :");
		toDatefield = new DateField();
		toDatefield.setValue(date1);
		toDatefield.setMaxValue(new Date());
		toDatefield.setWidth(100);
		btnSearch = new Button("Search");

		searchFields.setWidget(0, 0, lblreseller);
		searchFields.setWidget(0, 3, listBoxReseller);
		searchFields.setWidget(0, 6, lblmonth);
		searchFields.setWidget(0, 9, listBoxIntervel);
		searchFields.setWidget(0, 12, lblcategories);
		searchFields.setWidget(0, 15, listBoxcategories);
		searchFields.setWidget(0, 18, lblfrmdate);
		searchFields.setWidget(0, 21, fromDatefield);
		searchFields.setWidget(0, 24, lbltodate);
		searchFields.setWidget(0, 27, toDatefield);
		searchFields.setWidget(0, 30, btnSearch);

		vp.add(searchFields);
		vp.add(new HTML("<div id = 'resellerDetailsgridtable'></div>"));

		listBoxIntervel.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent arg0) {

				intervel = listBoxIntervel.getItemText(listBoxIntervel
						.getSelectedIndex());
				if (intervel.equalsIgnoreCase("One Month")) {

					Date tdyDate = new Date();
					tdyDate.setDate(tdyDate.getDate() - 30);
					fromDatefield.setValue(tdyDate);
					fromDatefield.setMaxValue(new Date());
					fromDatefield.setWidth(100);
					lblfrmdate.setVisible(false);
					fromDatefield.setVisible(false);
					lbltodate.setVisible(false);
					toDatefield.setVisible(false);

				} else if (intervel.equalsIgnoreCase("Two Month")) {

					Date tdyDate = new Date();
					tdyDate.setDate(tdyDate.getDate() - 60);
					fromDatefield.setValue(tdyDate);
					fromDatefield.setMaxValue(new Date());
					fromDatefield.setWidth(100);
					lblfrmdate.setVisible(false);
					fromDatefield.setVisible(false);
					lbltodate.setVisible(false);
					toDatefield.setVisible(false);

				} else if (intervel.equalsIgnoreCase("Three Month")) {

					Date tdyDate = new Date();
					tdyDate.setDate(tdyDate.getDate() - 90);
					fromDatefield.setValue(tdyDate);
					fromDatefield.setMaxValue(new Date());
					fromDatefield.setWidth(100);
					lblfrmdate.setVisible(false);
					fromDatefield.setVisible(false);
					lbltodate.setVisible(false);
					toDatefield.setVisible(false);

				} else if (intervel.equalsIgnoreCase("All")) {

					Date tdyDate = new Date();
					fromDatefield.setValue(tdyDate);
					fromDatefield.setMaxValue(tdyDate);
					toDatefield.setValue(tdyDate);
					toDatefield.setMaxValue(tdyDate);
					fromDatefield.setWidth(100);
					lblfrmdate.setVisible(true);
					fromDatefield.setVisible(true);
					lbltodate.setVisible(true);
					toDatefield.setVisible(true);

				}
			}
		});

		btnSearch.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {

				String val = listBoxReseller.getValue(listBoxReseller
						.getSelectedIndex());
				reseller = listBoxReseller.getItemText(listBoxReseller
						.getSelectedIndex());

				intervel = listBoxIntervel.getItemText(listBoxIntervel
						.getSelectedIndex());
				categories = listBoxcategories.getItemText(listBoxcategories
						.getSelectedIndex());
				fromdate = dtf.format(fromDatefield.getValue());
				todate = dtf.format(toDatefield.getValue());
				String where = "";

				if (intervel.equalsIgnoreCase("All")) {

					where += " WHERE DATE(dateCreated) BETWEEN '" + fromdate
							+ "' AND '" + todate + "'";
				} else {
					String month = null;
					if (intervel.equalsIgnoreCase("One Month")) {
						month = "1";
					} else if (intervel.equalsIgnoreCase("Two Month")) {
						month = "2";

					} else if (intervel.equalsIgnoreCase("Three Month")) {
						month = "3";
					}

					where += " WHERE  DATE(dateCreated) > (CURDATE() - INTERVAL "
							+ month + " MONTH)";

				}

				if (!reseller.equalsIgnoreCase("All")) {
					where += " AND suffix ='" + val + "'";
				}

				if (categories.equalsIgnoreCase("Demo")) {

					where += " And isDemo = '1'";
				} else if (categories.equalsIgnoreCase("Non Demo")) {

					where += " And isDemo = '0'";
				}

				where += " ORDER BY dateCreated ASC";

				JSONObject object = new JSONObject();
				String rptDate = dtf.format(new Date());

				object.put("Report Name", new JSONString("Reseller Details"));
				object.put("Date", new JSONString(rptDate));

				getSensorReports(where, object.toString());

			}
		});
		initComponent(vp);

	}

	public void getSensorReports(String sWhere, String header) {

		getHtmlReport("resellerDetails", sWhere, header);
	}

	public void getHtmlReport(String reportName, String condition, String header) {

		vp.add(new DisplayConfigController(reportName, condition, "report",
				"no", reportName, header));

	}

	private void providerlist() {

		listBoxReseller.clear();
		listBoxReseller.addItem("All", "All");
		for (Map.Entry<String, String> hMap : LoginDashboardModule.ProviderHashMap
				.entrySet()) {

			listBoxReseller.addItem(hMap.getValue(), hMap.getKey());

		}
		listBoxReseller.setSelectedIndex(1);

	}
}
