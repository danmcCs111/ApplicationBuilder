package BezierCurveCalculations;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.util.ArrayList;

import ShapeWidgetComponents.ShapeStyling;

public interface ShapePositionOnPoints 
{
	public static void drawNumberSequence(
			ArrayList<Point> points, 
			Graphics2D g2d, 
			Font myFont,
			ShapeStyling ss,
			int pointCollectionIndexSkipCount, 
			int sequenceRangeStart, int sequenceRangeEnd, int startSequenceNum)
	{
		FontRenderContext frc = g2d.getFontRenderContext();
		g2d.setFont(myFont);
		
		int count = startSequenceNum;
		for(int i = 0; i < points.size(); i+=pointCollectionIndexSkipCount)
		{
			Point p = points.get(i);
			g2d.setColor(ss.getDrawColor());
			GlyphVector gv = g2d.getFont().createGlyphVector(frc, count+"");
			if(ss.isCreateStrokedShape())
			{
				g2d.setStroke(ss.getStroke());
			}
			
			g2d.drawGlyphVector(gv, p.x, p.y);
			count++;
			if(count > sequenceRangeEnd)
			{
				count = sequenceRangeStart;
			}
		}
	}
}
