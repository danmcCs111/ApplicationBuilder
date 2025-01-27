package ClassDefintions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ApplicationBuilder.LoggingMessages;
import Params.ParamTypes;

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
	
	public ArrayList<String> getSupportedSetters()
	{
		ArrayList<String> tmp = new ArrayList<String>();
		
		supported:
		for(String setter : setters)
		{
			//TODO
			for(String s : getMethodParams(setter))
			{
				if(ParamTypes.getParamType(s) == null)
				{
					continue supported; //don't include if not supported yet in editor / xml generator
				}
			}
			tmp.add(setter);
		}
		for(String ext : this.extendedSetters)
		{
			LoggingMessages.printOut("Method: " + ext);
			tmp.add(ext);
		}
		return tmp;
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
	
	private ArrayList<String> getMethodParams(String method)
	{
		ArrayList<String> getMethodParams = new ArrayList<String>();
		
		String [] mets = method.split("\\[");
		if(mets != null && mets.length > 0)
		{
			String tmp = "";
			for(String spl : mets[1].split(", "))
			{
				tmp = spl.replaceAll("\s[a-zA-Z0-9]+", "");
				tmp = tmp.replace("]", "");
				getMethodParams.add(tmp);
			}
		}
		return getMethodParams;
	}
	
}
