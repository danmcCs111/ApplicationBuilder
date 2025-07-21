package ShapeWidgetComponents;

import java.awt.Color;

import ShapeEditorListeners.ShapeStylingActionListener;

public class ShapeStyling 
{
	private int shapeIndex;
	private Color color;
	private ShapeStylingActionListener shapeStyleActionListener;
	
	public ShapeStyling(int shapeIndex, Color color, ShapeStylingActionListener shapeStyleActionListener)
	{
		this.shapeIndex = shapeIndex;
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
	
}
