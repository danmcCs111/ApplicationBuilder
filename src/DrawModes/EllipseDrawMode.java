package DrawModes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import DrawModesAbstract.DrawMode;
import DrawModesAbstract.DrawModeInstructions;

public class EllipseDrawMode extends DrawMode
{
	@Override
	public String[] getDirections() {
		return DrawModeInstructions.ELLIPSE_DIRECTIONS;
	}

	@Override
	public String getClassName() {
		return Ellipse2D.class.getName();
	}

	@Override
	public String getModeText() {
		return "Elipse";
	}

	@Override
	public int getNumberOfPoints() {
		return getDirections().length;
	}

	@Override
	public Shape constructShape(Point[] points, Graphics2D g2d) {
		return new Ellipse2D.Double(
				points[0].x, points[0].y, 
				(points[1].x - points[0].x), (points[1].y - points[0].y));
	}

}
