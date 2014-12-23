package org.ednovo.gooru.client.mvp.shelf.collection.folders.uc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.shelf.ShelfCBundle;
import org.ednovo.gooru.client.mvp.shelf.ShelfPresenter;
import org.ednovo.gooru.client.mvp.shelf.list.TreeMenuImages;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.folder.FolderDo;
import org.ednovo.gooru.shared.model.folder.FolderListDo;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;


public abstract class FolderPopupUc extends PopupPanel {
	
	@UiField HTMLPanel folderStructureTree, buttonsContainer;
	@UiField Button cancelBtn, okBtn;
	@UiField FolderPopupStyleBundle folderPopupStyle;
	@UiField TextBox folderTitle;
	@UiField Label validationTitleLbl, addingLbl, popupHeaderTitleLbl,inputTitleLbl,inputDescLbl;
	
	private Tree folderTreePanel = new Tree(new TreeMenuImages());
	TreeItem treeChildSelectedItem = new TreeItem();
	TreeItem previousTreeChildSelectedItem = new TreeItem();
	
	String selectedParentItem = "";
	
	String destinationFolderName = "";
	
	String selectedSourceItem = "";
	
	@UiField HTMLPanel loadingImageLabel;
	
	private boolean isCollectionMove = false;
	
	private static final String COLLECTION_MOVE = "collectionMove";
	
	private static final String O1_LEVEL = "o1";
	
	private static final String O2_LEVEL = "o2";
	
	private static final String O3_LEVEL = "o3";

	private HashMap<String,String> params = new HashMap<String,String>();
	
	private boolean isPaginated = false;
	
	private Integer pageNumber = 0;
	
	private String selectedGooruOid = "";
	
	private String type="";
	
	private String FOLDER = "folder";
	
	private static FolderPopupUcUiBinder uiBinder = GWT.create(FolderPopupUcUiBinder.class);
	
	private MessageProperties i18n = GWT.create(MessageProperties.class);

	interface FolderPopupUcUiBinder extends UiBinder<Widget, FolderPopupUc> {}

