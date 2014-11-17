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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.client.CssTokens;
import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.effects.FadeInAndOut;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.search.event.AggregatorSuggestionEvent;
import org.ednovo.gooru.client.mvp.search.event.GetSearchKeyWordEvent;
import org.ednovo.gooru.client.mvp.search.event.SourceSuggestionEvent;
import org.ednovo.gooru.client.mvp.search.event.StandardsSuggestionEvent;
import org.ednovo.gooru.client.mvp.search.event.StandardsSuggestionInfoEvent;
import org.ednovo.gooru.client.uc.AppMultiWordSuggestOracle;
import org.ednovo.gooru.client.uc.AppSuggestBox;
import org.ednovo.gooru.client.uc.DisclosurePanelUc;
import org.ednovo.gooru.client.uc.DownToolTipUc;
import org.ednovo.gooru.client.uc.DownToolTipWidgetUc;
import org.ednovo.gooru.client.uc.H4Panel;
import org.ednovo.gooru.client.uc.H5Panel;
import org.ednovo.gooru.client.uc.PPanel;
import org.ednovo.gooru.client.uc.StandardPreferenceTooltip;
import org.ednovo.gooru.client.uc.tooltip.ToolTip;
import org.ednovo.gooru.client.ui.HTMLEventPanel;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.code.CodeDo;
import org.ednovo.gooru.shared.model.search.AbstractSearchDo;
import org.ednovo.gooru.shared.model.search.SearchDo;
import org.ednovo.gooru.shared.model.search.SearchFilterDo;
import org.ednovo.gooru.shared.model.user.ProfileDo;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Search Team
 * 
 */
public class SearchFilterVc extends Composite implements SelectionHandler<SuggestOracle.Suggestion> {

	private static SearchFilterVcUiBinder uiBinder = GWT.create(SearchFilterVcUiBinder.class);
	
	private static MessageProperties i18n = GWT.create(MessageProperties.class);

	interface SearchFilterVcUiBinder extends UiBinder<Widget, SearchFilterVc> {
	}
	public interface Style extends CssResource {
		String active();
	}

	/*@UiField
	DisclosurePanelUc categoryPanelUc;*/
	
	/*@UiField
	Anchor resourceLinkLbl, collectionLinkLbl;*/

	/*@UiField
	DisclosurePanelUc subjectPanelUc;*/

	/*@UiField
	DisclosurePanelUc gradePanelUc;*/

	/*@UiField
	DisclosurePanelUc sourcePanelUc,aggregatorPanelUc;*/

	/*@UiField
	DisclosurePanelUc standardPanelUc;*/

	/*@UiField
	DisclosurePanelUc authorPanelUc;*/
	
	@UiField HTMLPanel categoryPanelUc,subjectPanelUc,gradePanelUc,aggregatorPanelUc,sourcePanelUc,authorPanelUc,standardPanelUc,accessModePanel;
	
	@UiField PPanel panelNotMobileFriendly; 
	
	@UiField
	PPanel /*contentpanel,*/oerPanel;
	
	@UiField
	H5Panel resourceFormatLbl,subjectLbl,gradeLbl,standardLbl,accessModeLbl;
	@UiField(provided = true)
	AppSuggestBox sourceSgstBox;

	@UiField
	TextBox authorTxtBox;

	@UiField(provided = true)
	AppSuggestBox standardSgstBox;
	
	@UiField(provided = true)
	AppSuggestBox aggregatorSgstBox;
	
	@UiField
	FlowPanel /*flowpanel,*/myCollectionSearch,authorContainerFloPanel;

	@UiField
	FlowPanel sourceContainerFloPanel,aggregatorContainerFloPanel;

	@UiField
	FlowPanel standardContainerFloPanel;

	@UiField
	Label sourcesNotFoundLbl,aggregatorNotFoundLbl;
	
	@UiField
	H4Panel filtersText;

	@UiField
	Label standardsNotFoundLbl;
	
	@UiField
	Label publisherTooltip, standardHelpicon,clearAll,aggregatorTooltip,aggregatorLbl,sourceLbl,authorLbl;

	@UiField
	HTMLEventPanel sourceToolTip, standardToolTip,aggregatorToolTip;
	
	@UiField
	SearchCBundle res;
	/*@UiField Image publisherTooltip;*/
	CheckBox chkNotFriendly = null;
	CheckBox chkOER = null;
	CheckBox chkAccessMode = null;
	
	@UiField Button browseStandards;
	
	@UiField
	Style style;
	
	ToolTip toolTip = null;
	
	private AppMultiWordSuggestOracle sourceSuggestOracle;

	private AppMultiWordSuggestOracle standardSuggestOracle;
	
	private AppMultiWordSuggestOracle aggregatorSuggestOracle;

	private SearchDo<CodeDo> standardSearchDo = new SearchDo<CodeDo>();

	private SearchDo<CodeDo> standardsInfoSearchDo = new SearchDo<CodeDo>();

	private SearchDo<String> sourceSearchDo = new SearchDo<String>();
	
	private SearchDo<String> aggregatorSearchDo = new SearchDo<String>();
	
	private Map<String,String> standardCodesMap = new HashMap<String, String>();

	private static final String NO_MATCH_FOUND = i18n.GL0723();
	
	private static final String ALL = "All";
	
	private static final String COMMA_SEPARATOR = i18n.GL_GRR_COMMA();

	private boolean resourceSearch;
	
	private DownToolTipUc sourcetooltipPopUpUc;
	
	private DownToolTipUc standardtooltipPopUpUc;

	private boolean isSourcePopupShowing=false;

	private boolean isStandardPopupShowing=false;
	
	private DownToolTipUc aggregatortooltipPopUpUc;
	
	private static final String USER_META_ACTIVE_FLAG = "0";
	
	List<String> standardPreflist = null;
	List<String> standardPrefListElement = null;
	
	private static final  String  AUDITORY ="auditory";
	private static final  String  TACTILE ="tactile";
	private static final  String  VISUAL ="visual";
	private static final  String  COLOR_DEPENDENT ="color dependent";
	private static final  String  TEXT_ON_IMAGE ="text on image";
	private static final  String  TEXTUAL ="textual";
				
