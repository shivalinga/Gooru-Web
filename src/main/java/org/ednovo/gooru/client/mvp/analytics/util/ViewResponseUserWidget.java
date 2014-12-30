package org.ednovo.gooru.client.mvp.analytics.util;

import java.util.Iterator;
import java.util.Set;

import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.shared.model.analytics.FeedBackResponseDataDO;
import org.ednovo.gooru.shared.model.analytics.OetextDataDO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ViewResponseUserWidget extends Composite {

	private static ViewResponseUserWidgetUiBinder uiBinder = GWT
			.create(ViewResponseUserWidgetUiBinder.class);

	interface ViewResponseUserWidgetUiBinder extends
			UiBinder<Widget, ViewResponseUserWidget> {
	}
	@UiField Label questionCountlbl,usernamelbl,userResponselbl,editedText,createOn;
	@UiField HTMLPanel giveFeedBackpnl,editFeedBackpnl,userAnswerspnl;
	@UiField TextBox feedBacktxt;
	@UiField InlineLabel spnEdit,spnDelete;
	@UiField Button btnSubmit;
	@UiField Image userProfileImage,userProfileImage1;
	
	OetextDataDO oetextDataDO;
	
	
	/**
	 * Constructor
	 * @param oetextDataDO
	 * @param resourceGooruId
	 * @param collectionId
	 * @param classpageId
	 * @param pathwayId
	 * @param questionType
	 * @param isSummary
	 */
	public ViewResponseUserWidget(OetextDataDO oetextDataDO,String resourceGooruId,String collectionId, String classpageId,String pathwayId,String questionType,boolean isSummary,String session) {
		initWidget(uiBinder.createAndBindUi(this));
		setData(oetextDataDO,resourceGooruId,collectionId,classpageId,pathwayId,questionType,isSummary,session);
	 	feedBacktxt.getElement().setAttribute("placeholder", "Leave feedback for this answer");
		questionCountlbl.setVisible(false);
	}
	/**
	 * Constructor
	 * @param questionCount
	 * @param questionText
	 * @param questionAnswers
	 */
	public ViewResponseUserWidget(String questionCount,String questionText,String questionAnswers) {
		initWidget(uiBinder.createAndBindUi(this));
		questionCountlbl.setVisible(true);
		questionCountlbl.setText("Question "+questionCount);
		usernamelbl.setText(questionText);
		userResponselbl.setText(questionAnswers);
		giveFeedBackpnl.setVisible(false);
		editFeedBackpnl.setVisible(false);
		spnEdit.setVisible(false);
		spnDelete.setVisible(false);
	}
	class OnErrorProfileImage implements ErrorHandler{
		@Override
		public void onError(ErrorEvent event) {
			userProfileImage.setUrl("../images/settings/setting-user-image.png");
			userProfileImage1.setUrl("../images/settings/setting-user-image.png");
		}
	}
	/**
	 * This method is used to set the data.
	 * @param oetextDataDO
	 * @param resourceGooruId
	 * @param collectionId
	 * @param classpageId
	 * @param pathwayId
	 * @param questionType
	 * @param isSummary
	 */
	void setData(final OetextDataDO oetextDataDO,final String resourceGooruId,final String collectionId, final String classpageId,final String pathwayId,String questionType,boolean isSummary,final String session){
		this.oetextDataDO=oetextDataDO;
		giveFeedBackpnl.setVisible(false);
		editFeedBackpnl.setVisible(false);
		userAnswerspnl.setVisible(false);
		userResponselbl.setVisible(false);
		if(isSummary){
			spnEdit.setVisible(true);
			spnDelete.setVisible(true);
		}else{
			spnEdit.setVisible(false);
			spnDelete.setVisible(false);
		}
	
		if(AppClientFactory.getLoggedInUser().getProfileImageUrl()!=null){
			userProfileImage.setUrl(AppClientFactory.getLoggedInUser().getProfileImageUrl());
			userProfileImage1.setUrl(AppClientFactory.getLoggedInUser().getProfileImageUrl());
		}else{
			userProfileImage.setUrl("../images/settings/setting-user-image.png");
			userProfileImage1.setUrl("../images/settings/setting-user-image.png");
		}
		userProfileImage.addErrorHandler(new OnErrorProfileImage());
		userProfileImage1.addErrorHandler(new OnErrorProfileImage());
		
		usernamelbl.setText(oetextDataDO.getUserName());
		String oeText=oetextDataDO.getOEText();
		if(questionType.equalsIgnoreCase("MA") || questionType.equalsIgnoreCase("FIB")){
				userAnswerspnl.setVisible(true);
				if(oetextDataDO.getAnswerObject()!=null) {
	   			 JSONValue value = JSONParser.parseStrict(oetextDataDO.getAnswerObject());
	   			 JSONObject answerObject = value.isObject();
	   			 Set<String> keys=answerObject.keySet();
	   			 Iterator<String> itr = keys.iterator();
	   		      while(itr.hasNext()) {
	   		         JSONArray attemptsObj=(JSONArray) answerObject.get(itr.next().toString());
	   		         for(int j=0;j<attemptsObj.size();j++){
	   		        	Label answerChoice=new Label();
	   		            boolean skip = attemptsObj.get(j).isObject().get("skip").isBoolean().booleanValue();
	   		        	String status =attemptsObj.get(j).isObject().get("status").isString().stringValue();
	   		        	String matext =attemptsObj.get(j).isObject().get("text").isString().stringValue();
	   		        	String colorCode="",text="";
						if(status.equalsIgnoreCase("1")) {
							colorCode = "#4E9746";
						} else {
							colorCode = "#FF0000";
						}
						if(questionType.equalsIgnoreCase("MA")) {
							if(matext.equalsIgnoreCase("1")) {
							    text = "Yes";
							} else {
							    text = "No";
							}
					    }else if(questionType.equalsIgnoreCase("FIB")) {
							text=text+" "+matext;
					    }
	        		    if(skip == false)
						{
	        		    	answerChoice.setText(text+",");	
	        		    	answerChoice.getElement().getStyle().setColor(colorCode);
	        		    	answerChoice.getElement().getStyle().setFloat(Float.LEFT);
	        		    	answerChoice.getElement().getStyle().setPaddingRight(1, Unit.PX);
	        		    	answerChoice.getElement().getStyle().setFontWeight(FontWeight.BOLD);
	        		    	answerChoice.getElement().getStyle().setFontSize(14, Unit.PX);
	        		    	userAnswerspnl.add(answerChoice);
						}else{
							answerChoice.setText("No data found");	
							userAnswerspnl.add(answerChoice);
							break;
						}
	   		         }
	   		      }
	   		}else{
	   			Label answerChoice=new Label();
	   			answerChoice.setText("No data found");	
				userAnswerspnl.add(answerChoice);
	   		}
		}else{
			userResponselbl.setVisible(true);
			String feedBackStatus=oetextDataDO.getFeedbackStatus();
			if(oeText==null || oeText.trim().isEmpty()){
				userResponselbl.setText("The Student is not provided any responses..");
			}else{
				userResponselbl.setText(oetextDataDO.getOEText());
			}
			if((isSummary && feedBackStatus!=null && feedBackStatus.equalsIgnoreCase("false")) && (oeText!=null && !oeText.trim().isEmpty())){
				giveFeedBackpnl.setVisible(true);
			}
			if((feedBackStatus!=null && feedBackStatus.equalsIgnoreCase("true")) && (oeText!=null && !oeText.trim().isEmpty())){
				editFeedBackpnl.setVisible(true);
				editedText.setText(oetextDataDO.getFeedbackText());
				feedBacktxt.setText(oetextDataDO.getFeedbackText());
				createOn.setText(AnalyticsUtil.getCreatedTime(Long.toString(oetextDataDO.getFeedbackTimestamp())));
			}
			final String classCode=Document.get().getElementById("txtClassCode")!=null?Document.get().getElementById("txtClassCode").getInnerText():"";
			btnSubmit.addClickHandler(new  ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					AppClientFactory.getInjector().getAnalyticsService().postTeacherFeedBackToStudent(feedBacktxt.getText(), resourceGooruId, collectionId, classpageId, pathwayId, oetextDataDO.getGooruUId(),session,"","",classCode, new AsyncCallback<FeedBackResponseDataDO>() {
						@Override
						public void onSuccess(FeedBackResponseDataDO result) {
							if(result!=null){
								giveFeedBackpnl.setVisible(false);
								editFeedBackpnl.setVisible(true);
								editedText.setText(result.getFreeText());
								feedBacktxt.setText(result.getFreeText());
								createOn.setText(AnalyticsUtil.getCreatedTime(Long.toString(result.getCreatedOn())));
							}
						}
						@Override
						public void onFailure(Throwable caught) {
						}
					});
				}
			});
			spnDelete.addClickHandler(new ClickHandler() {
								@Override
								public void onClick(ClickEvent event) {
									AppClientFactory.getInjector().getAnalyticsService().postTeacherFeedBackToStudent("", resourceGooruId, collectionId, classpageId, pathwayId, oetextDataDO.getGooruUId(), session,"commentsDelete","",classCode, new AsyncCallback<FeedBackResponseDataDO>() {
										@Override
										public void onSuccess(FeedBackResponseDataDO result) {
											if(result!=null){
												giveFeedBackpnl.setVisible(true);
											editFeedBackpnl.setVisible(false);
												editedText.setText(result.getFreeText());
												feedBacktxt.setText(result.getFreeText());
												createOn.setText(AnalyticsUtil.getCreatedTime(Long.toString(result.getCreatedOn())));
											}
										}
										@Override
										public void onFailure(Throwable caught) {
										}
									});
								}
			});
			spnEdit.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					giveFeedBackpnl.setVisible(true);
					editFeedBackpnl.setVisible(false);
				}
			});
		}
	}
}
