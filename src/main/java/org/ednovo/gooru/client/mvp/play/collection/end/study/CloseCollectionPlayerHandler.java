package org.ednovo.gooru.client.mvp.play.collection.end.study;
import com.google.gwt.event.shared.EventHandler;
/**
 * @fileName : ClosePreviewPlayerHandler.java
 *
 * @description : 
 *
 * @version : 1.0
 *
 * @date: 06-Jan-2014
 *
 * @Author: Gooru Team
 *
 * @Reviewer: Gooru Team
 */
public interface CloseCollectionPlayerHandler extends EventHandler {

	/**
	 * @function closePreviewPlayer 
	 * 
	 * @created_date : 07-Jan-2014
	 * 
	 * @description
	 * 
	 * @return : void
	 */
	void closePreviewPlayer(boolean isClose);
}
