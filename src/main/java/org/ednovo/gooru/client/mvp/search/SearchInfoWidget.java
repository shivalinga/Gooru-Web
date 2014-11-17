/**
 * 
 */
package org.ednovo.gooru.client.mvp.search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.event.InvokeLoginEvent;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.addTagesPopup.AddTagesPopupView;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.collaborators.vc.SuccessPopupViewVc;
import org.ednovo.gooru.client.uc.DownToolTipWidgetUc;
import org.ednovo.gooru.client.uc.LiecenceTooltip;
import org.ednovo.gooru.client.uc.PlayerBundle;
import org.ednovo.gooru.client.uc.StandardSgItemVc;
import org.ednovo.gooru.client.uc.ToolTipPopUp;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.code.CodeDo;
import org.ednovo.gooru.shared.model.content.CollectionItemDo;
import org.ednovo.gooru.shared.model.content.LicenseDo;
import org.ednovo.gooru.shared.model.content.ResourceDo;
import org.ednovo.gooru.shared.model.content.customFieldValuesDO;
import org.ednovo.gooru.shared.model.search.ResourceSearchResultDo;
import org.ednovo.gooru.shared.util.InfoUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author mitraJ
 *
 */
public class SearchInfoWidget extends Composite {

	private static SearchInfoWidgetUiBinder uiBinder = GWT
			.create(SearchInfoWidgetUiBinder.class);
	
	private MessageProperties i18n = GWT.create(MessageProperties.class);

	interface SearchInfoWidgetUiBinder extends
	UiBinder<Widget, SearchInfoWidget> {
	}

	@UiField Label publisherText,courseText,legalText,
	standardsText,gradeTitle,originalUrlTitle,adsTitle,mbFriendlyLbl,
	mbFriendlyText,dataTypeLbl,dataTypeFormat,interactiveLbl,interactiveType,eduAllignLbl,eduAllignType,eduUseLbl,
	eduRoleLbl,eduRoleType,ageRangeLbl,ageRangeType,dKnowledgeLbl,readingLevelLbl,
	hasAdaptationType,hasAdaptationLbl,languageLbl,languageType,countryCodeLbl,countryCodeType,isAdaptationLbl,isAdaptationType,
	copyRightType,copyRightLbl,hostType,hostLbl,accessibilityAPILbl,controlType,controlLbl,
	acessHazardlLbl,acessHazardType,mediaFeatureLbl,accessModelLbl,accesibilityLbl,generalLbl,
	thumbnailText,licenceCodeLbl,licenceCodeType,educationallLbl,resourceInfoLbl,
	authorLbl,authorName,adsName,schLevelLbl,schLevelInfo,keywordsTitle,
	momentsoflearningLbl,resourceTypeImage,lblAggregation;

	@UiField HTMLPanel rightsLogoContainer,courseInfo,originalUrlText,publisherPanel,publisherType,coursePanel,gradesPanel,
	adsPanel,mobileFriendlyPanel,dataTypePanel,interactivityTypePanel,eduAllignPanel,eduUsePanel,eduRolePanel,ageRangePanel,dKnowledgePanel,
	readingLevelPanel,hasAdaptationPanel,languagePanel,countryCodePanel,isAdaptationPanel,copyRightPanel,hostPanel,
	accessibilityAPIPanel,accessibilityPanel,controlPanel,accessHazardPanel,mediaFeaturePanel,accessModePanel,thumbnailPanel,licenceCodePanel,
	authorPanel,schLevelPanel,eduUseType,keyWordsPanel,keywordsInfo,readingLevelType,accessModeType,mediaFeatureType,accessibilityAPIType,dKnowledgeType,
	momentsoflearningPanel,momentsoflearningType,thumbnailurlValue,generalPanel,gradesText,totalContainer;
	
	@UiField
	HTMLPanel aggregationPanel,aggregationType;

	@UiField FlowPanel licenceContainer,standardsInfoConatiner;

	@UiField HTMLPanel standardsContentContainer,loadingImagePanel;
	@UiField Label standaInfo,noInfoAvailable;

	@UiField Button addTagsBtn;

	AddTagesPopupView popup;
	
	List<String> coursesList;

	ToolTipPopUp toolTip ; 

	public static final String STANDARD_CODE = "code";
	public static final String STANDARD_DESCRIPTION = "description";
	public static final String IPAD_FRIENDLY="iPad_friendly";

	private ResourceSearchResultDo searchResultDo;
	private CollectionItemDo collectionItemDo;

	@UiField HTML lblcollectionName;

