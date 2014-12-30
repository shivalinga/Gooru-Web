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
package org.ednovo.gooru.client.mvp.home;

import java.util.HashMap;
import java.util.Map;

import org.ednovo.gooru.client.GooruCBundle;
import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SearchAsyncCallback;
import org.ednovo.gooru.client.SeoTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.classpages.ClasspageListVc;
import org.ednovo.gooru.client.mvp.classpages.event.ClearClasspageListEvent;
import org.ednovo.gooru.client.mvp.classpages.event.ClearClasspageListHandler;
import org.ednovo.gooru.client.mvp.classpages.event.DeleteClasspageListEvent;
import org.ednovo.gooru.client.mvp.classpages.event.DeleteClasspageListHandler;
import org.ednovo.gooru.client.mvp.classpages.event.OpenClasspageListEvent;
import org.ednovo.gooru.client.mvp.classpages.event.OpenClasspageListHandler;
import org.ednovo.gooru.client.mvp.home.event.HeaderTabType;
import org.ednovo.gooru.client.mvp.home.event.HomeEvent;
import org.ednovo.gooru.client.mvp.search.IsSearchView;
import org.ednovo.gooru.client.mvp.search.event.ConfirmStatusPopupEvent;
import org.ednovo.gooru.client.mvp.search.event.ConfirmStatusPopupHandler;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderEvent;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderHandler;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderZIndexEvent;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderZIndexHandler;
import org.ednovo.gooru.client.uc.AppMultiWordSuggestOracle;
import org.ednovo.gooru.client.uc.AppSuggestBox;
import org.ednovo.gooru.client.uc.HTMLEventPanel;
import org.ednovo.gooru.client.uc.tooltip.DashBoardToolTip;
import org.ednovo.gooru.client.uc.tooltip.DiscoverToolTip;
import org.ednovo.gooru.client.uc.tooltip.OrganizeToolTip;
import org.ednovo.gooru.client.uc.tooltip.StudyNowToolTip;
import org.ednovo.gooru.client.uc.tooltip.StudyToolTip;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.client.util.PlayerDataLogEvents;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.content.ClasspageListDo;
import org.ednovo.gooru.shared.model.search.AutoSuggestKeywordSearchDo;
import org.ednovo.gooru.shared.model.search.SearchDo;
import org.ednovo.gooru.shared.model.user.UserDo;
import org.ednovo.gooru.shared.util.DataLogEvents;
import org.ednovo.gooru.shared.util.GwtUUIDGenerator;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ScrollEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

/**
 * @author Search Team
 * 
 */
