package WidgetUtility;

import java.awt.SystemTray;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import Params.XmlToWidgetGenerator;
import Properties.LoggingMessages;

public class WidgetCreatorProperty 
{
	private static final String 
		ID_SUFFIX_REGEX = "#[0-9]*";
	
	private Object instance;
	private String 
		className,
		parentNodeTextWithID,
		componentTagRefId;
	private ArrayList<String> 
		settings,
		settingsName = new ArrayList<String>();
	private HashMap<String, String> settingsNameAndValue = new HashMap<String, String>();
	private ArrayList<XmlToWidgetGenerator> xmlToWidgetGenerators = new ArrayList<XmlToWidgetGenerator>();

	public WidgetCreatorProperty(String componentNameWithID, ArrayList<String> settings, String parentNodeTextWithID) 
	{
		this.setRefId(componentNameWithID);
		this.className = WidgetAttributes.getClassNameString(getRef());
		this.settings = settings;
		for (String s : settings)
		{
			splitAttributeNameAndValue(s);
		}
		this.parentNodeTextWithID = parentNodeTextWithID;
	}
	
	public void destroy()
	{
		instance = null;
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
		return WidgetComponent.getWidgetComponent(getRef());
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
	
	public String setParentRefWithID(String parentRefId)
	{
		return this.parentNodeTextWithID = parentRefId;
	}
	
	public static String stripRefWithID(String parentRefWithID)
	{
		return parentRefWithID.replaceAll(ID_SUFFIX_REGEX, "");
	}
	
	public String getParentRef()
	{
		return getParentRefWithID() == null
			? null
			: stripRefWithID(getParentRefWithID());
	}
	
	public String getRef()
	{
		return stripRefWithID(getRefWithID());
	}
	
	public String getRefWithID()
	{
		return this.componentTagRefId;
	}

	public boolean isThisRef(String refWithId) {
		return getRefWithID().equals(refWithId);
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
	private void setRefId(String componentWithID) 
	{
		this.componentTagRefId = componentWithID;
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
