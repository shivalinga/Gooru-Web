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
/**
 * 
*/
package org.ednovo.gooru.client.mvp.classpages.edit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SeoTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BasePlacePresenter;
import org.ednovo.gooru.client.mvp.analytics.AnalyticsPresenter;
import org.ednovo.gooru.client.mvp.classpages.assignments.AddAssignmentContainerPresenter;
import org.ednovo.gooru.client.mvp.classpages.classlist.ClassListPresenter;
import org.ednovo.gooru.client.mvp.classpages.classlist.WelcomeClassView;
import org.ednovo.gooru.client.mvp.classpages.edit.EditClasspagePresenter.IsEditClasspageProxy;
import org.ednovo.gooru.client.mvp.classpages.event.RefreshAssignmentsListEvent;
import org.ednovo.gooru.client.mvp.classpages.event.UpdateClasspageImageEvent;
import org.ednovo.gooru.client.mvp.home.event.HeaderTabType;
import org.ednovo.gooru.client.mvp.home.event.HomeEvent;
import org.ednovo.gooru.client.mvp.image.upload.ImageUploadPresenter;
import org.ednovo.gooru.client.mvp.play.collection.GwtUUIDGenerator;
import org.ednovo.gooru.client.mvp.search.event.ConfirmStatusPopupEvent;
import org.ednovo.gooru.client.mvp.search.event.SetFooterEvent;
import org.ednovo.gooru.client.mvp.shelf.ErrorPopup;
import org.ednovo.gooru.client.mvp.shelf.event.AssignmentEvent;
import org.ednovo.gooru.client.service.ClasspageServiceAsync;
import org.ednovo.gooru.client.util.PlayerDataLogEvents;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.content.AssignmentsListDo;
import org.ednovo.gooru.shared.model.content.ClasspageDo;
import org.ednovo.gooru.shared.model.content.ClasspageItemDo;
import org.ednovo.gooru.shared.model.content.CollectionDo;
import org.ednovo.gooru.shared.model.content.CollectionItemDo;
import org.ednovo.gooru.shared.model.user.ProfilePageDo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;


/**
 * @fileName : EditClasspagesPresenter.java
 *
 * @description : 
 *
 *
 * @version : 1.0
 *
 * @date: Apr 17, 2013
 *
 * @Author Gooru Team
 *
 * @Reviewer: 
 */
public class EditClasspagePresenter extends BasePlacePresenter<IsEditClasspageView, IsEditClasspageProxy> implements EditClasspageUiHandlers {

	
	@Inject
	private ClasspageServiceAsync classpageService;
	
	private SimpleAsyncCallback<CollectionDo> collectionAsyncCallback;
	
	private SimpleAsyncCallback<CollectionItemDo> assignmentAsyncCallback;
	
	private SimpleAsyncCallback<CollectionDo> assignmentListAsyncCallback;
	
	private SimpleAsyncCallback<AssignmentsListDo> assignmentsListAsyncCallback;
	
//	private ShelfListPresenter shelfListPresenter;
	
	private SimpleAsyncCallback<Map<String, String>> shareUrlGenerationAsyncCallback;
	
	private SimpleAsyncCallback<CollectionDo> updateAssignmentAsyncCallback;
	
	AnalyticsPresenter analyticsPresenter=null;

	AddAssignmentContainerPresenter assignmentContainer=null;
	
	public static final  Object SLOT_SET_SUMMARY_PROGRESS = new Object();
	final String SUMMARY="Summary",PROGRESS="Progress",REPORTS="reports";
	
	private Integer offset=0;
	private Integer limit=5;
	private static final Integer DEFAULT_LIMITVALUE=5;
	
	ClassListPresenter classlistPresenter;
	
	public static final  Object CLASSLIST_SLOT = new Object();
	
	String tab=null;
	
	private ImageUploadPresenter imageUploadPresenter;
	
	String classpageId="";
	
	String analyticsId="";
	String monitorId="";
	
	private boolean isApiCalled = false;
	public static boolean isLoggedInUser=false;
	
	private ClasspageDo classpageDo;
	
	private MessageProperties i18n = GWT.create(MessageProperties.class); 
	
