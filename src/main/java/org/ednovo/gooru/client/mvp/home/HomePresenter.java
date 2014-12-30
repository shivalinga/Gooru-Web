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
/**
 * 
 */
package org.ednovo.gooru.client.mvp.home;

import java.util.HashMap;
import java.util.Map;

import org.ednovo.gooru.client.AppPlaceKeeper;
import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SearchAsyncCallback;
import org.ednovo.gooru.client.SeoTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.event.InvokeLoginEvent;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BasePlacePresenter;
import org.ednovo.gooru.client.mvp.authentication.SignUpPresenter;
import org.ednovo.gooru.client.mvp.authentication.afterthirteen.SignUpAfterThirteenPresenter;
import org.ednovo.gooru.client.mvp.authentication.afterthirteen.SignUpCompleteProfilePresenter;
import org.ednovo.gooru.client.mvp.authentication.uc.StudentSignUpUc;
import org.ednovo.gooru.client.mvp.authentication.uc.ThanksEmailConfirmPopupUc;
import org.ednovo.gooru.client.mvp.community.contributors.ContributorsPresenter;
import org.ednovo.gooru.client.mvp.home.event.HeaderTabType;
import org.ednovo.gooru.client.mvp.home.event.HomeEvent;
import org.ednovo.gooru.client.mvp.home.event.SetTexasPlaceHolderEvent;
import org.ednovo.gooru.client.mvp.home.event.SetTexasPlaceHolderHandler;
import org.ednovo.gooru.client.mvp.home.presearchstandards.AddStandardsPreSearchPresenter;
import org.ednovo.gooru.client.mvp.home.register.UserRegistrationPresenter;
import org.ednovo.gooru.client.mvp.search.event.ConfirmStatusPopupEvent;
import org.ednovo.gooru.client.mvp.search.event.SetFooterEvent;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderZIndexEvent;
import org.ednovo.gooru.client.service.HomeServiceAsync;
import org.ednovo.gooru.client.service.SearchServiceAsync;
import org.ednovo.gooru.client.service.UserServiceAsync;
import org.ednovo.gooru.client.uc.AlertContentUc;
import org.ednovo.gooru.client.uc.AlertMessageUc;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.code.CodeDo;
import org.ednovo.gooru.shared.model.search.AutoSuggestKeywordSearchDo;
import org.ednovo.gooru.shared.model.search.CollectionSearchResultDo;
import org.ednovo.gooru.shared.model.search.ResourceSearchResultDo;
import org.ednovo.gooru.shared.model.search.SearchDo;
import org.ednovo.gooru.shared.model.user.ProfileDo;
import org.ednovo.gooru.shared.model.user.UserDo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

/**
 * @author Search Team
 * 
 */
public class HomePresenter extends BasePlacePresenter<IsHomeView, HomePresenter.IsHomeProxy> implements HomeUiHandlers {

	@Inject
	private SearchServiceAsync searchService;

	@Inject
	private HomeServiceAsync homeService;
	
	@Inject
	private UserServiceAsync userService;

	@UiField
	SearchHomeFilterVc searchHomeFilter;
	
	public static final  Object CONTRIBUTORS_SLOT = new Object();
	
	/** 
	 * This method is to get the contributorsSlot
	 */
	@Override
	public  Object getContributorsSlot() {
		return CONTRIBUTORS_SLOT;
	}

	SignUpPresenter signUpViewPresenter = null;
	
	ContributorsPresenter contributorsPresenter = null;
	
	SignUpCompleteProfilePresenter signUpCompletePresenter = null;
	
	SignUpAfterThirteenPresenter signUpAfterThirteenPresenter=null;
	private SearchDo<ResourceSearchResultDo> resourceSearchDo = new SearchDo<ResourceSearchResultDo>();

	private SearchDo<CollectionSearchResultDo> collectionSearchDo = new SearchDo<CollectionSearchResultDo>();
	
	private SimpleAsyncCallback<UserDo> registerdUserAsyncCallback;
	
	private SearchAsyncCallback<SearchDo<CodeDo>> standardSuggestionAsyncCallback;
	private SearchAsyncCallback<SearchDo<AutoSuggestKeywordSearchDo>> autoKeyWordSuggestionAsyncCallback;
	public boolean isResourceSearch() {
		return isResourceSearch;
	}

