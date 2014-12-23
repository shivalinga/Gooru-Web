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
package org.ednovo.gooru.client.mvp.play.collection.end;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.effects.FadeInAndOut;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BaseViewWithHandlers;
import org.ednovo.gooru.client.mvp.analytics.util.AnalyticsUtil;
import org.ednovo.gooru.client.mvp.home.LoginPopupUc;
import org.ednovo.gooru.client.mvp.home.library.assign.AssignPopupVc;
import org.ednovo.gooru.client.mvp.play.collection.body.CollectionPlayerMetadataPresenter;
import org.ednovo.gooru.client.mvp.play.collection.body.CollectionPlayerStyleBundle;
import org.ednovo.gooru.client.mvp.play.collection.preview.PreviewPlayerPresenter;
import org.ednovo.gooru.client.mvp.play.collection.preview.home.customize.RenameCustomizePopUp;
import org.ednovo.gooru.client.mvp.play.collection.preview.metadata.comment.CommentWidgetChildView;
import org.ednovo.gooru.client.mvp.play.resource.body.ResourcePlayerMetadataView;
import org.ednovo.gooru.client.mvp.search.SearchResultWrapperCBundle;
import org.ednovo.gooru.client.mvp.shelf.ShelfPresenter;
import org.ednovo.gooru.client.service.ResourceServiceAsync;
import org.ednovo.gooru.client.uc.PlayerBundle;
import org.ednovo.gooru.client.uc.tooltip.GlobalToolTip;
import org.ednovo.gooru.client.uc.tooltip.ToolTip;
import org.ednovo.gooru.client.util.PlayerDataLogEvents;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.analytics.CollectionSummaryMetaDataDo;
import org.ednovo.gooru.shared.model.analytics.CollectionSummaryUsersDataDo;
import org.ednovo.gooru.shared.model.analytics.PrintUserDataDO;
import org.ednovo.gooru.shared.model.content.ClasspageItemDo;
import org.ednovo.gooru.shared.model.content.CollectionDo;
import org.ednovo.gooru.shared.model.content.StandardFo;
import org.ednovo.gooru.shared.model.library.ConceptDo;
import org.ednovo.gooru.shared.model.player.CommentsDo;
import org.ednovo.gooru.shared.model.player.CommentsListDo;
import org.ednovo.gooru.shared.util.StringUtil;
import org.ednovo.gooru.shared.util.UAgentInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
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
import com.google.gwt.user.client.Window.Navigator;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

public class CollectionEndView extends BaseViewWithHandlers<CollectionEndUiHandlers> implements IsCollectionEndView{
	@UiField
	static FlowPanel studyMainContianer;
	@UiField
	FlowPanel metadataContainer;
	@UiField
	FlowPanel messageContainer,thumbnailContainer,spendTimeContainer,scoreContainer,nextCollectionContainer;
	@UiField
	FlowPanel frameContainer1,dataInsightsPanel;
	@UiField VerticalPanel commentsContainer,pnlSummary;
	@UiField Label commentCount,seeMoreButton,noCommentsLbl,toCommentText,orText,loginMessagingText,characterLimit,successPostMsg,replayCollection,whatNextCollectionTitle,
					resourceCount,questionCount,avgReactionImage,insightsHeaderText,insightsContentText,lblCharLimitComments,headingText;
	@UiField HTMLPanel collectionSummaryText,loadingImageLabel,addComment,loginMessaging,commentssection,switchContainer;
	@UiField TextArea commentField;
	@UiField Button postCommentBtn,postCommentCancel;
	@UiField Anchor loginUrl, signupUrl;
	@UiField CollectionPlayerStyleBundle playerStyle;
	@UiField Image userPhoto,collectionThumbnail,nextCollectionThumbnail;
	@UiField Button customizeCollectionBtn,shareCollectionBtn;
	
	@UiField InlineLabel requiredLabel,optionalLabel;
	
	@UiField SimpleCheckBox changeAssignmentStatusButton;
	
	@UiField ListBox sessionsDropDown;
	@UiField Image collectionImage;
	@UiField InlineLabel collectionTitle,collectionResourcesCount,collectionLastAccessed,lastModifiedTime;
	ToolTip toolTip;
	Map<String, Long> sessionData=new HashMap<String, Long>();
	PrintUserDataDO printData=new PrintUserDataDO();
	
	@Inject
	private ResourceServiceAsync resourceService;
	
	/*@UiField Frame insightsFrame;*/
	private String languageObjectiveValue;
	private CollectionDo collectionDo=null;
	
	public static final String STANDARD_CODE = "code";
	
	public static final String STANDARD_DESCRIPTION = "description";
	
	private static final String COLLECTION_COMMENTS="COLLECTION_COMMENTS";
	
	private static final String INITIAL_COMMENT_LIMIT = "10";
	
	private static final String CREATE = "CREATE";
	
	private static final String DELETE = "DELETE";
	
	private static final String EDIT = "EDIT";
	
	private static final String PAGINATION = "page";
	
//	private static final String COMMENTS_LBL = " "+i18n.GL1432;
	
	private static final String PRIMARY_STYLE = "primary";
	
	private static final String SECONDARY_STYLE = "secondary";
	
	private static final String DISABLED_STYLE = "disabled";
	
	private static final int INCREMENT_BY_ONE = 1;
	
	private static final int DECREMENT_BY_ONE = -1;

	private static final String EDUCATOR_DEFAULT_IMG = "../images/settings/setting-user-image.png";
	private Anchor usernameAnchor;
	
	private int totalCommentCount = 0;
	
	private int totalHitCount = 0;
	
	private int paginationCount = 0;
	
	private boolean isHavingBadWords;
	
	private boolean isCustomizePopup = false;
	
	private boolean isSharePopup = false;
	
	private PopupPanel toolTipPopupPanel=new PopupPanel();
	
	private static CollectionPlayerMetadataViewUiBinder uiBinder = GWT.create(CollectionPlayerMetadataViewUiBinder.class);

	interface CollectionPlayerMetadataViewUiBinder extends UiBinder<Widget, CollectionEndView> {
	}
	
	private MessageProperties i18n = GWT.create(MessageProperties.class);
	
