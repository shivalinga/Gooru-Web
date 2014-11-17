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
package org.ednovo.gooru.client.mvp.shelf.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.event.InvokeLoginEvent;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BaseViewWithHandlers;
import org.ednovo.gooru.client.mvp.dnd.Draggable;
import org.ednovo.gooru.client.mvp.dnd.DropBox;
import org.ednovo.gooru.client.mvp.dnd.IsDraggable.DRAG_TYPE;
import org.ednovo.gooru.client.mvp.folders.event.RefreshFolderType;
import org.ednovo.gooru.client.mvp.resource.dnd.ResourceDropController;
import org.ednovo.gooru.client.mvp.search.SearchCBundle;
import org.ednovo.gooru.client.mvp.search.event.RegisterSearchDropEvent;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderZIndexEvent;
import org.ednovo.gooru.client.mvp.search.event.UnregisterSearchDropEvent;
import org.ednovo.gooru.client.mvp.shelf.FolderStyleBundle;
import org.ednovo.gooru.client.mvp.shelf.ShelfCBundle;
import org.ednovo.gooru.client.mvp.shelf.ShelfView;
import org.ednovo.gooru.client.mvp.shelf.collection.folders.events.ChangeShelfPanelActiveStyleEvent;
import org.ednovo.gooru.client.mvp.shelf.collection.folders.events.SetFolderMetaDataEvent;
import org.ednovo.gooru.client.mvp.shelf.collection.folders.events.SetFolderParentNameEvent;
import org.ednovo.gooru.client.mvp.shelf.collection.folders.uc.FolderPopupUc;
import org.ednovo.gooru.client.mvp.shelf.event.RefreshType;
import org.ednovo.gooru.client.uc.AlertContentUc;
import org.ednovo.gooru.client.uc.HTMLEventPanel;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.content.CollectionDo;
import org.ednovo.gooru.shared.model.content.CollectionItemDo;
import org.ednovo.gooru.shared.model.content.ResourceFormatDo;
import org.ednovo.gooru.shared.model.content.ThumbnailDo;
import org.ednovo.gooru.shared.model.folder.FolderDo;
import org.ednovo.gooru.shared.model.folder.FolderItemDo;
import org.ednovo.gooru.shared.model.folder.FolderListDo;
import org.ednovo.gooru.shared.util.StringUtil;
import org.ednovo.gooru.shared.util.UAgentInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Navigator;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

/**
r * @author Search Team
 * 
 */
public class ShelfListView extends BaseViewWithHandlers<ShelfListUiHandlers> implements IsShelfListView,ClickHandler {

	private static ShelfTabViewUiBinder uiBinder = GWT.create(ShelfTabViewUiBinder.class);
	
	private static MessageProperties i18n = GWT.create(MessageProperties.class);

	interface ShelfTabViewUiBinder extends UiBinder<Widget, ShelfListView> {
	}

	@UiField ShelfAddCollection addCollectionItem;

	@UiField ShelfAddCollection newCollectionShelf;
	
	@UiField HTMLPanel myShelfVerPanelHolder, organizeButtonPanel;
	
	private Tree myShelfVerPanel = new Tree(new TreeMenuImages()){
		 @Override
		 public void onBrowserEvent(Event event) {
			    int eventType = DOM.eventGetType(event);
			    if(!(eventType==Event.ONKEYUP||eventType==Event.ONKEYPRESS||eventType==Event.ONKEYDOWN)) {
			    	super.onBrowserEvent(event);
			    }
		 }
		 public void onLoad(){
			 super.onLoad();
			 adjustTreeItemElementsStyle(myShelfVerPanel);
		 }
	};
	
	@UiField
	static
	FocusPanel shelfFocPanel;
	
	@UiField(provided = true)
	ShelfListCBundle res;

	@UiField
	SearchCBundle res1;

	@UiField
	SimplePanel dragImageSimPanel;

	@UiField
	FlowPanel folderCollectionPanel;

	@UiField
	static ScrollPanel collectionListScrollpanel;

	@UiField
	HTMLPanel folderListPanelEvent,organizelbl;

	static Integer pageNumber = 1;
	
	private boolean flag=true;
	
	private boolean isDragged;
	
	private boolean isLeftFolderClicked=false;

	private ResourceDropController tabDropController;

	private boolean fireConsumeShelfCollectionEvent;

	private ShelfCollection selectedShelfCollection = null;

	private FolderResource selectedFolder = null;

	private ThirdLevelFolderResource thirdLevelFolder = null;

	private static final List<FolderDo> SHELF_COLLECTIONS = new ArrayList<FolderDo>();

	private static final String NO_COLLECTION_MESSAGE = i18n.GL0995();
	
	private static final String LOADING_COLLECTION_MESSAGE = i18n.GL0996();
	
	boolean isFromAddResourcePresenter=false;

	@Inject
	ShelfView shelfView;
	
	public Map<Integer,Integer> totWidgets = new HashMap<Integer,Integer>();

	@UiField
	Label noCollectionMsgLbl,foldersText;
	
	@UiField 
	Button backArrowButton,createBtn;
	
	@UiField HTMLEventPanel organizeRootPnl;
	
	int collectionItemDoSize;

	private static String openedCollectionId = "";
	private final String NEW_RESOURCE_DRAG_MSG =i18n.GL0997();
	private final String NEW_COLLECTION_DRAG_MSG =i18n.GL0998();
	private final String CONGRATS_NO_IMG = i18n.GL0988();
	private final String MSG_NO_IMG =i18n.GL0987();
	private final String INFO_MSG =i18n.GL0986();
	
	int widgetPosition = 0;
	
	@UiField FolderStyleBundle folderStyle;
	
	private TreeItem treeChildSelectedItem = new TreeItem();
	
	private TreeItem previousTreeChildSelectedItem = new TreeItem();
	
	@UiField Label folderLabel, collectionLabel;
	
	private PlaceRequest searchRequest = null;
	
    private static final String PRE_SEARCH_LINK = i18n.GL1487();
	
	private static final String PRE_CLASSPAGE_LINK=i18n.GL1486();
	
	private static final String O1_LEVEL = "o1";
	
	private static final String O2_LEVEL = "o2";
	
	private static final String O3_LEVEL = "o3";
	
	private static final String ID = "id";

	private static final String FOLDER = "folder";
	
	private static final String SCOLLECTION = "scollection";
	
	private Integer childPageNumber = 1;
	/**
	 * Class constructor
	 */
	public ShelfListView() {
		res = ShelfListCBundle.INSTANCE;
		res1=SearchCBundle.INSTANCE;
		res.css().ensureInjected();
		res1.css().ensureInjected();
		
		ShelfCBundle.INSTANCE.css().ensureInjected();
		setWidget(uiBinder.createAndBindUi(this));
		newCollectionShelf.setLabel(i18n.GL0993());
		newCollectionShelf.getElement().setAttribute("alt", i18n.GL0993());
		newCollectionShelf.getElement().setAttribute("title",i18n.GL0993());
		foldersText.setText(i18n.GL0994());
		foldersText.getElement().setId("lblFoldersText");
		foldersText.getElement().setAttribute("alt", i18n.GL0994());
		foldersText.getElement().setAttribute("title", i18n.GL0994());
		
		noCollectionMsgLbl.getElement().setId("lblNoCollectionMsgLbl");
		noCollectionMsgLbl.setText(LOADING_COLLECTION_MESSAGE);
		noCollectionMsgLbl.getElement().setAttribute("alt", LOADING_COLLECTION_MESSAGE);
		noCollectionMsgLbl.getElement().setAttribute("title", LOADING_COLLECTION_MESSAGE);
		noCollectionMsgLbl.setVisible(true);
		newCollectionShelf.getElement().setId("lblNewCollection");
		backArrowButton.setText(i18n.GL1500());
		backArrowButton.getElement().setId("btnBackArrowButton");
		backArrowButton.getElement().setAttribute("alt", i18n.GL1500());
		backArrowButton.getElement().setAttribute("title", i18n.GL1500());

		backArrowButton.setVisible(false);
		backArrowButton.addClickHandler(this);
		createBtn.setText(i18n.GL1335());
		createBtn.getElement().setId("btnCreateBtn");
		createBtn.getElement().setAttribute("alt", i18n.GL1335());
		createBtn.getElement().setAttribute("title",i18n.GL1335());
		folderLabel.setText(i18n.GL1501());
		folderLabel.getElement().setId("lblFolderLabel");
		folderLabel.getElement().setAttribute("alt", i18n.GL1501());
		folderLabel.getElement().setAttribute("title", i18n.GL1501());
		collectionLabel.setText(i18n.GL0645());
		collectionLabel.getElement().setId("lblCollectionLabel");
		collectionLabel.getElement().setAttribute("alt", i18n.GL0645());
		collectionLabel.getElement().setAttribute("title", i18n.GL0645());
		organizelbl.getElement().setInnerText(i18n.GL0180());
		organizelbl.getElement().setId("pnlOrganizelbl");
		organizelbl.getElement().setAttribute("alt", i18n.GL0180());
		organizelbl.getElement().setAttribute("title", i18n.GL0180());
		shelfFocPanel.getElement().setId("focuspnlShelfFocPanel");
		collectionListScrollpanel.getElement().setId("sbCollectionListScrollpanel");
		folderCollectionPanel.getElement().setId("fpnlFolderCollectionPanel");
		addCollectionItem.getElement().setId("shelfListAddCollectionItem");
		organizeButtonPanel.getElement().setId("pnlOrganizeButtonPanel");
		organizeRootPnl.getElement().setId("epnlOrganizeRootPnl");
		folderListPanelEvent.getElement().setId("pnlFolderListPanelEvent");
		myShelfVerPanelHolder.getElement().setId("pnlMyShelfVerPanelHolder");
		dragImageSimPanel.getElement().setId("spnlDragImageSimPanel");
		tabDropController = new ResourceDropController(this);
		setShelfPosition(true);
		
		newCollectionShelf.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (AppClientFactory.getLoggedInUser().getUserUid().equals(AppClientFactory.GOORU_ANONYMOUS)) {
					AppClientFactory.fireEvent(new InvokeLoginEvent());
				} else {
					if (checkFolderCollectionSize()) {
						AlertContentUc alertContentUc = new AlertContentUc(i18n.GL0061(),i18n.GL0999());
					} else {
						Window.enableScrolling(false);
						AppClientFactory.fireEvent(new SetHeaderZIndexEvent(99, false));
						MixpanelUtil.Click_Add_NewCollection();
						getUiHandlers().initCreateCollection();
					}
				}
			}
		});
		
		//Click event handling on the Shelf Panel FolderItem
		myShelfVerPanel.addSelectionHandler(new SelectionHandler<TreeItem>() {
			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				treeChildSelectedItem = event.getSelectedItem();
				((ShelfCollection) treeChildSelectedItem.getWidget()).openFolderItem();
				setFolderActiveStatus();
			}
		});
		
		myShelfVerPanelHolder.add(myShelfVerPanel);
		
/*		  Boolean isIpad = !!Navigator.getUserAgent().matches("(.*)iPad(.*)");
		  Boolean isAndriod = !!Navigator.getUserAgent().matches("(.*)Android(.*)");
		  Boolean isWinDskp = !!Navigator.getUserAgent().matches("(.*)NT(.*)");
*/		  
		  UAgentInfo detector = new UAgentInfo(Navigator.getUserAgent());
