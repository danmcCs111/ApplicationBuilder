package WidgetUtility;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Node;

import ApplicationBuilder.LoggingMessages;

public class WidgetCreatorProperty {
	
	private String component;
	private ArrayList<String> settings;
	private String refId;
	
	private HashMap<String, String> settingsNameAndValue = new HashMap<String, String>();
	
	public WidgetCreatorProperty(String component, ArrayList<String> settings, String parentNodeText)
	{
		this.component = component;
		this.settings = settings;
		LoggingMessages.printOut("Component: " + component);
		for(String s : settings)
			splitAttributeNameAndValue(s);
		LoggingMessages.printOut("Attributes: " + getSettingsNameAndValue().toString());
		
		LoggingMessages.printOut("Parent: " + parentNodeText);
		LoggingMessages.printOut("");
	}
	
	public WidgetComponentType getComponentType()
	{
		return WidgetComponentType.getWidgetComponentType(this.component);
	}
	
	public ArrayList<String> getSettings()
	{
		return this.settings;
	}
	
	public HashMap<String, String> getSettingsNameAndValue()
	{
		return this.settingsNameAndValue;
	}
	
	public void setRefId(String refId)
	{
		LoggingMessages.printOut("RefId: " + refId + " Component: " + component);
		this.refId = refId;
	}
	
	private void splitAttributeNameAndValue(String attribute)
	{
		String [] ss = attribute.split("=");
		settingsNameAndValue.put(ss[0], ss[1]);
	}
}
