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
package org.ednovo.gooru.client.mvp.play.collection.preview.home;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BaseViewWithHandlers;
import org.ednovo.gooru.client.mvp.play.collection.event.UpdateCollectionViewCountEvent;
import org.ednovo.gooru.client.mvp.play.collection.event.UpdatePreviewViewCountEvent;
import org.ednovo.gooru.client.mvp.play.collection.preview.PreviewPlayerPresenter;
import org.ednovo.gooru.client.mvp.play.collection.preview.home.assign.AssignPopupPlayerVc;
import org.ednovo.gooru.client.mvp.play.collection.preview.home.customize.RenameCustomizePopUp;
import org.ednovo.gooru.client.mvp.play.collection.preview.home.share.SharePlayerVc;
import org.ednovo.gooru.client.uc.PlayerBundle;
import org.ednovo.gooru.client.uc.PreviewResourceView;
import org.ednovo.gooru.client.uc.tooltip.GlobalToolTip;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.client.util.PlayerDataLogEvents;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.content.CollectionDo;
import org.ednovo.gooru.shared.model.content.CollectionItemDo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
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

public class PreviewHomeView extends BaseViewWithHandlers<PreviewHomeUiHandlers> implements IsPreviewHomeView{
	
	@UiField Image collectionImage,collectionThumbnail;
	@UiField HTML collectionGoal;
	@UiField Button previewButton,viewCollectionSummaryBtn,backToClasspageButton;
	@UiField Label previousButton,nextButton,resourceCountTitle,learningobjectiveText;
	@UiField FlowPanel resourceCurosalContainer,collectionImageContainer,collectionEndImageContainer,thumbnailContainer;
	@UiField Button assignCollectionBtn,customizeCollectionBtn,shareCollectionBtn;
	@UiField FlowPanel previewButtonConatainer;
	@UiField InlineHTML separationLine;
	@UiField HTMLPanel endTextContainer,endText;
	
	private boolean isAssignPopup = false;
	
	private boolean isCustomizePopup = false;
	
	private boolean isSharePopup = false;
	
	private static PreviewHomeViewUiBinder uiBinder = GWT.create(PreviewHomeViewUiBinder.class);
	
	private PopupPanel toolTipPopupPanel=new PopupPanel();
	private HandlerRegistration previewClickHandler;
	private String collectionTitle;
	interface PreviewHomeViewUiBinder extends UiBinder<Widget, PreviewHomeView> {
	}
	
	private MessageProperties i18n = GWT.create(MessageProperties.class);
	
