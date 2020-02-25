package com.eiw.client.dashboard;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import com.eiw.client.AdminPortalService;
import com.eiw.client.AdminPortalServiceAsync;
import com.eiw.client.LoginConstants;
import com.eiw.client.dto.CompanyDataAdmin;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class HeaderPanel extends VerticalPanel {
	FlexTable ftTop = new FlexTable();
	String n;
	int i;
	ColumnModel cm;
	Image logo = new Image();
	Image setLogo = new Image();
	private AdminPortalServiceAsync fleetMgmtService = GWT
			.create(AdminPortalService.class);

	private LoginConstants constants = GWT.create(LoginConstants.class);
	String companyLogo = null;

	Label companyLabel = new Label();
	Label branchLabel = new Label();
	Label userNameLabel = new Label();
	Label compRoleLabel = new Label();
	Label lineLabel = new Label("|");
	Label lineLabel1 = new Label("|");
	Label lineLabel2 = new Label("|");
	Label lineLabel3 = new Label("|");
	FlexTable flextable = new FlexTable();

	String v;
	public static Label lblMsg = new Label();
	public static VerticalPanel cpMsg = new VerticalPanel();
	SortedMap<String, List<String>> hashMapFeatures = new TreeMap<String, List<String>>();

	@SuppressWarnings("deprecation")
	public HeaderPanel() {

		lblMsg.setStyleName("msg");
		flextable.setWidget(0, 0,
				new HTML("<div class='msg-left'>&nbsp;</div>"));
		flextable.setWidget(0, 1, lblMsg);
		flextable.setWidget(0, 2, new HTML(
				"<div class='msg-right'>&nbsp;</div>"));
		flextable.setCellSpacing(0);
		flextable.setStyleName("flextable");

		cpMsg.setStyleName("cpmsgcenter");
		cpMsg.add(flextable);
		add(cpMsg);

		if (LoginDashboardModule.ourLogoUrl != null) {
			logo.setUrl(LoginDashboardModule.upload_File + "Company/"
					+ LoginDashboardModule.ourLogoUrl);
		} else {
			logo.setUrl(LoginDashboardModule.upload_File
					+ "Company/transparent.gif");
		}

		if (LoginDashboardModule.imageUrl != null) {
			setLogo.setUrl(LoginDashboardModule.upload_File + "Company/"
					+ LoginDashboardModule.imageUrl);
		} else {
			setLogo.setUrl(LoginDashboardModule.upload_File
					+ "Company/CompanyLogo.png");
		}

		Image truckLogo = new Image("img/VolvoFH.jpg");
		Image userpIcon = new Image("img/pref-icon.png");
		Image dateIcon = new Image("img/date.png");
		Image smsIcon = new Image("img/sms1.png");
		Image logo1 = new Image("img/16branch.png");
		Image logo2 = new Image("img/16user.png");
		Image calLogo = new Image("img/calender.png");
		Image userLogo = new Image("img/userpreference.png");
		Image logout = new Image("img/logout.png");
		Image downloadIcon = new Image("img/download.png");

		logout.setTitle(constants.Logout());
		userpIcon.setTitle(constants.User_Preference());
		dateIcon.setTitle(constants.Calendar());
		// dateIcon.setVisible(false);
		smsIcon.setTitle(constants.SMS());
		downloadIcon.setTitle(constants.Download());

		logo.setStyleName("logo");
		logo.setSize("166", "71");
		setLogo.setSize("150", "50");
		calLogo.setSize("40", "40");
		smsIcon.setSize("28", "23");
		userpIcon.setSize("23", "23");
		dateIcon.setSize("20", "20");
		userLogo.setSize("40", "40");
		downloadIcon.setSize("28", "23");
		if (!LoginDashboardModule.compRole.equalsIgnoreCase("FleetManager")) {
			// userpIcon.setVisible(false);
			dateIcon.setVisible(false);
			smsIcon.setVisible(false);
			downloadIcon.setVisible(false);

		}

		VerticalPanel vpName = new VerticalPanel();
		vpName.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		HorizontalPanel hpLogout = new HorizontalPanel();
		hpLogout.setStyleName("hpLogout");
		hpLogout.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);

		ftTop.setWidth(Window.getClientWidth() + "");
		Label label = new Label("Logout");

		label.setStyleName("logoutLbl");
		logout.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				// TODO Auto-generated method stub
				// if (AlertsPage.timer != null) {
				// AlertsPage.timer.cancel();
				// }
				Window.Location.reload();
				// getLogoutInfo
				CompanyDataAdmin companyDataAdmin = new CompanyDataAdmin();
				companyDataAdmin.setUserLoginId(LoginDashboardModule.loginId);
				companyDataAdmin.setUserName(LoginDashboardModule.userName);
				companyDataAdmin.setCompanyName(LoginDashboardModule.compName);
				companyDataAdmin.setBranchName(LoginDashboardModule.brnchName);
				companyDataAdmin.setRoleName(LoginDashboardModule.compRole);

				fleetMgmtService.updateLoginInfo(companyDataAdmin,
						new AsyncCallback<String>() {

							public void onSuccess(String result) {
								// TODO Auto-generated method stub

							}

							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}
						});
			}
		});

		HorizontalPanel headp = new HorizontalPanel();
		HorizontalPanel demoh = new HorizontalPanel();
		headp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		HorizontalPanel hpName = new HorizontalPanel();
		hpName.setStyleName("hpName");
		lblMsg.setText("Super Admin");
		lblMsg.setStyleName("msg");

		hpName.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hpName.add(downloadIcon);
		hpName.add(userpIcon);
		hpName.add(dateIcon);
		hpName.add(smsIcon);
		// hpName.add(lineLabel3);
		// hpName.add(userNameLabel);
		// hpName.add(lineLabel1);
		// hpName.add(logo2);
		// hpName.add(compRoleLabel);
		// hpName.add(lineLabel);
		// hpName.add(logo1);
		// hpName.add(branchLabel);
		// hpName.add(lineLabel2);
		hpName.add(logout);
		// hpName.add(label);
		demoh.add(headp);
		demoh.add(hpName);
		// vpName.add(headp);
		vpName.add(demoh);
		vpName.add(hpLogout);
		userNameLabel.setText(LoginDashboardModule.userName);
		compRoleLabel.setText(LoginDashboardModule.compRole);
		companyLabel.setText(LoginDashboardModule.companyId);
		branchLabel.setText(LoginDashboardModule.branchId);

		userNameLabel.setStyleName("text_ftTop ");
		compRoleLabel.setStyleName("text_ftTop ");
		companyLabel.setStyleName("text_ftTop ");
		branchLabel.setStyleName("text_ftTop ");
		label.setStyleName("logoutLbl");
		lineLabel.setStyleName("text_ftTop");
		lineLabel1.setStyleName("text_ftTop");
		lineLabel2.setStyleName("text_ftTop");
		lineLabel3.setStyleName("text_ftTop");
		downloadIcon.setStyleName("hpicon");
		userpIcon.setStyleName("hpicon");
		dateIcon.setStyleName("hpicon");
		smsIcon.setStyleName("hpicon");
		logout.setStyleName("hpicon");
		logo1.setStyleName("logoutStyle");
		logo2.setStyleName("logoutStyle");

		ftTop.setStyleName("ftTop");
		ftTop.setWidget(0, 0, vpName);
		ftTop.setWidget(1, 0, logo);

		calLogo.setStyleName("setHeaderLogo");
		userLogo.setStyleName("setHeaderLogo");
		truckLogo.setStyleName("setHeaderLogo");
		ftTop.setWidget(1, 1, new MenuPanel());
		ftTop.setWidget(1, 2, setLogo);
		ftTop.getFlexCellFormatter().setColSpan(0, 0, 3);
		ftTop.getCellFormatter().setHorizontalAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_RIGHT);
		ftTop.getCellFormatter().setHorizontalAlignment(1, 2,
				HasHorizontalAlignment.ALIGN_RIGHT);
		ftTop.getFlexCellFormatter().setHorizontalAlignment(1, 1,
				HasHorizontalAlignment.ALIGN_CENTER);
		add(ftTop);

		smsIcon.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				// TODO Auto-generated method stub
				new SMSGadget();
			}
		});
		dateIcon.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
				/*
				 * if (gwtCalendarEvent == null) { gwtCalendarEvent = new
				 * GWTCalendarEvent(); }
				 */
				// gwtCalendarEvent.show();
				// new GWTCalendarEvent();
			}
		});
		userpIcon.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {

				new WelcomeScreen(LoginDashboardModule.userName,
						LoginDashboardModule.companyId,
						LoginDashboardModule.branchId,
						LoginDashboardModule.compRole,
						LoginDashboardModule.userImage);

			}
		});

	}
}