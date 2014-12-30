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
package org.ednovo.gooru.client.mvp.play.resource;



import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.client.AppPlaceKeeper;
import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SeoTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BasePlacePresenter;
import org.ednovo.gooru.client.mvp.home.LoginPopupUc;
import org.ednovo.gooru.client.mvp.play.collection.GwtUUIDGenerator;
import org.ednovo.gooru.client.mvp.play.collection.event.ShowResourceTabWidgetEvent;
import org.ednovo.gooru.client.mvp.play.collection.event.ShowResourceViewEvent;
import org.ednovo.gooru.client.mvp.play.collection.info.ResourceInfoPresenter;
import org.ednovo.gooru.client.mvp.play.error.ResourceNonExitView;
import org.ednovo.gooru.client.mvp.play.resource.add.AddResourceCollectionPresenter;
import org.ednovo.gooru.client.mvp.play.resource.body.ResourcePlayerMetadataPresenter;
import org.ednovo.gooru.client.mvp.play.resource.body.ResourcePlayerMetadataView;
import org.ednovo.gooru.client.mvp.play.resource.flag.ResourceFlagPresenter;
import org.ednovo.gooru.client.mvp.play.resource.share.ResourceSharePresenter;
import org.ednovo.gooru.client.mvp.rating.events.PostUserReviewResourceEvent;
import org.ednovo.gooru.client.mvp.search.AddResourceContainerPresenter;
import org.ednovo.gooru.client.mvp.search.event.UpdateSearchResultMetaDataEvent;
import org.ednovo.gooru.client.mvp.settings.CustomAnimation;
import org.ednovo.gooru.client.mvp.shelf.collection.CollectionFormInPlayPresenter;
import org.ednovo.gooru.client.mvp.shelf.event.RefreshCollectionInShelfListInResourcePlayEvent;
import org.ednovo.gooru.client.service.PlayerAppServiceAsync;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.client.util.PlayerDataLogEvents;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.content.CollectionDo;
import org.ednovo.gooru.shared.model.content.CollectionItemDo;
import org.ednovo.gooru.shared.model.content.ContentReportDo;
import org.ednovo.gooru.shared.model.content.StarRatingsDo;
import org.ednovo.gooru.shared.util.AttemptedAnswersDo;
import org.ednovo.gooru.shared.util.PlayerConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealRootPopupContentEvent;

public class ResourcePlayerPresenter extends BasePlacePresenter<IsResourcePlayerView, ResourcePlayerPresenter.IsResourcePlayerProxy> implements ResourcePlayerUiHandlers{
	
	@Inject
	private PlayerAppServiceAsync playerAppService;
	
    private SimpleAsyncCallback<CollectionDo> collectionDetailsAsync;
    
    private ResourcePlayerMetadataPresenter resoruceMetadataPresenter;
    
    private ResourceSharePresenter resourceSharePresenter;
    
    private ResourceInfoPresenter resourceInfoPresenter;
    
    private AddResourceCollectionPresenter addResourceCollectionPresnter;
    
    private CollectionFormInPlayPresenter collectionFormInPlayPresenter;
    
   // private AddResourceContainerPresenter addResourceContainerPresenter;
    
    private CollectionItemDo collectionItemDo;
    
    private String gooruOid=null;
    
    private String resourceActivityEventId=null;
    
    private String sessionId=null;
    
    private String sessionItemId=null;
    
    private String resourceDataLogEventId;
    
    private String resourceNewDataLogEventId;
    
    private Long resourceStartTime=0L;
    
    private Long hintOrExplanationStartTime=0L;
    
    private String hintOrExplanationEventName=null;
    
    private String hintOrExplanationEventId=null;
    
    private String resourcePlayEventName=null;
       
	private Map<String,AttemptedAnswersDo> attemptAnswersMap=new HashMap<String,AttemptedAnswersDo>();
	
	private List<Integer> attemptTrySequence=new ArrayList<Integer>();
	
	private List<Integer> attemptStatus=new ArrayList<Integer>();
	
	private List<Integer> answerIds=new ArrayList<Integer>();
	
	 private ResourceFlagPresenter resourceFlagPresenter;
	
	private String oeQuestionAnswerText="";
	
	private int hintId=0;
	
	private boolean isExplanationUsed=false;
	
	private boolean isUserAttemptedQuestion=false;
	
	private int userAttemptedQuestionType=0;
	
	private String questionType="RES";
	
	private JSONObject answerIdsObject=new JSONObject();
	
	private JSONObject hintIdsObject=new JSONObject();
	
	private JSONObject explanationIdsObject=new JSONObject();
	
	private List<List<JSONObject>> answerObjectArray=new ArrayList<List<JSONObject>>();
	
	private List<Integer> attemptTrySequenceArray=new ArrayList<Integer>();
	
	private List<Integer> attemptStatusArray=new ArrayList<Integer>();
	
	private Integer resourceScore=0;
	
	private Integer attemptCount=0;
	
	private boolean isOpenEndedAnswerSubmited=false;
	
	private static String Star_Rating_Widget = "ratingWidget";
	 
	private static final int CHILD_AGE=13;
	
	private MessageProperties i18n = GWT.create(MessageProperties.class);
	
    /**
	 * @return the answerIdsObject
	 */
	public JSONObject getAnswerIdsObject() {
		return answerIdsObject;
	}

	/**
	 * @param answerIdsObject the answerIdsObject to set
	 */
	public void setAnswerIdsObject(JSONObject answerIdsObject) {
		this.answerIdsObject = answerIdsObject;
	}

	/**
	 * @return the hintIdsObject
	 */
	public JSONObject getHintIdsObject() {
		return hintIdsObject;
	}

	/**
	 * @param hintIdsObject the hintIdsObject to set
	 */
	public void setHintIdsObject(JSONObject hintIdsObject) {
		this.hintIdsObject = hintIdsObject;
	}

	/**
	 * @return the explanationIdsObject
	 */
	public JSONObject getExplanationIdsObject() {
		return explanationIdsObject;
	}

	/**
	 * @param explanationIdsObject the explanationIdsObject to set
	 */
	public void setExplanationIdsObject(JSONObject explanationIdsObject) {
		this.explanationIdsObject = explanationIdsObject;
	}
	
	/**
	 * @return the attemptTrySequenceArray
	 */
	public List<Integer> getAttemptTrySequenceArray() {
		return attemptTrySequenceArray;
	}

	/**
	 * @param attemptTrySequenceArray the attemptTrySequenceArray to set
	 */
	public void setAttemptTrySequenceArray(List<Integer> attemptTrySequenceArray) {
		this.attemptTrySequenceArray = attemptTrySequenceArray;
	}

	/**
	 * @return the attemptStatusArray
	 */
	public List<Integer> getAttemptStatusArray() {
		return attemptStatusArray;
	}

