/**
 * 
 */
package org.ednovo.gooru.shared.model.user;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Search Team
 * 
 */
@JsonInclude(Include.NON_NULL)
public class LastModifiedUserDo extends ResponseStatusDo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4568823581577364299L;
	
	
	private String username;
	private String gooruUId;
	
	public LastModifiedUserDo(){}

	/** 
	 * This method is to get the username
	 */
	public String getUsername() {
		return username;
	}

	/** 
	 * This method is to set the username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/** 
	 * This method is to get the gooruUId
	 */
	public String getGooruUId() {
		return gooruUId;
	}

	/** 
	 * This method is to set the gooruUId
	 */
	public void setGooruUId(String gooruUId) {
		this.gooruUId = gooruUId;
	}
		
}