	//ShelfListPresenter shelfTabPresenter
	@Inject
	public EditClasspagePresenter(IsEditClasspageView view, IsEditClasspageProxy proxy, AddAssignmentContainerPresenter assignmentContainer,ImageUploadPresenter imageUploadPresenter, ClassListPresenter classlistPresenter,AnalyticsPresenter analyticsPresenter) {
		super(view, proxy);
		
		getView().setUiHandlers(this);
		
//		this.shelfListPresenter = shelfTabPresenter;
		this.assignmentContainer = assignmentContainer;
		this.imageUploadPresenter=imageUploadPresenter;		
		this.classlistPresenter=classlistPresenter;
		this.analyticsPresenter=analyticsPresenter;
		addRegisteredHandler(AssignmentEvent.TYPE, this);
		addRegisteredHandler(RefreshAssignmentsListEvent.TYPE, this);
		addRegisteredHandler(UpdateClasspageImageEvent.TYPE, this);
	}

	@ProxyCodeSplit
	@NameToken(PlaceTokens.EDIT_CLASSPAGE)
	public interface IsEditClasspageProxy extends ProxyPlace<EditClasspagePresenter> {
	}
	
	
	@Override
	public String getViewToken() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		Window.scrollTo(0, 0);
		AppClientFactory.fireEvent(new HomeEvent(HeaderTabType.TEACH));
		getView().clearPanel();
		if (AppClientFactory.isAnonymous()){
			AppClientFactory.getPlaceManager().redirectPlace(PlaceTokens.HOME);
		}
		
		//Call Event for Setting Confirm popup
		AppClientFactory.fireEvent(new ConfirmStatusPopupEvent(true));
		
