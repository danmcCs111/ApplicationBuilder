package WidgetExtensionsImpl;

import WidgetComponents.WeatherButtonPanel;
import WidgetExtensions.ExtendedAttributeParam;
import WidgetExtensions.WeatherButtonListenerExtension;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedWeatherButtonAddActionListener implements ExtendedAttributeParam
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		WeatherButtonListenerExtension wbl = (WeatherButtonListenerExtension) widgetProperties.getInstance();
		WeatherButtonPanel wb = (WeatherButtonPanel) WidgetBuildController.getInstance().findRefByName(arg0).getInstance();
		wb.setWeatherButtonListener(wbl);
	}

}
