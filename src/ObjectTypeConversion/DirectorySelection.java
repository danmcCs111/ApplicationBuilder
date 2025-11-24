package ObjectTypeConversion;

import Properties.PathUtility;
import WidgetComponentInterfaces.ParamOption.PathModifier;

public class DirectorySelection 
{
	private String 
		relativePath,
		linuxPath;
	private PathModifier pm;
	
	public DirectorySelection(String relativePath)
	{
		this(relativePath, PathModifier.none);
	}
	
	public DirectorySelection(String relatviePath, PathModifier pm)
	{
		this.pm = pm;
		if(!relatviePath.startsWith("."))
		{
			relatviePath = "." + relatviePath;
		}
		this.relativePath = relatviePath;
		this.linuxPath = PathUtility.getPathLinux(relatviePath);
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
		return PathUtility.getCurrentDirectory() + this.relativePath.substring(1, this.relativePath.length());
	}
}
