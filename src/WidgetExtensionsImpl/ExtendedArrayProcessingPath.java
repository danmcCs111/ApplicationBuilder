package WidgetExtensionsImpl;

import java.util.HashMap;
import java.util.List;

import Properties.LoggingMessages;
import Properties.PathUtility;
import WidgetComponents.SwappableCollection;
import WidgetExtensions.ExtendedAttributeStringParam;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedArrayProcessingPath implements ExtendedAttributeStringParam
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties)
	{
		HashMap<String, List<String>> pathAndFileList = new HashMap<String, List<String>>();
		String [] paths = null;
		if(arg0.contains(PATHS_DELIMITER))
		{
			paths = arg0.split(PATHS_DELIMITER);
		}
		else
		{
			paths = new String [] {arg0};
		}
		LoggingMessages.printOut(paths);
		for(int i = 0; i < paths.length; i++)
		{
			String [] tmp = paths[i].split(ARG_DELIMITER);
			String 
				p=tmp[0].trim(),
				f=tmp[1].trim();
			
			pathAndFileList.put(p, 
					PathUtility.getOSFileList(p, f));
		}
		SwappableCollection sc = (SwappableCollection) widgetProperties.getInstance();
		sc.setPathAndFileList(pathAndFileList);
	}
	
}
