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
/**
 * 
 */
package org.ednovo.gooru.client.mvp.play.resource.style;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.ClassName;
import com.google.gwt.resources.client.CssResource.NotStrict;

/**
 * @author Search Team
 * 
 */
public interface PlayerStyleBundle extends ClientBundle {

	public static final PlayerStyleBundle INSTANCE = GWT.create(PlayerStyleBundle.class);
	
	
	@NotStrict
	@Source("playerstyle.css")
	PlayerStyleResource getPlayerStyleResource();
	
	@NotStrict
	@Source("playermobile.css")
	PlayerStyleResource getPlayerMobileStyle();
	
	@NotStrict
	@Source("playertablet.css")
	PlayerStyleResource getPlayerTabletStyle();
	
	public interface PlayerStyleResource extends CssResource {
		String container();
		String whitebg();
		String navigationWrapper();
		String rightWrapper();
		String animationWrapper();
		String tabButtonsContainer();
		
		@ClassName("header-inner")
		String headerInner();
		
		@ClassName("nav-toc-wrapperContainerMain")
		String navTocWrapperContainerMain();
		
		@ClassName("nav-toc-wrapper")
		String navTocWrapper();
		
		String headerbar();
		
		@ClassName("studyplayer-container")
		String studyplayerContainer();
		
		String rightPanel();
		
		String leftPanel();
		
		String playerBodyContainer();
		
		String summaryContainer();
		
		String collectionPlayerContainer();
		
		String blocksmall();
		
		String loginStatus();
		
		@ClassName("player-resource-title")
		String playerResourceTitle();
		
		String resourceCountStyle();
		
		String fixedContainer();
		
		String bodyContent();
		
		String greybackground();
		
		String collectionImageContainer();
		
		String collectionImage();
		
		String buttonsContainer();
		
		String collectionbuttons();
		
		String logStatus();
	}
}
