package ShapeWidgetComponents;

import java.awt.geom.PathIterator;
import java.util.ArrayList;

public class PathIteratorAccumilated implements PathIterator
{
	ArrayList<PathIterator> pis;
	private int pisIndex=0;
	
	public PathIteratorAccumilated(ArrayList<PathIterator> pis)
	{
		this.pis = pis;
	}
	
	@Override
	public int getWindingRule() 
	{
		return pis.get(pisIndex).getWindingRule();
	}

	@Override
	public boolean isDone() 
	{
		boolean done = pis.get(pisIndex).isDone();
		if(!done)
		{
			return done;
		}
		else if(done && pisIndex+1 < pis.size())
		{
			pisIndex++;
			return false;
		}
		else
		{
			return true;
		}
	}

	@Override
	public void next() 
	{
		pis.get(pisIndex).next();
	}

	@Override
	public int currentSegment(float[] coords) 
	{
		return pis.get(pisIndex).currentSegment(coords);
	}

	@Override
	public int currentSegment(double[] coords) 
	{
		return pis.get(pisIndex).currentSegment(coords);
	}

}
