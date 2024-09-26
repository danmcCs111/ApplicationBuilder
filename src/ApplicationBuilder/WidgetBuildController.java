package ApplicationBuilder;

import java.lang.reflect.Method;
import java.util.ArrayList;

import WidgetUtility.WidgetComponentType;
import WidgetUtility.WidgetCreatorProperty;
import WidgetUtility.WidgetReader;

public class WidgetBuildController {
	
	private ArrayList<WidgetCreatorProperty> widgetCreatorProperties;
	private String sourceFile;
	
	public WidgetBuildController(String sourceFile)
	{
		this.sourceFile = sourceFile;
		readProperties();
	}
	
	/**
	 * read the properties of the source file passed during construction
	 */
	public void readProperties()
	{
		widgetCreatorProperties = WidgetReader.getWidgetCreatorProperties(sourceFile);//TODO new feature
		
		for(WidgetCreatorProperty wcProp : widgetCreatorProperties)
		{
			WidgetComponentType wcType = wcProp.getComponentType();
			Method m = wcType.getCreatorMethod();
			LoggingMessages.printOut("creator property method: " + m.getName());
		}
	}
}
