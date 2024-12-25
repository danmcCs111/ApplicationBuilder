package WidgetExtensions;

public class ExtendedActionListenerSubTypeAttribute extends ExtendedAttributeStringParam
{
	public static void applyMethod(ActionListenerExtension extension, String listenerSubTypeAttribute)
	{
		extension.applyActionListenerExtensionAttribute(listenerSubTypeAttribute);
	}
	
}
