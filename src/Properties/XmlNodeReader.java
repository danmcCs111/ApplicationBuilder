package Properties;

import java.awt.Component;
import java.io.File;
import java.io.FileWriter;
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

public abstract class XmlNodeReader 
{
	private ArrayList<Object> elements = new ArrayList<Object>();
	private int counter = 0;
	
	public ArrayList<?> getArrayList()
	{
		return this.elements;
	}
	
	public void clearArrayList()
	{
		elements = new ArrayList<Object>();
	}
	
	public ArrayList<?> openXml(File f)
	{
		readXml(f.getAbsolutePath());
		return this.elements;
	}
	
	public ArrayList<?> openXml(Component parent, String title, String fileTypeFilter, String defaultDirectoryRelative)
	{
		OpenDialog od = new OpenDialog();
		File f = od.performOpen(parent, title, fileTypeFilter, defaultDirectoryRelative);
		if(f == null)
		{
			return null;
		}
		readXml(f.getAbsolutePath());
		return elements;
	}
	
	protected void readXml(String sourceFile)
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
			elements = readXml(nl, null);
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			LoggingMessages.printOut("Malformed xml or non-widget build xml chosen: " + e.getMessage());
		}
	}
	
	private ArrayList<Object> readXml(NodeList nl, String parentId)
	{
		if(nl != null)
		{
			for(int i = 0; i < nl.getLength(); i++)
			{
				Node n = nl.item(i);
				String nodeName = n.getNodeName();
				if(nodeName.equals("#text"))//ignore
					continue;
				Object element = readNode(n, parentId);
				if(element != null)
				{
					elements.add(element);
				}
				NodeList nl2 = n.getChildNodes();
				if(n.getChildNodes() != null)
				{
					String counterId = null;
					if(n != null)
					{
						String nodeStr = n.getNodeName();
						counterId = getCounterId(nodeStr, counter);
						LoggingMessages.printOut(nodeStr + " " + counterId);
					}
					readXml(nl2, counterId);
				}
			}
		}
		return elements;
	}
	
	private Object readNode(Node node, String parentNode)
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
		
		Object o = createNewObjectFromNode(node, attributes, counter, parentNode);
		counter++;
		
		return o;
	}
	
	protected String getCounterId(String nodeStr, int counter)
	{
		return nodeStr + counter;
	}
	
	public abstract Object createNewObjectFromNode(Node n, ArrayList<String> attributes, int counter, String parentNode);
	
	public abstract String getFileTypeTitle();
	public abstract String getFileTypeFilter();
	public abstract String getDefaultDirectoryRelative();
	
	public void performSave(Component parent, String xml)
	{
		LoggingMessages.printOut(xml);
		SaveAsDialog sad = new SaveAsDialog(parent, getFileTypeTitle(), getFileTypeFilter(), getDefaultDirectoryRelative());
		File f = sad.getSelectedDirectory();
		if(f != null)
		{
			writeXml(f, xml);
		}
	}
	
	private void writeXml(File sourceFile, String xml)
	{
		try {
			String filename = sourceFile.getAbsolutePath();
			if(!filename.endsWith("." + getFileTypeFilter()))
			{
				filename += "." + getFileTypeFilter();
			}
			FileWriter fw = new FileWriter(filename);
			fw.write(xml);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
