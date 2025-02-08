package Editors;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;

import ApplicationBuilder.LoggingMessages;
import Params.ParameterEditor;

public class ActionListenerEditor extends ParameterEditor
{
	private static final String 
		EDITOR_DIRECTORY = "\\src\\ActionListeners\\ ",
		PACKAGE_PREFIX = "ActionListeners",
		EDITOR_PARAMETER_FILE_EXTENSION = "\\.java",
		EDITOR_PARAMETER_FILE_FILTER = "java";
	
	private JComboBox<String> componentEditor;
	
	@Override
	public Component getComponentEditor() 
	{
		if(componentEditor == null)
		{
			ArrayList<String> obs = loadClassExtensionsAsString(
					EDITOR_DIRECTORY, 
					EDITOR_PARAMETER_FILE_EXTENSION, 
					PACKAGE_PREFIX, 
					EDITOR_PARAMETER_FILE_FILTER);
			componentEditor = new JComboBox<String>(obs.toArray(new String [obs.size()]));
			
		}
		return componentEditor;
	}

	@Override
	public void setComponentValue(Object value) 
	{
		componentEditor.setSelectedItem(value.getClass().getName());//using class name
	}

	@Override
	public String[] getComponentValue() 
	{
		return new String [] {componentEditor.getSelectedItem().toString()};
	}

	@Override
	public String getComponentXMLOutput() 
	{
		return null;
	}

	@Override
	public String getParameterDefintionString() 
	{
		return ActionListener.class.getName();
	}

}
