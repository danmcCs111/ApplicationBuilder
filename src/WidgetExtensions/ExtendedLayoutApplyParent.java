package WidgetExtensions;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import ApplicationBuilder.LoggingMessages;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedLayoutApplyParent implements ExtendedAttributeStringParam
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		LoggingMessages.printOut(widgetProperties.getParentRefWithID());
		WidgetCreatorProperty parentProp = WidgetBuildController.getInstance().findRef(widgetProperties.getParentRefWithID());
		Object parentComp = parentProp.getInstance();
		Object comp = widgetProperties.getInstance();
		
		invoke(parentComp, comp, arg0);
	}
	
	//Component, constraints
	private static void invoke(Object parentComp, Object comp, String arg0)
	{
		Method m;
		Class<?> [] cs = null; 
		Object [] os = null;
		
		if(arg0 == null || arg0.trim().equals(""))
		{
			cs = new Class<?> [] {Component.class}; 
			os = new Object [] {comp};
		}
		else
		{
			cs = new Class<?> [] {Component.class, Object.class}; 
			os = new Object [] {comp, arg0};
		}
		
		try {
			m = parentComp.getClass().getMethod("add", cs);
			m.invoke(parentComp, os);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
