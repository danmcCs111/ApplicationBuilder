package WidgetExtensionsImpl;

import javax.swing.JComponent;

import ActionListeners.ActionListenerSubTypeExtension;
import WidgetExtensions.ExtendedAttributeParam;
import WidgetExtensions.ExtendedAttributeStringParam;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedActionListenerConnectedComponent implements ExtendedAttributeStringParam
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		try {
			Class<?> clazz = Class.forName(arg0);
			JComponent connectedComp = ExtendedAttributeParam.findComponent(clazz);
			ActionListenerSubTypeExtension ale = ExtendedActionListenerSubType.getActionListener(widgetProperties);
			
			ale.setConnectedComp(connectedComp);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
