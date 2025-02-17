package Params;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import Properties.PathUtility;
import WidgetComponents.LoggingMessages;

public class ParameterEditorParser 
{
	private static final String 
		EDITOR_DIRECTORY = "/src/Editors",
		PACKAGE_PREFIX = "Editors",
		EDITOR_PARAMETER_FILE_PREFIX = ".java",
		EDITOR_PARAMETER_FILE_FILTER = "java";
	
	private static final ArrayList<ParameterEditor> editorTypes = new ArrayList<ParameterEditor>();
	static {
		loadParameterEditorExtensions();
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
	
	
	public static ParameterEditor getParameterEditor(Class<?> paramDefClass)
	{
		return getParameterEditor(paramDefClass.getName());
	}
	public static ParameterEditor getParameterEditor(String paramDefNamedType)
	{
		for(ParameterEditor p : editorTypes)
		{
			if (ParameterUtility.isType(paramDefNamedType, p))
			{
				LoggingMessages.printOut("found editor: " + p.getClass().getName());
				return ParameterUtility.newInstance(p.getClass());
			}
		}
		return null;
	}
	
	private static void loadParameterEditorExtensions()
	{
		ArrayList<String> files = PathUtility.getOSFileList(PathUtility.getCurrentDirectory() + EDITOR_DIRECTORY, EDITOR_PARAMETER_FILE_FILTER);
		for(String file : files)
		{
			try {
				Class<?> c = Class.forName(PACKAGE_PREFIX + "." + file.replaceAll(EDITOR_PARAMETER_FILE_PREFIX, ""));
				editorTypes.add((ParameterEditor) c.getDeclaredConstructor().newInstance());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
	}
}
