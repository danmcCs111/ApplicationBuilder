package DrawModes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;

import DrawModesAbstract.DrawMode;
import ShapeWidgetComponents.RectangleCubic;

public class RectangleCubicDrawMode extends DrawMode
{
	private static final String [] RECTANGLE_CUBIC_DIRECTIONS = new String [] {
			"",
			"Enter x, y",
			"Enter x2, y2",
			"Enter x3, y3",
			"Enter x4, y4",
			
			"Enter control 1, y1",
			"Enter control 1.2, y1.2",
			
			"Enter control 2, y2",
			"Enter control 2.2, y2.2",
			
			"Enter control 3, y3",
			"Enter control 3.2, y3.2",
			
			"Enter control 4, y4",
			"Enter control 4.2, y4.2"
	};
	
	@Override
	public String[] getDirections() 
	{
		return RECTANGLE_CUBIC_DIRECTIONS;
	}

	@Override
	public String getClassName() 
	{
		return RectangleCubic.class.getName();
	}

	@Override
	public String getModeText() 
	{
		return "Rectangle Cubic";
	}

	@Override
	public int getNumberOfPoints() 
	{
		return RECTANGLE_CUBIC_DIRECTIONS.length;
	}

	@Override
	public Shape constructShape(Point[] points, Graphics2D g2d) {
		return new RectangleCubic(points[0], points[1], points[2], 
				points[3], points[4], points[5], points[6], points[7], points[8], points[9], points[10], points[11]);
	}

}
