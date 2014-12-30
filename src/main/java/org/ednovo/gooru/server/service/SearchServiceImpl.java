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
 */
package org.ednovo.gooru.server.service;


import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.service.SearchService;
import org.ednovo.gooru.server.annotation.ServiceURL;
import org.ednovo.gooru.server.deserializer.AutoCompleteDeSerializer;
import org.ednovo.gooru.server.deserializer.AutoSearchKeyWordDeSerializer;
import org.ednovo.gooru.server.deserializer.CollectionItemsResultDeSerializer;
import org.ednovo.gooru.server.deserializer.CollectionSearchResultDeSerializer;
import org.ednovo.gooru.server.deserializer.ResourceSearchResultDeSerializer;
import org.ednovo.gooru.server.deserializer.SearchFilterDeSerialier;
import org.ednovo.gooru.server.deserializer.ShareDeSerializer;
import org.ednovo.gooru.server.request.JsonResponseRepresentation;
import org.ednovo.gooru.server.request.ServiceProcessor;
import org.ednovo.gooru.server.request.ShareUrlToken;
import org.ednovo.gooru.server.request.UrlToken;
import org.ednovo.gooru.server.serializer.JsonDeserializer;
import org.ednovo.gooru.shared.exception.GwtException;
import org.ednovo.gooru.shared.exception.ServerDownException;
import org.ednovo.gooru.shared.model.code.CodeDo;
import org.ednovo.gooru.shared.model.code.StandardsLevel1DO;
import org.ednovo.gooru.shared.model.code.StandardsLevel2DO;
import org.ednovo.gooru.shared.model.code.StandardsLevel3DO;
import org.ednovo.gooru.shared.model.code.StandardsLevel4DO;
import org.ednovo.gooru.shared.model.library.ProfileLibraryListDo;
import org.ednovo.gooru.shared.model.search.AutoSuggestKeywordSearchDo;
import org.ednovo.gooru.shared.model.search.CollectionItemSearchResultDo;
import org.ednovo.gooru.shared.model.search.CollectionSearchResultDo;
import org.ednovo.gooru.shared.model.search.ResourceSearchResultDo;
import org.ednovo.gooru.shared.model.search.SearchDo;
import org.ednovo.gooru.shared.model.search.SearchFilterDo;
import org.ednovo.gooru.shared.model.search.SearchResourcesTagsDo;
import org.ednovo.gooru.shared.util.StringUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.restlet.ext.json.JsonRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * @author Search Team
 * 
 */
