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
/**
 * 
 */
package org.ednovo.gooru.client.mvp.search.resource;

import java.util.List;

import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.dnd.Draggable;
import org.ednovo.gooru.client.mvp.dnd.IsDraggable;
import org.ednovo.gooru.client.mvp.home.LoginPopupUc;
import org.ednovo.gooru.client.mvp.search.AbstractSearchView;
import org.ednovo.gooru.client.mvp.search.AddResourceContainerPresenter;
import org.ednovo.gooru.shared.model.folder.FolderDo;
import org.ednovo.gooru.shared.model.folder.FolderItemDo;
import org.ednovo.gooru.shared.model.search.ResourceSearchResultDo;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Search Team
 * 
 */
public class ResourceSearchView extends AbstractSearchView<ResourceSearchResultDo> implements IsResourceSearchView {

	/**
	 * Class constructor
	 */
	public ResourceSearchView() {
		super(true);
	}

	@Override
	public IsDraggable renderSearchResult(final ResourceSearchResultDo searchResultDo) {
		final ResourceSearchResultVc resourceSearchResultVc=new ResourceSearchResultVc(searchResultDo, dragController);
//		if(searchResultDo.getRatings().getReviewCount()>0){
			resourceSearchResultVc.setUpdateReviewCount(searchResultDo.getRatings().getReviewCount());
			/*resourceSearchResultVc.getRatingWidgetView().getRatingCountLabel().getElement().removeAttribute("class");
			resourceSearchResultVc.getRatingWidgetView().getRatingCountLabel().getElement().setAttribute("style", "cursor: pointer;text-decoration: none !important;color: #1076bb;");
			resourceSearchResultVc.getRatingWidgetView().getRatingCountLabel().getElement().getStyle().setPadding(4,Unit.PX)*/;
			resourceSearchResultVc.getRatingWidgetView().getRatingCountLabel().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if(resourceSearchResultVc.getUpdateReviewCount()>0){
						getUiHandlers().showRatingAndReviewPopup(searchResultDo);
					}
				}
			});
//		}
		
		
		resourceSearchResultVc.getAddButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(AppClientFactory.isAnonymous()){
					LoginPopupUc loginPopupUc=new LoginPopupUc();
				}else{
				getUiHandlers().showAddResourceToShelfView(resourceSearchResultVc.getAddResourceContainerPanel(),searchResultDo,"resource");
				getUiHandlers().showAndHideDisclosurePanelOnCLick(resourceSearchResultVc.getDisclosurePanelClose());
				}
				}
		});
		return resourceSearchResultVc;
	}
	
	
/*	private class ShowRatingPopupEvent implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			
		}
	}*/

	@Override
	protected void refreshShelfCollections(List<FolderDo> shelfCollections) {
		
		for (Widget widget : getSearchResultPanel()) {
			if (widget instanceof Draggable && ((Draggable)widget).getDraggableUc() instanceof ResourceSearchResultVc) {
				ResourceSearchResultVc searchResultVc = (ResourceSearchResultVc) ((Draggable)widget).getDraggableUc();
				boolean added = false;
				for (FolderDo collection : shelfCollections) {
					if (collection.getType().equals("scollection")&&collection.getCollectionItems() != null) {
						for (FolderItemDo collectionItem : collection.getCollectionItems()) {
							if (collectionItem.getGooruOid().equals(searchResultVc.getResourceSearchResultDo().getGooruOid())) {
								searchResultVc.setAddedToShelf(true);
								added = true;
								break;
							}
						}
						if (added) {
							break;
						}
					}
				}
				if (!added) {
					searchResultVc.setAddedToShelf(false);
				}
			}
		}
	}

	@Override
	public void setAddResourceContainerPresenter(AddResourceContainerPresenter addResourceContainerPresenter) {
//		System.out.println("inside::::"+resourceSearchResultVc);
//		if(resourceSearchResultVc!=null){
//			resourceSearchResultVc.getAddResourceContainerPanel().clear();
//			resourceSearchResultVc.getAddResourceContainerPanel().setWidget(addResourceContainerPresenter.getWidget());
//		}
	}

}
