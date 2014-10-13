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
package org.ednovo.gooru.client.mvp.play.collection;

import java.util.LinkedHashMap;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BasePopupViewWithHandlers;
import org.ednovo.gooru.client.mvp.home.HomeCBundle;
import org.ednovo.gooru.client.mvp.play.collection.body.CollectionPlayerMetadataView;
import org.ednovo.gooru.client.mvp.play.collection.footer.StudyPlayerFooterView;
import org.ednovo.gooru.client.mvp.play.collection.header.StudyPlayerHeaderView;
import org.ednovo.gooru.client.mvp.play.collection.preview.PreviewPlayerPresenter;
import org.ednovo.gooru.client.mvp.play.collection.preview.metadata.NavigationConfirmPopup;
import org.ednovo.gooru.client.mvp.play.resource.body.ResourcePlayerMetadataView;
import org.ednovo.gooru.client.mvp.play.resource.style.PlayerStyleBundle;
import org.ednovo.gooru.client.mvp.shelf.event.RefreshUserShelfCollectionsEvent;
import org.ednovo.gooru.client.uc.PlayerBundle;
import org.ednovo.gooru.client.uc.tooltip.GlobalTooltipWithButton;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.content.ContentReportDo;
import org.ednovo.gooru.shared.util.StringUtil;
import org.ednovo.gooru.shared.util.UAgentInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window.Navigator;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.NavigationEvent;
import com.gwtplatform.mvp.client.proxy.NavigationHandler;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

public class CollectionPlayerView extends BasePopupViewWithHandlers<CollectionPlayerUiHandlers> implements IsCollectionPlayerView{
	
	
	@UiField FlowPanel playerBodyContainer,navigationContainer,playerContent,menuContent;
	
	@UiField StudyPlayerHeaderView headerView;
	
	@UiField StudyPlayerFooterView footerView;
	
	@UiField HTMLPanel ipadSectiondiv,androidSectiondiv;
	
	@UiField com.google.gwt.user.client.ui.Image closeIpadBtn,closeAndriodBtn;
	
	@UiField Anchor viewAnchor,menuButton;
	
	@UiField HTMLPanel msgPanel,msglinkPanel,gooruPanel,ednovoPanel,appstorePanel;
	
	private PopupPanel appPopUp;
	
	private boolean isInfoButtonActive=false;
	
	private boolean isShareButtonActive=false;
	
	private boolean isNarrationButtonActive=false;
	
	private boolean isNavigationButtonActive=false;
	
	private boolean isAddButtonActive=false;
	
	private boolean  isFlagButtonActive=false;
	
	private static final String STUDY_PLAYER ="studyPlayer";
	
	
	GlobalTooltipWithButton globalTooltipWithButton,logOutToolTip;
	
	private int userRating=0;
	private String resourceId;
	private HandlerRegistration autoHideHandler;

	private final EventBus eventBus;

	private static final String COLLECTION_RESOURCE_THUMBS_WIDGET_MODE="COLLECTION_RESOURCE_RATING";
	
	private static CollectionPlayerViewUiBinder uiBinder = GWT.create(CollectionPlayerViewUiBinder.class);

	interface CollectionPlayerViewUiBinder extends UiBinder<Widget, CollectionPlayerView> {
	}
	
	private MessageProperties i18n = GWT.create(MessageProperties.class);
	
