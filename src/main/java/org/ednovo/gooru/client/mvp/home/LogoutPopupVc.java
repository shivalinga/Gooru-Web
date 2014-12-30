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

import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.classpages.event.ClearClasspageListEvent;
import org.ednovo.gooru.client.mvp.home.event.SetTexasAccountEvent;
import org.ednovo.gooru.client.mvp.home.event.SetTexasPlaceHolderEvent;
import org.ednovo.gooru.client.mvp.home.library.events.StandardPreferenceSettingEvent;
import org.ednovo.gooru.client.mvp.search.event.SetButtonEvent;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderZIndexEvent;
import org.ednovo.gooru.client.uc.AppPopUp;
import org.ednovo.gooru.client.uc.BlueButtonUc;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.user.UserDo;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.http.client.URL;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Search Team
 *
 */
public class LogoutPopupVc extends Composite{

	public AppPopUp appPopUp;

	@UiField
	Anchor cancelAnr;

	@UiField
	BlueButtonUc okBtnUc;
	
	@UiField Label lblClassDismissed;
	@UiField FlowPanel buttonContainer;
	
//	private static final String HEAR_THE_BELL_TEXT = i18n.GL0188;

	private static LogoutPopupVcUiBinder uiBinder = GWT.create(LogoutPopupVcUiBinder.class);

	interface LogoutPopupVcUiBinder extends UiBinder<Widget, LogoutPopupVc> {
	}

	 private MessageProperties i18n = GWT.create(MessageProperties.class);
	 
	/**
	 * Class constructor , get confirm logout popup
	 */
	public LogoutPopupVc() {
		initWidget(uiBinder.createAndBindUi(this));
		appPopUp = new AppPopUp();
		appPopUp.setStyleName("removeResourcePopup");
		appPopUp.setContent(i18n.GL0188(), uiBinder.createAndBindUi(this));
		
		lblClassDismissed.setText(i18n.GL0189());
		lblClassDismissed.getElement().setId("lblClassDismissed");
		lblClassDismissed.getElement().setAttribute("alt",i18n.GL0189());
		lblClassDismissed.getElement().setAttribute("title",i18n.GL0189());
		
		okBtnUc.setText(i18n.GL0190());
		okBtnUc.getElement().setId("btnOk");
		okBtnUc.getElement().setAttribute("alt",i18n.GL0190());
		okBtnUc.getElement().setAttribute("title",i18n.GL0190());
		
		cancelAnr.setText(i18n.GL0142());
		cancelAnr.getElement().setId("lnkCancel");
		cancelAnr.getElement().setAttribute("alt",i18n.GL0142());
		cancelAnr.getElement().setAttribute("title",i18n.GL0142());
		
		buttonContainer.getElement().setId("fpnlButtonContainer");
		
		appPopUp.show();
		appPopUp.center();
		
		
	}

	/**
	 * Cancel the logout and hide logout popup
	 * @param clickEvent instance of {@link ClickEvent}
	 */
	@UiHandler("cancelAnr")
	public void onCancelClick(ClickEvent clickEvent) {
		appPopUp.hide();
		
		Window.enableScrolling(true);
        AppClientFactory.fireEvent(new SetHeaderZIndexEvent(0, true));
	}

