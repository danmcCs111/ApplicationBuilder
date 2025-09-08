package WidgetExtensionsImpl;

import ActionListeners.ActionListenerSubTypeExtension;
import ActionListeners.ConnectedComponent;
import WidgetExtensions.ExtendedAttributeParam;
import WidgetExtensions.ExtendedAttributeStringParam;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedActionListenerConnectedComponent implements ExtendedAttributeStringParam
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		ConnectedComponent connectedComp = (ConnectedComponent) ExtendedAttributeParam.findComponentByName(arg0);
		ActionListenerSubTypeExtension ale = ExtendedActionListenerSubType.getActionListener(widgetProperties);
		
		ale.setConnectedComp(connectedComp);
	}
	
}
