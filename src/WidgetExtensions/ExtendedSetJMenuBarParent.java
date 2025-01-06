package WidgetExtensions;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import ApplicationBuilder.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

//TODO clean up these extended classes...
public class ExtendedSetJMenuBarParent implements ExtendedAttributeStringParam 
{

	@Override
	public void applyMethod(String arg0, WidgetBuildController wbc, WidgetCreatorProperty widgetProperties) 
	{
		Object instance = wbc.findRef(widgetProperties.getParentRef()).getInstance();
		if(instance instanceof JFrame)
		{
			((JFrame)instance).setJMenuBar((JMenuBar) widgetProperties.getInstance());
		}
	}
	
}
