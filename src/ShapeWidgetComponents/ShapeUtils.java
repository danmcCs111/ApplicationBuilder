package ShapeWidgetComponents;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Graphics2D.CurveShape;
import Properties.LoggingMessages;
import WidgetUtility.WidgetBuildController;

public class ShapeUtils 
{
	private static final String [] //TODO
		CURVE_DIRECTIONS = new String [] {
			"",
			"Enter x, y", 
			"Enter x2, y2", 
			"Enter Control Point 1", 
			"Enter Control Point 2"
		},
		TEXT_DIRECTIONS = new String [] {
			"",
			"Enter x, y"
		},
		LINE_DIRECTIONS = new String [] {
			"",
			"Enter x, y", 
			"Enter x2, y2"
		},
		ELLIPSE_DIRECTIONS = new String [] {
			"",
			"Enter x, y",
			"Enter x2, y2"
		},
		RECTANGLE_DIRECTIONS = new String [] {
				"",
				"Enter x, y",
				"Enter x2, y2"
		},
		RECTANGLE_CUBIC_DIRECTIONS = new String [] {
				"",
				"Enter x, y",
				"Enter x2, y2",
				"Enter x3, y3",
				"Enter x4, y4",
				
				"Enter control 1, y1",
				"Enter control 1.2, y1.2",
				
				"Enter control 2, y2",
				"Enter control 2.2, y2.2",
				
				"Enter control 3, y3",
				"Enter control 3.2, y3.2",
				
				"Enter control 4, y4",
				"Enter control 4.2, y4.2"
		},
		TRIANGLE_DIRECTIONS = new String [] {
				"",
				"Enter x, y",
				"Enter x2, y2",
				"Enter x3, y3"
		},
		TRIANGLE_CUBIC_DIRECTIONS = new String [] {
				"",
				"Enter x, y",
				"Enter x2, y2",
				"Enter x3, y3",
				
				"Enter control 1, y1",
				"Enter control 1.2, y1.2",
				
				"Enter control 2, y2",
				"Enter control 2.2, y2.2",
				
				"Enter control 3, y3",
				"Enter control 3.2, y3.2"
		};
	
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
			s = new TriangleCubic(cps.get(0), cps.get(1), cps.get(2), cps.get(3), cps.get(4), cps.get(5), cps.get(6), cps.get(7), cps.get(8));
		}
		else if(s instanceof Rectangle2D)
		{
			s = new Rectangle2D.Double(
					cps.get(0).x, cps.get(0).y, 
					(cps.get(1).x - cps.get(0).x), (cps.get(1).y - cps.get(0).y));
		}
		else if(s instanceof RectangleCubic)
		{
			s = new RectangleCubic(cps.get(0), cps.get(1), cps.get(2), cps.get(3), cps.get(4), cps.get(5), cps.get(6), cps.get(7), cps.get(8), cps.get(9), cps.get(10), cps.get(11));
		}
		else if(s instanceof Ellipse2D)
		{
			s = new Ellipse2D.Double(
					cps.get(0).x, cps.get(0).y, 
					(cps.get(1).x - cps.get(0).x), (cps.get(1).y - cps.get(0).y));
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
	 * 
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
		switch(mode)
		{
		case Text:
			//open dialog.
			JFrame frame = WidgetBuildController.getInstance().getFrame();
			String opt = (String) JOptionPane.showInputDialog(
					frame,
					"Enter Text");
			Font font = new Font(Font.SANS_SERIF, 0, 20);
			shape = new TextShape(opt, curvePoints[0], font, g2d);
			LoggingMessages.printOut(opt + " " + g2d);
			break;
		case Line:
			shape = new Line2D.Double(curvePoints[0], curvePoints[1]);
			break;
		case Curve:
			shape = new CurveShape(curvePoints[0], curvePoints[2], curvePoints[3], curvePoints[1]);
			break;
		case ellipse:
			shape = new Ellipse2D.Double(
					curvePoints[0].x, curvePoints[0].y, 
					(curvePoints[1].x - curvePoints[0].x), (curvePoints[1].y - curvePoints[0].y));
			break;
		case rectangle:
			shape = new Rectangle2D.Double(
					curvePoints[0].x, curvePoints[0].y, 
					(curvePoints[1].x - curvePoints[0].x), (curvePoints[1].y - curvePoints[0].y));
			break;
		case rectangleCubic:
			shape = new RectangleCubic(curvePoints[0], curvePoints[1], curvePoints[2], 
					curvePoints[3], curvePoints[4], curvePoints[5], curvePoints[6], curvePoints[7], curvePoints[8], curvePoints[9], curvePoints[10], curvePoints[11]);
			break;
		case triangle:
			shape = new Triangle(curvePoints[0], curvePoints[1], curvePoints[2]);
			break;
		case triangleCubic:
			shape = new TriangleCubic(curvePoints[0], curvePoints[1], curvePoints[2], 
					curvePoints[3], curvePoints[4], curvePoints[5], curvePoints[6], curvePoints[7], curvePoints[8]);
			break;
		}
		
		return shape;
	}
	
	public enum DrawMode//TODO
	{
		Text(GlyphVector.class.getName(), "Text", TEXT_DIRECTIONS, 1),
		Line(Line2D.class.getName(), "Line", LINE_DIRECTIONS, 2),
		Curve(CurveShape.class.getName(), "Curve", CURVE_DIRECTIONS, 4),
		ellipse(Ellipse2D.class.getName(), "Elipse", ELLIPSE_DIRECTIONS, 2),
		rectangle(Rectangle2D.class.getName(), "Rectangle", RECTANGLE_DIRECTIONS, 2),
		triangle(Triangle.class.getName(), "Triangle", TRIANGLE_DIRECTIONS, 3),
		triangleCubic(TriangleCubic.class.getName(), "Triangle Cubic", TRIANGLE_CUBIC_DIRECTIONS, 9),
		rectangleCubic(RectangleCubic.class.getName(), "Rectangle Cubic", RECTANGLE_CUBIC_DIRECTIONS, 12);
		
		private String className;
		private String modeText;
		private String [] directions;
		private int numberOfPoints;
		
		private DrawMode(String className, String modeText, String [] directions, int numberOfPoints)
		{
			this.className = className;
			this.modeText = modeText;
			this.directions = directions;
			this.numberOfPoints = numberOfPoints;
		}
		
		public static DrawMode getMatchingClassName(String className)
		{
			for(DrawMode dm : DrawMode.values())
			{
				if(dm.getClassName().equals(className))
				{
					return dm;
				}
			}
			return null;
		}
		
		public String getClassName()
		{
			return this.className;
		}
		
		public String getModeText()
		{
			return this.modeText;
		}
		public String [] getDirections()
		{
			return this.directions;
		}
		public int getNumberOfPoints()
		{
			return this.numberOfPoints;
		}
	}
	
}
