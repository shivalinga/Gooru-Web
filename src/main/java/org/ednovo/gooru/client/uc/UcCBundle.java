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
/**
 * 
 */
package org.ednovo.gooru.client.uc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * @author Search Team
 * 
 */
public interface UcCBundle extends ClientBundle {

	static final UcCBundle INSTANCE = GWT.create(UcCBundle.class);
	
	/**
	 * UcCss.
	 */
	public interface UcCss extends CssResource {
		
		// Folders //
		
		String editableFieldForFolders();
		
		// Folders End //
		
		String closeLabel();
		
		String closeLabelText();
		
		String closeLabelRemove();
		
		String disclosurePanelUc();

		String disclosurePanelUcHeader();
		
		String disclosurePanelUcHeaderImgOpen();
		
		String disclosurePanelUcHeaderImgClose();

		String disclosurePanelUcHeaderText();
		
		String gooruBlueButton();
		
		String gwtErrorMessage();
		
		String gwtErrorInfo();
		
		String gooruPanel();
		
		String gooruPanelIn();
		
		String labelStyle();
		
		String paginationUcSelected();
		
		String paginationUc(); 
		
		String separator();
		String	separatorSuggested();
		
		/*CollectionImageUc*/
		
		String collectionThumbnail();
		
		String collectionThumbnail1();
		
		String collectionNotify();
		
		String collectionThumbnails();
		
		String collectionHover();
		
		String playIcon();
		
		
		/*DownToolTipUc */
		
		String popup();
		
		String header();
		
		String content();
		
		String toolTipPopUp();
		
		/*LeftToolTipUc*/
		
		String lpopup();
		
		String lheader();
		
		String lcontent();
		
		/*ResourceImageUc*/
		
		String resourceThumbnail();
		
		String resourceThumbnails();
		
		String resourceName();
		
		String resourceNameNew();
		
		String resourceHover();
		
		String rplayIcon();
		
		/*StandardSgItemVc*/
	    
		String standards();
		
		String descStandards();
		
		String descLicence();
		
		/*EditableLabelUc*/
		
		String editableField();
		
		String editableFieldforClspage();
		/*ErrorPopupVc*/
		
		String errorPopUp();
		
		String captionTitle();
		
		String headerText();
		
		String inlineBorder();
		
		String firstTextBox();
		
		String secondTextBox();
		
		String QCFText();
		
		String dragMsg();
		
		String QuestMsg();
		
		String okButton();
		
		String cancelButton();
		
		String OkCancelDiv();
		
		/* Glass Panel */
	    String labelGlassPanelText();
	    
		String userDefaultSelectDisable();
		
		String userMoveSelectDisable();
		
		/* Resource type images*/
		
		String videoSmall();
		
		String interactiveSmall();
		
		String websiteSmall();
		
		String examSmall();
		
		String textbookSmall();
		
		String textSmall();
		
		String handoutSmall();
		
		String lessonSmall();
		
		String slideSmall();
		
		String questionSmall();

		String imageSmall();
		
		String audioSmall();
		
		String otherSmall();
		
		String webpageSmall();

		
		String videoSmallNew();
		
		String interactiveSmallNew();
		
		String websiteSmallNew();
		
		String examSmallNew();
		
		String textbookSmallNew();
		
		String textSmallNew();
		
		String handoutSmallNew();
		
		String lessonSmallNew();
		
		String slideSmallNew();
		
		String questionSmallNew();

		String imageSmallNew();
		
		String audioSmallNew();
		
		String otherSmallNew();
		
		String webpageSmallNew();

		String imageUploadPopup();
		
		String textBoxWithPlaceholderText();
		
		String textBoxWithPlaceholder();
		
		String increaseZindex();
		
		/* DatePickerUc Style */
		String monthYearContainer();
		
		String yearListStyle();
		
		String monthListStyle();
		
		String datePickerContainer();
		
		String todayButton();
		
		String doneButton();
		
		String dateButtonContainer();
		
		/* DateBoxUc Style */
		String gooruDateBox();
		
		String gooruDateText();
		
