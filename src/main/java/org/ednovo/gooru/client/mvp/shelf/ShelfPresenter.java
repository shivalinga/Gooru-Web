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
package org.ednovo.gooru.client.mvp.shelf;

import java.util.HashMap;
import java.util.Map;

import org.ednovo.gooru.client.AppPlaceKeeper;
import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SeoTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.event.ActivateSearchBarEvent;
import org.ednovo.gooru.client.event.InvokeLoginEvent;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BasePlacePresenter;
import org.ednovo.gooru.client.mvp.authentication.SignUpPresenter;
import org.ednovo.gooru.client.mvp.folders.event.RefreshFolderType;
import org.ednovo.gooru.client.mvp.home.AlmostDoneUc;
import org.ednovo.gooru.client.mvp.home.event.HeaderTabType;
import org.ednovo.gooru.client.mvp.home.event.HomeEvent;
import org.ednovo.gooru.client.mvp.image.upload.ImageUploadPresenter;
import org.ednovo.gooru.client.mvp.search.event.ConfirmStatusPopupEvent;
import org.ednovo.gooru.client.mvp.search.event.SetCollabCountEvent;
import org.ednovo.gooru.client.mvp.search.event.SetCollabCountHandler;
import org.ednovo.gooru.client.mvp.search.event.SetFooterEvent;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderZIndexEvent;
import org.ednovo.gooru.client.mvp.shelf.collection.folders.FolderItemTabPresenter;
import org.ednovo.gooru.client.mvp.shelf.collection.folders.events.RefreshFolderItemEvent;
import org.ednovo.gooru.client.mvp.shelf.collection.folders.events.SetFolderMetaDataEvent;
import org.ednovo.gooru.client.mvp.shelf.collection.folders.events.SetFolderParentNameEvent;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.assign.CollectionAssignTabPresenter;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.collaborators.CollectionCollaboratorsTabPresenter;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.info.CollectionInfoTabPresenter;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.CollectionResourceTabPresenter;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.item.ShelfCollectionResourceChildView;
import org.ednovo.gooru.client.mvp.shelf.event.CollectionAssignShareEvent;
import org.ednovo.gooru.client.mvp.shelf.event.CollectionAssignShareHandler;
import org.ednovo.gooru.client.mvp.shelf.event.GetEditPageHeightEvent;
import org.ednovo.gooru.client.mvp.shelf.event.RefreshCollectionInShelfListEvent;
import org.ednovo.gooru.client.mvp.shelf.event.RefreshType;
import org.ednovo.gooru.client.mvp.shelf.event.RefreshUserShelfCollectionsEvent;
import org.ednovo.gooru.client.mvp.shelf.event.UpdateResourceCountEvent;
import org.ednovo.gooru.client.mvp.shelf.list.ShelfListPresenter;
import org.ednovo.gooru.client.service.ResourceServiceAsync;
import org.ednovo.gooru.client.service.ShelfServiceAsync;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.content.CollectionDo;
import org.ednovo.gooru.shared.model.content.MetaDO;
import org.ednovo.gooru.shared.model.folder.FolderDo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

/**
 * @author Search Team
 * 
 */
public class ShelfPresenter extends BasePlacePresenter<IsShelfView, ShelfPresenter.IsShelfProxy> implements ShelfUiHandlers {

	@Inject
	private ShelfServiceAsync shelfService;

	@Inject
	private ResourceServiceAsync resourceService;

	private ShelfListPresenter shelfListPresenter;

	private CollectionResourceTabPresenter collectionResourceTabPresenter;

	private CollectionInfoTabPresenter collectionInfoTabPresenter;
	
	private CollectionAssignTabPresenter collectionAssignTabPresenter;
	
	private CollectionCollaboratorsTabPresenter collectionCollaboratorsTabPresenter;

	private FolderItemTabPresenter folderItemTabPresenter;
	
	private SimpleAsyncCallback<CollectionDo> collectionAsyncCallback;

	private SimpleAsyncCallback<CollectionDo> copyCollectionAsyncCallback;
	
	private SimpleAsyncCallback<MetaDO> permissionsAsyncCallback;

	private SimpleAsyncCallback<Void> deleteCollectionAsyncCallback;

	private ImageUploadPresenter imageUploadPresenter;

	private SimpleAsyncCallback<CollectionDo> updateCollectionAsyncCallback;

	private HandlerRegistration viewClickRegistration;

	private CollectionDo collectionDo;

