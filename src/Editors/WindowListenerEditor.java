package Editors;

import java.awt.Component;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JComboBox;

import Params.ParameterEditor;

public class WindowListenerEditor implements ParameterEditor 
{
	private static final String 
		EDITOR_DIRECTORY = "/src/WindowListeners",
		PACKAGE_PREFIX = "WindowListeners",
		EDITOR_PARAMETER_FILE_EXTENSION = ".java",
		EDITOR_PARAMETER_FILE_FILTER = "java";
	
	private JComboBox<String> comboBox;
	
	@Override
	public Component getComponentEditor() 
	{
		if(comboBox == null)
		{
			ArrayList<String> obs = ParameterEditor.loadClassExtensionsAsString(
					EDITOR_DIRECTORY, 
					EDITOR_PARAMETER_FILE_EXTENSION, 
					PACKAGE_PREFIX, 
					EDITOR_PARAMETER_FILE_FILTER);
			comboBox = new JComboBox<String>(obs.toArray(new String [obs.size()]));
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
		return comboBox == null
				? new String [] {""}
				: new String [] {comboBox.getSelectedItem().toString()};
	}

	@Override
	public String getComponentXMLOutput() {
		return null;
	}

	@Override
	public String getParameterDefintionString() 
	{
		return WindowListener.class.getName();
	}

	@Override
	public Object getComponentValueObj() 
	{
		return comboBox.getSelectedItem();
	}

}
