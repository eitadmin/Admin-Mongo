package com.eiw.client.adminportal;

import java.util.ArrayList;
import java.util.List;

import com.eiw.client.AdminPortalService;
import com.eiw.client.AdminPortalServiceAsync;
import com.eiw.client.dashboard.LoginDashboardModule;
import com.eiw.client.dto.VehicleData;
import com.eiw.client.fleetmgmt.DataReaderAdmin;
import com.eiw.client.gxtmodel.ModelVehicleData;
import com.eiw.client.icons.ImagesBundle;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.PagingModelMemoryProxy;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SMSCount extends VerticalPanel {

	private AdminPortalServiceAsync fleetMgmtService = GWT
			.create(AdminPortalService.class);
	ContentPanel contentPanel = new ContentPanel();
	Grid<ModelVehicleData> grid;
	Grid<ModelVehicleData> gridSmsHistory;
	ColumnModel cm;
	ListStore<ModelVehicleData> store;
	PagingLoader<PagingLoadResult<ModelData>> pagingLoader;
	PagingToolBar pagingBar;
	Button saveBtn = new Button("Save");
	TextField<Long> current = new TextField<Long>();
	TextField<Long> total = new TextField<Long>();
	String companyId;
	String branchId;
	String status;
	Dialog popup;
	String smsCount;

	public SMSCount(VerticalPanel bodyPanel1, final String companyId1,
			final String branchId1) {
		store = new ListStore<ModelVehicleData>();

		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		ColumnConfig column = new ColumnConfig();
		column.setId("companyId");
		column.setHeader("CompanyId");
		column.setWidth(275);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("branchId");
		column.setHeader("BranchId");
		column.setWidth(275);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("smsCount");
		column.setHeader("SMS Count");
		column.setWidth(275);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("status");
		column.setHeader("Status");
		column.setWidth(275);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("lastUpdBy");
		column.setHeader("Last Updated By");
		column.setWidth(275);

		column = new ColumnConfig();
		column.setHeader("Actions");
		column.setRenderer(new GridCellRenderer<ModelVehicleData>() {

			public Object render(final ModelVehicleData model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<ModelVehicleData> store,
					Grid<ModelVehicleData> grid) {

				ButtonBar buttonBar = new ButtonBar();
				final Button addButton = new Button();
				addButton.setIcon(ImagesBundle.Util.get("ICON-I_16add"));
				addButton.setTitle("Add");
				buttonBar.add(addButton);
				addButton
						.addSelectionListener(new SelectionListener<ButtonEvent>() {

							@Override
							public void componentSelected(ButtonEvent ce) {
								buttonSelectionMethod(addButton.getTitle(),
										model);
							}

						});
				final Button subButton = new Button();
				subButton.setIcon(ImagesBundle.Util.get("ICON-del"));
				subButton.setTitle("Sub");
				buttonBar.add(subButton);
				subButton
						.addSelectionListener(new SelectionListener<ButtonEvent>() {

							@Override
							public void componentSelected(ButtonEvent ce) {
								buttonSelectionMethod(subButton.getTitle(),
										model);
							}
						});
				Button showHistoryButton = new Button("Show History");
				showHistoryButton.setTitle("History");
				showHistoryButton
						.addSelectionListener(new SelectionListener<ButtonEvent>() {

							@Override
							public void componentSelected(ButtonEvent ce) {
								VehicleData vehi = new VehicleData();
								vehi.setCompanyId(model.getCompanyId());
								vehi.setBranchId(model.getBranchId());
								fleetMgmtService.getSmsHistory(vehi,
										new AsyncCallback<List<VehicleData>>() {

											@Override
											public void onSuccess(
													List<VehicleData> vehicleData) {

												Dialog popup = new Dialog();
												popup.setHeading("History");
												HorizontalPanel hp = new HorizontalPanel();
												hp.add(new HTML("Value"));
												hp.add(new HTML("Date"));
												hp.setSpacing(10);
												popup.add(hp);
												FlexTable flex = new FlexTable();
												for (int i = 0; i < vehicleData
														.size(); i++) {
													Label label1 = new Label();
													Label label2 = new Label();
													label1.setText(vehicleData
															.get(i)
															.getSmsNumber());
													label2.setText(vehicleData
															.get(i)
															.getLastUpdDate());
													flex.setWidget(i, 0, label1);
													flex.setWidget(i, 1, label2);
													flex.setCellSpacing(13);
												}
												popup.add(flex);
												popup.setButtons("");
												popup.setModal(true);
												popup.show();
											}

											@Override
											public void onFailure(Throwable arg0) {
											}
										});
							}
						});

				buttonBar.add(showHistoryButton);
				return buttonBar;
			}

		});
		saveBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				// TODO Auto-generated method stub
				String totalvalue = String.valueOf(total.getValue());
				VehicleData vehicleData = new VehicleData();
				vehicleData.setCompanyId(companyId);
				vehicleData.setBranchId(branchId);
				vehicleData.setStatus(status);
				vehicleData.setSmsNumber(smsCount);
				if ((totalvalue == null) || (totalvalue.equalsIgnoreCase(""))
						|| (totalvalue.equalsIgnoreCase("null"))) {

				} else {
					vehicleData.setEventId(Long.valueOf(totalvalue));
					fleetMgmtService.updateSMSCount(vehicleData,
							new AsyncCallback<String>() {

								@Override
								public void onSuccess(String status) {
									// TODO Auto-generated method stub
									if (status.trim().equalsIgnoreCase(
											"success")) {
										popup.hide();
										MessageBox.alert("SMSCount",
												"Updated Successfully", null);
										serviceForGridReconfigure();
									}
								}

								@Override
								public void onFailure(Throwable arg0) {
									// TODO Auto-generated method stub

								}
							});
				}
			}
		});
		column.setWidth(250);
		configs.add(column);

		cm = new ColumnModel(configs);
		grid = new Grid<ModelVehicleData>(store, cm);
		contentPanel.setHeading("SMS Count");
		contentPanel.setHeaderVisible(false);
		contentPanel.add(grid);
		contentPanel.setSize(1358, 483);
		grid.setSize(1358, 483);
		pagingBar = new PagingToolBar(10);
		pagingBar.bind(pagingLoader);
		contentPanel.setBottomComponent(pagingBar);
		bodyPanel1.add(contentPanel);
		serviceForGridReconfigure();
	}

	public void serviceForGridReconfigure() {
		fleetMgmtService.getSMSCount(LoginDashboardModule.suffix,
				new AsyncCallback<List<VehicleData>>() {

					@Override
					public void onSuccess(List<VehicleData> result) {
						// TODO Auto-generated method stub
						PagingModelMemoryProxy pagingProxy = new PagingModelMemoryProxy(
								DataReaderAdmin.getSMSCount(result));
						pagingLoader = new BasePagingLoader<PagingLoadResult<ModelData>>(
								pagingProxy);
						pagingLoader.setRemoteSort(true);
						store = new ListStore<ModelVehicleData>(pagingLoader);
						pagingBar.bind(pagingLoader);
						pagingLoader.load(0, 10);
						grid.reconfigure(store, cm);
					}

					@Override
					public void onFailure(Throwable arg0) {
						// TODO Auto-generated method stub

					}
				});
	}

	public void buttonSelectionMethod(String title, ModelVehicleData model) {

		// TODO Auto-generated method stub
		popup = new Dialog();
		ModelVehicleData modelVehicleData = model;
		popup.setSize(350, 150);
		Label lbl = new Label("Current SMS ");
		Label sym = new Label();
		final Label errorMsg = new Label();
		final TextField<Long> additional = new TextField<Long>();
		additional.setAllowBlank(false);
		current.setWidth(45);
		additional.setWidth(45);
		total.setWidth(45);
		total.setEnabled(false);
		current.setEnabled(false);
		current.setValue(modelVehicleData.getSmsCount());
		companyId = modelVehicleData.getCompanyId();
		branchId = modelVehicleData.getBranchId();
		status = modelVehicleData.getStatus();
		saveBtn.setEnabled(false);

		boolean blnExists = LoginDashboardModule.isMiniHashMap
				.containsKey(companyId);

		if (blnExists) {
			MessageBox
					.info("Company SMS Details",
							companyId
									+ " Subscribed mini apps. If you want to upgrade contact system administrator",
							null);
		} else {
			final FlexTable flex = new FlexTable();
			flex.setWidget(0, 0, lbl);
			flex.setWidget(0, 1, current);
			flex.setWidget(0, 2, sym);
			flex.setWidget(0, 3, additional);
			flex.setWidget(0, 4, new HTML("="));
			flex.setWidget(0, 5, total);
			flex.setWidget(1, 1, saveBtn);
			flex.setWidget(2, 0, errorMsg);
			if (title.equalsIgnoreCase("Add")) {
				popup.setHeading("Add-SMS Count");
				total.clear();
				sym.setText("+");
				additional.addListener(Events.OnBlur,
						new Listener<BaseEvent>() {

							@Override
							public void handleEvent(BaseEvent be) {

								String currentValue = String.valueOf(current
										.getValue());
								if (additional.getValue() != null) {
									String additionalValue = String
											.valueOf(additional.getValue());
									long totalValue = Long
											.valueOf(currentValue)
											+ Long.valueOf(additionalValue);
									total.setValue(totalValue);
									saveBtn.setEnabled(true);
									errorMsg.setVisible(false);
									smsCount = additionalValue;
								} else {
									saveBtn.setEnabled(false);
									errorMsg.setVisible(true);
									errorMsg.setText("Please Enter Values");
								}
							}
						});
			} else if (title.equalsIgnoreCase("Sub")) {
				popup.setHeading("Subtract-SMS Count");
				total.clear();
				sym.setText("-");
				additional.addListener(Events.OnBlur,
						new Listener<BaseEvent>() {

							@Override
							public void handleEvent(BaseEvent be) {

								String currentValue = String.valueOf(current
										.getValue());
								if (additional.getValue() != null) {
									String additionalValue = String
											.valueOf(additional.getValue());
									if (Long.valueOf(additionalValue) < Long
											.valueOf(currentValue)) {
										long totalValue = Long
												.valueOf(currentValue)
												- Long.valueOf(additionalValue);
										total.setValue(totalValue);
										saveBtn.setEnabled(true);
										errorMsg.setVisible(false);
										smsCount = "-" + additionalValue;
									} else {
										saveBtn.setEnabled(false);
										errorMsg.setVisible(true);
										errorMsg.setText("Value should be less than "
												+ currentValue);
									}
								} else {
									saveBtn.setEnabled(false);
									errorMsg.setVisible(true);
									errorMsg.setText("Please Enter Values");
								}
							}
						});
			}
			popup.add(flex);
			popup.setButtons("");
			popup.setModal(true);
			popup.show();
		}

	}
}
