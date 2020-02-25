package com.eiw.client.dashboard;

import com.eiw.client.AdminPortalService;
import com.eiw.client.AdminPortalServiceAsync;
import com.eiw.client.LoginConstants;
import com.eiw.client.dto.CompanyDataAdmin;
import com.eiw.client.icons.ImagesBundle;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

public class WelcomeScreen extends Dialog {

	private AdminPortalServiceAsync fleetMgmtService = GWT
			.create(AdminPortalService.class);
	FlexTable flexTable = new FlexTable();

	private LoginConstants constants = GWT.create(LoginConstants.class);

	Button btnLogout = new Button();
	Button btnOk = new Button();

	HorizontalPanel btnHp = new HorizontalPanel();
	HorizontalPanel hp = new HorizontalPanel();

	VerticalPanel vp = new VerticalPanel();

	Image lstLgnLogo = new Image("img/lastLogin.png");
	Image contactLogo = new Image("img/contact1.png");
	Image vehiAlotLogo = new Image("img/TRUCKWs.png");

	public WelcomeScreen(String userName, String compName, String brnchName,
			String compRole, String userImage) {
		final FormPanel formPanel = new FormPanel();

		formPanel.setFrame(true);
		formPanel.setAutoWidth(false);
		formPanel.setHeight("259");
		setHeading(constants.Welcome());
		setModal(true);
		setButtons("");
		setSize("370", "280");
		setResizable(false);
		setFooter(false);
		setDraggable(false);
		btnOk.setWidth("75");
		btnOk.setText("OK");
		btnLogout.setWidth("75");
		btnLogout.setText(constants.Logout());
		btnLogout.setIcon(ImagesBundle.Util.get("ICON-Logout"));

		Image companyLogo = new Image();
		String companyurl = LoginDashboardModule.upload_File + "Company/";
		if (LoginDashboardModule.imageUrl != null) {
			companyurl = companyurl + LoginDashboardModule.imageUrl;
		} else {
			companyurl = companyurl + "CompanyLogo.png";
		}
		companyLogo.setSize("120", "50");
		companyLogo.setUrl(companyurl);
		companyLogo.setStyleName("image");

		Image fleetLogo = new Image();
		String fleeturl = LoginDashboardModule.upload_File + "User/";
		if (userImage != null) {
			fleeturl = fleeturl + userImage;
		} else {
			fleeturl = fleeturl + "User.png";
		}
		fleetLogo.setSize("120", "120");
		fleetLogo.setUrl(fleeturl);
		fleetLogo.setStyleName("image");

		flexTable.setSize("200", "190");
		flexTable.setStyleName("welcome");
		flexTable.setWidget(0, 0, new Image("img/user2.png"));
		flexTable.setWidget(0, 1, new HTML(constants.Name()));
		flexTable.setWidget(0, 2, new HTML(userName));

		flexTable.setWidget(1, 0, new Image("img/userRole.png"));
		flexTable.setWidget(1, 1, new HTML(constants.Role()));
		flexTable.setWidget(1, 2, new HTML(compRole));

		// flexTable.setWidget(2, 0, new Image("img/branch1.png"));
		// flexTable.setWidget(2, 1, new HTML("Company,Branch"));
		// flexTable.setWidget(2, 2, new HTML(brnchName + " &nbsp; " +
		// compName));

		flexTable.setWidget(3, 0, new Image("img/lastLogin.png"));
		flexTable.setWidget(3, 1, new HTML(constants.Date()));
		flexTable.setWidget(3, 2, new HTML(LoginDashboardModule.lastLoginTime));

		flexTable.setWidget(4, 0, new Image("img/contact1.png"));
		flexTable.setWidget(4, 1, new HTML(constants.Contact()));
		flexTable.setWidget(4, 2, new HTML("0998745120"));

		// flexTable.setWidget(5, 0, new Image("img/TRUCKWs.png"));
		// flexTable.setWidget(5, 1, new HTML(constants.Vehicles_alloted()));
		// flexTable.setWidget(5, 2, new HTML("2"));

		hp.setStyleName("background");
		vp.add(companyLogo);
		vp.add(fleetLogo);
		hp.add(vp);
		hp.add(flexTable);
		hp.setSize("100%", "50");
		formPanel.add(hp);
		formPanel.addButton(btnOk);
		formPanel.addButton(btnLogout);

		formPanel.setButtonAlign(HorizontalAlignment.RIGHT);
		formPanel.setHeaderVisible(false);
		formPanel.setScrollMode(Scroll.AUTO);
		add(formPanel);
		show();

		btnLogout.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				// TODO Auto-generated method stub
				Window.Location.reload();
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
		btnOk.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				// TODO Auto-generated method stub
				hide();

			}
		});
	}
}
