package WidgetExtensionsImpl;

import ObjectTypeConversion.CsvReader;
import WidgetExtensions.ExtendedAttributeStringParam;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedCsvReader implements ExtendedAttributeStringParam 
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		WidgetBuildController.getInstance().setAppObject(CsvReader.class.getName(), new CsvReader(arg0));
	}
}
