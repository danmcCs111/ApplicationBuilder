package WidgetComponents;

import java.awt.Component;

import javax.swing.JColorChooser;

public class ColorEditor extends ParameterEditor
{
	@Override
	public Component getComponentEditor() 
	{
		JColorChooser jcc = new JColorChooser();
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
}
