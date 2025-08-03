package WidgetExtensionsImpl;

import ObjectTypeConversion.WavReader;
import WidgetExtensions.ExtendedAttributeParam;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedWavReader implements ExtendedAttributeParam 
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		WidgetBuildController.getInstance().setAppObject(WavReader.class.getName(), new WavReader(arg0));
	}
}
