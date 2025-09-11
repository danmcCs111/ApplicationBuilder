package WidgetExtensionsImpl;

import ObjectTypeConversion.NameId;
import WidgetExtensions.ConnectedComponentName;
import WidgetExtensions.ExtendedAttributeParam;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedConnectedComponent implements ExtendedAttributeParam 
{
	public void applyMethod(NameId arg0, WidgetCreatorProperty widgetProperties) 
	{
		ConnectedComponentName ecc = (ConnectedComponentName)(widgetProperties.getInstance());
		ecc.setConnectedComponentName(arg0.getNameId());
	}

}