public class HeaderUc extends Composite implements
		SelectionHandler<SuggestOracle.Suggestion> {
	
	private static HeaderUcUiBinder uiBinder = GWT
			.create(HeaderUcUiBinder.class);

	interface HeaderUcUiBinder extends UiBinder<Widget, HeaderUc> {
	}

	private MessageProperties i18n = GWT.create(MessageProperties.class);
	
	OpenClasspageListHandler openClasspageListHandler = new OpenClasspageListHandler() {

		@Override
		public void openClasspageList() {
			OpenClasspageList();
		}
	};

	ClearClasspageListHandler clearHandler = new ClearClasspageListHandler() {

		@Override
		public void clearClasspage() {
			clearClasspageList();
		}
	};

	SetHeaderHandler setHeader = new SetHeaderHandler() {

		@Override
		public void setHeaderEvent(UserDo userDo) {
			AppClientFactory
					.setBrowserWindowTitle(SeoTokens.HOME_TITLE_LOGGEDIN);
			AppClientFactory
					.setMetaDataDescription(SeoTokens.HOME_META_DESCRIPTION);
			setLoggedInUser(userDo);
		}
	};

	SetHeaderZIndexHandler setZindex = new SetHeaderZIndexHandler() {

		@Override
		public void setHeaderZIndex(int value, boolean isClearZIndex) {
			Document doc = Document.get();
			if (isClearZIndex) {
				try {
					doc.getElementById("headerMainPanel").getStyle()
							.clearZIndex();
					doc.getElementById("goToClasicInnerPanel").getStyle()
							.clearZIndex();
				} catch (Exception ex) {
					// Window.confirm("cleasezindex ex : "+ex.getMessage());
				}
			} else {
				try {
					doc.getElementById("headerMainPanel").getStyle()
							.setZIndex(value);
					doc.getElementById("goToClasicInnerPanel").getStyle()
							.setZIndex(value);
				} catch (Exception e) {
					// Window.confirm("cleasezindex e : "+e.getMessage());
				}
			}
		}

	};

	ConfirmStatusPopupHandler confirmUser = new ConfirmStatusPopupHandler() {

		@Override
		public void setVisibility(boolean value) {
			if (AppClientFactory.getLoggedInUser().getConfirmStatus() == 0) {
				acctActivationPl.setVisible(true);
			} else {
				acctActivationPl.setVisible(false);
			}

			if (AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(
					PlaceTokens.HOME)) {
				/*acctActivationPl.getElement().getStyle()
						.setMarginTop(51, Unit.PX);*/
			} else {
				acctActivationPl.getElement().getStyle().clearMarginTop();
			}
		}
	};
	

	private static final int TOOLTIP_DELAY_TIME = 1000;
	private String name;

	@UiField(provided = true)
	public AppSuggestBox editSearchTxtBox;

	@UiField
	FlowPanel editSearchInputFloPanel,signUpInfo;

	@UiField
	FlowPanel logInfoFloPanel;

	@UiField
	Button editSearchBtn, registerLinkLbl;

	@UiField
	Anchor resendEmailAncr;

	@UiField
	Label logoutDownArrowLbl, loginLink,confirmEmailText;

	@UiField
	HTMLEventPanel acctActivationPl;
	
	@UiField Image imgUserProfile;
	
	DiscoverToolTip discoverToolTip;
	
	OrganizeToolTip organizeToolTip;
	
	static PreFilterPopup prefilter=null;
	
	static String stadardCode;

	boolean isGooruGuidePanelOpen = false;

	private boolean isOpenSettingDropDown = true;

	private boolean isSettingIcon = false;
	
	boolean isEnter = false;
	
	/*private boolean isDiscover= false;
	
	private boolean isOpenDiscoverTooltp= true;*/

	private boolean isClassCodePopupOpen = true;

	private boolean isStudyNow = false;
	
	private boolean hasAutoSelected = false;
	
	boolean hasClasses = false;
	
	@UiField HTMLPanel discovertooltippop,myCollectionsPop,myDashBoardPop;
	
	@UiField static HTMLPanel myClassesPop;
	
	String classpageId = "";
	/*
	 * @UiField Anchor registerLinkAnr;
	 */

	private LogoutPanelVc logoutPanelVc;

	private ClasspageListVc classpageListVc;

	private SaveSharePanel saveSharePanel;

	@Inject
	HomeUiHandlers homeUiHandlers;

	@Inject
	HomePresenter homePresenter;

	@UiField
	AnchorElement gooruLearning;

	@UiField
	VerticalPanel headerSearchBarVerPanel;

	@UiField
	FlowPanel headerSearchBarFloPanel;

	@UiField(provided = true)
	GooruCBundle res;

	@UiField
	Label lblBeta; // gooruClassicViewLbl

	@UiField
	public static HTMLPanel dotsPanel, mainDotsPanel, mainInnerDotsPanel,dropDownImg,dropDownImgforDashboard;

	@UiField
	Label discoverLink, organizeLink, teachLink, studyLink, loggedInfoLbl,thanksLbl;

	@UiField
	Label arrowLbl;

	@UiField
	HTMLEventPanel discoverLinkContainer, organizeLinkContainer,organizeLinkMain,teachLinkMain,discoverLinkMain,
			teachLinkContainer, studyLinkContainer, LoginLinkContainer;

	Label noneMenu = null;

	@UiField
	HTMLPanel headerMainPanel;

	// @UiField TextBoxWithPlaceholder classCodeTxtBox;

	private UserDo userDo;
	private LoginPopupUc popup;

	private StudyNowToolTip studyNowToolTip;
	private static boolean addedAccounts = false;

	private PopupPanel toolTipPopupPanel = new PopupPanel();
	private SearchDo<AutoSuggestKeywordSearchDo> autoSuggestKeywordDo = new SearchDo<AutoSuggestKeywordSearchDo>();
	private SearchAsyncCallback<SearchDo<AutoSuggestKeywordSearchDo>> autoKeyWordSuggestionAsyncCallback;

	private Timer tooltipTimer = null;

	private static final String GOORU_UID = "gooruUid";

	private static final String ACCOUNT_TYPE = "accountType";
	private AppMultiWordSuggestOracle autokeySuggestOracle;
	String searchData = "";
	private String GOORU_SEARCH = " -<n> Gooru Search</n>";

	private String discoverLinkUrl = null;

	
	private static String DEFAULT_PROFILE_IMAGE="images/settings/setting-user-image.png";
	
	DashBoardToolTip dashBoardToolTip;

	/**
	 * Class constructor , set logged in user , gooru classic view link
	 */
	@SuppressWarnings("deprecation")
	public HeaderUc() {
		this.res = GooruCBundle.INSTANCE;
		res.css().ensureInjected();
		autokeySuggestOracle = new AppMultiWordSuggestOracle(true);
		setEditSearchTxtBox(new AppSuggestBox(autokeySuggestOracle) {

			@Override
			public HandlerRegistration addClickHandler(ClickHandler handler) {
				return null;
			}

			@Override
			public void keyAction(String text) {
				MixpanelUtil.Search_autocomplete_select();
				autokeySuggestOracle.clear();
				autoSuggestKeywordDo.setQuery(text);
				searchData = getEditSearchTxtBox().getText();
				autoSuggestKeywordDo.setType("resource");
				if (text != null && text.trim().length() > 0) {
					requestAutoSuggestKeyword(autoSuggestKeywordDo);
				} else {
					getEditSearchTxtBox().hideSuggestionList();
				}
			}
		});
		getEditSearchTxtBox().addSelectionHandler(this);
		getEditSearchTxtBox().setPopupStyleName("shelfEditSearchTextBox");
		Window.addWindowScrollHandler(new Window.ScrollHandler() {
		    @Override
		    public void onWindowScroll(ScrollEvent event) {
		    	getEditSearchTxtBox().hideSuggestionList();   	
			}
		});
		initWidget(uiBinder.createAndBindUi(this));
		
		headerMainPanel.getElement().setAttribute("id", "headerMainPanel");

		logoutPanelVc = new LogoutPanelVc();

		saveSharePanel = new SaveSharePanel() {

			@Override
			@UiHandler("closeButton")
			public void closeButton(ClickEvent clickEvent) {
				isGooruGuidePanelOpen = false;
				Window.enableScrolling(true);
				hide();
			}
		};

//		mainDotsPanel.setVisible(false);
		logoutPanelVc.setStyleName(GooruCBundle.INSTANCE.css().logoutPanel());
		editSearchInputFloPanel.setVisible(false);
		LoginLinkContainer.setVisible(false);
		loggedInfoLbl.setVisible(false);
		loggedInfoLbl.getElement().getStyle().clearWidth();
		imgUserProfile.setVisible(false);
		logoutDownArrowLbl.setVisible(false);
		
		
		imgUserProfile.addErrorHandler(new ErrorHandler() {
			
			@Override
			public void onError(ErrorEvent event) {
				imgUserProfile.setUrl(DEFAULT_PROFILE_IMAGE);
			}
		});
		
		/*
		 * classCodeTxtBox.setText("");
		 * classCodeTxtBox.getElement().setAttribute("maxlength", "10");
		 * classCodeTxtBox.getElement().setId("txtClassCode");
		 */

//		registerLinkLbl.addClickHandler(new studyClickHandler());
	/*	getEditSearchTxtBox().addKeyUpHandler(new SearchKeyUpHandler());*/
		getEditSearchTxtBox().addKeyDownHandler(new SearchKeyDownHandler());
//		getEditSearchTxtBox().addFocusListener(new SearchClickHandler());
		editSearchInputFloPanel.setVisible(false);
		// gooruGuideImgLbl.setStyleName(GooruCBundle.INSTANCE.css().gooruGuideImg());
		this.switchToClassicView();
		headerSearchBarVerPanel.setCellVerticalAlignment(
				headerSearchBarFloPanel, HasVerticalAlignment.ALIGN_MIDDLE);

		noneMenu = new Label();

		discoverLink.getParent().getElement().setId("LinkheaderElement1");
		organizeLink.getParent().getElement().setId("LinkheaderElement2");
		teachLink.getParent().getElement().setId("LinkheaderElement3");
		studyLink.getParent().getElement().setId("LinkheaderElement4");
		loggedInfoLbl.getParent().getElement().setId("LinkheaderElement5");

		discoverLink.getParent().setStyleName(
				GooruCBundle.INSTANCE.css().menu());
		
/*		discoverLink.getParent().getElement().setAttribute("style", "border-right: 1px solid white;");
		loggedInfoLbl.getParent().getElement().setAttribute("style", "border-right: 1px solid white;");*/

		noneMenu = new Label();

//		activateOnlyDiscover();
		activateAllMenu();

		discoverLinkContainer
				.addClickHandler(new OnClickDiscoverEventHandler());

		organizeLinkContainer
				.addClickHandler(new OnClickOrganizeEventHandler());
		

			organizeToolTip=new OrganizeToolTip();
			organizeToolTip.getElement().getStyle().setBackgroundColor("transparent");
			organizeToolTip.getElement().getStyle().setPosition(Position.ABSOLUTE);
			organizeToolTip.getElement().getStyle().setZIndex(99);
			myCollectionsPop.add(organizeToolTip);
			myCollectionsPop.getElement().getStyle().setPosition(Position.ABSOLUTE);
			myCollectionsPop.getElement().getStyle().setZIndex(99);
			myCollectionsPop.setVisible(false);
		
		
		
	
			organizeLinkMain.addMouseOverHandler(new OrganizeMouseOver());
			organizeLinkMain.addMouseOutHandler(new OrganizeMouseOut());
	
		
		

		teachLinkContainer.addClickHandler(new OnClickTeachEventHandler());
		
		

		classpageListVc = new ClasspageListVc(false,null);
		
		classpageListVc.setWidth("202px !important");
		classpageListVc.setHeight("246px !important");
		classpageListVc.setStyleName(HomeCBundle.INSTANCE.css()
				.classpageListContainer());
		myClassesPop.add(classpageListVc);
		myClassesPop.getElement().getStyle().setPosition(Position.ABSOLUTE);
		myClassesPop.getElement().getStyle().setMarginTop(50, Unit.PX);
		myClassesPop.getElement().getStyle().setZIndex(99);
		myClassesPop.setVisible(false);
		//classpageListVc.setPopupPosition(left, top);
		
		teachLinkMain.addMouseOverHandler(new TeachMouseOver());
		teachLinkMain.addMouseOutHandler(new TeachMouseOut());
		
/*		loggedInfoLbl
		.addClickHandler(new OnClickDashBoardEventHandler());*/


		dashBoardToolTip=new DashBoardToolTip() {
			
			@Override
			public void fireSelectionEvent() {
				if (!AppClientFactory.isAnonymous()){
					AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.DASHBOARD);
					manageDotsMenuSelection(loggedInfoLbl);
				}
				
			}
		};
		dashBoardToolTip.getElement().getStyle().setBackgroundColor("transparent");
		dashBoardToolTip.getElement().getStyle().setPosition(Position.ABSOLUTE);
		dashBoardToolTip.getElement().getStyle().setZIndex(99);
		dashBoardToolTip.getElement().getStyle().setLeft(loggedInfoLbl.getAbsoluteLeft(), Unit.PX);
		myDashBoardPop.add(dashBoardToolTip);
		myDashBoardPop.getElement().getStyle().setPosition(Position.ABSOLUTE);
		myDashBoardPop.getElement().getStyle().setZIndex(99);
		myDashBoardPop.setVisible(false);

//		LoginLinkContainer.addMouseOverHandler(new DashBoardMouseOver());
//		LoginLinkContainer.addMouseOutHandler(new DashBoardMouseOut());


		organizeLinkMain.addMouseOverHandler(new OrganizeMouseOver());
		organizeLinkMain.addMouseOutHandler(new OrganizeMouseOut());
		

		studyLinkContainer.addClickHandler(new studyClickHandler());

		// gooruClassicViewLbl.setText(MessageProperties.i18n.GL0094);
		getEditSearchTxtBox().getElement().setId("txtEditSearch");
		editSearchBtn.getElement().setId("btnEditSearch");
		editSearchBtn.setText(i18n.GL0176());
		editSearchBtn.getElement().setAttribute("alt",i18n.GL0176());
		editSearchBtn.getElement().setAttribute("title",i18n.GL0176());
		
		confirmEmailText.getElement().setAttribute("style", "float: left;font-size:11px;");
		confirmEmailText.setText(i18n.GL1248());
		confirmEmailText.getElement().setId("lblConfirmEmailText");
		confirmEmailText.getElement().setAttribute("alt",i18n.GL1248());
		confirmEmailText.getElement().setAttribute("title",i18n.GL1248());
		
		resendEmailAncr.getElement().setAttribute("style", "float: left;font-size:11px;");
		resendEmailAncr.setText(i18n.GL1249());
		resendEmailAncr.getElement().setId("lnkResendEmail");
		resendEmailAncr.getElement().setAttribute("alt",i18n.GL1249());
		resendEmailAncr.getElement().setAttribute("title",i18n.GL1249());
		
		/*thanksLbl.getElement().setAttribute("style", "padding-left:5px;");
		thanksLbl.setText(i18n.GL0498()+i18n.GL_SPL_EXCLAMATION());
		thanksLbl.getElement().setId("lblThanksLbl");
		thanksLbl.getElement().setAttribute("alt",i18n.GL0498());
		thanksLbl.getElement().setAttribute("title",i18n.GL0498());*/
		
		getEditSearchTxtBox().getElement().setAttribute("placeholder",
				i18n.GL0177());
		
		lblBeta.setText(i18n.GL0178());
		lblBeta.getElement().setId("lblBeta");
		lblBeta.getElement().setAttribute("alt",i18n.GL0178());
		lblBeta.getElement().setAttribute("title",i18n.GL0178());
		
		getBetaStatus();
		
		discoverLink.setText(i18n.GL1748_1());
		discoverLink.getElement().setId("lblDiscoverLink");
		discoverLink.getElement().setAttribute("alt",i18n.GL1748_1());
		discoverLink.getElement().setAttribute("title",i18n.GL1748_1());
		
		organizeLink.setText(i18n.GL1752());
		organizeLink.getElement().setId("lblOrganizeLink");
		organizeLink.getElement().setAttribute("alt",i18n.GL1752());
		organizeLink.getElement().setAttribute("title",i18n.GL1752());
		
		teachLink.setText(i18n.GL1753());
		teachLink.getElement().setId("lblTeachLink");
		teachLink.getElement().setAttribute("alt",i18n.GL1753());
		teachLink.getElement().setAttribute("title",i18n.GL1753());
		
		studyLink.setText(i18n.GL0182());//not used.
		studyLink.getElement().setId("lblStudyLink");
		studyLink.getElement().setAttribute("alt",i18n.GL0182());
		studyLink.getElement().setAttribute("title",i18n.GL0182());
		
		loggedInfoLbl.setText(i18n.GL0183());
		loggedInfoLbl.getElement().setId("lblLoggedInfo");
		loggedInfoLbl.getElement().setAttribute("alt",i18n.GL0183());
		loggedInfoLbl.getElement().setAttribute("title",i18n.GL0183());
		
		// classCodeTxtBox.setPlaceholder(MessageProperties.i18n.GL0184);
//		StudyLbl.setText(i18n.GL0185);
//		StudyLbl.getElement().setId("btnStudy");
		registerLinkLbl.setText(i18n.GL0186());
		registerLinkLbl.getElement().setId("btnRegister");
		registerLinkLbl.getElement().setAttribute("alt",i18n.GL0186());
		registerLinkLbl.getElement().setAttribute("title",i18n.GL0186());
		
		loginLink.setText(i18n.GL0187());
		loginLink.getElement().setId("lblLogin");
		loginLink.getElement().setAttribute("alt",i18n.GL0187());
		loginLink.getElement().setAttribute("title",i18n.GL0187());
		
		headerSearchBarVerPanel.getElement().setId("vsbHeaderSearchBarVerPanel");
		headerSearchBarFloPanel.getElement().setId("fpnlHeaderSearchBarFloPanel");
		editSearchTxtBox.getElement().setId("tbautoEditSearchTxtBox");
		StringUtil.setAttributes(editSearchTxtBox, true);
		mainDotsPanel.getElement().setId("pnlMainDotsPanel");
		mainInnerDotsPanel.getElement().setId("pnlMainInnerDotsPanel");
		dotsPanel.getElement().setId("pnlDotsPanel");
		

		dropDownImg.getElement().setId("pnlDropDownImg");
		dropDownImgforDashboard.getElement().getStyle().setMarginTop(14, Unit.PX);
		dropDownImgforDashboard.setVisible(false);
		signUpInfo.getElement().setId("fpnlSignUpInfo");
		logoutDownArrowLbl.getElement().setId("lblLogoutDownArrow");
		logInfoFloPanel.getElement().setId("fpnlLogInfoFloPanel");
		acctActivationPl.getElement().setId("epnlAcctActivationPl");
		

		
		
		discoverToolTip=new DiscoverToolTip();
		discoverToolTip.getElement().getStyle().setBackgroundColor("transparent");
		discoverToolTip.getElement().getStyle().setPosition(Position.ABSOLUTE);
		discoverToolTip.getElement().getStyle().setZIndex(99);
		
	

		
		discovertooltippop.add(discoverToolTip);
		
		discovertooltippop.getElement().getStyle().setPosition(Position.ABSOLUTE);
		discovertooltippop.getElement().getStyle().setMarginLeft(-4, Unit.PX);
		discovertooltippop.getElement().getStyle().setZIndex(99);
		
		discovertooltippop.setVisible(false);
		
		
		discoverLinkMain.addMouseOverHandler(new DiscoverMouseOver());
		discoverLinkMain.addMouseOutHandler(new DiscoverMouseOut());
		
		studyLinkContainer.setVisible(false);
		ClickHandler eve1 = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (!isSettingIcon) {
					isOpenSettingDropDown = true;
				} else {
					isSettingIcon = false;
				}
				if (!isStudyNow && studyNowToolTip != null) {
					isClassCodePopupOpen = true;
					studyNowToolTip.hide();
					studyNowToolTip.getClassCodeTxtBox().setText("");
				} else {
					isStudyNow = false;
				}
				/*if(!isDiscover && discoverToolTip != null){
					isOpenDiscoverTooltp=true;
					discoverToolTip.hide();
				}else{
					isDiscover=false;
				}*/
			}
		};
		acctActivationPl.setVisible(false);
		resendEmailAncr.addClickHandler(new ResendEmailConfirmation());

		// goToClasicGooruPanel.setVisible(false);
		// goToClasicInnerPanel.getElement().setAttribute("id",
		// "goToClasicInnerPanel");
		// goToClasicGooruPanel.getElement().getStyle().setVisibility(Visibility.HIDDEN);
		if (!AppClientFactory.isAnonymous()) {
			if (AppClientFactory.getLoggedInUser().getConfirmStatus() == 0) {
				acctActivationPl.setVisible(true);
			}
		} else {
			acctActivationPl.setVisible(false);
		}

		ClickHandler dotsClick = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getEditSearchTxtBox().setText("");
			}
		};
		mainDotsPanel.addDomHandler(dotsClick, ClickEvent.getType());

		AppClientFactory.getEventBus().addHandler(SetHeaderEvent.TYPE,
				setHeader);
		AppClientFactory.getEventBus().addHandler(SetHeaderZIndexEvent.TYPE,
				setZindex);
		AppClientFactory.getEventBus().addHandler(ClearClasspageListEvent.TYPE,
				clearHandler);
		AppClientFactory.getEventBus().addHandler(OpenClasspageListEvent.TYPE,
				openClasspageListHandler);
		AppClientFactory.getEventBus().addHandler(ConfirmStatusPopupEvent.TYPE, confirmUser);
		
		AppClientFactory.getEventBus().addHandler(DeleteClasspageListEvent.TYPE, deleteHandler);

		
		RootPanel.get().addDomHandler(eve1, ClickEvent.getType());

		/*
		 * if((AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens
		 * .SHELF))||(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(
		 * PlaceTokens
		 * .STUDY)||(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase
		 * (PlaceTokens
		 * .TEACH)))||(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase
		 * (PlaceTokens
		 * .SETTINGS))||(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase
		 * (PlaceTokens.PROFILE_PAGE))){ editSearchTxtBox.setText(""); }
		 */
		discoverLinkUrl = null;
		gooruLearning.setId("lnkeleGooruLearning");
		manageDotsMenuSelection(noneMenu);
		String emailId= AppClientFactory.getPlaceManager()
				.getRequestParameter("emailId");
	//	StringUtil.consoleLog("emailId..in header."+emailId);
		if(emailId!=null)
		{
			
			AppClientFactory.getInjector().getUserService().getRefershToken(AppClientFactory.getLoggedInUser().getGooruUId(),new AsyncCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
				//	StringUtil.consoleLog("Header UC RefershToken..."+result);
						UserDo user = AppClientFactory.getLoggedInUser();
						user.setRefreshToken(result);
						AppClientFactory.setLoggedInUser(user);
									
				}
				
				@Override
				public void onFailure(Throwable caught) {
				//	StringUtil.consoleLog("Header UC onFailure...");				
				}
			});
		}
	}



	/**
	 * @function getBetaStatus 
	 * 
	 * @created_date : Aug 14, 2014
	 * 
	 * @description
	 * 
	 * 
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 * 
	*/
	
	private void getBetaStatus() {
		try {
			new RequestBuilder(RequestBuilder.GET, "./images/json/product-beta-status.json").sendRequest("", new RequestCallback() {
				  @Override
				  public void onResponseReceived(Request req, Response resp) {
					  boolean status = getStatus(resp.getText());
					  if (status){
						  lblBeta.getElement().getStyle().setVisibility(Visibility.VISIBLE);
					  }else{
						  lblBeta.getElement().getStyle().setVisibility(Visibility.HIDDEN);
					  }
				  }

				  @Override
				  public void onError(Request res, Throwable throwable) {
					  
				  }
				});
		} catch (RequestException e) {
			
		}
	}
	/**
	 * 
	 * @function getStatus 
	 * 
	 * @created_date : Aug 14, 2014
	 * 
	 * @description
	 * 
	 * 
	 * @param text
	 * @return
	 * 
	 * @return : boolean
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	private boolean getStatus(String text) {
		boolean status = false;

		JSONValue jsonValue = JSONParser.parse(text);
        JSONObject jsonObject = jsonValue.isObject();
        
        status = jsonObject.get("betaStatus").isBoolean() !=null ? jsonObject.get("betaStatus").isBoolean().booleanValue() : false;
        
		return status;
	}

	public void clearClasspageList() {
	//	classpageListVc = null;
		studyNowToolTip = null;
	}

	/**
	 * Signin using the popup
	 * 
	 * @param clickEvent
	 *            instance of {@link ClickEvent}
	 */
	@UiHandler("loginLink")
	public void onLinkPopupClicked(ClickEvent clickEvent) {

		AppClientFactory.fireEvent(new SetHeaderZIndexEvent(99, false));
		popup = new LoginPopupUc(this);
		popup.setGlassEnabled(true);
		popup.center();
		popup.show();
		if (name != null) {
			if (name.equalsIgnoreCase("teach")) {
				popup.setNameToken(PlaceTokens.TEACH);
			} else if (name.equalsIgnoreCase("organize")) {
				popup.setNameToken(PlaceTokens.SHELF);
			}
			name = null;
		}
	}

	/*
	 * Selection Control DOTS panel
	 * 
	 * @param Label
	 */

	public void manageDotsMenuSelection(Label dotsLink) {

		for (int i = 1; i <=5; i++) {

			if (userDo != null
					&& !userDo.getUserUid().equals(
							AppClientFactory.GOORU_ANONYMOUS)) {
				try {
					Document.get()
							.getElementById("LinkheaderElement" + i)
							.removeClassName(GooruCBundle.INSTANCE.css().menuActive());
					Document.get()
					.getElementById("LinkheaderElement" + i)
					.removeClassName(GooruCBundle.INSTANCE.css().menu());
					
					Document.get().getElementById("LinkheaderElement" + i)
							.addClassName(GooruCBundle.INSTANCE.css().menu());
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				activateOnlyDiscover();

			}

		}
		if (dotsLink.equals(noneMenu)) {
			
		} else {
			
			dotsLink.getParent().setStyleName(
					GooruCBundle.INSTANCE.css().menuActive());
		}
	}

	public void activateOnlyDiscover() {
		organizeLink.getParent().setStyleName(
				GooruCBundle.INSTANCE.css().menu());
		teachLink.getParent().setStyleName(GooruCBundle.INSTANCE.css().menu());
		studyLink.getParent().setStyleName(GooruCBundle.INSTANCE.css().menu());
		loggedInfoLbl.getParent().setStyleName(
				GooruCBundle.INSTANCE.css().menu());
		discoverLink.getParent().setStyleName(
				GooruCBundle.INSTANCE.css().menu());
/*		if (classpageListVc != null) {
			if (classpageListVc.isShowing()) {
				classpageListVc.hide();
			}
		}*/

		if (logoutPanelVc != null) {
			if (logoutPanelVc.isShowing()) {
				logoutPanelVc.hide();
			}
			if (logoutPanelVc.logoutPopupVc != null) {
				if (logoutPanelVc.logoutPopupVc.appPopUp != null) {
					if (logoutPanelVc.logoutPopupVc.appPopUp.isShowing()) {
						logoutPanelVc.logoutPopupVc.appPopUp.hide();
					}
				}
			}

		}

	}

	public void activateAllMenu() {
		discoverLink.getParent().setStyleName(
				GooruCBundle.INSTANCE.css().menu());
		organizeLink.getParent().setStyleName(
				GooruCBundle.INSTANCE.css().menu());
		teachLink.getParent().setStyleName(GooruCBundle.INSTANCE.css().menu());
		studyLink.getParent().setStyleName(GooruCBundle.INSTANCE.css().menu());
		loggedInfoLbl.getParent().setStyleName(
				GooruCBundle.INSTANCE.css().menu());
	}

	/**
	 * Register using the popup
	 * 
	 * @param clickEvent
	 *            instance of {@link ClickEvent}
	 */
	@UiHandler("registerLinkLbl")
	public void onRegisterPopupClicked(ClickEvent clickEvent) {
		MixpanelUtil.Arrive_Register_popup();
		
		DataLogEvents.signUp(GwtUUIDGenerator.uuid(), "home",
				PlayerDataLogEvents.getUnixTime(), PlayerDataLogEvents.getUnixTime(), "");

		// RegisterVc registerVc = new RegisterVc();
		// registerVc.show();
		// registerVc.center();

		Map<String, String> map = StringUtil.splitQuery(Window.Location
				.getHref());
		if(map.containsKey("query"))
		{
			String queryVal = AppClientFactory.getPlaceManager().getRequestParameter("query");
			queryVal = queryVal.replace("+", " ");
			map.put("query", queryVal);
			editSearchTxtBox.setText(queryVal);
		}
		if(map.containsKey("flt.subjectName"))
		{
			String subjectNameVal = AppClientFactory.getPlaceManager().getRequestParameter("flt.subjectName");
			subjectNameVal = subjectNameVal.replace("+", " ");
			map.put("flt.subjectName", subjectNameVal);
		}
		map.put("callback", "signup");
		map.put("type", "1");
		//AppClientFactory.getPlaceManager().revealPlace(AppClientFactory.getCurrentPlaceToken(), map);
		PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(AppClientFactory.getCurrentPlaceToken(), map);
		AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, false);

	}

	/* Click event hanlder for Menu items */
	public class OnClickDiscoverEventHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			Window.enableScrolling(true);
			Cookies.setCookie("searchvalue", "");
			AppClientFactory.fireEvent(new SetHeaderZIndexEvent(99, true));
			Element e = null;
			if ((e = event.getRelativeElement()) != null) {
				if (e.getInnerHTML() != null
						&& e.getInnerHTML().contains("gwt-Label"))
					MixpanelUtil.Click_Discover_LandingPage();
			}
			manageDotsMenuSelection(discoverLink);
			if ((discoverLinkUrl != null)
					&& (!discoverLinkUrl.contains("featured-contributors"))) {
				Map<String, String> params = StringUtil
						.splitQuery(discoverLinkUrl);
				AppClientFactory.getPlaceManager().revealPlace(
						PlaceTokens.DISCOVER, params);
			} else {
				AppClientFactory.getPlaceManager()
						.revealPlace(PlaceTokens.DISCOVER);
			}
		}

	}

	public class OnClickOrganizeEventHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			AppClientFactory.setPreviousPlaceRequest(AppClientFactory
					.getPlaceManager().getCurrentPlaceRequest());
			Storage stockStore = Storage.getLocalStorageIfSupported();

			if (stockStore != null) {
				stockStore.setItem("tabKey", "resourceTab");
			}
			name = "organize";
			if (AppClientFactory.isAnonymous()){
				Window.enableScrolling(true);
			}else{
				Window.enableScrolling(false);
			}
			AppClientFactory.fireEvent(new SetHeaderZIndexEvent(98, true));
			manageDotsMenuSelection(organizeLink);
			AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.SHELF);
		}
	}

	public class OnClickTeachEventHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			Window.enableScrolling(true);
			AppClientFactory.fireEvent(new SetHeaderZIndexEvent(98, true));
			if (userDo != null
					&& !userDo.getUserUid().equals(
							AppClientFactory.GOORU_ANONYMOUS)) {
				
//				OpenClasspageList();
				
			
				AppClientFactory.getInjector().getClasspageService().v2GetAllClass("10", "0",new SimpleAsyncCallback<ClasspageListDo>() {
						@Override
						public void onSuccess(ClasspageListDo result) {
					//	hasClasses = result.getSearchResults().size() > 0 ? true : false; 
							if(result.getSearchResults()!=null){
							if (result.getSearchResults().size()>0){
								AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.CLASSHOME);
								////classpageId = result.getSearchResults().get(0).getGooruOid();
								//String userId = result.getSearchResults().get(0).getUser().getGooruUId();
							//OpenClasspageEdit(classpageId, userId);
								//AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.CLASSHOME,null,true);
							}else{
								AppClientFactory.getPlaceManager().redirectPlace(PlaceTokens.STUDY);
								//AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.STUDY);
							}
						}else
						{
							AppClientFactory.getPlaceManager().redirectPlace(PlaceTokens.STUDY);
						}
						}
				});
			} else {
				name = "teach";
				
//				onLinkPopupClicked(null);
//				TODO need to show new logout page....
				AppClientFactory.getPlaceManager().redirectPlace(PlaceTokens.STUDY);
			}
		}

	}

	public class OnClickDashBoardEventHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			name = "dashboard";
			if (AppClientFactory.isAnonymous()){
				Window.enableScrolling(true);
			}else{
				Window.enableScrolling(true);
			}
			AppClientFactory.fireEvent(new SetHeaderZIndexEvent(98, true));
			manageDotsMenuSelection(loggedInfoLbl);
			AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.DASHBOARD);
		}
	}
	
	
	public void OpenClasspageList() {
	/*	int left = teachLinkContainer.getAbsoluteLeft();
		int top = teachLinkContainer.getAbsoluteTop() + 50;
		showTeachPanelAsPopup(left, top);*/
	}

	//This method is not used now. Even the mixpanel event is moved to StudyNowToolTip.java
