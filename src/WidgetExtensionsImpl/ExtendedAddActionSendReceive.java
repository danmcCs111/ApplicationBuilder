package WidgetExtensionsImpl;

import javax.swing.AbstractButton;

import ActionListeners.AddActionSend;
import ActionListenersImpl.AddActionListener;
import ActionListenersImpl.AddActionReceive;
import ObjectTypeConversion.NameId;
import WidgetExtensions.ExtendedAttributeParam;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedAddActionSendReceive implements ExtendedAttributeParam 
{
	public void applyMethod(NameId arg0, NameId arg1, WidgetCreatorProperty widgetProperties) 
	{
		AddActionSend wcpSend = (AddActionSend) ExtendedAttributeParam.findComponentByName(arg0.getNameId());
		AddActionReceive wcpReceive = (AddActionReceive) ExtendedAttributeParam.findComponentByName(arg1.getNameId());
		
		AbstractButton comp = (AbstractButton) widgetProperties.getInstance();
		AddActionListener aal = new AddActionListener();
		aal.addActionRecieveListener(wcpReceive);
		aal.addActionSendListener(wcpSend);
		comp.addActionListener(aal);
	}
	
}
