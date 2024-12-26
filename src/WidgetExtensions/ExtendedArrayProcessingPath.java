package WidgetExtensions;

public class ExtendedArrayProcessingPath implements ExtendedAttributeStringParam
{
	@Override
	public void applyMethod(String path)
	{
		String [] paths = null;
		//TODO
		if(path.contains(";"))//TODO use semi colon on listed
		{
			paths = path.split(";");
		}
		else
		{
			paths = new String [] {path};
		}
		FileListOptionGenerator.getComponents(path, path, null);
	}
}
