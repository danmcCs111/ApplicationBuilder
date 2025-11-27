package WidgetExtensions;

import WidgetComponentInterfaces.SearchSubscriber;
import WidgetComponents.SearchBar;
import WidgetExtensionDefs.ExtendedAttributeStringParam;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedSearchField implements ExtendedAttributeStringParam 
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		String name = arg0;
		Object searchPanel = widgetProperties.getInstance();
		if(searchPanel instanceof SearchBar)
		{
			SearchBar sb = (SearchBar) searchPanel;
			WidgetCreatorProperty wcp = WidgetBuildController.getInstance().findRefByName(name);
			Object o = wcp.getInstance();
			if(o instanceof SearchSubscriber)
			{
				sb.addSearchSubscriber((SearchSubscriber) o);
			}
		}
	}

}
