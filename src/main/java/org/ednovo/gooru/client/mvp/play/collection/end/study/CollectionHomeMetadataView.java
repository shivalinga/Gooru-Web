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
package org.ednovo.gooru.client.mvp.play.collection.end.study;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BaseViewWithHandlers;
import org.ednovo.gooru.client.mvp.home.library.assign.AssignPopupVc;
import org.ednovo.gooru.client.mvp.play.collection.event.UpdateCollectionViewCountEvent;
import org.ednovo.gooru.client.mvp.play.collection.event.UpdatePreviewViewCountEvent;
import org.ednovo.gooru.client.mvp.play.collection.preview.PreviewPlayerPresenter;
import org.ednovo.gooru.client.mvp.play.collection.preview.home.assign.AssignPopupPlayerVc;
import org.ednovo.gooru.client.mvp.play.collection.preview.home.customize.RenameCustomizePopUp;
import org.ednovo.gooru.client.mvp.play.collection.preview.home.share.SharePlayerVc;
import org.ednovo.gooru.client.uc.PlayerBundle;
import org.ednovo.gooru.client.uc.tooltip.GlobalToolTip;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.client.util.PlayerDataLogEvents;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.content.CollectionDo;
import org.ednovo.gooru.shared.model.content.CollectionItemDo;
import org.ednovo.gooru.shared.util.StringUtil;

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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

public class CollectionHomeMetadataView extends BaseViewWithHandlers<CollectionHomeMetadataUiHandlers> implements IsCollectionHomeMetadataView{
	
	@UiField Image collectionThumbnail;
	@UiField HTML collectionGoal;
	@UiField Button customizeCollectionBtn,shareCollectionBtn,studyButton;
	/*@UiField HTMLPanel learningobjectiveText;*/
	/*@UiField FlowPanel thumbnailContainer;*/
	
	
	private boolean isAssignPopup = false;
	
	private boolean isCustomizePopup = false;
	
	private boolean isSharePopup = false;
	
	private PopupPanel toolTipPopupPanel=new PopupPanel();
	
	private HandlerRegistration studyButtonClickHandler;
	
	private String collectionTitle;
	
	private CollectionDo collectionDo=null;
	
	private static PreviewEndViewUiBinder uiBinder = GWT.create(PreviewEndViewUiBinder.class);

	interface PreviewEndViewUiBinder extends UiBinder<Widget, CollectionHomeMetadataView> {
	}
	
	private MessageProperties i18n = GWT.create(MessageProperties.class);
	
	@Inject
	public CollectionHomeMetadataView(){
		setWidget(uiBinder.createAndBindUi(this));
//		assignCollectionBtn.setText(i18n.GL0104());
//		assignCollectionBtn.getElement().setId("btnAssignCollectionBtn");
//		assignCollectionBtn.getElement().setAttribute("alt",i18n.GL0104());
//		assignCollectionBtn.getElement().setAttribute("title",i18n.GL0104());
		
		customizeCollectionBtn.setText(i18n.GL2037());
		customizeCollectionBtn.getElement().setId("btnCustomizeCollectionBtn");
		customizeCollectionBtn.getElement().setAttribute("alt",i18n.GL2037());
		customizeCollectionBtn.getElement().setAttribute("title",i18n.GL2037());
		
		shareCollectionBtn.setText(i18n.GL0536());
		shareCollectionBtn.getElement().setId("btnShareCollectionBtn");
		shareCollectionBtn.getElement().setAttribute("alt",i18n.GL0536());
		shareCollectionBtn.getElement().setAttribute("title",i18n.GL0536());
		
				
		collectionThumbnail.getElement().setId("imgCollectionThumbnail");
		//thumbnailContainer.getElement().setId("fpnlThumbnailContainer");
		collectionGoal.getElement().setId("htmlCollectionGoal");
		
		studyButton.setText(i18n.GL0594());
		studyButton.getElement().setId("btnStudyCollectionBtn");
		studyButton.getElement().setAttribute("alt",i18n.GL0594());
		studyButton.getElement().setAttribute("title",i18n.GL0594());
		
		
//		assignCollectionBtn.addMouseOverHandler(new OnassignCollectionBtnMouseOver());
//		assignCollectionBtn.addMouseOutHandler(new OnassignCollectionBtnMouseOut());
		customizeCollectionBtn.addMouseOverHandler(new OncustomizeCollectionBtnMouseOver());
		customizeCollectionBtn.addMouseOutHandler(new OncustomizeCollectionBtnMouseOut());
		shareCollectionBtn.addMouseOverHandler(new OnshareCollectionBtnMouseOver());
		shareCollectionBtn.addMouseOutHandler(new OnshareCollectionBtnMouseOut());
	}
	@UiHandler("collectionThumbnail")
	public void thumbnailErrorImage(ErrorEvent event){
		collectionThumbnail.setUrl("images/default-collection-image-160x120.png");
	}
	
