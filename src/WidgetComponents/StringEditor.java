package WidgetComponents;

import java.awt.Component;

import javax.swing.JTextField;

public class StringEditor extends ParameterEditor
{
	@Override
	public Component getComponentEditor() 
	{
		JTextField textField = new JTextField();
		return textField;
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
		return parameterValueType.toLowerCase().equals("java.lang.string");
	}

}
