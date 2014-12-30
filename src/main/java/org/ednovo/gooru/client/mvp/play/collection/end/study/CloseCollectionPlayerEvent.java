package org.ednovo.gooru.client.mvp.play.collection.end.study;
import com.google.gwt.event.shared.GwtEvent;

/**
 * @fileName : ClosePreviewPlayerEvent.java
 *
 * @description : 
 *
 * @version : 1.0
 *
 * @date: 02-Jan-2014
 *
 * @Author Gooru Team
 *
 * @Reviewer: Gooru Team
 */
public class CloseCollectionPlayerEvent extends GwtEvent<CloseCollectionPlayerHandler> {

	private boolean isClose;
	
	public static final Type<CloseCollectionPlayerHandler> TYPE = new Type<CloseCollectionPlayerHandler>();
	
	public CloseCollectionPlayerEvent(boolean isClose) {
		this.isClose = isClose;
	}
	
	@Override
	public Type<CloseCollectionPlayerHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(CloseCollectionPlayerHandler handler) {
		handler.closePreviewPlayer(isClose);
	}
}