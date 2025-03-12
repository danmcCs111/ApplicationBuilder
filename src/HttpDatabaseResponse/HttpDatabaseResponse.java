package HttpDatabaseResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
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

import Properties.LoggingMessages;

public class HttpDatabaseResponse 
{
	public static ArrayList<DatabaseResponseNode> parseResponse(String responseBody)
	{
		return getXml(responseBody);
	}
	
	public static ArrayList<DatabaseResponseNode> getXml(String response)
	{
		int indexOf = response.indexOf("<ResultSet>", 0);
		LoggingMessages.printOut(indexOf + "");
		String resopnseXml = response.substring(indexOf, response.length());
		LoggingMessages.printOut(resopnseXml);
		
		return parseXml(resopnseXml);
	}
	
	public static ArrayList<DatabaseResponseNode> parseXml(String responseXml)
	{
		ArrayList<DatabaseResponseNode> databaseNodes = new ArrayList<DatabaseResponseNode>();
		DocumentBuilderFactory dbFact = DocumentBuilderFactory.newInstance();
		DocumentBuilder dc;
		try {
			dc = dbFact.newDocumentBuilder();
			InputStream targetStream = new ByteArrayInputStream(responseXml.getBytes(StandardCharsets.UTF_8));
			Document document = dc.parse(targetStream);
			Element e = document.getDocumentElement();
			e.normalize();
			
			NodeList nl = e.getChildNodes();
			databaseNodes = generateWidgetCreatorPropertyList(nl, null);
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			LoggingMessages.printOut("Malformed xml or non-widget build xml chosen: " + e.getMessage());
			e.printStackTrace();
		}
		return databaseNodes;
	}
	
	private static ArrayList<DatabaseResponseNode> generateWidgetCreatorPropertyList(NodeList nl, String parentId)
	{
		ArrayList<DatabaseResponseNode> databaseResponseNodes = new ArrayList<DatabaseResponseNode>();
		if(nl != null)
		{
			for(int i = 0; i < nl.getLength(); i++)
			{
				Node n = nl.item(i);
				String nodeName = n.getNodeName();
				if(nodeName.equals("#text"))//ignore
					continue;
				
				DatabaseResponseNode databaseNode = generateDatabaseNode(n, parentId);
				if(databaseNode != null)
				{
					databaseResponseNodes.add(databaseNode);
				}
				NodeList nl2 = n.getChildNodes();
				if(n.getChildNodes() != null)
				{
					generateWidgetCreatorPropertyList(nl2, null);
				}
			}
		}
		return databaseResponseNodes;
	}
	
	private static DatabaseResponseNode generateDatabaseNode(Node node, String parentNode)
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
		String nodeValue = node.getTextContent();
		
		if(nodeValue != null && !nodeValue.isBlank())
		{
			attributes.add("content=" + nodeValue);
		}
		
		return new DatabaseResponseNode(node.getNodeName(), attributes);
	}
}
