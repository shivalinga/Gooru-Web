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
package org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.item;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;

/**
 * @author Search Team
 * 
 */
public interface CollectionEditResourceCBundle extends ClientBundle {

	public static CollectionEditResourceCBundle INSTANCE = GWT.create(CollectionEditResourceCBundle.class);

	/**
	 * CollectionEditResourceCss.
	 */
	public interface CollectionEditResourceCss extends CssResource {

		String important();

		String copyPopupResourcesPanel();
		String copyPopupResourcesListPanel();
		String copyPopUpResourceListImage();
		String resourceThumbnails();
		String copyPopUpResourceListBoxText();
		String copyPopUpResourceListBoxTextTop();
		
		String copyPopUpResourceListBoxLoadingTxt();
		
		String copyPopupResourcesNewContainer();

		String copyPopUpPanelStyle();

		String resourceNarration();

		String resourceNarrationPanel();

		String resourceTitle();

		String resourceTitleCursor();

		String resourceTitle1();

		String collectionResource();

		String collectionResourceEdit();

		String resourceMainPanel();

		String actionPanel();

		String overRideBlueButton();

		String copyAction();

		String removeAction();

		String editResourceInfo();

		String toLbl();

		String textBox();

		String questionBox();

		String editPanel();

		String fromLbl();

		String shelfResourceDragdropPositioner();
		
		String buttonPositionStyle();

		String shelfResourceDragdropSpacer();

		String shelfResourceSequenceSpacer();

		String shelfResourceSequenceNumber();

		String resourceNarrationEdit();

		String pencilEditImage();

		String pencilImageHide();

		String pencilAllign();

		String narrationAlertMessage();

		String reorderLabel();

		String shelfResourceSequencePanel();

		String reorderLabelContainer();
		
		String reorderLabelContainerRFormat();

		String newResourceLabel();

		String noResourceLabel();

		String noResourceContainer();

		String newResourceLabelColor();

		String newResourceMessageLabel();

		@ClassName(value="resourceNarrationEdit-readonly")
		String resourceNarrationEditReadOnly();

		/************** Copy Confirmation Popup*******************/
		String copyPopupResources();

		String copyPopupBirthday();

		String copyPopupleftAndWidth();

		String copyPopupResourcesDownArrow();

		String copyPopupEmail();

		String copyPopupResourcesUserInput();

		String copyPopupResourcesUserInputTwo();

		String copyPopupResourcesWrapper();

		String copyPopupRegisterWrapper();

		String copyPopupResourcesContainer();

		String copyPopupRegisterContainer();

		String copyPopupResourcesHeader();

		String copyPopupResourcesHeaderTitle();

		String copyPopupResourcesDescribtion();

		String copyPopupResourcesButtonContainer();

		String copyPopResourcesOkButton();

		String copyPopupResourcesQuestionMark();

		String copyPopupResourcesSendButton();

		String copyPopupResourcesCancelButton();

		String copyPopupSettingPageAddBtn ();

		String copyPopupSettingPageChangePasswordText ();

		String copyPopupSettingPageEducationInfoLeftInputDropDown ();

		String copyPopupSettingPageEducationInfoLeftInputDropDownTextContainer ();

		String copyPopupSettingPageEducationInfoLeftInputDropDownText ();

		String copyPopupSettingPageEducationInfoLeftInputDropDownTextContainerTwo ();

		/*String collectionEditHeaderText();

		String collectionEditPopUpOuterDiv();

        String collectionEditPopupBtnSprite();

		String collectionEditPopupCloseBtn();*/
		/* New Popup css */
		String myFolderCollectionEditResource();
		String myFolderCollectionPopupSprite();
		String myFolderCollectionArrowleftContainer();
		String myFolderCollectionArrowleft();
		String clear();
		String myFolderCollectionPopupContainer();
		String myFolderCollectionPopupOuterdiv();

		String myFolderCollectionPopup();
		String myFolderCollectionBlackBg();
		String myFolderCollectionTitle();
		String myFolderCollectionUrlContent();
		String myFolderCollectionUrlbg();
		String buttonSelected();
		String buttonDeSelected();

