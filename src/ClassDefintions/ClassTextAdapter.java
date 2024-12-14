package ClassDefintions;

import java.util.List;

import WidgetComponents.ParameterEditorParser;

public class ClassTextAdapter {
	
	public static void functionCall(Class<?> component, String methodDefintion, String method, String ...params)
	{
		parseParameterListFromMethodDefintion(methodDefintion);
	}
	
	private static List<String> parseParameterListFromMethodDefintion(String methodDefintion)
	{
		return ParameterEditorParser.parseMethodParamsToList(methodDefintion, true);
	}
}
