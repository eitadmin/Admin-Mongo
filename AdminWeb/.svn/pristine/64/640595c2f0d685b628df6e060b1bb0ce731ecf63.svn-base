package com.eiw.client.adminportal;

import com.eit.dcframework.client.DisplayConfigController;
import com.eiw.client.dashboard.LoginDashboardModule;
import com.eiw.client.gxtmodel.DisplayProcessor;
import com.eiw.client.gxtmodel.Maintenance;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;

public class NoTransmisRpt extends DisplayProcessor {
	VerticalPanel verticalPanel = new VerticalPanel();
	FlexTable searchFields;
	ComboBox<Maintenance> cbDay;
	Button btnSearch;
	Label lblDay;

	public NoTransmisRpt() {
		searchFields = new FlexTable();
		lblDay = new Label("Day Intervals");
		btnSearch = new Button("Search");
		cbDay = new ComboBox<Maintenance>();
		cbDay.setDisplayField("day");
		cbDay.setStore(new ListStore<Maintenance>());
		cbDay.setEmptyText("Enter day intervals");
		loadDays();

		searchFields.setWidget(0, 0, lblDay);
		searchFields.setWidget(0, 3, cbDay);
		searchFields.setWidget(0, 6, btnSearch);

		verticalPanel.add(searchFields);
		verticalPanel.add(new HTML("<div id = 'notransrptgridtable'></div>"));
		initComponent(verticalPanel);

		searchFields();
	}

	private void searchFields() {
		btnSearch.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {

				if (cbDay.getRawValue() != null) {

					String whereCond = "WHERE ve.lastTransmissionDate < DATE_SUB( NOW( ) , INTERVAL "
							+ cbDay.getRawValue()
							+ " DAY ) AND comp.suffix = '"
							+ LoginDashboardModule.suffix + "' ORDER BY comp.companyId";
					verticalPanel.add(new DisplayConfigController("notransrpt",
							whereCond, "report", "no", "notransrpt"));
				} else {
					MessageBox.info("Mandatory", "Day interval is mandatory",
							null);
				}
			}
		});
	}

	private void loadDays() {
		cbDay.getStore().removeAll();
		for (int i = 1; i <= 30; i++) {
			Maintenance models = new Maintenance();
			models.set("day", i);
			cbDay.getStore().add(models);
		}
	}
}