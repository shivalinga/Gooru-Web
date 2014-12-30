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
package org.ednovo.gooru.client.mvp.search;

import java.util.HashMap;
import java.util.List;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BaseViewWithHandlers;
import org.ednovo.gooru.client.mvp.play.collection.end.study.CloseCollectionPlayerEvent;
import org.ednovo.gooru.client.mvp.shelf.list.TreeMenuImages;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.folder.FolderDo;
import org.ednovo.gooru.shared.model.folder.FolderListDo;
import org.ednovo.gooru.shared.model.search.CollectionSearchResultDo;
import org.ednovo.gooru.shared.model.search.ResourceSearchResultDo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class AddResourceContainerView extends
		BaseViewWithHandlers<AddResourceContainerUiHandlers> implements
		IsAddResourceContainerView {

	private static AddResourceContainerViewUiBinder uiBinder = GWT
			.create(AddResourceContainerViewUiBinder.class);

	private MessageProperties i18n = GWT.create(MessageProperties.class);

	interface AddResourceContainerViewUiBinder extends
			UiBinder<Widget, AddResourceContainerView> {
	}

	@UiField
	ScrollPanel dropdownListContainerScrollPanel;

	@UiField
	HTMLPanel floderTreeContainer, buttonsContainer,
			createCollectionbuttonsContainer, loadingImage, enableSuccessView;
	
	

	@UiField
	Button addResourceBtnLbl, okButton;

	@UiField
	public Button cancelButton;

	@UiField
	Label addingText, displayCountLabel, addResourceText, displayErrorLabel,
			headerTitle;

	@UiField
	Anchor addCollectiorOrReourceText;

	@UiField
	FlowPanel addContent;

	@UiField
	InlineLabel headerTitleDes;
	
	Label loadingText = null;
	
	private boolean isLeftFolderClicked = false;
	HashMap<String,String> urlparams ;
	private int offset = 0;
	private int limit = 20;
	private int totalHitCount = 0;
	private int pageNum = 1;
	private String selectedFolderGooruOid;
	private String selectedCollectionGooruOid;
	boolean isSelectedFolder = false;
	boolean isSelectedCollection = false;
	boolean isResourceSearch = false;
	boolean isCollectionSearch = false;
	private String currentsearchType;
	ResourceSearchResultDo searchResultDo;
	CollectionSearchResultDo collectionsearchResultDo;
	private static final String O1_LEVEL = "o1";

	private static final String O2_LEVEL = "o2";

	private static final String O3_LEVEL = "o3";
	boolean isPlayer = false;
	boolean isTopMostSelected =true;
	HTMLPanel topMostTreeItem=new HTMLPanel("");
	private Tree folderTreePanel = new Tree(new TreeMenuImages()) {
		@Override
		public void onBrowserEvent(Event event) {
			int eventType = DOM.eventGetType(event);
			if (!(eventType == Event.ONKEYUP || eventType == Event.ONKEYPRESS || eventType == Event.ONKEYDOWN)) {
				super.onBrowserEvent(event);
			}
		}
	};

	private CollectionTreeItem cureentcollectionTreeItem = null;
	private CollectionTreeItem previousSelectedItem = null;
	private FolderTreeItem currentFolderSelectedTreeItem = null;
	private FolderTreeItem previousFolderSelectedTreeItem = null;

	@Inject
	public AddResourceContainerView() {
		setWidget(uiBinder.createAndBindUi(this));
		AddResourceContainerCBundle.INSTANCE.css().ensureInjected();
		loadingImage.setVisible(true);
		enableSuccessView.setVisible(false);
		buttonsContainer.setVisible(true);
		dropdownListContainerScrollPanel.setVisible(true);
		dropdownListContainerScrollPanel
				.addScrollHandler(new ScrollDropdownListContainer());
		displayCountLabel.setVisible(false);
		addingText.setVisible(false);
		topMostTreeItem.addStyleName(AddResourceContainerCBundle.INSTANCE
				.css().addMyCollectionsStyle());
		// selectedCollectionGooruOid =null;
		topMostTreeItem.getElement().setInnerHTML("My Collections");
		/*topMostTreeItem.getElement().setAttribute("style", "background-color: #cfe3f1;");*/
		addingText.setText(i18n.GL0591());
		urlparams= new HashMap<String, String>();
		folderTreePanel.addSelectionHandler(new SelectionHandler<TreeItem>() {
			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
					displayErrorLabel.setText("");
					isTopMostSelected =false;
					if(isTopMostSelected){
						/*topMostTreeItem.getElement().setAttribute("style", "background-color: #cfe3f1;");*/
						isSelectedFolder=false;
					}else{
						topMostTreeItem.getElement().setAttribute("style", "background-color: none;");
					}
					ClickHandler handler = new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							// TODO Auto-generated method stub
							isTopMostSelected =true;
							isSelectedFolder=false;
							removePreviousSelectedItem();
							/*topMostTreeItem.getElement().setAttribute("style", "background-color: #cfe3f1;");*/
						}
					};
					topMostTreeItem.addDomHandler(handler, ClickEvent.getType());
					getButtonVisiblity();
					final TreeItem item = (TreeItem) event.getSelectedItem();
					Widget folderWidget = item.getWidget();
					FolderTreeItem folderTreeItemWidget = null;
					if (folderWidget instanceof FolderTreeItem) {
						folderTreeItemWidget = (FolderTreeItem) folderWidget;
						if (folderTreeItemWidget.isOpen()) {
							folderTreeItemWidget
									.removeStyleName(AddResourceContainerCBundle.INSTANCE
											.css().open());
							folderTreeItemWidget.setOpen(false);
						} else {
							folderTreeItemWidget
									.addStyleName(AddResourceContainerCBundle.INSTANCE
											.css().open());
							folderTreeItemWidget.setOpen(true);
						}
						removePreviousSelectedItem();
						currentFolderSelectedTreeItem = folderTreeItemWidget;
						previousFolderSelectedTreeItem = currentFolderSelectedTreeItem;
						currentFolderSelectedTreeItem
								.addStyleName(AddResourceContainerCBundle.INSTANCE
										.css().selected());
						previousSelectedItem = cureentcollectionTreeItem = null;
						TreeItem parent = item.getParentItem();
						item.getTree().setSelectedItem(parent, false); // TODO FIX
																		// ME
						if (!folderTreeItemWidget.isApiCalled()) {
							folderTreeItemWidget.setApiCalled(true);
							getFolderItems(item, folderTreeItemWidget.getGooruOid());
						}
						selectedFolderGooruOid = folderTreeItemWidget.getGooruOid();
						if(currentFolderSelectedTreeItem.getFolerLevel()==1) {
							urlparams.clear();
							urlparams.put(O1_LEVEL, selectedFolderGooruOid);
						}
						if(currentFolderSelectedTreeItem.getFolerLevel()==2) {
							urlparams.put(O1_LEVEL, urlparams.get(O1_LEVEL));
							urlparams.put(O2_LEVEL, selectedFolderGooruOid);
						}
						if(currentFolderSelectedTreeItem.getFolerLevel()==3) {
							urlparams.put(O1_LEVEL, urlparams.get(O1_LEVEL));
							urlparams.put(O2_LEVEL, urlparams.get(O2_LEVEL));
							urlparams.put(O3_LEVEL, selectedFolderGooruOid);
						}
						if(currentFolderSelectedTreeItem.getFolerLevel()==4) {
						}
						isSelectedFolder = true;
						isSelectedCollection = false;
						
						if (parent != null)
							parent.setSelected(false); // TODO FIX ME
						item.setState(!item.getState(), false);
						// setSelectedCollectionsCount(item.getChildCount());
					} else if (folderWidget instanceof CollectionTreeItem) {
						removePreviousSelectedItem();
						cureentcollectionTreeItem = (CollectionTreeItem) folderWidget;
						previousSelectedItem = cureentcollectionTreeItem;
						cureentcollectionTreeItem
								.addStyleName(AddResourceContainerCBundle.INSTANCE
										.css().selected());
						previousFolderSelectedTreeItem = currentFolderSelectedTreeItem = null;
						selectedCollectionGooruOid = cureentcollectionTreeItem.getGooruOid();
						isSelectedCollection = true;
						isSelectedFolder = false;
						// setSelectedCollectionTitle();
						// closeDropDown();
					}
					if (AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(
							PlaceTokens.RESOURCE_SEARCH) || AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(
									PlaceTokens.COLLECTION_PLAY)) {
						topMostTreeItem.getElement().getStyle().setDisplay(Display.NONE);
					}else{
						topMostTreeItem.getElement().getStyle().setDisplay(Display.BLOCK);
					}
				}
			
		});
		floderTreeContainer.clear();
		floderTreeContainer.add(topMostTreeItem);
		floderTreeContainer.add(folderTreePanel);
		if (AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(
				PlaceTokens.RESOURCE_SEARCH) || AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(
						PlaceTokens.COLLECTION_PLAY)) {
			topMostTreeItem.getElement().getStyle().setDisplay(Display.NONE);
		}else{
			topMostTreeItem.getElement().getStyle().setDisplay(Display.BLOCK);
		}
		/*if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.RESOURCE_SEARCH)){
			topMostTreeItem.getElement().setAttribute("style", "display:none");
		}else if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.COLLECTION_SEARCH)){
			topMostTreeItem.getElement().setAttribute("style", "display:block");
		}*/
		loadingImage.setVisible(false);
		folderTreePanel.addItem(loadingTreeItem());
	}

	private void setSelectedCollectionTitle() {
		if (cureentcollectionTreeItem != null) {
			displayCountLabel.setText("\""
					+ cureentcollectionTreeItem.getCollectionName() + "\" "
					+ i18n.GL1975());
			displayCountLabel.getElement().setAttribute(
					"alt",
					"\"" + cureentcollectionTreeItem.getCollectionName()
							+ "\" " + i18n.GL1975());
			displayCountLabel.getElement().setAttribute(
					"title",
					"\"" + cureentcollectionTreeItem.getCollectionName()
							+ "\" " + i18n.GL1975());
		}

	}

	private void setSelectedCollectionsCount(int count) {
		if (count > 0) {
			String label = count == 1 ? count + " collection" : count
					+ " collections";
			displayCountLabel.setText(label + " " + i18n.GL1975());
			displayCountLabel.getElement().setAttribute("alt",
					label + " " + i18n.GL1975());
			displayCountLabel.getElement().setAttribute("title",
					label + " " + i18n.GL1975());

		} else {
			displayCountLabel.setText("");
			displayCountLabel.getElement().setAttribute("alt", "");
			displayCountLabel.getElement().setAttribute("title", "");
		}

	}

	private void getFolderItems(TreeItem item, String parentId) {
		getUiHandlers().getFolderItems(item, parentId);
	}

	private void removePreviousSelectedItem() {
		if (previousFolderSelectedTreeItem != null) {
			previousFolderSelectedTreeItem
					.removeStyleName(AddResourceContainerCBundle.INSTANCE.css()
							.selected());
		}
		if (previousSelectedItem != null) {
			previousSelectedItem
					.removeStyleName(AddResourceContainerCBundle.INSTANCE.css()
							.selected());
		}
	}

	public TreeItem loadingTreeItem() {
		
		if (AppClientFactory.getCurrentPlaceToken().equals(
				PlaceTokens.RESOURCE_SEARCH) || AppClientFactory.getCurrentPlaceToken().equals(
						PlaceTokens.COLLECTION_PLAY)) {
			loadingText = new Label(i18n.GL1452());
		} else {
			loadingText = new Label(i18n.GL2051());
		}
		loadingText.setStyleName(AddResourceContainerCBundle.INSTANCE.css()
				.loadingText());
		return new TreeItem(loadingText);
	}
	public TreeItem loadingTreeItemForNoFolder() {
		loadingText = new Label("");
		return new TreeItem(loadingText);
	}

	private class ScrollDropdownListContainer implements ScrollHandler {
		@Override
		public void onScroll(ScrollEvent event) {
			if ((dropdownListContainerScrollPanel.getVerticalScrollPosition() == dropdownListContainerScrollPanel
					.getMaximumVerticalScrollPosition())
					&& (totalHitCount > pageNum * limit)) {
				getUiHandlers().getWorkspaceData(pageNum * limit, limit, false,
						currentsearchType);
				pageNum++;
			}
		}
	}

	public class FolderTreeItem extends Composite {
		private FlowPanel folderContainer = null;
		private String gooruOid = null;
		Label floderName = null;
		Label arrowLabel = null;
		private boolean isOpen = false;
		private boolean isApiCalled = false;
		private int folerLevel = 1;

		public FolderTreeItem() {
			initWidget(folderContainer = new FlowPanel());
			folderContainer.setStyleName(AddResourceContainerCBundle.INSTANCE
					.css().folderLevel());
			Label arrowLabel = new Label();
			arrowLabel.setStyleName(AddResourceContainerCBundle.INSTANCE.css()
					.arrow());
			folderContainer.add(arrowLabel);
			floderName = new Label();
			floderName.setStyleName(AddResourceContainerCBundle.INSTANCE.css()
					.title());
			folderContainer.add(floderName);
		}

		public FolderTreeItem(String levelStyleName, String folderTitle,
				String gooruOid) {
			this();
			if (levelStyleName != null) {
				folderContainer.addStyleName(levelStyleName);
			}
			this.gooruOid = gooruOid;
			floderName.setText(folderTitle);
		}

		public boolean isOpen() {
			return isOpen;
		}

		public void setOpen(boolean isOpen) {
			this.isOpen = isOpen;
		}

		public String getGooruOid() {
			return gooruOid;
		}

		public boolean isApiCalled() {
			return isApiCalled;
		}

		public void setApiCalled(boolean isApiCalled) {
			this.isApiCalled = isApiCalled;
		}

		public int getFolerLevel() {
			return folerLevel;
		}

		public void setFolerLevel(int folerLevel) {
			this.folerLevel = folerLevel;
		}
	}

	public class CollectionTreeItem extends Composite {
		private FlowPanel folderContainer = null;
		Label folderName = null;
		private String collectionName = null;
		private String gooruOid = null;
		private boolean isOpen = false;

		public CollectionTreeItem() {
			initWidget(folderContainer = new FlowPanel());
			folderContainer.setStyleName(AddResourceContainerCBundle.INSTANCE
					.css().foldercollection());
			folderName = new Label();
			folderName.setStyleName(AddResourceContainerCBundle.INSTANCE.css()
					.title());
			folderContainer.add(folderName);
		}

		public CollectionTreeItem(String levelStyleName) {
			this();
			folderContainer.addStyleName(levelStyleName);
		}

		public CollectionTreeItem(String levelStyleName, String folderTitle,
				String gooruOid) {
			this();
			if (levelStyleName != null) {
				folderContainer.addStyleName(levelStyleName);
			}
			this.gooruOid = gooruOid;
			this.collectionName = folderTitle;
			folderName.setText(folderTitle);
		}

		public boolean isOpen() {
			return isOpen;
		}

		public void setOpen(boolean isOpen) {
			this.isOpen = isOpen;
		}

		public String getGooruOid() {
			return gooruOid;
		}

		public String getCollectionName() {
			return collectionName;
		}

	}

	@Override
	public void displayWorkspaceData(FolderListDo folderListDo,
			boolean clearShelfPanel, String searchType) {
		currentsearchType = searchType;
		resetEmptyCollMsg();
		if (!dropdownListContainerScrollPanel.isVisible()) {
			dropdownListContainerScrollPanel.setVisible(true);
		}

		if (clearShelfPanel) {
			folderTreePanel.clear();
		}
		if (searchType.equalsIgnoreCase("collection")) {
			isCollectionSearch = true;
			isResourceSearch = false;
			addResourceText.setText("Add this Collection to one of your existing Folders");
			addResourceText.setStyleName(AddResourceContainerCBundle.INSTANCE.css().addCollectionTextStyles());
		} else if (searchType.equalsIgnoreCase("resource")) {
			isResourceSearch = true;
			isCollectionSearch = false;
			addResourceText.setText(i18n.GL2088());
			addResourceText.setStyleName(AddResourceContainerCBundle.INSTANCE.css().addResourceTextStyles());
			addCollectiorOrReourceText.setText(i18n.GL2089());
			addResourceText.getElement().setAttribute("style",
					"display: inline-block;");
			addCollectiorOrReourceText.getElement().setAttribute("style",
					"display: inline-block;");
			createCollectionbuttonsContainer.getElement().setAttribute("style",
					"margin-left: 36px;margin-top: 10px;");
		}
		if (folderListDo != null) {
			List<FolderDo> foldersArrayList = folderListDo.getSearchResult();
			setPagination(folderListDo.getCount());
			if (foldersArrayList != null && foldersArrayList.size() > 0) {
				for (int i = 0; i < foldersArrayList.size(); i++) {
					FolderDo floderDo = foldersArrayList.get(i);
					if (isResourceSearch) {
						if (floderDo.getType().equals("folder")) {
							TreeItem folderItem = new TreeItem(
									new FolderTreeItem(null,
											floderDo.getTitle(),
											floderDo.getGooruOid()));
							folderTreePanel.addItem(folderItem);
							adjustTreeItemStyle(folderItem);
						} else if (floderDo.getType().equals("scollection")) {
							TreeItem folderItem = new TreeItem(
									new CollectionTreeItem(null,
											floderDo.getTitle(),
											floderDo.getGooruOid()));
							folderTreePanel.addItem(folderItem);
							adjustTreeItemStyle(folderItem);
						}
					} else if (isCollectionSearch) {
						if (floderDo.getType().equals("folder")) {
							TreeItem folderItem = new TreeItem(
									new FolderTreeItem(null,
											floderDo.getTitle(),
											floderDo.getGooruOid()));
							folderTreePanel.addItem(folderItem);
							adjustTreeItemStyle(folderItem);
						}
					}
				}
			}
		}

	}

	@Override
	public void clearShelfData() {
		
		// TODO Auto-generated method stub
		addingText.setVisible(false);
		folderTreePanel.clear();
		folderTreePanel.addItem(loadingTreeItem());
		cureentcollectionTreeItem = null;
		previousSelectedItem = null;
		// dropdownListPlaceHolder.setText(GL1377);
		offset = 0;
		limit = 20;
		totalHitCount = 0;
		pageNum = 1;
	}

	@Override
	public void setFolderItems(TreeItem item, FolderListDo folderListDo) {
		displayWorkspaceData(item, folderListDo);
	}

	private void displayWorkspaceData(TreeItem item, FolderListDo folderListDo) {
		if (folderListDo != null) {
			List<FolderDo> foldersArrayList = folderListDo.getSearchResult();
			// setSelectedCollectionsCount(folderListDo.getCount());
			if (foldersArrayList != null && foldersArrayList.size() > 0) {
				FolderTreeItem folderTreeItemWidget = (FolderTreeItem) item
						.getWidget();
				int folderLevel = folderTreeItemWidget.getFolerLevel();
				for (int i = 0; i < foldersArrayList.size(); i++) {
					FolderDo floderDo = foldersArrayList.get(i);
					if (currentsearchType.equalsIgnoreCase("collection")) {
						isCollectionSearch = true;
						isResourceSearch = false;
					} else if (currentsearchType.equalsIgnoreCase("resource")) {
						isResourceSearch = true;
						isCollectionSearch = false;
					}
					if (isResourceSearch) {
						if (floderDo.getType().equals("folder")) {
							String styleName = folderLevel == 1 ? AddResourceContainerCBundle.INSTANCE
									.css().parent()
									: AddResourceContainerCBundle.INSTANCE
											.css().child();
							FolderTreeItem innerFolderTreeItem = new FolderTreeItem(
									styleName, floderDo.getTitle(),
									floderDo.getGooruOid());
							innerFolderTreeItem.setFolerLevel(folderLevel + 1);
							TreeItem folderItem = new TreeItem(
									innerFolderTreeItem);
							item.addItem(folderItem);
							adjustTreeItemStyle(folderItem);
						} else if (floderDo.getType().equals("scollection")) {
							TreeItem folderItem = new TreeItem(
									new CollectionTreeItem(
											getTreeItemStyleName(folderLevel),
											floderDo.getTitle(),
											floderDo.getGooruOid()));
							item.addItem(folderItem);
							adjustTreeItemStyle(folderItem);
						}
					} else if (isCollectionSearch) {
						if (floderDo.getType().equals("folder")) {
							String styleName = folderLevel == 1 ? AddResourceContainerCBundle.INSTANCE
									.css().parent()
									: AddResourceContainerCBundle.INSTANCE
											.css().child();
							FolderTreeItem innerFolderTreeItem = new FolderTreeItem(
									styleName, floderDo.getTitle(),
									floderDo.getGooruOid());
							innerFolderTreeItem.setFolerLevel(folderLevel + 1);
							TreeItem folderItem = new TreeItem(
									innerFolderTreeItem);
							item.addItem(folderItem);
							adjustTreeItemStyle(folderItem);
						}
					}
				}
				item.setState(folderTreeItemWidget.isOpen());
			}
		}
	}

	private String getTreeItemStyleName(int folderLevel) {
		// TODO Auto-generated method stub
		if (folderLevel == 1) {
			return AddResourceContainerCBundle.INSTANCE.css().parent();
		} else if (folderLevel == 2) {
			return AddResourceContainerCBundle.INSTANCE.css().child();
		} else {
			return AddResourceContainerCBundle.INSTANCE.css().innerchild();
		}
	}

	private void adjustTreeItemStyle(final UIObject uiObject) {
		if (uiObject instanceof TreeItem) {
			if (uiObject != null && uiObject.getElement() != null) {
				Element element = uiObject.getElement();
				element.getStyle().setPadding(0, Unit.PX);
				element.getStyle().setMarginLeft(0, Unit.PX);
			}
		} else {
			if (uiObject != null
					&& uiObject.getElement() != null
					&& uiObject.getElement().getParentElement() != null
					&& uiObject.getElement().getParentElement()
							.getParentElement() != null
					&& uiObject.getElement().getParentElement()
							.getParentElement().getStyle() != null) {
				Element element = uiObject.getElement().getParentElement()
						.getParentElement();
				element.getStyle().setPadding(0, Unit.PX);
				element.getStyle().setMarginLeft(0, Unit.PX);
			}
		}

	}

	@Override
	public void displayNoCollectionsMsg() {

		dropdownListContainerScrollPanel.setVisible(false);
		buttonsContainer.setVisible(false);
		displayCountLabel.setVisible(true);
		if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.RESOURCE_SEARCH) || AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.COLLECTION_PLAY)){
			displayCountLabel.setText("There are no collections to add this resource.");
			addResourceText.setText(i18n.GL2088());
			addCollectiorOrReourceText.setText(i18n.GL2089());
			addResourceText.getElement().setAttribute("style",
					"display: inline-block;");
			addCollectiorOrReourceText.getElement().setAttribute("style",
					"display: inline-block;");
		}else{
			currentsearchType="collection";
			isTopMostSelected =true;
			buttonsContainer.setVisible(true);
			dropdownListContainerScrollPanel.setVisible(true);
			addResourceBtnLbl.setText(i18n.GL0590());
			folderTreePanel.clear();
			folderTreePanel.addItem(loadingTreeItemForNoFolder());
			//displayCountLabel.setText("There are no folders to add this collection.");
		}
	}

	public void resetEmptyCollMsg() {
		dropdownListContainerScrollPanel.setVisible(true);
		addResourceBtnLbl.setText(i18n.GL0590());
		buttonsContainer.setStyleName(AddResourceContainerCBundle.INSTANCE
				.css().assignmentButtonsContainer());
	}
	
	
	@UiHandler("addResourceBtnLbl")
	public void addResourceBtnLblClick(ClickEvent event) {
		
		addingText.setVisible(true);
		addResourceBtnLbl.setVisible(false);
		if (currentsearchType.equalsIgnoreCase("collection")) {
			isCollectionSearch = true;
			isResourceSearch = false;
		} else if (currentsearchType.equalsIgnoreCase("resource")) {
			isResourceSearch = true;
			isCollectionSearch = false;
		}
		if (isResourceSearch) {
			
			if (isSelectedCollection) {
				getUiHandlers().addResourceToCollection(
						selectedCollectionGooruOid, currentsearchType,
						cureentcollectionTreeItem.getCollectionName());
			} else if (isSelectedFolder) {
				displayErrorLabel.setText("Add me into a Collection");
				getButtonVisiblity();
			} else {
			
				if (!isSelectedCollection && !isSelectedFolder) {
					restrictionToAddResourcesData("please select a collection");
					getButtonVisiblity();
				}
			}
		} else if (isCollectionSearch) {
			if (isSelectedFolder) {
				getUiHandlers().addCollectionToFolder(selectedFolderGooruOid,currentsearchType,currentFolderSelectedTreeItem.floderName.getText(),currentFolderSelectedTreeItem.getFolerLevel(),this.urlparams);
				urlparams.clear();
			} 
			if(isTopMostSelected) {
				getUiHandlers().addCollectionToMyCollections("",currentsearchType);
			}
		}
	}

	public void setPagination(int count) {
		totalHitCount = count;
	}

	@Override
	public void setSearchResultDo(ResourceSearchResultDo searchResultDo) {
		// TODO Auto-generated method stub
		this.searchResultDo = searchResultDo;
	}

	@Override
	public void restrictionToAddResourcesData(String message) {
		// TODO Auto-generated method stub
		displayErrorLabel.setText(message);
	}

	@Override
	public Anchor getAddButton() {
		return addCollectiorOrReourceText;
	}

	@Override
	public Button getCancelButton() {
		return cancelButton;
	}

	@Override
	public Tree getfolderTreePanel() {
		return folderTreePanel;
	}

	public void setPlayerStyle(boolean isPlayer) {
		this.isPlayer = isPlayer;
		addContent.setStyleName(AddResourceContainerCBundle.INSTANCE.css()
				.addPlayerStyle());
		dropdownListContainerScrollPanel.getElement().setAttribute("style",
				"height: 135px !important;margin-left: 46px;overflow: auto;");
		floderTreeContainer.getElement().setAttribute("style",
				"height: 135px !important");
		cancelButton.setVisible(false);
		enableSuccessView.setVisible(false);
		buttonsContainer.setVisible(true);
	}

	@Override
	public void removePlayerStyle(boolean isPlayer) {
		this.isPlayer = isPlayer;
		addContent.removeStyleName(AddResourceContainerCBundle.INSTANCE.css()
				.addPlayerStyle());
		dropdownListContainerScrollPanel.getElement().setAttribute("style",
				"height: 275px !important;border: 1px solid #ddd;margin-left: 44px;overflow: auto;");
		floderTreeContainer.getElement().setAttribute("style",
				"height: 275px !important");
		cancelButton.setVisible(true);
	}

	@Override
	public void getButtonVisiblity() {
		addingText.setVisible(false);
		addResourceBtnLbl.setVisible(true);

	}

	@Override
	public void clearSelectedId() {
		selectedCollectionGooruOid = null;
		createCollectionbuttonsContainer.setVisible(true);
		dropdownListContainerScrollPanel.setVisible(true);
		buttonsContainer.setVisible(true);
		enableSuccessView.setVisible(false);
		displayErrorLabel.setText("");
	}

	@Override
	public void enableSuccessView(String title, String gooruOid,
			final HashMap<String, String> params) {
		dropdownListContainerScrollPanel.setVisible(false);
		createCollectionbuttonsContainer.setVisible(false);
		enableSuccessView.getElement().setAttribute("style", "width: 559px;");
		enableSuccessView.setVisible(true);
		buttonsContainer.setVisible(false);
		if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.RESOURCE_SEARCH)){
		headerTitle.setText("This resource has been added to  " + title);
		}else{
			headerTitle.setText("This collection has been added to  " + title);
		}
		cancelButton.setText("Continue searching");
		okButton.setText("View in My Collections");
		enableSuccessView.setVisible(true);
		// params.put("id", gooruOid);
		okButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				isSelectedCollection=false;
				AppClientFactory.getPlaceManager().revealPlace(
						PlaceTokens.SHELF, params);
				AppClientFactory.fireEvent(new CloseCollectionPlayerEvent(true));
			}
		});

	}

	@Override
	public void clearSelectedFolderId() {
		// TODO Auto-generated method stub
		selectedFolderGooruOid = null;
		createCollectionbuttonsContainer.setVisible(true);
		dropdownListContainerScrollPanel.setVisible(true);
		buttonsContainer.setVisible(true);
		enableSuccessView.setVisible(false);
		displayErrorLabel.setText("");
	}

	@Override
	public void setCollectionSearchResultDo(
			ResourceSearchResultDo searchResultDo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showAndHideMyCollections() {
		// TODO Auto-generated method stub
		if (AppClientFactory.getCurrentPlaceToken().equals(
				PlaceTokens.RESOURCE_SEARCH) || AppClientFactory.getCurrentPlaceToken().equals(
						PlaceTokens.COLLECTION_PLAY)) {
			//topMostTreeItem.getElement().getStyle().setDisplay(Display.NONE);
			isTopMostSelected =false;
		}else{
			topMostTreeItem.getElement().getStyle().setDisplay(Display.BLOCK);
			isTopMostSelected =true;
			if(isTopMostSelected){
				/*topMostTreeItem.getElement().setAttribute("style", "background-color: #cfe3f1;");*/
				isSelectedFolder=false;
			}else{
			//	topMostTreeItem.getElement().setAttribute("style", "background-color: none;");
			}
		}
	}

	@Override
	public void hideNoCollectionsMsg() {
		// TODO Auto-generated method stub
		displayCountLabel.setText("");
		buttonsContainer.setVisible(true);

		isSelectedFolder=false;
		isSelectedCollection =false;
	}

	


	

}
