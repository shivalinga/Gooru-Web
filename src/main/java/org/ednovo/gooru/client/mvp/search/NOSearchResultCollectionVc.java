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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.uc.HTMLEventPanel;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.featured.FeaturedCollectionContentDo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @fileName : NOSearchResultCollectionVc.java
 * 
 * @description :
 * 
 * 
 * @version : 5.9
 * 
 * @date: Sep 25, 2013
 * 
 * @Author Gooru Team
 * 
 * @Reviewer:
 */
public class NOSearchResultCollectionVc extends Composite {

	private static NOSearchResultCollectionVcUiBinder uiBinder = GWT
			.create(NOSearchResultCollectionVcUiBinder.class);

	MessageProperties i18n = GWT.create(MessageProperties.class);
	
	interface NOSearchResultCollectionVcUiBinder extends
			UiBinder<Widget, NOSearchResultCollectionVc> {
	}
	
	@UiField Label suggestedCollectionCountLbl,didnotFindText,orText,tryTipsText,removeFiltersText,checkSpellingText,differentKeywordText,changeToggleText,suggestedCollectionsText;
	
	@UiField HTMLPanel suggestedCollectionContainer;
	
	@UiField Button btnLibrary;
	
	@UiField NoSearchResultBundle noResultStyle;
	int totalItem=0;
	
	private final String FEATURED_TAB = "featured";
	
	private String DEFAULT_COLLECTION_IMAGE = "images/default-collection-image-160x120.png";
	
	public NOSearchResultCollectionVc() {
		initWidget(uiBinder.createAndBindUi(this));
		suggestedCollectionContainer.setVisible(false);
		btnLibrary.setText(i18n.GL0506());
		btnLibrary.getElement().setId("btnLibrary");
		btnLibrary.getElement().setAttribute("alt",i18n.GL0506());
		btnLibrary.getElement().setAttribute("title",i18n.GL0506());
		
		/*btnLibrary.getElement().getStyle().setMarginLeft(180, Unit.PX);*/
		didnotFindText.setText(i18n.GL0704());
		didnotFindText.getElement().setId("lblDidnotFindText");
		didnotFindText.getElement().setAttribute("alt",i18n.GL0704());
		didnotFindText.getElement().setAttribute("title",i18n.GL0704());
		
		orText.setText(i18n.GL0209().toUpperCase());
		orText.getElement().setId("lblOrText");
		orText.getElement().setAttribute("alt",i18n.GL0209().toUpperCase());
		orText.getElement().setAttribute("title",i18n.GL0209().toUpperCase());
		
		tryTipsText.setText(i18n.GL0705());
		tryTipsText.getElement().setId("lblTryTipsText");
		tryTipsText.getElement().setAttribute("alt",i18n.GL0705());
		tryTipsText.getElement().setAttribute("title",i18n.GL0705());
		
		removeFiltersText.setText(i18n.GL0706());
		removeFiltersText.getElement().setId("lblRemoveFiltersText");
		removeFiltersText.getElement().setAttribute("alt",i18n.GL0706());
		removeFiltersText.getElement().setAttribute("title",i18n.GL0706());
		
		checkSpellingText.setText(i18n.GL0707());
		checkSpellingText.getElement().setId("lblCheckSpellingText");
		checkSpellingText.getElement().setAttribute("alt",i18n.GL0707());
		checkSpellingText.getElement().setAttribute("title",i18n.GL0707());
		
		differentKeywordText.setText(i18n.GL0708());
		differentKeywordText.getElement().setId("lblDifferentKeywordText");
		differentKeywordText.getElement().setAttribute("alt",i18n.GL0708());
		differentKeywordText.getElement().setAttribute("title",i18n.GL0708());
		
		changeToggleText.setText(i18n.GL0709());
		changeToggleText.getElement().setId("lblChangeToggleText");
		changeToggleText.getElement().setAttribute("alt",i18n.GL0709());
		changeToggleText.getElement().setAttribute("title",i18n.GL0709());
		
		suggestedCollectionsText.setText(i18n.GL0710());
		suggestedCollectionsText.getElement().setId("lblSuggestedCollectionsText");
		suggestedCollectionsText.getElement().setAttribute("alt",i18n.GL0710());
		suggestedCollectionsText.getElement().setAttribute("title",i18n.GL0710());
		
		suggestedCollectionContainer.getElement().setId("pnlSuggestedCollectionContainer");
		suggestedCollectionCountLbl.getElement().setId("lblSuggestedCollectionCountLbl");
		//getCollectionData();
	}
	