		AppClientFactory.fireEvent(new SetFooterEvent(AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken()));
		
		
	}
	
		
	@Override
	protected void onHide() {
		super.onHide();
		getView().closeAllOpenedPopUp();
		imageUploadPresenter.getView().closeImageUploadWidget();
		assignmentContainer.getView().onUnload();
		this.classpageDo=null;
		AppClientFactory.getPlaceManager().setClasspageEventId(null);
	}
	
	@Override
	public void onBind() {
		super.onBind();
		setCollectionAsyncCallback(new SimpleAsyncCallback<CollectionDo>() {

			@Override
			public void onSuccess(CollectionDo collectionDo) {
				try{
					if (collectionDo.getMeta().getPermissions().toString().contains("edit")){
						isApiCalled = true;
						getView().setData(collectionDo);
					}else{
						isApiCalled = false;
						ErrorPopup error = new ErrorPopup(i18n.GL0341());
						error.center();
						error.show();
					}
				}catch(Exception e){
					
				}
			}
		});
		
		setAssignmentAsyncCallback(new SimpleAsyncCallback<CollectionItemDo>() {

			@Override
			public void onSuccess(CollectionItemDo result) {
//				getView().setAssignmentData(result,true);
//				TO DO....
			}
		});
		setShareUrlGenerationAsyncCallback(new SimpleAsyncCallback<Map<String, String>>() {
			@Override
			public void onSuccess(Map<String, String> shortenUrl) {
				classlistPresenter.setShareUrl(shortenUrl);
			}
		});
		setAssignmentListAsyncCallback(new SimpleAsyncCallback<CollectionDo>() {

			@Override
			public void onSuccess(CollectionDo result) {
			
			}
		});
		
		setAssignmentsListAsyncCallback(new SimpleAsyncCallback<AssignmentsListDo>() {

			@Override
			public void onSuccess(AssignmentsListDo result) {
				getView().listAssignments(result);
			}
		});
		
	}
	/*@Override
	private void generateShareLink(String classpageId){
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken());
		AppClientFactory.getInjector().getSearchService().getShortenShareUrl(classpageId, params, getShareShortenUrlAsyncCallback());
	}*/
	

	@Override
	public void generateShareLink(String classpageId) {
		try{
			Map<String, String> params = new HashMap<String, String>();
			params.put("type", AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken());
			AppClientFactory.getInjector().getSearchService().getShortenShareUrl(classpageId, params, getShareShortenUrlAsyncCallback());
		}catch(Exception e){
			
		}
	}
	
	@Override
	protected void onReset() {
//		setInSlot(TYPE_SHELF_TAB, shelfListPresenter);
		
		AppClientFactory.setBrowserWindowTitle(SeoTokens.TEACH_TITLE);
		AppClientFactory.setMetaDataDescription(SeoTokens.HOME_META_DESCRIPTION);
		AppClientFactory.fireEvent(new SetFooterEvent(AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken()));
		String isTab=AppClientFactory.getPlaceManager().getRequestParameter("tab", null);
		if(isTab==null){
			getView().clearPanel();
		}
		if (AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.EDIT_CLASSPAGE) && AppClientFactory.getPlaceManager().refreshPlace()){
			getView().getGlobalClasspageProcess().clear();
			//getClasspage();
		}
		
	}
	
	
	@Override
	public void getClasspageById(final String classpageId) {
		if (!isApiCalled){
			isApiCalled = true;
			getClasspageService().v2GetClasspageById(classpageId, getCollectionAsyncCallback());
		}
	}
	
	
	//// Setters and Getters //
	/** 
	 * This method is to get the assignmentsListAsyncCallback
	 */
	public AsyncCallback<AssignmentsListDo> getAssignmentsListAsyncCallback() {
		return assignmentsListAsyncCallback;
	}

	/** 
	 * This method is to set the assignmentsListAsyncCallback
	 */
	public void setAssignmentsListAsyncCallback(
			SimpleAsyncCallback<AssignmentsListDo> assignmentsListAsyncCallback) {
		this.assignmentsListAsyncCallback = assignmentsListAsyncCallback;
	}
	public void setShareUrlGenerationAsyncCallback(SimpleAsyncCallback<Map<String, String>> shareShortenUrlAsyncCallback) {
		this.shareUrlGenerationAsyncCallback = shareShortenUrlAsyncCallback;
	}

	public SimpleAsyncCallback<Map<String, String>> getShareShortenUrlAsyncCallback() {
		return shareUrlGenerationAsyncCallback;
	}
	
	/** 
	 * This method is to get the assignmentListAsyncCallback
	 */
	public SimpleAsyncCallback<CollectionDo> getAssignmentListAsyncCallback() {
		return assignmentListAsyncCallback;
	}

	/** 
	 * This method is to set the assignmentListAsyncCallback
	 */
	public void setAssignmentListAsyncCallback(
			SimpleAsyncCallback<CollectionDo> assignmentListAsyncCallback) {
		this.assignmentListAsyncCallback = assignmentListAsyncCallback;
	}
	
	/** 
	 * This method is to get the classpageService
	 */
	public ClasspageServiceAsync getClasspageService() {
		return classpageService;
	}

	/** 
	 * This method is to set the classpageService
	 */
	public void setClasspageService(ClasspageServiceAsync classpageService) {
		this.classpageService = classpageService;
	}
	
	/** 
	 * This method is to get the collectionAsyncCallback
	 */
	public SimpleAsyncCallback<CollectionDo> getCollectionAsyncCallback() {
		return collectionAsyncCallback;
	}

	/** 
	 * This method is to set the collectionAsyncCallback
	 */
	public void setCollectionAsyncCallback(
			SimpleAsyncCallback<CollectionDo> collectionAsyncCallback) {
		this.collectionAsyncCallback = collectionAsyncCallback;
	}
	/** 
	 * This method is to get the assignmentAsyncCallback
	 */
	public SimpleAsyncCallback<CollectionItemDo> getAssignmentAsyncCallback() {
		return assignmentAsyncCallback;
	}

	/** 
	 * This method is to set the assignmentAsyncCallback
	 */
	public void setAssignmentAsyncCallback(
			SimpleAsyncCallback<CollectionItemDo> assignmentAsyncCallback) {
		this.assignmentAsyncCallback = assignmentAsyncCallback;
	}
	// Custom Methods //
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
//		if (AppClientFactory.getPlaceManager().refreshPlace()) {
//			getView().resetEditClasspageView();
//		}
		this.classpageId=getPlaceManager().getRequestParameter("classpageid");
		if(classpageDo==null||(!classpageDo.getClasspageId().equals(classpageId))){
			getView().resetEditClasspageView();
		}
	//	classpageDo = null;
		getClasspage();
	}
	/**
	 * 
	 * @function getClasspage 
	 * 
	 * @created_date : 07-Dec-2014
	 * 
	 * @description
	 * 
	 * 
	 * @parm(s) : 
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	public void getClasspage(){
		this.analyticsId= getPlaceManager().getRequestParameter("analyticsId");
		this.monitorId = getPlaceManager().getRequestParameter("monitorid");
		final String sortingOrder=getPlaceManager().getRequestParameter("order",null);
		this.tab=AppClientFactory.getPlaceManager().getRequestParameter("tab", null);
		if(classpageDo==null||(!classpageDo.getClasspageId().equals(classpageId))){
			this.classpageService.getClasspage(classpageId, new SimpleAsyncCallback<ClasspageDo>() {
				@Override
				public void onSuccess(ClasspageDo classpageDo) {
					//If the user is first time logged in user and he/she created the new class at that time it will display this welcome popup.
					AppClientFactory.getInjector().getClasspageService().v2GetClassPartyCustomField(AppClientFactory.getLoggedInUser().getGooruUId(), new SimpleAsyncCallback<ProfilePageDo>() {
						@Override
						public void onSuccess(ProfilePageDo result) {
							if(result!=null && result.getOptionalValue() !=null && result.getOptionalValue().equalsIgnoreCase("true")){
								new WelcomeClassView(true);
								isLoggedInUser=true;
								Window.enableScrolling(false);
							}
						}
					});
					EditClasspagePresenter.this.classpageDo=classpageDo;
					if(classpageDo.getPermissions()!=null&&classpageDo.getPermissions().contains("edit")&& classpageDo.getClasspageId() != null){
						offset=0;
						limit=5;
						generateShareLink(classpageDo.getClasspageId());
						getView().setSortingOrderInDropdown(sortingOrder);
						if(sortingOrder!=null&&(sortingOrder.equals("earliest")||sortingOrder.equals("latest"))){
							getAssignmentsCount(classpageDo.getClasspageId(),getOffsetValue().toString(),limit.toString(),tab,analyticsId,monitorId,sortingOrder);
						}else{
							getClasspageItems(classpageDo.getClasspageId(),getOffsetValue().toString(),limit.toString(),tab,analyticsId,monitorId,sortingOrder,0);
						}
						getAssignmentsProgress(classpageId, "0", "20");	// to display assignment progress.
                        getView().setClasspageData(classpageDo);
                        classlistPresenter.setClassPageDo(classpageDo);
                        setInSlot(CLASSLIST_SLOT, classlistPresenter,false);
						triggerClassPageNewDataLogStartStopEvent(classpageDo.getClasspageId(), classpageDo.getClasspageCode());
					} else {
						ErrorPopup error = new ErrorPopup(i18n.GL0341());
						error.center();
						error.show();
					}
				}
			});
		}else{
			getView().setSortingOrderInDropdown(sortingOrder);
			getView().hideNoAssignmentsMessagePanel();
			if(sortingOrder!=null&&(sortingOrder.equals("earliest")||sortingOrder.equals("latest"))){
				getAssignmentsCount(classpageDo.getClasspageId(),getOffsetValue().toString(),limit.toString(),tab,analyticsId,monitorId,sortingOrder);
			}else{
				getClasspageItems(classpageDo.getClasspageId(),getOffsetValue().toString(),limit.toString(),tab,analyticsId,monitorId,sortingOrder,0);
			}
		}
	}
	/**
	 * 
	 * @function getOffsetValue 
	 * 
	 * @created_date : 07-Dec-2014
	 * 
	 * @description
	 * 
	 * 
	 * @parm(s) : @return
	 * 
	 * @return : Integer
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	public Integer getOffsetValue(){
		String pageNum=getPlaceManager().getRequestParameter("pageNum","1");
		int pageNumber=0;
		try{
			pageNumber=Integer.parseInt(pageNum);
			if(pageNumber==0){
				pageNumber=1;
			}
		}catch(Exception e){}
		
		return (((pageNumber-1)*DEFAULT_LIMITVALUE));
		
	}
	/**
	 * 
	 * @function getClasspageItems 
	 * 
	 * @created_date : 07-Dec-2014
	 * 
	 * @description
	 * 
	 * 
	 * @parm(s) : @param classpageId
	 * @parm(s) : @param offset
	 * @parm(s) : @param limit
	 * @parm(s) : @param tab
	 * @parm(s) : @param analyticsId
	 * @parm(s) : @param monitorId
	 * @parm(s) : @param sortingOrder
	 * @parm(s) : @param assignemntCount
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	public void getClasspageItems(String classpageId,String offset,String limit, final String tab, final String analyticsId, final String monitorId,final String sortingOrder,final int assignemntCount){
		this.classpageService.getClassPageItems(classpageId, offset, limit,sortingOrder,null, new SimpleAsyncCallback<ArrayList<ClasspageItemDo>>() {
			@Override
			public void onSuccess(ArrayList<ClasspageItemDo> classpageItemsList) {
				if(classpageItemsList!=null){
					getView().showClasspageItems(classpageItemsList, tab, analyticsId,monitorId,classlistPresenter,assignemntCount);
				}
			}
		});
	}
	/**
	 * 
	 * @function getAssignmentsCount 
	 * 
	 * @created_date : 07-Dec-2014
	 * 
	 * @description
	 * 
	 * 
	 * @parm(s) : @param classpageId
	 * @parm(s) : @param offset
	 * @parm(s) : @param limit
	 * @parm(s) : @param tab
	 * @parm(s) : @param analyticsId
	 * @parm(s) : @param monitorId
	 * @parm(s) : @param sortingOrder
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	public void getAssignmentsCount(final String classpageId,final String offset,final String limit, final String tab, final String analyticsId, final String monitorId,final String sortingOrder){
		this.classpageService.getClassPageItems(classpageId, "0", "1",null,null, new SimpleAsyncCallback<ArrayList<ClasspageItemDo>>() {
			@Override
			public void onSuccess(ArrayList<ClasspageItemDo> classpageItemsList) {
				if(classpageItemsList!=null){
					getClasspageItems(classpageId,offset,limit,tab,analyticsId,monitorId,sortingOrder,classpageItemsList.size());
				}else{
					getClasspageItems(classpageId,offset,limit,tab,analyticsId,monitorId,sortingOrder,0);
				}
			}
		});
	}
	@Override
	public void getNextClasspageItems(Integer offset,Integer limit) 
	{
		String classpageId=getPlaceManager().getRequestParameter("classpageid");
		String analyticsId=getPlaceManager().getRequestParameter("analyticsId");
		String monitorId=getPlaceManager().getRequestParameter("monitorid");
		//getClasspageItems( classpageId,offset.toString(),limit.toString(), tab, analyticsId,monitorId,null);
	}
	/**
	 * 
	 * @function initParam 
	 * 
	 * @created_date : 07-Dec-2014
	 * 
	 * @description
	 * 
	 * 
	 * @parm(s) : 
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	private void initParam() {
		classpageId = getPlaceManager().getRequestParameter("classpageid");
		String pageSize = getPlaceManager().getRequestParameter("pageSize");
		String pageNum = getPlaceManager().getRequestParameter("pageNum");
		String pos = getPlaceManager().getRequestParameter("pos");
		getView().setClasspageId(classpageId);
		getView().getClasspageById(classpageId, pageSize, pageNum, pos);
//		getView().getAssignemntsByClasspageId(classpageId, pageSize, pageNum);
//		getAssignmentsByClasspageById(classpageId, pageSize, pageNum);
//		generateShareLink(classpageId);
	}

	@Override
	public void addAssignmentsContainerPopup(String classpageId) {
		this.classpageId = classpageId;
		assignmentContainer.setClasspageId(getPlaceManager().getRequestParameter("classpageid"),this);
		assignmentContainer.getUserShelfData();
		assignmentContainer.setEditClasspagePresenter(this);
		addToPopupSlot(assignmentContainer);
	}

	@Override
	public void insertAssignment(CollectionItemDo collectionItemDo) {	
		
//		getView().setAssignmentData(collectionItemDo,false);
	}
	
	// @description to get all Assignments of a particular classpages

	@Override
	public void getAssignmentsByClasspageById(String classpageId, String pageSize, String pageNum) {
		getClasspageService().v2GetAssignemtsByClasspageId(classpageId,pageSize,pageNum, getAssignmentsListAsyncCallback());
	}


	@Override
	public void refreshAssignmentsList(boolean isPostDeleteAssignment) {
		getView().onDeleteAssignment(isPostDeleteAssignment);
	}
	
	@Override
	public void updateClassPageInfo(String classPageId,String collectionType, String title) {
		getView().getCollectionTitleUc().switchToEdit();
		getClasspageService().v2UpdateClassPageByid(classPageId,collectionType, title, null, getUpdateAssignmentAsyncCallback());
	}
	public SimpleAsyncCallback<CollectionDo> getUpdateAssignmentAsyncCallback() {
		if (updateAssignmentAsyncCallback == null) {
			updateAssignmentAsyncCallback = new SimpleAsyncCallback<CollectionDo>() {

				@Override
				public void onSuccess(CollectionDo result) {
					getView().onPostClassPageUpdate();
				}
			};
		}
		return updateAssignmentAsyncCallback;
	}
	/**
	 * 
	 * @function setUpdateAssignmentAsyncCallback 
	 * 
	 * @created_date : 07-Dec-2014
	 * 
	 * @description
	 * 
	 * 
	 * @parm(s) : @param updateAssignmentAsyncCallback
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	public void setUpdateAssignmentAsyncCallback(
			SimpleAsyncCallback<CollectionDo> updateAssignmentAsyncCallback) {
		this.updateAssignmentAsyncCallback = updateAssignmentAsyncCallback;
	}

	@Override
	public void showImageUploadWidget() {
		imageUploadPresenter.setCollectionImage(false);
		imageUploadPresenter.setClassPageImage(true);
		imageUploadPresenter.setEditResourceImage(false);
		imageUploadPresenter.setClasspageId(classpageId);
		addToPopupSlot(imageUploadPresenter);
	}
	
	@Override
	public void setUploadedImageToClassPage(String url){
		getView().setUploadedImageToClassPage(url);
	}
	public void setClasspageItemDo(ClasspageItemDo classpageItemDo){
		getView().setClasspageItemOnTop(classpageItemDo);
	}
	/**
	 * 
	 * @function triggerClassPageNewDataLogStartStopEvent 
	 * 
	 * @created_date : 07-Dec-2014
	 * 
	 * @description
	 * 
	 * 
	 * @parm(s) : @param classpageId
	 * @parm(s) : @param classCode
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	public void triggerClassPageNewDataLogStartStopEvent(String classpageId, String classCode){
		JSONObject classpageDataLog=new JSONObject();
		String classpageEventId=GwtUUIDGenerator.uuid();
		AppClientFactory.getPlaceManager().setClasspageEventId(classpageEventId);
		classpageDataLog.put(PlayerDataLogEvents.EVENTID, new JSONString(classpageEventId));
		classpageDataLog.put(PlayerDataLogEvents.EVENTNAME, new JSONString(PlayerDataLogEvents.CLASSPAGE_VIEW));
		classpageDataLog.put(PlayerDataLogEvents.SESSION, PlayerDataLogEvents.getDataLogSessionObject(null));
		classpageDataLog.put(PlayerDataLogEvents.USER, PlayerDataLogEvents.getDataLogUserObject());
		classpageDataLog.put(PlayerDataLogEvents.STARTTIME, new JSONNumber(PlayerDataLogEvents.getUnixTime()));
		classpageDataLog.put(PlayerDataLogEvents.ENDTIME, new JSONNumber(PlayerDataLogEvents.getUnixTime()));
		classpageDataLog.put(PlayerDataLogEvents.CONTEXT, PlayerDataLogEvents.getDataLogContextObject(classpageId, "", "", "", "","",null,classpageId,"teach"));
		classpageDataLog.put(PlayerDataLogEvents.VERSION,PlayerDataLogEvents.getDataLogVersionObject());
		classpageDataLog.put(PlayerDataLogEvents.METRICS,PlayerDataLogEvents.getDataLogMetricsObject(0L, 0));
		classpageDataLog.put(PlayerDataLogEvents.PAYLOADOBJECT,PlayerDataLogEvents.getClassPagePayLoadObject(classCode));
		PlayerDataLogEvents.collectionStartStopEvent(classpageDataLog);
	}


	@Override
	public void getAssignmentsProgress(String classpageId, String offsetProgress, String limitProgress) {
		this.classpageService.getClassPageItems(classpageId, offsetProgress, limitProgress,null,null, new SimpleAsyncCallback<ArrayList<ClasspageItemDo>>() {
			
			@Override
			public void onSuccess(ArrayList<ClasspageItemDo> classpageItemsList) {
				if(classpageItemsList!=null){
					getView().displayAssignmentPath(classpageItemsList);
					//getClasspage();
				}
			}
		});
	}

	@Override
	public void setCollectionProgressData(String clickedTab,String collectionId,String collectionTitle) {
		clearSlot(SLOT_SET_SUMMARY_PROGRESS);
		if(clickedTab!=null){
			if(clickedTab.equalsIgnoreCase(SUMMARY)){
				analyticsPresenter.getCollectionSummaryPresenter().setCollectionSummaryData(collectionId,"");
				setInSlot(SLOT_SET_SUMMARY_PROGRESS, analyticsPresenter.getCollectionSummaryPresenter(),false);
			}else if(clickedTab.equalsIgnoreCase(PROGRESS)){
				analyticsPresenter.getCollectionProgressPresenter().setCollectionProgressData(collectionId, "", false, collectionTitle);
				setInSlot(SLOT_SET_SUMMARY_PROGRESS, analyticsPresenter.getCollectionProgressPresenter(),false);	
			}else if(clickedTab.equalsIgnoreCase(REPORTS)){
				analyticsPresenter.getGradeCollectionJson();
				setInSlot(SLOT_SET_SUMMARY_PROGRESS, analyticsPresenter,false);	
			}
		}
	}
}
