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
	private Stroke stroke = new BasicStroke(2);//TODO
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
	
	public void setStroke(Stroke stroke)
	{
		this.stroke = stroke;
	}
	
	public void createStrokedShape(boolean create)
	{
		this.createStrokedShape = create;
		shapeStyleActionListener.notifyStylingChanged(shapeIndex, this);
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
		shapeStyleActionListener.notifyStylingChanged(shapeIndex, this);
	}
	
	public void setFillColor(Color c)
	{
		this.fillColor = c;
		shapeStyleActionListener.notifyStylingChanged(shapeIndex, this);
	}
	
}
