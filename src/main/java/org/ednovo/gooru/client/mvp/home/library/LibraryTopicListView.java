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
/**
 * 
 * @fileName : LibraryTopicListView.java
 *
 * @description : 
 *
 *
 * @version : 1.0
 *
 * @date: 04-Dec-2013
 *
 * @Author Gooru Team
 *
 * @Reviewer:
 */
package org.ednovo.gooru.client.mvp.home.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.home.library.assign.AssignPopupVc;
import org.ednovo.gooru.client.mvp.home.library.customize.RenameAndCustomizeLibraryPopUp;
import org.ednovo.gooru.client.mvp.home.library.events.OpenLessonConceptEvent;
import org.ednovo.gooru.client.mvp.home.library.events.OpenLessonConceptHandler;
import org.ednovo.gooru.client.mvp.home.library.events.SetConceptQuizDataEvent;
import org.ednovo.gooru.client.mvp.home.library.events.SetConceptQuizDataHandler;
import org.ednovo.gooru.client.mvp.home.library.events.SetLoadingIconEvent;
import org.ednovo.gooru.client.mvp.home.library.events.SetLoadingIconHandler;
import org.ednovo.gooru.client.mvp.home.library.events.StandardPreferenceSettingEvent;
import org.ednovo.gooru.client.mvp.home.library.events.StandardPreferenceSettingHandler;
import org.ednovo.gooru.client.mvp.rating.events.UpdateRatingsInRealTimeEvent;
import org.ednovo.gooru.client.mvp.rating.events.UpdateRatingsInRealTimeHandler;
import org.ednovo.gooru.client.mvp.search.IsSearchView;
import org.ednovo.gooru.client.uc.BrowserAgent;
import org.ednovo.gooru.client.uc.DownToolTipWidgetUc;
import org.ednovo.gooru.client.uc.StandardSgItemVc;
import org.ednovo.gooru.client.uc.UcCBundle;
import org.ednovo.gooru.client.uc.tooltip.GlobalToolTip;
import org.ednovo.gooru.client.uc.tooltip.LibraryTopicCollectionToolTip;
import org.ednovo.gooru.client.ui.HTMLEventPanel;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.client.util.PlayerDataLogEvents;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.content.StandardFo;
import org.ednovo.gooru.shared.model.library.ConceptDo;
import org.ednovo.gooru.shared.model.library.LessonDo;
import org.ednovo.gooru.shared.model.library.LibraryCollectionItemDo;
import org.ednovo.gooru.shared.model.library.LibraryResourceDo;
import org.ednovo.gooru.shared.model.library.PartnerConceptListDo;
import org.ednovo.gooru.shared.model.library.PartnerFolderDo;
import org.ednovo.gooru.shared.model.library.TopicDo;
import org.ednovo.gooru.shared.model.search.ResourceSearchResultDo;
import org.ednovo.gooru.shared.util.ResourceImageUtil;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

public class LibraryTopicListView extends Composite{

	@UiField ScrollPanel lessonScrollPanel;
	@UiField HTMLPanel topicBlock, conceptList,collectionInfo,resourcesInside,moreOnTopicText,standardsDescription;
	@UiField HTMLEventPanel searchLink;
	@UiField Label topicTitleLbl, noCollectionLbl;
	@UiField Image collectionImage;
	@UiField HTML collectionTitleLbl, collectionDescriptionLbl;
	@UiField Button assignCollectionBtn, customizeCollectionBtn;
	@UiField HTMLPanel loadingImage, collectionViewer;
	
	@UiField LibraryStyleBundle libraryStyle;
	
	@UiField Label collectionTitle, quizTitle;
	
	private HandlerRegistration imageHandler;

	private HandlerRegistration titleHandler;
	
	private String libraryGooruOid=null;
	
	@UiField FlowPanel standardsFloPanel;

	private Integer topicId;
	
	private boolean isScrollable = true;
	
	public static boolean isAssignPopup = false;
	
	public static boolean isCustomizePopup = false;
	
	private static boolean isVisible=true;
	
	private ConceptDo conceptDo;
	
	private ArrayList<ConceptDo> conceptDoList;
	
	private String searchTitle="";
	
	private static final String DEFULT_IMAGE_PREFIX = "images/default-";
	
	private static final String PNG = ".png";
	
	private static final String SMALL = "Small";
	
	private static final String DEFAULT_COLLECTION_IMAGE = "../images/default-collection-image-160x120.png";
	
	private static Integer LESSON_PAGE_INITIAL_LIMIT = 3;
	
	private static String PAGE = "page";
	
	private static final String COURSE_PAGE = "course-page";
	
	private static final String STANDARD_CODE = "code";

	private static final String STANDARD_DESCRIPTION = "description";
	
	private static final String PNG_CROP = "-80x60.";
	
	private static final String CUSTOMIZE = "customize";
	
	private static final String ASSIGN = "assign";
	
	private static final String COLLECTION_TITLE = "collectionTitle";
	
	private static LibraryTopicViewUiBinder uiBinder = GWT.create(LibraryTopicViewUiBinder.class);
	
	private PopupPanel toolTipPopupPanel = new PopupPanel();
	
	private PopupPanel toolTipPopupPanelNew = new PopupPanel();
	private PopupPanel toolTipPopupPanelCustomize = new PopupPanel();

	private String placeToken = null;
	
	private static final String SUBJECT_NAME = "subject";
	
	private static final String STANDARDS="standard";
	
	private static final String SHARING_TYPE = "public";
	
	private int pageNumber = 2;
	
	String lessonCode="";
	
	List<String> standPrefCode = new ArrayList<String>();

	private static final String STANDARD_ID = "standardId";
	
	private TopicDo topicDo = null;
	
	Map<String,String> topicDetails = new HashMap<String,String>();
	
	interface LibraryTopicViewUiBinder extends
			UiBinder<Widget, LibraryTopicListView> {
	}
	
	private MessageProperties i18n = GWT.create(MessageProperties.class);
	
