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
package org.ednovo.gooru.client.mvp.wrap;

import org.ednovo.gooru.client.event.ActivateSearchBarEvent;
import org.ednovo.gooru.client.event.ActivateSearchBarHandler;
import org.ednovo.gooru.client.event.InvokeGooruGuideBubbleEvent;
import org.ednovo.gooru.client.event.InvokeGooruGuideBubbleHandler;
import org.ednovo.gooru.client.event.InvokeLoginEvent;
import org.ednovo.gooru.client.event.InvokeLoginHandler;
import org.ednovo.gooru.client.event.InvokeRegisterHandler;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BasePresenter;
import org.ednovo.gooru.client.mvp.home.event.HeaderTabType;
import org.ednovo.gooru.client.mvp.home.event.HomeEvent;
import org.ednovo.gooru.client.mvp.home.event.HomeHandler;
import org.ednovo.gooru.client.mvp.home.event.SetDiscoverLinkEvent;
import org.ednovo.gooru.client.mvp.home.event.SetDiscoverLinkHandler;
import org.ednovo.gooru.client.mvp.prime.PrimePresenter;
import org.ednovo.gooru.client.mvp.search.SearchCBundle;
import org.ednovo.gooru.client.mvp.search.SearchFilterVc;
import org.ednovo.gooru.client.mvp.search.event.FilterEvent;
import org.ednovo.gooru.client.mvp.search.event.FilterHandler;
import org.ednovo.gooru.client.mvp.search.event.SearchFilterEvent;
import org.ednovo.gooru.client.mvp.search.event.SearchFilterEventHandler;
import org.ednovo.gooru.client.mvp.wrap.WrapPresenter.IsWrapProxy;
import org.ednovo.gooru.shared.model.search.SearchFilterDo;
import org.ednovo.gooru.shared.model.user.UserDo;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

/**
 * @author Search Team
 * 
 */
public class WrapPresenter extends BasePresenter<IsWrapView, IsWrapProxy> implements InvokeLoginHandler, InvokeRegisterHandler, ActivateSearchBarHandler, InvokeGooruGuideBubbleHandler,HomeHandler,SetDiscoverLinkHandler,SearchFilterEventHandler,FilterHandler {
	
	private String  RESOURCE_SEARCH="resource-search";
	private String COLLECTION_SEARCH="collection-search";
	
	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_VIEW = new Type<RevealContentHandler<?>>();

	@ProxyStandard
	public interface IsWrapProxy extends Proxy<WrapPresenter> {
	}

	@Inject
	public WrapPresenter(IsWrapView view, IsWrapProxy proxy) {
		super(view, proxy);
		addRegisteredHandler(InvokeLoginEvent.TYPE, this);
		addRegisteredHandler(ActivateSearchBarEvent.TYPE, this);
		addRegisteredHandler(InvokeGooruGuideBubbleEvent.TYPE, this);
		addRegisteredHandler(HomeEvent.TYPE, this);
		addRegisteredHandler(SetDiscoverLinkEvent.TYPE, this);
		addRegisteredHandler(SearchFilterEvent.TYPE, this);
		addRegisteredHandler(FilterEvent.TYPE, this);
	}

	@Override
	protected final void revealInParent() {
		RevealContentEvent.fire(this, PrimePresenter.TYPE_VIEW, this);
	}

	@Override
	public void invokeLogin() {
		getView().invokeLogin();
	}
	
	public void setLoginData(UserDo user) {
		getView().setLoginData(user);
	}

	@Override
	public void invokeRegister() {
		getView().invokeRegister();
	}

	@Override
	public void activateSearchBar(boolean activate) {
		getView().activateSearchBar(activate);
	}

	@Override
	public void invokeGooruGuideBubble() {
//		getView().invokeGooruGuideBubble();
		
	}

	@Override
	public void activateClassicButton(boolean activate) {
		getView().activateClassicButton(activate);
		
	}

	@Override
	public void setSelected(HeaderTabType tabType) {
			
			getView().setDotsPanelSelection(tabType);
			
	}

