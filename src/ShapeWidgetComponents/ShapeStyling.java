package ShapeWidgetComponents;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.PathIterator;

import BezierCurveCalculations.AffineTransformRasterizer;
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
	private boolean 
		createStrokedShape = false,
		skipShapeDraw = false;
	private NumberGeneratorConfig numberGeneratorConfig;
	private PathIterator pi;
	private AffineTransformRasterizer afs; 
	
	public ShapeStyling(ShapeStyling ss)
	{
		this.setDrawColor(ss.getDrawColor());
		this.setFillColor(ss.getFillColor());
		this.setIndex(ss.getIndex());
		this.setNumberGeneratorConfig(ss.getNumberGeneratorConfig());
		this.setSkipShapeDraw(ss.skipShapeDraw());
		this.setStrokeWidth(ss.getStrokeWidth());
	}
	
	public ShapeStyling(int shapeIndex, Color drawColor, Color fillColor, ShapeStylingActionListener shapeStyleActionListener)
	{
		this.shapeIndex = shapeIndex;
		this.drawColor = drawColor;
		this.fillColor = fillColor;
		this.shapeStyleActionListener = shapeStyleActionListener;
	}
	
	public int getIndex()
	{
		return this.shapeIndex;
	}
	
	public void setIndex(int index)
	{
		this.shapeIndex = index;
	}
	
	public void setSkipShapeDraw(boolean skipShapeDraw)
	{
		this.skipShapeDraw = skipShapeDraw;
		notifyChange();
	}
	
	public boolean skipShapeDraw()
	{
		return this.skipShapeDraw;
	}
	
	public PathIterator getPathIterator()
	{
		return this.pi;
	}
	
	public AffineTransformRasterizer getAffineTransform()
	{
		return this.afs;
	}
	
	public void setNumberGeneratorConfig(NumberGeneratorConfig ngConfig)
	{
		this.numberGeneratorConfig = ngConfig;
		notifyChange();
	}
	
	public void updateNumberGeneratorConfig(Shape s)
	{
		if(s == null)
		{
			this.afs = null;
			this.pi = null;
		}
		else
		{
			this.afs = new AffineTransformRasterizer();
			this.pi = s.getPathIterator(afs);
			if(numberGeneratorConfig != null)
			{
				numberGeneratorConfig.generateNumberGraphics(s, this);
			}
		}
	}
	
	public NumberGeneratorConfig getNumberGeneratorConfig()
	 {
		return this.numberGeneratorConfig;
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
		notifyChange();
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