	private String folderParentName = "";
	
	private boolean isPageRefreshed = true;
	
	ErrorPopup errorPopup = null;
	
	boolean isApiCalled=false;
	
    private String O1_LEVEL_VALUE = null, O2_LEVEL_VALUE = null, O3_LEVEL_VALUE = null;
	
	private String parentId, id=null;
	
	SignUpPresenter signUpViewPresenter = null;
	
	Map<String,String> folderMetaData = new HashMap<String,String>();
	
	private MessageProperties i18n = GWT.create(MessageProperties.class);
	
	private static final String CALLBACK = "callback";
	
	public static final String ASSESSMENT = "quiz";
	
	@ProxyCodeSplit
	@NameToken(PlaceTokens.SHELF)
	@UseGatekeeper(AppPlaceKeeper.class)
	public interface IsShelfProxy extends ProxyPlace<ShelfPresenter> {

	}

	/**
	 * class constructor to set all class of instance
	 * 
	 * @param imageUploadPresenter
	 *            instance of {@link ImageUploadPresenter}
	 * @param shelfTabPresenter
	 *            instance of {@link ShelfListPresenter}
	 * @param collectionResourceTabPresenter
	 *            instance of {@link CollectionResourceTabPresenter}
	 * @param collectionInfoTabPresenter
	 *            instance {@link CollectionInfoTabPresenter}
	 * @param collectionAssignTabPresenter
	 *            instance {@link CollectionAssignTabPresenter}
	 * @param view
	 *            {@link View}
	 * @param proxy
	 *            {@link Proxy}
	 */
	@Inject
	public ShelfPresenter(SignUpPresenter signUpViewPresenter,ImageUploadPresenter imageUploadPresenter, ShelfListPresenter shelfTabPresenter, CollectionResourceTabPresenter collectionResourceTabPresenter, CollectionInfoTabPresenter collectionInfoTabPresenter, CollectionAssignTabPresenter collectionAssignTabPresenter,CollectionCollaboratorsTabPresenter collectionCollaboratorsTabPresenter, FolderItemTabPresenter folderItemTabPresenter, IsShelfView view, IsShelfProxy proxy) {
		super(view, proxy);
		getView().setUiHandlers(this);
		getView().getLoadingImageVisible();
		this.signUpViewPresenter = signUpViewPresenter;
		this.shelfListPresenter = shelfTabPresenter;
		this.collectionResourceTabPresenter = collectionResourceTabPresenter;
		this.collectionInfoTabPresenter = collectionInfoTabPresenter;
		this.collectionAssignTabPresenter = collectionAssignTabPresenter;
		this.collectionCollaboratorsTabPresenter = collectionCollaboratorsTabPresenter;
		this.imageUploadPresenter = imageUploadPresenter;
		this.folderItemTabPresenter = folderItemTabPresenter;
		addRegisteredHandler(GetEditPageHeightEvent.TYPE, this);
		addRegisteredHandler(UpdateResourceCountEvent.TYPE, this);
		Document doc = Document.get();
		doc.getBody().setClassName(""); 
		
		CollectionAssignShareHandler handler = new CollectionAssignShareHandler() {
			
			@Override
			public void updateShareType(String shareType,String publishStatus,boolean isPublish,CollectionDo collection) {
				if(isPublish){
					getView().setPusblishStatus(publishStatus, collection);
				}else{
					collectionDo.setSharing(shareType);
					getView().setCollectionAnalyticsVisibility(shareType);
				}
			}
		};
		
		SetCollabCountHandler setCollabCount = new SetCollabCountHandler() {
			@Override
			public void setCollabCountBy(String type, Integer count) {
				getView().setCollabCountByType(type, count);
			}
		};
		
		addRegisteredHandler(CollectionAssignShareEvent.TYPE, handler);
		addRegisteredHandler(SetCollabCountEvent.TYPE, setCollabCount);
		addRegisteredHandler(SetFolderParentNameEvent.TYPE, this);
		addRegisteredHandler(SetFolderMetaDataEvent.TYPE, this);
	}
	
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		callBackMethods();
	}

	private void callBackMethods(){
		if (getPlaceManager().getRequestParameter(CALLBACK) != null && getPlaceManager().getRequestParameter(CALLBACK).equalsIgnoreCase("signup")) {
			//To show SignUp (Registration popup)
			if (AppClientFactory.isAnonymous()){
				Window.enableScrolling(false);
				AppClientFactory.fireEvent(new SetHeaderZIndexEvent(98, false));
				String type = getPlaceManager().getRequestParameter("type") ;
				int displayScreen =getPlaceManager().getRequestParameter("type") !=null  ? Integer.parseInt(type) : 1;
				signUpViewPresenter.displayPopup(displayScreen);
				addToPopupSlot(signUpViewPresenter);
			}
		}
		int flag = AppClientFactory.getLoggedInUser().getViewFlag();
		final String loginType = AppClientFactory.getLoggedInUser().getLoginType() !=null ? AppClientFactory.getLoggedInUser().getLoginType() : "";
		if(!AppClientFactory.isAnonymous() && flag==0 &&  loginType.equalsIgnoreCase("apps")) {
			AlmostDoneUc update = new AlmostDoneUc(AppClientFactory.getLoggedInUser().getEmailId(), AppClientFactory.getLoggedInUser());
			update.setGlassEnabled(true);
			update.show();
			update.center();
		}
	}
	
	
	@Override
	public void onBind() {
		super.onBind();
		setCollectionAsyncCallback(new SimpleAsyncCallback<CollectionDo>() {

			@Override
			public void onSuccess(CollectionDo collection) {
				isApiCalled= false;
				if (collection.getStatusCode()==200){
					if(collection.getMeta() != null){
						if (collection.getMeta().getPermissions().toString().contains("edit")){
							getView().setCollection(collection);
							fireEvent(new RefreshCollectionInShelfListEvent(collection, RefreshType.OPEN));
							getView().getLoadingImageInvisible();
						}else{
							getView().getLoadingImageLabel().setVisible(false);
							errorPopup = null;
							invokeErrorPopup();
						}
					}
				}else{
					getView().getLoadingImageLabel().setVisible(false);
					errorPopup = null;
					invokeErrorPopup();
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				super.onFailure(caught);
				AppClientFactory.fireEvent(new RefreshCollectionInShelfListEvent(null, RefreshType.OPEN));
			}
		});
	}
	
	@Override
	public void onUnbind(){
		super.onUnbind();
	}
	
	@Override
	protected void onReveal() {
		super.onReveal();
		viewClickRegistration = RootPanel.get().addDomHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ShelfCollectionResourceChildView.checkEditState();
			}
		}, ClickEvent.getType());
		
		AppClientFactory.fireEvent(new HomeEvent(HeaderTabType.ORGANIZE));
		AppClientFactory.setBrowserWindowTitle(SeoTokens.WORKSPACE_TITLE);
		AppClientFactory.setMetaDataDescription(SeoTokens.HOME_META_DESCRIPTION);
		String idParm = AppClientFactory.getPlaceManager().getRequestParameter("id") !=null && !AppClientFactory.getPlaceManager().getRequestParameter("id").equalsIgnoreCase("") ? AppClientFactory.getPlaceManager().getRequestParameter("id") : null;
		if (idParm != null && AppClientFactory.isAnonymous()){
			AppClientFactory.fireEvent(new InvokeLoginEvent());
			
		}else if(AppClientFactory.isAnonymous()){
			Window.enableScrolling(true);
			getView().setOnlyNoDataCollection();
		} else {
			getView().setBackToSearch();
			
			fireEvent(new ActivateSearchBarEvent(true));
			
			String id = getPlaceManager().getRequestParameter("id", "INVALID");
			if(id.equalsIgnoreCase("INVALID") && !AppClientFactory.isAnonymous())
			{
				getView().setNoDataCollection();
				Window.enableScrolling(true);
				AppClientFactory.fireEvent(new SetHeaderZIndexEvent(0, true));
			}
			getView().setBalloonPopup();
			Document doc = Document.get();
			doc.getElementById("uvTab").getStyle().setDisplay(Display.BLOCK);
			AppClientFactory.fireEvent(new SetFooterEvent(AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken()));
			//Call Event for Setting Confirm popup
			AppClientFactory.fireEvent(new ConfirmStatusPopupEvent(true));
		}		
	}

	@Override
	protected void onReset() {
		super.onReset();
		Window.scrollTo(0, 0);
		if(!AppClientFactory.isAnonymous()) {
			String id = getPlaceManager().getRequestParameter("id");
			String o1 = getPlaceManager().getRequestParameter("o1");
			String o2 = getPlaceManager().getRequestParameter("o2");
			String o3 = getPlaceManager().getRequestParameter("o3");
			
			if(o3!=null&&id==null) {
				setFoldersSlot(o3);
			} else if(o2!=null&&id==null) {
				setFoldersSlot(o2);
			} else if(o1!=null&&id==null) {
				setFoldersSlot(o1);
			} else if (id!=null && AppClientFactory.isAnonymous()) {
				AppClientFactory.getPlaceManager().redirectPlace(PlaceTokens.SHELF);
			} else if (AppClientFactory.getPlaceManager().refreshPlace()) {
				String eventType = getPlaceManager().getRequestParameter("eventType");
				if(eventType!=null) {
					AppClientFactory.fireEvent(new RefreshUserShelfCollectionsEvent());
				}
				if (id!=null) {
					getView().getFolderListPanel().clear();
					getView().getFolderListPanel().setVisible(false);
					getView().getLoadingImageVisible();
					
					if (!isApiCalled){
						isApiCalled= true;
						getResourceService().getCollection(id, false, getCollectionAsyncCallback());
					}
				}else{
					getView().getFolderListPanel().setVisible(true);
					setFoldersSlot(null);
//					Window.enableScrolling(true);
					AppClientFactory.fireEvent(new SetHeaderZIndexEvent(0, true));
				}
				collectionInfoTabPresenter.getView().reset();
				collectionResourceTabPresenter.getView().reset();
				collectionAssignTabPresenter.getView().reset();
				collectionCollaboratorsTabPresenter.getView().reset();
			}
			setInSlot(TYPE_SHELF_TAB, shelfListPresenter);
			AppClientFactory.fireEvent(new SetFooterEvent(AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken()));
		}else{
//			getView().setOnlyNoDataCollection();
			Window.enableScrolling(true);
		}
		String idParm = AppClientFactory.getPlaceManager().getRequestParameter("id") !=null && !AppClientFactory.getPlaceManager().getRequestParameter("id").equalsIgnoreCase("") ? AppClientFactory.getPlaceManager().getRequestParameter("id") : null;
		int windowHeight=Window.getClientHeight();
		getView().getEditPanel().getElement().getStyle().setHeight(windowHeight, Unit.PX);
		getView().getEditPanel().getElement().getStyle().setOverflowY(Overflow.AUTO);
		if (idParm == null){
			
			getView().getEditPanel().getElement().getStyle().setMarginTop(38, Unit.PX);
		}else{
			/*getView().getEditPanel().getElement().getStyle().clearHeight();
			getView().getEditPanel().getElement().getStyle().clearOverflowY();*/
			getView().getEditPanel().getElement().getStyle().clearMarginTop();
		}
	}
	
	@Override
	protected void onHide() {
		super.onHide();
		viewClickRegistration.removeHandler();
		getView().hideAllOpenedPopUp();
		imageUploadPresenter.getView().closeImageUploadWidget();
//		collectionResourceTabPresenter.closePopUp();
		
	}

	@Override
	public void deleteCollection(String collectionId) {
		this.getResourceService().deleteCollection(collectionId, getDeleteCollectionAsyncCallback());
	}

	public ShelfServiceAsync getShelfService() {
		return shelfService;
	}

	public ResourceServiceAsync getResourceService() {
		return resourceService;
	}

	public void setResourceService(ResourceServiceAsync resourceService) {
		this.resourceService = resourceService;
	}

	public SimpleAsyncCallback<CollectionDo> getCollectionAsyncCallback() {
		return collectionAsyncCallback;
	}
	

	/**
	 * @return instance of {@link CollectionDo}
	 */
	public SimpleAsyncCallback<CollectionDo> getCopyCollectionAsyncCallback() {
		if (copyCollectionAsyncCallback == null) {
			copyCollectionAsyncCallback = new SimpleAsyncCallback<CollectionDo>() {

				@Override
				public void onSuccess(CollectionDo result) {
					FolderDo folderDo=getView().getFolderDo(result);
//					AppClientFactory.fireEvent(new RefreshCollectionInShelfListEvent(result, RefreshType.INSERT_AND_VIEW));
					HashMap<String,String> params = new HashMap<String,String>();
					if(O3_LEVEL_VALUE!=null) {
						params.put("o3", O3_LEVEL_VALUE);
					}
					if(O2_LEVEL_VALUE!=null) {
						params.put("o2", O2_LEVEL_VALUE);
					}
					if(O1_LEVEL_VALUE!=null) {
						params.put("o1", O1_LEVEL_VALUE);
					}
					AppClientFactory.fireEvent(new RefreshFolderItemEvent(folderDo, RefreshFolderType.INSERT_AND_VIEW, params)); 
					getView().getLoadingImageInvisible();
					getView().editCopyCollectionTitle();
				}
			};
		}
		return copyCollectionAsyncCallback;
	}
		
