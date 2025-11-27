package WidgetExtensions;

import ObjectTypeConversion.NameId;
import WidgetExtensionDefs.ExtendedAttributeParam;
import WidgetExtensionInterfaces.ConnectedComponentName;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedConnectedComponent implements ExtendedAttributeParam 
{
	public void applyMethod(NameId arg0, WidgetCreatorProperty widgetProperties) 
	{
		ConnectedComponentName ecc = (ConnectedComponentName)(widgetProperties.getInstance());
		ecc.setConnectedComponentName(arg0.getNameId());
	}

}
