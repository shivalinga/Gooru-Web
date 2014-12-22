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

import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ednovo.gooru.server.AppSessionHolder;
import org.ednovo.gooru.server.request.JsonResponseRepresentation;
import org.ednovo.gooru.server.request.ServiceProcessor;
import org.ednovo.gooru.server.request.UrlToken;
import org.ednovo.gooru.server.serializer.JsonDeserializer;
import org.ednovo.gooru.shared.model.user.FilterSettings;
import org.ednovo.gooru.shared.model.user.UserDo;
import org.ednovo.gooru.shared.model.user.V2UserDo;
import org.ednovo.gooru.shared.util.ResourceImageUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.ext.json.JsonRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * @author Search Team
 * 
 */
public class BaseServiceImpl extends GwtAbstractServiceImpl implements RemoteService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7437954157376886661L;

	private static final Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);

	@Resource(name = "restConstants")
	private Properties restConstants;
	
	public static final String GOORU_ANONYMOUS = "ANONYMOUS";

	private static final String REST_ENDPOINT = "rest.endpoint";

	private static final String SEARCH_ENDPOINT = "search.endpoint";

	private static final String REST_USERNAME = "rest.username";

	private static final String REST_PASSWORD = "rest.password";

	private static final String SEARCH_USERNAME = "search.username";

	private static final String SEARCH_PASSWORD = "search.password";

	private static final String API_KEY = "api.key";

	private static final String HOME_ENDPOINT = "home.endpoint";
	
	private static final String ANALYTICS_ENDPOINT = "analytics.endpoint";

	private static final String DOMAIN_NAME = "domain.name";
	
	private static final String DOCVIEWER_HOME = "docViewer.home";
	
	private static final String DOCVIEWER_CACHE = "docViewer.cache";

	private static final String GOOGLE_ANALTICS_ADDITIONAL_ACCOUNTS = "google.analtics.additional.accounts";

	private static final String CLASSIC_ENDPOINT = "classic.endpoint";

	private static final String GOORU_SESSION_TOKEN = "gooru-session-token";
	
	private static final String GOORU_ACCESS_TOKEN = "google-access-token";
	
	private static final String GOORU_ACTIVE_USER = "gooru-active-user";

	private static final String SIGNED_USER_UID = "signed-user-uid";

	private static final String SIGNED_USER_EMAILID = "signed-user-emailid";

	private static final String COOKIE_PATH = "/";

	private static final String TOKEN = "token";

	private static final String USER_INFO_FAILED_ON_TOKEN = "Get User info failed on token : ";

	public static final int COOKIE_AGE = 86400;//24 hrs
	
	private static final String PRODUCTION_SWITCH = "production.switch";
	
	private static final String GOOGLE_SIGNIN = "google.signin";
	
	private static final String GOOGLE_DRIVE = "drive.api";
	
	private static final String PROFILE_IMAGE_RESPOSITORY_URL="profile.image.url";
	
	private static final String CDN_ENDPOINT = "cdn.endpoint";
	
	private static final String DATA_LOG_API_KEY = "log.api.key";
	
	private static final String WHATS_NEW_MOS_LINK = "whats.new.mos.link";
	
	private static final String WHATS_NEW_FIB_LINK = "whats.new.fib.link";
	
	private static final String MOS_LINK = "mos.link";

	private static final String GOORU_RELEASE_VERSION = "gooru.release.version";
	
	private static final String FACEBOOK_APP_ID="facebook.app_id";
	
	private static final String FACEBOOK_FEED_URL="facebook.dialogfeedurl";
	
	private static final String TAXONOMY_PREFERENCES = "taxonomy.preferences";
	
	private static final String SIGNED_USER_DOB = "signed-user-dob";
	
	private static final String SERVER_REDIRECT_URL="redirect.url";
	
	private static final String GOOGLE_RESTENDPOINT="google.restendpoint";
	
	private static final String STORIES_URL = "stories.url";
	
	private static final String SHOW_STORIES = "show.stories";
	
	private static final String COMMUNITY_LIBRARY_ID = "community.library.gooruOid";
	
	private static final String ASSESSMENT_ENDPOINT = "assessment.endpoint";
	
	private static final String HTTPS = "https";
	
	private static final String HTTP = "http";
	
	public BaseServiceImpl() {

	}

	@Override
	protected void onBeforeRequestDeserialized(String serializedRequest) {
		super.onBeforeRequestDeserialized(serializedRequest);
		AppSessionHolder.getInstance().setRequest(getThreadLocalRequest());
		AppSessionHolder.getInstance().setResponse(getThreadLocalResponse());
	}

	public static Logger getLogger() {
		return logger;
	}
	
	public String getProductionSwitchDate(){
		return restConstants.getProperty(PRODUCTION_SWITCH);
	}

	public String getDataApiKey(){
		return restConstants.getProperty(DATA_LOG_API_KEY);
	}
	
	public String getGoogleSignin(){
		return restConstants.getProperty(GOOGLE_SIGNIN);
	}
	
	public String getDriveGoogle(){
		return restConstants.getProperty(GOOGLE_DRIVE);
	}
	
	public String getWhatsNewMosLink(){
		return restConstants.getProperty(WHATS_NEW_MOS_LINK);
	}
	
	public String getWhatsNewFibLink(){
		return restConstants.getProperty(WHATS_NEW_FIB_LINK);
	}
	
	public String getMosLink(){
		return restConstants.getProperty(MOS_LINK);
	}
	
	public String getAssessementEndPoint(){
		return restConstants.getProperty(ASSESSMENT_ENDPOINT);
	}
	
	public String getRestEndPoint() {
		String restEndPoint = restConstants.getProperty(REST_ENDPOINT);
		if(getHttpRequest().getScheme().equalsIgnoreCase(HTTPS)) {
			restEndPoint = restEndPoint.replaceAll(HTTP, HTTPS);
		}
		return restEndPoint;
	}

	public String getSearchEndPoint() {
		String searchEndPoint = restConstants.getProperty(SEARCH_ENDPOINT);
		if(getHttpRequest().getScheme().equalsIgnoreCase(HTTPS)) {
			searchEndPoint = searchEndPoint.replaceAll(HTTP, HTTPS);
		}
		return searchEndPoint;
	}

	public String getRestUsername() {
		return restConstants.getProperty(REST_USERNAME);
	}

	public String getRestPassword() {
		return restConstants.getProperty(REST_PASSWORD);
	}

	public String getSearchUsername() {
		return restConstants.getProperty(SEARCH_USERNAME);
	}

	public String getSearchPassword() {
		return restConstants.getProperty(SEARCH_PASSWORD);
	}

	public String getApiKey() {
		return restConstants.getProperty(API_KEY);
	}

	public String getHomeEndPoint() {
		String homeEndPoint = restConstants.getProperty(HOME_ENDPOINT);
		if(getHttpRequest().getScheme().equalsIgnoreCase(HTTPS)) {
			homeEndPoint = homeEndPoint.replaceAll(HTTP, HTTPS);
			ResourceImageUtil.protocol=HTTPS;
		}
		return homeEndPoint;
	}
	
	public String getAnalyticsEndPoint() {
		String analyticsEndPoint = restConstants.getProperty(ANALYTICS_ENDPOINT);
		if(getHttpRequest().getScheme().equalsIgnoreCase(HTTPS)) {
			analyticsEndPoint = analyticsEndPoint.replaceAll(HTTP, HTTPS);
			ResourceImageUtil.protocol=HTTPS;
		}
		return analyticsEndPoint;
	}

	public String getHomeEndPointForEmbed() {
		return restConstants.getProperty(HOME_ENDPOINT);
	}
	
	public String getRestEndPointForEmbed() {
		return restConstants.getProperty(REST_ENDPOINT);
	}

	public String getDocViewerHome() {
		String docViewerHome = restConstants.getProperty(DOCVIEWER_HOME);
		if(getHttpRequest().getScheme().equalsIgnoreCase(HTTPS)) {
			docViewerHome = docViewerHome.replaceAll(HTTP, HTTPS);
		}
		return docViewerHome;
	}
	
	public String getDocViewerPoint() {
		return restConstants.getProperty(DOCVIEWER_CACHE);
	}
	
	public String getProfileImageUrl() {
		String profileImageUrl = restConstants.getProperty(PROFILE_IMAGE_RESPOSITORY_URL);
		if(getHttpRequest().getScheme().equalsIgnoreCase(HTTPS)) {
			profileImageUrl = profileImageUrl.replaceAll(HTTP, HTTPS);
		}
		return profileImageUrl;
	}
	
	public String getFacebookAppId(){
		return restConstants.getProperty(FACEBOOK_APP_ID);
	}
	
	public String getFacebookFeedUrl(){
		return restConstants.getProperty(FACEBOOK_FEED_URL);
	}
	
	public String getTaxonomyPreferences(){
		return restConstants.getProperty(TAXONOMY_PREFERENCES);
	}
	
	public String getCdnEndPoint() {
		String cdnEndPoint = restConstants.getProperty(CDN_ENDPOINT);
		if(getHttpRequest().getScheme().equalsIgnoreCase(HTTPS)) {
			cdnEndPoint = cdnEndPoint.replaceAll(HTTP, HTTPS);
		}
		return cdnEndPoint;
	}
	
	public String getRedirectUrl(){
		String serverRedirectUrl = restConstants.getProperty(SERVER_REDIRECT_URL);
		return serverRedirectUrl;
	}
	
	public String getGoogleRestEndPoint(){
		return restConstants.getProperty(GOOGLE_RESTENDPOINT);
	}
	
	public String getStoriesUrl(){
		return restConstants.getProperty(STORIES_URL);
	}
	
	public String showStoriesSection(){
		return restConstants.getProperty(SHOW_STORIES);
	}

	public String getCommunityLibaryGooruOid(){
		return restConstants.getProperty(COMMUNITY_LIBRARY_ID);
		
	}
	protected static Integer stringtoInteger(JSONObject jsonObject, String key) {	
		if (jsonObject != null && jsonObject.has(key)) {
			String value = null;
			try {
				value = jsonObject.getString(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return value != null ? Integer.parseInt(value) : null;
		} else {
			return null;
		}
	}

	protected static Form generateFormFromParamters(Map<String, String> params) {
		Form form = new Form();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			form.add(entry.getKey(), entry.getValue());
		}
		return form;
	}

	protected void setUserFilterProperties(UserDo user) {
		FilterSettings filterProperties = new FilterSettings();
		filterProperties.setRestEndPoint(getRestEndPoint());
		filterProperties.setHomeEndPoint(getHomeEndPoint());
		filterProperties.setDomainName(getDomainName());
		filterProperties.setClassicEndPoint(getClassicEndPoint());
		filterProperties.setDocViewerHome(getDocViewerHome());
		filterProperties.setDocViewerPoint(getDocViewerPoint());
		filterProperties.setApiKeyPoint(getApiKey());
		filterProperties.setGoogleAnalticsAdditionalAccounts(getGoogleAnalticsAdditionalAccounts());
		filterProperties.setGoogleSignin(getGoogleSignin());
		filterProperties.setProfileImageUrl(getProfileImageUrl());
		filterProperties.setCdnEndPoint(getCdnEndPoint());
		filterProperties.setDataApiKey(getDataApiKey());
		filterProperties.setGooruReleaseVersion(getGooruReleaseVersion());
		filterProperties.setFacebookAppId(getFacebookAppId());
		filterProperties.setFacebookFeedUrl(getFacebookFeedUrl());
		filterProperties.setTaxonomyPreferences(getTaxonomyPreferences());
		filterProperties.setAnalyticsEndPoint(getAnalyticsEndPoint());
		filterProperties.setCommunityLibraryGooruOid(getCommunityLibaryGooruOid());
		filterProperties.setAssessementEndPoint(getAssessementEndPoint());
		user.setSettings(filterProperties);
	}

	public String getDomainName() {
		return restConstants.getProperty(DOMAIN_NAME);
	}

	public String getClassicEndPoint() {
		return restConstants.getProperty(CLASSIC_ENDPOINT);
	}
	
	public String getGoogleAnalticsAdditionalAccounts() {
		return restConstants.getProperty(GOOGLE_ANALTICS_ADDITIONAL_ACCOUNTS);
	}

	public String getGooruReleaseVersion() {
		return restConstants.getProperty(GOORU_RELEASE_VERSION);
	}
	
	protected String getLoggedInSessionToken() {
		String token = getCookie(GOORU_SESSION_TOKEN);
		if (token == null) {
			token = (String) getHttpRequest().getSession().getAttribute(GOORU_SESSION_TOKEN);
			//Fix for handling when cookie get disabled.
			if (token == null || token.equalsIgnoreCase("null")) { 
//				UserDo user = guestSignIn();
				UserDo user = v2GuestSignIn();
				token = (user != null) ? user.getToken() : token;
				getHttpRequest().getSession().setAttribute(GOORU_SESSION_TOKEN, token);
			}
			setLoggedInSessionToken(token);
		}
		return token;
	}

	protected String getLoggedInAccessToken() {
		String token = getCookie(GOORU_ACCESS_TOKEN);
		if (token == null) {
			token = (String) getHttpRequest().getSession().getAttribute(GOORU_ACCESS_TOKEN);
			//Fix for handling when cookie get disabled.
			if (token == null || token.equalsIgnoreCase("null")) { 
				getHttpRequest().getSession().setAttribute(GOORU_ACCESS_TOKEN, token);
			}
			setLoggedInAccessToken(token);
		}
		return token;
	}
	protected void setLoggedInAccessToken(String accessToken) {
		Cookie cookie = new Cookie(GOORU_ACCESS_TOKEN, accessToken);
		cookie.setDomain(AppSessionHolder.getInstance().getRequest().getServerName());
		cookie.setPath(COOKIE_PATH);
		if (accessToken != null && accessToken.length() > 0) {
			cookie.setMaxAge(COOKIE_AGE);
		} else {
			cookie.setMaxAge(0);
		}
		getHttpResponse().addCookie(cookie);
		if (accessToken != null) {
			getHttpRequest().getSession().setAttribute(GOORU_ACCESS_TOKEN, accessToken);
		} else {
			getHttpRequest().getSession().removeAttribute(GOORU_ACCESS_TOKEN);
		}
	}
	private String getCookie(String name) {
		Cookie[] cookies = getHttpRequest().getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	protected String getGuestSessionToken(String restEndPointFromEmbed){
		UserDo user = v2GuestSignInForEmbed(restEndPointFromEmbed);
		String token = (user != null) ? user.getToken() : "";
		return token;
	}
	protected void setLoggedInSessionToken(String sessionToken) {
		Cookie cookie = new Cookie(GOORU_SESSION_TOKEN, sessionToken);
		cookie.setDomain(AppSessionHolder.getInstance().getRequest().getServerName());
		cookie.setPath(COOKIE_PATH);
		if (sessionToken != null && sessionToken.length() > 0) {
			cookie.setMaxAge(COOKIE_AGE);
		} else {
			cookie.setMaxAge(0);
		}
		getHttpResponse().addCookie(cookie);
		if (sessionToken != null) {
			getHttpRequest().getSession().setAttribute(GOORU_SESSION_TOKEN, sessionToken);
		} else {
			getHttpRequest().getSession().removeAttribute(GOORU_SESSION_TOKEN);
		}
	}
	
	protected void setUserActiveStatus(String userStatus){
		Cookie cookie = new Cookie(GOORU_ACTIVE_USER, userStatus);
		cookie.setDomain(AppSessionHolder.getInstance().getRequest().getServerName());
		cookie.setPath(COOKIE_PATH);
		cookie.setMaxAge(COOKIE_AGE);
		getHttpResponse().addCookie(cookie);
	}
	
	protected void setUserInActiveStatus(String userStatus){
		Cookie cookie = new Cookie(GOORU_ACTIVE_USER, userStatus);
		cookie.setDomain(AppSessionHolder.getInstance().getRequest().getServerName());
		cookie.setPath(COOKIE_PATH);
		cookie.setMaxAge(COOKIE_AGE);
		getHttpResponse().addCookie(cookie);
	}

	protected void setLoggedInInfo(String sessionToken, String loggedInUserUid, String loggedInEmailId,String dateOfBirth) {
		setLoggedInSessionToken(sessionToken);
		setLoggedInUserUid(loggedInUserUid);
		setLoggedInEmailId(loggedInEmailId);
		setLoggedInDateOfBirth(dateOfBirth);
		setUserActiveStatus("active");
	}


	protected void deleteLoggedInInfo() {
		Cookie[] cookies = getHttpRequest().getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(GOORU_SESSION_TOKEN)) {
					cookie.setMaxAge(0);
					cookie.setPath(COOKIE_PATH);
					getHttpResponse().addCookie(cookie);
				}
				else if(cookie.getName().equals(GOORU_ACTIVE_USER)){
					setUserInActiveStatus("in-active");
				}
			}
		}
		getHttpRequest().getSession().removeAttribute(GOORU_SESSION_TOKEN);
		setLoggedInUserUid(null);
	}

	protected String getLoggedInUserUid() {
		String sessionSessionToken = (String) getHttpRequest().getSession().getAttribute(GOORU_SESSION_TOKEN);
		String cookieSessionToken = getCookie(GOORU_SESSION_TOKEN);
		String loggedInUserUid = (String) getHttpRequest().getSession().getAttribute(SIGNED_USER_UID);

		boolean sessionTimedOutUser = sessionSessionToken == null && cookieSessionToken != null;
		boolean switchedUser = sessionSessionToken != null && cookieSessionToken != null && !cookieSessionToken.equals(sessionSessionToken);
		boolean inUser = sessionSessionToken != null && cookieSessionToken != null && cookieSessionToken.equals(sessionSessionToken);

		UserDo user = null;

		if (sessionTimedOutUser || switchedUser) {
//			user = getUserInfoByToken(cookieSessionToken);
			user = v2GetUserInfoByToken(cookieSessionToken);
		} else if (!inUser) {
			user = v2GuestSignIn();
		}
		if (user != null) {
			deleteLoggedInInfo();
			loggedInUserUid = user.getGooruUId();
			setLoggedInInfo(cookieSessionToken == null ? user.getToken() : cookieSessionToken, user.getGooruUId(),getLoggedInEmailId(),getLoggedInDateOfBirth());
		}
		return loggedInUserUid;
	}

	protected void setLoggedInUserUid(String loggedInUserUid) {
		if (loggedInUserUid != null) {
			getHttpRequest().getSession().setAttribute(SIGNED_USER_UID, loggedInUserUid);
		} else {
			getHttpRequest().getSession().removeAttribute(SIGNED_USER_UID);
		}
	}

	protected String getLoggedInEmailId() {
		String loggedInEmailId = (String) getHttpRequest().getSession().getAttribute(SIGNED_USER_EMAILID);
		return loggedInEmailId;
	}

	protected void setLoggedInEmailId(String loggedInEmailId) {
		if (loggedInEmailId != null) {
			getHttpRequest().getSession().setAttribute(SIGNED_USER_EMAILID, loggedInEmailId);
		} else {
			getHttpRequest().getSession().removeAttribute(SIGNED_USER_EMAILID);
		}
	}
	
	protected String getLoggedInDateOfBirth() {
		String loggedInDob = (String) getHttpRequest().getSession().getAttribute(SIGNED_USER_DOB);
		return loggedInDob;
	}
	
	protected void setLoggedInDateOfBirth(String dateOfBirth) {
		if (dateOfBirth != null) {
			getHttpRequest().getSession().setAttribute(SIGNED_USER_DOB, dateOfBirth);
		} else {
			getHttpRequest().getSession().removeAttribute(SIGNED_USER_DOB);
		}
	}

	protected boolean isLoggedInUserAnonymous() {
		return getLoggedInUserUid() == null || getLoggedInUserUid().equals(GOORU_ANONYMOUS);
	}

	protected UserDo guestSignIn() {
		UserDo user = null;
		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.GUEST_SIGNIN, getApiKey());
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.post(url, getRestUsername(), getRestPassword());
		jsonRep =jsonResponseRep.getJsonRepresentation();
		try {
			setLoggedInSessionToken(jsonRep.getJsonObject().getString(TOKEN));
			user = JsonDeserializer.deserialize(jsonRep.getJsonObject().toString(), UserDo.class);
			setLoggedInUserUid(user.getGooruUId());
			setLoggedInEmailId("");
			setLoggedInDateOfBirth("");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	protected UserDo guestSignInForEmbed(String restEndPointFromEmbed){
		UserDo user = null;
		String url = UrlGenerator.generateUrl(restEndPointFromEmbed, UrlToken.GUEST_SIGNIN, getApiKey());
		JsonRepresentation jsonRep = null;
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.post(url, getRestUsername(), getRestPassword());
		jsonRep =jsonResponseRep.getJsonRepresentation();
		try {
			user = JsonDeserializer.deserialize(jsonRep.getJsonObject().toString(), UserDo.class);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return user;
	}

	protected UserDo getUserInfo(String userUid) {
		UserDo userDo = null;
		String sessionToken = getLoggedInSessionToken();
		if (sessionToken != null) {
			String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.GET_USER, userUid, sessionToken);
			JsonRepresentation jsonRep = null;
			JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(), getRestPassword());
			jsonRep =jsonResponseRep.getJsonRepresentation();
			try {
				String data = jsonRep.getJsonObject().getJSONObject("user").toString();
//				String data = jsonRep.getJsonObject().getJSONObject("data").getJSONObject("user").toString(); //removed and moved to V2 API
				userDo = JsonDeserializer.deserialize(data, UserDo.class);
//				if (userDo.getCreatedOn()!=null){
					Date prodDate = new SimpleDateFormat("dd/MM/yyyy").parse(getProductionSwitchDate());				
					Date userCreatedDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S").parse(userDo.getCreatedOn());
					// if user created after production switch
					if (userCreatedDate.getTime() >= prodDate.getTime()){
						userDo.setBeforeProductionSwitch(false);
					}else{
						userDo.setBeforeProductionSwitch(true);
					}
//				}
			} catch (JSONException e) {
				e.printStackTrace();
			} 
			catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return userDo;
	}

	protected UserDo getUserInfoByToken(String token) {
		UserDo userDo = null;
		try {
			String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.GET_USERBYTOKEN, getLoggedInSessionToken());
			JsonRepresentation jsonRep = null;
			JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(), getRestPassword());
			jsonRep =jsonResponseRep.getJsonRepresentation();
			String data = jsonRep.getJsonObject().toString();
			userDo = JsonDeserializer.deserialize(data, UserDo.class);
//			if (userDo.getCreatedOn()!=null){
				Date prodDate = new SimpleDateFormat("dd/MM/yyyy").parse(getProductionSwitchDate());				
				Date userCreatedDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S").parse(userDo.getCreatedOn());
				// if user created after production switch
				if (userCreatedDate.getTime() >= prodDate.getTime()){
					userDo.setBeforeProductionSwitch(false);
				}else{
					userDo.setBeforeProductionSwitch(true);
				}
//			}
		} catch (Exception e) {
			getLogger().error(USER_INFO_FAILED_ON_TOKEN + token);
		}
		return userDo;
	}

	protected HttpServletRequest getHttpRequest() {
		return AppSessionHolder.getInstance().getRequest();
	}

	protected HttpServletResponse getHttpResponse() {
		return AppSessionHolder.getInstance().getResponse();
	}
	
	protected UserDo v2GuestSignIn() {
		UserDo user = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_GUEST_SIGNIN, getApiKey());
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.post(url, getRestUsername(), getRestPassword());
		JsonRepresentation jsonRep = null;
		jsonRep =jsonResponseRep.getJsonRepresentation();
		try {
			setLoggedInSessionToken(jsonRep.getJsonObject().getString(TOKEN));
			V2UserDo v2UserDo = JsonDeserializer.deserialize(jsonRep.getJsonObject().toString(), V2UserDo.class);
			user = v2UserDo.getUser();
			user.setToken(v2UserDo.getToken());
			setLoggedInUserUid(user.getGooruUId());
			setLoggedInEmailId("");
			setLoggedInDateOfBirth("");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return user;
	}
	protected UserDo v2GuestSignInForEmbed(String restEndPointFromEmbed){
		UserDo user = null;
		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(restEndPointFromEmbed, UrlToken.V2_GUEST_SIGNIN, getApiKey());
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.post(url, getRestUsername(), getRestPassword());
		jsonRep =jsonResponseRep.getJsonRepresentation();
		try {
			V2UserDo v2UserDo = JsonDeserializer.deserialize(jsonRep.getJsonObject().toString(), V2UserDo.class);
			user = v2UserDo.getUser();
			user.setToken(v2UserDo.getToken());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return user;
	}	
	
	protected UserDo v2GetUserInfoByToken(String token) {
		UserDo userDo = null;
		try {
			String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_GET_USER_BY_SESSIONTOKEN, getLoggedInSessionToken(), getLoggedInSessionToken());
			JsonRepresentation jsonRep = null;
			System.out.println("data object==>"+url);
			JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(), getRestPassword());
			jsonRep =jsonResponseRep.getJsonRepresentation();
			String data = jsonRep.getJsonObject().toString();
			System.out.println("data object==>"+data);
			userDo = JsonDeserializer.deserialize(data, UserDo.class);
//			if (userDo.getCreatedOn()!=null){
				Date prodDate = new SimpleDateFormat("dd/MM/yyyy").parse(getProductionSwitchDate());				
				Date userCreatedDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S").parse(userDo.getCreatedOn());
				// if user created after production switch
				if (userCreatedDate.getTime() >= prodDate.getTime()){
					userDo.setBeforeProductionSwitch(false);
				}else{
					userDo.setBeforeProductionSwitch(true);
				}
//			}
		} catch (Exception e) {
			getLogger().error(USER_INFO_FAILED_ON_TOKEN + token);
			e.printStackTrace();
		}
		return userDo;
	}
	
	public String getIpAddress(){
		return getHttpRequest().getRemoteAddr();
	}
}
