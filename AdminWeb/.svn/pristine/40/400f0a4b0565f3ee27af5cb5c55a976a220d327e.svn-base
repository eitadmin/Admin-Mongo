package com.eiw.client.dashboard;

import java.util.ArrayList;
import java.util.List;

import com.eiw.client.AdminPortalService;
import com.eiw.client.AdminPortalServiceAsync;
import com.eiw.client.dto.CompanyDataAdmin;
import com.eiw.client.fleetmgmt.DataReaderAdmin;
import com.eiw.client.gxtmodel.ModelCompanyData;
import com.eiw.client.icons.ImagesBundle;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.PagingModelMemoryProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PagingCompanyDetailsGrid extends VerticalPanel

{
	String companyId, branchId, user;
	PopupPanel disassociatePopup = new PopupPanel();
	static Grid<ModelCompanyData> grid;
	ModelCompanyData modelCompanyData;
	static ColumnModel cm;
	PopupPanel associatePopup = new PopupPanel();
	static PagingLoader<PagingLoadResult<ModelData>> pagingLoader;
	ContentPanel panel = new ContentPanel();
	static private AdminPortalServiceAsync fleetMgmtService = GWT
			.create(AdminPortalService.class);
	Button newButton = new Button("Add Company");
	static ListStore<ModelCompanyData> store = new ListStore<ModelCompanyData>();

	HorizontalPanel hPanel = new HorizontalPanel();
	HorizontalPanel hPanel2 = new HorizontalPanel();
	ContentPanel panel2 = new ContentPanel();
	FlexTable flexTable = new FlexTable();
	static PagingToolBar pagingBar;
	ToolBar toolBar = new ToolBar();

	public PagingCompanyDetailsGrid(String companyId1, String branchId1,
			String user1) {
		this.companyId = companyId1;
		this.branchId = branchId1;
		this.user = user1;

		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		ColumnConfig column = new ColumnConfig();
		column = new ColumnConfig();
		column.setId("companyId");
		column.setHeader("Company Id");
		column.setWidth(130);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("companyName");
		column.setHeader("Company Name");
		column.setWidth(180);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("addressLine1");
		column.setHeader("Address Line1");
		column.setWidth(180);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("addressLine2");
		column.setHeader("Address Line2");
		column.setWidth(180);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("addressCity");
		column.setHeader("City");
		column.setWidth(130);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("countryName");
		column.setHeader("Country");
		column.setWidth(100);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("userName");
		column.setHeader("Login Id");
		column.setWidth(100);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("saleperson");
		column.setHeader("Sales Person");
		column.setWidth(150);
		configs.add(column);

		column = new ColumnConfig();
		column.setHeader("Actions");
		column.setRenderer(new GridCellRenderer<ModelCompanyData>() {

			public Object render(final ModelCompanyData model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<ModelCompanyData> store,
					Grid<ModelCompanyData> grid) {
				ButtonBar buttonBar = new ButtonBar();
				Button editButton = new Button();
				editButton.setIcon(ImagesBundle.Util.get("ICON-I_16edit"));
				buttonBar.add(editButton);
				editButton.setTitle("Edit");
				editButton
						.addSelectionListener(new SelectionListener<ButtonEvent>() {
							public void componentSelected(ButtonEvent ce) {
								Dialog popup2 = new Dialog();
								popup2.setHeading("Edit Company Details");
								popup2.add(new AddCompanyForm(popup2,
										companyId, branchId, user, "edit",
										model));
								popup2.setModal(true);
								popup2.setAutoHeight(true);
								popup2.setAutoWidth(true);
								popup2.setButtons("");
								popup2.show();

							}
						});

				Button deleteButton = new Button();
				deleteButton.setIcon(ImagesBundle.Util.get("ICON-I_16delete"));
				deleteButton.setTitle("Delete");
				deleteButton
						.addSelectionListener(new SelectionListener<ButtonEvent>() {
							public void componentSelected(ButtonEvent ce) {

								MessageBox.confirm(
										"Alert",
										"Sure you want Delete "
												+ model.getCompanyName()
												+ " Details",
										new Listener<MessageBoxEvent>() {

											@Override
											public void handleEvent(
													MessageBoxEvent be) {
												Button btnClik = be
														.getButtonClicked();
												if (btnClik
														.getText()
														.equalsIgnoreCase("Yes")) {
													deleteCompany(model
															.getCompanyId());

												}
											}

										});
							}
						});
				buttonBar.add(deleteButton);
				return buttonBar;
			}
		});

		column.setWidth(200);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("remarks");
		column.setHidden(true);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("isFollowUp");
		column.setHidden(true);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("isDemo");
		column.setHidden(true);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("region");
		column.setHidden(true);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("noTransRptSkip");
		column.setHidden(true);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("password");
		column.setHidden(true);
		configs.add(column);

		cm = new ColumnModel(configs);

		grid = new Grid<ModelCompanyData>(store, cm);
		panel.setHeaderVisible(false);
		grid.setSize(1358, 483);
		panel.setSize(1358, 483);
		panel.add(grid);
		pagingBar = new PagingToolBar(10);
		pagingBar.bind(pagingLoader);
		panel.setBottomComponent(pagingBar);
		newButton.setIcon(ImagesBundle.Util.get("ICON-I_16add"));
		newButton.setBorders(true);
		toolBar.add(newButton);
		panel.setTopComponent(toolBar);
		add(panel);
		gridRefresh();

		newButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				Dialog popup = new Dialog();
				popup.add(new AddCompanyForm(popup, companyId, branchId, user,
						"new", null));
				popup.setHeading("Add New Company");
				popup.setModal(true);
				popup.setAutoHeight(true);
				popup.setAutoWidth(true);
				popup.setButtons("");
				popup.show();
			}
		});
	}

	public static void gridRefresh() {
		fleetMgmtService.getCompanyRegistration(LoginDashboardModule.suffix,
				new AsyncCallback<List<CompanyDataAdmin>>() {

					@Override
					public void onSuccess(
							List<CompanyDataAdmin> companyDataAdmins) {
						PagingModelMemoryProxy pagingProxy = new PagingModelMemoryProxy(
								DataReaderAdmin
										.getCompanyDetails(companyDataAdmins));
						pagingLoader = new BasePagingLoader<PagingLoadResult<ModelData>>(
								pagingProxy);
						pagingLoader.setRemoteSort(true);
						store = new ListStore<ModelCompanyData>(pagingLoader);

						pagingBar.bind(pagingLoader);
						pagingLoader.load(0, 10);
						grid.reconfigure(store, cm);
						grid.setBorders(true);
					}

					@Override
					public void onFailure(Throwable arg0) {
						// TODO Auto-generated method stub

					}
				});

	}

	public void deleteCompany(String companyId) {
		CompanyDataAdmin companyDataAdmin = new CompanyDataAdmin();
		companyDataAdmin.setCompanyId(companyId);

		fleetMgmtService.deleteCompanyRegistration(companyDataAdmin,
				new AsyncCallback<String>() {

					@Override
					public void onSuccess(String status) {
						if (status.trim().equalsIgnoreCase("persisted")) {
							MessageBox.alert("Sucess",
									"Company details deleted successfully",
									null);
							PagingCompanyDetailsGrid.gridRefresh();
						}
					}

					@Override
					public void onFailure(Throwable arg0) {
						// TODO Auto-generated method stub

					}
				});
	}
}