	/**
	 * Class constructor, creates new {@link AppSuggestBox} and events for StandardsSuggestionEvent
	 * 
	 * @param resourceSearch whether resource search or not
	 */
	public SearchFilterVc(final boolean resourceSearch) {
		this.resourceSearch = resourceSearch;
		res.INSTANCE.css().ensureInjected();
		standardSuggestOracle = new AppMultiWordSuggestOracle(true);
		standardSearchDo.setPageSize(15);
		standardsInfoSearchDo.setPageSize(20);
		standardsInfoSearchDo.setQuery("-");
		standardsInfoSearchDo.setType(AbstractSearchDo.STANDARD_CODE);
		if(AppClientFactory.getLoggedInUser().getSettings().getTaxonomyPreferences()!=null){
		String[] standards = AppClientFactory.getLoggedInUser().getSettings().getTaxonomyPreferences().split(",");
		standardPrefListElement=new ArrayList<String>();
			for (String str : standards) {
				standardPrefListElement.add(str);
		 }
		}
		final StandardPreferenceTooltip standardPreferenceTooltip=new StandardPreferenceTooltip();
		
		standardSgstBox = new AppSuggestBox(standardSuggestOracle) {
			@Override
			public void keyAction(String text) {
				text=text.toUpperCase();
				if(AppClientFactory.isAnonymous()) {
					standardSearchDo.setSearchResults(null);
					standardSearchDo.setQuery(text);
					if (text != null && text.trim().length() > 0) {
						AppClientFactory.fireEvent(new StandardsSuggestionEvent(standardSearchDo));
					}
				} else {
					boolean standardsPrefDisplayPopup = false;
					standardPreferenceTooltip.hide();
					standardSearchDo.setSearchResults(null);
					standardSearchDo.setQuery(text);
					if (text != null && text.trim().length() > 0) {
						
						standardPreferenceTooltip.hide();
						standardSuggestOracle.clear();
							if(standardPreflist!=null){
								for(int count=0; count<standardPreflist.size();count++) {
									if(text.contains("CCSS") || text.contains("TEKS") || text.contains("CA") ||text.contains("NGSS")||text.contains("CAS612")||text.contains("CASK5")||text.contains("CAELD")||text.contains("CSC")) {
										if(text.contains(standardPreflist.get(count))) {
											standardsPrefDisplayPopup = true;
											break;
										} else {
											standardsPrefDisplayPopup = false;
										}
									} else {
										standardsPrefDisplayPopup = true;
									}
								}
							}
							
							if(standardsPrefDisplayPopup==true){
								standardPreferenceTooltip.hide();
								AppClientFactory.fireEvent(new StandardsSuggestionEvent(standardSearchDo));
							} else{
								standardSgstBox.hideSuggestionList();
								standardSuggestOracle.clear();
								standardPreferenceTooltip.show();
								standardPreferenceTooltip.setPopupPosition(standardSgstBox.getAbsoluteLeft()+3, standardSgstBox.getAbsoluteTop()+33);
							}
							
						}
					}
			}

			@Override
			public HandlerRegistration addClickHandler(ClickHandler handler) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		
		sourceSuggestOracle = new AppMultiWordSuggestOracle(true);
		sourceSearchDo.setPageSize(10);		
		
		sourceSgstBox = new AppSuggestBox(sourceSuggestOracle) {
			@Override
			public void keyAction(String text) {
				if (resourceSearch) {
					sourceSearchDo.setSearchResults(null);
					sourceSearchDo.setQuery(text);
					if (text != null && text.trim().length() > 0) {
						AppClientFactory.fireEvent(new SourceSuggestionEvent(sourceSearchDo));
					}
				}
			}

			@Override
			public HandlerRegistration addClickHandler(ClickHandler handler) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		aggregatorSuggestOracle = new AppMultiWordSuggestOracle(true);
		aggregatorSearchDo.setPageSize(10);	
		aggregatorSgstBox = new AppSuggestBox(aggregatorSuggestOracle) {
			
			@Override
			public HandlerRegistration addClickHandler(ClickHandler handler) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void keyAction(String text) {
				if (resourceSearch) {
					aggregatorSearchDo.setSearchResults(null);
					aggregatorSearchDo.setQuery(text);
					if (text != null && text.trim().length() > 0) {
						AppClientFactory.fireEvent(new AggregatorSuggestionEvent(aggregatorSearchDo));
					}
				}
				
			}
		};
		
/*	ClickHandler eve1=new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(isSourcePopupShowing){
					sourceToolTip.setVisible(true);
					isSourcePopupShowing=false;
				}else{
					sourceToolTip.setVisible(false);
					isSourcePopupShowing=true;
				}
				if(isStandardPopupShowing){
					standardToolTip.setVisible(true);
					isStandardPopupShowing=false;
				}else{
					sourceToolTip.setVisible(false);
					isStandardPopupShowing=true;
				}
			}
		};
		RootPanel.get().addDomHandler(eve1, ClickEvent.getType());
*/
		
		sourceSgstBox.getElement().setAttribute("placeHolder", i18n.GL1464());
		sourceSgstBox.getElement().setId("asSourceSgst");
		sourceSgstBox.getElement().setAttribute("style","margin-top: 5px;");
		standardSgstBox.addSelectionHandler(this);
		aggregatorSgstBox.getElement().setId("asAggregatorSgst");
		aggregatorSgstBox.getElement().setAttribute("placeHolder", i18n.GL1749());
		aggregatorSgstBox.getElement().setAttribute("style","margin-top: 5px;");
		
		BlurHandler blurhander=new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if(standardPreferenceTooltip!=null && standardPreferenceTooltip.isShowing()){
					standardPreferenceTooltip.hide();
				}
			}
		};
		standardSgstBox.addDomHandler(blurhander, BlurEvent.getType());
		initWidget(uiBinder.createAndBindUi(this));
		filtersText.setText(i18n.GL0719());
		filtersText.getElement().setId("lblFiltersText");
		filtersText.getElement().setAttribute("alt",i18n.GL0719());
		filtersText.getElement().setAttribute("title",i18n.GL0719());
		
		/*resourceLinkLbl.setText(i18n.GL0174());
		resourceLinkLbl.getElement().setAttribute("alt",i18n.GL0174());
		resourceLinkLbl.getElement().setAttribute("title",i18n.GL0174());*/
		
		/*notifyText.setText(i18n.GL0720());
		notifyText.getElement().setId("lblNotifyText");
		notifyText.getElement().setAttribute("alt",i18n.GL0720());
		notifyText.getElement().setAttribute("title",i18n.GL0720());*/
		
		/*collectionLinkLbl.setText(i18n.GL0175());
		collectionLinkLbl.getElement().setAttribute("alt",i18n.GL0175());
		collectionLinkLbl.getElement().setAttribute("title",i18n.GL0175());*/
		
		resourceFormatLbl.setText(i18n.GL0721());
		resourceFormatLbl.getElement().setId("lblCategory");
		resourceFormatLbl.getElement().setAttribute("alt",i18n.GL0721());
		resourceFormatLbl.getElement().setAttribute("title",i18n.GL0721());
		
		sourceLbl.setText(i18n.GL0566());
		sourceLbl.getElement().setId("lblSource");
		sourceLbl.getElement().setAttribute("alt",i18n.GL0566());
		sourceLbl.getElement().setAttribute("title",i18n.GL0566());
		
		//sourceHelpicon.setText(i18n.GL_SPL_QUESTION);
		sourcesNotFoundLbl.setText(i18n.GL0723());
		sourcesNotFoundLbl.getElement().setId("lblSourcesNotFoundLbl");
		sourcesNotFoundLbl.getElement().setAttribute("alt",i18n.GL0723());
		sourcesNotFoundLbl.getElement().setAttribute("title",i18n.GL0723());
		
		authorLbl.setText(i18n.GL0573());
		authorLbl.getElement().setId("lblAuthor");
		authorLbl.getElement().setAttribute("alt",i18n.GL0573());
		authorLbl.getElement().setAttribute("title",i18n.GL0573());
		
		standardLbl.setText(i18n.GL0724());
		standardLbl.getElement().setId("lblStandard");
		standardLbl.getElement().setAttribute("alt",i18n.GL0724());
		standardLbl.getElement().setAttribute("title",i18n.GL0724());
		
		standardHelpicon.setText(i18n.GL_SPL_QUESTION());
		standardHelpicon.getElement().setId("lblStandardHelpicon");
		standardHelpicon.getElement().setAttribute("alt",i18n.GL_SPL_QUESTION());
		standardHelpicon.getElement().setAttribute("title",i18n.GL_SPL_QUESTION());
		
		standardsNotFoundLbl.setText(i18n.GL0723());
		standardsNotFoundLbl.getElement().setId("lblStandardsNotFoundLbl");
		standardsNotFoundLbl.getElement().setAttribute("alt",i18n.GL0723());
		standardsNotFoundLbl.getElement().setAttribute("title",i18n.GL0723());
		
		subjectLbl.setText(i18n.GL0226());
		subjectLbl.getElement().setId("lblSubject");
		subjectLbl.getElement().setAttribute("alt",i18n.GL0226());
		subjectLbl.getElement().setAttribute("title",i18n.GL0226());
		
		gradeLbl.setText(i18n.GL0165());
		gradeLbl.getElement().setId("lblGrade");
		gradeLbl.getElement().setAttribute("alt",i18n.GL0165());
		gradeLbl.getElement().setAttribute("title",i18n.GL0165());
		
		accessModeLbl.setText(i18n.GL2093());
		accessModeLbl.getElement().setId("lblAccessMode");
		accessModeLbl.getElement().setAttribute("alt",i18n.GL2093());
		accessModeLbl.getElement().setAttribute("title",i18n.GL2093());
		
		clearAll.setText(i18n.GL0725());
		clearAll.getElement().setId("lblClearAll");
		clearAll.getElement().setAttribute("alt",i18n.GL0725());
		clearAll.getElement().setAttribute("title",i18n.GL0725());
		
		aggregatorLbl.setText(i18n.GL1628()+i18n.GL_SPL_SEMICOLON()+" ");
		aggregatorLbl.getElement().setId("lblAggregator");
		aggregatorLbl.getElement().setAttribute("alt",i18n.GL1628()+i18n.GL_SPL_SEMICOLON()+" ");
		aggregatorLbl.getElement().setAttribute("title",i18n.GL1628()+i18n.GL_SPL_SEMICOLON()+" ");
		
		/*standardSgstBox.getElement().getStyle().setMarginTop(2, Unit.PX);
		standardSgstBox.getElement().getStyle().setMarginLeft(3, Unit.PX);*/
		
	//	browseStandards.getElement().getStyle().setPadding(4, Unit.PX);
		
//		aggregatorPanelUc.setHeaderTitle(i18n.GL1628()+i18n.GL_SPL_SEMICOLON()+" ");
		
		if (resourceSearch) {
			sourcePanelUc.setVisible(true);
			aggregatorPanelUc.setVisible(true);
			sourceLbl.setVisible(true);
			aggregatorLbl.setVisible(true);
			accessModeLbl.setVisible(true);
			sourcesNotFoundLbl.getElement().getStyle().setOpacity(0.0);
			sourceSgstBox.addSelectionHandler(this);
			aggregatorSgstBox.addSelectionHandler(this);
			aggregatorNotFoundLbl.getElement().getStyle().setOpacity(0.0);
						
			publisherTooltip.addMouseOverHandler(new MouseOverHandler() {
				@Override
				public void onMouseOver(MouseOverEvent event) {
					toolTip = new ToolTip(i18n.GL1769());
					toolTip.getLblLink().setVisible(false);
					toolTip.getElement().getStyle().setBackgroundColor("transparent");
					toolTip.getElement().getStyle().setPosition(Position.ABSOLUTE);
					toolTip.setPopupPosition(publisherTooltip.getAbsoluteLeft()-(50+22), publisherTooltip.getAbsoluteTop()+22);
					toolTip.show();
				}
			});
			publisherTooltip.addMouseOutHandler(new MouseOutHandler() {
				
				@Override
				public void onMouseOut(MouseOutEvent event) {
					EventTarget target = ((MouseOutEvent) event).getRelatedTarget();
					  if (Element.is(target)) {
						  if (!toolTip.getElement().isOrHasChild(Element.as(target))){
							  toolTip.hide();
						  }
					  }	
					
				}
			});
			aggregatorTooltip.addMouseOverHandler(new MouseOverHandler() {
				
				@Override
				public void onMouseOver(MouseOverEvent event) {
					toolTip = new ToolTip(i18n.GL1768());
					toolTip.getLblLink().setVisible(false);
					toolTip.getElement().getStyle().setBackgroundColor("transparent");
					toolTip.getElement().getStyle().setPosition(Position.ABSOLUTE);
					toolTip.setPopupPosition(aggregatorTooltip.getAbsoluteLeft()-(50+22), aggregatorTooltip.getAbsoluteTop()+22);
					toolTip.show();
					
				}
			});
			aggregatorTooltip.addMouseOutHandler(new MouseOutHandler() {
				
				@Override
				public void onMouseOut(MouseOutEvent event) {
					EventTarget target = ((MouseOutEvent) event).getRelatedTarget();
					  if (Element.is(target)) {
						  if (!toolTip.getElement().isOrHasChild(Element.as(target))){
							  toolTip.hide();
						  }
					  }	
					
				}
			});
			
		} else {
			authorLbl.setVisible(true);
			authorPanelUc.setVisible(true);
			authorTxtBox.getElement().setId("tbAuthor");
			StringUtil.setAttributes(authorTxtBox, true);
			authorTxtBox.addKeyUpHandler(new KeyUpHandler() {

				@Override
				public void onKeyUp(KeyUpEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						if (authorTxtBox.getText() != null && authorTxtBox.getText().length() > 0) {
							String text = authorTxtBox.getValue();
							authorContainerFloPanel.add(new FilterLabelVc(text,true));
							authorTxtBox.setText("");
							authorTxtBox.getElement().setAttribute("alt","");
							authorTxtBox.getElement().setAttribute("title","");
							AppClientFactory.fireEvent(new GetSearchKeyWordEvent());
						}
					}
				}
			});
		}
		standardsNotFoundLbl.getElement().getStyle().setOpacity(0.0);
		if(resourceSearch){
			resourceFormatLbl.setText(i18n.GL0721());
			categoryPanelUc.getElement().addClassName("categoryFilterContainer");
		}else{
			resourceFormatLbl.setText(i18n.GL1465());
		}
		