	public FolderPopupUc(final String moveType, boolean isFolderType) { 
		setWidget(uiBinder.createAndBindUi(this));
		setStaticText(moveType);
		setPageNumber(0);
		setId();
		getFolderData(moveType);
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

		    @Override
		    public void execute() {
		        folderTitle.setFocus(true);
		    }
		});
		
		if(isFolderType || moveType.equalsIgnoreCase(COLLECTION_MOVE)) {
			loadingImageLabel.setVisible(true);
		} else {
			loadingImageLabel.setVisible(false);
		}
		modifyPopup(isFolderType, moveType);
		folderTreePanel.getElement().removeAttribute("style");
		folderStructureTree.add(folderTreePanel);
		folderTitle.getElement().setAttribute("maxlength", "50");
		folderTitle.getElement().setAttribute("placeholder", i18n.GL1250());
		StringUtil.setAttributes(folderTitle, true);
		validationTitleLbl.getElement().getStyle().setDisplay(Display.NONE);
		validationTitleLbl.getElement().getStyle().setTextAlign(TextAlign.RIGHT);
		addingLbl.setVisible(false);
		folderTitle.addKeyUpHandler(new TitleKeyUpHandler());
		cancelBtn.addClickHandler(new AddCancelClick());
		folderTreePanel.addSelectionHandler(new SelectionHandler<TreeItem>() {
			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				int folderNavigation = 3;
				if(moveType.equalsIgnoreCase(COLLECTION_MOVE)){
					folderNavigation=4;
				}
				treeChildSelectedItem = event.getSelectedItem();
				openMatchedFolder(folderNavigation, false);
			}
		});
		folderTitle.addBlurHandler(new CheckProfanityForFolders());
	}
	
	public void setCollectionType(String collectionType){
		if(collectionType!=null&&collectionType.equals(ShelfPresenter.ASSESSMENT)){
			addAttributesToWidget(inputDescLbl,i18n.GL3036());
			addAttributesToWidget(popupHeaderTitleLbl,i18n.GL3037());
		}else{
			addAttributesToWidget(inputDescLbl,i18n.GL1364());
			addAttributesToWidget(popupHeaderTitleLbl,i18n.GL1264());
		}
	}
	public void addAttributesToWidget(Label labelText, String text){
		labelText.setText(text);
		labelText.getElement().setAttribute("alt",text);
		labelText.getElement().setAttribute("title",text);
	}
	private void modifyPopup(boolean isFolderType, String moveType) {
		if(isFolderType){
			inputDescLbl.setVisible(true);
		
			folderStructureTree.setVisible(true);
			
			if(!moveType.equalsIgnoreCase(COLLECTION_MOVE)) {
				inputDescLbl.setText(i18n.GL1329());
				inputDescLbl.getElement().setAttribute("alt",i18n.GL1329());
				inputDescLbl.getElement().setAttribute("title",i18n.GL1329());
			}
		}else{
			inputDescLbl.setVisible(false);
			folderStructureTree.setVisible(false);
			setTokenParameter();
		}
	}
	
	private void setStaticText(String moveType) {
		if(moveType.equalsIgnoreCase(COLLECTION_MOVE)){ 
			isCollectionMove = true;
			inputTitleLbl.getElement().getStyle().setDisplay(Display.NONE);
			folderTitle.getElement().getStyle().setDisplay(Display.NONE);
			
			addingLbl.setText(i18n.GL1362());
			addingLbl.getElement().setAttribute("alt",i18n.GL1362());
			addingLbl.getElement().setAttribute("title",i18n.GL1362());
			
			okBtn.setText(i18n.GL1261());
			okBtn.getElement().setAttribute("alt",i18n.GL1261());
			okBtn.getElement().setAttribute("title",i18n.GL1261());
			
			cancelBtn.setText(i18n.GL0142());
			cancelBtn.getElement().setAttribute("alt",i18n.GL0142());
			cancelBtn.getElement().setAttribute("title",i18n.GL0142());
			if(AppClientFactory.getPlaceManager().getRequestParameter(O1_LEVEL)==null){
				okBtn.getElement().removeClassName("enabled");
				okBtn.getElement().addClassName("disabled");
				okBtn.setEnabled(false);
			}else{
				okBtn.getElement().addClassName("enabled");
				okBtn.getElement().removeClassName("disabled");
				okBtn.setEnabled(true);
			}
		}else{
			isCollectionMove = false;
			popupHeaderTitleLbl.setText(i18n.GL1266);
			popupHeaderTitleLbl.getElement().setAttribute("alt",i18n.GL1266);
			popupHeaderTitleLbl.getElement().setAttribute("title",i18n.GL1266);
			
			inputTitleLbl.setText(i18n.GL1262());
			inputTitleLbl.getElement().setAttribute("alt",i18n.GL1262());
			inputTitleLbl.getElement().setAttribute("title",i18n.GL1262());
			
			inputDescLbl.setText(i18n.GL1263());
			inputDescLbl.getElement().setAttribute("alt",i18n.GL1263());
			inputDescLbl.getElement().setAttribute("title",i18n.GL1263());
			
			addingLbl.setText(i18n.GL0591().toLowerCase());
			addingLbl.getElement().setAttribute("alt",i18n.GL0591().toLowerCase());
			addingLbl.getElement().setAttribute("title",i18n.GL0591().toLowerCase());
			
			okBtn.setText(i18n.GL0190());
			okBtn.getElement().setAttribute("alt",i18n.GL0190());
			okBtn.getElement().setAttribute("title",i18n.GL0190());
			
			cancelBtn.setText(i18n.GL0142());
			cancelBtn.getElement().setAttribute("alt",i18n.GL0142());
			cancelBtn.getElement().setAttribute("title",i18n.GL0142());
		}
	}

	private void openFolders(int level, boolean isRefresh) {
		String o1 = AppClientFactory.getPlaceManager().getRequestParameter(O1_LEVEL);
		String o2 = AppClientFactory.getPlaceManager().getRequestParameter(O2_LEVEL);
		String o3 = AppClientFactory.getPlaceManager().getRequestParameter(O3_LEVEL);
		
		if(level==1&&o1!=null) {
			selectedGooruOid = o1;
			iterateTopLevelFolders(o1, level, isRefresh);
		} else if(level==2&&o2!=null) {
			selectedGooruOid = o2;
			getSecondLevelFolderItems(o2, level, isRefresh);
		} else if(level==3&&o3!=null) {
			selectedGooruOid = o3;
			getSecondLevelFolderItems(o3, level, isRefresh);
		}
	}
	
	private void iterateTopLevelFolders(String gooruOid, int level, boolean isRefresh){
		for(int i = 0; i < folderTreePanel.getItemCount(); i++) { 
			TreeItem item = folderTreePanel.getItem(i);
			getTopLevelFolders(item, gooruOid, level, isRefresh); 
		}
	}

	private void getTopLevelFolders(TreeItem item, String gooruOid, int level, boolean isRefresh) { 
		FolderPopupChildItem updatedItem = (FolderPopupChildItem) item.getWidget();
		if(gooruOid.equalsIgnoreCase(updatedItem.getGooruOid())) {
			treeChildSelectedItem = item;
			openMatchedFolder(level, isRefresh);
			return;
		}
	}

	private void getSecondLevelFolderItems(String gooruOid, int level, boolean isRefresh) {
		for(int i=0;i<treeChildSelectedItem.getChildCount();i++){
			TreeItem item = treeChildSelectedItem.getChild(i);
			getTopLevelFolders(item, gooruOid, level, isRefresh); 
		}
	}

	private void openMatchedFolder(int folderNavigation, boolean isRefresh) { 
		FolderPopupChildItem folderPopupChildItem = (FolderPopupChildItem) treeChildSelectedItem.getWidget();
		TreeItem parent = treeChildSelectedItem.getParentItem();
        treeChildSelectedItem.getTree().setSelectedItem(parent, false);
        if(parent != null)parent.setSelected(false);
        treeChildSelectedItem.setState(treeChildSelectedItem.getState(), false);
		if(folderPopupChildItem.getGooruOid()!=null) {
			if(selectedGooruOid.equals(folderPopupChildItem.getGooruOid())){
				okBtn.getElement().removeClassName("enabled");
				okBtn.getElement().addClassName("disabled");
				okBtn.setEnabled(false);
			} else {
				okBtn.getElement().addClassName("enabled");
				okBtn.getElement().removeClassName("disabled");
				okBtn.setEnabled(true);
			}
			destinationFolderName=folderPopupChildItem.getSelectedFolderTitle();
		}else{
			selectedParentItem="";
			params=folderPopupChildItem.getUrlParams();
			destinationFolderName=i18n.GL0180();
			if(AppClientFactory.getPlaceManager().getRequestParameter(O1_LEVEL)==null&& isCollectionMove){
				okBtn.getElement().removeClassName("enabled");
				okBtn.getElement().addClassName("disabled");
				okBtn.setEnabled(false);
			}else{
				okBtn.getElement().addClassName("enabled");
				okBtn.getElement().removeClassName("disabled");
				okBtn.setEnabled(true);
			}
			
		}
		if(folderPopupChildItem.getLevel()!=folderNavigation || isRefresh) {
			if(folderPopupChildItem.getLevel()!=0) {
				getCollectionItems(folderPopupChildItem.getGooruOid(), folderPopupChildItem.getCollectionOpenedStatus(), folderNavigation, isRefresh);
			}
			FolderPopupChildItem previousFolderPopupChildItem = (FolderPopupChildItem) previousTreeChildSelectedItem.getWidget();
			if(previousFolderPopupChildItem!=null&&previousFolderPopupChildItem.getFolderLevel()!=null) {
				previousFolderPopupChildItem.setSelectedStyle(false);
			}
			folderPopupChildItem.setSelectedStyle(true);
			previousTreeChildSelectedItem = treeChildSelectedItem;
		}
		
	}


	public class AddCancelClick implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			Window.enableScrolling(true);
			hide();
		}
	}
	
	private void setData(List<FolderDo> folders, String moveType) {
		if(getPageNumber()==0) {
			FolderDo organizeDo = new FolderDo();
			organizeDo.setTitle(i18n.GL1752());
			setOrganizeStaticData(organizeDo, 0, 0, 0);
		}
		
		int folderCount = 0;
		if(folders.size()>0) {
			for (FolderDo folderDo : folders) {
				if(folderDo.getType().equals(FOLDER)) {
					setOrganizeStaticData(folderDo, folderCount, 1, 1);
					folderCount++;
				}
			}
		}
		if(folderCount==0) {
			displayDefaultImage(moveType);
		}else{
			okBtn.getElement().addClassName("enabled");
			okBtn.setEnabled(true);
		}
		if(folders.size()==20) {
			setPageNumber(getPageNumber()+1);
			isPaginated = true;
		} else {
			loadingImageLabel.setVisible(false);
			if(moveType.equalsIgnoreCase(COLLECTION_MOVE)) {
				openFolders(1, true);
			}
			isPaginated = false;
		}
		setPaginatedResults(null, moveType);
	}
	
	private void setOrganizeStaticData(FolderDo folderDo, int folderCount, int position, int folderLevel) {
		FolderPopupChildItem folderPopupChildItem = new FolderPopupChildItem(folderDo, folderLevel){
			@Override
			public void onSetFolderItemData(HashMap<String,String> urlParams, String parentId, String folderTitle) {
				if(parentId!=null) {
					selectedParentItem = parentId;
				}
				params = urlParams;
			}
		};
		HashMap<String,String> params = new HashMap<String,String>();
		TreeItem item = new TreeItem(folderPopupChildItem);
		folderTreePanel.addItem(item);
		if(folderLevel==0) {
			previousTreeChildSelectedItem = item;
		} else {
			params.put(O1_LEVEL, folderDo.getGooruOid());
			folderPopupChildItem.setWidgetPositions(position, folderCount, params);
		}
		correctStyle(item);
	}

	private void getFolderData(final String moveType) {
		AppClientFactory.getInjector().getResourceService().getFolderWorkspace((getPageNumber()-1)*20, 20,null, FOLDER, new SimpleAsyncCallback<FolderListDo>() {
			@Override
			public void onSuccess(FolderListDo result) {
				setData(result.getSearchResult(), moveType);
			}
		});
	}
	
	private class TitleKeyUpHandler implements KeyUpHandler {
		
		public void onKeyUp(KeyUpEvent event) {
			folderTitle.removeStyleName(ShelfCBundle.INSTANCE.css().folderBorderColor());
			if (folderTitle.getText().length() >= 50) {
				validationTitleLbl.setText(i18n.GL0143());
				validationTitleLbl.getElement().setAttribute("alt",i18n.GL0143());
				validationTitleLbl.getElement().setAttribute("title",i18n.GL0143());
				validationTitleLbl.getElement().getStyle().setDisplay(Display.BLOCK);
				folderTitle.addStyleName(ShelfCBundle.INSTANCE.css().folderBorderColor());
				folderTitle.getElement().setAttribute("style", "border-color:#fab03a !important");	
			}else if(folderTitle.getText().equalsIgnoreCase("") && validationTitleLbl.isVisible()){
				validationTitleLbl.setText(i18n.GL0173());
				validationTitleLbl.getElement().setAttribute("alt",i18n.GL0173());
				validationTitleLbl.getElement().setAttribute("title",i18n.GL0173());
				folderTitle.getElement().setAttribute("style", "border-color:#fab03a !important");
			}else{
				validationTitleLbl.getElement().getStyle().setDisplay(Display.NONE);
				folderTitle.getElement().setAttribute("style", "border-color:#E3E3E3 !important");	
			}
		}
	}
	
	private class CheckProfanityForFolders implements BlurHandler{

		@Override
		public void onBlur(BlurEvent event) {
			Map<String, String> parms = new HashMap<String, String>();
			if(folderTitle!=null){
				parms.put("text", folderTitle.getValue());
			}
			AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {

				@Override
				public void onSuccess(Boolean result) {
					if (result){
						folderTitle.getElement().setAttribute("style", "border-color:#fab03a !important");	
						validationTitleLbl.setText(i18n.GL0554());
						validationTitleLbl.getElement().setAttribute("alt",i18n.GL0554());
						validationTitleLbl.getElement().setAttribute("title",i18n.GL0554());
						validationTitleLbl.getElement().getStyle().setDisplay(Display.BLOCK);
//						validationTitleLbl.setStyleName(CollectionCBundle.INSTANCE.css().mandatoryLabelError());
					}else{
						folderTitle.getElement().getStyle().clearBackgroundColor();
						folderTitle.getElement().setAttribute("style", "border-color:#E3E3E3");
//						folderTitle.getElement().setAttribute("style", "border-color:#ccc !important");	
						if(validationTitleLbl.getText().equalsIgnoreCase(i18n.GL0554()))
							validationTitleLbl.getElement().getStyle().setDisplay(Display.NONE);
					}
//					SetStyleForProfanity.SetStyleForProfanityForTextBox(folderTitle, validationTitleLbl, result);
				}
				
			});
		}
	}
	
	public void getCollectionItems(String collectionOid,boolean collectionOpenedStatus, final int folderNavigation, final boolean isRefresh) {
		if(collectionOpenedStatus) {
			setChildFolderItems(null, 0, false);
		} else {
			AppClientFactory.getInjector().getfolderService().getChildFolders(0, 20, collectionOid,null, FOLDER, new SimpleAsyncCallback<FolderListDo>() {
				@Override
				public void onSuccess(FolderListDo result) {
					setChildFolderItems(result.getSearchResult(), folderNavigation, isRefresh);
				}
			});
		}
	}
	
	public void setChildFolderItems(List<FolderDo> folderListDo, int folderNavigation, boolean isRefresh) {
		FolderPopupChildItem folderPopupChildItem = (FolderPopupChildItem) treeChildSelectedItem.getWidget();
		if(folderListDo!=null) {
			folderPopupChildItem.setCollectionOpenedStatus(true);
			int nextLevel = 1;
			if(folderPopupChildItem.getLevel()==1) {
				nextLevel = 2;
			} else if (folderPopupChildItem.getLevel()==2) {
				nextLevel = 3;
			} else if (folderPopupChildItem.getLevel()==3) {
				folderPopupChildItem.setLevel(folderPopupChildItem.getUrlParams());
				params=folderPopupChildItem.getUrlParams();
				/** As Level three folders will not be having folders as child, because of this only for third level selected parent item is assigned here. **/
				selectedParentItem=params.get(O3_LEVEL);
			}
			
			for(int i=0;i<folderListDo.size();i++) {
				if(folderListDo.get(i).getType().equals(FOLDER)) {
					FolderPopupChildItem shelfCollection = new FolderPopupChildItem(folderListDo.get(i), nextLevel){
						@Override
						public void onSetFolderItemData(HashMap<String,String> urlParams, String parentId, String folderTitle) {
							if(parentId!=null) {
								selectedParentItem = parentId;
							}
							params = urlParams;
						}
					};
					shelfCollection.setWidgetPositions(nextLevel, i, folderPopupChildItem.getUrlParams());
					TreeItem item = new TreeItem(shelfCollection);
					treeChildSelectedItem.addItem(item);
					correctStyle(item);
				}
			}
		}
		if(treeChildSelectedItem.getState()) {
			treeChildSelectedItem.setState(false);
			folderPopupChildItem.setFolderStyle(false);
		} else {
			treeChildSelectedItem.setState(true);
			folderPopupChildItem.setFolderStyle(true);
		}
		if(isRefresh) {
			openFolders(folderNavigation+1, isRefresh);
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
   	
	@UiHandler("okBtn")
	public void onPositiveClickEvent(final ClickEvent event){
		if(isCollectionMove){
			showAddingMsg(false);
			onClickPositiveButton(event, destinationFolderName, selectedParentItem, params);
		}else{
			if(folderTitleValidations()){
				showAddingMsg(false);
				Map<String, String> parms = new HashMap<String, String>();
				parms.put("text", folderTitle.getValue());
				AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {

					@Override
					public void onSuccess(Boolean result) {
						if(result){
							showAddingMsg(true);
//							SetStyleForProfanity.SetStyleForProfanityForTextBox(folderTitle, validationTitleLbl, result);
							folderTitle.getElement().setAttribute("style", "border-color:#fab03a !important");	
							validationTitleLbl.setText(i18n.GL0554());
							validationTitleLbl.getElement().setAttribute("alt",i18n.GL0554());
							validationTitleLbl.getElement().setAttribute("title",i18n.GL0554());
							validationTitleLbl.getElement().getStyle().setDisplay(Display.BLOCK);
						}else{
							showAddingMsg(false);
							folderTitle.getElement().getStyle().clearBackgroundColor();
							folderTitle.getElement().setAttribute("style", "border-color:#E3E3E3");
							if(validationTitleLbl.getText().equalsIgnoreCase(i18n.GL0554()))
							validationTitleLbl.getElement().getStyle().setDisplay(Display.NONE);
							onClickPositiveButton(event, folderTitle.getText(), selectedParentItem, params);
						}
					}
				});
			}
		}
	}
	
	private boolean folderTitleValidations() {
		String title=folderTitle.getText().trim();
		if(title==null || title.equals("")){
			validationTitleLbl.setText(i18n.GL0173());
			validationTitleLbl.getElement().setAttribute("alt",i18n.GL0173());
			validationTitleLbl.getElement().setAttribute("title",i18n.GL0173());
			validationTitleLbl.getElement().getStyle().setDisplay(Display.BLOCK);
			folderTitle.addStyleName(ShelfCBundle.INSTANCE.css().folderBorderColor());
			folderTitle.getElement().setAttribute("style", "border-color:#fab03a !important");
			return false;
		}else{
			folderTitle.getElement().getStyle().setBorderColor("#ccc");
			return true;
		}
		
	}

	private void setTokenParameter() {
		String o1 = AppClientFactory.getPlaceManager().getRequestParameter("o1");
		String o2 = AppClientFactory.getPlaceManager().getRequestParameter("o2");
		String o3 = AppClientFactory.getPlaceManager().getRequestParameter("o3");
		
		if(o3!=null) {
			selectedParentItem = o3;
		} else if(o2!=null) {
			selectedParentItem = o2;
		} else if(o1!=null) {
			selectedParentItem = o1;
		}
	}
	
	public abstract void onClickPositiveButton(ClickEvent event, String folderTitle, String parentId, HashMap<String,String> params);
	
	private void showAddingMsg(boolean isVisible) {
		addingLbl.setVisible(!isVisible);
		buttonsContainer.setVisible(isVisible);
	}
	
	private void displayDefaultImage(String moveType) {
		if(moveType.equalsIgnoreCase(COLLECTION_MOVE)) {
			folderStructureTree.addStyleName(folderPopupStyle.emptyCollection());
			okBtn.setEnabled(false);
			okBtn.getElement().addClassName("disabled");
		} else {
			okBtn.setEnabled(true);
			okBtn.getElement().addClassName("enabled");
			//folderStructureTree.addStyleName(folderPopupStyle.emptyFolder());
		}
	}

	public void setPaginatedResults(final String parentId, final String moveType) {
		Timer timer = new Timer(){
            @Override
            public void run()
            {
            	if(isPaginated) {
            		if(parentId==null) {
            			getFolderData(moveType);
            		} else {
            			
            		}
            	}
            }
        };
        timer.schedule(3000);
	}
	
	public Integer getPageNumber() {
		return pageNumber;
	}
	
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	public void setId(){
		popupHeaderTitleLbl.getElement().setId("lblPopupHeaderTitleLbl");
		inputTitleLbl.getElement().setId("lblInputTitleLbl");
		folderTitle.getElement().setId("txtFolderTitle");
		validationTitleLbl.getElement().setId("lblValidationTitleLbl");
		inputDescLbl.getElement().setId("lblInputDescLbl");
		loadingImageLabel.getElement().setId("pnlLoadingImageLabel");
		folderStructureTree.getElement().setId("pnlFolderStructureTree");
		addingLbl.getElement().setId("lblAddingLbl");
		buttonsContainer.getElement().setId("pnlButtonsContainer");
		cancelBtn.getElement().setId("btnCancelBtn");
		okBtn.getElement().setId("btnOkBtn");
	}
}