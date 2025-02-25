package WidgetExtensions;

public enum ExtendedMethodArgDef 
{
	ExtendedFileSelection(" [ObjectTypeConversion.FileSelection arg0]"),
	ExtendedDirectorySelection(" [ObjectTypeConversion.DirectorySelection arg0]"),
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
