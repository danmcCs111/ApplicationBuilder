package Editors;

import java.awt.Component;
import java.awt.event.ActionListener;

import EditorInterface.ListenerEditor;

public class ActionListenerEditor extends ListenerEditor
{
	private static final long serialVersionUID = 1990L;

	private static final String 
		EDITOR_DIRECTORY = "/src/ActionListenersImpl/ ",
		PACKAGE_PREFIX = "ActionListenersImpl",
		EDITOR_PARAMETER_FILE_EXTENSION = ".java",
		EDITOR_PARAMETER_FILE_FILTER = "java",
		EDIT_BUTTON_TEXT = "Edit";

	@Override
	public String getParameterDefintionString() 
	{
		return ActionListener.class.getName();
	}

	@Override
	public String getEditorDirectory() 
	{
		return EDITOR_DIRECTORY;
	}

	@Override
	public String getPackagePrefix() 
	{
		return PACKAGE_PREFIX;
	}

	@Override
	public String getFileExtension() 
	{
		return EDITOR_PARAMETER_FILE_EXTENSION;
	}

	@Override
	public String getFileFilter() 
	{
		return EDITOR_PARAMETER_FILE_FILTER;
	}

	@Override
	public String getEditButtonText() 
	{
		return EDIT_BUTTON_TEXT;
	}

	@Override
	public void setEditorDirectory(String editorDirectory) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPackagePrefix(String packagePrefix) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFileExtension(String fileExtension) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFileFilter(String fileFilter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEditButtonText(String editButtonText) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Component getComponentEditor() {
		return this;
	}
	
}