/*		  
		  if(isIpad && !StringUtil.IPAD_MESSAGE_Close_Click)
		  {
			  shelfFocPanel.getElement().setAttribute("style", "position:relative;");
		  }
		  else if(isAndriod && !StringUtil.IPAD_MESSAGE_Close_Click)
		  {
			  shelfFocPanel.getElement().setAttribute("style", "position:relative;");
		  }
		  else
		  {
*/			  shelfFocPanel.getElement().setAttribute("style", "position:fixed;");
//		  }
		
		folderLabel.addClickHandler(new CreateNewFolder());
		collectionLabel.addClickHandler(new CreateNewCollection());
		
		if (AppClientFactory.getLoggedInUser().getConfirmStatus() == 1){
			organizeButtonPanel.getElement().getStyle().clearMarginTop();
		}else{
			organizeButtonPanel.getElement().getStyle().setMarginTop(70, Unit.PX);
		}
		
		
	}
	
	public void setShelfPosition(boolean toRemove) {
		if (AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.DISCOVER)) {
			if (toRemove) {
				addCollectionItem.removeFromParent();
			}
			shelfFocPanel.setStyleName(res.css().shelfPanelForWorkspace());
		} else if (AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.SHELF)) {
			shelfFocPanel.setStyleName(res.css().shelfPanelForShelf());
		} else {
			shelfFocPanel.setStyleName(res1.css().rightPanel());
			shelfFocPanel.addStyleName("hidden-xs hidden-sm");
		}
	}

	@Override
	public void reset() {
//		super.reset();
		fireConsumeShelfCollectionEvent = false;
		selectedShelfCollection = null;
		selectedFolder = null;
		thirdLevelFolder = null;

		SHELF_COLLECTIONS.clear();
		myShelfVerPanel.clear();
		resetDragImage();
	}
	
	public void setBackToSearch() {
		boolean visible = false;
		try{
			searchRequest = AppClientFactory.getPlaceManager().getPreviousRequest();
			if (searchRequest != null) {
				String query = searchRequest.getParameter("query", null);
				String classpageId=searchRequest.getParameter("classpageid", null);
				visible = searchRequest != null && query != null;
				boolean 	isVisible = searchRequest != null && classpageId != null;
				if (visible) {
					if ((query.length()) >= 30) {
						query = query.substring(0, 30) + "...";
						backArrowButton.setHTML(PRE_SEARCH_LINK);
					} else {
						backArrowButton.setHTML(PRE_SEARCH_LINK);
					}
					backArrowButton.getElement().setAttribute("alt", PRE_SEARCH_LINK);
					backArrowButton.getElement().setAttribute("title", PRE_SEARCH_LINK);
				}
				
				backArrowButton.setVisible(visible);

				if(isVisible){
					backArrowButton.setHTML(PRE_CLASSPAGE_LINK);
					backArrowButton.getElement().setAttribute("alt", PRE_CLASSPAGE_LINK);
					backArrowButton.getElement().setAttribute("title", PRE_CLASSPAGE_LINK);
					backArrowButton.setVisible(isVisible);
				}
			}
		}
		catch(Exception e){
		}

	}
	
	@Override
	public void onClick(ClickEvent event) {
		Object source = event.getSource();
		if (source.equals(backArrowButton)) {
			if(backArrowButton.getText().equalsIgnoreCase("Back To ClassPage")){
				AppClientFactory.getPlaceManager().revealPlace(true, searchRequest);
			} else {
				AppClientFactory.getPlaceManager().revealPlace(true, searchRequest);
			}
		}
	}

	public void enableNoCollectionMessage(boolean isNoCollection) {
		if (isNoCollection == true) {
			noCollectionMsgLbl.getElement().getStyle().setDisplay(Display.NONE);
			noCollectionMsgLbl.getElement().getStyle().setMarginTop(20, Unit.PX);
			noCollectionMsgLbl.getElement().getStyle().setMarginLeft(20, Unit.PX);
			noCollectionMsgLbl.getElement().getStyle().setFontStyle(FontStyle.ITALIC);
			noCollectionMsgLbl.getElement().getStyle().setWidth(163, Unit.PX);
			noCollectionMsgLbl.getElement().getStyle().setColor("#999999");
			noCollectionMsgLbl.getElement().getStyle().setDisplay(Display.BLOCK);
		} else {
			noCollectionMsgLbl.getElement().getStyle().setDisplay(Display.NONE);
		}
	}

	@Override
	public void resetDragImage() {
		
		if (AppClientFactory.getCurrentPlaceToken().equals(	PlaceTokens.RESOURCE_SEARCH)) {
			enableNoCollectionMessage(false);
			totWidgets = setUserShelfMsg();
			setDiplayShelfMsg(totWidgets);
		}
		if (myShelfVerPanel.getItemCount() > 0) {
			displayFoldersPanel(false);
			enableNoCollectionMessage(false);
			if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.SHELF)){
				dragImageSimPanel.clear();
			}
			else if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.COLLECTION_SEARCH)){
				dragImageSimPanel.clear();
			}
			
		} else {
			if (AppClientFactory.getCurrentPlaceToken().equals(	PlaceTokens.COLLECTION_SEARCH)) {
				displayFoldersPanel(false);
				dragImageSimPanel.setWidget(new NoCollectionInShelfListView());
			} else if (AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.RESOURCE_SEARCH)) {
				displayFoldersPanel(false);
			} else {
				enableNoCollectionMessage(true);
				displayFoldersPanel(false);
				dragImageSimPanel.clear();
			}
		}
	}

	@Override
	public void setUserShelfData(List<FolderDo> collections,boolean clearShelfPanel) {
		String o1 = AppClientFactory.getPlaceManager().getRequestParameter(O1_LEVEL);
		String id = AppClientFactory.getPlaceManager().getRequestParameter(ID);
		
		String gooruOid = o1!=null?o1:id;
		
		SHELF_COLLECTIONS.clear();
		if(collections != null) {
			SHELF_COLLECTIONS.addAll(collections);
			collectionItemDoSize = SHELF_COLLECTIONS.size();
		}
		if (clearShelfPanel) {
			pageNumber = 1;
			myShelfVerPanel.clear();
		}
		
		int collectionCount = 0;
		if(collections != null) {
			for (FolderDo collection : collections) {
				ShelfCollection shelfCollection = new ShelfCollection(collection, 1);
				shelfCollection.setWidgetPositions(1, collectionCount, null);
				TreeItem treeItem = new TreeItem(shelfCollection);
				myShelfVerPanel.addItem(treeItem);
				//When page is refreshed, the folderItem previously selected will be highlighted.
				if(gooruOid!=null&&gooruOid.equalsIgnoreCase(collection.getGooruOid())) {
					checkShelfRefreshStatus(treeItem, gooruOid);
					AppClientFactory.fireEvent(new SetFolderParentNameEvent(collection.getTitle()));
					AppClientFactory.fireEvent(new SetFolderMetaDataEvent(StringUtil.getFolderMetaData(collection)));
					shelfCollection.setFolderOpenedStatus(true);
				}
				collectionCount++;
			}
		}
		if(collectionItemDoSize==0){
			noCollectionMsgLbl.setText(NO_COLLECTION_MESSAGE);
			noCollectionMsgLbl.getElement().setAttribute("alt", NO_COLLECTION_MESSAGE);
			noCollectionMsgLbl.getElement().setAttribute("title", NO_COLLECTION_MESSAGE);
		}
		resetDragImage();
		fireConsumeShelfCollectionEvent = true;
		getUiHandlers().requestShelfCollections();
	}

	/**
	 * @function checkShelfRefreshStatus 
	 * @created_date : 11-Feb-2014
	 * @description
	 * @parm(s) : @param treeItem
	 * @return : void
	 * @throws : <Mentioned if any exceptions>
	*/
	
	private void checkShelfRefreshStatus(TreeItem treeItem, String parentId) {
		treeChildSelectedItem = treeItem;
		ShelfCollection shelfCollection = (ShelfCollection) treeChildSelectedItem.getWidget();
		shelfCollection.setActiveStyle(true);
		String id = AppClientFactory.getPlaceManager().getRequestParameter(ID);
		id = id!=null?id:"";
		if(!parentId.equalsIgnoreCase(id)) {
			getUiHandlers().getChildFolderItems(parentId,false);
		}
		ShelfCollection previousShelfCollection = (ShelfCollection) previousTreeChildSelectedItem.getWidget();
		if(previousShelfCollection!=null) {
			previousShelfCollection.setActiveStyle(false);
		}
		previousTreeChildSelectedItem = treeChildSelectedItem;
	}
	
	@Override
	public Widget getDropTarget() {
		return asWidget();
	}

	@Override
	public void onDragOver(Draggable draggable) {
		if (draggable.getType().equals(DRAG_TYPE.COLLECTION)) {
//			draggable.getDraggableMirageUc().onDroppable(true);
			
		}else if(draggable.getType().equals(DRAG_TYPE.RESOURCE)){
			
		}
	}

	@Override
	public void onDragOut(Draggable draggable) {
		if (draggable.getType().equals(DRAG_TYPE.COLLECTION)) {
			draggable.getDraggableMirageUc().onDroppable(false);
		}
	}

	@Override
	public void onDrop(Draggable draggable) {
		if (draggable.getType().equals(DRAG_TYPE.COLLECTION)) {
//			new CustomAnimation(draggable).run(50);
//			AppClientFactory.fireEvent(new CopyCollectionEvent(draggable.getDragId()));
			
		}
	}

	@Override
	public void registerDropController() {
		if (AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.COLLECTION_SEARCH) 
				|| AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.RESOURCE_SEARCH)) {
			AppClientFactory.fireEvent(new RegisterSearchDropEvent(getDropController(),RegisterSearchDropEvent.DROP_AREA.SHELF));
		}
	}

	@Override
	public void unregisterDropController() {
		if (AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.COLLECTION_SEARCH)
				|| AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.RESOURCE_SEARCH)) {
			AppClientFactory.fireEvent(new UnregisterSearchDropEvent(getDropController(),RegisterSearchDropEvent.DROP_AREA.SHELF));
		}
	}

	@Override
	public void onLoad() {
//		super.onLoad();
		setShelfPosition(false);
		registerDropController();
	}

	@Override
	public void onUnload() {
//		super.onUnload();
		unregisterDropController();
	}

	public ResourceDropController getDropController() {
		return tabDropController;
	}

	/**
	 * @param dropController
	 *            instance of {@link ResourceDropController}
	 */
	public void setDropController(ResourceDropController dropController) {
		this.tabDropController = dropController;
	}

	@Override
	public void registerDropControllers() {
		registerDropController();
		addCollectionItem.registerDropController();
		Iterator<Widget> widgets = myShelfVerPanel.iterator();
		while (widgets.hasNext()) {
			Widget widget = widgets.next();
			if (widget instanceof DropBox) {
				((DropBox) widget).registerDropController();
			}
		}
	}

	@Override
	public void reregisterDropController() {
		unregisterDropController();
		registerDropController();
	}

	@Override
	public void disableDraggableEvent(String folderLevel,
			String folderObjectType) {
		if (folderLevel.equalsIgnoreCase("1")) {
		} else if (folderLevel.equalsIgnoreCase("2")) {
		}
	}

	@Override
	public void refreshCollectionInShelfList(CollectionDo collectionDo, RefreshType refreshType) {
		if (collectionDo != null) {
			FolderDo folderDo = getFolderDo(collectionDo);
			if (refreshType.equals(RefreshType.INSERT)|| refreshType.equals(RefreshType.INSERT_AND_VIEW)) {
				final ShelfCollection shelfCollection = new ShelfCollection(folderDo,1); 
				SHELF_COLLECTIONS.add(folderDo);
				myShelfVerPanel.insertItem(0, new TreeItem(shelfCollection.asWidget()));
				if(refreshType.equals(RefreshType.INSERT)){
					
					/** Changed to new API call for fetching resources in a order **/
		    		AppClientFactory.getInjector().getfolderService().getCollectionResources(folderDo.getGooruOid(),null, null, new SimpleAsyncCallback<FolderListDo>(){
						@Override
						public void onSuccess(FolderListDo result) {
							shelfCollection.setAllResources(result.getSearchResult()); 
							shelfCollection.setOpen();
							shelfCollection.glowTitle();
							shelfCollection.setCollectionOpenedStatus(true);
							
						}
		    		});
				}
				shelfCollection.setOpen();
				shelfCollection.glowTitle();
				totWidgets = setUserShelfMsg();
				setDiplayShelfMsg(totWidgets);				
				if (refreshType.equals(RefreshType.INSERT_AND_VIEW)) {
					if(AppClientFactory.getPlaceManager().getRequestParameter("id")==null){
						AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.SHELF);
					}else{
						AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.SHELF,new String[] { ID, folderDo.getGooruOid() });
						changeShelfPanelActiveStyle(folderDo.getGooruOid());
					}
				}
				setNewCollectionPanelCss(false, NEW_COLLECTION_DRAG_MSG);
				resetDragImage();
				
			} else if (refreshType.equals(RefreshType.UPDATE)) {
				ShelfCollection shelfCollection = getShelfCollection(folderDo.getGooruOid());
				shelfCollection.updateData(folderDo);
			} else if (refreshType.equals(RefreshType.DELETE)) {
				
				ShelfCollection shelfCollection = getShelfCollection(folderDo.getGooruOid());
				String id = AppClientFactory.getPlaceManager().getRequestParameter(ID);
				
				if(id!=null) {
					int index = widgetPosition;
					selectedShelfCollection = null;
					if (myShelfVerPanel.getItemCount() > 1) {
						if (index == 0) {
							selectedShelfCollection = (ShelfCollection) myShelfVerPanel.getItem(1).getWidget();
						} else {
							selectedShelfCollection = (ShelfCollection) myShelfVerPanel.getItem(index - 1).getWidget();
						}
						selectedShelfCollection.setOpen();
						AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.SHELF, new String[] {ID, selectedShelfCollection.getCollectionDo().getGooruOid()});
						AppClientFactory.fireEvent(new ChangeShelfPanelActiveStyleEvent());
					} else {
						AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.SHELF);
						AppClientFactory.fireEvent(new ChangeShelfPanelActiveStyleEvent());
					}
				}
				
				shelfCollection.removeFromParent();
				if(myShelfVerPanel.getItemCount()==0){
					noCollectionMsgLbl.setText(NO_COLLECTION_MESSAGE);
					noCollectionMsgLbl.getElement().setAttribute("alt", NO_COLLECTION_MESSAGE);
					noCollectionMsgLbl.getElement().setAttribute("title", NO_COLLECTION_MESSAGE);
				}else{
					noCollectionMsgLbl.setText(LOADING_COLLECTION_MESSAGE);
					noCollectionMsgLbl.getElement().setAttribute("alt", LOADING_COLLECTION_MESSAGE);
					noCollectionMsgLbl.getElement().setAttribute("title", LOADING_COLLECTION_MESSAGE);
				}
				// resetDragImage();

			} else if (refreshType.equals(RefreshType.OPEN)) {
				selectedShelfCollection = getShelfCollection(folderDo.getGooruOid());
				if (selectedShelfCollection != null) {
					selectedShelfCollection.setOpen();
				}
			}
		} else if (myShelfVerPanel.getItemCount() > 0) {
			selectedShelfCollection = (ShelfCollection) myShelfVerPanel.getItem(0).getWidget();
			AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.SHELF,new String[] {ID,selectedShelfCollection.getCollectionDo().getGooruOid() });
		} else {
			AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.SHELF);
		}
		getUiHandlers().requestShelfCollections();
		shelfView.setFocusCollectionTitle();
	}

	@Override
	public void refreshCollectionItemInShelfList(CollectionItemDo collectionItemDo, RefreshType refreshType) {
		FolderDo folderDo = getFolderItemDo(collectionItemDo);
		
		if (refreshType.equals(RefreshType.INSERT)) {
			ShelfCollection shelfCollection = getShelfCollection(collectionItemDo.getCollection().getGooruOid());
			shelfCollection.addCollectionItem(folderDo, true); 
		} else if (refreshType.equals(RefreshType.UPDATE)) {
			ShelfCollection shelfCollection = getShelfCollection(collectionItemDo.getCollection().getGooruOid());
			shelfCollection.updateCollectionItem(folderDo,collectionItemDo.getItemSequence());
		} else if (refreshType.equals(RefreshType.DELETE)) {
			ShelfCollection shelfCollection = getShelfCollection(collectionItemDo.getCollection().getGooruOid());
			shelfCollection.removeCollectionItem(folderDo);
		}
		getUiHandlers().requestShelfCollections();
	}

	@Override
	public List<FolderDo> getShelfCollections() {
		return SHELF_COLLECTIONS;
	}

	@Override
	public boolean isFireConsumeShelfCollectionEvent() {
		return fireConsumeShelfCollectionEvent;
	}

	public void setFireConsumeShelfCollectionEvent(
			boolean fireConsumeShelfCollectionEvent) {
		this.fireConsumeShelfCollectionEvent = fireConsumeShelfCollectionEvent;
	}

	@Override
	public void openShelfCollection(String collectionId) {
		selectedShelfCollection = getShelfCollection(collectionId);
		selectedShelfCollection.setOpen();
	}

	private ShelfCollection getShelfCollection(String collectionId) {
		int count = 0;
		Iterator<Widget> widgets = myShelfVerPanel.iterator();
		while (widgets.hasNext()) {
			Widget widget = widgets.next();
			if (widget instanceof ShelfCollection && ((ShelfCollection) widget).getCollectionDo().getGooruOid().equals(collectionId)) {
				widgetPosition = count;
				return (ShelfCollection) widget;
			}
			count++;
		}

		return null;
	}

	private ShelfCollection getOpenShelfCollection() {
		Iterator<Widget> widgets = myShelfVerPanel.iterator();
		while (widgets.hasNext()) {
			Widget widget = widgets.next();
			if (widget instanceof ShelfCollection&& ((ShelfCollection) widget).disPanel.isOpen()) {
				return (ShelfCollection) widget;
			}
	}
		return null;
	}

