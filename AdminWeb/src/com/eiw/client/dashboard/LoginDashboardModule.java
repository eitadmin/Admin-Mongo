package com.eiw.client.dashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.eiw.client.AdminPortalService;
import com.eiw.client.AdminPortalServiceAsync;
import com.eiw.client.LoginConstants;
import com.eiw.client.dto.DashboardContentPerCB;
import com.extjs.gxt.themes.client.Slate;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.util.Theme;
import com.extjs.gxt.ui.client.util.ThemeManager;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoginDashboardModule implements EntryPoint {
	LayoutPanel layoutPanel = new LayoutPanel();
	private final AdminPortalServiceAsync adminPortalService = GWT
			.create(AdminPortalService.class);
	private LoginConstants constants = GWT.create(LoginConstants.class);
	private LoginPanel loginPanel = new LoginPanel();
	private final FlexTable loginLayout = new FlexTable();
	public static String companyId;
	public static String branchId;
	public static String compName;
	public static String brnchName;
	public static String compRole;
	public static String userName;
	public static String imageUrl;
	public static String fileName;
	public static String contactNo;
	/*
	 * While moving to production make sure that u change bucketName as
	 * 'ltmsimages' and for static and local keep the bucketName as
	 * 'ltmsimageseit'
	 */
	public static String bucketName;
	public static String MARKER;
	public static String TRUCKIMAGE;
	public static String upload_File;
	public static String upload_FileName;

	public static boolean isSoundEnabled = false;
	public static boolean isAlertEnabled = false;
	public static DateTimeFormat defTimeZone = DateTimeFormat
			.getFormat("yyyy-MM-dd hh:mm:ss a");
	public static DateTimeFormat defDateZone = DateTimeFormat
			.getFormat("yyyy-MM-dd");
	public static DateTimeFormat defTimeStampZone = DateTimeFormat
			.getFormat("hh:mm:ss");
	public static Map<String, List<String>> compnayFeaturesList = new TreeMap<String, List<String>>();
	private MainPanel mainPanel;
	public static VerticalPanel bodyPanel = new VerticalPanel();
	public final static int width = Window.getClientWidth();
	public final static int height = Window.getClientHeight() - 90;
	public static String MAP_ZOOM_LEVEL = "";
	public static String MAP_STRT_LOC = "";
	Map<String, String> hmap = new HashMap<String, String>();
	public static HashMap<Integer, Integer> mapGeoFenceId = new HashMap<Integer, Integer>();
	public static String userImage;
	public static String loginId;
	static String lastLoginTime;
	public static Image box1 = new Image("img/loading12.gif");
	public static String suffix;
	public static String ourLogoUrl;
	public static List<String> adminDashboard = new ArrayList<String>();
	public static boolean isMiniApps;
	// get Mini Apps Companies
	public static Map<String, Boolean> isMiniHashMap;
	// get Wft/Ssp Companies
	public static Map<String, Boolean> isWftSspHashMap;

	public static Map<String, String> ProviderHashMap;

	public void onModuleLoad() {

		ThemeManager.register(Slate.SLATE);
		Theme themeYellow = new Theme("font", "font",
				"resources/css/theme_yellow.css");
		ThemeManager.register(new Theme("font", "font",
				"resources/css/theme_yellow.css"));
		Slate.SLATE.set("file", "resources/themes/slate/css/xtheme-slate.css");
		Theme.GRAY.set("file", "resources/css/gxt-gray.css");
		Theme.BLUE.set("file", "resources/css/gxt-all.css");
		GXT.setDefaultTheme(themeYellow, true);
		// preparin geofence map
		int value = 0;
		for (int cnt = 155; cnt < 175; cnt++) {
			value += 1;
			mapGeoFenceId.put(cnt, value);
		}
		bodyPanel.setSize("100%", "100%");
		layoutPanel.add(loginPanel);

		loginPanel.getCorpBox().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent arg0) {
				if (arg0.getNativeKeyCode() == 13) {
					// imgProgress.setVisible(true);
					checkadminlogin();
				}
			}
		});

		loginPanel.getUserBox().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent arg0) {
				if (arg0.getNativeKeyCode() == 13) {
					// imgProgress.setVisible(true);
					checkadminlogin();
				}
			}
		});

		loginPanel.getPwdBox().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent arg0) {
				if (arg0.getNativeKeyCode() == 13) {
					// imgProgress.setVisible(true);
					checkadminlogin();
				}
			}
		});

		loginPanel.getLoginButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				// imgProgress.setVisible(true);
				checkadminlogin();
			}
		});

		RootLayoutPanel rp = RootLayoutPanel.get();
		rp.add(layoutPanel);

		adminDashboard.add("Demo Companies");
		adminDashboard.add("Followup Companies");
		adminDashboard.add("SMS Balance Report");
		adminDashboard.add("Lost Deal");
		adminDashboard.add("Deleted Company Report");
		adminDashboard.add(" ");
	}

	protected void setStaticvariable(String providerBucketName) {
		bucketName = providerBucketName;
		MARKER = "https://" + providerBucketName
				+ ".s3.amazonaws.com/appsIcon/markers/";
		TRUCKIMAGE = "https://" + providerBucketName
				+ ".s3.amazonaws.com/appsIcon/vehicles/";
		upload_File = "https://" + providerBucketName + ".s3.amazonaws.com/";
	}

	public void checkadminlogin() {
		if (loginPanel.getCorpBox().getText().isEmpty()) {

			loginPanel.getLblError().setText(
					constants.Please_Enter_your_Corporate_Id());
			loginPanel.getLoginButton().setEnabled(true);
			return;
		} else if (loginPanel.getUserBox().getText().isEmpty()) {

			loginPanel.getLblError().setText(
					constants.Please_Enter_your_User_Id());
			loginPanel.getLoginButton().setEnabled(true);
			return;
		} else if (loginPanel.getPwdBox().getText().isEmpty()) {

			loginPanel.getLblError().setText(
					constants.Please_Enter_your_Password());
			loginPanel.getLoginButton().setEnabled(true);
			return;
		}
		loginPanel.getLoginButton().setEnabled(false);
		adminPortalService.authenticateUserAdmin(loginPanel.getCorpBox()
				.getText(), loginPanel.getUserBox().getText(), loginPanel
				.getPwdBox().getText(),
				new AsyncCallback<DashboardContentPerCB>() {
					public void onFailure(Throwable caught) {
						;
						loginPanel
								.getLblError()
								.setText(
										constants
												.Please_Check_your_Login_information());
						loginPanel.getLoginButton().setEnabled(true);
					}

					public void onSuccess(DashboardContentPerCB db) {
						if (db == null) {
							loginPanel
									.getLblError()
									.setText(
											constants
													.Please_Check_your_Login_information());
							loginPanel.getLoginButton().setEnabled(true);

							loginPanel.getLoginButton().setEnabled(true);
							return;
						}
						suffix = db.getSuffix();
						ourLogoUrl = db.getOurLogoUrl();
						companyId = db.getCompanyID();
						System.out.println("comp id first " + companyId);
						branchId = db.getBranchID();
						compName = db.getCompanyName();
						brnchName = db.getBranchName();
						compRole = db.getCompanyRole();
						userName = loginPanel.getUserBox().getText();
						contactNo = db.getContactNo();
						compnayFeaturesList = db.getCompFeatures();
						imageUrl = db.getCompanyLogoUrl();
						userImage = db.getUserImage();
						loginId = db.getUserLoginId();
						lastLoginTime = db.getLastLoginTime();
						System.out.println(compnayFeaturesList);
						setStaticvariable(db.getBucketName());

						isMiniApps = db.getIsMiniApps();
						isMiniHashMap = db.getIsMiniHashMap();
						isWftSspHashMap = db.getIsWftSspHashMap();
						ProviderHashMap = db.getProvider();

						hmap = db.getHashMap();
						if (hmap != null) {
							if (hmap.get("IMAGE") != null) {

							} else {

							}
							if (hmap.get("MAP_ZOOM_LEVEL") != null) {
								MAP_ZOOM_LEVEL = hmap.get("MAP_ZOOM_LEVEL");
							} else {
								MAP_ZOOM_LEVEL = String.valueOf(13);
							}
							MAP_STRT_LOC = hmap.get("MAP_STRT_LOC");
						} else {

							MAP_ZOOM_LEVEL = String.valueOf(13);
						}
						String userPrefName = null, userPrefValue = null;

						System.out.println("numberrr" + TRUCKIMAGE);
						System.out.println("numberrr" + MAP_ZOOM_LEVEL);
						System.out.println("numberrr" + MAP_STRT_LOC);
						System.out.println("numberrr" + contactNo);

						new WelcomeScreen(userName, companyId, branchId,
								compRole, userImage);
						mainPanel = new MainPanel(userName, companyId,
								branchId, compRole);
						loginPanel.setVisible(false);
						layoutPanel.add(mainPanel);

					}
				});
	}
}
