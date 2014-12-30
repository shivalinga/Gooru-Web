package org.ednovo.gooru.client.mvp.analytics.collectionSummaryIndividual;

import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.faq.TermsOfUse;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderZIndexEvent;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class EmailPopup extends PopupPanel {
	private static EmailPopupUiBinder uiBinder = GWT
			.create(EmailPopupUiBinder.class);

	interface EmailPopupUiBinder extends UiBinder<Widget, EmailPopup> {
	}
	private static MessageProperties i18n = GWT.create(MessageProperties.class);
	
	@UiField HTMLPanel successMesageContainer,mainBodycontainer;
	@UiField Button okBtn,sendBtn,cancelBtn;
	@UiField Label lblTankYou,lblFrom,lblTo,lblWeNeverShareText,lblSendMeCopy,lblSubject,lblMessageText,emailIdsText,headerTitlelbl,emailErrorlbl,userNamelbl,displayPdfPathlbl;
	@UiField TextArea messageTextArea;
	@UiField TextBox subjectTxt,emailTextbox;
	@UiField CheckBox sendmecopy;
	@UiField Anchor privacylbl;
	boolean isValidate=true;
	String fileName,filePath;
	
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	CollectionSummaryIndividualCBundle res;
	
	/**
	 * Constructor
	 */
	public EmailPopup() {
		this.res = CollectionSummaryIndividualCBundle.INSTANCE;
		res.css().ensureInjected();
		setWidget(uiBinder.createAndBindUi(this));
		this.setGlassEnabled(true);
		this.setStyleName(res.css().setEmailGlassStyleName());
		this.setGlassStyleName(res.css().setEmailPopupCenter());
		this.center();
		this.hide();
		displayPdfPathlbl.setText("Adding Attachment....");
		sendBtn.setEnabled(false);
		emailTextbox.getElement().setAttribute("placeholder","Separate email addresses with a comma or semicolon");
		emailErrorlbl.setVisible(false);
		successMesageContainer.setVisible(false);
		mainBodycontainer.setVisible(true);
		setStaticData();
	}
	/**
	 * This method is used to set the static text and id's
	 */
	void setStaticData(){
		StringUtil.setAttributes(successMesageContainer.getElement(), "pnlSuccessMesageContainer", null, null);
		StringUtil.setAttributes(mainBodycontainer.getElement(), "pnlMainBodycontainer", null, null);
		
		StringUtil.setAttributes(okBtn.getElement(), "btnOk", i18n.GL0190(), i18n.GL0190());
		StringUtil.setAttributes(sendBtn.getElement(), "btnSend", i18n.GL0228(), i18n.GL0228());
		StringUtil.setAttributes(cancelBtn.getElement(), "btnCancelBtn", i18n.GL0142(), i18n.GL0142());
		
		StringUtil.setAttributes(messageTextArea.getElement(), "tatMessageTextArea", null, null);
		
		StringUtil.setAttributes(subjectTxt.getElement(), "txtSubjectTxt", i18n.GL1443(), i18n.GL1443());
		StringUtil.setAttributes(emailTextbox.getElement(), "txtEmailTextbox", null, null);
		
		StringUtil.setAttributes(sendmecopy.getElement(), "chkSendmecopy", null, null);
			
		StringUtil.setAttributes(privacylbl.getElement(), "lnkPrivacylbl",i18n.GL1893(), i18n.GL1893());

		StringUtil.setAttributes(emailIdsText.getElement(), "lblEmailIdsText", null, null);
		StringUtil.setAttributes(headerTitlelbl.getElement(), "lblHeaderTitle", null, null);
		StringUtil.setAttributes(emailErrorlbl.getElement(), "lblEmailError", null, null);
		StringUtil.setAttributes(userNamelbl.getElement(), "lblUserName", null, null);
		StringUtil.setAttributes(displayPdfPathlbl.getElement(), "lblDisplayPdfPathlbl", null, null);
		StringUtil.setAttributes(lblTankYou.getElement(), "lblTankYou", i18n.GL0648(), i18n.GL0648());
		StringUtil.setAttributes(lblFrom.getElement(), "lblFrom", i18n.GL0223(), i18n.GL0223());
		StringUtil.setAttributes(lblTo.getElement(), "lblTo", i18n.GL0224(), i18n.GL0224());
		StringUtil.setAttributes(lblWeNeverShareText.getElement(), "lblWeNeverShareText", i18n.GL1892(), i18n.GL1892());
		StringUtil.setAttributes(lblSendMeCopy.getElement(), "lblSendMeCopy", i18n.GL0225(), i18n.GL0225());
		StringUtil.setAttributes(lblSubject.getElement(), "lblSubject", i18n.GL0226(), i18n.GL0226());
		StringUtil.setAttributes(lblMessageText.getElement(), "lblMessageText", i18n.GL0227(), i18n.GL0227());
	}
	/**
	 * This method will set the eamilpopup data
	 * @param pdfName
	 * @param path
	 */
	public void setEmailData(String pdfName,String path){
		successMesageContainer.setVisible(false);
		mainBodycontainer.setVisible(true);
		pdfName=pdfName.replaceAll(" ", "_");
		pdfName = pdfName + "_Collection_Summary.pdf";
		displayPdfPathlbl.setText(pdfName);
		fileName=pdfName;
		filePath=path;
		sendBtn.setEnabled(true);
		headerTitlelbl.setText(i18n.GL1449());
	}
	/**
	 * This method will set the user name
	 */
	public void setData(){
		userNamelbl.setText(AppClientFactory.getLoggedInUser().getUsernameDisplay());
	}
	/**
	 * This will handle the cancel event
	 * @param e
	 */
	@UiHandler("cancelBtn")
	public void onClickOfCancelButton(ClickEvent e){
		this.hide();
	}
	/**
	 * This will handle the click event on ok button
	 * @param e
	 */
	@UiHandler("okBtn")
	public void onClickOfOKButton(ClickEvent e){
		this.hide();
	}
	
	/**
	 * This will handle the click event on privacy text
	 * @param e
	 */
	@UiHandler("privacylbl")
	public void onClickOfprivacylblButton(ClickEvent e){
		TermsOfUse termsOfUse=new TermsOfUse() {
			@Override
			public void openParentPopup() {
				Window.enableScrolling(false);
				AppClientFactory.fireEvent(new SetHeaderZIndexEvent(98,false));
			}
		};
		termsOfUse.show();
		termsOfUse.setSize("902px", "300px");
		termsOfUse.center();
		termsOfUse.getElement().getStyle().setZIndex(99999);
	}
	
	/**
	 * This will handle the click event on the send button
	 * @param e
	 */
	@UiHandler("sendBtn")
	public void onClickOfSendButton(ClickEvent e){
		isValidate=true;
		isValidate=checkValidataions();
		if(isValidate){
			String emailIds=emailTextbox.getText();
			if(sendmecopy.isChecked()){
				emailIds=emailIds+","+AppClientFactory.getLoggedInUser().getEmailId();
			}
			emailIdsText.setText(emailIds);
			AppClientFactory.getInjector().getAnalyticsService().sendEmail(emailIds, subjectTxt.getText(), messageTextArea.getText(), userNamelbl.getText(), fileName, filePath, new AsyncCallback<Void>() {
				@Override
				public void onSuccess(Void result) {
					headerTitlelbl.setText(i18n.GL2295());
					successMesageContainer.setVisible(true);
					mainBodycontainer.setVisible(false);
				}
				@Override
				public void onFailure(Throwable caught) {
				}
			});
		}
	}
	/**
	 * This method will check the validations
	 * @return
	 */
	boolean checkValidataions(){
		String emailIds=emailTextbox.getText().trim();
		if(emailIds.isEmpty()){
			isValidate=false;
			emailErrorlbl.setText(i18n.GL0216());
			emailErrorlbl.setVisible(true);
		}
		String[] getAllEmailAddress = emailIds.split(",");
		for ( int i = 0; i < getAllEmailAddress.length; i++) {
			String getMailId = getAllEmailAddress[i].trim();
			boolean isEmail = validateEmail(getMailId);
			if (!isEmail) {
				isValidate=false;
				emailErrorlbl.setText(i18n.GL1027());
				emailErrorlbl.setVisible(true);
			}
		}
		return isValidate;
	}
	/**
	 * This method will check the email validation
	 * @param email
	 * @return
	 */
	boolean validateEmail(String email) {
		 boolean result = email.matches(EMAIL_PATTERN);
		 return result;
	}
}
