package org.ednovo.gooru.client.mvp.search.event;

import org.ednovo.gooru.client.mvp.search.SearchFilterVc;
import org.ednovo.gooru.shared.model.search.SearchDo;

import com.google.gwt.event.shared.EventHandler;

public interface SearchFilterEventHandler extends EventHandler{

	/**
	 * Get aggregator source
	 * @param searchDo instance of {@link SearchDo}
	 */
	void requestSearchFilterd(SearchFilterVc filterVc,boolean isResource);

}
