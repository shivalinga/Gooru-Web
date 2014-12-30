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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.service.AppService;
import org.ednovo.gooru.server.annotation.ServiceURL;
import org.ednovo.gooru.server.form.AppFormFactory;
import org.ednovo.gooru.server.request.JsonResponseRepresentation;
import org.ednovo.gooru.server.request.ServiceProcessor;
import org.ednovo.gooru.server.request.UrlToken;
import org.ednovo.gooru.server.serializer.JsonDeserializer;
import org.ednovo.gooru.shared.exception.GwtException;
import org.ednovo.gooru.shared.model.user.UserDo;
import org.ednovo.gooru.shared.model.user.V2UserDo;
import org.restlet.ext.json.JsonRepresentation;
import org.springframework.stereotype.Service;

/**
 * @author Search Team
 * 
 */
@Service("appService")
@ServiceURL("/appService")
public class AppServiceImpl extends BaseServiceImpl implements AppService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6736852011457993775L;

	@Override
	public UserDo getLoggedInUser() {
		UserDo user = null;
		String userUid = getLoggedInUserUid();
		if (!isLoggedInUserAnonymous()) {
			user = getUserInfo(userUid);
			user.setToken(getLoggedInSessionToken());
			user.setDateOfBirth(getLoggedInDateOfBirth()); 
//			user.setEmailId(getLoggedInEmailId());
		}
		if (user == null) {
//			user = guestSignIn();
			user = v2GuestSignIn();
		}
		setUserFilterProperties(user);
		return user;
	}

	@Override
	public UserDo signin(String username, String password) {
		UserDo user = null;
		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.SIGNIN, getApiKey(), getLoggedInSessionToken());
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.post(url, getRestUsername(), getRestPassword(), AppFormFactory.getSigninForm(username, password));
		jsonRep =jsonResponseRep.getJsonRepresentation();
		String content = null;
		try {
			content = jsonRep.getText();
			
			if (content.contains("{")) {
				user = JsonDeserializer.deserialize(jsonRep.getJsonObject().toString(), UserDo.class);
				Date prodDate = new SimpleDateFormat("dd/MM/yyyy").parse(getProductionSwitchDate());				
				Date userCreatedDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S").parse(user.getCreatedOn());
				// if user created after production switch
				if (userCreatedDate.getTime() >= prodDate.getTime()){
					user.setBeforeProductionSwitch(false);
				}else{
					user.setBeforeProductionSwitch(true);
				}
				
				setUserFilterProperties(user);
				deleteLoggedInInfo();
				setLoggedInInfo(user.getToken(), user.getGooruUId(), user.getEmailId(),user.getDateOfBirth());
				return user;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GwtException(e.getMessage());
		}
		if (content != null) {
			String[] parsed = content.split(":");
			if (parsed.length > 1) {
				content = parsed[1];
			}
		}
		throw new GwtException(content);
	}

	@Override
	public UserDo signout() {
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.SIGNOUT, getLoggedInSessionToken());
		ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		deleteLoggedInInfo();
		UserDo user = guestSignIn();
		setUserFilterProperties(user);
		return user;
	}

	
	/// V2 Apis
	@Override
	public UserDo v2Signin(String postData) {
		UserDo user = null;
		V2UserDo v2UserDo = null;
		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_SIGNIN, getApiKey());
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.post(url, getRestUsername(), getRestPassword(), postData);
		jsonRep =jsonResponseRep.getJsonRepresentation();
		String content = null;
		try {
			if(jsonResponseRep.getStatusCode()==401){
				user = new UserDo();
				user.setStatusCode(jsonResponseRep.getStatusCode());
				user.setErrorMsg(jsonResponseRep.getErrorMessage());
				return user;
			}else if(jsonResponseRep.getStatusCode()==400){
				user = new UserDo();
				user.setStatusCode(jsonResponseRep.getStatusCode());
				return user;
			}else{
				content = jsonRep.getText();
				if (content.contains("{")) {
					v2UserDo = JsonDeserializer.deserialize(jsonRep.getJsonObject().toString(), V2UserDo.class);
					user = v2UserDo.getUser();
					user.setToken(v2UserDo.getToken());
					user.setStatusCode(jsonResponseRep.getStatusCode());
					user.setDateOfBirth(v2UserDo.getDateOfBirth());
					user.setAccountCreatedType(user.getAccountCreatedType());

					//				user.setCreatedOn(v2UserDo.getCreatedOn());
					Date prodDate = new SimpleDateFormat("dd/MM/yyyy").parse(getProductionSwitchDate());				
					Date userCreatedDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S").parse(user.getCreatedOn());
					// if user created after production switch
					if (userCreatedDate.getTime() >= prodDate.getTime()){
						user.setBeforeProductionSwitch(false);
					}else{
						user.setBeforeProductionSwitch(true);
					}

					setUserFilterProperties(user);
					deleteLoggedInInfo();
					setLoggedInInfo(user.getToken(), user.getGooruUId(), user.getEmailId(),user.getDateOfBirth());
					AppClientFactory.setLoggedInUser(user);
					return user;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GwtException(e.getMessage());
		}
//		if (content != null) {
//			String[] parsed = content.split(":");
//			if (parsed.length > 1) {
//				content = parsed[1];
//			}
//		}
		throw new GwtException(content);
	}
	
	
	@Override
	public UserDo v2Signout() {
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_SIGNOUT, getLoggedInSessionToken());
		ServiceProcessor.post(url, getRestUsername(), getRestPassword());
		deleteLoggedInInfo();
		UserDo user = v2GuestSignIn();
		setUserFilterProperties(user);
		return user;
	}
	
	
	@Override
	public String getAnalyticsURL(String type, String id) {
		
		String analyticsUrl = getAnalyticsEndPoint() +"dashboard/#/" + type +"/"+id+"?session_token="+getLoggedInSessionToken();		
		return analyticsUrl;
	}
	
}
