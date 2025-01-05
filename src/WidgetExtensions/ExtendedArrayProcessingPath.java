package WidgetExtensions;

import java.util.HashMap;
import java.util.List;

import ApplicationBuilder.WidgetBuildController;
import Properties.PropertiesFileLoader;
import WidgetUtility.WidgetCreatorProperty;

/**
 * ; -> seperate paths
 * : -> give path filter
 */
public class ExtendedArrayProcessingPath implements ExtendedAttributeStringParam
{
	private HashMap<String, List<String>> pathAndFileList = new HashMap<String, List<String>>();
	
	@Override
	public void applyMethod(String path, WidgetBuildController wbc, WidgetCreatorProperty widgetProperties)
	{
		String [] paths = null;
		if(path.contains(";"))
		{
			paths = path.split(";");
		}
		else
		{
			paths = new String [] {path};
		}
		for(int i = 0; i < paths.length; i++)
		{
			String [] tmp = paths[i].split(":");
			String 
				p=tmp[0].trim(),
				f=tmp[1].trim();
			
			pathAndFileList.put(p, PropertiesFileLoader.getOSFileList(p, f));
		}
	}
	
	public HashMap<String, List<String>> getPathAndFileList()
	{
		return this.pathAndFileList;
	}
	
}
