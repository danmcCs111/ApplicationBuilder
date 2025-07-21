package ShapeWidgetComponents;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class GenericShape implements Shape 
{
	Point [] points;
	Rectangle2D bounds;
	ArrayList<Integer> pathIterators;
	
	public GenericShape(Point [] points, ArrayList<Integer> pathIterators)
	{
		this.points = points;
		this.pathIterators = pathIterators;
	}
	
	public ArrayList<Integer> getPathIterators()
	{
		return this.pathIterators;
	}
	
	public Point getPoint(int index)
	{
		return this.points[index];
	}
	
	public void constructBounds()
	{
		double 
			leastX = points[0].x,
			leastY = points[0].y,
			mostX = points[0].x,
			mostY = points[0].y;
		
		for(int i = 1; i < pathIterators.size(); i++)
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

	@Override
	public Rectangle getBounds() 
	{
		return this.bounds.getBounds();
	}

	@Override
	public Rectangle2D getBounds2D() 
	{
		return this.bounds;
	}

	@Override
	public boolean contains(double x, double y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(Point2D p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean intersects(double x, double y, double w, double h) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean intersects(Rectangle2D r) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(double x, double y, double w, double h) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(Rectangle2D r) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at) 
	{
		return new GenericShapeIterator(this, at);
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at, double flatness) 
	{
		return new GenericShapeIterator(this, at);
	}

}
