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
package org.ednovo.gooru.client.mvp.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BaseViewWithHandlers;
import org.ednovo.gooru.client.mvp.dnd.AppMirageDragContainer;
import org.ednovo.gooru.client.mvp.dnd.IsDraggable;
import org.ednovo.gooru.client.mvp.resource.dnd.ResourceDragController;
import org.ednovo.gooru.client.mvp.resource.dnd.ResourceDropController;
import org.ednovo.gooru.client.mvp.search.SearchRootView.Style;
import org.ednovo.gooru.client.mvp.search.event.DisableSpellSearchEvent;
import org.ednovo.gooru.client.mvp.search.event.GetSearchKeyWordEvent;
import org.ednovo.gooru.client.mvp.search.event.RegisterSearchDropEvent;
import org.ednovo.gooru.client.mvp.search.event.RequestShelfCollectionEvent;
import org.ednovo.gooru.client.mvp.search.event.SearchFilterEvent;
import org.ednovo.gooru.client.mvp.search.event.SearchPaginationEvent;
import org.ednovo.gooru.client.mvp.search.event.SwitchSearchEvent;
import org.ednovo.gooru.client.uc.CloseLabelSetting;
import org.ednovo.gooru.client.uc.PaginationButtonUc;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.code.CodeDo;
import org.ednovo.gooru.shared.model.folder.FolderDo;
import org.ednovo.gooru.shared.model.search.ResourceSearchResultDo;
import org.ednovo.gooru.shared.model.search.SearchDo;
import org.ednovo.gooru.shared.model.search.SearchFilterDo;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Search Team
 * 
 * @param <T>
 *            type of ResourceSearchResultDo
 */
public abstract class AbstractSearchView<T extends ResourceSearchResultDo> extends BaseViewWithHandlers<SearchUiHandlers> implements IsSearchView<T>, ClickHandler {

	private static AbstractSearchViewUiBinder uiBinder = GWT.create(AbstractSearchViewUiBinder.class);
	
	private static MessageProperties i18n = GWT.create(MessageProperties.class);

	interface AbstractSearchViewUiBinder extends UiBinder<Widget, AbstractSearchView<?>> {
	}

	@UiField(provided = true)
	SearchFilterVc searchFilterVc;

	@UiField(provided = true)
	AppMirageDragContainer searchResultPanel;

	@UiField
	FlowPanel paginationFocPanel;

	@UiField
	ScrollPanel searchFilterPanel;
	
	@UiField
	Style style;
	
	@UiField
	HTML queriedTextHtml;
	
	@UiField
	FlowPanel standardsConatiner;
	
	@UiField HTMLPanel spellerrorqueriedTextHtml,correctSpellHTML;
	
	@UiField
	Button resourceSearchBtn, collectionSearchBtn;
	
	@UiField Label wrongQueryText;
	
	@UiField Label correctQueryText;
		
	String grades,stdCode,subjects,categories,oerTag,mobileFirendlyTag,ratingTag,publisher,aggregator,accessMode,author,reviewTag;

	
	protected ResourceDragController dragController;

	private static final String PREVIOUS = i18n.GL1462().toUpperCase();

	private static final String NEXT = i18n.GL1463().toUpperCase();

	protected final List<FolderDo> SHELF_COLLECTIONS = new ArrayList<FolderDo>();

	public boolean refreshShelfInfo;

	public boolean refreshedShelfCollections;
	String rootWebUrl = AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken();
	
	private HandlerRegistration handlerRegistration=null;
	
	public interface Style extends CssResource {

		String resourceBtnActive();
		String collectionBtnActive();
		String secondaryResourceSearchBtn();
		String secondaryCollectionSearchBtn();
	}
	
