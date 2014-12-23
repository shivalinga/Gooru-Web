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
package org.ednovo.gooru.client.mvp.shelf.collection.tab.collaborators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BaseViewWithHandlers;
import org.ednovo.gooru.client.mvp.faq.TermsOfUse;
import org.ednovo.gooru.client.mvp.search.event.RemoveCollaboratorObjectEvent;
import org.ednovo.gooru.client.mvp.search.event.SetCollabCountEvent;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderZIndexEvent;
import org.ednovo.gooru.client.mvp.search.event.SetPanelVisibilityEvent;
import org.ednovo.gooru.client.mvp.search.event.SetPanelVisibilityHandler;
import org.ednovo.gooru.client.mvp.shelf.ShelfPresenter;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.collaborators.vc.CollaboratorViewVc;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.collaborators.vc.DeletePopupViewVc;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.collaborators.vc.SuccessPopupViewVc;
import org.ednovo.gooru.client.uc.suggestbox.widget.AutoSuggestForm;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.content.CollaboratorsDo;
import org.ednovo.gooru.shared.model.content.CollectionDo;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @fileName : CollectionCollaboratorsTabView.java
 *
 * @description : 
 *
 *
 * @version : 1.0
 *
 * @date: Jan 23, 2014
 *
 * @Author Gooru Team
 *
 * @Reviewer:
 */
public class CollectionCollaboratorsTabView extends BaseViewWithHandlers<CollectionCollaboratorsTabUiHandlers> implements IsCollectionCollaboratorsTab, SelectionHandler<SuggestOracle.Suggestion>  {

	@UiField(provided = true)
	CollectionCollaboratorsCBundle res;
	
	private static CollectionAssignViewTabUiBinder uiBinder = GWT.create(CollectionAssignViewTabUiBinder.class);
	
	private MessageProperties i18n = GWT.create(MessageProperties.class);
	
	String EMAIL_REGEX = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
	
	CollectionDo collectionDo = null;
	
	@UiField HTMLPanel panelViewMode, panelEditMode,panelCreator,panelCollaboratorsList,panelLoading, panelPendingSmallLoadingIcon,privacyLabelPanel;//, panelCurrentSmallLoadingIcon;
	
	@UiField VerticalPanel panelPendingCollabListContainer, panelActiveCollabListContainer;
	
	@UiField HTMLPanel panelCode,panelPendingContainer, panelActiveContainer, panelSuggestBox, panelActions;
	
	@UiField Label lblCollectionCreator, lblCurrentCollaborators, lblYouAreTheCollectionCreator, lblAddCollaborator, lblCollaboratorsDesc, lblInviteCollaborators, lblErrorMessage;
	
	@UiField Label lblPendingInvitations, lblCurrentCollabTitle, lblPleaseWait, lblText/*, lblToolTip*/;
	
	@UiField Button btnInvite, btnRemoveSelectedInvities;
	
	@UiField InlineLabel lblPii,toUsText;
	@UiField Anchor ancprivacy;
	
	AutoSuggestForm autoSuggetTextBox =null;
	
	MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	
//	private PopupPanel toolTipPopupPanel=new PopupPanel();
	
	private static int collabLimitCount = 20;
	
	int currentCollabCount=0;
	
	int overAllCollabCount = 0;
	
	private TermsOfUse termsOfUse;
	
//	@UiField TextBox txtCollaboratorsName;

//	SuggestBox suggestBox =null;
	
	interface CollectionAssignViewTabUiBinder extends UiBinder<Widget, CollectionCollaboratorsTabView> {
	}

		/**
	 * Class constructor
	 */
	public CollectionCollaboratorsTabView() {
		
		res = CollectionCollaboratorsCBundle.INSTANCE;
		CollectionCollaboratorsCBundle.INSTANCE.css().ensureInjected();
		setWidget(uiBinder.createAndBindUi(this));
		
		SetPanelVisibilityHandler panelHander = new SetPanelVisibilityHandler() {
			
			@Override
			public void setPendingActiveVisibility() {
				setPanelsVisibility();
			}
		};
		AppClientFactory.getEventBus().addHandler(SetPanelVisibilityEvent.TYPE, panelHander);
		
		clearContainers();
	}
	
