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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.htmltags.AsideTag;
import org.ednovo.gooru.client.mvp.classpages.tabitem.assignments.collections.CollectionsView;
import org.ednovo.gooru.client.mvp.play.collection.preview.PreviewPlayerPresenter;
import org.ednovo.gooru.client.mvp.search.SearchUiUtil;
import org.ednovo.gooru.client.uc.CollaboratorsUc;
import org.ednovo.gooru.client.uc.DownToolTipWidgetUc;
import org.ednovo.gooru.client.uc.PlayerBundle;
import org.ednovo.gooru.client.uc.StandardSgItemVc;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.content.AssignmentParentDo;
import org.ednovo.gooru.shared.model.content.CollectionDo;
import org.ednovo.gooru.shared.model.content.StandardFo;
import org.ednovo.gooru.shared.model.content.checkboxSelectedDo;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

public class MetadataWidget extends Composite {
	
	@UiField FlowPanel rightPanelContainer,teamContainer,courseTitle,standardsContainer;
	@UiField HTMLPanel teacherContainer,classInfoPanel,teacherProfileContainer,depthOfKnowledgePanel,learningAndInnovationSkillPanel,
	                    audiencePanel,instructionalmethodPanel;
	@UiField Anchor  seeMoreAnchor,previewFlagButton;
	@UiField Image profileThumbnailImage;
	@UiField HTML teacherTipLabel;
	@UiField AsideTag teacherPanel,dueDateSection,directionSection,authorPanel,courseSection,standardSection,viewSection,languageObjectiveContainer,
	                  depthOfKnowledgeContainer,learningAndInnovationSkillsContainer,audienceContainer,InstructionalmethodContainer;
	@UiField Label lblClassInfo,lblclassTitle,classTitleValue,lblunitTitle,unitTitleValue,lblTeacher,teacherNameLabel,lbldueDate,dueDate,
	               lblDirectionsDesc,lblDirections,lblAuthor,userNameLabel,lblCourse,lblStandards,viewsCountLabel,lbllanguageObjectiveText,
	               lbllanguageObjective,lbldepthOfKnowledgeText,lbllearningAndInnovationText,lblAudienceText,lblInstructionalmethodText;
	@UiField CollectionPlayerStyleBundle playerStyle;
	
	private CollectionDo collectionDo=null;
	
	private Anchor usernameAnchor=null;
	
	private String languageObjectiveValue=null;
	
    public static final String STANDARD_CODE = "code";
	
	public static final String STANDARD_DESCRIPTION = "description";
	
	private MessageProperties i18n = GWT.create(MessageProperties.class);
	
	private static MetadataWidgetUiBinder uiBinder = GWT.create(MetadataWidgetUiBinder.class);

	interface MetadataWidgetUiBinder extends UiBinder<Widget, MetadataWidget> {
	}

