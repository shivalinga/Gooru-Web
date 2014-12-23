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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BaseViewWithHandlers;
import org.ednovo.gooru.client.mvp.folders.FoldersWelcomePage;
import org.ednovo.gooru.client.mvp.folders.event.RefreshFolderType;
import org.ednovo.gooru.client.mvp.home.FooterOrganizeUc;
import org.ednovo.gooru.client.mvp.search.event.DisplayNoCollectionEvent;
import org.ednovo.gooru.client.mvp.search.event.DisplayNoCollectionHandler;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderZIndexEvent;
import org.ednovo.gooru.client.mvp.shelf.collection.CollectionCBundle;
import org.ednovo.gooru.client.mvp.shelf.collection.CollectionCollaboratorTabVc;
import org.ednovo.gooru.client.mvp.shelf.collection.CollectionShareTabVc;
import org.ednovo.gooru.client.mvp.shelf.collection.CollectionStatisticsTabVc;
import org.ednovo.gooru.client.mvp.shelf.collection.CollectionTabTitleVc;
import org.ednovo.gooru.client.mvp.shelf.collection.CollectionUploadImageUc;
import org.ednovo.gooru.client.mvp.shelf.collection.folders.events.InsertMovedCollectionEvent;
import org.ednovo.gooru.client.mvp.shelf.collection.folders.events.RemoveMovedCollectionFolderEvent;
import org.ednovo.gooru.client.mvp.shelf.collection.folders.events.SetCollectionMovedStyleEvent;
import org.ednovo.gooru.client.mvp.shelf.collection.folders.uc.DeleteFolderSuccessView;
import org.ednovo.gooru.client.mvp.shelf.collection.folders.uc.FolderPopupUc;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.collaborators.vc.DeletePopupViewVc;
import org.ednovo.gooru.client.mvp.shelf.event.RefreshCollectionInShelfListEvent;
import org.ednovo.gooru.client.mvp.shelf.event.RefreshType;
import org.ednovo.gooru.client.mvp.socialshare.event.UpdateSocialShareMetaDataEvent;
import org.ednovo.gooru.client.uc.BalloonPopupVc;
import org.ednovo.gooru.client.uc.CollectionAnalyticsUc;
import org.ednovo.gooru.client.uc.EditableLabelUc;
import org.ednovo.gooru.client.uc.EditableTextAreaUc;
import org.ednovo.gooru.client.uc.tooltip.ToolTip;
import org.ednovo.gooru.client.ui.HTMLEventPanel;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.content.ClassPageCollectionDo;
import org.ednovo.gooru.shared.model.content.CollectionDo;
import org.ednovo.gooru.shared.model.content.CollectionItemDo;
import org.ednovo.gooru.shared.model.content.ResourceFormatDo;
import org.ednovo.gooru.shared.model.content.ThumbnailDo;
import org.ednovo.gooru.shared.model.folder.FolderDo;
import org.ednovo.gooru.shared.model.folder.FolderItemDo;
import org.ednovo.gooru.shared.model.user.SettingDo;
import org.ednovo.gooru.shared.util.StringUtil;
import org.ednovo.gooru.shared.util.UAgentInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Navigator;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;


/**
 * @fileName : ShelfView.java
 * 
 * @description :
 * 
 * 
 * @version : 5.5
 * 
 * @date: June 17, 2013
 * 
 * @Author Gooru Team
 * 
 * @Reviewer:
 */