	/**
	 * @param attemptStatusArray the attemptStatusArray to set
	 */
	public void setAttemptStatusArray(List<Integer> attemptStatusArray) {
		this.attemptStatusArray = attemptStatusArray;
	}

	/**
	 * @return the resourceScore
	 */
	public Integer getResourceScore() {
		return resourceScore;
	}

	/**
	 * @param resourceScore the resourceScore to set
	 */
	public void setResourceScore(Integer resourceScore) {
		this.resourceScore = resourceScore;
	}
	
	/**
	 * @return the attemptCount
	 */
	public Integer getAttemptCount() {
		return attemptCount;
	}

	/**
	 * @param attemptCount the attemptCount to set
	 */
	public void setAttemptCount(Integer attemptCount) {
		this.attemptCount = attemptCount;
	}
	
	/**
	 * @return the isOpenEndedAnswerSubmited
	 */
	public boolean isOpenEndedAnswerSubmited() {
		return isOpenEndedAnswerSubmited;
	}

	/**
	 * @param isOpenEndedAnswerSubmited the isOpenEndedAnswerSubmited to set
	 */
	public void setOpenEndedAnswerSubmited(boolean isOpenEndedAnswerSubmited) {
		this.isOpenEndedAnswerSubmited = isOpenEndedAnswerSubmited;
	}
	
	public List<List<JSONObject>> getAnswerObjectArray() {
		return answerObjectArray;
	}

	public void setAnswerObjectArray(List<List<JSONObject>> answerObjectArray) {
		this.answerObjectArray = answerObjectArray;
	}
	
    public static final  Object TAB_PRESENTER_SLOT = new Object(); 
    public static final  Object METADATA_PRESENTER_SLOT = new Object();
//    public static final String ADD_WIDGET_MODE=i18n.GL0590.toUpperCase();
    public static final String RESOURCE_THUMBS_WIDGET_MODE="RESOURCE_RATING";
//    public static final String FLAG_WIDGET_MODE=i18n.GL0600.toUpperCase();
	
	
	@Inject
	public ResourcePlayerPresenter(ResourcePlayerMetadataPresenter resoruceMetadataPresenter,ResourceSharePresenter resourceSharePresenter,
			ResourceInfoPresenter resourceInfoPresenter,EventBus eventBus, CollectionFormInPlayPresenter collectionFormInPlayPresenter,
			IsResourcePlayerView view, IsResourcePlayerProxy proxy,AddResourceCollectionPresenter addResourceCollectionPresnter,ResourceFlagPresenter resourceFlagPresenter) {
		super(view, proxy);
		getView().setUiHandlers(this);
		this.resoruceMetadataPresenter=resoruceMetadataPresenter;
		this.resourceSharePresenter=resourceSharePresenter;
		this.resourceInfoPresenter=resourceInfoPresenter;
		this.addResourceCollectionPresnter=addResourceCollectionPresnter;
		addResourceCollectionPresnter.getAddNewCollectionButton().addClickHandler(new ShowNewCollectionWidget());
		this.collectionFormInPlayPresenter=collectionFormInPlayPresenter;
		this.resourceFlagPresenter=resourceFlagPresenter;
		//this.addResourceContainerPresenter=addResourceContainerPresenter;
		resourceFlagPresenter.setResourcePlayerPresenter(this);
		resourceSharePresenter.setResourcePlayerPresenter(this);
		addResourceCollectionPresnter.getAddCollectionViewButton().setVisible(false);
		resoruceMetadataPresenter.setResourcePlayerPresenter(this, false);
		this.resourceInfoPresenter.insertHideButtonAtLast();
	}

	@ProxyCodeSplit
	@NameToken(PlaceTokens.RESOURCE_PLAY)
	@UseGatekeeper(AppPlaceKeeper.class)
	public interface IsResourcePlayerProxy extends ProxyPlace<ResourcePlayerPresenter> {
	}

	@Override
	protected final void revealInParent() {
		RevealRootPopupContentEvent.fire(this, this);
	}
	
