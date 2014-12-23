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
package org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.child.ChildView;
import org.ednovo.gooru.client.effects.BackgroundColorEffect;
import org.ednovo.gooru.client.event.InvokeLoginEvent;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.addTagesPopup.AddTagesPopupView;
import org.ednovo.gooru.client.mvp.dnd.IsDraggableMirage;
import org.ednovo.gooru.client.mvp.resource.dnd.ResourceDragUc;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderZIndexEvent;
import org.ednovo.gooru.client.mvp.shelf.ShelfPresenter;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.collaborators.vc.SuccessPopupViewVc;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.IsCollectionResourceTabView;
import org.ednovo.gooru.client.mvp.shelf.event.InsertCollectionItemInAddResourceEvent;
import org.ednovo.gooru.client.mvp.shelf.event.PublishButtonHideEvent;
import org.ednovo.gooru.client.mvp.shelf.event.PublishButtonHideEventHandler;
import org.ednovo.gooru.client.mvp.shelf.event.RefreshCollectionItemInShelfListEvent;
import org.ednovo.gooru.client.mvp.shelf.event.RefreshType;
import org.ednovo.gooru.client.mvp.shelf.event.ReorderCollectionResourcesEvent;
import org.ednovo.gooru.client.uc.ConfirmationPopupVc;
import org.ednovo.gooru.client.uc.ResourceImageUc;
import org.ednovo.gooru.client.uc.tooltip.GlobalToolTip;
import org.ednovo.gooru.client.uc.tooltip.ToolTip;
import org.ednovo.gooru.client.util.ImageUtil;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.content.AssetsDo;
import org.ednovo.gooru.shared.model.content.CollectionDo;
import org.ednovo.gooru.shared.model.content.CollectionItemDo;
import org.ednovo.gooru.shared.model.content.ThumbnailDo;
import org.ednovo.gooru.shared.model.folder.FolderListDo;
import org.ednovo.gooru.shared.util.InfoUtil;
import org.ednovo.gooru.shared.util.ResourceImageUtil;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;


/**
 * @author Search Team
 * 
 */
