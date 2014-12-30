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

package org.ednovo.gooru.client.mvp.play.resource.body;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.rating.RatingWidgetView;
import org.ednovo.gooru.client.mvp.rating.events.OpenReviewPopUpEvent;
import org.ednovo.gooru.client.mvp.rating.events.PostUserReviewEvent;
import org.ednovo.gooru.client.mvp.rating.events.PostUserReviewResourceEvent;
import org.ednovo.gooru.client.mvp.rating.events.UpdateFlagIconColorEvent;
import org.ednovo.gooru.client.util.SetStyleForProfanity;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.content.ContentReportDo;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

/**
* @fileName : StarRatingsUc.java
*
* @description : Creates Thank you tool-tip once user rated. 
* 
* @version : 1.0
*
* @date:  April, 2013.
*
* @Author: Gooru Team.
* 
* @Reviewer: Gooru Team.
*/
public class ThankYouResourceStarRatingsPoor extends PopupPanel{
	
	private static ThankYouResourceStarRatingsUiBinder uiBinder = GWT.create(ThankYouResourceStarRatingsUiBinder.class);

	interface ThankYouResourceStarRatingsUiBinder extends UiBinder<Widget, ThankYouResourceStarRatingsPoor> {}
	
	private MessageProperties i18n = GWT.create(MessageProperties.class);
	
	@UiField Button btnSkip,btnPost;
	@UiField TextArea ratingCommentTxtArea;
	@UiField HTMLPanel buttonsContainer;
	@UiField Label saveAndPsotLbl,mandatoryDescLblForSwareWords,reviewTextAreaTitle,poorRatingHeaderLbl,poorRatingSubHeaderLbl,errorLbl;
	
	@UiField Label incorporateresourceText, unavailableresourceText,inaccurateTextresource,otherReason;
	
	ArrayList<String> reourceContentReportList=new ArrayList<String>();
	
	@UiField
	CheckBox resourceCheckBox1, resourceCheckBox2, resourceCheckBox3,
			resourceCheckBox4;
	
	private RatingWidgetView ratingWidgetView=null;
	
	String assocGooruOId,review,createrName;
	Integer score,count;
	double average;
	final String saving="Saving..";
	final String posting="Posting..";
	private int totalLength=0;
	
	private boolean isFlagged=false;
	
