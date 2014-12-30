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
package org.ednovo.gooru.client.mvp.play.collection.preview.home.customize;

import java.util.HashMap;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.home.library.customize.LoginPluginView;
import org.ednovo.gooru.client.mvp.play.collection.end.study.CloseCollectionPlayerEvent;
import org.ednovo.gooru.client.mvp.play.collection.preview.home.assign.AssignPopUpCBundle;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderZIndexEvent;
import org.ednovo.gooru.client.mvp.shelf.event.RefreshUserShelfCollectionsEvent;
import org.ednovo.gooru.client.service.ResourceServiceAsync;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.client.util.SetStyleForProfanity;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.content.CollectionDo;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * @author BLR Team
 * 
 */
public abstract class RenameCustomizePopUp extends PopupPanel{

	@UiField
	HTMLPanel popupContentAssign,loginCustom, copyCollectionSuccess, panelAssign,loadingImageLabel,popupcontentCustomize,customizeText,buttonsContainer;

	@UiField(provided = true)
	AssignPopUpCBundle res;

	@UiField
	Label cancelButton, assignDes,lblpopupTitle,lbltxtBoxTitle, errorLabel,loadingLbl;

	@UiField
	Button backtoLibrary, editCollection;

	@UiField
	TextBox copycollectionTextbox;

	@Inject
	private ResourceServiceAsync resourceService;

	private SimpleAsyncCallback<CollectionDo> saveCollectionAsyncCallback;

	CollectionDo collectionDo = new CollectionDo();

	@UiTemplate("RenameCustomizePopUp.ui.xml")
	interface Binder extends UiBinder<Widget, RenameCustomizePopUp> {

	}

	private static final Binder binder = GWT.create(Binder.class);
	
	private MessageProperties i18n = GWT.create(MessageProperties.class);

	private boolean isDraggedFromSearch=false;
	
	private boolean isCustomizePopup = false,isHavingBadWords;
	
