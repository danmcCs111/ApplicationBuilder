package WidgetUtility;

import java.awt.Component;
import java.awt.SystemTray;

import javax.swing.JComponent;

import ApplicationBuilder.LoggingMessages;
import WidgetComponents.ClassTypeHandler;
import WidgetComponents.ComponentClassTypeHandler;
import WidgetComponents.JComponentClassTypeHandler;
import WidgetComponents.SystemTrayClassTypeHandler;

public class WidgetComponent {
	
	private String 
		componentLabel;
	private static int counter = 0;
	
	private JComponent jComponent = null;
	private Component component = null;
	private SystemTray systemTray = null;
	
	
	public static String ID_SPLIT = "#";
	
	public String getNextCounterId()
	{
		return getLabelStr() + ID_SPLIT + counter++;
	}
	
	public String getLabelStr()
	{
		return this.componentLabel;
	}
	
	public static WidgetComponent getWidgetComponent(String text)
	{
		LoggingMessages.printOut(text);
		String [] packagePrefixes = new String [] {"java.awt.", "WidgetExtensions.", "javax.swing."};
		for(String packPre : packagePrefixes)
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
		Class<?>c = null;
		Object o = null;
		try {
			c = Class.forName(packagePrefix + text);
			o = c.newInstance();
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
}
