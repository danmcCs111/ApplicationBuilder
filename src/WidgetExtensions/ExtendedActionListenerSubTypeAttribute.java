package WidgetExtensions;

public class ExtendedActionListenerSubTypeAttribute 
{
	private static final String METHOD_DEF = "extendedActionListenerSubTypeAttribute [java.lang.String arg0]";

	public static String getMethodDefinition() 
	{
		return METHOD_DEF;
	}
	
	public static void applyMethod(ActionListenerExtension extension, String listenerSubTypeAttribute)
	{
		extension.applyActionListenerExtensionAttribute(listenerSubTypeAttribute);
	}
	
}
