package Graphics2D;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Triangle implements Shape
{
	Point [] points = new Point [3];
	Rectangle2D bounds;
	
	public Triangle()
	{
		
	}
	
	public Triangle(Point p1, Point p2, Point p3)
	{
		this.points[0] = p1;
		this.points[1] = p2;
		this.points[2] = p3;
		
		constructBounds();
	}
	
	private void constructBounds()
	{
		double 
			leastX = points[0].x,
			leastY = points[0].y,
			mostX = points[0].x,
			mostY = points[0].y;
		for(Point p : points)
		{
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
		 return new TriangleIterator(this, at);
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at, double flatness) 
	{
		return new TriangleIterator(this, at);
	}

}