	@Override
	public void onBind() {
	  super.onBind();
	  addRegisteredHandler(ShowResourceViewEvent.TYPE, this);
	  addRegisteredHandler(ShowResourceTabWidgetEvent.TYPE, this);
	  addRegisteredHandler(RefreshCollectionInShelfListInResourcePlayEvent.TYPE, this);
	  addRegisteredHandler(PostUserReviewResourceEvent.TYPE, this);
	}
	@Override
	protected void onReveal() {
	  super.onReveal();
	  Document doc=Document.get();
		Element bodyelement = doc.getBody();
		bodyelement.getParentElement().setAttribute("style", "overflow:hidden");
	}
	@Override
	protected void onReset() {
		  super.onReset();
		  Document doc=Document.get();
			Element bodyelement = doc.getBody();
			bodyelement.getParentElement().setAttribute("style", "overflow:hidden");
			
	}
	
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		getResourceDetails();
	}
	
	public void getResourceDetails(){
		  final String resourceId=getPlaceManager().getRequestParameter("id", null);
		  final String tabView=getPlaceManager().getRequestParameter("tab", null);
		  final String apiKey=getPlaceManager().getRequestParameter("key", null);
		  if(this.collectionItemDo!=null){
			  if(resourceId!=null&&!resourceId.equals("")){
				  showResourceView(resourceId,tabView);
				  showTabWidget(tabView,resourceId);
			  }
		  }else{
		      if(resourceId!=null && !resourceId.equalsIgnoreCase("")){
		    	  this.playerAppService.getResourceInfoDetails(apiKey,resourceId,tabView, new SimpleAsyncCallback<CollectionItemDo>() {
		    			@Override
		    			public void onSuccess(CollectionItemDo collectionItemDo) {
		    				if(collectionItemDo.getStatusCode() != 200){
		    					showResourceErrorMessage();
		    				}else{
		    					setPageTitle(collectionItemDo);
		    					showResoruceView(collectionItemDo,resourceId,tabView);
		    				}
		    			}
		    		});
		      }
		  }
	}

	/**
	 * @function setPageTitle 
	 *  
	 * @description : To set the Window Title.
	 *  
	 * @parm(s) : @param collectionDo {@link CollectionDo}
	 * 
	*/
	
	protected void setPageTitle(CollectionItemDo collectionItemDo) {
		AppClientFactory.setBrowserWindowTitle(SeoTokens.RESOURCE_PLAYER_TITLE+removeHtmlTags(collectionItemDo.getResource().getTitle()));
	}
	
	public void showResoruceView(CollectionItemDo collectionItemDo,String resourceId, String tabView){
		  this.collectionItemDo=collectionItemDo;
		  if(resourceId==null || resourceId.equalsIgnoreCase("")){
			  return;
		  }
		  else{  
			  showResourceView(resourceId,tabView);
			  showTabWidget(tabView,resourceId);
		  }
	}
	
	public PlayerAppServiceAsync getPlayerAppService() {
		return playerAppService;
	}

	public void setPlayerAppService(PlayerAppServiceAsync playerAppService) {
		this.playerAppService = playerAppService;
	}

	public SimpleAsyncCallback<CollectionDo> getCollectionDetailsAsync() {
		return collectionDetailsAsync;
	}

	public void setCollectionDetailsAsync(SimpleAsyncCallback<CollectionDo> collectionDetailsAsync) {
		this.collectionDetailsAsync = collectionDetailsAsync;
	}

	public void showResourceView(String resourceId,String tabView) {
		if(gooruOid!=null&&gooruOid.equals(this.collectionItemDo.getResource().getGooruOid())){
			makeButtonActive(tabView);
			return;
		}
		enablePlayerButton(true,true,true,true);
		makeButtonActive(tabView);
		resetAnswerLists();
		createResourceDataLogs();
		gooruOid=this.collectionItemDo.getResource().getGooruOid();
		updateResourceViewCount(gooruOid,collectionItemDo.getViews().toString(),null);
		getView().setResourceTitle(collectionItemDo.getResource().getTitle());
		updateThumbsRatingView(collectionItemDo.getResource().getUserRating()!=null?collectionItemDo.getResource().getUserRating():0);
		resoruceMetadataPresenter.showResourceWidget(collectionItemDo);
		if(!AppClientFactory.isAnonymous()){
			resoruceMetadataPresenter.setReaction(collectionItemDo);
			resoruceMetadataPresenter.setResourceStarRatings(collectionItemDo);
			getContentReport(collectionItemDo.getResource().getGooruOid());
		}else{
			resoruceMetadataPresenter.clearStarRatings();
		}
		setUserAttemptedQuestionTypeAndStatus(false,0);
		setOpenEndedAnswerSubmited(true);
		setInSlot(METADATA_PRESENTER_SLOT, resoruceMetadataPresenter,false);
	}
	public void makeButtonActive(String tabView){
		if(tabView!=null){
			//getView().clearActiveButtion();
			if(tabView.equalsIgnoreCase("add")){
				ResourcePlayerMetadataView.addPadding();
				getView().clearActiveButton(false,true, true,true);
				getView().makeButtonActive(true,false, false,false);	
			}
			else if(tabView.equalsIgnoreCase("info")){
				ResourcePlayerMetadataView.addPadding();
				getView().clearActiveButton(true,false, true,true);
				getView().makeButtonActive(false,true, false,false);	
			}
			else if(tabView.equalsIgnoreCase("share")){
				ResourcePlayerMetadataView.addPadding();
				getView().clearActiveButton(true,true, false,true);
				getView().makeButtonActive(false,false, true,false);
			}else if(tabView.equalsIgnoreCase("flag")){
				getView().clearActiveButton(true,true, true,false);
				getView().makeButtonActive(false,false, false,true);
			}
			
		}
	}
	public void createSessionAttemptTryWhenNavigation(){
		if(isUserAttemptedAnswer()){
			resoruceMetadataPresenter.createSessionAttemptTryWhenNavigation(getUserAttemptedQuestionType());
		}
	}
	public void increaseUserAttemptCount(){
		setAttemptCount(getAttemptCount()+1);
	}
	@Override
	public void showResourceView(Integer collectionItemSequence, boolean isForwardDirection) {
		//getCollectionItemDo(collectionItemSequence,isForwardDirection);
	}
	
	public void showTabWidget(String tabView,String resourceId){
		if(tabView==null||tabView.equals("")){
			getView().clearActiveButton(true,true,true,true);
			addResourceCollectionPresnter.getWidget().getElement().getStyle().setPosition(Position.RELATIVE);
			new CustomAnimation(getView().getNavigationContainer()).run(400);
			addResourceCollectionPresnter.getWidget().getElement().getStyle().clearPosition();
			resoruceMetadataPresenter.setMarginTop();
		}
		else if(tabView.equals("add")){
			MixpanelUtil.mixpanelEvent("Player_Click_Add");
			resoruceMetadataPresenter.clearMarginTop();
			setAddResourceCollectionView(resourceId);
		 }
		else if(tabView.equals("share")){
			resoruceMetadataPresenter.clearMarginTop();
			 setResourceShareView(resourceId);
		 }else if(tabView.equals("info")){
			 resoruceMetadataPresenter.clearMarginTop();
			 setResourceInfoView(resourceId);
		 }
		 else if(tabView.equals("flag")){
			 if(AppClientFactory.isAnonymous()){
				 clearSlot(TAB_PRESENTER_SLOT);
				 showLoginPopupWidget(i18n.GL0600().toUpperCase());
			}else{
				setResourceFlagView(resourceId);
			 }
		} else{
			 getView().getNavigationContainer().clear();
		 }
	}
	public void setAddResourceCollectionView(String resourceId){
	
		if(AppClientFactory.isAnonymous()){
			clearSlot(TAB_PRESENTER_SLOT);
			showLoginPopupWidget(i18n.GL0590().toUpperCase());
		}else{
			//addResourceContainerPresenter.setplayerStyle();
			//addResourceContainerPresenter.setCollectionItemData("", collectionItemDo);
			addResourceCollectionPresnter.setCollectionItemData(null, collectionItemDo);
			addResourceCollectionPresnter.getWidget().getElement().getStyle().setMarginTop(50, Unit.PX);
			addResourceCollectionPresnter.getWidget().getElement().getStyle().setPosition(Position.RELATIVE);
			setInSlot(TAB_PRESENTER_SLOT, addResourceCollectionPresnter,false);
			new CustomAnimation(getView().getNavigationContainer()).run(400);
			addResourceCollectionPresnter.getWidget().getElement().getStyle().clearPosition();
		}
	}
	public void setResourceInfoView(String resourceId){
		resourceInfoPresenter.setResoruceDetails(collectionItemDo);
		resourceInfoPresenter.getWidget().getElement().getStyle().setMarginTop(50, Unit.PX);
		setInSlot(TAB_PRESENTER_SLOT, resourceInfoPresenter,false);
		new CustomAnimation(getView().getNavigationContainer()).run(400);
	}
	public void setResourceShareView(String resourceId){
		resourceSharePresenter.setResourceShareData(collectionItemDo);
		setInSlot(TAB_PRESENTER_SLOT, resourceSharePresenter,false);
		new CustomAnimation(getView().getNavigationContainer()).run(400);
	}
	public void setResourceFlagView(String resourceId){
		resourceFlagPresenter.setFlagView(collectionItemDo);
		clearSlot(TAB_PRESENTER_SLOT);
		addToPopupSlot(resourceFlagPresenter);
	}
	public void clearTabSlot(){
		clearSlot(TAB_PRESENTER_SLOT);
	}
	
	public void enablePlayerButton(boolean isAddButtonEnable,boolean isInfoButtonEnable, boolean isShareButtonEnable,boolean isFlagButtonEnable){
		getView().enablePlayerButton(isAddButtonEnable,isInfoButtonEnable, isShareButtonEnable,isFlagButtonEnable);
	}
	
	public void createResourceDataLogs(){
		resourceActivityEventId=GwtUUIDGenerator.uuid();
		startPlayerActivityEvent(resourceActivityEventId, "", PlayerConstants.RESOURCE_PLAYER_EVENT_NAME, "", collectionItemDo.getResource().getGooruOid(), PlayerConstants.RESOURCE_PLAYER_CONTEXT+collectionItemDo.getResource().getGooruOid(), getUserAgent());
		createSession(collectionItemDo.getResource().getGooruOid());
		
	}
	public void startResourceInsightDataLog(){
		resourceDataLogEventId=GwtUUIDGenerator.uuid();
		resourceStartTime=PlayerDataLogEvents.getUnixTime();
		resourceNewDataLogEventId=GwtUUIDGenerator.uuid();
		if(collectionItemDo!=null){
			if(collectionItemDo.getResource().getResourceType().getName().equalsIgnoreCase("assessment-question")){
				questionType=PlayerDataLogEvents.getQuestionType(collectionItemDo.getResource().getType());
				if(collectionItemDo.getResource().getType()==6){
					resourcePlayEventName=PlayerDataLogEvents.QUESTION_RESOURCE_OE_EVENT_NAME;
				}else{
					resourcePlayEventName=PlayerDataLogEvents.QUESTION_RESOUCE_PLAY_EVENT_NAME;
				}
			}else{
				resourcePlayEventName=PlayerDataLogEvents.RESOUCE_PLAY_EVENT_NAME;
			}
		}
		PlayerDataLogEvents.resourcePlayStartStopEvent(resourceDataLogEventId, resourcePlayEventName, "",collectionItemDo.getResource().getGooruOid(),"", PlayerDataLogEvents.START_EVENT_TYPE, resourceStartTime,
	                 resourceStartTime, 0L,AppClientFactory.getLoginSessionToken(), AppClientFactory.getGooruUid(),attemptTrySequence,attemptStatus, answerIds,oeQuestionAnswerText,oeQuestionAnswerText.length());
		triggerCollectionItemNewDataLogStartStopEvent(collectionItemDo.getResource().getGooruOid(), resourceStartTime, resourceStartTime, PlayerDataLogEvents.START_EVENT_TYPE, 0, questionType);
	}
	
	public void stopResourceInsightDataLog(){
		stopHintOrExplanationEvent();
		Long resourceEndTime=PlayerDataLogEvents.getUnixTime();
		PlayerDataLogEvents.resourcePlayStartStopEvent(resourceDataLogEventId, resourcePlayEventName, "",collectionItemDo.getResource().getGooruOid(),"", PlayerDataLogEvents.STOP_EVENT_TYPE, resourceStartTime,
				resourceEndTime,resourceEndTime-resourceStartTime,AppClientFactory.getLoginSessionToken(), AppClientFactory.getGooruUid(),attemptTrySequence,attemptStatus, answerIds,oeQuestionAnswerText,oeQuestionAnswerText.length());
		triggerCollectionItemNewDataLogStartStopEvent(collectionItemDo.getResource().getGooruOid(), resourceStartTime, resourceEndTime, PlayerDataLogEvents.STOP_EVENT_TYPE, getResourceScore(), questionType);
	}
	public void stopDataLogEvents(){
		if(collectionItemDo!=null){
			stopResourceInsightDataLog();
			createSessionAttemptTryWhenNavigation();
			updateSession(sessionId);
			stopPlayerActivityEvent(resourceActivityEventId, "", PlayerConstants.RESOURCE_PLAYER_EVENT_NAME, "", collectionItemDo.getResource().getGooruOid(), PlayerConstants.RESOURCE_PLAYER_CONTEXT+collectionItemDo.getResource().getGooruOid(), getUserAgent());
		}
	}
	public void startPlayerActivityEvent(String activityEventId,String activityParentEventId,String eventName,String gooruOid,String resourceGooruOid,
			String context,String userAgent){
		this.playerAppService.startActivityPlayerLog(activityEventId, activityParentEventId, eventName, gooruOid, 
				resourceGooruOid, context, userAgent, new SimpleAsyncCallback<String>() {
			@Override
			public void onSuccess(String activityEventId) {
				
			}
		});
	}
	public void stopPlayerActivityEvent(String activityEventId,String activityParentEventId,String eventName,String gooruOid,String resourceGooruOid,
			String context,String userAgent){
			this.playerAppService.stopActivityPlayerLog(activityEventId, activityParentEventId, eventName, gooruOid, 
				resourceGooruOid, context, userAgent, new SimpleAsyncCallback<String>() {
			@Override
			public void onSuccess(String activityEventId) {
				
			}
		});
	}
	public void createSession(String collectionGooruOid){
		this.playerAppService.createSessionTracker(collectionGooruOid,null, new SimpleAsyncCallback<String>() {
			@Override
			public void onSuccess(String sessionId) {
				ResourcePlayerPresenter.this.sessionId=sessionId;
				startResourceInsightDataLog();
				if(collectionItemDo!=null){
					createSessionItem(sessionId, collectionItemDo.getResource().getGooruOid(), collectionItemDo.getResource().getGooruOid());
				}
			}
		});
	}
	
	public void createSessionItem(String sessionTrackerId,String collectionItemId, String resourceGooruOid){
		this.playerAppService.createSessionItemInCollection(sessionTrackerId, collectionItemId, resourceGooruOid, new SimpleAsyncCallback<String>() {
			@Override
			public void onSuccess(String sessionItemId) {
				ResourcePlayerPresenter.this.sessionItemId=sessionItemId;
			}
		});
	}
	
	public void createSessionItemAttempt(int answerId, String attemptResult){
		this.playerAppService.createSessionItemAttemptTry(sessionId, sessionItemId, answerId, attemptResult, new SimpleAsyncCallback<String>() {
			@Override
			public void onSuccess(String sessionItemId) {}
		});
	}
	
	public void createSessionItemAttemptOe(String answerId,String attemptStatus,String attemptAnswerResult){
		this.playerAppService.createSessionItemAttemptTryForOe(sessionId, sessionItemId, answerId,attemptStatus,attemptAnswerResult, new SimpleAsyncCallback<String>() {
			@Override
			public void onSuccess(String sessionItemId) {}
		});
	}
	
	public void updateSession(String sessionTrackerId){
		if(sessionTrackerId!=null){
			this.playerAppService.updateSessionInCollection(sessionTrackerId, new SimpleAsyncCallback<String>() {
				@Override
				public void onSuccess(String sessionItemId) {}
			});
		}
	}
	
	public void setUserAttemptResult(String collectionItemId,AttemptedAnswersDo attemptAnswersDo){
		attemptAnswersMap.put(collectionItemId, attemptAnswersDo);
	}
	
	public void removeUserAttemptResult(){
		attemptAnswersMap.remove(collectionItemDo.getCollectionItemId());
	}
	
	public Map<String, AttemptedAnswersDo> getAttemptAnswersMap() {
		return attemptAnswersMap;
	}
	
	public String getUserAgent(){
		return Window.Navigator.getUserAgent();
	}
	
	public void resetAnswerLists(){
		attemptTrySequence.clear();
		attemptStatus.clear();
		answerIds.clear();
		oeQuestionAnswerText="";
		isExplanationUsed=false;
		hintId=0;
		hintOrExplanationEventName=null;
		questionType="RES";
		hintIdsObject=new JSONObject();
		explanationIdsObject=new JSONObject();
		answerIdsObject=new JSONObject();
		answerObjectArray.clear();
		attemptStatusArray.clear();
		attemptTrySequenceArray.clear();
		attemptAnswersMap.clear();
		setResourceScore(0);
		setAttemptCount(0);
	}
	
	public void setAnswerAttemptSequence(int attemptSequence,int attemptStatus,int answerId){
		this.attemptTrySequence.add(attemptSequence);
		this.attemptStatus.add(attemptStatus);
		this.answerIds.add(answerId);
	}
	
	public void setOeQuestionAnswerText(String oeAnswerText){
		this.oeQuestionAnswerText=oeAnswerText;
	}
	
	public void startHintDataLogEvent(int hintId){
		stopHintOrExplanationEvent();
		this.hintId=hintId;
		hintOrExplanationStartTime=PlayerDataLogEvents.getUnixTime();
		hintOrExplanationEventId=GwtUUIDGenerator.uuid();
		if(collectionItemDo!=null){
			if(collectionItemDo.getResource().getResourceType().getName().equalsIgnoreCase("assessment-question")){
				if(collectionItemDo.getResource().getType()==6){
					hintOrExplanationEventName=PlayerDataLogEvents.QUESTION_OE_HINT_EVENT_NAME;
				}else{
					hintOrExplanationEventName=PlayerDataLogEvents.QUESTION_RESOURCE_HINT_EVENT_NAME;
				}
			}
		}
		PlayerDataLogEvents.hintsButtonDataLogEvent(hintOrExplanationEventId, hintOrExplanationEventName, resourceDataLogEventId, collectionItemDo.getResource().getGooruOid(),
				"", PlayerDataLogEvents.START_EVENT_TYPE, hintOrExplanationStartTime, hintOrExplanationStartTime, 0L, AppClientFactory.getLoginSessionToken(),
				AppClientFactory.getGooruUid(), 0, hintId,attemptTrySequence,attemptStatus, answerIds,oeQuestionAnswerText,oeQuestionAnswerText.length(),isExplanationUsed);
	}
	
	public void startExplanationDataLogEvent(){
		stopHintOrExplanationEvent();
		isExplanationUsed=true;
		hintOrExplanationEventId=GwtUUIDGenerator.uuid();
		if(collectionItemDo!=null){
			if(collectionItemDo.getResource().getResourceType().getName().equalsIgnoreCase("assessment-question")){
				if(collectionItemDo.getResource().getType()==6){
					hintOrExplanationEventName=PlayerDataLogEvents.QUESTION_OE_EXPLANATION_EVENT_NAME;
				}else{
					hintOrExplanationEventName=PlayerDataLogEvents.QUESTION_RESOURCE_EXPLANATION_EVENT_NAME;
				}
			}
		}
		PlayerDataLogEvents.explanationButtonDataLogEvent(hintOrExplanationEventId, hintOrExplanationEventName, resourceDataLogEventId, collectionItemDo.getResource().getGooruOid(), 
				"", PlayerDataLogEvents.START_EVENT_TYPE, hintOrExplanationStartTime,hintOrExplanationStartTime, 0L, AppClientFactory.getLoginSessionToken(),
				AppClientFactory.getGooruUid(), isExplanationUsed,attemptTrySequence,attemptStatus, answerIds,oeQuestionAnswerText,oeQuestionAnswerText.length(),hintId);
	}
	
	public void saveOeQuestionAnswerDataLogEvent(){
		stopHintOrExplanationEvent();
		hintOrExplanationEventName=null;
		String submitEventId=GwtUUIDGenerator.uuid();
		Long answerEndTime=PlayerDataLogEvents.getUnixTime();
		PlayerDataLogEvents.submitOeAnswerDataLogEvent(submitEventId, PlayerDataLogEvents.QUESTION_OE_SAVE_EVENT_NAME, resourceDataLogEventId,
				collectionItemDo.getResource().getGooruOid(),resourceStartTime,answerEndTime, answerEndTime-resourceStartTime,
				 AppClientFactory.getLoginSessionToken(), AppClientFactory.getGooruUid(),attemptTrySequence,attemptStatus, answerIds,oeQuestionAnswerText,oeQuestionAnswerText.length(),"");
	}
	
	public void stopHintOrExplanationEvent(){
		if(hintOrExplanationEventName!=null){
			Long endTime=PlayerDataLogEvents.getUnixTime();
			Long spendTime=endTime-hintOrExplanationStartTime;
			if(hintOrExplanationEventName.equals(PlayerDataLogEvents.QUESTION_RESOURCE_EXPLANATION_EVENT_NAME)){
				PlayerDataLogEvents.explanationButtonDataLogEvent(hintOrExplanationEventId, PlayerDataLogEvents.COLLECTION_RESOURCE_EXPLANATION_EVENT_NAME, resourceDataLogEventId, collectionItemDo.getResource().getGooruOid(), 
	    				"", PlayerDataLogEvents.STOP_EVENT_TYPE, hintOrExplanationStartTime,endTime, spendTime, AppClientFactory.getLoginSessionToken(),
	    				AppClientFactory.getGooruUid(), true,attemptTrySequence,attemptStatus, answerIds,oeQuestionAnswerText,oeQuestionAnswerText.length(),hintId);
			}else if(hintOrExplanationEventName.equals(PlayerDataLogEvents.QUESTION_RESOURCE_HINT_EVENT_NAME)){
				PlayerDataLogEvents.hintsButtonDataLogEvent(hintOrExplanationEventId, PlayerDataLogEvents.COLLECTION_RESOURCE_HINT_EVENT_NAME, resourceDataLogEventId, collectionItemDo.getResource().getGooruOid(),
						"", PlayerDataLogEvents.STOP_EVENT_TYPE, hintOrExplanationStartTime, endTime, spendTime,  AppClientFactory.getLoginSessionToken(),
						AppClientFactory.getGooruUid(), 0, hintId,attemptTrySequence,attemptStatus, answerIds,oeQuestionAnswerText,oeQuestionAnswerText.length(),isExplanationUsed);
			}
			else if(hintOrExplanationEventName.equals(PlayerDataLogEvents.QUESTION_OE_EXPLANATION_EVENT_NAME)){
				PlayerDataLogEvents.explanationButtonDataLogEvent(hintOrExplanationEventId, PlayerDataLogEvents.COLLECTION_RESOURCE_OE_EXPLANATION_EVENT_NAME, resourceDataLogEventId, collectionItemDo.getResource().getGooruOid(), 
						"", PlayerDataLogEvents.STOP_EVENT_TYPE, hintOrExplanationStartTime,endTime, spendTime, AppClientFactory.getLoginSessionToken(),
						AppClientFactory.getGooruUid(), true,attemptTrySequence,attemptStatus, answerIds,oeQuestionAnswerText,oeQuestionAnswerText.length(),hintId);
			}else if(hintOrExplanationEventName.equals(PlayerDataLogEvents.QUESTION_OE_HINT_EVENT_NAME)){
				PlayerDataLogEvents.hintsButtonDataLogEvent(hintOrExplanationEventId, PlayerDataLogEvents.COLLECTION_RESOURCE_OE_HINT_EVENT_NAME, resourceDataLogEventId, collectionItemDo.getResource().getGooruOid(),
						"", PlayerDataLogEvents.STOP_EVENT_TYPE, hintOrExplanationStartTime, endTime, spendTime, AppClientFactory.getLoginSessionToken(),
						AppClientFactory.getGooruUid(), 0, hintId,attemptTrySequence,attemptStatus, answerIds,oeQuestionAnswerText,oeQuestionAnswerText.length(),isExplanationUsed);
			}
		}
	}
	
	public void updateResourceViewCount(String gooruId,String viewsCount,String resourceType){
//		this.playerAppService.updateViewCount(gooruId, viewsCount, resourceType, new SimpleAsyncCallback<String>() {
//			@Override
//			public void onSuccess(String result) {
					updateViewCount();
//			}
//		});
	}
	
	public void updateViewCount(){
		if(collectionItemDo!=null){
			String viewsCount=collectionItemDo.getResource().getViews();
			Integer viewsCounts=Integer.parseInt(viewsCount)+1;
			collectionItemDo.getResource().setViews(viewsCounts.toString());
			resourceInfoPresenter.updateViewsCount(viewsCounts.toString());
		      try{
		    	  	AppClientFactory.fireEvent(new UpdateSearchResultMetaDataEvent(viewsCounts.toString(), collectionItemDo.getResource().getGooruOid(), "views"));
		         }
		      catch(Exception ex){}
		}
	}

	@Override
	public String getViewToken() {
		return PlaceTokens.RESOURCE_PLAY;
	}

	@Override
	public void resetResourcePlayer() {
		if(this.collectionItemDo!=null){
			stopDataLogEvents();
			getView().setResourceTitle("");
			enablePlayerButton(false,false, false,false);
			clearTabSlot();
			this.collectionItemDo=null;
			this.gooruOid=null;
			clearIframeContent();
			resoruceMetadataPresenter.resetResourceMetaData();
			resourceInfoPresenter.resetResourceInfo();
			setOpenEndedAnswerSubmited(true);
		}
	}
	
	public void showLoginPopupWidget(String widgetMode){
		LoginPopupUc popup =new LoginPopupUc();
		popup.setWidgetMode(widgetMode);
		popup.setGlassEnabled(true);
	}

	@Override
	public void showTabWidget(String widgetMode,boolean isLoginRequestCancel) {
		  String resourceId=getPlaceManager().getRequestParameter("id", null);
		  if(!isLoginRequestCancel&&widgetMode.equals(i18n.GL0590().toUpperCase())){
			 // getResource(resourceId);
			  setAddResourceCollectionView(resourceId);
		  } else if(!isLoginRequestCancel&&widgetMode.equals(RESOURCE_THUMBS_WIDGET_MODE)){
			  //getResource(resourceId);
		  }else if(!isLoginRequestCancel&&widgetMode.equals(i18n.GL0600().toUpperCase())){
			  getContentReport(collectionItemDo.getResource().getGooruOid());
		  } 
		  else if(isLoginRequestCancel){
			  PlaceRequest request=new PlaceRequest(PlaceTokens.RESOURCE_PLAY).with("id", resourceId);
		      AppClientFactory.getPlaceManager().revealPlace(false,request,true);
		  }else if(!isLoginRequestCancel&&widgetMode.equalsIgnoreCase(Star_Rating_Widget)){
				isResourceContentRating(collectionItemDo.getResource().getGooruOid());
			}
	}
	public void  getResource(String resourceId){
		this.playerAppService.getResourceInfoDetails(null,resourceId,null, new SimpleAsyncCallback<CollectionItemDo>() {
			@Override
			public void onSuccess(CollectionItemDo collectionItemDo) {
				if(collectionItemDo.getStatusCode()!=200){
					showResourceErrorMessage();
				}else{
					ResourcePlayerPresenter.this.collectionItemDo=collectionItemDo;
					updateThumbsRatingView(collectionItemDo.getResource().getUserRating());	
				}
				
			}
		});
	}
	
	/**
	 * it will display resource not found error view.
	 */
	protected void showResourceErrorMessage(){
		/*Label errorMessageLabel=new Label();
		errorMessageLabel.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().getResourceErrorStyle());
	*/	enablePlayerButton(false, false, false,false);
		setOpenEndedAnswerSubmited(true);
		getView().getPlayerBodyContainer().clear();
		getView().getPlayerBodyContainer().add(new ResourceNonExitView());
	}
	
	public class ShowNewCollectionWidget implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			String resourceId=collectionItemDo.getResource().getGooruOid();
			addToPopupSlot(collectionFormInPlayPresenter);
			collectionFormInPlayPresenter.setResourceUid(resourceId);
		}
	}
	
	public void updateAddResourceCollectionWidget(String collectionId){
		addResourceCollectionPresnter.updateWorkSpaceLink(collectionId);
	}
	
	@Override
	public void refreshCollectionInShelfListInPlay(String collectionId) {
		updateAddResourceCollectionWidget(collectionId);
	}

	@Override
	public void updateResourceThumbsRating(final int userThumbsRataing) {
		String resourceGooruOid=collectionItemDo.getResource().getGooruOid();
		this.playerAppService.updateContentThumbsRating(resourceGooruOid, userThumbsRataing, new SimpleAsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				updateThumbsRatingView(userThumbsRataing);
			}
		});
	}
	
	public void updateThumbsRatingView(int userThumbsRataing) {
		updateResourceLikes(userThumbsRataing);
		getView().updateThumbsRatingView(userThumbsRataing);
	}
	
	public void updateResourceLikes(int userThumbsRataing){
		if(collectionItemDo.getRating()!=null){
			int resourceLikes=collectionItemDo.getRating().getVotesUp()!=null?collectionItemDo.getRating().getVotesUp():0;
			int userRating=collectionItemDo.getResource().getUserRating()!=null?collectionItemDo.getResource().getUserRating():0;
			if(userThumbsRataing==1){
				resourceLikes=resourceLikes+1;
			}else if((userThumbsRataing==0||userThumbsRataing==-1)&&userRating==1){
				resourceLikes=resourceLikes-1;
			}
			resourceLikes=resourceLikes<0?0:resourceLikes;
			resourceInfoPresenter.updateLikesCount(resourceLikes);
			collectionItemDo.getResource().setUserRating(userThumbsRataing);
			collectionItemDo.getRating().setVotesUp(resourceLikes);
		}
		
	}

	@Override
	public void getContentReport(String assoGooruId) {
		playerAppService.getContentReport(collectionItemDo.getResource().getGooruOid(), AppClientFactory.getGooruUid(), new SimpleAsyncCallback<ArrayList<ContentReportDo>>() {
			@Override
			public void onSuccess(ArrayList<ContentReportDo> result) {
				String gooruFlagId="";
				if(result!=null&&result.size()>0){
					for(int i =0;i<result.size();i++){
						gooruFlagId = gooruFlagId+result.get(i).getDeleteContentGooruOid();
						if(result.size()!=(i+1)){
							gooruFlagId=gooruFlagId+",";
						}
					}
					getView().makeFlagButtonOrange();
					resourceFlagPresenter.setContentDeleteIds(gooruFlagId);
				 }
			}
		});
		
	}
	public void clearIframeContent(){
		resoruceMetadataPresenter.clearIfrmaeContent();
	}

	/**
	 * @return the isUserAttemptedAnswer
	 */
	public boolean isUserAttemptedAnswer() {
		return isUserAttemptedQuestion;
	}

	/**
	 * @param isUserAttemptedAnswer the isUserAttemptedAnswer to set
	 */
	public void setUserAttemptedAnswer(boolean isUserAttemptedQuestion) {
		this.isUserAttemptedQuestion = isUserAttemptedQuestion;
	}
	
   /**
	 * @return the userAttemptedQuestionType
	 */
	public int getUserAttemptedQuestionType() {
		return userAttemptedQuestionType;
	}

	/**
	 * @param userAttemptedQuestionType the userAttemptedQuestionType to set
	 */
	public void setUserAttemptedQuestionType(int userAttemptedQuestionType) {
		this.userAttemptedQuestionType = userAttemptedQuestionType;
	}
	
	public void setUserAttemptedQuestionTypeAndStatus(boolean isUserAttemptedThisQuestion,int questionType){
		setUserAttemptedAnswer(isUserAttemptedThisQuestion);
		setUserAttemptedQuestionType(questionType);
	}
	public void triggerSaveOeAnswerTextDataEvent(){
		String oeDataLogEventId=GwtUUIDGenerator.uuid();
		Long oeStartTime=PlayerDataLogEvents.getUnixTime();
		triggerSaveOeAnswerTextDataEvent(oeDataLogEventId,collectionItemDo.getResource().getGooruOid(),oeStartTime,oeStartTime,0);
	}
	
	public void triggerCollectionItemNewDataLogStartStopEvent(String resourceId,Long resourceStartTime,Long resourceEndTime,String eventType,Integer score,String questionType){
		JSONObject collectionDataLog=new JSONObject(); 
		collectionDataLog.put(PlayerDataLogEvents.EVENTID, new JSONString(resourceNewDataLogEventId));
		collectionDataLog.put(PlayerDataLogEvents.EVENTNAME, new JSONString(PlayerDataLogEvents.RESOURCE_PLAY));
		collectionDataLog.put(PlayerDataLogEvents.SESSION, PlayerDataLogEvents.getDataLogSessionObject(sessionId));
		collectionDataLog.put(PlayerDataLogEvents.STARTTIME, new JSONNumber(resourceStartTime));
		collectionDataLog.put(PlayerDataLogEvents.ENDTIME, new JSONNumber(resourceEndTime));
		collectionDataLog.put(PlayerDataLogEvents.USER, PlayerDataLogEvents.getDataLogUserObject());
		String questionTypeString=questionType.equals("RES")?"resource":"question";
		collectionDataLog.put(PlayerDataLogEvents.CONTEXT, PlayerDataLogEvents.getDataLogContextObject(resourceId,"", "", eventType, PlayerDataLogEvents.STUDY,questionTypeString,null,resourceId,null));
		collectionDataLog.put(PlayerDataLogEvents.VERSION,PlayerDataLogEvents.getDataLogVersionObject());
		collectionDataLog.put(PlayerDataLogEvents.METRICS,PlayerDataLogEvents.getDataLogMetricsObject(resourceEndTime-resourceStartTime, getResourceScore()));
		String searchTerm=null;
		if(eventType.equals(PlayerDataLogEvents.START_EVENT_TYPE)){
			searchTerm=getSearchKeyword();
		}
		collectionDataLog.put(PlayerDataLogEvents.PAYLOADOBJECT,PlayerDataLogEvents.getDataLogPayLoadObject(questionType,oeQuestionAnswerText,attemptStatusArray,attemptTrySequenceArray,answerIdsObject,hintIdsObject,explanationIdsObject,getAttemptCount(),answerObjectArray,searchTerm));
		PlayerDataLogEvents.collectionStartStopEvent(collectionDataLog);
	}
	public void triggerSaveOeAnswerTextDataEvent(String eventId,String resourceId,Long oeStartTime,Long oeEndTime,int score){
		JSONObject collectionDataLog=new JSONObject(); 
		collectionDataLog.put(PlayerDataLogEvents.EVENTID, new JSONString(eventId));
		collectionDataLog.put(PlayerDataLogEvents.EVENTNAME, new JSONString(PlayerDataLogEvents.RESOURCE_SAVE));
		collectionDataLog.put(PlayerDataLogEvents.SESSION, PlayerDataLogEvents.getDataLogSessionObject(sessionId));
		collectionDataLog.put(PlayerDataLogEvents.STARTTIME, new JSONNumber(oeStartTime));
		collectionDataLog.put(PlayerDataLogEvents.ENDTIME, new JSONNumber(oeEndTime));
		collectionDataLog.put(PlayerDataLogEvents.USER, PlayerDataLogEvents.getDataLogUserObject());
		collectionDataLog.put(PlayerDataLogEvents.CONTEXT, PlayerDataLogEvents.getDataLogContextObject(resourceId,"", resourceNewDataLogEventId, "", PlayerDataLogEvents.STUDY,"question",null,resourceId,null));
		collectionDataLog.put(PlayerDataLogEvents.VERSION,PlayerDataLogEvents.getDataLogVersionObject());
		collectionDataLog.put(PlayerDataLogEvents.METRICS,PlayerDataLogEvents.getDataLogMetricsObject(oeEndTime-oeStartTime, 0));
		collectionDataLog.put(PlayerDataLogEvents.PAYLOADOBJECT,PlayerDataLogEvents.getDataLogPayLoadObject(questionType,oeQuestionAnswerText,attemptStatusArray,attemptTrySequenceArray,answerIdsObject,hintIdsObject,explanationIdsObject,getAttemptCount(),answerObjectArray,null));
		PlayerDataLogEvents.collectionStartStopEvent(collectionDataLog);
	}

	public void triggerItemFlagDataLogEvent(Long startTime,String itemType,String flagText,ArrayList<String> contentReportList,String itemGooruOid,String collectionItemId){
		JSONObject collectionDataLog=new JSONObject(); 
		collectionDataLog.put(PlayerDataLogEvents.EVENTID, new JSONString(GwtUUIDGenerator.uuid()));
		collectionDataLog.put(PlayerDataLogEvents.EVENTNAME, new JSONString(PlayerDataLogEvents.ITEM_FLAG));
		collectionDataLog.put(PlayerDataLogEvents.SESSION, PlayerDataLogEvents.getDataLogSessionObject(sessionId));
		collectionDataLog.put(PlayerDataLogEvents.STARTTIME, new JSONNumber(startTime));
		collectionDataLog.put(PlayerDataLogEvents.ENDTIME, new JSONNumber(startTime));
		collectionDataLog.put(PlayerDataLogEvents.METRICS,PlayerDataLogEvents.getDataLogMetricsObject(startTime-startTime));
		collectionDataLog.put(PlayerDataLogEvents.VERSION,PlayerDataLogEvents.getDataLogVersionObject());
		collectionDataLog.put(PlayerDataLogEvents.PAYLOADOBJECT,PlayerDataLogEvents.getItemFlagDataLogPayLoadObject(itemType,flagText,contentReportList));
		String classpageId=AppClientFactory.getPlaceManager().getDataLogClasspageId();
		String path="";
		
		collectionDataLog.put(PlayerDataLogEvents.CONTEXT,PlayerDataLogEvents.getDataLogContextObjectForItemLoad(itemGooruOid, itemGooruOid, "", classpageId, "", PlayerDataLogEvents.STUDY, path, null, PlayerDataLogEvents.COLLECTION_FLAG_URL));
		PlayerDataLogEvents.collectionStartStopEvent(collectionDataLog);
	}
	public void triggerShareDataLogEvent(String resourceGooruOid, String itemType, String shareType, boolean confirmStatus){
		PlayerDataLogEvents.triggerItemShareDataLogEvent(resourceGooruOid, "", null,"", "", sessionId, itemType, shareType, confirmStatus, PlayerDataLogEvents.STUDY, "", null);
	}
	
	/**
	 * This method is used to log <b>star rating</b> data log event.
	 * @param resourceId specifies the unique id of the resource.
	 * @param currentRate specifies the user star rating.
	 * @param previousRate specifies the user previous star rating.
	 */
	public void triggerRatingDataLogEvent(String resourceId,double currentRate,double previousRate){
		PlayerDataLogEvents.triggerRatingDataLogEvent(resourceId,null, null,sessionId, "", null,currentRate,previousRate);
	}
	
	/**
	 * This method is used to log user <b>review</b> text data log event.
	 * @param resourceId specifies the unique id of the resource.
	 * @param reviewText specifies user entered review text.
	 */
	public void triggerReviewDataLogEvent(String resourceId,String reviewText){
		PlayerDataLogEvents.triggerReviewDataLogEvent(resourceId,null, null,sessionId, "", null,reviewText);
	}

	/**
	 * Gets the respective resource ratings rated by the user.
	 * @param resourceGooruId {@link String} 
	 */
	private void isResourceContentRating(String resourceGooruId) {
		if(!AppClientFactory.isAnonymous()){
			if(isChildAccount()){
				resoruceMetadataPresenter.childLoggedIn(true); 
			}else{
				resoruceMetadataPresenter.childLoggedIn(false); 
				getContentRating(resourceGooruId);
			}
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
	
	private int getAge(Date birthDate) {
		long ageInMillis = new Date().getTime() - birthDate.getTime();
		Date age = new Date(ageInMillis);
		return age.getYear() - 70;
	}
	
	/**
	 * Get ratings API is called and gets respective ratings.
	 * @param resourceGooruId {@link String}
	 */
	private void getContentRating(String resourceGooruId) {
		AppClientFactory.getInjector().getPlayerAppService().getResourceRatingWithReviews(collectionItemDo.getResource().getGooruOid(), AppClientFactory.getGooruUid(),0, new SimpleAsyncCallback<ArrayList<StarRatingsDo>>() {
			@Override
			public void onSuccess(ArrayList<StarRatingsDo> result) {
				if(result.size()>0){
					resoruceMetadataPresenter.getView().setUserStarRatings(result.get(0),false); 
				}else{
					resoruceMetadataPresenter.getView().setUserStarRatings(null,false); 
				}
				
			}
		});
		
	}
	private String removeHtmlTags(String html){
        html = html.replaceAll("</p>", " ").replaceAll("<p>", "").replaceAll("<br data-mce-bogus=\"1\">", "").replaceAll("<br>", "").replaceAll("</br>", "").replaceAll("<p class=\"p1\">", "");
        return html;
	}

	public void updateReviewAndRatings(String gooruOid,Integer reviewCount) {
		if(collectionItemDo!=null){
			if(gooruOid.equalsIgnoreCase(collectionItemDo.getResource().getGooruOid())){
				collectionItemDo.getResource().getRatings().setReviewCount(reviewCount);  
			}
		}
	}
	
	public void updateRatings(String gooruOid, double average) { 
		if(collectionItemDo!=null){
			if(gooruOid.equalsIgnoreCase(collectionItemDo.getResource().getGooruOid())){
				collectionItemDo.getResource().getRatings().setAverage(average); 
			}
		}
	}
	
	public double getResourceRating(String gooruOid) { 
		if(collectionItemDo!=null){
			if(gooruOid.equalsIgnoreCase(collectionItemDo.getResource().getGooruOid())){
				return collectionItemDo.getResource().getRatings().getAverage(); 
			}
		}
		return 0;
	}
	public String getSearchKeyword(){
		String keyword=null;
		PlaceRequest placeRequest=AppClientFactory.getPlaceManager().getPreviousPlayerRequestUrl();
		if(placeRequest!=null){
			if(placeRequest.getNameToken().equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)){
				keyword=placeRequest.getParameter("query", null);
				keyword=URL.encodeQueryString(keyword);
			}
		}
		return keyword;
	}

	@Override
	public void postReviewForResource(String assocGooruOId, String userReview,
			Integer score, boolean isUpdate) {
		if(resoruceMetadataPresenter!=null){
			resoruceMetadataPresenter.postReviewForResource(assocGooruOId, userReview, score, isUpdate);
		}
		
	}
	
}
