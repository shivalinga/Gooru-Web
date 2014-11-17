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
package org.ednovo.gooru.client.mvp.socialshare;

import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.uc.PPanel;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class SocialShareLinksView extends Composite {

	@UiField
	TextArea shareLinkTxtBox;
	
	@UiField
	FlowPanel shareLinkFlwPl;

	@UiField Anchor swithUrlLbl, swithToEmbedLbl;
	@UiField PPanel  shareLbl,shareLinkContainer;
	
	static MessageProperties i18n = GWT.create(MessageProperties.class);
	
	private static final String SWITCH_FULL_URL = i18n.GL0643();
	
	private static final String SWITCH_EMBED_CODE = i18n.GL0640();

	private static final String SWITCH_BITLY =i18n.GL0639();
	
	private static final String SWITCH_URL_LABEL = i18n.GL0641();

	private static final String SWITCH_TO_EMBED_LABEL = i18n.GL0642();

	private String bitlyLink, decodeRawUrl, embedBitlyLink;
	
	private boolean isPrivate = false;

	private static SocialShareLinksViewUiBinder uiBinder = GWT
			.create(SocialShareLinksViewUiBinder.class);

	interface SocialShareLinksViewUiBinder extends
			UiBinder<Widget, SocialShareLinksView> {
	}

	public SocialShareLinksView() {
		initWidget(uiBinder.createAndBindUi(this));
		shareLinkFlwPl.getElement().getStyle().clearPaddingTop();
		shareLinkFlwPl.getElement().setId("fpnlShareLinkFlwPl");
		shareLinkContainer.getElement().setId("fpnlShareLinkContainer");
		shareLinkTxtBox.addClickHandler(new OnTextBoxClick());
		shareLinkTxtBox.getElement().setId("tatShareLink");
		StringUtil.setAttributes(shareLinkTxtBox, true);
		swithUrlLbl.setText(i18n.GL0639());
		swithUrlLbl.getElement().setAttribute("alt", i18n.GL0639());
		swithUrlLbl.getElement().setAttribute("title", i18n.GL0639());
		swithToEmbedLbl.setText(i18n.GL0640());
		swithToEmbedLbl.getElement().setAttribute("alt", i18n.GL0640());
		swithToEmbedLbl.getElement().setAttribute("title", i18n.GL0640());
		swithUrlLbl.getElement().setId("lblSwithUrl");
		swithToEmbedLbl.getElement().setId("lblSwithToEmbedLbl");
		shareLbl.setText(i18n.GL0512());
		shareLbl.getElement().setId("lblShareLbl");
		shareLbl.getElement().setAttribute("alt", i18n.GL0512());
		shareLbl.getElement().setAttribute("title", i18n.GL0512());
	}

	public void setData(Map<String, String> shortenUrl) {
		if (shortenUrl != null && shortenUrl.containsKey("shortenUrl")) {
			//shareLinkTxtBox.setText(shortenUrl.get("shortenUrl"));
			//bitlyLink=shareLinkTxtBox.getText();
			shareLinkTxtBox.setText(shortenUrl.get("decodeRawUrl").toString());
			shareLinkTxtBox.getElement().setAttribute("alt", shortenUrl.get("decodeRawUrl").toString());
			shareLinkTxtBox.getElement().setAttribute("title", shortenUrl.get("decodeRawUrl").toString());

			bitlyLink=shortenUrl.get("shortenUrl");
			
		}
		decodeRawUrl=shortenUrl.get("decodeRawUrl").toString();
		
	}
	
	/**
	 * @author Search Team Selected shareLink textBox data.
	 */
	public class OnTextBoxClick implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			shareLinkTxtBox.selectAll();
			shareLinkTxtBox.setFocus(true);
		}
	}

	public TextArea getshareLinkTxtBox() {
		return shareLinkTxtBox;
	}

	public PPanel getShareLbl() {
		return shareLbl;
	}

	public FlowPanel getShareLinkFlwPl() {
		return shareLinkFlwPl;
	}
	
	public PPanel getShareLinkContainer() {
		return shareLinkContainer;
	}

	/**
	 * Switching between Url and Bitly link
	 * @param clickEvent instance of {@link ClickEvent}
	 */
	@UiHandler("swithUrlLbl")
	public void onClickSwithUrl(ClickEvent clickevent){
		if(!getIsPrivate()) {
			changeShareUrlEvents(SWITCH_BITLY);
		}
	}
	
	/**
	 * Switching between Url and Bitly link
	 * @param clickEvent instance of {@link ClickEvent}
	 */
	@UiHandler("swithToEmbedLbl")
	public void onClickSwithToEmbed(ClickEvent clickevent){
		if(!getIsPrivate()) {
			changeShareUrlEvents(SWITCH_TO_EMBED_LABEL);
		}
	}
	
	private void changeShareUrlEvents(String buttonType) {
		if(swithToEmbedLbl.getText().equalsIgnoreCase(SWITCH_BITLY)&&swithUrlLbl.getText().equalsIgnoreCase(SWITCH_EMBED_CODE)){
			if(buttonType.equalsIgnoreCase(SWITCH_TO_EMBED_LABEL)) {
				shareLinkTxtBox.setText(bitlyLink);
				shareLinkTxtBox.getElement().setAttribute("alt", bitlyLink);
				shareLinkTxtBox.getElement().setAttribute("title", bitlyLink);

				swithUrlLbl.setText(SWITCH_EMBED_CODE);
				swithUrlLbl.getElement().setAttribute("alt", SWITCH_EMBED_CODE);
				swithUrlLbl.getElement().setAttribute("title", SWITCH_EMBED_CODE);
				swithToEmbedLbl.setText(SWITCH_FULL_URL);
				swithToEmbedLbl.getElement().setAttribute("alt", SWITCH_FULL_URL);
				swithToEmbedLbl.getElement().setAttribute("title", SWITCH_FULL_URL);
			} else {
				shareLinkTxtBox.setText(getIframeText());
				shareLinkTxtBox.getElement().setAttribute("alt", getIframeText());
				shareLinkTxtBox.getElement().setAttribute("title", getIframeText());
				swithUrlLbl.setText(SWITCH_FULL_URL);
				swithUrlLbl.getElement().setAttribute("alt", SWITCH_FULL_URL);
				swithUrlLbl.getElement().setAttribute("title", SWITCH_FULL_URL);
				swithToEmbedLbl.setText(SWITCH_BITLY);
				swithToEmbedLbl.getElement().setAttribute("alt", SWITCH_BITLY);
				swithToEmbedLbl.getElement().setAttribute("title", SWITCH_BITLY);
			}
		}else if(swithToEmbedLbl.getText().equalsIgnoreCase(SWITCH_EMBED_CODE)&&swithUrlLbl.getText().equalsIgnoreCase(SWITCH_BITLY)){
			if(buttonType.equalsIgnoreCase(SWITCH_TO_EMBED_LABEL)) {
				shareLinkTxtBox.setText(getIframeText());
				shareLinkTxtBox.getElement().setAttribute("alt", getIframeText());
				shareLinkTxtBox.getElement().setAttribute("title", getIframeText());
				swithUrlLbl.setText(SWITCH_BITLY);
				swithUrlLbl.getElement().setAttribute("alt", SWITCH_BITLY);
				swithUrlLbl.getElement().setAttribute("title", SWITCH_BITLY);
				swithToEmbedLbl.setText(SWITCH_FULL_URL);
				swithToEmbedLbl.getElement().setAttribute("alt", SWITCH_FULL_URL);
				swithToEmbedLbl.getElement().setAttribute("title", SWITCH_FULL_URL);
			} else {
				shareLinkTxtBox.setText(bitlyLink);
				shareLinkTxtBox.getElement().setAttribute("alt", bitlyLink);
				shareLinkTxtBox.getElement().setAttribute("title", bitlyLink);
				swithUrlLbl.setText(SWITCH_FULL_URL);
				swithUrlLbl.getElement().setAttribute("alt", SWITCH_FULL_URL);
				swithUrlLbl.getElement().setAttribute("title", SWITCH_FULL_URL);
				swithToEmbedLbl.setText(SWITCH_EMBED_CODE);
				swithToEmbedLbl.getElement().setAttribute("alt", SWITCH_EMBED_CODE);
				swithToEmbedLbl.getElement().setAttribute("title", SWITCH_EMBED_CODE);
			}
		}else if(swithToEmbedLbl.getText().equalsIgnoreCase(SWITCH_FULL_URL)&&swithUrlLbl.getText().equalsIgnoreCase(SWITCH_EMBED_CODE)){
			if(buttonType.equalsIgnoreCase(SWITCH_TO_EMBED_LABEL)) {
				shareLinkTxtBox.setText(decodeRawUrl);
				shareLinkTxtBox.getElement().setAttribute("alt", decodeRawUrl);
				shareLinkTxtBox.getElement().setAttribute("title", decodeRawUrl);
				fullUrlMixPanelEvent();
				swithUrlLbl.setText(SWITCH_EMBED_CODE);
				swithUrlLbl.getElement().setAttribute("alt", SWITCH_EMBED_CODE);
				swithUrlLbl.getElement().setAttribute("title", SWITCH_EMBED_CODE);
				swithToEmbedLbl.setText(SWITCH_BITLY);
				swithToEmbedLbl.getElement().setAttribute("alt", SWITCH_BITLY);
				swithToEmbedLbl.getElement().setAttribute("title", SWITCH_BITLY);
			} else {
				shareLinkTxtBox.setText(getIframeText());
				shareLinkTxtBox.getElement().setAttribute("alt", getIframeText());
				shareLinkTxtBox.getElement().setAttribute("title", getIframeText());
				swithUrlLbl.setText(SWITCH_BITLY);
				swithUrlLbl.getElement().setAttribute("alt", SWITCH_BITLY);
				swithUrlLbl.getElement().setAttribute("title", SWITCH_BITLY);
				swithToEmbedLbl.setText(SWITCH_FULL_URL);
				swithToEmbedLbl.getElement().setAttribute("alt", SWITCH_FULL_URL);
				swithToEmbedLbl.getElement().setAttribute("title", SWITCH_FULL_URL);
			}
		}else if(swithToEmbedLbl.getText().equalsIgnoreCase(SWITCH_EMBED_CODE)&&swithUrlLbl.getText().equalsIgnoreCase(SWITCH_FULL_URL)){
			if(buttonType.equalsIgnoreCase(SWITCH_TO_EMBED_LABEL)) {
				shareLinkTxtBox.setText(getIframeText());
				shareLinkTxtBox.getElement().setAttribute("alt", getIframeText());
				shareLinkTxtBox.getElement().setAttribute("title", getIframeText());
				swithUrlLbl.setText(SWITCH_FULL_URL);
				swithUrlLbl.getElement().setAttribute("alt", SWITCH_FULL_URL);
				swithUrlLbl.getElement().setAttribute("title", SWITCH_FULL_URL);
				swithToEmbedLbl.setText(SWITCH_BITLY);
				swithToEmbedLbl.getElement().setAttribute("alt", SWITCH_BITLY);
				swithToEmbedLbl.getElement().setAttribute("title", SWITCH_BITLY);
			} else {
				shareLinkTxtBox.setText(decodeRawUrl);
				shareLinkTxtBox.getElement().setAttribute("alt", decodeRawUrl);
				shareLinkTxtBox.getElement().setAttribute("title", decodeRawUrl);
				fullUrlMixPanelEvent();
				swithUrlLbl.setText(SWITCH_BITLY);
				swithUrlLbl.getElement().setAttribute("alt", SWITCH_BITLY);
				swithUrlLbl.getElement().setAttribute("title", SWITCH_BITLY);
				swithToEmbedLbl.setText(SWITCH_EMBED_CODE);
				swithToEmbedLbl.getElement().setAttribute("alt", SWITCH_EMBED_CODE);
				swithToEmbedLbl.getElement().setAttribute("title", SWITCH_EMBED_CODE);
			}
		}else if(swithToEmbedLbl.getText().equalsIgnoreCase(SWITCH_BITLY)&&swithUrlLbl.getText().equalsIgnoreCase(SWITCH_FULL_URL)){
			if(buttonType.equalsIgnoreCase(SWITCH_TO_EMBED_LABEL)) {
				shareLinkTxtBox.setText(bitlyLink);
				shareLinkTxtBox.getElement().setAttribute("alt", bitlyLink);
				shareLinkTxtBox.getElement().setAttribute("title", bitlyLink);
				swithUrlLbl.setText(SWITCH_FULL_URL);
				swithUrlLbl.getElement().setAttribute("alt", SWITCH_FULL_URL);
				swithUrlLbl.getElement().setAttribute("title", SWITCH_FULL_URL);
				swithToEmbedLbl.setText(SWITCH_EMBED_CODE);
				swithToEmbedLbl.getElement().setAttribute("alt", SWITCH_EMBED_CODE);
				swithToEmbedLbl.getElement().setAttribute("title", SWITCH_EMBED_CODE);
			} else {
				shareLinkTxtBox.setText(decodeRawUrl);
				shareLinkTxtBox.getElement().setAttribute("alt", decodeRawUrl);
				shareLinkTxtBox.getElement().setAttribute("title", decodeRawUrl);
				fullUrlMixPanelEvent();
				swithUrlLbl.setText(SWITCH_EMBED_CODE);
				swithUrlLbl.getElement().setAttribute("alt", SWITCH_EMBED_CODE);
				swithUrlLbl.getElement().setAttribute("title", SWITCH_EMBED_CODE);
				swithToEmbedLbl.setText(SWITCH_BITLY);
				swithToEmbedLbl.getElement().setAttribute("alt", SWITCH_BITLY);
				swithToEmbedLbl.getElement().setAttribute("title", SWITCH_BITLY);
			}
		}else if(swithToEmbedLbl.getText().equalsIgnoreCase(SWITCH_FULL_URL)&&swithUrlLbl.getText().equalsIgnoreCase(SWITCH_BITLY)){
			if(buttonType.equalsIgnoreCase(SWITCH_TO_EMBED_LABEL)) {
				shareLinkTxtBox.setText(decodeRawUrl);
				shareLinkTxtBox.getElement().setAttribute("alt", decodeRawUrl);
				shareLinkTxtBox.getElement().setAttribute("title", decodeRawUrl);
				fullUrlMixPanelEvent();
				swithUrlLbl.setText(SWITCH_BITLY);
				swithUrlLbl.getElement().setAttribute("alt", SWITCH_BITLY);
				swithUrlLbl.getElement().setAttribute("title", SWITCH_BITLY);
				swithToEmbedLbl.setText(SWITCH_EMBED_CODE);
				swithToEmbedLbl.getElement().setAttribute("alt", SWITCH_EMBED_CODE);
				swithToEmbedLbl.getElement().setAttribute("title", SWITCH_EMBED_CODE);
			} else {
				shareLinkTxtBox.setText(bitlyLink);
				shareLinkTxtBox.getElement().setAttribute("alt", bitlyLink);
				shareLinkTxtBox.getElement().setAttribute("title", bitlyLink);
				swithUrlLbl.setText(SWITCH_EMBED_CODE);
				swithUrlLbl.getElement().setAttribute("alt", SWITCH_EMBED_CODE);
				swithUrlLbl.getElement().setAttribute("title", SWITCH_EMBED_CODE);
				swithToEmbedLbl.setText(SWITCH_FULL_URL);
				swithToEmbedLbl.getElement().setAttribute("alt", SWITCH_FULL_URL);
				swithToEmbedLbl.getElement().setAttribute("title", SWITCH_FULL_URL);
			}
		}
	}

	private String getIframeText() {
		String iframeText;
		if(embedBitlyLink==null){
		embedBitlyLink=shareLinkTxtBox.getText();
		}
		if(AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equals(PlaceTokens.COLLECTION_SEARCH)||AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equals(PlaceTokens.SHELF) ||AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equals(PlaceTokens.PROFILE_PAGE)){
			 iframeText = "<iframe width=\"500px\" height=\"208px\" src=\"" + embedBitlyLink + "\" frameborder=\"0\" ></iframe>";
		}else{
			 iframeText = "<iframe width=\"1024px\" height=\"768px\" src=\"" + embedBitlyLink + "\" frameborder=\"0\" ></iframe>";
		}
		return iframeText;
	}
	
	public void setIsPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
		setSwitchButtonStyles();
	}
	
	private boolean getIsPrivate() {
		return isPrivate;
	}
	
	public void setSwitchButtonStyles() {
		if(getIsPrivate()) {
			swithToEmbedLbl.getElement().getStyle().setCursor(Cursor.DEFAULT);
			swithUrlLbl.getElement().getStyle().setCursor(Cursor.DEFAULT);
		} else {
			swithToEmbedLbl.getElement().getStyle().setCursor(Cursor.POINTER);
			swithUrlLbl.getElement().getStyle().setCursor(Cursor.POINTER);
		}
	}
	private void fullUrlMixPanelEvent() {
		if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.COLLECTION_SEARCH)){
			MixpanelUtil.Share_direct_search();
		}
		else if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.SHELF)){
			MixpanelUtil.Share_direct_collection_edit();
		}
	}
	
	public void setEmbedBitlyLink(String embedBitlyLink) {
		this.embedBitlyLink = embedBitlyLink;
	}
	
}
