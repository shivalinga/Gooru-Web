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
import java.util.ArrayList;

import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.analytics.collectionProgress.CollectionProgressPresenter;
import org.ednovo.gooru.client.mvp.analytics.collectionSummary.CollectionSummaryPresenter;
import org.ednovo.gooru.client.service.AnalyticsServiceAsync;
import org.ednovo.gooru.client.service.ClasspageServiceAsync;
import org.ednovo.gooru.shared.model.analytics.CollectionSummaryMetaDataDo;
import org.ednovo.gooru.shared.model.analytics.GradeJsonData;
import org.ednovo.gooru.shared.model.content.ClasspageDo;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PresenterWidget;
public class AnalyticsPresenter extends PresenterWidget<IsAnalyticsView> implements AnalyticsUiHandlers{
	
	private CollectionProgressPresenter collectionProgressPresenter;
	
	private CollectionSummaryPresenter collectionSummaryPresenter;
	
	public static final  Object COLLECTION_PROGRESS_SLOT = new Object();
	
	public static final  Object COLLECTION_SUMMARY_SLOT = new Object();
	
	
	final String SUMMARY="Summary",PROGRESS="Progress",CLEARSUMMARY="ClearSummary",CLEARPROGRESS="ClearProgress";
	
	ClasspageDo classpageDo=null;
	
	@Inject
	private  AnalyticsServiceAsync analyticService;
	
	@Inject
	private ClasspageServiceAsync classpageService;
	
	/**
	 * AnalyticsPresenter constructor
	 * @param eventBus
	 * @param view
	 * @param collectionProgressPresenter
	 * @param collectionSummaryPresenter
	 * @param analyticsUnitAssignmentsPresenter
	 * @param personalizeUnitPresenter
	 */
	@Inject
	public AnalyticsPresenter(EventBus eventBus, IsAnalyticsView view,CollectionProgressPresenter collectionProgressPresenter,CollectionSummaryPresenter collectionSummaryPresenter) {
		super(eventBus, view);
		getView().setUiHandlers(this);
		this.collectionProgressPresenter=collectionProgressPresenter;
		this.collectionSummaryPresenter=collectionSummaryPresenter;
	}
	@Override
	public void getGradeCollectionJson() {
		clearSlot(COLLECTION_PROGRESS_SLOT);
		setInSlot(COLLECTION_PROGRESS_SLOT, null,false);
		getView().getCollectionProgressSlot().clear();
		
		clearSlot(COLLECTION_SUMMARY_SLOT);
		setInSlot(COLLECTION_SUMMARY_SLOT, null,false);
		getView().getCollectionSummarySlot().clear();
		
		getView().resetData();
		String classpageId=AppClientFactory.getPlaceManager().getRequestParameter("classpageid", null);
		AppClientFactory.getInjector().getAnalyticsService().getAnalyticsGradeData(classpageId,"", new AsyncCallback<ArrayList<GradeJsonData>>() {
			@Override
			public void onSuccess(ArrayList<GradeJsonData> result) {
				if(result.size()>0){
					if(result.get(0).getAggregateData()!=null && result.get(0).getAggregateData().equalsIgnoreCase("false")){
						getView().setNoDataText();
					}else{
						getView().setGradeCollectionData(result);
					}
				}
			}
			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}

	/* (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.analytics.AnalyticsUiHandlers#setClickedTabPresenter(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void setClickedTabPresenter(String clickedTab,String collectionId,String selectedCollectionTitle) {
		if(clickedTab!=null){
			if(clickedTab.equalsIgnoreCase(SUMMARY)){
				clearSlot(COLLECTION_SUMMARY_SLOT);	
				collectionSummaryPresenter.clearFrames();
				collectionSummaryPresenter.setCollectionSummaryData(collectionId,"");
				setInSlot(COLLECTION_SUMMARY_SLOT, collectionSummaryPresenter,false);
			}else if(clickedTab.equalsIgnoreCase(PROGRESS)){
				clearSlot(COLLECTION_PROGRESS_SLOT);
				collectionProgressPresenter.setCollectionProgressData(collectionId,"",false,selectedCollectionTitle);
				setInSlot(COLLECTION_PROGRESS_SLOT, collectionProgressPresenter,false);
			}else if(clickedTab.equalsIgnoreCase(CLEARPROGRESS)){
				clearSlot(COLLECTION_PROGRESS_SLOT);
				setInSlot(COLLECTION_PROGRESS_SLOT, null,false);
				getView().getCollectionProgressSlot().clear();
			}else if(clickedTab.equalsIgnoreCase(CLEARSUMMARY)){
				collectionSummaryPresenter.clearFrames();
				clearSlot(COLLECTION_SUMMARY_SLOT);
				setInSlot(COLLECTION_SUMMARY_SLOT, null,false);
				getView().getCollectionSummarySlot().clear();
			}
		}
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
	 * @see com.gwtplatform.mvp.client.PresenterWidget#onHide()
	 */
	@Override
	protected void onHide() {
		super.onHide();
		clearSlot(COLLECTION_PROGRESS_SLOT);	
		clearSlot(COLLECTION_SUMMARY_SLOT);	
	}
	
	/**
	 * Get the class page service
	 * @return
	 */
	public ClasspageServiceAsync getClasspageService() {
		return classpageService;
	}

	/**
	 * Sets the class page service
	 * @param classpageService
	 */
	public void setClasspageService(ClasspageServiceAsync classpageService) {
		this.classpageService = classpageService;
	}
	/* (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.analytics.AnalyticsUiHandlers#exportOEPathway(java.lang.String, java.lang.String)
	 */
	@Override
	public void exportOEPathway(String classpageId, String pathwayId,String timeZone) {
		this.analyticService.exportPathwayOE(classpageId, pathwayId,timeZone,new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				getView().getFrame().setUrl(result);
				//Window.open(result, "_blank", "directories=0,titlebar=0,toolbar=0,location=0,status=0,menubar=0,scrollbars=no,resizable=no,width=0,height=0");
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}
	/* (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.analytics.AnalyticsUiHandlers#getCollectionProgressPresenter()
	 */
	@Override
	public CollectionProgressPresenter getCollectionProgressPresenter() {
		return collectionProgressPresenter;
	}
	/* (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.analytics.AnalyticsUiHandlers#getCollectionSummaryPresenter()
	 */
	@Override
	public CollectionSummaryPresenter getCollectionSummaryPresenter() {
		return collectionSummaryPresenter;
	}
	
	@Override
	public void checkCollectionStaus(String classpageId, String collectionId) {
		AppClientFactory.getInjector().getAnalyticsService().getAssignmentAverageData(classpageId, "", collectionId, new AsyncCallback<CollectionSummaryMetaDataDo>() {
			@Override
			public void onSuccess(CollectionSummaryMetaDataDo result) {
				if(result!=null && result.getViews()!=0){
					setClickedTabPresenter(CLEARPROGRESS,"","");
					setClickedTabPresenter(CLEARSUMMARY,"","");
					getView().resetDataText();
				}else{
					getView().setNoDataText();
				}
			}
			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}
}
