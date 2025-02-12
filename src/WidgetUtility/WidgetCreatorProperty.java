package WidgetUtility;

import java.awt.SystemTray;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import ApplicationBuilder.LoggingMessages;
import Params.XmlToWidgetGenerator;
import WidgetComponents.ClassTypeHandler;

public class WidgetCreatorProperty 
{
	private static final String 
		ID_POSTFIX = "#",
		ID_SUFFIX_REGEX = "#[0-9]*";
	private static int postfixCounter = 0;
	
	private Object instance;
	private String 
		className,
		componentTag,
		parentNodeTextWithID,
		parentNodeText,
		componentTagRefId;
	private ArrayList<String> 
		settings,
		settingsName = new ArrayList<String>();
	private HashMap<String, String> settingsNameAndValue = new HashMap<String, String>();
	private ArrayList<XmlToWidgetGenerator> xmlToWidgetGenerators = new ArrayList<XmlToWidgetGenerator>();

	public WidgetCreatorProperty(String componentName, ArrayList<String> settings, String parentNodeTextWithID) 
	{
		className = WidgetAttributes.getClassNameString(componentName);
		this.componentTag = componentName;
		this.settings = settings;
		for (String s : settings)
		{
			splitAttributeNameAndValue(s);
		}
		this.setRefId(componentName);
		this.parentNodeTextWithID = parentNodeTextWithID;
		if(this.parentNodeTextWithID != null)
		{
			this.parentNodeText = this.parentNodeTextWithID.replaceAll(ID_SUFFIX_REGEX, "");
		}
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
					instance = c.getDeclaredConstructor().newInstance();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			}
			LoggingMessages.printOut("Create new Instance: " + instance);
		}
		return instance;
	}

	public WidgetComponent getComponentType() 
	{
		return WidgetComponent.getWidgetComponent(this.componentTag);
	}
	
	public ClassTypeHandler getClassType()
	{
		return getComponentType().getComponentClassType();
	}

	public ArrayList<String> getSettings() 
	{
		return this.settings;
	}
	
	public ArrayList<String> getSettingsName()
	{
		return this.settingsName;
	}

	public HashMap<String, String> getSettingsNameAndValue() 
	{
		return this.settingsNameAndValue;
	}

	public String getParentRefWithID()
	{
		return this.parentNodeTextWithID;
	}
	
	public static String stripParentRefWithID(String parentRefWithID)
	{
		return parentRefWithID.replaceAll(ID_SUFFIX_REGEX, "");
	}
	
	public String getParentRef()
	{
		return this.parentNodeText;
	}
	
	public String getRef()
	{
		return this.componentTag;
	}
	
	public String getRefWithID()
	{
		return this.componentTagRefId;
	}

	public boolean isThisRef(String ref) {
		return componentTagRefId.equals(ref);
	}
	
	public void addXmlToWidgetGenerator(XmlToWidgetGenerator xmlToWidgetGenerator)
	{
		if(xmlToWidgetGenerator != null)
			xmlToWidgetGenerators.add(xmlToWidgetGenerator);
	}
	
	public ArrayList<XmlToWidgetGenerator> getXmlToWidgetGenerators()
	{
		return this.xmlToWidgetGenerators;
	}

	private void splitAttributeNameAndValue(String attribute) 
	{
		String[] ss = attribute.split("=");
		settingsNameAndValue.put(ss[0], ss[1]);
		settingsName.add(ss[0]);
	}
	private void setRefId(String component) 
	{
		String postfix = ID_POSTFIX + postfixCounter++;
		this.componentTagRefId = component + postfix;
	}
	
	public static void resetIDCounter()
	{
		postfixCounter = 0;
	}
	
	@Override
	public String toString()
	{
		StringBuilder out = new StringBuilder();
		out.append("RefID: " + this.componentTagRefId + " ");
		out.append("ParentNodeID: " + this.parentNodeTextWithID + " ");
		if(this.settings != null && this.settings.size() > 0)
		{
			out.append(LoggingMessages.combine(settings) + " ");
		}
		if(xmlToWidgetGenerators != null && !xmlToWidgetGenerators.isEmpty())
		{
			for(XmlToWidgetGenerator xg : xmlToWidgetGenerators)
			{
				out.append("| \n ***XML Generator Object*** -> " + xg.toString() + " ");
			}
		}
		return out.toString();
	}
}
