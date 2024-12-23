package ApplicationBuilder;

import java.util.ArrayList;


import WidgetUtility.WidgetComponent;
import WidgetUtility.WidgetCreatorProperty;
import WidgetUtility.WidgetReader;

public class WidgetBuildController 
{
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
			WidgetComponent wcType = wcProp.getComponentType();
		}
	}
}
