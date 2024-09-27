package WidgetComponents;

import java.util.ArrayList;
import java.util.HashMap;

import ApplicationBuilder.LoggingMessages;

public class ParameterEditorParser {
	
	private static final HashMap<String, ParameterEditor> editorTypes = new HashMap<String, ParameterEditor>();
	static {
		editorTypes.put("boolean", new BooleanEditor());
		editorTypes.put("int", new IntegerEditor());
		editorTypes.put("java.awt.Color", new ColorEditor());
		editorTypes.put("java.lang.String", new StringEditor());
		editorTypes.put("float", new FloatEditor());
	}

	public ParameterEditorParser()
	{
		
	}
	
	public static ArrayList<String> parseMethodParamsToList(String methodText)
	{
		ArrayList<String> methodParams = new ArrayList<String>();
		
		String [] tmp = methodText.split("\\[");
//		String methodName = tmp[0].trim;
		String methodParam = tmp[1].split("\\]")[0];
		
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
	
	public static void launchEditor()
	{
		new EditParameterFrame();
	}
}
