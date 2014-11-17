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
import org.ednovo.gooru.client.mvp.dnd.IsDraggable;
import org.ednovo.gooru.client.mvp.dnd.IsDraggableMirage;
import org.ednovo.gooru.client.mvp.rating.RatingWidgetView;
import org.ednovo.gooru.client.mvp.rating.events.OpenReviewPopUpEvent;
import org.ednovo.gooru.client.mvp.rating.events.UpdateRatingsInRealTimeEvent;
import org.ednovo.gooru.client.mvp.rating.events.UpdateRatingsInRealTimeHandler;
import org.ednovo.gooru.client.mvp.resource.dnd.ResourceDragWithImgUc;
import org.ednovo.gooru.client.uc.ResourceImageUc;
import org.ednovo.gooru.client.uc.tooltip.ToolTip;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.search.CollectionItemSearchResultDo;
import org.ednovo.gooru.shared.model.search.ResourceSearchResultDo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Search Team
 *
 */
public class SimpleResourceVc extends Composite implements IsDraggable {

	private static SimpleResourceVcUiBinder uiBinder = GWT.create(SimpleResourceVcUiBinder.class);
	
	MessageProperties i18n = GWT.create(MessageProperties.class);

	interface SimpleResourceVcUiBinder extends UiBinder<Widget, SimpleResourceVc> {
	}

	@UiField
	HTML resourceTitleLbl;
	
	@UiField HTMLPanel resourceTitleContainer;

	@UiField
	ResourceImageUc resourceImageUc;

	@UiField
	Label positionLbl;

	@UiField
	FlowPanel resourceTitlePanel,internalPanel1,metaDataFloPanel,ratingWidgetPanel;
	
	@UiField Image imgNotFriendly, imgOER;

	ToolTip toolTip = null;
	
	private ResourceSearchResultDo resourceSearchResultDo;
	private RatingWidgetView ratingWidgetView=null;
	private CollectionItemSearchResultDo collectionItemSearchResultDo;
	
	private static final String VIEWS_PREFIX_NAME = "Views";  
	
	private Label viewCountLabel=null;

	/**
	 * Class constructor, calls resource search result setData method
	 * @param resourceSearchResultDo instance of {@link ResourceSearchResultDo}
	 * @param position of search label
	 */
	public SimpleResourceVc(CollectionItemSearchResultDo resourceSearchResultDo, int position) {
		initWidget(uiBinder.createAndBindUi(this));
		positionLbl.setText(position + "");
		positionLbl.getElement().setId("lblPositionLbl");
		positionLbl.getElement().setAttribute("alt",position + "");
		positionLbl.getElement().setAttribute("title",position + "");
		
			
		
		imgOER.setUrl("images/oer_icon.png");
		imgOER.getElement().setAttribute("id", i18n.GL1834());
		imgOER.getElement().setAttribute("alt", i18n.GL1834());
		imgOER.getElement().setAttribute("title", i18n.GL1834());
		
		setData(resourceSearchResultDo);
		
		internalPanel1.getElement().setId("fpnlInternalPanel1");
		resourceTitlePanel.getElement().setId("fpnlResourceTitlePanel");
		resourceTitleContainer.getElement().setId("pnlResourceTitleContainer");
		resourceTitleLbl.getElement().setId("pnlResourceTitleLbl");
		metaDataFloPanel.getElement().setId("fpnlMetaDataFloPanel");
		ratingWidgetPanel.getElement().setId("fpnlRatingWidgetPanel");
		AppClientFactory.getEventBus().addHandler(UpdateRatingsInRealTimeEvent.TYPE,setRatingWidgetMetaData);
	}

	/**
	 * Set resource meta data info such as title, image, views, etc..
	 * @param resourceSearchResultDo instance of {@link ResourceSearchResultDo}
	 */
	public void setData(CollectionItemSearchResultDo resourceSearchResultDo) {
		
		this.resourceSearchResultDo = resourceSearchResultDo;
		this.collectionItemSearchResultDo = resourceSearchResultDo;
		/*resourceTitleLbl.setText(StringUtil.truncateText(resourceSearchResultDo.getResourceTitle(), 30));*/
		resourceTitleLbl.setHTML(resourceSearchResultDo.getResourceTitle());
		resourceTitleLbl.getElement().setAttribute("alt",resourceSearchResultDo.getResourceTitle());
		resourceTitleLbl.getElement().setAttribute("title",resourceSearchResultDo.getResourceTitle());
		setResourcePlayLink();
		final String gooruOid = resourceSearchResultDo.getCollectionItemId();
		final String collectionId = resourceSearchResultDo.getCollectionId();
		resourceTitleLbl.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//Implementing Mixpanel
				if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.PROFILE_PAGE)) {
					MixpanelUtil.Preview_Resource_From_Profile("resourceTitleLbl");
				} else {
					MixpanelUtil.Preview_Resource_From_Search("resourceTitleLbl");
				}
				AppClientFactory.getPlaceManager().setRefreshPlace(false);
			}
		});
		
		SearchUiUtil.renderMetaData(metaDataFloPanel, resourceSearchResultDo.getCourseNames(), 30);
		viewCountLabel=SearchUiUtil.renderMetaData(metaDataFloPanel, resourceSearchResultDo.getTotalViews() + "", " " + VIEWS_PREFIX_NAME);  