		String myFolderCollectionUrlbgTitle();
		String myFolderCollectionFormContainer();
		String myFolderCollectionFormContent();
		String myEducationalFormContent();
		String myEducationArrowleftContainer();
		String myFolderCollectionFormTitle();
		String myFolderCollectionFormInputControl();
		String myEducationFormInputControl();
		String myFolderCollectionFormInputNoneborder();
		String myFolderCollectionCategoryDiv();
		String myFolderCollectionCategoryInputDiv();
		String myFolderCollectionCategoryInputDivRFormat();
		String myEducationPanelInputDiv();
		String myEducationDropdown();
		String myFolderCollectionCategoryDivText();
		String myFolderCollectionFormInputBottomText();
		String myFolderCollectionThumbImageContainer();
		String standardsInEditResource();
		String myFolderCollectionThumbImageDesc();
		String myFolderCollectionThumbRect();
		String myFolderCollectionThumbArrLContainer();
		String myFolderCollectionThumbArrLeftIcon();
		String myFolderCollectionThumbArrRContainer();
		String myFolderCollectionThumbArrRightIcon();
		String myFolderCollectionThumbRectDesc();
		String myFolderCollectionThumbOrtext();
		String myFolderCollectionThumbUploadImagetext();
		String myFolderCollectionRefreshMainContainer();
		String myFolderCollectionRefreshMainContainerWeb();
		String myFolderCollectionRefreshContainer();
		String myFolderCollectionRefreshInnerContainer();
		String myFolderCollectionRefreshIcon();
		String myFolderCollectionRefresh();
		String myFolderCollectionRefreshText();
		String myFolderCollectionFormTextarea();
		String myFolderCollectionFormDesc();
		String myFolderCollectionBlueButtonContainer();
		String myFolderCollectionBlue_Ok();
		String myFolderCollectionBlue_OkTitle();
		String myFolderCollectionBlue_Cancel();
		String myFolderCollectionGridbg();

		String inputTextBox();

		String myFolderCollectionButtonContainer();
		String myFolderCollectionOk();
		String myFolderCollectionAddTitle();
		String myFolderCollectionCancel();
		String myFolderCollectionFolderleftdesc();
		String myFolderCollectionFolderspriteNew();
		String myFolderCollectionFolderVideoOuterContainer();
		String myFolderCollectionFolderVideoContainer();
		String myFolderCollectionFolderVideoInnerdiv();
		String myFolderCollectionFolderVideoContent();
		String myFolderCollectionFolderspriteVideo();
		String myFolderCollectionFolderspriteInteractive();
		String myFolderCollectionFolderspriteWebsite();
		String myFolderCollectionFolderspriteSlide();
		String myFolderCollectionFolderspriteHandout();
		String myFolderCollectionFolderspriteTextbook();
		String myFolderCollectionFolderspriteLesson();
		String myFolderCollectionFolderspriteQuestion();
		String myFolderCollectionFolderspriteExam();
		String myFolderCollectionFolderspriteAudio();
		String myFolderCollectionFolderspriteImage();
		String myFolderCollectionFolderspriteText();
		String myFolderCollectionFolderspriteOther();
		String myFolderCollectionFolderVideoTitle();
		String myFolderCollectionFolderDropdown();
		String myFolderCollectionFolderUpdateDropdown();
		String resourceCategoryLabel();

		String newPopupTextArea();
		String categoryListBox();
		String resourceThumbnailImage();

		//ExistsResource CSS
		String resourceExistsPopupSpriteImg();

		String resourceExistsPopupSpriteVideoContainer();

		String resourceExistsPopupSpriteVideo();

		String resourceExistsPopupPopupOuterContainer();

		String resourceExistsPopupContainer();

		String resourceExistsPopup();

		String resourceExistsPopupBlackBg();

		String resourceExistsPopupTitle();

		String resourceExistsPopupDesc();

		String resourceExistsPopupFormContainer();

		String resourceExistsPopupFormTitle();

		String resourceExistsPopupFormInputControl();

		String resourceExistsPopupBlueButtonContainer();

		String resourceExistsPopupBluebg();

		String resourceExistsPopupBlue_Ok();

		String resourceExistsPopupBlue_OkTitle();

		String resourceExistsPopupBlue_Cancel();

		String resourceExistsPopupRoundedCorner();

		String resourceExistsPopupRoundedCornerThumbimgContainer();

		String resourceExistsPopupRoundedCornerThumbimg();

		String resourceExistsPopupRoundedCornerThumbHeading();

		String resourceExistsImageDim();

		//		Resource Type Icon CSS
		String resourceExistsResourceTypeSprite();
		String resourceExistsResourceTypeSpriteVideo();
		String resourceExistsResourceTypeSpriteInteractive();
		String resourceExistsResourceTypeSpriteWebsite();
		String resourceExistsResourceTypeSpriteSlide();
		String resourceExistsResourceTypeSpriteHandout();
		String resourceExistsResourceTypeSpriteTextbook();
		String resourceExistsResourceTypeSpriteLesson();
		String resourceExistsResourceTypeSpriteQuestion();
		String resourceExistsResourceTypeSpriteExam();
		String addNewResourceMandatoryCategory();


