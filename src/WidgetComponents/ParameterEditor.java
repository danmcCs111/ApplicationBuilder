package WidgetComponents;

import java.awt.Component;
import java.awt.FontMetrics;

import javax.swing.JLabel;

public abstract class ParameterEditor 
{
	public abstract Component getComponentEditor();
	public abstract String getComponentXMLOutput();
	
	public JLabel getFieldLabel(String labelText)
	{
		return new JLabel(labelText);
	}
	
	public int getFieldLabelWidth(JLabel label)
	{
		FontMetrics fm = label.getFontMetrics(label.getFont());
		int width = fm.stringWidth(label.getText());
		return width;
	}
	
	public boolean isType(String parameterDefStringName)
	{
		return parameterDefStringName == null 
			? false 
			: getParameterDefintionString().toLowerCase().equals(parameterDefStringName.toLowerCase());
	}
	
	public abstract String getParameterDefintionString();
}
