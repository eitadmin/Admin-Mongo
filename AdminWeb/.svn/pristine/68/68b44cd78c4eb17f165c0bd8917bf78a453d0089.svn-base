package com.eiw.client.dashboard;

import java.util.List;

import com.eiw.client.AdminPortalService;
import com.eiw.client.AdminPortalServiceAsync;
import com.eiw.client.LoginConstants;
import com.eiw.client.dto.VehicleData;
import com.eiw.client.gxtmodel.Messagebox;
import com.eiw.client.gxtmodel.MessageboxEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BatchListeners extends VerticalPanel {
	private AdminPortalServiceAsync fleetMgmtService = GWT
			.create(AdminPortalService.class);
	private LoginConstants constants = GWT.create(LoginConstants.class);
	Button btnStartListener = new Button("Start Listener");
	Button btnStopListener = new Button("Stop Listener");
	Button btnStartSimulator = new Button("Start Simulator");
	Button btnStopSimulator = new Button("Stop Simulator");
	Button btnStopSkywave = new Button("Stop Skywave");
	FlexTable flexTableBtns = new FlexTable();
	TextBox txtImei = new TextBox();
	boolean isBtnPress = false;

	public BatchListeners() {
		btnStartListener.setWidth(80);
		btnStopListener.setWidth(80);
		btnStartSimulator.setWidth(80);
		btnStopSimulator.setWidth(80);

		flexTableBtns.setWidget(0, 0, new HTML("Listener"));
		flexTableBtns.setWidget(0, 2, btnStartListener);
		flexTableBtns.setWidget(0, 3, btnStopListener);
		flexTableBtns.setWidget(1, 0, new HTML("Simulator"));
		flexTableBtns.setWidget(1, 1, txtImei);
		flexTableBtns.setWidget(1, 2, btnStartSimulator);
		flexTableBtns.setWidget(1, 3, btnStopSimulator);
		flexTableBtns.setWidget(2, 0, btnStopSkywave);
		flexTableBtns.setStyleName("flexio");
		add(flexTableBtns);

		btnStartListener.setEnabled(true);
		btnStopListener.setEnabled(false);
		fleetMgmtService
				.btnEnableDisable(new AsyncCallback<List<VehicleData>>() {
					public void onSuccess(List<VehicleData> vehicleDatas) {
						if (vehicleDatas != null && vehicleDatas.size() > 0) {
							isBtnPress = vehicleDatas.get(
									vehicleDatas.size() - 1).getChkStatus();
							if (isBtnPress) {
								btnStopListener.setEnabled(true);
								btnStartListener.setEnabled(false);
							}
						}
					}

					public void onFailure(Throwable arg0) {
					}
				});

		btnStartListener
				.addSelectionListener(new SelectionListener<ButtonEvent>() {
					public void componentSelected(ButtonEvent ce) {
						fleetMgmtService.startBatchListener(
								LoginDashboardModule.userName,
								new AsyncCallback<List<VehicleData>>() {
									public void onSuccess(
											List<VehicleData> vehicleDatas) {
										if (vehicleDatas.size() > 0) {
											isBtnPress = vehicleDatas.get(
													vehicleDatas.size() - 1)
													.getChkStatus();
											if (isBtnPress) {
												btnStopListener
														.setEnabled(true);
												btnStartListener
														.setEnabled(false);
											}
											String msg = vehicleDatas.get(
													vehicleDatas.size() - 1)
													.getThroughEmail();

											MessageBox.alert("", msg, null);
										}
									}

									public void onFailure(Throwable arg0) {
									}
								});
					}
				});

		btnStopListener
				.addSelectionListener(new SelectionListener<ButtonEvent>() {
					public void componentSelected(ButtonEvent ce) {
						fleetMgmtService.stopBatchListener(
								LoginDashboardModule.userName,
								new AsyncCallback<List<VehicleData>>() {
									public void onSuccess(
											List<VehicleData> vehicleDatas) {
										if (vehicleDatas.size() > 0) {
											isBtnPress = vehicleDatas.get(
													vehicleDatas.size() - 1)
													.getChkStatus();
											if (!isBtnPress) {
												btnStopListener
														.setEnabled(false);
												btnStartListener
														.setEnabled(true);
											}
											String msg = vehicleDatas.get(
													vehicleDatas.size() - 1)
													.getThroughEmail();

											MessageBox.alert("", msg, null);
										}
									}

									public void onFailure(Throwable arg0) {
									}
								});
					}
				});

		btnStartSimulator
				.addSelectionListener(new SelectionListener<ButtonEvent>() {
					public void componentSelected(ButtonEvent ce) {
						if (!txtImei.getValue().trim().equalsIgnoreCase("")) {
							fleetMgmtService.startSimulator(txtImei.getValue(),
									new AsyncCallback<String>() {
										public void onSuccess(String status) {
											MessageBox.alert("Batch", status,
													null);
										}

										public void onFailure(Throwable arg0) {
										}
									});
						} else {
							MessageBox.alert("Batch", "Please Enter Imei No",
									null);
						}
					}
				});

		btnStopSimulator
				.addSelectionListener(new SelectionListener<ButtonEvent>() {
					public void componentSelected(ButtonEvent ce) {
						if (!txtImei.getValue().trim().equalsIgnoreCase("")) {
							fleetMgmtService.stopSimulator(txtImei.getValue(),
									new AsyncCallback<String>() {
										public void onSuccess(String status) {
											MessageBox.alert("Batch", status,
													null);
										}

										public void onFailure(Throwable arg0) {
										}
									});
						} else {
							MessageBox.alert("Batch", "Please Enter Imei No",
									null);
						}
					}
				});

		btnStopSkywave
				.addSelectionListener(new SelectionListener<ButtonEvent>() {
					public void componentSelected(ButtonEvent ce) {
						Messagebox box = Messagebox.prompt(
								constants.Authentication(),
								constants.Please_enter_password(),
								new Listener<MessageboxEvent>() {

									@Override
									public void handleEvent(MessageboxEvent be) {
										if (be.getButtonClicked().getText()
												.equalsIgnoreCase("OK")) {
											System.out
													.println(be.getMessageBox()
															.getTextBox()
															.getRawValue());
											String pwd = be.getValue();
											if (pwd != null
													&& pwd.equals(LoginDashboardModule.compRole)) {
												fleetMgmtService
														.stopApplication(new AsyncCallback<String>() {

															@Override
															public void onFailure(
																	Throwable arg0) {
																// TODO
																// Auto-generated
																// method stub

															}

															@Override
															public void onSuccess(
																	String arg0) {
																// TODO
																// Auto-generated
																// method stub

															}

														});
											} else {
												Messagebox.alert(
														constants
																.Invalid_Password(),
														constants
																.Password_Wrong_Kindly_contact_the_System_Administrator(),
														null);
											}
										}
									}
								});
					}
				});
	}
}
