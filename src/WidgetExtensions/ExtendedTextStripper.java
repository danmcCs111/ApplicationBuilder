package WidgetExtensions;

import ApplicationBuilder.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedTextStripper implements ExtendedAttributeStringParam
{
	public static final String ARG_DELIMITER = "@";

	@Override
	public void applyMethod(String arg0, WidgetBuildController wbc, WidgetCreatorProperty widgetProperties)
	{
		ArrayActionListener aal = (ArrayActionListener) widgetProperties.getInstance();
		for(String s : arg0.split(ARG_DELIMITER))
		{
			aal.addStripFilter(s);
		}
	}

}