	@Override
	public void setCollectionMetadata(final CollectionDo collectionDo){
		setCollectionImage(collectionDo.getThumbnails().getUrl());
		setCollectionGoal(collectionDo.getGoals());
		showPopupAfterGmailSignin();
		//assignCollectionBtn.getElement().setAttribute("collectionId", collectionDo.getGooruOid());
		customizeCollectionBtn.getElement().setAttribute("collectionId", collectionDo.getGooruOid());
		shareCollectionBtn.getElement().setAttribute("collectionId", collectionDo.getGooruOid());
		//setReplyLink();
		collectionTitle = collectionDo.getTitle();
		this.collectionDo=collectionDo;
		if(studyButtonClickHandler!=null) {
			studyButtonClickHandler.removeHandler();
		}
		studyButtonClickHandler=studyButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Map<String,String> params = new LinkedHashMap<String,String>();
				params.put("id", collectionDo.getGooruOid());
				params = PreviewPlayerPresenter.setConceptPlayerParameters(params);
				
				MixpanelUtil.Preview_Player_Click_Preview();
				List<CollectionItemDo> collectionItems=collectionDo.getCollectionItems();
				if(AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equals(PlaceTokens.COLLECTION_PLAY)){
					AppClientFactory.fireEvent(new UpdateCollectionViewCountEvent());
				}else{
					AppClientFactory.fireEvent(new UpdatePreviewViewCountEvent());
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
	}
	public void setReplyLink(){
		Anchor resourceAnchor=new Anchor();
		resourceAnchor.setHref(getReplayLink());
		resourceAnchor.setStyleName("");
		HTMLPanel collectionHTMLPanel = new HTMLPanel("");
		resourceAnchor.addClickHandler(new ReplayCollectionEvent());
		collectionHTMLPanel.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().collectionreplay());
		Label collectionReplayButton=new Label(i18n.GL0632());
		collectionReplayButton.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().collectionreplayText());
		collectionHTMLPanel.add(collectionReplayButton);
		resourceAnchor.getElement().appendChild(collectionHTMLPanel.getElement());
		//thumbnailContainer.clear();
		//thumbnailContainer.add(resourceAnchor);
	}
	
	private class ReplayCollectionEvent implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			getUiHandlers().resetCollectionActivityEventId();
		}
	}
	public String getReplayLink(){
		String collectionId=AppClientFactory.getPlaceManager().getRequestParameter("id", null);
		String viewToken=AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken();
		String resourceLink="#"+viewToken+"&id="+collectionId;
		resourceLink += PreviewPlayerPresenter.setConceptPlayerParameters();
		return resourceLink;
	}
	public void setCollectionImage(String thumbnailUrl){
		collectionThumbnail.setUrl(thumbnailUrl);
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
//	@UiHandler("assignCollectionBtn")
//	public void onassignCollectionBtnClicked(ClickEvent clickEvent) {
//		String collectionId = clickEvent.getRelativeElement().getAttribute("collectionId");
//
//				if(!isAssignPopup){
//					isAssignPopup=true;
//				AssignPopupPlayerVc successPopupVc = new AssignPopupPlayerVc(collectionId) {
//					
//					@Override
//					public void closePoup() {
//						Window.enableScrolling(true);
//				        this.hide();
//				    	isAssignPopup=false;
//					}
//				};
//				Window.scrollTo(0, 0);
//				successPopupVc.setWidth("500px");
//				successPopupVc.setHeight("635px");
//
//				successPopupVc.show();
//				successPopupVc.center();
//				if (AppClientFactory.isAnonymous()){
//				successPopupVc.setPopupPosition(successPopupVc.getAbsoluteLeft(), 30);
//				}
//				else
//				{
//				successPopupVc.setPopupPosition(successPopupVc.getAbsoluteLeft(), 30);
//				}
//				
//			}
//		
//	}

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
				RenameCustomizePopUp successPopupVc = new RenameCustomizePopUp(collectionId, loginFlag,collectionTitle) {

					@Override
					public void closePoup() {
//						Window.enableScrolling(true);
						this.hide();	
						isCustomizePopup = false;
					}
				};
				Window.scrollTo(0, 0);
				successPopupVc.setWidth("500px");
				successPopupVc.setHeight("471px");
				successPopupVc.show();
				successPopupVc.center();
				
				Map<String,String> params = new HashMap<String,String>();
				params.put("id", AppClientFactory.getPlaceManager().getRequestParameter("id"));
				if(AppClientFactory.getPlaceManager().getRequestParameter("subject")!=null)
					params.put("subject", AppClientFactory.getPlaceManager().getRequestParameter("subject"));
				if(AppClientFactory.getPlaceManager().getRequestParameter("lessonId")!=null)
					params.put("lessonId", AppClientFactory.getPlaceManager().getRequestParameter("lessonId"));
				params.put("customize", "yes");
				PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(PlaceTokens.COLLECTION_PLAY, params);
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
		final Map<String, String> params = StringUtil.splitQuery(Window.Location.getHref());
		String collectionId = collectionDo.getGooruOid();
				//	Window.enableScrolling(false);
				//final Map<String,String> params = new HashMap<String,String>();
			AssignPopupVc successPopupVc = new AssignPopupVc(collectionId, collectionDo.getTitle(), collectionDo.getGoals()) {
					@Override
					public void closePoup() {
						Window.enableScrolling(true);
				        this.hide();
				    	params.remove("assign");
				    	PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(AppClientFactory.getCurrentPlaceToken(), params);
						AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, true);
					}
				};
				//Window.scrollTo(0, 0);
				int clientHeight=Window.getClientHeight();
				//successPopupVc.setWidth("500px");
				//successPopupVc.setHeight("658px");
				/*if(clientHeight>625){
					clientHeight=625;
					successPopupVc.getAssignContainer().getElement().setAttribute("style", "max-height:"+clientHeight+"px;width:500px;overflow-x:hidden;overflow-y:scroll");
				}else{
					successPopupVc.getAssignContainer().getElement().setAttribute("style", "max-height:"+clientHeight+"px;width:500px;overflow-x:hidden;overflow-y:scroll");
				}*/
				successPopupVc.show();
				int left = (Window.getClientWidth() - 500) >> 1;
			    int top = (Window.getClientHeight() - clientHeight) >> 1;
			    successPopupVc.setHeight("658px");
			    successPopupVc.setPopupPosition(Math.max(Window.getScrollLeft() + left, 0), Math.max(Window.getScrollTop()+5, 0));

				//successPopupVc.center();
				//successPopupVc.setPopupPosition(successPopupVc.getAbsoluteLeft(), 10);
				
