package WidgetExtensions;

import javax.swing.JComponent;

import ActionListeners.ActionListenerSubTypeExtension;
import ApplicationBuilder.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedActionListenerConnectedComponent implements ExtendedAttributeStringParam
{
	@Override
	public void applyMethod(String arg0, WidgetBuildController wbc, WidgetCreatorProperty widgetProperties) 
	{
		try {
			Class<?> clazz = Class.forName(arg0);
			JComponent connectedComp = ExtendedAttributeStringParam.findComponent(wbc, clazz);
			ActionListenerSubTypeExtension ale = ExtendedActionListenerSubType.getActionListener(widgetProperties);
			
			ale.setWidgetBuildController(wbc);
			ale.setConnectedComp(connectedComp);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
