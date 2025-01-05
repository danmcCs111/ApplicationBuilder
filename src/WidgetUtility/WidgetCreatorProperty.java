package WidgetUtility;

import java.awt.SystemTray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ApplicationBuilder.LoggingMessages;
import Params.XmlToWidgetGenerator;
import WidgetComponents.ClassTypeHandler;

public class WidgetCreatorProperty 
{
	private static final String ID_POSTFIX = "#";
	private static int postfixCounter = 0;
	
	private String 
		className,
		component,
		parentNodeText,
		refId;
	private Object instance;
	private ArrayList<String> settings;
	private List<XmlToWidgetGenerator> xmlToWidgetGenerators = new ArrayList<XmlToWidgetGenerator>();
	private HashMap<String, String> settingsNameAndValue = new HashMap<String, String>();

	public WidgetCreatorProperty(String componentName, ArrayList<String> settings, String parentNodeText) 
	{
		className = WidgetAttributes.getClassNameString(componentName);
		this.component = componentName;
		this.settings = settings;
		for (String s : settings)
		{
			splitAttributeNameAndValue(s);
		}
		this.setRefId(componentName);
		this.parentNodeText = parentNodeText;
	}
	
	public Object getInstance()
	{
		if(instance == null)
		{
			if(className.endsWith("SystemTray"))
			{
				instance = SystemTray.getSystemTray();
			}
			else
			{
				try {
					Class<?> c = Class.forName(className);
					instance = c.newInstance();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			LoggingMessages.printOut("Create new Instance: " + instance);
		}
		return instance;
	}

	public WidgetComponent getComponentType() 
	{
		return WidgetComponent.getWidgetComponent(this.component);
	}
	
	public ClassTypeHandler getClassType()
	{
		return getComponentType().getComponentClassType();
	}

	public ArrayList<String> getSettings() 
	{
		return this.settings;
	}

	public HashMap<String, String> getSettingsNameAndValue() 
	{
		return this.settingsNameAndValue;
	}

	public String getParentRef()
	{
		return this.parentNodeText;
	}

	public boolean isThisParentRef(String parentRef) {
		return refId.equals(parentRef);
	}
	
	public void addXmlToWidgetGenerator(XmlToWidgetGenerator xmlToWidgetGenerator)
	{
		if(xmlToWidgetGenerator != null)
			xmlToWidgetGenerators.add(xmlToWidgetGenerator);
	}
	
	public List<XmlToWidgetGenerator> getXmlToWidgetGenerators()
	{
		return this.xmlToWidgetGenerators;
	}

	private void splitAttributeNameAndValue(String attribute) {
		String[] ss = attribute.split("=");
		settingsNameAndValue.put(ss[0], ss[1]);
	}
	private void setRefId(String component) 
	{
		String postfix = ID_POSTFIX + postfixCounter++;
		this.refId = component + postfix;
	}
	
	@Override
	public String toString()
	{
		StringBuilder out = new StringBuilder();
		out.append("RefID: " + this.refId + " ");
		out.append("ParentNodeID: " + this.parentNodeText + " ");
		if(this.settings != null && this.settings.size() > 0)
		{
			out.append(LoggingMessages.combine(settings) + " ");
		}
		if(xmlToWidgetGenerators != null && !xmlToWidgetGenerators.isEmpty())
		{
			for(XmlToWidgetGenerator xg : xmlToWidgetGenerators)
				out.append("| \n ***XML Generator Object*** -> " + xg.toString() + " ");
		}
		return out.toString();
	}
}
