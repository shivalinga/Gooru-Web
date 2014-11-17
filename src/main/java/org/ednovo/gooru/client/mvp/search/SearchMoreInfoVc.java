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
package org.ednovo.gooru.client.mvp.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.dnd.AppMirageDragContainer;
import org.ednovo.gooru.client.mvp.resource.dnd.ResourceDragController;
import org.ednovo.gooru.client.uc.DownToolTipWidgetUc;
import org.ednovo.gooru.client.uc.ErrorMessagePanel;
import org.ednovo.gooru.client.uc.LicencegItemVc;
import org.ednovo.gooru.client.uc.StandardSgItemVc;
import org.ednovo.gooru.client.uc.tooltip.ToolTip;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.content.TagDo;
import org.ednovo.gooru.shared.model.search.ResourceSearchResultDo;
import org.ednovo.gooru.shared.model.search.SearchDo;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Search Team
 * 
 * @param <T>
 * @param <C>
 */
public abstract class SearchMoreInfoVc<T extends ResourceSearchResultDo, C extends ResourceSearchResultDo>	extends FocusPanel{

	protected static SearchMoreInfoVcUiBinder uiBinder = GWT
			.create(SearchMoreInfoVcUiBinder.class);
	
	static MessageProperties i18n = GWT.create(MessageProperties.class);

	interface SearchMoreInfoVcUiBinder extends
			UiBinder<Widget, SearchMoreInfoVc<?, ?>> {
	}

	@UiField
	public MoreInfoFieldVc resourceSearchGradeFieldVc,resourceSearchRightsFieldVc;



	@UiField
	public Label countLbl, countLblTxt,rightsLbl,lblNotFriendly;

	@UiField Image imgQuestionImage;
	
	@UiField
	ScrollPanel resourceScrPanel;
	
	

	@UiField
	ErrorMessagePanel messageInfo;
	
	

	@UiField(provided = true)
	public AppMirageDragContainer usedInResourcesPanel;

	@UiField
	FlowPanel resourceMoreInfoRightPanel;

	@UiField(provided = true)
	SearchMoreInfoVcCBundle res;

	protected static final String LICENSE_FOLDER = "images/license/";

	private SimpleAsyncCallback<SearchDo<C>> usedInResourcesAsyncCallback;

	private T searchResultDo;

	private SearchDo<C> usedInSearchDo;

	protected static final String OER_DESCRIPTION = i18n.GL1092();

	protected static final String OER_TITLE = i18n.GL1093();

	private static final String OER_PNG_IMG = "oer.png";

	private static final String OERCOMMONS_ORG = "oercommons.org";

	private static final String OER = "OER";

	private static final String NULL = "null";
	
	private static final String ALL_GRADES = i18n.GL1467().toUpperCase();
	
	ToolTip toolTip = null;
	
	private HashMap<String, SimpleResourceVc> simpleResourceMap=new HashMap<String, SimpleResourceVc>();
	
	public SearchMoreInfoVc(){
		
		usedInResourcesPanel = new AppMirageDragContainer(null);
//		setHandler();
	}