public class ShelfCollectionResourceChildView extends 
		ChildView<ShelfCollectionResourceChildPresenter> implements
		IsShelfCollectionResourceView {

	@UiField
	HTML resourceNarrationHtml;
	
	@UiField HTMLPanel resourceTitle1,resourceTitleContainer,minsText,secondsText,endMinsText,endSecondsText;

	@UiField
	Label  confirmDeleteLbl, fromLbl,updatePDFLabelText,startStopTimeDisplayText,editSartPageText,StartPageLbl,errorMsgLabelForPDF;
	
	@UiField
	HTML resourceTitleLbl;

	@UiField
	ResourceImageUc resourceImageUc;

	/*@UiField
	VerticalPanel actionVerPanel;
*/
	@UiField
	Button EditBtn,updateNarrationBtn,cancelNarrationBtn,updateVideoTimeBtn,cancelVideoTimeBtn,updatePdfBtn,cancelpdfBtn/*,resourceMoveUpBtn,resourceMoveDownBtn (hotfix changes)*/;

	@UiField
	FlowPanel editFloPanel, editFieldsFloPanel,actionVerPanel,actionVerPanelForUpdateTime;

	@UiField
	TextBox fromTxt, toTxt,EndTimeTxt1,EndTimeTxt2,startpdfPageNumber,stoppdfPageNumber/*,reorderTxtBox (hotfix changes)*/;

	@UiField
	TextArea narrationTxtArea;
	
	/*@UiField
	TinyMCE narrationTxtArea;*/

	@UiField
	FlowPanel narationFloPanel,resourceFlowPanel,ResourceEditButtonContainer,videoDisplay,narrationConatainer,videoImage,editPdfFlowPanel,actionVerPanelForUpdatePDF;

	@UiField
	Label pencilEditNarationLbl,updateResourceBtn,addTages,endPageLbl;

	@UiField
	Label narrationAlertMessageLbl,videoTimeField,fromLblDisplayText,ToLbl,UpdateTextMessage,editStartPageLbl,editVideoTimeLbl,errorMsgLabel, lblCharLimit;

	@UiField Image imgNotFriendly;
	
	/* hotfix changes
	 * @UiField HTMLPanel reorderContainer;*/
	
	ToolTip toolTip = null;
	
	@UiField(provided = true)
	CollectionEditResourceCBundle res;

//	IsCollectionResourceTabView isCollResourceTabView = null;

	private CollectionItemDo collectionItemDo;
	
	private static MessageProperties i18n = GWT.create(MessageProperties.class);

	List<Integer> collectionItems;
	
	Map<String, Integer> collectionListCount = new HashMap<String, Integer>();
	
	private int totalSize;
	
	private Integer pageNumber=1;
	
	private Integer pageSize=20;
	
	private PopupPanel toolTipPopupPanel=new PopupPanel(true);
	
	private boolean isReorderContainerVisible=false;
	
	private GlobalToolTip globalToolTip;
	
	
	
	//private static final String REG_EXP = "^(?:[01]\\d|2[0-3]):(?:[0-9]\\d):(?:[0-5]\\d)$";
	
	private static final String REG_EXP = "^(?:[01]\\d|2[0-3]):(?:[0-9]{0,6}):(?:[0-5]\\d)$";

	private static final String START_STOP_MINUTE = "00";
	private static final String START_STOP_SEC = "00";

	private int totalVideoLength;
	String narrationData;
	int narrationDataLength;
	@UiField
	Label copyResource, editInfoLbl;
	AddTagesPopupView popup;
	public Label getEditInfoLbl() {
		return editInfoLbl;
	}
	public Button getEditButton() {
		return EditBtn;
	}
	public FlowPanel getResourceEditButtonContainer()
	{
		return ResourceEditButtonContainer;
	}
	public void setEditInfoLbl(Label editInfoLbl) {
		this.editInfoLbl = editInfoLbl;
	}
	/*public void setResourceEditButtonContainer(FlowPanel ResourceEditButtonContainer)
	{
		this.ResourceEditButtonContainer=ResourceEditButtonContainer;
	}*/
	private static final String NO_NARRATION_ADDED =i18n.GL0956();

	private static final String ADD_NARRATION_FOR_YOUR_VIEWERS =i18n.GL0967();

	private ConfirmationPopupVc deleteConfirmationPopupVc;

	private CopyConfirmPopupVc copyConfirmPopupVc;

	private EditQuestionPopupVc editQuestionPopupVc;

	private IsCollectionResourceTabView collectionResourceTabView;

	private static ShelfCollectionResourceChildView previousCollectionResourceChildView;

	private static final String MESSAGE_CONTENT =i18n.GL0968();

	private static final String MESSAGE_HEADER =i18n.GL0748();

	private static final String PLAYER_NAME =i18n.GL0969();

	private static final String VALID_START_STOP_TIME = i18n.GL0970();

	private static final String YOUTUBE_START_END_TIME = i18n.GL0971();
	
	private static final String VALID_END_PAGE = i18n.GL2025();
	
	private static final String FROM_START_TIME =i18n.GL0972();
	
	private static final String FROM_STOP_TIME = i18n.GL0973();
//	private static final String FROM_START_PAGE = "Start page";
	private static final String VIDEO_TIME =i18n.GL0974();
	//private static final String START_TIME=i18n.GL0972+i18n.GL_SPL_SEMICOLON;
	
	private static final String START_PAGE=i18n.GL0961();
	private static boolean isConfirmationPopup;
	private static final String START_MINUTE="00";
	private static final String START_SEC="00";
	private static final String END_MINUTE="00";
	private static final String END_SEC="00";
	
	private static final String DEFAULT_START_PAGE="1";

	private static boolean isEdited = false;

	private static final String OOPS = i18n.GL0061();

	private static final String EDIT_CONFIRM =i18n.GL0975();
	
	private  String collectionType=null;

	private String selectedCollectionId;
	private boolean youtube;

	String mediaType = "";
	boolean setVisibility = false;
	
	boolean isHavingBadWords=false;
	
	Integer totalPages;
	
	private static ShelfCollectionResourceChildViewUiBinder uiBinder = GWT
			.create(ShelfCollectionResourceChildViewUiBinder.class);

	interface ShelfCollectionResourceChildViewUiBinder extends
			UiBinder<Widget, ShelfCollectionResourceChildView> {
	}

	public static void disableAllEditMode() {
		
		if (previousCollectionResourceChildView != null
				&& previousCollectionResourceChildView.isAttached()) {
			previousCollectionResourceChildView.setEditMode(false);
			previousCollectionResourceChildView = null;
		}
	}

	/**
	 * To alert user to save/update the content before moving to next action.
	 * 
	 * 
	 */
	public static void checkEditState() {

		if (isEdited) {
			if (previousCollectionResourceChildView != null	&& previousCollectionResourceChildView.isAttached()) {
				if (!isConfirmationPopup) {
					isConfirmationPopup = true;
					new ConfirmationPopupVc(OOPS, EDIT_CONFIRM) {

						@Override
						@UiHandler("okButton")
						public void onDelete(ClickEvent clickEvent) {
							hide();
							isConfirmationPopup = false;
							clickEvent.stopPropagation();
						}

						@Override
						@UiHandler("cancelButton")
						public void onCancelClick(ClickEvent clickEvent) {
							disableAllEditMode();
							hide();
							isEdited = false;
							isConfirmationPopup = false;
							clickEvent.stopPropagation();
						}
					};
				}
			}
		} else {
			disableAllEditMode();
		}
	}

	public boolean checkLoggedInUser() {
		boolean isValid = true;
		if (AppClientFactory.getLoggedInUser().getGooruUId().equalsIgnoreCase(
				collectionItemDo.getResource().getUser().getGooruUId())) {
			isValid = true;
		} else {
			isValid = false;
		}

		return isValid;
	}

	/**
	 * Class constructor
	 * 
	 * @param collectionResourceTabView
	 * @param collectionItem
	 *            instance of {@link CollectionItemDo}
	 */
	public ShelfCollectionResourceChildView(
			IsCollectionResourceTabView collectionResourceTabView,
			CollectionItemDo collectionItem,String collectionType) {
		this.collectionResourceTabView = collectionResourceTabView;
		this.collectionType=collectionType;
		res = CollectionEditResourceCBundle.INSTANCE;
		CollectionEditResourceCBundle.INSTANCE.css().ensureInjected();
		initWidget(uiBinder.createAndBindUi(this));
		this.collectionItemDo = collectionItem;
		
//		reorderContainer.setVisible(false);
		editFloPanel.setVisible(false);
		editFloPanel.getElement().setId("fpnlEditFloPanel");
		
			
		startStopTimeDisplayText.getElement().setId("lblStartStopTimeDisplayText");
		startStopTimeDisplayText.getElement().setAttribute("alt", i18n.GL0957());
		startStopTimeDisplayText.getElement().setAttribute("title", i18n.GL0957());
		minsText.getElement().setInnerHTML(i18n.GL0958());
		minsText.getElement().setId("pnlMinsText");
		minsText.getElement().setAttribute("alt", i18n.GL0958());
		minsText.getElement().setAttribute("title", i18n.GL0958());
		secondsText.getElement().setInnerHTML(i18n.GL0959());
		secondsText.getElement().setId("pnlSecondsText");
		secondsText.getElement().setAttribute("alt", i18n.GL0959());
		secondsText.getElement().setAttribute("title", i18n.GL0959());
		endMinsText.getElement().setInnerHTML(i18n.GL0958());
		endMinsText.getElement().setId("pnlEndMinsText");
		endMinsText.getElement().setAttribute("alt", i18n.GL0958());
		endMinsText.getElement().setAttribute("title", i18n.GL0958());
		endSecondsText.getElement().setInnerHTML(i18n.GL0959());
		endSecondsText.getElement().setId("pnlEndSecondsText");
		endSecondsText.getElement().setAttribute("alt", i18n.GL0959());
		endSecondsText.getElement().setAttribute("title", i18n.GL0959());
		
		editSartPageText.getElement().setId("lblEditSartPageText");
		editSartPageText.getElement().setAttribute("alt", i18n.GL0960());
		editSartPageText.getElement().setAttribute("title", i18n.GL0960());
		StartPageLbl.setText(i18n.GL0961());
		StartPageLbl.getElement().setId("lblStartPageLbl");
		StartPageLbl.getElement().setAttribute("alt", i18n.GL0961());
		StartPageLbl.getElement().setAttribute("title", i18n.GL0961());
		EditBtn.setText(i18n.GL0140());
		EditBtn.getElement().setAttribute("alt", i18n.GL0140());
		EditBtn.getElement().setAttribute("title", i18n.GL0140());
		updateResourceBtn.setText(i18n.GL0962());
		updateResourceBtn.getElement().setAttribute("alt", i18n.GL0962());
		updateResourceBtn.getElement().setAttribute("title", i18n.GL0962());
		addTages.setText("Add Tags");
		addTages.getElement().setAttribute("alt", "Add Tags");
		addTages.getElement().setAttribute("title", "Add Tags");
		editInfoLbl.setText(i18n.GL0963());
		editInfoLbl.getElement().setAttribute("alt", i18n.GL0963());
		editInfoLbl.getElement().setAttribute("title", i18n.GL0963());
		editVideoTimeLbl.setText(i18n.GL0964());
		editVideoTimeLbl.getElement().setAttribute("alt", i18n.GL0964());
		editVideoTimeLbl.getElement().setAttribute("title", i18n.GL0964());
		editStartPageLbl.setText(i18n.GL0960());
		editStartPageLbl.getElement().setAttribute("alt", i18n.GL0960());
		editStartPageLbl.getElement().setAttribute("title", i18n.GL0960());
		copyResource.setText(i18n.GL0965());
		copyResource.getElement().setAttribute("alt", i18n.GL0965());
		copyResource.getElement().setAttribute("title", i18n.GL0965());
		confirmDeleteLbl.setText(i18n.GL0237());
		confirmDeleteLbl.getElement().setAttribute("alt", i18n.GL0237());
		confirmDeleteLbl.getElement().setAttribute("title", i18n.GL0237());
		UpdateTextMessage.setText(i18n.GL0966());
		UpdateTextMessage.getElement().setId("lblUpdateTextMessage");
		UpdateTextMessage.getElement().setAttribute("alt", i18n.GL0966());
		UpdateTextMessage.getElement().setAttribute("title", i18n.GL0966());
		updateNarrationBtn.setText(i18n.GL0240());
		updateNarrationBtn.getElement().setAttribute("alt", i18n.GL0240());
		updateNarrationBtn.getElement().setAttribute("title", i18n.GL0240());
		cancelNarrationBtn.setText(i18n.GL0142());
		cancelNarrationBtn.getElement().setAttribute("alt", i18n.GL0142());
		cancelNarrationBtn.getElement().setAttribute("title", i18n.GL0142());
		updateVideoTimeBtn.setText(i18n.GL0240());
		updateVideoTimeBtn.getElement().setAttribute("alt", i18n.GL0240());
		updateVideoTimeBtn.getElement().setAttribute("title", i18n.GL0240());
		cancelVideoTimeBtn.setText(i18n.GL0142());
		cancelVideoTimeBtn.getElement().setAttribute("alt", i18n.GL0142());
		cancelVideoTimeBtn.getElement().setAttribute("title", i18n.GL0142());
		updatePdfBtn.setText(i18n.GL0240());
		updatePdfBtn.getElement().setAttribute("alt", i18n.GL0240());
		updatePdfBtn.getElement().setAttribute("title", i18n.GL0240());
		cancelpdfBtn.setText(i18n.GL0142());
		cancelpdfBtn.getElement().setAttribute("alt", i18n.GL0142());
		cancelpdfBtn.getElement().setAttribute("title", i18n.GL0142());
		resourceFlowPanel.getElement().setId("collectionItem-"+collectionItem.getResource().getGooruOid());
		narrationConatainer.getElement().setId("fpnlNarrationConatainer");
		/**
		 * Hotfix changes.
		 */
		/*reorderContainer.getElement().setId("reorderContainer");
		resourceMoveUpBtn.getElement().setId("resourceMoveUpBtn");
		resourceMoveDownBtn.getElement().setId("resourceMoveDownBtn");
		reorderTxtBox.getElement().setId("reorderTxtBox");*/
		setData(collectionItem);
		
		onResourceNarrationOut();
		addDomHandler(new ActionPanelHover(), MouseOverEvent.getType());
		addDomHandler(new ActionPanelOut(), MouseOutEvent.getType());
		setPresenter(new ShelfCollectionResourceChildPresenter(this));  
		//For 5.9 
		narrationAlertMessageLbl.setText(i18n.GL0143());
		narrationAlertMessageLbl.getElement().setId("lblNarrationAlertMessageLbl");
		narrationAlertMessageLbl.getElement().setAttribute("alt", i18n.GL0143());
		narrationAlertMessageLbl.getElement().setAttribute("title", i18n.GL0143());
		actionVerPanel.setVisible(false);
		actionVerPanel.getElement().setId("fpnlActionVerPanel");
		actionVerPanelForUpdateTime.getElement().setId("fpnlActionVerPanelForUpdateTime");
		actionVerPanelForUpdateTime.setVisible(false);
		UpdateTextMessage.setVisible(false);
		ResourceEditButtonContainer.getElement().setId("fpnlResourceEditButtonContainer");
//		ResourceEditButtonContainer.getElement().getStyle().setVisibility(Visibility.HIDDEN);
		ResourceEditButtonContainer.setVisible(false);
		EditBtn.setVisible(false);
		/**
		 * hotfixchanges
		 */
//		reorderContainer.setVisible(false);
		editPdfFlowPanel.getElement().setId("fpnlEditPdfFlowPanel");
		editPdfFlowPanel.setVisible(false);
		actionVerPanelForUpdatePDF.getElement().setId("fpnlActionVerPanelForUpdatePDF");
		actionVerPanelForUpdatePDF.setVisible(false);
		fromTxt.getElement().setId("txtFromTxt");
		fromTxt.setFocus(true);
		toTxt.setFocus(true);
		toTxt.getElement().setId("txtToTxt");
		EndTimeTxt1.setFocus(true);
		EndTimeTxt2.setFocus(true);
		startpdfPageNumber.getElement().setId("txtStartpdfPageNumber");
		startpdfPageNumber.setFocus(true);
		fromTxt.getElement().setAttribute("maxlength", "4");
		toTxt.getElement().setAttribute("maxlength", "4");
		EndTimeTxt1.getElement().setId("txtEndTimeTxt1");
		EndTimeTxt1.getElement().setAttribute("maxlength", "4");
		EndTimeTxt2.getElement().setId("txtEndTimeTxt2");
		EndTimeTxt2.getElement().setAttribute("maxlength", "4");
		startpdfPageNumber.getElement().setAttribute("maxlength", "4");
		EditBtn.getElement().setId("btnEdit");
		updateResourceBtn.getElement().setId("lblUpdateResoure");
		addTages.getElement().setId("lblAddTages");
		copyResource.getElement().setId("lblCopyResource");
		editInfoLbl.getElement().setId("lblEditInfo");
		editStartPageLbl.getElement().setId("lblEditStartPage");
		confirmDeleteLbl.getElement().setId("lblConfirmDelete");
		updateNarrationBtn.getElement().setId("btnUpdateNarration");
		cancelNarrationBtn.getElement().setId("btnCancelNarration");
		updatePdfBtn.getElement().setId("btnUpdatePdf");
		cancelpdfBtn.getElement().setId("btnCancelPdf");
		editVideoTimeLbl.getElement().setId("lblEditVideoTime");
		updateVideoTimeBtn.getElement().setId("btnUpdateVideoTime");
		cancelVideoTimeBtn.getElement().setId("btnCancelVideoTime");
		fromTxt.addKeyPressHandler(new NumbersOnly());
		toTxt.addKeyPressHandler(new NumbersOnly());
		EndTimeTxt1.addKeyPressHandler(new NumbersOnly());
		EndTimeTxt2.addKeyPressHandler(new NumbersOnly());
		startpdfPageNumber.addKeyPressHandler(new NumbersOnly());
		// resourceNarrationHtml.addMouseOverHandler(new showNarationPencil());
		// resourceNarrationHtml.addMouseOutHandler(new hideNarationPencil());
		resourceNarrationHtml.getElement().setId("htmlResourceNarrationHtml");
		narrationTxtArea.getElement().setAttribute("maxlength", "500");
		narrationTxtArea.getElement().setId("tatNarrationTxtArea");
		StringUtil.setAttributes(narrationTxtArea, true);
	//	narrationTxtArea.addKeyUpHandler(new narationValidation());
		fromTxt.addKeyUpHandler(new fromTxtKeyUpHandler());
		toTxt.addKeyUpHandler(new toTxtKeyUpHandler());
		editFieldsFloPanel.setVisible(false);
		resourceImageUc.getElement().setId("ResourceImageUc");
		resourceTitle1.getElement().setId("pnlResourceTitle1");
		resourceTitleContainer.getElement().setId("pnlResourceTitleContainer");
		resourceTitleLbl.getElement().setId("htmlResourceTitleLbl");
		pencilEditNarationLbl.getElement().setId("lblPencilEditNarationLbl");
		narationFloPanel.getElement().setId("fpnlNarationFloPanel");
		narrationAlertMessageLbl.getElement().setAttribute("alt", i18n.GL0143());
		videoDisplay.getElement().setId("fpnlVideoDisplay");
		videoImage.getElement().setId("fpnlVideoImage");
		videoTimeField.getElement().setId("lblVideoTimeField");
		fromLblDisplayText.getElement().setId("lblFromLblDisplayText");
		editFieldsFloPanel.getElement().setId("fpnlEditFieldsFloPanel");
		fromLbl.getElement().setId("lblFromLbl");
		ToLbl.getElement().setId("lblToLbl");
		updatePDFLabelText.getElement().setId("lblUpdatePDFLabelText");
		//endPageLbl.setVisible(false);
		endPageLbl.setText(i18n.GL2026());
		endPageLbl.getElement().setId("endPageLbl");
		
		
		
		
		// To check whether resource is public and is created by logged in user
		String resourceShare = collectionItemDo.getResource().getSharing();
	
		String resourceCategory = collectionItemDo.getResource().getResourceFormat() !=null ? collectionItemDo.getResource().getResourceFormat().getDisplayName() : "text";
		
	
		if (resourceShare.equalsIgnoreCase("public")
				&& !resourceCategory.equalsIgnoreCase("question")) {
			editInfoLbl.setVisible(false);
		} else if (resourceShare.equalsIgnoreCase("public")
				&& resourceCategory.equalsIgnoreCase("question")
				&& checkLoggedInUser()) {
			
			editInfoLbl.setVisible(true);
		} else if (resourceShare.equalsIgnoreCase("private")
				&& !resourceCategory.equalsIgnoreCase("question")
				&& checkLoggedInUser()) {
			
			editInfoLbl.setVisible(true);
		} else if (!checkLoggedInUser()) {
			editInfoLbl.setVisible(false);
		}
		
		/*narrationTxtArea.addInitializeHandler(new InitializeHandler() {
			@Override
			public void onInitialize(InitializeEvent event) {
				    Document document = IFrameElement.as(narrationTxtArea.getElement()).getContentDocument();
	                BodyElement body = document.getBody();
	                body.setAttribute("style", "font-family: Arial;font-size:13px;color:#515151;line-height:1.2;margin:0px;");
			}
		});*/
		
/*		narrationTxtArea.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				updateNarrationBtn.setEnabled(false);
				updateNarrationBtn.getElement().addClassName("disabled");
				if (trim(narrationTxtArea.getHTML()).length() > 0){
				
					updateNarrationBtn.setEnabled(false);
					updateNarrationBtn.getElement().addClassName("disabled");
					
				}
			}
		});*/
		

		
		narrationTxtArea.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				narrationTxtArea.getElement().getStyle().clearBackgroundColor();
				narrationTxtArea.getElement().getStyle().setBorderColor("#ccc");
				if(narrationTxtArea.getText().toString().trim().length() >= 500){ 
					narrationAlertMessageLbl
                    .removeStyleName("titleAlertMessageDeActive");
					narrationAlertMessageLbl
                    .addStyleName("titleAlertMessageActive");
					updateNarrationBtn.setEnabled(false);
	                updateNarrationBtn.getElement().addClassName("disabled");
					//event.preventDefault();
				}
				else
				{ 
					updateNarrationBtn.setEnabled(true);
	                updateNarrationBtn.getElement().removeClassName("disabled");
					narrationAlertMessageLbl
							.addStyleName("titleAlertMessageDeActive");
					narrationAlertMessageLbl
							.removeStyleName("titleAlertMessageActive");
				}
				
			}
		});
		narrationTxtArea.addKeyDownHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				narrationAlertMessageLbl.setVisible(false);
				if(narrationTxtArea.getText().toString().trim().length() >= 500){ 
					narrationAlertMessageLbl
                    .removeStyleName("titleAlertMessageDeActive");
					narrationAlertMessageLbl
                    .addStyleName("titleAlertMessageActive");
					updateNarrationBtn.setEnabled(false);
	                updateNarrationBtn.getElement().addClassName("disabled");
					if(event.getNativeEvent().getCtrlKey() && event.getNativeEvent().getKeyCode()==86){
						//event.getNativeEvent().preventDefault();
						((RichTextArea)event.getSource()).setFocus(false);
						narrationAlertMessageLbl.setVisible(true);
						return;
					}
				}else{
					narrationAlertMessageLbl
					.addStyleName("titleAlertMessageDeActive");
			narrationAlertMessageLbl
					.removeStyleName("titleAlertMessageActive");
			updateNarrationBtn.setEnabled(true);
            updateNarrationBtn.getElement().removeClassName("disabled");
            narrationAlertMessageLbl.setVisible(false);
				}
			}
		});
		
		imgNotFriendly.addMouseOverHandler(new MouseOverHandler() {
			
			@Override
			public void onMouseOver(MouseOverEvent event) {
				toolTip = new ToolTip(i18n.GL0454()+""+"<img src='/images/mos/MobileFriendly.png' style='margin-top:0px;width:20px;height:15px;'/>"+" "+i18n.GL04431()+" "+"<img src='/images/mos/mobileunfriendly.png' style='margin-top:0px;width:20px;height:15px;'/>"+" "+i18n.GL_SPL_EXCLAMATION());
				toolTip.getTootltipContent().getElement().setAttribute("style", "width: 258px;");
				
				toolTip.getElement().getStyle().setBackgroundColor("transparent");
				toolTip.getElement().getStyle().setPosition(Position.ABSOLUTE);
				toolTip.setPopupPosition(imgNotFriendly.getAbsoluteLeft()-(50+22), imgNotFriendly.getAbsoluteTop()+22);
				toolTip.show();
			}
		});
		imgNotFriendly.addMouseOutHandler(new MouseOutHandler() {
			
			@Override
			public void onMouseOut(MouseOutEvent event) {
				
				EventTarget target = event.getRelatedTarget();
				  if (Element.is(target)) {
					  if (!toolTip.getElement().isOrHasChild(Element.as(target))){
						  toolTip.hide();
					  }
				  }
			}
		});
		resourceNarrationHtml.getElement().getStyle().clearWidth();
		String value = StringUtil.generateMessage(i18n.GL2103(), "500");
		
		StringUtil.setAttributes(lblCharLimit.getElement(), "lblCharLimit", value, value);
		lblCharLimit.setText(value);
		lblCharLimit.setVisible(false);
		
		AppClientFactory.getEventBus().addHandler(PublishButtonHideEvent.TYPE, publishButtonHideEventHandler); 
	}
		PublishButtonHideEventHandler publishButtonHideEventHandler = new PublishButtonHideEventHandler(){

		@Override
		public void hideEditButton() {
					
			collectionItemDo.getResource().setSharing("public");
			editInfoLbl.setVisible(false);
		}
		
	};
	public void setUpdatedData(CollectionItemDo collectionItemDo) {
		
	}
	@UiHandler("EditBtn")
	public void onClickEdit(ClickEvent clickEvent)
	{
		if (/*ResourceEditButtonContainer.getElement().getStyle().getVisibility().equalsIgnoreCase("VISIBLE")*/ResourceEditButtonContainer.isVisible()) {
//			ResourceEditButtonContainer.getElement().getStyle().setVisibility(Visibility.HIDDEN);
			ResourceEditButtonContainer.setVisible(false);
			
		} else {
			narrationAlertMessageLbl
				.addStyleName("titleAlertMessageDeActive");
			narrationAlertMessageLbl
				.removeStyleName("titleAlertMessageActive");
	
			narrationTxtArea.getElement().getStyle().clearBackgroundColor();
			narrationTxtArea.getElement().getStyle().setBorderColor("#ccc");
			narrationAlertMessageLbl.setVisible(false);
//			ResourceEditButtonContainer.getElement().getStyle().setVisibility(Visibility.VISIBLE);
			ResourceEditButtonContainer.setVisible(true);
			}	
	}
	private class toTxtKeyUpHandler implements KeyUpHandler {

		@Override
		public void onKeyUp(KeyUpEvent event) {
			isEdited = true;
			toTxt.setFocus(true);
			
		}
	}

	private class fromTxtKeyUpHandler implements KeyUpHandler {

		@Override
		public void onKeyUp(KeyUpEvent event) {
			isEdited = true;
			fromTxt.setFocus(true);
		}

	}
	private class NumbersOnly implements KeyPressHandler {
	      
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
					
		}
    }
	/*private class showNarationPencil implements MouseOverHandler {

		@Override
		public void onMouseOver(MouseOverEvent event) {
			onResourceNarrationHover();
		}

	}*/

	private class narationValidation implements KeyUpHandler {

		@Override
		public void onKeyUp(KeyUpEvent event) {
			narrationDataLength=narrationTxtArea.getText().toString().trim().length();
			 if (trim(narrationTxtArea.getText()).length() > 0 && trim(narrationTxtArea.getText()).length() <= 500){
				
				updateNarrationBtn.setEnabled(true);
                updateNarrationBtn.getElement().removeClassName("disabled");
                narrationAlertMessageLbl
                        .addStyleName("titleAlertMessageDeActive");
                narrationAlertMessageLbl
                        .removeStyleName("titleAlertMessageActive");
				
			}
			else {
				updateNarrationBtn.setEnabled(true);
                updateNarrationBtn.getElement().addClassName("disabled");
				narrationAlertMessageLbl
						.addStyleName("titleAlertMessageDeActive");
				narrationAlertMessageLbl
						.removeStyleName("titleAlertMessageActive");
			}
			isEdited = true;
		}

	}

	/*private class hideNarationPencil implements MouseOutHandler {

		@Override
		public void onMouseOut(MouseOutEvent event) {
			onResourceNarrationOut();
		}

	}*/

	/**
	 * 
	 * To show the ResourceMetaDataInfo Edit,Copy and Remove buttons
	 */
	private class ActionPanelHover implements MouseOverHandler {

		@Override
		public void onMouseOver(MouseOverEvent event) {
			if ((actionVerPanel.isVisible()==false) && (actionVerPanelForUpdateTime.isVisible()==false) && (actionVerPanelForUpdatePDF.isVisible()==false)) {
				if(collectionType!=null && !collectionType.equals(ShelfPresenter.ASSESSMENT)){
					EditBtn.setVisible(true);
				}else{
					EditBtn.setVisible(false);
				}
				
				/**
				 * Hot fix changes
				 */
				/*if(isReorderContainerVisible){
					reorderContainer.setVisible(true);
				}else{
					reorderContainer.setVisible(false);
				}*/
				
				/**
				 * Following code is not changed for hot fix
				 */
				//ResourceEditButtonContainer.getElement().getStyle().setVisibility(Visibility.VISIBLE);
				//actionVerPanel.setVisible(true);
				// onResourceNarrationHover();

			}
		}
	}

	/**
	 * 
	 * To hide the ResourceMetaDataInfo Edit,Copy and Remove buttons
	 */
	private class ActionPanelOut implements MouseOutHandler {

		@Override
		public void onMouseOut(MouseOutEvent event) {
			//if (updateResourceBtn.getText().equalsIgnoreCase("Edit Narration")) {
				EditBtn.setVisible(false);
				/*s*/
				ResourceEditButtonContainer.setVisible(false);
//				ResourceEditButtonContainer.getElement().getStyle().setVisibility(Visibility.HIDDEN);
				//actionVerPanel.setVisible(false);
				//actionVerPanelForUpdateTime.setVisible(false);
				// onResourceNarrationOut();
			//}
		}
	}

	/**
	 * set collection item meta data , set title , url, narration, start and
	 * stop
	 * 
	 * @param collectionItem
	 *            instance of {@link CollectionItemDo}
	 */
	public void setData(CollectionItemDo collectionItem) {
		
		errorMsgLabel.setText("");
		videoDisplay.setVisible(true);
		narrationConatainer.setVisible(true);
		editFieldsFloPanel.setVisible(false);
		actionVerPanelForUpdateTime.setVisible(false);
		editFloPanel.setVisible(false);
		isEdited = false;
		disableAllEditMode();
		lblCharLimit.setVisible(false);
		resourceNarrationHtml.getElement().getStyle().clearWidth();
		
		String tumbnailUrl;
		//resourceTitleLbl.setText(StringUtil.truncateText(collectionItem.getResource().getTitle(), 70));
		String resourceTitle = collectionItem.getResource().getTitle()==null?"":collectionItem.getResource().getTitle();
		String titlelbl1=InfoUtil.removeQuestionTagsOnBoldClick(resourceTitle);
		resourceTitleLbl.setHTML(removeHtmlTags(titlelbl1));
		//resourceTitleLbl.setHTML(resourceTitle.replaceAll("<p>", "").replaceAll("</p>", "").replaceAll("<br data-mce-bogus=\"1\">", "").replaceAll("<br>", "").replaceAll("</br>", ""));
		
		resourceTitleLbl.getElement().setAttribute("alt", resourceTitle.replaceAll("<p>", "").replaceAll("</p>", "").replaceAll("<br data-mce-bogus=\"1\">", "").replaceAll("<br>", "").replaceAll("</br>", ""));
		resourceTitleLbl.getElement().setAttribute("title", resourceTitle.replaceAll("<p>", "").replaceAll("</p>", "").replaceAll("<br data-mce-bogus=\"1\">", "").replaceAll("<br>", "").replaceAll("</br>", ""));
		resourceTitleLbl.getElement().getStyle().setWidth(63, Unit.PCT);
		setResourcePlayLink();
		String resourceType = collectionItemDo.getResource().getResourceType().getName();
		youtube = resourceType.equalsIgnoreCase(ImageUtil.YOUTUBE);
	
		mediaType = collectionItem.getResource().getMediaType();
		
		setVisibility = mediaType !=null ?  mediaType.equalsIgnoreCase("iPad_friendly") ? true : false : true;
		//imgNotFriendly.setVisible(setVisibility);
		if(setVisibility)
		{
			imgNotFriendly.getElement().setId("imgImgFriendly");
			imgNotFriendly.setTitle(i18n.GL0737_1());
			imgNotFriendly.setAltText(i18n.GL0737_1());
			imgNotFriendly.setUrl("images/mos/MobileFriendly.png");
		}else
		{
			imgNotFriendly.getElement().setId("imgImgNotFriendly");
			imgNotFriendly.setTitle(i18n.GL0737());
			imgNotFriendly.setAltText(i18n.GL0737());
			imgNotFriendly.setUrl("images/mos/mobileunfriendly.png");
		}
		
		resourceTitle=resourceTitleLbl.getText();
		if (resourceTitleLbl.getText().length()>=70){
			resourceTitleLbl.getElement().getStyle().setWidth(80, Unit.PCT);
		}else{
			resourceTitleLbl.getElement().getStyle().clearWidth();	
		}
		if (collectionItem.getResource().getAssets() != null
				&& collectionItem.getResource().getAssets().size() > 0) {
			for (AssetsDo asstesDo : collectionItem.getResource().getAssets()) {

				tumbnailUrl = collectionItem.getCollection().getAssetURI()
						+ collectionItem.getResource().getFolder()
						+ asstesDo.getAsset().getName();
				
//				resourceImageUc.renderSearch(collectionItem.getResource().getCategory(), tumbnailUrl, collectionItem.getResource().getUrl(), collectionItem.getCollectionItemId(),PLAYER_NAME,resourceTitle, youtube,"");
				String resourceFormat = collectionItem.getResource().getResourceFormat() !=null ? collectionItem.getResource().getResourceFormat().getDisplayName() : "text";
				resourceImageUc.renderSearch(resourceFormat, tumbnailUrl, collectionItem.getResource().getUrl(), collectionItem.getCollectionItemId(),resourceTitle, youtube,collectionItem.getNarration(),collectionType,null);
			}
		} else {
				try {
					if(collectionItem.getResource().getThumbnails()==null) {
						ThumbnailDo thumbnailDo = new ThumbnailDo();
						collectionItem.getResource().setThumbnails(thumbnailDo);
						collectionItem.getResource().getThumbnails().setUrl("/null");
					}
				} catch (Exception e) {
						ThumbnailDo thumbnailDo = new ThumbnailDo();
						collectionItem.getResource().setThumbnails(thumbnailDo);
						collectionItem.getResource().getThumbnails().setUrl("/null");
				}
				
				/*resourceImageUc.renderSearch(collectionItem.getResource()
					.getCategory(), collectionItem.getResource()
					.getThumbnails().getUrl(), collectionItem.getResource()
					.getUrl(), collectionItem.getCollectionItemId(),
					PLAYER_NAME,resourceTitle, youtube,"");*/
				String resourceFormat =  collectionItem.getResource().getResourceFormat() != null ? collectionItem.getResource().getResourceFormat().getDisplayName() : "text";
				resourceImageUc.renderSearch(resourceFormat, collectionItem.getResource().getThumbnails().getUrl(), collectionItem.getResource().getUrl(), collectionItem.getCollectionItemId(),resourceTitle, youtube,collectionItem.getNarration(),collectionType,null);
		}

		if (collectionItem.getNarration() != null && !collectionItem.getNarration().trim().isEmpty()){
			narrationData=collectionItem.getNarration();
			narrationData=narrationData.replaceAll("rgb", "");
			resourceNarrationHtml.setHTML(narrationData);
			resourceNarrationHtml.getElement().setAttribute("alt", narrationData);
			resourceNarrationHtml.getElement().setAttribute("title", narrationData);
			/*if(collectionItem.getNarration().length() > 80) {
				narrationData=collectionItem.getNarration();
			String narration = collectionItem.getNarration().length() > 80 ? collectionItem
					.getNarration().substring(0, 80).trim()
					+ "..."
					: collectionItem.getNarration().trim();
			resourceNarrationHtml.setHTML(narration);
				}
				else
				{
					resourceNarrationHtml.setHTML(collectionItem.getNarration());	
				}*/
		} else {
			resourceNarrationHtml.setHTML(NO_NARRATION_ADDED);
			resourceNarrationHtml.getElement().setAttribute("alt", NO_NARRATION_ADDED);
			resourceNarrationHtml.getElement().setAttribute("title", NO_NARRATION_ADDED);
		}
		String category = collectionItemDo.getResource().getResourceFormat() != null ? collectionItemDo.getResource().getResourceFormat().getDisplayName() : "text";
	
		if(!youtube){
			videoImage.removeStyleName(CollectionEditResourceCBundle.INSTANCE.css().videoImageContainer());
			editVideoTimeLbl.setVisible(false);
			editStartPageLbl.setVisible(false);	
		}
		if (youtube) {
			editStartPageLbl.setVisible(false);
			editVideoTimeLbl.setVisible(true);
		
		
			
			videoTimeField.setText(VIDEO_TIME);
			videoTimeField.getElement().setAttribute("alt", VIDEO_TIME);
			videoTimeField.getElement().setAttribute("title", VIDEO_TIME);
			videoDisplay.setVisible(true);
			videoImage.setStyleName(CollectionEditResourceCBundle.INSTANCE.css().videoImageContainer());
			//if(collectionItemDo.getStart()!=null && collectionItemDo.getStop()!=null && collectionItemDo.getStart()!="00:00:00" && collectionItemDo.getStop()!="00:00:00"){
			String stopTime = (collectionItemDo.getStop() == null) ? "00:00:00"
					: collectionItemDo.getStop();
			
			String startTime = (collectionItemDo.getStart() == null) ? "00:00:00"
					: collectionItemDo.getStart();

			startTime = startTime.replaceAll("\\.", ":");
			stopTime = stopTime.replaceAll("\\.", ":");
			String youTubeVideoId = ResourceImageUtil.getYoutubeVideoId(collectionItemDo
					.getResource().getUrl());
			AppClientFactory.getInjector().getResourceService().getYoutubeDuration(youTubeVideoId,new SimpleAsyncCallback<String>() {
						@Override
						public void onSuccess(String youtubeInfo) {
							if (youtubeInfo != null) {
								totalVideoLength = Integer.parseInt(youtubeInfo);
								
								String tolTimeInmin = "";
								String totalTimeSec = "";

								int tolTimeInminutes = totalVideoLength / 60;
								if (tolTimeInminutes < 10) {
									tolTimeInmin = "0"
											+ tolTimeInminutes;
								} else {
									tolTimeInmin = tolTimeInminutes
											+ "";
								}

								int totalTimeInseconds = totalVideoLength % 60;
								if (totalTimeInseconds < 10) {
									totalTimeSec = "0"
											+ totalTimeInseconds;
								} else {
									totalTimeSec = totalTimeInseconds
											+ "";
								}
								startStopTimeDisplayText.setText(i18n.GL0957()+tolTimeInmin+":"+totalTimeSec);
							}
						}
			});
			if (!"00:00:00".equalsIgnoreCase(stopTime) ||!"00:00:00".equalsIgnoreCase(startTime)) {
					String[] VideoStartTime=startTime.split(":");
					String[] VideoEndTime=stopTime.split(":");
					String startMm=Integer.parseInt(VideoStartTime[0])*60+Integer.parseInt(VideoStartTime[1])+"";
					String startSec =null;
					String endSec = null;
					if (VideoStartTime.length>2){
						startSec=Integer.parseInt(VideoStartTime[2])+"";
					}else{
						startSec="00";
					}
					String endMm=Integer.parseInt(VideoEndTime[0])*60+Integer.parseInt(VideoEndTime[1])+"";
					
					if (VideoEndTime.length>2){
						endSec=Integer.parseInt(VideoEndTime[2])+"";
					}else{
						endSec="00";
					}
					 
					if(startMm.length()<2)
					{
						startMm="0"+startMm;
					}
					if(startSec.length()<2)
					{
						startSec="0"+startSec;
					}
					if(endMm.length()<2)
					{
						endMm="0"+endMm;
					}
					if(endSec.length()<2)
					{
						endSec="0"+endSec;
					}
					fromTxt.setText(startMm);
					fromTxt.getElement().setAttribute("alt", startMm);
					fromTxt.getElement().setAttribute("title", startMm);
					toTxt.setText(startSec);
					toTxt.getElement().setAttribute("alt", startSec);
					toTxt.getElement().setAttribute("title", startSec);
					EndTimeTxt1.setText(endMm);
					EndTimeTxt1.getElement().setAttribute("alt", endMm);
					EndTimeTxt1.getElement().setAttribute("title", endMm);
					EndTimeTxt2.setText(endSec);
					EndTimeTxt2.getElement().setAttribute("alt", endSec);
					EndTimeTxt2.getElement().setAttribute("title", endSec);
					fromLblDisplayText.setText(startMm+" "+i18n.GL0958()+" "+startSec +" "+i18n.GL0959()+" "+i18n.GL_GRR_Hyphen()+" "+endMm+" "+i18n.GL0958()+" "+endSec +" "+i18n.GL0959()+" ");
					fromLblDisplayText.getElement().setAttribute("alt", startMm+" "+i18n.GL0958()+" "+startSec +" "+i18n.GL0959()+" "+i18n.GL_GRR_Hyphen()+" "+endMm+" "+i18n.GL0958()+" "+endSec +" "+i18n.GL0959()+" ");
					fromLblDisplayText.getElement().setAttribute("title", startMm+" "+i18n.GL0958()+" "+startSec +" "+i18n.GL0959()+" "+i18n.GL_GRR_Hyphen()+" "+endMm+" "+i18n.GL0958()+" "+endSec +" "+i18n.GL0959()+" ");
					fromLbl.setText(FROM_START_TIME);
					fromLbl.getElement().setAttribute("alt", FROM_START_TIME);
					fromLbl.getElement().setAttribute("title", FROM_START_TIME);
					ToLbl.setText(FROM_STOP_TIME);
					ToLbl.getElement().setAttribute("alt", FROM_STOP_TIME);
					ToLbl.getElement().setAttribute("title", FROM_STOP_TIME);
			}
					
			else
			{
				
			String videoId = ResourceImageUtil.getYoutubeVideoId(collectionItemDo
						.getResource().getUrl());
					
				if (videoId != null) {
					AppClientFactory.getInjector().getResourceService().getYoutubeDuration(videoId, new SimpleAsyncCallback<String>() {
										@Override
										public void onSuccess(String youtubeInfo) {
											if (youtubeInfo != null) {
												totalVideoLength = Integer.parseInt(youtubeInfo);
												
												String tolTimeInmin = "";
												String totalTimeSec = "";

												int tolTimeInminutes = totalVideoLength / 60;
												if (tolTimeInminutes < 10) {
													tolTimeInmin = "0"
															+ tolTimeInminutes;
												} else {
													tolTimeInmin = tolTimeInminutes
															+ "";
												}

												int totalTimeInseconds = totalVideoLength % 60;
												if (totalTimeInseconds < 10) {
													totalTimeSec = "0"
															+ totalTimeInseconds;
												} else {
													totalTimeSec = totalTimeInseconds
															+ "";
												}
												fromLblDisplayText
														.setText(START_MINUTE
																+ " "+i18n.GL0958()+" "
																+ START_SEC
																+ " "+i18n.GL0959()+" "+i18n.GL_GRR_Hyphen()+" "
																+ tolTimeInmin
																+ " "+i18n.GL0958()+" "
																+ totalTimeSec
																+ " "+i18n.GL0959()+" ");
												fromLblDisplayText.getElement().setAttribute("alt", START_MINUTE
														+ " "+i18n.GL0958()+" "
														+ START_SEC
														+ " "+i18n.GL0959()+" "+i18n.GL_GRR_Hyphen()+" "
														+ tolTimeInmin
														+ " "+i18n.GL0958()+" "
														+ totalTimeSec
														+ " "+i18n.GL0959()+" ");
												fromLblDisplayText.getElement().setAttribute("title", START_MINUTE
														+ " "+i18n.GL0958()+" "
														+ START_SEC
														+ " "+i18n.GL0959()+" "+i18n.GL_GRR_Hyphen()+" "
														+ tolTimeInmin
														+ " "+i18n.GL0958()+" "
														+ totalTimeSec
														+ " "+i18n.GL0959()+" ");
												EndTimeTxt1
														.setText(tolTimeInmin);
												EndTimeTxt1.getElement().setAttribute("alt", tolTimeInmin);
												EndTimeTxt1.getElement().setAttribute("title", tolTimeInmin);
												EndTimeTxt2
														.setText(totalTimeSec);
												EndTimeTxt2.getElement().setAttribute("alt", totalTimeSec);
												EndTimeTxt2.getElement().setAttribute("title", totalTimeSec);
											} else {
												fromLblDisplayText
														.setText(START_MINUTE
																+ " "+i18n.GL0958()+" "
																+ START_SEC
																+ " "+i18n.GL0959()+" "+i18n.GL_GRR_Hyphen()+" "
																+ START_MINUTE
																+ " "+i18n.GL0958()+" "
																+ END_MINUTE
																+ " "+i18n.GL0959()+" ");
												fromLblDisplayText.getElement().setAttribute("alt", START_MINUTE
														+ " "+i18n.GL0958()+" "
														+ START_SEC
														+ " "+i18n.GL0959()+" "+i18n.GL_GRR_Hyphen()+" "
														+ START_MINUTE
														+ " "+i18n.GL0958()+" "
														+ END_MINUTE
														+ " "+i18n.GL0959()+" ");
												fromLblDisplayText.getElement().setAttribute("title", START_MINUTE
														+ " "+i18n.GL0958()+" "
														+ START_SEC
														+ " "+i18n.GL0959()+" "+i18n.GL_GRR_Hyphen()+" "
														+ START_MINUTE
														+ " "+i18n.GL0958()+" "
														+ END_MINUTE
														+ " "+i18n.GL0959()+" ");
												EndTimeTxt1.setText(END_MINUTE);
												EndTimeTxt1.getElement().setAttribute("alt", END_MINUTE);
												EndTimeTxt1.getElement().setAttribute("title", END_MINUTE);
												EndTimeTxt2.setText(END_SEC);
												EndTimeTxt2.getElement().setAttribute("alt", END_SEC);
												EndTimeTxt2.getElement().setAttribute("title", END_SEC);
											}
										}

									});
				}
				fromLbl.setText(FROM_START_TIME);
				fromLbl.getElement().setAttribute("alt", FROM_START_TIME);
				fromLbl.getElement().setAttribute("title", FROM_START_TIME);
				ToLbl.setText(FROM_STOP_TIME);
				ToLbl.getElement().setAttribute("alt", FROM_STOP_TIME);
				ToLbl.getElement().setAttribute("title", FROM_STOP_TIME);
				fromTxt.setText(START_STOP_MINUTE);
				fromTxt.getElement().setAttribute("alt", START_STOP_MINUTE);
				fromTxt.getElement().setAttribute("title", START_STOP_MINUTE);
				toTxt.setText(START_STOP_SEC);
				toTxt.getElement().setAttribute("alt", START_STOP_SEC);
				toTxt.getElement().setAttribute("title", START_STOP_SEC);

			}
		} else if (category!=null){ 
			if (category.equalsIgnoreCase("Slide")
				|| category.equalsIgnoreCase("Textbook")
				|| category.equalsIgnoreCase("Handout") || category.equalsIgnoreCase("Question")||category.equalsIgnoreCase("Exam")||category.equalsIgnoreCase("Lesson")
				||category.equalsIgnoreCase("Website")||category.equalsIgnoreCase("Webpage")||category.equalsIgnoreCase("Text")) {
				//fromLbl.setText(FROM_START_PAGE);
				videoImage.removeStyleName(CollectionEditResourceCBundle.INSTANCE.css().videoImageContainer());
				editVideoTimeLbl.setVisible(false);
				editStartPageLbl.setVisible(false);
			}
			
		}
		//if(category.equalsIgnoreCase("Textbook"))
		
		if(collectionItemDo.getResource() !=null && collectionItemDo.getResource().getUrl() != null && collectionItemDo.getResource().getUrl().endsWith(".pdf"))
		{
			
			editStartPageLbl.setVisible(true);
			editVideoTimeLbl.setVisible(false);
			fromLblDisplayText.setVisible(true);
			videoDisplay.setVisible(true);
					
			videoImage.removeStyleName(CollectionEditResourceCBundle.INSTANCE.css().videoImageContainer());
			videoImage.setStyleName(CollectionEditResourceCBundle.INSTANCE.css().pdfImageContainer());
			String startPageNumber=collectionItemDo.getStart();
			totalPages = collectionItemDo.getTotalPages();
			
			if(totalPages == null)
			{
				fromLblDisplayText.setVisible(false);
				editStartPageLbl.setVisible(false);
				videoImage.setVisible(false);
			}
			else
			{
				fromLblDisplayText.setVisible(true);
				editSartPageText.setText(i18n.GL2039() + totalPages);
				editStartPageLbl.setVisible(true);	
				videoImage.setVisible(true);
			}
			
			String endPageNumber=collectionItemDo.getStop();

		//	String endPageNumber=collectionItemDo.getStop();
			
			//updatePDFLabelText.setText("0f "+endPageNumber+" pages");
			if(startPageNumber==null){
			startpdfPageNumber.setText("1");
			startpdfPageNumber.getElement().setAttribute("alt", "1");
			startpdfPageNumber.getElement().setAttribute("title", "1");
			//videoImage.removeStyleName(CollectionEditResourceCBundle.INSTANCE.css().pdfImageContainer());
			fromLblDisplayText.setText(START_PAGE+DEFAULT_START_PAGE);
			fromLblDisplayText.getElement().setAttribute("alt", START_PAGE+DEFAULT_START_PAGE);
			fromLblDisplayText.getElement().setAttribute("title", START_PAGE+DEFAULT_START_PAGE);
			//fromLblDisplayText.setText(START_PAGE+DEFAULT_START_PAGE+" of "+DEFAULT_END_PAGE+" pages");
			}
			else{
				if(endPageNumber.equalsIgnoreCase("")){
					fromLblDisplayText.setText(START_PAGE+startPageNumber+" - "+ i18n.GL2026()+totalPages);
					stoppdfPageNumber.setText(totalPages+"");
				}
				else{
					fromLblDisplayText.setText(START_PAGE+startPageNumber+" - "+i18n.GL2026()+endPageNumber);	
					stoppdfPageNumber.setText(endPageNumber+"");
				}
				fromLblDisplayText.getElement().setAttribute("alt", START_PAGE+startPageNumber);
				fromLblDisplayText.getElement().setAttribute("title", START_PAGE+startPageNumber);
				
				startpdfPageNumber.setText(startPageNumber);
			
				startpdfPageNumber.getElement().setAttribute("alt", startPageNumber);
				startpdfPageNumber.getElement().setAttribute("title", startPageNumber);
			//fromLblDisplayText.setText(START_PAGE+startPageNumber+" of "+endPageNumber+" pages");
			}
		}
		else if (category!=null){ 
			if (category.equalsIgnoreCase("Slide")
				|| category.equalsIgnoreCase("Textbook")
				|| category.equalsIgnoreCase("Handout") ||category.equalsIgnoreCase("Question")||category.equalsIgnoreCase("Exam")||category.equalsIgnoreCase("Lesson")
				||category.equalsIgnoreCase("Website")||category.equalsIgnoreCase("Webpage")) {
				//fromLbl.setText(FROM_START_PAGE);
				videoImage.removeStyleName(CollectionEditResourceCBundle.INSTANCE.css().pdfImageContainer());
				editStartPageLbl.setVisible(false);
				editVideoTimeLbl.setVisible(false);
			}
		}
//		Window.enableScrolling(false);
	}
		
	/**
	 * To play resource
	 * 
	 * @param clickEvent
	 */
	/*@UiHandler("resourceTitleLbl")
	public void onResourceTitleClick(ClickEvent clickEvent) {
//		setResourcePlayLink();
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", collectionItemDo.getCollectionItemId());
		params.put("pn", "collectionItem");
		AppClientFactory.getPlaceManager().revealPlace(
				PlaceTokens.RESOURCE_PLAY, params);
	}*/
	
	public void setResourcePlayLink(){
		Anchor resourceAnchor=new Anchor();
		resourceAnchor.setHref(getResourceLink());
		resourceAnchor.setStyleName("");
		resourceAnchor.getElement().appendChild(resourceTitleLbl.getElement());
		resourceTitleContainer.add(resourceAnchor);
		if(collectionType!=null&&!collectionType.equals(ShelfPresenter.ASSESSMENT)){
			resourceAnchor.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					AppClientFactory.getPlaceManager().setRefreshPlace(false);
				}
			});
		}else{
			resourceAnchor.setTarget("_blank");
		}
	}
	
	public String getResourceLink(){
		if(collectionType!=null&&!collectionType.equals(ShelfPresenter.ASSESSMENT)){
			String collectionId=AppClientFactory.getPlaceManager().getRequestParameter("id", null);
			if(collectionItemDo.getNarration()!=null&&!collectionItemDo.getNarration().trim().equals("")){
				String resourceLink="#"+PlaceTokens.COLLECTION_PLAY+"&id="+collectionId+"&rid="+collectionItemDo.getCollectionItemId()+"&tab=narration";
				return resourceLink;
			}else{
				String resourceLink="#"+PlaceTokens.COLLECTION_PLAY+"&id="+collectionId+"&rid="+collectionItemDo.getCollectionItemId();
				return resourceLink;
			}
		}else{
			return AppClientFactory.loggedInUser.getSettings().getAssessementEndPoint();
		}
	}

	/**
	 * Edit collection item , update collection item
	 * 
	 * @param clickEvent
	 */
	@UiHandler("updateResourceBtn")
	public void onEditClick(ClickEvent clickEvent) {
		MixpanelUtil.Organize_Click_Edit_Narration();
		EditBtn.setVisible(false);
		/*reorderContainer.setVisible(false);*/
		ResourceEditButtonContainer.setVisible(false);
		/*ResourceEditButtonContainer.getElement().getStyle()
		.setVisibility(Visibility.HIDDEN);*/
		actionVerPanel.setVisible(true);
			editAndUpdateResource();
		
		lblCharLimit.setVisible(true);
		resourceNarrationHtml.getElement().getStyle().setWidth(230, Unit.PX);
	}
	@UiHandler("addTages")
	public void onAddTagesClick(ClickEvent clickEvent) {
		if(AppClientFactory.isAnonymous()) {
			AppClientFactory.fireEvent(new InvokeLoginEvent());
		} else {
			Window.enableScrolling(false);
			popup=new AddTagesPopupView(collectionItemDo.getResource().getGooruOid()){
				
				@Override
				public void closePoup(boolean isCancelclicked) {
			        this.hide();
			        if(!isCancelclicked){
			        SuccessPopupViewVc success = new SuccessPopupViewVc() {

						@Override
						public void onClickPositiveButton(ClickEvent event) {
							this.hide();
							if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.COLLECTION_SEARCH) || AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)){
								Window.enableScrolling(false);
							}else{
								Window.enableScrolling(true);
							}
						}
						
					};
					success.setHeight("253px");
					success.setWidth("450px");
					success.setPopupTitle(i18n.GL1795());
					success.setDescText(i18n.GL1796());
					success.enableTaggingImage();
					success.setPositiveButtonText(i18n.GL0190());
					success.center();
					success.show();
			        }else{
			        	if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.COLLECTION_SEARCH) || AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)){
			    			Window.enableScrolling(false);
			    		}else{
			    			Window.enableScrolling(true);
			    		}
			        }
				}
			};
			popup.show();
			popup.setPopupPosition(popup.getAbsoluteLeft(),Window.getScrollTop()+10);
		}
	}
	/*
	 * This clickEvent is used to edit pdf
	 */
	@UiHandler("editStartPageLbl")
	public void oneditStartPageLblClick(ClickEvent clickEvent) {
		MixpanelUtil.Organize_Click_Edit_Start_Page();
		errorMsgLabelForPDF.setText("");
		actionVerPanelForUpdatePDF.setVisible(true);
		//endPageLbl.setVisible(true);
		editPdfFlowPanel.setVisible(true);
		ResourceEditButtonContainer.setVisible(false);
		/*ResourceEditButtonContainer.getElement().getStyle()
		.setVisibility(Visibility.HIDDEN);*/
		EditBtn.setVisible(false);
		/*reorderContainer.setVisible(false);*/
		fromLblDisplayText.setVisible(false);
		videoDisplay.setVisible(false);
		narrationConatainer.setVisible(false);
		setEditMode(true);
		
		lblCharLimit.setVisible(true);
		resourceNarrationHtml.getElement().getStyle().setWidth(230, Unit.PX);
	}
	/*
	 * This clickEvent is used to update pdf start page
	 */
	@UiHandler("updatePdfBtn")
	public void updatePdfBtnClick(ClickEvent clickEvent) {
		updatePdfStartPage();
		
	}
	/*
	 * This clickEvent is used to cancel the pdf edit
	 */
	@UiHandler("cancelpdfBtn")
	public void cancelpdfBtnClick(ClickEvent clickEvent) {
		fromLblDisplayText.setVisible(true);
		errorMsgLabel.setText("");
		errorMsgLabelForPDF.setText("");
		videoDisplay.setVisible(true);
		actionVerPanelForUpdatePDF.setVisible(false);
		editPdfFlowPanel.setVisible(false);
		narrationConatainer.setVisible(true);
		startpdfPageNumber.setFocus(true);
		editFloPanel.setVisible(false);
		isEdited=false;
		disableAllEditMode();
		lblCharLimit.setVisible(false);
		resourceNarrationHtml.getElement().getStyle().clearWidth();
	}
	
	/**
	 * Add or update narration for the collection resource
	 * 
	 * @param clickEvent
	 *            instance of {@link ClickEvent}
	 */
	// @UiHandler("resourceNarrationHtml")
	// public void onEditNarationClick(ClickEvent clickEvent) {
	// editAndUpdateResource();
	// }

	@UiHandler("pencilEditNarationLbl")
	public void onPencilNarationClick(ClickEvent clickEvent) {
		editAndUpdateResource();
	}

	/**
	 * Copy collection item
	 * 
	 * @param clickEvent
	 *            instance of {@link ClickEvent}
	 */
	@UiHandler("copyResource")
	public void copyResource(ClickEvent clickEvent) {
		ResourceEditButtonContainer.setVisible(false);
//		ResourceEditButtonContainer.getElement().getStyle().setVisibility(Visibility.HIDDEN);
		EditBtn.setVisible(false);
		/*reorderContainer.setVisible(false);*/
		if (isEdited) {
				if (previousCollectionResourceChildView != null	&& previousCollectionResourceChildView.isAttached()) {
				if (!isConfirmationPopup) {
					isConfirmationPopup = true;
					new ConfirmationPopupVc(OOPS, EDIT_CONFIRM) {

						@Override
						@UiHandler("okButton")
						public void onDelete(ClickEvent clickEvent) {
							hide();
							isConfirmationPopup = false;
							clickEvent.stopPropagation();
						}

						@Override
						@UiHandler("cancelButton")
						public void onCancelClick(ClickEvent clickEvent) {
							disableAllEditMode();
							hide();
							isEdited = false;
							isConfirmationPopup = false;
							clickEvent.stopPropagation();
						}
					};
				}
			}
		} else {
			disableAllEditMode();
			copyConfirmPopupVc = new CopyConfirmPopupVc() {
				@Override
				public void populateUserCollections() {
					pageNumber=1;
					getPresenter().getUserColletionsList(pageSize,pageNumber);
				}
				public void copyResourceToCollection(String collectionId) {
					disableAllEditMode();
					selectedCollectionId=collectionId;
					getPresenter().createCollectionItem(collectionId,collectionItemDo.getCollectionItemId());
				}

				@Override
				public void getFolderInnerItems(TreeItem item, String parentId) {
					getPresenter().getFolderItems(item, parentId);
				}

				@Override
				public void getWorkspaceData(int offset, int limit,boolean clearShelfPanel) {
					getPresenter().getWorkspaceData(offset, limit,clearShelfPanel);
					
				}

			};
		}
	}

	
	/**
	 * Confirmation popup for collection item delete, delete collection item
	 * regarding the popup action
	 * 
	 * @param clickEvent
	 *            instance of {@link ClickEvent}
	 */
	@UiHandler("confirmDeleteLbl")
	public void deleteCollectionItem(ClickEvent clickEvent) {
		ResourceEditButtonContainer.setVisible(false);
//		ResourceEditButtonContainer.getElement().getStyle().setVisibility(Visibility.HIDDEN);
		EditBtn.setVisible(false);
		/*reorderContainer.setVisible(false);*/
		Window.enableScrolling(false);
        AppClientFactory.fireEvent(new SetHeaderZIndexEvent(88, false));

		deleteConfirmationPopupVc = new ConfirmationPopupVc(MESSAGE_HEADER,
				MESSAGE_CONTENT) {

			@Override
			public void onDelete(ClickEvent clickEvent) {
				getPresenter().deleteCollectionItem(
						collectionItemDo.getCollectionItemId());

			}
		};

	}
	/*
	 * This clickEvent is used to update pdf
	 */
	public void updatePdfStartPage()
 {
		String start = startpdfPageNumber.getText();
		String enteredStopPage = stoppdfPageNumber.getText();
		boolean isValid = this.validatePDF(start, enteredStopPage, totalPages);
		if (isValid) {
			MixpanelUtil.Organize_Click_Edit_Start_Page_Update();
			errorMsgLabel.setText("");
			errorMsgLabelForPDF.setText("");
			actionVerPanelForUpdatePDF.setVisible(false);
			editFloPanel.setVisible(false);
			editPdfFlowPanel.setVisible(false);
			actionVerPanelForUpdateTime.setVisible(false);
			fromLblDisplayText.setVisible(true);
			videoDisplay.setVisible(true);
			narrationConatainer.setVisible(true);
			UpdateTextMessage.setVisible(true);
			actionVerPanel.setVisible(false);

			// collectionItemDo.setStart(start);
			getPresenter().updateCollectionItem(
					collectionItemDo.getCollectionItemId(), narrationData,
					start, enteredStopPage);
			isEdited = false;
		}
	}
	/*
	 * This clickEvent is used to update video
	 */
	public void editAndUpdateVideoTime()
 {

		MixpanelUtil.Organize_Click_Edit_Start_Time_Update();
		errorMsgLabel.setText("");
		actionVerPanelForUpdateTime.setVisible(false);
		actionVerPanelForUpdatePDF.setVisible(false);
		actionVerPanel.setVisible(false);
		editFloPanel.setVisible(false);
		UpdateTextMessage.setVisible(true);
		// videoDisplay.setVisible(true);
		narrationConatainer.setVisible(true);
		editFieldsFloPanel.setVisible(false);

		String narration = null;
		String from = null;
		String to = null;
		if((fromTxt.getText().trim().length()>0)&&(toTxt.getText().trim().length()>0)&&(EndTimeTxt1.getText().trim().length()>0)&&(EndTimeTxt2.getText().trim().length()>0)){
			if (collectionItemDo.getResource().getResourceType().getName()
					.equalsIgnoreCase("video/youtube")) {
				from = FROM_START_TIME;
				to = FROM_STOP_TIME;
			}
			/*
			 * if (resourceNarrationHtml.getHTML().length() > 0) { narration =
			 * narrationTxtArea.getHTML(); collectionItemDo.setNarration(narration);
			 * }
			 */
			if (fromTxt.getText().length() > 0 && toTxt.getText().length() > 0) {
				from = fromTxt.getText();
				fromTxt.setText(from);
				fromTxt.getElement().setAttribute("alt", from);
				fromTxt.getElement().setAttribute("title", from);
				from = toTxt.getText();
				toTxt.setText(from);
				toTxt.getElement().setAttribute("alt", from);
				toTxt.getElement().setAttribute("title", from);
				String startTimeTxtMin = null;
				String startTimeTxtSec = null;
				if (fromTxt.getText().length() < 2) {
					startTimeTxtMin = "0" + fromTxt.getText();
	
				} else {
					startTimeTxtMin = fromTxt.getText();
				}
				if (toTxt.getText().length() < 2) {
					startTimeTxtSec = "0" + toTxt.getText();
				} else {
					startTimeTxtSec = toTxt.getText();
				}
				from = "00:" + startTimeTxtMin + ":" + startTimeTxtSec;
				// collectionItemDo.setStart(from);
	
			}
			if (EndTimeTxt1.getText().length() > 0
					&& EndTimeTxt2.getText().length() > 0) {
				to = EndTimeTxt1.getText();
				EndTimeTxt1.setText(to);
				EndTimeTxt1.getElement().setAttribute("alt", to);
				EndTimeTxt1.getElement().setAttribute("title", to);
				to = EndTimeTxt2.getText();
				EndTimeTxt2.setText(to);
				EndTimeTxt2.getElement().setAttribute("alt", to);
				EndTimeTxt2.getElement().setAttribute("title", to);
				String EndTimeTxtMin = null;
				String EndTimeTxtSec = null;
				if (EndTimeTxt1.getText().length() < 2) {
					EndTimeTxtMin = "0" + EndTimeTxt1.getText();
	
				} else {
					EndTimeTxtMin = EndTimeTxt1.getText();
				}
				if (EndTimeTxt2.getText().length() < 2) {
					EndTimeTxtSec = "0" + EndTimeTxt2.getText();
				} else {
					EndTimeTxtSec = EndTimeTxt2.getText();
				}
				to = "00:" + EndTimeTxtMin + ":" + EndTimeTxtSec;
				// collectionItemDo.setStop(to);
			}
	
			if ((collectionItemDo.getResource().getResourceType().getName()
					.equalsIgnoreCase("video/youtube"))
					&& (!from.equals(FROM_START_TIME) || !to.equals(FROM_STOP_TIME))) {
				this.youtubeValidation(narration, from, to);
			}else {
	
				getPresenter().updateCollectionItem(
						collectionItemDo.getCollectionItemId(), narrationData,
						from, to);
				// setEditMode(false);
				lblCharLimit.setVisible(false);
				resourceNarrationHtml.getElement().getStyle().clearWidth();
			}
			isEdited = false;
		}else
		{
			actionVerPanelForUpdateTime.setVisible(true);
			videoDisplay.setVisible(false);
			narrationConatainer.setVisible(false);
			editFieldsFloPanel.setVisible(true);
			ResourceEditButtonContainer.setVisible(false);
//			ResourceEditButtonContainer.getElement().getStyle().setVisibility(Visibility.HIDDEN);
			fromTxt.setFocus(true);
			UpdateTextMessage.setVisible(false);		
			errorMsgLabel.setText("");
			errorMsgLabel.setText(YOUTUBE_START_END_TIME);
		}
	}
	/**
	 * Update the collection item meta data
	 * 
	 */
	public void editAndUpdateResource() {
		//	if (updateResourceBtn.getText().equalsIgnoreCase("Edit Narration")) {
		videoDisplay.setVisible(false);
		editFieldsFloPanel.setVisible(false);
			if (collectionItemDo.getNarration() != null) {
				narrationTxtArea.setText(collectionItemDo.getNarration());
				narrationTxtArea.getElement().setAttribute("alt", collectionItemDo.getNarration());
				narrationTxtArea.getElement().setAttribute("title", collectionItemDo.getNarration());
			}
			resourceNarrationHtml.getElement().getStyle().setWidth(230, Unit.PX);
			resourceNarrationHtml.setHTML(ADD_NARRATION_FOR_YOUR_VIEWERS);
			String value = StringUtil.generateMessage(i18n.GL2103(), "500");
			lblCharLimit.setText(value);
			lblCharLimit.setVisible(true);
			resourceNarrationHtml.getElement().setAttribute("alt", ADD_NARRATION_FOR_YOUR_VIEWERS);
			resourceNarrationHtml.getElement().setAttribute("title", ADD_NARRATION_FOR_YOUR_VIEWERS);
			setEditMode(true);

	}
	/*
	 * This clickEvent is used to upadte narration
	 */
	@UiHandler("updateNarrationBtn")
	public void onclickOfnarrationUpdate(ClickEvent event)
	{
		Map<String, String> parms = new HashMap<String, String>();
		parms.put("text", narrationTxtArea.getText());
		AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean value) {
				isHavingBadWords = value;
				if (value){
					narrationAlertMessageLbl.addStyleName("narrationTxtArea");
					narrationAlertMessageLbl
							.removeStyleName("titleAlertMessageDeActive");
					narrationAlertMessageLbl
							.addStyleName("titleAlertMessageActive");
					
					
					narrationTxtArea.getElement().getStyle().setBorderColor("orange");
					narrationAlertMessageLbl.setText(i18n.GL0554());
					
					narrationAlertMessageLbl.getElement().setAttribute("alt", i18n.GL0554());
					narrationAlertMessageLbl.getElement().setAttribute("title", i18n.GL0554());
					narrationAlertMessageLbl.setVisible(true);
					
					MixpanelUtil.mixpanelEvent("Collaborator_edits_collection");
				}else{
//					if(narrationDataLength<=600){
						String narration = null;
						actionVerPanel.setVisible(false);
						actionVerPanelForUpdateTime.setVisible(false);
						actionVerPanelForUpdatePDF.setVisible(false);
						MixpanelUtil.Organize_Click_Edit_Narration_Update();
						UpdateTextMessage.setVisible(true);
						if (resourceNarrationHtml.getHTML().length() > 0) {
							//narration = narrationTxtArea.getRawContent();
							narration = trim(narrationTxtArea.getText());
							collectionItemDo.setNarration(narration);
						}
					
						try{
							getPresenter().updateNarrationItem(
									collectionItemDo.getCollectionItemId(), narration);
						}catch(Exception e){
							e.printStackTrace();
						}
						isEdited = false;
						lblCharLimit.setVisible(false);
						resourceNarrationHtml.getElement().getStyle().clearWidth();
//					}
					
				}
			}
		});
		
		
		
		
		
	}
	/*
	 * This clickEvent is used to cancel narration edit
	 */
	@UiHandler("cancelNarrationBtn")
	public void onclickcancelNarrationBtn(ClickEvent event){
		narrationTxtArea.setText(collectionItemDo.getNarration());
		narrationTxtArea.getElement().setAttribute("alt", collectionItemDo.getNarration());
		narrationTxtArea.getElement().setAttribute("title", collectionItemDo.getNarration());
		fromLblDisplayText.setVisible(true);
		videoDisplay.setVisible(true);
		setEditMode(false);
		isEdited = false;
		disableAllEditMode();
		lblCharLimit.setVisible(false);
		resourceNarrationHtml.getElement().getStyle().clearWidth();
	}
	/*
	 * This clickEvent is used to edit video time
	 */
	@UiHandler("editVideoTimeLbl")
	public void onclickEditVideoTimeLbl(ClickEvent event)
	{
		
		if (youtube) {
			lblCharLimit.setVisible(true);
			resourceNarrationHtml.getElement().getStyle().setWidth(230, Unit.PX);
			MixpanelUtil.Organize_Click_Edit_Start_Time();
			EditBtn.setVisible(false);
			/*reorderContainer.setVisible(false);*/
			actionVerPanelForUpdateTime.setVisible(true);
			videoDisplay.setVisible(false);
			narrationConatainer.setVisible(false);
			editFieldsFloPanel.setVisible(true);
			ResourceEditButtonContainer.setVisible(false);
			/*ResourceEditButtonContainer.getElement().getStyle()
					.setVisibility(Visibility.HIDDEN);*/
			fromTxt.setFocus(true);
			setEditMode(true);

		}
		
	}
	/*
	 * This clickEvent is used to update video time 
	 */
	@UiHandler("updateVideoTimeBtn")
	public void updateVideoTimeBtnClickEvent(ClickEvent event)
	{
		
		editAndUpdateVideoTime();
	}
	/*
	 * This clickEvent is used to cancel video time edit
	 */
	@UiHandler("cancelVideoTimeBtn")
	public void cancelVideoTimeBtnClickEvent(ClickEvent event)
	{
		errorMsgLabel.setText("");
		videoDisplay.setVisible(true);
		narrationConatainer.setVisible(true);
		editFieldsFloPanel.setVisible(false);
		actionVerPanelForUpdateTime.setVisible(false);
		editFloPanel.setVisible(false);
		isEdited = false;
		disableAllEditMode();
		lblCharLimit.setVisible(false);
		resourceNarrationHtml.getElement().getStyle().clearWidth();
	}

	/**
	 * Reorder the collection item with new new sequence
	 * @param shelfCollectionResourceChildView 
	 * 
	 * @param newSequence
	 *            reorder the collection item
	 * @param arrow 
	 */
	public void reorderCollectionItem(/*ShelfCollectionResourceChildView shelfCollectionResourceChildView,*/ Integer newSequence/*, String arrow*/) { 
		collectionItemDo.setItemSequence(newSequence);
		/**
		 * enabled again by removing move option
		 */
		EditBtn.setVisible(false);
		getPresenter().reorderCollectionItem(collectionItemDo);
		new Timer() {

			@Override
			public void run() {
				//remove in 5.9 sprint
				//setEditMode(false);
			}
		}.schedule(2000);
		
		
		/**
		 * Added new method in order to implement reordering of resources.
		 * (Commented the following method to enable DnD)
		 * 
		 */
		
//		getPresenter().reorderMyCollectionItem(collectionItemDo,shelfCollectionResourceChildView,arrow,newSequence);
		
		
	}

	@Override
	public Widget getDragHandle() {
		// The whole widget acts as drag widget
		return null;
	}

	@Override
	public IsDraggableMirage initDraggableMirage() {
		String resourceFormat = collectionItemDo.getResource().getResourceFormat() !=null ? collectionItemDo.getResource().getResourceFormat().getDisplayName() : "text";
		return new ResourceDragUc(resourceFormat,collectionItemDo.getResource().getTitle());
	}

	@Override
	public void onDragBlur() {
		// Nothing
	}

	@Override
	public String getDragId() {
		return collectionItemDo.getCollectionItemId();
	}

	public CollectionItemDo getCollectionItemDo() {
		return collectionItemDo;
	}
	
	@Override
	public DRAG_TYPE getDragType() {
		return DRAG_TYPE.COLLECTION_ITEM;
	}

	@Override
	public int getDragTopCorrection() {
		return 5;
	}

	@Override
	public int getDragLeftCorrection() {
		return 225;
	}

	/**
	 * Following method again enabled, as drag and drop is not removed for resource reorder at My Collections.
	 */
	@Override
	public void onPostReorder(CollectionItemDo collectionItemDo) {
		collectionItemDo.setCollection(this.collectionItemDo.getCollection());
		this.collectionItemDo.setItemSequence(collectionItemDo.getItemSequence());
		AppClientFactory.fireEvent(new RefreshCollectionItemInShelfListEvent(collectionItemDo, RefreshType.UPDATE));

	}

	@Override
	public void onPostUpdate(CollectionItemDo collectionItemDo) {
		// FIXME Bring all data
		this.collectionItemDo.setNarration(collectionItemDo.getNarration());
		this.collectionItemDo.setStart(collectionItemDo.getStart());
		this.collectionItemDo.setStop(collectionItemDo.getStop());
		//this.collectionItemDo.setStop(collectionItemDo.getAverageTime());
		setEditMode(false);
		lblCharLimit.setVisible(false);
		resourceNarrationHtml.getElement().getStyle().clearWidth();
	}

	/**
	 * Collection item update label view , update or edit text
	 * 
	 * @param editMode
	 *            update or edit text view
	 */
	public void setEditMode(boolean editMode) {
	/*	if ((editMode && updateResourceBtn.getText().equalsIgnoreCase(
				"Edit Narration"))
				|| (!editMode && updateResourceBtn.getText().equalsIgnoreCase(
						"Update"))) {
	*/		
		
			if (editMode && previousCollectionResourceChildView != null
					&& previousCollectionResourceChildView.isAttached()) {
				previousCollectionResourceChildView.setEditMode(false);
				previousCollectionResourceChildView = null;
			}
			actionVerPanel.setVisible(editMode);
			previousCollectionResourceChildView = editMode ? this : null;
			editFloPanel.setVisible(editMode);
			
			if (editMode) {
				addStyleName(CollectionEditResourceCBundle.INSTANCE.css()
						.collectionResourceEdit());
			} else {
				setData(this.collectionItemDo);
				removeStyleName(CollectionEditResourceCBundle.INSTANCE.css().collectionResourceEdit());
			}
			//updateResourceBtn.setText(editMode ? "Update" : "Edit Narration");
			//updateResourceBtn.setEnabled(true);
			collectionResourceTabView.setEditMode(editMode, this);
		}
	//}

	@Override
	public void onPostDelete() {
		deleteConfirmationPopupVc.hide();

		collectionResourceTabView.removeCollectionItem(collectionItemDo, this);
		AppClientFactory.fireEvent(new RefreshCollectionItemInShelfListEvent(
				collectionItemDo, RefreshType.DELETE));
		if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.SHELF) || AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.COLLECTION_SEARCH) || AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)){
			Window.enableScrolling(false);
		}else{
			Window.enableScrolling(true);
		}
        AppClientFactory.fireEvent(new SetHeaderZIndexEvent(0, true));


	}

	@Override
	public void onPostCopy(CollectionItemDo collectionItem) {
		copyConfirmPopupVc.hide();
		if(selectedCollectionId==AppClientFactory.getPlaceManager().getRequestParameter("id")) {
			AppClientFactory.fireEvent(new InsertCollectionItemInAddResourceEvent(
					collectionItem, RefreshType.INSERT));
		}
		if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.COLLECTION_SEARCH) || AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)){
			Window.enableScrolling(false);
		}else{
			Window.enableScrolling(true);
		}
        AppClientFactory.fireEvent(new SetHeaderZIndexEvent(0, true));
	
	}

	@Override
	public void onPutUpdate() {

		// collectionResourceTabView.insertColectionItem(collectionItemDo,true);
		editQuestionPopupVc.hide();
		// isCollResourceTabView.updateData();
		AppClientFactory.fireEvent(new RefreshCollectionItemInShelfListEvent(
				collectionItemDo, RefreshType.UPDATE));

	}

	public void onResourceNarrationHover() {
		pencilEditNarationLbl
				.setStyleName(CollectionEditResourceCBundle.INSTANCE.css()
						.pencilEditImage());
		pencilEditNarationLbl
				.addStyleName(CollectionEditResourceCBundle.INSTANCE.css()
						.pencilAllign());
	}

	public void onResourceNarrationOut() {
		pencilEditNarationLbl
				.setStyleName(CollectionEditResourceCBundle.INSTANCE.css()
						.pencilImageHide());
		pencilEditNarationLbl
				.removeStyleName(CollectionEditResourceCBundle.INSTANCE.css()
						.pencilAllign());
	}
