package WidgetExtensions;

import javax.swing.JComponent;

import ApplicationBuilder.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedActionListenerConnectedComponent implements ExtendedAttributeStringParam
{

	@Override
	public void applyMethod(String arg0, WidgetBuildController wbc, WidgetCreatorProperty widgetProperties) 
	{
		try {
			Class<?> clazz = Class.forName(arg0);
			JComponent connectedComp = findComponent(wbc, clazz);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private JComponent findComponent(WidgetBuildController wbc, Class<?> clazz)
	{
		for(WidgetCreatorProperty wcp : wbc.getWidgetCreationProperties())
		{
			if(wcp.getClassType().equals(clazz))
			{
				return (JComponent) wcp.getInstance();
			}
		}
		return null;
	}
	
}