	@Inject
	public CollectionEndView(){
		setWidget(uiBinder.createAndBindUi(this));
		setLabelAndIds();
		//teacherContainer.setVisible(false);
		messageContainer.setVisible(false);
		PlayerBundle.INSTANCE.getPlayerStyle().ensureInjected();
		SearchResultWrapperCBundle.INSTANCE.css().ensureInjected();
		loginMessagingText.setText(i18n.GL0568());
		loginMessagingText.getElement().setId("lblLoginMessagingText");
		loginMessagingText.getElement().setAttribute("alt",i18n.GL0568());
		loginMessagingText.getElement().setAttribute("title",i18n.GL0568());
		
		orText.setText(i18n.GL0209());
		orText.getElement().setId("lblOrText");
		orText.getElement().setAttribute("alt",i18n.GL0209());
		orText.getElement().setAttribute("title",i18n.GL0209());
		
		toCommentText.setText(" "+i18n.GL0569());
		toCommentText.getElement().setId("lblToCommentText");
		toCommentText.getElement().setAttribute("alt",i18n.GL0569());
		toCommentText.getElement().setAttribute("title",i18n.GL0569());
		
		customizeCollectionBtn.setText(i18n.GL2037());
		customizeCollectionBtn.getElement().setId("btnCustomizeCollectionEndBtn");
		customizeCollectionBtn.getElement().setAttribute("alt",i18n.GL2037());
		customizeCollectionBtn.getElement().setAttribute("title",i18n.GL2037());
		
		shareCollectionBtn.setText(i18n.GL0536());
		shareCollectionBtn.getElement().setId("btnShareCollectionEndBtn");
		shareCollectionBtn.getElement().setAttribute("alt",i18n.GL0536());
		shareCollectionBtn.getElement().setAttribute("title",i18n.GL0536());
		
		loginMessagingText.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().toCommentTextPreviewPlayer());
		orText.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().toCommentTextPreviewPlayer());
		toCommentText.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().toCommentTextPreviewPlayer());
		loginUrl.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().toCommentTextPreviewPlayer());
		signupUrl.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().toCommentTextPreviewPlayer());
		
		commentField.addClickHandler(new OnCommentsFieldClicked());
		commentField.addKeyUpHandler(new ValidateConfirmText());
		commentField.addBlurHandler(new OnCommentsFieldBlur());
		seeMoreButton.setVisible(false);
		
		String value = StringUtil.generateMessage(i18n.GL2103(), "500");
		lblCharLimitComments.setText(value);
		StringUtil.setAttributes(lblCharLimitComments.getElement(), "lblCharLimitComments", value, value);
		
		customizeCollectionBtn.addMouseOverHandler(new OncustomizeCollectionBtnMouseOver());
		customizeCollectionBtn.addMouseOutHandler(new OncustomizeCollectionBtnMouseOut());
		customizeCollectionBtn.addBlurHandler(new customizeCollectionBtnOnBlur());
		shareCollectionBtn.addMouseOverHandler(new OnshareCollectionBtnMouseOver());
		shareCollectionBtn.addMouseOutHandler(new OnshareCollectionBtnMouseOut());
		  Boolean isIpad = !!Navigator.getUserAgent().matches("(.*)iPad(.*)");
		  Boolean isAndriod = !!Navigator.getUserAgent().matches("(.*)Android(.*)");
		  Boolean isWinDskp = !!Navigator.getUserAgent().matches("(.*)NT(.*)");
		  
		  UAgentInfo detector = new UAgentInfo(Navigator.getUserAgent());
		  
		  if(isIpad && !StringUtil.IPAD_MESSAGE_Close_Click)
		  {
			  studyMainContianer.getElement().setAttribute("style", "margin-top:0px;");
			 
		  }
		  else if(isAndriod && !StringUtil.IPAD_MESSAGE_Close_Click)
		  {
			  studyMainContianer.getElement().setAttribute("style", "margin-top:0px;");
		  }
		  else
		  {
			  //studyMainContianer.getElement().setAttribute("style", "margin-top:50px;");
			  
		  }
			sessionsDropDown.addChangeHandler(new StudentsSessionsChangeHandler());
	}
	 public class StudentsSessionsChangeHandler implements ChangeHandler{
			@Override
			public void onChange(ChangeEvent event) {
					int selectedIndex=sessionsDropDown.getSelectedIndex();
					String classpageId=AppClientFactory.getPlaceManager().getRequestParameter("classpageid", null);
					if(classpageId==null){
						classpageId="";
					}
	                setSessionStartTime(selectedIndex);
					getUiHandlers().setCollectionSummaryData(collectionDo.getGooruOid(), classpageId,AppClientFactory.getLoggedInUser().getGooruUId(),sessionsDropDown.getValue(selectedIndex),printData);
			}
	 }

	public class OncustomizeCollectionBtnMouseOver implements MouseOverHandler{

		@Override
		public void onMouseOver(MouseOverEvent event) {
			toolTipPopupPanel.clear();
			toolTipPopupPanel.setWidget(new GlobalToolTip(i18n.GL0677()));
			toolTipPopupPanel.setStyleName("");
			toolTipPopupPanel.setPopupPosition(customizeCollectionBtn.getElement().getAbsoluteLeft()+18, customizeCollectionBtn.getElement().getAbsoluteTop()+10);
			toolTipPopupPanel.getElement().getStyle().setZIndex(999999);
			toolTipPopupPanel.show();
		}
		
	}
	
	public class OncustomizeCollectionBtnMouseOut implements MouseOutHandler{

		@Override
		public void onMouseOut(MouseOutEvent event) {
			toolTipPopupPanel.hide();
		}

		
		
	}
	public class customizeCollectionBtnOnBlur implements BlurHandler
	{

		@Override
		public void onBlur(BlurEvent event) {
			toolTipPopupPanel.hide();
			
		}
		
	}
	public class OnshareCollectionBtnMouseOver implements MouseOverHandler{

		@Override
		public void onMouseOver(MouseOverEvent event) {
			toolTipPopupPanel.clear();
			toolTipPopupPanel.setWidget(new GlobalToolTip(i18n.GL0678()));
			toolTipPopupPanel.setStyleName("");
			toolTipPopupPanel.setPopupPosition(shareCollectionBtn.getElement().getAbsoluteLeft()+5, shareCollectionBtn.getElement().getAbsoluteTop()+10);
			toolTipPopupPanel.getElement().getStyle().setZIndex(999999);
			toolTipPopupPanel.show();
		}
		
	}
	
	public class OnshareCollectionBtnMouseOut implements MouseOutHandler{

		@Override
		public void onMouseOut(MouseOutEvent event) {
			toolTipPopupPanel.hide();
		}

		
		
	}
	
	@Override
	public void setCollectionMetadata(CollectionDo collectionDo) {
		this.collectionDo = collectionDo;
		//showPopupAfterGmailSignin();
		setCollectionImage(collectionDo.getThumbnails().getUrl());
		String message=(collectionDo.getCollectionType()!=null&&collectionDo.getCollectionType().equals(ShelfPresenter.ASSESSMENT))?i18n.GL3044():i18n.GL2083();
		headingText.setText(message);
//		setCollectionGoal(collectionDo.getGoals());
//		assignCollectionBtn.getElement().setAttribute("collectionId", collectionDo.getGooruOid());
		customizeCollectionBtn.getElement().setAttribute("collectionId", collectionDo.getGooruOid());
		shareCollectionBtn.getElement().setAttribute("collectionId", collectionDo.getGooruOid());
		setReplyLink();
		//teamContainer.clear();
		//getUiHandlers().getFlagedReport(collectionDo.getGooruOid());
//		if (collectionDo.getMeta() !=null && collectionDo.getMeta().getCollaboratorCount()>0){
//			 CollaboratorsUc collaboratorsUc=new CollaboratorsUc(collectionDo);
//			 //teamContainer.add(collaboratorsUc);
//			setUserName(collectionDo.getUser().getUsernameDisplay() +" " + i18n.GL_GRR_AND());
//		}else{
//			setUserName(collectionDo.getUser().getUsernameDisplay());
//		}
		
		if (collectionDo.getMeta() !=null)
		{
			if(collectionDo.getMeta().getPermissions() != null)
			{
			if (collectionDo.getMeta().getPermissions().toString().contains("edit") || collectionDo.getMeta().isIsCollaborator()){
				switchContainer.setVisible(true);
				if(collectionDo.getSettings() != null)
				{
					if(collectionDo.getSettings().getComment() != null)
					{
						if(collectionDo.getSettings().getComment().equalsIgnoreCase("turn-on"))
						{
							commentField.setEnabled(true);
							commentssection.getElement().getStyle().setOpacity(1);
							changeAssignmentStatusButton.setChecked(true);
						}
						else
						{
							commentField.setEnabled(false);
							postCommentBtn.setEnabled(false);
							postCommentBtn.removeStyleName(PRIMARY_STYLE);
							postCommentBtn.addStyleName(SECONDARY_STYLE);
							postCommentBtn.addStyleName(DISABLED_STYLE);
							commentssection.getElement().getStyle().setOpacity(0.5);
							changeAssignmentStatusButton.setChecked(false);
						}
					}
					else
					{
						commentField.setEnabled(true);
						postCommentBtn.removeStyleName(SECONDARY_STYLE);
						postCommentBtn.removeStyleName(DISABLED_STYLE);
						postCommentBtn.addStyleName(PRIMARY_STYLE);
						commentssection.getElement().getStyle().setOpacity(1);
						changeAssignmentStatusButton.setChecked(true);
					}
				}
				else
				{
					commentField.setEnabled(true);
					commentssection.getElement().getStyle().setOpacity(1);
					changeAssignmentStatusButton.setChecked(true);
				}
				
				
				
			}
			else
			{				
				if(collectionDo.getSettings() != null)
				{
					if(collectionDo.getSettings().getComment() != null)
					{
						if(collectionDo.getSettings().getComment().equalsIgnoreCase("turn-off"))
						{
							commentssection.setVisible(false);
						}
						else
						{
							commentssection.setVisible(true);
						}
						
					}
					
				}
				else
				{
					commentssection.setVisible(true);
				}			
				
				switchContainer.setVisible(false);	
			}
			}
			else
			{
				switchContainer.setVisible(false);	
			}
		}
		else
		{
			switchContainer.setVisible(false);	
		}
		
		
		setViewCount(collectionDo.getViews());
		getAverageReaction();
		//renderStandards(standardsContainer,getStandardsMap(this.collectionDo.getMetaInfo().getStandards()));
		//renderLanguageObjective(collectionDo.getLanguageObjective());
		
	}
	

	public List<Map<String,String>> getStandardsMap(List<StandardFo> standareds){
		List<Map<String,String>> standardsList=new ArrayList<Map<String,String>>();
		for(int i=0;i<standareds.size();i++){
			Map<String, String> standardMap=new HashMap<String, String>();
			standardMap.put(STANDARD_CODE, standareds.get(i).getCode());
			standardMap.put(STANDARD_DESCRIPTION, standareds.get(i).getDescription());
			standardsList.add(standardMap);
		}
		return standardsList;
	}
	
	@Override
    public void setInSlot(Object slot, Widget content) {
		if(slot==CollectionPlayerMetadataPresenter.METADATA_PRESENTER_SLOT){
			metadataContainer.clear();
			if(content!=null){
				metadataContainer.add(content);
			}
		}else if(slot==CollectionEndPresenter.COLLECTION_REPORTS_SLOT){
			pnlSummary.clear();
			if(content!=null){
				pnlSummary.add(content);
			}
		}
	}
	

	public void setLabelAndIds() {
//		collectionSummaryLbl.setVisible(false);
		/*lblAuthor.setText(i18n.GL0573());
		lblAuthor.getElement().setId("lblAuthor");
		lblAuthor.getElement().setAttribute("alt",i18n.GL0573());
		lblAuthor.getElement().setAttribute("title",i18n.GL0573());
		  
		lblCourse.setText(i18n.GL0574());
		lblCourse.getElement().setId("lblCourse");
		lblCourse.getElement().setAttribute("alt",i18n.GL0574());
		lblCourse.getElement().setAttribute("title",i18n.GL0574());
		
		lblStandards.setText(i18n.GL0575());
		lblStandards.getElement().setId("lblStandards");
		lblStandards.getElement().setAttribute("alt",i18n.GL0575());
		lblStandards.getElement().setAttribute("title",i18n.GL0575());
		
		previewFlagButton.setText(i18n.GL0556());
		previewFlagButton.getElement().setId("lnkPreviewFlagButton");
		previewFlagButton.getElement().setAttribute("alt",i18n.GL0556());
		previewFlagButton.getElement().setAttribute("title",i18n.GL0556());*/
		
//		collectionSummaryLbl.setText(i18n.GL1544);
//		emptyMsgDescOne.setText(i18n.GL1545);
//		emptyMsgDescTwo.setText(i18n.GL1546);
		insightsHeaderText.setText(i18n.GL1626());
		insightsHeaderText.getElement().setId("lblInsightsHeaderText");
		insightsHeaderText.getElement().setAttribute("alt",i18n.GL1626());
		insightsHeaderText.getElement().setAttribute("title",i18n.GL1626());
		
		insightsContentText.setText(i18n.GL1627());
		insightsContentText.getElement().setId("lblInsightsContentText");
		insightsContentText.getElement().setAttribute("alt",i18n.GL1627());
		insightsContentText.getElement().setAttribute("title",i18n.GL1627());
		
		/*previewFlagButton.removeStyleName(PlayerBundle.INSTANCE.getPlayerStyle().previewCoverFlagImageOrange());
		previewFlagButton.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().playerPreviewCoverFlagImage());
		lbllanguageObjectiveText.setText(i18n.GL1721());
		lbllanguageObjectiveText.getElement().setId("lblLanguageObjectiveText");
		lbllanguageObjectiveText.getElement().setAttribute("alt",i18n.GL1721());
		lbllanguageObjectiveText.getElement().setAttribute("title",i18n.GL1721());*/
		
		studyMainContianer.getElement().setId("fpnlStudyMainContianer");
		metadataContainer.getElement().setId("fpnlMetadataContainer");
		/*teacherContainer.getElement().setId("pnlTeacherContainer");
		classInfoPanel.getElement().setId("pnlClassInfoPanel");
		lblClassInfo.getElement().setId("lblClassInfo");
		lblclassTitle.getElement().setId("lblClassTitle");
		classTitleValue.getElement().setId("lblClassTitleValue");
		teacherPanel.getElement().setId("pnlTeacherPanel");
		lblTeacher.getElement().setId("lblTeacher");
		teacherProfileContainer.getElement().setId("pnlTeacherProfileContainer");
		teacherNameLabel.getElement().setId("lblTeacherNameLabel");
		dueDateSection.getElement().setId("pnlDueDateSection");
		lbldueDate.getElement().setId("lbldueDate");
		dueDate.getElement().setId("lbldueDateLabel");
		directionSection.getElement().setId("pnlDirectionSection");
		lblDirections.getElement().setId("lblDirections");
		lblDirectionsDesc.getElement().setId("lblDirectionsDesc");
		authorPanel.getElement().setId("pnlAuthorPanel");
		profileThumbnailImage.getElement().setId("imgProfileThumbnailImage");
		userNameLabel.getElement().setId("lblUserNameLabel");
		teamContainer.getElement().setId("fpnlTeamContainer");
		courseSection.getElement().setId("pnlCourseSection");
		courseTitle.getElement().setId("fpnlCourseTitle");
		standardSection.getElement().setId("pnlStandardSection");
		standardsContainer.getElement().setId("fpnlStandardsContainer");
		viewSection.getElement().setId("pnlViewSection");
		viewsCountLabel.getElement().setId("lblViewsCountLabel");
		languageObjectiveContainer.getElement().setId("pnlLanguageObjectiveContainer");
		lbllanguageObjective.getElement().setId("lbllanguageObjective");
		seeMoreAnchor.getElement().setId("lnkSeeMoreAnchor");*/
		commentCount.getElement().setId("lblCommentCount");
		commentsContainer.getElement().setId("vpnlCommentsContainer");
		seeMoreButton.setText(i18n.GL0508());
		seeMoreButton.getElement().setId("lblSeeMoreButton");
		seeMoreButton.getElement().setAttribute("alt",i18n.GL0508());
		seeMoreButton.getElement().setAttribute("title",i18n.GL0508());
		seeMoreButton.getElement().setId("lblSeeMoreButton");
		noCommentsLbl.getElement().setId("lblNoCommentsLbl");
		addComment.getElement().setId("pnlAddComment");
		loginUrl.setText(i18n.GL0187().toLowerCase());
		loginUrl.getElement().setId("lnkLoginUrl");
		loginUrl.getElement().setAttribute("alt",i18n.GL0187().toLowerCase());
		loginUrl.getElement().setAttribute("title",i18n.GL0187().toLowerCase());
		
		signupUrl.setText(i18n.GL0186().toLowerCase());
		signupUrl.getElement().setId("lnkSignupUrl");
		signupUrl.getElement().setAttribute("alt",i18n.GL0186().toLowerCase());
		signupUrl.getElement().setAttribute("title",i18n.GL0186().toLowerCase());
		
		successPostMsg.setText(i18n.GL0570());
		successPostMsg.getElement().setId("lblSuccessPostMsg");
		successPostMsg.getElement().setAttribute("alt",i18n.GL0570());
		successPostMsg.getElement().setAttribute("title",i18n.GL0570());
		
		postCommentBtn.setText(i18n.GL0571());
		postCommentBtn.getElement().setId("btnPostCommentBtn");
		postCommentBtn.getElement().setAttribute("alt",i18n.GL0571());
		postCommentBtn.getElement().setAttribute("title",i18n.GL0571());
		
		postCommentCancel.setText(i18n.GL0142());
		postCommentCancel.getElement().setId("btnPostCommentCancel");
		postCommentCancel.getElement().setAttribute("alt",i18n.GL0142());
		postCommentCancel.getElement().setAttribute("title",i18n.GL0142());
		
		characterLimit.setText(i18n.GL0143());
		characterLimit.getElement().setId("lblCharacterLimit");
		characterLimit.getElement().setAttribute("alt",i18n.GL0143());
		characterLimit.getElement().setAttribute("title",i18n.GL0143());
		postCommentBtn.setEnabled(false);
		userPhoto.getElement().setId("imgUserPhoto");
		commentField.getElement().setId("tatCommentField");
		StringUtil.setAttributes(commentField, true);
		dataInsightsPanel.getElement().setId("pnlDataInsightsPanel");
		frameContainer1.getElement().setId("fpnlframeContainer1");
		messageContainer.getElement().setId("fpnlMessageContainer");
		
		replayCollection.setText(i18n.GL0632());
		replayCollection.getElement().setId("lblReplayCollection");
		replayCollection.getElement().setAttribute("alt",i18n.GL0632());
		replayCollection.getElement().setAttribute("title",i18n.GL0632());
		
		
		collectionSummaryText.getElement().setInnerText(i18n.GL1587());
		StringUtil.setAttributes(collectionSummaryText.getElement(), "pnlCollectionSummaryText", i18n.GL1587(), i18n.GL1587());
		
		collectionThumbnail.getElement().setId("imgCollectionThumbnail");
		thumbnailContainer.getElement().setId("fpnlThumbnailContainer");
	}
	
	@UiHandler("collectionThumbnail")
	public void thumbnailErrorImage(ErrorEvent event){
		collectionThumbnail.setUrl("images/default-collection-image-160x120.png");
	}
	
	@UiHandler("nextCollectionThumbnail")
	public void nextThumbnailErrorImage(ErrorEvent event){
		nextCollectionThumbnail.setUrl("images/default-collection-image-160x120.png");
	}
	

	public void setReplyLink(){
		Anchor resourceAnchor=new Anchor();
		resourceAnchor.setHref(getReplayLink());
		resourceAnchor.setStyleName("");
		HTMLPanel collectionHTMLPanel = new HTMLPanel("");
		resourceAnchor.addClickHandler(new ReplayCollectionEvent());
		collectionHTMLPanel.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().collectionreplayContainer());
		Label collectionReplayButton=new Label(i18n.GL0632());
		collectionReplayButton.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().collectionreplayText());
		collectionHTMLPanel.add(collectionReplayButton);
		resourceAnchor.getElement().appendChild(collectionHTMLPanel.getElement());
		thumbnailContainer.clear();
		thumbnailContainer.add(resourceAnchor);
	}
	
	private class ReplayCollectionEvent implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			getUiHandlers().resetCollectionActivityEventId();
		}
	}
	public String getReplayLink(){
		String collectionId=AppClientFactory.getPlaceManager().getRequestParameter("id", null);
		String viewToken=AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken();
		String resourceLink="#"+viewToken+"&id="+collectionId;
		resourceLink += PreviewPlayerPresenter.setConceptPlayerParameters();
		return resourceLink;
	}
	public void setCollectionImage(String thumbnailUrl){
		collectionThumbnail.setUrl(thumbnailUrl);
	}
	/**
	 * 
	 * @function oncustomizeCollectionBtnClicked 
	 * 
	 * @created_date : 11-Dec-2013
	 * 
	 * @description
	 * 
	 * 
	 * @parm(s) : @param clickEvent
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	@UiHandler("customizeCollectionBtn")
	public void oncustomizeCollectionBtnClicked(ClickEvent clickEvent) {
		
		String collectionId = clickEvent.getRelativeElement().getAttribute("collectionId");

				if(!isCustomizePopup){
					isCustomizePopup=true;
				Boolean loginFlag = false;
				if (AppClientFactory.isAnonymous()){
					loginFlag = true;
				}
				else
				{
					loginFlag = false;
				}
				RenameCustomizePopUp successPopupVc = new RenameCustomizePopUp(collectionId, loginFlag,collectionDo.getTitle()) {

					@Override
					public void closePoup() {
//						Window.enableScrolling(true);
						this.hide();	
						isCustomizePopup = false;
					}
				};
				Window.scrollTo(0, 0);
				successPopupVc.setWidth("500px");
				successPopupVc.setHeight("471px");
				successPopupVc.show();
				successPopupVc.center();
				
				Map<String,String> params = new HashMap<String,String>();
				params.put("id", AppClientFactory.getPlaceManager().getRequestParameter("id"));
				
				if(AppClientFactory.getPlaceManager().getRequestParameter("subject")!=null){
					params.put("subject", AppClientFactory.getPlaceManager().getRequestParameter("subject"));
					
				}
				if(AppClientFactory.getPlaceManager().getRequestParameter("lessonId")!=null){
					params.put("lessonId", AppClientFactory.getPlaceManager().getRequestParameter("lessonId"));
					params.put("customize", "yes");
					
				}
				params.put("view", "end");
				PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(PlaceTokens.COLLECTION_PLAY, params);
				AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, true);
				
			}
		
	}
	
	/**
	 * 
	 * @function oncustomizeCollectionBtnClicked 
	 * 
	 * @created_date : 11-Dec-2013
	 * 
	 * @description
	 * 
	 * 
	 * @parm(s) : @param clickEvent
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	@UiHandler("shareCollectionBtn")
	public void onshareCollectionBtnClicked(ClickEvent clickEvent) {
		getUiHandlers().triggerCollectionShareDataEvent(null,PlayerDataLogEvents.COLLECTION,"gooru",false);
		final Map<String, String> params = StringUtil.splitQuery(Window.Location.getHref());
		String collectionId = collectionDo.getGooruOid();
		AssignPopupVc successPopupVc = new AssignPopupVc(collectionId, collectionDo.getTitle(), collectionDo.getGoals()) {
				@Override
				public void closePoup() {
					Window.enableScrolling(true);
			        this.hide();
			    	params.remove("assign");
			    	PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(AppClientFactory.getCurrentPlaceToken(), params);
					AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, true);
				}
			};
		int clientHeight=Window.getClientHeight();
		if(clientHeight>625){
			clientHeight=625;
			successPopupVc.getAssignContainer().getElement().setAttribute("style", "max-height:"+clientHeight+"px;width:500px;overflow-x:hidden;overflow-y:scroll");
		}else{
			successPopupVc.getAssignContainer().getElement().setAttribute("style", "max-height:"+clientHeight+"px;width:500px;overflow-x:hidden;overflow-y:scroll");
		}
		successPopupVc.show();
		int left = (Window.getClientWidth() - 500) >> 1;
	    int top = (Window.getClientHeight() - clientHeight) >> 1;
	    successPopupVc.setPopupPosition(Math.max(Window.getScrollLeft() + left, 0), Math.max(Window.getScrollTop() + top, 0));
		params.put("assign", "yes");
		PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(AppClientFactory.getCurrentPlaceToken(), params);
		AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, true);
	}
	
	public void resetMetadataFields(){
		commentField.setText("");
		commentField.getElement().setAttribute("alt","");
		commentField.getElement().setAttribute("title","");
		commentField.setVisible(false);
		loginMessaging.setVisible(true);
		modifyEditControls(false);
		getFlagButton().setText(i18n.GL0556());
		getFlagButton().removeStyleName(PlayerBundle.INSTANCE.getPlayerStyle().previewCoverFlagImageOrange());
		getFlagButton().setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().playerPreviewCoverFlagImage());
		this.collectionDo=null;
	}

	@Override
	public void setRelatedConceptsContent(ArrayList<ConceptDo> conceptDoList,
			String coursePage, String subject, String lessonId,
			String libraryName) {
		
	}

	@Override
	public void isConceptsContainerVisible(boolean isVisible) {
		
	}


	public void setDataInsightsUrl(){
		String page=AppClientFactory.getPlaceManager().getRequestParameter("page", null);
		if(AppClientFactory.isAnonymous()){
			frameContainer1.clear();
			frameContainer1.setVisible(false);
			messageContainer.setVisible(true);
//		}else if(page!=null&&page.equals("teach")){
//			frameContainer1.clear();
//			frameContainer1.setVisible(false);
//			messageContainer.setVisible(false);
		}else{
			frameContainer1.clear();
			frameContainer1.setVisible(true);
			messageContainer.setVisible(false);
/*			frameContainer1.add(new DataInsightsIframe(StringUtil.generateMessage(AppClientFactory.getLoggedInUser().getSettings().getAnalyticsEndPoint()+DataInsightsUrlTokens.STUDYPLAYER_SUMMARY_DATA,
					collectionDo.getGooruOid(),AppClientFactory.getGooruUid(),"",AppClientFactory.getLoginSessionToken())));*/
			getUiHandlers().setCollectionSummaryBasedOnClasspageIdSessionId();
		}
	}
	
	public void setClasspageInsightsUrl(String classpageId, String sessionId){
		if(sessionId==null) {
			sessionId = "";
		}
		String page=AppClientFactory.getPlaceManager().getRequestParameter("page", null);
		if(AppClientFactory.isAnonymous()){
			frameContainer1.clear();
			frameContainer1.setVisible(false);
			messageContainer.setVisible(true);
//		}else if(page!=null&&page.equals("teach")){
//			frameContainer1.clear();
//			frameContainer1.setVisible(false);
//			messageContainer.setVisible(false);
		}else{
			frameContainer1.clear();
			frameContainer1.setVisible(true);
			messageContainer.setVisible(false);
			getUiHandlers().setCollectionSummaryBasedOnClasspageIdSessionId();
			/*frameContainer1.add(new DataInsightsIframe(StringUtil.generateMessage(AppClientFactory.getLoggedInUser().getSettings().getAnalyticsEndPoint()+DataInsightsUrlTokens.PLAYER_CLASS_PREVIOUS_DATA,
					classpageId,collectionDo.getGooruOid(),AppClientFactory.getGooruUid(),sessionId,AppClientFactory.getLoginSessionToken())));*/
		}
	}
	
	public void setDataInsightsSummaryUrl(String sessionId){
		String page=AppClientFactory.getPlaceManager().getRequestParameter("page", null);
		if(AppClientFactory.isAnonymous()){
			frameContainer1.clear();
			frameContainer1.setVisible(false);
			messageContainer.setVisible(true);
//		}else if(page!=null&&page.equals("teach")){
//			frameContainer1.clear();
//			frameContainer1.setVisible(false);
//			messageContainer.setVisible(false);
		}else{
			frameContainer1.clear();
			frameContainer1.setVisible(true);
			messageContainer.setVisible(false);
			sessionId=sessionId!=null?sessionId:"";
			getUiHandlers().setCollectionSummaryBasedOnClasspageIdSessionId();
			/*frameContainer1.add(new DataInsightsIframe(StringUtil.generateMessage(AppClientFactory.getLoggedInUser().getSettings().getAnalyticsEndPoint()+DataInsightsUrlTokens.STUDYPLAYER_SUMMARY_DATA,
					collectionDo.getGooruOid(),AppClientFactory.getGooruUid(),sessionId,AppClientFactory.getLoginSessionToken())));*/
		}
	}
	
	public class DataInsightsIframe extends Composite{
		private Frame dataInsightsFrame=null;
		private String insightsUrl="";
		public DataInsightsIframe(String insightsUrl){
			initWidget(dataInsightsFrame=new Frame());
			this.insightsUrl=insightsUrl;
			dataInsightsFrame.setStyleName(playerStyle.insightsFrameContent());
			dataInsightsFrame.getElement().setAttribute("id", "student-dashboard");
		}
		public void onLoad(){
			dataInsightsFrame.setUrl(insightsUrl);
		}
	}
	
	public void clearDashBoardIframe(){
		//insightsFrame.setUrl("");
	}
	

	
	/**
	 * @function setCommentsText 
	 * 
	 * @created_date : 03-Jan-2014
	 * 
	 * @description
	 * 
	 * @parm(s) : @param commentIncrement
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	private void setCommentsText(int commentIncrement) {
		totalCommentCount+=commentIncrement;
		commentCount.setText(totalCommentCount+" "+i18n.GL1432());
		commentCount.getElement().setAttribute("alt",totalCommentCount+" "+i18n.GL1432());
		commentCount.getElement().setAttribute("title",totalCommentCount+" "+i18n.GL1432());
	}
	
	/**
	 * @function setCommentsWidget 
	 * 
	 * @created_date : 02-Jan-2014
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
	public void setCommentsWidget(CommentsDo commentsDo, String action) {
		if(action.equalsIgnoreCase(CREATE)){
			setCommentsText(INCREMENT_BY_ONE);
		}
		commentsContainer.add(new CommentWidgetChildView(commentsDo,collectionDo));
		showSeeMoreButton();
	}
	
	/**
	 * @function deleteComment 
	 * 
	 * @created_date : 03-Jan-2014
	 * 
	 * @description
	 * 
	 * @parm(s) : @param commentUid
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	private void deleteComment(final String commentUid) {
		Iterator<Widget> widgets = commentsContainer.iterator();
		while (widgets.hasNext()) {
			Widget widget = widgets.next();
			if (widget instanceof CommentWidgetChildView && ((CommentWidgetChildView) widget).getCommentUid().equals(commentUid)) {
				CommentWidgetChildView commentWidgetChildView = ((CommentWidgetChildView) widget);
				final String commentText=commentWidgetChildView.getCommentField().getText();
				int index = commentsContainer.getWidgetIndex(commentWidgetChildView);
				commentsContainer.remove(index);
				final HTMLPanel deletePanel = new HTMLPanel(i18n.GL0555());
				deletePanel.setStyleName(playerStyle.deleteMsg());
				commentsContainer.insert(deletePanel, index);
				new FadeInAndOut(deletePanel.getElement(), 1000);
				Timer timer = new Timer()
		        {
		            @Override
		            public void run()
		            {
						int deleteIndex = commentsContainer.getWidgetIndex(deletePanel);
						commentsContainer.remove(deleteIndex);
						getUiHandlers().deleteCommentFromCollection(collectionDo.getGooruOid(),commentUid, commentsContainer.getWidgetCount()+"", 1+"",commentText);
		            }
		        };
		        timer.schedule(1000);
		        totalHitCount+=DECREMENT_BY_ONE;
		        setCommentsText(DECREMENT_BY_ONE);
			}
		}
	}
	
	/**
	 * @function editComment 
	 * 
	 * @created_date : 04-Jan-2014
	 * 
	 * @description
	 * 
	 * @parm(s) : @param commentUid
	 * 
	 * @return : void
	 *
	 */
	private void editComment(String commentUid) {
		Iterator<Widget> widgets = commentsContainer.iterator();
		while (widgets.hasNext()) {
			Widget widget = widgets.next();
			if (widget instanceof CommentWidgetChildView) {
				CommentWidgetChildView commentWidgetChildView = ((CommentWidgetChildView) widget);
				if(!commentWidgetChildView.getCommentUid().equals(commentUid)&&commentWidgetChildView.getCommentField().isVisible()) {
					commentWidgetChildView.enableEditFunction(false);
				}
			}
		}
	}
	

	
	public void clearCommentContainer(boolean isClear) {
		if(isClear) {
			commentsContainer.clear();
			totalCommentCount = 0;
			totalHitCount = 0;
			paginationCount = 0;
		}
	}
	/**
	 * @function showSeeMoreButton 
	 * 
	 * @created_date : 06-Jan-2014
	 * 
	 * @description
	 * 
	 * @parm(s) : 
	 * 
	 * @return : void
	 */
	public void showSeeMoreButton() {
		if(totalHitCount>Integer.parseInt(INITIAL_COMMENT_LIMIT) && (commentsContainer.getWidgetCount()<totalHitCount)) {
			seeMoreButton.setVisible(true);
		} else {
			seeMoreButton.setVisible(false);
		}
	}
	
	/**
	 * @function clickOnSeeMoreButton 
	 * 
	 * @created_date : 06-Jan-2014
	 * 
	 * @description
	 * 
	 * @parm(s) : 
	 * 
	 * @return : void
	 */
	@UiHandler("seeMoreButton")
	public void clickOnSeeMoreButton(ClickEvent event) {
		getUiHandlers().getPaginationResults(collectionDo.getGooruOid(), paginationCount+"", INITIAL_COMMENT_LIMIT);
	}
	/**
	 * @function setCommentsData 
	 * 
	 * @created_date : 02-Jan-2014
	 * 
	 * @description
	 * 
	 * @parm(s) : 
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>m
	*/
	@Override
	public void setCommentsData(CommentsListDo commentDoList,CollectionDo collectionDo, boolean isToClearCommentContainer) {
		clearCommentContainer(isToClearCommentContainer);
		if(totalHitCount == 0) {
			totalHitCount = commentDoList.getTotalHitCount();
			setCommentsText(commentDoList.getTotalHitCount());
		}
		if(commentDoList.getSearchResults() != null){
			int size = commentDoList.getSearchResults().size();
			paginationCount=paginationCount+size;
			if(size>0) {
				
				for(int i=0;i<size;i++) {
					setCommentsWidget(commentDoList.getSearchResults().get(i),PAGINATION);
				}
			}else {
				totalCommentCount=0;
				setCommentsText(totalCommentCount);
				showSeeMoreButton();
			}
		}
		if(totalHitCount==0 && paginationCount==0) {
			noCommentsLbl.setVisible(true);
		} else {
			noCommentsLbl.setVisible(false);
		}
	}
	
	/**
	 * @function updateCommentChildView 
	 * 
	 * @created_date : 03-Jan-2014
	 * 
	 * @description
	 * 
	 * @parm(s) : @param commentUid, @param action
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */	
	@Override
	public void updateCommentChildView(String commentUid, String action) {
		if(!commentUid.isEmpty() && action.equals(DELETE)) {
			deleteComment(commentUid);
			addComment.setVisible(true);
		} else if (!commentUid.isEmpty() && action.equals(EDIT)) {
			addComment.setVisible(false);
			editComment(commentUid);
		} else if(commentUid.isEmpty() && action.equals(EDIT)) {
			addComment.setVisible(true);
		}
	}
	

	/**
	 * @function clickOnLoginUrl 
	 * 
	 * @created_date : 03-Jan-2014
	 * 
	 * @description
	 * 
	 * @parm(s) : @param event
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	@UiHandler("loginUrl")
	public void clickOnLoginUrl(ClickEvent event) {
		LoginPopupUc popup = new LoginPopupUc();
		popup.setWidgetMode(COLLECTION_COMMENTS);
		popup.getElement().getStyle().setZIndex(100000);
		popup.setGlassEnabled(true);
		popup.center();
		popup.show();
	}
	
	/**
	 * @function clickOnSignupUrl 
	 * 
	 * @created_date : 03-Jan-2014
	 * 
	 * @description
	 * 
	 * @parm(s) : @param event
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	@UiHandler("signupUrl")
	public void clickOnSignupUrl(ClickEvent event) {
		Map<String, String> params = StringUtil.splitQuery(Window.Location.getHref());
		params.put("callback", "signup");
		params.put("type", "1");
		PlaceRequest placeRequest = new PlaceRequest(AppClientFactory.getCurrentPlaceToken()); 
		if (params != null) {
			for (String key : params.keySet()) {
				placeRequest = placeRequest.with(key, params.get(key));
			}
		}
		AppClientFactory.getPlaceManager().revealPlace(false,placeRequest,true);
	}

	/**
	 * @function setPlayerLoginStatus 
	 * 
	 * @created_date : 02-Jan-2014
	 * 
	 * @description
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	*/
	@Override
	public void setPlayerLoginStatus(boolean isLoggedIn) {
		postCommentCancel.setVisible(false);
		successPostMsg.setVisible(false);
		characterLimit.setVisible(false);
		if(isLoggedIn) {
			userPhoto.setVisible(true);
			commentField.setVisible(true);
			loginMessaging.setVisible(false);
			String commentorImage = AppClientFactory.loggedInUser.getUserUid();
			userPhoto.setUrl(AppClientFactory.loggedInUser.getSettings().getProfileImageUrl()+commentorImage+".png");
			userPhoto.addErrorHandler(new ErrorHandler() {
				@Override
				public void onError(ErrorEvent event) {
					userPhoto.setUrl(EDUCATOR_DEFAULT_IMG);
				}
			});
			postCommentBtn.setEnabled(true);
		} else {
			userPhoto.setVisible(false);
			commentField.setVisible(false);
			loginMessaging.setVisible(true);
			postCommentBtn.setEnabled(false);
		}
	}
	
	/**
	 * 
	 * @function clickOnPostCommentBtn 
	 * 
	 * @created_date : 03-Jan-2014
	 * 
	 * @description
	 * 
	 * @parm(s) : 
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	@UiHandler("postCommentBtn")
	public void clickOnPostCommentBtn(ClickEvent event) {
		
		if (commentField.getText().trim().length() > 0){
			if(postCommentBtn.getStyleName().contains(PRIMARY_STYLE)) {
				//check for bad words first.
				Map<String, String> parms = new HashMap<String, String>();
				parms.put("text", removeHtmlTags(commentField.getText()));
				postCommentBtn.setEnabled(false);
				AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {
	
					@Override
					public void onSuccess(Boolean value) {
						isHavingBadWords = value;
						postCommentBtn.setEnabled(true);
						if (value){
							commentField.getElement().getStyle().setBorderColor("orange");
							characterLimit.setText(i18n.GL0554());
							characterLimit.getElement().setAttribute("alt",i18n.GL0554());
							characterLimit.getElement().setAttribute("title",i18n.GL0554());
							characterLimit.setVisible(true);
						}else{
							commentField.getElement().getStyle().clearBackgroundColor();
							commentField.getElement().getStyle().setBorderColor("#ccc");
							characterLimit.setVisible(false);
							
							getUiHandlers().createCommentForCollection(collectionDo.getGooruOid(), removeHtmlTags(commentField.getText()));
							
							commentField.setText("");
							commentField.getElement().setAttribute("alt","");
							commentField.getElement().setAttribute("title","");
							commentField.setVisible(false);
							modifyEditControls(false);
							displaySuccessMsg(true);
						}
					}
				});
			}
		}else{
			commentField.setText("");
			commentField.getElement().setAttribute("alt","");
			commentField.getElement().setAttribute("title","");
			characterLimit.setVisible(false);
			modifyEditControls(false);
		}
	}
	
	/**
	 * 
	 * @function clickOnPostCommentCancel 
	 * 
	 * @created_date : 03-Jan-2014
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
	 */
	@UiHandler("postCommentCancel")
	public void clickOnPostCommentCancel(ClickEvent event) {
		commentField.setText("");
		commentField.getElement().getStyle().clearBackgroundColor();
		commentField.getElement().getStyle().setBorderColor("#ccc");
		characterLimit.setVisible(false);
		modifyEditControls(false);
	}
	
	/**
	 * 
	 * @fileName : PreviewPlayerMetadataView.java
	 *
	 * @function : OnCommentsFieldBlur
	 * 
	 * @description : 
	 *
	 *
	 * @version : 1.0
	 *
	 * @date: Jan 6, 2014
	 *
	 * @Author Gooru Team
	 *
	 * @Reviewer: Gooru Team
	 */
	private class OnCommentsFieldBlur implements BlurHandler{
		@Override
		public void onBlur(BlurEvent event) {
		
			if (commentField.getText().length() > 0){
				Map<String, String> parms = new HashMap<String, String>();
				parms.put("text", commentField.getText());
				AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {
	
					@Override
					public void onSuccess(Boolean value) {
						isHavingBadWords = value;
						if (value){
							commentField.getElement().getStyle().setBorderColor("orange");
							characterLimit.setText(i18n.GL0554());
							characterLimit.getElement().setAttribute("alt",i18n.GL0554());
							characterLimit.getElement().setAttribute("title",i18n.GL0554());
							characterLimit.setVisible(true);
						}else{
							commentField.getElement().getStyle().clearBackgroundColor();
							commentField.getElement().getStyle().setBorderColor("#ccc");
							characterLimit.setVisible(false);	
						}
					}
				});
			}
		}		
	}
	
	/**
	 * @fileName : PreviewPlayerMetadataView.java
	 *
	 * @description : OnCommentsFieldClicked sub class
	 *
	 * @version : 1.0
	 *
	 * @date: 03-Jan-2014
	 *
	 * @Author: Gooru Team
	 *
	 * @Reviewer: Gooru Team
	 */
	private class OnCommentsFieldClicked implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
		
			commentField.getElement().getStyle().clearBackgroundColor();
			commentField.getElement().getStyle().setBorderColor("#ccc");
			if(commentField.getText().trim().length()==0){
				modifyEditControls(false);
			}else{
				if (event.getSource() == commentField && !postCommentCancel.isVisible()) {
					modifyEditControls(true);
				}
			}
			
			
		}
	}
	
	/**
	 * @fileName : PreviewPlayerMetadataView.java
	 *
	 * @description : OnCommentsFieldValidated Sub class
	 *
	 * @version : 1.0
	 *
	 * @date: 03-Jan-2014
	 *
	 * @Author: Gooru Team
	 *
	 * @Reviewer: Gooru Team
	 */
	private class ValidateConfirmText implements KeyUpHandler {
		@Override
		public void onKeyUp(KeyUpEvent event) {
			if(commentField.getText().length()>415) {
				commentField.setText(commentField.getText().substring(0,415));
				commentField.getElement().setAttribute("alt",commentField.getText().substring(0,415));
				commentField.getElement().setAttribute("title",commentField.getText().substring(0,415));
				characterLimit.setText(i18n.GL0143());
				characterLimit.getElement().setAttribute("alt",i18n.GL0143());
				characterLimit.getElement().setAttribute("title",i18n.GL0143());
				characterLimit.setVisible(true);
			} else {
				if(commentField.getText().trim().length()==0){
					modifyEditControls(false);
				}else{
					modifyEditControls(true);
				}
				characterLimit.setVisible(false);
			}
		}
	}
	/**
	 * @function modifyEditControls 
	 * 
	 * @created_date : 03-Jan-2014
	 * 
	 * @description
	 * 
	 * @parm(s) : @param isCommentsFieldClicked
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	private void modifyEditControls(boolean isCommentsFieldClicked) {
		postCommentCancel.setVisible(isCommentsFieldClicked);
		if(isCommentsFieldClicked) {
			postCommentBtn.removeStyleName(SECONDARY_STYLE);
			postCommentBtn.removeStyleName(DISABLED_STYLE);
			postCommentBtn.addStyleName(PRIMARY_STYLE);
		} else {
			postCommentBtn.removeStyleName(PRIMARY_STYLE);
			postCommentBtn.addStyleName(SECONDARY_STYLE);
			postCommentBtn.addStyleName(DISABLED_STYLE);
		}
	}
	
	/**
	 * @function displaySuccessMsg 
	 * 
	 * @created_date : 03-Jan-2014
	 * 
	 * @description
	 * 
	 * @parm(s) : @param isVisible
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	@Override
	public void displaySuccessMsg(boolean isVisible) {
		commentField.setVisible(!isVisible);
		successPostMsg.setVisible(isVisible);
	}
	
	public static void onClosingAndriodorIpaddiv()
	{
		//studyMainContianer.getElement().setAttribute("style", "margin-top:50px;");
	}

	@Override
	public void setViewCount(String viewCount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUserProfileName(String gooruUid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLikesCount(int likesCount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayAuthorDetails(boolean isDisplayDetails) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Anchor getFlagButton() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTeacherInfo(ClasspageItemDo classpageItemDo) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void displaySpendTime(Long hours, Long mins, Double secs) {
			spendTimeContainer.clear();
			if(AppClientFactory.isAnonymous()){
				dispalyTime();
				return;
			}
			String minsString = (mins == 0)? "00": ((mins < 10)? "0"+mins : ""+mins );
			String secsString ="";
			if(secs>0 && secs<1){
				secsString="<1";
			}else{
				secsString = (secs == 0)? "00": ((secs < 10)? "0" + secs : "" + secs);
			}
	        if (hours > 0){
	        	displayTime(hours.toString(),hours==1?"hr":"hrs");
	        	displayTime(" "+minsString.toString(),minsString.equals("01")?"min":"mins");
	        	displayTime(" "+secsString.toString(),secsString.equals("01")?"sec":"secs");
	        }
	        else if (mins > 0){
	        	displayTime(minsString.toString(),minsString.equals("01")?"min":"mins");
	        	displayTime(" "+secsString.toString(),secsString.equals("01")?"sec":"sec");
	        }
	        else {
	        	displayTime(secsString.toString(),secsString.equals("01")?"sec":"sec");
	        }
	}
	public void displayTime(String time, String timeText){
		InlineLabel inlineTimeLabel=new InlineLabel(time);
		inlineTimeLabel.setStyleName(playerStyle.timeTextBig());
		InlineLabel inlineTimeString=new InlineLabel(timeText);
		inlineTimeString.setStyleName(playerStyle.timeTextSmall());
		spendTimeContainer.add(inlineTimeLabel);
		spendTimeContainer.add(inlineTimeString);
	}
	public void dispalyTime(){
		spendTimeContainer.clear();
		InlineLabel inlineTimeLabel=new InlineLabel("-");
		inlineTimeLabel.setStyleName(playerStyle.timeTextBig());
		spendTimeContainer.add(inlineTimeLabel);
	}
	public void displayScore(String collectionScore, String noOfQuestions){
		InlineLabel inlineTimeLabel=new InlineLabel(collectionScore);
		inlineTimeLabel.setStyleName(playerStyle.timeTextBig());
		InlineLabel inlineTimeString=new InlineLabel("/"+noOfQuestions);
		inlineTimeString.setStyleName(playerStyle.timeTextSmall());
		scoreContainer.add(inlineTimeLabel);
		scoreContainer.add(inlineTimeString);
	}


	@Override
	public void displayScoreCount(Integer collectionScore, Integer noOfQuestions) {
		if(AppClientFactory.isAnonymous()){
			noOfQuestions=0;
		}
		scoreContainer.clear();
		if(noOfQuestions==0){
			InlineLabel inlineTimeLabel=new InlineLabel("-");
			inlineTimeLabel.setStyleName(playerStyle.timeTextBig());
			scoreContainer.add(inlineTimeLabel);
		}else{
			displayScore(collectionScore.toString(),noOfQuestions.toString());
		}
	}
	
	public void displayNextCollectionDetails(final CollectionDo collectionDo,final String subjectId,final String lessonId,final String libraryType){
		if(collectionDo!=null){
			hideNextCollectionContainer(true);
			whatNextCollectionTitle.setText(collectionDo.getTitle().substring(0,10)+"...");
			whatNextCollectionTitle.setTitle(collectionDo.getTitle());
			nextCollectionThumbnail.setUrl(collectionDo.getThumbnails().getUrl());
			if(collectionDo!=null&&collectionDo.getCollectionItems()!=null){
				hideNextCollectionContainer(false);
				int questionCount=0;
				int resourceCount=0;
				for(int i=0;i<collectionDo.getCollectionItems().size();i++){
					if(collectionDo.getCollectionItems().get(i).getResource().getResourceFormat()!=null){
						if(collectionDo.getCollectionItems().get(i).getResource().getResourceFormat().getDisplayName().equalsIgnoreCase("Question")){
							questionCount++;
						}else{
							resourceCount++;
						}
					}
				}
				if(resourceCount>0){
					this.resourceCount.setText(resourceCount==1?resourceCount+" Resource":resourceCount+" Resources");
				}
				if(questionCount>0){
					this.questionCount.setText(questionCount==1?questionCount+" Question":questionCount+" Questions");
				}
				nextCollectionThumbnail.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						Map<String,String> params = new LinkedHashMap<String,String>();
						params.put("id", collectionDo.getGooruOid());
						if(subjectId!=null){
							params.put("subject", subjectId);
						}
						if(lessonId!=null){
							params.put("lessonId", lessonId);
						}
						if(libraryType!=null){
							params.put("library", libraryType);
						}
						AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.COLLECTION_PLAY, params);
					}
				});
			}
		}else{
			hideNextCollectionContainer(true);
		}
	}
	
	public void hideNextCollectionContainer(boolean hide){
		nextCollectionContainer.setVisible(!hide);
	}
	
	public void  getAverageReaction(){
		getUiHandlers().getAvgReaction();
	}
	
	public void showAvgReaction(String reactionType){
		if(AppClientFactory.isAnonymous()){
			reactionType=null;
		}
		avgReactionImage.setText("");
		if(reactionType!=null){
			if(reactionType.equals(ResourcePlayerMetadataView.REACTION_CAN_EXPLAIN)){
				avgReactionImage.setStyleName(playerStyle.reactionCanExplain());
			}else if(reactionType.equals(ResourcePlayerMetadataView.REACTION_CAN_UNDERSTAND)){
				avgReactionImage.setStyleName(playerStyle.reactionCanUnderstand());
			}else if(reactionType.equals(ResourcePlayerMetadataView.REACTION_DONOT_UNDERSTAND)){
				avgReactionImage.setStyleName(playerStyle.reactionDonotUnderstand());
			}else if(reactionType.equals(ResourcePlayerMetadataView.REACTION_MEH)){
				avgReactionImage.setStyleName(playerStyle.reactionMeh());
			}else if(reactionType.equals(ResourcePlayerMetadataView.REACTION_NEED_HELP)){
				avgReactionImage.setStyleName(playerStyle.reactionNeedHelp());
			}
		}else{
			avgReactionImage.setText("-");
			avgReactionImage.setStyleName(playerStyle.timeTextBig());
		}
	}
	@UiHandler("changeAssignmentStatusButton")
	public void clickOnStatusChangeBtn(ClickEvent event) {
		if (changeAssignmentStatusButton.isChecked()){
		getUiHandlers().updateCommentsStatus("turn-on");
		}
		else{
		getUiHandlers().updateCommentsStatus("turn-off");
		}
	}
	
	public ResourceServiceAsync getResourceService() {
		return resourceService;
	}

	public void setResourceService(ResourceServiceAsync resourceService) {
		this.resourceService = resourceService;
	}
	
	@Override
	public void changeCommentsButton(CollectionDo result) {
		if(result.getSettings()!=null)
		{
			if(result.getSettings().getComment()!=null)
			{
				if(result.getSettings().getComment().equalsIgnoreCase("turn-on"))
				{
					requiredLabel.removeStyleName(playerStyle.mutedText());
					optionalLabel.removeStyleName(playerStyle.mutedText());								
					commentField.setEnabled(true);
					commentssection.getElement().getStyle().setOpacity(1);
					changeAssignmentStatusButton.setChecked(true);
				}
				else
				{
					requiredLabel.setStyleName(playerStyle.mutedText());
					optionalLabel.setStyleName(playerStyle.mutedText());								
					commentField.setEnabled(false);
					postCommentBtn.setEnabled(false);
					postCommentBtn.removeStyleName(PRIMARY_STYLE);
					postCommentBtn.addStyleName(SECONDARY_STYLE);
					postCommentBtn.addStyleName(DISABLED_STYLE);
					commentssection.getElement().getStyle().setOpacity(0.5);
					changeAssignmentStatusButton.setChecked(false);
				}
				
			}
			else
			{
				requiredLabel.setStyleName(playerStyle.mutedText());
				optionalLabel.setStyleName(playerStyle.mutedText());
			}
		}
		else
		{
			requiredLabel.setStyleName(playerStyle.mutedText());
			optionalLabel.setStyleName(playerStyle.mutedText());
		}
		
	}


	@Override
	public void setSessionsData(ArrayList<CollectionSummaryUsersDataDo> result) {
		sessionsDropDown.clear();
		sessionData.clear();
		for (CollectionSummaryUsersDataDo collectionSummaryUsersDataDo : result) {
			sessionData.put(collectionSummaryUsersDataDo.getSessionId(), collectionSummaryUsersDataDo.getTimeStamp());
			int day=collectionSummaryUsersDataDo.getFrequency();
			sessionsDropDown.addItem(day+AnalyticsUtil.getOrdinalSuffix(day)+" Session",collectionSummaryUsersDataDo.getSessionId());
		}
		setSessionStartTime(result.size()-1);
	}
	public void setSessionStartTime(int selectedIndex) {
		if(sessionData.size()!=0){
			lastModifiedTime.setText(AnalyticsUtil.getCreatedTime(Long.toString(sessionData.get(sessionsDropDown.getValue(selectedIndex)))));
			sessionsDropDown.setSelectedIndex(selectedIndex);
			printData.setUserName(null);
			printData.setSession(sessionsDropDown.getItemText(selectedIndex));
			printData.setSessionStartTime(AnalyticsUtil.getCreatedTime(Long.toString(sessionData.get(sessionsDropDown.getValue(selectedIndex)))));
		}
	}

	@Override
	public void setCollectionMetaDataByUserAndSession(ArrayList<CollectionSummaryMetaDataDo> result) {
		if(result.size()!=0){
			collectionTitle.setText(result.get(0).getTitle());
			collectionLastAccessed.setText(AnalyticsUtil.getCreatedTime(Long.toString(result.get(0).getLastModified())));
			if(result.get(0).getThumbnail()!=null){
				collectionImage.setUrl(result.get(0).getThumbnail());
			}else{
				collectionImage.setUrl("images/analytics/default-collection-image.png");
			}
			collectionImage.addErrorHandler(new ErrorHandler() {
				@Override
				public void onError(ErrorEvent event) {
					collectionImage.setUrl("images/analytics/default-collection-image.png");
				}
			});
			collectionResourcesCount.setText((result.get(0).getResourceCount()-result.get(0).getTotalQuestionCount())+" Resources | "+result.get(0).getTotalQuestionCount()+" Questions");
		}
	}
	
	@Override
	public void resetCollectionMetaData(){
		collectionTitle.setText("");
		collectionLastAccessed.setText("");
		collectionImage.setUrl("");
		collectionResourcesCount.setText("");
	}

	@Override
	public HTMLPanel getLoadingImageLabel() {
		return loadingImageLabel;
	}
	
	/**
	 * @function removeHtmlTags 
	 * 
	 * @created_date : 15-Dec-2014
	 * 
	 * @description this method is used to remove the html tags in comment input box
	 * 
	 * @parm(s) : @param String 
	 * 
	 * @return : String
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	private String removeHtmlTags(String html){
		html = html.replaceAll("(<\\w+)[^>]*(>)", "$1$2");
		html = html.replaceAll("</p>", " ").replaceAll("<p>", "").replaceAll("<br data-mce-bogus=\"1\">", "").replaceAll("<br>", "").replaceAll("</br>", "").replaceAll("</a>", "").replaceAll("<a>", "");
        return html;
	}

}