/*	public class OnClickStudyEventHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			Window.enableScrolling(true);
			AppClientFactory.fireEvent(new SetHeaderZIndexEvent(99, true));
			Element e = null;

			if ((e = event.getRelativeElement()) != null) {
				if (e.getInnerHTML() != null
						&& e.getInnerHTML().contains("gwt-Label"))
					MixpanelUtil.Click_Study_LandingPage();
			}
			manageDotsMenuSelection(studyLink);
			AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.STUDY);
		}
	}
*/
	/* On mouse over and out handlers */
	public class DiscoverMouseOver implements MouseOverHandler {
		@Override
		public void onMouseOver(MouseOverEvent event) {
		//	discovertooltippop.setPopupPosition(event.getRelativeElement().getAbsoluteLeft(), event.getRelativeElement().getAbsoluteTop() + 50);
			discovertooltippop.setVisible(true);
		}
	}

	public class DiscoverMouseOut implements MouseOutHandler {

		@Override
		public void onMouseOut(MouseOutEvent event) {
			discovertooltippop.setVisible(false);
		}
	}

	public class OrganizeMouseOver implements MouseOverHandler {

		@Override
		public void onMouseOver(final MouseOverEvent event) {
			if (!AppClientFactory.isAnonymous()){
			myCollectionsPop.setVisible(true);
			}
		}
	}

	public class OrganizeMouseOut implements MouseOutHandler {

		@Override
		public void onMouseOut(MouseOutEvent event) {
			if (!AppClientFactory.isAnonymous()){
			myCollectionsPop.setVisible(false);
			}
		}
	}

	
	public class DashBoardMouseOver implements MouseOverHandler {

		@Override
		public void onMouseOver(final MouseOverEvent event) {
			if (!AppClientFactory.isAnonymous()){
				myDashBoardPop.setVisible(true);
			}
		}
	}

	public class DashBoardMouseOut implements MouseOutHandler {

		@Override
		public void onMouseOut(MouseOutEvent event) {
			if (!AppClientFactory.isAnonymous()){
				myDashBoardPop.setVisible(false);
			}
		}
	}
	public class TeachMouseOver implements MouseOverHandler {

		@Override
		public void onMouseOver(final MouseOverEvent event) {
			if (!AppClientFactory.isAnonymous()){
				myClassesPop.setVisible(true);
				
			}
		}
	}

	public class TeachMouseOut implements MouseOutHandler {

		@Override
		public void onMouseOut(MouseOutEvent event) {
			if (!AppClientFactory.isAnonymous()){
				
				myClassesPop.setVisible(false);
			}
		}

	}

	public class StudyMouseOver implements MouseOverHandler {

		@Override
		public void onMouseOver(final MouseOverEvent event) {
			toolTipPopupPanel.clear();
			toolTipPopupPanel.setWidget(new StudyToolTip());
			toolTipPopupPanel.setStyleName("");
			if (event.getSource().equals(studyLink)) {
				toolTipPopupPanel.setPopupPosition(event.getRelativeElement()
						.getAbsoluteLeft() - 101, event.getRelativeElement()
						.getAbsoluteTop() + 25);
			} else {
				toolTipPopupPanel.setPopupPosition(event.getRelativeElement()
						.getAbsoluteLeft() - 85, event.getRelativeElement()
						.getAbsoluteTop() + 41);
			}

//			tooltipTimer = new Timer() {
//				public void run() {
					toolTipPopupPanel.show();
//				}
//			};
//			tooltipTimer.schedule(TOOLTIP_DELAY_TIME);
		}
	}

	public class StudyMouseOut implements MouseOutHandler {

		@Override
		public void onMouseOut(MouseOutEvent event) {
//			tooltipTimer.cancel();
			toolTipPopupPanel.hide();
		}
	}

	/*
	 * @UiHandler("gooruGuideImgLbl") public void mouseOverOnGooruGuide(final
	 * MouseOverEvent event){
	 * 
	 * 
	 * toolTipPopupPanel.clear(); toolTipPopupPanel.setWidget(new
	 * GooruGuideToolTip()); toolTipPopupPanel.setStyleName("");
	 * toolTipPopupPanel
	 * .setPopupPosition(event.getRelativeElement().getAbsoluteLeft()-215,
	 * event.getRelativeElement().getAbsoluteTop()+28); tooltipTimer=new
	 * Timer(){ public void run() { toolTipPopupPanel.show(); } };
	 * tooltipTimer.schedule(TOOLTIP_DELAY_TIME); }
	 * 
	 * @UiHandler("gooruGuideImgLbl") public void
	 * mouseOutOnGooruGuide(MouseOutEvent event){ tooltipTimer.cancel();
	 * toolTipPopupPanel.hide(); }
	 */

	public void manageDotsMenuSelectionFromEvent(HeaderTabType tabType) {

		if (tabType == HeaderTabType.DISCOVER) {
			manageDotsMenuSelection(discoverLink);
		} else if (tabType == HeaderTabType.ORGANIZE) {
			manageDotsMenuSelection(organizeLink);
		} else if (tabType == HeaderTabType.TEACH) {
			manageDotsMenuSelection(teachLink);
		} else if (tabType == HeaderTabType.STUDY) {
			manageDotsMenuSelection(studyLink);
		} else if (tabType == HeaderTabType.USERNAME) {
			manageDotsMenuSelection(loggedInfoLbl);
		}else if (tabType == HeaderTabType.HOME){
			
		}else {
			manageDotsMenuSelection(noneMenu);
		}

	}

	/**
	 * View about goorulearning link , logout link
	 * 
	 * @param clickEvent
	 *            instance of {@link ClickEvent}
	 */
	@UiHandler("logoutDownArrowLbl")
	public void logoutPanel(ClickEvent clickEvent) {
		int left = logoutDownArrowLbl.getAbsoluteLeft() - 77;
		int top = logoutDownArrowLbl.getAbsoluteTop() + 41;
		isSettingIcon = true;
		if (isOpenSettingDropDown) {
			showLogoutPopup(left, top);
		} else {
			logoutPanelVc.hide();
			isOpenSettingDropDown = true;
		}
	}

	/**
	 * View sign out popup
	 * 
	 * @param clickEvent
	 *            instance of {@link ClickEvent}
	 */
	@UiHandler("loggedInfoLbl")
	public void signoutPanel(ClickEvent clickEvent) {

		/*
		 * int left = logoutDownArrowLbl.getAbsoluteLeft() - 85; int top =
		 * logoutDownArrowLbl.getAbsoluteTop() + 13; showLogoutPopup(left, top);
		 */
		Window.enableScrolling(true);
		AppClientFactory.fireEvent(new SetHeaderZIndexEvent(99, true));
		Element e = null;
		if ((e = clickEvent.getRelativeElement()) != null) {
			if (e.getInnerHTML() != null
					&& e.getInnerHTML().contains("gwt-Label"))
				MixpanelUtil.Click_Discover_LandingPage();
		}
		manageDotsMenuSelection(loggedInfoLbl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", userDo.getGooruUId());
		params.put("user", userDo.getUsername());
		MixpanelUtil.Click_On_UserName();
		AppClientFactory.getPlaceManager().revealPlace(
				PlaceTokens.PROFILE_PAGE, params);

	}

	/**
	 * Search resource by entered keyword
	 * 
	 * @param clickEvent
	 *            instance of {@link ClickEvent}
	 */
	@UiHandler("editSearchBtn")
	public void OnSearchClick(ClickEvent clickEvent) {
//		Window.enableScrolling(true);
		AppClientFactory.fireEvent(new SetHeaderZIndexEvent(0, true));
		if (getEditSearchTxtBox().getText() != null
				&& getEditSearchTxtBox().getText().length() > 0) {
			savePlaceRequest();
			MixpanelUtil.Perform_Search(getEditSearchTxtBox().getText().trim()
					.toLowerCase(), "HeaderUc");
			Map<String, String> params = new HashMap<String, String>();
			params = updateParams(params);
			if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.COLLECTION_SEARCH)){
				AppClientFactory.getPlaceManager().revealPlace(
						PlaceTokens.COLLECTION_SEARCH, params);
			}else{
				String queryVal = params.get("query");
				//queryVal = queryVal.replaceAll("%5C1", "&");
				Map<String, String> map = params;
				map.put("query", queryVal);	
				editSearchTxtBox.setText(queryVal);
				if(prefilter!=null){
					prefilter.hide();
				}
				
				AppClientFactory.getPlaceManager().revealPlace(
						PlaceTokens.RESOURCE_SEARCH, params);
			}
			AppClientFactory.fireEvent(new HomeEvent(HeaderTabType.NONE));
			getEditSearchTxtBox().hideSuggestionList();
		}
		else
		{
			//else is for * query search.
			
			if(!prefilter.getFilter().isEmpty()&&getEditSearchTxtBox().getText().isEmpty())
			{
				Map<String, String> params = new HashMap<String, String>();
				params = updateParams(params);
				Map<String, String> map = params;
				String queryVal = params.get("query");
				map.put("query", "*");
				AppClientFactory.getPlaceManager().revealPlace(
						PlaceTokens.RESOURCE_SEARCH, map);
			}
		}
		
		if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.SHELF)){
			MixpanelUtil.mixpanelEvent("Perform_Search_FromOrganize");
		}else if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.STUDY)){
			MixpanelUtil.mixpanelEvent("Perform_Search_FromStudy");
		}else if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.SETTINGS)){
			MixpanelUtil.mixpanelEvent("Perform_Search_FromSettings");
		}else if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.PROFILE_PAGE)){
			MixpanelUtil.mixpanelEvent("Perform_Search_FromProfile");
		}else if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.EDIT_CLASSPAGE)){
			MixpanelUtil.mixpanelEvent("Perform_Search_FromTeach");
		}else if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.HOME)){
			MixpanelUtil.mixpanelEvent("Perform_Search_FromLandingPage");
			if(AppClientFactory.isAnonymous()){
				MixpanelUtil.mixpanelEvent("Perform_Search_FromLandingPage_Loggedout");
			}else{	
				MixpanelUtil.mixpanelEvent("Perform_Search_FromLandingPage_Loggedin");
			}
			if(AppClientFactory.getPlaceManager().getRequestParameter("courseId")!=null){
				MixpanelUtil.mixpanelEvent("Perform_Search_FromCoursePage");
			}
			if(AppClientFactory.getPlaceManager().getRequestParameter("page")!=null && AppClientFactory.getPlaceManager().getRequestParameter("page").equalsIgnoreCase("featured-contributors")){
				MixpanelUtil.mixpanelEvent("Perform_Search_FromContributorsPage");
			}
		}
		if(hasAutoSelected){
			MixpanelUtil.mixpanelEvent("Select_Autocomplete_Search");
		}
		
	}
	
	public void savePlaceRequest(){
		String currentPlaceToken=AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken();
		if(currentPlaceToken.equals(PlaceTokens.COLLECTION_SEARCH)||currentPlaceToken.equals(PlaceTokens.RESOURCE_SEARCH)){
		}else{
			AppClientFactory.getPlaceManager().setSearchMovedPlaceRequest(AppClientFactory.getPlaceManager().getCurrentPlaceRequest());
		}
	}

	/**
	 * Set pagination for search
	 * 
	 * @param params
	 *            variable for Map<String,String>
	 * @return pagination values
	 */
	public Map<String, String> updateParams(Map<String, String> params) {
		params.clear();
		if(prefilter!=null){
			params=prefilter.getFilter();
			String subject = params.get(IsSearchView.SUBJECT_FLT);
			if(AppClientFactory.getPlaceManager().getRequestParameter(IsSearchView.SUBJECT_FLT)!=null)
			{
				subject = AppClientFactory.getPlaceManager().getRequestParameter(IsSearchView.SUBJECT_FLT);
			}
			if (subject != null) {
				params.put(IsSearchView.SUBJECT_FLT, subject);
			}else{
				params.remove(IsSearchView.SUBJECT_FLT);
			}
			String grade = params.get(IsSearchView.GRADE_FLT);
			
			if(AppClientFactory.getPlaceManager().getRequestParameter(IsSearchView.GRADE_FLT)!=null)
			{
				grade = AppClientFactory.getPlaceManager().getRequestParameter(IsSearchView.GRADE_FLT);
			}
			
			if (grade != null) {
				params.put(IsSearchView.GRADE_FLT, grade);
			}else{
				params.remove(IsSearchView.GRADE_FLT);
			}
			String standardsUrlParam = null;
			if(AppClientFactory.getPlaceManager().getRequestParameter(IsSearchView.STANDARD_FLT)!=null)
			{
				standardsUrlParam = AppClientFactory.getPlaceManager().getRequestParameter(IsSearchView.STANDARD_FLT);
				params.put(IsSearchView.STANDARD_FLT, standardsUrlParam);
			}
			else
			{
				if(stadardCode!=null && !stadardCode.equals("")){
				params.put(IsSearchView.STANDARD_FLT, stadardCode);
				stadardCode=null;
				}
			}

			

		}
		params.put("category", "All");
		params.put("query", getEditSearchText());
		String currentPlaceToken=AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken();
		if(currentPlaceToken.equals(PlaceTokens.RESOURCE_SEARCH))
		{
		params.put(IsSearchView.RATINGS_FLT, "5,4,3,2,1,0");
		}
		params.put("pageNum", "1");
		params.put("pageSize", "8");
		
		return params;
	}

	/**
	 * @param text
	 *            Search keyword
	 */
	public void setEditSearchText(String text) {
		getEditSearchTxtBox().setText(text);
	}

	/**
	 * Set loggedIn user details
	 * 
	 * @param userDo
	 *            instance of {@link UserDo}
	 */
	public void setLoggedInUser(final UserDo userDo) {
		this.userDo = userDo;
		activateAllMenu();
		if (userDo != null
				&& !userDo.getUserUid()
						.equals(AppClientFactory.GOORU_ANONYMOUS)) {
			mainDotsPanel.getElement().getStyle().setWidth(580, Unit.PX);
			mainInnerDotsPanel.getElement().getStyle().setWidth(550, Unit.PX);
			
			AppClientFactory.getInjector().getHomeService().getClientIpAddress(new SimpleAsyncCallback<String>() {

				@Override
				public void onSuccess(String clientIpAddress) {
					//To set the Identity in Mixpanel.
					MixpanelUtil.setIdentity(userDo.getUsername(), userDo.getEmailId(), userDo.getUserRoleSetString(), userDo.getCreatedOn(), userDo.getFirstName() + " "+ userDo.getLastName(), clientIpAddress);
				}
			});
			
			 int flag = userDo.getViewFlag() != null ? userDo.getViewFlag() : 0;
//			 if((flag==7||flag==2||flag==1)){
//					showMarketingPopup(userDo);
//			}
			 
			loggedInfoLbl.setText(userDo.getUsername());
			loggedInfoLbl.setTitle(userDo.getUsername());
			loggedInfoLbl.getElement().setAttribute("alt", userDo.getUsername());
			
			imgUserProfile.setUrl(userDo.getProfileImageUrl() != "" && userDo.getProfileImageUrl() !=null ? userDo.getProfileImageUrl() : null);
			
			imgUserProfile.getElement().setAttribute("onerror","imgError(this);");
			imgUserProfile.setAltText(userDo.getUsername());
			imgUserProfile.getElement().setId("imgUserProfile");
			imgUserProfile.getElement().setAttribute("alt",userDo.getUsername());
			imgUserProfile.getElement().setAttribute("title",userDo.getUsername());
			imgUserProfile.setVisible(true);

			LoginLinkContainer.setVisible(true);
			loggedInfoLbl.setVisible(true);
/*			if (userDo.getUsername().length() >= 15){
				LoginLinkContainer.getElement().getStyle().setWidth(155, Unit.PX);
			}else if (userDo.getUsername().length() <= 5){
				LoginLinkContainer.getElement().getStyle().setWidth(100, Unit.PX);
			}*/
			logoutDownArrowLbl.setVisible(true);
			logInfoFloPanel.setVisible(false);
			if (AppClientFactory.getLoggedInUser().getConfirmStatus() == 0) {
				acctActivationPl.setVisible(true);
			}
			mainDotsPanel.setVisible(true);
			

			// gooruGuideImgLbl.addStyleName(GooruCBundle.INSTANCE.css().loggedInGooruGuideImg());
			this.switchToClassicView();

			if (userDo.isBeforeProductionSwitch()) {
				// goToClasicGooruPanel.setVisible(true);
				logoutPanelVc.displayClassicGooruLink(true);
				// goToClasicGooruPanel.getElement().getStyle().setVisibility(Visibility.VISIBLE);SearchKeyDownHandler
			} else {
				// goToClasicGooruPanel.setVisible(false);
				logoutPanelVc.displayClassicGooruLink(false);
				// goToClasicGooruPanel.getElement().getStyle().setVisibility(Visibility.HIDDEN);
			}

		} else {
			mainDotsPanel.getElement().getStyle().clearWidth();
			mainInnerDotsPanel.getElement().getStyle().clearWidth();
			
			LoginLinkContainer.setVisible(false);
			loggedInfoLbl.setVisible(false);
			loggedInfoLbl.getElement().getStyle().clearWidth();
			logoutDownArrowLbl.setVisible(false);
			acctActivationPl.setVisible(false);
			logInfoFloPanel.setVisible(true);
//			mainDotsPanel.setVisible(false);
			// goToClasicGooruPanel.setVisible(false);
			// goToClasicGooruPanel.getElement().getStyle().setVisibility(Visibility.HIDDEN);
//			activateOnlyDiscover();
			// gooruGuideImgLbl.addStyleName(GooruCBundle.INSTANCE.css().loggedInGooruGuideImg());
			// gooruClassicViewLbl.setStyleName(GooruCBundle.INSTANCE.css().gooruClassicView());

		}
		if (AppClientFactory.getLoggedInUser() != null) {
			gooruLearning.setHref(AppClientFactory.getLoggedInUser()
					.getSettings() !=null ? AppClientFactory.getLoggedInUser()
					.getSettings().getHomeEndPoint() : "");
		}
		try {
			String googleAnaltics = userDo.getSettings()
					.getGoogleAnalticsExtraAccounts();
			if (GWT.isScript() && googleAnaltics != null
					&& !googleAnaltics.startsWith("$") && !addedAccounts) {
				String[] accounts = googleAnaltics.split(",");
				for (String account : accounts) {
					String[] accountParams = account.split(":");
					if (accountParams.length == 2) {
						AppClientFactory.getInjector().getGoogleAnalytics()
								.addAccount(accountParams[0], accountParams[1]);
					}
				}
				addedAccounts = true;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void showMarketingPopup(UserDo userDo){
		new ImprovedGooruPopUpView();
	}
	
	/**
	 * @return search keyword
	 */
	public String getEditSearchText() {
		String searchText = getEditSearchTxtBox().getText();
		if (searchText != null && searchText.length() > 0) {
			return searchText;
		} else {
			return null;
		}
	}
	
	/*@SuppressWarnings("deprecation")
	private class SearchClickHandler implements FocusListener{

		@Override
		@Deprecated
	    public void onFocus(Widget sender) {
			AppClientFactory.fireEvent(new PreFilterEvent());
		}

		@Override
		@Deprecated
		public void onLostFocus(Widget sender) {
		}

	}*/

	/**
	 * @author Search Team
	 * 
	 */
	private class SearchKeyDownHandler implements KeyDownHandler{

		@Override
		public void onKeyDown(KeyDownEvent event) {
			arrowLbl.setVisible(true);
			if(prefilter!=null){
				prefilter.hide();
			}
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				if (getEditSearchTxtBox().getText() != null
						&& getEditSearchTxtBox().getText().length() > 0) {
					if (AppClientFactory.getCurrentPlaceToken()
							.equalsIgnoreCase(PlaceTokens.TEACH)) {
						MixpanelUtil.Perform_Search_FromTeach();

					} else if (AppClientFactory.getCurrentPlaceToken()
							.equalsIgnoreCase(PlaceTokens.SHELF)) {
						MixpanelUtil.Perform_Search_FromOrganize();
					}
					if(hasAutoSelected){
						MixpanelUtil.mixpanelEvent("Select_Autocomplete_Search");	
					}
					
					MixpanelUtil.Perform_Search(getEditSearchTxtBox().getText()
							.trim().toLowerCase(), "HeaderUc");
					savePlaceRequest();
					Map<String, String> params = new HashMap<String, String>();
					params = updateParams(params);
					if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.COLLECTION_SEARCH)){
						AppClientFactory.getPlaceManager().revealPlace(
								PlaceTokens.COLLECTION_SEARCH, params);
					}else{
						String queryVal = params.get("query");
						//queryVal = queryVal.replaceAll("%5C1", "&");
						Map<String, String> map = params;
						map.put("query", queryVal);
						AppClientFactory.getPlaceManager().revealPlace(
								PlaceTokens.RESOURCE_SEARCH, params);
					}
					
					/*
					 * if(!(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase
					 * (PlaceTokens.COLLECTION)) ||
					 * (!(AppClientFactory.getCurrentPlaceToken
					 * ().equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)))){
					 * editSearchTxtBox.setText(""); }
					 */
					if ((AppClientFactory.getCurrentPlaceToken()
							.equalsIgnoreCase(PlaceTokens.SHELF))
							|| (AppClientFactory.getCurrentPlaceToken()
									.equalsIgnoreCase(PlaceTokens.STUDY) || (AppClientFactory
									.getCurrentPlaceToken()
									.equalsIgnoreCase(PlaceTokens.TEACH)))
							|| (AppClientFactory.getCurrentPlaceToken()
									.equalsIgnoreCase(PlaceTokens.SETTINGS))
							|| (AppClientFactory.getCurrentPlaceToken()
									.equalsIgnoreCase(PlaceTokens.PROFILE_PAGE))) {
						getEditSearchTxtBox().setText("");
					}

					AppClientFactory.fireEvent(new HomeEvent(
							HeaderTabType.DISCOVER));
					getEditSearchTxtBox().hideSuggestionList();
				}
			}
	
		}
	}
	
	/*private class SearchKeyUpHandler implements KeyUpHandler {

		@Override
		public void onKeyUp(KeyUpEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				if (getEditSearchTxtBox().getText() != null
						&& getEditSearchTxtBox().getText().length() > 0) {
					if (AppClientFactory.getCurrentPlaceToken()
							.equalsIgnoreCase(PlaceTokens.TEACH)) {
						MixpanelUtil.Perform_Search_FromTeach();

					} else if (AppClientFactory.getCurrentPlaceToken()
							.equalsIgnoreCase(PlaceTokens.SHELF)) {
						MixpanelUtil.Perform_Search_FromOrganize();
					}
					if(hasAutoSelected){
						MixpanelUtil.mixpanelEvent("Select_Autocomplete_Search");	
					}
					
					MixpanelUtil.Perform_Search(getEditSearchTxtBox().getText()
							.trim().toLowerCase(), "HeaderUc");
					Map<String, String> params = new HashMap<String, String>();
					params = updateParams(params);
					if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.COLLECTION_SEARCH)){
						AppClientFactory.getPlaceManager().revealPlace(
								PlaceTokens.COLLECTION_SEARCH, params);
					}else{
						String queryVal = params.get("query");
						queryVal = queryVal.replaceAll("%5C1", "&");
						Map<String, String> map = params;
						map.put("query", queryVal);
						AppClientFactory.getPlaceManager().revealPlace(
								PlaceTokens.RESOURCE_SEARCH, params);
					}
					
					
					 * if(!(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase
					 * (PlaceTokens.COLLECTION)) ||
					 * (!(AppClientFactory.getCurrentPlaceToken
					 * ().equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)))){
					 * editSearchTxtBox.setText(""); }
					 
					if ((AppClientFactory.getCurrentPlaceToken()
							.equalsIgnoreCase(PlaceTokens.SHELF))
							|| (AppClientFactory.getCurrentPlaceToken()
									.equalsIgnoreCase(PlaceTokens.STUDY) || (AppClientFactory
									.getCurrentPlaceToken()
									.equalsIgnoreCase(PlaceTokens.TEACH)))
							|| (AppClientFactory.getCurrentPlaceToken()
									.equalsIgnoreCase(PlaceTokens.SETTINGS))
							|| (AppClientFactory.getCurrentPlaceToken()
									.equalsIgnoreCase(PlaceTokens.PROFILE_PAGE))) {
						getEditSearchTxtBox().setText("");
					}

					AppClientFactory.fireEvent(new HomeEvent(
							HeaderTabType.DISCOVER));
					getEditSearchTxtBox().hideSuggestionList();
				}
			}
		}
	}
*/

	/**
	 * @author Search Team
	 * 
	 */
	private class studyClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			isStudyNow = true;			
			if (isClassCodePopupOpen) {
				if(studyNowToolTip!=null && studyNowToolTip.isShowing()){
					studyNowToolTip.hide();
					studyNowToolTip.getClassCodeTxtBox().setText("");
				}else{
					if(AppClientFactory.isAnonymous()){
						isEnter=true;
						if(studyNowToolTip==null){
							studyNowToolTip = new StudyNowToolTip();
						}else if(logInfoFloPanel.isVisible()){ 
							if(studyNowToolTip!=null){
								studyNowToolTip.getLblTitle().setVisible(true);
								studyNowToolTip.getClassStudyList().clear();
								
							}
						}
					}else{
						if(studyNowToolTip==null || isEnter ){
							isEnter=false;
							studyNowToolTip = new StudyNowToolTip();
						}
					}
					studyNowToolTip.refreshClasslist();
					studyNowToolTip.getElement().getStyle()
							.setBackgroundColor("transparent");
					studyNowToolTip.getElement().getStyle()
							.setPosition(Position.ABSOLUTE);
					studyNowToolTip.setPopupPosition(event.getRelativeElement()
							.getAbsoluteLeft() - (AppClientFactory.isAnonymous() ? 115 : 134), event
							.getRelativeElement().getAbsoluteTop() + (AppClientFactory.isAnonymous() ? 23 : 33));
					studyNowToolTip.show();
					getEditSearchTxtBox().setText("");
					ClickHandler handler = new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							studyNowToolTip.show();
							isStudyNow = true;
						}
					};
					studyNowToolTip.addDomHandler(handler, ClickEvent.getType());
