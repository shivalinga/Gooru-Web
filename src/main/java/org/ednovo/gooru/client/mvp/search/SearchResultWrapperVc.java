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
package org.ednovo.gooru.client.mvp.search;

import java.util.HashMap;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.search.resource.ResourceSearchPresenter;
import org.ednovo.gooru.client.uc.BrowserAgent;
import org.ednovo.gooru.client.uc.UcCBundle;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.search.ResourceSearchResultDo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Search Team
 * 
 * @param <T>
 * @param <C>
 */
public abstract class SearchResultWrapperVc<T extends ResourceSearchResultDo, C extends ResourceSearchResultDo> extends FocusPanel implements MouseOverHandler, MouseOutHandler {

	protected static SearchResultWrapperVcUiBinder uiBinder = GWT.create(SearchResultWrapperVcUiBinder.class);
	
	private static MessageProperties i18n = GWT.create(MessageProperties.class);

	interface SearchResultWrapperVcUiBinder extends UiBinder<Widget, SearchResultWrapperVc<?, ?>> {
	}

	@UiField
	SimplePanel contentSimPanel;

	@UiField public
	DisclosurePanel disclosureDisPanel;

	@UiField
	FocusPanel moreInfoLinkFocPanel,collectionFocPanel;

	@UiField
	FocusPanel shareLinkFocPanel;
	
	@UiField
	FocusPanel addLinkFocPanel;

	@UiField
	Label addedStatusLbl;

	@UiField
	public SimplePanel disclosureContentSimPanel;

	@UiField
	FlowPanel disclosureHeaderFloPanel;

	@UiField
	Anchor moreInfoLbl,collcResLbl;

	@UiField
	Anchor shareLbl;
	
	@UiField 
	public Anchor addLbl;
	
	@UiField(provided = true)
	SearchResultWrapperCBundle res;

	@UiField
	FocusPanel dragHandleFocPanel;
	
	@UiField
	Label resourcePlayerClickPanel;
	
	ResourceSearchPresenter resourceSearchPresenter;
	
	@UiField public FlowPanel ratingWidgetPanel;

	private static String DRAG_TO_ADD = i18n.GL0735();

	private static String ADDED = i18n.GL0736();

	private SearchShareVc searchShareVc;

	private CollectionInfo collectionInfo;
	
	private SearchInfoWidget searchInfoWidget;
	
	private AddResourceContainerView addResourceOrFolderContainerView ;
	
	private Boolean moreInfoMode = true;
	
	private Boolean colleResMode = true;
	
	private Boolean shareMode = true;
	
	private Boolean addMode = true;

	private static SearchResultWrapperVc<?, ?> openedResult;

	private String rootWebUrl = AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken();
	
	BrowserAgent browserAgent = new BrowserAgent();

	private T searchResultDo;
	
