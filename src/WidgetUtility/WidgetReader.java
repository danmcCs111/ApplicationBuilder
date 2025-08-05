package WidgetUtility;

import java.util.ArrayList;

import org.w3c.dom.Node;

import Params.XmlToWidgetGenerator;
import Properties.XmlNodeReader;

/**
 * single instance for now
 * use initWidgetReader to read saved widget xml design
 * TODO allow for a reload?
 */
public class WidgetReader extends XmlNodeReader
{
	private ArrayList<WidgetCreatorProperty> widgetCreatorProperties = new ArrayList<WidgetCreatorProperty>(); 
	private String sourceFile;
	
	@SuppressWarnings("unchecked")
	public WidgetReader(String sourceFile)
	{
		this.sourceFile = sourceFile;
		readXml(sourceFile);
		widgetCreatorProperties.addAll((ArrayList<WidgetCreatorProperty>) getArrayList());
		collectWidgetCreatorProperties();
	}
	
	public String getSourceFileAbsolutePath()
	{
		return this.sourceFile;
	}
	
	public void clearSourceFile()
	{
		this.sourceFile = "";
	}
	
	@SuppressWarnings("unchecked")
	protected ArrayList<WidgetCreatorProperty> getWidgetCreatorProperties()
	{
		if(widgetCreatorProperties.isEmpty() && this.sourceFile != null)
		{
			readXml(sourceFile);
			widgetCreatorProperties.addAll((ArrayList<WidgetCreatorProperty>) getArrayList());
			collectWidgetCreatorProperties();
		}
		return widgetCreatorProperties;
	}
	
	protected void setWidgetCreatorProperties(ArrayList<WidgetCreatorProperty> propertiesReplace)
	{
		widgetCreatorProperties = propertiesReplace;
	}
	
	public void clearWidgetCreatorProperties()
	{
		this.widgetCreatorProperties = new ArrayList<WidgetCreatorProperty>();
		clearArrayList();
	}
	
	public ArrayList<WidgetCreatorProperty> collectWidgetCreatorProperties()
	{
		for(WidgetCreatorProperty wcp : widgetCreatorProperties)
		{
			for(String s : wcp.getSettingsName())
			{
				String val = wcp.getSettingsNameAndValue().get(s);
				ArrayList<XmlToWidgetGenerator> xmlToWidgetGenerator = WidgetAttributes.setAttribute(wcp.getClassType(), s, val);
				wcp.addXmlToWidgetGenerator(xmlToWidgetGenerator);
			}
		}
		return widgetCreatorProperties;
	}

	@Override
	public Object createNewObjectFromNode(Node n, ArrayList<String> attributes, int counter, String parentNode) 
	{
		return new WidgetCreatorProperty (
				n.getNodeName()+WidgetComponent.ID_SPLIT+WidgetComponent.getCountId(), 
				attributes, 
				parentNode
		);
	}
	
	@Override
	protected String getCounterId(String nodeStr, int counter)
	{
		return nodeStr + WidgetComponent.ID_SPLIT + WidgetComponent.nextCountId();
	}

	@Override
	public String getFileTypeTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFileTypeFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDefaultDirectoryRelative() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
