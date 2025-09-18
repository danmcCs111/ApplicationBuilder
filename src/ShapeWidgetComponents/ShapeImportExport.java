package ShapeWidgetComponents;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Node;

import Properties.PathUtility;
import Properties.XmlNodeReader;

public class ShapeImportExport extends XmlNodeReader
{
	private static final String 
		WIDGETS_SHAPE_OPEN_TAG = "<Widgets>",
		WIDGETS_SHAPE_CLOSE_TAG = "</Widgets>";
	public static final String 
		FILE_TYPE_TITLE = "XML",
		FILE_TYPE_FILTER = "xml",
		DEFAULT_DIRECTORY_RELATIVE =  "/Properties/shapes/ ";
	
	ArrayList<ShapeElement> shapeElements = new ArrayList<ShapeElement>();
	ShapeElement shapeElement;
	
	public ShapeImportExport()
	{
		
	}
	
	public String toXml(ShapeDrawingCollection sdc, HashMap<Integer, ArrayList<Integer>> paths)
	{
		String 
			type = "",
			content = "",
			xml = "";
		for(int shapeIndex = 0; shapeIndex < sdc.getShapes().size(); shapeIndex++)
		{
			int count = 0;
			String suffixCount = "0";//TODO up to 99
			for(Point p : sdc.getShapeControlPoints().get(shapeIndex))
			{
				type = stripString(sdc.getShapes().get(shapeIndex).getClass().getName());
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
			ShapeStyling ss = sdc.getShapeStylings().get(shapeIndex);
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

	@Override
	public String getFileTypeTitle() 
	{
		return FILE_TYPE_TITLE;
	}

	@Override
	public String getFileTypeFilter() 
	{
		return FILE_TYPE_FILTER;
	}

	@Override
	public String getDefaultDirectoryRelative() 
	{
		return DEFAULT_DIRECTORY_RELATIVE;
	}

}
