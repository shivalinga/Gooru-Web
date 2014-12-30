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
import org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.add.drive.DriveView;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.item.AddSetupAdvancedCBundle;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.item.CollectionEditResourceCBundle;
import org.ednovo.gooru.client.uc.AppMultiWordSuggestOracle;
import org.ednovo.gooru.client.uc.AppSuggestBox;
import org.ednovo.gooru.client.uc.BlueButtonUc;
import org.ednovo.gooru.client.uc.CloseLabel;
import org.ednovo.gooru.client.uc.DownToolTipWidgetUc;
import org.ednovo.gooru.client.uc.HTMLEventPanel;
import org.ednovo.gooru.client.uc.StandardsPreferenceOrganizeToolTip;
import org.ednovo.gooru.client.uc.tooltip.BrowseStandardsTooltip;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.client.util.SetStyleForProfanity;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.code.CodeDo;
import org.ednovo.gooru.shared.model.content.CollectionDo;
import org.ednovo.gooru.shared.model.drive.GoogleDriveItemDo;
import org.ednovo.gooru.shared.model.search.SearchDo;
import org.ednovo.gooru.shared.model.user.ProfileDo;
import org.ednovo.gooru.shared.util.ResourceImageUtil;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.URL;
import com.google.gwt.regexp.shared.MatchResult;
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
import com.google.gwt.user.client.ui.FlowPanel;
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

public abstract class AddWebResourceView extends Composite implements SelectionHandler<SuggestOracle.Suggestion> {

	public interface AddWebResourceViewUiBinder extends UiBinder<Widget, AddWebResourceView> {
	}

	public static AddWebResourceViewUiBinder uiBinder = GWT.create(AddWebResourceViewUiBinder.class);
	
	private MessageProperties i18n = GWT.create(MessageProperties.class);

	@UiField
	public Label standardsDefaultText,mandatoryEducationalLbl, generateImageLbl,mandatorymomentsOfLearninglLbl,driveFileInfoLbl,mandatorygenerateFromUrlLbl;
	
	@UiField
	public BlueButtonUc addResourceBtnLbl;

	@UiField
	Label standardMaxMsg, mandatoryUrlLbl, mandatoryTitleLbl,rightsLbl;

	@UiField
	Label mandatoryCategoryLbl;
	
	@UiField
	HTMLEventPanel refreshLbl,lblContentRights,videoResourcePanel,websiteResourcePanel,interactiveResourcePanel,imageResourcePanel,textResourcePanel,audioResourcePanel,
	activityPanel,handoutPanel,homeworkPanel,gamePanel,presentationPanel,referenceMaterialPanel,quizPanel,curriculumPlanPanel,
	lessonPlanPanel,unitPlanPanel,projectPlanPanel,readingPanel,textbookPanel,articlePanel,bookPanel,preparingTheLearningPanel,interactingWithTheTextPanel,extendingUnderstandingPanel,
	advancedSetupContainer,defaultPanel,eHearderIconEducationalUse,eHearderIconMomentsOfLearning,eHearderIconstandards,
	eHearderIconAccessHazard,eHearderIconMediafeature,eHearderIconMobileFriendly,defaultPanelMomentsOfLearningPnl;
	
	@UiField
	Label leftArrowLbl, rightArrowLbl,momentsOfLearningDropDownLbl;

	@UiField
	public TextBox urlTextBox, titleTextBox;

	@UiField
	public TextArea descriptionTxtAera;

	// @UiField public ListBox resourceTypeListBox;

	@UiField
	public Image setThumbnailImage;
	// Drop down for Resource Type//
	@UiField
	HTMLPanel extendingUnderstandingText,interactingWithTheTextText,preparingTheLearningText,homeworkText,	gameText,presentationText,referenceMaterialText,quizText,curriculumPlanText,lessonPlanText,
		unitPlanText,projectPlanText,readingText,textbookText,articleText,bookText,activityText,handoutText,descCharcterLimit,panelContentRights,titleText,categoryTitle,educationalTitle,momentsOfLearningTitle,orText,refreshText,
		educationalpanel,generateFromUrlPanel,defaultText,momentsOfLearningContainer,mediaLabelContainer,accessHazardContainer,standardsBrowseContainer,mobileFriendlyContainer,mediaFeatureContainer,
		defaultMomentsOfLearningText,mediaDropdownArrowConatainer;

	
	@UiField HTMLPanel panelCategoryInputDiv;


	@UiField
	public HTMLPanel addResourceBtnPanel,loadingPanel,urlTitle,descriptionLabel,videoLabel,interactiveText,websiteText,imagesText,contentPanel,textsText,audioText,urlContianer;//otherText

	@UiField
	HTMLPanel categorypanel, video, interactive, website,thumbnailText,audio,texts,image,rightsContent,errorContainer;//other

	@UiField
	HTMLPanel resourceTypePanel,educationalUsePanel,momentsOfLearningPanel, resourceDescriptionContainer,buttonsPanel,educationalContainer;

	@UiField
	Label resoureDropDownLbl, resourceCategoryLabel,resourceEducationalLabel,resourcemomentsOfLearningLabel, loadingTextLbl,mandatoryDescLblForSwareWords,mandatoryTitleLblForSwareWords,educationalDropDownLbl;
	
	@UiField
	CheckBox rightsChkBox;
	
	/*@UiField
	Anchor copyRightAnr;*/
	/*
	@UiField
	Anchor termsAndPolicyAnr,privacyAnr;
	
	@UiField
	Anchor commuGuideLinesAnr;*/
		
	@UiField InlineLabel agreeText,andText,additionalText,commuGuideLinesAnr, termsAndPolicyAnr,privacyAnr,copyRightAnr,moblieFriendly;
	
	@UiField(provided = true)
	AppSuggestBox standardSgstBox;
	
	@UiField FlowPanel standardsPanel,standardContainer;
	
	@UiField Button cancelResourcePopupBtnLbl,mobileYes,mobileNo,generateFromUrlBtn,uploadImageLbl;
	
	@UiField Label accessHazard,flashingHazard,motionSimulationHazard,soundHazard;
	
	@UiField Label mediaLabel,lblMediaPlaceHolder,lblMediaFeatureArrow;
	
	@UiField ScrollPanel spanelMediaFeaturePanel,scrollPanel;
	
	@UiField HTMLPanel htmlMediaFeatureListContainer;
	
	@UiField Button browseStandards;
	
	@UiField InlineLabel advancedText;
	
	@UiField(provided = true)
	AddTagesCBundle res2;
	
	Integer videoDuration=0;
	
	private CopyRightPolicyVc copyRightPolicy;
	
	private TermsAndPolicyVc termsAndPolicyVc;
	
	private TermsOfUse termsOfUse;
	// public TinyMCE tinyMce=null; 
	public boolean isValidYoutubeUrlFlag = true;
	
	private boolean hasClickedOnDropDwn=false;

	public boolean resoureDropDownLblOpen = false,educationalDropDownLblOpen=false,momentsOfLearningOpen=false;
	
	private boolean isShortenedUrl;
	
	boolean isValidate = true;
	
	private static final String GOOGLE_DRIVE = "Google Drive";
	
	private AppMultiWordSuggestOracle standardSuggestOracle;
	
	private SearchDo<CodeDo> standardSearchDo = new SearchDo<CodeDo>();
	
	private static final String FLT_CODE_ID = "id";
	
	List<String> standardPreflist;
	
	private Map<String, String> standardCodesMap = new HashMap<String, String>();
	
	List<CodeDo> standardsDo=new ArrayList<CodeDo>();
	
	String courseCode="";
	
	int activeImageIndex = 0;
	
	protected List<String> thumbnailImages;
	
	String thumbnailUrlStr = null;
	
	CollectionDo collectionDo;
	
	boolean isHavingBadWordsInTextbox=false,isHavingBadWordsInRichText=false;
	
//	private static final String RESOURCE_UPLOAD_FILE_PATTERN = "([^\\s]+([^?#]*\\.(?:mp3))$)";
	
	private static final String USER_META_ACTIVE_FLAG = "0";
	
	private static final String DEFAULT_COMBO_BOX_TEXT ="Please choose one of the following...";
	
	String mediaFeatureStr = i18n.GL1767();
	
	final StandardsPreferenceOrganizeToolTip standardsPreferenceOrganizeToolTip=new StandardsPreferenceOrganizeToolTip();
	
	
	private boolean isGoogleDriveFile=false;
	
	private GoogleDriveItemDo googleDriveItemDo=null;
	
	private boolean isBrowseTooltip =false;
	
	BrowseStandardsTooltip browseStandardsTooltip;
	
	private boolean isBrowseStandardsToolTip = false;
	
	private boolean isGenerateURL =false;
	
	public AddSetupAdvancedView addSetupAdvancedView;
	
	HandlerRegistration videoClickHandler,websiteClickHandler,interactiveClickHandler,imageClickHandler,textClickHandler,audioClickHandler=null;

