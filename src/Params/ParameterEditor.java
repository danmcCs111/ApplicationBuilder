package Params;

import java.awt.Component;
import java.util.ArrayList;

import Properties.LoggingMessages;
import Properties.PathUtility;

public interface ParameterEditor 
{
	public abstract void destroy();
	public abstract Component getComponentEditor();
	public abstract void setComponentValue(Object value);
	public abstract String [] getComponentValue();
	public abstract Object getComponentValueObj();
	public abstract String getComponentXMLOutput();
	public abstract String getParameterDefintionString();
	
	public static ArrayList<String> loadClassExtensionsAsString(String classFileDirectory, String classFileExtension, String packagePrefix, String fileFilter)
	{
		ArrayList<String> classExtensions = new ArrayList<String>();
		ArrayList<String> files = PathUtility.getOSFileList(PathUtility.getCurrentDirectory() + classFileDirectory, fileFilter);
		for(String file : files)
		{
			LoggingMessages.printOut(packagePrefix + "." + file.replaceAll(classFileExtension, ""));
			try {
				Class<?> c = Class.forName(packagePrefix + "." + file.replaceAll(classFileExtension, ""));
				classExtensions.add(c.getName());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return classExtensions;
	}
	
}
