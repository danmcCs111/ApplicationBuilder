package WidgetExtensionsImpl;

import WidgetExtensions.ConnectedComponentName;
import WidgetExtensions.ExtendedAttributeStringParam;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedConnectedComponent implements ExtendedAttributeStringParam 
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		ConnectedComponentName ecc = (ConnectedComponentName)(widgetProperties.getInstance());
		ecc.setConnectedComponentName(arg0);
	}

}
