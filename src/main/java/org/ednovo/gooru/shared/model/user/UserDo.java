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
package org.ednovo.gooru.shared.model.user;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Search Team
 * 
 */
@JsonInclude(Include.NON_NULL)
public class UserDo extends ResponseStatusDo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8351101332489190167L;

	@JsonProperty("partyUid")
	private String userUid;
	private String firstName;
	private String lastName;
	private String username;
	private String userName;
	private String emailId = "";
	private Integer confirmStatus;
	private String registerToken;
	private Set<UserRoleAssocDo> userRoleSet;
	private String userRoleSetString;
	private UserGroupDo userGroup;
	private String importCode;
	private Integer addedBySystem;
	private Integer accountTypeId;
	private String profileImageUrl;
	private OrganizationDo organization;
	private String token;
	private boolean availability;
	private String gooruUId;
	private boolean collaboratorCheck;
	private String externalId;
	private FilterSettings settings;
	private Integer viewFlag;
	private String loginType;
	private String usernameDisplay;
	private List<CustomFieldDo> customFields;
	private Integer active;
	
	private Integer statusCode;
	
	private String createdOn;
	
	private boolean isBeforeProductionSwitch=false;
	
	private boolean profileUserVisibility; 

	private String dateOfBirth;
	
	private String accountCreatedType;
	
	private String organizationName;
	
	private UserMetaDo meta;
	
	private String accessToken;
	
	private String refreshToken;
	
	private String errorMsg;
	
	public UserDo(){}
	
	
	/** 
	 * This method is to get the isBeforeProductionSwitch
	 */
	public boolean isBeforeProductionSwitch() {
		return isBeforeProductionSwitch;
	}

	/** 
	 * This method is to set the isBeforeProductionSwitch
	 */
	public void setBeforeProductionSwitch(boolean isBeforeProductionSwitch) {
		this.isBeforeProductionSwitch = isBeforeProductionSwitch;
	}

	/** 
	 * This method is to get the createdOn
	 */
	public String getCreatedOn() {
		return createdOn;
	}

	/** 
	 * This method is to set the createdOn
	 */
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getGooruUId() {
		return gooruUId;
	}

	public void setGooruUId(String gooruUId) {
		this.gooruUId = gooruUId;
	}

	public String getUserUid() {
		return userUid;
	}

	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Integer getConfirmStatus() {
		return confirmStatus;
	}

	public void setConfirmStatus(Integer confirmStatus) {
		this.confirmStatus = confirmStatus;
	}

	public String getRegisterToken() {
		return registerToken;
	}

	public void setRegisterToken(String registerToken) {
		this.registerToken = registerToken;
	}

	public Set<UserRoleAssocDo> getUserRoleSet() {
		return userRoleSet;
	}

	public void setUserRoleSet(Set<UserRoleAssocDo> userRoleSet) {
		this.userRoleSet = userRoleSet;
	}

	public String getUserRoleSetString() {
		return userRoleSetString;
	}

	public void setUserRoleSetString(String userRoleSetString) {
		this.userRoleSetString = userRoleSetString;
	}

	public UserGroupDo getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(UserGroupDo userGroup) {
		this.userGroup = userGroup;
	}

	public String getImportCode() {
		return importCode;
	}

	public void setImportCode(String importCode) {
		this.importCode = importCode;
	}

	public Integer getAddedBySystem() {
		return addedBySystem;
	}

	public void setAddedBySystem(Integer addedBySystem) {
		this.addedBySystem = addedBySystem;
	}

	public Integer getAccountTypeId() {
		return accountTypeId;
	}

	public void setAccountTypeId(Integer accountTypeId) {
		this.accountTypeId = accountTypeId;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public OrganizationDo getOrganization() {
		return organization;
	}

	public void setOrganization(OrganizationDo organization) {
		this.organization = organization;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setAvailability(boolean availability) {
		this.availability = availability;
	}

	public boolean isAvailability() {
		return availability;
	}

	public void setCollaboratorCheck(boolean collaboratorCheck) {
		this.collaboratorCheck = collaboratorCheck;
	}

	public boolean isCollaboratorCheck() {
		return collaboratorCheck;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setSettings(FilterSettings settings) {
		this.settings = settings;
	}

	public FilterSettings getSettings() {
		return settings;
	}

	public Integer getViewFlag() {
		return viewFlag;
	}

	public void setViewFlag(Integer viewFlag) {
		this.viewFlag = viewFlag;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	/** 
	 * This method is to get the profileUserVisibility
	 */
	public boolean isProfileUserVisibility() {
		return profileUserVisibility;
	}

	/** 
	 * This method is to set the profileUserVisibility
	 */
	public void setProfileUserVisibility(boolean profileUserVisibility) {
		this.profileUserVisibility = profileUserVisibility;
	}

	public String getUsernameDisplay() {
		return usernameDisplay;
	}

	public void setUsernameDisplay(String usernameDisplay) {
		this.usernameDisplay = usernameDisplay;
	}

	public List<CustomFieldDo> getCustomFields() {
		return customFields;
	}

	public void setCustomFields(List<CustomFieldDo> customFields) {
		this.customFields = customFields;
	}

	/** 
	 * This method is to get the user date of birth
	 */
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	/** 
	 * This method is to set the user dateofbirth
	 */
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getAccountCreatedType() {
		return accountCreatedType;
	}

	public void setAccountCreatedType(String accountCreatedType) {
		this.accountCreatedType = accountCreatedType;
	}


	/** 
	 * This method is to get the organizationName
	 */
	public String getOrganizationName() {
		return organizationName;
	}


	/** 
	 * This method is to set the organizationName
	 */
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public UserMetaDo getMeta() {
		return meta;
	}

	public void setMeta(UserMetaDo meta) {
		this.meta = meta;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	/**
	 * @return the active
	 */
	public Integer getActive() {
		return active;
	}


	/**
	 * @param active the active to set
	 */
	public void setActive(Integer active) {
		this.active = active;
	}


	/**
	 * @return the statusCode
	 */
	public Integer getStatusCode() {
		return statusCode;
	}


	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}


	/** 
	 * This method is to get the accessToken
	 */
	public String getAccessToken() {
		return accessToken;
	}


	/** 
	 * This method is to set the accessToken
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}


	/** 
	 * This method is to get the refreshToken
	 */
	public String getRefreshToken() {
		return refreshToken;
	}


	/** 
	 * This method is to set the refreshToken
	 */
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}


	/**
	 * @return the errorMsg
	 */
	public String getErrorMsg() {
		return errorMsg;
	}


	/**
	 * @param errorMsg the errorMsg to set
	 */
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	
}
