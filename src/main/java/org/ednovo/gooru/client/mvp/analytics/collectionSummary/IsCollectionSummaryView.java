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
package org.ednovo.gooru.client.mvp.analytics.collectionSummary;
import java.util.ArrayList;

import org.ednovo.gooru.client.gin.IsViewWithHandlers;
import org.ednovo.gooru.shared.model.analytics.CollectionSummaryMetaDataDo;
import org.ednovo.gooru.shared.model.analytics.CollectionSummaryUsersDataDo;
import org.ednovo.gooru.shared.model.analytics.UserDataDo;

import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTMLPanel;


public interface IsCollectionSummaryView extends IsViewWithHandlers<CollectionSummaryUiHandlers>{
	/**
	 * This method is used to set the users data.
	 * @param result
	 */
	void setUsersData(ArrayList<CollectionSummaryUsersDataDo> result);
	/**
	 * This method is used to set collection meta data.
	 * @param result
	 * @param pathwayId
	 * @param classpageId
	 */
	void setCollectionMetaData(CollectionSummaryMetaDataDo result,String pathwayId,String classpageId);
	/**
	 * This method is used to set collection resource data.
	 * @param result
	 */
	void setCollectionResourcesData(ArrayList<UserDataDo> result);
	/**
	 * This method is used to set users session data.
	 * @param result
	 */
	void setUserSessionsData(ArrayList<CollectionSummaryUsersDataDo> result);
	/**
	 * This method is used to get the loading image.
	 * @return
	 */
	HTMLPanel getLoadinImage();
	/**
	 * This method is used to get the frame
	 * @return
	 */
	Frame getFrame();
}
