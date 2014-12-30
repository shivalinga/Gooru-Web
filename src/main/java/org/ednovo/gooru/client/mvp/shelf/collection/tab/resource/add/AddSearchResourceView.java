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
package org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.add;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.search.IsSearchView;
import org.ednovo.gooru.client.uc.HTMLEventPanel;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.code.CodeDo;
import org.ednovo.gooru.shared.model.code.LibraryCodeDo;
import org.ednovo.gooru.shared.model.content.CollectionDo;
import org.ednovo.gooru.shared.model.search.ResourceSearchResultDo;
import org.ednovo.gooru.shared.model.search.SearchDo;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public abstract class AddSearchResourceView extends Composite {
	
	private String subjectDoFilter = "";
	private String gradeDoFilter = "";
	private SearchDo<ResourceSearchResultDo> filterSearchDo = new SearchDo<ResourceSearchResultDo>();
	
	private static MessageProperties i18n = GWT.create(MessageProperties.class);
	
//	private static final AppConstants CONSTANTS = GWT.create(AppConstants.class);
	
	@UiField 
	Label totalResources,searchtitleText,serachcontentText,suggestedText;
	
	@UiField
	TextBox searchBox;
	
	@UiField
	Button addResourceButton;
	
	@UiField
	HTMLEventPanel suggestedResourcesPanel;
	
	@UiField HTMLPanel searchResultspanel,noResultsPanel;
	
	@UiField HTML noResultsLabel;
	 
	Map<String, String> filterMap = new HashMap<String, String>();
	
	HTMLPanel addResourceThumbnailContent;
	
    /*private static final String DEFULT_VIMAGE = "images/default-video.png";

	private static final String DEFULT_QIMAGE = "images/default-question.png";

	private static final String DEFULT_IIMAGE = "images/default-interactive.png";

	private static final String DEFULT_WIMAGE = "images/default-website.png";

	private static final String DEFULT_TIMAGE = "images/default-textbook.png";

	private static final String DEFULT_EIMAGE = "images/default-exam.png";

	private static final String DEFULT_SIMAGE = "images/default-slide.png";

	private static final String DEFULT_HIMAGE = "images/default-handout.png";

	private static final String DEFULT_LIMAGE = "images/default-lesson.png";
	*/
	private String collectionTitle = "";
	
	HTMLPanel addResourceSprite;
	
	HTML addResourceImgDesc;
	
	Button viewAllResourcesBtn = null;
	
	String collectionId = "";
	
	private static final String DEFULT_IMAGE_PREFIX = "images/default-";
	
	private static final String PNG =i18n.GL0899();
	
	private static final String SMALL = i18n.GL0900();
	private String category;
	PopupPanel appPopup;
	public interface AddSearchResourceViewUiBinder extends UiBinder<Widget, AddSearchResourceView> {
		
	}
	
	public static AddSearchResourceViewUiBinder uiBinder=GWT.create(AddSearchResourceViewUiBinder.class);
	
	public AddSearchResourceView(CollectionDo collectionDo,PopupPanel popuppanel){
		initWidget(uiBinder.createAndBindUi(this));
		this.appPopup=popuppanel;
		final String collectionTitle = collectionDo.getTitle();
		this.collectionTitle = collectionTitle;
		searchtitleText.setText(i18n.GL0894());
		searchtitleText.getElement().setId("lblSearchtitleText");
		searchtitleText.getElement().setAttribute("alt", i18n.GL0894());
		searchtitleText.getElement().setAttribute("title", i18n.GL0894());
		serachcontentText.setText(i18n.GL0895());
		serachcontentText.getElement().setId("lblSerachcontentText");
		serachcontentText.getElement().setAttribute("alt", i18n.GL0895());
		serachcontentText.getElement().setAttribute("title", i18n.GL0895());
		addResourceButton.setText(i18n.GL0896());
		addResourceButton.getElement().setAttribute("alt", i18n.GL0896());
		addResourceButton.getElement().setAttribute("title", i18n.GL0896());
		suggestedText.setText(i18n.GL0897());
		suggestedText.getElement().setId("lblSuggestedText");
		suggestedText.getElement().setAttribute("alt", i18n.GL0897());
		suggestedText.getElement().setAttribute("title", i18n.GL0897());
		searchResultspanel.getElement().setId("pnlSearchResultspanel");
		noResultsPanel.getElement().setId("pnlNoResultsPanel");
		totalResources.getElement().setId("lblTotalResources");
		noResultsLabel.getElement().setId("htmlNoResultsLabel");
		//GL0896
		searchBox.getElement().setAttribute("placeholder", i18n.GL1967());
		searchBox.setText(collectionTitle);
		searchBox.getElement().setId("txtSearchBox");
		StringUtil.setAttributes(searchBox, true);
		searchBox.getElement().setAttribute("alt", collectionTitle);
		searchBox.getElement().setAttribute("title", collectionTitle);
		searchBox.addKeyUpHandler(new SearchKeyUpHandler());
		suggestedResourcesPanel.getElement().setId("epnlSuggestedResourcesPanel");
		suggestedResourcesPanel.addMouseOverHandler(new showSearchButton());
		suggestedResourcesPanel.addMouseOutHandler(new hideSearchButton());
		viewAllResourcesBtn = new Button();
		viewAllResourcesBtn.getElement().setId("btnViewAllResults");
		viewAllResourcesBtn.setText("View Results");
		viewAllResourcesBtn.setStyleName("primary");
		addResourceButton.getElement().setId("btnGoTosearch");
		viewAllResourcesBtn.setVisible(false);
		collectionId = collectionDo.getGooruOid();
				
		filterSearchDo.setQuery(collectionTitle);

		addResourceButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MixpanelUtil.Perform_Search();
				if(searchBox.getText().equalsIgnoreCase(collectionTitle)) {
					redirectSearchFiltersPage();
				} else {
					redirectSearchNoFiltersPage();
				}
			}
		});
		
		viewAllResourcesBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MixpanelUtil.Click_On_ViewAllResults();
				redirectSearchFiltersPage();
			}
		});
		
		if(collectionDo.getGrade()!=null) {
			setFilters(collectionDo.getGrade());
			filterMap.put(IsSearchView.GRADE_FLT, gradeDoFilter);
		}
		
		Set<CodeDo> codeDoSet = collectionDo.getTaxonomySet();
		
		if(codeDoSet!=null & codeDoSet.size()>0) {
			setSubject(collectionDo.getTaxonomySet());
		} else {
			if(filterMap.size()>0) {
				filterSearchDo.setFilters(filterMap);
			}
			filterMap.put("fltNot.scollectionGooruOIds", collectionId);
			//getResourceSearchResults(filterSearchDo);
			getSuggestedResourceSearchResults(filterSearchDo,collectionId);
		}
	}
	
	private class SearchKeyUpHandler implements KeyUpHandler {
		@Override
		public void onKeyUp(KeyUpEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				if (searchBox.getText() != null && searchBox.getText().length() > 0) {
					if(searchBox.getText().equalsIgnoreCase(collectionTitle)) {
						redirectSearchFiltersPage();
					} else {
						redirectSearchNoFiltersPage();
					}
				}
			}
		}
	}

	public class showSearchButton implements MouseOverHandler {
		@Override
		public void onMouseOver(MouseOverEvent event) {
			viewAllResourcesBtn.setVisible(true);
		}
	}
	
	public class hideSearchButton implements MouseOutHandler {
		@Override
		public void onMouseOut(MouseOutEvent event) {
			viewAllResourcesBtn.setVisible(false);
		}
	}
	
