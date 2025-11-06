package WidgetExtensionsImpl;

import WidgetComponents.ButtonArrayLoadingNotification;
import WidgetExtensions.ButtonArrayLoadingNotifier;
import WidgetExtensions.ExtendedAttributeStringParam;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedAddButtonArrayNotification implements ExtendedAttributeStringParam
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		WidgetCreatorProperty wcp = WidgetBuildController.getInstance().findRefByName(arg0);
		
		ButtonArrayLoadingNotifier baln = (ButtonArrayLoadingNotifier) wcp.getInstance();
		baln.addButtonArrayLoadingSubscriber((ButtonArrayLoadingNotification) widgetProperties.getInstance());
	}

}
