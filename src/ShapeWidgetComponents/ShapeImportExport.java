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

import org.w3c.dom.Node;

import Properties.LoggingMessages;
import Properties.PathUtility;
import Properties.SaveAsDialog;
import Properties.XmlNodeReader;

public class ShapeImportExport extends XmlNodeReader
{
	private static final String 
		WIDGETS_SHAPE_OPEN_TAG = "<Widgets>",
		WIDGETS_SHAPE_CLOSE_TAG = "</Widgets>";
	public static final String 
		FILE_TYPE_TITLE = "XML",
		FILE_TYPE_FILTER = "xml",
		DEFAULT_DIRECTORY_RELATIVE =  "/src/ApplicationBuilder/shapes/ ";
	
	private ArrayList<ArrayList<Point>> points;
	private ArrayList<Shape> shape;
	private ArrayList<ShapeStyling> shapeStyling;
	private HashMap<Integer, ArrayList<Integer>> paths;
	
	ArrayList<ShapeElement> shapeElements = new ArrayList<ShapeElement>();
	ShapeElement shapeElement;
	
	public ShapeImportExport(ArrayList<ArrayList<Point>> points, 
			ArrayList<ShapeStyling> shapeStyling, 
			ArrayList<Shape> shape, 
			HashMap<Integer, ArrayList<Integer>> paths)
	{
		this.points = points;
		this.shapeStyling = shapeStyling;
		this.shape = shape;
		this.paths = paths;
	}
	
	public ShapeImportExport()
	{
		
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
			String suffixCount = "0";//TODO up to 99
			for(Point p : points.get(shapeIndex))
			{
				type = stripString(shape.get(shapeIndex).getClass().getName());
				content += "Point" + (count < 10 ? suffixCount : "") + count + "=\"" +  p.x + ", " + p.y + "\" ";
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
			Color c = ss.getDrawColor();
			content += "ColorDraw=\"" + c.getRed() + ", " + c.getGreen() + ", " + c.getBlue() + "\" ";
			Color cFill = ss.getFillColor();
			content += "ColorFill=\"" + cFill.getRed() + ", " + cFill.getGreen() + ", " + cFill.getBlue() + "\" ";
			boolean createStrokedShape = ss.isCreateStrokedShape();
			content += "CreateStroke=\"" + createStrokedShape + "\" ";
			int strokeWth = ss.getStrokeWidth();
			if(strokeWth > 0)
			{
				content += "StrokeWidth=\"" + strokeWth + "\" ";
			}
			NumberGeneratorConfig ngConfig = ss.getNumberGeneratorConfig();
			if(ngConfig != null)
			{
				content += "NumberGeneratorConfig=\"" + ngConfig.toString() + "\" ";
			}
			content += "SkipShapeDraw=\"" + ss.skipShapeDraw() + "\"";
			
			xml += "<" + type + " " + content + " > " + "</" + type + ">" + PathUtility.NEW_LINE;
			content = "";
		}
		return WIDGETS_SHAPE_OPEN_TAG + PathUtility.NEW_LINE + xml + WIDGETS_SHAPE_CLOSE_TAG;
	}
	
	private String stripString(String classname)
	{
		return classname.split("\\$")[0];
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

	@Override
	public Object createNewObjectFromNode(Node n, ArrayList<String> attributes, int counter, String parentNode) 
	{
		return new ShapeElement (
				n.getNodeName(),
				counter, 
				attributes, 
				parentNode
		);
	}

}
