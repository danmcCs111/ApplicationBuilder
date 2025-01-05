package Params;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import ApplicationBuilder.LoggingMessages;
import ClassDefintions.StringToObjectConverter;
import WidgetExtensions.ExtendedAttributeStringParam;
import WidgetExtensions.ExtendedLayoutApplyParent;
import WidgetUtility.WidgetCreatorProperty;

public class XmlToWidgetGenerator 
{
	private String methodName;
	private ArrayList<StringToObjectConverter> stringToObjectConverterList = new ArrayList<StringToObjectConverter>();
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
//			generateExtended(o, cs, os);
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
	
	public void generateExtended(Class<? extends ExtendedAttributeStringParam> extendedAttr, WidgetCreatorProperty widgetProperties)
	{
		LoggingMessages.printOut("|TODO| -> Generate Extended");
		Method m;
		try {
			Object [] os = new Object [stringToObjectConverterList.size() + 1];
			Class<?> [] cs = new Class<?> [stringToObjectConverterList.size() + 1];
			for(int i = 0; i < stringToObjectConverterList.size(); i++)
			{
				List<String> params = paramsList.get(i);
				StringToObjectConverter sc = stringToObjectConverterList.get(i);
				os[i]=sc.conversionCall(params.toArray(new String [] {}));
				cs[i]=sc.getDefinitionClass();
			}
			cs[cs.length-1] = WidgetCreatorProperty.class;
			os[os.length-1] = widgetProperties;
			
			Object tmp = extendedAttr.newInstance();
			
			m = tmp.getClass().getMethod("applyMethod", cs);
			m.invoke(tmp, os);
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
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
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
