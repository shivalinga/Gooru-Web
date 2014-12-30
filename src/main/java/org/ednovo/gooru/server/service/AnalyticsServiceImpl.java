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
package org.ednovo.gooru.server.service;

import java.util.ArrayList;
import java.util.List;
import org.restlet.representation.StringRepresentation;
import org.ednovo.gooru.client.service.AnalyticsService;
import org.ednovo.gooru.server.annotation.ServiceURL;
import org.ednovo.gooru.server.request.JsonResponseRepresentation;
import org.ednovo.gooru.server.request.ServiceProcessor;
import org.ednovo.gooru.server.request.UrlToken;
import org.ednovo.gooru.server.serializer.JsonDeserializer;
import org.ednovo.gooru.shared.model.analytics.CollectionProgressDataDo;
import org.ednovo.gooru.shared.model.analytics.CollectionSummaryMetaDataDo;
import org.ednovo.gooru.shared.model.analytics.CollectionSummaryUsersDataDo;
import org.ednovo.gooru.shared.model.analytics.FeedBackResponseDataDO;
import org.ednovo.gooru.shared.model.analytics.GradeJsonData;
import org.ednovo.gooru.shared.model.analytics.OetextDataDO;
import org.ednovo.gooru.shared.model.analytics.UserDataDo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;

