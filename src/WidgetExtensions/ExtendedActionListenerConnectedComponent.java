package WidgetExtensions;

import javax.swing.JComponent;

import ActionListeners.ActionListenerSubTypeExtension;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedActionListenerConnectedComponent implements ExtendedAttributeStringParam
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		try {
			Class<?> clazz = Class.forName(arg0);
			JComponent connectedComp = ExtendedAttributeStringParam.findComponent(clazz);
			ActionListenerSubTypeExtension ale = ExtendedActionListenerSubType.getActionListener(widgetProperties);
			
			ale.setConnectedComp(connectedComp);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
