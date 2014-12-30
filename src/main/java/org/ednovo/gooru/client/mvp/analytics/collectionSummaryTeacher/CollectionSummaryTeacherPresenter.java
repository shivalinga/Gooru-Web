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
package org.ednovo.gooru.client.mvp.analytics.collectionSummaryTeacher;
import java.util.ArrayList;

import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.service.AnalyticsServiceAsync;
import org.ednovo.gooru.shared.model.analytics.CollectionSummaryMetaDataDo;
import org.ednovo.gooru.shared.model.analytics.OetextDataDO;
import org.ednovo.gooru.shared.model.analytics.UserDataDo;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PresenterWidget;
public class CollectionSummaryTeacherPresenter extends PresenterWidget<IsCollectionSummaryTeacherView> implements CollectionSummaryTeacherUiHandlers{
	@Inject
	private  AnalyticsServiceAsync analyticService;
	
	private String collectionId,classpageId,pathwayId;
	 
	/**
	 * Constructor
	 * @param eventBus
	 * @param view
	 */
	@Inject
	public CollectionSummaryTeacherPresenter(EventBus eventBus, IsCollectionSummaryTeacherView view) {
		super(eventBus, view);
		getView().setUiHandlers(this);
	}

	/**
	 * Get the analytics service
	 * @return
	 */
	public AnalyticsServiceAsync getAnalyticService() {
		return analyticService;
	}

	/**
	 * Set the analytics service
	 * @param analyticService
	 */
	public void setAnalyticService(AnalyticsServiceAsync analyticService) {
		this.analyticService = analyticService;
	}

	/* (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.analytics.collectionSummaryTeacher.CollectionSummaryTeacherUiHandlers#setTeacherData(java.lang.String, java.lang.String, java.lang.String, org.ednovo.gooru.shared.model.analytics.CollectionSummaryMetaDataDo, com.google.gwt.user.client.ui.HTMLPanel)
	 */
	@Override
	public void setTeacherData(String collectionId,String classpageId,String pathwayId,final CollectionSummaryMetaDataDo result,final HTMLPanel loadingImage) {
		this.pathwayId=pathwayId;
		this.classpageId=AppClientFactory.getPlaceManager().getRequestParameter("classpageid", null);
		this.collectionId=collectionId;
		this.analyticService.getCollectionResourceData(this.collectionId,this.classpageId,"",new AsyncCallback<ArrayList<UserDataDo>>() {
			@Override
			public void onSuccess(ArrayList<UserDataDo> userData) {
				getView().setTeacherResourceData(userData,result,loadingImage);
			}
			@Override
			public void onFailure(Throwable caught) {
			}
		});		
	}

	/* (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.analytics.collectionSummaryTeacher.CollectionSummaryTeacherUiHandlers#setOEtextData(java.lang.String, java.lang.String)
	 */
	@Override
	public void setOEtextData(final String resourceGooruId,final String questionType) {
		this.analyticService.getOETextData(resourceGooruId, collectionId, classpageId, pathwayId,"AS","","", new AsyncCallback<ArrayList<OetextDataDO>>() {
			@Override
			public void onSuccess(ArrayList<OetextDataDO> result) {
				getView().setViewResponseData(result,resourceGooruId,collectionId,classpageId,pathwayId,questionType,"AS");
			}
			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}

	/* (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.analytics.collectionSummaryTeacher.CollectionSummaryTeacherUiHandlers#setHtmltopdf(java.lang.String)
	 */
	@Override
	public void setHtmltopdf(String htmlString,String collectionTitle) {
		this.analyticService.setHTMLtoPDF(htmlString,collectionTitle, new AsyncCallback<String>() {
					@Override
					public void onSuccess(String result) {
						getView().getFrame().setUrl(result);
						//Window.open(result, "_blank", "status=0,toolbar=0,menubar=0,location=0");
					}
					
					@Override
					public void onFailure(Throwable caught) {
						
					}
				});
	}
	@Override
	public void clearFrame(){
		getView().getFrame().setUrl("");
	}
}
