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
package org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.add;

import org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.item.AddSetupAdvancedCBundle;
import org.ednovo.gooru.shared.i18n.MessageProperties;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public  abstract class AddSetupAdvancedView extends Composite{

	private  MessageProperties i18n = GWT.create(MessageProperties.class);
	@UiField
	public Anchor educationUseAdvancedPnl,momentsOfLearningAdvancedPnl,standardsAdvancedPnl,accessHazardAdvancedPnl,mediaFeatureAdvancedPnl,mobileFreindlyAdvancedPnl;
	
	@UiField public HTMLPanel educationUseAdvancedContainer,momentsOfLearningAdvancedContainer,standardsAdvancedContainer,
	accessHazardAdvancedContainer,mediaFeatureAdvancedContainer,mobileFreindlyAdvancedContainer,setUpLabel;

	public AddSetupAdvancedView(){
		initWidget(Uibinder.createAndBindUi(this));
		AddSetupAdvancedCBundle.INSTANCE.css().ensureInjected();
		setUpLabel.getElement().setInnerText(i18n.GL3097()+i18n.GL_SPL_QUESTION());
		educationUseAdvancedPnl.setText(i18n.GL1664());
		momentsOfLearningAdvancedPnl.setText(i18n.GL1678());
		standardsAdvancedPnl.setText(i18n.GL1682());
		accessHazardAdvancedPnl.setText(i18n.GL1705());
		mediaFeatureAdvancedPnl.setText(i18n.GL3094());
		mobileFreindlyAdvancedPnl.setText(i18n.GL1811());
		
		
	}
	public interface Binder extends UiBinder<Widget, AddSetupAdvancedView> 
	{
		
	}
	public static Binder Uibinder = GWT.create(Binder.class); 
	
	public  abstract  void showAndHideContainers();
}
