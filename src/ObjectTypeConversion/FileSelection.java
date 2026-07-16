package ObjectTypeConversion;

import Properties.PathUtility;
import WidgetComponentInterfaces.ParamOption.PathModifier;

public class FileSelection 
{
	private String 
		relativePath,
		linuxPath;
	private PathModifier 
		pm;
	private boolean
		isRelative;
	
	public FileSelection(String relativePath, boolean isRelative)
	{
		this(relativePath, PathModifier.none, isRelative);
	}
	
	public FileSelection(String relativePath)
	{
		this(relativePath, PathModifier.none, true);
	}
	
	public FileSelection(String relativePath, PathModifier pm, boolean isRelative)
	{
		this.isRelative = isRelative;
		if(isRelative)
		{
			this.pm = pm;
			if(!relativePath.startsWith("."))
			{
				relativePath = "." + relativePath;
			}
			
			this.relativePath = relativePath;
			this.linuxPath = PathUtility.getPathLinux(relativePath);
		}
		else
		{
			this.relativePath = relativePath;
			this.linuxPath = PathUtility.getPathLinux(relativePath);
		}
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
		if(isRelative)
		{
			return PathUtility.getCurrentDirectory() + this.relativePath.substring(1);
		}
		else
		{
			return this.relativePath;
		}
	}
}
