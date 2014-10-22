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

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;

/**
 * @author Search Team
 *
 */
public interface HomeCBundle extends ClientBundle{
	
	static final HomeCBundle INSTANCE = GWT.create(HomeCBundle.class);
	
	@NotStrict
	@Source("Home.css")
	HomeCss css();
	
	@NotStrict
	@Source("res_homestyle.css")
	HomeCss getResponsiveStyle();
	
	@NotStrict
	@Source("res_home2style.css")
	HomeCss getResponsive1Style();
	
	@NotStrict
	@Source("res_home3style.css")
	HomeCss getResponsive2Style();

	@NotStrict
	@Source("res_home4style.css")
	HomeCss getResponsive3Style();

	
	public interface HomeCss extends CssResource{
		
		/*HomeView*/
		String searchSaveShare();
		
		String searchImageIcon();
		
		String homeSearchInputBoxColor();
		
		String homeSearchInputBox();
		
		String searchBarBanner();
		
		String homeSearchHeader();
		
		String searchBarMainContainer();
		
		String searchHeaderSubText();
		
		String searchInputBoxDiv();
		
		String searchInputBox();
		
		String homePageSearchButton();
		
		String advanceSearchLink();
		
		String homeInfoFeaturedContent();
		
		String searchInfoContainer();
		
		String searchInfoInnerContent();
		
		String searchInfoHeader();
		
		String searchInfoHeaderForSuppoters();
		
		String searchInfoIcon();
		
		String searchInfoSubHeader();
		
		String searchInfoColumnDivider();
		
		String searchInfoSubContent();
		
		String ourMissionImage();
		
		String searchInfoLinks();
		
		String searchInfoLink();
		
		String searchInfoLeftColumn();
		
		String discoverContent();
		
		String organizeContent();
		
		String customizeContent();
		
		String shareContent();		
		
		String organizeNumber();
		
		String customizeNumber();
		
		String shareNumber();
		
		String organizeImg();
		
		String customizeImg();
		
		String shareImg();
		
		String discoverTxt();
		
		String organizeTxt();
		
		String customizeTxt();
		
		String shareTxt();
		
		String logoutText();
		
		String logPanel();
		
		String goorulandingHeaderContainer();
		
		String gooruLearningIcon();
		
		String studyInfoButton();
		
		String howDoesItWork();
		
		/*How does it works styles*/
		String discoverNumber();
		
		String discoverImg();
		
		String discoverText();
		/**/
		
		/*SearchHomeFilterVc*/
		
		String filterSubHeader();
		
			
		String filterArrow();
		
		String filterContainer();
		
		String filterInnerContent();
		
		String filterHeader();
		
		String filterSection();
		
		String filterLabel();
		
		String filterSectionTextbox();
		
		String filterSectionTypes();
		
		String filterSuggestBox();
		
		String filterLeftSection ();
		
		String filterSelectionBox();
		
		/*AboutViewVc*/
		
		String aboutInfoContainer();
		
		String aboutInfoInnerContent();
		
		String aboutInfoColumnDivider();
		
		String aboutInfoAlignCenter();
		
		String aboutInfoColumnDividerLeft();
		
		String supporters();
		
		String alpha();
		
		String hewlett();
		
		String cisco();
		
		String howDoesItHeader();
		
		String supporterHeader();
		
/*		String pd();*/
		
		String onr();
		
		String ramShriram();
		
		String sce();
		
		String gates();
		
		String google();
		
		String pearson();
		
		String  nextGeneration();
		
		String  fenwick();
		
		/*FeaturedContentVc*/
		
		String featuredContentContainer();
		
		String featuredWrapperContainer();
		
		String featuredCollectionTitle();
		
		String featuredBaseContainer();
		
		String featuredLeftContainer();
		
		String featuredSlide();
		
		
		/*FeaturedSlideVc*/
		
		String featuredRightContainer();
		
		String featuredContainer();
		
		String featuredRightContainerContent();
		
		String featuredContentImageContainer();
		
