package WidgetExtensions;

public class ExtendedAttributeStringParam 
{
	public static  String getMethodDefinition(String clazzName) 
	{
		String [] className = clazzName.split("\\.");
		String methodDef = className[className.length-1];
		String methodBeginCase = methodDef.substring(0, 1);
		
		methodDef = methodBeginCase.toLowerCase() + methodDef.substring(1, methodDef.length()) + " [java.lang.String arg0]";
		
		return methodDef;
	}

}