	/**
	 * 
	 */
	public RenameCustomizePopUp(String collectionId, final Boolean loginFlag,final String collectionTitle) {
		super(false);
		this.res = AssignPopUpCBundle.INSTANCE;
		res.css().ensureInjected();
		add(binder.createAndBindUi(this));
		errorLabel.setVisible(false);
		this.setGlassEnabled(true);
		customizeText.getElement().setInnerHTML(i18n.GL0743());
		customizeText.getElement().setAttribute("alt",i18n.GL0743());
		customizeText.getElement().setAttribute("title",i18n.GL0743());
		
		backtoLibrary.setText(i18n.GL0142());
		backtoLibrary.getElement().setAttribute("alt",i18n.GL0142());
		backtoLibrary.getElement().setAttribute("title",i18n.GL0142());
		
		editCollection.setText(i18n.GL0636());
		editCollection.getElement().setAttribute("alt",i18n.GL0636());
		editCollection.getElement().setAttribute("title",i18n.GL0636());
		
		panelAssign.getElement().getStyle().setMarginBottom(3, Unit.PX);
		loginCustom.getElement().getStyle().setMarginBottom(15, Unit.PX);
		isDraggedFromSearch=false;
		Window.enableScrolling(false);
		this.getElement().setAttribute("style", "z-index:99999;");
		this.getGlassElement().setAttribute("style", "z-index:99999; position:absolute; left:0px; top:0px;");
		AppClientFactory.fireEvent(new SetHeaderZIndexEvent(99, false));

		popupcontentCustomize.setVisible(false);
		loadingImageLabel.setVisible(true);

		editCollection.getElement().setAttribute("collectionId", collectionId);
		

		assignDes.setText(i18n.GL0744());
		assignDes.getElement().setAttribute("alt",i18n.GL0744());
		assignDes.getElement().setAttribute("title",i18n.GL0744());
		
		lblpopupTitle.setText(i18n.GL0743());
		lblpopupTitle.getElement().setAttribute("alt",i18n.GL0743());
		lblpopupTitle.getElement().setAttribute("title",i18n.GL0743());
		
		lbltxtBoxTitle.setText(i18n.GL0553());
		lbltxtBoxTitle.getElement().setAttribute("alt",i18n.GL0553());
		lbltxtBoxTitle.getElement().setAttribute("title",i18n.GL0553());
		
		copycollectionTextbox.setMaxLength(50);
		copycollectionTextbox.addKeyPressHandler(new OnkeyPress());
		copycollectionTextbox.addBlurHandler(new OnBlurr());
		copycollectionTextbox.addKeyUpHandler(new OnkeyUp());
		
		/*copycollectionTextbox.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				Map<String, String> parms = new HashMap<String, String>();
				parms.put("text", copycollectionTextbox.getValue());
				AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {

					@Override
					public void onSuccess(Boolean value) {
						isHavingBadWords=value;
						SetStyleForProfanity.SetStyleForProfanityForTextBox(copycollectionTextbox, errorLabel, value);
					}
				});
			}
		});*/
		AppClientFactory.getInjector().getClasspageService().getSCollIdClasspageById(collectionId, new SimpleAsyncCallback<CollectionDo>(){

			@Override
			public void onSuccess(CollectionDo result) {
				collectionDo = result;
				MixpanelUtil.Preview_Click_Customize_successful();

				copycollectionTextbox.setText(result.getTitle());

				if (loginFlag) {
					loginCustom.setVisible(true);
					copyCollectionSuccess.setVisible(false);
					LoginPluginView assignWidget = new LoginPluginView(result,collectionTitle) {

						@Override
						public void closePoupfromChild() {
							closePoup();
						}

						@Override
						public void showSuccessMsgfromChild(String collectionId,String collectionTitle) {
							showSuccessMsg(collectionId);

						}
					};
					loginCustom.add(assignWidget);
				} else {
					loginCustom.setVisible(false);
					copyCollectionSuccess.setVisible(true);


				}
				popupcontentCustomize.setVisible(true);
				loadingImageLabel.setVisible(false);
			}
		});

		MixpanelUtil.mixpanelEvent("CoursePage_customize_collection");
		
		
		
		
		
		
		
		setId();
		this.center();

	}
	public void setId(){
		panelAssign.getElement().setId("pnlPanelAssign");
		lbltxtBoxTitle.getElement().setId("lbltxtBoxTitle");
		lblpopupTitle.getElement().setId("lblpopupTitle");
		assignDes.getElement().setId("lblAssignDes");
		editCollection.getElement().setId("btnEditCollection");
		customizeText.getElement().setId("pnlCustomizeText");
		backtoLibrary.getElement().setId("btnBacktoLibrary");
		cancelButton.getElement().setId("lblCancelButton");
		loadingImageLabel.getElement().setId("pnlLoadingImageLabel");
		loadingLbl.getElement().setId("pnlLoadingLbl");
		popupcontentCustomize.getElement().setId("pnlPopupcontentCustomize");
		loginCustom.getElement().setId("pnlLoginCustom");
		copyCollectionSuccess.getElement().setId("pnlCopyCollectionSuccess");
		popupContentAssign.getElement().setId("pnlPopupContentAssign");
		copycollectionTextbox.getElement().setId("txtCopycollectionTextbox");
		StringUtil.setAttributes(copycollectionTextbox, true);
		errorLabel.getElement().setId("errlblErrorLabel");
		buttonsContainer.getElement().setId("pnlButtonsContainer");
	}

