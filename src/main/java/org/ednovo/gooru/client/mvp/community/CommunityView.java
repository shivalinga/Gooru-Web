package org.ednovo.gooru.client.mvp.community;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.gin.BaseViewWithHandlers;
import org.ednovo.gooru.client.mvp.home.ResetPasswordVc;
import org.ednovo.gooru.client.mvp.home.library.LibraryView;
import org.ednovo.gooru.client.mvp.home.register.RegisterVc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Search Team
` * 
 */
public class CommunityView extends BaseViewWithHandlers<CommunityUiHandlers> implements IsCommunityView {

	@UiField HTMLPanel landingpagePanel;
	
	LibraryView libraryView = null;
	
	
	private static LandingPageViewUiBinder uiBinder = GWT.create(LandingPageViewUiBinder.class);

	interface LandingPageViewUiBinder extends UiBinder<Widget, CommunityView> {
	}

	/**
	 * Class constructor
	 */
	public CommunityView() {		
		setWidget(uiBinder.createAndBindUi(this));
		libraryView = new LibraryView(PlaceTokens.DISCOVER);
//		libraryView.getCourseTabs().setVisible(false);
		Window.scrollTo(0, 0);
		landingpagePanel.add(libraryView);
		landingpagePanel.getElement().setId("pnlPartnerPanel");
		landingpagePanel.getElement().getStyle().setMarginTop(39, Unit.PX);
	}

	@Override
	public void loadFeaturedContributors(String callBack, String placeToken) {
		libraryView.loadContributorsPage(callBack,placeToken);
	}
	@Override
	public void resetPassword(String resetToken) {
		new ResetPasswordVc(resetToken);
		
	}
	@Override
	public void registerPopup() {
		RegisterVc registerVc = new RegisterVc();
		registerVc.show();
		registerVc.center();
	}
}
