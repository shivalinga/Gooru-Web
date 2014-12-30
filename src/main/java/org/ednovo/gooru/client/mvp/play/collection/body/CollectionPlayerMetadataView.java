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
package org.ednovo.gooru.client.mvp.play.collection.body;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.client.DataInsightsUrlTokens;
import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.effects.FadeInAndOut;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BaseViewWithHandlers;
import org.ednovo.gooru.client.mvp.home.LoginPopupUc;
import org.ednovo.gooru.client.mvp.play.collection.preview.PreviewPlayerPresenter;
import org.ednovo.gooru.client.mvp.play.collection.preview.metadata.comment.CommentWidgetChildView;
import org.ednovo.gooru.client.mvp.search.SearchResultWrapperCBundle;
import org.ednovo.gooru.client.mvp.search.SearchUiUtil;
import org.ednovo.gooru.client.service.ResourceServiceAsync;
import org.ednovo.gooru.client.uc.CollaboratorsUc;
import org.ednovo.gooru.client.uc.DownToolTipWidgetUc;
import org.ednovo.gooru.client.uc.PlayerBundle;
import org.ednovo.gooru.client.uc.StandardSgItemVc;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.content.ClasspageItemDo;
import org.ednovo.gooru.shared.model.content.CollectionDo;
import org.ednovo.gooru.shared.model.content.StandardFo;
import org.ednovo.gooru.shared.model.content.checkboxSelectedDo;
import org.ednovo.gooru.shared.model.library.ConceptDo;
import org.ednovo.gooru.shared.model.player.CommentsDo;
import org.ednovo.gooru.shared.model.player.CommentsListDo;
import org.ednovo.gooru.shared.util.StringUtil;
import org.ednovo.gooru.shared.util.UAgentInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

public class CollectionPlayerMetadataView extends BaseViewWithHandlers<CollectionPlayerMetadataUiHandlers> implements IsCollectionPlayerMetadataView{

	@UiField
	static FlowPanel studyMainContianer;
	@UiField
	FlowPanel metadataContainer;
	@UiField
	FlowPanel standardsContainer,leftPanelContainer,rightPanelContainer;
	@UiField
	FlowPanel teamContainer;
	/*@UiField
	FlowPanel messageContainer;*/
	/*@UiField
	FlowPanel frameContainer;*/
	@UiField
	FlowPanel courseTitle;
	@UiField VerticalPanel commentsContainer;
	@UiField TextArea commentField;
	@UiField Button postCommentBtn,postCommentCancel;
	@UiField Label userNameLabel,viewsCountLabel,lblClassInfo,classTitleValue,lblclassTitle,lblTeacher,lbldueDate,lblDirections,lblDirectionsDesc,commentCount,seeMoreButton,noCommentsLbl,toCommentText,orText,loginMessagingText,characterLimit;
	@UiField Label lblAuthor, lblCourse, lblStandards,teacherNameLabel,dueDate,/*insightsHeaderText,insightsContentText,*/lbllanguageObjectiveText,lbllanguageObjective,successPostMsg,
				lbldepthOfKnowledgeText,lbllearningAndInnovationText,lblAudienceText,lblInstructionalmethodText;
	@UiField Image profileThumbnailImage,userPhoto;
	@UiField HTMLPanel teacherPanel,classInfoPanel,authorPanel,courseSection,standardSection,teacherContainer,viewSection,dueDateSection,directionSection,teacherProfileContainer,languageObjectiveContainer,addComment,loginMessaging,
						depthOfKnowledgePanel,audiencePanel,instructionalmethodPanel,learningAndInnovationSkillPanel,
						InstructionalmethodContainer,audienceContainer,learningAndInnovationSkillsContainer,depthOfKnowledgeContainer,switchContainer,commentssection;
	@UiField Anchor previewFlagButton,seeMoreAnchor,loginUrl, signupUrl;
	@UiField CollectionPlayerStyleBundle playerStyle;
	@UiField HTML teacherTipLabel;
	@UiField Label lblCharLimitComments;
	@UiField SimpleCheckBox changeAssignmentStatusButton;
	@UiField InlineLabel requiredLabel,optionalLabel;
	//@UiField Frame insightsFrame;
//	@UiField Button collectionSummaryPrintBtn;
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
	
	@Inject
	private ResourceServiceAsync resourceService;
	
	private static CollectionPlayerMetadataViewUiBinder uiBinder = GWT.create(CollectionPlayerMetadataViewUiBinder.class);

	interface CollectionPlayerMetadataViewUiBinder extends UiBinder<Widget, CollectionPlayerMetadataView> {
	}
	
	private MessageProperties i18n = GWT.create(MessageProperties.class);
	
