package WidgetExtensions;

import java.util.HashMap;
import java.util.List;

import WidgetUtility.WidgetCreatorProperty;

public class ExtendedSwappableHolder implements ExtendedAttributeStringParam
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		SwappableCollection comp = (SwappableCollection) ExtendedAttributeStringParam.findComponent(SwappableCollection.class);
		JButtonArray buttonArray = (JButtonArray) widgetProperties.getInstance();
		HashMap<String, List<String>> pathAndFileList = comp.getPathAndFileList();
		
		String key = pathAndFileList.keySet().iterator().next();//just first path
		buttonArray.addJButtons(key, pathAndFileList.get(key), 0);
	}

}
