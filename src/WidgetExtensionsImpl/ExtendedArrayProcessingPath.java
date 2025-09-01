package WidgetExtensionsImpl;

import java.util.HashMap;
import java.util.List;

import Properties.LoggingMessages;
import Properties.PathUtility;
import WidgetComponents.SwappableCollection;
import WidgetExtensions.ExtendedAttributeParam;
import WidgetExtensions.ExtendedAttributeStringParam;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedArrayProcessingPath implements ExtendedAttributeStringParam
{
	@Override
	public void applyMethod(String path, WidgetCreatorProperty widgetProperties)
	{
		HashMap<String, List<String>> pathAndFileList = new HashMap<String, List<String>>();
		String [] paths = null;
		if(path.contains(PATHS_DELIMITER))
		{
			paths = path.split(PATHS_DELIMITER);
		}
		else
		{
			paths = new String [] {path};
		}
		LoggingMessages.printOut(paths);
		for(int i = 0; i < paths.length; i++)
		{
			String [] tmp = paths[i].split(ARG_DELIMITER);
			String 
				p=tmp[0].trim(),
				f=tmp[1].trim();
			
			pathAndFileList.put(p, PathUtility.getOSFileList(p, f));
		}
		SwappableCollection sc = (SwappableCollection) ExtendedAttributeParam.findComponent(SwappableCollection.class);
		sc.setPathAndFileList(pathAndFileList);
	}
	
}
