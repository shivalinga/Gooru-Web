package org.ednovo.gooru.client.mvp.search.event;

import org.ednovo.gooru.client.mvp.search.SearchFilterVc;

import com.google.gwt.event.shared.GwtEvent;


public class SearchFilterEvent extends GwtEvent<SearchFilterEventHandler>{

	public static final Type<SearchFilterEventHandler> TYPE = new Type<SearchFilterEventHandler>();

	private SearchFilterVc searchFilterVc;
	private boolean resource;
	
	public SearchFilterEvent(SearchFilterVc searchFilterVc,boolean resource) {
		this.searchFilterVc=searchFilterVc;
		this.resource=resource;
	}

	
	@Override
	public Type<SearchFilterEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SearchFilterEventHandler handler) {
		handler.requestSearchFilterd(searchFilterVc,resource);
		
	}
	
}
