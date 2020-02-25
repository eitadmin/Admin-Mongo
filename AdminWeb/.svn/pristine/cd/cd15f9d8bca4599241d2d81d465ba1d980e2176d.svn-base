package com.eiw.client.dashboard;

import java.util.List;

import com.eiw.client.AdminPortalService;
import com.eiw.client.AdminPortalServiceAsync;
import com.eiw.client.LoginConstants;
import com.eiw.client.dto.OperatorData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SMSGadget extends VerticalPanel {
	private AdminPortalServiceAsync fleetMgmtService = GWT
			.create(AdminPortalService.class);
	private LoginConstants constants = GWT.create(LoginConstants.class);

	List<OperatorData> opData;

	ListBox listBox = new ListBox();

	Label opLabel = new Label();
	Label telLabel = new Label();
	Label dispplateLabel = new Label(constants.Plate_Number());
	Label disptelLabel = new Label(constants.Mobile_Number());
	Label dispopLabel = new Label(constants.Operator_Name());
	Label msg = new Label(constants.Message_Sent_Successfully());
	Label Wmsg = new Label(
			constants.Welcome_to_Logistics_Technologies_Services());
	Dialog dialog = new Dialog();
	OperatorData operatorData = new OperatorData();
	FlexTable flexTable = new FlexTable();
	TextArea txtMsg = new TextArea();

	public SMSGadget() {

		Label phLbl = new Label();
		phLbl.setStyleName("formLabel");

		dialog.setHideOnButtonClick(true);
		// dialog.setSize(500, 400);
		dialog.setBodyBorder(false);

		Button sendBtn = new Button(constants.Send());
		Button resetBtn = new Button(constants.Reset());
		Button cancelBtn = new Button(constants.Cancel());
		dialog.setHeading(constants.SMS_Gadget());
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.add(sendBtn);
		horizontalPanel.add(resetBtn);
		horizontalPanel.add(cancelBtn);
		horizontalPanel.setStyleName("hPanelBtn");
		listBox.setStyleName("formLabel");
		txtMsg.setText("");
		flexTable.setWidget(1, 0, dispplateLabel);
		flexTable.setWidget(1, 1, listBox);
		flexTable.setWidget(2, 0, disptelLabel);
		flexTable.setWidget(2, 1, telLabel);
		flexTable.setWidget(3, 0, dispopLabel);
		flexTable.setWidget(3, 1, opLabel);
		flexTable.setWidget(4, 0, new HTML(constants.Message()));
		flexTable.setWidget(4, 1, txtMsg);
		flexTable.setWidget(5, 0, horizontalPanel);
		flexTable.setStyleName("formLabel");
		flexTable.setCellSpacing(7);
		flexTable.getFlexCellFormatter().setColSpan(5, 0, 2);
		flexTable.getFlexCellFormatter().setHorizontalAlignment(5, 0,
				ALIGN_CENTER);
		// formPanel.add(flexTable);
		dialog.add(flexTable);
		dialog.setButtons("");
		dialog.show();

		fleetMgmtService.getOperatorTelNo(LoginDashboardModule.companyId,
				LoginDashboardModule.branchId, LoginDashboardModule.userName,
				new AsyncCallback<List<OperatorData>>() {
					public void onFailure(Throwable arg0) {
						HeaderPanel.lblMsg.setText("Unable to Connect...");

					}

					@Override
					public void onSuccess(List<OperatorData> operatorDatas) {
						if (operatorDatas.size() == 0) {
							// dialog.hide();
							MessageBox.alert("",
									constants.No_Operators_Available(), null);
							dialog.hide();
						} else {
							for (OperatorData operatorData : operatorDatas) {
								opData = operatorDatas;
								listBox.addItem(operatorData.getPlateNo(),
										operatorData.getVin());
								if (operatorData.getVin().equalsIgnoreCase(
										listBox.getValue(listBox
												.getSelectedIndex()))) {
									opLabel.setText(operatorData
											.getOperatorID());
									telLabel.setText(operatorData.getTelNo());
								}

							}
						}
					}

				});

		listBox.addChangeListener(new ChangeListener() {

			@Override
			public void onChange(Widget arg0) {
				for (OperatorData operatorData : opData) {
					if (operatorData.getVin().equalsIgnoreCase(
							listBox.getValue(listBox.getSelectedIndex()))) {
						opLabel.setText(operatorData.getOperatorID());

						telLabel.setText(operatorData.getTelNo());
					}
				}
			}
		});

		sendBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				System.out.println(telLabel.getText());
				System.out.println(txtMsg.getText());
				fleetMgmtService.sendSMS(telLabel.getText(), txtMsg.getText(),
						new AsyncCallback<String>() {

							@Override
							public void onFailure(Throwable arg0) {
							}

							@Override
							public void onSuccess(String status) {
								// Window.alert("The output is "
								// + status);
								msg.setStyleName("alertBlue");
								flexTable.setWidget(0, 0, msg);
								flexTable.getFlexCellFormatter().setColSpan(0,
										0, 2);
								txtMsg.setText("");
							}
						});
			}
		});
		cancelBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				dialog.hide();
				listBox.clear();
			}
		});
		resetBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				// txtMob.setText("");
				txtMsg.setText("");
			}
		});
	}
}
