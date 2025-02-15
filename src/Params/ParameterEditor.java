package Params;

import java.awt.Component;
import java.awt.FontMetrics;
import java.util.ArrayList;

import javax.swing.JLabel;

import ApplicationBuilder.LoggingMessages;
import Properties.PathUtility;

public interface ParameterEditor 
{
	public abstract Component getComponentEditor();
	public abstract void setComponentValue(Object value);
	public abstract String [] getComponentValue();
	public abstract Object getComponentValueObj();
	public abstract String getComponentXMLOutput();
	public abstract String getParameterDefintionString();
	
	public static JLabel getFieldLabel(String labelText)
	{
		return new JLabel(labelText);
	}
	
	public static int getFieldLabelWidth(JLabel label)
	{
		FontMetrics fm = label.getFontMetrics(label.getFont());
		int width = fm.stringWidth(label.getText());
		return width;
	}
	
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