public class ShelfView extends BaseViewWithHandlers<ShelfUiHandlers> implements
		IsShelfView, ClickHandler{

	@UiField(provided = true)
	EditableLabelUc collectionTitleUc;

	@UiField(provided = true)
	EditableTextAreaUc collectionDescriptionUc;

	@UiField
	SimplePanel collectionMetaDataSimPanel, folderListPanel;

	@UiField
	Label copyCollectionLbl, deleteUserCollectionLbl,lblDeleting, collectionEditImageLbl,
			simplePencilPanel,collectionDescriptionTitle, lblLastEditedBy,moveCollectionLbl, lblCharLimit;

	@UiField
	SimplePanel shelfTabSimPanel;

	@UiField HTMLPanel panelActionItems;
	
	@UiField
	CollectionTabTitleVc infoTabVc, resourceTabVc, collaboratorTabVc,
			shareTabVc, assignTabVc;
	
	@UiField Label statisticsTabVc;

	@UiField FooterOrganizeUc panelFoooter;
	
	@UiField
	HTML backToSearchHtml, backToSearchPreHtml;

	@UiField
	FlowPanel backToSearchFloPanel, noCollectionResetPanel, collectionFloPanel;

	@UiField
	static FlowPanel scrollContainer;

	@UiField(provided = true)
	ShelfCBundle res;

	@UiField
	Button collectionPreviewBtn,editSelfCollectionDescSaveButton,editSelfCollectionSaveButton,editSelfCollectionSaveButtonCancel,editSelfCollectionDescSaveButtonCancel;

	@UiField
	CollectionUploadImageUc collectionImageShelfUc;

	/*
	 * @UiField FocusPanel simplePencilFocPanel;
	 */
	@UiField
	HTMLPanel collPopup, statPopup,loadingImageLabel,panelFriendly,editPanel,rbPublicPanel,publishedPanel;

	@UiField
	FlowPanel shelfViewMainContainer;

	@UiField Image imgNotFriendly;
	
	@UiField Button rbPublic;
	
	@UiField
	HTMLEventPanel collectionTitleContainer,
			collectionDescriptionTitleContainer, 
			editCollectionTitle, editCollectionDescTitle;
	
	ToolTip toolTip=null;
	
	DeletePopupViewVc delete = null;
	
	@UiField Image imgFriendly;
	
	@UiField Label lblFriendly,lblPublishPending,lblPublish;
	
	boolean collectionItemsNotFriendly = false;

	private DeleteConfirmPopupVc deleteConfirmPopup;
	
	private FolderPopupUc folderPopupUc;

	private CollectionCollaboratorTabVc collectionCollaboratorTabVc;

	private CollectionShareTabVc collectionShareTabVc;
	
	private CollectionStatisticsTabVc collectionStatisticsTabVc;
	
	private String deleteCollectionId="";

	private static CollectionDo collectionDo = null; 

	private PlaceRequest searchRequest = null;
	
	private PlaceRequest oldSearchRequest = null;
	
	private boolean isEditCopy=false;
	
	private boolean isShowTitlePencil=true;
	
	private boolean isShowDescPencil=true;
	
	private static final String COLLECTION_MOVE = "collectionMove";
	
	private static final String COLLECTION_MOVE_MP_EVENT ="Organize_move_collection";

	/* HTML5 Storage implementation for tab persistance */
	private Storage stockStore = null;

	Image categoryImage = new Image();

	@UiField
	Label titleAlertMessageLbl;

	@UiField
	Label descriptionAlertMessageLbl;

	private Timer tooltipTimer = null;
	
	boolean refresh = true;
	
	int currentCollabCount =0;
	
	int defaultCollabCount = 5;
	
	boolean isOwnerUsedInOwnCollection = false;
	
	boolean isCollabUsedThisCollection = false;
	
	static MessageProperties i18n = GWT.create(MessageProperties.class);

	private static String WHAT_IS_THIS_COLLECTION_ABOUT = i18n.GL1485()+i18n.GL_SPL_FULLSTOP()+i18n.GL_SPL_FULLSTOP()+i18n.GL_SPL_FULLSTOP();

	private static final String PRE_SEARCH_LINK = i18n.GL1487();
	
	private static final String PRE_CLASSPAGE_LINK= i18n.GL1486();

	private static final String DELETE_COLLECTION = i18n.GL0558();

	private static final String NO_COLLECTION_MESSAGE = i18n.GL0995();

	private static final int TOOLTIP_DELAY_TIME = 1000;
	
	private static final String O1_LEVEL = "o1";
	
	private static final String O2_LEVEL = "o2";
	
	private static final String O3_LEVEL = "o3";
	
	private static final String ID = "id";
	
	List<ClassPageCollectionDo> classpageTitles=null;
	
	private static final String GOORU_UID = "gooruuid";

	private static ShelfViewUiBinder uiBinder = GWT
			.create(ShelfViewUiBinder.class);

	interface ShelfViewUiBinder extends UiBinder<Widget, ShelfView> {
	}

	/**
	 * class constructor
	 */
	public ShelfView() {
		this.res = ShelfCBundle.INSTANCE;
		
				/**
		 * this method is used to save collection description after update
		 * 
		 * 
		 */

		
		
		
		collectionDescriptionUc = new EditableTextAreaUc() {

			@Override
			public void onEditDisabled(String text) {
				descriptionAlertMessageLbl
						.addStyleName("titleAlertMessageDeActive");
				descriptionAlertMessageLbl
						.removeStyleName("titleAlertMessageActive");
				editSelfCollectionDescSaveButtonCancel.getElement().getStyle()
						.setDisplay(Display.NONE);
				editSelfCollectionDescSaveButton.getElement().getStyle()
						.setDisplay(Display.NONE);
				collectionDo.setGoals(text);
				getUiHandlers().updateCollectionInfo(
						collectionDo.getGooruOid(), null, text);

			}
		/**
		 * This method is used to validate collection description length and return error message
		 */
			@Override
			public void checkCharacterLimit(String text) {
				if (text.length() >= 500) {
					descriptionAlertMessageLbl
							.addStyleName("titleAlertMessageActive");
					descriptionAlertMessageLbl
							.removeStyleName("titleAlertMessageDeActive");
				} else {
					descriptionAlertMessageLbl
							.addStyleName("titleAlertMessageDeActive");
					descriptionAlertMessageLbl
							.removeStyleName("titleAlertMessageActive");
				}
			}
			
			@Override
			public void showProfanityError(boolean value) {
				if(value){
					if(!editSelfCollectionDescSaveButton.isVisible()){
						getLblErrorMessage().setVisible(false);
					}
				}else{
					
				}
			}
		};
		/**
		 * this method is used to save collection title after update
		 */
		collectionTitleUc = new EditableLabelUc() {

			@Override
			public void onEditDisabled(String text) {
				titleAlertMessageLbl.addStyleName("titleAlertMessageDeActive");
				titleAlertMessageLbl.removeStyleName("titleAlertMessageActive");
				collectionDo.setTitle(text);
				editSelfCollectionSaveButton.getElement().getStyle()
						.setDisplay(Display.NONE);
				editSelfCollectionSaveButtonCancel.getElement().getStyle()
						.setDisplay(Display.NONE);

				getUiHandlers().updateCollectionInfo(
						collectionDo.getGooruOid(), text, null);
			}
			/**
			 * This method is used to validate collection title length and return error message
			 */
			@Override
			public void checkCharacterLimit(String text) {
				if (text.length() >= 50) {
					titleAlertMessageLbl.setText(i18n.GL0143());
					titleAlertMessageLbl
							.addStyleName("titleAlertMessageActive");
					titleAlertMessageLbl
							.removeStyleName("titleAlertMessageDeActive");
				} else {
					titleAlertMessageLbl
							.addStyleName("titleAlertMessageDeActive");
					titleAlertMessageLbl
							.removeStyleName("titleAlertMessageActive");
				}
			}
			
			@Override
			public void showProfanityError(boolean value) {
				if (value) {
					if(editSelfCollectionSaveButton.isVisible()){
						titleAlertMessageLbl.setText(i18n.GL0554());
						titleAlertMessageLbl
								.addStyleName("titleAlertMessageActive");
						titleAlertMessageLbl
								.removeStyleName("titleAlertMessageDeActive");
					}
					
				} else {
					titleAlertMessageLbl
							.addStyleName("titleAlertMessageDeActive");
					titleAlertMessageLbl
							.removeStyleName("titleAlertMessageActive");
				}
			}
			
			@Override
			public void showErrorMessage(String message){
				titleAlertMessageLbl.setText(message);
				StringUtil.setAttributes(titleAlertMessageLbl.getElement(), "lblTitleAlertMessageLbl", message, message);
				
				titleAlertMessageLbl
						.addStyleName("titleAlertMessageActive");
				titleAlertMessageLbl
						.removeStyleName("titleAlertMessageDeActive");
			}
		};
		
		DisplayNoCollectionHandler displayNoCollections = new DisplayNoCollectionHandler() {

			@Override
			public void showNoCollections() {
				setNoDataCollection();
			}

		
		};

		AppClientFactory.getEventBus().addHandler(
				DisplayNoCollectionEvent.TYPE, displayNoCollections);
		
		collectionDescriptionUc.setPlaceholder(WHAT_IS_THIS_COLLECTION_ABOUT);
		collectionDescriptionUc.getElement().setId("tatCollectionDescription");
		// collectionDescriptionUc.getElement().setAttribute("placeholder",
		// WHAT_IS_THIS_COLLECTION_ABOUT);
		CollectionCBundle.INSTANCE.css().ensureInjected();
		res.css().ensureInjected();
		setWidget(uiBinder.createAndBindUi(this));
		editSelfCollectionSaveButtonCancel.setText(i18n.GL0142());
		editSelfCollectionSaveButtonCancel.getElement().setId("btnEditCancel");
		editSelfCollectionSaveButtonCancel.getElement().setAttribute("alt",i18n.GL0142());
		editSelfCollectionSaveButtonCancel.getElement().setAttribute("title",i18n.GL0142());
		
		editSelfCollectionSaveButton.setText(i18n.GL0141());
		editSelfCollectionSaveButton.getElement().setId("btnSave");
		editSelfCollectionSaveButton.getElement().setAttribute("alt",i18n.GL0141());
		editSelfCollectionSaveButton.getElement().setAttribute("title",i18n.GL0141());
		
		titleAlertMessageLbl.setText(i18n.GL0143());
		titleAlertMessageLbl.getElement().setId("lblTitleAlertMessageLbl");
		titleAlertMessageLbl.getElement().setAttribute("alt",i18n.GL0143());
		titleAlertMessageLbl.getElement().setAttribute("title",i18n.GL0143());
		
		collectionDescriptionTitle.setText(i18n.GL0618());
		collectionDescriptionTitle.getElement().setId("lblCollectionDescriptionTitle");
		collectionDescriptionTitle.getElement().setAttribute("alt",i18n.GL0618());
		collectionDescriptionTitle.getElement().setAttribute("title",i18n.GL0618());
		
		String value = StringUtil.generateMessage(i18n.GL2103(), "500");
		
		StringUtil.setAttributes(lblCharLimit.getElement(), "lblCharLimit", value, value);
		lblCharLimit.setText(value);
		lblCharLimit.setVisible(false);
		
		
		editSelfCollectionDescSaveButtonCancel.setText(i18n.GL0142());
		editSelfCollectionDescSaveButtonCancel.getElement().setId("btnEditDescCancel");
		editSelfCollectionDescSaveButtonCancel.getElement().setAttribute("alt",i18n.GL0142());
		editSelfCollectionDescSaveButtonCancel.getElement().setAttribute("title",i18n.GL0142());
		
		editSelfCollectionDescSaveButton.setText(i18n.GL0141());
		editSelfCollectionDescSaveButton.getElement().setId("btnEditDescSave");
		editSelfCollectionDescSaveButton.getElement().setAttribute("alt",i18n.GL0141());
		editSelfCollectionDescSaveButton.getElement().setAttribute("title",i18n.GL0141());
		
		descriptionAlertMessageLbl.setText(i18n.GL0143());
		descriptionAlertMessageLbl.getElement().setId("lblDescriptionAlertMessageLbl");
		descriptionAlertMessageLbl.getElement().setAttribute("alt",i18n.GL0143());
		descriptionAlertMessageLbl.getElement().setAttribute("title",i18n.GL0143());
		
		collectionPreviewBtn.setText(i18n.GL0633());
		collectionPreviewBtn.getElement().setId("btnCollectionPreview");
		collectionPreviewBtn.getElement().setAttribute("alt",i18n.GL0633());
		collectionPreviewBtn.getElement().setAttribute("title",i18n.GL0633());
		
		copyCollectionLbl.setText(i18n.GL0827());
		copyCollectionLbl.getElement().setId("lblCopyCollection");
		copyCollectionLbl.getElement().setAttribute("alt",i18n.GL0827());
		copyCollectionLbl.getElement().setAttribute("title",i18n.GL0827());
		
		moveCollectionLbl.setText(i18n.GL1261());
		moveCollectionLbl.getElement().setId("moveCollectionLbl");
		moveCollectionLbl.getElement().setAttribute("alt",i18n.GL1261());
		moveCollectionLbl.getElement().setAttribute("title",i18n.GL1261());
		
		imgNotFriendly.setTitle(i18n.GL0732());
		imgNotFriendly.getElement().setId("moveCollectionLbl");
		imgNotFriendly.getElement().setAttribute("alt",i18n.GL0732());
		imgNotFriendly.getElement().setAttribute("title",i18n.GL0732());
		imgNotFriendly.setAltText(i18n.GL0732());
		imgNotFriendly.setUrl("images/mos/questionmark.png");
		
		infoTabVc.setLabel(i18n.GL0828());
		infoTabVc.getElement().setId("lblInfoTab");
		infoTabVc.getElement().setAttribute("alt",i18n.GL0828());
		infoTabVc.getElement().setAttribute("title",i18n.GL0828());
		
		resourceTabVc.setLabel(i18n.GL0829());
		resourceTabVc.getElement().setId("lblResourceTab");
		resourceTabVc.getElement().setAttribute("alt",i18n.GL0829());
		resourceTabVc.getElement().setAttribute("title",i18n.GL0829());
		
		shareTabVc.setLabel(i18n.GL0526());
		shareTabVc.getElement().setId("lblShareTab");
		shareTabVc.getElement().setAttribute("alt",i18n.GL0526());
		shareTabVc.getElement().setAttribute("title",i18n.GL0526());
		
		assignTabVc.setLabel(i18n.GL0519_1());
		assignTabVc.getElement().setId("lblAssignTab");
		assignTabVc.getElement().setAttribute("alt",i18n.GL0519_1());
		assignTabVc.getElement().setAttribute("title",i18n.GL0519_1());
		
		collaboratorTabVc.setLabel(i18n.GL0830());
		collaboratorTabVc.getElement().setId("lblCollaboratorTab");
		collaboratorTabVc.getElement().setAttribute("alt",i18n.GL0830());
		collaboratorTabVc.getElement().setAttribute("title",i18n.GL0830());
		
		statisticsTabVc.setText(i18n.GL0831());
		statisticsTabVc.getElement().setId("lblStatisticsTab");
		statisticsTabVc.getElement().setAttribute("alt",i18n.GL0831());
		statisticsTabVc.getElement().setAttribute("title",i18n.GL0831());
	
		backToSearchHtml.addClickHandler(this);
		infoTabVc.addClickHandler(this);
		resourceTabVc.addClickHandler(this);
		shareTabVc.addClickHandler(this);
		//By default not visible.
		lblLastEditedBy.setVisible(false);
		shelfViewMainContainer.getElement().setId("fpnlShelfViewMainContainer");
		scrollContainer.getElement().setId("fpnlScrollContainer");
		backToSearchFloPanel.getElement().setId("fpnlBackToSearchFloPanel");
		backToSearchPreHtml.getElement().setId("htmlBackToSearchPreHtml");
		backToSearchHtml.getElement().setId("htmlBackToSearchHtml");
		shelfTabSimPanel.getElement().setId("spnlShelfTabSimPanel");
		noCollectionResetPanel.getElement().setId("fpnlNoCollectionResetPanel");
		loadingImageLabel.getElement().setId("pnlLoadingImageLabel");
		editPanel.getElement().setId("pnlEditPanel");		
		folderListPanel.getElement().setId("spnlFolderListPanel");
		collectionFloPanel.getElement().setId("fpnlCollectionFloPanel");
		lblLastEditedBy.getElement().setId("lblLastEditedBy");
		collectionImageShelfUc.getElement().setId("collectionImageUcCollectionImageShelfUc");
		collectionTitleContainer.getElement().setId("epnlCollectionTitleContainer");
		editCollectionTitle.getElement().setId("epnlEditCollectionTitle");
		collectionEditImageLbl.getElement().setId("lblCollectionEditImageLbl");
		panelActionItems.getElement().setId("pnlPanelActionItems");
		panelActionItems.getElement().getStyle().setMarginTop(8, Unit.PX);
		collectionDescriptionTitleContainer.getElement().setId("epnlCollectionDescriptionTitleContainer");
		editCollectionDescTitle.getElement().setId("epnlEditCollectionDescTitle");
		simplePencilPanel.getElement().setId("lblSimplePencilPanel");
		panelFriendly.getElement().setId("pnlPanelFriendly");
		//imgFriendly.getElement().setId("imgImgFriendly");
		lblFriendly.getElement().setId("lblFriendly");
		collPopup.getElement().setId("pnlCollPopup");
		statPopup.getElement().setId("pnlStatPopup");
		rbPublicPanel.getElement().setId("pnlRbPublicPanel");
		rbPublic.getElement().setId("btnRbPublic");
		publishedPanel.getElement().setId("pnlPublishedPanel");
		collectionMetaDataSimPanel.getElement().setId("spnlCollectionMetaDataSimPanel");
		panelFoooter.getElement().setId("footerOrganizePanelFoooter");
		
		/* Disabled */
//		collaboratorTabVc.addStyleName("deactivated");
//		assignTabVc.addStyleName("deactivated");
//		statisticsTabVc.addStyleName("deactivated");
		
		collectionEditImageLbl.setVisible(false);
		statisticsTabVc.setVisible(false);
		deleteUserCollectionLbl.getElement().setId("lblDeleteUserCollection");
	
		editSelfCollectionDescSaveButtonCancel.getElement().setAttribute(
				"style", "margin-top:3px");
		editSelfCollectionSaveButton.getElement().getStyle()
				.setDisplay(Display.NONE);
		editSelfCollectionSaveButtonCancel.getElement().getStyle()
				.setDisplay(Display.NONE);
		editSelfCollectionDescSaveButton.getElement().getStyle()
				.setDisplay(Display.NONE);
		editSelfCollectionDescSaveButtonCancel.getElement().getStyle()
				.setDisplay(Display.NONE);
		//collectionPreviewBtn.getElement().getStyle().setFontWeight(FontWeight.NORMAL);
		simplePencilPanel.setVisible(false);

		assignTabVc.addClickHandler(this);
		collaboratorTabVc.addClickHandler(this);
		
		lblPublishPending.setVisible(false);
		lblPublishPending.getElement().getStyle().setMarginTop(9, Unit.PX);
		lblPublishPending.setText(i18n.GL1943());
		lblPublishPending.getElement().setId("lblPublishPending");
		lblPublishPending.getElement().setAttribute("alt",i18n.GL1943());
		lblPublishPending.getElement().setAttribute("title",i18n.GL1943());
		
		lblPublish.setText(i18n.GL1942());
		lblPublish.getElement().setId("lblPublish");
		lblPublish.getElement().setAttribute("alt",i18n.GL1942());
		lblPublish.getElement().setAttribute("title",i18n.GL1942());

		handelChangeImageEvent();
		// simplePencilFocPanel.addMouseOverHandler(new hideEditPencil());
		// simplePencilFocPanel.addMouseOutHandler(new showEditPencil());
		collectionTitleUc.getElement().setId("txtCollectionTitle");
		
		
		  Boolean isIpad = !!Navigator.getUserAgent().matches("(.*)iPad(.*)");
		  Boolean isAndriod = !!Navigator.getUserAgent().matches("(.*)Android(.*)");
		  Boolean isWinDskp = !!Navigator.getUserAgent().matches("(.*)NT(.*)");
		  
		  UAgentInfo detector = new UAgentInfo(Navigator.getUserAgent());
		  
		  if(isIpad && !StringUtil.IPAD_MESSAGE_Close_Click)
		  {
			  scrollContainer.getElement().setAttribute("style", "position:relative;");
		  }
		  else if(isAndriod && !StringUtil.IPAD_MESSAGE_Close_Click)
		  {
			  scrollContainer.getElement().setAttribute("style", "position:relative;");
		  }		  
		  else
		  {
			  scrollContainer.getElement().setAttribute("style", "position:fixed;");
		  }
		

		collectionFloPanel.addDomHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				event.stopPropagation();
			}
		}, ClickEvent.getType());
