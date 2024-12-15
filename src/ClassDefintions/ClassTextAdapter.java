package ClassDefintions;

import java.util.List;

import ApplicationBuilder.LoggingMessages;
import Params.ParamTypes;
import WidgetComponents.ParameterEditorParser;

public class ClassTextAdapter {
	
	public static void functionCall(Class<?> component, String methodDefintion, String method, String ...params)
	{
		List<String> paramDefList = parseParameterListFromMethodDefintion(methodDefintion);
		for(String p : paramDefList)
		{
			LoggingMessages.printOut("param: " + p);
			LoggingMessages.printOut("int name: " + int.class.getName());
			LoggingMessages.printOut("method definition: " + p);
			LoggingMessages.printOut("converter: " + ParamTypes.getParamType(p).getConverter().getClass().getName());
		}
		
	}
	
	private static List<String> parseParameterListFromMethodDefintion(String methodDefintion)
	{
		return ParameterEditorParser.parseMethodParamsToList(methodDefintion, true);
	}
	
}
