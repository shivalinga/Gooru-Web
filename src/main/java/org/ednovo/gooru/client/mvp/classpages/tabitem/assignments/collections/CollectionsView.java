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
package org.ednovo.gooru.client.mvp.classpages.tabitem.assignments.collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.child.ChildView;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.classpages.assignments.AddAssignmentContainerCBundle;
import org.ednovo.gooru.client.mvp.classpages.edit.EditClasspageView;
import org.ednovo.gooru.client.mvp.home.WaitPopupVc;
import org.ednovo.gooru.client.mvp.search.event.SetMarkButtonEvent;
import org.ednovo.gooru.client.mvp.search.event.SetMarkButtonHandler;
import org.ednovo.gooru.client.mvp.shelf.ShelfPresenter;
import org.ednovo.gooru.client.mvp.socialshare.event.UpdateSocialShareMetaDataEvent;
import org.ednovo.gooru.client.mvp.socialshare.event.UpdateSocialShareMetaDataHandler;
import org.ednovo.gooru.client.uc.tooltip.ToolTip;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.content.ClasspageItemDo;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

/**
 * 
 * @fileName : CollectionsView.java
 *
 * @description : This class is used to display the collection in an assignment tab.
 *
 *
 * @version : 1.0
 *
 * @date: May 10, 2013
 *
 * @Author Gooru Team
 *
 * @Reviewer:
 */
public class CollectionsView extends ChildView<CollectionsPresenter> implements IsCollectionsView{
	
	@UiField Button viewClassItemAnalyticsButton,editClassItemButton;
	
	@UiField FlowPanel dropdownPanel,editButtonsToolBar,classpageItemContainer,dropdownPanelAnalyticsButton;
	
	@UiField HTMLPanel directionContentPanel,thumbnailContainer, panelCircle;
	
	@UiField HTML learningObject;
	
	@UiField Anchor classpageItemTitle;
	
	@UiField Label editDueDateButton,editDirectionButton,deleteItemButton,dueDateText,dueDate,editCollection,directionsLabel,learningObjective,moniterProgress,collectionSummary,assignmentIndex;
	
	@UiField Image collectionImage;
	
	@UiField FlowPanel headerRightPanel;
	
	@UiField Label lblDirectionCharLimit;
	
	private  Button changeStatusButton;
	
	private EditToolBarView editToolBarView=null;
	
	private TextArea directionTextArea=null;
	
	private String directionText="";
	private Label directionErrorLabel=new Label();
	
	private ClasspageItemDo classpageItemDo=null;
	
	private WaitPopupVc removeConfirmBox=null;
	
	private static final String OPEN="open";
	private static final String COMPLETED="completed";
	
	
	private static CollectionsViewUiBinder uiBinder = GWT.create(CollectionsViewUiBinder.class);
	
	MessageProperties i18n = GWT.create(MessageProperties.class);
	
	public interface CollectionsViewUiBinder extends UiBinder<Widget, CollectionsView> {}
	