	private boolean isResourceSearch = true;

	private final String QUERY = "query";
	
	private final String FLT_STANDARD = "flt.standard";
	
	private final String FLT_GRADE = "flt.grade";
	
	private final String FLT_MEDIA_TYPE = "flt.mediaType";
	
	private UserRegistrationPresenter userRegistrationPresenter;

	private static final String GOORU_UID = "gooruuid";
	
	private static final String USER_TYPE = "type";
	
	private static final String CALLBACK = "callback";
	
	private static final String LIBRARY_PAGE = "page";
	
	private static final String LOGINEVENT = "loginEvent";
	
	private static final String ERROR = "error";
	
	AddStandardsPreSearchPresenter addStandardsPresenter = null;
	
	private static final String USER_META_ACTIVE_FLAG = "0";
	
	private String parentGooruUID;
	
	private boolean isLandingPageLoaded = false;
	
	@ProxyStandard
	@NameToken(PlaceTokens.HOME)
	@UseGatekeeper(AppPlaceKeeper.class)
	public interface IsHomeProxy extends ProxyPlace<HomePresenter> {
	}
	
//	PreFilterPopup preFilter = new PreFilterPopup();
	
	private MessageProperties i18n = GWT.create(MessageProperties.class);

	/**
	 * Class constructor
	 * @param userRegistrationPresenter instance of {@link UserRegistrationPresenter}
	 * @param view {@link View}
	 * @param proxy {@link Proxy}
	 */
	@Inject
	public HomePresenter(UserRegistrationPresenter userRegistrationPresenter, ContributorsPresenter contributorsPresenter, SignUpPresenter signUpViewPresenter, SignUpCompleteProfilePresenter signUpCompletePresenter,SignUpAfterThirteenPresenter signUpAfterThirteenPresenter, IsHomeView view, IsHomeProxy proxy,AddStandardsPreSearchPresenter addStandardsPresenterObj) {
		super(view, proxy);
		getView().setUiHandlers(this);
		this.addStandardsPresenter = addStandardsPresenterObj;
		this.signUpViewPresenter = signUpViewPresenter;
		this.userRegistrationPresenter = userRegistrationPresenter;
		this.signUpCompletePresenter = signUpCompletePresenter;
		this.signUpAfterThirteenPresenter=signUpAfterThirteenPresenter;
		this.contributorsPresenter = contributorsPresenter;
		
		
		addRegisteredHandler(SetTexasPlaceHolderEvent.TYPE, new SetTexasPlaceHolderHandler() {
			
			@Override
			public void setTexasPlaceHolderEvent(boolean isTexasAccount) {
				
			}
		});
	}
	
	@Override
	public void onBind() {
		super.onBind();
		MixpanelUtil.Arrive_Landing_Page();
		setRegisterdUserAsyncCallback(new SimpleAsyncCallback<UserDo>() {
			@Override
			public void onSuccess(UserDo user) {
				initilazeRegistrationView(user);
			}
		});	
		}
	
