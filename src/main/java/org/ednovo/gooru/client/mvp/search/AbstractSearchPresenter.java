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
package org.ednovo.gooru.client.mvp.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SearchAsyncCallback;
import org.ednovo.gooru.client.SeoTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.event.RegisterTabDndEvent;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BasePlacePresenter;
import org.ednovo.gooru.client.mvp.authentication.SignUpPresenter;
import org.ednovo.gooru.client.mvp.home.AlmostDoneUc;
import org.ednovo.gooru.client.mvp.home.event.HeaderTabType;
import org.ednovo.gooru.client.mvp.home.event.HomeEvent;
import org.ednovo.gooru.client.mvp.home.library.events.StandardPreferenceSettingEvent;
import org.ednovo.gooru.client.mvp.resource.dnd.ResourceDropController;
import org.ednovo.gooru.client.mvp.search.event.AggregatorSuggestionEvent;
import org.ednovo.gooru.client.mvp.search.event.ConfirmStatusPopupEvent;
import org.ednovo.gooru.client.mvp.search.event.ConsumeShelfCollectionsEvent;
import org.ednovo.gooru.client.mvp.search.event.DisableSpellSearchEvent;
import org.ednovo.gooru.client.mvp.search.event.PostSearchEvent;
import org.ednovo.gooru.client.mvp.search.event.PreSearchEvent;
import org.ednovo.gooru.client.mvp.search.event.RefreshSearchEvent;
import org.ednovo.gooru.client.mvp.search.event.RegisterSearchDropEvent;
import org.ednovo.gooru.client.mvp.search.event.RegisterSearchDropEvent.DROP_AREA;
import org.ednovo.gooru.client.mvp.search.event.SearchEvent;
import org.ednovo.gooru.client.mvp.search.event.SearchPaginationEvent;
import org.ednovo.gooru.client.mvp.search.event.SetFooterEvent;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderZIndexEvent;
import org.ednovo.gooru.client.mvp.search.event.SourceSuggestionEvent;
import org.ednovo.gooru.client.mvp.search.event.StandardsSuggestionEvent;
import org.ednovo.gooru.client.mvp.search.event.StandardsSuggestionInfoEvent;
import org.ednovo.gooru.client.mvp.search.event.SwitchSearchEvent;
import org.ednovo.gooru.client.mvp.search.event.UnregisterSearchDropEvent;
import org.ednovo.gooru.client.mvp.search.standards.AddStandardsPresenter;
import org.ednovo.gooru.client.service.SearchServiceAsync;
import org.ednovo.gooru.shared.model.code.CodeDo;
import org.ednovo.gooru.shared.model.folder.FolderDo;
import org.ednovo.gooru.shared.model.search.ResourceSearchResultDo;
import org.ednovo.gooru.shared.model.search.SearchDo;
import org.ednovo.gooru.shared.model.search.SearchFilterDo;
import org.ednovo.gooru.shared.model.user.ProfileDo;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

/**
 * @author Search Team
 * 
 * @param <T>
 * @param <C>
 * @param <V>
 * @param <P>
 */
public abstract class AbstractSearchPresenter<T extends ResourceSearchResultDo, C extends ResourceSearchResultDo, V extends IsSearchView<T>, P extends Proxy<?>> extends BasePlacePresenter<IsSearchView<T>, P> implements SearchUiHandlers {

	@Inject
	private SearchServiceAsync searchService;

	private final String QUERY = "query";

	private final String PAGE_NUM = "pageNum";

	private final String PAGE_SIZE = "pageSize";

	private SearchDo<T> searchDo = new SearchDo<T>();

	private SearchAsyncCallback<SearchDo<T>> searchAsyncCallback;

	private SearchAsyncCallback<SearchDo<CodeDo>> standardSuggestionAsyncCallback;

	private SearchAsyncCallback<SearchDo<CodeDo>> standardSuggestionInfoAsyncCallback;

	private SearchAsyncCallback<SearchDo<String>> sourceSuggestionAsyncCallback;
	
	private SearchAsyncCallback<SearchDo<String>> aggregatorSuggestionAsyncCallback;

	protected static final String ALL = "*";

	SignUpPresenter signUpViewPresenter = null;
	
