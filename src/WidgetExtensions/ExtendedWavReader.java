package WidgetExtensions;

import ObjectTypeConversion.WavReader;
import WidgetExtensionDefs.ExtendedAttributeStringParam;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedWavReader implements ExtendedAttributeStringParam 
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		WidgetBuildController.getInstance().setAppObject(WavReader.class.getName(), new WavReader(arg0));
	}
}