	/**
	 * Class constructor, creates new instance of {@link ScrollHandler},
	 * {@link AppMirageDragContainer} and assign usedSearchDo page size
	 * 
	 * @param searchDragController
	 *            instance of {@link ResourceDragController}
	 */
	
	
	public SearchMoreInfoVc(final ResourceDragController searchDragController,	boolean isResourceSearch) {
		this.res = SearchMoreInfoVcCBundle.INSTANCE;
		res.css().ensureInjected();
		usedInResourcesPanel = new AppMirageDragContainer(searchDragController);
		usedInResourcesPanel.setClonnable(true);
		usedInSearchDo = new SearchDo<C>();
		
		if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)){
			
			usedInSearchDo.setPageSize(5);
		}
		else{
			usedInSearchDo.setPageSize(20);
		}
		
		setWidget(uiBinder.createAndBindUi(this));

		rightsLbl.setText("");
		rightsLbl.getElement().setAttribute("alt","");
		rightsLbl.getElement().setAttribute("title","");
		
		resourceSearchRightsFieldVc.setToolTip(i18n.GL0730());
		imgQuestionImage.setTitle(i18n.GL0732());
		imgQuestionImage.setAltText(i18n.GL0732());
		imgQuestionImage.setUrl("images/mos/questionmark.png");
		resourceSearchGradeFieldVc.setToolTip(i18n.GL0325());
		rightsLbl.setVisible(false);
		

		setUsedInResourcesAsyncCallback(new SimpleAsyncCallback<SearchDo<C>>() {

			@Override
			public void onSuccess(SearchDo<C> result) {
				usedInResourcesPanel.setClonnable(false);
//				usedInResourcesPanel.clear();
				usedInResourcesPanel.setClonnable(true);
				usedInSearchDo = result;
				simpleResourceMap.clear();
				setUsedInResources(result.getSearchResults());
			}
		});
		if (isResourceSearch) {
			resourceScrPanel.addScrollHandler(new ScrollHandler() {

				@Override
				public void onScroll(ScrollEvent event) {
					if (resourceScrPanel.getVerticalScrollPosition() == resourceScrPanel
							.getMaximumVerticalScrollPosition()
							&& usedInSearchDo.getPageNum() < usedInSearchDo
									.getTotalPages()) {
						usedInSearchDo.setPageNum(usedInSearchDo.getPageNum() + 1);
						usedInSearchDo.setSearchResults(null);
						if(!AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.RESOURCE_SEARCH)){
							requestUsedInResources();
						}
						
					}
				}
			});
			
		}
	
		/*rightsLbl.setVisible(false);
		if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)){
		//	metaInfoFloPanel.setVisible(false);
			rightsLbl.setVisible(true);
			resourceScrPanel.setStyleName(res.css().moreInfoResourceSearceRightScrollPanel());
			resourceMoreInfoRightPanel.getElement().getStyle().setWidth(489, Unit.PX);
//			resourceScrPanel.setStyleName(res.css().moreInfoResourceSearchRightPanel());
		}
		else{
			metaInfoFloPanel.setVisible(true);
			shareField.setVisible(false);
		}*/
		setHandler();
		
		rightsLbl.getElement().setId("lblRightsLbl");
		resourceScrPanel.getElement().setId("sbResourceScrPanel");
		resourceMoreInfoRightPanel.getElement().setId("fpnlResourceMoreInfoRightPanel");
		countLblTxt.getElement().setId("lblCountLblTxt");
		countLbl.getElement().setId("lblCountLbl");
		lblNotFriendly.getElement().setId("lblNotFriendly");
		imgQuestionImage.getElement().setId("imgQuestionImage");
		messageInfo.getElement().setId("errlblMessageInfo");
	}

	public void setHandler(){
		imgQuestionImage.addMouseOverHandler(new MouseOverHandler() {
			
			@Override
			public void onMouseOver(MouseOverEvent event) {
				toolTip = new ToolTip(i18n.GL0454()+""+"<img src='/images/mos/MobileFriendly.png' style='margin-top:0px;width:20px;height:15px;'/>"+" "+i18n.GL04431()+" "+"<img src='/images/mos/mobileunfriendly.png' style='margin-top:0px;width:20px;height:15px;'/>"+" "+i18n.GL_SPL_EXCLAMATION());
				toolTip.getTootltipContent().getElement().setAttribute("style", "width: 258px;");
				toolTip.getElement().getStyle().setBackgroundColor("transparent");
				toolTip.getElement().getStyle().setPosition(Position.ABSOLUTE);
				toolTip.setPopupPosition(imgQuestionImage.getAbsoluteLeft()-(50+22), imgQuestionImage.getAbsoluteTop()+22);
				toolTip.show();
			}
		});
		imgQuestionImage.addMouseOutHandler(new MouseOutHandler() {
			
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
		imgQuestionImage.setVisible(false);
	}
	
	/**
	 * Reset usedInSearchDo values, resource count text and resource count
	 */
	public void reset(boolean moreInfoMode) {
		if(usedInSearchDo!=null){
			usedInSearchDo.setPageNum(1);
			usedInSearchDo.setSearchResults(null);
		}
		
		usedInResourcesPanel.setClonnable(false);
		usedInResourcesPanel.clear();
		getMessageInfo().setVisible(false);
		setResourceCountTxt("");
		setResourceCount("");
		setNotFriendly("");
		usedInResourcesPanel.setClonnable(true);
		if(moreInfoMode)
		requestUsedInResources();
	}

	/**
	 * @return type of SearchResultDo
	 */
	public T getSearchResultDo() {
		return searchResultDo;
	}

	/**
	 * @return the usedInSearchDo
	 */
	public SearchDo<C> getUsedInSearchDo() {
		return usedInSearchDo;
	}

	/**
	 * Set search meta data and styles for widget
	 * 
	 * @param searchResultDo
	 *            type of searchResultDo
	 */
	public void setData(T searchResultDo) {
		
		usedInSearchDo.setQuery(searchResultDo.getGooruOid());  
		this.searchResultDo = searchResultDo;
		String grade = searchResultDo.getGrade();
		if (grade != null) {
			grade = grade.replace("null,", "").replace("null ,", "").replace("null", "");
			
		}
		if (StringUtil.hasValidString(grade)) {

			boolean isKindergarten = false;
			boolean isHigherEducation = false;

			if (grade.indexOf("Kindergarten") != -1) {
				isKindergarten = true;
			}

			if (grade.indexOf("Higher Education") != -1) {
				isHigherEducation = true;
			}
			if (grade.indexOf("-") > 0) {
				if (grade.indexOf(",") == -1) {
					grade = generateGradeIfHypen(grade);
					
				}
			}

			List<String> gradeList = Arrays.asList(grade.split(","));
			int gradeListSize = gradeList.size();

			StringBuilder finalGradeStringB = new StringBuilder();
			
			
			List<Integer> gradeListInt = new ArrayList<Integer>();
			finalGradeStringB.append(gradeListSize > 1 ? i18n.GL1320_1()+i18n.GL_SPL_SEMICOLON()+" " : i18n.GL0325()+i18n.GL_SPL_SEMICOLON()+" ");
			
			
			for (String eachGrade1 : gradeList) {
				if (!eachGrade1.equalsIgnoreCase("Kindergarten")
						&& !eachGrade1.equalsIgnoreCase("Higher Education")) {

					eachGrade1 = eachGrade1.replaceAll("th", "")
							.replaceAll("TH", "").replaceAll("st", "")
							.replaceAll("ST", "").replaceAll("nd", "")
							.replaceAll("ND", "").replaceAll("rd", "")
							.replaceAll("RD", "");
					eachGrade1 = eachGrade1.toLowerCase()
							.replaceAll("Grade", "").replaceAll("grade", "");
					eachGrade1 = eachGrade1.toLowerCase().replaceAll("K-", "")
							.replaceAll("k-", "");
					eachGrade1 = eachGrade1.toLowerCase().replaceAll("K", "")
							.replaceAll("k", "");
					try {

						
						 
					//	gradeListInt.clear();
						String grad[] = generateGradeIfHypen(eachGrade1).trim().split(",");
						for (int i = 0; i < grad.length; i++) {
 
							gradeListInt.add(Integer.parseInt(grad[i]));
							
						}

					} catch (Exception e) {
						//gradeListInt.add(0);
						e.printStackTrace();
					}
				}
			}
			gradeListInt = sortList(gradeListInt);
			String finalGrde = formatGrades(gradeListInt);
			if(finalGrde.equalsIgnoreCase(ALL_GRADES)){
				finalGradeStringB.append(ALL_GRADES);
			}else{
			   if(isKindergarten&&isHigherEducation){
				    finalGradeStringB.append(finalGrde.equalsIgnoreCase("")?"Kindergarten":"Kindergarten, ");
					finalGradeStringB.append(finalGrde);
					finalGradeStringB.append(finalGrde.equalsIgnoreCase("")?", Higher Education":", Higher Education");
			   }else if(isKindergarten){
					finalGradeStringB.append(finalGrde.equalsIgnoreCase("")?"Kindergarten":"Kindergarten, ");
					finalGradeStringB.append(finalGrde);
				}else if(isHigherEducation){
					finalGradeStringB.append(finalGrde);
					finalGradeStringB.append(finalGrde.equalsIgnoreCase("")?"Higher Education":", Higher Education");
				}else{
					finalGradeStringB.append(finalGrde);
				}
			}
			

			

			grade = finalGradeStringB.toString();
			
/*			if(!AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)){
				if(!gradeFieldVc.getElement().toString().contains("Grade:")) {
					gradeFieldVc.setHtmlTxt(grade);
				}
			}*/
			
		} else {
			//gradeFieldVc.setHtmlTxt(null);
		}
		//timeFieldVc.setHtmlTxt(StringUtil.getValidString(searchResultDo.getAverageTime(), null));
//		likesFieldVc.setHtmlTxt(StringUtil.getValidStringWithSuffix(searchResultDo.getVotesUp() + "", "0", " likes"));
		/*if (searchResultDo.getLicense() != null	&& searchResultDo.getLicense().getIcon() != null&& searchResultDo.getLicense().getIcon().isEmpty()) {

			rightsFieldVc.contentFloPanel.setVisible(false);
			rightsFieldVc.imageIconSimPanel.setVisible(false);

		}*/
		if (searchResultDo.getLicense() != null	&& searchResultDo.getLicense().getIcon() != null&& searchResultDo.getLicense().getIcon().isEmpty()&&AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)) {

			resourceSearchRightsFieldVc.contentFloPanel.setVisible(false);
			resourceSearchRightsFieldVc.imageIconSimPanel.setVisible(false);
			rightsLbl.setVisible(false);

		}
/*		else{
			rightsFieldVc.contentFloPanel.setVisible(false);
			rightsFieldVc.imageIconSimPanel.setVisible(false);
		}*/
		if (searchResultDo.getLicense() != null	&& searchResultDo.getLicense().getIcon() != null&& !searchResultDo.getLicense().getIcon().equals(NULL)) {
			Image image = new Image(searchResultDo.getAssetURI()+ searchResultDo.getLicense().getIcon());
			image.setAltText(i18n.GL0730());
			image.setTitle(i18n.GL0730());
			//rightsLbl.getElement().setAttribute("style", "padding-left: 309px;");
			StandardSgItemVc standardItem = null;
			LicencegItemVc licencegItemVcObj=null;
			if (searchResultDo.getLicense().getCode() != null&& !searchResultDo.getLicense().getCode().equalsIgnoreCase(NULL)) {
				//standardItem = new StandardSgItemVc(searchResultDo.getLicense()	.getCode(), searchResultDo.getLicense().getDefinition());
				licencegItemVcObj=new LicencegItemVc(searchResultDo.getLicense().getCode(), searchResultDo.getLicense().getDefinition());
			}
			DownToolTipWidgetUc widget = new DownToolTipWidgetUc(image,	standardItem);
			DownToolTipWidgetUc widget1 = new DownToolTipWidgetUc(image,licencegItemVcObj);
			widget1.setStyleName("rightsToolTip");
			/*if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.RESOURCE_SEARCH)){
				rightsLbl.setVisible(true);
				resourceSearchRightsFieldVc.addWidget(widget1);
				widget.setStyleName("rightsToolTip");
				resourceSearchRightsFieldVc.addWidget(widget);
			}else{
				//rightsFieldVc.addWidget(widget1);
				widget.setStyleName("rightsToolTip");
				//rightsFieldVc.addWidget(widget);
			}*/
			
		} else if (searchResultDo.getLicense() != null	&& searchResultDo.getLicense().getTag() == OER|| (searchResultDo.getUrl() != null && searchResultDo.getUrl().indexOf(OERCOMMONS_ORG) >= 0)) {
			Image image = new Image(LICENSE_FOLDER + OER_PNG_IMG);
			image.setAltText(i18n.GL1098());
			image.setTitle(i18n.GL1098());
			StandardSgItemVc standardItem = new StandardSgItemVc(OER_TITLE,	OER_DESCRIPTION);
			standardItem.setHeight("100px");
			DownToolTipWidgetUc widget = new DownToolTipWidgetUc(image,	standardItem);
			widget.setStyleName("rightsToolTip");
			/*if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.RESOURCE_SEARCH)){
				rightsLbl.setVisible(true);
				resourceSearchRightsFieldVc.addWidget(widget);
			}else{
				//rightsFieldVc.addWidget(widget);
			}*/
			
		} else {
			rightsLbl.setVisible(false);
			//rightsFieldVc.setHtmlTxt(null);
			resourceSearchRightsFieldVc.setHtmlTxt(null);
		}
		if (searchResultDo.getTagSet() != null
				&& !searchResultDo.getTagSet().equals(null)
				&& !searchResultDo.getTagSet().equals("")) {
			int tagIndex = 0;
			int tagLength = 0;
			StringBuilder tagList = new StringBuilder();
			for (TagDo tag : searchResultDo.getTagSet()) {
				tagLength += tag.getLabel().length();
				Label label = new Label((tagIndex > 0 ? "," : "")
						+ tag.getLabel());
				label.setStyleName(SearchResultWrapperCBundle.INSTANCE.css()
						.tagText());
				label.setStyleName("tagToolStyle");
				tagList.append((tagIndex > 0 ? ", " : "") + tag.getLabel());
	/*			if (tagIndex < 4) {
					tagsFieldVc.addWidget(label);
				}*/
				tagIndex++;
			}

			if (tagLength > 50) {
				Label toolTipInfo = new Label(tagList.toString());
				toolTipInfo.setWidth("100px");
				DownToolTipWidgetUc toolTipUc = new DownToolTipWidgetUc(
						new Label("+" + (tagIndex - 4)), toolTipInfo);
				toolTipUc.setStyleName(SearchResultWrapperCBundle.INSTANCE
						.css().blueLink());
		/*		tagsFieldVc.addWidget(toolTipUc);*/
			} /*else {
				tagsFieldVc.setHtmlTxt(null);
			}*/

		} /*else {
			tagsFieldVc.setHtmlTxt(null);
		}*/
	}
	
	

	/*
	 * private String generateGrade(String gradeTxt){ String tmpGradeTxt = "";
	 * if (gradeTxt.indexOf("-") > 0){ if (gradeTxt.indexOf(",") == -1){
	 * tmpGradeTxt = generateGradeIfHypen(gradeTxt); }else{ String gradeList[];
	 * gradeList = gradeTxt.split(","); for (int k=0; k<gradeList.length;k++){
	 * if (gradeList[k].indexOf("-") > 0){ if (k==gradeList.length){ tmpGradeTxt
	 * = tmpGradeTxt + generateGradeIfHypen(gradeList[k]); }else{ tmpGradeTxt =
	 * tmpGradeTxt + generateGradeIfHypen(gradeList[k]) + ","; } }else{ if
	 * (k==gradeList.length-1){ tmpGradeTxt = tmpGradeTxt + gradeList[k]; }else{
	 * tmpGradeTxt = tmpGradeTxt + gradeList[k] + ","; } } } } } return
	 * tmpGradeTxt; }
	 */

	private String formatGrades(List<Integer> list) {
		
    	StringBuffer grade = new StringBuffer();
		try {
			if(list.size()>3){
                int firstGrade=list.get(0);
                int lastGrade=list.get(list.size()-1);
                String displayGrade=firstGrade+"-"+lastGrade;
                if(list.size()>=5){
                	if(firstGrade<=4&&lastGrade>=9){
                		grade.append("ALL GRADES");
                	}
                	else{
                		grade.append(firstGrade);
                		grade.append("-");
                		grade.append(lastGrade);
                	}
                	
                }else{
                        if(displayGrade.equalsIgnoreCase("1-12")){
                                grade.append("ALL GRADES");
                        }else{
                                grade.append(firstGrade);
                                grade.append("-");
                                grade.append(lastGrade);
                        }
                }
        }else{
                for(int i=0;i<list.size();i++){
                	grade.append(list.get(i));
                	if(i!=(list.size()-1)){
						grade.append(", ");
					}
                }
        }
			
			
		} catch (Exception e) {
		}
		return grade.toString();
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
			gradeStr.append(Integer.parseInt(gradeList[0]));
			/*gradeStr.append(Integer.parseInt("0"));*/
		}
		return gradeStr.toString();
	}

	/*
	 * private String generateGradeIfHypen(String grade){ String gradeList[];
	 * StringBuilder gradeStr = new StringBuilder(); gradeList =
	 * grade.split("-"); if (gradeList.length>=2){ int start =1; try{ start =
	 * Integer.parseInt(gradeList[0]); }catch(Exception e){ e.printStackTrace();
	 * } int end = Integer.parseInt(gradeList[1]); if ( start < end){ for (int i
	 * = start; i<=end; i++){ if (i==end){ gradeStr.append(i); }else{
	 * gradeStr.append(i).append(","); } } } }else{
	 * gradeStr.append(Integer.parseInt(gradeList[0])); } return
	 * gradeStr.toString(); }
	 */
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
	
	

	/**
	 * Set resource that used in any collection
	 * 
	 * @param childResources
	 *            used resource
	 */
	public void setUsedInResources(List<C> childResources) {
		resourceSearchRightsFieldVc = new MoreInfoFieldVc();
		if (childResources != null) {
			for (C resource : childResources) {
				renderUsedInResource(resource);
			}
		}
	}

	abstract public void renderUsedInResource(C childResource);

	abstract public void requestUsedInResources();

	public SimpleAsyncCallback<SearchDo<C>> getUsedInResourcesAsyncCallback() {
		return usedInResourcesAsyncCallback;
	}

	public void setUsedInResourcesAsyncCallback(
			SimpleAsyncCallback<SearchDo<C>> usedInResourcesAsyncCallback) {
		this.usedInResourcesAsyncCallback = usedInResourcesAsyncCallback;
	}

	/**
	 * Add instance of {@link MoreInfoFieldVc} to metaInfoPanel widget
	 * 
	 * @param moreInfoField
	 *            instance of {@link MoreInfoFieldVc}
	 */
	public void addMoreInfoField(MoreInfoFieldVc moreInfoField) {
	//	metaInfoFloPanel.add(moreInfoField);
	}

	/**
	 * Set resource count
	 * 
	 * @param text
	 *            count of the resource
	 */
	public void setResourceCount(String text) {
		countLbl.setText(text);
		countLbl.getElement().setAttribute("alt",text);
		countLbl.getElement().setAttribute("title",text);
	}

	/**
	 * Set resource count text
	 * 
	 * @param text
	 *            count text
	 */
	public void setResourceCountTxt(String text) {
		countLblTxt.setText(text);
		countLblTxt.getElement().setAttribute("alt",text);
		countLblTxt.getElement().setAttribute("title",text);
	}

	/**
	 * @return messageInfo instance of {@link ErrorMessagePanel}
	 */
	public ErrorMessagePanel getMessageInfo() {
		return messageInfo;
	}

	public AppMirageDragContainer getUsedInResourcesPanel() {
		return usedInResourcesPanel;
	}

/*	*//**
	 * @return rightsFiled instance of {@link MoreInfoFieldVc}
	 *//*
	public MoreInfoFieldVc getRightsField() {
		return rightsFieldVc;
	}

	*//**
	 * @return likesFiled instance of {@link MoreInfoFieldVc}
	 *//*
	public MoreInfoFieldVc getLikesField() {
		return likesFieldVc;
	}*/

	
	public void setNotFriendly(String text){
		lblNotFriendly.setText(text);
		lblNotFriendly.getElement().setAttribute("alt",text);
		lblNotFriendly.getElement().setAttribute("title",text);
	}
	public HashMap<String, SimpleResourceVc> getSimpleResourceMap() {
		return simpleResourceMap;
	}

	public void setSimpleResourceMap(HashMap<String, SimpleResourceVc> simpleResourceMap) {
		this.simpleResourceMap = simpleResourceMap;
	}

	public void showNotFriendly(boolean visibility){
		lblNotFriendly.setVisible(visibility);
		imgQuestionImage.setVisible(visibility);
	}
}