	@Inject
	public CollectionPlayerMetadataView(){
		setWidget(uiBinder.createAndBindUi(this));
		setLabelAndIds();
		teacherContainer.setVisible(false);
		//messageContainer.setVisible(false);
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
		
		loginMessagingText.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().toCommentTextPreviewPlayer());
		orText.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().toCommentTextPreviewPlayer());
		toCommentText.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().toCommentTextPreviewPlayer());
		loginUrl.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().toCommentTextPreviewPlayer());
		signupUrl.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().toCommentTextPreviewPlayer());
		
		commentField.addClickHandler(new OnCommentsFieldClicked());
		commentField.addKeyUpHandler(new ValidateConfirmText());
		commentField.addBlurHandler(new OnCommentsFieldBlur());
		
		String value = StringUtil.generateMessage(i18n.GL2103(), "500");
		lblCharLimitComments.setText(value);
		StringUtil.setAttributes(lblCharLimitComments.getElement(), "lblCharLimitComments", value, value);
		
		seeMoreButton.setVisible(false);
		depthOfKnowledgeContainer.getElement().setId("pnlDepthOfKnowledgeContainer");
		depthOfKnowledgePanel.getElement().setId("pnlDepthOfKnowledgePanel");
		learningAndInnovationSkillsContainer.getElement().setId("pnlLearningAndInnovationSkillsContainer");
		learningAndInnovationSkillPanel.getElement().setId("pnlLearningAndInnovationSkillPanel");
		audienceContainer.getElement().setId("pnlAudienceContainer");
		audiencePanel.getElement().setId("pnlAudiencePanel");
		InstructionalmethodContainer.getElement().setId("pnlInstructionalmethodContainer");
		instructionalmethodPanel.getElement().setId("pnlInstructionalmethodPanel");
		Boolean isIpad = !!Navigator.getUserAgent().matches("(.*)iPad(.*)");
		Boolean isAndriod = !!Navigator.getUserAgent().matches("(.*)Android(.*)");
		Boolean isWinDskp = !!Navigator.getUserAgent().matches("(.*)NT(.*)");
		UAgentInfo detector = new UAgentInfo(Navigator.getUserAgent());
		if(isIpad && !StringUtil.IPAD_MESSAGE_Close_Click) {
		  studyMainContianer.getElement().setAttribute("style", "margin-top:0px;");
		}else if(isAndriod && !StringUtil.IPAD_MESSAGE_Close_Click){
		  studyMainContianer.getElement().setAttribute("style", "margin-top:0px;");
		} else{
		  //studyMainContianer.getElement().setAttribute("style", "margin-top:50px;");
		}
	}
	
	@Override
	public void setCollectionMetadata(CollectionDo collectionDo) {
		this.collectionDo = collectionDo;
		teamContainer.clear();
		//getUiHandlers().getFlagedReport(collectionDo.getGooruOid());
		if (collectionDo.getMeta() !=null && collectionDo.getMeta().getCollaboratorCount()>0){
			 CollaboratorsUc collaboratorsUc=new CollaboratorsUc(collectionDo);
			 teamContainer.add(collaboratorsUc);
			setUserName(collectionDo.getUser().getUsernameDisplay() +" " + i18n.GL_GRR_AND());
		}else{
			setUserName(collectionDo.getUser().getUsernameDisplay());
		}
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
							postCommentBtn.setEnabled(true);
							postCommentBtn.setStyleName(PRIMARY_STYLE);
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
		setUserProfileImage(collectionDo.getUser().getGooruUId());
		renderCourseInfo(collectionDo.getMetaInfo().getCourse());
		renderStandards(standardsContainer,getStandardsMap(this.collectionDo.getMetaInfo().getStandards()));
		renderLanguageObjective(collectionDo.getLanguageObjective());
		renderDepthOfKnowledge(collectionDo.getDepthOfKnowledges());
		renderInstructionalMethod(collectionDo.getInstructionalMethod());
		renderAudience(collectionDo.getAudience());
		renderLearningAndInnovationSkill(collectionDo.getLearningSkills());
		if(collectionDo.getKeyPoints() != null){
			if(collectionDo.getKeyPoints().length()>410){
				authorPanel.getElement().getStyle().setHeight(295, Unit.PX);
			}
			else if(collectionDo.getKeyPoints().length()>300){
				authorPanel.getElement().getStyle().setHeight(253, Unit.PX);
			}
			else if(collectionDo.getKeyPoints().length()>100){
				authorPanel.getElement().getStyle().setHeight(180, Unit.PX);
			}
			else{
				authorPanel.getElement().getStyle().setHeight(100, Unit.PX);
			}
			teacherTipLabel.setHTML(""+collectionDo.getKeyPoints()+"");		
			teacherTipLabel.getElement().setAttribute("alt",""+collectionDo.getKeyPoints()+"");
			teacherTipLabel.getElement().setAttribute("title",""+collectionDo.getKeyPoints()+"");
		}else{
			teacherTipLabel.setHTML("");
			authorPanel.getElement().getStyle().clearHeight();
		}
		setLeftPanelHeight();
	}
	private void setLeftPanelHeight(){
		 Scheduler.get().scheduleDeferred(new ScheduledCommand(){
			@Override
			public void execute() {
				int height=rightPanelContainer.getElement().getOffsetHeight();
				if(height>650){
					leftPanelContainer.getElement().setAttribute("style", "min-height:"+height+"px;");
				} 
			}
	      });
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
		}
	}
	

	public void setLabelAndIds() {
//		collectionSummaryLbl.setVisible(false);
		lblAuthor.setText(i18n.GL0573());
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
		previewFlagButton.getElement().setAttribute("title",i18n.GL0556());
		
//		collectionSummaryLbl.setText(i18n.GL1544);
//		emptyMsgDescOne.setText(i18n.GL1545);
//		emptyMsgDescTwo.setText(i18n.GL1546);
//		insightsHeaderText.setText(i18n.GL1626());
//		insightsHeaderText.getElement().setId("lblInsightsHeaderText");
//		insightsHeaderText.getElement().setAttribute("alt",i18n.GL1626());
//		insightsHeaderText.getElement().setAttribute("title",i18n.GL1626());
//		
//		insightsContentText.setText(i18n.GL1627());
//		insightsContentText.getElement().setId("lblInsightsContentText");
//		insightsContentText.getElement().setAttribute("alt",i18n.GL1627());
//		insightsContentText.getElement().setAttribute("title",i18n.GL1627());
		
		previewFlagButton.removeStyleName(PlayerBundle.INSTANCE.getPlayerStyle().previewCoverFlagImageOrange());
		previewFlagButton.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().playerPreviewCoverFlagImage());
		lbllanguageObjectiveText.setText(i18n.GL1721());
		lbllanguageObjectiveText.getElement().setId("lblLanguageObjectiveText");
		lbllanguageObjectiveText.getElement().setAttribute("alt",i18n.GL1721());
		lbllanguageObjectiveText.getElement().setAttribute("title",i18n.GL1721());
		
		studyMainContianer.getElement().setId("fpnlStudyMainContianer");
		metadataContainer.getElement().setId("fpnlMetadataContainer");
		teacherContainer.getElement().setId("pnlTeacherContainer");
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
		seeMoreAnchor.getElement().setId("lnkSeeMoreAnchor");
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
		
		lbldepthOfKnowledgeText.setText(i18n.GL1693());
		lbldepthOfKnowledgeText.getElement().setId("lbldepthOfKnowledgeText");
		lbldepthOfKnowledgeText.getElement().setAttribute("alt",i18n.GL1693());
		lbldepthOfKnowledgeText.getElement().setAttribute("title",i18n.GL1693());
		
		lbllearningAndInnovationText.setText(i18n.GL1722());
		lbllearningAndInnovationText.getElement().setId("lbllearningAndInnovationText");
		lbllearningAndInnovationText.getElement().setAttribute("alt",i18n.GL1722());
		lbllearningAndInnovationText.getElement().setAttribute("title",i18n.GL1722());
		
		lblAudienceText.setText(i18n.GL1723());
		lblAudienceText.getElement().setId("lblAudienceText");
		lblAudienceText.getElement().setAttribute("alt",i18n.GL1723());
		lblAudienceText.getElement().setAttribute("title",i18n.GL1723());
		
		lblInstructionalmethodText.setText(i18n.GL1724());
		lblInstructionalmethodText.getElement().setId("lblInstructionalmethodText");
		lblInstructionalmethodText.getElement().setAttribute("alt",i18n.GL1724());
		lblInstructionalmethodText.getElement().setAttribute("title",i18n.GL1724());
		
		
