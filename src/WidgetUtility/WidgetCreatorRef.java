package WidgetUtility;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public interface WidgetCreatorRef {
	
	private static Method [] getDeclaredMethods()
	{
		return WidgetCreator.class.getDeclaredMethods();
	}
	
	public static Component callWidgetCreation(String methodName, Object [] params)
	{
		Component c = null;
		Method m = getMethodMatching(methodName);
		try {
			Object o = m.invoke(WidgetCreator.class, params);
			if (o instanceof Component) {
				c = (Component) m.invoke(m, params);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return c;
	}
	
	public static ArrayList<Component> callListWidgetCreation(String methodName, ArrayList<Object []> listParams)
	{
		ArrayList<Component> comps = new ArrayList<Component>();
		for(Object [] os : listParams) {
			comps.add(callWidgetCreation(methodName, os));
		}
		return comps;
	}
	
	private static Method getMethodMatching(String methodName)
	{
		Method method = null;
		
		for (Method m : getDeclaredMethods())
		{
			if (m.getName().toLowerCase().equals(methodName.toLowerCase())){
				method = m;
				break;
			}
		}
		return method;
	}
}
