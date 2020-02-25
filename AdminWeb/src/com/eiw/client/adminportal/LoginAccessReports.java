package com.eiw.client.adminportal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LoginAccessReports extends LayoutContainer {
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
	Label lblfromDate = new Label("FromDate ");
	Label lbltoDate = new Label("ToDate");
	HorizontalPanel hp = new HorizontalPanel();
	ToolBar toolBar = new ToolBar();
	ListBox lbCompanyName = new ListBox();
	Label lblCompanyName = new Label("CompanyName");
	ListBox lbBranchName = new ListBox();
	Label lblBranchName = new Label("BranchName");
	ListBox lbUserId = new ListBox();
	Label lblUserId = new Label("UserId");
	List<CompanyDataAdmin> compList1 = null;
	DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd");
	String fromDate, toDate;
	PagingToolBar pagingBar;
	Date date1 = new Date();
	DateWrapper date = new DateWrapper(new Date());
	Image box1 = new Image("img/loading12.gif");

	public LoginAccessReports(String compName, String brnchName,
			String userName, String role, VerticalPanel bodyPanel1) {
		store = new GroupingStore<ModelCompanyData>();
		// store.add(TestData.getSpeedSummary(vehicleDatas));
		// store.groupBy("plateNo");
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		ColumnConfig column = new ColumnConfig();
		column.setId("companyName");
		column.setHeader("ClientName");
		column.setSortable(false);
		column.setWidth(100);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("userName");
		column.setHeader("UserId");
		column.setWidth(80);
		column.setGroupable(false);
		column.setSortable(false);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("roleName");
		column.setHeader("Role");
		column.setWidth(80);
		column.setGroupable(false);
		column.setSortable(false);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("loginTime");
		column.setHeader("LoginTime");
		column.setWidth(100);
		column.setGroupable(false);
		column.setSortable(false);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("macIp");
		column.setHeader("MacIP");
		column.setWidth(100);
		column.setGroupable(false);
		column.setSortable(false);
		// configs.add(column);

		column = new ColumnConfig();
		column.setId("logoutTime");
		column.setHeader("LogoutTime");
		column.setWidth(120);
		column.setGroupable(false);
		column.setSortable(false);
		// configs.add(column);

		column = new ColumnConfig();
		column.setId("timeConnected");
		column.setHeader("Length Of Time Connected");
		column.setWidth(80);
		column.setGroupable(false);
		column.setSortable(false);
		// configs.add(column);

		view = new GroupingView();
		view.setShowGroupedColumn(false);
		view.setForceFit(true);
		view.setGroupRenderer(new GridGroupRenderer() {
			public String render(GroupColumnData data) {
				return data.group;
			}
		});
		// store.groupBy("companyName");
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
		toDatefield.setMaxValue(new Date());
		toDatefield.setValue(date1);
		btnOk.setIcon(ImagesBundle.Util.get("ICON-16Go"));
		btnOk.setBorders(true);
		hp.setSpacing(3);
		hp.add(lblCompanyName);
		hp.add(lbCompanyName);
		hp.add(lblBranchName);
		hp.add(lbBranchName);
		hp.add(lblUserId);
		hp.add(lbUserId);
		hp.add(lblfromDate);
		hp.add(fromDatefield);
		hp.add(lbltoDate);
		hp.add(toDatefield);
		hp.add(btnOk);
		toolBar.add(hp);
		toolBar.setSpacing(2);
		cp.setHeading("Login Access Report");
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

		fleetMgmtService.getCompanyDetails(LoginDashboardModule.suffix,
				new AsyncCallback<List<CompanyDataAdmin>>() {

					public void onSuccess(List<CompanyDataAdmin> compList) {
						compList1 = compList;
						Set<String> setCompanyName = new TreeSet<String>();
						lbCompanyName.clear();
						lbCompanyName.addItem("All");
						lbBranchName.addItem("All");
						lbUserId.addItem("All");
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
				lbBranchName.clear();
				lbUserId.clear();
				lbBranchName.addItem("All");
				lbUserId.addItem("All");
				String selectedValue = lbCompanyName.getItemText(lbCompanyName
						.getSelectedIndex());
				for (int i = 0; i < compList1.size(); i++) {
					String compName = compList1.get(i).getCompanyName();
					if (compName.equalsIgnoreCase(selectedValue))
						setBranchName.add(compList1.get(i).getBranchName());
				}
				for (String setBranch : setBranchName) {
					lbBranchName.addItem(setBranch);
				}
			}
		});

		lbBranchName.addChangeListener(new ChangeListener() {

			public void onChange(Widget sender) {
				Set<String> setUserId = new TreeSet<String>();
				lbUserId.clear();
				lbUserId.addItem("All");
				String selectedCompValue = lbCompanyName
						.getItemText(lbCompanyName.getSelectedIndex());
				String selectedBrchValue = lbBranchName
						.getItemText(lbBranchName.getSelectedIndex());
				for (int i = 0; i < compList1.size(); i++) {
					String compName = compList1.get(i).getCompanyName();
					String brchName = compList1.get(i).getBranchName();
					if (compName.equalsIgnoreCase(selectedCompValue)
							&& brchName.equalsIgnoreCase(selectedBrchValue))
						setUserId.add(compList1.get(i).getUserName());
				}
				for (String setUserName : setUserId) {
					lbUserId.addItem(setUserName);
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

						// box.close();
					}

				};
				t.schedule(20000);
				String selectedCompValue = lbCompanyName
						.getItemText(lbCompanyName.getSelectedIndex());
				String selectedBrchValue = lbBranchName
						.getItemText(lbBranchName.getSelectedIndex());
				String selectedUserValue = lbUserId.getItemText(lbUserId
						.getSelectedIndex());
				CompanyDataAdmin companyDataAdmin = new CompanyDataAdmin();
				if (fromDatefield.getValue() == null) {
					fromDate = null;
				} else {
					fromDate = dtf.format(fromDatefield.getValue());
				}
				if (toDatefield.getValue() == null) {
					toDate = null;
				} else {
					// pdfBtn.setEnabled(true);
					// printBtn.setEnabled(true);
					toDate = dtf.format(toDatefield.getValue());
				}
				System.out.println("fromDate" + fromDate);
				System.out.println("toDate" + toDate);
				companyDataAdmin.setFromDate(fromDate);
				companyDataAdmin.setToDate(toDate);
				companyDataAdmin.setCompanyName(selectedCompValue);
				companyDataAdmin.setBranchName(selectedBrchValue);
				companyDataAdmin.setUserName(selectedUserValue);
				fleetMgmtService.getLoginInfo(LoginDashboardModule.suffix,
						companyDataAdmin,
						new AsyncCallback<List<CompanyDataAdmin>>() {

							public void onSuccess(
									List<CompanyDataAdmin> compDatas) {
								if (box1.isVisible() == true) {
									box1.setVisible(false);
								}
								if (compDatas.size() == 0) {
									store.removeAll();
									MessageBox.alert("Login Access Report",
											"No Data Available!", null);
									// pdfBtn.setEnabled(false);
									// printBtn.setEnabled(false);
								} else {
									PagingModelMemoryProxy pagingProxy = new PagingModelMemoryProxy(
											DataReaderAdmin
													.getLoginDetails(compDatas));
									pagingLoader = new BasePagingLoader<PagingLoadResult<ModelData>>(
											pagingProxy);
									System.out.println("sizeeeeeeeeee"
											+ compDatas.size());
									pagingLoader.setRemoteSort(true);
									store = new GroupingStore<ModelCompanyData>(
											pagingLoader);
									pagingBar.bind(pagingLoader);
									pagingLoader.load(0, 13);
									store.groupBy("companyName");
									grid.reconfigure(store, cm);
								}
							}

							public void onFailure(Throwable caught) {

							}
						});
			}
		});

	}
}
