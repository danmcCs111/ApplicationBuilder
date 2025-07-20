package ShapeWidgetComponents;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.util.NoSuchElementException;

public class TriangleIterator implements PathIterator
{
	Triangle triangle;
    AffineTransform affine;
    int index;
    
    public TriangleIterator(Triangle triangle, AffineTransform affine)
    {
    	this.triangle = triangle;
    	this.affine = affine;
    }
    
	@Override
	public int getWindingRule() 
	{
		return WIND_NON_ZERO;
	}

	@Override
	public boolean isDone() 
	{
		return (index > 3);
	}

	@Override
	public void next() 
	{
		index++;
	}

	@Override
	public int currentSegment(float[] coords) 
	{
		if (isDone()) 
		{
            throw new NoSuchElementException("line iterator out of bounds");
        }
		coords[0] = (float) triangle.getPoint(index).getX();
		coords[1] = (float) triangle.getPoint(index).getY();
        int type = (index==0)
        		?SEG_MOVETO
        		:SEG_LINETO;
        
        if (affine != null) 
        {
            affine.transform(coords, 0, coords, 0, 1);
        }
        return type;
	}

	@Override
	public int currentSegment(double[] coords) 
	{
		if (isDone()) 
		{
            throw new NoSuchElementException("line iterator out of bounds");
        }
		coords[0] = (double) triangle.getPoint(index).getX();
		coords[1] = (double) triangle.getPoint(index).getY();
        int type = (index==0)
        		?SEG_MOVETO
        		:SEG_LINETO;
        
        if (affine != null) 
        {
            affine.transform(coords, 0, coords, 0, 1);
        }
        return type;
	}
	
}
