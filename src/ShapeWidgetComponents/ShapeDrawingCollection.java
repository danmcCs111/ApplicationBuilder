package ShapeWidgetComponents;

import java.awt.Point;
import java.awt.Shape;
import java.util.ArrayList;

public class ShapeDrawingCollection 
{
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
