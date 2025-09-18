package ShapeWidgetComponents;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.util.ArrayList;

import BezierCurveCalculations.AffineTransformRasterizer;
import BezierCurveCalculations.ShapePositionOnPoints;

public interface ShapeDrawingCollectionGraphics 
{
	public static void clearAll(Container drawPanel)
	{
		Graphics2D g2d = (Graphics2D) drawPanel.getGraphics();
		if(g2d != null)
			drawPanel.paint(g2d);
	}
	
	public static void drawAll(Container drawPanel, ShapeDrawingCollection sdc, Shape selectionRect)
	{
		drawAll(drawPanel, sdc, selectionRect, true);
	}
	public static void drawAll(Container drawPanel, ShapeDrawingCollection sdc, Shape selectionRect, boolean drawControlPoints)
	{
		Graphics2D g2d = (Graphics2D) drawPanel.getGraphics();
		clearAll(drawPanel);
		drawShapes(g2d, sdc);
		if(selectionRect != null) drawShape(g2d, selectionRect, Color.gray);
		if(drawControlPoints) drawControlPoints(g2d, sdc);
		drawGenerators(g2d, sdc);
	}
	
	public static void drawGenerators(Container draw, ShapeDrawingCollection sdc)
	{
		drawGenerators((Graphics2D)draw.getGraphics(), sdc);
	}
	public static void drawGenerators(Graphics2D g2d, ShapeDrawingCollection sdc)
	{
		int count = 0;
		for(Shape s : sdc.getShapes())
		{
			ShapeStyling ss = sdc.getShapeStylings().get(count);
			if(ss.getNumberGeneratorConfig() != null)
			{
				ss.updateNumberGeneratorConfig(s);
				drawShapeStylingAlgorithm(g2d, s, ss);
			}
			count++;
		}
	}
	
	public static void drawShapes(Container drawPanel, ShapeDrawingCollection sdc)
	{
		drawShapes((Graphics2D)drawPanel.getGraphics(), sdc);
	}
	public static void drawShapes(Graphics2D g2d, ShapeDrawingCollection sdc)
	{
		int count = 0;
		for(Shape s : sdc.getShapes())
		{
			drawShape(g2d, s, sdc.getShapeStylings().get(count));
			count++;
		}
	}
	public static void drawShape(Container draw, Shape shape, Color c)
	{
		drawShape((Graphics2D)draw.getGraphics(), shape, c);
	}
	public static void drawShape(Graphics2D g2d, Shape shape, Color c)//Use defaultStroke
	{
		if(g2d == null)
			return;
		g2d.setStroke(ShapeDrawingCollection.defaultStroke);
		g2d.setColor(c);
		g2d.draw(shape);
	}
	
	public static void drawShape(Container draw, Shape shape, ShapeStyling shapeStyling)
	{
		drawShape((Graphics2D)draw.getGraphics(), shape, shapeStyling);
	}
	public static void drawShape(Graphics2D g2d, Shape shape, ShapeStyling shapeStyling)
	{
		if(g2d == null || shapeStyling.skipShapeDraw())
			return;
		Color c = shapeStyling.getDrawColor(); 
		Color fillColor = shapeStyling.getFillColor();
		Stroke stroke = shapeStyling.getStroke();
		Shape s = shape;
		
		if(stroke != null && shapeStyling.isCreateStrokedShape())
		{
			g2d.setStroke(stroke);
			s = g2d.getStroke().createStrokedShape(shape);
		}
		else {
			g2d.setStroke(ShapeDrawingCollection.defaultStroke);
		}
		g2d.setColor(c);
		g2d.draw(s);
		if(fillColor != null)
		{
			g2d.setColor(fillColor);
			g2d.fill(s);
		}
	}
	public static void drawControlPoint(Container draw, Point p)
	{
		drawControlPoint((Graphics2D)draw.getGraphics(), p);
	}
	public static void drawControlPoint(Graphics2D g2d, Point p)
	{
		if(g2d == null)
			return;
		Rectangle r = new Rectangle(p);
		r.setSize(ShapeDrawingCollection.CONTROL_POINT_PIXEL_SIZE.width, ShapeDrawingCollection.CONTROL_POINT_PIXEL_SIZE.height);
		g2d.setStroke(ShapeDrawingCollection.defaultStroke);
		g2d.setColor(Color.black);
		g2d.draw(r);
	}
	public static void drawControlPoints(Container draw, ShapeDrawingCollection sdc)
	{
		drawControlPoints((Graphics2D)draw.getGraphics(), sdc);
	}
	public static void drawControlPoints(Graphics2D g2d, ShapeDrawingCollection sdc)
	{
		for(ArrayList<Point> controlPoints : sdc.getShapeControlPoints())
		{
			for(Point p : controlPoints)
			{
				drawControlPoint(g2d, p);
			}
		}
	}
	
	public static void drawGlyph(Container draw, String text, Point p, Font font, Color fillColor)
	{
		drawGlyph((Graphics2D)draw.getGraphics(), text, p, font, fillColor);
	}
	public static void drawGlyph(Graphics2D g2d, String text, Point p, Font font, Color fillColor)
	{
		FontRenderContext frc = g2d.getFontRenderContext();
		g2d.setFont(font);
		g2d.setColor(fillColor);
		GlyphVector gv = g2d.getFont().createGlyphVector(frc, text);
		g2d.drawGlyphVector(gv, p.x, p.y);
	}
	
	public static void drawShapeStylingAlgorithm(Container draw, Shape s, ShapeStyling ss)
	{
		drawShapeStylingAlgorithm((Graphics2D)draw.getGraphics(), s, ss);
	}
	public static void drawShapeStylingAlgorithm(Graphics2D g2d, Shape s, ShapeStyling ss)
	{
		NumberGeneratorConfig ngConfig = ss.getNumberGeneratorConfig();
		if(ngConfig == null)
			return;
		Color selectColor = ngConfig.getFillColor();
		Font testFont = ngConfig.getFont();
		AffineTransformRasterizer afs = ngConfig.getAffineTransformRasterizer();
		ArrayList<Point> points = ngConfig.getPoints();
		double it = ngConfig.getSampleSkipDivide();
		
		if(afs == null)
		{
			return;
		}
		if(selectColor == null)
		{
			selectColor = Color.black;
		}
		
		ShapePositionOnPoints.drawNumberSequence(
				points, 
				g2d, 
				testFont, 
				selectColor,
				(ngConfig.getNumberOfSamples()/it), 
				ngConfig.getRangeValLow(), 
				ngConfig.getRangeValHigh(), 
				ngConfig.getStartingNumber()
		);
	}
}
