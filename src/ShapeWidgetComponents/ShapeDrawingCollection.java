package ShapeWidgetComponents;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Shape;
import java.util.ArrayList;

public class ShapeDrawingCollection 
{
	public static Dimension CONTROL_POINT_PIXEL_SIZE = new Dimension(6,6);
	public static final BasicStroke defaultStroke = new BasicStroke(1);
	
	ArrayList<Shape> shapes = new ArrayList<Shape>();
	ArrayList<ArrayList<Point>> shapeControlPoints = new ArrayList<ArrayList<Point>>();
	ArrayList<ShapeStyling> shapeStylings = new ArrayList<ShapeStyling>();
	
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
