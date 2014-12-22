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
package org.ednovo.gooru.shared.model.user;

import java.io.Serializable;

public class FilterSettings implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8381391705849022374L;

	private String restEndPoint;

	private String homeEndPoint;

	private String domainName;

	private String classicEndPoint;

	private String docViewerPoint;
	
	private String apiKeyPoint;
	
	private String googleSignin;
	
	private String profileImageUrl;
	
	private String cdnEndPoint;

	private String googleAnalticsAdditionalAccounts;
	
	private String dataApiKey;

	private String gooruReleaseVersion;
	
	private String docViewerHome;
	
	private String facebookAppId;
	
	private String facebookFeedUrl;
	
	private String taxonomyPreferences;
	
	private String analyticsEndPoint;
	
	private String communityLibraryGooruOid;
	
	private String assessementEndPoint;
	
	public FilterSettings(){}
	
	public String getApiKeyPoint() {
		return apiKeyPoint;
	}

	public void setApiKeyPoint(String apiKeyPoint) {
		this.apiKeyPoint = apiKeyPoint;
	}

	public String getGoogleAnalticsAdditionalAccounts() {
		return googleAnalticsAdditionalAccounts;
	}

	public void setRestEndPoint(String restEndPoint) {
		this.restEndPoint = restEndPoint;
	}

	public String getRestEndPoint() {
		return restEndPoint;
	}

	public void setHomeEndPoint(String homeEndPoint) {
		this.homeEndPoint = homeEndPoint;
	}

	public String getHomeEndPoint() {
		return homeEndPoint;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setClassicEndPoint(String classicEndPoint) {
		this.classicEndPoint = classicEndPoint;
	}

	public String getClassicEndPoint() {
		return classicEndPoint;
	}

	public void setDocViewerPoint(String docViewerPoint) {
		this.docViewerPoint = docViewerPoint;
	}

	public String getDocViewerPoint() {
		return docViewerPoint;
	}

	public String getGoogleAnalticsExtraAccounts() {
		return googleAnalticsAdditionalAccounts;
	}

	public void setGoogleAnalticsAdditionalAccounts(String googleAnalticsAdditionalAccount) {
		this.googleAnalticsAdditionalAccounts = googleAnalticsAdditionalAccount;
	}

	/** 
	 * This method is to get the googleSignin
	 */
	public String getGoogleSignin() {
		return googleSignin;
	}

	/** 
	 * This method is to set the googleSignin
	 */
	public void setGoogleSignin(String googleSignin) {
		this.googleSignin = googleSignin;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public String getCdnEndPoint() {
		return cdnEndPoint;
	}

	public void setCdnEndPoint(String cdnEndPoint) {
		this.cdnEndPoint = cdnEndPoint;
	}

	/** 
	 * This method is to get the dataApiKey
	 */
	public String getDataApiKey() {
		return dataApiKey;
	}

	/** 
	 * This method is to set the dataApiKey
	 */
	public void setDataApiKey(String dataApiKey) {
		this.dataApiKey = dataApiKey;
	}

	/** 
	 * This method is to get the gooruReleaseVersion
	 */
	public String getGooruReleaseVersion() {
		return gooruReleaseVersion;
	}

	/** 
	 * This method is to set the gooruReleaseVersion
	 */
	public void setGooruReleaseVersion(String gooruReleaseVersion) {
		this.gooruReleaseVersion = gooruReleaseVersion;
	}

	/** 
	 * This method is to get the docViewerHome
	 */
	public String getDocViewerHome() {
		return docViewerHome;
	}

	/** 
	 * This method is to set the docViewerHome
	 */
	public void setDocViewerHome(String docViewerHome) {
		this.docViewerHome = docViewerHome;
	}

	public String getFacebookAppId() {
		return facebookAppId;
	}

	public void setFacebookAppId(String facebookAppId) {
		this.facebookAppId = facebookAppId;
	}

	public String getFacebookFeedUrl() {
		return facebookFeedUrl;
	}

	public void setFacebookFeedUrl(String facebookFeedUrl) {
		this.facebookFeedUrl = facebookFeedUrl;
	}

	public String getTaxonomyPreferences() {
		return taxonomyPreferences;
	}

	public void setTaxonomyPreferences(String taxonomyPreferences) {
		this.taxonomyPreferences = taxonomyPreferences;
	}

	public String getAnalyticsEndPoint() {
		return analyticsEndPoint;
	}

	public void setAnalyticsEndPoint(String analyticsEndPoint) {
		this.analyticsEndPoint = analyticsEndPoint;
	}

	public String getCommunityLibraryGooruOid() {
		return communityLibraryGooruOid;
	}

	public void setCommunityLibraryGooruOid(String communityLibraryGooruOid) {
		this.communityLibraryGooruOid = communityLibraryGooruOid;
	}

	public String getAssessementEndPoint() {
		return assessementEndPoint;
	}

	public void setAssessementEndPoint(String assessementEndPoint) {
		this.assessementEndPoint = assessementEndPoint;
	}
	
}