//		collaboratorTabVc.addMouseOverHandler(new MouseOverHandler() {
//
//			@Override
//			public void onMouseOver(MouseOverEvent event) {
//				tooltipTimer = new Timer() {
//					public void run() {
//						collPopup.setVisible(true);
//					}
//				};
//				tooltipTimer.schedule(TOOLTIP_DELAY_TIME);
//			}
//		});
//		collaboratorTabVc.addMouseOutHandler(new MouseOutHandler() {
//
//			@Override
//			public void onMouseOut(MouseOutEvent event) {
//				tooltipTimer.cancel();
//				collPopup.setVisible(false);
//			}
//		});
		statisticsTabVc.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {

				tooltipTimer = new Timer() {
					public void run() {
						if (collectionDo.getSharing() != null && collectionDo.getSharing().equalsIgnoreCase("private")){
							statPopup.setVisible(true);
						}
					}
				};
				tooltipTimer.schedule(TOOLTIP_DELAY_TIME);
			}
		});
		statisticsTabVc.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				tooltipTimer.cancel();
				statPopup.setVisible(false);
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
		
		noCollectionResetPanel.setVisible(false);
		collPopup.setVisible(false);
		statPopup.setVisible(false);
		statisticsTabVc.addClickHandler(new AnalyticsClickEvent());
		
		lblDeleting.setVisible(false);
		lblDeleting.setText(i18n.GL0560());
		lblDeleting.getElement().setId("lblDeleting");
		lblDeleting.getElement().setAttribute("alt",i18n.GL0560());
		lblDeleting.getElement().setAttribute("title",i18n.GL0560());
		
		classpageTitles = new ArrayList<ClassPageCollectionDo>();
		loadingImageLabel.getElement().setId("loadingImageLabel");
		
		panelFoooter.setVisible(true);
		rbPublic.setText(i18n.GL1921());
		rbPublic.getElement().setAttribute("alt",i18n.GL1921());
		rbPublic.getElement().setAttribute("title",i18n.GL1921());
		
		lblPublish.setText(i18n.GL1942());
		lblPublish.getElement().setAttribute("alt",i18n.GL1942());
		lblPublish.getElement().setAttribute("title",i18n.GL1942());
//		if (AppClientFactory.getLoggedInUser().getConfirmStatus() == 1){
//			shelfViewMainContainer.getElement().getStyle().clearMarginTop();
//		}else{
//			shelfViewMainContainer.getElement().getStyle().setMarginTop(-28, Unit.PX);
//		}
//	
		if (AppClientFactory.isAnonymous()){
			Window.enableScrolling(true);
		}else{
			Window.enableScrolling(false);
		}
	}
	
	
	/**
	 * This class is used to edit collection description
	 */
	public class OpenCollectionEditDescription implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			/*collectionDescriptionUc.switchToEdit();
			collectionDescriptionUc.setText(collectionDo.getGoals());*/
			lblCharLimit.setVisible(true);
			editSelfCollectionDescSaveButton.getElement().getStyle()
					.setDisplay(Display.BLOCK);
			editSelfCollectionDescSaveButtonCancel.getElement().getStyle()
					.setDisplay(Display.BLOCK);
			/*
			 * editSelfCollectionSaveButton.getElement().getStyle().setDisplay(
			 * Display.NONE);
			 * editSelfCollectionSaveButtonCancel.getElement().getStyle
			 * ().setDisplay(Display.NONE);
			 */
			simplePencilPanel.setVisible(false);
			isShowDescPencil=false;
			collectionDescriptionUc.switchToEdit();
		}
	}
