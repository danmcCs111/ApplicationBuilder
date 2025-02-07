package Editors;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JSpinner;

import WidgetComponents.ParameterEditor;

public class IntegerEditor extends ParameterEditor
{
	private static final Dimension SPINNER_SIZE = new Dimension(50, 50);
	private JSpinner js = null;
	
	@Override
	public Component getComponentEditor() 
	{
		if(js == null)
		{
			js = new JSpinner();
			js.setSize(SPINNER_SIZE.width, SPINNER_SIZE.height);
		}
		return js;
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
		return "int";
	}

	@Override
	public void setComponentValue(Object value) 
	{
		js.setValue(value);
	}

	@Override
	public String[] getComponentValue() 
	{
		return new String [] {js.getValue()+""};
	}

}
