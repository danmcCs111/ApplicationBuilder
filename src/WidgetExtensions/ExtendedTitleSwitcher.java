package WidgetExtensions;

import java.awt.Component;

import ApplicationBuilder.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedTitleSwitcher implements ExtendedAttributeStringParam
{
	@Override
	public void applyMethod(String arg0, WidgetBuildController wbc, WidgetCreatorProperty widgetProperties) 
	{
		ExtendedStringCollection esc = (ExtendedStringCollection) ExtendedAttributeStringParam.findComponentByName(wbc, arg0);
		Component c = (Component) widgetProperties.getInstance();
		esc.setTextPathComponent(c);
	}

}
