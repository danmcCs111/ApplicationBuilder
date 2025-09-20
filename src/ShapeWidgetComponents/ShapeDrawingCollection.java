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
	
	private ArrayList<ArrayList<Point>> shapeControlPoints = new ArrayList<ArrayList<Point>>();
	private ArrayList<Shape> shapes = new ArrayList<Shape>();
	private ArrayList<ShapeStyling> shapeStylings = new ArrayList<ShapeStyling>();
	private ArrayList<AddShapesImportedListener> asils = new ArrayList<AddShapesImportedListener>();
	
	public ShapeDrawingCollection()
	{
		
	}
	
	public void addShapeImportedListener(AddShapesImportedListener asil)
	{
		if(!asils.contains(asil))
		{
			asils.add(asil);
		}
	}

	public ArrayList<ShapeStyling> addShapeImports(ArrayList<ShapeElement> shapeElements, ShapeStylingActionListener ssal)
	{
		ArrayList<ShapeStyling> sss = new ArrayList<ShapeStyling>();
		for(ShapeElement se : shapeElements)
		{
			int count = shapes.size();
			LoggingMessages.printOut(se.toString());
			this.addShapeControlPoints(se.getPoints());
			Shape s = se.getShape();
			ShapeStyling ss = se.getShapeStyling(count, ssal);//added through notify
			this.addShape(s);
			for(AddShapesImportedListener asil : asils)
			{
				asil.notifyImported(se);
			}
			sss.add(ss);
		}
		LoggingMessages.printOut("style size: " + shapeStylings.size());
		
		return sss;
	}
	
	public void remove(int index)
	{
		for(ShapeStyling ss : this.shapeStylings)
		{
			if(ss.getIndex() > index)
			{
				ss.setIndex(ss.getIndex()-1);
			}
		}
		this.shapes.remove(index);
		this.shapeStylings.remove(index);
		this.shapeControlPoints.remove(index);
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
		LoggingMessages.printOut("added: " + ss.getDrawColor() + this.shapeStylings.size());
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
