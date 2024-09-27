package WidgetComponents;

import java.util.ArrayList;
import java.util.HashMap;

import ApplicationBuilder.LoggingMessages;

public class ParameterEditorParser {
	
	private static final ArrayList<ParameterEditor> editorTypes = new ArrayList<ParameterEditor>();
	static {
		editorTypes.add(new BooleanEditor());
		editorTypes.add(new IntegerEditor());
		editorTypes.add(new ColorEditor());
		editorTypes.add(new StringEditor());
		editorTypes.add(new FloatEditor());
	}

	public ParameterEditorParser()
	{
		
	}
	
	public static ArrayList<String> parseMethodParamsToList(String methodText)
	{
		ArrayList<String> methodParams = new ArrayList<String>();
		
		String [] tmp = methodText.split("\\[");
		String methodName = tmp[0].trim();
		String methodParam = tmp[1].split("\\]")[0];
		methodParams.add(methodName);
		
		String [] tmps = methodParam.split(",");
		for(String s : tmps)
		{
			String s2 = s.trim().split(" arg[0-9]*")[0];
			methodParams.add(s2);
			LoggingMessages.printOut(s2);
		}
		methodParams.toArray(new String[methodParams.size()]);
		
		return methodParams;
	}
	
	public static ParameterEditor getParameterEditor(String param)
	{
		for(ParameterEditor p : editorTypes)
		{
			if (p.isType(param))
			{
				LoggingMessages.printOut("found editor: " + p.getClass().getName());
				return p;
			}
		}
		return null;
	}
	
	public static void launchEditor(String methodName)
	{
		new EditParameterFrame(methodName);
	}
}
