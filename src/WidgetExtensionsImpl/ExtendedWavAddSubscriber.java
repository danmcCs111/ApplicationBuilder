package WidgetExtensionsImpl;

import ActionListeners.WavReaderSubscriber;
import ObjectTypeConversion.WavReader;
import WidgetExtensions.ExtendedAttributeStringParam;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedWavAddSubscriber implements ExtendedAttributeStringParam 
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		Object current = widgetProperties.getInstance();
		WavReader cr = (WavReader) WidgetBuildController.getInstance().getAppObject(WavReader.class.getName());
		cr.addWavReaderSubscriber((WavReaderSubscriber)current);
	}
	
}
