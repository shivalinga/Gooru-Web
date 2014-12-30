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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.add.AddSetupAdvancedView;
import org.ednovo.gooru.client.mvp.shelf.event.GetEditPageHeightEvent;
import org.ednovo.gooru.client.uc.AppMultiWordSuggestOracle;
import org.ednovo.gooru.client.uc.AppPopUp;
import org.ednovo.gooru.client.uc.AppSuggestBox;
import org.ednovo.gooru.client.uc.CloseLabel;
import org.ednovo.gooru.client.uc.DownToolTipWidgetUc;
import org.ednovo.gooru.client.uc.HTMLEventPanel;
import org.ednovo.gooru.client.uc.StandardsPreferenceOrganizeToolTip;
import org.ednovo.gooru.client.uc.tooltip.BrowseStandardsTooltip;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.client.util.SetStyleForProfanity;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.code.CodeDo;
import org.ednovo.gooru.shared.model.content.CollectionItemDo;
import org.ednovo.gooru.shared.model.content.ResourceMetaInfoDo;
import org.ednovo.gooru.shared.model.content.checkboxSelectedDo;
import org.ednovo.gooru.shared.model.search.SearchDo;
import org.ednovo.gooru.shared.model.user.ProfileDo;
import org.ednovo.gooru.shared.util.ResourceImageUtil;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
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
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
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

public abstract class EditResourcePopupVc extends AppPopUp implements SelectionHandler<SuggestOracle.Suggestion>{

	CollectionItemDo collectionItemDo;
	CollectionItemDo collectionOriginalItemDo;
	
	@UiField HTMLPanel panelCategory;

	@UiField
	public Button addResourceBtn,cancelResourcePopupBtnLbl,mobileYes,mobileNo,browseStandards;
	
	@UiField
	public Label generateImageLbl,resoureDropDownLbl;

	@UiField
	Label resourcemomentsOfLearningLabel,standardMaxMsg,mandatoryEducationalLbl,resourceEducationalLabel,mandatoryUrlLbl, mandatoryTitleLbl,educationalDropDownLbl;

	@UiField
	Label mandatoryCategoryLbl, urlTextLbl,refreshLbl;

	@UiField
	Label leftArrowLbl, rightArrowLbl, uploadImageLbl,mandatoryDescLblForSwareWords,mandatoryTitleLblForSwareWords,momentsOfLearningDropDownLbl;

	@UiField
	public TextBox titleTextBox;

	@UiField
	public TextArea descriptionTxtAera;
	
	@UiField HTMLEventPanel videoResourcePanel,lblContentRights,interactiveResourcePanel,websiteResourcePanel,imageResourcePanel,
	textResourcePanel,audioResourcePanel,activityPanel,handoutPanel,homeworkPanel,gamePanel,presentationPanel,
	referenceMaterialPanel,quizPanel,curriculumPlanPanel,lessonPlanPanel,unitPlanPanel,projectPlanPanel,readingPanel,
	textbookPanel,articlePanel,bookPanel,preparingTheLearningPanel,interactingWithTheTextPanel,extendingUnderstandingPanel;

	/*@UiField
	public ListBox resourceTypeListBox;*/

	@UiField
	Image setThumbnailImage;
	
	// Drop down for Resource Type//
	@UiField
	HTMLPanel momentsOfLearningPanel,momentsOfLearningTitle,extendingUnderstandingText,interactingWithTheTextText,preparingTheLearningText,educationalUsePanel,educationalTitle,homeworkText,gameText,presentationText,referenceMaterialText,quizText,curriculumPlanText,lessonPlanText,
	unitPlanText,projectPlanText,readingText,textbookText,articleText,bookText,activityText,handoutText,descCharcterLimit,saveButtonContainer,panelContentRights,rightsContainer,videoPanel,interactivePanel,websitePanel,imagePanel,textsPanel,audioPanel,
	educationalpanel,rightsContent,educationalContainer,momentsOfLearningContainer,accessHazardContainer,standardsBrowseContainer,mobileFriendlyContainer,mediaFeatureContainer,defaultMomentsOfLearningText,defaultText;//otherPanel
	
	@UiField Label mandatorymomentsOfLearninglLbl,standardsDefaultText,resourceCategoryLabel,loadingTextLbl,rightsLbl;
	
	 @UiField HTMLPanel categorypanel,video,interactive,website,resourceTypePanel,image,texts,audio,resourceFormat,resDescription,urlTextPanel,titleTextPanel,thumbnailLbl,orLbl,refreshLblPanel,errorContainer;//other,
	 @UiField CheckBox rightsChkBox;
	
	@UiField InlineLabel agreeText,andText,additionalText,commuGuideLinesAnr, termsAndPolicyAnr,privacyAnr,copyRightAnr,moblieFriendly,advancedText;
	
	@UiField(provided = true)
	AppSuggestBox standardSgstBox;
	
	@UiField(provided = true)
	AddTagesCBundle res2;
	@UiField FlowPanel standardContainer,standardsPanel;
	
	@UiField Label accessHazard,flashingHazard,motionSimulationHazard,soundHazard;
	
	@UiField Label mediaLabel,lblMediaPlaceHolder,lblMediaFeatureArrow;
	
	@UiField ScrollPanel spanelMediaFeaturePanel;
	
	@UiField HTMLPanel htmlMediaFeatureListContainer,mediaLabelContainer,mediaDropdownArrowConatainer;
	
	@UiField HTMLEventPanel AdvancedSetupContainer,eHearderIconEducationalUse,eHearderIconMomentsOfLearning,eHearderIconstandards,
	eHearderIconAccessHazard,eHearderIconMediafeature,eHearderIconMobileFriendly,defaultPanelMomentsOfLearningPnl,defaultPanel;
	
	private static final String USER_META_ACTIVE_FLAG = "0";
	ResourceMetaInfoDo resMetaInfoDo = null;
	private CopyRightPolicyVc copyRightPolicy;
	
	private TermsAndPolicyVc termsAndPolicyVc;
	private TermsOfUse termsOfUse;
	private int activeImageIndex = 0;
	protected List<String> thumbnailImagesLink;
	
	private String thumbnailUrlStr = null;
	
	private boolean hasClickedOnDropDwn = false;
	
	private String mobileFeature;
	
	String fileNameWithOutRespUrl = null;
	
	public boolean resoureDropDownLblOpen = false,educationalDropDownLblOpen=false,momentsOfLearningOpen=false;
	
	private static final String DEFULT_IMAGE_PREFIX = "images/default-";

	private static final String PNG = ".png";
	boolean isHavingBadWordsInTextbox=false,isHavingBadWordsInRichText=false;
	
	String courseCode="";
	private AppMultiWordSuggestOracle standardSuggestOracle;
	private SearchDo<CodeDo> standardSearchDo = new SearchDo<CodeDo>();
	private static final String FLT_CODE_ID = "id";
	List<String> standardPreflist;
	private Map<String, String> standardCodesMap = new HashMap<String, String>();
	Set<CodeDo> standardsDo=new HashSet<CodeDo>();
	Set<CodeDo> deletedStandardsDo=new HashSet<CodeDo>();
	private static final String DEFAULT_COMBO_BOX_TEXT ="Please choose one of the following...";
	StandardsPreferenceOrganizeToolTip standardsPreferenceOrganizeToolTip=new StandardsPreferenceOrganizeToolTip();
	private boolean isBrowseTooltip =false;
	
	 BrowseStandardsTooltip browseStandardsTooltip;
	 private boolean isBrowseStandardsToolTip = false;
	
	 public AddSetupAdvancedView addSetupAdvancedView;
	 
	final List<String> tagList = new ArrayList<String>();
	
	List<String> tagListGlobal = new ArrayList<String>();
	
	HandlerRegistration videoClickHandler,websiteClickHandler,interactiveClickHandler,imageClickHandler,textClickHandler,audioClickHandler=null;

	
	private static EditResourcePopupVcUiBinder uiBinder = GWT
			.create(EditResourcePopupVcUiBinder.class);

	interface EditResourcePopupVcUiBinder extends
			UiBinder<Widget, EditResourcePopupVc> {
	}
	
	private MessageProperties i18n = GWT.create(MessageProperties.class);
	String mediaFeatureStr = i18n.GL1767();
	public abstract void updateResource(CollectionItemDo collectionItemDo,List<String> tagList);
	private boolean isQuestionResource=false;
	private boolean isUserResource=false;

