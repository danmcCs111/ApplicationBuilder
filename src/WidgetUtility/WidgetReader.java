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
	private static ArrayList<WidgetCreatorProperty> widgetCreatorProperties = new ArrayList<WidgetCreatorProperty>(); 
	
	public static ArrayList<WidgetCreatorProperty> collectWidgetCreatorProperties(String sourceFile)
	{
		initWidgetReader(sourceFile);
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
	
	private WidgetReader(String sourceFile)
	{
		readWidgetBuilder(sourceFile);
	}
	private WidgetReader(File sourceFile)
	{
		readWidgetBuilder(sourceFile);
	}
	
	private static void initWidgetReader(String sourceFile)
	{
		new WidgetReader(sourceFile);
	}
	
	private static void readWidgetBuilder(File sourceFile)
	{
		readWidgetBuilder(sourceFile.getAbsolutePath());
	}
	private static void readWidgetBuilder(String sourceFile)
	{
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
	
	private static ArrayList<WidgetCreatorProperty> generateWidgetCreatorPropertyList(NodeList nl, String parentId)
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
	
	private static WidgetCreatorProperty generateWidgetCreatorProperty(Node node, String parentNode)
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
