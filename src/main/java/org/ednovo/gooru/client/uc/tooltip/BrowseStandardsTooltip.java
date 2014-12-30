package org.ednovo.gooru.client.uc.tooltip;

import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.ui.HTMLEventPanel;
import org.ednovo.gooru.shared.i18n.MessageProperties;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class BrowseStandardsTooltip extends PopupPanel {
	
	@UiField
	HTMLEventPanel confirmationPanel;
	
	@UiField
	Label desLbl;
	
	@UiField
	HTMLPanel panelArrow,descPanel;
	 
	@UiField Anchor anchorLbl;
	
	
	public interface BrowseStandardsTooltipUiBinder extends UiBinder<Widget, BrowseStandardsTooltip>{
	}
	
	public static BrowseStandardsTooltipUiBinder toolTipBrowseStandardsUiBinder=GWT.create(BrowseStandardsTooltipUiBinder.class);{
	}
	
	private MessageProperties i18n = GWT.create(MessageProperties.class); 
	
    public BrowseStandardsTooltip(String description, String value){
    	setWidget(toolTipBrowseStandardsUiBinder.createAndBindUi(this));
		anchorLbl.setText(value.toLowerCase());
		if(AppClientFactory.isAnonymous()){
			desLbl.setText(i18n.GL1613());
			anchorLbl.setVisible(false);
		}else{
			desLbl.setText(description);
			anchorLbl.setHref("#settings");
			anchorLbl.setVisible(true);
		}
		descPanel.add(desLbl);
		descPanel.add(anchorLbl);
		confirmationPanel.getElement().setId("epnlConfirmationPanel");
		desLbl.getElement().setId("lblDesLbl");
		desLbl.getElement().setAttribute("alt", description);
		desLbl.getElement().setAttribute("title", description);
		getConfirmationPanel();
	}
   
    public void setArrowLeft(){
     	panelArrow.getElement().getStyle().setPosition(Position.ABSOLUTE);
    //	panelArrow.getElement().getStyle().setLeft(165, Unit.PX);
    }
    
    public HTMLPanel getPanelArrow(){
    	return panelArrow;
    }
    
    public HTMLEventPanel getConfirmationPanel(){
    	confirmationPanel.getElement().getStyle().setMarginTop(-45, Unit.PX);
    	panelArrow.getElement().getStyle().setTop(1, Unit.PX);
		return confirmationPanel;
    }

}
