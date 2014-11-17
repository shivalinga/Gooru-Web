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
package org.ednovo.gooru.client.mvp.search.collection;

import java.util.HashMap;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.dnd.IsDraggable;
import org.ednovo.gooru.client.mvp.dnd.IsDraggableMirage;
import org.ednovo.gooru.client.mvp.resource.dnd.ResourceDragController;
import org.ednovo.gooru.client.mvp.resource.dnd.ResourceDragWithImgUc;
import org.ednovo.gooru.client.mvp.search.SearchUiUtil;
import org.ednovo.gooru.client.mvp.search.event.UpdateSearchResultMetaDataEvent;
import org.ednovo.gooru.client.mvp.search.event.UpdateSearchResultMetaDataHandler;
import org.ednovo.gooru.client.service.UserServiceAsync;
import org.ednovo.gooru.client.uc.CollaboratorsUc;
import org.ednovo.gooru.client.uc.CollectionImageUc;
import org.ednovo.gooru.client.uc.SeparatorUc;
import org.ednovo.gooru.client.uc.UserProfileUc;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.search.CollectionSearchResultDo;
import org.ednovo.gooru.shared.model.user.ProfileDo;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * @author Search Team
 *
 */
public class CollectionSearchResultVc extends Composite implements IsDraggable, IsSearchResultVc{

	private static CollectionSearchResultVcUiBinder uiBinder = GWT.create(CollectionSearchResultVcUiBinder.class);
	
	private static MessageProperties i18n = GWT.create(MessageProperties.class);

	interface CollectionSearchResultVcUiBinder extends UiBinder<Widget, CollectionSearchResultVc> {
	}
	
	private final FlowPanel profilePanel=new FlowPanel();

	@UiField
	Label  creatorNameLblValue, creatorNameLbl, resourceCountLbl;

	@UiField
	HTML collectionDescriptionHtml,collectionTitleLbl;
	
	@UiField Image imgOER;
	
	@UiField
	HTMLPanel containerPanel;

	@UiField
	FlowPanel collectionTitlePanel,metaDataPanelFloPanel, standardsFloPanel,teamFlowPanel;

	@UiField
	CollectionImageUc collectionImageUc;

	@UiField(provided = true)
	CollectionSearchResultWrapperVc wrapperVc;

	@UiField(provided = true)
	CollectionSearchResultCBundle res;
	
	@Inject
	private UserServiceAsync userService;
	
	

	private SimpleAsyncCallback<ProfileDo> userProfileAsyncCallback;
	
	private CollectionSearchResultDo collectionResultDo;
	
	private static final String VIEWS = " "+i18n.GL1099();
	private static final String VIEW = " "+i18n.GL1428();
	
	private static final String CREATED_BY = i18n.GL0622();
	
	private static final String RESOURCES = " "+i18n.GL0174();
	
	private static final String RESOURCE = " "+i18n.GL1110();

	private static final String QUESTIONS = " "+i18n.GL1042();
	
	private static final String QUESTION = " "+i18n.GL0308();

