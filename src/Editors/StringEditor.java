package Editors;

import java.awt.Component;

import javax.swing.JTextField;

import WidgetComponents.ParameterEditor;

public class StringEditor extends ParameterEditor
{
	private JTextField textField = null;
	
	@Override
	public Component getComponentEditor() 
	{
		if(textField == null)
		{
			textField = new JTextField();
		}
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

	@Override
	public void setComponentValue(Object value) 
	{
		textField.setText((String)value);
	}

	@Override
	public String[] getComponentValue() 
	{
		return new String [] {textField.getText()};
	}

}
