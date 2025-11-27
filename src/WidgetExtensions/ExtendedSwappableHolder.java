package WidgetExtensions;

import java.util.HashMap;
import java.util.List;

import WidgetComponentInterfaces.ButtonArray;
import WidgetComponents.SwappableCollection;
import WidgetExtensionDefs.ExtendedAttributeStringParam;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedSwappableHolder implements ExtendedAttributeStringParam
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		ButtonArray buttonArray = (ButtonArray) widgetProperties.getInstance();
		SwappableCollection comp = (SwappableCollection) WidgetBuildController.getInstance().findRefByName(arg0).getInstance();
		
		//TODO fix. just first path
		HashMap<String, List<String>> pathAndFileList = comp.getPathAndFileList();
		int count = 0;
		for(String key : pathAndFileList.keySet())
		{
			buttonArray.addJButtons(key, pathAndFileList.get(key), count);
			count++;
		}
	}

}
