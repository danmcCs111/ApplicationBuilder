package WidgetComponents;

import java.util.ArrayList;

import javax.swing.JFrame;

import ApplicationBuilder.LoggingMessages;

public class ParameterEditorParser 
{
	private static final ArrayList<ParameterEditor> editorTypes = new ArrayList<ParameterEditor>();
	static {
		editorTypes.add(new BooleanEditor());
		editorTypes.add(new IntegerEditor());
		editorTypes.add(new ColorEditor());
		editorTypes.add(new StringEditor());
		editorTypes.add(new FloatEditor());
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
}
