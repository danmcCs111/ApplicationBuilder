package BezierCurveCalculations;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.util.ArrayList;

import Properties.LoggingMessages;

public interface ShapePositionOnPoints 
{
	public static void drawNumberSequence(
			ArrayList<Point> points, 
			Graphics2D g2d, 
			Font myFont,
			Color fillColor,
			double pointCollectionIndexSkipCount, 
			int sequenceRangeStart, int sequenceRangeEnd, int startSequenceNum)
	{
		FontRenderContext frc = g2d.getFontRenderContext();
		g2d.setFont(myFont);
		
		int count = startSequenceNum;
		g2d.setColor(fillColor);
//		LoggingMessages.printOut(pointCollectionIndexSkipCount + " skips");
		for(int i = 0; i < points.size(); i+=pointCollectionIndexSkipCount)
		{
			Point p = points.get(i);
			String numDisplay = count + "";
//			fontPixelSize(g2d, myFont, numDisplay);
			GlyphVector gv = g2d.getFont().createGlyphVector(frc, numDisplay);
			
			g2d.drawGlyphVector(gv, p.x, p.y);
			count++;
			if(count > sequenceRangeEnd)
			{
				count = sequenceRangeStart;
			}
		}
	}
	
	public static int fontPixelSize(Graphics2D g2d, Font font, String test)
	{
		int width = g2d.getFontMetrics(font).stringWidth(test); 
//		LoggingMessages.printOut(test + " width: " + width);
		return width;
	}
}
