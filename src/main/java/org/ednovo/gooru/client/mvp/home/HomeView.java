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
package org.ednovo.gooru.client.mvp.home;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SearchAsyncCallback;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BaseViewWithHandlers;
import org.ednovo.gooru.client.mvp.faq.CopyRightPolicyVc;
import org.ednovo.gooru.client.mvp.faq.TermsAndPolicyVc;
import org.ednovo.gooru.client.mvp.faq.TermsOfUse;
import org.ednovo.gooru.client.mvp.home.event.HeaderTabType;
import org.ednovo.gooru.client.mvp.home.event.HomeEvent;
import org.ednovo.gooru.client.mvp.home.library.LibraryView;
import org.ednovo.gooru.client.mvp.home.presearchstandards.AddStandardsPreSearchPresenter;
import org.ednovo.gooru.client.mvp.home.register.RegisterVc;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderZIndexEvent;
import org.ednovo.gooru.client.uc.AppMultiWordSuggestOracle;
import org.ednovo.gooru.client.uc.AppSuggestBox;
import org.ednovo.gooru.client.ui.PeListPanel;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.content.ClasspageListDo;
import org.ednovo.gooru.shared.model.library.JSONStandardsDo;
import org.ednovo.gooru.shared.model.library.LibraryUserDo;
import org.ednovo.gooru.shared.model.library.SubjectDo;
import org.ednovo.gooru.shared.model.search.AutoSuggestKeywordSearchDo;
import org.ednovo.gooru.shared.model.search.SearchDo;
import org.ednovo.gooru.shared.model.user.UserDo;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ScrollEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


/**
 * @author Search Team
` * 
 */
public class HomeView extends BaseViewWithHandlers<HomeUiHandlers> implements IsHomeView, SelectionHandler<SuggestOracle.Suggestion> {

	@UiField HTMLPanel gooruPanel, panelLandingPage, contributorsContainer, panelStandardLibraries, panelDistrictLibraries, panelPartnerLibraries, panelText, panelGooruStories;
	@UiField Button btnSignUp, btnMoreOnCollections,viewSampleResportsBtn;
	@UiField Label lblHeading, lblSubHeading, lblCopyRight; 
//	@UiField TextBoxWithPlaceholder txtSearch;
	@UiField Button btnSearch;
	@UiField Anchor achLearn, achTerms, achPrivacy,achCopyright, achGooruStories;//achDataPolicy
	@UiField TextBox txtEmbedLink;
	@UiField HTML htmlDescription;
	
	LibraryView libraryView = null;
	private TermsOfUse termsOfUse;
	
	private SearchDo<AutoSuggestKeywordSearchDo> autoSuggestKeywordDo = new SearchDo<AutoSuggestKeywordSearchDo>();
	private SearchAsyncCallback<SearchDo<AutoSuggestKeywordSearchDo>> autoKeyWordSuggestionAsyncCallback;

	private AppMultiWordSuggestOracle autokeySuggestOracle;
	String searchData = "";
	private String GOORU_SEARCH = " -<n> Gooru Search</n>";
	
	@UiField(provided = true)
	public AppSuggestBox txtSearch;
	
	String jsonDataString = null;

	Map<String, String> allSubject = new HashMap<String, String>();
	Map<String, String> allCourse  = new HashMap<String, String>();
	
	private static HomeViewUiBinder uiBinder = GWT.create(HomeViewUiBinder.class);

	interface HomeViewUiBinder extends UiBinder<Widget, HomeView> {
	}
	
	private MessageProperties i18n = GWT.create(MessageProperties.class);

