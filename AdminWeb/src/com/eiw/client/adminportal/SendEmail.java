package com.eiw.client.adminportal;

import com.eiw.client.AdminPortalService;
import com.eiw.client.AdminPortalServiceAsync;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SendEmail extends VerticalPanel {

	private AdminPortalServiceAsync fleetMgmtService = GWT
			.create(AdminPortalService.class);
	ContentPanel contentPanel = new ContentPanel();
	Label lblFrom = new Label("From");
	Label lblTo = new Label("To");
	Label lblSubject = new Label("Subject");
	Label lblMessage = new Label("Message");
	TextBox txtFrom = new TextBox();
	TextBox txtTo = new TextBox();
	TextBox txtSubject = new TextBox();
	TextArea txtMessage = new TextArea();
	FlexTable flexTable = new FlexTable();
	Button btnSend = new Button("Send");

	public SendEmail(VerticalPanel bodyPanel1, final String companyId1,
			final String branchId1) {
		contentPanel.setHeading("Send EMail");
		flexTable.setWidget(0, 0, lblFrom);
		flexTable.setWidget(0, 1, txtFrom);
		flexTable.setWidget(1, 0, lblTo);
		flexTable.setWidget(1, 1, txtTo);
		flexTable.setWidget(2, 0, lblSubject);
		flexTable.setWidget(2, 1, txtSubject);
		flexTable.setWidget(3, 0, lblMessage);
		flexTable.setWidget(3, 1, txtMessage);
		flexTable.setWidget(4, 0, btnSend);
		contentPanel.add(flexTable);
		bodyPanel1.add(contentPanel);

		btnSend.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				fleetMgmtService.sendEmail(txtFrom.getValue(),
						txtTo.getValue(), txtSubject.getValue(),
						txtMessage.getValue(), new AsyncCallback<String>() {

							@Override
							public void onSuccess(String arg0) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onFailure(Throwable arg0) {
								// TODO Auto-generated method stub

							}
						});

				// EmailSendHttpClient sendMail = new
				// EmailSendHttpClient(txtFrom
				// .getValue(), txtTo.getValue(), txtSubject.getValue(),
				// txtMessage.getValue());
				try {

					// sendMail.send();
				} catch (Exception e) {
					//
				}
			}
		});
	}

}
