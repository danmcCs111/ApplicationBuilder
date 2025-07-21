package ShapeWidgetComponents;

import java.awt.Point;
import java.awt.Shape;
import java.util.ArrayList;

import Properties.LoggingMessages;
import Properties.PathUtility;

public class ShapeImportExport 
{
	private static final long serialVersionUID = 1L;
	
	private static final String 
		WIDGETS_SHAPE_OPEN_TAG = "<Widgets>",
		WIDGETS_SHAPE_CLOSE_TAG = "</Widgets>";
	
	private ArrayList<ArrayList<Point>> points;
	private ArrayList<Shape> shape;
	private ArrayList<ShapeStyling> shapeStyling;
	private ArrayList<ArrayList<Integer>> paths;
	
	public ShapeImportExport(ArrayList<ArrayList<Point>> points, ArrayList<ShapeStyling> shapeStyling, ArrayList<Shape> shape, ArrayList<ArrayList<Integer>> paths)
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
			for(Point p : points.get(shapeIndex))
			{
				type = shape.get(shapeIndex).getClass().getName();
				content += "<Point>" +  p.x + ", " + p.y + "</Point>" + PathUtility.NEW_LINE;
				if(paths != null)
				{
					for(int pathVal : paths.get(shapeIndex))
					{
						content += "<PathValue>" + pathVal + "</PathValue>" + PathUtility.NEW_LINE;
					}
				}
			}
			xml += "<" + type + ">" + PathUtility.NEW_LINE + content + "</" + type + ">" + PathUtility.NEW_LINE;
		}
		return WIDGETS_SHAPE_OPEN_TAG + PathUtility.NEW_LINE + xml + WIDGETS_SHAPE_CLOSE_TAG;
	}
	
	public void performSave()
	{
		LoggingMessages.printOut(toXml());
	}

}
