package com.eiw.client.adminportal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.eiw.client.AdminPortalService;
import com.eiw.client.AdminPortalServiceAsync;
import com.eiw.client.dashboard.LoginDashboardModule;
import com.eiw.client.dto.CompanyDataAdmin;
import com.eiw.client.fleetmgmt.DataReaderAdmin;
import com.eiw.client.gxtmodel.ModelCompanyData;
import com.eiw.client.icons.ImagesBundle;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.PagingModelMemoryProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
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
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ErrorLogReport extends LayoutContainer {
	private AdminPortalServiceAsync fleetMgmtService = GWT
			.create(AdminPortalService.class);
	String userId = LoginDashboardModule.userName;
	DateField fromDatefield = new DateField();
	DateField toDatefield = new DateField();
	Button btnOk = new Button("Go");
	// Button pdfBtn = new Button("PDF");
	// Button printBtn = new Button("Print");
	PagingLoader<PagingLoadResult<ModelData>> pagingLoader;
	Grid<ModelCompanyData> grid;
	GroupingStore<ModelCompanyData> store;
	ColumnModel cm;
	GroupingView view;
	ContentPanel cp = new ContentPanel();
	Label lblfromDate = new Label("From");
	Label lbltoDate = new Label("To");
	HorizontalPanel hp = new HorizontalPanel();
	ToolBar toolBar = new ToolBar();
	List<CompanyDataAdmin> compList1 = null;
	DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd");
	String fromDate, toDate;
	PagingToolBar pagingBar;
	Date date1 = new Date();
	DateWrapper date = new DateWrapper(new Date());
	Image box1 = new Image("img/loading12.gif");

	public ErrorLogReport(String compName, String brnchName, String userName,
			String role, VerticalPanel bodyPanel1) {
		store = new GroupingStore<ModelCompanyData>();
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		ColumnConfig column = new ColumnConfig();
		column.setId("plateNo");
		column.setHeader("Plate No");
		column.setWidth(150);
		column.setGroupable(false);
		column.setSortable(false);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("serverTime");
		column.setHeader("Time");
		column.setWidth(150);
		column.setGroupable(false);
		column.setSortable(false);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("serverTimeStamp");
		column.setHeader("Server TimeStamp");
		column.setWidth(150);
		column.setGroupable(false);
		column.setSortable(false);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("module");
		column.setHeader("Module");
		column.setGroupable(false);
		column.setSortable(false);
		column.setWidth(150);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("description");
		column.setHeader("Description");
		column.setWidth(150);
		column.setGroupable(false);
		column.setSortable(false);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("eventSource");
		column.setHeader("Event Source");
		column.setWidth(150);
		column.setGroupable(false);
		column.setSortable(false);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("serverDate");
		column.setHeader("Date");
		column.setWidth(175);
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
		cm = new ColumnModel(configs);
		grid = new Grid<ModelCompanyData>(store, cm);
		grid.setSize(1358, 483);
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
		fromDatefield.setWidth(100);
		toDatefield.setValue(date1);
		toDatefield.setMaxValue(new Date());
		toDatefield.setWidth(100);
		btnOk.setIcon(ImagesBundle.Util.get("ICON-16Go"));
		btnOk.setBorders(true);
		hp.setSpacing(3);
		hp.add(lblfromDate);
		hp.add(fromDatefield);
		hp.add(lbltoDate);
		hp.add(toDatefield);
		hp.add(btnOk);
		toolBar.add(hp);
		toolBar.setSpacing(2);
		cp.setHeading("Error Report");
		cp.setHeaderVisible(false);
		cp.setTopComponent(toolBar);
		cp.setSize(1358, 483);
		cp.setBodyBorder(true);
		cp.add(grid);
		pagingBar = new PagingToolBar(13);
		pagingBar.bind(pagingLoader);
		cp.setIcon(ImagesBundle.Util.get("ICON-lastLogin"));
		cp.setBottomComponent(pagingBar);
		bodyPanel1.add(cp);

		btnOk.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				Popup popup = new Popup();
				box1.setVisible(true);
				popup.add(box1);
				popup.show();
				Timer t = new Timer() {
					@Override
					public void run() {
					}
				};
				t.schedule(20000);
				CompanyDataAdmin companyDataAdmin = new CompanyDataAdmin();
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
					MessageBox.alert("Error Log Report",
							"Please Select 'from' Date", null);
					store.removeAll();
				} else if (toDate == null) {
					MessageBox.alert("Error Log Report",
							"Please Select 'to' Date", null);
					store.removeAll();
				} else if (fromDatefield.getValue().after(
						toDatefield.getValue())) {
					MessageBox.alert("Error Log Report",
							"To Date Should be greater than From Date!", null);
					store.removeAll();
				} else {
					// pdfBtn.setEnabled(true);
					// printBtn.setEnabled(true);
					System.out.println("fromDate" + fromDate);
					System.out.println("toDate" + toDate);
					companyDataAdmin.setFromDate(fromDate);
					companyDataAdmin.setToDate(toDate);
					fleetMgmtService.getErrorLogInfo(companyDataAdmin,
							new AsyncCallback<List<CompanyDataAdmin>>() {

								public void onSuccess(
										List<CompanyDataAdmin> compDatas) {
									if (box1.isVisible() == true) {
										box1.setVisible(false);
									}
									if (compDatas.size() == 0) {
										store.removeAll();
										MessageBox.alert("Error Log Report",
												"No Data Available!", null);
										// pdfBtn.setEnabled(false);
										// printBtn.setEnabled(false);
									} else {
										PagingModelMemoryProxy pagingProxy = new PagingModelMemoryProxy(
												DataReaderAdmin
														.getErrorLogDetails(compDatas));
										pagingLoader = new BasePagingLoader<PagingLoadResult<ModelData>>(
												pagingProxy);
										System.out.println("sizeeeeeeeeee"
												+ compDatas.size());
										pagingLoader.setRemoteSort(true);
										store = new GroupingStore<ModelCompanyData>(
												pagingLoader);
										pagingBar.bind(pagingLoader);
										pagingLoader.load(0, 13);
										store.groupBy("serverDate");
										grid.reconfigure(store, cm);
									}
								}

								public void onFailure(Throwable caught) {

								}
							});
				}
			}
		});

	}
}
