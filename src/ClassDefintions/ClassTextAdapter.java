package ClassDefintions;

import java.util.List;

import ApplicationBuilder.LoggingMessages;
import Params.ParamTypes;
import WidgetComponents.ParameterEditorParser;

public class ClassTextAdapter {
	
	public static void functionCall(Class<?> component, String methodDefintion, String method, String ...params)
	{
		List<String> paramDefList = parseParameterListFromMethodDefintion(methodDefintion);
		int count = 0;
		for(String p : paramDefList)
		{
			StringToObjectConverter stringToObjectConverter = ParamTypes.getParamType(p).getConverter();
			LoggingMessages.printOut("converter: " + stringToObjectConverter.getClass().getName());
			for(int i = 0; i < stringToObjectConverter.numberOfArgs(); i++)
			{
				LoggingMessages.printOut("param: " + params[count + i]);
			}
			LoggingMessages.printNewLine();
			count++;
		}
	}
	
	private static List<String> parseParameterListFromMethodDefintion(String methodDefintion)
	{
		return ParameterEditorParser.parseMethodParamsToList(methodDefintion, true);
	}
	
}
