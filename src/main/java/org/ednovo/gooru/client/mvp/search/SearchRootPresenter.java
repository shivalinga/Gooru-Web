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

import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BasePresenter;
import org.ednovo.gooru.client.mvp.search.collection.CollectionSearchPresenter;
import org.ednovo.gooru.client.mvp.search.event.GetSearchKeyWordEvent;
import org.ednovo.gooru.client.mvp.search.event.PostSearchEvent;
import org.ednovo.gooru.client.mvp.search.event.PreSearchEvent;
import org.ednovo.gooru.client.mvp.search.event.RefreshSearchEvent;
import org.ednovo.gooru.client.mvp.search.resource.ResourceSearchPresenter;
import org.ednovo.gooru.client.mvp.shelf.list.ShelfListPresenter;
import org.ednovo.gooru.client.mvp.wrap.WrapPresenter;
import org.ednovo.gooru.client.service.SearchServiceAsync;
import org.ednovo.gooru.shared.model.search.ResourceSearchResultDo;
import org.ednovo.gooru.shared.model.search.SearchDo;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

/**
 * @author Search Team
 * 
 */
public class SearchRootPresenter extends BasePresenter<IsSearchRootView, SearchRootPresenter.IsSearchRootProxy> implements SearchRootUiHandlers {

	@Inject
	private SearchServiceAsync searchService;

	private ShelfListPresenter shelfTabPresenter;
	private AppClientFactory factory;
	boolean viewcookie=true;

	Document document = Document.get();

	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_VIEW = new Type<RevealContentHandler<?>>();

	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_SHELF_TAB = new Type<RevealContentHandler<?>>();

	@ProxyCodeSplit
	public interface IsSearchRootProxy extends Proxy<SearchRootPresenter> {
	}

	/**
	 * Class constructor
	 * 
	 * @param shelfTabPresenter instance of {@link ShelfListPresenter}
	 * @param view {@link View}
	 * @param proxy {@link Proxy}
	 * @param resourceSearchPresenter {@link ResourceSearchPresenter}
	 * @param collectionSearchPresenter {@link CollectionSearchPresenter}
	 */
	@Inject
	public SearchRootPresenter(ShelfListPresenter shelfTabPresenter, IsSearchRootView view, IsSearchRootProxy proxy, ResourceSearchPresenter resourceSearchPresenter, CollectionSearchPresenter collectionSearchPresenter) {
		super(view, proxy);
		getView().setUiHandlers(this);
		addRegisteredHandler(PreSearchEvent.TYPE, this);
		addRegisteredHandler(PostSearchEvent.TYPE, this);
		addRegisteredHandler(GetSearchKeyWordEvent.TYPE, this);
		this.shelfTabPresenter = shelfTabPresenter;
	}

	@Override
	public void onBind() {
		super.onBind();
	
		
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		Window.scrollTo(0,0);
	}

	

	
	
	@Override
	public void onReset() {
		super.onReset();
		
	    	
		String msg = Cookies.getCookie("TakeATour");
	    if (msg==null)
			msg="";
//		getView().clearPanel();
		/*if(!msg.equalsIgnoreCase("viewed") && AppClientFactory.isAnonymous()){
//			document.getElementById("searchFilterPanelDiv").getStyle().setMarginTop(-95, Unit.PX);
//			getView().addresource();
		} else {
			document.getElementById("searchFilterPanelDiv").getStyle().setMarginTop(0, Unit.PX);
		}*/
	/*	document.getElementById("searchFilterPanelDiv").getStyle().setMarginTop(0, Unit.PX);*/
		shelfTabPresenter.enableFolderCollectionPanel();
		setInSlot(TYPE_SHELF_TAB, shelfTabPresenter);
		
	}

	public SearchServiceAsync getSearchService() {
		return searchService;
	}

	public void setSearchService(SearchServiceAsync searchService) {
		this.searchService = searchService;
	}

	@Override
	public <T extends ResourceSearchResultDo> void postSearch(SearchDo<T> searchDo) {
		getView().postSearch(searchDo);
	}

	@Override
	public <T extends ResourceSearchResultDo> void preSearch(SearchDo<T> searchDo) {
		getView().preSearch(searchDo);
	}
	
	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, WrapPresenter.TYPE_VIEW, this);
	}

	@Override
	public void getSearchKeyword() {
		String searchText=getView().getSearchText();
		if (searchText != null && searchText.length() > 0) {
			AppClientFactory.fireEvent(new RefreshSearchEvent(searchText));
		}
	}

}
