package WidgetExtensions;

public class ExtendedActionListenerSubTypeAttribute implements ExtendedAttributeStringParam
{
	public static void applyMethod(ActionListenerExtension extension, String listenerSubTypeAttribute)
	{
		extension.applyActionListenerExtensionAttribute(listenerSubTypeAttribute);
	}
	
}
