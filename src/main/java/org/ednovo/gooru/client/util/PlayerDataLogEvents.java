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
package org.ednovo.gooru.client.util;

import java.util.ArrayList;
import java.util.List;

import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.play.collection.GwtUUIDGenerator;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;



public class PlayerDataLogEvents {
	
	public static final String COLLECTION_PLAY_EVENT_NAME="collection-play-dots";
	public static final String COLLECTION_RESOUCE_PLAY_EVENT_NAME="collection-resource-play-dots";
	public static final String COLLECTION_QUESTION_PLAY_EVENT_NAME="collection-question-resource-play-dots";
	public static final String COLLECTION_RESOURCE_HINT_EVENT_NAME="collection-resource-hint-play-dots";
	public static final String COLLECTION_RESOURCE_EXPLANATION_EVENT_NAME="collection-resource-explanation-play-dots";
	public static final String COLLECTION_RESOURCE_OE_EVENT_NAME="collection-resource-oe-play-dots";
	public static final String COLLECTION_RESOURCE_OE_HINT_EVENT_NAME="collection-resource-oe-hint-play-dots";
	public static final String COLLECTION_RESOURCE_OE_EXPLANATION_EVENT_NAME="collection-resource-oe-explanation-play-dots";
	public static final String COLLECTION_RESOURCE_OE_SUBMIT_EVENT_NAME="collection-resource-oe-save-dots";
	public static final String RESOUCE_PLAY_EVENT_NAME="resource-play-dots";
	public static final String QUESTION_RESOUCE_PLAY_EVENT_NAME="question-play-dots";
	public static final String QUESTION_RESOURCE_OE_EVENT_NAME="question-oe-play-dots";
	public static final String QUESTION_RESOURCE_HINT_EVENT_NAME="question-hint-play-dots";
	public static final String QUESTION_RESOURCE_EXPLANATION_EVENT_NAME="question-explanation-play-dots";
	public static final String QUESTION_OE_HINT_EVENT_NAME="question-oe-hint-play-dots";
	public static final String QUESTION_OE_EXPLANATION_EVENT_NAME="question-oe-explanation-play-dots";
	public static final String QUESTION_OE_SAVE_EVENT_NAME="question-oe-save-dots";
	public static final String OPEN_SESSION_STATUS="open";
	public static final String CLOSE_SESSION_STATUS="close";
	public static final String START_EVENT_TYPE="start";
	public static final String STOP_EVENT_TYPE="stop";
	
	// new events implementation text
	// Event Names 
	public static final String COLLECTION_PLAY="collection.play";
	public static final String COLLECTION_RESOURCE_PLAY="collection.resource.play";
	public static final String RESOURCE_PLAY="resource.play";
	public static final String CLASSPAGE_VIEW="classpage.view";
	public static final String REACTION_CREATE="reaction.create";
	public static final String REACTION_DELETE="reaction.delete";
	public static final String COLLECTION_RESOURCE_SAVE="collection.resource.save";
	public static final String RESOURCE_SAVE="resource.save";
	public static final String ITEM_LOAD="item.load";
	public static final String ITEM_SHARE="item.share";
	public static final String ITEM_FLAG="item.flag";
	public static final String LIBRARY_VIEW="library.view";
	public static final String COMMENT_CREATE="comment.create";
	public static final String COMMENT_EDIT="comment.edit";
	public static final String COMMENT_DELETE="comment.delete";
	public static final String ITEM_RATE="item.rate";
	public static final String ITEM_REVIEW="item.review";
	
	//event keys
	public static final String EVENTID="eventId";
	public static final String EVENTNAME="eventName";
	public static final String SESSION="session";
	public static final String APIKEY="apiKey";
	public static final String ORGANIZATIONUID="organizationUId";
	public static final String SESSIONTOKEN="sessionToken";
	public static final String USER="user";
	public static final String GOORUUID="gooruUId";
	public static final String STARTTIME="startTime";
	public static final String ENDTIME="endTime";
	public static final String CONTEXT="context";
	public static final String CONTENTGOORUID="contentGooruId";
	public static final String PARENTGOORUID="parentGooruId";
	public static final String PARENTEVENTID="parentEventId";
	public static final String TYPE="type";
	public static final String CLIENTSOURCE="clientSource";
	public static final String PATH="path";
	public static final String PAGELOCATION="pageLocation";
	public static final String MODE="mode";
	public static final String VERSION="version";
	public static final String LOGAPI="logApi";
	public static final String METRICS="metrics";
	public static final String TOTALTIMESPENTINMS="totalTimeSpentInMs";
	public static final String SCORE="score";
	public static final String RESOURCETYPE="resourceType";
	public static final String PAYLOADOBJECT="payLoadObject";
	public static final String QUESTIONTYPE="questionType";
	public static final String TOTALNOOFCHARACTER="totalNoOfCharacter";
	public static final String TEXT="text";
	public static final String ATTEMPTSTATUS="attemptStatus";
	public static final String ATTEMPTTRYSEQUENCE="attemptTrySequence";
	public static final String ANSWERS="answers";
	public static final String HINTS="hints";
	public static final String EXPLANATION="explanation";
	public static final String CODEID="codeId";
	public static final String REACTIONTYPE="reactionType";
	public static final String WEB="web";
	public static final String PREVIEW="preview";
	public static final String STUDY="study";
	public static final String SESSIONID="sessionId";
	public static final String ATTEMPTCOUNT="attemptCount";
	public static final String ANSWEROBJECT="answerObject";
	public static final String STATUS="status";
	public static final String ORDER="order";
	public static final String ANSWERID="answerId";
	public static final String TIMESTAMP="timeStamp";
	public static final String SKIP="skip";
	public static final String CONTENTITEMID="contentItemId";
	public static final String PARENTITEMID="parentItemId";
	public static final String ITEMTYPE="itemType";
	public static final String URL="url";
	public static final String SHARETYPE="shareType";
	public static final String CONFIRMSTATUS="confirmStatus";
	public static final String OPTIONTEXT="optionText";
	public static final String COLLECTION="collection";
	public static final String RESOURCE="resource";
	public static final String PROFILE="profile";
	public static final String COLLECTION_LOAD_URL="/collection/load";
	public static final String COLLECTION_SHARE_URL="/collection/share";
	public static final String PROFILE_SHARE_URL="/profile/share";
	public static final String COLLECTION_FLAG_URL="/collection/flag";
	public static final String FACEBOOK="facebook";
	public static final String TWITTER="twitter";
	public static final String MAIL="mail";
	public static final String SEARCHTERM="searchTerm";
	public static final String RATE="rate";
	public static final String PREVIOUS_RATE="previousRate";
	
	
	
	
	
