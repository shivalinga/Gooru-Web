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
import org.ednovo.gooru.client.service.PlayerAppServiceAsync;
import org.ednovo.gooru.client.uc.PlayerBundle;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.content.CollectionItemDo;
import org.ednovo.gooru.shared.model.search.ResourceSearchResultDo;
import org.ednovo.gooru.shared.model.social.SocialShareDo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Search Team
 * 
 */
public class SearchInfoTabVc extends Composite {

	private static SearchShareVcUiBinder uiBinder = GWT
			.create(SearchShareVcUiBinder.class);
	
	private MessageProperties i18n = GWT.create(MessageProperties.class);

	private PlayerAppServiceAsync playerAppService;

	interface SearchShareVcUiBinder extends UiBinder<Widget, SearchInfoTabVc> {
	}

	private SimpleAsyncCallback<Map<String, String>> shareUrlGenerationAsyncCallback;
	
	private SimpleAsyncCallback<Map<String, String>> shareUrlWithGenerationAsyncCallback;

	/*@UiField
	FlowPanel panelMain;
	
	@UiField
	TextBox shortenUrlTxtBox;
	*/

	
	/*@UiField FlowPanel embedContainer;*/
	
	@UiField HTML lblResourceName;
	
	@UiField Label resourceTypeImage;
	

	
	public SocialShareLinksView socialShareLinksView = null;

	private ResourceSearchResultDo searchResultDo;
	private CollectionItemDo collectionItemDo;
	private static final String  SHORTEN_URL= "shortenUrl";
	
	private static final String  RAW_URL= "rawUrl";
	
	String rawUrl;