	@Inject
	public CollectionPlayerView(EventBus eventBus){
		super(eventBus);
		PlayerBundle.INSTANCE.getPlayerStyle().ensureInjected();
		this.eventBus = eventBus;
		appPopUp=new PopupPanel();
		appPopUp.setGlassEnabled(true);
		//appPopUp.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().setStudyPlayerContainer());
		appPopUp.setStyleName(PlayerStyleBundle.INSTANCE.getPlayerStyleResource().studyplayerContainer());
		appPopUp.add(uiBinder.createAndBindUi(this));
		//footerView.getNavigationButton().addClickHandler(new ShowTabWidgetView("navigation"));
		//footerView.getNarrationButton().addClickHandler(new ShowTabWidgetView("narration"));
		footerView.getShareButton().addClickHandler(new ShowTabWidgetView("share"));
		footerView.getInfoButton().addClickHandler(new ShowTabWidgetView("info"));
		footerView.getAddButton().addClickHandler(new ShowTabWidgetView("add"));
		footerView.getFlagButton().addClickHandler(new ShowTabWidgetView("flag"));
		getResourceAnimationContainer().getElement().getStyle().setProperty("display", "none");
		headerView.getCloseButton().addClickHandler(new CloseResourcePlayerEvent());
		/*headerView.getThumbsDownButton().addClickHandler(new UpdateThumbsDownEvent());
		headerView.getThumbsUpButton().addClickHandler(new UpdateThumbsUpEvent());*/
		headerView.getAuthorContainer().addClickHandler(new ShowLoginPopupEvent());
		menuButton.addClickHandler(new ShowAuthorContainerEvent());
		menuButton.addTouchStartHandler(new ShowAuthorContainerTouchEvent());
		setAutoHideOnNavigationEventEnabled(true);
		hidePlayerButtons(true,null);
		  Boolean isIpad = !!Navigator.getUserAgent().matches("(.*)iPad(.*)");
		  Boolean isAndriod = !!Navigator.getUserAgent().matches("(.*)Android(.*)");
		  Boolean isWinDskp = !!Navigator.getUserAgent().matches("(.*)NT(.*)");
		  
		  UAgentInfo detector = new UAgentInfo(Navigator.getUserAgent());
		  
		  if(isIpad && !StringUtil.IPAD_MESSAGE_Close_Click)
		  {
			  headerView.getElement().setAttribute("style", "position:relative;");
			 ipadSectiondiv.setVisible(true);
			 androidSectiondiv.setVisible(false);
	
		  }
		  else if(isAndriod && !StringUtil.IPAD_MESSAGE_Close_Click)
		  {
			  headerView.getElement().setAttribute("style", "position:relative;");
			  ipadSectiondiv.setVisible(false);
			  androidSectiondiv.setVisible(true);
			 // wrapperPanel.getElement().getFirstChildElement().getFirstChildElement().setAttribute("style", "position:fixed;");
		  }
		  else
		  {
			  ipadSectiondiv.setVisible(false);
			  androidSectiondiv.setVisible(false);
			 // headerView.getElement().setAttribute("style", "position:fixed;");
			  
		  }
		  setUiText();
		
	}
	
	@UiHandler("closeIpadBtn")
	public void onIpadCloseClick(ClickEvent clickEvent){
		 ipadSectiondiv.setVisible(false);
		  androidSectiondiv.setVisible(false);
		 // headerView.getElement().setAttribute("style", "position:fixed;");
		  StringUtil.IPAD_MESSAGE_Close_Click = true;
		  CollectionPlayerMetadataView.onClosingAndriodorIpaddiv();
		  ResourcePlayerMetadataView.onClosingAndriodorIpaddiv();
	}

	@UiHandler("closeAndriodBtn")
	public void onAndriodCloseClick(ClickEvent clickEvent){
		 ipadSectiondiv.setVisible(false);
		  androidSectiondiv.setVisible(false);
		 // headerView.getElement().setAttribute("style", "position:fixed;");
		  StringUtil.IPAD_MESSAGE_Close_Click = true;
		  CollectionPlayerMetadataView.onClosingAndriodorIpaddiv();
		  ResourcePlayerMetadataView.onClosingAndriodorIpaddiv();
	}
	
	 @Override
	  public void setAutoHideOnNavigationEventEnabled(boolean autoHide) {
	    if (autoHide) {
	      if (autoHideHandler != null) {
	        return;
	      }
	      autoHideHandler = eventBus.addHandler(NavigationEvent.getType(), new NavigationHandler() {
	            @Override
	            public void onNavigation(NavigationEvent navigationEvent) {
	              if(!AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equals(PlaceTokens.COLLECTION_PLAY)){
	            	  closePreviewPlayer();
	            	  hideFromPopup(false);
	              } 
	            }
	          });
	    } else {
	      if (autoHideHandler != null) {
	        autoHideHandler.removeHandler();
	      }
	    }
	  }

	
	public void removeStudentViewButton(){
		//headerView.getStudentViewButton().removeFromParent();
	}

	@Override
	public FlowPanel getPlayerBodyContainer() {
		return playerBodyContainer;
	}