	public LibraryTopicListView(TopicDo topicDo, Integer topicNumber, String placeToken) {
		initWidget(uiBinder.createAndBindUi(this));
		this.topicId = topicDo.getCodeId();
		setPlaceToken(placeToken);
		libraryGooruOid="";
		if(AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equals(PlaceTokens.COMMUNITY)){
			libraryGooruOid=AppClientFactory.getLoggedInUser().getSettings().getCommunityLibraryGooruOid();
		}
		moreOnTopicText.getElement().setInnerHTML(i18n.GL1169());
		moreOnTopicText.getElement().setAttribute("alt",i18n.GL1169());
		moreOnTopicText.getElement().setAttribute("title",i18n.GL1169());
		assignCollectionBtn.setText(i18n.GL0526());
		assignCollectionBtn.getElement().setAttribute("alt",i18n.GL0526());
		assignCollectionBtn.getElement().setAttribute("title",i18n.GL0526());
		customizeCollectionBtn.setText(i18n.GL2037());
		customizeCollectionBtn.getElement().setAttribute("alt",i18n.GL2037());
		customizeCollectionBtn.getElement().setAttribute("title",i18n.GL2037());
		noCollectionLbl.setText(i18n.GL1170());
		noCollectionLbl.getElement().setAttribute("alt",i18n.GL1170());
		noCollectionLbl.getElement().setAttribute("title",i18n.GL1170());
		topicTitleLbl.setText(i18n.GL1171()+" "+topicNumber+": "+topicDo.getLabel());
		topicTitleLbl.getElement().setAttribute("alt",i18n.GL1171()+" "+topicNumber+": "+topicDo.getLabel());
		topicTitleLbl.getElement().setAttribute("title",i18n.GL1171()+" "+topicNumber+": "+topicDo.getLabel());
		searchTitle=topicDo.getLabel();
		setIds();
		setAssets();
		
		toolTipPopupPanelCustomize.clear();
		toolTipPopupPanelNew.clear();
		toolTipPopupPanelCustomize.hide();
		toolTipPopupPanelNew.hide();
		if(topicDo.getLesson()!=null) {
			setLessonData(topicDo.getLesson(),libraryGooruOid);
		} else {
			setOnlyConceptData(topicDo.getCollection(), true, null, 0,libraryGooruOid);
		}
		try {
			this.topicDo = topicDo;
			if(topicDo.getLesson()!=null) {
				if(topicDo.getLesson().get(0).getConcept()!=null&&topicDo.getLesson().get(0).getConcept().size()>0) {
					setConceptDoList(topicDo.getLesson().get(0).getConcept().get(0).getCollection());
					setConceptData(conceptDoList.get(0),topicDo.getCodeId(), topicDo.getLesson().get(0).getCodeId()+"", topicDo.getLesson().get(0).getLabel(),topicDo.getLesson().get(0).getCode(),libraryGooruOid);
				} else {
					setConceptData(topicDo.getLesson().get(0).getCollection().get(0),topicDo.getCodeId(), topicDo.getLesson().get(0).getCodeId()+"", topicDo.getLesson().get(0).getLabel(),topicDo.getLesson().get(0).getCode(),libraryGooruOid);
				}
			} else if (topicDo.getCollection()!=null) {
				if(topicDo.getLesson().get(0).getConcept()!=null&&topicDo.getLesson().get(0).getConcept().size()>0) {
					setConceptDoList(topicDo.getLesson().get(0).getConcept().get(0).getCollection());
					setConceptData(conceptDoList.get(0),topicDo.getCodeId(),null, topicDo.getLesson().get(0).getLabel(),topicDo.getLesson().get(0).getCode(),libraryGooruOid);
				} else {
					setConceptData(topicDo.getCollection().get(0),topicDo.getCodeId(),null, topicDo.getLesson().get(0).getLabel(),topicDo.getLesson().get(0).getCode(),libraryGooruOid);
				}
			} else {
				setDefaultCollectionLbl();
			}
		} catch(Exception e) {
			setDefaultCollectionLbl();
		}
		addCollectionQuizTitleData("lesson");
		
		
		String subjectName = AppClientFactory.getPlaceManager().getRequestParameter(SUBJECT_NAME);
		if(subjectName!=null && subjectName.equalsIgnoreCase(STANDARDS)) {
			searchLink.getElement().getStyle().setDisplay(Display.NONE);
		}
		else
		{
			searchLink.getElement().getStyle().setDisplay(Display.BLOCK);
		}
		
		searchLink.addClickHandler(new OnSearchLinkClick());
		loadingImage.setVisible(false);
		
		assignCollectionBtn.addMouseOverHandler(new OnassignCollectionBtnMouseOver());
		assignCollectionBtn.addMouseOutHandler(new OnassignCollectionBtnMouseOut());
		customizeCollectionBtn.addMouseOverHandler(new OncustomizeCollectionBtnMouseOver());
		customizeCollectionBtn.addMouseOutHandler(new OncustomizeCollectionBtnMouseOut());
		assignCollectionBtn.addBlurHandler(new assignTooltipBlur());
		customizeCollectionBtn.addBlurHandler(new assignTooltipBlur());
		
		if(!AppClientFactory.isAnonymous()){
			try {
				getStandardPrefCode(AppClientFactory.getLoggedInUser().getMeta().getTaxonomyPreference().getCode());
			} catch (Exception e) {}
		}else{
			standardsFloPanel.setVisible(true);
		}
		AppClientFactory.getEventBus().addHandler(OpenLessonConceptEvent.TYPE, openLessonConceptHandler);
		AppClientFactory.getEventBus().addHandler(SetLoadingIconEvent.TYPE, setLoadingIconHandler);
		AppClientFactory.getEventBus().addHandler(StandardPreferenceSettingEvent.TYPE, standardPreferenceSettingHandler);
		AppClientFactory.getEventBus().addHandler(UpdateRatingsInRealTimeEvent.TYPE,setRatingWidgetMetaData);
		AppClientFactory.getEventBus().addHandler(SetConceptQuizDataEvent.TYPE,setConceptQuizDataHandler);
		Map<String, String> maps = StringUtil.splitQuery(Window.Location
				.getHref());
		if(maps.containsKey("emailId")){
			showPopupAfterGmailSignin();
		}
		
	}
	

	private boolean setQuizTabVisiblity(ArrayList<ConceptDo> conceptDoList) {
		boolean isCollectionTabVisible = false;
		if(conceptDoList!=null&&conceptDoList.size()>0){
			for(int i=0;i<conceptDoList.size();i++){
				if(conceptDoList.get(i).getCollectionType().equals("quiz")){
					isCollectionTabVisible = true;
					break;
				}
			}
		}else{
			return false;
		}
		return isCollectionTabVisible; 
	}


	StandardPreferenceSettingHandler standardPreferenceSettingHandler= new StandardPreferenceSettingHandler(){
		@Override
		public List<String> getCode(List<String> standPrefCode) {
			try {
				
				if(!AppClientFactory.isAnonymous()){
					AppClientFactory.getLoggedInUser().getMeta().getTaxonomyPreference().setCode(standPrefCode);
					getStandardPrefCode(standPrefCode); 
				}else{
					getStandardPrefCode(null); 
				}
				
			} catch (Exception e) {}
			return standPrefCode;
			
			}
	};
	
	protected void getStandardPrefCode(List<String> standPrefCode) {
		if(!AppClientFactory.isAnonymous()) {
			if(standPrefCode!=null){ 
				this.standPrefCode=standPrefCode;
				standardsFloPanel.setVisible(true);
				if(conceptDo!=null){
					setMetaDataInfo(conceptDo);
				}
				
			}else{
				standardsFloPanel.setVisible(false);
			}
		}else{
			standardsFloPanel.setVisible(true);
			if(conceptDo!=null){
				setMetaDataInfo(conceptDo);
			}
			
		}
	}
	public void setIds(){
		topicBlock.getElement().setId("pnlTopicBlock");
		topicTitleLbl.getElement().setId("lblTopicTitleLbl");
		searchLink.getElement().setId("epnlSearchLink");
		moreOnTopicText.getElement().setId("pnlMoreOnTopicText");
		lessonScrollPanel.getElement().setId("sbLessonScrollPanel");
		conceptList.getElement().setId("pnlConceptList");
		loadingImage.getElement().setId("pnlLoadingImage");
		collectionViewer.getElement().setId("pnlCollectionViewer");
		collectionInfo.getElement().setId("pnlCollectionInfo");
		standardsDescription.getElement().setId("pnlStandardsDescription");
		collectionImage.getElement().setId("imgCollectionImage");
		collectionTitleLbl.getElement().setId("htmlCollectionTitleLbl");
		collectionDescriptionLbl.getElement().setId("htmlCollectionDescriptionLbl");
		standardsFloPanel.getElement().setId("fpnlStandardsFloPanel");
		assignCollectionBtn.getElement().setId("btnAssignCollection");
		customizeCollectionBtn.getElement().setId("btnCustomizeCollection");
		resourcesInside.getElement().setId("pnlResourcesInside");
		noCollectionLbl.getElement().setId("lnlNoCollectionLbl");
		collectionTitle.getElement().setId("collectionTitle");
		quizTitle.getElement().setId("quizTitle");
	}
	
	private void setAssets() {
		collectionTitle.setText(i18n.GL0645());
		collectionTitle.getElement().setAttribute("alt",i18n.GL0645());
		collectionTitle.getElement().setAttribute("title",i18n.GL0645());
		quizTitle.setText(i18n.GL1670());
		quizTitle.getElement().setAttribute("alt",i18n.GL1670());
		quizTitle.getElement().setAttribute("title",i18n.GL1670());
	}
	
	public LibraryTopicListView(ConceptDo conceptDo, Integer conceptNumber, String placeToken) {
		initWidget(uiBinder.createAndBindUi(this));
		this.topicId = conceptNumber;
		setPlaceToken(placeToken);
		collectionImage.getElement().setAttribute("collid", conceptDo.getGooruOid());
		collectionTitleLbl.getElement().setAttribute("collid", conceptDo.getGooruOid());
		collectionTitleLbl.getElement().setAttribute(COLLECTION_TITLE,conceptDo.getTitle());
		assignCollectionBtn.setText(i18n.GL0526());
		assignCollectionBtn.getElement().setAttribute("alt",i18n.GL0526());
		assignCollectionBtn.getElement().setAttribute("title",i18n.GL0526());
		customizeCollectionBtn.setText(i18n.GL2037());
		customizeCollectionBtn.getElement().setAttribute("alt",i18n.GL2037());
		customizeCollectionBtn.getElement().setAttribute("title",i18n.GL2037());
		topicTitleLbl.setText(conceptDo.getTitle());
		topicTitleLbl.getElement().setAttribute("alt",conceptDo.getTitle());
		topicTitleLbl.getElement().setAttribute("title",conceptDo.getTitle());
		searchTitle=conceptDo.getTitle();
		moreOnTopicText.getElement().setInnerHTML(i18n.GL1169());
		moreOnTopicText.getElement().setAttribute("alt",i18n.GL1169());
		moreOnTopicText.getElement().setAttribute("title",i18n.GL1169());
		try {
			setConceptData(conceptDo,conceptNumber,null, null,null,null);
		} catch(Exception e) {
			collectionInfo.setVisible(false);
			resourcesInside.setVisible(false);
			noCollectionLbl.setVisible(true);
		}
		
		//searchLink.getElement().getStyle().setDisplay(Display.NONE);
		
		assignCollectionBtn.addMouseOverHandler(new OnassignCollectionBtnMouseOver());
		assignCollectionBtn.addMouseOutHandler(new OnassignCollectionBtnMouseOut());
		customizeCollectionBtn.addMouseOverHandler(new OncustomizeCollectionBtnMouseOver());
		customizeCollectionBtn.addMouseOutHandler(new OncustomizeCollectionBtnMouseOut());
		assignCollectionBtn.addBlurHandler(new assignTooltipBlur());
		customizeCollectionBtn.addBlurHandler(new assignTooltipBlur());
		setIds();
		setAssets();
		addCollectionQuizTitleData("concept");
		lessonScrollPanel.setVisible(false);
		collectionViewer.addStyleName(libraryStyle.collectionViewerSubStyle());
		collectionInfo.addStyleName(libraryStyle.collectionInfoSubStyle());
		resourcesInside.addStyleName(libraryStyle.resourcesInsideSubStyle());
		searchLink.addClickHandler(new OnSearchLinkClick());
		loadingImage.setVisible(false);
		if(!AppClientFactory.isAnonymous()){
			try {
				getStandardPrefCode(AppClientFactory.getLoggedInUser().getMeta().getTaxonomyPreference().getCode());
			} catch (Exception e) {}
		}else{
			standardsFloPanel.setVisible(true);
		}
		
		AppClientFactory.getEventBus().addHandler(StandardPreferenceSettingEvent.TYPE, standardPreferenceSettingHandler);
		AppClientFactory.getEventBus().addHandler(UpdateRatingsInRealTimeEvent.TYPE,setRatingWidgetMetaData);
		
		Map<String, String> maps = StringUtil.splitQuery(Window.Location
				.getHref());
		if(maps.containsKey("emailId")){
			showPopupAfterGmailSignin();
		}
	}

