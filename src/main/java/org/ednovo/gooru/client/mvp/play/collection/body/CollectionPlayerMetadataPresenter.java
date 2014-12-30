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
package org.ednovo.gooru.client.mvp.play.collection.body;


import java.util.ArrayList;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.play.collection.CollectionPlayerPresenter;
import org.ednovo.gooru.client.mvp.play.collection.end.study.CollectionHomeMetadataPresenter;
import org.ednovo.gooru.client.mvp.play.collection.event.EditCommentChildViewEvent;
import org.ednovo.gooru.client.mvp.play.collection.event.SetPlayerLoginStatusEvent;
import org.ednovo.gooru.client.mvp.play.collection.event.UpdateCommentChildViewEvent;
import org.ednovo.gooru.client.mvp.play.collection.preview.end.PreviewEndPresenter;
import org.ednovo.gooru.client.mvp.play.collection.preview.home.PreviewHomePresenter;
import org.ednovo.gooru.client.service.LibraryServiceAsync;
import org.ednovo.gooru.client.service.PlayerAppServiceAsync;
import org.ednovo.gooru.client.uc.PlayerBundle;
import org.ednovo.gooru.client.util.PlayerDataLogEvents;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.content.ClasspageItemDo;
import org.ednovo.gooru.shared.model.content.CollectionDo;
import org.ednovo.gooru.shared.model.content.ContentReportDo;
import org.ednovo.gooru.shared.model.library.ConceptDo;
import org.ednovo.gooru.shared.model.player.CommentsDo;
import org.ednovo.gooru.shared.model.player.CommentsListDo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Button;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PresenterWidget;

public class CollectionPlayerMetadataPresenter extends PresenterWidget<IsCollectionPlayerMetadataView> implements CollectionPlayerMetadataUiHandlers{
	
	@Inject
	private PlayerAppServiceAsync playerAppService;
	
	@Inject
	private LibraryServiceAsync libraryService;

    private SimpleAsyncCallback<CommentsListDo> commentsListDoAsync;
    
	private CollectionDo collectionDo=null;
	
	private CollectionPlayerPresenter collectionPlayerPresenter=null;
	
	private PreviewHomePresenter previewHomePresenter;
	
	private PreviewEndPresenter previewEndPresenter;
	
	private CollectionHomeMetadataPresenter collectionHomeMetadataPresenter;
	
	public static final  Object METADATA_PRESENTER_SLOT = new Object();
	
	private static final String PAGE = "course-page";
	
	private static final String CREATE = "CREATE";
	
	private static final String EDIT = "EDIT";
	
	private static final String FEATCHINGCOMMENT = "FEATCHINGCOMMENT";
	
	private static final String INITIAL_COMMENT_LIMIT = "10";
	
	private static final String INITIAL_OFFSET = "0";
	
	private MessageProperties i18n = GWT.create(MessageProperties.class);
	
	@Inject
	public CollectionPlayerMetadataPresenter(EventBus eventBus, IsCollectionPlayerMetadataView view,PreviewHomePresenter previewHomePresenter,
			PreviewEndPresenter previewEndPresenter,CollectionHomeMetadataPresenter collectionHomeMetadataPresenter) {
		super(eventBus, view);
		getView().setUiHandlers(this);
		this.previewHomePresenter=previewHomePresenter;
		this.previewEndPresenter=previewEndPresenter;
		this.collectionHomeMetadataPresenter=collectionHomeMetadataPresenter;
		addRegisteredHandler(SetPlayerLoginStatusEvent.TYPE, this);
		//addRegisteredHandler(EditCommentChildViewEvent.TYPE, this);
	}
	
	public void setCollectionMetadata(final CollectionDo collectionDo){
		this.collectionDo=collectionDo;
		getView().setCollectionMetadata(collectionDo);
		if(AppClientFactory.isAnonymous()) {
			getView().setPlayerLoginStatus(false);
		} else {
			getView().setPlayerLoginStatus(true);
		}
		getCollectionCommentsList(collectionDo.getGooruOid(), INITIAL_OFFSET, INITIAL_COMMENT_LIMIT);
		setRelatedConcepts(collectionDo);
	}
	