//	public SimpleAsyncCallback<MetaDO> getPermissionsAsyncCallback() {
//		if (permissionsAsyncCallback == null) {
//			permissionsAsyncCallback = new SimpleAsyncCallback<MetaDO>() {
//
//				@Override
//				public void onSuccess(MetaDO result) {
//					if (result.getPermissions().size() != 0) {
//						getView().getLoadingImageVisible();
//						String Values = result.getPermissions().toString();
////						for (int i = 0; i < result.getPermissions().size(); i++) {
//							if (Values.contains("edit")) {
//								String id = getPlaceManager()
//										.getRequestParameter("id", "INVALID");
////								getResourceService().getCollection(id, false,
////										getCollectionAsyncCallback());
//							} else {
////								invokeErrorPopup();
//							}
////						}
//					} else {
////						invokeErrorPopup();
//					}
//					//AppClientFactory.fireEvent(new RefreshCollectionInShelfListEvent(result, RefreshType.INSERT_AND_VIEW));
//				}
//			};
//		}
//		return permissionsAsyncCallback;
//	}
	/**
	 * @param collectionAsyncCallback
	 *            instance of {@link CollectionDo}
	 */
	public void setCollectionAsyncCallback(SimpleAsyncCallback<CollectionDo> collectionAsyncCallback) {
		this.collectionAsyncCallback = collectionAsyncCallback;
	}

	public SimpleAsyncCallback<Void> getDeleteCollectionAsyncCallback() {
		if (deleteCollectionAsyncCallback == null) {
			deleteCollectionAsyncCallback = new SimpleAsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					getView().setPersistantTabFlag("resourceTab");
					getView().onPostCollectionDelete();
				}
			};
		}
		return deleteCollectionAsyncCallback;
	}

	/**
	 * @return instance of {@link CollectionDo}
	 */
	public SimpleAsyncCallback<CollectionDo> getUpdateCollectionAsyncCallback() {
		if (updateCollectionAsyncCallback == null) {
			updateCollectionAsyncCallback = new SimpleAsyncCallback<CollectionDo>() {

				@Override
				public void onSuccess(CollectionDo result) {
					getView().onPostCollectionUpdate();
				}
			};
		}
		return updateCollectionAsyncCallback;
	}

	@Override
	public String getViewToken() {
		return PlaceTokens.SHELF;
	}

	@Override
	public void revealTab(Type<RevealContentHandler<?>> tabType, CollectionDo collectionDo) {
		this.collectionDo = collectionDo;
		if (tabType.equals(TYPE_COLLECTION_RESOURCE_TAB)) {
			addToSlot(TYPE_COLLECTION_RESOURCE_TAB, collectionResourceTabPresenter);
			collectionResourceTabPresenter.getView().setData(collectionDo);
		} else if (tabType.equals(TYPE_COLLECTION_INFO_TAB)) {
			addToSlot(TYPE_COLLECTION_INFO_TAB, collectionInfoTabPresenter);
			collectionInfoTabPresenter.getView().setData(collectionDo);
		}else if(tabType.equals(TYPE_ASSIGN_INFO_TAB)){
			addToSlot(TYPE_ASSIGN_INFO_TAB, collectionAssignTabPresenter);
			collectionAssignTabPresenter.getClasspage(collectionDo, collectionDo.getSharing());
		}else if(tabType.equals(TYPE_COLLABORATOR_TAB)){
			addToSlot(TYPE_COLLABORATOR_TAB, collectionCollaboratorsTabPresenter);
			collectionCollaboratorsTabPresenter.setData(collectionDo);
		}
		
	}

	@Override
	public void clearTabSlot() {
		clearSlot(TYPE_COLLECTION_RESOURCE_TAB);
		clearSlot(TYPE_COLLECTION_INFO_TAB);
		clearSlot(TYPE_ASSIGN_INFO_TAB);
		clearSlot(TYPE_COLLABORATOR_TAB);
	}

	@Override
	public void imageUpload() {
		addToPopupSlot(imageUploadPresenter);
	}

	@Override
	public void updateCollectionInfo(String collectionId, String title, String description) {
		getResourceService().updateCollectionMetadata(collectionId, title, description, null, null, null, null, null, null, null, getUpdateCollectionAsyncCallback());
	}
	

	@Override
	public void copyCollection(String collectionUid, boolean addToShelf) {
		CollectionDo collection = new CollectionDo();
		collection.setGooruOid(collectionUid);
		setFolderUrlParams();
		AppClientFactory.getInjector().getfolderService().copyDraggedCollectionIntoFolder(collection, " ", parentId, addToShelf, getCopyCollectionAsyncCallback());
	}

	
	public  void getEditPageHeight(PopupPanel editQuestionPopupPanel,boolean isHeightClear){
		
        int height=1230+editQuestionPopupPanel.getAbsoluteTop();
        int containerHeight=getView().getShelfViewMainContainer().getOffsetHeight();
        containerHeight=containerHeight+getView().getShelfViewMainContainer().getAbsoluteTop();
        if(isHeightClear){
                getView().getShelfViewMainContainer().getElement().getStyle().clearHeight();
        }else{
                if(height >= containerHeight){                
                        int adjustableHeight=height-containerHeight;
                        getView().getShelfViewMainContainer().setHeight((containerHeight+adjustableHeight)+"px");
                        increaseGlassHeight(editQuestionPopupPanel.getElement().getPreviousSiblingElement());
                }else{
                	
                }
        }
        
	}

	public void increaseGlassHeight(Element glass){
	        
	         Style style = glass.getStyle();
	
	      int winWidth = Window.getClientWidth();
	      int winHeight = Window.getClientHeight();
	
	      // Hide the glass while checking the document size. Otherwise it would
	      // interfere with the measurement.
	      style.setDisplay(Display.NONE);
	      style.setWidth(0, Unit.PX);
	      style.setHeight(0, Unit.PX);
	
	      int width = Document.get().getScrollWidth();
	      int height = Document.get().getScrollHeight();
	
	      // Set the glass size to the larger of the window's client size or the
	      // document's scroll size.
	      style.setWidth(Math.max(width, winWidth), Unit.PX);
	      style.setHeight(Math.max(height, winHeight), Unit.PX);
	
	      // The size is set. Show the glass again.
	      style.setDisplay(Display.BLOCK);
	}
	
	@Override
	public void updateResourceCount(int resourceCount) {
		getView().updateResoureCount(resourceCount);
		
	}
	
	@Override
	public void setFoldersSlot(String parentId) {
		clearSlot(TYPE_FOLDERS_SLOT);
		setInSlot(TYPE_FOLDERS_SLOT, folderItemTabPresenter);
		getView().getFolderListPanel().setVisible(true);
		folderItemTabPresenter.setFolderData(parentId, folderParentName, 1);
	}
	
	@Override
	public void setFolderParentName(String folderParentName) {
		folderItemTabPresenter.setFolderTitle(folderParentName);
		this.folderParentName = folderParentName;
	}

	@Override
	public void moveCollection(final String sourceId, final String targetId,final String folderName,final HashMap<String, String> params) { 
		AppClientFactory.getInjector().getfolderService().moveCollectionIntoFolder(sourceId,targetId,new SimpleAsyncCallback<Void>(){
			@Override
			public void onSuccess(Void result) {
				getView().CollMovedSucessFully(sourceId,targetId,folderName,params); 
			}
			
		});
	}
	
	private void setFolderUrlParams() {
		O1_LEVEL_VALUE = AppClientFactory.getPlaceManager().getRequestParameter("o1");
		O2_LEVEL_VALUE = AppClientFactory.getPlaceManager().getRequestParameter("o2");
		O3_LEVEL_VALUE = AppClientFactory.getPlaceManager().getRequestParameter("o3");
		if(O3_LEVEL_VALUE!=null){
			parentId=O3_LEVEL_VALUE;
		}else if(O2_LEVEL_VALUE!=null){
			parentId=O2_LEVEL_VALUE;
		}else if(O1_LEVEL_VALUE!=null){
			parentId=O1_LEVEL_VALUE;
		}
	}
	
	public void invokeErrorPopup(){
		if (errorPopup == null){
			errorPopup = new ErrorPopup(i18n.GL0340());
			errorPopup.show();
			errorPopup.center();
		}
	}

	@Override
	public void setFolderMetaData(Map<String, String> folderMetaData) {
		folderItemTabPresenter.setFolderMetaData(folderMetaData);
		this.folderMetaData = folderMetaData;
	}
	
}
