package DrawModes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.HashMap;

import DrawModes.GeneralPathDrawMode.DrawPaths;
import DrawModesAbstract.DrawMode;
import DrawModesAbstract.DrawModeInstructions;
import Graphics2D.GeneralPathShape;
import Properties.LoggingMessages;

public class PenDrawMode extends DrawMode
{
	@Override
	public String[] getDirections() {
		return DrawModeInstructions.PEN_DIRECTIONS;
	}

	@Override
	public String getClassName() {
		return GeneralPathShape.class.getName();
	}

	@Override
	public String getModeText() {
		return "Pen";
	}

	@Override
	public int getNumberOfPoints() {
		return getDirections().length;
	}

	public Shape constructPenShape(HashMap<Integer, Point> moveTos, Point[] points, Graphics2D g2d) 
	{
		GeneralPathShape gps = new GeneralPathShape();
		if(points.length < 1)
		{
			return gps; 
		}
		ArrayList<DrawPaths> drawPaths = new ArrayList<DrawPaths>();
		for(int i = 0; i < points.length-1; i++)
		{
			if(moveTos.containsKey(i))
			{
				LoggingMessages.printOut("move to: " + i);
				gps.moveTo(points[i].x, points[i].y);
				drawPaths.add(DrawPaths.MoveTo);
			}
			else
			{
				gps.lineTo(points[i].x, points[i].y);
				drawPaths.add(DrawPaths.LineTo);	
			}
			
		}
		gps.setDrawPaths(drawPaths);
		return gps;
	}

	@Override
	public Shape constructShape(Point[] points, Graphics2D g2d) {
		// TODO Auto-generated method stub
		return null;
	}

}