	/**
	 * Class constructor, call shorten url of collection view method
	 */
	public SearchInfoTabVc() {
		initWidget(uiBinder.createAndBindUi(this));
		socialShareLinksView = new SocialShareLinksView();
		socialShareLinksView.getshareLinkTxtBox().setReadOnly(true);
		//shareViaText.setText(GL0638);
		socialShareLinksView.getShareLbl().setText(i18n.GL0511());
	/*	socialShareLinksView.getShareLinkFlwPl().getElement().getStyle().setPaddingTop(0, Unit.PX);
		socialShareLinksView.getShareLinkFlwPl().getElement().getStyle().setWidth(22, Unit.PC);
		socialShareLinksView.getShareLinkFlwPl().getElement().getStyle().setFloat(Float.LEFT);
		socialShareLinksView.getShareLinkFlwPl().getElement().getStyle().setPaddingLeft(10, Unit.PX);
		socialShareLinksView.getShareLinkContainer().getElement().getStyle().setWidth(353, Unit.PX);*/
		setShareUrlGenerationAsyncCallback(new SimpleAsyncCallback<Map<String, String>>() {
			@Override
			public void onSuccess(Map<String, String> shortenUrl) {
				//socialContentPanel.clear();
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
		//socialShareLinksViewContainer.add(socialShareLinksView);
		
		resourceTypeImage.getElement().setId("lblResourceTypeImage");
		lblResourceName.getElement().setId("htmlLblResourceName");
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
		
		AppClientFactory.getInjector().getPlayerAppService().getResourceObj(searchResultDo.getGooruOid(),new SimpleAsyncCallback<CollectionItemDo>() {
			@Override
			public void onSuccess(CollectionItemDo collectionItemDo) 
			{
				if(collectionItemDo.getResource().getResourceFormat()!=null){
					setResourceTypeImage(collectionItemDo.getResource().getResourceFormat().getDisplayName());
				}
				if(collectionItemDo.getResource().getTitle()!=null){
					lblResourceName.setHTML(removeHtmlTags(collectionItemDo.getResource().getTitle()));
					lblResourceName.getElement().setAttribute("alt",removeHtmlTags(collectionItemDo.getResource().getTitle()));
					lblResourceName.getElement().setAttribute("title",removeHtmlTags(collectionItemDo.getResource().getTitle()));
				}
				
			/*	if(collectionItemDo.getStatusCode() != 200){
					showResourceErrorMessage();
				}else{
					setPageTitle(collectionItemDo);
					showResoruceView(collectionItemDo,resourceId,tabView);
				}*/
			}
		});
/*		if (socialShareLinksView.getshareLinkTxtBox().getText().length() < 4) {
				AppClientFactory.getInjector().getSearchService().getShortenShareUrl(searchResultDo.getGooruOid(), params, getShareShortenUrlAsyncCallback());
		}
		
		if (!AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equals(PlaceTokens.PROFILE_PAGE)){
			Map<String, String> paramsEmbed = new HashMap<String, String>();
			paramsEmbed.put("type", AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken());
			paramsEmbed.put("shareType", "embed");
			if (socialShareLinksView.getshareLinkTxtBox().getText().length() < 4) {
				AppClientFactory.getInjector().getSearchService().getShortenShareUrl(searchResultDo.getGooruOid(), paramsEmbed, getShareUrlWithGenerationAsyncCallback());
			}
		}*/
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
		SocialShareView socialView = new SocialShareView(shareDo);
		
		//socialContentPanel.add(socialView);

	}
	
	private String removeHtmlTags(String html){
        html = html.replaceAll("</p>", " ").replaceAll("<p>", "").replaceAll("<br data-mce-bogus=\"1\">", "").replaceAll("<br>", "").replaceAll("</br>", "");
        return html;
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


	public PlayerAppServiceAsync getPlayerAppService() {
		return playerAppService;
	}


	public void setPlayerAppService(PlayerAppServiceAsync playerAppService) {
		this.playerAppService = playerAppService;
	}
	
	public void setResourceTypeImage(String resourceType){
		if(resourceType!=null){
			resourceType=resourceType.toLowerCase();
			if(resourceType.equalsIgnoreCase("lesson")||resourceType.equalsIgnoreCase("textbook")||resourceType.equalsIgnoreCase("handout"))
			{
				resourceType=resourceType.replaceAll("lesson", "Text").replaceAll("textbook", "Text").replaceAll("handout", "Text");
			}
			if(resourceType.equalsIgnoreCase("slide"))
			{
				resourceType=resourceType.replaceAll("slide","Image");
			}
			if(resourceType.equalsIgnoreCase("exam")||resourceType.equalsIgnoreCase("challenge")||resourceType.equalsIgnoreCase("website"))
			{
				resourceType=resourceType.replaceAll("exam","Webpage").replaceAll("challenge", "Webpage").replaceAll("website", "Webpage");
			}
			//lblresourceType.setText(resourceType);
			resourceTypeImage.setStyleName(getResourceTypeImage(resourceType));
		}
	}
	
	public String getResourceTypeImage(String resourceType){
		if(resourceType.equalsIgnoreCase("Video")||resourceType.equalsIgnoreCase("Videos")){
			return PlayerBundle.INSTANCE.getPlayerStyle().videoResourceTypeInfo();
		}else if(resourceType.equalsIgnoreCase("Interactive")){
			return PlayerBundle.INSTANCE.getPlayerStyle().interactiveResourceTypeInfo();
		}
		else if(resourceType.equalsIgnoreCase("Website")||resourceType.equalsIgnoreCase("Webpage")){
			return PlayerBundle.INSTANCE.getPlayerStyle().websiteResourceTypeInfo();		
		}
		else if(resourceType.equalsIgnoreCase("Slide")){
			return PlayerBundle.INSTANCE.getPlayerStyle().imageResourceTypeInfo();
		}
		else if(resourceType.equalsIgnoreCase("Textbook")){
			return PlayerBundle.INSTANCE.getPlayerStyle().textResourceTypeInfo();
		}
		else if(resourceType.equalsIgnoreCase("Question")){
			return PlayerBundle.INSTANCE.getPlayerStyle().questionResourceTypeInfo();
		}
		else if(resourceType.equalsIgnoreCase("lesson")){
			return PlayerBundle.INSTANCE.getPlayerStyle().lessonResourceTypeInfo();
			
		}else if(resourceType.equalsIgnoreCase("Handout")){
			return PlayerBundle.INSTANCE.getPlayerStyle().textResourceTypeInfo();
		} else if(resourceType.equalsIgnoreCase("text")){
			return PlayerBundle.INSTANCE.getPlayerStyle().textResourceTypeInfo();
		}
		else if(resourceType.equalsIgnoreCase("image")){
			return PlayerBundle.INSTANCE.getPlayerStyle().imageResourceTypeInfo();
		}
		else if(resourceType.equalsIgnoreCase("audio")){
			return PlayerBundle.INSTANCE.getPlayerStyle().audioResourceTypeInfo();
		}else if(resourceType.equalsIgnoreCase("exam")){
			return PlayerBundle.INSTANCE.getPlayerStyle().websiteResourceTypeInfo();
		}
		else {
			return PlayerBundle.INSTANCE.getPlayerStyle().otherResourceTypeInfo();
		}
	}
	
	
}