//		resourceImageUc.renderSearch(resourceSearchResultDo.getCategory(), resourceSearchResultDo.getUrl(), null, resourceSearchResultDo.getCollectionItemId(), PLAYER_NAME,resourceSearchResultDo.getResourceTitle(), false,collectionId);
		
		resourceImageUc.renderSearch(resourceSearchResultDo.getCategory(), resourceSearchResultDo.getUrl(), null, resourceSearchResultDo.getCollectionItemId(), resourceSearchResultDo.getResourceTitle(), false, resourceSearchResultDo.getNarration(),collectionItemSearchResultDo.getCollectionId());
		String mediaType = resourceSearchResultDo.getMediaType();
		
		boolean setVisibility = mediaType !=null ?  mediaType.equalsIgnoreCase("iPad_friendly") ? true : false : true;
		//boolean setVisibility = mediaType !=null ?  mediaType.equalsIgnoreCase("not_iPad_friendly") ? false : true : true;
		
		if (resourceTitleLbl.getText().length() > 30){
			String resourceTitleText=resourceTitleLbl.getText();
			 resourceTitleText=resourceTitleText.substring(0, 26)+"...";
			 resourceTitleLbl.setText(resourceTitleText);
			 //resourceTitleLbl.getElement().getStyle().setWidth(210, Unit.PX);
			//resourceTitleLbl.getElement().getStyle().setFloat(Float.LEFT);
		}else{
			resourceTitleLbl.getElement().getStyle().clearWidth();
			resourceTitleLbl.getElement().getStyle().clearFloat();
		}
		
		imgNotFriendly.addMouseOverHandler(new MouseOverHandler() {
			
			@Override
			public void onMouseOver(MouseOverEvent event) {
				toolTip = new ToolTip(i18n.GL0454()+""+"<img src='/images/mos/MobileFriendly.png' style='margin-top:0px;width:20px;height:15px;'/>"+" "+i18n.GL04431()+" "+"<img src='/images/mos/mobileunfriendly.png' style='margin-top:0px;width:20px;height:15px;'/>"+" "+i18n.GL_SPL_EXCLAMATION());
				toolTip.getTootltipContent().getElement().setAttribute("style", "width: 258px;");
				toolTip.getElement().getStyle().setBackgroundColor("transparent");
				toolTip.getElement().getStyle().setPosition(Position.ABSOLUTE);
				toolTip.setPopupPosition(imgNotFriendly.getAbsoluteLeft()-(50+22), imgNotFriendly.getAbsoluteTop()+22);
				toolTip.show();
			}
		});
		imgNotFriendly.addMouseOutHandler(new MouseOutHandler() {
			
			@Override
			public void onMouseOut(MouseOutEvent event) {
				
				EventTarget target = event.getRelatedTarget();
				  if (Element.is(target)) {
					  if (!toolTip.getElement().isOrHasChild(Element.as(target))){
						  toolTip.hide();
					  }
				  }
			}
		});
		//imgNotFriendly.setVisible(setVisibility);
		if(setVisibility)
		{
			imgNotFriendly.getElement().setId("imgImgFriendly");
			imgNotFriendly.setTitle(i18n.GL0737_1());
			imgNotFriendly.setAltText(i18n.GL0737_1());
			imgNotFriendly.setUrl("images/mos/MobileFriendly.png");
		}else
		{
			imgNotFriendly.getElement().setId("imgImgNotFriendly");
			imgNotFriendly.setTitle(i18n.GL0737());
			imgNotFriendly.setAltText(i18n.GL0737());
			imgNotFriendly.setUrl("images/mos/mobileunfriendly.png");
		}
		
		boolean oerVisibility = resourceSearchResultDo.getLicense() !=null &&  resourceSearchResultDo.getLicense().getCode() !=null ? resourceSearchResultDo.getLicense().getCode().contains("CC") ? true : false : false;

		imgOER.setVisible(oerVisibility);
		
		if (setVisibility || oerVisibility){
			resourceTitleContainer.getElement().getStyle().setFloat(Float.LEFT);
		}else{
			resourceTitleContainer.getElement().getStyle().clearFloat();
		}

		
		setAvgRatingWidget(resourceSearchResultDo);
	}
	private void setAvgRatingWidget(CollectionItemSearchResultDo resourceSearchResultDo) {
		ratingWidgetView=new RatingWidgetView();
		if(resourceSearchResultDo.getRatings()!=null){
			ratingWidgetView.getRatingCountOpenBrace().setText(i18n. GL_SPL_OPEN_SMALL_BRACKET());
			ratingWidgetView.getRatingCountLabel().setText(resourceSearchResultDo.getRatings().getCount()!=null?resourceSearchResultDo.getRatings().getCount().toString():"0");
			ratingWidgetView.getRatingCountCloseBrace().setText(i18n. GL_SPL_CLOSE_SMALL_BRACKET());
			ratingWidgetView.setAvgStarRating(resourceSearchResultDo.getRatings().getAverage());
		}
		//ratingWidgetView.getRatingCountLabel().addClickHandler(new ShowRatingPopupEvent());
		ratingWidgetPanel.clear();
		ratingWidgetPanel.add(ratingWidgetView);
	}
	
	UpdateRatingsInRealTimeHandler setRatingWidgetMetaData = new UpdateRatingsInRealTimeHandler() {	

		@Override
		public void updateRatingInRealTime(String gooruOid, double average,Integer count) {
			if(collectionItemSearchResultDo.getGooruOid().equals(gooruOid)){
				collectionItemSearchResultDo.getRatings().setCount(count);
				collectionItemSearchResultDo.getRatings().setAverage(average);
				setAvgRatingWidget(collectionItemSearchResultDo);
			}
			/*ratingWidgetView.getRatingCountLabel().setText(collectionItemSearchResultDo.getRatings().getCount()!=null?collectionItemSearchResultDo.getRatings().getCount().toString():"0");
			ratingWidgetView.setAvgStarRating(collectionItemSearchResultDo.getRatings().getAverage());*/
		}
	};
	
	public void updateResourceViewCount(String viewCount){
		if(viewCountLabel!=null){
			System.out.println("viewCountLabel====>"+viewCountLabel.getText());
			viewCountLabel.setText(viewCount+ " " + VIEWS_PREFIX_NAME);  
		}
	}

	/**
	 * 
	 * Inner class implementing {@link ClickEvent}
	 *
	 */
	private class ShowRatingPopupEvent implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			/**
			 * OnClick of count label event to invoke Review pop-pup
			 */
			AppClientFactory.fireEvent(new OpenReviewPopUpEvent(resourceSearchResultDo.getGooruOid(),collectionItemSearchResultDo.getResourceTitle(),resourceSearchResultDo.getOwner().getUsername())); 

		}
	}
	public void setResourcePlayLink(){
		Anchor resourceAnchor=new Anchor();
		resourceAnchor.setHref(getResourceLink());
		resourceAnchor.setStyleName("");
		resourceAnchor.getElement().appendChild(resourceTitleLbl.getElement());
		resourceTitleContainer.add(resourceAnchor);
	}
	
	public String getResourceLink(){
		String collectionId=collectionItemSearchResultDo.getCollectionId();
		if(collectionItemSearchResultDo.getNarration()!=null&&!collectionItemSearchResultDo.getNarration().trim().equals("")){
			String resourceLink="#"+PlaceTokens.COLLECTION_PLAY+"&id="+collectionId+"&rid="+collectionItemSearchResultDo.getCollectionItemId()+"&tab=narration";
			return resourceLink;
		}else{
			String resourceLink="#"+PlaceTokens.COLLECTION_PLAY+"&id="+collectionId+"&rid="+collectionItemSearchResultDo.getCollectionItemId();
			return resourceLink;
		}
	}


	@Override
	public String getDragId() {
		return resourceSearchResultDo.getGooruOid();
	}

	@Override
	public DRAG_TYPE getDragType() {
		return DRAG_TYPE.RESOURCE;
	}

	@Override
	public IsDraggableMirage initDraggableMirage() {
		return new ResourceDragWithImgUc(resourceSearchResultDo.getCategory(), resourceSearchResultDo.getResourceTitle());
	}

	@Override
	public void onDragBlur() {
	}

	@Override
	public Widget getDragHandle() {
		return null;
	}

	@Override
	public int getDragTopCorrection() {
		return 7;
	}

	@Override
	public int getDragLeftCorrection() {
		return 11;
	}
	
	
}
