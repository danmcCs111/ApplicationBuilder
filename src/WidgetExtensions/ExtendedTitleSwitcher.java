package WidgetExtensions;

import javax.swing.JComponent;

import ApplicationBuilder.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedTitleSwitcher implements ExtendedAttributeStringParam
{
	@Override
	public void applyMethod(String arg0, WidgetBuildController wbc, WidgetCreatorProperty widgetProperties) 
	{
		JComponent c = ExtendedAttributeStringParam.findComponentByName(wbc, arg0);
		
	}

}
