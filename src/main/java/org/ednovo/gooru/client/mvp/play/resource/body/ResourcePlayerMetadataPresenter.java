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


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.play.collection.CollectionPlayerPresenter;
import org.ednovo.gooru.client.mvp.play.collection.end.CollectionEndPresenter;
import org.ednovo.gooru.client.mvp.play.collection.preview.PreviewPlayerPresenter;
import org.ednovo.gooru.client.mvp.play.resource.ResourcePlayerPresenter;
import org.ednovo.gooru.client.mvp.play.resource.framebreaker.ResourceFrameBreakerView;
import org.ednovo.gooru.client.mvp.play.resource.question.QuestionResourcePresenter;
import org.ednovo.gooru.client.mvp.rating.RatingAndReviewPopupPresenter;
import org.ednovo.gooru.client.mvp.rating.events.DeletePlayerStarRatingsEvent;
import org.ednovo.gooru.client.mvp.rating.events.DeletePlayerStarReviewEvent;
import org.ednovo.gooru.client.mvp.rating.events.OpenReviewPopUpEvent;
import org.ednovo.gooru.client.mvp.rating.events.PostUserReviewEvent;
import org.ednovo.gooru.client.mvp.rating.events.PostUserReviewResourceEvent;
import org.ednovo.gooru.client.mvp.rating.events.PostUserReviewResourceEventHandler;
import org.ednovo.gooru.client.mvp.rating.events.UpdateUserStarReviewEvent;
import org.ednovo.gooru.client.util.PlayerDataLogEvents;
import org.ednovo.gooru.shared.model.content.CollectionDo;
import org.ednovo.gooru.shared.model.content.CollectionItemDo;
import org.ednovo.gooru.shared.model.content.ContentReportDo;
import org.ednovo.gooru.shared.model.content.ContentStarRatingsDo;
import org.ednovo.gooru.shared.model.content.ReactionDo;
import org.ednovo.gooru.shared.model.content.ResourceTagsDo;
import org.ednovo.gooru.shared.model.content.StarRatingsDo;
import org.ednovo.gooru.shared.util.ResourceImageUtil;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

public class ResourcePlayerMetadataPresenter extends PresenterWidget<IsResourcePlayerMetadataView> implements ResourcePlayerMetadataUiHandlers{
	
	private QuestionResourcePresenter questionResourcePresenter;
	
	private CollectionEndPresenter collectionEndPresenter;
	
	private CollectionPlayerPresenter collectionPlayerPresenter;
	
	private PreviewPlayerPresenter previewPlayerPresenter;
	
	private ResourcePlayerPresenter resourcePlayerPresenter;
	
	private boolean isCollectionPlayer=false;
	
	private boolean isResourcePlayer=false;
	
	private boolean isPreviewPlayer=false;
	
	private RatingAndReviewPopupPresenter ratingAndReviewPopup;

	private String resourceTitle = null;
	
	@Inject
	public ResourcePlayerMetadataPresenter(EventBus eventBus, IsResourcePlayerMetadataView view,QuestionResourcePresenter questionResourcePresenter,CollectionEndPresenter collectionEndPresenter,RatingAndReviewPopupPresenter ratingAndReviewPopup) {
		super(eventBus, view);
		this.questionResourcePresenter=questionResourcePresenter;
		//this.collectionEndPresenter=collectionEndPresenter;
		this.ratingAndReviewPopup = ratingAndReviewPopup;
		getView().setUiHandlers(this);
		addRegisteredHandler(OpenReviewPopUpEvent.TYPE, this);
		addRegisteredHandler(UpdateUserStarReviewEvent.TYPE,this);
		addRegisteredHandler(DeletePlayerStarReviewEvent.TYPE,this);
		addRegisteredHandler(DeletePlayerStarRatingsEvent.TYPE,this);
	}
	
	public void showResourceWidget(CollectionItemDo collectionItemDo){
		getView().showResourceWidget(collectionItemDo);
	}
	
	public void showResourceWidget(CollectionItemDo collectionItemDo,PlaceRequest nextResoruceRequest,PlaceRequest previousResourceRequest){
		getView().showResourceWidget(collectionItemDo,nextResoruceRequest,previousResourceRequest);
	}
	