		String gooruCalendarIcon();
		
		String gooruDatePickerBox();
		
		/* ParentDatePicker Style */
		String parentDatePickerContainer();
		
		String parentDateBox();
		
		String parentDateText();
		
		String searchStandard();
		
		/* Glass Panel */
		String glassPanelWithLoading();
		
		String imageLoadingPanel();
		
		String imagePanel();
		
		/* Arrow Uc */
		String previousArrow();
		
		String nextArrow();
		
		/* Navigation Uc */
		
		String navigationLabel();		
		
		String removeToolTipConatainer();
		
		String removeTollTipTopImage();
		
		String removeToolTipContentContainer();
		
		String hoverPopupTextContainer();
		
		String hoverPopupArrow();
		
		String hoverPopupContainer();
		
		String comingSoonLbl();
		
		String hoverCollPopupContainerPos();
		
		String hoverStasPopupContainerPos();
		
		String gooruDateBoxAssignment();
		
		/* UserProfileUc */
		String profilePopup(); 
		
		String profilePopupArrow(); 
		
		String profilePopupContainer(); 
		
		String profilePopupInnerContaienr(); 
		
		String profilePopupInnerImage(); 
		
		String profilePopupInnerSeparator(); 
		
		String profilePopupInnerTitle(); 
		
		String profilePopupInnerHints(); 
		
		String profilePopupInnerDetails();
		
		
		/* EmailShareUc */
		
		String classPageEmailPopupContainer();
		
		String classPageEmailHeaderContainer();
		
		String classPageEmailHeaderContent();
		
		String classPageEmailHeaderDesc();
		
		String classPageEmaiInputContainer();
		
		String classPageEmaiInputText();
		
		String classPageEmailInputControl();
		
		String classPageEmailInputControlInnerDiv();
		
		String classPageEmailCheckBoxContainer();
		
		String classPageEmailCheckBoxBg();
		
		String classPageEmailAlign();

		String classPageEmailButtonContainer();
		
		String classPageEmailButtonBg();		
		
		String classPageEmailButtonTitle();		
		
		String classPageEmailNormalText();		
		
		String classPageEmailButtonCancel();		
		
		String classPageEmailMainContainer();		
		
		String classPageEmailBlueText();		
		
		String classPageEmailDescText();		
		
		String classPageEmailButtonOuterDiv();		
		
		String classPageEmailOktext();	
		
		String classPageEmailInputNormalControl();
		
		String clear();
		
		String classPageEmailCheckBoxBgHoverSprite();
		
		String classPageEmailCheckBoxBgHover();
		
		String emailShareErrorLabel();
		
		String editableSelfField();
		String displayNone();
		
		String shareEmailButtonContainer();
		String emailPrimary();
		String emailSecondary();
		
		String emailSendButton();
		

		// PPP //
		
		String collectionPPPThumbnail();
		String publicProfileTextarea();
		String editableFieldProfile();
		String publicPPGwtErrorMessage();
		String editImage();
		String errorValidation();
		 
		//Tooltip Arrows
		String toolTip();

		String arrowShadow();

		String arrowBorder();

		String arrow();
		
		String disableGooruButon();
		
		String profilePageEditDecsription();
		
		String profilePageTextAreaDecsription();
		/* Standards Popup*/
		String standardsHeader();

		String standardsTitle();

		String standardsCloseMarker();

		String standardsSprite();

		String standardsCloseMark();
		
		String standardsContent();
		
		String container();
		
		String divContainer();
		
		String standardsDesc();
		
		String editableTextField();
		String errorValidationClassPage();
		String settingpageEditTextBox();
		String editableFieldSetting();
		String errorValidationSetting();
		String editableUserSettingField();
		String SettingEditEmail();
		String SettingEditLastName();
		String closeLabelRemoveInSetting();
		
		String iconPosition();
		String errorValidationEmailForSetting();
		String editableFieldlastNameSetting();
		
		String txtAlignLeft();
		
		String privacyAlignStyle();
		
		String paddingClear();
	}

	@Source("Uc.css")
	UcCss css();

}
