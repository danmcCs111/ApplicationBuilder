package ShapeWidgetComponents;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
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
		clearAll(drawPanel);
		drawShapes(drawPanel, sdc);
		if(selectionRect != null) drawShape(drawPanel, selectionRect, Color.gray);
		if(drawControlPoints) drawControlPoints(drawPanel, sdc);
		drawGenerators(drawPanel, sdc);
	}
	
	public static void drawGenerators(Container drawPanel, ShapeDrawingCollection sdc)
	{
		int count = 0;
		for(Shape s : sdc.getShapes())
		{
			ShapeStyling ss = sdc.getShapeStylings().get(count);
			if(ss.getNumberGeneratorConfig() != null)
			{
				ss.updateNumberGeneratorConfig(s);
				drawShapeStylingAlgorithm(drawPanel, s, ss);
			}
			count++;
		}
	}
	
	public static void drawShapes(Container drawPanel, ShapeDrawingCollection sdc)
	{
		int count = 0;
		for(Shape s : sdc.getShapes())
		{
			drawShape(drawPanel, s, sdc.getShapeStylings().get(count));
			count++;
		}
	}
	public static void drawShape(Container drawPanel, Shape shape, Color c)
	{
		Graphics2D g2d = (Graphics2D)drawPanel.getGraphics();
		if(g2d == null)
			return;
		g2d.setColor(c);
		g2d.draw(shape);
	}
	public static void drawShape(Container drawPanel, Shape shape, ShapeStyling shapeStyling)
	{
		Graphics2D g2d = (Graphics2D)drawPanel.getGraphics();
		if(g2d == null || shapeStyling.skipShapeDraw())
			return;
		Color c = shapeStyling.getDrawColor(); 
		Color fillColor = shapeStyling.getFillColor();
		Stroke stroke = shapeStyling.getStroke();
		Shape s = shape;
		
		if(stroke != null && !g2d.getStroke().equals(stroke) && shapeStyling.isCreateStrokedShape())
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
	public static void drawControlPoint(Container drawPanel, Point p)
	{
		Graphics2D g2d = (Graphics2D)drawPanel.getGraphics();
		if(g2d == null)
			return;
		Rectangle r = new Rectangle(p);
		r.setSize(ShapeDrawingCollection.CONTROL_POINT_PIXEL_SIZE.width, ShapeDrawingCollection.CONTROL_POINT_PIXEL_SIZE.height);
		g2d.setColor(Color.black);
		g2d.draw(r);
	}
	public static void drawControlPoints(Container drawPanel, ShapeDrawingCollection sdc)
	{
		for(ArrayList<Point> controlPoints : sdc.getShapeControlPoints())
		{
			for(Point p : controlPoints)
			{
				drawControlPoint(drawPanel, p);
			}
		}
	}
	
	public static void drawShapeStylingAlgorithm(Container drawPanel, Shape s, ShapeStyling ss)
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
				(Graphics2D)drawPanel.getGraphics(), 
				testFont, 
				selectColor,
				(ngConfig.getNumberOfSamples()/it), 
				ngConfig.getRangeValLow(), 
				ngConfig.getRangeValHigh(), 
				ngConfig.getStartingNumber()
		);
	}
}
