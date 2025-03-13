package WidgetExtensionsImpl;

import WidgetComponents.SendHttpRequestPanel;
import WidgetExtensions.DatabaseResponseNodeListenerExtension;
import WidgetExtensions.ExtendedAttributeParam;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedDatabaseResponseNodeListener implements ExtendedAttributeParam
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		DatabaseResponseNodeListenerExtension wbl = (DatabaseResponseNodeListenerExtension) widgetProperties.getInstance();
		SendHttpRequestPanel wb = (SendHttpRequestPanel) WidgetBuildController.getInstance().findRefByName(arg0).getInstance();
		wb.setDatabaseResponseNodeListener(wbl);
	}

}
