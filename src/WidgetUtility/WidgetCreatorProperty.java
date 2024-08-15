package WidgetUtility;

import java.util.ArrayList;

public class WidgetCreatorProperty {
	
	private String component;
	private ArrayList<String> settings;
	
	public WidgetCreatorProperty(String component, ArrayList<String> settings)
	{
		this.component = component;
		this.settings = settings;
	}
	
	public WidgetComponentType getComponentType()
	{
		return WidgetComponentType.getWidgetComponentType(this.component);
	}
	
	public ArrayList<String> getSettings()
	{
		return this.settings;
	}
}