	public static void collectionStartStopEvent(JSONObject collectionDataLogEventMap){
		triggerDataLogCall(collectionDataLogEventMap);
	}
	public static void collectionItemStartStopEvent(){
		
	}
	public static void resourceStartStopEvent(){
		
	}
	public static void saveOeAnswerEvent(){
		
	}
	public static void visitingClasspageEvent(){
		
	}
	
	public static JSONString getDataLogSessionObject(String sessionId){
		JSONObject eventJsonObject=new JSONObject();
		try{
			eventJsonObject.put(APIKEY, new JSONString(AppClientFactory.getLoggedInUser().getSettings().getApiKeyPoint()));
			eventJsonObject.put(ORGANIZATIONUID, new JSONString(""));
			eventJsonObject.put(SESSIONTOKEN, new JSONString(AppClientFactory.getLoggedInUser().getToken()));
			if(sessionId!=null){
				eventJsonObject.put(SESSIONID, new JSONString(sessionId));
			}
		}catch(Exception e){
			
		}
		return new JSONString(eventJsonObject.toString());
	}
	
	public static JSONString getDataLogUserObject(){
		JSONObject eventJsonObject=new JSONObject();
		try{
			eventJsonObject.put(GOORUUID, new JSONString(AppClientFactory.getLoggedInUser().getGooruUId()));
		}catch(Exception e){
			
		}
		return new JSONString(eventJsonObject.toString());
	}
	
	public static JSONString getDataLogContextObject(String collectionId,String parentGooruId,String parentEventId,String eventType,String mode,
							String resourceType,String reactionType,String path,String pageLocation){
		JSONObject contextMap=new JSONObject();
		try{
			contextMap.put(CONTENTGOORUID, new JSONString(collectionId));
			parentGooruId=parentGooruId==null?"":parentGooruId;
			if(parentGooruId!=null){
				contextMap.put(PARENTGOORUID, new JSONString(parentGooruId));
			}
			if(parentEventId!=null){
				contextMap.put(PARENTEVENTID, new JSONString(parentEventId));
			}
			contextMap.put(TYPE, new JSONString(eventType));
			contextMap.put(RESOURCETYPE, new JSONString(resourceType));
			contextMap.put(CLIENTSOURCE, new JSONString(WEB));
			if(reactionType!=null){
				contextMap.put(REACTIONTYPE, new JSONString(reactionType));
			}
			contextMap.put(PATH, new JSONString(path));
			if(pageLocation!=null){
				contextMap.put(PAGELOCATION, new JSONString(pageLocation)); 
			}else{
				contextMap.put(PAGELOCATION, new JSONString(AppClientFactory.getPlaceManager().getPageLocation())); 
			}
			contextMap.put(MODE, new JSONString(mode));
		}catch(Exception e){
			
		}
		return new JSONString(contextMap.toString());
	}
	
	
	public static JSONString getDataLogContextObjectForItemLoad(String collectionId,String contentItemId,String parentEventId,String parentGooruOid,String parentItemId,
			String mode,String path,String pageLocation,String url){
		JSONObject contextMap=new JSONObject();
		try{
			contextMap.put(CONTENTGOORUID, new JSONString(collectionId));
			contentItemId=contentItemId!=null?contentItemId:"";
			contextMap.put(CONTENTITEMID, new JSONString(contentItemId));
			if(parentGooruOid!=null){
				contextMap.put(PARENTGOORUID, new JSONString(parentGooruOid));
			}
			if(parentEventId!=null){
				contextMap.put(PARENTEVENTID, new JSONString(parentEventId));
			}
			contextMap.put(PARENTITEMID, new JSONString(parentItemId));
			contextMap.put(CLIENTSOURCE, new JSONString(WEB));
			contextMap.put(PATH, new JSONString(path));
			if(pageLocation!=null){
				contextMap.put(PAGELOCATION, new JSONString(pageLocation)); 
			}else{
				contextMap.put(PAGELOCATION, new JSONString(AppClientFactory.getPlaceManager().getPageLocation()));
			}
			if(mode!=null){
				contextMap.put(MODE, new JSONString(mode));
			}
			contextMap.put(URL, new JSONString(url));
		}catch(Exception e){
			
		}
		return new JSONString(contextMap.toString());
	}
	
	public static JSONString getLibraryDataLogContext(String libaryGooruOid,String pageLocation){
		JSONObject contextMap=new JSONObject();
		try{
			libaryGooruOid=libaryGooruOid!=null?libaryGooruOid:"";
			contextMap.put(CONTENTGOORUID, new JSONString(libaryGooruOid));
			contextMap.put(CLIENTSOURCE, new JSONString(WEB));
			contextMap.put(PATH, new JSONString(libaryGooruOid));
			contextMap.put(PAGELOCATION, new JSONString(pageLocation)); 
		}catch(Exception e){
			
		}
		return new JSONString(contextMap.toString());
	}
	
