package WidgetExtensionsImpl;

import java.awt.Component;

import WidgetExtensions.ExtendedAttributeParam;
import WidgetExtensions.ExtendedAttributeStringParam;
import WidgetExtensions.ExtendedStringCollection;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedTitleSwitcher implements ExtendedAttributeStringParam
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		ExtendedStringCollection esc = (ExtendedStringCollection) ExtendedAttributeParam.findComponentByName(arg0);
		Component c = (Component) widgetProperties.getInstance();
		esc.setTextPathComponent(c);
	}

}
