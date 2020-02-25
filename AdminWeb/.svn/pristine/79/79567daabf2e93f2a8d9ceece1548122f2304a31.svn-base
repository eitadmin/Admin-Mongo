package com.eiw.client.dashboard;

import com.eiw.client.LoginConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LoginPanel extends DockPanel {
	private LoginConstants constants = GWT.create(LoginConstants.class);
	private final TextBox corpBox = new TextBox();
	private final TextBox userBox = new TextBox();
	private final PasswordTextBox pwdBox = new PasswordTextBox();
	private final Button loginButton = new Button(constants.login());
	Label langLabel = new Label(constants.langSelect());
	Label corpLabel = new Label(constants.corporateid());
	Label userLabel = new Label(constants.userid());
	Label passwordLabel = new Label(constants.password());
	Label lblError = new Label();
	Image logoImg = new Image("img/logo.jpg");
	VerticalPanel loginPanel = new VerticalPanel();
	FlexTable flexTable = new FlexTable();
	ListBox listBox = new ListBox();

	public LoginPanel() {

		corpBox.getElement().setPropertyString("placeholder", "Corporate ID");
		userBox.getElement().setPropertyString("placeholder", "User ID");
		pwdBox.getElement().setPropertyString("placeholder", "Password");
		this.setWidth(Window.getClientWidth() + "");
		this.setHeight(Window.getClientHeight() + "");
		setVerticalAlignment(ALIGN_MIDDLE);
		setHorizontalAlignment(ALIGN_CENTER);
		listBox.setStyleName("ddlBox");
		userBox.setStyleName("textBox2");
		corpBox.setStyleName("textBox1");
		pwdBox.setStyleName("textBox3");
		loginButton.setStyleName("trueLoginButton1");
		loginPanel.setStyleName("loginPanel");
		flexTable.setStyleName("loginFlexTable");

		corpBox.setText("");
		userBox.setText("");
		pwdBox.setText("");

		lblError.setStyleName("textbox_alert_label1");

		HorizontalPanel tpanel = new HorizontalPanel();

		flexTable.setCellSpacing(5);

		flexTable.setWidget(1, 2, corpBox);
		flexTable.setWidget(2, 2, userBox);
		flexTable.setWidget(3, 2, pwdBox);
		flexTable.setWidget(4, 2, lblError);
		flexTable.setWidget(5, 0, loginButton);
		flexTable.setWidget(6, 2, tpanel);
		flexTable.getFlexCellFormatter().setColSpan(5, 0, 3);

		flexTable.getFlexCellFormatter().setHeight(0, 1, "30");
		flexTable.getFlexCellFormatter().setHeight(1, 1, "30");
		flexTable.getFlexCellFormatter().setHeight(2, 1, "30");
		flexTable.getFlexCellFormatter().setHeight(3, 1, "30");
		flexTable.getFlexCellFormatter().setHeight(4, 2, "12");
		flexTable.getFlexCellFormatter().setHeight(5, 0, "30");
		flexTable.getFlexCellFormatter().setHeight(6, 2, "30");

		flexTable.getFlexCellFormatter().setHorizontalAlignment(3, 0,
				HasHorizontalAlignment.ALIGN_CENTER);
		flexTable.getFlexCellFormatter().setHorizontalAlignment(0, 1,
				ALIGN_LEFT);
		flexTable.getFlexCellFormatter().setHorizontalAlignment(1, 1,
				ALIGN_LEFT);
		flexTable.getFlexCellFormatter().setHorizontalAlignment(2, 1,
				ALIGN_LEFT);
		flexTable.getFlexCellFormatter().setHorizontalAlignment(0, 2,
				ALIGN_LEFT);
		flexTable.getFlexCellFormatter().setHorizontalAlignment(1, 2,
				ALIGN_LEFT);
		flexTable.getFlexCellFormatter().setHorizontalAlignment(2, 2,
				ALIGN_LEFT);
		flexTable.getFlexCellFormatter().setHorizontalAlignment(3, 1,
				ALIGN_LEFT);
		flexTable.getFlexCellFormatter().setHorizontalAlignment(3, 2,
				ALIGN_LEFT);
		flexTable.getFlexCellFormatter().setHorizontalAlignment(4, 1,
				ALIGN_CENTER);
		flexTable.getFlexCellFormatter().setHorizontalAlignment(5, 0,
				ALIGN_CENTER);
		flexTable.getFlexCellFormatter().setHorizontalAlignment(6, 2,
				ALIGN_CENTER);

		HorizontalPanel panel = new HorizontalPanel();
		FlexTable flex = new FlexTable();

		loginPanel.add(panel);
		loginPanel.add(flexTable);
		add(loginPanel, DockPanel.CENTER);

	}

	public TextBox getUserBox() {
		return userBox;
	}

	public PasswordTextBox getPwdBox() {
		return pwdBox;
	}

	public Button getLoginButton() {
		return loginButton;
	}

	public TextBox getCorpBox() {
		return corpBox;
	}

	public Label getLblError() {
		return lblError;
	}
}
