package com.eiw.client.adminportal;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.eiw.client.AdminPortalService;
import com.eiw.client.AdminPortalServiceAsync;
import com.eiw.client.dashboard.HeaderPanel;
import com.eiw.client.dashboard.LoginDashboardModule;
import com.eiw.client.dto.CompanyDataAdmin;
import com.eiw.client.dto.VehicleData;
import com.eiw.client.fleetmgmt.DataReaderAdmin;
import com.eiw.client.gxtmodel.ModelVehicleData;
import com.eiw.client.icons.ImagesBundle;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.PagingModelMemoryProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.util.DateWrapper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridGroupRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupColumnData;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class VehicleEventReport extends LayoutContainer {
	private AdminPortalServiceAsync fleetMgmtService = GWT
			.create(AdminPortalService.class);
	String userId = LoginDashboardModule.userName;
	DateField fromDatefield = new DateField();
	DateField toDatefield = new DateField();
	Button btnOk = new Button("Go");
	// Button pdfBtn = new Button("PDF");
	// Button printBtn = new Button("Print");
	PagingLoader<PagingLoadResult<ModelData>> pagingLoader;
	Grid<ModelVehicleData> grid;
	GroupingStore<ModelVehicleData> store;
	ColumnModel cm;
	GroupingView view;
	private FormBinding formBindings;
	ContentPanel cp = new ContentPanel();
	ContentPanel cp1 = new ContentPanel();
	Label lblfromDate = new Label("From");
	Label lbltoDate = new Label("To");
	Label lblVehicle = new Label("Select Vehicle");
	Label lblIO = new Label("IO Event");
	ListBox listValue = new ListBox();
	ListBox listBox = new ListBox();
	HorizontalPanel hp = new HorizontalPanel();
	HorizontalPanel hp1 = new HorizontalPanel();
	ToolBar toolBar = new ToolBar();
	List<CompanyDataAdmin> compList1 = null;
	DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd");
	String fromDate, toDate;
	PagingToolBar pagingBar;
	Date date1 = new Date();
	DateWrapper date = new DateWrapper(new Date());
	Image box1 = new Image("img/loading12.gif");
	VerticalPanel vp = new VerticalPanel();
	HashMap<String, String> ioHashMap = new HashMap<String, String>();
	String ioEventDetailsValue;

	public VehicleEventReport(String compName, String brnchName,
			String userName, String role, VerticalPanel bodyPanel1) {
		store = new GroupingStore<ModelVehicleData>();

		String ioDetails = "1=Digital Input Status 1,2=Digital Input Status 2,3=Digital Input Status 3,4=Digital Input 4,9=Analog Input 1,10=Analog Input 2,11=Analog Input 3,19=Analog Input 4,21=GSM Signal Strength,22=Current Profile,23=Accelerometer data,24=GPS speed,66=Power Supply Voltage,67=Battery Voltage,68=Battery Current,69=GPS Power,70=PCB Temperature,72=Temperature Sensor1,73=Temperature Sensor2,74=Temperature Sensor3,76=Fuel Counter,78=iButton Input,145=CAN 0,146=CAN 1,147=CAN 2,148=CAN 3,149=CAN 4,150=CAN 5,151=CAN 6,152=CAN 7,153=CAN 8,154=CAN 9,155=Geozone 01,156=Geozone 02,157=Geozone 03,158=Geozone 04,159=Geozone 05,160=Geozone 06,161=Geozone 07,162=Geozone 08,163=Geozone 09,164=Geozone 10,165=Geozone 11,166=Geozone 12,167=Geozone 13,168=Geozone 14,169=Geozone 15,170=Geozone 16,171=Geozone 17,172=Geozone 18,173=Geozone 19,174=Geozone 20,199=Virtual Odometer,240=Movement";
		String[] ioParamsArray = ioDetails.split("\\,");
		listValue.addItem("All");
		for (String a : ioParamsArray) {
			int indexOfEqual = a.indexOf("=");
			ioHashMap.put((a.substring(0, indexOfEqual)).trim(),
					(a.substring(indexOfEqual + 1)).trim());
			listValue.addItem(a.substring(indexOfEqual + 1),
					a.substring(0, indexOfEqual));
		}
		listValue.setWidth("100");
		listBox.setWidth("100");
		fromDatefield.setWidth("100");
		toDatefield.setWidth("100");
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		ColumnConfig column = new ColumnConfig();
		column.setId("eventTimeStamp");
		column.setHeader("Event TimeStamp");
		column.setWidth(110);
		column.setGroupable(false);
		column.setSortable(false);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("serverTimeStamp");
		column.setHeader("Server TimeStamp");
		column.setSortable(false);
		column.setWidth(110);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("speed");
		column.setHeader("Speed");
		column.setWidth(110);
		column.setGroupable(false);
		column.setSortable(false);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("bytesTrx");
		column.setHeader("Bytes Transmitted");
		column.setWidth(110);
		column.setGroupable(false);
		column.setSortable(false);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("eventSource");
		column.setHeader("EventSource Id");
		column.setWidth(110);
		column.setGroupable(false);
		column.setSortable(false);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("ioEvent");
		column.setHeader("IO Event");
		column.setWidth(105);
		column.setGroupable(false);
		column.setSortable(false);
		configs.add(column);

		view = new GroupingView();
		view.setShowGroupedColumn(false);
		view.setForceFit(true);
		view.setGroupRenderer(new GridGroupRenderer() {
			public String render(GroupColumnData data) {
				return data.group;
			}
		});
		store.groupBy("groupByDate");
		cm = new ColumnModel(configs);
		grid = new Grid<ModelVehicleData>(store, cm);
		grid.setSize(1036, 483);
		grid.setView(view);
		grid.setBorders(true);
		grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		// printBtn.setScale(ButtonScale.MEDIUM);
		// printBtn.setBorders(true);
		// pdfBtn.setScale(ButtonScale.MEDIUM);
		// pdfBtn.setBorders(true);
		// pdfBtn.setIcon(ImagesBundle.Util.get("ICON-pdf"));
		// printBtn.setIcon(ImagesBundle.Util.get("ICON-print"));
		// printBtn.setEnabled(false);
		// pdfBtn.setEnabled(false);
		// hp.add(pdfBtn);
		// hp.add(printBtn);
		String currentDate = (date1.getYear() + 1900) + "-"
				+ (date1.getMonth() + 1) + "-01";
		fromDatefield.setValue(dtf.parse(currentDate));
		fromDatefield.setMaxValue(new Date());
		toDatefield.setMaxValue(new Date());
		toDatefield.setValue(date1);
		btnOk.setIcon(ImagesBundle.Util.get("ICON-16Go"));
		btnOk.setBorders(true);
		hp.setSpacing(3);
		hp.add(lblVehicle);
		hp.add(listBox);
		hp.add(lblIO);
		hp.add(listValue);
		hp.add(lblfromDate);
		hp.add(fromDatefield);
		hp.add(lbltoDate);
		hp.add(toDatefield);
		hp.add(btnOk);
		toolBar.add(hp);
		toolBar.setSpacing(2);
		cp.setHeading("Vehicle Event Report");
		cp.setHeaderVisible(false);
		cp.setTopComponent(toolBar);
		cp.setSize(1036, 483);
		cp.setBodyBorder(false);
		cp.add(grid);
		FormPanel panel = createForm();
		panel.setHeight("130");
		formBindings = new FormBinding(panel, true);
		pagingBar = new PagingToolBar(13);
		pagingBar.bind(pagingLoader);
		cp.setIcon(ImagesBundle.Util.get("ICON-lastLogin"));
		cp.setBottomComponent(pagingBar);
		cp1.setHeading("Details");
		cp1.setSize(322, 483);
		cp1.add(panel);
		vp.setHeight("300");
		cp1.add(vp);
		hp1.add(cp);
		hp1.add(cp1);
		bodyPanel1.add(hp1);
		fleetMgmtService.getLiveVehicles(LoginDashboardModule.suffix,
				new AsyncCallback<List<VehicleData>>() {

					public void onFailure(Throwable arg0) {
						HeaderPanel.lblMsg.setText("Unable to Connect...");
					}

					@Override
					public void onSuccess(final List<VehicleData> vehicleDatas) {

						System.out.println("size = " + vehicleDatas.size());
						for (int i = 0; i < vehicleDatas.size(); i++) {
							System.out.println("size = "
									+ vehicleDatas.get(i).getPlateNo());
							listBox.addItem(vehicleDatas.get(i).getPlateNo(),
									vehicleDatas.get(i).getVin());
						}

					}

				});

		btnOk.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				VehicleData vehicleData = new VehicleData();
				if (fromDatefield.getValue() == null) {
					fromDate = null;
				} else {
					fromDate = dtf.format(fromDatefield.getValue());
				}
				if (toDatefield.getValue() == null) {
					toDate = null;
				} else {
					toDate = dtf.format(toDatefield.getValue());
				}
				if (fromDate == null) {
					MessageBox.alert("Vehicle Event Report",
							"Please Select from Date", null);
				} else if (toDate == null) {
					MessageBox.alert("Vehicle Event Report",
							"Please Select to Date", null);
				} else if (fromDatefield.getValue().after(
						toDatefield.getValue())) {
					MessageBox.alert("Vehicle Event Report",
							"To Date should be greater than From Date()", null);
				} else {
					// pdfBtn.setEnabled(true);
					// printBtn.setEnabled(true);
					LoginDashboardModule.box1.setVisible(true);
					System.out.println("fromDate" + fromDate);
					System.out.println("toDate" + toDate);
					ioEventDetailsValue = listValue.getValue(listValue
							.getSelectedIndex());
					vehicleData.setfromDate(fromDate);
					vehicleData.setToDate(toDate);
					vehicleData.setVin(listBox.getValue(listBox
							.getSelectedIndex()));
					vehicleData.setEventStatus(ioEventDetailsValue);
					fleetMgmtService.getVehicleEventInfo(vehicleData,
							new AsyncCallback<List<VehicleData>>() {

								@Override
								public void onFailure(Throwable arg0) {

								}

								@Override
								public void onSuccess(List<VehicleData> result) {
									LoginDashboardModule.box1.setVisible(false);
									if (result.size() == 0) {
										store.removeAll();
										MessageBox.alert(
												"Vehicle Event Report",
												"No Data Available!", null);
										// pdfBtn.setEnabled(false);
										// printBtn.setEnabled(false);
									} else {
										PagingModelMemoryProxy pagingProxy = new PagingModelMemoryProxy(
												DataReaderAdmin
														.getVehicleEventData(result));
										pagingLoader = new BasePagingLoader<PagingLoadResult<ModelData>>(
												pagingProxy);
										pagingLoader.setRemoteSort(true);
										store = new GroupingStore<ModelVehicleData>(
												pagingLoader);
										pagingBar.bind(pagingLoader);
										pagingLoader.load(0, 13);
										store.groupBy("groupByDate");
										grid.reconfigure(store, cm);
									}
									grid.getSelectionModel()
											.addListener(
													Events.SelectionChange,
													new Listener<SelectionChangedEvent<ModelVehicleData>>() {

														@Override
														public void handleEvent(
																SelectionChangedEvent<ModelVehicleData> be) {
															if (be.getSelection()
																	.size() > 0) {
																formBindings
																		.bind((ModelVehicleData) be
																				.getSelection()
																				.get(0));
																String ioEvent = be
																		.getSelection()
																		.get(0)
																		.getIOEvent();
																String ioEventValues = be
																		.getSelection()
																		.get(0)
																		.getIoEventDetails();
																ioParameters(
																		ioEvent,
																		ioEventValues);
															} else {
																formBindings
																		.unbind();
															}
														}

													});
								}
							});
				}
			}
		});
	}

	private void ioParameters(String ioevents, String params) {
		// ioevents
		String[] ioEventsArray = ioevents.split("\\,");
		HashMap<String, String> ioEventsMap = new HashMap<String, String>();
		for (String a : ioEventsArray) {
			int indexOfEqual = a.indexOf("=");
			ioEventsMap.put((a.substring(0, indexOfEqual)).trim(),
					(a.substring(indexOfEqual + 1)).trim());
		}
		// ioparams
		String[] ioParamsArray = params.split("\\,");
		HashMap<String, String> ioParamsMap = new HashMap<String, String>();
		for (String a : ioParamsArray) {
			int indexOfEqual = a.indexOf("=");
			ioParamsMap.put((a.substring(0, indexOfEqual)).trim(),
					(a.substring(indexOfEqual + 1)).trim());
		}
		// run time flextable
		FlexTable runFlexTable = new FlexTable();

		Iterator itEvents = ioEventsMap.entrySet().iterator();

		int i = 0;
		while (itEvents.hasNext()) {
			Map.Entry pairsEvents = (Map.Entry) itEvents.next();
			System.out.println("Events::::   " + pairsEvents.getKey() + " = "
					+ pairsEvents.getValue());
			String keyEvents = pairsEvents.getKey().toString();

			Iterator itParams = ioParamsMap.entrySet().iterator();
			while (itParams.hasNext()) {
				Map.Entry pairsParams = (Map.Entry) itParams.next();
				System.out.println("Params::::   " + pairsParams.getKey()
						+ " = " + pairsParams.getValue());
				String keyParams = pairsParams.getKey().toString();

				if (keyEvents.equalsIgnoreCase(keyParams)) {
					System.out.println("I am inside for " + keyEvents);
					runFlexTable.setWidget(i, 0, new HTML(pairsParams
							.getValue().toString()));
					runFlexTable.setWidget(i, 1, new HTML(pairsEvents
							.getValue().toString()));
					i++;
				}

			}
		}
		for (int si = 0; si < 20; si++) {
			if (si % 2 == 0) {
				runFlexTable.getRowFormatter().setStyleName(si,
						"rowEven-FlexTable");
			} else {
				runFlexTable.getRowFormatter().setStyleName(si,
						"rowOdd-FlexTable");
			}
		}
		runFlexTable.setWidth("100%");
		vp.setWidth("100%");
		vp.clear();
		vp.add(runFlexTable);
		// ioDetailsVPanel.add(ioDtlPanel);
	}

	private FormPanel createForm() {
		FormPanel panel = new FormPanel();
		panel.setFrame(true);
		panel.setHeaderVisible(false);
		TextField<String> lat = new TextField<String>();
		lat.setFieldLabel("Latitude");
		lat.setName("lat");
		lat.setEnabled(false);
		panel.add(lat);
		TextField<String> lng = new TextField<String>();
		lng.setFieldLabel("Longitude");
		lng.setName("lng");
		lng.setEnabled(false);
		panel.add(lng);
		return panel;
	}
}
