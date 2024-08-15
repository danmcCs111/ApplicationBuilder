package WidgetUtility;

import java.util.ArrayList;
import java.util.HashMap;

import ApplicationBuilder.LoggingMessages;

public class WidgetCreatorProperty {
	
	private String component;
	private ArrayList<String> settings;
	
	private HashMap<String, String> settingsNameAndValue = new HashMap<String, String>();
	
	public WidgetCreatorProperty(String component, ArrayList<String> settings)
	{
		this.component = component;
		this.settings = settings;
		LoggingMessages.printOut("Component: " + component);
		for(String s : settings)
			splitAttributeNameAndValue(s);
		LoggingMessages.printOut(getSettingsNameAndValue().toString());
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
	
	private void splitAttributeNameAndValue(String attribute)
	{
		String [] ss = attribute.split("=");
		settingsNameAndValue.put(ss[0], ss[1]);
	}
}
