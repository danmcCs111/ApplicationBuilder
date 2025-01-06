package WidgetExtensions;

import java.io.File;

import ApplicationBuilder.LoggingMessages;
import Properties.PropertiesFileLoader;

public class UrlToValueReader 
{
	private static final String 
		URL_DELIMITER = "=",
		URL_KEY = "URL";
	
	
	public static String parse(String filename, String path)
	{
		return parse(path + File.separator + filename);
	}
	
	public static String parse(String filenameAndPath)
	{
		String value = null;
		value = PropertiesFileLoader.readProperties(filenameAndPath, URL_DELIMITER).get(URL_KEY);
		return value;
	}
	
}
