package org.ednovo.gooru.client.mvp.search.event;

import org.ednovo.gooru.shared.model.search.SearchDo;
import org.ednovo.gooru.shared.model.search.SearchFilterDo;

import com.google.gwt.event.shared.EventHandler;

public interface FilterHandler extends EventHandler{

	/**
	 * Get aggregator source
	 * @param searchDo instance of {@link SearchDo}
	 */
	void searchFilters(SearchFilterDo searchDo);

}
