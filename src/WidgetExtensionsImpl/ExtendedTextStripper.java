package WidgetExtensionsImpl;

import ActionListeners.ArrayActionListener;
import WidgetExtensions.ExtendedAttributeParam;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedTextStripper implements ExtendedAttributeParam
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties)
	{
		ArrayActionListener aal = (ArrayActionListener) widgetProperties.getInstance();
		for(String s : arg0.split(ARG_DELIMITER))
		{
			aal.addStripFilter(s);
		}
	}

}