	/**
	 * 
	 */
	@Override
	public void onLoad() {
		
	}
	
	@Override
	public void onUnload() {
//		setLabelsAndIds();
		
	}

	@Override
	public void reset() {
		super.reset();
	}
	
	/**
	 * 
	 * @function setLabelsAndIds 
	 * 
	 * @created_date : Jan 23, 2014
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
	public void setLabelsAndIds(){
		
		lblCollectionCreator.setText(i18n.GL0936());
		lblCollectionCreator.getElement().setId("lblCollectionCreator");
		lblCollectionCreator.getElement().setAttribute("alt",i18n.GL0936());
		lblCollectionCreator.getElement().setAttribute("title",i18n.GL0936());
		
		lblCurrentCollaborators.setText(i18n.GL0939());
		lblCurrentCollaborators.getElement().setId("lblCurrentCollaborators");
		lblCurrentCollaborators.getElement().setAttribute("alt",i18n.GL0939());
		lblCurrentCollaborators.getElement().setAttribute("title",i18n.GL0939());
		
		lblYouAreTheCollectionCreator.setText(i18n.GL0940());
		lblYouAreTheCollectionCreator.getElement().setId("lblYouAreTheCollectionCreator");
		lblYouAreTheCollectionCreator.getElement().setAttribute("alt",i18n.GL0940());
		lblYouAreTheCollectionCreator.getElement().setAttribute("title",i18n.GL0940());
		
		lblAddCollaborator.setText(i18n.GL0941());
		lblAddCollaborator.getElement().setId("lblAddCollaborator");
		lblAddCollaborator.getElement().setAttribute("alt",i18n.GL0941());
		lblAddCollaborator.getElement().setAttribute("title",i18n.GL0941());
		
		lblCollaboratorsDesc.getElement().setId("lblCollaboratorsDesc");
		
		
		lblInviteCollaborators.setText(i18n.GL0943());
		lblInviteCollaborators.getElement().setId("lblInviteCollaborators");
		lblInviteCollaborators.getElement().setAttribute("alt",i18n.GL0943());
		lblInviteCollaborators.getElement().setAttribute("title",i18n.GL0943());
		
		lblPendingInvitations.setText(i18n.GL1114());
		lblPendingInvitations.getElement().setId("lblPendingInvitations");
		lblPendingInvitations.getElement().setAttribute("alt",i18n.GL1114());
		lblPendingInvitations.getElement().setAttribute("title",i18n.GL1114());
		
		lblCurrentCollabTitle.setText(i18n.GL1113());
		lblCurrentCollabTitle.getElement().setId("lblCurrentCollabTitle");
		lblCurrentCollabTitle.getElement().setAttribute("alt",i18n.GL1113());
		lblCurrentCollabTitle.getElement().setAttribute("title",i18n.GL1113());
		
		btnInvite.setText(i18n.GL0944());
		btnInvite.getElement().setId("btnInvite");
		btnInvite.getElement().setAttribute("alt",i18n.GL0944());
		btnInvite.getElement().setAttribute("title",i18n.GL0944());
		
		lblPii.setText(i18n.GL1892());
		lblPii.getElement().setId("lblPii");
		lblPii.getElement().setAttribute("alt",i18n.GL1892());
		lblPii.getElement().setAttribute("title",i18n.GL1892());
		
		privacyLabelPanel.setVisible(false);
		ancprivacy.setText(i18n.GL1893());
		ancprivacy.getElement().setId("lnkAncprivacy");
		ancprivacy.getElement().setAttribute("alt",i18n.GL1893());
		ancprivacy.getElement().setAttribute("title",i18n.GL1893());
		
		toUsText.setText(i18n.GL1894());
		toUsText.getElement().setId("spnToUsText");
		toUsText.getElement().setAttribute("alt",i18n.GL1894());
		toUsText.getElement().setAttribute("title",i18n.GL1894());
	
		btnInvite.setEnabled(false);
		panelActions.getElement().addClassName(res.css().buttonTooltip());
		btnInvite.setVisible(true);
		lblText.setText(i18n.GL1184());
		lblText.getElement().setId("lblText");
		lblText.getElement().setAttribute("alt",i18n.GL1184());
		lblText.getElement().setAttribute("title",i18n.GL1184());
		
//		btnInvite.addMouseOverHandler(new OnBtnInviteMouseOver());
//		btnInvite.addMouseOutHandler(new OnBtnInviteMouseOut());
		
		btnRemoveSelectedInvities.setText(i18n.GL0237());
		btnRemoveSelectedInvities.getElement().setId("btnRemoveSelectedInvities");
		btnRemoveSelectedInvities.getElement().setAttribute("alt",i18n.GL0237());
		btnRemoveSelectedInvities.getElement().setAttribute("title",i18n.GL0237());
		
		btnRemoveSelectedInvities.setVisible(false);
		
		
		
/*		lblToolTip.setText(i18n.GL1165_1);
		lblToolTip.setVisible(false);
*/		
		lblErrorMessage.setVisible(false);
		