	public void setData(List<FeaturedCollectionContentDo> searchDo)
	{
		for(int i=0;i<searchDo.size();i++) 
		{
			if(i<searchDo.size()){
				for(int j=0;j<searchDo.get(i).getScollections().size();j++){
				totalItem=totalItem+searchDo.get(i).getScollections().size();
				if(totalItem<5){
				HTMLPanel collectionContainer=new HTMLPanel("");
				collectionContainer.setStyleName(noResultStyle.collectionContainer());
				HTMLEventPanel collectionThambnailBlueBar=new HTMLEventPanel("");
				collectionThambnailBlueBar.setStyleName(noResultStyle.collectionThambnailBlueBar());
				final HTMLPanel collectionThumbnail=new HTMLPanel("");
				collectionThumbnail.getElement().setId(searchDo.get(i).getScollections().get(j).getGooruOid());
				collectionThumbnail.setStyleName(noResultStyle.collectionThumbnail());
				final Image collectionImage=new Image();
				collectionImage.setHeight("80px");
				collectionImage.setWidth("105px");
				collectionImage.setUrl(searchDo.get(i).getScollections().get(j).getThumbnails().getUrl()); 
				HTML titleText=new HTML();
				titleText.setStyleName(noResultStyle.collectionTitle());
				final String collectionTitle=searchDo.get(i).getScollections().get(j).getTitle();
				collectionImage.setAltText(collectionTitle);
				collectionImage.setTitle(collectionTitle);
				
				if(collectionTitle.length()>=32){
					titleText.setHTML(collectionTitle.substring(0, 25)+ "...");	
				} else {
					titleText.setHTML(collectionTitle);
				}
				collectionThumbnail.add(collectionImage);
				collectionImage.addErrorHandler(new ErrorHandler() {
					@Override
					public void onError(ErrorEvent event) {
						collectionImage.setUrl(DEFAULT_COLLECTION_IMAGE);
						collectionImage.setHeight("80px");
						collectionImage.setWidth("105px");
						collectionImage.setAltText(collectionTitle);
						collectionImage.setTitle(collectionTitle);
					}
				});
				collectionThambnailBlueBar.add(collectionThumbnail);
				collectionContainer.add(collectionThambnailBlueBar);
				collectionContainer.add(titleText);
				suggestedCollectionContainer.add(collectionContainer);
				
				collectionThambnailBlueBar.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						MixpanelUtil.Search_No_Results_Click_Collection();
						Map<String, String> params = new HashMap<String, String>();
						params.put("id",collectionThumbnail.getElement().getId());
						com.google.gwt.user.client.Window.scrollTo(0, 0);
						AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.COLLECTION_PLAY, params);
					}
				});
			}
		}
		}
		}
	}
	
	
	@UiHandler("btnLibrary")
	public void onClickOnLibrary(ClickEvent clickEvent){
		MixpanelUtil.mixpanelEvent("Search_noresults_browse_library");
		AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.HOME);
	}

	public void getCollectionData()
	{
		
		AppClientFactory.getInjector().getHomeService().getFeaturedThemeCollection(FEATURED_TAB, new SimpleAsyncCallback<List<FeaturedCollectionContentDo>>() {
			@Override
			public void onSuccess(List<FeaturedCollectionContentDo> result) {
				setData(result);
			}
		});

/*		
		SearchDo<CollectionSearchResultDo> searchInput=new SearchDo<CollectionSearchResultDo>();
		AppClientFactory.getInjector().getSearchService().getSuggestedSearchResultForCollectionNoResult(searchInput, new AsyncCallback<SearchDo<CollectionSearchResultDo>>() {
			@Override
			public void onSuccess(SearchDo<CollectionSearchResultDo> result) {
				setData(result);
			}
			@Override
			public void onFailure(Throwable caught) {
			}
		});
*/	}
	
}
