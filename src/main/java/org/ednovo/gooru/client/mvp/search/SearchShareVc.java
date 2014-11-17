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
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.socialshare.SocialShareLinksView;
import org.ednovo.gooru.client.mvp.socialshare.SocialShareView;
import org.ednovo.gooru.client.uc.PPanel;
import org.ednovo.gooru.client.util.PlayerDataLogEvents;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.content.CollectionItemDo;
import org.ednovo.gooru.shared.model.search.ResourceSearchResultDo;
import org.ednovo.gooru.shared.model.social.SocialShareDo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Search Team
 * 
 */
public class SearchShareVc extends Composite {

	private static SearchShareVcUiBinder uiBinder = GWT
			.create(SearchShareVcUiBinder.class);
	
	private static MessageProperties i18n = GWT.create(MessageProperties.class);

	interface SearchShareVcUiBinder extends UiBinder<Widget, SearchShareVc> {
	}

	private SimpleAsyncCallback<Map<String, String>> shareUrlGenerationAsyncCallback;
	
	private SimpleAsyncCallback<Map<String, String>> shareUrlWithGenerationAsyncCallback;

	/*@UiField
	FlowPanel panelMain;
	
	@UiField
	TextBox shortenUrlTxtBox;
	*/
	@UiField
	HTMLPanel socialContentPanel;
	
	/*@UiField FlowPanel embedContainer;*/
	
	@UiField FlowPanel socialShareLinksViewContainer;
	@UiField PPanel shareViaText;
	
	public SocialShareLinksView socialShareLinksView = null;

	private ResourceSearchResultDo searchResultDo;
	private CollectionItemDo collectionItemDo;
	private static final String  SHORTEN_URL= "shortenUrl";
	
	private static final String  RAW_URL= "rawUrl";
	
	String rawUrl;

	/**
	 * Class constructor, call shorten url of collection view method
	 */
	public SearchShareVc() {
		initWidget(uiBinder.createAndBindUi(this));
		socialShareLinksView = new SocialShareLinksView();
		socialShareLinksView.getshareLinkTxtBox().setReadOnly(true);
		shareViaText.setText(i18n.GL0638());
		shareViaText.getElement().setId("lblShareViaText");
		shareViaText.getElement().setAttribute("alt",i18n.GL0638());
		shareViaText.getElement().setAttribute("title",i18n.GL0638());
		socialContentPanel.getElement().setId("pnlSocialContentPanel");
		socialShareLinksViewContainer.getElement().setId("fpnlSocialShareLinksViewContainer");
		
		socialShareLinksView.getShareLbl().setText(i18n.GL0511());
		/*socialShareLinksView.getShareLinkFlwPl().getElement().getStyle().setPaddingTop(0, Unit.PX);
		socialShareLinksView.getShareLinkFlwPl().getElement().getStyle().setWidth(22, Unit.PC);
		socialShareLinksView.getShareLinkFlwPl().getElement().getStyle().setFloat(Float.LEFT);
		socialShareLinksView.getShareLinkFlwPl().getElement().getStyle().setPaddingLeft(10, Unit.PX);
		socialShareLinksView.getShareLinkContainer().getElement().getStyle().setWidth(353, Unit.PX);*/
		setShareUrlGenerationAsyncCallback(new SimpleAsyncCallback<Map<String, String>>() {
			@Override
			public void onSuccess(Map<String, String> shortenUrl) {
				socialContentPanel.clear();
				setData(shortenUrl);
			}
		});
		/*shortenUrlTxtBox.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				shortenUrlTxtBox.selectAll();
				shortenUrlTxtBox.setFocus(true);
			}
		});*/
		setShareUrlWithGenerationAsyncCallback(new SimpleAsyncCallback<Map<String,String>>() {

			@Override
			public void onSuccess(Map<String, String> result) {
				if (result != null && result.containsKey("shortenUrl")) {
					//embedContainer.add(new SearchEmbedVc(result.get(SHORTEN_URL), true));
					socialShareLinksView.setEmbedBitlyLink(result.get("decodeRawUrl"));
					rawUrl=result.get("rawUrl").toString();
				}			
			}
		});
		socialShareLinksViewContainer.add(socialShareLinksView);
	}
	

	/**
	 * Assign {@link ResourceSearchResultDo} instance
	 * 
	 * @param searchResultDo
	 *            instance of {@link ResourceSearchResultDo}
	 */
	public void setData(ResourceSearchResultDo searchResultDo) {
		this.searchResultDo = searchResultDo;
	}
	
