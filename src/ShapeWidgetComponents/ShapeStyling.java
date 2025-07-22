package ShapeWidgetComponents;

import java.awt.Color;

import ShapeEditorListeners.ShapeStylingActionListener;

public class ShapeStyling 
{
	private int shapeIndex;
	private Color 
		drawColor,
		fillColor;
	private ShapeStylingActionListener shapeStyleActionListener;
	
	public ShapeStyling(int shapeIndex, Color drawColor, Color fillColor, ShapeStylingActionListener shapeStyleActionListener)
	{
		this.shapeIndex = shapeIndex;
		this.drawColor = drawColor;
		this.fillColor = fillColor;
		this.shapeStyleActionListener = shapeStyleActionListener;
	}
	
	public Color getDrawColor()
	{
		return this.drawColor;
	}
	
	public Color getFillColor()
	{
		return this.fillColor;
	}
	
	public void setDrawColor(Color c)
	{
		this.drawColor = c;
		shapeStyleActionListener.notifyStylingChanged(shapeIndex, this);
	}
	
	public void setFillColor(Color c)
	{
		this.fillColor = c;
		shapeStyleActionListener.notifyStylingChanged(shapeIndex, this);
	}
	
}