@Service("analyticsService")
@ServiceURL("/analyticsService")
public class AnalyticsServiceImpl extends BaseServiceImpl implements AnalyticsService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AnalyticsServiceImpl.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String THUMBNAIL="thumbnail";
	public static final String USERCOUNT="userCount";
	public static final String LASTMODIFIED="lastModified";
	public static final String COMPLETIONSTATUS="completionStatus";
	public static final String TIMESPENT="timeSpent";
	public static final String OE="OE";
	public static final String TITLE="title";
	public static final String DESCRIPTION="description";
	public static final String OPTIONS="options";
	public static final String SKIP="skip";
	public static final String SCORE="score";
	public static final String TOTALQUESTIONCOUNT="totalQuestionCount";
	public static final String TOTALRESOURCECOUNT="totalResourceCount";
	public static final String GRADEINPERCENTAGE="gradeInPercentage";
	public static final String GOORUOID="gooruOId";
	
	public static final String FIELDS="fields";
	public static final String AVGTIMESPENT="avgTimeSpent";
	public static final String VIEWS="views";
	public static final String AVGREACTION="avgReaction";
	public static final String SESSION="session";
	public static final String USERUID="userUId";
	public static final String COLLECTIONGOORUOID="collectionGooruOId";
	public static final String PATHWAYID="pathwayId";
	
	public static final String FILTERS="filters";
	public static final String PAGINATE="paginate";
	public static final String SORTBY="sortBy";
	public static final String SORTORDER="sortOrder";
	
	public static final String ITEMSEQUENCE="itemSequence";
	public static final String ASCENDING="ASC";
	
	public static final String CLASSID="classId";

	@Override
	public ArrayList<CollectionProgressDataDo> getCollectionProgressData(String collectionId,String classPageId,String pathwayId) {
		JsonRepresentation jsonRep = null;
		ArrayList<CollectionProgressDataDo> collectionProgressDataList=new ArrayList<CollectionProgressDataDo>();
		String dataPassing="{%22fields%22:%22timeSpent,avgTimeSpent,resourceGooruOId,OE,questionType,category,gooruUId,userName,userData,metaData,reaction,gooruOId,title,description,options,skip%22,%22filters%22:{%22session%22:%22FS%22,%22classId%22:%22"+classPageId+"%22,%22pathwayId%22:%22"+pathwayId+"%22},%22paginate%22:{%22sortBy%22:%22itemSequence%22,%22sortOrder%22:%22ASC%22}}";
		String url = UrlGenerator.generateUrl(getAnalyticsEndPoint(), UrlToken.V1_COLLECTIONPROGRESSDATA, collectionId,getLoggedInSessionToken(),dataPassing);
		LOGGER.info("url:+"+url);
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		jsonRep = jsonResponseRep.getJsonRepresentation();
		if(jsonResponseRep.getStatusCode()==200){ 
			try {
				collectionProgressDataList= (ArrayList<CollectionProgressDataDo>) JsonDeserializer.deserialize(jsonRep.getJsonObject().getJSONArray("content").toString(),new TypeReference<List<CollectionProgressDataDo>>() {});
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else{
		}
		return collectionProgressDataList;
	}

	@Override
	public ArrayList<CollectionSummaryUsersDataDo> getCollectionSummaryUsersData(String classpageId) {
		JsonRepresentation jsonRep = null;
		ArrayList<CollectionSummaryUsersDataDo> collectionSummaryUsersDataDoList=new ArrayList<CollectionSummaryUsersDataDo>();
		String dataPassing="{%22fields%22:%22userGroupUId,userName,gooruUId%22}";
		String url = UrlGenerator.generateUrl(getAnalyticsEndPoint(), UrlToken.V1_GETUSERSFORPATHWAY, classpageId,getLoggedInSessionToken(),dataPassing);
		LOGGER.info("url:+"+url);
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		jsonRep = jsonResponseRep.getJsonRepresentation();
		if(jsonResponseRep.getStatusCode()==200){
			try {
				collectionSummaryUsersDataDoList= (ArrayList<CollectionSummaryUsersDataDo>) JsonDeserializer.deserialize(jsonRep.getJsonObject().getJSONArray("content").toString(),new TypeReference<List<CollectionSummaryUsersDataDo>>() {});
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else{
		}
		return collectionSummaryUsersDataDoList;
	}

	@Override
	public ArrayList<UserDataDo> getCollectionResourceData(String collectionId,String classpageId,String pathwayId) {
		JsonRepresentation jsonRep = null;
		ArrayList<UserDataDo> collectionResourcesList=new ArrayList<UserDataDo>();
		String dataPassing="{%22fields%22:%22answerObject,score,totalAttemptUserCount,timeSpent,views,avgTimeSpent,OE,collectionGooruOId,category,resourceGooruOId,metaData,title,questionType,options,description,options,skip,totalInCorrectCount,avgReaction,reaction,attempts,text,totalCorrectCount,itemSequence%22,%22filters%22:{%22session%22:%22AS%22,%22classId%22:%22"+classpageId+"%22,%22pathwayId%22:%22"+pathwayId+"%22},%22paginate%22:{%22sortBy%22:%22itemSequence%22,%22sortOrder%22:%22DESC%22}}";
		String url = UrlGenerator.generateUrl(getAnalyticsEndPoint(), UrlToken.V1_GETCOLLECTIONRESOURCEDATA, collectionId,getLoggedInSessionToken(),dataPassing);
		LOGGER.info("url:+"+url);
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		jsonRep = jsonResponseRep.getJsonRepresentation();
		if(jsonResponseRep.getStatusCode()==200){
			try {
				collectionResourcesList= (ArrayList<UserDataDo>) JsonDeserializer.deserialize(jsonRep.getJsonObject().getJSONArray("content").toString(),new TypeReference<List<UserDataDo>>() {});
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else{
		}
		return collectionResourcesList;
	}

	@Override
	public ArrayList<CollectionSummaryUsersDataDo> getSessionsDataByUser(
			String collectionId, String classId, String userId) {
		JsonRepresentation jsonRep = null;
		ArrayList<CollectionSummaryUsersDataDo> sessionDataList=new ArrayList<CollectionSummaryUsersDataDo>();
		//String url ="http://www.goorulearning.org/insights/api/v1/classpage/fe78faa5-f7f0-4927-9282-a58a4e3deb5d/sessions.json?sessionToken=04fb9f7e-47c3-11e4-8d6c-123141016e2a&data={%22fields%22:%22%22,%22filters%22:{%22userUId%22:%22240fb01d-5383-4bb1-ae61-df045bf5f611%22,%22classId%22:%226a4cdb36-c579-4994-8ea0-5130a9838cbd%22},%22paginate%22:{%22sortBy%22:%22timeStamp%22,%22sortOrder%22:%22ASC%22}}&timestamp=1411986438542";
		String dataPassing ="{%22fields%22:%22%22,%22filters%22:{%22userUId%22:\""+userId+"\",%22classId%22:%22"+classId+"%22},%22paginate%22:{%22sortBy%22:%22timeStamp%22,%22sortOrder%22:%22ASC%22}}";
		String url = UrlGenerator.generateUrl(getAnalyticsEndPoint(), UrlToken.V1_GETSESSIONSDATABYUSER, collectionId,getLoggedInSessionToken(),dataPassing);
		LOGGER.info("url:+"+url);
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		jsonRep = jsonResponseRep.getJsonRepresentation();
		if(jsonResponseRep.getStatusCode()==200){
			try {
				sessionDataList= (ArrayList<CollectionSummaryUsersDataDo>) JsonDeserializer.deserialize(jsonRep.getJsonObject().getJSONArray("content").toString(),new TypeReference<List<CollectionSummaryUsersDataDo>>() {});
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else{
		}
		return sessionDataList;
	}

	@Override
	public ArrayList<UserDataDo> getUserSessionDataByUser(String collectionId,String classId, String userId,String sessionId,String pathwayId) {
		JsonRepresentation jsonRep = null;
		ArrayList<UserDataDo> collectionResourcesList=new ArrayList<UserDataDo>();
		String dataPassing ="{%22fields%22:%22answerObject,score,totalAttemptUserCount,timeSpent,views,avgTimeSpent,OE,collectionGooruOId,category,resourceGooruOId,metaData,title,questionType,options,description,options,skip,totalInCorrectCount,avgReaction,reaction,attempts,text,totalCorrectCount,itemSequence%22,%22filters%22:{%22userUId%22:%22"+userId+"%22,%22session%22:%22CS%22,%22sessionId%22:%22"+sessionId+"%22,%22pathwayId%22:%22"+pathwayId+"%22,%22classId%22:%22"+classId+"%22},%22paginate%22:{%22sortBy%22:%22itemSequence%22,%22sortOrder%22:%22ASC%22}}&timestamp=1410757700537";
		String url = UrlGenerator.generateUrl(getAnalyticsEndPoint(), UrlToken.V1_GETSESSIONDATABYUSERSESSION, collectionId,getLoggedInSessionToken(),dataPassing);
	
		//String url="http://www.goorulearning.org/insights/api/v1/classpage/fe78faa5-f7f0-4927-9282-a58a4e3deb5d/resources.json?sessionToken=04fb9f7e-47c3-11e4-8d6c-123141016e2a&data={%22fields%22:%22feedbackText,feedbackStatus,feedbackTimestamp,feedbackProviderUId,answerObject,score,totalAttemptUserCount,timeSpent,views,avgTimeSpent,OE,collectionGooruOId,category,resourceGooruOId,metaData,title,questionType,options,description,options,skip,reaction,attempts,text,itemSequence%22,%22filters%22:{%22session%22:%22CS%22,%22sessionId%22:%22"+sessionId+"%22,%22userUId%22:%22240fb01d-5383-4bb1-ae61-df045bf5f611%22,%22classId%22:%226a4cdb36-c579-4994-8ea0-5130a9838cbd%22},%22paginate%22:{%22sortBy%22:%22itemSequence%22,%22sortOrder%22:%22ASC%22}}&timestamp=1411986439903";
		LOGGER.info("url:+"+url);
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		jsonRep = jsonResponseRep.getJsonRepresentation();
		if(jsonResponseRep.getStatusCode()==200){
			try {
				collectionResourcesList= (ArrayList<UserDataDo>) JsonDeserializer.deserialize(jsonRep.getJsonObject().getJSONArray("content").toString(),new TypeReference<List<UserDataDo>>() {});
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else{
		}
		return collectionResourcesList;
	}

	@Override
	public ArrayList<CollectionSummaryMetaDataDo> getCollectionMetaDataByUserAndSession(
			String collectionId, String classId, String userId, String sessionId) {
		JsonRepresentation jsonRep = null;
		ArrayList<CollectionSummaryMetaDataDo> collectionSummaryMetaDataDoList=new ArrayList<CollectionSummaryMetaDataDo>();
		String dataPassing="{%22fields%22:%22thumbnail,userCount,lastModified,completionStatus,timeSpent,views,avgTimeSpent,OE,gooruOId,title,description,options,skip,score,avgReaction,totalQuestionCount,gradeInPercentage%22,%22filters%22:{%22userUId%22:%22"+userId+"%22,%22session%22:%22CS%22,%22sessionId%22:%22"+sessionId+"%22,%22classId%22:%22"+classId+"%22}}";
		String url = UrlGenerator.generateUrl(getAnalyticsEndPoint(), UrlToken.V1_GETCOLLECTIONMETADATA, collectionId,getLoggedInSessionToken(),dataPassing);
		//String url="http://www.goorulearning.org/insights/api/v1/classpage/fe78faa5-f7f0-4927-9282-a58a4e3deb5d.json?sessionToken=04fb9f7e-47c3-11e4-8d6c-123141016e2a&data={%22fields%22:%22thumbnail,lastModified,completionStatus,timeSpent,views,avgTimeSpent,OE,gooruOId,title,description,options,skip,score,avgReaction,totalQuestionCount,gradeInPercentage%22,%22filters%22:{%22session%22:%22CS%22,%22sessionId%22:%22FAFFA60D-46FA-45C1-BBB0-0BD40A800C6B%22,%22userUId%22:%22240fb01d-5383-4bb1-ae61-df045bf5f611%22,%22classId%22:%226a4cdb36-c579-4994-8ea0-5130a9838cbd%22}}&timestamp=1411986439399";
		LOGGER.info("url:+"+url);
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		jsonRep = jsonResponseRep.getJsonRepresentation();
		if(jsonResponseRep.getStatusCode()==200){
			try {
				collectionSummaryMetaDataDoList= (ArrayList<CollectionSummaryMetaDataDo>) JsonDeserializer.deserialize(jsonRep.getJsonObject().getJSONArray("content").toString(),new TypeReference<List<CollectionSummaryMetaDataDo>>() {});
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else{
		}
		return collectionSummaryMetaDataDoList;
	}

	@Override
	public String setHTMLtoPDF(String htmlString,String fileName) {
		String pdfName=fileName.replaceAll(" ", "_");
		pdfName = pdfName + "_Collection_Summary.pdf";
		String savedFileName=null;
		StringRepresentation stringRepresentation= null;
		String downloadUrl="";
		try{
			//String url = "http://www.goorulearning.org/gooruapi/rest/v2/media/htmltopdf?sessionToken=aec96f9c-42df-11e4-8d6c-123141016e2a";
			String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_GENERATE_PDF,getLoggedInSessionToken());
			String dataPassing="{\"fileName\":\"Mymedia\",\"html\":\""+htmlString+"\"}";
			LOGGER.info("html to pdf url"+url);
			LOGGER.info(dataPassing);
			stringRepresentation = ServiceProcessor.postString(url, getRestUsername(), getRestPassword(),dataPassing);
			savedFileName=stringRepresentation.getText();
			downloadUrl=UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_DOWNLOADFILE,getLoggedInSessionToken(),savedFileName,pdfName);
		}catch(Exception e){
			
		}
		return downloadUrl;
	}

	@Override
	public ArrayList<GradeJsonData> getAnalyticsGradeData(String classpageId,String pathwayId) {
		JsonRepresentation jsonRep = null;
		ArrayList<GradeJsonData> collectionResourcesList=new ArrayList<GradeJsonData>();
		String dataPassing ="{%22fields%22:%22timeSpent,score,gradeInPercentage,totalQuestionCount,avgTimeSpent,resourceGooruOId,gooruUId,userName,userData,gooruOId,title%22,%22filters%22:{%22session%22:%22AS%22,%22userUId%22:%22%22,%22collectionGooruOId%22:%22%22,%22pathwayId%22:%22"+pathwayId+"%22},%22paginate%22:{%22sortBy%22:%22itemSequence%22,%22sortOrder%22:%22ASC%22}}";
		String url = UrlGenerator.generateUrl(getAnalyticsEndPoint(), UrlToken.V1_GETGRADEJSON, classpageId,getLoggedInSessionToken(),dataPassing);
		LOGGER.info("url:+"+url);
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		jsonRep = jsonResponseRep.getJsonRepresentation();
		if(jsonResponseRep.getStatusCode()==200){
			try {
				String boolVal="";
				JSONArray messageArr=jsonRep.getJsonObject().getJSONArray("message");
				if(messageArr.length()>0){
					boolVal=messageArr.getJSONObject(0).getString("aggregateData");
					if(boolVal.equalsIgnoreCase("true")){
						collectionResourcesList= (ArrayList<GradeJsonData>) JsonDeserializer.deserialize(jsonRep.getJsonObject().getJSONArray("content").toString(),new TypeReference<List<GradeJsonData>>() {});
					}else{
						GradeJsonData gradeData=new GradeJsonData();
						gradeData.setAggregateData(boolVal);
						collectionResourcesList.add(gradeData);
					}
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else{
		}
		return collectionResourcesList;
	}

	@Override
	public String exportPathwayOE(String classpageId, String pathwayId,String timeZone) {
		String dataPassing ="{%22fields%22:%22%22,%22filters%22:{%22session%22:%22FS%22,%22sessionId%22:%22%22,%22userUId%22:%22%22,%22classId%22:%22%22,%22collectionGooruOId%22:%22%22,%22pathwayId%22:%22"+pathwayId+"%22},%22paginate%22:{%22sortBy%22:%22%22,%22sortOrder%22:%22%22}}";
		String url = UrlGenerator.generateUrl(getAnalyticsEndPoint(), UrlToken.V1_EXPORTOEPATHWAY, classpageId,getLoggedInSessionToken(),dataPassing,timeZone);
		LOGGER.info("url:+"+url);
		return url;
	}

	@Override
	public ArrayList<GradeJsonData>  getBottomAndTopScoresData(String collectionId, String classId,
			String pathwayId,String sortOrder) {
		JsonRepresentation jsonRep = null;
		ArrayList<GradeJsonData> collectionResourcesList=new ArrayList<GradeJsonData>();
		String dataPassing ="{%22fields%22:%22timeSpent,firstName,lastName,emailId,profileUrl,score,gradeInPercentage,totalQuestionCount,avgTimeSpent,resourceGooruOId,gooruUId,userName,userData,gooruOId,title%22,%22filters%22:{%22session%22:%22FS%22,%22userUId%22:%22%22,%22collectionGooruOId%22:%22"+collectionId+"%22,%22pathwayId%22:%22"+pathwayId+"%22},%22paginate%22:{%22sortBy%22:%22itemSequence,gradeInPercentage%22,%22sortOrder%22:%22"+sortOrder+"%22,%22totalRecords%22:3}}";
		String url = UrlGenerator.generateUrl(getAnalyticsEndPoint(), UrlToken.V1_GETGRADEJSON, classId,getLoggedInSessionToken(),dataPassing);
		LOGGER.info("url:+"+url);
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		jsonRep = jsonResponseRep.getJsonRepresentation();
		if(jsonResponseRep.getStatusCode()==200){
			try {
				collectionResourcesList= (ArrayList<GradeJsonData>) JsonDeserializer.deserialize(jsonRep.getJsonObject().getJSONArray("content").toString(),new TypeReference<List<GradeJsonData>>() {});
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else{
		}
		return collectionResourcesList;
	}
	
	
	@Override
	public CollectionSummaryMetaDataDo getAssignmentAverageData(String classId,String unitId,String collectionId){
		
		JsonRepresentation jsonRep = null;
		CollectionSummaryMetaDataDo collectionSummaryMetaDataDo=null;
		String requiredFields=AVGTIMESPENT +","+AVGREACTION+","+VIEWS+","+THUMBNAIL+","+USERCOUNT+","+LASTMODIFIED+","+COMPLETIONSTATUS+","+TIMESPENT+","+OE+","+TITLE+","+DESCRIPTION+","+OPTIONS+","+SKIP+","+SCORE+","+TOTALQUESTIONCOUNT+","+TOTALRESOURCECOUNT+","+GRADEINPERCENTAGE+","+GOORUOID;
		
		String urlDataParameterValue=createJsonPayloadObject(unitId,classId,"",requiredFields);
		
		System.out.println("getAnalyticsEndPoint()::"+getAnalyticsEndPoint());
		System.out.println("collectionId::"+collectionId);
		System.out.println("urlDataParameterValue::"+urlDataParameterValue);
		String  url= UrlGenerator.generateUrl(getAnalyticsEndPoint(), UrlToken.V1_GETCOLLECTIONMETADATA, collectionId,getLoggedInSessionToken(),urlDataParameterValue);
		LOGGER.info("url==>"+url);
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		jsonRep = jsonResponseRep.getJsonRepresentation();
		if(jsonResponseRep.getStatusCode()==200){
			try {
				JSONArray jsonArray=jsonRep.getJsonObject().getJSONArray("content");
				if(jsonArray!=null&&jsonArray.length()>0){
					collectionSummaryMetaDataDo=JsonDeserializer.deserialize(jsonArray.getJSONObject(0).toString(),new TypeReference<CollectionSummaryMetaDataDo>(){});
					LOGGER.info("JSON_RESPONSE==>"+jsonArray.getJSONObject(0).toString());
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return collectionSummaryMetaDataDo;
	}
	
	public String createJsonPayloadObject(String unitId,String classId,String userUid,String requiredFields){
		JSONObject jsonDataObject=new JSONObject(); 
		try {
			jsonDataObject.put(FIELDS, requiredFields);
			
			JSONObject filtersJsonObject=new JSONObject();
			filtersJsonObject.put(SESSION, "AS");
			filtersJsonObject.put(USERUID, userUid);
			filtersJsonObject.put(CLASSID, classId);
			filtersJsonObject.put(PATHWAYID, unitId);
			
			jsonDataObject.put(FILTERS, filtersJsonObject);
			
			JSONObject paginateJsonObject=new JSONObject();
			paginateJsonObject.put(SORTBY, ITEMSEQUENCE);
			paginateJsonObject.put(SORTORDER, ASCENDING);
			
			jsonDataObject.put(PAGINATE, paginateJsonObject);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonDataObject.toString();
	}

	private ArrayList<OetextDataDO> deserializeOETextResponse(JSONArray jsonArray) {
		ArrayList<OetextDataDO> oetextlistObj=new ArrayList<OetextDataDO>();
		if(jsonArray.length()!=0){
			for (int i=0;i<jsonArray.length();i++) {
				try {
					JSONObject jsonObject=(JSONObject) jsonArray.get(i);
					OetextDataDO oetextDataDOObj=new OetextDataDO();
					if(!jsonObject.isNull("answerObject"))
					oetextDataDOObj.setAnswerObject(jsonObject.getString("answerObject"));
					
					if(!jsonObject.isNull("feedbackStatus"))
					oetextDataDOObj.setFeedbackStatus(jsonObject.getString("feedbackStatus"));
					
					if(!jsonObject.isNull("feedbackText"))
					oetextDataDOObj.setFeedbackText(jsonObject.getString("feedbackText"));
					
					if(!jsonObject.isNull("feedbackTimestamp"))
					oetextDataDOObj.setFeedbackTimestamp(jsonObject.getLong("feedbackTimestamp"));
					
					if(!jsonObject.isNull("feedbackProviderUId"))
					oetextDataDOObj.setFeedbackProviderUId(jsonObject.getString("feedbackProviderUId"));
					
					if(!jsonObject.isNull("gooruUId"))
					oetextDataDOObj.setGooruUId(jsonObject.getString("gooruUId"));
					
					if(!jsonObject.isNull("OEText"))
					oetextDataDOObj.setOEText(jsonObject.getString("OEText"));
					
					if(!jsonObject.isNull("organizationUId"))
					oetextDataDOObj.setOrganizationUId(jsonObject.getString("organizationUId"));
					
					if(!jsonObject.isNull("status"))
					oetextDataDOObj.setStatus(jsonObject.getInt("status"));
					
					if(!jsonObject.isNull("userGroupUId"))
					oetextDataDOObj.setUserGroupUId(jsonObject.getString("userGroupUId"));
					
					if(!jsonObject.isNull("userName"))
					oetextDataDOObj.setUserName(jsonObject.getString("userName"));
					
					oetextlistObj.add(oetextDataDOObj);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return oetextlistObj;
	}

	@Override
	public FeedBackResponseDataDO postTeacherFeedBackToStudent(String freeText,
			String resourceId, String collectionId, String classpageId,	String pathwayId, String userId, String session,String contentItemId,String parentItemId,String classCode) {
		JsonRepresentation jsonRep = null;
		FeedBackResponseDataDO feedBackResponseDataDO=new FeedBackResponseDataDO();
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_ITEMFEEDBACK, session,getLoggedInSessionToken());
		//String url ="http://www.goorulearning.org/gooruapi/rest/v2/session/AS/item/feedback?sessionToken=08a99a16-4ea5-11e4-8d6c-123141016e2a";
		LOGGER.info("url:+"+url);
		JSONObject mainObj=new JSONObject();
		JSONObject userObj=new JSONObject();
		JSONObject setPlayLoadObj=new JSONObject();
		try {
			userObj.put("partyUid", userId);
			
			setPlayLoadObj.put("classCode", classCode);
			setPlayLoadObj.put("pathwayId", pathwayId);
			setPlayLoadObj.put("classId",classpageId);
			setPlayLoadObj.put("sessionId",session);
			if(contentItemId.equalsIgnoreCase("commentsDelete")){
				setPlayLoadObj.put("active ","false");
			}else{
				setPlayLoadObj.put("active ","true");
			}
			
			mainObj.put("contentGooruOId",resourceId);
			mainObj.put("contentItemId",contentItemId);
			mainObj.put("parentItemId",parentItemId);
			mainObj.put("parentGooruOId",collectionId);
			mainObj.put("freeText",freeText);
			mainObj.put("payLoadObject",setPlayLoadObj.toString());
			mainObj.put("user",userObj);
			LOGGER.info("mainObj.toString()::"+mainObj.toString());
			JsonResponseRepresentation jsonResponseRep = ServiceProcessor.post(url, getRestUsername(), getRestPassword(),mainObj.toString());
			jsonRep = jsonResponseRep.getJsonRepresentation();
			if(jsonResponseRep.getStatusCode()==200){
				feedBackResponseDataDO=deserializeTeacherResponse(jsonRep.getJsonObject());
			}else{
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return feedBackResponseDataDO;
	}

	private FeedBackResponseDataDO deserializeTeacherResponse(
			JSONObject jsonObject) {
		FeedBackResponseDataDO feedBackResponseDataDO=new FeedBackResponseDataDO();
			try {
				if(!jsonObject.isNull("contentGooruOId"))
					feedBackResponseDataDO.setContentGooruOId(jsonObject.getString("contentGooruOId"));
					
				if(!jsonObject.isNull("contentItemId"))
					feedBackResponseDataDO.setContentItemId(jsonObject.getString("contentItemId"));
				
				if(!jsonObject.isNull("createdOn"))
				  feedBackResponseDataDO.setCreatedOn(jsonObject.getLong("createdOn"));
				
				if(!jsonObject.isNull("feedbackProvidedBy") )
					feedBackResponseDataDO.setFeedbackProvidedByGooruId(jsonObject.getJSONObject("feedbackProvidedBy").getString("gooruUId"));
				
				if(!jsonObject.isNull("freeText"))
					feedBackResponseDataDO.setFreeText(jsonObject.getString("freeText"));
				
				if(!jsonObject.isNull("parentGooruOId"))
					feedBackResponseDataDO.setParentGooruOId(jsonObject.getString("parentGooruOId"));

				if(!jsonObject.isNull("parentItemId"))
					feedBackResponseDataDO.setSessionId(jsonObject.getString("parentItemId"));
				
				if(!jsonObject.isNull("sessionId"))
					feedBackResponseDataDO.setSessionItemFeedbackUid(jsonObject.getString("sessionId"));
				
				if(!jsonObject.isNull("user"))
					feedBackResponseDataDO.setUserGooruId(jsonObject.getJSONObject("user").getString("gooruUId"));
					
			} catch (JSONException e) {
				e.printStackTrace();
			}
		return feedBackResponseDataDO;
	}

	@Override
	public ArrayList<OetextDataDO> getOETextData(String resourceId,String collectionId, String classpageId, String pathwayId,String session, String sessionId, String userUId) {
		JsonRepresentation jsonRep = null;
		ArrayList<OetextDataDO> collectionResourcesList=new ArrayList<OetextDataDO>();
		String dataPassing ="{%22fields%22:%22feedbackStatus,userName,OEText,gooru_uid,feedbackText,feedbackProviderUId,feedbackTimestamp,answerObject%22,%22filters%22:{%22resourceGooruOId%22:%22"+resourceId+"%22,%22classId%22:%22"+classpageId+"%22,%22pathwayId%22:%22"+pathwayId+"%22,%22session%22:%22"+session+"%22,%22sessionId%22:%22"+sessionId+"%22,%22userUId%22:%22"+userUId+"%22}}";
		String url = UrlGenerator.generateUrl(getAnalyticsEndPoint(), UrlToken.V1_OETEXTJSON, collectionId,getLoggedInSessionToken(),dataPassing);
		LOGGER.info("url:+"+url);
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		jsonRep = jsonResponseRep.getJsonRepresentation();
		if(jsonResponseRep.getStatusCode()==200){
			try {
				collectionResourcesList=deserializeOETextResponse(jsonRep.getJsonObject().getJSONArray("content"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else{
		}
		return collectionResourcesList;
	}

	@Override
	public void sendEmail(String to, String subject, String message,String displayName, String fileName, String path) {
		JsonRepresentation jsonRep = null;
		//String url = "http://www.goorulearning.org/gooruapi/rest/v2/share/mail?sessionToken=5ef6d576-663a-11e4-a2ea-123141016e2a";
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_SEND_EMAIL_WITH_PDF,getLoggedInSessionToken());
		LOGGER.info("url:+"+url);
		JSONObject mainObj=new JSONObject();
		JSONObject attachmentObj=new JSONObject();
		JSONArray setPlayLoadObj=new JSONArray();
		try {
			attachmentObj.put("fileName", fileName);
			attachmentObj.put("url", path);

			setPlayLoadObj.put(attachmentObj);
			
			mainObj.put("to",to);
			mainObj.put("subject",subject);
			mainObj.put("message",message);
			mainObj.put("fromDisplayName",displayName);
			mainObj.put("attachment",setPlayLoadObj.toString());
			
			LOGGER.info("mainObj.toString()::"+mainObj.toString());
			JsonResponseRepresentation jsonResponseRep = ServiceProcessor.post(url, getRestUsername(), getRestPassword(),mainObj.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String exportTeacherSummary(String collectionGooruOId,String pathwayId, String classId,String timeZone) {
		String dataPassing ="{%22fields%22:%22%22,%22filters%22:{%22session%22:%22AS%22,%22sessionId%22:%22%22,%22userUId%22:%22%22,%22classId%22:%22"+classId+"%22,%22collectionGooruOId%22:%22"+collectionGooruOId+"%22},%22paginate%22:{%22sortBy%22:%22%22,%22sortOrder%22:%22%22}}";
		String url = UrlGenerator.generateUrl(getAnalyticsEndPoint(), UrlToken.V1_EXPORTSUMMARYATHWAY, classId,getLoggedInSessionToken(),dataPassing,timeZone);
		LOGGER.info("url:+"+url);
		return url;
	}
}
