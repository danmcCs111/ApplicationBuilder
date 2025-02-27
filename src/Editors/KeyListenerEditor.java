package Editors;

import java.awt.Component;
import java.awt.event.KeyListener;

import EditorAbstract.ListenerEditor;

public class KeyListenerEditor extends ListenerEditor 
{
	private static final long serialVersionUID = 1L;

	private static final String 
		KEY_LISTENERS_DIRECTORY = "/src/KeyListenersImpl/",
		PACKAGE_PREFIX = "KeyListenersImpl",
		EDITOR_PARAMETER_FILE_EXTENSION = ".java",
		EDITOR_PARAMETER_FILE_FILTER = "java",
		EDIT_BUTTON_TEXT = "Edit";

	private static String 
		keyListenersDirectory = KEY_LISTENERS_DIRECTORY,
		packagePrefix = PACKAGE_PREFIX,
		editorParamFileExtension = EDITOR_PARAMETER_FILE_EXTENSION,
		editorParamFileFilter = EDITOR_PARAMETER_FILE_FILTER,
		editButtonText = EDIT_BUTTON_TEXT;

	@Override
	public Component getComponentEditor() 
	{
		return this;
	}

	@Override
	public String getParameterDefintionString() 
	{
		return KeyListener.class.getName();
	}

	@Override
	public String getEditorDirectory() 
	{
		return keyListenersDirectory;
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
		KeyListenerEditor.keyListenersDirectory = editorDirectory;
	}

	@Override
	public void setPackagePrefix(String packagePrefix) 
	{
		KeyListenerEditor.packagePrefix = packagePrefix;
	}

	@Override
	public void setFileExtension(String fileExtension) 
	{
		KeyListenerEditor.editorParamFileExtension = fileExtension;
	}

	@Override
	public void setFileFilter(String fileFilter) 
	{
		KeyListenerEditor.editorParamFileFilter = fileFilter;
	}

	@Override
	public void setEditButtonText(String editButtonText) 
	{
		KeyListenerEditor.editButtonText = editButtonText;
	}

}
