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
	private static final String 
		CONTENT_TAG = "content=",
		RESULT_TAG = "Result",
		RESULT_SET_TAG = "<ResultSet>";
	
	private ArrayList<ArrayList<DatabaseResponseNode>> databaseResponseNodesFull = new ArrayList<ArrayList<DatabaseResponseNode>>();
	private ArrayList<DatabaseResponseNode> databaseResponseNodes = new ArrayList<DatabaseResponseNode>();
	
	public ArrayList<ArrayList<DatabaseResponseNode>> parseResponse(String responseBody)
	{
		getXml(responseBody);
		return databaseResponseNodesFull;
	}
	
	private void getXml(String response)
	{
		int indexOf = response.indexOf(RESULT_SET_TAG, 0);
		String resopnseXml = response.substring(indexOf, response.length());
		
		parseXml(resopnseXml);
	}
	
	private void parseXml(String responseXml)
	{
		DocumentBuilderFactory dbFact = DocumentBuilderFactory.newInstance();
		DocumentBuilder dc;
		try {
			dc = dbFact.newDocumentBuilder();
			InputStream targetStream = new ByteArrayInputStream(responseXml.getBytes(StandardCharsets.UTF_8));
			Document document = dc.parse(targetStream);
			Element e = document.getDocumentElement();
			e.normalize();
			
			NodeList nl = e.getChildNodes();
			generateWidgetCreatorPropertyList(nl, null);
			if(!databaseResponseNodes.isEmpty()) 
			{
				databaseResponseNodesFull.add(databaseResponseNodes);
				databaseResponseNodes = new ArrayList<DatabaseResponseNode>();
			}
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			LoggingMessages.printOut("Malformed xml or non-widget build xml chosen: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void generateWidgetCreatorPropertyList(NodeList nl, String parentId)
	{
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
				if(nl2 != null)
				{
					if(n.getNodeName().equals(RESULT_TAG))
					{
						databaseResponseNodesFull.add(databaseResponseNodes);
						databaseResponseNodes = new ArrayList<DatabaseResponseNode>();
					}
					generateWidgetCreatorPropertyList(nl2, n.getNodeName());
				}
			}
		}
	}
	
	private DatabaseResponseNode generateDatabaseNode(Node node, String parentNode)
	{
		if(node.getNodeName().equals(RESULT_TAG))
			return null;
		
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
			attributes.add(CONTENT_TAG + nodeValue);//add as a tag.
		}
		
		return new DatabaseResponseNode(node.getNodeName(), attributes);
	}
}