//		dataInsightsPanel.getElement().setId("pnlDataInsightsPanel");
//		frameContainer.getElement().setId("fpnlFrameContainer");
//		messageContainer.getElement().setId("fpnlMessageContainer");
	}
	
	/*public void setUserName(String userName){
		userNameLabel.setText(userName);
		usernameAnchor = new Anchor();
		//userNameLabel.setText(userName);
		if(StringUtil.isPartnerUser(collectionDo.getUser().getUsername())){
			usernameAnchor.setHref("#"+collectionDo.getUser().getUsernameDisplay());
		}
	else{
		String token= "#"+PlaceTokens.PROFILE_PAGE+"&id="+collectionDo.getUser().getGooruUId()+"&user="+collectionDo.getUser().getUsername();
		usernameAnchor.setHref(token);
	}
		usernameAnchor.setText(userName);
		usernameAnchor.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().setUserText());
		usernameAnchor.setTarget("_blank");
		userNameLabel.setText("");
		userNameLabel.getElement().appendChild(usernameAnchor.getElement());
	}*/
	public void setUserName(String userName){
		
		//userNameLabel.setText(userName);
		if(collectionDo.getUser().getCustomFields()!=null){
			if(collectionDo.getUser().getCustomFields().get(0).getOptionalValue()!=null){
				String getUserProfileStatus	=collectionDo.getUser().getCustomFields().get(0).getOptionalValue();
				if(getUserProfileStatus.equalsIgnoreCase("true")){
					usernameAnchor = new Anchor();
					if(StringUtil.isPartnerUser(collectionDo.getUser().getUsername())){
						usernameAnchor.setHref("#"+collectionDo.getUser().getUsernameDisplay());
					}
				else{
					String token= "#"+PlaceTokens.PROFILE_PAGE+"&id="+collectionDo.getUser().getGooruUId()+"&user="+collectionDo.getUser().getUsername();
					usernameAnchor.setHref(token);
				}
					usernameAnchor.setText(userName);
					usernameAnchor.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().setUserText());
					usernameAnchor.setTarget("_blank");
					userNameLabel.setText("");
					userNameLabel.getElement().appendChild(usernameAnchor.getElement());
				}
				else{
					userNameLabel.setText(userName);
					userNameLabel.getElement().setAttribute("alt",userName);
					userNameLabel.getElement().setAttribute("title",userName);
				}
			}
			
		}
		
	}
	public void setUserProfileImage(String profileUserId){
		profileThumbnailImage.setUrl(AppClientFactory.loggedInUser.getSettings().getProfileImageUrl()+profileUserId+".png?p="+Math.random());
	}
	@UiHandler("profileThumbnailImage")
	public void setDefaultProfileImage(ErrorEvent event){
		profileThumbnailImage.setUrl("images/settings/setting-user-image.png");
	}
	public void setViewCount(String viewCount){
		String viewsText=Integer.parseInt(viewCount)==1?viewCount+" "+i18n.GL1428():viewCount+" "+i18n.GL0934();
		viewsCountLabel.setText(viewsText);
		viewsCountLabel.getElement().setAttribute("alt",viewsText);
		viewsCountLabel.getElement().setAttribute("title",viewsText);
	}
	public void renderCourseInfo(List<String> courseInfo){
		courseTitle.clear();
		if(courseInfo!=null&&courseInfo.size()>0){
			courseSection.setVisible(true);
			//setCourseTitle(courseInfo.get(0));
			SearchUiUtil.renderMetaData(courseTitle, courseInfo, 0);
			Label dummyLabel=new Label();
			dummyLabel.setStyleName(playerStyle.clearBoth());
			courseTitle.add(dummyLabel);
		}else{
			courseSection.setVisible(false);
		}
	}
	public void setCourseTitle(String title){
		//courseTitle.setText(title);
	}
	public void setLikesCount(int likesCount){
		
	}
	public void resetMetadataFields(){
		userNameLabel.setText("");
		userNameLabel.getElement().setAttribute("alt","");
		userNameLabel.getElement().setAttribute("title","");
		viewsCountLabel.setText("");
		viewsCountLabel.getElement().setAttribute("alt","");
		viewsCountLabel.getElement().setAttribute("title","");
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
	public void setUserProfileName(String gooruUid) {
		Anchor anchor = new Anchor();
		String userName = userNameLabel.getText();
		if(StringUtil.isPartnerUser(collectionDo.getUser().getUsername())){
			anchor.setHref("#"+collectionDo.getUser().getUsernameDisplay());
		}else{
			String token= "#"+PlaceTokens.PROFILE_PAGE+"&id="+gooruUid+"&user="+collectionDo.getUser().getUsername();
			anchor.setHref(token);
		}
		anchor.setText(userName);
		anchor.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().setUserText());
		anchor.setTarget("_blank");
		userNameLabel.setText("");
		userNameLabel.getElement().appendChild(anchor.getElement());
	}
	public void renderStandards(FlowPanel standardsContainer, List<Map<String,String>> standardsList) {
		standardsContainer.clear();
		if (standardsList != null&&standardsList.size()>0) {
			standardSection.setVisible(true);
			Iterator<Map<String, String>> iterator = standardsList.iterator();
			int count = 0;
			FlowPanel toolTipwidgets = new FlowPanel();
			while (iterator.hasNext()) {
				Map<String, String> standard = iterator.next();
				String stdCode = standard.get(STANDARD_CODE);
				String stdDec = standard.get(STANDARD_DESCRIPTION);
				if (count > 2) {
					if (count < 18){
						StandardSgItemVc standardItem = new StandardSgItemVc(stdCode, stdDec);
						toolTipwidgets.add(standardItem);
					}
				} else {
					DownToolTipWidgetUc toolTipUc = new DownToolTipWidgetUc(new Label(stdCode), new Label(stdDec), standardsList);
					toolTipUc.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().getstandardMoreInfo());
					standardsContainer.add(toolTipUc);
				}
				count++;
			}
			if (standardsList.size()>18){
				final Label left = new Label("+"+(standardsList.size() - 18));
				toolTipwidgets.add(left);
			}
			if (standardsList.size() > 2) {
				Integer moreStandardsCount = standardsList.size() - 3;
				if (moreStandardsCount > 0){
					DownToolTipWidgetUc toolTipUc = new DownToolTipWidgetUc(new Label("+" + moreStandardsCount), toolTipwidgets, standardsList);
					toolTipUc.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().getstandardMoreLink());
					standardsContainer.add(toolTipUc);
				}
			}
		}else{
			standardSection.setVisible(false);
		}
	}
	
	public void displayAuthorDetails(boolean isDisplayDetails) {
		authorPanel.setVisible(isDisplayDetails);
		teacherContainer.setVisible(!isDisplayDetails);
	}
	
	@UiHandler("previewFlagButton")
	public void clickOnpreviewFlagButton(ClickEvent event) {
		String view=AppClientFactory.getPlaceManager().getRequestParameter("view", null);
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("id", collectionDo.getGooruOid());
		params = PreviewPlayerPresenter.setConceptPlayerParameters(params);
		if(view!=null&&view.equalsIgnoreCase("end")){
			params.put("view", "end");
			params.put("tab", "flag");
			PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(AppClientFactory.getCurrentPlaceToken(), params);
			AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, true);
		}else{
			params.put("tab", "flag");
			PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(AppClientFactory.getCurrentPlaceToken(), params);
			AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, true);
		}
		
	}

	public Anchor getFlagButton() {
		return previewFlagButton;
	}

	@Override
	public void setRelatedConceptsContent(ArrayList<ConceptDo> conceptDoList,
			String coursePage, String subject, String lessonId,
			String libraryName) {
		
	}

	@Override
	public void isConceptsContainerVisible(boolean isVisible) {
		
	}

	@Override
	public void setTeacherInfo(ClasspageItemDo classpageItemDo) {
		displayAuthorDetails(false);
		teacherContainer.setVisible(true);
		classTitleValue.setText(classpageItemDo.getTitle());
		classTitleValue.getElement().setAttribute("alt",classpageItemDo.getTitle());
		classTitleValue.getElement().setAttribute("title",classpageItemDo.getTitle());
		
		lblclassTitle.setText(i18n.GL1578());
		lblclassTitle.getElement().setAttribute("alt",i18n.GL1578());
		lblclassTitle.getElement().setAttribute("title",i18n.GL1578());
		
		lblClassInfo.setText(i18n.GL1579());
		lblClassInfo.getElement().setAttribute("alt",i18n.GL1579());
		lblClassInfo.getElement().setAttribute("title",i18n.GL1579());
		
		lblTeacher.setText(i18n.GL1580());
		lblTeacher.getElement().setAttribute("alt",i18n.GL1580());
		lblTeacher.getElement().setAttribute("title",i18n.GL1580());
		
		lbldueDate.setText(i18n.GL1581());
		lbldueDate.getElement().setAttribute("alt",i18n.GL1581());
		lbldueDate.getElement().setAttribute("title",i18n.GL1581());
		
		lblDirections.setText(i18n.GL1582());
		lblDirections.getElement().setAttribute("alt",i18n.GL1582());
		lblDirections.getElement().setAttribute("title",i18n.GL1582());
		
		setDueDateText(classpageItemDo.getPlannedEndDate());
		setDirectionText(classpageItemDo.getDirection());
		teacherNameLabel.setText(classpageItemDo.getUserNameDispaly());
		teacherNameLabel.getElement().setAttribute("alt",classpageItemDo.getUserNameDispaly());
		teacherNameLabel.getElement().setAttribute("title",classpageItemDo.getUserNameDispaly());
		
		teacherProfileContainer.clear();
		//teacherProfileThumbnailImage.setUrl(classpageItemDo.getProfileImageUrl()+"?p="+Math.random()); 
		teacherProfileContainer.add(new TeacherImage(classpageItemDo.getProfileImageUrl()+"?p="+Math.random()));
	}
	
	public void setDueDateText(String text){
		if(text!=null&&!text.trim().equals("")){
			dueDate.setText(text);
			dueDate.getElement().setAttribute("alt",text);
			dueDate.getElement().setAttribute("title",text);
			dueDateSection.setVisible(true);
		}else{
			dueDateSection.setVisible(false);
		}
	}
	public void setDirectionText(String text){
		if(text!=null&&!text.trim().equals("")){
			lblDirectionsDesc.setText(text);
			lblDirectionsDesc.getElement().setAttribute("alt",text);
			lblDirectionsDesc.getElement().setAttribute("title",text);
			directionSection.setVisible(true);
		}else{
			directionSection.setVisible(false);
		}
	}
	
