package org.ednovo.gooru.client.mvp.search.event;

import org.ednovo.gooru.shared.model.search.SearchFilterDo;

import com.google.gwt.event.shared.GwtEvent;


public class FilterEvent extends GwtEvent<FilterHandler>{

	public static final Type<FilterHandler> TYPE = new Type<FilterHandler>();

	private SearchFilterDo searchDo;
	
	public FilterEvent(SearchFilterDo searchDo) {
		setSearchDo(searchDo);
	}

	
	@Override
	public Type<FilterHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(FilterHandler handler) {
		handler.searchFilters(searchDo);;
		
	}
	public SearchFilterDo getSearchDo() {
		return searchDo;
	}

	public void setSearchDo(SearchFilterDo searchDo) {
		this.searchDo = searchDo;
	}
}
