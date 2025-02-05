package WidgetComponents;

import java.awt.Component;
import java.awt.FontMetrics;

import javax.swing.JLabel;

public abstract class ParameterEditor 
{
	public abstract Component getComponentEditor();
	public abstract void setComponentValue(Object value);
	public abstract String getComponentXMLOutput();
	
	public ParameterEditor newInstance()
	{
		try {
			return ((ParameterEditor) getClass().forName(this.getClass().getName()).newInstance());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
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
	
	public boolean isType(Class<?> parameterDefClassName)
	{
		return parameterDefClassName == null 
			? false 
			: getParameterDefintionString().toLowerCase().equals(parameterDefClassName.getName().toLowerCase());
	}
	
	public abstract String getParameterDefintionString();
}
