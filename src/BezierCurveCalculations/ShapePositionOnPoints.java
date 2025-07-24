package BezierCurveCalculations;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public interface ShapePositionOnPoints 
{
	public static void drawNumberSequence(
			ArrayList<Point> points, 
			Graphics2D g2d, 
			int pointCollectionIndexSkipCount, 
			int sequenceRangeStart, int sequenceRangeEnd, int startSequenceNum)
	{
		int count = startSequenceNum;
		for(int i = 0; i < points.size(); i+=pointCollectionIndexSkipCount)
		{
			Point p = points.get(i);
			g2d.drawString(count+"", p.x, p.y);
			count++;
			if(count > sequenceRangeEnd)
			{
				count = sequenceRangeStart;
			}
		}
	}
}