	/**
	 * 
	 */
	public void onReveal() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", AppClientFactory.getPlaceManager()
				.getCurrentPlaceRequest().getNameToken());
		params.put("shareType", "share");
		if (socialShareLinksView.getshareLinkTxtBox().getText().length() < 4) {
				AppClientFactory.getInjector().getSearchService().getShortenShareUrl(searchResultDo.getGooruOid(), params, getShareShortenUrlAsyncCallback());
		}
		
		if (!AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equals(PlaceTokens.PROFILE_PAGE)){
			Map<String, String> paramsEmbed = new HashMap<String, String>();
			paramsEmbed.put("type", AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken());
			paramsEmbed.put("shareType", "embed");
			if (socialShareLinksView.getshareLinkTxtBox().getText().length() < 4) {
				AppClientFactory.getInjector().getSearchService().getShortenShareUrl(searchResultDo.getGooruOid(), paramsEmbed, getShareUrlWithGenerationAsyncCallback());
			}
		}
	}

	/**
	 * Set shorten url of the collection view url
	 * 
	 * @param shortenUrl
	 *            to be set in shorten url UI field
	 */
	private void setData(Map<String, String> shortenUrl) {
		if (shortenUrl != null && shortenUrl.containsKey(SHORTEN_URL)) {
			//shortenUrlTxtBox.setText(shortenUrl.get(SHORTEN_URL));
			socialShareLinksView.setData(shortenUrl);
			rawUrl=shortenUrl.get("rawUrl").toString();
		}
		addSocialResource();
		if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.PROFILE_PAGE)){
			Map<String, String> paramsEmbed = new HashMap<String, String>();
			paramsEmbed.put("type", AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken());
			paramsEmbed.put("shareType", "embed");
			AppClientFactory.getInjector().getSearchService().getShortenShareUrl(searchResultDo.getGooruOid(), paramsEmbed, getShareUrlWithGenerationAsyncCallback());
//			panelMain.getElement().getStyle().setMarginLeft(-2, Unit.PX);
		}else{
			//panelMain.getElement().getStyle().setMarginLeft(15, Unit.PX);
		}
	}

	private void addSocialResource() {
		SocialShareDo shareDo = new SocialShareDo();
		shareDo.setBitlylink(rawUrl);
		shareDo.setRawUrl(rawUrl);
		shareDo.setTitle(searchResultDo.getResourceTitle());
		shareDo.setDescription(searchResultDo.getDescription());
		shareDo.setThumbnailurl(searchResultDo.getUrl());
		shareDo.setCategoryType(searchResultDo.getCategory());
		shareDo.setIsSearchShare(true);
		if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equals(PlaceTokens.PROFILE_PAGE)){
			shareDo.setOnlyIcon(false);
		}else{
			shareDo.setOnlyIcon(true);
		}
		shareDo.setDecodeRawUrl(socialShareLinksView.getshareLinkTxtBox().getText());
		shareDo.setOnlyIcon(false);
		shareDo.setShareType("public");
		SocialShareView socialView = new SocialShareView(shareDo){
			public void triggerShareDataEvent(String shareType,boolean confirmStaus){
				if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equals(PlaceTokens.RESOURCE_SEARCH)){
					PlayerDataLogEvents.triggerItemShareDataLogEvent(searchResultDo.getGooruOid(), "", "", "", "", PlayerDataLogEvents.RESOURCE, shareType, confirmStaus, "", searchResultDo.getGooruOid(), AppClientFactory.getPlaceManager().getSeachEventPageLocation());
				}else if(AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equals(PlaceTokens.COLLECTION_SEARCH)){
					PlayerDataLogEvents.triggerItemShareDataLogEvent(searchResultDo.getGooruOid(), "", "", "", "", PlayerDataLogEvents.COLLECTION, shareType, confirmStaus, "", searchResultDo.getGooruOid(), AppClientFactory.getPlaceManager().getSeachEventPageLocation());
				}
			}
		};
		socialContentPanel.add(socialView);

	}

	public void setShareUrlGenerationAsyncCallback(
			SimpleAsyncCallback<Map<String, String>> shareShortenUrlAsyncCallback) {
		this.shareUrlGenerationAsyncCallback = shareShortenUrlAsyncCallback;
	}

	public SimpleAsyncCallback<Map<String, String>> getShareShortenUrlAsyncCallback() {
		return shareUrlGenerationAsyncCallback;
	}
	/** 
	 * This method is to get the shareUrlWithGenerationAsyncCallback
	 */
	public SimpleAsyncCallback<Map<String, String>> getShareUrlWithGenerationAsyncCallback() {
		return shareUrlWithGenerationAsyncCallback;
	}
	/** 
	 * This method is to set the shareUrlWithGenerationAsyncCallback
	 */
	public void setShareUrlWithGenerationAsyncCallback(
			SimpleAsyncCallback<Map<String, String>> shareUrlWithGenerationAsyncCallback) {
		this.shareUrlWithGenerationAsyncCallback = shareUrlWithGenerationAsyncCallback;
	}

	public void setData(CollectionItemDo collectionItemDo) {
		this.collectionItemDo =collectionItemDo;
	}
}