		setInviteButtonEnable(overAllCollabCount);
		
		lblPleaseWait.setText(i18n.GL1137());
		lblPleaseWait.getElement().setId("lblPleaseWait");
		lblPleaseWait.getElement().setAttribute("alt",i18n.GL1137());
		lblPleaseWait.getElement().setAttribute("title",i18n.GL1137());
		
		panelCollaboratorsList.clear();		// View mode
		panelPendingCollabListContainer.clear();	//edit mode
		panelActiveCollabListContainer.clear();	//edit mode
		
		lblPleaseWait.setVisible(false);
		
		createAutoSuggestBox();
		
		panelViewMode.getElement().setId("pnlPanelViewMode");
		panelCreator.getElement().setId("pnlPanelCreator");
		panelCollaboratorsList.getElement().setId("pnlPanelCollaboratorsList");
		panelLoading.getElement().setId("pnlPanelLoading");
		panelEditMode.getElement().setId("pnlPanelEditMode");
		panelSuggestBox.getElement().setId("pnlPanelSuggestBox");
		panelActions.getElement().setId("pnlPanelActions");
		panelCode.getElement().setId("pnlPanelCode");
		lblErrorMessage.getElement().setId("lblErrorMessage");
		privacyLabelPanel.getElement().setId("pnlPrivacyLabelPanel");
		panelPendingSmallLoadingIcon.getElement().setId("pnlPanelPendingSmallLoadingIcon");
		panelPendingContainer.getElement().setId("pnlPanelPendingContainer");
		panelPendingCollabListContainer.getElement().setId("vpnlPanelPendingCollabListContainer");
		panelActiveContainer.getElement().setId("pnlPanelActiveContainer");
		panelActiveCollabListContainer.getElement().setId("vpnlPanelActiveCollabListContainer");
	}
	/**
	 * @function setInviteButtonEnable 
	 * 
	 * @created_date : Mar 4, 2014
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
	@Override
	public void setInviteButtonEnable(int currentCount) {
		currentCollabCount = currentCount;
		if (currentCollabCount >=collabLimitCount){
			btnInvite.getElement().addClassName("disabled");
			btnInvite.setEnabled(false);
			panelActions.getElement().addClassName(res.css().buttonTooltip());
		}else{
			panelActions.getElement().removeClassName(res.css().buttonTooltip());
			btnInvite.getElement().removeClassName("disabled");
			btnInvite.setEnabled(true);
		}
	}

	/**
	 * @function createAutoSuggestBox 
	 * 
	 * @created_date : Jan 30, 2014
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
	
	private void createAutoSuggestBox() {
		panelSuggestBox.setStyleName("auto_suggest");
		autoSuggetTextBox = new AutoSuggestForm(oracle) {

			@Override
			public void onSubmit(DomEvent<EventHandler> event) {
				// Nothing to do....
			}

			@Override
			public void dataObjectModel(String text) {
				if (text.trim().length() > 0){
					AppClientFactory.getInjector().getCollaboratorsService().getSuggestionByName(text.trim(), new SimpleAsyncCallback<List<String>>() {
	
						@Override
						public void onSuccess(List<String> lstOfSuggestedCollaborators) {
							autoSuggetTextBox.clearAutoSuggestData();
							for (String lstCollab : lstOfSuggestedCollaborators){
								oracle.add(lstCollab);
							}
							autoSuggetTextBox.setAutoSuggest(oracle);
						}
					});
				}
			}

			@Override
			public void keyPressOnTextBox(KeyPressEvent event) {
				lblErrorMessage.setVisible(false);
			}

			@Override
			public void errorMsgVisibility(boolean visibility, String emailId) {
				if (visibility){
					showErrorMessage(StringUtil.generateMessage(i18n.GL1019(), emailId));
				}else{
					lblErrorMessage.setVisible(false);
				}
			}

		};
		autoSuggetTextBox.clearAutoSuggestData();
		panelSuggestBox.clear();
		panelSuggestBox.add(autoSuggetTextBox);
		autoSuggetTextBox.getTxtInput().getTxtInputBox().setFocus(true);
	}

	@UiHandler("btnRemoveSelectedInvities")
	public void onClickRemoveCollab(ClickEvent event){
		List<String> emailIdsToRemove = null;
		if (panelActiveCollabListContainer.getWidgetCount()>0){
			//get the selected item
			emailIdsToRemove = getCollaboratorOject(panelActiveCollabListContainer);
		}
		if (emailIdsToRemove == null && panelPendingCollabListContainer.getWidgetCount()>0){
			//get the selected item			
			emailIdsToRemove = getCollaboratorOject(panelPendingCollabListContainer);
		}

		if (emailIdsToRemove != null){
			final String toRemove =  emailIdsToRemove.get(0);
			final String emailId = emailIdsToRemove.get(1);
			final String collectionId = collectionDo.getGooruOid();
			DeletePopupViewVc delete = new DeletePopupViewVc() {				
				@Override
				public void onClickPositiveButton(ClickEvent event) {
					AppClientFactory.getInjector().getCollaboratorsService().removeCollaboratorsFromListByEmailIds(collectionId, "[\"" + toRemove +"\"]", new SimpleAsyncCallback<Void>() {
						@Override
						public void onSuccess(Void result) {
							
							hide();
							Window.enableScrolling(true);
							AppClientFactory.fireEvent(new SetHeaderZIndexEvent(0, true));
							
							AppClientFactory.fireEvent(new RemoveCollaboratorObjectEvent(toRemove));
							AppClientFactory.fireEvent(new SetCollabCountEvent("decrementBy", 1));
//							collectionDo.getMeta().setCollaboratorCount(collectionDo.getMeta().getCollaboratorCount() -1);
							
							if(panelActiveCollabListContainer.getWidgetCount() <= 0){
								panelActiveContainer.setVisible(false);
							}
							if (panelPendingCollabListContainer.getWidgetCount() <= 0){
								panelPendingContainer.setVisible(false);
							}
						}
					});
				}
				
				@Override
				public void onClickNegitiveButton(ClickEvent event) {
					Window.enableScrolling(true);
					AppClientFactory.fireEvent(new SetHeaderZIndexEvent(0, true));
					hide();
				}
			};
			delete.setPopupTitle(i18n.GL1118());
			delete.setDescText(StringUtil.generateMessage(i18n.GL1119(), emailIdsToRemove.get(1) != null ? emailIdsToRemove.get(1) : emailIdsToRemove.get(0))); 
			delete.setPositiveButtonText(i18n.GL_GRR_YES());
			delete.setNegitiveButtonText(i18n.GL0142());
			delete.center();
			delete.show();
			
		}else{
			//give message...
		}
		autoSuggetTextBox.getTxtInput().getTxtInputBox().setFocus(true);
	}	

	private List<String> getCollaboratorOject(VerticalPanel vpanelCollab) {
		int count = 0;
		List<String> selectedEmailIds=null;
		Iterator<Widget> widgets = vpanelCollab.iterator();
		while (widgets.hasNext()) {
			Widget widget = widgets.next();
			if (widget instanceof CollaboratorViewVc) {
				selectedEmailIds =  ((CollaboratorViewVc) widget).getSelectedObjectId();
				if (selectedEmailIds !=null && selectedEmailIds.size() > 0){
					break;
				}
			}
			count++;
		}
		return selectedEmailIds;
	}
	@UiHandler("btnInvite")
	public void  OnClick(ClickEvent event){
		//Check for null - Check for Empty
		lblPleaseWait.setVisible(true);
		btnInvite.setVisible(false);
		String collabEmailIds = autoSuggetTextBox.getSelectedItemsAsString();
		String emailIds[] = collabEmailIds.trim().split("\\s*,\\s*");
		List<String> lstEmailID = new ArrayList<String>();
		
		for (int i=0; i<emailIds.length; i++){
			lstEmailID.add("\""+emailIds[i].toLowerCase().trim()+"\"");
		}
		if (collabEmailIds != null && collabEmailIds.equalsIgnoreCase("")){
//			showErrorMessage(MessageProperties.i18n.GL1015);
			lblPleaseWait.setVisible(false);
			btnInvite.setVisible(true);
			return;
		}
		
		currentCollabCount = emailIds.length + overAllCollabCount;
		if (currentCollabCount > collabLimitCount){
			lblPleaseWait.setVisible(false);
			btnInvite.setVisible(true);
			showErrorMessage(StringUtil.generateMessage(i18n.GL1016(), ""+collabLimitCount));
			return;
		}
				
		//Check for Valid Email ID format
		boolean from;
		for (int i=0; i<emailIds.length; i++){
			String emailID = emailIds[i].toLowerCase().trim();
			from = emailID.matches(EMAIL_REGEX);
			if (!from){
				lblPleaseWait.setVisible(false);
				btnInvite.setVisible(true);
				showErrorMessage(StringUtil.generateMessage(i18n.GL1019(), emailID));
				return;
			}
		}
		if (AppClientFactory.getLoggedInUser().getEmailId()!=null){
			boolean isValid=true;
			for (int i=0; i<emailIds.length; i++){
				String emailId = emailIds[i].toLowerCase().trim();
				if (emailId.equalsIgnoreCase(AppClientFactory.getLoggedInUser().getEmailId())){
					lblPleaseWait.setVisible(false);
					btnInvite.setVisible(true);
					showErrorMessage(i18n.GL1018());
					isValid = false;
					break;
				}
			}
			if (!isValid){
				return;
			}
		}
		


		btnInvite.getElement().addClassName("disabled");
		btnInvite.setEnabled(false);
//		panelActions.getElement().addClassName(res.css().buttonTooltip()); - this is not required
		//Call API to add the collaborators to the list.
		getUiHandlers().addCollaborators(collectionDo.getGooruOid(), lstEmailID);	// this will callback the displayCollaborators method.
	}
	
	public void showErrorMessage(String errorMessage){
		lblErrorMessage.setText(errorMessage);
		lblErrorMessage.getElement().setAttribute("alt",errorMessage);
		lblErrorMessage.getElement().setAttribute("title",errorMessage);
		lblErrorMessage.setVisible(true);
	}
	
	/**
	 * @param FolderDo
	 * 
	 */
	@Override
	public void displayView(CollectionDo collectionDo) {
		this.collectionDo = collectionDo;
		setLabelsAndIds();
		modifyStaticText(collectionDo.getCollectionType());
		overAllCollabCount = 0;
		setInviteButtonEnable(overAllCollabCount);

		setLoadingVisibility(true);
		setActiveCollabPanelVisibility(false);
		setPendingCollabPanelVisibility(false);
		
		panelEditMode.setVisible(false);
		panelViewMode.setVisible(false);
		lblCollectionCreator.setVisible(false);
		// If Collaborator display only view of all collaborators.
		if (collectionDo.getMeta() !=null && collectionDo.getMeta().isIsCollaborator()){
			panelViewMode.setVisible(true);
			lblCollectionCreator.setVisible(true);
			panelCreator.clear();
			CollaboratorViewVc collabView = new CollaboratorViewVc(AppClientFactory.getCurrentPlaceToken(), null, collectionDo, false, true, false) {
				@Override
				public void setCollabCount(int count, String type) {
					updateCollabCount(count, type);
				}
			};
			
			panelCreator.add(collabView);
			
			//Call Get collaborators API. This method will callback displayViewCollaboratorsByList.
			getUiHandlers().getCollaboratosListByCollectionId(collectionDo.getGooruOid(), "view");
			
		}else if (collectionDo.getMeta() !=null && !collectionDo.getMeta().isIsCollaborator()){
		// else if owner then display edit mode and list of all collaborators.
			panelEditMode.setVisible(true);
			//Call Get collaborators API. This method will callback displayActiveCollaboratorsByList & displayPendingCollaboratorsByList.
			getUiHandlers().getCollaboratosListByCollectionId(collectionDo.getGooruOid(), "edit");
			autoSuggetTextBox.getTxtInput().getTxtInputBox().setFocus(true);
//			panelEditMode.add(suggestBox);
		}
		
	}
	public void modifyStaticText(String collectionType){
		if(collectionType!=null&&collectionType.equals(ShelfPresenter.ASSESSMENT)){
			lblCollaboratorsDesc.setText(i18n.GL3035());
			lblCollaboratorsDesc.getElement().setAttribute("alt",i18n.GL3035());
			lblCollaboratorsDesc.getElement().setAttribute("title",i18n.GL3035());
		}else{
			lblCollaboratorsDesc.setText(i18n.GL0942());
			lblCollaboratorsDesc.getElement().setAttribute("alt",i18n.GL0942());
			lblCollaboratorsDesc.getElement().setAttribute("title",i18n.GL0942());
		}
	}
	private void updateCollabCount(int count, String type) {
//		collectionDo.getMeta().setCollaboratorCount(count);
		if (type.equalsIgnoreCase("decrementBy")){
			if (overAllCollabCount > count){
				overAllCollabCount -= count;
			}
		}else{
//			This is not called because we are adding the collaborators in same class.
//			overAllCollabCount += count;
		}
		setInviteButtonEnable(overAllCollabCount);
	}
	/* 
	 * @description 
	 * 	this method is responsible to display the list of collaborators in view mode.
	 */
	@Override
	public void displayViewCollaboratorsByList(List<CollaboratorsDo> listCollaboratosDo){
		// TODO create method to set the collobrators list from API.
		panelCollaboratorsList.clear();	
		panelLoading.setVisible(false);
		CollaboratorsDo collbDoYou = new CollaboratorsDo();
		collbDoYou.setEmailId(AppClientFactory.getLoggedInUser().getEmailId());
		collbDoYou.setGooruOid(collectionDo.getGooruOid());
		collbDoYou.setGooruUid(AppClientFactory.getLoggedInUser().getGooruUId());
		collbDoYou.setProfileImageUrl(AppClientFactory.getLoggedInUser().getProfileImageUrl() !=null ? AppClientFactory.getLoggedInUser().getProfileImageUrl() : "");
		collbDoYou.setStatus("active");
		collbDoYou.setUsername(AppClientFactory.getLoggedInUser().getUsername());
		
		
		for(CollaboratorsDo collbDo : listCollaboratosDo){
			boolean isYou = collbDo.getGooruUid().equalsIgnoreCase(AppClientFactory.getLoggedInUser().getGooruUId()) ? true : false;
			collbDoYou.setEmailId(collbDo.getEmailId());
			if (isYou){
				CollaboratorViewVc collabView = new CollaboratorViewVc(AppClientFactory.getCurrentPlaceToken(), collbDoYou, collectionDo, true, false, false) {
					@Override
					public void setCollabCount(int count, String type) {
						updateCollabCount(count, type);
					}
				};
				panelCollaboratorsList.add(collabView);
				break;
			}else{

			}
		}
		
		
		
		
		for(CollaboratorsDo collbDo : listCollaboratosDo){
			boolean isYou = collbDo.getGooruUid().equalsIgnoreCase(AppClientFactory.getLoggedInUser().getGooruUId()) ? true : false;
			if (isYou){
				
			}else{
				
				CollaboratorViewVc collabView1 = new CollaboratorViewVc(AppClientFactory.getCurrentPlaceToken(), collbDo, collectionDo, isYou, false, false) {
					@Override
					public void setCollabCount(int count, String type) {
						updateCollabCount(count, type);
					}
				};
				panelCollaboratorsList.add(collabView1);
			}
		}		
	}


	/* 
	 * @description 
	 * 	this method is responsible to display the list of active collaborators in edit mode.
	 */
	@Override
	public void displayActiveCollaboratorsByList(
			List<CollaboratorsDo> lstCollaborators) {
		int count=0;
		panelActiveCollabListContainer.clear();
		if (lstCollaborators.size() > 0){
			setActiveCollabPanelVisibility(true);
			for(CollaboratorsDo collbDo : lstCollaborators){
				
				CollaboratorViewVc collabView1 = new CollaboratorViewVc(AppClientFactory.getCurrentPlaceToken(), collbDo, collectionDo, false, false, false) {
					@Override
					public void setCollabCount(int count, String type) {
						updateCollabCount(count, type);
					}
				};
				panelActiveCollabListContainer.insert(collabView1, count);
				count++;
			}
			overAllCollabCount +=count;
		}
	}

	/* 
	 * @description 
	 * 	this method is responsible to display the list of pending collaborators in edit mode.
	 */
	@Override
	public void displayPendingCollaboratorsByList(
			List<CollaboratorsDo> lstCollaborators) {
		int count =0;
		panelPendingCollabListContainer.clear();
		if (lstCollaborators.size() > 0){
			setPendingCollabPanelVisibility(true);
			for(CollaboratorsDo collbDo : lstCollaborators){
				CollaboratorViewVc collabView1 = new CollaboratorViewVc(AppClientFactory.getCurrentPlaceToken(), collbDo, collectionDo, false, false, false) {
					@Override
					public void setCollabCount(int count, String type) {
						updateCollabCount(count, type);
					}
				};
				panelPendingCollabListContainer.insert(collabView1, count);
				count++;
			}
			overAllCollabCount +=count;
		}
	}

	/* 
	 * @description 
	 * 	this method is responsible to display the list of collaborators after successfully added.
	 */
	@Override
	public void displayCollaborators(List<CollaboratorsDo> collabList) {

		lblPleaseWait.setVisible(false);
		btnInvite.setVisible(true);
		btnInvite.getElement().removeClassName("disabled");
		btnInvite.setEnabled(true);
				
		final List<CollaboratorsDo> collabList1 = collabList;
		SuccessPopupViewVc success = new SuccessPopupViewVc() {
			
			@Override
			public void onClickPositiveButton(ClickEvent event) {
				int addedCount = 0;
				int pendingCount = 0;
				hide();
				for(CollaboratorsDo collbDo : collabList1){
					
						if (collbDo.getStatus().equalsIgnoreCase("active")){
							Document ele = Document.get();
							if (ele.getElementById(collbDo.getGooruUid()) == null){
								setActiveCollabPanelVisibility(true);
								
								CollaboratorViewVc collabView1 = new CollaboratorViewVc(AppClientFactory.getCurrentPlaceToken(), collbDo, collectionDo, false, false, true) {
									@Override
									public void setCollabCount(int count, String type) {
										updateCollabCount(count, type);
									}
								};
								panelActiveCollabListContainer.insert(collabView1, 0);
								addedCount++;
							}
						}else{
							Document ele = Document.get();
							if (ele.getElementById(collbDo.getEmailId()) == null){
								setPendingCollabPanelVisibility(true);
								CollaboratorViewVc collabView1 = new CollaboratorViewVc(AppClientFactory.getCurrentPlaceToken(), collbDo, collectionDo, false, false, true) {
									@Override
									public void setCollabCount(int count, String type) {
										updateCollabCount(count, type);
									}
								};
								panelPendingCollabListContainer.insert(collabView1, 0);
								pendingCount++;
								MixpanelUtil.mixpanelEvent("Collaborator_invite_pending");
							}
						}
					
					oracle.add(collbDo.getEmailId());
					MixpanelUtil.mixpanelEvent("Collaborator_invite_success");	//called for every user.
				}
				AppClientFactory.fireEvent(new SetCollabCountEvent("incrementBy", addedCount));
//				collectionDo.getMeta().setCollaboratorCount(collectionDo.getMeta().getCollaboratorCount() + addedCount);//this is not required since this is handled by service.
				overAllCollabCount +=addedCount;
				overAllCollabCount +=pendingCount;
				currentCollabCount = overAllCollabCount; 
				setInviteButtonEnable(currentCollabCount);
				autoSuggetTextBox.clearAutoSuggestData();
				createAutoSuggestBox();
				
				if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.COLLECTION_SEARCH) || AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)){
					Window.enableScrolling(false);
				}else{
					Window.enableScrolling(true);
				}
				AppClientFactory.fireEvent(new SetHeaderZIndexEvent(0, true));
				
				autoSuggetTextBox.getTxtInput().getTxtInputBox().setFocus(true);
			}
		};
		success.setPopupTitle(i18n.GL1120());
		if (collabList.size()>1){
			success.setDescText(i18n.GL1121());
		}else{
			success.setDescText(i18n.GL1360());
		}
		success.setPositiveButtonText(i18n.GL0190());
		success.center();
		success.show();
		
	}
	
	public void setPendingCollabPanelVisibility(boolean visibility){
		
		panelPendingContainer.setVisible(visibility);
	}
	public void setActiveCollabPanelVisibility(boolean visibility){
		panelActiveContainer.setVisible(visibility);
	}
	@Override
	public void setRemoveCollabButtonVisibility(boolean visibility){
		btnRemoveSelectedInvities.setVisible(false);
	}
	public void setLoadingVisibility(boolean visibility){
		panelPendingSmallLoadingIcon.setVisible(visibility);
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.logical.shared.SelectionHandler#onSelection(com.google.gwt.event.logical.shared.SelectionEvent)
	 */
	@Override
	public void onSelection(SelectionEvent<Suggestion> event) {
		//TODO ... Set the selection.
		
	}
	
	public void setPanelsVisibility(){
		if(panelActiveCollabListContainer.getWidgetCount() <= 0){
			panelActiveContainer.setVisible(false);
		}
		if (panelPendingCollabListContainer.getWidgetCount() <= 0){
			panelPendingContainer.setVisible(false);
		}
		btnInvite.setEnabled(true);
		btnInvite.removeStyleName("disabled");
		panelActions.getElement().removeClassName(res.css().buttonTooltip());
	}
	
	public void clearContainers(){
		panelActiveCollabListContainer.clear();
		panelPendingCollabListContainer.clear();
		panelCollaboratorsList.clear();	//View mode.
	}
	
//	public class OnBtnInviteMouseOver implements MouseOverHandler{
//
//		@Override
//		public void onMouseOver(MouseOverEvent event) {
//			toolTipPopupPanel.clear();
//			toolTipPopupPanel.setWidget(new GlobalToolTip(MessageProperties.i18n.GL1184,true));
//			toolTipPopupPanel.setStyleName("");
//			toolTipPopupPanel.setPopupPosition(btnInvite.getElement().getAbsoluteLeft()-35, btnInvite.getElement().getAbsoluteTop()+4);
//			toolTipPopupPanel.getElement().getStyle().setZIndex(999999);
//			toolTipPopupPanel.getElement().getStyle().setMarginLeft(40, Unit.PX);
//			toolTipPopupPanel.show();
//		}
//		
//	}
	
//	public class OnBtnInviteMouseOut implements MouseOutHandler{
//
//		@Override
//		public void onMouseOut(MouseOutEvent event) {
//			toolTipPopupPanel.hide();
//		}
//	}
	
	
	@UiHandler("ancprivacy")
	public void onClickPrivacyAnchor(ClickEvent clickEvent){
		Window.enableScrolling(false);
		AppClientFactory.fireEvent(new SetHeaderZIndexEvent(98, false));
		termsOfUse=new TermsOfUse(){

			@Override
			public void openParentPopup() {
				Window.enableScrolling(true);
				AppClientFactory.fireEvent(new SetHeaderZIndexEvent(0, true));
			}
			
		};
		termsOfUse.show();
		termsOfUse.setSize("902px", "300px");
		termsOfUse.center();
		termsOfUse.getElement().getStyle().setZIndex(999);//To display the view in collection player.
	}
	
}
