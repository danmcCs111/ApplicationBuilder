package ShapeWidgetComponents;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.util.NoSuchElementException;

public class TriangleCubicIterator implements PathIterator
{
	TriangleCubic triangleCubic;
    AffineTransform affine;
    int index;
    static final int NUM_OF_STEPS = 4;
    
    public TriangleCubicIterator(TriangleCubic triangleCubic, AffineTransform affine)
    {
    	this.triangleCubic = triangleCubic;
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
        	coords[0] = (float) triangleCubic.getPoint(index).getX();
    		coords[1] = (float) triangleCubic.getPoint(index).getY();
        }
        else
        {
        	int indexAdjust = NUM_OF_STEPS + (index > 1 ? ((index - 1) * 2):0);
        	coords[0] = (float) triangleCubic.getPoint(indexAdjust).getX();
    		coords[1] = (float) triangleCubic.getPoint(indexAdjust).getY();
    		coords[2] = (float) triangleCubic.getPoint(indexAdjust+1).getX();
    		coords[3] = (float) triangleCubic.getPoint(indexAdjust+1).getY();
    		coords[4] = (float) triangleCubic.getPoint(index).getX();
    		coords[5] = (float) triangleCubic.getPoint(index).getY();
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
        	coords[0] = (double) triangleCubic.getPoint(index).getX();
    		coords[1] = (double) triangleCubic.getPoint(index).getY();
        }
        else
        {
        	int indexAdjust = NUM_OF_STEPS + (index > 1 ? ((index - 1) * 2):0);
        	coords[0] = (double) triangleCubic.getPoint(indexAdjust).getX();
    		coords[1] = (double) triangleCubic.getPoint(indexAdjust).getY();
    		coords[2] = (double) triangleCubic.getPoint(indexAdjust+1).getX();
    		coords[3] = (double) triangleCubic.getPoint(indexAdjust+1).getY();
    		coords[4] = (double) triangleCubic.getPoint(index).getX();
    		coords[5] = (double) triangleCubic.getPoint(index).getY();
        }
        if (affine != null) 
        {
            affine.transform(coords, 0, coords, 0, 1);
        }
        return type;
	}
}
