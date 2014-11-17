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
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.dnd.IsDraggable;
import org.ednovo.gooru.client.mvp.dnd.IsDraggableMirage;
import org.ednovo.gooru.client.mvp.resource.dnd.ResourceDragWithImgUc;
import org.ednovo.gooru.client.uc.CollectionImageUc;
import org.ednovo.gooru.client.uc.SeparatorUc;
import org.ednovo.gooru.client.uc.UserProfileUc;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.search.CollectionSearchResultDo;
import org.ednovo.gooru.shared.model.user.ProfileDo;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Search Team
 *
 */
public class SimpleCollectionVc extends Composite implements IsDraggable {

	private static SimpleCollectionVcUiBinder uiBinder = GWT.create(SimpleCollectionVcUiBinder.class);
	
	private static MessageProperties i18n = GWT.create(MessageProperties.class);

	interface SimpleCollectionVcUiBinder extends UiBinder<Widget, SimpleCollectionVc> {
	}

	@UiField
	HTML collectionTitleLbl;

	@UiField
	Label creatorNameLbl,creatorNameLblValue,gradesLblValue;

	@UiField
	Label resourceCountLbl, questionCountLbl;

	@UiField
	CollectionImageUc collectionImageUc;

	@UiField
	FlowPanel metaDataFloPanel,internalPanel1, standardsDataPanel,collectionTitlePanel,collectionGradePanel;
	
	@UiField
	HTMLPanel containerPanel;
	
	
	//@UiField SearchMoreInfoVcCBundle res;

	private static final String ALL_GRADES = i18n.GL1467().toUpperCase();
	
	private CollectionSearchResultDo collectionSearchResultDo;
	
	private static final String VIEWS= " "+i18n.GL0934();
	
	private static final String RESOURCES = " "+i18n.GL0174().toLowerCase();
	
	private static final String RESOURCE = " "+i18n.GL1110().toLowerCase();
	
	private static final String QUESTIONS = " "+i18n.GL1042().toLowerCase();
	
	private static final String QUESTION = " "+i18n.GL0308().toLowerCase();
	
