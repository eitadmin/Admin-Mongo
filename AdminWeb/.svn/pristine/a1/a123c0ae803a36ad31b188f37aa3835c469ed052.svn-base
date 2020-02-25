package com.eiw.client.adminportal;

import java.util.Date;
import java.util.List;

import com.eiw.client.AdminPortalService;
import com.eiw.client.AdminPortalServiceAsync;
import com.eiw.client.dashboard.LoginDashboardModule;
import com.eiw.client.dto.CompanyDataAdmin;
import com.eiw.client.gxtmodel.ModelCompanyData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.CalendarUtil;

public class VehicleEventRestore extends VerticalPanel {

	private AdminPortalServiceAsync fleetMgmtService = GWT
			.create(AdminPortalService.class);
	Button btnRun = new Button("Run");
	Button btnClear = new Button("Clear");
	DateField fromDate = new DateField();
	DateField toDate = new DateField();
	FormPanel formPanel = new FormPanel();
	FieldSet fieldSet = new FieldSet();
	FormLayout formLayout = new FormLayout();
	ComboBox<ModelCompanyData> comboBox = new ComboBox<ModelCompanyData>();
	ListStore<ModelCompanyData> listStore = new ListStore<ModelCompanyData>();
	DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd");
	Date date1 = new Date();

	public VehicleEventRestore() {
		CalendarUtil.addMonthsToDate(date1, -3);
		formPanel.setHeading("");
		formPanel.setHeaderVisible(false);
		formPanel.setFrame(true);
		formPanel.setBorders(true);
		formPanel.setAutoWidth(true);
		formPanel.setAutoHeight(true);
		fieldSet.setLayout(formLayout);
		fieldSet.setHeading("Vehicle Event Restore");

		fromDate.setFieldLabel("From");
		fromDate.setMaxValue(date1);
		fromDate.setAllowBlank(false);
		fromDate.setWidth("100");
		fromDate.setValue(date1);
		toDate.setValue(date1);
		toDate.setFieldLabel("To");
		toDate.setAllowBlank(false);
		toDate.setMaxValue(date1);
		toDate.setWidth("100");

		fieldSet.add(fromDate);
		fieldSet.add(toDate);

		// get company details
		comboBox.setDisplayField("feature");
		comboBox.setFieldLabel("Company");
		comboBox.setEditable(false);
		comboBox.setEmptyText("Select the Company");
		comboBox.setTypeAhead(true);
		comboBox.setAllowBlank(false);
		comboBox.setTriggerAction(TriggerAction.ALL);
		comboBox.setWidth(100);
		fleetMgmtService.getCompanyNames(LoginDashboardModule.suffix,
				new AsyncCallback<List<CompanyDataAdmin>>() {

					@Override
					public void onSuccess(List<CompanyDataAdmin> companyList) {
						for (int i = 0; i < companyList.size(); i++) {
							if (!companyList.get(i).getCompanyName()
									.equalsIgnoreCase("LTMS"))
								listStore.add(new ModelCompanyData(companyList
										.get(i).getCompanyName(), companyList
										.get(i).getCompanyId()));
						}
					}

					@Override
					public void onFailure(Throwable arg0) {

					}
				});
		comboBox.setStore(listStore);

		FormButtonBinding binding = new FormButtonBinding(formPanel);
		binding.addButton(btnRun);
		// binding.addButton(btnClear);

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.add(btnRun);
		horizontalPanel.add(btnClear);
		formPanel.add(comboBox);
		formPanel.add(fieldSet);
		// formPanel.add(btnRun);
		formPanel.add(horizontalPanel);
		add(formPanel);
		btnClear.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {

				comboBox.clear();
				fromDate.setValue(date1);
				toDate.setValue(date1);

			}
		});

		btnRun.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				// LoginDashboardModule.box1.setVisible(true);
			}
		});
	}
}
