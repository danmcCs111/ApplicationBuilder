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
	public String getParameterDefintionString() 
	{
		return "java.lang.string";
	}

}
