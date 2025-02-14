package Params;

import java.awt.Component;
import java.awt.FontMetrics;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.JLabel;

import ApplicationBuilder.LoggingMessages;
import Properties.PathUtility;

public abstract class ParameterEditor 
{
	public abstract Component getComponentEditor();
	public abstract void setComponentValue(Object value);
	public abstract String [] getComponentValue();
	public abstract Object getComponentValueObj();
	public abstract String getComponentXMLOutput();
	
	public ParameterEditor newInstance()
	{
		try {
			return ((ParameterEditor) Class.forName(this.getClass().getName()).getDeclaredConstructor().newInstance());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
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
	
	public JLabel getFieldLabel(String labelText)
	{
		return new JLabel(labelText);
	}
	
	public int getFieldLabelWidth(JLabel label)
	{
		FontMetrics fm = label.getFontMetrics(label.getFont());
		int width = fm.stringWidth(label.getText());
		return width;
	}
	
	public boolean isType(String parameterDefStringName)
	{
		return parameterDefStringName == null 
			? false 
			: getParameterDefintionString().toLowerCase().equals(parameterDefStringName.toLowerCase());
	}
	
	public boolean isType(Class<?> parameterDefClassName)
	{
		return parameterDefClassName == null 
			? false 
			: isType(parameterDefClassName.getName());
	}
	
	protected static ArrayList<String> loadClassExtensionsAsString(String classFileDirectory, String classFileExtension, String packagePrefix, String fileFilter)
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
	
	public abstract String getParameterDefintionString();
}
