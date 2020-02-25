package com.eiw.client.adminportal;

import java.util.ArrayList;
import java.util.List;

import com.eiw.client.AdminPortalService;
import com.eiw.client.AdminPortalServiceAsync;
import com.eiw.client.LoginConstants;
import com.eiw.client.dashboard.LoginDashboardModule;
import com.eiw.client.dto.CompanyDataAdmin;
import com.eiw.client.fleetmgmt.DataReaderAdmin;
import com.eiw.client.gxtmodel.Maintenance;
import com.eiw.client.gxtmodel.ModelCompanyData;
import com.eiw.client.icons.ImagesBundle;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridGroupRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupColumnData;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CompanyFeatures extends VerticalPanel {

	private LoginConstants constants = GWT.create(LoginConstants.class);

	private AdminPortalServiceAsync fleetMgmtService = GWT
			.create(AdminPortalService.class);
	ContentPanel cpMaint = new ContentPanel();
	static Grid<Maintenance> gridMaint;
	static PagingLoader<PagingLoadResult<ModelData>> pagingLoaderMaint;
	static ColumnModel cmMaint;
	static PagingToolBar pagingBarMaint;
	static GroupingStore<Maintenance> storeMaint;
	ColumnConfig chkBoxColumnMaint;
	GroupingView view;
	ListBox listBoxCompany = new ListBox();
	List<Maintenance> listModel = new ArrayList<Maintenance>();
	List<Maintenance> listModelRemove = new ArrayList<Maintenance>();
	Button btnEnable = new Button("Save");
	HorizontalPanel horizontalPanel = new HorizontalPanel();
	List<String> listOfFeatures = new ArrayList<String>();
	Label lblCompName = new Label("Company");
	ComboBox<ModelCompanyData> comboCountry = new ComboBox<ModelCompanyData>();
	Label lblSelectAll = new Label(constants.Select_All());
	CheckBox chkSelectAll = new CheckBox();

	public CompanyFeatures(VerticalPanel bodyPanel1, final String companyId1,
			final String branchId1) {

		btnEnable.setIcon(ImagesBundle.Util.get("ICON-legend_enable"));
		// adding component to cp header
		horizontalPanel.add(lblCompName);
		horizontalPanel.add(listBoxCompany);
		horizontalPanel.add(new Html("&nbsp;"));
		horizontalPanel.add(chkSelectAll);
		horizontalPanel.add(new Html("&nbsp;"));
		horizontalPanel.add(lblSelectAll);
		horizontalPanel.add(new Html("&nbsp;&nbsp;"));
		horizontalPanel.add(btnEnable);
		horizontalPanel.setStyleName("featureHeader");
		cpMaint.getHeader().addTool(horizontalPanel);

		// listBoxCompany
		// final String defaultCompany = "ALMONEE";
		// listBoxCompany.addItem(defaultCompany, defaultCompany);
		fleetMgmtService.getCompanyNames(LoginDashboardModule.suffix,
				new AsyncCallback<List<CompanyDataAdmin>>() {

					@Override
					public void onSuccess(List<CompanyDataAdmin> companyList) {
						for (int i = 0; i < companyList.size(); i++) {
							// if (!(companyList.get(i).getCompanyName()
							// .equalsIgnoreCase(defaultCompany)))
							listBoxCompany.addItem(companyList.get(i)
									.getCompanyName(), companyList.get(i)
									.getCompanyId());

						}
						callService();
					}

					@Override
					public void onFailure(Throwable arg0) {
						// TODO Auto-generated method stub

					}
				});

		// Grid
		storeMaint = new GroupingStore<Maintenance>();
		List<ColumnConfig> colConfigMaint = new ArrayList<ColumnConfig>();

		chkBoxColumnMaint = new ColumnConfig("", "Choose-Options", 15);
		colConfigMaint.add(chkBoxColumnMaint);

		chkBoxColumnMaint.setRenderer(new GridCellRenderer<Maintenance>() {

			@Override
			public Object render(final Maintenance model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<Maintenance> store, Grid<Maintenance> grid) {
				final CheckBox checkBox = new CheckBox();
				if (model.getEnable().equalsIgnoreCase("true")) {
					checkBox.setValue(true);
				}
				checkBox.addListener(Events.OnClick,
						new Listener<FieldEvent>() {

							@Override
							public void handleEvent(FieldEvent be) {
								if (checkBox.getValue() == true) {
									listModel.add(model);
								}
								if (checkBox.getValue() == false) {
									listModelRemove.add(model);
									listModel.remove(model);
								}
							}
						});
				return checkBox;
			}
		});

		ColumnConfig columnConfig = new ColumnConfig("feature", "Features", 100);
		colConfigMaint.add(columnConfig);
		columnConfig = new ColumnConfig("featureName", "Features Name", 130);
		columnConfig.setRenderer(new GridCellRenderer<ModelData>() {
			@Override
			public Object render(ModelData model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<ModelData> store, Grid<ModelData> grid) {
				Maintenance maintenance = (Maintenance) model;
				String[] name = String.valueOf(maintenance.get("featureName"))
						.split("_");
				if (name.length > 1) {
					return new HTML("( " + name[0] + " ) " + name[1]);
				} else {
					return new HTML(name[0]);
				}

			}
		});
		colConfigMaint.add(columnConfig);

		view = new GroupingView();
		view.setShowGroupedColumn(false);
		view.setForceFit(true);
		view.setGroupRenderer(new GridGroupRenderer() {
			public String render(GroupColumnData data) {
				return data.group;
			}
		});

		cmMaint = new ColumnModel(colConfigMaint);
		gridMaint = new Grid<Maintenance>(storeMaint, cmMaint);
		storeMaint.groupBy("feature");
		gridMaint.setView(view);
		gridMaint.setBorders(true);
		gridMaint.setSize(1358, 449);
		cpMaint.setSize(1358, 483);
		cpMaint.setScrollMode(Scroll.NONE);
		cpMaint.add(gridMaint);
		pagingBarMaint = new PagingToolBar(7);
		pagingBarMaint.bind(pagingLoaderMaint);
		bodyPanel1.add(cpMaint);
		// service call
		// callService();

		listBoxCompany.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				callService();
			}
		});

		btnEnable.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				// if (listModel.size() == 0) {
				// MessageBox.alert("Company Features", "No Item Selected!",
				// null);
				//
				// } else {
				List<CompanyDataAdmin> companyDataAdmins = new ArrayList<CompanyDataAdmin>();
				List<CompanyDataAdmin> companyDataAdmins1 = new ArrayList<CompanyDataAdmin>();
				for (int i = 0; i < listModel.size(); i++) {
					CompanyDataAdmin dataAdmin = new CompanyDataAdmin();
					dataAdmin.setCompanyFeature(listModel.get(i).getFeature());
					dataAdmin.setFeatureName(listModel.get(i).getFeatureName());
					dataAdmin.setContact(listModel.get(i).getMenuId());
					dataAdmin.setFax(listModel.get(i).getMenuRefId());
					companyDataAdmins.add(dataAdmin);
				}
				for (int i = 0; i < listModelRemove.size(); i++) {
					CompanyDataAdmin dataAdmin1 = new CompanyDataAdmin();
					dataAdmin1.setCompanyFeature(listModelRemove.get(i)
							.getFeature());
					dataAdmin1.setFeatureName(listModelRemove.get(i)
							.getFeatureName());
					dataAdmin1.setContact(listModelRemove.get(i).getMenuId());
					dataAdmin1.setFax(listModelRemove.get(i).getMenuRefId());
					companyDataAdmins1.add(dataAdmin1);
				}

				fleetMgmtService.addCompanyFearures(companyDataAdmins,
						companyDataAdmins1, listBoxCompany
								.getValue(listBoxCompany.getSelectedIndex()),
						new AsyncCallback<String>() {

							@Override
							public void onSuccess(String arg0) {
								listModel.clear();
								listModelRemove.clear();
								callService();
								chkSelectAll.setValue(false);
								MessageBox.info("Features Configuration",
										"Features Updated Successfuly", null);

							}

							@Override
							public void onFailure(Throwable arg0) {
								// TODO Auto-generated method stub

							}
						});

			}
		});

		chkSelectAll.addListener(Events.OnClick, new Listener<FieldEvent>() {

			@Override
			public void handleEvent(FieldEvent be) {
				if (chkSelectAll.getValue()) {
					selectAllRecord();
				} else {
					notselectAllRecord();
				}
			}
		});

		lblSelectAll.addListener(Events.OnClick, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				if (chkSelectAll.getValue()) {
					chkSelectAll.setValue(false);
					notselectAllRecord();
				} else {
					chkSelectAll.setValue(true);
					selectAllRecord();
				}
			}
		});

	}

	public void callService() {

		boolean blnExists = LoginDashboardModule.isMiniHashMap
				.containsKey(listBoxCompany.getValue(listBoxCompany
						.getSelectedIndex()));

		boolean blnExists1 = LoginDashboardModule.isWftSspHashMap
				.containsKey(listBoxCompany.getValue(listBoxCompany
						.getSelectedIndex()));

		CompanyDataAdmin dataAdmin = new CompanyDataAdmin();
		dataAdmin.setCompanyName(listBoxCompany.getValue(listBoxCompany
				.getSelectedIndex()));
		if (blnExists) {
			storeMaint.removeAll();
			MessageBox
					.info("Company Features Details",
							listBoxCompany.getValue(listBoxCompany
									.getSelectedIndex())
									+ " Subscribed mini apps. If you want to upgrade contact system administrator",
							null);
			dataAdmin.setIsMiniApps(true);
		} else {
			dataAdmin.setIsMiniApps(false);
		}

		if (blnExists1) {
			storeMaint.removeAll();
			dataAdmin.setIsSspWft(true);
		} else {
			dataAdmin.setIsSspWft(false);
		}

		fleetMgmtService.getCompanyFeatures(dataAdmin,
				new AsyncCallback<List<CompanyDataAdmin>>() {

					@Override
					public void onFailure(Throwable arg0) {

					}

					@Override
					public void onSuccess(
							List<CompanyDataAdmin> companyDataAdmins) {
						storeMaint = new GroupingStore<Maintenance>();
						storeMaint.add(DataReaderAdmin
								.getCompFeatures(companyDataAdmins));
						storeMaint.groupBy("feature");
						gridMaint.reconfigure(storeMaint, cmMaint);
						listModel.clear();
						for (int i = 0; i < gridMaint.getStore().getCount(); i++) {
							Maintenance modeldata = gridMaint.getStore().getAt(
									i);
							if (modeldata.getEnable().equalsIgnoreCase("true")) {
								modeldata.set("enabled", "true");
								listModel.add(modeldata);
							}

						}

					}

				});

	}

	public void selectAllRecord() {
		listModel.clear();
		for (int i = 0; i < gridMaint.getStore().getCount(); i++) {
			Maintenance modeldata = gridMaint.getStore().getAt(i);
			modeldata.set("enabled", "true");
			listModel.add(modeldata);
		}
		storeMaint.removeAll();
		storeMaint.add(listModel);
	}

	public void notselectAllRecord() {
		listModel.clear();
		for (int i = 0; i < gridMaint.getStore().getCount(); i++) {
			Maintenance modeldata = gridMaint.getStore().getAt(i);
			modeldata.set("enabled", "false");
			listModel.add(modeldata);
			listModelRemove.add(modeldata);
		}
		storeMaint.removeAll();
		storeMaint.add(listModel);
	}
}