	public LibraryTopicListView(PartnerFolderDo partnerFolderDo, int topicNumber, String placeToken,String libraryGooruOid) {
		initWidget(uiBinder.createAndBindUi(this));
		this.topicId = topicNumber;
		this.libraryGooruOid=libraryGooruOid;
		setPlaceToken(placeToken);
		searchLink.getElement().getStyle().setDisplay(Display.NONE);
		moreOnTopicText.getElement().setInnerHTML(i18n.GL1169());
		moreOnTopicText.getElement().setAttribute("alt",i18n.GL1169());
		moreOnTopicText.getElement().setAttribute("title",i18n.GL1169());
		assignCollectionBtn.setText(i18n.GL0526());
		assignCollectionBtn.getElement().setAttribute("alt",i18n.GL0526());
		assignCollectionBtn.getElement().setAttribute("title",i18n.GL0526());
		customizeCollectionBtn.setText(i18n.GL2037());
		customizeCollectionBtn.getElement().setAttribute("alt",i18n.GL2037());
		customizeCollectionBtn.getElement().setAttribute("title",i18n.GL2037());
		noCollectionLbl.setText(i18n.GL1170());
		toolTipPopupPanelCustomize.clear();
		toolTipPopupPanelNew.clear();
		toolTipPopupPanelCustomize.hide();
		toolTipPopupPanelNew.hide();
		noCollectionLbl.getElement().setAttribute("alt",i18n.GL1170());
		noCollectionLbl.getElement().setAttribute("title",i18n.GL1170());
		topicTitleLbl.setText(partnerFolderDo.getTitle());
		topicTitleLbl.getElement().setAttribute("alt",partnerFolderDo.getTitle());
		topicTitleLbl.getElement().setAttribute("title",partnerFolderDo.getTitle());
		searchTitle=partnerFolderDo.getTitle();
		setIds();
		setAssets();
		addCollectionQuizTitleData("partner");
		if(partnerFolderDo.getCollections()!=null) {
			setOnlyConceptData(partnerFolderDo.getCollections(), false, partnerFolderDo.getGooruOid(), partnerFolderDo.getItemCount(),libraryGooruOid);
			try {
				setConceptData(partnerFolderDo.getCollections().get(0),topicId, null, null,null,libraryGooruOid);
			} catch(Exception e) {
				setDefaultCollectionLbl();
			}
		} else {
			setPartnerLibraryLessonData(partnerFolderDo.getFolderItems());
			try {
				setConceptData(partnerFolderDo.getFolderItems().get(0).getCollections().get(0),topicId, null, null,null,libraryGooruOid);
			} catch(Exception e) {
				setDefaultCollectionLbl();
			}
		}
		
		searchLink.addClickHandler(new OnSearchLinkClick());
		loadingImage.setVisible(false);
		
		assignCollectionBtn.addMouseOverHandler(new OnassignCollectionBtnMouseOver());
		assignCollectionBtn.addMouseOutHandler(new OnassignCollectionBtnMouseOut());
		customizeCollectionBtn.addMouseOverHandler(new OncustomizeCollectionBtnMouseOver());
		customizeCollectionBtn.addMouseOutHandler(new OncustomizeCollectionBtnMouseOut());
		assignCollectionBtn.addBlurHandler(new assignTooltipBlur());
		customizeCollectionBtn.addBlurHandler(new assignTooltipBlur());
		
		if(!AppClientFactory.isAnonymous()){
			try {
				getStandardPrefCode(AppClientFactory.getLoggedInUser().getMeta().getTaxonomyPreference().getCode());
			} catch (Exception e) {}
		}else{
			standardsFloPanel.setVisible(true);
		}
		AppClientFactory.getEventBus().addHandler(OpenLessonConceptEvent.TYPE, openLessonConceptHandler);
		AppClientFactory.getEventBus().addHandler(SetLoadingIconEvent.TYPE, setLoadingIconHandler);
		AppClientFactory.getEventBus().addHandler(StandardPreferenceSettingEvent.TYPE, standardPreferenceSettingHandler);
		AppClientFactory.getEventBus().addHandler(UpdateRatingsInRealTimeEvent.TYPE,setRatingWidgetMetaData);
		
		Map<String, String> maps = StringUtil.splitQuery(Window.Location
				.getHref());
		if(maps.containsKey("emailId")){
			showPopupAfterGmailSignin();
		}
	}

	private void setPartnerLibraryLessonData(final ArrayList<PartnerFolderDo> lessonDoList) {
		boolean isLessonHighlighted = true;
		if(lessonDoList.size()>=LESSON_PAGE_INITIAL_LIMIT) {
			for(int i=0;i<LESSON_PAGE_INITIAL_LIMIT;i++) {
				if(i==0) {
					isLessonHighlighted = true;
				} else {
					isLessonHighlighted = false;
				}
				conceptList.add(new LibraryLessonUc(lessonDoList.get(i),topicId,isLessonHighlighted, (i+1)));
			}
			final String subject = AppClientFactory.getPlaceManager().getRequestParameter("subject","featured");
			lessonScrollPanel.addScrollHandler(new ScrollHandler() {
				@Override
				public void onScroll(ScrollEvent event) {
					if(isScrollable) {
						isScrollable = false;
						/*AppClientFactory.getInjector().getLibraryService().getLessonsOnPagination(subject, ""+topicId, LESSON_PAGE_INITIAL_LIMIT, 5, getPlaceToken(), new SimpleAsyncCallback<ArrayList<LessonDo>>() {

							@Override
							public void onSuccess(ArrayList<LessonDo> result) {
								for(int i=0;i<result.size();i++) {
									conceptList.add(new LibraryLessonUc(result.get(i),topicId,false,((LESSON_PAGE_INITIAL_LIMIT+1)+i)));
								}
							}
						});*/
					}
				}
			});
		} else {
			for(int i=0;i<lessonDoList.size();i++) {
				if(i==0) {
					isLessonHighlighted = true;
				} else {
					isLessonHighlighted = false;
				}
				conceptList.add(new LibraryLessonUc(lessonDoList.get(i),topicId,isLessonHighlighted,(i+1)));
			}
		}
	}
	
	/** 
	 * This method is to set the conceptDo
	 */
	public void setConceptDo(ConceptDo conceptDo) {
		this.conceptDo = conceptDo;
	}
	
	private ConceptDo getConceptDo() {
		return conceptDo;
	}
	
