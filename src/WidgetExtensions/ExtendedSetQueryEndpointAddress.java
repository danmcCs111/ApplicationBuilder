package WidgetExtensions;

import ApplicationBuilder.QueryUpdateTool;
import WidgetExtensionDefs.ExtendedAttributeStringParam;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedSetQueryEndpointAddress implements ExtendedAttributeStringParam
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		QueryUpdateTool.setEndpoint(arg0);
	}
	
}
