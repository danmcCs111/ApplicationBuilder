package BezierCurveCalculations;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;

public class AffineTransformSampler extends AffineTransform 
{
	private static final long serialVersionUID = 1L;
	
	private double [] points = new double [6];
	private int numOfSteps = 0;
	
	public void resetSteps()
	{
		numOfSteps = 0;
	}
	
	public int getNumberOfSteps()
	{
		return numOfSteps;
	}
	
	public ArrayList<Shape> sampleLineSegments(PathIterator pi, Shape s, double stepInc)
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
				shapes.addAll(collectCubicLineSegmentCollection(points, pointOld, stepInc));
				pointOld = new Point((int)points[4], (int)points[5]);
				numOfSteps++;
				break;
			case PathIterator.SEG_QUADTO:
				shapes.addAll(collectQuadraticLineSegmentCollection(points, pointOld, stepInc));
				pointOld = new Point((int)points[2], (int)points[3]);
				numOfSteps++;
				break;
			case PathIterator.SEG_LINETO:
				shapes.addAll(collectLineLineSegmentCollection(points, pointOld, stepInc));
				pointOld = new Point((int)points[0], (int)points[1]);
				numOfSteps++;
				break;
			}
			
			pi.next();
		}
		return shapes;
	}
	
	public ArrayList<Point> samplePoints(PathIterator pi, Shape s, double stepInc)
	{
		ArrayList<Point> pointsCollect = new ArrayList<Point>();
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
				pointsCollect.addAll(collectCubicPointCollection(points, pointOld, stepInc));
				pointOld = new Point((int)points[4], (int)points[5]);
				numOfSteps++;
				break;
			case PathIterator.SEG_QUADTO:
				pointsCollect.addAll(collectQuadraticPointCollection(points, pointOld, stepInc));
				pointOld = new Point((int)points[2], (int)points[3]);
				numOfSteps++;
				break;
			case PathIterator.SEG_LINETO:
				pointsCollect.addAll(collectLinePointCollection(points, pointOld, stepInc));
				pointOld = new Point((int)points[0], (int)points[1]);
				numOfSteps++;
				break;
			}
			
			pi.next();
		}
		return pointsCollect;
	}
	
	public ArrayList<Shape> collectCubicLineSegmentCollection(double [] points, Point pointOld, double stepInc)
	{
		ArrayList<Shape> shapes = new ArrayList<Shape>();
		Point 
			st = pointOld,
			c1 = new Point((int)points[0], (int)points[1]),
			c2 = new Point((int)points[2], (int)points[3]),
			end = new Point((int)points[4], (int)points[5]);
		for(double i = stepInc; i < 1; i+=stepInc)
		{
			Point ret = BezierCurveSample.getPointAtCubicCurve(st, c1, c2, end, i);
			shapes.add(new Line2D.Double(pointOld, ret));
			pointOld = ret;
		}
		return shapes;
	}
	
	public ArrayList<Shape> collectQuadraticLineSegmentCollection(double [] points, Point pointOld, double stepInc)
	{
		ArrayList<Shape> shapes = new ArrayList<Shape>();
		Point 
			st = pointOld,
			c1 = new Point((int)points[0], (int)points[1]),
			end = new Point((int)points[2], (int)points[3]);
		for(double i = stepInc; i < 1; i+=stepInc)
		{
			Point ret = BezierCurveSample.getPointAtQuadraticCurve(st, c1, end, i);
			shapes.add(new Line2D.Double(pointOld, ret));
			pointOld = ret;
		}
		return shapes;
	}
	
	public ArrayList<Shape> collectLineLineSegmentCollection(double [] points, Point pointOld, double stepInc)
	{
		ArrayList<Shape> shapes = new ArrayList<Shape>();
		Point 
			st = pointOld,
			end = new Point((int)points[0], (int)points[1]);
		for(double i = stepInc; i < 1; i+=stepInc)
		{
			Point ret = BezierCurveSample.getPointAtLine(st, end, i);
			shapes.add(new Line2D.Double(pointOld, ret));
			pointOld = ret;
		}
		return shapes;
	}
	
	public ArrayList<Point> collectCubicPointCollection(double [] points, Point pointOld, double stepInc)
	{
		ArrayList<Point> pointsCollect = new ArrayList<Point>();
		Point 
			st = pointOld,
			c1 = new Point((int)points[0], (int)points[1]),
			c2 = new Point((int)points[2], (int)points[3]),
			end = new Point((int)points[4], (int)points[5]);
		for(double i = stepInc; i < 1; i+=stepInc)
		{
			Point ret = BezierCurveSample.getPointAtCubicCurve(st, c1, c2, end, i);
			pointsCollect.add(ret);
			pointOld = ret;
		}
		return pointsCollect;
	}
	
	public ArrayList<Point> collectQuadraticPointCollection(double [] points, Point pointOld, double stepInc)
	{
		ArrayList<Point> pointsCollect = new ArrayList<Point>();
		Point 
			st = pointOld,
			c1 = new Point((int)points[0], (int)points[1]),
			end = new Point((int)points[2], (int)points[3]);
		for(double i = stepInc; i < 1; i+=stepInc)
		{
			Point ret = BezierCurveSample.getPointAtQuadraticCurve(st, c1, end, i);
			pointsCollect.add(ret);
			pointOld = ret;
		}
		return pointsCollect;
	}
	
	public ArrayList<Point> collectLinePointCollection(double [] points, Point pointOld, double stepInc)
	{
		ArrayList<Point> pointsCollect = new ArrayList<Point>();
		Point 
			st = pointOld,
			end = new Point((int)points[0], (int)points[1]);
		for(double i = stepInc; i < 1; i+=stepInc)
		{
			Point ret = BezierCurveSample.getPointAtLine(st, end, i);
			pointsCollect.add(ret);
			pointOld = ret;
		}
		return pointsCollect;
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
