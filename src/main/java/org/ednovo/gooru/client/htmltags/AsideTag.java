package org.ednovo.gooru.client.htmltags;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.InsertPanel;
import com.google.gwt.user.client.ui.Widget;
public class AsideTag extends ComplexPanel implements InsertPanel{
	public AsideTag() {
		setElement(Document.get().createElement("aside"));
	}

	@Override
	public void add(Widget w){
		add(w,getElement());
	}
	
	public void insert(Widget w,int beforeIndex){
		insert(w, getElement(), beforeIndex, true);
	}
}