package org.ednovo.gooru.client.uc;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.InsertPanel;
import com.google.gwt.user.client.ui.Widget;

public class H4Panel extends ComplexPanel implements InsertPanel,HasClickHandlers {
	String text;
	public H4Panel() {
		setElement(Document.get().createHElement(4));
    }
	
	@Override
	public void add(Widget w){
	add(w,getElement());
	}

	@Override
	public void insert(Widget w, int beforeIndex) {
		insert(w, getElement(), beforeIndex, true);
	}
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		getElement().setInnerText(text);
	}

	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}




}
