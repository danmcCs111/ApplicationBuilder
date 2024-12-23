package ClassDefintions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ApplicationBuilder.LoggingMessages;

public class ClassAndSetters 
{
	private Class<?> clazz = null;
	private ArrayList<String> 
		setters = null,
		extendedSetters = new ArrayList<String>();
	
	public ClassAndSetters(String classStr, ArrayList<String> setters)
	{
		try {
			this.setters = setters;
			clazz = Class.forName(classStr);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Class<?> getClazz()
	{
		return this.clazz;
	}
	
	public ArrayList<String> getSetters()
	{
		return this.setters;
	}
	
	public String getSetter(String method)
	{
		for(String s : this.setters)
		{
			if(s.startsWith(method))
			{
				return s;
			}
		}
		//else try extended?
		return getExtendedSetter(method);
	}
	
	public void addExtendedSetters(String ... setterExtended)
	{
		this.extendedSetters.addAll(Arrays.asList(setterExtended));
	}
	public void addExtendedSetters(List<String> setterExtended)
	{
		this.extendedSetters.addAll(setterExtended);
	}
	
	/*
	 * TODO using plain strings
	 */
	public String getExtendedSetter(String methodExtended)
	{
		for(String s : this.extendedSetters)
		{
			if(s.startsWith(methodExtended))
			{
				return s;
			}
		}
		return null;
	}
	
	public void printSetters()
	{
		for(String s : this.setters)
		{
			LoggingMessages.printOut(this.clazz.toString() + ": " + s);
		}
	}
}
