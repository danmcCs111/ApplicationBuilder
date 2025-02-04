package WidgetUtility;

import java.util.ArrayList;
import java.util.List;

import ApplicationBuilder.LoggingMessages;
import Params.XmlToWidgetGenerator;
import WidgetComponents.ParameterEditor;

public class XmlToEditor 
{
	private List<WidgetCreatorProperty> widgetCreatorProperties;
	private ArrayList<ParameterEditor> parameterEditors;
	
	public XmlToEditor(List<WidgetCreatorProperty> widgetCreatorProperties)
	{
		this.widgetCreatorProperties = widgetCreatorProperties;
	}
	
	public ArrayList<ParameterEditor> getParameterEditors()
	{
		ArrayList<ParameterEditor> editors = new ArrayList<ParameterEditor>();
		for(WidgetCreatorProperty wcp : widgetCreatorProperties)
		{
			for (XmlToWidgetGenerator xwg : wcp.getXmlToWidgetGenerators())
			{
//				ParameterEditorParser.parseMethodParamsToList(xwg.getMethodName(), true);
				LoggingMessages.printOut(xwg.toString());
			}
		}
		return editors;
	}
	
}
