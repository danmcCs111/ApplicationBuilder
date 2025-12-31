package ObjectTypeConversion;

import Properties.PathUtility;
import WidgetComponentInterfaces.ParamOption.PathModifier;

public class FileSelection 
{
	private String 
		relativePath,
		linuxPath;
	private PathModifier pm;
	
	public FileSelection(String relativePath)
	{
		this(relativePath, PathModifier.none);
	}
	
	public FileSelection(String relativePath, PathModifier pm)
	{
		this.pm = pm;
		if(!relativePath.startsWith("."))
		{
			relativePath = "." + relativePath;
		}
		
		this.relativePath = relativePath;
		this.linuxPath = PathUtility.getPathLinux(relativePath);
	}
	
	public PathModifier getPathModifier()
	{
		return pm;
	}
	
	public String getPathLinux()
	{
		return linuxPath;
	}
	
	public String getRelativePath()
	{
		return relativePath;
	}
	
	public String getFullPath()
	{
		return PathUtility.getCurrentDirectory() + this.relativePath.substring(1);
	}
}
