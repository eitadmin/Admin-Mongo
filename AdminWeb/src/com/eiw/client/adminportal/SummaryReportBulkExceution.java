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
import com.extjs.gxt.ui.client.widget.MessageBox;
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

public class SummaryReportBulkExceution extends VerticalPanel {

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
	String dateArray[] = { "2010-12-11", "2010-12-12" };
	DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd");
	MessageBox box;

	public SummaryReportBulkExceution(VerticalPanel bodyPanel1,
			final String companyId1, final String branchId1) {
		formPanel.setHeading("");
		formPanel.setHeaderVisible(false);
		formPanel.setFrame(true);
		formPanel.setBorders(true);
		formPanel.setAutoWidth(true);
		formPanel.setAutoHeight(true);
		fieldSet.setLayout(formLayout);
		fieldSet.setHeading("Bulk Summary Report Execution");

		fromDate.setFieldLabel("From");
		fromDate.setMaxValue(new Date());
		fromDate.setAllowBlank(false);
		fromDate.setWidth("100");
		toDate.setFieldLabel("To");
		toDate.setAllowBlank(false);
		toDate.setMaxValue(new Date());
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
							listStore.add(new ModelCompanyData(companyList.get(
									i).getCompanyName(), companyList.get(i)
									.getCompanyId()));
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
		bodyPanel1.add(formPanel);
		btnClear.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {

				comboBox.clear();
				fromDate.clear();
				toDate.clear();

			}
		});

		btnRun.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				LoginDashboardModule.box1.setVisible(true);
				fleetMgmtService.vehicleSummaryBulkExec(
						dtf.format(fromDate.getValue()),
						dtf.format(toDate.getValue()), comboBox.getRawValue(),
						new AsyncCallback<String>() {

							@Override
							public void onSuccess(String arg0) {
								if (box != null) {
									box.close();
								}
								LoginDashboardModule.box1.setVisible(false);
								MessageBox
										.alert("Bulk Report",
												"Bulk Execution Completed Successfully",
												null);

							}

							@Override
							public void onFailure(Throwable arg0) {
								// TODO Auto-generated method stub

							}
						});
			}
		});
	}
}