	public RenameCustomizePopUp(String dragId) { 
		super(false);
		this.res = AssignPopUpCBundle.INSTANCE;
		res.css().ensureInjected();
		add(binder.createAndBindUi(this));
		errorLabel.setVisible(false);
		this.setGlassEnabled(true);
		customizeText.getElement().setInnerHTML(i18n.GL0322());
		customizeText.getElement().setAttribute("alt",i18n.GL0322());
		customizeText.getElement().setAttribute("title",i18n.GL0322());
		isDraggedFromSearch = true;
		copyCollectionSuccess.getElement().getStyle().setPadding(0, Unit.PX);
		backtoLibrary.setText(i18n.GL0142());
		editCollection.setText(i18n.GL0190());
		panelAssign.getElement().getStyle().setMarginBottom(4, Unit.PX);
		loginCustom.getElement().getStyle().setMarginBottom(15, Unit.PX);
		buttonsContainer.setStyleName(res.css().collectionSearchRenamePopupButtons());
		loadingLbl.getElement().getStyle().setMarginTop(5, Unit.PCT);
		loadingLbl.getElement().getStyle().setMarginLeft(25, Unit.PCT);
		errorLabel.getElement().getStyle().setPaddingTop(30, Unit.PX);
		errorLabel.getElement().getStyle().setColor("orange"); 
		Window.enableScrolling(false);
		copycollectionTextbox.addKeyPressHandler(new OnkeyPress());
		copycollectionTextbox.addBlurHandler(new OnBlurr());
		copycollectionTextbox.setMaxLength(50);
		/*this.getElement().setAttribute("style", "z-index:99999;");
		this.getGlassElement().setAttribute("style", "z-index:99999; position:absolute; leftge:0px; top:0px;");
		AppClientFactory.fireEvent(new SetHeaderZIndexEvent(99, false));*/
		popupcontentCustomize.setVisible(false);
		loadingImageLabel.setVisible(true);
		editCollection.getElement().setAttribute("collectionId", dragId);
		assignDes.getElement().getStyle().setDisplay(Display.NONE);
		lblpopupTitle.getElement().getStyle().setDisplay(Display.NONE);
		lbltxtBoxTitle.setText(i18n.GL0553());
		lbltxtBoxTitle.getElement().setAttribute("alt",i18n.GL0553());
		lbltxtBoxTitle.getElement().setAttribute("title",i18n.GL0553());
		copycollectionTextbox.getElement().getStyle().setWidth(275, Unit.PX);
		AppClientFactory.getInjector().getClasspageService().getSCollIdClasspageById(dragId, new SimpleAsyncCallback<CollectionDo>(){
			@Override
			public void onSuccess(CollectionDo result) {
				collectionDo = result;
				copycollectionTextbox.setText(result.getTitle());
				loginCustom.setVisible(false);
				copyCollectionSuccess.setVisible(true);
				loadingImageLabel.setVisible(false);
				popupcontentCustomize.setVisible(true);
			}
		});
		 setId();
	}

	public abstract void closePoup();

	public ResourceServiceAsync getResourceService() {
		return resourceService;
	}

	public void setResourceService(ResourceServiceAsync resourceService) {
		this.resourceService = resourceService;
	}

	public void showSuccessMsg(String collectionId) {
		loginCustom.setVisible(false);
		copyCollectionSuccess.setVisible(true);
		editCollection.getElement().setAttribute("collectionId", collectionId);
		customizeText.getElement().setInnerHTML(i18n.GL0743());

	}

	/**
	 * Added click handler to hide the login popup.
	 * 
	 * @param clickEvent
	 *            instance of {@link ClickEvent}
	 */
	@UiHandler("cancelButton")
	public void onCancelClicked(ClickEvent clickEvent) {
		closePoup();
	}

	@UiHandler("backtoLibrary")
	public void onbacktoLibrarybuttonClicked(ClickEvent clickEvent) {
		closePoup();
	}