/**
 * This method is used to get collection title
 * @return collection title
 */
	public static String getCollectionTitle() {
		return collectionDo.getTitle();
	}

	@Override
	public void setCollection(CollectionDo collection) {
		noCollectionResetPanel.setVisible(false);
		this.collectionDo = collection;
		panelFoooter.setVisible(true);
		editPanel.getElement().getStyle().setBackgroundColor("white");
		collectionFloPanel.getElement().setAttribute("style", "min-height:"+(Window.getClientHeight()-100)+"px");
		if(collection != null) {
			if (AppClientFactory.isContentAdmin() || collectionDo
					.getUser().getGooruUId().equals(AppClientFactory.getLoggedInUser()
							.getGooruUId())){
					deleteUserCollectionLbl.setVisible(true);			
			}else{
				deleteUserCollectionLbl.setVisible(false);
			}
			
			if(collectionDo.getSharing()!=null){
				final String share=collectionDo.getSharing();
				AppClientFactory.getInjector().getUserService().getUserProfileDetails(GOORU_UID, new SimpleAsyncCallback<SettingDo>() {

					@Override
					public void onSuccess(SettingDo result) {
						if(result.getUser().getAccountTypeId()==2){
							rbPublicPanel.setVisible(false);
							publishedPanel.setVisible(false);
						}else{
							rbPublicPanel.setVisible(true);
							if(share.equalsIgnoreCase("private")||share.equalsIgnoreCase("anyonewithlink")){
								if(collectionDo.getPublishStatus()!=null && collectionDo.getPublishStatus().getValue().equals("pending")){
									rbPublic.setVisible(false);
									lblPublishPending.setVisible(true);
									publishedPanel.setVisible(false);
								}else{
									rbPublic.setVisible(true);
									lblPublishPending.setVisible(false);
									publishedPanel.setVisible(false);
								}
							}else{
								rbPublic.setVisible(false);
								lblPublishPending.setVisible(false);
								publishedPanel.setVisible(true);
							}
						}
						if(!collectionDo.getCollectionType().equals(ShelfPresenter.ASSESSMENT)){
							rbPublicPanel.setVisible(true);
						}else{
							rbPublicPanel.setVisible(false);
						}
					}
					
				});
			}
			if(!collection.getCollectionType().equals(ShelfPresenter.ASSESSMENT)){
				editCollectionDescTitle.addMouseOverHandler(new OnCollectionDescriptionClick());
				editCollectionDescTitle.addMouseOutHandler(new OnCollectionDescriptionOut());
				collectionDescriptionTitle.addMouseOverHandler(new OnCollectionDescriptionClick());
				collectionDescriptionTitle.addMouseOutHandler(new OnCollectionDescriptionOut());
				collectionTitleContainer.addMouseOverHandler(new hideEditPencil());
				collectionTitleContainer.addMouseOutHandler(new showEditPencil());
				collectionEditImageLbl.addClickHandler(new OnEditImageClick());
				editCollectionTitle.addClickHandler(new OnEditImageClick());
				editCollectionDescTitle.addClickHandler(new OpenCollectionEditDescription());
				simplePencilPanel.addClickHandler(new OpenCollectionEditDescription());
				collectionDescriptionTitle.addClickHandler(new OpenCollectionEditDescription());
				copyCollectionLbl.setVisible(true);
				deleteUserCollectionLbl.setVisible(true);
				collaboratorTabVc.setVisible(true);
				rbPublicPanel.setVisible(true);
			}else{
				copyCollectionLbl.setVisible(false);
				deleteUserCollectionLbl.setVisible(false);
				collaboratorTabVc.setVisible(false);
				rbPublicPanel.setVisible(false);
			}
			
			if (collection.getMeta()!=null && collection.getMeta().getCollaboratorCount() > 0 && collection.getLastModifiedUser() != null){
				String lastModifiedDate = collection.getLastModified().toString() != null ? getTimeStamp(collection.getLastModified().getTime()+"") : "";
				String lastModifiedUser = collection.getLastModifiedUser().getUsername() != null ?  collection.getLastModifiedUser().getUsername() : "";
				lblLastEditedBy.setText(StringUtil.generateMessage(i18n.GL1112(), lastModifiedDate, lastModifiedUser));
				lblLastEditedBy.setVisible(lastModifiedUser!=null && !lastModifiedUser.equalsIgnoreCase("") ? true : false);
				if (lastModifiedUser!=null && !lastModifiedUser.equalsIgnoreCase("")){
					panelActionItems.getElement().getStyle().setTop(111, Unit.PX);
				}else{
					panelActionItems.getElement().getStyle().clearTop();
				}
			}
			else{
				lblLastEditedBy.setVisible(false);
				panelActionItems.getElement().getStyle().clearTop();
			}
		} else {
			lblLastEditedBy.setVisible(false);
			panelActionItems.getElement().getStyle().clearTop();
		}
		
		getUiHandlers().clearTabSlot();
		if (collectionDo.getSharing() != null){
			setCollectionAnalyticsVisibility(collectionDo.getSharing());
		}
		currentCollabCount = collectionDo.getMeta()!=null&&collectionDo.getMeta().getCollaboratorCount()!=null?collectionDo.getMeta().getCollaboratorCount():0 ;
		setCollabCount(currentCollabCount);
		
		setTab(getPersistantTabObjectUsingTabFlag());
		collectionTitleUc.setText(collection.getTitle());
		collectionDescriptionUc.setText(collection.getGoals());
		collectionImageShelfUc.setUrl(collection.getThumbnails().getUrl());
		collectionImageShelfUc.getCollectionImg().setAltText(collection.getTitle());
		collectionImageShelfUc.getCollectionImg().setTitle(collection.getTitle());
		collectionFloPanel.setVisible(true);
		deleteUserCollectionLbl.setText(DELETE_COLLECTION);
		deleteUserCollectionLbl.getElement().setAttribute("alt",DELETE_COLLECTION);
		deleteUserCollectionLbl.getElement().setAttribute("title",DELETE_COLLECTION);
		
		
		if (AppClientFactory.getLoggedInUser().getConfirmStatus() == 0 && lblLastEditedBy.isVisible()){
			editPanel.getElement().getStyle().setMarginTop(39, Unit.PX);
		}else if (AppClientFactory.getLoggedInUser().getConfirmStatus() == 0){
			editPanel.getElement().getStyle().setMarginTop(23, Unit.PX);
		}else{
			editPanel.getElement().getStyle().clearMarginTop();
		}
		
		Integer resourcesCount = 0;
		if (collectionDo.getCollectionItems() != null && collectionDo.getCollectionItems().size() != 0) {
			resourcesCount = collectionDo.getCollectionItems().size();
		}
		resourceTabVc.setLabel(""+i18n.GL0829()+" (" + resourcesCount + ")");
		
		//getCollectionShareTabVc();
	}
	public void setCollabCount(int count){
		//	Set the count of Collaborators;
			String strColl = i18n.GL0830() + " (" + count + ")" ;
			collaboratorTabVc.setLabel(strColl);
	}
	@Override
	public void setCollabCountByType(String type, int count){
		if (type.equalsIgnoreCase("incrementBy")){
			currentCollabCount = currentCollabCount + count;
			setCollabCount(currentCollabCount);
		}else{
			if (currentCollabCount < count){
				setCollabCount(0);
			}else{
				currentCollabCount = currentCollabCount - count;
				setCollabCount(currentCollabCount);
			}
		}
		collectionDo.getMeta().setCollaboratorCount(currentCollabCount);
	}
	/**
	 * @function setCollectionAnalyticsVisibility 
	 * 
	 * @created_date : Jan 14, 2014
	 * 
	 * @description
	 * 
	 * 
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 * 
	*/
	
	public void setCollectionAnalyticsVisibility(String shareValue) {
		//Check whether the Collection is public or not.
		collectionDo.setSharing(shareValue);
		//Enable this if we enable the statistics tab.
		if (shareValue != null && shareValue.equalsIgnoreCase("private")){
			//Set the deactivated style
			statisticsTabVc.addStyleName("deactivated");
//			statisticsTabVc.setEnabled(false);
		}else{
			statisticsTabVc.removeStyleName("deactivated");
//			statisticsTabVc.setEnabled(true);
		}
	}

	@Override
	public void setInSlot(Object slot, Widget content) {
		if (AppClientFactory.isAnonymous()){
			shelfTabSimPanel.setVisible(false);
			folderListPanel.setVisible(false);
			loadingImageLabel.setVisible(false);
			editPanel.setVisible(false);
			noCollectionResetPanel.setVisible(true);
			scrollContainer.setVisible(false);
		}else{
			scrollContainer.setVisible(true);
			noCollectionResetPanel.setVisible(false);
			shelfTabSimPanel.setVisible(true);
			folderListPanel.setVisible(true);
			loadingImageLabel.setVisible(false);
			editPanel.setVisible(true);
			if (content != null) {
				if (slot == ShelfUiHandlers.TYPE_SHELF_TAB) {
					shelfTabSimPanel.setWidget(content);
				} else if(slot == ShelfUiHandlers.TYPE_FOLDERS_SLOT) {
					folderListPanel.setWidget(content);
				}
			}
		}
	}

	@Override
	public void addToSlot(Object slot, Widget content) {
		if (content != null) {
			if (slot == ShelfUiHandlers.TYPE_COLLECTION_RESOURCE_TAB) {
				collectionMetaDataSimPanel.setWidget(content);
				if (content != null) {
					Integer resourcesCount = 0;
					if (collectionDo.getCollectionItems() != null
							&& collectionDo.getCollectionItems().size() != 0) {
						resourcesCount = collectionDo.getCollectionItems()
								.size();
					}
					resourceTabVc.setLabel(""+i18n.GL0829()+" (" + resourcesCount + ")");
				}
			} else if (slot == ShelfUiHandlers.TYPE_COLLECTION_INFO_TAB || slot == ShelfUiHandlers.TYPE_ASSIGN_INFO_TAB || slot == ShelfUiHandlers.TYPE_COLLABORATOR_TAB ) {
				collectionMetaDataSimPanel.setWidget(content);
			}
		}
	}

	@Override
	public void reset() {
		super.reset();
		this.collectionDo = null;
		collectionCollaboratorTabVc = null;
		collectionShareTabVc = null;
		collectionStatisticsTabVc = null;
		collectionMetaDataSimPanel.clear();
		collectionFloPanel.setVisible(false);
		collectionTitleUc.setValue("");
		if(!isEditCopy){
			editSelfCollectionSaveButton.getElement().getStyle()
			.setDisplay(Display.NONE);
	        editSelfCollectionSaveButtonCancel.getElement().getStyle()
			.setDisplay(Display.NONE);
	        collectionTitleUc.switchToCancelLabel();
	        editSelfCollectionDescSaveButton.getElement().getStyle()
			.setDisplay(Display.NONE);
	        editSelfCollectionDescSaveButtonCancel.getElement().getStyle()
			.setDisplay(Display.NONE);
	        collectionDescriptionUc.switchToDesCancelLabel();
		}
		titleAlertMessageLbl
		.addStyleName("titleAlertMessageDeActive");
		titleAlertMessageLbl
		.removeStyleName("titleAlertMessageActive");
		titleAlertMessageLbl.setVisible(false);
		collectionDescriptionUc.setValue(WHAT_IS_THIS_COLLECTION_ABOUT);
		collectionDescriptionUc.getElement().setId("tatCollectionDescription");
		isEditCopy=false;
		scrollContainer.getElement().getStyle().setHeight(Window.getClientHeight() - 3, Unit.PX);
	}

	@Override
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
						backToSearchHtml.setHTML(PRE_SEARCH_LINK + query + "\"");
						backToSearchHtml.getElement().setAttribute("alt", PRE_SEARCH_LINK);
						backToSearchHtml.getElement().setAttribute("title", PRE_SEARCH_LINK);
					} else {
						backToSearchHtml.setHTML(PRE_SEARCH_LINK + query + "\"");
						backToSearchHtml.getElement().setAttribute("alt", PRE_SEARCH_LINK);
						backToSearchHtml.getElement().setAttribute("title", PRE_SEARCH_LINK);
					}
				}
				
				backToSearchPreHtml.setVisible(visible);
				backToSearchHtml.setVisible(visible);

				if(isVisible){
					backToSearchHtml.setHTML(PRE_CLASSPAGE_LINK);
					backToSearchHtml.getElement().setAttribute("alt", PRE_CLASSPAGE_LINK);
					backToSearchHtml.getElement().setAttribute("title", PRE_CLASSPAGE_LINK);
					backToSearchPreHtml.setVisible(isVisible);
					backToSearchHtml.setVisible(isVisible);
				}
			}
		}
		catch(Exception e){
		}

	}

	@Override
	public void onClick(ClickEvent event) {
		Object source = event.getSource();
		getUiHandlers().clearTabSlot();
		if (source.equals(backToSearchHtml)) {
			if(backToSearchHtml.getText().equalsIgnoreCase("Back To ClassPage")){
				AppClientFactory.getPlaceManager().revealPlace(true, searchRequest);
			} else {
				AppClientFactory.getPlaceManager().revealPlace(true, searchRequest);
			}
			
		} else {
			setTab(source);
		}
	}

	/**
	 * view the collection tab
	 * 
	 * @param tab
	 *            which needs to be viewed
	 */
	public void setTab(Object tab) {
//		Window.enableScrolling(true);
		AppClientFactory.fireEvent(new SetHeaderZIndexEvent(0, true));
		setIpadFriendly();
		modifyStaticText(collectionDo.getCollectionType());
//		panelFriendly.setVisible(false);
		if(tab!=null){
			if (tab.equals(infoTabVc)) {
				MixpanelUtil.Click_Info_CollectionEdit();
				setPersistantTabFlag("infoTab");
				infoTabVc.setSelected(true);
				getUiHandlers().revealTab(ShelfUiHandlers.TYPE_COLLECTION_INFO_TAB,collectionDo);
				collectionMetaDataSimPanel.getElement().removeAttribute("style");
			} else if (tab.equals(resourceTabVc)) {
				MixpanelUtil.Click_Resource_CollectionEdit();
				setPersistantTabFlag("resourceTab");
				resourceTabVc.setSelected(true);
				collectionMetaDataSimPanel.getElement().removeAttribute("style");
				
				
				getUiHandlers().revealTab(
						ShelfUiHandlers.TYPE_COLLECTION_RESOURCE_TAB, collectionDo);
			} else if (tab.equals(collaboratorTabVc)) {
				collectionMetaDataSimPanel.clear();
				MixpanelUtil.mixpanelEvent("Click_Collaborators_CollectionEdit");
				setPersistantTabFlag("collaboratorTab");
				collaboratorTabVc.setSelected(true);
				collectionMetaDataSimPanel.getElement().removeAttribute("style");
				
//				collectionMetaDataSimPanel.setWidget(getCollectionCollaboratorTabVc());
				getUiHandlers().revealTab(ShelfUiHandlers.TYPE_COLLABORATOR_TAB, collectionDo);
			}
			else if (tab.equals(assignTabVc)) {
				MixpanelUtil.Click_Assign_CollectionEdit();
				collectionMetaDataSimPanel.getElement().setAttribute("style", "min-height:0px;");
				setPersistantTabFlag("assignTab");
				assignTabVc.setSelected(true);
				collectionMetaDataSimPanel.clear();
				getUiHandlers().revealTab(ShelfUiHandlers.TYPE_ASSIGN_INFO_TAB, collectionDo);
			}
			else if (tab.equals(shareTabVc)) {
				MixpanelUtil.Click_Share_CollectionEdit();
				setPersistantTabFlag("shareTab");
				shareTabVc.setSelected(true);
				collectionMetaDataSimPanel.setWidget((collectionShareTabVc!=null?collectionShareTabVc:getCollectionShareTabVc()));
				collectionShareTabVc.onReveal();
				collectionMetaDataSimPanel.getElement().removeAttribute("style");
			}
		}else{
			collectionMetaDataSimPanel.getElement().removeAttribute("style");
			if(refresh){
				analyticsClickEvent();
				setPersistantTabFlag("resourceTab");
			}else{
				setPersistantTabFlag("resourceTab");
				setTab(getPersistantTabObjectUsingTabFlag());
			}
		}
		
	}

	private void setIpadFriendly() {
		int notFriendlyCount = 0;
		for (CollectionItemDo collectionItemDo : collectionDo.getCollectionItems()){
			if (collectionItemDo.getResource().getMediaType() !=null && collectionItemDo.getResource().getMediaType().equalsIgnoreCase("not_ipad_friendly")){
				notFriendlyCount++;
			}
		}
		//imgFriendly.setVisible(true);
		lblFriendly.setVisible(true);
		if (notFriendlyCount>0){
			//imgFriendly.getElement().getStyle().clearWidth();
			//imgFriendly.setAltText(i18n.GL0737());
			//imgFriendly.setTitle(i18n.GL0737());
			//imgFriendly.setUrl("images/mos/MobileFriendly.png");
			if(collectionDo.getCollectionType()!=null&&collectionDo.getCollectionType().equals(ShelfPresenter.ASSESSMENT)){
				lblFriendly.setText(StringUtil.generateMessage(i18n.GL3012(), String.valueOf(notFriendlyCount), notFriendlyCount>1 ? i18n.GL_GRR_ARE() : i18n.GL_GRR_IS()));
			}else{
				lblFriendly.setText(StringUtil.generateMessage(i18n.GL0449(), String.valueOf(notFriendlyCount), notFriendlyCount>1 ? i18n.GL_GRR_ARE() : i18n.GL_GRR_IS()));
			}
		}else{
			//imgFriendly.getElement().getStyle().setWidth(25, Unit.PX);
			//imgFriendly.setUrl("images/mos/friendlyResource.png");
			//imgFriendly.setAltText(i18n.GL0865());
			//imgFriendly.setTitle(i18n.GL0865());
			if(collectionDo.getCollectionType()!=null&&collectionDo.getCollectionType().equals(ShelfPresenter.ASSESSMENT)){
				lblFriendly.setText(i18n.GL3013());
			}else{
				lblFriendly.setText(i18n.GL0453());
			}
		}
	}
	
	public void modifyStaticText(String collectionType){
		collectionType=(collectionType!=null&&collectionType.equals(ShelfPresenter.ASSESSMENT))?i18n.GL3007().toLowerCase():i18n.GL2001();
		collectionDescriptionUc.setPlaceholder(StringUtil.generateMessage(WHAT_IS_THIS_COLLECTION_ABOUT, collectionType));
	}

	/**
	 * To get the right CollectionTabTitleVc object for display based on tabFlag
	 * in persistant store
	 * 
	 * @return instance of {@link CollectionTabTitleVc}
	 */
	public CollectionTabTitleVc getPersistantTabObjectUsingTabFlag() {
		CollectionTabTitleVc objectToReturn = resourceTabVc;
		stockStore = Storage.getLocalStorageIfSupported();
		if (stockStore != null) {

			String tabFlag = stockStore.getItem("tabKey");

			if (tabFlag != null) {
				if (tabFlag.equalsIgnoreCase("infoTab")) {

					objectToReturn = infoTabVc;

				} else if (tabFlag.equalsIgnoreCase("resourceTab")) {
                    
					objectToReturn = resourceTabVc;

				} else if (tabFlag.equalsIgnoreCase("statisticsTab")) {
					
					objectToReturn = null;

				} else if (tabFlag.equalsIgnoreCase("collaboratorTab")) {

					objectToReturn = collaboratorTabVc;

				} else if (tabFlag.equalsIgnoreCase("assignTab")) {

					 objectToReturn = assignTabVc;

				} else if (tabFlag.equalsIgnoreCase("shareTab")) {

					objectToReturn = shareTabVc;

				}
			} else {
				objectToReturn = resourceTabVc;
			}

		}

		return objectToReturn;

	}

	/**
	 * Sets the incoming tabFlag into Persistant store
	 * 
	 * @param flag
	 *            generated when tabs are being switched
	 */
	public void setPersistantTabFlag(String flag) {
		stockStore = Storage.getLocalStorageIfSupported();
		if (stockStore != null) {
			stockStore.setItem("tabKey", flag);
		}

	}

	/**
	 * get collection collaborator tab view
	 * 
	 * @return collection collaborator view
	 */
	public CollectionCollaboratorTabVc getCollectionCollaboratorTabVc() {
		if (collectionCollaboratorTabVc == null) {
			collectionCollaboratorTabVc = new CollectionCollaboratorTabVc(collectionDo);
		}
		return collectionCollaboratorTabVc;
	}

	/**
	 * get collection share tab view
	 * 
	 * @return collection share tab view
	 */
	public CollectionShareTabVc getCollectionShareTabVc() {
//		if (collectionShareTabVc == null) {
			collectionShareTabVc = new CollectionShareTabVc(collectionDo);
//		}
		return collectionShareTabVc;
	}

	/**
	 * get collection statistics tab view
	 * 
	 * @return collection statistics tab view
	 */
	public CollectionStatisticsTabVc getCollectionStatisticsTabVc() {
		if (collectionStatisticsTabVc == null) {
			collectionStatisticsTabVc = new CollectionStatisticsTabVc(
					collectionDo);
		}
		return collectionStatisticsTabVc;
	}

	/**
	 * create delete confirmation pop and delete the collection if user wants
	 * 
	 * @param clickEvent
	 *            instance of {@link ClickEvent}
	 */
	@UiHandler("deleteUserCollectionLbl")
	public void deleteCollectionPopup(ClickEvent clickEvent) {
		if(!collectionDo.getCollectionType().equals(ShelfPresenter.ASSESSMENT)){
			deleteCollectionId = collectionDo.getGooruOid();
			/*collectionEditButtonContainer.getElement().getStyle()
			.setVisibility(Visibility.HIDDEN);
			
	*/		MixpanelUtil.Organize_Click_Collection_Delete();
	
	//		deleteUserCollectionLbl.setVisible(false);
	//		lblDeleting.setVisible(true);
			//Verify whether creator has added the collection in classpage
			AppClientFactory.getInjector().getClasspageService().getClasspagesListByCollectionId(collectionDo.getGooruOid(), collectionDo.getUser().getGooruUId(), new SimpleAsyncCallback<ArrayList<ClassPageCollectionDo>>() {
				
				@Override
				public void onSuccess(ArrayList<ClassPageCollectionDo> result) {
					classpageTitles = result;
					//This collection has been added by classpage.
					isOwnerUsedInOwnCollection = false;
					if (classpageTitles.size() > 0){
						isOwnerUsedInOwnCollection = true;
	//					showCollectionIsUsedByOwner();
					}
					//This collection is not added by Creator But verify whether this collection is added by some other collaborators.
					AppClientFactory.getInjector().getClasspageService().getCollectionUsedCount(collectionDo.getGooruOid(), new SimpleAsyncCallback<Integer>() {
	
						@Override
						public void onSuccess(Integer count) {
							isCollabUsedThisCollection = false;
							if (count>0){
								isCollabUsedThisCollection = true;
	//								showCollectionIsUserByCollab();
							}
							if (isOwnerUsedInOwnCollection && isCollabUsedThisCollection){
								showCollectionIsByBoth(count);
							}else if (isOwnerUsedInOwnCollection){
								showCollectionIsUsedByOwner();
							}else if (isCollabUsedThisCollection){
	//							showCollectionIsUserByCollab();
								showCollectionIsByBoth(count);
							}else{
								showDeletePopup();
							}
						}
					});
					
				}
				
			});
		}
	}
	
	private void showDeletePopup() {
		if (delete!=null){
			delete = null;
		}
		delete = new DeletePopupViewVc() {
			
			@Override
			public void onClickPositiveButton(ClickEvent event) {
				deleteUserCollectionLbl.setVisible(false);
				lblDeleting.setVisible(true);
				getUiHandlers().deleteCollection(collectionDo.getGooruOid());
			}
			
			@Override
			public void onClickNegitiveButton(ClickEvent event) {
				showNotInProgress();
				delete.hide();
			}
		};
		delete.setPopupTitle(i18n.GL0748());
		if(collectionDo.getCollectionType()!=null&&collectionDo.getCollectionType().equals(ShelfPresenter.ASSESSMENT)){
			delete.setNotes(StringUtil.generateMessage(i18n.GL3038(), collectionDo.getTitle()));
			delete.setDescText(i18n.GL3039());
		}else{
			delete.setNotes(StringUtil.generateMessage(i18n.GL1020(), collectionDo.getTitle()));
			delete.setDescText(i18n.GL1238());
		}
		delete.setDeleteValidate("delete");
		delete.setPositiveButtonText(i18n.GL0190());
		delete.setNegitiveButtonText(i18n.GL0142());
		delete.setPleaseWaitText(i18n.GL0339());
		delete.center();
		delete.show();
	}
	
	public void showCollectionIsByBoth(int collabCount){

		if (delete!=null){
			delete = null;
		}
		delete = new DeletePopupViewVc() {
			
			@Override
			public void onClickPositiveButton(ClickEvent event) {
				deleteUserCollectionLbl.setVisible(false);
				lblDeleting.setVisible(true);
				getUiHandlers().deleteCollection(collectionDo.getGooruOid());
			}
			
			@Override
			public void onClickNegitiveButton(ClickEvent event) {
				showNotInProgress();
				delete.hide();
			}
		};
		delete.setPopupTitle(i18n.GL1163());
		if(collabCount==0 || collabCount>1)
		{
		delete.setNotes(i18n.GL1338());
		}
		else
		{
		delete.setNotes(i18n.GL1495());	
		}
		delete.setDescText(i18n.GL1339());
		delete.setDeleteValidate("delete");
		delete.setPositiveButtonText(i18n.GL0190());
		delete.setNegitiveButtonText(i18n.GL0142());
		delete.setPleaseWaitText(i18n.GL0339());
		delete.center();
		delete.show();
	
	}
	
	public void showCollectionIsUserByCollab(){

		if (delete!=null){
			delete = null;
		}
		delete = new DeletePopupViewVc() {
			
			@Override
			public void onClickPositiveButton(ClickEvent event) {
				getUiHandlers().deleteCollection(collectionDo.getGooruOid());
				deleteUserCollectionLbl.setVisible(false);
				lblDeleting.setVisible(true);
			}
			
			@Override
			public void onClickNegitiveButton(ClickEvent event) {
				showNotInProgress();
				delete.hide();
			}
		};
		delete.setPopupTitle(i18n.GL0748());
		delete.setNotes(StringUtil.generateMessage(i18n.GL1020(), collectionDo.getTitle()));
		delete.setDescText(i18n.GL1238());
		delete.setDeleteValidate("delete");
		delete.setPositiveButtonText(i18n.GL0190());
		delete.setNegitiveButtonText(i18n.GL0142());
		delete.setPleaseWaitText(i18n.GL0339());
		delete.center();
		delete.show();
	
	}
	
	public void showCollectionIsUsedByOwner(){

		delete = new DeletePopupViewVc() {
			
			@Override
			public void onClickPositiveButton(ClickEvent event) {
				deleteUserCollectionLbl.setVisible(false);
				lblDeleting.setVisible(true);
				getUiHandlers().deleteCollection(collectionDo.getGooruOid());
			}
			
			@Override
			public void onClickNegitiveButton(ClickEvent event) {
				showNotInProgress();
				delete.hide();
			}
		};
		delete.setPopupTitle(i18n.GL1163());
		
		StringBuffer sb = new StringBuffer();
		String anchString = "<a href=\"{0}\" target=\"_blank\">{1}</a>";
		String classpageUrl = "#teach&pageSize=10&classpageid={0}&pageNum=0&pos=1";
		int count = classpageTitles.size() >= defaultCollabCount ? defaultCollabCount : classpageTitles.size(); 
		for (int i=0; i<count;i++){
			String url = StringUtil.generateMessage(classpageUrl, classpageTitles.get(i).getClasspageId());
			if (classpageTitles.size()==1){
				sb.append(StringUtil.generateMessage(anchString, url,classpageTitles.get(i).getTitle()));
			}else{
				if (i == (count-1)){
					sb.append(i18n.GL_GRR_AND()+" "+StringUtil.generateMessage(anchString, url,classpageTitles.get(i).getTitle()));
				}else{
					sb.append(StringUtil.generateMessage(anchString, url,classpageTitles.get(i).getTitle()) + ", ");
				}
			}
		}
		String remaining;
		if (classpageTitles.size()>defaultCollabCount){
			remaining = "+"+(classpageTitles.size() - count) +" "+ i18n.GL1153();
		}else{
			if (classpageTitles.size()==1){
				remaining = (" "+i18n.GL1155());
			}else{
				remaining = (" "+i18n.GL1154()+i18n.GL_SPL_EXCLAMATION());
			}
		}
		delete.setNotes(i18n.GL1156()+" "+sb.toString()+" "+remaining);
			
		delete.setDescText(i18n.GL1187());
		delete.setDeleteValidate("delete");
		delete.setPositiveButtonText(i18n.GL0190());
		delete.setNegitiveButtonText(i18n.GL0142());
		delete.setPleaseWaitText(i18n.GL0339());
		
		delete.center();
		delete.show();
	
	}
	
	
	/*@UiHandler("PreviewBtn")
	public void clickOnPreviewBtn(ClickEvent clickEvent)
	{
		//new CustomAnimation(collectionEditButtonContainer).run(500);
		if (collectionEditButtonContainer.getElement().getStyle()
				.getVisibility().equalsIgnoreCase("VISIBLE")) {
			collectionEditButtonContainer.getElement().getStyle()
					.setVisibility(Visibility.HIDDEN);
		} else {
			collectionEditButtonContainer.getElement().getStyle()
					.setVisibility(Visibility.VISIBLE);
		}
		
	}*/
	/**
	 * allows user to play collection
	 * 
	 * @param event
	 *            {@link ClickEvent} instance
	 */
	@UiHandler("collectionPreviewBtn")
	public void collectionPlay(ClickEvent event) {
		if(collectionDo!=null && !collectionDo.getCollectionType().equals(ShelfPresenter.ASSESSMENT)){
			MixpanelUtil.Preview_Collection_From_CollectionEdit();
			HashMap<String,String> params = new HashMap<String,String>();
			params.put("id", collectionDo.getGooruOid());
			PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(PlaceTokens.COLLECTION_PLAY, params);
			AppClientFactory.getPlaceManager().revealPlace(false,placeRequest,true);
		}else{
			Window.open(AppClientFactory.loggedInUser.getSettings().getAssessementEndPoint(), "_blank", "");
		}
	}

	/**
	 * @param clickEvent
	 *            instance of {@link ClickEvent}
	 */
	@UiHandler("copyCollectionLbl")
	public void onCopyCollectionClick(ClickEvent clickEvent) {
	/*	collectionEditButtonContainer.getElement().getStyle()
		.setVisibility(Visibility.HIDDEN);
*/		if(!collectionDo.getCollectionType().equals(ShelfPresenter.ASSESSMENT)){
			MixpanelUtil.Organize_Click_Collection_Copy();
	        boolean addToShelf=AppClientFactory.getPlaceManager().getRequestParameter("o1")==null ? true : false;
			getLoadingImageVisible();
			getUiHandlers().copyCollection(collectionDo.getGooruOid(), addToShelf);
			if (collectionDo.getMeta().isIsCollaborator()){
				MixpanelUtil.mixpanelEvent("Collaborator_copies_collection");
			}
		}
	}
	
	
	@UiHandler("moveCollectionLbl")
	public void onMoveCollectionClick(ClickEvent clickEvent){
		folderPopupUc = new FolderPopupUc(COLLECTION_MOVE , true) {
			@Override
			public void onClickPositiveButton(ClickEvent event, String folderName, String parentId, HashMap<String,String> params) {
				MixpanelUtil.mixpanelEvent(COLLECTION_MOVE_MP_EVENT);
				getUiHandlers().moveCollection(collectionDo.getGooruOid(),parentId,folderName,params);  
			}
		};
		folderPopupUc.setGlassEnabled(true);
		folderPopupUc.removeStyleName("gwt-PopupPanelGlass");
		folderPopupUc.setPopupPosition(clickEvent.getRelativeElement().getAbsoluteLeft() - (604), Window.getScrollTop() + 60);
		Window.enableScrolling(false);
		folderPopupUc.setCollectionType(collectionDo.getCollectionType());
		/*folderPopupUc.center();*/
		folderPopupUc.show();
		
	}

	/**
	 * change the collection image
	 */
	private void handelChangeImageEvent() {
		collectionImageShelfUc.getChangeImage().addClickHandler(
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						collectionImageShelfUc.getChangeImage().getElement().getStyle().setDisplay(Display.NONE);
						getUiHandlers().imageUpload();
					}
				});
	}

	@Override
	public void onPostCollectionImageUpload(String url) {
		collectionImageShelfUc.setUrl(url);
		collectionImageShelfUc.getCollectionImg().setAltText(collectionDo.getTitle());
		collectionImageShelfUc.getCollectionImg().setTitle(collectionDo.getTitle());
		categoryImage.setUrl(url);
		AppClientFactory.fireEvent(new UpdateSocialShareMetaDataEvent(
				collectionTitleUc.getText(), collectionDescriptionUc.getText(),
				categoryImage.getUrl()));
	}

	@Override
	public void onPostCollectionDelete() {
//		deleteConfirmPopup.hide();
		/*AppClientFactory.fireEvent(new RefreshCollectionInShelfListEvent(
				collectionDo, RefreshType.DELETE));*/
		Map<String, String> params = new HashMap<String, String>();
		if(AppClientFactory.getPlaceManager().getRequestParameter("o3")!=null){
			params.put("o1",AppClientFactory.getPlaceManager().getRequestParameter("o1"));  
			params.put("o2",AppClientFactory.getPlaceManager().getRequestParameter("o2"));
			params.put("o3",AppClientFactory.getPlaceManager().getRequestParameter("o3"));
			AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.SHELF,params);
			AppClientFactory.fireEvent(new RemoveMovedCollectionFolderEvent(deleteCollectionId)); 
		}else if(AppClientFactory.getPlaceManager().getRequestParameter("o2")!=null){
			params.put("o1",AppClientFactory.getPlaceManager().getRequestParameter("o1"));  
			params.put("o2",AppClientFactory.getPlaceManager().getRequestParameter("o2"));
			AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.SHELF,params);
			AppClientFactory.fireEvent(new RemoveMovedCollectionFolderEvent(deleteCollectionId)); 
		}else if(AppClientFactory.getPlaceManager().getRequestParameter("o1")!=null){
			params.put("o1",AppClientFactory.getPlaceManager().getRequestParameter("o1")); 
			AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.SHELF,params);
			AppClientFactory.fireEvent(new RemoveMovedCollectionFolderEvent(deleteCollectionId)); 
		}else{
			getLoadingImageInvisible();
			AppClientFactory.fireEvent(new RemoveMovedCollectionFolderEvent(deleteCollectionId)); 
			AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.SHELF);
		}
		if (delete!=null)
			hideDeletePopup(delete);
	}

	@Override
	public void onPostCollectionUpdate() {
		AppClientFactory.fireEvent(new RefreshCollectionInShelfListEvent(
				collectionDo, RefreshType.UPDATE));
	}

	public class hideEditPencil implements MouseOverHandler {

		@Override
		public void onMouseOver(MouseOverEvent event) {
			if(isShowTitlePencil)
			collectionEditImageLbl.setVisible(true);
		}

	}

	public class showEditPencil implements MouseOutHandler {

		@Override
		public void onMouseOut(MouseOutEvent event) {
			collectionEditImageLbl.setVisible(false);
		}

	}
