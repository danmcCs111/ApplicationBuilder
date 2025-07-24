package ShapeWidgetComponents;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

import ShapeEditorListeners.ShapeStylingActionListener;

public class ShapeStyling 
{
	private int shapeIndex;
	private Color 
		drawColor,
		fillColor;
	private ShapeStylingActionListener shapeStyleActionListener;
	private Stroke stroke;
	private int strokeWidth = -1;
	private boolean createStrokedShape = false;
	
	public ShapeStyling(int shapeIndex, Color drawColor, Color fillColor, ShapeStylingActionListener shapeStyleActionListener)
	{
		this.shapeIndex = shapeIndex;
		this.drawColor = drawColor;
		this.fillColor = fillColor;
		this.shapeStyleActionListener = shapeStyleActionListener;
	}
	
	public Stroke getStroke()
	{
		return this.stroke;
	}
	
	public void setStrokeWidth(int strokeWidth)
	{
		this.strokeWidth = strokeWidth;
		if(strokeWidth <= 0)
		{
			createStrokedShape = false;
		}
		else
		{
			this.stroke = new BasicStroke(this.strokeWidth);
		}
	}
	
	private void notifyChange()
	{
		if(shapeIndex > -1 && shapeStyleActionListener != null)
		{
			shapeStyleActionListener.notifyStylingChanged(shapeIndex, this);
		}
	}
	
	public int getStrokeWidth()
	{
		return this.strokeWidth;
	}
	
	public void createStrokedShape(boolean create)
	{
		this.createStrokedShape = create;
		notifyChange();
	}
	
	public boolean isCreateStrokedShape()
	{
		return this.createStrokedShape;
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
		notifyChange();
	}
	
	public void setFillColor(Color c)
	{
		this.fillColor = c;
		notifyChange();
	}
	
}
