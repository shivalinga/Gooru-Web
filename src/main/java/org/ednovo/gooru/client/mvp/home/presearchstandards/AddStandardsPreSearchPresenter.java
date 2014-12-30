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
package org.ednovo.gooru.client.mvp.home.presearchstandards;

import java.util.ArrayList;
import java.util.List;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.addTagesPopup.AddTagesCBundle;
import org.ednovo.gooru.client.mvp.home.library.LibraryLessonUc;
import org.ednovo.gooru.client.mvp.shelf.event.RefreshCollectionInShelfListEvent;
import org.ednovo.gooru.client.mvp.shelf.event.RefreshCollectionInShelfListInPlayEvent;
import org.ednovo.gooru.client.mvp.shelf.event.RefreshCollectionInShelfListInPreviewPlayEvent;
import org.ednovo.gooru.client.mvp.shelf.event.RefreshCollectionInShelfListInResourcePlayEvent;
import org.ednovo.gooru.client.mvp.shelf.event.RefreshType;
import org.ednovo.gooru.client.service.ResourceServiceAsync;
import org.ednovo.gooru.client.service.ShelfServiceAsync;
import org.ednovo.gooru.client.service.TaxonomyServiceAsync;
import org.ednovo.gooru.shared.model.code.LibraryCodeDo;
import org.ednovo.gooru.shared.model.code.StandardsLevel1DO;
import org.ednovo.gooru.shared.model.code.StandardsLevel2DO;
import org.ednovo.gooru.shared.model.code.StandardsLevel3DO;
import org.ednovo.gooru.shared.model.code.StandardsLevel4DO;
import org.ednovo.gooru.shared.model.content.CollectionDo;
import org.ednovo.gooru.shared.model.library.LessonDo;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.Proxy;

/**
 * @author Search Team
 *
 */
public class AddStandardsPreSearchPresenter extends PresenterWidget<IsAddStandardsPreSearchView> implements AddStandardsPreSearchUiHandlers {

	@Inject
	private ShelfServiceAsync shelfService;

	@Inject
	private ResourceServiceAsync resourceService;

	@Inject
	private TaxonomyServiceAsync taxonomyService;

	private SimpleAsyncCallback<CollectionDo> saveCollectionAsyncCallback;

	private SimpleAsyncCallback<CollectionDo> collectionAsyncCallback;

	private SimpleAsyncCallback<List<LibraryCodeDo>> courseAsyncCallback;

	private String resourceOid;
	
	private String playerType=null;
	
	private boolean isCCSSAvailable =false;
	private boolean isNGSSAvailable =false;
	private boolean isTEKSAvailable =false;
	private boolean isCAAvailable =false;
	
	private String standardVal="";

	public String getResourceUid() {
		return resourceOid;
	}
	
	public String setPlayerType(String playerType){
		return playerType;
	}

	public void setResourceUid(String resourceUid) {
		this.resourceOid = resourceUid;
	}

	/**
	 * Class constructor
	 * @param view {@link View}
	 * @param proxy {@link Proxy}
	 */
	@Inject
	public AddStandardsPreSearchPresenter( EventBus eventBus,IsAddStandardsPreSearchView view) {
		super(eventBus,view);
		getView().setUiHandlers(this);
	}

	@Override
	public void onBind() {
		super.onBind();
	
	}
	
	public void loadDataFrompresnter()
	{
		getView().loadData();	
	}
	
	public void callDefaultStandardsLoad()
	{
		if(isCCSSAvailable){
			standardVal = "CCSS";
			getView().setStandardsStyles(standardVal);
		}else if(isTEKSAvailable){
			standardVal = "TEKS";
			getView().setStandardsStyles(standardVal);
		}else if(isNGSSAvailable){
			standardVal = "NGSS";
			getView().setStandardsStyles(standardVal);
		}else if(isCAAvailable){
			standardVal = "CA";
			getView().setStandardsStyles(standardVal);
		}
		getView().loadData();
		//getView().reset();
		AppClientFactory.getInjector().getSearchService().getFirstLevelStandards("0", standardVal, new SimpleAsyncCallback<ArrayList<StandardsLevel1DO>>() {
			@Override
			public void onSuccess(ArrayList<StandardsLevel1DO> result) {
				for(int i=0;i<result.size();i++) {
					getView().SetData(result.get(i),i);
				}
				getView().setEnableStandardButtons(isCCSSAvailable,isTEKSAvailable,isNGSSAvailable,isCAAvailable);
			}
		});
	}
	
	public void loadStateStandards(String stateCode)
	{
		getView().reset();
		AppClientFactory.getInjector().getSearchService().getFirstLevelStandards("0", stateCode, new SimpleAsyncCallback<ArrayList<StandardsLevel1DO>>() {
			@Override
			public void onSuccess(ArrayList<StandardsLevel1DO> result) {
				for(int i=0;i<result.size();i++) {
					getView().SetData(result.get(i),i);
				}
			}
		});
	}
	
	public void getFirstLevelObjects(String levelOrder, final String standardCode)
	{
		AppClientFactory.getInjector().getSearchService().getSecondLevelStandards(levelOrder, standardCode, new SimpleAsyncCallback<ArrayList<StandardsLevel2DO>>() {
			@Override
			public void onSuccess(ArrayList<StandardsLevel2DO> result) {
			
					getView().loadSecondLevelContianerObjects(result);
				
			}
		});
	}

	@Override
	protected void onReveal(){
		callDefaultStandardsLoad();
	}

	@Override
	public String setStandardsVal() {
		return getView().setStandardsVal();
	}
	
	@Override
	public Integer setStandardsIdVal() {
		return getView().setStandardsIdVal();
	}
	
	@Override
	public String setStandardDesc() {
		return getView().setStandardsDesc();
	}
	
	@Override
	public Button getAddBtn() {
		return getView().getAddBtn();
	}
	

	@Override
	public void getSecondLevelObjects(String levelOrder,
			String standardCodeSelected) {
		AppClientFactory.getInjector().getSearchService().getThirdLevelStandards(levelOrder, standardCodeSelected, new SimpleAsyncCallback<ArrayList<StandardsLevel3DO>>() {
			@Override
			public void onSuccess(ArrayList<StandardsLevel3DO> result) {
			
					getView().loadThirdLevelContianerObjects(result);
				
			}
		});
		
	}

	@Override
	public void getThirdLevelObjects(String levelOrder,
			String standardCodeSelected) {
		AppClientFactory.getInjector().getSearchService().getFourthLevelStandards(levelOrder, standardCodeSelected, new SimpleAsyncCallback<ArrayList<StandardsLevel4DO>>() {
			@Override
			public void onSuccess(ArrayList<StandardsLevel4DO> result) {
			
					getView().loadFourthLevelContianerObjects(result);
				
			}
		});
		
	}

	public void enableStandardsData(boolean isCCSSAvailable,
			boolean isTEKSAvailable, boolean isNGSSAvailable,
			boolean isCAAvailable) {
		// TODO Auto-generated method stub
		this.isCCSSAvailable= isCCSSAvailable;
		this.isNGSSAvailable= isNGSSAvailable;
		this.isTEKSAvailable= isTEKSAvailable;
		this.isCAAvailable= isCAAvailable;
	}

	public void setEnableStandardButtons(boolean isCCSSAvailable2,
			boolean isTEKSAvailable2, boolean isNGSSAvailable2,
			boolean isCAAvailable2) {
		getView().setEnableStandardButtons(isCCSSAvailable2, isNGSSAvailable2, isTEKSAvailable2, isCAAvailable2);
		
	}

}