	public static  JSONString getDataLogVersionObject(){
		JSONObject versionMap=new JSONObject();
		try{
			versionMap.put(LOGAPI, new JSONString("0.1")); 
		}catch(Exception e){
			
		}
		return new JSONString(versionMap.toString());
	}
	public static  JSONString getDataLogMetricsObject(Long totalTimesInSec){
		JSONObject metricsMap=new JSONObject();
		try{
			metricsMap.put(TOTALTIMESPENTINMS, new JSONNumber(totalTimesInSec));
		}catch(Exception e){
			
		}
		return new JSONString(metricsMap.toString());
	}
	public static  JSONString getDataLogMetricsObject(Long totalTimesInSec,Integer score){
		JSONObject metricsMap=new JSONObject();
		try{
			metricsMap.put(TOTALTIMESPENTINMS, new JSONNumber(totalTimesInSec));
			metricsMap.put(SCORE,new JSONNumber(score));
		}catch(Exception e){
			
		}
		return new JSONString(metricsMap.toString());
	}
	
	public static  JSONString getDataLogPayLoadObject(String questionType,String oeAnswerText, List<Integer> attemptStatus, List<Integer> attemptTrySequence,
										JSONObject answerIdsObject, JSONObject hintIdsObject,JSONObject explanationIdsObject,Integer attemptCount,List<List<JSONObject>> answerObjectArray,String searchTerm){
		JSONObject payLoadMap=new JSONObject();
		try{
			payLoadMap.put(QUESTIONTYPE, new JSONString(questionType));
			payLoadMap.put(TOTALNOOFCHARACTER,new JSONNumber(oeAnswerText.length()));
			payLoadMap.put(TEXT,new JSONString(StringUtil.replaceSpecial(oeAnswerText)));
			payLoadMap.put(ATTEMPTSTATUS, new JSONString(createJsniIntArray(attemptStatus).toString()));
			payLoadMap.put(ATTEMPTTRYSEQUENCE, new JSONString(createJsniIntArray(attemptTrySequence).toString()));
			payLoadMap.put(ANSWERS, new JSONString(answerIdsObject.toString()));
			payLoadMap.put(ATTEMPTCOUNT,new JSONNumber(attemptCount));
			payLoadMap.put(HINTS, new JSONString(hintIdsObject.toString()));
			payLoadMap.put(EXPLANATION, new JSONString(explanationIdsObject.toString()));
			payLoadMap.put(ANSWEROBJECT, new JSONString(getAnswerObjectArrayInString(answerObjectArray)));
			if(searchTerm!=null){
				payLoadMap.put(SEARCHTERM, new JSONString(searchTerm));
			}
		}catch(Exception e){
			
		}
		String payLoad = payLoadMap.toString();
		payLoad = payLoad.replaceAll("\\\\\"\\[", "[").replaceAll("\\]\\\\\"", "]");
		return new JSONString(payLoad);
	}
	
	public static  JSONString getDataLogPayLoadObject(String reactionType){
		JSONObject payLoadMap=new JSONObject();
		try{
			//payLoadMap.put(REACTIONTYPE, new JSONString(reactionType));
		}catch(Exception e){
		
		}
		return new JSONString(payLoadMap.toString());
	}
	
	public static  JSONString getItemLoadDataLogPayLoadObject(String itemType){
		JSONObject payLoadMap=new JSONObject();
		try{
			payLoadMap.put(ITEMTYPE, new JSONString(itemType));
		}catch(Exception e){
		
		}
		return new JSONString(payLoadMap.toString());
	}
	public static  JSONString getItemShareDataLogPayLoadObject(String itemType,String shareType,boolean confirmStatus){
		JSONObject payLoadMap=new JSONObject();
		try{
			payLoadMap.put(ITEMTYPE, new JSONString(itemType));
			payLoadMap.put(SHARETYPE, new JSONString(shareType));
			payLoadMap.put(CONFIRMSTATUS, JSONBoolean.getInstance(confirmStatus));
		}catch(Exception e){
		
		}
		return new JSONString(payLoadMap.toString());
	}
	public static  JSONString getItemFlagDataLogPayLoadObject(String itemType,String flagText,ArrayList<String> contentReportList){
		JSONObject payLoadMap=new JSONObject();
		try{
			payLoadMap.put(ITEMTYPE, new JSONString(itemType));
			payLoadMap.put(TEXT, new JSONString(flagText));
			JSONObject flagList=new JSONObject();
			for(int i=0;i<contentReportList.size();i++){
				flagList.put("value"+(i+1), new JSONString(contentReportList.get(i)));
			}
			payLoadMap.put(OPTIONTEXT, flagList);
		}catch(Exception e){
		
		}
		return new JSONString(payLoadMap.toString());
	}
	
	public static String getAnswerObjectArrayInString(List<List<JSONObject>> answerObjectArray){
		JSONObject attemptedAnswersArray=new JSONObject();
		for(int i=0;i<answerObjectArray.size();i++){
			List<JSONObject> jsonArray=answerObjectArray.get(i);
			System.out.println("jsonArray+- : "+jsonArray+"");
			attemptedAnswersArray.put("attempt"+(i+1), new JSONString(jsonArray+""));
		}
		String tempArray = attemptedAnswersArray+"";
		tempArray = tempArray.replaceAll("\\\\", "");
		System.out.println("tempArray : "+tempArray);
		return tempArray;
	}
	