/**
 * This class is used to edit collection title and description on click of pencil image  
 *
 *
 */
	private class OnEditImageClick implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			editSelfCollectionSaveButton.getElement().getStyle()
					.setDisplay(Display.BLOCK);
			editSelfCollectionSaveButtonCancel.getElement().getStyle()
					.setDisplay(Display.BLOCK);
			/*
			 * editSelfCollectionDescSaveButton.getElement().getStyle().setDisplay
			 * (Display.NONE);
			 * editSelfCollectionDescSaveButtonCancel.getElement(
			 * ).getStyle().setDisplay(Display.NONE);
			 */
			collectionTitleUc.switchToEdit();
			isShowTitlePencil=false;
			collectionEditImageLbl.setVisible(false);

		}
	}

	/**
	 * edit the collection title of copy collection.
	 * 
	 */
	@Override
	public void editCopyCollectionTitle() {
		isEditCopy=true;
		editSelfCollectionSaveButton.getElement().getStyle()
		.setDisplay(Display.BLOCK);
        editSelfCollectionSaveButtonCancel.getElement().getStyle()
		.setDisplay(Display.BLOCK);
		collectionEditImageLbl.setVisible(false);
		collectionTitleUc.switchToEdit();
	}
	
	/**
	 * Set focus to the collection title of copy collection.
	 * 
	 */
	@Override
	public void setFocusCollectionTitle() {
		collectionTitleUc.setFocus();
	}
	
	public class OnCollectionDescriptionClick implements MouseOverHandler {
		@Override
		public void onMouseOver(MouseOverEvent event) {
			if(isShowDescPencil)
			simplePencilPanel.setVisible(true);
		}
	}

	public class OnCollectionDescriptionOut implements MouseOutHandler {
		@Override
		public void onMouseOut(MouseOutEvent event) {
			simplePencilPanel.setVisible(false);
		}
	}
