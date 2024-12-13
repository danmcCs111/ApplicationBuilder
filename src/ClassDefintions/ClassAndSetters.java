package ClassDefintions;

import java.util.ArrayList;

import ApplicationBuilder.LoggingMessages;

public class ClassAndSetters {

	private Class<?> clazz = null;
	private ArrayList<String> setters = null;
	
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
	
	public void printSetters()
	{
		for(String s : this.setters)
		{
			LoggingMessages.printOut(this.clazz.toString() + ": " + s);
		}
	}
}
