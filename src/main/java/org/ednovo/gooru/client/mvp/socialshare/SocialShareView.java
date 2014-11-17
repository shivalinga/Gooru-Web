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
package org.ednovo.gooru.client.mvp.socialshare;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.dnd.IsDraggableMirage;
import org.ednovo.gooru.client.mvp.home.LoginPopupUc;
import org.ednovo.gooru.client.mvp.socialshare.event.UpdateSocialShareMetaDataEvent;
import org.ednovo.gooru.client.mvp.socialshare.event.UpdateSocialShareMetaDataHandler;
import org.ednovo.gooru.client.service.UserServiceAsync;
import org.ednovo.gooru.client.uc.EmailShareUc;
import org.ednovo.gooru.client.uc.PPanel;
import org.ednovo.gooru.client.ui.HTMLEventPanel;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.client.util.PlayerDataLogEvents;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.content.CollectionDo;
import org.ednovo.gooru.shared.model.social.SocialShareDo;
import org.ednovo.gooru.shared.model.user.SettingDo;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
* @fileName : SocialShareView.java
*
* @description : This file used to share the user data by using 
*                     social web-sites or through email.
* 
* @version : 5.4
*
* @date:  August, 2013.
*
* @Author: Gooru Team
* 
* @Reviewer: Gooru Team
*/

