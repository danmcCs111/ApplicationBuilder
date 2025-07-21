package ShapeWidgetComponents;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class GenericShapeIterator implements PathIterator 
{
	GenericShape genericShape;
    AffineTransform affine;
    int index;
    int numberOfSteps;
    
    public GenericShapeIterator(GenericShape genericShape, AffineTransform affine)
    {
    	this.genericShape = genericShape;
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
		return index > numberOfSteps;
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
		
		ArrayList<Integer> pathIterators = genericShape.getPathIterators();
		
        int type = pathIterators.get(index);
        
        if(type == SEG_MOVETO || type == SEG_LINETO)
        {
        	coords[0] = (float) genericShape.getPoint(index).getX();
    		coords[1] = (float) genericShape.getPoint(index).getY();
        }
        else if(type == SEG_CUBICTO)
        {
        	int indexAdjust = numberOfSteps + (index > 1 ? ((index - 1) * 2):0);
        	coords[0] = (float) genericShape.getPoint(indexAdjust).getX();
    		coords[1] = (float) genericShape.getPoint(indexAdjust).getY();
    		coords[2] = (float) genericShape.getPoint(indexAdjust+1).getX();
    		coords[3] = (float) genericShape.getPoint(indexAdjust+1).getY();
    		coords[4] = (float) genericShape.getPoint(index).getX();
    		coords[5] = (float) genericShape.getPoint(index).getY();
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
        
        if(type == SEG_MOVETO || type == SEG_LINETO)
        {
        	coords[0] = (double) genericShape.getPoint(index).getX();
    		coords[1] = (double) genericShape.getPoint(index).getY();
        }
        else if(type == SEG_CUBICTO)
        {
        	int indexAdjust = numberOfSteps + (index > 1 ? ((index - 1) * 2):0);
        	coords[0] = (double) genericShape.getPoint(indexAdjust).getX();
    		coords[1] = (double) genericShape.getPoint(indexAdjust).getY();
    		coords[2] = (double) genericShape.getPoint(indexAdjust+1).getX();
    		coords[3] = (double) genericShape.getPoint(indexAdjust+1).getY();
    		coords[4] = (double) genericShape.getPoint(index).getX();
    		coords[5] = (double) genericShape.getPoint(index).getY();
        }
        if (affine != null) 
        {
            affine.transform(coords, 0, coords, 0, 1);
        }
        return type;
	}

}
