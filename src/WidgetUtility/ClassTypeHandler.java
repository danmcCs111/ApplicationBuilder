package WidgetUtility;

import ObjectTypeConvertersImpl.ClassAndSetters;

public interface ClassTypeHandler 
{
	public abstract void applyAttribute(String method, String ... params);
	public abstract Class<?> getClassType();
	public abstract boolean isContainer();
	
	public static ClassAndSetters getClassAndSetters(ClassTypeHandler classTypeHandler)
	{
		ClassAndSetters tmp = null;
		Class<?> c = classTypeHandler.getClassType();
		for(ClassAndSetters cs : WidgetAttributes.getClassAndSetters())
		{
			if(cs.getClazz().toString().equals(c.toString()))
			{
				tmp = cs;
			}
		}
		return tmp;
	}
	
}
