package org.ednovo.gooru.client.mvp.shelf.collection.folders;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.effects.BackgroundColorEffect;
import org.ednovo.gooru.client.event.InvokeLoginEvent;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BaseViewWithHandlers;
import org.ednovo.gooru.client.mvp.folders.event.RefreshFolderType;
import org.ednovo.gooru.client.mvp.search.event.DisplayNoCollectionEvent;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderZIndexEvent;
import org.ednovo.gooru.client.mvp.shelf.FolderStyleBundle;
import org.ednovo.gooru.client.mvp.shelf.collection.folders.events.OpenParentFolderEvent;
import org.ednovo.gooru.client.mvp.shelf.collection.folders.events.RefreshFolderItemEvent;
import org.ednovo.gooru.client.mvp.shelf.collection.folders.events.ReorderShelfListItemsEvent;
import org.ednovo.gooru.client.mvp.shelf.collection.folders.item.ShelfFolderItemChildView;
import org.ednovo.gooru.client.mvp.shelf.collection.folders.uc.FolderDeleteView;
import org.ednovo.gooru.client.mvp.shelf.collection.folders.uc.FolderPopupUc;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.item.CollectionEditResourceCBundle;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.item.CollectionEditResourceCBundle.CollectionEditResourceCss;
import org.ednovo.gooru.client.uc.EditableLabelUc;
import org.ednovo.gooru.client.uc.HTMLEventPanel;
import org.ednovo.gooru.client.uc.tooltip.GlobalToolTip;
import org.ednovo.gooru.client.uc.tooltip.LibraryTopicCollectionToolTip;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.folder.FolderDo;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Search Team
 * 
 */
public class FolderItemTabView extends BaseViewWithHandlers<FolderItemTabUiHandlers> implements IsFolderItemTabView {

	@UiField(provided = true)
	EditableLabelUc organizeTitleLbl;
	
	@UiField HTMLPanel mainSection, panelTitleSection;
	@UiField VerticalPanel folderContentBlock;
	@UiField Label editFolderLbl, deleteFolderLbl, folderTitleErrorLbl, editMetaLbl;
	@UiField Button newCollectionBtn, newFolderBtn,assessmentButton;
	@UiField HTMLEventPanel editButtonEventPanel;
	@UiField FlowPanel folderContentPanel;
	@UiField Button editFolderSaveBtn,editFolderCancelBtn;
	@UiField FolderStyleBundle folderStyle;
	@UiField HTMLPanel loadingImage;
//	@UiField FolderItemPanelVc folderItemPanel;
	
	@UiField
	FolderItemMetaDataUc folderItemMetaDataUc;
	
	private  List<FolderDo> folderList = null;
	
	private PopupPanel toolTipPopupPanel=new PopupPanel();
	
	private String parentId, presentFolderId;
	
	private static final String O1_LEVEL = "o1";
	
	private static final String O2_LEVEL = "o2";
	
	private static final String O3_LEVEL = "o3";
	
	private boolean isRootFolder = true;
	
	private boolean isFolderType = false;
	
	private boolean isBadWord = false;
	
	private boolean isFolderPanelEmpty = false;
	
	private boolean isPaginated = false;
	
	private Integer pageNumber = 0;
	
	private String parentName = null;
	
	private int totalCount;
	
	private static final String UP_ARROW = "MoveUp";
	
	private static final String DOWN_ARROW = "MoveDown";
	
	private static final int EXTREME_TOP = 1;
	
	private static final String REORDER_VALIDATION_MSG = "Success";
	
	private String O1_LEVEL_VALUE = null, O2_LEVEL_VALUE = null, O3_LEVEL_VALUE = null;
	
	private CollectionEditResourceCss css;
	
	private ShelfFolderItemChildView shelfFolderItemChildView;
	
	private static FolderItemTabViewUiBinder uiBinder = GWT.create(FolderItemTabViewUiBinder.class);
	
	private MessageProperties i18n = GWT.create(MessageProperties.class);
	
	interface FolderItemTabViewUiBinder extends UiBinder<Widget, FolderItemTabView> {
	}

	/**
	 * Class constructor
	 */
	public FolderItemTabView() {
     	setFolderTitleValidations();
		setWidget(uiBinder.createAndBindUi(this));
		css = CollectionEditResourceCBundle.INSTANCE.css();
		setStaticMsgs();
		newFolderBtn.addClickHandler(new AddNewFolderClick());
		newFolderBtn.addMouseOverHandler(new NewFolderMouseOver());
		newFolderBtn.addMouseOutHandler(new NewFolderMouseOut());
		editFolderSaveBtn.setVisible(false);
		editFolderCancelBtn.setVisible(false);
		folderTitleErrorLbl.setVisible(false);	
		editFolderSaveBtn.setText(i18n.GL0141());
		editFolderSaveBtn.getElement().setAttribute("alt",i18n.GL0141());
		editFolderSaveBtn.getElement().setAttribute("title",i18n.GL0141());
		
		editFolderCancelBtn.setText(i18n.GL0142());
		editFolderCancelBtn.getElement().setAttribute("alt",i18n.GL0142());
		editFolderCancelBtn.getElement().setAttribute("title",i18n.GL0142());
		
		loadingImage.setVisible(true);
		folderContentBlock.setVisible(false);
		if (AppClientFactory.getLoggedInUser().getConfirmStatus() == 1){
			panelTitleSection.getElement().getStyle().clearPaddingTop();
		}else{
			panelTitleSection.getElement().getStyle().setPaddingTop(45, Unit.PX);
		}
		
		folderContentPanel.getElement().setId("fpnlFolderContentPanel");
		panelTitleSection.getElement().setId("pnlPanelTitleSection");
		organizeTitleLbl.getElement().setId("edlblOrganizeTitleLbl");
		editButtonEventPanel.getElement().setId("epnlEditButtonEventPanel");
		editFolderLbl.getElement().setId("lblEditFolderLbl");
		deleteFolderLbl.getElement().setId("lblDeleteFolderLbl");
		editMetaLbl.getElement().setId("lblEditMetaLbl");
		editFolderSaveBtn.getElement().setId("btnEditFolderSaveBtn");
		editFolderCancelBtn.getElement().setId("btnEditFolderCancelBtn");
		
		folderItemMetaDataUc.getElement().setId("folderItemMetaDataUc");
		mainSection.getElement().setId("pnlMainSection");
	}
	