	public EditResourcePopupVc(CollectionItemDo collectionItemDo) {
		super();
		this.res2 = AddTagesCBundle.INSTANCE;
		res2.css().ensureInjected();
		AddSetupAdvancedCBundle.INSTANCE.css().ensureInjected();
		CollectionEditResourceCBundle.INSTANCE.css().ensureInjected();
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
					//standardsPreferenceOrganizeToolTip.hide();
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
						standardsPreferenceOrganizeToolTip.getElement().getStyle().setZIndex(1111);*/
						//standardSuggestOracle.add(i18n.GL1613);
						
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
					
				/*standardsPreferenceOrganizeToolTip.hide();*/
				errorContainer.setVisible(false);
				}
			}
		};
		standardSgstBox.addDomHandler(blurHandler, BlurEvent.getType());
		standardSgstBox.addSelectionHandler(this);
		// this.getElement().getStyle().setWidth(450, Unit.PX);
		// this.getElement().getStyle().setHeight(788, Unit.PX);
		this.collectionItemDo = collectionItemDo;
		this.collectionOriginalItemDo = collectionItemDo;
		
		setContent(i18n.GL0949(), uiBinder.createAndBindUi(this));
		defaultText.getElement().setInnerHTML(i18n.GL3093());
		defaultMomentsOfLearningText.getElement().setInnerHTML(i18n.GL3093());
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
		addResourceBtn.addClickHandler(new AddClickHandler());
		addResourceBtn.getElement().getStyle().setFloat(Float.LEFT);
		uploadImageLbl.addClickHandler(new OnEditImageClick());
		titleTextBox.getElement().setId("txtTitleTextBox");
		StringUtil.setAttributes(titleTextBox, true);
		titleTextBox.addKeyUpHandler(new TitleKeyUpHandler());
		descriptionTxtAera.getElement().setId("tatDescriptionTxtAera");
		StringUtil.setAttributes(descriptionTxtAera, true);
		descriptionTxtAera.addKeyUpHandler(new DescriptionKeyUpHandler());
		titleTextBox.getElement().setAttribute("maxlength", "50");
		descriptionTxtAera.getElement().setAttribute("maxlength", "300");
		mandatoryTitleLblForSwareWords.getElement().setId("lblMandatoryTitleLblForSwareWords");
		mandatoryDescLblForSwareWords.getElement().setId("lblMandatoryDescLblForSwareWords");
		mandatoryUrlLbl.setVisible(false);
		clearTitleErrorMessage();
		clearCategoryErrorMessage();
		descCharcterLimit.setVisible(false);
		leftArrowLbl.setVisible(false);
		rightArrowLbl.setVisible(false);
		setThumbnailImage.setVisible(true);
		setThumbnailImage.getElement().setId("imgSetThumbnailImage");
		resourceTypePanel.setVisible(true);
		loadingTextLbl.getElement().getStyle().setFontStyle(FontStyle.ITALIC);
		generateImageLbl.setVisible(false);
		saveButtonContainer.getElement().setId("pnlSaveButtonContainer");
		saveButtonContainer.setVisible(true);
		loadingTextLbl.setVisible(false);
		panelContentRights.getElement().setId("pnlPanelContentRights");
		rightsContent.getElement().setId("pnlRightsContent");
		panelContentRights.setVisible(false);
		mandatoryTitleLblForSwareWords.setVisible(false);
		mandatoryDescLblForSwareWords.setVisible(false);
		rightsChkBox.addClickHandler(new rightsChecked());
		rightsChkBox.getElement().setId("chkRights");
		setModal(true);
		Window.enableScrolling(false);
        AppClientFactory.fireEvent(new SetHeaderZIndexEvent(99, false));
        categorypanel.getElement().setId("pnlCategorypanel");
        mandatoryTitleLbl.getElement().setId("lblMandatoryTitleLbl");
        mandatoryTitleLbl.setText(i18n.GL0173());
        mandatoryTitleLbl.getElement().setAttribute("alt", i18n.GL0173());
        mandatoryTitleLbl.getElement().setAttribute("title", i18n.GL0173());
        descCharcterLimit.getElement().setInnerText(i18n.GL0143());
        descCharcterLimit.getElement().setId("pnlDescCharcterLimit");
        descCharcterLimit.getElement().setAttribute("alt", i18n.GL0143());
        descCharcterLimit.getElement().setAttribute("title", i18n.GL0143());
        mandatoryCategoryLbl.setText(i18n.GL1515());
        mandatoryCategoryLbl.getElement().setId("lblMandatoryCategoryLbl");
        mandatoryCategoryLbl.getElement().setAttribute("alt", i18n.GL1515());
        mandatoryCategoryLbl.getElement().setAttribute("title", i18n.GL1515());
        thumbnailLbl.getElement().setInnerText(i18n.GL0911());
        thumbnailLbl.getElement().setId("pnlThumbnailLbl");
        thumbnailLbl.getElement().setAttribute("alt", i18n.GL0911());
        thumbnailLbl.getElement().setAttribute("title", i18n.GL0911());
        titleTextPanel.getElement().setInnerText(i18n.GL0318()+i18n.GL_SPL_STAR());
        titleTextPanel.getElement().setId("pnlTitleTextPanel");
        titleTextPanel.getElement().setAttribute("alt", i18n.GL0318()+i18n.GL_SPL_STAR());
        titleTextPanel.getElement().setAttribute("title", i18n.GL0318()+i18n.GL_SPL_STAR());
        
        videoPanel.getElement().setInnerHTML(i18n.GL0918());
        videoPanel.getElement().setId("pnlVideoPanel");
        videoPanel.getElement().setAttribute("alt", i18n.GL0918());
        videoPanel.getElement().setAttribute("title", i18n.GL0918());
        
        interactivePanel.getElement().setInnerHTML(i18n.GL0919());
        interactivePanel.getElement().setId("pnlInteractivePanel");
        interactivePanel.getElement().setAttribute("alt", i18n.GL0919());
        interactivePanel.getElement().setAttribute("title", i18n.GL0919());
        
		websitePanel.getElement().setInnerHTML(i18n.GL1396());
		websitePanel.getElement().setId("pnlWebsitePanel");
		websitePanel.getElement().setAttribute("alt", i18n.GL1396());
		websitePanel.getElement().setAttribute("title", i18n.GL1396());
		textsPanel.getElement().setInnerHTML(i18n.GL1044());
		textsPanel.getElement().setId("pnlTextsPanel");
		textsPanel.getElement().setAttribute("alt", i18n.GL1044());
		textsPanel.getElement().setAttribute("title", i18n.GL1044());
		audioPanel.getElement().setInnerHTML(i18n.GL1045());
		audioPanel.getElement().setId("pnlAudioPanel");
		audioPanel.getElement().setAttribute("alt", i18n.GL1045());
		audioPanel.getElement().setAttribute("title", i18n.GL1045());
		imagePanel.getElement().setInnerHTML(i18n.GL1046());
		imagePanel.getElement().setId("pnlImagePanel");
		imagePanel.getElement().setAttribute("alt", i18n.GL1046());
		imagePanel.getElement().setAttribute("title", i18n.GL1046());
		generateImageLbl.setText(i18n.GL0922());
		generateImageLbl.getElement().setId("lblGenerateImageLbl");
		generateImageLbl.getElement().setAttribute("alt", i18n.GL0922());
		generateImageLbl.getElement().setAttribute("title", i18n.GL0922());
		orLbl.getElement().setInnerText(i18n.GL_GRR_Hyphen()+i18n.GL0209().toLowerCase()+i18n.GL_GRR_Hyphen());
		orLbl.getElement().setId("pnlOrLbl");
		orLbl.getElement().setAttribute("alt", i18n.GL_GRR_Hyphen()+i18n.GL0209().toLowerCase()+i18n.GL_GRR_Hyphen());
		orLbl.getElement().setAttribute("title", i18n.GL_GRR_Hyphen()+i18n.GL0209().toLowerCase()+i18n.GL_GRR_Hyphen());
		uploadImageLbl.setText(i18n.GL0912());
		uploadImageLbl.getElement().setId("lblUploadImageLbl");
		uploadImageLbl.getElement().setAttribute("alt", i18n.GL0912());
		uploadImageLbl.getElement().setAttribute("title", i18n.GL0912());
		refreshLblPanel.getElement().setInnerText(i18n.GL0923());
		refreshLblPanel.getElement().setId("pnlRefreshLblPanel");
		refreshLblPanel.getElement().setAttribute("alt", i18n.GL0923());
		refreshLblPanel.getElement().setAttribute("title", i18n.GL0923());
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
		termsAndPolicyAnr.getElement().setAttribute("alt", i18n.GL0872()+i18n.GL_GRR_COMMA());
		termsAndPolicyAnr.getElement().setAttribute("title", i18n.GL0872()+i18n.GL_GRR_COMMA());
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
		addResourceBtn.setText(i18n.GL0590());
		addResourceBtn.getElement().setId("btnAddResourceBtn");
		addResourceBtn.getElement().setAttribute("alt", i18n.GL0141());
		addResourceBtn.getElement().setAttribute("title", i18n.GL0141());
		cancelResourcePopupBtnLbl.setText(i18n.GL0142());
		cancelResourcePopupBtnLbl.getElement().setId("lnkCancelResourcePopupBtnLbl");
		cancelResourcePopupBtnLbl.getElement().setAttribute("alt", i18n.GL0142());
		cancelResourcePopupBtnLbl.getElement().setAttribute("title", i18n.GL0142());
		loadingTextLbl.setText(i18n.GL0808().toLowerCase());
		loadingTextLbl.getElement().setId("lblLoadingTextLbl");
		loadingTextLbl.getElement().setAttribute("alt", i18n.GL0808().toLowerCase());
		loadingTextLbl.getElement().setAttribute("title", i18n.GL0808().toLowerCase());
