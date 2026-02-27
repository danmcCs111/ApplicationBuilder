package WidgetExtensions;

import java.util.LinkedHashMap;
import java.util.List;

import ObjectTypeConversion.PathArrayProcessing;
import Properties.PathUtility;
import WidgetComponents.SwappableCollection;
import WidgetExtensionDefs.ExtendedAttributeParam;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedArrayProcessingPath implements ExtendedAttributeParam
{
	public void applyMethod(PathArrayProcessing path, WidgetCreatorProperty widgetProperties)
	{
		LinkedHashMap<String, List<String>> pathAndFileList = new LinkedHashMap<String, List<String>>();
		SwappableCollection sc = (SwappableCollection) widgetProperties.getInstance();
		for(int i = 0; i < path.getSize(); i++)
		{
			pathAndFileList.put(path.getPathValue(i), 
				PathUtility.getOSFileList(path.getPathValue(i), path.getExtensionValue(i)));
			sc.setPathAndExtension(path.getPathValue(i), path.getExtensionValue(i));
		}
		sc.setPathAndFileList(pathAndFileList);
	}
	
}