	public static JSONArray createJsniIntArray(List<Integer> attemptTrySequence){
		JSONArray attemptTrySequenceArray=new JSONArray();
		if(attemptTrySequence!=null&&attemptTrySequence.size()>0){
			for(int i=0;i<attemptTrySequence.size();i++){
				attemptTrySequenceArray.set(i, new JSONNumber(attemptTrySequence.get(i)));
			}
		}
		return attemptTrySequenceArray;	
	}
	public static JSONArray createJsniStringArray(List<String> attemptTrySequence){
		JSONArray attemptTrySequenceArray=new JSONArray();
		if(attemptTrySequence!=null&&attemptTrySequence.size()>0){
			for(int i=0;i<attemptTrySequence.size();i++){
				attemptTrySequenceArray.set(i, new JSONString(attemptTrySequence.get(i)));
			}
		}
		return attemptTrySequenceArray;	
	}
	public static native void triggerDataLogCall(JSONObject eventJsonObject) /*-{
		$wnd._et.data.push(eventJsonObject);
 	}-*/;
	
	public static void collectionPlayStartEvent(String eventId,String eventName,
			String sessionId,String sessionStaus,String contentGooruId,String type,Long startTime,
			Long endTime,Long timeSpentInMs, String sessionToken,String gooruUId) {
			JSONObject eventJsonObject=new JSONObject();
			eventJsonObject.put("eventId", new JSONString(eventId));
			eventJsonObject.put("eventName", new JSONString(eventName));
			eventJsonObject.put("sessionId", new JSONString(sessionId));
			eventJsonObject.put("sessionStatus", new JSONString(sessionStaus));
			eventJsonObject.put("contentGooruId", new JSONString(contentGooruId));
			eventJsonObject.put("type", new JSONString(type));
			eventJsonObject.put("startTime", new JSONNumber(startTime));
			eventJsonObject.put("endTime", new JSONNumber(endTime));
			eventJsonObject.put("timeSpentInMs", new JSONNumber(timeSpentInMs));
			eventJsonObject.put("gooruUId", new JSONString(gooruUId));
			eventJsonObject.put("sessionToken", new JSONString(sessionToken));
			//triggerDataLogCall(eventJsonObject);
 	}
	
	public static void resourcePlayStartStopEvent(String eventId,String eventName,String parentEventId,
			String contentGooruId,String parentGooruId,String type,Long startTime,
			Long endTime, Long timeSpentInMs,String sessionToken,String gooruUId,List<Integer> attemptTrySequence,
			List<Integer> attemptStatus,List<Integer> answerId,String openEndedText,int totalNoCharacters){
		JSONObject eventJsonObject=new JSONObject();
		eventJsonObject.put("eventId", new JSONString(eventId));
		eventJsonObject.put("eventName", new JSONString(eventName));
		eventJsonObject.put("parentEventId", new JSONString(parentEventId));
		eventJsonObject.put("contentGooruId", new JSONString(contentGooruId));
		eventJsonObject.put("parentGooruId", new JSONString(parentGooruId));
		eventJsonObject.put("type", new JSONString(type));
		eventJsonObject.put("startTime", new JSONNumber(startTime));
		eventJsonObject.put("endTime", new JSONNumber(endTime));
		eventJsonObject.put("timeSpentInMs", new JSONNumber(timeSpentInMs));
		eventJsonObject.put("sessionToken", new JSONString(sessionToken));
		eventJsonObject.put("gooruUId", new JSONString(gooruUId));
		eventJsonObject.put("attemptTrySequence", createJsniIntArray(attemptTrySequence));
		eventJsonObject.put("attemptStatus", createJsniIntArray(attemptStatus));
		eventJsonObject.put("answerId", createJsniIntArray(answerId));
		eventJsonObject.put("openEndedText", new JSONString(openEndedText));
		eventJsonObject.put("totalNoCharacters", new JSONNumber(totalNoCharacters));
		//triggerDataLogCall(eventJsonObject);
	}
	
	public static  void explanationButtonDataLogEvent(String eventId,String eventName,String parentEventId,String contentGooruId,String parentGooruId,String type,Long startTime,
			Long endTime,Long timeSpentInMs,String sessionToken,String gooruUId,boolean isExplanationUsed,List<Integer> attemptTrySequence,
			List<Integer> attemptStatus,List<Integer> answerId,String openEndedText,int totalNoCharacters,int hintId) {
		JSONObject eventJsonObject=new JSONObject();
		eventJsonObject.put("eventId", new JSONString(eventId));
		eventJsonObject.put("eventName", new JSONString(eventName));
		eventJsonObject.put("parentEventId", new JSONString(parentEventId));
		eventJsonObject.put("contentGooruId", new JSONString(contentGooruId));
		eventJsonObject.put("parentGooruId", new JSONString(parentGooruId));
		eventJsonObject.put("type", new JSONString(type));
		eventJsonObject.put("startTime", new JSONNumber(startTime));
		eventJsonObject.put("endTime", new JSONNumber(endTime));
		eventJsonObject.put("timeSpentInMs", new JSONNumber(timeSpentInMs));
		eventJsonObject.put("sessionToken", new JSONString(sessionToken));
		eventJsonObject.put("gooruUId", new JSONString(gooruUId));
		eventJsonObject.put("isExplanationUsed", JSONBoolean.getInstance(isExplanationUsed));
		eventJsonObject.put("attemptTrySequence", createJsniIntArray(attemptTrySequence));
		eventJsonObject.put("attemptStatus", createJsniIntArray(attemptStatus));
		eventJsonObject.put("answerId", createJsniIntArray(answerId));
		eventJsonObject.put("totalNoCharacters", new JSONNumber(totalNoCharacters));
		eventJsonObject.put("openEndedText", new JSONString(openEndedText));
		eventJsonObject.put("hintId", new JSONNumber(hintId));
		//triggerDataLogCall(eventJsonObject);
 	}
	
