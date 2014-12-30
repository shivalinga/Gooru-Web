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
package org.ednovo.gooru.client.mvp.home;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.client.CssTokens;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.search.IsSearchView;
import org.ednovo.gooru.client.uc.DisclosurePanelUc;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.shared.i18n.MessageProperties;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @description : This class used for to show the Pre-filter seach Popup
 * @version :1.2
 * @author janamitra
 * @date: Dec 1 2014
 * @Reviewer Gooru Team
 */
public class PreFilterPopup extends PopupPanel {

	private static PreFilterPopupUiBinder uiBinder = GWT
			.create(PreFilterPopupUiBinder.class);
	
	private static MessageProperties i18n = GWT.create(MessageProperties.class);

	interface PreFilterPopupUiBinder extends UiBinder<Widget, PreFilterPopup> {
	}
	
	
	@UiField HTMLPanel filterPanel,eleGradePanelUc,middleGradePanelUc,highrGradePanelUc,subjectPanelUc,gradesPanel;

	
	@UiField Label lblGradesSubj,lblStandards;
	
	@UiField HTMLPanel standardsPanel;
	
//	@UiField Anchor gradeAnc,standardsButton;
	
//	@UiField SimplePanel standardsPanel;
	
	CheckBox gradeCheckBox;

	
	
	private static final String COMMA_SEPARATOR = i18n.GL_GRR_COMMA();
	
	String[] elementaryGrades = new String[]{i18n.GL3070(),i18n.GL3071(),i18n.GL3072(),i18n.GL3073(),i18n.GL3074()};
	String[] middleGrades = new String[]{i18n.GL3075(),i18n.GL3076(),i18n.GL3077(),i18n.GL3078(),i18n.GL3079()};
	String[] higherGrades = new String[]{i18n.GL3080(),i18n.GL3081(),i18n.GL3082(),i18n.GL3083(),i18n.GL3084()};
	String[] subjects = new String[]{i18n.GL1000().trim(),i18n.GL1001().trim(),i18n.GL1002().trim(),i18n.GL1003().trim(),i18n.GL3085().trim(),i18n.GL3086().trim()};

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
	public PreFilterPopup() {
		setWidget(uiBinder.createAndBindUi(this));
//		show();
		renderCheckBoxs(eleGradePanelUc, elementaryGrades);
		renderCheckBoxs(middleGradePanelUc, middleGrades);
		renderCheckBoxs(highrGradePanelUc, higherGrades);
		renderCheckBoxs(subjectPanelUc, subjects);
		setStaticData();
		eventActions();
		setPreSelectedFilters(AppClientFactory.getCurrentPlaceToken());
		
		/*gradeAnc.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
			hideGradePanel().setVisible(true);
			ShowSTandardsPanel().setVisible(false);
				
			}
		});*/
		/*standardsButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
			
				hideGradePanel().setVisible(false);
				ShowSTandardsPanel().setVisible(true);
			}
		});*/
//		clearAllFields();
	}

	/**
	 * If any filters are checked, this method will be called and will respective check box will get checked.
	 * @param currentPlaceToken
	 */
	private void setPreSelectedFilters(String currentPlaceToken) {
		String gradeVal=AppClientFactory.getPlaceManager().getRequestParameter("flt.grade",null);
		String subjectVal=AppClientFactory.getPlaceManager().getRequestParameter("flt.subjectName",null);
		if(gradeVal!=null){
			setSelectedFilter(eleGradePanelUc, gradeVal, ",");
			setSelectedFilter(middleGradePanelUc, gradeVal, ",");
			setSelectedFilter(highrGradePanelUc, gradeVal, ",");
		}
		if(subjectVal!=null){
			setSelectedFilter(subjectPanelUc, subjectVal, " ~~");
		}
	}

	/**
	 * To perform Actions with events.
	 */
	private void eventActions() {
		// TODO Auto-generated method stub
		lblGradesSubj.addClickHandler(new GradeandSubjectFilter());
		lblStandards.addClickHandler(new StandardsFilter());
		lblGradesSubj.getElement().setAttribute("style", "background: #e5e5e5;");
		standardsPanel.setVisible(false);
	}

	/**
	 * To set the static data to the fields
	 */
	private void setStaticData() {
		// TODO Auto-generated method stub
		lblGradesSubj.setText(i18n.GL3045());
		lblStandards.setText(i18n.GL0575());
	}

