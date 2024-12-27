package WidgetUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ApplicationBuilder.LoggingMessages;
import Params.XmlToWidgetGenerator;
import WidgetComponents.ClassTypeHandler;

public class WidgetCreatorProperty 
{
	private String 
		component,
		parentNodeText,
		refId;
	private Object instance;
	private ArrayList<String> settings;
	private List<XmlToWidgetGenerator> xmlToWidgetGenerators = new ArrayList<XmlToWidgetGenerator>();
	
	private static final String ID_POSTFIX = "#";
	private static int postfixCounter = 0;

	private HashMap<String, String> settingsNameAndValue = new HashMap<String, String>();

	public WidgetCreatorProperty(String component, ArrayList<String> settings, String parentNodeText) 
	{
		this.component = component;
		this.settings = settings;
		for (String s : settings)
		{
			splitAttributeNameAndValue(s);
		}
		this.setRefId(component);
		this.parentNodeText = parentNodeText;
	}
	
	public Object getInstance()
	{
		if(instance == null)
		{
			try {
				Class<?> c = Class.forName(component);
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