	public CollectionsView(){
		setButtonVisibility();
	}
	/**
	 * 
	 * @param classpageItemDo
	 * @param sequenceNum
	 */
	public CollectionsView(ClasspageItemDo classpageItemDo,int sequenceNum){
		initWidget(uiBinder.createAndBindUi(this));
		setStaticTexts();
		setPresenter(new CollectionsPresenter(this));
		
		CollectionsCBundle.INSTANCE.css().ensureInjected();
		AddAssignmentContainerCBundle.INSTANCE.css().ensureInjected();

		this.classpageItemDo=classpageItemDo;
		dropdownPanel.setVisible(false);
		dropdownPanelAnalyticsButton.setVisible(false);
		Event.addNativePreviewHandler(new NativePreviewHandler() {
	        public void onPreviewNativeEvent(NativePreviewEvent event) {
	        	hidePopup(event);
	          }
	    });
		setClasspageItemDo();
		setCollectionItemIndex(sequenceNum);
		editClassItemButton.addClickHandler(new OpenDropdownPanelEvent());
		viewClassItemAnalyticsButton.addClickHandler(new OpenAnalyticsDropdownPanelEvent());
		editDirectionButton.addClickHandler(new EditDirectionEvent());
		editDueDateButton.addClickHandler(new EditDueDateEvent());
		deleteItemButton.addClickHandler(new DeleteItemEvent());
		editCollection.addClickHandler(new CollectionEditEvent());
		
		moniterProgress.addClickHandler(new monitorProgressEvent());
		
		collectionSummary.addClickHandler(new CollectionSummaryEvent());
		/**
		 * Adding Event Handler.
		 * @param UpdateSocialShareMetaDataEvent.TYPE is type of event.
		 * @param setHeader is Object of Handler.
		 */
		AppClientFactory.getEventBus().addHandler(UpdateSocialShareMetaDataEvent.TYPE,setHeader);
		
		SetMarkButtonHandler setMarkButtonVisibility = new SetMarkButtonHandler() {

			@Override
			public void setVisibility() {
				setButtonVisibility();
			}
			
		};
		AppClientFactory.getEventBus().addHandler(SetMarkButtonEvent.TYPE,
				setMarkButtonVisibility);
		
		setButtonVisibility();
		
	}
	/**
	 * 
	 * @function setCollectionItemIndex 
	 * 
	 * @created_date : 07-Dec-2014
	 * 
	 * @description
	 * 
	 * 
	 * @parm(s) : @param sequenceNum
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	public void setCollectionItemIndex(int sequenceNum){
		assignmentIndex.setText(""+classpageItemDo.getSequenceNumber());
	}
	/**
	 * 
	 * @function setStaticTexts 
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
	public void setStaticTexts(){
		
		panelCircle.getElement().setId("pnlCircle");
		assignmentIndex.getElement().setId("lblAssignmentIndex");
		classpageItemContainer.getElement().setId("pnlClasspageItemContainer");
		editButtonsToolBar.getElement().setId("pnlEditButtonsToolBar");
		dueDateText.getElement().setId("lblDueDateText");
		dueDate.getElement().setId("lblDueDate");
		headerRightPanel.getElement().setId("pnlHeaderRight");
		dropdownPanelAnalyticsButton.getElement().setId("btnDropdownPanelAnalytics");
		dropdownPanel.getElement().setId("pnlDropdown");
		directionContentPanel.getElement().setId("pnlDirectionContent");
		thumbnailContainer.getElement().setId("pnlThumbnailContainer");
		collectionImage.getElement().setId("imgCollectionImage");
		
		viewClassItemAnalyticsButton.setText(i18n.GL0510());
		viewClassItemAnalyticsButton.getElement().setId("btnViewClassItemAnalytics");
		viewClassItemAnalyticsButton.getElement().setAttribute("alt",i18n.GL0510());
		viewClassItemAnalyticsButton.getElement().setAttribute("title",i18n.GL0510());
		
		editClassItemButton.setText(i18n.GL0140());
		editClassItemButton.getElement().setId("btnEditClassItem");
		editClassItemButton.getElement().setAttribute("alt",i18n.GL0140());
		editClassItemButton.getElement().setAttribute("title",i18n.GL0140());
		
		editDueDateButton.setText(i18n.GL1368());
		editDueDateButton.getElement().setId("btnEditDueDate");
		editDueDateButton.getElement().setAttribute("alt",i18n.GL1368());
		editDueDateButton.getElement().setAttribute("title",i18n.GL1368());
		
		editDirectionButton.setText(i18n.GL1369());
		editDirectionButton.getElement().setId("btnEditDirection");
		editDirectionButton.getElement().setAttribute("alt",i18n.GL1369());
		editDirectionButton.getElement().setAttribute("title",i18n.GL1369());
		
		editCollection.setText(i18n.GL1370());
		editCollection.getElement().setId("btnEditCollection");
		editCollection.getElement().setAttribute("alt",i18n.GL1370());
		editCollection.getElement().setAttribute("title",i18n.GL1370());
		
		deleteItemButton.setText(i18n.GL1371());
		deleteItemButton.getElement().setId("btnDeleteItem");
		deleteItemButton.getElement().setAttribute("alt",i18n.GL1371());
		deleteItemButton.getElement().setAttribute("title",i18n.GL1371());
		
		directionsLabel.setText(i18n.GL1372());	
		directionsLabel.getElement().setId("lblDirections");
		directionsLabel.getElement().setAttribute("alt",i18n.GL1372());
		directionsLabel.getElement().setAttribute("title",i18n.GL1372());
		
		learningObjective.setText(i18n.GL1373());
		learningObjective.getElement().setId("lblLearningObjective");
		learningObjective.getElement().setAttribute("alt",i18n.GL1373());
		learningObjective.getElement().setAttribute("title",i18n.GL1373());
		
		moniterProgress.setText(i18n.GL1586());
		moniterProgress.getElement().setId("lblMointerProgress");
		moniterProgress.getElement().setAttribute("alt",i18n.GL1586());
		moniterProgress.getElement().setAttribute("title",i18n.GL1586());
		
		collectionSummary.setText(i18n.GL1587());
		collectionSummary.getElement().setId("lblCollectionSummary");
		collectionSummary.getElement().setAttribute("alt",i18n.GL1587());
		collectionSummary.getElement().setAttribute("title",i18n.GL1587());
	
		classpageItemTitle.getElement().setId("lnkClasspageItemTitle");
		learningObject.getElement().setId("htmlLearningObject");
		
		String value = StringUtil.generateMessage(i18n.GL2103(), "500");
		lblDirectionCharLimit.setText(value);
		lblDirectionCharLimit.setVisible(false);
		StringUtil.setAttributes(lblDirectionCharLimit.getElement(), "lblDirectionCharLimit", value, value);
	}
	/**
	 * 
	 * @param classpageItemDo
	 * @param isStudentView
	 * @param sequenceNum
	 */
	public CollectionsView(ClasspageItemDo classpageItemDo,boolean isStudentView,int sequenceNum){
		initWidget(uiBinder.createAndBindUi(this));
		setPresenter(new CollectionsPresenter(this));
		CollectionsCBundle.INSTANCE.css().ensureInjected();
		this.classpageItemDo=classpageItemDo;
		headerRightPanel.clear();
		createChangeStatusButton();
		setClasspageItemDo();
		setCollectionItemIndex(sequenceNum);
		setStaticTexts();
		setReadStatus();
		setButtonVisibility();
	}
	/**
	 * 
	 * @function setClasspageItemDo 
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
	public void setClasspageItemDo(){
		this.directionText=this.classpageItemDo.getDirection();
		//editCollection.setHref("#"+PlaceTokens.SHELF+"&id="+classpageItemDo.getCollectionId());
		setClasspageItemTitle();
		setDirection(directionText);
		setDueDate();
		setLearningObject();
		setThumbnailUrl();
	}
	/**
	 * 
	 * @function setClasspageItemTitle 
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
	private void setClasspageItemTitle(){
		classpageItemTitle.setHTML(classpageItemDo.getCollectionTitle());
		if(classpageItemDo!=null && classpageItemDo.getCollectionType()!=null && classpageItemDo.getCollectionType().equals(ShelfPresenter.ASSESSMENT)){
			classpageItemTitle.setHref(AppClientFactory.loggedInUser.getSettings().getAssessementEndPoint());
			classpageItemTitle.setTarget("_blank");
		}else{
			classpageItemTitle.setHref("#"+PlaceTokens.COLLECTION_PLAY+"&id="+classpageItemDo.getCollectionId()+"&cid="+classpageItemDo.getCollectionItemId()+"&page="+getCurrentPlaceToken()+"&eventid="+AppClientFactory.getPlaceManager().getClasspageEventId());
		}
	}
	/**
	 * 
	 * @function getCurrentPlaceToken 
	 * 
	 * @created_date : 07-Dec-2014
	 * 
	 * @description
	 * 
	 * 
	 * @parm(s) : @return
	 * 
	 * @return : String
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	public String getCurrentPlaceToken(){
		String placeToken=AppClientFactory.getCurrentPlaceToken();
		if(placeToken!=null){
			if(placeToken.equals(PlaceTokens.EDIT_CLASSPAGE)){
				placeToken="teach";
			}else if(placeToken.equals(PlaceTokens.STUDENT)){
				placeToken="study";
			}
		}
		return placeToken;
	}
	/**
	 * 
	 * @function setDueDate 
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
	private void setDueDate(){
		String dueDate=classpageItemDo.getPlannedEndDate();
		if(dueDate!=null&&!dueDate.equals("")){
			dueDateText.setText(i18n.GL1390());
			dueDateText.getElement().setAttribute("alt",i18n.GL1390());
			dueDateText.getElement().setAttribute("title",i18n.GL1390());
			dueDateText.setStyleName(CollectionsCBundle.INSTANCE.css().dueDataIcon());
			this.dueDate.setText(dueDate.toString());
			this.dueDate.getElement().setAttribute("alt",dueDate.toString());
			this.dueDate.getElement().setAttribute("title",dueDate.toString());
		}
	}
	/**
	 * 
	 * @function setLearningObject 
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
	public void setLearningObject(){
		String learningObject=classpageItemDo.getGoal();
		if(learningObject!=null&&!learningObject.equals("")&&!learningObject.equals("null")){
			this.learningObject.setHTML(learningObject);
			this.learningObject.getElement().setAttribute("alt",learningObject);
			this.learningObject.getElement().setAttribute("title",learningObject);
		}else{
			this.learningObject.setStyleName(CollectionsCBundle.INSTANCE.css().systemMessage());
			this.learningObject.setHTML(i18n.GL1374());
			this.learningObject.getElement().setAttribute("alt",i18n.GL1374());
			this.learningObject.getElement().setAttribute("title",i18n.GL1374());
		}
	}
	/**
	 * 
	 * @function setThumbnailUrl 
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
	public void setThumbnailUrl(){
		collectionImage.setUrl(classpageItemDo.getThumbnailUrl()!=null?StringUtil.formThumbnailName(classpageItemDo.getThumbnailUrl(),"-160x120."):"null");
		Anchor thumbnailAnchor=new Anchor();
		if(classpageItemDo!=null && classpageItemDo.getCollectionType()!=null && classpageItemDo.getCollectionType().equals(ShelfPresenter.ASSESSMENT)){
			thumbnailAnchor.setHref(AppClientFactory.loggedInUser.getSettings().getAssessementEndPoint());
			thumbnailAnchor.setTarget("_blank");
		}else{
			thumbnailAnchor.setHref("#"+PlaceTokens.COLLECTION_PLAY+"&id="+classpageItemDo.getCollectionId()+"&cid="+classpageItemDo.getCollectionItemId()+"&page="+getCurrentPlaceToken()+"&eventid="+AppClientFactory.getPlaceManager().getClasspageEventId());
		}
		thumbnailAnchor.getElement().appendChild(collectionImage.getElement());
		thumbnailContainer.add(thumbnailAnchor);
	}
	/**
	 * 
	 * @function setReadStatus 
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
	public void setReadStatus(){
		if(classpageItemDo.getStatus().equalsIgnoreCase(OPEN)){
			editButtonsToolBar.setStyleName(CollectionsCBundle.INSTANCE.css().openStateCollectionHeader());
			panelCircle.removeStyleName(CollectionsCBundle.INSTANCE.css().completedStatus());
		}else{
			editButtonsToolBar.setStyleName(CollectionsCBundle.INSTANCE.css().completeStateCollectionHeader());
			panelCircle.addStyleName(CollectionsCBundle.INSTANCE.css().completedStatus());
		}
	}
	
	/**
	 * 
	 * @function createChangeStatusButton 
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
	public void createChangeStatusButton(){
		changeStatusButton=new Button();
		changeButtonText();
		changeStatusButton.setStyleName("secondary");
		changeStatusButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				changeCollectionStatus();
			}
		});
		headerRightPanel.add(changeStatusButton);
	}
	/**
	 * 
	 * @function changeCollectionStatus 
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
	public void changeCollectionStatus(){
		String readStatus=classpageItemDo.getStatus().equalsIgnoreCase(OPEN)?COMPLETED:OPEN;
		getPresenter().updateClasspageItem(classpageItemDo.getCollectionItemId(), null, null, readStatus);
	}
	/**
	 * 
	 * @function changeButtonText 
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
	public void changeButtonText(){
		String buttonText=classpageItemDo.getStatus().equalsIgnoreCase(OPEN)?"Mark as Complete":"Mark as Incomplete";
		changeStatusButton.setText(buttonText);
	}
	@UiHandler("collectionImage")
	public void setErrorImage(ErrorEvent event){
		collectionImage.setUrl("images/default-collection-image-160x120.png");
	}
	public void setDefaultMessage(){
		
	}
	/**
	 * 
	 * @fileName : CollectionsView.java
	 *
	 * @description : 
	 *
	 *
	 * @version : 1.0
	 *
	 * @date: 07-Dec-2014
	 *
	 * @Author Gooru Team
	 *
	 * @Reviewer:
	 */
	private class OpenDropdownPanelEvent implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			if(dropdownPanel.isVisible()){
				dropdownPanel.setVisible(false);
			}else{
				dropdownPanel.setVisible(true);
			}
		}
	}
	/**
	 * 
	 * @fileName : CollectionsView.java
	 *
	 * @description : 
	 *
	 *
	 * @version : 1.0
	 *
	 * @date: 07-Dec-2014
	 *
	 * @Author Gooru Team
	 *
	 * @Reviewer:
	 */
	private class OpenAnalyticsDropdownPanelEvent implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			String classpageId=AppClientFactory.getPlaceManager().getRequestParameter("classpageid", null);
			getPresenter().checkCollectionStaus(classpageId,classpageItemDo.getCollectionId());
			
		}
	}
	/**
	 * 
	 * @fileName : CollectionsView.java
	 *
	 * @description : 
	 *
	 *
	 * @version : 1.0
	 *
	 * @date: 07-Dec-2014
	 *
	 * @Author Gooru Team
	 *
	 * @Reviewer:
	 */
	private class EditDirectionEvent implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			dropdownPanel.setVisible(false);
			editDirection();
			editButtonsToolBar.setVisible(false);
			lblDirectionCharLimit.setVisible(true);
			editToolBarView=new EditToolBarView(false);
			if(classpageItemDo.getPlannedEndDate()!=null&&!classpageItemDo.getPlannedEndDate().toString().equals("")){
				editToolBarView.dueDatePanel.add(new Label(classpageItemDo.getPlannedEndDate()!=null?classpageItemDo.getPlannedEndDate().toString():"")); // TODO need to set date.
				editToolBarView.dueDateText.add(new Label(i18n.GL1390()));
				editToolBarView.dueDateText.setStyleName(CollectionsCBundle.INSTANCE.css().dueDataIcon());
				editToolBarView.dueDatePanel.setStyleName(CollectionsCBundle.INSTANCE.css().dateText());
			}
			editToolBarView.cancelButton.addClickHandler(new ResetEditContentEvent());
			editToolBarView.saveButton.addClickHandler(new UpdateEditedContentEvent());
			classpageItemContainer.insert(editToolBarView, 0);
		}
	}
	/**
	 * 
	 * @fileName : CollectionsView.java
	 *
	 * @description : 
	 *
	 *
	 * @version : 1.0
	 *
	 * @date: 07-Dec-2014
	 *
	 * @Author Gooru Team
	 *
	 * @Reviewer:
	 */
	private class EditDueDateEvent implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			dropdownPanel.setVisible(false);
			editButtonsToolBar.setVisible(false);
			editToolBarView=new EditToolBarView(true);
			editToolBarView.dueDateText.add(new Label(i18n.GL1390()));
			editToolBarView.dueDateText.setStyleName(CollectionsCBundle.INSTANCE.css().dueDataIcon());
			//editToolBarView.dateBoxUc.getDoneButton().addClickHandler(new OnDoneClick());
			editToolBarView.cancelButton.addClickHandler(new ResetEditContentEvent());
			editToolBarView.saveButton.addClickHandler(new UpdateEditedDueDateEvent());
			classpageItemContainer.insert(editToolBarView, 0);
		}
		
	}
	/**
	 * 
	 * @fileName : CollectionsView.java
	 *
	 * @description : 
	 *
	 *
	 * @version : 1.0
	 *
	 * @date: 07-Dec-2014
	 *
	 * @Author Gooru Team
	 *
	 * @Reviewer:
	 */
	private class DeleteItemEvent implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			dropdownPanel.setVisible(false);
			removeConfirmBox=new WaitPopupVc(i18n.GL1387(),i18n.GL1388()) {
				@Override
				public void onTextConfirmed() {
					getPresenter().deleteClasspageItem(classpageItemDo.getCollectionItemId());
				}
			};
		}
		
	}
	/**
	 * 
	 * @fileName : CollectionsView.java
	 *
	 * @description : 
	 *
	 *
	 * @version : 1.0
	 *
	 * @date: 07-Dec-2014
	 *
	 * @Author Gooru Team
	 *
	 * @Reviewer:
	 */
	private class CollectionEditEvent implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			getCollectionFolders();
		};
	}
	/**
	 * 
	 * @fileName : CollectionsView.java
	 *
	 * @description : 
	 *
	 *
	 * @version : 1.0
	 *
	 * @date: 07-Dec-2014
	 *
	 * @Author Gooru Team
	 *
	 * @Reviewer:
	 */
	private class CollectionSummaryEvent implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			
			String collectionId = classpageItemDo.getCollectionId();
			
			Map<String,String> params = new HashMap<String,String>();
			String pageSize=AppClientFactory.getPlaceManager().getRequestParameter("pageSize", null);
			String classpageid=AppClientFactory.getPlaceManager().getRequestParameter("classpageid", null);
			String pageNum=AppClientFactory.getPlaceManager().getRequestParameter("pageNum", null);
			String pos=AppClientFactory.getPlaceManager().getRequestParameter("pos", null);
			params.put("pageSize", pageSize);
			params.put("classpageid", classpageid);
			params.put("pageNum", pageNum);
			params.put("pos", pos);
			params.put("analyticsId", collectionId);
			PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(PlaceTokens.EDIT_CLASSPAGE, params);
			AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, true);
			EditClasspageView.setAnalyticsData();
		};
	}
	/**
	 * 
	 * @fileName : CollectionsView.java
	 *
	 * @description : 
	 *
	 *
	 * @version : 1.0
	 *
	 * @date: 07-Dec-2014
	 *
	 * @Author Gooru Team
	 *
	 * @Reviewer:
	 */
	private class monitorProgressEvent implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			
			String collectionId = classpageItemDo.getCollectionId();
			
			Map<String,String> params = new HashMap<String,String>();
			String pageSize=AppClientFactory.getPlaceManager().getRequestParameter("pageSize", null);
			String classpageid=AppClientFactory.getPlaceManager().getRequestParameter("classpageid", null);
			String pageNum=AppClientFactory.getPlaceManager().getRequestParameter("pageNum", null);
			String pos=AppClientFactory.getPlaceManager().getRequestParameter("pos", null);
			params.put("pageSize", pageSize);
			params.put("classpageid", classpageid);
			params.put("pageNum", pageNum);
			params.put("pos", pos);
			params.put("monitorid", collectionId);
			PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(PlaceTokens.EDIT_CLASSPAGE, params);
			AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, true);
			EditClasspageView.setAnalyticsMonitoringData();
		};
	}
	/**
	 * 
	 * @fileName : CollectionsView.java
	 *
	 * @description : 
	 *
	 *
	 * @version : 1.0
	 *
	 * @date: 07-Dec-2014
	 *
	 * @Author Gooru Team
	 *
	 * @Reviewer:
	 */
	private class ResetEditContentEvent implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			resetEditContent();
		}
	}
	/**
	 * 
	 * @fileName : CollectionsView.java
	 *
	 * @description : 
	 *
	 *
	 * @version : 1.0
	 *
	 * @date: 07-Dec-2014
	 *
	 * @Author Gooru Team
	 *
	 * @Reviewer:
	 */
	private class UpdateEditedDueDateEvent implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			if(editToolBarView!=null){
				hideButtons(true);
				if(editToolBarView.dateBoxUc!=null&&editToolBarView.dateBoxUc.getValue()!=null&&!editToolBarView.dateBoxUc.getDateBox().getValue().equals("")&&editToolBarView.dateBoxUc.getDate()!=null&&!editToolBarView.dateBoxUc.getDate().trim().equals("")){
					updateClasspageItem(null,editToolBarView.dateBoxUc.getDate(),null);
				}else{
					//TODO display error message when direction.
					resetEditContent();
				}
			}
		}
	}
	/**
	 * 
	 * @function hideButtons 
	 * 
	 * @created_date : 07-Dec-2014
	 * 
	 * @description
	 * 
	 * 
	 * @parm(s) : @param hideButtons
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	private void hideButtons(boolean hideButtons){
		editToolBarView.cancelButton.setVisible(!hideButtons);
		editToolBarView.saveButton.setVisible(!hideButtons);
		editToolBarView.savingText.setVisible(hideButtons);
	}
	/**
	 * 
	 * @function showButtons 
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
	private void showButtons(){
		editButtonsToolBar.setVisible(false);
		editToolBarView=new EditToolBarView(true);
		editToolBarView.cancelButton.addClickHandler(new ResetEditContentEvent());
		editToolBarView.saveButton.addClickHandler(new UpdateEditedDueDateEvent());
	}
	/**
	 * 
	 * @fileName : CollectionsView.java
	 *
	 * @description : 
	 *
	 *
	 * @version : 1.0
	 *
	 * @date: 07-Dec-2014
	 *
	 * @Author Gooru Team
	 *
	 * @Reviewer:
	 */
	private class UpdateEditedContentEvent implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			if(directionTextArea!=null){
				final String directionTextAreaText=directionTextArea.getText().trim().equals(i18n.GL1389())?"":directionTextArea.getText().trim();
				if(directionTextAreaText.length()>=500){
					directionErrorLabel.setText(i18n.GL0143());
				}else{
					hideButtons(true);
					directionErrorLabel.setText("");
					Map<String, String> parms = new HashMap<String, String>();
					parms.put("text", directionTextAreaText);
					AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {
						@Override
						public void onSuccess(Boolean isFound) {
							if(isFound){
								directionErrorLabel.setText(i18n.GL0554());
								hideButtons(false);
							}else{
								directionErrorLabel.setText("");
								updateClasspageItem(directionTextAreaText,null,null);
							}
						}		
					});
				}
			}else{
				resetEditContent();
			}
		}
	}
	/**
	 * 
	 * @function updateClasspageItem 
	 * 
	 * @created_date : 07-Dec-2014
	 * 
	 * @description
	 * 
	 * 
	 * @parm(s) : @param directionText
	 * @parm(s) : @param dueDate
	 * @parm(s) : @param readStatus
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	public void updateClasspageItem(String directionText,String dueDate,String readStatus){
		getPresenter().updateClasspageItem(classpageItemDo.getCollectionItemId(), directionText,dueDate,readStatus);
	}
	@Override
	public void updateDirection(String directionText) {
		this.directionText=directionText;
		classpageItemDo.setDirection(directionText);
		resetEditContent();
	}
	@Override
	public void updateDueDate(String dueDate) {
		classpageItemDo.setPlannedEndDate(dueDate);
		setDueDate();
		resetEditContent();
		
	}
	@Override
	public void updateCollectionStatus(String readStatus) {
		classpageItemDo.setStatus(readStatus);
		setReadStatus();
		changeButtonText();
		updateAssignmentCircleColor(classpageItemDo.getCollectionItemId(),readStatus);
	}
	/**
	 * 
	 * @function editDirection 
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
	public void editDirection(){
		directionContentPanel.clear();
		directionTextArea =new TextArea();
		directionTextArea.getElement().setAttribute("maxlength", "500");
		directionTextArea.setStyleName(CollectionsCBundle.INSTANCE.css().classpageTextarea());
		if(directionText!=null&&!directionText.equals("")&&!directionText.equals("null")){
			directionTextArea.removeStyleName(AddAssignmentContainerCBundle.INSTANCE.css().assignmentsystemMessage());
			directionTextArea.setText(directionText);
		}else{
			directionTextArea.setText(i18n.GL1389());
			directionTextArea.addStyleName(AddAssignmentContainerCBundle.INSTANCE.css().assignmentsystemMessage());
		}
		directionTextArea.addFocusHandler(new DirectonFoucsEvent());
		directionTextArea.addBlurHandler(new DirectionBlurEvent());
		directionTextArea.addKeyUpHandler(new DirectionKeypressEvent());
		directionErrorLabel.setStyleName(AddAssignmentContainerCBundle.INSTANCE.css().assignmentDirectiomErrorMessage());
		directionContentPanel.clear();
		directionErrorLabel.setText("");
		directionContentPanel.add(directionTextArea);
		directionContentPanel.add(directionErrorLabel);
	}
	/**
	 * 
	 * @function resetEditContent 
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
	public void resetEditContent(){
		directionContentPanel.clear();
		setDirection(directionText);
		Widget widget=classpageItemContainer.getWidget(0);
		if(widget instanceof EditToolBarView){
			classpageItemContainer.remove(0);
		}
		editButtonsToolBar.setVisible(true);
		lblDirectionCharLimit.setVisible(false);
	}
	/**
	 * 
	 * @function setDirection 
	 * 
	 * @created_date : 07-Dec-2014
	 * 
	 * @description
	 * 
	 * 
	 * @parm(s) : @param directionText
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	private void setDirection(String directionText){
		directionContentPanel.clear();
		HTML directionContent=new HTML();
		directionContent.setStyleName("");
		if(directionText==null||directionText.equals("")||directionText.equals("null")){
			directionContent.setStyleName(CollectionsCBundle.INSTANCE.css().systemMessage());
			directionText=i18n.GL1374();
		}
		directionContent.setHTML(directionText);
		directionContentPanel.add(directionContent);
	}
	
	/**
	 * 
	 */
	public void removeClasspageItemWidget(){
		removeConfirmBox.hide();
		removeConfirmBox.hideButtons(true);
		this.removeFromParent();
		resetPagination();
		updateCollectionsView();
	}
	/**
	 * 
	 * @function hidePopup 
	 * 
	 * @created_date : 07-Dec-2014
	 * 
	 * @description
	 * 
	 * 
	 * @parm(s) : @param event
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	public void hidePopup(NativePreviewEvent event){
    	if(event.getTypeInt()==Event.ONCLICK){
    		Event nativeEvent = Event.as(event.getNativeEvent());
        	boolean target=eventTargetsPopup(nativeEvent);
        	if(!target)
        	{
        		dropdownPanel.setVisible(false);
        		dropdownPanelAnalyticsButton.setVisible(false);
        	}
    	}
     }
	/**
	 * 
	 * @function eventTargetsPopup 
	 * 
	 * @created_date : 07-Dec-2014
	 * 
	 * @description
	 * 
	 * 
	 * @parm(s) : @param event
	 * @parm(s) : @return
	 * 
	 * @return : boolean
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	private boolean eventTargetsPopup(NativeEvent event) {
		EventTarget target = event.getEventTarget();
		if (Element.is(target)) {
			return dropdownPanel.getElement().isOrHasChild(Element.as(target))||editClassItemButton.getElement().isOrHasChild(Element.as(target));
		}
		return false;
	}

	public void resetPagination(){
		
	}
	public void updateCollectionsView(){
		
	}
	
	public void updateAssignmentCircleColor(String collectionItemId,String readStatus){
		
	}
	
/*	@UiHandler("viewClassItemAnalyticsButton")
	public void clickOnAnalyticsBtn(ClickEvent clickEvent){
		 MixpanelUtil.mixpanelEvent("Classpage_With_Analytics");
		new CollectionAnalyticsUc(classpageItemDo.getCollectionId(), classpageItemDo.getCollectionTitle(),null) {
			
			@Override
			public void setDefaultTab() {
				
			}
		};
		Window.enableScrolling(false);
		AppClientFactory.fireEvent(new SetHeaderZIndexEvent(0, false));
	}*/
	/**
	 * 
	 * @function getCollectionFolders 
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
	public void getCollectionFolders(){
		AppClientFactory.getInjector().getClasspageService().getCollectionParentFolders(classpageItemDo.getCollectionId(), new SimpleAsyncCallback<ArrayList<String>>() {
			@Override
			public void onSuccess(ArrayList<String> foldersList) {
				if(foldersList!=null){
					Map<String,String> parametesMap=new HashMap<String,String>();
					parametesMap.put("id", classpageItemDo.getCollectionId());
					if(foldersList.size()>0){
						for(int i=0;i<foldersList.size();i++){
							parametesMap.put("o"+(i+1), foldersList.get(i));
						}
					}
					AppClientFactory.getPlaceManager().revealPlace(true, AppClientFactory.getPlaceManager().preparePlaceRequest(PlaceTokens.SHELF, parametesMap));
				}
			}
		});
	}
	/**
	 * 
	 * @fileName : CollectionsView.java
	 *
	 * @description : 
	 *
	 *
	 * @version : 1.0
	 *
	 * @date: 07-Dec-2014
	 *
	 * @Author Gooru Team
	 *
	 * @Reviewer:
	 */
	private class DirectonFoucsEvent implements FocusHandler{
		@Override
		public void onFocus(FocusEvent event) {
			String directionText=directionTextArea.getText().trim();
			if(directionText.equalsIgnoreCase(i18n.GL1389())){
				directionTextArea.setText("");
			}
			directionTextArea.removeStyleName(AddAssignmentContainerCBundle.INSTANCE.css().assignmentsystemMessage());
		}
	}
	/**
	 * 
	 * @fileName : CollectionsView.java
	 *
	 * @description : 
	 *
	 *
	 * @version : 1.0
	 *
	 * @date: 07-Dec-2014
	 *
	 * @Author Gooru Team
	 *
	 * @Reviewer:
	 */
	private class DirectionBlurEvent implements BlurHandler{
		@Override
		public void onBlur(BlurEvent event) {
			String directionText=directionTextArea.getText().trim();
			if(!directionText.equalsIgnoreCase(i18n.GL1389())&&directionText.length()>0){
				if(directionText.length()>=500){
					directionErrorLabel.setText(i18n.GL0143());
					
				}else{
					directionErrorLabel.setText("");
					showButtons();
				}
			}else{
				directionTextArea.setText(i18n.GL1389());
				directionTextArea.addStyleName(AddAssignmentContainerCBundle.INSTANCE.css().assignmentsystemMessage());
			}
		}
	}
	/**
	 * 
	 * @fileName : CollectionsView.java
	 *
	 * @description : 
	 *
	 *
	 * @version : 1.0
	 *
	 * @date: 07-Dec-2014
	 *
	 * @Author Gooru Team
	 *
	 * @Reviewer:
	 */
	private class DirectionKeypressEvent implements KeyUpHandler{
		@Override
		public void onKeyUp(KeyUpEvent event) {
			String directionText=directionTextArea.getText().trim();
			if(directionText.length()>=500){
				directionErrorLabel.setText(i18n.GL0143());
				//event.preventDefault();
				}else{
				directionErrorLabel.setText("");
			}
		}
	}
	
	/**
	 * Updating the Collection Data (Title, Description)
	 * by using UpdateSocialShareMetaDataHandler.
	 */
	UpdateSocialShareMetaDataHandler setHeader = new UpdateSocialShareMetaDataHandler() {
		@Override
		public void updateSocialShareMetaData(String title,
				String description1, String imageUrl) {
			classpageItemDo.setCollectionTitle(title);
			classpageItemDo.setGoal(description1);
		}
	};
	@Override
	public void setButtonVisibility(){
		if (AppClientFactory.isAnonymous()){
			changeStatusButton.setVisible(false);
		}else{
			if (AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.STUDENT)){
				changeStatusButton.setVisible(true);
			}
		}
	}
	@Override
	public void setViewCollectionAnalytics(boolean isaggregateData) {
		if(isaggregateData){
			if(dropdownPanelAnalyticsButton.isVisible()){
				dropdownPanelAnalyticsButton.setVisible(false);
			}else{
				dropdownPanelAnalyticsButton.setVisible(true);
			}
		}else{
			ToolTip toolTip=new ToolTip(i18n.GL3098(),"");
			toolTip.getTootltipContent().getElement().setAttribute("style", "width: 184px;");
			toolTip.getElement().getStyle().setBackgroundColor("transparent");
			toolTip.getElement().getStyle().setPosition(Position.ABSOLUTE);
			toolTip.setPopupPosition(viewClassItemAnalyticsButton.getAbsoluteLeft(), viewClassItemAnalyticsButton.getAbsoluteTop()+36);
			toolTip.setAutoHideEnabled(true);
			toolTip.show();
		}
	}
}