	/**
	 * Class constructor
	 */
	public HomeView() {		
		
		autokeySuggestOracle = new AppMultiWordSuggestOracle(true);
		setEditSearchTxtBox(new AppSuggestBox(autokeySuggestOracle) {

			@Override
			public HandlerRegistration addClickHandler(ClickHandler handler) {
				return null;
			}

			@Override
			public void keyAction(String text) {
				MixpanelUtil.Search_autocomplete_select();
				autokeySuggestOracle.clear();
				
				
				
				autoSuggestKeywordDo.setQuery(text);
				searchData = getEditSearchTxtBox().getText();
				autoSuggestKeywordDo.setType("resource");
				if (text != null && text.trim().length() > 0) {
					requestAutoSuggestKeyword(autoSuggestKeywordDo);
				} else {
					getEditSearchTxtBox().hideSuggestionList();
				}

			}

		});
		getEditSearchTxtBox().addSelectionHandler(this);
		getEditSearchTxtBox().setPopupStyleName("shelfEditSearchTextBox");
		
		Window.addWindowScrollHandler(new Window.ScrollHandler() {
		    @Override
		    public void onWindowScroll(ScrollEvent event) {
		    	getEditSearchTxtBox().hideSuggestionList();   	
			}
		});
		
		setWidget(uiBinder.createAndBindUi(this));
		gooruPanel.getElement().setId("gooruPanel");
		getEditSearchTxtBox().addKeyDownHandler(new SearchKeyDownHandler());
		panelLandingPage.setVisible(true);
		gooruPanel.setVisible(false);
		setIds();
		
		
		generateSubjectsData();
		generateCourseData();
		
		generateStandardLibraries();
		generateDistrictLibraries();
		generatePartnerLibraries();
		String emailId= AppClientFactory.getPlaceManager()
				.getRequestParameter("emailId");
	//	StringUtil.consoleLog("emailId..in home."+emailId);
		if(emailId!=null)
		{
			
			AppClientFactory.getInjector().getUserService().getRefershToken(AppClientFactory.getLoggedInUser().getGooruUId(),new AsyncCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
					//StringUtil.consoleLog("Header UC RefershToken..."+result);
						UserDo user = AppClientFactory.getLoggedInUser();
						user.setRefreshToken(result);
						AppClientFactory.setLoggedInUser(user);
									
				}
				
				@Override
				public void onFailure(Throwable caught) {
				//	StringUtil.consoleLog("Header UC onFailure...");				
				}
			});
		}
	

		
		
		panelGooruStories.setVisible(false);
		
		AppClientFactory.getInjector().getSearchService().showGooruStoriesSection( new SimpleAsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if (result.equalsIgnoreCase("true")){
					
					PeListPanel p = new PeListPanel();
					p.setTitle(i18n.GL2188_3());
					p.getElement().setInnerHTML(i18n.GL2188_3());
					panelText.add(p);
					
					AppClientFactory.getInjector().getSearchService().getGooruStoriesUrl("", new SimpleAsyncCallback<String>() {
						
						@Override
						public void onSuccess(String result) {
							achGooruStories.setHref(result);
							achGooruStories.setTarget("_blank");
						}
					});
					panelGooruStories.setVisible(true);
				}
			}
		});
		
		/*ClickHandler rootClick = new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				System.out.println("-->> "+preFilter);
				if(!isArrowIcon && preFilter!=null){
					isOpenPrefilterPopup=true;
					preFilter.hide();
				}else{
					isArrowIcon=false;
				}
			}
			
		};*/
		
//		RootPanel.get().addDomHandler(rootClick, ClickEvent.getType());
		Date todaysYear = new Date();
		String copyRightTxt = i18n.GL1246() + "" + (todaysYear.getYear() + 1900);
		
		lblCopyRight.setText(copyRightTxt);
		lblCopyRight.getElement().setId("lblCopyRightYearText");
		lblCopyRight.getElement().setAttribute("alt",copyRightTxt);
		lblCopyRight.getElement().setAttribute("title",copyRightTxt);
		
