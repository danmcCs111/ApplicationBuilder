package ObjectTypeConversion;

import Properties.PathUtility;
import WidgetComponentInterfaces.ParamOption.PathModifier;

public class DirectorySelection 
{
	private String 
		relativePath,
		linuxPath;
	private PathModifier 
		pm;
	private boolean
		isRelativePath = true;
	
	public DirectorySelection(String relativePath)
	{
		this(relativePath, PathModifier.none, true);
	}
	
	public DirectorySelection(String relativePath, boolean isRelativePath)
	{
		this(relativePath, PathModifier.none, isRelativePath);
	}
	
	public DirectorySelection(String relativePath, PathModifier pm)
	{
		this(relativePath, pm, true);
	}
	
	public DirectorySelection(String relatviePath, PathModifier pm, boolean isRelativePath)
	{
		this.isRelativePath = isRelativePath;
		if(isRelativePath)
		{
			this.pm = pm;
			if(!relatviePath.startsWith("."))
			{
				relatviePath = "." + relatviePath;
			}
			this.relativePath = relatviePath;
			this.linuxPath = PathUtility.getPathLinux(relatviePath);
		}
		else
		{
			this.relativePath = relatviePath;
			this.linuxPath = PathUtility.getPathLinux(relatviePath);
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
		if(isRelativePath)
		{
			if(PathUtility.isWindows())
			{
				return PathUtility.getCurrentDirectory() + this.relativePath.substring(1);
			}
			else
			{
				return PathUtility.getCurrentDirectory() + this.linuxPath.substring(1);
			}
		}
		else //direct.
		{
			if(PathUtility.isWindows())
			{
				return relativePath;
			}
			else
			{
				return linuxPath;
			}
		}
	}
}
