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
package org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.add;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.effects.FadeInAndOut;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.faq.CopyRightPolicyVc;
import org.ednovo.gooru.client.mvp.faq.TermsAndPolicyVc;
import org.ednovo.gooru.client.mvp.faq.TermsOfUse;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderZIndexEvent;
import org.ednovo.gooru.client.mvp.shelf.collection.CollectionCBundle;
import org.ednovo.gooru.client.uc.AppMultiWordSuggestOracle;
import org.ednovo.gooru.client.uc.AppSuggestBox;
import org.ednovo.gooru.client.uc.BlueButtonUc;
import org.ednovo.gooru.client.uc.CloseLabel;
import org.ednovo.gooru.client.uc.DownToolTipWidgetUc;
import org.ednovo.gooru.client.uc.HTMLEventPanel;
import org.ednovo.gooru.client.uc.RemoveToolTipUc;
import org.ednovo.gooru.client.uc.StandardsPreferenceOrganizeToolTip;
import org.ednovo.gooru.client.uc.tooltip.BrowseStandardsTooltip;
import org.ednovo.gooru.client.uc.tooltip.ToolTip;
import org.ednovo.gooru.client.ui.TinyMCE;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.client.util.SetStyleForProfanity;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.code.CodeDo;
import org.ednovo.gooru.shared.model.content.CollectionItemDo;
import org.ednovo.gooru.shared.model.content.CollectionQuestionItemDo;
import org.ednovo.gooru.shared.model.content.ProfanityCheckDo;
import org.ednovo.gooru.shared.model.content.QuestionAnswerDo;
import org.ednovo.gooru.shared.model.content.QuestionHintsDo;
import org.ednovo.gooru.shared.model.content.checkboxSelectedDo;
import org.ednovo.gooru.shared.model.search.SearchDo;
import org.ednovo.gooru.shared.model.user.ProfileDo;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.Widget;

public abstract class AddQuestionResourceView extends Composite implements SelectionHandler<SuggestOracle.Suggestion>{
	
	public interface AddQuestionResourceViewUiBinder extends UiBinder<Widget, AddQuestionResourceView>{
		
	}
	
	public static AddQuestionResourceViewUiBinder uiBinder=GWT.create(AddQuestionResourceViewUiBinder.class);
	
	private static MessageProperties i18n = GWT.create(MessageProperties.class);
	
	private CollectionItemDo collectionItemDo=null;
	 boolean isAnsweEmpty = false;
	@UiField Label depthOfKnowledgeHeader,standardMaxMsg,standardsDefaultText,errorMessageForAnsCheck,errorMessageForHintsCheck,errorMessageForExplanation,addResourceFormTitleChoice,ansChoiceErrMsg; 
	@UiField HTMLEventPanel addQuestionResourceButton,lblContentRights;
	@UiField HTMLPanel questionAnswerChoiceContainer,questionTrueOrFalseAnswerChoiceContainer;
	@UiField public static Label errorMessageForQuestion;
	@UiField Label questionTypeHeader,questionTypeText,loadingTextLbl,rightsLbl,explanationLabel,questionNameErrorLbl,explainationErrorLbl,depthOfKnowledgeTitle;
	@UiField Anchor addAnswerChoice,addHintsLabel;

	@UiField Anchor addQuestionImg;

/*	@UiField HTMLPanel educationalUsePanel,educationalTitle,homeworkText,gameText,presentationText,referenceMaterialText,quizText,curriculumPlanText,lessonPlanText,
	unitPlanText,projectPlanText,readingText,textbookText,articleText,bookText,activityText,handoutText;
*/	
	@UiField HTMLPanel hintsContainer,buttonContainer,questionText,correctText,noLabelText;

	@UiField HTMLPanel addQuestImgContainer,panelContentRights,rightsContent,errorContainer;
	/*@UiField ListBox questionTypeTextBox;*/
	@UiField BlueButtonUc addbutton;
	/*@UiField TextArea explainationTextArea;*/
	@UiField TinyMCE questionNameTextArea,explainationTextArea;
	@UiField FlowPanel standardContainer,answerchoiceTitleContainer,explanationContainer;
	
	/*@UiField Button questionNameTextAreaToolBarButton;*/
	@UiField Button cancelButton,browseStandards;
	@UiField
	CheckBox chkLevelRecall,chkLevelSkillConcept,chkLevelStrategicThinking,chkLevelExtendedThinking,rightsChkBox;
	
	@UiField AddQuestionAnswerChoice alphaLetterA,alphaLetterB;
	private CopyRightPolicyVc copyRightPolicy;
	
	@UiField Image depthOfKnoweldgeToolTip;
	
	@UiField InlineLabel agreeText,andText,additionalText,commuGuideLinesAnr, termsAndPolicyAnr,privacyAnr,copyRightAnr;
	
	@UiField Label charLimitLbl,charLimitExplanation;
	
	ToolTip toolTip=null;
	private TermsAndPolicyVc termsAndPolicyVc;
	private TermsOfUse termsOfUse;
	
