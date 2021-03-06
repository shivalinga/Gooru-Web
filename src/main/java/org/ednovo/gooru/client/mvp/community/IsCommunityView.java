/**
 * 
 */
package org.ednovo.gooru.client.mvp.community;

import org.ednovo.gooru.client.gin.IsViewWithHandlers;
import org.ednovo.gooru.client.mvp.home.library.contributors.LibraryContributorsView;

/**
 * @author Search Team
 * 
 */
public interface IsCommunityView extends IsViewWithHandlers<CommunityUiHandlers> {
	/**
	 * Load featured contributors {@link LibraryContributorsView}
	 * @param callBack
	 */
	void loadFeaturedContributors(String callBack, String placeToken);

	/**
	 * @function resetPassword 
	 * 
	 * @created_date : Jul 29, 2014
	 * 
	 * @description
	 * 
	 * 
	 * @param resetToken
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 * 
	*/
	
	void resetPassword(String resetToken);

	/**
	 * @function registerPopup 
	 * 
	 * @created_date : Jul 29, 2014
	 * 
	 * @description
	 * 
	 * 
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 * 
	*/
	
	void registerPopup();

}
