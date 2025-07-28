package ShapeWidgetComponents;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Shape;
import java.util.ArrayList;

import Properties.LoggingMessages;
import ShapeEditorListeners.AddShapesImportedListener;
import ShapeEditorListeners.ShapeStylingActionListener;

public class ShapeDrawingCollection 
{
	public static Dimension CONTROL_POINT_PIXEL_SIZE = new Dimension(6,6);
	public static final BasicStroke defaultStroke = new BasicStroke(1);
	
	ArrayList<ArrayList<Point>> shapeControlPoints = new ArrayList<ArrayList<Point>>();
	ArrayList<Shape> shapes = new ArrayList<Shape>();
	ArrayList<ShapeStyling> shapeStylings = new ArrayList<ShapeStyling>();
	ArrayList<AddShapesImportedListener> asils = new ArrayList<AddShapesImportedListener>();
	
	public ShapeDrawingCollection()
	{
		
	}
	
	public void addShapeImportedListener(AddShapesImportedListener asil)
	{
		asils.add(asil);
	}
	
	
	public void addShapeImports(ArrayList<ShapeElement> shapeElements, ShapeStylingActionListener ssal)
	{
		int count = shapes.size();
		for(ShapeElement se : shapeElements)
		{
			LoggingMessages.printOut(se.toString());
			this.addShapeControlPoints(se.getPoints());
			ShapeStyling ss = se.getShapeStyling(count, ssal);
			Shape s = se.getShape(ss);
			this.addShapeStyling(ss);
			this.addShape(s);
			
			LoggingMessages.printOut(ss.toString());
			for(AddShapesImportedListener asil : asils)
			{
				asil.notifyImported(se);
			}
			
			count++;
		}
	}
	
	public void addShape(Shape s)
	{
		this.shapes.add(s);
	}
	
	public void addShapeControlPoints(ArrayList<Point> shapeControlPoints)
	{
		this.shapeControlPoints.add(shapeControlPoints);
	}
	
	public void addShapeStyling(ShapeStyling ss)
	{
		this.shapeStylings.add(ss);
	}
	
	public ArrayList<Shape> getShapes()
	{
		return this.shapes;
	}
	public void setShapes(ArrayList<Shape> shapesRepl)
	{
		this.shapes = shapesRepl;
	}
	
	public ArrayList<ArrayList<Point>> getShapeControlPoints()
	{
		return this.shapeControlPoints;
	}
	public void setShapeControlPoints(ArrayList<ArrayList<Point>> shapeControlPoints)
	{
		this.shapeControlPoints = shapeControlPoints;
	}
	
	public ArrayList<ShapeStyling> getShapeStylings()
	{
		return this.shapeStylings;
	}
	public void setShapeStylings(ArrayList<ShapeStyling> shapeStylings)
	{
		this.shapeStylings = shapeStylings;
	}
	
}
