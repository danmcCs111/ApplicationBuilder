package BezierCurveCalculations;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;

import Properties.LoggingMessages;

public class AffineTransformSampler extends AffineTransform 
{
	private static final long serialVersionUID = 1L;
	
	private double [] points = new double [6];
	
	public ArrayList<Shape> sample(PathIterator pi, Shape s)
	{
		ArrayList<Shape> shapes = new ArrayList<Shape>();
		Point pointOld = null;
		
		while(!pi.isDone())
		{
			int retMode = pi.currentSegment(new double[6]);
			
			if(pointOld == null && retMode == PathIterator.SEG_MOVETO)
			{
				pointOld = new Point((int)points[0], (int)points[1]);
			}
			switch(retMode)
			{
			case PathIterator.SEG_CUBICTO:
				shapes.addAll(collectCubicPoint(points, pointOld));
				pointOld = new Point((int)points[4], (int)points[5]);
				break;
			case PathIterator.SEG_QUADTO:
				shapes.addAll(collectQuadraticPoint(points, pointOld));
				pointOld = new Point((int)points[2], (int)points[3]);
				break;
			case PathIterator.SEG_LINETO:
				shapes.addAll(collectLinePoint(points, pointOld));
				pointOld = new Point((int)points[0], (int)points[1]);
				break;
			}
			
			pi.next();
		}
		return shapes;
	}
	
	public ArrayList<Shape> collectCubicPoint(double [] points, Point pointOld)
	{
		ArrayList<Shape> shapes = new ArrayList<Shape>();
		Point 
			st = pointOld,
			c1 = new Point((int)points[0], (int)points[1]),
			c2 = new Point((int)points[2], (int)points[3]),
			end = new Point((int)points[4], (int)points[5]);
		for(double i = 0.1; i < 1; i+=.01)
		{
			Point ret = BezierCurveSample.getPointAtCubicCurve(st, c1, c2, end, i);
			LoggingMessages.printOut(ret.toString());
			shapes.add(new Line2D.Double(pointOld, ret));
			pointOld = ret;
		}
		return shapes;
	}
	
	public ArrayList<Shape> collectQuadraticPoint(double [] points, Point pointOld)
	{
		ArrayList<Shape> shapes = new ArrayList<Shape>();
		Point 
			st = pointOld,
			c1 = new Point((int)points[0], (int)points[1]),
			end = new Point((int)points[2], (int)points[3]);
		for(double i = 0.1; i < 1; i+=.01)
		{
			Point ret = BezierCurveSample.getPointAtQuadraticCurve(st, c1, end, i);
			shapes.add(new Line2D.Double(pointOld, ret));
			pointOld = ret;
		}
		return shapes;
	}
	
	public ArrayList<Shape> collectLinePoint(double [] points, Point pointOld)
	{
		ArrayList<Shape> shapes = new ArrayList<Shape>();
		Point 
			st = pointOld,
			end = new Point((int)points[0], (int)points[1]);
		for(double i = 0.1; i < 1; i+=.01)
		{
			Point ret = BezierCurveSample.getPointAtLine(st, end, i);
			shapes.add(new Line2D.Double(pointOld, ret));
			pointOld = ret;
		}
		return shapes;
	}
	
	@Override
	public void transform(double[] srcPts, int srcOff,
            double[] dstPts, int dstOff,
            int numPts)
	{
		super.transform(srcPts, srcOff, dstPts, dstOff, numPts);
		points = dstPts; 
	}

}
