package com.eiw.client.dashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.eiw.client.LoginConstants;
import com.eiw.client.adminportal.AllCompanysDetails;
import com.eiw.client.adminportal.BandwidthUtilizationReport;
import com.eiw.client.adminportal.CompanyFeatures;
import com.eiw.client.adminportal.ErrorLogReport;
import com.eiw.client.adminportal.LoginAccessReports;
import com.eiw.client.adminportal.NoTransmisRpt;
import com.eiw.client.adminportal.ResellerDetails;
import com.eiw.client.adminportal.SMSCount;
import com.eiw.client.adminportal.SendEmail;
import com.eiw.client.adminportal.SmsBalanceRpt;
import com.eiw.client.adminportal.SummaryReportBulkExceution;
import com.eiw.client.adminportal.VehicleEventReport;
import com.eiw.client.adminportal.VehiclesDevicesReports;
import com.eiw.client.adminportal.deviceCommand;
import com.eiw.client.icons.ImagesBundle;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Popup;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.SplitButton;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MenuPanel extends HorizontalPanel {
	SplitButton btnAlerts = null;
	Label phLbl = new Label();
	Image setLogo = new Image();
	private LoginConstants constants = GWT.create(LoginConstants.class);
	public static TabPanel tabPanel = new TabPanel();
	public static TabItem tabItem = new TabItem();
	FlexTable flextable = new FlexTable();
	public static HashMap<String, TabItem> hMap = new HashMap<String, TabItem>();

	Map<String, List<String>> hashMapFeatures = new TreeMap<String, List<String>>();

	@SuppressWarnings("deprecation")
	public MenuPanel() {
		tabPanel.setSize(LoginDashboardModule.width,
				LoginDashboardModule.height);
		tabPanel.setMinTabWidth(200);
		tabPanel.setResizeTabs(true);
		tabPanel.setAnimScroll(true);
		tabPanel.setTabScroll(true);
		tabPanel.setCloseContextMenu(true);

		tabItem = new TabItem("Dashboard");
		tabItem.add(new DashBoard());
		tabPanel.add(tabItem);
		LoginDashboardModule.bodyPanel.add(tabPanel);

		Button btnDashBoard = new Button(constants.Dash_Board());
		btnDashBoard.setWidth(95);
		btnDashBoard.setHeight(30);
		btnDashBoard.setIcon(ImagesBundle.Util.get("ICON-dashBoard"));
		btnDashBoard.setVisible(false);
		// Tracking
		SplitButton btnTrack = new SplitButton(constants.Tracking());
		btnTrack.setIcon(ImagesBundle.Util.get("ICON-tracker"));
		btnTrack.setWidth(95);
		btnTrack.setHeight(30);
		btnTrack.setVisible(false);

		Menu menuTracking = new Menu();

		MenuItem menuLiveTracking = new MenuItem(constants.Live_Tracking());
		menuLiveTracking.setIcon(ImagesBundle.Util.get("ICON-live"));
		menuLiveTracking.setVisible(false);
		menuTracking.add(menuLiveTracking);

		MenuItem menuBackTracking = new MenuItem(constants.Back_Tracking());
		menuBackTracking.setIcon(ImagesBundle.Util.get("ICON-bktrk"));
		menuBackTracking.setVisible(false);
		menuTracking.add(menuBackTracking);

		MenuItem menuMultiTracking = new MenuItem(constants.Multi_Tracking());
		menuMultiTracking.setIcon(ImagesBundle.Util.get("ICON-dashBoard"));
		menuMultiTracking.setVisible(false);
		menuTracking.add(menuMultiTracking);

		btnTrack.setMenu(menuTracking);

		// Fleet Mgmt

		SplitButton btnFleetManagement = new SplitButton(
				constants.Fleet_Management());

		Menu menuFleet = new Menu();

		btnFleetManagement.setIcon(ImagesBundle.Util
				.get("ICON-fleetManagement"));
		btnFleetManagement.setWidth(135);
		btnFleetManagement.setHeight(30);
		btnFleetManagement.setVisible(false);

		MenuItem menuMyFleets = new MenuItem(constants.My_Fleets());
		menuMyFleets.setIcon(ImagesBundle.Util.get("ICON-vehicle1"));
		menuMyFleets.setVisible(false);
		menuFleet.add(menuMyFleets);

		MenuItem menuMyDevices = new MenuItem(constants.My_Devices());
		menuMyDevices.setIcon(ImagesBundle.Util.get("ICON-device"));
		menuMyDevices.setVisible(false);
		menuFleet.add(menuMyDevices);

		MenuItem menuMyDrivers = new MenuItem(constants.My_Drivers());
		menuMyDrivers.setIcon(ImagesBundle.Util.get("ICON-operator"));
		menuMyDrivers.setVisible(false);
		menuFleet.add(menuMyDrivers);

		btnFleetManagement.setMenu(menuFleet);

		// Alerts
		btnAlerts = new SplitButton(constants.Alerts());
		btnAlerts.setIcon(ImagesBundle.Util.get("ICON-alertIcon"));
		btnAlerts.setWidth(75);
		btnAlerts.setHeight(30);
		btnAlerts.setVisible(false);

		Menu menuAlerts = new Menu();

		// MenuItem menuAlertServices = new
		// MenuItem(constants.Alert_Services());
		// menuAlertServices.setIcon(ImagesBundle.Util.get("ICON-servicesIcon"));
		// menuAlerts.add(menuAlertServices);

		MenuItem menuAlertAlertSystem = new MenuItem(constants.Alert_System());
		menuAlertAlertSystem.setIcon(ImagesBundle.Util
				.get("ICON-alertsystemIcon"));
		menuAlertAlertSystem.setVisible(false);
		MenuItem menuAlertConfig = new MenuItem(constants.Alert_Configuration());
		menuAlertConfig.setIcon(ImagesBundle.Util.get("ICON-alertsystemIcon"));
		menuAlertConfig.setVisible(false);

		menuAlerts.add(menuAlertConfig);
		// menuAlerts.add(menuAlertAlertSystem);

		btnAlerts.setMenu(menuAlerts);

		// User Pref

		SplitButton btnUserpreferences = new SplitButton(
				constants.User_Preference());
		btnUserpreferences
				.setIcon(ImagesBundle.Util.get("ICON-userPreference"));
		btnUserpreferences.setWidth(140);
		btnUserpreferences.setHeight(30);
		btnUserpreferences.setVisible(false);

		Menu menuUserpreferences = new Menu();

		MenuItem menuLandmarks = new MenuItem(constants.Landmarks());
		menuLandmarks.setIcon(ImagesBundle.Util.get("ICON-landmarksIcon"));
		menuLandmarks.setVisible(false);
		menuUserpreferences.add(menuLandmarks);

		MenuItem menuVehicles = new MenuItem(constants.Vehicles());
		menuVehicles.setIcon(ImagesBundle.Util.get("ICON-myvehicleIcon"));
		menuVehicles.setVisible(false);
		menuUserpreferences.add(menuVehicles);

		btnUserpreferences.setMenu(menuUserpreferences);

		// Reports

		SplitButton btnReports = new SplitButton(constants.Reports());
		btnReports.setIcon(ImagesBundle.Util.get("ICON-reportsIcon"));
		btnReports.setWidth(85);
		btnReports.setHeight(30);
		btnReports.setVisible(false);

		Menu menuReports = new Menu();

		MenuItem menuGeozoneReport = new MenuItem(constants.Geozone_Report());
		menuGeozoneReport.setIcon(ImagesBundle.Util.get("ICON-geoAlertMap"));
		menuGeozoneReport.setVisible(false);
		// menuReports.add(menuGeozoneReport);

		MenuItem menuVehicleMap = new MenuItem(constants.Geozone_Vehicle_Map());
		menuVehicleMap.setIcon(ImagesBundle.Util.get("ICON-mapvehicleIcon"));
		menuVehicleMap.setVisible(false);
		menuReports.add(menuVehicleMap);

		MenuItem menuGeozoneMap = new MenuItem(constants.Vehicle_Geozone_Map());
		menuGeozoneMap.setIcon(ImagesBundle.Util.get("ICON-vehiclemapIcon"));
		menuGeozoneMap.setVisible(false);
		menuReports.add(menuGeozoneMap);

		MenuItem menuMaintenance = new MenuItem(constants.Maintenance());
		menuMaintenance.setIcon(ImagesBundle.Util.get("ICON-maintRpt"));
		menuMaintenance.setVisible(false);
		menuReports.add(menuMaintenance);

		MenuItem menuOverspeedSummary = new MenuItem(
				constants.Overspeed_Summary());
		menuOverspeedSummary
				.setIcon(ImagesBundle.Util.get("ICON-overSpeedRpt"));
		menuOverspeedSummary.setVisible(false);
		menuReports.add(menuOverspeedSummary);

		MenuItem menuVehicleAlerts = new MenuItem(
				constants.Vehicle_Alerts_Report());
		menuVehicleAlerts.setIcon(ImagesBundle.Util.get("ICON-vehiAlertRpt"));
		menuVehicleAlerts.setVisible(false);
		menuReports.add(menuVehicleAlerts);

		MenuItem menuVehicleSummary = new MenuItem(
				constants.Vehicle_Complete_Summary());
		menuVehicleSummary.setIcon(ImagesBundle.Util
				.get("ICON-vehiCompSummRpt"));
		menuVehicleSummary.setVisible(false);
		menuReports.add(menuVehicleSummary);

		MenuItem menuVehicleMovement = new MenuItem(
				constants.Vehicle_Movement_Report());
		menuVehicleMovement.setIcon(ImagesBundle.Util.get("ICON-vehiMoveRpt"));
		menuVehicleMovement.setVisible(false);
		menuReports.add(menuVehicleMovement);

		MenuItem menuVehicleStatus = new MenuItem(
				constants.Vehicle_Status_Report());
		menuVehicleStatus.setIcon(ImagesBundle.Util.get("ICON-vehiStatusRpt"));
		menuVehicleStatus.setVisible(false);
		menuReports.add(menuVehicleStatus);

		MenuItem menuVehicleStop = new MenuItem(constants.Vehicle_stop_Report());
		menuVehicleStop.setIcon(ImagesBundle.Util.get("ICON-reportIcon"));
		menuVehicleStop.setVisible(false);
		menuReports.add(menuVehicleStop);

		btnReports.setMenu(menuReports);

		// NEW
		SplitButton btnVehicle = new SplitButton("Vehicle Management");
		btnVehicle.setIcon(ImagesBundle.Util.get("ICON-vehicle"));
		btnVehicle.setWidth(180);
		btnVehicle.setHeight(30);
		btnVehicle.setVisible(false);

		Menu menuVehicle = new Menu();

		MenuItem menuVehicleDetails = new MenuItem("Vehicle");
		menuVehicleDetails.setIcon(ImagesBundle.Util.get("ICON-vehicle1"));
		menuVehicleDetails.setVisible(false);
		menuVehicle.add(menuVehicleDetails);

		MenuItem menuOperatorDetails = new MenuItem("Operator");
		menuOperatorDetails.setIcon(ImagesBundle.Util.get("ICON-operator"));
		menuOperatorDetails.setVisible(false);
		menuVehicle.add(menuOperatorDetails);

		MenuItem menuDeviceDetails = new MenuItem("Device");
		menuDeviceDetails.setIcon(ImagesBundle.Util.get("ICON-device"));
		menuDeviceDetails.setVisible(false);
		menuVehicle.add(menuDeviceDetails);

		MenuItem menuGeozone = new MenuItem("GeoFencing");
		menuGeozone.setIcon(ImagesBundle.Util.get("ICON-I_16geofen1"));
		menuGeozone.setVisible(false);
		menuVehicle.add(menuGeozone);

		MenuItem menuSimcardDetails = new MenuItem("Simcard");
		menuSimcardDetails.setIcon(ImagesBundle.Util.get("ICON-I_sim"));
		menuSimcardDetails.setVisible(false);
		menuVehicle.add(menuSimcardDetails);

		btnVehicle.setMenu(menuVehicle);

		SplitButton btnAssociate = new SplitButton("Association");
		btnAssociate.setIcon(ImagesBundle.Util.get("ICON-associate"));
		btnAssociate.setWidth(100);
		btnAssociate.setHeight(30);
		btnAssociate.setVisible(false);

		Menu menuAssociate = new Menu();

		MenuItem menuVehicleOperator = new MenuItem(
				"Vehicle Operator Association");
		menuVehicleOperator.setIcon(ImagesBundle.Util.get("ICON-operator"));
		menuVehicleOperator.setVisible(false);
		menuAssociate.add(menuVehicleOperator);

		MenuItem menuVehicleDevice = new MenuItem("Vehicle Device Association");
		menuVehicleDevice.setIcon(ImagesBundle.Util.get("ICON-device"));
		menuVehicleDevice.setVisible(false);
		menuAssociate.add(menuVehicleDevice);

		MenuItem menuVehicleGeozone = new MenuItem(
				"Vehicle Geozone Association");
		menuVehicleGeozone.setIcon(ImagesBundle.Util.get("ICON-VehicleGeo"));
		menuVehicleGeozone.setVisible(false);
		menuAssociate.add(menuVehicleGeozone);

		MenuItem menuDeviceSimcard = new MenuItem("Device Simcard Association");
		menuDeviceSimcard.setIcon(ImagesBundle.Util.get("ICON-I_sim"));
		menuDeviceSimcard.setVisible(false);
		menuAssociate.add(menuDeviceSimcard);

		btnAssociate.setMenu(menuAssociate);

		// Added by Niaz for Company admin menu on 2010-12-02

		SplitButton btnCompany = new SplitButton("Company Management");
		btnCompany.setIcon(ImagesBundle.Util.get("ICON-tracker"));
		btnCompany.setWidth(150);
		btnCompany.setHeight(30);
		btnCompany.setVisible(false);

		Menu menuBranch = new Menu();

		MenuItem menuBranchDetails = new MenuItem("Company Branches");
		menuBranchDetails.setIcon(ImagesBundle.Util.get("ICON-live"));
		menuBranchDetails.setVisible(false);
		menuBranch.add(menuBranchDetails);

		MenuItem menuImageUploadDetails = new MenuItem("Image Upload");
		menuImageUploadDetails.setIcon(ImagesBundle.Util.get("ICON-dashBoard"));
		menuImageUploadDetails.setVisible(false);
		menuBranch.add(menuImageUploadDetails);

		btnCompany.setMenu(menuBranch);

		// Added by Niaz for LTS Super Admin menu on 2010-12-02

		SplitButton btnSuperAdmin = new SplitButton("<b>Supervisor</b>");
		btnSuperAdmin.setIcon(ImagesBundle.Util.get("ICON-tracker"));
		btnSuperAdmin.setWidth(120);
		btnSuperAdmin.setHeight(30);
		btnSuperAdmin.setVisible(false);

		SplitButton btnSuperAdminreport = new SplitButton("<b>Reports</b>");
		btnSuperAdminreport.setIcon(ImagesBundle.Util.get("ICON-reportsIcon"));
		btnSuperAdminreport.setWidth(120);
		btnSuperAdminreport.setHeight(30);
		btnSuperAdminreport.setVisible(false);

		Button btnAssetCommand = new Button("<b>Commands</b>");
		btnAssetCommand.setWidth(120);
		btnAssetCommand.setHeight(30);
		btnAssetCommand.setVisible(false);
		btnAssetCommand
				.addSelectionListener(new SelectionListener<ButtonEvent>() {

					@Override
					public void componentSelected(ButtonEvent ce) {
						// TODO Auto-generated method stub
//					    PopupPanel pbDeviceCommand = new PopupPanel();
						tabItem = new TabItem("Device Command");
						tabItem.setIcon(ImagesBundle.Util.get("ICON-allcompany"));
   					    VerticalPanel verticalPanel = new VerticalPanel();
						tabItem.add(new deviceCommand());
						tabItem.setScrollMode(Scroll.AUTO);
						tabItem.setClosable(true);
						tabPanel.add(tabItem);
						tabPanel.setSelection(tabItem);
						hMap.put("Device Command", tabItem);
                       }

				});

		Menu menubtnSuperAdmin = new Menu();
		Menu menubtnSuperAdminreport = new Menu();

		MenuItem menuErrorLogDetails = new MenuItem("Error Log Report");
		menuErrorLogDetails.setIcon(ImagesBundle.Util.get("ICON-error"));
		menuErrorLogDetails.setVisible(false);
		menubtnSuperAdminreport.add(menuErrorLogDetails);

		MenuItem menuBWUtilDetails = new MenuItem(
				"BandWidth Utilization Report");
		menuBWUtilDetails.setIcon(ImagesBundle.Util.get("ICON-bwusage"));
		menuBWUtilDetails.setVisible(false);
		menubtnSuperAdminreport.add(menuBWUtilDetails);

		MenuItem menuLoginAccessDetails = new MenuItem("Login Access Report");
		menuLoginAccessDetails.setIcon(ImagesBundle.Util.get("ICON-login"));
		menuLoginAccessDetails.setVisible(false);
		menubtnSuperAdminreport.add(menuLoginAccessDetails);

		MenuItem menuVehicleEventDetails = new MenuItem("Vehicle Event Report");
		menuVehicleEventDetails.setIcon(ImagesBundle.Util
				.get("ICON-vehiclereport"));
		menuVehicleEventDetails.setVisible(false);
		menubtnSuperAdminreport.add(menuVehicleEventDetails);

		MenuItem menuImeiDetails = new MenuItem("Vehicles Devices Report");
		menuImeiDetails.setIcon(ImagesBundle.Util.get("ICON-bwusage"));
		menuImeiDetails.setVisible(false);
		menubtnSuperAdminreport.add(menuImeiDetails);

		MenuItem menuCompanyFeaturesDetails = new MenuItem(
				"Company Features Details");
		menuCompanyFeaturesDetails.setIcon(ImagesBundle.Util
				.get("ICON-feature"));
		menuCompanyFeaturesDetails.setVisible(false);
		menubtnSuperAdmin.add(menuCompanyFeaturesDetails);

		MenuItem menuSendEmail = new MenuItem("Send Email");
		menuSendEmail.setIcon(ImagesBundle.Util.get("ICON-bktrk"));
		menuSendEmail.setVisible(false);
		menubtnSuperAdmin.add(menuSendEmail);

		MenuItem menuSMSCount = new MenuItem("SMS Count");
		menuSMSCount.setIcon(ImagesBundle.Util.get("ICON-count"));
		menuSMSCount.setVisible(false);
		menubtnSuperAdmin.add(menuSMSCount);

		MenuItem menuSummaryReportBulk = new MenuItem("Bulk Summary Report");
		menuSummaryReportBulk.setIcon(ImagesBundle.Util.get("ICON-bulkreport"));
		menuSummaryReportBulk.setVisible(false);
		menubtnSuperAdminreport.add(menuSummaryReportBulk);

		MenuItem menuNonTransRpt = new MenuItem("No Transmission Report");
		// menuNonTransRpt.setIcon(ImagesBundle.Util.get("ICON-bulkreport"));
		menuNonTransRpt.setVisible(true);
		menubtnSuperAdminreport.add(menuNonTransRpt);

		MenuItem menuSmsBalance = new MenuItem("Sms Balance Report");
		menuNonTransRpt.setIcon(ImagesBundle.Util.get("ICON-sms"));
		menuSmsBalance.setVisible(true);
		menubtnSuperAdminreport.add(menuSmsBalance);

		MenuItem menuAllCompanysDetails = new MenuItem("All Companys Details");
		menuNonTransRpt.setIcon(ImagesBundle.Util.get("ICON-allcompany"));
		menuAllCompanysDetails.setVisible(true);
		menubtnSuperAdminreport.add(menuAllCompanysDetails);

		MenuItem menuNewCompany = new MenuItem("Register New Company");
		menuNewCompany.setIcon(ImagesBundle.Util.get("ICON-Newcompany"));
		menuNewCompany.setVisible(false);
		menubtnSuperAdmin.add(menuNewCompany);

		MenuItem menuResellerDetails = new MenuItem("Reseller Details");
		menuNonTransRpt.setIcon(ImagesBundle.Util.get("ICON-allcompany"));
		menuResellerDetails.setVisible(true);
		menubtnSuperAdminreport.add(menuResellerDetails);

		MenuItem menuBatchListener = new MenuItem("Batch Listener");
		menuBatchListener.setIcon(ImagesBundle.Util.get("ICON-listener"));
		menuBatchListener.setVisible(false);
		menubtnSuperAdmin.add(menuBatchListener);

		btnSuperAdmin.setMenu(menubtnSuperAdmin);
		btnSuperAdminreport.setMenu(menubtnSuperAdminreport);

		add(btnDashBoard);
		setSpacing(3);
		add(btnTrack);
		add(btnFleetManagement);
		add(btnAlerts);
		add(btnUserpreferences);
		add(btnReports);
		add(btnVehicle);
		add(btnAssociate);
		add(btnCompany);
		add(btnSuperAdmin);
		add(btnSuperAdminreport);
		add(btnAssetCommand);

		if (LoginDashboardModule.compRole.equalsIgnoreCase("FleetManager")) {
			btnDashBoard.setVisible(true);
			// if (true) {
			tabItem = new TabItem(constants.Dash_Board());
			if (hMap.get(constants.Dash_Board()) == null) {
				tabItem = new TabItem(constants.Dash_Board());
				tabItem.setIcon(ImagesBundle.Util.get("ICON-dashBoard"));
				// tabItem.add(new DashBoard());
				tabItem.setScrollMode(Scroll.AUTO);
				tabPanel.add(tabItem);
				tabPanel.setSelection(tabItem);
				// new DashBoard(NavigationPanel.bodyPanel,
				// LoginDashboardModule.companyId,LoginDashboardModule.branchId);
				hMap.put(constants.Dash_Board(), tabItem);
			} else
				tabPanel.setSelection(tabItem);
			tabItem.addListener(Events.Close, new Listener<BaseEvent>() {
				public void handleEvent(BaseEvent be) {
					tabItem = hMap.put(constants.Dash_Board(), null);
				}
			});
			// }

			// Features customization
			hashMapFeatures = LoginDashboardModule.compnayFeaturesList;
			for (Map.Entry<String, List<String>> e : hashMapFeatures.entrySet()) {
				if (e.getKey().equalsIgnoreCase("Tracking")) {
					List<String> listOfFeatures = new ArrayList<String>();
					btnTrack.setVisible(true);
					listOfFeatures = e.getValue();
					for (int i = 0; i < listOfFeatures.size(); i++) {
						if (listOfFeatures.get(i).equalsIgnoreCase(
								"Live Tracking")) {
							menuLiveTracking.setVisible(true);
						} else if (listOfFeatures.get(i).equalsIgnoreCase(
								"Back Tracking")) {
							menuBackTracking.setVisible(true);
						} else if (listOfFeatures.get(i).equalsIgnoreCase(
								"Multi Tracking")) {
							menuMultiTracking.setVisible(true);
						}
					}

				} else if (e.getKey().equalsIgnoreCase("Fleet Management")) {
					List<String> listOfFeatures = new ArrayList<String>();
					btnFleetManagement.setVisible(true);
					listOfFeatures = e.getValue();
					for (int i = 0; i < listOfFeatures.size(); i++) {
						if (listOfFeatures.get(i).equalsIgnoreCase("My Fleets")) {
							menuMyFleets.setVisible(true);
						} else if (listOfFeatures.get(i).equalsIgnoreCase(
								"My Devices")) {
							menuMyDevices.setVisible(true);
						} else if (listOfFeatures.get(i).equalsIgnoreCase(
								"My Drivers")) {
							menuMyDrivers.setVisible(true);
						}
					}

				} else if (e.getKey().equalsIgnoreCase("Alerts")) {
					List<String> listOfFeatures = new ArrayList<String>();
					btnAlerts.setVisible(true);
					listOfFeatures = e.getValue();
					for (int i = 0; i < listOfFeatures.size(); i++) {
						if (listOfFeatures.get(i).equalsIgnoreCase(
								"Alert Configuration")) {
							menuAlertConfig.setVisible(true);
						}
					}

				} else if (e.getKey().equalsIgnoreCase("Reports")) {
					List<String> listOfFeatures = new ArrayList<String>();
					btnReports.setVisible(true);
					listOfFeatures = e.getValue();
					for (int i = 0; i < listOfFeatures.size(); i++) {
						if (listOfFeatures.get(i).equalsIgnoreCase(
								"Geozone Report")) {
							menuGeozoneReport.setVisible(true);
						} else if (listOfFeatures.get(i).equalsIgnoreCase(
								"Geozone Vehicles Map")) {
							menuGeozoneMap.setVisible(true);
						} else if (listOfFeatures.get(i).equalsIgnoreCase(
								"Vehicle Geozones Map")) {
							menuVehicleMap.setVisible(true);

						} else if (listOfFeatures.get(i).equalsIgnoreCase(
								"Maintenance")) {
							menuMaintenance.setVisible(true);
						} else if (listOfFeatures.get(i).equalsIgnoreCase(
								"Overspeed Summary Report")) {
							menuOverspeedSummary.setVisible(true);
						} else if (listOfFeatures.get(i).equalsIgnoreCase(
								"Vehicle Alerts Report")) {
							menuVehicleAlerts.setVisible(true);

						} else if (listOfFeatures.get(i).equalsIgnoreCase(
								"Vehicle Complete Summary")) {
							menuVehicleSummary.setVisible(true);
						} else if (listOfFeatures.get(i).equalsIgnoreCase(
								"Vehicle Movement Report")) {
							menuVehicleMovement.setVisible(true);
						} else if (listOfFeatures.get(i).equalsIgnoreCase(
								"Vehicle Status Report")) {
							menuVehicleStatus.setVisible(true);
						} else if (listOfFeatures.get(i).equalsIgnoreCase(
								"Vehicle Stop Report")) {
							menuVehicleStop.setVisible(true);
						} else if (listOfFeatures.get(i).equalsIgnoreCase(
								"All Companys Details")) {
							menuVehicleStop.setVisible(true);
						}
					}
				} else if (e.getKey().equalsIgnoreCase("Header Panel")) {
					List<String> listOfFeatures = new ArrayList<String>();
					listOfFeatures = e.getValue();
					for (int i = 0; i < listOfFeatures.size(); i++) {
						if (listOfFeatures.get(i).equalsIgnoreCase("Calendar")) {
							// dateIcon.setVisible(true);
						}
					}
				} else if (e.getKey().equalsIgnoreCase("User Preference")) {
					List<String> listOfFeatures = new ArrayList<String>();
					btnUserpreferences.setVisible(true);
					// btnUserpreferences.setEnabled(false);
					listOfFeatures = e.getValue();
					for (int i = 0; i < listOfFeatures.size(); i++) {
						if (listOfFeatures.get(i).equalsIgnoreCase("Landmarks")) {
							menuLandmarks.setVisible(true);
							menuLandmarks.setEnabled(false);
						} else if (listOfFeatures.get(i).equalsIgnoreCase(
								"Vehicles")) {
							menuVehicles.setVisible(true);
							menuVehicles.setEnabled(false);
						}
					}
				}
			}
		} else if (LoginDashboardModule.compRole
				.equalsIgnoreCase("VehicleAdmin")) {
			btnVehicle.setVisible(true);
			menuVehicleDetails.setVisible(true);
			menuOperatorDetails.setVisible(true);
			menuDeviceDetails.setVisible(true);
			menuSimcardDetails.setVisible(true);
			menuGeozone.setVisible(true);
			btnAssociate.setVisible(true);
			menuVehicleOperator.setVisible(true);
			menuVehicleDevice.setVisible(true);
			menuVehicleGeozone.setVisible(true);
			menuDeviceSimcard.setVisible(true);
		} else if (LoginDashboardModule.compRole
				.equalsIgnoreCase("CompanyAdmin")) {
			btnCompany.setVisible(true);
			menuBranchDetails.setVisible(true);
			menuImageUploadDetails.setVisible(true);
		} else if (LoginDashboardModule.compRole.equalsIgnoreCase("SuperAdmin")) {
			btnSuperAdmin.setVisible(true);
			btnSuperAdminreport.setVisible(true);
			btnAssetCommand.setVisible(true);
			menuErrorLogDetails.setVisible(true);
			menuLoginAccessDetails.setVisible(true);
			menuBWUtilDetails.setVisible(true);
			menuVehicleEventDetails.setVisible(true);
			menuCompanyFeaturesDetails.setVisible(true);
			// menuSendEmail.setVisible(true);
			menuSMSCount.setVisible(true);
			menuNewCompany.setVisible(true);
			// menuResellerDetails.setVisible(true);
			menuBatchListener.setVisible(true);
			menuSummaryReportBulk.setVisible(true);
			menuImeiDetails.setVisible(true);
		}

		// listener

		menuErrorLogDetails
				.addSelectionListener(new SelectionListener<MenuEvent>() {

					@Override
					public void componentSelected(MenuEvent ce) {
						// NavigationPanel.isLive = false;
						// NavigationPanel.bodyPanel.clear();
						tabItem = hMap.get("ErrorLogDetails");
						if (tabItem == null) {
							tabItem = new TabItem("Error Log Report");
							tabItem.setIcon(ImagesBundle.Util.get("ICON-error"));
							VerticalPanel verticalPanel = new VerticalPanel();
							new ErrorLogReport(LoginDashboardModule.companyId,
									LoginDashboardModule.branchId,
									LoginDashboardModule.userName,
									LoginDashboardModule.compRole,
									verticalPanel);

							tabItem.add(verticalPanel);
							tabItem.setScrollMode(Scroll.AUTO);
							tabItem.setClosable(true);
							tabPanel.add(tabItem);
							tabPanel.setSelection(tabItem);
							hMap.put("ErrorLogDetails", tabItem);
						} else
							tabPanel.setSelection(tabItem);
						tabItem.addListener(Events.Close,
								new Listener<BaseEvent>() {
									public void handleEvent(BaseEvent be) {
										tabItem = hMap.put("ErrorLogDetails",
												null);
									}
								});
					}
				});

		menuBWUtilDetails
				.addSelectionListener(new SelectionListener<MenuEvent>() {

					@Override
					public void componentSelected(MenuEvent ce) {
						// NavigationPanel.isLive = false;
						// NavigationPanel.bodyPanel.clear();
						tabItem = hMap.get("BandWidth Utilization Report");
						if (tabItem == null) {
							tabItem = new TabItem(
									"BandWidth Utilization Report");
							tabItem.setIcon(ImagesBundle.Util
									.get("ICON-bwusage"));
							VerticalPanel verticalPanel = new VerticalPanel();
							new BandwidthUtilizationReport(verticalPanel,
									LoginDashboardModule.companyId,
									LoginDashboardModule.branchId,
									LoginDashboardModule.userName,
									LoginDashboardModule.compRole);

							tabItem.add(verticalPanel);
							tabItem.setScrollMode(Scroll.AUTO);
							tabItem.setClosable(true);
							tabPanel.add(tabItem);
							tabPanel.setSelection(tabItem);
							hMap.put("BWUtilDetails", tabItem);
						} else
							tabPanel.setSelection(tabItem);
						tabItem.addListener(Events.Close,
								new Listener<BaseEvent>() {
									public void handleEvent(BaseEvent be) {
										tabItem = hMap.put(
												"BandWidth Utilization Report",
												null);
									}
								});
					}
				});

		menuLoginAccessDetails
				.addSelectionListener(new SelectionListener<MenuEvent>() {

					@Override
					public void componentSelected(MenuEvent ce) {
						// NavigationPanel.isLive = false;
						// NavigationPanel.bodyPanel.clear();
						tabItem = hMap.get("LoginAccessDetails");
						if (tabItem == null) {
							tabItem = new TabItem("Login Access Report");
							tabItem.setIcon(ImagesBundle.Util.get("ICON-login"));
							VerticalPanel verticalPanel = new VerticalPanel();
							new LoginAccessReports(
									LoginDashboardModule.companyId,
									LoginDashboardModule.branchId,
									LoginDashboardModule.userName,
									LoginDashboardModule.compRole,
									verticalPanel);

							tabItem.add(verticalPanel);
							tabItem.setScrollMode(Scroll.AUTO);
							tabItem.setClosable(true);
							tabPanel.add(tabItem);
							tabPanel.setSelection(tabItem);
							hMap.put("LoginAccessDetails", tabItem);
						} else
							tabPanel.setSelection(tabItem);
						tabItem.addListener(Events.Close,
								new Listener<BaseEvent>() {
									public void handleEvent(BaseEvent be) {
										tabItem = hMap.put(
												"LoginAccessDetails", null);
									}
								});
					}
				});
		menuVehicleEventDetails
				.addSelectionListener(new SelectionListener<MenuEvent>() {

					@Override
					public void componentSelected(MenuEvent ce) {
						// NavigationPanel.isLive = false;
						// NavigationPanel.bodyPanel.clear();
						tabItem = hMap.get("Vehicle Event Report");
						if (tabItem == null) {
							tabItem = new TabItem("Vehicle Event Report");
							tabItem.setIcon(ImagesBundle.Util
									.get("ICON-vehiclereport"));
							VerticalPanel verticalPanel = new VerticalPanel();
							verticalPanel.setSize("100%", "100%");
							new VehicleEventReport(
									LoginDashboardModule.companyId,
									LoginDashboardModule.branchId,
									LoginDashboardModule.userName,
									LoginDashboardModule.compRole,
									verticalPanel);

							tabItem.add(verticalPanel);
							tabItem.setScrollMode(Scroll.AUTO);
							tabItem.setClosable(true);
							tabPanel.add(tabItem);
							tabPanel.setSelection(tabItem);
							hMap.put("Vehicle Event Report", tabItem);
						} else
							tabPanel.setSelection(tabItem);
						tabItem.addListener(Events.Close,
								new Listener<BaseEvent>() {
									public void handleEvent(BaseEvent be) {
										tabItem = hMap.put(
												"Vehicle Event Report", null);
									}
								});
					}
				});
		menuImeiDetails
				.addSelectionListener(new SelectionListener<MenuEvent>() {

					@Override
					public void componentSelected(MenuEvent ce) {
						// NavigationPanel.isLive = false;
						// NavigationPanel.bodyPanel.clear();
						tabItem = hMap.get("VehiclesIMEI");
						if (tabItem == null) {
							tabItem = new TabItem("Vehicles Devices Report");
							tabItem.setIcon(ImagesBundle.Util
									.get("ICON-bwusage"));
							VerticalPanel verticalPanel = new VerticalPanel();
							new VehiclesDevicesReports(
									LoginDashboardModule.companyId,
									LoginDashboardModule.branchId,
									LoginDashboardModule.userName,
									LoginDashboardModule.compRole,
									verticalPanel);

							tabItem.add(verticalPanel);
							tabItem.setScrollMode(Scroll.AUTO);
							tabItem.setClosable(true);
							tabPanel.add(tabItem);
							tabPanel.setSelection(tabItem);
							hMap.put("VehiclesIMEI", tabItem);
						} else
							tabPanel.setSelection(tabItem);
						tabItem.addListener(Events.Close,
								new Listener<BaseEvent>() {
									public void handleEvent(BaseEvent be) {
										tabItem = hMap
												.put("VehiclesIMEI", null);
									}
								});
					}
				});

		menuCompanyFeaturesDetails
				.addSelectionListener(new SelectionListener<MenuEvent>() {

					@Override
					public void componentSelected(MenuEvent ce) {
						// NavigationPanel.isLive = false;
						// NavigationPanel.bodyPanel.clear();
						tabItem = hMap.get("CompanyFeaturesDetails");
						if (tabItem == null) {
							tabItem = new TabItem("Company Features Details");
							tabItem.setIcon(ImagesBundle.Util
									.get("ICON-feature"));
							VerticalPanel verticalPanel = new VerticalPanel();
							new CompanyFeatures(verticalPanel,
									LoginDashboardModule.companyId,
									LoginDashboardModule.branchId);
							tabItem.add(verticalPanel);
							tabItem.setScrollMode(Scroll.AUTO);
							tabItem.setClosable(true);
							tabPanel.add(tabItem);
							tabPanel.setSelection(tabItem);
							hMap.put("CompanyFeaturesDetails", tabItem);
						} else
							tabPanel.setSelection(tabItem);
						tabItem.addListener(Events.Close,
								new Listener<BaseEvent>() {
									public void handleEvent(BaseEvent be) {
										tabItem = hMap.put(
												"CompanyFeaturesDetails", null);
									}
								});
					}
				});

		menuSendEmail.addSelectionListener(new SelectionListener<MenuEvent>() {

			@Override
			public void componentSelected(MenuEvent ce) {
				// NavigationPanel.isLive = false;
				// NavigationPanel.bodyPanel.clear();
				tabItem = hMap.get("Send Email");
				if (tabItem == null) {
					tabItem = new TabItem("Send Email");
					tabItem.setIcon(ImagesBundle.Util.get("ICON-bktrk"));
					VerticalPanel verticalPanel = new VerticalPanel();
					new SendEmail(verticalPanel,
							LoginDashboardModule.companyId,
							LoginDashboardModule.branchId);
					tabItem.add(verticalPanel);
					tabItem.setScrollMode(Scroll.AUTO);
					tabItem.setClosable(true);
					tabPanel.add(tabItem);
					tabPanel.setSelection(tabItem);
					hMap.put("Send Email", tabItem);
				} else
					tabPanel.setSelection(tabItem);
				tabItem.addListener(Events.Close, new Listener<BaseEvent>() {
					public void handleEvent(BaseEvent be) {
						tabItem = hMap.put("Send Email", null);
					}
				});
			}
		});

		menuSMSCount.addSelectionListener(new SelectionListener<MenuEvent>() {

			@Override
			public void componentSelected(MenuEvent ce) {
				// NavigationPanel.isLive = false;
				// NavigationPanel.bodyPanel.clear();
				tabItem = hMap.get("SMS Count");
				if (tabItem == null) {
					tabItem = new TabItem("SMS Count");
					tabItem.setIcon(ImagesBundle.Util.get("ICON-count"));
					VerticalPanel verticalPanel = new VerticalPanel();
					new SMSCount(verticalPanel, LoginDashboardModule.companyId,
							LoginDashboardModule.branchId);
					tabItem.add(verticalPanel);
					tabItem.setScrollMode(Scroll.AUTO);
					tabItem.setClosable(true);
					tabPanel.add(tabItem);
					tabPanel.setSelection(tabItem);
					hMap.put("SMS Count", tabItem);
				} else
					tabPanel.setSelection(tabItem);
				tabItem.addListener(Events.Close, new Listener<BaseEvent>() {
					public void handleEvent(BaseEvent be) {
						tabItem = hMap.put("SMS Count", null);
					}
				});
			}
		});
		menuNewCompany.addSelectionListener(new SelectionListener<MenuEvent>() {

			@Override
			public void componentSelected(MenuEvent ce) {
				tabItem = hMap.get("Register New Company");
				if (tabItem == null) {
					tabItem = new TabItem("Register New Company");
					tabItem.setIcon(ImagesBundle.Util.get("ICON-Newcompany"));
					VerticalPanel verticalPanel = new VerticalPanel();
					tabItem.add(new PagingCompanyDetailsGrid(
							LoginDashboardModule.companyId,
							LoginDashboardModule.branchId,
							LoginDashboardModule.userName));
					tabItem.setScrollMode(Scroll.AUTO);
					tabItem.setClosable(true);
					tabPanel.add(tabItem);
					tabPanel.setSelection(tabItem);
					hMap.put("Register New Company", tabItem);
				} else
					tabPanel.setSelection(tabItem);
				tabItem.addListener(Events.Close, new Listener<BaseEvent>() {
					public void handleEvent(BaseEvent be) {
						tabItem = hMap.put("Register New Company", null);
					}
				});
			}
		});

		menuResellerDetails
				.addSelectionListener(new SelectionListener<MenuEvent>() {

					@Override
					public void componentSelected(MenuEvent ce) {
						tabItem = hMap.get("Reseller Details");
						if (tabItem == null) {
							tabItem = new TabItem("Reseller Details");
							tabItem.setIcon(ImagesBundle.Util
									.get("ICON-allcompany"));
							tabItem.add(new ResellerDetails());
							tabItem.setScrollMode(Scroll.AUTO);
							tabItem.setClosable(true);
							tabPanel.add(tabItem);
							tabPanel.setSelection(tabItem);
							hMap.put("Reseller Details", tabItem);
						} else
							tabPanel.setSelection(tabItem);
						tabItem.addListener(Events.Close,
								new Listener<BaseEvent>() {
									public void handleEvent(BaseEvent be) {
										tabItem = hMap.put("Reseller Details",
												null);
									}
								});
					}
				});

		menuBatchListener
				.addSelectionListener(new SelectionListener<MenuEvent>() {

					@Override
					public void componentSelected(MenuEvent ce) {
						tabItem = hMap.get("Batch Listener");
						if (tabItem == null) {
							tabItem = new TabItem("Batch Listener");
							tabItem.setIcon(ImagesBundle.Util
									.get("ICON-listener"));
							tabItem.add(new BatchListeners());
							tabItem.setScrollMode(Scroll.AUTO);
							tabItem.setClosable(true);
							tabPanel.add(tabItem);
							tabPanel.setSelection(tabItem);
							hMap.put("Batch Listener", tabItem);
						} else
							tabPanel.setSelection(tabItem);
						tabItem.addListener(Events.Close,
								new Listener<BaseEvent>() {
									public void handleEvent(BaseEvent be) {
										tabItem = hMap.put("Batch Listener",
												null);
									}
								});
					}
				});
		menuSummaryReportBulk
				.addSelectionListener(new SelectionListener<MenuEvent>() {

					@Override
					public void componentSelected(MenuEvent ce) {
						// NavigationPanel.isLive = false;
						// NavigationPanel.bodyPanel.clear();
						tabItem = hMap.get("BulkSummary Report");
						if (tabItem == null) {
							tabItem = new TabItem("BulkSummary Report");
							tabItem.setIcon(ImagesBundle.Util
									.get("ICON-bulkreport"));
							VerticalPanel verticalPanel = new VerticalPanel();
							new SummaryReportBulkExceution(verticalPanel,
									LoginDashboardModule.companyId,
									LoginDashboardModule.branchId);
							tabItem.add(verticalPanel);
							tabItem.setScrollMode(Scroll.AUTO);
							tabItem.setClosable(true);
							tabPanel.add(tabItem);
							tabPanel.setSelection(tabItem);
							hMap.put("Bulk Summary Report", tabItem);
						} else
							tabPanel.setSelection(tabItem);
						tabItem.addListener(Events.Close,
								new Listener<BaseEvent>() {
									public void handleEvent(BaseEvent be) {
										tabItem = hMap.put(
												"Bulk Summary Report", null);
									}
								});
					}
				});

		menuNonTransRpt
				.addSelectionListener(new SelectionListener<MenuEvent>() {

					@Override
					public void componentSelected(MenuEvent ce) {
						tabItem = hMap.get("No Transmission Report");
						if (tabItem == null) {
							tabItem = new TabItem("No Transmission Report");
							tabItem.setIcon(ImagesBundle.Util
									.get("ICON-bulkreport"));
							// VerticalPanel verticalPanel = new
							// VerticalPanel();
							// new NoTransmisRpt(verticalPanel,
							// LoginDashboardModule.companyId,
							// LoginDashboardModule.branchId);
							tabItem.add(new NoTransmisRpt());
							tabItem.setScrollMode(Scroll.AUTO);
							tabItem.setClosable(true);
							tabPanel.add(tabItem);
							tabPanel.setSelection(tabItem);
							hMap.put("No Transmission Report", tabItem);
						} else
							tabPanel.setSelection(tabItem);
						tabItem.addListener(Events.Close,
								new Listener<BaseEvent>() {
									public void handleEvent(BaseEvent be) {
										tabItem = hMap.put(
												"No Transmission Report", null);
									}
								});
					}
				});

		menuSmsBalance.addSelectionListener(new SelectionListener<MenuEvent>() {

			@Override
			public void componentSelected(MenuEvent ce) {
				tabItem = hMap.get("SMS Balance Report");
				if (tabItem == null) {
					tabItem = new TabItem("SMS Balance Report");
					tabItem.setIcon(ImagesBundle.Util.get("ICON-sms"));

					tabItem.add(new SmsBalanceRpt());
					tabItem.setScrollMode(Scroll.AUTO);
					tabItem.setClosable(true);
					tabPanel.add(tabItem);
					tabPanel.setSelection(tabItem);
					hMap.put("SMS Balance Report", tabItem);
				} else
					tabPanel.setSelection(tabItem);
				tabItem.addListener(Events.Close, new Listener<BaseEvent>() {
					public void handleEvent(BaseEvent be) {
						tabItem = hMap.put("SMS Balance Report", null);
					}
				});
			}
		});
		menuAllCompanysDetails
				.addSelectionListener(new SelectionListener<MenuEvent>() {

					@Override
					public void componentSelected(MenuEvent ce) {
						tabItem = hMap.get("All Companys Details");
						if (tabItem == null) {
							tabItem = new TabItem("All Companys Details");
							tabItem.setIcon(ImagesBundle.Util
									.get("ICON-allcompany"));
							tabItem.add(new AllCompanysDetails());
							tabItem.setScrollMode(Scroll.AUTO);
							tabItem.setClosable(true);
							tabPanel.add(tabItem);
							tabPanel.setSelection(tabItem);
							hMap.put("All Companys Details", tabItem);
						} else
							tabPanel.setSelection(tabItem);
						tabItem.addListener(Events.Close,
								new Listener<BaseEvent>() {
									public void handleEvent(BaseEvent be) {
										tabItem = hMap.put(
												"All Companys Details", null);
									}
								});
					}
				});
	}
}