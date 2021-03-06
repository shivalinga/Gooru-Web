package org.ednovo.gooru.client.mvp.library.district;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.authentication.SignUpPresenter;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderZIndexEvent;
import org.ednovo.gooru.client.service.LibraryServiceAsync;
import org.ednovo.gooru.shared.model.library.ProfileLibraryListDo;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PresenterWidget;

public class DistrictPresenter extends PresenterWidget<IsDistrictView> implements DistrictUiHandlers {

	@Inject
	private LibraryServiceAsync libraryService;
	
	private SimpleAsyncCallback<ProfileLibraryListDo> profileLibraryListAsyncCallback;
	
	private static final String LIBRARY_PAGE = "page";
	
	private static final String CALLBACK = "callback";
	
	SignUpPresenter signUpViewPresenter = null;

	private String viewToken = null;
	
	@Inject
	public DistrictPresenter(IsDistrictView view, EventBus eventBus, SignUpPresenter signUpViewPresenter) {
		super(eventBus, view);
		this.signUpViewPresenter = signUpViewPresenter; 
		getView().setUiHandlers(this);
	}
	
	@Override
	public void onBind() {
		super.onBind();
		setProfileLibraryListAsyncCallback(new SimpleAsyncCallback<ProfileLibraryListDo>() {
			@Override
			public void onSuccess(ProfileLibraryListDo profileLibraryListDo) {
				getView().loadFeaturedContributors("featured-course",getViewToken(),profileLibraryListDo);
			}
		});
	}
	
	@Override
	public void onReveal() {
		super.onReveal();
	}
	
	@Override
	public void onReset() {
		super.onReset();
		Window.enableScrolling(true);
		if (AppClientFactory.getPlaceManager().getRequestParameter(CALLBACK) != null && AppClientFactory.getPlaceManager().getRequestParameter(CALLBACK).equalsIgnoreCase("signup")) {
			//To show SignUp (Registration popup)
			if (AppClientFactory.isAnonymous()){
				Window.enableScrolling(false);
				AppClientFactory.fireEvent(new SetHeaderZIndexEvent(98, false));
				String type = AppClientFactory.getPlaceManager().getRequestParameter("type") ;
				int displayScreen =AppClientFactory.getPlaceManager().getRequestParameter("type") !=null  ? Integer.parseInt(type) : 1;
				signUpViewPresenter.displayPopup(displayScreen);
				addToPopupSlot(signUpViewPresenter);
			}
		}
		if(AppClientFactory.getLoggedInUser()!=null&&AppClientFactory.getPlaceManager().refreshPlace()) {
			getIntoLibrarypage();
		}
	}
	
/*	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
	}
	
*/	private void getIntoLibrarypage() {
		String sharing = "public";
		if(AppClientFactory.getCurrentPlaceToken().equalsIgnoreCase(PlaceTokens.LIFEBOARD)) {
			sharing = null;
		}

		if (AppClientFactory.getPlaceManager().getRequestParameter(LIBRARY_PAGE) != null && AppClientFactory.getPlaceManager().getRequestParameter(LIBRARY_PAGE).equalsIgnoreCase("course-page")) {
			//getView().loadFeaturedContributors("course-page",getViewToken());
		} else if (AppClientFactory.getPlaceManager().getRequestParameter(LIBRARY_PAGE) != null && AppClientFactory.getPlaceManager().getRequestParameter(LIBRARY_PAGE).equalsIgnoreCase("featured-course")) {
			getLibraryService().getLibraryWorkspace(getViewToken(), 20, sharing, "", 0, getProfileLibraryListAsyncCallback());
		} else if (AppClientFactory.getPlaceManager().getRequestParameter(LIBRARY_PAGE) == null) {
			getLibraryService().getLibraryWorkspace(getViewToken(), 20, sharing, "", 0, getProfileLibraryListAsyncCallback());
		}
	}
	
	public LibraryServiceAsync getLibraryService() {
		return libraryService;
	}
	
	/**
	 * @return the profileLibraryListAsyncCallback
	 */
	public SimpleAsyncCallback<ProfileLibraryListDo> getProfileLibraryListAsyncCallback() {
		return profileLibraryListAsyncCallback;
	}

	/**
	 * @param profileLibraryListAsyncCallback the profileLibraryListAsyncCallback to set
	 */
	public void setProfileLibraryListAsyncCallback(SimpleAsyncCallback<ProfileLibraryListDo> profileLibraryListAsyncCallback) {
		this.profileLibraryListAsyncCallback = profileLibraryListAsyncCallback;
	}

	public String getViewToken() {
		return viewToken;
	}

	public void setPartnerWidget(String viewToken) {
		this.viewToken = viewToken;
	}

}