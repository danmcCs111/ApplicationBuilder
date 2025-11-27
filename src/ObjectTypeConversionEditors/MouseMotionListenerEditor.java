package ObjectTypeConversionEditors;

import java.awt.Component;
import java.awt.event.MouseMotionListener;

import EditorInterfaces.ListenerEditor;

public class MouseMotionListenerEditor extends ListenerEditor
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
	
	public MouseMotionListenerEditor()
	{
		super();
	}
	
	@Override
	public String getParameterDefintionString() 
	{
		return MouseMotionListener.class.getName();
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
		MouseMotionListenerEditor.editorDirectory = editorDirectory;
	}

	@Override
	public void setPackagePrefix(String packagePrefix) 
	{
		MouseMotionListenerEditor.packagePrefix = packagePrefix;
	}

	@Override
	public void setFileExtension(String fileExtension) 
	{
		MouseMotionListenerEditor.editorParamFileExtension = fileExtension;
	}

	@Override
	public void setFileFilter(String fileFilter) 
	{
		MouseMotionListenerEditor.editorParamFileFilter = fileFilter;
	}

	@Override
	public void setEditButtonText(String editButtonText) 
	{
		MouseMotionListenerEditor.editButtonText = editButtonText;
	}

	@Override
	public Component getComponentEditor() 
	{
		return this;
	}
}