	boolean isGeneralInfo=false;
	boolean isEducationalInfo=false;
	boolean isResourceInfo=false;
	boolean isAccessibilityInfo=false;
	boolean isGrades =false;
	LiecenceTooltip liecenceTooltip;
	/**
	 * Because this class has a default constructor, it can
	 * be used as a binder template. In other words, it can be used in other
	 * *.ui.xml files as follows:
	 * <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 *   xmlns:g="urn:import:**user's package**">
	 *  <g:**UserClassName**>Hello!</g:**UserClassName>
	 * </ui:UiBinder>
	 * Note that depending on the widget that is used, it may be necessary to
	 * implement HasHTML instead of HasText.
	 */
	public SearchInfoWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		PlayerBundle.INSTANCE.getPlayerStyle().ensureInjected();
		isGeneralInfo=false;
		isEducationalInfo=false;
		isResourceInfo=false;
		isAccessibilityInfo=false;
		isGrades =false;
		totalContainer.getElement().getStyle().setPadding(15, Unit.PX);
		setResourceInfoData();
	}

	private void setResourceInfoData() {
		publisherText.setText(i18n.GL0566()+" ");
		setIdForLabel(publisherText,"PublisherText",i18n.GL0566()+" ");
		
		lblAggregation.setText(i18n.GL1628().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		setIdForLabel(lblAggregation,"LblAggregation",i18n.GL1628().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		
		courseText.setText(i18n.GL0616()+" ");
		setIdForLabel(courseText,"CourseText",i18n.GL0616()+" ");
		
		legalText.setText(i18n.GL0730().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		setIdForLabel(legalText,"LegalText",i18n.GL0730().trim()+i18n.GL_SPL_SEMICOLON());
		
		standardsText.setText(i18n.GL0619()+" ");
		setIdForLabel(standardsText,"standardsText",i18n.GL0619()+" ");
		
		//		resourceInfoText.setText(i18n.GL0621);
		gradeTitle.setText(i18n.GL0325().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		setIdForLabel(gradeTitle,"GradeTitle",i18n.GL0325().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		
		originalUrlTitle.setText(i18n.GL0976().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		setIdForLabel(originalUrlTitle,"OriginalUrlTitle",i18n.GL0976().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		
		generalLbl.setText(i18n.GL1708());
		setIdForLabel(generalLbl,"GeneralLbl",i18n.GL1708());
		
		hostLbl.setText(i18n.GL1700().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		setIdForLabel(hostLbl,"HostLbl",i18n.GL1700().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		
		//Educational static data
		educationallLbl.setText(i18n.GL1720());
		setIdForLabel(educationallLbl,"EducationallLbl",i18n.GL1720());
		
		eduAllignLbl.setText(i18n.GL1690().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		setIdForLabel(eduAllignLbl,"EduAllignLbl",i18n.GL1690().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		
		eduUseLbl.setText(i18n.GL1664().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		setIdForLabel(eduUseLbl,"EduUseLbl",i18n.GL1664().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		
		eduRoleLbl.setText(i18n.GL1867()+" ");
		setIdForLabel(eduRoleLbl,"EduRoleLbl",i18n.GL1867()+" ");
		
		interactiveLbl.setText(i18n.GL1689().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		setIdForLabel(interactiveLbl,"InteractiveLbl",i18n.GL1689().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		
		ageRangeLbl.setText(i18n.GL1692().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		setIdForLabel(ageRangeLbl,"AgeRangeLbl",i18n.GL1692().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		
		dKnowledgeLbl.setText(i18n.GL1693().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		setIdForLabel(dKnowledgeLbl,"DKnowledgeLbl",i18n.GL1693().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		
		momentsoflearningLbl.setText(i18n.GL1678().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		setIdForLabel(momentsoflearningLbl,"MomentsoflearningLbl",i18n.GL1678().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		
		readingLevelLbl.setText(i18n.GL1694().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		setIdForLabel(readingLevelLbl,"ReadingLevelLbl",i18n.GL1694().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		
		schLevelLbl.setText(i18n.GL1868().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		setIdForLabel(schLevelLbl,"SchLevelLbl",i18n.GL1868().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		
		//Resource Info Static data
		resourceInfoLbl.setText(i18n.GL1716());
		setIdForLabel(resourceInfoLbl,"ResourceInfoLbl",i18n.GL1716());
		
		
//		dateCreatedLbl.setText(i18n.GL1717+i18n.GL_SPL_SEMICOLON);
		countryCodeLbl.setText(i18n.GL1697().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		setIdForLabel(countryCodeLbl,"CountryCodeLbl",i18n.GL1697().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		
		languageLbl.setText(i18n.GL1696().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		setIdForLabel(languageLbl,"LanguageLbl",i18n.GL1696().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		
		dataTypeLbl.setText(i18n.GL1688().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		setIdForLabel(dataTypeLbl,"DataTypeLbl",i18n.GL1688().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		
		authorLbl.setText(i18n.GL0573().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		setIdForLabel(authorLbl,"AuthorLbl",i18n.GL0573()+" "+i18n.GL_SPL_SEMICOLON()+" ");
		
		copyRightLbl.setText(i18n.GL1699().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		setIdForLabel(copyRightLbl,"CopyRightLbl",i18n.GL1699()+" "+i18n.GL_SPL_SEMICOLON()+" ");
		
		keywordsTitle.setText(i18n.GL1876().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		setIdForLabel(keywordsTitle,"KeywordsTitle",i18n.GL1876()+" "+i18n.GL_SPL_SEMICOLON()+" ");
		
		adsTitle.setText(i18n.GL1800().trim()+i18n.GL_SPL_SEMICOLON());
		setIdForLabel(adsTitle,"AdsTitle",i18n.GL1800()+" "+i18n.GL_SPL_SEMICOLON());
		
		//Accessibility Static data
		accesibilityLbl.setText(i18n.GL1703());
		setIdForLabel(accesibilityLbl,"AccesibilityLbl",i18n.GL1703());
		
		mbFriendlyLbl.setText(i18n.GL1687().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		setIdForLabel(mbFriendlyLbl,"MbFriendlyLbl",i18n.GL1687()+" "+i18n.GL_SPL_SEMICOLON()+" ");
		
		accessModelLbl.setText(i18n.GL1707().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		setIdForLabel(accessModelLbl,"AccessModelLbl",i18n.GL1707()+" "+i18n.GL_SPL_SEMICOLON()+" ");
		
		mediaFeatureLbl.setText(i18n.GL1706().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		setIdForLabel(mediaFeatureLbl,"MediaFeatureLbl",i18n.GL1706()+" "+i18n.GL_SPL_SEMICOLON()+" ");
		
		controlLbl.setText(i18n.GL1704().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		setIdForLabel(controlLbl,"ControlLbl",i18n.GL1704()+" "+i18n.GL_SPL_SEMICOLON()+" ");

		acessHazardlLbl.setText(i18n.GL1705().trim()+i18n.GL_SPL_SEMICOLON()+" ");
		setIdForLabel(acessHazardlLbl,"AcessHazardlLbl",i18n.GL1705()+" "+i18n.GL_SPL_SEMICOLON()+" ");
	}

	/**
	 * Assign {@link ResourceSearchResultDo} instance
	 * 
	 * @param searchResultDo
	 *            instance of {@link ResourceSearchResultDo}
	 *//*
	public void setData(ResourceSearchResultDo searchResultDo) {
		this.searchResultDo = searchResultDo;
	}*/

	public void setData(ResourceSearchResultDo searchResultsDo) {
		this.searchResultDo=searchResultsDo;
		if(searchResultsDo.getResourceFormat()!=null){
			setResourceTypeImage(searchResultsDo.getResourceFormat().getValue());
		}
		if(searchResultsDo.getResourceTitle()!=null){
			lblcollectionName.setHTML(removeHtmlTags(searchResultsDo.getResourceTitle()));
			

		}
		/**
		 * Commented this, as it will set very early as it is, grades should display after formating.
		 */
		/*if(searchResultsDo.getGrade()!=null)
		{
			gradesText.setText(searchResultsDo.getGrade());
		}*/
	}

	public void setResourceTypeImage(String resourceType){
		if(resourceType!=null){
			resourceType=resourceType.toLowerCase();
			if(resourceType.equalsIgnoreCase("lesson")||resourceType.equalsIgnoreCase("textbook")||resourceType.equalsIgnoreCase("handout"))
			{
				resourceType=resourceType.replaceAll("lesson", "Text").replaceAll("textbook", "Text").replaceAll("handout", "Text");
			}
			if(resourceType.equalsIgnoreCase("slide"))
			{
				resourceType=resourceType.replaceAll("slide","Image");
			}
			if(resourceType.equalsIgnoreCase("exam")||resourceType.equalsIgnoreCase("challenge")||resourceType.equalsIgnoreCase("website"))
			{
				resourceType=resourceType.replaceAll("exam","Webpage").replaceAll("challenge", "Webpage").replaceAll("website", "Webpage");
			}
			//lblresourceType.setText(resourceType);
			resourceTypeImage.setStyleName(getResourceTypeImage(resourceType));
		}
	}

	private String removeHtmlTags(String html){
		html = html.replaceAll("</p>", " ").replaceAll("<p>", "").replaceAll("<br data-mce-bogus=\"1\">", "").replaceAll("<br>", "").replaceAll("</br>", "");
		return html;
	}

	public String getResourceTypeImage(String resourceType){
		if(resourceType.equalsIgnoreCase("Video")||resourceType.equalsIgnoreCase("Videos")){
			return PlayerBundle.INSTANCE.getPlayerStyle().videoResourceTypeInfo();
		}else if(resourceType.equalsIgnoreCase("Interactive")){
			return PlayerBundle.INSTANCE.getPlayerStyle().interactiveResourceTypeInfo();
		}
		else if(resourceType.equalsIgnoreCase("Website")||resourceType.equalsIgnoreCase("Webpage")){
			return PlayerBundle.INSTANCE.getPlayerStyle().websiteResourceTypeInfo();		
		}
		else if(resourceType.equalsIgnoreCase("Slide")){
			return PlayerBundle.INSTANCE.getPlayerStyle().imageResourceTypeInfo();
		}
		else if(resourceType.equalsIgnoreCase("Textbook")){
			return PlayerBundle.INSTANCE.getPlayerStyle().textResourceTypeInfo();
		}
		else if(resourceType.equalsIgnoreCase("Question")){
			return PlayerBundle.INSTANCE.getPlayerStyle().questionResourceTypeInfo();
		}
		else if(resourceType.equalsIgnoreCase("lesson")){
			return PlayerBundle.INSTANCE.getPlayerStyle().lessonResourceTypeInfo();

		}else if(resourceType.equalsIgnoreCase("Handout")){
			return PlayerBundle.INSTANCE.getPlayerStyle().textResourceTypeInfo();
		} else if(resourceType.equalsIgnoreCase("text")){
			return PlayerBundle.INSTANCE.getPlayerStyle().textResourceTypeInfo();
		}
		else if(resourceType.equalsIgnoreCase("image")){
			return PlayerBundle.INSTANCE.getPlayerStyle().imageResourceTypeInfo();
		}
		else if(resourceType.equalsIgnoreCase("audio")){
			return PlayerBundle.INSTANCE.getPlayerStyle().audioResourceTypeInfo();
		}else if(resourceType.equalsIgnoreCase("exam")){
			return PlayerBundle.INSTANCE.getPlayerStyle().websiteResourceTypeInfo();
		}
		else {
			return PlayerBundle.INSTANCE.getPlayerStyle().otherResourceTypeInfo();
		}
	}

	/**
	 * @param collectionItemDo
	 *            instance of {@link collectionItemDo}
	 * 
	 */

	public void setData(CollectionItemDo collectionItemDo) {
		this.collectionItemDo =collectionItemDo;
	}

	public void setData(){
		AppClientFactory.getInjector().getPlayerAppService().getResourceCollectionItem(null, searchResultDo.getGooruOid(), null, new SimpleAsyncCallback<CollectionItemDo>() {

			@Override
			public void onSuccess(CollectionItemDo collectionItemDo) {
				
				getLoadingImagePanel().setVisible(false);
//				setPublisher(collectionItemDo.getResource().getResourceSource()!=null?collectionItemDo.getResource().getResourceSource().getAttribution():"",collectionItemDo.getResource().getUrl());
				/*if(collectionItemDo.getResource().getThumbnails()!=null){
					setThumbnailUrl(collectionItemDo.getResource().getThumbnails().getUrl());
				}*/
				/*if(collectionItemDo.getResource().getCreatedOn()!=null){
					setCreatedDate(collectionItemDo.getResource().getCreatedOn());
				}*/
                setGeneralResourceInfo(collectionItemDo);
				setEducationInfoDetails(collectionItemDo);
				setResourceInfoDetails(collectionItemDo);
				setAccessibilityDetails(collectionItemDo);
				if(isAccessibilityInfo==false  
						&& isGeneralInfo==false
						&& isEducationalInfo==false
						&& isResourceInfo==false){
							noInfoAvailable.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().noresourcesAvailable());
							noInfoAvailable.setText("No information available for this resource");
							noInfoAvailable.setVisible(true);
						}else{
							noInfoAvailable.setVisible(false);
						}
			}
		});
		
		
		
	}


	protected void setGeneralResourceInfo(CollectionItemDo CollectiongenealInfo) {
		if(CollectiongenealInfo.getResource()!=null){
			 ResourceDo resourceDo= CollectiongenealInfo.getResource();
			String grade = resourceDo.getGrade()!=null ? resourceDo.getGrade() : null;
			String url = resourceDo.getUrl()!=null ? resourceDo.getUrl() : null;
			String host = (resourceDo.getCustomFieldValues()!=null && resourceDo.getCustomFieldValues().getCfHost()!=null) ?
					       resourceDo.getCustomFieldValues().getCfHost() : null;
	        String licence= (resourceDo.getLicense()!=null && resourceDo.getLicense().getIcon()!=null) ? resourceDo.getLicense().getIcon() :null;		       
			
			/*if(grade!=null && !grade.equals("")){
				gradesPanel.setVisible(true);
				gradeTitle.setVisible(true);
				gradesText.setText(InfoUtil.getGrades(grade));
				isGeneralInfo=true;
			}else{
				gradesPanel.setVisible(false);
				gradeTitle.setVisible(false);
			}*/
	        
	        if(grade!=null && !grade.equals("")){
				List<String> gradesdetails = new ArrayList<String>();
				List<Integer> gradeListInt = new  ArrayList<Integer>();
				String[] gradeslist=resourceDo.getGrade().split(",");
				for (String eachGrade1 : gradeslist) {
					if (!eachGrade1.trim().equalsIgnoreCase("Kindergarten")
							&& !eachGrade1.trim().equalsIgnoreCase("Higher Education")) {
						eachGrade1 = eachGrade1.replaceAll("th", "")
								.replaceAll("TH", "").replaceAll("st", "")
								.replaceAll("ST", "").replaceAll("nd", "")
								.replaceAll("ND", "").replaceAll("rd", "")
								.replaceAll("RD", "");
						eachGrade1 = eachGrade1.replaceAll("Grade", "").replaceAll("grade", "");
						eachGrade1 = eachGrade1.replaceAll("K-", "")
								.replaceAll("k-", "");
				/*		eachGrade1 = eachGrade1.toLowerCase().replaceAll("K", "")
								.replaceAll("k", "");*/
						
						gradesdetails.add(eachGrade1);
						
/*						try {
							String grad[] = generateGradeIfHypen(eachGrade1).trim().split(",");
							for (int i = 0; i < grad.length; i++) {
								try
								{
								gradeListInt.add(Integer.parseInt(grad[i]));
								}
								catch(Exception ex)
								{
									gradeListInt.add(1000);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}*/
					}else{
						gradesdetails.add(eachGrade1);
					}
				}
			//	gradeListInt = sortList(gradeListInt);
/*				for(int glevel=0;glevel<gradeListInt.size();glevel++){
					System.out.println("gradeListInt::"+gradeListInt.get(glevel));

					gradesdetails.add(Integer.toString(gradeListInt.get(glevel)));
					
					
				}*/
				//List<String> stringList = new ArrayList<String>(Arrays.asList(gradeslist));
				setGrades(gradesdetails);
				}
	        
	        
			setResourceAttribution(CollectiongenealInfo.getResource().getResourceSource()!=null?CollectiongenealInfo.getResource().getResourceSource().getAttribution():
				null,CollectiongenealInfo.getResource().getTaxonomySet());
			
			renderStandards(standardsInfoConatiner,CollectiongenealInfo.getStandards());
			
			setOriginalUrl(CollectiongenealInfo.getResource().getAssetURI(),CollectiongenealInfo.getResource().getFolder(),
					CollectiongenealInfo.getResource().getUrl(),CollectiongenealInfo.getResource().getResourceType().getName());

			if(resourceDo.getPublisher()!=null || CollectiongenealInfo.getResource().getResourceFormat()!=null){

			if(resourceDo.getPublisher()!=null){
				if(resourceDo.getPublisher().size()>0){
					InfoUtil.setDepthofknowledgeDetails(CollectiongenealInfo.getResource().getPublisher(), publisherType, publisherText, publisherPanel);
				isGeneralInfo=true;
				}
				else{
					publisherPanel.setVisible(false);
				}
			}
			if(CollectiongenealInfo.getResource().getResourceFormat()!=null){
				if(CollectiongenealInfo.getResource().getResourceFormat()!=null && CollectiongenealInfo.getResource().getResourceFormat().getValue().equalsIgnoreCase("question")){
					List<String> publisherQuestionUserName = new ArrayList<String>();
					publisherQuestionUserName.add(CollectiongenealInfo.getResource().getCreator().getUsername());
					InfoUtil.setDepthofknowledgeDetails(publisherQuestionUserName, publisherType, publisherText, publisherPanel);
				}
			}
		}
			
			if(resourceDo.getAggregator()!=null){
//				setAggregatorvalues(collectionItemDo.getResource().getAggregator());
				if(resourceDo.getAggregator().size()>0){
				InfoUtil.setDepthofknowledgeDetails(CollectiongenealInfo.getResource().getAggregator(), aggregationType, lblAggregation, aggregationPanel);
				isGeneralInfo=true;
				}
				else{
					aggregationPanel.setVisible(false);
				}
			}else{
				aggregationPanel.setVisible(false);
			}
			
			if(host!=null && !host.equals("")){
				hostPanel.setVisible(true);
				hostType.setText(" "+CollectiongenealInfo.getResource().getCustomFieldValues().getCfHost());
				hostType.getElement().setAttribute("alt"," "+CollectiongenealInfo.getResource().getCustomFieldValues().getCfHost());
				hostType.getElement().setAttribute("title"," "+CollectiongenealInfo.getResource().getCustomFieldValues().getCfHost());
				isGeneralInfo=true;
			}else{
				hostPanel.setVisible(false);
			}
			
			setResourceLicenceLogo(CollectiongenealInfo.getResource().getAssetURI(), CollectiongenealInfo.getResource().getLicense());
			
			if(grade==null && coursesList.isEmpty() && CollectiongenealInfo.getStandards().isEmpty() && url==null &&
					resourceDo.getPublisher().isEmpty() && resourceDo.getAggregator().isEmpty() && host==null && licence==null ){
				generalLbl.setVisible(false);
				generalPanel.setVisible(false);
			}
		}else{
			generalLbl.setVisible(false);
			gradesPanel.setVisible(false);
			generalPanel.setVisible(false);
			hostPanel.setVisible(false);
			aggregationPanel.setVisible(false);
			originalUrlTitle.setVisible(false);
			originalUrlText.setVisible(false);
			publisherPanel.setVisible(false);
			coursePanel.setVisible(false);
			standardsContentContainer.setVisible(false);
			licenceContainer.setVisible(false);
		}
		if(isGeneralInfo){
			generalLbl.setVisible(true);
		}else{
			generalLbl.setVisible(false);
		}
	}

	/**
	 * To set Resource License
	 * @param assetUrl 
	 * @param licenseDo instance of {@link LicenseDo}
	 */
	public void setResourceLicenceLogo(String assetUrl,LicenseDo licenseDo){
		if(licenseDo!=null){
			if(licenseDo.getIcon()!=null&&!licenseDo.getIcon().trim().equals("")){
				Image image=new Image();
				image.setUrl(assetUrl+licenseDo.getIcon());
				image.addMouseOverHandler(new MouseOverShowStandardToolTip(licenseDo.getCode(),licenseDo.getName()));
				image.addMouseOutHandler(new MouseOutHideToolTipOER());
				licenceContainer.setVisible(true);
				rightsLogoContainer.clear();
				rightsLogoContainer.add(image);
				isGeneralInfo=true;
			}
			else{
				licenceContainer.setVisible(false);
				rightsLogoContainer.clear();
			}
		}else{
			licenceContainer.setVisible(false);
			rightsLogoContainer.clear();
		}		
	}	


	public class MouseOverShowStandardToolTip implements MouseOverHandler
	{
		String desc=null;
		String code=null;

		public MouseOverShowStandardToolTip(String code,String description) {
			this.code = code;
			this.desc = description;
		}

		@Override
		public void onMouseOver(MouseOverEvent event) {
			
			liecenceTooltip = new LiecenceTooltip(code,desc, (event.getRelativeElement().getAbsoluteLeft()-109),(event.getRelativeElement().getAbsoluteTop()+22));
			liecenceTooltip.setStyleName("");
			liecenceTooltip.show();
			liecenceTooltip.getElement().getStyle().setZIndex(99999);
		}
	}
	public class MouseOutHideToolTipOER implements MouseOutHandler
	{
		@Override
		public void onMouseOut(MouseOutEvent event) {
			liecenceTooltip.hide();
		}

	}
	public class MouseOutHideToolTip implements MouseOutHandler
	{
		@Override
		public void onMouseOut(MouseOutEvent event) {
			toolTip.hide();
		}

	}
		
	
	/**
	 * To show Standards Information
	 * @param standardsContainer
	 * @param standardsList
	 */

	public void renderStandards(FlowPanel standardsContainer, List<Map<String,String>> standardsList) {
		standardsContainer.clear();

		if (standardsList != null) {
			standaInfo.setVisible(false);
			standardsContentContainer.setVisible(true);
			Iterator<Map<String, String>> iterator = standardsList.iterator();
			int count = 0;
			FlowPanel toolTipwidgets = new FlowPanel();
			while (iterator.hasNext()) {
				Map<String, String> standard = iterator.next();
				String stdCode = standard.get(STANDARD_CODE);
				String stdDec = standard.get(STANDARD_DESCRIPTION);
				if (count > 2) {
					isGeneralInfo=true;
					if (count < 18){
						StandardSgItemVc standardItem = new StandardSgItemVc(stdCode, stdDec);
						toolTipwidgets.add(standardItem);
					}
				} else {
					isGeneralInfo=true;
					DownToolTipWidgetUc toolTipUc = new DownToolTipWidgetUc(new Label(stdCode), new Label(stdDec), standardsList);
					toolTipUc.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().getstandardMoreInfo());
					standardsContainer.add(toolTipUc);
				}
				count++;
			}
			if (standardsList.size()>18){
				isGeneralInfo=true;
				final Label left = new Label("+"+(standardsList.size() - 18));
				toolTipwidgets.add(left);
				standardsContentContainer.setVisible(true);
			}
			if (standardsList.size() > 2) {
				isGeneralInfo=true;
				Integer moreStandardsCount = standardsList.size() - 3;
				if (moreStandardsCount >0){
					DownToolTipWidgetUc toolTipUc = new DownToolTipWidgetUc(new Label("+" + moreStandardsCount), toolTipwidgets, standardsList);
					toolTipUc.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().getstandardMoreLink());
					standardsContainer.add(toolTipUc);
					standardsContentContainer.setVisible(true);
				}
			}
			if(standardsList.isEmpty())
			{
				standardsContentContainer.setVisible(false);
			}
		}
		else{
			standardsContentContainer.setVisible(false);
		}
	}
	/**
	 * To set the Resource Original Url
	 * @param assetUri
	 * @param folder
	 * @param originalUrl
	 * @param resourceTypeName
	 */
	public void setOriginalUrl(String assetUri,String folder,String originalUrl,String resourceTypeName){
		this.originalUrlText.clear();
		if(originalUrl!=null&&!originalUrl.equalsIgnoreCase("")&&!originalUrl.equalsIgnoreCase("null")){
			if(resourceTypeName.equalsIgnoreCase("image/png")){
				if(!originalUrl.substring(0, 4).equalsIgnoreCase("http")){
					originalUrl=assetUri+folder+originalUrl;
				}
			}
			String[] urlFormat = originalUrl.split("\\.");
			String urlExtension = urlFormat[urlFormat.length - 1];
			if(urlExtension.equalsIgnoreCase("pdf")){
				if(!originalUrl.substring(0, 4).equalsIgnoreCase("http")){
					originalUrl=assetUri+folder+originalUrl;
				}
			}
			Anchor originalUrlAnchor=new Anchor(originalUrl);
			originalUrlAnchor.setHref(originalUrl);
			originalUrlAnchor.setStyleName("");
			originalUrlAnchor.setTarget("_blank");
			originalUrlAnchor.getElement().setAttribute("style"," word-break: break-all;");
			this.originalUrlText.add(originalUrlAnchor);
			this.originalUrlTitle.setVisible(true);
			this.originalUrlText.setVisible(true);
			isGeneralInfo=true;
		}else{
			this.originalUrlTitle.setVisible(false);
			this.originalUrlText.setVisible(false);
		}
	}
	
	/**
	 * 
	 * @param code
	 *//*
	private void setLicenCode(String code) {

		if(code==null||code.equalsIgnoreCase("")||code.equalsIgnoreCase("null") || code.equalsIgnoreCase("Not Available")){
			licenceCodePanel.setVisible(false);
		}else{
			licenceCodePanel.setVisible(true);
			licenceCodeType.setText(code);
			licenceCodeLbl.setText(i18n.GL1719+i18n.GL_SPL_SEMICOLON);
		}		
	}*/
	/**
	 * 
	 * @param createdOn
	 *//*
	private void setCreatedDate(Date createdOn) {

		String dateString = DateTimeFormat.getFormat("MM/dd/yyyy").format(createdOn);
		dateCreatedPanel.setVisible(true);
		createdDateInfo.setText(dateString);
		dateCreatedLbl.setText(i18n.GL1717+i18n.GL_SPL_SEMICOLON);
	}*/
	/**
	 * 
	 * @param attribution
	 * @param taxonomoyList
	 */
	public void setResourceAttribution(String attribution,Set<CodeDo> taxonomoyList){

		coursesList=new ArrayList<String>();
		if(taxonomoyList!=null){
			Iterator<CodeDo> taxonomyIterator=taxonomoyList.iterator();
			while (taxonomyIterator.hasNext()) {
				CodeDo codeDo=taxonomyIterator.next();
				if(codeDo.getDepth()==2){
					coursesList.add(codeDo.getLabel());
				}
			}

		}
		courseInfo.clear();
		if(coursesList.size()>0){
			isGeneralInfo=true;
			final Label courseInfoLabel=new Label(" "+coursesList.get(0));
			courseInfoLabel.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().resourceCourseLabel());
			courseInfo.add(courseInfoLabel);
			coursePanel.setVisible(true);
		}
		if(coursesList.size()>1){
			isGeneralInfo=true;
			final Label courseCountLabel=new Label("+"+(coursesList.size()-1)); 
			courseCountLabel.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().resourceCourseNum());
			courseInfo.add(courseCountLabel);
			Widget Coursewidget = getToolTipwidgets(coursesList);
			courseCountLabel.addMouseOverHandler(new MouseOverShowToolTip(Coursewidget));
			courseCountLabel.addMouseOutHandler(new MouseOutHideToolTip());
			coursePanel.setVisible(true);

		}
		if(coursesList.size()==0){
			coursePanel.setVisible(false);
		}

	}

	private Widget getToolTipwidgets(List<String> coursesList) {

		FlowPanel toolTipwidgets = new FlowPanel();
		for(int i=1;i<coursesList.size();i++){
			Label courseLabel = new Label(coursesList.get(i));
			toolTipwidgets.add(courseLabel);
		}
		return toolTipwidgets;
	}

	public class MouseOverShowToolTip implements MouseOverHandler
	{
		Widget widget;

		public MouseOverShowToolTip(Widget coursewidget) {
			this.widget = coursewidget;
		}

		@Override
		public void onMouseOver(MouseOverEvent event) {
			//toolTip = new ToolTipPopUp(widget,getWidget().getAbsoluteLeft() + (getWidget().getOffsetWidth() / 2) - (tooltipPopUpUc.getOffsetWidth() / 2), getWidget().getAbsoluteTop() + getWidget().getOffsetHeight());	
			toolTip = new ToolTipPopUp(widget,(event.getRelativeElement().getAbsoluteLeft()-106),(event.getRelativeElement().getAbsoluteTop()+9)); 
			toolTip.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().courseTooltip());
			toolTip.show();
		}

	}

	private void seteducationaluseDetails(List<String> eduUsedetails) {
		eduUseType.clear();
		if(eduUsedetails == null || eduUsedetails.size() == 0 || eduUsedetails.contains(null) || eduUsedetails.contains("") ){
			eduUsePanel.setVisible(false);
		}else{
			educationallLbl.setText(i18n.GL1720());
			if(eduUsedetails.size()>0){
				isEducationalInfo=true;
				final Label eduUseLabel=new Label(" "+eduUsedetails.get(0));
				eduUseLabel.getElement().setAttribute("style", "float: left;");
				eduUseType.add(eduUseLabel);
				eduUsePanel.setVisible(true);
				educationallLbl.setText(i18n.GL1720());
				educationallLbl.getElement().setAttribute("alt",i18n.GL1720());
				educationallLbl.getElement().setAttribute("title",i18n.GL1720());
				
				eduUseLbl.setText(i18n.GL1664().trim()+i18n.GL_SPL_SEMICOLON()+" ");
				eduUseLbl.getElement().setAttribute("alt",i18n.GL1664()+" "+i18n.GL_SPL_SEMICOLON()+" ");
				eduUseLbl.getElement().setAttribute("title",i18n.GL1664()+" "+i18n.GL_SPL_SEMICOLON()+" ");
				educationallLbl.setVisible(true);
			}
			if(eduUsedetails.size()>2){
				isEducationalInfo=true;
				final Label eduUseLabel=new Label("+"+(eduUsedetails.size()-2)); 
				eduUseLabel.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().resourceCourseNum());
				eduUseType.add(eduUseLabel);
				Widget eduusewidget = getCommonwidget(eduUsedetails);
				eduUseLabel.addMouseOverHandler(new MouseOverShowToolTip(eduusewidget));
				eduUseLabel.addMouseOutHandler(new MouseOutHideToolTip());
				eduUsePanel.setVisible(true);
				educationallLbl.setText(i18n.GL1720());
				educationallLbl.getElement().setAttribute("alt",i18n.GL1720());
				educationallLbl.getElement().setAttribute("title",i18n.GL1720());
				
				eduUseLbl.setText(i18n.GL1664().trim()+i18n.GL_SPL_SEMICOLON()+" ");
				eduUseLbl.getElement().setAttribute("alt",i18n.GL1664()+" "+i18n.GL_SPL_SEMICOLON()+" ");
				eduUseLbl.getElement().setAttribute("title",i18n.GL1664()+" "+i18n.GL_SPL_SEMICOLON()+" ");
				educationallLbl.setVisible(true);
			}
		}
	}
	
	private Widget getCommonwidget(List<String> commonList) {

		FlowPanel toolTipwidgets = new FlowPanel();
		for(int i=2;i<commonList.size();i++){
			Label commonLabel = new Label(commonList.get(i));
			toolTipwidgets.add(commonLabel);
		}
		return toolTipwidgets;
	}

	/**
	 * To set Resource Educational details
	 * @param collectionItemDo {@link CollectionItemDo} 
	 */

	private void setEducationInfoDetails(CollectionItemDo collectionItemDo) {

		List<String> eduUsedetails = new ArrayList<String>();
		List<String> depthofknowledgedetails = new ArrayList<String>();
		List<String> momentoflearningdetails = new ArrayList<String>();
		educationallLbl.setVisible(true);
		if(collectionItemDo.getResource()!=null ){
			if(collectionItemDo.getResource().getEducationalUse()!=null){
				if(collectionItemDo.getResource().getEducationalUse().size()>0){
					for(int i=0;i<collectionItemDo.getResource().getEducationalUse().size();i++){
						if(collectionItemDo.getResource().getEducationalUse().get(i).isSelected())
						{
							eduUsedetails.add(collectionItemDo.getResource().getEducationalUse().get(i).getValue());
						}
					}
					seteducationaluseDetails(eduUsedetails);
				}else{
					eduUsePanel.setVisible(false);
				}
			}

			if(collectionItemDo.getResource().getResourceFormat().getValue().equalsIgnoreCase("question")){
				depthofknowledgedetails = new ArrayList<String>();
				if(collectionItemDo.getResource().getDepthOfKnowledges()!=null){
					if(collectionItemDo.getResource().getDepthOfKnowledges().size()>0){
						for(int i=0;i<collectionItemDo.getResource().getDepthOfKnowledges().size();i++){
							if(collectionItemDo.getResource().getDepthOfKnowledges().get(i).isSelected())
							{
								depthofknowledgedetails.add(collectionItemDo.getResource().getDepthOfKnowledges().get(i).getValue());
							}
						}
						if(depthofknowledgedetails.size()>0){
						isEducationalInfo=true;
						dKnowledgeLbl.setVisible(true);
						InfoUtil.setDepthofknowledgeDetails(depthofknowledgedetails, dKnowledgeType, dKnowledgeLbl, dKnowledgePanel);
						}else{
							dKnowledgeLbl.setVisible(false);
						}
						//dKnowledgePanel.setVisible(true);
					}else{
						dKnowledgePanel.setVisible(false);
					}
				}else{
					dKnowledgePanel.setVisible(false);
				}
				momentsoflearningPanel.setVisible(false);	
			}
			else{
				momentoflearningdetails = new ArrayList<String>();
				if(collectionItemDo.getResource().getMomentsOfLearning()!=null){
					if(collectionItemDo.getResource().getMomentsOfLearning().size()>0){
						for(int i=0;i<collectionItemDo.getResource().getMomentsOfLearning().size();i++){
							if(collectionItemDo.getResource().getMomentsOfLearning().get(i).isSelected())
							{
								momentoflearningdetails.add(collectionItemDo.getResource().getMomentsOfLearning().get(i).getValue());
							}
						}
						if(momentoflearningdetails.size()>0){
						momentsoflearningLbl.setVisible(true);
						isEducationalInfo=true;
						InfoUtil.setDepthofknowledgeDetails(momentoflearningdetails,momentsoflearningType,momentsoflearningLbl,momentsoflearningPanel);
						}else{
							momentsoflearningLbl.setVisible(false);
						}
						//momentsoflearningPanel.setVisible(true);
					}else{
						momentsoflearningPanel.setVisible(false);	
					}
				}else{
					momentsoflearningPanel.setVisible(false);
				}
				dKnowledgePanel.setVisible(false);
			}
			if(collectionItemDo.getResource().getCustomFieldValues()!=null){
				String learningMode = collectionItemDo.getResource().getCustomFieldValues().getCfLearningMode()!=null ? collectionItemDo.getResource().getCustomFieldValues().getCfLearningMode() : null;
				String educationRole= collectionItemDo.getResource().getCustomFieldValues().getCfEndUser()!=null ? collectionItemDo.getResource().getCustomFieldValues().getCfEndUser() : null ;
				String age = collectionItemDo.getResource().getCustomFieldValues().getCfAge()!=null ? collectionItemDo.getResource().getCustomFieldValues().getCfAge() : null ;
				String readingLevel =collectionItemDo.getResource().getCustomFieldValues().getCfReadingLevel()!=null ? collectionItemDo.getResource().getCustomFieldValues().getCfReadingLevel() : null ;
				String schLevel = collectionItemDo.getResource().getCustomFieldValues().getCfSchoolLevel()!=null ? collectionItemDo.getResource().getCustomFieldValues().getCfSchoolLevel() : null ;
				String alignType = collectionItemDo.getResource().getCustomFieldValues().getCfEducationalAlignment()!=null ? collectionItemDo.getResource().getCustomFieldValues().getCfEducationalAlignment() : null;

				if(alignType!=null && !alignType.equals("")){
					isEducationalInfo=true;
					eduAllignType.setText(" "+alignType);
					eduAllignType.getElement().setAttribute("alt"," "+alignType);
					eduAllignType.getElement().setAttribute("title"," "+alignType);
					eduAllignPanel.setVisible(true);
				}else{
					eduAllignPanel.setVisible(false);
				}
				if(educationRole!=null && !educationRole.equals("")){
					isEducationalInfo=true;
					eduRoleType.setText(" "+educationRole);
					eduRoleType.getElement().setAttribute("alt"," "+educationRole);
					eduRoleType.getElement().setAttribute("title"," "+educationRole);
					eduRolePanel.setVisible(true);
				}else{
					eduRolePanel.setVisible(false);
				}
				if(learningMode!=null && !learningMode.equals("")){
					isEducationalInfo=true;
					interactiveType.setText(" "+learningMode);
					interactiveType.getElement().setAttribute("alt"," "+learningMode);
					interactiveType.getElement().setAttribute("title"," "+learningMode);
					interactivityTypePanel.setVisible(true);
				}else{
					interactivityTypePanel.setVisible(false);
				}
				if(age!=null && !age.equals("")){
					isEducationalInfo=true;
					ageRangeType.setText(" "+age);
					ageRangeType.getElement().setAttribute("alt"," "+age);
					ageRangeType.getElement().setAttribute("title"," "+age);
					ageRangePanel.setVisible(true);
				}else{
					ageRangePanel.setVisible(false);
				}

				if(readingLevel!=null && !readingLevel.equals("")){
					isEducationalInfo=true;
					readingLevelType.getElement().setInnerText(" "+readingLevel);
					readingLevelType.getElement().setAttribute("alt"," "+readingLevel);
					readingLevelType.getElement().setAttribute("title"," "+readingLevel);
					readingLevelPanel.setVisible(true);
				}else{
					readingLevelPanel.setVisible(false);
				}

				if(schLevel!=null && !schLevel.equals("")){
					isEducationalInfo=true;
					schLevelInfo.setText(" "+schLevel);
					schLevelInfo.getElement().setAttribute("alt"," "+schLevel);
					schLevelInfo.getElement().setAttribute("title"," "+schLevel);
					schLevelPanel.setVisible(true);
				}else{
					schLevelPanel.setVisible(false);
				}

				if(learningMode==null && educationRole==null && age==null && readingLevel==null && schLevel==null
						&& alignType==null && depthofknowledgedetails.isEmpty() && momentoflearningdetails.isEmpty() && eduUsedetails.isEmpty()){
					educationallLbl.setVisible(false);
				}
			}else{
				if(depthofknowledgedetails.isEmpty() && momentoflearningdetails.isEmpty() && eduUsedetails.isEmpty()){
					educationallLbl.setVisible(false);
				}
			}
			
		}else{
			educationallLbl.setVisible(false);
			schLevelPanel.setVisible(false);
			readingLevelPanel.setVisible(false);
			ageRangePanel.setVisible(false);
			interactivityTypePanel.setVisible(false);
			eduRolePanel.setVisible(false);
			eduAllignPanel.setVisible(false);
			momentsoflearningPanel.setVisible(false);
			dKnowledgePanel.setVisible(false);
		}
		if(isEducationalInfo){
			educationallLbl.setVisible(true);
		}else{
			educationallLbl.setVisible(false);
		}
	}
	
	/**
	 * To set Resource Information details.
	 * @param collectionInfo instance of {@link CollectionItemDo}
	 */
	
	protected void setResourceInfoDetails(CollectionItemDo collectionInfo) {
		
		if(collectionInfo.getResource()!=null && collectionInfo.getResource().getCustomFieldValues()!=null){
			resourceInfoLbl.setVisible(true);
			customFieldValuesDO customField= collectionInfo.getResource().getCustomFieldValues();
			
			String countryCode= customField.getCfCountryCode()!=null ? customField.getCfCountryCode() : null;
			String language= customField.getCfLanguageCode()!=null ? customField.getCfLanguageCode() : null;
			String dataType= customField.getCfDataType()!=null ? customField.getCfDataType() : null;
			String author= customField.getCfAuthor()!=null ? customField.getCfAuthor() : null;
			String copyrightHolder= customField.getCfCopyrightHolder()!=null ? customField.getCfCopyrightHolder() : null;
			String keywords= customField.getCfKeywords()!=null ? customField.getCfKeywords() : null;
			String ads= customField.getCfAds()!=null ? customField.getCfAds() : null;
			
			
			if(countryCode!=null && !countryCode.equals("")){
				isResourceInfo=true;
				countryCodeType.setText(" "+countryCode);
				countryCodeType.getElement().setAttribute("alt"," "+countryCode);
				countryCodeType.getElement().setAttribute("title"," "+countryCode);
				countryCodePanel.setVisible(true);
			}else{
				countryCodePanel.setVisible(false);
			}
			if(language!=null && !language.equals("")){
				isResourceInfo=true;
				languageType.setText(" "+language);
				languageType.getElement().setAttribute("alt"," "+language);
				languageType.getElement().setAttribute("title"," "+language);
				languagePanel.setVisible(true);
			}else{
				languagePanel.setVisible(false);
			}
			if(dataType!=null && !dataType.equals("")){
				isResourceInfo=true;
				dataTypeFormat.setText(" "+dataType);
				dataTypeFormat.getElement().setAttribute("alt"," "+dataType);
				dataTypeFormat.getElement().setAttribute("title"," "+dataType);
				dataTypePanel.setVisible(true);
			}else{
				dataTypePanel.setVisible(false);
			}
			if(copyrightHolder!=null && !copyrightHolder.equals("")){
				isResourceInfo=true;
				copyRightType.setText(" "+copyrightHolder);
				copyRightType.getElement().setAttribute("alt"," "+copyrightHolder);
				copyRightType.getElement().setAttribute("title"," "+copyrightHolder);
				copyRightPanel.setVisible(true);
			}else{
				copyRightPanel.setVisible(false);
			}
			if(author!=null && !author.equals("")){
				isResourceInfo=true;
				authorName.setText(" "+author);
				authorName.getElement().setAttribute("alt"," "+author);
				authorName.getElement().setAttribute("title"," "+author);
				authorPanel.setVisible(true);
			}else{
				authorPanel.setVisible(false);
			}
			if(keywords!=null && !keywords.equals("")){
				isResourceInfo=true;
				keywordsInfo.getElement().setInnerText(" "+keywords);
				keywordsInfo.getElement().setAttribute("alt"," "+keywords);
				keywordsInfo.getElement().setAttribute("title"," "+keywords);
				keyWordsPanel.setVisible(true);
			}else{
				keyWordsPanel.setVisible(false);
			}
			if(ads!=null && !ads.equals("")){
				isResourceInfo=true;
				adsName.setText(" "+ads);
				adsName.getElement().setAttribute("alt"," "+ads);
				adsName.getElement().setAttribute("title"," "+ads);
				adsPanel.setVisible(true);
			}else{
				adsPanel.setVisible(false);
			}
			
			if(countryCode==null && language==null && dataType==null && author==null && copyrightHolder==null
					&& keywords==null && ads==null){
				resourceInfoLbl.setVisible(false);
			}
			
		}else{
			countryCodePanel.setVisible(false);
			adsPanel.setVisible(false);
			keyWordsPanel.setVisible(false);
			authorPanel.setVisible(false);
			copyRightPanel.setVisible(false);
			dataTypePanel.setVisible(false);
			languagePanel.setVisible(false);
			resourceInfoLbl.setVisible(false);
		}
		if(isResourceInfo){
			resourceInfoLbl.setVisible(true);
		}else{
			resourceInfoLbl.setVisible(false);
		}
	}
	
	/**
	 * To set the Resource Accessibility details 
	 * @param collectionItem instance of {@link CollectionItemDo} 
	 */
	
	protected void setAccessibilityDetails(CollectionItemDo collectionItem) {
		
		if(collectionItem.getResource()!=null){
			accesibilityLbl.setVisible(true);
			String mobileFriendly= collectionItem.getResource().getMediaType()!=null ? collectionItem.getResource().getMediaType() : null;
			String accessMode = null,mediaFeature = null, controlFlexibility = null, accessHazard = null;
			
			if(mobileFriendly!=null && !mobileFriendly.equals("")){
				if(mobileFriendly.equalsIgnoreCase(IPAD_FRIENDLY)){
					mobileFriendlyPanel.setVisible(true);
					mbFriendlyText.setText(" "+i18n.GL_GRR_YES());
					mbFriendlyText.getElement().setAttribute("alt"," "+i18n.GL_GRR_YES());
					mbFriendlyText.getElement().setAttribute("title"," "+i18n.GL_GRR_YES());
					isAccessibilityInfo=true;
				}else{
					mobileFriendlyPanel.setVisible(true);
					mbFriendlyText.setText(" "+i18n.GL1735());
					mbFriendlyText.getElement().setAttribute("alt"," "+i18n.GL1735());
					mbFriendlyText.getElement().setAttribute("title"," "+i18n.GL1735());
					isAccessibilityInfo=true;
				}
			}else{
				mobileFriendlyPanel.setVisible(false);
			}
			
			if(collectionItem.getResource().getCustomFieldValues()!=null){
				customFieldValuesDO customFields=collectionItem.getResource().getCustomFieldValues();
				
				accessMode= customFields.getCfAccessMode()!=null ? customFields.getCfAccessMode() : null ;
				mediaFeature = customFields.getCfMediaFeature()!=null ? customFields.getCfMediaFeature() : null;
				controlFlexibility= customFields.getCfControlFlexibility()!=null ? customFields.getCfControlFlexibility() : null;
				accessHazard = customFields.getCfAccessHazard()!=null ? customFields.getCfAccessHazard() : null;
				if(accessMode!=null && !accessMode.equals("")){
					accessModeType.getElement().setInnerText(" "+accessMode);
					accessModePanel.setVisible(true);
					isAccessibilityInfo=true;
				}else{
					accessModePanel.setVisible(false);
				}
				if(mediaFeature!=null && !mediaFeature.equals("")){
					mediaFeatureType.getElement().setInnerText(" "+mediaFeature);
					mediaFeatureType.getElement().setAttribute("alt"," "+mediaFeature);
					mediaFeatureType.getElement().setAttribute("title"," "+mediaFeature);
					
					mediaFeaturePanel.setVisible(true);
					isAccessibilityInfo=true;
				}else{
					mediaFeaturePanel.setVisible(false);
				}
				if(controlFlexibility!=null && !controlFlexibility.equals("")){
					controlType.setText(" "+controlFlexibility);
					controlType.getElement().setAttribute("alt"," "+controlFlexibility);
					controlType.getElement().setAttribute("title"," "+controlFlexibility);
					controlPanel.setVisible(true);
					isAccessibilityInfo=true;
				}else{
					controlPanel.setVisible(false);
				}
				if(accessHazard!=null && !accessHazard.equals("")){
					acessHazardType.setText(" "+accessHazard);
					acessHazardType.getElement().setAttribute("alt"," "+accessHazard);
					acessHazardType.getElement().setAttribute("title"," "+accessHazard);
					accessHazardPanel.setVisible(true);
					isAccessibilityInfo=true;
				}else{
					accessHazardPanel.setVisible(false);
				}
			}
			
			if(mobileFriendly==null && accessMode==null && mediaFeature==null && controlFlexibility==null && accessHazard==null){
				accesibilityLbl.setVisible(false);
			}
			 
		}else{
			accesibilityLbl.setVisible(false);
			accessHazardPanel.setVisible(false);
			controlPanel.setVisible(false);
			mediaFeaturePanel.setVisible(false);
			accessModePanel.setVisible(false);
			mobileFriendlyPanel.setVisible(false);
		}
		if(isAccessibilityInfo){
			accesibilityLbl.setVisible(true);	
		}else{
			accesibilityLbl.setVisible(false);
		}
		if(isGrades){
			gradesPanel.setVisible(true);
			gradeTitle.setVisible(true);
			gradesText.setVisible(true);
		}else{
			gradesPanel.setVisible(false);
			gradeTitle.setVisible(false);
			gradesText.setVisible(false);
		}
	}
	
	
	/**
	 * @return the loadingImageLabel
	 */
	public HTMLPanel getLoadingImagePanel() {
		return loadingImagePanel;
	}

	@UiHandler("addTagsBtn")
	public void onAddTagsBtnClicked(ClickEvent clickEvent) 
	{
		if(AppClientFactory.isAnonymous()) {
			AppClientFactory.fireEvent(new InvokeLoginEvent());
		} else {
			Window.enableScrolling(false);
			popup=new AddTagesPopupView(searchResultDo.getGooruOid()) {

				@Override
				public void closePoup(boolean isCancelclicked) {
					 this.hide();
					 if(!isCancelclicked){
				        SuccessPopupViewVc success = new SuccessPopupViewVc() {

							@Override
							public void onClickPositiveButton(ClickEvent event) {
								this.hide();
								
								if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.COLLECTION_SEARCH) || AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)){
									Window.enableScrolling(false);
								}else{
									Window.enableScrolling(true);
								}
							}
							
						};
						success.setHeight("253px");
						success.setWidth("450px");
						success.setPopupTitle(i18n.GL1795());
						success.setDescText(i18n.GL1796());
						success.enableTaggingImage();
						success.setPositiveButtonText(i18n.GL0190());
						success.center();
						success.show();
					 }else{
						 if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.COLLECTION_SEARCH) || AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)){
								Window.enableScrolling(false);
							}else{
								Window.enableScrolling(true);
							}
					 }
				}
			};
			popup.show();
			popup.setPopupPosition(popup.getAbsoluteLeft(),Window.getScrollTop()+10);
		}
	}
	
	private String generateGradeIfHypen(String grade) {
		String gradeList[];
	 
		StringBuilder gradeStr = new StringBuilder();
		gradeList = grade.split("-");
		if (gradeList.length >= 2) {
			int start = Integer.parseInt(gradeList[0].trim());
			int end = Integer.parseInt(gradeList[1].trim());
			if (start < end) {
				for (int i = start; i <= end; i++) {
					if (i == end) {
						gradeStr.append(i);
					} else {
						gradeStr.append(i).append(",");
					}
				}
			}
		} else {
			try
			{
			gradeStr.append(Math.round(Double.parseDouble(gradeList[0].trim())));
			}
			catch(Exception ex)
			{
				gradeStr.append(gradeList[0].trim());
			}
		}
		return gradeStr.toString();
	}	
	public List<Integer> sortList(List<Integer> list) {

		int listSize = list.size();

		for (int i = 0; i < listSize; i++) {

			for (int j = 1; j < listSize - i; j++) {
				if (list.get(j - 1) > list.get(j)) {
					int temp = list.get(j - 1);
					list.set(j - 1, list.get(j));
					list.set(j, temp);
				}
			}
		}

		return list;
	}
	
	private void setGrades(List<String> gradesdetails) {
		// TODO Auto-generated method stub
		gradesText.clear();
		if(gradesdetails == null || gradesdetails.size() == 0 || gradesdetails.contains(null) || gradesdetails.contains("") ){
		}else{
		if(gradesdetails.size()>0){
			if(gradesdetails.size()==1){
				final Label gradesLabel=new Label(" "+gradesdetails.get(0));
				gradesLabel.getElement().setAttribute("style", "float: left;");
				gradesText.add(gradesLabel);
				isGrades =true;
			} if(gradesdetails.size()==2){
				final Label gradesLabel=new Label(" "+gradesdetails.get(0)+","+gradesdetails.get(1));
				gradesLabel.getElement().setAttribute("style", "float: left;");
				gradesText.add(gradesLabel);
				isGrades =true;
			}
		}
		if(gradesdetails.size()>2){
			final Label gradesLabelCountLabel=new Label("+"+(gradesdetails.size()-2)); 
			gradesLabelCountLabel.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().resourceCourseNum());
			final Label gradesLabel=new Label(" "+gradesdetails.get(0)+","+gradesdetails.get(1));
			gradesLabel.getElement().setAttribute("style", "float:left;");
			gradesText.add(gradesLabel);
			gradesText.add(gradesLabelCountLabel);
			Widget gradeswidget = getCommonwidget(gradesdetails);
			gradesLabelCountLabel.addMouseOverHandler(new MouseOverShowToolTip(gradeswidget));
			gradesLabelCountLabel.addMouseOutHandler(new MouseOutHideToolTip());
			isGrades =true;
		}
		}
	}
	public void setIdForLabel(Label label,String uiFieldName,String text){
		label.getElement().setId("lbl"+uiFieldName);
		label.getElement().setAttribute("alt",text);
		label.getElement().setAttribute("title",text);
	}
}