//		InternalServerErrorPopupViewVc error = new InternalServerErrorPopupViewVc() {
//		};
//		error.show();
		
	}
	
	
	
	/**
	 * @function generatePartnerLibraries 
	 * 
	 * @created_date : Aug 12, 2014
	 * 
	 * @description
	 * 
	 * 
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 * 
	*/
	
	private void generatePartnerLibraries() {
		AppClientFactory.getInjector().getLibraryService().getPartners(new SimpleAsyncCallback<ArrayList<LibraryUserDo>>() {
			@Override
			public void onSuccess(ArrayList<LibraryUserDo> partnersList) {
				if (partnersList != null){
					for(int i=0;i<partnersList.size();i++) {
						final LibraryUserDo libraryUserDo = partnersList.get(i);
						PeListPanel pTag = new PeListPanel();
						Anchor anchor = new Anchor();
						anchor.setText(libraryUserDo.getDisplayName());
						String url = "#"+libraryUserDo.getUsername();
						anchor.setHref(url);
						pTag.add(anchor);
						panelPartnerLibraries.add(pTag);
					}
				}
			}
		});
	}
	/**
	 * 
	 * @function generateDistrictLibraries 
	 * 
	 * @created_date : 11-Nov-2014
	 * 
	 * @description
	 * 
	 * 
	 * @parm(s) : 
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	private void generateDistrictLibraries() {
		
		
		try {
			new RequestBuilder(RequestBuilder.GET, "./images/json/district-libraries.json").sendRequest("", new RequestCallback() {
				  @Override
				  public void onResponseReceived(Request req, Response resp) {
					  List<JSONStandardsDo> stdList = getList(resp.getText());
					    for (int k=0; k<stdList.size(); k++){
					    
						    PeListPanel pTag = new PeListPanel();
							Anchor anchor = new Anchor();
							anchor.setText(stdList.get(k).getLabel());
							String url = StringUtil.generateMessage(stdList.get(k).getLink(), stdList.get(k).getCourseId()+"", stdList.get(k).getSubjectId()+"");
							if (stdList.get(k).getExtraParms()!=null){
								url = url + "&" + stdList.get(k).getExtraParms(); 
							}
							anchor.setHref(url);
							pTag.add(anchor);
							panelDistrictLibraries.add(pTag);
					    }
				  }

				  @Override
				  public void onError(Request res, Throwable throwable) {
				  }
				});
		} catch (RequestException e) {
			jsonDataString = null;
		}
		
			
	}
	
	/**
	 * @function generateStandardLibraries 
	 * 
	 * @created_date : Aug 5, 2014
	 * 
	 * @description
	 * 
	 * 
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 * 
	*/
	private void generateStandardLibraries() {
		
		
		try {
			new RequestBuilder(RequestBuilder.GET, "./images/json/standards.json").sendRequest("", new RequestCallback() {
				  @Override
				  public void onResponseReceived(Request req, Response resp) {
					  List<JSONStandardsDo> stdList = getList(resp.getText());
					    for (int k=0; k<stdList.size(); k++){
					    
						    PeListPanel pTag = new PeListPanel();
							Anchor anchor = new Anchor();
							anchor.setText(stdList.get(k).getLabel());
							String url = StringUtil.generateMessage(stdList.get(k).getLink(), stdList.get(k).getCourseId()+"", stdList.get(k).getSubjectId()+"");
							if (stdList.get(k).getExtraParms()!=null){
								url = url + "&" + stdList.get(k).getExtraParms(); 
							}
							anchor.setHref(url);
							pTag.add(anchor);
							panelStandardLibraries.add(pTag);
					    }
				  }

				  @Override
				  public void onError(Request res, Throwable throwable) {
				  }
				});
		} catch (RequestException e) {
			jsonDataString = null;
		}
		
			
	}
	/**
	 * @function getJSONDataFromFile 
	 * 
	 * @created_date : Aug 5, 2014
	 * 
	 * @description
	 * 
	 * 
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 * 
	*/
	
	private String getJSONDataFromFile() {
		
		try {
			new RequestBuilder(RequestBuilder.GET, "./images/json/standards.json").sendRequest("", new RequestCallback() {
				  @Override
				  public void onResponseReceived(Request req, Response resp) {
					  jsonDataString = resp.getText();
				  }

				  @Override
				  public void onError(Request res, Throwable throwable) {
				  }
				});
		} catch (RequestException e) {
			jsonDataString = null;
		}
		return jsonDataString;
	}


	/**
	 * 
	 * @function getList 
	 * 
	 * @created_date : Aug 5, 2014
	 * 
	 * @description
	 * 
	 * 
	 * @param jsonResponse
	 * @return
	 * 
	 * @return : List<JSONStandardsDo>
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	@SuppressWarnings("deprecation")
	public List<JSONStandardsDo> getList(String jsonResponse){
		List<JSONStandardsDo> stdList = new ArrayList<JSONStandardsDo>();
		JSONValue jsonValue = JSONParser.parse(jsonResponse);
        JSONArray jsonArray = jsonValue.isArray();
        for (int i=0; i<jsonArray.size(); i++){
        	JSONObject obj = jsonArray.get(i).isObject(); 
        	if (obj != null){
        		JSONStandardsDo stdObj = new JSONStandardsDo();
        		JSONValue label = obj.get("label").isString();
        		JSONNumber courseId = obj.get("courseId").isNumber();
        		JSONNumber subjectId = obj.get("subjectId").isNumber();
        		JSONValue link = obj.get("link").isString();
        		JSONValue extraParms = obj.get("extraParms").isString();        		
        		
        		stdObj.setLabel(label.isString().stringValue());
        		stdObj.setCourseId((int) courseId.isNumber().getValue());
        		stdObj.setSubjectId((int) subjectId.isNumber().getValue());
        		stdObj.setLink(link.isString().stringValue());
        		stdObj.setExtraParms(extraParms!= null ? extraParms.isString().stringValue() : null);
        		
        		stdList.add(stdObj);
        	}
        }
        
        return stdList;
	}
	
	/**
	 * @function generateCourseData 
	 * 
	 * @created_date : Jul 29, 2014
	 * 
	 * @description
	 * 
	 * 
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 * 
	*/
	private class SearchKeyDownHandler implements KeyDownHandler{

		@Override
		public void onKeyDown(KeyDownEvent event) {
			AppClientFactory.fireEvent(new SetHeaderZIndexEvent(0, true));
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				if (getEditSearchTxtBox().getText() != null
						&& getEditSearchTxtBox().getText().length() > 0) {
					savePlaceRequest();
					MixpanelUtil.Perform_Search(getEditSearchTxtBox().getText().trim()
							.toLowerCase(), "LandingPage");
					Map<String, String> params = new HashMap<String, String>();
					params = updateParams(params);
					if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.COLLECTION_SEARCH)){
						AppClientFactory.getPlaceManager().revealPlace(
								PlaceTokens.COLLECTION_SEARCH, params);
					}else{
						String queryVal = params.get("query");
						//queryVal = queryVal.replaceAll("%5C1", "&");
						Map<String, String> map = params;
						map.put("query", queryVal);	
						AppClientFactory.getPlaceManager().revealPlace(
								PlaceTokens.RESOURCE_SEARCH, map);
					}
					AppClientFactory.fireEvent(new HomeEvent(HeaderTabType.NONE));
					getEditSearchTxtBox().hideSuggestionList();
					
					
			
				}
			}
		}
	}
	private void generateCourseData() {
		
	}
	/**
	 * @function generateSubjectsData 
	 * 
	 * @created_date : Jul 29, 2014
	 * 
	 * @description
	 * 
	 * 
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 * 
	*/
	
	private void generateSubjectsData() {	
		List<SubjectDo> subList = new ArrayList<SubjectDo>();
		
		for (int i=0; i<4; i++){
//			Subject 
		}
		
		
		allSubject.put("Science", "20001");
		allSubject.put("Language Arts", "30186");
		allSubject.put("Math", "20002");
		allSubject.put("Social Sciences", "25681");
	}
	
	
	
	/**
	 * 
	 */
	@Override
	public void setInSlot(Object slot, Widget content) {
		if (slot == getUiHandlers().getContributorsSlot()){
			contributorsContainer.add(content);
		}
	}
	

	/**
	 * @function setIds 
	 * 
	 * @created_date : Jul 28, 2014
	 * 
	 * @description
	 * 
	 * 
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 * 
	*/
	
	private void setIds() {
		StringUtil.setAttributes(btnSignUp.getElement(), "btnHomeSignUp", i18n.GL0186(), i18n.GL0186());
		StringUtil.setAttributes(btnSearch.getElement(), "btnSearch", i18n.GL0176(), i18n.GL0176());
				
		StringUtil.setAttributes(lblHeading.getElement(), "lblHeading", i18n.GL2046(), i18n.GL2046());
		StringUtil.setAttributes(lblSubHeading.getElement(), "lblSubHeading", i18n.GL2047(), i18n.GL2047());
		String currentUrl = Window.Location.getHref();
		String protocol = currentUrl.startsWith("https") ? "https" : "http";
				
		String url = "<a href=\""+protocol+"://www.goorulearning.org/\"><img src=\"http://partners.goorulearning.org/_images/badges/01_goorubutton-honor.png\" width=\"250\" height=\"250\"/></a>";
		
		txtEmbedLink.setText(url);
		StringUtil.setAttributes(txtEmbedLink.getElement(), "txtEmbedLink", "", "");
		txtEmbedLink.setReadOnly(true);
		txtEmbedLink.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				txtEmbedLink.selectAll();
			}
		});
		getEditSearchTxtBox().getElement().setAttribute("placeholder", i18n.GL2073());
		getEditSearchTxtBox().getElement().setId("txtEditSearch");		
		
		htmlDescription.setHTML(i18n.GL2102());
		StringUtil.setAttributes(htmlDescription.getElement(), "htmlDescription", i18n.GL2102_1(), i18n.GL2102_1());
		
		if (AppClientFactory.isAnonymous()){
			btnSignUp.setVisible(true);
		}else{
			btnSignUp.setVisible(false);
		}
		
		
		Window.enableScrolling(true);
	}

	/* (non-Javadoc)
	 * @see org.ednovo.gooru.client.gin.BaseViewWithHandlers#onLoad()
	 */
	@Override
	public void onLoad() {
				
	}
	
	@Override
	public void resetPassword(String resetToken) {
		new ResetPasswordVc(resetToken);
		
	}
	
	@Override
	public void registerPopup() {
		RegisterVc registerVc = new RegisterVc();
		registerVc.show();
		registerVc.center();
	}

	/* (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.home.IsHomeView#loadFeaturedContributors()
	 */
	@Override
	public void loadFeaturedContributors(String callBack, String placeToken) {
//		libraryView.loadContributorsPage(callBack,placeToken);
	}
	
	@UiHandler("btnSignUp")
	public void onClickSignUp(ClickEvent event){
		Map<String, String> map = StringUtil.splitQuery(Window.Location
				.getHref());
		map.put("callback", "signup");
		map.put("type", "1");
		AppClientFactory.getPlaceManager().revealPlace(
				AppClientFactory.getCurrentPlaceToken(), map);
	}
	
	@UiHandler("btnSearch")
	public void onClickSearch(ClickEvent event){

//		Window.enableScrolling(true);
		AppClientFactory.fireEvent(new SetHeaderZIndexEvent(0, true));
		if (getEditSearchTxtBox().getText() != null
				&& getEditSearchTxtBox().getText().length() > 0) {
			savePlaceRequest();
			MixpanelUtil.Perform_Search(getEditSearchTxtBox().getText().trim()
					.toLowerCase(), "LandingPage");
			Map<String, String> params = new HashMap<String, String>();
			params = updateParams(params);
			if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.COLLECTION_SEARCH)){
				AppClientFactory.getPlaceManager().revealPlace(
						PlaceTokens.COLLECTION_SEARCH, params);
			}else{
				String queryVal = params.get("query");
				//queryVal = queryVal.replaceAll("%5C1", "&");
				Map<String, String> map = params;
				map.put("query", queryVal);	
				AppClientFactory.getPlaceManager().revealPlace(
						PlaceTokens.RESOURCE_SEARCH, map);
			}
			AppClientFactory.fireEvent(new HomeEvent(HeaderTabType.NONE));
			getEditSearchTxtBox().hideSuggestionList();
		}	
	}
	
	public void savePlaceRequest(){
		String currentPlaceToken=AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken();
		if(currentPlaceToken.equals(PlaceTokens.COLLECTION_SEARCH)||currentPlaceToken.equals(PlaceTokens.RESOURCE_SEARCH)){
		}else{
			AppClientFactory.getPlaceManager().setSearchMovedPlaceRequest(AppClientFactory.getPlaceManager().getCurrentPlaceRequest());
		}
	}

	/**
	 * Set pagination for search
	 * 
	 * @param params
	 *            variable for Map<String,String>
	 * @return pagination values
	 */
	public Map<String, String> updateParams(Map<String, String> params) {
		params.put("category", "All");
		params.put("query", getEditSearchText());
		params.put("pageNum", "1");
		params.put("pageSize", "8");
		return params;
	}
	/**
	 * @return search keyword
	 */
	public String getEditSearchText() {
		String searchText = getEditSearchTxtBox().getText();
		if (searchText != null && searchText.length() > 0) {
			return searchText;
		} else {
			return null;
		}
	}
	
	@UiHandler("btnMoreOnCollections")
	public void onClickMoreOnCollections(ClickEvent event){
		AppClientFactory.setPreviousPlaceRequest(AppClientFactory
				.getPlaceManager().getCurrentPlaceRequest());
		Storage stockStore = Storage.getLocalStorageIfSupported();

		if (stockStore != null) {
			stockStore.setItem("tabKey", "resourceTab");
		}
		AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.SHELF);
	}
	
	@UiHandler("btnMoreOnClasses")
	public void onClickMoreOnClasses(ClickEvent event){
		if (!AppClientFactory.isAnonymous()){
			
			AppClientFactory.getInjector().getClasspageService().v2GetAllClass("10", "0",new SimpleAsyncCallback<ClasspageListDo>() {
				@Override
				public void onSuccess(ClasspageListDo result) {
					if(result.getSearchResults()!=null){
						if (result.getSearchResults().size()>0){
						AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.CLASSHOME);
					}else{
						AppClientFactory.getPlaceManager().redirectPlace(PlaceTokens.STUDY);
					}
				}else
				{
					AppClientFactory.getPlaceManager().redirectPlace(PlaceTokens.STUDY);
				}
				}
		});
			
			//AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.CLASSHOME);
		} else {
			AppClientFactory.getPlaceManager().redirectPlace(PlaceTokens.STUDY);
		}
	}
	@UiHandler("achLearn")
	public void onClickLearn(ClickEvent event){
		int scrollTop =0;
		try{
			scrollTop = Document.get().getElementById("getStarted").getAbsoluteTop();
		}catch(Exception e){
			
		}
		
		Window.scrollTo(0, scrollTop-40);
	}

	@UiHandler("viewSampleResportsBtn")
	public void onClickViewSampleReportBtn(ClickEvent event){
		SampleReportView sampleReportView= new SampleReportView();
	}
	@UiHandler("achTerms")
	public void onClickTermsLink(ClickEvent envent){
		Window.enableScrolling(false);
		AppClientFactory.fireEvent(new SetHeaderZIndexEvent(99, false));	
		
		termsOfUse=new TermsOfUse(){

			@Override
			public void openParentPopup() {
				// TODO Auto-generated method stub
				
			}
			
		};
		termsOfUse.show();
		termsOfUse.setSize("902px", "300px");
		termsOfUse.center();
	}
	@UiHandler("achPrivacy")
	public void onClickPrivacyLink(ClickEvent envent){
		Window.enableScrolling(false);
		AppClientFactory.fireEvent(new SetHeaderZIndexEvent(99, false));	
		TermsAndPolicyVc termsAndPolicyVc = new TermsAndPolicyVc(false) {
			
			@Override
			public void openParentPopup() {
				
			}
		};
		termsAndPolicyVc.show();
		termsAndPolicyVc.setSize("902px", "300px");
		termsAndPolicyVc.center();
	}
