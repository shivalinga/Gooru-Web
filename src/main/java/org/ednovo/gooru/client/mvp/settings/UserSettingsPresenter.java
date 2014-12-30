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
 * @fileName : UserSettingsPresenter.java 
 *
 * @description : 
 *
 * @version :1.0
 *
 * @date: APR 18 2013

 * @Author Gooru Team 
 * 
 * Reviewer Gooru Team
 *
 */
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SeoTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.event.InvokeLoginEvent;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BasePlacePresenter;
import org.ednovo.gooru.client.mvp.home.event.HeaderTabType;
import org.ednovo.gooru.client.mvp.home.event.HomeEvent;
import org.ednovo.gooru.client.mvp.home.event.SetUserProfileImageEvent;
import org.ednovo.gooru.client.mvp.home.library.events.StandardPreferenceSettingEvent;
import org.ednovo.gooru.client.mvp.image.upload.ImageUploadPresenter;
import org.ednovo.gooru.client.mvp.search.event.ConfirmStatusPopupEvent;
import org.ednovo.gooru.client.mvp.search.event.SetFooterEvent;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderEvent;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderZIndexEvent;
import org.ednovo.gooru.client.service.UserServiceAsync;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.code.CodeDo;
import org.ednovo.gooru.shared.model.code.LibraryCodeDo;
import org.ednovo.gooru.shared.model.code.ProfileCodeDo;
import org.ednovo.gooru.shared.model.drive.GoogleDriveDo;
import org.ednovo.gooru.shared.model.user.BiographyDo;
import org.ednovo.gooru.shared.model.user.FilterSettings;
import org.ednovo.gooru.shared.model.user.GoogleToken;
import org.ednovo.gooru.shared.model.user.ProfileDo;
import org.ednovo.gooru.shared.model.user.ProfilePageDo;
import org.ednovo.gooru.shared.model.user.SettingDo;
import org.ednovo.gooru.shared.model.user.UserDo;
import org.ednovo.gooru.shared.model.user.V2UserDo;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