@Service("searchService")
@ServiceURL("/searchService")
public class SearchServiceImpl extends BaseServiceImpl implements SearchService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4286188874651640611L;
	
	private static final String RESOURCE = "resource";
	
	private static final String SCOLLECTION = "scollection";
	
	private static final String SINGLE = "single";
	
	private static final String REAL_URL = "realUrl";
	
	private static final String STANDARD = "standard";
	
	private static final String TRUE = "true";
	
	private static final String MY_STRING = "my";
	
	private static final String TYPE = "type";
	
	private static final String SHARETYPE="shareType";

	private static final String CLASSPAGE = "classpage";
	
	private static final String SHORTEN_URL = "shortenUrl";
	
	private static final String FLT_CODE_ID = "flt.codeId";
	
	private static final String COURSE_CODE_ID = "id";
	
	private static final String FLT_SOURCE_CODE_ID = "flt.sourceCodeId";
	
	private static final String COLLECTION_EDIT_EVENT ="collection-edit";
	
	private static final String HTTPS = "https";
	
	private static final String HTTP = "http";
	
	@Autowired
	private CollectionSearchResultDeSerializer collectionSearchResultDeSerializer;

	@Autowired
	private ResourceSearchResultDeSerializer resourceSearchResultDeSerializer;
	
	@Autowired
	private CollectionItemsResultDeSerializer collectionItemsResultDeSerializer;

	@Autowired
	private AutoCompleteDeSerializer autoCompleteDeSerializer;

	@Autowired
	private SearchFilterDeSerialier searchFilterDeSerialier;

	@Autowired
	private ShareDeSerializer shareDeSerializer;
	
	@Autowired
	private AutoSearchKeyWordDeSerializer autoSearchKeyWordDeSerializer;
	
	String  query;
	String collectionQuery;
	@Override
	public SearchFilterDo getSearchFilters(String type) {
		if (type.equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)) {
			type = RESOURCE;
		} else {
			type = SCOLLECTION;
		}
		JsonRepresentation jsonRep=null;
		String url = UrlGenerator.generateUrl(getSearchEndPoint(), UrlToken.SEARCH_FILTER, getLoggedInSessionToken(), type);
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getSearchUsername(), getSearchPassword());
		jsonRep=jsonResponseRep.getJsonRepresentation();
		return searchFilterDeSerialier.deserializeSearchFilter(jsonRep, type);
	}

	@Override
	public SearchDo<ResourceSearchResultDo> getResourceSearchResults(SearchDo<ResourceSearchResultDo> searchDo) {
		SearchDo<ResourceSearchResultDo> searchDOEmpty = new SearchDo<ResourceSearchResultDo>();
		String query1=searchDo.getSearchQuery();
	
		 query= query1;
		try{
			if(searchDo.getFilters()!=null){
				for (String key : searchDo.getFilters().keySet()) {
				  String value = searchDo.getFilters().get(key);
				  value = value.replaceAll("&", "%26");
				  searchDo.getFilters().put(key, value);
				 }
			}
			if(query.equalsIgnoreCase("'*'"))
			{
				query = "*";
			}
			
		JsonRepresentation jsonRep=null;
		Map<String,String> filtersMap=searchDo.getFilters();
		if(filtersMap!=null){
	        String category=filtersMap.get("category");
	        if(category!=null&&category.equalsIgnoreCase("All")){
	                filtersMap.remove("category");
	        }
	        else if(category!=null){
	        	if(category.equalsIgnoreCase("Website")){
	               	category=category.replaceAll("Website", "webpage");
	                filtersMap.remove("category");
	                filtersMap.put("flt.resourceFormat",category);
	        	}
	        	else {
	        		 filtersMap.remove("category");
	                 filtersMap.put("flt.resourceFormat",category);
	        	}
	        }
		}
		
		String url = UrlGenerator.generateUrl(getSearchEndPoint(), UrlToken.RESOURCE_SEARCH, filtersMap, getLoggedInSessionToken(), query, searchDo.getPageNum() + "", searchDo.getPageSize() + "", SINGLE, "false", TRUE, TRUE);
		if(getSearchEndPoint().contains(HTTPS)){
			url = appendHttpsURL(url);
		}
		System.out.println("getResourceSearchResults:::"+url);
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getSearchUsername(), getSearchPassword());
		jsonRep=jsonResponseRep.getJsonRepresentation();
		try{
			resourceSearchResultDeSerializer.deserialize(jsonRep, searchDo);	
		}
		catch(Exception e)
		{
			
		}
		return searchDo;
		}catch(Exception e){
		}
		return searchDOEmpty;
	}
	
	private String appendHttpsURL(String url) {
		url = url+"&protocolSupported=http,https";
		return url;
	}

	@Override
	public SearchDo<CollectionSearchResultDo> getCollectionSearchResults(SearchDo<CollectionSearchResultDo> searchDo) {
		SearchDo<CollectionSearchResultDo> searchDOEmpty = new SearchDo<CollectionSearchResultDo>();
		String query1=searchDo.getSearchQuery();
		collectionQuery= query1 ;
		try{
			if(searchDo.getFilters()!=null){
			for (String key : searchDo.getFilters().keySet()) {
				  String value = searchDo.getFilters().get(key);
				  value=value.replaceAll("&", "%26");
				  searchDo.getFilters().put(key, value);
			}
		 }
		JsonRepresentation jsonRep=null;
		/*if(collectionQuery!=null){
			if(collectionQuery.contains("252")){
				collectionQuery=collectionQuery.replaceAll("%","").replaceAll("2", "").replaceAll("5", "").replaceAll("B", "");
				collectionQuery=collectionQuery.trim();
				collectionQuery=collectionQuery.replaceAll(" ","%20");
			}
		}*/
		if(collectionQuery.equalsIgnoreCase("'*'"))
		{
			collectionQuery = "*";
		}
		String url = UrlGenerator.generateUrl(getSearchEndPoint(), UrlToken.SIMPLE_COLLECTION_SEARCH, searchDo.getFilters(), getLoggedInSessionToken(), collectionQuery, searchDo.getPageNum() + "", searchDo.getPageSize() + "", MY_STRING);
		if(getSearchEndPoint().contains(HTTPS)){
			url = appendHttpsURL(url);
		}
		System.out.println("getCollectionSearchResults.."+url);
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getSearchUsername(), getSearchPassword());
		jsonRep=jsonResponseRep.getJsonRepresentation();
		collectionSearchResultDeSerializer.deserialize(jsonRep, searchDo);
		return searchDo;
		}catch(Exception e){
		}
		return searchDOEmpty;
	}

	/*@Override
	public SearchDo<ResourceSearchResultDo> getCollectionResources(SearchDo<ResourceSearchResultDo> searchDo) {
		String url = UrlGenerator.generateUrl(getSearchEndPoint(), UrlToken.COLLECTION_RESOURCE_LIST, getLoggedInSessionToken(), searchDo.getPageNum() + "", searchDo.getPageSize() + "", searchDo.getSearchQuery());
		JsonRepresentation jsonRep = ServiceProcessor.get(url, getSearchUsername(), getSearchPassword());
		resourceSearchResultDeSerializer.deserialize(jsonRep, searchDo);
		return searchDo;
	}*/
	@Override
	public SearchDo<CollectionItemSearchResultDo> getCollectionItems(String collectionId) throws GwtException {
		JsonRepresentation jsonRep=null;
		SearchDo<CollectionItemSearchResultDo> searchDo=new SearchDo<CollectionItemSearchResultDo>();
		String url = UrlGenerator.generateUrl(getSearchEndPoint(), UrlToken.COLLECTION_ITEMS_LIST, collectionId,getLoggedInSessionToken());
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getSearchUsername(), getSearchPassword());
		jsonRep=jsonResponseRep.getJsonRepresentation();
		collectionItemsResultDeSerializer.deserializeCollectionItems(jsonRep, searchDo);
		return searchDo;
	}
	

	@Override
	public SearchDo<CollectionSearchResultDo> getResourceCollections(SearchDo<CollectionSearchResultDo> searchDo) {
		JsonRepresentation jsonRep=null;
		String url = UrlGenerator.generateUrl(getSearchEndPoint(), UrlToken.RESOURCE_COLLECTION_LIST, getLoggedInSessionToken(), searchDo.getPageNum() + "", searchDo.getPageSize() + "", searchDo.getSearchQuery());
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getSearchUsername(), getSearchPassword());
		jsonRep=jsonResponseRep.getJsonRepresentation();
		collectionSearchResultDeSerializer.deserialize(jsonRep, searchDo);
		return searchDo;
	}

	/*@Override
	public SearchDo<String> getSuggestSearchQuery(SearchDo<String> searchDo) {
		String url = UrlGenerator.generateUrl(getSearchEndPoint(), UrlToken.SEARCH_SUGGEST_QUERY, getLoggedInSessionToken(), searchDo.getSearchQuery(), searchDo.getPageSize() + "", searchDo.getPageNum() + "");
		JsonRepresentation jsonRep = ServiceProcessor.get(url, getSearchUsername(), getSearchPassword());
		searchDo.setSearchResults(autoCompleteDeSerializer.deserializeSearchQuery(jsonRep));
		return searchDo;
	}*/

	@Override
	public SearchDo<String> getSuggestSource(SearchDo<String> searchDo) {
		JsonRepresentation jsonRep=null;
		String url = UrlGenerator.generateUrl(getSearchEndPoint(), UrlToken.SEARCH_SUGGEST_SOURCE, getLoggedInSessionToken(), URLEncoder.encode(searchDo.getSearchQuery()), searchDo.getPageSize() + "", searchDo.getPageNum() + "");
		
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getSearchUsername(), getSearchPassword());
		jsonRep=jsonResponseRep.getJsonRepresentation();
		searchDo.setSearchResults(autoCompleteDeSerializer.deserializeSource(jsonRep));
		return searchDo;
	}

	@Override
	public SearchDo<CodeDo> getSuggestStandard(SearchDo<CodeDo> searchDo) {
		JsonRepresentation jsonRep=null;
		String url = UrlGenerator.generateUrl(getSearchEndPoint(), UrlToken.SEARCH_SUGGEST_STANDARD, searchDo.getType() != null ? searchDo.getType() : STANDARD , getLoggedInSessionToken(), searchDo.getSearchQuery(), searchDo.getPageSize() + "", searchDo.getPageNum() + "");
		if(searchDo.getFilters()!=null && searchDo.getFilters().size()>0) {
			url = url + "&"+FLT_CODE_ID+"="+searchDo.getFilters().get(FLT_CODE_ID);
		}
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getSearchUsername(), getSearchPassword());
		jsonRep=jsonResponseRep.getJsonRepresentation();
		searchDo.setSearchResults(autoCompleteDeSerializer.deserializeStandards(jsonRep));
		return searchDo;
	}
	
	@Override
	public SearchDo<CodeDo> getSuggestStandardByFilterCourseId(SearchDo<CodeDo> searchDo) {
		JsonRepresentation jsonRep=null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.SUGGEST_STANDARD_BY_FILTER, getLoggedInSessionToken(), searchDo.getSearchQuery());
		if(searchDo.getFilters()!=null && searchDo.getFilters().size()>0) {
			url = url + "&"+COURSE_CODE_ID+"="+searchDo.getFilters().get(COURSE_CODE_ID);
		}
		System.out.println("getSuggestStandardByFilterCourseId:::::::"+url);
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getSearchUsername(), getSearchPassword());
		jsonRep=jsonResponseRep.getJsonRepresentation();
		searchDo.setSearchResults(autoCompleteDeSerializer.deserializeStandards(jsonRep));
		return searchDo;
	}

	@Override
	public Map<String, String> getShortenShareUrl(String contentGooruOid, Map<String, String> params) {
		JsonRepresentation jsonRep=null;
		Map<String, String> shortenUrl = new HashMap<String, String>();
			if (params.get(TYPE).equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)) {	
				if (params.get(SHARETYPE).equalsIgnoreCase("embed")){
					params.put(REAL_URL, UrlGenerator.generateUrl(getHomeEndPoint() +"/"+ ShareUrlToken.RESOURCE_PLAY_URL.getUrl()+"%26embed=true", contentGooruOid, RESOURCE));
				}else{
					params.put(REAL_URL, UrlGenerator.generateUrl(getHomeEndPoint() +"/"+ ShareUrlToken.RESOURCE_PLAY_URL.getUrl()+"%26share=true", contentGooruOid, RESOURCE));
				}
			}else if(params.get(TYPE).equalsIgnoreCase(PlaceTokens.EDIT_CLASSPAGE)) 
				params.put(REAL_URL, UrlGenerator.generateUrl(getHomeEndPoint()+"/" + ShareUrlToken.CLASSPAGE.getUrl(), contentGooruOid, CLASSPAGE));
			else {
				if (params.get(SHARETYPE).equalsIgnoreCase("embed")){
					//params.put(REAL_URL, UrlGenerator.generateUrl(getHomeEndPoint()+"/" + ShareUrlToken.COLLECTION_PLAY_URL.getUrl()+"%26embed=true", contentGooruOid));
					params.put(REAL_URL, UrlGenerator.generateUrl(getHomeEndPoint()+"/" + ShareUrlToken.COLLECTION_PLAY_EMBEDED_URL.getUrl(), contentGooruOid));
				
				}else{
					params.put(REAL_URL, UrlGenerator.generateUrl(getHomeEndPoint() +"/" + ShareUrlToken.COLLECTION_PLAY_URL.getUrl()+"%26share=true", contentGooruOid));
					}
			}
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.SHARE_SHORTEN_URL, params, contentGooruOid, getLoggedInSessionToken());
		
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		jsonRep=jsonResponseRep.getJsonRepresentation();
		try{
		shortenUrl = shareDeSerializer.deserializeShortenUrl(jsonRep);
		}
		catch(Exception e)
		{
			
		}
		
		if(getHttpRequest().getScheme().equalsIgnoreCase(HTTPS)) {
			shortenUrl.put(SHORTEN_URL, shortenUrl.get(SHORTEN_URL).replaceAll(HTTP, HTTPS));
		}
		return shortenUrl;
	}
	
	@Override
	public Map<String, String> getShortenShareUrlforAssign(String contentGooruOid, Map<String, String> params,String classpageItemId) {
		JsonRepresentation jsonRep=null;
		Map<String, String> shortenUrl = new HashMap<String, String>();
			if (params.get(TYPE).equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)) {	
				if (params.get(SHARETYPE).equalsIgnoreCase("embed")){
					params.put(REAL_URL, UrlGenerator.generateUrl(getHomeEndPoint() +"/"+ ShareUrlToken.RESOURCE_PLAY_URL.getUrl()+"%26embed=true", contentGooruOid, RESOURCE));
				}else{
					params.put(REAL_URL, UrlGenerator.generateUrl(getHomeEndPoint() +"/"+ ShareUrlToken.RESOURCE_PLAY_URL.getUrl()+"%26share=true", contentGooruOid, RESOURCE));
				}
			}else if(params.get(TYPE).equalsIgnoreCase(PlaceTokens.EDIT_CLASSPAGE)) 
				params.put(REAL_URL, UrlGenerator.generateUrl(getHomeEndPoint()+"/" + ShareUrlToken.CLASSPAGE.getUrl(), contentGooruOid, CLASSPAGE));
			else if(params.get(TYPE).equalsIgnoreCase(PlaceTokens.COLLECTION_PLAY)) 
				params.put(REAL_URL, UrlGenerator.generateUrl(getHomeEndPoint() +"/" + ShareUrlToken.COLLECTION_PLAY_CLASSPAGE_URL.getUrl()+"%26page=study%26share=true", contentGooruOid,classpageItemId));
			else {
				if (params.get(SHARETYPE).equalsIgnoreCase("embed")){
					//params.put(REAL_URL, UrlGenerator.generateUrl(getHomeEndPoint()+"/" + ShareUrlToken.COLLECTION_PLAY_URL.getUrl()+"%26embed=true", contentGooruOid));
					params.put(REAL_URL, UrlGenerator.generateUrl(getHomeEndPoint()+"/" + ShareUrlToken.COLLECTION_PLAY_EMBEDED_URL.getUrl(), contentGooruOid));
				
				}else{
					params.put(REAL_URL, UrlGenerator.generateUrl(getHomeEndPoint() +"/" + ShareUrlToken.COLLECTION_PLAY_URLAssign.getUrl()+"%26share=true", contentGooruOid));
				}
			}
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.SHARE_SHORTEN_URL, params, contentGooruOid, getLoggedInSessionToken());
		
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		jsonRep=jsonResponseRep.getJsonRepresentation();
		try{
		shortenUrl = shareDeSerializer.deserializeShortenUrl(jsonRep);
		}
		catch(Exception e)
		{
			
		}
		
		if(getHttpRequest().getScheme().equalsIgnoreCase(HTTPS)) {
			shortenUrl.put(SHORTEN_URL, shortenUrl.get(SHORTEN_URL).replaceAll(HTTP, HTTPS));
		}
		return shortenUrl;
	}

	@Override
	public String getGoogleSignin(String parms) {
		
		parms = parms.replaceAll("#", "%23");
		parms = parms.replaceAll("!", "%21");
		parms = parms.replaceAll("&", "%26");
				
		String gSigninUrl = getGoogleSignin() + "domain=gmail.com&callBackUrl=" + parms;
		return gSigninUrl;
	}
	
	@Override
	public String getGooruStoriesUrl(String parms) {
		
		return getStoriesUrl();
	}
	
	
	@Override
	public String getGoogleSignin(String placeToken, Map<String, String> parms) {
		
		String callback = StringUtil.generateMessage(getHomeEndPoint() + "#" +placeToken, parms);
		
		callback = callback.replaceAll("#", "%23");
		callback = callback.replaceAll("!", "%21");
		callback = callback.replaceAll("&", "%26");
		
		String gSigninUrl = getGoogleSignin() + "domain=gmail.com&callBackUrl=" + callback;
		return gSigninUrl;
	}
	
	@Override
	public String getGoogleDrive(String url, Map<String, String> parms) {
		
		String callback = StringUtil.generateMessage(url, parms);
		
		callback = callback.replaceAll("#", "%23");
		callback = callback.replaceAll("!", "%21");
		callback = callback.replaceAll("&", "%26");
		
		String gDriveUrl = getDriveGoogle() + "?emailId="+parms.get("emailId")+"&callBackUrl=" + callback;
		return gDriveUrl;
	}
	
	@Override
	public String getCollectionPlayDirectLink(String params){
		String directLink="";
		directLink = getHomeEndPoint() + params;
		return directLink;
	}

	@Override
	public String getHomeEndPointUrl(){
		return  getHomeEndPoint();
	}

	@Override
	public SearchDo<ResourceSearchResultDo> getSuggestSearchResultForResourceNoResult(SearchDo<ResourceSearchResultDo> searchDo){
		    JsonRepresentation jsonRep=null;
			SearchDo<ResourceSearchResultDo> searchDOEmpty = new SearchDo<ResourceSearchResultDo>();
			try{
			String url = UrlGenerator.generateUrl(getSearchEndPoint(), UrlToken.SEARCH_SUGGEST_NO_RESULT, searchDo.getFilters(), getLoggedInSessionToken(), query);
			JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getSearchUsername(), getSearchPassword());
			jsonRep=jsonResponseRep.getJsonRepresentation();
			resourceSearchResultDeSerializer.deserialize(jsonRep, searchDo);
			return searchDo;
			}catch(Exception e){
			}
			return searchDOEmpty;
		}
	
	@Override
	public SearchDo<AutoSuggestKeywordSearchDo> getSuggestedAutokeyword(
			SearchDo<AutoSuggestKeywordSearchDo> searchDo) throws GwtException {
		String pageSize="5";
		String pageNumber="1";
		JsonRepresentation jsonRep=null;
		String url = UrlGenerator.generateUrl(getSearchEndPoint(), UrlToken.SEARCH_AUTO_SUGGEST_KEYWORD, getLoggedInSessionToken(),searchDo.getSearchQuery(),pageSize,searchDo.getType(),pageNumber);
		
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getSearchUsername(), getSearchPassword());
		jsonRep=jsonResponseRep.getJsonRepresentation();
		searchDo.setSearchResults(autoSearchKeyWordDeSerializer.deserializeAutoKeyword(jsonRep));
		return searchDo;
	
	}
	
	@Override
	public SearchDo<String> getSuggestedAggregator(SearchDo<String> searchDo)
			throws GwtException {
		JsonRepresentation jsonRep=null;
		String url = UrlGenerator.generateUrl(getSearchEndPoint(), UrlToken.SEARCH_SUGGEST_AGGREGATOR, getLoggedInSessionToken(), URLEncoder.encode(searchDo.getSearchQuery()), searchDo.getPageSize() + "", searchDo.getPageNum() + "");
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getSearchUsername(), getSearchPassword());
		jsonRep=jsonResponseRep.getJsonRepresentation();
		searchDo.setSearchResults(autoCompleteDeSerializer.deserializeAggregator(jsonRep));
		return searchDo;
	}
	
	@Override
	public SearchDo<ResourceSearchResultDo> getCollectionSuggestedResourceSearchResults(
			SearchDo<ResourceSearchResultDo> searchDo, String contentGorruOid)
			throws GwtException {
		// TODO Auto-generated method stub
	
		SearchDo<ResourceSearchResultDo> searchDOEmpty = new SearchDo<ResourceSearchResultDo>();
		String query1=searchDo.getSearchQuery();
		 query= query1;
		try{
			if(searchDo.getFilters()!=null){
				for (String key : searchDo.getFilters().keySet()) {
				  String value = searchDo.getFilters().get(key);
				  value = value.replaceAll("&", "%26");
				  searchDo.getFilters().put(key, value);
				 }
			}
				
		JsonRepresentation jsonRep=null;
		Map<String,String> filtersMap=searchDo.getFilters();
		if(filtersMap!=null){
	        String category=filtersMap.get("category");
	        if(category!=null&&category.equalsIgnoreCase("All")){
	                filtersMap.remove("category");
	        }
	        else if(category!=null){
	        	if(category.equalsIgnoreCase("Website")){
	               	category=category.replaceAll("Website", "webpage");
	                filtersMap.remove("category");
	                filtersMap.put("flt.resourceFormat",category);
	        	}
	        	else {
	        		 filtersMap.remove("category");
	                 filtersMap.put("flt.resourceFormat",category);
	        	}
	        }
		}
		//String url = UrlGenerator.generateUrl(getHomeEndPoint(), UrlToken.SEARCH_SUGGEST_RESOURCES, getLoggedInSessionToken(), URLEncoder.encode(searchDo.getSearchQuery()), COLLECTION_EDIT_EVENT ,"8c20a619-8aba-4b10-ae2c-6cf71d469a80");
		String url = UrlGenerator.generateUrl(getHomeEndPoint(), UrlToken.SEARCH_SUGGEST_RESOURCES, getLoggedInSessionToken(), URLEncoder.encode(searchDo.getSearchQuery()), COLLECTION_EDIT_EVENT ,contentGorruOid);
	
		if(getSearchEndPoint().contains(HTTPS)){
			url = appendHttpsURL(url);
		}

		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getSearchUsername(), getSearchPassword());
		jsonRep=jsonResponseRep.getJsonRepresentation();
		try{
			resourceSearchResultDeSerializer.deserializeSuggestedResources(jsonRep, searchDo);	
		}
		catch(Exception e)
		{
			
		}
		return searchDo;
		}catch(Exception e){
		}
		return searchDOEmpty;
	
	
	}
	
	@Override
	public ArrayList<StandardsLevel1DO> getFirstLevelStandards(String levelOrder, String standardLabel) {
		JsonRepresentation jsonRep=null;
		ArrayList<StandardsLevel1DO> standardLevelArry = new ArrayList<StandardsLevel1DO>();
		
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_LevelWiseStandards,levelOrder,standardLabel, getLoggedInSessionToken());
	
		if(getSearchEndPoint().contains(HTTPS)){
			url = appendHttpsURL(url);
		}
		System.out.println("getFirstLevelStandards::"+url);
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getSearchUsername(), getSearchPassword());
		jsonRep=jsonResponseRep.getJsonRepresentation();
		try {
			if (jsonRep != null && jsonRep.getSize() != -1) {
				standardLevelArry = JsonDeserializer.deserialize(jsonRep.getJsonArray().toString(), new TypeReference<ArrayList<StandardsLevel1DO>>() {});
				return standardLevelArry;
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return standardLevelArry;
	}
	
	@Override
	public ArrayList<StandardsLevel2DO> getSecondLevelStandards(String levelOrder, String standardLabel) {
		JsonRepresentation jsonRep=null;
		ArrayList<StandardsLevel2DO> standardLevelArry = new ArrayList<StandardsLevel2DO>();
		
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_LevelWiseStandards,levelOrder,standardLabel, getLoggedInSessionToken());
	
		if(getSearchEndPoint().contains(HTTPS)){
			url = appendHttpsURL(url);
		}
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getSearchUsername(), getSearchPassword());
		jsonRep=jsonResponseRep.getJsonRepresentation();
		try {
			if (jsonRep != null && jsonRep.getSize() != -1) {
	
				standardLevelArry = JsonDeserializer.deserialize(jsonRep.getJsonArray().toString(), new TypeReference<ArrayList<StandardsLevel2DO>>() {});
	
				return standardLevelArry;
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return standardLevelArry;
	}
	
	@Override
	public ArrayList<StandardsLevel3DO> getThirdLevelStandards(String levelOrder, String standardLabel) {
		JsonRepresentation jsonRep=null;
		ArrayList<StandardsLevel3DO> standardLevelArry = new ArrayList<StandardsLevel3DO>();
		
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_LevelWiseStandards,levelOrder,standardLabel, getLoggedInSessionToken());
	
		if(getSearchEndPoint().contains(HTTPS)){
			url = appendHttpsURL(url);
		}
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getSearchUsername(), getSearchPassword());
		jsonRep=jsonResponseRep.getJsonRepresentation();
		try {
			if (jsonRep != null && jsonRep.getSize() != -1) {
	
				standardLevelArry = JsonDeserializer.deserialize(jsonRep.getJsonArray().toString(), new TypeReference<ArrayList<StandardsLevel3DO>>() {});
	
				return standardLevelArry;
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return standardLevelArry;
	}
	@Override
	public ArrayList<StandardsLevel4DO> getFourthLevelStandards(String levelOrder, String standardLabel) {
		JsonRepresentation jsonRep=null;
		ArrayList<StandardsLevel4DO> standardLevelArry = new ArrayList<StandardsLevel4DO>();
		
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_LevelWiseStandards,levelOrder,standardLabel, getLoggedInSessionToken());
	
		if(getSearchEndPoint().contains(HTTPS)){
			url = appendHttpsURL(url);
		}
		System.out.println("getFourthLevelStandards:::"+url);
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getSearchUsername(), getSearchPassword());
		jsonRep=jsonResponseRep.getJsonRepresentation();
		try {
			if (jsonRep != null && jsonRep.getSize() != -1) {
	
				standardLevelArry = JsonDeserializer.deserialize(jsonRep.getJsonArray().toString(), new TypeReference<ArrayList<StandardsLevel4DO>>() {});
	
				return standardLevelArry;
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return standardLevelArry;
	}
	
	
	@Override
	public SearchDo<CodeDo> getSuggestStandardByFilterCourseIdsource(
			SearchDo<CodeDo> searchDo) throws GwtException, ServerDownException {
		// TODO Auto-generated method stub
		JsonRepresentation jsonRep=null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.SUGGEST_STANDARD_BY_FILTER_Source_CodeId, getLoggedInSessionToken(), searchDo.getSearchQuery());
		System.out.println("searchDo.getFilters():::::::"+searchDo.getFilters());
		if(searchDo.getFilters()!=null && searchDo.getFilters().size()>0) {
			url = url + "&"+FLT_SOURCE_CODE_ID+"="+searchDo.getFilters().get(FLT_SOURCE_CODE_ID);
		}
		System.out.println("getSuggestStandardByFiltersource course id 1 CourseId api new:::::::"+url);
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getSearchUsername(), getSearchPassword());
		jsonRep=jsonResponseRep.getJsonRepresentation();
		searchDo.setSearchResults(autoCompleteDeSerializer.deserializeStandards(jsonRep));
		return searchDo;
	}
	
	@Override
	public String showGooruStoriesSection() throws GwtException, ServerDownException {
		return showStoriesSection();
	}

	@Override
	public SearchResourcesTagsDo getResourceTags(String resourceId,	String offSet, String limit) throws GwtException,ServerDownException {
		
		JsonRepresentation jsonRep = null;
		SearchResourcesTagsDo searchResourcesTagsDo = new SearchResourcesTagsDo();
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.RESOURCE_TAGS, resourceId,getLoggedInSessionToken(),offSet,limit);
		getLogger().info("-- resource based tags url -- "+url);
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		jsonRep = jsonResponseRep.getJsonRepresentation();	
		
		try{
			searchResourcesTagsDo = JsonDeserializer.deserialize(jsonRep.getJsonObject().toString(), SearchResourcesTagsDo.class);
		}
		catch(JSONException ex){
			
		}
		return searchResourcesTagsDo;
	}
	
	/*@Override
	public SearchDo<CollectionSearchResultDo> getSuggestedSearchResultForCollectionNoResult(
			SearchDo<CollectionSearchResultDo> searchDo) throws GwtException {
		SearchDo<CollectionSearchResultDo> searchDOEmpty = new SearchDo<CollectionSearchResultDo>();
		try{
		String url = UrlGenerator.generateUrl(getSearchEndPoint(), UrlToken.SEARCH_SUGGEST_COLLECTION_NO_RESULT, searchDo.getFilters(), getLoggedInSessionToken(), collectionQuery);
		JsonRepresentation jsonRep = ServiceProcessor.get(url, getSearchUsername(), getSearchPassword());
		collectionSearchResultDeSerializer.deserialize(jsonRep, searchDo);
		return searchDo;
		}catch(Exception e){
		}
		return searchDOEmpty;
	}*/
	
}
