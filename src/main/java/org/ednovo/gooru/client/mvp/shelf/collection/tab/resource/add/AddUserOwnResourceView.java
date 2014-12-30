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
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.effects.FadeInAndOut;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.addTagesPopup.AddTagesCBundle;
import org.ednovo.gooru.client.mvp.faq.CopyRightPolicyVc;
import org.ednovo.gooru.client.mvp.faq.TermsAndPolicyVc;
import org.ednovo.gooru.client.mvp.faq.TermsOfUse;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderZIndexEvent;
import org.ednovo.gooru.client.mvp.shelf.collection.CollectionCBundle;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.assign.CollectionAssignCBundle;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.item.AddSetupAdvancedCBundle;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.item.CollectionEditResourceCBundle;
import org.ednovo.gooru.client.uc.AppMultiWordSuggestOracle;
import org.ednovo.gooru.client.uc.AppSuggestBox;
import org.ednovo.gooru.client.uc.BlueButtonUc;
import org.ednovo.gooru.client.uc.CloseLabel;
import org.ednovo.gooru.client.uc.DownToolTipWidgetUc;
import org.ednovo.gooru.client.uc.StandardsPreferenceOrganizeToolTip;
import org.ednovo.gooru.client.uc.tooltip.BrowseStandardsTooltip;
import org.ednovo.gooru.client.ui.HTMLEventPanel;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.client.util.SetStyleForProfanity;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.code.CodeDo;
import org.ednovo.gooru.shared.model.content.CollectionDo;
import org.ednovo.gooru.shared.model.search.SearchDo;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public abstract class AddUserOwnResourceView extends Composite implements SelectionHandler<SuggestOracle.Suggestion> { 
	public interface AddUserOwnResourceViewUiBinder extends UiBinder<Widget,AddUserOwnResourceView> {
		
	}
	public static AddUserOwnResourceViewUiBinder uiBinder = GWT.create(AddUserOwnResourceViewUiBinder.class);
	
	private static MessageProperties i18n = GWT.create(MessageProperties.class);
	
	@UiField HTMLPanel loadingImagePanel,rightsContent,homeworkText,gameText,presentationText,referenceMaterialText,quizText,curriculumPlanText,lessonPlanText,
	unitPlanText,projectPlanText,readingText,textbookText,articleText,bookText,handoutText,mediaLabelContainer,educationalContainer,
	momentsOfLearningContainer,mediaFeatureContainer,accessHazardContainer,standardsBrowseContainer,mobileFriendlyContainer,mediaDropdownArrowConatainer;
	
	@UiField
	public Button cancelResourcePopupBtnLbl,uploadImageLbl,browseResourceBtn;
	@UiField
	public BlueButtonUc addResourceBtnLbl;

	@UiField
	Label resourceContentChkLbl, mandatoryTitleLbl,descCharcterLimit,standardsDefaultText,accessHazard,flashingHazard,motionSimulationHazard,soundHazard,mediaLabel;
	
	@UiField
	HTMLEventPanel lblContentRights;
	
	@UiField 
	org.ednovo.gooru.client.uc.HTMLEventPanel preparingTheLearningPanel,interactingWithTheTextPanel,activityPanel,extendingUnderstandingPanel,handoutPanel,homeworkPanel,gamePanel,presentationPanel,referenceMaterialPanel,quizPanel,curriculumPlanPanel,
	lessonPlanPanel,unitPlanPanel,projectPlanPanel,readingPanel,textbookPanel,articlePanel,bookPanel,defaultPanel,defaultPanelMomentsOfLearningPnl;


	@UiField
	public TextBox resourcePathTextBox, titleTextBox;

	@UiField
	public TextArea descriptionTxtAera;


	@UiField
	public Image setThumbnailImage;
	// Drop down for Resource Type//
	@UiField
	HTMLPanel panelContentRights,resourceTitleContainer,filePathContainer,thumbnailImageText,errorContainer,interactingWithTheTextText,extendingUnderstandingText;

	@UiField
	public HTMLPanel loadingPanel,htmlMediaFeatureListContainer,imageContainer,fileSizeLimit,titleText,descriptionText,categoryPanelText,imageText,textsPanelLabel,preparingTheLearningText,
	defaultText,defaultMomentsOfLearningText;

	@UiField
	HTMLPanel categorypanel,texts,image,educationalTitle,activityText,educationalpanel,educationalUsePanel;

	@UiField
	HTMLPanel resourceTypePanel, resourceDescriptionContainer,panelAction,momentsOfLearningTitle,momentsOfLearningpanel,momentsOfLearningPanel;

	@UiField
	Label resoureDropDownLbl, resourceCategoryLabel,rightsLbl,mandatoryDescLblForSwareWords,mandatoryTitleLblForSwareWords,resourcemomentsOfLearningLabel,momentsOfLearningDropDownLbl,mandatorymomentsOfLearninglLbl,resourceEducationalLabel;
	
	@UiField
	FormPanel fileuploadForm;
	
	
	@UiField
	FileUpload chooseResourceBtn;
	
	@UiField FlowPanel standardsPanel,standardContainer;
	
	@UiField
	CheckBox rightsChkBox;

	@UiField Label lblAdding,standardMaxMsg;
	@UiField InlineLabel agreeText,andText,additionalText,commuGuideLinesAnr, termsAndPolicyAnr,privacyAnr,copyRightAnr,moblieFriendly;
	@UiField org.ednovo.gooru.client.uc.HTMLEventPanel imageResourcePanel,textsResourcePanel,AdvancedSetupContainer,eHearderIconEducationalUse,eHearderIconMomentsOfLearning,eHearderIconstandards,
	eHearderIconAccessHazard,eHearderIconMediafeature,eHearderIconMobileFriendly;
	
	@UiField(provided = true)
	AppSuggestBox standardSgstBox;
	 
	@UiField Button browseStandards,mobileYes,mobileNo;
	
	@UiField
	public Label educationalDropDownLbl,mandatoryEducationalLbl,lblMediaFeatureArrow,lblMediaPlaceHolder;
	
	@UiField ScrollPanel spanelMediaFeaturePanel;
	
	@UiField InlineLabel advancedText;
	
	public AddSetupAdvancedView addSetupAdvancedView;
	
	
	/** 
	 * This method is to get the lblAdding
	 */
	public Label getLblAdding() {
		return lblAdding;
	}

	/** 
	 * This method is to set the lblAdding
	 */
	public void setLblAdding(Label lblAdding) {
		this.lblAdding = lblAdding;
	}
	
	private boolean isValidImageSize=true,isHavingBadWordsInTextbox=false,isHavingBadWordsInRichText=false;
	
	String originalFileName=null;
	
	private CopyRightPolicyVc copyRightPolicy;
	
	private TermsAndPolicyVc termsAndPolicyVc;
	private TermsOfUse termsOfUse;
	
	String thumbnailUrlStr = null;
	
	String filePath,resourceTitle,resourceDesc,resourceCategory;
	
	private static final String RESOURCE_UPLOAD_FILE_PATTERN = "([^\\s]+([^?#]*\\.(?:jpg|jpeg|pdf))$)";
	private static final String RESOURCE_FILE_SUPPORT_MSG = i18n.GL0955();
	
	private static final String IMAGE_UPLOAD_URL = "/v2/media?sessionToken={0}";
	boolean isEnabled = true;
	boolean isValidText=false,isValidTextArea=false,isValidFilePath=false;
	CollectionDo collectionDo;
	
	private AppMultiWordSuggestOracle standardSuggestOracle;
	
	private SearchDo<CodeDo> standardSearchDo = new SearchDo<CodeDo>();
	
	private Map<String, String> standardCodesMap = new HashMap<String, String>();
	
	List<String> standardPreflist;
	
	String courseCode="";
	
	final StandardsPreferenceOrganizeToolTip standardsPreferenceOrganizeToolTip=new StandardsPreferenceOrganizeToolTip();
	
	List<CodeDo> standardsDo=new ArrayList<CodeDo>();
	
	private static final String FLT_CODE_ID = "id";
	
	BrowseStandardsTooltip browseStandardsTooltip;
	
	private boolean isBrowseStandardsToolTip = false;
	
	private boolean isBrowseTooltip =false;
	
	private boolean hasClickedOnDropDwn=false;

	String mediaFeatureStr = i18n.GL1767();
	
	public boolean resoureDropDownLblOpen = false,educationalDropDownLblOpen=false,momentsOfLearningOpen=false;
	
	private static final String DEFAULT_COMBO_BOX_TEXT ="Please choose one of the following...";
	
	/**
	 *  Class constructor
	 *  
	 * @param collectionDo instance of {@link CollectionDo}
	 */
	
	public AddUserOwnResourceView(CollectionDo collectionDo){ 
		
		standardSuggestOracle = new AppMultiWordSuggestOracle(true);
		standardSearchDo.setPageSize(10);
		standardSgstBox = new AppSuggestBox(standardSuggestOracle) {
			@Override
			public void keyAction(String text) {
				text=text.toUpperCase();
				errorContainer.setVisible(false);
				//standardsPreferenceOrganizeToolTip.hide();
				standardSearchDo.setSearchResults(null);
				boolean standardsPrefDisplayPopup = false;
				//standardSgstBox.hideSuggestionList();
				if(!courseCode.isEmpty()) {
					Map<String,String> filters = new HashMap<String, String>();
					filters.put(FLT_CODE_ID,courseCode);
					standardSearchDo.setFilters(filters);
				}
				standardSearchDo.setQuery(text);
				if (text != null && text.trim().length() > 0) {
					//standardsPreferenceOrganizeToolTip.hide();
					errorContainer.setVisible(false);
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
					
						//standardsPreferenceOrganizeToolTip.hide();
						errorContainer.setVisible(false);
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
			*/			//standardSuggestOracle.add(i18n.i18n.GL1613);
					}
				}
			}

			@Override
			public HandlerRegistration addClickHandler(ClickHandler handler) {
				return null;
			}
		};
		BlurHandler blurHandler=new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				if(standardsPreferenceOrganizeToolTip.isShowing()){
				//standardsPreferenceOrganizeToolTip.hide();
					errorContainer.setVisible(false);
				}
			}
		};
		standardSgstBox.addDomHandler(blurHandler, BlurEvent.getType());
		standardSgstBox.addSelectionHandler(this);
		
		this.collectionDo = collectionDo;
		initWidget(uiBinder.createAndBindUi(this));
		mediaDropdownArrowConatainer.getElement().getStyle().setRight(10, Unit.PX);
		textsResourcePanel.addStyleName("active");
		resourceCategoryLabel.setText(i18n.GL1044());
		advancedText.setText(i18n.GL3096());
		mediaLabelContainer.getElement().getStyle().setMarginBottom(10, Unit.PX);
		addSetupAdvancedView = new AddSetupAdvancedView() {
			@Override
			public void showAndHideContainers() {
			}
		};
		AdvancedSetupContainer.add(addSetupAdvancedView);
		errorContainer.setVisible(false);
		errorContainer.add(standardsPreferenceOrganizeToolTip);
		standardContainer.getElement().setId("fpnlStandardContainer");
		standardsDefaultText.setText(i18n.GL1682());
		standardsDefaultText.getElement().setId("lblStandardsDefaultText");
		standardsDefaultText.getElement().setAttribute("alt", i18n.GL1682());
		standardsDefaultText.getElement().setAttribute("title", i18n.GL1682());
		standardSgstBox.getElement().setId("StandardSgstBox");
		browseStandards.addClickHandler(new onBrowseStandarsCLick());
		standardMaxMsg.getElement().setId("lblStandardMaxMsg");
		standardsPanel.getElement().setId("fpnlStandardsPanel");
		momentsOfLearningTitle.getElement().setInnerHTML(i18n.GL1678());
		momentsOfLearningTitle.getElement().setId("pnlMomentsOfLearningTitle");
		momentsOfLearningTitle.getElement().setAttribute("alt", i18n.GL1678());
		momentsOfLearningTitle.getElement().setAttribute("title", i18n.GL1678());
		momentsOfLearningpanel.getElement().setId("pnlMomentsOfLearningPanel");
		momentsOfLearningpanel.setVisible(false);
		resourcemomentsOfLearningLabel.setText(i18n.GL1684());
		resourcemomentsOfLearningLabel.getElement().setId("lblResourcemomentsOfLearningLabel");
		resourcemomentsOfLearningLabel.getElement().setAttribute("alt", i18n.GL1684());
		resourcemomentsOfLearningLabel.getElement().setAttribute("title", i18n.GL1684());
		momentsOfLearningDropDownLbl.getElement().setId("lblMomentsOfLearningDropDownLbl");
		mandatorymomentsOfLearninglLbl.getElement().setId("lblMandatorymomentsOfLearninglLbl");
		momentsOfLearningPanel.getElement().setId("pnlMomentsOfLearningPanel");
		momentsOfLearningPanel.setVisible(false);
		preparingTheLearningPanel.getElement().setId("epnlPreparingTheLearningPanel");
		preparingTheLearningText.getElement().setInnerHTML(i18n.GL1679());
		preparingTheLearningText.getElement().setId("pnlPreparingTheLearningText");
		preparingTheLearningText.getElement().setAttribute("alt", i18n.GL1679());
		preparingTheLearningText.getElement().setAttribute("title", i18n.GL1679());
		interactingWithTheTextPanel.getElement().setId("epnlInteractingWithTheTextPanel");
		interactingWithTheTextText.getElement().setInnerHTML(i18n.GL1680());
		interactingWithTheTextText.getElement().setId("pnlInteractingWithTheTextText");
		interactingWithTheTextText.getElement().setAttribute("alt", i18n.GL1680());
		interactingWithTheTextText.getElement().setAttribute("title", i18n.GL1680());
		extendingUnderstandingPanel.getElement().setId("epnlExtendingUnderstandingPanel");
		extendingUnderstandingText.getElement().setInnerHTML(i18n.GL1681());
		extendingUnderstandingText.getElement().setId("pnlExtendingUnderstandingText");
		extendingUnderstandingText.getElement().setAttribute("alt", i18n.GL1681());
		extendingUnderstandingText.getElement().setAttribute("title", i18n.GL1681());
		educationalTitle.getElement().setInnerHTML(i18n.GL1664());
		educationalTitle.getElement().setId("pnlEducationalTitle");
		educationalTitle.getElement().setAttribute("alt", i18n.GL1664());
		educationalTitle.getElement().setAttribute("title", i18n.GL1664());
		educationalpanel.getElement().setId("pnlEducationalpanel");
		resourceEducationalLabel.setText(i18n.GL1684());
		resourceEducationalLabel.getElement().setId("lblResourceEducationalLabel");
		resourceEducationalLabel.getElement().setAttribute("alt", i18n.GL1684());
		resourceEducationalLabel.getElement().setAttribute("title", i18n.GL1684());
		educationalDropDownLbl.getElement().setId("lblEducationalDropDownLbl");
		mandatoryEducationalLbl.getElement().setId("lblMandatoryEducationalLbl");
		educationalUsePanel.getElement().setId("pnlEducationalUsePanel");
		educationalUsePanel.setVisible(false);
		activityPanel.getElement().setId("epnlActivityPanel");
		activityText.getElement().setInnerHTML(i18n.GL1665());
		activityText.getElement().setId("pnlActivityText");
		activityText.getElement().setAttribute("alt", i18n.GL1665());
		activityText.getElement().setAttribute("title", i18n.GL1665());
		handoutPanel.getElement().setId("epnlHandoutPanel");
		homeworkPanel.getElement().setId("epnlHomeworkPanel");
		gamePanel.getElement().setId("epnlGamePanel");
		presentationPanel.getElement().setId("epnlPresentationPanel");
		referenceMaterialPanel.getElement().setId("epnlReferenceMaterialPanel");
		quizPanel.getElement().setId("epnlQuizPanel");
		curriculumPlanPanel.getElement().setId("epnlCurriculumPlanPanel");
		lessonPlanPanel.getElement().setId("epnlLessonPlanPanel");
		unitPlanPanel.getElement().setId("epnlUnitPlanPanel");
		projectPlanPanel.getElement().setId("epnlProjectPlanPanel");
		readingPanel.getElement().setId("epnlReadingPanel");
		textbookPanel.getElement().setId("epnlTextbookPanel");
		articlePanel.getElement().setId("epnlArticlePanel");
		bookPanel.getElement().setId("epnlBookPanel");
		handoutText.getElement().setInnerHTML(i18n.GL0907());
		handoutText.getElement().setId("pnlHandoutText");
		handoutText.getElement().setAttribute("alt", i18n.GL0907());
		handoutText.getElement().setAttribute("title", i18n.GL0907());
		homeworkText.getElement().setInnerHTML(i18n.GL1666());
		homeworkText.getElement().setId("pnlHomeworkText");
		homeworkText.getElement().setAttribute("alt", i18n.GL1666());
		homeworkText.getElement().setAttribute("title", i18n.GL1666());
		gameText.getElement().setInnerHTML(i18n.GL1667());
		gameText.getElement().setId("pnlGameText");
		gameText.getElement().setAttribute("alt", i18n.GL1667());
		gameText.getElement().setAttribute("title", i18n.GL1667());
		presentationText.getElement().setInnerHTML(i18n.GL1668());
		presentationText.getElement().setId("pnlPresentationText");
		presentationText.getElement().setAttribute("alt", i18n.GL1668());
		presentationText.getElement().setAttribute("title", i18n.GL1668());
		referenceMaterialText.getElement().setInnerHTML(i18n.GL1669());
		referenceMaterialText.getElement().setId("pnlReferenceMaterialText");
		referenceMaterialText.getElement().setAttribute("alt", i18n.GL1669());
		referenceMaterialText.getElement().setAttribute("title", i18n.GL1669());
		quizText.getElement().setInnerHTML(i18n.GL1670());
		quizText.getElement().setId("pnlQuizText");
		quizText.getElement().setAttribute("alt", i18n.GL1670());
		quizText.getElement().setAttribute("title", i18n.GL1670());
		curriculumPlanText.getElement().setInnerHTML(i18n.GL1671());
		curriculumPlanText.getElement().setId("pnlCurriculumPlanText");
		curriculumPlanText.getElement().setAttribute("alt", i18n.GL1671());
		curriculumPlanText.getElement().setAttribute("title", i18n.GL1671());
		lessonPlanText.getElement().setInnerHTML(i18n.GL1672());
		lessonPlanText.getElement().setId("pnlLessonPlanText");
		lessonPlanText.getElement().setAttribute("alt", i18n.GL1672());
		lessonPlanText.getElement().setAttribute("title", i18n.GL1672());
		unitPlanText.getElement().setInnerHTML(i18n.GL1673());
		unitPlanText.getElement().setId("pnlUnitPlanText");
		unitPlanText.getElement().setAttribute("alt", i18n.GL1673());
		unitPlanText.getElement().setAttribute("title", i18n.GL1673());
		projectPlanText.getElement().setInnerHTML(i18n.GL1674());
		projectPlanText.getElement().setId("pnlProjectPlanText");
		projectPlanText.getElement().setAttribute("alt", i18n.GL1674());
		projectPlanText.getElement().setAttribute("title", i18n.GL1674());
		readingText.getElement().setInnerHTML(i18n.GL1675());
		readingText.getElement().setId("pnlReadingText");
		readingText.getElement().setAttribute("alt", i18n.GL1675());
		readingText.getElement().setAttribute("title", i18n.GL1675());
		textbookText.getElement().setInnerHTML(i18n.GL0909());
		textbookText.getElement().setId("pnlTextbookText");
		textbookText.getElement().setAttribute("alt", i18n.GL0909());
		textbookText.getElement().setAttribute("title", i18n.GL0909());
		articleText.getElement().setInnerHTML(i18n.GL1676());
		articleText.getElement().setId("pnlArticleText");
		articleText.getElement().setAttribute("alt", i18n.GL1676());
		articleText.getElement().setAttribute("title", i18n.GL1676());
		bookText.getElement().setInnerHTML(i18n.GL1677());
		bookText.getElement().setId("pnlBookText");
		bookText.getElement().setAttribute("alt", i18n.GL1677());
		bookText.getElement().setAttribute("title", i18n.GL1677());
		spanelMediaFeaturePanel.setVisible(false);
		accessHazard.setText(i18n.GL1804());
		accessHazard.getElement().setId("lblAccessHazard");
		accessHazard.getElement().setAttribute("alt",i18n.GL1804());
		accessHazard.getElement().setAttribute("title",i18n.GL1804());
		accessHazard.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		
		flashingHazard.setText(i18n.GL1806());
		flashingHazard.getElement().setId("lblFlashingHazard");
		flashingHazard.getElement().setAttribute("alt",i18n.GL1806());
		flashingHazard.getElement().setAttribute("title",i18n.GL1806());
		
		motionSimulationHazard.setText(i18n.GL1808());
		motionSimulationHazard.getElement().setId("lblMotionSimulationHazard");
		motionSimulationHazard.getElement().setAttribute("alt",i18n.GL1808());
		motionSimulationHazard.getElement().setAttribute("title",i18n.GL1808());
		
		soundHazard.setText(i18n.GL1810());
		soundHazard.getElement().setId("lblSoundHazard");
		soundHazard.getElement().setAttribute("alt",i18n.GL1810());
		soundHazard.getElement().setAttribute("title",i18n.GL1810());
		
		mediaLabel.setText("Media Feature");
		mediaLabel.getElement().setId("lblMediaFeature");
		mediaLabel.getElement().setAttribute("alt","Media Feature");
		mediaLabel.getElement().setAttribute("title","Media Feature");
		mediaLabel.getElement().getStyle().setDisplay(Display.INLINE);
		
		lblMediaPlaceHolder.setText("Choose a Media Feature Option:");
		lblMediaPlaceHolder.getElement().setId("phMediaFeature");
		lblMediaPlaceHolder.getElement().setAttribute("alt","Choose a Media Feature Option:");
		lblMediaPlaceHolder.getElement().setAttribute("title","Choose a Media Feature Option:");
		
		spanelMediaFeaturePanel.setVisible(false);
		
		moblieFriendly.setText(i18n.GL1811());
		moblieFriendly.getElement().setId("spnMobileFriendly");
		moblieFriendly.getElement().setAttribute("alt",i18n.GL1811());
		moblieFriendly.getElement().setAttribute("title",i18n.GL1811());
		moblieFriendly.getElement().getStyle().setDisplay(Display.INLINE);
		
		mobileYes.setText(i18n.GL_GRR_YES());
		mobileYes.getElement().setId("btnYes");
		mobileYes.getElement().setAttribute("alt",i18n.GL_GRR_YES());
		mobileYes.getElement().setAttribute("title",i18n.GL_GRR_YES());
		
		mobileNo.setText(i18n.GL1735());
		mobileNo.getElement().setId("btnNo");
		mobileNo.getElement().setAttribute("alt",i18n.GL1735());
		mobileNo.getElement().setAttribute("title",i18n.GL1735());
		
		
		
		
		
		
		fileSizeLimit.getElement().setInnerHTML(" "+i18n.GL0901());
		fileSizeLimit.getElement().setId("pnlFileSizeLimit");
		fileSizeLimit.getElement().setAttribute("alt", i18n.GL0901());
		fileSizeLimit.getElement().setAttribute("title", i18n.GL0901());
		browseResourceBtn.setText(i18n.GL0902());
		browseResourceBtn.getElement().setId("btnBrowseResourceBtn");
		browseResourceBtn.getElement().setAttribute("alt", i18n.GL0902());
		browseResourceBtn.getElement().setAttribute("title", i18n.GL0902());
		titleText.getElement().setInnerHTML(i18n.GL0318());
		titleText.getElement().setId("pnlTitleText");
		titleText.getElement().setAttribute("alt", i18n.GL0318());
		titleText.getElement().setAttribute("title", i18n.GL0318());
		descriptionText.getElement().setInnerHTML(i18n.GL0904());
		descriptionText.getElement().setId("pnlDescriptionText");
		descriptionText.getElement().setAttribute("alt", i18n.GL0904());
		descriptionText.getElement().setAttribute("title", i18n.GL0904());
		categoryPanelText.getElement().setInnerHTML(i18n.GL3103()+i18n.GL_SPL_STAR());
		categoryPanelText.getElement().setId("pnlCategoryPanelText");
		categoryPanelText.getElement().setAttribute("alt", i18n.GL0906());
		categoryPanelText.getElement().setAttribute("title", i18n.GL0906());
		textsPanelLabel.getElement().setInnerHTML(i18n.GL1044());
		textsPanelLabel.getElement().setId("pnlTextsPanelLabel");
		textsPanelLabel.getElement().setAttribute("alt", i18n.GL1044());
		textsPanelLabel.getElement().setAttribute("title", i18n.GL1044());
		
		imageText.getElement().setInnerHTML(i18n.GL1046());
		imageText.getElement().setId("pnlImageText");
		imageText.getElement().setAttribute("alt", i18n.GL1046());
		imageText.getElement().setAttribute("title", i18n.GL1046());
		
		thumbnailImageText.getElement().setInnerHTML(i18n.GL0911());
		thumbnailImageText.getElement().setId("pnlThumbnailImageText");
		thumbnailImageText.getElement().setAttribute("alt", i18n.GL0911());
		thumbnailImageText.getElement().setAttribute("title", i18n.GL0911());
		
		uploadImageLbl.setText(i18n.GL0912());
		uploadImageLbl.getElement().setAttribute("alt", i18n.GL0912());
		uploadImageLbl.getElement().setAttribute("title", i18n.GL0912());
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
		andText.setText(i18n.GL_GRR_AND());
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
		cancelResourcePopupBtnLbl.setText(i18n.GL0142());
		cancelResourcePopupBtnLbl.getElement().setAttribute("alt", i18n.GL0142());
		cancelResourcePopupBtnLbl.getElement().setAttribute("title", i18n.GL0142());
		cancelResourcePopupBtnLbl.getElement().setAttribute("style", "margin-left:10px;");
		addResourceBtnLbl.setText(i18n.GL0590());
		addResourceBtnLbl.getElement().setAttribute("alt", i18n.GL0590());
		addResourceBtnLbl.getElement().setAttribute("title", i18n.GL0590());
		lblAdding.setText(i18n.GL0591().toLowerCase());
		lblAdding.getElement().setAttribute("alt", i18n.GL0591().toLowerCase());
		lblAdding.getElement().setAttribute("title", i18n.GL0591().toLowerCase());
		
		
		

		CollectionEditResourceCBundle.INSTANCE.css().ensureInjected();
		cancelResourcePopupBtnLbl.addClickHandler(new CloseClickHandler());
		addResourceBtnLbl.addClickHandler(new AddClickHandler());
		uploadImageLbl.addClickHandler(new OnEditImageClick());
		browseResourceBtn.addClickHandler(new OnBrowseBtnClick());
		rightsChkBox.addClickHandler(new OnRightsChecked());
		cancelResourcePopupBtnLbl.getElement().setId("btnCancel");
		rightsChkBox.getElement().setId("chkRights");
		uploadImageLbl.getElement().setId("btnUploadImage");
		addResourceBtnLbl.getElement().setId("btnAdd");
		resourcePathTextBox.getElement().setId("tbUrl");
		StringUtil.setAttributes(resourcePathTextBox, true);
		titleTextBox.getElement().setId("tbTitle");
		StringUtil.setAttributes(titleTextBox, true);
		descriptionTxtAera.getElement().setId("taDescription");
		StringUtil.setAttributes(descriptionTxtAera, true);
		descriptionTxtAera.getElement().setAttribute("placeholder", i18n.GL0359());
		resourcePathTextBox.addKeyUpHandler(new ResourcePathKeyUpHandler());
		titleTextBox.addKeyUpHandler(new TitleKeyUpHandler());
		descriptionTxtAera.addKeyUpHandler(new DescriptionKeyUpHandler());
		titleTextBox.getElement().setAttribute("maxlength", "50");
		descriptionTxtAera.getElement().setAttribute("maxlength", "300");
		resourceCategoryLabel.setText(i18n.GL0360());
		resourceCategoryLabel.getElement().setId("lblResourceCategoryLabel");
		resourceCategoryLabel.getElement().setAttribute("alt", i18n.GL0360());
		resourceCategoryLabel.getElement().setAttribute("title", i18n.GL0360());
		resourceContentChkLbl.setVisible(false);
		resourceContentChkLbl.getElement().setId("lblResourceContentChkLbl");
		loadingPanel.getElement().setId("pnlLoadingPanel");
		mandatoryTitleLbl.setVisible(false);
		mandatoryTitleLbl.getElement().setId("lblMandatoryTitleLbl");
		descCharcterLimit.getElement().setId("lblDescCharcterLimit");
		descCharcterLimit.setVisible(false);
		setThumbnailImage.getElement().setId("imgSetThumbnailImage");
		setThumbnailImage.setVisible(false);
		resourceTypePanel.setVisible(true);
		loadingPanel.setVisible(false);
		panelContentRights.setVisible(false);
		panelContentRights.getElement().setId("pnlPanelContentRights");
		imageContainer.getElement().setId("pnlImageContainer");
		imageContainer.getElement().getStyle().setDisplay(Display.NONE);
		rightsLbl.getElement().getStyle().setColor("black");
		chooseResourceBtn.getElement().setId("fileUpload1");
		filePathContainer.getElement().setId("pnlFilePathContainer");
		fileuploadForm.getElement().setId("fpFileuploadForm");
		resourceTitleContainer.getElement().setId("pnlResourceTitleContainer");
		mandatoryTitleLblForSwareWords.getElement().setId("lblMandatoryTitleLblForSwareWords");
		resourceDescriptionContainer.getElement().setId("pnlResourceDescriptionContainer");
		mandatoryDescLblForSwareWords.getElement().setId("lblMandatoryDescLblForSwareWords");
		categorypanel.getElement().setId("pnlCategorypanel");
		resoureDropDownLbl.getElement().setId("lblResoureDropDownLbl");
		resourceTypePanel.getElement().setId("pnlResourceTypePanel");
		imageResourcePanel.getElement().setId("epnlImageResourcePanel");
		image.getElement().setId("pnlImage");
		textsResourcePanel.getElement().setId("epnlTextsResourcePanel");
		texts.getElement().setId("pnlTexts");
		lblContentRights.getElement().setId("epnlLblContentRights");
		rightsContent.getElement().setId("pnlRightsContent");
		panelAction.getElement().setId("pnlPanelAction");
		loadingImagePanel.getElement().setId("pnlLoadingImagePanel");
		
		defaultText.getElement().setInnerHTML(i18n.GL3093());
		defaultMomentsOfLearningText.getElement().setInnerHTML(i18n.GL3093());
		clearFields();
		handelFormEvent();
		
		lblMediaFeatureArrow.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				OpenMediaFeatureDropdown();
			}
		});
		lblMediaPlaceHolder.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				OpenMediaFeatureDropdown();
			}
		});
		List<String> mediaFeatureList = Arrays.asList(mediaFeatureStr.split(","));
		for(int n=0; n<mediaFeatureList.size(); n++)
		{
				String mediaTitleVal = mediaFeatureList.get(n);
				
				final Label titleLabel = new Label(mediaTitleVal);
				titleLabel.setStyleName(CollectionAssignCBundle.INSTANCE.css().classpageTitleText());
				titleLabel.getElement().setAttribute("id", mediaTitleVal);
				//Set Click event for title
				titleLabel.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {		
						String optionSelected = titleLabel.getElement().getId();
						lblMediaPlaceHolder.setText(optionSelected);
						spanelMediaFeaturePanel.setVisible(false);
						lblMediaPlaceHolder.getElement().setId(titleLabel.getElement().getId());
						lblMediaPlaceHolder.setStyleName(CollectionAssignCBundle.INSTANCE.css().selectedClasspageText());
						lblMediaPlaceHolder.setText(optionSelected);
						setAdvancedOptionsStyles();
					}
				});
				htmlMediaFeatureListContainer.add(titleLabel);
		}
		HTMLEventPanel defaultMediaFeaturePnl = new HTMLEventPanel("");
		defaultMediaFeaturePnl.getElement().setClassName(CollectionEditResourceCBundle.INSTANCE.css().myFolderCollectionFolderVideoOuterContainer());
		HTMLPanel defaultMediaFeatureText = new HTMLPanel("");
		defaultMediaFeatureText.getElement().setInnerHTML(i18n.GL3093());
		defaultMediaFeatureText.getElement().setClassName(CollectionEditResourceCBundle.INSTANCE.css().myEducationalPanelSubTitles());
		defaultMediaFeatureText.getElement().addClassName(CollectionEditResourceCBundle.INSTANCE.css().setBorder());
		defaultMediaFeaturePnl.add(defaultMediaFeatureText);
		defaultMediaFeaturePnl.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				lblMediaPlaceHolder.setText(i18n.GL3051()+i18n.GL_SPL_SEMICOLON());
				spanelMediaFeaturePanel.setVisible(false);
				lblMediaPlaceHolder.setStyleName(CollectionAssignCBundle.INSTANCE.css().selectedClasspageText());
				setAdvancedOptionsStyles();
			}
		});
		htmlMediaFeatureListContainer.add(defaultMediaFeaturePnl);
		

		copyRightAnr.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
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
		});
		
		termsAndPolicyAnr.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
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
			
		});
		privacyAnr.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
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
			
		});
		commuGuideLinesAnr.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Window.open("http://support.goorulearning.org/hc/en-us/articles/200688506","_blank",""); 
			}
		});
		
		
		
		resourceCategoryLabel.setText(i18n.GL1044());
		resourceCategoryLabel.getElement().setAttribute("alt", i18n.GL1044());
		resourceCategoryLabel.getElement().setAttribute("title", i18n.GL1044());
		categorypanel.setStyleName(texts.getStyleName());
		resourceTypePanel.setVisible(true);
		resoureDropDownLblOpen = false;
		
		
		lblAdding.getElement().getStyle().setDisplay(Display.NONE);
		panelAction.getElement().getStyle().setDisplay(Display.BLOCK);
		titleTextBox.addBlurHandler(new CheckProfanityInOnBlur(titleTextBox, null, mandatoryTitleLblForSwareWords,resourcePathTextBox));
		descriptionTxtAera.addBlurHandler(new CheckProfanityInOnBlur(null, descriptionTxtAera, mandatoryDescLblForSwareWords,resourcePathTextBox));
	
		ClickHandler rootHandler= new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if(!hasClickedOnDropDwn){
					educationalUsePanel.setVisible(false);
					educationalDropDownLblOpen = false;
					momentsOfLearningPanel.setVisible(false);
					momentsOfLearningOpen = false;
					spanelMediaFeaturePanel.setVisible(false);
					
				}else{
					hasClickedOnDropDwn=false;
				}
				
			}
		};
		
		RootPanel.get().addDomHandler(rootHandler, ClickEvent.getType());
	
