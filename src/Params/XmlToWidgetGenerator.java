package Params;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import ApplicationBuilder.LoggingMessages;
import ApplicationBuilder.WidgetBuildController;
import ClassDefintions.StringToObjectConverter;
import WidgetExtensions.ExtendedAttributeStringParam;
import WidgetUtility.WidgetCreatorProperty;

public class XmlToWidgetGenerator 
{
	private String methodName;
	private ArrayList<StringToObjectConverter> stringToObjectConverterList = new ArrayList<StringToObjectConverter>();
	//list of lists, example: setArrayForegroundAndBackground [240, 240, 240], [175, 204, 175] class ClassDefintions.ColorConverter, class ClassDefintions.ColorConverter
	private ArrayList<List<String>> paramsList = new ArrayList<List<String>>();
	
	public XmlToWidgetGenerator(StringToObjectConverter stringToObjectConverter, String methodName, List<String> params)
	{
		this.stringToObjectConverterList.add(stringToObjectConverter);
		this.methodName = methodName;
		this.paramsList.add(params);
	}
	
	public void addNextParams(StringToObjectConverter stringToObjectConverter, List<String> params)
	{
		this.stringToObjectConverterList.add(stringToObjectConverter);
		this.paramsList.add(params);
	}
	
	public String getMethodName()
	{
		return this.methodName;
	}
	
	public ArrayList<StringToObjectConverter> getObjectConverterList()
	{
		return stringToObjectConverterList;
	}
	
	public ArrayList<List<String>> getParamsList()
	{
		return this.paramsList;
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
			Method m = o.getClass().getMethod(methodName, cs);
			m.invoke(o, os);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public void generateExtended(Class<? extends ExtendedAttributeStringParam> extendedAttr, 
			WidgetBuildController widgetBuildController, 
			WidgetCreatorProperty widgetProperties)
	{
		Method m;
		try {
			Object [] os = new Object [stringToObjectConverterList.size() + 1];//ADD [number] for params listed additionally
			Class<?> [] cs = new Class<?> [stringToObjectConverterList.size() + 1];
			for(int i = 0; i < stringToObjectConverterList.size(); i++)
			{
				List<String> params = paramsList.get(i);
				StringToObjectConverter sc = stringToObjectConverterList.get(i);
				os[i]=sc.conversionCall(params.toArray(new String [] {}));
				cs[i]=sc.getDefinitionClass();
			}
			
			cs[cs.length-1] = WidgetCreatorProperty.class;//additional added params for extended methods
			os[os.length-1] = widgetProperties;
			
			Object tmp = extendedAttr.newInstance();
			
			m = tmp.getClass().getMethod("applyMethod", cs);
			m.invoke(tmp, os);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("Method: " + methodName + " ");
		sb.append(LoggingMessages.combine(paramsList) + " ");
		sb.append(LoggingMessages.combine(stringToObjectConverterList));
		
		return sb.toString();
	}
}
