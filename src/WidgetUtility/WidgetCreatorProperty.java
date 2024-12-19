package WidgetUtility;

import java.util.ArrayList;
import java.util.HashMap;
import ApplicationBuilder.LoggingMessages;
import WidgetComponents.ClassTypeHandler;

public class WidgetCreatorProperty {

	private String component;
	private String parentNodeText;
	private ArrayList<String> settings;
	private String refId;
	
	private static final String ID_POSTFIX = "#";
	private static int postfixCounter = 0;

	private HashMap<String, String> settingsNameAndValue = new HashMap<String, String>();

	public WidgetCreatorProperty(String component, ArrayList<String> settings, String parentNodeText) {
		this.component = component;
		this.settings = settings;
		for (String s : settings)
		{
			splitAttributeNameAndValue(s);
		}
		this.setRefId(component);
		this.parentNodeText = parentNodeText;
	}

	public WidgetComponent getComponentType() {
		return WidgetComponent.getWidgetComponent(this.component);
	}
	
	public ClassTypeHandler getClassType()
	{
		return getComponentType().getComponentClassType();
	}

	public ArrayList<String> getSettings() {
		return this.settings;
	}

	public HashMap<String, String> getSettingsNameAndValue() {
		return this.settingsNameAndValue;
	}

	public String getParentRef()
	{
		return this.parentNodeText;
	}

	public boolean isThisParentRef(String parentRef) {
		return refId.equals(parentRef);
	}

	private void splitAttributeNameAndValue(String attribute) {
		String[] ss = attribute.split("=");
		settingsNameAndValue.put(ss[0], ss[1]);
	}
	private void setRefId(String component) {
		
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
			out.append(LoggingMessages.combine(settings));
		return out.toString();
	}
}
