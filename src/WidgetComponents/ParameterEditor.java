package WidgetComponents;

import java.awt.Component;
import java.awt.FontMetrics;

import javax.swing.JLabel;

public abstract class ParameterEditor {

	public abstract Component getComponentEditor();
	public abstract String getComponentXMLOutput();
	public abstract boolean isType(String parameterValueType);
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
}
