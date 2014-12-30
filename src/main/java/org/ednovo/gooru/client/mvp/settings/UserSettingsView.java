/*******************************************************************************
 * Copyright 2013 Ednovo d/b/a Gooru. All rights reserved.
 * 
 *  http://www.goorulearning.org/
 * 
 *  Permission is hereby granted, free of charge, to any person obtaining
 *  a copy of this software and associated documentation files (the
 *  "Software"), to deal in the Software without restriction, including
 *  without limitation the rights to use, copy, modify, merge, publish,
 *  distribute, sublicense, and/or sell copies of the Software, and to
 *  permit persons to whom the Software is furnished to do so, subject to
 *  the following conditions:
 * 
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 * 
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 *  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 *  OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
package org.ednovo.gooru.client.mvp.settings;
/**


*
* @description : 
*
* @version :1.0
*
* @date: APR 19 2013
   	
* @Author Gooru Team
* 
* Reviewer Gooru Team
*
*/
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.effects.FadeInAndOut;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BaseViewWithHandlers;
import org.ednovo.gooru.client.mvp.home.ForgotPwdSuccessVc;
import org.ednovo.gooru.client.mvp.home.library.events.StandardPreferenceSettingEvent;
import org.ednovo.gooru.client.mvp.home.library.events.StandardPreferenceSettingHandler;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderZIndexEvent;
import org.ednovo.gooru.client.mvp.shelf.collection.CollectionCBundle;
import org.ednovo.gooru.client.uc.AlertContentUc;
import org.ednovo.gooru.client.uc.CloseLabelSetting;
import org.ednovo.gooru.client.uc.ErrorLabelUc;
import org.ednovo.gooru.client.uc.ProfileBiographyEditUC;
import org.ednovo.gooru.client.uc.ProfilePageGradeLabel;
import org.ednovo.gooru.client.uc.SettingEditLabelUc;
import org.ednovo.gooru.client.uc.SettingEmailEditLabelUc;
import org.ednovo.gooru.client.uc.SettingLastNameEditLabelUC;
import org.ednovo.gooru.client.ui.HTMLEventPanel;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.code.CodeDo;
import org.ednovo.gooru.shared.model.code.LibraryCodeDo;
import org.ednovo.gooru.shared.model.code.ProfileCodeDo;
import org.ednovo.gooru.shared.model.user.ProfileDo;
import org.ednovo.gooru.shared.model.user.SettingDo;
import org.ednovo.gooru.shared.model.user.UserDo;
import org.ednovo.gooru.shared.model.user.V2UserDo;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.tractionsoftware.gwt.user.client.ui.GroupedListBox;

public class UserSettingsView extends BaseViewWithHandlers<UserSettingsUiHandlers> implements IsUserSettingsView{
	private static UserSettingsViewUiBinder uiBinder = GWT
			.create(UserSettingsViewUiBinder.class);

	interface UserSettingsViewUiBinder extends
			UiBinder<Widget, UserSettingsView> {

	}
	

	boolean isChildAccount = false;
	
	//private final Widget widget;
	private static final String PROFILE_DEFAULT_IMAGE="./images/settings/setting-user-image.png";
	
	@UiField(provided = false)
	ProfileBiographyEditUC profileTextArea;
	
	@UiField(provided = true)
	SettingEditLabelUc tbFirstNameUcLabel;
	
	@UiField(provided = true)
	SettingLastNameEditLabelUC tbLastNameUcLabel;
	
	@UiField(provided = true)
	SettingEmailEditLabelUc lbEmail;
		
	@UiField HTMLEventPanel pencilTextAreaImage,plAccount,plSecurity,plEducation,plContact,profileDescriptionlabel,biographyCancelButton,panelStandards,panelDrive;
	@UiField HTMLPanel userAccount,userSecurity,userEducation,userContact,accountMiniusArrow,securityMiniusArrow,educationalMiniusArrow,contactMiniusArrow,standardsText,standardsSaveCancelButtonContainer,userStandardDefaultView,userStandardEditView,userStandardTextPanel,panelUserNameLabelContainer,panelGoogleDrive,
	standardsEditButtonContainer,standardsButtonContainer;
	@UiField Label aboutUsCharacterValidation,lbMale,lbFemale,lbOther,lbShare,lbRole,lbName,lbUserName,lbUName,forgetPassword,forgetPasswordMsg;
	@UiField HTMLPanel aboutUsContainer,profilePageText,aboutUsText,accountText,usernameText,nametext,genderText,securityText,settingsinfoText, panelHelp;
	//@UiField TextBox tbLastName,tbFirstName;
	@UiField Button settingsSaveButton,profileOnButton,profileOffButton, btnSave, btnSeeMyProfile,btnViewAdmin;
	@UiField HTMLPanel radioButtonContainer,settingsText,appearText,emailtext;
	@UiField UserSettingStyle Settings;
	@UiField Label courseLabel,courseMaxMsg,courseLbl,gradeLbl,SavingTextLabel,EduSavingTextLabel,lbMaleText,lbFemaleText,lbOtherText;
	@UiField FlowPanel KinderGarten,gradeTopList,gradeMiddleList,higherEducation,courseData,collectionCourseLstPanel,coursesPanel,collectionCourseDefaultLstPanel;
	@UiField Label uploadProfilImageButton,accountSavingTextLabel,notToShareText,gradeText;
	@UiField Label charLimitFNameLbl,emailTextConfirmation,lblgender,email,roleText;
	@UiField FocusPanel noAboutUsContainer;
	@UiField HTMLEventPanel profileImageContainer,userCoursePopup;
	@UiField Image uploadProfileImage;
	@UiField HTMLPanel editButtonContainerAccount,editButtonContainerEdu,editButtonContainerContact,buttonContainer,emailbuttonContainer,EduInfoButtonContainer,gradeContainer,DefaultGardeContainer,courseContainer, panelToolTipContent,panelTooltipContainer;
	@UiField Button editButtonAccount,editButtonEdu,editButtonContact,settingCancelButton,emailCancelButton,emailSaveButton,eduInfoCancelButton,eduInfoSaveButton,standardsSaveButton,standardsCancelButton,standardsEditButton, btnConnect;
	
	@UiField Label panelHeading, lblPleaseWait,lblCommonCore,lblCaliforniaScience,description,standardSavingTextLabel,lblTexas,lblUserMessage,lblNgss,lblImageSubHeading, lblHeading, lblSubHeading,lblDisconnect;
	
	@UiField HTML htmlToolTipDesc, htmlConnectedAs;
	@UiField TextBox txtUserName;
	@UiField ErrorLabelUc userNameValidationUc;
	@UiField HTMLPanel emailPanel;
	@UiField Label lblCSS,lblCaliforniaSocialSciencesStandards,lblCaliforniaELDS;
	
	boolean isValidUserName=false;
	boolean isAvailable = false;
	boolean isProfanityCleared=false;
	boolean isUserNameChanged = false;
	
	String USER_NAME_REGEX = "[A-Za-z0-9^]*";
	
	private static String BY_USERNAME = "username";
	private static String DISABLED = "disabled";
	
	private ProfileDo profileDo;
	private static String USER_META_ACTIVE_FLAG = "1";
//	private static String NONE_ADDED = i18n.GL1476;
	private GroupedListBox collectionCourseLst;
	HTML defaultCoursePanel;
	private SettingDo settingDo;
	private V2UserDo v2userDo;
	private AlertContentUc alertContentUc;
	
	private ForgotPwdSuccessVc forgotPwdSuccessVc;
	String gooruUid="";
	boolean enableEdit = false;
	boolean enableEditFirstName = false;
	
	CheckBox commonCoreChk = new CheckBox();
	CheckBox texasChk = new CheckBox();
	CheckBox ngssChk = new CheckBox();
	
	CheckBox californiaStandChk = new CheckBox();
	CheckBox CSSChk = new CheckBox();
	CheckBox CaliforniaSocialSciencesStandardsChk = new CheckBox();
	CheckBox CaliforniaELDSChk = new CheckBox();
	
	String USER_TAXONOMY_ROOT_CODE="user_taxonomy_root_code";
	List<String> userStandardPrefcode=new ArrayList<String>();
	
	boolean isDriveConnected = false;
	
	public MessageProperties i18n = GWT.create(MessageProperties.class);
	
	/** 
	 * This method is to get the settingDo
	 */
	public SettingDo getSettingDo() {
		return settingDo;
	}
 
	/** 
	 * This method is to set the settingDo
	 */
	public void setSettingDo(SettingDo settingDo) {
		this.settingDo = settingDo;
	}
	
	private String[] gendersArray=new String[]{"Male","Female","Others","Prefer not to share"};
	
	

	public interface Binder extends UiBinder<Widget, UserSettingsView> {
	}

