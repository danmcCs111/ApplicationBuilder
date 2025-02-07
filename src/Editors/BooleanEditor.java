package Editors;

import java.awt.Component;

import javax.swing.JComboBox;

import WidgetComponents.ParameterEditor;

public class BooleanEditor extends ParameterEditor
{
	private JComboBox<String> trueOrFalse = null;
	
	@Override
	public Component getComponentEditor() 
	{
		if(trueOrFalse == null)
		{
			new JComboBox<String>();
			trueOrFalse.addItem("");
			trueOrFalse.addItem("true");
			trueOrFalse.addItem("false");
		}
		return trueOrFalse;
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
		return "boolean";
	}

	@Override
	public void setComponentValue(Object value) 
	{
		trueOrFalse.setSelectedItem(value);
	}

	@Override
	public String [] getComponentValue() 
	{
		return new String [] {trueOrFalse.getSelectedItem().toString()};
	}

}
