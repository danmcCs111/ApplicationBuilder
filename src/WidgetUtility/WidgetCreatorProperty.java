package WidgetUtility;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JComponent;

import org.w3c.dom.Node;

import ApplicationBuilder.LoggingMessages;
import WidgetComponents.ClassTypeHandler;

public class WidgetCreatorProperty {

	private String component;
	private ArrayList<String> settings;
	private String refId;

	private HashMap<String, String> settingsNameAndValue = new HashMap<String, String>();

	public WidgetCreatorProperty(String component, ArrayList<String> settings, String parentNodeText) {
		this.component = component;
		this.settings = settings;
		LoggingMessages.printOut("Component: " + component);
		for (String s : settings)
			splitAttributeNameAndValue(s);
		LoggingMessages.printOut("Attributes: " + getSettingsNameAndValue().toString());

		LoggingMessages.printOut("Parent: " + parentNodeText);
		LoggingMessages.printOut("");
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

	public void setRefId(String refId) {
		LoggingMessages.printOut("RefId: " + refId + " Component: " + component);
		this.refId = refId;
	}

	public boolean isThisParentRef(String parentRef) {
		return refId.equals(parentRef);
	}

	private void splitAttributeNameAndValue(String attribute) {
		String[] ss = attribute.split("=");
		settingsNameAndValue.put(ss[0], ss[1]);
	}
}