	public static  void hintsButtonDataLogEvent(String eventId,String eventName,String parentEventId,
			String contentGooruId,String parentGooruId,String type,Long startTime,
			Long endTime,Long timeSpentInMs,String sessionToken,String gooruUId,int attemptTrySequence,int hintId,List<Integer> answerAttemptTrySequence,
			List<Integer> attemptStatus,List<Integer> answerId,String openEndedText,int totalNoCharacters,boolean isExplanationUsed) {
		JSONObject eventJsonObject=new JSONObject();
		eventJsonObject.put("eventId", new JSONString(eventId));
		eventJsonObject.put("eventName", new JSONString(eventName));
		eventJsonObject.put("parentEventId", new JSONString(parentEventId));
		eventJsonObject.put("contentGooruId", new JSONString(contentGooruId));
		eventJsonObject.put("parentGooruId", new JSONString(parentGooruId));
		eventJsonObject.put("type", new JSONString(type));
		eventJsonObject.put("startTime", new JSONNumber(startTime));
		eventJsonObject.put("endTime", new JSONNumber(endTime));
		eventJsonObject.put("timeSpentInMs", new JSONNumber(timeSpentInMs));
		eventJsonObject.put("sessionToken", new JSONString(sessionToken));
		eventJsonObject.put("gooruUId", new JSONString(gooruUId));
		//eventJsonObject.put("attemptTrySequence", new JSONNumber(attemptTrySequence));
		eventJsonObject.put("isExplanationUsed", JSONBoolean.getInstance(isExplanationUsed));
		eventJsonObject.put("hintId", new JSONNumber(hintId));
		eventJsonObject.put("attemptTrySequence", createJsniIntArray(answerAttemptTrySequence));
		eventJsonObject.put("attemptStatus", createJsniIntArray(attemptStatus));
		eventJsonObject.put("answerId", createJsniIntArray(answerId));
		eventJsonObject.put("totalNoCharacters", new JSONNumber(totalNoCharacters));
		eventJsonObject.put("openEndedText", new JSONString(openEndedText));
		//triggerDataLogCall(eventJsonObject);
 	}
	
 	public static void submitOeAnswerDataLogEvent(String eventId,String eventName,String parentEventId,
			String contentGooruId,Long startTime,
			Long endTime, Long timeSpentInMs,String sessionToken,String gooruUId,List<Integer> attemptTrySequence,
			List<Integer> attemptStatus,List<Integer> answerId,String openEndedText,int totalNoCharacters,String parentGooruOid){
		JSONObject eventJsonObject=new JSONObject();
		eventJsonObject.put("eventId", new JSONString(eventId));
		eventJsonObject.put("eventName", new JSONString(eventName));
		eventJsonObject.put("parentEventId", new JSONString(parentEventId));
		eventJsonObject.put("contentGooruId", new JSONString(contentGooruId));
		eventJsonObject.put("startTime", new JSONNumber(startTime));
		eventJsonObject.put("endTime", new JSONNumber(endTime));
		eventJsonObject.put("timeSpentInMs", new JSONNumber(timeSpentInMs));
		eventJsonObject.put("sessionToken", new JSONString(sessionToken));
		eventJsonObject.put("gooruUId", new JSONString(gooruUId));
/*		eventJsonObject.put("attemptTrySequence", createJsniIntArray(attemptTrySequence));
		eventJsonObject.put("attemptStatus", createJsniIntArray(attemptStatus));
		eventJsonObject.put("answerId", createJsniIntArray(answerId));*/
		eventJsonObject.put("openEndedText", new JSONString(openEndedText));
		eventJsonObject.put("totalNoCharacters", new JSONNumber(totalNoCharacters));
		eventJsonObject.put("parentGooruId", new JSONString(parentGooruOid));
		//triggerDataLogCall(eventJsonObject);
	}
 	public static String getQuestionType(int questionType){
		switch (questionType) {
			case 1:
				return "MC";
			case 3:
				return "TF";
			case 4:
				return "FIB";
			case 6:
				return "OE";
			case 7:
				return "MA";
			default:
				return "RES";
		}
	}
	
	public static JSONString getClassPagePayLoadObject(String codeId){
		JSONObject payLoadMap=new JSONObject();
		try{
			payLoadMap.put(CODEID, new JSONString(codeId));
		}catch(Exception e){
			
		}
		return new JSONString(payLoadMap.toString());
	}
	
	public static void triggerLibraryShareDataEvent( String collectionId, String libraryGooruOid){
		libraryGooruOid=libraryGooruOid!=null?libraryGooruOid:"";
		String path=libraryGooruOid+"/"+collectionId;
		String parentEventId=AppClientFactory.getPlaceManager().getLibaryEventId();
		triggerItemShareDataLogEvent(collectionId,"",parentEventId,libraryGooruOid,"","","collection","gooru",false,null,path,"library");
	}
	
	public static void triggerProfileCollectionShareDataEvent(String collectionId){
		String path=collectionId;
		triggerItemShareDataLogEvent(collectionId,"","","","","","collection","gooru",false,null,path,"profile");
		
	}

