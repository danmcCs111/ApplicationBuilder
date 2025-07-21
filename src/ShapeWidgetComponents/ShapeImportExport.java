package ShapeWidgetComponents;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Shape;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
import Properties.OpenDialog;
import Properties.PathUtility;
import Properties.SaveAsDialog;

public class ShapeImportExport 
{
	private static final long serialVersionUID = 1L;
	
	private static final String 
		WIDGETS_SHAPE_OPEN_TAG = "<Widgets>",
		WIDGETS_SHAPE_CLOSE_TAG = "</Widgets>",
		FILE_TYPE_TITLE = "XML",
		FILE_TYPE_FILTER = "xml",
		DEFAULT_DIRECTORY_RELATIVE =  "/src/ApplicationBuilder/shapes/ ";
	
	private ArrayList<ArrayList<Point>> points;
	private ArrayList<Shape> shape;
	private ArrayList<ShapeStyling> shapeStyling;
	private HashMap<Integer, ArrayList<Integer>> paths;
	
	ArrayList<ShapeElement> shapeElements = new ArrayList<ShapeElement>();
	ShapeElement shapeElement;
	private int counter = 0; 
	
	public ShapeImportExport(ArrayList<ArrayList<Point>> points, ArrayList<ShapeStyling> shapeStyling, ArrayList<Shape> shape, HashMap<Integer, ArrayList<Integer>> paths)
	{
		this.points = points;
		this.shapeStyling = shapeStyling;
		this.shape = shape;
		this.paths = paths;
	}
	
	private String toXml()
	{
		String 
			type = "",
			content = "",
			xml = "";
		for(int shapeIndex = 0; shapeIndex < this.shape.size(); shapeIndex++)
		{
			int count = 0;
			for(Point p : points.get(shapeIndex))
			{
				type = stripString(shape.get(shapeIndex).getClass().getName());
				content += "Point" + count + "=\"" +  p.x + ", " + p.y + "\" ";
				if(paths != null && paths.containsKey(shapeIndex))
				{
					for(int pathVal : paths.get(shapeIndex))
					{
						content += "PathValue=\"" + pathVal + "\" ";
					}
				}
				count++;
			}
			ShapeStyling ss = shapeStyling.get(shapeIndex);
			Color c = ss.getColor();
			content += "Color=\"" + c.getRed() + ", " + c.getGreen() + ", " + c.getBlue() + "\" ";
			
			xml += "<" + type + " " + content + " > " + "</" + type + ">" + PathUtility.NEW_LINE;
			content = "";
		}
		return WIDGETS_SHAPE_OPEN_TAG + PathUtility.NEW_LINE + xml + WIDGETS_SHAPE_CLOSE_TAG;
	}
	
	private String stripString(String classname)
	{
		return classname.replace("$", ".");
	}
	
	public void performSave(Component parent)
	{
		String xml = toXml();
		LoggingMessages.printOut(xml);
		SaveAsDialog sad = new SaveAsDialog(parent, FILE_TYPE_TITLE, FILE_TYPE_FILTER, DEFAULT_DIRECTORY_RELATIVE);
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
			if(!filename.endsWith("." + FILE_TYPE_FILTER))
			{
				filename += "." + FILE_TYPE_FILTER;
			}
			FileWriter fw = new FileWriter(filename);
			fw.write(xml);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void openXml(Component parent)
	{
		OpenDialog od = new OpenDialog();
		File f = od.performOpen(parent, FILE_TYPE_TITLE, FILE_TYPE_FILTER, DEFAULT_DIRECTORY_RELATIVE);
		readXml(f.getAbsolutePath());
		for(ShapeElement se : shapeElements)
		{
			LoggingMessages.printOut(se.toString());
		}
	}
	
	private void readXml(String sourceFile)
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
			shapeElements = readXml(nl, null);
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			LoggingMessages.printOut("Malformed xml or non-widget build xml chosen: " + e.getMessage());
			LoggingMessages.printOut("Cause: " + LoggingMessages.combine(e.getStackTrace()));
		}
	}
	
	private ArrayList<ShapeElement> readXml(NodeList nl, String parentId)
	{
		if(nl != null)
		{
			for(int i = 0; i < nl.getLength(); i++)
			{
				Node n = nl.item(i);
				String nodeName = n.getNodeName();
				if(nodeName.equals("#text"))//ignore
					continue;
				ShapeElement shapeElement = readNode(n, parentId);
				if(shapeElement != null)
				{
					shapeElements.add(shapeElement);
				}
				NodeList nl2 = n.getChildNodes();
				if(n.getChildNodes() != null)
				{
					String counterId = null;
					if(n != null)
					{
						String nodeStr = n.getNodeName();
						counterId = nodeStr + counter;
						LoggingMessages.printOut(nodeStr + " " + counterId);
					}
					readXml(nl2, counterId);
				}
			}
		}
		return shapeElements;
	}
	
	private ShapeElement readNode(Node node, String parentNode)
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
		return new ShapeElement (
				node.getNodeName(),
				counter++, 
				attributes, 
				parentNode
		);
	}

}
