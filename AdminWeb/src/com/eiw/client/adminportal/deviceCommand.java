package com.eiw.client.adminportal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.eiw.client.AdminPortalService;
import com.eiw.client.AdminPortalServiceAsync;
import com.eiw.client.dashboard.LoginDashboardModule;
import com.eiw.client.dto.CompanyDataAdmin;
import com.eiw.client.dto.VehicleData;
import com.eiw.client.gxtmodel.DisplayProcessor;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

public class deviceCommand extends DisplayProcessor {
	public AdminPortalServiceAsync fleetMgmtService = GWT
			.create(AdminPortalService.class);
	VerticalPanel verticalPanel = new VerticalPanel();
	FlexTable searchFields;
	public ListBox listBoxCompanyy;
	public ListBox listBoxPlateNo;
	public ListBox listBoxCommands;
	Button btnSearchDetails;
	Label lblDay, lblPlateNo;
	PopupPanel dialog = new PopupPanel();
	PopupPanel popup = new PopupPanel();
	Label lblPlateno = new Label("Asset No");
	TextBox txtplateNo = new TextBox();
	Label lblImeiNo = new Label("IMEI NO");
	TextBox txtImeiNo = new TextBox();
	Label lblDeviceName = new Label("Device");
	TextBox txtDeviceName = new TextBox();
	Label lblModelName = new Label("Model Name");
	TextBox txtModelName = new TextBox();
	Label lblCommand = new Label("Command");
	TextBox txtCommand = new TextBox();
	TextArea txtResult = new TextArea();
	private ArrayList<String> lstCmd = new ArrayList<String>();
	Button btnSend = new Button("Send");

	public String companyNames = " ";
	String getCommand = " ";
	String[] splitCommand;
	String sendCommand = "";
	DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy:MM:dd");

	Map<String, String> hmap = new HashMap<String, String>();

	public deviceCommand() {
		searchFields = new FlexTable();

		lblDay = new Label("Company Names");
		lblPlateNo = new Label("Asset Number");
		btnSearchDetails = new Button("Search");
		listBoxCompanyy = new ListBox();
		listBoxPlateNo = new ListBox();
		listBoxCommands = new ListBox();
		loadDays();
		clickHandler();

		searchFields.setWidget(0, 0, lblDay);
		searchFields.setWidget(0, 3, listBoxCompanyy);
		searchFields.setWidget(2, 0, lblPlateNo);
		searchFields.setWidget(2, 3, listBoxPlateNo);
		searchFields.setWidget(3, 3, btnSearchDetails);

		verticalPanel.add(searchFields);
		initComponent(verticalPanel);

		searchFields();
	}

	public void searchFields() {

		listBoxCompanyy.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				resultDatas();
			}
		});
	}

	public void resultDatas() {
		String companyId = listBoxCompanyy.getValue(listBoxCompanyy
				.getSelectedIndex());
		listBoxPlateNo.clear();
		hmap.clear();
		fleetMgmtService.getCompanyIdDetails(companyId,
				new AsyncCallback<List<VehicleData>>() {

					@Override
					public void onSuccess(List<VehicleData> arg0) {

						for (int j = 0; j < arg0.size(); j++) {
							listBoxPlateNo.addItem(arg0.get(j).getPlateNo());
							String value = arg0.get(j).getPlateNo() + ","
									+ arg0.get(j).getImeiNo() + ","
									+ arg0.get(j).getMaintenanceType() + ","
									+ arg0.get(j).getModel();
							hmap.put(arg0.get(j).getPlateNo(), value);

						}

					}

					@Override
					public void onFailure(Throwable arg0) {
						// TODO Auto-generated method stub

					}
				});
	}

	private void loadSearchValue() {
		dialog.hide();
		FlexTable ft = new FlexTable();
		ft.setWidget(0, 1, lblPlateno);
		ft.setWidget(0, 2, txtplateNo);
		ft.setWidget(0, 4, lblImeiNo);
		ft.setWidget(0, 5, txtImeiNo);
		ft.setWidget(1, 1, lblDeviceName);
		ft.setWidget(1, 2, txtDeviceName);
		ft.setWidget(1, 4, lblModelName);
		ft.setWidget(1, 5, txtModelName);
		ft.setWidget(2, 2, lblCommand);
		ft.setWidget(2, 3, txtCommand);
		ft.setWidget(3, 3, btnSend);
		ft.setWidget(4, 3, txtResult);
		dialog.setWidget(ft);
		dialog.show();
		dialog.setAutoHideEnabled(true);
		dialog.center();
		dialog.setPixelSize(50, 250);
		String[] companyValues = hmap.get(
				listBoxPlateNo.getValue(listBoxPlateNo.getSelectedIndex()))
				.split(",");
		txtplateNo.setValue(companyValues[0]);
		txtImeiNo.setValue(companyValues[1]);
		txtDeviceName.setValue(companyValues[2]);
		txtModelName.setValue(companyValues[3]);

	}

	public void loadDays() {
		fleetMgmtService.getCompanyNames(LoginDashboardModule.suffix,
				new AsyncCallback<List<CompanyDataAdmin>>() {
					@Override
					public void onSuccess(List<CompanyDataAdmin> companyList) {
						listBoxCompanyy.addItem("");
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

	public void clickHandler() {
		btnSearchDetails.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				// TODO Auto-generated method stub
				loadSearchValue();
			}
		});

		btnSend.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				// TODO Auto-generated method stub
				callServlet();
			}
		});

	}

	public void callServlet() {
		getCommand = txtCommand.getValue();
		sendDataToServlet(getCommand);
	}

	public void sendDataToServlet(String cmd) {
		String requiredCommand;
		requiredCommand = URL.encodeComponent(cmd);
		System.out.println(requiredCommand);
		String gwt = GWT.getHostPageBaseURL() + "deviceServlet";
		String url = "?imeiNo=" + txtImeiNo.getValue() + "&manufacturer="
				+ txtDeviceName.getValue() + "new" + "&command="
				+ requiredCommand;

		System.out.println(gwt + url);
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, gwt
				+ url);

		try {
			builder.sendRequest(null, new RequestCallback() {

				@Override
				public void onError(Request request, Throwable exception) {
					System.out.println("Error is " + exception);
				}

				@Override
				public void onResponseReceived(Request arg0, Response arg1) {
					// TODO Auto-generated
					// method stub
					System.out.println(arg1.getText());
//					Window.alert(arg1.getText());
					txtResult.setCharacterWidth(40);
					txtResult.setVisibleLines(10);
					txtResult.setValue("My command is:" + txtCommand.getValue()
							+ "\n" + "Result is " + arg1.getText());
				}

			});
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public void loadSelectedCmd() {
		listBoxCommands.clear();
		for (String str : lstCmd) {
			listBoxCommands.addItem(str);
		}
	}
}