		String myWebResourceContainer();

		String urlLabel();
		String myFolderEditButtonContainer();

		String reportResource();

		String reportResourceContainer();

		String newPopupTextAreaEdit();

		String noCollectionMsgForAddPopUp();

		String loadingImageForSelfEdit();
		
	    String loadingImageMainDiv();
	    
		String loadingImageForResource();
		
		String loadingImageForResource1();

		String copyPopupResourcesButtonContainerClick();

		String addResourceThumbnailContent();
		String addResourceImgDesc();

		String addButtonTitle();
		String assessmentDownnArrow();
		String assessmentPopup();
		String assessmentPopupList();
		String assessmentAddContainer();

		String contentCollectionBtnbg();

		String contentCollectionPlusIcon();

		String contentCollectionAddTitle();

		String contentCollectionEdit();
		String newResourceLabelNoHover();
		String contentCollectionBtnbgGray();
		String addResourceCloseButtonContainer();
		String addResourceCloseButton();
		
		String addResource();
		String addResourceDisable();
		String questionRadioButtonStyle();
		String addNewResourceBackgroudColor();
		
		// User own resource //
		
		String ownResourceFormInputTextBox();
		
		String ownResourceFormInputControl();
		String ownResourceFormOk();
		String ownResourceFormCancel();
		
		String ownResourceFormUpload();
		String ownResourceFormBrowse();
		String ownResourceFormBrowsePosition();
		
		String ownResourceFormRightsContent();
		String resourceRightsPopupContainer();
		String arrowShadow();
		String arrowBorder();
		String arrow();
		String resourceRightsInnerPopup();
		String resourceRightsPopupTitle();
		String ownResourceFormDeclarationText();
		String editButtonContainer();
		String editButtonText();
		String panelAlignRight();
		String resourceUploadPosition();
		String resourceRightsCheckBox();
		String resourceRightsContainer();
		String resourceRightsMessage();
		String anchorText();
		String processing();
		String narrationCancelButton();
		String resourceVideoTime();
		String videoText();
		String videoStartTimeLabel();
		String videoEndTimeLabel();
		String videoImageContainer();
		String editTimeContainer();
		String startStopTimeDisplayText();
		String MinuteLabel();
		String textAlignContainer();
		String outerdivContainer();
		String gEditButton();
		String pdfImageContainer();
		String resourceUploadEditPosition();
		
		String ownResourceFormInputTextBoxForError();
		String myFolderCollectionFormInputControlForErrors();
		String ownResourceFormInputControlForErrors();
		String customAnchorText();
		String fileActionPanel();
		String adding();
		String btnAdd();
		String fileEditAdding();
		String fileActionPanelEditFile();
		String fileUplodDefultLbl();
		String changeFileBtnPosition();
		String rollBackToPaperClipPos();
		String fileContainer();
		String paperClip();
		String imgHeight();
		String resourceRightsContainerWeb();
		String resourceRightsContainerPdf();
		
		String infoCourseListText();
		String collectionInfoCourseList();
		String collectionInfobuttonSelected();
		String loginPopupGlassStyle();
		String updateButton();
		String andText();
		String additionalText();
		String updateTextStyle();
		String myshelfCollectionresourceDropdown();
		String myEducationalPanelSubTitles();
		String myEducationQuestionDropdown();
		String myEducationEditDropdown();
		
		String shelfFoldereDragdropSpacer();
		String myFolderCollectionFolderUpdateDropdownPanel();
		String loadingpanelImage();
        String dDriveSlides();
        
        String driveWebContainer();
        String errorMsgLabel();
        String editpdfContainer();
        String stopPdfLbl();
        String h5();
        String li();
        String clearfix();
        String dropdownContainerInstructional();
        String placeHolderText();
        String arrowInstructional();
        String scrollPanelContainerInstructional();
        String selectsection();
        String fRight();
        String OnButtonDeActive();
        String accessHazard();
        String charLimit();
        /**
         * Following line is commented for hotfix
         */
        /*String upDownBtn();*/
        
        String coursePopupStyle();
        String generateImage();
        String generateMessage();
        String myFolderCollectionThumbOrtextWeb();
        String generateImageContainer();
        String  myFolderCollectionThumbRectWeb();
        String eHeader();
        String eInput();
        String advncedOption();
        String shelfGradeInfoBottomWeb();
        String myFolderCollectionFolderVideoInnerdivRFormat();
        String myFolderCollectionFolderVideoOuterContainerRFormat();
        String myFolderCollectionFolderDropdownRFormat();
        String setBorder();
           
	}
	
	@NotStrict
	@Source("CollectionEditResource.css")
	CollectionEditResourceCss css();

}
