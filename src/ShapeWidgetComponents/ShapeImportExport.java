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

import Properties.LoggingMessages;
import Properties.PathUtility;
import Properties.SaveAsDialog;

public class ShapeImportExport 
{
	private static final long serialVersionUID = 1L;
	
	private static final String 
		WIDGETS_SHAPE_OPEN_TAG = "<Widgets>",
		WIDGETS_SHAPE_CLOSE_TAG = "</Widgets>";
	
	private ArrayList<ArrayList<Point>> points;
	private ArrayList<Shape> shape;
	private ArrayList<ShapeStyling> shapeStyling;
	private HashMap<Integer, ArrayList<Integer>> paths;
	
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
			for(Point p : points.get(shapeIndex))
			{
				type = shape.get(shapeIndex).getClass().getName();
				content += "<Point>" +  p.x + ", " + p.y + "</Point>" + PathUtility.NEW_LINE;
				if(paths != null && paths.containsKey(shapeIndex))
				{
					for(int pathVal : paths.get(shapeIndex))
					{
						content += "<PathValue>" + pathVal + "</PathValue>" + PathUtility.NEW_LINE;
					}
				}
			}
			ShapeStyling ss = shapeStyling.get(shapeIndex);
			Color c = ss.getColor();
			content += "<Color>" + c.getRed() + ", " + c.getGreen() + ", " + c.getBlue() + "</Color>" + PathUtility.NEW_LINE;
			xml += "<" + type + ">" + PathUtility.NEW_LINE + content + "</" + type + ">" + PathUtility.NEW_LINE;
			content = "";
		}
		return WIDGETS_SHAPE_OPEN_TAG + PathUtility.NEW_LINE + xml + WIDGETS_SHAPE_CLOSE_TAG;
	}
	
	public void performSave(Component parent)
	{
		String xml = toXml();
		LoggingMessages.printOut(xml);
		SaveAsDialog sad = new SaveAsDialog(parent, "XML", "xml", "/src/ApplicationBuilder/shapes/ ");
		File f = sad.getSelectedDirectory();
		if(f != null)
		{
			writeXml(f, xml);
		}
	}
	
	private void writeXml(File sourceFile, String xml)
	{
		try {
			FileWriter fw = new FileWriter(sourceFile);
			fw.write(xml);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