//	@UiHandler("teacherProfileThumbnailImage")
//	public void setDefaultTeacherProfileImage(ErrorEvent event){
//		teacherProfileThumbnailImage.setUrl("images/settings/setting-user-image.png");
//	}
	
	public void hideCollectionDetails(boolean hide){
		authorPanel.setVisible(!hide);
		courseSection.setVisible(!hide);
		standardSection.setVisible(!hide);
		viewSection.setVisible(!hide);
	}
	

	public class TeacherImage extends Composite{
		private Image image=null;
		private String teacherImageUrl="";
		public TeacherImage(String imageUrl){
			initWidget(image=new Image());
			this.teacherImageUrl=imageUrl;
			image.setStyleName(playerStyle.userImage());
			image.addErrorHandler(new TeacherErrorEvent());
		}
		public void onLoad(){
			image.setUrl(teacherImageUrl);
		}
		public class TeacherErrorEvent implements ErrorHandler{
			@Override
			public void onError(ErrorEvent event) {
				image.setUrl("images/settings/setting-user-image.png");
			}
		}
	}
	
	
	
	
//	@UiHandler("insightsFrame")
//	public void ifrmaeContentLoaded(LoadEvent laodEvent){
//		loginPopupTimer.schedule(500);
//	}
	
	public void setDataInsightsUrl(){
		String page=AppClientFactory.getPlaceManager().getRequestParameter("page", null);
		if(AppClientFactory.isAnonymous()){
//			frameContainer.clear();
//			frameContainer.setVisible(false);
//			messageContainer.setVisible(true);
		}else if(page!=null&&page.equals("teach")){
//			frameContainer.clear();
//			frameContainer.setVisible(false);
//			messageContainer.setVisible(false);
		}else{
//			frameContainer.clear();
//			frameContainer.setVisible(true);
//			messageContainer.setVisible(false);
//			frameContainer.add(new DataInsightsIframe(StringUtil.generateMessage(AppClientFactory.getLoggedInUser().getSettings().getAnalyticsEndPoint()+DataInsightsUrlTokens.STUDYPLAYER_SUMMARY_DATA,
//					collectionDo.getGooruOid(),AppClientFactory.getGooruUid(),"",AppClientFactory.getLoginSessionToken())));
		}
	}
	
	public void setClasspageInsightsUrl(String classpageId, String sessionId){
		if(sessionId==null) {
			sessionId = "";
		}
		String page=AppClientFactory.getPlaceManager().getRequestParameter("page", null);
//		if(AppClientFactory.isAnonymous()){
//			frameContainer.clear();
//			frameContainer.setVisible(false);
//			messageContainer.setVisible(true);
//		}else if(page!=null&&page.equals("teach")){
//			frameContainer.clear();
//			frameContainer.setVisible(false);
//			messageContainer.setVisible(false);
//		}else{
//			frameContainer.clear();
//			frameContainer.setVisible(true);
//			messageContainer.setVisible(false);
//			frameContainer.add(new DataInsightsIframe(StringUtil.generateMessage(AppClientFactory.getLoggedInUser().getSettings().getAnalyticsEndPoint()+DataInsightsUrlTokens.PLAYER_CLASS_PREVIOUS_DATA,
//					classpageId,collectionDo.getGooruOid(),AppClientFactory.getGooruUid(),sessionId,AppClientFactory.getLoginSessionToken())));
//		}
	}
	
	public void setDataInsightsSummaryUrl(String sessionId){
		String page=AppClientFactory.getPlaceManager().getRequestParameter("page", null);
//		if(AppClientFactory.isAnonymous()){
//			frameContainer.clear();
//			frameContainer.setVisible(false);
//			messageContainer.setVisible(true);
//		}else if(page!=null&&page.equals("teach")){
//			frameContainer.clear();
//			frameContainer.setVisible(false);
//			messageContainer.setVisible(false);
//		}else{
//			frameContainer.clear();
//			frameContainer.setVisible(true);
//			messageContainer.setVisible(false);
//			sessionId=sessionId!=null?sessionId:"";
//			frameContainer.add(new DataInsightsIframe(StringUtil.generateMessage(AppClientFactory.getLoggedInUser().getSettings().getAnalyticsEndPoint()+DataInsightsUrlTokens.STUDYPLAYER_SUMMARY_DATA,
//					collectionDo.getGooruOid(),AppClientFactory.getGooruUid(),sessionId,AppClientFactory.getLoginSessionToken())));
//		}
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
	
	public void renderLanguageObjective(String languageObjective){	
		lbllanguageObjective.getElement().setAttribute("style", "word-wrap: break-word;");
		if(languageObjective!=null){
			languageObjectiveValue=languageObjective;
			languageObjectiveContainer.setVisible(true);
			//lbllanguageObjectiveAll.setVisible(false);
			seeMoreAnchor.getElement().setAttribute("style", "float:right;");
			if(languageObjective.length()>=200){
				seeMoreAnchor.setText(i18n.GL1728());	
				seeMoreAnchor.getElement().setAttribute("alt",i18n.GL1728());
				seeMoreAnchor.getElement().setAttribute("title",i18n.GL1728());
				seeMoreAnchor.setVisible(true);
				lbllanguageObjective.setText(languageObjective.substring(0,200));
				lbllanguageObjective.getElement().setAttribute("alt",languageObjective.substring(0,200));
				lbllanguageObjective.getElement().setAttribute("title",languageObjective.substring(0,200));
					//lbllanguageObjectiveAll.setText(languageObjective.substring(80,languageObjective.length()));
			}else{
				seeMoreAnchor.setVisible(false);
				lbllanguageObjective.setText(languageObjective);
				lbllanguageObjective.getElement().setAttribute("alt",languageObjective);
				lbllanguageObjective.getElement().setAttribute("title",languageObjective);
			}
		}else{
				languageObjectiveContainer.setVisible(false);
		}
	}
	public void renderDepthOfKnowledge(List<checkboxSelectedDo> depthofKnowledgeList ) {
		depthOfKnowledgeContainer.setVisible(false);
		if(depthofKnowledgeList!=null){
			depthOfKnowledgePanel.clear();
			boolean depthofKnowledgeValue = false;
			for(checkboxSelectedDo checkboxSelectedDo : depthofKnowledgeList) {
				if(checkboxSelectedDo.isSelected()){
					depthofKnowledgeValue = true;
					Label depthofKnowledge = new Label(checkboxSelectedDo.getValue());
					depthofKnowledge.addStyleName(playerStyle.depthofKnow());
					depthofKnowledge.getElement().setAttribute("style", "display:table");
					depthOfKnowledgePanel.add(depthofKnowledge);
				}
			}
			if(depthofKnowledgeValue){
				depthOfKnowledgeContainer.setVisible(true);
			}else{
				depthOfKnowledgeContainer.setVisible(false);
			}
		}else{
			depthOfKnowledgeContainer.setVisible(false);
		}
	}
	public void renderInstructionalMethod(List<checkboxSelectedDo> instructionmethodList){
		InstructionalmethodContainer.setVisible(false);
		if(instructionmethodList!=null){
			instructionalmethodPanel.clear();
			boolean instructionMethod=false;
			for (checkboxSelectedDo checkboxSelectedDo : instructionmethodList) {
				if(checkboxSelectedDo.isSelected()){
					instructionMethod = true;
					Label lblInstructionMethod = new Label(checkboxSelectedDo.getValue());
					instructionalmethodPanel.add(lblInstructionMethod);
					InstructionalmethodContainer.setVisible(true);
				}
			}
			if(instructionMethod){
				InstructionalmethodContainer.setVisible(true);
			}else{
				InstructionalmethodContainer.setVisible(false);
			}
		}else{
				InstructionalmethodContainer.setVisible(false);
		}
	}
	public void renderAudience(List<checkboxSelectedDo> audienceList){
		audienceContainer.setVisible(false);
		if(audienceList!=null){
			audiencePanel.clear();
			boolean audience=false;
			for (checkboxSelectedDo checkboxSelectedDo : audienceList) {
				if(checkboxSelectedDo.isSelected()){
					audience = true;
					Label lblaudience = new Label(checkboxSelectedDo.getValue());
					audiencePanel.add(lblaudience);
				}
			}
			if(audience){
				audienceContainer.setVisible(true);
			}
			else{
				audienceContainer.setVisible(false);
			}
		}
		else{
			audienceContainer.setVisible(false);
		}
	}
	public void renderLearningAndInnovationSkill(List<checkboxSelectedDo> learningSkillsList){
		learningAndInnovationSkillsContainer.setVisible(false);
		if(learningSkillsList!=null){
			learningAndInnovationSkillPanel.clear();
			boolean learningAndInnovationSkill = false;
			Label lbllearningSkills = null;
			for (checkboxSelectedDo checkboxSelectedDo : learningSkillsList) {
				if(checkboxSelectedDo.isSelected()){
					learningAndInnovationSkill = true;
					lbllearningSkills = new Label(checkboxSelectedDo.getValue());
					lbllearningSkills.addStyleName(playerStyle.depthofKnow());
					lbllearningSkills.getElement().setAttribute("style", "display:table");
					learningAndInnovationSkillPanel.add(lbllearningSkills);
				}
			}
			if(learningAndInnovationSkill){
				learningAndInnovationSkillsContainer.setVisible(true);
			}else{
				learningAndInnovationSkillsContainer.setVisible(false);
			}
		}else{
			learningAndInnovationSkillsContainer.setVisible(false);
		}
	}
	@UiHandler("seeMoreAnchor")
	public void clickSeeAll(ClickEvent event)
	{
		//lbllanguageObjectiveAll.setVisible(true);
		seeMoreAnchor.setVisible(false);
		lbllanguageObjective.setText("");
		lbllanguageObjective.setText(languageObjectiveValue);
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
	 * @function hideorShowEditButtonForAllCommentWidgets 
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
	private void hideorShowEditButtonForAllCommentWidgets(Boolean boolFlag) {
		Iterator<Widget> widgets = commentsContainer.iterator();
		while (widgets.hasNext()) {
			Widget widget = widgets.next();
			if (widget instanceof CommentWidgetChildView) {
				CommentWidgetChildView commentWidgetChildView = ((CommentWidgetChildView) widget);
				if(boolFlag)
				{
					commentWidgetChildView.getEditButton().setVisible(true);
				}
				else
				{
					commentWidgetChildView.getEditButton().setVisible(false);	
				}
			
			}
		}
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
			lblCharLimitComments.setVisible(false);
			editComment(commentUid);
		} else if(commentUid.isEmpty() && action.equals(EDIT)) {
			lblCharLimitComments.setVisible(true);
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
	
	@UiHandler("changeAssignmentStatusButton")
	public void clickOnStatusChangeBtn(ClickEvent event) {
		if (changeAssignmentStatusButton.isChecked()){
		getUiHandlers().updateCommentsStatus("turn-on");
		}
		else{
		getUiHandlers().updateCommentsStatus("turn-off");
		}
	}
	
	public void changeCommentsButton(CollectionDo result) {

					
					if(result.getSettings()!=null)
					{
						if(result.getSettings().getComment()!=null)
						{
					
							if(result.getSettings().getComment().equalsIgnoreCase("turn-on"))
							{
								hideorShowEditButtonForAllCommentWidgets(true);
								requiredLabel.removeStyleName(playerStyle.mutedText());
								optionalLabel.removeStyleName(playerStyle.mutedText());								
								commentField.setEnabled(true);	
								postCommentBtn.setEnabled(true);
								postCommentBtn.setStyleName(PRIMARY_STYLE);
								commentssection.getElement().getStyle().setOpacity(1);
								changeAssignmentStatusButton.setChecked(true);
							}
							else
							{
								hideorShowEditButtonForAllCommentWidgets(false);
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
							hideorShowEditButtonForAllCommentWidgets(true);
							requiredLabel.setStyleName(playerStyle.mutedText());
							optionalLabel.setStyleName(playerStyle.mutedText());
						}
					}
					else
					{
						hideorShowEditButtonForAllCommentWidgets(true);
						requiredLabel.setStyleName(playerStyle.mutedText());
						optionalLabel.setStyleName(playerStyle.mutedText());
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
			if(commentField.getText().length()>500) {
				commentField.setText(commentField.getText().substring(0,500));
				commentField.getElement().setAttribute("alt",commentField.getText().substring(0,500));
				commentField.getElement().setAttribute("title",commentField.getText().substring(0,500));
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
	/**
	 * @function removeHtmlTags 
	 * 
	 * @created_date : 16-Oct-2014
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

	@Override
	public void hideTeachPanelDetails(boolean isDisplayDetails) {
		teacherContainer.setVisible(isDisplayDetails);
		hideCollectionDetails(false);
	}

	public ResourceServiceAsync getResourceService() {
		return resourceService;
	}

	public void setResourceService(ResourceServiceAsync resourceService) {
		this.resourceService = resourceService;
	}
	
	
}
