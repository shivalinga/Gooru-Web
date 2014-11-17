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
package org.ednovo.gooru.client.mvp.search.resource;

import java.util.HashMap;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.dnd.IsDraggable;
import org.ednovo.gooru.client.mvp.dnd.IsDraggableMirage;
import org.ednovo.gooru.client.mvp.rating.RatingWidgetView;
import org.ednovo.gooru.client.mvp.rating.events.DeletePlayerStarReviewEvent;
import org.ednovo.gooru.client.mvp.rating.events.DeletePlayerStarReviewHandler;
import org.ednovo.gooru.client.mvp.rating.events.UpdateResourceRatingCountEvent;
import org.ednovo.gooru.client.mvp.rating.events.UpdateResourceRatingCountEventHandler;
import org.ednovo.gooru.client.mvp.rating.events.UpdateResourceReviewCountEvent;
import org.ednovo.gooru.client.mvp.rating.events.UpdateResourceReviewCountEventHandler;
import org.ednovo.gooru.client.mvp.resource.dnd.ResourceDragController;
import org.ednovo.gooru.client.mvp.resource.dnd.ResourceDragWithImgUc;
import org.ednovo.gooru.client.mvp.search.SearchUiUtil;
import org.ednovo.gooru.client.mvp.search.collection.IsSearchResultVc;
import org.ednovo.gooru.client.mvp.search.event.UpdateSearchResultMetaDataEvent;
import org.ednovo.gooru.client.mvp.search.event.UpdateSearchResultMetaDataHandler;
import org.ednovo.gooru.client.uc.H4Panel;
import org.ednovo.gooru.client.uc.ResourceImageUc;
import org.ednovo.gooru.client.uc.tooltip.ToolTip;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.search.ResourceSearchResultDo;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Search Team
 *
 */