	@UiHandler("editCollection")
	public void onEditcollectionbuttonClicked(ClickEvent clickEvent) {
		//String collectionId = clickEvent.getRelativeElement().getAttribute("collectionId");

		final String collectionTitle = copycollectionTextbox.getText();
		if(isDraggedFromSearch){
			if(collectionTitle.isEmpty() || collectionTitle.trim().isEmpty()){
				errorLabel.setText(i18n.GL0693());
				errorLabel.setVisible(true);
			}else{
				Map<String, String> parms = new HashMap<String, String>();
				parms.put("text", copycollectionTextbox.getValue());
				AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {
					@Override
					public void onSuccess(Boolean value) {
						isHavingBadWords=value;
						if(value){
							SetStyleForProfanity.SetStyleForProfanityForSearchRenameCollTextBox(copycollectionTextbox, errorLabel, isHavingBadWords);
						}else{
							collectionDo.setTitle(collectionTitle);
//							AppClientFactory.fireEvent(new CopyCollectionEvent(collectionDo)); 
							closePoup();
						}
					}
				});
			}
		}else{
			Map<String, String> parms = new HashMap<String, String>();
			parms.put("text", copycollectionTextbox.getValue());
			AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {
				@Override
				public void onSuccess(Boolean value) {
							isHavingBadWords=value;
							if(value){
								SetStyleForProfanity.SetStyleForProfanityForTextBox(copycollectionTextbox, errorLabel, value);
							}else{
								
								if(!collectionTitle.isEmpty() && !collectionTitle.trim().isEmpty())
								{
									closePoup();
								if(!isCustomizePopup){
									isCustomizePopup=true;
								
								collectionDo.setTitle(collectionTitle);
								AppClientFactory.getInjector().getResourceService().copyCollection(collectionDo, "true", null,getSaveCollectionAsyncCallback());
							}
						}
						else
						{
							errorLabel.setText(i18n.GL0693());
							errorLabel.setVisible(true);	
						}
					}
				}
			});
		}
	}
	

	/**
	 * @return instance of collectionDo after collection save
	 */
	public SimpleAsyncCallback<CollectionDo> getSaveCollectionAsyncCallback() {
		if (saveCollectionAsyncCallback == null) {
			saveCollectionAsyncCallback = new SimpleAsyncCallback<CollectionDo>() {
				@Override
				public void onSuccess(CollectionDo result) {
					AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.SHELF,new String[] {"id",result.getGooruOid()});
					AppClientFactory.fireEvent(new RefreshUserShelfCollectionsEvent());
					closePoup();
					AppClientFactory.fireEvent(new CloseCollectionPlayerEvent(true));
				}
			};
		}
		return saveCollectionAsyncCallback;
	}

	public class OnkeyPress implements KeyPressHandler
	{
		@Override
		public void onKeyPress(KeyPressEvent event) {
			String collTitle=copycollectionTextbox.getText().trim();
			if(collTitle.length()>=50){
				errorLabel.setText(i18n.GL0143());
				errorLabel.setVisible(true);
			}else{
				errorLabel.setText("");
				errorLabel.setVisible(false);
			}
			
		}
	}
	public class OnkeyUp implements KeyUpHandler{
		
		@Override
		public void onKeyUp(KeyUpEvent event) {
			String collTitle=copycollectionTextbox.getText().trim();
			if(collTitle.length()>=50){
				errorLabel.setText(i18n.GL0143());
				errorLabel.setVisible(true);
			}else{
				errorLabel.setText("");
				errorLabel.setVisible(false);
			}
			
		}
	}
	public class OnBlurr implements BlurHandler
	{
		@Override
		public void onBlur(BlurEvent event) {
			Map<String, String> parms = new HashMap<String, String>();
			parms.put("text", copycollectionTextbox.getValue());
			checkForProfanity(parms,isDraggedFromSearch);
			
		}
		
	}

	public void checkForProfanity(Map<String, String> parms,final boolean isDraggedFromSearch ) { 
		AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean value){ 
				isHavingBadWords=value;
				if(isDraggedFromSearch){
					SetStyleForProfanity.SetStyleForProfanityForSearchRenameCollTextBox(copycollectionTextbox, errorLabel, isHavingBadWords);
				}else{
					SetStyleForProfanity.SetStyleForProfanityForTextBox(copycollectionTextbox, errorLabel, value);
				}
				
			}
		});
	}
}