	public void displayAuthorDetails(boolean isDisplayDetails){
		getView().displayAuthorDetails(isDisplayDetails);
	}
	public void setPreviewHomePresenter(){
		previewHomePresenter.setCollectionMetadata(collectionDo);
		previewHomePresenter.removeAssignmentButtons();
		setInSlot(METADATA_PRESENTER_SLOT, previewHomePresenter,false);
	}
	public void setPreviewEndPresenter(){
		previewEndPresenter.setCollectionMetadata(collectionDo);
		setInSlot(METADATA_PRESENTER_SLOT, previewEndPresenter,false);
	}
	
	public void setStudyEndPage(){
		previewHomePresenter.setCollectionMetadata(collectionDo);
		previewHomePresenter.removeAssignmentImagetButtons(); 
		setInSlot(METADATA_PRESENTER_SLOT, previewHomePresenter,false);
	}
	
	public void setCollectionHomeMetadata(){
		collectionHomeMetadataPresenter.setCollectionMetadata(collectionDo);
		setInSlot(METADATA_PRESENTER_SLOT, collectionHomeMetadataPresenter,false);
	}
	
	public void setViewCount(String viewCount){
		getView().setViewCount(viewCount);
	}
	
	public void updateLikesCount(int likesCount){
		//getView().setLikesCount(likesCount);
	}
	
	public void setUserProfileName(String gooruUid){
		getView().setUserProfileName(gooruUid);
	}
	
	public void resetMetadataFields(){
		getView().resetMetadataFields();
	}

	public PlayerAppServiceAsync getPlayerAppService() {
		return playerAppService;
	}

	public void setPlayerAppService(PlayerAppServiceAsync playerAppService) {
		this.playerAppService = playerAppService;
	}
	
	public SimpleAsyncCallback<CommentsListDo>  getCommentsListDoAsync() {
		return commentsListDoAsync;
	}

	public void setCommentsListDoAsync(SimpleAsyncCallback<CommentsListDo> commentsListDoAsync) {
		this.commentsListDoAsync = commentsListDoAsync;
	}
	

	@Override
	public void setPlayerLoginStatusHandler(boolean isLoggedIn) {
		getView().setPlayerLoginStatus(isLoggedIn);
		if(collectionPlayerPresenter!=null){
			collectionPlayerPresenter.updateHeaderView();
		}
	}
	
	private void getCollectionCommentsList(String gooruOid, final String offset, String limit) {
		this.playerAppService.getCollectionCommentsList(gooruOid, offset, limit, new SimpleAsyncCallback<CommentsListDo>() {
			@Override
			public void onSuccess(CommentsListDo commentDoList) {
				getView().setCommentsData(commentDoList,collectionDo, Integer.parseInt(offset) >0 ? false : true);
			}
		});
	}

	@Override
	public void createCommentForCollection(String gooruOid, String comment) {
		collectionPlayerPresenter.triggerCommentDataLogEvent(null, PlayerDataLogEvents.COMMENT_CREATE, comment);
		this.playerAppService.createCommentForCollection(gooruOid, comment, new SimpleAsyncCallback<CommentsDo>() {
			@Override
			public void onSuccess(CommentsDo commentsDo) {
				getView().setCommentsWidget(commentsDo, CREATE);
				getView().displaySuccessMsg(false);
			}
		});
	}

	
	public void updateCommentChildView(String commentUid, String action) {
		getView().updateCommentChildView(commentUid, action);
	}

