package Params;

import java.util.ArrayList;
import java.util.List;

import ApplicationBuilder.LoggingMessages;
import ClassDefintions.StringToObjectConverter;

public class XmlToWidgetGenerator {

	private ArrayList<StringToObjectConverter> stringToObjectConverterList = new ArrayList<StringToObjectConverter>();
	private String method;
	private ArrayList<List<String>> paramsList = new ArrayList<List<String>>();
	
	public XmlToWidgetGenerator(StringToObjectConverter stringToObjectConverter, String method, List<String> params)
	{
		this.stringToObjectConverterList.add(stringToObjectConverter);
		this.method = method;
		this.paramsList.add(params);
	}
	
	public void addNextParams(StringToObjectConverter stringToObjectConverter, List<String> params)
	{
		this.stringToObjectConverterList.add(stringToObjectConverter);
		this.paramsList.add(params);
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("Method: " + method + " ");
		sb.append(LoggingMessages.combine(paramsList) + " ");
		sb.append(LoggingMessages.combine(stringToObjectConverterList));
		
		return sb.toString();
	}
}
