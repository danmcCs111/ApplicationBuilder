package Editors;

import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import Params.ParameterEditor;

public class ActionListenerEditor extends JComboBox<String> implements ParameterEditor
{
	private static final long serialVersionUID = 1990L;

	private static final String 
		EDITOR_DIRECTORY = "/src/ActionListeners",
		PACKAGE_PREFIX = "ActionListeners",
		EDITOR_PARAMETER_FILE_EXTENSION = ".java",
		EDITOR_PARAMETER_FILE_FILTER = "java";
	
	public ActionListenerEditor()
	{
		super(ParameterEditor.loadClassExtensionsAsString(
				EDITOR_DIRECTORY, 
				EDITOR_PARAMETER_FILE_EXTENSION, 
				PACKAGE_PREFIX, 
				EDITOR_PARAMETER_FILE_FILTER).toArray(new String[] {}));
	}
	
	@Override
	public Component getComponentEditor() 
	{
		return this;
	}

	@Override
	public void setComponentValue(Object value) 
	{
		if(value != null)
			this.setSelectedItem(value.getClass().getName());//using class name
	}

	@Override
	public String[] getComponentValue() 
	{
		return this == null
			? new String [] {""}
			: new String [] {this.getSelectedItem().toString()};
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

	@Override
	public Object getComponentValueObj() 
	{
		return this.getSelectedItem();
	}

}
