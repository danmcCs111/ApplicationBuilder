package ShapeWidgetComponents;

import java.awt.Color;
import java.awt.Shape;

public class ShapeStyling 
{
	private Shape shape;
	private Color color;
	
	public ShapeStyling(Shape shape, Color color)
	{
		this.shape = shape;
		this.color = color;
	}
	
	public Color getColor()
	{
		return this.color;
	}
}
