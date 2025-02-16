package WidgetUtility;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ApplicationBuilder.LoggingMessages;
import Params.XmlToWidgetGenerator;

/**
 * single instance for now
 * use initWidgetReader to read saved widget xml design
 * TODO allow for a reload?
 */
public class WidgetReader 
{
	private ArrayList<WidgetCreatorProperty> widgetCreatorProperties = new ArrayList<WidgetCreatorProperty>(); 
	private String sourceFile;
	
	public WidgetReader(String sourceFile)
	{
		this.sourceFile = sourceFile;
		readWidgetBuilder(sourceFile);
		collectWidgetCreatorProperties();
	}
	
	public String getSourceFileAbsolutePath()
	{
		return this.sourceFile;
	}
	
	public void clearSourceFile()
	{
		this.sourceFile = null;
	}
	
	protected ArrayList<WidgetCreatorProperty> getWidgetCreatorProperties()
	{
		if(widgetCreatorProperties.isEmpty() && this.sourceFile != null)
		{
			readWidgetBuilder(sourceFile);
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
	}
	
	public ArrayList<WidgetCreatorProperty> collectWidgetCreatorProperties()
	{
		for(WidgetCreatorProperty wcp : widgetCreatorProperties)
		{
			for(String s : wcp.getSettingsName())
			{
				String val = wcp.getSettingsNameAndValue().get(s);
				XmlToWidgetGenerator xmlToWidgetGenerator = WidgetAttributes.setAttribute(wcp.getClassType(), s, val);
				wcp.addXmlToWidgetGenerator(xmlToWidgetGenerator);
			}
		}
		return widgetCreatorProperties;
	}
	
	private void readWidgetBuilder(String sourceFile)
	{
		if(sourceFile == null)
			return;
		File f = new File(sourceFile);
		DocumentBuilderFactory dbFact = DocumentBuilderFactory.newInstance();
		DocumentBuilder dc;
		try {
			dc = dbFact.newDocumentBuilder();
			Document document = dc.parse(f);
			Element e = document.getDocumentElement();
			e.normalize();
			
			NodeList nl = e.getChildNodes();
			widgetCreatorProperties = generateWidgetCreatorPropertyList(nl, null);
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			LoggingMessages.printOut("Malformed xml or non-widget build xml chosen: " + e.getMessage());
			LoggingMessages.printOut("Cause: " + LoggingMessages.combine(e.getStackTrace()));
		}
	}
	
	private ArrayList<WidgetCreatorProperty> generateWidgetCreatorPropertyList(NodeList nl, String parentId)
	{
		if(nl != null)
		{
			for(int i = 0; i < nl.getLength(); i++)
			{
				Node n = nl.item(i);
				String nodeName = n.getNodeName();
				if(nodeName.equals("#text"))//ignore
					continue;
				WidgetCreatorProperty wcProperty = generateWidgetCreatorProperty(n, parentId);
				if(wcProperty != null)
				{
					widgetCreatorProperties.add(wcProperty);
				}
				NodeList nl2 = n.getChildNodes();
				if(n.getChildNodes() != null)
				{
					String counterId = null;
					if(n != null)
					{
						String nodeStr = n.getNodeName().split(WidgetComponent.ID_SPLIT)[0];
						WidgetComponent wcType = WidgetComponent.getWidgetComponent(nodeStr);
						counterId = wcType.getNextCounterId();
					}
					generateWidgetCreatorPropertyList(nl2, counterId);
				}
			}
		}
		return widgetCreatorProperties;
	}
	
	private WidgetCreatorProperty generateWidgetCreatorProperty(Node node, String parentNode)
	{
		ArrayList<String> attributes = new ArrayList<String>();
		
		NamedNodeMap nnMap = node.getAttributes();
		if(nnMap == null)
		{
			LoggingMessages.printOut("null, Continue...");
			return null;
		}
		for(int j = 0;  j < nnMap.getLength(); j++)
		{
			attributes.add(nnMap.item(j).toString());
		}
		String counterId = parentNode;
		
		return new WidgetCreatorProperty(node.getNodeName(), attributes, counterId);
	}
}
