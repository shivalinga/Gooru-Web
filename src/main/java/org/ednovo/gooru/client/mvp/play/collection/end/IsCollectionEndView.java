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
package org.ednovo.gooru.client.mvp.play.collection.end;

import java.util.ArrayList;
import java.util.Map;

import org.ednovo.gooru.client.gin.IsViewWithHandlers;
import org.ednovo.gooru.shared.model.analytics.CollectionSummaryMetaDataDo;
import org.ednovo.gooru.shared.model.analytics.CollectionSummaryUsersDataDo;
import org.ednovo.gooru.shared.model.content.ClasspageItemDo;
import org.ednovo.gooru.shared.model.content.CollectionDo;
import org.ednovo.gooru.shared.model.library.ConceptDo;
import org.ednovo.gooru.shared.model.player.CommentsDo;
import org.ednovo.gooru.shared.model.player.CommentsListDo;
import org.ednovo.gooru.shared.util.AttemptedAnswersDo;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;


public interface IsCollectionEndView extends IsViewWithHandlers<CollectionEndUiHandlers>{
public void setCollectionMetadata(CollectionDo collectionDo);
	
	public void setViewCount(String viewCount);
	
	public void setUserProfileName(String gooruUid);
	
	public void setLikesCount(int likesCount);
	
	public void resetMetadataFields();
	
	public void displayAuthorDetails(boolean isDisplayDetails);
	
	public Anchor getFlagButton();

	public void setRelatedConceptsContent(ArrayList<ConceptDo> conceptDoList, String coursePage, String subject, String lessonId, String libraryName);

	public void isConceptsContainerVisible(boolean isVisible);

	public void setTeacherInfo(ClasspageItemDo classpageItemDo); 
	
	public void setDataInsightsSummaryUrl(String sessionId);
	
	public void setDataInsightsUrl();
	
	public void clearDashBoardIframe();
	
	public void setClasspageInsightsUrl(String classpageId, String sessionId);
	
	public void setCommentsData(CommentsListDo commentDoList, CollectionDo collectionDo, boolean isToClearCommentContainer);
	
	public void setCommentsWidget(CommentsDo commentsDo, String action);
	
	public void displaySuccessMsg(boolean isVisible);
	
	public void updateCommentChildView(String commentUid, String action);
	
	public void clearCommentContainer(boolean isClear);
	
	public void setPlayerLoginStatus(boolean isLoggedIn);
	
	public void displaySpendTime(Long hours,Long mins, Double secs);
	
	public void displayScoreCount(Integer collectionScore,Integer noOfQuestions);
	
	public void displayNextCollectionDetails(CollectionDo collectionDo,String subjectId,String courseId,String libraryType);
	
	public void hideNextCollectionContainer(boolean hide);
	
	public void showAvgReaction(String averageReaction);
	
	public void dispalyTime();

	public void changeCommentsButton(CollectionDo collectionDoObj);

	public void setSessionsData(ArrayList<CollectionSummaryUsersDataDo> result);

	public void setCollectionMetaDataByUserAndSession(ArrayList<CollectionSummaryMetaDataDo> result);
	
	public void resetCollectionMetaData();
	
	public HTMLPanel getLoadingImageLabel();
	
	public void hidePanel();
	
	public void resetData();
	
	public void showMessageWhenDataNotFound();
}