	AddStandardsPresenter addStandardsPresenter = null;
	
	private boolean setFilter = true;
	
	private static final String USER_META_ACTIVE_FLAG = "0";
	
	private boolean isCCSSAvailable =false;
	private boolean isNGSSAvailable =false;
	private boolean isTEKSAvailable =false;
	private boolean isCAAvailable =false;
	
	/**
	 * Class constructor
	 * 
	 * @param view
	 *            {@link View}
	 * @param proxy
	 *            {@link Proxy}
	 */
	public AbstractSearchPresenter(V view, P proxy, SignUpPresenter signUpViewPresenter, AddStandardsPresenter addStandardsPresenterObj) {
		super(view, proxy);
		this.signUpViewPresenter = signUpViewPresenter;
		this.addStandardsPresenter = addStandardsPresenterObj;
		addRegisteredHandler(SearchPaginationEvent.TYPE, this);
		addRegisteredHandler(RefreshSearchEvent.TYPE, this);
		addRegisteredHandler(SwitchSearchEvent.TYPE, this);
		addRegisteredHandler(DisableSpellSearchEvent.TYPE, this);
		addRegisteredHandler(SearchEvent.TYPE, this);
		addRegisteredHandler(StandardsSuggestionEvent.TYPE, this);
		addRegisteredHandler(StandardsSuggestionInfoEvent.TYPE, this);
		addRegisteredHandler(RegisterSearchDropEvent.TYPE, this);
		addRegisteredHandler(UnregisterSearchDropEvent.TYPE, this);
		addRegisteredHandler(ConsumeShelfCollectionsEvent.TYPE, this);
		if (getViewToken().equals(PlaceTokens.RESOURCE_SEARCH)) {
			addRegisteredHandler(SourceSuggestionEvent.TYPE, this);
			addRegisteredHandler(AggregatorSuggestionEvent.TYPE, this);
		}
	}



	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, SearchRootPresenter.TYPE_VIEW, this);
	}

	@Override
	public void onBind() {
		super.onBind();
		setSearchAsyncCallback(new SearchAsyncCallback<SearchDo<T>>() {

			@Override
			protected void run(SearchDo<T> searchDo) {				
				requestSearch(searchDo, this);
			}

			@Override
			public void onCallSuccess(SearchDo<T> result) {
				setSearchDo(result);
				getEventBus().fireEvent(new PostSearchEvent(result));
				getView().postSearch(result);
			}
		});
		setStandardSuggestionAsyncCallback(new SearchAsyncCallback<SearchDo<CodeDo>>() {

			@Override
			protected void run(SearchDo<CodeDo> searchDo) {
				getSearchService().getSuggestStandard(searchDo, this);
			}

			@Override
			public void onCallSuccess(SearchDo<CodeDo> result) {
				getView().setStandardsSuggestions(result);
			}
		});
		setStandardSuggestionInfoAsyncCallback(new SearchAsyncCallback<SearchDo<CodeDo>>() {

			@Override
			protected void run(SearchDo<CodeDo> searchDo) {
				getSearchService().getSuggestStandard(searchDo, this);
			}

			@Override
			public void onCallSuccess(SearchDo<CodeDo> result) {
				getView().setStandardsSuggestionsInfo(result);
			}
		});
		if (getViewToken().equals(PlaceTokens.RESOURCE_SEARCH)) {
			setSourceSuggestionAsyncCallback(new SearchAsyncCallback<SearchDo<String>>() {

				@Override
				protected void run(SearchDo<String> searchDo) {
					getSearchService().getSuggestSource(searchDo, this);
				
				}

				@Override
				public void onCallSuccess(SearchDo<String> result) {
					getView().setSourceSuggestions(result);
				}
			});
			setAggregatorSuggestionAsyncCallback(new SearchAsyncCallback<SearchDo<String>>() {

				@Override
				protected void run(SearchDo<String> searchDo) {
					getSearchService().getSuggestedAggregator(searchDo, this);
					
				}

				@Override
				public void onCallSuccess(SearchDo<String> result) {
					getView().setAggregatorSuggestions(result);
					
				}
				
			});
			
		}
	}

	@Override
	protected void onReveal() {	
		super.onReveal();
		AppClientFactory.fireEvent(new RegisterTabDndEvent());
		AppClientFactory.fireEvent(new ConfirmStatusPopupEvent(true));
		AppClientFactory.fireEvent(new HomeEvent(HeaderTabType.NONE));
	}

	@Override
	protected void onReset() {
		super.onReset();
		
		String count = Cookies.getCookie("MyCookie");
			if(count!= null && Integer.parseInt(count)==7){
				Window.enableScrolling(false);
				Cookies.setCookie("MyCookie","8");
			}
			else{
				Window.enableScrolling(false);
				//Window.enableScrolling(true);
			}
		
		AppClientFactory.fireEvent(new SetFooterEvent(AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken()));
		if (AppClientFactory.getPlaceManager().refreshPlace()) {
			if (setFilter) {
				searchDo.setPageNum(1);
				getSearchService().getSearchFilters(getCurrentPlaceToken(), new SimpleAsyncCallback<SearchFilterDo>() {

					@Override
					public void onSuccess(SearchFilterDo searchFilterDo) {
						getView().setSearchFilter(searchFilterDo);
					}
				});
				setFilter = false;
			} else {
				initiateSearch();
			}
		}
		if (getPlaceManager().getRequestParameter("callback") != null && getPlaceManager().getRequestParameter("callback").equalsIgnoreCase("signup")) {
			//To show SignUp (Registration popup)
			Window.enableScrolling(false);
			AppClientFactory.fireEvent(new SetHeaderZIndexEvent(98, false));
			String type = getPlaceManager().getRequestParameter("type") ;
			int displayScreen =getPlaceManager().getRequestParameter("type") !=null  ? Integer.parseInt(type) : 1;
			signUpViewPresenter.displayPopup(displayScreen);
			addToPopupSlot(signUpViewPresenter);
		}
		
		int flag = AppClientFactory.getLoggedInUser().getViewFlag();
		final String loginType = AppClientFactory.getLoggedInUser().getLoginType() !=null ? AppClientFactory.getLoggedInUser().getLoginType() : "";
		if(!AppClientFactory.isAnonymous() && flag==0 &&  loginType.equalsIgnoreCase("apps")) {
			AlmostDoneUc update = new AlmostDoneUc(AppClientFactory.getLoggedInUser().getEmailId(), AppClientFactory.getLoggedInUser());
			update.setGlassEnabled(true);
			update.show();
			update.center();
		}
		
		Document doc = Document.get();
		doc.getElementById("uvTab").getStyle().setDisplay(Display.BLOCK);
		AppClientFactory.fireEvent(new HomeEvent(HeaderTabType.NONE));
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		if (AppClientFactory.getPlaceManager().refreshPlace()) {
//			if (getPlaceManager().getRequestParameter("callback") != null
//					&& !getPlaceManager().getRequestParameter("callback")
//							.equalsIgnoreCase("signup")) {
				initParam();
//			}
		}
	}

	@Override
	public void initiateSearch() {
		AppClientFactory.fireEvent(new PreSearchEvent(getSearchDo()));
		getView().preSearch(getSearchDo());
		setPageTitle(getSearchDo().getSearchQuery());
		AppClientFactory.setMetaDataDescription(SeoTokens.HOME_META_DESCRIPTION);
		if (getSearchDo().getSearchQuery() != null && getSearchDo().getSearchQuery().trim().length() >=0) {
			getSearchAsyncCallback().execute(getSearchDo());
		}
	}

	public void setPageTitle(String searchQuery) {
		String pageToken = AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken();
		if(pageToken.equals(PlaceTokens.COLLECTION_SEARCH)) {
			AppClientFactory.setBrowserWindowTitle(SeoTokens.COLLECTION_SEARCH_TITLE+searchQuery);
		} else if (pageToken.equals(PlaceTokens.RESOURCE_SEARCH)) {
			AppClientFactory.setBrowserWindowTitle(SeoTokens.RESOURCE_SEARCH_TITLE+searchQuery);
		}
	}
	
	/**
	 * @return search filters such as category, subject, grade, etc..
	 */
	protected Map<String, String> getSearchFilters() {
		Map<String, String> filters = new HashMap<String, String>();
		String category = getPlaceManager().getRequestParameter(IsSearchView.CATEGORY_FLT);
		if (category != null) {
			filters.put(IsSearchView.CATEGORY_FLT, category);
		}
		String subject = getPlaceManager().getRequestParameter(IsSearchView.SUBJECT_FLT);
		if (subject != null) {
			filters.put(IsSearchView.SUBJECT_FLT, subject);
		}
		String grade = getPlaceManager().getRequestParameter(IsSearchView.GRADE_FLT);
		if (grade != null) {
			filters.put(IsSearchView.GRADE_FLT, grade);
		}
		String standard = getPlaceManager().getRequestParameter(IsSearchView.STANDARD_FLT);
		if (standard != null) {
			filters.put(IsSearchView.STANDARD_FLT, standard);
		}
		if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)){
			String notFriendly = getPlaceManager().getRequestParameter(IsSearchView.MEDIATYPE_FLT);
			if (notFriendly != null && notFriendly.equalsIgnoreCase("not_ipad_friendly")) {
				filters.put(IsSearchView.MEDIATYPE_FLT, notFriendly);
			}
			String oer=getPlaceManager().getRequestParameter(IsSearchView.OER_FLT);
			String accessMode=getPlaceManager().getRequestParameter(IsSearchView.ACCESS_MODE_FLT);
			if(oer != null && oer.equalsIgnoreCase("1")){
				filters.put(IsSearchView.OER_FLT, oer);
			}
			
			if(accessMode != null){
				filters.put(IsSearchView.ACCESS_MODE_FLT, accessMode);
			}
			
			
		}
		return filters;
	}

	public SearchAsyncCallback<SearchDo<T>> getSearchAsyncCallback() {
		return searchAsyncCallback;
	}

	public void setSearchAsyncCallback(SearchAsyncCallback<SearchDo<T>> searchAsyncCallback) {
		this.searchAsyncCallback = searchAsyncCallback;
	}

	/**
	 * @return type of {@link SearchDo}
	 */
	public SearchDo<T> getSearchDo() {
		return searchDo;
	}

	/**
	 * @param searchDo
	 *            , assign searchDo
	 */
	public void setSearchDo(SearchDo<T> searchDo) {
		this.searchDo = searchDo;
	}

	public SearchServiceAsync getSearchService() {
		return searchService;
	}

	/**
	 * Assign page number and page size for search
	 */
	private void initParam() {
		getSearchDo().setSearchResults(null);
		String queryVal = "";
		if(getPlaceManager().getRequestParameter(QUERY) != null)
		{
			queryVal = getPlaceManager().getRequestParameter(QUERY);	
			/*queryVal = queryVal.replaceAll("%5C1", "&");*/
		}
		try
		{
		queryVal = URL.decodeQueryString(queryVal);
		}
		catch(Exception ex)
		{
			
		}
		getSearchDo().setQuery(queryVal);
		getSearchDo().setPageNum(getPlaceManager().getRequestParameterAsInt(PAGE_NUM));
		getSearchDo().setPageSize(getPlaceManager().getRequestParameterAsInt(PAGE_SIZE));
		getSearchDo().setFilters(getSearchFilters());
	}

	@Override
	public void paginateSearch(int pageNum) {
		String queryVal = "";
		if(getPlaceManager().getRequestParameter(QUERY) != null)
		{
			queryVal = getPlaceManager().getRequestParameter(QUERY);	
			/*queryVal = queryVal.replaceAll("%5C1", "&");*/
		}
		try
		{
		queryVal = URL.decodeQueryString(queryVal);
		}
		catch(Exception ex)
		{
			
		}
		if (getViewToken().equalsIgnoreCase(getCurrentPlaceToken())) {
			getSearchDo().setPageNum(pageNum);
			getSearchDo().setQuery(queryVal);
			onSearchRequest(null);
		}
	}

	@Override
	public void refreshSearch(String query) {
		getSearchDo().setQuery(query);
		getSearchDo().setPageNum(null);
		getSearchDo().setPageSize(null);
		onSearchRequest(null);
	}

	@Override
	public void switchSearch(String viewToken,String searchQuery) {
		getSearchDo().setPageNum(null);
		getSearchDo().setPageSize(null);
		getSearchDo().setNotFriendly(null);
		getSearchDo().setQuery(searchQuery);
		Map<String, String> params = new HashMap<String, String>();
		params.put(IsSearchView.RATINGS_FLT, "5,4,3,2,1,0");
		getSearchDo().setFilters(params);
		onSearchRequest(viewToken);
	}
	@Override
	public void disableSpellCheck(String viewToken,String searchQuery,String disableSpellCheck) {
		getSearchDo().setPageNum(null);
		getSearchDo().setPageSize(null);
		getSearchDo().setNotFriendly(null);
		getSearchDo().setQuery(searchQuery);
		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put(IsSearchView.RATINGS_FLT, "5,4,3,2,1,0");
		if(getSearchDo().getFilters() != null)
		{
		filterMap = getSearchDo().getFilters();
		}
		filterMap.put("disableSpellCheck", "true");
		getSearchDo().setFilters(filterMap);
		onSearchDisableSpellCheck(viewToken);
	}

	@Override
	public void onSearch(String query) {
		getSearchDo().setPageNum(null);
		getSearchDo().setPageSize(null);
		getSearchDo().setQuery(query);
		onSearchRequest(null);
	}

	/**
	 * Set search view token ,assign search query, page number and page size
	 * 
	 * @param viewToken
	 *            is a page view url
	 */
	public void onSearchRequest(String viewToken) {
		if (viewToken == null) {
			viewToken = getCurrentPlaceToken();
		}
		if (getViewToken().equalsIgnoreCase(viewToken)) {
			Map<String, String> params = getView().getSearchFilters();
			if(!viewToken.equalsIgnoreCase(AppClientFactory.getCurrentPlaceToken())) {
				params.clear();
				params.put(IsSearchView.CATEGORY_FLT, "all");
				String subject = getPlaceManager().getRequestParameter(IsSearchView.SUBJECT_FLT);
				if (subject != null) {
					params.put(IsSearchView.SUBJECT_FLT, subject);
				}
				String grade = getPlaceManager().getRequestParameter(IsSearchView.GRADE_FLT);
				if (grade != null) {
					params.put(IsSearchView.GRADE_FLT, grade);
				}
				String standard = getPlaceManager().getRequestParameter(IsSearchView.STANDARD_FLT);
				if (standard != null) {
					params.put(IsSearchView.STANDARD_FLT, standard);
				}
				if (viewToken.equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)){
					String notFriendly = getPlaceManager().getRequestParameter(IsSearchView.MEDIATYPE_FLT);
					String oer = getPlaceManager().getRequestParameter(IsSearchView.OWNER_FLT);
					String accessMode = getPlaceManager().getRequestParameter(IsSearchView.ACCESS_MODE_FLT);
					String reviewTag = getPlaceManager().getRequestParameter(IsSearchView.REVIEWS_FLT);
					params.put(IsSearchView.RATINGS_FLT, "5,4,3,2,1,0");
					if (notFriendly != null) {
						params.put(IsSearchView.MEDIATYPE_FLT, notFriendly);
					}
					if (oer != null) {
						params.put(IsSearchView.OER_FLT, oer);
					}
					
					if (accessMode != null) {
						params.put(IsSearchView.ACCESS_MODE_FLT, accessMode);
					}
					if(reviewTag != null)
					{
						params.put(IsSearchView.REVIEWS_FLT, reviewTag);
					}
				}else{
					params.remove(IsSearchView.MEDIATYPE_FLT);
					params.remove(IsSearchView.OER_FLT);
					params.remove(IsSearchView.ACCESS_MODE_FLT);
				}
				
				
				getView().resetFilters();
			}
			params.put(QUERY, getSearchDo().getUrlQuery());
			params.put(PAGE_SIZE, getSearchDo().getPageSize() + "");
			params.put(PAGE_NUM, getSearchDo().getPageNum() + "");
			getPlaceManager().revealPlace(viewToken, params, true);
		}
	}
	/**
	 * Set search view token ,assign search query, page number and page size
	 * 
	 * @param viewToken
	 *            is a page view url
	 */
	public void onSearchDisableSpellCheck(String viewToken) {
		if (viewToken == null) {
			viewToken = getCurrentPlaceToken();
		}
		if (getViewToken().equalsIgnoreCase(viewToken)) {
			Map<String, String> params = getView().getSearchFilters();
			if(!viewToken.equalsIgnoreCase(AppClientFactory.getCurrentPlaceToken())) {
				params.clear();
				params.put(IsSearchView.CATEGORY_FLT, "all");
				String subject = getPlaceManager().getRequestParameter(IsSearchView.SUBJECT_FLT);
				if (subject != null) {
					params.put(IsSearchView.SUBJECT_FLT, subject);
				}
				String grade = getPlaceManager().getRequestParameter(IsSearchView.GRADE_FLT);
				if (grade != null) {
					params.put(IsSearchView.GRADE_FLT, grade);
				}
				String standard = getPlaceManager().getRequestParameter(IsSearchView.STANDARD_FLT);
				if (standard != null) {
					params.put(IsSearchView.STANDARD_FLT, standard);
				}
				if (viewToken.equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)){
					String notFriendly = getPlaceManager().getRequestParameter(IsSearchView.MEDIATYPE_FLT);
					String oer = getPlaceManager().getRequestParameter(IsSearchView.OWNER_FLT);
					String accessMode = getPlaceManager().getRequestParameter(IsSearchView.ACCESS_MODE_FLT);
					String ratingMode = getPlaceManager().getRequestParameter(IsSearchView.RATINGS_FLT);
					if(ratingMode != null)
					{
					params.put(IsSearchView.RATINGS_FLT, ratingMode);
					}
					
					if (notFriendly != null) {
						params.put(IsSearchView.MEDIATYPE_FLT, notFriendly);
					}
					if (oer != null) {
						params.put(IsSearchView.OER_FLT, oer);
					}
					
					if (accessMode != null) {
						params.put(IsSearchView.ACCESS_MODE_FLT, accessMode);
					}
				}else{
					params.remove(IsSearchView.MEDIATYPE_FLT);
					params.remove(IsSearchView.OER_FLT);
					params.remove(IsSearchView.ACCESS_MODE_FLT);
				}
				
				
				
				
				getView().resetFilters();
			}
			params.put("disableSpellCheck", "true");
			params.put(QUERY, getSearchDo().getUrlQuery());
			params.put(PAGE_SIZE, getSearchDo().getPageSize() + "");
			params.put(PAGE_NUM, getSearchDo().getPageNum() + "");
			getPlaceManager().revealPlace(viewToken, params, true);
		}
	}

	public SearchAsyncCallback<SearchDo<CodeDo>> getStandardSuggestionAsyncCallback() {
		return standardSuggestionAsyncCallback;
	}

	public void setStandardSuggestionAsyncCallback(SearchAsyncCallback<SearchDo<CodeDo>> standardSuggestionAsyncCallback) {
		this.standardSuggestionAsyncCallback = standardSuggestionAsyncCallback;
	}

	public SearchAsyncCallback<SearchDo<String>> getSourceSuggestionAsyncCallback() {
		return sourceSuggestionAsyncCallback;
	}

	public void setSourceSuggestionAsyncCallback(SearchAsyncCallback<SearchDo<String>> sourceSuggestionAsyncCallback) {
		this.sourceSuggestionAsyncCallback = sourceSuggestionAsyncCallback;
	}

	public SearchAsyncCallback<SearchDo<String>> getAggregatorSuggestionAsyncCallback() {
		return aggregatorSuggestionAsyncCallback;
	}

	public void setAggregatorSuggestionAsyncCallback(
			SearchAsyncCallback<SearchDo<String>> aggregatorSuggestionAsyncCallback) {
		this.aggregatorSuggestionAsyncCallback = aggregatorSuggestionAsyncCallback;
	}

	@Override
	public void requestStandardsSuggestion(SearchDo<CodeDo> searchDo) {
		if (isCurrentView()) {
			getStandardSuggestionAsyncCallback().execute(searchDo);
		}
	}

	@Override
	public void requestSourceSuggestion(SearchDo<String> searchDo) {
		if (isCurrentView()) {
			getSourceSuggestionAsyncCallback().execute(searchDo);
		}
	}
	@Override
	public void requestAggregatorSuggestion(SearchDo<String> searchDo) {
		if (isCurrentView()) {
			getAggregatorSuggestionAsyncCallback().execute(searchDo);
		}
	}
	protected abstract void requestSearch(SearchDo<T> searchDo, SearchAsyncCallback<SearchDo<T>> searchAsyncCallback);

	@Override
	public void registerDropController(ResourceDropController dropController, DROP_AREA type) {
		if (isCurrentView()) {
			getView().registerDropController(dropController, type);
		}
	}

	@Override
	public void unregisterDropController(ResourceDropController dropController, DROP_AREA dropType) {
		if (isCurrentView()) {
			getView().unregisterDropController(dropController, dropType);
		}
	}

	@Override
	public void consumeShelfCollections(List<FolderDo> shelfCollections) {
		if (isCurrentView()) {
			getView().setShelfCollections(shelfCollections);
		}
	}
	
	@Override
	public void getAddStandards() {
		
		if(!AppClientFactory.isAnonymous()){
		AppClientFactory.getInjector().getUserService().getUserProfileV2Details(AppClientFactory.getLoggedInUser().getGooruUId(),
				USER_META_ACTIVE_FLAG,
				new SimpleAsyncCallback<ProfileDo>() {
					@Override
					public void onSuccess(final ProfileDo profileObj) {
					AppClientFactory.fireEvent(new StandardPreferenceSettingEvent(profileObj.getUser().getMeta().getTaxonomyPreference().getCode()));
					checkStandarsList(profileObj.getUser().getMeta().getTaxonomyPreference().getCode());
					}
					public void checkStandarsList(List<String> standarsPreferencesList) {
						
					if(standarsPreferencesList!=null){
							if(standarsPreferencesList.contains("CCSS")){
								isCCSSAvailable = true;
							}else{
								isCCSSAvailable = false;
							}
							if(standarsPreferencesList.contains("NGSS")){
								isNGSSAvailable = true;
							}else{
								isNGSSAvailable = false;
							}
							if(standarsPreferencesList.contains("TEKS")){
								isTEKSAvailable = true;
							}else{
								isTEKSAvailable = false;
							}
							if(standarsPreferencesList.contains("CA")){
								isCAAvailable = true;
							}else{
								isCAAvailable = false;
							}
								if(isCCSSAvailable || isNGSSAvailable || isTEKSAvailable || isCAAvailable){
									addStandardsPresenter.enableStandardsData(isCCSSAvailable,isTEKSAvailable,isNGSSAvailable,isCAAvailable);
									addToPopupSlot(addStandardsPresenter);
									getView().OnStandardsClickEvent(addStandardsPresenter.getAddBtn());
								}
							
					}
						
					}

				});
		}else{
			isCCSSAvailable = true;
			isNGSSAvailable = true;
			isCAAvailable = true;
			if(isCCSSAvailable || isNGSSAvailable || isTEKSAvailable || isCAAvailable){
				addStandardsPresenter.enableStandardsData(isCCSSAvailable,isTEKSAvailable,isNGSSAvailable,isCAAvailable);
				addToPopupSlot(addStandardsPresenter);
				getView().OnStandardsClickEvent(addStandardsPresenter.getAddBtn());
			}
		}
	}
	
	@Override
	public void setUpdatedStandards() {
		getView().setUpdatedStandards(addStandardsPresenter.setStandardsVal());
	}
	
	@Override
	public void closeStandardsPopup() {
		addStandardsPresenter.hidePopup();
	}

	/**
	 * @return the standardSuggestionInfoAsyncCallback
	 */
	public SearchAsyncCallback<SearchDo<CodeDo>> getStandardSuggestionInfoAsyncCallback() {
		return standardSuggestionInfoAsyncCallback;
	}

	/**
	 * @param standardSuggestionInfoAsyncCallback
	 *            the standardSuggestionInfoAsyncCallback to set
	 */
	public void setStandardSuggestionInfoAsyncCallback(SearchAsyncCallback<SearchDo<CodeDo>> standardSuggestionInfoAsyncCallback) {
		this.standardSuggestionInfoAsyncCallback = standardSuggestionInfoAsyncCallback;
	}

	@Override
	public void requestStandardsSuggestionInfo(SearchDo<CodeDo> searchDo) {
		if (isCurrentView()) {
			getStandardSuggestionInfoAsyncCallback().execute(searchDo);
		}
	}
}
