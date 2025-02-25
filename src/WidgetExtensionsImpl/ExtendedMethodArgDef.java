package WidgetExtensionsImpl;

public enum ExtendedMethodArgDef 
{
	ExtendedFileSelection(" [ClassDefinitions.FileSelection arg0]"),
	ExtendedDirectorySelection(" [ClassDefinitions.DirectorySelection arg0]"),
	ExtendedAttributeStringParam(" [java.lang.String arg0]");
	
	private String methodArgDef;
	
	private ExtendedMethodArgDef(String methodArgDef)
	{
		this.methodArgDef = methodArgDef;
	}
	
	public String getMethodArgDef()
	{
		return this.methodArgDef;
	}
}