//				if(!successPopupVc.isVisible()){
//					successPopupVc.show();
//					successPopupVc.center();
//				}
//				Window.enableScrolling(false);
//				if (AppClientFactory.isAnonymous()){
//					successPopupVc.setPopupPosition(successPopupVc.getAbsoluteLeft(), 10);
//				}
//				else{
					//successPopupVc.setPopupPosition(successPopupVc.getAbsoluteLeft(), 10);
//				}
				params.put("assign", "yes");
				PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(AppClientFactory.getCurrentPlaceToken(), params);
				AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, true);

		
//		final String collectionId = clickEvent.getRelativeElement().getAttribute("collectionId");
//
//				if(!isSharePopup){
//					isSharePopup=true;
//
//				SharePlayerVc successPopupVc = new SharePlayerVc(collectionId) {
//
//					@Override
//					public void closePoup() {
//						Window.enableScrolling(true);
//						this.hide();	
//						isSharePopup = false;
//					}
//					public void triggerShareEvent(String shareType,boolean confirmStatus){
//						getUiHandlers().triggerCollectionShareDataEvent(collectionId,PlayerDataLogEvents.COLLECTION,shareType,confirmStatus);
//					}
//				};
//				Window.scrollTo(0, 0);
//				successPopupVc.setWidth("500px");
//				successPopupVc.setHeight("350px");
//				successPopupVc.show();
//				successPopupVc.center();
//			}
		
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
		String assign = AppClientFactory.getPlaceManager().getRequestParameter("assign")!=null ? AppClientFactory.getPlaceManager().getRequestParameter("assign") : null;
		String emailId = AppClientFactory.getPlaceManager().getRequestParameter("emailId")!=null ? AppClientFactory.getPlaceManager().getRequestParameter("emailId") : null;
		if(customize!=null && customize.equals("yes") && emailId!=null){
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
			successPopupVc.setHeight("471px");
			successPopupVc.show();
			successPopupVc.center();
		}
		if(assign!=null && assign.equals("yes") && emailId!=null){
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

	
//	public class OnassignCollectionBtnMouseOver implements MouseOverHandler{
//
//		@Override
//		public void onMouseOver(MouseOverEvent event) {
//			toolTipPopupPanel.clear();
//			toolTipPopupPanel.setWidget(new GlobalToolTip(i18n.GL0676()));
//			toolTipPopupPanel.setStyleName("");
//			toolTipPopupPanel.setPopupPosition(assignCollectionBtn.getElement().getAbsoluteLeft()+8, assignCollectionBtn.getElement().getAbsoluteTop()+10);
//			toolTipPopupPanel.getElement().getStyle().setZIndex(999999);
//			toolTipPopupPanel.show();
//		}
//		
//	}
	
//	public class OnassignCollectionBtnMouseOut implements MouseOutHandler{
//
//		@Override
//		public void onMouseOut(MouseOutEvent event) {
//			toolTipPopupPanel.hide();
//		}
//
//		
//		
//	}
	
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
		
}