	public static void triggerItemShareDataLogEvent(String itemGooruOid, String itemContentItemId,String parentEventId,String parentGooruOid,String parentContItemId,String sessionId,
			String itemType,String shareType,boolean confirmStatus,String playerMode,String path,String pageLocation){
		JSONObject collectionDataLog=new JSONObject(); 
		Long startTime=PlayerDataLogEvents.getUnixTime();
		collectionDataLog.put(PlayerDataLogEvents.EVENTID, new JSONString(GwtUUIDGenerator.uuid()));
		collectionDataLog.put(PlayerDataLogEvents.EVENTNAME, new JSONString(PlayerDataLogEvents.ITEM_SHARE));
		collectionDataLog.put(PlayerDataLogEvents.SESSION, PlayerDataLogEvents.getDataLogSessionObject(sessionId));
		collectionDataLog.put(PlayerDataLogEvents.STARTTIME, new JSONNumber(startTime));
		collectionDataLog.put(PlayerDataLogEvents.ENDTIME, new JSONNumber(startTime));
		collectionDataLog.put(PlayerDataLogEvents.METRICS,PlayerDataLogEvents.getDataLogMetricsObject(startTime-startTime));
		collectionDataLog.put(PlayerDataLogEvents.VERSION,PlayerDataLogEvents.getDataLogVersionObject());
		collectionDataLog.put(PlayerDataLogEvents.USER, PlayerDataLogEvents.getDataLogUserObject());
		collectionDataLog.put(PlayerDataLogEvents.PAYLOADOBJECT,PlayerDataLogEvents.getItemShareDataLogPayLoadObject(itemType,shareType,confirmStatus));
		collectionDataLog.put(PlayerDataLogEvents.CONTEXT,PlayerDataLogEvents.getDataLogContextObjectForItemLoad(itemGooruOid, itemContentItemId, parentEventId, parentGooruOid, parentContItemId, playerMode, path, pageLocation, PlayerDataLogEvents.COLLECTION_SHARE_URL));
		PlayerDataLogEvents.collectionStartStopEvent(collectionDataLog);
	}	
	
	public static void triggerItemShareDataLogEventForProfile(String itemGooruOid, String itemContentItemId,String parentGooruOid,String parentContItemId,String sessionId,
			String itemType,String shareType,boolean confirmStatus,String playerMode,String path,String pageLocation){
		JSONObject collectionDataLog=new JSONObject(); 
		Long startTime=PlayerDataLogEvents.getUnixTime();
		collectionDataLog.put(PlayerDataLogEvents.EVENTID, new JSONString(GwtUUIDGenerator.uuid()));
		collectionDataLog.put(PlayerDataLogEvents.EVENTNAME, new JSONString(PlayerDataLogEvents.ITEM_SHARE));
		collectionDataLog.put(PlayerDataLogEvents.SESSION, PlayerDataLogEvents.getDataLogSessionObject(sessionId));
		collectionDataLog.put(PlayerDataLogEvents.STARTTIME, new JSONNumber(startTime));
		collectionDataLog.put(PlayerDataLogEvents.ENDTIME, new JSONNumber(startTime));
		collectionDataLog.put(PlayerDataLogEvents.METRICS,PlayerDataLogEvents.getDataLogMetricsObject(startTime-startTime));
		collectionDataLog.put(PlayerDataLogEvents.VERSION,PlayerDataLogEvents.getDataLogVersionObject());
		collectionDataLog.put(PlayerDataLogEvents.USER, PlayerDataLogEvents.getDataLogUserObject());
		collectionDataLog.put(PlayerDataLogEvents.PAYLOADOBJECT,PlayerDataLogEvents.getItemShareDataLogPayLoadObject(itemType,shareType,confirmStatus));
		collectionDataLog.put(PlayerDataLogEvents.CONTEXT,PlayerDataLogEvents.getDataLogContextObjectForItemLoad(itemGooruOid, itemContentItemId, null, parentGooruOid, parentContItemId, playerMode, path, pageLocation, PlayerDataLogEvents.PROFILE_SHARE_URL));
		PlayerDataLogEvents.collectionStartStopEvent(collectionDataLog);
	}
	
	public static void triggerLibraryViewEvent(String libraryGooruOid){
		String viewToken=AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken();
		if(!AppClientFactory.getPlaceManager().isLibaryEventTriggered(viewToken)){
			String eventId=GwtUUIDGenerator.uuid();
			AppClientFactory.getPlaceManager().setLibaryEventTriggered(viewToken);
			AppClientFactory.getPlaceManager().setLibraryEventId(eventId);
			PlayerDataLogEvents.triggerLibarayViewEvent(libraryGooruOid, eventId, "library");
		}
	}
	
	public static void triggerLibarayViewEvent(String libararyGooruOid,String libraryEventId,String pageLocation){
		JSONObject libarayViewData=new JSONObject();
		Long startTime=PlayerDataLogEvents.getUnixTime();
		libarayViewData.put(PlayerDataLogEvents.EVENTID, new JSONString(libraryEventId));
		libarayViewData.put(PlayerDataLogEvents.EVENTNAME, new JSONString(PlayerDataLogEvents.LIBRARY_VIEW));
		libarayViewData.put(PlayerDataLogEvents.SESSION, PlayerDataLogEvents.getDataLogSessionObject(null));
		libarayViewData.put(PlayerDataLogEvents.STARTTIME, new JSONNumber(startTime));
		libarayViewData.put(PlayerDataLogEvents.ENDTIME, new JSONNumber(startTime));
		libarayViewData.put(PlayerDataLogEvents.METRICS,PlayerDataLogEvents.getDataLogMetricsObject(startTime-startTime));
		libarayViewData.put(PlayerDataLogEvents.VERSION,PlayerDataLogEvents.getDataLogVersionObject());
		libarayViewData.put(PlayerDataLogEvents.USER, PlayerDataLogEvents.getDataLogUserObject());
		libarayViewData.put(PlayerDataLogEvents.PAYLOADOBJECT,new JSONString(new JSONObject().toString()));
		libarayViewData.put(PlayerDataLogEvents.CONTEXT,PlayerDataLogEvents.getLibraryDataLogContext(libararyGooruOid, pageLocation));
		PlayerDataLogEvents.collectionStartStopEvent(libarayViewData);
	}
	