		/*resourceLinkLbl.getElement().setId("lblResourceLink");
		collectionLinkLbl.getElement().setId("lblCollectionLink");
		
		resourceLinkLbl.setText(i18n.GL0174());
		resourceLinkLbl.getElement().setAttribute("alt",i18n.GL0174());
		resourceLinkLbl.getElement().setAttribute("title",i18n.GL0174());
		
		collectionLinkLbl.setText(i18n.GL0175());
		collectionLinkLbl.getElement().setAttribute("alt",i18n.GL0175());
		collectionLinkLbl.getElement().setAttribute("title",i18n.GL0175());*/
		
		myCollectionSearch.getElement().setId("fpnlMyCollectionSearch");
//		flowpanel.getElement().setId("fpnlFlowpanel");
//		contentpanel.getElement().setId("pnlContentpanel");
		panelNotMobileFriendly.getElement().setId("pnlPanelNotMobileFriendly");
		accessModePanel.getElement().setId("pnlaccessMode");
		oerPanel.getElement().setId("pnlOerPanel");
		aggregatorPanelUc.getElement().setId("discpnlAggregatorPanelUc");
		aggregatorTooltip.getElement().setId("lblAggregatorTooltip");
		aggregatorToolTip.getElement().setId("epnlAggregatorToolTip");
		aggregatorNotFoundLbl.getElement().setId("lblAggregatorNotFoundLbl");
		aggregatorContainerFloPanel.getElement().setId("fpnlAggregatorContainerFloPanel");
		publisherTooltip.getElement().setId("lblPublisherTooltip");
		sourceToolTip.getElement().setId("epnlSourceToolTip");
		sourcesNotFoundLbl.getElement().setId("lblSourcesNotFoundLbl");
		sourceContainerFloPanel.getElement().setId("fpnlSourceContainerFloPanel");
		authorContainerFloPanel.getElement().setId("fpnlAuthorContainerFloPanel");
		standardSgstBox.getElement().setId("tbautoStandards");
		standardToolTip.getElement().setId("epnlStandardToolTip");
		standardsNotFoundLbl.getElement().setId("lblStandardsNotFoundLbl");
		standardContainerFloPanel.getElement().setId("fpnlStandardContainerFloPanel");
}
	

	/**
	 * @param disclosurePanelVc instance of DisclosurePanelUc which gets added widget
	 * @param key check box name
	 * @param value check box value
	 */
	public void renderCheckBox(DisclosurePanelUc disclosurePanelVc, String key, String value) {
		CheckBox categoryChk = new CheckBox();
		categoryChk.setText(value);
		categoryChk.setName(key);
		if(value.equalsIgnoreCase("videos")){
			categoryChk.setText("Video");
			MixpanelUtil.mixpanelEvent("search_video_filter_selected");
			categoryChk.getElement().setId("chkVideos");	
		}else if(value.equalsIgnoreCase("webpage")){
			MixpanelUtil.mixpanelEvent("search_webpage_filter_selected");
			categoryChk.getElement().setId("chkwebpage");	
		}
		/*else if(value.equalsIgnoreCase("websites"))
		{
			MixpanelUtil.mixpanelEvent("search_websites_filter_selected");
			categoryChk.getElement().setId("chkwebsites");	
		}*/
		else if(value.equalsIgnoreCase("interactives")){
			categoryChk.setText("Interactive");
			MixpanelUtil.mixpanelEvent("search_interactives_filter_selected");
			categoryChk.getElement().setId("chkInteractives");
		}
		else if(value.equalsIgnoreCase("questions")){
			categoryChk.setText("Question");
			MixpanelUtil.mixpanelEvent("search_questions_filter_selected");
			categoryChk.getElement().setId("chkQuestions");
		}
		else if(value.equalsIgnoreCase("images")){
			categoryChk.setText("Image");
			MixpanelUtil.mixpanelEvent("search_images_filter_selected");
			categoryChk.getElement().setId("chkImages");
		}
		else if(value.equalsIgnoreCase("texts")){
			categoryChk.setText("Text");
			MixpanelUtil.mixpanelEvent("search_texts_filter_selected");
			categoryChk.getElement().setId("chkTexts");
		}
		else if(value.equalsIgnoreCase("audio")){
			MixpanelUtil.mixpanelEvent("search_audios_filter_selected");
			categoryChk.getElement().setId("chkAudios");
		}
		/*else if(value.equalsIgnoreCase("others")){
			MixpanelUtil.mixpanelEvent("search_others_filter_selected");
			categoryChk.getElement().setId("chkOther");
		}*/
		else if(value.equalsIgnoreCase("science")){
			categoryChk.getElement().setId("chkScience");
		}
		else if(value.equalsIgnoreCase("math")){
			categoryChk.getElement().setId("chkMath");
		}
		else if(value.equalsIgnoreCase("Social Sciences")){
			categoryChk.getElement().setId("chkSocialSciences");
		}
		else if(value.equalsIgnoreCase("Language Arts")){
			categoryChk.getElement().setId("chkLanguageArts");
		}
		else if(value.equalsIgnoreCase("Arts and Humanities")){
			categoryChk.getElement().setId("chkArts&Humanities");
		}
		else if(value.equalsIgnoreCase("Technology & Engineering")){
			categoryChk.setText("Tech & Engineering");
			categoryChk.getElement().setId("chkTechnology&Engineering");
		}
		else if(value.equalsIgnoreCase("Elementary School")){
			categoryChk.getElement().setId("chkElementarySchool");
		}
		else if(value.equalsIgnoreCase("Middle School")){
			categoryChk.getElement().setId("chkMiddleSchool");
		}
		else if(value.equalsIgnoreCase("High School")){
			categoryChk.getElement().setId("chkHighSchool");
		}
		else if(value.equalsIgnoreCase("Higher Education")){
			categoryChk.getElement().setId("chkHigherEducation");
		}
		else if(value.equalsIgnoreCase("Show only Quizzes")){
			categoryChk.getElement().setId("chkShowonlyQuizzes");
		}
		categoryChk.setStyleName(CssTokens.FILTER_CHECKBOX);
		categoryChk.addStyleName(value.toLowerCase());
		disclosurePanelVc.addWidget(categoryChk);
		categoryChk.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				AppClientFactory.fireEvent(new GetSearchKeyWordEvent());
			}
		});
	}
	public void renderOERCheckBox(PPanel disclosurePanelVc, String key, final String value) {
		chkOER = new CheckBox();	
		chkOER.setText(value);
		chkOER.setName(key);
		
		if(value.equalsIgnoreCase("OER")){
			chkOER.getElement().setId("chkOer");
			//chkOER.getElement().getStyle().setMarginTop(20, Unit.PX);
		}
		//	chkOER.setStyleName(CssTokens.FILTER_CHECKBOX);
			chkOER.addStyleName(value.toLowerCase());
			disclosurePanelVc.add(chkOER);
	
		chkOER.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (chkOER.getValue()){
					MixpanelUtil.mixpanelEvent("checks the OER filter box");

				}else{
					MixpanelUtil.mixpanelEvent("unchecks the OER filter box");
				}
				AppClientFactory.fireEvent(new GetSearchKeyWordEvent());
				
			}
		});
		
	}
	
	private void renderAccessModeCheckBox(HTMLPanel accessModePanel,String key,String value) {
		chkAccessMode = new CheckBox();
		chkAccessMode.setText(value);
		chkAccessMode.setName(key);
		chkAccessMode.setStyleName(CssTokens.FILTER_CHECKBOX);
		chkAccessMode.addStyleName(value.toLowerCase());
		accessModePanel.add(chkAccessMode);
		chkAccessMode.addValueChangeHandler(new ValueChangeHandler<Boolean>(){

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				AppClientFactory.fireEvent(new GetSearchKeyWordEvent());  
			}
			
		});
	}
	
	
	/**
	 * @param disclosurePanelVc instance of DisclosurePanelUc which gets added widget
	 * @param key check box name
	 * @param value check box value
	 */
	public void renderCheckBox(PPanel disclosurePanelVc, String key, final String value) {
		
		chkNotFriendly = new CheckBox();
		chkNotFriendly.setText(value);
		chkNotFriendly.setName(key);
		
		if(value.equalsIgnoreCase("Mobile Friendly")){
			//disclosurePanelVc.setStyleName("mobilefriendlyContainer");
			chkNotFriendly.getElement().setId("chkNotFriendly");
//			chkNotFriendly.getElement().getStyle().setMarginTop(20, Unit.PX);
	
		}
		//chkNotFriendly.setStyleName(CssTokens.FILTER_CHECKBOX);
		chkNotFriendly.addStyleName(value.toLowerCase());
		disclosurePanelVc.add(chkNotFriendly);
		chkNotFriendly.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (chkNotFriendly.getValue()){
						MixpanelUtil.MOS_Filter("Selected");
					
				}else{
						MixpanelUtil.MOS_Filter("Unselected");
				}
				AppClientFactory.fireEvent(new GetSearchKeyWordEvent());
			}
		});
		
	}
	
	public void renderCheckBox1(HTMLPanel disclosurePanelVc, String key, final String value) {
		chkNotFriendly = new CheckBox();
		chkNotFriendly.setText(value);
		chkNotFriendly.setName(key);
		
		if(value.equalsIgnoreCase("Mobile Friendly")){
			//disclosurePanelVc.setStyleName("mobilefriendlyContainer");
			chkNotFriendly.getElement().setId("chkNotFriendly");
//			chkNotFriendly.getElement().getStyle().setMarginTop(20, Unit.PX);
	
		}
		if(resourceSearch)
		chkNotFriendly.setStyleName(res.css().filterCheckBox());
		chkNotFriendly.addStyleName(value.toLowerCase());
		disclosurePanelVc.add(chkNotFriendly);
		chkNotFriendly.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (chkNotFriendly.getValue()){
						MixpanelUtil.MOS_Filter("Selected");
					
				}else{
						MixpanelUtil.MOS_Filter("Unselected");
				}
				AppClientFactory.fireEvent(new GetSearchKeyWordEvent());
			}
		});
		
	}

	/*public void renderRadioButtons(final DisclosurePanelUc disclosurePanelVc, String key, String value){
		final QuestionTypeFilter questionTypeFilter=new QuestionTypeFilter(key);
		questionTypeFilter.radioButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				FlowPanel flowPanel=disclosurePanelVc.getContent();
				for(int i=0;i<flowPanel.getWidgetCount();i++){
					
					QuestionTypeFilter widget=(QuestionTypeFilter)flowPanel.getWidget(i);
					widget.radioButton.setStyleName(SearchMoreInfoVcCBundle.INSTANCE.css().questionRadioButton());
					
				}
				
				questionTypeFilter.radioButton.setStyleName(SearchMoreInfoVcCBundle.INSTANCE.css().questionRadioButtonSelected());
				if(!questionTypeFilter.hiddenRadioButton.getValue()){
					questionTypeFilter.hiddenRadioButton.setValue(true);
					AppClientFactory.fireEvent(new GetSearchKeyWordEvent());
				}
			}
		});
		questionTypeFilter.radioButtonLabel.setText(value);
				
		disclosurePanelVc.addWidget(questionTypeFilter);
	}*/
	

	/**
	 * Set resource search page view
	 * @param clickEvent instance of {@link ClickEvent}
	 */
	/*@UiHandler("resourceLinkLbl")
	public void onResourceLinkLblClicked(ClickEvent clickEvent) {
		
		if(!AppClientFactory.getPlaceManager().getRequestParameter("query").equalsIgnoreCase("")){ 
			MixpanelUtil.Show_Collection_Search_Results();
			AppClientFactory.fireEvent(new SwitchSearchEvent(PlaceTokens.RESOURCE_SEARCH,AppClientFactory.getPlaceManager().getRequestParameter("query")));
		}
	}*/

	/**
	 * Set collection search page view 
	 * @param clickEvent instance of {@link ClickEvent}
	 */
	/*@UiHandler("collectionLinkLbl")
	public void onCollectionLinkLblClicked(ClickEvent clickEvent) {
		if(!AppClientFactory.getPlaceManager().getRequestParameter("query").equalsIgnoreCase("")){ 
			MixpanelUtil.Show_Collection_Search_Results();
			AppClientFactory.fireEvent(new SwitchSearchEvent(PlaceTokens.COLLECTION_SEARCH,AppClientFactory.getPlaceManager().getRequestParameter("query")));
		}
	}*/
	
	
	@UiHandler("standardHelpicon")
	public void onStandardHelpiconClicked(ClickEvent event) {
		if(!(standardToolTip.getWidgetCount()>0)) {
			standardtooltipPopUpUc = new DownToolTipUc();
			standardtooltipPopUpUc.setContent(new HTML(i18n.GL0247()));
			standardToolTip.add(standardtooltipPopUpUc);
		}
		if(standardToolTip.isVisible()) {
			standardToolTip.setVisible(false);
		} else {
			sourceToolTip.setVisible(false);
			standardToolTip.setVisible(true);
			aggregatorToolTip.setVisible(false);
		}
		isStandardPopupShowing = true;
	}
	public class QuestionTypeFilter extends Composite{
		
		FlowPanel flowPanel;
		InlineLabel radioButton;
		InlineLabel radioButtonLabel;
		RadioButton hiddenRadioButton=null;

	    public QuestionTypeFilter(String key){
	    	SearchMoreInfoVcCBundle.INSTANCE.css().ensureInjected();
	    	initWidget(flowPanel=new FlowPanel());
	    	flowPanel.setStyleName(SearchMoreInfoVcCBundle.INSTANCE.css().questionRadioButtonContainer());
	    	radioButton=new InlineLabel();
	    	radioButtonLabel=new InlineLabel();
	    	radioButton.setStyleName(SearchMoreInfoVcCBundle.INSTANCE.css().questionRadioButton());
	    	flowPanel.add(radioButton);
	    	flowPanel.add(radioButtonLabel);
	    	hiddenRadioButton=new RadioButton("qusetionFilter",key);
	    	hiddenRadioButton.getElement().getStyle().setDisplay(Display.NONE);
	    	flowPanel.add(hiddenRadioButton);
	    }

	}

	/**
	 * Get filter for search
	 * @param searchFilterDo instance of {@link SearchFilterDo}
	 */
	public void renderFilter(SearchFilterDo searchFilterDo) {
		categoryPanelUc.clear();
		subjectPanelUc.clear();
		gradePanelUc.clear();
		panelNotMobileFriendly.clear();
		accessModePanel.clear();
		oerPanel.clear();
		if (searchFilterDo != null) {
			if (searchFilterDo.getCategories() != null) {
				Iterator<Map.Entry<String, String>> categoriesIterator = searchFilterDo.getCategories().entrySet().iterator();
				while (categoriesIterator.hasNext()) {
					Map.Entry<String, String> entry = categoriesIterator.next();
					renderCheckBox1(categoryPanelUc, entry.getKey(), entry.getValue());
				}
			}
			if (searchFilterDo.getGradeLevels() != null) {		
				renderCheckBox1(gradePanelUc, "K-4", i18n.GL0166());
				renderCheckBox1(gradePanelUc, "5-8", i18n.GL0167());
				renderCheckBox1(gradePanelUc, "9-12", i18n.GL0168());
				renderCheckBox1(gradePanelUc, "H", i18n.GL0169());
			}
			if (searchFilterDo.getSubjects() != null) {
				for (String subject : searchFilterDo.getSubjects()) {
					renderCheckBox1(subjectPanelUc, subject, subject);
				}
			}
			
		}
		if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)){
			
			/*resourceLinkLbl.addStyleName(style.active());
			collectionLinkLbl.removeStyleName(style.active());*/
			renderAccessModeCheckBox(accessModePanel,AUDITORY,i18n.GL2094());
			renderAccessModeCheckBox(accessModePanel,TACTILE,i18n.GL2095());
			renderAccessModeCheckBox(accessModePanel,VISUAL,i18n.GL2096());
			renderAccessModeCheckBox(accessModePanel,COLOR_DEPENDENT,i18n.GL2097());
			renderAccessModeCheckBox(accessModePanel,TEXT_ON_IMAGE,i18n.GL2098());
			renderAccessModeCheckBox(accessModePanel,TEXTUAL,i18n.GL2099());
			
			renderOERCheckBox(oerPanel, "not_show_OER", "OER");
			renderCheckBox(panelNotMobileFriendly, "not_ipad_friendly", "Mobile Friendly");
			final Image imgNotFriendly = new Image("images/mos/questionmark.png");
			imgNotFriendly.getElement().getStyle().setLeft(104, Unit.PX);
			imgNotFriendly.getElement().getStyle().setTop(-25, Unit.PX);
			imgNotFriendly.getElement().getStyle().setMarginLeft(30, Unit.PX);
			imgNotFriendly.getElement().getStyle().setPosition(Position.RELATIVE);
	
			
			
/*			imgNotFriendly.getElement().getStyle().setMarginLeft(29, Unit.PX);
*/
			imgNotFriendly.getElement().getStyle().setCursor(Cursor.POINTER);
			imgNotFriendly.setAltText(i18n.GL0732());
			imgNotFriendly.setTitle(i18n.GL0732());
			imgNotFriendly.addMouseOverHandler(new MouseOverHandler() {
				
				@Override
				public void onMouseOver(MouseOverEvent event) {
					toolTip = new ToolTip(i18n.GL0454()+""+"<img src='/images/mos/MobileFriendly.png' style='margin-top:0px;width:20px;height:15px;'/>"+" "+i18n.GL04431()+" "+"<img src='/images/mos/mobileunfriendly.png' style='margin-top:0px;width:20px;height:15px;'/>"+" "+i18n.GL_SPL_EXCLAMATION());
					toolTip.getTootltipContent().getElement().setAttribute("style", "width: 258px;");
					toolTip.getElement().getStyle().setBackgroundColor("transparent");
					toolTip.getElement().getStyle().setPosition(Position.ABSOLUTE);
					toolTip.setPopupPosition(imgNotFriendly.getAbsoluteLeft()-(50+22), imgNotFriendly.getAbsoluteTop()+22);
					toolTip.show();
				}
			});
			imgNotFriendly.addMouseOutHandler(new MouseOutHandler() {
				
				@Override
				public void onMouseOut(MouseOutEvent event) {
					
					EventTarget target = event.getRelatedTarget();
					  if (Element.is(target)) {
						  if (!toolTip.getElement().isOrHasChild(Element.as(target))){
							  toolTip.hide();
						  }
					  }
				}
			});
			panelNotMobileFriendly.add(imgNotFriendly);
			panelNotMobileFriendly.setVisible(true);
			//added for OER search
			
			final Image oer = new Image("images/mos/questionmark.png");
			oer.getElement().getStyle().setLeft(72, Unit.PX);
			oer.getElement().getStyle().setTop(-23, Unit.PX);
			oer.getElement().getStyle().setPosition(Position.RELATIVE);
			oer.getElement().getStyle().setCursor(Cursor.POINTER);
			oer.setAltText(i18n.GL0732());
			oer.setTitle(i18n.GL0732());
			oer.addMouseOverHandler(new MouseOverHandler() {
				
				@Override
				public void onMouseOver(MouseOverEvent event) {
					toolTip = new ToolTip(i18n.GL1770());
					toolTip.getLblLink().setVisible(false);
					toolTip.getElement().getStyle().setBackgroundColor("transparent");
					toolTip.getElement().getStyle().setPosition(Position.ABSOLUTE);
					toolTip.setPopupPosition(oer.getAbsoluteLeft()-(50+22), oer.getAbsoluteTop()+22);
					toolTip.show();
				}
			
			});
			oer.addMouseOutHandler(new MouseOutHandler() {
				
				@Override
				public void onMouseOut(MouseOutEvent event) {
					EventTarget target = event.getRelatedTarget();
					  if (Element.is(target)) {
						  if (!toolTip.getElement().isOrHasChild(Element.as(target))){
							  toolTip.hide();
						  }
					  }
					
				}
			});
			oerPanel.add(oer);
			oerPanel.setVisible(true);
		}/*else{
			collectionLinkLbl.addStyleName(style.active());
			resourceLinkLbl.removeStyleName(style.active());
		}*/
		this.setVisible(true);
	}
	

	/**
	 * Get search filter such as grade, subject, category, etc..
	 * @return search filter as Map value
	 */
	protected Map<String, String> getFilter() {
		Map<String, String> filterMap = new HashMap<String, String>();
		
		String selectedAccessMode = getSelectedFilter(accessModePanel);
		if (!selectedAccessMode.isEmpty()) {
			filterMap.put(IsSearchView.ACCESS_MODE_FLT, selectedAccessMode);
		}
		
		String selectedGrade = getSelectedFilter(gradePanelUc);
		if (!selectedGrade.isEmpty()) {
			filterMap.put(IsSearchView.GRADE_FLT, selectedGrade);
		}
	//	if(resourceSearch){
			String category = getSelectedFilter(categoryPanelUc);
			if (!category.isEmpty()) {
				filterMap.put(IsSearchView.CATEGORY_FLT, category);
			} else {
				filterMap.put(IsSearchView.CATEGORY_FLT, ALL);
			}
	//	}else{
		//	String category = getSelectedRadioButton(categoryPanelUc,COMMA_SEPARATOR);
	//		if (!category.isEmpty()) {
	//			filterMap.put(IsSearchView.CATEGORY_FLT, category);
	//		} else {
	//			filterMap.put(IsSearchView.CATEGORY_FLT, "all");
	//		}
	//	}
		String selectedSubject = getSelectedFilter(subjectPanelUc, "~~");
		if (!selectedSubject.isEmpty()) {
			filterMap.put(IsSearchView.SUBJECT_FLT, selectedSubject);
		}
		if (resourceSearch) {
			String sourceSgsts = getSuggestions(sourceContainerFloPanel);
			if (!sourceSgsts.isEmpty()) {
				filterMap.put(IsSearchView.PUBLISHER_FLT, sourceSgsts);
			}
			String aggregatorSgsts = getSuggestions(aggregatorContainerFloPanel);
			if (!aggregatorSgsts.isEmpty()) {
				filterMap.put(IsSearchView.AGGREGATOR_FLT, aggregatorSgsts);
			}
		} else {
			String authorSgsts = getSuggestions(authorContainerFloPanel);
			if (!authorSgsts.isEmpty()) {
				filterMap.put(IsSearchView.OWNER_FLT, authorSgsts);
			}
		}
		String standardSgsts = getSuggestions(standardContainerFloPanel);
		if (!standardSgsts.isEmpty()) {
			filterMap.put(IsSearchView.STANDARD_FLT, standardSgsts);
		}
		if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)){
			if (chkNotFriendly !=null &&  chkNotFriendly.getValue()){
//				if (chkNotFriendly.getText().equalsIgnoreCase("not_ipad_friendly")){
					filterMap.put(IsSearchView.MEDIATYPE_FLT, "not_ipad_friendly");
//				}
			}
				if(chkOER!=null && chkOER.getValue()){
					filterMap.put(IsSearchView.OER_FLT, "1");
				}
			
			}else{
			filterMap.remove(IsSearchView.MEDIATYPE_FLT);
			filterMap.remove(IsSearchView.OER_FLT);
		}
		
		
		return filterMap;
	}

	/**
	 * Suggest added filters
	 * @param flowPanel instance of {@link FlowPanel} which has all filter value as widget
	 * @return filter suggestions as string
	 */
	public String getSuggestions(FlowPanel flowPanel) {
		String suggestions = "";
		Iterator<Widget> widgets = flowPanel.iterator();
		while (widgets.hasNext()) {
			Widget widget = widgets.next();
			if (widget instanceof FilterLabelVc) {
				if (!suggestions.isEmpty()) {
					suggestions += COMMA_SEPARATOR;
				}
				suggestions += ((FilterLabelVc) widget).getSourceText();
			} else if (widget instanceof DownToolTipWidgetUc) {
				if (!suggestions.isEmpty()) {
					suggestions += COMMA_SEPARATOR;
				}
				suggestions += ((FilterLabelVc) ((DownToolTipWidgetUc) widget).getWidget()).getSourceText();
			}
		}
		return suggestions;
	}

	/**
	 * Get added suggestion filters
	 * @param flowPanel instance of {@link FlowPanel} which has all filter value as widget
	 * @return filter suggestions as string
	 */
	public List<String> getSuggestionsAsList(FlowPanel flowPanel) {
		List<String> suggestions = new ArrayList<String>();
		Iterator<Widget> widgets = flowPanel.iterator();
		while (widgets.hasNext()) {
			Widget widget = widgets.next();
			if (widget instanceof FilterLabelVc) {
				suggestions.add(((FilterLabelVc) widget).getSourceText());
			} else if (widget instanceof DownToolTipWidgetUc) {
				suggestions.add(((FilterLabelVc) ((DownToolTipWidgetUc) widget).getWidget()).getSourceText());
			}
		}
		return suggestions;
	}
	/**
	 * Get added suggestion filters
	 * @param flowPanel instance of {@link FlowPanel} which has all filter value as widget
	 * @return filter suggestions as string
	 */
	public List<String> getAggregatorAsList(FlowPanel flowPanel) {
		List<String> aggregations = new ArrayList<String>();
		Iterator<Widget> widgets = flowPanel.iterator();
		while (widgets.hasNext()) {
			Widget widget = widgets.next();
			if (widget instanceof FilterLabelVc) {
				aggregations.add(((FilterLabelVc) widget).getSourceText());
			} else if (widget instanceof DownToolTipWidgetUc) {
				aggregations.add(((FilterLabelVc) ((DownToolTipWidgetUc) widget).getWidget()).getSourceText());
			}
		}
		return aggregations;
	}
	/**
	 * @param categoryPanelUc2 instance of {@link DisclosurePanelUc}
	 * @return selected filterDisclosurePanell name
	 */
	private String getSelectedFilter(HTMLPanel categoryPanelUc2) {
		return getSelectedFilter(categoryPanelUc2, COMMA_SEPARATOR);
	}

	/**
	 * Get filters for search
	 * @param categoryPanelUc2 instance of {@link DisclosurePanelUc} which has filters widget
	 * @param separator concatenation of the filters with separator
	 * @return concatenation of selected filters
	 */
	private String getSelectedFilter(HTMLPanel categoryPanelUc2, String separator) {
		String selectedFilter = "";
		for(int i =0;i<categoryPanelUc2.getWidgetCount();i++){
			Widget filterWidget = categoryPanelUc2.getWidget(i);
			if (filterWidget instanceof CheckBox) {
				CheckBox filterCheckBox = (CheckBox) filterWidget;
				if (filterCheckBox != null && filterCheckBox.getValue()) {
					if (!selectedFilter.isEmpty()) {
						selectedFilter += separator;
					}
					selectedFilter += filterCheckBox.getName();
					MixpanelUtil.mixpanelEvent("search_"+selectedFilter+"_filter_selected");
				}
			}
		}
		
			/*for (Widget filterWidget : categoryPanelUc2.getWidget(0)) {
				if (filterWidget instanceof CheckBox) {
					CheckBox filterCheckBox = (CheckBox) filterWidget;
					if (filterCheckBox != null && filterCheckBox.getValue()) {
						if (!selectedFilter.isEmpty()) {
							selectedFilter += separator;
						}
						selectedFilter += filterCheckBox.getName();
						MixpanelUtil.mixpanelEvent("search_"+selectedFilter+"_filter_selected");
					}
				}
			}*/
	
		return selectedFilter;
	}
	
	/*private String getSelectedRadioButton(DisclosurePanelUc filterDisclosurePanell, String separator){
		String selectedFilter = "";
		for (Widget filterWidget : filterDisclosurePanell.getContent()) {
			if (filterWidget instanceof QuestionTypeFilter) {
				QuestionTypeFilter filterRadiobuttonBox = (QuestionTypeFilter) filterWidget;
				if (filterRadiobuttonBox.hiddenRadioButton != null && filterRadiobuttonBox.hiddenRadioButton.getValue()) {
					if (!selectedFilter.isEmpty()) {
						selectedFilter += separator;
					}
					selectedFilter += filterRadiobuttonBox.hiddenRadioButton.getText();
				}
			}
		}
		return selectedFilter;
		
	}*/

	/**
	 * Set search filters
	 * @param filter as Map to set filter values 
	 */
	public void setFilter(Map<String, String> filter) {
		String grade = filter.get(IsSearchView.GRADE_FLT);
		
		String notFriendly = filter.get(IsSearchView.MEDIATYPE_FLT);
		
		String oer = filter.get(IsSearchView.OER_FLT);
		
		String categories = filter.get(IsSearchView.CATEGORY_FLT);
		
		String aggregator = filter.get(IsSearchView.AGGREGATOR_FLT);
		
		String authors = filter.get(IsSearchView.OWNER_FLT);
		
		String subjects = filter.get(IsSearchView.SUBJECT_FLT);
		
		String standards = filter.get(IsSearchView.STANDARD_FLT);
		
		if(categories==null){
			clearAllFields();
		}
		setSelectedFilter(categoryPanelUc, categories);
		setSelectedFilter(subjectPanelUc, subjects, "~~");
		setSelectedFilter(gradePanelUc, grade);
		standardSgstBox.setText("");
		standardSgstBox.getElement().setAttribute("alt","");
		standardSgstBox.getElement().setAttribute("title","");
		
		sourceSgstBox.setText("");
		sourceSgstBox.getElement().setAttribute("alt","");
		sourceSgstBox.getElement().setAttribute("title","");
		
		authorTxtBox.setText("");
		authorTxtBox.getElement().setAttribute("alt","");
		authorTxtBox.getElement().setAttribute("title","");
		
		aggregatorSgstBox.setText("");
		aggregatorSgstBox.getElement().setAttribute("alt","");
		aggregatorSgstBox.getElement().setAttribute("title","");
		
		if (standards != null) {
			setFilterSuggestionData(standardContainerFloPanel, standards.split(COMMA_SEPARATOR), true);
		}
		else
		{
			standardContainerFloPanel.clear();
		}
		if (resourceSearch) {
			String sources = filter.get(IsSearchView.PUBLISHER_FLT);
			if (sources != null) {
				setFilterSuggestionData(sourceContainerFloPanel, sources.split(COMMA_SEPARATOR), false);
			}
			else{
				sourceContainerFloPanel.clear();	
			}
			
			if (aggregator != null) {
					setFilterSuggestionData(aggregatorContainerFloPanel, aggregator.split(COMMA_SEPARATOR), false);
				}
			else
			{
				aggregatorContainerFloPanel.clear();
			}
		}else{
			if (authors != null) {
				setFilterSuggestionData(authorContainerFloPanel, authors.split(COMMA_SEPARATOR), false);
			}
			else
			{
				authorContainerFloPanel.clear();
			}
		}
		if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)){
			if (notFriendly!=null){
				try{
					chkNotFriendly.setValue(true);
				}catch(Exception e){
					
				}
			}else{
				try{
					chkNotFriendly.setValue(false);
				}catch(Exception e){
					
				}
			}
		if(oer!=null)
			{
				try{
				chkOER.setValue(true);
				}catch(Exception e){}
			}
			else{
				try{
				chkOER.setValue(false);
				}catch(Exception e){}
			}
		
		}
		if(grade == null){
			clearFilter(gradePanelUc);
		}
		if(subjects == null){
			clearFilter(subjectPanelUc);
		}
	}

	private void setFilterSuggestionData(FlowPanel flowPanel, String[] filters, boolean addToolTip) {

		Iterator<Widget> widgets = flowPanel.iterator();
		while (widgets.hasNext()) {
			Widget filterWidget = widgets.next();
			boolean exist = false;
			for (String filter : filters) {
				if (filterWidget instanceof FilterLabelVc && filter.equals(((FilterLabelVc) filterWidget).getSourceText())) {
					exist = true;
				} else if (filterWidget instanceof DownToolTipWidgetUc && filter.equals(((FilterLabelVc) ((DownToolTipWidgetUc) filterWidget).getWidget()).getSourceText())) {
					exist = true;
				}
				if (exist) {
					break;
				}
			}
			if (!exist) {
				filterWidget.removeFromParent();
			}
		}
		widgets = flowPanel.iterator();
		String filterCodes = "";
		for (String filter : filters) {
			boolean exist = false;
			while (widgets.hasNext()) {
				Widget filterWidget = widgets.next();
				if (filterWidget instanceof FilterLabelVc && filter.equals(((FilterLabelVc) filterWidget).getSourceText())) {
					exist = true;
				} else if (filterWidget instanceof DownToolTipWidgetUc && filter.equals(((FilterLabelVc) ((DownToolTipWidgetUc) filterWidget).getWidget()).getSourceText())) {
					exist = true;
				}
				if (exist) {
					break;
				}
			}
			if (!exist) {
				if (addToolTip) {
					flowPanel.add(new DownToolTipWidgetUc(new FilterLabelVc(filter) {
						
						@Override
						public void onCloseLabelClick(ClickEvent event) {
							getParent().removeFromParent();
							AppClientFactory.fireEvent(new GetSearchKeyWordEvent());
						}
						
					}, i18n.GL1466()));
					filterCodes += COMMA_SEPARATOR + filter;
				} else {
					flowPanel.add(new FilterLabelVc(filter));
				}
			}
		}

		if (filterCodes.length() > 1) {
			standardsInfoSearchDo.setQuery(filterCodes);
			AppClientFactory.fireEvent(new StandardsSuggestionInfoEvent(standardsInfoSearchDo));
		}
	}

	/**
	 * Call setSelectedFilter method
	 * @param filterFlowPanel instance of {@link DisclosurePanelUc}
	 * @param checkedValues filters name 
	 */
	private void setSelectedFilter(HTMLPanel filterHtmlPanel, String checkedValues) {
		setSelectedFilter(filterHtmlPanel, checkedValues, COMMA_SEPARATOR);
	}

	/**
	 * Set filter value for search with separator
	 * @param filterFlowPanel instance of {@link DisclosurePanelUc} which has filter values
	 * @param checkedValues selected filter value
	 * @param separator concatenation of the filter value by separator 
	 */
	private void setSelectedFilter(HTMLPanel filterHtmlPanel, String checkedValues, String separator) {
		List<String> items = null;
		if (checkedValues != null) {
			items = Arrays.asList(checkedValues.split("\\s*" + separator + "\\s*"));
		}
		
		if (items != null) {
			//if(resourceSearch){
			for(int i=0;i<filterHtmlPanel.getWidgetCount();i++){
				Widget filterWidget = filterHtmlPanel.getWidget(i);
				if (filterWidget instanceof CheckBox) {
					CheckBox filterCheckBox = (CheckBox) filterWidget;
					filterCheckBox.setValue(false);
					for (String item : items) {
						if ((filterCheckBox.getName().equals(item))) {	
							filterCheckBox.setValue(true);
						}
					}
				}
			}
			/**
			 * Removed this logic as per the new requrement in 6.5 sprint
			 */
				/*for (Widget filterWidget : filterFlowPanel.getContent()) {
					if (filterWidget instanceof CheckBox) {
						CheckBox filterCheckBox = (CheckBox) filterWidget;
						filterCheckBox.setValue(false);
						for (String item : items) {
							if ((filterCheckBox.getName().equals(item))) {	
								filterCheckBox.setValue(true);
							}
						}
					}
				}*/
			//}
/*			else{
				boolean isRadioButtonSelected=false;
				for (Widget filterWidget : filterFlowPanel.getContent()) {
					if (filterWidget instanceof QuestionTypeFilter) {
						QuestionTypeFilter questionTypeFilter = (QuestionTypeFilter) filterWidget;
						//filterCheckBox.setValue(false);
						questionTypeFilter.radioButton.setStyleName(SearchMoreInfoVcCBundle.INSTANCE.css().questionRadioButton());
						questionTypeFilter.hiddenRadioButton.setValue(false);
						for (String item : items) {
							if ((questionTypeFilter.hiddenRadioButton.getText().equals(item))) {
								questionTypeFilter.hiddenRadioButton.setValue(true);
								isRadioButtonSelected=true;
								questionTypeFilter.radioButton.setStyleName(SearchMoreInfoVcCBundle.INSTANCE.css().questionRadioButtonSelected());
							}
						}
					}
				}
//				if(!isRadioButtonSelected){
//					QuestionTypeFilter questionTypeFilter = (QuestionTypeFilter)filterFlowPanel.getContent().getWidget(2);
//					questionTypeFilter.hiddenRadioButton.setValue(true);
//					questionTypeFilter.radioButton.setStyleName(SearchMoreInfoVcCBundle.INSTANCE.css().questionRadioButtonSelected());
//				}
				
			}*/
			
		}
		
	}

	/**
	 * Clear all selected filter values
	 * @param gradePanelUc instance {@link DisclosurePanelUc} which has selected filter values
	 */
	public void clearFilter(HTMLPanel gradePanelUc) {
		
	//	if(resourceSearch){
		for(int i=0;i<gradePanelUc.getWidgetCount();i++){
			Widget filterWidget = gradePanelUc.getWidget(i);
			if (filterWidget instanceof CheckBox) {
				((CheckBox) filterWidget).setValue(false);
			}
		}
		/**
		 * Removed this logic as per the new requrement in 6.5 sprint
		 */
			/*for (Widget filterWidget : gradePanelUc.getContent()) {
				if (filterWidget instanceof CheckBox) {
					((CheckBox) filterWidget).setValue(false);
				}
			}*/
/*		}else{
			for (Widget filterWidget : filterFlowPanel.getContent()) {
				if (filterWidget instanceof QuestionTypeFilter) {
					QuestionTypeFilter questionTypeFilter = (QuestionTypeFilter) filterWidget;
					questionTypeFilter.radioButton.setStyleName(SearchMoreInfoVcCBundle.INSTANCE.css().questionRadioButton());
					questionTypeFilter.hiddenRadioButton.setValue(false);
					if(questionTypeFilter.hiddenRadioButton.getText().equalsIgnoreCase("all")){
						questionTypeFilter.hiddenRadioButton.setValue(true);
						questionTypeFilter.radioButton.setStyleName(SearchMoreInfoVcCBundle.INSTANCE.css().questionRadioButtonSelected());
					}

				}
				if (filterWidget instanceof CheckBox) {
					((CheckBox) filterWidget).setValue(false);
				}
			}
//			QuestionTypeFilter questionTypeFilter = (QuestionTypeFilter)filterFlowPanel.getContent().getWidget(2);
//			questionTypeFilter.hiddenRadioButton.setValue(true);
//			questionTypeFilter.radioButton.setStyleName(SearchMoreInfoVcCBundle.INSTANCE.css().questionRadioButtonSelected());
		}*/
	}

	/**
	 * Clear all specified widget values , specified text box and refresh event  
	 * @param clickEvent instance of {@link ClickEvent}
	 */
	@UiHandler("clearAll")
	public void onClearFilter(ClickEvent clickEvent) {
		clearFilter(categoryPanelUc);
		clearFilter(gradePanelUc);
		clearFilter(subjectPanelUc);
		clearFilter(accessModePanel);
		standardSgstBox.setText("");
		standardSgstBox.getElement().setAttribute("alt","");
		standardSgstBox.getElement().setAttribute("title","");
		
		sourceSgstBox.setText("");
		sourceSgstBox.getElement().setAttribute("alt","");
		sourceSgstBox.getElement().setAttribute("title","");
		
		sourceContainerFloPanel.clear();
		standardContainerFloPanel.clear();
		authorContainerFloPanel.clear();
		standardCodesMap.clear();
		aggregatorContainerFloPanel.clear();
		if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH)){
			chkNotFriendly.setValue(false);
			chkOER.setValue(false);
		}
		AppClientFactory.fireEvent(new GetSearchKeyWordEvent());
	}
	
	public void clearAllFields(){
		clearFilter(categoryPanelUc);
		clearFilter(gradePanelUc);
		clearFilter(subjectPanelUc);
		clearFilter(accessModePanel);
		standardSgstBox.setText("");
		sourceSgstBox.setText("");
		sourceSgstBox.getElement().setAttribute("alt","");
		sourceSgstBox.getElement().setAttribute("title","");
		
		sourceContainerFloPanel.clear();
		standardContainerFloPanel.clear();
		authorContainerFloPanel.clear();
		aggregatorContainerFloPanel.clear();
		standardCodesMap.clear();
		AppClientFactory.fireEvent(new GetSearchKeyWordEvent());
	}

	@Override
	public void onSelection(SelectionEvent<SuggestOracle.Suggestion> event) {
	if (event.getSource().equals(sourceSgstBox)) {
			String text = sourceSgstBox.getValue();
			if (text.equals(NO_MATCH_FOUND)) {
				new FadeInAndOut(sourcesNotFoundLbl.getElement(), 5000, 5000);
			} else {
				sourceContainerFloPanel.add(new FilterLabelVc(text,true));
				AppClientFactory.fireEvent(new GetSearchKeyWordEvent());
			}
			sourceSgstBox.setText("");
			sourceSgstBox.getElement().setAttribute("alt","");
			sourceSgstBox.getElement().setAttribute("title","");
			sourceSuggestOracle.clear();
			
		} else if(event.getSource().equals(aggregatorSgstBox)){
			String text = aggregatorSgstBox.getValue();
			if (text.equals(NO_MATCH_FOUND)) {
				new FadeInAndOut(aggregatorNotFoundLbl.getElement(), 5000, 5000);
			} else {
				aggregatorContainerFloPanel.add(new FilterLabelVc(text,true));
				AppClientFactory.fireEvent(new GetSearchKeyWordEvent());
			}
			aggregatorSgstBox.setText("");
			aggregatorSgstBox.getElement().setAttribute("alt","");
			aggregatorSgstBox.getElement().setAttribute("title","");
			aggregatorSuggestOracle.clear();
		}
			
			else {
			String text = standardSgstBox.getValue();
			if (text.equals(NO_MATCH_FOUND)) {
				new FadeInAndOut(standardsNotFoundLbl.getElement(), 5000, 5000);
			} else {
				addStandardFilter(text);
				AppClientFactory.fireEvent(new GetSearchKeyWordEvent());
			}
			standardSgstBox.setText("");
			standardSgstBox.getElement().setAttribute("alt","");
			standardSgstBox.getElement().setAttribute("title","");
			standardSuggestOracle.clear();
		}
	}

	public void addStandardFilter(String code) {		
		standardContainerFloPanel.add(new DownToolTipWidgetUc(new FilterLabelVc(code), standardCodesMap.get(code)));
	}

	/**
	 * Set suggestion standards
	 * @param standardSearchDo instance of {@link SearchDo} type of CodeDo 
	 */
	public void setStandardSuggestions(SearchDo<CodeDo> standardSearchDo) {
		standardSuggestOracle.clear();
		this.standardSearchDo = standardSearchDo;
		if (this.standardSearchDo.getSearchResults() != null) {
			List<String> sources = getSuggestionsAsList(standardContainerFloPanel);
			for (CodeDo code : standardSearchDo.getSearchResults()) {
				if (!sources.contains(code.getCode())) {
					standardSuggestOracle.add(code.getCode());
				}
				standardCodesMap.put(code.getCode(), code.getLabel());
			}
		}
		if (standardSuggestOracle.isEmpty()) {
			standardSuggestOracle.add(NO_MATCH_FOUND);
		}
		standardSgstBox.showSuggestionList();
	}

	public void setSourceSuggestionsInfo(SearchDo<CodeDo> searchDo) {
		Iterator<Widget> widgets = standardContainerFloPanel.iterator();
		while (widgets.hasNext()) {
			DownToolTipWidgetUc downToolTipWidgetUc = (DownToolTipWidgetUc) widgets.next();
			FilterLabelVc filterWidget = (FilterLabelVc) downToolTipWidgetUc.getWidget();
			HTML html = (HTML)downToolTipWidgetUc.getToolTipWidget();
			for (CodeDo code : searchDo.getSearchResults()) {
				if (filterWidget.getSourceText().equals(code.getCode())) {
					html.setHTML(code.getLabel());
					break;
				}
			}
		}
	}

	/**
	 * @param sourceSearchDo instance of {@link SearchDo}
	 */
	public void setSourceSuggestions(SearchDo<String> sourceSearchDo) {
		sourceSuggestOracle.clear();
		this.sourceSearchDo = sourceSearchDo;
		if (this.sourceSearchDo.getSearchResults() != null) {
			this.sourceSearchDo.getSearchResults().removeAll(getSuggestionsAsList(sourceContainerFloPanel));
		}
		if (this.sourceSearchDo.getSearchResults() != null && this.sourceSearchDo.getSearchResults().size() > 0) {
			sourceSuggestOracle.setAll(sourceSearchDo.getSearchResults());
		} else {
			sourceSuggestOracle.add(NO_MATCH_FOUND);
		}
		sourceSgstBox.showSuggestionList();
	}
	public void setAggregatorSuggestions(SearchDo<String> aggregatorSearchDo) {
		aggregatorSuggestOracle.clear();
		this.aggregatorSearchDo=aggregatorSearchDo;
		if(this.aggregatorSearchDo.getSearchResults() != null){
			this.aggregatorSearchDo.getSearchResults().removeAll(getAggregatorAsList(aggregatorContainerFloPanel));
		}
		if (this.aggregatorSearchDo.getSearchResults() != null && this.aggregatorSearchDo.getSearchResults().size() > 0) {
			aggregatorSuggestOracle.setAll(aggregatorSearchDo.getSearchResults());
		} else {
			aggregatorSuggestOracle.add(NO_MATCH_FOUND);
		}
		aggregatorSgstBox.showSuggestionList();
	}
	public void getUserStandardPrefCodeId()
	{
		/**
		 * This RPC is to get the User profile Details(codeId value)
		 */
		AppClientFactory.getInjector().getUserService().getUserProfileV2Details(AppClientFactory.getGooruUid(),USER_META_ACTIVE_FLAG,new SimpleAsyncCallback<ProfileDo>() {

							@Override
							public void onSuccess(ProfileDo profileObj) {
								if(profileObj.getUser().getMeta().getTaxonomyPreference().getCode()!=null){
									if(profileObj.getUser().getMeta().getTaxonomyPreference().getCode().size()==0){
										standardLbl.setVisible(false);
										standardPanelUc.setVisible(false);
									}else
									{
										standardLbl.setVisible(true);
										standardPanelUc.setVisible(true);
										standardPreflist=new ArrayList<String>();
										for (String code : profileObj.getUser().getMeta().getTaxonomyPreference().getCode()) {
											standardPreflist.add(code);
											standardPreflist.add(code.substring(0, 2));
										 }
									}
								}else{
									standardLbl.setVisible(false);
									standardPanelUc.setVisible(false);
								}
							}

						});
	}
	public void getStandardVisiblity()
	{
		standardLbl.setVisible(true);
		standardPanelUc.setVisible(true);
	}
	
	public FlowPanel getMainContainer(){
		return myCollectionSearch;
	}
}
