package ApplicationBuilder;

import Properties.PathUtility;
import WidgetUtility.WidgetBuildController;

public class ApplicationBuilder
{
	public static final String 
		DEFAULT_APPLICATION_EDITOR_PATH = PathUtility.getCurrentDirectory() + 
			"/Properties/data/GeneratedApplicationBuilder.xml";
	
	public static String [] argsApp;
	
	public static void main(String [] args)
	{
		argsApp = args;
		if(args.length >= 1) 
		{
			buildAppFromXML(args[0]);
		}
		else 
		{
			buildAppFromXML(DEFAULT_APPLICATION_EDITOR_PATH);
		}
	}
	
	public static void buildAppFromXML(String SOURCE_FILE)
	{
		WidgetBuildController.getInstance().readProperties(SOURCE_FILE);
		WidgetBuildController.getInstance().generateGraphicalInterface(false);
	}
}