	public AddWebResourceView(CollectionDo collectionDo,boolean isGoogleDriveFile,GoogleDriveItemDo googleDriveItemDo) { 
		this.res2 = AddTagesCBundle.INSTANCE;
		res2.css().ensureInjected();
		AddSetupAdvancedCBundle.INSTANCE.css().ensureInjected();
		CollectionEditResourceCBundle.INSTANCE.css().ensureInjected();
		this.isGoogleDriveFile=isGoogleDriveFile;
		this.googleDriveItemDo=googleDriveItemDo;
		standardSuggestOracle = new AppMultiWordSuggestOracle(true);
		standardSearchDo.setPageSize(10);
		standardSgstBox = new AppSuggestBox(standardSuggestOracle) {
			@SuppressWarnings("deprecation")
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
		
		advancedText.setText(i18n.GL3096());
		mediaLabelContainer.getElement().getStyle().setMarginBottom(10, Unit.PX);
		mediaDropdownArrowConatainer.getElement().getStyle().setRight(10, Unit.PX);
		addSetupAdvancedView = new AddSetupAdvancedView() {
			@Override
			public void showAndHideContainers() {
			}
		};
		advancedSetupContainer.add(addSetupAdvancedView);
		generateFromUrlBtn.setText(i18n.GL3092());
		/*generateFromUrlBtn.setEnabled(false);
		generateFromUrlBtn.getElement().getStyle().setOpacity(0.5);
		isGenerateURL =false;*/
		disableGenerateBtn();
		refreshLbl.setVisible(false);
		errorContainer.setVisible(false);
		errorContainer.add(standardsPreferenceOrganizeToolTip);
		urlTitle.getElement().setInnerHTML(i18n.GL0915());
		urlTitle.getElement().setId("pnlUrlTitle");
		urlTitle.getElement().setAttribute("alt", i18n.GL0915());
		urlTitle.getElement().setAttribute("title", i18n.GL0915());
		titleText.getElement().setInnerHTML(i18n.GL0318()+i18n.GL_SPL_STAR());
		titleText.getElement().setId("pnlTitleText");
		titleText.getElement().setAttribute("alt", i18n.GL0318());
		titleText.getElement().setAttribute("title", i18n.GL0318());
		descriptionLabel.getElement().setInnerHTML(i18n.GL0904());
		descriptionLabel.getElement().setId("pnlDescriptionLabel");
		descriptionLabel.getElement().setAttribute("alt", i18n.GL0904());
		descriptionLabel.getElement().setAttribute("title", i18n.GL0904());
		categoryTitle.getElement().setInnerHTML(i18n.GL3103()+i18n.GL_SPL_STAR());
		categoryTitle.getElement().setId("pnlCategoryTitle");
		categoryTitle.getElement().setAttribute("alt", i18n.GL0906());
		categoryTitle.getElement().setAttribute("title", i18n.GL0906());
		videoLabel.getElement().setInnerHTML(i18n.GL0918());
		videoLabel.getElement().setId("pnlVideoLabel");
		videoLabel.getElement().setAttribute("alt", i18n.GL0918());
		videoLabel.getElement().setAttribute("title", i18n.GL0918());
		interactiveText.getElement().setInnerHTML(i18n.GL0919());
		interactiveText.getElement().setId("pnlInteractiveText");
		interactiveText.getElement().setAttribute("alt", i18n.GL0919());
		interactiveText.getElement().setAttribute("title", i18n.GL0919());
		websiteText.getElement().setInnerHTML(i18n.GL1396());
		websiteText.getElement().setId("pnlWebsiteText");
		websiteText.getElement().setAttribute("alt", i18n.GL1396());
		websiteText.getElement().setAttribute("title", i18n.GL1396());
		educationalTitle.getElement().setInnerHTML(i18n.GL1664());
		educationalTitle.getElement().setId("pnlEducationalTitle");
		educationalTitle.getElement().setAttribute("alt", i18n.GL1664());
		educationalTitle.getElement().setAttribute("title", i18n.GL1664());
		activityText.getElement().setInnerHTML(i18n.GL1665());
		activityText.getElement().setId("pnlActivityText");
		activityText.getElement().setAttribute("alt", i18n.GL1665());
		activityText.getElement().setAttribute("title", i18n.GL1665());
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
		
		defaultText.getElement().setInnerHTML(i18n.GL3093());
		defaultMomentsOfLearningText.getElement().setInnerHTML(i18n.GL3093());
		//defaultMediaFeatureText.getElement().setInnerHTML(i18n.GL3093());
		
		
		momentsOfLearningTitle.getElement().setInnerHTML(i18n.GL1678());
		momentsOfLearningTitle.getElement().setId("pnlMomentsOfLearningTitle");
		momentsOfLearningTitle.getElement().setAttribute("alt", i18n.GL1678());
		momentsOfLearningTitle.getElement().setAttribute("title", i18n.GL1678());
		preparingTheLearningText.getElement().setInnerHTML(i18n.GL1679());
		preparingTheLearningText.getElement().setId("pnlPreparingTheLearningText");
		preparingTheLearningText.getElement().setAttribute("alt", i18n.GL1679());
		preparingTheLearningText.getElement().setAttribute("title", i18n.GL1679());
		interactingWithTheTextText.getElement().setInnerHTML(i18n.GL1680());
		interactingWithTheTextText.getElement().setId("pnlInteractingWithTheTextText");
		interactingWithTheTextText.getElement().setAttribute("alt", i18n.GL1680());
		interactingWithTheTextText.getElement().setAttribute("title", i18n.GL1680());
		extendingUnderstandingText.getElement().setInnerHTML(i18n.GL1681());
		extendingUnderstandingText.getElement().setId("pnlExtendingUnderstandingText");
		extendingUnderstandingText.getElement().setAttribute("alt", i18n.GL1681());
		extendingUnderstandingText.getElement().setAttribute("title", i18n.GL1681());
		standardsDefaultText.setText(i18n.GL1682());
		standardsDefaultText.getElement().setId("lblStandardsDefaultText");
		standardsDefaultText.getElement().setAttribute("alt", i18n.GL1682());
		standardsDefaultText.getElement().setAttribute("title", i18n.GL1682());
		resourceDescriptionContainer.getElement().setId("pnlResourceDescriptionContainer");
		/*slideText.getElement().setInnerHTML(i18n.GL0908);
		handoutText.getElement().setInnerHTML(i18n.GL0907);
		textbookLabel.getElement().setInnerHTML(i18n.GL0909);
		lessonText.getElement().setInnerHTML(i18n.GL0910);
		examText.getElement().setInnerHTML(i18n.GL0921);*/
		textsText.getElement().setInnerHTML(i18n.GL1044());
		textsText.getElement().setId("pnlTextsText");
		textsText.getElement().setAttribute("alt", i18n.GL1044());
		textsText.getElement().setAttribute("title", i18n.GL1044());
		audioText.getElement().setInnerHTML(i18n.GL1045());
		audioText.getElement().setId("pnlAudioText");
		audioText.getElement().setAttribute("alt", i18n.GL1045());
		audioText.getElement().setAttribute("title", i18n.GL1045());
		imagesText.getElement().setInnerHTML(i18n.GL1046());
		imagesText.getElement().setId("pnlImagesText");
		imagesText.getElement().setAttribute("alt", i18n.GL1046());
		imagesText.getElement().setAttribute("title", i18n.GL1046());
		contentPanel.getElement().setId("pnlContentPanel");
		urlContianer.getElement().setId("pnlUrlContianer");
//		otherText.getElement().setInnerHTML(i18n.GL1047);
		
		
		thumbnailText.getElement().setInnerHTML(i18n.GL0911());
		thumbnailText.getElement().setId("pnlThumbnailText");
		thumbnailText.getElement().setAttribute("alt", i18n.GL0911());
		thumbnailText.getElement().setAttribute("title", i18n.GL0911());
		generateImageLbl.setText(i18n.GL0922());
		generateImageLbl.getElement().setId("lblGenerateImageLbl");
		generateImageLbl.getElement().setAttribute("alt", i18n.GL0922());
		generateImageLbl.getElement().setAttribute("title", i18n.GL0922());
		orText.getElement().setInnerHTML(i18n.GL_GRR_Hyphen()+i18n.GL0209()+i18n.GL_GRR_Hyphen());
		orText.getElement().setId("pnlOrText");
		orText.getElement().setAttribute("alt", i18n.GL0209());
		orText.getElement().setAttribute("title", i18n.GL0209());
		uploadImageLbl.setText(i18n.GL0912());
		uploadImageLbl.getElement().setAttribute("alt", i18n.GL0912());
		uploadImageLbl.getElement().setAttribute("title", i18n.GL0912());
		
		refreshText.getElement().setInnerHTML(i18n.GL0923());
		refreshText.getElement().setId("pnlRefreshText");
		refreshText.getElement().setAttribute("alt", i18n.GL0923());
		refreshText.getElement().setAttribute("title", i18n.GL0923());
		rightsLbl.setText(i18n.GL0869());
		rightsLbl.getElement().setId("lblRightsLbl");
		rightsLbl.getElement().setAttribute("alt", i18n.GL0869());
		rightsLbl.getElement().setAttribute("title", i18n.GL0869());
		addResourceBtnLbl.setText(i18n.GL0590());
		addResourceBtnLbl.getElement().setAttribute("alt", i18n.GL0590());
		addResourceBtnLbl.getElement().setAttribute("title", i18n.GL0590());
		cancelResourcePopupBtnLbl.setText(i18n.GL0142());
		cancelResourcePopupBtnLbl.getElement().setAttribute("alt", i18n.GL0142());
		cancelResourcePopupBtnLbl.getElement().setAttribute("title", i18n.GL0142());
		loadingTextLbl.setText(i18n.GL0591().toLowerCase());
		loadingTextLbl.getElement().setId("lblLoadingTextLbl");
		loadingTextLbl.getElement().setAttribute("alt", i18n.GL0591().toLowerCase());
		loadingTextLbl.getElement().setAttribute("title", i18n.GL0591().toLowerCase());
		cancelResourcePopupBtnLbl.addClickHandler(new CloseClickHandler());
		addResourceBtnLbl.setEnable(true);
		addResourceBtnLbl.getElement().removeClassName("secondary");
		addResourceBtnLbl.getElement().addClassName("primary");
		
		addResourceBtnLbl.addClickHandler(new AddClickHandler());
		uploadImageLbl.addClickHandler(new OnEditImageClick());
		browseStandards.addClickHandler(new onBrowseStandarsCLick());
		
		generateFromUrlBtn.addClickHandler(new onGenerateFromUrlBtnClick());
		
		uploadImageLbl.getElement().setId("lblUploadImage");
		addResourceBtnLbl.getElement().setId("btnAdd");
		urlTextBox.getElement().setId("tbUrl");
		titleTextBox.getElement().setId("tbTitle");
		StringUtil.setAttributes(titleTextBox, true);
		cancelResourcePopupBtnLbl.getElement().setId("lblCancel");
		descriptionTxtAera.getElement().setId("taDescription");
		StringUtil.setAttributes(descriptionTxtAera, true);
		descriptionTxtAera.getElement().setAttribute("placeholder", i18n.GL0359());
		if(!isGoogleDriveFile){
			urlTextBox.addKeyUpHandler(new UrlKeyUpHandler());
			urlTextBox.addBlurHandler(new UrlBlurHandler());
		}
		titleTextBox.addKeyUpHandler(new TitleKeyUpHandler());
		descriptionTxtAera.addKeyUpHandler(new DescriptionKeyUpHandler());
		titleTextBox.getElement().setAttribute("maxlength", "50");
		descriptionTxtAera.getElement().setAttribute("maxlength", "300");
		resourceCategoryLabel.setText(i18n.GL0360());
		resourceCategoryLabel.getElement().setId("lblResourceCategoryLabel");
		resourceCategoryLabel.getElement().setAttribute("alt", i18n.GL0360());
		resourceCategoryLabel.getElement().setAttribute("title", i18n.GL0360());
		resourceEducationalLabel.setText(i18n.GL1684());
		resourceEducationalLabel.getElement().setId("lblResourceEducationalLabel");
		resourceEducationalLabel.getElement().setAttribute("alt", i18n.GL1684());
		resourceEducationalLabel.getElement().setAttribute("title", i18n.GL1684());
		resourcemomentsOfLearningLabel.setText(i18n.GL1684());
		resourcemomentsOfLearningLabel.getElement().setId("lblResourcemomentsOfLearningLabel");
		resourcemomentsOfLearningLabel.getElement().setAttribute("alt", i18n.GL1684());
		resourcemomentsOfLearningLabel.getElement().setAttribute("title", i18n.GL1684());
		mandatoryUrlLbl.getElement().setId("lblMandatoryUrlLbl");
		mandatoryUrlLbl.setVisible(false);
		mandatoryTitleLbl.setVisible(false);
		mandatoryTitleLbl.getElement().setId("lblMandatoryTitleLbl");
		mandatoryTitleLblForSwareWords.getElement().setId("lblMandatoryTitleLblForSwareWords");
		mandatoryTitleLblForSwareWords.setVisible(false);
		mandatoryDescLblForSwareWords.getElement().setId("lblMandatoryDescLblForSwareWords");
		mandatoryDescLblForSwareWords.setVisible(false);
		mandatoryCategoryLbl.setVisible(false);
		mandatoryCategoryLbl.getElement().setId("lblMandatoryCategoryLbl");
		mandatoryCategoryLbl.getElement().getStyle().setTop(-10, Unit.PX);
		descCharcterLimit.getElement().setInnerText(i18n.GL0143());
		descCharcterLimit.getElement().setId("pnlDescCharcterLimit");
		descCharcterLimit.getElement().setAttribute("alt", i18n.GL0143());
		descCharcterLimit.getElement().setAttribute("title", i18n.GL0143());
		descCharcterLimit.setVisible(false);
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
		additionalText.getElement().setId("" +"lblAdditionalText");
		additionalText.getElement().setAttribute("alt", i18n.GL0874());
		additionalText.getElement().setAttribute("title", i18n.GL0874());
		leftArrowLbl.getElement().setId("lblLeftArrowLbl");
		leftArrowLbl.setVisible(false);
		rightArrowLbl.setVisible(false);
		rightArrowLbl.getElement().setId("lblRightArrowLbl");
		setThumbnailImage.getElement().setId("imgSetThumbnailImage");
		setThumbnailImage.setVisible(false);
		loadingTextLbl.getElement().getStyle().setFontStyle(FontStyle.ITALIC);
		resourceTypePanel.setVisible(true);
		resourceTypePanel.getElement().setId("pnlResourceTypePanel");
		educationalUsePanel.getElement().setId("pnlEducationalUsePanel");
		educationalUsePanel.setVisible(false);
		momentsOfLearningPanel.getElement().setId("pnlMomentsOfLearningPanel");
		momentsOfLearningPanel.setVisible(false);
		loadingPanel.getElement().setId("pnlLoadingPanel");
		categorypanel.getElement().setId("pnlCategorypanel");
		loadingPanel.setVisible(false);
		panelContentRights.getElement().setId("pnlPanelContentRights");
		panelContentRights.setVisible(false);
		rightsChkBox.addClickHandler(new rightsChecked());
		rightsChkBox.getElement().setId("chkRights");
		resoureDropDownLbl.getElement().setId("lblResoureDropDownLbl");
		videoResourcePanel.getElement().setId("epnlVideoResourcePanel");
		video.getElement().setId("pnlVideo");
		websiteResourcePanel.getElement().setId("epnlWebsiteResourcePanel");
		website.getElement().setId("pnlWebsite");
		interactiveResourcePanel.getElement().setId("epnlInteractiveResourcePanel");
		
		videoClickHandler=videoResourcePanel.addClickHandler(new checkAvailableClickHandler());
		websiteClickHandler=websiteResourcePanel.addClickHandler(new checkAvailableClickHandler());
		interactiveClickHandler = interactiveResourcePanel.addClickHandler(new checkAvailableClickHandler());
		imageClickHandler=imageResourcePanel.addClickHandler(new checkAvailableClickHandler());
		textClickHandler = textResourcePanel.addClickHandler(new checkAvailableClickHandler());
		audioClickHandler = audioResourcePanel.addClickHandler(new checkAvailableClickHandler());
		
		interactive.getElement().setId("pnlInteractive");
		imageResourcePanel.getElement().setId("epnlImageResourcePanel");
		image.getElement().setId("pnlImage");
		textResourcePanel.getElement().setId("epnlTextResourcePanel");
		texts.getElement().setId("pnlTexts");
		audioResourcePanel.getElement().setId("epnlAudioResourcePanel");
		audio.getElement().setId("pnlAudio");
		refreshLbl.getElement().setId("epnlRefreshLbl");
		refreshLbl.getElement().getStyle().setWidth(70, Unit.PX);
		educationalpanel.getElement().setId("pnlEducationalpanel");
		educationalDropDownLbl.getElement().setId("lblEducationalDropDownLbl");
		mandatoryEducationalLbl.getElement().setId("lblMandatoryEducationalLbl");
		activityPanel.getElement().setId("epnlActivityPanel");
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
		momentsOfLearningDropDownLbl.getElement().setId("lblMomentsOfLearningDropDownLbl");
		mandatorymomentsOfLearninglLbl.getElement().setId("lblMandatorymomentsOfLearninglLbl");
		preparingTheLearningPanel.getElement().setId("epnlPreparingTheLearningPanel");
		interactingWithTheTextPanel.getElement().setId("epnlInteractingWithTheTextPanel");
		extendingUnderstandingPanel.getElement().setId("epnlExtendingUnderstandingPanel");
		standardContainer.getElement().setId("fpnlStandardContainer");
		standardSgstBox.getElement().setId("StandardSgstBox");
		standardMaxMsg.getElement().setId("lblStandardMaxMsg");
		standardsPanel.getElement().setId("fpnlStandardsPanel");
		driveFileInfoLbl.getElement().setId("lblDriveFileInfoLbl");
		lblContentRights.getElement().setId("epnlLblContentRights");
		rightsContent.getElement().setId("pnlRightsContent");
		buttonsPanel.getElement().setId("pnlButtonsPanel");
		addResourceBtnPanel.getElement().setId("pnlAddResourceBtnPanel");
		
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
		

		mediaLabel.setText(i18n.GL3094());
		mediaLabel.getElement().setId("lblMediaFeature");
		mediaLabel.getElement().setAttribute("alt","Media Feature");
		mediaLabel.getElement().setAttribute("title","Media Feature");
		mediaLabel.getElement().getStyle().setDisplay(Display.INLINE);
		
		lblMediaPlaceHolder.setText(i18n.GL3051()+i18n.GL_SPL_SEMICOLON());
		lblMediaPlaceHolder.getElement().setId("phMediaFeature");
		lblMediaPlaceHolder.getElement().setAttribute("alt","Choose a Media Feature Option:");
		lblMediaPlaceHolder.getElement().setAttribute("title","Choose a Media Feature Option:");
		
		mandatorygenerateFromUrlLbl.setText(i18n.GL3095());
		
		spanelMediaFeaturePanel.setVisible(false);
		
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
		for(int n=0; n<mediaFeatureList.size(); n++){
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
		clearFields();
		cancelResourcePopupBtnLbl.getElement().setAttribute("style", "margin-top:10px");
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
		
		titleTextBox.addBlurHandler(new CheckProfanityInOnBlur(titleTextBox, null, mandatoryTitleLblForSwareWords));
		descriptionTxtAera.addBlurHandler(new CheckProfanityInOnBlur(null, descriptionTxtAera, mandatoryDescLblForSwareWords));
		AppClientFactory.getInjector().getUserService().getUserProfileV2Details(AppClientFactory.getGooruUid(),USER_META_ACTIVE_FLAG,new SimpleAsyncCallback<ProfileDo>() {

			@Override
			public void onSuccess(ProfileDo profileObj) {
			if(profileObj.getUser().getMeta().getTaxonomyPreference().getCodeId()!=null){
					if(profileObj.getUser().getMeta().getTaxonomyPreference().getCodeId().size()==0){
						standardContainer.setVisible(true);
						isBrowseTooltip = true;
						DisableStandars();
					}else{
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
		
		/*if(isGenerateURL == false){
			generateFromUrlBtn.addMouseOverHandler(new MouseOverHandler() {
				@Override
				public void onMouseOver(MouseOverEvent event) {
					mandatorygenerateFromUrlLbl.setVisible(true);
				}
			});
		}*/
		generateFromUrlBtn.addMouseOutHandler(new MouseOutHandler() {
			
			@Override
			public void onMouseOut(MouseOutEvent event) {
				mandatorygenerateFromUrlLbl.setVisible(false);
			}
		});
		
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
	
	private void OpenMediaFeatureDropdown() {
		hasClickedOnDropDwn=true;
		if (spanelMediaFeaturePanel.isVisible()){
			spanelMediaFeaturePanel.setVisible(false);
		}else{
			spanelMediaFeaturePanel.setVisible(true);
		}
	}
	
	
	public void setDriveFileDetails(){
//		urlTitle.setVisible(false);
//		urlTextBox.setVisible(false);
//		urlContianer.setVisible(false);
		if(isGoogleDriveFile&&!googleDriveItemDo.isShared()){
			mandatoryUrlLbl.setText(i18n.GL2009_1());
			mandatoryUrlLbl.setVisible(true);
		}
		titleTextBox.setValue(googleDriveItemDo.getTitle());
		titleTextBox.getElement().setAttribute("alt", googleDriveItemDo.getTitle());
		titleTextBox.getElement().setAttribute("title", googleDriveItemDo.getTitle());
		urlTextBox.setReadOnly(true);
		titleTextBox.setFocus(true);
		if(googleDriveItemDo.getMimeType().equals(DriveView.DOCUMENT_MIMETYPE)||googleDriveItemDo.getMimeType().equals(DriveView.PRESENTATION_MIMETYPE)
				||googleDriveItemDo.getMimeType().equals(DriveView.SPREADSHEET_MIMETYPE)){
			urlTextBox.setValue(googleDriveItemDo.getEmbedLink());
			urlTextBox.getElement().setAttribute("alt", googleDriveItemDo.getEmbedLink());
			urlTextBox.getElement().setAttribute("title", googleDriveItemDo.getEmbedLink());
			//handoutResourcePanel(null);
		}else if(googleDriveItemDo.getMimeType().equals(DriveView.DRAWING_MIMETYPE)){
			urlTextBox.setValue(googleDriveItemDo.getEmbedLink());
			urlTextBox.getElement().setAttribute("alt", googleDriveItemDo.getEmbedLink());
			urlTextBox.getElement().setAttribute("title", googleDriveItemDo.getEmbedLink());
			//slideResourcePanel(null);
		}else if(googleDriveItemDo.getMimeType().equals(DriveView.FORM_MIMETYPE)){
			try{
				urlTextBox.setValue(googleDriveItemDo.getDefaultOpenWithLink().replaceFirst("edit", "viewform"));
				urlTextBox.getElement().setAttribute("alt",googleDriveItemDo.getDefaultOpenWithLink().replaceFirst("edit", "viewform"));
				urlTextBox.getElement().setAttribute("title", googleDriveItemDo.getDefaultOpenWithLink().replaceFirst("edit", "viewform"));
			}catch(Exception e){
				urlTextBox.setValue(googleDriveItemDo.getAlternateLink().replaceFirst("edit", "viewform"));
				urlTextBox.getElement().setAttribute("alt",googleDriveItemDo.getAlternateLink().replaceFirst("edit", "viewform"));
				urlTextBox.getElement().setAttribute("title", googleDriveItemDo.getAlternateLink().replaceFirst("edit", "viewform"));
			}

			//interactiveResourcePanel(null);
		}
	}

	public void onLoad() {
		super.onLoad();
		urlTextBox.setFocus(true);
		clearFields();
		if(isGoogleDriveFile){
			setDriveFileDetails();
			//driveFileInfoLbl.setText("If file is private, we will automatically update to public");
			driveFileInfoLbl.removeFromParent();
		}else{
			driveFileInfoLbl.removeFromParent();
		}
		// resourceDescriptionContainer.clear();
		// tinyMce=new TinyMCE();
		// resourceDescriptionContainer.add(tinyMce);
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
	
	
	private class CloseClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			clearFields();
			hidePopup();
		}
	}
	
	
	@UiHandler("lblContentRights")
	public void onMouseOver(MouseOverEvent event){
		panelContentRights.setVisible(true);
	}
	
	@UiHandler("lblContentRights")
	public void onMouseOut(MouseOutEvent event){
		panelContentRights.setVisible(false);
	}
	
	
	public abstract void hidePopup();
	
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
	
	
	private class OnEditImageClick implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			// getUiHandlers().resourceImageUpload();
			resourceImageUpload();
		}
	}
	
	private class onBrowseStandarsCLick implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			browseStandardsInfo();
		}
	}
	/**
	 * 
	 * @author GooruTeam
	 * This method is used to generate image on button click
	 *
	 */
	private class onGenerateFromUrlBtnClick implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			if(isGenerateURL){
				
				
				String userUrlStr = urlTextBox.getText().trim();
				userUrlStr = URL.encode(userUrlStr);
				//userUrlStr = userUrlStr.replaceAll("#", "%23");
				urlTextBox.setText(URL.decode(userUrlStr));
				urlTextBox.getElement().setAttribute("alt",userUrlStr);
				urlTextBox.getElement().setAttribute("title", userUrlStr);
				String userUrlStr1 = userUrlStr.replaceAll(
						"feature=player_detailpage&", "");
				userUrlStr1 = userUrlStr.replaceAll(
						"feature=player_embedded&", "");

				if (userUrlStr.indexOf("youtube") >0){
					String youTubeIbStr = ResourceImageUtil.getYoutubeVideoId(userUrlStr);
					String thumbnailUrl = "http://img.youtube.com/vi/"+youTubeIbStr+"/1.jpg";
					generateImageLbl.setVisible(false);
					setThumbnailImage.getElement().setAttribute("style","width: 80px;height: 60px;");
					setThumbnailImage.setUrl(thumbnailUrl);
					//thumbnailUrlStr = thumbnailImages.get(activeImageIndex);
				}else{
					activeImageIndex=0;
					setImageThumbnail();
					generateImageLbl.setVisible(false);
					setThumbnailImage.getElement().setAttribute("style","width: 80px;height: 60px;");
				}
//				loadingPanel.setVisible(true);
//				contentPanel.getElement().getStyle().setOpacity(0.6);
			}
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
	public void setMobileFriendlyObjectVal(String mobileFriendlyVal){
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
	public void onmobileNoClick(ClickEvent click){
		mobileNo.getElement().setClassName(AddTagesCBundle.INSTANCE.css().OffButtonsActive());
		mobileYes.getElement().setClassName(AddTagesCBundle.INSTANCE.css().OnButtonDeActive());
		updateMobileFriendlyAdvancedStyles();
	}
		
	/*
	 * method for select access hazards
	 */
	public String[] setAccessHazards(){
		String[] accessHazardsArr = null;
		List<String> accessHazardsSelected = new ArrayList<String>();

		if(flashingHazard.getElement().getClassName().contains("select")){
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
	public abstract void resourceImageUpload();

	public abstract void browseStandardsInfo();

	public abstract void closeStandardsPopup();

	private class AddClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {

			final Map<String, String> parms = new HashMap<String, String>();
			parms.put("text", titleTextBox.getValue());
			final List<String> tagList = new ArrayList<String>();
			AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {
				@Override
				public void onSuccess(Boolean value) {
					isHavingBadWordsInTextbox = value;
					if(value){
						SetStyleForProfanity.SetStyleForProfanityForTextBox(titleTextBox, mandatoryTitleLblForSwareWords,value);
						/*		addResourceBtnLbl.setEnabled(true);
							addResourceBtnLbl.getElement().removeClassName("secondary");
							addResourceBtnLbl.getElement().addClassName("primary");	*/			
					}else{
						parms.put("text", descriptionTxtAera.getText());
						AppClientFactory.getInjector().getResourceService().checkProfanity(parms,new SimpleAsyncCallback<Boolean>() {

							@Override
							public void onSuccess(Boolean result) {
								isValidate = true;
								isHavingBadWordsInRichText=result;
								if(result){
									SetStyleForProfanity.SetStyleForProfanityForTextArea(descriptionTxtAera, mandatoryDescLblForSwareWords, result);
									/*			addResourceBtnLbl.setEnabled(true);
										addResourceBtnLbl.getElement().removeClassName("secondary");
										addResourceBtnLbl.getElement().addClassName("primary");	*/
								}else{
									if (!isHavingBadWordsInRichText && !isHavingBadWordsInTextbox) {

										String urlStr = urlTextBox.getText();
										urlStr = urlStr.replaceAll("feature=player_detailpage&", "");
										urlStr = urlStr.replaceAll("feature=player_embedded&", "");
										urlStr = URL.encode(urlStr);
										//urlStr = urlStr.replaceAll("#", "%23");
										String youTubeId = getYoutubeVideoId(urlStr);

										if (urlStr.endsWith("/")) {
											urlStr = urlStr.substring(0, urlStr.length() - 1);
										}

										String descriptionStr = descriptionTxtAera.getText().trim(); // tinyMce.getText().trim();
										final String titleStr = titleTextBox.getText().trim();
										final String categoryStr = resourceCategoryLabel.getText();// resourceTypeListBox.getItemText(resourceTypeListBox.getSelectedIndex());
										final String idStr = "";


										if (urlStr.contains("goorulearning.org")) {
											if (urlStr.contains("support.goorulearning.org")
													|| urlStr.contains("about.goorulearning.org")) {
												isValidate = true;
											} else {
												showUrlErrorMessage(i18n.GL0924());
												isValidate = false;
											}
										}
										if(isGoogleDriveFile&&!googleDriveItemDo.isShared()){
											showUrlErrorMessage(i18n.GL2009_1());
											isValidate = false;
										}
										if(!rightsChkBox.getValue()){
											rightsLbl.getElement().getStyle().setColor("orange");
											isValidate = false;
										}
										if (urlStr == null || urlStr.equalsIgnoreCase("")) {
											showUrlErrorMessage(i18n.GL0916());
											isValidate = false;
										} else {
											boolean isStartWithHttp = urlStr.matches("^(http|https)://.*$");
											if (!isStartWithHttp) {
												urlStr = "http://" + urlStr;
												urlTextBox.setText(urlStr);
												urlTextBox.getElement().setAttribute("alt",urlStr);
												urlTextBox.getElement().setAttribute("title", urlStr);
											}
										}

										if (titleStr.toLowerCase().contains("www.")
												|| titleStr.toLowerCase().contains("http://")
												|| titleStr.toLowerCase().contains("https://")
												|| titleStr.toLowerCase().contains("ftp://")) {
											showTitleErrorMessage(i18n.GL0323());
											isValidate = false;
										}

										if (titleStr == null || titleStr.equalsIgnoreCase("")) {
											showTitleErrorMessage(i18n.GL0173());
											isValidate = false;
										}
										if (descriptionStr.length() >300) {
											descCharcterLimit.setVisible(true);
											isValidate = false;
										}
										if (categoryStr == null
												|| categoryStr.equalsIgnoreCase("-1")
												|| categoryStr
												.equalsIgnoreCase("Choose a resource format")) {
											showCategoryErrorMessage(i18n.GL0917());
											scrollPanel.setVerticalScrollPosition(0);
											isValidate = false;
										}

										if (!isValidYoutubeUrlFlag && categoryStr.equalsIgnoreCase("Video")) {
											showCategoryErrorMessage(i18n.GL0925());
											scrollPanel.setVerticalScrollPosition(0);
											isValidate = false;

										}

										if (!isValidUrl(urlStr, true)) {
											showUrlErrorMessage(i18n.GL0926());
											isValidate = false;
										}
										if(urlStr.indexOf("youtube")!=-1){
											if(youTubeId==null || youTubeId.equalsIgnoreCase("null") || youTubeId.equalsIgnoreCase("")){
												if(!categoryStr.equalsIgnoreCase("Webpage")){
													isValidate = true;									
												}else{
													showCategoryErrorMessage(i18n.GL0927());
													scrollPanel.setVerticalScrollPosition(0);
													isValidate = false;													}
											}
										}
										if(categoryStr.equalsIgnoreCase("Audio") && !hasValidateResource())
										{
											showUrlErrorMessage(i18n.GL1161());
											isValidate = false;
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
										//AreYouSurceToolTip AreYouSurceToolTip=new AreYouSurceToolTip();
										if (isValidate && !isShortenedUrl()) {
											MixpanelUtil.Create_NewResource();
											// getUiHandlers().addResource(idStr, urlStr, titleStr,
											// descriptionStr, categoryStr, thumbnailUrlStr);
											loadingTextLbl.getElement().getStyle().setDisplay(Display.BLOCK);
											buttonsPanel.getElement().getStyle().setDisplay(Display.NONE);
											descriptionStr = descriptionTxtAera.getText().trim();
											// String descriptionStr ="";
											urlStr = urlStr.replaceAll("feature=player_detailpage&", "");
											urlStr = urlStr.replaceAll("feature=player_embedded&", "");
											//												final String urlStrFinal=urlStr;
											//												final String descriptionStrFinal=descriptionStr;


											String hostName=null;
											if(isGoogleDriveFile){
												hostName=GOOGLE_DRIVE;
											}

											if(collectionDo.getSharing().equalsIgnoreCase("public")){
												//													if(isGoogleDriveFile&&!googleDriveItemDo.isShared()){
												//														AppClientFactory.getInjector().getResourceService().updateFileShareToAnyoneWithLink(googleDriveItemDo.getId(), new SimpleAsyncCallback<GoogleDriveDo>() {
												//															@Override
												//															public void onSuccess(GoogleDriveDo result) {
												//																addResource(idStr, urlStrFinal, titleStr, descriptionStrFinal,categoryStr, thumbnailUrlStr, getVideoDuration(),true,resourceEducationalLabel.getText(),resourcemomentsOfLearningLabel.getText(),standardsDo);
												//																addResourceBtnLbl.setEnabled(true);
												//															}
												//														});
												//													}else{
												addResource(idStr, urlStr, titleStr, descriptionStr,categoryStr, thumbnailUrlStr, getVideoDuration(),true,resourceEducationalLabel.getText(),resourcemomentsOfLearningLabel.getText(),standardsDo,hostName,tagList);


												/*addResourceBtnLbl.setEnabled(true);
														addResourceBtnLbl.getElement().removeClassName("secondary");
														addResourceBtnLbl.getElement().addClassName("primary");*/	
												//													}
											}
											else{
												//													if(isGoogleDriveFile&&!googleDriveItemDo.isShared()){
												//														AppClientFactory.getInjector().getResourceService().updateFileShareToAnyoneWithLink(googleDriveItemDo.getId(), new SimpleAsyncCallback<GoogleDriveDo>() {
												//															@Override
												//															public void onSuccess(GoogleDriveDo result) {
												//																addResource(idStr, urlStrFinal, titleStr, descriptionStrFinal,categoryStr, thumbnailUrlStr, getVideoDuration(),false,resourceEducationalLabel.getText(),resourcemomentsOfLearningLabel.getText(),standardsDo);
												//																addResourceBtnLbl.setEnabled(true);
												//															}
												//														});
												//													}else{
												addResource(idStr, urlStr, titleStr, descriptionStr,categoryStr, thumbnailUrlStr, getVideoDuration(),false,resourceEducationalLabel.getText(),resourcemomentsOfLearningLabel.getText(),standardsDo,hostName,tagList);

												/*addResourceBtnLbl.setEnabled(true);
														addResourceBtnLbl.getElement().removeClassName("secondary");
														addResourceBtnLbl.getElement().addClassName("primary");*/	
												//													}
											}

										}
										/*				addResourceBtnLbl.setEnabled(true);
											addResourceBtnLbl.getElement().removeClassName("secondary");
											addResourceBtnLbl.getElement().addClassName("primary");	*/
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
	 * @function addResource 
	 * 
	 * @created_date : 15-Dec-2014
	 * 
	 * @description
	 * 
	 * 
	 * @parm(s) : @param idStr
	 * @parm(s) : @param urlStr
	 * @parm(s) : @param titleStr
	 * @parm(s) : @param descriptionStr
	 * @parm(s) : @param categoryStr
	 * @parm(s) : @param thumbnailUrlStr
	 * @parm(s) : @param endTime
	 * @parm(s) : @param conformationFlag
	 * @parm(s) : @param educationalUse
	 * @parm(s) : @param momentsOfLearning
	 * @parm(s) : @param standards
	 * @parm(s) : @param hostName
	 * @parm(s) : @param tagList
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	public abstract void addResource(String idStr, String urlStr,	String titleStr, String descriptionStr, String categoryStr,	String thumbnailUrlStr, Integer endTime, boolean conformationFlag,String educationalUse,String momentsOfLearning,List<CodeDo> standards,String hostName,List<String> tagList);

//	public abstract void addResource(String idStr, String urlStr,	String titleStr, String descriptionStr, String categoryStr,	String thumbnailUrlStr, Integer endTime);

	private class UrlBlurHandler implements BlurHandler {

		@Override
		public void onBlur(BlurEvent event) {
			
			refreshLbl.setVisible(false);
			setThumbnailImage.setUrl("");
			setThumbnailImage.setVisible(false);
			rightArrowLbl.setVisible(false);
			leftArrowLbl.setVisible(false);
			generateImageLbl.setVisible(true);
			
			final Map<String, String> parms = new HashMap<String, String>();
			
			parms.put("text", urlTextBox.getText().trim());
			AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {

				@Override
				public void onSuccess(Boolean value) {
					if(!value){
						addResourceBtnLbl.setVisible(true);
						addResourceBtnPanel.setVisible(true);
						String userUrlStr = urlTextBox.getText().trim();
						if (userUrlStr.contains("goorulearning.org")) {
							if (userUrlStr.contains("support.goorulearning.org")
									|| userUrlStr.contains("about.goorulearning.org")) {

							} else {
								showUrlErrorMessage(i18n.GL0924());
								return;
							}
						}

						if (userUrlStr.endsWith("/")) {
							userUrlStr = userUrlStr.substring(0, userUrlStr.length() - 1);
						}
						if (!userUrlStr.equalsIgnoreCase("")) {
							boolean isStartWithHttp = userUrlStr
									.matches("^(http|https)://.*$");
							if (!isStartWithHttp) {
								userUrlStr = "http://" + userUrlStr;
								urlTextBox.setText(userUrlStr);
								urlTextBox.getElement().setAttribute("alt",userUrlStr);
								urlTextBox.getElement().setAttribute("title", userUrlStr);
							}
							if (isValidUrl(userUrlStr, true)) {
								enableGenerateBtn();
								refreshLbl.setVisible(true);
								userUrlStr = URL.encode(userUrlStr);
								//userUrlStr = userUrlStr.replaceAll("#", "%23");
								urlTextBox.setText(URL.decode(userUrlStr));
								urlTextBox.getElement().setAttribute("alt",userUrlStr);
								urlTextBox.getElement().setAttribute("title", userUrlStr);
								String userUrlStr1 = userUrlStr.replaceAll(
										"feature=player_detailpage&", "");
								userUrlStr1 = userUrlStr.replaceAll(
										"feature=player_embedded&", "");
								// getResourceInfo(userUrlStr1);
								checkShortenUrl(userUrlStr);
								if (userUrlStr.indexOf("youtube") >0){
									setVideoCategory();
									if(websiteClickHandler!=null){
											websiteClickHandler.removeHandler();
									}
									 if(interactiveClickHandler!=null){
										 interactiveClickHandler.removeHandler();
									}
									 if(imageClickHandler!=null){
										 imageClickHandler.removeHandler();
									}
									 if(textClickHandler!=null){
										 textClickHandler.removeHandler();
									}
									 if(audioClickHandler!=null){
										 audioClickHandler.removeHandler();
									}
									 
									}
								else{
									setVideoCategory();
									websiteClickHandler=websiteResourcePanel.addClickHandler(new checkAvailableClickHandler());
									interactiveClickHandler = interactiveResourcePanel.addClickHandler(new checkAvailableClickHandler());
									imageClickHandler=imageResourcePanel.addClickHandler(new checkAvailableClickHandler());
									textClickHandler = textResourcePanel.addClickHandler(new checkAvailableClickHandler());
									audioClickHandler = audioResourcePanel.addClickHandler(new checkAvailableClickHandler());
								}
								loadingPanel.setVisible(true);
								contentPanel.getElement().getStyle().setOpacity(0.6);

							} else {
								disableGenerateBtn();
								refreshLbl.setVisible(false);
								showUrlErrorMessage(i18n.GL0926());
							}
						}else{
							disableGenerateBtn();
							refreshLbl.setVisible(false);
						}
					}else{
						SetStyleForProfanity.SetStyleForProfanityForTextBox(urlTextBox, mandatoryUrlLbl, value);
						disableGenerateBtn();
						refreshLbl.setVisible(false);
					}
				}
			});
		}
	}

	public abstract void getResourceInfo(String userUrlStr);

	public abstract void checkShortenUrl(String userUrlStr);
	
	public abstract void updateThumbanilImage(String userUrlStr);

	private class UrlKeyUpHandler implements KeyUpHandler {
		public void onKeyUp(KeyUpEvent event) {
			clearUrlErrorMessage();
			String userUrlStr = urlTextBox.getText().trim();
			if (userUrlStr.equalsIgnoreCase("")) {
				refreshLbl.setVisible(false);
			}
		}
	}

	private class TitleKeyUpHandler implements KeyUpHandler {
		public void onKeyUp(KeyUpEvent event) {
			clearTitleErrorMessage();
			if (titleTextBox.getText().length() >= 50) {
				showTitleErrorMessage(i18n.GL0143());
			}
		}
	}

	private class DescriptionKeyUpHandler implements KeyUpHandler {
		public void onKeyUp(KeyUpEvent event) {
			descCharcterLimit.setVisible(false);
			if (descriptionTxtAera.getText().length() >=300) {
				descriptionTxtAera.setText(descriptionTxtAera.getText().trim()
						.substring(0, 300));
				descriptionTxtAera.getElement().setAttribute("alt", descriptionTxtAera.getText());
				descriptionTxtAera.getElement().setAttribute("title", descriptionTxtAera.getText());
				descCharcterLimit.setVisible(true);
			}

		}
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
	
	@UiHandler("leftArrowLbl")
	void leftArrowClick(ClickEvent event) {
		activeImageIndex--;
		setImageThumbnail();
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

	@UiHandler("rightArrowLbl")
	void rightArrowClick(ClickEvent event) {
		activeImageIndex++;
		setImageThumbnail();
	}

	/*@UiHandler("videoResourcePanel")
	void videoResourcePanel(ClickEvent event) {
		setVideoCategory();
	}*/
	
	
	/**
	 * 
	 * @function setVideoCategory 
	 * 
	 * @created_date : 15-Dec-2014
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
	void setVideoCategory(){
		MixpanelUtil.mixpanelEvent("organize_add_resource_video_selected");
		resourceCategoryLabel.setText(i18n.GL0918());
		resourceCategoryLabel.getElement().setAttribute("alt", i18n.GL0918());
		resourceCategoryLabel.getElement().setAttribute("title", i18n.GL0918());
		categorypanel.setStyleName(video.getStyleName());
		//resourceTypePanel.setVisible(false);
		resourceTypePanel.setVisible(true);
		resoureDropDownLblOpen = false;
		clearCategoryErrorMessage();
		videoResourcePanel.addStyleName("active");
		websiteResourcePanel.removeStyleName("active");
		interactiveResourcePanel.removeStyleName("active");
		audioResourcePanel.removeStyleName("active");
		textResourcePanel.removeStyleName("active");
		imageResourcePanel.removeStyleName("active");
	}
	
	void setInteractiveCategory() {
		MixpanelUtil.mixpanelEvent("organize_add_resource_interactive_selected");
		resourceCategoryLabel.setText(i18n.GL0919());
		resourceCategoryLabel.getElement().setAttribute("alt", i18n.GL0919());
		resourceCategoryLabel.getElement().setAttribute("title", i18n.GL0919());
		categorypanel.setStyleName(interactive.getStyleName());
		//resourceTypePanel.setVisible(false);
		resourceTypePanel.setVisible(true);
		resoureDropDownLblOpen = false;
		clearCategoryErrorMessage();
		interactiveResourcePanel.addStyleName("active");
		websiteResourcePanel.removeStyleName("active");
		audioResourcePanel.removeStyleName("active");
		textResourcePanel.removeStyleName("active");
		videoResourcePanel.removeStyleName("active");
		imageResourcePanel.removeStyleName("active");
	}

	void setWebsiteCategory() {
		MixpanelUtil.mixpanelEvent("organize_add_resource_website_selected");
		resourceCategoryLabel.setText(i18n.GL1396());
		resourceCategoryLabel.getElement().setAttribute("alt", i18n.GL1396());
		resourceCategoryLabel.getElement().setAttribute("title", i18n.GL1396());
		categorypanel.setStyleName(website.getStyleName());
		//resourceTypePanel.setVisible(false);
		resourceTypePanel.setVisible(true);
		resoureDropDownLblOpen = false;
		websiteResourcePanel.addStyleName("active");
		interactiveResourcePanel.removeStyleName("active");
		audioResourcePanel.removeStyleName("active");
		textResourcePanel.removeStyleName("active");
		videoResourcePanel.removeStyleName("active");
		imageResourcePanel.removeStyleName("active");
		clearCategoryErrorMessage();
	}

	void setImageCategory() {
		MixpanelUtil.mixpanelEvent("organize_add_resource_image_selected");
		resourceCategoryLabel.setText(i18n.GL1046());
		resourceCategoryLabel.getElement().setAttribute("alt", i18n.GL1046());
		resourceCategoryLabel.getElement().setAttribute("title", i18n.GL1046());
		categorypanel.setStyleName(image.getStyleName());
		//resourceTypePanel.setVisible(false);
		resourceTypePanel.setVisible(true);
		resoureDropDownLblOpen = false;
		clearCategoryErrorMessage();
		imageResourcePanel.addStyleName("active");
		websiteResourcePanel.removeStyleName("active");
		interactiveResourcePanel.removeStyleName("active");
		audioResourcePanel.removeStyleName("active");
		textResourcePanel.removeStyleName("active");
		videoResourcePanel.removeStyleName("active");
	}

	void setTextCategory() {
		MixpanelUtil.mixpanelEvent("organize_add_resource_text_selected");
		resourceCategoryLabel.setText(i18n.GL1044());
		resourceCategoryLabel.getElement().setAttribute("alt", i18n.GL1044());
		resourceCategoryLabel.getElement().setAttribute("title", i18n.GL1044());
		categorypanel.setStyleName(texts.getStyleName());
		//resourceTypePanel.setVisible(false);
		resourceTypePanel.setVisible(true);
		resoureDropDownLblOpen = false;
		clearCategoryErrorMessage();
		textResourcePanel.addStyleName("active");
		websiteResourcePanel.removeStyleName("active");
		interactiveResourcePanel.removeStyleName("active");
		audioResourcePanel.removeStyleName("active");
		videoResourcePanel.removeStyleName("active");
		imageResourcePanel.removeStyleName("active");
	}

	void setAudioCategory() {
		MixpanelUtil.mixpanelEvent("organize_add_resource_audio_selected");
		resourceCategoryLabel.setText(i18n.GL1045());
		resourceCategoryLabel.getElement().setAttribute("alt", i18n.GL1045());
		resourceCategoryLabel.getElement().setAttribute("title", i18n.GL1045());
		categorypanel.setStyleName(audio.getStyleName());
		//resourceTypePanel.setVisible(false);
		resourceTypePanel.setVisible(true);
		resoureDropDownLblOpen = false;
		clearCategoryErrorMessage();
		audioResourcePanel.addStyleName("active");
		websiteResourcePanel.removeStyleName("active");
		interactiveResourcePanel.removeStyleName("active");
		textResourcePanel.removeStyleName("active");
		videoResourcePanel.removeStyleName("active");
		imageResourcePanel.removeStyleName("active");
	}

//	@UiHandler("otherResourcePanel")
//	void lessonResourcePanel(ClickEvent event) {
//		MixpanelUtil.mixpanelEvent("organize_add_resource_other_selected");
//		resourceCategoryLabel.setText(i18n.GL1047);
//		categorypanel.setStyleName(other.getStyleName());
//		resourceTypePanel.setVisible(false);
//		resoureDropDownLblOpen = false;
//		mandatoryCategoryLbl.setVisible(false);
//	}

	// @UiHandler("questionResourcePanel")
	// void questionResourcePanel(ClickEvent event){
	// resourceCategoryLabel.setText("Question");
	// categorypanel.setStyleName(question.getStyleName());
	// resourceTypePanel.setVisible(false);
	// resoureDropDownLblOpen=false;
	// mandatoryCategoryLbl.setVisible(false);
	// }
	/*@UiHandler("examResourcePanel")
	void examResourcePanel(ClickEvent event) {
		resourceCategoryLabel.setText(i18n.GL0921);
		//categorypanel.setStyleName(exam.getStyleName());
		resourceTypePanel.setVisible(false);
		resoureDropDownLblOpen = false;
		mandatoryCategoryLbl.setVisible(false);
	}*/

/*	@UiHandler("resoureDropDownLbl")
	public void dropDownClick(ClickEvent event) {
		if(!isGoogleDriveFile){
			if (resoureDropDownLblOpen == false) {
				resourceTypePanel.setVisible(true);
				resoureDropDownLblOpen = true;
	
			} else {
				resourceTypePanel.setVisible(false);
				resoureDropDownLblOpen = false;
			}
		}

	}*/
	
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
	/**
	 * 
	 * @function setImageThumbnail 
	 * 
	 * @created_date : 15-Dec-2014
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
	public void setImageThumbnail() {
		if( thumbnailImages.size()>0){
			if (activeImageIndex == 0) {
				leftArrowLbl.setVisible(false);
			} else {
				leftArrowLbl.setVisible(true);
			}
			if (thumbnailImages != null) {
				if (activeImageIndex == thumbnailImages.size()) {
					rightArrowLbl.setVisible(false);
				} else {
					rightArrowLbl.setVisible(true);
				}
				// setThumbnailImage.setUrlAndVisibleRect(thumbnailImages.get(activeImageIndex),
				// 0, 0, 80, 60);
				setThumbnailImage.getElement().setAttribute("style",
						"width: 80px;height: 60px;");
				setThumbnailImage.setUrl(thumbnailImages.get(activeImageIndex));
				thumbnailUrlStr = thumbnailImages.get(activeImageIndex);
			}
		}
	}

	@UiHandler("refreshLbl")
	void refreshClick(ClickEvent event) {
		String userUrlStr = urlTextBox.getText().trim();
		if (userUrlStr.indexOf("youtube") == -1) {
			activeImageIndex = 0;
			setImageThumbnail();
		}
	}
	/*
	 * @UiHandler("refreshlabel") void refreshlabelClick(ClickEvent event){
	 * String userUrlStr = urlTextBox.getText().trim(); if
	 * (userUrlStr.indexOf("youtube")==-1){ activeImageIndex=0;
	 * setImageThumbnail(); } }
	 */
	public void clearFields() {
		urlTextBox.setText("");
		titleTextBox.setText("");

		// if(tinyMce!=null){
		// tinyMce.setEmptyContent("");
		// }
		// try {
		// tinyMce.setEmptyContent("");
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }

		descriptionTxtAera.setText("");
		// resourceTypeListBox.setSelectedIndex(0);
		generateImageLbl.setVisible(true);
		setThumbnailImage.setUrl("");
		if (thumbnailImages != null) {
			thumbnailImages.clear();
		}
		resourceCategoryLabel.setText(i18n.GL0360());
		resourceCategoryLabel.getElement().setAttribute("alt", i18n.GL0360());
		resourceCategoryLabel.getElement().setAttribute("title", i18n.GL0360());
		categorypanel.setStyleName("");

		clearCategoryErrorMessage();
		clearTitleErrorMessage();
		clearUrlErrorMessage();
		descCharcterLimit.setVisible(false);
		loadingTextLbl.getElement().getStyle().setDisplay(Display.NONE);
		buttonsPanel.getElement().getStyle().setDisplay(Display.BLOCK);
//		buttonsPanel.setVisible(true);
//		loadingTextLbl.setVisible(false);
		setVisible(true);
	}



	public void setVisible(boolean visible) {
		addResourceBtnLbl.setVisible(visible);
		addResourceBtnPanel.setVisible(visible);
	}

	private RegExp urlValidator;
	private RegExp urlPlusTldValidator;
	/**
	 * 
	 * @function isValidUrl 
	 * 
	 * @created_date : 15-Dec-2014
	 * 
	 * @description
	 * 
	 * 
	 * @parm(s) : @param url
	 * @parm(s) : @param topLevelDomainRequired
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
	public boolean isValidUrl(String url, boolean topLevelDomainRequired) {
		int count = returnCount(url);
		 if(count > 2)
	         return false;
		
		if (urlValidator == null || urlPlusTldValidator == null) {
			/*urlValidator = RegExp
					.compile("^((ftp|http|https)://[\\w@.\\-\\_\\()]+(:\\d{1,5})?(/[\\w#!:.?+=&%@!\\_\\-/\\()]+)*){1}$");
		*/	
			/*urlPlusTldValidator = RegExp
			.compile("^((ftp|http|https)://[\\w@.\\-\\_\\()]+\\.[a-zA-Z]{2,}(:\\d{1,5})?(/[\\w#!:.?+=&%@!\\,\\_\\-/\\()]+)*){1}$");
*/
			urlValidator = RegExp
					.compile("^((ftp|http|https)://[\\w@.\\-\\_\\()]+(:\\d{1,5})?(/[\\?%&=]+)*)");
			
			urlPlusTldValidator = RegExp
					.compile("^((ftp|http|https)://[\\w@.\\-\\_\\()]+(:\\d{1,5})?(/[\\?%&=]+)*)");

					}
		return (topLevelDomainRequired ? urlPlusTldValidator : urlValidator)
				.exec(url) != null;
	}
	/**
	 * 
	 * @function returnCount 
	 * 
	 * @created_date : 15-Dec-2014
	 * 
	 * @description
	 * 
	 * 
	 * @parm(s) : @param url
	 * @parm(s) : @return
	 * 
	 * @return : Integer
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	public Integer returnCount(String url) {

		String string = url;
		String substring1 = "http:";
		String substring2 = "https:";
		String substring3 = "ftp:";
		String substring4 = "www";

		Integer count = 0;
		Integer idx = 0;

		while ((idx = string.indexOf(substring1, idx)) != -1) {
			idx++;
			count++;
		}

		idx = 0;
		while ((idx = string.indexOf(substring2, idx)) != -1) {
			idx++;
			count++;
		}
		idx = 0;
		while ((idx = string.indexOf(substring3, idx)) != -1) {
			idx++;
			count++;
		}

		idx = 0;
		while ((idx = string.indexOf(substring4, idx)) != -1) {
			idx++;
			count++;
		}

		return count;

	}
	/**
	 * 
	 * @function getYoutubeVideoId 
	 * 
	 * @created_date : 15-Dec-2014
	 * 
	 * @description
	 * 
	 * 
	 * @parm(s) : @param youtubeUrl
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
	public String getYoutubeVideoId(String youtubeUrl) {

		youtubeUrl=youtubeUrl.replaceAll("feature=player_detailpage&", "");
		youtubeUrl=youtubeUrl.replaceAll("feature=player_embedded&","");


			String pattern = "^.*((youtu.be"+ "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*";
			String videoId = null; 
		    try {
		    	RegExp reg = RegExp.compile(pattern, "gi");
		    	MatchResult res = reg.exec(youtubeUrl);
		    	videoId = res.getGroup(7);
		    } catch (Exception e) {
		    	
			}
			return videoId;
	
	}
	
	

	public boolean isShortenedUrl() {
		return isShortenedUrl;
	}

	public void setShortenedUrl(boolean isShortenedUrl) {
		this.isShortenedUrl = isShortenedUrl;
	}

	/** 
	 * This method is to get the videoDuration
	 */
	public Integer getVideoDuration() {
		return videoDuration;
	}

	/** 
	 * This method is to set the videoDuration
	 */
	public void setVideoDuration(Integer videoDuration) {
		this.videoDuration = videoDuration;
	}
	/**
	 * 
	 * @fileName : AddWebResourceView.java
	 *
	 * @description : 
	 *
	 *
	 * @version : 1.0
	 *
	 * @date: 15-Dec-2014
	 *
	 * @Author Gooru Team
	 *
	 * @Reviewer:
	 */
	public class CheckProfanityInOnBlur implements BlurHandler{
		private TextBox textBox;
		private Label label;
		private TextArea textArea;
		public CheckProfanityInOnBlur(TextBox textBox,TextArea textArea,Label label){
			this.textBox=textBox;
			this.label=label;
			this.textArea=textArea;
		}
		@Override
		public void onBlur(BlurEvent event) {
			Map<String, String> parms = new HashMap<String, String>();
			if(textBox!=null){
				parms.put("text", textBox.getValue());
			}else{
				descCharcterLimit.setVisible(false);
				parms.put("text", textArea.getText());
			}
/*			addResourceBtnLbl.setEnabled(false);
			addResourceBtnLbl.getElement().removeClassName("primary");	
			addResourceBtnLbl.getElement().addClassName("secondary");*/

			AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {

				@Override
				public void onSuccess(Boolean value) {
					addResourceBtnLbl.setEnabled(true);
					addResourceBtnLbl.getElement().removeClassName("secondary");	
					addResourceBtnLbl.getElement().addClassName("primary");
					if(textBox!=null){
						isHavingBadWordsInTextbox = value;
						SetStyleForProfanity.SetStyleForProfanityForTextBox(textBox, label, value);
					}else{
						isHavingBadWordsInRichText=value;
						SetStyleForProfanity.SetStyleForProfanityForTextArea(textArea, label, value);
					}
					
				}
			});
		}
	}
	
	
	/**
	 * 
	 * @function hasValidateResource 
	 * 
	 * @created_date : 15-Dec-2014
	 * 
	 * @description
	 * 
	 * 
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
	public boolean hasValidateResource(){
		String userUrlStr = urlTextBox.getText().trim();
		boolean isValid;
		if(userUrlStr.endsWith(".mp3"))
		{
			return isValid = false;	
		}
		else
		{
			return isValid = true;		
		}
		
	}
	
	
	/**
	 * 
	 * @function setUpdatedBrowseStandarsCode 
	 * 
	 * @created_date : 15-Dec-2014
	 * 
	 * @description  This method is used to update the browse standards
	 * 
	 * 
	 * @parm(s) : @param standardsCodeVal
	 * @parm(s) : @param id
	 * @parm(s) : @param desc
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
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
	
	
	/**
	 * 
	 * @function hideBrowseStandardsPopup 
	 * 
	 * @created_date : 15-Dec-2014
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
	
	
	/**
	 * 
	 * @function eventTargetsPopup 
	 * 
	 * @created_date : 15-Dec-2014
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
	 * @function enableGenerateBtn 
	 * 
	 * @created_date : 15-Dec-2014
	 * 
	 * @description   This method is used to enable the generatefromURL button based on the url entered.
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
	public void enableGenerateBtn(){
		generateFromUrlBtn.getElement().getStyle().clearColor();
		generateFromUrlBtn.getElement().removeClassName("disabled");
		generateFromUrlBtn.getElement().getStyle().setOpacity(1);
		isGenerateURL =true;
		generateFromUrlBtn.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				mandatorygenerateFromUrlLbl.setVisible(false);
			}
		});
	}
	
	
	/**
	 * 
	 * @function disableGenerateBtn 
	 * 
	 * @created_date : 15-Dec-2014
	 * 
	 * @description This method is used to disable the generatefromURL button based on the url entered.
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
	public void disableGenerateBtn(){
		generateFromUrlBtn.getElement().getStyle().setColor("#999");
		generateFromUrlBtn.getElement().addClassName("disabled");
		generateFromUrlBtn.getElement().getStyle().setOpacity(0.5);
		isGenerateURL =false;
		mandatorygenerateFromUrlLbl.setVisible(false);
		generateFromUrlBtn.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				mandatorygenerateFromUrlLbl.setVisible(true);
			}
		});
	}
	
	
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
			/*addSetupAdvancedView.mobileFreindlyAdvancedContainer.addStyleName(AddSetupAdvancedCBundle.INSTANCE.css().active());*/
		}	
	}

	/**
	 * 
	 * @function showUrlErrorMessage 
	 * 
	 * @created_date : 15-Dec-2014
	 * 
	 * @description : This method is used to show UrlErrorMessage.
	 * 
	 * 
	 * @parm(s) : @param message
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	public void showUrlErrorMessage(String message){
		mandatoryUrlLbl.setText(message);
		StringUtil.setAttributes(mandatoryUrlLbl.getElement(), "lblMandatoryUrlLbl", message, message);
		urlTextBox.getElement().getStyle().setBorderColor("orange");
		urlTextBox.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
		urlTextBox.getElement().getStyle().setBorderWidth(1, Unit.PX);
		mandatoryUrlLbl.setVisible(true);
	}
	
	
	/**
	 * 
	 * @function clearUrlErrorMessage 
	 * 
	 * @created_date : 15-Dec-2014
	 * 
	 * @description This method is used to clear the URL Error Message.
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
	public void clearUrlErrorMessage(){
		mandatoryUrlLbl.setVisible(false);
		urlTextBox.getElement().getStyle().clearBorderColor();
		urlTextBox.getElement().getStyle().clearBorderStyle();
		urlTextBox.getElement().getStyle().clearBorderWidth();
	}
	
	/**
	 * 
	 * @function showTitleErrorMessage 
	 * 
	 * @created_date : 15-Dec-2014
	 * 
	 * @description : This method is used to show the error message for title
	 * 
	 * 
	 * @parm(s) : @param message
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	public void showTitleErrorMessage(String message){
		mandatoryTitleLbl.setText(message);
		StringUtil.setAttributes(mandatoryTitleLbl.getElement(), "lblMandatoryTitleLbl", message, message);
		titleTextBox.getElement().getStyle().setBorderColor("orange");
		titleTextBox.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
		titleTextBox.getElement().getStyle().setBorderWidth(1, Unit.PX);
		mandatoryTitleLbl.setVisible(true);
	}
	
	
	/**
	 * 
	 * @function clearTitleErrorMessage 
	 * 
	 * @created_date : 15-Dec-2014
	 * 
	 * @description  This method is used to clear the error message for title
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
	public void clearTitleErrorMessage(){
		mandatoryTitleLbl.setVisible(false);
		titleTextBox.getElement().getStyle().clearBorderColor();
		titleTextBox.getElement().getStyle().clearBorderStyle();
		titleTextBox.getElement().getStyle().clearBorderWidth();
	}
	
	
	/**
	 * 
	 * @function showCategoryErrorMessage 
	 * 
	 * @created_date : 15-Dec-2014
	 * 
	 * @description  This method is used to show the error message for category
	 * 
	 * 
	 * @parm(s) : @param message
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	public void showCategoryErrorMessage(String message){
		mandatoryCategoryLbl.setText(message);
		StringUtil.setAttributes(mandatoryTitleLbl.getElement(), "lblMandatoryCategoryLbl", message, message);
		panelCategoryInputDiv.getElement().getStyle().setBorderColor("orange");
		panelCategoryInputDiv.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
		panelCategoryInputDiv.getElement().getStyle().setBorderWidth(1, Unit.PX);
		panelCategoryInputDiv.getElement().setId("panelCategoryInputDiv");
		mandatoryCategoryLbl.setVisible(true);
	}
	
	
	/**
	 * 
	 * @function clearCategoryErrorMessage 
	 * 
	 * @created_date : 15-Dec-2014
	 * 
	 * @description   This method is used to clear the error message for category
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
	public void clearCategoryErrorMessage(){
		mandatoryCategoryLbl.setVisible(false);
		panelCategoryInputDiv.getElement().getStyle().clearBorderColor();
		panelCategoryInputDiv.getElement().getStyle().clearBorderStyle();
		panelCategoryInputDiv.getElement().getStyle().clearBorderWidth();
	}
	
	private class  checkAvailableClickHandler implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			if(event.getSource() == websiteResourcePanel){
				setWebsiteCategory();
			}else if(event.getSource() == videoResourcePanel){
				setVideoCategory();
			}else if(event.getSource() == interactiveResourcePanel){
				setInteractiveCategory();
			}else if(event.getSource() == imageResourcePanel){
				setImageCategory();
			}
			else if(event.getSource() == textResourcePanel){
				setTextCategory();
			}else if(event.getSource() == audioResourcePanel){
				setAudioCategory();
			}
			
		}
		
	}
}
