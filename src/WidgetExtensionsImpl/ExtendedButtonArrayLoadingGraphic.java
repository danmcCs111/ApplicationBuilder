package WidgetExtensionsImpl;

import WidgetComponents.SwappableCollection;
import WidgetExtensions.ExtendedAttributeParam;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedButtonArrayLoadingGraphic implements ExtendedAttributeParam
{
	public void applyMethod(boolean arg0, WidgetCreatorProperty widgetProperties)
	{
		SwappableCollection sc = (SwappableCollection) widgetProperties.getInstance();
		sc.setIsLoadingGraphic(arg0);
	}
}
