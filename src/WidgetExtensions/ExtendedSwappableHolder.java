package WidgetExtensions;

import java.util.HashMap;
import java.util.List;

import WidgetComponents.JButtonArray;
import WidgetComponents.SwappableCollection;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedSwappableHolder implements ExtendedAttributeParam
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		JButtonArray buttonArray = (JButtonArray) widgetProperties.getInstance();
		SwappableCollection comp = (SwappableCollection) ExtendedAttributeParam.findComponent(SwappableCollection.class);
		
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