//					isClassCodePopupOpen = false;
				}
				
			} else {
				studyNowToolTip.hide();
				studyNowToolTip.getClassCodeTxtBox().setText("");
				isClassCodePopupOpen = true;
			}
			
			 Window.addWindowScrollHandler(new Window.ScrollHandler() {
		       public void onWindowScroll(Window.ScrollEvent scrollEvent) {
		    	   studyNowToolTip.getElement().getStyle()
					.setPosition(Position.FIXED);	
//			    	   studyNowToolTip.setPopupPosition(event.getScrollLeft(),event.getScrollTop());
 		    	   studyNowToolTip.setPopupPosition(848,33);
		       }
		    });
		}
	}

	/**
	 * View or hide search bar in header
	 * 
	 * @param enabled
	 *            to view or hide
	 */
	public void setSearchBarEnabled(boolean enabled) {
		editSearchInputFloPanel.setVisible(enabled);
	}

	/**
	 * Set classic link location
	 * 
	 * @param enabled
	 *            to left view or right view
	 */
	public void setClassicButtonEnabled(boolean enabled) {
		/*
		 * if (enabled) {
		 * gooruClassicViewLbl.getElement().getStyle().setFloat(Float.RIGHT); }
		 * else {
		 */
		// gooruClassicViewLbl.getElement().getStyle().setFloat(Float.LEFT);
		/* } */
	}

	/**
	 * Set logout popup location
	 * 
	 * @param left
	 *            value to set popup location from left
	 * @param top
	 *            value to set popup location from top
	 */
	public void showLogoutPopup(int left, int top) {
		logoutPanelVc.setWidth("80px");
		logoutPanelVc.setStyleName(GooruCBundle.INSTANCE.css().logoutPopup());
		logoutPanelVc.setPopupPosition(left, top);
		logoutPanelVc.show();
		isOpenSettingDropDown = false;
	}

	/**
	 * To view the gooru guide panel
	 * 
	 * @param clickEvent
	 *            instance of {@link ClickEvent}
	 */
	/*
	 * @UiHandler("gooruGuideImgLbl") public void gooruGuideLabel(ClickEvent
	 * clickEvent) { MixpanelUtil.Click_On_Help(); // openGooruGuide(); }
	 */
	/**
	 * To view the gooru guide panel
	 * 
	 * @param clickEvent
	 *            instance of {@link ClickEvent}
	 */
	/*
	 * @UiHandler("gooruGuidePanel") public void gooruGuidePanel(ClickEvent
	 * clickEvent) { tooltipTimer.cancel(); toolTipPopupPanel.hide();
	 * Window.enableScrolling(false); openGooruGuide(); }
	 */

	public void openGooruGuide() {
		// int left = logoutDownArrowLbl.getAbsoluteLeft() -1160 ;
		int top = logoutDownArrowLbl.getAbsoluteTop() + 53;

	}

	/*
	 * public int getGooruGuideIconLeft() { return
	 * gooruGuideImgLbl.getAbsoluteLeft(); }
	 */

	/*
	 * public int getGooruGuideIconTop() { return
	 * gooruGuideImgLbl.getAbsoluteTop(); }
	 */

	/**
	 * Allow the user to go classic view of gooru
	 */
	public void switchToClassicView() {
		// gooruClassicViewLbl.addClickHandler(new ClickHandler() {
		//
		// @Override
		// public void onClick(ClickEvent event) {
		// Window.Location.replace(AppClientFactory.getLoggedInUser().getSettings().getClassicEndPoint());
		//
		// }
		// });
	}

	public void showTeachPanelAsPopup(final int left, int top) {

		/*classpageListVc.getElement().getStyle().setZIndex(98);
		classpageListVc.show();*/
		// classpageListVc.getAllClasspages();
/*		Window.addWindowScrollHandler(new Window.ScrollHandler() {
			public void onWindowScroll(Window.ScrollEvent scrollEvent) {
				if (classpageListVc!=null){
					classpageListVc.getElement().getStyle().setPosition(Position.FIXED);	
					classpageListVc.setPopupPosition(left,51);
				}
			}
		});*/
	}
	DeleteClasspageListHandler deleteHandler = new DeleteClasspageListHandler() {
		
		@Override
		public void deleteClasspage(String classpageId) {
			
			if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.EDIT_CLASSPAGE)){
	/*			if(classpageListVc!=null){
					classpageListVc.removeClasspageItem(classpageId);
				}else{
					classpageListVc = new ClasspageListVc(true,classpageId);
				}*/
			}else{
				if(studyNowToolTip!=null){
					studyNowToolTip.removeClasspageItem(classpageId);
				}else{
					studyNowToolTip = new StudyNowToolTip();
				}
			}
				
			
		}
	};

	/**
	 * ReSend confirmation mail to user.
	 * 
	 * @param params
	 *            contains gooruUid, accountType
	 */

	public class ResendEmailConfirmation implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			Map<String, String> params = new HashMap<String, String>();
			params.put(GOORU_UID, userDo.getGooruUId());

			if (userDo.getAccountTypeId() == 2
					|| userDo.getAccountTypeId() == 1) {
				params.put(ACCOUNT_TYPE, "Parent");
			} else {
				params.put(ACCOUNT_TYPE, "NonParent");
			}

			AppClientFactory
					.getInjector()
					.getUserService()
					.resendConfirmationMail(params,
							new SimpleAsyncCallback<Object>() {
								@Override
								public void onSuccess(Object result) {

								}
							});
		}

	}

	@Override
	public void onSelection(SelectionEvent<Suggestion> event) {
		String searchText = editSearchTxtBox.getText();
		searchText= searchText.replaceAll("-<n> Gooru Search</n>", "");
		editSearchTxtBox.setText(searchText.trim());
		Window.enableScrolling(true);
		AppClientFactory.fireEvent(new SetHeaderZIndexEvent(0, true));
		if (editSearchTxtBox.getText() != null && editSearchTxtBox.getText().length() > 0) {
			MixpanelUtil.Perform_Search(editSearchTxtBox.getText().trim().toLowerCase(),"HeaderUc");
			Map<String, String> params = new HashMap<String, String>();
			params = updateParams(params);
			savePlaceRequest();
			if (AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(
					PlaceTokens.COLLECTION_SEARCH)) {
				AppClientFactory.getPlaceManager().revealPlace(
						PlaceTokens.COLLECTION_SEARCH, params);
			} else {
				String queryVal = params.get("query");
				//queryVal = queryVal.replaceAll("%5C1", "&");
				Map<String, String> map = params;
				map.put("query", queryVal);
				editSearchTxtBox.setText(queryVal);
				AppClientFactory.getPlaceManager().revealPlace(
						PlaceTokens.RESOURCE_SEARCH, map);
			}
			editSearchTxtBox.setText("");
			AppClientFactory.fireEvent(new HomeEvent(HeaderTabType.DISCOVER));
			editSearchTxtBox.hideSuggestionList();
			getEditSearchTxtBox().setText(searchText.trim());
		}
		else
		{
			//else is for * query search.
			if(!prefilter.getFilter().isEmpty()&&getEditSearchTxtBox().getText().isEmpty())
			{
				getEditSearchTxtBox().setText("");
				Map<String, String> params = new HashMap<String, String>();
				params = updateParams(params);
				Map<String, String> map = params;
				map.put("query", "*");
				AppClientFactory.getPlaceManager().revealPlace(
						PlaceTokens.RESOURCE_SEARCH, map);
			}
		}
		
		hasAutoSelected=true;
		MixpanelUtil.mixpanelEvent("Select_Autocomplete_Search");


	}

	public void requestAutoSuggestKeyword(
			SearchDo<AutoSuggestKeywordSearchDo> searchDo) {
		getAutoSuggestionKeyWordAsyncCallback().execute(searchDo);
	}

	public void setAutoKeyWordSuggestions(
			SearchDo<AutoSuggestKeywordSearchDo> autoSuggestKeywordDo) {
		autokeySuggestOracle.clear();
		this.autoSuggestKeywordDo = autoSuggestKeywordDo;
		searchData = searchData + GOORU_SEARCH;
		autokeySuggestOracle.add(searchData);
		if (this.autoSuggestKeywordDo.getSearchResults() != null) {
			for (AutoSuggestKeywordSearchDo autoSuggestKeywordSearchDo : autoSuggestKeywordDo
					.getSearchResults()) {
				autokeySuggestOracle.add(autoSuggestKeywordSearchDo
						.getKeyword());
			}
		}
		getEditSearchTxtBox().showSuggestionList();

	}

	/**
	 * @return suggestion standards for the collection as map string
	 */
	public SearchAsyncCallback<SearchDo<AutoSuggestKeywordSearchDo>> getAutoSuggestionKeyWordAsyncCallback() {
		if (autoKeyWordSuggestionAsyncCallback == null) {
			autoKeyWordSuggestionAsyncCallback = new SearchAsyncCallback<SearchDo<AutoSuggestKeywordSearchDo>>() {

				@Override
				protected void run(SearchDo<AutoSuggestKeywordSearchDo> searchDo) {
					AppClientFactory.getInjector().getSearchService()
							.getSuggestedAutokeyword(searchDo, this);

				}

				@Override
				public void onCallSuccess(
						SearchDo<AutoSuggestKeywordSearchDo> result) {
					setAutoKeyWordSuggestions(result);

				}

			};
		}
		return autoKeyWordSuggestionAsyncCallback;
	}

	public AppSuggestBox getEditSearchTxtBox() {
		return editSearchTxtBox;
	}

	public void setEditSearchTxtBox(AppSuggestBox editSearchTxtBox) {
		this.editSearchTxtBox = editSearchTxtBox;
	}

	public void setDiscoverLinkFromLibrary(String discoverLink) {
		this.discoverLinkUrl = discoverLink;
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();
		getEditSearchTxtBox().setFocus(true);
	}
	
	/**
	 * 
	 * @function OpenClasspageEdit
	 * 
	 * @created_date : Aug 15, 2013
	 * 
	 * @description
	 * 
	 * 
	 * @parm(s) : @param gooruOId
	 * 
	 * @return : void
	 * 
	 * @throws : <Mentioned if any exceptions>
	 * 
	 * 
	 * 
	 * 
	 */
	private void OpenClasspageEdit(String gooruOId, String gooruUid) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("pageNum", "0");
		params.put("pageSize", "10");
		params.put("pos", "1");
		if(gooruUid.equals(AppClientFactory.getLoggedInUser().getGooruUId())) {
			params.put("classpageid", gooruOId);
			AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.EDIT_CLASSPAGE, params);
		} else {
			params.put("id", gooruOId);
			AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.STUDENT, params);
		}
	}
	
	public static void closeClassContainer()
	{
		if(myClassesPop != null)
		{
		myClassesPop.setVisible(false);
		}
	}

	public  Label getArrowLbl() {
		return arrowLbl;
	}

	public static void setPrefilterObj(PreFilterPopup prefilterObj) {
		prefilter=prefilterObj;
	    //stadardCode=stadardCodeId;
	}

	public static void setStandardsCode(String stadardCodeId, int id, String code){
		stadardCode=stadardCodeId;
		/*SearchFilterVc searchFilterVc= new SearchFilterVc(true);
		System.out.println("id::"+id);
		System.out.println("code::"+code	);
		searchFilterVc.setUpdatedBrowseStandarsCode(stadardCodeId,id,code);*/	
		}
}


