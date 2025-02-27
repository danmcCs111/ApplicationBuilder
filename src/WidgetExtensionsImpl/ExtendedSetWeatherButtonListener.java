package WidgetExtensionsImpl;

import WidgetComponents.WeatherButton;
import WidgetComponents.WeatherButtonListener;
import WidgetExtensions.ExtendedAttributeParam;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedSetWeatherButtonListener implements ExtendedAttributeParam
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		WeatherButtonListener wbl = (WeatherButtonListener) widgetProperties.getInstance();
		WeatherButton wb = (WeatherButton) WidgetBuildController.getInstance().findRefByName(arg0).getInstance();
		wb.setWeatherButtonListener(wbl);
	}

}
