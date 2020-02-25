package com.eiw.client.dashboard;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;

public class MainPanel extends Viewport {

	public MainPanel(String userName, String compName, String brnchName,
			String compRole) {
		setLayout(new BorderLayout());
		BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH,
				80);
		BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
		northData.setMargins(new Margins(0, 0, 5, 0));
		centerData.setMargins(new Margins(0, 5, 5, 0));
		add(new HeaderPanel(), northData);
		add(LoginDashboardModule.bodyPanel, centerData);
		LoginDashboardModule.box1.setStyleName("processImage");
		LoginDashboardModule.box1.setVisible(false);
		add(LoginDashboardModule.box1);

	}
}