/*	private void getResourceSearchResults(SearchDo<ResourceSearchResultDo> searchDo) {
		AppClientFactory.getInjector().getSearchService().getResourceSearchResults(searchDo, new SimpleAsyncCallback<SearchDo<ResourceSearchResultDo>>(){
			@Override
			public void onSuccess(SearchDo<ResourceSearchResultDo> result) {
				setData(result);
			}
		});
	}*/
	
	private void getSuggestedResourceSearchResults(SearchDo<ResourceSearchResultDo> searchDo,final String contentGorruOid) {
		AppClientFactory.getInjector().getSearchService().getCollectionSuggestedResourceSearchResults(searchDo,contentGorruOid, new SimpleAsyncCallback<SearchDo<ResourceSearchResultDo>>() {
			
			@Override
			public void onSuccess(SearchDo<ResourceSearchResultDo> result) {
				// TODO Auto-generated method stub
				setData(result,contentGorruOid);
			}			
		});
	}
	
	private void setFilters(String grade){
		boolean isGradeAvailable = false;
		if(grade.contains("Higher Education")) {
			gradeDoFilter = gradeDoFilter + "H";
			isGradeAvailable = true;
		}
		if(grade.contains("5")||grade.contains("6")||grade.contains("7")||
				grade.contains("8")) {	
			if(isGradeAvailable == true) {
				gradeDoFilter = gradeDoFilter + "," + "5-8";
			} else {
				gradeDoFilter = gradeDoFilter + "5-8";
				isGradeAvailable = true;
			}
		} 
		if(grade.contains("9")||grade.contains("10")||grade.contains("11")||
				grade.contains("12")) {
			if(isGradeAvailable == true) {
				gradeDoFilter = gradeDoFilter + "," + "9-12";
			} else {
				gradeDoFilter = gradeDoFilter + "9-12";
				isGradeAvailable = true;
			}
		}
		if(grade.contains("Kindergarten")||grade.contains("1")||grade.contains("2")||
				grade.contains("3")||grade.contains("4")) {
			if(isGradeAvailable == true) {
				gradeDoFilter = gradeDoFilter + "," + "K-4";
			} else {
				gradeDoFilter = gradeDoFilter + "K-4";
			}
		}
	}
	
	public void setSubject(Set<CodeDo> codeDoSet) {
		String codeDoFilter = "";
		for(CodeDo codeDo : codeDoSet) {
			if(codeDo.getDepth() == 2) {
				codeDoFilter = codeDoFilter + "," +codeDo.getLabel();
			}
		}

		if(!codeDoFilter.isEmpty()) {
			final String codeFilter = codeDoFilter;
			AppClientFactory.getInjector().getTaxonomyService().getCourse(new SimpleAsyncCallback<List<LibraryCodeDo>>() {
					@Override
					public void onSuccess(List<LibraryCodeDo> libraryCode) {
						if (libraryCode != null) {
							int codeIterator = 0;
							for (LibraryCodeDo libraryCodeDo : libraryCode) {
								boolean isSubjectAvailable = false;
								for (LibraryCodeDo liCodeDo : libraryCodeDo.getNode()) {
									if(codeFilter.contains(liCodeDo.getLabel())) {
										isSubjectAvailable = true;
										codeIterator++;
										break;
									}
									if(isSubjectAvailable == true) {
										if(codeIterator==1 || codeIterator==5) {
											subjectDoFilter = subjectDoFilter + libraryCodeDo.getLabel();
										} else if(codeIterator>1 && codeIterator<5) {
											subjectDoFilter = subjectDoFilter + "~~" + libraryCodeDo.getLabel();
										}
									}
								}
							}
						}
					}
				});
			if(filterMap.size()>0) {
				filterMap.put(IsSearchView.SUBJECT_FLT, subjectDoFilter);
				filterSearchDo.setFilters(filterMap);
			}
		}
		filterMap.put("fltNot.scollectionGooruOIds", collectionId);
		//getResourceSearchResults(filterSearchDo);
		getSuggestedResourceSearchResults(filterSearchDo,collectionId);
	}
	
	public abstract void hidePopup();

	private void setData(SearchDo<ResourceSearchResultDo> result,String collectiongooruOid) {
	List<ResourceSearchResultDo> suggestedSearchResults = result.getSuggestResults();
		/*if(result.getSearchHits()==0) {
			searchResultspanel.setVisible(false);
		} else {
			searchResultspanel.setVisible(true);
			totalResources.setText(StringUtil.generateMessage(i18n.GL0335, Integer.toString(result.getSearchHits())));
			final List<ResourceSearchResultDo> searchResults = result.getSearchResults();
			for(int i = 0; i < 4; i++) {
				final String thumbnailUrl = searchResults.get(i).getUrl();
				category = searchResults.get(i).getCategory();
				if(category!=null){
				if(category.equalsIgnoreCase("lesson")||category.equalsIgnoreCase("textbook")||category.equalsIgnoreCase("handout")){
					category=category.replaceAll("lesson", "text").replaceAll("textbook", "text").replaceAll("handout", "text");
				}
				if(category.equalsIgnoreCase("slide")){
					category=category.replaceAll("slide","image");
				}
				if(category.equalsIgnoreCase("exam")||category.equalsIgnoreCase("website") || category.equalsIgnoreCase("challenge")){
					category=category.replaceAll("exam","webpage").replaceAll("website","webpage").replaceAll("challenge","webpage");
				}
				}
				HTMLPanel thumbnailContainer = new HTMLPanel("");
				
				addResourceThumbnailContent = new HTMLPanel("");
				addResourceThumbnailContent.setStyleName(CollectionEditResourceCBundle.INSTANCE.css().addResourceThumbnailContent());			
				final Image resourceThumbnail = new Image();
				resourceThumbnail.setUrl(thumbnailUrl);
				resourceThumbnail.getElement().getStyle().setHeight(60, Unit.PX);
				resourceThumbnail.getElement().getStyle().setWidth(80, Unit.PX);
				
				addResourceSprite = new HTMLPanel("");
				addResourceSprite.addStyleName(UcCBundle.INSTANCE.css().resourceName());
				addResourceSprite.addStyleName(category.toLowerCase() + SMALL);
				addResourceImgDesc = new HTML();
				String resourceTitle = searchResults.get(i).getResourceTitle();
				if(resourceTitle.length() > 25) {
					resourceTitle = resourceTitle.substring(0, 24) + "...";
				}
				
				thumbnailContainer.add(resourceThumbnail);
				thumbnailContainer.add(addResourceSprite);
				thumbnailContainer.getElement().getStyle().setHeight(65, Unit.PX);
				
				addResourceImgDesc.setHTML(resourceTitle);
				addResourceImgDesc.setStyleName(CollectionEditResourceCBundle.INSTANCE.css().addResourceImgDesc());
				
				addResourceThumbnailContent.add(thumbnailContainer);
				addResourceThumbnailContent.add(addResourceImgDesc);
				
				AddSearchSuggestedResourceView addsearchobj = new AddSearchSuggestedResourceView(searchResults.get(i));
				suggestedResourcesPanel.add(addsearchobj);
			//	suggestedResourcesPanel.add(addResourceThumbnailContent);
				
				resourceThumbnail.addErrorHandler(new ErrorHandler(){
					@Override
					public void onError(ErrorEvent event) {
						resourceThumbnail.setUrl(DEFULT_IMAGE_PREFIX + category.toLowerCase() + PNG);
					}
				});
			}
			
			suggestedResourcesPanel.add(viewAllResourcesBtn);		
			viewAllResourcesBtn.getElement().getStyle().setMarginLeft(-236, Unit.PX);
			viewAllResourcesBtn.getElement().getStyle().setPosition(Position.ABSOLUTE);
			viewAllResourcesBtn.getElement().getStyle().setMarginTop(33, Unit.PX);
		}*/
		if(suggestedSearchResults.size()==0) {
			suggestedResourcesPanel.setVisible(false);
			noResultsLabel.setHTML(i18n.GL1957());
			noResultsLabel.getElement().setAttribute("alt", i18n.GL1957());
			noResultsLabel.getElement().setAttribute("title", i18n.GL1957());
			noResultsPanel.setVisible(true);
			noResultsLabel.setVisible(true);
		} else {
			suggestedResourcesPanel.setVisible(true);
				for(int i = 0; i < suggestedSearchResults.size(); i++) {
					if(suggestedSearchResults.size()<=2){
						noResultsLabel.setVisible(true);
						noResultsLabel.setHTML("");
						noResultsLabel.setHTML(i18n.GL1958());
						noResultsLabel.getElement().setAttribute("alt", i18n.GL1958());
						noResultsLabel.getElement().setAttribute("title", i18n.GL1958());
						}
					if(i>3){
						noResultsLabel.setVisible(false);
						break;
					}
				AddSearchSuggestedResourceView addsearchobj = new AddSearchSuggestedResourceView(suggestedSearchResults.get(i),collectiongooruOid) {
					@Override
					public void closePopup() {
						appPopup.hide();
					}
				};
				suggestedResourcesPanel.add(addsearchobj);
				}
			
		}
	}
	
	private void redirectSearchFiltersPage(){
		Map<String,String> filterResourceParams = new HashMap<String,String>();
		filterResourceParams.put("category", "All");
		filterResourceParams.put("query", searchBox.getText());
		if(!gradeDoFilter.isEmpty()) {
			filterResourceParams.put(IsSearchView.GRADE_FLT, gradeDoFilter);
		}
		if(!subjectDoFilter.isEmpty()) {
			filterResourceParams.put(IsSearchView.SUBJECT_FLT, subjectDoFilter);
		}
		filterResourceParams.put("pageSize", "8");
		filterResourceParams.put("pageNum", "1");
		filterResourceParams.put(IsSearchView.RATINGS_FLT, "5,4,3,2,1,0");
		hidePopup();
		AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.RESOURCE_SEARCH, filterResourceParams);
	}

	private void redirectSearchNoFiltersPage() {
		Map<String,String> noFilterResourceParams = new HashMap<String,String>();
		noFilterResourceParams.put("query",searchBox.getText());
		noFilterResourceParams.put("category", "All");
		noFilterResourceParams.put("pageSize", "8");
		noFilterResourceParams.put("pageNum", "1");
		noFilterResourceParams.put(IsSearchView.RATINGS_FLT, "5,4,3,2,1,0");
		hidePopup();
		AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.RESOURCE_SEARCH, noFilterResourceParams);
	}
}
