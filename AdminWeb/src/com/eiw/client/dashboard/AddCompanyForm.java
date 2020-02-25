package com.eiw.client.dashboard;

import java.util.HashMap;
import java.util.List;

import com.eiw.client.AdminPortalService;
import com.eiw.client.AdminPortalServiceAsync;
import com.eiw.client.dto.CompanyDataAdmin;
import com.eiw.client.fleetmgmt.DataReaderAdmin;
import com.eiw.client.gxtmodel.ModelCompanyData;
import com.eiw.client.icons.ImagesBundle;
import com.extjs.gxt.ui.client.Style.ButtonScale;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AddCompanyForm extends LayoutContainer {
	private AdminPortalServiceAsync fleetMgmtService = GWT
			.create(AdminPortalService.class);
	String companyId, mode;
	String branchId;
	String user;
	Dialog mainPopup;
	PagingLoader<PagingLoadResult<ModelData>> pagingLoader;
	ListStore<ModelCompanyData> storeCompany = new ListStore<ModelCompanyData>();
	Button addButton = new Button("OK");
	FormLayout layout1 = new FormLayout();
	FieldSet fieldSetLeft = new FieldSet();
	HashMap<String, String> country = new HashMap<String, String>();
	ModelCompanyData model;
	long addressId = 0;

	public AddCompanyForm(Dialog popup1, String compId, String brnchId,
			String userName1, String mode1, ModelCompanyData model) {

		this.companyId = compId;
		this.branchId = brnchId;
		this.user = userName1;
		this.mainPopup = popup1;
		this.mode = mode1;
		this.model = model;
		mainPopup.clearState();
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		fieldSetLeft.setLayout(layout1);
		fieldSetLeft.setHeading("Company Details");
		fieldSetLeft.setWidth("350");

		setLayout(new FlowLayout(1));
		HorizontalPanel hp = new HorizontalPanel();

		final FormPanel formPanel = new FormPanel();
		formPanel.setHeading("Add New Company");
		formPanel.setHeaderVisible(false);
		formPanel.setFrame(false);
		formPanel.setAutoWidth(true);
		formPanel.setAutoHeight(true);

		final TextField<String> txtCompanyId = new TextField<String>();
		txtCompanyId.setStyleName("TxtBox");
		txtCompanyId.setFieldLabel("Company Id");
		txtCompanyId.setAllowBlank(false);
		fieldSetLeft.add(txtCompanyId);

		final TextField<String> txtCompanyName = new TextField<String>();
		txtCompanyName.setStyleName("TxtBox");
		txtCompanyName.setFieldLabel("Company Name");
		txtCompanyName.setAllowBlank(false);
		fieldSetLeft.add(txtCompanyName);

		final TextField<String> txtAddressLine1 = new TextField<String>();
		txtAddressLine1.setStyleName("TxtBox");
		txtAddressLine1.setFieldLabel("Address Line 1");
		txtAddressLine1.setAllowBlank(false);
		fieldSetLeft.add(txtAddressLine1);

		final TextField<String> txtAddressLine2 = new TextField<String>();
		txtAddressLine2.setStyleName("TxtBox");
		txtAddressLine2.setFieldLabel("Address Line 2");
		txtAddressLine2.setAllowBlank(true);
		fieldSetLeft.add(txtAddressLine2);

		final TextField<String> txtCity = new TextField<String>();
		txtCity.setStyleName("TxtBox");
		txtCity.setFieldLabel("Address City");
		txtCity.setAllowBlank(false);
		fieldSetLeft.add(txtCity);

		final ComboBox<ModelCompanyData> comboCountry = new ComboBox<ModelCompanyData>();
		comboCountry.setFieldLabel("Country");
		comboCountry.setDisplayField("feature");
		comboCountry.setWidth(150);
		comboCountry.setStore(storeCompany);
		comboCountry.setTypeAhead(true);
		comboCountry.setForceSelection(true);
		comboCountry.setAllowBlank(false);
		comboCountry.setEditable(false);
		comboCountry.setTriggerAction(TriggerAction.ALL);
		fieldSetLeft.add(comboCountry);

		final ComboBox<ModelData> regionCountry = new ComboBox<ModelData>();
		ModelData m1 = new BaseModelData();
		m1.set("region", "Asia/Riyadh");
		ModelData m2 = new BaseModelData();
		m2.set("region", "Asia/Dubai");
		ModelData m3 = new BaseModelData();
		m3.set("region", "Asia/Kolkata");
		regionCountry.setStore(new ListStore<ModelData>());
		regionCountry.getStore().add(m1);
		regionCountry.getStore().add(m2);
		regionCountry.getStore().add(m3);
		regionCountry.setFieldLabel("Region");
		regionCountry.setDisplayField("region");
		regionCountry.setTriggerAction(TriggerAction.ALL);
		regionCountry.setEditable(false);
		regionCountry.setAllowBlank(false);
		fieldSetLeft.add(regionCountry);

		final TextField<String> txtLoginId = new TextField<String>();
		txtLoginId.setStyleName("TxtBox");
		txtLoginId.setFieldLabel("Login Id");
		txtLoginId.setAllowBlank(false);
		fieldSetLeft.add(txtLoginId);

		final TextField<String> txtPwd = new TextField<String>();
		txtPwd.setStyleName("TxtBox");
		txtPwd.setFieldLabel("Password");
		txtPwd.setAllowBlank(false);
		txtPwd.setPassword(true);
		fieldSetLeft.add(txtPwd);

		final TextField<String> txtSalesPerson = new TextField<String>();
		txtSalesPerson.setStyleName("TxtBox");
		txtSalesPerson.setFieldLabel("Sales Person");
		fieldSetLeft.add(txtSalesPerson);

		final TextArea taRemarks = new TextArea();
		taRemarks.setFieldLabel("Remarks");
		taRemarks.setAllowBlank(false);
		fieldSetLeft.add(taRemarks);

		CheckBoxGroup compType = new CheckBoxGroup();
		compType.setFieldLabel("Company Type");
		final CheckBox chBoxDemo = new CheckBox();
		chBoxDemo.setBoxLabel("Demo");
		compType.add(chBoxDemo);
		final CheckBox chBoxFollUp = new CheckBox();
		chBoxFollUp.setBoxLabel("Follow Up");
		compType.add(chBoxFollUp);
		final CheckBox chBoxLostDeal = new CheckBox();
		chBoxLostDeal.setBoxLabel("Lost Deal");
		compType.add(chBoxLostDeal);
		fieldSetLeft.add(compType);

		CheckBoxGroup reportTyp = new CheckBoxGroup();
		reportTyp.setFieldLabel("Report Type");
		final CheckBox chBoxNoTrans = new CheckBox();
		chBoxNoTrans.setBoxLabel("NoTransmissionReportSkip");
		reportTyp.add(chBoxNoTrans);
		fieldSetLeft.add(reportTyp);

		CheckBoxGroup appType = new CheckBoxGroup();
		appType.setFieldLabel("Application Type");
		final CheckBox chBoxMiniapps = new CheckBox();
		chBoxMiniapps.setBoxLabel("Mini Apps");
		appType.add(chBoxMiniapps);
		final CheckBox chBoxWftSSp = new CheckBox();
		chBoxWftSSp.setBoxLabel("WFT/SSP");
		appType.add(chBoxWftSSp);
		fieldSetLeft.add(appType);
		
		final TextField<String> mobileAppLink = new TextField<String>();
		mobileAppLink.setFieldLabel("Mobile App Shorter Link");
		fieldSetLeft.add(mobileAppLink);
		
		hp.setSpacing(3);
		hp.add(fieldSetLeft);
		hp.setAutoHeight(true);
		hp.setAutoWidth(true);

		formPanel.add(hp);
		formPanel.setAutoHeight(true);
		formPanel.setAutoWidth(true);

		// formPanel.setScrollMode(Scroll.AUTO);

		formPanel.addButton(addButton);
		addButton.setIcon(ImagesBundle.Util.get("ICONS-save"));
		addButton.setScale(ButtonScale.MEDIUM);

		Button cancelbutton = new Button("Cancel",
				new SelectionListener<ButtonEvent>() {
					public void componentSelected(ButtonEvent ce) {
						mainPopup.hide();
					}
				});
		cancelbutton.setIcon(ImagesBundle.Util.get("ICONS-remove"));
		cancelbutton.setScale(ButtonScale.MEDIUM);
		formPanel.addButton(cancelbutton);
		formPanel.setButtonAlign(HorizontalAlignment.CENTER);

		FormButtonBinding binding = new FormButtonBinding(formPanel);
		binding.addButton(addButton);
		formPanel.setFooter(false);
		add(formPanel);

		chBoxLostDeal.addListener(Events.OnClick, new Listener<BaseEvent>() {

			@Override
			public void handleEvent(BaseEvent be) {
				if (chBoxLostDeal.getValue()) {
					chBoxDemo.setValue(false);
					chBoxFollUp.setValue(false);
				} else {

				}

			}
		});

		// ChboxWtSSp ChkBox Listner
		chBoxWftSSp.addListener(Events.OnClick, new Listener<BaseEvent>() {

			@Override
			public void handleEvent(BaseEvent be) {
				if (chBoxWftSSp.getValue()) {
					chBoxMiniapps.setValue(false);
				}

			}
		});

		// Mini Apps ChkBox Listner
		chBoxMiniapps.addListener(Events.OnClick, new Listener<BaseEvent>() {

			@Override
			public void handleEvent(BaseEvent be) {
				if (chBoxMiniapps.getValue()) {
					chBoxWftSSp.setValue(false);
				}

			}
		});

		if (mode != null && mode.equalsIgnoreCase("new")) {
			txtCompanyId.addListener(Events.OnBlur, new Listener<BaseEvent>() {

				@Override
				public void handleEvent(BaseEvent be) {
					LoginDashboardModule.fileName = txtCompanyId.getValue();

					fleetMgmtService
							.validatecompanyId(new AsyncCallback<List<String>>() {

								@Override
								public void onSuccess(List<String> companyNames) {
									for (String ser : companyNames) {
										if (ser.equalsIgnoreCase(txtCompanyId
												.getValue())) {
											MessageBox.alert(
													"Register New Company",
													"Company Id " + ser
															+ " already exist",
													null);
											txtCompanyId.setValue("");
										}
									}
								}

								@Override
								public void onFailure(Throwable arg0) {

								}
							});

				}
			});

		}

		fleetMgmtService.getCountryList(companyId,
				new AsyncCallback<List<CompanyDataAdmin>>() {

					@Override
					public void onSuccess(List<CompanyDataAdmin> compList) {
						for (int i = 0; i < compList.size(); i++) {
							country.put(compList.get(i).getCountryName(),
									compList.get(i).getCountryCode());
						}
						storeCompany.add(DataReaderAdmin.getCountry(compList));
						if (mode != null && mode.equalsIgnoreCase("edit")) {
							if (model != null) {
								String country_Name = model.getCountryName();
								ModelData comboModel = comboCountry.getStore()
										.findModel("feature", country_Name);
								comboCountry
										.setValue((ModelCompanyData) comboModel);
							}
						}
					}

					@Override
					public void onFailure(Throwable arg0) {
					}
				});

		addButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				final CompanyDataAdmin companyDataAdmin = new CompanyDataAdmin();
				companyDataAdmin.setCompanyId(txtCompanyId.getValue());
				companyDataAdmin.setCompanyName(txtCompanyName.getValue());
				companyDataAdmin.setAddressLine1(txtAddressLine1.getValue());
				companyDataAdmin.setAddressLine2(txtAddressLine2.getValue());
				companyDataAdmin.setAddressCity(txtCity.getValue());
				companyDataAdmin.setCountryName(comboCountry.getRawValue());
				companyDataAdmin.setCountryCode(country.get(comboCountry
						.getRawValue()));
				companyDataAdmin.setUserName(txtLoginId.getValue());
				companyDataAdmin.setPwd(txtPwd.getValue());
				companyDataAdmin.setModule(regionCountry.getRawValue());
				companyDataAdmin.setSuffix(LoginDashboardModule.suffix);
				companyDataAdmin.setRemarks(taRemarks.getValue());
				companyDataAdmin.setSalesPerson(txtSalesPerson.getValue());
				if (chBoxDemo.getValue()) {
					companyDataAdmin.setIsdemo(true);
				} else {
					companyDataAdmin.setIsdemo(false);
				}
				if (chBoxFollUp.getValue()) {
					companyDataAdmin.setIsfollowup(true);
				} else {
					companyDataAdmin.setIsfollowup(false);
				}
				if (chBoxNoTrans.getValue()) {
					companyDataAdmin.setIsNoTransmissionSkip(true);
				} else {
					companyDataAdmin.setIsNoTransmissionSkip(false);
				}
				// Mini

				if (chBoxMiniapps.getValue()) {
					companyDataAdmin.setIsMiniApps(true);
				} else {
					companyDataAdmin.setIsMiniApps(false);
				}

				// iswft

				if (chBoxWftSSp.getValue()) {
					companyDataAdmin.setIsSspWft(true);
				} else {
					companyDataAdmin.setIsSspWft(false);
				}
				
				if(mobileAppLink.getValue() != null) {
					companyDataAdmin.setMobioleAppShortenLink(mobileAppLink.getValue().toString().trim());
				}
				
				// LostDeal

				if (chBoxLostDeal.getValue()) {
					companyDataAdmin.setIsLostDeal(true);
				} else {
					companyDataAdmin.setIsLostDeal(false);
				}

				if (mode != null && mode.equalsIgnoreCase("edit")) {
					companyDataAdmin.setAddressId(model.getAddressId());
				}

				if (mode != null && mode.equalsIgnoreCase("new")) {
					fleetMgmtService.addCompanyRegistration(companyDataAdmin,
							new AsyncCallback<String>() {

								@Override
								public void onSuccess(String status) {
									if (status.trim().equalsIgnoreCase(
											"persisted")) {
										mainPopup.hide();
										MessageBox
												.alert("Sucess",
														"New company details Added successfully",
														null);
										PagingCompanyDetailsGrid.gridRefresh();
										if (chBoxMiniapps.getValue()) {
											LoginDashboardModule.isMiniHashMap
													.put(txtCompanyId
															.getValue(), true);
										} else if (chBoxWftSSp.getValue()) {
											LoginDashboardModule.isWftSspHashMap
													.put(txtCompanyId
															.getValue(), true);
										}

									}

								}

								@Override
								public void onFailure(Throwable arg0) {
									// TODO Auto-generated method stub

								}
							});
				} else {
					fleetMgmtService.editCompanyRegistration(companyDataAdmin,
							new AsyncCallback<String>() {

								@Override
								public void onFailure(Throwable arg0) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onSuccess(String status) {
									if (status.trim().equalsIgnoreCase(
											"persisted")) {
										mainPopup.hide();
										addButton.setText("OK");
										MessageBox
												.alert("Sucess",
														"Company details Edited successfully",
														null);

										boolean blnExists = LoginDashboardModule.isMiniHashMap
												.containsKey(companyDataAdmin
														.getCompanyId());

										boolean blnExists1 = LoginDashboardModule.isWftSspHashMap
												.containsKey(companyDataAdmin
														.getCompanyId());

										if (!blnExists && !blnExists1) {

											if (companyDataAdmin
													.getIsMiniApps()) {
												LoginDashboardModule.isMiniHashMap
														.put(txtCompanyId
																.getValue(),
																true);
											} else if (companyDataAdmin
													.getIsSspWft()) {
												LoginDashboardModule.isWftSspHashMap
														.put(txtCompanyId
																.getValue(),
																true);
											} else {
												LoginDashboardModule.isMiniHashMap
														.remove(txtCompanyId
																.getValue());
												LoginDashboardModule.isWftSspHashMap
														.remove(txtCompanyId
																.getValue());

											}

										} else if (blnExists && !blnExists1) {

											if (companyDataAdmin
													.getIsMiniApps()) {
												LoginDashboardModule.isMiniHashMap
														.put(txtCompanyId
																.getValue(),
																true);
												LoginDashboardModule.isWftSspHashMap
														.remove(txtCompanyId
																.getValue());
											} else if (companyDataAdmin
													.getIsSspWft()) {
												LoginDashboardModule.isWftSspHashMap
														.put(txtCompanyId
																.getValue(),
																true);
												LoginDashboardModule.isMiniHashMap
														.remove(txtCompanyId
																.getValue());
											} else {
												LoginDashboardModule.isMiniHashMap
														.remove(txtCompanyId
																.getValue());
												LoginDashboardModule.isWftSspHashMap
														.remove(txtCompanyId
																.getValue());

											}

										} else if (!blnExists && blnExists1) {

											if (companyDataAdmin
													.getIsMiniApps()) {
												LoginDashboardModule.isMiniHashMap
														.put(txtCompanyId
																.getValue(),
																true);
												LoginDashboardModule.isWftSspHashMap
														.remove(txtCompanyId
																.getValue());
											} else if (companyDataAdmin
													.getIsSspWft()) {
												LoginDashboardModule.isWftSspHashMap
														.put(txtCompanyId
																.getValue(),
																true);
												LoginDashboardModule.isMiniHashMap
														.remove(txtCompanyId
																.getValue());
											} else {
												LoginDashboardModule.isMiniHashMap
														.remove(txtCompanyId
																.getValue());
												LoginDashboardModule.isWftSspHashMap
														.remove(txtCompanyId
																.getValue());

											}

										} else {

											if (!companyDataAdmin
													.getIsMiniApps()) {

												LoginDashboardModule.isMiniHashMap
														.remove(txtCompanyId
																.getValue());

											} else if (!companyDataAdmin
													.getIsSspWft()) {

												LoginDashboardModule.isWftSspHashMap
														.remove(txtCompanyId
																.getValue());

											}

										}

										PagingCompanyDetailsGrid.gridRefresh();
									}
								}
							});
				}
			}
		});

		if (mode != null && mode.equalsIgnoreCase("edit")) {
			if (model != null) {
				txtCompanyId.setValue(model.getCompanyId());
				txtCompanyId.setReadOnly(true);
				txtCompanyName.setValue(model.getCompanyName());
				txtCompanyName.setReadOnly(true);
				txtAddressLine1.setValue(model.getAddressLine1());

				txtAddressLine2.setValue(model.getAddressLine2());
				txtCity.setValue(model.getAddressCity());
				String region = model.getRegion();
				ModelData comboModel = regionCountry.getStore().findModel(
						"region", region);
				regionCountry.setValue((BaseModelData) comboModel);

				txtLoginId.setValue(model.getUserName());
				txtPwd.setValue(model.getPassword());
				taRemarks.setValue(model.getRemarks());
				chBoxDemo.setValue(model.getIsDemo());
				chBoxFollUp.setValue(model.getIsFollowup());
				chBoxNoTrans.setValue(model.getIsNoTransRptSkip());
				chBoxMiniapps.setValue(model.getIsMiniApps());
				txtSalesPerson.setValue(model.getSaleperson());
				chBoxLostDeal.setValue(model.getIsLostDeal());
				chBoxWftSSp.setValue(model.getIsSspWft());
				mobileAppLink.setValue(model.getshorterlink());
				addButton.setText("OK");
				addButton.setEnabled(true);
			}
		}
	}
}