	/**
	 * Class Constructor
	 * @param assocGooruOId 
	 * @param score 
	 * @param review 
	 * @param average 
	 * @param count 
	 */
	public ThankYouResourceStarRatingsPoor(String assocGooruOId, Integer score, String review, double average, Integer count,String createrName){   
		this.assocGooruOId = assocGooruOId;
		this.score = score;
		this.review = review;
		this.average=average;
		this.count=count;
		this.createrName = createrName;
		setWidget(uiBinder.createAndBindUi(this));
		
		ratingCommentTxtArea.getElement().setAttribute("maxlength", "500");
		incorporateresourceText.setText(i18n.GL0612());
		incorporateresourceText.getElement().setId("lblIncorporateresourceText");
		incorporateresourceText.getElement().setAttribute("alt",i18n.GL0612());
		incorporateresourceText.getElement().setAttribute("title",i18n.GL0612());
		
		unavailableresourceText.setText(i18n.GL0613());
		unavailableresourceText.getElement().setId("lblUnavailableresourceText");
		unavailableresourceText.getElement().setAttribute("alt",i18n.GL0613());
		unavailableresourceText.getElement().setAttribute("title",i18n.GL0613());
		
		inaccurateTextresource.setText(i18n.GL0614());
		inaccurateTextresource.getElement().setId("lblInaccurateTextresource");
		inaccurateTextresource.getElement().setAttribute("alt",i18n.GL0614());
		inaccurateTextresource.getElement().setAttribute("title",i18n.GL0614());
		
		otherReason.setText(i18n.GL0606());
		otherReason.getElement().setId("lblOtherReason");
		otherReason.getElement().setAttribute("alt",i18n.GL0606());
		otherReason.getElement().setAttribute("title",i18n.GL0606());
		
		poorRatingHeaderLbl.setText(i18n.GL1854());
		poorRatingHeaderLbl.getElement().setId("lblPoorRatingHeader");
		poorRatingHeaderLbl.getElement().setAttribute("alt",i18n.GL1854());
		poorRatingHeaderLbl.getElement().setAttribute("title",i18n.GL1854());
		
		poorRatingSubHeaderLbl.setText(i18n.GL2035());
		poorRatingSubHeaderLbl.getElement().setId("lblPoorRatingSubHeader");
		poorRatingSubHeaderLbl.getElement().setAttribute("alt",i18n.GL2035());
		poorRatingSubHeaderLbl.getElement().setAttribute("title",i18n.GL2035());
		
		setUserReview(review);
//		setAvgRatingWidget();
		setGlassEnabled(true);
		saveAndPsotLbl.setVisible(false);
		buttonsContainer.setVisible(true);
		/*ratingText.getElement().setInnerHTML(i18n.GL1991());
		ratingText.getElement().setId("pnlRatingText");
		ratingText.getElement().setAttribute("alt",i18n.GL1991());
		ratingText.getElement().setAttribute("title",i18n.GL1991());*/
		
		btnSkip.setText(i18n.GL0142());
		btnSkip.getElement().setId("btnSkip");
		btnSkip.getElement().setAttribute("alt",i18n.GL1004());
		btnSkip.getElement().setAttribute("title",i18n.GL1004());
	
		reviewTextAreaTitle.getElement().setId("lblReviewTextAreaTitle");
		resourceCheckBox4.getElement().setId("chkResourceCheckBox4");
		resourceCheckBox3.getElement().setId("chkResourceCheckBox3");
		resourceCheckBox2.getElement().setId("chkResourceCheckBox2");
		resourceCheckBox1.getElement().setId("chkResourceCheckBox1");
		ratingCommentTxtArea.getElement().setId("tatRatingCommentTxtArea");
		StringUtil.setAttributes(ratingCommentTxtArea, true);
		mandatoryDescLblForSwareWords.getElement().setId("lblMandatoryDescLblForSwareWords");
		buttonsContainer.getElement().setId("pnlButtonsContainer");
		btnSkip.getElement().setId("btnSkip");
		btnPost.getElement().setId("btnPost");
		saveAndPsotLbl.getElement().setId("lblSaveAndPsotLbl");
	}
	
	/**
	 * Average star ratings widget will get integrated.
	 */
	private void setAvgRatingWidget() {
		ratingWidgetView=new RatingWidgetView();
		ratingWidgetView.getRatingCountOpenBrace().setText(i18n. GL_SPL_OPEN_SMALL_BRACKET());
		ratingWidgetView.getRatingCountLabel().setText(count.toString());
		ratingWidgetView.getRatingCountCloseBrace().setText(i18n. GL_SPL_CLOSE_SMALL_BRACKET());
		ratingWidgetView.setAvgStarRating(average);
		ratingWidgetView.getRatingCountLabel().addClickHandler(new ShowRatingPopupEvent());
//		ratingWidgetPanel.add(ratingWidgetView);
	}
	
