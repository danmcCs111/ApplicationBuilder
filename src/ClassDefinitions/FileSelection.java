package ClassDefinitions;

import Properties.PathUtility;

public class FileSelection 
{
	private String relatviePath;
	
	public FileSelection(String relatviePath)
	{
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
