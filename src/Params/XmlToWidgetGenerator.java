package Params;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import ApplicationBuilder.LoggingMessages;
import ClassDefintions.StringToObjectConverter;

public class XmlToWidgetGenerator 
{
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
	
	public void generate(Object o)
	{
		Object [] os = new Object [stringToObjectConverterList.size()];
		Class<?> [] cs = new Class<?> [stringToObjectConverterList.size()];
		for(int i = 0; i < stringToObjectConverterList.size(); i++)
		{
			List<String> params = paramsList.get(i);
			StringToObjectConverter sc = stringToObjectConverterList.get(i);
			os[i]=sc.conversionCall(params.toArray(new String [] {}));
			cs[i]=sc.getDefinitionClass();
		}
		
		try {
			Method m = o.getClass().getMethod(method, cs);
			m.invoke(o, os);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("Method: " + method + " ");
		sb.append(LoggingMessages.combine(paramsList) + " ");
		sb.append(LoggingMessages.combine(stringToObjectConverterList));
		
		return sb.toString();
	}
}