//		otherPanel.getElement().setInnerHTML(MessageProperties.i18n.GL1047);  
		resourceFormat.getElement().setInnerHTML(i18n.GL3103()+i18n.GL_SPL_STAR()); 
		resourceFormat.getElement().setId("pnlResourceFormat");
		resourceFormat.getElement().setAttribute("alt", i18n.GL0906());
		resourceFormat.getElement().setAttribute("title", i18n.GL0906());
		
		resDescription.getElement().setInnerHTML(i18n.GL0904()); 
		resDescription.getElement().setId("pnlResDescription");
		resDescription.getElement().setAttribute("alt", i18n.GL0904());
		resDescription.getElement().setAttribute("title", i18n.GL0904());
		
		urlTextPanel.getElement().setInnerHTML(i18n.GL0915());
		urlTextPanel.getElement().setId("pnlUrlTextPanel");
		urlTextPanel.getElement().setAttribute("alt", i18n.GL0915());
		urlTextPanel.getElement().setAttribute("title", i18n.GL0915());
		
		
		mandatoryUrlLbl.setText(i18n.GL0916());
		mandatoryUrlLbl.getElement().setId("lblMandatoryUrlLbl");
		mandatoryUrlLbl.getElement().setAttribute("alt", i18n.GL0916());
		mandatoryUrlLbl.getElement().setAttribute("title", i18n.GL0916());
		
		momentsOfLearningPanel.setVisible(false);
		momentsOfLearningPanel.getElement().setId("pnlMomentsOfLearningPanel");
		educationalTitle.getElement().setInnerHTML(i18n.GL1664());
		educationalTitle.getElement().setId("lblEducationalTitle");
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
		educationalUsePanel.getElement().setId("pnlEducationalUsePanel");
		educationalUsePanel.setVisible(false);
		
		standardsDefaultText.setText(i18n.GL1682());
		standardsDefaultText.getElement().setId("lblStandardsDefaultText");
		standardsDefaultText.getElement().setAttribute("alt", i18n.GL1682());
		standardsDefaultText.getElement().setAttribute("title", i18n.GL1682());
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
		resourceEducationalLabel.setText(i18n.GL1684());
		resourceEducationalLabel.getElement().setId("lblResourceEducationalLabel");
		resourceEducationalLabel.getElement().setAttribute("alt", i18n.GL1684());
		resourceEducationalLabel.getElement().setAttribute("title", i18n.GL1684());
		resourcemomentsOfLearningLabel.setText(i18n.GL1684());
		resourcemomentsOfLearningLabel.getElement().setId("lblResourcemomentsOfLearningLabel");
		resourcemomentsOfLearningLabel.getElement().setAttribute("alt", i18n.GL1684());
		resourcemomentsOfLearningLabel.getElement().setAttribute("title", i18n.GL1684());
		urlTextLbl.getElement().setId("lblUrlTextLbl");
		resourceCategoryLabel.getElement().setId("lblResourceCategoryLabel");
		resoureDropDownLbl.getElement().setId("lblResoureDropDownLbl");
		resourceTypePanel.getElement().setId("pnlResourceTypePanel");
		videoResourcePanel.getElement().setId("epnlVideoResourcePanel");
		interactiveResourcePanel.getElement().setId("epnlInteractiveResourcePanel");
		websiteResourcePanel.getElement().setId("epnlWebsiteResourcePanel");
		imageResourcePanel.getElement().setId("epnlImageResourcePanel");
		textResourcePanel.getElement().setId("epnlTextResourcePanel");
		audioResourcePanel.getElement().setId("epnlAudioResourcePanel");
		video.getElement().setId("pnlVideo");
		interactive.getElement().setId("pnlInteractive");
		website.getElement().setId("pnlWebsite");
		image.getElement().setId("pnlImage");
		texts.getElement().setId("pnlTexts");
		audio.getElement().setId("pnlAudio");
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
		leftArrowLbl.getElement().setId("lblLeftArrowLbl");
		rightArrowLbl.getElement().setId("lblRightArrowLbl");
		refreshLbl.getElement().setId("lblRefreshLbl");
		rightsContainer.getElement().setId("pnlRightsContainer");
		lblContentRights.getElement().setId("epnlLblContentRights");
		cancelResourcePopupBtnLbl.getElement().setAttribute("style", "margin-left:10px");
		
		 	videoClickHandler=videoResourcePanel.addClickHandler(new checkAvailableClickHandler());
		 	websiteClickHandler=websiteResourcePanel.addClickHandler(new checkAvailableClickHandler());
		 	interactiveClickHandler = interactiveResourcePanel.addClickHandler(new checkAvailableClickHandler());
		 	imageClickHandler=imageResourcePanel.addClickHandler(new checkAvailableClickHandler());
		 	textClickHandler = textResourcePanel.addClickHandler(new checkAvailableClickHandler());
		 	audioClickHandler = audioResourcePanel.addClickHandler(new checkAvailableClickHandler());
		
		displayResourceInfo();
		show();
		center();
		AppClientFactory.fireEvent(new GetEditPageHeightEvent(this, false));
		getResourceMetaInfo(collectionItemDo.getResource().getUrl());
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
		mediaDropdownArrowConatainer.getElement().getStyle().setRight(10, Unit.PX);
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
		

		mediaLabel.setText("Media Feature");
		mediaLabel.getElement().setId("lblMediaFeature");
		mediaLabel.getElement().setAttribute("alt","Media Feature");
		mediaLabel.getElement().setAttribute("title","Media Feature");
		mediaLabel.getElement().getStyle().setDisplay(Display.INLINE);
		
		if(mobileFeature!=null){
			if(mobileFeature.equalsIgnoreCase(""))
			{
				lblMediaPlaceHolder.setText("Choose a Media Feature Option:");	
			}
		}
		else
		{
			lblMediaPlaceHolder.setText("Choose a Media Feature Option:");
		}
		lblMediaPlaceHolder.getElement().setId("phMediaFeature");
		lblMediaPlaceHolder.getElement().setAttribute("alt","Choose a Media Feature Option:");
		lblMediaPlaceHolder.getElement().setAttribute("title","Choose a Media Feature Option:");
		
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
		
		browseStandards.addClickHandler(new onBrowseStandardsClick());
		
		ClickHandler rootHandler= new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
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
	
	public abstract void browseStandardsInfo(boolean val,boolean userResource);
	public abstract void closeStandardsPopup();
	
	private class onBrowseStandardsClick implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			isQuestionResource= false;
			isUserResource = false;
			browseStandardsInfo(isQuestionResource,isUserResource);
		}
	}
	public void onLoad(){
		super.onLoad();
		Scheduler.get().scheduleDeferred(new ScheduledCommand(){

			@Override
			public void execute() {
				setResourceDescription();
			}
        });
	}
	private void OpenMediaFeatureDropdown() {
		hasClickedOnDropDwn=true;
		if (spanelMediaFeaturePanel.isVisible()){
			spanelMediaFeaturePanel.setVisible(false);
		}else{
			spanelMediaFeaturePanel.setVisible(true);
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

	@UiHandler("cancelResourcePopupBtnLbl")
	public void cancelPopUp(ClickEvent clickEvent) {
		AppClientFactory.fireEvent(new GetEditPageHeightEvent(this, true));

		Window.enableScrolling(true);
        AppClientFactory.fireEvent(new SetHeaderZIndexEvent(0, true));
        if(deletedStandardsDo.size()>0){
        	AppClientFactory.getInjector().getResourceService().UpdateResourceTaxonomy(collectionItemDo.getResource().getGooruOid(), deletedStandardsDo, new SimpleAsyncCallback<Void>() {
				
				@Override
				public void onSuccess(Void result) {
					deletedStandardsDo.clear();
				}
			});
        }
		hide();
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
	public void getResourceMetaInfo(String url) {
		AppClientFactory
				.getInjector()
				.getResourceService()
				.getResourceMetaInfo(url,
						new SimpleAsyncCallback<ResourceMetaInfoDo>() {

							@Override
							public void onSuccess(ResourceMetaInfoDo result) {
								setData(result);
								
							}
						});
	}

	public void setData(ResourceMetaInfoDo result) {
		setResMetaInfo(result);
		if(result.getImages()!=null && result.getImages().size()>1){
			rightArrowLbl.setVisible(true);
		}else{
			rightArrowLbl.setVisible(false);
		}
		setThumbnailImages(result.getImages());
		updateUi();
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
	private void setThumbnailImages(List<String> images) {
		thumbnailImagesLink = images;
	}

	private void setResMetaInfo(ResourceMetaInfoDo result) {
		this.resMetaInfoDo = result;
	}
	
	public void setResourceDescription(){
		descriptionTxtAera.setText(collectionItemDo.getResource().getDescription());
		descriptionTxtAera.getElement().setAttribute("alt", collectionItemDo.getResource().getDescription());
		descriptionTxtAera.getElement().setAttribute("title", collectionItemDo.getResource().getDescription());
	}

	public void displayResourceInfo() {
		updateStandardsAdvancedSetupStyle();
		String url = collectionItemDo.getResource().getUrl();
		if(collectionItemDo.getResource().getResourceTags()!=null){
			for(int i=0;i<collectionItemDo.getResource().getResourceTags().size();i++){
				tagListGlobal.add("\""+collectionItemDo.getResource().getResourceTags().get(i).getLabel()+"\"");
				if(collectionItemDo.getResource().getResourceTags().get(i).getLabel().contains("Media Feature"))
				{
					setMediaFeatureObjectVal(collectionItemDo.getResource().getResourceTags().get(i).getLabel());
				}
				if(collectionItemDo.getResource().getResourceTags().get(i).getLabel().contains("Mobile Friendly"))
				{
					setMobileFriendlyObjectVal(collectionItemDo.getResource().getResourceTags().get(i).getLabel());
				}
				
				if(collectionItemDo.getResource().getResourceTags().get(i).getLabel().contains("Access Hazard"))
				{
					setAccessHazardObjectVal(collectionItemDo.getResource().getResourceTags().get(i).getLabel());
				}
			}
		}
		urlTextLbl.setText(url);
		urlTextLbl.getElement().setAttribute("alt", i18n.GL0827());
		urlTextLbl.getElement().setAttribute("title", i18n.GL0827());
		
		if(urlTextLbl.getText().contains("youtube")){
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
					}else{
						websiteClickHandler=websiteResourcePanel.addClickHandler(new checkAvailableClickHandler());
						interactiveClickHandler = interactiveResourcePanel.addClickHandler(new checkAvailableClickHandler());
						imageClickHandler=imageResourcePanel.addClickHandler(new checkAvailableClickHandler());
						textClickHandler = textResourcePanel.addClickHandler(new checkAvailableClickHandler());
						audioClickHandler = audioResourcePanel.addClickHandler(new checkAvailableClickHandler());
					}
		
		if (collectionItemDo.getResource().getDescription().length() >= 300) {
			descriptionTxtAera.setText(collectionItemDo.getResource()
					.getDescription().substring(0, 300));
			descriptionTxtAera.getElement().setAttribute("alt", collectionItemDo.getResource().getDescription());
			descriptionTxtAera.getElement().setAttribute("title", collectionItemDo.getResource().getDescription());
		} else {
			descriptionTxtAera.setText(collectionItemDo.getResource()
					.getDescription());
			descriptionTxtAera.getElement().setAttribute("alt", collectionItemDo.getResource().getDescription());
			descriptionTxtAera.getElement().setAttribute("title", collectionItemDo.getResource().getDescription());
		}
		if (collectionItemDo.getResource().getTitle().length() >= 50) {
			titleTextBox.setText(collectionItemDo.getResource().getTitle()
					.substring(0, 50));
			titleTextBox.getElement().setAttribute("alt", collectionItemDo.getResource().getTitle());
			titleTextBox.getElement().setAttribute("title", collectionItemDo.getResource().getTitle());
		} else {
			titleTextBox.setText(collectionItemDo.getResource().getTitle());
			titleTextBox.getElement().setAttribute("alt", collectionItemDo.getResource().getTitle());
			titleTextBox.getElement().setAttribute("title", collectionItemDo.getResource().getTitle());
		}

		setThumbnailImage.setVisible(true);
		String category = collectionItemDo.getResource().getCategory().trim();
		
		if (category.equalsIgnoreCase("Video")||category.equalsIgnoreCase("Videos")) {
			resourceCategoryLabel.setText(i18n.GL0918());
			resourceCategoryLabel.getElement().setAttribute("alt", i18n.GL0918());
			resourceCategoryLabel.getElement().setAttribute("title", i18n.GL0918());
			categorypanel.setStyleName(video.getStyleName());
			resourceTypePanel.setVisible(true);
			resoureDropDownLblOpen=false;
			videoResourcePanel.addStyleName("active");
		} else if (category.equalsIgnoreCase("Interactive")) {
			resourceCategoryLabel.setText(i18n.GL0919());
			resourceCategoryLabel.getElement().setAttribute("alt", i18n.GL0919());
			resourceCategoryLabel.getElement().setAttribute("title",i18n.GL0919());
			categorypanel.setStyleName(interactive.getStyleName());
			resourceTypePanel.setVisible(true);
			resoureDropDownLblOpen=false;
			interactiveResourcePanel.addStyleName("active");
		} else if (category.equalsIgnoreCase("Website")||category.equalsIgnoreCase("websites")||category.equalsIgnoreCase("webpage")) {
			resourceCategoryLabel.setText(i18n.GL1396());
			resourceCategoryLabel.getElement().setAttribute("alt", i18n.GL1396());
			resourceCategoryLabel.getElement().setAttribute("title", i18n.GL1396());
			categorypanel.setStyleName(website.getStyleName());
			resourceTypePanel.setVisible(true);
			resoureDropDownLblOpen=false;
			websiteResourcePanel.addStyleName("active");
		}
		else if(category.equalsIgnoreCase("audio")) {
			resourceCategoryLabel.setText(i18n.GL1045());
			resourceCategoryLabel.getElement().setAttribute("alt", i18n.GL1045());
			resourceCategoryLabel.getElement().setAttribute("title",i18n.GL1045());
			categorypanel.setStyleName(audio.getStyleName());
			resourceTypePanel.setVisible(true);
			resoureDropDownLblOpen=false;
			audioResourcePanel.addStyleName("active");
		}
	//	else if((category.equalsIgnoreCase("texts")||(category.equalsIgnoreCase("Slide")||(category.equalsIgnoreCase("Handout")||(category.equalsIgnoreCase("Textbook")||(category.equalsIgnoreCase("Lesson")) {
		else if(category.equalsIgnoreCase("texts")||category.equalsIgnoreCase("text")) {
				resourceCategoryLabel.setText(i18n.GL1044());
				resourceCategoryLabel.getElement().setAttribute("alt", i18n.GL1044());
				resourceCategoryLabel.getElement().setAttribute("title", i18n.GL1044());
			categorypanel.setStyleName(texts.getStyleName());
			resourceTypePanel.setVisible(true);
			resoureDropDownLblOpen=false;
			textResourcePanel.addStyleName("active");
		}
		else if (category.equalsIgnoreCase("image")) {
			resourceCategoryLabel.setText(i18n.GL1046());
			resourceCategoryLabel.getElement().setAttribute("alt", i18n.GL1046());
			resourceCategoryLabel.getElement().setAttribute("title", i18n.GL1046());
			categorypanel.setStyleName(image.getStyleName());
			resourceTypePanel.setVisible(true);
			resoureDropDownLblOpen=false;
			imageResourcePanel.addStyleName("active");
		}
//		else if (category.equalsIgnoreCase("other")) {
//			resourceCategoryLabel.setText(MessageProperties.i18n.GL1047);
//			categorypanel.setStyleName(other.getStyleName());
//			resourceTypePanel.setVisible(false);
//			resoureDropDownLblOpen=false;
//		} 
		else if (category.equalsIgnoreCase("Slide")) {
			resourceCategoryLabel.setText(i18n.GL1046());
			resourceCategoryLabel.getElement().setAttribute("alt", i18n.GL1046());
			resourceCategoryLabel.getElement().setAttribute("title", i18n.GL1046());
			categorypanel.setStyleName(image.getStyleName());
			resourceTypePanel.setVisible(true);
			resoureDropDownLblOpen=false;
		} else if (category.equalsIgnoreCase("Handout")) {
			resourceCategoryLabel.setText(i18n.GL1044());
			resourceCategoryLabel.getElement().setAttribute("alt", i18n.GL1044());
			resourceCategoryLabel.getElement().setAttribute("title", i18n.GL1044());
			categorypanel.setStyleName(texts.getStyleName());
			resourceTypePanel.setVisible(true);
			resoureDropDownLblOpen=false;
		} else if (category.equalsIgnoreCase("Textbook")) {
			resourceCategoryLabel.setText(i18n.GL1044());
			resourceCategoryLabel.getElement().setAttribute("alt", i18n.GL1044());
			resourceCategoryLabel.getElement().setAttribute("title", i18n.GL1044());
			categorypanel.setStyleName(texts.getStyleName());
			resourceTypePanel.setVisible(true);
			resoureDropDownLblOpen=false;
		} else if (category.equalsIgnoreCase("Lesson")) {
			resourceCategoryLabel.setText(i18n.GL1044());
			resourceCategoryLabel.getElement().setAttribute("alt", i18n.GL1044());
			resourceCategoryLabel.getElement().setAttribute("title", i18n.GL1044());
			categorypanel.setStyleName(texts.getStyleName());
			resourceTypePanel.setVisible(true);
			resoureDropDownLblOpen=false;
//		} else if (category.equalsIgnoreCase("Question")) {
//			resourceCategoryLabel.setText("Question");
//			categorypanel.setStyleName(question.getStyleName());
//			resourceTypePanel.setVisible(false);
//			resoureDropDownLblOpen=false;
		} else if (category.equalsIgnoreCase("Exam")) {
			resourceCategoryLabel.setText(i18n.GL1396());
			resourceCategoryLabel.getElement().setAttribute("alt", i18n.GL1396());
			resourceCategoryLabel.getElement().setAttribute("title", i18n.GL1396());
			categorypanel.setStyleName(website.getStyleName());
			resourceTypePanel.setVisible(true);
			resoureDropDownLblOpen=false;
		}

		thumbnailUrlStr = collectionItemDo.getResource().getThumbnails() != null ? collectionItemDo.getResource().getThumbnails().getUrl() : null;
		setImage(thumbnailUrlStr, category);
		
		if( collectionItemDo.getResource().getEducationalUse()!=null){
			for (checkboxSelectedDo item : collectionItemDo.getResource().getEducationalUse()) {
				
				   if(item.isSelected()){
					    resourceEducationalLabel.setText(item.getValue());
						educationalUsePanel.setVisible(false);
						educationalDropDownLblOpen = false;
						mandatoryEducationalLbl.setVisible(false);
						setAdvancedOptionsStyles();
				   }
				}
		}
		if(collectionItemDo.getResource().getMomentsOfLearning()!=null){
			for (checkboxSelectedDo item : collectionItemDo.getResource().getMomentsOfLearning()) {	
				   if(item.isSelected()){
					   resourcemomentsOfLearningLabel.setText(item.getValue());
					   resourcemomentsOfLearningLabel.getElement().setAttribute("alt", item.getValue());
						resourcemomentsOfLearningLabel.getElement().setAttribute("title", item.getValue());
					   momentsOfLearningPanel.setVisible(false);
					   momentsOfLearningOpen = false;
					   mandatorymomentsOfLearninglLbl.setVisible(false);
					   setAdvancedOptionsStyles();
				   }
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
			updateStandardsAdvancedSetupStyle();
		}
		
	}

	public void setImage(String thumbnailUrlImage, String category){
		if (thumbnailUrlImage.endsWith("null")) {
			thumbnailUrlImage = DEFULT_IMAGE_PREFIX + category.toLowerCase() + PNG;
		} 
		if (thumbnailUrlImage!=null && thumbnailUrlImage.indexOf("youtube") >0){
			String urlStr = urlTextLbl.getText().trim();
			String youTubeIbStr = ResourceImageUtil.getYoutubeVideoId(urlStr);
			thumbnailUrlImage = "http://img.youtube.com/vi/"+youTubeIbStr+"/1.jpg";
		}
		setThumbnailImage.setUrl(thumbnailUrlImage);
	}
	public void updateUi() {
		generateImageLbl.setVisible(false);
		setThumbnailImage.setVisible(true);
		if (urlTextLbl.getText().indexOf("youtube") != -1) {
			rightArrowLbl.setVisible(false);
		}
	}

	private class AddClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			final Map<String, String> parms = new HashMap<String, String>();
			parms.put("text", titleTextBox.getValue());
			AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {
				@Override
				public void onSuccess(Boolean value) {
						isHavingBadWordsInTextbox = value;
						if(value){
							SetStyleForProfanity.SetStyleForProfanityForTextBox(titleTextBox, mandatoryTitleLblForSwareWords,value);
						}else{
							parms.put("text", descriptionTxtAera.getText());
							AppClientFactory.getInjector().getResourceService().checkProfanity(parms,new SimpleAsyncCallback<Boolean>() {
								
								@Override
								public void onSuccess(Boolean result) {
									isHavingBadWordsInRichText=result;
									if(result){
										SetStyleForProfanity.SetStyleForProfanityForTextArea(descriptionTxtAera, mandatoryDescLblForSwareWords, result);
									}else{
										if (!isHavingBadWordsInRichText && !isHavingBadWordsInTextbox) {
											boolean isValidate = true;
											
											String titleStr = titleTextBox.getText().trim();
											String categoryStr =resourceCategoryLabel.getText();// resourceTypeListBox.getItemText(resourceTypeListBox.getSelectedIndex());
											
											String urlStr = urlTextLbl.getText().trim();
											String youTubeId = getYoutubeVideoId(urlStr);

											
											if (titleStr.toLowerCase().contains("http://") || titleStr.toLowerCase().contains("https://") || titleStr.toLowerCase().contains("ftp://")){
												showTitleErrorMessage(i18n.GL0323());
												isValidate = false;
											}
											
											if(!rightsChkBox.getValue()){
												rightsLbl.getElement().getStyle().setColor("orange");
												isValidate = false;
											}
											if (titleStr == null || titleStr.equalsIgnoreCase("")) {
												showTitleErrorMessage(i18n.GL0173());
												isValidate = false;
											}
											if (categoryStr == null
													|| categoryStr.equalsIgnoreCase("-1")
													|| categoryStr
															.equalsIgnoreCase("Choose a resource format")) {
												showCategoryErrorMessage(i18n.GL0917());
												
												isValidate = false;
											}
											
											if(urlStr.indexOf("youtube")!=-1){
												
												if(youTubeId==null || youTubeId.equalsIgnoreCase("null") || youTubeId.equalsIgnoreCase("")){
													if(!categoryStr.equalsIgnoreCase("Webpage")){
														isValidate = true;									
													}else{
														
														showCategoryErrorMessage(i18n.GL0927());
														
														isValidate = false;													}
												}
												
											}
											if(mobileYes.getStyleName().contains(AddTagesCBundle.INSTANCE.css().OffButtonsActive()))
											{
												
												tagList.add("Mobile Friendly : "+i18n.GL_GRR_YES());
												
											}
											else if(mobileNo.getStyleName().contains(AddTagesCBundle.INSTANCE.css().OffButtonsActive()))
											{
											
												tagList.add("Mobile Friendly : "+i18n.GL1735());
												
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
											if (isValidate) {
												saveButtonContainer.setVisible(false);
												loadingTextLbl.setVisible(true);
												collectionItemDo.getResource().setTitle(titleStr);
												collectionItemDo.getResource().setDescription(descriptionTxtAera.getText().trim());
												if(categoryStr.contains("Videos")||categoryStr.contains("Interactives")||categoryStr.contains("Images")||categoryStr.contains("Texts"))
												{
													categoryStr=categoryStr.substring(0, categoryStr.length()-1);
													/* if(categoryStr.contains("Image")||categoryStr.contains("Images")){
														 categoryStr="Slide";
													 }*/
												}
												collectionItemDo.getResource().setCategory(categoryStr);
												
												if (thumbnailUrlStr!=null){
													collectionItemDo.getResource().getThumbnails().setUrl(thumbnailUrlStr);
												}else{
													collectionItemDo.getResource().getThumbnails().setUrl(null);
												}
//												collectionItemDo.getResource().setUrl(urlStr);
												if(!resourceEducationalLabel.getText().equalsIgnoreCase(i18n.GL1684())){
													ArrayList<checkboxSelectedDo> arrayOfEducational=new ArrayList<checkboxSelectedDo>();
													checkboxSelectedDo educationalOfObj=new checkboxSelectedDo();
													educationalOfObj.setSelected(true);
													educationalOfObj.setValue(resourceEducationalLabel.getText());
													arrayOfEducational.add(educationalOfObj);
													if(collectionOriginalItemDo.getResource().getEducationalUse() != null)
													{
													for(int eduI=0; eduI<collectionOriginalItemDo.getResource().getEducationalUse().size(); eduI++)
													{
														if(!resourceEducationalLabel.getText().equalsIgnoreCase(collectionOriginalItemDo.getResource().getEducationalUse().get(eduI).getValue()))
														{
															checkboxSelectedDo eduUseObjPrevious=new checkboxSelectedDo();
															eduUseObjPrevious.setSelected(false);
															eduUseObjPrevious.setValue(collectionOriginalItemDo.getResource().getEducationalUse().get(eduI).getValue());
															arrayOfEducational.add(eduUseObjPrevious);
														}
													}
													}
													collectionItemDo.getResource().setEducationalUse(arrayOfEducational);
												}else{
													ArrayList<checkboxSelectedDo> arrayOfEducational=new ArrayList<checkboxSelectedDo>();
													checkboxSelectedDo educationalOfObj=new checkboxSelectedDo();
												if(resourceEducationalLabel.getText().equalsIgnoreCase(i18n.GL1684())){
													if(collectionOriginalItemDo.getResource().getEducationalUse() != null){
														for(int eduI=0; eduI<collectionOriginalItemDo.getResource().getEducationalUse().size(); eduI++)
														{
															if(!resourceEducationalLabel.getText().equalsIgnoreCase(collectionOriginalItemDo.getResource().getEducationalUse().get(eduI).getValue()))
															{
																checkboxSelectedDo eduUseObjPrevious=new checkboxSelectedDo();
																eduUseObjPrevious.setSelected(false);
																eduUseObjPrevious.setValue(collectionOriginalItemDo.getResource().getEducationalUse().get(eduI).getValue());
																arrayOfEducational.add(eduUseObjPrevious);
															}
														}
													}
														collectionItemDo.getResource().setEducationalUse(arrayOfEducational);
													}
												}
												if(!resourcemomentsOfLearningLabel.getText().equalsIgnoreCase(i18n.GL1684())){
													ArrayList<checkboxSelectedDo> arrayOfMoments=new ArrayList<checkboxSelectedDo>();
													checkboxSelectedDo momentsOfObj=new checkboxSelectedDo();
													momentsOfObj.setSelected(true);
													momentsOfObj.setValue(resourcemomentsOfLearningLabel.getText());
													arrayOfMoments.add(momentsOfObj);
													
													if(collectionOriginalItemDo.getResource().getMomentsOfLearning() != null)
													{
													for(int momentsI=0; momentsI<collectionOriginalItemDo.getResource().getMomentsOfLearning().size(); momentsI++)
													{
														if(!resourcemomentsOfLearningLabel.getText().equalsIgnoreCase(collectionOriginalItemDo.getResource().getMomentsOfLearning().get(momentsI).getValue()))
														{
															checkboxSelectedDo momentsOfObjPrevious=new checkboxSelectedDo();
															momentsOfObjPrevious.setSelected(false);
															momentsOfObjPrevious.setValue(collectionOriginalItemDo.getResource().getMomentsOfLearning().get(momentsI).getValue());
															arrayOfMoments.add(momentsOfObjPrevious);
														}
													}
													}
													collectionItemDo.getResource().setMomentsOfLearning(arrayOfMoments);
												}else{
													ArrayList<checkboxSelectedDo> arrayOfMoments=new ArrayList<checkboxSelectedDo>();
													checkboxSelectedDo momentsOfObj=new checkboxSelectedDo();
													if(resourcemomentsOfLearningLabel.getText().equalsIgnoreCase(i18n.GL1684())){
														if(collectionOriginalItemDo.getResource().getMomentsOfLearning() != null)
														{
														for(int momentsI=0; momentsI<collectionOriginalItemDo.getResource().getMomentsOfLearning().size(); momentsI++)
														{
															if(!resourcemomentsOfLearningLabel.getText().equalsIgnoreCase(collectionOriginalItemDo.getResource().getMomentsOfLearning().get(momentsI).getValue()))
															{
																checkboxSelectedDo momentsOfObjPrevious=new checkboxSelectedDo();
																momentsOfObjPrevious.setSelected(false);
																momentsOfObjPrevious.setValue(collectionOriginalItemDo.getResource().getMomentsOfLearning().get(momentsI).getValue());
																arrayOfMoments.add(momentsOfObjPrevious);
															}
														}
														}
														collectionItemDo.getResource().setMomentsOfLearning(arrayOfMoments);
													}
												}
												collectionItemDo.getResource().setTaxonomySet(standardsDo);
												
												if(tagListGlobal!=null&&tagListGlobal.size()!=0){
												AppClientFactory.getInjector().getResourceService().deleteTagsServiceRequest(collectionItemDo.getResource().getGooruOid(), tagListGlobal.toString(), new AsyncCallback<Void>() {
													
													@Override
													public void onSuccess(Void result) {
														// TODO Auto-generated method stub
														updateResource(collectionItemDo,tagList);	
													}
													
													@Override
													public void onFailure(Throwable caught) {
														// TODO Auto-generated method stub
														
													}
												});
												
											}
											else{
												updateResource(collectionItemDo,tagList);
											}
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
	
	void showTitleErrorMessage(String message){
		mandatoryTitleLbl.setText(message);
		StringUtil.setAttributes(mandatoryTitleLbl.getElement(), "lblMandatoryTitleLbl", message, message);
		mandatoryTitleLbl.setVisible(true);
		
		titleTextBox.getElement().addClassName("errorBorderMessage");
	}
	
	void clearTitleErrorMessage(){
		
		titleTextBox.getElement().removeClassName("errorBorderMessage");
		mandatoryTitleLbl.setVisible(false);
	}
	
	void showCategoryErrorMessage(String message){
		mandatoryCategoryLbl.setText(message);
		StringUtil.setAttributes(mandatoryCategoryLbl.getElement(), "lblMandatoryCategoryLbl", message, message);
		
		mandatoryCategoryLbl.setVisible(true);
		
		panelCategory.getElement().addClassName("errorBorderMessage");
	}
	
	void clearCategoryErrorMessage(){
		
		panelCategory.getElement().removeClassName("errorBorderMessage");
		mandatoryCategoryLbl.setVisible(false);
	}

	private class OnEditImageClick implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			resourceImageUpload();
		}
	}

	public abstract void resourceImageUpload();

	private class TitleKeyUpHandler implements KeyUpHandler {

		public void onKeyUp(KeyUpEvent event) {
			clearTitleErrorMessage();
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
			descCharcterLimit.setVisible(false);
			if (descriptionTxtAera.getText().length() >= 300) {
				descriptionTxtAera.setText(descriptionTxtAera.getText().trim().substring(0,300));
				descriptionTxtAera.getElement().setAttribute("alt", descriptionTxtAera.getText());
				descriptionTxtAera.getElement().setAttribute("title", descriptionTxtAera.getText());
				descCharcterLimit.setVisible(true);
			}
		}
	}

	@UiHandler("leftArrowLbl")
	void leftArrowClick(ClickEvent event) {
		activeImageIndex--;
		setImageThumbnail();
	}

	@UiHandler("rightArrowLbl")
	void rightArrowClick(ClickEvent event) {
		activeImageIndex++;
		setImageThumbnail();
	}
	
	void setVideoCategory(){
		resourceCategoryLabel.setText(i18n.GL0918());
		resourceCategoryLabel.getElement().setAttribute("alt", i18n.GL0918());
		resourceCategoryLabel.getElement().setAttribute("title", i18n.GL0918());
		//resourceCategoryLabel.setStyleName(video.getStyleName());
		categorypanel.setStyleName(video.getStyleName());
		resourceTypePanel.setVisible(true);
		resoureDropDownLblOpen=false;
		videoResourcePanel.addStyleName("active");
		websiteResourcePanel.removeStyleName("active");
		interactiveResourcePanel.removeStyleName("active");
		audioResourcePanel.removeStyleName("active");
		textResourcePanel.removeStyleName("active");
		imageResourcePanel.removeStyleName("active");
	}
	
	void setInteractiveCategory(){
		resourceCategoryLabel.setText(i18n.GL0919());
		resourceCategoryLabel.getElement().setAttribute("alt", i18n.GL0919());
		resourceCategoryLabel.getElement().setAttribute("title", i18n.GL0919());
		categorypanel.setStyleName(interactive.getStyleName());
		resourceTypePanel.setVisible(true);
		resoureDropDownLblOpen=false;
		interactiveResourcePanel.addStyleName("active");
		websiteResourcePanel.removeStyleName("active");
		audioResourcePanel.removeStyleName("active");
		textResourcePanel.removeStyleName("active");
		videoResourcePanel.removeStyleName("active");
		imageResourcePanel.removeStyleName("active");
	}
	
	void setWebsiteCategory(){
		resourceCategoryLabel.setText(i18n.GL1396());
		resourceCategoryLabel.getElement().setAttribute("alt", i18n.GL1396());
		resourceCategoryLabel.getElement().setAttribute("title", i18n.GL1396());
		categorypanel.setStyleName(website.getStyleName());
		resourceTypePanel.setVisible(true);
		resoureDropDownLblOpen=false;
		websiteResourcePanel.addStyleName("active");
		interactiveResourcePanel.removeStyleName("active");
		audioResourcePanel.removeStyleName("active");
		textResourcePanel.removeStyleName("active");
		videoResourcePanel.removeStyleName("active");
		imageResourcePanel.removeStyleName("active");
	}
	
	void setImageCategory() {
		resourceCategoryLabel.setText(i18n.GL1046());
		resourceCategoryLabel.getElement().setAttribute("alt", i18n.GL1046());
		resourceCategoryLabel.getElement().setAttribute("title", i18n.GL1046());
		categorypanel.setStyleName(image.getStyleName());
		resourceTypePanel.setVisible(true);
		resoureDropDownLblOpen = false;
		mandatoryCategoryLbl.setVisible(false);
		imageResourcePanel.addStyleName("active");
		websiteResourcePanel.removeStyleName("active");
		interactiveResourcePanel.removeStyleName("active");
		audioResourcePanel.removeStyleName("active");
		textResourcePanel.removeStyleName("active");
		videoResourcePanel.removeStyleName("active");
		clearCategoryErrorMessage();

	}

	
	void setTextCategory() {
		resourceCategoryLabel.setText(i18n.GL1044());
		resourceCategoryLabel.getElement().setAttribute("alt", i18n.GL1044());
		resourceCategoryLabel.getElement().setAttribute("title", i18n.GL1044());
		categorypanel.setStyleName(texts.getStyleName());
		resourceTypePanel.setVisible(true);
		resoureDropDownLblOpen = false;
		mandatoryCategoryLbl.setVisible(false);
		textResourcePanel.addStyleName("active");
		websiteResourcePanel.removeStyleName("active");
		interactiveResourcePanel.removeStyleName("active");
		audioResourcePanel.removeStyleName("active");
		videoResourcePanel.removeStyleName("active");
		imageResourcePanel.removeStyleName("active");
		clearCategoryErrorMessage();
	}

	
	void setAudioCategory() {
		resourceCategoryLabel.setText(i18n.GL1045());
		resourceCategoryLabel.getElement().setAttribute("alt", i18n.GL1045());
		resourceCategoryLabel.getElement().setAttribute("title", i18n.GL1045());
		categorypanel.setStyleName(audio.getStyleName());
		resourceTypePanel.setVisible(true);
		resoureDropDownLblOpen = false;
		mandatoryCategoryLbl.setVisible(false);
		audioResourcePanel.addStyleName("active");
		websiteResourcePanel.removeStyleName("active");
		interactiveResourcePanel.removeStyleName("active");
		textResourcePanel.removeStyleName("active");
		videoResourcePanel.removeStyleName("active");
		imageResourcePanel.removeStyleName("active");
		clearCategoryErrorMessage();
	}

//	@UiHandler("otherResourcePanel")
//	void lessonResourcePanel(ClickEvent event) {
//		resourceCategoryLabel.setText(MessageProperties.i18n.GL1047);
//		categorypanel.setStyleName(other.getStyleName());
//		resourceTypePanel.setVisible(false);
//		resoureDropDownLblOpen = false;
//		mandatoryCategoryLbl.setVisible(false);
//	}
	/*@UiHandler("slideResourcePanel")
	void slideResourcePanel(ClickEvent event){
		resourceCategoryLabel.setText("Slide");
		categorypanel.setStyleName(slide.getStyleName());
		resourceTypePanel.setVisible(false);
		resoureDropDownLblOpen=false;
	}
	@UiHandler("handoutResourcePanel")
	void handoutResourcePanel(ClickEvent event){
		resourceCategoryLabel.setText("Handout");
		categorypanel.setStyleName(handout.getStyleName());
		resourceTypePanel.setVisible(false);
		resoureDropDownLblOpen=false;
	}
	@UiHandler("textbookResourcePanel")
	void textbookResourcePanel(ClickEvent event){
		resourceCategoryLabel.setText("Textbook");
		categorypanel.setStyleName(textbook.getStyleName());
		resourceTypePanel.setVisible(false);
		resoureDropDownLblOpen=false;
	}
	@UiHandler("lessonResourcePanel")
	void lessonResourcePanel(ClickEvent event){
		resourceCategoryLabel.setText("Lesson");
		categorypanel.setStyleName(lesson.getStyleName());
		resourceTypePanel.setVisible(false);
		resoureDropDownLblOpen=false;
	}
//	@UiHandler("questionResourcePanel")
//	void questionResourcePanel(ClickEvent event){
//		resourceCategoryLabel.setText("Question");
//		categorypanel.setStyleName(question.getStyleName());
//		resourceTypePanel.setVisible(false);
//		resoureDropDownLblOpen=false;
//	}
	@UiHandler("examResourcePanel")
	void examResourcePanel(ClickEvent event){
		resourceCategoryLabel.setText("Exam");
		categorypanel.setStyleName(exam.getStyleName());
		resourceTypePanel.setVisible(false);
		resoureDropDownLblOpen=false;
	}*/
	/*@UiHandler("resoureDropDownLbl")
	public void dropDownClick(ClickEvent event){
		if(resoureDropDownLblOpen==false){
			resourceTypePanel.setVisible(true);
			resoureDropDownLblOpen=true;
			
		} else {
			resourceTypePanel.setVisible(false);
			resoureDropDownLblOpen=false;
		}
		
	}*/

	public void setImageThumbnail() {
		
		if (activeImageIndex == 0) {
			leftArrowLbl.setVisible(false);
		} else {
			leftArrowLbl.setVisible(true);
		}
			if (thumbnailImagesLink != null) {
				
			if (activeImageIndex == thumbnailImagesLink.size()) {
				rightArrowLbl.setVisible(false);
			} else {
				rightArrowLbl.setVisible(true);
			}
			setThumbnailImage.setUrl(thumbnailImagesLink.get(activeImageIndex));
			thumbnailUrlStr = thumbnailImagesLink.get(activeImageIndex);
		}
	}

	@UiHandler("refreshLbl")
	void refreshClick(ClickEvent event) {
		String thumbnailUrlStr = collectionItemDo.getResource().getThumbnails() != null ? collectionItemDo.getResource().getThumbnails().getUrl() : null;
		setImage(thumbnailUrlStr, collectionItemDo.getResource().getCategory());
		
		leftArrowLbl.setVisible(false);
		if (urlTextLbl.getText().contains("youtube")) {
			rightArrowLbl.setVisible(false);
		}
	}

	/*public void clearFields() {
		titleTextBox.setText("");
		descriptionTxtAera.setText("");
		//resourceTypeListBox.setSelectedIndex(0);
		generateImageLbl.setVisible(true);
		setThumbnailImage.setUrl("");
	}
*/
	/*private RegExp urlValidator;
	private RegExp urlPlusTldValidator;
*/
	
	/** 
	 * This method is to get the setThumbnailImage
	 */
	public Image getSetThumbnailImage() {
		return setThumbnailImage;
	}

	/** 
	 * This method is to set the setThumbnailImage
	 */
	public void setSetThumbnailImage(Image setThumbnailImage) {
		this.setThumbnailImage = setThumbnailImage;
	}
	
	/** 
	 * This method is to get the thumbnailUrlStr
	 */
	public String getThumbnailUrlStr() {
		return thumbnailUrlStr;
	}

	/** 
	 * This method is to set the thumbnailUrlStr
	 */
	public void setThumbnailUrlStr(String thumbnailUrlStr) {
		this.thumbnailUrlStr = thumbnailUrlStr;
	}
	
	public void setFileNameWithOutRespUrl(String fileNameWithOutRespUrl ){
		this.fileNameWithOutRespUrl = fileNameWithOutRespUrl;
		rightArrowLbl.setVisible(false);
	}
	
	
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
				parms.put("text", textArea.getText());
			}
			addResourceBtn.setEnabled(false);
			AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {

				@Override
				public void onSuccess(Boolean value) {
					addResourceBtn.setEnabled(true);
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
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	@UiHandler("handoutPanel")
	void handoutPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_handout_selected");
		resourceEducationalLabel.setText(i18n.GL0907());
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	@UiHandler("homeworkPanel")
	void homeworkPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_homework_selected");
		resourceEducationalLabel.setText(i18n.GL1666());
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	@UiHandler("gamePanel")
	void gamePanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_game_selected");
		resourceEducationalLabel.setText(i18n.GL1667());
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	@UiHandler("presentationPanel")
	void presentationPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_presentation_selected");
		resourceEducationalLabel.setText(i18n.GL1668());
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	@UiHandler("referenceMaterialPanel")
	void referenceMaterialPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_reference_material_selected");
		resourceEducationalLabel.setText(i18n.GL1669());
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	@UiHandler("quizPanel")
	void quizPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_quiz_selected");
		resourceEducationalLabel.setText(i18n.GL1670());
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	@UiHandler("curriculumPlanPanel")
	void curriculumPlanPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_curriculum_plan_selected");
		resourceEducationalLabel.setText(i18n.GL1671());
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	@UiHandler("lessonPlanPanel")
	void lessonPlanPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_lesson_plan_selected");
		resourceEducationalLabel.setText(i18n.GL1672());
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	@UiHandler("unitPlanPanel")
	void unitPlanPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_unit_plan_selected");
		resourceEducationalLabel.setText(i18n.GL1673());
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	@UiHandler("projectPlanPanel")
	void projectPlanPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_project_plan_selected");
		resourceEducationalLabel.setText(i18n.GL1674());
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	@UiHandler("readingPanel")
	void readingPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_reading_selected");
		resourceEducationalLabel.setText(i18n.GL1675());
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	@UiHandler("textbookPanel")
	void textbookPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_textbook_selected");
		resourceEducationalLabel.setText(i18n.GL0909());
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	@UiHandler("articlePanel")
	void articlePanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_article_selected");
		resourceEducationalLabel.setText(i18n.GL1676());
		educationalUsePanel.setVisible(false);
		educationalDropDownLblOpen = false;
		mandatoryEducationalLbl.setVisible(false);
		setAdvancedOptionsStyles();
	}
	@UiHandler("bookPanel")
	void bookPanel(ClickEvent event) {
		MixpanelUtil.mixpanelEvent("organize_add_resource_book_selected");
		resourceEducationalLabel.setText(i18n.GL1677());
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
	@Override
	public void onSelection(SelectionEvent<Suggestion> event) {
		
		addStandard(standardSgstBox.getValue(), getCodeIdByCode(standardSgstBox.getValue(), standardSearchDo.getSearchResults()));
		standardSgstBox.setText("");
		standardSuggestOracle.clear();
		updateStandardsAdvancedSetupStyle();
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
	public void standardMaxShow() {
		standardSgstBox.addStyleName(CollectionCBundle.INSTANCE.css().standardTxtBox());
		standardMaxMsg.setStyleName(CollectionCBundle.INSTANCE.css().standardMax());
		standardsPanel.addStyleName(CollectionCBundle.INSTANCE.css().floatLeftNeeded());
		new FadeInAndOut(standardMaxMsg.getElement(), 5000, 5000);
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
					if(codeObj.getCodeId()==Integer.parseInt(id)){
						//standardsDo.remove(codeObj);
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
				}
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
	public void setMobileFriendlyObjectVal(String mobileFriendlyVal)
	{
		if(mobileFriendlyVal.contains(i18n.GL_GRR_YES()))
		{
			mobileYes.getElement().setClassName(AddTagesCBundle.INSTANCE.css().OffButtonsActive());
			mobileNo.getElement().setClassName(AddTagesCBundle.INSTANCE.css().OnButtonDeActive());
		}
		else if(mobileFriendlyVal.contains(i18n.GL1735()))
		{
			mobileNo.getElement().setClassName(AddTagesCBundle.INSTANCE.css().OffButtonsActive());
			mobileYes.getElement().setClassName(AddTagesCBundle.INSTANCE.css().OnButtonDeActive());
		}
		updateMobileFriendlyAdvancedStyles();
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
	public void setMediaFeatureObjectVal(String mediaFeatureVal)
	{
		
		try
		{
		if(mediaFeatureVal != null)
		{
			String[] stringArry=mediaFeatureVal.split(" : ");
			if(stringArry.length!=0){
				mobileFeature = stringArry[1].trim();
				lblMediaPlaceHolder.setText(stringArry[1].trim());
				spanelMediaFeaturePanel.setVisible(false);
				//lblMediaPlaceHolder.getElement().setId(titleLabel.getElement().getId());
				lblMediaPlaceHolder.setStyleName(CollectionAssignCBundle.INSTANCE.css().selectedClasspageText());
				setAdvancedOptionsStyles();
			}
			
		}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			
		}
	}
	public void setAccessHazardObjectVal(String accessHazardStr)
	{
		
		String[] stringArry=accessHazardStr.split(" : ");
		if(stringArry.length!=0){
			if(stringArry[1].trim().equalsIgnoreCase(i18n.GL1806()))
			{
					flashingHazard.getElement().addClassName(AddTagesCBundle.INSTANCE.css().select());
			}
			if(stringArry[1].trim().equalsIgnoreCase(i18n.GL1808()))
			{
				motionSimulationHazard.getElement().addClassName(AddTagesCBundle.INSTANCE.css().select());
			}
			if(stringArry[1].trim().equalsIgnoreCase(i18n.GL1810()))
			{
				soundHazard.getElement().addClassName(AddTagesCBundle.INSTANCE.css().select());
			}
			setAdvancedAccessHazardStyles(stringArry.length);
		}
	}
	public void setUpdatedBrowseStandardsVal(String setStandardsVal,Integer codeId, String setStandardDesc) {
		if (standardsPanel.getWidgetCount() <5) {
			if (setStandardsVal != null && !setStandardsVal.isEmpty()) {
				CodeDo codeObj=new CodeDo();
				codeObj.setCodeId(codeId);
				codeObj.setCode(setStandardsVal);
				standardsDo.add(codeObj);
				standardsPanel.add(createStandardLabel(setStandardsVal, Integer.toString(codeId), setStandardDesc));
			}
		} else {
			standardMaxShow();
			standardSgstBox.setText("");
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
		if(lblMediaPlaceHolder.getText().equalsIgnoreCase("Choose a Media Feature Option:") || lblMediaPlaceHolder.getText().equalsIgnoreCase("")){
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
