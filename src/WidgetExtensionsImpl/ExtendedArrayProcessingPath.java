package WidgetExtensionsImpl;

import java.util.HashMap;
import java.util.List;

import ObjectTypeConversion.PathArrayProcessing;
import Properties.PathUtility;
import WidgetComponents.SwappableCollection;
import WidgetExtensions.ExtendedAttributeParam;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedArrayProcessingPath implements ExtendedAttributeParam
{
	public void applyMethod(PathArrayProcessing path, WidgetCreatorProperty widgetProperties)
	{
		HashMap<String, List<String>> pathAndFileList = new HashMap<String, List<String>>();
		for(int i = 0; i < path.getSize(); i++)
		{
			pathAndFileList.put(path.getPathValue(i), 
				PathUtility.getOSFileList(path.getPathValue(i), path.getExtensionValue(i)));
		}
		SwappableCollection sc = (SwappableCollection) widgetProperties.getInstance();
		sc.setPathAndFileList(pathAndFileList);
	}
	
}
