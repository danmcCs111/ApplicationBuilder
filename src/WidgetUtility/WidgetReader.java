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

/**
 * use initWidgetReader to read saved widget xml design
 * TODO allow for a reload?
 */
public class WidgetReader {
	
	private static WidgetReader widgetReader = null;
	
	private WidgetReader()
	{
		readWidgetBuilder();
	}
	
	public static void initWidgetReader()
	{
		if(widgetReader == null)
			widgetReader = new WidgetReader();
	}
	
	public static void readWidgetBuilder()
	{
		File f = new File("src\\ApplicationBuilder\\data\\WidgetBuild.xml");
		DocumentBuilderFactory dbFact = DocumentBuilderFactory.newInstance();
		DocumentBuilder dc;
		try {
			dc = dbFact.newDocumentBuilder();
			Document document = dc.parse(f);
			Element e = document.getDocumentElement();
			e.normalize();
			
			NodeList nl = e.getChildNodes();
			LoggingMessages.printOut("" + nl.getLength());
			generateWidgetCreatorPropertyList(nl, null);
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<WidgetCreatorProperty> generateWidgetCreatorPropertyList(NodeList nl, String parentId)
	{
		ArrayList<WidgetCreatorProperty> widgetCreatorProperties = new ArrayList<WidgetCreatorProperty>();
		
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
					widgetCreatorProperties.add(wcProperty);
				NodeList nl2 = n.getChildNodes();
				if(n.getChildNodes() != null)
				{
					String counterId = null;
					if(n != null)
					{
						String nodeStr = n.getNodeName().split(WidgetComponentType.ID_SPLIT)[0];
						WidgetComponentType wcType = WidgetComponentType.getWidgetComponentType(nodeStr);
						counterId = wcType.getNextCounterId();
						wcProperty.setRefId(counterId);
					}
					generateWidgetCreatorPropertyList(nl2, counterId);
				}
			}
		}
		return widgetCreatorProperties;
	}
	
	public static WidgetCreatorProperty generateWidgetCreatorProperty(Node node, String parentNode)
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
