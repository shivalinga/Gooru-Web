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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.AppInjector;
import org.ednovo.gooru.client.mvp.home.HomeCBundle;
import org.ednovo.gooru.client.mvp.play.resource.style.PlayerStyleBundle;
import org.ednovo.gooru.client.mvp.search.event.DisplayNoCollectionEvent;
import org.ednovo.gooru.client.uc.BrowserAgent;
import org.ednovo.gooru.client.uc.UcCBundle;
import org.ednovo.gooru.shared.model.user.UserDo;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.gwtplatform.mvp.client.DelayedBindRegistry;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

public class GooruEntry implements EntryPoint {

	private final AppInjector appInjector = GWT.create(AppInjector.class);
	
	private HandlerRegistration nativePreviewHandlerRegistration;
	
	private static final String GOORU_USER_INACTIVE = "in-active";
	
	public void onModuleLoad() {

		DelayedBindRegistry.bind(appInjector);

		String device = BrowserAgent.returnFormFactorWithSizeView();
		String size[] = device.split("-");
		if (size[0].equalsIgnoreCase("mobile") || size[0].equalsIgnoreCase("iphone")){
			Map<String, String> params = new HashMap<String, String>();
			params.put("device", size[0]);
			params.put("size", size[1]);
			appInjector.getPlaceManager().revealPlace(new PlaceRequest(PlaceTokens.DEVICE_NOT_SUPPORTED));
//			appInjector.getEventBus().fireEvent(new SetDeviceDetailsEvent(size[0], size[1]));
		}else{
			appInjector.getAppService().getLoggedInUser(new SimpleAsyncCallback<UserDo>() {
				@Override
				public void onSuccess(UserDo loggedInUser) {
					AppClientFactory.setLoggedInUser(loggedInUser);
//					loadCssJsFiles();
					UcCBundle.INSTANCE.css().ensureInjected();
					HomeCBundle.INSTANCE.css().ensureInjected();
					AppClientFactory.getInjector().getWrapPresenter().get().setLoginData(loggedInUser);
					appInjector.getPlaceManager().revealCurrentPlace();
					AppClientFactory.setProtocol(getHttpOrHttpsProtocol());
					registerWindowEvents();
				}
	
//				@Override
//				public void onFailure(Throwable caught) {
//					appInjector.getPlaceManager().revealPlace(new PlaceRequest(PlaceTokens.ERROR));
//				}
				
			});
			AppClientFactory.setAppGinjector(appInjector);
		}
		StyleInjector.injectAtEnd("@media (min-width: 240px) and (max-width: 767px) {" + PlayerStyleBundle.INSTANCE.getPlayerMobileStyle().getText() + "}");
		StyleInjector.injectAtEnd("@media (min-width: 768px) and (max-width: 991px) {" + PlayerStyleBundle.INSTANCE.getPlayerTabletStyle().getText() + "}");
		PlayerStyleBundle.INSTANCE.getPlayerStyleResource().ensureInjected();
	}
	
	/* 
	 * Registering the native events.
	 * 
	 * */
	
