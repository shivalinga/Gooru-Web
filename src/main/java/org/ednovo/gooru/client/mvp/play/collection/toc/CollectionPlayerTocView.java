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
package org.ednovo.gooru.client.mvp.play.collection.toc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BaseViewWithHandlers;
import org.ednovo.gooru.client.mvp.play.collection.preview.PreviewPlayerPresenter;
import org.ednovo.gooru.client.mvp.play.collection.preview.home.ResourceCurosal;
import org.ednovo.gooru.client.mvp.play.collection.preview.metadata.NavigationConfirmPopup;
import org.ednovo.gooru.client.uc.HTMLEventPanel;
import org.ednovo.gooru.client.uc.PlayerBundle;
import org.ednovo.gooru.client.uc.TocCollectionEndView;
import org.ednovo.gooru.client.uc.TocCollectionHomeView;
import org.ednovo.gooru.client.uc.TocResourceView;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.content.CollectionDo;
import org.ednovo.gooru.shared.model.content.CollectionItemDo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

public class CollectionPlayerTocView extends BaseViewWithHandlers<CollectionPlayerTocUiHandlers> implements IsCollectionPlayerTocView{

	@UiField FlowPanel navgationTocContainer,carouselContainer;
	@UiField Label previousButton,nextButton,resourceCountLabel;
	
	
	private int selectedWidgetIndex=-1;
	
	private static CollectionPlayerTocViewUiBinder uiBinder = GWT.create(CollectionPlayerTocViewUiBinder.class);

	interface CollectionPlayerTocViewUiBinder extends UiBinder<Widget, CollectionPlayerTocView> {
	}
	
	private MessageProperties i18n = GWT.create(MessageProperties.class);
	
	@Inject
	public CollectionPlayerTocView(){
		setWidget(uiBinder.createAndBindUi(this));
		
		previousButton.getElement().setId("lblPreviousButton");
		navgationTocContainer.getElement().setId("fpnlNavgationTocContainer");
		nextButton.getElement().setId("lblNextButton");
	}
	public void clearNavigationPanel(){
		navgationTocContainer.clear();
	}
	public void hideResourceCountLabel(boolean hide){
		resourceCountLabel.setVisible(!hide);
	}
	
	public void clearMarginLeft(){
		navgationTocContainer.getElement().getStyle().clearMarginLeft();
	}
	@Override
	public void setNavigationResources(CollectionDo collectionDo,boolean isCollectionHome){
		if(collectionDo!=null){
			int resourcesSize=collectionDo.getCollectionItems()!=null?collectionDo.getCollectionItems().size():0;
			if(navgationTocContainer.getWidgetCount()==0){
				int resourceCount=0;
				int questionCount=0;
				navgationTocContainer.clear();
				nextButton.setVisible(true);
				previousButton.setVisible(true);
				List<CollectionItemDo> collectionItems=collectionDo.getCollectionItems();
			
				TocCollectionHomeView tocCollectionHomeView=new TocCollectionHomeView(collectionDo.getThumbnails().getUrl());
				if(!isCollectionHome){
					tocCollectionHomeView.hideResourceThumbnailContainer(true);
				}
				tocCollectionHomeView.addClickHandler(new HomeRequest());
				navgationTocContainer.add(tocCollectionHomeView);
				for(int i=0;i<collectionItems.size();i++){
					if(collectionDo.getCollectionItems().get(i).getResource().getResourceFormat()!=null){
						if(collectionDo.getCollectionItems().get(i).getResource().getResourceFormat().getDisplayName().equalsIgnoreCase("Question")){
							questionCount++;
						}else{
							resourceCount++;
						}
					}
					CollectionItemDo collectionItemDo=collectionItems.get(i);
					TocResourceView tocResoruceView=new TocResourceView(collectionItemDo,i+1,true,false);
					tocResoruceView.addClickHandler(new ResourceRequest(collectionItemDo));
					tocResoruceView.setCollectionItemId(collectionItemDo.getCollectionItemId());
					if(!isCollectionHome){
						tocResoruceView.hideResourceThumbnailContainer(true);
					}
					navgationTocContainer.add(tocResoruceView);
				}
				TocCollectionEndView tocCollectionEndView=new TocCollectionEndView(collectionDo.getThumbnails().getUrl());
				tocCollectionEndView.addClickHandler(new EndRequest());
				if(!isCollectionHome){
					tocCollectionEndView.hideResourceThumbnailContainer(true);
				}
				navgationTocContainer.add(tocCollectionEndView);
				//resources width with padding and margin constitutes 100px for each and collection home and end with padding and margin width
				//have 100px each. navgationTocContainer width is derived from this.
				//if(resourcesSize>7){
					navgationTocContainer.getElement().removeAttribute("style");
					new ResourceCurosal(nextButton, previousButton, navgationTocContainer, resourcesSize+2, 100,carouselContainer);
//				}else{
//					nextButton.getElement().getStyle().setVisibility(Visibility.HIDDEN);
//					previousButton.getElement().getStyle().setVisibility(Visibility.HIDDEN);
//					navgationTocContainer.getElement().setAttribute("style", "width:"+((resourcesSize+2)*100)+"px !important;");
//				}
				String resourceString = resourceCount == 1? resourceCount + " " + i18n.GL1110().toLowerCase() : resourceCount + " " + i18n.GL0174().toLowerCase();
				String questionString = questionCount == 1? questionCount + " " + i18n.GL0308().toLowerCase() : questionCount + " " + i18n.GL1042().toLowerCase();
				String finalMessage = "";
				if (resourceCount >0 && questionCount > 0){
					finalMessage = resourceString + " " + i18n.GL_GRR_AND() + " " + questionString + " " + i18n.GL0578() + i18n.GL_SPL_SEMICOLON()+" ";
				}else if (resourceCount >0){
					finalMessage = resourceString + " " + i18n.GL0578() + i18n.GL_SPL_SEMICOLON()+" ";
				}else if (questionCount >0){
					finalMessage = questionString + " " + i18n.GL0578() + i18n.GL_SPL_SEMICOLON()+" ";
				}
				resourceCountLabel.setText(finalMessage);
				
			}else{
				setResourceThumbnailVisibility(isCollectionHome);
			}
		}
	}
	public void setResourceThumbnailVisibility(boolean visibility){
		int widgetsCount=navgationTocContainer.getWidgetCount();
		for(int i=0;i<widgetsCount;i++){
			Widget widget=navgationTocContainer.getWidget(i);
			if(widget instanceof TocCollectionHomeView){
				((TocCollectionHomeView)widget).hideResourceThumbnailContainer(!visibility);
			}else if(widget instanceof TocResourceView){
				((TocResourceView)widget).hideResourceThumbnailContainer(!visibility);
			} else if(widget instanceof TocCollectionEndView){
				((TocCollectionEndView)widget).hideResourceThumbnailContainer(!visibility);
			}
		}
		
	}
	@Override
	public void setResourceActive(String collectionId,String collectionItemid,boolean isCollectionHome){
		if(selectedWidgetIndex!=-1){
			Widget widget=navgationTocContainer.getWidget(selectedWidgetIndex);
			widget.removeStyleName(PlayerBundle.INSTANCE.getPlayerStyle().tocResourceSelected());
		}
		if(collectionItemid!=null){
			int widgetCount=navgationTocContainer.getWidgetCount();
			for(int i=0;i<widgetCount;i++){
				Widget widget=navgationTocContainer.getWidget(i);
				if(widget instanceof TocResourceView){
					TocResourceView resourceView=(TocResourceView)widget;
					if(collectionItemid.equals(resourceView.getCollectionItemId())){
						selectedWidgetIndex=i;
						resourceView.addStyleName(PlayerBundle.INSTANCE.getPlayerStyle().tocResourceSelected());
						return;
					}
				}
			}		
		}else if(isCollectionHome){
			selectedWidgetIndex=0;
			Widget widget=navgationTocContainer.getWidget(selectedWidgetIndex);
			widget.addStyleName(PlayerBundle.INSTANCE.getPlayerStyle().tocResourceSelected());
		}else{
			int widgetCount=navgationTocContainer.getWidgetCount();
			selectedWidgetIndex=widgetCount-1;
			Widget widget=navgationTocContainer.getWidget(selectedWidgetIndex);
			widget.addStyleName(PlayerBundle.INSTANCE.getPlayerStyle().tocResourceSelected());
		}
		
	}
	
