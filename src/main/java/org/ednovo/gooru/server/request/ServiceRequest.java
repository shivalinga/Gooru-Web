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
package org.ednovo.gooru.server.request;



import org.ednovo.gooru.server.AppSessionHolder;
import org.ednovo.gooru.shared.exception.ServerDownException;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.engine.header.Header;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.util.Series;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Search Team
 * 
 */
public abstract class ServiceRequest {

	private static final Logger logger = LoggerFactory.getLogger(ServiceProcessor.class);

	private ClientResource clientResource = null;

	private Representation representation = null;
	
	private static final String UP="Up";
	
	private static final String DOWN="Down";
	
	private static final String WARNING="Warning";
	
	private static final String CURRENT_EVENT="current-event";
	
	private static final String STATUS="status";
	
	private static final String NAME="name";
	
	private static final String ERROR = "Error : "; 

	protected ServiceRequest() {
	}

	public JsonResponseRepresentation execute() {
		try {
			return run();
		} catch (ResourceException exception) {
//			logger.error(ERROR, exception);
			//throw new RuntimeException(exception.getMessage());
			JsonResponseRepresentation jsonResponseRepresentation=new JsonResponseRepresentation();
			int statusCode=exception.getStatus().getCode();
			jsonResponseRepresentation.setStatusCode(exception.getStatus().getCode());
			if(statusCode==504 || statusCode==502){
				String serverStatus=getApiServerStatus();
				if(serverStatus!=null && serverStatus.equalsIgnoreCase(DOWN)){
					throw new ServerDownException(statusCode,"");
				}else if(serverStatus!=null&&serverStatus.equalsIgnoreCase(WARNING)){
					//throw new ServerDownException(statusCode,"");
				}
			}else{
				String message=parseJsonErrorResponse(getClientResource().getResponse().getEntity());
				jsonResponseRepresentation.setErrorMessage(message);
				//throw new GwtException(exception.getStatus().getCode(),message);
			}
			
			return jsonResponseRepresentation;
		} catch (Exception exception) {
			logger.error(ERROR, exception);
			throw new RuntimeException(exception.getMessage());
		} finally {
			releaseClientResources();
		}
	}
	
	public String parseJsonErrorResponse(Representation errorRepresentation){
		String messageString=null;
		try{
			/**
			 *  Taking values from response header to check authorized user or not. Implemented to differentiate from blocked user or authentication issue.
			 */
			Series<org.restlet.engine.header.Header> responseHeaders=(Series<Header>)this.clientResource.getResponseAttributes().get(HeaderConstants.ATTRIBUTE_HEADERS);
			if(responseHeaders!=null){
				if(responseHeaders.getValues("Unauthorized")!=null){
					messageString = responseHeaders.getValues("Unauthorized");
				}else{
					JsonRepresentation jsonRepresentation=new JsonRepresentation(errorRepresentation.getText());
					JSONObject errorObject=jsonRepresentation.getJsonObject();
					if(errorObject!=null){
						messageString=errorObject.isNull("status")?null:errorObject.getString("status");
					}
				}
			}
			return messageString;
		}catch(Exception exception){
			logger.error(ERROR, exception);
			return messageString;
		}

	}
	
	protected String getApiServerStatus(){
		String serverStatus=null;
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(UrlToken.SERVER_STATUS_URL.getUrl());
		if(jsonResponseRep!=null){
			try {
				JSONObject serverStatusJsonObject=jsonResponseRep.getJsonRepresentation().getJsonObject();
				if (serverStatusJsonObject != null) {
					JSONObject currentEventJsonObject = serverStatusJsonObject
							.isNull(CURRENT_EVENT) ? null
							: serverStatusJsonObject
									.getJSONObject(CURRENT_EVENT);
					if (currentEventJsonObject != null) {
						JSONObject statusJsonObject = currentEventJsonObject
								.isNull(STATUS) ? null : currentEventJsonObject
								.getJSONObject(STATUS);
						if (statusJsonObject != null) {
							serverStatus = statusJsonObject.isNull(NAME) ? null
									: statusJsonObject.getString(NAME);
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return serverStatus;
	}

	public abstract JsonResponseRepresentation run() throws Exception;

	public StringRepresentation executeString() {
		try {
			return runString();
		} catch (ResourceException exception) {
			logger.error(ERROR, exception);
			//throw new RuntimeException(exception.getMessage());
			return new StringRepresentation("");
		} catch (Exception exception) {
			logger.error(ERROR, exception);
			throw new RuntimeException(exception.getMessage());
		} finally {
			releaseClientResources();
		}
	}

	public StringRepresentation runString() throws Exception{
		return new StringRepresentation("");
	}
	
	
	
	
	protected void releaseClientResources() {
		try {
			if (clientResource != null) {
				clientResource.release();
			}
			if (representation != null) {
				representation.release();
			}
			clientResource = null;
			representation = null;
		} catch (Exception e) {
			getLogger().error(e.getMessage());
		}
	}

	public ClientResource getClientResource() {
	   Series<org.restlet.engine.header.Header> requestHeaders=(Series<Header>)this.clientResource.getRequestAttributes().get(HeaderConstants.ATTRIBUTE_HEADERS);
       if(requestHeaders==null){
    	   requestHeaders= new Series<Header>(Header.class);
    	   this.clientResource.getRequestAttributes().put(HeaderConstants.ATTRIBUTE_HEADERS,requestHeaders);
       }
       Header userAgentHeader=new Header(HeaderConstants.HEADER_USER_AGENT, AppSessionHolder.getInstance().getRequest().getHeader(HeaderConstants.HEADER_USER_AGENT));
       requestHeaders.add(userAgentHeader);
	   return clientResource;
	}

	public void setClientResource(ClientResource clientResource) {
		this.clientResource = clientResource;
	}

	public Representation getRepresentation() {
		return representation;
	}

	public void setRepresentation(Representation representation) {
		this.representation = representation;
	}

	public static Logger getLogger() {
		return logger;
	}
	
	public void setBody() {
		throw new RuntimeException("Not implemented");
	}
}
