package WidgetExtensions;

import ApplicationBuilder.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public interface ExtendedAttributeStringParam 
{
	public static final String METHOD_ARG_DEF = " [java.lang.String arg0]";
	
	public static String getMethodDefinition(Class<? extends ExtendedAttributeStringParam> clazz) 
	{
		String [] className = clazz.getName().split("\\.");
		String methodDef = className[className.length-1];
		String methodBeginCase = methodDef.substring(0, 1);
		
		methodDef = methodBeginCase.toLowerCase() + methodDef.substring(1, methodDef.length()) + METHOD_ARG_DEF;
		
		return methodDef;
	}
	
	public abstract void applyMethod(String arg0, WidgetBuildController wbc, WidgetCreatorProperty widgetProperties);
	
}