	/**
	 * 
	 * @param list 
	 * @function setLessonData 
	 * 
	 * @created_date : 04-Dec-2013
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
	private void setLessonData(final List<LessonDo> lessonDoList,final String libraryGooruOid) {
		boolean isLessonHighlighted = true;
		String subjectName = AppClientFactory.getPlaceManager().getRequestParameter(SUBJECT_NAME);
		final String subject = AppClientFactory.getPlaceManager().getRequestParameter("subject","featured");
		int overallCount = 0;
		if(subjectName!=null && subjectName.equalsIgnoreCase(STANDARDS)) {
			LESSON_PAGE_INITIAL_LIMIT = 20;
		}
		if(lessonDoList.size()>=LESSON_PAGE_INITIAL_LIMIT) {
			for(int i=0;i<LESSON_PAGE_INITIAL_LIMIT;i++) {
				if(i==0) {
					isLessonHighlighted = true;
				} else {
					isLessonHighlighted = false;
				}
				if(lessonDoList.get(i).getConcept()!=null&&lessonDoList.get(i).getConcept().size()>0) {
					overallCount = ++overallCount + lessonDoList.get(i).getConcept().size();
				} else {
					overallCount = ++overallCount + lessonDoList.get(i).getCollection().size();
				}
				conceptList.add(new LibraryLessonUc(lessonDoList.get(i),topicId,isLessonHighlighted, (i+1),libraryGooruOid));
			}

			lessonScrollPanel.addScrollHandler(new ScrollHandler() {
				@Override
				public void onScroll(ScrollEvent event) {
					if(isScrollable) {
						isScrollable = false;
						AppClientFactory.getInjector().getLibraryService().getLessonsOnPagination(subject, ""+topicId, LESSON_PAGE_INITIAL_LIMIT, 20, getPlaceToken(), new SimpleAsyncCallback<ArrayList<LessonDo>>() {
							@Override
							public void onSuccess(ArrayList<LessonDo> result) {
								for(int i=0;i<result.size();i++) {
									conceptList.add(new LibraryLessonUc(result.get(i),topicId,false,((LESSON_PAGE_INITIAL_LIMIT+1)+i),libraryGooruOid));
								}
							}
						});
					}
				}
			});
		} else {
			for(int i=0;i<lessonDoList.size();i++) {
				if(i==0) {
					isLessonHighlighted = true;
				} else {
					isLessonHighlighted = false;
				}
				conceptList.add(new LibraryLessonUc(lessonDoList.get(i),topicId,isLessonHighlighted,(i+1),libraryGooruOid));
			}
		}
		if(overallCount<15) {
			isScrollable = false;
			AppClientFactory.getInjector().getLibraryService().getLessonsOnPagination(subject, ""+topicId, LESSON_PAGE_INITIAL_LIMIT, 20, getPlaceToken(), new SimpleAsyncCallback<ArrayList<LessonDo>>() {
				@Override
				public void onSuccess(ArrayList<LessonDo> result) {
					for(int i=0;i<result.size();i++) {
						conceptList.add(new LibraryLessonUc(result.get(i),topicId,false,((LESSON_PAGE_INITIAL_LIMIT+1)+i),libraryGooruOid));
					}
				}
			});
		}
	}
	
	
	/**
	 * @function setOnlyConceptData 
	 * 
	 * @created_date : 29-Dec-2013
	 * 
	 * @description
	 * 
	 * @parm(s) : @param conceptDoList
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	private void setOnlyConceptData(ArrayList<ConceptDo> conceptDoList, boolean isTopicCalled, final String parentId, final int partnerItemCount,final String libraryGooruOid) {
		boolean isLessonHighlighted = true;
		int pageCount = 0;
		String subjectName = AppClientFactory.getPlaceManager().getRequestParameter(SUBJECT_NAME);
		if(subjectName!=null && subjectName.equalsIgnoreCase(STANDARDS) || StringUtil.isPartnerUser(AppClientFactory.getCurrentPlaceToken())) {
			LESSON_PAGE_INITIAL_LIMIT = 20;
		}
		if(parentId!=null) {
			LESSON_PAGE_INITIAL_LIMIT = 20;
			pageCount = partnerItemCount;
		} else {
			pageCount = conceptDoList.size();
		}
		if(pageCount>=LESSON_PAGE_INITIAL_LIMIT) {
			conceptList.add(new LibraryLessonUc(conceptDoList,topicId,isLessonHighlighted, 0,libraryGooruOid));
			final String subject = AppClientFactory.getPlaceManager().getRequestParameter("subject","featured");
			lessonScrollPanel.addScrollHandler(new ScrollHandler() {
				@Override
				public void onScroll(ScrollEvent event) {
					if(isScrollable) {
						if(StringUtil.isPartnerUser(AppClientFactory.getCurrentPlaceToken())) {
							if(LESSON_PAGE_INITIAL_LIMIT<partnerItemCount) {
								isScrollable = false;
								String gooruUid = AppClientFactory.getPlaceManager().getRequestParameter("pid");
								AppClientFactory.getInjector().getLibraryService().getPartnerChildFolders(gooruUid, (pageNumber-1)*20, 20,parentId,SHARING_TYPE, null,new SimpleAsyncCallback<PartnerConceptListDo>() {
									@Override
									public void onSuccess(PartnerConceptListDo result) {
										LESSON_PAGE_INITIAL_LIMIT = LESSON_PAGE_INITIAL_LIMIT + result.getSearchResult().size();
										pageNumber = pageNumber + 1;
										if(LESSON_PAGE_INITIAL_LIMIT < partnerItemCount) {
											isScrollable = true;
										}
										conceptList.add(new LibraryLessonUc(result.getSearchResult(),topicId,false, 0,libraryGooruOid));
									}
								});
							}
						} else {
							AppClientFactory.getInjector().getLibraryService().getLessonsOnPagination(subject, ""+topicId, LESSON_PAGE_INITIAL_LIMIT, 20, getPlaceToken(), new SimpleAsyncCallback<ArrayList<LessonDo>>() {

								@Override
								public void onSuccess(ArrayList<LessonDo> lessonDoList) {
									for(int i=0;i<lessonDoList.size();i++) {
										isScrollable = false;
										conceptList.add(new LibraryLessonUc(lessonDoList.get(i).getCollection(),topicId,false, 0,libraryGooruOid));
									}
								}
							});
						}
					}
				}
			});
		} else {
			int conceptSize = 1;
			if(isTopicCalled) {
				conceptSize = conceptDoList.size();
			}
			for(int i=0;i<conceptSize;i++) {
				if(i==0) {
					isLessonHighlighted = true;
				} else {
					isLessonHighlighted = false;
				}
				conceptList.add(new LibraryLessonUc(conceptDoList,topicId,isLessonHighlighted,(i+1),libraryGooruOid));
			}
		}
	}
	
	/**
	 * 
	 * @fileName : LibraryTopicListView.java
	 *
	 * @description : 
	 *
	 *
	 * @version : 1.0
	 *
	 * @date: 04-Dec-2013
	 *
	 * @Reviewer:
	 */
	private class OnSearchLinkClick implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			String page = AppClientFactory.getPlaceManager().getRequestParameter(PAGE,"landing");
			if(page.equals(COURSE_PAGE)) {
				MixpanelUtil.mixpanelEvent("CoursePage_search_topic");
			} else {
				MixpanelUtil.mixpanelEvent("LandingPage_search_topic");
			}
			AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.RESOURCE_SEARCH, updateParams(searchTitle));
			

		}
	}
	
	/**
	 * 
	 * @function updateParams 
	 * 
	 * @created_date : 04-Dec-2013
	 * 
	 * @description
	 * 
	 * 
	 * @parm(s) : @param searchQuery
	 * @parm(s) : @return
	 *
	 * @return : Map<String,String>
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 *
	 *
	 *
	 */
	public Map<String, String> updateParams(String searchQuery) {
		Map<String, String> params = new HashMap<String,String>();
		params.put("category", "All");
		params.put("query", searchQuery);
		params.put("pageSize", "8");
		params.put("pageNum", "1");
		params.put(IsSearchView.RATINGS_FLT, "5,4,3,2,1,0");
		return params;
	}

	/**
	 * 
	 * @function setConceptData 
	 * 
	 * @created_date : 04-Dec-2013
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
	 */
	public void setConceptData(final ConceptDo conceptDo, Integer topicId, final String lessonId, String lessonLabel,String lessonCode,final String libraryGooruOid) {
		setConceptDo(conceptDo);
		this.lessonCode=lessonCode;
		String subjectName = AppClientFactory.getPlaceManager().getRequestParameter(SUBJECT_NAME);
		if(this.topicId==topicId) {
		if(subjectName!=null && subjectName.equalsIgnoreCase(STANDARDS)) {
			standardsDescription.clear();
			InlineLabel headerLbl = new InlineLabel(i18n.GL1363()+i18n.GL_SPL_SEMICOLON()+" ");
			headerLbl.getElement().getStyle().setFontWeight(FontWeight.BOLD);
			InlineLabel textLbl = new InlineLabel("");
			if(lessonLabel != null)
			{
			if(lessonLabel.length() > 400)
			{
			lessonLabel = lessonLabel.substring(0, 400) + "...";
			}
			textLbl = new InlineLabel(lessonLabel);
			}
			else
			{				
			textLbl = new InlineLabel(conceptDo.getLabel());
			}
			
			standardsDescription.add(headerLbl);
			standardsDescription.add(textLbl);
			standardsDescription.setVisible(true);
		} else {
			standardsDescription.setVisible(false);
		}
		
	
			String id = null;
			if(conceptDo.getId()!=null)	{
				id=conceptDo.getId();
			} 
			else if(conceptDo.getGooruOid()!=null){
				id=conceptDo.getGooruOid();
			}
			if(id!=null) {
				collectionInfo.setVisible(true);
				resourcesInside.setVisible(true);
				noCollectionLbl.setVisible(false);
				
				try {
					collectionImage.setUrl(StringUtil.formThumbnailName(conceptDo.getThumbnails().getUrl(),"-160x120."));
					collectionImage.addErrorHandler(new ErrorHandler() {
						@Override
						public void onError(ErrorEvent event) {
							collectionImage.setUrl(DEFAULT_COLLECTION_IMAGE);
						}
					});
					if(imageHandler!=null) {
						imageHandler.removeHandler();
					}
					if(titleHandler!=null) {
						titleHandler.removeHandler();
					}
					imageHandler=collectionImage.addClickHandler(new CollectionOpenClickHandler(lessonId,libraryGooruOid));
					titleHandler=collectionTitleLbl.addClickHandler(new CollectionOpenClickHandler(lessonId,libraryGooruOid));
				} catch (Exception e) {
					collectionImage.setUrl(DEFAULT_COLLECTION_IMAGE);
				}
				
				try
				{
				collectionTitleLbl.setHTML(conceptDo.getTitle());
				collectionTitleLbl.getElement().setAttribute("alt",conceptDo.getTitle());
				collectionTitleLbl.getElement().setAttribute("title",conceptDo.getTitle());
				String description = conceptDo.getGoals();
				if(description!=null&&description.length()>=97) {
					String browesr = BrowserAgent.getWebBrowserClient();
					if(browesr.contains("chrome")||browesr.contains("safari")) {
						description = description.substring(0,97)+"..."; 
					} else {
						description = description.substring(0,85)+"...";
					}
				}
				collectionDescriptionLbl.setHTML(description);
				collectionDescriptionLbl.getElement().setAttribute("alt",description);
				collectionDescriptionLbl.getElement().setAttribute("title",description);
				}
				catch(Exception ex)
				{
					
				}
				setMetaDataInfo(conceptDo); 
				resourcesInside.clear();
				ArrayList<LibraryCollectionItemDo> libraryResources =  conceptDo.getCollectionItems();
				int resourceCount = 0;
				//System.out.println("AppClientFactory.getCurrentPlaceToken()87878::"+AppClientFactory.getCurrentPlaceToken());
				if(libraryResources!=null) {
					if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.COMMUNITY)){
						resourceCount = libraryResources.size();
					}else{
						if(conceptDo.getItemCount()!=null){
							resourceCount = conceptDo.getItemCount();
						}
						else
						{
							resourceCount = libraryResources.size();
						}
						
					}
					int resources=resourceCount<=4?resourceCount:4;
					final Label resourceCountLbl = new Label(resources+" "+i18n.GL_GRR_OF()+" "+i18n.GL_GRR_THE()+" "+resourceCount+" "+i18n.GL1094().toLowerCase());
					resourcesInside.add(resourceCountLbl);
					for(int i=0;i<resources;i++) {
						try {
							final LibraryCollectionItemDo libraryItem = libraryResources.get(i);
							final LibraryResourceDo libraryResourceDo = libraryItem.getResource();
							
							String categoryString = "";
							if(libraryResourceDo.getCategory()!=null) {
								categoryString = libraryResourceDo.getCategory();
							} else if(libraryResourceDo.getResourceFormat()!=null){
								categoryString = libraryResourceDo.getResourceFormat().getDisplayName();
							}
							
							final String category = categoryString;
							final HTMLEventPanel resourcePanel = new HTMLEventPanel("");
							resourcePanel.setStyleName(libraryStyle.resourceImage());
							
							final Image resourceImage = new Image();
							resourceImage.setWidth("80px");
							resourceImage.setHeight("60px");
							try
							{
								String resourceTitle = libraryResourceDo.getTitle().replaceAll("\\<[^>]*>","");
								libraryResourceDo.setTitle(resourceTitle);
							} catch (Exception e){
							}
							resourceImage.setAltText(libraryResourceDo.getTitle());
							resourceImage.setTitle(libraryResourceDo.getTitle());
							
							final String categoryImage=categoryString;
							
							final List<String> sourceAttribution = libraryResourceDo.getPublisher();
//							if(libraryResourceDo.getResourceSource()!=null&&libraryResourceDo.getResourceSource().getAttribution()!=null) {
//								sourceAttribution = libraryResourceDo.getResourceSource().getAttribution();
//							}
//							final String attribution = sourceAttribution;
							
							String domainName = "";
							if(libraryResourceDo.getResourceSource()!=null&&libraryResourceDo.getResourceSource().getDomainName()!=null) {
								domainName = libraryResourceDo.getResourceSource().getDomainName();
							}
							final String domain = domainName;
							final HTMLEventPanel resourceCategoryIcon = new HTMLEventPanel("");
							resourceCategoryIcon.addMouseOverHandler(new MouseOverHandler() {
							   	
								@Override
								public void onMouseOver(MouseOverEvent event) {
									try
									{
										toolTipPopupPanel.clear();
										toolTipPopupPanel.setWidget(new LibraryTopicCollectionToolTip(libraryResourceDo.getTitle(),categoryImage,sourceAttribution,libraryResourceDo.getRatings().getCount(),libraryResourceDo.getRatings().getAverage(),domain));
										toolTipPopupPanel.setStyleName("");
										toolTipPopupPanel.setPopupPosition(event.getRelativeElement().getAbsoluteLeft() - 2, event.getRelativeElement().getAbsoluteTop() + 55);
										toolTipPopupPanel.show();
									}
									catch(Exception ex)
									{
										
									}
								}
							});
							resourceCategoryIcon.addMouseOutHandler(new MouseOutHandler() {
								
								@Override
								public void onMouseOut(MouseOutEvent event) {
									toolTipPopupPanel.hide();
								}
							});
							
							resourceCategoryIcon.addClickHandler(new ClickHandler() {
								@Override
								public void onClick(ClickEvent event) {
									String page = AppClientFactory.getPlaceManager().getRequestParameter(PAGE,"landing");
									if(page.equals(COURSE_PAGE)) {
										MixpanelUtil.mixpanelEvent("CoursePage_Plays_Resource");
									} else {
										MixpanelUtil.mixpanelEvent("LandingPage_Plays_Resource");
									}
									Map<String, String> params = new HashMap<String, String>();
									params.put("id", conceptDo.getGooruOid());
									
									String resourceId = libraryItem.getCollectionItemId();
									if(resourceId==null) {
										resourceId = libraryResourceDo.getCollectionItemId();
									}
									params.put("rid", resourceId);
									params.put("subject", AppClientFactory.getPlaceManager().getRequestParameter("subject","featured"));
									params.put("lessonId", lessonId);
									if(libraryGooruOid!=null){
										params.put("lid", libraryGooruOid);
									}
									String libraryEventId=AppClientFactory.getPlaceManager().getLibaryEventId();
									if(libraryEventId!=null){
										params.put("eventid", libraryEventId);
									}
									if(getPlaceToken().equals(PlaceTokens.RUSD_LIBRARY) || getPlaceToken().equals(PlaceTokens.SAUSD_LIBRARY)) {
										params.put("library", getPlaceToken());
									}
									String standardId = AppClientFactory.getPlaceManager().getRequestParameter(STANDARD_ID);
									if(standardId!=null){
										params.put("rootNodeId", standardId);
									}
									
									PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(PlaceTokens.COLLECTION_PLAY, params);
									AppClientFactory.getPlaceManager().revealPlace(false,placeRequest,true);
								}
							});
							
							resourceImage.addMouseOverHandler(new MouseOverHandler() {
							   	
								@Override
								public void onMouseOver(MouseOverEvent event) {
									try
									{
										toolTipPopupPanel.clear();
										toolTipPopupPanel.setWidget(new LibraryTopicCollectionToolTip(libraryResourceDo.getTitle(),categoryImage,sourceAttribution,libraryResourceDo.getRatings().getCount(),libraryResourceDo.getRatings().getAverage(),domain));
										toolTipPopupPanel.setStyleName("");
										toolTipPopupPanel.setPopupPosition(event.getRelativeElement().getAbsoluteLeft() - 2, event.getRelativeElement().getAbsoluteTop() + 55);
										toolTipPopupPanel.show();
									}
									catch(Exception ex)
									{
										
									}
								}
							});
							
							resourceImage.addMouseOutHandler(new MouseOutHandler() {
								
								@Override
								public void onMouseOut(MouseOutEvent event) {
									toolTipPopupPanel.hide();
								}
							});
							try {
								if(libraryResourceDo.getResourceType()!=null&&libraryResourceDo.getResourceType().getName().equalsIgnoreCase("video/youtube")) {
									String youTubeIbStr = ResourceImageUtil.getYoutubeVideoId(libraryResourceDo.getUrl());
									String thumbnailUrl = ResourceImageUtil.youtubeImageLink(youTubeIbStr,Window.Location.getProtocol());
									resourceImage.setUrl(thumbnailUrl);
								} else {
									if(libraryResourceDo.getThumbnails()!=null&&libraryResourceDo.getThumbnails().getUrl()!=null&&libraryResourceDo.getThumbnails().getUrl().isEmpty()) {
										resourceImage.setUrl(DEFULT_IMAGE_PREFIX +getDetaultResourceImage(category.toLowerCase()) + PNG);
									} else {
										resourceImage.setUrl(libraryResourceDo.getThumbnails().getUrl());
									}
								}
								resourceImage.addErrorHandler(new ErrorHandler() {
									@Override
									public void onError(ErrorEvent event) {
										resourceImage.setUrl(DEFULT_IMAGE_PREFIX +getDetaultResourceImage(category.toLowerCase()) + PNG);
									}
								});
							} catch (Exception e){
								resourceImage.setUrl(DEFULT_IMAGE_PREFIX + getDetaultResourceImage(category.toLowerCase()) + PNG);
								resourceImage.setAltText(libraryResourceDo.getTitle());
								resourceImage.setTitle(libraryResourceDo.getTitle());
							}
							
							resourcePanel.addClickHandler(new ClickHandler() {
								@Override
								public void onClick(ClickEvent event) {
									String page = AppClientFactory.getPlaceManager().getRequestParameter(PAGE,"landing");
									if(page.equals(COURSE_PAGE)) {
										MixpanelUtil.mixpanelEvent("CoursePage_Plays_Resource");
									} else {
										MixpanelUtil.mixpanelEvent("LandingPage_Plays_Resource");
									}
									Map<String, String> params = new HashMap<String, String>();
									params.put("id", conceptDo.getGooruOid());
									
									String resourceId = libraryItem.getCollectionItemId();
									if(resourceId==null) {
										resourceId = libraryResourceDo.getCollectionItemId();
									}
									params.put("rid", resourceId);
									params.put("subject", AppClientFactory.getPlaceManager().getRequestParameter("subject","featured"));
									params.put("lessonId", lessonId);
									if(libraryGooruOid!=null){
										params.put("lid", libraryGooruOid);
									}
									String libraryEventId=AppClientFactory.getPlaceManager().getLibaryEventId();
									if(libraryEventId!=null){
										params.put("eventid", libraryEventId);
									}
									if(getPlaceToken().equals(PlaceTokens.RUSD_LIBRARY) || getPlaceToken().equals(PlaceTokens.SAUSD_LIBRARY)) {
										params.put("library", getPlaceToken());
									}
									String standardId = AppClientFactory.getPlaceManager().getRequestParameter(STANDARD_ID);
									if(standardId!=null){
										params.put("rootNodeId", standardId);
									}
									
									PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(PlaceTokens.COLLECTION_PLAY, params);
									AppClientFactory.getPlaceManager().revealPlace(false,placeRequest,true);
								}
							});
							
							
							resourceCategoryIcon.addStyleName(UcCBundle.INSTANCE.css().resourceName());
							resourceCategoryIcon.addStyleName(getDetaultResourceImage(category.toLowerCase()) + SMALL);
							resourcePanel.add(resourceImage);
							resourcePanel.add(resourceCategoryIcon);
							resourcesInside.add(resourcePanel);
						} catch (Exception e){
							e.printStackTrace();
						}
					}
				}
			} else {
				collectionInfo.setVisible(false);
				resourcesInside.setVisible(false);
				noCollectionLbl.setVisible(true);
			}
		}
		loadingImage.setVisible(false);
		collectionViewer.setVisible(true);
	}
	
	private void setMetaDataInfo(ConceptDo conceptDo) {
		collectionImage.getElement().setAttribute("collid", conceptDo.getGooruOid());
		collectionTitleLbl.getElement().setAttribute("collid", conceptDo.getGooruOid());
		collectionTitleLbl.getElement().setAttribute(COLLECTION_TITLE,conceptDo.getTitle());
		if(AppClientFactory.isAnonymous()){
			standardsFloPanel.clear();
			standardsFloPanel.setVisible(true);
		}
		if(conceptDo.getMetaInfo()!=null){
			if(conceptDo.getMetaInfo().getStandards()!=null) {
				standardsFloPanel.clear();
				List<StandardFo> standardFoList = conceptDo.getMetaInfo().getStandards();
				List<Map<String, String>> standardMap = new ArrayList<Map<String, String>>();
				List<Map<String, String>> tempStandardMap = new ArrayList<Map<String, String>>();
				
				ResourceSearchResultDo searchResultDo = new ResourceSearchResultDo();
				for(int i=0;i<standardFoList.size();i++) {
					if(isUserStandards(standardFoList.get(i).getCode())) {
						if(AppClientFactory.getPlaceManager().getRequestParameter("libtype")!=null&&standardFoList.get(i).getCode().contains("CCSS")){
						}else{
							Map<String, String> standards = new HashMap<String, String>();
							standards.put(STANDARD_CODE, standardFoList.get(i).getCode());
							standards.put(STANDARD_DESCRIPTION, standardFoList.get(i).getDescription());
							standardMap.add(standards);
							tempStandardMap.add(standards);
							
							if(standardFoList.get(i).getCode() == lessonCode)
							{
								standardMap.clear();
								tempStandardMap.remove(standards);
								standardMap.add(standards);
								standardMap.addAll(tempStandardMap);
							}
						}
					}
				}
				if(standardMap.size()<=0) {
//					standardsFloPanel.setVisible(false);
				} else {
					standardsFloPanel.setVisible(true);
					searchResultDo.setStandards(standardMap);
					renderStandards(standardsFloPanel, searchResultDo);
				}
			}
			}
			else 
			{
				if(conceptDo.getStandards() != null){
					standardsFloPanel.clear();
					List<Map<String, String>> standardMap = conceptDo.getStandards();
					Iterator<Map<String, String>> iterator2 = standardMap.iterator();
					int removeCount = 0;
					while (iterator2.hasNext()) {
						Map<String, String> standard = iterator2.next();
						if(!isUserStandards(standard.get(STANDARD_CODE))) {
							if(AppClientFactory.getPlaceManager().getRequestParameter("libtype")!=null&&standard.get(STANDARD_CODE).contains("CCSS")){
							}else{
								standardMap.remove(removeCount);
							}
							
						}
						removeCount++;
					}
					
					ResourceSearchResultDo searchResultDo = new ResourceSearchResultDo();
					if(standardMap.size()<=0) {
						standardsFloPanel.setVisible(false);
					} else {
						standardsFloPanel.setVisible(true);
						searchResultDo.setStandards(standardMap);
						renderStandards(standardsFloPanel, searchResultDo);
					}
				}
			}
	}

	private boolean isUserStandards(String code) {
		boolean isUserStandards = false;
		if(!AppClientFactory.isAnonymous()) {
			outerloop:
			for(int i=0;i<standPrefCode.size();i++){
				if(code.contains(standPrefCode.get(i))){
					isUserStandards=true;
					break outerloop;
				}
			}
		} else {
//			if(AppClientFactory.getPlaceManager().getRequestParameter("subject")!=null) {
				if(AppClientFactory.getPlaceManager().getRequestParameter("libtype")!=null&&code.contains("CCSS")){
				} 
				else if(AppClientFactory.getPlaceManager().getRequestParameter("libtype")==null&&code.contains("TEKS")){
				} 
				else {
					isUserStandards=true;
				}
//			}
		}
		return isUserStandards;
	}
	
	public class CollectionOpenClickHandler implements ClickHandler {
			private String lessonId;
			private String libraryId;
			public CollectionOpenClickHandler(String lessonId,String libraryId) {
				this.lessonId = lessonId;
				this.libraryId=libraryId;
			}
			@Override
			public void onClick(ClickEvent event) {

				String collectionIdVal = "";
				try{
					collectionIdVal = ((Image)event.getSource()).getElement().getAttribute("collid");
				}
				catch(Exception ex){
					collectionIdVal = ((HTML)event.getSource()).getElement().getAttribute("collid");
				}

				String page = AppClientFactory.getPlaceManager().getRequestParameter(PAGE,"landing");
				if(AppClientFactory.getPlaceManager().getRequestParameter(STANDARD_ID)!=null){
					MixpanelUtil.mixpanelEvent("standardlibrary_play_collection");	
				}
				if(page.equals(COURSE_PAGE)) {
					MixpanelUtil.mixpanelEvent("CoursePage_Plays_Collection");
				} else {
					MixpanelUtil.mixpanelEvent("LandingPage_Plays_Collection");
				}
				Map<String, String> params = new HashMap<String, String>();
				params.put("id", collectionIdVal);
				params.put("subject", AppClientFactory.getPlaceManager().getRequestParameter("subject","featured"));
				params.put("lessonId", lessonId);
				if(libraryId!=null){
					params.put("lid", libraryId);
				}
				String libraryEventId=AppClientFactory.getPlaceManager().getLibaryEventId();
				if(libraryEventId!=null){
					params.put("eventid", libraryEventId);
				}
				if(getPlaceToken().equals(PlaceTokens.RUSD_LIBRARY) || getPlaceToken().equals(PlaceTokens.SAUSD_LIBRARY)) {
					params.put("library", getPlaceToken());
				}
				String standardId = AppClientFactory.getPlaceManager().getRequestParameter(STANDARD_ID);
				if(standardId!=null){
					params.put("rootNodeId", standardId);
				}
				AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.COLLECTION_PLAY, params);
			}
	}
	
	OpenLessonConceptHandler openLessonConceptHandler = new OpenLessonConceptHandler() {
		@Override
		public void openLessonConcept(ConceptDo conceptDo, Integer topicId, String lessonId, String lessonLabel, String lessonCode,String libraryGooruOid) {
			setConceptData(conceptDo, topicId, lessonId, lessonLabel,lessonCode,libraryGooruOid);
			setConceptDoList(null);
			addCollectionQuizTitleData("lesson");
		}
	};
	
	SetLoadingIconHandler setLoadingIconHandler = new SetLoadingIconHandler() {
		@Override
		public void setLoadingIcon(boolean isVisible, Integer topicIdCollection) {
			if(topicId==topicIdCollection) {
				collectionViewer.setVisible(!isVisible);
				loadingImage.setVisible(isVisible);
			}
		}
	};
	
	/**
	 * 
	 * @function onassignCollectionBtnClicked 
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
	 * 
	 */
	@UiHandler("assignCollectionBtn")
	public void onassignCollectionBtnClicked(ClickEvent clickEvent) {
		String collectionId = collectionTitleLbl.getElement().getAttribute("collid");
		PlayerDataLogEvents.triggerLibraryShareDataEvent(collectionId, libraryGooruOid);
		toolTipPopupPanelNew.clear();
		toolTipPopupPanelNew.hide();
		final Map<String, String> params = StringUtil.splitQuery(Window.Location
				.getHref());
		if(params.containsKey(CUSTOMIZE)){
			params.remove(CUSTOMIZE);
		}
		if(AppClientFactory.getPlaceManager().getRequestParameter(STANDARD_ID)!=null){
			MixpanelUtil.mixpanelEvent("standardlibrary_assign_collection");	
		}
		MixpanelUtil.mixpanelEvent("LandingPage_Assign_Collection");
		
				if(!isAssignPopup){
					isAssignPopup=true;
				//	Window.enableScrolling(false);
				//final Map<String,String> params = new HashMap<String,String>();
				AssignPopupVc successPopupVc = new AssignPopupVc(collectionId, getConceptDo().getTitle(), getConceptDo().getGoals()) {
					
					@Override
					public void closePoup() {
						Window.enableScrolling(true);
				        this.hide();
				    	isAssignPopup=false;
/*				    	Map<String, String> paramsAssignPopup = StringUtil.splitQuery(Window.Location.getHref());
				    	paramsAssignPopup.remove(ASSIGN);
				    	PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(AppClientFactory.getCurrentPlaceToken(), paramsAssignPopup);
						AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, false);*/
					}
				};
				Window.scrollTo(0, 0);
				successPopupVc.setWidth("500px");
				successPopupVc.setHeight("658px");
				if(!successPopupVc.isVisible()){
					successPopupVc.show();
					successPopupVc.center();
				}
				Window.enableScrolling(false);
				   
				if (AppClientFactory.isAnonymous()){
				successPopupVc.setPopupPosition(successPopupVc.getAbsoluteLeft(), 10);
				}
				else
				{
				successPopupVc.setPopupPosition(successPopupVc.getAbsoluteLeft(), 10);
				}
				
				params.put(ASSIGN, "yes");
				params.put("collectionId", collectionId);
				PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(AppClientFactory.getCurrentPlaceToken(), params);
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
	@UiHandler("customizeCollectionBtn")
	public void oncustomizeCollectionBtnClicked(ClickEvent clickEvent) {
		toolTipPopupPanelCustomize.clear();
		toolTipPopupPanelCustomize.hide();
		if(AppClientFactory.getPlaceManager().getRequestParameter(STANDARD_ID)!=null){
			MixpanelUtil.mixpanelEvent("standardlibrary_customize_collection");	
		}
		final Map<String, String> params = StringUtil.splitQuery(Window.Location
				.getHref());
		if(params.containsKey(ASSIGN)){
			params.remove(ASSIGN);
		}
		String collectionId = collectionTitleLbl.getElement().getAttribute("collid");
		String collectionTitle = collectionTitleLbl.getElement().getAttribute(COLLECTION_TITLE);
		MixpanelUtil.mixpanelEvent("LandingPage_customize_collection");
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
		//	final Map<String,String> params = new HashMap<String,String>();
			RenameAndCustomizeLibraryPopUp successPopupVc = new RenameAndCustomizeLibraryPopUp(collectionId, loginFlag, collectionTitle) {

				@Override
				public void closePoup() {
					Window.enableScrolling(true);
					this.hide();	
					isCustomizePopup = false;
/*			    	Map<String, String> paramsAssignPopup = StringUtil.splitQuery(Window.Location.getHref());
			    	paramsAssignPopup.remove(CUSTOMIZE);
			    	PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(AppClientFactory.getCurrentPlaceToken(), paramsAssignPopup);
					AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, false);*/
				}
			};
			Window.scrollTo(0, 0);
			successPopupVc.setWidth("500px");
			successPopupVc.setHeight("471px");
			successPopupVc.show();
			successPopupVc.center();
			
			
			params.put(CUSTOMIZE, "yes");
			params.put("collectionId", collectionId);
			PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(AppClientFactory.getCurrentPlaceToken(), params);
			AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, true);
			
		}

	}	
	/**
	 * 
	 * Showing Customize or Assign popup after login with gmail account.
	 * 
	 */
	
	private void showPopupAfterGmailSignin() {
		// TODO Auto-generated method stub

		
		String collectionId = getConceptDo().getGooruOid()!=null ? getConceptDo().getGooruOid():null;
		
		String colleId = AppClientFactory.getPlaceManager().getRequestParameter("collectionId")!=null ? AppClientFactory.getPlaceManager().getRequestParameter("collectionId") : null;
		String customize = AppClientFactory.getPlaceManager().getRequestParameter(CUSTOMIZE)!=null ? AppClientFactory.getPlaceManager().getRequestParameter(CUSTOMIZE) : null;
		String assign = AppClientFactory.getPlaceManager().getRequestParameter(ASSIGN)!=null ? AppClientFactory.getPlaceManager().getRequestParameter(ASSIGN) : null;
		String emailId = AppClientFactory.getPlaceManager().getRequestParameter("emailId")!=null ? AppClientFactory.getPlaceManager().getRequestParameter("emailId") : null;
		
		if(customize!=null && customize.equals("yes") && emailId!=null){
			if(colleId.equals(collectionId) && isVisible){
				isVisible=false;
				Boolean loginFlag = false;
				if (AppClientFactory.isAnonymous()){
					loginFlag = true;
				}
				else
				{
					loginFlag = false;
				}
				final Map<String, String> params = StringUtil.splitQuery(Window.Location
						.getHref());
				//final Map<String,String> params = new HashMap<String,String>();
				RenameAndCustomizeLibraryPopUp successPopupVc = new RenameAndCustomizeLibraryPopUp(collectionId, loginFlag, getConceptDo().getTitle()) {

					@Override
					public void closePoup() {
						Window.enableScrolling(true);
						this.hide();	
						isCustomizePopup = false;
					}
				};
				Window.scrollTo(0, 0);
				successPopupVc.setWidth("500px");
				successPopupVc.setHeight("471px");
				successPopupVc.show();
				successPopupVc.center();
			}
			
		}
		if(assign!=null && assign.equals("yes") && emailId!=null){
		//	final Map<String,String> params = new HashMap<String,String>();
			final Map<String, String> params = StringUtil.splitQuery(Window.Location
					.getHref());
			if(colleId.equals(collectionId) && isVisible ){
				isVisible=false;
				AssignPopupVc successPopupVc = new AssignPopupVc(collectionId, getConceptDo().getTitle(), getConceptDo().getGoals()) {

					@Override
					public void closePoup() {
						Window.enableScrolling(true);
						this.hide();
						isAssignPopup=false;
					}
				};
				Window.scrollTo(0, 0);
				successPopupVc.setWidth("500px");
				successPopupVc.setHeight("658px");
				if(!successPopupVc.isVisible()){
					successPopupVc.show();
					successPopupVc.center();
				}
				if (AppClientFactory.isAnonymous()){
					successPopupVc.setPopupPosition(successPopupVc.getAbsoluteLeft(), 10);
				}
				else
				{
					successPopupVc.setPopupPosition(successPopupVc.getAbsoluteLeft(), 10);
				}
			}
			
		}


	}

	public void renderStandards(FlowPanel standardsContainer, ResourceSearchResultDo searchResultDo) {
		if (searchResultDo.getStandards() != null) {
			List<Map<String, String>> standards = searchResultDo.getStandards();
			Iterator<Map<String, String>> iterator = standards.iterator();
			int count = 0;
			FlowPanel toolTipwidgets = new FlowPanel();
			while (iterator.hasNext()) {
				Map<String, String> standard = iterator.next();
				String stdCode = standard.get(STANDARD_CODE);
				String stdDec = standard.get(STANDARD_DESCRIPTION);
				if (count > 0) {
					if (count < 18){
						StandardSgItemVc standardItem = new StandardSgItemVc(stdCode, stdDec);
						toolTipwidgets.add(standardItem);
					}
				} else {
					DownToolTipWidgetUc toolTipUc = new DownToolTipWidgetUc(new Label(stdCode), new Label(stdDec), standards);
					toolTipUc.setStyleName(UcCBundle.INSTANCE.css().searchStandard());
					standardsContainer.add(toolTipUc);
				}
				count++;
			}
			if (standards.size()>18){
				final Label left = new Label("+"+(standards.size() - 18));
				toolTipwidgets.add(left);
			}
			if (searchResultDo.getStandards().size() > 1) {
				Integer moreStandardsCount = searchResultDo.getStandards().size() - 1;
				DownToolTipWidgetUc toolTipUc = new DownToolTipWidgetUc(new Label("+" + moreStandardsCount), toolTipwidgets, standards);
				toolTipUc.setStyleName(libraryStyle.blueLink());
				standardsContainer.add(toolTipUc);
			}
		}
	}

	public String getPlaceToken() {
		return placeToken;
	}

	private void setPlaceToken(String placeToken) {
		this.placeToken = placeToken;
	}
	
	private void setDefaultCollectionLbl() {
		collectionInfo.setVisible(false);
		resourcesInside.setVisible(false);
		noCollectionLbl.setVisible(true);
	}
	

	public class OnassignCollectionBtnMouseOver implements MouseOverHandler{

		@Override
		public void onMouseOver(MouseOverEvent event) {
			toolTipPopupPanelNew.clear();
			toolTipPopupPanelNew.setWidget(new GlobalToolTip(i18n.GL0676()));
			toolTipPopupPanelNew.setStyleName("");
			toolTipPopupPanelNew.setPopupPosition(assignCollectionBtn.getElement().getAbsoluteLeft()+8, assignCollectionBtn.getElement().getAbsoluteTop()+10);
			toolTipPopupPanelNew.getElement().getStyle().setZIndex(999999);
			toolTipPopupPanelNew.show();
		}
		
	}
	
	public class OnassignCollectionBtnMouseOut implements MouseOutHandler{

		@Override
		public void onMouseOut(MouseOutEvent event) {
			toolTipPopupPanelNew.hide();
		}

		
		
	}
	
	public class OncustomizeCollectionBtnMouseOver implements MouseOverHandler{

		@Override
		public void onMouseOver(MouseOverEvent event) {
			toolTipPopupPanelCustomize.clear();
			toolTipPopupPanelCustomize.setWidget(new GlobalToolTip(i18n.GL0677()));
			toolTipPopupPanelCustomize.setStyleName("");
			toolTipPopupPanelCustomize.setPopupPosition(customizeCollectionBtn.getElement().getAbsoluteLeft()+18, customizeCollectionBtn.getElement().getAbsoluteTop()+10);
			toolTipPopupPanelCustomize.getElement().getStyle().setZIndex(999999);
			toolTipPopupPanelCustomize.show();
		}
		
	}
	
	public class OncustomizeCollectionBtnMouseOut implements MouseOutHandler{

		@Override
		public void onMouseOut(MouseOutEvent event) {
			toolTipPopupPanelCustomize.hide();
		}
	}
	public String getDetaultResourceImage(String category){
		String categoryIcon=category;
		if(category!=null){
			if(category.equalsIgnoreCase("Lesson")||category.equalsIgnoreCase("Textbook")||category.equalsIgnoreCase("Handout"))
			{
				categoryIcon=category.replaceAll("Lesson", "Text").replaceAll("Textbook", "Text").replaceAll("Handout", "Text").replaceAll("lesson", "Text").replaceAll("textbook", "Text").replaceAll("handout", "Text");
			}
			if(category.equalsIgnoreCase("Slide"))
			{
				categoryIcon=category.replaceAll("Slide","Image").replaceAll("slide","Image");
			}
			if(category.equalsIgnoreCase("Exam") || category.equalsIgnoreCase("Challenge")||categoryIcon.equalsIgnoreCase("Website"))
			{
				categoryIcon=category.replaceAll("Exam","Webpage").replaceAll("Challenge", "Webpage").replaceAll("exam","Webpage").replaceAll("challenge", "Webpage");
			}
		}
		return categoryIcon.toLowerCase();
	}
	
	UpdateRatingsInRealTimeHandler setRatingWidgetMetaData = new UpdateRatingsInRealTimeHandler() {	

		@Override
		public void updateRatingInRealTime(String gooruOid, double average,Integer count) {
			
		}
	};
	
	SetConceptQuizDataHandler setConceptQuizDataHandler = new SetConceptQuizDataHandler() {
		@Override
		public void setConceptQuizDataHandler(ArrayList<ConceptDo> conceptDoList, Integer topicId,
				String lessonId, String lessonLabel, String lessonCode, String conceptId,String libraryGooruOid) {
			setConceptDoList(conceptDoList);
			for(int i = 0; i<conceptDoList.size();i++) {
				if(conceptDoList.get(i).getGooruOid().equalsIgnoreCase(conceptId)) {
					setConceptData(conceptDoList.get(i),topicId,lessonId,lessonLabel,lessonCode,libraryGooruOid);
				}
			}
			addCollectionQuizTitleData("lesson");
		}
	};
	
	private void addCollectionQuizTitleData(String pageType) {
		if(pageType.equals("lesson")&&conceptDoList!=null&&conceptDoList.size()>0&&AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.DISCOVER)&&AppClientFactory.getPlaceManager().getRequestParameter("standardId")==null&&setQuizTabVisiblity(conceptDoList)) {
			setCollectionQuizVisibility(true);
			collectionTitle.addStyleName(libraryStyle.collectionQuizTabActive());
			quizTitle.removeStyleName(libraryStyle.collectionQuizTabActive());
		} else {
			setCollectionQuizVisibility(false);
		}
	}
	
	private void setCollectionQuizVisibility(boolean isVisible) {
		collectionTitle.setVisible(isVisible);
		quizTitle.setVisible(isVisible);
	}
	
	@UiHandler("collectionTitle")
	public void clickCollectionTitle(ClickEvent event) {
		collectionTitle.addStyleName(libraryStyle.collectionQuizTabActive());
		quizTitle.removeStyleName(libraryStyle.collectionQuizTabActive());
		setConceptDoData("collection");
	}
	
	@UiHandler("quizTitle")
	public void clickQuizTitle(ClickEvent event) {
		collectionTitle.removeStyleName(libraryStyle.collectionQuizTabActive());
		quizTitle.addStyleName(libraryStyle.collectionQuizTabActive());
		setConceptDoData("quiz");
	}
	
	private void setConceptDoData(String collectionType) {
		for(int i = 0; i<conceptDoList.size();i++) {
			if(conceptDoList.get(i).getCollectionType().equalsIgnoreCase(collectionType)) {
				setConceptData(conceptDoList.get(i),topicDo.getCodeId(),null, topicDo.getLesson().get(0).getLabel(),topicDo.getLesson().get(0).getCode(),null);
			}
		}
	}
	public ArrayList<ConceptDo> getConceptDoList() {
		return conceptDoList;
	}
	public void setConceptDoList(ArrayList<ConceptDo> conceptDoList) {
		this.conceptDoList = conceptDoList;
	}
	public class assignTooltipBlur implements BlurHandler{

		@Override
		public void onBlur(BlurEvent event) {
			toolTipPopupPanelCustomize.clear();
			toolTipPopupPanelNew.clear();
			toolTipPopupPanelCustomize.hide();
			toolTipPopupPanelNew.hide();
			
		}
		
	}
}