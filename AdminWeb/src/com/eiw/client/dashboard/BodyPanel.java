package com.eiw.client.dashboard;

import com.eiw.client.icons.ImagesBundle;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BodyPanel extends ContentPanel {
	// ScrollPanel scrollPanel = new ScrollPanel();
	public final static VerticalPanel verticalPanel = new VerticalPanel();
	public final static int bodyWidth = Window.getClientWidth() - 225;
	public final static int bodyHeight = Window.getClientHeight() - 190;
	public final static String heading = "Live Tracking";
	public final static String icon = "ICON-livetracking1";

	// static String bodyHeader = "";
	public BodyPanel(String userName, String compName, String brnchName,
			String compRole) {

		// RoundedPanel roundedPanel = new RoundedPanel(RoundedPanel.ALL, 9);
		verticalPanel.setHeight(BodyPanel.bodyHeight + "");
		// verticalPanel.setStyleName("roundBorder");
		verticalPanel.setWidth(bodyWidth + "");
		// setHeight(bodyHeight+"");
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		// roundedPanel.add(verticalPanel);
		/*
		 * scrollPanel.add(verticalPanel);
		 * scrollPanel.setAlwaysShowScrollBars(true); add(scrollPanel);
		 */
		setHeadindAndIcon(this);

		add(verticalPanel);
		setScrollMode(Scroll.AUTO);
	}

	static void setHeadindAndIcon(ContentPanel cp) {
		cp.setHeading(heading);
		cp.setIcon(ImagesBundle.Util.get(icon));
	}
}