/**
 * To get more collection item after scroll down,if collection is more than 20.
 * @param event instance of ScrollEvent
 */
	@UiHandler("collectionListScrollpanel")
	public void dragImageSimPanelscroll(ScrollEvent event) {

		if (collectionListScrollpanel.getVerticalScrollPosition() == collectionListScrollpanel.getMaximumVerticalScrollPosition() && collectionItemDoSize >= 20) {
			pageNumber = pageNumber + 1;
			getUiHandlers().getSelfCollectionListItems(20, pageNumber, false);
		}
	}

	/**
	 * This method is used to get pageNumber
	 * 
	 * @return pageNumber
	 */
	public static int getpageNumber() {
		return pageNumber;

	}

	/**
	 * This method is used to set page number
	 * 
	 * @param pageNumber
	 */
	public static void setPageNumber(Integer pageNumber) {
		ShelfListView.pageNumber = pageNumber;
	}

	@Override
	public void disableFolderCollectionPanel() {
		folderCollectionPanel.getElement().getStyle().setDisplay(Display.NONE);
	}

	@Override
	public void enableFolderCollectionPanel() {
		folderCollectionPanel.getElement().getStyle().setDisplay(Display.BLOCK);
	}

	/**
	 * This method checks for the limit of each folder/subfolder
	 * 
	 * @return boolean
	 */
	public boolean checkFolderCollectionSize() {
		if (myShelfVerPanel.getItemCount() > 50) {
			return true;
		}
		return false;
	}

	/**
 * 
 */
	@Override
	public void collectionListScrollpanel(boolean isSetClear) {

/*		if (isSetClear) {
			collectionListScrollpanel.getElement().getStyle().clearHeight();
		} else {*/
			collectionListScrollpanel.getElement().getStyle().setHeight(Window.getClientHeight(), Unit.PX);
/*		}
*/
	}

	@Override
	public void displayFoldersPanel(boolean isLoggedIn) {
		if (isLoggedIn == true) {
			folderListPanelEvent.getElement().getStyle().setDisplay(Display.BLOCK);
		} else {
			folderListPanelEvent.getElement().getStyle().setDisplay(Display.NONE);
		}
	}
	
	@Override
	public void setNewCollectionPanel() {
		if(treeChildSelectedItem!=null){
			ShelfCollection treeItemShelfCollection = (ShelfCollection) treeChildSelectedItem.getWidget();
			if (AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.RESOURCE_SEARCH)) {
				setNewCollectionPanelCss(false, NEW_RESOURCE_DRAG_MSG);
				enableDisableOrganizePnl(false);
				organizeButtonPanel.setVisible(false);
				collectionListScrollpanel.getElement().getStyle().setMarginRight(10, Unit.PX);
				collectionListScrollpanel.getElement().getStyle().setWidth(284, Unit.PX);
				if(treeItemShelfCollection!=null) {
					treeItemShelfCollection.setActiveStyle(false);
				}
			} else if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.COLLECTION_SEARCH)) {
				setNewCollectionPanelCss(false, NEW_COLLECTION_DRAG_MSG);
				enableDisableOrganizePnl(false);
				organizeButtonPanel.setVisible(false);
				collectionListScrollpanel.getElement().getStyle().setMarginRight(10, Unit.PX);
				collectionListScrollpanel.getElement().getStyle().setWidth(284, Unit.PX);
				if(treeItemShelfCollection!=null) {
					treeItemShelfCollection.setActiveStyle(false);
				}
			} else if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.SHELF)) {
				setNewCollectionPanelCss(true, "");
				enableDisableOrganizePnl(true);
				organizeButtonPanel.setVisible(true);
				collectionListScrollpanel.getElement().getStyle().setMarginRight(0, Unit.PX);
				collectionListScrollpanel.getElement().getStyle().setWidth(295, Unit.PX);
				if(treeItemShelfCollection!=null) {
					if(organizeRootPnl.getStyleName().contains(folderStyle.active())) {
						AppClientFactory.fireEvent(new SetFolderParentNameEvent(i18n.GL0180()));
						treeItemShelfCollection.setActiveStyle(false);
					} else {
						treeItemShelfCollection.setActiveStyle(true);
					}
				}
			}
		}
		try {
			ShelfCollection shelfCollection = getOpenShelfCollection();
			if(shelfCollection!=null) {
				shelfCollection.setOpenStatus(true);
			}
		} catch(Exception e) {
			
		}
	}
	
	private void setNewCollectionPanelCss(boolean isNewCollection, String msg) {
		if(isNewCollection){
			newCollectionShelf.getElement().getStyle().setDisplay(Display.NONE);
			addCollectionItem.getElement().getStyle().setDisplay(Display.NONE);
		} else {
			addCollectionItem.setLabel(msg);
			addCollectionItem.getElement().setAttribute("alt", msg);
			addCollectionItem.getElement().setAttribute("title", msg);
			addCollectionItem.removeStyleName(ShelfListCBundle.INSTANCE.css().shelfNewCollection());
			addCollectionItem.addStyleName(folderStyle.dropbox());
			addCollectionItem.getLabel().setStyleName(ShelfListCBundle.INSTANCE.css().resourceCollectionPanelText());
			addCollectionItem.getElement().getStyle().setDisplay(Display.BLOCK);
			newCollectionShelf.getElement().getStyle().setDisplay(Display.NONE);
		}
	}

	@Override
	public void setOpenCollectionId(String collectionId) {
		this.openedCollectionId = collectionId;
	}

	@Override
	public void setUserShelfMsg(String userMsg) {
		if(myShelfVerPanel.getItemCount()<=10){
			dragImageSimPanel.clear();
			dragImageSimPanel.setWidget(new NoResourceInShelfListView(userMsg));
			if(myShelfVerPanel.getItemCount()>5){
				dragImageSimPanel.clear();
				dragImageSimPanel.setWidget(new NoResourceInShelfListView(CONGRATS_NO_IMG));
			}
		}
	}
	
	public void setDiplayShelfMsg(Map<Integer, Integer> totWidgets){ 
		if(myShelfVerPanel.getItemCount()<=5){
			for(Map.Entry<Integer,Integer> obj:totWidgets.entrySet()){
				if(obj.getValue()<2 ){
					dragImageSimPanel.clear();
					dragImageSimPanel.setWidget(new NoResourceInShelfListView(INFO_MSG));
				}
			}
		}
		else if(myShelfVerPanel.getItemCount()>5 && myShelfVerPanel.getItemCount()<=10){
			dragImageSimPanel.clear();
			dragImageSimPanel.setWidget(new NoResourceInShelfListView(MSG_NO_IMG));
		}
		else if(myShelfVerPanel.getItemCount()>10){
			dragImageSimPanel.clear();
		}
		
	}
	
	private Map<Integer, Integer> setUserShelfMsg() { 
		Map<Integer,Integer> totWidgetsCount = new HashMap<Integer,Integer>();
		if(myShelfVerPanel.getItemCount()==0){
			dragImageSimPanel.setWidget(new NoResourceInShelfListView(""));
		}
		Iterator<Widget> widgets = myShelfVerPanel.iterator();
		int counter=0;
		while (widgets.hasNext()) {
			Widget widget = widgets.next();
			if (widget instanceof ShelfCollection) {
				int childWidget = ((ShelfCollection) widget).getContentVerPanel().getWidgetCount();
				totWidgetsCount.put(counter,childWidget);
			}
			counter++;
	}
		return totWidgetsCount;
	}

	public FolderDo getFolderDo(CollectionDo collectionDo) {
		FolderDo folderDo = new FolderDo();
		folderDo.setGooruOid(collectionDo.getGooruOid());
		folderDo.setTitle(collectionDo.getTitle());
		folderDo.setType(collectionDo.getCollectionType());
		folderDo.setSharing(collectionDo.getSharing());
		ThumbnailDo thumbnailDo = new ThumbnailDo();
		thumbnailDo.setUrl(collectionDo.getThumbnailUrl());
		folderDo.setThumbnails(thumbnailDo);
		List<FolderItemDo> folderItems = new ArrayList<FolderItemDo>();
		if(collectionDo.getCollectionItems()!=null) {
			for(int i=0;i<collectionDo.getCollectionItems().size();i++) {
				CollectionItemDo collectionItemDo = collectionDo.getCollectionItems().get(i);
				FolderItemDo folderItemDo = new FolderItemDo();
				folderItemDo.setGooruOid(collectionItemDo.getGooruOid());
				folderItemDo.setTitle(collectionItemDo.getResourceTitle());
				folderItemDo.setType(collectionItemDo.getItemType());
				ResourceFormatDo resourceFormatDo = new ResourceFormatDo();
				resourceFormatDo.setValue(collectionItemDo.getResource().getResourceFormat().getValue());
				folderItems.add(folderItemDo);
			}
			folderDo.setCollectionItems(folderItems);
		}
		return folderDo;
	}
	
	public FolderDo getFolderItemDo(CollectionItemDo collectionItemDo) {
		FolderDo folderDo = new FolderDo();
		folderDo.setGooruOid(collectionItemDo.getGooruOid());
		folderDo.setTitle(collectionItemDo.getResourceTitle());
		folderDo.setType(collectionItemDo.getItemType());
		if(collectionItemDo.getCollection()!=null && collectionItemDo.getCollection().getCollectionItems()!=null){
			folderDo.setItemCount(collectionItemDo.getCollection().getCollectionItems().size());
		}
		ResourceFormatDo resourceFormatDo = new ResourceFormatDo();
		resourceFormatDo.setValue(collectionItemDo.getResource().getResourceFormat().getValue());
		folderDo.setResourceFormat(resourceFormatDo);  
		return folderDo;
	}

	@Override
	public void getChildFolderItems(List<FolderDo> folderListDo) {
		String o2 = null, o3 = null, selectedFolder = null, selectedFolderName = null, id = null;
		FolderDo folderDo = null;
		TreeItem selectedItem = null;
		ShelfCollection selectedWidget = (ShelfCollection) treeChildSelectedItem.getWidget();
		if(folderListDo!=null) {
			int nextLevel = 1;
			if(selectedWidget.getLevel()==1) {
				o2=AppClientFactory.getPlaceManager().getRequestParameter(O2_LEVEL);
				id=AppClientFactory.getPlaceManager().getRequestParameter(ID);
				nextLevel = 2;
			} else if (selectedWidget.getLevel()==2) { 
				o3=AppClientFactory.getPlaceManager().getRequestParameter(O3_LEVEL);
				id=AppClientFactory.getPlaceManager().getRequestParameter(ID);
				nextLevel = 3;
			}else if (selectedWidget.getLevel()==3) {
				id=AppClientFactory.getPlaceManager().getRequestParameter(ID);
				nextLevel = 4;
			}
			for(int i=0;i<folderListDo.size();i++) {
				ShelfCollection shelfCollection = new ShelfCollection(folderListDo.get(i), nextLevel);
				shelfCollection.setWidgetPositions(nextLevel, i, selectedWidget.getUrlParams());
				TreeItem item = new TreeItem(shelfCollection);
				treeChildSelectedItem.addItem(item);
				correctStyle(item);
				if(nextLevel==2&& (o2!=null&&o2.equalsIgnoreCase(folderListDo.get(i).getGooruOid()) || id!=null&&id.equalsIgnoreCase(folderListDo.get(i).getGooruOid()))) {
					if(o2!=null) {
						selectedFolder = o2;
					} else {
						selectedFolder = id;
					}
					selectedItem = item;
					selectedFolderName = folderListDo.get(i).getTitle();
					folderDo = folderListDo.get(i);
				} else if(nextLevel==3&& (o3!=null&&o3.equalsIgnoreCase(folderListDo.get(i).getGooruOid()) || id!=null&&id.equalsIgnoreCase(folderListDo.get(i).getGooruOid()))) {
					if(o3!=null) {
						selectedFolder = o3;
					} else {
						selectedFolder = id;
					}
					selectedItem = item;
					selectedFolderName = folderListDo.get(i).getTitle();
					folderDo = folderListDo.get(i);
				} else if (nextLevel==4&&id!=null&&id.equalsIgnoreCase(folderListDo.get(i).getGooruOid())) {
					selectedFolder = id;
					selectedItem = item;
					selectedFolderName = folderListDo.get(i).getTitle();
					folderDo = folderListDo.get(i);
				} 
			}
		}
		if(treeChildSelectedItem.getState()) {
			if(isDragged){
				isDragged=false;
			}else{
				isDragged=false;
				treeChildSelectedItem.setState(false);
				selectedWidget.setOpenStyle(false, treeChildSelectedItem.getChildCount());
			}
		} else {
			isDragged=false;
			treeChildSelectedItem.setState(true);
			selectedWidget.setOpenStyle(true, treeChildSelectedItem.getChildCount());
		}
		
		if(selectedFolder!=null&&selectedItem!=null) { 
			checkShelfRefreshStatus(selectedItem, selectedFolder);
			ShelfCollection selectedWidget1 = (ShelfCollection) treeChildSelectedItem.getWidget();
			AppClientFactory.fireEvent(new SetFolderParentNameEvent(selectedFolderName));
			AppClientFactory.fireEvent(new SetFolderMetaDataEvent(StringUtil.getFolderMetaData(folderDo)));
			selectedWidget1.setFolderOpenedStatus(true);
		}
		
	}
	
	@Override
	public void getCollectionItems(List<FolderDo> folderDo) {
		ShelfCollection selectedWidget = (ShelfCollection) treeChildSelectedItem.getWidget();
		if(folderDo!=null) {
			selectedWidget.setCollectionItems(folderDo); 
		}
		if(treeChildSelectedItem.getState()) {
			treeChildSelectedItem.setState(false);
			selectedWidget.setOpenStyle(false, treeChildSelectedItem.getChildCount());
		} else {
			treeChildSelectedItem.setState(true);
			selectedWidget.setOpenStyle(true, treeChildSelectedItem.getChildCount());
		}
	}	
	
   	private static void correctStyle(final UIObject uiObject) {
	      if (uiObject instanceof TreeItem) {
	         if (uiObject != null && uiObject.getElement() != null) {
	            Element element = uiObject.getElement();
	            element.getStyle().setPadding(0, Unit.PX);
	            element.getStyle().setMarginLeft(0, Unit.PX);
	         }
	      } else {
	         if (uiObject != null && uiObject.getElement() != null && uiObject.getElement().getParentElement() != null
	               && uiObject.getElement().getParentElement().getParentElement() != null
	               && uiObject.getElement().getParentElement().getParentElement().getStyle() != null) {
	            Element element = uiObject.getElement().getParentElement().getParentElement();
	            element.getStyle().setPadding(0, Unit.PX);
	            element.getStyle().setMarginLeft(0, Unit.PX);
	         }
	      }
   }


	@Override
	public void getAllCollectionItems(String collectionId) {
		
	}


	@Override
	public void insertDraggedCollectionInShelfList(CollectionDo collectionDo,String selectedFolder) {
		onDragOverOpenFolder(selectedFolder,false);
		ShelfCollection selectedWidget = (ShelfCollection) treeChildSelectedItem.getWidget();
		if (collectionDo != null) {
			FolderDo folderDo = getFolderDo(collectionDo);
			int nextLevel = 1;
			if(selectedWidget.getLevel()==1) {
				nextLevel = 2;
			} else if (selectedWidget.getLevel()==2) {
				nextLevel = 3;
			}else if (selectedWidget.getLevel()==3) { 
				nextLevel = 4;
			}
			ShelfCollection shelfCollection = new ShelfCollection(folderDo, nextLevel);
			shelfCollection.setWidgetPositions(nextLevel, 0, selectedWidget.getUrlParams());
			TreeItem item = new TreeItem(shelfCollection);
			treeChildSelectedItem.insertItem(0, item);
			correctStyle(item);
			treeChildSelectedItem.setState(true);
			selectedWidget.setOpenStyle(true, treeChildSelectedItem.getChildCount());
			
		}
	}
	
	
	
	@Override
	public void refreshFolderItemData(FolderDo folderDo, RefreshFolderType refreshFolderType, HashMap<String, String> params) {
		
		
		if(params!=null){
			isFromAddResourcePresenter	=params.containsKey("from");
		}
		
		if(refreshFolderType.equals(RefreshFolderType.INSERT) || refreshFolderType.equals(RefreshFolderType.INSERT_AND_VIEW) ) {
			if(params!=null) {
				if(params.get(O3_LEVEL)!=null) {
					TreeItem level1Item = getFirstLevelTreeWidget(params.get(O1_LEVEL));
					if(level1Item!=null) {
						TreeItem level2Item = getSecondLevelTreeWidget(level1Item, params.get(O2_LEVEL));
						if(level2Item!=null) {
							TreeItem level3Item = getSecondLevelTreeWidget(level2Item, params.get(O3_LEVEL));
							if(level3Item!=null) {
								ShelfCollection selectedThirdWidget = (ShelfCollection) level3Item.getWidget();
								if(selectedThirdWidget.getFolderOpenedStatus()==true) {
									ShelfCollection shelfCollection = new ShelfCollection(folderDo, 4);
									TreeItem treeItem = new TreeItem(shelfCollection);
									shelfCollection.setWidgetPositions(4, 0, selectedThirdWidget.getUrlParams());
									level3Item.insertItem(0, treeItem);
									correctStyle(treeItem);
								}
							}
						}
						/** Currently not required as we cannot create folder in last level i.e in level-3  **/
						/*isDragged=true;	
						onDragOverOpenFolder(params.get(O1_LEVEL),false);
						isDragged=true;
						onDragOverOpenFolder(params.get(O2_LEVEL),false);
						isDragged=true;
						onDragOverOpenFolder(params.get(O3_LEVEL),false);
						setCreatedFolderActiveStatus("",folderDo,params,3);
						AppClientFactory.fireEvent(new SetFolderParentNameEvent(folderDo.getTitle()));
						AppClientFactory.fireEvent(new SetCollectionMovedStyleEvent(folderDo.getGooruOid()));*/  
					}	
				} else if(params.get(O2_LEVEL)!=null) {
					TreeItem level1Item = getFirstLevelTreeWidget(params.get(O1_LEVEL));
					if(level1Item!=null ) {
						TreeItem level2Item = getSecondLevelTreeWidget(level1Item, params.get(O2_LEVEL));
						if(level2Item!=null && !isLeftFolderClicked) {
							ShelfCollection selectedSecondWidget = (ShelfCollection) level2Item.getWidget();
							if(selectedSecondWidget.getFolderOpenedStatus()==true) {
								ShelfCollection shelfCollection = new ShelfCollection(folderDo, 3);
								TreeItem treeItem = new TreeItem(shelfCollection);
								shelfCollection.setWidgetPositions(3, 0, selectedSecondWidget.getUrlParams());
								level2Item.insertItem(0, treeItem);
								correctStyle(treeItem);
							}
						}else{
							isLeftFolderClicked=false;
							if(level2Item!=null) {
								ShelfCollection selectedSecondWidget = (ShelfCollection) level2Item.getWidget();
								if(selectedSecondWidget.getFolderOpenedStatus()==true) {
									ShelfCollection shelfCollection = new ShelfCollection(folderDo, 3);
									TreeItem treeItem = new TreeItem(shelfCollection);
									shelfCollection.setWidgetPositions(3, 0, selectedSecondWidget.getUrlParams());
									level2Item.insertItem(0, treeItem);
									correctStyle(treeItem);
								}
							}
							
							isDragged=true;
							onDragOverOpenFolder(params.get(O1_LEVEL),false);
							isDragged=true;
							onDragOverOpenFolder(params.get(O2_LEVEL),false);
							setCreatedFolderActiveStatus("",folderDo,params,2);
							AppClientFactory.fireEvent(new SetFolderParentNameEvent(folderDo.getTitle()));
							AppClientFactory.fireEvent(new SetFolderMetaDataEvent(StringUtil.getFolderMetaData(folderDo)));
							setFolderStyle(folderDo.getGooruOid());
//							AppClientFactory.fireEvent(new SetCollectionMovedStyleEvent(folderDo.getGooruOid())); 
						}
					}
				} else if(params.get(O1_LEVEL)!=null) {
					TreeItem item = getFirstLevelTreeWidget(params.get(O1_LEVEL));
					if(item!=null && !isLeftFolderClicked) {
						ShelfCollection selectedWidget = (ShelfCollection) item.getWidget();
						if(selectedWidget.getFolderOpenedStatus()==true) {
							ShelfCollection shelfCollection = new ShelfCollection(folderDo, 2);
							TreeItem treeItem = new TreeItem(shelfCollection);
							shelfCollection.setWidgetPositions(2, 0, selectedWidget.getUrlParams());
							item.insertItem(0, treeItem);	
							correctStyle(treeItem);
						}
					}else{
						isLeftFolderClicked=false;
						if(item!=null) {
							ShelfCollection selectedWidget = (ShelfCollection) item.getWidget();
							if(selectedWidget.getFolderOpenedStatus()==true) {
								ShelfCollection shelfCollection = new ShelfCollection(folderDo, 2);
								TreeItem treeItem = new TreeItem(shelfCollection);
								shelfCollection.setWidgetPositions(2, 0, selectedWidget.getUrlParams());
								item.insertItem(0, treeItem);
								correctStyle(treeItem);
							}
						}
						isDragged=true;
						onDragOverOpenFolder(params.get(O1_LEVEL),false);
						setCreatedFolderActiveStatus("",folderDo,params,1); 
						AppClientFactory.fireEvent(new SetFolderParentNameEvent(folderDo.getTitle()));
						AppClientFactory.fireEvent(new SetFolderMetaDataEvent(StringUtil.getFolderMetaData(folderDo)));
						setFolderStyle(folderDo.getGooruOid());
//						AppClientFactory.fireEvent(new SetCollectionMovedStyleEvent(folderDo.getGooruOid()));
					}
					  
				} else {
					isLeftFolderClicked=false;
					ShelfCollection shelfCollection = new ShelfCollection(folderDo, 1);
					TreeItem treeItem = new TreeItem(shelfCollection);
					shelfCollection.setWidgetPositions(1, 0, null);
					myShelfVerPanel.insertItem(0, treeItem);
					correctStyle(treeItem);
					if(refreshFolderType.equals(RefreshFolderType.INSERT)){
						setCreatedFolderActiveStatus("",folderDo,params,0);	
					}
				}
				if (refreshFolderType.equals(RefreshFolderType.INSERT_AND_VIEW)) {
					if(AppClientFactory.getPlaceManager().getRequestParameter("id")==null){
						AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.SHELF);
					}else{
						params.put(ID, folderDo.getGooruOid());
						AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.SHELF,params);
						if(params.get(O1_LEVEL)!=null){
							setCopiedCollectionStyle(params); 
						}else{
							changeShelfPanelActiveStyle(folderDo.getGooruOid());
						}
							
      					
					}
				}
				
			} else {
				ShelfCollection selectedWidget = (ShelfCollection) treeChildSelectedItem.getWidget();
				int nextLevel = 1;
				if(selectedWidget!=null) {
					if(getFolderLevel()==0 || !selectedWidget.getCollectionDo().getType().equals(FOLDER)) {
						
					} else if(selectedWidget.getLevel()==1) {
						nextLevel = 2;
					} else if (selectedWidget.getLevel()==2) { 
						nextLevel = 3;
					}else if (selectedWidget.getLevel()==3) { 
						nextLevel = 4;
					}
				}
				
				ShelfCollection shelfCollection = new ShelfCollection(folderDo, nextLevel);
				TreeItem treeItem = new TreeItem(shelfCollection);
				if(selectedWidget==null || nextLevel==1) {
					shelfCollection.setWidgetPositions(1, 0, null);
					myShelfVerPanel.insertItem(0, treeItem);
				} else {
					shelfCollection.setWidgetPositions(nextLevel, 0, selectedWidget.getUrlParams());
					if(selectedWidget.getCollectionDo().getType().equals("folder")) {
						treeChildSelectedItem.insertItem(0, treeItem);
					} else {
						myShelfVerPanel.insertItem(0, treeItem);
					}
					correctStyle(treeItem);
				}
			}
			resetDragImage();
		} else if(refreshFolderType.equals(RefreshFolderType.DELETE)) {
			ShelfCollection selectedWidget = (ShelfCollection) treeChildSelectedItem.getWidget();
			if(selectedWidget!=null) {
				if(selectedWidget.getCollectionDo().getGooruOid().equalsIgnoreCase(folderDo.getGooruOid())) {
					TreeItem parentSelectedItem = treeChildSelectedItem.getParentItem();
					if(selectedWidget.getLevel()==1) {
						AppClientFactory.fireEvent(new SetFolderParentNameEvent(i18n.GL0180()));
						treeChildSelectedItem.remove();
					} else {
						treeChildSelectedItem.remove();
						treeChildSelectedItem = parentSelectedItem;
						ShelfCollection previousItem = (ShelfCollection) treeChildSelectedItem.getWidget();
						AppClientFactory.fireEvent(new SetFolderParentNameEvent(previousItem.getCollectionDo().getTitle()));
					}
				}
			} else {
				 for(int i = 0; i < myShelfVerPanel.getItemCount(); i++) {
					 TreeItem item = myShelfVerPanel.getItem(i);
					 ShelfCollection deletedItem = (ShelfCollection) item.getWidget();
					 if(folderDo.getGooruOid().equalsIgnoreCase(deletedItem.getCollectionDo().getGooruOid())) {
						 item.remove();
						 AppClientFactory.fireEvent(new SetFolderParentNameEvent(i18n.GL0180()));
					 }
				 }
			}
			if(myShelfVerPanel.getItemCount()==0){
				noCollectionMsgLbl.setText(NO_COLLECTION_MESSAGE);
				noCollectionMsgLbl.getElement().setAttribute("alt", NO_COLLECTION_MESSAGE);
				noCollectionMsgLbl.getElement().setAttribute("title", NO_COLLECTION_MESSAGE);
				resetDragImage();
			}else{
				noCollectionMsgLbl.setText(LOADING_COLLECTION_MESSAGE);
				noCollectionMsgLbl.getElement().setAttribute("alt", LOADING_COLLECTION_MESSAGE);
				noCollectionMsgLbl.getElement().setAttribute("title", LOADING_COLLECTION_MESSAGE);
			}
		}
	}
	
	
	/**
	 * This method iterates the folder child items into which the folder is created from left shelf option.
	 */

	public void setFolderStyle(String gooruOid) {
		for(int i = 0; i < treeChildSelectedItem.getChildCount(); i++) { 
			 TreeItem item = treeChildSelectedItem.getChild(i);
			 getCreatedFolderParentWidget(item,gooruOid);
		}
	}
	
	/**
	 * This method will get the created parent folder widget, and sets as selected item.
	 * 
	 * @param item
	 * @param gooruOid
	 */
	private void getCreatedFolderParentWidget(TreeItem item, String gooruOid) {
		ShelfCollection topLevelTreeItem = (ShelfCollection) item.getWidget();
		if(gooruOid.equalsIgnoreCase(topLevelTreeItem.getCollectionDo().getGooruOid())) {
			 treeChildSelectedItem = item;
			 setFolderActiveStatus();
			 return;
		}
	}
	

	@Override
	public void removeMovedCollFolder(String sourceId) {
		ShelfCollection selectedWidget = (ShelfCollection) treeChildSelectedItem.getWidget();
		if(selectedWidget!=null) {
			if(selectedWidget.getCollectionDo().getGooruOid().equalsIgnoreCase(sourceId)) {
				TreeItem parentSelectedItem = treeChildSelectedItem.getParentItem();
				if(selectedWidget.getLevel()==0){
					AppClientFactory.fireEvent(new SetFolderParentNameEvent(i18n.GL0180()));
					treeChildSelectedItem.remove();
					setCollectionActiveStyle(selectedWidget);
					return;
				}
				if(selectedWidget.getLevel()==1) {
					AppClientFactory.fireEvent(new SetFolderParentNameEvent(i18n.GL0180()));
					treeChildSelectedItem.remove();
					setCollectionActiveStyle(selectedWidget);
					return;
				} else {
					treeChildSelectedItem.remove();
					treeChildSelectedItem = parentSelectedItem;
					if(treeChildSelectedItem!=null){
						ShelfCollection previousItem = (ShelfCollection) treeChildSelectedItem.getWidget();
						AppClientFactory.fireEvent(new SetFolderParentNameEvent(previousItem.getCollectionDo().getTitle()));
						setCollectionActiveStyle(selectedWidget);
					}
					return;
				}
			}
		} else {
			 for(int i = 0; i < myShelfVerPanel.getItemCount(); i++) {
				 TreeItem item = myShelfVerPanel.getItem(i);
				 ShelfCollection deletedItem = (ShelfCollection) item.getWidget();
				 item.remove();
				 AppClientFactory.fireEvent(new SetFolderParentNameEvent(i18n.GL0180()));
				 setCollectionActiveStyle(selectedWidget);
				 return;
			 }
		}
	}
	
	
	
	private void setCollectionActiveStyle(ShelfCollection selectedWidget) { 
		ShelfCollection previousShelfCollection = (ShelfCollection) previousTreeChildSelectedItem.getWidget();
		if(previousShelfCollection==null) {
			previousTreeChildSelectedItem = treeChildSelectedItem;
		}
		if(previousShelfCollection!=null&&(selectedWidget.getCollectionDo().getGooruOid()!=previousShelfCollection.getCollectionDo().getGooruOid())) {
			previousShelfCollection.setActiveStyle(false);
		}
		previousTreeChildSelectedItem = treeChildSelectedItem;
	}

	public int getFolderLevel() {
		int folderLevel = 0;
		String o1=AppClientFactory.getPlaceManager().getRequestParameter(O1_LEVEL);
		String o2=AppClientFactory.getPlaceManager().getRequestParameter(O2_LEVEL);
		String o3=AppClientFactory.getPlaceManager().getRequestParameter(O3_LEVEL);
		String id=AppClientFactory.getPlaceManager().getRequestParameter(ID);
		if(o3!=null) {folderLevel = 3;} else if (o2!=null) {folderLevel = 2;} else if(o1!=null) {folderLevel = 1;} else if(id!=null) {folderLevel = 4;}
		return folderLevel;
	}
	
	public void enableDisableOrganizePnl(boolean isShelfPage) {
		if(isShelfPage) {
			organizeRootPnl.setVisible(true);
//			organizeRootPnl.getElement().getStyle().setWidth(229, Unit.PX);
			if(getFolderLevel()==0) {
				organizeRootPnl.addStyleName(folderStyle.active());
			} else {
				organizeRootPnl.removeStyleName(folderStyle.active());
			}
		} else {
			organizeRootPnl.setVisible(false);
		}
	}
	
	@UiHandler("organizeRootPnl")
	public void clickOnOrganizeRootPnl(ClickEvent event) {
		ShelfCollection shelfCollection = (ShelfCollection) treeChildSelectedItem.getWidget();
		if(shelfCollection!=null&&shelfCollection.getLevel()!=0) {
			shelfCollection.setActiveStyle(false);
		}
		AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.SHELF);
	}
	
	public class CreateNewFolder implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			isLeftFolderClicked=true;
			FolderPopupUc folderPopupUc = new FolderPopupUc("", true) {
				@Override
				public void onClickPositiveButton(ClickEvent event, String folderName, String parentId, HashMap<String,String> params) {
					if(!folderName.isEmpty()) {
						getUiHandlers().createFolderInParent(folderName, parentId, params); 
						Window.enableScrolling(true);
						this.hide();
					}
				}
			};
			folderPopupUc.setGlassEnabled(true);
			folderPopupUc.removeStyleName("gwt-PopupPanelGlass");
			folderPopupUc.setPopupPosition(event.getRelativeElement().getAbsoluteLeft() + (110), Window.getScrollTop() + 50);
			Window.enableScrolling(false);
			folderPopupUc.show();
		}
	}
	
	public void setFolderActiveStatus() { 
		ShelfCollection shelfCollection = (ShelfCollection) treeChildSelectedItem.getWidget();
		if(shelfCollection.getCollectionDo().getType().equals("folder")) {
			TreeItem parent = treeChildSelectedItem.getParentItem();
			treeChildSelectedItem.getTree().setSelectedItem(parent, false);
			if(parent != null)parent.setSelected(false);
			treeChildSelectedItem.setState(treeChildSelectedItem.getState(), false);
			getUiHandlers().getChildFolderItems(shelfCollection.getCollectionDo().getGooruOid(),shelfCollection.getFolderOpenedStatus());
			shelfCollection.setFolderOpenedStatus(true);
		} else {
			getUiHandlers().getCollectionItems(shelfCollection.getCollectionDo().getGooruOid(),shelfCollection.getCollectionOpenedStatus()); 
			shelfCollection.setCollectionOpenedStatus(true);
		}
		if((AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.SHELF))) {
			shelfCollection.setActiveStyle(true);
		}
		
		ShelfCollection previousShelfCollection = (ShelfCollection) previousTreeChildSelectedItem.getWidget();
		if(previousShelfCollection==null) {
			previousTreeChildSelectedItem = treeChildSelectedItem;
		}
		if(previousShelfCollection!=null&&(shelfCollection.getCollectionDo().getGooruOid()!=previousShelfCollection.getCollectionDo().getGooruOid())) {
			previousShelfCollection.setActiveStyle(false);
		}
		previousTreeChildSelectedItem = treeChildSelectedItem;
	}
	
	public class CreateNewCollection implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			final String o1 = AppClientFactory.getPlaceManager().getRequestParameter(O1_LEVEL);
			final String o2 = AppClientFactory.getPlaceManager().getRequestParameter(O2_LEVEL);
			final String o3 = AppClientFactory.getPlaceManager().getRequestParameter(O3_LEVEL);
			Map<String, String> params = new HashMap<String, String>();
			if(o3!=null) {
				params.put(O1_LEVEL, o1);
				params.put(O2_LEVEL, o2);
				params.put(O3_LEVEL, o3);
				params.put("folderId", o3);
				AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.COLLECTION,params);
			} else if(o2!=null) {
				params.put(O1_LEVEL, o1);
				params.put(O2_LEVEL, o2);
				params.put("folderId", o2);
				AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.COLLECTION,params);
			} else if(o1!=null){
				params.put(O1_LEVEL, o1);
				params.put("folderId", o1);
				AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.COLLECTION,params);
			} else {
				AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.COLLECTION);
			}
			Window.enableScrolling(false);
			
		}
	}
	
	public void changeShelfPanelActiveStyle(String gooruOid) {
		for(int i = 0; i < myShelfVerPanel.getItemCount(); i++) { 
			TreeItem item = myShelfVerPanel.getItem(i);
			checkFolderItemStyle(item, gooruOid);
		}
	}
	
	@Override
	public void changeShelfPanelActiveStyle() { 
		String gooruOid = null;
		String o1 = AppClientFactory.getPlaceManager().getRequestParameter(O1_LEVEL);
		String o2 = AppClientFactory.getPlaceManager().getRequestParameter(O2_LEVEL);
		String o3 = AppClientFactory.getPlaceManager().getRequestParameter(O3_LEVEL);
		String id = AppClientFactory.getPlaceManager().getRequestParameter(ID);
		ShelfCollection shelfCollection = (ShelfCollection) treeChildSelectedItem.getWidget(); 
		
		if(shelfCollection==null || organizeRootPnl.getStyleName().contains(folderStyle.active())) {
			if(id!=null) {
				gooruOid = id;
			} else {
				gooruOid = o1;
			}
			for(int i = 0; i < myShelfVerPanel.getItemCount(); i++) { 
				TreeItem item = myShelfVerPanel.getItem(i);
				checkFolderItemStyle(item, gooruOid);
			}
		} else {
			/** If the selected folder is closed, and when clicked on right side the following condition executes and make that folder open. **/
			if(treeChildSelectedItem.getState()==false){
				treeChildSelectedItem.setState(true);
			}
			
			
			if(organizeRootPnl.getStyleName().contains(folderStyle.active())) {
				gooruOid = o1;
			} else if(shelfCollection.getLevel()==1) {
				if(id==null){
					gooruOid = o2;
				}else{
					gooruOid = id; 
				}
				
			} else if(shelfCollection.getLevel()==2) {
				if(id==null){
					gooruOid = o3;
				}else{
					gooruOid = id;
				}
			} else if(shelfCollection.getLevel()==3) {
				gooruOid = id;
			}
			for(int i = 0; i < treeChildSelectedItem.getChildCount(); i++) {
				 TreeItem item = treeChildSelectedItem.getChild(i);
				 checkFolderItemStyle(item, gooruOid);
			}
		}
	}
	
	private void checkFolderItemStyle(TreeItem item, String gooruOid) {
		ShelfCollection updatedItem = (ShelfCollection) item.getWidget();
		 if(gooruOid.equalsIgnoreCase(updatedItem.getCollectionDo().getGooruOid())) {
			 treeChildSelectedItem = item;
			 //updatedItem.setActiveStyle(true);
			 setFolderActiveStatus();
			 return;
		 }
	}

	/**
	 * The following method is not in use, It is handled in Shelf collection.java file.
	 */
	@Override
	public void updateShelfFolderName(String folderName) {
		ShelfCollection shelfCollection = (ShelfCollection) treeChildSelectedItem.getWidget();
		shelfCollection.getCollectionDo().setTitle(folderName);
		shelfCollection.titleLbl.setHTML(folderName);
	}

	@Override
	public void onDragOverOpenFolder(String folderId,boolean showcollectionFormView) {
		for(int i = 0; i < myShelfVerPanel.getItemCount(); i++) {
			TreeItem item = myShelfVerPanel.getItem(i);
			getDraggedOverFolder(item, folderId,showcollectionFormView);
		}
	}
	
	private void getDraggedOverFolder(TreeItem item, String gooruOid,boolean showcollectionFormView){ 
		ShelfCollection topLevelTreeItem = (ShelfCollection) item.getWidget();
		/** if the following condition isTrue, then user dropped on first level folder **/
		if(gooruOid.equalsIgnoreCase(topLevelTreeItem.getCollectionDo().getGooruOid())) {
			 treeChildSelectedItem = item;
			 /** if the following condition isFalse, then rename collection pop up will be opened. **/
			
			 if(showcollectionFormView){
				isDragged=true;
			    topLevelTreeItem.getCollectionForm(); 
			 }
			 setFolderActiveStatus();
			 return;
		}
			if(item.getState()){
				for(int i=0;i<item.getChildCount();i++){
					TreeItem secondLevelParentFolder = item.getChild(i);
					getSecondLevelFolderItems(secondLevelParentFolder,gooruOid,showcollectionFormView);
				}
			}
	}
	
	private void getSecondLevelFolderItems(TreeItem secondLevelParentFolder,String gooruOid,boolean showcollectionFormView) {
		ShelfCollection  secondLevelTreeItem = (ShelfCollection) secondLevelParentFolder.getWidget();
		/** if the following condition isTrue, then user dropped on second level folder **/
		if(gooruOid.equalsIgnoreCase(secondLevelTreeItem.getCollectionDo().getGooruOid())){ 
			treeChildSelectedItem = secondLevelParentFolder; 
			/** if the following condition isTrue, then rename collection pop up will be opened. **/
			if(showcollectionFormView){
				isDragged=true;
				secondLevelTreeItem.getCollectionForm();
			}
			setFolderActiveStatus();
			 return;
		}
			if(secondLevelParentFolder.getState()){
				for(int i=0;i<secondLevelParentFolder.getChildCount();i++){
					TreeItem thirdLevelParentFolder = secondLevelParentFolder.getChild(i);
					getThirdLevelFolderItems(thirdLevelParentFolder,gooruOid,showcollectionFormView);
				}
			}
	}

	private void getThirdLevelFolderItems(TreeItem thirdLevelParentFolder,	String gooruOid,boolean showcollectionFormView) {
		ShelfCollection  thirdLevelTreeItem = (ShelfCollection) thirdLevelParentFolder.getWidget();
		/** if the following condition isTrue, then user dropped on third level folder**/
		if(gooruOid.equalsIgnoreCase(thirdLevelTreeItem.getCollectionDo().getGooruOid())){
			treeChildSelectedItem = thirdLevelParentFolder;
			/** if the following condition isTrue, then rename collection pop up will be opened.**/
			if(showcollectionFormView){
				isDragged=true;
				thirdLevelTreeItem.getCollectionForm(); 
			}
			setFolderActiveStatus();
			 
			return;
		}
	}

	public TreeItem getFirstLevelTreeWidget(String gooruOid) {
		 for(int i = 0; i < myShelfVerPanel.getItemCount(); i++) {
			 TreeItem item = myShelfVerPanel.getItem(i);
			 ShelfCollection selectedItem = (ShelfCollection) item.getWidget();
			 if(selectedItem.getCollectionDo().getGooruOid().equalsIgnoreCase(gooruOid)) {
				 return item;
			 }
		 }
		 return null;
	}

	public TreeItem getSecondLevelTreeWidget(TreeItem widget, String gooruOid) {
		 for(int i = 0; i < widget.getChildCount(); i++) {
			 TreeItem item = widget.getChild(i);
			 ShelfCollection selectedItem = (ShelfCollection) item.getWidget();
			 if(selectedItem.getCollectionDo().getGooruOid().equalsIgnoreCase(gooruOid)) {
				 return item;
			 }
		 }
		 return null;
	}

	@Override
	public void insertMovedCollection(FolderDo folderDo,RefreshFolderType refreshFolderType, HashMap<String, String> params) {
		if(refreshFolderType.equals(RefreshFolderType.INSERT)) {
			if(params!=null) {
				if(params.get(O3_LEVEL)!=null) {
					TreeItem level1Item = getFirstLevelTreeWidget(params.get(O1_LEVEL));
					if(level1Item!=null) {
						TreeItem level2Item = getSecondLevelTreeWidget(level1Item, params.get(O2_LEVEL));
						if(level2Item!=null) {
							TreeItem level3Item = getSecondLevelTreeWidget(level2Item, params.get(O3_LEVEL));
							if(level3Item!=null) {
								flag=true;
								ShelfCollection selectedThirdWidget = (ShelfCollection) level3Item.getWidget();
								if(selectedThirdWidget.getFolderOpenedStatus()==true) {
									ShelfCollection shelfCollection = new ShelfCollection(folderDo, 4);
									TreeItem treeItem = new TreeItem(shelfCollection);
									shelfCollection.setWidgetPositions(4, 0, selectedThirdWidget.getUrlParams());
									level3Item.insertItem(0, treeItem);
									correctStyle(treeItem);
								}
							}
						}
					
						isDragged=true;	
						onDragOverOpenFolder(params.get(O1_LEVEL),false);
						isDragged=true;
						onDragOverOpenFolder(params.get(O2_LEVEL),false);
						isDragged=true;
						onDragOverOpenFolder(params.get(O3_LEVEL),false);

						setMovedCollectionActiveStatus("",folderDo,params,3);
					}	
				} else if(params.get(O2_LEVEL)!=null) {
					TreeItem level1Item = getFirstLevelTreeWidget(params.get(O1_LEVEL));
					if(level1Item!=null) {
						TreeItem level2Item = getSecondLevelTreeWidget(level1Item, params.get(O2_LEVEL));
						if(level2Item!=null) {
							ShelfCollection selectedSecondWidget = (ShelfCollection) level2Item.getWidget();
							if(selectedSecondWidget.getFolderOpenedStatus()==true) {
								flag=true;
								ShelfCollection shelfCollection = new ShelfCollection(folderDo, 3);
								TreeItem treeItem = new TreeItem(shelfCollection);
								shelfCollection.setWidgetPositions(3, 0, selectedSecondWidget.getUrlParams());
								level2Item.insertItem(0, treeItem);
								correctStyle(treeItem);
							}
						}
						isDragged=true;
						onDragOverOpenFolder(params.get(O1_LEVEL),false);
						isDragged=true;
						onDragOverOpenFolder(params.get(O2_LEVEL),false);
						setMovedCollectionActiveStatus("",folderDo,params,2);
					}
				} else if(params.get(O1_LEVEL)!=null) {
					TreeItem item = getFirstLevelTreeWidget(params.get(O1_LEVEL));
					if(item!=null) {
						ShelfCollection selectedWidget = (ShelfCollection) item.getWidget();
						if(selectedWidget.getFolderOpenedStatus()==true) {
							flag=true;
							ShelfCollection shelfCollection = new ShelfCollection(folderDo, 2);
							TreeItem treeItem = new TreeItem(shelfCollection);
							shelfCollection.setWidgetPositions(2, 0, selectedWidget.getUrlParams());
							item.insertItem(0, treeItem);
							correctStyle(treeItem);
						}
					}
					
					isDragged=true;
					onDragOverOpenFolder(params.get(O1_LEVEL),false);
					setMovedCollectionActiveStatus("",folderDo,params,1); 
					
				} else {
					ShelfCollection shelfCollection = new ShelfCollection(folderDo, 1);
					TreeItem treeItem = new TreeItem(shelfCollection);
					shelfCollection.setWidgetPositions(1, 0, null);
					myShelfVerPanel.insertItem(0, treeItem);
					correctStyle(treeItem);
					setMovedCollectionActiveStatus("",folderDo,params,0);
				}
			}
		}
	}

	
	public void setMovedCollectionActiveStatus(String gooruOid,FolderDo folderDo, HashMap<String, String> params, int level){ 
		if(level==1){
			params.put("id", folderDo.getGooruOid());
			AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.SHELF, params); 
		}else if(level==2){
			params.put("id", folderDo.getGooruOid());
			AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.SHELF, params); 
		}else if(level==3){
			params.put("id", folderDo.getGooruOid());
			AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.SHELF, params); 
		}else if(level==0){
			params.put("id", folderDo.getGooruOid()); 
			AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.SHELF, params); 
			for(int i = 0; i < myShelfVerPanel.getItemCount(); i++) {
				TreeItem item = myShelfVerPanel.getItem(i);
				 getMovedCollectionWidget(item,folderDo.getGooruOid());
			}	
		}
	}
	
	public void setCreatedFolderActiveStatus(String gooruOid,FolderDo folderDo, HashMap<String, String> params, int level){ 
		if(level==1){
			params.put(O2_LEVEL, folderDo.getGooruOid());
			if(!isFromAddResourcePresenter){
				AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.SHELF, params); 
			}
		}else if(level==2){
			params.put(O3_LEVEL, folderDo.getGooruOid());
			if(!isFromAddResourcePresenter){
			AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.SHELF, params); 
			}
		}else if(level==3){
			if(!isFromAddResourcePresenter){
			AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.SHELF, params); 
			}
		}else if(level==0){
			if(!isFromAddResourcePresenter){
			AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.SHELF, params);
			}
			for(int i = 0; i < myShelfVerPanel.getItemCount(); i++) {
				TreeItem item = myShelfVerPanel.getItem(i);
				 getMovedCollectionWidget(item,folderDo.getGooruOid());
			}	
		}
	}
	
	

	/**
	 * This method iterates the folder child items to which the collection moved.
	 */
	@Override
	public void setMovedCollectionStyle(String gooruOid) {
		for(int i = 0; i < treeChildSelectedItem.getChildCount(); i++) { 
			 TreeItem item = treeChildSelectedItem.getChild(i);
			 getMovedCollectionWidget(item,gooruOid);
		}
	}
	
	/**
	 * This method will get the moved collection widget, and sets as selected item.
	 * 
	 * @param item
	 * @param gooruOid
	 */
	private void getMovedCollectionWidget(TreeItem item, String gooruOid) {
		ShelfCollection topLevelTreeItem = (ShelfCollection) item.getWidget();
		if(gooruOid.equalsIgnoreCase(topLevelTreeItem.getCollectionDo().getGooruOid())) {
			 treeChildSelectedItem = item;
			 setCollectionStyle(topLevelTreeItem);
			 return;
		}
	}
	
	/**
	 * Once the collection is copied in folders, the following method will give the folder widget in which collection got copied.
	 * @param params
	 */
	private void setCopiedCollectionStyle(HashMap<String, String> params) {
		ShelfCollection copiedCollectionFolderWidget = (ShelfCollection) treeChildSelectedItem.getWidget();
		if(params.get(O3_LEVEL)!=null) {
			setCopiedcollectionStyle(copiedCollectionFolderWidget,params.get("id"));
		}else if(params.get(O2_LEVEL)!=null){
			setCopiedcollectionStyle(copiedCollectionFolderWidget,params.get("id"));
			
		}else if(params.get(O1_LEVEL)!=null){
			setCopiedcollectionStyle(copiedCollectionFolderWidget,params.get("id"));
		}
	}
	
	/**
	 * This method iterates the child items of the folder in which collection is copied.
	 * 
	 * @param copiedCollectionFolderWidget
	 * @param CopiedcollectionId
	 */
	private void setCopiedcollectionStyle(ShelfCollection copiedCollectionFolderWidget, String CopiedcollectionId) {
		for(int i = 0; i < treeChildSelectedItem.getParentItem().getChildCount(); i++) { 
			 TreeItem item = treeChildSelectedItem.getParentItem().getChild(i);
			 getMovedCollectionWidget(item,CopiedcollectionId);
		}
	}

	/**
	 * This method is used to set the active style on left panel for the moved and copied collections.
	 * @param topLevelTreeItem
	 */
	private void setCollectionStyle(ShelfCollection topLevelTreeItem) {
		
		if((AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.SHELF))) {
			topLevelTreeItem.setActiveStyle(true);
		}
		ShelfCollection previousShelfCollection = (ShelfCollection) previousTreeChildSelectedItem.getWidget();
		if(previousShelfCollection==null) {
			previousTreeChildSelectedItem = treeChildSelectedItem;
		}
		if(previousShelfCollection!=null&&(topLevelTreeItem.getCollectionDo().getGooruOid()!=previousShelfCollection.getCollectionDo().getGooruOid())) {
			previousShelfCollection.setActiveStyle(false);
		}
		previousTreeChildSelectedItem = treeChildSelectedItem;
	}

	/**
	 * The following method will be called through an event, only when we clicked on folder items on right side.To set the active status of clicked item.
	 */
	
	@Override
	public void setChildFolderCollectionStyle(HashMap<String, String> params, String clickType) {
		if(clickType.equalsIgnoreCase("folder")){
			if(params.get(O3_LEVEL)!=null){
				isDragged=true;
				onDragOverOpenFolder(params.get(O1_LEVEL),false);
				isDragged=true;
				onDragOverOpenFolder(params.get(O2_LEVEL),false);
				isDragged=true;
				onDragOverOpenFolder(params.get(O3_LEVEL),false);
			}else if(params.get(O2_LEVEL)!=null){
				isDragged=true;
				onDragOverOpenFolder(params.get(O1_LEVEL),false);
				isDragged=true;
				onDragOverOpenFolder(params.get(O2_LEVEL),false);
			}
		}else if(clickType.equalsIgnoreCase("scollection")||clickType.equalsIgnoreCase("collection")){ 
			if(params.get(O3_LEVEL)!=null){
				isDragged=true;
				onDragOverOpenFolder(params.get(O1_LEVEL),false);
				isDragged=true;
				onDragOverOpenFolder(params.get(O2_LEVEL),false);
				isDragged=true;
				onDragOverOpenFolder(params.get(O3_LEVEL),false);
				setMovedCollectionStyle(params.get("id")); 
			}else if(params.get(O2_LEVEL)!=null){
				isDragged=true;
				onDragOverOpenFolder(params.get(O1_LEVEL),false);
				isDragged=true;
				onDragOverOpenFolder(params.get(O2_LEVEL),false);
				setMovedCollectionStyle(params.get("id")); 
			}else if(params.get(O1_LEVEL)!=null){
				isDragged=true;
				onDragOverOpenFolder(params.get(O1_LEVEL),false);
				setMovedCollectionStyle(params.get("id")); 
			}
		}
	}
	
	public void adjustTreeItemElementsStyle(Tree shelfTreePanel){
		int treeItemsCount=shelfTreePanel.getItemCount();
		if(shelfTreePanel!=null&&treeItemsCount>0){
			for(int i=0;i<treeItemsCount;i++){
				TreeItem treeItem=shelfTreePanel.getItem(i);
				Widget shelfWidget= treeItem.getWidget();
				if(shelfWidget instanceof ShelfCollection){
					adjustChildTreeItemsStyle(treeItem);
				}
				correctStyle(treeItem);
			}
		}
	}
	public void adjustChildTreeItemsStyle(TreeItem treeItem){
		int treeItemsCount=treeItem.getChildCount();
		if(treeItem!=null&&treeItemsCount>0){
			for(int i=0;i<treeItemsCount;i++){
				TreeItem childTreeItem=treeItem.getChild(i);
				Widget shelfWidget= childTreeItem.getWidget();
				if(shelfWidget instanceof ShelfCollection){
					adjustChildTreeItemsStyle(childTreeItem);
				}
				correctStyle(childTreeItem);
			}
		}
	}

	@Override
	public void openParentFolderEvent() {
		/** If the selected folder is closed, and when created folder on right side the following condition executes and make that folder will open. **/
		ShelfCollection shelfCollection = (ShelfCollection) treeChildSelectedItem.getWidget();
		if(treeChildSelectedItem.getState()==false){
			treeChildSelectedItem.setState(true);
//			shelfCollection.setOpenStyle(true);
		}
	}
	/** 
	 * This method is to get the childPageNumber
	 */
	public Integer getChildPageNumber() {
		return childPageNumber;
	}

	@Override
	public void setChildPageNumber(Integer childPageNumber) {
		this.childPageNumber = childPageNumber;
	}

	@Override
	public void updateShelfFolderMetaData(String ideas, String performanceTasks, String questions) {
		ShelfCollection shelfCollection = (ShelfCollection) treeChildSelectedItem.getWidget();
		shelfCollection.getCollectionDo().setIdeas(ideas);
		shelfCollection.getCollectionDo().setPerformanceTasks(performanceTasks);
		shelfCollection.getCollectionDo().setQuestions(questions);
	}
	
	public static void onClosingAndriodorIpaddiv()
	{
		  shelfFocPanel.getElement().setAttribute("style", "position:fixed;");
	}

	
	/**
	 * Setting the active style when opened from the player.
	 */
	@Override
	public void setCollectionActiveStyle() {
		 
		String gooruOid = null;
		String o1 = AppClientFactory.getPlaceManager().getRequestParameter(O1_LEVEL);
		String o2 = AppClientFactory.getPlaceManager().getRequestParameter(O2_LEVEL);
		String o3 = AppClientFactory.getPlaceManager().getRequestParameter(O3_LEVEL);
		String id = AppClientFactory.getPlaceManager().getRequestParameter(ID);
		ShelfCollection shelfCollection = (ShelfCollection) treeChildSelectedItem.getWidget(); 
		
		if(shelfCollection==null || organizeRootPnl.getStyleName().contains(folderStyle.active())) {
			if(id!=null) {
				gooruOid = id;
			} else {
				gooruOid = o1;
			}
			for(int i = 0; i < myShelfVerPanel.getItemCount(); i++) { 
				TreeItem item = myShelfVerPanel.getItem(i);
				checkFolderItemStyle(item, gooruOid);
			}
		} else {
			if(id!=null) {
				gooruOid = id;
			}
			for(int i = 0; i < myShelfVerPanel.getItemCount(); i++) { 
				TreeItem item = myShelfVerPanel.getItem(i);
				checkFolderItemStyle(item, gooruOid);
			}
		}
	
	}
	
	@Override
	public void refreshFolderItemDataInSearchAddResource(FolderDo folderDo,
			RefreshFolderType refreshFolderType, HashMap<String, String> params) {
		System.out.println("params::::::"+params);
		System.out.println("refreshFolderType here::::::"+refreshFolderType);
		if(refreshFolderType.equals(RefreshFolderType.INSERT)) {
		if(params!=null) {
			if(params.get(O3_LEVEL)!=null) {
				TreeItem level1Item = getFirstLevelTreeWidget(params.get(O1_LEVEL));
				if(level1Item!=null) {
					TreeItem level2Item = getSecondLevelTreeWidget(level1Item, params.get(O2_LEVEL));
					if(level2Item!=null) {
						TreeItem level3Item = getSecondLevelTreeWidget(level2Item, params.get(O3_LEVEL));
						if(level3Item!=null) {
							ShelfCollection selectedThirdWidget = (ShelfCollection) level3Item.getWidget();
							if(selectedThirdWidget.getFolderOpenedStatus()==true) {
								ShelfCollection shelfCollection = new ShelfCollection(folderDo, 4);
								TreeItem treeItem = new TreeItem(shelfCollection);
								shelfCollection.setWidgetPositions(4, 0, selectedThirdWidget.getUrlParams());
								level3Item.insertItem(0, treeItem);
								correctStyle(treeItem);
							}
						}
					}
				}	
			} else if(params.get(O2_LEVEL)!=null) {
				TreeItem level1Item = getFirstLevelTreeWidget(params.get(O1_LEVEL));
				if(level1Item!=null ) {
					TreeItem level2Item = getSecondLevelTreeWidget(level1Item, params.get(O2_LEVEL));
					if(level2Item!=null && !isLeftFolderClicked) {
						ShelfCollection selectedSecondWidget = (ShelfCollection) level2Item.getWidget();
						if(selectedSecondWidget.getFolderOpenedStatus()==true) {
							ShelfCollection shelfCollection = new ShelfCollection(folderDo, 3);
							TreeItem treeItem = new TreeItem(shelfCollection);
							shelfCollection.setWidgetPositions(3, 0, selectedSecondWidget.getUrlParams());
							level2Item.insertItem(0, treeItem);
							correctStyle(treeItem);
						}
					}else{
						isLeftFolderClicked=false;
						if(level2Item!=null) {
							ShelfCollection selectedSecondWidget = (ShelfCollection) level2Item.getWidget();
							if(selectedSecondWidget.getFolderOpenedStatus()==true) {
								ShelfCollection shelfCollection = new ShelfCollection(folderDo, 3);
								TreeItem treeItem = new TreeItem(shelfCollection);
								shelfCollection.setWidgetPositions(3, 0, selectedSecondWidget.getUrlParams());
								level2Item.insertItem(0, treeItem);
								correctStyle(treeItem);
							}
						}
						isDragged=true;
						onDragOverOpenFolder(params.get(O1_LEVEL),false);
						isDragged=true;
						onDragOverOpenFolder(params.get(O2_LEVEL),false);
						setCreatedFolderActiveStatus("",folderDo,params,2);
						AppClientFactory.fireEvent(new SetFolderParentNameEvent(folderDo.getTitle()));
						AppClientFactory.fireEvent(new SetFolderMetaDataEvent(StringUtil.getFolderMetaData(folderDo)));
						setFolderStyle(folderDo.getGooruOid());
					}
				}
			} else if(params.get(O1_LEVEL)!=null) {
				TreeItem item = getFirstLevelTreeWidget(params.get(O1_LEVEL));
				if(item!=null && !isLeftFolderClicked) {
					ShelfCollection selectedWidget = (ShelfCollection) item.getWidget();
					if(selectedWidget.getFolderOpenedStatus()==true) {
						ShelfCollection shelfCollection = new ShelfCollection(folderDo, 2);
						TreeItem treeItem = new TreeItem(shelfCollection);
						shelfCollection.setWidgetPositions(2, 0, selectedWidget.getUrlParams());
						item.insertItem(0, treeItem);	
						correctStyle(treeItem);
					}
				}else{
					isLeftFolderClicked=false;
					if(item!=null) {
						ShelfCollection selectedWidget = (ShelfCollection) item.getWidget();
						if(selectedWidget.getFolderOpenedStatus()==true) {
							ShelfCollection shelfCollection = new ShelfCollection(folderDo, 2);
							TreeItem treeItem = new TreeItem(shelfCollection);
							shelfCollection.setWidgetPositions(2, 0, selectedWidget.getUrlParams());
							item.insertItem(0, treeItem);
							correctStyle(treeItem);
						}
					}
					isDragged=true;
					onDragOverOpenFolder(params.get(O1_LEVEL),false);
					setCreatedFolderActiveStatus("",folderDo,params,1); 
					AppClientFactory.fireEvent(new SetFolderParentNameEvent(folderDo.getTitle()));
					AppClientFactory.fireEvent(new SetFolderMetaDataEvent(StringUtil.getFolderMetaData(folderDo)));
					setFolderStyle(folderDo.getGooruOid());
				}
				  
			}
			
		}
		params.clear();
		
}
	}

}