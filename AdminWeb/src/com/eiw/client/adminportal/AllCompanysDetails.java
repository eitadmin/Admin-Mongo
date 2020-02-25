package com.eiw.client.adminportal;

import java.util.Date;
import java.util.List;

import com.eit.dcframework.client.DisplayConfigController;
import com.eiw.client.AdminPortalService;
import com.eiw.client.AdminPortalServiceAsync;
import com.eiw.client.dashboard.LoginDashboardModule;
import com.eiw.client.dto.CompanyDataAdmin;
import com.eiw.client.gxtmodel.DisplayProcessor;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;

public class AllCompanysDetails extends DisplayProcessor {
	public AdminPortalServiceAsync fleetMgmtService = GWT
			.create(AdminPortalService.class);
	VerticalPanel verticalPanel = new VerticalPanel();
	FlexTable searchFields;
	public ListBox listBoxCompanyy;
	Button btnSearch;
	Label lblDay;
	public String companyNames = "", companyNames1 = "";
	DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy:MM:dd");

	public AllCompanysDetails() {
		searchFields = new FlexTable();
		lblDay = new Label("Company Names");
		btnSearch = new Button("Search");
		listBoxCompanyy = new ListBox();
		// listBoxCompanyy.setSize("100px", "25px");
		loadDays();

		searchFields.setWidget(0, 0, lblDay);
		searchFields.setWidget(0, 3, listBoxCompanyy);
		searchFields.setWidget(0, 6, btnSearch);

		verticalPanel.add(searchFields);
		verticalPanel.add(new HTML(
				"<div id = 'AllCompanysDetailsgridtable'></div>"));
		initComponent(verticalPanel);

		searchFields();
	}

	private void searchFields() {
		btnSearch.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {

				JSONObject object = new JSONObject();
				String rptDate = dtf.format(new Date());
				object.put("Report Date", new JSONString(rptDate));
				object.put("Report Name", new JSONString("Company Details"));

				companyNames = listBoxCompanyy.getItemText(listBoxCompanyy
						.getSelectedIndex());

				companyNames1 = listBoxCompanyy.getValue(listBoxCompanyy
						.getSelectedIndex());
				System.out.println("companyNames" + companyNames);
				System.out.println("companyNames1" + companyNames1);
				System.out.println("listBoxCompanyy.getSelectedIndex()"
						+ listBoxCompanyy.getSelectedIndex());

				String whereCond;
				String company = "CompanyAdmin";

				whereCond = " WHERE cbucr.crRoleName ='" + company + "'";

				if (companyNames.equalsIgnoreCase("All Company")) {

				} else {
					whereCond += " AND " + "c.companyId = '" + companyNames1
							+ "'";
					object.put("Company Name", new JSONString(companyNames));
				}

				whereCond += " AND "
						+ "c.suffix = '"
						+ LoginDashboardModule.suffix
						+ "' ORDER BY v.companyId)AS Result,(SELECT @a:= 0) AS SERIAL ";

				verticalPanel.add(new DisplayConfigController(
						"AllCompanysDetails", whereCond, "report", "no",
						"AllCompanysDetails", object.toString()));
			}
		});
	}

	public void loadDays() {
		fleetMgmtService.getCompanyNames(LoginDashboardModule.suffix,
				new AsyncCallback<List<CompanyDataAdmin>>() {
					@Override
					public void onSuccess(List<CompanyDataAdmin> companyList) {
						listBoxCompanyy.addItem("All Company", "1");
						for (int i = 0; i < companyList.size(); i++) {
							listBoxCompanyy.addItem(companyList.get(i)
									.getCompanyName(), companyList.get(i)
									.getCompanyId());

						}
					}

					@Override
					public void onFailure(Throwable arg0) {
						// TODO Auto-generated method stub
					}
				});
	}
}