/**
 * This method is used to update and save collection title
 * @param event instance of ClickEvent
 */
	@UiHandler("editSelfCollectionSaveButton")
	public void clickedOneditSelfCollectionSaveButton(ClickEvent event) {
		
		Map<String, String> parms = new HashMap<String, String>();
		parms.put("text", collectionTitleUc.getText());
		AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean value) {
				boolean isHavingBadWords = value;
				if (value){
					collectionTitleUc.getElement().getStyle().setBorderColor("orange");
//					mandatoryErrorLbl.setText(MessageProperties.i18n.GL0554);
//					mandatoryErrorLbl.setVisible(true);
				}else{
					titleAlertMessageLbl.setVisible(false);
					collectionTitleUc.switchToLabel();
					AppClientFactory.fireEvent(new UpdateSocialShareMetaDataEvent(
							collectionTitleUc.getText(), collectionDescriptionUc.getText(),
							categoryImage.getUrl()));
					MixpanelUtil.mixpanelEvent("Collaborator_edits_collection");
					isShowTitlePencil=true;
					
					collectionTitleUc.getElement().getStyle().clearBackgroundColor();
					collectionTitleUc.getElement().getStyle().setBorderColor("#ccc");
//					mandatoryErrorLbl.setVisible(false);
					lblCharLimit.setVisible(false);
				}
			}
		});
		
	}
	/**
	 * This method is used to update and save collection description
	 * @param event instance of ClickEvent
	 */
	@UiHandler("editSelfCollectionDescSaveButton")
	public void clickedOneditFolderDescSaveButton(ClickEvent event) {
		descriptionAlertMessageLbl.setVisible(false);
		collectionDescriptionUc.switchToLabel();
		AppClientFactory.fireEvent(new UpdateSocialShareMetaDataEvent(
				collectionTitleUc.getText(), collectionDescriptionUc.getText(),
				categoryImage.getUrl()));
		MixpanelUtil.mixpanelEvent("Collaborator_edits_collection");
		 isShowDescPencil=true;
		 lblCharLimit.setVisible(false);
	}
	/**
	 * This method is used to cancel collection title update
	 * @param event instance of ClickEvent
	 */
	@UiHandler("editSelfCollectionSaveButtonCancel")
	public void clickedOneditSelfCollectionSaveButtonCancel(ClickEvent event) {
		titleAlertMessageLbl.addStyleName("titleAlertMessageDeActive");
		titleAlertMessageLbl.removeStyleName("titleAlertMessageActive");
		titleAlertMessageLbl.setVisible(false);
		editSelfCollectionSaveButton.getElement().getStyle()
				.setDisplay(Display.NONE);
		editSelfCollectionSaveButtonCancel.getElement().getStyle()
				.setDisplay(Display.NONE);
		collectionTitleUc.switchToCancelLabel();
		collectionTitleUc.setText(collectionTitleUc.getText());
		isShowTitlePencil=true;
		lblCharLimit.setVisible(false);

	}
	/**
	 * This method is used to cancel collection description update
	 * @param event instance of ClickEvent
	 */
	@UiHandler("editSelfCollectionDescSaveButtonCancel")
	public void clickedOnEditFolderDescSaveButtonCancel(ClickEvent event) {
		descriptionAlertMessageLbl.addStyleName("titleAlertMessageDeActive");
		descriptionAlertMessageLbl.removeStyleName("titleAlertMessageActive");
		descriptionAlertMessageLbl.setVisible(false);
		editSelfCollectionDescSaveButton.getElement().getStyle()
				.setDisplay(Display.NONE);
		editSelfCollectionDescSaveButtonCancel.getElement().getStyle()
				.setDisplay(Display.NONE);
        isShowDescPencil=true;
		collectionDescriptionUc.switchToDesCancelLabel();
		lblCharLimit.setVisible(false);
	}

	public void analyticsClickEvent(){
			setPersistantTabFlag("statisticsTab");
			if(!statisticsTabVc.getStyleName().contains("deactivated")) {

				new CollectionAnalyticsUc(AppClientFactory.getPlaceManager().getRequestParameter("id"), collectionDo.getTitle(), null) {

					@Override
					public void setDefaultTab() {
						if(refresh) {
							setTab(getPersistantTabObjectUsingTabFlag());
						}
					}
				};
				Window.enableScrolling(false);
				AppClientFactory.fireEvent(new SetHeaderZIndexEvent(0, false));
		}
	}
	
	public class AnalyticsClickEvent implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			   refresh = false;
			   MixpanelUtil.mixpanelEvent("Organize_With_Analytics");
			   analyticsClickEvent();
			}
	}
	

	@Override
	public void setNoDataCollection() {
		//temporary fix
//		noCollectionResetPanel.getElement().getStyle().setDisplay(Display.NONE);
		////
		if (AppClientFactory.isAnonymous()){
			Window.enableScrolling(true);
		}else{
			Window.enableScrolling(false);
		}
		AppClientFactory.fireEvent(new SetHeaderZIndexEvent(0, true));
		
		noCollectionResetPanel.clear();
		noCollectionResetPanel.add(new FoldersWelcomePage());
		shelfTabSimPanel.setVisible(false);
		folderListPanel.setVisible(false);
		loadingImageLabel.setVisible(false);
		editPanel.setVisible(false);
		noCollectionResetPanel.setVisible(true);
		panelFoooter.setVisible(false);
		scrollContainer.setVisible(false);
		getLoadingImageInvisible();
	}
	
	@Override
	public void setOnlyNoDataCollection() {
		
		getLoadingImageInvisible();
//		Window.enableScrolling(true);
		AppClientFactory.fireEvent(new SetHeaderZIndexEvent(0, true));
		
		shelfTabSimPanel.setVisible(false);
		folderListPanel.setVisible(false);
		loadingImageLabel.setVisible(false);
		editPanel.setVisible(false);
		panelFoooter.setVisible(false);
		scrollContainer.setVisible(false);
		noCollectionResetPanel.clear();
		noCollectionResetPanel.add(new FoldersWelcomePage());
		noCollectionResetPanel.setVisible(true);
	}

	@Override
	public void setBalloonPopup() {
//		BalloonPopupVc balloonPopup = new BalloonPopupVc("",i18n.GL0683+i18n.GL_SPL_EXCLAMATION);
//		collPopup.add(balloonPopup);

		BalloonPopupVc balloonStatsPopup = new BalloonPopupVc("",i18n.GL0682());
		balloonStatsPopup.getPopupContainer().getElement().removeClassName("hoverCollPopupContainerPos");
		balloonStatsPopup.getPopupContainer().getElement().addClassName("hoverStasPopupContainerPos");
		balloonStatsPopup.getPopupDesc().getElement().getStyle().setColor("#515151");
		balloonStatsPopup.getPopupDesc().getElement().getStyle().setFontSize(12, Unit.PX);
		statPopup.add(balloonStatsPopup);
	}

	public FlowPanel getShelfViewMainContainer() {
		return shelfViewMainContainer;
	}

	public void updateResoureCount(int resourceCount) {
		com.google.gwt.core.shared.GWT.log("resource count" + resourceCount);
		resourceTabVc.setLabel(""+i18n.GL0829()+" (" + resourceCount + ")");
		
		setIpadFriendly();
	}