	/**
	 * 
	 * Inner class implementing {@link ClickEvent}
	 *
	 */
	private class ShowRatingPopupEvent implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			/**
			 * OnClick of count label event to invoke Review pop-pup
			 */
			AppClientFactory.fireEvent(new OpenReviewPopUpEvent(assocGooruOId, "",createrName)); 
		}
	}

	/**
	 * On click Submit button user entered review will get posted.
	 * @param clickEvent {@link ClickEvent}
	 */
	@UiHandler("btnPost")
	public void onRatingReviewPostclick(ClickEvent clickEvent){
		if(totalLength!=500){
			if(ratingCommentTxtArea.getText().trim().equals("")&& !btnPost.getText().equalsIgnoreCase("Save")){ 
				if((resourceCheckBox1.getValue()||resourceCheckBox2.getValue()||resourceCheckBox3.getValue()||resourceCheckBox4.getValue())){
					setUserPoorReview();
				}else{
					btnPost.setEnabled(false);
					btnPost.getElement().addClassName("disabled");
				}
			}else if(!ratingCommentTxtArea.getText().trim().equals("") && !btnPost.getText().equalsIgnoreCase("Save")){
				setUserPoorReview();
			}else if(btnPost.getText().equalsIgnoreCase("Save")){
				setUserPoorReview();
			}
		}
	}
	
	
	private void setUserPoorReview() {
		btnPost.setEnabled(true);
		btnPost.getElement().removeClassName("disabled");
		Map<String, String> parms = new HashMap<String, String>();
		parms.put("text", ratingCommentTxtArea.getText());
		AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean value) {
					if(!value){
						saveAndPsotLbl.setText("");
						saveAndPsotLbl.getElement().setAttribute("alt","");
						saveAndPsotLbl.getElement().setAttribute("title","");
						saveAndPsotLbl.setVisible(true);
						buttonsContainer.setVisible(false);
						if(btnPost.getText().equalsIgnoreCase("Save")){
							saveAndPsotLbl.setText(saving);
							saveAndPsotLbl.getElement().setAttribute("alt",saving);
							saveAndPsotLbl.getElement().setAttribute("title",saving);
						 
							if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.COLLECTION_PLAY)){
								AppClientFactory.fireEvent(new PostUserReviewEvent(assocGooruOId,ratingCommentTxtArea.getText().trim(),score,true)); 
							}else{
								AppClientFactory.fireEvent(new PostUserReviewResourceEvent(assocGooruOId,ratingCommentTxtArea.getText().trim(),score,true));
							}
						}else if(btnPost.getText().equalsIgnoreCase("Submit")){
							saveAndPsotLbl.setText(posting);
							saveAndPsotLbl.getElement().setAttribute("alt",posting);
							saveAndPsotLbl.getElement().setAttribute("title",posting);
							if(resourceCheckBox1.isChecked())
							{
								isFlagged = true;
								reourceContentReportList.add("missing-concept");
							}
							if(resourceCheckBox2.isChecked())
							{
								isFlagged = true;
								reourceContentReportList.add("not-loading");
							}
							if(resourceCheckBox3.isChecked())
							{
								isFlagged = true;
								reourceContentReportList.add("inappropriate");
							}
							if(resourceCheckBox4.isChecked())
							{
								isFlagged = true;
								reourceContentReportList.add("other");
							}
							
							AppClientFactory.getInjector().getPlayerAppService().createContentReport(assocGooruOId, ratingCommentTxtArea.getText().trim(), reourceContentReportList, "", new SimpleAsyncCallback<ContentReportDo>() {
								@Override
								public void onSuccess(ContentReportDo result) {
									//getView().showSuccesmessagePopup();
									isResourceflagged();
									
									if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.COLLECTION_PLAY)){
										AppClientFactory.fireEvent(new PostUserReviewEvent(assocGooruOId,ratingCommentTxtArea.getText().trim(),score,false));
									}else{
										AppClientFactory.fireEvent(new PostUserReviewResourceEvent(assocGooruOId,ratingCommentTxtArea.getText().trim(),score,false));
									}
								}
							});	
						}
					}
					SetStyleForProfanity.SetStyleForProfanityForTextArea(ratingCommentTxtArea, mandatoryDescLblForSwareWords, value);
			}
		});
		
	}

	private void isResourceflagged() {
		if(isFlagged){
			isFlagged=false;
			AppClientFactory.fireEvent(new UpdateFlagIconColorEvent()); 
			
		}
	}
	
	
	/**
	 * On click Skip button user user can skip by giving review and thank you tool tip will close.
	 * @param clickEvent {@link ClickEvent}
	 */
	@UiHandler("btnSkip")
	public void onRatingReviewSkipclicked(ClickEvent clickEvent){
		hide();
	}
	
	@UiHandler("resourceCheckBox1")
	public void onClickOfresourceCheckBox1(ClickEvent event)
	{
	if(resourceCheckBox1.isChecked()||resourceCheckBox2.isChecked()||resourceCheckBox3.isChecked()||resourceCheckBox4.isChecked())
		{
			btnPost.setEnabled(true);
			btnPost.getElement().removeClassName("disabled");
		}
		else
		{
			if(!btnPost.getText().equalsIgnoreCase("Save")){
				isFlagged = false;
				btnPost.setEnabled(false);
				btnPost.getElement().addClassName("disabled");
			}
		}
		
	}
	
	@UiHandler("resourceCheckBox2")
	public void onClickOfresourceCheckBox2(ClickEvent event)
	{
	if(resourceCheckBox1.isChecked()||resourceCheckBox2.isChecked()||resourceCheckBox3.isChecked()||resourceCheckBox4.isChecked())
		{
			btnPost.setEnabled(true);
			btnPost.getElement().removeClassName("disabled");
		}
		else
		{
			if(!btnPost.getText().equalsIgnoreCase("Save")){
				isFlagged = false;
				btnPost.setEnabled(false);
				btnPost.getElement().addClassName("disabled");
			}
		}
		
	}
	
	@UiHandler("resourceCheckBox3")
	public void onClickOfresourceCheckBox3(ClickEvent event)
	{
	if(resourceCheckBox1.isChecked()||resourceCheckBox2.isChecked()||resourceCheckBox3.isChecked()||resourceCheckBox4.isChecked())
		{
			btnPost.setEnabled(true);
			btnPost.getElement().removeClassName("disabled");
		}
		else
		{
			if(!btnPost.getText().equalsIgnoreCase("Save")){
				isFlagged = false;
				btnPost.setEnabled(false);
				btnPost.getElement().addClassName("disabled");
			}
		}
		
	}
	
	@UiHandler("resourceCheckBox4")
	public void onClickOfresourceCheckBox4(ClickEvent event)
	{
	if(resourceCheckBox1.isChecked()||resourceCheckBox2.isChecked()||resourceCheckBox3.isChecked()||resourceCheckBox4.isChecked())
		{
			btnPost.setEnabled(true);
			btnPost.getElement().removeClassName("disabled");
		}
		else
		{
			if(!btnPost.getText().equalsIgnoreCase("Save")){
				isFlagged = false;
				btnPost.setEnabled(false);
				btnPost.getElement().addClassName("disabled");
			}
		}
		
	}
	
	@UiHandler("ratingCommentTxtArea")
	public void keyRatingTextArea(KeyUpEvent event){
		String review=ratingCommentTxtArea.getText().trim();
		totalLength=ratingCommentTxtArea.getText().trim().length();
		errorLbl.setText("");
		errorLbl.getElement().setAttribute("alt","");
		errorLbl.getElement().setAttribute("title","");
		if(ratingCommentTxtArea.getText().length()>0){
			btnPost.setEnabled(true);
			btnPost.getElement().removeClassName("disabled");
			errorLbl.setText("");
			errorLbl.getElement().setAttribute("alt","");
			errorLbl.getElement().setAttribute("title","");
		}else{
			if(btnPost.getText().equalsIgnoreCase("Save")){
				btnPost.setEnabled(true);
				btnPost.getElement().removeClassName("disabled");
			}else{
				btnPost.setEnabled(false);
				btnPost.getElement().addClassName("disabled");
			}
			
		}
		if(review.length()==500){
			errorLbl.setText(i18n.GL0143());
			errorLbl.getElement().setAttribute("alt",i18n.GL0143());
			errorLbl.getElement().setAttribute("title",i18n.GL0143());
			errorLbl.setVisible(true);
			
					
		}else{
			errorLbl.setVisible(false);
				
		}
	}
	
	
	/**
	 * Sets the user review on text area if available.
	 * @param review
	 */
	private void setUserReview(String review) {
		if(!review.equals("")){
			reviewTextAreaTitle.setText(i18n.GL1858());
			reviewTextAreaTitle.getElement().setAttribute("alt",i18n.GL1858());
			reviewTextAreaTitle.getElement().setAttribute("title",i18n.GL1858());
			btnPost.setText("Save");
			btnPost.getElement().setAttribute("alt","Save");
			btnPost.getElement().setAttribute("title","Save");
			ratingCommentTxtArea.setText(review.trim());
			ratingCommentTxtArea.getElement().setAttribute("alt",review.trim());
			ratingCommentTxtArea.getElement().setAttribute("title",review.trim());
		}else{
			btnPost.setEnabled(false);
			btnPost.getElement().addClassName("disabled");
			reviewTextAreaTitle.setText(i18n.GL2036());
			reviewTextAreaTitle.getElement().setAttribute("alt",i18n.GL1855());
			reviewTextAreaTitle.getElement().setAttribute("title",i18n.GL1855());
			btnPost.setText(i18n.GL0486());
			btnPost.getElement().setAttribute("alt","Submit");
			btnPost.getElement().setAttribute("title","Submit");
		}
	}
	
	@Override
	public void hide(boolean autoClose) {
		super.hide(true);
		
		if(autoClose && ratingCommentTxtArea.getText().trim().equals("")&& !btnPost.getText().equalsIgnoreCase("Save")){ 
			if((resourceCheckBox1.getValue()||resourceCheckBox2.getValue()||resourceCheckBox3.getValue()||resourceCheckBox4.getValue())){
				setUserPoorReview();
			}else{
				btnPost.setEnabled(false);
				btnPost.getElement().addClassName("disabled");
			}
		}else if(autoClose && !ratingCommentTxtArea.getText().trim().equals("") && !btnPost.getText().equalsIgnoreCase("Save")){
			setUserPoorReview();
		}
		
		/*if(autoClose && !ratingCommentTxtArea.getText().equals("") && !btnPost.getText().equalsIgnoreCase("Save")){
			Map<String, String> parms = new HashMap<String, String>();
			parms.put("text", ratingCommentTxtArea.getText());
			AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {
				@Override
				public void onSuccess(Boolean value) {
						if(!value){
							saveAndPsotLbl.setText("");
							saveAndPsotLbl.setVisible(true);
							buttonsContainer.setVisible(false);
							if(btnPost.getText().equalsIgnoreCase("Save")){
								saveAndPsotLbl.setText(saving);
								saveAndPsotLbl.getElement().setAttribute("alt",saving);
								saveAndPsotLbl.getElement().setAttribute("title",saving);
								AppClientFactory.fireEvent(new PostUserReviewEvent(assocGooruOId,ratingCommentTxtArea.getText().trim(),score,true));  
							}else if(btnPost.getText().equalsIgnoreCase("Submit")){
								saveAndPsotLbl.setText(posting);
								saveAndPsotLbl.getElement().setAttribute("alt",posting);
								saveAndPsotLbl.getElement().setAttribute("title",posting);
								if(resourceCheckBox1.isChecked())
								{
									isFlagged=true;
									reourceContentReportList.add("missing-concept");
								}
								if(resourceCheckBox2.isChecked())
								{
									isFlagged=true;
									reourceContentReportList.add("not-loading");
								}
								if(resourceCheckBox3.isChecked())
								{
									isFlagged=true;
									reourceContentReportList.add("inappropriate");
								}
								if(resourceCheckBox4.isChecked())
								{
									isFlagged=true;
									reourceContentReportList.add("other");
								}
								
								AppClientFactory.getInjector().getPlayerAppService().createContentReport(assocGooruOId, ratingCommentTxtArea.getText().trim(), reourceContentReportList, "", new SimpleAsyncCallback<ContentReportDo>() {
									@Override
									public void onSuccess(ContentReportDo result) {
										isResourceflagged();
										AppClientFactory.fireEvent(new PostUserReviewEvent(assocGooruOId,ratingCommentTxtArea.getText().trim(),score,false));
									}
								});	  
							}
						}
						SetStyleForProfanity.SetStyleForProfanityForTextArea(ratingCommentTxtArea, mandatoryDescLblForSwareWords, value);
				}
			});
		}*/
	}
}
