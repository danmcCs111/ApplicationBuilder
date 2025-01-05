package WidgetExtensions;

import java.util.HashMap;
import java.util.List;

import ApplicationBuilder.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedSwappableHolder implements ExtendedAttributeStringParam
{
	
	@Override
	public void applyMethod(String arg0, WidgetBuildController wbc, WidgetCreatorProperty widgetProperties) 
	{
		SwappableCollection comp = (SwappableCollection) ExtendedAttributeStringParam.findComponent(wbc, SwappableCollection.class);
		JButtonArray buttonArray = (JButtonArray) widgetProperties.getInstance();
		HashMap<String, List<String>> pathAndFileList = comp.getPathAndFileList();
		
		for(String key : pathAndFileList.keySet())
		{
			buttonArray.addJButtons(key, pathAndFileList.get(key));
		}
	}

}
