package WidgetExtensions;

import WidgetComponents.SendHttpRequestPanel;
import WidgetExtensionDefs.ExtendedAttributeStringParam;
import WidgetExtensionInterfaces.DatabaseResponseNodeListenerExtension;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedDatabaseResponseNodeListener implements ExtendedAttributeStringParam
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		DatabaseResponseNodeListenerExtension wbl = (DatabaseResponseNodeListenerExtension) widgetProperties.getInstance();
		SendHttpRequestPanel wb = (SendHttpRequestPanel) WidgetBuildController.getInstance().findRefByName(arg0).getInstance();
		wb.setDatabaseResponseNodeListener(wbl);
	}

}