	/**
	 * This method is used to create data in JSON format and log <b>create comment, update comment and delete comment</b> data log events
	 * @param collectionId specifies unique id of the collection.
	 * @param commentGooruOid specifies unique id of the comment in case of comment update and delete.
	 * @param parentEventId  specifies event id of the library view event id or class view event id.
	 * @param parentGooruOid specifies unique id of the library gooruOid or class view event id.
	 * @param sessionId specifies unique id of the session created by session tracker API.
	 * @param path specifies the hierarchy of the collection.
	 * @param pageLocation specifies the page location of the event.
	 * @param commentText specifies the user given comment text.
	 * @param eventName specifies the name of the data log event.
	 */
	public static void triggerCommentDataLogEvent(String collectionId, String commentGooruOid,String parentEventId,String parentGooruOid,String sessionId,
			String path,String pageLocation,String commentText,String eventName){
		JSONObject collectionDataLog=new JSONObject(); 
		Long startTime=PlayerDataLogEvents.getUnixTime();
		collectionDataLog.put(PlayerDataLogEvents.EVENTID, new JSONString(GwtUUIDGenerator.uuid()));
		collectionDataLog.put(PlayerDataLogEvents.EVENTNAME, new JSONString(eventName));
		collectionDataLog.put(PlayerDataLogEvents.SESSION, PlayerDataLogEvents.getDataLogSessionObject(sessionId));
		collectionDataLog.put(PlayerDataLogEvents.STARTTIME, new JSONNumber(startTime));
		collectionDataLog.put(PlayerDataLogEvents.ENDTIME, new JSONNumber(startTime));
		collectionDataLog.put(PlayerDataLogEvents.METRICS,PlayerDataLogEvents.getDataLogMetricsObject(startTime-startTime));
		collectionDataLog.put(PlayerDataLogEvents.VERSION,PlayerDataLogEvents.getDataLogVersionObject());
		collectionDataLog.put(PlayerDataLogEvents.USER, PlayerDataLogEvents.getDataLogUserObject());
		collectionDataLog.put(PlayerDataLogEvents.PAYLOADOBJECT,PlayerDataLogEvents.getCommentDataLogPayLoadObject(commentText));
		collectionDataLog.put(PlayerDataLogEvents.CONTEXT,PlayerDataLogEvents.getCommentDataLogContextObject(collectionId, commentGooruOid, parentEventId, parentGooruOid, path, pageLocation));
		PlayerDataLogEvents.collectionStartStopEvent(collectionDataLog);
	}
	
	/**
	 *  This method is used to create data in JSON format and log user<b>star rating</b> data log events
	 * @param resourceId specifies unique id of the collection item.
	 * @param collectionId specifies unique id of the collection.
	 * @param parentEventId specifies event id of the collection play event id
	 * @param sessionId specifies unique id of the session created by session tracker API.
	 * @param path specifies the hierarchy of the collection item.
	 * @param pageLocation specifies the page location of the event.
	 * @param currentRate specifies user given star rating for collection item.
	 * @param previousRate specifies user previous given star rating for collection item.
	 */
	public static void triggerRatingDataLogEvent(String resourceId,String collectionId, String parentEventId,String sessionId,String path, String pageLocation,double currentRate,double previousRate){
		JSONObject collectionDataLog=new JSONObject(); 
		Long startTime=PlayerDataLogEvents.getUnixTime();
		collectionDataLog.put(PlayerDataLogEvents.EVENTID, new JSONString(GwtUUIDGenerator.uuid()));
		collectionDataLog.put(PlayerDataLogEvents.EVENTNAME, new JSONString(ITEM_RATE));
		collectionDataLog.put(PlayerDataLogEvents.SESSION, PlayerDataLogEvents.getDataLogSessionObject(sessionId));
		collectionDataLog.put(PlayerDataLogEvents.STARTTIME, new JSONNumber(startTime));
		collectionDataLog.put(PlayerDataLogEvents.ENDTIME, new JSONNumber(startTime));
		collectionDataLog.put(PlayerDataLogEvents.METRICS,PlayerDataLogEvents.getDataLogMetricsObject(startTime-startTime));
		collectionDataLog.put(PlayerDataLogEvents.VERSION,PlayerDataLogEvents.getDataLogVersionObject());
		collectionDataLog.put(PlayerDataLogEvents.USER, PlayerDataLogEvents.getDataLogUserObject());
		collectionDataLog.put(PlayerDataLogEvents.PAYLOADOBJECT,PlayerDataLogEvents.getRatingtDataLogPayLoadObject(currentRate,previousRate));
		collectionDataLog.put(PlayerDataLogEvents.CONTEXT,PlayerDataLogEvents.getCommentDataLogContextObject(resourceId,null,parentEventId, collectionId, path, pageLocation));
		PlayerDataLogEvents.collectionStartStopEvent(collectionDataLog);
	}
	
