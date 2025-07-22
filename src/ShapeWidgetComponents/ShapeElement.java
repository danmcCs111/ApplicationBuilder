package ShapeWidgetComponents;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import Properties.LoggingMessages;
import ShapeEditorListeners.ShapeStylingActionListener;

public class ShapeElement 
{
	public static final String ID_SPLITTER = "#";

	private String 
		nodeName,
		nodeId,
		parentNode;
	private ArrayList<String> attributes;
	private Class<?> shapeClass = null;
	
	private Color 
		colorDraw,
		colorFill;
	private boolean isCreateStroke = false;
	private int strokeWidth = -1;
	private ArrayList<Point> controlPoints = new ArrayList<Point>();
	
	public ShapeElement(String nodeName, int count, ArrayList<String> attributes, String parentNode)
	{
		this.nodeName = nodeName;
		this.nodeId = nodeName + ID_SPLITTER + count;
		this.attributes = attributes;
		this.parentNode = parentNode;
		generateNodeClassObject();
		parseAttributes();
	}
	
	public boolean isCreateStroke()
	{
		return this.isCreateStroke;
	}
	
	public int getStrokeWidth()
	{
		return this.strokeWidth;
	}
	
	public String getShapeClassName()
	{
		return this.shapeClass.getName();
	}
	
	public ArrayList<String> getAttributes()
	{
		return this.attributes;
	}
	
	public ShapeStyling getShapeStyling(int index, ShapeStylingActionListener actionListener)
	{
		LoggingMessages.printOut(colorDraw + "");
		ShapeStyling ss = new ShapeStyling(index, colorDraw, colorFill, actionListener);
		ss.createStrokedShape(isCreateStroke);
		ss.setStrokeWidth(strokeWidth);
		return ss;
	}
	
	public ArrayList<Point> getPoints()
	{
		return this.controlPoints;
	}
	
	private void generateNodeClassObject()
	{
		try {
			this.shapeClass = Class.forName(this.nodeName);
			LoggingMessages.printOut(shapeClass.getName());
		} catch (ClassNotFoundException e) {
		}
	}
	
	private void parseAttributes()
	{
		LoggingMessages.printOut(attributes.size() + "");
		for(String s : attributes)
		{
			s = s.replaceAll("\"", "");
			if(s.startsWith("Point"))
			{
				String valuePoint = s.split("=")[1];
				String [] points = valuePoint.split(",");
				Point p = new Point(Integer.parseInt(points[0].strip()), Integer.parseInt(points[1].strip()));
				controlPoints.add(p);
			}
			else if(s.startsWith("ColorDraw"))
			{
				String colorPoints = s.split("=")[1];
				String [] points = colorPoints.split(",");
				Color c = new Color(Integer.parseInt(points[0].strip()), Integer.parseInt(points[1].strip()), Integer.parseInt(points[2].strip()));
				colorDraw = c;
			}
			else if(s.startsWith("ColorFill"))
			{
				String colorPoints = s.split("=")[1];
				String [] points = colorPoints.split(",");
				Color c = new Color(Integer.parseInt(points[0].strip()), Integer.parseInt(points[1].strip()), Integer.parseInt(points[2].strip()));
				colorFill = c;
			}
			else if(s.startsWith("CreateStroke"))
			{
				this.isCreateStroke = Boolean.parseBoolean(s.split("=")[1]);
			}
			else if(s.startsWith("StrokeWidth"))
			{
				this.strokeWidth = Integer.parseInt(s.split("=")[1]);
			}
		}
	}
	
	@Override
	public String toString()
	{
		return nodeId + ", " + parentNode + ", " + LoggingMessages.combine(attributes);
	}
	
}
