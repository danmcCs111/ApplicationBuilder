package ObjectTypeConversionEditors;

import java.awt.Component;
import java.awt.event.WindowListener;

import EditorInterfaces.ListenerEditor;

public class WindowListenerEditor extends ListenerEditor
{
	private static final long serialVersionUID = 2000L;

	private static final String 
		EDITOR_DIRECTORY = "/src/WindowListeners/",
		PACKAGE_PREFIX = "WindowListeners",
		EDITOR_PARAMETER_FILE_EXTENSION = ".java",
		EDITOR_PARAMETER_FILE_FILTER = "java",
		EDIT_BUTTON_TEXT = "Edit";

	private static String 
		editorDirectory = EDITOR_DIRECTORY,
		packagePrefix = PACKAGE_PREFIX,
		editorParamFileExtension = EDITOR_PARAMETER_FILE_EXTENSION,
		editorParamFileFilter = EDITOR_PARAMETER_FILE_FILTER,
		editButtonText = EDIT_BUTTON_TEXT;
	
	@Override
	public String getParameterDefintionString() 
	{
		return WindowListener.class.getName();
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
		WindowListenerEditor.editorDirectory = editorDirectory;
	}

	@Override
	public void setPackagePrefix(String packagePrefix) 
	{
		WindowListenerEditor.packagePrefix = packagePrefix;
	}

	@Override
	public void setFileExtension(String fileExtension) 
	{
		WindowListenerEditor.editorParamFileExtension = fileExtension;
	}

	@Override
	public void setFileFilter(String fileFilter) 
	{
		WindowListenerEditor.editorParamFileFilter = fileFilter;
	}

	@Override
	public void setEditButtonText(String editButtonText) 
	{
		WindowListenerEditor.editButtonText = editButtonText;
	}
	
	@Override
	public Component getComponentEditor() 
	{
		return this;
	}	
	
}
