package ApplicationBuilder;

import Properties.PathUtility;
import WidgetUtility.WidgetBuildController;

public class ApplicationBuilder
{
	public static final String DEFAULT_APPLICATION_EDITOR_PATH = PathUtility.getCurrentDirectory() + 
			"/src/ApplicationBuilder/data/GeneratedApplicationBuilder.xml";
	
	public static void main(String [] args)
	{
		if(args.length == 1) buildAppFromXML(args[0]);
		else buildAppFromXML(DEFAULT_APPLICATION_EDITOR_PATH);
	}
	
	public static void buildAppFromXML(String SOURCE_FILE)
	{
		WidgetBuildController.getInstance().readProperties(SOURCE_FILE);
		WidgetBuildController.getInstance().generateGraphicalInterface(false);
	}
}
