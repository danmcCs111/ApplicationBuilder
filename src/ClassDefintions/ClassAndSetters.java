package ClassDefintions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Params.ParamTypes;
import Properties.LoggingMessages;

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
	
	public String getSetter(String methodName)
	{
		for(String s : this.setters)//working off of a method def
		{
			if(s.startsWith(methodName))
			{
				//TODO fix? not checking params on match. correcting over same prefixed names
				char af = s.subSequence(methodName.length(), s.length()).charAt(0);
				String tst = af + "";
				LoggingMessages.printOut(tst);
				if(!tst.replaceAll("[a-zA-Z]*", "").equals(""))
				{
					return s;
				}
			}
		}
		//else try extended?
		return getExtendedSetter(methodName);
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
				tmp = spl.replaceAll(" [a-zA-Z0-9]+", "");
				tmp = tmp.replace("]", "");
				getMethodParams.add(tmp);
			}
		}
		return getMethodParams;
	}
	
}
