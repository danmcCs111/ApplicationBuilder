package WidgetComponents;

import java.awt.Component;

import javax.swing.JComboBox;

public class BooleanEditor extends ParameterEditor
{
	@Override
	public Component getComponentEditor() {
		JComboBox<String> trueOrFalse = new JComboBox<String>();
		trueOrFalse.addItem("");
		trueOrFalse.addItem("true");
		trueOrFalse.addItem("false");
		return trueOrFalse;
	}

	@Override
	public String getComponentXMLOutput() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isType(String parameterValueType) 
	{
		// TODO Auto-generated method stub
		return parameterValueType.toLowerCase().equals("boolean");
	}

}
