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
package org.ednovo.gooru.client.mvp.play.resource.body;


import java.util.Date;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.event.InvokeLoginEvent;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BaseViewWithHandlers;
import org.ednovo.gooru.client.mvp.addTagesPopup.AddTagesPopupView;
import org.ednovo.gooru.client.mvp.home.LoginPopupUc;
import org.ednovo.gooru.client.mvp.play.collection.body.GwtEarthWidget;
import org.ednovo.gooru.client.mvp.play.collection.preview.metadata.NavigationConfirmPopup;
import org.ednovo.gooru.client.mvp.play.resource.framebreaker.ResourceFrameBreakerView;
import org.ednovo.gooru.client.mvp.rating.events.UpdateRatingOnDeleteEvent;
import org.ednovo.gooru.client.mvp.rating.events.UpdateRatingOnDeleteHandler;
import org.ednovo.gooru.client.mvp.rating.events.UpdateRatingsInRealTimeEvent;
import org.ednovo.gooru.client.mvp.rating.events.UpdateResourceRatingCountEvent;
import org.ednovo.gooru.client.mvp.rating.events.UpdateResourceReviewCountEvent;
import org.ednovo.gooru.client.mvp.search.SearchUiUtil;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.collaborators.vc.SuccessPopupViewVc;
import org.ednovo.gooru.client.uc.BrowserAgent;
import org.ednovo.gooru.client.uc.DownToolTipWidgetUc;
import org.ednovo.gooru.client.uc.PlayerBundle;
import org.ednovo.gooru.client.uc.StarRatingsUc;
import org.ednovo.gooru.client.uc.tooltip.GlobalToolTip;
import org.ednovo.gooru.client.ui.HTMLEventPanel;
import org.ednovo.gooru.client.util.PlayerDataLogEvents;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.content.CollectionItemDo;
import org.ednovo.gooru.shared.model.content.ReactionDo;
import org.ednovo.gooru.shared.model.content.ResourceTagsDo;
import org.ednovo.gooru.shared.model.content.StarRatingsDo;
import org.ednovo.gooru.shared.util.InfoUtil;
import org.ednovo.gooru.shared.util.ResourceImageUtil;
import org.ednovo.gooru.shared.util.StringUtil;
import org.ednovo.gooru.shared.util.UAgentInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Navigator;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

public class ResourcePlayerMetadataView extends BaseViewWithHandlers<ResourcePlayerMetadataUiHandlers> implements IsResourcePlayerMetadataView{

	@UiField FlowPanel resourceWidgetContainer,tagsButtonContainer;
	@UiField
	static FlowPanel wrapperContainerField,tagsContainer,resourcePublisher;
	@UiField Button forwardButton,backwardButton,selectedEmoticButton,canExplainEmoticButton,understandEmoticButton,mehEmoticButton,doNotUnderstandEmoticButton,
					needHelpButton,plusAddTagsButton,narrationButton;
	@UiField HTMLEventPanel emoticsContainer;
	@UiField HTMLPanel singleEmoticsContainer,collectionContainer,ratingsContainer;
	@UiField Label reactionToolTipOne,reactionToolTipTwo,reactionToolTipThree,reactionToolTipFour,reactionToolTipFive,mouseOverStarValue,starValue;
	@UiField
	static ResourcePlayerMetadataBundle playerStyle;
	@UiField HTML resourceTitleLbl;
	
//	@UiField(provided = true)
	@UiField InlineHTML one_star,two_star,three_star,four_star,five_star;
	
	/*@UiField SimpleRadioButton rating1;
	@UiField SimpleRadioButton rating2;
	@UiField SimpleRadioButton rating3;
	@UiField SimpleRadioButton rating4;
	@UiField SimpleRadioButton rating5;*/
	
	/*@UiField SimpleCheckBox starFive,starFour,starThree,starTwo,starOne;*/
	
	/*@UiField  StarRatingsUc starRatings;*/

	HandlerRegistration forwardButtonHandler=null, backwardButtonHandler=null;
	
	public static final String REACTION_CAN_UNDERSTAND = "i-can-understand";
	public static final String REACTION_CAN_EXPLAIN = "i-can-explain";
	public static final String REACTION_MEH = "meh";
	public static final String REACTION_DONOT_UNDERSTAND = "i-donot-understand";
	public static final String REACTION_NEED_HELP = "i-need-help";
	private static final String CREATE_PREVIEW_PLAYER_REACTION = "create-reaction-preview";
	private static final String CREATE_STUDY_PLAYER_REACTION = "create-reaction";
	private boolean isCanUnderstandSelected=false;
	private boolean isCanExplainSelected=false;
	private boolean isMehSelected=false;
	private boolean isDoNotUnderstandSelected=false;
	private boolean isNeedHelpSelected=false;
	private CollectionItemDo collectionItemDo=null;
	private String gooruReactionId="";
	private UserStarRatingsWidget userStarRatings = null;
	
	private static final String ONE_STAR="oneStar";
	private static final String TWO_STAR="twoStar";
	private static final String THREE_STAR="threeStar";
	private static final String FOUR_STAR="fourStar";
	private static final String FIVE_STAR="fiveStar";
	
	private static final String POOR="Poor";
	private static final String FAIR="Fair";
	private static final String GOOD="Good";
	private static final String VERY_GOOD="Very Good";
	private static final String EXCELLENT="Excellent";
	private static final String REACTIONS_WIDGET="Contentreactions";
	private static final String RATINGS_WIDGET="Contentratings";
	private static final int CHILD_AGE=13;
	private static final String DEFAULT_RATING_TEXT="Rate this resource";
	private static final String EMBED="embed";
	private static final String SYNDICATE="syndicate";
	private StarRatingsDo starRatingsDo;
	
	private static final String FILLED_BLUE = "filled filledBlue";
	ThankYouResourceStarRatings thankYouResourceStarRatings;
	ThankYouResourceStarRatingsPoor thankYouResourceStarRatingsPoor;
	RatingsConfirmationPopup ratingsConfirmationPopup;
	private boolean isRated=false,isFromThanksPopup=false;
	private double previousRating=0;
	String assocGooruOId;
	Integer score,count;
	double average;
	
	private PopupPanel toolTipPopupPanel=new PopupPanel();
	
	int currentRating=0;
	
	public HTMLPanel getCollectionContainer(){
		return collectionContainer;
	}
	
	public Button getNarrationButton(){
		return narrationButton;
	}
	
	private static ResourcePlayerMetadataViewUiBinder uiBinder = GWT.create(ResourcePlayerMetadataViewUiBinder.class);

	interface ResourcePlayerMetadataViewUiBinder extends UiBinder<Widget, ResourcePlayerMetadataView> {
	}
	
	private MessageProperties i18n = GWT.create(MessageProperties.class);
	
//	@UiFactory
//	public SimpleRadioButton createRadioButton() {
//	    return new SimpleRadioButton("");
//	}