	/* (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.home.event.SetDiscoverLinkHandler#setDiscoverLink(java.lang.String)
	 */
	@Override
	public void setDiscoverLink(String discoverLink) {
		getView().setDiscoverLinkFromLibrary(discoverLink);
	}

	@Override
	public void requestSearchFilterd(SearchFilterVc filterVc,boolean resource) {
		// TODO Auto-generated method stub
		/*getView().getSearchFiltersPanel().clear();
		getView().getSearchFiltersPanel().getElement().setId("left-menu");
		filterVc.getMainContainer().setStyleName("col-md-12");
		filterVc.getMainContainer().addStyleName(SearchCBundle.INSTANCE.css().filtersContainer());
		filterVc.getMainContainer().addStyleName(SearchCBundle.INSTANCE.css().searchFilters());
		filterVc.getMainContainer().addStyleName("hidden-sm");
		filterVc.getMainContainer().addStyleName("hidden-md");
		getView().getSearchFiltersPanel().add(filterVc);*/

		
		if(resource){
		getView().getSearchFiltersPanel().clear();
		getView().getSearchFiltersPanel().getElement().setId("left-menu");
		filterVc.getMainContainer().setStyleName("col-md-12");
		filterVc.getMainContainer().addStyleName(SearchCBundle.INSTANCE.css().filtersContainer());
		filterVc.getMainContainer().addStyleName(SearchCBundle.INSTANCE.css().searchFilters());
		filterVc.getMainContainer().addStyleName("hidden-sm");
		filterVc.getMainContainer().addStyleName("hidden-md");
		getView().getSearchFiltersPanel().add(filterVc);
		}else{
			getView().getCollectionSearchFiltersPanel().clear();
			getView().getCollectionSearchFiltersPanel().getElement().setId("left-menu");
			filterVc.getMainContainer().setStyleName("col-md-12");
			filterVc.getMainContainer().addStyleName(SearchCBundle.INSTANCE.css().filtersContainer());
			filterVc.getMainContainer().addStyleName(SearchCBundle.INSTANCE.css().searchFilters());
			filterVc.getMainContainer().addStyleName("hidden-sm");
			filterVc.getMainContainer().addStyleName("hidden-md");
			getView().getCollectionSearchFiltersPanel().add(filterVc);
		}
	}

	@Override
	public void searchFilters(SearchFilterDo searchDo) {
		// TODO Auto-generated method stub
		String nameToken=AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken();
		if(nameToken!=null&&nameToken.equalsIgnoreCase(RESOURCE_SEARCH)){
			Widget widget=getView().getSearchFiltersPanel()!=null?getView().getSearchFiltersPanel().getWidget(0):null;
			if(widget!=null&&widget instanceof SearchFilterVc){
				SearchFilterVc filterVc=(SearchFilterVc)widget;
				filterVc.renderFilter(searchDo);	
			}
		}else if(nameToken!=null&&nameToken.equalsIgnoreCase(COLLECTION_SEARCH)){
			Widget widget=getView().getCollectionSearchFiltersPanel()!=null?getView().getCollectionSearchFiltersPanel().getWidget(0):null;
			if(widget!=null&&widget instanceof SearchFilterVc){
				SearchFilterVc filterVc=(SearchFilterVc)widget;
				filterVc.renderFilter(searchDo);	
			}
		}
	
	}

	@Override
	protected void onReset() {
		// TODO Auto-generated method stub
		super.onReset();
		
		String nameToken=AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken();
		if(nameToken!=null&&nameToken.equalsIgnoreCase(RESOURCE_SEARCH)){
			getView().getSearchFiltersPanel().getElement().getStyle().setDisplay(Display.BLOCK);
			getView().getCollectionSearchFiltersPanel().getElement().getStyle().setDisplay(Display.NONE);
		}else if(nameToken!=null&&nameToken.equalsIgnoreCase(COLLECTION_SEARCH)){
			getView().getSearchFiltersPanel().getElement().getStyle().setDisplay(Display.NONE);
			getView().getCollectionSearchFiltersPanel().getElement().getStyle().setDisplay(Display.BLOCK);

		}
	}
}