	private void setFolderTitleValidations() {
		organizeTitleLbl = new EditableLabelUc(){

			@Override
			public void checkCharacterLimit(String text) {
				if (text.length() >= 50) {
					folderTitleErrorLbl.setText(i18n.GL0143());
					folderTitleErrorLbl.getElement().setAttribute("alt",i18n.GL0143());
					folderTitleErrorLbl.getElement().setAttribute("title",i18n.GL0143());
					folderTitleErrorLbl.getElement().getStyle().setFloat(Float.RIGHT);
					folderTitleErrorLbl.setVisible(true);
				}else{
					folderTitleErrorLbl.setVisible(false);
				}
			}

			@Override
			public void showProfanityError(boolean value) {
				if(value){
					if(editFolderSaveBtn.isVisible()){
						folderTitleErrorLbl.getElement().getStyle().setFloat(Float.RIGHT);
						folderTitleErrorLbl.setVisible(true);
						folderTitleErrorLbl.setText(i18n.GL0554());
						folderTitleErrorLbl.getElement().setAttribute("alt",i18n.GL0554());
						folderTitleErrorLbl.getElement().setAttribute("title",i18n.GL0554());
					}
				}else if(!folderTitleValidations()){
					folderTitleErrorLbl.setVisible(true);	
				}
				else{
					folderTitleErrorLbl.setVisible(false);	
				}
			}

			@Override
			public void onEditDisabled(String text) {
				editButtonEventPanel.setVisible(true);
				editFolderSaveBtn.setVisible(false);
				editFolderCancelBtn.setVisible(false);
			}

		};
		organizeTitleLbl.getElement().getStyle().setCursor(Cursor.DEFAULT);
	}

	/**
	 * @function setStaticMsgs 
	 * @created_date : 04-Feb-2014
	 * @description
	 * @parm(s) : 
	 * @return : void
	 * @throws : <Mentioned if any exceptions>
	*/
	private void setStaticMsgs() {
		organizeTitleLbl.setText(i18n.GL0180());
		organizeTitleLbl.getElement().setAttribute("alt",i18n.GL0180());
		organizeTitleLbl.getElement().setAttribute("title",i18n.GL0180());
		
		editFolderLbl.setText(i18n.GL1147());
		editFolderLbl.getElement().setAttribute("alt",i18n.GL1147());
		editFolderLbl.getElement().setAttribute("title",i18n.GL1147());
		
		deleteFolderLbl.setText(i18n.GL1148());
		deleteFolderLbl.getElement().setAttribute("alt",i18n.GL1148());
		deleteFolderLbl.getElement().setAttribute("title",i18n.GL1148());
		
		editMetaLbl.setText(i18n.GL1654());
		editMetaLbl.getElement().setAttribute("alt",i18n.GL1654());
		editMetaLbl.getElement().setAttribute("title",i18n.GL1654());
		
		newCollectionBtn.setText(i18n.GL1451());
		newCollectionBtn.getElement().setId("btnNewCollectionBtn");
		newCollectionBtn.getElement().setAttribute("alt",i18n.GL1451());
		newCollectionBtn.getElement().setAttribute("title",i18n.GL1451());
		
		assessmentButton.setText(i18n.GL3024());
		assessmentButton.getElement().setId("btnNewCollectionBtn");
		assessmentButton.getElement().setAttribute("alt",i18n.GL3024());
		assessmentButton.getElement().setAttribute("title",i18n.GL3024());
		
		newFolderBtn.setText(i18n.GL1450());
		newFolderBtn.getElement().setId("btnNewFolderBtn");
		newFolderBtn.getElement().setAttribute("alt",i18n.GL1450());
		newFolderBtn.getElement().setAttribute("title",i18n.GL1450());
		folderItemMetaDataUc.setVisible(false);
		loadingImage.getElement().setId("pnlLoadingImage");
		folderContentBlock.getElement().setId("vpnlFolderContentBlock");
	}
	
	public class AddNewFolderClick implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			if(!newFolderBtn.getStyleName().contains("disabled")){
				FolderPopupUc folderPopupUc = new FolderPopupUc("", isFolderType) {
					@Override
					public void onClickPositiveButton(ClickEvent event, String folderName, String parentId, HashMap<String,String> params) {
						if(!folderName.isEmpty()) {
							getUiHandlers().createFolderInParent(folderName, parentId);
//							Window.enableScrolling(true);
							this.hide();
						}
					}
				};
				folderPopupUc.setGlassEnabled(true);
				folderPopupUc.removeStyleName("gwt-PopupPanelGlass");
				if(isFolderType){
					folderPopupUc.getElement().setAttribute("style", "top:50px !important;");	
				}else{
					folderPopupUc.setPopupPosition(event.getRelativeElement().getAbsoluteLeft() - (464), Window.getScrollTop() + 233);
				}
				
