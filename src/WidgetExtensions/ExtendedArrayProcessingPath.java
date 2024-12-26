package WidgetExtensions;


public class ExtendedArrayProcessingPath implements ExtendedAttributeStringParam
{
	public static void applyMethod(String path)
	{
		String [] paths = null;
		//TODO
		if(path.contains(","))
		{
			paths = path.split(",");
		}
		else
		{
			paths = new String [] {path};
		}
		FileListOptionGenerator.getComponents(path, path, null);
	}
}
