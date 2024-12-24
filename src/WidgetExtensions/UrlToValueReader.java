package WidgetExtensions;

import java.io.File;

import Properties.PropertiesFileLoader;

public class UrlToValueReader 
{
	public static String parse(String filename, String path)
	{
		return parse(path + File.pathSeparator + filename, "=");
	}
	
	public static String parse(String filenameAndPath)
	{
		String value = null;
		PropertiesFileLoader.readProperties(filenameAndPath, "=");
		return value;
	}
	
}
