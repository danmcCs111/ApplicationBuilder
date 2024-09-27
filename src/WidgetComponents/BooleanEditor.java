package WidgetComponents;

import java.awt.Component;

public class BooleanEditor implements ParameterEditor{

	@Override
	public Component getComponentEditor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getComponentXMLOutput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isType(String parameterValueType) {
		// TODO Auto-generated method stub
		return parameterValueType.toLowerCase().equals("boolean");
	}

}
