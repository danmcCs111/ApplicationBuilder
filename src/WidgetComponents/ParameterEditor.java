package WidgetComponents;

import java.awt.Component;

public interface ParameterEditor {

	public Component getComponentEditor();
	public String getComponentXMLOutput();
	public boolean isType(String parameterValueType);
}