	boolean isSaveButtonClicked=false,isAddBtnClicked=true,isRightsClicked=false,educationalDropDownLblOpen=false;
	private String questionType="MC";
	ArrayList<checkboxSelectedDo> depthOfKnowledges= new ArrayList<checkboxSelectedDo>();
	ArrayList<CodeDo> standardsDo=new ArrayList<CodeDo>();
	Set<CodeDo> deletedStandardsDo=new HashSet<CodeDo>();
	private static final String USER_META_ACTIVE_FLAG = "0";
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
		if(questionType.equals("MC")){
			removeSelectedOption(questionAnswerChoiceContainer, null);
		}
	}
	@UiField AddResourceBundle addWebResourceStyle;
	
	
		
	RemoveToolTipUc removeToolTip=null;   
	boolean validationValue=false, isAnswerChoiceSelected=false;
	private static final String ERROR_MSG_QUESTION = i18n.GL0310();
    private static final String ERROR_MSG_ANSWER = i18n.GL0311();
    private static final String ERROR_MSG_ANSWER_SELECTED =i18n.GL0312(); 
    private static final String ERROR_MSG_ATLEAST_SELECTED =i18n.GL0876(); 
    private static final String ERROR_MSG_HINTS_LENGTH = i18n.GL0877();
	private static final String ERROR_MSG_ANSWER_LENGTH =i18n.GL0878();
	private static final String ERROR_MSG_EXPLAINATION_LENGTH =i18n.GL0879();
	private static final String ERROR_MSG_QUESTION_LENGTH =i18n.GL0880();
	private static final String ERROR_MSG_CHAR_LIMIT=i18n.GL0143();
	private static final String ERROR_MSG_HINTS = i18n.GL2201();
	
	private static final String ERROR_MSG_FIB_BALANCED=i18n.GL0881();
	private static final String ERROR_MSG_FIB_BLANKS=i18n.GL0882();
	private static final String ERROR_MSG_MULTIPLE_FIB=i18n.GL0883();
	private static final String ERROR_MSG_MARK_AS_BLANK = i18n.GL0884();
	private static final String REGX_PATTERN = "\\[(.*?)\\]";
	private static final String FIB_SEPARATOR = i18n.GL0885();
	
	private static final int ANSWER_CHOICE_HINTS_TEXT_LENGTH =150;
	private static final int QUESTION_TEXT_LENGTH =500;
	private static final int EXPLAINATION_TEXT_LENGTH =500;
	
	private List<Widget> answerChoicesList=new ArrayList<Widget>();
	
	@UiField(provided = true)
	AppSuggestBox standardSgstBox;
	
	@UiField FlowPanel standardsPanel;
	private AppMultiWordSuggestOracle standardSuggestOracle;
	private SearchDo<CodeDo> standardSearchDo = new SearchDo<CodeDo>();
	private static final String FLT_CODE_ID = "id";
	List<String> standardPreflist;
	private Map<String, String> standardCodesMap = new HashMap<String, String>();
	String courseCode="";
	boolean isEditResource=false;
	
	String[] anserChoiceArray=new String[]{"A","B","C","D","E"};
	List<ProfanityCheckDo> profanityList,hintsListForProfanity;
	private boolean isBrowseTooltip =false;
	
	BrowseStandardsTooltip browseStandardsTooltip;
	private boolean isBrowseStandardsToolTip = false;
	
	final StandardsPreferenceOrganizeToolTip standardsPreferenceOrganizeToolTip=new StandardsPreferenceOrganizeToolTip();
	
	public AddQuestionResourceView(){
		initializeAutoSuggestedBox();
		initWidget(uiBinder.createAndBindUi(this));
		setHeaderAndBodyText("MC");
		isEditResource=false;
		questionTypeHeader.getElement().setId("lblQuestionTypeHeader");
		questionTypeText.getElement().setId("lblQuestionTypeText");
		questionText.getElement().setId("pnlQuestionText");
		questionText.getElement().setInnerHTML(" "+i18n.GL0863());
		questionText.getElement().setAttribute("alt", i18n.GL0863());
		questionText.getElement().setAttribute("title", i18n.GL0863());
		questionNameErrorLbl.getElement().setId("errlblQuestionNameErrorLbl");
		addQuestionImg.setText(i18n.GL_SPL_PLUS()+" "+i18n.GL0860());
		addQuestionImg.getElement().setAttribute("alt", i18n.GL0860());
		addQuestionImg.getElement().setAttribute("title", i18n.GL0860());
		addResourceFormTitleChoice.getElement().setId("lblAddResourceFormTitleChoice");
		addResourceFormTitleChoice.getElement().setAttribute("alt", i18n.GL0864());
		addResourceFormTitleChoice.getElement().setAttribute("title", i18n.GL0864());
		addResourceFormTitleChoice.setText(i18n.GL0864());
		correctText.getElement().setId("pnlCorrectText");
		noLabelText.getElement().setId("pnlNoLabelText");
		correctText.getElement().setInnerHTML(i18n.GL0314());
		correctText.getElement().setAttribute("alt", i18n.GL0314());
		correctText.getElement().setAttribute("title", i18n.GL0314());
		noLabelText.setVisible(false);
		alphaLetterA.getElement().setId("qacAlphaLetterA");
		alphaLetterA.getElement().setAttribute("alt", i18n.GL_GRR_ALPHABET_A());
		alphaLetterA.getElement().setAttribute("title", i18n.GL_GRR_ALPHABET_A());
		alphaLetterA.setLabelName(i18n.GL_GRR_ALPHABET_A());
		alphaLetterB.setLabelName(i18n.GL_GRR_ALPHABET_B());
		alphaLetterB.getElement().setId("qacAlphaLetterB");
		alphaLetterB.getElement().setAttribute("alt", i18n.GL_GRR_ALPHABET_B());
		alphaLetterB.getElement().setAttribute("title", i18n.GL_GRR_ALPHABET_B());
		addAnswerChoice.setText(i18n.GL0866());
		addAnswerChoice.getElement().setAttribute("alt", i18n.GL0866());
		addAnswerChoice.getElement().setAttribute("title", i18n.GL0866());
		explanationLabel.setText(" "+i18n.GL0867());
		explanationLabel.getElement().setId("lblExplanationLabel");
		explanationLabel.getElement().setAttribute("alt", i18n.GL0867());
		explanationLabel.getElement().setAttribute("title", i18n.GL0867());
		explanationLabel.getElement().getStyle().setDisplay(Display.INLINE);
		addHintsLabel.setText(i18n.GL0868());
		addHintsLabel.getElement().setAttribute("alt", i18n.GL0868());
		addHintsLabel.getElement().setAttribute("title", i18n.GL0868());
		rightsLbl.setText(i18n.GL0869());
		rightsLbl.getElement().setId("lblRightsLbl");
		rightsLbl.getElement().setAttribute("alt", i18n.GL0869());
		rightsLbl.getElement().setAttribute("title", i18n.GL0869());
		agreeText.setText(i18n.GL0870());
		agreeText.getElement().setId("lblAgreeText");
		agreeText.getElement().setAttribute("alt", i18n.GL0870());
		agreeText.getElement().setAttribute("title", i18n.GL0870());
		commuGuideLinesAnr.setText(i18n.GL0871()+i18n.GL_GRR_COMMA());
		commuGuideLinesAnr.getElement().setId("lnkCommuGuideLinesAnr");
		commuGuideLinesAnr.getElement().setAttribute("alt", i18n.GL0871());
		commuGuideLinesAnr.getElement().setAttribute("title", i18n.GL0871());
		termsAndPolicyAnr.setText(i18n.GL0872()+i18n.GL_GRR_COMMA());
		termsAndPolicyAnr.getElement().setId("lnkTermsAndPolicyAnr");
		termsAndPolicyAnr.getElement().setAttribute("alt", i18n.GL0872());
		termsAndPolicyAnr.getElement().setAttribute("title", i18n.GL0872());
		privacyAnr.setText(" "+i18n.GL0873());
		privacyAnr.getElement().setId("lnkPrivacyAnr");
		privacyAnr.getElement().setAttribute("alt", i18n.GL0873());
		privacyAnr.getElement().setAttribute("title", i18n.GL0873());
		andText.setText(" "+i18n.GL_GRR_AND().trim()+" ");
		andText.getElement().setId("lblAndText");
		andText.getElement().setAttribute("alt", i18n.GL_GRR_AND());
		andText.getElement().setAttribute("title", i18n.GL_GRR_AND());
		copyRightAnr.setText(" "+i18n.GL0875().trim());
		copyRightAnr.getElement().setId("lnkCopyRightAnr");
		copyRightAnr.getElement().setAttribute("alt", i18n.GL0875());
		copyRightAnr.getElement().setAttribute("title", i18n.GL0875());
		additionalText.setText(i18n.GL0874());
		additionalText.getElement().setId("lblAdditionalText");

		additionalText.getElement().setAttribute("alt", i18n.GL0874());
		additionalText.getElement().setAttribute("title", i18n.GL0874());
		cancelButton.setText(i18n.GL0142());
		cancelButton.getElement().setAttribute("alt", i18n.GL0142());
		cancelButton.getElement().setAttribute("title", i18n.GL0142());
		cancelButton.getElement().setAttribute("style", "margin-left:10px");
		addbutton.setText(i18n.GL0590());
		addbutton.getElement().setAttribute("alt", i18n.GL0590());
		addbutton.getElement().setAttribute("title", i18n.GL0590());
		loadingTextLbl.setText(i18n.GL0591().toLowerCase());

		loadingTextLbl.getElement().setId("lblLoadingTextLbl");
		loadingTextLbl.getElement().setAttribute("alt", i18n.GL0591().toLowerCase());
		loadingTextLbl.getElement().setAttribute("title", i18n.GL0591().toLowerCase());
		questionNameTextArea.getElement().setId("tinyMCEQuestionNameTextArea");
		questionNameTextArea.getElement().setAttribute("maxlength", "500");
		explainationTextArea.getElement().setAttribute("maxlength", "400");
		explainationTextArea.getElement().setId("tinyMCEExplainationTextArea");
		addbutton.getElement().setId("btnAdd");
		cancelButton.getElement().setId("btnCancel");
		addQuestionImg.getElement().setId("lnkAddQuestionImg");
		addAnswerChoice.getElement().setId("lnkAnswerChoice");
		addHintsLabel.getElement().setId("lnkHints");
		panelContentRights.setVisible(false);
		panelContentRights.getElement().setId("pnlPanelContentRights");
		questionNameTextArea.markAsBlankPanel.setVisible(false);
		loadingTextLbl.getElement().getStyle().setFontStyle(FontStyle.ITALIC);
		buttonContainer.getElement().setId("pnlButtonContainer");
	    buttonContainer.getElement().getStyle().setDisplay(Display.BLOCK);
		loadingTextLbl.setVisible(false);
		rightsChkBox.addClickHandler(new rightsChecked());
		rightsChkBox.getElement().setId("chkRights");
		errorMessageForQuestion.getElement().setId("errlblErrorMessageForQuestion");
		addQuestImgContainer.getElement().setId("pnlAddQuestImgContainer");
		answerchoiceTitleContainer.getElement().setId("fpnlAnswerchoiceTitleContainer");
		questionAnswerChoiceContainer.getElement().setId("pnlQuestionAnswerChoiceContainer");
		questionTrueOrFalseAnswerChoiceContainer.getElement().setId("pnlQuestionTrueOrFalseAnswerChoiceContainer");
		errorMessageForAnsCheck.getElement().setId("lblErrorMessageForAnsCheck");
		ansChoiceErrMsg.getElement().setId("lblAnsChoiceErrMsg");
		explanationContainer.getElement().setId("fpnlExplanationContainer");
		explainationErrorLbl.getElement().setId("errlblExplainationErrorLbl");
		hintsContainer.getElement().setId("pnlHintsContainer");
		errorMessageForHintsCheck.getElement().setId("errlblErrorMessageForHintsCheck");
		depthOfKnowledgeHeader.getElement().setId("lblDepthOfKnowledgeHeader");
		depthOfKnoweldgeToolTip.getElement().setId("imgDepthOfKnoweldgeToolTip");
		depthOfKnowledgeTitle.getElement().setId("lblDepthOfKnowledgeTitle");
		chkLevelRecall.getElement().setId("chkChkLevelRecall");
		chkLevelSkillConcept.getElement().setId("chkChkLevelSkillConcept");
		chkLevelStrategicThinking.getElement().setId("chkChkLevelStrategicThinking");
		chkLevelExtendedThinking.getElement().setId("chkChkLevelExtendedThinking");
		standardContainer.getElement().setId("fpnlStandardContainer");
		standardsDefaultText.getElement().setId("lblStandardsDefaultText");
		standardSgstBox.getElement().setId("appSuggestBoxStandardSgstBox");
		standardMaxMsg.getElement().setId("lblStandardMaxMsg");
		standardsPanel.getElement().setId("fpnlStandardsPanel");
		lblContentRights.getElement().setId("epnlLblContentRights");
		rightsContent.getElement().setId("pnlRightsContent");
		addQuestionResourceButton.getElement().setId("epnlAddQuestionResourceButton");
		browseStandards.addClickHandler(new onBrowseStandardsClick());
		setTextForTheFields();
		errorContainer.setVisible(false);
		errorContainer.add(standardsPreferenceOrganizeToolTip);
		alphaLetterA.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				if(alphaLetterA.getParent().getElement().getChildNodes().getLength()>3)
				{
				alphaLetterA.ansChoiceDeleteButton.getElement().getStyle().setDisplay(Display.BLOCK);
				}
				else
				{
				alphaLetterA.ansChoiceDeleteButton.getElement().getStyle().setDisplay(Display.NONE);
				}
			}
		});
		alphaLetterA.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				alphaLetterA.ansChoiceDeleteButton.getElement().getStyle().setDisplay(Display.NONE);
			}
		});
		showRemoveToolTip(alphaLetterA.ansChoiceDeleteButton);	
		alphaLetterA.ansChoiceDeleteButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(alphaLetterA.getParent().getElement().getChildNodes().getLength()>3)
				{
				alphaLetterA.removeFromParent();
				refreshOptionNames();
				removeToolTip.hide();
				}
			}
		});
		alphaLetterB.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				if(alphaLetterB.getParent().getElement().getChildNodes().getLength()>3)
				{
				alphaLetterB.ansChoiceDeleteButton.getElement().getStyle().setDisplay(Display.BLOCK);
				}
				else
				{
				alphaLetterB.ansChoiceDeleteButton.getElement().getStyle().setDisplay(Display.NONE);
				}
			}
		});
		alphaLetterB.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				alphaLetterB.ansChoiceDeleteButton.getElement().getStyle().setDisplay(Display.NONE);
			}
		});
		showRemoveToolTip(alphaLetterB.ansChoiceDeleteButton);	
		alphaLetterB.ansChoiceDeleteButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(alphaLetterB.getParent().getElement().getChildNodes().getLength()>3)
				{
				alphaLetterB.removeFromParent();
				refreshOptionNames();
				removeToolTip.hide();
				}
			}
		});
		//questionTypeTextBox.getElement().setId("lbQuestion Type*");
		for(int i=0;i<questionAnswerChoiceContainer.getWidgetCount();i++){
			final AddQuestionAnswerChoice addQuestionAnswerChoice=(AddQuestionAnswerChoice)questionAnswerChoiceContainer.getWidget(i);
			addQuestionAnswerChoice.optionSelectedButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if(questionType.equals("MA")){
						selectYesOption(questionAnswerChoiceContainer, addQuestionAnswerChoice);
					}else{
						selectOrDeselectOption(questionAnswerChoiceContainer,addQuestionAnswerChoice);
					}
				}
			});
			addQuestionAnswerChoice.optionNoButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if(questionType.equals("MA")){
						selectNoOption(questionAnswerChoiceContainer, addQuestionAnswerChoice);
					}
				}
			});
		}
		setTrueOrFalseFields();
		//questionNameTextAreaToolBarButton.addClickHandler(new ShowTinyMceToolBar(questionNameTextArea));
	}
	public AddQuestionResourceView(CollectionItemDo collectionItemDo){
		initializeAutoSuggestedBox();
		initWidget(uiBinder.createAndBindUi(this));
		this.collectionItemDo=collectionItemDo;
		isEditResource=true;
		questionTypeHeader.getElement().setId("lblQuestionTypeHeader");
		questionTypeText.getElement().setId("lblQuestionTypeText");
		questionNameErrorLbl.getElement().setId("errlblQuestionNameErrorLbl");
		questionNameTextArea.getElement().setId("tinyMCEQuestionNameTextArea");
		addQuestionImg.getElement().setId("lnkAddQuestionImg");
		addbutton.setText(i18n.GL0590());
		addbutton.getElement().setId("btnAdd");
		addbutton.getElement().setAttribute("alt", i18n.GL0590());
		addbutton.getElement().setAttribute("title", i18n.GL0590());
		cancelButton.getElement().setAttribute("style", "margin-left:10px");
		loadingTextLbl.setVisible(false);
		loadingTextLbl.setText(i18n.GL0808());
		loadingTextLbl.getElement().setId("lblLoadingTextLbl");
		loadingTextLbl.getElement().setAttribute("alt", i18n.GL0808());
		loadingTextLbl.getElement().setAttribute("title", i18n.GL0808());
		rightsLbl.setText(i18n.GL0869());
		rightsLbl.getElement().setId("lblRightsLbl");
		rightsLbl.getElement().setAttribute("alt", i18n.GL0869());
		rightsLbl.getElement().setAttribute("title", i18n.GL0869());
		agreeText.setText(i18n.GL0870());
		agreeText.getElement().setId("lblAgreeText");
		agreeText.getElement().setAttribute("alt", i18n.GL0870());
		agreeText.getElement().setAttribute("title", i18n.GL0870());
		commuGuideLinesAnr.setText(i18n.GL0871()+i18n.GL_GRR_COMMA());
		commuGuideLinesAnr.getElement().setId("lnkCommuGuideLinesAnr");
		commuGuideLinesAnr.getElement().setAttribute("alt", i18n.GL0871());
		commuGuideLinesAnr.getElement().setAttribute("title", i18n.GL0871());
		termsAndPolicyAnr.setText(" "+i18n.GL0872()+i18n.GL_GRR_COMMA());
		termsAndPolicyAnr.getElement().setId("lnkTermsAndPolicyAnr");
		termsAndPolicyAnr.getElement().setAttribute("alt", i18n.GL0872());
		termsAndPolicyAnr.getElement().setAttribute("title", i18n.GL0872());
		privacyAnr.setText(" "+i18n.GL0873());
		privacyAnr.getElement().setId("lnkPrivacyAnr");
		privacyAnr.getElement().setAttribute("alt", i18n.GL0873());
		privacyAnr.getElement().setAttribute("title", i18n.GL0873());
		andText.setText(" "+i18n.GL_GRR_AND()+" ");
		andText.getElement().setId("lblAndText");
		andText.getElement().setAttribute("alt", i18n.GL_GRR_AND());
		andText.getElement().setAttribute("title", i18n.GL_GRR_AND());
		copyRightAnr.setText(" "+i18n.GL0875());
		copyRightAnr.getElement().setId("lnkCopyRightAnr");
		copyRightAnr.getElement().setAttribute("alt", i18n.GL0875());
		copyRightAnr.getElement().setAttribute("title", i18n.GL0875());
		additionalText.setText(i18n.GL0874());
		additionalText.getElement().setId("lblAdditionalText");
		additionalText.getElement().setAttribute("alt", i18n.GL0874());
		additionalText.getElement().setAttribute("title", i18n.GL0874());
		cancelButton.setText(i18n.GL0142());
		cancelButton.getElement().setId("btnCancel");
		cancelButton.getElement().setAttribute("alt", i18n.GL0142());
		cancelButton.getElement().setAttribute("title", i18n.GL0142());
		loadingTextLbl.setText(i18n.GL0808().toLowerCase());
		loadingTextLbl.getElement().setId("lblLoadingTextLbl");
		loadingTextLbl.getElement().setAttribute("alt", i18n.GL0808().toLowerCase());
		loadingTextLbl.getElement().setAttribute("title", i18n.GL0808().toLowerCase());
		explanationLabel.setText(" "+i18n.GL0867());
		explanationLabel.getElement().setId("lblExplanationLabel");
		explanationLabel.getElement().setAttribute("alt", i18n.GL0867());
		explanationLabel.getElement().setAttribute("title", i18n.GL0867());
		explanationLabel.getElement().getStyle().setDisplay(Display.INLINE);
		addAnswerChoice.setText(i18n.GL0866());
		addAnswerChoice.getElement().setAttribute("alt", i18n.GL0866());
		addAnswerChoice.getElement().setAttribute("title", i18n.GL0866());
		addAnswerChoice.getElement().setId("lnkAnswerChoice");
		addHintsLabel.setText(i18n.GL0868());
		addHintsLabel.getElement().setAttribute("alt", i18n.GL0868());
		addHintsLabel.getElement().setAttribute("title", i18n.GL0868());
		addHintsLabel.getElement().setId("lnkHints");
		errorMessageForQuestion.getElement().setId("errlblErrorMessageForQuestion");
		addQuestImgContainer.getElement().setId("pnlAddQuestImgContainer");
		answerchoiceTitleContainer.getElement().setId("fpnlAnswerchoiceTitleContainer");
		addResourceFormTitleChoice.getElement().setId("lblAddResourceFormTitleChoice");
		questionAnswerChoiceContainer.getElement().setId("pnlQuestionAnswerChoiceContainer");
		questionTrueOrFalseAnswerChoiceContainer.getElement().setId("pnlQuestionTrueOrFalseAnswerChoiceContainer");
		errorMessageForAnsCheck.getElement().setId("lblErrorMessageForAnsCheck");
		ansChoiceErrMsg.getElement().setId("lblAnsChoiceErrMsg");
		correctText.getElement().setId("pnlCorrectText");
		noLabelText.getElement().setId("pnlNoLabelText");
		alphaLetterA.getElement().setId("qacAlphaLetterA");
		explanationContainer.getElement().setId("fpnlExplanationContainer");
		explainationTextArea.getElement().setId("tinyMCEExplainationTextArea");
		explainationErrorLbl.getElement().setId("errlblExplainationErrorLbl");
		hintsContainer.getElement().setId("pnlHintsContainer");
		errorMessageForHintsCheck.getElement().setId("errlblErrorMessageForHintsCheck");
		depthOfKnowledgeHeader.getElement().setId("lblDepthOfKnowledgeHeader");
		depthOfKnoweldgeToolTip.getElement().setId("imgDepthOfKnoweldgeToolTip");
		depthOfKnowledgeTitle.getElement().setId("lblDepthOfKnowledgeTitle");
		chkLevelRecall.getElement().setId("chkChkLevelRecall");
		chkLevelSkillConcept.getElement().setId("chkChkLevelSkillConcept");
		chkLevelStrategicThinking.getElement().setId("chkChkLevelStrategicThinking");
		chkLevelExtendedThinking.getElement().setId("chkChkLevelExtendedThinking");
		standardContainer.getElement().setId("fpnlStandardContainer");
		standardsDefaultText.getElement().setId("lblStandardsDefaultText");
		standardSgstBox.getElement().setId("appSuggestBoxStandardSgstBox");
		standardMaxMsg.getElement().setId("lblStandardMaxMsg");
		standardsPanel.getElement().setId("fpnlStandardsPanel");
		lblContentRights.getElement().setId("epnlLblContentRights");
		panelContentRights.getElement().setId("pnlPanelContentRights");
		rightsContent.getElement().setId("pnlRightsContent");
		buttonContainer.getElement().setId("pnlButtonContainer");
		setQuestionTypeStaticTexts();
		rightsChkBox.addClickHandler(new rightsChecked());
		rightsChkBox.getElement().setId("chkRights");
		setTrueOrFalseFields();
		setTextForTheFields();
		errorContainer.setVisible(false);
		errorContainer.add(standardsPreferenceOrganizeToolTip);
		browseStandards.addClickHandler(new onBrowseStandardsClick());
	}
	public void initializeAutoSuggestedBox(){
		standardSuggestOracle = new AppMultiWordSuggestOracle(true);
		standardSearchDo.setPageSize(10);
		standardSgstBox = new AppSuggestBox(standardSuggestOracle) {
			@Override
			public void keyAction(String text) {
				text=text.toUpperCase();
				//standardsPreferenceOrganizeToolTip.hide();
				errorContainer.setVisible(false);
				standardSearchDo.setSearchResults(null);
				boolean standardsPrefDisplayPopup = false;
				standardSgstBox.hideSuggestionList();
				if(!courseCode.isEmpty()) {
					Map<String,String> filters = new HashMap<String, String>();
					filters.put(FLT_CODE_ID,courseCode);
					standardSearchDo.setFilters(filters);
				}
				standardSearchDo.setQuery(text);
				if (text != null && text.trim().length() > 0) {
					errorContainer.setVisible(false);
				//	standardsPreferenceOrganizeToolTip.hide();
					if(standardPreflist!=null){
						for(int count=0; count<standardPreflist.size();count++) {
							if(text.contains(standardPreflist.get(count))) {
								standardsPrefDisplayPopup = true;
								break;
							} else {
								standardsPrefDisplayPopup = false;
							}
						}						
					}
					if(standardsPrefDisplayPopup){
						errorContainer.setVisible(false);
						//standardsPreferenceOrganizeToolTip.hide();
						AppClientFactory.getInjector().getSearchService().getSuggestStandardByFilterCourseId(standardSearchDo, new SimpleAsyncCallback<SearchDo<CodeDo>>() {
							@Override
							public void onSuccess(SearchDo<CodeDo> result) {
								setStandardSuggestions(result);
								
							}
						});
						
						standardSgstBox.showSuggestionList();
						}
					else{
						errorContainer.setVisible(true);
						standardSgstBox.hideSuggestionList();
						standardSuggestOracle.clear();
						/*standardsPreferenceOrganizeToolTip.show();
						standardsPreferenceOrganizeToolTip.setPopupPosition(standardSgstBox.getAbsoluteLeft()+3, standardSgstBox.getAbsoluteTop()+33);
						standardsPreferenceOrganizeToolTip.getElement().getStyle().setZIndex(1111);
					*/	//standardSuggestOracle.add(i18n.GL1613);
					}
					}
			}
			@Override
			public HandlerRegistration addClickHandler(ClickHandler handler) {
				return null;
			}
		};
		standardSgstBox.addSelectionHandler(this);
		BlurHandler blurHandler=new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				if(standardsPreferenceOrganizeToolTip.isShowing()){
					errorContainer.setVisible(false);
				//standardsPreferenceOrganizeToolTip.hide();
				}
			}
		};
		standardSgstBox.addDomHandler(blurHandler, BlurEvent.getType());
	}
	public void setTextForTheFields(){
		/*educationalTitle.getElement().setInnerHTML(i18n.GL1664);
		activityText.getElement().setInnerHTML(i18n.GL1665);
		handoutText.getElement().setInnerHTML(i18n.GL0907);
		homeworkText.getElement().setInnerHTML(i18n.GL1666);
		gameText.getElement().setInnerHTML(i18n.GL1667);
		presentationText.getElement().setInnerHTML(i18n.GL1668);
		referenceMaterialText.getElement().setInnerHTML(i18n.GL1669);
		quizText.getElement().setInnerHTML(i18n.GL1670);
		curriculumPlanText.getElement().setInnerHTML(i18n.GL1671);
		lessonPlanText.getElement().setInnerHTML(i18n.GL1672);
		unitPlanText.getElement().setInnerHTML(i18n.GL1673);
		projectPlanText.getElement().setInnerHTML(i18n.GL1674);
		readingText.getElement().setInnerHTML(i18n.GL1675);
		textbookText.getElement().setInnerHTML(i18n.GL0909);
		articleText.getElement().setInnerHTML(i18n.GL1676);
		bookText.getElement().setInnerHTML(i18n.GL1677);
		educationalUsePanel.setVisible(false);
		resourceEducationalLabel.setText(i18n.GL1684);*/
		standardsDefaultText.setText(i18n.GL1682());
		standardsDefaultText.getElement().setAttribute("alt", i18n.GL1682());
		standardsDefaultText.getElement().setAttribute("title", i18n.GL1682());
		depthOfKnowledgeHeader.setText(i18n.GL1693());
		depthOfKnowledgeHeader.getElement().setAttribute("alt", i18n.GL1693());
		depthOfKnowledgeHeader.getElement().setAttribute("title", i18n.GL1693());
		chkLevelRecall.setText(i18n.GL1645());
		chkLevelRecall.getElement().setAttribute("alt", i18n.GL1645());
		chkLevelRecall.getElement().setAttribute("title", i18n.GL1645());
		chkLevelSkillConcept.setText(i18n.GL1646());
		chkLevelSkillConcept.getElement().setAttribute("alt", i18n.GL1646());
		chkLevelSkillConcept.getElement().setAttribute("title", i18n.GL1646());
		chkLevelStrategicThinking.setText(i18n.GL1647());
		chkLevelStrategicThinking.getElement().setAttribute("alt", i18n.GL1647());
		chkLevelStrategicThinking.getElement().setAttribute("title", i18n.GL1647());
		chkLevelExtendedThinking.setText(i18n.GL1648());
		chkLevelExtendedThinking.getElement().setAttribute("alt", i18n.GL1648());
		chkLevelExtendedThinking.getElement().setAttribute("title", i18n.GL1648());
		depthOfKnoweldgeToolTip.setUrl("images/mos/questionmark.png");
		depthOfKnoweldgeToolTip.setTitle("Question Mark");
		depthOfKnoweldgeToolTip.getElement().setAttribute("alt", "Question Mark");
		depthOfKnoweldgeToolTip.getElement().setAttribute("title", "Question Mark");
		addQuestionResourceButton.getElement().setId("epnlAddQuestionResourceButton");
		
		String value = StringUtil.generateMessage(i18n.GL2103(), "500");
		charLimitLbl.setText(value);
		StringUtil.setAttributes(charLimitLbl.getElement(), "charLimitLbl", value, value);
		
		charLimitExplanation.setText(value);
		StringUtil.setAttributes(charLimitExplanation.getElement(), "charLimitExplanation", value, value);
		
		addClickEventsForCheckBox();
		AppClientFactory.getInjector().getUserService().getUserProfileV2Details(AppClientFactory.getGooruUid(),USER_META_ACTIVE_FLAG,new SimpleAsyncCallback<ProfileDo>() {

			@Override
			public void onSuccess(ProfileDo profileObj) {
			if(profileObj.getUser().getMeta().getTaxonomyPreference().getCodeId()!=null){
					if(profileObj.getUser().getMeta().getTaxonomyPreference().getCodeId().size()==0){
						standardContainer.setVisible(true);
						isBrowseTooltip = true;
						DisableStandars();
					}else
					{
						standardContainer.setVisible(true);
						isBrowseTooltip = false;
						enableStandards();
						standardPreflist=new ArrayList<String>();
						for (String code : profileObj.getUser().getMeta().getTaxonomyPreference().getCode()) {
							standardPreflist.add(code);
							standardPreflist.add(code.substring(0, 2));
						 }
						
					}
				}else{
					standardContainer.setVisible(true);
					isBrowseTooltip = true;
					DisableStandars();
				}
			}

		});
	}
	@Override
	public void onSelection(SelectionEvent<Suggestion> event) {
		addStandard(standardSgstBox.getValue(), getCodeIdByCode(standardSgstBox.getValue(), standardSearchDo.getSearchResults()));
		standardSgstBox.setText("");
		standardSuggestOracle.clear();
	}
	private static String getCodeIdByCode(String code, List<CodeDo> codes) {
		if (codes != null) {
			for (CodeDo codeDo : codes) {
				if (code.equals(codeDo.getCode())) {
					return codeDo.getCodeId() + "";
				}
			}
		}
		return null;
	}
	/**
	 * Adding new standard for the collection , will check it has more than
	 * fifteen standards
	 * 
	 * @param standard
	 *            which to be added for the collection
	 */
	public void addStandard(String standard, String id) {
		if (standardsPanel.getWidgetCount() <5) {
			if (standard != null && !standard.isEmpty()) {
				CodeDo codeObj=new CodeDo();
				codeObj.setCodeId(Integer.parseInt(id));
				codeObj.setCode(standard);
				standardsDo.add(codeObj);
				standardsPanel.add(createStandardLabel(standard, id, standardCodesMap.get(id)));
			}
		} else {
			standardMaxShow();
			standardSgstBox.setText("");
		}
	}
	/**
	 * new label is created for the standard which needs to be added
	 * 
	 * @param standardCode
	 *            update standard code
	 * @return instance of {@link DownToolTipWidgetUc}
	 */
	public DownToolTipWidgetUc createStandardLabel(final String standardCode, final String id, String description) {
		CloseLabel closeLabel = new CloseLabel(standardCode) {

			@Override
			public void onCloseLabelClick(ClickEvent event) {
				for(final CodeDo codeObj:standardsDo){
					if(isEditResource){
					if(codeObj.getCodeId()==Integer.parseInt(id)){
						AppClientFactory.getInjector().getResourceService().deleteTaxonomyResource(collectionItemDo.getResource().getGooruOid(), codeObj.getCodeId(), new SimpleAsyncCallback<Void>() {

							@Override
							public void onSuccess(Void result) {
								CodeDo deletedObj=new CodeDo();
								deletedObj.setCodeId(codeObj.getCodeId());
								deletedStandardsDo.add(deletedObj);
								standardsDo.remove(codeObj);
							}
						});
					}
				}else
				{
					if(codeObj.getCodeId()==Integer.parseInt(id)){
						standardsDo.remove(codeObj);
					}
				}
				}
				this.getParent().removeFromParent();
			}
		};
		return new DownToolTipWidgetUc(closeLabel, description);
	}
	public void standardMaxShow() {
		standardSgstBox.addStyleName(CollectionCBundle.INSTANCE.css().standardTxtBox());
		standardMaxMsg.setStyleName(CollectionCBundle.INSTANCE.css().standardMax());
		standardsPanel.addStyleName(CollectionCBundle.INSTANCE.css().floatLeftNeeded());
		new FadeInAndOut(standardMaxMsg.getElement(), 5000, 5000);
	}
	public void setStandardSuggestions(SearchDo<CodeDo> standardSearchDo) {
		standardSuggestOracle.clear();
		this.standardSearchDo = standardSearchDo;
		if (this.standardSearchDo.getSearchResults() != null) {
			List<String> sources = getAddedStandards(standardsPanel);
			for (CodeDo code : standardSearchDo.getSearchResults()) {
				if (!sources.contains(code.getCode())) {
					standardSuggestOracle.add(code.getCode());
				}
				standardCodesMap.put(code.getCodeId() + "", code.getLabel());
			}
		}
		standardSgstBox.showSuggestionList();
	}
	/**
	 * get the standards are added for collection
	 * 
	 * @param flowPanel
	 *            having all added standards label
	 * @return standards text in list which are added for the collection
	 */
	private List<String> getAddedStandards(FlowPanel flowPanel) {
		List<String> suggestions = new ArrayList<String>();
		for (Widget widget : flowPanel) {
			if (widget instanceof DownToolTipWidgetUc) {
				suggestions.add(((CloseLabel) ((DownToolTipWidgetUc) widget).getWidget()).getSourceText());
			}
		}
		return suggestions;
	}
	/**
	 * Sets the text for type of question selected based on question type number.
	 */
	
	public void setQuestionTypeStaticTexts(){
		int questionTypeNum=collectionItemDo.getResource().getType() !=null ? collectionItemDo.getResource().getType() : collectionItemDo.getQuestionInfo().getType();
		if(questionTypeNum==1){
			setQuestionType("MC");
			showMulipleChoice();
		}else if(questionTypeNum==3){
			setQuestionType("T/F");
			showTrueOrFalseAnswerChoice();
		}else if(questionTypeNum==6){
			setQuestionType("OE");
			showOpenEndedQuestion();
		}else if(questionTypeNum==4){
			setQuestionType("FIB");
			showFillInTheBlank();
		}else if(questionTypeNum==7){
			setQuestionType("MA");
			showMulipleAnswerChoiceOptions();
		}
	}
	public void refreshOptionNames(){
		for(int i=0;i<questionAnswerChoiceContainer.getWidgetCount();i++){
			AddQuestionAnswerChoice addQuestionAnswerChoice=(AddQuestionAnswerChoice)questionAnswerChoiceContainer.getWidget(i);
			addQuestionAnswerChoice.setLabelName(anserChoiceArray[i]);
		}
		if(questionAnswerChoiceContainer.getWidgetCount()<5){
			addAnswerChoice.getElement().getStyle().setDisplay(Display.BLOCK);
		}
	}
	
	public void selectOrDeselectOption(HTMLPanel questionAnswerChoiceContainer,AddQuestionAnswerChoice addQuestionAnswerChoice){

		if(questionType.equals("T/F")||questionType.equals("MC")){
			removeSelectedOption(questionAnswerChoiceContainer,addQuestionAnswerChoice);
		}
		clearErrorMessageForAnswer();
		if(addQuestionAnswerChoice.optionSelectedButton.getStyleName().equals(addWebResourceStyle.answerDeselected())){
			addQuestionAnswerChoice.optionSelectedButton.setStyleName(addWebResourceStyle.answerSelected());
			addStyleToBody(addQuestionAnswerChoice);
			
		}
		else if(addQuestionAnswerChoice.optionSelectedButton.getStyleName().equals(addWebResourceStyle.answerSelected())){
			addQuestionAnswerChoice.optionSelectedButton.setStyleName(addWebResourceStyle.answerDeselected());
			removeStyleToBody(addQuestionAnswerChoice);
		}
	}
	
	public void removeSelectedOption(HTMLPanel questionAnswerChoiceContainer,AddQuestionAnswerChoice selectedAnswerChoice){
		for(int i=0;i<questionAnswerChoiceContainer.getWidgetCount();i++){
			AddQuestionAnswerChoice addQuestionAnswerChoice=(AddQuestionAnswerChoice)questionAnswerChoiceContainer.getWidget(i);
			removeStyleToBody(addQuestionAnswerChoice);
			if(selectedAnswerChoice!=null&&addQuestionAnswerChoice.optionSelectedButton.getStyleName().equals(addWebResourceStyle.answerSelected())
					&& selectedAnswerChoice.optionSelectedButton.getStyleName().equals(addWebResourceStyle.answerSelected())){
				
			}else{
				addQuestionAnswerChoice.optionSelectedButton.setStyleName(addWebResourceStyle.answerDeselected());
			}
		}
	}
	public void selectYesOption(HTMLPanel questionAnswerChoiceContainer,AddQuestionAnswerChoice addQuestionAnswerChoice){

		String styleSelectedVal = "";
		String styleSelectedValcomparer = "";
		
		List<String> items = Arrays.asList(addQuestionAnswerChoice.optionSelectedButton.getStyleName().split("\\s*-\\s*"));
		
		if (!items.isEmpty()) {
			styleSelectedVal = items.get(items.size()-1);
			}
		
		List<String> itemsComparer = Arrays.asList(addWebResourceStyle.answerDeselected().split("\\s*-\\s*"));
		
		if (!itemsComparer.isEmpty()) {
			styleSelectedValcomparer = itemsComparer.get(itemsComparer.size()-1);
			}
		
		if(styleSelectedVal.equals(styleSelectedValcomparer)){
			addQuestionAnswerChoice.optionSelectedButton.setStyleName(addWebResourceStyle.answerSelected());
			addQuestionAnswerChoice.optionNoButton.setStyleName(addWebResourceStyle.answerDeselected());
		}
		
		
		
	}
	public void selectNoOption(HTMLPanel questionAnswerChoiceContainer,AddQuestionAnswerChoice addQuestionAnswerChoice){
			if(addQuestionAnswerChoice.optionNoButton.getStyleName().equals(addWebResourceStyle.answerDeselected())){
				addQuestionAnswerChoice.optionNoButton.setStyleName(addWebResourceStyle.answerSelected());
				addQuestionAnswerChoice.optionSelectedButton.setStyleName(addWebResourceStyle.answerDeselected());
			}
	}
	
	public void addStyleToBody(AddQuestionAnswerChoice addQuestionAnswerChoice){
//		try{
//			if(addQuestionAnswerChoice!=null){
//				if(addQuestionAnswerChoice.answerTextBox!=null){
//					addQuestionAnswerChoice.answerTextBox.addStyleToBody();
//				}
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
	}
	public void removeStyleToBody(AddQuestionAnswerChoice addQuestionAnswerChoice){
//		try{
//			if(addQuestionAnswerChoice!=null){
//				if(addQuestionAnswerChoice.answerTextBox!=null){
//					addQuestionAnswerChoice.answerTextBox.removeStyleToBody();
//				}
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
	}
	@Override
	public void onLoad() {
		super.onLoad();
	}
	
	@UiHandler("cancelButton")
	public void clickedOnCancelButton(ClickEvent clickEvent){
		
		resetToHints();
		//resetToAnswers();
		if(isEditResource){
			if(deletedStandardsDo.size()>0){
	        	AppClientFactory.getInjector().getResourceService().UpdateResourceTaxonomy(collectionItemDo.getResource().getGooruOid(), deletedStandardsDo, new SimpleAsyncCallback<Void>() {
					
					@Override
					public void onSuccess(Void result) {
						deletedStandardsDo.clear();
					}
				});
	        }
		}
		hidePopup();
	}
	public void setTrueOrFalseFields(){
		questionTrueOrFalseAnswerChoiceContainer.clear();
		//addAnswerChoice.getElement().getStyle().setDisplay(Display.NONE);
		for(int i=0;i<2;i++){
			int widgetCount=questionTrueOrFalseAnswerChoiceContainer.getWidgetCount();
			final AddQuestionAnswerChoice addQuestionAnswer=new AddQuestionAnswerChoice(anserChoiceArray[widgetCount]);
			addQuestionAnswer.optionNoButton.setStyleName(addWebResourceStyle.answerDeselected());
			addQuestionAnswer.tinyOrTextBoxConatiner.clear();
			if(i==0){
				addQuestionAnswer.fieldValue="True";
				HTML trueHtmlString=new HTML("True");
				trueHtmlString.getElement().getStyle().setMarginTop(8, Unit.PX);
				addQuestionAnswer.tinyOrTextBoxConatiner.add(trueHtmlString);
			}else{
				addQuestionAnswer.fieldValue="False";
				HTML falseHtmlString=new HTML("False");
				falseHtmlString.getElement().getStyle().setMarginTop(8, Unit.PX);
				addQuestionAnswer.tinyOrTextBoxConatiner.add(falseHtmlString);
			}
			addQuestionAnswer.optionSelectedButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					selectOrDeselectOption(questionTrueOrFalseAnswerChoiceContainer,addQuestionAnswer);
				}
			});
			questionTrueOrFalseAnswerChoiceContainer.add(addQuestionAnswer);
		}

	}
	public void selectTrueOrFallseCorrectAnswerOption(int answerIndex,boolean isAnswerCorrect){
//		int widgetCount=questionTrueOrFalseAnswerChoiceContainer.getWidgetCount();
		//for(int i=0;i<widgetCount;i++){
			final AddQuestionAnswerChoice addQuestionAnswer=(AddQuestionAnswerChoice)questionTrueOrFalseAnswerChoiceContainer.getWidget(answerIndex);
			if(isAnswerCorrect){
				addQuestionAnswer.optionSelectedButton.setStyleName(addWebResourceStyle.answerSelected());
			}else{
				addQuestionAnswer.optionSelectedButton.setStyleName(addWebResourceStyle.answerDeselected());
			}
		//}
	}
	public void setMultipleChoiceAnswerFields(){
		questionAnswerChoiceContainer.clear();
		for(int i=0;i<2;i++){
			int widgetCount=questionAnswerChoiceContainer.getWidgetCount();
			final AddQuestionAnswerChoice addQuestionAnswer=new AddQuestionAnswerChoice(anserChoiceArray[widgetCount]);
			addQuestionAnswer.optionNoButton.setStyleName(addWebResourceStyle.answerDeselected());
			addQuestionAnswer.optionSelectedButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if(questionType.equals("MA")){
						selectYesOption(questionAnswerChoiceContainer, addQuestionAnswer);
					}else{
						selectOrDeselectOption(questionAnswerChoiceContainer,addQuestionAnswer);
					}
				}
			});
			addQuestionAnswer.optionNoButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if(questionType.equals("MA")){
						selectNoOption(questionAnswerChoiceContainer, addQuestionAnswer);
					}
				}
			});
			questionAnswerChoiceContainer.add(addQuestionAnswer);
		}
	}
	@UiHandler("addAnswerChoice")
	public void clickedOnAddChoiceButton(ClickEvent clickEvent){
		int widgetCount=questionAnswerChoiceContainer.getWidgetCount();
		final AddQuestionAnswerChoice addQuestionAnswer=new AddQuestionAnswerChoice(anserChoiceArray[widgetCount]);
		addQuestionAnswer.optionNoButton.setStyleName(addWebResourceStyle.answerDeselected());
		addQuestionAnswer.optionSelectedButton.setStyleName(addWebResourceStyle.answerDeselected());
		if(questionType.equals("MA")){
			addQuestionAnswer.showAnswerChoicesForMultipleAnswers();
		}else{
			addQuestionAnswer.showAnswerChoicesForOthers();
		}
		addQuesetionAnswerOptionTextArea(addQuestionAnswer,widgetCount);
	}
	private void addQuesetionAnswerOptionTextArea(final AddQuestionAnswerChoice addQuestionAnswer,int widgetCount){
		
		answerChoicesList.add(addQuestionAnswer);
		addQuestionAnswer.optionSelectedButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(questionType.equals("MA")){
					selectYesOption(questionAnswerChoiceContainer, addQuestionAnswer);
				}else{
					selectOrDeselectOption(questionAnswerChoiceContainer,addQuestionAnswer);
				}
			}
		});
		addQuestionAnswer.optionNoButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(questionType.equals("MA")){
					selectNoOption(questionAnswerChoiceContainer, addQuestionAnswer);
				}
			}
		});
		if(widgetCount>1){
			addQuestionAnswer.ansChoiceDeleteButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if(addQuestionAnswer.getParent().getElement().getChildNodes().getLength()>3)
					{
					addQuestionAnswer.removeFromParent();
					refreshOptionNames();
					removeToolTip.hide();
					isAddBtnClicked=true;
					}
				}
			});
			addQuestionAnswer.addMouseOverHandler(new MouseOverHandler() {
				@Override
				public void onMouseOver(MouseOverEvent event) {
					if(addQuestionAnswer.getParent().getElement().getChildNodes().getLength()>3)
					{
					addQuestionAnswer.ansChoiceDeleteButton.getElement().getStyle().setDisplay(Display.BLOCK);
					}
					else
					{
					addQuestionAnswer.ansChoiceDeleteButton.getElement().getStyle().setDisplay(Display.NONE);
					}
				}
			});
			addQuestionAnswer.addMouseOutHandler(new MouseOutHandler() {
				@Override
				public void onMouseOut(MouseOutEvent event) {
					addQuestionAnswer.ansChoiceDeleteButton.getElement().getStyle().setDisplay(Display.NONE);
				}
			});
			showRemoveToolTip(addQuestionAnswer.ansChoiceDeleteButton);	
		}
		questionAnswerChoiceContainer.add(addQuestionAnswer);
		if(questionAnswerChoiceContainer.getWidgetCount()>=5){
			addAnswerChoice.getElement().getStyle().setDisplay(Display.NONE);
		}
	}
	
	public void showRemoveToolTip(final Label deleteButton){
		deleteButton.addMouseOverHandler(new MouseOverHandler() {
			
			@Override
			public void onMouseOver(MouseOverEvent event) {
				    removeToolTip=new RemoveToolTipUc();	                
	                int left=event.getRelativeElement().getAbsoluteLeft()-16;
	                int top=event.getRelativeElement().getAbsoluteTop()+27;
				    removeToolTip.setPopupPosition(left, top);
	                removeToolTip.getElement().getStyle().setZIndex(999);
	                removeToolTip.show();
			}
		});
		deleteButton.addMouseOutHandler(new MouseOutHandler() {
			
			@Override
			public void onMouseOut(MouseOutEvent event) {
					removeToolTip.hide();
			}
		});
	}

	public void resetToHints(){
		int widgetsCount=hintsContainer.getWidgetCount();
		for(int i=0;i<widgetsCount;){
			AddHintsView addHintsView =(AddHintsView) hintsContainer.getWidget(i);
			//addHintsView.hintTextBox.setText("");
			if(i<0){			
			if(i==0){
				addHintsView.hintsTextLblVal.setText("");
			}else{
				addHintsView.hintsTextLblVal.setText("");
			}
			i++;
			}else{
				addHintsView.removeFromParent();
				widgetsCount=hintsContainer.getWidgetCount();
			}
		
		}
	}
	/*public void resetToAnswers(){
		int widgetsCount=questionAnswerChoiceContainer.getWidgetCount();
		for(int i=0; i< widgetsCount;){
			AddQuestionAnswerChoice addQuestionAnswerChoice=(AddQuestionAnswerChoice)questionAnswerChoiceContainer.getWidget(i);
			if(i<2){
				if(i==0){
					addQuestionAnswerChoice.answerTextBox.setText("");
					addQuestionAnswerChoice.optionSelectedButton.setStyleName(addWebResourceStyle.answerDeselected());
				}else{
					addQuestionAnswerChoice.answerTextBox.setText("");
					addQuestionAnswerChoice.optionSelectedButton.setStyleName(addWebResourceStyle.answerDeselected());
				}
				i++;
			}
			else{
				addQuestionAnswerChoice.removeFromParent();
				widgetsCount=questionAnswerChoiceContainer.getWidgetCount();
				addAnswerChoice.getElement().getStyle().setDisplay(Display.BLOCK);
			}
			
		}
	}*/
	
	/*public void resetAllFields(){
		int widgetsCount=questionAnswerChoiceContainer.getWidgetCount();
		addAnswerChoice.getElement().getStyle().setDisplay(Display.NONE);
		for(int i=0; i< widgetsCount;){
			AddQuestionAnswerChoice addQuestionAnswerChoice=(AddQuestionAnswerChoice)questionAnswerChoiceContainer.getWidget(i);
			if(i<2){
				if(i==0){
					//addQuestionAnswerChoice.answerTextBox.setReadOnly(true);
					addQuestionAnswerChoice.answerTextBox.setText("True");
					addQuestionAnswerChoice.optionSelectedButton.setStyleName(addWebResourceStyle.answerDeselected());
				}else{
					//addQuestionAnswerChoice.answerTextBox.setReadOnly(true);
					addQuestionAnswerChoice.answerTextBox.setText("False");
					addQuestionAnswerChoice.optionSelectedButton.setStyleName(addWebResourceStyle.answerDeselected());
				}
				i++;
			}
			else{
				addQuestionAnswerChoice.removeFromParent();
				widgetsCount=questionAnswerChoiceContainer.getWidgetCount();
			}
			
		}
		
	}*/
	
	
    @UiHandler("addHintsLabel")
    public void clickOnHintsLabel(ClickEvent event){
    	Window.enableScrolling(false);
    	int widgetCount=hintsContainer.getWidgetCount();
        final AddHintsView addHints = new AddHintsView(widgetCount+1);
        addHintsTextArea(addHints);
    }
    
    
    
    private void addHintsTextArea(final AddHintsView addHints){
    	
	       hintsContainer.add(addHints); 
	       addHints.hintDelLbl.addClickHandler(new ClickHandler() {
		        
		        @Override
		        public void onClick(ClickEvent event) 
		        {    
		            addHints.removeFromParent();
		            removeToolTip.hide();
		            refreshHintNumber();
		            isAddBtnClicked=true;
		        }
	       });
	    
	    addHints.addMouseOverHandler(new MouseOverHandler() {
	        
	        @Override
	        public void onMouseOver(MouseOverEvent event) {
	            addHints.hintDelLbl.getElement().getStyle().setDisplay(Display.BLOCK);
	            
	        }
	    });
	    addHints.addMouseOutHandler(new MouseOutHandler() {
	        
	        @Override
	        public void onMouseOut(MouseOutEvent event) {
	            addHints.hintDelLbl.getElement().getStyle().setDisplay(Display.NONE);
	        }
	    });
	    showRemoveToolTip(addHints.hintDelLbl);
	    
	    if(hintsContainer.getWidgetCount()>=5){
	        addHintsLabel.getElement().getStyle().setDisplay(Display.NONE);
	    }
    }
    

    
    protected void refreshHintNumber() 
    {
        int hintWidgetsCount=hintsContainer.getWidgetCount();
        for(int i=0 ; i<hintWidgetsCount;i++)
        {
            Widget childWidget=hintsContainer.getWidget(i);
            AddHintsView addHints=(AddHintsView)childWidget;
            addHints.hintNumLbl.setText(""+(i+1));
            
        }
        if(hintsContainer.getWidgetCount()<5){
            addHintsLabel.getElement().getStyle().setDisplay(Display.BLOCK);
        }
        
    }
    private class rightsChecked implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			if(rightsChkBox.getValue()){
				rightsLbl.getElement().getStyle().setColor("black");
			}
			else{
				rightsLbl.getElement().getStyle().setColor("orange");
			}
			
		}
	}
    private class onBrowseStandardsClick implements ClickHandler {
  		@Override
  		public void onClick(ClickEvent event) {
  			callBrowseStandards();
  		}
  	}
    
    @UiHandler("addQuestionResourceButton")
	public void clickedOnAddQuestionButton(ClickEvent event)
	{
    	boolean fieldValidationCheck;
    	if(getQuestionType().equalsIgnoreCase("T/F")){
    		if (isAnswerChoiceEmpty(questionTrueOrFalseAnswerChoiceContainer)) {
    			fieldValidationCheck = false;
				isAddBtnClicked=true;
			}
				if(!isHintsAdded(hintsContainer)){
					if (!isAnswerChoiceSelected(questionTrueOrFalseAnswerChoiceContainer)) {
						fieldValidationCheck = false;
						showErrorMessageForAnswer(ERROR_MSG_ANSWER_SELECTED);
						explanationContainer.getElement().setAttribute("style", "padding-top: 20px;");
						isAddBtnClicked=true;
					}else{
						explanationContainer.getElement().setAttribute("style", "padding-top: 0px;");
					}
			
			}
    	}else if(getQuestionType().equalsIgnoreCase("MC")){
    		if (isAnswerChoiceEmpty(questionAnswerChoiceContainer)) {
    			fieldValidationCheck = false;
				isAddBtnClicked=true;
			}
    	
			if(!isHintsAdded(hintsContainer)){
				if (!isAnswerChoiceSelected(questionAnswerChoiceContainer)) {
					String errorMessage=getQuestionType().equalsIgnoreCase("MA")?ERROR_MSG_ATLEAST_SELECTED:ERROR_MSG_ANSWER_SELECTED;
					showErrorMessageForAnswer(errorMessage);
					explanationContainer.getElement().setAttribute("style", "padding-top: 20px;");
					fieldValidationCheck = false;
					isAddBtnClicked=true;
				}else{
					explanationContainer.getElement().setAttribute("style", "padding-top: 0px;");
				}
			}
    	} else if(getQuestionType().equalsIgnoreCase("MA")){
    		if (isAnswerChoiceEmpty(questionAnswerChoiceContainer)) {
    			fieldValidationCheck = false;
				isAddBtnClicked=true;
			}
    	
			if(!isHintsAdded(hintsContainer)){
				if (!isYesOrNoChoiceSelected(questionAnswerChoiceContainer)) {
					String errorMessage=getQuestionType().equalsIgnoreCase("MA")?ERROR_MSG_ATLEAST_SELECTED:ERROR_MSG_ANSWER_SELECTED;
					showErrorMessageForAnswer(errorMessage);
					explanationContainer.getElement().setAttribute("style", "padding-top: 20px;");
					fieldValidationCheck = false;
					isAddBtnClicked=true;
				}else{
					explanationContainer.getElement().setAttribute("style", "padding-top: 0px;");
				}
			}
    	}
    	if(isAddBtnClicked){
    		isRightsClicked=rightsChkBox.getValue();
    		isAddBtnClicked=false;
    		final Map<String, String> parms = new HashMap<String, String>();
    		parms.put("text", questionNameTextArea.getText());
    		AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {

    			@Override
    			public void onSuccess(Boolean value) {
    				if(value){
    					SetStyleForProfanity.SetStyleForProfanityForTinyMCE(questionNameTextArea, questionNameErrorLbl, value);
    					isAddBtnClicked=true;
    				}else{
    					SetStyleForProfanity.SetStyleForProfanityForTinyMCE(questionNameTextArea, questionNameErrorLbl, value);
    					parms.put("text", explainationTextArea.getText());
    					AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {

    						@Override
    						public void onSuccess(Boolean value) {
    							if(value){
    								SetStyleForProfanity.SetStyleForProfanityForTinyMCE(explainationTextArea, explainationErrorLbl, value);
    								isAddBtnClicked=true;
    							}else{
    								
    								MixpanelUtil.mixpanelEvent("Collaborator_edits_collection");
    								
    								SetStyleForProfanity.SetStyleForProfanityForTinyMCE(explainationTextArea, explainationErrorLbl, value);
    								String mediaFileName=null;
    							       
    						        boolean isQuestEnteredFlag = true;
    						        List<String> answersListFIB = new ArrayList<String>();
    						    	boolean fieldValidationStaus=true;
    						    	if(addQuestImgContainer.getElement().hasChildNodes()){
    						    		AddQuestionImg addQuestionImage=(AddQuestionImg)addQuestImgContainer.getWidget(0);
    						    		mediaFileName=addQuestionImage.getFileName();
    						    	}
    						    	clearErrorQuestionMessage();
    						        if(questionNameTextArea.getText()==null||questionNameTextArea.getText().trim().equals("")){
    						        	showErrorQuestionMessage(ERROR_MSG_QUESTION);
    						        	fieldValidationStaus=false;
    						        	isQuestEnteredFlag=false;
    						        	isAddBtnClicked=true;
    						        }
    						        //This regex is used to get text count with out html tags
    						        String questionNameText = questionNameTextArea.getText().replaceAll("\\<.*?>","");
    						        if(questionNameText.length()>QUESTION_TEXT_LENGTH){
    						        	showErrorQuestionMessage(ERROR_MSG_QUESTION_LENGTH);
    						        	fieldValidationStaus=false;
    						        	isAddBtnClicked=true;
    						        }
    						        errorMessageForExplanation.setText("");
    						        if(questionNameText.length()>QUESTION_TEXT_LENGTH){
    						        	showErrorQuestionMessage(ERROR_MSG_QUESTION_LENGTH);
    						        	fieldValidationStaus=false;
    						        	isAddBtnClicked=true;
    						        }
    						        String explainationText = explainationTextArea.getText().replaceAll("\\<.*?>","");
    						    	if(explainationText.trim().length() > EXPLAINATION_TEXT_LENGTH){
    						    		errorMessageForExplanation.setText(ERROR_MSG_EXPLAINATION_LENGTH);
    									fieldValidationStaus=false;
    									isAddBtnClicked=true;
    								}
    						    	if (fieldValidationStaus && getQuestionType().equalsIgnoreCase("T/F")) {
    									clearErrorMessageForAnswer();
    									if (isAnswerChoiceEmpty(questionTrueOrFalseAnswerChoiceContainer)) {
    										fieldValidationStaus = false;
    										isAddBtnClicked=true;
    									}else{
    										
    										if(!isHintsAdded(hintsContainer)){
    											if (!isAnswerChoiceSelected(questionTrueOrFalseAnswerChoiceContainer)) {
    												fieldValidationStaus = false;
    												showErrorMessageForAnswer(ERROR_MSG_ANSWER_SELECTED);
    												explanationContainer.getElement().setAttribute("style", "padding-top: 20px;");
    												isAddBtnClicked=true;
    											}else{
    												explanationContainer.getElement().setAttribute("style", "padding-top: 0px;");
    												profanityCheckForHints(fieldValidationStaus,answersListFIB,mediaFileName);
    											}
    										}
    									}
    								}
    							
    								else if (fieldValidationStaus && getQuestionType().equalsIgnoreCase("MC")) {
    									clearErrorMessageForAnswer();
    									if (isAnswerChoiceEmpty(questionAnswerChoiceContainer)) {
    										fieldValidationStaus = false;
    										isAddBtnClicked=true;
    									}else{
    										if(!isHintsAdded(hintsContainer)){
    											if (!isAnswerChoiceSelected(questionAnswerChoiceContainer)) {
    												String errorMessage=getQuestionType().equalsIgnoreCase("MA")?ERROR_MSG_ATLEAST_SELECTED:ERROR_MSG_ANSWER_SELECTED;
    												showErrorMessageForAnswer(errorMessage);
    												explanationContainer.getElement().setAttribute("style", "padding-top: 20px;");
    												fieldValidationStaus = false;
    												isAddBtnClicked=true;
    											}else{
    												explanationContainer.getElement().setAttribute("style", "padding-top: 0px;");
    												isProfanityCheckForAnswerChoice(fieldValidationStaus,answersListFIB,mediaFileName);
    											}
    										}
    									}
    								}
    						    	
    								else if (fieldValidationStaus && getQuestionType().equalsIgnoreCase("MA")) {
    									clearErrorMessageForAnswer();
    									if (isAnswerChoiceEmpty(questionAnswerChoiceContainer)) {
    										fieldValidationStaus = false;
    										isAddBtnClicked=true;
    									}else{
    										if(!isHintsAdded(hintsContainer)){
    											if (!isYesOrNoChoiceSelected(questionAnswerChoiceContainer)) {
    												String errorMessage=getQuestionType().equalsIgnoreCase("MA")?ERROR_MSG_ATLEAST_SELECTED:ERROR_MSG_ANSWER_SELECTED;
    												showErrorMessageForAnswer(errorMessage);
    												
    												explanationContainer.getElement().setAttribute("style", "padding-top: 20px;");
    												fieldValidationStaus = false;
    												isAddBtnClicked=true;
    											}else{
    												explanationContainer.getElement().setAttribute("style", "padding-top: 0px;");
    												isProfanityCheckForAnswerChoice(fieldValidationStaus,answersListFIB,mediaFileName);
    											}
    										}
    									}
    								}
    								/**
    								 * Based on Question Type FIB validations done.
    								 */
    								else if(fieldValidationStaus && getQuestionType().equalsIgnoreCase("FIB")){
    									
    									String questionTextFIB = questionNameTextArea.getRawContent().trim();
    									boolean isBalanced=true;
    									if(!isFIBQuestionBalanced(questionTextFIB) && (questionNameTextArea.getText().trim()!=null || !questionNameTextArea.getText().trim().equalsIgnoreCase(""))){ 
    										errorMessageForQuestion.setText(ERROR_MSG_FIB_BALANCED);
    										errorMessageForQuestion.getElement().setAttribute("alt", ERROR_MSG_FIB_BALANCED);
        						        	errorMessageForQuestion.getElement().setAttribute("title", ERROR_MSG_FIB_BALANCED);
    							        	fieldValidationStaus=false;
    							        	isBalanced=false;
    							        	isAddBtnClicked=true;
    									}
    									
    									if(isBalanced){
    										if(answersListFIB.size()!=0){
    											answersListFIB.removeAll(answersListFIB);
    											answersListFIB = getFIBAnswers(questionTextFIB);
    										}else{
    											answersListFIB = getFIBAnswers(questionTextFIB);
    										}
    									}
    									
    									if(answersListFIB.size()==0 && isQuestEnteredFlag){
    										errorMessageForQuestion.setText(ERROR_MSG_FIB_BALANCED);
    										errorMessageForQuestion.getElement().setAttribute("alt", ERROR_MSG_FIB_BALANCED);
        						        	errorMessageForQuestion.getElement().setAttribute("title", ERROR_MSG_FIB_BALANCED);
    										fieldValidationStaus=false;
    										isAddBtnClicked=true;
    									}
    									if(answersListFIB.size()!=0 && isQuestEnteredFlag){
    										for(String answer:answersListFIB){
    											if(answer.contains("[") || answer.contains("]")){
    												errorMessageForQuestion.setText(ERROR_MSG_MULTIPLE_FIB);
    												errorMessageForQuestion.getElement().setAttribute("alt", ERROR_MSG_MULTIPLE_FIB);
    	        						        	errorMessageForQuestion.getElement().setAttribute("title", ERROR_MSG_MULTIPLE_FIB);
    												fieldValidationStaus=false;
    												break;
    											}
    										}
    									}
    									if(isAnsweEmpty){
    										errorMessageForQuestion.setText(ERROR_MSG_MARK_AS_BLANK);
    										errorMessageForQuestion.getElement().setAttribute("alt", ERROR_MSG_MARK_AS_BLANK);
        						        	errorMessageForQuestion.getElement().setAttribute("title", ERROR_MSG_MARK_AS_BLANK);
    										isAnsweEmpty=false;
    										fieldValidationStaus=false;
    										isAddBtnClicked=true;
    									}else{
    										isAnsweEmpty=true;
    									}
    									if(answersListFIB.size()>3 && isQuestEnteredFlag){
    										errorMessageForQuestion.setText(ERROR_MSG_FIB_BLANKS);
    										errorMessageForQuestion.getElement().setAttribute("alt", ERROR_MSG_FIB_BLANKS);
        						        	errorMessageForQuestion.getElement().setAttribute("title", ERROR_MSG_FIB_BLANKS);
    										fieldValidationStaus=false;
    										isAddBtnClicked=true;
    									}
    									if(isAnsweEmpty && !isHintsAdded(hintsContainer)){
    										profanityCheckForHints(fieldValidationStaus,answersListFIB,mediaFileName);
    									}else{
    										isAddBtnClicked=true;
    									}
    									
    								}else if(fieldValidationStaus && getQuestionType().equalsIgnoreCase("OE")){
    									if(!isHintsAdded(hintsContainer)){
    										profanityCheckForHints(fieldValidationStaus,answersListFIB,mediaFileName);
    									}
    								}
    							}
    						}
    					});
    				}
    			}
    		});
    	}
	}
    /**
     * 
     * @function showErrorMessageForAnswer 
     * 
     * @created_date : 16-Dec-2014
     * 
     * @description
     * 
     * 
     * @parm(s) : @param errMessage
     * 
     * @return : void
     *
     * @throws : <Mentioned if any exceptions>
     *
     * 
     *
     *
     */
    void showErrorMessageForAnswer(String errMessage){
    	    	
		ansChoiceErrMsg.setText(errMessage);
    	StringUtil.setAttributes(ansChoiceErrMsg.getElement(), "lblErrorMessageForAnswer", errMessage, errMessage);
    	
    	questionNameTextArea.getElement().addClassName("errorBorderMessage");
    }
    /**
     * 
     * @function clearErrorMessageForAnswer 
     * 
     * @created_date : 16-Dec-2014
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
    void clearErrorMessageForAnswer(){
    	errorMessageForQuestion.setText("");
    	StringUtil.setAttributes(errorMessageForQuestion.getElement(), "errlblErrorMessageForQuestion", null, null);
    	
    	questionNameTextArea.getElement().removeClassName("errorBorderMessage");
    }
    /**
     * 
     * @function showErrorQuestionMessage 
     * 
     * @created_date : 16-Dec-2014
     * 
     * @description
     * 
     * 
     * @parm(s) : @param errMessage
     * 
     * @return : void
     *
     * @throws : <Mentioned if any exceptions>
     *
     * 
     *
     *
     */
    void showErrorQuestionMessage(String errMessage){
    	
    	errorMessageForQuestion.setText(errMessage);
    	StringUtil.setAttributes(errorMessageForQuestion.getElement(), "errlblErrorMessageForQuestion", errMessage, errMessage);
    	
    	questionNameTextArea.getElement().addClassName("errorBorderMessage");
    }
    /**
     * 
     * @function clearErrorQuestionMessage 
     * 
     * @created_date : 16-Dec-2014
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
    void clearErrorQuestionMessage(){
    	errorMessageForQuestion.setText("");
    	StringUtil.setAttributes(errorMessageForQuestion.getElement(), "errlblErrorMessageForQuestion", null, null);
    	
    	questionNameTextArea.getElement().removeClassName("errorBorderMessage");
    }

    /**
	 * If all validations successful, question is added to the collection.
	 */
	public void addFunctionality(boolean fieldValidationStaus,List<String> answersListFIB,String mediaFileName){
		
    	if(fieldValidationStaus){
    		buttonContainer.getElement().getStyle().setDisplay(Display.NONE);
    		loadingTextLbl.setVisible(true);
    		CollectionQuestionItemDo collectionQuestionItemDo = new CollectionQuestionItemDo();
			String questionText = questionNameTextArea.getRawContent().replace("&nbsp;","").trim();
			String explaination = explainationTextArea.getText();
			if(explaination!=null&&!explaination.trim().equals("")){
				explaination=explainationTextArea.getRawContent().trim();
			}
					
			ArrayList<QuestionAnswerDo> enteredAnswers = new ArrayList<QuestionAnswerDo>(); 
			ArrayList<QuestionHintsDo> enteredHints = new ArrayList<QuestionHintsDo>();
			HashMap<String,ArrayList<QuestionAnswerDo>> answerMap = new HashMap<String,ArrayList<QuestionAnswerDo>>();
			HashMap<String,ArrayList<QuestionHintsDo>> hintsMap = new HashMap<String,ArrayList<QuestionHintsDo>>();
			if(getQuestionType().equalsIgnoreCase("T/F")){
				for(int i=0;i<questionTrueOrFalseAnswerChoiceContainer.getWidgetCount();i++)
				{
					QuestionAnswerDo questionAnswerDo = new QuestionAnswerDo();
					AddQuestionAnswerChoice addQuestionAnswerChoice=(AddQuestionAnswerChoice)questionTrueOrFalseAnswerChoiceContainer.getWidget(i);
					questionAnswerDo.setAnswerText(addQuestionAnswerChoice.fieldValue);
					//questionAnswerDo.setAnswerText(addQuestionAnswerChoice.answerTextBox.getRawContent()); 
					questionAnswerDo.setAnswerType("text");
					questionAnswerDo.setSequence(i+1);
					if(addQuestionAnswerChoice.optionSelectedButton.getStyleName().equals(addWebResourceStyle.answerSelected()))
					{
						questionAnswerDo.setIsCorrect(true);
					}
					else
					{
						questionAnswerDo.setIsCorrect(false);
					}
					enteredAnswers.add(questionAnswerDo);
				}
				
			}else if(getQuestionType().equalsIgnoreCase("MC")||getQuestionType().equalsIgnoreCase("MA")){
				for(int i=0;i<questionAnswerChoiceContainer.getWidgetCount();i++)
				{
					QuestionAnswerDo questionAnswerDo = new QuestionAnswerDo();
					AddQuestionAnswerChoice addQuestionAnswerChoice=(AddQuestionAnswerChoice)questionAnswerChoiceContainer.getWidget(i);
					questionAnswerDo.setAnswerText(addQuestionAnswerChoice.answerTextBox.getRawContent());
					//questionAnswerDo.setAnswerText(addQuestionAnswerChoice.answerTextBox.getRawContent()); 
					questionAnswerDo.setAnswerType("text");
					questionAnswerDo.setSequence(i+1);
					if(addQuestionAnswerChoice.optionSelectedButton.getStyleName().equals(addWebResourceStyle.answerSelected()))
					{
						questionAnswerDo.setIsCorrect(true);
					}
					else
					{
						questionAnswerDo.setIsCorrect(false);
					}
					enteredAnswers.add(questionAnswerDo);
				}
			}else if(getQuestionType().equalsIgnoreCase("FIB")){
				questionText = getFIBQuestion(questionNameTextArea.getRawContent().replace("&nbsp;","").trim()); 
				for(int i=0;i<answersListFIB.size();i++)
				{
					QuestionAnswerDo questionAnswerDo = new QuestionAnswerDo();
					questionAnswerDo.setAnswerText(answersListFIB.get(i).trim()); 
					questionAnswerDo.setAnswerType("text");
					questionAnswerDo.setSequence(i+1);
					questionAnswerDo.setIsCorrect(false);
					enteredAnswers.add(questionAnswerDo);
				}
			}
			
			answerMap.put("answer",enteredAnswers);
				
			for(int i=0;i<hintsContainer.getWidgetCount();i++)
			{
				AddHintsView addHints = (AddHintsView)hintsContainer.getWidget(i);
				QuestionHintsDo questionHintsDo=new QuestionHintsDo();
				String hintText=addHints.hintTextBox.getText();
				if(hintText!=null&&!hintText.trim().equals("")){
					hintText=addHints.hintTextBox.getRawContent().trim();
				}
				questionHintsDo.setHintText(hintText); 
				questionHintsDo.setSequence(i+1);
				enteredHints.add(questionHintsDo);
			}
			hintsMap.put("hint",enteredHints);
			
			collectionQuestionItemDo.setTypeName(getQuestionType()); 
			collectionQuestionItemDo.setQuestionText(questionText);
			collectionQuestionItemDo.setAnswers(answerMap);
			collectionQuestionItemDo.setExplanation(explaination);
			collectionQuestionItemDo.setHints(hintsMap);
			collectionQuestionItemDo.setTitle(questionText);
			collectionQuestionItemDo.setDescription(questionText);  
			HashMap<String,ArrayList<CodeDo>> taxonomySet = new HashMap<String,ArrayList<CodeDo>>();
			 taxonomySet.put("taxonomyCode", standardsDo);
			collectionQuestionItemDo.setTaxonomySet(taxonomySet);
			HashMap<String,ArrayList<checkboxSelectedDo>> depthOfKnowledge = new HashMap<String,ArrayList<checkboxSelectedDo>>();
			depthOfKnowledge.put("depthOfKnowledge", depthOfKnowledges);
			collectionQuestionItemDo.setDepthOfKnowledges(depthOfKnowledge);
			
			/*HashMap<String,ArrayList<checkboxSelectedDo>> educationalUse = new HashMap<String,ArrayList<checkboxSelectedDo>>();
			ArrayList<checkboxSelectedDo> arrayList=new ArrayList<checkboxSelectedDo>();
			if(!isEditResource){
				checkboxSelectedDo checkObj=new checkboxSelectedDo();
				checkObj.setSelected(true);
				checkObj.setValue(resourceEducationalLabel.getText());
				arrayList.add(checkObj);
			}else{
				if(collectionItemDo.getResource().getEducationalUse() != null)
				{
				for(int eduI=0; eduI<collectionItemDo.getResource().getEducationalUse().size(); eduI++)
				{
					if(!resourceEducationalLabel.getText().trim().equalsIgnoreCase(collectionItemDo.getResource().getEducationalUse().get(eduI).getValue().trim()))
					{
						checkboxSelectedDo eduUseObjPrevious=new checkboxSelectedDo();
						eduUseObjPrevious.setSelected(false);
						eduUseObjPrevious.setValue(collectionItemDo.getResource().getEducationalUse().get(eduI).getValue());
						arrayList.add(eduUseObjPrevious);
					}else{
						checkboxSelectedDo eduUseObjPrevious=new checkboxSelectedDo();
						eduUseObjPrevious.setSelected(true);
						eduUseObjPrevious.setValue(collectionItemDo.getResource().getEducationalUse().get(eduI).getValue());
						arrayList.add(eduUseObjPrevious);
					}
				}
				}
			}
			educationalUse.put("educationalUse", arrayList);
			if(!resourceEducationalLabel.getText().equalsIgnoreCase(i18n.GL1684))
			collectionQuestionItemDo.setEducationalUse(educationalUse);*/
			if(!isSaveButtonClicked){
				isSaveButtonClicked=true;
				if (getQuestionType().equalsIgnoreCase("T/F")) {
		    		MixpanelUtil.ClickAddOnTrueOrFalseTabFromCollectionEdit();
		    	}else if(getQuestionType().equalsIgnoreCase("MC")){
		    		MixpanelUtil.ClickAddOnMultipleChoiceTabFromCollectionEdit();
		    	}
		    	else if(getQuestionType().equalsIgnoreCase("OE")){
		    		MixpanelUtil.ClickAddOnOpenEndedTabFromCollectionEdit();
		    	}
		    	else if(getQuestionType().equalsIgnoreCase("MA")){
		    		MixpanelUtil.successfullyaddaMAquestiontype();
		    	}else if(getQuestionType().equalsIgnoreCase("FIB")){
		    		MixpanelUtil.question_Create_FIB_success();
		    	}
				createQuestionResource(mediaFileName,collectionQuestionItemDo);
			}
    	}
	}
	
	/**
	 * Abstract method to add question resource.
	 * @param mediaFileName instance of {@link String}
	 * @param collectionQuestionItemDo instance of {@link CollectionQuestionItemDo}
	 */
	public abstract void createQuestionResource(String mediaFileName,CollectionQuestionItemDo collectionQuestionItemDo);
	
	 private boolean isAnswerChoiceSelected(HTMLPanel questionAnswerChoiceContainer) 
     {
     
             boolean isAnswerChoiceSelected=false;
             for(int i=0;i<questionAnswerChoiceContainer.getWidgetCount();i++){
                     AddQuestionAnswerChoice addQuestionAnswerChoice=(AddQuestionAnswerChoice)questionAnswerChoiceContainer.getWidget(i);
                     if(addQuestionAnswerChoice.optionSelectedButton.getStyleName().equals(addWebResourceStyle.answerSelected())){
                            isAnswerChoiceSelected=true;
                     }
                     
             }
             
             return isAnswerChoiceSelected;

     }
	 private boolean isYesOrNoChoiceSelected(HTMLPanel questionAnswerChoiceContainer) {
         boolean isAnswerChoiceSelected=false;
         for(int i=0;i<questionAnswerChoiceContainer.getWidgetCount();i++){
             AddQuestionAnswerChoice addQuestionAnswerChoice=(AddQuestionAnswerChoice)questionAnswerChoiceContainer.getWidget(i);
             if(addQuestionAnswerChoice.optionSelectedButton.getStyleName().equals(addWebResourceStyle.answerSelected())||
            		 addQuestionAnswerChoice.optionNoButton.getStyleName().equals(addWebResourceStyle.answerSelected())){
                    isAnswerChoiceSelected=true;
             }else{
            	 return false;
             }
         }
         
         return isAnswerChoiceSelected;
     }
	 public boolean profanityCheckForHints(final boolean fieldValidationStaus,final List<String> answersListFIB,final String mediaFileName){
		 validationValue=false;
		 AppClientFactory.getInjector().getResourceService().checkProfanityForList(hintsListForProfanity, new SimpleAsyncCallback<List<ProfanityCheckDo>>() {
				
				@Override
				public void onSuccess(List<ProfanityCheckDo> result) {
					 for(int i=0;i<hintsContainer.getWidgetCount();i++){
						 final AddHintsView addHints = (AddHintsView) hintsContainer.getWidget(i);
						 addHints.errorMessageforHints.setText("");
						 SetStyleForProfanity.SetStyleForProfanityForTinyMCE(addHints.hintTextBox, addHints.errorMessageforHints, result.get(i).questionValue);
						  if(result.get(i).questionValue==true){
							  addHints.errorMessageforHints.getElement().setAttribute("style", "float: left;left: 24px;");
			                  validationValue=true;
			                  isAddBtnClicked=true;
						  }
					  }
					 if(validationValue){
						  return;
					  }else{
						  if(!isRightsClicked){
								rightsLbl.getElement().getStyle().setColor("orange");
								isAddBtnClicked=true;
							}else{
								if(fieldValidationStaus){
									 addFunctionality(!validationValue,answersListFIB,mediaFileName);
								}
							}
					  }
				}
			});
		 return validationValue;
	 }
	 /**
	  * 
	  * @function isProfanityCheck 
	  * 
	  * @created_date : 24-Jan-2014
	  * 
	  * @description : This method is used to check profanity for multiple widgets list (Because we can't do sync call form client side)
	  * 
	  * @parm(s) : @return
	  * 
	  * @return : boolean
	  *
	  * @throws : <Mentioned if any exceptions>
	  *
	  */
    public boolean isProfanityCheckForAnswerChoice(final boolean fieldValidationStaus,final List<String> answersListFIB,final String mediaFileName){
    	validationValue=false;
    	AppClientFactory.getInjector().getResourceService().checkProfanityForList(profanityList, new SimpleAsyncCallback<List<ProfanityCheckDo>>() {
			
			@Override
			public void onSuccess(List<ProfanityCheckDo> result) {
				//addFunctionality(fieldValidationStaus,answersListFIB,mediaFileName);
				  for(int i=0;i<questionAnswerChoiceContainer.getWidgetCount();i++){
					  final AddQuestionAnswerChoice addQuestionAnswerChoice=(AddQuestionAnswerChoice)questionAnswerChoiceContainer.getWidget(i);
					  addQuestionAnswerChoice.errorMessageforAnswerChoice.setText("");
					  addQuestionAnswerChoice.getAnswerTextBox().getElement().removeClassName("errorBorderMessage");
					  SetStyleForProfanity.SetStyleForProfanityForTinyMCE(addQuestionAnswerChoice.answerTextBox, addQuestionAnswerChoice.errorMessageforAnswerChoice, result.get(i).questionValue);
					  if(result.get(i)!=null && result.get(i).questionValue==true){
						  	 addQuestionAnswerChoice.errorMessageforAnswerChoice.getElement().setAttribute("style", "float: left;left: 24px;");
		                     validationValue=true;
		                     isAddBtnClicked=true;
					  }
				  }
				  if(validationValue && fieldValidationStaus){
					  return;
				  }else{
						AppClientFactory.getInjector().getResourceService().checkProfanityForList(hintsListForProfanity, new SimpleAsyncCallback<List<ProfanityCheckDo>>() {
							
							@Override
							public void onSuccess(List<ProfanityCheckDo> result) {
								 for(int i=0;i<hintsContainer.getWidgetCount();i++){
									  if(result.get(i).questionValue==true){
										  final AddHintsView addHints = (AddHintsView) hintsContainer.getWidget(i);
										  addHints.errorMessageforHints.setText("");
										  addHints.errorMessageforHints.getElement().setAttribute("style", "float: left;left: 24px;");
						                  SetStyleForProfanity.SetStyleForProfanityForTinyMCE(addHints.hintTextBox, addHints.errorMessageforHints, result.get(i).questionValue);
						                  validationValue=true;
						                  isAddBtnClicked=true;
									  }
								  }
								 if(validationValue){
									  return;
								  }else{
									  if(!isRightsClicked){
											rightsLbl.getElement().getStyle().setColor("orange");
											isAddBtnClicked=true;
										}else{
											 if(fieldValidationStaus)
											  addFunctionality(!validationValue,answersListFIB,mediaFileName);
										}
								  }
							}
						});
				  }
			}
		});
		return validationValue;
    }
    public boolean isAnswerChoiceEmpty(HTMLPanel questionAnswerChoiceContainer){
		profanityList=new ArrayList<ProfanityCheckDo>();
         for(int i=0;i<questionAnswerChoiceContainer.getWidgetCount();i++){
                 final AddQuestionAnswerChoice addQuestionAnswerChoice=(AddQuestionAnswerChoice)questionAnswerChoiceContainer.getWidget(i);
                 String answerChoiceValue=null;
                 addQuestionAnswerChoice.errorMessageforAnswerChoice.setText("");
                 addQuestionAnswerChoice.getAnswerTextBox().getElement().removeClassName("errorBorderMessage");
                 if(getQuestionType().equalsIgnoreCase("T/F")){
                	 answerChoiceValue=addQuestionAnswerChoice.fieldValue;
                 }else if(getQuestionType().equalsIgnoreCase("MC")||getQuestionType().equalsIgnoreCase("MA")){
                	 answerChoiceValue=addQuestionAnswerChoice.answerTextBox.getContent().replaceAll("\\<.*?>","");
                 }
                 ProfanityCheckDo profanitymodel=new ProfanityCheckDo();
                 if(answerChoiceValue==null||answerChoiceValue.trim().equalsIgnoreCase("")){
                	  	 isAnswerChoiceSelected=true;
                         addQuestionAnswerChoice.errorMessageforAnswerChoice.setText(ERROR_MSG_ANSWER);
                         addQuestionAnswerChoice.getAnswerTextBox().getElement().addClassName("errorBorderMessage");
                         profanitymodel.setQuestionID(Integer.toString(i));
                         profanityList.add(profanitymodel);
                         addQuestionAnswerChoice.errorMessageforAnswerChoice.getElement().setAttribute("style", "display:block");
                 }else{
                	 if(answerChoiceValue.trim().length()>ANSWER_CHOICE_HINTS_TEXT_LENGTH){
                		   isAnswerChoiceSelected=true;
                		   Document.get().getElementById(addQuestionAnswerChoice.answerTextBox.getID()+"_message").setInnerText("");
                		   addQuestionAnswerChoice.errorMessageforAnswerChoice.setText(ERROR_MSG_ANSWER_LENGTH);
                		   addQuestionAnswerChoice.getAnswerTextBox().getElement().addClassName("errorBorderMessage");
                	 }else{
                		 	isAnswerChoiceSelected=false;
	                		profanitymodel.setQuestionID(Integer.toString(i));
	                     	profanitymodel.setQuestionText(answerChoiceValue);
	                     	profanityList.add(profanitymodel);
                	 }
                 }
         }
         return isAnswerChoiceSelected;
 }
    public boolean isHintsAdded(HTMLPanel hintsContainer){
    	boolean hintsAdded=false;
    	hintsListForProfanity=new ArrayList<ProfanityCheckDo>();
    	if(hintsContainer.getWidgetCount()>=1){
    		  for(int i=0;i<hintsContainer.getWidgetCount();i++){
  	        	final AddHintsView addHints = (AddHintsView) hintsContainer.getWidget(i);
  	        	  ProfanityCheckDo profanitymodel=new ProfanityCheckDo();
  	        	  profanitymodel.setQuestionID(Integer.toString(i));
  	        	 
  	        	 String hintText=addHints.hintTextBox.getContent().toString().trim().replaceAll("&nbsp;", " ");
  	        	 hintText=hintText.replaceAll("\\<.*?>","");
  	        	  if(hintText!=null && !hintText.trim().equals("")){
  	        		 String hintsText=addHints.hintTextBox.getContent().replaceAll("\\<.*?>","");	
  	        		  if(hintsText.trim().length()>ANSWER_CHOICE_HINTS_TEXT_LENGTH){
  	        			  Document.get().getElementById(addHints.hintTextBox.getID()+"_message").setInnerText("");
  	            		  addHints.errorMessageforHints.setText(ERROR_MSG_HINTS_LENGTH);
  	            		  hintsAdded=true;
  	            		  isAddBtnClicked=true;
  	        		  }else{
  	        			  hintsAdded=false;
  	        			  isAddBtnClicked=true;
 	            		  addHints.errorMessageforHints.setText("");
 	            		  profanitymodel.setQuestionText(addHints.hintTextBox.getContent());
  	        		  }
  	        		 
  	        	  }else{
  	        		  addHints.errorMessageforHints.setText(ERROR_MSG_HINTS);
	        		  hintsAdded=true;
            	      isAddBtnClicked=true;
  	        	  }
  	        	  hintsListForProfanity.add(profanitymodel);
  	        }
    	}
        return hintsAdded;
}
     /*public void resetAllErrorFields(){
    		errorMessageForQuestion.setText("");
    		ansChoiceErrMsg.setText("");
    		errorMessageForExplanation.setText("");
    		 for(int i=0;i<questionAnswerChoiceContainer.getWidgetCount();i++){
         		 AddQuestionAnswerChoice addQuestionAnswerChoice=(AddQuestionAnswerChoice)questionAnswerChoiceContainer.getWidget(i);   
                 addQuestionAnswerChoice.errorMessageforAnswerChoice.setText("");       
    		 }
     }*/
     
	@UiHandler("addQuestionImg")
	public void clickOnAddQuestImg(ClickEvent event){
		uploadQuestionImage();
	}
	public void setEditQuestionImage(){
		String tumbnailUrl="";
		if(collectionItemDo!=null){
			if (collectionItemDo.getResource().getAssets() != null
					&& collectionItemDo.getResource().getAssets().size() > 0) {
				tumbnailUrl = collectionItemDo.getCollection().getAssetURI()
						+ collectionItemDo.getResource().getFolder()
						+ collectionItemDo.getResource().getAssets().get(0).getAsset().getName();
				
			} 
			setEditQuestionImage(tumbnailUrl);
		}
		
	}
	
	/**
	 * This method sets Multiple Answer question type content.
	 * 
	 */
	
	public void showMulipleChoice(){
		setHeaderAndBodyText(questionType);
		clearErrorMessageForAnswer();
		clearErrorQuestionMessage();
		 for(int i=0;i<questionAnswerChoiceContainer.getWidgetCount();i++){
     		 AddQuestionAnswerChoice addQuestionAnswerChoice=(AddQuestionAnswerChoice)questionAnswerChoiceContainer.getWidget(i); 
     		addQuestionAnswerChoice.optionNoButton.setStyleName(addWebResourceStyle.answerDeselected());
     		 addQuestionAnswerChoice.errorMessageforAnswerChoice.setText("");
     		addQuestionAnswerChoice.getAnswerTextBox().getElement().removeClassName("errorBorderMessage");
     		 addQuestionAnswerChoice.showAnswerChoicesForOthers();
		 }
		questionTrueOrFalseAnswerChoiceContainer.getElement().getStyle().setDisplay(Display.NONE);
		questionAnswerChoiceContainer.getElement().getStyle().setDisplay(Display.BLOCK);
		addAnswerChoice.getElement().getStyle().setDisplay(Display.BLOCK);
		answerchoiceTitleContainer.getElement().getStyle().setDisplay(Display.BLOCK);
	}
	
	/**
	 * This method sets Multiple Answer question type content.
	 * 
	 */
	
	public void showMulipleAnswerChoiceOptions(){
		setHeaderAndBodyText(questionType);
		clearErrorMessageForAnswer();
		clearErrorQuestionMessage();
		 for(int i=0;i<questionAnswerChoiceContainer.getWidgetCount();i++){
     		 AddQuestionAnswerChoice addQuestionAnswerChoice=(AddQuestionAnswerChoice)questionAnswerChoiceContainer.getWidget(i);  
     		addQuestionAnswerChoice.optionNoButton.setStyleName(addWebResourceStyle.answerDeselected());
     		 addQuestionAnswerChoice.errorMessageforAnswerChoice.setText("");
     		addQuestionAnswerChoice.getAnswerTextBox().getElement().removeClassName("errorBorderMessage");
     		 addQuestionAnswerChoice.showAnswerChoicesForMultipleAnswers();
		 }
		questionTrueOrFalseAnswerChoiceContainer.getElement().getStyle().setDisplay(Display.NONE);
		questionAnswerChoiceContainer.getElement().getStyle().setDisplay(Display.BLOCK);
		addAnswerChoice.getElement().getStyle().setDisplay(Display.BLOCK);
		answerchoiceTitleContainer.getElement().getStyle().setDisplay(Display.BLOCK);
	}
	
	/**
	 * This method sets True/False question type content.
	 * 
	 */
	public void showTrueOrFalseAnswerChoice(){
		setHeaderAndBodyText("T/F");
		clearErrorMessageForAnswer();
		clearErrorQuestionMessage();
		questionAnswerChoiceContainer.getElement().getStyle().setDisplay(Display.NONE);
		questionTrueOrFalseAnswerChoiceContainer.getElement().getStyle().setDisplay(Display.BLOCK);
		addAnswerChoice.getElement().getStyle().setDisplay(Display.NONE);
		answerchoiceTitleContainer.getElement().getStyle().setDisplay(Display.BLOCK);
	}
	
	/**
	 * This method sets Open-Ended question type content.
	 * 
	 */
	
	public void showOpenEndedQuestion(){ 
		setHeaderAndBodyText("OE");
		clearErrorMessageForAnswer();
		clearErrorQuestionMessage();
		questionAnswerChoiceContainer.getElement().getStyle().setDisplay(Display.NONE);
		questionTrueOrFalseAnswerChoiceContainer.getElement().getStyle().setDisplay(Display.NONE);
		addAnswerChoice.getElement().getStyle().setDisplay(Display.NONE);
		answerchoiceTitleContainer.getElement().getStyle().setDisplay(Display.NONE);
		
	}
	/**
	 * This method sets Fill in the blank question content.
	 */
	public void showFillInTheBlank(){
		questionNameTextArea.markAsBlankPanel.setVisible(true);
		setHeaderAndBodyText("FIB");
		clearErrorMessageForAnswer();
		clearErrorQuestionMessage();
		questionAnswerChoiceContainer.getElement().getStyle().setDisplay(Display.NONE);
		questionTrueOrFalseAnswerChoiceContainer.getElement().getStyle().setDisplay(Display.NONE);
		addAnswerChoice.getElement().getStyle().setDisplay(Display.NONE);
		answerchoiceTitleContainer.getElement().getStyle().setDisplay(Display.NONE);
	}
	
	/**
	 * This method sets header text for respective questions.
	 * 
	 * @param tabType
	 *            is a selected question type.
	 */
	
	public void setHeaderAndBodyText(String tabType){
		if(tabType.equals("MC")){
			questionTypeHeader.setText(i18n.GL0349());
			questionTypeText.setText(i18n.GL0350());
			questionTypeHeader.getElement().setAttribute("alt", i18n.GL0349());
			questionTypeHeader.getElement().setAttribute("title", i18n.GL0349());
			questionTypeText.getElement().setAttribute("alt", i18n.GL0350());
			questionTypeText.getElement().setAttribute("title", i18n.GL0350());
			questionNameTextArea.markAsBlankPanel.setVisible(false);
			charLimitLbl.getElement().setAttribute("style", "margin-left: 165px;");
		}else if(tabType.equals("MA")){
			questionTypeHeader.setText(i18n.GL0351());
			questionTypeHeader.getElement().setAttribute("alt", i18n.GL0351());
			questionTypeHeader.getElement().setAttribute("title", i18n.GL0351());
			questionTypeText.setText(i18n.GL0352());
			questionTypeText.getElement().setAttribute("alt", i18n.GL0352());
			questionTypeText.getElement().setAttribute("title", i18n.GL0352());
			questionNameTextArea.markAsBlankPanel.setVisible(false);
			charLimitLbl.getElement().setAttribute("style", "margin-left: 165px;");
		}else if(tabType.equals("T/F")){
			questionTypeHeader.setText(i18n.GL0353());
			questionTypeHeader.getElement().setAttribute("alt", i18n.GL0353());
			questionTypeHeader.getElement().setAttribute("title", i18n.GL0353());
			questionTypeText.setText(i18n.GL0354());
			questionTypeText.getElement().setAttribute("alt", i18n.GL0354());
			questionTypeText.getElement().setAttribute("title", i18n.GL0354());
			questionNameTextArea.markAsBlankPanel.setVisible(false);
			charLimitLbl.getElement().setAttribute("style", "margin-left: 165px;");
		}else if(tabType.equals("FIB")){
			questionTypeHeader.setText(i18n.GL0355());
			questionTypeHeader.getElement().setAttribute("alt", i18n.GL0355());
			questionTypeHeader.getElement().setAttribute("title", i18n.GL0355());
			questionTypeText.setText(i18n.GL0356());
			questionTypeText.getElement().setAttribute("alt", i18n.GL0356());
			questionTypeText.getElement().setAttribute("title", i18n.GL0356());
			charLimitLbl.getElement().setAttribute("style", "margin-left: 70px;");
		}else if(tabType.equals("OE")){
			questionTypeHeader.setText(i18n.GL0357());
			questionTypeHeader.getElement().setAttribute("alt", i18n.GL0357());
			questionTypeHeader.getElement().setAttribute("title", i18n.GL0357());
			questionTypeText.setText(i18n.GL0358());
			questionTypeText.getElement().setAttribute("alt", i18n.GL0358());
			questionTypeText.getElement().setAttribute("title", i18n.GL0358());
			questionNameTextArea.markAsBlankPanel.setVisible(false);
			charLimitLbl.getElement().setAttribute("style", "margin-left: 165px;");
		}
	}
	public boolean getQuestionEditMode(){
		return collectionItemDo!=null?true:false;
	}
	public void getHideRightsToolTip()
	{
	panelContentRights.setVisible(false);
	}
	public abstract void uploadQuestionImage();
	public abstract void hidePopup();
	public abstract void setEditQuestionImage(String fileName);
	
	public class ShowTinyMceToolBar implements ClickHandler{
		private TinyMCE tinyMce=null;
		public ShowTinyMceToolBar(TinyMCE tinyMce){
			this.tinyMce=tinyMce;
		}
		@Override
		public void onClick(ClickEvent event) {
			tinyMce.showTinyMceToolBar();
		}
		
	}
	
	/**
	 * Sets the added content to Edit and updating based on question type.
	 */
	
	protected void showEditQuestionResourceView(){
		TreeSet<QuestionAnswerDo> answerChoicesSet = collectionItemDo.getResource().getAnswers() != null ? collectionItemDo.getResource().getAnswers() : collectionItemDo.getQuestionInfo().getAnswers();
		Iterator<QuestionAnswerDo> it = answerChoicesSet.iterator(); 
		List<QuestionAnswerDo> questionAnswerDoList = new ArrayList<QuestionAnswerDo>();
		try{
			/**
			 *  If type = 4 from API, treated as FIB.
			 */
			int type = collectionItemDo.getResource().getType() != null ? collectionItemDo.getResource().getType() : collectionItemDo.getQuestionInfo().getType();
			String explanation = collectionItemDo.getResource().getExplanation() != null ? collectionItemDo.getResource().getExplanation() : collectionItemDo.getQuestionInfo().getExplanation();
			if(type==4){
				 while(it.hasNext()){
					 QuestionAnswerDo answer = it.next();
					 questionAnswerDoList.add(answer); 
				 }
				 String fillInTheBlankQuestion = collectionItemDo.getResource().getTitle();
				 String[] fibArray = fillInTheBlankQuestion.split(FIB_SEPARATOR);
				 List<String> questionArray = Arrays.asList(fibArray);
				 int answerArraySize = answerChoicesSet.size();
				 StringBuffer questionText = new StringBuffer();
				 for(int j = 0; j < questionArray.size(); j++) {
					 questionText.append(questionArray.get(j));
					 if(j<answerArraySize) {
							 questionText.append("["+questionAnswerDoList.get(j).getAnswerText()+"]");
					 }
				 }
				 questionNameTextArea.setText(questionText.toString());
				 questionNameTextArea.getElement().setAttribute("alt", questionText.toString());
				 questionNameTextArea.getElement().setAttribute("title", questionText.toString());
				 explainationTextArea.setText(explanation);
				 explainationTextArea.getElement().setAttribute("alt", explanation);
				 explainationTextArea.getElement().setAttribute("title", explanation);
			 }else{
				 questionNameTextArea.setText(collectionItemDo.getResource().getTitle());
				 questionNameTextArea.getElement().setAttribute("alt", collectionItemDo.getResource().getTitle());
				 questionNameTextArea.getElement().setAttribute("title", collectionItemDo.getResource().getTitle());
				 explainationTextArea.setText(explanation);
				 explainationTextArea.getElement().setAttribute("alt", explanation);
				 explainationTextArea.getElement().setAttribute("title", explanation);
			 }
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		TreeSet<QuestionHintsDo> hintsList = collectionItemDo.getResource().getHints() != null ? collectionItemDo.getResource().getHints() : collectionItemDo.getQuestionInfo().getHints();
		Iterator<QuestionHintsDo> iterator = hintsList.iterator();
		hintsContainer.clear();
		while (iterator.hasNext()) {
			QuestionHintsDo hints = iterator.next();
			int widgetCount=hintsContainer.getWidgetCount();
	        final AddHintsView addHints = new AddHintsView(widgetCount+1,hints.getHintText());
	        addHintsTextArea(addHints);
		}
		int type = collectionItemDo.getResource().getType() != null ? collectionItemDo.getResource().getType() : collectionItemDo.getQuestionInfo().getType();
		if(type==1){
			addResourceFormTitleChoice.setText(i18n.GL0864());
			addResourceFormTitleChoice.getElement().setAttribute("alt", i18n.GL0864());
			addResourceFormTitleChoice.getElement().setAttribute("title", i18n.GL0864());
			correctText.clear();
			correctText.getElement().setInnerHTML(i18n.GL0314());
			correctText.getElement().setAttribute("alt", i18n.GL0314());
			correctText.getElement().setAttribute("title", i18n.GL0314());
			setCorrectTextStyle();
			noLabelText.setVisible(false);
			questionAnswerChoiceContainer.clear();
			while (it.hasNext()) {
				QuestionAnswerDo answer = it.next();
				int widgetCount=questionAnswerChoiceContainer.getWidgetCount();
				
				final AddQuestionAnswerChoice addQuestionAnswer=new AddQuestionAnswerChoice(anserChoiceArray[widgetCount],answer.getAnswerText());
				addQuestionAnswer.optionNoButton.setStyleName(addWebResourceStyle.answerDeselected());
				if(answer.isIsCorrect()){	
					addQuestionAnswer.optionSelectedButton.setStyleName(addWebResourceStyle.answerSelected());
				}else{
					addQuestionAnswer.optionSelectedButton.setStyleName(addWebResourceStyle.answerDeselected());
				}
				addQuesetionAnswerOptionTextArea(addQuestionAnswer,widgetCount);
			}
		}else if(type==7){
			addResourceFormTitleChoice.setText("Enter answers and select correct ones *");
			addResourceFormTitleChoice.getElement().setAttribute("alt", "Enter answers and select correct ones *");
			addResourceFormTitleChoice.getElement().setAttribute("title", "Enter answers and select correct ones *");
			correctText.clear();
			correctText.getElement().setInnerHTML("Yes");
			correctText.getElement().setAttribute("alt", "Yes");
			correctText.getElement().setAttribute("title", "Yes");
			noLabelText.setVisible(true);
			noLabelText.getElement().setInnerHTML("No");
			noLabelText.getElement().setAttribute("alt", "No");
			noLabelText.getElement().setAttribute("title", "No");
			correctText.getElement().setAttribute("alt", "No");
			correctText.getElement().setAttribute("title", "No");
			setYesOrNoLabelStyles();
			questionAnswerChoiceContainer.clear();
			while (it.hasNext()) {
				QuestionAnswerDo answer = it.next();
				int widgetCount=questionAnswerChoiceContainer.getWidgetCount();
				final AddQuestionAnswerChoice addQuestionAnswer=new AddQuestionAnswerChoice(anserChoiceArray[widgetCount],answer.getAnswerText());
				addQuestionAnswer.optionNoButton.setStyleName(addWebResourceStyle.answerDeselected());
				addQuestionAnswer.showAnswerChoicesForMultipleAnswers();
				if(answer.isIsCorrect()){	
					addQuestionAnswer.optionSelectedButton.setStyleName(addWebResourceStyle.answerSelected());
				}else{
					addQuestionAnswer.optionNoButton.setStyleName(addWebResourceStyle.answerSelected());
				}
				addQuesetionAnswerOptionTextArea(addQuestionAnswer,widgetCount);
			}
		}else if(type==3){
			addResourceFormTitleChoice.setText(i18n.GL0864());
			addResourceFormTitleChoice.getElement().setAttribute("alt", i18n.GL0864());
			addResourceFormTitleChoice.getElement().setAttribute("title", i18n.GL0864());
			correctText.clear();
			correctText.getElement().setInnerHTML(i18n.GL0314());
			correctText.getElement().setAttribute("alt", i18n.GL0314());
			correctText.getElement().setAttribute("title", i18n.GL0314());
			setCorrectTextStyle();
			noLabelText.setVisible(false);
			setMultipleChoiceAnswerFields();
			int answerCount=0;
			while (it.hasNext()) {
				QuestionAnswerDo answer = it.next();
				selectTrueOrFallseCorrectAnswerOption(answerCount,answer.isIsCorrect());
				answerCount++;
			}
		}else{
			setMultipleChoiceAnswerFields();
		}
		/*if(collectionItemDo.getResource().getEducationalUse()!=null){
		for (checkboxSelectedDo item : collectionItemDo.getResource().getEducationalUse()) {			
			   if(item.isSelected()){
				    resourceEducationalLabel.setText(item.getValue());
					educationalUsePanel.setVisible(false);
					educationalDropDownLblOpen = false;
					mandatoryEducationalLbl.setVisible(false);
			   }
		}
		}*/
		if(collectionItemDo.getResource().getDepthOfKnowledges()!=null){
		int checkBoxCount=0;
		for (checkboxSelectedDo item : collectionItemDo.getResource().getDepthOfKnowledges()) {			
			   if(item.isSelected()){
				   if(checkBoxCount==0)
				   chkLevelRecall.setChecked(true);
				   if(checkBoxCount==1)
			       chkLevelSkillConcept.setChecked(true);
				   if(checkBoxCount==2)
			       chkLevelStrategicThinking.setChecked(true);
				   if(checkBoxCount==3)
			       chkLevelExtendedThinking.setChecked(true);
			   }
			   checkBoxCount++;
			}
		}
		
		if(collectionItemDo.getStandards()!=null){
			standardsPanel.clear();
			standardsDo.clear();
			String codeID="",code="",label="";
			for (Map<String, String> map: collectionItemDo.getStandards()) {
				 CodeDo codeObj=new CodeDo();
				for (Map.Entry<String, String> entry : map.entrySet()) {
					String key = entry.getKey();
					String values = entry.getValue();
					 if(key.contains("codeId")){
						 codeID=values;
						 codeObj.setCodeId(Integer.parseInt(values));
					 }
					 if(key.contains("code")){
						 code=values;
						 codeObj.setCode(values);
					 }
					 if(key.contains("description")){
						 label=values;
						 codeObj.setLabel(values);
					 }
					}
				 standardsDo.add(codeObj);
				 standardsPanel.add(createStandardLabel(code, codeID,label));
			}
		}
	}
	
	
	/**
	 * This method checks the FIB question is balanced or not i.e. balanced with open and closing brackets ([ ]) 
	 * 
	 * @param questionTextFIB
	 *                   is a user entered question.
	 *                   
	 * @return the Boolean value.
	 */
	
	private boolean isFIBQuestionBalanced(String questionTextFIB) {
		
		 Stack<Character> st = new Stack<Character>();

	        for(char chr : questionTextFIB.toCharArray())
	        {
	            switch(chr) {

	                case '[':
	                    st.push(chr);
	                    break;

	                case ']':
	                    if(st.isEmpty() || st.pop() != '[') {
	                    	
	                    	return false;
	                    }
	                    break;
	                        
	            }
	        }
	        return st.isEmpty();
	}
	
	
	/**
	 * This method will extract the blank answer (bracketed text) in the entered question.
	 * 
	 * @param questionTextFIB
	 *                 is a user entered question.
	 *                 
	 * @return matchList-FIB answers instance of {@link List} 
	 */
	
	private List<String> getFIBAnswers(String questionTextFIB) {
		List<String> matchList = new ArrayList<String>();
		
		String temp="";
		String ans="";
		for(int i=0;i<questionTextFIB.length();i++)
		{
			if(questionTextFIB.charAt(i)=='[')
			{
				temp="";
				int j=i;
				while((questionTextFIB.charAt(j)!=']'))
				{

					temp=temp+questionTextFIB.charAt(j);
					j=j+1;
				}
			
				if(temp.equalsIgnoreCase("[")){ 
					isAnsweEmpty = true;
				}else{
					isAnsweEmpty = false;
				}
				
				/**
				 * As question text is a raw content, replacing unnecessary spaces. 
				 */
				
				ans = temp.substring(1).replace("&nbsp;", "").trim(); 
				if(ans.equalsIgnoreCase("")){
					isAnsweEmpty = true;
				}else{
					isAnsweEmpty = false;
				}
				matchList.add(ans);

			}
		}
		return matchList;
	} 
	
	
	/**
	 * This method is used to replace all brackets ([], so called blanks) to underscores(_ replaced with 7 underscores).
	 * 
	 * @param rawQuest
	 *            is a user entered question.
	 *            
	 * @return rawQuest instance of {@link String}
	 */
	private String getFIBQuestion(String rawQuest) {
		String temp="";
		for(int i=0;i<rawQuest.length();i++)
		{
			if(rawQuest.charAt(i)=='[')
			{
				temp="";
				int j=i;
				while( (rawQuest.charAt(j)!=']'))
				{
					temp=temp+rawQuest.charAt(j);
					j=j+1;
				}
				rawQuest=rawQuest.replace(temp+"]", "_______").trim();
			}
		}
		return rawQuest;
	}
		
	/**
	 * On mouse over panelContentRights will be visible.
	 * 
	 * @param event instance of {@link MouseOverEvent}
	 */
	@UiHandler("lblContentRights")
	public void onMouseOver(MouseOverEvent event){
		panelContentRights.setVisible(true);
	}
	
	/**
	 * On mouse out panelContentRights will not be visible.
	 * 
	 * @param event instance of {@link MouseOutEvent}
	 */
	
	@UiHandler("lblContentRights")
	public void onMouseOut(MouseOutEvent event){
		panelContentRights.setVisible(false);
	}
	
	/**
	 * Opens up Terms of Use pop-up.
	 * 
	 * @param event instance of {@link ClickEvent}
	 */
	
	@UiHandler("termsAndPolicyAnr")
	public void onClickTrems(ClickEvent event){
		Window.enableScrolling(false);
		termsOfUse = new TermsOfUse() {
			@Override
			public void openParentPopup() {
				Window.enableScrolling(false);
				AppClientFactory.fireEvent(new SetHeaderZIndexEvent(98,false));
			}
		};
		
		termsOfUse.show();
		termsOfUse.setSize("902px", "300px");
		termsOfUse.center();
		termsOfUse.getElement().getStyle().setZIndex(999);
	
	}
	
	/**
	 * Opens up Privacy pop-up.
	 * 
	 * @param event instance of {@link ClickEvent}
	 */
	
	@UiHandler("privacyAnr")
	public void onClickPrivacy(ClickEvent event){
		Window.enableScrolling(false);
		termsAndPolicyVc = new TermsAndPolicyVc(false) {
			@Override
			public void openParentPopup() {
				Window.enableScrolling(false);
				AppClientFactory.fireEvent(new SetHeaderZIndexEvent(98,false));
			}
		};
		
		termsAndPolicyVc.show();
		termsAndPolicyVc.setSize("902px", "300px");
		termsAndPolicyVc.center();
		termsAndPolicyVc.getElement().getStyle().setZIndex(999);
	
	}
	
	/**
	 * Opens up Copy rights pop-up.
	 * 
	 * @param event instance of {@link ClickEvent}
	 */
	
	@UiHandler("copyRightAnr")
	public void onClickCopyright(ClickEvent event){
		Window.enableScrolling(false);
		copyRightPolicy = new  CopyRightPolicyVc() {
			@Override
			public void openParentPopup() {
				Window.enableScrolling(false);
				AppClientFactory.fireEvent(new SetHeaderZIndexEvent(98,false));
			}
		};
		
		copyRightPolicy.show();
		copyRightPolicy.setSize("902px", "300px");
		copyRightPolicy.center();
		copyRightPolicy.getElement().getStyle().setZIndex(999);
	}
	@UiHandler("commuGuideLinesAnr")
	public void onClickCommunityGuide(ClickEvent event){
		Window.open("http://support.goorulearning.org/hc/en-us/articles/200688506","_blank",""); 
	
	}
	/*@UiHandler("activityPanel")
	void activityPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_activity_selected");
		resourceEducationalLabel.setText(i18n.GL1665);
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
	}
	@UiHandler("handoutPanel")
	void handoutPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_handout_selected");
		resourceEducationalLabel.setText(i18n.GL0907);
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
	}
	@UiHandler("homeworkPanel")
	void homeworkPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_homework_selected");
		resourceEducationalLabel.setText(i18n.GL1666);
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
	}
	@UiHandler("gamePanel")
	void gamePanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_game_selected");
		resourceEducationalLabel.setText(i18n.GL1667);
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
	}
	@UiHandler("presentationPanel")
	void presentationPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_presentation_selected");
		resourceEducationalLabel.setText(i18n.GL1668);
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
	}
	@UiHandler("referenceMaterialPanel")
	void referenceMaterialPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_reference_material_selected");
		resourceEducationalLabel.setText(i18n.GL1669);
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
	}
	@UiHandler("quizPanel")
	void quizPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_quiz_selected");
		resourceEducationalLabel.setText(i18n.GL1670);
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
	}
	@UiHandler("curriculumPlanPanel")
	void curriculumPlanPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_curriculum_plan_selected");
		resourceEducationalLabel.setText(i18n.GL1671);
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
	}
	@UiHandler("lessonPlanPanel")
	void lessonPlanPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_lesson_plan_selected");
		resourceEducationalLabel.setText(i18n.GL1672);
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
	}
	@UiHandler("unitPlanPanel")
	void unitPlanPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_unit_plan_selected");
		resourceEducationalLabel.setText(i18n.GL1673);
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
	}
	@UiHandler("projectPlanPanel")
	void projectPlanPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_project_plan_selected");
		resourceEducationalLabel.setText(i18n.GL1674);
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
	}
	@UiHandler("readingPanel")
	void readingPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_reading_selected");
		resourceEducationalLabel.setText(i18n.GL1675);
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
	}
	@UiHandler("textbookPanel")
	void textbookPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_textbook_selected");
		resourceEducationalLabel.setText(i18n.GL0909);
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
	}
	@UiHandler("articlePanel")
	void articlePanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_article_selected");
		resourceEducationalLabel.setText(i18n.GL1676);
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
	}
	@UiHandler("bookPanel")
	void bookPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_book_selected");
		resourceEducationalLabel.setText(i18n.GL1677);
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
	}
	
	@UiHandler("educationalDropDownLbl")
	public void educationalDropDownClick(ClickEvent event) {
		if (educationalDropDownLblOpen == false) {
			educationalUsePanel.setVisible(true);
			educationalDropDownLblOpen = true;
		} else {
			educationalUsePanel.setVisible(false);
			educationalDropDownLblOpen = false;
		}
	}*/
	/**
     * Gets the name of the used browser.
     */
     public static native String getBrowserName() /*-{
         return navigator.userAgent.toLowerCase();
     }-*/;
     /*
     */
    /* public static boolean isFirefoxBrowser() {
         return getBrowserName().toLowerCase().contains("firefox");
     }
     public static boolean isIEBrowser() {
         return getBrowserName().toLowerCase().contains("msie");
     }*/
     
     public void setYesOrNoLabelStyles(){
    	 correctText.setStyleName(addWebResourceStyle.yesNoTextStyle());
    	 noLabelText.setStyleName(addWebResourceStyle.yesNoTextStyle());
     }
     
     public void setCorrectTextStyle(){
    	 correctText.setStyleName(addWebResourceStyle.addResourceFormTitleChoiceAlign());
     }
     public void addClickEventsForCheckBox(){
    	 chkLevelRecall.addClickHandler(new AddCheckBoxClickHandler());
    	 chkLevelSkillConcept.addClickHandler(new AddCheckBoxClickHandler());
    	 chkLevelStrategicThinking.addClickHandler(new AddCheckBoxClickHandler());
    	 chkLevelExtendedThinking.addClickHandler(new AddCheckBoxClickHandler());
    	 depthOfKnoweldgeToolTip.addMouseOverHandler(new MouseOverHandler() {
 			
 			@Override
 			public void onMouseOver(MouseOverEvent event) {
 				toolTip = new ToolTip(i18n.GL1734());
 				toolTip.getLblLink().setVisible(false);
 				toolTip.getElement().getStyle().setBackgroundColor("transparent");
 				toolTip.getElement().getStyle().setPosition(Position.ABSOLUTE);
 				toolTip.setPopupPosition(depthOfKnoweldgeToolTip.getAbsoluteLeft()-(50+22), depthOfKnoweldgeToolTip.getAbsoluteTop()+22);
 				toolTip.getElement().getStyle().setZIndex(1111);
 				toolTip.show();
 			}
 		});
 		depthOfKnoweldgeToolTip.addMouseOutHandler(new MouseOutHandler() {
 			
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
     }
     public class AddCheckBoxClickHandler implements ClickHandler  {

		@Override
		public void onClick(ClickEvent event) {
			 CheckBox checkBox = (CheckBox) event.getSource();
		     boolean checked = checkBox.getValue();		
		     
		     depthOfKnowledges.clear();
		    	
		     if(chkLevelRecall.isChecked())
		     {
		    	 checkboxSelectedDo depthObj=new checkboxSelectedDo();
			     depthObj.setSelected(true);
			     depthObj.setValue(chkLevelRecall.getText());
			     depthOfKnowledges.add(depthObj); 
		     }
		     else
		     {
		      	 checkboxSelectedDo depthObj=new checkboxSelectedDo();
			     depthObj.setSelected(false);
			     depthObj.setValue(chkLevelRecall.getText());
			     depthOfKnowledges.add(depthObj);  
		     }
		     
		     if(chkLevelSkillConcept.isChecked())
		     {
		    	 checkboxSelectedDo depthObj=new checkboxSelectedDo();
			     depthObj.setSelected(true);
			     depthObj.setValue(chkLevelSkillConcept.getText());
			     depthOfKnowledges.add(depthObj); 
		     }
		     else
		     {
		      	 checkboxSelectedDo depthObj=new checkboxSelectedDo();
			     depthObj.setSelected(false);
			     depthObj.setValue(chkLevelSkillConcept.getText());
			     depthOfKnowledges.add(depthObj);  
		     }
		     
		     if(chkLevelStrategicThinking.isChecked())
		     {
		    	 checkboxSelectedDo depthObj=new checkboxSelectedDo();
			     depthObj.setSelected(true);
			     depthObj.setValue(chkLevelStrategicThinking.getText());
			     depthOfKnowledges.add(depthObj); 
		     }
		     else
		     {
		      	 checkboxSelectedDo depthObj=new checkboxSelectedDo();
			     depthObj.setSelected(false);
			     depthObj.setValue(chkLevelStrategicThinking.getText());
			     depthOfKnowledges.add(depthObj);  
		     }
		     
		     if(chkLevelExtendedThinking.isChecked())
		     {
		    	 checkboxSelectedDo depthObj=new checkboxSelectedDo();
			     depthObj.setSelected(true);
			     depthObj.setValue(chkLevelExtendedThinking.getText());
			     depthOfKnowledges.add(depthObj); 
		     }
		     else
		     {
		      	 checkboxSelectedDo depthObj=new checkboxSelectedDo();
			     depthObj.setSelected(false);
			     depthObj.setValue(chkLevelExtendedThinking.getText());
			     depthOfKnowledges.add(depthObj);  
		     }
		     
		 /*    if(checked){
		    	 checkboxSelectedDo depthObj=new checkboxSelectedDo();
			     depthObj.setSelected(true);
			     depthObj.setValue(checkBox.getText());
			     depthOfKnowledges.add(depthObj);
		     }else{
		    	 for (checkboxSelectedDo currentElement : depthOfKnowledges) {
		    		if( currentElement.getValue().equalsIgnoreCase(checkBox.getText())){
		    			depthOfKnowledges.remove(currentElement);
		    		}
		    	}
		     }*/
		}
     }
     
     public abstract void callBrowseStandards();
     
     public abstract void closeStandardsPopup();
     
     public void setUpdatedBrowseStandarsCode(String standardsCodeVal, int id,String desc) {
		if (standardsPanel.getWidgetCount() <5) {
			if (standardsCodeVal != null && !standardsCodeVal.isEmpty()) {
				CodeDo codeObj=new CodeDo();
				codeObj.setCodeId(id);
				codeObj.setCode(standardsCodeVal);
				standardsDo.add(codeObj);
				standardsPanel.add(createStandardLabel(standardsCodeVal, Integer.toString(id), desc));
			}
		} else {
			standardMaxShow();
		}
		closeStandardsPopup();
	}
     public void DisableStandars(){
    	 browseStandardsTooltip=new BrowseStandardsTooltip("To see all standards, please edit your standards preference in","settings");
 		browseStandards.getElement().getStyle().setColor("#999");
 		browseStandards.getElement().addClassName("disabled");
 		browseStandards.addMouseOverHandler(new MouseOverHandler() {
 			@Override
 			public void onMouseOver(MouseOverEvent event) {
 					if(isBrowseTooltip == true){
 						browseStandardsTooltip.show();
 						browseStandardsTooltip.setPopupPosition(browseStandards.getAbsoluteLeft()+3, browseStandards.getAbsoluteTop()+33);
 						browseStandardsTooltip.getElement().getStyle().setZIndex(999999);
 						isBrowseStandardsToolTip= true;
 					}
 				}
 		});
 		
 		Event.addNativePreviewHandler(new NativePreviewHandler() {
 	        public void onPreviewNativeEvent(NativePreviewEvent event) {
 	        	hideBrowseStandardsPopup(event);
 	          }
 	    });
 	}
 	
 	public void hideBrowseStandardsPopup(NativePreviewEvent event){
 		try{
 			if(event.getTypeInt()==Event.ONMOUSEOVER){
 				Event nativeEvent = Event.as(event.getNativeEvent());
 				boolean target=eventTargetsPopup(nativeEvent);
 				if(!target)
 				{
 					if(isBrowseStandardsToolTip){
 						browseStandardsTooltip.hide();
 					}
 				}
 			}
 		}catch(Exception ex){ex.printStackTrace();}
 	}
 	
 	private boolean eventTargetsPopup(NativeEvent event) {
 		EventTarget target = event.getEventTarget();
 		if (Element.is(target)) {
 			try{
 				return browseStandardsTooltip.getElement().isOrHasChild(Element.as(target));
 			}catch(Exception ex){}
 		}
 		return false;
 	}

 	public void enableStandards(){
 		browseStandards.getElement().getStyle().clearColor();
 		browseStandards.getElement().removeClassName("disabled");
 	}
}
