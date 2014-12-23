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

import java.io.IOException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.ednovo.gooru.client.service.ClasspageService;
import org.ednovo.gooru.server.annotation.ServiceURL;
import org.ednovo.gooru.server.deserializer.ResourceDeserializer;
import org.ednovo.gooru.server.form.ResourceFormFactory;
import org.ednovo.gooru.server.request.JsonResponseRepresentation;
import org.ednovo.gooru.server.request.ServiceProcessor;
import org.ednovo.gooru.server.request.UrlToken;
import org.ednovo.gooru.server.serializer.JsonDeserializer;
import org.ednovo.gooru.shared.exception.GwtException;
import org.ednovo.gooru.shared.exception.ServerDownException;
import org.ednovo.gooru.shared.model.content.AssignmentDo;
import org.ednovo.gooru.shared.model.content.AssignmentsListDo;
import org.ednovo.gooru.shared.model.content.AssignmentsSearchDo;
import org.ednovo.gooru.shared.model.content.ClassPageCollectionDo;
import org.ednovo.gooru.shared.model.content.ClasspageDo;
import org.ednovo.gooru.shared.model.content.ClasspageItemDo;
import org.ednovo.gooru.shared.model.content.ClasspageListDo;
import org.ednovo.gooru.shared.model.content.CollaboratorsDo;
import org.ednovo.gooru.shared.model.content.CollectionDo;
import org.ednovo.gooru.shared.model.content.CollectionItemDo;
import org.ednovo.gooru.shared.model.content.MetaDO;
import org.ednovo.gooru.shared.model.content.ResourceDo;
import org.ednovo.gooru.shared.model.content.StudentsAssociatedListDo;
import org.ednovo.gooru.shared.model.content.TaskDo;
import org.ednovo.gooru.shared.model.content.TaskResourceAssocDo;
import org.ednovo.gooru.shared.model.social.SocialShareDo;
import org.ednovo.gooru.shared.model.user.BitlyUrlDo;
import org.ednovo.gooru.shared.model.user.ProfilePageDo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.StringRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;