	@Inject
	public ResourcePlayerMetadataView(){
		
//		one_star = new SimpleRadioButton("");
//		two_star = new SimpleRadioButton("");
//		three_star = new SimpleRadioButton("");
//		four_star = new SimpleRadioButton("");
//		five_star = new SimpleRadioButton("");
		
		setWidget(uiBinder.createAndBindUi(this));
		//allEmoticsContainer.getElement().getStyle().setDisplay(Display.BLOCK);
		////singleEmoticsContainer.setVisible(false);
		singleEmoticsContainer.getElement().getStyle().setDisplay(Display.NONE);
		//emoticsContainer.addMouseOverHandler(new OnEmoticsMouseOver());
		//emoticsContainer.addMouseOutHandler(new OnEmoticsMouseOut());
		reactionToolTipOne.setText(i18n.GL0581()); 
		reactionToolTipOne.getElement().setId("lblReactionToolTipOne");
		reactionToolTipOne.getElement().setAttribute("alt",i18n.GL0581());
		reactionToolTipOne.getElement().setAttribute("title",i18n.GL0581());
		
		reactionToolTipTwo.setText(i18n.GL0582()); 
		reactionToolTipTwo.getElement().setId("lblReactionToolTipTwo");
		reactionToolTipTwo.getElement().setAttribute("alt",i18n.GL0582());
		reactionToolTipTwo.getElement().setAttribute("title",i18n.GL0582());
		
		reactionToolTipThree.setText(i18n.GL0583()); 
		reactionToolTipThree.getElement().setId("lblReactionToolTipThree");
		reactionToolTipThree.getElement().setAttribute("alt",i18n.GL0583());
		reactionToolTipThree.getElement().setAttribute("title",i18n.GL0583());
		
		reactionToolTipFour.setText(i18n.GL0584()); 
		reactionToolTipFour.getElement().setId("lblReactionToolTipFour");
		reactionToolTipFour.getElement().setAttribute("alt",i18n.GL0584());
		reactionToolTipFour.getElement().setAttribute("title",i18n.GL0584());
		
		reactionToolTipFive.setText(i18n.GL0585()); 
		reactionToolTipFive.getElement().setId("lblReactionToolTipFive");
		reactionToolTipFive.getElement().setAttribute("alt",i18n.GL0585());
		reactionToolTipFive.getElement().setAttribute("title",i18n.GL0585());
		
		plusAddTagsButton.setText("+ "+i18n.GL2081());
		plusAddTagsButton.getElement().setId("plusAddTagsButton");
		plusAddTagsButton.getElement().setAttribute("alt",i18n.GL2081());
		plusAddTagsButton.getElement().setAttribute("title",i18n.GL2081());
		
		/*rating1 = new SimpleRadioButton("rating");
		rating2 = new SimpleRadioButton("rating");
		rating3 = new SimpleRadioButton("rating");
		rating4 = new SimpleRadioButton("rating");
		rating5 = new SimpleRadioButton("rating");*/
		
//		starValue.setVisible(false);
		starValue.setVisible(true);
		starValue.setText(i18n.GL1879());
		starValue.getElement().setId("lblStarValue");
		starValue.getElement().setAttribute("alt",i18n.GL1879());
		starValue.getElement().setAttribute("title",i18n.GL1879());
		
		if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.RESOURCE_PLAY)){
			emoticsContainer.removeFromParent();
			resourceTitleLbl.setVisible(false);
			resourcePublisher.setVisible(false);
			ratingsContainer.getElement().getStyle().setFloat(Float.RIGHT);
			ratingsContainer.getElement().getStyle().setMarginRight(430,Unit.PX);
			if(isChildAccount()){
				collectionContainer.getElement().getStyle().setDisplay(Display.NONE);
			}else{
				collectionContainer.getElement().getStyle().setDisplay(Display.BLOCK);
			}
//			collectionContainer.getElement().getStyle().setDisplay(Display.NONE);
		}else{
			resourceTitleLbl.setVisible(true);
			resourcePublisher.setVisible(true);
		}
		
		  Boolean isIpad = !!Navigator.getUserAgent().matches("(.*)iPad(.*)");
		  Boolean isAndriod = !!Navigator.getUserAgent().matches("(.*)Android(.*)");
		  Boolean isWinDskp = !!Navigator.getUserAgent().matches("(.*)NT(.*)");
		  
		  UAgentInfo detector = new UAgentInfo(Navigator.getUserAgent());
		  
		  if(isIpad && !StringUtil.IPAD_MESSAGE_Close_Click)
		  {
			  wrapperContainerField.getElement().setAttribute("style", "margin-top:0px;");
			 
		  }
		  else if(isAndriod && !StringUtil.IPAD_MESSAGE_Close_Click)
		  {
			  wrapperContainerField.getElement().setAttribute("style", "margin-top:0px;");
		  }
		  else
		  {
			 // wrapperContainerField.getElement().setAttribute("style", "margin-top:50px;");
			  
		  }
		  
			one_star.addMouseOverHandler(new OnStarMouseOver(ONE_STAR));
			two_star.addMouseOverHandler(new OnStarMouseOver(TWO_STAR));
			three_star.addMouseOverHandler(new OnStarMouseOver(THREE_STAR));
			four_star.addMouseOverHandler(new OnStarMouseOver(FOUR_STAR));
			five_star.addMouseOverHandler(new OnStarMouseOver(FIVE_STAR));
			
			one_star.addMouseOutHandler(new OnStarMouseOut(ONE_STAR));
			two_star.addMouseOutHandler(new OnStarMouseOut(TWO_STAR));
			three_star.addMouseOutHandler(new OnStarMouseOut(THREE_STAR));
			four_star.addMouseOutHandler(new OnStarMouseOut(FOUR_STAR));
			five_star.addMouseOutHandler(new OnStarMouseOut(FIVE_STAR));
			setId();
			
			doNotUnderstandEmoticButton.addMouseOverHandler(new ReactionsMouseOverHandler(doNotUnderstandEmoticButton,i18n.GL0584()));
			needHelpButton.addMouseOverHandler(new ReactionsMouseOverHandler(needHelpButton, i18n.GL0585()));
			mehEmoticButton.addMouseOverHandler(new ReactionsMouseOverHandler(mehEmoticButton, i18n.GL0583()));
			understandEmoticButton.addMouseOverHandler(new ReactionsMouseOverHandler(understandEmoticButton, i18n.GL0582()));
			canExplainEmoticButton.addMouseOverHandler(new ReactionsMouseOverHandler(canExplainEmoticButton, i18n.GL0581()));
			
			doNotUnderstandEmoticButton.addMouseOutHandler(new ReactionsMouseOutHandler());
			needHelpButton.addMouseOutHandler(new ReactionsMouseOutHandler());
			mehEmoticButton.addMouseOutHandler(new ReactionsMouseOutHandler());
			understandEmoticButton.addMouseOutHandler(new ReactionsMouseOutHandler());
			canExplainEmoticButton.addMouseOutHandler(new ReactionsMouseOutHandler());
	}

	public void showResourceWidget(CollectionItemDo collectionItemDo){
		this.collectionItemDo = collectionItemDo;
		if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.RESOURCE_PLAY)){
			collectionContainer.getElement().getStyle().setMarginTop(51, Unit.PX);
			tagsButtonContainer.setVisible(false);
			if(isChildAccount()){
				collectionContainer.getElement().getStyle().setDisplay(Display.NONE);
			}else{
				collectionContainer.getElement().getStyle().setDisplay(Display.BLOCK);
			}
		}
		
			displayPublisher();
						
		if(collectionItemDo.getResource().getTitle()!=null){
			resourceTitleLbl.setHTML(removeHtmlTags(collectionItemDo.getResource().getTitle()));
			resourceTitleLbl.getElement().setAttribute("alt",removeHtmlTags(collectionItemDo.getResource().getTitle()));
			resourceTitleLbl.getElement().setAttribute("title",removeHtmlTags(collectionItemDo.getResource().getTitle()));
		}else{
			resourceTitleLbl.setHTML("");
			resourceTitleLbl.getElement().setAttribute("alt","");
			resourceTitleLbl.getElement().setAttribute("title","");
		}
		
		getUiHandlers().setResourceMetaData(resourceTitleLbl.getHTML());
		if(forwardButton!=null){
			forwardButton.removeFromParent();
		}
		if(backwardButton!=null){
			backwardButton.removeFromParent();
		}
		previewResouceWidget(collectionItemDo);
		AppClientFactory.getEventBus().addHandler(UpdateRatingOnDeleteEvent.TYPE, updateRatingOnDeleteHandler);
	}
	UpdateRatingOnDeleteHandler updateRatingOnDeleteHandler = new UpdateRatingOnDeleteHandler(){

		@Override
		public void updateRating(boolean isDeleted) {
		if(isDeleted){
			clearAllStars();
		}
	}
		
	};
	public void showResourceWidget(PlaceRequest previousResourceRequest){	
		
		collectionContainer.setVisible(false);
		
		if(forwardButtonHandler!=null||backwardButtonHandler!=null){
			forwardButtonHandler.removeHandler();
			backwardButtonHandler.removeHandler();
		}
		forwardButton.getElement().getStyle().setDisplay(Display.NONE);
		forwardButtonHandler=forwardButton.addClickHandler(new ShowResourceView(previousResourceRequest));
		backwardButtonHandler=backwardButton.addClickHandler(new ShowResourceView(previousResourceRequest));
	}

	public void showResourceWidget(CollectionItemDo collectionItemDo,PlaceRequest nextResoruceRequest,PlaceRequest previousResourceRequest){ 
		if(AppClientFactory.isAnonymous()){
			setDefaultReaction();
		}
		if(collectionItemDo!=null){
			collectionContainer.setVisible(true);
			this.collectionItemDo = collectionItemDo;
			if(collectionItemDo.getResource().getTitle()!=null){
				int sequenceNumber=collectionItemDo.getItemSequence();
				String titlelbl1=InfoUtil.removeQuestionTagsOnBoldClick(collectionItemDo.getResource().getTitle());
			//	resourceTitleLbl.setHTML(sequenceNumber+". "+removeHtmlTags(collectionItemDo.getResource().getTitle()));
				resourceTitleLbl.setHTML(sequenceNumber+". "+removeHtmlTags(titlelbl1));
				resourceTitleLbl.getElement().setAttribute("alt",removeHtmlTags(collectionItemDo.getResource().getTitle()));
				resourceTitleLbl.getElement().setAttribute("title",removeHtmlTags(collectionItemDo.getResource().getTitle()));
			}else{
				resourceTitleLbl.setHTML("");
				resourceTitleLbl.getElement().setAttribute("alt","");
				resourceTitleLbl.getElement().setAttribute("title","");
			}
			displayPublisher();
			getUiHandlers().setResourceMetaData(resourceTitleLbl.getHTML());
			if(forwardButtonHandler!=null||backwardButtonHandler!=null){
				forwardButtonHandler.removeHandler();
				backwardButtonHandler.removeHandler();
			}
			forwardButton.getElement().getStyle().clearDisplay();
			previewResouceWidget(collectionItemDo);

			forwardButtonHandler=forwardButton.addClickHandler(new ShowResourceView(nextResoruceRequest));
			backwardButtonHandler=backwardButton.addClickHandler(new ShowResourceView(previousResourceRequest));
		}

	}
	
	private void displayPublisher(){
		if(collectionItemDo.getResource()!=null && collectionItemDo.getResource().getResourceFormat()!=null ||collectionItemDo.getResource()!=null && collectionItemDo.getResource().getPublisher()!=null){
			List<String> publishersList=null;
			resourcePublisher.clear();
			if(collectionItemDo.getResource().getPublisher()!=null){
				if(!collectionItemDo.getResource().getPublisher().isEmpty()){
					publishersList=collectionItemDo.getResource().getPublisher()!=null?collectionItemDo.getResource().getPublisher():null;
					if(publishersList!=null&&publishersList.size()>0){
						//publishersList.set(0,i18n.GL0566()+publishersList.get(0));
						resourcePublisher.clear();
						SearchUiUtil.renderMetaData(resourcePublisher, publishersList);
					}else{
						resourcePublisher.clear();
						resourcePublisher.getElement().setInnerHTML("");
						resourcePublisher.getElement().getStyle().setPaddingTop(0, Unit.PX);
					}
				}
				else if(collectionItemDo.getResource().getResourceFormat()!=null && collectionItemDo.getResource().getResourceFormat().getValue().equalsIgnoreCase("question")){
					if (collectionItemDo.getResource().getCreator() != null && collectionItemDo.getResource().getCreator().getUsername()!=null){
						if(!AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.RESOURCE_PLAY)){
							resourcePublisher.setVisible(true);
							resourcePublisher.clear();
							resourcePublisher.getElement().setInnerHTML(i18n.GL0566()+collectionItemDo.getResource().getCreator().getUsername());
							resourcePublisher.getElement().getStyle().clearPaddingTop();
						}else{
							resourcePublisher.setVisible(false);
						}
					}
				}
			}
			else if(collectionItemDo.getResource().getResourceFormat()!=null && collectionItemDo.getResource().getResourceFormat().getValue().equalsIgnoreCase("question")){
				if (collectionItemDo.getResource().getCreator() != null && collectionItemDo.getResource().getCreator().getUsername()!=null){
					if(!AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.RESOURCE_PLAY)){
						resourcePublisher.setVisible(true);
						resourcePublisher.clear();
						resourcePublisher.getElement().setInnerHTML(i18n.GL0566()+collectionItemDo.getResource().getCreator().getUsername());
						resourcePublisher.getElement().getStyle().clearPaddingTop();
					}else{
						resourcePublisher.setVisible(false);
					}
				}
			}
		}
	}

	public void previewResouceWidget(final CollectionItemDo collectionItemDo){
		resourceWidgetContainer.clear();
		setResourceWidgetContainerHeight();
		String resourceTypeName=collectionItemDo.getResource().getResourceType().getName();
		wrapperContainerField.getElement().getStyle().clearHeight();
		if(resourceTypeName.equalsIgnoreCase("video/youtube")){
			//wrapperContainerField.getElement().getStyle().setHeight(525	, Unit.PX);
			String utubeId=ResourceImageUtil.getYoutubeVideoId(collectionItemDo.getResource().getUrl());
			getUiHandlers().getYoutubeFeedCallback(utubeId);
		}else if(resourceTypeName.equalsIgnoreCase("animation/kmz")){
			resourceWidgetContainer.add(new GwtEarthWidget(collectionItemDo.getResource().getUrl()));
		}else if(resourceTypeName.equalsIgnoreCase("animation/swf")){
			String resourceSourceUrl="";
			if(collectionItemDo.getResource().getHasFrameBreaker()!=null&&collectionItemDo.getResource().getHasFrameBreaker().equals(true)){
				resourceWidgetContainer.add(new ResourceFrameBreakerView(collectionItemDo,false));
			}else{
				if(!collectionItemDo.getResource().getUrl().substring(0, 4).equalsIgnoreCase("http")){
					resourceSourceUrl = collectionItemDo.getResource().getUrl();
					resourceSourceUrl=collectionItemDo.getResource().getAssetURI()+collectionItemDo.getResource().getFolder()+resourceSourceUrl;
				}else{
					resourceSourceUrl=collectionItemDo.getResource().getUrl();
				}
				resourceWidgetContainer.add(new WebResourceWidget(resourceSourceUrl));
			}
		}else if(resourceTypeName.equalsIgnoreCase("assessment-question")){
			getUiHandlers().showQuestionView(collectionItemDo);
		}else if(resourceTypeName.equalsIgnoreCase("resource/url")||resourceTypeName.equalsIgnoreCase("image/png")){
			if(collectionItemDo.getResource().getHasFrameBreaker()!=null&&collectionItemDo.getResource().getHasFrameBreaker().equals(true)||isProtocolMatched(collectionItemDo.getResource().getUrl())){
				resourceWidgetContainer.add(new ResourceFrameBreakerView(collectionItemDo,false));
			}else if(collectionItemDo.getResource().getUrl().contains("imdb")){
				resourceWidgetContainer.add(new ResourceFrameBreakerView(collectionItemDo,false));
			}
			else{
				if(resourceTypeName.equalsIgnoreCase("image/png")){
//					final WebResourceWidget webResourceWidget=new WebResourceWidget(collectionItemDo.getResource().getAssetURI()+collectionItemDo.getResource().getFolder()+collectionItemDo.getResource().getUrl());
//					System.out.println("webResourceWidget"+webResourceWidget);
					HTMLPanel htmlPanel = new HTMLPanel("");
					htmlPanel.addStyleName(playerStyle.collectionPlayerImage());
					Image img = new Image();
					img.setUrl(collectionItemDo.getResource().getAssetURI()+collectionItemDo.getResource().getFolder()+collectionItemDo.getResource().getUrl());
					htmlPanel.add(img);
					resourceWidgetContainer.add(htmlPanel);
				}
				else{
					if(!collectionItemDo.getResource().getUrl().startsWith("http")){
						collectionItemDo.getResource().setUrl(collectionItemDo.getResource().getAssetURI()+collectionItemDo.getResource().getFolder()+collectionItemDo.getResource().getUrl());
					}
					if(collectionItemDo.getResource().getUrl().contains("docs.google.com")){
						getUiHandlers().getGoogleDriveFile(collectionItemDo.getResource().getUrl());
					}else{
						final WebResourceWidget webResourceWidget=new WebResourceWidget(collectionItemDo.getResource().getUrl());
						resourceWidgetContainer.add(webResourceWidget);
					}
				}
			}
		}else {

			
			String[] urlFormat = collectionItemDo.getResource().getUrl().split("\\.");
			String urlExtension = urlFormat[urlFormat.length - 1];
			if(urlExtension.equalsIgnoreCase("pdf")){
				String resourceSourceUrl="";
				if(!collectionItemDo.getResource().getUrl().substring(0, 4).equalsIgnoreCase("http")){
//					resourceSourceUrl = URL.encode(collectionItemDo.getResource().getUrl()).replaceAll("\\+", "%20");
					resourceSourceUrl = collectionItemDo.getResource().getUrl();
					resourceSourceUrl=collectionItemDo.getResource().getAssetURI()+collectionItemDo.getResource().getFolder()+resourceSourceUrl;
				}else{
					resourceSourceUrl=collectionItemDo.getResource().getUrl();
				}
				String signedFlag=resourceSourceUrl.contains("http")||resourceSourceUrl.contains("https")?"0":"1";
				String startPage=collectionItemDo.getStart()!=null?collectionItemDo.getStart():"1";
				resourceWidgetContainer.add(new WebResourceWidget(AppClientFactory.getLoggedInUser().getSettings().getDocViewerHome()+"?startPage="+startPage+"&endPage=&signedFlag="+signedFlag+"&oid="+collectionItemDo.getResource().getGooruOid()+"&appKey="+AppClientFactory.getLoggedInUser().getSettings().getDocViewerPoint()+"&url="+resourceSourceUrl));
			}
			else
			{
				String resourceSourceUrl="";
				if(!collectionItemDo.getResource().getUrl().substring(0, 4).equalsIgnoreCase("http")){
					resourceSourceUrl = collectionItemDo.getResource().getUrl();
					resourceSourceUrl=collectionItemDo.getResource().getAssetURI()+collectionItemDo.getResource().getFolder()+resourceSourceUrl;
				}else{
					resourceSourceUrl=collectionItemDo.getResource().getUrl();
				}
				resourceWidgetContainer.add(new WebResourceWidget(resourceSourceUrl));
			}
		}
	}
	public void setResourceWidgetContainerHeight(){
		int windowHeight=Window.getClientHeight();
		if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.RESOURCE_PLAY)){
			resourceWidgetContainer.setHeight((windowHeight-116)+"px");
		}else{
			resourceWidgetContainer.setHeight((windowHeight-202)+"px");
		}
	}
	public void setGoogleDriveFileStatusCode(Integer statusCode){
		if(statusCode==302){
			ResourceFrameBreakerView resourceFrameBreakerViewnew =new ResourceFrameBreakerView(collectionItemDo,true);
			resourceFrameBreakerViewnew.setFilePermissionMessage();
			resourceWidgetContainer.add(resourceFrameBreakerViewnew);
		}else if(statusCode==404){
			ResourceFrameBreakerView resourceFrameBreakerViewnew =new ResourceFrameBreakerView(collectionItemDo,true);
			resourceFrameBreakerViewnew.setFileDeletedMessage();
			resourceWidgetContainer.add(resourceFrameBreakerViewnew);
		}else{
			final WebResourceWidget webResourceWidget=new WebResourceWidget(collectionItemDo.getResource().getUrl());
			resourceWidgetContainer.add(webResourceWidget);
		}
	}
	
	/**
	 * On click, "canExplain" emotic will be set for a resource, provided user should be logged in or it demands for log in.
	 * @param clickEvent {@link ClickEvent}
	 */
	@UiHandler("canExplainEmoticButton")
	public void clickOnCanExplainEmoticButtonEmotic(ClickEvent clickEvent){
		if(AppClientFactory.isAnonymous()){
			showLoginPopupWidget(REACTIONS_WIDGET);
		}else{
			if(isCanExplainSelected){
				canExplainEmoticButton.setStyleName(playerStyle.spriteType());
				canExplainEmoticButton.addStyleName(playerStyle.emoticon_i());
				getUiHandlers().deleteReaction(gooruReactionId);
				deleteReactionTriggerEvent();
				setDefaultReaction();
			}else{
				canExplainEmoticButton.setStyleName(playerStyle.spriteType());
				canExplainEmoticButton.addStyleName(playerStyle.emoticon_vi());
				deleteReactionTriggerEvent();
				createReaction(collectionItemDo.getResource().getGooruOid(),REACTION_CAN_EXPLAIN,gooruReactionId,AppClientFactory.getPlaceManager().getRequestParameter("id"),AppClientFactory.getCurrentPlaceToken());
				getUiHandlers().triggerCreateReactionEvent(collectionItemDo.getResource().getGooruOid(), REACTION_CAN_EXPLAIN,PlayerDataLogEvents.REACTION_CREATE);
			}
			 
		}
		
	}
	
	/**
	 * On click, "understand" emotic will be set for a resource, provided user should be logged in or it demands for log in.
	 * @param clickEvent {@link ClickEvent}
	 */
	@UiHandler("understandEmoticButton")
	public void clickOnUnderstandEmotic(ClickEvent clickEvent){
		if(AppClientFactory.isAnonymous()){
			showLoginPopupWidget(REACTIONS_WIDGET);
		}else{
			/**
			 * If user clicks on selected emotic again, it will de-select and will call delete API, else it will highlight the selected emotic 
			 * and call Create API.
			 */
			if(isCanUnderstandSelected){
				understandEmoticButton.setStyleName(playerStyle.spriteType());
				understandEmoticButton.addStyleName(playerStyle.emoticon_ii());
				getUiHandlers().deleteReaction(gooruReactionId);
				deleteReactionTriggerEvent();
				setDefaultReaction();
			}else{
				understandEmoticButton.setStyleName(playerStyle.spriteType());
				understandEmoticButton.addStyleName(playerStyle.emoticon_vii());
				deleteReactionTriggerEvent();
				createReaction(collectionItemDo.getResource().getGooruOid(),REACTION_CAN_UNDERSTAND,gooruReactionId,AppClientFactory.getPlaceManager().getRequestParameter("id"),AppClientFactory.getCurrentPlaceToken());
				getUiHandlers().triggerCreateReactionEvent(collectionItemDo.getResource().getGooruOid(), REACTION_CAN_UNDERSTAND,PlayerDataLogEvents.REACTION_CREATE);
			}
		}
		
	}
	
	/**
	 * On click,"meh" emotic will be set for a resource, provided user should be logged in or it demands for log in.
	 * @param clickEvent {@link ClickEvent}
	 */
	@UiHandler("mehEmoticButton")
	public void clickOnMehEmoticButton(ClickEvent clickEvent){
		if(AppClientFactory.isAnonymous()){
			showLoginPopupWidget(REACTIONS_WIDGET);
		}else{
			/**
			 * If user clicks on selected emotic again, it will de-select and will call delete API, else it will highlight the selected emotic 
			 * and call Create API.
			 */
			if(isMehSelected){
				mehEmoticButton.setStyleName(playerStyle.spriteType());
				mehEmoticButton.addStyleName(playerStyle.emoticon_viii());
				getUiHandlers().deleteReaction(gooruReactionId);
				deleteReactionTriggerEvent();
				setDefaultReaction();
			}else{
				mehEmoticButton.setStyleName(playerStyle.spriteType());
				mehEmoticButton.addStyleName(playerStyle.emoticon_v());
				deleteReactionTriggerEvent();
				createReaction(collectionItemDo.getResource().getGooruOid(),REACTION_MEH,gooruReactionId,AppClientFactory.getPlaceManager().getRequestParameter("id"),AppClientFactory.getCurrentPlaceToken());
				getUiHandlers().triggerCreateReactionEvent(collectionItemDo.getResource().getGooruOid(), REACTION_MEH,PlayerDataLogEvents.REACTION_CREATE);
			}
			
		}
		
	}
	
	/**
	 * On click, "doNotUnderstand" emotic will be set for a resource, provided user should be logged in or it demands for log in.
	 * @param clickEvent {@link ClickEvent}
	 */
	@UiHandler("doNotUnderstandEmoticButton")
	public void clickOnDoNotUnderstandEmoticButton(ClickEvent clickEvent){
		if(AppClientFactory.isAnonymous()){
			showLoginPopupWidget(REACTIONS_WIDGET);
		}else{
			/**
			 * If user clicks on selected emotic again, it will de-select and will call delete API, else it will highlight the selected emotic 
			 * and call Create API.
			 */
			if(isDoNotUnderstandSelected){
				doNotUnderstandEmoticButton.setStyleName(playerStyle.spriteType());
				doNotUnderstandEmoticButton.addStyleName(playerStyle.emoticon_iii());
				getUiHandlers().deleteReaction(gooruReactionId);
				deleteReactionTriggerEvent();
				setDefaultReaction();
			}else{
				doNotUnderstandEmoticButton.setStyleName(playerStyle.spriteType());
				doNotUnderstandEmoticButton.addStyleName(playerStyle.emoticon_ix());
				deleteReactionTriggerEvent();
				createReaction(collectionItemDo.getResource().getGooruOid(),REACTION_DONOT_UNDERSTAND,gooruReactionId,AppClientFactory.getPlaceManager().getRequestParameter("id"),AppClientFactory.getCurrentPlaceToken());
				getUiHandlers().triggerCreateReactionEvent(collectionItemDo.getResource().getGooruOid(), REACTION_DONOT_UNDERSTAND,PlayerDataLogEvents.REACTION_CREATE);
			}
		}
	}
	
	/**
	 * On click, "needHelp" emotic will be set for a resource, provided user should be logged in or it demands for log in.
	 * @param clickEvent {@link ClickEvent}
	 */
	@UiHandler("needHelpButton")
	public void clickOnNeedHelpButtonEmoticButton(ClickEvent clickEvent){
		if(AppClientFactory.isAnonymous()){
			showLoginPopupWidget(REACTIONS_WIDGET);
		}else{
			/**
			 * If user clicks on selected emotic again, it will de-select and will call delete API, else it will highlight the selected emotic 
			 * and call Create API.
			 */
			if(isNeedHelpSelected){
				needHelpButton.setStyleName(playerStyle.spriteType());
				needHelpButton.addStyleName(playerStyle.emoticon_iv());
				deleteReactionTriggerEvent();
				getUiHandlers().deleteReaction(gooruReactionId);
				setDefaultReaction();
			}else{
				needHelpButton.setStyleName(playerStyle.spriteType());
				needHelpButton.addStyleName(playerStyle.emoticon_x());
				deleteReactionTriggerEvent();
				createReaction(collectionItemDo.getResource().getGooruOid(),REACTION_NEED_HELP,gooruReactionId,AppClientFactory.getPlaceManager().getRequestParameter("id"),AppClientFactory.getCurrentPlaceToken());
				getUiHandlers().triggerCreateReactionEvent(collectionItemDo.getResource().getGooruOid(), REACTION_NEED_HELP,PlayerDataLogEvents.REACTION_CREATE);
			}
		}
	}

	public void deleteReactionTriggerEvent(){
		if(isCanUnderstandSelected){
			getUiHandlers().triggerCreateReactionEvent(collectionItemDo.getResource().getGooruOid(), REACTION_CAN_UNDERSTAND,PlayerDataLogEvents.REACTION_DELETE);
		}else if(isCanExplainSelected){
			getUiHandlers().triggerCreateReactionEvent(collectionItemDo.getResource().getGooruOid(), REACTION_CAN_EXPLAIN,PlayerDataLogEvents.REACTION_DELETE);
		}else if(isDoNotUnderstandSelected){
			getUiHandlers().triggerCreateReactionEvent(collectionItemDo.getResource().getGooruOid(), REACTION_DONOT_UNDERSTAND,PlayerDataLogEvents.REACTION_DELETE);
		}else if(isMehSelected){
			getUiHandlers().triggerCreateReactionEvent(collectionItemDo.getResource().getGooruOid(), REACTION_MEH,PlayerDataLogEvents.REACTION_DELETE);
		}else if(isNeedHelpSelected){
			getUiHandlers().triggerCreateReactionEvent(collectionItemDo.getResource().getGooruOid(), REACTION_NEED_HELP,PlayerDataLogEvents.REACTION_DELETE);
		}
	}
	private void showLoginPopupWidget(String widgetType) { 
		LoginPopupUc popup =new LoginPopupUc();
		if(widgetType.equals(REACTIONS_WIDGET)){
			popup.setWidgetMode("reactionWidget");
		}else if(widgetType.equals(RATINGS_WIDGET)){
			popup.setWidgetMode("ratingWidget");
		}
		
		popup.setGlassEnabled(true);
	}

	@Override
	public FlowPanel getResourceWidgetContainer(){
		return resourceWidgetContainer;
	}

	public class ShowResourceView implements ClickHandler{
		private PlaceRequest resourceRequest;
		public ShowResourceView(PlaceRequest resourceRequest){
			this.resourceRequest=resourceRequest;
		}
		@Override
		public void onClick(ClickEvent event) {
			if(!getUiHandlers().isOeAnswerSubmited()){
				NavigationConfirmPopup confirmPopup=new NavigationConfirmPopup() {
					@Override
					public void navigateToNextResource() {
						super.hide();
						AppClientFactory.getPlaceManager().revealPlace(false, resourceRequest,true);
					}
				};
			}else{
				AppClientFactory.getPlaceManager().revealPlace(false, resourceRequest,true);
			}
		}
	}

	/*
	 * Inner Class
	 */
	public class OnEmoticsMouseOver implements MouseOverHandler{

		/**
		 * On mouse over emotics options will be shown for the user.  
		 */
		
		@Override
		public void onMouseOver(MouseOverEvent event) {
			//allEmoticsContainer.getElement().getStyle().setDisplay(Display.BLOCK);
			singleEmoticsContainer.getElement().getStyle().setDisplay(Display.NONE);
			
			/**
			 * On mouse over if user selected any emotic, selected emotic will be highlighted.
			 * Note: selected emotic will be differntiated based on respective emotic flag.
			 */
		
		}

	}

	/*
	 * Inner Class
	 */
	public class OnEmoticsMouseOut implements MouseOutHandler{
		
		/**
		 * On mouse out only selected emotic will be visible.
		 */
		@Override
		public void onMouseOut(MouseOutEvent event) {
			//allEmoticsContainer.getElement().getStyle().setDisplay(Display.NONE);
			singleEmoticsContainer.getElement().getStyle().setDisplay(Display.NONE);
		}
	}

	/**
	 * Sets the emotics for respective resource.
	 * 
	 * @param reactionDo {@link ReactionDo}
	 * @param gooruReactionId {@link String}
	 */
	
	@Override
	public void setReaction(ReactionDo reactionDo,String gooruReactionId) {
		this.gooruReactionId=gooruReactionId;
		if(reactionDo.getReactionText().equalsIgnoreCase(REACTION_CAN_EXPLAIN)){
			canExplainEmoticButton.setStyleName(playerStyle.spriteType());
			canExplainEmoticButton.addStyleName(playerStyle.emoticon_vi());
			isCanExplainSelected=true;
			
			/**
			 * Except "can explain" emotic, all other will be changed to normal emotics.
			 * Note: this changes is made to achieve when user tries to select multiple emotic, previously selected emotic shld be removed.
			 */
			understandEmoticButton.setStyleName(playerStyle.spriteType());
			understandEmoticButton.addStyleName(playerStyle.emoticon_ii());
			
			mehEmoticButton.setStyleName(playerStyle.spriteType());
			mehEmoticButton.addStyleName(playerStyle.emoticon_viii());
			
			doNotUnderstandEmoticButton.setStyleName(playerStyle.spriteType());
			doNotUnderstandEmoticButton.addStyleName(playerStyle.emoticon_iii());
			
			needHelpButton.setStyleName(playerStyle.spriteType());
			needHelpButton.addStyleName(playerStyle.emoticon_iv());
			
			/**
			 * Except can explain emotic flag, all other emotic flag set to false, based on this again on mouse over previously selected will be removed.
			 * 
			 */
			isCanUnderstandSelected=false;
			isMehSelected=false;
			isDoNotUnderstandSelected=false;
			isNeedHelpSelected=false;
		}else if(reactionDo.getReactionText().equalsIgnoreCase(REACTION_CAN_UNDERSTAND)){
			understandEmoticButton.setStyleName(playerStyle.spriteType());
			understandEmoticButton.addStyleName(playerStyle.emoticon_vii());
			isCanUnderstandSelected = true;
			
			/**
			 * Except "can understand" emotic, all other will be changed to normal emotics.
			 * Note: this changes is made to achieve when user tries to select multiple emotic, previously selected emotic shld be removed.
			 */
			canExplainEmoticButton.setStyleName(playerStyle.spriteType());
			canExplainEmoticButton.addStyleName(playerStyle.emoticon_i());
			
			mehEmoticButton.setStyleName(playerStyle.spriteType());
			mehEmoticButton.addStyleName(playerStyle.emoticon_viii());
			
			doNotUnderstandEmoticButton.setStyleName(playerStyle.spriteType());
			doNotUnderstandEmoticButton.addStyleName(playerStyle.emoticon_iii());
			
			needHelpButton.setStyleName(playerStyle.spriteType());
			needHelpButton.addStyleName(playerStyle.emoticon_iv());
			
			/**
			 * Except "can understand" emotic flag, all other emotic flag set to false, based on this again on mouse over previously selected will be removed.
			 * 
			 */
			isCanExplainSelected=false;
			isMehSelected=false;
			isDoNotUnderstandSelected=false;
			isNeedHelpSelected=false;
		}else if(reactionDo.getReactionText().equalsIgnoreCase(REACTION_MEH)){
			mehEmoticButton.setStyleName(playerStyle.spriteType());
			mehEmoticButton.addStyleName(playerStyle.emoticon_v());
			isMehSelected = true;
			
			/**
			 * Except "meh" emotic, all other will be changed to normal emotics.
			 * Note: this changes is made to achieve when user tries to select multiple emotic, previously selected emotic shld be removed.
			 */
			canExplainEmoticButton.setStyleName(playerStyle.spriteType());
			canExplainEmoticButton.addStyleName(playerStyle.emoticon_i());
			
			understandEmoticButton.setStyleName(playerStyle.spriteType());
			understandEmoticButton.addStyleName(playerStyle.emoticon_ii());
			
			doNotUnderstandEmoticButton.setStyleName(playerStyle.spriteType());
			doNotUnderstandEmoticButton.addStyleName(playerStyle.emoticon_iii());
			
			needHelpButton.setStyleName(playerStyle.spriteType());
			needHelpButton.addStyleName(playerStyle.emoticon_iv());
			
			/**
			 * Except "meh" emotic flag, all other emotic flag set to false, based on this again on mouse over previously selected will be removed.
			 * 
			 */
			isCanExplainSelected=false;
			isCanUnderstandSelected=false;
			isDoNotUnderstandSelected=false;
			isNeedHelpSelected=false;
		}else if(reactionDo.getReactionText().equalsIgnoreCase(REACTION_DONOT_UNDERSTAND)){
			doNotUnderstandEmoticButton.setStyleName(playerStyle.spriteType());
			doNotUnderstandEmoticButton.addStyleName(playerStyle.emoticon_ix());
			isDoNotUnderstandSelected = true;
			
			/**
			 * Except "do not understand" emotic, all other will be changed to normal emotics.
			 * Note: this changes is made to achieve when user tries to select multiple emotic, previously selected emotic shld be removed.
			 */
			canExplainEmoticButton.setStyleName(playerStyle.spriteType());
			canExplainEmoticButton.addStyleName(playerStyle.emoticon_i());
			
			understandEmoticButton.setStyleName(playerStyle.spriteType());
			understandEmoticButton.addStyleName(playerStyle.emoticon_ii());
			
			mehEmoticButton.setStyleName(playerStyle.spriteType());
			mehEmoticButton.addStyleName(playerStyle.emoticon_viii());
			
			needHelpButton.setStyleName(playerStyle.spriteType());
			needHelpButton.addStyleName(playerStyle.emoticon_iv());
			
			/**
			 * Except "do not understand" emotic flag, all other emotic flag set to false, based on this again on mouse over previously selected will be removed.
			 * 
			 */
			isCanExplainSelected=false;
			isCanUnderstandSelected=false;
			isMehSelected=false;
			isNeedHelpSelected=false;
		}else if(reactionDo.getReactionText().equalsIgnoreCase(REACTION_NEED_HELP)){
			needHelpButton.setStyleName(playerStyle.spriteType());
			needHelpButton.addStyleName(playerStyle.emoticon_x());
			isNeedHelpSelected = true;
			
			/**
			 * Except "need help" emotic, all other will be changed to normal emotics.
			 * Note: this changes is made to achieve when user tries to select multiple emotic, previously selected emotic shld be removed.
			 */
			canExplainEmoticButton.setStyleName(playerStyle.spriteType());
			canExplainEmoticButton.addStyleName(playerStyle.emoticon_i());
			
			understandEmoticButton.setStyleName(playerStyle.spriteType());
			understandEmoticButton.addStyleName(playerStyle.emoticon_ii());
			
			mehEmoticButton.setStyleName(playerStyle.spriteType());
			mehEmoticButton.addStyleName(playerStyle.emoticon_viii());
			
			doNotUnderstandEmoticButton.setStyleName(playerStyle.spriteType());
			doNotUnderstandEmoticButton.addStyleName(playerStyle.emoticon_iii());
			
			/**
			 * Except "need help" emotic flag, all other emotic flag set to false, based on this again on mouse over previously selected will be removed.
			 * 
			 */
			isCanExplainSelected=false;
			isCanUnderstandSelected=false;
			isMehSelected=false;
			isDoNotUnderstandSelected=false;
			
		}
		
	}
	public static void addPadding(){
		//wrapperContainerField.removeStyleName(playerStyle.collectionPlayerWrapper());
		//wrapperContainerField.addStyleName(playerStyle.collectionPlayerWrapperPadding());
	}
	
	public static void removePadding(){
		//wrapperContainerField.removeStyleName(playerStyle.collectionPlayerWrapperPadding());
		//wrapperContainerField.addStyleName(playerStyle.collectionPlayerWrapper());
	}

	/**
	 * If no reaction available, sets the default reaction for all the resources.
	 */
	@Override
	public void setDefaultReaction() {
		gooruReactionId="";
		isCanUnderstandSelected=false;
		isCanExplainSelected = false;
		isDoNotUnderstandSelected=false;
		isMehSelected=false;
		isNeedHelpSelected=false;
		selectedEmoticButton.setStyleName(playerStyle.spriteType());
		selectedEmoticButton.addStyleName(playerStyle.emoticon_ii());
		
		/**
		 * Resets all emotics to normal in allemoticContainer,
		 */
		canExplainEmoticButton.setStyleName(playerStyle.spriteType());
		canExplainEmoticButton.addStyleName(playerStyle.emoticon_i());
		
		understandEmoticButton.setStyleName(playerStyle.spriteType());
		understandEmoticButton.addStyleName(playerStyle.emoticon_ii());
		
		mehEmoticButton.setStyleName(playerStyle.spriteType());
		mehEmoticButton.addStyleName(playerStyle.emoticon_viii());
		
		doNotUnderstandEmoticButton.setStyleName(playerStyle.spriteType());
		doNotUnderstandEmoticButton.addStyleName(playerStyle.emoticon_iii());
		
		needHelpButton.setStyleName(playerStyle.spriteType());
		needHelpButton.addStyleName(playerStyle.emoticon_iv());
	}
	
	/**
	 * On select of any reaction based on the player type API call is done.
	 * 
	 * @param resourceGooruId {@link String}
	 * @param reactionText {@link String}
	 * @param gooruReactionId {@link String}
	 * @param collectionId {@link String}
	 * @param playerName {@link String}
	 */
	private void createReaction(String resourceGooruId,String reactionText,String gooruReactionId,String collectionId,String playerName){
		if(playerName.trim().equalsIgnoreCase(PlaceTokens.COLLECTION_PLAY)){ 
			getUiHandlers().createReaction(resourceGooruId,reactionText,gooruReactionId,collectionId,CREATE_STUDY_PLAYER_REACTION);
		}else if(playerName.trim().equalsIgnoreCase(PlaceTokens.PREVIEW_PLAY)){
			getUiHandlers().createReaction(resourceGooruId,reactionText,gooruReactionId,collectionId,CREATE_PREVIEW_PLAYER_REACTION);
		}
		
	}
	
	private String removeHtmlTags(String html){
		html = html.replaceAll("(<\\w+)[^>]*(>)", "$1$2");
        html = html.replaceAll("</p>", " ").replaceAll("<p>", "").replaceAll("<br data-mce-bogus=\"1\">", "").replaceAll("<br>", "").replaceAll("</br>", "").replaceAll("<p class=\"p1\">", "");
        return html;
	}
	public boolean isProtocolMatched(String sourceUrl){
		boolean isProtocolsMatched=false;
		if(FlashAndVideoPlayerWidget.getProtocal().equalsIgnoreCase("https:")){
				isProtocolsMatched=!sourceUrl.contains("https");
		}
		else{
			if(!sourceUrl.contains("docs.google.com")){
				isProtocolsMatched=sourceUrl.contains("https");
			}
		}
		if(sourceUrl.toLowerCase().endsWith("jpg")||sourceUrl.toLowerCase().endsWith("jpeg")){
			{
				isProtocolsMatched=sourceUrl.contains("https");
			}
		}
		return isProtocolsMatched;
	}
	
	/**
	 * Sets the user ratings and and star rating widget will get added.
	 * 
	 * @param result {@link StarRatingsDo}
	 * @param showThankYouToolTip {@link Boolean}
	 */
	@Override
	public void setUserStarRatings(StarRatingsDo result, boolean showThankYouToolTip) {
		if(result!=null){
			this.starRatingsDo=result;
			isRated=true; 
			if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.RESOURCE_PLAY)){
				if(result.getRatings()!=null){
					if(isFromThanksPopup){
						getUiHandlers().updateResourceReview(collectionItemDo.getResource().getGooruOid(),result.getRatings().getReviewCount());
						AppClientFactory.fireEvent(new UpdateRatingsInRealTimeEvent(collectionItemDo.getResource().getGooruOid(),result.getRatings().getAverage(),result.getRatings().getReviewCount()));
						AppClientFactory.fireEvent(new UpdateResourceReviewCountEvent(collectionItemDo.getResource().getGooruOid(),result.getRatings().getReviewCount()));
					}
					getUiHandlers().updateResourceRatings(collectionItemDo.getResource().getGooruOid(),result.getRatings().getAverage());
					AppClientFactory.fireEvent(new UpdateResourceRatingCountEvent(collectionItemDo.getResource().getGooruOid(),result.getRatings().getAverage(),result.getRatings().getReviewCount()));
				}
				
			}
			/**
			 * Changed to collection player, as preview player feature removed.
			 */
			else if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.COLLECTION_PLAY)){
				if(result.getRatings()!=null){
					if(isFromThanksPopup){
						getUiHandlers().updateResourceReview(collectionItemDo.getResource().getGooruOid(),result.getRatings().getReviewCount());
						AppClientFactory.fireEvent(new UpdateResourceReviewCountEvent(collectionItemDo.getResource().getGooruOid(),result.getRatings().getReviewCount()));
					}
					getUiHandlers().updateResourceRatings(collectionItemDo.getResource().getGooruOid(),result.getRatings().getAverage());
					AppClientFactory.fireEvent(new UpdateRatingsInRealTimeEvent(collectionItemDo.getResource().getGooruOid(),result.getRatings().getAverage(),result.getRatings().getReviewCount()));
					
				}
			}
		}else{
			isRated=false;
		}
		
		if(thankYouResourceStarRatingsPoor!=null){
			if(thankYouResourceStarRatingsPoor.isVisible()){
				thankYouResourceStarRatingsPoor.hide();
				if(isFromThanksPopup){
					displaySuccessPopup();
				}
			}
		}
		
		if(thankYouResourceStarRatings!=null){
			if(thankYouResourceStarRatings.isVisible()){
				thankYouResourceStarRatings.hide();
				if(isFromThanksPopup){
					displaySuccessPopup();
				}
			}
		}
		setRatings(result,showThankYouToolTip);
	}
	
	@Override
	public void setDefaultUserStarRatings() {
//		starValue.setVisible(false);
	}
	
	/**
	 * 
	 * Inner class extending to StarRatingsUc {@link StarRatingsUc}
	 *
	 */
	public class UserStarRatingsWidget extends StarRatingsUc{
		StarRatingsDo ratingsDo =null;
		public boolean showThankYouToolTip;
		
		/**
		 * Class Constructor
		 * @param result {@link StarRatingsDo}
		 * @param showThankYouToolTip {@link Boolean} 
		 */
		public UserStarRatingsWidget(StarRatingsDo result, boolean showThankYouToolTip) { 
			this.ratingsDo=result;
			this.showThankYouToolTip=showThankYouToolTip;
//			setRatings(result,showThankYouToolTip);
		}
		/**
		 * Class constructor
		 */
		public UserStarRatingsWidget() {
			setDefaultRatings();
		}
		/**
		 * Sets the default rating.
		 */
		private void setDefaultRatings() {
			getDefaultRatings();
		}

		/**
		 * Implementation of parent class method, in this create API will be called based the rating selected.
		 * @param selectedStar {@link String}
		 */
		@Override
		public void crateStarRating(String selectedStar) {
			if(selectedStar.equals("starOne")){
				getUiHandlers().createStarRatings(collectionItemDo.getResource().getGooruOid(),1,true,null,collectionItemDo.getResource().getGooruOid());
			}else if(selectedStar.equals("starTwo")){                                           
				getUiHandlers().createStarRatings(collectionItemDo.getResource().getGooruOid(),2,true,null,collectionItemDo.getResource().getGooruOid());
			}else if(selectedStar.equals("starThree")){                                          
				getUiHandlers().createStarRatings(collectionItemDo.getResource().getGooruOid(),3,true,null,collectionItemDo.getResource().getGooruOid());
			}else if(selectedStar.equals("starFour")){                                           
				getUiHandlers().createStarRatings(collectionItemDo.getResource().getGooruOid(),4,true,null,collectionItemDo.getResource().getGooruOid());
			}else if(selectedStar.equals("starFive")){                                           
				getUiHandlers().createStarRatings(collectionItemDo.getResource().getGooruOid(),5,true,null,collectionItemDo.getResource().getGooruOid());
			}
		}
		
	}
	
	public void removeRatingContainer(boolean flag){
		if(flag){
	//		ratingsContainer.removeFromParent();
			ratingsContainer.setVisible(false);
			emoticsContainer.setVisible(true);
		}else{
			if(isChildAccount()){
	//			ratingsContainer.removeFromParent();
				ratingsContainer.setVisible(false);
			}else{
				ratingsContainer.setVisible(true);
			}
			emoticsContainer.setVisible(false);
			//emoticsContainer.removeFromParent();
		}
	}
	
	/**
	 * Checks weather the logged in user is child or not.
	 * @return isChild {@link Boolean} 
	 */
	private boolean isChildAccount() {
		Date convertedDOB = null;
		boolean isChild=false;
		int loggedInUserAge = 0;
		com.google.gwt.i18n.client.DateTimeFormat dateFormat = com.google.gwt.i18n.client.DateTimeFormat.getFormat("yyyy-MM-dd hh:mm:ss.S");
		if(AppClientFactory.getLoggedInUser().getDateOfBirth()!=null&&!AppClientFactory.getLoggedInUser().getDateOfBirth().equals("")){
			convertedDOB = dateFormat.parse(AppClientFactory.getLoggedInUser().getDateOfBirth());
			loggedInUserAge = getAge(convertedDOB);
			if(loggedInUserAge<=CHILD_AGE){
				isChild=true;
			}else if(loggedInUserAge>CHILD_AGE){
				isChild=false;
			}
		}
		
		return isChild;
	}

	/**
	 * On click of Star 1 sets the rating on view.
	 * @param clickEvent {@link ClickEvent}
	 */
	@UiHandler("one_star")
	public void onStarOneclicked(ClickEvent event){
		if(AppClientFactory.isAnonymous()){
			getDefaultRatings();
			showLoginPopupWidget(RATINGS_WIDGET);
		}else{
			if(isRated){
				updateStarRating(ONE_STAR);
			}else{
				isRated=true;
				crateStarRating(ONE_STAR);
			}
		}
		
		
	}
	
	/**
	 * On click of Star 2 sets the rating on view.
	 * @param clickEvent {@link ClickEvent}
	 */
	@UiHandler("two_star")
	public void onStarTwoclicked(ClickEvent event){
		if(AppClientFactory.isAnonymous()){
			getDefaultRatings();
			showLoginPopupWidget(RATINGS_WIDGET);
		}else{
			if(isRated){
				updateStarRating(TWO_STAR);
			}else{
				isRated=true;
				crateStarRating(TWO_STAR);
			}
		}
		
	}
	
	/**
	 * On click of Star 3 sets the rating on view.
	 * @param clickEvent {@link ClickEvent}
	 */
	@UiHandler("three_star")
	public void onStarThreeclicked(ClickEvent event){
		if(AppClientFactory.isAnonymous()){
			getDefaultRatings();
			showLoginPopupWidget(RATINGS_WIDGET);
		}else{
			if(isRated){
				updateStarRating(THREE_STAR);
			}else{
				isRated=true;
				crateStarRating(THREE_STAR);
			}
		}
	}
	
	/**
	 * On click of Star 4 sets the rating on view.
	 * @param clickEvent {@link ClickEvent}
	 */
	@UiHandler("four_star")
	public void onStarFourclicked(ClickEvent event){
		if(AppClientFactory.isAnonymous()){
			getDefaultRatings();
			showLoginPopupWidget(RATINGS_WIDGET);
		}else{
			if(isRated){
				updateStarRating(FOUR_STAR);
			}else{
				isRated=true;
				crateStarRating(FOUR_STAR);
			}
		}
		
	}
	
	/**
	 * On click of Star 5 sets the rating on view.
	 * @param clickEvent {@link ClickEvent}
	 */
	@UiHandler("five_star")
	public void onStarFiveclicked(ClickEvent event){
		if(AppClientFactory.isAnonymous()){
			getDefaultRatings();
			showLoginPopupWidget(RATINGS_WIDGET);
		}else{
			if(isRated){
				updateStarRating(FIVE_STAR);
			}else{
				isRated=true;
				crateStarRating(FIVE_STAR);
			}
		}
		
	}
	
	/**
	 * Sets the Star rating based on the API result.
	 * @param result {@link StarRatingsDo}
	 * @param showThankYouToolTip {@link Boolean} 
	 */
	private void setRatings(StarRatingsDo result,boolean showThankYouToolTip) {
		if(showThankYouToolTip){
			getAvgRatingAndCount(result.getAssocGooruOid(),result.getScore(),result.getFreeText());
		}
		setUserRatings(result);
	}
	
	
	private void getAvgRatingAndCount(String assocGooruOid, Integer score, String review) {
		getUiHandlers().getAvgRatingAndCount(assocGooruOid,score,review);
	}

	/**
	 * default rating will get set
	 */
	public void getDefaultRatings(){
//		one_star.setValue(false);
//		two_star.setValue(false);
//		three_star.setValue(false);
//		four_star.setValue(false);
//		five_star.setValue(false);
	}
	
	public void setPreviousRating(double previousRating){
		this.previousRating=previousRating;
	}
	
	public double getPreviousRating(){
		return previousRating;
	}
	
	/**
	 * In this create API will be called based the rating selected.
	 * @param selectedStar {@link String}
	 */
	public void crateStarRating(String selectedStar) {
		if(selectedStar.equals(ONE_STAR)){
			getUiHandlers().createStarRatings(collectionItemDo.getResource().getGooruOid(),1,true,"",collectionItemDo.getResource().getGooruOid());
		}else if(selectedStar.equals(TWO_STAR)){                                           
			getUiHandlers().createStarRatings(collectionItemDo.getResource().getGooruOid(),2,true,"",collectionItemDo.getResource().getGooruOid());
		}else if(selectedStar.equals(THREE_STAR)){                                          
			getUiHandlers().createStarRatings(collectionItemDo.getResource().getGooruOid(),3,true,"",collectionItemDo.getResource().getGooruOid());
		}else if(selectedStar.equals(FOUR_STAR)){                                           
			getUiHandlers().createStarRatings(collectionItemDo.getResource().getGooruOid(),4,true,"",collectionItemDo.getResource().getGooruOid());
		}else if(selectedStar.equals(FIVE_STAR)){                                           
			getUiHandlers().createStarRatings(collectionItemDo.getResource().getGooruOid(),5,true,"",collectionItemDo.getResource().getGooruOid());
		}
	}
	
	/**
	 * In this update API will be called based the rating selected.
	 * @param selectedStar {@link String}
	 */
	public void updateStarRating(String selectedStar) {
		if(selectedStar.equals(ONE_STAR)){
			getUiHandlers().updateStarRatings(starRatingsDo.getDeleteRatingGooruOid(),1,true,collectionItemDo.getResource().getGooruOid());
		}else if(selectedStar.equals(TWO_STAR)){                                           
			getUiHandlers().updateStarRatings(starRatingsDo.getDeleteRatingGooruOid(),2,true,collectionItemDo.getResource().getGooruOid());
		}else if(selectedStar.equals(THREE_STAR)){                                          
			getUiHandlers().updateStarRatings(starRatingsDo.getDeleteRatingGooruOid(),3,true,collectionItemDo.getResource().getGooruOid());
		}else if(selectedStar.equals(FOUR_STAR)){                                           
			getUiHandlers().updateStarRatings(starRatingsDo.getDeleteRatingGooruOid(),4,true,collectionItemDo.getResource().getGooruOid());
		}else if(selectedStar.equals(FIVE_STAR)){                                           
			getUiHandlers().updateStarRatings(starRatingsDo.getDeleteRatingGooruOid(),5,true,collectionItemDo.getResource().getGooruOid());
		}
	}
	
	
	/**
	 * Resource ratings will get set based on the respective scores.
	 * 
	 * @param result {@link StarRatingsDo}
	 */
	public void setUserRatings(StarRatingsDo result){

		if(result!=null){
			setStyle();
			currentRating = result.getScore();
			if(result.getScore()==1){
				starValue.setVisible(true);
				starValue.setText(POOR);
				setStarRatingValue(1); 
			}else if(result.getScore()==2){
				starValue.setVisible(true);
				starValue.setText(FAIR);
				setStarRatingValue(2); 
			}else if(result.getScore()==3){
				starValue.setVisible(true);
				starValue.setText(GOOD);
				setStarRatingValue(3);
			}else if(result.getScore()==4){
				starValue.setVisible(true);
				starValue.setText(VERY_GOOD);
				setStarRatingValue(4);
				
			}else if(result.getScore()==5){
				starValue.setVisible(true);
				starValue.setText(EXCELLENT);
				setStarRatingValue(5);
			}else{
				setStarRatingValue(0);
			}
		}else{
			starValue.setVisible(true);
			starValue.setText(i18n.GL1879());
			setStarRatingValue(0);
		}
	
	}
	
	private void setStyle() {
		if(!starValue.getText().equalsIgnoreCase(DEFAULT_RATING_TEXT)){
			starValue.getElement().getStyle().setFontStyle(FontStyle.ITALIC);
		}
	}

	public void clearAllStars(){
		one_star.getElement().removeClassName(FILLED_BLUE);
		two_star.getElement().removeClassName(FILLED_BLUE);
		three_star.getElement().removeClassName(FILLED_BLUE);
		four_star.getElement().removeClassName(FILLED_BLUE);
		five_star.getElement().removeClassName(FILLED_BLUE);
		setPreviousRating(0);
	}
	
	
	/**
	 * Sets the stars on view based on the scores.
	 * @param starRating {@link Integer}
	 */
	private void setStarRatingValue(int starRating) {
		
		clearAllStars();
		setPreviousRating(starRating);
		if(starRating==1){
			one_star.getElement().addClassName(FILLED_BLUE);
		}else if(starRating==2){
			one_star.getElement().addClassName(FILLED_BLUE);
			two_star.getElement().addClassName(FILLED_BLUE);
		}else if(starRating==3){
			one_star.getElement().addClassName(FILLED_BLUE);
			two_star.getElement().addClassName(FILLED_BLUE);
			three_star.getElement().addClassName(FILLED_BLUE);
		}else if(starRating==4){
			one_star.getElement().addClassName(FILLED_BLUE);
			two_star.getElement().addClassName(FILLED_BLUE);
			three_star.getElement().addClassName(FILLED_BLUE);
			four_star.getElement().addClassName(FILLED_BLUE);
		}else if(starRating==5){
			one_star.getElement().addClassName(FILLED_BLUE);
			two_star.getElement().addClassName(FILLED_BLUE);
			three_star.getElement().addClassName(FILLED_BLUE);
			four_star.getElement().addClassName(FILLED_BLUE);
			five_star.getElement().addClassName(FILLED_BLUE);
		}else{
			clearAllStars();
			setPreviousRating(0);
		}
	}
	
	/**
	 * 
	 * Inner class which implements mouse over handler.
	 *
	 */
	public class OnStarMouseOver implements MouseOverHandler{
		private String starScore="";
		/**
		 * Class Constructor.
		 * @param starScore {@link String}
		 */
		public OnStarMouseOver(String starScore) {
			this.starScore=starScore;
		}

		@Override
		public void onMouseOver(MouseOverEvent event) {
			if(starValue.getText().equals(DEFAULT_RATING_TEXT)){
				clearAllStars();
				starValue.setVisible(false);
			}else{
				starValue.setVisible(true);
			}
			if(starScore.equalsIgnoreCase(ONE_STAR)){
				if(starValue.getText().equals(DEFAULT_RATING_TEXT)){
					clearAllStars();
					one_star.getElement().addClassName(FILLED_BLUE);
					mouseOverStarValue.setText(POOR);
					mouseOverStarValue.getElement().setAttribute("alt",POOR);
					mouseOverStarValue.getElement().setAttribute("title",POOR);
				}else{
					starValue.setVisible(true);
				}
			}else if(starScore.equalsIgnoreCase(TWO_STAR)){
				if(starValue.getText().equals(DEFAULT_RATING_TEXT)){
					clearAllStars();
					one_star.getElement().addClassName(FILLED_BLUE);
					two_star.getElement().addClassName(FILLED_BLUE);
					mouseOverStarValue.setText(FAIR);
					mouseOverStarValue.getElement().setAttribute("alt",FAIR);
					mouseOverStarValue.getElement().setAttribute("title",FAIR);
				}else{
					starValue.setVisible(true);
				}
			}else if(starScore.equalsIgnoreCase(THREE_STAR)){
				if(starValue.getText().equals(DEFAULT_RATING_TEXT)){
					clearAllStars();
					one_star.getElement().addClassName(FILLED_BLUE);
					two_star.getElement().addClassName(FILLED_BLUE);
					three_star.getElement().addClassName(FILLED_BLUE);
					mouseOverStarValue.setText(GOOD);
					mouseOverStarValue.getElement().setAttribute("alt",GOOD);
					mouseOverStarValue.getElement().setAttribute("title",GOOD);
				}else{
					starValue.setVisible(true);
				}
			}else if(starScore.equalsIgnoreCase(FOUR_STAR)){
				if(starValue.getText().equals(DEFAULT_RATING_TEXT)){
					clearAllStars();
					one_star.getElement().addClassName(FILLED_BLUE);
					two_star.getElement().addClassName(FILLED_BLUE);
					three_star.getElement().addClassName(FILLED_BLUE);
					four_star.getElement().addClassName(FILLED_BLUE);
					mouseOverStarValue.setText(VERY_GOOD);
					mouseOverStarValue.getElement().setAttribute("alt",VERY_GOOD);
					mouseOverStarValue.getElement().setAttribute("title",VERY_GOOD);
				}else{
					starValue.setVisible(true);
				}
			}else if(starScore.equalsIgnoreCase(FIVE_STAR)){
				if(starValue.getText().equals(DEFAULT_RATING_TEXT)){
					clearAllStars();
					one_star.getElement().addClassName(FILLED_BLUE);
					two_star.getElement().addClassName(FILLED_BLUE);
					three_star.getElement().addClassName(FILLED_BLUE);
					four_star.getElement().addClassName(FILLED_BLUE);
					five_star.getElement().addClassName(FILLED_BLUE);
					mouseOverStarValue.setText(EXCELLENT);
					mouseOverStarValue.getElement().setAttribute("alt",EXCELLENT);
					mouseOverStarValue.getElement().setAttribute("title",EXCELLENT);
				}

			}
		}
		
	}
	
	public class OnStarMouseOut implements MouseOutHandler{
		private String starScore="";
		public OnStarMouseOut(String starScore) {
			this.starScore=starScore;
		}

		@Override
		public void onMouseOut(MouseOutEvent event) {
			clearAllStars();
			if(starScore.equalsIgnoreCase(ONE_STAR)){
				starValue.setVisible(true);
				mouseOverStarValue.setText("");
				mouseOverStarValue.getElement().setAttribute("alt","");
				mouseOverStarValue.getElement().setAttribute("title","");
			}else if(starScore.equalsIgnoreCase(TWO_STAR)){
				starValue.setVisible(true);
				mouseOverStarValue.setText("");
				mouseOverStarValue.getElement().setAttribute("alt","");
				mouseOverStarValue.getElement().setAttribute("title","");
			}else if(starScore.equalsIgnoreCase(THREE_STAR)){
				starValue.setVisible(true);
				mouseOverStarValue.setText("");
				mouseOverStarValue.getElement().setAttribute("alt","");
				mouseOverStarValue.getElement().setAttribute("title","");
			}else if(starScore.equalsIgnoreCase(FOUR_STAR)){
				starValue.setVisible(true);
				mouseOverStarValue.setText("");
				mouseOverStarValue.getElement().setAttribute("alt","");
				mouseOverStarValue.getElement().setAttribute("title","");
			}else if(starScore.equalsIgnoreCase(FIVE_STAR)){
				starValue.setVisible(true);
				mouseOverStarValue.setText("");
				mouseOverStarValue.getElement().setAttribute("alt","");
				mouseOverStarValue.getElement().setAttribute("title","");
			}
			
			if(!starValue.getText().equals(DEFAULT_RATING_TEXT)){
				setStarRatingValue(currentRating);
			}
		}
	}
	
	
	public static void onClosingAndriodorIpaddiv()
	{
		 // wrapperContainerField.getElement().setAttribute("style", "margin-top:50px;");
	}

	/**
	 * API call to update or to create review.
	 * User review will get created or if already reviewed and edited, review will get updated.
	 * 
	 * @param assocGooruOid {@link String}
	 * @param userReview {@link String}
	 * @param score {@link Integer}
	 * @param isUpdate {@link Boolean}
	 */
	@Override
	public void postReview(String assocGooruOId, String userReview, Integer score,boolean isUpdate) {
		if(isUpdate){
			getUiHandlers().updateReview(starRatingsDo.getDeleteRatingGooruOid(), score,userReview,collectionItemDo.getResource().getGooruOid());
		}else{
			if(ratingsConfirmationPopup!=null && ratingsConfirmationPopup.isVisible()){
				ratingsConfirmationPopup.hide();
			}
			isFromThanksPopup=true;
			getUiHandlers().createStarRatings(assocGooruOId,score,false,userReview,collectionItemDo.getResource().getGooruOid());
		}
		
	}

	/**
	 * Thank you too-tip will get closed, if it i opened.
	 */
	@Override
	public void hideThankYouPopUp() {
		if(thankYouResourceStarRatings!=null){
			thankYouResourceStarRatings.hide();
			displaySuccessPopup();
		}
		
		if(thankYouResourceStarRatingsPoor!=null){
			thankYouResourceStarRatingsPoor.hide();
			displaySuccessPopup();
		}
	}

	/**
	 * Invokes Thankyou tool tip after user rated a resource and sets the metadata.
	 * @param assocGooruOid {@link String}
	 * @param score {@link Integer}
	 * @param review {@link String}
	 * @param average {@link Integer}
	 * @param count {@link Integer}
	 */
	@Override
	public void setRatingMetaData(String assocGooruOid, Integer score,String review, double average, Integer count) {
		this.assocGooruOId=assocGooruOid;
		this.score=score;
		this.count=count;
		this.average=average;
		if(score > 1)
		{
			thankYouResourceStarRatings = new ThankYouResourceStarRatings(assocGooruOid,score,review,average,count,collectionItemDo.getResource().getUser().getUsername()); 
			thankYouResourceStarRatings.getElement().getStyle().setZIndex(999999);
			thankYouResourceStarRatings.getElement().getStyle().setPadding(0, Unit.PX);
			if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.RESOURCE_PLAY)){  
				thankYouResourceStarRatings.setPopupPosition(three_star.getElement().getAbsoluteLeft()+(-150),three_star.getElement().getAbsoluteTop()+40);
			}else{
				thankYouResourceStarRatings.setPopupPosition(800,Window.getScrollTop()+153);
			}
			
			thankYouResourceStarRatings.show();
			thankYouResourceStarRatings.setAutoHideEnabled(true);
			thankYouResourceStarRatings.getElement().getPreviousSiblingElement().getStyle().setZIndex(999999);
			thankYouResourceStarRatings.getElement().getPreviousSiblingElement().getStyle().setOpacity(0);
		}
		else
		{
			thankYouResourceStarRatingsPoor = new ThankYouResourceStarRatingsPoor(assocGooruOid,score,review,average,count,collectionItemDo.getResource().getUser().getUsername()); 
			thankYouResourceStarRatingsPoor.getElement().getStyle().setZIndex(999999);
			thankYouResourceStarRatingsPoor.getElement().getStyle().setPadding(0, Unit.PX);
			if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.RESOURCE_PLAY)){
				thankYouResourceStarRatingsPoor.setPopupPosition(three_star.getElement().getAbsoluteLeft()+(-150),three_star.getElement().getAbsoluteTop()+40);
			}else{
				thankYouResourceStarRatingsPoor.setPopupPosition(800,Window.getScrollTop()+153);
			}
			thankYouResourceStarRatingsPoor.show();
			thankYouResourceStarRatingsPoor.setAutoHideEnabled(true);
			thankYouResourceStarRatingsPoor.getElement().getPreviousSiblingElement().getStyle().setZIndex(999999);
			thankYouResourceStarRatingsPoor.getElement().getPreviousSiblingElement().getStyle().setOpacity(0);
		}
	}
	
	
	private int getAge(Date birthDate) {
		long ageInMillis = new Date().getTime() - birthDate.getTime();
		Date age = new Date(ageInMillis);
		return age.getYear() - 70;
	}

	@Override
	public void displaySuccessPopup() {
		if(ratingsConfirmationPopup!=null && ratingsConfirmationPopup.isVisible()){
			ratingsConfirmationPopup.hide();
		}
		isFromThanksPopup=false;
		ratingsConfirmationPopup=new RatingsConfirmationPopup(assocGooruOId,score,count,average,collectionItemDo.getResource().getUser().getUsername());
		ratingsConfirmationPopup.show();
		ratingsConfirmationPopup.setAutoHideEnabled(true);
		ratingsConfirmationPopup.getElement().getStyle().setZIndex(99999);
		ratingsConfirmationPopup.getElement().getPreviousSiblingElement().getStyle().setZIndex(999999);
		ratingsConfirmationPopup.getElement().getPreviousSiblingElement().getStyle().setOpacity(0);
		if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.RESOURCE_PLAY)){
			ratingsConfirmationPopup.setPopupPosition(three_star.getElement().getAbsoluteLeft()+(-150),three_star.getElement().getAbsoluteTop()+40);
		}else{
			ratingsConfirmationPopup.setPopupPosition(800,Window.getScrollTop()+153);
		}
		

	}

	@Override
	public void updateRatingOnSearch(StarRatingsDo starRatingsDo) {
		if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.RESOURCE_PLAY)){
			if(starRatingsDo.getRatings()!=null){
				AppClientFactory.fireEvent(new UpdateResourceRatingCountEvent(collectionItemDo.getResource().getGooruOid(),starRatingsDo.getRatings().getAverage(),starRatingsDo.getRatings().getCount()));
			}
			
		}
		
	}

	@Override
	public void clearAllStarsForAnnonymous() {
		ratingsContainer.setVisible(true);
		starValue.setText(DEFAULT_RATING_TEXT);
		clearAllStars();
	}

	@Override
	public void childLoggedIn(boolean isChild) {
		if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.RESOURCE_PLAY)){
			if(isChild){
				collectionContainer.getElement().getStyle().setDisplay(Display.NONE);
			}else{
				collectionContainer.getElement().getStyle().setDisplay(Display.BLOCK);
			}
		}
		/**
		 * Changed to collection player, as preview player feature removed.
		 */
		else if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.COLLECTION_PLAY)){
			if(isChild){
				ratingsContainer.setVisible(false);
			}else{
				ratingsContainer.setVisible(true);
			}
			
		}
	}

	@Override
	public void deleteRatingsValue() {
		isRated=false;
		starValue.setText(DEFAULT_RATING_TEXT);
		clearAllStars();
	}
	public void setId(){
		wrapperContainerField.getElement().setId("fpnlWrapperContainerField");
		collectionContainer.getElement().setId("pnlCollectionContainer");
		ratingsContainer.getElement().setId("pnlRatingsContainer");
		mouseOverStarValue.getElement().setId("lblMouseOverStarValue");
		one_star.getElement().setId("divOne_star");
		two_star.getElement().setId("divTwo_star");
		three_star.getElement().setId("divThree_star");
		four_star.getElement().setId("divFour_star");
		five_star.getElement().setId("divFive_star");
		emoticsContainer.getElement().setId("epnlEmoticsContainer");
		singleEmoticsContainer.getElement().setId("pnlSingleEmoticsContainer");
		selectedEmoticButton.getElement().setId("btnSelectedEmoticButton");
		needHelpButton.getElement().setId("btnNeedHelpButton");
		doNotUnderstandEmoticButton.getElement().setId("btnDoNotUnderstandEmoticButton");
		mehEmoticButton.getElement().setId("btnMehEmoticButton");
		understandEmoticButton.getElement().setId("btnUnderstandEmoticButton");
		canExplainEmoticButton.getElement().setId("btnCanExplainEmoticButton");
		forwardButton.getElement().setId("btnForwardButton");
		backwardButton.getElement().setId("btnBackwardButton");
		resourceTitleLbl.getElement().setId("htmlResourceTitleLbl");
		resourcePublisher.getElement().setId("lblResourcePublisher");
		resourceWidgetContainer.getElement().setId("fpnlResourceWidgetContainer");
	}

	@Override
	public void displayResourceTags(List<ResourceTagsDo> resourceTagsList) {
		tagsContainer.clear();
		if(resourceTagsList!=null){
			FlowPanel toolTipwidgets = new FlowPanel();
	
			for(int i=0;i<resourceTagsList.size();i++){
				if(i<3)
				{
				
					String tagsdefaultLabel = i18n.GL1664() + " : ";
					String tagLabel = resourceTagsList.get(i).getLabel();
					HTMLPanel tagPanel = new HTMLPanel("");
					if(resourceTagsList.get(i).getLabel().toLowerCase().contains(i18n.GL1664().toLowerCase()))
					{
						tagLabel = resourceTagsList.get(i).getLabel().toLowerCase().replace(tagsdefaultLabel.toLowerCase(), "");
						tagLabel = tagLabel.substring(0, 1).toUpperCase() + tagLabel.substring(1);
					}
					tagPanel.setStyleName(playerStyle.eductaionalUseDesign());
					tagPanel.getElement().setInnerHTML(tagLabel);
					tagsContainer.add(tagPanel);
				
				}
				else
				{
					HTMLPanel tagPanel = new HTMLPanel("");
					String tagsdefaultLabel = i18n.GL1664() + " : ";
					String tagLabel = resourceTagsList.get(i).getLabel();
					if(resourceTagsList.get(i).getLabel().toLowerCase().contains(i18n.GL1664().toLowerCase()))
					{
						tagLabel = resourceTagsList.get(i).getLabel().toLowerCase().replace(tagsdefaultLabel.toLowerCase(), "");
						tagLabel = tagLabel.substring(0, 1).toUpperCase() + tagLabel.substring(1);
					}
					tagPanel.getElement().setInnerHTML(tagLabel);
					toolTipwidgets.add(tagPanel);
					
		
				}
			}
			if(resourceTagsList.size()>3)
			{
				DownToolTipWidgetUc toolTipUc = new DownToolTipWidgetUc(new Label("+" + (resourceTagsList.size()-3)), toolTipwidgets);
				toolTipUc.setStyleName(playerStyle.educationalUseMoretags());
				tagsContainer.add(toolTipUc);
			}
			
		}
	}
	@UiHandler("plusAddTagsButton")
	public void onAddTagsBtnClicked(ClickEvent clickEvent) {
		addResourceTags();
	}
	
	public void addResourceTags(){
		if(AppClientFactory.isAnonymous()) {
			AppClientFactory.fireEvent(new InvokeLoginEvent());
		} else {
			AddTagesPopupView addTagesPopupView=new AddTagesPopupView(collectionItemDo.getResource().getGooruOid()) {
				public void getAddedResourceTags(){
					getUiHandlers().getResourceTagsToDisplay(collectionItemDo.getResource().getGooruOid());
				}
				@Override
				public void closePoup(boolean isCancelclicked) {
			        this.hide();
			        if(!isCancelclicked){
			        	 SuccessPopupViewVc success = new SuccessPopupViewVc() {

								@Override
								public void onClickPositiveButton(ClickEvent event) {
									this.hide();
								}
							};
							success.setGlassStyleName(PlayerBundle.INSTANCE.getPlayerStyle().resourceTagsGlassPanel());
							success.setHeight("253px");
							success.setWidth("450px");
							success.setPopupTitle(i18n.GL1795());
							success.setDescText(i18n.GL1796());
							success.enableTaggingImage();
							success.setPositiveButtonText(i18n.GL0190());
							success.center();
							success.show();
							success.getElement().getStyle().setZIndex(99999);
			        }
				}
			};
			addTagesPopupView.show();
			addTagesPopupView.setPopupPosition(addTagesPopupView.getAbsoluteLeft(),Window.getScrollTop()+10);
		}
	}

	@Override
	public void checkYoutubeAccessControls(Map<String, String> accessControls) {
		// TODO Auto-generated method stub
		String device = BrowserAgent.returnFormFactorView();
		if(accessControls!=null){
			if(accessControls.get(EMBED).equals("allowed")){
				if(accessControls.get(SYNDICATE).equals("allowed") || (device.equalsIgnoreCase("desktop"))){
					resourceWidgetContainer.add(new FlashAndVideoPlayerWidget(ResourceImageUtil.getYoutubeVideoId(collectionItemDo.getResource().getUrl()), collectionItemDo.getStart(), collectionItemDo.getStop()));
				}else {
					resourceWidgetContainer.add(new ResourceFrameBreakerView(collectionItemDo,false));
				}
				
			}else{
				resourceWidgetContainer.add(new ResourceFrameBreakerView(collectionItemDo,false));
			}
			
		}
	}
	
	public void clearMarginTop(){
		collectionContainer.getElement().getStyle().setMarginTop(0, Unit.PX);
	}
	public void setMarginTop(){
		collectionContainer.getElement().getStyle().setMarginTop(51, Unit.PX);
	}
	
	public class ReactionsMouseOverHandler implements MouseOverHandler{
		Button widget;
		String text;
		ReactionsMouseOverHandler(Button widget,String text){
			this.widget=widget;
			this.text=text;
			}

		@Override
		public void onMouseOver(MouseOverEvent event) {
			
			toolTipPopupPanel.clear();
			toolTipPopupPanel.setWidget(new GlobalToolTip(text));
			toolTipPopupPanel.setStyleName("");
			toolTipPopupPanel.setPopupPosition(widget.getElement().getAbsoluteLeft()+18, widget.getElement().getAbsoluteTop()+10);
			toolTipPopupPanel.getElement().getStyle().setZIndex(999999);
			toolTipPopupPanel.show();
			
		}
	}
	
	public class ReactionsMouseOutHandler implements MouseOutHandler {

		@Override
		public void onMouseOut(MouseOutEvent event) {
     		toolTipPopupPanel.hide();
		}
		
	}
	
}
