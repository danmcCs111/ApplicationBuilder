package WidgetExtensionsImpl;

import java.awt.Component;

import WidgetUtility.WidgetCreatorProperty;

public class ExtendedTitleSwitcher implements ExtendedAttributeParam
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		ExtendedStringCollection esc = (ExtendedStringCollection) ExtendedAttributeParam.findComponentByName(arg0);
		Component c = (Component) widgetProperties.getInstance();
		esc.setTextPathComponent(c);
	}

}
