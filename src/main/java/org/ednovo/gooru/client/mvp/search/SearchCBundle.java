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

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;

/**
 * @author Search Team
 *
 */
public interface SearchCBundle extends ClientBundle{
	
	static final SearchCBundle INSTANCE = GWT.create(SearchCBundle.class);
	
	@NotStrict
	@Source("search.css")
	SearchCss css();
	
	@NotStrict
	@Source("res_searchstyle.css")
	SearchCss getResponsiveStyle();
	
	@NotStrict
	@Source("res_search1style.css")
	SearchCss getResponsive1Style();
	
	@NotStrict
	@Source("res_search2style.css")
	SearchCss getResponsive2Style();

	@NotStrict
	@Source("res_search3style.css")
	SearchCss getResponsive3Style();
	
	@NotStrict
	@Source("res_search4style.css")
	SearchCss getResponsive4Style();

	@NotStrict
	@Source("res_search5style.css")
	SearchCss getResponsive5Style();
	
	@NotStrict
	@Source("res_search6style.css")
	SearchCss getResponsive6Style();
	
	@NotStrict
	@Source("res_search7style.css")
	SearchCss getResponsive7Style();
	
	@NotStrict
	@Source("res_search8style.css")
	SearchCss getResponsive8Style();
	
	public interface SearchCss extends CssResource{
		 String bodyWrapper();
		 String shareRight();
		 String searchMainContainer();
		 String searchResultMain();
		 String searchFilters();
		 String categoryFilterContainer();
		 String filterCheckBox();
		 String webpage();
		 String interactives();
		 String questions();
		 String images();
		 String texts();
		 String audio();
		 String searchResult();
		 String searchResult1();
		 String searchResultScroll();
		 String innerResult();
		 String searchInner();
		 @ClassName("panel-body")
		 String panel_body();
		 @ClassName("panel-footer-main") 
		 String panel_footer_main();
		 @ClassName("panel-footer")
		 String panel_footer();
		 String panelFooterMenuInfo();
		 String filtersInner();
		 String shareLink();
		 String active();
		 @ClassName("form-control1")
		 String form_control1();
		 String facebookbtn();
		 String twitterbtn();
		 String emailbtn();
		 String resourceThumbnail();
		 String resourceHeaderTextPanel();
		 String ratingWidgetPanel();
		 String searchStandardHint();
		 String playerBodyContainer();
		 String rateThis();
		 String infoBtnContainer();
		 String playerHeader();
		 String mainHeadContainer();
		 String flag();
		 String add();
		 String info();
		 String share();
		 String oerContainer();
		 String mobilefriendlyContainer();
		 String filterStandardContainer();
		 @ClassName("form-control")
		 String form_control();
		 String filteraggContainer();
		 String clearall();
		 String btn();
		 String closeBtn();
		 String standerdHeader();
		 String standerdTxt();
		 String standarBodyContainer();
		 String listGroup();
		 String scrollBar();
		 @ClassName("modal-dialog")
		 String modal_dialog();
		 String rightPanel();
		 String dropbox();
		 String folderLinks();
		 String resourceUpArrow();
		 String dragHere();
		 String secondaryIC();
		 String secondaryICactive();
		 String secondaryIC1();
		 String secondaryIC1active();
		 String PageNotify();
		 String searchbox();
		 @ClassName("navbar-form")
		 String navbar_form();
		 String panel();
		 String filterpublisherContainer();
		 @ClassName("col-md-4")
		 String col_md_4();
		 @ClassName("col-md-2")
		 String col_md_2();
		 String focus();
		 String filtersContainer();
		 String filtersButton();
	}	

}