	@Override
	public void onReveal() {
		super.onReveal();
		if(AppClientFactory.isAnonymous()) {
			AppClientFactory.setBrowserWindowTitle(SeoTokens.HOME_TITLE_ANONYMOUS);
		} else {
			AppClientFactory.setBrowserWindowTitle(SeoTokens.HOME_TITLE_LOGGEDIN);
		}
		AppClientFactory.setMetaDataDescription(SeoTokens.HOME_META_DESCRIPTION);
		AppClientFactory.fireEvent(new HomeEvent(HeaderTabType.HOME));
		AppClientFactory.fireEvent(new SetFooterEvent(AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken()));
/*		isLandingPageLoaded = false;
		if(!isLandingPageLoaded) {
			getIntoLibrarypage();
		}
*/		Document doc = Document.get();
		doc.getElementById("uvTab").getStyle().setDisplay(Display.BLOCK);
	}
	
	
	private void callBackMethods(){
		if(AppClientFactory.getLoggedInUser().getConfirmStatus()==0){
			AppClientFactory.fireEvent(new ConfirmStatusPopupEvent(true));
		}
		if (getPlaceManager().getRequestParameter(CALLBACK) != null && getPlaceManager().getRequestParameter(CALLBACK).equalsIgnoreCase("registration")) {
			this.getUserService().getRegistredUserDetails(AppClientFactory.getPlaceManager().getRequestParameter(GOORU_UID), getRegisterdUserAsyncCallback());
			parentGooruUID=AppClientFactory.getPlaceManager().getRequestParameter(GOORU_UID);
		}else if (getPlaceManager().getRequestParameter(CALLBACK) != null && getPlaceManager().getRequestParameter(CALLBACK).equalsIgnoreCase("changePassword")) {
			validateResetLink(AppClientFactory.getPlaceManager().getRequestParameter("resetToken"));
		}else if (getPlaceManager().getRequestParameter(CALLBACK) != null && getPlaceManager().getRequestParameter(CALLBACK).equalsIgnoreCase("register")) {
			getView().registerPopup();
		}else if (getPlaceManager().getRequestParameter(CALLBACK) != null && getPlaceManager().getRequestParameter(CALLBACK).equalsIgnoreCase("signup")) {
			//To show SignUp (Registration popup)
			if (AppClientFactory.isAnonymous()){
				Window.enableScrolling(false);
				AppClientFactory.fireEvent(new SetHeaderZIndexEvent(98, false));
				String type = getPlaceManager().getRequestParameter("type") ;
				int displayScreen =getPlaceManager().getRequestParameter("type") !=null  ? Integer.parseInt(type) : 1;
				signUpViewPresenter.displayPopup(displayScreen);
				addToPopupSlot(signUpViewPresenter);
			}
		}else if (getPlaceManager().getRequestParameter(CALLBACK) != null && getPlaceManager().getRequestParameter(CALLBACK).equalsIgnoreCase("registerChild")){
			if (getPlaceManager().getRequestParameter("dob") != null && getPlaceManager().getRequestParameter("userName") != null){
				String externalId = AppClientFactory.getLoggedInUser().getExternalId();
				String email = AppClientFactory.getLoggedInUser().getEmailId();
				
				String parentEmailId = email !=null && !email.equalsIgnoreCase("") ? email : externalId !=null && externalId.equalsIgnoreCase("") ? externalId : null;				
				String parameterEmailId = getPlaceManager().getRequestParameter("emailId", null);
				parentEmailId = parameterEmailId !=null && !parameterEmailId.equalsIgnoreCase("") ? parameterEmailId : parentEmailId;
				
				StudentSignUpUc studentSignUp = new StudentSignUpUc(parentEmailId, getPlaceManager().getRequestParameter("userName"), getPlaceManager().getRequestParameter("dob").replaceAll("D", "\\/"), AppClientFactory.isAnonymous() ? getPlaceManager().getRequestParameter("privateGooruUId") : AppClientFactory.getLoggedInUser().getGooruUId());
				studentSignUp.center();
				studentSignUp.show();
			}
		}else if (getPlaceManager().getRequestParameter(CALLBACK) != null && getPlaceManager().getRequestParameter(CALLBACK).equalsIgnoreCase("guide")){

		}else if (getPlaceManager().getRequestParameter(CALLBACK) != null && getPlaceManager().getRequestParameter(CALLBACK).equalsIgnoreCase("profileUpdate")){
			if (AppClientFactory.isAnonymous()){
				AppClientFactory.fireEvent(new InvokeLoginEvent());
			}else{
				signUpCompletePresenter.displayView();
				addToPopupSlot(signUpCompletePresenter);
			}
		}
		else if (getPlaceManager().getRequestParameter(CALLBACK) != null && getPlaceManager().getRequestParameter(CALLBACK).equalsIgnoreCase("turn13")){
			if (AppClientFactory.isAnonymous()){
				AppClientFactory.fireEvent(new InvokeLoginEvent());
			}else{
				if(!signUpAfterThirteenPresenter.isVisible()) {
					signUpAfterThirteenPresenter.displayView();
					addToPopupSlot(signUpAfterThirteenPresenter);
				}
			}
		}
		else if  (getPlaceManager().getRequestParameter(CALLBACK) != null && getPlaceManager().getRequestParameter(CALLBACK).equalsIgnoreCase("confirmUser")){
			//SignUpGradeCourseView gradeCourseView = new SignUpGradeCourseView(AppClientFactory.getLoggedInUser());
			//Check if user is logged or not.
			if (AppClientFactory.isAnonymous()){
				//If not Open Login Popup
				AppClientFactory.fireEvent(new InvokeLoginEvent());
			}else{
				if (AppClientFactory.getLoggedInUser().getConfirmStatus() == 0){
					String gooruUid = getPlaceManager().getRequestParameter("gooruuid") !=null ? getPlaceManager().getRequestParameter("gooruuid") : "";
					String token = getPlaceManager().getRequestParameter("sessionid") !=null ? getPlaceManager().getRequestParameter("sessionid") : "";
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
								ThanksEmailConfirmPopupUc confirmPopup = new ThanksEmailConfirmPopupUc();
								confirmPopup.center();
								confirmPopup.show();
							}
						}
					});
				}
			}
		} 
		
		if (getPlaceManager().getRequestParameter(LOGINEVENT) != null && getPlaceManager().getRequestParameter(LOGINEVENT).equalsIgnoreCase("true") && AppClientFactory.isAnonymous()) {
			AppClientFactory.fireEvent(new InvokeLoginEvent());
		}
		
		if (getPlaceManager().getRequestParameter(ERROR) != null && getPlaceManager().getRequestParameter(ERROR).equals("401") && AppClientFactory.isAnonymous()) {
			new AlertContentUc(i18n.GL1966(), i18n.GL1938());
		}
		
		final UserDo userDo = AppClientFactory.getLoggedInUser(); 
		int flag = userDo.getViewFlag();
		final String loginType = AppClientFactory.getLoggedInUser().getLoginType() !=null ? AppClientFactory.getLoggedInUser().getLoginType() : "";
		if(!AppClientFactory.isAnonymous() && flag==0 &&  loginType.equalsIgnoreCase("apps")) {
			AlmostDoneUc update = new AlmostDoneUc(AppClientFactory.getLoggedInUser().getEmailId(), AppClientFactory.getLoggedInUser());
			update.setGlassEnabled(true);
			update.show();
			update.center();
		}
		else if(flag>0 && flag<=11 && !AppClientFactory.isAnonymous()){
			showMarketingPopup(userDo);
		}
		AppClientFactory.fireEvent(new SetFooterEvent(AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken()));	
	}
	
	private void validateResetLink(String resetToken) {
		// TODO Auto-generated method stub
		AppClientFactory.getInjector().getUserService().isValidResetPasswordLink(resetToken, new SimpleAsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				if(result.equals("true")){
					getView().resetPassword(AppClientFactory.getPlaceManager().getRequestParameter("resetToken"));
				}else{
					new AlertMessageUc(i18n.GL1089(), new HTML(i18n.GL0100()));
				}
			}
		});
	}

	@Override
	public void onReset() {
		super.onReset();
		if (AppClientFactory.isAnonymous()){
			getView().getBtnSignUp().setVisible(true);
		}else{
			getView().getBtnSignUp().setVisible(false);
		}
	}
	
	private void getIntoLibrarypage() {
		if (getPlaceManager().getRequestParameter(LIBRARY_PAGE) != null && getPlaceManager().getRequestParameter(LIBRARY_PAGE).equalsIgnoreCase("featured-contributors")) {
			getView().loadFeaturedContributors("featured-contributors",getViewToken());
		} else if (getPlaceManager().getRequestParameter(LIBRARY_PAGE) != null && getPlaceManager().getRequestParameter(LIBRARY_PAGE).equalsIgnoreCase("course-page")) {
			getView().loadFeaturedContributors("course-page",getViewToken());
		} else if (getPlaceManager().getRequestParameter(LIBRARY_PAGE) != null && getPlaceManager().getRequestParameter(LIBRARY_PAGE).equalsIgnoreCase("featured-course")) {
			getView().loadFeaturedContributors("featured-course",getViewToken());
		} else if (getPlaceManager().getRequestParameter(LIBRARY_PAGE) == null) {
			getView().loadFeaturedContributors("featured-course",getViewToken());
		}
	}
	
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		request.getParameter("", "register");
		callBackMethods();
		
		getIntoLibrarypage();
		if (AppClientFactory.isAnonymous()){
			getView().getBtnSignUp().setVisible(true);
		}else{
			getView().getBtnSignUp().setVisible(false);
		}
		setInSlot(CONTRIBUTORS_SLOT, contributorsPresenter);
	}

	@Override
	public void homeSearch(Map<String, String> hm) {
		hm.clear();
		resetSearchInfo(getCollectionSearchDo());
		resetSearchInfo(getResourceSearchDo());
		homeSearch(isResourceSearch(), hm);
	}

	/**
	 * Reset the search result
	 * @param searchDo instance of {@link SearchDo}
	 */
	public void resetSearchInfo(SearchDo<?> searchDo) {
		searchDo.setSearchResults(null);
		searchDo.setSearchHits(0);
	}

	/**
	 * Take to search page , resource search or collection search  
	 * @param isResourceSearch takes to resource search view if it is true otherwise to collection search view
	 * @param params search filters as map value
	 */
	public void homeSearch(boolean isResourceSearch, Map<String, String> params) {
		updateParams(isResourceSearch() ? getResourceSearchDo() : getCollectionSearchDo(), params);
		getPlaceManager().revealPlace(isResourceSearch ? PlaceTokens.RESOURCE_SEARCH : PlaceTokens.COLLECTION_SEARCH, params);
	}

	/**
	 * Set pagination for search in home page
	 * @param searchDo instance of {@link SearchDo}
	 * @param params set pagination values to Map object   
	 */
	public void updateParams(SearchDo<?> searchDo, Map<String, String> params) {
	
	}

	public HomeServiceAsync getFilterService() {
		return homeService;
	}

	public SearchServiceAsync getSearchService() {
		return searchService;
	}

	public void setResourceSearchDo(SearchDo<ResourceSearchResultDo> resourceSearchDo) {
		this.resourceSearchDo = resourceSearchDo;
	}

	public SearchDo<ResourceSearchResultDo> getResourceSearchDo() {
		return resourceSearchDo;
	}

	public SearchDo<CollectionSearchResultDo> getCollectionSearchDo() {
		return collectionSearchDo;
	}

	/**
	 * Assign collectionSearchDo instance value
	 * @param collectionSearchDo instance of the {@link CollectionSearchResultDo}
	 */
	public void setCollectionSearchDo(SearchDo<CollectionSearchResultDo> collectionSearchDo) {
		this.collectionSearchDo = collectionSearchDo;
	}

	@Override
	public void initilazeRegistrationView(UserDo user) {
		String userType = getPlaceManager().getRequestParameter(USER_TYPE);
		if (userType == null || (userType != null && !userType.equalsIgnoreCase("Parent") && !userType.equalsIgnoreCase("NonParent"))) {

			alert(i18n.GL1415()+i18n.GL_SPL_FULLSTOP()+i18n.GL_SPL_FULLSTOP()+i18n.GL_SPL_FULLSTOP(), i18n.GL1416());

		} else if (user != null) {
			if (user.isAvailability()) {
				if (user.getConfirmStatus() == 1 && userType.equalsIgnoreCase("Parent")) {
					if (AppClientFactory.getLoggedInUser().getUserUid().equals(AppClientFactory.GOORU_ANONYMOUS)) {
						LoginPopupUc login = new LoginPopupUc(user.getEmailId());
					} else if(AppClientFactory.getLoggedInUser().getUserUid().equalsIgnoreCase(parentGooruUID)||AppClientFactory.getLoggedInUser().getUserUid()==parentGooruUID){
						userRegistrationPresenter.setAccountType(userType);
						userRegistrationPresenter.setUser(user);
						addToPopupSlot(userRegistrationPresenter, true); 
					}
					else {
						alert(i18n.GL0065()+i18n.GL_SPL_FULLSTOP(), i18n.GL1417());
						
					}
				} else if (user.getConfirmStatus() == 1 && !userType.equalsIgnoreCase("Parent")) {
					alert(i18n.GL0065()+i18n.GL_SPL_FULLSTOP(), i18n.GL1418()); 
				} 
				else {
					userRegistrationPresenter.setAccountType(userType);
					userRegistrationPresenter.setUser(user);
					addToPopupSlot(userRegistrationPresenter, true);
				}
			} else {
				alert(i18n.GL0065()+i18n.GL_SPL_FULLSTOP(), i18n.GL1419());
			}

		} else {
			alert(i18n.GL1415()+i18n.GL_SPL_FULLSTOP()+i18n.GL_SPL_FULLSTOP()+i18n.GL_SPL_FULLSTOP(), i18n.GL1420()+i18n.GL_SPL_FULLSTOP());
		}
	}

	/**
	 * Create alert popup with message header and message content
	 * @param messageHeader popup heading 
	 * @param messageContent content of the popup
	 */
	private void alert(String messageHeader, String messageContent) {
		new AlertContentUc(messageHeader, messageContent).getAlertButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.HOME);
			}
		});
	}
	
	@Override
	public String getViewToken() {
		return PlaceTokens.HOME;
	}

	public void setRegisterdUserAsyncCallback(SimpleAsyncCallback<UserDo> registerdUserAsyncCallback) {
		this.registerdUserAsyncCallback = registerdUserAsyncCallback;
	}

	public SimpleAsyncCallback<UserDo> getRegisterdUserAsyncCallback() {
		return registerdUserAsyncCallback;
	}

	public void setUserService(UserServiceAsync userService) {
		this.userService = userService;
	}

	public UserServiceAsync getUserService() {
		return userService;
	}

	/**
	 * @return suggestion standards for the collection as map string
	 */
	public SearchAsyncCallback<SearchDo<CodeDo>> getStandardSuggestionAsyncCallback() {
		if (standardSuggestionAsyncCallback == null) {
			standardSuggestionAsyncCallback = new SearchAsyncCallback<SearchDo<CodeDo>>() {

				@Override
				protected void run(SearchDo<CodeDo> searchDo) {
					getSearchService().getSuggestStandard(searchDo, this);
				}

				@Override
				public void onCallSuccess(SearchDo<CodeDo> result) {
					
				}
			};
		}
		return standardSuggestionAsyncCallback;
	}

	@Override
	public void requestStandardsSuggestion(SearchDo<CodeDo> searchDo) {
		getStandardSuggestionAsyncCallback().execute(searchDo);
	}
	public void showMarketingPopup(UserDo userDo){
		new ImprovedGooruPopUpView();
//		 AppClientFactory.getInjector().getUserService().updateUserViewFlag(userDo.getGooruUId(), 7, new SimpleAsyncCallback<UserDo>() {
//				@Override
//				public void onSuccess(UserDo newUser) {
//					UserDo user = AppClientFactory.getLoggedInUser();
//					user.setViewFlag(newUser.getViewFlag());
//					AppClientFactory.setLoggedInUser(user);
//				}
//			});
	}

	/**
	 * @return suggestion standards for the collection as map string
	 */
	public SearchAsyncCallback<SearchDo<AutoSuggestKeywordSearchDo>> getAutoSuggestionKeyWordAsyncCallback() {
		if (autoKeyWordSuggestionAsyncCallback == null) {
			autoKeyWordSuggestionAsyncCallback = new SearchAsyncCallback<SearchDo<AutoSuggestKeywordSearchDo>>() {

				@Override
				protected void run(SearchDo<AutoSuggestKeywordSearchDo> searchDo) {
					getSearchService().getSuggestedAutokeyword(searchDo, this);
					
				}

				@Override
				public void onCallSuccess(
					SearchDo<AutoSuggestKeywordSearchDo> result) {
					
					
					
				}

				
			};
		}
		return autoKeyWordSuggestionAsyncCallback;
	}


	
	@Override
	public void requestAutoSuggestKeyword(
			SearchDo<AutoSuggestKeywordSearchDo> searchDo) {
			getAutoSuggestionKeyWordAsyncCallback().execute(searchDo);
	}

}
