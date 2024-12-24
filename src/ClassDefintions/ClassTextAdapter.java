package ClassDefintions;

import java.util.ArrayList;
import java.util.List;

import Params.ParamTypes;
import Params.XmlToWidgetGenerator;
import WidgetComponents.ParameterEditorParser;

public class ClassTextAdapter 
{
	public static XmlToWidgetGenerator functionCall(Class<?> component, String methodDefintion, String method, String ... params)
	{
		XmlToWidgetGenerator methodParams = null;
		ArrayList<String> paramDefList = parseParameterListFromMethodDefintion(methodDefintion);
		int count = 0;
		for(String p : paramDefList)//loop through the method definition params
		{
			StringToObjectConverter stringToObjectConverter = ParamTypes.getParamType(p).getConverter();
			List<String> argParams = new ArrayList<String>();
			for(int i = 0; i < stringToObjectConverter.numberOfArgs(); i++)//loop through the xml args per method def param
			{
				argParams.add(params[count + i]);
			}
			if(methodParams == null)
			{
				methodParams = new XmlToWidgetGenerator(stringToObjectConverter, method, argParams);
			}
			else
			{
				methodParams.addNextParams(stringToObjectConverter, argParams);
			}
			count++;
		}
		
		return methodParams;
	}
	
	private static ArrayList<String> parseParameterListFromMethodDefintion(String methodDefintion)
	{
		return ParameterEditorParser.parseMethodParamsToList(methodDefintion, true);
	}
	
}
