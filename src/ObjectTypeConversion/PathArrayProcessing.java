package ObjectTypeConversion;

import java.util.ArrayList;
import java.util.HashMap;

import Properties.LoggingMessages;

public class PathArrayProcessing 
{
	private static final String 
		ARG_DELIMITER = ";",
		VALUE_DELIMITER = "@";
	
	private HashMap<part, ArrayList<String>> pathArray = new HashMap<part, ArrayList<String>>();
	
	public PathArrayProcessing(String arg0)
	{
		buildCollection(arg0);
	}
	
	public void buildCollection(String arg0)
	{
		pathArray.put(part.path, new ArrayList<String>());
		pathArray.put(part.extension, new ArrayList<String>());
		
		LoggingMessages.printOut(arg0);
		if(arg0 == null || arg0.strip().isEmpty())
			return;
		
		String [] vals = arg0.split(ARG_DELIMITER);
		for(String v : vals)
		{
			String [] strVs = v.split(VALUE_DELIMITER);
			LoggingMessages.printOut(strVs);
			if(strVs.length > 1)
			{
				pathArray.get(part.path).add(strVs[0].strip());
				pathArray.get(part.extension).add(strVs[1].strip());
			}
		}
	}
	
	public void addPathAndExtension(String path, String extension)
	{
		pathArray.get(part.path).add(path);
		pathArray.get(part.extension).add(extension);
	}
	
	public String getPathValue(int index)
	{
		return pathArray.get(part.path).get(index);
	}
	public String getExtensionValue(int index)
	{
		return pathArray.get(part.extension).get(index);
	}
	public int getSize()
	{
		return pathArray.get(part.path).size();
	}
	
	public enum part
	{
		path,
		extension
	}
	
	@Override
	public String toString()
	{
		String ret = "";
		for(int i = 0; i < getSize(); i++)
		{
			ret += getPathValue(i) + VALUE_DELIMITER + getExtensionValue(i) + ((i+1 < getSize())?(ARG_DELIMITER + " "):"");
		}
		return ret;
	}
}