	@Inject
	public PreviewHomeView(){
		setWidget(uiBinder.createAndBindUi(this));
		previewButton.setText(i18n.GL0633());
		previewButton.getElement().setId("btnPreviewButton");
		previewButton.getElement().setAttribute("alt",i18n.GL0633());
		previewButton.getElement().setAttribute("title",i18n.GL0633());
		
		assignCollectionBtn.setText(i18n.GL0526());
		assignCollectionBtn.getElement().setId("btnAsignCollectionBtn");
		assignCollectionBtn.getElement().setAttribute("alt",i18n.GL0526());
		assignCollectionBtn.getElement().setAttribute("title",i18n.GL0526());
		
		customizeCollectionBtn.setText(i18n.GL2037());
		customizeCollectionBtn.getElement().setId("btnCustomizeCollectionBtn");
		customizeCollectionBtn.getElement().setAttribute("alt",i18n.GL2037());
		customizeCollectionBtn.getElement().setAttribute("title",i18n.GL2037());
		
		shareCollectionBtn.setText(i18n.GL0526());
		shareCollectionBtn.getElement().setId("btnShareCollectionBtn");
		shareCollectionBtn.getElement().setAttribute("alt",i18n.GL0526());
		shareCollectionBtn.getElement().setAttribute("title",i18n.GL0526());
		
		learningobjectiveText.setText(i18n.GL0618());
		learningobjectiveText.getElement().setId("lblLearningobjectiveText");
		learningobjectiveText.getElement().setAttribute("alt",i18n.GL0618());
		learningobjectiveText.getElement().setAttribute("title",i18n.GL0618());
		
		backToClasspageButton.setText(i18n.GL1631());
		backToClasspageButton.getElement().setId("btnBackToClasspageButton");
		backToClasspageButton.getElement().setAttribute("alt",i18n.GL1631());
		backToClasspageButton.getElement().setAttribute("title",i18n.GL1631());
		
		assignCollectionBtn.addMouseOverHandler(new OnassignCollectionBtnMouseOver());
		assignCollectionBtn.addMouseOutHandler(new OnassignCollectionBtnMouseOut());
		customizeCollectionBtn.addMouseOverHandler(new OncustomizeCollectionBtnMouseOver());
		customizeCollectionBtn.addMouseOutHandler(new OncustomizeCollectionBtnMouseOut());
		shareCollectionBtn.addMouseOverHandler(new OnshareCollectionBtnMouseOver());
		shareCollectionBtn.addMouseOutHandler(new OnshareCollectionBtnMouseOut());
		collectionEndImageContainer.setVisible(false);
		endTextContainer.setVisible(false);
		viewCollectionSummaryBtn.setVisible(false);
		
		
		collectionImageContainer.getElement().setId("fpnlCollectionImageContainer");
		collectionImage.getElement().setId("imgCollectionImage");
		collectionEndImageContainer.getElement().setId("fpnlCollectionEndImageContainer");
		collectionThumbnail.getElement().setId("imgCollectionThumbnail");
		thumbnailContainer.getElement().setId("fpnlThumbnailContainer");
		endTextContainer.getElement().setId("pnlEndTextContainer");
		endText.getElement().setId("pnlEndText");
		viewCollectionSummaryBtn.getElement().setId("btnViewCollectionSummaryBtn");
		previewButtonConatainer.getElement().setId("fpnlPreviewButtonConatainer");
		collectionGoal.getElement().setId("htmlCollectionGoal");
		resourceCountTitle.getElement().setId("lblResourceCountTitle");
		previousButton.getElement().setId("lblPreviousButton");
		resourceCurosalContainer.getElement().setId("fpnlResourceCurosalContainer");
		nextButton.getElement().setId("lblNextButton");
		
	}
	@UiHandler("collectionImage")
	public void thumbnailErrorImage(ErrorEvent event){
		collectionImage.setUrl("images/collection-default-thubnail.png");
	}
	