		String featuredLeftContainerContent();
		
		String featuredThemeContentContainer();
		
		String featuredThemeContent();
		
		/*LoginPopUc*/
		
		String loginTop();
		
		String loginBorderPanel();
		
		String loginGooruLogoContainer();
		
		String loginGooruLabel();
		
		String loginGooruLabelSubText();
		
		String loginBlackText(); 
		
		String loginHyplink();
		
		String loginCkbox();
		
		String loginCancelButton();
		
		String loginBtn();
		
		String cancelBtn();
		
		String register();
		
		String registerLinkQ();
		
		String registerLink();
		
		
		/* forgot Password */
		
		String forgotEmailId();
		
		String forgotPwdFormContainer();
		
		String forgotPwdContentDiv();
		
		String forgotEmailDiv();
		
		String forgotEmailIdLabel();
		
		String forgotPwdFormButtonDiv();
		
		String forgotPwdFormCancelButton();
		
		String resetPwdTextError();
		
		
		/*** success Forgot Password***/
		
		String forgotPwdSuccess();
		
		String successPopupContainer();
		
		String okSuccessBtn();
		
		String successText();
		
		String successMsgText();
		
		String mailSupport();
		
		/************ Reset Password ************/
		
		String resetPwdPanel();
		
		String resetPwdTextContainer();
		
		String resetPwdLabel();
		
		String resetPwdText();
		
		String resetPwdContentDiv();
		
		/***************** Reset success password *****************/
		
		String resetPwdSuccessContainer();
		
		String okBlueBtn();
		
		String resetSuccessText();
		
		String resetPwdSuccessText();
		
		/********************* Reset Timeout View*******************/
		
		String resetTimeoutHeader();
		
		String resetTimeoutText();
		
		String resetFooterText();
		
		String forgotPwdLink();
		
		String linkTxt();
		
		/******************* Password Error Label *****************/
		
		String passwordErrorLabel();
		
		/***************** Log out popup **************/
		
		String removePopupContainer();
		
		String actionField();
		
		String removeText();
		
		String actionFieldOne();
		
		/*****  Search info *****/
		
		String landingSearchIconCont();
		
		String landingSearchIcon();
		
		String homePageFeaturedStartStudy();
		
		String studyFeaturedCollectionButton();
		
		String studyFeaturedCollectionArrow();
		
		/********************* Try it out  ***********************/
		
		String landingPageOuterDiv();
		
		String landingPageLearn();
		
		String landingPageCelebration();
		
		String tryItOut();
		
		String backToClassic();
		
		String tryItoutContainer();
		
		String classicGooru();
		
		String tryItOutGooru();
		
		String innerContainer();
		
		String celebrations();
		
		String classicContainer();
		
		String classicInnercontainer();
		
		String classictryItOut();
		
		String fadeblueButtonText();
		
		String fadeblueArrow();
		
		String blueButtonText();
		
		String blueArrow();
		
		String headerPopUpTitle();
		
		String closePopUp();
		
		String popUpTitleContext();
		
		String landingPageCelebrationText();
		
		String textBoxesField();
		
		String textBoxPlaceHolderWidth();
		
		String landingPagesliderArrow();
		
		String landingPageSliderArrowBackground();
		
		
		
		
		String landingPageSliderArrowSpan();
		
