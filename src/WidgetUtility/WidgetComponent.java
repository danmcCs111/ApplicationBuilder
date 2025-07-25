package WidgetUtility;

import java.awt.Component;
import java.awt.SystemTray;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JComponent;

import Properties.LoggingMessages;

public class WidgetComponent 
{
	public static final String ID_SPLIT = "#";
	private static final String [] PACKAGE_PREFIXES = new String [] {
			"java.awt.", 
			"WidgetComponents.",
			"ShapeWidgetComponents.",
			"javax.swing."
	};
	private static int counter = 0;

	private String componentLabel;
	private JComponent jComponent = null;
	private Component component = null;
	private SystemTray systemTray = null;
	
	public static int nextCountId()
	{
		return counter++;
	}
	
	public static int getCountId()
	{
		return counter;
	}
	
	private WidgetComponent(String componentLabel, JComponent jComponent)
	{
		this.componentLabel = componentLabel;
		this.jComponent = jComponent;
	}
	private WidgetComponent(String componentLabel, Component component)
	{
		this.componentLabel = componentLabel;
		this.component = component;
	}
	private WidgetComponent(String componentLabel, SystemTray systemTray)
	{
		this.componentLabel = componentLabel;
		this.systemTray = systemTray;
	}
	
	public static void resetIDCounter()
	{
		counter = 0;
	}
	
	public String getLabelStr()
	{
		return this.componentLabel;
	}
	
	public static WidgetComponent getWidgetComponent(String text)
	{
		for(String packPre : PACKAGE_PREFIXES)
		{
			WidgetComponent wc = getWidgetComponent(text, packPre);
			if(wc != null)
			{
				return wc;
			}
		}
		
		LoggingMessages.printOut("ERROR: " + text);
		return null;
	}
	
	public static WidgetComponent getWidgetComponent(String text, String packagePrefix)
	{
		Class <?> c = null;
		Object o = null;
		try {
			c = Class.forName(packagePrefix + text);
			o = c.getDeclaredConstructor().newInstance();
			if (o instanceof JComponent) {
				JComponent component = (JComponent) o;
				return new WidgetComponent(text, component);
			}
			else if (o instanceof Component)
			{
				Component component = (Component) o;
				return new WidgetComponent(text, component);
			}
		} catch (ClassNotFoundException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
			if(c.toGenericString().endsWith(packagePrefix + "SystemTray"))
			{ 
				SystemTray component = SystemTray.getSystemTray();
				return new WidgetComponent(text, component);
			}
		} catch (IllegalArgumentException e) {
		} catch (InvocationTargetException e) {
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
		}
		return null;
	}
	
	public ClassTypeHandler getComponentClassType()
	{
		ClassTypeHandler c = null;
		Object o = getComponentObject();
		
		if (o instanceof JComponent)
		{
			c = new JComponentClassTypeHandler(o);
		}
		else if(o instanceof Component)
		{
			c = new ComponentClassTypeHandler(o);
		}
		
		else if(o instanceof SystemTray)
		{
			c = new SystemTrayClassTypeHandler(o);
		}
		return c;
	}
	private Object getComponentObject()
	{
		Object o = null;
		if(jComponent != null)
		{
			o = jComponent;
		}
		else if (component != null)
		{
			o = component;
		}
		else if (systemTray != null)
		{
			o = systemTray;
		}
		return o;
	}
	
}