	/**
	 * Assign new instance for {@link ResourceDragController}, {@link AppMirageDragContainer}, {@link SearchFilterVc}
	 * 
	 * @param resourceSearch
	 *            whether resource search or not
	 */
	public AbstractSearchView(boolean resourceSearch) {
		dragController = new ResourceDragController(RootPanel.get());
		searchResultPanel = new AppMirageDragContainer(dragController);
		searchFilterVc = new SearchFilterVc(resourceSearch);
		setWidget(uiBinder.createAndBindUi(this));
		
		queriedTextHtml.getElement().setId("htmlQueriedTextHtml");
		
		resourceSearchBtn.getElement().setId("btnResource");
		collectionSearchBtn.getElement().setId("btnCollection");
		
		resourceSearchBtn.setText(i18n.GL0174());
		collectionSearchBtn.setText(i18n.GL0175());
		
		searchFilterPanel.getElement().setId("searchFilterPanelDiv");
		searchFilterVc.getElement().setId("searchFilterVcsearchFilterVc");
		paginationFocPanel.getElement().setId("fnlPaginationFocPanel");
		searchResultPanel.getElement().setId("appMirageDragContainer");
		
		correctSpellHTML.setVisible(false);
		spellerrorqueriedTextHtml.setVisible(false);
		
		
		standardsConatiner.clear();
		showCategoryFilter();
		showSubjectsFilter();
		showGradesFilter();
		showStandardsFilter();
		showPublisherFilter();
		showAggregatorFilter();
		showOERFilter();
		showMobileFriendlyFilter();
		showAccessModeFilter();
		showAuthorFilter();
		showRatingsFilter();
		showReviewFilter();
	
		if(!(stdCode!=null || grades!=null || subjects!=null || oerTag!=null || mobileFirendlyTag!=null || ratingTag!=null || publisher!=null || aggregator!=null || accessMode!=null || author!=null|| reviewTag!=null)){
			standardsConatiner.setVisible(false);
		}else{
			standardsConatiner.setVisible(true);
		}
		
		getBrowseBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
			getUiHandlers().getAddStandards();
				
			}
		});
		

	}

	@Override
	public void preSearch(SearchDo<T> searchDo) {
		reset();
		searchFilterVc.setFilter(searchDo.getFilters());
		if(!AppClientFactory.isAnonymous()) {
			searchFilterVc.getUserStandardPrefCodeId();
		}else{
			searchFilterVc.getStandardVisiblity();
		}
		
		if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equals(PlaceTokens.RESOURCE_SEARCH)) {
			resourceSearchBtn.addStyleName(style.resourceBtnActive());
			collectionSearchBtn.removeStyleName(style.collectionBtnActive());
			resourceSearchBtn.removeStyleName(style.secondaryResourceSearchBtn()); 
			collectionSearchBtn.addStyleName(style.secondaryCollectionSearchBtn());
		} else {
			collectionSearchBtn.addStyleName(style.collectionBtnActive());
			resourceSearchBtn.removeStyleName(style.resourceBtnActive());
			collectionSearchBtn.removeStyleName(style.secondaryCollectionSearchBtn());
			resourceSearchBtn.addStyleName(style.secondaryResourceSearchBtn()); 
		}
		}
		
	

	@Override
	public void postSearch(SearchDo<T> searchDo) {
		//searchResultPanel.setVisible(false);
		
		int countValue = searchDo.getSearchResults().size();
		String searchText = AppClientFactory.getPlaceManager()
				.getRequestParameter("query");
		
		if (searchText == null) {
			searchText = "";

		} else {
			if (searchText.contains("252")) {
				searchText = searchText.replaceAll("%", " ")
						.replaceAll("2", "").replaceAll("5", "")
						.replaceAll("B", "");
			}
			searchText = searchText.trim();
			if (searchText.length() > 50) {
				if (countValue > 0) {
					searchText = searchText.substring(0, 50) + "...";
					searchText = " " + i18n.GL1468() + " <b>" + searchText
							+ "</b>";
				} else {
					searchText = searchText.substring(0, 50) + "...";
					searchText = i18n.GL0507() + " <b>" + searchText + "</b>";
				}

			} else {
				if (countValue > 0) {
					searchText = "" + i18n.GL1468() + " <b>" + searchText
							+ "</b>";
				} else {
					searchText = i18n.GL0507() + " <b>" + searchText + "</b>";
				}
			}
			queriedTextHtml.setHTML(searchText);
			queriedTextHtml.getElement().setAttribute("alt", StringUtil.removeHtml(searchText));
			queriedTextHtml.getElement().setAttribute("title", StringUtil.removeHtml(searchText));
			
		}
		
		if(searchDo.getSpellCheckQueryString() == null){
			correctSpellHTML.setVisible(false);
			spellerrorqueriedTextHtml.setVisible(false);
			queriedTextHtml.setVisible(true);
		}
		else
		{

			wrongQueryText.setText(searchDo.getUserQueryString());
			wrongQueryText.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)){
						AppClientFactory.fireEvent(new DisableSpellSearchEvent(PlaceTokens.RESOURCE_SEARCH,getSearchText(),""));
					}
					else if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.COLLECTION_SEARCH)){
						AppClientFactory.fireEvent(new DisableSpellSearchEvent(PlaceTokens.COLLECTION_SEARCH,getSearchText(),""));
					}
				}
			});

			final String correctSearchTerm = searchDo.getSpellCheckQueryString();
			String searchTextValue = i18n.GL1468() + " <b>" + correctSearchTerm + "</b>";
			
			queriedTextHtml.setHTML(searchTextValue);
			queriedTextHtml.getElement().setAttribute("alt", StringUtil.removeHtml(searchTextValue));
			queriedTextHtml.getElement().setAttribute("title", StringUtil.removeHtml(searchTextValue));
			
			correctQueryText.setText(searchDo.getSpellCheckQueryString());
			correctQueryText.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					correctSpellHTML.setVisible(false);
					spellerrorqueriedTextHtml.setVisible(false);
					queriedTextHtml.setVisible(true);
				}
			});
			
			correctSpellHTML.setVisible(true);
			spellerrorqueriedTextHtml.setVisible(true);
			queriedTextHtml.setVisible(false);
			
			
		}
		
		standardsConatiner.clear();
		System.out.println("presearch:::");
		showCategoryFilter();
		showSubjectsFilter();
		showGradesFilter();
		showStandardsFilter();
		showPublisherFilter();
		showAggregatorFilter();
		showOERFilter();
		showMobileFriendlyFilter();
		showAccessModeFilter();
		showAuthorFilter();
		showRatingsFilter();
		showReviewFilter();
		if(!(stdCode!=null || grades!=null || subjects!=null || oerTag!=null || mobileFirendlyTag!=null || ratingTag!=null|| publisher!=null || aggregator!=null || accessMode!=null || author!=null|| reviewTag!=null)){
			standardsConatiner.setVisible(false);
		}else{
			standardsConatiner.setVisible(true);
		}
		
		searchResultPanel.setClonnable(true);
		
		
		if (searchDo.getSearchResults() != null && searchDo.getSearchResults().size() > 0) {
			for (T searchResult : searchDo.getSearchResults()) {
				searchDo.getSearchHits();
				searchResultPanel.addDraggable(renderSearchResult(searchResult));
			}
			if (searchDo.getTotalPages() > 1) {
				if (searchDo.getPageNum() > 1) {
					paginationFocPanel.add(new PaginationButtonUc(searchDo.getPageNum() - 1, PREVIOUS, this));
				}
				int page = searchDo.getPageNum() < 10 ? 1 : searchDo.getPageNum() - 8;
				for (int count = 1; count < 10 && page <= searchDo.getTotalPages(); page++, ++count) {
					paginationFocPanel.add(new PaginationButtonUc(page, page == searchDo.getPageNum(), this));
				}
				if (searchDo.getPageNum() < searchDo.getTotalPages()) {
					paginationFocPanel.add(new PaginationButtonUc(searchDo.getPageNum() + 1, NEXT, this));
				}
			}
		}
		
		else {
			MixpanelUtil.Search_No_Results();
			if(AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().contains("resource-search"))
			{
			searchResultPanel.add(new NOSearchResultCollectionVc());
			
			}
			if(AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().contains("collection-search"))
			{
			searchResultPanel.add(new NOSearchResultCollectionVc());
			}
			
		}
		refreshShelfInfo = true;
		if (refreshedShelfCollections) {
			refreshShelfCollections(SHELF_COLLECTIONS);
		} else {
			AppClientFactory.fireEvent(new RequestShelfCollectionEvent());
		}
		searchResultPanel.setVisible(true);
	}

	@Override
	public void reset() {
		super.reset();
		refreshShelfInfo = false;
		refreshedShelfCollections = false;
		searchResultPanel.setClonnable(false);
		searchResultPanel.clear();
		paginationFocPanel.clear();
		SHELF_COLLECTIONS.clear();
		queriedTextHtml.setHTML("<p></p>");
		queriedTextHtml.getElement().setAttribute("alt","<p></p>");
		queriedTextHtml.getElement().setAttribute("title","<p></p>");
	}

	/**
	 * @return instance of {@link SearchFilterVc}
	 */
	public SearchFilterVc getSearchFilterVc() {
		return searchFilterVc;
	}
	
	

	@Override
	public void setSearchFilter(SearchFilterDo searchFilterDo) {
		getSearchFilterVc().renderFilter(searchFilterDo);
		getUiHandlers().initiateSearch();
	}

	@Override
	public Map<String, String> getSearchFilters() {
		return getSearchFilterVc().getFilter();
	}

	public abstract IsDraggable renderSearchResult(T searchDo);

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() instanceof PaginationButtonUc) {
			AppClientFactory.fireEvent(new SearchPaginationEvent(((PaginationButtonUc) event.getSource()).getPage()));
		} else {
			Window.alert("Event is not caught");
		}
	}

	/**
	 * @return instance of {@link AppMirageDragContainer}
	 */
	public AppMirageDragContainer getSearchResultPanel() {
		return searchResultPanel;
	}

	@Override
	public void setSourceSuggestions(SearchDo<String> sourceSuggestions) {
		searchFilterVc.setSourceSuggestions(sourceSuggestions);
	}

	@Override
	public void setAggregatorSuggestions(SearchDo<String> aggregatorSuggestions) {
		searchFilterVc.setAggregatorSuggestions(aggregatorSuggestions);
	}
	
	@Override
	public void setStandardsSuggestions(SearchDo<CodeDo> standardsSuggestions) {
		searchFilterVc.setStandardSuggestions(standardsSuggestions);
	}

	@Override
	public void registerDropController(ResourceDropController dropController, RegisterSearchDropEvent.DROP_AREA type) {
		dragController.unregisterDropController(dropController);
		dragController.registerDropController(dropController);
	}

	@Override
	public void unregisterDropController(ResourceDropController dropController, RegisterSearchDropEvent.DROP_AREA type) {
		dragController.unregisterDropController(dropController);
	}

	@Override
	public void onUnload() {
		super.onUnload();
		dragController.unregisterDropControllers();
	}

	@Override
	public void setShelfCollections(List<FolderDo> shelfCollections) {
		SHELF_COLLECTIONS.clear();
		if (shelfCollections != null) {
			SHELF_COLLECTIONS.addAll(shelfCollections);
		}
		refreshedShelfCollections = true;
		if (refreshShelfInfo) {
			refreshShelfCollections(SHELF_COLLECTIONS);
		}
	}

	protected abstract void refreshShelfCollections(List<FolderDo> shelfCollections);

	@Override
	public void setStandardsSuggestionsInfo(SearchDo<CodeDo> result) {
		searchFilterVc.setSourceSuggestionsInfo(result);
	}
	
	@Override
	public void resetFilters(){
		searchFilterVc.clearAllFields();
	}
	
	public Button getBrowseBtn()
	{
		return searchFilterVc.browseStandards;
	}
	
	public void OnStandardsClickEvent(Button standardsButtonClicked)
	{
		if(handlerRegistration!=null){
			handlerRegistration.removeHandler();
		}
		handlerRegistration=standardsButtonClicked.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				getUiHandlers().setUpdatedStandards();
		
				
			}
		});
	}
	
	public void setUpdatedStandards(String standardsCode)
	{
		if(!standardsCode.isEmpty())
		{
		searchFilterVc.addStandardFilter(standardsCode);
		AppClientFactory.fireEvent(new GetSearchKeyWordEvent());
		}
		getUiHandlers().closeStandardsPopup();
	}
	
	@Override
	public String getSearchText() {
//		return searchBarVc.getSearchText();
		return AppClientFactory.getPlaceManager().getRequestParameter("query");
	}
	
	/**
	 * Set resource search page view
	 * @param clickEvent instance of {@link ClickEvent}
	 */
	@UiHandler("resourceSearchBtn")
	public void onResourceSearchButtonClicked(ClickEvent clickEvent) {
		if(getSearchText()!=null || getSearchText().length()>0){
			MixpanelUtil.Show_Resource_Search_Results();
			AppClientFactory.fireEvent(new SwitchSearchEvent(PlaceTokens.RESOURCE_SEARCH,getSearchText()));	
		}
		
		if(!AppClientFactory.getPlaceManager().getRequestParameter("query").equalsIgnoreCase("")){ 
			MixpanelUtil.Show_Collection_Search_Results();
			AppClientFactory.fireEvent(new SwitchSearchEvent(PlaceTokens.RESOURCE_SEARCH,AppClientFactory.getPlaceManager().getRequestParameter("query")));
		}
	}

	/**
	 * Set collection search page view 
	 * @param clickEvent instance of {@link ClickEvent}
	 */
	@UiHandler("collectionSearchBtn")
	public void onCollectionSearchBtnClicked(ClickEvent clickEvent) {
		if(getSearchText()!=null || getSearchText().length()>0){
			MixpanelUtil.Show_Collection_Search_Results();
			AppClientFactory.fireEvent(new SwitchSearchEvent(PlaceTokens.COLLECTION_SEARCH,getSearchText()));
		}
		if(!AppClientFactory.getPlaceManager().getRequestParameter("query").equalsIgnoreCase("")){ 
			MixpanelUtil.Show_Collection_Search_Results();
			AppClientFactory.fireEvent(new SwitchSearchEvent(PlaceTokens.COLLECTION_SEARCH,AppClientFactory.getPlaceManager().getRequestParameter("query")));
		}
	}
	/**
	 * Pre-Selected Subjects showing in search page
	 */
	private void showSubjectsFilter() {
		subjects = AppClientFactory.getPlaceManager().getRequestParameter("flt.subjectName");
		if(subjects!=null){
			String[] split = subjects.split("~~");
			for(int i=0; i<split.length; i++){
				standardsConatiner.add(createTagsLabel(split[i],"subjectPanel"));
			}
				
		}
	}
	/**
	 * Pre-Selected Subjects showing in search page
	 */
	private void showCategoryFilter() {
		categories = AppClientFactory.getPlaceManager().getRequestParameter("category");
		if(categories!=null){
			String[] split = categories.split(",");
			for(int i=0; i<split.length; i++){
				if(!split[i].equalsIgnoreCase("all"))
				{
				standardsConatiner.add(createTagsLabel(split[i],"categoryPanel"));
				}
			}
				
		}
	}

	/**
	 * Pre-Selected grades showing in search page
	 */
	private void showGradesFilter() {
	    grades = AppClientFactory.getPlaceManager().getRequestParameter("flt.grade");
		if(grades!=null){
			String[] gradesSplit = grades.split(",");
			for(int i=0; i<gradesSplit.length; i++){
				if(gradesSplit[i].equals("12gte")){
					standardsConatiner.add(createTagsLabel(i18n.GL3084(),"gradePanel"));
				}
				else if(gradesSplit[i].equalsIgnoreCase("pre-k")){
					standardsConatiner.add(createTagsLabel(i18n.GL3070(),"gradePanel"));
				}
				else{
					standardsConatiner.add(createTagsLabel(i18n.GL0325()+" "+gradesSplit[i],"gradePanel"));
				}
				
			}
				
		}
	}
	/**
	 * Pre-Selected Standards showing in search page
	 */
	private void showStandardsFilter() {
	    stdCode = AppClientFactory.getPlaceManager().getRequestParameter("flt.standard");
		if(stdCode!=null){
			String[] stdSplit = stdCode.split(",");
			for(int i=0; i<stdSplit.length; i++){
				standardsConatiner.add(createTagsLabel(stdSplit[i],"standPanel"));
			}
//			standardsConatiner.add(createTagsLabel(stdCode,"standPanel"));
		}
	}
	/**
	 * To show the publisher values in search page
	 */
	private void showPublisherFilter() {
		publisher = AppClientFactory.getPlaceManager().getRequestParameter("flt.publisher");
		if(publisher!=null){
			String[] split = publisher.split(",");
			for(int i=0; i<split.length; i++){
				standardsConatiner.add(createTagsLabel(split[i],"publisherPanel"));
			}
				
		}
	}
	
	/**
	 * To show the aggregator values in search page
	 */
	private void showAggregatorFilter() {
		aggregator = AppClientFactory.getPlaceManager().getRequestParameter("flt.aggregator");
		if(aggregator!=null){
			String[] split = aggregator.split(",");
			for(int i=0; i<split.length; i++){
				standardsConatiner.add(createTagsLabel(split[i],"aggregatorPanel"));
			}
				
		}
	}
	/**
	 * To show the Access mode values in search page
	 */
	private void showAccessModeFilter() {
		accessMode = AppClientFactory.getPlaceManager().getRequestParameter("flt.cfAccessMode");
		if(accessMode!=null){
			String[] split = accessMode.split(",");
			for(int i=0; i<split.length; i++){
				standardsConatiner.add(createTagsLabel(split[i],"accessPanel"));
			}
				
		}
	}
	/**
	 * To show the Author values in search page
	 */
	private void showAuthorFilter() {
		author = AppClientFactory.getPlaceManager().getRequestParameter("flt.owner");
		if(author!=null){
			String[] split = author.split(",");
			for(int i=0; i<split.length; i++){
				standardsConatiner.add(createTagsLabel(split[i],"authorPanel"));
			}
				
		}
	}
	/**
	 * To show the Author values in search page
	 */
	private void showReviewFilter() {
		reviewTag = AppClientFactory.getPlaceManager().getRequestParameter("flt.isReviewed");
		if(reviewTag!=null){
			if(reviewTag.equalsIgnoreCase("1"))
			{
				standardsConatiner.add(createTagsLabel("Only Resources with Reviews","onlyReviewPanel"));
			}

		}
	}

	/**
	 * Show user searched filter 
	 * 
	 * @param filterValue
	 *            search filter of the label widget which is user searched filter value
	 * @return the label of user search filter.
	 */
	protected CloseLabelSetting createTagsLabel(final String filterValue, final String panelName) {
		return new CloseLabelSetting(filterValue) {

			@Override
			public void onCloseLabelClick(ClickEvent event) {
				String newFilterVal = filterValue;
				if(filterValue.contains("Grade "))
				{
					newFilterVal = filterValue.replaceAll("Grade ", "");
				}
				else if(filterValue.contains("Higher Ed"))
				{
					newFilterVal = filterValue.replaceAll("Higher Ed", "12gte");
				}
				AppClientFactory.fireEvent(new SearchFilterEvent(newFilterVal,panelName));
			}
		};
	}
	
	/**
	 * Pre-Selected Standards showing in search page
	 */
	private void showOERFilter() {
		oerTag = AppClientFactory.getPlaceManager().getRequestParameter("flt.isOer");
		if(oerTag!=null){
			if(oerTag.equalsIgnoreCase("1"))
			{
				standardsConatiner.add(createTagsLabel("OER","oerPanel"));
			}

		}
	}
	/**
	 * Pre-Selected Standards showing in search page
	 */
	private void showMobileFriendlyFilter() {
		mobileFirendlyTag = AppClientFactory.getPlaceManager().getRequestParameter("fltNot.mediaType");
		if(mobileFirendlyTag!=null){
			if(mobileFirendlyTag.equalsIgnoreCase("not_ipad_friendly"))
			{
				standardsConatiner.add(createTagsLabel("Mobile Friendly","mobileFirendlyPanel"));
			}

		}
	}
	/**
	 * Pre-Selected Standards showing in search page
	 */
	private void showRatingsFilter() {

		ratingTag = AppClientFactory.getPlaceManager().getRequestParameter("flt.rating");
		if(ratingTag!=null){
			if(ratingTag.equalsIgnoreCase("5,4,3,2,1,0"))
			{
				standardsConatiner.add(createTagsLabel("All Ratings","ratingallPanel"));
			}
			else 
			{
				String[] ratingsSplit = ratingTag.split(",");
				for(int i=0; i<ratingsSplit.length; i++){
					if(ratingsSplit[i].equalsIgnoreCase("0"))
					{
						standardsConatiner.add(createTagsLabel("No Ratings","ratingPanel"));	
					}
					else
					{
					standardsConatiner.add(createTagsLabel(ratingsSplit[i]+" Star","ratingPanel"));
					}
				}
			}
			

		}
	}
	
	
	public abstract void setAddResourceContainerPresenter(AddResourceContainerPresenter addResourceContainerPresenter);

}
