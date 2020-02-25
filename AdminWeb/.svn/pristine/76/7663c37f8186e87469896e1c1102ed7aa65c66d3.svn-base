package com.eiw.client.adminportal;

import java.util.ArrayList;
import java.util.List;

import com.eiw.client.AdminPortalService;
import com.eiw.client.AdminPortalServiceAsync;
import com.eiw.client.dashboard.LoginDashboardModule;
import com.eiw.client.dto.VehicleIMEIDto;
import com.eiw.client.gxtmodel.ModelCompanyData;
import com.eiw.client.icons.ImagesBundle;
import com.extjs.gxt.ui.client.Style.ButtonScale;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.PagingModelMemoryProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;

public class VehiclesDevicesReports extends LayoutContainer {
	private AdminPortalServiceAsync fleetMgmtService = GWT
			.create(AdminPortalService.class);
	PagingLoader<PagingLoadResult<ModelData>> pagingLoader;
	Grid<ModelCompanyData> grid;
	GroupingStore<ModelCompanyData> store;
	ColumnModel cm;
	GroupingView view;
	ContentPanel cp = new ContentPanel();
	DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd");
	PagingToolBar pagingBar;
	Button pdfBtn = new Button("PDF");
	Button csvBtn = new Button("CSV");

	public VehiclesDevicesReports(String compName, String brnchName,
			String userName, String role, VerticalPanel bodyPanel1) {
		LoginDashboardModule.box1.setVisible(true);
		fetchCompanyDeviceIMEI();
		pdfBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				String gwt = GWT.getHostPageBaseURL();
				Window.open(
						gwt
								+ "generatePDFServlet?name=CompanyBasedVehiclesDeviceIMEIReport&fromDate="
								+ "" + "&toDate=" + "" + "&compName="
								+ LoginDashboardModule.companyId
								+ "&brnchName=" + LoginDashboardModule.branchId
								+ "&userName=" + LoginDashboardModule.userName
								+ "&selectedVehicle=" + "" + "&companyLogo="
								+ LoginDashboardModule.imageUrl + "&ourlogo="
								+ "" + "&suffix=" + LoginDashboardModule.suffix
								+ "&bucketName="
								+ LoginDashboardModule.bucketName, null, null);
			}
		});

		csvBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				String gwt = GWT.getHostPageBaseURL();
				Window.open(
						gwt
								+ "generateCSVServlet?name=CompanyBasedVehiclesDeviceIMEIReport&fromDate="
								+ "" + "&toDate=" + "" + "&compName="
								+ LoginDashboardModule.companyId
								+ "&brnchName=" + LoginDashboardModule.branchId
								+ "&userName=" + LoginDashboardModule.userName
								+ "&selectedVehicle=" + "" + "&suffix="
								+ LoginDashboardModule.suffix, null, null);
			}
		});
		store = new GroupingStore<ModelCompanyData>();
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		ColumnConfig column = new ColumnConfig();

		column.setId("companyId");
		column.setHeader("Company Id");
		column.setWidth(150);
		column.setGroupable(false);
		column.setSortable(false);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("plateNo");
		column.setHeader("Plate No");
		column.setWidth(60);
		column.setGroupable(false);
		column.setSortable(false);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("imei");
		column.setHeader("Vehicle Device IMEI");
		column.setWidth(60);
		column.setGroupable(false);
		column.setSortable(false);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("user");
		column.setHeader("Fleet User");
		column.setWidth(180);
		column.setGroupable(false);
		column.setSortable(false);
		configs.add(column);

		store.groupBy("companyId");
		view = new GroupingView();
		view.setShowGroupedColumn(false);
		view.setForceFit(true);
		ToolBar toolBar = new ToolBar();
		pdfBtn.setIcon(ImagesBundle.Util.get("ICON-pdf"));
		pdfBtn.setScale(ButtonScale.MEDIUM);
		pdfBtn.setEnabled(false);
		pdfBtn.setBorders(true);
		csvBtn.setIcon(ImagesBundle.Util.get("ICON-csv"));
		csvBtn.setScale(ButtonScale.MEDIUM);
		csvBtn.setEnabled(false);
		csvBtn.setBorders(true);
		cm = new ColumnModel(configs);
		grid = new Grid<ModelCompanyData>(store, cm);
		grid.setSize(1358, 483);
		grid.setView(view);
		grid.setBorders(true);
		cp.setHeaderVisible(false);
		cp.setSize(1358, 483);
		cp.setBodyBorder(true);
		cp.add(grid);
		pagingBar = new PagingToolBar(12);
		pagingBar.bind(pagingLoader);
		cp.setIcon(ImagesBundle.Util.get("ICON-lastLogin"));
		toolBar.add(pdfBtn);
		toolBar.add(csvBtn);
		cp.setTopComponent(toolBar);
		cp.setBottomComponent(pagingBar);
		bodyPanel1.add(cp);

	}

	private List<ModelCompanyData> itrateVehiclesIMEI(
			List<VehicleIMEIDto> vehImei) {

		List<ModelCompanyData> maintenances = new ArrayList<ModelCompanyData>();
		for (int i = 0; i < vehImei.size(); i++) {
			maintenances.add(new ModelCompanyData(
					vehImei.get(i).getCompanyId(), vehImei.get(i).getPlateNo(),
					vehImei.get(i).getImeiNo(), vehImei.get(i).getFleetUser()));

		}
		return maintenances;

	}

	private void fetchCompanyDeviceIMEI() {
		fleetMgmtService.getVehilceIMEI(LoginDashboardModule.suffix,
				new AsyncCallback<List<VehicleIMEIDto>>() {

					@Override
					public void onSuccess(List<VehicleIMEIDto> vehImei) {
						if (vehImei.size() != 0) {
							LoginDashboardModule.box1.setVisible(false);
							pdfBtn.setEnabled(true);
							csvBtn.setEnabled(true);
							store.removeAll();
							PagingModelMemoryProxy pagingProxy = new PagingModelMemoryProxy(
									itrateVehiclesIMEI(vehImei));
							pagingLoader = new BasePagingLoader<PagingLoadResult<ModelData>>(
									pagingProxy);
							pagingLoader.setRemoteSort(true);
							store = new GroupingStore<ModelCompanyData>(
									pagingLoader);
							pagingBar.bind(pagingLoader);
							pagingLoader.load(0, 12);
							store.groupBy("companyId");
							grid.reconfigure(store, cm);
						}

					}

					@Override
					public void onFailure(Throwable arg0) {
						// TODO Auto-generated method stub

					}
				});
	}
}
