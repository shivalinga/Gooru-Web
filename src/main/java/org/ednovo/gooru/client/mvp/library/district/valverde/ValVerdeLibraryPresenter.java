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
package org.ednovo.gooru.client.mvp.library.district.valverde;

import org.ednovo.gooru.client.AppPlaceKeeper;
import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BasePlacePresenter;
import org.ednovo.gooru.client.mvp.authentication.SignUpPresenter;
import org.ednovo.gooru.client.mvp.home.register.UserRegistrationPresenter;
import org.ednovo.gooru.client.mvp.library.district.DistrictPresenter;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderZIndexEvent;

import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

public class ValVerdeLibraryPresenter extends BasePlacePresenter<IsValVerdeLibraryView, ValVerdeLibraryPresenter.IsValVerdeLibraryProxy> implements ValVerdeLibraryUiHandlers {

	private DistrictPresenter districtPresenter;

	private static final String CALLBACK = "callback";
	
	SignUpPresenter signUpViewPresenter = null;
	
	@ProxyCodeSplit
	@NameToken(PlaceTokens.VALVERDE)
	@UseGatekeeper(AppPlaceKeeper.class)
	public interface IsValVerdeLibraryProxy extends ProxyPlace<ValVerdeLibraryPresenter> {
	}

	/**
	 * Class constructor
	 * @param userRegistrationPresenter instance of {@link UserRegistrationPresenter}
	 * @param view {@link View}
	 * @param proxy {@link Proxy}
	 */
	@Inject
	public ValVerdeLibraryPresenter(IsValVerdeLibraryView view, IsValVerdeLibraryProxy proxy, DistrictPresenter districtPresenter,SignUpPresenter signUpViewPresenter) {
		super(view, proxy);
		getView().setUiHandlers(this);
		this.districtPresenter = districtPresenter;
		this.signUpViewPresenter = signUpViewPresenter;
	}
	
	@Override
	public void onBind() {
		super.onBind();
	}
	
	@Override
	public void onReveal() {
		super.onReveal();
	}
	
	@Override
	public void onReset() {
		super.onReset();
	}
	
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		districtPresenter.setPartnerWidget(PlaceTokens.VALVERDE);
		clearSlot(TYPE_FOLDERS_SLOT);
		setInSlot(TYPE_FOLDERS_SLOT, districtPresenter);
		
		if (getPlaceManager().getRequestParameter(CALLBACK) != null && !getPlaceManager().getRequestParameter(CALLBACK).equalsIgnoreCase("signup")) {
		    //To show SignUp (Registration popup)
		    if (AppClientFactory.isAnonymous()){
		        Window.enableScrolling(false);
		        AppClientFactory.fireEvent(new SetHeaderZIndexEvent(98, false));
		        String type = getPlaceManager().getRequestParameter("type") ;
		        int displayScreen =getPlaceManager().getRequestParameter("type") !=null  ? Integer.parseInt(type) : 1;
		        signUpViewPresenter.displayPopup(displayScreen);
		        addToPopupSlot(signUpViewPresenter);
		    }
		}
	}
	
	@Override
	public String getViewToken() {
		return PlaceTokens.VALVERDE;
	}

}