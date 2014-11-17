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

import java.util.List;
import java.util.Map;

import org.ednovo.gooru.client.gin.IsViewWithHandlers;
import org.ednovo.gooru.client.mvp.resource.dnd.ResourceDropController;
import org.ednovo.gooru.client.mvp.search.event.RegisterSearchDropEvent;
import org.ednovo.gooru.shared.model.code.CodeDo;
import org.ednovo.gooru.shared.model.folder.FolderDo;
import org.ednovo.gooru.shared.model.search.ResourceSearchResultDo;
import org.ednovo.gooru.shared.model.search.SearchDo;
import org.ednovo.gooru.shared.model.search.SearchFilterDo;

import com.google.gwt.user.client.ui.Button;

/**
 * @author Search Team
 * 
 */
public interface IsSearchView<T extends ResourceSearchResultDo> extends IsViewWithHandlers<SearchUiHandlers> {
	
	String CATEGORY_FLT = "category";
	
	String SUBJECT_FLT = "flt.subjectName";
	
	String STANDARD_FLT = "flt.standard";
	
	String GRADE_FLT = "flt.grade";
	
	/*String SOURCE_FLT = "flt.source";*/
	
	String PUBLISHER_FLT = "flt.publisher";
	
	String OWNER_FLT = "flt.owner";
	
	String MEDIATYPE_FLT = "fltNot.mediaType";
	
	String OER_FLT = "flt.isOer";
	
	String AGGREGATOR_FLT = "flt.aggregator";
	
	String ACCESS_MODE_FLT = "flt.cfAccessMode";
	
	/**
	 * Set post search view
	 * @param searchDo instance of {@link SearchDo}
	 */
	void postSearch(SearchDo<T> searchDo);
	
	/**
	 * Set previous search view
	 * @param searchDo instance of {@link SearchDo}
	 */
	void preSearch(SearchDo<T> searchDo);
	
	/**
	 * Set search filters
	 * @param searchFilterDo instance of {@link SearchFilterDo}
	 */
	void setSearchFilter(SearchFilterDo searchFilterDo);
	
	/**
	 * Get filters for search
	 * @return search filters
	 */
	Map<String,String> getSearchFilters();
	
	/**
	 * Set source suggestion
	 * @param sourceSuggestions 
	 */
	void setSourceSuggestions(SearchDo<String> sourceSuggestions);
	
	/**
	 * Set standards suggestion
	 * @param result instance of {@link SearchDo} type 
	 */
	void setStandardsSuggestions(SearchDo<CodeDo> result);
	
	/**
	 * Set suggestion standard info
	 * @param result
	 */
	void setStandardsSuggestionsInfo(SearchDo<CodeDo> result);
	
	/**
	 * Register a new DropController, representing a new drop target, with this drag controller and Unregister a DropController from this drag controller.
	 * @param dropController instance of {@link ResourceDropController}
	 * @param type Drop area
	 */
	void registerDropController(ResourceDropController dropController, RegisterSearchDropEvent.DROP_AREA type);
	
	/**
	 * Unregister a DropController from this drag controller.
	 * @param dropController instance of {@link ResourceDropController}
	 * @param type Drop area
	 */
	void unregisterDropController(ResourceDropController dropController, RegisterSearchDropEvent.DROP_AREA type);
	
	/**
	 * Add collections to user shelf
	 * @param shelfCollections collections list
	 */
	void setShelfCollections(List<FolderDo> shelfCollections) ;

	void resetFilters();

	/**
	 * Set standards suggestion
	 * @param aggregatorSuggestions instance of {@link SearchDo} type 
	 */
	void setAggregatorSuggestions(SearchDo<String> aggregatorSuggestions);
	
	public void setAddResourceContainerPresenter(AddResourceContainerPresenter addResourceContainerPresenter);

	void setUpdatedStandards(String setStandardsVal);

	void OnStandardsClickEvent(Button addBtn);

	Map<String, String> getSearchFilters1();

	SearchFilterVc getSearchFilterVc();

}
