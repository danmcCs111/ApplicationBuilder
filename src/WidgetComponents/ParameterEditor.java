package WidgetComponents;

import java.util.ArrayList;

import ApplicationBuilder.LoggingMessages;

public class ParameterEditor {

	public ParameterEditor()
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
}