/**
 * This method is used to hide loading image
 */
	@Override
	public void getLoadingImageInvisible() {
		backToSearchFloPanel.setVisible(true);
		loadingImageLabel.setVisible(false);
		panelFoooter.setVisible(true);
	}
	/**
	 * This method is used to display loading image
	 */
	public void getLoadingImageVisible() {
		backToSearchFloPanel.setVisible(false);
		loadingImageLabel.setVisible(true);
		panelFoooter.setVisible(false);
	}

	@Override
	public void hideAllOpenedPopUp() {
		if(deleteConfirmPopup!=null && deleteConfirmPopup.isShowing()){
			deleteConfirmPopup.hide();
		}
		
	}
	/**
	 * @function getCreatedTime 
	 * 
	 * @created_date : 06-Jan-2014
	 * 
	 * @description
	 * 
	 * @parm(s) : @param commentCreatedTime
	 * 
	 * @return : String
	 */
	private String getTimeStamp(String commentCreatedTime) {
		String createdTime = null;
		Long currentTime = System.currentTimeMillis();
		Long commentTime = Long.parseLong(commentCreatedTime);
		Long elapsedTime = (currentTime - commentTime);
		
		int seconds = (int) (elapsedTime / 1000) % 60 ;
		int minutes = (int) ((elapsedTime / (1000*60)) % 60);
		int hours   = (int) ((elapsedTime / (1000*60*60)) % 24);
		int days = (int) (elapsedTime / (1000*60*60*24));
		
		if(days>0) {
			createdTime = days + getTimePrefix(days," "+i18n.GL0562(), i18n.GL0579(), i18n.GL0580());
		} else if(hours>0&&hours<24) {
			createdTime = hours + getTimePrefix(hours," "+i18n.GL0563(), i18n.GL1435(), i18n.GL1436());
		} else if(minutes>0&&minutes<60) {
			createdTime = minutes + getTimePrefix(minutes," "+i18n.GL0564(), i18n.GL1437(), i18n.GL1438());
		} else if(seconds<=60) {
			createdTime = i18n.GL0561();
		}
		return createdTime;
	}
	/**
	 * @function getTimePrefix 
	 * 
	 * @created_date : 06-Jan-2014
	 * 
	 * @description
	 * 
	 * @parm(s) : @param count
	 * @parm(s) : @param msg
	 * @parm(s) : @param regex
	 * @parm(s) : @param replacement
	 * 
	 * @return : String
	 *
	 */
	private String getTimePrefix(int count, String msg, String regex, String replacement) {
		if(count==1) {
			msg = msg.replaceAll(regex, replacement);
		}
		return msg;
	}

	@Override
	public SimplePanel getFolderListPanel() {
		return folderListPanel;
	}

	@Override
	public void CollMovedSucessFully(String sourceId,String targetId,String folderName,HashMap<String, String> urlParams) {
		folderPopupUc.hide();
		final FolderDo folderDo = getFolderDo(collectionDo); 
		final DeleteFolderSuccessView deleteFolderSuccessView=new DeleteFolderSuccessView(folderName) { 
			@Override
			public void onClickPositiveButton(ClickEvent event) {
//				Window.enableScrolling(true);
				appPopUp.hide();
				AppClientFactory.fireEvent(new SetCollectionMovedStyleEvent(folderDo.getGooruOid()));  
			}
		};
		
		HashMap<String, String> params = new HashMap<String, String>();
		
		if(params.get(O3_LEVEL)!=null){
			AppClientFactory.fireEvent(new RemoveMovedCollectionFolderEvent(sourceId)); 
			AppClientFactory.fireEvent(new InsertMovedCollectionEvent(folderDo, RefreshFolderType.INSERT, urlParams)); 
		}else if(params.get(O2_LEVEL)!=null){
			AppClientFactory.fireEvent(new RemoveMovedCollectionFolderEvent(sourceId)); 
			AppClientFactory.fireEvent(new InsertMovedCollectionEvent(folderDo, RefreshFolderType.INSERT, urlParams));
		}else if(params.get(O1_LEVEL)!=null){
			AppClientFactory.fireEvent(new RemoveMovedCollectionFolderEvent(sourceId)); 
			AppClientFactory.fireEvent(new InsertMovedCollectionEvent(folderDo, RefreshFolderType.INSERT, urlParams));
		}else{
			AppClientFactory.fireEvent(new RemoveMovedCollectionFolderEvent(sourceId));
			AppClientFactory.fireEvent(new InsertMovedCollectionEvent(folderDo, RefreshFolderType.INSERT, urlParams));
			getLoadingImageInvisible();
		}
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
				resourceFormatDo.setValue(collectionItemDo.getCategory());
				folderItems.add(folderItemDo);
			}
			folderDo.setCollectionItems(folderItems);
		}
		return folderDo;
	}
	
	
	public void showNotInProgress(){
		deleteUserCollectionLbl.setVisible(true);
		lblDeleting.setVisible(false);
		
//		Window.enableScrolling(true);
		AppClientFactory.fireEvent(new SetHeaderZIndexEvent(0, true));
	}
	
	public void hideDeletePopup(DeletePopupViewVc delete){
		showNotInProgress();
		delete.hide();
	}
	/** 
	 * This method is to get the loadingImageLabel
	 */
	@Override
	public HTMLPanel getLoadingImageLabel() {
		return loadingImageLabel;
	}
	
	public static void onClosingAndriodorIpaddiv()
	{
		  scrollContainer.getElement().setAttribute("style", "position:fixed;");
	}
	
	@UiHandler("rbPublic")
	public void clickOnPublish(ClickEvent clickEvent){
		if(collectionShareTabVc!=null){
			collectionShareTabVc.clickOnPublic();
		}else{
			getCollectionShareTabVc().clickOnPublic();
		}
	
	}
	
	@Override
	public void setPusblishStatus(String publishStatus, CollectionDo colleDo) {
		
		if(publishStatus!=null){
			collectionDo.setPublishStatus(colleDo.getPublishStatus());
			if(publishStatus.equalsIgnoreCase("pending")){
				rbPublic.setVisible(false);
				lblPublishPending.setVisible(true);	
				publishedPanel.setVisible(false);
			}
			if(publishStatus.equalsIgnoreCase("Reviewed")){
				rbPublic.setVisible(false);
				lblPublishPending.setVisible(false);	
				publishedPanel.setVisible(true);
			}
			
		}else{
			rbPublic.setVisible(true);
			lblPublishPending.setVisible(false);	
			publishedPanel.setVisible(false);
		}
	}
	
	public static native void setFolderListHeight() /*-{
		var element = document.getElementById("wrapper");
		if (element !=null){
			alert("test 1");
			$wnd.element.parentNode.className = "wrapperParent";
		}
	}-*/;

	/** 
	 * This method is to get the editPanel
	 */
	@Override
	public HTMLPanel getEditPanel() {
		return editPanel;
	}

	/** 
	 * This method is to set the editPanel
	 */
	public void setEditPanel(HTMLPanel editPanel) {
		this.editPanel = editPanel;
	}

}