public class ResourceSearchResultVc extends Composite implements IsDraggable,
		IsSearchResultVc {

	private static ResourceSearchResultVcUiBinder uiBinder = GWT
			.create(ResourceSearchResultVcUiBinder.class);

	private static MessageProperties i18n = GWT.create(MessageProperties.class);

	interface ResourceSearchResultVcUiBinder extends
			UiBinder<Widget, ResourceSearchResultVc> {
	}

	@UiField
	HTML lblResourceTitle;

	@UiField
	Image imgNotFriendly, imgOER;

	@UiField
	H4Panel resourceTitleContainer;

	@UiField
	FlowPanel resourceTitlePanel, metaDataFloPanel;

	@UiField
	HTML resourceDescriptionHtml;

	@UiField
	ResourceImageUc resourceImageUc;

	@UiField
	FlowPanel standardsFloPanel;

	@UiField(provided = true)
	ResourceSearchResultWrapperVc wrapperVcr;

	@UiField(provided = true)
	ResourceSearchResultCBundle res;

	ToolTip toolTip = null;

	private ResourceSearchResultDo resourceSearchResultDo;

	private RatingWidgetView ratingWidgetView = null;

	private static final String PLAYER_NAME = "resource";

	private static final String VIDEO = "Video";

	private static final String QUESTION = "Question";

	private static final String PAGES = " " + i18n.GL1471();

	private static final String VIEW = " " + i18n.GL1428();

	private static final String VIEWS = " " + i18n.GL0934();

	private static final String NULL = "null";
	private static String publisherData = "";
	private static String aggregatorData = "";
	private boolean isRatingUpdated = true;

	private int updateReviewCount = 0;

	/**
	 * Class constructor, assign new instance of
	 * {@link ResourceSearchResultWrapperVc}, and call resource search result
	 * setData method
	 * 
	 * @param resourceSearchResultDo
	 *            instance of {@link ResourceSearchResultDo}
	 * @param searchDragController
	 *            instance of {@link ResourceDragController}
	 */
	public ResourceSearchResultVc(
			ResourceSearchResultDo resourceSearchResultDo,
			ResourceDragController searchDragController) {
		
		wrapperVcr = new ResourceSearchResultWrapperVc(searchDragController);
		this.res = ResourceSearchResultCBundle.INSTANCE;
		res.css().ensureInjected();
		initWidget(uiBinder.createAndBindUi(this));
		resourceDescriptionHtml.getElement().getStyle().setTextAlign(TextAlign.JUSTIFY);
		// wrapperVcr.addStyleName("resourceSearchResultBox");
		AppClientFactory.getEventBus().addHandler(
				UpdateSearchResultMetaDataEvent.TYPE, setUpdateMetaData);
		AppClientFactory.getEventBus().addHandler(
				UpdateResourceRatingCountEvent.TYPE, setRatingCount);
		AppClientFactory.getEventBus().addHandler(
				DeletePlayerStarReviewEvent.TYPE, deleteStarRating);
		AppClientFactory.getEventBus().addHandler(
				UpdateResourceReviewCountEvent.TYPE, setReviewCount);
		ratingWidgetView = new RatingWidgetView();
		wrapperVcr.ratingWidgetPanel.add(ratingWidgetView);
		setData(resourceSearchResultDo);

		resourceTitlePanel.getElement().setId("fpnlResourceTitlePanel");
		resourceTitleContainer.getElement().setId("epnlResourceTitleContainer");

		metaDataFloPanel.getElement().setId("fpnlMetaDataFloPanel");
		standardsFloPanel.getElement().setId("fpnlStandardsFloPanel");
		resourceDescriptionHtml.getElement().setId(
				"htmlResourceDescriptionHtml");
		// imgOER.setVisible(false);
		imgOER.setUrl("images/oer_icon.png");
		imgOER.getElement().setAttribute("id", i18n.GL1834());
		imgOER.getElement().setAttribute("alt", i18n.GL1834());
		imgOER.getElement().setAttribute("title", i18n.GL1834());
	}

	public RatingWidgetView getRatingWidgetView() {
		return ratingWidgetView;
	}

	public int getUpdateReviewCount() {
		return updateReviewCount;
	}

	public void setUpdateReviewCount(int updateReviewCount) {
		this.updateReviewCount = updateReviewCount;
		ratingWidgetView.getRatingCountLabel().getElement()
				.removeAttribute("class");
		if (updateReviewCount > 0) {
			ratingWidgetView
					.getRatingCountLabel()
					.getElement()
					.setAttribute("style",
							"cursor: pointer;text-decoration: none !important;color: #1076bb;");
			ratingWidgetView.getRatingCountLabel().getElement().getStyle()
					.setPadding(4, Unit.PX);
		} else {
			ratingWidgetView
					.getRatingCountLabel()
					.getElement()
					.setAttribute("style",
							"cursor: none;text-decoration: none !important;color: #4e9746;");
		}
	}

	public Anchor getAddButton() {
		return wrapperVcr.addLbl;
	}

	public SimplePanel getAddResourceContainerPanel() {
		return wrapperVcr.disclosureContentSimPanel;
	}

	public DisclosurePanel getDisclosurePanelClose() {
		return wrapperVcr.disclosureDisPanel;
	}

	/**
	 * 
	 * @function updateViews
	 * 
	 * @created_date : Aug 11, 2013
	 * 
	 * @description
	 * 
	 * 
	 * @parm(s) : @param count
	 * @parm(s) : @param contentId
	 * @parm(s) : @param whatToUpdate
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	public void updateViews(String count, String contentId, String whatToUpdate) {
		if (resourceSearchResultDo.getGooruOid().equalsIgnoreCase(contentId)) {
			metaDataFloPanel.clear();
			String category = resourceSearchResultDo.getResourceFormat()
					.getValue() != null ? resourceSearchResultDo
					.getResourceFormat().getValue() : "webpage";
			boolean shortenMetaLength = category.equalsIgnoreCase(VIDEO)
					|| category.equalsIgnoreCase(QUESTION) ? true : false;
			if (resourceSearchResultDo.getPublisher() != null) {
				String publisherData = "";
				for (String publisher : resourceSearchResultDo.getPublisher()) {
					if (resourceSearchResultDo.getPublisher().size() > 1) {
						publisherData = publisherData + publisher + ",";
					} else {
						publisherData = publisher;
					}

				}
				if (publisherData.endsWith(",")) {
					publisherData = publisherData.substring(0,
							publisherData.length() - 1);
				}
				SearchUiUtil.renderSourceMetadata(metaDataFloPanel,
						publisherData, null, shortenMetaLength ? 15 : 25);
			}
			// String source = resourceSearchResultDo.getResourceSource() !=
			// null ?
			// resourceSearchResultDo.getResourceSource().getAttribution() :
			// null;
			// SearchUiUtil.renderSourceMetadata(metaDataFloPanel, source ,null,
			// shortenMetaLength ? 15 : 25);
			if (resourceSearchResultDo.getAggregator() != null) {
				String aggregatorData = "";
				for (String aggregator : resourceSearchResultDo.getAggregator()) {
					if (resourceSearchResultDo.getAggregator().size() > 1) {
						aggregatorData = aggregatorData + aggregator + ",";
					} else {
						aggregatorData = aggregator;
					}
				}
				if (aggregatorData.endsWith(",")) {
					aggregatorData = aggregatorData.substring(0,
							aggregatorData.length() - 1);
				}
				SearchUiUtil.renderSourceMetadata(metaDataFloPanel,
						aggregatorData, null, shortenMetaLength ? 15 : 25);
			}

			SearchUiUtil.renderMetaData(metaDataFloPanel,
					resourceSearchResultDo.getCourseNames(),
					shortenMetaLength ? 15 : 18);

			SearchUiUtil.renderMetaData(metaDataFloPanel,
					count + (Integer.parseInt(count) == 1 ? VIEW : VIEWS));
			if (category.equalsIgnoreCase(VIDEO)) {
				SearchUiUtil.renderMetaData(metaDataFloPanel,
						StringUtil.stringToTime(resourceSearchResultDo
								.getDurationInSec()));
			} else if (category.equalsIgnoreCase(QUESTION)) {
				SearchUiUtil
						.renderMetaData(
								metaDataFloPanel,
								StringUtil.getQuestionType(resourceSearchResultDo
										.getQuestionType() != null ? resourceSearchResultDo
										.getQuestionType() : ""));
			}
			if (resourceSearchResultDo.getNumOfPages() != null
					&& !resourceSearchResultDo.getNumOfPages().equals("0")
					&& !resourceSearchResultDo.getNumOfPages()
							.equalsIgnoreCase(NULL)
					&& resourceSearchResultDo.getNumOfPages().length() > 0) {
				SearchUiUtil.renderMetaData(metaDataFloPanel,
						resourceSearchResultDo.getNumOfPages() + PAGES);
			}
		}
	}

	/**
	 * Set resource search result meta data info such as title, image,
	 * description , etc..
	 * 
	 * @param resourceSearchResultDo
	 *            instance of {@link ResourceSearchResultDo}
	 */
	public void setData(ResourceSearchResultDo resourceSearchResultDo) {
		this.resourceSearchResultDo = resourceSearchResultDo;
		ratingWidgetView.getAverageRatingLabel().setText(
				Double.toString(resourceSearchResultDo.getRatings()
						.getAverage()) + " ");
		ratingWidgetView.getAverageRatingLabel().setVisible(false);
		// ratingWidgetView.getRatingCountLabel().getElement().getStyle().setColor("#4e9746");
		Integer reviewCount;
		reviewCount = resourceSearchResultDo.getRatings().getReviewCount();
		if (reviewCount == null) {
			reviewCount = 0;
		}
		ratingWidgetView.getRatingCountLabel().setText(
				" " + reviewCount.toString() + " " + i18n.GL2024());
		ratingWidgetView.setAvgStarRating(resourceSearchResultDo.getRatings()
				.getAverage());
		String category = resourceSearchResultDo.getResourceFormat().getValue() != null ? resourceSearchResultDo
				.getResourceFormat().getValue() : "webpage";
		wrapperVcr.setData(resourceSearchResultDo);
		String description = resourceSearchResultDo.getDescription();
		String title = "";
		String resourceTitle;
		if (resourceSearchResultDo.getResourceTitle().contains("class")) {
			title = resourceSearchResultDo.getResourceTitle();
		} else {
			title = StringUtil.truncateText(
					resourceSearchResultDo.getResourceTitle(), 38);
		}
		boolean shortenMetaLength = category.equalsIgnoreCase(VIDEO)
				|| category.equalsIgnoreCase(QUESTION) ? true : false;

		if (resourceSearchResultDo.getPublisher() != null) {
			String publisherData = "";
			for (String publisher : resourceSearchResultDo.getPublisher()) {
				if (resourceSearchResultDo.getPublisher().size() > 1) {
					publisherData = publisherData + publisher + ",";
				} else {
					publisherData = publisher;
				}

			}
			if (publisherData.endsWith(",")) {
				publisherData = publisherData.substring(0,
						publisherData.length() - 1);
			}
			SearchUiUtil.renderSourceMetadata(metaDataFloPanel, publisherData,
					null, shortenMetaLength ? 15 : 25);
		}

		// String source = resourceSearchResultDo.getResourceSource() != null ?
		// resourceSearchResultDo.getResourceSource().getAttribution() : null;
		if (resourceSearchResultDo.getAggregator() != null) {
			String aggregatorData = "";
			for (String aggregator : resourceSearchResultDo.getAggregator()) {
				if (resourceSearchResultDo.getAggregator().size() > 1) {
					aggregatorData = aggregatorData + aggregator + ",";
				} else {
					aggregatorData = aggregator;
				}
			}
			if (aggregatorData.endsWith(",")) {
				aggregatorData = aggregatorData.substring(0,
						aggregatorData.length() - 1);
			}
			SearchUiUtil.renderSourceMetadata(metaDataFloPanel, aggregatorData,
					null, shortenMetaLength ? 15 : 25);
		}

		SearchUiUtil.renderMetaData(metaDataFloPanel, resourceSearchResultDo
				.getCourseNames(), shortenMetaLength ? 15 : 18);
		SearchUiUtil.renderMetaData(metaDataFloPanel,
				resourceSearchResultDo.getTotalViews()
						+ (resourceSearchResultDo.getTotalViews() == 1 ? VIEW
								: VIEWS));
		if (category.equalsIgnoreCase(VIDEO)) {
			SearchUiUtil.renderMetaData(metaDataFloPanel, StringUtil
					.stringToTime(resourceSearchResultDo.getDurationInSec()));
		} else if (category.equalsIgnoreCase(QUESTION)) {
			SearchUiUtil.renderMetaData(metaDataFloPanel,
					StringUtil.getQuestionType(resourceSearchResultDo
							.getQuestionType() != null ? resourceSearchResultDo
							.getQuestionType() : ""));
		}
		if (resourceSearchResultDo.getNumOfPages() != null
				&& !resourceSearchResultDo.getNumOfPages().equals("0")
				&& !resourceSearchResultDo.getNumOfPages().equalsIgnoreCase(
						NULL)
				&& resourceSearchResultDo.getNumOfPages().length() > 0) {
			SearchUiUtil.renderMetaData(metaDataFloPanel,
					resourceSearchResultDo.getNumOfPages() + PAGES);
		}
		title = title.replaceAll("<p>", "").replaceAll("</p>", "");

		lblResourceTitle.setHTML(title.trim());
		resourceTitle = resourceSearchResultDo.getResourceTitle().trim();
		lblResourceTitle.getElement().setId(
				resourceSearchResultDo.getGooruOid());
		if (lblResourceTitle.getText().length() > 30) {
			String lblString = lblResourceTitle.getText();
			lblString = lblString.substring(0, 24) + "...";
			lblResourceTitle.setText(lblString);
			// lblResourceTitle.getElement().getStyle().setWidth(240, Unit.PX);
		}
		String mediaType = resourceSearchResultDo.getMediaType();

		boolean oerVisibility = resourceSearchResultDo.getLicense() != null
				&& resourceSearchResultDo.getLicense().getCode() != null ? resourceSearchResultDo
				.getLicense().getCode().contains("CC") ? true : false
				: false;

		imgOER.setVisible(oerVisibility);

		boolean setVisibility = mediaType != null ? mediaType
				.equalsIgnoreCase("iPad_friendly") ? true : false : true;

		// imgNotFriendly.setVisible(setVisibility);
		if (setVisibility) {
			imgNotFriendly.getElement().setId("imgImgFriendly");
			imgNotFriendly.setTitle(i18n.GL0737_1());
			imgNotFriendly.setAltText(i18n.GL0737_1());
			imgNotFriendly.setUrl("images/mos/MobileFriendly.png");
		} else {
			imgNotFriendly.getElement().setId("imgImgNotFriendly");
			imgNotFriendly.setTitle(i18n.GL0737());
			imgNotFriendly.setAltText(i18n.GL0737());
			imgNotFriendly.setUrl("images/mos/mobileunfriendly.png");
		}
		// lblResourceTitle.getElement().getStyle().setFloat(Float.LEFT);

		imgNotFriendly.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				toolTip = new ToolTip(
						i18n.GL0454()
								+ ""
								+ "<img src='/images/mos/MobileFriendly.png' style='margin-top:0px;width:20px;height:15px;'/>"
								+ " "
								+ i18n.GL04431()
								+ " "
								+ "<img src='/images/mos/mobileunfriendly.png' style='margin-top:0px;width:20px;height:15px;'/>"
								+ " " + i18n.GL_SPL_EXCLAMATION());
				toolTip.getTootltipContent().getElement()
						.setAttribute("style", "width: 258px;");
				toolTip.getElement().getStyle()
						.setBackgroundColor("transparent");
				toolTip.getElement().getStyle().setZIndex(9999999);
				toolTip.getElement().getStyle().setPosition(Position.ABSOLUTE);
				toolTip.setPopupPosition(imgNotFriendly.getAbsoluteLeft()
						- (50 + 22), imgNotFriendly.getAbsoluteTop() + 22);
				toolTip.show();
			}
		});
		imgNotFriendly.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {

				EventTarget target = event.getRelatedTarget();
				if (Element.is(target)) {
					if (!toolTip.getElement().isOrHasChild(Element.as(target))) {
						toolTip.hide();
					}
				}
			}
		});
		if (description != null && description.length() > 205) {
			description = description.trim().substring(0, 205) + "...";
		}
		resourceDescriptionHtml.setHTML(description);
		// resourceTitleContainer.getElement().getStyle().setZIndex(99999);
		// resourceImageUc.getElement().getStyle().setZIndex(99999);
		resourceImageUc.renderSearch(category, resourceSearchResultDo.getUrl(),
				null, resourceSearchResultDo.getGooruOid(), PLAYER_NAME,
				resourceTitle, false, "");
		SearchUiUtil.renderStandards(standardsFloPanel, resourceSearchResultDo);
	}

	UpdateResourceRatingCountEventHandler setRatingCount = new UpdateResourceRatingCountEventHandler() {
		@Override
		public void setResourceRatingCount(String resourceId, double avg,
				Integer count) {
			if (resourceSearchResultDo.getGooruOid().equals(resourceId)) {
				ratingWidgetView.setAvgStarRating(avg);
			}
		}

	};

	UpdateSearchResultMetaDataHandler setUpdateMetaData = new UpdateSearchResultMetaDataHandler() {

		@Override
		public void updateSearchResultMetaData(String count, String resourceId,
				String whatToUpdate) {
			if (count != null) {
				updateViews(count, resourceId, whatToUpdate);
			}
		}
	};

	@UiHandler("resourceTitleContainer")
	public void onClickResourceTitle(ClickEvent event) {
		MixpanelUtil.Preview_Resource_From_Search("ResourceTitleLbl");
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", resourceSearchResultDo.getGooruOid());
		params.put("pn", "resource");
		AppClientFactory.getPlaceManager().revealPlace(
				PlaceTokens.RESOURCE_PLAY, params);
	}

	/**
	 * @return wrapperVcr instance of {@link ResourceSearchResultWrapperVc}
	 */
	public ResourceSearchResultWrapperVc getSearchResultVc() {
		return wrapperVcr;
	}

	/**
	 * @param searchResultVc
	 *            instance of {@link ResourceSearchResultWrapperVc}
	 */
	public void setSearchResultVc(ResourceSearchResultWrapperVc searchResultVc) {
		this.wrapperVcr = searchResultVc;
	}

	@Override
	public String getDragId() {
		return resourceSearchResultDo.getGooruOid();
	}

	@Override
	public DRAG_TYPE getDragType() {
		return DRAG_TYPE.RESOURCE;
	}

	@Override
	public IsDraggableMirage initDraggableMirage() {
		return new ResourceDragWithImgUc(resourceSearchResultDo
				.getResourceFormat().getValue(),
				resourceSearchResultDo.getResourceTitle());
	}

	/**
	 * @return the resourceSearchResultDo instance of
	 *         {@link ResourceSearchResultDo}
	 */
	public ResourceSearchResultDo getResourceSearchResultDo() {
		return resourceSearchResultDo;
	}

	/**
	 * @param resourceSearchResultDo
	 *            the resourceSearchResultDo to set
	 */
	public void setResourceSearchResultDo(
			ResourceSearchResultDo resourceSearchResultDo) {
		this.resourceSearchResultDo = resourceSearchResultDo;
	}

	@Override
	public void onDragBlur() {
		getSearchResultVc().onMouseOut(null);
	}

	@Override
	public Widget getDragHandle() {
		return getSearchResultVc().getDragHandlePanel();
	}

	@Override
	public int getDragTopCorrection() {
		return 27;
	}

	@Override
	public int getDragLeftCorrection() {
		return 20;
	}

	@Override
	public void setAddedToShelf(boolean addedToShelf) {
		wrapperVcr.setAddedToShelf(addedToShelf);
	}

	DeletePlayerStarReviewHandler deleteStarRating = new DeletePlayerStarReviewHandler() {
		@Override
		public void deleteStarRatings(String resourceGooruOid) {
			if (resourceSearchResultDo.getGooruOid().equals(resourceGooruOid)) {
				if (ratingWidgetView != null) {
					String[] revCount = ratingWidgetView.getRatingCountLabel()
							.getText().split(" ");
					if (Integer.parseInt(revCount[1].trim()) == 1) {
						// ratingWidgetView.getRatingCountLabel().getElement().removeAttribute("class");
						// ratingWidgetView.getRatingCountLabel().getElement().setAttribute("style",
						// "cursor: none;text-decoration: none !important;color: grey");
						ratingWidgetView.setAvgStarRating(0);
						ratingWidgetView.getRatingCountLabel().setText(
								" " + (Integer.parseInt(revCount[1]) - 1) + " "
										+ i18n.GL2024());
						setUpdateReviewCount(Integer.parseInt(revCount[1]) - 1);
						// ratingWidgetView.getRatingCountLabel().getElement().getStyle().setColor("#4e9746");
					} else {
						// ratingWidgetView.getRatingCountLabel().getElement().getStyle().setColor("#1076bb");
						setUpdateReviewCount(Integer.parseInt(revCount[1]) - 1);
						ratingWidgetView.getRatingCountLabel().setText(
								" " + (Integer.parseInt(revCount[1]) - 1) + " "
										+ i18n.GL2024());
					}
				}
			}
		}

	};

	UpdateResourceReviewCountEventHandler setReviewCount = new UpdateResourceReviewCountEventHandler() {
		@Override
		public void setReviewCount(String resourceId, Integer count) {
			if (resourceSearchResultDo.getGooruOid().equals(resourceId)) {
				setUpdateReviewCount(count);
				/*
				 * ratingWidgetView.getRatingCountLabel().getElement().setAttribute
				 * ("style",
				 * "cursor: pointer;text-decoration: none !important;color: #1076bb;"
				 * );
				 * ratingWidgetView.getRatingCountLabel().getElement().getStyle
				 * ().setColor("#1076bb");
				 */
				ratingWidgetView.getRatingCountLabel().setText(
						" " + Integer.toString(count) + " " + i18n.GL2024());
				ratingWidgetView.getAverageRatingLabel().setVisible(false);
				/*
				 * if(count==1 && isRatingUpdated){ isRatingUpdated=false; //
				 * ratingWidgetView
				 * .getRatingCountLabel().getElement().removeAttribute("class");
				 * //
				 * ratingWidgetView.getRatingCountLabel().getElement().setAttribute
				 * ("style",
				 * "cursor: pointer;text-decoration: none !important;color: #1076bb;"
				 * ); //
				 * ratingWidgetView.getRatingCountLabel().getElement().getStyle
				 * ().setColor("#1076bb"); if(reviewLabelHandler !=null){
				 * reviewLabelHandler.removeHandler(); } // reviewLabelHandler =
				 * ratingWidgetView.getRatingCountLabel().addClickHandler(new
				 * ShowRatingPopupEvent()); }
				 */
			}
		}

	};
}