public class SocialShareView extends Composite implements
IsSocialShareView {

	private static SocialShareViewUiBinder uiBinder = GWT
			.create(SocialShareViewUiBinder.class);
	
	MessageProperties i18n = GWT.create(MessageProperties.class);

	interface SocialShareViewUiBinder extends UiBinder<Widget, SocialShareView> {
	}


	@UiField
	HTMLEventPanel twitterPanel,emailPanel,fbIconPanel,twIconPanel,emailIconPanel;

	@UiField
	HTMLPanel shareTextPanel,shareIconPanel,panelfbIcon,panelTwIcon, panelEmailIcon,socialShareContainer;
	
	@UiField
	Anchor facbookText,twitterText,emailText;
	
	@UiField
	PPanel fbPanel,panelTwitter,panelEmail;
	
	private SocialShareDo socialDo;

	private UserServiceAsync userService;

	private SimpleAsyncCallback<SettingDo> userProfileAsyncCallback;

	@UiField Image categoryImage;;

	@UiField
	SocialShareStyle socialShareStyle;

	private String category;

	private String description;	

	public static final String DEFULT_IMAGE ="images/default-collection-image.png";

	public static final String DEFULT_VIMAGE ="images/default-video.png";

	public static final String DEFULT_QIMAGE ="images/default-question.png";

	public static final String DEFULT_IIMAGE ="images/default-interactive.png";

	public static final String DEFULT_WIMAGE ="images/default-website.png";

	public static final String DEFULT_AIMAGE ="images/default-audio.png";
	
	public static final String DEFULT_ITYPEIMAGE ="images/default-image.png";
	
	public static final String DEFULT_OIMAGE ="images/default-other.png";
	
	public static final String DEFULT_TEXTIMAGE ="images/default-text.png";
	
	public static final String DEFAULT_PROFILEIMAGE="images/profilepage/user-profile-pic.png";

	private boolean isProfilePageView = false;
	
	public HTML htmlName;

	/**
	 * Class constructor
	 * 
	 * @param collectionDo
	 *            instance of {@link CollectionDo}
	 */
	public SocialShareView(SocialShareDo shareDo) {

		this.socialDo=shareDo;

		initWidget(uiBinder.createAndBindUi(this));
		shareTextPanel.getElement().setId("pnlShareTextPanel");
		fbPanel.getElement().setId("epnlFbPanel");
		twitterPanel.getElement().setId("epnlTwitterPanel");
		emailPanel.getElement().setId("epnlEmailPanel");
		panelTwitter.getElement().setId("pnlPanelTwitter");
		panelEmail.getElement().setId("pnlPanelEmail");
		emailText.getElement().setInnerHTML(i18n.GL0212());
		emailText.getElement().setId("pnlEmailText");
		emailText.getElement().setAttribute("alt", i18n.GL0212());
		emailText.getElement().setAttribute("title", i18n.GL0212());
		twitterText.getElement().setInnerHTML(i18n.GL0647());
		twitterText.getElement().setId("pnlTwitterText");
		twitterText.getElement().setAttribute("alt", i18n.GL0647());
		twitterText.getElement().setAttribute("title", i18n.GL0647());
		facbookText.getElement().setInnerHTML(i18n.GL0646());
		facbookText.getElement().setId("pnlFacbookText");
		facbookText.getElement().setAttribute("alt", i18n.GL0646());
		facbookText.getElement().setAttribute("title", i18n.GL0646());
		shareIconPanel.getElement().setId("pnlShareIconPanel");
		fbIconPanel.getElement().setId("epnlFbIconPanel");
		panelfbIcon.getElement().setId("pnlPanelfbIcon");
		twIconPanel.getElement().setId("epnlTwIconPanel");
		panelTwIcon.getElement().setId("pnlPanelTwIcon");
		emailIconPanel.getElement().setId("epnlEmailIconPanel");
		panelEmailIcon.getElement().setId("pnlPanelEmailIcon");
		categoryImage.getElement().setId("imgCategoryImage");
		
		category=socialDo.getCategoryType()!=null?socialDo.getCategoryType():"collection";
		description=socialDo.getDescription();
		String title=socialDo.getTitle();
		if(description==null){
			description = "";
		} else {
			description = description.replaceAll("\\<.*?\\>", "");
		}

		if(title==null){
			title="";
		}

		setPresenter(new SocialSharePresenter(this));
		//		shareTextPanel.getElement().getStyle().setCursor(Cursor.POINTER);

	/*	try {
			if(socialDo.getIsSearchShare()){
				socialShareContainer.getElement().getStyle().setWidth(100, Unit.PX);
			}
		} catch (Exception e) {

		}
*/
		if (socialDo.isOnlyIcon()) {
			shareTextPanel.setVisible(false);
			shareIconPanel.setVisible(true);
		} else {
			shareIconPanel.setVisible(false);
			shareTextPanel.setVisible(true);
		}


		if(title.contains("img")){
			socialDo.setTitle(i18n.GL0308());
		}else{
			if (title.length() > 50) {
				title = title.substring(0,50)+ "...";
				socialDo.setTitle(title.replaceAll("<p>","").replaceAll("</p>", ""));
			}else{
				socialDo.setTitle(title.replaceAll("<p>","").replaceAll("</p>", ""));
			}
		}
		//        if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.SHELF)){
		if((socialDo.getShareType().equalsIgnoreCase("private"))){

			panelfbIcon.setStyleName(socialShareStyle.classPageShareButtonsFTEBgDisable());
			panelTwIcon.setStyleName(socialShareStyle.classPageShareButtonsFTEBgDisable());
			panelEmailIcon.setStyleName(socialShareStyle.classPageShareButtonsFTEBgDisable());

		//	fbPanel.setStyleName(socialShareStyle.classPageShareButtonsBgDisable());
		//	panelTwitter.setStyleName(socialShareStyle.classPageShareButtonsBgDisable());
		//	panelEmail.setStyleName(socialShareStyle.classPageShareButtonsBgDisable());

			//				shareTextPanel.getElement().getStyle().setCursor(Cursor.DEFAULT);
			//				shareIconPanel.getElement().getStyle().setCursor(Cursor.DEFAULT);
		}else{

			panelfbIcon.setStyleName(socialShareStyle.fbPageShareIconButtonsBg());
			panelTwIcon.setStyleName(socialShareStyle.twitterPageShareIconButtonsBg());
			panelEmailIcon.setStyleName(socialShareStyle.classPageShareButtonsFTEBg());

			//fbPanel.setStyleName(socialShareStyle.fbPageShareButtonsBg());
			//panelTwitter.setStyleName(socialShareStyle.twitterPageShareButtonsBg());
			//panelEmail.setStyleName(socialShareStyle.classPageShareButtonsBg());

			//				shareTextPanel.getElement().getStyle().setCursor(Cursor.POINTER);
			//				shareIconPanel.getElement().getStyle().setCursor(Cursor.POINTER);
		}
		//		}

//		categoryImage.addErrorHandler(new ErrorHandler() {
//
//			@Override
//			public void onError(ErrorEvent event) {
//				String url = socialDo.getThumbnailurl();
//				if (url == null || url.endsWith("null") || url.contains("null") || url.endsWith("/")){
//					if(category==null){
//						categoryImage.setUrl(DEFULT_IMAGE);
//					}
//					else if (category.equalsIgnoreCase("collection")) {
//						categoryImage.setUrl(DEFULT_IMAGE);
//					} else if (category.equalsIgnoreCase("video")) {
//						categoryImage.setUrl(DEFULT_VIMAGE);
//					} else if (category.equalsIgnoreCase("question")) {
//						categoryImage.setUrl(DEFULT_QIMAGE);
//					} else if (category.equalsIgnoreCase("interactive")) {
//						categoryImage.setUrl(DEFULT_IIMAGE);
//					} else if (category.equalsIgnoreCase("website")||category.equalsIgnoreCase("exam")||category.equalsIgnoreCase("webpage")) {
//						categoryImage.setUrl(DEFULT_WIMAGE);
//					} else if (category.equalsIgnoreCase("slide")||category.equalsIgnoreCase("image")){
//						categoryImage.setUrl(DEFULT_ITYPEIMAGE);
//					} else if (category.equalsIgnoreCase("textbook")||category.equalsIgnoreCase("handout")||category.equalsIgnoreCase("lesson")||category.equalsIgnoreCase("text")) {
//						categoryImage.setUrl(DEFULT_TEXTIMAGE);
//					} else if (category.equalsIgnoreCase("audio")) {
//						categoryImage.setUrl(DEFULT_AIMAGE);
//					} else if (category.equalsIgnoreCase("other")) {
//						categoryImage.setUrl(DEFULT_OIMAGE);
//					} else if (category.equalsIgnoreCase("profile")){
//						categoryImage.setUrl(DEFAULT_PROFILEIMAGE);
//					}
//				}
//				socialDo.setThumbnailurl(categoryImage.getUrl());
//			}
//		});
//		setUrl(socialDo.getThumbnailurl());
		if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.PROFILE_PAGE)) {
			isProfilePageView = true;
		}

		/**
		 * Adding Event Handler.
		 * @param UpdateSocialShareMetaDataEvent.TYPE is type of event.
		 * @param setHeader is Object of Handler.
		 */
		AppClientFactory.getEventBus().addHandler(UpdateSocialShareMetaDataEvent.TYPE,setHeader);
	}
	public void setSocialShareContainerId(String socialShareId) {
		socialShareContainer.getElement().setId(socialShareId);
	}

	/**
	 * @param url
	 *            of the image
	 */
	public void setUrl(String url) {
		categoryImage.setUrl(StringUtil.formThumbnailName(url, "."));
		categoryImage.setAltText(socialDo.getTitle());
		categoryImage.setTitle(socialDo.getTitle());
	}
	
	@Override
	public void onLoad(){
		setUrl(socialDo.getThumbnailurl());
	}
	
	@UiHandler("categoryImage")
	public void setDefaultImage(ErrorEvent event){
		if (category.equalsIgnoreCase("collection")) {
			categoryImage.setUrl(DEFULT_IMAGE);
		} else if (category.equalsIgnoreCase("video")) {
			categoryImage.setUrl(DEFULT_VIMAGE);
		} else if (category.equalsIgnoreCase("question")) {
			categoryImage.setUrl(DEFULT_QIMAGE);
		} else if (category.equalsIgnoreCase("interactive")) {
			categoryImage.setUrl(DEFULT_IIMAGE);
		} else if (category.equalsIgnoreCase("website")||category.equalsIgnoreCase("exam")||category.equalsIgnoreCase("webpage")) {
			categoryImage.setUrl(DEFULT_WIMAGE);
		} else if (category.equalsIgnoreCase("slide")||category.equalsIgnoreCase("image")){
			categoryImage.setUrl(DEFULT_ITYPEIMAGE);
		} else if (category.equalsIgnoreCase("textbook")||category.equalsIgnoreCase("handout")||category.equalsIgnoreCase("lesson")||category.equalsIgnoreCase("text")) {
			categoryImage.setUrl(DEFULT_TEXTIMAGE);
		} else if (category.equalsIgnoreCase("audio")) {
			categoryImage.setUrl(DEFULT_AIMAGE);
		} else if (category.equalsIgnoreCase("other")) {
			categoryImage.setUrl(DEFULT_OIMAGE);
		} else if (category.equalsIgnoreCase("profile")){
			categoryImage.setUrl(DEFAULT_PROFILEIMAGE);
		}
	}

	/**
	 * Call FaceBook share event
	 * @param clickEvent instance of {@link ClickEvent}
	 */
	@UiHandler("fbPanel")
	public void onFbClickEvent(ClickEvent event) {
		if(!(socialDo.getShareType().equalsIgnoreCase("private"))){
			if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.RESOURCE_SEARCH)){
				MixpanelUtil.Click_Facebook_FromResource();
			}else if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.COLLECTION_SEARCH)){
				MixpanelUtil.Click_Facebook_FromCollection();
			}
			else if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.COLLECTION_PLAY) || AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.PREVIEW_PLAY)){
				if(socialShareContainer.getElement().getId().equalsIgnoreCase("collectionShareViewPage")) {
					MixpanelUtil.ClickFacebookFromShareInCollectionplayer();
				} else if (socialShareContainer.getElement().getId().equalsIgnoreCase("collectionShareSummaryPage")) {
					MixpanelUtil.ClickFacebookFromSummaryPageInCollectionplayer();
				}

			}
			onFbShareEvent();
		}
	}
	/**
	 * Call FaceBook share event
	 * @param clickEvent instance of {@link ClickEvent}
	 */
	@UiHandler("fbIconPanel")
	public void onFbIconClickEvent(ClickEvent event){
		if(!isProfilePageView || !(socialDo.getShareType().equalsIgnoreCase("private"))){
			if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.RESOURCE_SEARCH)){
				MixpanelUtil.Click_Facebook_FromResource();
			}else if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.COLLECTION_SEARCH)){
				MixpanelUtil.Click_Facebook_FromCollection();
			}else if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.COLLECTION_PLAY) || AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.PREVIEW_PLAY)){
				if(socialShareContainer.getElement().getId().equalsIgnoreCase("collectionShareViewPage")) {
					MixpanelUtil.ClickFacebookFromShareInCollectionplayer();
				} else if (socialShareContainer.getElement().getId().equalsIgnoreCase("collectionShareSummaryPage")) {

					MixpanelUtil.ClickFacebookFromSummaryPageInCollectionplayer();
				}

			}

			onFbShareEvent();
		}
	}

	/**
	 * Call Email share event
	 * @param clickEvent instance of {@link ClickEvent}
	 */
	@UiHandler("emailPanel")
	public void onEmailClickEvent(ClickEvent event){
		if(!(socialDo.getShareType().equalsIgnoreCase("private"))){
			if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.RESOURCE_SEARCH)){
				MixpanelUtil.Click_Email_FromResource();
			}else if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.COLLECTION_SEARCH)){
				MixpanelUtil.Click_Email_FromCollection();
			}
			else if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.COLLECTION_PLAY) || AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.PREVIEW_PLAY)){
				if(socialShareContainer.getElement().getId().equalsIgnoreCase("collectionShareViewPage")) {
					MixpanelUtil.ClickEmailFromShareInCollectionplayer();
				} else if (socialShareContainer.getElement().getId().equalsIgnoreCase("collectionShareSummaryPage")) {
					MixpanelUtil.ClickEmailFromSummaryPageInCollectionplayer();
				}
			}
			onEmailShareEvent();
		}
	}
	/**
	 * Call Email share event
	 * @param clickEvent instance of {@link ClickEvent}
	 */
	@UiHandler("emailIconPanel")
	public void onEmailIconClickEvent(ClickEvent event){
		if(!isProfilePageView || !(socialDo.getShareType().equalsIgnoreCase("private"))){
			if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.RESOURCE_SEARCH)){
				MixpanelUtil.Click_Email_FromResource();
			}else if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.COLLECTION_SEARCH)){
				MixpanelUtil.Click_Email_FromCollection();

			}
			else if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.COLLECTION_PLAY) || AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.PREVIEW_PLAY)){
				if(socialShareContainer.getElement().getId().equalsIgnoreCase("collectionShareViewPage")) {
					MixpanelUtil.ClickEmailFromShareInCollectionplayer();
				} else if (socialShareContainer.getElement().getId().equalsIgnoreCase("collectionShareSummaryPage")) {
					MixpanelUtil.ClickEmailFromSummaryPageInCollectionplayer();
				}
			}

			onEmailShareEvent();
		}
	}

	/**
	 * Call Twitter share event
	 * @param clickEvent instance of {@link ClickEvent}
	 */
	@UiHandler("twitterPanel")
	public void onTwitterClickEvent(ClickEvent event) {
		if(!(socialDo.getShareType().equalsIgnoreCase("private"))){
			if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.RESOURCE_SEARCH)){
				MixpanelUtil.Click_Twitter_FromResource();
			}else if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.COLLECTION_SEARCH)){
				MixpanelUtil.Click_Twitter_FromCollection();
			}
			else if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.COLLECTION_PLAY) || AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.PREVIEW_PLAY)){
				if(socialShareContainer.getElement().getId().equalsIgnoreCase("collectionShareViewPage")) {
					MixpanelUtil.ClickTwitterFromShareInCollectionplayer();
				} else if (socialShareContainer.getElement().getId().equalsIgnoreCase("collectionShareSummaryPage")) {
					MixpanelUtil.ClickTwitterFromSummaryPageInCollectionplayer();
				}
			}
			onTwisterShareEvent();
		}
	}

	/**
	 * Call Twitter share event
	 * @param clickEvent instance of {@link ClickEvent}
	 */
	@UiHandler("twIconPanel")
	public void onTwitterIconClickEvent(ClickEvent event) {
		if(!isProfilePageView || !(socialDo.getShareType().equalsIgnoreCase("private"))){
			if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.RESOURCE_SEARCH)){
				MixpanelUtil.Click_Twitter_FromResource();
			}else if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.COLLECTION_SEARCH)){
				MixpanelUtil.Click_Twitter_FromCollection();

			}	else if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.COLLECTION_PLAY) || AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.PREVIEW_PLAY)){
				if(socialShareContainer.getElement().getId().equalsIgnoreCase("collectionShareViewPage")) {
					MixpanelUtil.ClickTwitterFromShareInCollectionplayer();
				} else if (socialShareContainer.getElement().getId().equalsIgnoreCase("collectionShareSummaryPage")) {
					MixpanelUtil.ClickTwitterFromSummaryPageInCollectionplayer();
				}
			}

			onTwisterShareEvent();
		}
	}

	/**
	 * Share the data by using FB.
	 */
	private void onFbShareEvent() {
		MixpanelUtil.Click_On_FaceBook();
		if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.PROFILE_PAGE)){
			if(socialDo.getIsSearchShare()){
				triggerShareDataEvent(PlayerDataLogEvents.FACEBOOK,false);
				SocialShareView.postOnFacebook(socialDo.getTitle(),socialDo.getRawUrl(),description,categoryImage.getUrl());
//				Window.open(
//						"http://www.facebook.com/sharer/sharer.php?s=100&p[url]="
//								+ socialDo.getRawUrl() + "&p[images][0]="
//								+ socialDo.getThumbnailurl() + "&p[title]="
//								+ socialDo.getTitle().replaceAll("\\+", "%2B") + "&p[summary]=" + description,
//								"_blank", "width=626,height=436");
			}
			else{
				triggerShareDataEvent(PlayerDataLogEvents.FACEBOOK,false);
				String title=i18n.GL1995()+" "+i18n.GL1433();
				String token= Window.Location.getHref();
				SocialShareView.postOnFacebook(title,socialDo.getRawUrl(),getAsHtml(description)+" "+token,categoryImage.getUrl());
//				Window.open(
//						"http://www.facebook.com/sharer/sharer.php?s=100&p[url]="
//								+socialDo.getRawUrl() + "&p[images][0]="
//								+ socialDo.getThumbnailurl() + "&p[title]="
//								+ "Check out "+socialDo.getTitle().replaceAll("\\+", "%2B")+" on Gooru" + "&p[summary]=" + description,
//								"_blank", "width=626,height=436");
			}
		}
		else{
			triggerShareDataEvent(PlayerDataLogEvents.FACEBOOK,false);
			SocialShareView.postOnFacebook(socialDo.getTitle(),socialDo.getRawUrl(),description,categoryImage.getUrl());
//			Window.open(
//					"http://www.facebook.com/sharer/sharer.php?s=100&p[url]="
//							+ socialDo.getRawUrl() + "&p[images][0]="
//							+ socialDo.getThumbnailurl() + "&p[title]="
//							+ socialDo.getTitle().replaceAll("\\+", "%2B") + "&p[summary]=" + description,
//							"_blank", "width=626,height=436");
		}
	}

	/**
	 * Share the data by using Twister.
	 */
	private void onTwisterShareEvent() {
		MixpanelUtil.Click_On_Twitter();
		if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.PROFILE_PAGE)){
			if(socialDo.getIsSearchShare()){
				triggerShareDataEvent(PlayerDataLogEvents.TWITTER,false);
				Window.open("http://twitter.com/intent/tweet?text=" + "Gooru - "+socialDo.getTitle().replaceAll("\\+", "%2B")+ ": " + socialDo.getBitlylink(), "_blank", "width=600,height=300");  
			}else{
				triggerShareDataEvent(PlayerDataLogEvents.TWITTER,false);
//				Window.open("http://twitter.com/intent/tweet?text=" + "Check out "+socialDo.getTitle().replaceAll("\\+", "%2B")+ "'s Gooru Profile Page - " + socialDo.getBitlylink(), "_blank", "width=600,height=300");
				Window.open("http://twitter.com/intent/tweet?text=" + getEncodedUrl(i18n.GL1085_1()) + socialDo.getBitlylink() , "_blank", "width=600,height=300");
			}
		}else{
			triggerShareDataEvent(PlayerDataLogEvents.TWITTER,false);
			Window.open("http://twitter.com/intent/tweet?text=" + "Gooru - "+socialDo.getTitle().replaceAll("\\+", "%2B")+ ": " + socialDo.getBitlylink(), "_blank", "width=600,height=300");
		}
	}
	/**
	 * Share the data by using Email.
	 */
	private void onEmailShareEvent() {
		MixpanelUtil.Click_On_Email();
		if(!(AppClientFactory.isAnonymous())){
			AppClientFactory.getInjector().getUserService().getUserProfileDetails(AppClientFactory.getLoggedInUser().getGooruUId(), new SimpleAsyncCallback<SettingDo>() {

				@Override
				public void onSuccess(SettingDo result) {
					socialDo.setEmailId(result.getExternalId());
					EmailShareUc emailShare=new EmailShareUc(socialDo){
						public void triggerEmailEvent(boolean confirmStaus){
							triggerShareDataEvent(PlayerDataLogEvents.MAIL,confirmStaus);
						}
					};
					emailShare.show();
					emailShare.center();
				}
			});
		}else{
			LoginPopupUc popup = new LoginPopupUc();
			popup.setGlassEnabled(true);
			popup.show();
			popup.center();
		}
	}

	@Override
	public Widget getDragHandle() {
		return null;
	}

	@Override
	public IsDraggableMirage initDraggableMirage() {
		return null;
	}

	@Override
	public void onDragBlur() {
		// TODO Auto-generated method stub
	}

	@Override
	public String getDragId() {
		return null;
	}

	@Override
	public DRAG_TYPE getDragType() {
		return null;
	}

	@Override
	public int getDragTopCorrection() {
		return 0;
	}

	@Override
	public int getDragLeftCorrection() {
		return 0;
	}

	public void setUserProfileAsyncCallback(SimpleAsyncCallback<SettingDo> userProfileAsyncCallback) {
		this.userProfileAsyncCallback = userProfileAsyncCallback;
	}

	public UserServiceAsync getUserService() {
		return userService;
	}

	public SimpleAsyncCallback<SettingDo> getUserprofileAsyncCallback() {
		return userProfileAsyncCallback;
	}

	/**
	 * Updating the User Meta data (Title, Description)
	 * by using UpdateSocialShareMetaDataHandler.
	 */
	UpdateSocialShareMetaDataHandler setHeader = new UpdateSocialShareMetaDataHandler() {
		@Override
		public void updateSocialShareMetaData(String title,
				String description1, String imageUrl) {
			socialDo.setTitle(title);

			socialDo.setDescription(description1);

			description = socialDo.getDescription();
			if(description==null){
				description = "";
			} else {
				description = description.replaceAll("\\<.*?\\>", "");
			}

			if (!imageUrl.equalsIgnoreCase("")) {
				socialDo.setThumbnailurl(imageUrl);
			}
		}
	};
	   public static void postOnFacebook(String titleName,String shareLink,String description,String thumbnailUrl){
		   String faceBookFeedUrl=AppClientFactory.getLoggedInUser().getSettings().getFacebookFeedUrl();
		   String appId=AppClientFactory.getLoggedInUser().getSettings().getFacebookAppId();
		   faceBookFeedUrl=faceBookFeedUrl+"?app_id="+appId+"&display=popup&name="+getEncodedUrl(titleName)+"&link="+shareLink
				   +"&picture="+getEncodedUrl(thumbnailUrl)+"&description="
				   +getEncodedUrl(description)
				   +"&redirect_uri="+getEncodedUrl("https://www.facebook.com/");
				   //+"&actions="+getEncodedUrl(actions);
		   Window.open(faceBookFeedUrl,"_blank", "width=626,height=436");
	   }
	   
	   public static native String getEncodedUrl(String url)/*-{
			var encodeurl= $wnd.encodeURIComponent(url);
			return encodeurl;
		}-*/;
	@Override
	public void setPresenter(SocialSharePresenter childPresenter) {
		
	}
	
	public void setShareDo(SocialShareDo shareDo){
		this.socialDo=shareDo;
		setUrl(socialDo.getThumbnailurl());
	}
	public void triggerShareDataEvent(String shareType,boolean confirmStaus){
		
	}
	
	public String getAsHtml(String name){
		htmlName=new HTML();
		htmlName.getElement().setInnerHTML(name);
		return htmlName.getHTML();
		
	}
	
}