	@Override
	public void setCollectionMetadata(final CollectionDo collectionDo){
		setCollectionImage(collectionDo.getThumbnails().getUrl());
		setCollectionEndImage(collectionDo.getThumbnails().getUrl());
		setCollectionGoal(collectionDo.getGoals());
		collectionTitle = collectionDo.getTitle();
		assignCollectionBtn.getElement().setAttribute("collectionId", collectionDo.getGooruOid());
		customizeCollectionBtn.getElement().setAttribute("collectionId", collectionDo.getGooruOid());
		shareCollectionBtn.getElement().setAttribute("collectionId", collectionDo.getGooruOid());
		if(previewClickHandler!=null) {
			previewClickHandler.removeHandler();
		}
		previewClickHandler=previewButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Map<String,String> params = new LinkedHashMap<String,String>();
				params.put("id", collectionDo.getGooruOid());
				params = PreviewPlayerPresenter.setConceptPlayerParameters(params);
				
				MixpanelUtil.Preview_Player_Click_Preview();
				List<CollectionItemDo> collectionItems=collectionDo.getCollectionItems();
				if(AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equals(PlaceTokens.PREVIEW_PLAY)){
					AppClientFactory.fireEvent(new UpdatePreviewViewCountEvent());
				}else{
					AppClientFactory.fireEvent(new UpdateCollectionViewCountEvent());
				}
				if(collectionItems.size()>0){
					CollectionItemDo collectionItemDo=collectionItems.get(0);
					if(collectionItemDo.getCollectionItemId() != null)
					{
						if(collectionItemDo.getNarration()!=null&&!collectionItemDo.getNarration().trim().equals("")){
							params.put("rid", collectionItemDo.getCollectionItemId());
							params.put("tab", "narration");
							PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(AppClientFactory.getCurrentPlaceToken(), params);
							AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, true);
						}else{
							params.put("rid", collectionItemDo.getCollectionItemId());
							PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(AppClientFactory.getCurrentPlaceToken(), params);
							AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, true);
						}
					}

					
				}
				else{
					params.put("view", "end");
					PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(AppClientFactory.getCurrentPlaceToken(), params);
					AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, true);
				}
			}
		});
		setReplyLink();
		
	}
	public void setCollectionImage(String thumbnailUrl){
		collectionImage.setUrl(thumbnailUrl);
	}
	public void setCollectionGoal(String collectionGoal){
		this.collectionGoal.setHTML("");
		if(collectionGoal!=null &&!collectionGoal.isEmpty()){
			if(collectionGoal.length()>415){
				collectionGoal =(collectionGoal.substring(0, 415))+"...";
				this.collectionGoal.setHTML(collectionGoal);
			}
			else{
				this.collectionGoal.setHTML(collectionGoal);
			}
		}else{
			this.collectionGoal.setHTML(i18n.GL1374());
		}
	}
	@Override
	public void setCollectionResources(CollectionDo collectionDo) {
		resourceCurosalContainer.clear();
		showPopupAfterGmailSignin();
		int resourceCount=0;
		int questionCount=0;
		FlowPanel curosalPanel=new FlowPanel();
		int resourcesSize=collectionDo.getCollectionItems()!=null?collectionDo.getCollectionItems().size():0;
		if(resourcesSize>0){
			previousButton.setVisible(true);
			nextButton.setVisible(true);
			for(int itemIndex=0;itemIndex<resourcesSize;itemIndex++){
				if(collectionDo.getCollectionItems().get(itemIndex).getResource().getResourceFormat()!=null){
					if(collectionDo.getCollectionItems().get(itemIndex).getResource().getResourceFormat().getDisplayName().equalsIgnoreCase("Question")){
						questionCount++;
					}else{
						resourceCount++;
					}
				}
				PreviewResourceView previewResourceView=new PreviewResourceView(collectionDo.getCollectionItems().get(itemIndex), itemIndex);
				previewResourceView.addClickHandler(new ClickHandler() { 
					@Override
					public void onClick(ClickEvent event) {
						MixpanelUtil.Preview_Click_Resource();
					}
				});
				curosalPanel.add(previewResourceView);
			}
			if(resourcesSize>4){
				new ResourceCurosal(nextButton, previousButton, curosalPanel, resourcesSize, 145,null);
			}
			String resourceString = resourceCount == 1? resourceCount + " " + i18n.GL1110().toLowerCase() : resourceCount + " " + i18n.GL0174().toLowerCase();
			String questionString = questionCount == 1? questionCount + " " + i18n.GL0308().toLowerCase() : questionCount + " " + i18n.GL1042().toLowerCase();
			String finalMessage = "";
			if (resourceCount >0 && questionCount > 0){
				finalMessage = resourceString + " " + i18n.GL_GRR_AND() + " " + questionString + " " + i18n.GL0578() + i18n.GL_SPL_SEMICOLON()+" ";
			}else if (resourceCount >0){
				finalMessage = resourceString + " " + i18n.GL0578() + i18n.GL_SPL_SEMICOLON()+" ";
			}else if (questionCount >0){
				finalMessage = questionString + " " + i18n.GL0578() + i18n.GL_SPL_SEMICOLON()+" ";
			}else{
			
			}
			resourceCountTitle.setText(finalMessage);
			resourceCountTitle.getElement().setAttribute("alt",finalMessage);
			resourceCountTitle.getElement().setAttribute("title",finalMessage);
			resourceCurosalContainer.add(curosalPanel);
		}
		else
		{
			resourceCountTitle.setText(i18n.GL0684());
			resourceCountTitle.getElement().setAttribute("alt",i18n.GL0684());
			resourceCountTitle.getElement().setAttribute("title",i18n.GL0684());
			Image resourceThumbnail = new Image();
			resourceThumbnail.setUrl("images/resource_trans.png");
			resourceThumbnail.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().noResourceDefault());
			previousButton.setVisible(false);
			nextButton.setVisible(false);
			resourceCurosalContainer.add(resourceThumbnail);
		}
		
