package ShapeWidgetComponents;

import java.awt.Color;
import java.awt.Shape;

import ShapeEditorListeners.ShapeStylingActionListener;

public class ShapeStyling 
{
	private Shape shape;
	private int shapeIndex;
	private Color color;
	private ShapeStylingActionListener shapeStyleActionListener;
//	private ArrayList<Point> points;
	
	public ShapeStyling(int shapeIndex, Shape shape, Color color, ShapeStylingActionListener shapeStyleActionListener)
	{
		this.shapeIndex = shapeIndex;
		this.shape = shape;
		this.color = color;
		this.shapeStyleActionListener = shapeStyleActionListener;
	}
	
	public Color getColor()
	{
		return this.color;
	}
	
	public void setColor(Color c)
	{
		this.color = c;
		shapeStyleActionListener.notifyStylingChanged(shapeIndex, this);
	}
	
	public Shape getShape()
	{
		return this.shape;
	}
	
//	public ArrayList<Point> getPoints()
//	{
//		return this.points;
//	}
}