	private static final String USER_META_ACTIVE_FLAG = "0";
	/**
	 * Class constructor, call collection result setData method 
	 * @param collectionSearchResultDo instance of {@link CollectionSearchResultDo}
	 */
	public SimpleCollectionVc(CollectionSearchResultDo collectionSearchResultDo) {
		initWidget(uiBinder.createAndBindUi(this));
		setData(collectionSearchResultDo);
		SearchMoreInfoVcCBundle.INSTANCE.css().ensureInjected();
		if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)){
			internalPanel1.setStyleName(SearchMoreInfoVcCBundle.INSTANCE.css().resourceCollectionThumbnailPanel());
		//	collectionTitlePanel.setStyleName(SearchMoreInfoVcCBundle.INSTANCE.css().resourceCollectionTitlePanel());
			collectionTitleLbl.getElement().getStyle().setColor("#1076BB");
			/*internalPanel1.getElement().getStyle().setPaddingTop(10, Unit.PX); 
			internalPanel1.getElement().getStyle().setPaddingBottom(10, Unit.PX);
			internalPanel1.getElement().getStyle().setBorderStyle(BorderStyle.SOLID); 
			internalPanel1.getElement().getStyle().setBorderWidth(1, Unit.PX);
			internalPanel1.getElement().getStyle().setBottom(1, Unit.PX);
			internalPanel1.getElement().getStyle().setBorderColor("#515151");
			internalPanel1.getElement().getStyle().setWidth(497, Unit.PX);*/
		}
		internalPanel1.getElement().setId("fpnlInternalPanel1");
		collectionTitlePanel.getElement().setId("fpnlCollectionTitlePanel");
		containerPanel.getElement().setId("pnlContainerPanel");
		metaDataFloPanel.getElement().setId("fpnlMetaDataFloPanel");
		standardsDataPanel.getElement().setId("fpnlStandardsDataPanel");
		resourceCountLbl.getElement().setId("lblResourceCountLbl");
		questionCountLbl.getElement().setId("lblQuestionCountLbl");
		collectionGradePanel.getElement().setId("fpnlCollectionGradePanel");
		gradesLblValue.getElement().setId("lblGradesLblValue");
	}

	/**
	 * Set collection meta data info such as title, image, resource count, etc.. 
	 * @param collectionSearchResultDo instance of {@link CollectionSearchResultDo}
	 */ 
	public void setData(final CollectionSearchResultDo collectionSearchResultDo) {
		this.collectionSearchResultDo = collectionSearchResultDo;
		//collectionTitleLbl.setText(StringUtil.truncateText(collectionSearchResultDo.getResourceTitle(), 30));
		collectionTitleLbl.setHTML(StringUtil.truncateText(collectionSearchResultDo.getResourceTitle(), 30));
		collectionTitleLbl.getElement().setId("htmlCollectionTitleLbl");
		collectionTitleLbl.getElement().setAttribute("alt",StringUtil.truncateText(collectionSearchResultDo.getResourceTitle(), 30));
		collectionTitleLbl.getElement().setAttribute("title",StringUtil.truncateText(collectionSearchResultDo.getResourceTitle(), 30));
		
		creatorNameLbl.setText(i18n.GL0622());
		creatorNameLbl.getElement().setId("lblCreatorNameLbl");
		creatorNameLbl.getElement().setAttribute("alt",i18n.GL0622());
		creatorNameLbl.getElement().setAttribute("title",i18n.GL0622());
		
		creatorNameLblValue.setText(collectionSearchResultDo.getOwner().getUsername());
		creatorNameLblValue.getElement().setId("lblCreatorNameLblValue");
		creatorNameLblValue.getElement().setAttribute("alt",collectionSearchResultDo.getOwner().getUsername());
		creatorNameLblValue.getElement().setAttribute("title",collectionSearchResultDo.getOwner().getUsername());
		
		if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)){
			String grade =collectionSearchResultDo.getGrade();
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
				
				/*if(gradeListInt.size()!=12){
					if (isKindergarten) {
						finalGradeStringB.append("Kindergarten, ");
					}
				}*/
				
		
				for (String eachGrade1 : gradeList) {
					if (!eachGrade1.equalsIgnoreCase("Kindergarten") && !eachGrade1.equalsIgnoreCase("Higher Education")) {
						eachGrade1 = eachGrade1.replaceAll("th", "").replaceAll("TH", "").replaceAll("st", "").replaceAll("ST", "").replaceAll("nd", "").replaceAll("ND", "").replaceAll("rd", "").replaceAll("RD", "");
						eachGrade1 = eachGrade1.toLowerCase().replaceAll("Grade", "").replaceAll("grade", "");
						eachGrade1 = eachGrade1.toLowerCase().replaceAll("K-", "").replaceAll("k-", "");
						eachGrade1 = eachGrade1.toLowerCase().replaceAll("K", "").replaceAll("k", "");
						try 
						{
							String grad[] = generateGradeIfHypen(eachGrade1).trim().split(",");
							for (int i = 0; i < grad.length; i++) {
								gradeListInt.add(Integer.parseInt(grad[i]));
							}
						} 
						catch (Exception e) 
						{
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
				
			
				gradesLblValue.setText(finalGradeStringB.toString());
				gradesLblValue.getElement().setAttribute("alt",finalGradeStringB.toString());
				gradesLblValue.getElement().setAttribute("title",finalGradeStringB.toString());
				collectionGradePanel.add(gradesLblValue);
			}
			else {
				gradesLblValue.setText(null);
				gradesLblValue.getElement().setAttribute("alt",null);
				gradesLblValue.getElement().setAttribute("title",null);
			}
		}
		
		if ((collectionSearchResultDo.getOwner().isProfileUserVisibility())){
			if(StringUtil.isPartnerUser(collectionSearchResultDo.getOwner().getUsername())) {
				creatorNameLblValue.getElement().getStyle().setColor("#1076bb");
				creatorNameLblValue.getElement().getStyle().setCursor(Cursor.POINTER);
				creatorNameLblValue.getElement().getStyle().setFloat(Float.LEFT);

				creatorNameLblValue.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						MixpanelUtil.Click_Resource_Username();
						AppClientFactory.getPlaceManager().revealPlace(collectionSearchResultDo.getOwner().getUsername());
					}
				});

				creatorNameLblValue.addMouseOverHandler(new MouseOverHandler() {

					@Override
					public void onMouseOver(MouseOverEvent event) {
						AppClientFactory.getInjector().getUserService().getUserProfileV2Details(collectionSearchResultDo.getOwner().getGooruUId(), USER_META_ACTIVE_FLAG, new SimpleAsyncCallback<ProfileDo>(){

							@Override
							public void onSuccess(ProfileDo result) {
								String username=result.getUser().getUsernameDisplay();
								String aboutMe=result.getAboutMe();
								UserProfileUc userProfieVc = new UserProfileUc(username,aboutMe, result.getUser().getProfileImageUrl());
								containerPanel.add(userProfieVc);
							}

						});
					}
				});

				creatorNameLblValue.addMouseOutHandler(new MouseOutHandler() {

					@Override
					public void onMouseOut(MouseOutEvent event) {
						containerPanel.clear();
					}
				});
			}else{
				creatorNameLblValue.getElement().getStyle().setColor("#1076bb");
				creatorNameLblValue.getElement().getStyle().setCursor(Cursor.POINTER);
				creatorNameLblValue.getElement().getStyle().setFloat(Float.LEFT);

				creatorNameLblValue.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						MixpanelUtil.Click_Resource_Username();
						Map<String, String> params = new HashMap<String, String>();
						params.put("id", collectionSearchResultDo.getOwner().getGooruUId());
						params.put("user", collectionSearchResultDo.getOwner().getUsername());
						AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.PROFILE_PAGE,params);
					}
				});
			}

		}
		SearchUiUtil.renderMetaData(metaDataFloPanel, collectionSearchResultDo.getCourseNames(), 30);
		SearchUiUtil.renderMetaData(metaDataFloPanel, collectionSearchResultDo.getTotalViews() + "", VIEWS);
		
		int resourceCount = collectionSearchResultDo.getOnlyResourceCount();
		int questionCount = collectionSearchResultDo.getQuestionCount();
		
		if (resourceCount>1){
			resourceCountLbl.setText(resourceCount + RESOURCES);
			resourceCountLbl.getElement().setAttribute("alt",resourceCount + RESOURCES);
			resourceCountLbl.getElement().setAttribute("title",resourceCount + RESOURCES);
		} else if(resourceCount==0&&questionCount==0){
			resourceCountLbl.setText(resourceCount + RESOURCE);
			resourceCountLbl.getElement().setAttribute("alt",resourceCount + RESOURCE);
			resourceCountLbl.getElement().setAttribute("title",resourceCount + RESOURCE);
		}
		
		if (questionCount>1){
			questionCountLbl.setText(questionCount + QUESTIONS);
			questionCountLbl.getElement().setAttribute("alt",questionCount + QUESTIONS);
			questionCountLbl.getElement().setAttribute("title",questionCount + QUESTIONS);
		} else if (questionCount==1) {
			questionCountLbl.setText(questionCount + QUESTION);
			questionCountLbl.getElement().setAttribute("alt",questionCount + QUESTION);
			questionCountLbl.getElement().setAttribute("title",questionCount + QUESTION);
		}
		
		if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)){
			if(!resourceCountLbl.getText().isEmpty()) {
				metaDataFloPanel.add(new SeparatorUc());
				metaDataFloPanel.add(resourceCountLbl);
			}
			if(!questionCountLbl.getText().isEmpty()) {
				metaDataFloPanel.add(new SeparatorUc());
				metaDataFloPanel.add(questionCountLbl);
			}
		}
		SearchUiUtil.renderStandards(standardsDataPanel, collectionSearchResultDo);
		collectionImageUc.setUrl(collectionSearchResultDo.getUrl(), collectionSearchResultDo.getResourceTitle());
		collectionImageUc.setGooruOid(collectionSearchResultDo.getGooruOid());
	}
	
	
	private static String generateGradeIfHypen(String grade) {
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
		} 
		else {
			gradeStr.append(Integer.parseInt(gradeList[0]));
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
	
	private  String formatGrades(List<Integer> list) {

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


		} 
		catch (Exception e) {
		}
		return grade.toString();
	}

	
	
	
	
	
	
	
	

	@Override
	public String getDragId() {
		return collectionSearchResultDo.getGooruOid();
	}

	@Override
	public DRAG_TYPE getDragType() {
		return DRAG_TYPE.COLLECTION;
	}

	@Override
	public IsDraggableMirage initDraggableMirage() {
		return new ResourceDragWithImgUc(DRAG_TYPE.COLLECTION.getName(), collectionSearchResultDo.getResourceTitle());
	}
	
	@Override
	public void onDragBlur() {
	}

	@Override
	public Widget getDragHandle() {
		return null;
	}

	@Override
	public int getDragTopCorrection() {
		return 7;
	}

	@Override
	public int getDragLeftCorrection() {
		return 11;
	}
}