//	@UiHandler("achDataPolicy")
//	public void onClickDataPolicyLink(ClickEvent envent){
//		
//	}
	@UiHandler("achCopyright")
	public void onClickCopyrightLink(ClickEvent envent){
		Window.enableScrolling(false);
		AppClientFactory.fireEvent(new SetHeaderZIndexEvent(99, false));	
		
		CopyRightPolicyVc copyRightPolicy = new CopyRightPolicyVc() {
			
			@Override
			public void openParentPopup() {
				//No need to set.
			}
		};
		copyRightPolicy.setSize("902px", "300px");
		copyRightPolicy.center();
		copyRightPolicy.show();

	}
	
	public AppSuggestBox getEditSearchTxtBox() {
		return txtSearch;
	}

	public void setEditSearchTxtBox(AppSuggestBox editSearchTxtBox) {
		this.txtSearch = editSearchTxtBox;
	}
	public void requestAutoSuggestKeyword(
			SearchDo<AutoSuggestKeywordSearchDo> searchDo) {
		getAutoSuggestionKeyWordAsyncCallback().execute(searchDo);
	}

	public void setAutoKeyWordSuggestions(
			SearchDo<AutoSuggestKeywordSearchDo> autoSuggestKeywordDo) {
		autokeySuggestOracle.clear();
		this.autoSuggestKeywordDo = autoSuggestKeywordDo;
		searchData = searchData + GOORU_SEARCH;
		autokeySuggestOracle.add(searchData);
		if (this.autoSuggestKeywordDo.getSearchResults() != null) {
			for (AutoSuggestKeywordSearchDo autoSuggestKeywordSearchDo : autoSuggestKeywordDo
					.getSearchResults()) {
				autokeySuggestOracle.add(autoSuggestKeywordSearchDo
						.getKeyword());
			}
		}
		getEditSearchTxtBox().showSuggestionList();

	}

	/**
	 * @return suggestion standards for the collection as map string
	 */
	public SearchAsyncCallback<SearchDo<AutoSuggestKeywordSearchDo>> getAutoSuggestionKeyWordAsyncCallback() {
		if (autoKeyWordSuggestionAsyncCallback == null) {
			autoKeyWordSuggestionAsyncCallback = new SearchAsyncCallback<SearchDo<AutoSuggestKeywordSearchDo>>() {

				@Override
				protected void run(SearchDo<AutoSuggestKeywordSearchDo> searchDo) {
					AppClientFactory.getInjector().getSearchService()
							.getSuggestedAutokeyword(searchDo, this);

				}

				@Override
				public void onCallSuccess(
						SearchDo<AutoSuggestKeywordSearchDo> result) {
					setAutoKeyWordSuggestions(result);

				}

			};
		}
		return autoKeyWordSuggestionAsyncCallback;
	}

	@Override
	public void onSelection(SelectionEvent<Suggestion> event) {
		String searchText = txtSearch.getText();
		searchText= searchText.replaceAll("-<n> Gooru Search</n>", "");
		txtSearch.setText(searchText.trim());
		Window.enableScrolling(true);
		AppClientFactory.fireEvent(new SetHeaderZIndexEvent(0, true));
		if (txtSearch.getText() != null && txtSearch.getText().length() > 0) {
			MixpanelUtil.Perform_Search(txtSearch.getText().trim().toLowerCase(),"HeaderUc");
			Map<String, String> params = new HashMap<String, String>();
			params = updateParams(params);
			savePlaceRequest();
			if (AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(
					PlaceTokens.COLLECTION_SEARCH)) {
				AppClientFactory.getPlaceManager().revealPlace(
						PlaceTokens.COLLECTION_SEARCH, params);
			} else {
				String queryVal = params.get("query");
				//queryVal = queryVal.replaceAll("%5C1", "&");
				Map<String, String> map = params;
				map.put("query", queryVal);
				AppClientFactory.getPlaceManager().revealPlace(
						PlaceTokens.RESOURCE_SEARCH, map);
			}
			txtSearch.setText("");
			AppClientFactory.fireEvent(new HomeEvent(HeaderTabType.DISCOVER));
			txtSearch.hideSuggestionList();
		}
		MixpanelUtil.mixpanelEvent("Select_Autocomplete_Search");
		getEditSearchTxtBox().setText(searchText.trim());

	}
	/** 
	 * This method is to get the btnSignUp
	 */
	@Override
	public Button getBtnSignUp() {
		return btnSignUp;
	}
	/** 
	 * This method is to set the btnSignUp
	 */
	public void setBtnSignUp(Button btnSignUp) {
		this.btnSignUp = btnSignUp;
	}

}