	private static final String USER_META_ACTIVE_FLAG = "0";
	/**
	 * Class constructor, creates new instance of CollectionSearchResultWrapperVc and call collection search result setData method
	 * @param collectionResultDo instance {@link CollectionSearchResultDo}
	 * @param searchDragController instance of {@link ResourceDragController}
	 */
	public CollectionSearchResultVc(CollectionSearchResultDo collectionResultDo, ResourceDragController searchDragController) {
		long startTime = System.currentTimeMillis();
		wrapperVc = new CollectionSearchResultWrapperVc(searchDragController,collectionResultDo.getGooruOid());
		this.res = CollectionSearchResultCBundle.INSTANCE;
		res.css().ensureInjected();
		initWidget(uiBinder.createAndBindUi(this));
		setData(collectionResultDo);
		wrapperVc.addStyleName("collectionSearchResultBox");
		resourceCountLbl.setVisible(false);
		
		collectionTitlePanel.getElement().setId("fpnlCollectionTitlePanel");
		collectionTitleLbl.getElement().setId("htmlCollectionTitleLbl");
		creatorNameLbl.getElement().setId("lblCreatorNameLbl");
		creatorNameLblValue.getElement().setId("lblCreatorNameLblValue");
		teamFlowPanel.getElement().setId("fpnlTeamFlowPanel");
		containerPanel.getElement().setId("pnlContainerPanel");
		metaDataPanelFloPanel.getElement().setId("pnlMetaDataPanelFloPanel");
		resourceCountLbl.getElement().setId("lblResourceCountLbl");
		standardsFloPanel.getElement().setId("fpnlStandardsFloPanel");
		collectionDescriptionHtml.getElement().setId("htmlCollectionDescriptionHtml");
		
		imgOER.setUrl("images/oer_icon.png");
		imgOER.getElement().setAttribute("id", i18n.GL1834());
		imgOER.getElement().setAttribute("alt", i18n.GL1834());
		imgOER.getElement().setAttribute("title", i18n.GL1834());
		
		collectionDescriptionHtml.getElement().getStyle().setTextAlign(TextAlign.JUSTIFY);
		
		
		AppClientFactory.getEventBus().addHandler(UpdateSearchResultMetaDataEvent.TYPE,setUpdateMetaData);
	}
	UpdateSearchResultMetaDataHandler setUpdateMetaData =new UpdateSearchResultMetaDataHandler(){

		@Override
		public void updateSearchResultMetaData(String count, String resourceId,
				String whatToUpdate) {
			if(count!=null){
			updateViews(count, resourceId, whatToUpdate);
			}
		}
	};

	
	/**
	 * 
	 * @function updateViews 
	 * 
	 * @created_date : Aug 11, 2013
	 * 
	 * @description
	 * 
	 * 
	 * @parm(s) : @param count
	 * @parm(s) : @param contentId
	 * @parm(s) : @param whatToUpdate
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	public void updateViews(String count, String contentId, String whatToUpdate){
		if (collectionResultDo.getGooruOid().equalsIgnoreCase(contentId)){
			metaDataPanelFloPanel.clear();
			SearchUiUtil.renderMetaData(metaDataPanelFloPanel, collectionResultDo.getCourseNames(), 30);
			SearchUiUtil.renderMetaData(metaDataPanelFloPanel, count + "", (Integer.parseInt(count) >1 ? VIEWS : VIEW));
			metaDataPanelFloPanel.add(new SeparatorUc());
			resourceCountLbl.setText(collectionResultDo.getOnlyResourceCount() +" " + (collectionResultDo.getOnlyResourceCount()>1 ? RESOURCES : RESOURCE));
			resourceCountLbl.getElement().setAttribute("alt",collectionResultDo.getOnlyResourceCount() +" " + (collectionResultDo.getOnlyResourceCount()>1 ? RESOURCES : RESOURCE));
			resourceCountLbl.getElement().setAttribute("title",collectionResultDo.getOnlyResourceCount() +" " + (collectionResultDo.getOnlyResourceCount()>1 ? RESOURCES : RESOURCE));
		}
	}
	
	

	/**
	 * Set collection meta info such as collection title, image, creator, etc..
	 * @param collectionResultDo instance of {@link CollectionSearchResultDo}
	 */
	public void setData(final CollectionSearchResultDo collectionResultDo) {
		this.collectionResultDo = collectionResultDo;
		wrapperVc.setData(collectionResultDo);
		//collectionTitleLbl.setText(StringUtil.truncateText(collectionResultDo.getResourceTitle(), 40));
				
		boolean oerVisibility = collectionResultDo.getLicense() !=null &&  collectionResultDo.getLicense().getCode() !=null ? collectionResultDo.getLicense().getCode().contains("CC") ? true : false : false;

		imgOER.setVisible(oerVisibility);
		
		if (oerVisibility){
			collectionTitleLbl.getElement().getStyle().setFloat(Float.LEFT);
		}else{
			collectionTitleLbl.getElement().getStyle().clearFloat();
		}
		
		collectionTitleLbl.setHTML(StringUtil.truncateText(collectionResultDo.getResourceTitle(), 40));
		collectionTitleLbl.getElement().setAttribute("alt",StringUtil.truncateText(collectionResultDo.getResourceTitle(), 40));
		collectionTitleLbl.getElement().setAttribute("title",StringUtil.truncateText(collectionResultDo.getResourceTitle(), 40));
		teamFlowPanel.clear();
		creatorNameLbl.setText(CREATED_BY);
		creatorNameLbl.getElement().setAttribute("alt",CREATED_BY);
		creatorNameLbl.getElement().setAttribute("title",CREATED_BY);
		if (collectionResultDo.getCollaboratorCount()!=null && collectionResultDo.getCollaboratorCount()>0){
			 CollaboratorsUc collaboratorsUc=new CollaboratorsUc(collectionResultDo);
			 teamFlowPanel.add(collaboratorsUc);
				creatorNameLblValue.setText(collectionResultDo.getOwner().getUsername() +" " + i18n.GL_GRR_AND() +" ");
				creatorNameLblValue.getElement().setAttribute("alt",collectionResultDo.getOwner().getUsername() +" " + i18n.GL_GRR_AND() +" ");
				creatorNameLblValue.getElement().setAttribute("title",collectionResultDo.getOwner().getUsername() +" " + i18n.GL_GRR_AND() +" ");
		}else{
			creatorNameLblValue.setText(collectionResultDo.getOwner().getUsername());
			creatorNameLblValue.getElement().setAttribute("alt",collectionResultDo.getOwner().getUsername());
			creatorNameLblValue.getElement().setAttribute("title",collectionResultDo.getOwner().getUsername());
		}
		if ((collectionResultDo.getOwner().isProfileUserVisibility())){
			if(StringUtil.isPartnerUser(collectionResultDo.getOwner().getUsername())) {
				creatorNameLblValue.getElement().getStyle().setColor("#1076bb");
				creatorNameLblValue.getElement().getStyle().setCursor(Cursor.POINTER);
//				creatorNameLblValue.getElement().getStyle().setFloat(Float.LEFT);
				
				creatorNameLblValue.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						MixpanelUtil.Click_Username();
						Map<String, String> params = new HashMap<String, String>();
						params.put("pid", collectionResultDo.getOwner().getGooruUId());
						AppClientFactory.getPlaceManager().revealPlace(collectionResultDo.getOwner().getUsername());
					}
				});
				
