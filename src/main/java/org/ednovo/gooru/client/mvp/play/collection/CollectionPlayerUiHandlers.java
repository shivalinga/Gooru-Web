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
package org.ednovo.gooru.client.mvp.play.collection;

import org.ednovo.gooru.client.gin.BaseUiHandlers;
import org.ednovo.gooru.client.mvp.play.collection.event.EditCommentChildViewHandler;
import org.ednovo.gooru.client.mvp.play.collection.end.study.CloseCollectionPlayerHandler;
import org.ednovo.gooru.client.mvp.play.collection.event.ShowCollectionTabWidgetEventHandler;
import org.ednovo.gooru.client.mvp.play.collection.event.ShowResourceViewEventHandler;
import org.ednovo.gooru.client.mvp.play.collection.event.UpdateCollectionViewCountEventHandler;
import org.ednovo.gooru.client.mvp.play.collection.event.UpdateCommentChildViewHandler;
import org.ednovo.gooru.client.mvp.rating.events.PostUserReviewEventHandler;
import org.ednovo.gooru.client.mvp.rating.events.UpdateFlagIconColorEventHandler;
import org.ednovo.gooru.client.mvp.shelf.event.RefreshCollectionInShelfListInPlayHandler;

public interface CollectionPlayerUiHandlers extends BaseUiHandlers,ShowResourceViewEventHandler,UpdateCollectionViewCountEventHandler,
					ShowCollectionTabWidgetEventHandler,RefreshCollectionInShelfListInPlayHandler,UpdateFlagIconColorEventHandler,EditCommentChildViewHandler,UpdateCommentChildViewHandler,CloseCollectionPlayerHandler,PostUserReviewEventHandler{
	public void updateResourceThumbsRating(int userThumbsRataing);
	public void showLoginPopupWidget(String widgetMode);
	public void resetCollectionPlayer();
	public void getReportData(String associatedGooruOid);
	public void revealTeachOrStudypage(String page);
	public boolean isOpenEndedAnswerSubmited();
	public void navigateToNext(String direction);
	}
