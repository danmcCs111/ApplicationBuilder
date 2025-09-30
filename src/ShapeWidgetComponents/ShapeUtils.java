package ShapeWidgetComponents;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import DrawModes.GeneralPathDrawMode;
import DrawModesAbstract.DrawMode;
import Graphics2D.CurveShape;
import Properties.LoggingMessages;

public class ShapeUtils 
{
	public static Shape recalculateShape(Shape s, ArrayList<Point> cps)
	{
		if(s instanceof TextShape)
		{
			Font font = ((TextShape) s).getFont();
			Graphics2D g2d = ((TextShape) s).getGraphics();
			String text = ((TextShape) s).getText();
			
			s = new TextShape(text, cps.get(0), font, g2d);
		}
		if(s instanceof CurveShape)
		{
			s = new CurveShape(cps.get(0), cps.get(2), cps.get(3), cps.get(1));
		}
		else if(s instanceof Line2D)
		{
			s = new Line2D.Double(cps.get(0), cps.get(1));
		}
		else if(s instanceof Triangle)
		{
			s = new Triangle(cps.get(0), cps.get(1), cps.get(2));
		}
		else if(s instanceof TriangleCubic)
		{
			s = new TriangleCubic(
					cps.get(0), cps.get(1), 
					cps.get(2), cps.get(3), 
					cps.get(4), cps.get(5), 
					cps.get(6), cps.get(7), 
					cps.get(8));
		}
		else if(s instanceof Rectangle2D)
		{
			s = new Rectangle2D.Double(
					cps.get(0).x, cps.get(0).y, 
					(cps.get(1).x - cps.get(0).x), 
					(cps.get(1).y - cps.get(0).y));
		}
		else if(s instanceof RectangleCubic)
		{
			s = new RectangleCubic(
					cps.get(0), cps.get(1), 
					cps.get(2), cps.get(3), 
					cps.get(4), cps.get(5), 
					cps.get(6), cps.get(7), 
					cps.get(8), cps.get(9), 
					cps.get(10), cps.get(11));
		}
		else if(s instanceof Ellipse2D)
		{
			s = new Ellipse2D.Double(
					cps.get(0).x, cps.get(0).y, 
					(cps.get(1).x - cps.get(0).x), 
					(cps.get(1).y - cps.get(0).y));
		}
		else if(s instanceof GeneralPathShape)
		{
			GeneralPathShape gps = (GeneralPathShape) s;
			s = GeneralPathDrawMode.recalculateShape(gps, cps);
		}
		return s;
	}
	
	public static ArrayList<Point> rotate(Shape s, ArrayList<Point> cps, double degreesRotate)
	{
		Rectangle2D bounds = s.getBounds2D();
		double 
			cX = bounds.getCenterX(),
			cY = bounds.getCenterY();
		double angleRadians = Math.toRadians(degreesRotate);
		
		ArrayList<Point> newPoints = new ArrayList<Point>();
		for(Point p : cps)
		{
			double 
				translatedX = p.x - cX,
		        translatedY = p.y - cY,
		        rotatedX = translatedX * Math.cos(angleRadians) - translatedY * Math.sin(angleRadians),
		        rotatedY = translatedX * Math.sin(angleRadians) + translatedY * Math.cos(angleRadians);
	        
	        rotatedX += cX;
	        rotatedY += cY;
	        newPoints.add(new Point((int)rotatedX, (int)rotatedY));
		}
		
		return newPoints;
	}
	
	/**
	 * @param s
	 * @param cps
	 * @param scalePercent greater than 1 to apply positive scale. less than 1 for negative scaling
	 * @return
	 */
	public static ArrayList<Point> scaleControlPoints(Shape s, ArrayList<Point> cps, double scalePercent)
	{
		Rectangle2D bounds = s.getBounds2D();
		double 
			widthAdjustApply = (bounds.getWidth() * scalePercent) - bounds.getWidth(),
			heightAdjustApply = (bounds.getHeight() * scalePercent) - bounds.getHeight(),
			centerX = bounds.getCenterX(),
			centerY = bounds.getCenterY();
		
		ArrayList<Point> newPoints = new ArrayList<Point>();
		for(Point p : cps)
		{
			int adjustX = p.x > centerX ? 1 : -1;
			int adjustY = p.y > centerY ? 1 : -1;
			Point newPoint = new Point (
					(int)((adjustX * widthAdjustApply) + p.x), 
					(int)((adjustY * heightAdjustApply) + p.y)
			);
			newPoints.add(newPoint);
			LoggingMessages.printOut("old point: " + p.x + ", " + p.y);
			LoggingMessages.printOut("new point: " + newPoint.x + ", " + newPoint.y);
			LoggingMessages.printOut("Center: " + centerX + ", " + centerY);
		}
		
		return newPoints;
	}
	
	public static Shape constructShape(DrawMode mode, Point [] curvePoints, Graphics2D g2d)
	{
		Shape shape = null;
		shape = mode.constructShape(curvePoints, g2d);
		
		return shape;
	}
	
}