	/**
	 * To set the Names and Values for Checkbox's
	 * @param htmlPanel {@link HTMLPanel} 
	 * @param stringArray {@link String}
	 */
	private void renderCheckBoxs(HTMLPanel htmlPanel, String[] stringArray) {
		// TODO Auto-generated method stub
		for(int i=0;i<stringArray.length;i++){
			gradeCheckBox= new CheckBox();
			gradeCheckBox.setName(stringArray[i]);
			
			if(stringArray.equals(higherGrades)){
				if(stringArray[i].equals(i18n.GL3084())){
					gradeCheckBox.setName("12gte");
				}
				/*if(stringArray[i].equals(i18n.GL3084())){
					gradeCheckBox.setName("13gte");
				}*/
			}
			gradeCheckBox.setText(stringArray[i].trim());
			gradeCheckBox.setStyleName(CssTokens.FILTER_CHECKBOX);
			htmlPanel.add(gradeCheckBox);
			gradeCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					if (gradeCheckBox.getValue()){
							MixpanelUtil.MOS_Filter("Selected");
					}else{
							MixpanelUtil.MOS_Filter("Unselected");
					}
//					AppClientFactory.fireEvent(new GetSearchKeyWordEvent());
				}
			});
			
		}
	}
	
	
	/**
	 * Get search filter such as grade, subject etc..
	 * @return search filter as Map value
	 */
	protected Map<String, String> getFilter() {
		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.clear();
		String filterGrade = "";
		String elemGrade="";
		
		String eleGrade = getSelectedFilter(eleGradePanelUc);
		if (!eleGrade.isEmpty()) {
			filterGrade+=eleGrade;
		}
		String midGrade = getSelectedFilter(middleGradePanelUc);
		if (!midGrade.isEmpty()) {
			if(filterGrade.equals("")){
				if(elemGrade.isEmpty()){
					filterGrade+=midGrade;
				}
				
			}else {
				filterGrade+=","+midGrade;
				
			}
		}
		String highGrade = getSelectedFilter(highrGradePanelUc);
		if (!highGrade.isEmpty()) {
			if(filterGrade.equals("")){
				filterGrade+=highGrade;
			}else{
				filterGrade+=","+highGrade;
			}
		}
		if(filterGrade!=null && !filterGrade.equals("")){
			filterMap.put(IsSearchView.GRADE_FLT, filterGrade);
		}
		String selectedSubject = getSelectedFilter(subjectPanelUc, "~~");
		if (!selectedSubject.isEmpty()) {
			filterMap.put(IsSearchView.SUBJECT_FLT, selectedSubject);
		}
		
		/*String standardSgsts = getSuggestions(standardsPanel);
		if (!standardSgsts.isEmpty()) {
			filterMap.put(IsSearchView.STANDARD_FLT, standardSgsts);
		}*/
		
		return filterMap;
		
	}
	
	/**
	 * Clear all selected filter values
	 * @param gradePanelUc instance {@link DisclosurePanelUc} which has selected filter values
	 */
	public void clearFilter(HTMLPanel gradePanelUc) {
		
	//	if(resourceSearch){
		for(int i=0;i<gradePanelUc.getWidgetCount();i++){
			Widget filterWidget = gradePanelUc.getWidget(i);
			if (filterWidget instanceof CheckBox) {
				((CheckBox) filterWidget).setValue(false);
			}
		}
	}
	
	/**
	 * @param categoryPanelUc2 instance of {@link DisclosurePanelUc}
	 * @return selected filterHTMLPanelUc name
	 */
	private String getSelectedFilter(HTMLPanel categoryPanelUc2) {
		return getSelectedFilter(categoryPanelUc2, COMMA_SEPARATOR);
	}
	
	/**
	 * Get filters for search
	 * @param categoryPanelUc2 instance of {@link DisclosurePanelUc} which has filters widget
	 * @param separator concatenation of the filters with separator
	 * @return concatenation of selected filters
	 */
	private String getSelectedFilter(HTMLPanel categoryPanelUc2, String separator) {
		String selectedFilter = "";
		for(int i =0;i<categoryPanelUc2.getWidgetCount();i++){
			Widget filterWidget = categoryPanelUc2.getWidget(i);
			if (filterWidget instanceof CheckBox) {
				CheckBox filterCheckBox = (CheckBox) filterWidget;
				if (filterCheckBox != null && filterCheckBox.getValue()) {
					if (!selectedFilter.isEmpty()) {
						selectedFilter += separator;
					}
					selectedFilter += filterCheckBox.getName();
					MixpanelUtil.mixpanelEvent("search_"+selectedFilter+"_filter_selected");
				}
			}
		}
		return selectedFilter;
	}
	/**
	 * Clear all panels values
	 */
	public void clearAllFields(){
		clearFilter(eleGradePanelUc);
		clearFilter(middleGradePanelUc);
		clearFilter(highrGradePanelUc);
		clearFilter(subjectPanelUc);
	}
	/**
	 * @return the lblStandards
	 */
	public Label getStandardsInfo(){
		return lblStandards;
	}
	/**
	 * @return the gradesPanel
	 */
	public HTMLPanel hideGradePanel(){
		return gradesPanel;
	}
	/**
	 * @return the standardsPanel
	 */
	public HTMLPanel ShowSTandardsPanel(){
		return standardsPanel;
	}
    /**
     * This class used to show Grades & Subjects filter when user click on Grade & Suject tab
     * @author seachTeam
     *
     */
	private class GradeandSubjectFilter implements ClickHandler{

		/* (non-Javadoc)
		 * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
		 */
		@Override
		public void onClick(ClickEvent event) {
			lblGradesSubj.getElement().setAttribute("style", "background: #e5e5e5;");
			filterPanel.getElement().setAttribute("style", "width:565px;");
			lblStandards.getElement().getStyle().clearBackgroundColor();
			standardsPanel.setVisible(false);
			gradesPanel.setVisible(true);
		}
		
	}
	
	/**
     * This class used to show Standards filter when user click on Grade tab
     * @author searchTeam
     *
     */
	private class StandardsFilter implements ClickHandler{

		/** (non-Javadoc)
		 * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
		 */
		@Override
		public void onClick(ClickEvent event) {
			lblStandards.getElement().setAttribute("style", "background: #e5e5e5;");
			filterPanel.getElement().setAttribute("style", "width:922px;");
			lblGradesSubj.getElement().getStyle().clearBackgroundColor();
			standardsPanel.setVisible(true);
			gradesPanel.setVisible(false);
//			standardsPanel.add(new AppPopUpStandards());
		}
		
	}
	
	/**
	 * To set the Default tab as grade & subject tab 
	 */
	public void hidePlanels(){
		lblGradesSubj.getElement().setAttribute("style", "background: #e5e5e5;");
		filterPanel.getElement().setAttribute("style", "width:565px;");
		lblStandards.getElement().getStyle().clearBackgroundColor();
		standardsPanel.setVisible(false);
		gradesPanel.setVisible(true);
	}
	
	/**
	 * Set search filters
	 */
	public void setFilter() {
		String grade = AppClientFactory.getPlaceManager().getRequestParameter(IsSearchView.GRADE_FLT);
		
		String subjects = AppClientFactory.getPlaceManager().getRequestParameter(IsSearchView.SUBJECT_FLT);
		
		String standards = AppClientFactory.getPlaceManager().getRequestParameter(IsSearchView.STANDARD_FLT);
		
		
		if(grade!=null){
			setSelectedFilter(gradesPanel,grade,COMMA_SEPARATOR);
		}
		if(subjects!=null){
			setSelectedFilter(subjectPanelUc,subjects,"~~");
		}
		if(standards!=null){
			setSelectedFilter(standardsPanel,standards,COMMA_SEPARATOR);
		}
	}
	/**
	 * Set filter value for search with separator
	 * @param filterFlowPanel instance of {@link DisclosurePanelUc} which has filter values
	 * @param checkedValues selected filter value
	 * @param separator concatenation of the filter value by separator 
	 */
	private void setSelectedFilter(HTMLPanel filterHtmlPanel, String checkedValues, String separator) {
		List<String> items = null;
		if (checkedValues != null) {
			items = Arrays.asList(checkedValues.split("\\s*" + separator + "\\s*"));
		}
		
		if (items != null) {
			//if(resourceSearch){
			for(int i=0;i<filterHtmlPanel.getWidgetCount();i++){
				Widget filterWidget = filterHtmlPanel.getWidget(i);
				if (filterWidget instanceof CheckBox) {
					CheckBox filterCheckBox = (CheckBox) filterWidget;
					filterCheckBox.setValue(false);
					for (String item : items) {
						if ((filterCheckBox.getName().equals(item))) {	
							filterCheckBox.setValue(true);
						}
					}
				}
			}
		}
	}

}