				creatorNameLblValue.addMouseOverHandler(new MouseOverHandler() {
					
					@Override
					public void onMouseOver(MouseOverEvent event) {
						
						AppClientFactory.getInjector().getUserService().getUserProfileV2Details(collectionResultDo.getOwner().getGooruUId(), USER_META_ACTIVE_FLAG, new SimpleAsyncCallback<ProfileDo>(){
	
							@Override
							public void onSuccess(ProfileDo result) {
								String username=result.getUser().getUsernameDisplay();
								String aboutMe=result.getAboutMe();
								UserProfileUc userProfileUc = new UserProfileUc(username,aboutMe, result.getUser().getProfileImageUrl());
								profilePanel.clear();
								profilePanel.add(userProfileUc);
								
							}
							
						});
						
						containerPanel.clear();
						containerPanel.add(profilePanel);
					}
				});
				
				creatorNameLblValue.addMouseOutHandler(new MouseOutHandler() {
					
					@Override
					public void onMouseOut(MouseOutEvent event) {
						//profilePanel.clear();
						containerPanel.clear();
					}
				});
			
			}else{
				creatorNameLblValue.getElement().getStyle().setColor("#1076bb");
				creatorNameLblValue.getElement().getStyle().setCursor(Cursor.POINTER);
				
				creatorNameLblValue.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						MixpanelUtil.Click_Username();
						Map<String, String> params = new HashMap<String, String>();
						params.put("id", collectionResultDo.getOwner().getGooruUId());
						params.put("user", collectionResultDo.getOwner().getUsername());
						AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.PROFILE_PAGE,params);
					}
				});
			}
			
			
			
		}/*else{
			creatorNameLblValue.setText(" "+collectionResultDo.getOwner().getUsername());
		}*/
		collectionImageUc.setUrl(collectionResultDo.getUrl(),collectionResultDo.getResourceTitle());
		collectionImageUc.getElement().getStyle().setZIndex(9999);
		collectionImageUc.setGooruOid(collectionResultDo.getGooruOid());
		String description=collectionResultDo.getDescription();
		if(description!=null && description.length()>235){
		description = description.trim().substring(0, 235) +"...";	
		}
		collectionDescriptionHtml.setHTML(description);
		
		SearchUiUtil.renderMetaData(metaDataPanelFloPanel, collectionResultDo.getCourseNames(), 30);
		SearchUiUtil.renderMetaData(metaDataPanelFloPanel, collectionResultDo.getTotalViews() + "", VIEWS);
		if(collectionResultDo.getOnlyResourceCount()>0 || (collectionResultDo.getOnlyResourceCount()==0&&collectionResultDo.getQuestionCount()==0)) {
			metaDataPanelFloPanel.add(new SeparatorUc());
			Label resourceCountLbl1 = new Label(collectionResultDo.getOnlyResourceCount() +" " + (collectionResultDo.getOnlyResourceCount()>1 ? RESOURCES : RESOURCE));
			resourceCountLbl1.setStyleName(res.css().resourceCount());
			metaDataPanelFloPanel.add(resourceCountLbl1);
		}
		if(collectionResultDo.getQuestionCount()>0) {
			metaDataPanelFloPanel.add(new SeparatorUc());
			Label questionCountLbl1 = new Label(collectionResultDo.getQuestionCount() +" " + (collectionResultDo.getQuestionCount()>1 ? QUESTIONS : QUESTION));
			questionCountLbl1.setStyleName(res.css().resourceCount());
			metaDataPanelFloPanel.add(questionCountLbl1);
		}
		
		//resourceCountLbl.setText(collectionResultDo.getOnlyResourceCount() +" " + (collectionResultDo.getOnlyResourceCount()>1 ? RESOURCES : RESOURCE));
		
		SearchUiUtil.renderStandards(standardsFloPanel, collectionResultDo);
	}

	
	@UiHandler("collectionTitleLbl")
	public void onClickCollectionTitle(ClickEvent event){
		MixpanelUtil.Preview_Collection_From_Search("CollectionTitleLbl");
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", collectionResultDo.getGooruOid());
		com.google.gwt.user.client.Window.scrollTo(0, 0);
		AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.COLLECTION_PLAY, params);
	}
	
	/**
	 * @return type of {@link CollectionSearchResultWrapperVc}
	 */
	public CollectionSearchResultWrapperVc getSearchResultVc() {
		return wrapperVc;
	}

	/**
	 * Assign {@link CollectionSearchResultWrapperVc} instance
	 * @param searchResultVc instance of {@link CollectionSearchResultWrapperVc}
	 */
	public void setSearchResultVc(CollectionSearchResultWrapperVc searchResultVc) {
		this.wrapperVc = searchResultVc;
	}

	@Override
	public String getDragId() {
		return collectionResultDo.getGooruOid();
	}

	@Override
	public DRAG_TYPE getDragType() {
		return DRAG_TYPE.COLLECTION;
	}

	@Override
	public IsDraggableMirage initDraggableMirage() {
		return new ResourceDragWithImgUc(DRAG_TYPE.COLLECTION.getName(), collectionResultDo.getResourceTitle());
	}

	@Override
	public void onDragBlur() {
		getSearchResultVc().onMouseOut(null);
	}

	@Override
	public Widget getDragHandle() {
		return getSearchResultVc().getDragHandlePanel();
	}

	@Override
	public int getDragTopCorrection() {
		return 27;
	}

	@Override
	public int getDragLeftCorrection() {
		return 20;
	}

	/**
	 * @return the collectionResultDo instance
	 */
	public CollectionSearchResultDo getCollectionResultDo() {
		return collectionResultDo;
	}
	/**
	 * @param collectionResultDo
	 *            the collectionResultDo to set
	 */
	public void setCollectionResultDo(CollectionSearchResultDo collectionResultDo) {
		this.collectionResultDo = collectionResultDo;
	}

	@Override
	public void setAddedToShelf(boolean addedToShelf) {
		wrapperVc.setAddedToShelf(addedToShelf);
	}
	
	public void setUserProfileAsyncCallback(SimpleAsyncCallback<ProfileDo> userProfileAsyncCallback) {
		this.userProfileAsyncCallback = userProfileAsyncCallback;
	}

	public UserServiceAsync getUserService() {
		return userService;
	}
	public void setUserService(UserServiceAsync userService) {
		this.userService = userService;
	}

	public SimpleAsyncCallback<ProfileDo> getUserprofileAsyncCallback() {
		return userProfileAsyncCallback;
	}
	public Anchor getAddButton(){
		return wrapperVc.addLbl;
	}
	
	public DisclosurePanel getDisclosurePanelClose(){
		return wrapperVc.disclosureDisPanel;
	}
	
	public SimplePanel getAddResourceContainerPanel(){
		return wrapperVc.disclosureContentSimPanel;
	}
}
