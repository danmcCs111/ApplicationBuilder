package ShapeWidgetComponents;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class TriangleCubic implements Shape
{
	Point [] points = new Point [10];
	Rectangle2D bounds;
	
	public TriangleCubic(Point p1, Point p2, Point p3, Point c11, Point c12, Point c21, Point c22, Point c31, Point c32)
	{
		this.points[0] = p1;
		this.points[1] = p2;
		this.points[2] = p3;
		this.points[3] = p1;
		
		this.points[4] = c11;
		this.points[5] = c12;
		
		this.points[6] = c21;
		this.points[7] = c22;
		
		this.points[8] = c31;
		this.points[9] = c32;
		
		constructBounds();
	}
	
	private void constructBounds()
	{
		double 
			leastX = points[0].x,
			leastY = points[0].y,
			mostX = points[0].x,
			mostY = points[0].y;
		for(int i = 1; i < 4; i++)
		{
			Point p = points[i];
			if(leastX > p.x)
				leastX = p.x;
			if(leastY > p.y)
				leastY = p.y;
			if(mostX < p.x)
				mostX =  p.x;
			if(mostY < p.y)
				mostY =  p.y;
		}
		bounds = new Rectangle2D.Double(leastX, leastY, mostX - leastX, mostY - leastY);
	}
	
	public Point getPoint(int index)
	{
		return this.points[index];
	}
	
	@Override
	public Rectangle getBounds() 
	{
		return bounds.getBounds();
	}

	@Override
	public Rectangle2D getBounds2D() 
	{
		return bounds;
	}

	@Override
	public boolean contains(double x, double y) 
	{
		return false;
	}

	@Override
	public boolean contains(Point2D p) 
	{
		return false;
	}

	@Override
	public boolean intersects(double x, double y, double w, double h) 
	{
		return false;
	}

	@Override
	public boolean intersects(Rectangle2D r) 
	{
		return false;
	}

	@Override
	public boolean contains(double x, double y, double w, double h) 
	{
		return false;
	}

	@Override
	public boolean contains(Rectangle2D r) 
	{
		return false;
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at) 
	{
		 return new TriangleCubicIterator(this, at);
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at, double flatness) 
	{
		return new TriangleCubicIterator(this, at);
	}
	
}
