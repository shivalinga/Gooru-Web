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
package org.ednovo.gooru.client.mvp.play.resource.body;

import java.util.ArrayList;

import org.ednovo.gooru.client.gin.BaseUiHandlers;
import org.ednovo.gooru.client.mvp.rating.events.DeletePlayerStarRatingsEventHandler;
import org.ednovo.gooru.client.mvp.rating.events.DeletePlayerStarReviewHandler;
import org.ednovo.gooru.client.mvp.rating.events.OpenReviewPopUpEventHandler;
import org.ednovo.gooru.client.mvp.rating.events.PostUserReviewEvent;
import org.ednovo.gooru.client.mvp.rating.events.PostUserReviewEventHandler;
import org.ednovo.gooru.client.mvp.rating.events.PostUserReviewResourceEventHandler;
import org.ednovo.gooru.client.mvp.rating.events.UpdateUserStarReviewEventHandler;
import org.ednovo.gooru.shared.model.content.CollectionItemDo;

public interface ResourcePlayerMetadataUiHandlers extends BaseUiHandlers,OpenReviewPopUpEventHandler,UpdateUserStarReviewEventHandler,DeletePlayerStarReviewHandler,DeletePlayerStarRatingsEventHandler {
	
	public void showQuestionView(CollectionItemDo collectionItemDo);

	public void createReaction(String resourceId,String reactionText, String gooruReactionId, String collectionId, String createStudyPlayerReaction);

	public void deleteReaction(String gooruReactionId);   
	
	public void  triggerCreateReactionEvent(String resourceId,String reactionType,String eventName);
	
	public void createStarRatings(String gooruOid, int starRatingValue, boolean showThankYouToolTip,String userReview,String resourceGooruId);   
	
	public boolean isOeAnswerSubmited();

	public void updateStarRatings(String gooruOid, int starRatingValue, boolean showThankYouToolTip,String resourceGooruId);

	public void updateReview(String deleteRatingGooruOid, Integer score,String userReview,String resourceGooruId);

	public void getAvgRatingAndCount(String assocGooruOid, Integer score, String review);   
	
	public void setResourceMetaData(String resourceTitle);

	void createCollectionContentReport(String associatedGooruOid,
			String freeText, ArrayList<String> contentReportList,
			String deleteContentReportGooruOids);
	
	public void getGoogleDriveFile(String fileUrl);
	public void getResourceTagsToDisplay(String resourceId);

	public void getYoutubeFeedCallback(String utubeId);

	public void updateResourceReview(String gooruOid,Integer reviewCount);

	public void updateResourceRatings(String gooruOid,double average); 
}
