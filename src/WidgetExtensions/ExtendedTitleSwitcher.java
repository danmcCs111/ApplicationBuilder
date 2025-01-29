package WidgetExtensions;

import java.awt.Component;

import WidgetUtility.WidgetCreatorProperty;

public class ExtendedTitleSwitcher implements ExtendedAttributeStringParam
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		ExtendedStringCollection esc = (ExtendedStringCollection) ExtendedAttributeStringParam.findComponentByName(arg0);
		Component c = (Component) widgetProperties.getInstance();
		esc.setTextPathComponent(c);
	}

}
