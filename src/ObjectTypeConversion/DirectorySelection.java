package ObjectTypeConversion;

import Properties.PathUtility;

public class DirectorySelection 
{
	private String relatviePath;
	
	public DirectorySelection(String relatviePath)
	{
		if(!relatviePath.startsWith("."))
		{
			relatviePath = "." + relatviePath;
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
