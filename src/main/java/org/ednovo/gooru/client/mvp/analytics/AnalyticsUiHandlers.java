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
package org.ednovo.gooru.client.mvp.analytics;

import org.ednovo.gooru.client.gin.BaseUiHandlers;
import org.ednovo.gooru.client.mvp.analytics.collectionProgress.CollectionProgressPresenter;
import org.ednovo.gooru.client.mvp.analytics.collectionSummary.CollectionSummaryPresenter;

public interface AnalyticsUiHandlers extends BaseUiHandlers{
	void getGradeCollectionJson() ;
	/**
	 * This method is used to set the clicked tab presenter on the slot.
	 * @param clickedTab
	 * @param collectionId
	 * @param selectedCollectionTitle
	 */
	void setClickedTabPresenter(String clickedTab,String collectionId,String selectedCollectionTitle);
	/**
	 * This method is used to export the OE responses.
	 * @param classpageId
	 * @param pathwayId
	 * @param timeZone
	 */
	void exportOEPathway(String classpageId,String pathwayId,String timeZone);
	
	/**
	 * This method is used to get the collection progress presenter.
	 * @return
	 */
	CollectionProgressPresenter getCollectionProgressPresenter();
	/**
	 * This method is used to get the collection summary presenter.
	 * @return
	 */
	CollectionSummaryPresenter getCollectionSummaryPresenter();
	
	void checkCollectionStaus(String classpageId,String collectionId);
}
