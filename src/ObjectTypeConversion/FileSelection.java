package ObjectTypeConversion;

import Properties.PathUtility;
import WidgetComponentInterfaces.ParamOption.PathModifier;

public class FileSelection 
{
	private String relatviePath;
	
	public FileSelection(String relatviePath)
	{
		this(relatviePath, PathModifier.none);
	}
	
	public FileSelection(String relatviePath, PathModifier pm)
	{
		if(!relatviePath.startsWith("."))
		{
			relatviePath = "." + relatviePath;
		}
		switch(pm)
		{
		case linux:
			this.relatviePath = PathUtility.getPathLinux(relatviePath);
			break;
		case none:
		default:
			this.relatviePath = relatviePath;
			break;
		}
		this.relatviePath = relatviePath;
	}
	
	public String getRelativePath()
	{
		return relatviePath;
	}
	
	public String getFullPath()
	{
		return PathUtility.getCurrentDirectory() + this.relatviePath;
	}
}