/*
 * This method is used to validate video time
 */
	public void youtubeValidation(final String narration, final String start,
			final String stop) {
		RegExp regExp = RegExp.compile(REG_EXP);
		if (!(regExp.test(start)) || !(regExp.test(stop))) {
			UpdateTextMessage.setVisible(false);
			errorMsgLabel.setText("");
			errorMsgLabel.setText(YOUTUBE_START_END_TIME);
			//new AlertContentUc(i18n.GL0061(), YOUTUBE_START_END_TIME);
			return;
		}
		String videoId = ResourceImageUtil.getYoutubeVideoId(collectionItemDo
				.getResource().getUrl());
		if (videoId != null) {
			
			AppClientFactory.getInjector().getResourceService()
					.getYoutubeDuration(videoId,
							new SimpleAsyncCallback<String>() {
								@Override
								public void onSuccess(String youtubeInfo) {
									int totalVideoLengthInMin;
									if(youtubeInfo!=null){
									
									totalVideoLengthInMin = Integer
											.parseInt(youtubeInfo);
									
									String[] startSplitTimeHours = start
											.split(":");
									String[] endSplitTimeHours = stop
											.split(":");
									Integer startTimeInSeconds = (Integer
											.parseInt(startSplitTimeHours[0]) * 3600)
											+ (Integer
													.parseInt(startSplitTimeHours[1]) * 60)
											+ (Integer
													.parseInt(startSplitTimeHours[2]));
									Integer endTimeInSeconds = (Integer
											.parseInt(endSplitTimeHours[0]) * 3600)
											+ (Integer
													.parseInt(endSplitTimeHours[1]) * 60)
											+ (Integer
													.parseInt(endSplitTimeHours[2]));
									if (startTimeInSeconds < endTimeInSeconds
											&& startTimeInSeconds <= totalVideoLengthInMin
											&& endTimeInSeconds <= totalVideoLengthInMin
											|| startTimeInSeconds <= totalVideoLengthInMin
											&& endTimeInSeconds == 0
											|| endTimeInSeconds <= totalVideoLengthInMin
											&& startTimeInSeconds == 0
											|| endTimeInSeconds == 0
											&& startTimeInSeconds == 0) {
										getPresenter().updateCollectionItem(
												collectionItemDo
														.getCollectionItemId(),
														narrationData, start, stop);
										///setEditMode(false);
									} else {
										actionVerPanelForUpdateTime.setVisible(true);
										videoDisplay.setVisible(false);
										narrationConatainer.setVisible(false);
										editFieldsFloPanel.setVisible(true);
										ResourceEditButtonContainer.setVisible(false);
//										ResourceEditButtonContainer.getElement().getStyle().setVisibility(Visibility.HIDDEN);
										fromTxt.setFocus(true);
										UpdateTextMessage.setVisible(false);		
										errorMsgLabel.setText("");
										errorMsgLabel.setText(VALID_START_STOP_TIME);
										//setEditMode(true);
										/*new AlertContentUc(i18n.GL0061(),
												VALID_START_STOP_TIME);*/
									}
								}
								}
							});
		}
		else
		{
			UpdateTextMessage.setVisible(false);
		}
	}

	public boolean validatePDF(String startpage,String stopPage,Integer totalPage){
		boolean isValid;
		Integer enteredStopPage =	Integer.parseInt(stopPage);
		Integer startpdfpage = Integer.parseInt(startpage);
		if(enteredStopPage > totalPage){
			errorMsgLabelForPDF.setText(VALID_END_PAGE);
			isValid = false;
		}else if( startpdfpage > totalPage){
			errorMsgLabelForPDF.setText(VALID_END_PAGE);
			isValid = false;	
		}
		else if( enteredStopPage < startpdfpage){
			errorMsgLabelForPDF.setText(VALID_END_PAGE);
			isValid = false;	
		}
		else
		{
			isValid = true;
			errorMsgLabelForPDF.setText("");
		}
		return isValid;
	}
	@Override
	public void onPostUserCollections(List<CollectionDo> result) {
//		int count = 0;
//		totalSize=result.size();
//		if(result.size()==0){
//			copyConfirmPopupVc.copyLabel.setVisible(true);
//			copyConfirmPopupVc.btnCancel.setVisible(true);
//			copyConfirmPopupVc.loadingTextLbl.setVisible(false);
//			}
//		collectionItems = new ArrayList<Integer>();
//		String collectionTitle=ShelfView.getCollectionTitle();
//		
//		for (CollectionDo collection : result) {
//			if (!collection.getCollectionType().toString().trim().equalsIgnoreCase("folder")){
//				copyConfirmPopupVc.resourceFirstElement.getElement().setAttribute("style", "padding-left:5px");
//				if(collectionTitle.length()>=40) {
//					copyConfirmPopupVc.resourceFirstElement.setText(collectionTitle.substring(0, 40)+"...");
//				}else{
//					copyConfirmPopupVc.resourceFirstElement.setText(collectionTitle);
//				}
//					
//				final Label titleLabel = new Label(collection.getTitle());
//				titleLabel.setStyleName(CollectionEditResourceCBundle.INSTANCE
//						.css().copyPopUpResourceListBoxText());
//				titleLabel.getElement()
//						.setAttribute("id", collection.getGooruOid());
//						copyConfirmPopupVc.htmlScrollPanel.add(titleLabel);
//				//copyConfirmPopupVc.copyPopUpScrollHtmlPanel.add(ScrPanel);
//	
//				// copyConfirmPopupVc.copyPopupListBox.setVisibleItemCount(count);
//				// copyConfirmPopupVc.copyPopupListBox.setValue(count,
//				// collection.getGooruOid());
//						
//				count++;
//				copyConfirmPopupVc.loadingTextLbl.setVisible(false);
//				copyConfirmPopupVc.copyLabel.setVisible(true);
//				copyConfirmPopupVc.btnCancel.setVisible(true);
//				
//				int collectionItemDoSize = collection.getCollectionItems().size();
//				
//				collectionItems.add(collectionItemDoSize);
//				copyConfirmPopupVc.setCollectionItemDoSize(collectionItems);
//	
//				collectionListCount.put(collection.getGooruOid(), collectionItemDoSize);
//				titleLabel.addClickHandler(new ClickHandler() {
//	
//					@Override
//					public void onClick(ClickEvent event) {
//						if(titleLabel.getText().length()>=40) {
//							copyConfirmPopupVc.resourceFirstElement.setText(titleLabel
//									.getText().substring(0, 40)+"...");
//							copyConfirmPopupVc.resourceFirstElement.getElement()
//									.setAttribute("id",
//											titleLabel.getElement().getAttribute("id"));
//						}
//						else
//						{
//							copyConfirmPopupVc.resourceFirstElement.setText(titleLabel
//									.getText());
//							copyConfirmPopupVc.resourceFirstElement.getElement()
//									.setAttribute("id",
//											titleLabel.getElement().getAttribute("id"));
//					
//						}
//						copyConfirmPopupVc.copyPopUpScrollHtmlPanel.getElement().getStyle().setVisibility(Visibility.HIDDEN);
//						collId = copyConfirmPopupVc.resourceFirstElement.getElement().getId();
//					}
//				});
//			}
//
//		}
//		// Set current collection as selected
//		for (int i = 0; i < result.size(); i++) {
//			if (result.get(i).getGooruOid()
//					.equalsIgnoreCase(
//							collectionItemDo.getCollection().getGooruOid())) {
//				// copyConfirmPopupVc.resourceFirstElement.getText()
//				collId = collectionItemDo.getCollection().getGooruOid();
//				break;
//				
//			}
//		}
//		
//		copyConfirmPopupVc.copyPopUpScrollHtmlPanel.addScrollHandler(new ScrollHandler() {
//
//			@Override
//			public void onScroll(ScrollEvent event) {
//				
//			if (copyConfirmPopupVc.copyPopUpScrollHtmlPanel.getVerticalScrollPosition() == copyConfirmPopupVc.copyPopUpScrollHtmlPanel.getMaximumVerticalScrollPosition() && totalSize >=20)
//				 {
////				copyConfirmPopupVc.copyLabel.setVisible(false);
////				copyConfirmPopupVc.btnCancel.setVisible(false);
//				copyConfirmPopupVc.loadingTextLbl.setVisible(true);
//			
//				
//				pageNumber=pageNumber+1;
//				getPresenter().getUserColletionsList(pageSize,pageNumber);
//					
//				}
//
//			}
//		});
		
	}
	/**
	 * This method is used to trim the text of rich text box.
	 * @function trim 
	 * 
	 * @created_date : 20-Jan-2014
	 * 
	 * @description :
	 * 
	 * 
	 * @parm(s) : @param s
	 * @parm(s) : @return
	 * 
	 * @return : String
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	public String trim(String s) {

		   String result = s.trim(); 
		   String x = result.replaceAll("<br>", "");
		   x = x.replaceAll("&nbsp;", "");
		   x = x.trim();
		   if(x.equals("")) {
		       return x;
		   } else {
		       return result;
		   }
		}
	/**
	 * Highlight the New collection item
	 */
	@Override
	public void addNewResource() {
		//resourceFlowPanel.setStyleName(res.css().addNewResourceBackgroudColor());
		new BackgroundColorEffect(resourceFlowPanel.getElement(),"#E7F1F8" ,"white", 5000);
	}
	

	/*native void redirect()
	-{
	        $wnd.location.reload();
	}-;*/

	@Override
	public Label getVisible() {
		return UpdateTextMessage;
		
	}
	@Override
	public void displayWorkspaceData(FolderListDo folderListDo,boolean clearShelfPanel) {
		copyConfirmPopupVc.displayWorkspaceData(folderListDo,clearShelfPanel);
		
	}
	@Override
	public void setFolderItems(TreeItem item, FolderListDo folderListDo) {
		copyConfirmPopupVc.setFolderItems(item,folderListDo);
	}
	private String removeHtmlTags(String html){
		html = html.replaceAll("(<\\w+)[^>]*(>)", "$1$2");
        html = html.replaceAll("</p>", " ").replaceAll("<p>", "").replaceAll("<br data-mce-bogus=\"1\">", "").replaceAll("<br>", "").replaceAll("</br>", "").replaceAll("<p class=\"p1\">", "");
        return html;
	}
	
	public void setUpDownArrowVisibility(int totalCount) { 
		
	}
	
	
	/**
	 * Sets the re-order Up button visibility
	 * @param isvisible {@link Boolean}
	 */
	/*public void upButtonIsVisible(boolean isvisible) {
		
		if(isvisible){
			resourceMoveUpBtn.getElement().getStyle().setVisibility(Visibility.VISIBLE);
		}else{
			resourceMoveUpBtn.getElement().getStyle().setVisibility(Visibility.HIDDEN);
		}
	}*/


	/**
	 * Sets the re-order Down button visibility
	 * @param isvisible {@link Boolean}
	 */
	/*public void downButtonIsVisible(boolean isvisible) {
		if(isvisible){
			resourceMoveDownBtn.getElement().getStyle().setVisibility(Visibility.VISIBLE);
		}else{
			resourceMoveDownBtn.getElement().getStyle().setVisibility(Visibility.HIDDEN);
		}
	}*/
	
	
	/*public void showReorderValidationToolTip(String validationMsg){
		toolTipPopupPanel.clear();
		globalToolTip = new GlobalToolTip(validationMsg);
		globalToolTip.confirmationPanel.getElement().getStyle().setWidth(155, Unit.PX);
		toolTipPopupPanel.setWidget(globalToolTip);
		toolTipPopupPanel.setStyleName("");
		toolTipPopupPanel.setPopupPosition(reorderTxtBox.getElement().getAbsoluteLeft()+160, reorderTxtBox.getElement().getAbsoluteTop()-70);
		toolTipPopupPanel.getElement().getStyle().setZIndex(9999);
		toolTipPopupPanel.show();
		new FadeInAndOut(toolTipPopupPanel.getElement(), 10200);
	}*/
	
	
	public void reorderCollectionResources(ShelfCollectionResourceChildView shelfCollectionResourceChildView, int itemToBeMovedPosSeqNumb, String arrow) { 
//		reorderCollectionItem(shelfCollectionResourceChildView,itemToBeMovedPosSeqNumb,arrow);
	}
	
	@Override
	public void onPostResourceReorder(CollectionItemDo collectionItemDo,ShelfCollectionResourceChildView shelfCollectionResourceChildView,String arrow,Integer newSequence) {
		collectionItemDo.setCollection(this.collectionItemDo.getCollection());
		this.collectionItemDo.setItemSequence(collectionItemDo.getItemSequence());
		AppClientFactory.fireEvent(new RefreshCollectionItemInShelfListEvent(collectionItemDo, RefreshType.UPDATE));
		
		/*AppClientFactory.fireEvent(new ReorderCollectionResourcesEvent(shelfCollectionResourceChildView,arrow,newSequence));*/
	}

	
