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
package org.ednovo.gooru.client.mvp.wrap;

import org.ednovo.gooru.client.gin.IsView;
import org.ednovo.gooru.client.mvp.home.event.HeaderTabType;
import org.ednovo.gooru.shared.model.user.UserDo;

import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * @author Search Team
 * 
 */
public interface IsWrapView extends IsView {
	
	/**
	 * Enable or disable logIn popup
	 */
	void invokeLogin();
	
	/**
	 * Enable or disable register popup
	 */
	void invokeRegister();
	
	/**
	 * Set loggedIn user details
	 * @param user instance of {@link UserDo}
	 */
	void setLoginData(UserDo user);
	
	/**
	 * Set enable or disable search bar
	 * @param activate if is true active bar is visible or invisible 
	 */
	void activateSearchBar(boolean activate);
	
	/**
	 * Set enabled or disabled classic url link
	 * @param activate if is true classic url link is visible or invisible 
	 */
	void activateClassicButton(boolean activate);
	
	/**
	 * Create gooru guide view 
	 * 
	 */
	void invokeGooruGuideBubble();
	
	/**
	 * Set Dots Panel Selection in Header
	 * 
	 */
	void setDotsPanelSelection(HeaderTabType tabType);

	/**
	 * Set Discover link from the library page
	 * 
	 */
	void setDiscoverLinkFromLibrary(String discoverLink);

	HTMLPanel getSearchFiltersPanel();

	HTMLPanel getCollectionSearchFiltersPanel();
	
}
