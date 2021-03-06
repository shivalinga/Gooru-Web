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
package org.ednovo.gooru.client.mvp.library.district;
import org.ednovo.gooru.client.ui.HTMLEventPanel;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.library.ProfileLibraryDo;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class DistrictFeaturedView extends Composite {
	
	@UiField HTMLEventPanel featuredCourse;
	@UiField Label courseTitle;
	@UiField Label courseAuthor;
	@UiField Image featuredCourseImage, contributorImage;
	
	private static MessageProperties i18n = GWT.create(MessageProperties.class);
	
	private String courseId;
	
	private final static String DEFAULT_USER_IMG = "../images/settings/setting-user-image.png";
	
	private final static String COURSE_100_75_IMG = "../images/library/course-100x75.png";
	
	private final static String COURSE_100_75_CROP = "-100x75.";
	
	private static final String PNG = ".png";
	
	private final static String MR =i18n.GL_GRR_BYMR()+" ";
	
	private final static String MS =i18n.GL_GRR_BYMS()+" ";

	private final static String FEMALE = "female";

	private final static String MALE = "male";
	
	private String parentId;
	
	private static FeaturedCourseListViewUiBinder uiBinder = GWT
			.create(FeaturedCourseListViewUiBinder.class);

	interface FeaturedCourseListViewUiBinder extends
			UiBinder<Widget, DistrictFeaturedView> {
	}

	public DistrictFeaturedView(ProfileLibraryDo profileLibraryDo) {
		initWidget(uiBinder.createAndBindUi(this));
		setData(profileLibraryDo);
	}
	
	private void setData(ProfileLibraryDo profileLibraryDo) {
		courseTitle.setText(profileLibraryDo.getTitle());
		courseTitle.getElement().setId("lblCourseTitle");
		courseTitle.getElement().setAttribute("alt",profileLibraryDo.getTitle());
		courseTitle.getElement().setAttribute("title",profileLibraryDo.getTitle());
		courseAuthor.getElement().setId("lblCourseAuthor");
		
		featuredCourseImage.setUrl(StringUtil.formThumbnailName(profileLibraryDo.getThumbnails().getUrl(),COURSE_100_75_CROP));
		featuredCourseImage.setWidth("100px");
		featuredCourseImage.setHeight("75px");

		featuredCourseImage.addErrorHandler(new ErrorHandler() {
			@Override
			public void onError(ErrorEvent event) {
				featuredCourseImage.setUrl(COURSE_100_75_IMG);
			}
		});

		/*		contributorImage.setHeight("46px");
		contributorImage.setWidth("46px");
		
		if(courseDo.getCreator()!=null) {
			courseAuthor.setVisible(true);
			String authorName = "";
			String contributorProfileImage = "";
			/// In User Object is null
			if (courseDo.getUser()!=null &&  courseDo.getUser().size()>0){
				int j=0;
				for (int i=0;i<courseDo.getUser().size();i++){
					j = i;
					if (courseDo.getUser().get(i).getIsOwner() !=null &&  courseDo.getUser().get(i).getIsOwner().equalsIgnoreCase("1")){
						break;
					}
				}
				
				if(courseDo.getUser().get(j).getGender().equalsIgnoreCase(MALE)) {
					authorName = MR+courseDo.getUser().get(j).getLastName();
				} else if(courseDo.getUser().get(j).getGender().equalsIgnoreCase(FEMALE)) {
			 	    authorName = MS+courseDo.getUser().get(j).getLastName();
				} else {
					authorName = courseDo.getUser().get(j).getLastName();
				}
				
				if (courseDo.getUser().size()>1){
					courseAuthor.setText(authorName +" "+GL_GRR_AND+" "+GL1117);
				}else{
					courseAuthor.setText(authorName);
				}
				contributorProfileImage =AppClientFactory.getLoggedInUser().getSettings().getProfileImageUrl() + courseDo.getUser().get(j).getGooruUId()+PNG;
			}else{
				if(courseDo.getCreator().getGender().equalsIgnoreCase(MALE)) {
					authorName = MR+courseDo.getCreator().getLastName();
				} else if(courseDo.getCreator().getGender().equalsIgnoreCase(FEMALE)) {
					authorName = MS+courseDo.getCreator().getLastName();
				} else {
					authorName = courseDo.getCreator().getLastName();
					if(courseDo.getCreator().getLastName().contains("RUSD")) {
						authorName = GL1747 +" "+authorName;
					}
				}
				courseAuthor.setText(authorName);
				contributorProfileImage =AppClientFactory.getLoggedInUser().getSettings().getProfileImageUrl() + courseDo.getCreator().getGooruUId()+PNG; 
			}
			contributorImage.setUrl(contributorProfileImage);			
			contributorImage.addErrorHandler(new ErrorHandler() {
				@Override
				public void onError(ErrorEvent event) {
					contributorImage.setUrl(DEFAULT_USER_IMG);
				}
			});
		} else {
			courseAuthor.setVisible(false);
			contributorImage.setUrl(DEFAULT_USER_IMG);
		}
		if(courseDo.getCodeId()!=null) {
			setCourseId(courseDo.getCodeId());
		} else {
			setParentId(courseDo.getParentId());
		}
		contributorImage.setUrl(DEFAULT_USER_IMG);
*/		courseAuthor.setVisible(false);
		contributorImage.setVisible(false);
		setCourseId(profileLibraryDo.getGooruOid());
		featuredCourse.getElement().setId("epnlFeaturedCourse");
		featuredCourseImage.getElement().setId("imgFeaturedCourseImage");
		contributorImage.getElement().setId("imgContributorImage");
	}
	
	public HTMLEventPanel getfeaturedCoursePanel() {
		return featuredCourse;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
}
