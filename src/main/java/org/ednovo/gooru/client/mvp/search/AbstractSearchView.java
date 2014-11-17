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

import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BaseViewWithHandlers;
import org.ednovo.gooru.client.mvp.dnd.AppMirageDragContainer;
import org.ednovo.gooru.client.mvp.dnd.IsDraggable;
import org.ednovo.gooru.client.mvp.resource.dnd.ResourceDragController;
import org.ednovo.gooru.client.mvp.resource.dnd.ResourceDropController;
import org.ednovo.gooru.client.mvp.search.event.FilterEvent;
import org.ednovo.gooru.client.mvp.search.event.GetSearchKeyWordEvent;
import org.ednovo.gooru.client.mvp.search.event.RegisterSearchDropEvent;
import org.ednovo.gooru.client.mvp.search.event.RequestShelfCollectionEvent;
import org.ednovo.gooru.client.mvp.search.event.SearchFilterEvent;
import org.ednovo.gooru.client.mvp.search.event.SearchPaginationEvent;
import org.ednovo.gooru.client.uc.PaginationButtonUc;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.code.CodeDo;
import org.ednovo.gooru.shared.model.folder.FolderDo;
import org.ednovo.gooru.shared.model.search.ResourceSearchResultDo;
import org.ednovo.gooru.shared.model.search.SearchDo;
import org.ednovo.gooru.shared.model.search.SearchFilterDo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Search Team
 * 
 * @param <T>
 *            type of ResourceSearchResultDo
 */
