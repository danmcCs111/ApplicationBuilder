package WidgetExtensions;

import java.awt.Component;

import javax.swing.JComponent;

import ApplicationBuilder.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public interface ExtendedAttributeStringParam 
{
	public static final String 
		METHOD_ARG_DEF = " [java.lang.String arg0]",
		PATHS_DELIMITER = ";",
		ARG_DELIMITER = "@";
	
	public static String getMethodDefinition(Class<? extends ExtendedAttributeStringParam> clazz) 
	{
		String [] className = clazz.getName().split("\\.");
		String methodDef = className[className.length-1];
		String methodBeginCase = methodDef.substring(0, 1);
		
		methodDef = methodBeginCase.toLowerCase() + methodDef.substring(1, methodDef.length()) + METHOD_ARG_DEF;
		
		return methodDef;
	}
	
	public abstract void applyMethod(String arg0, WidgetBuildController wbc, WidgetCreatorProperty widgetProperties);
	
	public static JComponent findComponent(WidgetBuildController wbc, Class<?> clazz)
	{
		for(WidgetCreatorProperty wcp : wbc.getWidgetCreationProperties())
		{
			if(wcp.getInstance().getClass().equals(clazz))
			{
				return (JComponent) wcp.getInstance();
			}
		}
		return null;
	}
	
	public static Component findComponentByName(WidgetBuildController wbc, String name)
	{
		for(WidgetCreatorProperty wcp : wbc.getWidgetCreationProperties())
		{
			Component c = (Component) wcp.getInstance();
			if(name.equals(c.getName()))
			{
				return (Component) wcp.getInstance();
			}
		}
		return null;
	}
	
}
