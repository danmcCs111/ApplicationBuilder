package DrawModes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;

import DrawModesAbstract.DrawMode;
import DrawModesAbstract.DrawModeInstructions;
import ShapeWidgetComponents.RectangleCubic;

public class RectangleCubicDrawMode extends DrawMode
{
	@Override
	public String[] getDirections() 
	{
		return DrawModeInstructions.RECTANGLE_CUBIC_DIRECTIONS;
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
		return getDirections().length;
	}

	@Override
	public Shape constructShape(Point[] points, Graphics2D g2d) {
		return new RectangleCubic(points[0], points[1], points[2], 
				points[3], points[4], points[5], points[6], points[7], points[8], points[9], points[10], points[11]);
	}

}