	@Inject
	public UserSettingsView() {

		//widget = binder.createAndBindUi(this);
		tbLastNameUcLabel=new SettingLastNameEditLabelUC(){
			
			@Override
			public void onEditDisabled(String text) {
				enableEdit=true;
				//getUiHandlers().saveSettingsInformation();
			}
			
			public void checkCharacterLimit(String text) {
				//	titleAlertMessageLbl.setText(MessageProperties.i18n.GL0143);
					if (text.length() >= 25) {
						charLimitFNameLbl.setVisible(true);	
					} else {
						charLimitFNameLbl.setVisible(false);	
					}
					
				}
			
		};
		
		tbFirstNameUcLabel=new SettingEditLabelUc(){
			@Override
			public void onEditDisabled(String text) {
				enableEditFirstName = true;
		
			}
			public void checkCharacterLimit(String text) {
			//	titleAlertMessageLbl.setText(MessageProperties.i18n.GL0143);
				if (text.length() >= 25) {
					charLimitFNameLbl.setVisible(true);	
				} else {
					charLimitFNameLbl.setVisible(false);	
				}
				
			}
			
		};
		
		lbEmail=new SettingEmailEditLabelUc(){
			@Override
			public void onEditDisabled(String text) {
				editButtonContact.setVisible(false);
				emailbuttonContainer.setVisible(false);
				getUiHandlers().saveEmail(false);
				
			}
		
			
		};
		
		
		
		setWidget(uiBinder.createAndBindUi(this));
		CollectionCBundle.INSTANCE.css().ensureInjected();
		
		MixpanelUtil.Loading_SettingsPage();
		
		panelHeading.setText(i18n.GL2007());
		panelHeading.getElement().setId("lblPanelHeading");
		panelHeading.getElement().setAttribute("alt", i18n.GL2007());
		panelHeading.getElement().setAttribute("title", i18n.GL2007());
		panelHeading.getElement().getStyle().setMarginTop(-4, Unit.PX);
		panelStandards.getElement().setId("epnlPanelStandards");
		collectionCourseLstPanel.getElement().setId("fpnlCollectionCourseLstPanel");
		btnConnect.setText(i18n.GL2008());
		standardsButtonContainer.getElement().setId("pnlStandardsButtonContainer");
		standardsSaveCancelButtonContainer.getElement().setId("pnlStandardsSaveCancelButtonContainer");
		standardSavingTextLabel.getElement().setId("lblStandardSavingTextLabel");
		KinderGarten.getElement().setId("fpnlKinderGarten");
		gradeTopList.getElement().setId("fpnlGradeTopList");
		gradeMiddleList.getElement().setId("fpnlGradeMiddleList");
		btnConnect.getElement().setId("btnBtnConnect");
		btnConnect.getElement().setAttribute("alt", i18n.GL2008());
		btnConnect.getElement().setAttribute("title", i18n.GL2008());
		btnConnect.getElement().removeClassName("green");
		userStandardEditView.getElement().setId("pnlUserStandardEditView");
		userEducation.getElement().setId("pnlUserEducation");
		higherEducation.getElement().setId("fpnlHigherEducation");
		lbRole.getElement().setId("lblLbRole");
		userCoursePopup.getElement().setId("epnlUserCoursePopup");
		courseContainer.getElement().setId("pnlCourseContainer");
		lblImageSubHeading.setText(i18n.GL2009());
		lblImageSubHeading.getElement().setId("lblLblImageSubHeading");
		lblImageSubHeading.getElement().setAttribute("alt", i18n.GL2009());
		lblImageSubHeading.getElement().setAttribute("title", i18n.GL2009());
		lblHeading.setText(i18n.GL2009());
		lblHeading.getElement().setId("lblLblHeading");
		lblHeading.getElement().setAttribute("alt", i18n.GL2009());
		lblHeading.getElement().setAttribute("title", i18n.GL2009());
		lblSubHeading.setText(i18n.GL2010());
		lblSubHeading.getElement().setId("lblLblSubHeading");
		lblSubHeading.getElement().setAttribute("alt", i18n.GL2010());
		lblSubHeading.getElement().setAttribute("title", i18n.GL2010());
		
		
		htmlConnectedAs.setVisible(false);
		
		editButtonContainerEdu.getElement().setId("pnlEditButtonContainerEdu");
		lblDisconnect.setText(i18n.GL2011());
		lblDisconnect.getElement().setId("lblLblDisconnect");
		lblDisconnect.getElement().setAttribute("alt", i18n.GL2011());
		lblDisconnect.getElement().setAttribute("title", i18n.GL2011());
		gradeContainer.getElement().setId("pnlGradeContainer");
		lblDisconnect.setVisible(false); 
		btnConnect.setEnabled(true);
		EduInfoButtonContainer.getElement().setId("pnlEduInfoButtonContainer");
		plEducation.getElement().setId("epnlPlEducation");
		plSecurity.getElement().setId("epnlPlSecurity");
		courseData.getElement().setId("fpnlCourseData");
		courseData.getElement().getStyle().setWidth(324, Unit.PX);
		educationalMiniusArrow.getElement().setId("pnlEducationalMiniusArrow");
		settingsText.getElement().setInnerHTML(i18n.GL0192());
		settingsText.getElement().setId("pnlSettingsText");
		settingsText.getElement().setAttribute("alt", i18n.GL0192());
		settingsText.getElement().setAttribute("title", i18n.GL0192());
		uploadProfilImageButton.setText(i18n.GL0800());
		uploadProfilImageButton.getElement().setAttribute("alt", i18n.GL0800());
		uploadProfilImageButton.getElement().setAttribute("title", i18n.GL0800());
		profilePageText.getElement().setInnerHTML(i18n.GL0801());
		profilePageText.getElement().setId("pnlProfilePageText");
		profilePageText.getElement().setAttribute("alt", i18n.GL0801());
		profilePageText.getElement().setAttribute("title", i18n.GL0801());
		profileOnButton.setText(i18n.GL0802());
		profileOnButton.getElement().setId("btnProfileOnButton");
		profileOnButton.getElement().setAttribute("alt", i18n.GL0802());
		profileOnButton.getElement().setAttribute("title", i18n.GL0802());
		profileOffButton.setText(i18n.GL0803());
		profileOffButton.getElement().setId("btnProfileOffButton");
		profileOffButton.getElement().setAttribute("alt", i18n.GL0803());
		profileOffButton.getElement().setAttribute("title", i18n.GL0803());
		lbEmail.getElement().setId("lblLbEmail");
		userContact.getElement().setId("pnlUserContact");
		lbName.getElement().setId("lblLbName");
		lbUserName.getElement().setId("lblLbUserName");
		profileDescriptionlabel.getElement().setId("epnlProfileDescriptionlabel");
		accountMiniusArrow.getElement().setId("pnlAccountMiniusArrow");
		userAccount.getElement().setId("pnlUserAccount");
		userSecurity.getElement().setId("pnlUserSecurity");
		panelUserNameLabelContainer.getElement().setId("pnlPanelUserNameLabelContainer");
		lbUName.getElement().setId("lblLbUName");
		tbFirstNameUcLabel.getElement().setId("lblTbFirstNameUcLabel");
		tbLastNameUcLabel.getElement().setId("lblTbLastNameUcLabel");
		aboutUsText.getElement().setInnerHTML(i18n.GL0804());
		contactMiniusArrow.getElement().setId("pnlContactMiniusArrow");
		aboutUsText.getElement().setId("pnlAboutUsText");
		aboutUsText.getElement().setAttribute("alt", i18n.GL0804());
		aboutUsText.getElement().setAttribute("title", i18n.GL0804());
		appearText.getElement().setInnerHTML(i18n.GL0805());
		appearText.getElement().setId("pnlAppearText");
		appearText.getElement().setAttribute("alt", i18n.GL0805());
		appearText.getElement().setAttribute("title", i18n.GL0805());
		aboutUsCharacterValidation.setText(i18n.GL0143());
		plContact.getElement().setId("epnlPlContact");
		emailbuttonContainer.getElement().setId("pnlEmailbuttonContainer");
		aboutUsCharacterValidation.getElement().setId("lblAboutUsCharacterValidation");
		aboutUsCharacterValidation.getElement().setAttribute("alt", i18n.GL0143());
		aboutUsCharacterValidation.getElement().setAttribute("title", i18n.GL0143());
		editButtonContainerContact.getElement().setId("pnlEditButtonContainerContact");
		btnSave.setText(i18n.GL0141());
		btnSave.getElement().setId("BtnSave");
		btnSave.getElement().setAttribute("alt", i18n.GL0141());
		btnSave.getElement().setAttribute("title", i18n.GL0141());
		biographyCancelButton.getElement().setInnerHTML(i18n.GL0142());
		biographyCancelButton.getElement().setId("epnlBiographyCancelButton");
		biographyCancelButton.getElement().setAttribute("alt", i18n.GL0142());
		biographyCancelButton.getElement().setAttribute("title", i18n.GL0142());
		btnSave.getElement().setAttribute("alt", i18n.GL0141());
		btnSave.getElement().setAttribute("title", i18n.GL0141());
		btnSeeMyProfile.setText(i18n.GL0806());
		btnSeeMyProfile.getElement().setId("btnBtnSeeMyProfile");
		btnSeeMyProfile.getElement().setAttribute("alt", i18n.GL0806());
		btnSeeMyProfile.getElement().setAttribute("title", i18n.GL0806());
		accountText.getElement().setInnerHTML(i18n.GL0807());
		accountText.getElement().setId("pnlAccountText");
		accountText.getElement().setAttribute("alt", i18n.GL0807());
		accountText.getElement().setAttribute("title", i18n.GL0807());
		accountSavingTextLabel.setText(i18n.GL0808());
		accountSavingTextLabel.getElement().setId("lblAccountSavingTextLabel");
		accountSavingTextLabel.getElement().setAttribute("alt", i18n.GL0808());
		accountSavingTextLabel.getElement().setAttribute("title", i18n.GL0808());
		editButtonAccount.setText(i18n.GL0140());
		editButtonAccount.getElement().setAttribute("alt", i18n.GL0140());
		editButtonAccount.getElement().setAttribute("title", i18n.GL0140());
		settingCancelButton.setText(i18n.GL0142());
		settingCancelButton.getElement().setAttribute("alt", i18n.GL0142());
		settingCancelButton.getElement().setAttribute("title", i18n.GL0142());
		settingsSaveButton.setText(i18n.GL0141());
		settingsSaveButton.getElement().setAttribute("alt", i18n.GL0141());
		settingsSaveButton.getElement().setAttribute("title", i18n.GL0141());
		usernameText.getElement().setInnerHTML(i18n.GL0652());
		usernameText.getElement().setId("pnlUsernameText");
		usernameText.getElement().setAttribute("alt",i18n.GL0652());
		usernameText.getElement().setAttribute("title", i18n.GL0652());
		nametext.getElement().setInnerHTML(i18n.GL0649());
		nametext.getElement().setId("pnlNametext");
		nametext.getElement().setAttribute("alt",i18n.GL0649());
		nametext.getElement().setAttribute("title", i18n.GL0649());
		uploadProfileImage.setTitle(i18n.GL0823());
		uploadProfileImage.getElement().setId("imgUploadProfileImage");
		uploadProfileImage.getElement().setAttribute("alt", i18n.GL0823());
		uploadProfileImage.getElement().setAttribute("title", i18n.GL0823());
		uploadProfileImage.setAltText(i18n.GL0823());
		aboutUsContainer.getElement().setId("pnlAboutUsContainer");
		editButtonContainerAccount.getElement().setId("pnlEditButtonContainerAccount");
		securityMiniusArrow.getElement().setId("pnlSecurityMiniusArrow");
		//i18n.GL0823

		charLimitFNameLbl.setText(i18n.GL0143());
		charLimitFNameLbl.getElement().setId("lblCharLimitFNameLbl");
		charLimitFNameLbl.getElement().setAttribute("alt", i18n.GL0143());
		charLimitFNameLbl.getElement().setAttribute("title", i18n.GL0143());
		genderText.getElement().setInnerHTML(i18n.GL0809()+i18n.GL_SPL_SEMICOLON());
		genderText.getElement().setId("pnlGenderText");
		genderText.getElement().setAttribute("alt", i18n.GL0809());
		genderText.getElement().setAttribute("title", i18n.GL0809());
		lbMaleText.setText(i18n.GL0810());
		lbMaleText.getElement().setId("lblLbMaleText");
		lbMaleText.getElement().setAttribute("alt",i18n.GL0810());
		lbMaleText.getElement().setAttribute("title", i18n.GL0810());
		lbFemaleText.setText(i18n.GL0811());
		lbFemaleText.getElement().setId("LbFemaleText");
		lbFemaleText.getElement().setAttribute("alt",i18n.GL0811());
		lbFemaleText.getElement().setAttribute("title", i18n.GL0811());
		lbOtherText.setText(i18n.GL0419());
		lbOtherText.getElement().setId("lblLbOtherText");
		lbOtherText.getElement().setAttribute("alt",i18n.GL0419());
		lbOtherText.getElement().setAttribute("title", i18n.GL0419());
		notToShareText.setText(i18n.GL0812());
		notToShareText.getElement().setId("lblNotToShareText");
		notToShareText.getElement().setAttribute("alt",i18n.GL0812());
		notToShareText.getElement().setAttribute("title", i18n.GL0812());
		emailtext.getElement().setInnerHTML(i18n.GL0212());
		emailtext.getElement().setId("pnlEmailtext");
		emailtext.getElement().setAttribute("alt",i18n.GL0212());
		emailtext.getElement().setAttribute("title", i18n.GL0212());
		SavingTextLabel.setText(i18n.GL0808());
		SavingTextLabel.getElement().setId("lblSavingTextLabel");
		SavingTextLabel.getElement().setAttribute("alt",i18n.GL0808());
		SavingTextLabel.getElement().setAttribute("title", i18n.GL0808());
		editButtonContact.setText(i18n.GL0140());
		editButtonContact.getElement().setAttribute("alt",i18n.GL0140());
		editButtonContact.getElement().setAttribute("title", i18n.GL0140());
		emailCancelButton.setText(i18n.GL0142());
		emailCancelButton.getElement().setAttribute("alt",i18n.GL0142());
		emailCancelButton.getElement().setAttribute("title", i18n.GL0142());
		emailSaveButton.setText(i18n.GL0141());
		emailSaveButton.getElement().setAttribute("alt",i18n.GL0141());
		emailSaveButton.getElement().setAttribute("title", i18n.GL0141());
		email.setText(i18n.GL0212()+i18n.GL_SPL_SEMICOLON());
		email.getElement().setId("lblEmail");
		email.getElement().setAttribute("alt",i18n.GL0212());
		email.getElement().setAttribute("title", i18n.GL0212());
		emailTextConfirmation.setText(i18n.GL0813());
		emailTextConfirmation.getElement().setId("lblEmailTextConfirmation");
		panelGoogleDrive.getElement().setId("pnlPanelGoogleDrive");
		panelDrive.getElement().setId("epnlPanelDrive");
		emailTextConfirmation.getElement().setAttribute("alt",i18n.GL0813());
		emailTextConfirmation.getElement().setAttribute("title", i18n.GL0813());
		securityText.getElement().setInnerHTML(i18n.GL0814());
		securityText.getElement().setId("pnlSecurityText");
		securityText.getElement().setAttribute("alt",i18n.GL0814());
		securityText.getElement().setAttribute("title", i18n.GL0814());
		forgetPasswordMsg.setText(i18n.GL0815());
		forgetPasswordMsg.getElement().setId("lblForgetPasswordMsg");
		forgetPasswordMsg.getElement().setAttribute("alt",i18n.GL0815());
		forgetPasswordMsg.getElement().setAttribute("title", i18n.GL0815());
		forgetPassword.setText(" "+i18n.GL0816());
		forgetPassword.getElement().setAttribute("alt", i18n.GL0816());
		forgetPassword.getElement().setAttribute("title",i18n.GL0816());
		lblPleaseWait.setText(i18n.GL0339());
		lblPleaseWait.getElement().setId("lblLblPleaseWait");
		lblPleaseWait.getElement().setAttribute("alt",i18n.GL0339());
		lblPleaseWait.getElement().setAttribute("title", i18n.GL0339());
		settingsinfoText.getElement().setInnerHTML(i18n.GL0817());
		settingsinfoText.getElement().setId("pnlSettingsinfoText");
		settingsinfoText.getElement().setAttribute("alt",i18n.GL0817());
		settingsinfoText.getElement().setAttribute("title", i18n.GL0817());
		EduSavingTextLabel.setText(i18n.GL0808());
		EduSavingTextLabel.getElement().setId("lblEduSavingTextLabel");
		EduSavingTextLabel.getElement().setAttribute("alt",i18n.GL0808());
		EduSavingTextLabel.getElement().setAttribute("title", i18n.GL0808());
		editButtonEdu.setText(i18n.GL0140());
		editButtonEdu.getElement().setAttribute("alt",i18n.GL0140());
		editButtonEdu.getElement().setAttribute("title", i18n.GL0140());
		eduInfoCancelButton.setText(i18n.GL0142());
		eduInfoCancelButton.getElement().setAttribute("alt",i18n.GL0142());
		eduInfoCancelButton.getElement().setAttribute("title", i18n.GL0142());
		eduInfoSaveButton.setText(i18n.GL0141());
		eduInfoSaveButton.getElement().setAttribute("alt",i18n.GL0141());
		eduInfoSaveButton.getElement().setAttribute("title", i18n.GL0141());
		roleText.setText(" "+i18n.GL0818());
		roleText.getElement().setId("lblRoleText");
		roleText.getElement().setAttribute("alt",i18n.GL0818());
		roleText.getElement().setAttribute("title", i18n.GL0818());
		gradeText.setText(i18n.GL0819());
		gradeText.getElement().setId("lblGradeText");
		gradeText.getElement().setAttribute("alt",i18n.GL0819());
		gradeText.getElement().setAttribute("title", i18n.GL0819());
		gradeLbl.setText(i18n.GL0820());
		gradeLbl.getElement().setId("lblGradeLbl");
		gradeLbl.getElement().setAttribute("alt",i18n.GL0820());
		gradeLbl.getElement().setAttribute("title", i18n.GL0820());
		courseLabel.setText(i18n.GL0821());
		courseLabel.getElement().setId("lblCourseLabel");
		courseLabel.getElement().setAttribute("alt",i18n.GL0821());
		courseLabel.getElement().setAttribute("title", i18n.GL0821());
		courseLbl.setText(i18n.GL0820());
		courseLbl.getElement().setId("lblCourseLbl");
		courseLbl.getElement().setAttribute("alt",i18n.GL0820());
		courseLbl.getElement().setAttribute("title", i18n.GL0820());

		courseMaxMsg.setText(i18n.GL0822());
		courseMaxMsg.getElement().setId("lblCourseMaxMsg");
		courseMaxMsg.getElement().setAttribute("alt",i18n.GL0822());
		courseMaxMsg.getElement().setAttribute("title", i18n.GL0822());
		htmlToolTipDesc.setHTML(i18n.GL1539());
		htmlToolTipDesc.getElement().setId("htmlHtmlToolTipDesc");
		htmlToolTipDesc.getElement().setAttribute("alt", i18n.GL1539());
		htmlToolTipDesc.getElement().setAttribute("title", i18n.GL1539());
		plAccount.getElement().setId("epnlPlAccount");
		panelToolTipContent.getElement().setId("pnlPanelToolTipContent");
		panelToolTipContent.getElement().getStyle().setWidth(247, Unit.PX);
		panelTooltipContainer.getElement().setId("pnlPanelTooltipContainer");
		panelTooltipContainer.getElement().getStyle().setWidth(277, Unit.PX);
		panelTooltipContainer.getElement().getStyle().setLeft(-127, Unit.PX);
		userNameValidationUc.getElement().setId("errlblUserNameValidationUc");
		emailPanel.getElement().setId("pnlEmailPanel");
		emailPanel.setVisible(true);
		//i18n.GL0820
		//For 5.9 release
		editButtonAccount.addClickHandler(new onEditImageName());
		editButtonContact.addClickHandler(new onEditForEmail());
		lbShare.getElement().setId("rdShare");
		lbOther.getElement().setId("rdOther");
		lbMale.getElement().setId("rdMale");
		lbFemale.getElement().setId("rdFemale");
		forgetPassword.getElement().setId("lblForgetPassword");
		emailSaveButton.getElement().setId("btnSaveEmail");
		emailCancelButton.getElement().setId("btnCancelEmail");
		editButtonContact.getElement().setId("btnEditEmail");
		editButtonAccount.getElement().setId("btnEditAccount");
		settingCancelButton.getElement().setId("btnCancelAccount");
		settingsSaveButton.getElement().setId("btnSaveAccount");
		radioButtonContainer.getElement().setId("pnlRadioButtonContainer");
		editButtonEdu.getElement().setId("btnEdit");
		eduInfoCancelButton.getElement().setId("btnCancel");
		eduInfoSaveButton.getElement().setId("btnSave");
		editButtonEdu.addClickHandler(new onEducationInfo());
		emailbuttonContainer.setVisible(false);		
		charLimitFNameLbl.setVisible(false);
		SavingTextLabel.setVisible(false);
		emailTextConfirmation.setVisible(false);
		EduInfoButtonContainer.setVisible(false);
		gradeContainer.setVisible(false);
		courseContainer.setVisible(false);
		radioButtonContainer.setVisible(false);
		accountSavingTextLabel.setVisible(false);
		EduSavingTextLabel.setVisible(false);
		txtUserName.getElement().setId("txtTxtUserName");
		txtUserName.setVisible(false);
		txtUserName.getElement().getStyle().setMarginLeft(5, Unit.PX);
		panelHelp.getElement().setId("pnlPanelHelp");
		panelHelp.setVisible(false);
		txtUserName.getElement().setAttribute("maxlength", "20");
		StringUtil.setAttributes(txtUserName, true);
		//end
		getForgetPassword().setVisible(false);
		getForgetPasswordMsg().setVisible(false); 
		aboutUsCharacterValidation.setVisible(false);
		noAboutUsContainer.getElement().setId("focusPnlNoAboutUsContainer");
		noAboutUsContainer.setVisible(false);
		profileTextArea.getElement().setId("UCProfileTextArea");
		profileTextArea.getBiographyEditImage().setVisible(false);
		profileTextArea.getBiographyEditImage().getElement().setAttribute("style", "none");
		btnSave.setVisible(false);
		biographyCancelButton.setVisible(false);
		pencilTextAreaImage.getElement().setId("epnlPencilTextAreaImage");
		pencilTextAreaImage.setVisible(false);
		buttonContainer.getElement().setId("pnlButtonContainer");
		lblgender.getElement().setId("lblLblgender");
		buttonContainer.setVisible(false);
		uploadProfilImageButton.getElement().setId("lblUploadProfilImage");
		uploadProfilImageButton.addClickHandler(new UploadProfileImage());
		uploadProfileImage.addErrorHandler(new ProfileDefaultImage());
		profileImageContainer.getElement().setId("epnlProfileImageContainer");
		DefaultGardeContainer.getElement().setId("pnlDefaultGardeContainer");
		collectionCourseDefaultLstPanel.getElement().setId("fpnlCollectionCourseDefaultLstPanel");
		coursesPanel.getElement().setId("fpnlCoursesPanel");
		profileImageContainer.addMouseOverHandler(new ShowUploadImageButton());
		profileImageContainer.addMouseOutHandler(new HideUploadImageButton());
		aboutUsContainer.setVisible(false);
		coursesPanel.clear();
		
		lblPleaseWait.setVisible(false);
		
	//	txtUserName.addBlurHandler(new OnBlurHandler());
		txtUserName.addKeyUpHandler(new OnKeyUpHandler());
		clearErrorMessage();
		/*txtUserName.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				clearErrorMessage();
				if (txtUserName.getText().length() <4 || txtUserName.getText().length() >20){
					setErrorMessage(i18n.GL0473);
				}
			}
		});*/
		//added in 6.1
		standardsEditButton.setText(i18n.GL0140());
		standardsEditButton.getElement().setId("btnStandardsEditButton");
		standardsEditButton.getElement().setAttribute("alt",i18n.GL0140());
		standardsEditButton.getElement().setAttribute("title", i18n.GL0140());
		standardsSaveButton.setText(i18n.GL0141());
		standardsSaveButton.getElement().setId("btnStandardsSaveButton");
		standardsSaveButton.getElement().setAttribute("alt",i18n.GL0141());
		standardsSaveButton.getElement().setAttribute("title", i18n.GL0141());
		standardsCancelButton.setText(i18n.GL0142());
		standardsCancelButton.getElement().setId("btnStandardsCancelButton");
		standardsCancelButton.getElement().setAttribute("alt",i18n.GL0142());
		standardsCancelButton.getElement().setAttribute("title", i18n.GL0142());
		standardsText.getElement().setInnerHTML(i18n.GL1559());
		standardsText.getElement().setId("pnlStandardsText");
		standardsText.getElement().setAttribute("alt",i18n.GL1559());
		standardsText.getElement().setAttribute("title", i18n.GL1559());
		standardsEditButtonContainer.getElement().setId("pnlStandardsEditButtonContainer");
		userStandardDefaultView.getElement().setId("pnlUserStandardDefaultView");
		standardsSaveCancelButtonContainer.setVisible(false);
		
		lblCommonCore.setText(i18n.GL1560());
		lblCommonCore.getElement().setId("lblLblCommonCore");
		lblCommonCore.getElement().setAttribute("alt",i18n.GL1560());
		lblCommonCore.getElement().setAttribute("title", i18n.GL1560());
		lblCaliforniaScience.setText(i18n.GL1561());
		lblCaliforniaScience.getElement().setId("lblLblCaliforniaScience");
		lblCaliforniaScience.getElement().setAttribute("alt",i18n.GL1561());
		lblCaliforniaScience.getElement().setAttribute("title", i18n.GL1561());
		lblTexas.setText(i18n.GL1562());
		lblTexas.getElement().setId("lblLblTexas");
		lblTexas.getElement().setAttribute("alt",i18n.GL1562());
		lblTexas.getElement().setAttribute("title", i18n.GL1562());
		lblNgss.setText(i18n.GL1655());
		lblNgss.getElement().setId("lblLblNgss");
		lblNgss.getElement().setAttribute("alt",i18n.GL1655());
		lblNgss.getElement().setAttribute("title", i18n.GL1655());
		description.setText(i18n.GL1583());
		description.getElement().setId("lblDescription");
		description.getElement().setAttribute("alt",i18n.GL1583());
		description.getElement().setAttribute("title", i18n.GL1583());
		userStandardEditView.setVisible(false);
		userStandardTextPanel.getElement().setId("pnlUserStandardTextPanel");
		userStandardTextPanel.add(commonCoreChk);
		userStandardTextPanel.add(texasChk);
		userStandardTextPanel.add(ngssChk);
		
		commonCoreChk.setText(i18n.GL1560());
		commonCoreChk.setName("27787,24146");
		
		californiaStandChk.setText(i18n.GL1561());
		//californiaStandChk.setName("30424,42236,42237");
		
		texasChk.setText(i18n.GL1562());
		texasChk.setName("72168");
		ngssChk.setText(i18n.GL1655());
		ngssChk.setName("77271");
		
		commonCoreChk.setStyleName(Settings.standardsCheckBox());
	
		texasChk.setStyleName(Settings.standardsCheckBox());

		ngssChk.setStyleName(Settings.standardsCheckBox());
		//added in 6.5
		lblCSS.setText(i18n.GL2105());
		lblCaliforniaSocialSciencesStandards.setText(i18n.GL2106());
		lblCaliforniaELDS.setText(i18n.GL2107());
		
		lblCSS.getElement().setId("lblCSS");
		lblCSS.getElement().setAttribute("alt",i18n.GL2105());
		lblCSS.getElement().setAttribute("title", i18n.GL2105());
		
		lblCaliforniaSocialSciencesStandards.getElement().setId("lblCaliforniaSocialSciencesStandards");
		lblCaliforniaSocialSciencesStandards.getElement().setAttribute("alt",i18n.GL2106());
		lblCaliforniaSocialSciencesStandards.getElement().setAttribute("title", i18n.GL2106());
		
		lblCaliforniaELDS.getElement().setId("lblCaliforniaELDS");
		lblCaliforniaELDS.getElement().setAttribute("alt",i18n.GL2107());
		lblCaliforniaELDS.getElement().setAttribute("title", i18n.GL2107());
		
		CSSChk.setText(i18n.GL2105());
		
		californiaStandChk.setName("42236,42237");
		//californiaStandChk.setName("24553");
		CaliforniaSocialSciencesStandardsChk.setText(i18n.GL2106());
		CaliforniaELDSChk.setText(i18n.GL2107());
		
		CSSChk.setStyleName(Settings.standardsCheckBox());
		CaliforniaSocialSciencesStandardsChk.setStyleName(Settings.substandardsCheckBox());
		CaliforniaELDSChk.setStyleName(Settings.substandardsCheckBox());
		californiaStandChk.setStyleName(Settings.substandardsCheckBox());
		
		userStandardTextPanel.add(CSSChk);
		//CSSLabel.getElement().setAttribute("style", "margin-left: 14px;");
		//CSSLabel.setText(i18n.GL2105());
		//userStandardTextPanel.add(CSSLabel);
		userStandardTextPanel.add(californiaStandChk);
		userStandardTextPanel.add(CaliforniaSocialSciencesStandardsChk);
		userStandardTextPanel.add(CaliforniaELDSChk);
		CaliforniaSocialSciencesStandardsChk.setName("30424");
		CaliforniaELDSChk.setName("78320");
		//CSSChk.setName("42236,42237");
		
		
	//added in 6.4
		btnViewAdmin.setText(i18n.GL1993() );
		btnViewAdmin.getElement().setId("btnBtnViewAdmin");
		btnViewAdmin.getElement().setAttribute("alt", i18n.GL1993() );
		btnViewAdmin.getElement().setAttribute("title", i18n.GL1993() );
		
		displayAdminPortal();

		standardSavingTextLabel.setText("");
		standardsEditButton.setVisible(true);
		userStandardDefaultView.setVisible(true);
		lblTexas.setVisible(false);
		lblCaliforniaScience.setVisible(false);
		lblCommonCore.setVisible(false);
		lblNgss.setVisible(false);
		lblCSS.setVisible(false);
		lblCaliforniaSocialSciencesStandards.setVisible(false);
		lblCaliforniaELDS.setVisible(false);
		lblUserMessage.setText(i18n.GL1476());
		lblUserMessage.getElement().setId("lblLblUserMessage");
		lblUserMessage.getElement().setAttribute("alt", i18n.GL1476() );
		lblUserMessage.getElement().setAttribute("title", i18n.GL1476() );
		lblUserMessage.setVisible(false);
		commonCoreChk.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				//if(commonCoreChk.isChecked()||californiaStandChk.isChecked()||texasChk.isChecked())
				if(commonCoreChk.isChecked()||CSSChk.isChecked()||texasChk.isChecked()||ngssChk.isChecked()||CaliforniaSocialSciencesStandardsChk.isChecked()||CaliforniaELDSChk.isChecked()||californiaStandChk.isChecked())
				{
					standardsSaveButton.setEnabled(true);
					standardsSaveButton.getElement().removeClassName("disabled");
				}
				
			}
		});
		californiaStandChk.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						if(californiaStandChk.isChecked()||CaliforniaSocialSciencesStandardsChk.isChecked()||CaliforniaELDSChk.isChecked())
						{
							CSSChk.setChecked(true);
							
						}else{
							CSSChk.setChecked(false);
							
						}			
						if(commonCoreChk.isChecked()||CSSChk.isChecked()||texasChk.isChecked()||ngssChk.isChecked()||CaliforniaSocialSciencesStandardsChk.isChecked()||CaliforniaELDSChk.isChecked()||californiaStandChk.isChecked())
						{
							standardsSaveButton.setEnabled(true);
							standardsSaveButton.getElement().removeClassName("disabled");
						}
					}
				});
		texasChk.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
				//	if(commonCoreChk.isChecked()||californiaStandChk.isChecked()||texasChk.isChecked())
					if(commonCoreChk.isChecked()||CSSChk.isChecked()||texasChk.isChecked()||ngssChk.isChecked()||CaliforniaSocialSciencesStandardsChk.isChecked()||CaliforniaELDSChk.isChecked()||californiaStandChk.isChecked())
					{
						standardsSaveButton.setEnabled(true);
						standardsSaveButton.getElement().removeClassName("disabled");
					}
					
					
				}
			});
		ngssChk.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(commonCoreChk.isChecked()||CSSChk.isChecked()||texasChk.isChecked()||ngssChk.isChecked()||CaliforniaSocialSciencesStandardsChk.isChecked()||CaliforniaELDSChk.isChecked()||californiaStandChk.isChecked())
				{
					standardsSaveButton.setEnabled(true);
					standardsSaveButton.getElement().removeClassName("disabled");
				}
				
				
			}
		});
		CSSChk.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(CSSChk.isChecked())
				{
					CaliforniaSocialSciencesStandardsChk.setChecked(true);
					CaliforniaELDSChk.setChecked(true);
					californiaStandChk.setChecked(true);
				}else{
					CaliforniaSocialSciencesStandardsChk.setChecked(false);
					CaliforniaELDSChk.setChecked(false);
					californiaStandChk.setChecked(false);
				}
				if(commonCoreChk.isChecked()||CSSChk.isChecked()||texasChk.isChecked()||ngssChk.isChecked()||CaliforniaSocialSciencesStandardsChk.isChecked()||CaliforniaELDSChk.isChecked()||californiaStandChk.isChecked())
				{
					standardsSaveButton.setEnabled(true);
					standardsSaveButton.getElement().removeClassName("disabled");
				}
				
			}
		});
		CaliforniaSocialSciencesStandardsChk.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(CaliforniaSocialSciencesStandardsChk.isChecked()||CaliforniaELDSChk.isChecked()||californiaStandChk.isChecked())
				{
					CSSChk.setChecked(true);
					
				}else{
					CSSChk.setChecked(false);
					
				}	
				if(commonCoreChk.isChecked()||CSSChk.isChecked()||texasChk.isChecked()||ngssChk.isChecked()||CaliforniaSocialSciencesStandardsChk.isChecked()||CaliforniaELDSChk.isChecked()||californiaStandChk.isChecked())
				{
					standardsSaveButton.setEnabled(true);
					standardsSaveButton.getElement().removeClassName("disabled");
				}
				
			}
		});
		CaliforniaELDSChk.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(CaliforniaELDSChk.isChecked()||CaliforniaSocialSciencesStandardsChk.isChecked()||californiaStandChk.isChecked())
				{
					CSSChk.setChecked(true);
					
				}else{
					CSSChk.setChecked(false);
					
				}	
				if(commonCoreChk.isChecked()||CSSChk.isChecked()||texasChk.isChecked()||ngssChk.isChecked()||CaliforniaSocialSciencesStandardsChk.isChecked()||CaliforniaELDSChk.isChecked()||californiaStandChk.isChecked())
				{
					standardsSaveButton.setEnabled(true);
					standardsSaveButton.getElement().removeClassName("disabled");
				}
				
			}
		});
		AppClientFactory.getEventBus().addHandler(StandardPreferenceSettingEvent.TYPE, standardPreferenceSettingHandler);
		
		if (AppClientFactory.isAnonymous()){
			AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.HOME);
		}
	}
	StandardPreferenceSettingHandler standardPreferenceSettingHandler= new StandardPreferenceSettingHandler(){
		@Override
		public List<String> getCode(List<String> standPrefCode) {
			userStandardPrefcode.clear();
			if(standPrefCode!=null){
				userStandardPrefcode.addAll(standPrefCode);
			}
			getUserCodeId(userStandardPrefcode);
				return standPrefCode;
			
			}
	};
	/**
	 * 
	 * @fileName : UserSettingsView.java
	 *
	 * @description : 
	 *
	 *
	 * @version : 1.0
	 *
	 * @date: Mar 14, 2014
	 *
	 * @Author Gooru Team
	 *
	 * @Reviewer:
	 */
	private class OnKeyUpHandler implements KeyUpHandler {

		@Override
		public void onKeyUp(KeyUpEvent event) {
			clearErrorMessage();
			if (txtUserName.getText().length() <4 || txtUserName.getText().length() >20){
				setErrorMessage(i18n.GL0473());
			}
			if (txtUserName.getText().equalsIgnoreCase(lbUName.getText().trim())){
				isUserNameChanged = false;
				clearErrorMessage();
				enableAccSaveButton();
				return;
			}
			isUserNameChanged = true;
			disableAccSaveButton();
			
			 if (event.getSource() == txtUserName
					&& txtUserName.getText()!= null
					&& !txtUserName.getText().equalsIgnoreCase("")) {
								
				boolean userNameValidate = txtUserName.getText().matches(USER_NAME_REGEX);
				/// Words are clear then continue the next steps
				if(!userNameValidate){
					 if(!txtUserName.getText().contains(" ")){
							if (txtUserName.isVisible()){
									setErrorMessage(i18n.GL0475());
								}
					}else if(txtUserName.getText().contains(" ")){
						setErrorMessage(i18n.GL1635());
					}
				}else if (txtUserName.getText().length() <4 || txtUserName.getText().length() >20){
					if (txtUserName.isVisible())
						setErrorMessage(i18n.GL0473());
				}else{
					// Check for profanity for user name
					if (txtUserName.isVisible())
						checkProfanity(txtUserName.getText().trim());
				}
			} 
		}

		
	}
	/**
	 * 
	 * @function checkProfanity 
	 * 
	 * @created_date : Mar 14, 2014
	 * 
	 * @description
	 * 
	 * 
	 * @param inputString
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	public void checkProfanity(String inputString){
		disableAccSaveButton();
		Map<String, String> parms = new HashMap<String, String>();
		parms.put("text", inputString);
		AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean value) {
				isProfanityCleared = !value;
				if (value){
					if (txtUserName.isVisible()){
						setErrorMessage(i18n.GL0554());
						disableAccSaveButton();
					}
				}else{
					if (txtUserName.isVisible()){
//						enableAccSaveButton();
						clearErrorMessage();
						
						isValidUserName = checkUserAvailability(txtUserName.getText().trim(), BY_USERNAME);
					}
				}
			}
		});		
	}
	/**
	 * Checks the availability of user name, entered by User.
	 * 
	 * @param userName
	 * @param type
	 * 
	 */
	public boolean checkUserAvailability(String userName, final String type) {

		AppClientFactory.getInjector().getUserService()
				.getEmailId(userName, type, new SimpleAsyncCallback<UserDo>() {
					@Override
					public void onSuccess(UserDo result) {
						isAvailable = result.isAvailability();
						if (type.equalsIgnoreCase(BY_USERNAME) && isAvailable) {
							isValidUserName = result.isAvailability();
							if (txtUserName.isVisible()){
								setErrorMessage(i18n.GL0444());
								disableAccSaveButton();
							}

						}else if (type.equalsIgnoreCase(BY_USERNAME) && !isAvailable) {
							if (txtUserName.isVisible()){
								isValidUserName = result.isAvailability();
								
								clearErrorMessage();
								enableAccSaveButton();
							}
						}
					}
				});
		return isAvailable;
	}
	private class onEditImageName implements ClickHandler
	{

		@Override
		public void onClick(ClickEvent event) {
			buttonContainer.setVisible(true);
			editButtonAccount.setVisible(false);
			tbFirstNameUcLabel.switchToEdit();
			tbLastNameUcLabel.switchToEdit();
			lbUName.setVisible(false);
			txtUserName.setVisible(true);
			panelHelp.setVisible(true);
			radioButtonContainer.setVisible(true);
			lblgender.setVisible(false);
			emailbuttonContainer.setVisible(false);
			EduInfoButtonContainer.setVisible(false);
			lbEmail.cancel();
			if(!AppClientFactory.loggedInUser.getLoginType().trim().equalsIgnoreCase("apps")){
				editButtonContact.setVisible(!isChildAccount);
			}
			editButtonEdu.setVisible(true);
			gradeContainer.setVisible(false);
			DefaultGardeContainer.setVisible(true);
			collectionCourseDefaultLstPanel.setVisible(true);
			courseContainer.setVisible(false);
			
			
			txtUserName.setText(lbUName.getText());
			txtUserName.getElement().setAttribute("alt", lbUName.getText());
			txtUserName.getElement().setAttribute("title", lbUName.getText());
			txtUserName.setFocus(true);
//			disableAccSaveButton();
			
			clearErrorMessage();
		}
		
	}
	private class onEditForEmail implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			emailbuttonContainer.setVisible(true);
			//editButtonContact.setVisible(false);
			lbEmail.switchToEdit();
			buttonContainer.setVisible(false);
			EduInfoButtonContainer.setVisible(false);
			tbFirstNameUcLabel.switchToLabel();
			tbLastNameUcLabel.switchToLabel();
			editButtonEdu.setVisible(true);
			editButtonContact.setVisible(!isChildAccount);
			radioButtonContainer.setVisible(false);
			lblgender.setVisible(true);
			editButtonAccount.setVisible(true);
			gradeContainer.setVisible(false);
			DefaultGardeContainer.setVisible(true);
			collectionCourseDefaultLstPanel.setVisible(true);
			courseContainer.setVisible(false);
		
		
			lbUName.setVisible(true);
			txtUserName.setVisible(false);
			panelHelp.setVisible(false);
			clearErrorMessage();
		}
		
	}
	private class onEducationInfo implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			EduInfoButtonContainer.setVisible(true);
			editButtonEdu.setVisible(false);
			gradeContainer.setVisible(true);
			DefaultGardeContainer.setVisible(false);
			collectionCourseDefaultLstPanel.setVisible(false);
			courseContainer.setVisible(true);
			buttonContainer.setVisible(false);
			emailbuttonContainer.setVisible(false);
			lbEmail.cancel();
			tbFirstNameUcLabel.switchToLabel();
			tbLastNameUcLabel.switchToLabel();
			
			if(!AppClientFactory.loggedInUser.getLoginType().trim().equalsIgnoreCase("apps")){
				//editButtonContact.setVisible(true);
				editButtonContact.setVisible(!isChildAccount);
			}
			radioButtonContainer.setVisible(false);
			lblgender.setVisible(true);
			editButtonAccount.setVisible(true);
			
			lbUName.setVisible(true);
			txtUserName.setVisible(false);
			panelHelp.setVisible(false);
			clearErrorMessage();
		}
		
	}
	@UiHandler("btnConnect")
	public void onClickConnect(ClickEvent event){
		
		
		if (!isDriveConnected){
			getUiHandlers().getGoogleDrive();
			
		}else{
			getUiHandlers().revokeToken();
			//StringUtil.clearCookies("google-access-token", "/", ".www.goorulearning.org");
			//googleDirveStatus(false);
		}
	}
	
	@UiHandler("settingsSaveButton")
	public void OnClickSaveButton(ClickEvent event) {
		
		tbFirstNameUcLabel.switchToLabel();
		tbLastNameUcLabel.switchToLabel(); 
		if(enableEdit && enableEditFirstName){
			enableEdit=false;
			enableEditFirstName=false;
			buttonContainer.setVisible(false);
			editButtonAccount.setVisible(false);
			radioButtonContainer.setVisible(false);
			lblgender.setVisible(true);
			charLimitFNameLbl.setVisible(false);
			accountSavingTextLabel.setVisible(true);
			
			lbUName.setVisible(true);
			lbUName.setText(txtUserName.getText());
			lbUName.getElement().setAttribute("alt", txtUserName.getText());
			lbUName.getElement().setAttribute("title", txtUserName.getText());
			txtUserName.setVisible(false);
			panelHelp.setVisible(false);
			
			getUiHandlers().saveSettingsInformation();
			
		}else{
			tbFirstNameUcLabel.switchToEdit();
			tbLastNameUcLabel.switchToEdit();
		}
		
	}
	@UiHandler("settingCancelButton")
	public void OnClickCancelSettingpage(ClickEvent event) {
		tbFirstNameUcLabel.cancel();
		tbLastNameUcLabel.cancel();
		enableEdit=false;
		enableEditFirstName=false;
		buttonContainer.setVisible(false);
		editButtonAccount.setVisible(true);
		radioButtonContainer.setVisible(false);
		lblgender.setVisible(true);
		charLimitFNameLbl.setVisible(false);
		lbUName.setVisible(true);
		txtUserName.setVisible(false);
		panelHelp.setVisible(false);
		txtUserName.setText(lbUName.getText());
		txtUserName.getElement().setAttribute("alt", lbUName.getText());
		txtUserName.getElement().setAttribute("title", lbUName.getText());
		if(!AppClientFactory.loggedInUser.getLoginType().trim().equalsIgnoreCase("apps")&& AppClientFactory.loggedInUser.getAccountTypeId()!=2){
			editButtonContact.setVisible(true);
		}
		clearErrorMessage();
		
	}
	@UiHandler("emailSaveButton")
	public void onClickEmailSaveButton(ClickEvent event) {
		MixpanelUtil.Settings_email_change_saved();
		lbEmail.switchToLabel();
		
	}
	@UiHandler("emailCancelButton")
	public void onClickEmailCancelButton(ClickEvent event) {
		lbEmail.cancel();
		emailbuttonContainer.setVisible(false);
		editButtonContact.setVisible(true);
		
		clearErrorMessage();
		
	}
	@UiHandler("eduInfoSaveButton")
	public void onClickEduInfoSaveButton(ClickEvent event) {
		MixpanelUtil.Settings_educational_info_saved();
		gradeContainer.setVisible(false);
		DefaultGardeContainer.setVisible(true);
		EduInfoButtonContainer.setVisible(false);
		editButtonEdu.setVisible(true);
		collectionCourseDefaultLstPanel.setVisible(true);
		courseContainer.setVisible(false);
		EduSavingTextLabel.setVisible(true);
		editButtonEdu.setVisible(false);
		AppClientFactory.getInjector().getUserService().getUserProfileV2Details(gooruUid, USER_META_ACTIVE_FLAG, new SimpleAsyncCallback<ProfileDo>() {

			@Override
			public void onSuccess(ProfileDo profileObj) {
				setProfileData(profileObj);
				EduSavingTextLabel.setVisible(false);
				editButtonEdu.setVisible(true);
			}
			
		});
		
		}
	@UiHandler("eduInfoCancelButton")
	public void onClickEduInfoCancelButton(ClickEvent event) {
		gradeContainer.setVisible(false);
		EduInfoButtonContainer.setVisible(false);
		editButtonEdu.setVisible(true);
		DefaultGardeContainer.setVisible(true);
		collectionCourseDefaultLstPanel.setVisible(true);
		courseContainer.setVisible(false);
		
		clearErrorMessage();
	}
	
	public Label getForgetPassword() {
		return forgetPassword;
	}
	
	public Label getForgetPasswordMsg() {
		return forgetPasswordMsg;
	}
	
	public HTMLEventPanel getPlAccount() {
		return plAccount;
	}

	public HTMLPanel getUserAccount() {
		return userAccount;
	}

	public HTMLPanel getInfoArrow() {
		return accountMiniusArrow;
	}

	
	public HTMLEventPanel getPlSecurity() {
		return plSecurity;
	}

	public HTMLPanel getUserSecurity() {
		return userSecurity;
	}


	public HTMLPanel getSecurityMiniusArrow() {
		return securityMiniusArrow;
	}

	public Label getLbMale() {
		return lbMale;
	}

	public Label getLbFemale() {
		return lbFemale;
	}

	public Label getLbOther() {
		return lbOther;
	}

	public Label getLbShare() {
		return lbShare;
	}

	public HTMLEventPanel getPlEducation() {
		return plEducation;
	}

	public HTMLEventPanel getPlContact() {
		return plContact;
	}

	public HTMLPanel getUserEducation() {
		return userEducation;
	}

	public HTMLPanel getUserContact() {
		return userContact;
	}

	public HTMLPanel getAccountMiniusArrow() {
		return accountMiniusArrow;
	}


	public HTMLPanel getEducationalMiniusArrow() {
		return educationalMiniusArrow;
	}

	

	public HTMLPanel getContactMiniusArrow() {
		return contactMiniusArrow;
	}

	public SettingEmailEditLabelUc getLbEmail() {
		return lbEmail;
	}

	public Label getLbRole() {
		return lbRole;
	}

	

	public Label getLbName() {
		return lbName;
	}

	public Label getLbUserName() {
		return lbUserName;
	}
    
	public Label getLbUName() {
		return lbUName;
	}
	/** 
	 * This method is to get the txtUserName
	 */
	@Override
	public TextBox getTxtUserName() {
		return txtUserName;
	}

	/** 
	 * This method is to set the txtUserName
	 */
	public void setTxtUserName(TextBox txtUserName) {
		this.txtUserName = txtUserName;
	}
	/*closeAllOpenedPopUp
	 * To set the title based on Role 
	 */
	@Override
	public void setData(SettingDo settingDo) {
//		String role=settingDo.getUserType();
//		gooruUid=settingDo.getUser().getGooruUId();
//		if(role != null){
//			if(role.equalsIgnoreCase("Teacher")){
//				gradeLbl.setText(i18n.GL1477);
//				courseLbl.setText(i18n.GL1478);
//			}
//			else if(role.equalsIgnoreCase("Student")){
//				gradeLbl.setText(i18n.GL1479);
//				courseLbl.setText(i18n.GL1480);
//			}
//			else{
//				gradeLbl.setText(i18n.GL1481);
//				courseLbl.setText(i18n.GL1482);
//			}
//		}
//		else{
//			if(settingDo.getUser().getAccountTypeId() == 2){
//				gradeLbl.setText(i18n.GL1479);
//				courseLbl.setText(i18n.GL1480);
//			}
//			
//		}
		
		
	}
	/*closeAllOpenedPopUp
	 * To set the title based on Role 
	 */
	@Override
	public void setData(V2UserDo v2userDo) {
		this.v2userDo = v2userDo;
		String role=v2userDo.getUserType();
		gooruUid=v2userDo.getUser().getGooruUId();
		if(role != null){
			if(role.equalsIgnoreCase("Teacher")){
				gradeLbl.setText(i18n.GL1477());
				gradeLbl.getElement().setAttribute("alt",i18n.GL1477());
				gradeLbl.getElement().setAttribute("title", i18n.GL1477());
				courseLbl.setText(i18n.GL1478());
				courseLbl.getElement().setAttribute("alt",i18n.GL1478());
				courseLbl.getElement().setAttribute("title", i18n.GL1478());
			}
			else if(role.equalsIgnoreCase("Student")){
				gradeLbl.setText(i18n.GL1479());
				gradeLbl.getElement().setAttribute("alt",i18n.GL1479());
				gradeLbl.getElement().setAttribute("title", i18n.GL1479());
				courseLbl.setText(i18n.GL1480());
				courseLbl.getElement().setAttribute("alt",i18n.GL1480());
				courseLbl.getElement().setAttribute("title", i18n.GL1480());
			}
			else{
				gradeLbl.setText(i18n.GL1481());
				gradeLbl.getElement().setAttribute("alt",i18n.GL1481());
				gradeLbl.getElement().setAttribute("title",i18n.GL1481());
				courseLbl.setText(i18n.GL1482());
				courseLbl.getElement().setAttribute("alt",i18n.GL1482());
				courseLbl.getElement().setAttribute("title", i18n.GL1482());
			}
		}
		else{
			if(v2userDo.getUser().getAccountTypeId()!=null&&v2userDo.getUser().getAccountTypeId() == 2){
				gradeLbl.setText(i18n.GL1479());
				gradeLbl.getElement().setAttribute("alt",i18n.GL1479());
				gradeLbl.getElement().setAttribute("title", i18n.GL1479());
				courseLbl.setText(i18n.GL1480());
				courseLbl.getElement().setAttribute("alt",i18n.GL1480());
				courseLbl.getElement().setAttribute("title", i18n.GL1480());
			}
			
		}
		
		
	}
	@UiHandler("forgetPassword")
	public void onForgotPwdClicked(ClickEvent clickEvent) {
		if(lbEmail.getText()!=null){
			lblPleaseWait.setVisible(true);
			forgetPassword.setVisible(false);
			AppClientFactory.getInjector().getUserService().forgotPassword(lbEmail.getText(), new SimpleAsyncCallback<Map<String, Object>>() {
					@Override
					public void onSuccess(Map<String, Object> result) {
						lblPleaseWait.setVisible(false);
						forgetPassword.setVisible(true);
					if (result != null && result.containsKey("error") && result.get("error").toString().length() > 0) {
						alertContentUc=new AlertContentUc(i18n.GL0061(), (String) result.get("error"));
						return;
					}
					if (result != null && result.containsKey("gooruUid") && result.get("gooruUid").toString().length() > 0) {
						Window.enableScrolling(false);
						AppClientFactory.fireEvent(new SetHeaderZIndexEvent(98, false));
						forgotPwdSuccessVc=new ForgotPwdSuccessVc();
						forgotPwdSuccessVc.center();
					}
				}
		
			});
		}
	}
	
	public String getSelectedButton(){
		return Settings.radioButtonSelected();
	}
	public String getRadioButton(){
		return Settings.radio();
	}

	@Override
	public void clearPanels() {
		
		tbFirstNameUcLabel.setText("");
		lbUName.setText("");
		txtUserName.setText("");
		panelHelp.setVisible(false);
		tbLastNameUcLabel.setText("");
		lbRole.setText("");
		lbUserName.setText("");
		lbName.setText("");
		lbEmail.setText("");
		profileTextArea.setText("");
		contactMiniusArrow.getElement().getStyle().setDisplay(Display.NONE);
		educationalMiniusArrow.getElement().getStyle().setDisplay(Display.NONE);
		securityMiniusArrow.getElement().getStyle().setDisplay(Display.NONE);
		accountMiniusArrow.getElement().getStyle().setDisplay(Display.NONE);
		
	    userAccount.getElement().getStyle().setDisplay(Display.BLOCK);
	    userContact.getElement().getStyle().setDisplay(Display.BLOCK);
	    userEducation.getElement().getStyle().setDisplay(Display.BLOCK);
	    userSecurity.getElement().getStyle().setDisplay(Display.BLOCK);
	    lbMale.setStyleName(getRadioButton());
	    lbFemale.setStyleName(getRadioButton());
	    lbOther.setStyleName(getRadioButton());
	    lbShare.setStyleName(getRadioButton());
	}

	@Override
	public Button getprofileOnButton() {
		profileOnButton.setStyleName(Settings.publicProfileOnButtonActive());
		profileOnButton.removeStyleName(Settings.publicProfileOnButtonDeActive());
		profileOffButton.setStyleName(Settings.publicProfileOffButtonsDeActive());
		profileOffButton.removeStyleName(Settings.publicProfileOffButtonsActive());
		return profileOnButton;
	}

	@Override
	public Button getProfileOffButton() {
		profileOffButton.setStyleName(Settings.publicProfileOffButtonsActive());
		profileOffButton.removeStyleName(Settings.publicProfileOffButtonsDeActive());
		profileOnButton.setStyleName(Settings.publicProfileOnButtonDeActive());
		profileOnButton.removeStyleName(Settings.publicProfileOnButtonActive());
	
		return profileOffButton;
	}

	@Override
	public ProfileBiographyEditUC getProfileBiographyEditUC() {
		return profileTextArea;
	}

	@Override
	public HTMLPanel getAboutUsContainer() {
		return aboutUsContainer;
	}

	@Override
	public Button getSeeMyPageButton() {
	return btnSeeMyProfile;
	}

	
	@Override
	public FocusPanel noAboutUsContainer() {
		return noAboutUsContainer;
	}

	@Override
	public HTMLEventPanel getBiographyCancelButton() {
		return biographyCancelButton;
	}

	@Override
	public Label getaboutUsCharacterValidation() {
		return aboutUsCharacterValidation;
	}
	
	
	private class UploadProfileImage implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			uploadProfilImageButton.getElement().getStyle().setDisplay(Display.NONE);
			getUiHandlers().showUploadProfileImageWidget();
		}
	}
	
	private class ProfileDefaultImage implements ErrorHandler{
		@Override
		public void onError(ErrorEvent event) {
			uploadProfileImage.setUrl(PROFILE_DEFAULT_IMAGE);
			uploadProfilImageButton.setText(i18n.GL1087());
			try{
				uploadProfileImage.getElement().setAttribute("alt", v2userDo.getUser().getUsername());
				uploadProfileImage.getElement().setAttribute("title", v2userDo.getUser().getUsername());
				uploadProfileImage.setAltText(v2userDo.getUser().getUsername());
				uploadProfileImage.setTitle(v2userDo.getUser().getUsername());
			}catch(Exception exception){

			}
		}
	}

	@Override
	public void setUserProfileImageUrl(String imageUrl) {
		double randomNumber=Math.random();
		uploadProfileImage.setUrl(imageUrl+"?p="+randomNumber);
		uploadProfilImageButton.setText(i18n.GL0800());
		try{
			uploadProfileImage.getElement().setAttribute("alt", v2userDo.getUser().getUsername());
			uploadProfileImage.getElement().setAttribute("title", v2userDo.getUser().getUsername());
			uploadProfileImage.setAltText(v2userDo.getUser().getUsername());
			uploadProfileImage.setTitle(v2userDo.getUser().getUsername());
		}catch(Exception exception){

		}

	}
	private class ShowUploadImageButton implements MouseOverHandler{

		@Override
		public void onMouseOver(MouseOverEvent event) {
			uploadProfilImageButton.getElement().getStyle().setDisplay(Display.BLOCK);
		}
		
	}
	private class HideUploadImageButton implements MouseOutHandler{

		@Override
		public void onMouseOut(MouseOutEvent event) {
			uploadProfilImageButton.getElement().getStyle().setDisplay(Display.NONE);
		}
		
	}

	@Override
	public void setProfileData(ProfileDo profileDo) {
		uploadProfileImage.setUrl(profileDo.getUser().getProfileImageUrl() + "?p="+ Math.random());
		uploadProfilImageButton.setText(i18n.GL0800());
		StringUtil.setAttributes(uploadProfilImageButton.getElement(), "uploadProfilImageButton", i18n.GL0800(), i18n.GL0800());
		setGradeList(profileDo.getGrade(), profileDo);
		Set<ProfileCodeDo> codeDo = profileDo.getCourses();
		coursesPanel.clear();
		
		collectionCourseDefaultLstPanel.clear();
		for (ProfileCodeDo code : codeDo) {
			coursesPanel.add(createCourseLabel(code.getCode().getLabel(), code.getCode().getCodeId() + ""));
			//collectionCourseDefaultLstPanel.add(createCourseLabel(code.getCode().getLabel(), code.getCode().getCodeId() + ""));
			defaultCoursePanel=new HTML(code.getCode().getLabel());
			collectionCourseDefaultLstPanel.add(defaultCoursePanel);
			defaultCoursePanel.setStyleName(Settings.deafaultCourse());
			
		}
		if(profileDo.getCourses().size()==0){
			Label defaultCourseLabel=new Label();
			defaultCourseLabel.setStyleName(Settings.defaultTextcss());
			defaultCourseLabel.getElement().setAttribute("style","margin-left: 0px !important");
			defaultCourseLabel.setText(i18n.GL1476());
			collectionCourseDefaultLstPanel.add(defaultCourseLabel);
		}
		
	}
	
	/**
	 * separate the view according to grade level of the user
	 */
	public void setGradeList(String grades, ProfileDo profileDo) {
		if (grades != null) {
			DefaultGardeContainer.clear();
			// For short
			List<Integer> listI = new ArrayList<Integer>();
			List<String> listS = new ArrayList<String>();

			List<Object> listO = new ArrayList<Object>();
			String[] newst = grades.split(",");

			for (int i = 0; i < newst.length; i++) {
				try {
					int k = Integer.parseInt(newst[i]);
					listI.add(k);
				} catch (Exception e) {
					listS.add(newst[i]);
				}
			}
			Collections.sort(listS, Collections.reverseOrder());

			Collections.sort(listI);
			if (listS.contains("Kindergarten")
					&& listS.contains("Higher Education")) {
				listO.add("Kindergarten");
				listO.addAll(listI);
				listO.add("Higher Education");
			} else if (listS.contains("Kindergarten")) {
				listO.add("Kindergarten");
				listO.addAll(listI);
			} else if (listS.contains("Higher Education")) {
				listO.addAll(listI);
				listO.add("Higher Education");
			} else {
				listO.addAll(listI);
			}

			StringBuilder sortedGrade = new StringBuilder();
			for (Object obj : listO) {

				sortedGrade.append(obj.toString());
				Label gradeLabel = new Label(obj.toString());
				gradeLabel.setStyleName(CollectionCBundle.INSTANCE.css()
						.settingPageDefaultGrade());
				gradeLabel.getElement().setAttribute("selected", "selected");
				DefaultGardeContainer.add(gradeLabel);
			}
		}
		else
		{
			DefaultGardeContainer.clear();
			Label defaulTextLabel=new Label();
			defaulTextLabel.setStyleName(Settings.defaultTextcss());
			defaulTextLabel.setText(i18n.GL1476());
			DefaultGardeContainer.add(defaulTextLabel);
			
		}
				
		KinderGarten.clear();
		higherEducation.clear();
		gradeTopList.clear();
		gradeMiddleList.clear();
		
		KinderGarten.add(new ProfilePageGradeLabel(i18n.GL0850(), profileDo));
		higherEducation.add(new ProfilePageGradeLabel(i18n.GL0169(),profileDo));
		for (int i = 1; i <= 12; i++) {
			if (i <= 6) {
				gradeTopList.add(new ProfilePageGradeLabel(i + "", profileDo));
				
			}
			if (i >= 7) {
				gradeMiddleList.add(new ProfilePageGradeLabel(i + "", profileDo));
				
			}
		}
	}
	
	
	/**
	 * New course is created for User
	 * 
	 * @param courseLabel
	 *            the course of the label widget which is used to get all
	 *            courses
	 * @param courseCode
	 *            the course of the course code, an absolute course code will be
	 *            used to create a new course
	 * @return the label of all course created for the User.
	 */
	protected CloseLabelSetting createCourseLabel(final String courseLabel,
			final String courseCode) {
		return new CloseLabelSetting(courseLabel) {

			@Override
			public void onCloseLabelClick(ClickEvent event) {
				this.removeFromParent();
				CodeDo codeDo = new CodeDo();
				codeDo.setCodeId(Integer.parseInt(courseCode));
				getUiHandlers().deleteCourse(codeDo);
			}
		};
	}
	/**
	 * Set courses to the ListBox
	 * 
	 * @param libraryCode 
	 *          To set library code for all courses.  
	 */
	@Override
	public void setCourseList(List<LibraryCodeDo> libraryCode) {
		collectionCourseLst = new GroupedListBox();
		collectionCourseLst.addStyleName(CollectionCBundle.INSTANCE.css()
				.infoTextBox());
		collectionCourseLst.addItem(" What course(s) do you teach? ", "-1");
		if (libraryCode != null) {
			for (LibraryCodeDo libraryCodeDo : libraryCode) {
				for (LibraryCodeDo liCodeDo : libraryCodeDo.getNode()) {
					collectionCourseLst.addItem(libraryCodeDo.getLabel() + "|"
							+ liCodeDo.getLabel(), liCodeDo.getCodeId() + "");
				}
			}
		}
		collectionCourseLstPanel.clear();
		collectionCourseLstPanel.add(collectionCourseLst);
		
		collectionCourseLst.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				addCourseEvent();
			}
		});
			
	}
	
	/**
	 * Adding new course , will check it has more than five
	 * courses and the selected course is repeated or not
	 * 
	 */
	
	private void addCourseEvent() {
			if (coursesPanel.getWidgetCount() < 5) {
				final String courseCodeLabel = collectionCourseLst
						.getItemText(collectionCourseLst.getSelectedIndex());
				final String courseCode = collectionCourseLst
						.getValue(collectionCourseLst.getSelectedIndex());
				if (collectionCourseLst.getSelectedIndex() == 0) {
					return;
				}
				if (validateCourse(courseCodeLabel) && courseCode != null) {
					alertContentUc=	new AlertContentUc(i18n.GL1089(), i18n.GL1090());
				} else {
					Set<ProfileCodeDo> profileCodeDoSet = new HashSet<ProfileCodeDo>();
					ProfileCodeDo profileCodeDo = new ProfileCodeDo();
					CodeDo codeDo = new CodeDo();
					codeDo.setCodeId(Integer.parseInt(courseCode));
					profileCodeDo.setCode(codeDo);
					profileCodeDoSet.add(profileCodeDo);
					getUiHandlers().addCourse(profileCodeDoSet);
					coursesPanel.add(createCourseLabel(courseCodeLabel, courseCode));
					collectionCourseLst.setSelectedIndex(0);
				}
				courseMaxHide();
			} else {
				courseMaxShow();
			}
		
	}
	
	
	/**
	 * Will check it ,the selected course is repeated or not
	 * 
	 * @param course
	 *            to validate with already added course
	 * @return true if repeated course is selected or false
	 */
	protected boolean validateCourse(String course) {
		for (Widget widget : coursesPanel) {
			if (course.equals(((CloseLabelSetting) widget).getSourceText())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * to hide message if course exceeds less than five
	 */
	public void courseMaxHide() {
		courseData.setStyleName(CollectionCBundle.INSTANCE.css().floatLeft());
		collectionCourseLst.setStyleName(CollectionCBundle.INSTANCE.css()
				.infoTextBox());
		/*addCourseBtn.setStyleName(CollectionCBundle.INSTANCE.css()
				.infoAddButton());*/
		courseMaxMsg.setStyleName(CollectionCBundle.INSTANCE.css()
				.courseMaxMsg());
		courseMaxMsg.getElement().getStyle().setFloat(Float.LEFT);
	}
	
	/**
	 * to display message if course exceeds more than five
	 */
	public void courseMaxShow() {
		courseMaxMsg.setStyleName(CollectionCBundle.INSTANCE.css().profileCourseMaxMsgShow());
		new FadeInAndOut(courseMaxMsg.getElement(), 10000, 10000);
	}

	@Override
	public void closeAllOpenedPopUp() {
		/*if(savePopup!=null && savePopup.isShowing()){
			savePopup.hide();
		}
	*/	if(alertContentUc!=null){
			alertContentUc.getAlertBox().hide();
		}
		if(forgotPwdSuccessVc!=null){
			forgotPwdSuccessVc.hide();
		}
	}

	@Override
	public void disableContentAndSetOldContent(String aboutMe) {
	
		
	}

	@Override
	public SettingLastNameEditLabelUC getTbLastName() {
		 
		return tbLastNameUcLabel;
	}

	@Override
	public SettingEditLabelUc getTbFirstName() {
		return tbFirstNameUcLabel;
	}

	@Override
	public Label getSavingTextLabel() {
		return SavingTextLabel;
	}

	@Override
	public Button getEditEmailButton() {
		return editButtonContact;
	}

	@Override
	public Label getEmailTextConfirmation() {
		return emailTextConfirmation;
	}
	

	@Override
	public Label getGenderText() {
		return lblgender;
	}

	/** 
	 * This method is to get the isChildAccount
	 */
	public boolean isChildAccount() {
		return isChildAccount;
	}

	/** 
	 * This method is to set the isChildAccount
	 */
	@Override
	public void setChildAccount(boolean isChildAccount) {
		this.isChildAccount = isChildAccount;
	}

	@Override
	public Label getAccountSavingText() {
		return accountSavingTextLabel;
	}

	@Override
	public Button getEditButtonAccount() {
		return editButtonAccount;
	}
	
	private void setErrorMessage(String errorMessage){
		userNameValidationUc.getElement().getStyle().setMarginLeft(85, Unit.PX);
		userNameValidationUc.getElement().getStyle().setLineHeight(120, Unit.PCT);
		userNameValidationUc.getElement().getStyle().setWidth(300, Unit.PX);
		userNameValidationUc.getElement().getStyle().setFontSize(12, Unit.PX);
		userNameValidationUc.setVisible(true);
		userNameValidationUc.addStyleName("errorMessage");
		userNameValidationUc.setText(errorMessage);
		userNameValidationUc.getElement().setAttribute("alt", errorMessage);
		userNameValidationUc.getElement().setAttribute("title", errorMessage);
		
		txtUserName.getElement().getStyle().setBorderColor("orange");
	}
	
	private void clearErrorMessage(){
		userNameValidationUc.setVisible(false);
		userNameValidationUc.getElement().getStyle().setLineHeight(0, Unit.PCT);
		txtUserName.getElement().getStyle().clearBackgroundColor();
		txtUserName.getElement().getStyle().setBorderColor("#ccc");
		
	}
	
	private void disableAccSaveButton(){
		settingsSaveButton.getElement().addClassName(DISABLED);
		settingsSaveButton.setEnabled(false);
	}
	
	private void enableAccSaveButton(){
		settingsSaveButton.getElement().removeClassName(DISABLED);
		settingsSaveButton.setEnabled(true);
	}

	/** 
	 * This method is to get the isUserNameChanged
	 */
	@Override
	public boolean isUserNameChanged() {
		return isUserNameChanged;
	}

	/** 
	 * This method is to set the isUserNameChanged
	 */
	public void setUserNameChanged(boolean isUserNameChanged) {
		this.isUserNameChanged = isUserNameChanged;
	}

	/** 
	 * This method is to get the isValidUserName
	 */
	@Override
	public boolean isValidUserName() {
		return isValidUserName;
	}

	/** 
	 * This method is to set the isValidUserName
	 */
	public void setValidUserName(boolean isValidUserName) {
		this.isValidUserName = isValidUserName;
	}

	@Override
	public void hideuserDetailsContainerOnClickOfTab() {
		buttonContainer.setVisible(false);
		editButtonAccount.setVisible(true);
		tbFirstNameUcLabel.switchToLabel();
		tbLastNameUcLabel.switchToLabel();
		lbUName.setVisible(true);
		
	}
	@UiHandler("standardsEditButton")
	public void onClickOfstandardsEditButton(ClickEvent event)
	{
		standardsSaveCancelButtonContainer.setVisible(true);
		standardsEditButton.setVisible(false);
		userStandardEditView.setVisible(true);
		userStandardDefaultView.setVisible(false);
		//CSSLabel.setVisible(true);
	}
	public String getcheckedValue(){
		String codeId = "";
		
		if(commonCoreChk.isChecked()){
			if(codeId!=""){
				codeId=codeId+","+commonCoreChk.getName();
			}
			else
			{
				codeId=codeId+commonCoreChk.getName();	
			}
		}
		else
		{
			
		}
		if(californiaStandChk.isChecked())
		{
			if(codeId!=""){
				codeId=codeId+","+californiaStandChk.getName();	
			}
			else
			{
				codeId=codeId+californiaStandChk.getName();	
			}
		}
		if(texasChk.isChecked())
		{
			if(codeId!=""){
				codeId=codeId+","+texasChk.getName();	
			}
			else
			{
				codeId=codeId+texasChk.getName();	
			}
		}
		if(ngssChk.isChecked())
		{
			if(codeId!=""){
				codeId=codeId+","+ngssChk.getName();	
			}
			else
			{
				codeId=codeId+ngssChk.getName();	
			}
		}
		if(CaliforniaSocialSciencesStandardsChk.isChecked())
		{
			if(codeId!=""){
				codeId=codeId+","+CaliforniaSocialSciencesStandardsChk.getName();	
			}
			else
			{
				codeId=codeId+CaliforniaSocialSciencesStandardsChk.getName();	
			}
		}
		if(CaliforniaELDSChk.isChecked())
		{
			if(codeId!=""){
				codeId=codeId+","+CaliforniaELDSChk.getName();	
			}
			else
			{
				codeId=codeId+CaliforniaELDSChk.getName();	
			}
		}
		
		return codeId;
	}
	@UiHandler("standardsSaveButton")
	public void onClickOfstandardsSaveButton(ClickEvent event)
	{
	
		if(commonCoreChk.isChecked() || californiaStandChk.isChecked() || texasChk.isChecked()||ngssChk.isChecked()||CaliforniaSocialSciencesStandardsChk.isChecked()||CaliforniaELDSChk.isChecked()){
			getUiHandlers().updatePartyCustomField(USER_TAXONOMY_ROOT_CODE,getcheckedValue());
		}
		if(userStandardPrefcode!=null){
		if(!commonCoreChk.isChecked() && !californiaStandChk.isChecked() && !texasChk.isChecked() && !ngssChk.isChecked() && !CaliforniaSocialSciencesStandardsChk.isChecked()&& !CaliforniaELDSChk.isChecked()){
				standardsSaveButton.setEnabled(true);
				standardsSaveButton.getElement().removeClassName("disabled");
				UserSettingStandardDeleteView userSettingStandardDeleteView = new UserSettingStandardDeleteView(gooruUid,standardsEditButton,standardsSaveCancelButtonContainer,standardSavingTextLabel);
			   	userSettingStandardDeleteView.show();
				userSettingStandardDeleteView.center();
				userStandardEditView.setVisible(false);
				userStandardDefaultView.setVisible(true);
			}
		}
		else
		{
			if(!commonCoreChk.isChecked() && !californiaStandChk.isChecked() && !texasChk.isChecked()&& !ngssChk.isChecked()&& !CaliforniaSocialSciencesStandardsChk.isChecked()&& !CaliforniaELDSChk.isChecked()){
				standardsSaveButton.setEnabled(false);
				standardsSaveButton.getElement().addClassName("disabled");
			}
		}
	
		
	}
	@UiHandler("standardsCancelButton")
	public void onClickOfstandardsCancelButton(ClickEvent event)
	{
		standardsSaveCancelButtonContainer.setVisible(false);
		standardsEditButton.setVisible(true);
		userStandardEditView.setVisible(false);
		userStandardDefaultView.setVisible(true);
		getUserCodeId(userStandardPrefcode);
	
	}

	@Override
	public void getUserCodeId(List<String> list) {
		standardsSaveCancelButtonContainer.setVisible(false);
		standardsEditButton.setVisible(true);
		userStandardEditView.setVisible(false);
		userStandardDefaultView.setVisible(true);
		if(list!=null){
			if(list.size()!=0){
			lblUserMessage.setVisible(false);
			standardsSaveButton.setEnabled(true);
			standardsSaveButton.getElement().removeClassName("disabled");
			if(list.contains("CCSS")){
				commonCoreChk.setChecked(true);
				lblCommonCore.setVisible(true);
			}
			else
			{
				lblCommonCore.setVisible(false);
				commonCoreChk.setChecked(false);
			}
			if(list.contains("CASK5")){
				lblCSS.setVisible(true);
				californiaStandChk.setChecked(true);
				lblCaliforniaScience.setVisible(true);
				CSSChk.setChecked(true);
			}else{
				lblCSS.setVisible(false);
				californiaStandChk.setChecked(false);
				lblCaliforniaScience.setVisible(false);
				CSSChk.setChecked(false);
			}
			if(list.contains("CAS612")){
				lblCSS.setVisible(true);
				californiaStandChk.setChecked(true);
				lblCaliforniaScience.setVisible(true);
				CSSChk.setChecked(true);
			}else{
				lblCSS.setVisible(false);
				californiaStandChk.setChecked(false);
				lblCaliforniaScience.setVisible(false);
				CSSChk.setChecked(false);
			}
			if(list.contains("TEXAS")||list.contains("TEKS")){
				texasChk.setChecked(true);
				lblTexas.setVisible(true);
			}else{
				texasChk.setChecked(false);
				lblTexas.setVisible(false);
				
			}
			if(list.contains("NGSS")){
				ngssChk.setChecked(true);
				lblNgss.setVisible(true);
			}else{
				ngssChk.setChecked(false);
				lblNgss.setVisible(false);
				
			}
			if(list.contains("CAELD")){
				lblCaliforniaELDS.setVisible(true);
				CaliforniaELDSChk.setChecked(true);
				lblCSS.setVisible(true);
				CSSChk.setChecked(true);
				
			}else{
				lblCaliforniaELDS.setVisible(false);
				CaliforniaELDSChk.setChecked(false);
				lblCSS.setVisible(false);
				CSSChk.setChecked(false);
			}
			if(list.contains("CA")){
				lblCaliforniaSocialSciencesStandards.setVisible(true);
				CaliforniaSocialSciencesStandardsChk.setChecked(true);
				lblCSS.setVisible(true);
				CSSChk.setChecked(true);
			}else{
				lblCaliforniaSocialSciencesStandards.setVisible(false);
				CaliforniaSocialSciencesStandardsChk.setChecked(false);
				lblCSS.setVisible(false);
				CSSChk.setChecked(false);
			}
			if(list.contains("CA")||list.contains("CAELD")||list.contains("CASK5")||list.contains("CAS612"))
			{
				CSSChk.setChecked(true);
				lblCSS.setVisible(true);
			}
			else
			{
				CSSChk.setChecked(false);
				lblCSS.setVisible(false);
			}
			
		}else{
			lblCommonCore.setVisible(false);	
			lblCaliforniaScience.setVisible(false);
			lblTexas.setVisible(false);
			lblNgss.setVisible(false);
			lblCaliforniaELDS.setVisible(false);
			lblCaliforniaSocialSciencesStandards.setVisible(false);
			lblCSS.setVisible(false);
			lblUserMessage.setVisible(true);
			standardsSaveButton.setEnabled(false);
			standardsSaveButton.getElement().addClassName("disabled");
			texasChk.setChecked(false);
			californiaStandChk.setChecked(false);
			commonCoreChk.setChecked(false);
			CSSChk.setChecked(false);
			ngssChk.setChecked(false);
			
			CaliforniaSocialSciencesStandardsChk.setChecked(false);
			CaliforniaELDSChk.setChecked(false);
		}
		}
		else
		{
			lblCommonCore.setVisible(false);	
			lblCaliforniaScience.setVisible(false);
			lblTexas.setVisible(false);
			lblNgss.setVisible(false);
			lblCaliforniaELDS.setVisible(false);
			lblCaliforniaSocialSciencesStandards.setVisible(false);
			lblCSS.setVisible(false);
			lblUserMessage.setVisible(true);
			standardsSaveButton.setEnabled(false);
			standardsSaveButton.getElement().addClassName("disabled");
			texasChk.setChecked(false);
			californiaStandChk.setChecked(false);
			commonCoreChk.setChecked(false);
			ngssChk.setChecked(false);
			CaliforniaSocialSciencesStandardsChk.setChecked(false);
			CaliforniaELDSChk.setChecked(false);
			CSSChk.setChecked(false);
			
		}
			
	}
	
	@UiHandler("btnViewAdmin")
	public void clickOnAdmin(ClickEvent clickEvent){
		//String adminUrl=Window.Location.getProtocol()+"//"+Window.Location.getHost()+"/admin";
		String adminUrlnew = Window.Location.getProtocol()+"//"+Window.Location.getHost()+"/admin/signin?sessionToken="+AppClientFactory.getLoginSessionToken();
		Window.open(adminUrlnew, "_blank", "");
			
	}
	
	@Override
	public Label getStandardSavingTextLabel() {
		return standardSavingTextLabel;
	}

	@Override
	public HTMLPanel getstandardsSaveCancelButtonContainer() {
		return standardsSaveCancelButtonContainer;
	}

	@Override
	public HTMLPanel getuserStandardEditView() {
		return userStandardEditView;
	}

	@Override
	public Button getstandardsEditButton() {
		return standardsEditButton;
	}

	@Override
	public HTMLPanel getuserStandardDefaultView() {
		return userStandardDefaultView;
	}

	@Override
	public void hideEmailContainer() {
		emailPanel.setVisible(false);
	}
	@Override
	public void googleDirveStatus(boolean isConnected){
		this.isDriveConnected = isConnected;
		lblDisconnect.setVisible(isConnected);
		if (isConnected){
			btnConnect.getElement().addClassName("green");
			btnConnect.setText(i18n.GL2012());
			btnConnect.getElement().setAttribute("alt", i18n.GL2012());
			btnConnect.getElement().setAttribute("title", i18n.GL2012());
		}else{
			btnConnect.getElement().removeClassName("green");
			btnConnect.setText(i18n.GL2008());
			btnConnect.getElement().setAttribute("alt", i18n.GL2008());
			btnConnect.getElement().setAttribute("title", i18n.GL2008());
			htmlConnectedAs.setVisible(false);
		}
	}
	@Override
	public void setConnectedAs(String connectedEmailId){
		String connectedAs = StringUtil.generateMessage(i18n.GL2193(), connectedEmailId);
		StringUtil.setAttributes(htmlConnectedAs.getElement(), "htmlConnectedAs", StringUtil.removeHtml(connectedAs), StringUtil.removeHtml(connectedAs));
		htmlConnectedAs.setHTML(connectedAs);
		htmlConnectedAs.setVisible(true);
		htmlConnectedAs.getElement().getStyle().setLineHeight(3, Unit.EM);
	}

	@Override
	public void displayAdminPortal() {
		if (!AppClientFactory.isAnonymous()){
			String userRoles = AppClientFactory.getLoggedInUser().getUserRoleSetString();
			if(userRoles.contains("Content Admin") || userRoles.contains("Content_Admin") || userRoles.contains("superadmin")){
				btnViewAdmin.setVisible(true);
			}else{
				btnViewAdmin.setVisible(false);
				
			}
		}else {
			btnViewAdmin.setVisible(false);
		}
	}
}