package WidgetExtensions;

public enum ExtendedMethodArgDef 
{
	ExtendedFileSelection(" [ObjectTypeConversion.FileSelection arg0]"),
	ExtendedDirectorySelection(" [ObjectTypeConversion.DirectorySelection arg0]"),
	CommandBuildSelection(" [ObjectTypeConversion.CommandBuild arg0]"),
	CsvReaderSelection(" [ObjectTypeConversion.CsvReader arg0]"),
	NameIdSelection(" [ObjectTypeConversion.NameId arg0]"),
	WavReaderSelection(" [ObjectTypeConversion.WavReader arg0]"),
	ExtendedScrollBarSetUnit(" [int arg0]"),
	ImageMouseAdapter(" [java.lang.String arg0, boolean arg1]"),
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
