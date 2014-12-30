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
package org.ednovo.gooru.client.mvp.analytics.collectionSummaryIndividual;
import java.util.ArrayList;

import org.ednovo.gooru.client.gin.IsViewWithHandlers;
import org.ednovo.gooru.shared.model.analytics.CollectionSummaryMetaDataDo;
import org.ednovo.gooru.shared.model.analytics.OetextDataDO;
import org.ednovo.gooru.shared.model.analytics.PrintUserDataDO;
import org.ednovo.gooru.shared.model.analytics.UserDataDo;

import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTMLPanel;


public interface IsCollectionSummaryIndividualView extends IsViewWithHandlers<CollectionSummaryIndividualUiHandlers>{
	
	/**
	 * This method is used to set individual data
	 * @param result
	 * @param loadingImage
	 */
	void setIndividualData(ArrayList<UserDataDo> result,HTMLPanel loadingImage);
	
	/**
	 * This method is used to set individual user collection meta data.
	 * @param result
	 * @param printUserDataDO
	 */
	void setIndividualCollectionMetaData(ArrayList<CollectionSummaryMetaDataDo> result,PrintUserDataDO printUserDataDO);
	
	/**
	 * This method is used to set view responses data
	 * @param result
	 * @param resourceGooruId
	 * @param collectionId
	 * @param classpageId
	 * @param pathwayId
	 * @param questionType
	 * @param isSummary
	 */
	void setViewResponseData(ArrayList<OetextDataDO> result,String resourceGooruId, String collectionId, String classpageId,String pathwayId, String questionType, boolean isSummary,String session);

    /**
     * This method is used to enable and disable email button
     * @param isSummary
     */
    void enableAndDisableEmailButton(boolean isSummary);

	/**
	 * This method is used to set the pdf attachment to email popup
	 * @param path
	 */
	void setPdfForEmail(String path);
	/**
	 * This method is used to set empty error message.
	 */
	void setErrorMessage();
	/**
	 * Get Ifame tag
	 * @return
	 */
	Frame getFrame();
}
