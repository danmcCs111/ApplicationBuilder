package ObjectTypeConversion;

import Properties.PathUtility;
import WidgetComponentInterfaces.ParamOption;

public class DirectorySelection 
{
	private String relatviePath;
	
	public DirectorySelection(String relatviePath)
	{
		this(relatviePath, ParamOption.PathModifier.none);
	}
	
	public DirectorySelection(String relatviePath, ParamOption.PathModifier pm)
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
