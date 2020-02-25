package com.eiw.client.gxtmodel;

import com.eit.dcframework.client.DisplayConfigController;
import com.extjs.gxt.ui.client.widget.Composite;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.user.client.ui.Widget;

public abstract class DisplayProcessor extends Composite {

	protected String featureName;

	protected Button btnClose = new Button("Close");

	public void editify(String colValues, String reportName, String formName) {
	}

	public void viewify() {
	}

	public void deletify(String colValues, String reportName) {
	}

	public void cloneView() {
	}

	public void dtoToForm(Object dtoObj, String docNo) {
	}

	public Object formToDto(boolean type, boolean isClone) {
		return null;
	}

	public void loadJsForm() {
	}

	public DisplayConfigController showGrid() {
		return null;
	}

	// public void close() {
	// TabItem tabItem = null;
	// tabItem = HeaderPanel.hMap.put(featureName, null);
	// if (tabItem != null) {
	// tabItem.close();
	// }
	// }

	public void reloadChangeableData() {

	}

	public void process(String colValues, String mode, String reportName,
			String formName, String tab) {

		if ("add".equalsIgnoreCase(mode)) {
			tab(formName, this, mode);
		} else if ("edit".equalsIgnoreCase(mode)
				|| "clone".equalsIgnoreCase(mode)) {
			this.editify(colValues, reportName, formName);
			// if (tab.equalsIgnoreCase("yes")) {
			// tab(formName, this, mode);
			// }
		} else if ("view".equalsIgnoreCase(mode)) {
			this.editify(colValues, reportName, formName);
			// if (tab.equalsIgnoreCase("yes")) {
			// tab(formName, this, mode);
			// }
		} else if ("delete".equalsIgnoreCase(mode)) {
			this.editify(colValues, reportName, formName);
			this.deletify(colValues, reportName);
			// if (tab.equalsIgnoreCase("yes")) {
			// tab(formName, this, mode);
			// }
		}

	}

	public void tab(final String tabName, final Widget widget, String mode) {
		// MenuItem menuItem = null;
		// String hMapTitle = null;
		// TabPanel tabPanel = new TabPanel();
		// TabItem tabItem = new TabItem();
		// MenuItem menuItem = (MenuItem) ce.getItem();
		// hMapTitle = menuItem.getText();
		// tabItem = hMap.get(hMapTitle);
		// if (hMap.get(hMapTitle) == null) {
		// tabItem = new TabItem(hMapTitle);
		// try {
		// if (hMapTitle.equalsIgnoreCase(constants
		// .Vehicle_Driver_Matching())) {
		// getTabItem(hMapTitle);
		// return;
		// } else if (hMapTitle.equalsIgnoreCase(constants
		// .Full_Tracking())) {
		// getTabItem(hMapTitle);
		// return;
		// } else
		// tabItem.add((Widget) getTabItem(hMapTitle));
		// } catch (Exception e) {
		// System.out.println(e);
		// return;
		// }
		// tabItem.setIcon(menuItem.getIcon());
		// tabItem.setScrollMode(Scroll.AUTO);
		// tabItem.setClosable(true);
		// tabPanel.add(tabItem);
		// tabPanel.setSelection(tabItem);
		// hMap.put(hMapTitle, tabItem);
		// } else
		// tabPanel.setSelection(tabItem);
		// tabItem.addListener(Events.Close, new Listener<BaseEvent>() {
		// public void handleEvent(BaseEvent be) {
		// tabItem = hMap.put(hMapTitle, null);
		// }
		// });
	}

	public static DisplayProcessor processDisplay(String featureName,
			String mode, String values) {

//		if (featureName.equalsIgnoreCase("Query Configuration")) {
//			return new QueryConfigForm(mode);
//		} else if (featureName.equalsIgnoreCase("Display Configuration")) {
//			return new DisplayMain(mode);
//		} else if (featureName.equalsIgnoreCase("Datasource Configuration")) {
//			return new DataSourceConfigForm(mode);
//		}
		return processOtherDisplays(featureName, mode, values);
	}

	protected static DisplayProcessor processOtherDisplays(String featureName,
			String mode, String values) {
		// TODO Auto-generated method stub
		return null;
	}
}