	public MetadataWidget(){
		initWidget(uiBinder.createAndBindUi(this));
		setLabelAndIds();
		teacherContainer.setVisible(false);
	}
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
				authorPanel.getElement().getStyle().setHeight(130, Unit.PX);
			}
			else{
				authorPanel.getElement().getStyle().setHeight(100, Unit.PX);
			}
			teacherTipLabel.setHTML(""+collectionDo.getKeyPoints()+"");		
			teacherTipLabel.getElement().setAttribute("alt",""+collectionDo.getKeyPoints()+"");
			teacherTipLabel.getElement().setAttribute("title",""+collectionDo.getKeyPoints()+"");
		}
		//setLeftPanelHeight();
		setTeacherName(collectionDo.getUser().getUsernameDisplay(), collectionDo.getUser().getProfileImageUrl());
		//placeCollectionMetadataContainer();
	}
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
	public void setViewCount(String viewCount){
		String viewsText=Integer.parseInt(viewCount)==1?viewCount+" "+i18n.GL1428():viewCount+" "+i18n.GL0934();
		viewsCountLabel.setText(viewsText);
		viewsCountLabel.getElement().setAttribute("alt",viewsText);
		viewsCountLabel.getElement().setAttribute("title",viewsText);
	}
	public void setUserProfileImage(String profileUserId){
		profileThumbnailImage.setUrl(AppClientFactory.loggedInUser.getSettings().getProfileImageUrl()+profileUserId+".png?p="+Math.random());
	}
	@UiHandler("profileThumbnailImage")
	public void setDefaultProfileImage(ErrorEvent event){
		profileThumbnailImage.setUrl("images/settings/setting-user-image.png");
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
	public void clickSeeAll(ClickEvent event){
		//lbllanguageObjectiveAll.setVisible(true);
		seeMoreAnchor.setVisible(false);
		lbllanguageObjective.setText("");
		lbllanguageObjective.setText(languageObjectiveValue);
	}
	public void setTeacherName(String userNameDisplay,String profileImageUrl){
		teacherNameLabel.setText(userNameDisplay);
		teacherNameLabel.getElement().setAttribute("alt",userNameDisplay);
		teacherNameLabel.getElement().setAttribute("title",userNameDisplay);
		teacherProfileContainer.clear();
		//teacherProfileThumbnailImage.setUrl(classpageItemDo.getProfileImageUrl()+"?p="+Math.random()); 
		teacherProfileContainer.add(new TeacherImage(profileImageUrl+"?p="+Math.random()));
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
	
	public static String convertMillisecondsToDate(Long milliseconds){
		Date currentDate = new Date(milliseconds);
		DateTimeFormat fmt = DateTimeFormat.getFormat ("MM/dd/yyyy");
		String date=fmt.format(currentDate);
		return date;
	}
	
	public void setDueDateText(Long date){
		
		if(date!=null&&!date.equals("")){
			String text=CollectionsView.convertMillisecondsToDate(date);
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

	public void setLabelAndIds(){
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
		previewFlagButton.removeStyleName(PlayerBundle.INSTANCE.getPlayerStyle().previewCoverFlagImageOrange());
		previewFlagButton.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().playerPreviewCoverFlagImage());
		lbllanguageObjectiveText.setText(i18n.GL1721());
		lbllanguageObjectiveText.getElement().setId("lblLanguageObjectiveText");
		lbllanguageObjectiveText.getElement().setAttribute("alt",i18n.GL1721());
		lbllanguageObjectiveText.getElement().setAttribute("title",i18n.GL1721());
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
		depthOfKnowledgeContainer.getElement().setId("pnlDepthOfKnowledgeContainer");
		depthOfKnowledgePanel.getElement().setId("pnlDepthOfKnowledgePanel");
		learningAndInnovationSkillsContainer.getElement().setId("pnlLearningAndInnovationSkillsContainer");
		learningAndInnovationSkillPanel.getElement().setId("pnlLearningAndInnovationSkillPanel");
		audienceContainer.getElement().setId("pnlAudienceContainer");
		audiencePanel.getElement().setId("pnlAudiencePanel");
		InstructionalmethodContainer.getElement().setId("pnlInstructionalmethodContainer");
		instructionalmethodPanel.getElement().setId("pnlInstructionalmethodPanel");


	}
	public void resetMetadataFields(){
		userNameLabel.setText("");
		userNameLabel.getElement().setAttribute("alt","");
		userNameLabel.getElement().setAttribute("title","");
		viewsCountLabel.setText("");
		viewsCountLabel.getElement().setAttribute("alt","");
		viewsCountLabel.getElement().setAttribute("title","");
		getFlagButton().setText(i18n.GL0556());
		getFlagButton().removeStyleName(PlayerBundle.INSTANCE.getPlayerStyle().previewCoverFlagImageOrange());
		getFlagButton().setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().playerPreviewCoverFlagImage());
		this.collectionDo=null;
	}
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
	
	public void displayAuthorDetails(boolean isDisplayDetails) {
		authorPanel.setVisible(isDisplayDetails);
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
	
	public void setTeacherInfo(AssignmentParentDo assignmentParentDo) {
		hideCollectionDetails(true);
		teacherContainer.setVisible(true);
		String classTitle=assignmentParentDo.getClassTitle()!=null?assignmentParentDo.getClassTitle():"";
		classTitleValue.setText(classTitle);
		classTitleValue.getElement().setAttribute("alt",classTitle);
		classTitleValue.getElement().setAttribute("title",classTitle);
		
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
		
		lblunitTitle.setText(i18n.GL2249());
		String unitTitle=assignmentParentDo.getPathwayTitle()!=null?assignmentParentDo.getPathwayTitle():"";
		unitTitleValue.setText(unitTitle);
		setDueDateText(assignmentParentDo.getPlannedEndDate());
		setDirectionText(assignmentParentDo.getNarration());
	
	}
	
	public void hideCollectionDetails(boolean hide){
		authorPanel.setVisible(!hide);
		courseSection.setVisible(!hide);
		standardSection.setVisible(!hide);
		viewSection.setVisible(!hide);
	}




}