/*	
	Commented the following to enable DnD again, for hot fix
	
	public void setReorderContainerVisibility(boolean isEnable) { 
		isReorderContainerVisible = isEnable;
	}
	
	
	*//**
	 * @return the reorderTxtBox
	 *//*
	public TextBox getReorderTxtBox() {
		return reorderTxtBox;
	}
	*//**
	 * @param reorderTxtBox the reorderTxtBox to set
	 *//*
	public void setReorderTxtBox(TextBox reorderTxtBox) {
		this.reorderTxtBox = reorderTxtBox;
	}
	*//**
	 * @return the resourceMoveUpBtn
	 *//*
	public Button getResourceMoveUpBtn() {
		return resourceMoveUpBtn;
	}
	*//**
	 * @param resourceMoveUpBtn the resourceMoveUpBtn to set
	 *//*
	public void setResourceMoveUpBtn(Button resourceMoveUpBtn) {
		this.resourceMoveUpBtn = resourceMoveUpBtn;
	}
	*//**
	 * @return the resourceMoveDownBtn
	 *//*
	public Button getResourceMoveDownBtn() {
		return resourceMoveDownBtn;
	}
	*//**
	 * @param resourceMoveDownBtn the resourceMoveDownBtn to set
	 *//*
	public void setResourceMoveDownBtn(Button resourceMoveDownBtn) {
		this.resourceMoveDownBtn = resourceMoveDownBtn;
	}
	*//**
	 * @return the reorderContainer
	 *//*
	public HTMLPanel getReorderContainer() {
		return reorderContainer;
	}
	*//**
	 * @param reorderContainer the reorderContainer to set
	 *//*
	public void setReorderContainer(HTMLPanel reorderContainer) {
		this.reorderContainer = reorderContainer;
	}
*/	
}
