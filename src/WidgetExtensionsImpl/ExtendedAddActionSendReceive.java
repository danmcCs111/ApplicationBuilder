package WidgetExtensionsImpl;

import javax.swing.AbstractButton;

import ActionListeners.AddActionSend;
import ActionListenersImpl.AddActionListener;
import ActionListenersImpl.AddActionReceive;
import WidgetExtensions.ExtendedAttributeParam;
import WidgetExtensions.ExtendedAttributeStringParam;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedAddActionSendReceive implements ExtendedAttributeStringParam 
{
	private static final String DELIMITER="@";
	
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		String [] args = arg0.split(DELIMITER);
		AddActionSend wcpSend = (AddActionSend) ExtendedAttributeParam.findComponentByName(args[0].strip());
		AddActionReceive wcpReceive = (AddActionReceive) ExtendedAttributeParam.findComponentByName(args[1].strip());
		
		AbstractButton comp = (AbstractButton) widgetProperties.getInstance();
		AddActionListener aal = new AddActionListener();
		aal.addActionRecieveListener(wcpReceive);
		aal.addActionSendListener(wcpSend);
		comp.addActionListener(aal);
	}
	
}