	/**
	 * Class constructor, creates new instance of SearchShareVc ,creates handled events for MouseOver and MouseOut 
	 */
	public SearchResultWrapperVc() {

		this.res = SearchResultWrapperCBundle.INSTANCE;
		res.css().ensureInjected();
		setWidget(uiBinder.createAndBindUi(this));
		setAddedStatus(true);
		disclosureContentSimPanel.getElement().getStyle().setWidth(100, Unit.PCT);
		disclosureContentSimPanel.getElement().getStyle().setBorderWidth(0, Unit.PX);
		disclosureContentSimPanel.getElement().getStyle().setPadding(0, Unit.PX);
		disclosureContentSimPanel.getElement().getStyle().setMargin(0, Unit.PX);
		moreInfoLbl.setText(i18n.GL1756());
		moreInfoLbl.getElement().setAttribute("alt",i18n.GL1756());
		moreInfoLbl.getElement().setAttribute("title",i18n.GL1756());
	
		shareLbl.setText(i18n.GL0526());
		shareLbl.getElement().setAttribute("alt",i18n.GL0526());
		shareLbl.getElement().setAttribute("title",i18n.GL0526());
		if (AppClientFactory.getCurrentPlaceToken().equals(
				PlaceTokens.RESOURCE_SEARCH) || AppClientFactory.getCurrentPlaceToken().equals(
						PlaceTokens.COLLECTION_PLAY)) {
		addLbl.setText(i18n.GL0590());
		}else{
			addLbl.setText(i18n.GL2037());
		}
		
		moreInfoLbl.getElement().setId("lblMoreInfo");
		collcResLbl.getElement().setId("lblResColle");
		shareLbl.getElement().setId("lblSahre");
		addLbl.getElement().setId("lblAdd");
		
		String browserType = browserAgent.returnFormFactorView();
		if(!(browserType.equalsIgnoreCase("desktop"))) {
			setResourcePlayerClickPanelMobile();
//			disclosureHeaderFloPanel.setVisible(true);
		} else {
			addMouseOutHandler(this);
			addMouseOverHandler(this);
//			disclosureHeaderFloPanel.setVisible(true);
		}
		searchShareVc = new SearchShareVc();
		collectionInfo = new CollectionInfo();
		searchInfoWidget = new SearchInfoWidget();
		
		
		addStyleName(UcCBundle.INSTANCE.css().userDefaultSelectDisable());
		if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.PROFILE_PAGE)){
			addStyleName(SearchResultWrapperCBundle.INSTANCE.css().collectionPPPPanel());
			disclosureHeaderFloPanel.addStyleName(SearchResultWrapperCBundle.INSTANCE.css().collectionPPPDisclosureHeader());
		}
		else{
			addStyleName(SearchResultWrapperCBundle.INSTANCE.css().searchPanel());
			addedStatusLbl.setText(DRAG_TO_ADD);
			addedStatusLbl.getElement().setAttribute("alt",DRAG_TO_ADD);
			addedStatusLbl.getElement().setAttribute("title",DRAG_TO_ADD);
		}
		dragHandleFocPanel.getElement().setId("focuspnlDragHandleFocPanel");
		contentSimPanel.getElement().setId("spnlContentSimPanel");
		ratingWidgetPanel.getElement().setId("fpnlRatingWidgetPanel");
		addedStatusLbl.getElement().setId("lblAddedStatusLbl");

		resourcePlayerClickPanel.getElement().setId("lblResourcePlayerClickPanel");
		disclosureHeaderFloPanel.getElement().setId("fpnlDisclosureHeaderFloPanel");
		disclosureDisPanel.getElement().setId("discpnlDisclosureDisPanel");
		disclosureDisPanel.getElement().getStyle().setWidth(100, Unit.PCT);
		disclosureContentSimPanel.getElement().setId("spnlDisclosureContentSimPanel");
	}

	void setResourcePlayerClickPanelMobile() {
		resourcePlayerClickPanel.getElement().getStyle().setPosition(Position.ABSOLUTE);
		if(rootWebUrl.contains("collection-search")){
			resourcePlayerClickPanel.getElement().getStyle().setWidth(141, Unit.PX);
		} else if (rootWebUrl.contains("resource-search")) {
			resourcePlayerClickPanel.getElement().getStyle().setWidth(100, Unit.PX);
		}
		resourcePlayerClickPanel.getElement().getStyle().setHeight(107, Unit.PX);
	}
	
	@Override
	public void onMouseOut(MouseOutEvent event) {
		if (!disclosureDisPanel.isOpen()) {
			disclosureHeaderFloPanel.setVisible(true);
			setAddedStatus(true);
		}
	}

	@Override
	public void onMouseOver(MouseOverEvent event) {
		disclosureHeaderFloPanel.setVisible(true);
		setAddedStatus(true);
	}

	public void discloseCollectionMoreInfoTab() {
		disclosureHeaderFloPanel.setVisible(true);
		setAddedStatus(true);
		openMoreInfoContainer();
	}
	
	/**
	 * View more info panel
	 * @param clickEvent instance of {@link ClickEvent}
	 */
	@UiHandler("moreInfoLinkFocPanel")
	public void onMoreInfoLinkClicked(ClickEvent clickEvent) {
		openMoreInfoContainer();
	}
	
	public void openMoreInfoContainer() {
		if (moreInfoMode && (colleResMode || shareMode || addMode)) {
			moreInfoMode = false;
			colleResMode = true;
			shareMode = true;
			addMode =true;
			collcResLbl.removeStyleName(res.css().infoLblActive());
			moreInfoLbl.addStyleName(res.css().moreInfoActive());
			shareLbl.removeStyleName(res.css().shareActive());
			addLbl.removeStyleName(res.css().addLblActive());
			
			disclosureContentSimPanel.clear();
			if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.RESOURCE_SEARCH)){
				disclosureContentSimPanel.setWidget(getSearchInfoWidget());
				getSearchInfoWidget().setData(this.searchResultDo);
				getSearchInfoWidget().setData();
			}else{
				disclosureContentSimPanel.setWidget(getCollectionInfo());
				getCollectionInfo().setInfoData(this.searchResultDo.getGooruOid());
			}
			onDisclosureOpen();
			MixpanelUtil.Click_moreInfo();
		} else {
			onDisclosureClose();
			moreInfoLbl.removeStyleName(res.css().moreInfoActive());
			shareLbl.removeStyleName(res.css().shareActive());
			collcResLbl.removeStyleName(res.css().infoLblActive());
			addLbl.removeStyleName(res.css().addLblActive());
		}
	}
	
	/**
	 * View share link widget
	 * @param clickEvent instance Of {@link ClickEvent}
	 */
	@UiHandler("shareLinkFocPanel")
	public void onShareShortenUrlLink(ClickEvent clickEvent) {

		if (shareMode && (colleResMode || moreInfoMode || addMode)) {
			moreInfoLbl.removeStyleName(res.css().moreInfoActive());
			collcResLbl.removeStyleName(res.css().infoLblActive());
			shareLbl.addStyleName(res.css().shareActive());
			addLbl.removeStyleName(res.css().addLblActive());
			disclosureContentSimPanel.clear();
			disclosureContentSimPanel.setWidget(getSearchShareVc());
			getSearchShareVc().onReveal();
			shareMode = false;
			colleResMode = true;
			moreInfoMode = true;
			addMode =true;
			onDisclosureOpen();
			MixpanelUtil.Click_Share();
		} else {
			onDisclosureClose();
			moreInfoLbl.removeStyleName(res.css().moreInfoActive());
			shareLbl.removeStyleName(res.css().shareActive());
			collcResLbl.removeStyleName(res.css().infoLblActive());
			addLbl.removeStyleName(res.css().addLblActive());
		}
	}
	
	/**
	 * View share link widget
	 * @param clickEvent instance Of {@link ClickEvent}
	 */
	@UiHandler("collectionFocPanel")
	public void onInfoTabClick(ClickEvent clickEvent) {

		if (colleResMode && (shareMode || moreInfoMode || addMode)) {
			colleResMode = false;
			moreInfoMode = true;
			shareMode = true;
			addMode = true;
			collcResLbl.addStyleName(res.css().infoLblActive());
			moreInfoLbl.removeStyleName(res.css().moreInfoActive());
			shareLbl.removeStyleName(res.css().shareActive());
			addLbl.removeStyleName(res.css().addLblActive());
			disclosureContentSimPanel.clear();
			disclosureContentSimPanel.setWidget(getSearchMoreInfoVc());
			getSearchMoreInfoVc().setData(this.searchResultDo);
			getSearchMoreInfoVc().reset(moreInfoMode);
			onDisclosureOpen();
			MixpanelUtil.Click_moreInfo();
		} else {
			onDisclosureClose();
			moreInfoLbl.removeStyleName(res.css().moreInfoActive());
			shareLbl.removeStyleName(res.css().shareActive());
			collcResLbl.removeStyleName(res.css().infoLblActive());
			addLbl.removeStyleName(res.css().addLblActive());
		}
	}

	@UiHandler("addLinkFocPanel")
	public void onaddTabClick(ClickEvent clickEvent) {
		if (addMode && (shareMode || moreInfoMode || colleResMode)) {
			addMode = false;
			moreInfoMode = true;
			shareMode = true;
			colleResMode = true;
			collcResLbl.removeStyleName(res.css().infoLblActive());
			moreInfoLbl.removeStyleName(res.css().moreInfoActive());
			shareLbl.removeStyleName(res.css().shareActive());
			addLbl.addStyleName(res.css().addLblActive());

			/*if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.RESOURCE_SEARCH)){
				disclosureContentSimPanel.setWidget();
			}else{
				disclosureContentSimPanel.setWidget();
			}*/
			
			onDisclosureOpen();
			MixpanelUtil.Click_addInfo();
		} else {
			onDisclosureClose();
			moreInfoLbl.removeStyleName(res.css().moreInfoActive());
			shareLbl.removeStyleName(res.css().shareActive());
			collcResLbl.removeStyleName(res.css().infoLblActive());
			addLbl.removeStyleName(res.css().addLblActive());
		}
	}
	
	/**
	 * Close disclosure panel widget
	 */
	public void onDisclosureClose() {
		openedResult = null;
		moreInfoMode = true;
		shareMode = true;
		colleResMode = true;
		addMode = true;
		disclosureDisPanel.setOpen(false);
	}

	/**
	 * open disclosure panel widget
	 */
	public void onDisclosureOpen() {
		if (openedResult != null && !openedResult.equals(this)) {
			openedResult.getDisclosurePanel().setOpen(false);
			openedResult.setAddedStatus(true);
			if (!getDisclosurePanel().isOpen()) {
				getDisclosurePanel().setOpen(true);
			}
		} else {
			getDisclosurePanel().setOpen(true);
		}
		openedResult = this;
	}

	/**
	 * Added dragged resource to shelf   
	 * @param addedToShelf if is true add dragged resource to shelf else not
	 */
	public void setAddedToShelf(boolean addedToShelf) {
		addedStatusLbl.setText(addedToShelf ? ADDED : DRAG_TO_ADD);
		addedStatusLbl.getElement().setAttribute("alt",addedToShelf ? ADDED : DRAG_TO_ADD);
		addedStatusLbl.getElement().setAttribute("title",addedToShelf ? ADDED : DRAG_TO_ADD);
		if (addedToShelf) {
			addedStatusLbl.addStyleName(SearchResultWrapperCBundle.INSTANCE.css().added());
			if (getDisclosurePanel().isOpen()) {
				final Timer t = new Timer() {
					public void run() {
						getSearchMoreInfoVc().reset(moreInfoMode);
					}
				};
				t.schedule(2000);
			}
		} else {
			addedStatusLbl.removeStyleName(SearchResultWrapperCBundle.INSTANCE.css().added());
		}
		setAddedStatus(addedToShelf);
	}

	/**
	 * Add dragged status 
	 * @param added whether status is added or not
	 */
	public void setAddedStatus(Boolean added) {
//		addedStatusLbl.setVisible(added != null ? added : !addedStatusLbl.getText().equals(DRAG_TO_ADD));
		addedStatusLbl.setVisible(false);
	}

	/**
	 * Set Widget instance to contentPanel
	 * @param widget instance of {@link Widget}
	 */
	@UiChild(tagname = "content")
	protected void setContent(Widget widget) {
		contentSimPanel.setWidget(widget);
	}

	/**
	 * Set moreInfo and share meat data
	 * @param searchResultDo Type of SearchResultDo
	 */
	public void setData(T searchResultDo) {
		this.searchResultDo = searchResultDo;
		//getSearchMoreInfoVc().setData(searchResultDo); 
		getSearchShareVc().setData(searchResultDo);
	
		
		if(rootWebUrl.contains("collection-search")){
			collcResLbl.setText(i18n.GL1755()+ " ("+searchResultDo.getResourceCount()+")");
			collcResLbl.getElement().setAttribute("alt",i18n.GL1755()+ " ("+searchResultDo.getResourceCount()+")");
			collcResLbl.getElement().setAttribute("title",i18n.GL1755()+ " ("+searchResultDo.getResourceCount()+")");
		} else if (rootWebUrl.contains("resource-search")) {
			collcResLbl.setText(i18n.GL1754() + " ("+searchResultDo.getScollectionCount()+")");
			collcResLbl.getElement().setAttribute("alt",i18n.GL1754() + " ("+searchResultDo.getScollectionCount()+")");
			collcResLbl.getElement().setAttribute("title",i18n.GL1754() + " ("+searchResultDo.getScollectionCount()+")");
		}
		
		final String gooruOid = searchResultDo.getGooruOid();
		resourcePlayerClickPanel.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Map<String, String> params = new HashMap<String, String>();
				params.put("id", gooruOid);
				if(rootWebUrl.contains("collection-search")) {
					Window.scrollTo(0, 0);
					AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.COLLECTION_PLAY, params);
				} else if(rootWebUrl.contains("resource-search")){
					params.put("pn", "resource");
					AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.RESOURCE_PLAY, params);
				}
			}
		});

	}


	/**
	 * @return instance of {@link DisclosurePanel}
	 */
	public DisclosurePanel getDisclosurePanel() {
		return disclosureDisPanel;
	}
	
	/**
	 * @return instance of {@link SearchShareVc}
	 */
	public SearchShareVc getSearchShareVc() {
		return searchShareVc;
	}
	
	/**
	 * @return the searchInfoWidget
	 */
	public SearchInfoWidget getSearchInfoWidget() {
		return searchInfoWidget;
	}
	
	/**
	 * @return the collectionInfo
	 */
	public CollectionInfo getCollectionInfo() {
		return collectionInfo;
	}

	/**
	 * @return dragHandlePanel instance of {@link FocusPanel}
	 */
	public FocusPanel getDragHandlePanel() {
		return dragHandleFocPanel;
	}

	protected abstract SearchMoreInfoVc<T, C> getSearchMoreInfoVc();

	public AddResourceContainerView getAddResourceOrFolderContainerView() {
		return addResourceOrFolderContainerView;
	}
	

}
