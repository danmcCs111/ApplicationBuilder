package WidgetComponents;

import java.util.ArrayList;

import javax.swing.JFrame;

import ApplicationBuilder.LoggingMessages;
import Properties.PathUtility;

public class ParameterEditorParser 
{
	private static final String 
		EDITOR_DIRECTORY = "\\src\\Editors\\ ",
		PACKAGE_PREFIX = "Editors",
		EDITOR_PARAMETER_FILE_PREFIX = "\\.java",
		EDITOR_PARAMETER_FILE_FILTER = "java";
	
	private static final ArrayList<ParameterEditor> editorTypes = new ArrayList<ParameterEditor>();
	static {
		printParameterEditorExtensions();
	}

	/**
	 * @param methodText
	 * @return method name and parameters
	 */
	public static ArrayList<String> parseMethodParamsToList(String methodText)
	{
		return parseMethodParamsToList(methodText, false);
	}
	/**
	 * @param methodText
	 * @return parameters and method name if given false flag
	 */
	public static ArrayList<String> parseMethodParamsToList(String methodText, boolean paramsOnly)
	{
		ArrayList<String> methodParams = new ArrayList<String>();
		
		String [] tmp = methodText.split("\\[");
		String methodName = tmp[0].trim();
		String methodParam = tmp[1].split("\\]")[0];
		if(!paramsOnly)
			methodParams.add(methodName);
		
		String [] tmps = methodParam.split(",");
		for(String s : tmps)
		{
			String s2 = s.trim().split(" arg[0-9]*")[0];
			methodParams.add(s2);
		}
		methodParams.toArray(new String[methodParams.size()]);
		
		return methodParams;
	}
	
	public static ParameterEditor getParameterEditor(String paramDefNamedType)
	{
		for(ParameterEditor p : editorTypes)
		{
			if (p.isType(paramDefNamedType))
			{
				LoggingMessages.printOut("found editor: " + p.getClass().getName());
				return p;
			}
		}
		return null;
	}
	
	public static JFrame launchEditor(String methodName)
	{
		EditParameterFrame pFrame = new EditParameterFrame(methodName);
		pFrame.addSaveAndCancelButtons();
		return pFrame;
	}
	
	public static void printParameterEditorExtensions()
	{
		LoggingMessages.printOut("Param extensions");
		ArrayList<String> files = PathUtility.getOSFileList(PathUtility.getCurrentDirectory() + EDITOR_DIRECTORY, EDITOR_PARAMETER_FILE_FILTER);
		for(String file : files)
		{
			try {
				Class<?> c = Class.forName(PACKAGE_PREFIX + "." + file.replaceAll(EDITOR_PARAMETER_FILE_PREFIX, ""));
				editorTypes.add((ParameterEditor) c.newInstance());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
}
