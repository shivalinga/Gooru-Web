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
package org.ednovo.gooru.client.mvp.home.presearchstandards;

import java.util.ArrayList;

import org.ednovo.gooru.client.mvp.home.HeaderUc;
import org.ednovo.gooru.client.uc.HTMLEventPanel;
import org.ednovo.gooru.client.uc.StandardPreferenceTooltip;
import org.ednovo.gooru.client.uc.tooltip.BrowseStandardsTooltip;
import org.ednovo.gooru.client.uc.tooltip.ToolTip;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.code.StandardsLevel1DO;
import org.ednovo.gooru.shared.model.code.StandardsLevel2DO;
import org.ednovo.gooru.shared.model.code.StandardsLevel3DO;
import org.ednovo.gooru.shared.model.code.StandardsLevel4DO;
import org.ednovo.gooru.shared.model.content.CollectionDo;

import com.gargoylesoftware.htmlunit.javascript.host.MouseEvent;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PopupViewWithUiHandlers;

/**
 * @author Search Team
 *
 */
public class AddStandardsPreSearchView extends PopupViewWithUiHandlers<AddStandardsPreSearchUiHandlers> implements IsAddStandardsPreSearchView {
	
	
	ToolTip toolTip;
	
	@UiField Button commonStandards1,texasKnowledge1,ngss1,californiaStandards1,addBtn;
	
	@UiField HTMLPanel addStandardsPanel,levelOneStandards,levelTwoStandards,levelThreeStandards,levelFourStandards;
	
	private boolean isCheckedValue;
	
	private boolean scienceCodeVal, instantVal = false;

	private String scienceStrCode = "";
	private CollectionDo collectionDo;
	
	private PopupPanel toolTipPopupPanel=new PopupPanel();
	
//	public static final String GRADE_INFO = MessageProperties.GL0320;

//	public static final String COURSE_INFO = MessageProperties.GL0321;

	private static final String GOORU_UID = "gooruuid";
	
	String selectedCodeVal = "";
	
	Integer selectedCodeId = 0;
	String selectedCodeDesc = "";
	private static AddStandardsPreSearchViewUiBinder uiBinder = GWT.create(AddStandardsPreSearchViewUiBinder.class);
	
	static MessageProperties i18n = GWT.create(MessageProperties.class);
	
	private static final String TITLE_THIS_COLLECTION = i18n.GL0322();
	
	private static String CONFIRM_MESSAGE = i18n.GL1490()+i18n.GL_SPL_EXCLAMATION();
	
	boolean isHavingBadWords;
	
	final StandardPreferenceTooltip standardPreferenceTooltip=new StandardPreferenceTooltip();
	
	private boolean isCCSSAvailable =false;
	private boolean isNGSSAvailable =false;
	private boolean isTEKSAvailable =false;
	private boolean isCAAvailable =false;
	
	private boolean isBrowseStandardsToolTip = false;
	
	@UiTemplate("AddStandardsPreSearchView.ui.xml")
	interface AddStandardsPreSearchViewUiBinder extends UiBinder<Widget, AddStandardsPreSearchView> {
	}
	BrowseStandardsTooltip browseStandardsTooltip = new BrowseStandardsTooltip(i18n.GL3050(),i18n.GL0192());