	/**
	 * This method is used to frame data in JSON format and log user <b>review</b> text data log event.  
	 * @param resourceId specifies unique id of the collection item.
	 * @param collectionId specifies unique id of the collection.
	 * @param parentEventId specifies event id of the collection play event id
	 * @param sessionId specifies unique id of the session created by session tracker API.
	 * @param path specifies the hierarchy of the collection item.
	 * @param pageLocation specifies the page location of the event.
	 * @param reviewText specifies the user entered review text.
	 */
	public static void triggerReviewDataLogEvent(String resourceId,String collectionId, String parentEventId,String sessionId,String path, String pageLocation,String reviewText){
		JSONObject collectionDataLog=new JSONObject(); 
		Long startTime=PlayerDataLogEvents.getUnixTime();
		collectionDataLog.put(PlayerDataLogEvents.EVENTID, new JSONString(GwtUUIDGenerator.uuid()));
		collectionDataLog.put(PlayerDataLogEvents.EVENTNAME, new JSONString(ITEM_REVIEW));
		collectionDataLog.put(PlayerDataLogEvents.SESSION, PlayerDataLogEvents.getDataLogSessionObject(sessionId));
		collectionDataLog.put(PlayerDataLogEvents.STARTTIME, new JSONNumber(startTime));
		collectionDataLog.put(PlayerDataLogEvents.ENDTIME, new JSONNumber(startTime));
		collectionDataLog.put(PlayerDataLogEvents.METRICS,PlayerDataLogEvents.getDataLogMetricsObject(startTime-startTime));
		collectionDataLog.put(PlayerDataLogEvents.VERSION,PlayerDataLogEvents.getDataLogVersionObject());
		collectionDataLog.put(PlayerDataLogEvents.USER, PlayerDataLogEvents.getDataLogUserObject());
		collectionDataLog.put(PlayerDataLogEvents.PAYLOADOBJECT,PlayerDataLogEvents.getReviewDataLogPayLoadObject(reviewText));
		collectionDataLog.put(PlayerDataLogEvents.CONTEXT,PlayerDataLogEvents.getCommentDataLogContextObject(resourceId,null,parentEventId, collectionId, path, pageLocation));
		PlayerDataLogEvents.collectionStartStopEvent(collectionDataLog);
	}
	
	/**
	 * This method is used to frame comments payload JSON object and will return JSON object.
	 * @param commentText specifies user entered comment text.
	 * @return  JSON object
	 */
	public static  JSONString getCommentDataLogPayLoadObject(String commentText){
		JSONObject payLoadMap=new JSONObject();
		try{
			if(commentText!=null){
				payLoadMap.put(TEXT, new JSONString(com.google.gwt.http.client.URL.encodeQueryString(commentText)));
			}
		}catch(Exception e){
		}
		return new JSONString(payLoadMap.toString());
	}
	
	/**
	 *  This method is used to frame comments context JSON object and will return context JSON object.
	 * @param collectionId specifies unique id of the collection.
	 * @param collectionId specifies unique id of the collection.
	 * @param commentGooruOid specifies unique id of the comment in case of comment update and delete.
	 * @param parentEventId  specifies event id of the library view event id or class view event id.
	 * @param parentGooruOid specifies unique id of the library gooruOid or class view event id.
	 * @param path specifies the hierarchy of the collection.
	 * @param pageLocation specifies the page location of the event.
	 * @return JSON object.
	
	 */
	public static  JSONString getCommentDataLogContextObject(String collectionId,String commentGooruOid,String parentEventId, String parentGooruOid, String path, String pageLocation){
		JSONObject contextMap=new JSONObject();
		try{
			contextMap.put(CONTENTGOORUID, new JSONString(collectionId));
			if(parentGooruOid!=null){
				contextMap.put(PARENTGOORUID, new JSONString(parentGooruOid));
			}
			if(parentEventId!=null){
				contextMap.put(PARENTEVENTID, new JSONString(parentEventId));
			}
			contextMap.put(CLIENTSOURCE, new JSONString(WEB));
			contextMap.put(PATH, new JSONString(path));
			if(pageLocation!=null){
				contextMap.put(PAGELOCATION, new JSONString(pageLocation)); 
			}else{
				contextMap.put(PAGELOCATION, new JSONString(AppClientFactory.getPlaceManager().getPageLocation()));
			}
			
		}catch(Exception e){
			
		}
		return new JSONString(contextMap.toString());
	}
	
	/**
	 * This method is used to frame rating payload JSON object and will return JSON object.
	 * @param currentRate specifies user given star rating for collection item.
	 * @param previousRate specifies user previous given star rating for collection item.
	 * @return JSON object.
	 */
	public static  JSONString getRatingtDataLogPayLoadObject(double currentRate,double previousRate){
		JSONObject payLoadMap=new JSONObject();
		try{
			payLoadMap.put(RATE, new JSONNumber(currentRate));
			payLoadMap.put(PREVIOUS_RATE, new JSONNumber(previousRate));
			payLoadMap.put(ITEMTYPE, new JSONString(RESOURCE));
		}catch(Exception e){
		}
		return new JSONString(payLoadMap.toString());
	}
	/**
	 * This method is used to frame review payload JSON object and will return JSON object.
	 * @param reviewText specifies the user entered review text.
	 * @return JSON object
	 */
	public static  JSONString getReviewDataLogPayLoadObject(String reviewText){
		JSONObject payLoadMap=new JSONObject();
		try{
			payLoadMap.put(TEXT, new JSONString(com.google.gwt.http.client.URL.encodeQueryString(reviewText)));
			payLoadMap.put(ITEMTYPE, new JSONString(RESOURCE));
		}catch(Exception e){
		}
		return new JSONString(payLoadMap.toString());
	}
	
	
	public static Long getUnixTime(){
		return System.currentTimeMillis();
	}
	public static native String getUnixTimeStamp() /*-{
		var currentDate=new Date();
		var unixTimeStamp=currentDate.getTime();
		return ""+unixTimeStamp;
	}-*/;

	
}