		String featuredBaseContainer1();
		String featuredSlide1();
		
		
		
		
		String landingHelpPopupContainer();
		String landingHelpPopupTitle();
		String landingPageHelpDotsContainer();
		String landingPageHelpDotsTotalContainer();
		String landingPageDotsTitle();
		String ladningPageHelpSearchButton();
		String landingDiscoverIconContainer();
		String landingDiscoverIcon();
		String landingPageDiscoverIconDetails();
		String landingPageHelpOrganizeImageContainer();
		String landingPageHelpOrganizeBigIconContainerTwo();
		String landingPageHelpOrganizeBigIconHolder();
		String landingPageHelpOrganizePlusIcon();
		String landingPageHelpOrganizeTopArrow();
		String landingPageHelpOrganizeDownArrow();
		String landingPageHelpDottedBoxContainer();
		String landingPageHelpDotsText();
		String landingPageHelpOrganizeBigIconContainer();
		String landingPageHelpOrganizeBigIconTotalContainer();
		String landingPageHelpOrganizeLessionTotlatContainer();
		String landingPageLessionTitle();
		String landingPageHelpTeachLessoinContainer();
		String landingPageHelpTeachLessoinNumber();
		String landingPageHelpTeachLessoinNumberActive();
		String landingPageHelpTeachLessoinHintActive();
		String landingPageHelpTeachLessoinHint();
		String landingPageHelpTeachLessoinTextActive();
		String landingPageHelpTeachLessoinText();
		String landingPageHelpDiscoverSearchBoxContainer();
		String landingPageHelpDiscoverSearchBox();
		
		String landingPageLessionTitleSmall();
		
		String askMoreQuestionForHelp();
		
		String logoutTextForLandingPageHelp();

		String waitPopupContainer();

		String confirmMessageContainer();

		String confirmMessage();

		String confirmMessageOk();

		String confirmMessageDelete();

		String confirmMessages();
		
		String helpPagePosition();
		
		String loginPopupGlassStyle();
		
		String playerAddToolTipGlassStyle();
		
		String userMenuBottomLine();
		
		/* ClasspageList VC CSS*/
		String classpageListContainer();
		
		String htmlPanelNoClasspageContainer();
		
		String newClasspageLink();
		
		String classpageListTitle();
		
		String classpageBottomLine();
		
		String loadingText();
		
		String htmlPanelClasspageList();
		
		String plusImg();
		
		String plus();
		
		String htmlPanelContentContainer();
		
		String gooruGuide();
		
		String noClasspageYet();
		
		String classpageTitleHeader();
		String gwtSuggestBoxPopup();
		
		String setAsCenterPopup();
		String okButtonmargin();
		String removingText();
		
//		New Landing Page Css
		String headerPanel();
		
		String signUpButton();

		String header();

		String subheader();
		String btnBlock();
		String secondary();
		String learnMoreContainer();

		String imageFieldContainer();
		String lblCollectionProgressDetails();
		String lblCollectionProgress();
		String summaryPopupContainer();
		String summaryText();
		String popupInner();
		String reportPopupHeader();
		String closeButton();
		String rightImageContainer();
		String leftImageContainer();

		String description();
		String setMarketingPopUpCenter();
		String banner();
		String shadow();
		String bannerInner();
		String caption();
		/*String h2();
		String p();*/
		String btn();

		@ClassName("navbar-default")
		String navbar_default();
		@ClassName("navbar-collapse")
		String navbar_collapse();
		@ClassName("navbar-nav")
		String navbar_nav();
		String small();
		@ClassName("container-fluid")
		String container_fluid();
		String communityblock();
		String getStart();
		String getInner();
		String getInnerRight();
		String getContentContainer();
		String getStartedIMG();
		String getCaption();
		String mission();
		String partext();
		
		String yourSupport();
		String imgBox();
		String margin();
		String gooruBadge();
		String goorubadge();
		String badgepadd();
		String social();
		String noMargin();
		String logo();
		String searchbox();
		String mainmenu();
		String sInner();
		String searchContainer();
		String signup();
		String dropdowDisplay();
		String districDropDown();
		String partnersDropdown();
		String rightArrow();
		String resourceRightsPopupContainer();
		String collectionToolTipLabel();
		String folderToolTipLabel();
		String tooltipContainer();
		String menuActive();
		String menu();
		String logoutPanel();
		String loggedInfo();
		String login();
		String logoutDownArrow();
		String searchButton();
		String dataTableResultHeading();
		String menuHeader();
		String blueBand();
		String dataTableResult();
		String rightBlock1();
		String blockContent();
		String districtLibraryContainer();
		String blockHeader();
		String last();
	}	
    
}
