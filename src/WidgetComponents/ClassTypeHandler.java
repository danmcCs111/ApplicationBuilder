package WidgetComponents;

import ApplicationBuilder.LoggingMessages;
import ClassDefintions.ClassAndSetters;
import WidgetUtility.WidgetAttributes;

public interface ClassTypeHandler 
{
	public abstract void applyAttribute(String method, String ... params);
	public abstract Class<?> getClassType();
	
	public static ClassAndSetters getClassAndSetters(ClassTypeHandler classTypeHandler)
	{
		ClassAndSetters tmp = null;
		Class<?> c = classTypeHandler.getClassType();
		for(ClassAndSetters cs : WidgetAttributes.getClassAndSetters())
		{
			if(cs.getClazz().toString().equals(c.toString()))
			{
				LoggingMessages.printOut(cs.getClazz() + "" );
				tmp = cs;
			}
		}
		return tmp;
	}
}
