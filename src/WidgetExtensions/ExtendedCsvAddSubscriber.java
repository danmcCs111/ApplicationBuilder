package WidgetExtensions;

import ActionListeners.CsvReaderSubscriber;
import ObjectTypeConversion.CsvReader;
import WidgetExtensionDefs.ExtendedAttributeStringParam;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedCsvAddSubscriber implements ExtendedAttributeStringParam 
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		Object current = widgetProperties.getInstance();
		CsvReader cr = (CsvReader) WidgetBuildController.getInstance().getAppObject(CsvReader.class.getName());
		cr.addCsvReaderSubscriber((CsvReaderSubscriber)current);
	}
	
}