public class UserSettingsPresenter
		extends
		BasePlacePresenter<IsUserSettingsView, UserSettingsPresenter.IsUserSettingProxy>
		implements UserSettingsUiHandlers {

	// private static final String
	// PROFILE_IMAGE_REPOSITORY_URL="http://profile-demo.s3.amazonaws.com/profile-qalive/";
	// private static final String
	// PROFILE_IMAGE_REPOSITORY_URL="http://profile-demo.s3.amazonaws.com/profile-prod/";

	@Inject
	private UserServiceAsync userService;

	private ImageUploadPresenter imageUploadPresenter;

	private static final String GOORU_UID = "gooruuid";

	private SimpleAsyncCallback<SettingDo> userProfileAsyncCallback;

	private SimpleAsyncCallback<ProfilePageDo> userProfilePageAsyncCallback;

	private SimpleAsyncCallback<BiographyDo> userProfileBiographyAsyncCallback;
	
	private SimpleAsyncCallback<V2UserDo> userV2ProfilePageAsyncCallback;
	
	private static final String GOOGLE_REFRESH_TOKEN = "google-refresh-token";
	private static final String GOOGLE_ACCESS_TOKEN = "google-access-token";

	Date dob;
	SettingDo user = null;
	private boolean isUserUnder13 = false;
	private SavePopup savePopup;

	public SettingDo getUser() {
		return user;
	}

	public void setUser(SettingDo user) {
		this.user = user;
	}

	BiographyDo biographyDo;
	String gooruUid;
	String aboutUs;
	String userName;
	String gender;
	private String Refersh_emailId;
	private static final String USER_META_ACTIVE_FLAG = "0";
	
	String EMAIL_REGEX = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

	private String emailId = "";

	@ProxyCodeSplit
	@NameToken(PlaceTokens.SETTINGS)
	public interface IsUserSettingProxy extends
			ProxyPlace<UserSettingsPresenter> {

	}
	
	private MessageProperties i18n = GWT.create(MessageProperties.class);

	@Inject
	public UserSettingsPresenter(final IsUserSettingsView view,
			final IsUserSettingProxy proxy,
			ImageUploadPresenter imageUploadPresenter) {
		super(view, proxy);
		getView().setUiHandlers(this);
		Window.enableScrolling(true);
		this.imageUploadPresenter = imageUploadPresenter;
		addRegisteredHandler(SetUserProfileImageEvent.TYPE, this);
	}

	@Override
	public void onReveal() {
		Window.scrollTo(0, 0);
		AppClientFactory.setBrowserWindowTitle(SeoTokens.SETTINGS_TITLE);
		AppClientFactory
				.setMetaDataDescription(SeoTokens.HOME_META_DESCRIPTION);
		getView().clearPanels();
		getView().getAboutUsContainer().setVisible(false);
		
		if (AppClientFactory.isAnonymous()){
			AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.HOME);
		}
		
		boolean isConfirmStatus = true;
		String newMailId = AppClientFactory.getPlaceManager()
				.getRequestParameter("newMailId");
//		Cookies.setCookie("GOOGLE_ACCESS_TOKEN", "ya29.PADXXYiamS8JHxsAAADsdCb743SQMuoXVuZlTw5kC3kLVP_-UThz6jTa0kv7NA");
		//Make an API call to get the accesstoken using refresh token.
		
		final String refresh_token = Cookies.getCookie(GOOGLE_REFRESH_TOKEN) !=null && !Cookies.getCookie(GOOGLE_REFRESH_TOKEN).equalsIgnoreCase("") ? Cookies.getCookie(GOOGLE_REFRESH_TOKEN) : null;
//		StringUtil.consoleLog("refresh token : "+refresh_token);
		if (refresh_token != null){
			
			AppClientFactory.getInjector().getResourceService().refreshGoogleAccessToken(refresh_token, new SimpleAsyncCallback<GoogleToken>() {

				@Override
				public void onSuccess(GoogleToken result) {
//					StringUtil.consoleLog("refreshGoogleAccessToken : Success");
					final String access_token = result.getAccess_token() !=null && !result.getAccess_token().equalsIgnoreCase("") ? result.getAccess_token() : null;
//					StringUtil.consoleLog("access_token : Success : "+access_token);
					if (access_token !=null ){
						
						AppClientFactory.getInjector().getResourceService().getGoogleDriveFilesList(null,null,new SimpleAsyncCallback<GoogleDriveDo>() {
							@Override
							public void onSuccess(GoogleDriveDo googleDriveDo) {
								if(googleDriveDo!=null){
									if (googleDriveDo.getError()!=null && googleDriveDo.getError().getCode() == 401){
//										StringUtil.consoleLog("access_token : 401");
										getView().googleDirveStatus(false);
									}else if (googleDriveDo.getError()!=null && googleDriveDo.getError().getCode()==403){
//										StringUtil.consoleLog("access_token : 403");
										getView().googleDirveStatus(false);
									}else{
//										StringUtil.consoleLog("access_token : no error");
										UserDo user = AppClientFactory.getLoggedInUser();
										user.setAccessToken(access_token);
										AppClientFactory.setLoggedInUser(user);
										
										getView().googleDirveStatus(true);
									}
								}else{
//									StringUtil.consoleLog("google drive file list empty");
									getView().googleDirveStatus(false);
								}
							}
						});
					}else{
//						StringUtil.consoleLog("refresh token null");
						getView().googleDirveStatus(false);
					}
				}
			});
		}else{
			getView().googleDirveStatus(false);
		}
		
		String userId = AppClientFactory.getPlaceManager().getRequestParameter(
				"userId");
		String confirmStatus = AppClientFactory.getPlaceManager()
				.getRequestParameter("confirmStatus");
		if (confirmStatus != null && confirmStatus.equalsIgnoreCase("true")) {
			isConfirmStatus = true;
		} else {
			isConfirmStatus = false;
		}

		if (newMailId != null && userId != null && isConfirmStatus != false) {
			emailId = newMailId;
			if (!AppClientFactory.getLoggedInUser().getUserUid().equals(AppClientFactory.GOORU_ANONYMOUS)) {
				saveEmail(isConfirmStatus);
			}
			else
			{
				AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.HOME);
				AppClientFactory.fireEvent(new InvokeLoginEvent());
			}
		} else {
			displaySettingsPage();
		}
		Window.enableScrolling(true);
		AppClientFactory.fireEvent(new SetHeaderZIndexEvent(0, true));
		if (AppClientFactory.getLoggedInUser().getConfirmStatus() == 0 && isConfirmStatus){
			String gooruUid = AppClientFactory.getGooruUid();
			String token = AppClientFactory.getLoginSessionToken();
			Map<String, String> params = new HashMap<String, String>();
			params.put("confirmUser", "true");
			params.put("gooruUid", AppClientFactory.getLoggedInUser().getGooruUId());
			// Confirm User and remove/hide Not confirmed Popup.
			AppClientFactory.getInjector().getUserService().updateUserDetails(gooruUid, token, params, new SimpleAsyncCallback<ProfileDo>() {
				@Override
				public void onSuccess(ProfileDo result) {
					//Display thanks popup if required.
					//Set Visiblity to false
					AppClientFactory.setLoggedInUser(result.getUser());
					boolean isConfirmed = result.getUser().getConfirmStatus() == 1 ? true : false;
					if (isConfirmed){
						AppClientFactory.fireEvent(new ConfirmStatusPopupEvent(isConfirmed));
					}
				}
			});
		}
		AppClientFactory.fireEvent(new ConfirmStatusPopupEvent(false));
	}

	void displaySettingsPage() {
		this.getUserService().getV2UserProfileDetails(
				AppClientFactory.getPlaceManager().getRequestParameter(
						GOORU_UID), getUserV2ProfilePageAsyncCallback());
		AppClientFactory.fireEvent(new HomeEvent(HeaderTabType.NONE));
		AppClientFactory.fireEvent(new SetFooterEvent(AppClientFactory
				.getPlaceManager().getCurrentPlaceRequest().getNameToken()));
		this.getUserService().getUserProfilePage(
				AppClientFactory.getPlaceManager().getRequestParameter(
						GOORU_UID), getUserProfilePageAsyncCallback());
		getView().setUserProfileImageUrl("EMPTY");
	}

	@Override
	public void onBind() {
		super.onBind();

		getView().clearPanels();
		//New V2 Api async call for get and updating User details (UserName, First Name, Last Name, Gender)
		setUserV2ProfilePageAsyncCallback(new SimpleAsyncCallback<V2UserDo>() {

			@Override
			public void onSuccess(V2UserDo user) {
				
				// For child account set the edit button visibility false.
				// for some old account, AccountTypeId is null.
				getView().getAccountSavingText().setVisible(false);
				getView().getEditButtonAccount().setVisible(true);
				if (user.getUser() != null) {
					if (user.getUser().getAccountTypeId() != null) {
						getView().getEditEmailButton().setVisible(
								user.getUser().getAccountTypeId() == 2 ? false
										: true);
						getView().setChildAccount(
								user.getUser().getAccountTypeId() == 2 ? true
										: false);
					} else {
						getView().getEditEmailButton().setVisible(true);
						getView().setChildAccount(false);
					}
					//TODO need to set the issue
//					getView().setSettingDo(user);
					// getView().setData(user);
					//TODO need to convert to date object and this is required to validate child account
//					dob = user.getDateOfBirth();
					if(user.getDateOfBirth()!=null){
					Date date = new Date(Long.parseLong(user.getDateOfBirth()));
					dob = date;
					}
					gooruUid = user.getUser().getGooruUId();
					userName = user.getUser().getUsername();
					FilterSettings filterSettings =  AppClientFactory.getLoggedInUser().getSettings();
					
					UserDo userDo =  user.getUser();
					userDo.setSettings(filterSettings);
					userDo.setMeta(AppClientFactory.getLoggedInUser().getMeta());
					userDo.setToken(AppClientFactory.getLoggedInUser().getToken());
					AppClientFactory.setLoggedInUser(userDo);
					AppClientFactory.fireEvent(new SetHeaderEvent(userDo));
					if (user.getGender() != null) {

						gender = user.getGender() != null ? user.getGender()
								.getGenderId() : "";
						if (gender.equalsIgnoreCase("M")) {
							getView().getLbMale().setStyleName(
									getView().getSelectedButton());
						} else if (gender.equalsIgnoreCase("F")) {
							getView().getLbFemale().setStyleName(
									getView().getSelectedButton());
						} else if (gender.equalsIgnoreCase("O")) {
							getView().getLbOther().setStyleName(
									getView().getSelectedButton());
						} else if (gender.equalsIgnoreCase("X")) {
							getView().getLbShare().setStyleName(
									getView().getSelectedButton());
						}

						if (user.getGender().getName() != null) {
							String gender = user.getGender().getName()
									.substring(0, 1).toUpperCase()
									+ user.getGender().getName().substring(1);
							if ("Do not wish to share".equalsIgnoreCase(gender)) {
								String gender1 = gender.replace(
										i18n.GL1199(),
										i18n.GL0812());
								getView().getGenderText().setText(gender1);
							} else {
								getView().getGenderText().setText(gender);
							}
						} else {
							getView().getGenderText().setText("");
						}
					}
					// getView().getGenderText().setText(user.getGender().getName().substring(0,1).toUpperCase()+user.getGender().getName().substring(1));

					if (user.getAboutMe() != null) {
						getView().noAboutUsContainer().setVisible(false);
						getView().getProfileBiographyEditUC().setText(
								user.getAboutMe());
					} else {
						getView().noAboutUsContainer().setVisible(true);
					}
					getView().getLbUName()
							.setText(user.getUser().getUsername());
//					getView().getLbUName().setVisible(false);
					getView().getTxtUserName().setText(user.getUser().getUsername());
					getView().getTxtUserName().setVisible(false);
					getView().getTbFirstName().setText(
							user.getUser().getFirstName());
					getView().getTbLastName().setText(
							user.getUser().getLastName());
					getView().getLbUserName().setText(
							user.getUser().getUsername());
					getView().hideuserDetailsContainerOnClickOfTab();
					if (user.getUser().getLoginType()
							.equalsIgnoreCase("credential")) {					
						if(user.getUserType() != null && user.getUserType().length() > 1)
						{
						user.setUserType(user.getUserType().substring(0,1).toUpperCase()+user.getUserType().substring(1, user.getUserType().length()));
						}
						if (dob != null) {
							getView().getLbUName().getElement()
									.setAttribute("dob", "" + dob);
							int age = getAge(dob);
							getView().getLbUName().getElement()
									.setAttribute("date", "" + age);
					
							if (age < 13) 
							{
								getView().getLbRole().setText(i18n.GL0417());
							} 
							else 
							{
								getView().getLbRole().setText(
										user.getUserType());
							}
						} else if (user.getUser().getAccountTypeId() != null) {
							if (user.getUser().getAccountTypeId() == 2) {
								getView().getLbRole().setText(i18n.GL0417());
								
							} else {
								getView().getLbRole().setText(
										user.getUserType());
							}
						} else {
							getView().getLbRole().setText(user.getUserType());
						}
					} else {
						getView().getLbRole().setText(
								user.getUserType() != null ? user.getUserType()
										: "");
					}

					if (user.getUser().getEmailId() != null) {
						Refersh_emailId=user.getUser().getEmailId();
						boolean isValidEmail = user.getUser().getEmailId().matches(EMAIL_REGEX);
						if(isValidEmail){
							getView().getLbEmail().setText(user.getUser().getEmailId());
							//StringUtil.consoleLog("setEmailId 1"+user.getExternalId());
							
							
						}else{
							getView().hideEmailContainer();
						}
						
					} else {
						if(user.getUser().getAccountTypeId() != 2){
							if(user.getUser().getEmailId()!=null){
								Refersh_emailId=user.getUser().getEmailId();
								boolean isValidEmail = user.getUser().getEmailId().matches(EMAIL_REGEX);
								if(isValidEmail){
									//StringUtil.consoleLog("setEmailId 2"+user.getUser().getEmailId());
									
									
									getView().getLbEmail().setText(
											user.getUser().getEmailId());
									}
								}else{
									getView().hideEmailContainer();
								}
							}
						
					}
					// getView().getLbEmail().setText(user.getExternalId());
					getView().getLbName().setText(
							user.getUser().getFirstName() + " "
									+ user.getUser().getLastName());
					
					
					if (user.getUser().getLoginType() != null) {
						if (user.getUser().getLoginType().trim()
								.equalsIgnoreCase("apps")) {
							getView().getForgetPassword().setVisible(false);
							getView().getForgetPasswordMsg().setVisible(true);
							getView().getEditEmailButton().setVisible(false);
						} else if (user.getUser().getLoginType().trim()
								.equalsIgnoreCase("Credential")) {
							getView().getForgetPassword().setVisible(true);
							getView().getForgetPasswordMsg().setVisible(false);
							if(user.getUser().getAccountTypeId() != null && user.getUser().getAccountTypeId() != 2){
								getView().getEditEmailButton().setVisible(true);
							}
						}
					}

					if (user.getUser().getLoginType()
							.equalsIgnoreCase("credential")) {
						if (dob != null) {
							getView().getLbUName().getElement()
									.setAttribute("dob", "" + dob);
							int age = getAge(dob);
							getView().getLbUName().getElement()
									.setAttribute("date", "" + age);
							if (age < 13) {
								getView().getAboutUsContainer().setVisible(
										false);
								setUserUnder13(true);
							} else {
								getView().getAboutUsContainer()
										.setVisible(true);
								setUserUnder13(false);
							}
						} else if (user.getUser().getAccountTypeId() != null) {
							// All child accounts have account type id is set to
							// 2.
							// Based on this will not show PPP for child
							// account.,
							// but we need to look when child trun >13.
							if (user.getUser().getAccountTypeId() == 2) {
								getView().getAboutUsContainer().setVisible(
										false);
								
								setUserUnder13(true);
							} else {
								getView().getAboutUsContainer()
										.setVisible(true);
								setUserUnder13(false);
							}
						} else {
							getView().getAboutUsContainer().setVisible(true);
							setUserUnder13(false);
						}
					} else {
						getView().getAboutUsContainer().setVisible(true);
						setUserUnder13(false);
					}
					getView().setUserProfileImageUrl(
							AppClientFactory.getLoggedInUser().getSettings()
									.getProfileImageUrl()
									+ user.getUser().getGooruUId()
									+ "-158x158.png");

					getView().setData(user);
				} else {
					
				}
				updateRefershToken();
				getView().displayAdminPortal();
				/**
				 * This RPC is to get the User profile Details(grade value)
				 */
				AppClientFactory
						.getInjector()
						.getUserService()
						.getUserProfileV2Details(gooruUid,
								USER_META_ACTIVE_FLAG,
								new SimpleAsyncCallback<ProfileDo>() {

									@Override
									public void onSuccess(ProfileDo profileObj) {
										getView().setProfileData(profileObj);
										//getView().getUserCodeId(profileObj.getUser().getMeta().getTaxonomyPreference().getCode());
										AppClientFactory.fireEvent(new StandardPreferenceSettingEvent(profileObj.getUser().getMeta().getTaxonomyPreference().getCode()));
									}

								});
				/**
				 * This RPC is to get the Courses
				 */
				AppClientFactory
						.getInjector()
						.getTaxonomyService()
						.getCourse(
								new SimpleAsyncCallback<List<LibraryCodeDo>>() {
									@Override
									public void onSuccess(
											List<LibraryCodeDo> result) {
										getView().setCourseList(result);
									}
								});

			
			}
		});
		
		
		
		setUserProfilePageAsyncCallback(new SimpleAsyncCallback<ProfilePageDo>() {

			@Override
			public void onSuccess(ProfilePageDo result) {
				if (result.getOptionalValue() == "true"
						|| result.getOptionalValue().equalsIgnoreCase("true")) {
					getView().getSeeMyPageButton().setVisible(true);
					getView().getprofileOnButton();

				} else {
					getView().getSeeMyPageButton().setVisible(false);
					getView().getProfileOffButton();

				}
				// Click Event to see my page
				if (result.getOptionalValue() == "true"
						|| result.getOptionalValue().equalsIgnoreCase("true")) {
					getView().getSeeMyPageButton().addClickHandler(
							new ClickHandler() {

								@Override
								public void onClick(ClickEvent event) {
									Map<String, String> params = new HashMap<String, String>();
									params.put("id", gooruUid);
									params.put("user", userName);
									MixpanelUtil.Click_On_SeemyPage();
									AppClientFactory.getPlaceManager()
											.revealPlace(
													PlaceTokens.PROFILE_PAGE,
													params);
								}
							});
				}
			}
		});
		setUserProfileBiographyAsyncCallback(new SimpleAsyncCallback<BiographyDo>() {

			@Override
			public void onSuccess(BiographyDo result) {
				if (result.getAboutMe() != null) {
					getView().getProfileBiographyEditUC().setText(
							result.getAboutMe());
					getView().getSettingDo().setAboutMe(result.getAboutMe());
				}

			}
		});

		setUserProfileAsyncCallback(new SimpleAsyncCallback<SettingDo>() {
			@Override
			public void onSuccess(final SettingDo user) {
				// For child account set the edit button visibility false.
				// for some old account, AccountTypeId is null.

				getView().getAccountSavingText().setVisible(false);
				getView().getEditButtonAccount().setVisible(true);
				if (user.getUser() != null) {
					if (user.getUser().getAccountTypeId() != null) {
						getView().getEditEmailButton().setVisible(
								user.getUser().getAccountTypeId() == 2 ? false
										: true);
						getView().setChildAccount(
								user.getUser().getAccountTypeId() == 2 ? true
										: false);
					} else {
						getView().getEditEmailButton().setVisible(true);
						getView().setChildAccount(false);
					}

					getView().setSettingDo(user);
					// getView().setData(user);
					dob = user.getDateOfBirth();
					gooruUid = user.getUser().getGooruUId();
					userName = user.getUser().getUsername();

					if (user.getGender() != null) {

						gender = user.getGender() != null ? user.getGender()
								.getGenderId() : "";
						if (gender.equalsIgnoreCase("M")) {
							getView().getLbMale().setStyleName(
									getView().getSelectedButton());
						} else if (gender.equalsIgnoreCase("F")) {
							getView().getLbFemale().setStyleName(
									getView().getSelectedButton());
						} else if (gender.equalsIgnoreCase("O")) {
							getView().getLbOther().setStyleName(
									getView().getSelectedButton());
						} else if (gender.equalsIgnoreCase("X")) {
							getView().getLbShare().setStyleName(
									getView().getSelectedButton());
						}

						if (user.getGender().getName() != null) {
							String gender = user.getGender().getName()
									.substring(0, 1).toUpperCase()
									+ user.getGender().getName().substring(1);
							if ("Do not wish to share".equalsIgnoreCase(gender)) {
								String gender1 = gender.replace(
										i18n.GL1199(),
										i18n.GL0812());
								getView().getGenderText().setText(gender1);
							} else {
								getView().getGenderText().setText(gender);
							}
						} else {
							getView().getGenderText().setText("");
						}
					}
					// getView().getGenderText().setText(user.getGender().getName().substring(0,1).toUpperCase()+user.getGender().getName().substring(1));

					if (user.getAboutMe() != null) {
						getView().noAboutUsContainer().setVisible(false);
						getView().getProfileBiographyEditUC().setText(
								user.getAboutMe());
					} else {
						getView().noAboutUsContainer().setVisible(true);
					}
					getView().getLbUName()
							.setText(user.getUser().getUsername());
					getView().getTbFirstName().setText(
							user.getUser().getFirstName());
					getView().getTbLastName().setText(
							user.getUser().getLastName());

					if (user.getUser().getLoginType()
							.equalsIgnoreCase("credential")) {
						if (dob != null) {
							getView().getLbUName().getElement()
									.setAttribute("dob", "" + dob);
							int age = getAge(dob);
							getView().getLbUName().getElement()
									.setAttribute("date", "" + age);
							if (age < 13) {
								getView().getLbRole().setText(i18n.GL0417());
							} else {
								getView().getLbRole().setText(
										user.getUserType());
							}
						} else if (user.getUser().getAccountTypeId() != null) {
							if (user.getUser().getAccountTypeId() == 2) {
								getView().getLbRole().setText(i18n.GL0417());
								
							} else {
								getView().getLbRole().setText(
										user.getUserType());
							}
						} else {
							getView().getLbRole().setText(user.getUserType());
						}
					} else {
						getView().getLbRole().setText(
								user.getUserType() != null ? user.getUserType()
										: "");
					}

					if (user.getUser().getEmailId() != null) {
						getView().getLbEmail().setText(user.getUser().getEmailId());
						//StringUtil.consoleLog("setEmailId 3"+user.getExternalId());
						Refersh_emailId = user.getUser().getEmailId();
						
						
					} else {
						if(user.getUser().getAccountTypeId() != 2){
						getView().getLbEmail().setText(
								user.getUser().getEmailId());
						//StringUtil.consoleLog("setEmailId 4"+user.getUser().getEmailId());
						Refersh_emailId = user.getUser().getEmailId();
						
						}
					}
					// getView().getLbEmail().setText(user.getExternalId());
					getView().getLbName().setText(
							user.getUser().getFirstName() + " "
									+ user.getUser().getLastName());
					getView().getLbUserName().setText(
							user.getUser().getUsername());
					if (user.getUser().getLoginType() != null) {
						if (user.getUser().getLoginType().trim()
								.equalsIgnoreCase("apps")) {
							getView().getForgetPassword().setVisible(false);
							getView().getForgetPasswordMsg().setVisible(true);
							getView().getEditEmailButton().setVisible(false);
						} else if (user.getUser().getLoginType().trim()
								.equalsIgnoreCase("Credential")) {
							getView().getForgetPassword().setVisible(true);
							getView().getForgetPasswordMsg().setVisible(false);
							getView().getEditEmailButton().setVisible(true);
						}
					}

					if (user.getUser().getLoginType()
							.equalsIgnoreCase("credential")) {
						if (dob != null) {
							getView().getLbUName().getElement()
									.setAttribute("dob", "" + dob);
							int age = getAge(dob);
							getView().getLbUName().getElement()
									.setAttribute("date", "" + age);
							if (age < 13) {
								getView().getAboutUsContainer().setVisible(
										false);
								setUserUnder13(true);
							} else {
								getView().getAboutUsContainer()
										.setVisible(true);
								setUserUnder13(false);
							}
						} else if (user.getUser().getAccountTypeId() != null) {
							// All child accounts have account type id is set to
							// 2.
							// Based on this will not show PPP for child
							// account.,
							// but we need to look when child trun >13.
							if (user.getUser().getAccountTypeId() == 2) {
								getView().getAboutUsContainer().setVisible(
										false);
								
								setUserUnder13(true);
							} else {
								getView().getAboutUsContainer()
										.setVisible(true);
								setUserUnder13(false);
							}
						} else {
							getView().getAboutUsContainer().setVisible(true);
							setUserUnder13(false);
						}
					} else {
						getView().getAboutUsContainer().setVisible(true);
						setUserUnder13(false);
					}
					getView().setUserProfileImageUrl(
							AppClientFactory.getLoggedInUser().getSettings()
									.getProfileImageUrl()
									+ user.getUser().getGooruUId()
									+ "-158x158.png");

					getView().setData(user);
				} else {
					
				}
				/**
				 * This RPC is to get the User profile Details(grade value)
				 */
				AppClientFactory
						.getInjector()
						.getUserService()
						.getUserProfileV2Details(gooruUid,
								USER_META_ACTIVE_FLAG,
								new SimpleAsyncCallback<ProfileDo>() {

									@Override
									public void onSuccess(ProfileDo profileObj) {
										getView().setProfileData(profileObj);
									}

								});
				/**
				 * This RPC is to get the Courses
				 */
				AppClientFactory
						.getInjector()
						.getTaxonomyService()
						.getCourse(
								new SimpleAsyncCallback<List<LibraryCodeDo>>() {
									@Override
									public void onSuccess(
											List<LibraryCodeDo> result) {
										getView().setCourseList(result);
									}
								});

			}

		});
		UpdateGender();
	}

	// Label work like radio button functionality
	public void UpdateGender() {
		getView().getLbMale().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getView().getLbFemale()
						.setStyleName(getView().getRadioButton());
				getView().getLbOther().setStyleName(getView().getRadioButton());
				getView().getLbShare().setStyleName(getView().getRadioButton());
				getView().getLbMale().setStyleName(
						getView().getSelectedButton());

			}
		});
		getView().getLbShare().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getView().getLbMale().setStyleName(getView().getRadioButton());
				getView().getLbFemale()
						.setStyleName(getView().getRadioButton());
				getView().getLbOther().setStyleName(getView().getRadioButton());
				getView().getLbShare().setStyleName(
						getView().getSelectedButton());

			}
		});
		getView().getLbFemale().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getView().getLbMale().setStyleName(getView().getRadioButton());
				getView().getLbOther().setStyleName(getView().getRadioButton());
				getView().getLbShare().setStyleName(getView().getRadioButton());
				getView().getLbFemale().setStyleName(
						getView().getSelectedButton());
			}
		});
		getView().getLbOther().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getView().getLbMale().setStyleName(getView().getRadioButton());
				getView().getLbFemale()
						.setStyleName(getView().getRadioButton());
				getView().getLbShare().setStyleName(getView().getRadioButton());
				getView().getLbOther().setStyleName(
						getView().getSelectedButton());
			}
		});
	}

	private FocusWidget getLbMale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onHide() {
		super.onHide();
		getView().closeAllOpenedPopUp();
		imageUploadPresenter.getView().closeImageUploadWidget();
	}

	public void setUserProfileAsyncCallback(
			SimpleAsyncCallback<SettingDo> userProfileAsyncCallback) {
		this.userProfileAsyncCallback = userProfileAsyncCallback;
	}

	public UserServiceAsync getUserService() {
		return userService;
	}

	public SimpleAsyncCallback<SettingDo> getUserprofileAsyncCallback() {
		return userProfileAsyncCallback;
	}

	public SimpleAsyncCallback<BiographyDo> getUserProfileBiographyAsyncCallback() {
		return userProfileBiographyAsyncCallback;
	}

	public void setUserProfileBiographyAsyncCallback(
			SimpleAsyncCallback<BiographyDo> userProfileBiographyAsyncCallback) {
		this.userProfileBiographyAsyncCallback = userProfileBiographyAsyncCallback;
	}

	public SimpleAsyncCallback<ProfilePageDo> getUserProfilePageAsyncCallback() {
		return userProfilePageAsyncCallback;
	}

	public void setUserProfilePageAsyncCallback(
			SimpleAsyncCallback<ProfilePageDo> userProfilePageAsyncCallback) {
		this.userProfilePageAsyncCallback = userProfilePageAsyncCallback;
	}

	@Override
	public String getViewToken() {
		return null;
	}

	@Override
	public void saveSettingsInformation() {
		// String emailValue="";
		Map<String, String> updateUserDetails = new HashMap<String, String>();
		String fnValue = getView().getTbFirstName().getText().trim();
		String lnValue = getView().getTbLastName().getText().trim();
		String userName = getView().getTxtUserName().getText().trim();

		updateUserDetails.put("firstName", fnValue);
		updateUserDetails.put("lastName", lnValue);
		
		if (getView().isUserNameChanged()){
			if (getView().isValidUserName()){
				updateUserDetails.put("username", userName);
			}
		}
		
		// updateUserDetails.put("externalId", emailValue);
		if (getView().getLbRole().getText().equalsIgnoreCase("Student")) {
			updateUserDetails.put("userType", "Student");
		} else if (getView().getLbRole().getText().equalsIgnoreCase("Teacher")) {
			updateUserDetails.put("userType", "Teacher");
		} else if (getView().getLbRole().getText().equalsIgnoreCase("Parent")) {
			updateUserDetails.put("userType", "Parent");
		} else if (getView().getLbRole().getText().equalsIgnoreCase("Other")) {
			updateUserDetails.put("userType", "Other");
		}
		String gender="";
		if (getView().getLbFemale().getStyleName()
				.equals(getView().getSelectedButton())) {
			updateUserDetails.put("gender", "F");
			gender = "F";
		} else if (getView().getLbMale().getStyleName()
				.equals(getView().getSelectedButton())) {
			updateUserDetails.put("gender", "M");
			gender = "M";
		} else if (getView().getLbOther().getStyleName()
				.equals(getView().getSelectedButton())) {
			updateUserDetails.put("gender", "O");
			gender = "O";
		} else if (getView().getLbShare().getStyleName()
				.equals(getView().getSelectedButton())) {
			updateUserDetails.put("gender", "X");
			gender = "X";
		}
		
//		updateUserDetails.put("aboutMe", getView().getProfileBiographyEditUC()
//				.getText());
//		this.getUserService().updateProfileSettings(
//				AppClientFactory.getPlaceManager().getRequestParameter(
//						GOORU_UID), updateUserDetails,
//				getUserprofileAsyncCallback());
		
		this.getUserService().updateV2ProfileDo("", "", fnValue, lnValue, "", "", userName,gender, false, getUserV2ProfilePageAsyncCallback());

	}

	@Override
	public void updateProfileVisibilitySetting(String optionalValue) {
		getUserService().updateUserProfileVisibility(
				AppClientFactory.getPlaceManager().getRequestParameter(
						GOORU_UID), optionalValue,
				getUserProfilePageAsyncCallback());
	}

	@Override
	public void updateUserBiography(String biography, String role,
			String firstName, String lastName, String gender) {
		getUserService().updateProfileBiography(
				AppClientFactory.getPlaceManager().getRequestParameter(
						GOORU_UID), biography, role, firstName, lastName,
				this.gender, getUserProfileBiographyAsyncCallback());
	}

	private int getAge(Date birthDate) {
		long ageInMillis = new Date().getTime() - birthDate.getTime();
		Date age = new Date(ageInMillis);
		return age.getYear() - 70;
	}

	public void showUploadProfileImageWidget() {
		imageUploadPresenter.showUploadTypeWidgets(isUserUnder13());
		addToPopupSlot(imageUploadPresenter);
		imageUploadPresenter.setProfileImage(true);
		imageUploadPresenter.setCollectionImage(false);
		imageUploadPresenter.setEditResourceImage(false);
	}

	@Override
	public void addCourse(Set<ProfileCodeDo> profileCodeDo) {
		AppClientFactory
				.getInjector()
				.getProfilePageService()
				.addCourseUserProfile(profileCodeDo, "settings",
						new SimpleAsyncCallback<Void>() {
							@Override
							public void onSuccess(Void result) {

							}
						});
	}

	@Override
	public void deleteCourse(CodeDo codeDo) {
		AppClientFactory
				.getInjector()
				.getProfilePageService()
				.deleteCourseUserProfile(codeDo, "settings",
						new SimpleAsyncCallback<Void>() {
							@Override
							public void onSuccess(Void result) {

							}
						});
	}

	@Override
	public void setUserProfileImage(String imageUrl) {
		getView().setUserProfileImageUrl(imageUrl);

	}

	public boolean isUserUnder13() {
		return isUserUnder13;
	}

	public void setUserUnder13(boolean isUserUnder13) {
		this.isUserUnder13 = isUserUnder13;
	}

	@Override
	public void saveEmail(final boolean isEmailConfirmed) {
		String emailValue = "";

		if (isEmailConfirmed == true) {
			emailValue = emailId;
		} else {
			getView().getSavingTextLabel().setVisible(true);
			Map<String, String> updateUserDetails = new HashMap<String, String>();
			if (getView().getLbEmail().getText() != null
					&& getView().getLbEmail().getText() != "") {
				emailValue = getView().getLbEmail().getText().trim();
			}
		}

		AppClientFactory
				.getInjector()
				.getUserService()
				.updateNewEmailStatus(emailValue, isEmailConfirmed,
						new SimpleAsyncCallback<Void>() {
							@Override
							public void onSuccess(Void Result) {
								if (isEmailConfirmed) {
									displaySettingsPage();
								} else {
									if (savePopup != null
											&& savePopup.isShowing()) {
										savePopup.hide();
									}
									savePopup = new SavePopup();
									getView().getSavingTextLabel().setVisible(
											false);
									getView().getEditEmailButton().setVisible(
											true);
									getView().getEmailTextConfirmation()
											.setVisible(true);
								}
								AppClientFactory.fireEvent(new ConfirmStatusPopupEvent(false));
							}

						});
	}

	/** 
	 * This method is to get the userV2ProfilePageAsyncCallback
	 */
	public SimpleAsyncCallback<V2UserDo> getUserV2ProfilePageAsyncCallback() {
		return userV2ProfilePageAsyncCallback;
	}

	/** 
	 * This method is to set the userV2ProfilePageAsyncCallback
	 */
	public void setUserV2ProfilePageAsyncCallback(
			SimpleAsyncCallback<V2UserDo> userV2ProfilePageAsyncCallback) {
		this.userV2ProfilePageAsyncCallback = userV2ProfilePageAsyncCallback;
	}

	@Override
	public void updatePartyCustomField(String optionKey, String optionValue) {
		getView().getStandardSavingTextLabel().setText(i18n.GL0808());
		getView().getstandardsSaveCancelButtonContainer().setVisible(false);
		getView().getstandardsEditButton().setVisible(false);
		AppClientFactory.getInjector().getUserService().updatePartyCustomField(gooruUid,optionKey,optionValue,new SimpleAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
					getView().getStandardSavingTextLabel().setText("");
					getView().getuserStandardEditView().setVisible(false);
					getView().getuserStandardDefaultView().setVisible(true);
					getView().getstandardsEditButton().setVisible(true);
					AppClientFactory.getInjector().getUserService().getUserProfileV2Details(gooruUid,
							USER_META_ACTIVE_FLAG,
							new SimpleAsyncCallback<ProfileDo>() {

								@Override
								public void onSuccess(final ProfileDo profileObj) {
									AppClientFactory.fireEvent(new StandardPreferenceSettingEvent(profileObj.getUser().getMeta().getTaxonomyPreference().getCode()));
								//	getView().getUserCodeId(profileObj.getUser().getMeta().getTaxonomyPreference().getCode());
								}

							});
				}
		});
		
	}

	
	public void updateRefershToken() {
		final String refreshToken = AppClientFactory.getLoggedInUser().getRefreshToken();
		if(refreshToken==null){
			AppClientFactory.getInjector().getUserService().getRefershToken(AppClientFactory.getLoggedInUser().getGooruUId(),new SimpleAsyncCallback<String>() {
				@Override
				public void onSuccess(String result) {
					UserDo user = AppClientFactory.getLoggedInUser();
					user.setRefreshToken(result);
					AppClientFactory.setLoggedInUser(user);
					if(result!=null&&!result.equals("")&&!result.equals("null")){
						getGoogleAccessToken(result);
					}
				}
			});
		}else{
			getGoogleAccessToken(refreshToken);
		}
	}

	public void getGoogleAccessToken(String refreshToken){
		AppClientFactory.getInjector().getResourceService().refreshGoogleAccessToken(refreshToken, new SimpleAsyncCallback<GoogleToken>() {
			@Override
			public void onSuccess(GoogleToken result) {
				final String access_token = result.getAccess_token() !=null && !result.getAccess_token().equalsIgnoreCase("") ? result.getAccess_token() : null;
				final String connectedEmailId = result.getEmailId() !=null && !result.getEmailId().equalsIgnoreCase("") ? result.getEmailId() : null;
				if (access_token !=null ){
					UserDo user = AppClientFactory.getLoggedInUser();
					user.setAccessToken(access_token);
					AppClientFactory.setLoggedInUser(user);
					AppClientFactory.getInjector().getResourceService().getGoogleDriveFilesList(null,null,new SimpleAsyncCallback<GoogleDriveDo>() {
						@Override
						public void onSuccess(GoogleDriveDo googleDriveDo) {
							if(googleDriveDo!=null){
								if (googleDriveDo.getError()!=null && googleDriveDo.getError().getCode() == 401){
									getView().googleDirveStatus(false);
								}else if (googleDriveDo.getError()!=null && googleDriveDo.getError().getCode()==403){
									getView().googleDirveStatus(false);
								}else{
									UserDo user = AppClientFactory.getLoggedInUser();
									user.setAccessToken(access_token);
									AppClientFactory.setLoggedInUser(user);
									getView().googleDirveStatus(true);
									getView().setConnectedAs(connectedEmailId);
								}
							}else{
								getView().googleDirveStatus(false);
							}
						}
					});
				}else{
					getView().googleDirveStatus(false);
				}
			}
		});
	}
	@Override
	public void revokeToken() {
		AppClientFactory.getInjector().getUserService().revokeToken(AppClientFactory.getLoggedInUser().getGooruUId(),new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().googleDirveStatus(true);
			}
			@Override
			public void onSuccess(String result) {
				UserDo user = AppClientFactory.getLoggedInUser();
				user.setRefreshToken(null);
				AppClientFactory.setLoggedInUser(user);			
				getView().googleDirveStatus(false);
			}
		});
		
	}

	@Override
	public void getGoogleDrive() {
		Map<String, String> parms = new HashMap<String, String>();
		parms = StringUtil.splitQuery(Window.Location.getHref());
		parms.put("emailId", Refersh_emailId);
		AppClientFactory.getInjector().getSearchService().getGoogleDrive(Window.Location.getHref(), parms, new SimpleAsyncCallback<String>() {

			@Override
			public void onSuccess(String redirectUrl) {
				
				MixpanelUtil.mixpanelEvent("Access_Google_Drive");
				Window.Location.replace(redirectUrl);

			}
		});
		
	}
	
}