	public void showResourceWidget(CollectionDo collectionDo,PlaceRequest previousResourceRequest){
		getView().showResourceWidget(previousResourceRequest);
		//collectionEndPresenter.showCollectionEndPreview(collectionDo,collectionPlayerPresenter.getAttemptAnswersMap());
		getView().getResourceWidgetContainer().clear();
		//getView().getResourceWidgetContainer().add(collectionEndPresenter.getWidget());
	}

	@Override
	public void showQuestionView(CollectionItemDo collectionItemDo) {
		questionResourcePresenter.setCollectionPlayer(isCollectionPlayer);
		questionResourcePresenter.setResourcePlayer(isResourcePlayer);
		questionResourcePresenter.setPreviewPlayer(isPreviewPlayer);
		questionResourcePresenter.setCollectionPlayerPresnter(collectionPlayerPresenter);
		questionResourcePresenter.setResourcePlayerPresenter(resourcePlayerPresenter);
		questionResourcePresenter.setPreviewPlayerPresenter(previewPlayerPresenter);
		questionResourcePresenter.showQuestionPreview(collectionItemDo);
		//removeUserAttemptResult();
		int windowHeight=Window.getClientHeight();
		if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.RESOURCE_PLAY)){
			questionResourcePresenter.getWidget().setHeight((windowHeight-116)+"px");
		}else{
			questionResourcePresenter.getWidget().setHeight((windowHeight-202)+"px");
		}
		getView().getResourceWidgetContainer().add(questionResourcePresenter.getWidget());
	}
	
	public void setCollectionPlayerPresnter(CollectionPlayerPresenter collectionPlayerPresenter,boolean isCollectionPlayer){
		this.collectionPlayerPresenter=collectionPlayerPresenter;
		//collectionEndPresenter.setCollectionPlayerPresenter(collectionPlayerPresenter);
		this.isCollectionPlayer=true;
		this.isResourcePlayer=false;
		this.isPreviewPlayer=false;
	}
	
	public void setPreviewPlayerPresenter(PreviewPlayerPresenter previewPlayerPresenter){
		this.previewPlayerPresenter=previewPlayerPresenter;
		this.isCollectionPlayer=false;
		this.isResourcePlayer=false;
		this.isPreviewPlayer=true;
	}
	
	public void setResourcePlayerPresenter(ResourcePlayerPresenter resourcePlayerPresenter,boolean isCollectionPlayer){
		this.resourcePlayerPresenter=resourcePlayerPresenter;
		this.isCollectionPlayer=false;
		this.isResourcePlayer=true;
		this.isPreviewPlayer=false;
	}
	
	public void removeUserAttemptResult(){
		if(isCollectionPlayer){
			collectionPlayerPresenter.removeUserAttemptResult();
		}else if(isResourcePlayer){
			resourcePlayerPresenter.removeUserAttemptResult();
		}else if(isPreviewPlayer){
			previewPlayerPresenter.removeUserAttemptResult();
		}
	}
	
	public boolean isOeAnswerSubmited(){
		boolean isOeAnswerSubmited=true;
		if(isCollectionPlayer){
			isOeAnswerSubmited=collectionPlayerPresenter.isOpenEndedAnswerSubmited();
		}else if(isResourcePlayer){
			isOeAnswerSubmited=resourcePlayerPresenter.isOpenEndedAnswerSubmited();
		}else if(isPreviewPlayer){
			isOeAnswerSubmited=previewPlayerPresenter.isOpenEndedAnswerSubmited();
		}
		return isOeAnswerSubmited;
	}
	public void  triggerCreateReactionEvent(String resourceId,String reactionType,String eventName){
		Long startTime=PlayerDataLogEvents.getUnixTime();
		if(isCollectionPlayer){
			collectionPlayerPresenter.triggerReactiontDataLogEvent(resourceId, startTime, startTime, reactionType,eventName);
		}else if(isResourcePlayer){
			//resourcePlayerPresenter.removeUserAttemptResult();
		}else if(isPreviewPlayer){
			previewPlayerPresenter.triggerReactiontDataLogEvent(resourceId, startTime, startTime, reactionType,eventName);
		}
	}
	public void saveReactionToShowAvg(String reactionText){
		if(isCollectionPlayer){
			collectionPlayerPresenter.saveReactionToShowAvg(reactionText);
		}
	}
	public void resetResourceMetaData(){
		getView().getResourceWidgetContainer().clear();
	}
	public void removeRatingContainer(boolean flag){
		getView().removeRatingContainer(flag);
	}
	@Override
	public void createReaction(String resourceId,final String reactionText,String gooruReactionId,String collectionId, String createStudyPlayerReaction) {
		AppClientFactory.getInjector().getPlayerAppService().createReaction(resourceId,reactionText,gooruReactionId,collectionId,createStudyPlayerReaction, new SimpleAsyncCallback<ReactionDo>() {
			@Override
			public void onSuccess(ReactionDo result) {
				saveReactionToShowAvg(reactionText);
				getView().setReaction(result,result.getDeleteReactionGooruOid()); 
			}
		});
	}

	/**
	 * Get reaction API is called and gets respective reaction.
	 * @param collectionItemDo {@link CollectionItemDo}
	 */
	public void setReaction(CollectionItemDo collectionItemDo) { 
		AppClientFactory.getInjector().getPlayerAppService().getResourceReaction(collectionItemDo.getResource().getGooruOid(),AppClientFactory.getGooruUid(), new SimpleAsyncCallback<ArrayList<ReactionDo>>() {
			@Override
			public void onSuccess(ArrayList<ReactionDo> result) {
				String gooruReactionId="";
				if(result.size()==0){
					getView().setDefaultReaction();
				}else{
					for(int i =0;i<result.size();i++){
						gooruReactionId = gooruReactionId+result.get(i).getDeleteReactionGooruOid();
						if(result.size()!=(i+1)){
							gooruReactionId=gooruReactionId+",";
						}
					}
					getView().setReaction(result.get(0),gooruReactionId); 
				}
				
			}
		});
	}

	@Override
	public void deleteReaction(String gooruReactionId) {
		AppClientFactory.getInjector().getPlayerAppService().deleteReaction(gooruReactionId,new SimpleAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				
			}
		});
	}
	
	public void updateFlagImageOnEndView(){
		//collectionEndPresenter.updateFlagImageOnEndView();
	}
	
	public void clearIfrmaeContent(){
		try {
			Element ifrmaeElement = Document.get().getElementById("resourcePlayerContainer");
			if (ifrmaeElement != null) {
				ifrmaeElement.setAttribute("src", "");
			}
		} catch (Exception exception) {
		}
	}
	public void createSessionAttemptTryWhenNavigation(int questionType){
		questionResourcePresenter.createSessionAttemptTryWhenNavigation(questionType);
	}
	
	
	/**
	 * API call to create the ratings based on the user input and triggers the API.
	 * 
	 * @param associateGooruOid {@link String}
	 * @param starRatingValue {@link Integer}
	 * @param clickEvent {@link ClickEvent}
	 */
	@Override
	public void createStarRatings(String associateGooruOid, int starRatingValue, final boolean showThankYouToolTip,String userReview,String resourceGooruId) {
		if(showThankYouToolTip){
			triggerCreateRatingEvent(resourceGooruId, starRatingValue, getView().getPreviousRating());
		}else{
			triggerCreateReviewEvent(resourceGooruId, userReview);
		}
		AppClientFactory.getInjector().getPlayerAppService().createStarRatings(associateGooruOid,starRatingValue,userReview,new SimpleAsyncCallback<StarRatingsDo>() {
			@Override
			public void onSuccess(StarRatingsDo result) { 
				getView().setUserStarRatings(result,showThankYouToolTip);
//				getView().updateRatingOnSearch(result); 
				
			}
		});
	}

	/**
	 * API call to set Ratings for a resource.
	 * 
	 * @param collectionItemDo {@link CollectionItemDo}
	 */
	public void setResourceStarRatings(CollectionItemDo collectionItemDo) { 
		
		AppClientFactory.getInjector().getPlayerAppService().getResourceRatingWithReviews(collectionItemDo.getResource().getGooruOid(), AppClientFactory.getGooruUid(),0, new SimpleAsyncCallback<ArrayList<StarRatingsDo>>() {

			@Override
			public void onSuccess(ArrayList<StarRatingsDo> result) {
				if(result.size()>0){
					getView().setUserStarRatings(result.get(0),false); 
				}else{
					getView().setUserStarRatings(null,false); 
				}
				
			}
		});
	}

	/**
	 * Receiving event to post the review. 
	 */
	
	public void postReview(String assocGooruOId, String userReview, Integer score,boolean isUpdate) {
		getView().postReview(assocGooruOId,userReview,score,isUpdate);	
	}
	
	
	public void postReviewForResource(String assocGooruOId, String userReview, Integer score,boolean isUpdate) {
		getView().postReview(assocGooruOId,userReview,score,isUpdate);	
	}

	/**
	 * API call to updats the Ratings for a resource.
	 * 
	 * @param gooruOid {@link String}
	 * @param starRatingValue {@link Integer}
	 * @param showThankYouToolTip {@link Boolean} 
	 */
	@Override
	public void updateStarRatings(String gooruOid, int starRatingValue,boolean showThankYouToolTip,String resourceGooruId) {
		triggerCreateRatingEvent(resourceGooruId, starRatingValue, getView().getPreviousRating());
		AppClientFactory.getInjector().getPlayerAppService().updateResourceStarRatings(gooruOid, starRatingValue, new SimpleAsyncCallback<ArrayList<StarRatingsDo>>(){

			@Override
			public void onSuccess(ArrayList<StarRatingsDo> result) {
				if(result.size()>0){
					getView().setUserStarRatings(result.get(0),true); 
				}
			}
		}); 
	}
	
	
	@Override
	public void createCollectionContentReport(String associatedGooruOid,String freeText,ArrayList<String> contentReportList,String deleteContentReportGooruOids) {
		AppClientFactory.getInjector().getPlayerAppService().createContentReport(associatedGooruOid, freeText, contentReportList, deleteContentReportGooruOids, new SimpleAsyncCallback<ContentReportDo>() {
			
			@Override
			public void onSuccess(ContentReportDo result) {
				//getView().showSuccesmessagePopup();
				String chkViewPage = AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getParameter("view", null);			
				if(chkViewPage == null)
				{
				if(isPreviewPlayer){
					previewPlayerPresenter.updateFlagImageOnHomeView();
				}else if(isCollectionPlayer){
					collectionPlayerPresenter.updateFlagImageOnHomeView();
				}
				}
				else
				{
					if(isPreviewPlayer){
						previewPlayerPresenter.updateFlagImageOnHomeView();
					}else if(isCollectionPlayer){
						collectionPlayerPresenter.updateFlagImageOnHomeView();
					}
				}
			}
		});	
		
	}

	/**
	 * Updates the Reviews for a resource.
	 * 
	 * @param gooruOid {@link String}
	 * @param starRatingValue {@link Integer}
	 * @param showThankYouToolTip {@link Boolean} 
	 */
	@Override
	public void updateReview(String deleteRatingGooruOid, Integer score,String userReview,String resourceGooruId) {
		triggerCreateReviewEvent(resourceGooruId, userReview);
		AppClientFactory.getInjector().getPlayerAppService().updateResourceStarReviews(deleteRatingGooruOid, score, userReview, new SimpleAsyncCallback<ArrayList<StarRatingsDo>>(){

			@Override
			public void onSuccess(ArrayList<StarRatingsDo> result) {
				if(result.size()>0){
					getView().hideThankYouPopUp();
				}
			}
		}); 
	}

	/**
	 * Receiving event to open review pop-up
	 * 
	 */
	@Override

	public void openReviewPopUp(String assocGooruOId, String title,String createrName) {
		
		Window.enableScrolling(false);		
		ratingAndReviewPopup.displayPopup(resourceTitle, assocGooruOId,createrName);
		ratingAndReviewPopup.getWidget().getElement().getStyle().setZIndex(999999);
		addToPopupSlot(ratingAndReviewPopup);

	}

	/**
	 * API call to get a avg rating and total count.
	 * 
	 * @param assocGooruOid {@link String}
	 * @param score {@link Integer}
	 * @param review {@link String} 
	 */
	@Override
	public void getAvgRatingAndCount(final String assocGooruOid,final Integer score, final String review) {
		AppClientFactory.getInjector().getPlayerAppService().getContentStarRatings(assocGooruOid, new SimpleAsyncCallback<ContentStarRatingsDo>() {

			@Override
			public void onSuccess(ContentStarRatingsDo result) {
				getView().setRatingMetaData(assocGooruOid,score,review,result.getAverage(),result.getCount());
			}
		});
	}

	@Override
	public void setResourceMetaData(String resourceTitle) {
		this.resourceTitle = resourceTitle;
	}

	@Override
	public void updateStarRatingAndreviews(
			ArrayList<StarRatingsDo> starRatingsDo) {
		getView().setUserStarRatings(starRatingsDo.get(0),false); 
	}

	public void clearStarRatings() {
		getView().clearAllStarsForAnnonymous();
	}

	public void childLoggedIn(boolean isChild) { 
		getView().childLoggedIn(isChild);
	}

	@Override
	public void deleteStarRatings(String gooruId) {
		getView().deleteRatingsValue();
	}

	public void getGoogleDriveFile(String fileUrl) {
		AppClientFactory.getInjector().getPlayerAppService().getGoogleDriveFileStatusCode(fileUrl, new SimpleAsyncCallback<Integer>() {
			@Override
			public void onSuccess(Integer statusCode) {
				getView().setGoogleDriveFileStatusCode(statusCode);
			}
		});
	}
	
	public void getResourceTagsToDisplay(String resourceId){
		AppClientFactory.getInjector().getResourceService().getTagsToResource(resourceId, new SimpleAsyncCallback<List<ResourceTagsDo>>() {
			@Override
			public void onSuccess(List<ResourceTagsDo> resourceTagsList) {
				getView().displayResourceTags(resourceTagsList);
			}
		});
	}

	@Override
	public void getYoutubeFeedCallback(String utubeId) {
		// TODO Auto-generated method stub
		AppClientFactory.getInjector().getPlayerAppService().getYoutubeFeedCallback(utubeId, new SimpleAsyncCallback<Map<String,String>>() {

			@Override
			public void onSuccess(Map<String, String> result) {
				
				getView().checkYoutubeAccessControls(result);
			}
		});
		
	}
	public HTMLPanel getCollectionContainer(){
		return getView().getCollectionContainer();
	}
	
	public FlowPanel getResourceWidgetContainer(){
		return getView().getResourceWidgetContainer();
	}
	
	public Button getNarrationButton(){
		return getView().getNarrationButton();
	}
	
	public void clearMarginTop(){
		getView().clearMarginTop();
	}
	public void setMarginTop(){
		getView().setMarginTop();
	}

	@Override
	public void updateResourceReview(String gooruOid,Integer reviewCount) {
		if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.COLLECTION_PLAY)){
			collectionPlayerPresenter.updateReviewAndRatings(gooruOid,reviewCount); 
		}else{
			resourcePlayerPresenter.updateReviewAndRatings(gooruOid,reviewCount);
		}
	}
	
	@Override
	public void updateResourceRatings(String gooruOid,double average) {
		if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.COLLECTION_PLAY)){
			collectionPlayerPresenter.updateRatings(gooruOid,average);  
		}else{
			resourcePlayerPresenter.updateRatings(gooruOid,average);   
		}
	}
	
	public double getResourceRating(String resoruceGooruOid){
		if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.COLLECTION_PLAY)){
			return collectionPlayerPresenter.getResourceRating(resoruceGooruOid);  
		}else{
			return resourcePlayerPresenter.getResourceRating(resoruceGooruOid);   
		}
	}

	@Override
	public void deleteRatingsInPlayer() {
		getView().deleteRatingsValue();
	}

	
	/*@Override
	public void updateStarRatingAndreviews(String assocGooruOId,
			String loogedInGooruOId, ArrayList<StarRatingsDo> starRatingsDo) {
		AppClientFactory.getInjector().getPlayerAppService().getResourceRatingWithReviews(assocGooruOId, AppClientFactory.getGooruUid(), new SimpleAsyncCallback<ArrayList<StarRatingsDo>>() {

			@Override
			public void onSuccess(ArrayList<StarRatingsDo> result) {
				if(result.size()>0){
					getView().setUserStarRatings(result.get(0),false); 
				}else{
					getView().setUserStarRatings(null,false); 
				}
				
			}
		});
		
	}*/
	
	public void  triggerCreateRatingEvent(String resourceId,double currentRate, double previousRate){
		if(isCollectionPlayer){
			collectionPlayerPresenter.triggerRatingDataLogEvent(resourceId, currentRate, previousRate);
		}else if(isResourcePlayer){
			resourcePlayerPresenter.triggerRatingDataLogEvent(resourceId, currentRate, previousRate);
		}
	}
	
	public void  triggerCreateReviewEvent(String resourceId,String reviewText){
		if(isCollectionPlayer){
			collectionPlayerPresenter.triggerReviewDataLogEvent(resourceId, reviewText);
		}else if(isResourcePlayer){
			resourcePlayerPresenter.triggerReviewDataLogEvent(resourceId, reviewText);
		}
	}

}
