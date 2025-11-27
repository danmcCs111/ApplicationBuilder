package Graphics2D;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.util.NoSuchElementException;

public class RectangleCubicIterator implements PathIterator
{
	RectangleCubic rectangleCubic;
    AffineTransform affine;
    int index;
    static final int NUM_OF_STEPS = 5;
    
    public RectangleCubicIterator(RectangleCubic rectangleCubic, AffineTransform affine)
    {
    	this.rectangleCubic = rectangleCubic;
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
		return (index >= NUM_OF_STEPS);
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
		
        int type = (index==0)
        		?SEG_MOVETO
        		:SEG_CUBICTO;
        
        if(type == SEG_MOVETO)
        {
        	coords[0] = (float) rectangleCubic.getPoint(index).getX();
    		coords[1] = (float) rectangleCubic.getPoint(index).getY();
        }
        else
        {
        	int indexAdjust = NUM_OF_STEPS + (index > 1 ? ((index - 1) * 2):0);
        	coords[0] = (float) rectangleCubic.getPoint(indexAdjust).getX();
    		coords[1] = (float) rectangleCubic.getPoint(indexAdjust).getY();
    		coords[2] = (float) rectangleCubic.getPoint(indexAdjust+1).getX();
    		coords[3] = (float) rectangleCubic.getPoint(indexAdjust+1).getY();
    		coords[4] = (float) rectangleCubic.getPoint(index).getX();
    		coords[5] = (float) rectangleCubic.getPoint(index).getY();
        }
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
		
        int type = (index==0)
        		?SEG_MOVETO
        		:SEG_CUBICTO;
        
        if(type == SEG_MOVETO)
        {
        	coords[0] = (double) rectangleCubic.getPoint(index).getX();
    		coords[1] = (double) rectangleCubic.getPoint(index).getY();
        }
        else
        {
        	int indexAdjust = NUM_OF_STEPS + (index > 1 ? ((index - 1) * 2):0);
        	coords[0] = (double) rectangleCubic.getPoint(indexAdjust).getX();
    		coords[1] = (double) rectangleCubic.getPoint(indexAdjust).getY();
    		coords[2] = (double) rectangleCubic.getPoint(indexAdjust+1).getX();
    		coords[3] = (double) rectangleCubic.getPoint(indexAdjust+1).getY();
    		coords[4] = (double) rectangleCubic.getPoint(index).getX();
    		coords[5] = (double) rectangleCubic.getPoint(index).getY();
        }
        if (affine != null) 
        {
            affine.transform(coords, 0, coords, 0, 1);
        }
        return type;
	}
}