				Window.enableScrolling(false);
				folderPopupUc.show();
			}
			
		}
	}
	
	
	@Override
	public void reset() {
		super.reset();
	}

	/**
	 * @function setFolderData 
	 * 
	 * @created_date : 04-Feb-2014
	 * 
	 * @description
	 * 
	 * @parm(s) : 
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	*/
	@Override
	public void setFolderData(List<FolderDo> folderList, String folderParentName, String presentFolderId,int totalCount) { 
		setFolderUrlParams();
		setTotalCount(totalCount);
		/*Label label = new Label("");
		label.setStyleName(getCss().shelfFoldereDragdropSpacer());
		folderItemPanel.superAdd(label);
		
		Label toplabel = new Label("");
		toplabel.setStyleName(getCss().shelfFoldereDragdropSpacer());
		folderItemPanel.add(toplabel);*/
		
		if(O1_LEVEL_VALUE==null&&O2_LEVEL_VALUE==null&&O3_LEVEL_VALUE==null) {
			isRootFolder = true;
			editButtonEventPanel.setVisible(false);
			editFolderLbl.addStyleName("disabled");
			deleteFolderLbl.addStyleName("disabled");
		} else {
			isRootFolder = false;
			editButtonEventPanel.setVisible(true);
			editFolderLbl.removeStyleName("disabled");
			deleteFolderLbl.removeStyleName("disabled");
		}
		
		this.folderList= folderList;
		this.presentFolderId= presentFolderId;
		if(isRootFolder) {
			organizeTitleLbl.removeStyleName(folderStyle.folder());
		} else {
			organizeTitleLbl.addStyleName(folderStyle.folder());
		}
		if(folderParentName!=null) {
			organizeTitleLbl.setText(folderParentName);
			organizeTitleLbl.getElement().setAttribute("alt",folderParentName);
			organizeTitleLbl.getElement().setAttribute("title",folderParentName);
		}
		mainSection.getElement().setAttribute("style", "min-height:"+(Window.getClientHeight()-100)+"px");
		
		if(folderList != null){
			
			if(folderList.size()==0&&!isPaginated){
				if(AppClientFactory.getPlaceManager().getRequestParameter(O1_LEVEL)==null){
					isFolderPanelEmpty = true;
					folderContentBlock.clear();
					isFolderType = false;

					mainSection.addStyleName(folderStyle.mainSection());
					mainSection.removeStyleName(folderStyle.emptyFolder());
					loadingImage.setVisible(false);

					Element ele = Document.get().getElementById("loadingImageLabel");
					if (ele!=null){
						ele.removeFromParent();
					}
					//				folderContentBlock.add(new FoldersWelcomePageToolTip());
					//				folderContentBlock.add(new FoldersWelcomePage());
					AppClientFactory.fireEvent(new DisplayNoCollectionEvent());

				}else{
					folderContentBlock.clear();
					isFolderType = false;
					mainSection.addStyleName(folderStyle.mainSection()); 
					mainSection.addStyleName(folderStyle.emptyFolder());
				}

			}
			else{
				mainSection.removeStyleName(folderStyle.emptyFolder());
				if(!isPaginated) {
					folderContentBlock.clear();
				}
				for(int i = 0; i<folderList.size(); i++) {
					shelfFolderItemChildView = new ShelfFolderItemChildView(folderList.get(i),i+1);
					shelfFolderItemChildView.setCollectionItemId(folderList.get(i).getCollectionItemId());
					shelfFolderItemChildView.setItemGooruOId(folderList.get(i).getGooruOid());
					shelfFolderItemChildView.setUpDownArrowVisibility(totalCount);
					shelfFolderItemChildView.getMoveUpBtn().addClickHandler(new OnClickReorderUpButton(folderList.get(i).getGooruOid())); 
					shelfFolderItemChildView.getMoveDownBtn().addClickHandler(new OnClickReorderDownButton(folderList.get(i).getGooruOid()));
					shelfFolderItemChildView.getMoveTopBtn().addClickHandler(new OnClickReorderTopButton(folderList.get(i).getGooruOid())); 
					shelfFolderItemChildView.getMoveBottomBtn().addClickHandler(new OnClickReorderBottomButton(folderList.get(i).getGooruOid()));
					shelfFolderItemChildView.getReorderTxtBox().addKeyPressHandler(new HasNumbersOnly()); 
					shelfFolderItemChildView.getReorderTxtBox().addKeyUpHandler(new ReorderText(folderList.get(i).getGooruOid())); 
					if(folderList.get(i).getType().equalsIgnoreCase("folder")){
						isFolderType = false;
					}
					
					folderContentBlock.add(shelfFolderItemChildView);
				}
				setFolderCollectionItemSequence();
			}
		}
		else{
			mainSection.removeStyleName(folderStyle.emptyFolder());
			if(!isPaginated) {
				folderContentBlock.clear();
			}
			for(int i = 0; i<folderList.size(); i++) {
				shelfFolderItemChildView = new ShelfFolderItemChildView(folderList.get(i),i+1);
				if(folderList.get(i).getType().equalsIgnoreCase("folder")){
					isFolderType = false;
				}
				folderContentBlock.add(shelfFolderItemChildView);
			}
		}
		loadingImage.setVisible(false);
		folderContentBlock.setVisible(true);
		if(O3_LEVEL_VALUE!=null){
			newFolderBtn.addStyleName("disabled");
		}else{
			newFolderBtn.removeStyleName("disabled");
		}
		if(folderList.size()==20) {
			isPaginated = true;
		} else {
			isPaginated = false;
		}
		setPaginatedResults();
	}
	
	/**
	 * Based on number of folders/collections, will be making visible reorder panel.
	 * 
	 * @param isEnable {@link Boolean}
	 * @param shelfFolderItemChildView {@link ShelfFolderItemChildView}
	 */
	private void isReorderButtonEnabled(boolean isEnable, ShelfFolderItemChildView shelfFolderItemChildView) { 
		if(isEnable){
			shelfFolderItemChildView.getReorderPanel().setVisible(isEnable);
		}else{
			shelfFolderItemChildView.getReorderPanel().setVisible(isEnable);
		}
	}

	public class NewFolderMouseOver implements MouseOverHandler{

		@Override
		public void onMouseOver(MouseOverEvent event) {
			if(newFolderBtn.getStyleName().contains("disabled")){
				toolTipPopupPanel.clear();
				toolTipPopupPanel.setWidget(new LibraryTopicCollectionToolTip(i18n.GL1178()));
				toolTipPopupPanel.setStyleName("");
				toolTipPopupPanel.setPopupPosition(event.getRelativeElement().getAbsoluteLeft() - 2, event.getRelativeElement().getAbsoluteTop() + 27);
				toolTipPopupPanel.show();
			}
		}
		
	}
	
	public class NewFolderMouseOut implements MouseOutHandler{

		@Override
		public void onMouseOut(MouseOutEvent event) {
			toolTipPopupPanel.hide();
		}
		
	}
	
	public class OnEmptyOrganizeNewCollMouseOver implements MouseOverHandler{

		@Override
		public void onMouseOver(MouseOverEvent event) {
			toolTipPopupPanel.clear();
			toolTipPopupPanel.setWidget(new GlobalToolTip(i18n.GL1308()));
			toolTipPopupPanel.setStyleName("");
			toolTipPopupPanel.setPopupPosition(event.getRelativeElement().getAbsoluteLeft() - 2, event.getRelativeElement().getAbsoluteTop() + 27);
			toolTipPopupPanel.show();
		}
		
	}
	
	public class OnEmptyOrganizeNewCollMouseOut implements MouseOutHandler{

		@Override
		public void onMouseOut(MouseOutEvent event) {
			toolTipPopupPanel.hide();
		}

		
		
	}
	
	/**
	 * @function deleteFolder
	 * 
	 * @created_date : 06-Feb-2014
	 * 
	 * @description
	 * 
	 * @parm(s) : 
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	*/
	@UiHandler("deleteFolderLbl")
	public void onclickOnDeleteFolder(ClickEvent event){
		if(!isRootFolder) {
			FolderDeleteView folderDeleteView=new FolderDeleteView();
			folderDeleteView.setGlassEnabled(true);
			folderDeleteView.setStyleName("folderDelete");
			folderDeleteView.setPopupPosition((Window.getClientWidth()-450)/2, Window.getScrollTop() + 182);

			Window.enableScrolling(false);
			folderDeleteView.show();
		}
	}
	
	@UiHandler("editFolderLbl")
	public void editFolderName(ClickEvent clickEvent){
		if(!isRootFolder) {
			editButtonEventPanel.setVisible(false);
			editFolderSaveBtn.setVisible(true);
			editFolderCancelBtn.setVisible(true);
			organizeTitleLbl.switchToEdit();
		}
	}
	
	@UiHandler("editFolderCancelBtn")
	public void clickCancelBtn(ClickEvent clickEvent){
		editButtonEventPanel.setVisible(true);
		folderTitleErrorLbl.setVisible(false);
		editFolderSaveBtn.setVisible(false);
		editFolderCancelBtn.setVisible(false);
		organizeTitleLbl.switchToCancelLabel();
		organizeTitleLbl.setText(organizeTitleLbl.getText());
		organizeTitleLbl.getElement().setAttribute("alt",organizeTitleLbl.getText());
		organizeTitleLbl.getElement().setAttribute("title",organizeTitleLbl.getText());
		organizeTitleLbl.getElement().getStyle().clearBackgroundColor();
		organizeTitleLbl.getElement().getStyle().setBorderColor("#ccc");
	}
	
	@UiHandler("editFolderSaveBtn")
	public void clickSaveBtn(ClickEvent clickEvent){
		if(folderTitleValidations()){
			Map<String, String> parms = new HashMap<String, String>();
			parms.put("text", organizeTitleLbl.getTextBoxSource().getText());
			AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {
				@Override
				public void onSuccess(Boolean result) {
					if (result){
						if(editFolderSaveBtn.isVisible()){
							organizeTitleLbl.getElement().getStyle().setBorderColor("orange");
							folderTitleErrorLbl.setText(i18n.GL0554());
							folderTitleErrorLbl.getElement().setAttribute("alt",i18n.GL0554());
							folderTitleErrorLbl.getElement().setAttribute("title",i18n.GL0554());
							folderTitleErrorLbl.setVisible(true);
							folderTitleErrorLbl.getElement().getStyle().setFloat(Float.RIGHT);
						}
					}else{
						editButtonEventPanel.setVisible(true);
						folderTitleErrorLbl.setVisible(false);
						editFolderSaveBtn.setVisible(false);
						editFolderCancelBtn.setVisible(false);
						presentFolderId = presentFolderId!=null ? presentFolderId : " " ;
						getUiHandlers().updateCollectionInfo(presentFolderId, organizeTitleLbl.getTextBoxSource().getText(), null);
						organizeTitleLbl.getElement().getStyle().clearBackgroundColor();
						organizeTitleLbl.getElement().getStyle().setBorderColor("#ccc");
						organizeTitleLbl.switchToLabel();
					}
				}
				
			});
		}
		
	}
	
	private boolean folderTitleValidations() {
		String title=organizeTitleLbl.getTextBoxSource().getText().trim();
		if(title==null || title.equals("")){
			folderTitleErrorLbl.setText(i18n.GL0173());
			folderTitleErrorLbl.getElement().setAttribute("alt",i18n.GL0173());
			folderTitleErrorLbl.getElement().setAttribute("title",i18n.GL0173());
			folderTitleErrorLbl.setVisible(true);
			organizeTitleLbl.getElement().setAttribute("style", "border-color:#fab03a");
			return false;
		}else{
			organizeTitleLbl.getElement().getStyle().setBorderColor("#ccc");
			return true;
		}
		
	}

	@Override
	public void setParentId(String parentId) {
		this.parentId=parentId;
	}
	
	@Override
	public void addFolderItem(FolderDo folderDo, String parentId, HashMap<String,String> urlParams) {
		if(isFolderPanelEmpty==true) {
			folderContentBlock.clear();
			isFolderPanelEmpty=false;
		}
		folderDo.setType("folder");
		setFolderUrlParams();
		HashMap<String,String> params = new HashMap<String,String>();
		if(O3_LEVEL_VALUE!=null) {
			params.put(O3_LEVEL, O3_LEVEL_VALUE);
		}
		if(O2_LEVEL_VALUE!=null) {
			params.put(O2_LEVEL, O2_LEVEL_VALUE);
		}
		if(O1_LEVEL_VALUE!=null) {
			params.put(O1_LEVEL, O1_LEVEL_VALUE);
		}
		
		AppClientFactory.fireEvent(new RefreshFolderItemEvent(folderDo, RefreshFolderType.INSERT, urlParams));
		AppClientFactory.fireEvent(new OpenParentFolderEvent());
		if(urlParams!=null) { 	
			if((O3_LEVEL_VALUE!=null&&O3_LEVEL_VALUE.equalsIgnoreCase(urlParams.get(O3_LEVEL))&&urlParams.size()==3) || (O2_LEVEL_VALUE!=null&&O2_LEVEL_VALUE.equalsIgnoreCase(urlParams.get(O2_LEVEL))&&urlParams.size()==2) || (O1_LEVEL_VALUE!=null&&O1_LEVEL_VALUE.equalsIgnoreCase(urlParams.get(O1_LEVEL)))&&urlParams.size()==1) {
				addFolder(folderDo);
			} else if (urlParams.get(O3_LEVEL)==null&&urlParams.get(O1_LEVEL)==null&&urlParams.get(O2_LEVEL)==null&&O3_LEVEL_VALUE==null&&O2_LEVEL_VALUE==null&&O1_LEVEL_VALUE==null) {
				addFolder(folderDo);
			}
			mainSection.getElement().setAttribute("style", "min-height:"+(Window.getClientHeight()-100)+"px");
		} else {
			if((O3_LEVEL_VALUE!=null&&O3_LEVEL_VALUE.equalsIgnoreCase(parentId))) {
				addFolder(folderDo);
			} else if((O2_LEVEL_VALUE!=null&&O2_LEVEL_VALUE.equalsIgnoreCase(parentId))) {
				MixpanelUtil.mixpanelEvent("Organize_create_subsubfolder");
				addFolder(folderDo);
			} else if ((O1_LEVEL_VALUE!=null&&O1_LEVEL_VALUE.equalsIgnoreCase(parentId))) {
				MixpanelUtil.mixpanelEvent("Organize_create_subfolder");
				addFolder(folderDo);
			} else if (O1_LEVEL_VALUE==null&&O2_LEVEL_VALUE==null&O3_LEVEL_VALUE==null) {
				MixpanelUtil.mixpanelEvent("Organize_create_folder");
				addFolder(folderDo);
			}
		}
	}
	
	private void addFolder(FolderDo folderDo) {
		shelfFolderItemChildView = new ShelfFolderItemChildView(folderDo,1);
		shelfFolderItemChildView.setCollectionItemId(folderDo.getCollectionItemId());
		shelfFolderItemChildView.setItemGooruOId(folderDo.getGooruOid());
		shelfFolderItemChildView.getMoveUpBtn().addClickHandler(new OnClickReorderUpButton(folderDo.getGooruOid())); 
		shelfFolderItemChildView.getMoveDownBtn().addClickHandler(new OnClickReorderDownButton(folderDo.getGooruOid()));
		shelfFolderItemChildView.getMoveTopBtn().addClickHandler(new OnClickReorderTopButton(folderDo.getGooruOid())); 
		shelfFolderItemChildView.getMoveBottomBtn().addClickHandler(new OnClickReorderBottomButton(folderDo.getGooruOid()));
		shelfFolderItemChildView.getReorderTxtBox().addKeyPressHandler(new HasNumbersOnly()); 
		shelfFolderItemChildView.getReorderTxtBox().addKeyUpHandler(new ReorderText(folderDo.getGooruOid())); 
		shelfFolderItemChildView.upButtonIsVisible(false);
		folderContentBlock.insert(shelfFolderItemChildView, 0); 
		setTotalCount(getTotalCount()+1);
		setFolderCollectionItemSequence();
		mainSection.removeStyleName(folderStyle.emptyFolder());
	}
	
	
	@UiHandler("newCollectionBtn")
	public void onClickNewCollectionBtn(ClickEvent clickEvent){
		if (AppClientFactory.getLoggedInUser().getUserUid().equals(AppClientFactory.GOORU_ANONYMOUS)) {
			AppClientFactory.fireEvent(new InvokeLoginEvent());
		} else {
			setFolderUrlParams();
			Map<String, String> params = new HashMap<String, String>();
			if(O3_LEVEL_VALUE!=null) {
				params.put(O1_LEVEL, O1_LEVEL_VALUE);
				params.put(O2_LEVEL, O2_LEVEL_VALUE);
				params.put(O3_LEVEL, O3_LEVEL_VALUE);
			} else if(O2_LEVEL_VALUE!=null) {
				params.put(O1_LEVEL, O1_LEVEL_VALUE);
				params.put(O2_LEVEL, O2_LEVEL_VALUE);
			} else if(O1_LEVEL_VALUE!=null){
				params.put(O1_LEVEL, O1_LEVEL_VALUE);
			}
				params.put("folderId", presentFolderId);
				Window.enableScrolling(false);
				AppClientFactory.fireEvent(new SetHeaderZIndexEvent(99, false));
				AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.COLLECTION,params);
				
			}
		}
	
	@UiHandler("assessmentButton")
	public void onClickNewAssessmentBtn(ClickEvent clickEvent){
		Window.open(AppClientFactory.loggedInUser.getSettings().getAssessementEndPoint()+PlaceTokens.CREATE_ASSIGNMENT, "_blank", "");
//		if (AppClientFactory.getLoggedInUser().getUserUid().equals(AppClientFactory.GOORU_ANONYMOUS)) {
//			AppClientFactory.fireEvent(new InvokeLoginEvent());
//		} else {
//			setFolderUrlParams();
//			Map<String, String> params = new HashMap<String, String>();
//			if(O3_LEVEL_VALUE!=null) {
//				params.put(O1_LEVEL, O1_LEVEL_VALUE);
//				params.put(O2_LEVEL, O2_LEVEL_VALUE);
//				params.put(O3_LEVEL, O3_LEVEL_VALUE);
//			} else if(O2_LEVEL_VALUE!=null) {
//				params.put(O1_LEVEL, O1_LEVEL_VALUE);
//				params.put(O2_LEVEL, O2_LEVEL_VALUE);
//			} else if(O1_LEVEL_VALUE!=null){
//				params.put(O1_LEVEL, O1_LEVEL_VALUE);
//			}
//				params.put("folderId", presentFolderId);
//				params.put("type", "assessment");
//				Window.enableScrolling(false);
//				AppClientFactory.fireEvent(new SetHeaderZIndexEvent(99, false));
//				AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.COLLECTION,params);
//				
//			}
		}

	@Override
	public void setFolderTitle(String title) {
		organizeTitleLbl.setText(title);
		organizeTitleLbl.getElement().setAttribute("alt",title);
		organizeTitleLbl.getElement().setAttribute("title",title);
	}
	
	public void setPaginatedResults() {
		Timer timer = new Timer(){
            @Override
            public void run()
            {
            	if(isPaginated) {
            		getUiHandlers().setFolderData(parentId, parentName, getPageNumber());
            	}
            }
        };
        timer.schedule(3000);
	}

	public Integer getPageNumber() {
		return pageNumber;
	}
	
	@Override
	public void setPageDetails(Integer pageNumber, String parentId, String parentName) {
		this.pageNumber = pageNumber;
		this.parentId = parentId;
		this.parentName = parentName;
	}
	
	private void setFolderUrlParams() {
		O1_LEVEL_VALUE = AppClientFactory.getPlaceManager().getRequestParameter(O1_LEVEL);
		O2_LEVEL_VALUE = AppClientFactory.getPlaceManager().getRequestParameter(O2_LEVEL);
		O3_LEVEL_VALUE = AppClientFactory.getPlaceManager().getRequestParameter(O3_LEVEL);
		displayMetaDataContainer();
	}
	
	private void displayMetaDataContainer() {
		Map<String, String> map = StringUtil.splitQuery(Window.Location.getHref());
		if(map.size()>0) {
			folderItemMetaDataUc.setVisible(true);
		} else {
			folderItemMetaDataUc.setVisible(false);
		}
	}
	
	@UiHandler("editMetaLbl")
	public void editMetaData(ClickEvent event) {
		folderItemMetaDataUc.updateFolderData(presentFolderId, organizeTitleLbl.getText());
		folderItemMetaDataUc.beforeEditSetOpen();
		folderItemMetaDataUc.showEditableMetaData(false);
	}

	@Override
	public void setFolderMetaData(Map<String, String> folderMetaData) {
		String ideas = folderMetaData.get("ideas")!=null?folderMetaData.get("ideas"):"";
		String questions = folderMetaData.get("questions")!=null?folderMetaData.get("questions"):"";
		String performanceTasks = folderMetaData.get("performanceTasks")!=null?folderMetaData.get("performanceTasks"):"";
		folderItemMetaDataUc.setMetaData(ideas, questions, performanceTasks);
		folderItemMetaDataUc.showEditableMetaData(true);
	}
	
	
	/**
	 * Updates the item sequence and reorder buttons of all folders
	 * and collection, as new folder or collection created or reordered.
	 */
	private void setFolderCollectionItemSequence() { 
		Iterator<Widget> widgets = folderContentBlock.iterator();
		int seqNum=1;
		while (widgets.hasNext()) {
			Widget widget = widgets.next();
			if (widget instanceof ShelfFolderItemChildView) {
				
				if(getTotalCount()==1){
					isReorderButtonEnabled(false,(ShelfFolderItemChildView) widget);
					
				}else{
					isReorderButtonEnabled(true,(ShelfFolderItemChildView) widget);
				}
				
				/**
				 * For a first folder/collection hiding the up and top most arrow.
				 */
				if(seqNum==1){
					((ShelfFolderItemChildView) widget).upButtonIsVisible(false); 
					((ShelfFolderItemChildView) widget).downButtonIsVisible(true); 
				}else{
					/**
					 * If user moved folder to last position, based on total count down arrow will be invisible and 
					 * vice versa in case of reordering last folder or collection to the first position, up arrow should be in visible.
					 */
					if(seqNum==getTotalCount()){
						((ShelfFolderItemChildView) widget).upButtonIsVisible(true); 
						((ShelfFolderItemChildView) widget).downButtonIsVisible(false);  
					}else{
						((ShelfFolderItemChildView) widget).upButtonIsVisible(true); 
						((ShelfFolderItemChildView) widget).downButtonIsVisible(true); 
					}
				}
				
				((ShelfFolderItemChildView) widget).getItemNumber().setText(seqNum+"");
				((ShelfFolderItemChildView) widget).getReorderTxtBox().setText(seqNum+"");
			}
			seqNum++;
		}
	}
	
	
	/**
	 * This inner class used for disabling up and down arrow based on user entered reorder value.
	 *
	 */

	public class ReorderText implements KeyUpHandler {
		String itemGooruOid;
		int itemPosSeqNumb,itemToBeMovedPosSeqNumb;
		
		public ReorderText(String itemGooruOid) {
			this.itemGooruOid=itemGooruOid;
		}

		@Override
		public void onKeyUp(KeyUpEvent event) {

			ShelfFolderItemChildView shelfFolderItemChildView = getFolderOrCollectionWidget(itemGooruOid);

			itemPosSeqNumb = shelfFolderItemChildView != null ?(Integer.parseInt(shelfFolderItemChildView.getItemNumber().getText().trim())):0;
			itemToBeMovedPosSeqNumb = (shelfFolderItemChildView != null && shelfFolderItemChildView.getReorderTxtBox().getText().trim() !=null && !shelfFolderItemChildView.getReorderTxtBox().getText().trim().equals(""))?(Integer.parseInt(shelfFolderItemChildView.getReorderTxtBox().getText().trim())):0;

			if(itemToBeMovedPosSeqNumb==0 && itemPosSeqNumb!=1 && itemPosSeqNumb!=getTotalCount()){
				shelfFolderItemChildView.downButtonIsVisible(true);
				shelfFolderItemChildView.upButtonIsVisible(true);
			}else if(itemToBeMovedPosSeqNumb>itemPosSeqNumb && itemPosSeqNumb!=getTotalCount()){
				//disable up arrow
				shelfFolderItemChildView.upButtonIsVisible(false); 
				shelfFolderItemChildView.downButtonIsVisible(true); 
			}else if(itemToBeMovedPosSeqNumb<itemPosSeqNumb && itemPosSeqNumb!=1){
				//disable down arrow
				shelfFolderItemChildView.downButtonIsVisible(false);
				shelfFolderItemChildView.upButtonIsVisible(true);
			}
		}
	}
	
	
	/**
	 * This inner class used for to restrict text box values to have only numbers
	 *
	 */

	public class HasNumbersOnly implements KeyPressHandler {

		@Override
		public void onKeyPress(KeyPressEvent event) {

			if (!Character.isDigit(event.getCharCode()) 
					&& event.getNativeEvent().getKeyCode() != KeyCodes.KEY_TAB 
					&& event.getNativeEvent().getKeyCode() != KeyCodes.KEY_BACKSPACE
					&& event.getNativeEvent().getKeyCode() != KeyCodes.KEY_SHIFT
					&& event.getNativeEvent().getKeyCode() != KeyCodes.KEY_ENTER
					&& event.getNativeEvent().getKeyCode() != KeyCodes.KEY_LEFT
					&& event.getNativeEvent().getKeyCode() != KeyCodes.KEY_RIGHT
					&& event.getNativeEvent().getKeyCode() != KeyCodes.KEY_DELETE){
				((TextBox) event.getSource()).cancelKey();
			}
			if (event.getNativeEvent().getKeyCode() == 46
					|| event.getNativeEvent().getKeyCode() == 37) {
				((TextBox) event.getSource()).cancelKey();
			}
		}
	}
	
	/**
	 * 
	 * Inner class for reorder Extreme top button, which implements click handler {@link ClickHandler}
	 *
	 */
	
	public class OnClickReorderTopButton implements ClickHandler{
		private String itemGooruOid;
		int itemPosSeqNumb,itemToBeMovedPosSeqNumb,itemSeqToAPI;

		/**
		 * Class constructor
		 * @param itemGooruOid {@link String}
		 */
		public OnClickReorderTopButton(String itemGooruOid) {
			this.itemGooruOid = itemGooruOid;
		}

		@Override
		public void onClick(ClickEvent event) {
			
			ShelfFolderItemChildView shelfFolderItemChildView = getFolderOrCollectionWidget(itemGooruOid);
			
			itemPosSeqNumb = shelfFolderItemChildView != null ?(Integer.parseInt(shelfFolderItemChildView.getItemNumber().getText().trim())):0;
			itemToBeMovedPosSeqNumb = EXTREME_TOP;
			
			if(shelfFolderItemChildView!=null){
				itemSeqToAPI= getMoveUpItemSeq(itemPosSeqNumb,itemToBeMovedPosSeqNumb);
				getUiHandlers().reorderFoldersOrCollection(shelfFolderItemChildView,itemToBeMovedPosSeqNumb,itemPosSeqNumb,UP_ARROW,shelfFolderItemChildView.getCollectionItemId(),itemSeqToAPI);
			}
			
		}
	}
	
	/**
	 * 
	 * Inner class for reorder Up button, which implements click handler {@link ClickHandler}
	 *
	 */
	
	public class OnClickReorderUpButton implements ClickHandler{
		private String itemGooruOid;
		int itemPosSeqNumb,itemToBeMovedPosSeqNumb,itemSeqToAPI;
		private String reorderValidationMsg;
		/**
		 * Class constructor
		 * @param itemGooruOid {@link String}
		 */
		public OnClickReorderUpButton(String itemGooruOid) {
			this.itemGooruOid = itemGooruOid;
		}

		@Override
		public void onClick(ClickEvent event) {
			
			ShelfFolderItemChildView shelfFolderItemChildView = getFolderOrCollectionWidget(itemGooruOid);
			
			itemPosSeqNumb = shelfFolderItemChildView != null ?(Integer.parseInt(shelfFolderItemChildView.getItemNumber().getText().trim())):0;
			itemToBeMovedPosSeqNumb = (shelfFolderItemChildView != null && shelfFolderItemChildView.getReorderTxtBox().getText().trim() !=null && !shelfFolderItemChildView.getReorderTxtBox().getText().trim().equals(""))?(Integer.parseInt(shelfFolderItemChildView.getReorderTxtBox().getText().trim())):0;
			
			if(shelfFolderItemChildView!=null){
				reorderValidationMsg = reorderValidations(itemToBeMovedPosSeqNumb,itemPosSeqNumb,UP_ARROW);
				if(reorderValidationMsg.equalsIgnoreCase(REORDER_VALIDATION_MSG)){
					
					if(itemToBeMovedPosSeqNumb==itemPosSeqNumb){
						itemToBeMovedPosSeqNumb-=1;
					}
					
					itemSeqToAPI= getMoveUpItemSeq(itemPosSeqNumb,itemToBeMovedPosSeqNumb);
					getUiHandlers().reorderFoldersOrCollection(shelfFolderItemChildView,itemToBeMovedPosSeqNumb,itemPosSeqNumb,UP_ARROW,shelfFolderItemChildView.getCollectionItemId(),itemSeqToAPI);
					/*reorderItemToNewPosition(shelfFolderItemChildView,(itemToBeMovedPosSeqNumb-1),UP_ARROW);*/
				}else{
					shelfFolderItemChildView.showReorderValidationToolTip(reorderValidationMsg);
				}
			}
			
		}
	}
	
	/**
	 * 
	 * Inner class for reorder down button, which implements click handler {@link ClickHandler}
	 *
	 */
	
	public class OnClickReorderDownButton implements ClickHandler{
		
		private String itemGooruOid;
		int itemPosSeqNumb,itemToBeMovedPosSeqNumb,itemSeqToAPI;
		private String reorderValidationMsg;
		
		
		/**
		 * Class constructor
		 * @param itemGooruOid {@link String}
		 */
		public OnClickReorderDownButton(String itemGooruOid) {
			this.itemGooruOid = itemGooruOid;
		}

		@Override
		public void onClick(ClickEvent event) {
			
			ShelfFolderItemChildView shelfFolderItemChildView = getFolderOrCollectionWidget(itemGooruOid);

			itemPosSeqNumb = shelfFolderItemChildView != null ?(Integer.parseInt(shelfFolderItemChildView.getItemNumber().getText().trim())):0;
			itemToBeMovedPosSeqNumb = shelfFolderItemChildView != null && shelfFolderItemChildView.getReorderTxtBox().getText().trim() !=null && !shelfFolderItemChildView.getReorderTxtBox().getText().trim().equals("")?(Integer.parseInt(shelfFolderItemChildView.getReorderTxtBox().getText().trim())):0;
			if(shelfFolderItemChildView!=null){
				reorderValidationMsg = reorderValidations(itemToBeMovedPosSeqNumb,itemPosSeqNumb,DOWN_ARROW);
				
				if(reorderValidationMsg.equalsIgnoreCase(REORDER_VALIDATION_MSG)){
					if(itemToBeMovedPosSeqNumb==itemPosSeqNumb){
						itemToBeMovedPosSeqNumb+=1;
					}
					
					itemSeqToAPI= getMoveDownItemSeq(itemPosSeqNumb,itemToBeMovedPosSeqNumb);
					getUiHandlers().reorderFoldersOrCollection(shelfFolderItemChildView,itemToBeMovedPosSeqNumb,itemPosSeqNumb,DOWN_ARROW,shelfFolderItemChildView.getCollectionItemId(),itemSeqToAPI);
				}else{
					shelfFolderItemChildView.showReorderValidationToolTip(reorderValidationMsg);
				}
			}
		}
	}
	
	
	/**
	 * 
	 * Inner class for reorder extreme bottom button, which implements click handler {@link ClickHandler}
	 *
	 */
	
	public class OnClickReorderBottomButton implements ClickHandler{
		
		private String itemGooruOid;
		int itemPosSeqNumb,itemToBeMovedPosSeqNumb,itemSeqToAPI;
		
		
		/**
		 * Class constructor
		 * @param itemGooruOid {@link String}
		 */
		public OnClickReorderBottomButton(String itemGooruOid) {
			this.itemGooruOid = itemGooruOid;
		}

		@Override
		public void onClick(ClickEvent event) {
			
			ShelfFolderItemChildView shelfFolderItemChildView = getFolderOrCollectionWidget(itemGooruOid);

			itemPosSeqNumb = shelfFolderItemChildView != null ?(Integer.parseInt(shelfFolderItemChildView.getItemNumber().getText().trim())):0;
			itemToBeMovedPosSeqNumb = getTotalCount();
			if(shelfFolderItemChildView!=null){
				itemSeqToAPI= getMoveDownItemSeq(itemPosSeqNumb,itemToBeMovedPosSeqNumb);
				getUiHandlers().reorderFoldersOrCollection(shelfFolderItemChildView,itemToBeMovedPosSeqNumb,itemPosSeqNumb,DOWN_ARROW,shelfFolderItemChildView.getCollectionItemId(),itemSeqToAPI);
			}
		}
	}
	

	/**
	 * Before reorder will return with valid message.
	 * @param itemToBeMovedPosSeqNumb {@link Integer}
	 * @param itemPosSeqNumb {@link Integer}
	 * @param arrow {@link String}
	 * @return validationStaus {@link String} 
	 */
	public String reorderValidations(int itemToBeMovedPosSeqNumb,int itemPosSeqNumb,String arrow) {
		String validationStaus=REORDER_VALIDATION_MSG; 
		if(itemToBeMovedPosSeqNumb==0){
			validationStaus = i18n.GL3003();
		}else if(itemToBeMovedPosSeqNumb>getTotalCount()){
			validationStaus = StringUtil.generateMessage(i18n.GL3004(),itemToBeMovedPosSeqNumb+"");
		}else if(itemToBeMovedPosSeqNumb>itemPosSeqNumb && arrow.equalsIgnoreCase(UP_ARROW)){
			validationStaus = "Please click on down arrow";
		}else if(itemToBeMovedPosSeqNumb<itemPosSeqNumb && arrow.equalsIgnoreCase(DOWN_ARROW)){
			validationStaus = "Please click on Up arrow";
		}else if(itemToBeMovedPosSeqNumb==0){
			validationStaus = "Please specify the reorder sequence.";
		}
		return validationStaus;
	}
	
	
	/**
	 * Calculation of an item sequence to pass for an API, while moving up the order.
	 * @param itemPosSeqNumb {@link Integer}
	 * @param itemToBeMovedPosSeqNumb {@link Integer}
	 * @return moveUpItemSeq
	 */
	public int getMoveUpItemSeq(int itemPosSeqNumb,int itemToBeMovedPosSeqNumb) {
		int moveUpItemSeq=0;
		/**
		 * itemSeq = totcount - ((currentPosition+givenNewPosition) - (1 + currentPosition))
		 */
		moveUpItemSeq = getTotalCount() - (Math.abs((itemPosSeqNumb + itemToBeMovedPosSeqNumb))-(Math.abs((1+itemPosSeqNumb)))); 
		
		return Math.abs(moveUpItemSeq);
	}
	
	
	
	/**
	 * Calculation of an item sequence to pass for an API, while moving down the order.
	 * @param itemPosSeqNumb {@link Integer}
	 * @param itemToBeMovedPosSeqNumb {@link Integer}
	 * @return moveDownitemSeq
	 */
	public int getMoveDownItemSeq(int itemPosSeqNumb,int itemToBeMovedPosSeqNumb) {
		int moveDownItemSeq=0;
		/**
		 * itemSeq = totcount - ((currentPosition-givenNewPosition) + (1 - currentPosition))
		 */
		moveDownItemSeq = getTotalCount() -(Math.abs((itemPosSeqNumb - itemToBeMovedPosSeqNumb))+(Math.abs((1-itemPosSeqNumb)))); 
		return Math.abs(moveDownItemSeq);
	}
	
	

	/**
	 * Gets the respective folder or collection widget for reorder.
	 * @param itemGooruOid {@link String}
	 * 
	 * @return widget {@link ShelfFolderItemChildView}
	 */
	public ShelfFolderItemChildView getFolderOrCollectionWidget(String itemGooruOid) { 
		Iterator<Widget> widgets = folderContentBlock.iterator();
		while (widgets.hasNext()) {
			Widget widget = widgets.next();
			if (widget instanceof ShelfFolderItemChildView && ((ShelfFolderItemChildView) widget).getItemGooruOId().equals(itemGooruOid)) {
				return (ShelfFolderItemChildView) widget;
			}
		}
		return null;
	}
	
	/**
	 * On reorder API call success, following method will trigger to change the widget position accordingly.
	 * 
	 * @param shelfFolderItemChildView {@link ShelfFolderItemChildView}
	 * @param itemToBeMovedPosSeqNumb {@link Integer}
	 * @param itemPosSeqNumb {@link Integer}
	 * @param direction {@link String}
	 */
	
	@Override
	public void onReorderChangeWidgetPosition(ShelfFolderItemChildView shelfFolderItemChildView,int itemToBeMovedPosSeqNumb,int itemPosSeqNumb, String direction) {
		
		
		
		String id = AppClientFactory.getPlaceManager().getRequestParameter("id",null);
		String o1 = AppClientFactory.getPlaceManager().getRequestParameter("o1",null);
		String o2 = AppClientFactory.getPlaceManager().getRequestParameter("o2",null);
		String o3 = AppClientFactory.getPlaceManager().getRequestParameter("o3",null);
		HashMap<String,String> params = new HashMap<String,String>();
		
		
		if(o3!=null&&id==null) {
			params.put("o3",o3);
			params.put("o2",o2);
			params.put("o1",o1);
		} else if(o2!=null&&id==null) {
			params.put("o2",o2);
			params.put("o1",o1);
		} else if(o1!=null&&id==null) {
			params.put("o1",o1);
		}
		
		if(direction.equalsIgnoreCase(DOWN_ARROW)){
			reorderItemToNewPosition(shelfFolderItemChildView,(itemToBeMovedPosSeqNumb),DOWN_ARROW,params);
		}else{
			reorderItemToNewPosition(shelfFolderItemChildView,(itemToBeMovedPosSeqNumb-1),UP_ARROW,params);
		}
		
	}
	

	/**
	 * Reorders the position of folder or collection widget to the new position.
	 * @param shelfFolderItemChildView {@link ShelfFolderItemChildView}
	 * @param newItemPosition {@link Integer}
	 * @param params 
	 */
	public void reorderItemToNewPosition(ShelfFolderItemChildView shelfFolderItemChildView, int newItemPosition,String direction, HashMap<String, String> params){
		folderContentBlock.insert(shelfFolderItemChildView, newItemPosition);
		if(shelfFolderItemChildView.toolTipPosPopupPanel!=null && shelfFolderItemChildView.toolTipPosPopupPanel.isVisible()){
			shelfFolderItemChildView.toolTipPosPopupPanel.hide(); 
		}
		Document.get().getElementById("pnlEditPanel").setScrollTop(0 + (newItemPosition)*(shelfFolderItemChildView.getOffsetHeight()-23));
		new BackgroundColorEffect(shelfFolderItemChildView.getElement(),"#E7F1F8" ,"white", 4000);
		setFolderCollectionItemSequence();
		AppClientFactory.fireEvent(new ReorderShelfListItemsEvent(shelfFolderItemChildView.getItemGooruOId(), newItemPosition, direction, params, shelfFolderItemChildView.getFolderDo(),shelfFolderItemChildView.getItemNumber().getText()));
	}
	
	
	
	/**
	 * @return css instance of {@link CollectionEditResourceCss}
	 */
	public CollectionEditResourceCss getCss() {
		return css;
	}


	/**
	 * @param css
	 *            name the styles to set
	 */
	public void setCss(CollectionEditResourceCss css) {
		this.css = css;
	}
	
	/**
	 * @return the totalCount
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * @param totalCount the totalCount to set
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}