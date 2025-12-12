package WidgetExtensionDefs;

public enum ExtendedMethodArgDef 
{
	ExtendedFileSelection(" [ObjectTypeConversion.FileSelection arg0]"),
	ExtendedDirectorySelection(" [ObjectTypeConversion.DirectorySelection arg0]"),
	CommandBuildSelection(" [ObjectTypeConversion.CommandBuild arg0]"),
	CsvReaderSelection(" [ObjectTypeConversion.CsvReader arg0]"),
	ColorSelection(" [java.awt.Color arg0]"),
	NameIdSelection(" [ObjectTypeConversion.NameId arg0]"),
	LookAndFeelClassNameSelection(" [ObjectTypeConversion.LookAndFeelClassName arg0]"),
	BooleanSelection(" [boolean arg0]"),
	NameIdSendAndReceiveSelection(" [ObjectTypeConversion.NameId arg0, ObjectTypeConversion.NameId arg1]"),
	WavReaderSelection(" [ObjectTypeConversion.WavReader arg0]"),
	ExtendedScrollBarSetUnit(" [int arg0]"),
	ImageMouseAdapter(" [ObjectTypeConversion.NameId arg0, boolean arg1]"),
	PageParserCollection(" [ObjectTypeConversion.PageParserCollection arg0"),
	PathArrayProcessingFolders(" [ObjectTypeConversion.PathArrayProcessing arg0]"),
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
