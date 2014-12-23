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
package org.ednovo.gooru.client.mvp.play.collection.header;


import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.shelf.ShelfPresenter;
import org.ednovo.gooru.client.uc.PlayerBundle;
import org.ednovo.gooru.client.uc.tooltip.GlobalToolTip;
import org.ednovo.gooru.client.ui.HTMLEventPanel;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class StudyPlayerHeaderView extends Composite{
	
	@UiField HTML resourceTitle;
	
	@UiField Label closeButtonForCollection,loginMessageText/*,thumbsDownButton,thumbsUpButton*/;
	
	@UiField HTMLEventPanel authorContainer;
	
	@UiField InlineLabel wishLabel,loginUserName,wishingText;
	
	@UiField StudyPlayerStyle headerStyle;
	
	private PopupPanel toolTipPopupPanel=new PopupPanel();
	
	
	private static CollectionPlayerHeaderViewUiBinder uiBinder = GWT.create(CollectionPlayerHeaderViewUiBinder.class);

	interface CollectionPlayerHeaderViewUiBinder extends UiBinder<Widget, StudyPlayerHeaderView> {
	}
	
	private MessageProperties i18n = GWT.create(MessageProperties.class);
	
	public interface StudyPlayerStyle extends CssResource{
		public String loggedIn();
		public String loggedOut();
	}
	
	public StudyPlayerHeaderView(){
		initWidget(uiBinder.createAndBindUi(this));
		//navigationButton.getElement().setId("navigationButton");
		PlayerBundle.INSTANCE.getPlayerStyle().ensureInjected();
//		studentViewButton.setText(i18n.GL0139());
//		studentViewButton.getElement().setId("lnkStudentViewButton");
//		studentViewButton.getElement().setAttribute("alt",i18n.GL0139());
//		studentViewButton.getElement().setAttribute("title",i18n.GL0139());
//		
//		studentViewButton.addMouseOverHandler(new OnStudentViewButtonMouseOver());
//		studentViewButton.addMouseOutHandler(new OnStudentViewButtonMouseOut());
		//shareButton.addMouseOverHandler(new ShareButtonMouseOver());
		//shareButton.addMouseOutHandler(new ShareButtonMouseOut());
		
		//navigationButton.getElement().setId("btnNavigationButton");
		closeButtonForCollection.getElement().setId("lblCloseButtonForCollection");
		resourceTitle.getElement().setId("htmlResourceTitle");
		authorContainer.getElement().setId("epnlAuthorContainer");
		wishLabel.getElement().setId("spnWishLabel");
		loginUserName.getElement().setId("spnLoginUserName");
		wishingText.getElement().setId("spnWishingText");
		wishLabel.getElement().getStyle().setFloat(Float.LEFT);
		wishingText.getElement().getStyle().setFloat(Float.LEFT);
		loginMessageText.getElement().setId("lblLoginMessageText");
	}
	
	public void setResourceTitle(String title){
		resourceTitle.setHTML(title);
	}

	
	public HTMLEventPanel getAuthorContainer() {
		return authorContainer;
	}
	
	public void displayAuthorName(String collectionType){
		if(AppClientFactory.isAnonymous()){
			authorContainer.removeStyleName(headerStyle.loggedIn());
			authorContainer.addStyleName(headerStyle.loggedOut());
			setLoggedOutWishingText();
		}else{
			authorContainer.removeStyleName(headerStyle.loggedOut());
			authorContainer.addStyleName(headerStyle.loggedIn());
			setLoggedInWishingText(collectionType);
			loginMessageText.setText(StringUtil.generateMessage(i18n.GL1616(), AppClientFactory.getLoggedInUser().getUsernameDisplay()));
			loginMessageText.getElement().setAttribute("alt",StringUtil.generateMessage(i18n.GL1616(), AppClientFactory.getLoggedInUser().getUsernameDisplay()));
			loginMessageText.getElement().setAttribute("title",StringUtil.generateMessage(i18n.GL1616(), AppClientFactory.getLoggedInUser().getUsernameDisplay()));
		}
	}
	public void setLoggedOutWishingText(){
		wishLabel.setText("");
		wishLabel.getElement().setAttribute("alt","");
		wishLabel.getElement().setAttribute("title","");
		loginUserName.setText("");
		loginUserName.getElement().setAttribute("alt","");
		loginUserName.getElement().setAttribute("title","");
		wishingText.setText(i18n.GL1531());
		wishingText.getElement().setAttribute("alt","");
		wishingText.getElement().setAttribute("title","");
	}
	public void setLoggedInWishingText(String collectionType){
		wishLabel.setText(i18n.GL1529());
		wishLabel.getElement().setAttribute("alt",i18n.GL1529());
		wishLabel.getElement().setAttribute("title",i18n.GL1529());
		loginUserName.setText(AppClientFactory.getLoggedInUser().getUsernameDisplay());
		loginUserName.getElement().setAttribute("alt",AppClientFactory.getLoggedInUser().getUsernameDisplay());
		loginUserName.getElement().setAttribute("title",AppClientFactory.getLoggedInUser().getUsernameDisplay());
		if(collectionType!=null&&collectionType.equals(ShelfPresenter.ASSESSMENT)){
			wishingText.setText(i18n.GL1530());
			wishingText.getElement().setAttribute("alt",i18n.GL1530());
			wishingText.getElement().setAttribute("title",i18n.GL1530());
		}else{
			wishingText.setText(i18n.GL3041());
			wishingText.getElement().setAttribute("alt",i18n.GL3041());
			wishingText.getElement().setAttribute("title",i18n.GL3041());
		}
	}
	

	

	public Label getCloseButton(){
		return closeButtonForCollection;
	}

	
	public class OnStudentViewButtonMouseOver implements MouseOverHandler{

		@Override
		public void onMouseOver(MouseOverEvent event) {
			toolTipPopupPanel.clear();
			toolTipPopupPanel.setWidget(new GlobalToolTip(i18n.GL0668(),true));
			toolTipPopupPanel.setStyleName("");
			//toolTipPopupPanel.setPopupPosition(studentViewButton.getElement().getAbsoluteLeft()-35, studentViewButton.getElement().getAbsoluteTop()+4);
			toolTipPopupPanel.getElement().getStyle().setZIndex(999999);
			toolTipPopupPanel.show();
			toolTipPopupPanel.getElement().getStyle().setMarginLeft(57, Unit.PX);
		}
		
	}
	
	public class OnStudentViewButtonMouseOut implements MouseOutHandler{

		@Override
		public void onMouseOut(MouseOutEvent event) {
			toolTipPopupPanel.hide();
		}

		
		
	}
	public class ShareButtonMouseOver implements MouseOverHandler{

		@Override
		public void onMouseOver(MouseOverEvent event) {
//			if(!isShareButtonEnabled){
//			toolTipPopupPanel.clear();
//			toolTipPopupPanel.setWidget(new GlobalToolTip(i18n.GL0679()));
//			toolTipPopupPanel.setStyleName("");
//			//toolTipPopupPanel.setPopupPosition(shareButton.getElement().getAbsoluteLeft()+7, shareButton.getElement().getAbsoluteTop()+21);
//			toolTipPopupPanel.getElement().getStyle().clearMarginLeft();
//			toolTipPopupPanel.getElement().getStyle().setZIndex(999999);
//			toolTipPopupPanel.show();
//			}
		}
		
	}
	
	public class ShareButtonMouseOut implements MouseOutHandler{

		@Override
		public void onMouseOut(MouseOutEvent event) {
			toolTipPopupPanel.hide();
		}

		
		
	}
	
}
