package Editors;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.JComboBox;

import Params.ParameterEditor;

public class LayoutEditor extends ParameterEditor 
{
	private JComboBox<String> comboBox;
	private static final String [] layoutManagers = new String [] {
			GridLayout.class.getName(),
			BorderLayout.class.getName(),
			FlowLayout.class.getName()
	};//for now just standard library managers
	
	@Override
	public Component getComponentEditor() 
	{
		if(comboBox == null)
		{
			comboBox = new JComboBox<String>(layoutManagers);
		}
		return comboBox;
	}

	@Override
	public void setComponentValue(Object value) 
	{
		comboBox.setSelectedItem(value.getClass().getName());//using class name
	}

	@Override
	public String[] getComponentValue() 
	{
		return new String [] {comboBox.getSelectedItem().toString()};
	}

	@Override
	public String getComponentXMLOutput() {
		return null;
	}

	@Override
	public String getParameterDefintionString() 
	{
		return LayoutManager.class.getName();
	}

}
