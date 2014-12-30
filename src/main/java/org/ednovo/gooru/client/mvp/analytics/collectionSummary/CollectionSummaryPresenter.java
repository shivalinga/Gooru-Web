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
import java.util.ArrayList;

import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.analytics.collectionSummaryIndividual.CollectionSummaryIndividualPresenter;
import org.ednovo.gooru.client.mvp.analytics.collectionSummaryTeacher.CollectionSummaryTeacherPresenter;
import org.ednovo.gooru.client.mvp.analytics.util.AnalyticsUtil;
import org.ednovo.gooru.client.service.AnalyticsServiceAsync;
import org.ednovo.gooru.shared.model.analytics.CollectionSummaryMetaDataDo;
import org.ednovo.gooru.shared.model.analytics.CollectionSummaryUsersDataDo;
import org.ednovo.gooru.shared.model.analytics.PrintUserDataDO;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PresenterWidget;
public class CollectionSummaryPresenter extends PresenterWidget<IsCollectionSummaryView> implements CollectionSummaryUiHandlers{
	
	public static final  Object TEACHER_STUDENT_SLOT = new Object();
	
	private CollectionSummaryTeacherPresenter collectionSummaryTeacherPresenter;
	
	private CollectionSummaryIndividualPresenter collectionSummaryIndividualPresenter;

    CollectionSummaryMetaDataDo collectionMetadata;
    
	@Inject
	private  AnalyticsServiceAsync analyticService;
	
	String collectionId;
	
	/**
	 * Constructor
	 * @param eventBus
	 * @param view
	 * @param collectionSummaryTeacherPresenter
	 * @param collectionSummaryIndividualPresenter
	 */
	@Inject
	public CollectionSummaryPresenter(EventBus eventBus, IsCollectionSummaryView view,CollectionSummaryTeacherPresenter collectionSummaryTeacherPresenter,CollectionSummaryIndividualPresenter collectionSummaryIndividualPresenter) {
		super(eventBus, view);
		this.collectionSummaryTeacherPresenter=collectionSummaryTeacherPresenter;
		this.collectionSummaryIndividualPresenter=collectionSummaryIndividualPresenter;
		getView().setUiHandlers(this);
	}

	/**
	 * This method will return the analytics service
	 * @return
	 */
	public AnalyticsServiceAsync getAnalyticService() {
		return analyticService;
	}

	/**
	 * This method will set the analytics serivice
	 * @param analyticService
	 */
	public void setAnalyticService(AnalyticsServiceAsync analyticService) {
		this.analyticService = analyticService;
	}

	/* (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.analytics.collectionSummary.CollectionSummaryUiHandlers#setCollectionSummaryData(java.lang.String, java.lang.String)
	 */
	@Override
	public void setCollectionSummaryData(final String collectionId,final String pathwayId) {
		this.collectionId=collectionId;
		getView().getLoadinImage().setVisible(true);
		final String classpageId=AppClientFactory.getPlaceManager().getRequestParameter("classpageid", null);
		this.analyticService.getCollectionSummaryUsersData(classpageId,new AsyncCallback<ArrayList<CollectionSummaryUsersDataDo>>() {
			
			@Override
			public void onSuccess(ArrayList<CollectionSummaryUsersDataDo> result) {
				getView().setUsersData(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});
		this.analyticService.getAssignmentAverageData(classpageId, pathwayId, collectionId,new AsyncCallback<CollectionSummaryMetaDataDo>() {
			
			@Override
			public void onSuccess(CollectionSummaryMetaDataDo result) {
				getView().setCollectionMetaData(result,pathwayId,classpageId);
				collectionMetadata=result;
				setTeacherData(collectionId,classpageId,pathwayId);
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}

	/* (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.analytics.collectionSummary.CollectionSummaryUiHandlers#loadUserSessions(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.ednovo.gooru.shared.model.analytics.PrintUserDataDO)
	 */
	@Override
	public void loadUserSessions(final String collectionId,final String classId,final String userId,final String pathwayId,final PrintUserDataDO printUserDataDO) {
		this.analyticService.getSessionsDataByUser(collectionId, classId, userId, new AsyncCallback<ArrayList<CollectionSummaryUsersDataDo>>() {
			
			@Override
			public void onSuccess(ArrayList<CollectionSummaryUsersDataDo> result) {
				System.out.println("sessions Size::"+result.size());
				getView().setUserSessionsData(result);
				if(result.size()!=0){
					printUserDataDO.setSession("1st Session");
					printUserDataDO.setSessionStartTime(AnalyticsUtil.getCreatedTime(Long.toString(result.get(0).getTimeStamp())));
					setIndividualData(collectionId,classId,userId,result.get(0).getSessionId(), pathwayId,printUserDataDO);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}

	/* (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.analytics.collectionSummary.CollectionSummaryUiHandlers#setTeacherData(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void setTeacherData(String collectionId,String classpageId,String pathwayId) {
		getView().getLoadinImage().setVisible(true);
		clearSlot(TEACHER_STUDENT_SLOT);
		collectionSummaryTeacherPresenter.setTeacherData(collectionId,classpageId,pathwayId,collectionMetadata,getView().getLoadinImage());
		setInSlot(TEACHER_STUDENT_SLOT, collectionSummaryTeacherPresenter,false);		
	}

	/* (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.analytics.collectionSummary.CollectionSummaryUiHandlers#setIndividualData(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.ednovo.gooru.shared.model.analytics.PrintUserDataDO)
	 */
	@Override
	public void setIndividualData(String collectionId,String classpageId,String userId,String sessionId,String pathwayId,PrintUserDataDO printUserDataDO) {
		getView().getLoadinImage().setVisible(true);
		clearSlot(TEACHER_STUDENT_SLOT);
		collectionSummaryIndividualPresenter.setIndividualData(collectionId,classpageId,userId,sessionId,pathwayId,true,getView().getLoadinImage(),printUserDataDO);
		setInSlot(TEACHER_STUDENT_SLOT, collectionSummaryIndividualPresenter,false);	
	}
	@Override
	protected void onHide() {
		super.onHide();
		clearFrames();
	}

	/* (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.analytics.collectionSummary.CollectionSummaryUiHandlers#exportCollectionSummary(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void exportCollectionSummary(String collectionId,String classpageId, String userId, String sessionId,String pathwayId,String timeZone) {
		this.analyticService.exportTeacherSummary(collectionId, pathwayId, classpageId, timeZone, new AsyncCallback<String>() {
			
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
	@Override
	public void clearFrames(){
		collectionSummaryTeacherPresenter.clearFrame();
		collectionSummaryIndividualPresenter.clearFrame();
	}
}
