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

package org.ednovo.gooru.client.uc.tooltip;

import java.util.HashMap;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.folders.event.RefreshFolderType;
import org.ednovo.gooru.client.mvp.shelf.collection.folders.events.RefreshFolderItemEvent;
import org.ednovo.gooru.client.mvp.shelf.collection.folders.uc.FolderPopupUc;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.folder.FolderDo;
import org.ednovo.gooru.shared.util.StringUtil;
import org.ednovo.gooru.shared.util.UAgentInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Navigator;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * @fileName : OrganizeToolTip.java
 *
 * @description : 
 *
 *
 * @version : 1.0
 *
 * @date: 27-Dec-2013
 *
 * @Author: Gooru Team
 *
 * @Reviewer: Gooru Team
 */
public class OrganizeToolTip extends PopupPanel implements HasMouseOutHandlers{
	
	private static OrganizeTipUiBinder uiBinder = GWT
			.create(OrganizeTipUiBinder.class);
	
	private MessageProperties i18n = GWT.create(MessageProperties.class); 

	interface OrganizeTipUiBinder extends UiBinder<Widget, OrganizeToolTip> {
	}
	
	@UiField Label lblCreateFolder,lblCreateCollection, lblEditMyCollections,lblCreateAsseement;
	
	@UiField HTMLPanel tooltipPanel,panelCode;
	
	public OrganizeToolTip() {
		setWidget(uiBinder.createAndBindUi(this));
		
		lblCreateCollection.setText(i18n.GL1757());
		lblCreateCollection.getElement().setId("lblLblCreateCollection");
		lblCreateCollection.getElement().setAttribute("alt", i18n.GL1757());
		lblCreateCollection.getElement().setAttribute("title", i18n.GL1757());
		
		lblCreateAsseement.setText(i18n.GL3011());
		lblCreateAsseement.getElement().setId("lblLblAssessment");
		lblCreateAsseement.getElement().setAttribute("alt", i18n.GL3011());
		lblCreateAsseement.getElement().setAttribute("title", i18n.GL3011());
		
		lblCreateFolder.setText(i18n.GL1758());
		lblCreateFolder.getElement().setId("lblLblCreateFolder");
		lblCreateFolder.getElement().setAttribute("alt", i18n.GL1758());
		lblCreateFolder.getElement().setAttribute("title", i18n.GL1758());
		
		lblEditMyCollections.setText(i18n.GL1759());
		lblEditMyCollections.getElement().setId("lblLblEditMyCollections");
		lblEditMyCollections.getElement().setAttribute("alt", i18n.GL1759());
		lblEditMyCollections.getElement().setAttribute("title", i18n.GL1759());
		lblEditMyCollections.setVisible(false);
		
		panelCode.getElement().setId("pnlPanelCode");
		tooltipPanel.getElement().setId("pnlTooltipPanel");
		
		  Boolean isIpad = !!Navigator.getUserAgent().matches("(.*)iPad(.*)");
		  Boolean isAndriod = !!Navigator.getUserAgent().matches("(.*)Android(.*)");
		  Boolean isWinDskp = !!Navigator.getUserAgent().matches("(.*)NT(.*)");
		  
		  UAgentInfo detector = new UAgentInfo(Navigator.getUserAgent());
		  
		  if(isIpad && !StringUtil.IPAD_MESSAGE_Close_Click)
		  {
			  tooltipPanel.getElement().setAttribute("style", "position:relative;top:-3px;");
		  }
		  else if(isAndriod && !StringUtil.IPAD_MESSAGE_Close_Click)
		  {
			  tooltipPanel.getElement().setAttribute("style", "position:relative;top:-3px;");
		  }		  
		  else
		  {
			  tooltipPanel.getElement().setAttribute("style", "position:fixed;top:50px;");
		  }
		
		lblCreateFolder.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (!AppClientFactory.isAnonymous()){
					FolderPopupUc folderPopupUc = new FolderPopupUc("", true) {
						@Override
						public void onClickPositiveButton(ClickEvent event, String folderName, String parentId, HashMap<String,String> params) {
							if(!folderName.isEmpty()) {
								createFolderInParent(folderName, parentId, params); 
//								Window.enableScrolling(true);
								this.hide();
							}
						}
					};
					folderPopupUc.setGlassEnabled(true);
					folderPopupUc.removeStyleName("gwt-PopupPanelGlass");
					folderPopupUc.setPopupPosition((Window.getClientWidth() - 456)/2, (Window.getClientHeight() - 522)/2);
					Window.enableScrolling(false);
					folderPopupUc.show();
				}
			}
		});
		
		
		
		this.addMouseOutHandler(new MouseOutHandler() {
			
			@Override
			public void onMouseOut(MouseOutEvent event) {
			    hide();
			}
		});
        
        lblCreateCollection.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (!AppClientFactory.isAnonymous()){
					Map<String, String> params= new HashMap<String, String>();
					params.put("myCollection", "true");
					AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.COLLECTION,params);
				}
			}
		});
        
        lblCreateAsseement.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (!AppClientFactory.isAnonymous()){
//					Map<String, String> params= new HashMap<String, String>();
//					params.put("myCollection", "true");
//					params.put("type", "assessment");
//					AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.COLLECTION,params);
					Window.open(AppClientFactory.loggedInUser.getSettings().getAssessementEndPoint(), "_blank", "");
				}
			}
		});
        
        lblEditMyCollections.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (!AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.SHELF))
					AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.SHELF);
			}
		});
	}
	
	@Override
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return addDomHandler(handler, MouseOutEvent.getType());
	}
	
	public void createFolderInParent(String folderName,
			final String parentId, final HashMap<String, String> params) {
		boolean addToShelf = false;
		if(parentId.isEmpty()) {
			addToShelf = true;
		}
		AppClientFactory.getInjector().getfolderService().createFolder(folderName, parentId, addToShelf, new SimpleAsyncCallback<FolderDo>() {
			@Override
			public void onSuccess(FolderDo folderDo) {
				folderDo.setType("folder");
//				AppClientFactory.fireEvent(new UpdateFolderItemEvent(folderDo, parentId, params));
				AppClientFactory.fireEvent(new RefreshFolderItemEvent(folderDo, RefreshFolderType.INSERT, params));
				AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.SHELF,params);
			}
		});
	}	
}