	private void registerWindowEvents(){
		nativePreviewHandlerRegistration = Event.addNativePreviewHandler(new NativePreviewHandler() {
			
			public void onPreviewNativeEvent(NativePreviewEvent event) {
				if(event.getTypeInt()==Event.ONMOUSEOVER){
					if(AppClientFactory.getUserStatus()!=null){
						Cookies.setCookie("gooru-active-user", AppClientFactory.getUserStatus(),getCurrentDateAndTime()); 
					}
					if((AppClientFactory.getUserStatus()==null || AppClientFactory.getUserStatus().trim().equals(GOORU_USER_INACTIVE)) &&  AppClientFactory.isUserflag() && (AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.COLLECTION_PLAY) || AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.PREVIEW_PLAY))){
					}
					else if((AppClientFactory.getUserStatus()==null || AppClientFactory.getUserStatus().trim().equals(GOORU_USER_INACTIVE)) &&  AppClientFactory.isUserflag() && AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.RESOURCE_PLAY)){
						
					}
					else if((AppClientFactory.getUserStatus()==null || AppClientFactory.getUserStatus().trim().equals(GOORU_USER_INACTIVE)) && AppClientFactory.isUserflag()){
						AppClientFactory.setUserflag(false)	;
						if(AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.HOME) || AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.RESOURCE_SEARCH) || AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.COLLECTION_SEARCH) || AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.STUDY)){
							userLoggedOutheader();
						}
						else{
							redirectToLandingPage();
						}
						
					}
					
				}
			}
		});
	}
	
	/* 
	 * If user logged out by staying on Discover or Study page on any one of the tab,
	 * this method will be called and header will reset on other tabs. 
	 * 
	 * */
	
	protected Date getCurrentDateAndTime() {
		Date date = new Date();
		Date updatedDate = new Date((date.getTime() + (1000 * 60 * 60 * 24)));//(1000 * 60 * 60 * 24)=24 hrs. **** 120060 = 2 mins
		return updatedDate;
	}

	private void userLoggedOutheader(){
		appInjector.getAppService().getLoggedInUser(new SimpleAsyncCallback<UserDo>() {
			@Override
			public void onSuccess(UserDo loggedInUser) {
				AppClientFactory.setLoggedInUser(loggedInUser);
				UcCBundle.INSTANCE.css().ensureInjected();
				HomeCBundle.INSTANCE.css().ensureInjected();
				AppClientFactory.getInjector().getWrapPresenter().get().setLoginData(loggedInUser);
			}

//			@Override
//			public void onFailure(Throwable caught) {
//				appInjector.getPlaceManager().revealPlace(new PlaceRequest(PlaceTokens.ERROR));
//			}
		});
	}
	
	
	/* 
	 * If user logged out on any one of the tab,
	 * this method will be called to redirect to landing page on other tabs 
	 *  and Log-in pop up will be invoked.
	 *  
	 * */
	
	private void redirectToLandingPage(){
		appInjector.getAppService().getLoggedInUser(new SimpleAsyncCallback<UserDo>() {
			@Override
			public void onSuccess(UserDo loggedInUser) {
				AppClientFactory.setLoggedInUser(loggedInUser);
				UcCBundle.INSTANCE.css().ensureInjected();
				HomeCBundle.INSTANCE.css().ensureInjected();
				AppClientFactory.getInjector().getWrapPresenter().get().setLoginData(loggedInUser);
				if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.STUDENT)){
					
				}else if(AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.SHELF)){
					AppClientFactory.fireEvent(new DisplayNoCollectionEvent());
				}else{
					Map<String, String> params = new HashMap<String,String>();
					params.put("loginEvent", "true");
					appInjector.getPlaceManager().revealPlace(PlaceTokens.HOME, params);
				}
                //	appInjector.getPlaceManager().redirectPlace(PlaceTokens.HOME); 
//				loginPopupTimer.schedule(LOGIN_POPUP_DELAY_TIME);
			}

//			@Override
//			public void onFailure(Throwable caught) {
//				appInjector.getPlaceManager().revealPlace(new PlaceRequest(PlaceTokens.ERROR));
//			}
		});
	}
	
	Timer loginPopupTimer = new Timer() {
		@Override
		public void run() {
			
//			Window.Location.reload();
		}
	};
	
	public String getHttpOrHttpsProtocol() {
		return Window.Location.getProtocol();
	}
	
	private void loadCssJsFiles() {
		String cdnEndPoint = AppClientFactory.getLoggedInUser().getSettings().getCdnEndPoint();
		BrowserAgent.loadCssFile(cdnEndPoint+"/css/gooru.css?r=59","css");
		BrowserAgent.loadCssFile(cdnEndPoint+"/css/gooru-global.css?r=59","css");
		BrowserAgent.loadCssFile(cdnEndPoint+"/scripts/tinymce/tinymce/jscripts/tiny_mce/plugins/asciimath/js/ASCIIMathMLwFallback.js","js");
		BrowserAgent.loadCssFile(cdnEndPoint+"/scripts/tinymce/tinymce/jscripts/tiny_mce/tiny_mce.js","js");
		BrowserAgent.loadCssFile(cdnEndPoint+"/scripts/errorImageFunction.js","js");
	}
	/*Timer t = new Timer() {
		@Override
		public void run() {
			if((AppClientFactory.getUserStatus()==null || AppClientFactory.getUserStatus().trim().equals(GOORU_USER_INACTIVE)) && flag){
				flag= false;
				redirectToLandingPage();
				
			}
		}
	};*/
	

}