	@Override
	public void deleteCommentFromCollection(final String gooruOid,String commentUid,final String offset, final String limit,String commentText) {
		collectionPlayerPresenter.triggerCommentDataLogEvent(commentUid, PlayerDataLogEvents.COMMENT_DELETE,commentText);
		this.playerAppService.deleteCollectionCommentbyCommentUid(commentUid, new SimpleAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void noResult) {
				if(Integer.parseInt(offset)>=9)
				insertCommentAfterDeletion(gooruOid, offset, limit);
			}
		});
	}
	public void insertCommentAfterDeletion(final String gooruOid,final String offset, String limit){
		this.playerAppService.getCollectionCommentsList(gooruOid, offset, limit, new SimpleAsyncCallback<CommentsListDo>() {
			@Override
			public void onSuccess(CommentsListDo commentDoList) {
				getView().setCommentsWidget(commentDoList.getSearchResults().get(0), FEATCHINGCOMMENT);
			}
		});
	}
	
	public void editCommentChildView(String commentUid, String commentText, String action) {
		collectionPlayerPresenter.triggerCommentDataLogEvent(commentUid, PlayerDataLogEvents.COMMENT_EDIT,commentText);
		this.playerAppService.updateCollectionCommentbyCommentUid(commentUid, commentText, new SimpleAsyncCallback<CommentsDo>() {
			@Override
			public void onSuccess(CommentsDo result) {
				getView().updateCommentChildView("", EDIT);
			}
		});
	}

	@Override
	public void getPaginationResults(String gooruOid, String offset, String limit) {
		getCollectionCommentsList(gooruOid, offset, limit);
	}

	
	
	public void getFlagedReport(String gooruOid) {
		playerAppService.getContentReport(collectionDo.getGooruOid(), AppClientFactory.getGooruUid(), new SimpleAsyncCallback<ArrayList<ContentReportDo>>() {
			@Override
			public void onSuccess(ArrayList<ContentReportDo> result) {
				String gooruFlagId="";
				if(result.size()==0){
					getView().getFlagButton().setText(i18n.GL0556());
					getView().getFlagButton().removeStyleName(PlayerBundle.INSTANCE.getPlayerStyle().previewCoverFlagImageOrange());
					getView().getFlagButton().setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().playerPreviewCoverFlagImage());
				}else{
					for(int i =0;i<result.size();i++){
						gooruFlagId = gooruFlagId+result.get(i).getDeleteContentGooruOid();
						getView().getFlagButton().setText(i18n.GL0557());
						getView().getFlagButton().removeStyleName(PlayerBundle.INSTANCE.getPlayerStyle().playerPreviewCoverFlagImage());
						getView().getFlagButton().setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().previewCoverFlagImageOrange());
						
					}
			}
			}
		});
		
	}

	public void setRelatedConcepts(CollectionDo collectionDo) {
		final String subject = AppClientFactory.getPlaceManager().getRequestParameter("subject");
		final String lessonId = AppClientFactory.getPlaceManager().getRequestParameter("lessonId", "123");
		final String libraryType = AppClientFactory.getPlaceManager().getRequestParameter("library", PlaceTokens.DISCOVER);
		
		if(subject!=null) {
			this.libraryService.getLibraryCollections(subject, lessonId, libraryType, new SimpleAsyncCallback<ArrayList<ConceptDo>>() {
				@Override
				public void onSuccess(ArrayList<ConceptDo> conceptDoList) {
					getView().isConceptsContainerVisible(true);
					getView().setRelatedConceptsContent(conceptDoList, PAGE, subject, lessonId, libraryType);
				}
			});
		} else {
			getView().isConceptsContainerVisible(false);
		}
	}

	public void setCollectionDoOnRefresh(CollectionDo collectionDo) { 
		this.collectionDo = collectionDo;
		getView().setCollectionMetadata(collectionDo);
		setRelatedConcepts(collectionDo);
	}

	public CollectionPlayerPresenter getCollectionPlayerPresenter() {
		return collectionPlayerPresenter;
	}

	public void setCollectionPlayerPresenter(CollectionPlayerPresenter collectionPlayerPresenter) {
		this.collectionPlayerPresenter = collectionPlayerPresenter;
		collectionHomeMetadataPresenter.setCollectionPlayerPresenter(collectionPlayerPresenter);
		previewHomePresenter.setCollectionPlayerPresenter(collectionPlayerPresenter);
	}

	public void setTeacherInfo(ClasspageItemDo classpageItemDo) { 
		getView().setTeacherInfo(classpageItemDo);
	}

	public void setDataInsightsSummaryUrl(String sessionId){
		getView().setDataInsightsSummaryUrl(sessionId);
	}
	
	public void setDataInsightsUrl(){
		getView().setDataInsightsUrl();
	}
	public Button getBackToClassButton(){
		return previewHomePresenter.getBackToClassButton();
	}
	public void clearDashBoardIframe(){
		getView().clearDashBoardIframe();
	}
	public void setClasspageInsightsUrl(String classpageId, String sessionId){
		getView().setClasspageInsightsUrl(classpageId, sessionId);
	}
	public void hideTeacherInfo() { 
		getView().hideTeachPanelDetails(false);
	}
	@Override
	public void updateCommentsStatus(String commentsStatus){
		if(collectionPlayerPresenter!=null){
			collectionPlayerPresenter.updateCommentsStatus(commentsStatus);
		}
	}
	
	public void changeCommentsButton(CollectionDo collectionDoObj){
		getView().changeCommentsButton(collectionDoObj);
	}

}
	

