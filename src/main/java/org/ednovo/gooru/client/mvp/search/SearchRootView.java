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

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BaseViewWithHandlers;
import org.ednovo.gooru.client.mvp.search.event.SwitchSearchEvent;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.search.SearchDo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Search Team
 * 
 */
public class SearchRootView extends BaseViewWithHandlers<SearchRootUiHandlers> implements IsSearchRootView {

	private static SearchRootViewUiBinder uiBinder = GWT.create(SearchRootViewUiBinder.class);
	
	private static MessageProperties i18n = GWT.create(MessageProperties.class);

	interface SearchRootViewUiBinder extends UiBinder<Widget, SearchRootView> {
	}

	public interface Style extends CssResource {
		String bodyHeight();
		String panelHeight();
		String resourceBtnActive();
		String collectionBtnActive();
		String secondaryResourceSearchBtn();
		String secondaryCollectionSearchBtn();
	}

	/*@UiField
	SearchBarVc searchBarVc;*/
	@UiField
	FlowPanel flowpanel;
	@UiField
	HTMLPanel contentpanel;
	@UiField
	SimplePanel searchWrapperSimPanel, shelfTabSimPanel;
	@UiField FlowPanel panelSearchPage;
	@UiField Anchor searchFilterMenu;

	/*@UiField
	Anchor resourceLinkLbl, collectionLinkLbl;*/
	
	@UiField
	Button resourceSearchBtn, collectionSearchBtn;
	
	@UiField
    Label lodingImage;
	
	@UiField
	Style style;
	@UiField
	SearchCBundle res;
	
	@UiField
	HTML queriedTextHtml;

	/**
	 * Class constructor
	 */
	public SearchRootView() {
		setWidget(uiBinder.createAndBindUi(this));
		res.css().ensureInjected();
		resourceSearchBtn.getElement().setId("btnResource");
		collectionSearchBtn.getElement().setId("btnCollection");
		
		resourceSearchBtn.setText(i18n.GL0174());
		collectionSearchBtn.setText(i18n.GL0175());
		queriedTextHtml.getElement().setId("htmlQueriedTextHtml");
		searchWrapperSimPanel.getElement().setId("spnlSearchWrapperSimPanel");
		shelfTabSimPanel.getElement().setId("spnlShelfTabSimPanel");
		lodingImage.getElement().setId("lblLodingImage");
		searchFilterMenu.getElement().setId("toggle-menu1");
	/*	int windowHeight=Window.getClientHeight();
		panelSearchPage.setStyleName("panelHeight");
		panelSearchPage.getElement().getStyle().setHeight(windowHeight - 50, Unit.PX);*/
	}

	@Override
	public void setInSlot(Object slot, Widget content) {
		if (content != null) {
			if (slot == SearchRootPresenter.TYPE_VIEW) {
				searchWrapperSimPanel.clear();
				searchWrapperSimPanel.add(content);
			} else if (slot == SearchRootPresenter.TYPE_SHELF_TAB) {
				shelfTabSimPanel.setWidget(content);
			}
		}
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

	@Override
	public void reset() {
		super.reset();
		queriedTextHtml.setHTML("<p></p>");
		queriedTextHtml.getElement().setAttribute("alt","<p></p>");
		queriedTextHtml.getElement().setAttribute("title","<p></p>");
	}

	@Override
	public void preSearch(SearchDo<?> searchDo) {
		lodingImage.setVisible(true);
//		searchBarVc.setSearchText(searchDo.getUrlQuery());
		
//				searchBarVc.setInitialSearchQuery();
		
		
		if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equals(PlaceTokens.RESOURCE_SEARCH)) {
			resourceSearchBtn.addStyleName(res.css().secondaryICactive());
			collectionSearchBtn.removeStyleName(res.css().secondaryIC1active());
			resourceSearchBtn.removeStyleName(res.css().secondaryIC()); 
			collectionSearchBtn.addStyleName(res.css().secondaryIC1());
		} else {
			collectionSearchBtn.addStyleName(res.css().secondaryIC1active());
			resourceSearchBtn.removeStyleName(res.css().secondaryICactive());
			collectionSearchBtn.removeStyleName(res.css().secondaryIC1());
			resourceSearchBtn.addStyleName(res.css().secondaryIC()); 
		}
	}

	@Override
	public void postSearch(SearchDo<?> searchDo) {
		// String searchText = searchBarVc.getSearchText();
		if (AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(
				PlaceTokens.RESOURCE_SEARCH)
				|| AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(
						PlaceTokens.COLLECTION_SEARCH)) {
			Document doc = Document.get();
		/*	doc.getBody().setClassName(style.bodyHeight());*/

		}
		Window.enableScrolling(false);
		int countValue = searchDo.getSearchResults().size();

		String searchText = AppClientFactory.getPlaceManager()
				.getRequestParameter("query");

		if (searchText == null) {
			searchText = "";
			lodingImage.setVisible(false);
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
			queriedTextHtml.getElement().setAttribute("alt", searchText);
			queriedTextHtml.getElement().setAttribute("title", searchText);
			lodingImage.setVisible(false);
		}
	}

	/*@Override
	public void clearPanel() {
		contentpanel.clear();
	}*/
	@UiHandler("searchFilterMenu")
	public void searchFilterMenuClickEvent(ClickEvent event){
		invokeShowHideMenuContainer();
			
	}
	
	public static native void invokeShowHideMenuContainer() /*-{
		$wnd.showSearchFilters();
	}-*/;
}