	public FlowPanel getResourceAnimationContainer() {
		return footerView.getResourceAnimationContainer();
	}

	@Override
	public void setResourceTitle(String resourceTitle) {
		headerView.setResourceTitle(resourceTitle);
	}
	
	public void setNarrationButton(Button narrationButton){
		footerView.setNarrationButton(narrationButton);
		footerView.getNarrationButton().addClickHandler(new ShowTabWidgetView("narration"));
	}

	@Override
	public void enablePlayerButton(boolean isAddButtonEnable,boolean isInfoButtonEnable,
			boolean isShareButtonEnable, boolean isNarrationButtonEnable,
			boolean isNavigationButtonEnable, boolean isFlagButtonActive) {
		footerView.enableButtons(isAddButtonEnable,isInfoButtonEnable, isShareButtonEnable, isNarrationButtonEnable, isNavigationButtonEnable,isFlagButtonActive);
	}
	
	public class ShowTabWidgetView implements ClickHandler{
		String tabView="";
		public ShowTabWidgetView(String tabView){
			this.tabView=tabView;
		}
		@Override
		public void onClick(ClickEvent event) {
			if(tabView.equalsIgnoreCase("add")){
				MixpanelUtil.clickInfo(STUDY_PLAYER);	
				setTabPlaceRequest(tabView,footerView.isAddButtonEnabled(),isAddButtonActive);
			}
			else if(tabView.equalsIgnoreCase("info")){
				MixpanelUtil.clickInfo(STUDY_PLAYER);
				MixpanelUtil.OpenInfo();
				setTabPlaceRequest(tabView,footerView.isInfoButtonEnabled(),isInfoButtonActive);
			}else if(tabView.equalsIgnoreCase("share")){
				MixpanelUtil.clickShareCollection(STUDY_PLAYER);
				MixpanelUtil.OpenShare();
				setTabPlaceRequest(tabView,footerView.isShareButtonEnabled(),isShareButtonActive);
			}else if(tabView.equalsIgnoreCase("navigation")){
				MixpanelUtil.clickNavigation(STUDY_PLAYER);	
				setTabPlaceRequest(tabView,footerView.isNavigationButtonEnabled(),isNavigationButtonActive);
			}else if(tabView.equalsIgnoreCase("narration")){
				MixpanelUtil.ClickNarration(STUDY_PLAYER);	
				setTabPlaceRequest(tabView,footerView.isNarrationButtonEnabled(),isNarrationButtonActive);
			}
			else if(tabView.equalsIgnoreCase("flag")){
				setTabPlaceRequest(tabView,true,false);
			}	
		}		
	}	
	public void setTabPlaceRequest(String tabView, boolean isButtonEnable,boolean isButtonActive){
		if(isButtonEnable){
			String collectionId=AppClientFactory.getPlaceManager().getRequestParameter("id", null);
			String resourceId=AppClientFactory.getPlaceManager().getRequestParameter("rid", null);
			String view=AppClientFactory.getPlaceManager().getRequestParameter("view", null);
			
			Map<String,String> params = new LinkedHashMap<String,String>();
			params.put("id", collectionId);
			params = PreviewPlayerPresenter.setConceptPlayerParameters(params);
			
			if(resourceId!=null&&!resourceId.equalsIgnoreCase("")){
				if(isButtonActive){
					params.put("rid", resourceId);
					PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(PlaceTokens.COLLECTION_PLAY, params);
					AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, true);
				}else{
					params.put("rid", resourceId);
					params.put("tab", tabView);
					PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(PlaceTokens.COLLECTION_PLAY, params);
					AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, true);
					ResourcePlayerMetadataView.removePadding();
				}
			}
			else if(view!=null&&view.equalsIgnoreCase("end")){
				if(isButtonActive){
					params.put("view", "end");
					PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(PlaceTokens.COLLECTION_PLAY, params);
					AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, true);
				}else{
					params.put("tab", tabView);
					params.put("view", "end");
					PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(PlaceTokens.COLLECTION_PLAY, params);
					AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, true);
				}
			}else{
				if(isButtonActive){
					PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(PlaceTokens.COLLECTION_PLAY, params);
					AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, true);

				}else{
					params.put("tab", tabView);
					PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(PlaceTokens.COLLECTION_PLAY, params);
					AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, true);
				}
			}
		}
		
	}
		
	@Override
    public void setInSlot(Object slot, Widget content) {
		if(slot==CollectionPlayerPresenter.COLLECTION_PLAYER_TOC_PRESENTER_SLOT){
			    //getNavigationContainer().getElement().getStyle().setProperty("display", "none");
			getResourceAnimationContainer().clear();
			getResourceAnimationContainer().setVisible(false);
			if(content!=null){
				getResourceAnimationContainer().add(content);
			}
		}else if(slot==CollectionPlayerPresenter.COLLECTION_PLAYER_NAVIGATION_SLOT){
			navigationContainer.clear();
			navigationContainer.setVisible(true);
			if(content!=null){
				navigationContainer.add(content);
			}
		}else if(slot==CollectionPlayerPresenter.METADATA_PRESENTER_SLOT){
			getPlayerBodyContainer().clear();
			if(content!=null){
				getPlayerBodyContainer().add(content);
			}
		}  
	}

	@Override
	public void makeButtonActive(boolean makeAddButtionActive,boolean makeInfoButtionActive,boolean makeShareButtonActive, boolean makeNarrationButtonActive,
			boolean makeNavigationButtonActive,boolean makeFlagButtonActive) {
		footerView.makeButtonActive(makeAddButtionActive,makeInfoButtionActive, makeShareButtonActive, makeNarrationButtonActive, makeNavigationButtonActive,makeFlagButtonActive);
		setActiveButton(makeAddButtionActive,makeInfoButtionActive, makeShareButtonActive, makeNarrationButtonActive, makeNavigationButtonActive,makeFlagButtonActive);
		if(makeNavigationButtonActive || makeInfoButtionActive || makeShareButtonActive)
		{
			ResourcePlayerMetadataView.removePadding();
			//CollectionPlayerMetadataView.removePadding();
		}
		if(!AppClientFactory.isAnonymous() && makeAddButtionActive){
			ResourcePlayerMetadataView.removePadding();
			//CollectionPlayerMetadataView.removePadding();
		}
	}

	@Override
	public void clearActiveButton(boolean deselectAddButton,boolean deselectInfoButton,boolean deselectShareButtion,boolean deselectNarrationButton,boolean deselectNavigationButton,boolean deselectFlagButton) {
		footerView.clearActiveButton(deselectAddButton,deselectInfoButton, deselectShareButtion, deselectNarrationButton, deselectNavigationButton,deselectFlagButton);	
		setActiveButton(false,false,false,false,false,false);
		ResourcePlayerMetadataView.addPadding();
		//CollectionPlayerMetadataView.addPadding();
	}
	public void setActiveButton(boolean makeAddButtionActive,boolean makeInfoButtionActive,
			boolean makeShareButtonActive,boolean makeNarrationButtonActive,boolean makeNavigationButtonActive,boolean makeFlagButtonActive){
		this.isInfoButtonActive=makeInfoButtionActive;
		this.isShareButtonActive=makeShareButtonActive;
		this.isNarrationButtonActive=makeNarrationButtonActive;
		this.isNavigationButtonActive=makeNavigationButtonActive;
		this.isAddButtonActive=makeAddButtionActive;
		this.isFlagButtonActive=makeFlagButtonActive;
	}

	public class CloseResourcePlayerEvent implements ClickHandler{
		public void onClick(ClickEvent event) {
			if(!getUiHandlers().isOpenEndedAnswerSubmited()){
				new NavigationConfirmPopup() {
					@Override
					public void navigateToNextResource() {
						super.hide();
						closePlayer();
					}
				};
			}else{
				closePlayer();
			}
			
		}
	}
	
	public void closePlayer(){
		String page=AppClientFactory.getPlaceManager().getRequestParameter("page",null);
		if(page!=null){
			if(page.equals("teach")){
				//TODO reveal to teach page
				getUiHandlers().revealTeachOrStudypage(page);
			}else if(page.equals("study")){
				//TODO reveal to studypage
				getUiHandlers().revealTeachOrStudypage(page);
			}else{
				hideFromPopup(true);
				closePreviewPlayer();
			}
		}else{
			hideFromPopup(true);
			closePreviewPlayer();
		}
	}
	
	@Override
	public void showClasspage() {
		hideFromPopup(true);
		closePreviewPlayer();
	}
	
	public void showClasspage(String classpageId,String page){
		//TODO reveal to classpage..
		String viewToken=AppClientFactory.getPlaceManager().getPreviousPlayerRequestUrl().getNameToken();
		if(viewToken.equals(PlaceTokens.DISCOVER)){
			Map<String,String> paramsMap=new LinkedHashMap<String,String>();
			if(page.equals("teach")){
				paramsMap.put("classpageid", classpageId);
				revealToClassPage(PlaceTokens.EDIT_CLASSPAGE,paramsMap);
			}else if(page.equals("study")){
				paramsMap.put("id", classpageId);
				revealToClassPage(PlaceTokens.STUDENT,paramsMap);
			}
		}else{
			hideFromPopup(true);
			closePreviewPlayer();
		}
	}
	
	
	public void revealToClassPage(String placeToken,Map<String,String> paramsMap){
		paramsMap.put("pageNum", "0");
		paramsMap.put("pageSize", "10");
		paramsMap.put("pos", "1");
		PlaceRequest placeRequest=new PlaceRequest(placeToken);
		AppClientFactory.getPlaceManager().revealPlace(placeRequest, paramsMap);
	}
	
	@Override
	public void closePreviewPlayer() {
		AppClientFactory.fireEvent(new RefreshUserShelfCollectionsEvent());
		hide();
		getUiHandlers().resetCollectionPlayer();
	}

	@Override
	public Widget asWidget() {
		return appPopUp;
	}

	@Override
	protected String getDefaultView() {
		return PlaceTokens.HOME;
	}	
	
	/*private class UpdateThumbsDownEvent implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			if(AppClientFactory.isAnonymous()){
				getUiHandlers().showLoginPopupWidget(COLLECTION_RESOURCE_THUMBS_WIDGET_MODE);
			}else{
				int thumbsStaus=userRating==0||userRating==1?-1:0;
				getUiHandlers().updateResourceThumbsRating(thumbsStaus);
			}
		}
	}
	
	private class UpdateThumbsUpEvent implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			if(AppClientFactory.isAnonymous()){
				getUiHandlers().showLoginPopupWidget(COLLECTION_RESOURCE_THUMBS_WIDGET_MODE);
			}else{
				int thumbsStaus=userRating==0||userRating==-1?1:0;
				getUiHandlers().updateResourceThumbsRating(thumbsStaus);
			}
		}
	}*/
	@Override
	public void updateThumbsRatingView(int userThumbRating) {
		/*userRating=userThumbRating;
		if(userThumbRating==0){
			headerView.getThumbsDownButton().setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().thumbsDownNormal());
			headerView.getThumbsUpButton().setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().thumbsUpNormal());
		}else if(userThumbRating==-1){
			headerView.getThumbsDownButton().setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().thumbsDownActive());
			headerView.getThumbsUpButton().setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().thumbsUpNormal());
		}else if(userThumbRating==1){
			headerView.getThumbsDownButton().setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().thumbsDownNormal());
			headerView.getThumbsUpButton().setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().thumbsUpActive());
		}*/
		/*String resourcePlayerFirstTimeUser = Cookies.getCookie("resourcePlayerFirstTimeUser");
		if(resourcePlayerFirstTimeUser==null){
			Cookies.setCookie("resourcePlayerFirstTimeUser", "1");
			globalTooltipWithButton=new GlobalTooltipWithButton(i18n.GL0542, i18n.GL0543);
			globalTooltipWithButton.setGlassStyleName(HomeCBundle.INSTANCE.css().playerAddToolTipGlassStyle());
			globalTooltipWithButton.setStyleName("");
			globalTooltipWithButton.getElement().getStyle().setZIndex(999999);
			globalTooltipWithButton.setPopupPosition(headerView.getAddButton().getAbsoluteLeft() + 7, headerView.getAddButton().getAbsoluteTop()+25);
			globalTooltipWithButton.show();
		}*/
	}

	public int getUserRating() {
		return userRating;
	}

	public void setUserRating(int userRating) {
		this.userRating = userRating;
	}
	
	public void resetThumbsButtons(){
		/*userRating=0;
		headerView.getThumbsDownButton().setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().thumbsDownNormal());
		headerView.getThumbsUpButton().setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().thumbsUpNormal());*/
	}

	@Override
	public void defaultReportView() {
		footerView.getFlagButton().removeStyleName(PlayerBundle.INSTANCE.getPlayerStyle().flagButtonOrange());
		footerView.getFlagButton().removeStyleName(PlayerBundle.INSTANCE.getPlayerStyle().flagButtonActive());
		footerView.getFlagButton().setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().flagButtonDisable());
		
	}

	@Override
	public void flaggedReportView(ContentReportDo contentReportDo, String FlagId) {
		footerView.getFlagButton().removeStyleName(PlayerBundle.INSTANCE.getPlayerStyle().flagButtonDisable());
		footerView.getFlagButton().removeStyleName(PlayerBundle.INSTANCE.getPlayerStyle().flagButtonActive());
		footerView.getFlagButton().setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().flagButtonOrange());
		
	}

	@Override
	public void hideFlagButton(boolean hideButton) {
		//footerView.getFlagButton().setVisible(hideButton);
	}
	@Override
	public void makeFlagButtonOrange() {
		footerView.makeFlagButtonOrange();
	}

	/**
	 * This method is used to show first time Add tooltip popup.
	 */
	@Override
	public void showAddToolTip() {
/*		String resourcePlayerFirstTimeUser = Cookies.getCookie("resourcePlayerFirstTimeUser");
		if(resourcePlayerFirstTimeUser==null){
			Cookies.setCookie("resourcePlayerFirstTimeUser", "1");
			globalTooltipWithButton=new GlobalTooltipWithButton(i18n.GL0681, i18n.GL0543);
			globalTooltipWithButton.setGlassStyleName(HomeCBundle.INSTANCE.css().playerAddToolTipGlassStyle());
			globalTooltipWithButton.setStyleName("");
			globalTooltipWithButton.getElement().getStyle().setZIndex(999999);
			globalTooltipWithButton.setPopupPosition(headerView.getAddButton().getAbsoluteLeft() + 7, headerView.getAddButton().getAbsoluteTop()+25);
			globalTooltipWithButton.show();
		}*/
	}
	
	public void showLogoutMessage(boolean hide) {
		String resourcePlayerFirstTimeUser = Cookies.getCookie("lp");
		if(hide){
			if(logOutToolTip!=null){
				logOutToolTip.hide();
			}
		}else {
			if(AppClientFactory.isAnonymous()){
				if(resourcePlayerFirstTimeUser==null){
					//Cookies.setCookie("lp", "1");
					logOutToolTip=new GlobalTooltipWithButton(i18n.GL1614(),i18n.GL1615(), i18n.GL0543());
					logOutToolTip.getCloseButton().addClickHandler(new StoreCookieHandler());
					logOutToolTip.setGlassStyleName(HomeCBundle.INSTANCE.css().playerAddToolTipGlassStyle());
					logOutToolTip.setStyleName("");
					logOutToolTip.getElement().getStyle().setZIndex(999999);
					logOutToolTip.setPopupPosition(headerView.getAuthorContainer().getAbsoluteLeft() + 55, headerView.getAuthorContainer().getAbsoluteTop()+15);
					logOutToolTip.show();
				}
			}
		}
	}
	private class StoreCookieHandler implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			if(logOutToolTip!=null){
				Cookies.setCookie("lp", "1");
			}
		}
	}
	private class ShowLoginPopupEvent implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			if(AppClientFactory.isAnonymous()){
				getUiHandlers().showLoginPopupWidget(CollectionPlayerPresenter.UPDATE_HEADER);
			}
		}
	}
	
	@Override
	public void hidePlayerButtons(boolean isHidePlayerButtons,String collectionId) {
		if(collectionId==null){
			headerView.getAuthorContainer().setVisible(!isHidePlayerButtons);
			//headerView.getFlagButton().setVisible(isHidePlayerButtons);
			footerView.setVisible(!isHidePlayerButtons);
		}else{
			String view=AppClientFactory.getPlaceManager().getRequestParameter("view",null);
			if(view!=null&&view.equalsIgnoreCase("end")){
				headerView.getAuthorContainer().setVisible(false);
			}else{
				headerView.getAuthorContainer().setVisible(isHidePlayerButtons);
				headerView.displayAuthorName();
				showLogoutMessage(!isHidePlayerButtons);
			}
			//headerView.getFlagButton().setVisible(!isHidePlayerButtons);
			footerView.setVisible(!isHidePlayerButtons);
		}
		//headerView.getNavigationButton().setVisible(!isHidePlayerButtons);
//		headerView.getNarrationButton().setVisible(!isHidePlayerButtons);
//		headerView.getShareButton().setVisible(!isHidePlayerButtons);
//		headerView.getInfoButton().setVisible(!isHidePlayerButtons);
//		headerView.getAddButton().setVisible(!isHidePlayerButtons);
	}
	
	public void updateAuthorDetails(){
		headerView.displayAuthorName();
	}
	
	public void scrollStudyPage(){
		appPopUp.getElement().setScrollTop(300);
	}
	public void setUiText()
	{
		  menuContent.getElement().setId("menu");
		  playerContent.getElement().setId("page");
		  menuButton.getElement().setId("toggle-menu");
		  androidSectiondiv.getElement().setId("pnlAndroidSectiondiv");
		  closeAndriodBtn.getElement().setId("imgCloseAndriodBtn");
		  ipadSectiondiv.getElement().setId("pnlIpadSectiondiv");
		  closeIpadBtn.getElement().setId("imgCloseIpadBtn");
		  headerView.getElement().setId("studnetPlayerHeaderHeaderView");
		  navigationContainer.getElement().setId("fpnlNavigationContainer");
		  playerBodyContainer.getElement().setId("fpnlPlayerBodyContainer");
		  
		  msglinkPanel.getElement().setInnerHTML(i18n.GL1984());
		  msglinkPanel.getElement().setId("pnlMsglinkPanel");
		  msglinkPanel.getElement().setAttribute("alt",i18n.GL1984());
		  msglinkPanel.getElement().setAttribute("title",i18n.GL1984());

		  msgPanel.getElement().setInnerHTML(i18n.GL1983());
		  msgPanel.getElement().setId("pnlMsgPanel");
		  msgPanel.getElement().setAttribute("alt",i18n.GL1983());
		  msgPanel.getElement().setAttribute("title",i18n.GL1983());
		  
		  gooruPanel.getElement().setInnerHTML(i18n.GL0733());
		  gooruPanel.getElement().setId("pnlGooruPanel");
		  gooruPanel.getElement().setAttribute("alt",i18n.GL0733());
		  gooruPanel.getElement().setAttribute("title",i18n.GL0733());
		  
		  ednovoPanel.getElement().setInnerHTML(i18n.GL1985());
		  ednovoPanel.getElement().setId("pnlEdnovoPanel");
		  ednovoPanel.getElement().setAttribute("alt",i18n.GL1985());
		  ednovoPanel.getElement().setAttribute("title",i18n.GL1985());
		  
		  appstorePanel.getElement().setInnerHTML(i18n.GL1986());
		  appstorePanel.getElement().setId("pnlAppstorePanel");
		  appstorePanel.getElement().setAttribute("alt",i18n.GL1986());
		  appstorePanel.getElement().setAttribute("title",i18n.GL1986());
		  
		  viewAnchor.setText(i18n.GL1428());
		  viewAnchor.getElement().setId("lnkViewAnchor");
		  viewAnchor.getElement().setAttribute("alt",i18n.GL1428());
		  viewAnchor.getElement().setAttribute("title",i18n.GL1428());
	}
	
	public void addClonedMenuContent(FlowPanel rightPanelElement){
		menuContent.clear();
		if(rightPanelElement!=null){
			menuContent.add(rightPanelElement);
		}
	}
	
	private class ShowAuthorContainerEvent implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			invokeShowHideMenuContainer();
		}
	}
	private class ShowAuthorContainerTouchEvent implements TouchStartHandler{
		@Override
		public void onTouchStart(TouchStartEvent event) {
			invokeShowHideMenuContainer();
		}
	}
	
	public static native void invokeShowHideMenuContainer() /*-{
    	$wnd.showAuthorContianer();
	}-*/;
	
	public FlowPanel menuContent(){
		return menuContent;
	}
}