	/**
	 * Logout from signed user and makes as a anonymous user 
	 * @param clickEvent instance of {@link ClickEvent}
	 */
	@UiHandler("okBtnUc")
	public void userLogout(ClickEvent clickEvent) {
//		StringUtil.clearCookies("google-access-token", "/", ".www.goorulearning.org");
//		StringUtil.clearCookies("google-refresh-token", "/", ".www.goorulearning.org");
		AppClientFactory.getInjector().getAppService().v2Signout(new SimpleAsyncCallback<UserDo>() {

			@Override
			public void onSuccess(UserDo result) {
				
			    String premiumAccountUserName = AppClientFactory.getLoggedInUser().getUsername();
				
				AppClientFactory.fireEvent(new ClearClasspageListEvent());
				
				Window.enableScrolling(true);
		        AppClientFactory.fireEvent(new SetHeaderZIndexEvent(0, true));
				AppClientFactory.setLoggedInUser(result);
				AppClientFactory.getInjector().getWrapPresenter().get().setLoginData(result);
				AppClientFactory.fireEvent(new SetButtonEvent());
				//AppClientFactory.resetPlace();
				if(premiumAccountUserName.equalsIgnoreCase("TexasTeacher")) {
					AppClientFactory.fireEvent(new SetTexasAccountEvent("failure"));
					AppClientFactory.fireEvent(new SetTexasPlaceHolderEvent(false));
				}else{
					AppClientFactory.fireEvent(new SetTexasAccountEvent("success"));
					AppClientFactory.fireEvent(new SetTexasPlaceHolderEvent(true));
				}
				AppClientFactory.fireEvent(new StandardPreferenceSettingEvent(null));

				if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.RESOURCE_SEARCH) 
						|| AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.COLLECTION_SEARCH)){
					Map<String, String> map = StringUtil.splitQuery(Window.Location.getHref());
					if(map.containsKey("query"))
					{
						String queryVal = map.get("query");
						queryVal = queryVal.replaceAll("%5C1", "&");
						try
						{
						queryVal = URL.decodeQueryString(queryVal);
						}
						catch(Exception ex)
						{
							
						}
						map.put("query", queryVal);
					}
					if(map.containsKey("flt.subjectName"))
					{
						String subjectNameVal = map.get("flt.subjectName");
						subjectNameVal = subjectNameVal.replaceAll("%5C1", "&");
						try
						{
							subjectNameVal = URL.decodeQueryString(subjectNameVal);
						}
						catch(Exception ex)
						{
							
						}
						subjectNameVal = subjectNameVal.replace("+", " ");
						map.put("flt.subjectName", subjectNameVal);
					}
					if(map.containsKey("flt.rating"))
					{
						String ratingsVal = map.get("flt.rating");
						ratingsVal = ratingsVal.replaceAll("%252C", ",");
						try
						{
							ratingsVal = URL.decodeQueryString(ratingsVal);
						}
						catch(Exception ex)
						{
							
						}
						map.put("flt.rating", ratingsVal);
					}
					if(map.containsKey("flt.grade"))
					{
						String gradeVal = map.get("flt.grade");
						gradeVal = gradeVal.replaceAll("%252C", ",");
						try
						{
							gradeVal = URL.decodeQueryString(gradeVal);
						}
						catch(Exception ex)
						{
							
						}
						map.put("flt.grade", gradeVal);
					}
					if(map.containsKey("flt.cfAccessMode"))
					{
						String accessMode = map.get("flt.cfAccessMode");
						accessMode = accessMode.replaceAll("%252C", ",");
						try
						{
							accessMode = URL.decodeQueryString(accessMode);
						}
						catch(Exception ex)
						{
							
						}
						map.put("flt.cfAccessMode", accessMode);
					}
					if(map.containsKey("flt.publisher"))
					{
						String publisher = map.get("flt.publisher");
						publisher = publisher.replaceAll("%252C", ",");
						try
						{
							publisher = URL.decodeQueryString(publisher);
						}
						catch(Exception ex)
						{
							
						}
						map.put("flt.publisher", publisher);
					}
					if(map.containsKey("flt.aggregator"))
					{
						String aggregator = map.get("flt.aggregator");
						aggregator = aggregator.replaceAll("%252C", ",");
						try
						{
							aggregator = URL.decodeQueryString(aggregator);
						}
						catch(Exception ex)
						{
							
						}
						map.put("flt.aggregator", aggregator);
					}
					if(map.containsKey("flt.standard"))
					{
						String standard = map.get("flt.standard");
						standard = standard.replaceAll("%252C", ",");
						try
						{
							standard = URL.decodeQueryString(standard);
						}
						catch(Exception ex)
						{
							
						}
						map.put("flt.standard", standard);
					}
					if(map.containsKey("category"))
					{
						String category = map.get("category");
						category = category.replaceAll("%252C", ",");
						try
						{
							category = URL.decodeQueryString(category);
						}
						catch(Exception ex)
						{
							
						}
						map.put("category", category);
					}
					map.remove("callback");
					map.remove("type");
					map.remove("userName");
					map.remove("account");
					AppClientFactory.getPlaceManager().revealPlace(AppClientFactory.getCurrentPlaceToken(), map);
				}else{
					AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.HOME);
				}
				appPopUp.hide();
			}

		});

	}

}
