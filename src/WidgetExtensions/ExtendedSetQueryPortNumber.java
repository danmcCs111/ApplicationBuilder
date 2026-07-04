package WidgetExtensions;

import ApplicationBuilder.QueryUpdateTool;
import WidgetExtensionDefs.ExtendedAttributeStringParam;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedSetQueryPortNumber implements ExtendedAttributeStringParam
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		QueryUpdateTool.setPortNumber(Integer.parseInt(arg0));
	}

}