	public class ResourceRequest implements ClickHandler{
		private CollectionItemDo collectionItemDo;
		public ResourceRequest(CollectionItemDo collectionItemDo){
			this.collectionItemDo=collectionItemDo;
		}
		public void onClick(ClickEvent event){
			Map<String,String> params = new LinkedHashMap<String,String>();
			String collectionId=AppClientFactory.getPlaceManager().getRequestParameter("id", null);
			params.put("id", collectionId);
			params = PreviewPlayerPresenter.setConceptPlayerParameters(params);
			String viewToken=AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken();
			if(collectionItemDo.getNarration()!=null&&!collectionItemDo.getNarration().trim().equals("")){
				params.put("rid", collectionItemDo.getCollectionItemId());
				params.put("tab", "narration");
				final PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(viewToken, params);
				if(!getUiHandlers().isOpenEndedAnswerSubmited()){
					NavigationConfirmPopup confirmPopup=new NavigationConfirmPopup() {
						@Override
						public void navigateToNextResource() {
							super.hide();
							AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, true);
						}
					};
				}else{
					AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, true);
				}
			}else{
				params.put("rid", collectionItemDo.getCollectionItemId());
				final PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(viewToken, params);
				if(!getUiHandlers().isOpenEndedAnswerSubmited()){
					NavigationConfirmPopup confirmPopup=new NavigationConfirmPopup() {
						@Override
						public void navigateToNextResource() {
							super.hide();
							AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, true);
						}
					};
				}else{
					AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, true);
				}
			}
		}
	}
	
	public class EndRequest implements ClickHandler{
		public void onClick(ClickEvent event){	
			Map<String,String> params = new LinkedHashMap<String,String>();
			String collectionId=AppClientFactory.getPlaceManager().getRequestParameter("id", null);
			params.put("id", collectionId);
			params.put("view", "end");
			params = PreviewPlayerPresenter.setConceptPlayerParameters(params);
			String viewToken=AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken();
			//PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(viewToken, params);
			final PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(viewToken, params);
			if(!getUiHandlers().isOpenEndedAnswerSubmited()){
				NavigationConfirmPopup confirmPopup=new NavigationConfirmPopup() {
					@Override
					public void navigateToNextResource() {
						super.hide();
						AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, true);
					}
				};
			}else{
				AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, true);
			}
		}
	}
	
	public class HomeRequest implements ClickHandler{
		public void onClick(ClickEvent event){
			Map<String,String> params = new LinkedHashMap<String,String>();
			String collectionId=AppClientFactory.getPlaceManager().getRequestParameter("id", null);
			params.put("id", collectionId);
			params = PreviewPlayerPresenter.setConceptPlayerParameters(params);
			String viewToken=AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken();
			final PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(viewToken, params);
			if(!getUiHandlers().isOpenEndedAnswerSubmited()){
				NavigationConfirmPopup confirmPopup=new NavigationConfirmPopup() {
					@Override
					public void navigateToNextResource() {
						super.hide();
						AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, true);
					}
				};
			}else{
				AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, true);
			}
		}
	}
	
}
