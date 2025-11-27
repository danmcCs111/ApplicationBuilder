package ObjectTypeConversionEditors;

import java.awt.Component;
import java.awt.event.MouseAdapter;

import EditorInterfaces.ListenerEditor;

public class MouseAdapterEditor extends ListenerEditor
{
	private static final long serialVersionUID = 1990L;

	private static final String 
		EDITOR_DIRECTORY = "/src/MouseListenersImpl/",
		PACKAGE_PREFIX = "MouseListenersImpl",
		EDITOR_PARAMETER_FILE_EXTENSION = ".java",
		EDITOR_PARAMETER_FILE_FILTER = "java",
		EDIT_BUTTON_TEXT = "Edit";
	
	private static String
		editorDirectory = EDITOR_DIRECTORY,
		packagePrefix = PACKAGE_PREFIX,
		editorParamFileExtension = EDITOR_PARAMETER_FILE_EXTENSION,
		editorParamFileFilter = EDITOR_PARAMETER_FILE_FILTER,
		editButtonText = EDIT_BUTTON_TEXT;
	
	public MouseAdapterEditor()
	{
		super();
	}
	
	@Override
	public String getParameterDefintionString() 
	{
		return MouseAdapter.class.getName();
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
		MouseAdapterEditor.editorDirectory = editorDirectory;
	}

	@Override
	public void setPackagePrefix(String packagePrefix) 
	{
		MouseAdapterEditor.packagePrefix = packagePrefix;
	}

	@Override
	public void setFileExtension(String fileExtension) 
	{
		MouseAdapterEditor.editorParamFileExtension = fileExtension;
	}

	@Override
	public void setFileFilter(String fileFilter) 
	{
		MouseAdapterEditor.editorParamFileFilter = fileFilter;
	}

	@Override
	public void setEditButtonText(String editButtonText) 
	{
		MouseAdapterEditor.editButtonText = editButtonText;
	}

	@Override
	public Component getComponentEditor() 
	{
		return this;
	}
}
