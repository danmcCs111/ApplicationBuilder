package WidgetExtensions;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

//TODO clean up these extended classes...
public class ExtendedSetJMenuBarParent implements ExtendedAttributeStringParam 
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		Object instance = WidgetBuildController.getInstance().findRef(widgetProperties.getParentRefWithID()).getInstance();
		if(instance instanceof JFrame)
		{
			((JFrame)instance).setJMenuBar((JMenuBar) widgetProperties.getInstance());
		}
	}
	
}
