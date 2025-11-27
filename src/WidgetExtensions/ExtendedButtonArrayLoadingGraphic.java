package WidgetExtensions;

import WidgetComponents.SwappableCollection;
import WidgetExtensionDefs.ExtendedAttributeParam;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedButtonArrayLoadingGraphic implements ExtendedAttributeParam
{
	public void applyMethod(boolean arg0, WidgetCreatorProperty widgetProperties)
	{
		SwappableCollection sc = (SwappableCollection) widgetProperties.getInstance();
		sc.setIsLoadingGraphic(arg0);
	}
}
