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

import org.ednovo.gooru.client.gin.BaseUiHandlers;
import org.ednovo.gooru.shared.model.analytics.PrintUserDataDO;

public interface CollectionSummaryUiHandlers extends BaseUiHandlers{
 
/**
 * This method is sued to set collection summary data.
 * @param collectionId
 * @param pathwayId
 */
void setCollectionSummaryData(String collectionId,String pathwayId);
  /**
   * This method is used to load user sessions.
 * @param collectionId
 * @param classId
 * @param userId
 * @param pathwayId
 * @param printUserDataDO
 */
void loadUserSessions(String collectionId,String classId,String userId,String pathwayId,PrintUserDataDO printUserDataDO);
  /**
   * This method is used to set teacher data.
 * @param collectionId
 * @param classpageId
 * @param pathwayId
 */
void setTeacherData(String collectionId,String classpageId,String pathwayId);
  /**
   * This method is used to set individual student data.
 * @param collectionId
 * @param classpageId
 * @param userId
 * @param sessionId
 * @param pathwayId
 * @param printUserDataDO
 */
void setIndividualData(String collectionId,String classpageId,String userId,String sessionId,String pathwayId,PrintUserDataDO printUserDataDO);
/**
 * This method is used to export the collection summary data.
 * @param collectionId
 * @param classpageId
 * @param userId
 * @param sessionId
 * @param pathwayId
 * @param timeZone
 */
void exportCollectionSummary(String collectionId,String classpageId,String userId,String sessionId,String pathwayId,String timeZone);

public void clearFrames();
}
