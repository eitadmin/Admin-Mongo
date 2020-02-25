package com.eiw.client.adminportal;

import com.eit.dcframework.client.DisplayConfigController;
import com.eiw.client.dashboard.LoginDashboardModule;
import com.eiw.client.gxtmodel.DisplayProcessor;
import com.extjs.gxt.ui.client.widget.VerticalPanel;

public class SmsBalanceRpt extends DisplayProcessor {
	VerticalPanel verticalPanel = new VerticalPanel();

	public SmsBalanceRpt() {
		String whereCond = " WHERE com.suffix = '" + LoginDashboardModule.suffix
				+ "' GROUP BY sms.companyId,sms.branchId";

		verticalPanel.add(new DisplayConfigController("smsbalancerpt",
				whereCond, "report", "no", "smsbalancerpt"));
		initComponent(verticalPanel);
	}
}