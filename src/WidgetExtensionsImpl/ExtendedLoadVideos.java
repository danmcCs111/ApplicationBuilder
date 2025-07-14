package WidgetExtensionsImpl;

import Editors.CommandBuildEditor;
import WidgetExtensions.ExtendedAttributeParam;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedLoadVideos implements ExtendedAttributeParam 
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		Object m = widgetProperties.getInstance();
		if(m instanceof CommandBuildEditor)
		{
			CommandBuildEditor b = ((CommandBuildEditor)m);
			if(arg0 != null && !arg0.equals(""))
			{
				b.setComponentValue(arg0);
			}
		}
	}

}
