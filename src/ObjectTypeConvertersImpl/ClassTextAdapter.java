package ObjectTypeConvertersImpl;

import java.util.ArrayList;
import java.util.List;

import ObjectTypeConversion.StringToObjectConverter;
import Params.ParamTypes;
import Params.ParameterEditorParser;
import Params.XmlToWidgetGenerator;
import Properties.LoggingMessages;

public class ClassTextAdapter 
{
	public static final String PARAM_MULTIPLIER = "#";
	
	public static ArrayList<XmlToWidgetGenerator> functionCall(Class<?> component, String methodDefintion, String method, String ... params)
	{
		ArrayList<XmlToWidgetGenerator> methodParams = new ArrayList<XmlToWidgetGenerator>();
		ArrayList<String> paramDefList = parseParameterListFromMethodDefintion(methodDefintion);
		int count = 0;
		
		LoggingMessages.printOut(methodDefintion);
		for(String p : paramDefList)//loop through the method definition params
		{
			LoggingMessages.printOut("param: " + p);
			StringToObjectConverter stringToObjectConverter = ParamTypes.getParamType(p).getConverter();
			ArrayList<List<String>> argParams = new ArrayList<List<String>>();
			argParams.add(new ArrayList<String>());
			
			if(params.length-count >= stringToObjectConverter.numberOfArgs())
			{
				for(int i = 0; i < stringToObjectConverter.numberOfArgs(); i++)//loop through the xml args per method def param
				{
					String param = params[count + i];
					if(param.contains(PARAM_MULTIPLIER))
					{
						String [] multiplier = param.split(PARAM_MULTIPLIER);
						for(int j = 0; j < multiplier.length; j++)
						{
							if(j == argParams.size())
							{
								argParams.add(new ArrayList<String>());
							}
							argParams.get(j).add(multiplier[j]);
						}
					}
					else
					{
						argParams.get(0).add(param);
					}
				}
			}
			if(methodParams == null || methodParams.isEmpty())
			{
				for(List<String> argP : argParams)
				{
					methodParams.add(new XmlToWidgetGenerator(stringToObjectConverter, method, argP));
				}
			}
			else
			{
				methodParams.get(0).addNextParams(stringToObjectConverter, argParams.get(0));//only multiplier on single args for now.
			}
			count += stringToObjectConverter.numberOfArgs();
		}
		
		return methodParams;
	}
	
	private static ArrayList<String> parseParameterListFromMethodDefintion(String methodDefintion)
	{
		return ParameterEditorParser.parseMethodParamsToList(methodDefintion, true);
	}
	
}
