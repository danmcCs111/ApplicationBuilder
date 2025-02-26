package Editors;

import java.awt.Component;
import java.awt.event.ActionListener;

import EditorAbstract.ListenerEditor;

public class ActionListenerEditor extends ListenerEditor
{
	private static final long serialVersionUID = 1990L;

	private static final String 
		EDITOR_DIRECTORY = "/src/ActionListenersImpl/ ",
		PACKAGE_PREFIX = "ActionListenersImpl",
		EDITOR_PARAMETER_FILE_EXTENSION = ".java",
		EDITOR_PARAMETER_FILE_FILTER = "java",
		EDIT_BUTTON_TEXT = "Edit";
	
	private static String //ehh static.
		editorDirectory = EDITOR_DIRECTORY,
		packagePrefix = PACKAGE_PREFIX,
		editorParamFileExtension = EDITOR_PARAMETER_FILE_EXTENSION,
		editorParamFileFilter = EDITOR_PARAMETER_FILE_FILTER,
		editButtonText = EDIT_BUTTON_TEXT;
	
	public ActionListenerEditor()
	{
		super();
	}
	
	@Override
	public String getParameterDefintionString() 
	{
		return ActionListener.class.getName();
	}

	@Override
	public String getEditorDirectory() 
	{
		return editorDirectory;
	}

	@Override
	public String getPackagePrefix() 
	{
		return packagePrefix;
	}

	@Override
	public String getFileExtension() 
	{
		return editorParamFileExtension;
	}

	@Override
	public String getFileFilter() 
	{
		return editorParamFileFilter;
	}

	@Override
	public String getEditButtonText() 
	{
		return editButtonText;
	}

	@Override
	public void setEditorDirectory(String editorDirectory) 
	{
		this.editorDirectory = editorDirectory;
	}

	@Override
	public void setPackagePrefix(String packagePrefix) 
	{
		this.packagePrefix = packagePrefix;
	}

	@Override
	public void setFileExtension(String fileExtension) 
	{
		this.editorParamFileExtension = fileExtension;
	}

	@Override
	public void setFileFilter(String fileFilter) 
	{
		this.editorParamFileFilter = fileFilter;
	}

	@Override
	public void setEditButtonText(String editButtonText) 
	{
		this.editButtonText = editButtonText;
	}

	@Override
	public Component getComponentEditor() 
	{
		return this;
	}
	
}
