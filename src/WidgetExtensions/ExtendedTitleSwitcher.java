package WidgetExtensions;

import java.awt.Component;

import WidgetExtensionDefs.ExtendedAttributeParam;
import WidgetExtensionDefs.ExtendedAttributeStringParam;
import WidgetExtensionDefs.ExtendedStringCollection;
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