@Service("classpageService")
@ServiceURL("/classpageService")
public class ClasspageServiceImpl extends BaseServiceImpl implements
		ClasspageService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3513384975044132831L;

	private static final String ADD_TO_USER_CLASSPAGE = "addToUserClasspage";

	private static final String TRUE = "true";

	private static final String CLASSPAGE = "classpage";

	private static final String ASSIGNMENT = "assignment";
	
	private static final String COLLECTIONTYPE = "collectionType";
	
	private static final String TITLE="title";
	private static final String ITEMCOUNT="itemCount";
	
	private static final String TASKRESOURCEASSOC = "taskResourceAssoc";
	
	private static final String COLLECTIONITEM="collectionItem";
	
	private static final String ITEMTYPE = "itemType";
	
	private static final String NARRATION="narration";
	
	private static final String PLANNEDENDDATE="plannedEndDate";
	
	private static final String COLLECTIONID="collectionId";
	
	private static final String SEARCHRESULTS="searchResults";
	private static final String GOALS="goals";
	private static final String THUMBNAIL="thumbnails";
	private static final String THUMBNAILURL="url";
	private static final String TOTALHITCOUNT="totalHitCount";
	private static final String RESOURCE="resource";
	private static final String COLLECTIONITEMID="collectionItemId";
	private static final String ADDED="added";
	private static final String GOORUOID="gooruOid";
	private static final String CREATOR="creator";
	private static final String UID="gooruUId";
	private static final String USERNAME="username";
	private static final String PROFILEIMAGE="profileImageUrl";	
	private static final String CLASSPAGECODE="classpageCode";
	private static final String META="meta";
	private static final String PERMISSIONS="permissions";
	private static final String COLLECTION="collection";
	private static final String SHARING="sharing";
	private static final String STATUS="status";
	private static final String MEMBERCOUNT="memberCount";
	private static final String USERNAMEWDISPLAY="usernameDisplay";
	private static final String PROFILEIMAGEURL="profileImageUrl";
	private static final String USER="user";
	private static final String ITEMSEQUENCE="itemSequence";

	private static final String HTTPS = "https";
	
	private static final String HTTP = "http";

	

	@Autowired
	ResourceDeserializer resourceDeserializer;
	/**
	 * This method is deprecated and  use instead versoion2 createClasspage(String classPageTitle);
	 */
	@Deprecated
	@Override
	public CollectionDo createClasspage(CollectionDo collectionDo) {
		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(),
				UrlToken.CREATE_CLASSPAGE, getLoggedInSessionToken());
		Form form = ResourceFormFactory.generateDataForm(collectionDo,
				CLASSPAGE);
		form.add(ADD_TO_USER_CLASSPAGE, TRUE);
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.post(url, getRestUsername(),
				getRestPassword(), form);
		jsonRep =jsonResponseRep.getJsonRepresentation();
		return deserializeCollection(jsonRep);

	}
	
	public CollectionDo deserializeCollection(JsonRepresentation jsonRep) {
		if (jsonRep != null && jsonRep.getSize() != -1) {
			try {
				JSONObject mainObj = jsonRep.getJsonObject();
				if(!mainObj.isNull(META)){
					if(!mainObj.getJSONObject(META).isNull(STATUS)){
						mainObj.put("status", mainObj.getJSONObject(META).getString(STATUS));	
						//classpageDo.setStatus(classpageJsonObject.getJSONObject(META).getString(STATUS));
					}
				}
				return JsonDeserializer.deserialize(mainObj.toString(), CollectionDo.class);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return new CollectionDo();
	}

	public CollectionDo deserializeAssignmentsCollection(
			JsonRepresentation jsonRep) {
		if (jsonRep != null && jsonRep.getSize() != -1) {
			try {
				CollectionDo collectionDo = JsonDeserializer.deserialize(
						jsonRep.getJsonObject().toString(), CollectionDo.class);
				return collectionDo;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return new CollectionDo();
	}
//	Version 1 API
	/*@Override
	public CollectionDo getClasspageById(String classpageId)
			throws GwtException {

		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(),
				UrlToken.GET_CLASSPAGE_BY_ID, classpageId,
				getLoggedInSessionToken());
		jsonRep = ServiceProcessor.get(url, getRestUsername(),
				getRestPassword());
		
		return deserializeClasspage(jsonRep);
	}*/
	public CollectionDo deserializeClasspage(JsonRepresentation jsonRep) {
		if (jsonRep != null && jsonRep.getSize() != -1) {
			try {
				CollectionDo collectionDo = JsonDeserializer.deserialize(jsonRep.getJsonObject().toString(), CollectionDo.class);

				List<CollectionItemDo> collectionItemDo = collectionDo.getCollectionItems();
				for (int i=0; i<collectionItemDo.size();i++){
					long milliseconds = Long.parseLong(collectionItemDo.get(i).getResource().getTrackActivity().getEndTime());
					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
					Date resultdate = new Date(milliseconds);
					collectionItemDo.get(i).getResource().getTrackActivity().setEndTime(sdf.format(resultdate));
				}
				return collectionDo;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return new CollectionDo();
	}
//	Version 1 API
	/*@Override
	public List<CollectionDo> getAllClasspages() throws GwtException {

		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(),
				UrlToken.LIST_MY_CLASSPAGES, getLoggedInSessionToken());
		jsonRep = ServiceProcessor.get(url, getRestUsername(),
				getRestPassword());

		return deserializeCollections(jsonRep);
	}
*/
	public List<CollectionDo> deserializeCollections(JsonRepresentation jsonRep) {
		try {
			if (jsonRep != null && jsonRep.getSize() != -1) {
				return JsonDeserializer.deserialize(jsonRep.getJsonArray()
						.toString(), new TypeReference<List<CollectionDo>>() {
				});
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new ArrayList<CollectionDo>();
	}

	/*@Override
	public CollectionDo createAssignment(CollectionDo collectionDo, String gooruContentId, String dueDate) throws GwtException {
		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.CREATE_ASSIGNMENT, getLoggedInSessionToken(), gooruContentId, dueDate);
		Form form = ResourceFormFactory.generateDataForm(collectionDo,ASSIGNMENT);
		form.add(ADD_TO_USER_CLASSPAGE, TRUE);

		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.post(url, getRestUsername(), getRestPassword(), form);
		jsonRep =jsonResponseRep.getJsonRepresentation();
		
		return deserializeAssignmentsCollection(jsonRep);
	}*/
//	Version 1 API
	/*@Override
	public void deleteAssignment(String assignmentId) throws GwtException {
		String url = UrlGenerator.generateUrl(getRestEndPoint(),
				UrlToken.DELETE_ASSIGNMENT, assignmentId,
				getLoggedInSessionToken());
		ServiceProcessor.delete(url, getRestUsername(), getRestPassword());
	}*/
//	Version 1 API
	/*@Override
	public CollectionDo updateAssignment(CollectionDo collectionDo,
			String updateDueDate) throws GwtException {
		JsonRepresentation jsonRep = null;

		String url = UrlGenerator.generateUrl(getRestEndPoint(),
				UrlToken.UPDATE_ASSIGNMENT, collectionDo.getGooruOid(),
				getLoggedInSessionToken(), updateDueDate);
		Form form = ResourceFormFactory.generateDataForm(collectionDo,
				ASSIGNMENT);
		form.add(ADD_TO_USER_CLASSPAGE, TRUE);

		jsonRep = ServiceProcessor.put(url, getRestUsername(),
				getRestPassword(), form);
		return deserializeAssignmentsCollection(jsonRep);
	}*/
//	Version 1 API
	/*@Override
	public CollectionDo getClasspageByCode(String classpageCode)throws GwtException {
		
		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.GET_CLASSPAGE_BY_CODE, classpageCode, getLoggedInSessionToken());
		jsonRep = ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		
		return deserializeCollection(jsonRep);
	}

	@Override
	public CollectionItemDo addCollectionToAssignment(String collectionId,
			String assignmentId, CollectionDo collectionDo) throws GwtException {
		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(),UrlToken.ADD_COLLECTIONS_TO_ASSIGNMENT, collectionId, assignmentId, getLoggedInSessionToken());

		Form form = ResourceFormFactory.generateDataForm(collectionDo, COLLECTIONITEM);
		form.add(ITEMTYPE, "added");

		jsonRep = ServiceProcessor.post(url, getRestUsername(), getRestPassword(), form);
		return deserializeCollectionItem(jsonRep);
	}*/
	public CollectionItemDo deserializeCollectionItem(JsonRepresentation jsonRep) {
		if (jsonRep != null && jsonRep.getSize() != -1) {
			try {
				return JsonDeserializer.deserialize(jsonRep.getJsonObject().toString(), CollectionItemDo.class);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return new CollectionItemDo();
	}
//	Version 1 API
	/*@Override
	public List<CollectionItemDo> getAssignmentCollectionsById(String assignmentId) {
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.GET_ASSIGNMENT_COLLECTIONS, assignmentId, getLoggedInSessionToken());
		return deserializeCollectionItems(ServiceProcessor.get(url, getRestUsername(), getRestPassword()));
	}
	public List<CollectionItemDo> deserializeCollectionItems(JsonRepresentation jsonRep) {
		if (jsonRep != null && jsonRep.getSize() != -1) {
			try {
				return JsonDeserializer.deserialize(jsonRep.getJsonArray().toString(), new TypeReference<List<CollectionItemDo>>() {
				});
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return new ArrayList<CollectionItemDo>();
	}

	@Override
	public void removeCollectionFromAssignment(String collectionId)
			throws GwtException {
		String url = UrlGenerator.generateUrl(getRestEndPoint(),
				UrlToken.DELETE_ASSIGNMENT_COLLECION_ITEM, collectionId,
				getLoggedInSessionToken());
		ServiceProcessor.delete(url, getRestUsername(), getRestPassword());
	}

	@Override
	public AssignmentsListDo getAssignemtsByClasspageId(String classpageId,String pageSize, String pageNum)
			throws GwtException {
		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.GET_CLASSPAGE_ASSIGNMENTS, classpageId, getLoggedInSessionToken(), pageSize, pageNum);
		jsonRep = ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		return deserializeAssignmentsList(jsonRep);
	}	*/
		
	public AssignmentsListDo deserializeAssignmentsList(JsonRepresentation jsonRep) {
		if (jsonRep != null && jsonRep.getSize() != -1) {
			try {
				
				AssignmentsListDo assignmentList = JsonDeserializer.deserialize(jsonRep.getJsonObject().toString(), AssignmentsListDo.class);
				
//				List<CollectionItemDo> collectionItemDo = assignmentList.getSearchResults();
//				for (int i=0; i<collectionItemDo.size();i++){
//					long milliseconds = Long.parseLong(collectionItemDo.get(i).getResource().getTrackActivity().getEndTime());
//					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
//					Date resultdate = new Date(milliseconds);
//					collectionItemDo.get(i).getResource().getTrackActivity().setEndTime(sdf.format(resultdate));
//				}
				
				return assignmentList;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return new AssignmentsListDo();
	}
//	Version 1 API
	/*@Override
	public CollectionDo updateClassPageByid(String classpageId,
			String CollectionType, String title) throws GwtException {
		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.UPDATE_CLASSPAGE, classpageId,getLoggedInSessionToken());
		CollectionDo collectionDo = new CollectionDo();
		collectionDo.setTitle(title);
		Form form = ResourceFormFactory.generateDataForm(collectionDo,
				CLASSPAGE);
		form.add("CollectionType", CollectionType);

		jsonRep = ServiceProcessor.put(url, getRestUsername(),
				getRestPassword(), form);
		return deserializeCollection(jsonRep);
	}
	*/
	/*
	 * Version 2 Api Implementation...
	 * 
	 */
	
	@Override
	public CollectionDo v2CreateClasspage(CollectionDo collectionDo) {
		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(),
				UrlToken.V2_CREATE_CLASSPAGE, getLoggedInSessionToken());
		ClasspageDo classpageDo = new ClasspageDo();
		classpageDo.setCollectionType(collectionDo.getCollectionType());
		classpageDo.setTitle(collectionDo.getTitle());
		//classpageDo.setTaxonomySet(null);
		
		String formData = ResourceFormFactory.generateStringDataForm(classpageDo,
				CLASSPAGE);

		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.post(url, getRestUsername(),
				getRestPassword(), formData);
		jsonRep =jsonResponseRep.getJsonRepresentation();
		return deserializeCollection(jsonRep);

	}
	
	@Override
	public CollectionDo v2UpdateClassPageByid(String classpageId,
			String CollectionType, String title, String shareType) throws GwtException {
		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_UPDATE_CLASSPAGE, classpageId,getLoggedInSessionToken());
		ClasspageDo classpageDo = new ClasspageDo();
		classpageDo.setCollectionType(CollectionType);
		if(title==null || title.equals("")){
			title=classpageDo.getTitle();
		}else{
			classpageDo.setTitle(title);
		}
		if(shareType==null || shareType.equals("")){
			shareType=classpageDo.getSharing();
		}else{
			classpageDo.setSharing(shareType);
		}
		
		//classpageDo.setTaxonomySet(null);
		try{
			JSONObject jsonObject=new JSONObject();
			JSONObject titleJsonObject= new JSONObject();
			titleJsonObject.put(TITLE, title);
			titleJsonObject.put(SHARING, shareType);
			jsonObject.put(CLASSPAGE, titleJsonObject);
			JsonResponseRepresentation jsonResponseRep = ServiceProcessor.put(url, getRestUsername(),getRestPassword(), jsonObject.toString());
			jsonRep =jsonResponseRep.getJsonRepresentation();
		}catch(Exception e){
			
		}
		return deserializeCollection(jsonRep);
	}
	
	@Override
	public AssignmentsListDo v2GetAssignemtsByClasspageId(String classpageId,String pageSize, String pageNum)
			throws GwtException {
		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_GET_CLASSPAGE_ASSIGNMENTS, classpageId, getLoggedInSessionToken(), pageSize, pageNum);
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		jsonRep =jsonResponseRep.getJsonRepresentation();
		return deserializeV2AssignmentsList(jsonRep);
	}
	
	@Override
	public ClasspageListDo v2GetAllClasspages(String limit, String offSet) throws GwtException {

		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(),
				UrlToken.V2_LIST_MY_CLASSPAGES, getLoggedInSessionToken(), limit, offSet);
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(),
				getRestPassword());
		jsonRep =jsonResponseRep.getJsonRepresentation();
		return deserializeClasspageList(jsonRep);
	}
	public ClasspageListDo deserializeClasspageList(JsonRepresentation jsonRep) {
		if (jsonRep != null && jsonRep.getSize() != -1) {
			try {
				ClasspageListDo classpageList = JsonDeserializer.deserialize(jsonRep.getJsonObject().toString(), ClasspageListDo.class);				
//				List<CollectionDo> collectionDo = classpageList.getSearchResults();
//				for (int i=0; i<collectionDo.size();i++){
//					long milliseconds = Long.parseLong(collectionDo.get(i).getTrackActivity().getEndTime());
//					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
//					Date resultdate = new Date(milliseconds);
//					collectionDo.get(i).getTrackActivity().setEndTime(sdf.format(resultdate));
//				}
				
				return classpageList;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return new ClasspageListDo();
	}
	
	@Override
	public ClasspageListDo v2GetUserClasses(String limit, String offSet, String randomId) throws GwtException {

		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(),
				UrlToken.V2_GET_LISTTEACHCLASSES, getLoggedInSessionToken(), limit, offSet, randomId);
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(),
				getRestPassword());
		jsonRep =jsonResponseRep.getJsonRepresentation();
		return deserializeClasspageList(jsonRep);
	}
	
	@Override
	public ClasspageListDo v2GetUserStudyClasses(String limit, String offSet, String randomId) throws GwtException {

		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(),
				UrlToken.V2_GET_LISTSTUDYCLASSES, getLoggedInSessionToken(), limit, offSet, randomId);
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(),
				getRestPassword());
		jsonRep =jsonResponseRep.getJsonRepresentation();
		return deserializeClasspageList(jsonRep);
	}
	
	@Override
	public void deleteClasspage(String classpageId) throws GwtException {
		String url = UrlGenerator.generateUrl(getRestEndPoint(),
				UrlToken.V2_CLASSPAGE_DELETE, classpageId,
				getLoggedInSessionToken());
		ServiceProcessor.delete(url, getRestUsername(), getRestPassword());
	}

	@Override
	public AssignmentDo v2CreateAssignment(AssignmentDo assignmentDo) throws GwtException {
		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_CREATE_ASSIGNMENT, getLoggedInSessionToken());
		String formData = ResourceFormFactory.generateStringDataForm(assignmentDo,null);
		
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.post(url, getRestUsername(), getRestPassword(), formData);
		jsonRep =jsonResponseRep.getJsonRepresentation();
		return deserializeAssignments(jsonRep);
	}
	
	public AssignmentDo deserializeAssignments(
			JsonRepresentation jsonRep) {
		if (jsonRep != null && jsonRep.getSize() != -1) {
			try {
				AssignmentDo assignmentDo = JsonDeserializer.deserialize(
						jsonRep.getJsonObject().toString(), AssignmentDo.class);
				return assignmentDo;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return new AssignmentDo();
	}
	@Override
	public CollectionDo v2GetClasspageById(String classpageId)
			throws GwtException {

		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(),
				UrlToken.V2_GET_CLASSPAGE_BY_ID, classpageId,
				getLoggedInSessionToken());
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(),
				getRestPassword());
		jsonRep =jsonResponseRep.getJsonRepresentation();
		return deserializeV2Classpage(jsonRep);
	}
	
	@Override
	public CollectionDo getSCollIdClasspageById(String classpageId)
			throws GwtException {

		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(),
				UrlToken.SIMPLE_COLL_GETAPI, classpageId,
				getLoggedInSessionToken());
		getLogger().info("--- getSCollIdClasspageById URL -- "+url);
		
		try{
			JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(),
				getRestPassword());
		    jsonRep =jsonResponseRep.getJsonRepresentation();
		}
		catch(Exception e)
		{
			
		}
		
		return deserializeV2Classpage(jsonRep);
	}
	
	public CollectionDo deserializeV2Classpage(JsonRepresentation jsonRep) {
		CollectionDo collectionDo = new CollectionDo();
		try {
			if (jsonRep.getText() != null && jsonRep.getSize() != -1) {
				try {
					collectionDo = JsonDeserializer.deserialize(jsonRep.getJsonObject().toString(), CollectionDo.class);
					return collectionDo;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return collectionDo;
	}

	
	public AssignmentsListDo deserializeV2AssignmentsList(JsonRepresentation jsonRep) {
		if (jsonRep != null && jsonRep.getSize() != -1) {
			try {
				
				AssignmentsListDo assignmentList = JsonDeserializer.deserialize(jsonRep.getJsonObject().toString(), AssignmentsListDo.class);
				
				List<AssignmentsSearchDo> assignmentsSearchDo = assignmentList.getSearchResults();
				for (int i=0; i<assignmentsSearchDo.size();i++){
					if (assignmentsSearchDo.get(i).getTask().getPlannedEndDate()!=null && !assignmentsSearchDo.get(i).getTask().getPlannedEndDate().equalsIgnoreCase("")){
						long milliseconds = Long.parseLong(assignmentsSearchDo.get(i).getTask().getPlannedEndDate());
						SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
						Date resultdate = new Date(milliseconds);
						assignmentsSearchDo.get(i).getTask().setPlannedEndDate(sdf.format(resultdate));
					}
				}
				
				return assignmentList;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return new AssignmentsListDo();
	}
	
	@Override
	public void socialShareEmail(String fromTxt, String toTxt,String cfm, String subTxt,
		String msgTxt) throws GwtException {
		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_SOCIAL_SHARE, getLoggedInSessionToken());
		
		String completeLogoDetails = "<!DOCTYPE html> <html> <head> <meta http-equiv=" +
		"Content-Type" +
		" content=" +
		"text/html; charset=UTF-8" +
		" /> </head> <body style='font-family: arial, sans-serif;color: #515151;height:345px;font-size: 12px; background-color: #f0f0f0;'><div style='text-align: center;'> <img id='logo-header' src='http://sfs.goorulearning.org/media/mail/v1/images/gooru-logo-small.png' style='width:100px;height:30px;margin: 20px auto 10px auto;'" +
		"></img></div> <div class='content-block img-desc' style='width: 500px;padding: 35px;background-color: white;border: 1px solid #DDD;margin:0px auto 30px auto;-moz-box-shadow: 0 0 10px rgba(0,0,0,.1); -webkit-box-shadow: 0 0 10px rgba(0,0,0,.1);box-shadow: 0 0 10px rgba(0,0,0,.1);'> <div style='font-family: arial;width: 520px;color:#666;height:150px'>"+ msgTxt + 
		"</body> </html>";
		
		SocialShareDo socialShareDo = new SocialShareDo();
		socialShareDo.setTo(toTxt);
		socialShareDo.setFromDisplayName(fromTxt);
		socialShareDo.setCfm(cfm);
		socialShareDo.setSubject(subTxt);
		socialShareDo.setMessage(completeLogoDetails);
		
		socialShareDo.setGooruOid(null);
		socialShareDo.setBitlylink(null);
		socialShareDo.setTitle(null);
		socialShareDo.setDescription(null);
		socialShareDo.setThumbnailurl(null);
		
		
		String formData = ResourceFormFactory.generateStringDataForm(socialShareDo, null);

		
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.post(url, getRestUsername(),	getRestPassword(), formData);
		jsonRep =jsonResponseRep.getJsonRepresentation();
	}

	@Override
	public TaskDo v2UpdateAssignment(AssignmentDo assignmentDo, String assignmentId)
			throws GwtException {
		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(),UrlToken.V2_UPDATE_ASSIGNMENT, assignmentId, getLoggedInSessionToken());
		String formData = ResourceFormFactory.generateStringDataForm(assignmentDo,null);
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.put(url, getRestUsername(),getRestPassword(), formData);
		jsonRep =jsonResponseRep.getJsonRepresentation();
		return deserializeTask(jsonRep);
		
	}
	public TaskDo deserializeTask(JsonRepresentation jsonRep) {
		if (jsonRep != null && jsonRep.getSize() != -1) {
			try {
				
				TaskDo taskDo = JsonDeserializer.deserialize(jsonRep.getJsonObject().toString(), TaskDo.class);
				
				return taskDo;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return new TaskDo();
	}

	@Override
	public void v2DeleteAssignment(String assignmentId) throws GwtException {
		String url = UrlGenerator.generateUrl(getRestEndPoint(),
				UrlToken.V2_DELETE_ASSIGNMENT, assignmentId,
				getLoggedInSessionToken());
		ServiceProcessor.delete(url, getRestUsername(), getRestPassword());
	}

	@Override
	public TaskResourceAssocDo v2AddCollectionToAssignment(String assignmentId,
			TaskResourceAssocDo taskResourceAssocDo) throws GwtException {
		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(),UrlToken.V2_ADD_COLLECTIONS_TO_ASSIGNMENT, assignmentId, getLoggedInSessionToken());
		String formData = ResourceFormFactory.generateStringDataForm(taskResourceAssocDo, TASKRESOURCEASSOC);
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.post(url, getRestUsername(), getRestPassword(), formData);
		jsonRep =jsonResponseRep.getJsonRepresentation();
		return deserializeTaskResourceAssoc(jsonRep);
	}
	public TaskResourceAssocDo deserializeTaskResourceAssoc(JsonRepresentation jsonRep) {
		if (jsonRep != null && jsonRep.getSize() != -1) {
			try {
				
				TaskResourceAssocDo taskResourceAssocDo = JsonDeserializer.deserialize(jsonRep.getJsonObject().toString(), TaskResourceAssocDo.class);
				
				return taskResourceAssocDo;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return new TaskResourceAssocDo();
	}

	@Override
	public List<ResourceDo> v2GetAssignmentCollectionsById(String assignmentId)
			throws GwtException {
		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_GET_ASSIGNMENT_COLLECTIONS, assignmentId, getLoggedInSessionToken());
		JsonResponseRepresentation jsonResponseRep =ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		jsonRep =jsonResponseRep.getJsonRepresentation();
		return deserializeTaskResourceSearch(jsonRep);
	}
	public List<ResourceDo> deserializeTaskResourceSearch(JsonRepresentation jsonRep) {
		if (jsonRep != null && jsonRep.getSize() != -1) {
			try {
				return JsonDeserializer.deserialize(jsonRep.getJsonArray()
						.toString(), new TypeReference<List<ResourceDo>>() {
				});
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return new  ArrayList<ResourceDo>();
	}
	
	@Override
	public void v2RemoveCollectionFromAssignment(String collectionId, String assignmentId)
			throws GwtException {
		String url = UrlGenerator.generateUrl(getRestEndPoint(),UrlToken.V2_DELETE_ASSIGNMENT_COLLECION_ITEM, assignmentId, collectionId,
				getLoggedInSessionToken());
		ServiceProcessor.delete(url, getRestUsername(), getRestPassword());
	}

	@Override
	public CollectionDo v2getClasspageByCode(String classpageCode) throws GwtException{
		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_GET_CLASSPAGE_BY_CODE, classpageCode, getLoggedInSessionToken());
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		jsonRep =jsonResponseRep.getJsonRepresentation();
		if(jsonRep!=null && jsonRep.getSize()!=1){	
			return deserializeCollection(jsonRep);
		}else{
			CollectionDo obj=new CollectionDo();
			return obj;
		}
	}
	@Override
	public List<String> ShotenUrl(String url){
		
		String httpUrl = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.GENERATE_BITLY_LINK, getLoggedInSessionToken());
		StringRepresentation strRep = null;
		
		BitlyUrlDo bitlyUrlDo = new BitlyUrlDo();
		bitlyUrlDo.setFullUrl(getHomeEndPoint() +"/#"+ url);
		String formData = ResourceFormFactory.generateStringDataForm(bitlyUrlDo, null);
		strRep = ServiceProcessor.postString(httpUrl, getRestUsername(), getRestPassword(), formData);
		List<String> listUrl=new ArrayList<String>();
		String bitlyLink=strRep.getText().toString();
		String rawUrl=bitlyUrlDo.getFullUrl();
		String originalUrl=bitlyUrlDo.getFullUrl();
		try {
			rawUrl=URLEncoder.encode(rawUrl, "UTF-8");
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(getHttpRequest().getScheme().equalsIgnoreCase(HTTPS)) {
			bitlyLink = bitlyLink.replaceAll(HTTP, HTTPS);
		}
		
		listUrl.add(bitlyLink);
		listUrl.add(rawUrl);
		listUrl.add(originalUrl);
		/*return strRep.getText().toString();*/
		return listUrl;
	}
	public BitlyUrlDo deserializeBitlyDo(JsonRepresentation jsonRep) {
		if (jsonRep != null && jsonRep.getSize() != -1) {
			try {
				return JsonDeserializer.deserialize(jsonRep.getJsonObject()
						.toString(), BitlyUrlDo.class);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return new BitlyUrlDo();
	}

/*	@Override
	public MetaDO checkPermissionsForClasspage(String classpageId)
			throws GwtException {
		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.CHECK_CLASSPAGE_PERMISSIONS_BY_ID, classpageId, getLoggedInSessionToken());
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		jsonRep =jsonResponseRep.getJsonRepresentation();
		return deserializePermissions(jsonRep);
	}
*/
	private MetaDO deserializePermissions(JsonRepresentation jsonRep) {
		if (jsonRep !=null && jsonRep.getSize() != -1){
			try{
				return JsonDeserializer.deserialize(jsonRep.getJsonObject().toString(), MetaDO.class);
			}catch(JSONException e){
				
			}
		}
		return new MetaDO();
	}
	
	
	@Override
	public List<ClassPageCollectionDo> getCollectionClasspageAssoc(String collectionId)
			throws GwtException {
		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_GET_CLASSPAGE_TITLES, collectionId, getLoggedInSessionToken());
		JsonResponseRepresentation jsonResponseRep =ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		jsonRep =jsonResponseRep.getJsonRepresentation();
		return deserializeClasses(jsonRep);
	}
	
	public List<ClassPageCollectionDo> deserializeClasses(JsonRepresentation jsonRep) {
		try {
			if (jsonRep != null && jsonRep.getSize() != -1) {
				return JsonDeserializer.deserialize(jsonRep.getJsonArray()
						.toString(), new TypeReference<List<ClassPageCollectionDo>>() {
				});
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		return new ArrayList<ClassPageCollectionDo>();
	}

	@Override
	public void deleteCollectionAssocInAssignment(String collectionId)
			throws GwtException {
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_DELETE_COLLECTION, collectionId,
				getLoggedInSessionToken());
		ServiceProcessor.delete(url, getRestUsername(), getRestPassword());
	}
	
	public ArrayList<ClasspageDo> getMyClassPages(String limit, String offset){
		JsonRepresentation jsonRep = null;
		ClasspageDo classpageDo=null;
		ArrayList<ClasspageDo> classPagesList=new ArrayList<ClasspageDo>();
		String url = UrlGenerator.generateUrl(getRestEndPoint(),UrlToken.V2_LIST_MY_CLASSPAGES, getLoggedInSessionToken(), limit, offset);
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(),getRestPassword());
		jsonRep =jsonResponseRep.getJsonRepresentation();
		if(jsonResponseRep.getStatusCode()==200){
			classPagesList=deserializeClassPages(jsonRep);
		}else{
			 classpageDo=new ClasspageDo();
			 classpageDo.setStatusCode(jsonResponseRep.getStatusCode());
			 classPagesList.add(classpageDo);
		}
		return classPagesList;
	}
	public CollectionDo createClassPage(String classPageTitle){
		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(),UrlToken.CREATE_CLASSPAGE_V2, getLoggedInSessionToken());
		JSONObject classPageJsonObject=new JSONObject();
		JSONObject collectionTypeJsonObject=new JSONObject();
		try {
			collectionTypeJsonObject.put(COLLECTIONTYPE, CLASSPAGE);
			collectionTypeJsonObject.put(TITLE, classPageTitle);
			classPageJsonObject.put(CLASSPAGE, collectionTypeJsonObject);
			JsonResponseRepresentation jsonResponseRep =ServiceProcessor.post(url, getRestUsername(), getRestPassword(),classPageJsonObject.toString());
			jsonRep=jsonResponseRep.getJsonRepresentation();
		} catch (JSONException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return deserializeCollection(jsonRep);
	}
	public ClasspageDo getClasspage(String classpageId) throws ServerDownException{
		JsonRepresentation jsonRep = null;
		ClasspageDo classPageDo=new ClasspageDo();
		if(classpageId != null)
		{
		String url = UrlGenerator.generateUrl(getRestEndPoint(),UrlToken.V2_GET_CLASSPAGE_BY_ID, classpageId,getLoggedInSessionToken());
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(),getRestPassword());
		if(jsonResponseRep.getStatusCode()==200){
			jsonRep =jsonResponseRep.getJsonRepresentation();
			try {
				if(jsonRep.getText()!=null){
					if(jsonRep.getText().trim() != null && !jsonRep.getText().trim().equals("null")&&!jsonRep.getText().trim().equals("")){
						classPageDo=deserializeClassPage(jsonRep.getJsonObject());
					}
					else {
						classPageDo=new ClasspageDo();
					}
				} else {
					classPageDo=new ClasspageDo();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			 classPageDo=new ClasspageDo();
		}
		classPageDo.setStatusCode(jsonResponseRep.getStatusCode());
		}
		else{
			 classPageDo=new ClasspageDo();
		}
		return classPageDo;
	}
	
	public ClasspageItemDo createClassPageItem(String classpageId,String collectionId,String dueDate,String direction){
		ClasspageItemDo classpageItemDo=new ClasspageItemDo();
		String url = UrlGenerator.generateUrl(getRestEndPoint(),UrlToken.CREATE_CLASSPAGE_ITEM_V2, classpageId,getLoggedInSessionToken());
		System.out.println("createClassPageItem::"+url);
		JSONObject classPageItemJsonObject=createClasspageJsonObject( collectionId, direction, dueDate,null);
		System.out.println("classPageItemJsonObject::"+classPageItemJsonObject.toString());
		JsonResponseRepresentation jsonResponseRep =ServiceProcessor.post(url, getRestUsername(), getRestPassword(),classPageItemJsonObject.toString());
		if(jsonResponseRep.getStatusCode()==200){
			try{
				classpageItemDo=deserializeClassPageItem(jsonResponseRep.getJsonRepresentation().getJsonObject(),RESOURCE);
			}catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return classpageItemDo;
	}
	@Override
	public ArrayList<ClasspageItemDo> assignItemToClass(String classpageId,String collectionOrFolderId,String dueDate,String direction){
		ArrayList<ClasspageItemDo> classpageItemDoList=new ArrayList<ClasspageItemDo>();
		try {
			if(direction != null)
			{
			direction = URLEncoder.encode(direction, "UTF-8");
			}
			else
			{
				direction = "";
			}
			
			if(dueDate == null)
			{
				dueDate = "";
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String url = UrlGenerator.generateUrl(getRestEndPoint(),UrlToken.ASSIGN_ITEM_TO_CLASS, classpageId,collectionOrFolderId,getLoggedInSessionToken(),direction,dueDate);
		JsonResponseRepresentation jsonResponseRep =ServiceProcessor.post(url, getRestUsername(), getRestPassword());
		if(jsonResponseRep.getStatusCode()==200){
			classpageItemDoList=deserializeClassItems(jsonResponseRep.getJsonRepresentation());
		}
		return classpageItemDoList;
	}
	
	
	public ArrayList<ClasspageItemDo> getClassPageItems(String classpageId,String offset,String limit,String sortingOrder,String studyStatus){
		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(),UrlToken.GET_CLASSPAGE_ITEMS_V2, classpageId,getLoggedInSessionToken(),offset,limit);

		
		if(sortingOrder!=null){
			if(sortingOrder.equalsIgnoreCase("asce")){
				sortingOrder="sequence";
			}else if(sortingOrder.equalsIgnoreCase("desc")){
				sortingOrder="sequence-desc";
			}else if(sortingOrder.equalsIgnoreCase("recent")){

				sortingOrder="recent";
			}else if(sortingOrder.equalsIgnoreCase("duedate")){
				sortingOrder="due-date";

			}
			else if(sortingOrder.equalsIgnoreCase("duedate")){
				sortingOrder="due-date";
			}
			else if(sortingOrder.equalsIgnoreCase("all"))
			{
				sortingOrder="sequence";
			}
			else if(sortingOrder.equalsIgnoreCase("completed"))
			{
				sortingOrder="sequence";
				studyStatus = "completed";
			}
			else if(sortingOrder.equalsIgnoreCase("todo"))
			{
				sortingOrder="sequence";
				studyStatus = "open";
			}else if(sortingOrder.equalsIgnoreCase("earliest")){ // getting the assignments in ascending order based on due date.
				sortingOrder="due-date";
			}else if(sortingOrder.equalsIgnoreCase("latest")){  // getting the assignments in descending order based on due date.
				sortingOrder="due-date-earliest";
			}
			url=url+"&orderBy="+sortingOrder;
		}else{
			url=url+"&orderBy=sequence";
		}
			if(studyStatus!=null){
				url=url+"&status="+studyStatus;
			}
			System.out.println("get url====>"+url);
		JsonResponseRepresentation jsonResponseRep =ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		
		if(jsonResponseRep.getStatusCode()==200){
			jsonRep=jsonResponseRep.getJsonRepresentation();
			return deserializeClassPageItems(jsonRep);
		}else{
			return new ArrayList<ClasspageItemDo>();
		}
	}
	
	public String updateClasspageItem(String classpageItemId,String direction,String dueDate,String readStatus){
		String url = UrlGenerator.generateUrl(getRestEndPoint(),UrlToken.UPDATE_CLASSPAGE_ITEMS_V2, classpageItemId,getLoggedInSessionToken());
		JSONObject classPageItemJsonObject=createClasspageJsonObject(null, direction, dueDate,readStatus);
		JsonResponseRepresentation jsonResponseRep =ServiceProcessor.put(url, getRestUsername(), getRestPassword(),classPageItemJsonObject.toString());
		return jsonResponseRep.getStatusCode().toString();
	}
	
	public String deleteClassPageItem(String collectionId){
		String url = UrlGenerator.generateUrl(getRestEndPoint(),UrlToken.DELETE_CLASSPAGE_ITEMS_V2, collectionId,getLoggedInSessionToken());
		JsonResponseRepresentation jsonResponseRep =ServiceProcessor.delete(url, getRestUsername(), getRestPassword());
		return jsonResponseRep.getStatusCode().toString();
	}
	
	public ArrayList<String> getCollectionParentFolders(String collectionId){
		String url = UrlGenerator.generateUrl(getRestEndPoint(),UrlToken.GET_COLLECTION_PARENT_FOLDERS, collectionId,getLoggedInSessionToken());
		JsonResponseRepresentation jsonResponseRep =ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		if(jsonResponseRep.getStatusCode()==200){
			JsonRepresentation jsonRepresentation=jsonResponseRep.getJsonRepresentation();
			try{
				ArrayList<String> folderList=new ArrayList<String>();
				JSONArray foldersArray=jsonRepresentation.getJsonArray();
				if(foldersArray!=null&&foldersArray.length()>0){
					for(int i=0;i<foldersArray.length();i++){
						folderList.add(foldersArray.getString(i));
					}
				}
				return folderList;
			}catch(Exception e){
				
			}
		}
		return null;
	}
	
	@Override
	public ClasspageItemDo getClassPageItem(String classItemId) {
		ClasspageItemDo classpageItemDo=new ClasspageItemDo();
		String url = UrlGenerator.generateUrl(getRestEndPoint(),UrlToken.V2_GET_CLASSPAGE_COLL_DETAILS, classItemId,getLoggedInSessionToken());
		JsonResponseRepresentation jsonResponseRep =ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		if(jsonResponseRep.getStatusCode()==200){
			try{
				classpageItemDo=deserializeClassPageItem(jsonResponseRep.getJsonRepresentation().getJsonObject(),RESOURCE);
				JSONObject classPageJsonObject=jsonResponseRep.getJsonRepresentation().getJsonObject().isNull(COLLECTION)?null:jsonResponseRep.getJsonRepresentation().getJsonObject().getJSONObject(COLLECTION);
				if(classPageJsonObject!=null){
					classpageItemDo.setTitle(classPageJsonObject.isNull(TITLE)?"":classPageJsonObject.getString(TITLE));
					classpageItemDo.setClasspageId(classPageJsonObject.isNull(GOORUOID)?"":classPageJsonObject.getString(GOORUOID));
					JSONObject userJsonObject=classPageJsonObject.isNull(USER)?null:classPageJsonObject.getJSONObject(USER);
					if(userJsonObject!=null){
						classpageItemDo.setUserNameDispaly(userJsonObject.isNull(USERNAMEWDISPLAY)?"":userJsonObject.getString(USERNAMEWDISPLAY));
						classpageItemDo.setProfileImageUrl(userJsonObject.isNull(PROFILEIMAGEURL)?"":userJsonObject.getString(PROFILEIMAGEURL));
					}
				}
			}catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return classpageItemDo;
	}
	
	protected JSONObject createClasspageJsonObject(String collectionId,String direction,String dueDate,String status){
		JSONObject classPageItemJsonObject=new JSONObject();
		JSONObject collectionJsonObject=new JSONObject();
		try {
			collectionJsonObject.put(ITEMTYPE, ADDED);
			if(direction!=null){
				collectionJsonObject.put(NARRATION, direction);
			}
			if(dueDate!=null){
				collectionJsonObject.put(PLANNEDENDDATE, dueDate);
			}
			if(status!=null){
				collectionJsonObject.put(STATUS, status);
			}
			classPageItemJsonObject.put(COLLECTIONITEM, collectionJsonObject);
			if(collectionId!=null){
				classPageItemJsonObject.put(COLLECTIONID, collectionId);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return classPageItemJsonObject;
	}
	protected ArrayList<ClasspageDo> deserializeClassPages(JsonRepresentation jsonRep){
		ArrayList<ClasspageDo> classpagesList=new ArrayList<ClasspageDo>();
		try {
			if(jsonRep!=null&&jsonRep.getJsonObject()!=null){
				JSONObject classpagesJsonObj=jsonRep.getJsonObject();
				int totalHitCount=classpagesJsonObj.getInt(TOTALHITCOUNT);
				JSONArray classPagesArray=classpagesJsonObj.getJSONArray(SEARCHRESULTS);
				if(classPagesArray!=null&&classPagesArray.length()>0){
					for(int i=0;i<classPagesArray.length();i++){
						ClasspageDo classpageDo=deserializeClassPage(classPagesArray.getJSONObject(i));
						classpageDo.setStatusCode(200);
						classpageDo.setTotalHitCount(totalHitCount);
						classpagesList.add(classpageDo);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return classpagesList;
	}
	protected ClasspageDo deserializeClassPage(JSONObject classpageJsonObject){
		ClasspageDo classpageDo=new ClasspageDo();
			try {
				if(classpageJsonObject!=null){
					classpageDo.setClasspageId(classpageJsonObject.getString(GOORUOID));
					classpageDo.setClasspageCode(classpageJsonObject.getString(CLASSPAGECODE));
					classpageDo.setTitle(classpageJsonObject.getString(TITLE));
					if(!classpageJsonObject.isNull(ITEMCOUNT))
					{
					classpageDo.setItemCount(classpageJsonObject.getString(ITEMCOUNT));
					}
					else
					{
					classpageDo.setItemCount("0");
					}
					classpageDo.setThumbnailUrl(classpageJsonObject.getJSONObject(THUMBNAIL)!=null?classpageJsonObject.getJSONObject(THUMBNAIL).getString(THUMBNAILURL):"");
					ArrayList<String> permissionList=new ArrayList<String>();
					if(!classpageJsonObject.isNull(CREATOR)){
						if(!classpageJsonObject.getJSONObject(CREATOR).isNull(UID)){
							classpageDo.setCreatorId(classpageJsonObject.getJSONObject(CREATOR).getString(UID));
						}
						if(!classpageJsonObject.getJSONObject(CREATOR).isNull(USERNAME)){
							classpageDo.setCreatorUsername(classpageJsonObject.getJSONObject(CREATOR).getString(USERNAME));
						}
						if(!classpageJsonObject.getJSONObject(CREATOR).isNull(PROFILEIMAGE)){
							classpageDo.setCreatorProfileImage(classpageJsonObject.getJSONObject(CREATOR).getString(PROFILEIMAGE)+"?p="+UUID.randomUUID());
						}
					}
					if(!classpageJsonObject.isNull(META)){
						if(!classpageJsonObject.getJSONObject(META).isNull(STATUS)){
							classpageDo.setStatus(classpageJsonObject.getJSONObject(META).getString(STATUS));
						}
						if(!classpageJsonObject.getJSONObject(META).isNull(MEMBERCOUNT)){
							classpageDo.setMemberCount(classpageJsonObject.getJSONObject(META).getString(MEMBERCOUNT));
						}
						if(!classpageJsonObject.getJSONObject(META).isNull(PERMISSIONS)){
							JSONArray permissionsArray=classpageJsonObject.getJSONObject(META).getJSONArray(PERMISSIONS);
							if(permissionsArray!=null&&permissionsArray.length()>0){
								for(int i=0;i<permissionsArray.length();i++){
									permissionList.add(permissionsArray.getString(i));
								}
							}
						}
					}
					classpageDo.setSharing(classpageJsonObject.getString(SHARING));
					classpageDo.setPermissions(permissionList);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				classpageDo=new ClasspageDo();
			}
		return classpageDo;
		
	}
	protected ArrayList<ClasspageItemDo> deserializeClassPageItems(JsonRepresentation jsonRep){
		ArrayList<ClasspageItemDo> classpageItemsList=new ArrayList<ClasspageItemDo>();
			try {
				if(jsonRep!=null){
					JSONObject classPageJsonObject=jsonRep.getJsonObject();
					if(classPageJsonObject!=null){
						JSONArray classpageItemsArray=classPageJsonObject.getJSONArray(SEARCHRESULTS);
						int totalHitCount=classPageJsonObject.getInt(TOTALHITCOUNT);
						if(classpageItemsArray!=null&&classpageItemsArray.length()>0){
							for(int i=0;i<classpageItemsArray.length();i++){
								JSONObject classpageItemJsonObject=classpageItemsArray.getJSONObject(i);
								ClasspageItemDo classpageItemDo=deserializeClassPageItem(classpageItemJsonObject,RESOURCE);
								classpageItemDo.setTotalHitCount(totalHitCount);
								classpageItemsList.add(classpageItemDo);
							}
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		return classpageItemsList;
	}
	protected ArrayList<ClasspageItemDo> deserializeClassItems(JsonRepresentation jsonRep ){
		ArrayList<ClasspageItemDo> classpageItemsList=new ArrayList<ClasspageItemDo>();
			try {
				if(jsonRep!=null){
					JSONArray classpageItemsArray=jsonRep.getJsonArray();
					if(classpageItemsArray!=null&&classpageItemsArray.length()>0){
						for(int i=0;i<classpageItemsArray.length();i++){
							JSONObject classpageItemJsonObject=classpageItemsArray.getJSONObject(i);
							/*if(direction!=null){
								classpageItemJsonObject.put(NARRATION, direction);
							}
							if(dueDate!=null){
								classpageItemJsonObject.put(PLANNEDENDDATE, dueDate);
							}*/
							ClasspageItemDo classpageItemDo=deserializeClassPageItem(classpageItemJsonObject,RESOURCE);
							classpageItemsList.add(classpageItemDo);
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		return classpageItemsList;
	}
	public ClasspageItemDo deserializeClassPageItem(JSONObject classpageItemJsonObject,String resourceType){
		ClasspageItemDo classpageItemDo=new ClasspageItemDo();
		try{
			classpageItemDo.setCollectionItemId(classpageItemJsonObject.getString(COLLECTIONITEMID));
			classpageItemDo.setDirection(classpageItemJsonObject.isNull(NARRATION)?null:classpageItemJsonObject.getString(NARRATION));
			classpageItemDo.setStatus(classpageItemJsonObject.isNull(STATUS)?null:classpageItemJsonObject.getString(STATUS));
			classpageItemDo.setSequenceNumber(classpageItemJsonObject.isNull(ITEMSEQUENCE)?0:classpageItemJsonObject.getInt(ITEMSEQUENCE));
			classpageItemDo.setPlannedEndDate(convertMilliSecondsToDate(classpageItemJsonObject.isNull(PLANNEDENDDATE)?null:classpageItemJsonObject.getLong(PLANNEDENDDATE)));
			JSONObject resourceJsonObject=classpageItemJsonObject.isNull(resourceType)?null:classpageItemJsonObject.getJSONObject(resourceType);
			if(resourceJsonObject!=null){
				classpageItemDo.setCollectionTitle(resourceJsonObject.getString(TITLE));
				classpageItemDo.setGoal(resourceJsonObject.isNull(GOALS)?null:resourceJsonObject.getString(GOALS));
				classpageItemDo.setCollectionId(resourceJsonObject.getString(GOORUOID));
				classpageItemDo.setThumbnailUrl(resourceJsonObject.getJSONObject(THUMBNAIL)!=null?resourceJsonObject.getJSONObject(THUMBNAIL).getString(THUMBNAILURL):"");
			}
		}catch (JSONException e) {
			e.printStackTrace();
		}
		return classpageItemDo;
	}
	public String convertMilliSecondsToDate(Long milliseconds){
		if(milliseconds!=null&&milliseconds!=0){
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			Date resultdate = new Date(milliseconds);
			return sdf.format(resultdate);
		}else{
			return null;
		}
	}
	/**
	 * 
	 */
	@Override
	public ArrayList<ClassPageCollectionDo> getClasspagesListByCollectionId(String collectionId, String collabUId){
		JsonRepresentation jsonRep = null;
		ArrayList<ClassPageCollectionDo> lstClasspageTitle=new  ArrayList<ClassPageCollectionDo>();
		String url = UrlGenerator.generateUrl(getRestEndPoint(),UrlToken.V2_LIST_CLASSPAGES_BY_USER_ID,collectionId, collabUId, getLoggedInSessionToken());
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(),getRestPassword());
		jsonRep =jsonResponseRep.getJsonRepresentation();
		if(jsonResponseRep.getStatusCode()==200){
			lstClasspageTitle=deserializeClassPagesList(jsonRep);
		}else{
			
		}
		return lstClasspageTitle;
	}

	protected ArrayList<ClassPageCollectionDo> deserializeClassPagesList(JsonRepresentation jsonRep){
		try {
			if (jsonRep != null && jsonRep.getSize() != -1) {
				return JsonDeserializer.deserialize(jsonRep.getJsonArray()
						.toString(), new TypeReference< ArrayList<ClassPageCollectionDo>>() {
				});
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new  ArrayList<ClassPageCollectionDo>();
	}
	
	/* (non-Javadoc)
	 * @see org.ednovo.gooru.client.service.ClasspageService#getCollectionUsedCount(java.lang.String)
	 */
	@Override
	public Integer getCollectionUsedCount(String collectionId){
		int count=0;
		JsonResponseRepresentation jsonResponseRep = null;
		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(),
				UrlToken.V2_COLLECTION_USED_COUNT, collectionId,
				getLoggedInSessionToken());
		jsonResponseRep = ServiceProcessor.get(url, getRestUsername(),
				getRestPassword());
		
		jsonRep =jsonResponseRep.getJsonRepresentation();
		
		if (jsonResponseRep.getStatusCode()==200){
			try{
				count = Integer.parseInt(jsonRep.getText().trim());
			}catch(Exception e){
				count=0;
			}
		}
		
		return count;
	}

	/* (non-Javadoc)
	 * @see org.ednovo.gooru.client.service.ClasspageService#inviteStudentToClass(java.lang.String)
	 */
	@Override
	public ArrayList<CollaboratorsDo> inviteStudentToClass(String classId, List<String> lstEmailId) throws GwtException {
		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(),
				UrlToken.V2_INVITE_STUDENT_TO_CLASS, classId, getLoggedInSessionToken());
		String formData = lstEmailId.toString();
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.post(url, getRestUsername(),
				getRestPassword(), formData);
		jsonRep =jsonResponseRep.getJsonRepresentation();
		return deserializeMembers(jsonRep);
	}
	/**
	 * 
	 * @function deserializeCollaboratorsList 
	 * 
	 * @created_date : Jan 27, 2014
	 * 
	 * @description
	 * 
	 * 
	 * @param jsonRep
	 * @return
	 * 
	 * @return : ArrayList<CollaboratorsDo>
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	public  ArrayList<CollaboratorsDo> deserializeMembers(JsonRepresentation jsonRep) {
		try {
			if (jsonRep != null && jsonRep.getSize() != -1) {
				return JsonDeserializer.deserialize(jsonRep.getJsonArray()
						.toString(), new TypeReference<ArrayList<CollaboratorsDo>>() {
				});
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new ArrayList<CollaboratorsDo>();
	}
	@Override
	public StudentsAssociatedListDo getAssociatedStudentListByCode(String classCode,  int offSet, int pageSize, String statusType)
			throws GwtException {
		
		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_GET_MEMBER_LIST_BY_CODE, classCode, getLoggedInSessionToken(), statusType, ""+pageSize, offSet+"");
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		jsonRep =jsonResponseRep.getJsonRepresentation();
		
		return deserializeStudentsList(jsonRep);
	}
	
	@Override
	public void removeStudentFromClass(String classCode, String type, String emailIds)
			throws GwtException {

		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_DELETE_MEMBER_FROM_CLASS_BY_CODE, classCode, getLoggedInSessionToken());
		String formData = emailIds;
		url = url + "&data=" + formData;
		ServiceProcessor.delete(url, getRestUsername(), getRestPassword());
		
//		JsonRepresentation jsonRepGetClass = null;
//		String urlGetClass = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_GET_MEMBER_LIST_BY_CODE, classCode, getLoggedInSessionToken());
//		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(urlGetClass, getRestUsername(), getRestPassword());
//		jsonRepGetClass =jsonResponseRep.getJsonRepresentation();
		
//		return deserializeMembersList(jsonRepGetClass);
	}
	/**
	 * 
	 * @function deserializeCollaborators 
	 * 
	 * @created_date : Jan 27, 2014
	 * 
	 * @description
	 * 
	 * 
	 * @param jsonRep
	 * @return
	 * 
	 * @return : Map<String,ArrayList<CollaboratorsDo>>
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	public Map<String, ArrayList<CollaboratorsDo>> deserializeMembersList(JsonRepresentation jsonRep) {
		try {
			if (jsonRep != null && jsonRep.getSize() != -1) {
				return JsonDeserializer.deserialize(jsonRep.getJsonObject()
						.toString(), new TypeReference<Map<String, ArrayList<CollaboratorsDo>>>() {
				});
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new HashMap<String, ArrayList<CollaboratorsDo>>();
	}
	
	public StudentsAssociatedListDo deserializeStudentsList(JsonRepresentation jsonRep){
		try {
			if (jsonRep != null && jsonRep.getSize() != -1) {
				return JsonDeserializer.deserialize(jsonRep.getJsonObject()
						.toString(), new TypeReference< StudentsAssociatedListDo>() {
				});
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new StudentsAssociatedListDo();
	}

	@Override
	public ClasspageDo studentJoinIntoClass(String classCode, String emailId )
			throws GwtException {
		JsonRepresentation jsonRep = null;
		ClasspageDo classpageDo=null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(),UrlToken.V2_JOIN_CLASS, classCode, getLoggedInSessionToken());
		ArrayList<String> listOfEmail= new ArrayList<String>();
		try {
			listOfEmail.add("\"" + emailId + "\"");
			JsonResponseRepresentation jsonResponseRep =ServiceProcessor.post(url, getRestUsername(), getRestPassword(),listOfEmail.toString());
			jsonRep=jsonResponseRep.getJsonRepresentation();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classpageDo;
	}
	
	@Override
	public List<String> getSuggestionByName(String emailId){
		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(),
				UrlToken.V2_SUGGEST_MEMBER, emailId, getLoggedInSessionToken());
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		
		jsonRep =jsonResponseRep.getJsonRepresentation();
		return deserializeSuggestList(jsonRep);
	}
	
	
	public  List<String> deserializeSuggestList(JsonRepresentation jsonRep) {
		try {
			if (jsonRep != null && jsonRep.getSize() != -1) {
				return JsonDeserializer.deserialize(jsonRep.getJsonArray()
						.toString(), new TypeReference<List<String>>() {
				});
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}
	
	@Override
	public ProfilePageDo v2GetClassPartyCustomField(String gooruUid) throws GwtException {
		ProfilePageDo profilePageDo = new ProfilePageDo();
		String userUid = getLoggedInUserUid();
/*		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.GET_CLASSPARTY_CUSTOMFIELD, userUid, getLoggedInSessionToken());
		JsonRepresentation jsonRep = null;
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		jsonRep = jsonResponseRep.getJsonRepresentation();
		try {
			profilePageDo = JsonDeserializer.deserialize(jsonRep.getJsonObject().toString(), ProfilePageDo.class);
		} catch (JSONException e) {
			e.printStackTrace();
		} */
		return profilePageDo;
	}

	/* (non-Javadoc)
	 * @see org.ednovo.gooru.client.service.ClasspageService#getActiveAssociatedStudentListByCode(java.lang.String, int, int, java.lang.String)
	 */
	@Override
	public StudentsAssociatedListDo getActiveAssociatedStudentListByCode(
			String classCode, int offSet, int pageSize, String statusType)
			throws GwtException {
		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_GET_MEMBER_LIST_BY_CODE, classCode, getLoggedInSessionToken(), statusType, ""+pageSize, offSet+"");
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		jsonRep =jsonResponseRep.getJsonRepresentation();
		
		return deserializeStudentsList(jsonRep);
	}
	
	@Override
	public ClasspageListDo v2GetAllClass(String limit, String offSet) throws GwtException {

		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(),
				UrlToken.TEACH_STUDY, getLoggedInSessionToken(), limit, offSet);
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(),
				getRestPassword());
		jsonRep =jsonResponseRep.getJsonRepresentation();
		return deserializeClasspageList(jsonRep);
	}
	
	@Override
	public void v2ChangeAssignmentSequence(String classpageId, String classpageAssignmentId, int sequence) throws GwtException {
		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.UPDATE_ASSIGNMENT_SEQUENCE, classpageAssignmentId, String.valueOf(sequence), getLoggedInSessionToken());
		
		try{
			JSONObject jsonObject=new JSONObject();
			JSONObject titleJsonObject= new JSONObject();
			jsonObject.put(CLASSPAGE, titleJsonObject);
			
			JsonResponseRepresentation jsonResponseRep = ServiceProcessor.put(url, getRestUsername(), getRestPassword(), jsonObject.toString());
			jsonRep =jsonResponseRep.getJsonRepresentation();
		}catch(Exception e){
			
		}
	}	
}

