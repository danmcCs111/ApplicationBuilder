package Editors;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;

import WidgetComponents.ParameterEditor;

public class ColorEditor extends ParameterEditor
{
	private JButton colorPicker = null;
	private JColorChooser jcc = null;
	
	@Override
	public Component getComponentEditor() 
	{
		if(colorPicker == null)
		{
			colorPicker = new JButton();
			jcc = new JColorChooser();
			colorPicker.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JDialog d = new JDialog();
					d.add(jcc);
					d.setVisible(true);
					d.pack();
				}
			});
		}
		return colorPicker;
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
		colorPicker.setForeground((Color)value);
		colorPicker.setText("[-Color Select-]");
		jcc.setColor((Color)value);
	}

	@Override
	public String[] getComponentValue() 
	{
		Color c = jcc.getColor();
		return new String [] {c.getRed()+"", c.getGreen()+"",c.getBlue()+""};
	}
}
