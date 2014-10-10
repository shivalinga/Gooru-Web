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
package org.ednovo.gooru.client.mvp.play.collection.preview.home;


import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ResourceCurosal {
	
	private int widgetsCount=0;
	private Label nextButton;
	private Label previousButton;
	private FlowPanel widgetsPanel=null;
	private FlowPanel carosualContainer=null;
	private boolean isNextButtonActive=false;
	private boolean isPreviousButtonActive=false;
	private static int WIDGET_WIDTH=90;
	private static final int ANIMATION_DURATION=300;
	private double currentMarginLeft=0.0;
	
	public ResourceCurosal(Label nextButton,Label previousButton,FlowPanel widgetsPanel,int widgetsCount, int widgetWidth,FlowPanel carosualContainer){
		WIDGET_WIDTH = widgetWidth;
		this.nextButton=nextButton;
		this.previousButton=previousButton;
		this.widgetsPanel=widgetsPanel;
		this.widgetsCount=widgetsCount;
		this.carosualContainer=carosualContainer;
		setTotalWidth();
		nextButton.addClickHandler(new ShowNextWidgetEvent());
		previousButton.addClickHandler(new ShowPreviousWidgetEvent());
		nextButton.addMouseOverHandler(new ButtonMouseOverEvent(true));
		nextButton.addMouseOutHandler(new ButtonMouseOutEvent(true));
		previousButton.addMouseOverHandler(new ButtonMouseOverEvent(false));
		previousButton.addMouseOutHandler(new ButtonMouseOutEvent(false));
		adjustNextbuttonVisibility(currentMarginLeft);
		adjustPreviousButtonVisibility();
		addWidthCarouselContainer();
		Window.addResizeHandler(new ResizeLogicEvent());
	}
	public void setTotalWidth(){
		widgetsPanel.setWidth((widgetsCount*WIDGET_WIDTH)+"px");
		//widgetsPanel.getElement().getStyle().setPosition(Position.STATIC);
	}
	
	public void adjustNextbuttonVisibility(double currentMarginLeft){
		int totalWidgetsWidth=widgetsCount*WIDGET_WIDTH;
		if((totalWidgetsWidth-getTotalViewableWidth())<=(currentMarginLeft)){
			activateNextButton(false);
		}else{
			activateNextButton(true);
		}
	}
	
	public void adjustPreviousButtonVisibility(){
		if((currentMarginLeft-WIDGET_WIDTH)<=0){
			activatePreviousButton(false);
		}else{
			activatePreviousButton(true);
		}
	}
	
	public void adjustMarginLeft(){
//		int totalWidgetsWidth=widgetsCount*WIDGET_WIDTH;
//		if((totalWidgetsWidth-getTotalViewableWidth())<=(currentMarginLeft)){
//				int totalNavigableWidth=totalWidgetsWidth-getTotalViewableWidth();
//				System.out.println("totalNavigableWidth===>"+totalNavigableWidth);
//				System.out.println("currentMarginLeft===>"+currentMarginLeft);
//				if(currentMarginLeft>totalNavigableWidth){
//					currentMarginLeft=totalNavigableWidth;
//					widgetsPanel.getElement().getStyle().setMarginLeft(-(currentMarginLeft), Unit.PX);
//				}else{
//					
//				}
//		}
		
	}
	
	public void activateNextButton(boolean isNextButtonActive){
		this.isNextButtonActive=isNextButtonActive;
		if(isNextButtonActive){
			nextButton.getElement().getStyle().setVisibility(Visibility.VISIBLE);
		}else{
			nextButton.getElement().getStyle().setVisibility(Visibility.HIDDEN);
		}
	}
	public void activatePreviousButton(boolean isPreviousButtonActive){
		this.isPreviousButtonActive=isPreviousButtonActive;
		if(isPreviousButtonActive){
			previousButton.getElement().getStyle().setVisibility(Visibility.VISIBLE);
		}else{
			previousButton.getElement().getStyle().setVisibility(Visibility.HIDDEN);
		}
	}
	
	public class ShowNextWidgetEvent implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			if(isNextButtonActive){
				adjustNextbuttonVisibility(currentMarginLeft+WIDGET_WIDTH);
				new WidgetSlideAnimation(widgetsPanel, "forward").run(ANIMATION_DURATION);
				activatePreviousButton(true);
			}
		}
	}
	public class ShowPreviousWidgetEvent implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			if(isPreviousButtonActive){
				adjustPreviousButtonVisibility();
				new WidgetSlideAnimation(widgetsPanel, "backward").run(ANIMATION_DURATION);
				activateNextButton(true);
			}
		}
	}
	
	public class ButtonMouseOverEvent implements MouseOverHandler{
		private boolean isNextButton=false;
		public ButtonMouseOverEvent(boolean isNextButton){
			this.isNextButton=isNextButton;
		}
		@Override
		public void onMouseOver(MouseOverEvent event) {
			if(isNextButton&&isNextButtonActive){
				//nextButton.getElement().setAttribute("style", "border-left: 15px solid #999; cursor: pointer;");
			}else if(!isNextButton&&isPreviousButtonActive){
				//previousButton.getElement().setAttribute("style", "border-right: 15px solid #999; cursor: pointer;");
			}
		}
	}
	public class ButtonMouseOutEvent implements MouseOutHandler{
		private boolean isNextButton=false;
		public ButtonMouseOutEvent(boolean isNextButton){
			this.isNextButton=isNextButton;
		}
		@Override
		public void onMouseOut(MouseOutEvent event) {
			if(isNextButton){
				nextButton.getElement().removeAttribute("style");
				if(!isNextButtonActive){
					nextButton.getElement().getStyle().setVisibility(Visibility.HIDDEN);
				}
			}else{
				previousButton.getElement().removeAttribute("style");
				if(!isPreviousButtonActive){
					previousButton.getElement().getStyle().setVisibility(Visibility.HIDDEN);
				}
			}
		}
	}
	public class WidgetSlideAnimation extends  Animation{
		
		private Widget slideWidget;
		private String slideDirection;
		private double startingMarginLeft;
		private double desiredMarginLeft;
		public WidgetSlideAnimation(Widget widget,String direction){
			this.slideWidget=widget;
			this.slideDirection=direction;
			setAnimationPosition();
		}
		private void setAnimationPosition(){	
			startingMarginLeft=currentMarginLeft;
			if(slideDirection.equalsIgnoreCase("forward")){
				desiredMarginLeft=startingMarginLeft+WIDGET_WIDTH;
			}
			else{
				desiredMarginLeft=startingMarginLeft-WIDGET_WIDTH;
			}
		}
		protected void onStart() {
			super.onStart();
		}
		@Override
		protected void onUpdate(double progress) {
			double marginLeft = -(extractProportionalLength(progress)) ;
			slideWidget.getElement().getStyle().setMarginLeft(marginLeft, Unit.PX);
		}
		 private double extractProportionalLength(double progress) {
	            double marginLeft = startingMarginLeft - (startingMarginLeft - desiredMarginLeft) * progress; // this is for hidding ,eg: startHeight=200,desiredHeight=
	            return marginLeft;
	      }
		 protected double interpolate(double progress) {
			    return (1 + Math.cos(Math.PI + progress * Math.PI)) / 2;
		 }
		protected void onComplete() {
			if(slideDirection.equalsIgnoreCase("forward")){
				desiredMarginLeft=startingMarginLeft+WIDGET_WIDTH;
			}
			else{
				desiredMarginLeft=startingMarginLeft-WIDGET_WIDTH;
			}
			currentMarginLeft=desiredMarginLeft;
			slideWidget.getElement().getStyle().setMarginLeft(-(desiredMarginLeft), Unit.PX);
		}
	}
	
	public class ResizeLogicEvent implements ResizeHandler{
		@Override
		public void onResize(ResizeEvent event) {
			addWidthCarouselContainer();
			adjustNextbuttonVisibility(currentMarginLeft);
			adjustMarginLeft();
			adjustPreviousButtonVisibility();
		}
	}
	
	public void addWidthCarouselContainer(){
		if(carosualContainer!=null){
			carosualContainer.setWidth((getTotalViewableWidth())+"px");
		}
	}
	
	public int getTotalViewableWidth(){
		int totalClientWidth=Window.getClientWidth();
		int totalViewableWidth=0;
		if(totalClientWidth>1000){
			 totalViewableWidth=900;
		}else{
			 totalViewableWidth=totalClientWidth-100;
		}
		return totalViewableWidth;
	}
	public FlowPanel getCarosualContainer() {
		return carosualContainer;
	}
	public void setCarosualContainer(FlowPanel carosualContainer) {
		this.carosualContainer = carosualContainer;
	}

}
