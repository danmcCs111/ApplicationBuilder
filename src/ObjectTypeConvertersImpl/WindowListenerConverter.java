package ObjectTypeConvertersImpl;

import java.awt.event.WindowListener;
import java.lang.reflect.InvocationTargetException;

import ObjectTypeConversion.StringToObjectConverter;

public class WindowListenerConverter implements StringToObjectConverter
{
	@Override
	public String toString()
	{
		return ActionListenerConverter.class.toString();
	}
	
	@Override
	public int numberOfArgs() 
	{
		return 1;
	}

	@Override
	public Object conversionCall(String... args) 
	{
		if(conversionCallIsBlankCheck(args))
		{
			return getDefaultNullValue();
		}
		Class<?> c;
		try {
			c = Class.forName(args[0]);
			return c.getDeclaredConstructor().newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Class<?> getDefinitionClass() 
	{
		return WindowListener.class;
	}

	@Override
	public Object getDefaultNullValue() 
	{
		return null;
	}

	@Override
	public String conversionCallStringXml(String... args) 
	{
		return args[0];
	}

}
