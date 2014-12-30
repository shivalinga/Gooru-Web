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
package org.ednovo.gooru.client.mvp.play.collection.preview.metadata.comment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.child.ChildView;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.play.collection.event.EditCommentChildViewEvent;
import org.ednovo.gooru.client.mvp.play.collection.event.UpdateCommentChildViewEvent;
import org.ednovo.gooru.client.mvp.play.collection.preview.metadata.comment.events.SetCommentsOptionsEvent;
import org.ednovo.gooru.client.mvp.play.collection.preview.metadata.comment.events.SetCommentsOptionsHandler;
import org.ednovo.gooru.client.uc.ConfirmationPopupVc;
import org.ednovo.gooru.client.uc.HTMLEventPanel;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.content.CollectionDo;
import org.ednovo.gooru.shared.model.player.CommentsDo;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class CommentWidgetChildView extends ChildView<CommentWidgetChildPresenter> implements IsCommentWidgetView{

	@UiField HTMLPanel authorBadge, editButton, messageInfo,tooltipDeletetext;
	
	@UiField Label userName, timestamp, successPostMsg,characterLimit;
	
	@UiField HTML commentHtml;
	
	@UiField HTMLEventPanel editPanel, deletePanel, deleteButton;
	
	@UiField Image userPhoto;
	
	@UiField TextArea commentField;
	
	@UiField Button postCommentBtn, postCommentCancel;
	
	private String commentUid;
	
	private static final String DELETE = "DELETE";
	
	private static final String EDIT = "EDIT";
	
	private static final String PNG = ".png";

	private static final String PRIMARY_STYLE = "primary";
	
	private static final String SECONDARY_STYLE = "secondary";
	
	private static final String DISABLED_STYLE = "disabled";
	
	private static final String CONTENT_ADMIN_ROLE = "Content_Admin";
	
	private static boolean CHECK_COLLOBORATOR =false;
	
	private List<String> getPermissionsList = null;
	
	private static final String EDUCATOR_DEFAULT_IMG = "../images/settings/setting-user-image.png";
	
	private static final String DATE_FORMAT="MMMM dd, yyyy";
	
	private String collectionOwnerUid = null;
	private String commentOwnerUid = null;
	private String loggedInOwnerUid = null;
	
	private CommentsDo commentsDo;
	private CollectionDo collectionDo;
	
	private DeleteConfirmationPopupVc deleteConfirmationPopupVc;
	
	private static CommentWidgetChildViewUiBinder uiBinder = GWT.create(CommentWidgetChildViewUiBinder.class);

	interface CommentWidgetChildViewUiBinder extends UiBinder<Widget, CommentWidgetChildView> {}
	
	private MessageProperties i18n = GWT.create(MessageProperties.class);

	public CommentWidgetChildView(CommentsDo commentsDo, CollectionDo collectionDo) {
		initWidget(uiBinder.createAndBindUi(this));
		enableEditFunction(false);
		this.commentsDo = commentsDo;
		this.collectionDo = collectionDo;
		successPostMsg.setVisible(false);
		commentField.addKeyUpHandler(new ValidateConfirmText());
		commentField.addBlurHandler(new OnCommentsFieldBlur());
		authorBadge.getElement().setInnerHTML(i18n.GL0573());
		authorBadge.getElement().setId("pnlAuthorBadge");
		authorBadge.getElement().setAttribute("alt",i18n.GL0573());
		authorBadge.getElement().setAttribute("title",i18n.GL0573());
		
		editPanel.getElement().setInnerHTML(i18n.GL0140());
		editPanel.getElement().setId("epnlEditPanel");
		editPanel.getElement().setAttribute("alt",i18n.GL0140());
		editPanel.getElement().setAttribute("title",i18n.GL0140());
		
		deletePanel.getElement().setInnerHTML(i18n.GL0558());
		deletePanel.getElement().setId("epnlDeletePanel");
		deletePanel.getElement().setAttribute("alt",i18n.GL0558());
		deletePanel.getElement().setAttribute("title",i18n.GL0558());
		
		tooltipDeletetext.getElement().setInnerHTML(i18n.GL0558());
		tooltipDeletetext.getElement().setId("epnlTooltipDeletetext");
		tooltipDeletetext.getElement().setAttribute("alt",i18n.GL0558());
		tooltipDeletetext.getElement().setAttribute("title",i18n.GL0558());
		
		postCommentBtn.setText(i18n.GL0571());
		postCommentBtn.getElement().setId("btnPostCommentBtn");
		postCommentBtn.getElement().setAttribute("alt",i18n.GL0571());
		postCommentBtn.getElement().setAttribute("title",i18n.GL0571());
		
		postCommentCancel.setText(i18n.GL0142());
		postCommentCancel.getElement().setId("btnPostCommentCancel");
		postCommentCancel.getElement().setAttribute("alt",i18n.GL0142());
		postCommentCancel.getElement().setAttribute("title",i18n.GL0142());
		
		characterLimit.setText(i18n.GL0143());
		characterLimit.getElement().setId("lblCharacterLimit");
		characterLimit.getElement().setAttribute("alt",i18n.GL0143());
		characterLimit.getElement().setAttribute("title",i18n.GL0143());
		
		successPostMsg.setText(i18n.GL0570());
		successPostMsg.getElement().setId("lblSuccessPostMsg");
		successPostMsg.getElement().setAttribute("alt",i18n.GL0570());
		successPostMsg.getElement().setAttribute("title",i18n.GL0570());
		
		editButton.setVisible(false);
		deleteButton.setVisible(false);		
		AppClientFactory.getEventBus().addHandler(SetCommentsOptionsEvent.TYPE, setZindex);
		
		
		setCommentData(commentsDo, collectionDo);
		
		if(collectionDo.getSettings()!=null)
		{
			if(collectionDo.getSettings().getComment()!=null)
			{
	
				if(collectionDo.getSettings().getComment().equalsIgnoreCase("turn-on"))
				{
					editButton.setVisible(true);
				}
				else
				{
					editButton.setVisible(false);
				}
			}
		}
		
		userPhoto.getElement().setId("imgUserPhoto");
		messageInfo.getElement().setId("pnlMessageInfo");
		userName.getElement().setId("lblUserName");
		timestamp.getElement().setId("lblTimestamp");
		commentHtml.getElement().setId("htmlCommentHtml");
		editButton.getElement().setId("pnlEditButton");
		deleteButton.getElement().setId("epnlDeleteButton");
		commentField.getElement().setId("tatCommentField");
		StringUtil.setAttributes(commentField, true);
	}
	
	/*
	 * Event for options button to enable after successful login.
	 */
	SetCommentsOptionsHandler setZindex = new SetCommentsOptionsHandler() {
		
		@Override
		public void setOptions() {
			setOptionsButtons();
		}
	};
	
	/**
	 * 
	 * @param collectionDo
	 * 
	 * @function setCommentData 
	 * 
	 * @created_date : 02-Jan-2014
	 * 
	 * @description
	 * 
	 * @parm(s) : @param i
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	private void setCommentData(CommentsDo commentsDo, CollectionDo collectionDo) {
		//Added try catch because "commentsDo.getCommentorUid().getGooruUId()" is null.
		getPermissionsList = new ArrayList<String>();
		characterLimit.setVisible(false);
		try{
			setCommentUid(commentsDo.getCommentUid());
			collectionOwnerUid = collectionDo.getUser().getGooruUId() !=null ? collectionDo.getUser().getGooruUId() : null;
			commentOwnerUid = commentsDo.getCommentorUid().getGooruUId() != null ? commentsDo.getCommentorUid().getGooruUId() : null;
			loggedInOwnerUid = AppClientFactory.getLoggedInUser().getGooruUId();
			CHECK_COLLOBORATOR = collectionDo.getMeta().isIsCollaborator();
			 if(collectionDo.getMeta().getPermissions()!=null){
			getPermissionsList = collectionDo.getMeta().getPermissions();
			 }
			userPhoto.setUrl(AppClientFactory.loggedInUser.getSettings().getProfileImageUrl()+commentOwnerUid+PNG);
			userPhoto.addErrorHandler(new ErrorHandler() {
				@Override
				public void onError(ErrorEvent event) {
					userPhoto.setUrl(EDUCATOR_DEFAULT_IMG);
				}
			});
			setMixPanelEvent(collectionOwnerUid, loggedInOwnerUid);
			userName.setText(commentsDo.getCommentorUid().getUsername());
			if(collectionOwnerUid.equalsIgnoreCase(commentOwnerUid)) {
				authorBadge.setVisible(true);
			} else {
				authorBadge.setVisible(false);
			}
			String commentTime = getCreatedTime(commentsDo.getCreatedOn());

			if (commentsDo.getLastModifiedOn() !=null){
				commentTime = commentTime+ " "+i18n.GL_GRR_Hyphen()+" "+i18n.GL1434();
			}
			
			timestamp.setText(commentTime); 
			timestamp.getElement().setAttribute("alt",commentTime);
			timestamp.getElement().setAttribute("title",commentTime);
			commentHtml.setHTML(commentsDo.getComment());
			commentHtml.getElement().setAttribute("alt",commentsDo.getComment());
			commentHtml.getElement().setAttribute("title",commentsDo.getComment());
			commentField.setText(commentsDo.getComment());
			commentField.getElement().setAttribute("alt",commentsDo.getComment());
			commentField.getElement().setAttribute("title",commentsDo.getComment());
			
			
			
			
			
			setOptionsButtons();
			
		}catch(Exception e){
			
		}
		
	}
	
	
	public void setOptionsButtons(){
		loggedInOwnerUid = AppClientFactory.getLoggedInUser().getGooruUId();
		if(!AppClientFactory.isAnonymous() && (commentOwnerUid.equalsIgnoreCase(loggedInOwnerUid))) {
			editButton.setVisible(true);
			deleteButton.setVisible(false);
		} else if(!AppClientFactory.isAnonymous() && (collectionOwnerUid.equalsIgnoreCase(loggedInOwnerUid))) {
			deleteButton.setVisible(true);
			editButton.setVisible(false);
		} else if(AppClientFactory.isAnonymous()){
			editButton.setVisible(false);
			deleteButton.setVisible(false);
		} else if(!AppClientFactory.isAnonymous() && AppClientFactory.getLoggedInUser().getUserRoleSetString().contains(CONTENT_ADMIN_ROLE)){
			deleteButton.setVisible(true);
			editButton.setVisible(false);
		}else if(!AppClientFactory.isAnonymous() && getPermissionsList!=null && getPermissionsList.toString().contains("edit")){
				deleteButton.setVisible(false);
				editButton.setVisible(true);
		}
	}
	

	/** 
	 * This method is to get the commentUid
	 */
	public String getCommentUid() {
		return commentUid;
	}

	/** 
	 * This method is to set the commentUid
	 */
	public void setCommentUid(String commentUid) {
		this.commentUid = commentUid;
	}
	
	@UiHandler("deletePanel")
	public void clickOnDeletePanel(ClickEvent event) {
		deleteConfirmationPopupVc=new DeleteConfirmationPopupVc(i18n.GL0558(),i18n.GL0559());
	}
	
	@UiHandler("deleteButton")
	public void clickOnDeleteButton(ClickEvent event) {
		deleteConfirmationPopupVc=new DeleteConfirmationPopupVc(i18n.GL0558(),i18n.GL0559());
	}
	
	@UiHandler("editPanel")
	public void clickOnEditPanel(ClickEvent event) {
		AppClientFactory.fireEvent(new UpdateCommentChildViewEvent(getCommentUid(),EDIT));
		enableEditFunction(true);
	}
	
	@UiHandler("postCommentCancel")
	public void clickOnPostCommentCancel(ClickEvent event) {
    	commentHtml.setHTML(commentsDo.getComment());
    	commentHtml.getElement().setAttribute("alt",commentsDo.getComment());
		commentHtml.getElement().setAttribute("title",commentsDo.getComment());
    	commentField.setText(commentsDo.getComment());
    	commentField.getElement().setAttribute("alt",commentsDo.getComment());
    	commentField.getElement().setAttribute("title",commentsDo.getComment());
		AppClientFactory.fireEvent(new UpdateCommentChildViewEvent("",EDIT));
		enableEditFunction(false);
		characterLimit.setVisible(false);
	}
	
	@UiHandler("postCommentBtn")
	public void clickOnPostCommentBtn(ClickEvent event) {
		if (commentField.getText().length() > 0){
			Map<String, String> parms = new HashMap<String, String>();
			parms.put("text", removeHtmlTags(commentField.getText()));
			AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {

				@Override
				public void onSuccess(Boolean value) {
					boolean isHavingBadWords = value;
					if (isHavingBadWords){
						commentField.getElement().getStyle().setBorderColor("orange");
						characterLimit.setText(i18n.GL0554());
						characterLimit.setVisible(true);
					}else{
						commentField.getElement().getStyle().clearBackgroundColor();
						commentField.getElement().getStyle().setBorderColor("#ccc");
						AppClientFactory.fireEvent(new EditCommentChildViewEvent(getCommentUid(),removeHtmlTags(commentField.getText()),EDIT));
				    	displaySuccessMsg(true);
						Timer timer = new Timer(){
				            @Override
				            public void run()
				            {
				            	commentsDo.setComment(removeHtmlTags(commentField.getText()));
				            	commentHtml.setHTML(commentsDo.getComment());
				            	String commentTime = getCreatedTime(commentsDo.getCreatedOn());
				    			commentTime = commentTime+ " "+i18n.GL_GRR_Hyphen()+" "+i18n.GL1434();
				    			timestamp.setText(commentTime);
				    			timestamp.getElement().setAttribute("alt",commentTime);
				    			timestamp.getElement().setAttribute("title",commentTime);
				            	successPostMsg.setVisible(false);
				            	enableEditFunction(false);
				            }
				        };
				        timer.schedule(1000);
						characterLimit.setVisible(false);						
					}
				}
			});
		}else{
			//Ask user to delete the comment.
			commentHtml.setHTML(commentsDo.getComment());
			deleteConfirmationPopupVc=new DeleteConfirmationPopupVc(i18n.GL0558(),i18n.GL0559());
			enableEditFunction(true);
		}
	}		
	public void enableEditFunction(boolean isVisible) {
		messageInfo.setVisible(!isVisible);
		commentField.setVisible(isVisible);
		postCommentBtn.setVisible(isVisible);
		postCommentCancel.setVisible(isVisible);
	}

	public void displaySuccessMsg(boolean isVisible) {
		commentField.setVisible(!isVisible);
		successPostMsg.setVisible(isVisible);
	}
	
	public TextArea getCommentField() {
		return commentField;
	}
	/**
	 * 
	 * @fileName : CommentWidgetChildView.java
	 * 
	 * @function : OnCommentsFieldBlur
	 *
	 * @description : Validation for profanity check on validation.
	 *
	 *
	 * @version : 1.0
	 *
	 * @date: Jan 6, 2014
	 *
	 * @Author Gooru Team
	 *
	 * @Reviewer: Gooru Team
	 */
	private class OnCommentsFieldBlur implements BlurHandler{

		/* (non-Javadoc)
		 * @see com.google.gwt.event.dom.client.BlurHandler#onBlur(com.google.gwt.event.dom.client.BlurEvent)
		 */
		@Override
		public void onBlur(BlurEvent event) {
			if (commentField.getText().length() > 0){
				
				Map<String, String> parms = new HashMap<String, String>();
				parms.put("text", commentField.getText());
				AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {
	
					@Override
					public void onSuccess(Boolean value) {
						if (value){
							commentField.getElement().getStyle().setBorderColor("orange");
							characterLimit.setText(i18n.GL0554());
							characterLimit.setVisible(true);
						}else{
							commentField.getElement().getStyle().clearBackgroundColor();
							commentField.getElement().getStyle().setBorderColor("#ccc");
							characterLimit.setVisible(false);						
						}
					}
				});
			}
		}		
	}
	
	/**
	 * @fileName : PreviewPlayerMetadataView.java
	 *
	 * @description : OnCommentsFieldValidated Sub class
	 *
	 * @version : 1.0
	 *
	 * @date: 03-Jan-2014
	 *
	 * @Author: Gooru Team
	 *
	 * @Reviewer: Gooru Team
	 */
	private class ValidateConfirmText implements KeyUpHandler {
		@Override
		public void onKeyUp(KeyUpEvent event) {
			if(commentField.getText().length()>415) {
				commentField.setText(commentField.getText().substring(0,415));
				characterLimit.setText(i18n.GL0143());
				characterLimit.setVisible(true);
			} else {
				if(commentField.getText().trim().length()==0){
					modifyEditControls(false);
				}else{
					modifyEditControls(true);
				}
				characterLimit.setVisible(false);
			}
		}
	}
	
	private class DeleteConfirmationPopupVc extends ConfirmationPopupVc{
		public DeleteConfirmationPopupVc(String messageHeader,String messageContent){
			super(messageHeader, messageContent);
			setPopupZindex(100000);
			setGlassZindex(100000);
			setDeleteData(i18n.GL0560(), i18n.GL0558());
			setScrollDisable();
		}
		@Override
		public void onDelete(ClickEvent clickEvent) {
			Timer timer = new Timer()
	        {
	            @Override
	            public void run()
	            {
	            	AppClientFactory.fireEvent(new UpdateCommentChildViewEvent(getCommentUid(),DELETE));		            
	            	deleteConfirmationPopupVc.hide();
	            }
	        };
	        timer.schedule(500);
		}
		public void hide() {
			 super.hide();
		}
	}
	
	/**
	 * @function getCreatedTime 
	 * 
	 * @created_date : 06-Jan-2014
	 * 
	 * @description
	 * 
	 * @parm(s) : @param commentCreatedTime
	 * 
	 * @return : String
	 */
	private String getCreatedTime(String commentCreatedTime) {
		String createdTime = null;
		Long currentTime = System.currentTimeMillis();
		Long commentTime = Long.parseLong(commentCreatedTime);
		Long elapsedTime = (currentTime - commentTime);
		int seconds = (int) (elapsedTime / 1000) % 60 ;
		int minutes = (int) ((elapsedTime / (1000*60)) % 60);
		int hours   = (int) ((elapsedTime / (1000*60*60)) % 24);
		int days = (int) (elapsedTime / (1000*60*60*24));
		Date currentDate = new Date(commentTime);
		DateTimeFormat fmt = DateTimeFormat.getFormat (DATE_FORMAT);
		if(days>6){
			createdTime = fmt.format (currentDate);
		}
		else if(days>0&&days<=6) {
			createdTime = days + getTimePrefix(days," "+i18n.GL0562(), i18n.GL0579(), i18n.GL0580());
		} else if(hours>0&&hours<24) {
			createdTime = hours + getTimePrefix(hours," "+i18n.GL0563(), i18n.GL1435(), i18n.GL1436());
		} else if(minutes>0&&minutes<60) {
			createdTime = minutes + getTimePrefix(minutes," "+i18n.GL0564(), i18n.GL1437(), i18n.GL1438());
		} else if(seconds<=60) {
			createdTime = i18n.GL0561();
		}
		return createdTime;
	}
	
	/**
	 * @function getTimePrefix 
	 * 
	 * @created_date : 06-Jan-2014
	 * 
	 * @description
	 * 
	 * @parm(s) : @param count
	 * @parm(s) : @param msg
	 * @parm(s) : @param regex
	 * @parm(s) : @param replacement
	 * 
	 * @return : String
	 *
	 */
	private String getTimePrefix(int count, String msg, String regex, String replacement) {
		if(count==1) {
			msg = msg.replaceAll(regex, replacement);
		}
		return msg;
	}	
	/**
	 * @function modifyEditControls 
	 * 
	 * @created_date : 03-Jan-2014
	 * 
	 * @description
	 * 
	 * @parm(s) : @param isCommentsFieldClicked
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	private void modifyEditControls(boolean isCommentsFieldClicked) {
		postCommentCancel.setVisible(isCommentsFieldClicked);
		if(isCommentsFieldClicked) {
			postCommentBtn.removeStyleName(SECONDARY_STYLE);
			postCommentBtn.removeStyleName(DISABLED_STYLE);
			postCommentBtn.addStyleName(PRIMARY_STYLE);
		} else {
			postCommentBtn.removeStyleName(PRIMARY_STYLE);
			postCommentBtn.addStyleName(SECONDARY_STYLE);
			postCommentBtn.addStyleName(DISABLED_STYLE);
		}
	}
	
	private void setMixPanelEvent(String collectionAuthor, String loggedInUser) {
		if(collectionAuthor.equalsIgnoreCase(loggedInUser)) {
			MixpanelUtil.mixpanelEvent("Preview_Author_Comments");
		} else {
			MixpanelUtil.mixpanelEvent("Preview_User_Comments");
		}
	}
	
	/**
	 * @function removeHtmlTags 
	 * 
	 * @created_date : 10-Nov-2014
	 * 
	 * @description this method is used to remove the html tags in comment input box
	 * 
	 * @parm(s) : @param String 
	 * 
	 * @return : String
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	private String removeHtmlTags(String html){
		html = html.replaceAll("(<\\w+)[^>]*(>)", "$1$2");
		html = html.replaceAll("</p>", " ").replaceAll("<p>", "").replaceAll("<br data-mce-bogus=\"1\">", "").replaceAll("<br>", "").replaceAll("</br>", "").replaceAll("</a>", "").replaceAll("<a>", "");
        return html;
	}


	public HTMLPanel getEditButton() {
		return editButton;
	}


	public void setEditButton(HTMLPanel editButton) {
		this.editButton = editButton;
	}
	
	
}