	/**
	 * Class constructor 
	 * @param eventBus {@link EventBus}
	 */
	@Inject
	public AddStandardsPreSearchView(EventBus eventBus) {
		super(eventBus);
		/*this.res = CollectionCBundle.INSTANCE;
		res.css().ensureInjected();*/


		addBtn= new Button();
		commonStandards1 = new Button();
		texasKnowledge1 = new Button();
		ngss1 = new Button();
		californiaStandards1 = new Button();
	
	
		AddStandardsBundle.INSTANCE.css().ensureInjected();

		
		commonStandards1.setStyleName("primary");
		commonStandards1.addStyleName(AddStandardsBundle.INSTANCE.css().btnStandardsStyle());
	
		commonStandards1.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				if(isCCSSAvailable==false){
					browseStandardsTooltip.show();
					browseStandardsTooltip.setPopupPosition(commonStandards1.getAbsoluteLeft()+3, commonStandards1.getAbsoluteTop()+33);
					browseStandardsTooltip.getConfirmationPanel().getElement().getStyle().setLeft(0, Unit.PX);
					browseStandardsTooltip.getElement().getStyle().setZIndex(999999);
					isBrowseStandardsToolTip= true;
				}
			}
		});
		Event.addNativePreviewHandler(new NativePreviewHandler() {
	        public void onPreviewNativeEvent(NativePreviewEvent event) {
	        	hideBrowseStandardsPopup(event);
	          }
	    });
	}
	
	/** (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.home.presearchstandards.IsAddStandardsPreSearchView#loadData()
	 */
	@Override
	public void loadData()
	{
		addBtn.setEnabled(false);
		addBtn.removeStyleName("primary");
		addBtn.addStyleName("secondary");
	

	}
	
	/**
	 * To set the Standards
	 * @see org.ednovo.gooru.client.mvp.home.presearchstandards.IsAddStandardsPreSearchView#SetData(org.ednovo.gooru.shared.model.code.StandardsLevel1DO, int)
	 */
	@Override
	public void SetData(final StandardsLevel1DO levelOneData, int valArr)
	{
		instantVal = false;
		HTMLEventPanel levelOneStandardsInner = new HTMLEventPanel("");
		levelOneStandardsInner.setStyleName(AddStandardsBundle.INSTANCE.css().dropMenu());
		levelOneStandardsInner.getElement().setInnerHTML(levelOneData.getLabel());
		levelOneStandardsInner.getElement().setAttribute("id", levelOneData.getCodeId().toString());
		if(levelOneData.getCode()!= null)
		{
		if(levelOneData.getCode().equalsIgnoreCase("CA.SCI") && !scienceCodeVal)
		{
			scienceStrCode = levelOneData.getCodeId().toString();
			instantVal = true;
			scienceCodeVal = true;	
		}
		}
		if(!scienceStrCode.isEmpty())
		{			
			levelOneStandardsInner.getElement().setAttribute("dupid", scienceStrCode);
		}
		if(valArr==0)
		{
			levelOneStandardsInner.addStyleName(AddStandardsBundle.INSTANCE.css().dropMenuSelected());	
		}
		levelOneStandardsInner.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				HTMLEventPanel clickedElement = (HTMLEventPanel)event.getSource();
				String codeStandardsVal = clickedElement.getElement().getAttribute("id");
				if(levelOneData.getCode() != null)
				{
				if(levelOneData.getCode().equalsIgnoreCase("CA.SCI"))
				{
					codeStandardsVal = clickedElement.getElement().getAttribute("dupid")+","+clickedElement.getElement().getAttribute("id");
					getFirstLevelObjects("1",codeStandardsVal);
				}
				else
				{
					getFirstLevelObjects("1",codeStandardsVal);
				}
				}
				else
				{
					getFirstLevelObjects("1",codeStandardsVal);
				}
				
				for(int l=0;l<levelOneStandards.getWidgetCount();l++)
				{
					levelOneStandards.getWidget(l).setStyleName(AddStandardsBundle.INSTANCE.css().dropMenu());
				}
				clickedElement.addStyleName(AddStandardsBundle.INSTANCE.css().dropMenuSelected());	
			}
		});

		if(!instantVal)
		{
		levelOneStandards.add(levelOneStandardsInner.asWidget());
		}
		
		if(valArr == 0)
		{
		for(int i=0;i<levelOneData.getNode().size();i++)
		{
			HTMLEventPanel levelOneStandardsInner2 = new HTMLEventPanel("");
			levelOneStandardsInner2.setStyleName(AddStandardsBundle.INSTANCE.css().dropMenu());
			levelOneStandardsInner2.getElement().setInnerHTML(levelOneData.getNode().get(i).getLabel());
			levelOneStandardsInner2.getElement().setAttribute("id", levelOneData.getNode().get(i).getCodeId().toString());
			if(i==0)
			{
				levelOneStandardsInner2.addStyleName(AddStandardsBundle.INSTANCE.css().dropMenuSelected());				
			}
			levelOneStandardsInner2.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					HTMLEventPanel clickedElement = (HTMLEventPanel)event.getSource();
					String codeStandardsVal = clickedElement.getElement().getAttribute("id");
					getSecondLevelObjects("2",codeStandardsVal);
					for(int l=0;l<levelTwoStandards.getWidgetCount();l++)
					{
						levelTwoStandards.getWidget(l).setStyleName(AddStandardsBundle.INSTANCE.css().dropMenu());
					}
					clickedElement.addStyleName(AddStandardsBundle.INSTANCE.css().dropMenuSelected());	
					
				}
			});
			levelTwoStandards.add(levelOneStandardsInner2.asWidget());
			if(i==0)
			{
			for(int j=0;j<levelOneData.getNode().get(i).getNode().size();j++)
			{
				HTMLEventPanel levelOneStandardsInner3 = new HTMLEventPanel("");
				levelOneStandardsInner3.setStyleName(AddStandardsBundle.INSTANCE.css().dropMenu());
				levelOneStandardsInner3.getElement().setInnerHTML(levelOneData.getNode().get(i).getNode().get(j).getLabel());
				levelOneStandardsInner3.getElement().setAttribute("id", levelOneData.getNode().get(i).getNode().get(j).getCodeId().toString());
				if(j==0)
				{
					levelOneStandardsInner3.addStyleName(AddStandardsBundle.INSTANCE.css().dropMenuSelected());				
				}
				levelOneStandardsInner3.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						HTMLEventPanel clickedElement = (HTMLEventPanel)event.getSource();
						String codeStandardsVal = clickedElement.getElement().getAttribute("id");
						getThirdLevelObjects("3",codeStandardsVal);
						for(int l=0;l<levelThreeStandards.getWidgetCount();l++)
						{
							levelThreeStandards.getWidget(l).setStyleName(AddStandardsBundle.INSTANCE.css().dropMenu());
						}
						clickedElement.addStyleName(AddStandardsBundle.INSTANCE.css().dropMenuSelected());
						
					}
				});
				levelThreeStandards.add(levelOneStandardsInner3.asWidget());
				if(j==0)
				{
				for(int k=0;k<levelOneData.getNode().get(i).getNode().get(j).getNode().size();k++)
				{
					HTMLEventPanel levelOneStandardsInner4Outer = new HTMLEventPanel("");
					HTMLEventPanel levelOneStandardsInner4 = new HTMLEventPanel("");
					HTMLEventPanel levelOneStandardsInner4Code = new HTMLEventPanel("");
					levelOneStandardsInner4Outer.setStyleName(AddStandardsBundle.INSTANCE.css().dropMenu());
					levelOneStandardsInner4Code.getElement().setInnerHTML(levelOneData.getNode().get(i).getNode().get(j).getNode().get(k).getCode());
					levelOneStandardsInner4.getElement().setInnerHTML(levelOneData.getNode().get(i).getNode().get(j).getNode().get(k).getLabel());
					final String codeVal = levelOneData.getNode().get(i).getNode().get(j).getNode().get(k).getCode();
					final Integer codeIdVal = levelOneData.getNode().get(i).getNode().get(j).getNode().get(k).getCodeId();
					final String codeDesc = levelOneData.getNode().get(i).getNode().get(j).getNode().get(k).getLabel();
				
					levelOneStandardsInner4Outer.add(levelOneStandardsInner4Code);
					levelOneStandardsInner4Outer.add(levelOneStandardsInner4);	
					levelOneStandardsInner4Outer.getElement().setAttribute("id", levelOneData.getNode().get(i).getNode().get(j).getNode().get(k).getCodeId().toString());
					levelOneStandardsInner4Outer.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							// TODO Auto-generated method stub
							HTMLEventPanel clickedObject = (HTMLEventPanel)event.getSource();
							selectedCodeVal = codeVal;
							selectedCodeId=codeIdVal;
							selectedCodeDesc = codeDesc;
							addBtn.setEnabled(true);
							addBtn.removeStyleName("secondary");
							addBtn.addStyleName("primary");
							for(int l=0;l<levelFourStandards.getWidgetCount();l++)
							{
								levelFourStandards.getWidget(l).setStyleName(AddStandardsBundle.INSTANCE.css().dropMenu());
							}
							clickedObject.addStyleName(AddStandardsBundle.INSTANCE.css().dropMenuSelected());		
							HeaderUc.setStandardsCode(codeVal,codeIdVal,codeDesc);
						}
					});
					levelFourStandards.add(levelOneStandardsInner4Outer.asWidget());
				}
				}
			}
			}
		}
	}
		//setEnableStandardButtons(isCCSSAvailable,isNGSSAvailable,isTEKSAvailable,isCAAvailable);
	//	appPopUp.getElement().setAttribute("style", "width:1000px;height:599px;z-index:99999;visibility: visible;position: absolute;left: 0 !important;right: 0 !important;margin:auto;top:0 !important;bottom:0 !important;");
	}
	/**
	 * To get the First level Objects
	 * @param levelOrder
	 * @param standardCodeSelected
	 */
	public void getFirstLevelObjects(String levelOrder, String standardCodeSelected)
	{
		selectedCodeVal = "";
		selectedCodeId = 0;
		addBtn.setEnabled(false);
		addBtn.removeStyleName("primary");
		addBtn.addStyleName("secondary");
		getUiHandlers().getFirstLevelObjects(levelOrder,standardCodeSelected);
	}
	/**
	 * To get the Second level Objects
	 * @param levelOrder
	 * @param standardCodeSelected
	 */
	public void getSecondLevelObjects(String levelOrder, String standardCodeSelected)
	{
		selectedCodeVal = "";
		selectedCodeId = 0;
		addBtn.setEnabled(false);
		addBtn.removeStyleName("primary");
		addBtn.addStyleName("secondary");
		getUiHandlers().getSecondLevelObjects(levelOrder,standardCodeSelected);
	}
	/**
	 * To get the Third level Objects
	 * @param levelOrder
	 * @param standardCodeSelected
	 */
	public void getThirdLevelObjects(String levelOrder, String standardCodeSelected)
	{
		selectedCodeVal = "";
		selectedCodeId = 0;
		addBtn.setEnabled(false);
		addBtn.removeStyleName("primary");
		addBtn.addStyleName("secondary");
		getUiHandlers().getThirdLevelObjects(levelOrder,standardCodeSelected);
	}
	
	/** (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.home.presearchstandards.IsAddStandardsPreSearchView#loadSecondLevelContianerObjects(java.util.ArrayList)
	 */
	public void loadSecondLevelContianerObjects(ArrayList<StandardsLevel2DO> levelOneData)
	{
		levelTwoStandards.clear();
		levelThreeStandards.clear();
		levelFourStandards.clear();
		
		for(int i=0;i<levelOneData.size();i++)
		{
			try
			{
			HTMLEventPanel levelOneStandardsInner2 = new HTMLEventPanel("");
			levelOneStandardsInner2.setStyleName(AddStandardsBundle.INSTANCE.css().dropMenu());
			levelOneStandardsInner2.getElement().setInnerHTML(levelOneData.get(i).getLabel());
			levelOneStandardsInner2.getElement().setAttribute("id", levelOneData.get(i).getCodeId().toString());
			if(i==0)
			{
				levelOneStandardsInner2.addStyleName(AddStandardsBundle.INSTANCE.css().dropMenuSelected());				
			}
			levelOneStandardsInner2.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					HTMLEventPanel clickedElement = (HTMLEventPanel)event.getSource();
					String codeStandardsVal = clickedElement.getElement().getAttribute("id");
					getSecondLevelObjects("2",codeStandardsVal);
					for(int l=0;l<levelTwoStandards.getWidgetCount();l++)
					{
						levelTwoStandards.getWidget(l).setStyleName(AddStandardsBundle.INSTANCE.css().dropMenu());
					}
					clickedElement.addStyleName(AddStandardsBundle.INSTANCE.css().dropMenuSelected());
					
				}
			});
			levelTwoStandards.add(levelOneStandardsInner2.asWidget());
			if(i==0)
			{
			for(int j=0;j<levelOneData.get(i).getNode().size();j++)
			{
				HTMLEventPanel levelOneStandardsInner3 = new HTMLEventPanel("");
				levelOneStandardsInner3.setStyleName(AddStandardsBundle.INSTANCE.css().dropMenu());
				levelOneStandardsInner3.getElement().setInnerHTML(levelOneData.get(i).getNode().get(j).getLabel());
				levelOneStandardsInner3.getElement().setAttribute("id", levelOneData.get(i).getNode().get(j).getCodeId().toString());
				if(j==0)
				{
					levelOneStandardsInner3.addStyleName(AddStandardsBundle.INSTANCE.css().dropMenuSelected());				
				}
				levelOneStandardsInner3.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						HTMLEventPanel clickedElement = (HTMLEventPanel)event.getSource();
						String codeStandardsVal = clickedElement.getElement().getAttribute("id");
						getThirdLevelObjects("3",codeStandardsVal);
						for(int l=0;l<levelThreeStandards.getWidgetCount();l++)
						{
							levelThreeStandards.getWidget(l).setStyleName(AddStandardsBundle.INSTANCE.css().dropMenu());
						}
						clickedElement.addStyleName(AddStandardsBundle.INSTANCE.css().dropMenuSelected());
						
					}
				});
				levelThreeStandards.add(levelOneStandardsInner3.asWidget());
				if(j==0)
				{
				for(int k=0;k<levelOneData.get(i).getNode().get(j).getNode().size();k++)
				{
					HTMLEventPanel levelOneStandardsInner4Outer = new HTMLEventPanel("");
					HTMLEventPanel levelOneStandardsInner4 = new HTMLEventPanel("");
					HTMLEventPanel levelOneStandardsInner4Code = new HTMLEventPanel("");
					levelOneStandardsInner4Outer.setStyleName(AddStandardsBundle.INSTANCE.css().dropMenu());
					levelOneStandardsInner4Code.getElement().setInnerHTML(levelOneData.get(i).getNode().get(j).getNode().get(k).getCode());
					levelOneStandardsInner4.getElement().setInnerHTML(levelOneData.get(i).getNode().get(j).getNode().get(k).getLabel());
					final String codeVal = levelOneData.get(i).getNode().get(j).getNode().get(k).getCode();
					final Integer codeIdVal = levelOneData.get(i).getNode().get(j).getNode().get(k).getCodeId();
					final String codeDesc = levelOneData.get(i).getNode().get(j).getNode().get(k).getLabel();
					
					levelOneStandardsInner4Outer.add(levelOneStandardsInner4Code);
					levelOneStandardsInner4Outer.add(levelOneStandardsInner4);	
					levelOneStandardsInner4Outer.getElement().setAttribute("id", levelOneData.get(i).getNode().get(j).getNode().get(k).getCodeId().toString());
					levelOneStandardsInner4Outer.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							// TODO Auto-generated method stub
							HTMLEventPanel clickedObject = (HTMLEventPanel)event.getSource();
							selectedCodeVal = codeVal;
							selectedCodeId=codeIdVal;
							selectedCodeDesc = codeDesc;
							addBtn.setEnabled(true);
							addBtn.removeStyleName("secondary");
							addBtn.addStyleName("primary");
							for(int l=0;l<levelFourStandards.getWidgetCount();l++)
							{
								levelFourStandards.getWidget(l).setStyleName(AddStandardsBundle.INSTANCE.css().dropMenu());
							}
							clickedObject.addStyleName(AddStandardsBundle.INSTANCE.css().dropMenuSelected());	
							HeaderUc.setStandardsCode(codeVal,codeIdVal,codeDesc);
						}
					});
					levelFourStandards.add(levelOneStandardsInner4Outer.asWidget());
				}
				}
			}
			}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	/** (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.home.presearchstandards.IsAddStandardsPreSearchView#loadThirdLevelContianerObjects(java.util.ArrayList)
	 */
	public void loadThirdLevelContianerObjects(ArrayList<StandardsLevel3DO> levelOneData)
	{
		levelThreeStandards.clear();
		levelFourStandards.clear();
		
			for(int j=0;j<levelOneData.size();j++)
			{
				try{
				HTMLEventPanel levelOneStandardsInner3 = new HTMLEventPanel("");
				levelOneStandardsInner3.setStyleName(AddStandardsBundle.INSTANCE.css().dropMenu());
				levelOneStandardsInner3.getElement().setInnerHTML(levelOneData.get(j).getLabel());
				levelOneStandardsInner3.getElement().setAttribute("id", levelOneData.get(j).getCodeId().toString());
				if(j==0)
				{
					levelOneStandardsInner3.addStyleName(AddStandardsBundle.INSTANCE.css().dropMenuSelected());				
				}
				levelOneStandardsInner3.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						HTMLEventPanel clickedElement = (HTMLEventPanel)event.getSource();
						String codeStandardsVal = clickedElement.getElement().getAttribute("id");
						getThirdLevelObjects("3",codeStandardsVal);
						for(int l=0;l<levelThreeStandards.getWidgetCount();l++)
						{
							levelThreeStandards.getWidget(l).setStyleName(AddStandardsBundle.INSTANCE.css().dropMenu());
						}
						clickedElement.addStyleName(AddStandardsBundle.INSTANCE.css().dropMenuSelected());
						
					}
				});
				levelThreeStandards.add(levelOneStandardsInner3.asWidget());
				if(j==0)
				{
				for(int k=0;k<levelOneData.get(j).getNode().size();k++)
				{
					HTMLEventPanel levelOneStandardsInner4Outer = new HTMLEventPanel("");
					HTMLEventPanel levelOneStandardsInner4 = new HTMLEventPanel("");
					HTMLEventPanel levelOneStandardsInner4Code = new HTMLEventPanel("");
					levelOneStandardsInner4Outer.setStyleName(AddStandardsBundle.INSTANCE.css().dropMenu());
					levelOneStandardsInner4Code.getElement().setInnerHTML(levelOneData.get(j).getNode().get(k).getCode());
					levelOneStandardsInner4.getElement().setInnerHTML(levelOneData.get(j).getNode().get(k).getLabel());
					final String codeVal = levelOneData.get(j).getNode().get(k).getCode();
					final Integer codeIdVal = levelOneData.get(j).getNode().get(k).getCodeId();
					final String codeDesc = levelOneData.get(j).getNode().get(k).getLabel();
					
					levelOneStandardsInner4Outer.add(levelOneStandardsInner4Code);
					levelOneStandardsInner4Outer.add(levelOneStandardsInner4);	
					levelOneStandardsInner4Outer.getElement().setAttribute("id", levelOneData.get(j).getNode().get(k).getCodeId().toString());
					levelOneStandardsInner4Outer.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							HTMLEventPanel clickedObject = (HTMLEventPanel)event.getSource();
							selectedCodeVal = codeVal;
							selectedCodeId=codeIdVal;
							selectedCodeDesc = codeDesc;
							addBtn.setEnabled(true);
							addBtn.removeStyleName("secondary");
							addBtn.addStyleName("primary");
							for(int l=0;l<levelFourStandards.getWidgetCount();l++)
							{
								levelFourStandards.getWidget(l).setStyleName(AddStandardsBundle.INSTANCE.css().dropMenu());
							}
							clickedObject.addStyleName(AddStandardsBundle.INSTANCE.css().dropMenuSelected());	
							HeaderUc.setStandardsCode(codeVal,codeIdVal,codeDesc);
						}
					});
					levelFourStandards.add(levelOneStandardsInner4Outer.asWidget());
				}
				}
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
				}

	}
	
	/** (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.home.presearchstandards.IsAddStandardsPreSearchView#loadFourthLevelContianerObjects(java.util.ArrayList)
	 */
	public void loadFourthLevelContianerObjects(ArrayList<StandardsLevel4DO> levelOneData)
	{
		levelFourStandards.clear();
		

				for(int k=0;k<levelOneData.size();k++)
				{
					try{
					HTMLEventPanel levelOneStandardsInner4Outer = new HTMLEventPanel("");
					HTMLEventPanel levelOneStandardsInner4 = new HTMLEventPanel("");
					HTMLEventPanel levelOneStandardsInner4Code = new HTMLEventPanel("");
					levelOneStandardsInner4Outer.setStyleName(AddStandardsBundle.INSTANCE.css().dropMenu());
					levelOneStandardsInner4Code.getElement().setInnerHTML(levelOneData.get(k).getCode());
					levelOneStandardsInner4.getElement().setInnerHTML(levelOneData.get(k).getLabel());
					final String codeVal = levelOneData.get(k).getCode();
					final Integer codeIdVal = levelOneData.get(k).getCodeId();
					final String codeDesc = levelOneData.get(k).getLabel();
					
					levelOneStandardsInner4Outer.add(levelOneStandardsInner4Code);
					levelOneStandardsInner4Outer.add(levelOneStandardsInner4);	
					levelOneStandardsInner4Outer.getElement().setAttribute("id", levelOneData.get(k).getCodeId().toString());
					levelOneStandardsInner4Outer.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							// TODO Auto-generated method stub
							HTMLEventPanel clickedObject = (HTMLEventPanel)event.getSource();
							selectedCodeVal = codeVal;
							selectedCodeId=codeIdVal;
							selectedCodeDesc = codeDesc;
							addBtn.setEnabled(true);
							addBtn.removeStyleName("secondary");
							addBtn.addStyleName("primary");
							for(int l=0;l<levelFourStandards.getWidgetCount();l++)
							{
								levelFourStandards.getWidget(l).setStyleName(AddStandardsBundle.INSTANCE.css().dropMenu());
							}
							clickedObject.addStyleName(AddStandardsBundle.INSTANCE.css().dropMenuSelected());	
							HeaderUc.setStandardsCode(codeVal,codeIdVal,codeDesc);
						}
					});
					levelFourStandards.add(levelOneStandardsInner4Outer.asWidget());
				}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
				}
				

	}
	


	/** (non-Javadoc)
	 * @see com.gwtplatform.mvp.client.View#asWidget()
	 */
	@Override
	public Widget asWidget() {
		// TODO Auto-generated method stub
		//collectionTitleTxtBox.setFocus(true);
		return uiBinder.createAndBindUi(this);
	}

	/**(non-Javadoc)
	 * @see org.ednovo.gooru.client.gin.IsView#reset()
	 */
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		//addBtn.setText("Add");
		levelOneStandards.clear();
		levelTwoStandards.clear();
		levelThreeStandards.clear();
		levelFourStandards.clear();
		scienceCodeVal = false;
	}
	
	/** (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.home.presearchstandards.IsAddStandardsPreSearchView#setDefaultCCSS()
	 */
	@Override
	public void setDefaultCCSS() {
		texasKnowledge1.removeStyleName("primary");
		texasKnowledge1.addStyleName("secondary");
		ngss1.removeStyleName("primary");
		ngss1.addStyleName("secondary");
		californiaStandards1.removeStyleName("primary");
		californiaStandards1.addStyleName("secondary");
		commonStandards1.removeStyleName("secondary");
		commonStandards1.addStyleName("primary");
	}

	@Override
	public void onLoad() {
		reset();
		//getUiHandlers().callDefaultStandardsLoad();
		//default load for the widget to be here
		//getUiHandlers().callDefaultStandardsLoad();
		//addBtn.setText("Add");
	}

	/** (non-Javadoc)
	 * @see org.ednovo.gooru.client.gin.IsView#onUnload()
	 */
	@Override
	public void onUnload() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * return the addBtn
	 */
	@Override
	public Button getAddBtn() {
		return addBtn;
	}
	/**
	 * set the addBtn
	 */
	public void setAddBtn(Button addBtn) {
		this.addBtn = addBtn;
	}
	
	/**(non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.home.presearchstandards.IsAddStandardsPreSearchView#setStandardsVal()
	 */
	@Override
	public String setStandardsVal()
	{
		String standardVal = selectedCodeVal;
		return standardVal;
	}
	
	/** (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.home.presearchstandards.IsAddStandardsPreSearchView#setStandardsIdVal()
	 */
	@Override
	public Integer setStandardsIdVal()
	{
		Integer standardVal = selectedCodeId;
		return standardVal;
	}
	
	/**
	 * texasKnowledge1 button ClickEvent
	 * @param click {@link ClickEvent}
	 */
	@UiHandler("texasKnowledge1")
	public void ontexasKnowledgeClick(ClickEvent click){
		if(isTEKSAvailable){
		selectedCodeVal = "";
		selectedCodeId = 0;
		addBtn.setEnabled(false);
		addBtn.removeStyleName("primary");
		addBtn.addStyleName("secondary");
		scienceCodeVal = false;
		instantVal = false;
		commonStandards1.removeStyleName("primary");
		commonStandards1.addStyleName("secondary");
		ngss1.removeStyleName("primary");
		ngss1.addStyleName("secondary");
		californiaStandards1.removeStyleName("primary");
		californiaStandards1.addStyleName("secondary");
		texasKnowledge1.removeStyleName("secondary");
		texasKnowledge1.addStyleName("primary");
		getUiHandlers().loadStateStandards("TEKS");
		}else{
		}
	}
	/**
	 * commonStandards1 button ClickEvent
	 * @param click {@link ClickEvent}
	 */
	@UiHandler("commonStandards1")
	public void oncommonStandardsClick(ClickEvent click){
		if(isCCSSAvailable){
			selectedCodeVal = "";
			selectedCodeId = 0;
			addBtn.setEnabled(false);
			addBtn.removeStyleName("primary");
			addBtn.addStyleName("secondary");
			scienceCodeVal = false;
			instantVal = false;
			texasKnowledge1.removeStyleName("primary");
			texasKnowledge1.addStyleName("secondary");
			ngss1.removeStyleName("primary");
			ngss1.addStyleName("secondary");
			californiaStandards1.removeStyleName("primary");
			californiaStandards1.addStyleName("secondary");
			commonStandards1.removeStyleName("secondary");
			commonStandards1.addStyleName("primary");
			getUiHandlers().loadStateStandards("CCSS");
		}else{
		}
	}
	/**
	 * ngss1 button ClickEvent
	 * @param click {@link ClickEvent}
	 */
	@UiHandler("ngss1")
	public void onngssClick(ClickEvent click){
		if(isNGSSAvailable){
		selectedCodeVal = "";
		selectedCodeId = 0;
		addBtn.setEnabled(false);
		addBtn.removeStyleName("primary");
		addBtn.addStyleName("secondary");
		scienceCodeVal = false;
		instantVal = false;
		texasKnowledge1.removeStyleName("primary");
		texasKnowledge1.addStyleName("secondary");
		commonStandards1.removeStyleName("primary");
		commonStandards1.addStyleName("secondary");
		californiaStandards1.removeStyleName("primary");
		californiaStandards1.addStyleName("secondary");
		ngss1.removeStyleName("secondary");
		ngss1.addStyleName("primary");
		getUiHandlers().loadStateStandards("NGSS");
		}else{
			
		}
	}
	/**
	 * ngss1 button MouseEvent
	 * @param mouseEvent {@link MouseEvent}
	 */
	@UiHandler("ngss1")
	public void onNgssMouseOver(MouseOverEvent mouseEvent){
		if(isNGSSAvailable==false){
			browseStandardsTooltip.show();
			browseStandardsTooltip.setPopupPosition(ngss1.getAbsoluteLeft()+28, ngss1.getAbsoluteTop()+39);
			browseStandardsTooltip.getConfirmationPanel().getElement().getStyle().setLeft(0, Unit.PX);
			browseStandardsTooltip.getElement().getStyle().setZIndex(999999);
			isBrowseStandardsToolTip= true;
		}
	}
	/**
	 * texasKnowledge1 button MouseEvent
	 * @param mouseEvent {@link MouseEvent}
	 */
	@UiHandler("texasKnowledge1")
	public void onTexasMouseOver(MouseOverEvent mouseEvent){
		if(isTEKSAvailable==false){
			browseStandardsTooltip.show();
			browseStandardsTooltip.setPopupPosition(texasKnowledge1.getAbsoluteLeft()+28, texasKnowledge1.getAbsoluteTop()+39);
			browseStandardsTooltip.getConfirmationPanel().getElement().getStyle().setLeft(0, Unit.PX);
			browseStandardsTooltip.getElement().getStyle().setZIndex(999999);
			isBrowseStandardsToolTip= true;
		}
	}
	/**
	 * commonStandards1 button MouseEvent
	 * @param mouseEvent {@link MouseEvent}
	 */
	@UiHandler("commonStandards1")
	public void onCommonMouseOver(MouseOverEvent mouseEvent){
		if(isCCSSAvailable==false){
			browseStandardsTooltip.show();
			browseStandardsTooltip.setPopupPosition(commonStandards1.getAbsoluteLeft()+14, commonStandards1.getAbsoluteTop()+39);
			browseStandardsTooltip.getConfirmationPanel().getElement().getStyle().setLeft(0, Unit.PX);
			browseStandardsTooltip.getElement().getStyle().setZIndex(999999);
			isBrowseStandardsToolTip= true;
		}
	}
	
	/**
	 * californiaStandards1 button MouseEvent
	 * @param mouseEvent {@link MouseEvent}
	 */
	@UiHandler("californiaStandards1")
	public void onCaliforniaMouseOver(MouseOverEvent mouseEvent){
		if(isCAAvailable==false){
			browseStandardsTooltip.show();
			browseStandardsTooltip.setPopupPosition(californiaStandards1.getAbsoluteLeft()+3, californiaStandards1.getAbsoluteTop()+39);
			browseStandardsTooltip.getConfirmationPanel().getElement().getStyle().setLeft(0, Unit.PX);
			browseStandardsTooltip.getElement().getStyle().setZIndex(999999);
			isBrowseStandardsToolTip= true;
		}
	}
	
	/**
	 * californiaStandards1 button ClickEvent
	 * @param clickEvent {@link ClickEvent}
	 */
	@UiHandler("californiaStandards1")
	public void oncaliforniaStandardsClick(ClickEvent clickEvent){
		if(isCAAvailable){
		selectedCodeVal = "";
		selectedCodeId = 0;
		addBtn.setEnabled(false);
		addBtn.removeStyleName("primary");
		addBtn.addStyleName("secondary");
		scienceCodeVal = false;
		instantVal = false;
		texasKnowledge1.removeStyleName("primary");
		texasKnowledge1.addStyleName("secondary");
		commonStandards1.removeStyleName("primary");
		commonStandards1.addStyleName("secondary");
		ngss1.removeStyleName("primary");
		ngss1.addStyleName("secondary");
		californiaStandards1.removeStyleName("secondary");
		californiaStandards1.addStyleName("primary");
		getUiHandlers().loadStateStandards("CA");
		}else{
			
		}
	}

	/** (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.home.presearchstandards.IsAddStandardsPreSearchView#setStandardsDesc()
	 */
	@Override
	public String setStandardsDesc() {
			return selectedCodeDesc;
	}

	/**
	 * To set the Default Styles for TEKS
	 */
	public void setDefaultTEKS() {
		commonStandards1.removeStyleName("primary");
		commonStandards1.addStyleName("secondary");
		ngss1.removeStyleName("primary");
		ngss1.addStyleName("secondary");
		californiaStandards1.removeStyleName("primary");
		californiaStandards1.addStyleName("secondary");
		texasKnowledge1.removeStyleName("secondary");
		texasKnowledge1.addStyleName("primary");
	}
	/**
	 * To set the Default Styles for NGSS
	 */
	public void setDefaultNGSS() {
		texasKnowledge1.removeStyleName("primary");
		texasKnowledge1.addStyleName("secondary");
		commonStandards1.removeStyleName("primary");
		commonStandards1.addStyleName("secondary");
		californiaStandards1.removeStyleName("primary");
		californiaStandards1.addStyleName("secondary");
		ngss1.removeStyleName("secondary");
		ngss1.addStyleName("primary");
	}
	/**
	 * To set the Default Styles for CA
	 */
	public void setDefaultCA() {
		texasKnowledge1.removeStyleName("primary");
		texasKnowledge1.addStyleName("secondary");
		commonStandards1.removeStyleName("primary");
		commonStandards1.addStyleName("secondary");
		ngss1.removeStyleName("primary");
		ngss1.addStyleName("secondary");
		californiaStandards1.removeStyleName("secondary");
		californiaStandards1.addStyleName("primary");
	}
	
	/** (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.home.presearchstandards.IsAddStandardsPreSearchView#setEnableStandardButtons(boolean, boolean, boolean, boolean)
	 */
	@Override
	public void setEnableStandardButtons(boolean isCCSSAvailable1,
			boolean isTEKSAvailable1, boolean isNGSSAvailable1,
			boolean isCAAvailable1) {
		this.isCCSSAvailable = isCCSSAvailable1;
		this.isNGSSAvailable = isNGSSAvailable1;
		this.isTEKSAvailable = isTEKSAvailable1;
		this.isCAAvailable = isCAAvailable1;
		
		if(isCCSSAvailable == true){
			commonStandards1.getElement().getStyle().clearColor();
			commonStandards1.setStyleName("primary");
			commonStandards1.addStyleName(AddStandardsBundle.INSTANCE.css().btnStandardsStyle());
			commonStandards1.getElement().removeClassName("disabled");
		}else{
			commonStandards1.getElement().getStyle().setColor("#999");
			commonStandards1.getElement().addClassName("disabled");
		}
		if(isNGSSAvailable == true){
			ngss1.getElement().getStyle().clearColor();
			ngss1.getElement().removeClassName("disabled");
		}else{
			ngss1.getElement().getStyle().setColor("#999");
			ngss1.getElement().addClassName("disabled");
		}
		
		if(isTEKSAvailable == true){
			texasKnowledge1.getElement().getStyle().clearColor();
			texasKnowledge1.getElement().removeClassName("disabled");
		}else{
			texasKnowledge1.getElement().getStyle().setColor("#999");
			texasKnowledge1.getElement().addClassName("disabled");
		}
		
		if(isCAAvailable == true){
			californiaStandards1.getElement().getStyle().clearColor();
			californiaStandards1.getElement().removeClassName("disabled");
		}else{
			californiaStandards1.getElement().getStyle().setColor("#999");
			californiaStandards1.getElement().addClassName("disabled");
		}
		
	}

	/** (non-Javadoc)
	 * @see org.ednovo.gooru.client.mvp.home.presearchstandards.IsAddStandardsPreSearchView#setStandardsStyles(java.lang.String)
	 */
	@Override
	public void setStandardsStyles(String standardVal) {

		if(standardVal.equalsIgnoreCase("CCSS")){
			setDefaultCCSS();
		}else if(standardVal.equalsIgnoreCase("TEKS")){
			setDefaultTEKS();
		}else if(standardVal.equalsIgnoreCase("NGSS")){
			setDefaultNGSS();
		}else if(standardVal.equalsIgnoreCase("CA")){
			setDefaultCA();
		}
	}
	
	/**
	 * To hide Browse Standards Popup 
	 * @param event {@link NativePreviewEvent}
	 */
	public void hideBrowseStandardsPopup(NativePreviewEvent event){
		try{
			if(event.getTypeInt()==Event.ONMOUSEOVER){
				Event nativeEvent = Event.as(event.getNativeEvent());
				boolean target=eventTargetsPopup(nativeEvent);
				if(!target)
				{
					if(isBrowseStandardsToolTip){
						browseStandardsTooltip.hide();
					}
				}
			}
		}catch(Exception ex){ex.printStackTrace();}
	}
	
	/**
	 * @param event {@link NativeEvent}
	 * @return false	{@link Boolean}
	 */
	private boolean eventTargetsPopup(NativeEvent event) {
		EventTarget target = event.getEventTarget();
		if (Element.is(target)) {
			try{
				return browseStandardsTooltip.getElement().isOrHasChild(Element.as(target));
			}catch(Exception ex){}
		}
		return false;
	}
	
	/**
	 * @return the addStandardsPanel
	 */
	@Override
	public HTMLPanel getAddStandardsPanel() {
		return addStandardsPanel;
	}
	
}
