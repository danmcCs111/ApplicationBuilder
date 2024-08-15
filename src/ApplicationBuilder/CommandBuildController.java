package ApplicationBuilder;

import java.lang.reflect.Method;
import java.util.ArrayList;

import WidgetUtility.WidgetComponentType;
import WidgetUtility.WidgetCreatorProperty;
import WidgetUtility.WidgetReader;

public class CommandBuildController {
	
	private ArrayList<WidgetCreatorProperty> widgetCreatorProperties;
	
	public CommandBuildController()
	{
		widgetCreatorProperties = WidgetReader.getWidgetCreatorProperties();//TODO new feature
		
		for(WidgetCreatorProperty wcProp : widgetCreatorProperties)
		{
			WidgetComponentType wcType = wcProp.getComponentType();
			Method m = wcType.getCreatorMethod();
			LoggingMessages.printOut("creator property method: " + m.getName());
		}
	}
}
