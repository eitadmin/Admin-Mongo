package com.eiw.client.adminportal;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.eiw.client.AdminPortalService;
import com.eiw.client.AdminPortalServiceAsync;
import com.eiw.client.dashboard.LoginDashboardModule;
import com.eiw.client.dto.CompanyDataAdmin;
import com.eiw.client.dto.VehicleData;
import com.eiw.client.fleetmgmt.DataReaderAdmin;
import com.eiw.client.gxtmodel.ModelVehicleData;
import com.eiw.client.icons.ImagesBundle;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.util.DateWrapper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Popup;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridGroupRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupColumnData;
import com.extjs.gxt.ui.client.widget.grid.GroupSummaryView;
import com.extjs.gxt.ui.client.widget.grid.SummaryColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.SummaryRenderer;
import com.extjs.gxt.ui.client.widget.grid.SummaryType;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class BandwidthUtilizationReport extends LayoutContainer {
	VerticalPanel bodyPanel = new VerticalPanel();
	AdminPortalServiceAsync fleetMgmtService = GWT
			.create(AdminPortalService.class);
	String userId = LoginDashboardModule.userName;
	DateField fromDatefield = new DateField();
	DateField toDatefield = new DateField();
	Button btnOk = new Button("Go");
	PagingLoader<PagingLoadResult<ModelData>> pagingLoader;
	PagingToolBar pagingBar;
	Grid<ModelVehicleData> grid;
	GroupingStore<ModelVehicleData> store;
	ColumnModel cm;
	GroupSummaryView view;
	String selectedVehicle, fromDate, toDate;
	ContentPanel cp = new ContentPanel();
	Label lblfromDate = new Label("From ");
	Label lbltoDate = new Label("To");
	HorizontalPanel hp = new HorizontalPanel();
	ToolBar toolBar = new ToolBar();
	ListBox lbVehicles = new ListBox();
	Label lblVehicles = new Label("Vehicles");
	// Button pdfBtn = new Button("PDF");
	// Button printBtn = new Button("Print");
	Image box1 = new Image("img/loading12.gif");
	DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd");
	Date date1 = new Date();
	DateWrapper date = new DateWrapper(new Date());
	SummaryColumnConfig columnTrx, columnBytes;
	int total = 0, storeCnt = 0, count = 0, preCnt = 0;
	ListBox lbCompanyName = new ListBox();
	Label lblCompanyName = new Label("CompanyName");
	ListBox lbBranchName = new ListBox();
	Label lblBranchName = new Label("BranchName");
	ListBox lbPlateNo = new ListBox();
	Label lblPlateNo = new Label("Vehicle Number");
	List<CompanyDataAdmin> compList1 = null;
	HashMap<String, String> hmapVehicle = new HashMap<String, String>();

	public BandwidthUtilizationReport(VerticalPanel bodyPanel1,
			final String compName, final String brnchName, String userName,
			String userRole) {
		bodyPanel = bodyPanel1;
		store = new GroupingStore<ModelVehicleData>();
		final Listener<MessageBoxEvent> l = new Listener<MessageBoxEvent>() {
			public void handleEvent(MessageBoxEvent ce) {
			}
		};
		lbCompanyName.setWidth("100");
		lbBranchName.setWidth("100");
		lbPlateNo.setWidth("100");
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		SummaryColumnConfig column = new SummaryColumnConfig();
		column.setId("plateNo");
		column.setHeader("Vehicle Number");
		column.setSortable(false);
		column.setWidth(140);
		// configs.add(column);

		column = new SummaryColumnConfig();
		column.setId("clientName");
		column.setHeader("Client Name");
		column.setWidth(140);
		column.setGroupable(false);
		column.setSortable(false);
		configs.add(column);

		column = new SummaryColumnConfig();
		column.setId("imei");
		column.setHeader("Imei No");
		column.setWidth(140);
		column.setGroupable(false);
		column.setSortable(false);
		configs.add(column);

		columnTrx = new SummaryColumnConfig();
		columnTrx.setId("timeStampStr");
		columnTrx.setHeader("Date");
		columnTrx.setWidth(140);
		columnTrx.setSummaryType(SummaryType.COUNT);
		configs.add(columnTrx);

		columnBytes = new SummaryColumnConfig();
		columnBytes.setId("bytesTrx");
		columnBytes.setHeader("Bytes Trasnmitted");
		columnBytes.setSummaryType(SummaryType.SUM);
		columnBytes.setWidth(140);
		configs.add(columnBytes);

		columnTrx.setSummaryRenderer(new SummaryRenderer() {
			public String render(Number value, Map<String, Number> data) {
				return "Total Bytes Transmitted";
			}
		});

		view = new GroupSummaryView();
		view.setShowGroupedColumn(false);
		view.setForceFit(true);
		view.setGroupRenderer(new GridGroupRenderer() {
			public String render(GroupColumnData data) {
				return data.group;
			}
		});
		cm = new ColumnModel(configs);
		grid = new Grid<ModelVehicleData>(store, cm);
		grid.setView(view);
		grid.setBorders(true);
		grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		hp.setSpacing(3);
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
		fromDatefield.setWidth(100);
		toDatefield.setMaxValue(new Date());
		toDatefield.setValue(date1);
		toDatefield.setWidth(100);
		btnOk.setIcon(ImagesBundle.Util.get("ICON-16Go"));
		btnOk.setBorders(true);
		hp.add(lblCompanyName);
		hp.add(lbCompanyName);
		hp.add(lblBranchName);
		hp.add(lbBranchName);
		hp.add(lblPlateNo);
		hp.add(lbPlateNo);
		hp.add(lblfromDate);
		hp.add(fromDatefield);
		hp.add(lbltoDate);
		hp.add(toDatefield);
		hp.add(btnOk);
		toolBar.add(hp);
		toolBar.setSpacing(2);
		cp.setHeading("BandWidth Utilization Report");
		cp.setHeaderVisible(false);
		cp.setTopComponent(toolBar);
		cp.setSize(1358, 483);
		grid.setSize(1358, 445);
		cp.setBodyBorder(true);
		cp.add(grid);
		pagingBar = new PagingToolBar(13);
		pagingBar.bind(pagingLoader);
		bodyPanel1.add(cp);

		fleetMgmtService.getCompanyDetails(LoginDashboardModule.suffix,
				new AsyncCallback<List<CompanyDataAdmin>>() {

					public void onSuccess(List<CompanyDataAdmin> compList) {
						compList1 = compList;
						Set<String> setCompanyName = new TreeSet<String>();
						lbCompanyName.clear();
						lbCompanyName.addItem("All");
						lbBranchName.addItem("All");
						lbPlateNo.addItem("All");
						hmapVehicle.clear();
						hmapVehicle.put("All", "All");
						for (int i = 0; i < compList.size(); i++) {
							setCompanyName
									.add(compList.get(i).getCompanyName());
						}
						for (String setComp : setCompanyName) {
							lbCompanyName.addItem(setComp);
						}

					}

					public void onFailure(Throwable caught) {

					}
				});
		lbCompanyName.addChangeListener(new ChangeListener() {

			public void onChange(Widget sender) {
				Set<String> setBranchName = new TreeSet<String>();
				Set<String> setPlateNo = new TreeSet<String>();
				lbBranchName.clear();
				lbBranchName.addItem("All");
				lbPlateNo.clear();
				lbPlateNo.addItem("All");
				hmapVehicle.clear();
				hmapVehicle.put("All", "All");
				String selectedValue = lbCompanyName.getItemText(lbCompanyName
						.getSelectedIndex());
				for (int i = 0; i < compList1.size(); i++) {
					String compName = compList1.get(i).getCompanyName();
					if (compName.equalsIgnoreCase(selectedValue)) {
						setBranchName.add(compList1.get(i).getBranchName());
						if (compList1.get(i).getPlateNo() != null) {
							setPlateNo.add(compList1.get(i).getPlateNo());
							hmapVehicle.put(compList1.get(i).getPlateNo(),
									compList1.get(i).getVin());
						}
					}
				}
				for (String setBranch : setBranchName) {
					lbBranchName.addItem(setBranch);
				}
				for (String setPlate : setPlateNo) {
					lbPlateNo.addItem(setPlate);
				}
			}
		});

		lbBranchName.addChangeListener(new ChangeListener() {

			@Override
			public void onChange(Widget arg0) {
				Set<String> setPlateNo = new TreeSet<String>();
				lbPlateNo.clear();
				lbPlateNo.addItem("All");
				hmapVehicle.clear();
				hmapVehicle.put("All", "All");
				String selectedValue = lbBranchName.getItemText(lbBranchName
						.getSelectedIndex());
				for (int i = 0; i < compList1.size(); i++) {
					String branchName = compList1.get(i).getBranchName();
					if (branchName.equalsIgnoreCase(selectedValue)) {
						if (compList1.get(i).getPlateNo() != null) {
							setPlateNo.add(compList1.get(i).getPlateNo());
							hmapVehicle.put(compList1.get(i).getPlateNo(),
									compList1.get(i).getVin());
						}
					}
				}
				for (String setPlate : setPlateNo) {
					lbPlateNo.addItem(setPlate);
				}
			}
		});

		btnOk.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				Popup popup = new Popup();
				box1.setVisible(true);
				// popup.setPosition(0, 0);
				popup.add(box1);
				popup.show();
				Timer t = new Timer() {
					@Override
					public void run() {

					}

				};
				t.schedule(20000);
				if (fromDatefield.getValue() == null) {
					fromDate = null;
					MessageBox.alert("BandWidth Utilization Report",
							"Enter 'From' Date", l);
				} else if (toDatefield.getValue() == null) {
					toDate = null;
					MessageBox.alert("BandWidth Utilization Report",
							"Enter 'To' Date", l);
				} else {
					fromDate = dtf.format(fromDatefield.getValue());
					toDate = dtf.format(toDatefield.getValue());
					// pdfBtn.setEnabled(true);
					// printBtn.setEnabled(true);
					VehicleData vehicleData = new VehicleData();
					String selectedCompValue = lbCompanyName
							.getItemText(lbCompanyName.getSelectedIndex());
					String selectedBrchValue = lbBranchName
							.getItemText(lbBranchName.getSelectedIndex());
					String selectedPlateValue = lbPlateNo.getItemText(lbPlateNo
							.getSelectedIndex());
					vehicleData.setCompanyId(selectedCompValue);
					vehicleData.setBranchId(selectedBrchValue);
					vehicleData.setVin(hmapVehicle.get(selectedPlateValue));
					vehicleData.setfromDate(fromDate);
					vehicleData.setToDate(toDate);

					fleetMgmtService.getBWUtilReport(
							LoginDashboardModule.suffix, vehicleData,
							new AsyncCallback<List<VehicleData>>() {

								public void onSuccess(
										List<VehicleData> vehicleDatas) {
									if (box1.isVisible() == true) {
										box1.setVisible(false);
									}

									if (vehicleDatas.size() == 0) {
										store.removeAll();
										MessageBox.alert(
												"BandWidth Utilization Report",
												"No Datas Available", l);
										// pdfBtn.setEnabled(false);
										// printBtn.setEnabled(false);
									} else {
										store = new GroupingStore<ModelVehicleData>();
										store.add(DataReaderAdmin
												.getBWUtilList(vehicleDatas));

										grid.reconfigure(store, cm);
										storeCnt = store.getCount();
										store.groupBy("plateNo");
									}
								}

								public void onFailure(Throwable arg0) {
									// TODO Auto-generated method stub

								}
							});
				}
			}
		});
	}
}