public abstract class AbstractSearchView<T extends ResourceSearchResultDo>
		extends BaseViewWithHandlers<SearchUiHandlers> implements
		IsSearchView<T>, ClickHandler {

	private static AbstractSearchViewUiBinder uiBinder = GWT
			.create(AbstractSearchViewUiBinder.class);

	private static MessageProperties i18n = GWT.create(MessageProperties.class);

	interface AbstractSearchViewUiBinder extends
			UiBinder<Widget, AbstractSearchView<?>> {
	}

	@UiField(provided = true)
	SearchFilterVc searchFilterVc;

	SearchFilterVc searchFilterVc1;

	@UiField(provided = true)
	AppMirageDragContainer searchResultPanel;

	@UiField
	FlowPanel paginationFocPanel;

	@UiField
	ScrollPanel searchFilterPanel;

	protected ResourceDragController dragController;

	private static final String PREVIOUS = i18n.GL1462().toUpperCase();

	private static final String NEXT = i18n.GL1463().toUpperCase();

	protected final List<FolderDo> SHELF_COLLECTIONS = new ArrayList<FolderDo>();

	public boolean refreshShelfInfo;

	public boolean refreshedShelfCollections;
	String rootWebUrl = AppClientFactory.getPlaceManager()
			.getCurrentPlaceRequest().getNameToken();

	private HandlerRegistration handlerRegistration = null;

	/**
	 * Assign new instance for {@link ResourceDragController},
	 * {@link AppMirageDragContainer}, {@link SearchFilterVc}
	 * 
	 * @param resourceSearch
	 *            whether resource search or not
	 */
	public AbstractSearchView(boolean resourceSearch) {
		dragController = new ResourceDragController(RootPanel.get());
		searchResultPanel = new AppMirageDragContainer(dragController);
		searchFilterVc = new SearchFilterVc(resourceSearch);
		searchFilterVc1 = new SearchFilterVc(resourceSearch);
		setWidget(uiBinder.createAndBindUi(this));

		searchFilterPanel.getElement().setId("searchFilterPanelDiv");
		searchFilterPanel.getElement().getStyle().setOverflow(Overflow.VISIBLE);
		searchFilterVc.getElement().setId("searchFilterVc");
		paginationFocPanel.getElement().setId("fnlPaginationFocPanel");
		searchResultPanel.getElement().setId("appMirageDragContainer");
		AppClientFactory.fireEvent(new SearchFilterEvent(searchFilterVc1,resourceSearch));


		getBrowseBtn().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getUiHandlers().getAddStandards();

			}
		});
		searchFilterVc1.browseStandards.addClickHandler(new ClickHandler() {

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
		searchFilterVc1.setFilter(searchDo.getFilters());
		if (!AppClientFactory.isAnonymous()) {
			searchFilterVc.getUserStandardPrefCodeId();
			searchFilterVc1.getUserStandardPrefCodeId();
		} else {
			searchFilterVc.getStandardVisiblity();
			searchFilterVc1.getStandardVisiblity();
		}
	}

	@Override
	public void postSearch(SearchDo<T> searchDo) {
		// searchResultPanel.setVisible(false);
		searchResultPanel.setClonnable(true);
		if (searchDo.getSearchResults() != null	&& searchDo.getSearchResults().size() > 0) {
			for (T searchResult : searchDo.getSearchResults()) {
				searchDo.getSearchHits();
				searchResultPanel
						.addDraggable(renderSearchResult(searchResult));
			}
			if (searchDo.getTotalPages() > 1) {
				if (searchDo.getPageNum() > 1) {
					paginationFocPanel.add(new PaginationButtonUc(searchDo.getPageNum() - 1, PREVIOUS, this));
				}
				int page = searchDo.getPageNum() < 10 ? 1 : searchDo.getPageNum() - 8;
				for (int count = 1; count < 10&& page <= searchDo.getTotalPages(); page++, ++count) {
					paginationFocPanel.add(new PaginationButtonUc(page,
							page == searchDo.getPageNum(), this));
				}
				if (searchDo.getPageNum() < searchDo.getTotalPages()) {
					paginationFocPanel.add(new PaginationButtonUc(searchDo
							.getPageNum() + 1, NEXT, this));
				}
			}
		}

		else {
			MixpanelUtil.Search_No_Results();
			if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest()
					.getNameToken().contains("resource-search")) {
				searchResultPanel.add(new NOSearchResultCollectionVc());

			}
			if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest()
					.getNameToken().contains("collection-search")) {
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
	}

	/**
	 * @return instance of {@link SearchFilterVc}
	 */
	@Override
	public SearchFilterVc getSearchFilterVc() {
		return searchFilterVc;
	}

	@Override
	public void setSearchFilter(SearchFilterDo searchFilterDo) {
		getSearchFilterVc().renderFilter(searchFilterDo);
		searchFilterVc1.renderFilter(searchFilterDo);
		System.out.println("====================I am In Set Search Filters");
		AppClientFactory.getEventBus().fireEvent(new FilterEvent(searchFilterDo));

		getUiHandlers().initiateSearch();
	}

	@Override
	public Map<String, String> getSearchFilters() {
		return getSearchFilterVc().getFilter();
	}
	
	@Override
	public Map<String,String> getSearchFilters1(){
		return searchFilterVc1.getFilter();
	}
	public abstract IsDraggable renderSearchResult(T searchDo);

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() instanceof PaginationButtonUc) {
			AppClientFactory.fireEvent(new SearchPaginationEvent(
					((PaginationButtonUc) event.getSource()).getPage()));
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
		searchFilterVc1.setSourceSuggestions(sourceSuggestions);
		searchFilterVc.setSourceSuggestions(sourceSuggestions);
	}

	@Override
	public void setAggregatorSuggestions(SearchDo<String> aggregatorSuggestions) {
		searchFilterVc.setAggregatorSuggestions(aggregatorSuggestions);
		searchFilterVc1.setAggregatorSuggestions(aggregatorSuggestions);

	}

	@Override
	public void setStandardsSuggestions(SearchDo<CodeDo> standardsSuggestions) {
		searchFilterVc.setStandardSuggestions(standardsSuggestions);
		searchFilterVc1.setStandardSuggestions(standardsSuggestions);

	}

	@Override
	public void registerDropController(ResourceDropController dropController,
			RegisterSearchDropEvent.DROP_AREA type) {
		dragController.unregisterDropController(dropController);
		dragController.registerDropController(dropController);
	}

	@Override
	public void unregisterDropController(ResourceDropController dropController,
			RegisterSearchDropEvent.DROP_AREA type) {
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

	protected abstract void refreshShelfCollections(
			List<FolderDo> shelfCollections);

	@Override
	public void setStandardsSuggestionsInfo(SearchDo<CodeDo> result) {
		searchFilterVc.setSourceSuggestionsInfo(result);
		searchFilterVc1.setSourceSuggestionsInfo(result);

	}

	@Override
	public void resetFilters() {
		searchFilterVc.clearAllFields();
		searchFilterVc1.clearAllFields();
		

	}

	public Button getBrowseBtn() {
		return searchFilterVc.browseStandards;
	}

	public void OnStandardsClickEvent(Button standardsButtonClicked) {
		if (handlerRegistration != null) {
			handlerRegistration.removeHandler();
		}
		handlerRegistration = standardsButtonClicked
				.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						getUiHandlers().setUpdatedStandards();

					}
				});
	}

	public void setUpdatedStandards(String standardsCode) {
		if (!standardsCode.isEmpty()) {
			searchFilterVc.addStandardFilter(standardsCode);
			AppClientFactory.fireEvent(new GetSearchKeyWordEvent());
		}
		getUiHandlers().closeStandardsPopup();
	}

	public abstract void setAddResourceContainerPresenter(
			AddResourceContainerPresenter addResourceContainerPresenter);

}
