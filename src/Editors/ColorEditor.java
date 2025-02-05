package Editors;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JColorChooser;

import WidgetComponents.ParameterEditor;

public class ColorEditor extends ParameterEditor
{
	private JColorChooser jcc = null;
	
	@Override
	public Component getComponentEditor() 
	{
		if(jcc == null)
		{
			jcc = new JColorChooser();
		}
		return jcc;
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
		return "java.awt.color";
	}

	@Override
	public void setComponentValue(Object value) 
	{
		jcc.setColor((Color)value);
	}
}