//http://127.0.0.1:8888/index.html?gwt.codesvr=127.0.0.1:9997#collection-play&id=f9b1b4ca-c784-46bc-9f49-f7d7c5e1d5c1
	}
	
	/**
	 * 
	 * @function onassignCollectionBtnClicked 
	 * 
	 * @created_date : 11-Dec-2013
	 * 
	 * @description
	 * 
	 * 
	 * @parm(s) : @param clickEvent
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 */
	@UiHandler("assignCollectionBtn")
	public void onassignCollectionBtnClicked(ClickEvent clickEvent) {
		MixpanelUtil.Preview_Click_Assign();
		   String collectionId = clickEvent.getRelativeElement().getAttribute("collectionId");
		    System.out.println("assign-collectionId:"+collectionId);

				if(!isAssignPopup){
					isAssignPopup=true;
				AssignPopupPlayerVc successPopupVc = new AssignPopupPlayerVc(collectionId) {
					
					@Override
					public void closePoup() {
						Window.enableScrolling(true);
				        this.hide();
				    	isAssignPopup=false;
					}
				};
				Window.scrollTo(0, 0);
				successPopupVc.setWidth("500px");
				successPopupVc.setHeight("635px");

				successPopupVc.show();
				successPopupVc.center();
				if (AppClientFactory.isAnonymous()){
				successPopupVc.setPopupPosition(successPopupVc.getAbsoluteLeft(), 30);
				}
				else
				{
				successPopupVc.setPopupPosition(successPopupVc.getAbsoluteLeft(), 30);
				}
				
				Map<String,String> params = new HashMap<String,String>();
				params.put("id", AppClientFactory.getPlaceManager().getRequestParameter("id"));
				if(AppClientFactory.getPlaceManager().getRequestParameter("subject")!=null)
					params.put("subject", AppClientFactory.getPlaceManager().getRequestParameter("subject"));
				if(AppClientFactory.getPlaceManager().getRequestParameter("lessonId")!=null)
					params.put("lessonId", AppClientFactory.getPlaceManager().getRequestParameter("lessonId"));
				params.put("assign", "yes");
				PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(PlaceTokens.PREVIEW_PLAY, params);
				AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, true);
				
			}
		
	}
	/**
	 * 
	 * @function oncustomizeCollectionBtnClicked 
	 * 
	 * @created_date : 11-Dec-2013
	 * 
	 * @description
	 * 
	 * 
	 * @parm(s) : @param clickEvent
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	@UiHandler("customizeCollectionBtn")
	public void oncustomizeCollectionBtnClicked(ClickEvent clickEvent) {
		MixpanelUtil.Preview_Click_Customize();
		String collectionId = clickEvent.getRelativeElement().getAttribute("collectionId");
		if(!isCustomizePopup){
			isCustomizePopup=true;
			Boolean loginFlag = false;
			if (AppClientFactory.isAnonymous()){
				loginFlag = true;
			}
			else
			{
				loginFlag = false;
			}
			RenameCustomizePopUp successPopupVc = new RenameCustomizePopUp(collectionId, loginFlag, collectionTitle) {

				@Override
				public void closePoup() {
					Window.enableScrolling(true);
					this.hide();	
					isCustomizePopup = false;
				}
			};
			Window.scrollTo(0, 0);
			successPopupVc.setWidth("500px");
			successPopupVc.setHeight("440px");
			successPopupVc.show();
			successPopupVc.center();
			
			Map<String,String> params = new HashMap<String,String>();
			params.put("id", AppClientFactory.getPlaceManager().getRequestParameter("id"));
			if(AppClientFactory.getPlaceManager().getRequestParameter("subject")!=null)
				params.put("subject", AppClientFactory.getPlaceManager().getRequestParameter("subject"));
			if(AppClientFactory.getPlaceManager().getRequestParameter("lessonId")!=null)
				params.put("lessonId", AppClientFactory.getPlaceManager().getRequestParameter("lessonId"));
			params.put("customize", "yes");
			PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(PlaceTokens.PREVIEW_PLAY, params);
			AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, true);
		}
		
	}
	/**
	 * 
	 * @function oncustomizeCollectionBtnClicked 
	 * 
	 * @created_date : 11-Dec-2013
	 * 
	 * @description
	 * 
	 * 
	 * @parm(s) : @param clickEvent
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	@UiHandler("shareCollectionBtn")
	public void onshareCollectionBtnClicked(ClickEvent clickEvent) {
		MixpanelUtil.Preview_Click_Share();
		final String collectionId = clickEvent.getRelativeElement().getAttribute("collectionId");

				if(!isSharePopup){
					isSharePopup=true;

				SharePlayerVc successPopupVc = new SharePlayerVc(collectionId) {

					@Override
					public void closePoup() {
						Window.enableScrolling(true);
						this.hide();	
						isSharePopup = false;
					}
					public void triggerShareEvent(String shareType,boolean confirmStatus){
						getUiHandlers().triggerCollectionShareDataEvent(collectionId,PlayerDataLogEvents.COLLECTION,shareType,confirmStatus);
					}
				};
				Window.scrollTo(0, 0);
				successPopupVc.setWidth("500px");
				successPopupVc.setHeight("350px");
				successPopupVc.show();
				successPopupVc.center();
			}
		
	}
	
	/**
	 * 
	 * Showing Customize or Assign popup after login with gmail account.
	 * 
	 */
	
	private void showPopupAfterGmailSignin() {
		// TODO Auto-generated method stub
		String collectionId = AppClientFactory.getPlaceManager().getRequestParameter("id")!=null ? AppClientFactory.getPlaceManager().getRequestParameter("id") : null;
		String customize = AppClientFactory.getPlaceManager().getRequestParameter("customize")!=null ? AppClientFactory.getPlaceManager().getRequestParameter("customize") : null;
		String assign = AppClientFactory.getPlaceManager().getRequestParameter("Assign")!=null ? AppClientFactory.getPlaceManager().getRequestParameter("Assign") : null;
		if(customize!=null && customize.equals("yes")){
			Boolean loginFlag = false;
			if (AppClientFactory.isAnonymous()){
				loginFlag = true;
			}
			else
			{
				loginFlag = false;
			}
			RenameCustomizePopUp successPopupVc = new RenameCustomizePopUp(collectionId, loginFlag, collectionTitle) {

				@Override
				public void closePoup() {
					Window.enableScrolling(true);
					this.hide();	
					isCustomizePopup = false;
				}
			};
			Window.scrollTo(0, 0);
			successPopupVc.setWidth("500px");
			successPopupVc.setHeight("440px");
			successPopupVc.show();
			successPopupVc.center();
		}
		if(assign!=null && assign.equals("yes")){
			AssignPopupPlayerVc successPopupVc = new AssignPopupPlayerVc(collectionId) {
				
				@Override
				public void closePoup() {
					Window.enableScrolling(true);
			        this.hide();
			    	isAssignPopup=false;
				}
			};
			Window.scrollTo(0, 0);
			successPopupVc.setWidth("500px");
			successPopupVc.setHeight("635px");

			successPopupVc.show();
			successPopupVc.center();
			if (AppClientFactory.isAnonymous()){
			successPopupVc.setPopupPosition(successPopupVc.getAbsoluteLeft(), 30);
			}
			else
			{
			successPopupVc.setPopupPosition(successPopupVc.getAbsoluteLeft(), 30);
			}
		}


	}

	
	public class OnassignCollectionBtnMouseOver implements MouseOverHandler{

		@Override
		public void onMouseOver(MouseOverEvent event) {
			toolTipPopupPanel.clear();
			toolTipPopupPanel.setWidget(new GlobalToolTip(i18n.GL0676()));
			toolTipPopupPanel.setStyleName("");
			toolTipPopupPanel.setPopupPosition(assignCollectionBtn.getElement().getAbsoluteLeft()+8, assignCollectionBtn.getElement().getAbsoluteTop()+10);
			toolTipPopupPanel.getElement().getStyle().setZIndex(999999);
			toolTipPopupPanel.show();
		}
		
	}
	
	public class OnassignCollectionBtnMouseOut implements MouseOutHandler{

		@Override
		public void onMouseOut(MouseOutEvent event) {
			toolTipPopupPanel.hide();
		}
	}
	
	public class OncustomizeCollectionBtnMouseOver implements MouseOverHandler{

		@Override
		public void onMouseOver(MouseOverEvent event) {
			toolTipPopupPanel.clear();
			toolTipPopupPanel.setWidget(new GlobalToolTip(i18n.GL0677()));
			toolTipPopupPanel.setStyleName("");
			toolTipPopupPanel.setPopupPosition(customizeCollectionBtn.getElement().getAbsoluteLeft()+18, customizeCollectionBtn.getElement().getAbsoluteTop()+10);
			toolTipPopupPanel.getElement().getStyle().setZIndex(999999);
			toolTipPopupPanel.show();
		}
		
	}
	
	public class OncustomizeCollectionBtnMouseOut implements MouseOutHandler{

		@Override
		public void onMouseOut(MouseOutEvent event) {
			toolTipPopupPanel.hide();
		}

		
		
	}
	
	public class OnshareCollectionBtnMouseOver implements MouseOverHandler{

		@Override
		public void onMouseOver(MouseOverEvent event) {
			toolTipPopupPanel.clear();
			toolTipPopupPanel.setWidget(new GlobalToolTip(i18n.GL0678()));
			toolTipPopupPanel.setStyleName("");
			toolTipPopupPanel.setPopupPosition(shareCollectionBtn.getElement().getAbsoluteLeft()+5, shareCollectionBtn.getElement().getAbsoluteTop()+10);
			toolTipPopupPanel.getElement().getStyle().setZIndex(999999);
			toolTipPopupPanel.show();
		}
		
	}
	
	public class OnshareCollectionBtnMouseOut implements MouseOutHandler{
		@Override
		public void onMouseOut(MouseOutEvent event) {
			toolTipPopupPanel.hide();
		}
	}
	
	public void removeAssignmentButtons(){
		collectionImageContainer.setVisible(true);
		previewButton.setVisible(true);
		collectionEndImageContainer.setVisible(false);
		endTextContainer.setVisible(false);
		viewCollectionSummaryBtn.setVisible(false);
		assignCollectionBtn.removeFromParent();
		customizeCollectionBtn.removeFromParent();
		shareCollectionBtn.removeFromParent();
		separationLine.removeFromParent();
		previewButton.setText(i18n.GL0594());
		//addHrTag();
	}
	
	public void removeAssignmentImageButtons(){
		collectionEndImageContainer.setVisible(true);
		endTextContainer.setVisible(true);
		String page=AppClientFactory.getPlaceManager().getRequestParameter("page", null);
		if(page!=null&&page.equals("teach")){
			viewCollectionSummaryBtn.setVisible(false);
		}else{
			viewCollectionSummaryBtn.setVisible(true);
		}
		endText.getElement().setInnerHTML(i18n.GL0596());
		viewCollectionSummaryBtn.setText("View your Collection Summary");
		collectionImageContainer.setVisible(false);
		previewButton.setVisible(false);
		assignCollectionBtn.removeFromParent();
		customizeCollectionBtn.removeFromParent();
		shareCollectionBtn.removeFromParent();
		separationLine.removeFromParent();
		
		//addHrTag();
	}
	
	@UiHandler("collectionThumbnail")
	public void thumbnailEndPageErrorImage(ErrorEvent event){
		collectionThumbnail.setUrl("images/default-collection-image-160x120.png");
	}
	
	public void setCollectionEndImage(String thumbnailUrl){
		collectionThumbnail.setUrl(thumbnailUrl);
	}
	
	public void setReplyLink(){
		Anchor resourceAnchor=new Anchor();
		resourceAnchor.setHref(getReplayLink());
		resourceAnchor.setStyleName("");
		resourceAnchor.addClickHandler(new ReplayCollectionEvent());
		HTMLPanel collectionHTMLPanel = new HTMLPanel("");
		collectionHTMLPanel.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().collectionreplay());
		
		Label collectionReplayButton=new Label(i18n.GL0632());
		collectionReplayButton.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().collectionreplayText());
		
		collectionHTMLPanel.add(collectionReplayButton);
		
		resourceAnchor.getElement().appendChild(collectionHTMLPanel.getElement());
		thumbnailContainer.clear();
		thumbnailContainer.add(resourceAnchor);
	}
	public String getReplayLink(){
		String collectionId=AppClientFactory.getPlaceManager().getRequestParameter("id", null);
		String viewToken=AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken();
		String resourceLink="#"+viewToken+"&id="+collectionId;
		resourceLink += PreviewPlayerPresenter.setConceptPlayerParameters();
		return resourceLink;
	}
		
	@UiHandler("viewCollectionSummaryBtn")
	public void onviewCollectionSummaryBtnClicked(ClickEvent clickEvent) {
		getUiHandlers().scrollStudyPageEndPage();
	}
	public Button getBackToClassButton(){
		return backToClasspageButton;
	}
	
	private class ReplayCollectionEvent implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			getUiHandlers().resetCollectionActivityEventId();
		}
	}
	
}
