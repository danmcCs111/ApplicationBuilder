package WidgetExtensions;

import java.awt.Component;

import javax.swing.JComponent;

import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public interface ExtendedAttributeParam
{
	public static final String 
		PATHS_DELIMITER = ";",
		ARG_DELIMITER = "@";
	
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties);
	
	public static String getMethodDefinition(Class<? extends ExtendedAttributeParam> clazz, String methodArgDef) 
	{
		String [] className = clazz.getName().split("\\.");
		String 
			methodDef = className[className.length-1],
			methodBeginCase = methodDef.substring(0, 1);
		
		methodDef = methodBeginCase.toLowerCase() + methodDef.substring(1, methodDef.length()) + methodArgDef;
		
		return methodDef;
	}
	
	public static JComponent findComponent(Class<?> clazz)
	{
		for(WidgetCreatorProperty wcp : WidgetBuildController.getInstance().getWidgetCreatorProperties())
		{
			if(wcp.getInstance().getClass().equals(clazz))
			{
				return (JComponent) wcp.getInstance();
			}
		}
		return null;
	}
	
	public static Component findComponentByName(String name)
	{
		for(WidgetCreatorProperty wcp : WidgetBuildController.getInstance().getWidgetCreatorProperties())
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
