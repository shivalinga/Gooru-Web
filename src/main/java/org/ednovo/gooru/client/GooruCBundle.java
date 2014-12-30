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
package org.ednovo.gooru.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
/**
 * 
 * @fileName : GooruCBundle.java
 *
 * @description : 
 *
 *
 * @version : 1.0
 *
 * @date: 06-Dec-2013
 *
 * @Author Gooru Team
 *
 * @Reviewer:
 */
public interface GooruCBundle extends ClientBundle {
	
	static final GooruCBundle INSTANCE = GWT.create(GooruCBundle.class);
	
	public interface GooruCss extends CssResource{
		
		/*HeaderUc*/
		
		String goorulandingHeaderContainer();
		
		String logoDiv();
		
		String gooruLearningIcon();
		
		String editSearchInputBoxDiv();
		
		String editInputBox();
		
		String editSearchButton();
		
		String login();
		
		String hiMsg();
		
		String loggedInfo();
		
		String logoutDownArrow();
		
		String logoutPanel();
		
		String logoutPopup();
		
		/*FooterUc*/
		
		String goorulandingFooterContainer();
		
		String facebookImage();
		
		String twitterImage();
		
		String youtubeImage();
		
		String googleplusImage();
		
		//String pinterestImage();
		
		String homePageFooterMenu();
		
		String gooruCopyRight();
		
		String learnMoreIconsDiv();
		
		/* image upload  */
		String imageUploadPopup();
		
		String uploadActive();
		
		String uploadClose();
		
		String deactivated();
		
		String imageCropPopup();
		
		/*HeaderUc Contents*/
		
		String gooruClassicView();
		
		/*String classicViewLabel();*/
		
		String gooruGuideImg();
		
		String loggedInGooruGuideImg();
		
		String imageUrlError();
		
		String textboxUrlError();
		
		/********** Gooru Guide Info *********/
		
		String guideInfoText();
		
		String gooruGuideCont(); 
		
		String gooruGuideButton();
		
		String guideButton();
		
		String guideInfoArrow();
		
		String tryitoutpopup();
		
		/*Header Dots Css*/
		
		String teachUserNameTextWhite ();

		String teachUserNameTextWhiteActive();
		
		String teachUserNameTextGrayActive();
		
		String menuActive();
		
		String menu();
		
		String teachUserNameTextBlack();
		
		String loginUserNameContainer();
		
		String loggedUserNameText();
		
		String registerLink();
		
		String signUpButton();
		
		String profileImageActive();
		
		String profileImageContainer();
		
		String displayFlagPopup();
		
		String goorulandingFooterContainerNew();
		
		String discoverDropDown();
		
		String discoverDropDownBorder();
		
		String discoverTextStyle();
		String positionStyle();
		
	}
	@Source("Gooru.css")
	GooruCss css();
}