/** Add Advanced Setup Changes **/
		
		educationalContainer.setVisible(false);
		educationalUsePanel.setVisible(false);
		momentsOfLearningContainer.setVisible(false);
		momentsOfLearningPanel.setVisible(false);
		standardContainer.setVisible(false);
		mediaFeatureContainer.setVisible(false);
		accessHazardContainer.setVisible(false);
		standardsBrowseContainer.setVisible(false);
		mobileFriendlyContainer.setVisible(false);
		
		addSetupAdvancedView.educationUseAdvancedPnl.addClickHandler(new AddSetupAdvancedClickHandlers());
		addSetupAdvancedView.momentsOfLearningAdvancedPnl.addClickHandler(new AddSetupAdvancedClickHandlers());
		addSetupAdvancedView.standardsAdvancedPnl.addClickHandler(new AddSetupAdvancedClickHandlers());
		addSetupAdvancedView.accessHazardAdvancedPnl.addClickHandler(new AddSetupAdvancedClickHandlers());
		addSetupAdvancedView.mediaFeatureAdvancedPnl.addClickHandler(new AddSetupAdvancedClickHandlers());
		addSetupAdvancedView.mobileFreindlyAdvancedPnl.addClickHandler(new AddSetupAdvancedClickHandlers());
		
		eHearderIconEducationalUse.addClickHandler(new MinimizePanelsClickHandler());
		eHearderIconMomentsOfLearning.addClickHandler(new MinimizePanelsClickHandler());
		eHearderIconstandards.addClickHandler(new MinimizePanelsClickHandler());
		eHearderIconAccessHazard.addClickHandler(new MinimizePanelsClickHandler());
		eHearderIconMediafeature.addClickHandler(new MinimizePanelsClickHandler());
		eHearderIconMobileFriendly.addClickHandler(new MinimizePanelsClickHandler());
		
		/** Add Advanced Setup Changes End**/
	}
	
	private class MinimizePanelsClickHandler implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			addSetupAdvancedView.setUpLabel.setVisible(true);
			if(event.getSource()==eHearderIconEducationalUse){
				educationalContainer.setVisible(false);
				educationalUsePanel.setVisible(false);
				addSetupAdvancedView.educationUseAdvancedPnl.setVisible(true);
			}else if(event.getSource()==eHearderIconMomentsOfLearning){
				momentsOfLearningContainer.setVisible(false);
				momentsOfLearningPanel.setVisible(false);
				addSetupAdvancedView.momentsOfLearningAdvancedPnl.setVisible(true);
			}else if(event.getSource()==eHearderIconstandards){
				standardsBrowseContainer.setVisible(false);
				addSetupAdvancedView.standardsAdvancedPnl.setVisible(true);
			}else if(event.getSource()==eHearderIconAccessHazard){
				accessHazardContainer.setVisible(false);
				addSetupAdvancedView.accessHazardAdvancedPnl.setVisible(true);
			}else if(event.getSource()==eHearderIconMediafeature){
				mediaFeatureContainer.setVisible(false);
				addSetupAdvancedView.mediaFeatureAdvancedPnl.setVisible(true);
			}else if(event.getSource()==eHearderIconMobileFriendly){
				mobileFriendlyContainer.setVisible(false);
				addSetupAdvancedView.mobileFreindlyAdvancedPnl.setVisible(true);
			}
		}
	}
	
	private class AddSetupAdvancedClickHandlers implements ClickHandler{
		public AddSetupAdvancedClickHandlers() {
		}
		@Override
		public void onClick(ClickEvent event) {
			if(event.getSource()==addSetupAdvancedView.educationUseAdvancedPnl){
			educationalContainer.setVisible(true);
			educationalUsePanel.setVisible(true);
			addSetupAdvancedView.educationUseAdvancedPnl.setVisible(false);
			}else if(event.getSource()==addSetupAdvancedView.momentsOfLearningAdvancedPnl){
				momentsOfLearningContainer.setVisible(true);
				momentsOfLearningPanel.setVisible(true);
				addSetupAdvancedView.momentsOfLearningAdvancedPnl.setVisible(false);
			}else if(event.getSource()==addSetupAdvancedView.standardsAdvancedPnl){
				standardContainer.setVisible(true);
				standardsBrowseContainer.setVisible(true);
				addSetupAdvancedView.standardsAdvancedPnl.setVisible(false);
			}else if(event.getSource()==addSetupAdvancedView.accessHazardAdvancedPnl){
				accessHazardContainer.setVisible(true);
				addSetupAdvancedView.accessHazardAdvancedPnl.setVisible(false);
			}else if(event.getSource()==addSetupAdvancedView.mediaFeatureAdvancedPnl){
				mediaFeatureContainer.setVisible(true);
				addSetupAdvancedView.mediaFeatureAdvancedPnl.setVisible(false);
			}else if(event.getSource()==addSetupAdvancedView.mobileFreindlyAdvancedPnl){
				mobileFriendlyContainer.setVisible(true);
				addSetupAdvancedView.mobileFreindlyAdvancedPnl.setVisible(false);
			}
			
			if(isAllAdditionalTagsOpen()){
				addSetupAdvancedView.setUpLabel.setVisible(false);
			}
		}
	}
	
	
	/**
	 * This method will check all additional tags are open or not.
	 * 
	 * @return allAdditionalTagInVisisble
	 */
	public boolean isAllAdditionalTagsOpen() {
		
		boolean allAdditionalTagInVisisble = false;
		

		if (!addSetupAdvancedView.educationUseAdvancedPnl.isVisible()
				&& !addSetupAdvancedView.momentsOfLearningAdvancedPnl.isVisible()
				&& !addSetupAdvancedView.standardsAdvancedPnl.isVisible()
				&& !addSetupAdvancedView.accessHazardAdvancedPnl.isVisible()
				&& !addSetupAdvancedView.mediaFeatureAdvancedPnl.isVisible()
				&& !addSetupAdvancedView.mobileFreindlyAdvancedPnl.isVisible()) {
			allAdditionalTagInVisisble = true;
		}
		return allAdditionalTagInVisisble;
	}
	/**
	 * On click of this button can browse for file from local computer.
	 * 
	 * @param event is a instance of {@link ClickEvent}
	 */
	
	@UiHandler("chooseResourceBtn")
	public void onChangeFileUploadBtn(ChangeEvent event){
		if (!"".equalsIgnoreCase(chooseResourceBtn.getFilename())) {
			String size=getFileNameSize();
			double sizeOfImage=Double.parseDouble(size);
			if(sizeOfImage>5){
				isValidImageSize=false;
				resourceContentChkLbl.setText(i18n.GL0913());
				resourceContentChkLbl.getElement().setAttribute("alt", i18n.GL0913());
				resourceContentChkLbl.getElement().setAttribute("title", i18n.GL0913());
				resourceContentChkLbl.setVisible(true);
				fileuploadForm.reset();
				if(!resourcePathTextBox.getText().equalsIgnoreCase("")){
					resourcePathTextBox.setText("");
				}
			}
		}
	}
	
	private void handelFormEvent() {
		chooseResourceBtn.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				filePathContainer.setStyleName(CollectionEditResourceCBundle.INSTANCE.css().ownResourceFormInputControl());
				resourcePathTextBox.getElement().getStyle().setBorderColor("#dddddd");

				 if(hasValidateResource()){
					 isValidImageSize=true;
					 resourceContentChkLbl.setVisible(false);
					 resourcePathTextBox.setText(chooseResourceBtn.getFilename().trim());
					 resourcePathTextBox.getElement().setAttribute("alt", chooseResourceBtn.getFilename().trim());
					 resourcePathTextBox.getElement().setAttribute("title", chooseResourceBtn.getFilename().trim());
				 }
				 else{
					 if(!chooseResourceBtn.getFilename().trim().equalsIgnoreCase("")){
						 resourceContentChkLbl.setVisible(true);
						 resourceContentChkLbl.setText(RESOURCE_FILE_SUPPORT_MSG);
						 resourceContentChkLbl.getElement().setAttribute("alt", RESOURCE_FILE_SUPPORT_MSG);
						 resourceContentChkLbl.getElement().setAttribute("title", RESOURCE_FILE_SUPPORT_MSG);
					 }
					 
				 }
				
			}
		});
		
	}

	

	private class CloseClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			clearFields();
			hidePopup();
		}
	}
	public abstract void hidePopup();
	
	public void clearFields() {
		resourcePathTextBox.setText("");
		titleTextBox.setText("");
		descriptionTxtAera.setText("");
		setThumbnailImage.setUrl("");
		resourceCategoryLabel.setText(i18n.GL0360());
		resourceCategoryLabel.getElement().setAttribute("alt", i18n.GL0360());
		resourceCategoryLabel.getElement().setAttribute("title", i18n.GL0360());
		categorypanel.setStyleName("");
		mandatoryTitleLbl.setVisible(false);
		resourceContentChkLbl.setVisible(false);
		descCharcterLimit.setVisible(false);
		setVisible(true);
	}
	
	private class AddClickHandler implements ClickHandler {
		@SuppressWarnings("deprecation")
		@Override
		public void onClick(ClickEvent event) {
			addResourceBtnLbl.setEnabled(true);
			addResourceBtnLbl.getElement().addClassName("secondary");
			addResourceBtnLbl.getElement().removeClassName("primary");
			//addResourceBtnLbl.getElement().setAttribute("style", "background: #999;border: none;");
			final Map<String, String> parms = new HashMap<String, String>();
			parms.put("text", titleTextBox.getValue());
			AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {
				@Override
				public void onSuccess(Boolean value) {
						isHavingBadWordsInTextbox = value;
						if(value){
							SetStyleForProfanity.SetStyleForProfanityForTextBox(titleTextBox, mandatoryTitleLblForSwareWords,value);
							addResourceBtnLbl.setEnabled(true);
							addResourceBtnLbl.getElement().removeClassName("secondary");
							addResourceBtnLbl.getElement().addClassName("primary");
						}else{
							parms.put("text", descriptionTxtAera.getText());
							final List<String> tagList = new ArrayList<String>();
							AppClientFactory.getInjector().getResourceService().checkProfanity(parms,new SimpleAsyncCallback<Boolean>() {
								
								@Override
								public void onSuccess(Boolean result) {
									isHavingBadWordsInRichText=result;
									if(result){
										SetStyleForProfanity.SetStyleForProfanityForTextArea(descriptionTxtAera, mandatoryDescLblForSwareWords, result);
										addResourceBtnLbl.setEnabled(true);
										addResourceBtnLbl.getElement().removeClassName("secondary");
										addResourceBtnLbl.getElement().addClassName("primary");
									}else{
										
										MixpanelUtil.mixpanelEvent("Collaborator_edits_collection");
										//lblAdding.getElement().getStyle().setDisplay(Display.BLOCK);
										//panelAction.getElement().getStyle().setDisplay(Display.NONE);
										filePath = resourcePathTextBox.getText().trim();
										resourceTitle = titleTextBox.getText().trim();
										resourceDesc = descriptionTxtAera.getText().trim();
										resourceCategory = resourceCategoryLabel.getText();
										boolean isValidate = true;
										if(filePath==null || filePath.equals("")){
											 isValidate = false;
											 isEnabled = false;
											 resourcePathTextBox.setStyleName(CollectionEditResourceCBundle.INSTANCE.css().ownResourceFormInputControlForErrors());
											 resourceContentChkLbl.setText(i18n.GL0914());
											 resourceContentChkLbl.getElement().setAttribute("alt", i18n.GL0914());
											 resourceContentChkLbl.getElement().setAttribute("title", i18n.GL0914());
											 resourceContentChkLbl.setVisible(true);
										}
										if(resourceTitle==null || resourceTitle.equals("")){
											isValidate = false;
											isEnabled = false;
											resourceTitleContainer.setStyleName(CollectionEditResourceCBundle.INSTANCE.css().myFolderCollectionFormInputControlForErrors());
											mandatoryTitleLbl.setText(i18n.GL0903());
											mandatoryTitleLbl.getElement().setAttribute("alt", i18n.GL0903());
											mandatoryTitleLbl.getElement().setAttribute("title", i18n.GL0903());
											mandatoryTitleLbl.setVisible(true);
										}
										if(resourceDesc==null || resourceDesc.equals("")){
											isValidate = false;
											isEnabled = false;
											resourceDescriptionContainer.setStyleName(CollectionEditResourceCBundle.INSTANCE.css().myFolderCollectionFormInputControlForErrors());
											resourceDescriptionContainer.addStyleName(CollectionEditResourceCBundle.INSTANCE.css().myFolderCollectionFormTextarea());
										
											descCharcterLimit.setText(i18n.GL0905());
											descCharcterLimit.getElement().setAttribute("alt", i18n.GL0905());
											descCharcterLimit.getElement().setAttribute("title", i18n.GL0905());
											descCharcterLimit.setVisible(true);
										}
										if(resourceCategory==null || resourceCategory.equals("-1") || resourceCategory.equalsIgnoreCase("Choose a resource format") ){ 
											isValidate = false;
											isEnabled = false;
										}
										
										if(mobileYes.getStyleName().contains(AddTagesCBundle.INSTANCE.css().OffButtonsActive()))
										{
											
											tagList.add("Mobile Friendly : "+mobileYes.getText());
											
										}
										else if(mobileNo.getStyleName().contains(AddTagesCBundle.INSTANCE.css().OffButtonsActive()))
										{
										
											tagList.add("Mobile Friendly : "+mobileNo.getText());
											
										}
										if(!lblMediaPlaceHolder.getText().equalsIgnoreCase("Choose a Media Feature Option:"))
										{
											
											tagList.add(mediaLabel.getText()+" : "+lblMediaPlaceHolder.getText());
										
										}
										
										String hazardArr[] = setAccessHazards();
										
										if(hazardArr != null)
										{
											for(int i=0;i<hazardArr.length;i++)
											{
												//tagList.add('"' + hazardArr[i].toString() +'"');
												tagList.add(hazardArr[i].toString());
											}
										}
										if(resourceEducationalLabel.getText()!=null ||!resourceEducationalLabel.getText().trim().equalsIgnoreCase(""))
										{
											if(!resourceEducationalLabel.getText().trim().equalsIgnoreCase(DEFAULT_COMBO_BOX_TEXT)){
												tagList.add("Educational Use : "+resourceEducationalLabel.getText());
											}
											
										}
										
										
										if(!rightsChkBox.getValue()){
											rightsLbl.getElement().getStyle().setColor("orange");
											isValidate = false;
											isEnabled = false;
										}
										if(isValidate){
											addResourceBtnLbl.setEnabled(true);
											addResourceBtnLbl.getElement().removeClassName("secondary");
											addResourceBtnLbl.getElement().addClassName("primary");
											addResourceBtnLbl.getElement().setAttribute("style", "background: #1076BB;border: 1px solid #1076BB;");
											loadingImagePanel.clear();
											loadingImagePanel.add(setLoadingPanel());
											fileuploadForm.setAction(AppClientFactory.getLoggedInUser().getSettings().getRestEndPoint() + StringUtil.generateMessage(IMAGE_UPLOAD_URL, AppClientFactory.getLoggedInUser().getToken(), chooseResourceBtn.getFilename()));
											fileuploadForm.addFormHandler(new FormHandler() {
												
												public void onSubmitComplete(FormSubmitCompleteEvent event) {
													panelAction.getElement().getStyle().setDisplay(Display.NONE);
													lblAdding.getElement().getStyle().setDisplay(Display.BLOCK);
													loadingImagePanel.setVisible(false);
													if(isValidImageSize){
														if(collectionDo.getSharing().equalsIgnoreCase("public")){
															parseUploadFileDetails(event.getResults(),true,tagList);
														}
														else{
															parseUploadFileDetails(event.getResults(),false,tagList);
														}
													}
												}
												
												@Override
												public void onSubmit(FormSubmitEvent event) {
												
												}
											});
											fileuploadForm.submit();
											
											
											
											/* String str ="{\"deleteType\":\"DELETE\",\"deleteUrl\":\"media/3f4d6fb4-a42f-4f7c-b11d-28fb8f654194.jpg\",\"imageValidationMsg\":null,\"name\":\"3f4d6fb4-a42f-4f7c-b11d-28fb8f654194.jpg\",\"originalFilename\":\"6-004s09.jpg\",\"size\":72869,\"statusCode\":200,\"uploadImageSource\":\"local\",\"url\":\"http://qarepo.goorulearning.org/qalive/uploaded-media/3f4d6fb4-a42f-4f7c-b11d-28fb8f654194.jpg\"}";
							                 parseUploadFileDetails(str,false,tagList);*/
											
										}else{
											lblAdding.getElement().getStyle().setDisplay(Display.NONE);
											panelAction.getElement().getStyle().setDisplay(Display.BLOCK);
										/*	addResourceBtnLbl.setEnabled(true);*/
										}
									}
								}
							});
						}
				}
			});
		}

		protected void parseUploadFileDetails(String jsonString,boolean showPreview, List<String> tagList) {  
			if(jsonString!=null){
				JSONValue jsonParseValue=JSONParser.parseStrict(jsonString);
				JSONObject jsonObject=jsonParseValue.isObject();
				JSONValue jsonMediaFileValue=jsonObject.get("name");
				String mediaFileName=jsonMediaFileValue.isString().toString().replaceAll("^\"|\"$", "");
				JSONValue jsonOriginalFileValue=jsonObject.get("originalFilename");
				String originalFileName=jsonOriginalFileValue.isString().toString().replaceAll("^\"|\"$", "");
				if(resourceCategory.equalsIgnoreCase("Image")||resourceCategory.equalsIgnoreCase("Text"))
				{
					//resourceCategory=resourceCategory.substring(0, resourceCategory.length()-1);
					 if(resourceCategory.equalsIgnoreCase("Image")){
						 resourceCategory="Image";
					 }
				}
				if(showPreview){
					showResourcePreview(filePath,mediaFileName,originalFileName,resourceTitle,resourceDesc,resourceCategory,resourceEducationalLabel.getText(),resourcemomentsOfLearningLabel.getText(),standardsDo,tagList);
					addResourceBtnLbl.setEnabled(true);
				}else{
					addUserResource(filePath,mediaFileName,originalFileName,resourceTitle,resourceDesc,resourceCategory,resourceEducationalLabel.getText(),resourcemomentsOfLearningLabel.getText(),standardsDo,tagList);
					addResourceBtnLbl.setEnabled(true);
				}
				
			}
		}
	}
	

	public abstract void showResourcePreview(String filePath, String mediaFileName,String originalFileName,String resourceTitle, String resourceDesc, String resourceCategory, String educationalLevel, String momentsOfLearning, List<CodeDo> standardsDo, List<String> tagList);
	public abstract void addUserResource(String filePath,String mediaFileName,String originalFileName, String resourceTitle, String resourceDesc, String resourceCategory, String educationalLevel, String momentsOfLearning, List<CodeDo> standardsDo, List<String> tagList);
	
	
	private class OnEditImageClick implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			resourceImageUpload();
		}
	}
	
	private class OnBrowseBtnClick implements ClickHandler { 
		@Override
		public void onClick(ClickEvent event) {
//			resourceUpload();
		}
	}
	
	private class OnRightsChecked implements ClickHandler {
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
	
	public abstract void resourceImageUpload();
	public abstract void resourceUpload();
	
	private class ResourcePathKeyUpHandler implements KeyUpHandler {
		public void onKeyUp(KeyUpEvent event) {
			filePathContainer.setStyleName(CollectionEditResourceCBundle.INSTANCE.css().ownResourceFormInputControl());
			resourceContentChkLbl.setVisible(false);
			addResourceBtnLbl.setEnabled(true);
			addResourceBtnLbl.getElement().removeClassName("secondary");
			addResourceBtnLbl.getElement().addClassName("primary");
			/*addResourceBtnLbl.getElement().setAttribute("style", "background: #1076BB;border: 1px solid #1076BB;");*/
		}
	}
	
	private class TitleKeyUpHandler implements KeyUpHandler {

		public void onKeyUp(KeyUpEvent event) {
			addResourceBtnLbl.setEnabled(true);
			addResourceBtnLbl.getElement().removeClassName("secondary");
			addResourceBtnLbl.getElement().addClassName("primary");
			/*addResourceBtnLbl.getElement().setAttribute("style", "background: #1076BB;border: 1px solid #1076BB;");*/
			mandatoryTitleLbl.setVisible(false);
			resourceTitleContainer.setStyleName(CollectionEditResourceCBundle.INSTANCE.css().myFolderCollectionFormInputControl());
			if (titleTextBox.getText().length() >= 50) {
				mandatoryTitleLbl.setText(i18n.GL0143());
				mandatoryTitleLbl.getElement().setAttribute("alt", i18n.GL0143());
				mandatoryTitleLbl.getElement().setAttribute("title", i18n.GL0143());
				mandatoryTitleLbl.setVisible(true);
			}
		}
	}
	
	
	private class DescriptionKeyUpHandler implements KeyUpHandler {
		public void onKeyUp(KeyUpEvent event) {
			addResourceBtnLbl.setEnabled(true);
			addResourceBtnLbl.getElement().removeClassName("secondary");
			addResourceBtnLbl.getElement().addClassName("primary");
			/*addResourceBtnLbl.getElement().setAttribute("style", "background: #1076BB;border: 1px solid #1076BB;");*/
			descCharcterLimit.setVisible(false);
			resourceDescriptionContainer.setStyleName(CollectionEditResourceCBundle.INSTANCE.css().myFolderCollectionFormInputControl());
			resourceDescriptionContainer.addStyleName(CollectionEditResourceCBundle.INSTANCE.css().myFolderCollectionFormTextarea());
			if (descriptionTxtAera.getText().length() >= 300) {
//				descriptionTxtAera.setText(descriptionTxtAera.getText().trim().substring(0, 300));
				descCharcterLimit.setText(i18n.GL0143());
				descCharcterLimit.getElement().setAttribute("alt", i18n.GL0143());
				descCharcterLimit.getElement().setAttribute("title", i18n.GL0143());
				descCharcterLimit.setVisible(true);
			}

		}
	}

	@UiHandler("imageResourcePanel")
	void slideResourcePanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_image_selected");
		resourceCategoryLabel.setText(i18n.GL1046());
		resourceCategoryLabel.getElement().setAttribute("alt", i18n.GL1046());
		resourceCategoryLabel.getElement().setAttribute("title", i18n.GL1046());
		categorypanel.setStyleName(image.getStyleName());
		resourceTypePanel.setVisible(true);
		resoureDropDownLblOpen = false;
		imageResourcePanel.addStyleName("active");
		textsResourcePanel.removeStyleName("active");
	}

	@UiHandler("textsResourcePanel")
	void handoutResourcePanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_text_selected");
		resourceCategoryLabel.setText(i18n.GL1044());
		resourceCategoryLabel.getElement().setAttribute("alt", i18n.GL1044());
		resourceCategoryLabel.getElement().setAttribute("title", i18n.GL1044());
		categorypanel.setStyleName(texts.getStyleName());
		resourceTypePanel.setVisible(true);
		resoureDropDownLblOpen = false;
		textsResourcePanel.addStyleName("active");
		imageResourcePanel.removeStyleName("active");
	}

	/*@UiHandler("textbookResourcePanel")
	void textbookResourcePanel(ClickEvent event) {
		resourceCategoryLabel.setText(i18n.GL0909);
		categorypanel.setStyleName(textbook.getStyleName());
		resourceTypePanel.setVisible(false);
		resoureDropDownLblOpen = false;
	}*/

	/*@UiHandler("lessonResourcePanel")
	void lessonResourcePanel(ClickEvent event) {
		resourceCategoryLabel.setText(i18n.GL0910);
		categorypanel.setStyleName(lesson.getStyleName());
		resourceTypePanel.setVisible(false);
		resoureDropDownLblOpen = false;
	}*/


	/*@UiHandler("resoureDropDownLbl")
	public void dropDownClick(ClickEvent event) {
		if (resoureDropDownLblOpen == false) {
			resourceTypePanel.setVisible(true);
			resoureDropDownLblOpen = true;

		} else {
			resourceTypePanel.setVisible(false);
			resoureDropDownLblOpen = false;
		}

	}*/
	
	@UiHandler("lblContentRights")
	public void onMouseOver(MouseOverEvent event){
		panelContentRights.setVisible(true);
	}
	
	@UiHandler("lblContentRights")
	public void onMouseOut(MouseOutEvent event){
		panelContentRights.setVisible(false);
	}
	
	public boolean hasValidateResource(){
		boolean isValid = true;
		String uploadResourceName = chooseResourceBtn.getFilename();
		try {
			RegExp reg = RegExp.compile(RESOURCE_UPLOAD_FILE_PATTERN, "gi");
			if(uploadResourceName != null && !reg.test(uploadResourceName)){
				return isValid = false;
			}
		}catch (Exception e) {
			
		}
		return isValid;
	}
	
	
	/**
	 * To get the upload file size from client end
	 * @return it will return the upload file size in mb
	 */
	 
	public final native String getFileNameSize() /*-{
		var fileSize;
		if ($wnd.$.browser.msie) 
		{
			var objFSO = new ActiveXObject("Scripting.FileSystemObject");
			var sPath =   $wnd.$("#fileUpload1")[0].value;
			var objFile = objFSO.getFile(sPath);
			var iSize = objFile.size;
			fileSize = iSize/ 1048576;
		}
		else 
		{
			fileSize =  $wnd.$("#fileUpload1")[0].files[0].size ;//size in kb
			fileSize = fileSize / 1048576; //size in mb 
		}
		return fileSize.toString();
	}-*/;
	public class CheckProfanityInOnBlur implements BlurHandler{
		private TextBox textBox;
		private Label label;
		private TextArea textArea;
		private TextBox filePathVal;
		
		public CheckProfanityInOnBlur(TextBox textBox,TextArea textArea,Label label,TextBox path){
			this.textBox=textBox;
			this.label=label;
			this.textArea=textArea;
			this.filePathVal=path;
		}
		@Override
		public void onBlur(BlurEvent event) {
			Map<String, String> parms = new HashMap<String, String>();
			if(textBox!=null){
				parms.put("text", textBox.getValue());
			}else{
				parms.put("text", textArea.getText());
			}
			addResourceBtnLbl.setEnabled(true);
			addResourceBtnLbl.getElement().removeClassName("secondary");
			addResourceBtnLbl.getElement().addClassName("primary");
			AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {

				@Override
				public void onSuccess(Boolean value) {
					addResourceBtnLbl.setEnabled(true);
					if(textBox!=null){
						isHavingBadWordsInTextbox = value;
						SetStyleForProfanity.SetStyleForProfanityForTextBox(textBox, label, value);
						isEnabled = false;
						if(value || textBox.getValue().trim().equalsIgnoreCase("")){
							isValidText=false;
						}else{
							isValidText=true;
						}
					
					}else{
						isHavingBadWordsInRichText=value;
						SetStyleForProfanity.SetStyleForProfanityForTextArea(textArea, label, value);
						isEnabled = false;
						if(value || textArea.getText().trim().equalsIgnoreCase("")){
							isValidTextArea=false;
						}else{
							isValidTextArea=true;
						}
					}
					if(filePathVal.getText().trim().equalsIgnoreCase("")){
						isValidFilePath =false;
					}else{
						isValidFilePath =true;
					}
					/*if(isValidText && isValidTextArea && isValidFilePath){
						addResourceBtnLbl.setEnabled(true);
						addResourceBtnLbl.getElement().setAttribute("style", "background: #1076BB;border: 1px solid #1076BB;");
					}else{
						addResourceBtnLbl.setEnabled(false);
						addResourceBtnLbl.getElement().setAttribute("style", "background: #999;border: none;");
					}*/
				}
			});
		}
	}
	public Label setLoadingPanel(){
		Label loadingImage=new Label();
		loadingImage.setStyleName(CollectionEditResourceCBundle.INSTANCE.css().loadingpanelImage());
		return loadingImage;
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
				codeObj.setCodeId(Integer.parseInt(getCodeIdByCode(standardSgstBox.getValue(), standardSearchDo.getSearchResults())));
				codeObj.setCode(standardSgstBox.getValue());
				standardsDo.add(codeObj);
				standardsPanel.add(createStandardLabel(standard, id, standardCodesMap.get(id)));
			}
		} else {
			standardMaxShow();
			standardSgstBox.setText("");
		}
	}
	
	public void standardMaxShow() {
		standardSgstBox.addStyleName(CollectionCBundle.INSTANCE.css().standardTxtBox());
		standardMaxMsg.setStyleName(CollectionCBundle.INSTANCE.css().standardMax());
		standardsPanel.addStyleName(CollectionCBundle.INSTANCE.css().floatLeftNeeded());
		new FadeInAndOut(standardMaxMsg.getElement(), 5000, 5000);
	}
	
	@Override
	public void onSelection(SelectionEvent<Suggestion> event) {
		
		addStandard(standardSgstBox.getValue(), getCodeIdByCode(standardSgstBox.getValue(), standardSearchDo.getSearchResults()));
		standardSgstBox.setText("");
		standardSuggestOracle.clear();
		updateStandardsAdvancedSetupStyle();
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
				this.getParent().removeFromParent();
				updateStandardsAdvancedSetupStyle();
			}
		};
		return new DownToolTipWidgetUc(closeLabel, description);
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
	
	private class onBrowseStandarsCLick implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			browseStandardsInfo();
		}
	}
	/**
	 * 
	 * @function DisableStandars 
	 * 
	 * @created_date : 15-Dec-2014
	 * 
	 * @description    This method is used to disable the standrds based on user selected standards from settings
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
	/**
	 * 
	 * @function enableStandards 
	 * 
	 * @created_date : 15-Dec-2014
	 * 
	 * @description  This method is used to enable the standrds based on user selected standards from settings
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
	public void enableStandards(){
		browseStandards.getElement().getStyle().clearColor();
		browseStandards.getElement().removeClassName("disabled");
	}
	/**
	 * 
	 * @param standardsCodeVal
	 * @param id
	 * @param desc
	 */
	public void setUpdatedBrowseStandarsCode(String standardsCodeVal,int id,String desc) {
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
		updateStandardsAdvancedSetupStyle();
	}
	@UiHandler("defaultPanel")
	void defaultPanel(ClickEvent event) {
		resourceEducationalLabel.setText(i18n.GL1684());
		resourceEducationalLabel.getElement().setAttribute("alt", i18n.GL1684());
		resourceEducationalLabel.getElement().setAttribute("title", i18n.GL1684());
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	@UiHandler("activityPanel")
	void activityPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_activity_selected");
		resourceEducationalLabel.setText(i18n.GL1665());
		resourceEducationalLabel.getElement().setAttribute("alt", i18n.GL1665());
		resourceEducationalLabel.getElement().setAttribute("title", i18n.GL1665());
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	@UiHandler("handoutPanel")
	void handoutPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_handout_selected");
		resourceEducationalLabel.setText(i18n.GL0907());
		resourceEducationalLabel.getElement().setAttribute("alt", i18n.GL0907());
		resourceEducationalLabel.getElement().setAttribute("title", i18n.GL0907());
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	@UiHandler("homeworkPanel")
	void homeworkPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_homework_selected");
		resourceEducationalLabel.setText(i18n.GL1666());
		resourceEducationalLabel.getElement().setAttribute("alt", i18n.GL1666());
		resourceEducationalLabel.getElement().setAttribute("title", i18n.GL1666());
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	@UiHandler("gamePanel")
	void gamePanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_game_selected");
		resourceEducationalLabel.setText(i18n.GL1667());
		resourceEducationalLabel.getElement().setAttribute("alt", i18n.GL1667());
		resourceEducationalLabel.getElement().setAttribute("title", i18n.GL1667());
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	@UiHandler("presentationPanel")
	void presentationPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_presentation_selected");
		resourceEducationalLabel.setText(i18n.GL1668());
		resourceEducationalLabel.getElement().setAttribute("alt", i18n.GL1668());
		resourceEducationalLabel.getElement().setAttribute("title", i18n.GL1668());
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	@UiHandler("referenceMaterialPanel")
	void referenceMaterialPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_reference_material_selected");
		resourceEducationalLabel.setText(i18n.GL1669());
		resourceEducationalLabel.getElement().setAttribute("alt", i18n.GL1669());
		resourceEducationalLabel.getElement().setAttribute("title", i18n.GL1669());
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	@UiHandler("quizPanel")
	void quizPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_quiz_selected");
		resourceEducationalLabel.setText(i18n.GL1670());
		resourceEducationalLabel.getElement().setAttribute("alt", i18n.GL1670());
		resourceEducationalLabel.getElement().setAttribute("title", i18n.GL1670());
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	@UiHandler("curriculumPlanPanel")
	void curriculumPlanPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_curriculum_plan_selected");
		resourceEducationalLabel.setText(i18n.GL1671());
		resourceEducationalLabel.getElement().setAttribute("alt", i18n.GL1671());
		resourceEducationalLabel.getElement().setAttribute("title", i18n.GL1671());
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	@UiHandler("lessonPlanPanel")
	void lessonPlanPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_lesson_plan_selected");
		resourceEducationalLabel.setText(i18n.GL1672());
		resourceEducationalLabel.getElement().setAttribute("alt", i18n.GL1672());
		resourceEducationalLabel.getElement().setAttribute("title", i18n.GL1672());
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	@UiHandler("unitPlanPanel")
	void unitPlanPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_unit_plan_selected");
		resourceEducationalLabel.setText(i18n.GL1673());
		resourceEducationalLabel.getElement().setAttribute("alt", i18n.GL1673());
		resourceEducationalLabel.getElement().setAttribute("title", i18n.GL1673());
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	@UiHandler("projectPlanPanel")
	void projectPlanPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_project_plan_selected");
		resourceEducationalLabel.setText(i18n.GL1674());
		resourceEducationalLabel.getElement().setAttribute("alt", i18n.GL1674());
		resourceEducationalLabel.getElement().setAttribute("title", i18n.GL1674());
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	@UiHandler("readingPanel")
	void readingPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_reading_selected");
		resourceEducationalLabel.setText(i18n.GL1675());
		resourceEducationalLabel.getElement().setAttribute("alt", i18n.GL1675());
		resourceEducationalLabel.getElement().setAttribute("title", i18n.GL1675());
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	@UiHandler("textbookPanel")
	void textbookPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_textbook_selected");
		resourceEducationalLabel.setText(i18n.GL0909());
		resourceEducationalLabel.getElement().setAttribute("alt", i18n.GL0909());
		resourceEducationalLabel.getElement().setAttribute("title", i18n.GL0909());
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	@UiHandler("articlePanel")
	void articlePanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_article_selected");
		resourceEducationalLabel.setText(i18n.GL1676());
		resourceEducationalLabel.getElement().setAttribute("alt", i18n.GL1676());
		resourceEducationalLabel.getElement().setAttribute("title", i18n.GL1676());
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	@UiHandler("bookPanel")
	void bookPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_book_selected");
		resourceEducationalLabel.setText(i18n.GL1677());
		resourceEducationalLabel.getElement().setAttribute("alt", i18n.GL1677());
		resourceEducationalLabel.getElement().setAttribute("title", i18n.GL1677());
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	
	@UiHandler("educationalDropDownLbl")
	public void educationalDropDownClick(ClickEvent event) {
		hasClickedOnDropDwn=true;
		if (educationalDropDownLblOpen == false) {
			educationalUsePanel.setVisible(true);
			educationalDropDownLblOpen = true;
		} else {
			educationalUsePanel.setVisible(false);
			educationalDropDownLblOpen = false;
		}
	}
	@UiHandler("defaultPanelMomentsOfLearningPnl")
	void defaultPanelMomentsOfLearningPnl(ClickEvent event) {
		resourcemomentsOfLearningLabel.setText(i18n.GL1684());
		resourcemomentsOfLearningLabel.getElement().setAttribute("alt", i18n.GL1684());
		resourcemomentsOfLearningLabel.getElement().setAttribute("title", i18n.GL1684());
		momentsOfLearningPanel.setVisible(false);
		momentsOfLearningOpen = false;
		mandatorymomentsOfLearninglLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	
	@UiHandler("preparingTheLearningPanel")
	void preparingTheLearningPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_preparing_the_learning_selected");
		resourcemomentsOfLearningLabel.setText(i18n.GL1679());
		resourcemomentsOfLearningLabel.getElement().setAttribute("alt", i18n.GL1679());
		resourcemomentsOfLearningLabel.getElement().setAttribute("title", i18n.GL1679());
		momentsOfLearningPanel.setVisible(false);
		momentsOfLearningOpen = false;
		mandatorymomentsOfLearninglLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	@UiHandler("interactingWithTheTextPanel")
	void interactingWithTheTextPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_interacting_with_the_text_selected");
		resourcemomentsOfLearningLabel.setText(i18n.GL1680());
		resourcemomentsOfLearningLabel.getElement().setAttribute("alt", i18n.GL1680());
		resourcemomentsOfLearningLabel.getElement().setAttribute("title", i18n.GL1680());
		momentsOfLearningPanel.setVisible(false);
		momentsOfLearningOpen = false;
		mandatorymomentsOfLearninglLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	@UiHandler("extendingUnderstandingPanel")
	void extendingUnderstandingPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_extending_Understanding_selected");
		resourcemomentsOfLearningLabel.setText(i18n.GL1681());
		resourcemomentsOfLearningLabel.getElement().setAttribute("alt", i18n.GL1681());
		resourcemomentsOfLearningLabel.getElement().setAttribute("title", i18n.GL1681());
		momentsOfLearningPanel.setVisible(false);
		momentsOfLearningOpen = false;
		mandatorymomentsOfLearninglLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	@UiHandler("momentsOfLearningDropDownLbl")
	public void momentsOfLearningDropDownClick(ClickEvent event) {
		hasClickedOnDropDwn=true;
		if (momentsOfLearningOpen == false) {
			momentsOfLearningPanel.setVisible(true);
			momentsOfLearningOpen = true;
		} else {
			momentsOfLearningPanel.setVisible(false);
			momentsOfLearningOpen = false;
		}
	}
	
	private void OpenMediaFeatureDropdown() {
		hasClickedOnDropDwn=true;
		if (spanelMediaFeaturePanel.isVisible()){
			spanelMediaFeaturePanel.setVisible(false);
		}else{
			spanelMediaFeaturePanel.setVisible(true);
		}
	}
	
	/*
	 * method for select access hazards
	 */
	public String[] setAccessHazards()
	{
		String[] accessHazardsArr = null;
		List<String> accessHazardsSelected = new ArrayList<String>();
		
		if(flashingHazard.getElement().getClassName().contains("select"))
		{
			String hazardsStr = accessHazard.getText()+" : "+flashingHazard.getText();
			//String hazardsStr = flashingHazard.getText();
			accessHazardsSelected.add(hazardsStr);
		}
		if(motionSimulationHazard.getElement().getClassName().contains("select"))
		{
			String hazardsStr = accessHazard.getText()+" : "+motionSimulationHazard.getText();
			//String hazardsStr = motionSimulationHazard.getText();
			accessHazardsSelected.add(hazardsStr);
		}
		if(soundHazard.getElement().getClassName().contains("select"))
		{
			String hazardsStr = accessHazard.getText()+" : "+soundHazard.getText();
			//String hazardsStr = soundHazard.getText();
			accessHazardsSelected.add(hazardsStr);
		}
		
		accessHazardsArr = accessHazardsSelected.toArray(new String[accessHazardsSelected.size()]);
		setAdvancedAccessHazardStyles(accessHazardsArr.length);
		return accessHazardsArr;
	}
	
	@UiHandler("flashingHazard")
	public void onflashingHazardClick(ClickEvent click){
		if(flashingHazard.getStyleName().toString().contains("select"))
		{
			flashingHazard.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().select());
		}
		else
		{
			flashingHazard.getElement().addClassName(AddTagesCBundle.INSTANCE.css().select());
		}
		setAccessHazards();
	}

	@UiHandler("motionSimulationHazard")
	public void onmotionSimulationHazardClick(ClickEvent click){
		if(motionSimulationHazard.getStyleName().toString().contains("select"))
		{
			motionSimulationHazard.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().select());
		}
		else
		{
			motionSimulationHazard.getElement().addClassName(AddTagesCBundle.INSTANCE.css().select());
		}
		setAccessHazards();
	}

	@UiHandler("soundHazard")
	public void onsoundHazardClick(ClickEvent click){
		if(soundHazard.getStyleName().toString().contains("select"))
		{
			soundHazard.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().select());
		}
		else
		{
			soundHazard.getElement().addClassName(AddTagesCBundle.INSTANCE.css().select());
		}
		setAccessHazards();
	}
	
	public void setMobileFriendlyObjectVal(String mobileFriendlyVal)
	{
		if(mobileFriendlyVal.contains(mobileYes.getText()))
		{
			mobileYes.getElement().setClassName(AddTagesCBundle.INSTANCE.css().OffButtonsActive());
			mobileNo.getElement().setClassName(AddTagesCBundle.INSTANCE.css().OnButtonDeActive());
		}
		else if(mobileFriendlyVal.contains(mobileNo.getText()))
		{
			mobileNo.getElement().setClassName(AddTagesCBundle.INSTANCE.css().OffButtonsActive());
			mobileYes.getElement().setClassName(AddTagesCBundle.INSTANCE.css().OnButtonDeActive());
		}
	}	
	@UiHandler("mobileYes")
	public void onmobileYesClick(ClickEvent click)
	{
		mobileYes.getElement().setClassName(AddTagesCBundle.INSTANCE.css().OffButtonsActive());
		mobileNo.getElement().setClassName(AddTagesCBundle.INSTANCE.css().OnButtonDeActive());
		updateMobileFriendlyAdvancedStyles();
	}
	
	@UiHandler("mobileNo")
	public void onmobileNoClick(ClickEvent click)
	{
		mobileNo.getElement().setClassName(AddTagesCBundle.INSTANCE.css().OffButtonsActive());
		mobileYes.getElement().setClassName(AddTagesCBundle.INSTANCE.css().OnButtonDeActive());
		updateMobileFriendlyAdvancedStyles();
	}
	
	
	public abstract void browseStandardsInfo();
	
	public abstract void closeStandardsPopup();
	/**
	 * 
	 * @function setAdvancedOptionsStyles 
	 * 
	 * @created_date : 15-Dec-2014
	 * 
	 * @description  This method is used to set styles for educationaluse,momentsoflearning and mediafeature based on dropdown selection.
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
	public void setAdvancedOptionsStyles(){
		if(resourceEducationalLabel.getText().equalsIgnoreCase(i18n.GL1684())){
			addSetupAdvancedView.educationUseAdvancedContainer.setStyleName(AddSetupAdvancedCBundle.INSTANCE.css().setupBoxes());
		}else{
			addSetupAdvancedView.educationUseAdvancedContainer.setStyleName(AddSetupAdvancedCBundle.INSTANCE.css().setupBoxes());
			addSetupAdvancedView.educationUseAdvancedContainer.addStyleName(AddSetupAdvancedCBundle.INSTANCE.css().active());
		}
		if(resourcemomentsOfLearningLabel.getText().equalsIgnoreCase(i18n.GL1684())){
			addSetupAdvancedView.momentsOfLearningAdvancedContainer.setStyleName(AddSetupAdvancedCBundle.INSTANCE.css().setupBoxes());
		}else{
			addSetupAdvancedView.momentsOfLearningAdvancedContainer.setStyleName(AddSetupAdvancedCBundle.INSTANCE.css().setupBoxes());
			addSetupAdvancedView.momentsOfLearningAdvancedContainer.addStyleName(AddSetupAdvancedCBundle.INSTANCE.css().active());
		}
		if(lblMediaPlaceHolder.getText().equalsIgnoreCase("Choose a Media Feature Option:")){
			addSetupAdvancedView.mediaFeatureAdvancedContainer.setStyleName(AddSetupAdvancedCBundle.INSTANCE.css().setupBoxes());
		}else{
			addSetupAdvancedView.mediaFeatureAdvancedContainer.setStyleName(AddSetupAdvancedCBundle.INSTANCE.css().setupBoxes());
			addSetupAdvancedView.mediaFeatureAdvancedContainer.addStyleName(AddSetupAdvancedCBundle.INSTANCE.css().active());
		}
	}
	/**
	 * 
	 * @function setAdvancedAccessHazardStyles 
	 * 
	 * @created_date : 15-Dec-2014
	 * 
	 * @description  This method is used to set styles for accesshazard on click of perticular panel.
	 * 
	 * 
	 * @parm(s) : @param length
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	public void setAdvancedAccessHazardStyles(int length){
		if(length == 0){
			addSetupAdvancedView.accessHazardAdvancedContainer.setStyleName(AddSetupAdvancedCBundle.INSTANCE.css().setupBoxes());
		}else{
			addSetupAdvancedView.accessHazardAdvancedContainer.setStyleName(AddSetupAdvancedCBundle.INSTANCE.css().setupBoxes());
			addSetupAdvancedView.accessHazardAdvancedContainer.addStyleName(AddSetupAdvancedCBundle.INSTANCE.css().active());
		}
	}
	/**
	 * 
	 * @function updateStandardsAdvancedSetupStyle 
	 * 
	 * @created_date : 15-Dec-2014
	 * 
	 * @description This method is used to set styles for standards based on the number of standards.
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
	public void updateStandardsAdvancedSetupStyle() {
		if(standardsPanel.getWidgetCount()==0){
			addSetupAdvancedView.standardsAdvancedContainer.setStyleName(AddSetupAdvancedCBundle.INSTANCE.css().setupBoxes());
		}else{
			addSetupAdvancedView.standardsAdvancedContainer.setStyleName(AddSetupAdvancedCBundle.INSTANCE.css().setupBoxes());
			addSetupAdvancedView.standardsAdvancedContainer.addStyleName(AddSetupAdvancedCBundle.INSTANCE.css().active());
		}
	}
	/**
	 * 
	 * @function updateMobileFriendlyAdvancedStyles 
	 * 
	 * @created_date : 15-Dec-2014
	 * 
	 * @description This method is used to set styles for MobileFriendly tags based on the user selection(Yes/No).
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
	public void updateMobileFriendlyAdvancedStyles(){
		if(mobileYes.getStyleName().contains(AddTagesCBundle.INSTANCE.css().OffButtonsActive()))
		{
			addSetupAdvancedView.mobileFreindlyAdvancedContainer.setStyleName(AddSetupAdvancedCBundle.INSTANCE.css().setupBoxes());
			addSetupAdvancedView.mobileFreindlyAdvancedContainer.addStyleName(AddSetupAdvancedCBundle.INSTANCE.css().active());
		}
		else if(mobileNo.getStyleName().contains(AddTagesCBundle.INSTANCE.css().OffButtonsActive()))
		{
			addSetupAdvancedView.mobileFreindlyAdvancedContainer.setStyleName(AddSetupAdvancedCBundle.INSTANCE.css().setupBoxes());
/*			addSetupAdvancedView.mobileFreindlyAdvancedContainer.addStyleName(AddSetupAdvancedCBundle.INSTANCE.css().active());*/		}	
	}
}
