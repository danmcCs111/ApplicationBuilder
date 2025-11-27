package DrawModes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;

import DrawModesAbstract.DrawMode;
import DrawModesAbstract.DrawModeInstructions;
import Graphics2D.Triangle;
import Graphics2D.TriangleCubic;

public class TriangleCubicDrawMode extends DrawMode
{
	@Override
	public String[] getDirections() {
		return DrawModeInstructions.TRIANGLE_CUBIC_DIRECTIONS;
	}

	@Override
	public String getClassName() {
		return TriangleCubic.class.getName();
	}

	@Override
	public String getModeText() {
		return "Triangle Cubic";
	}

	@Override
	public int getNumberOfPoints() {
		return getDirections().length;
	}

	@Override
	public Shape constructShape(Point[] points, Graphics2D g2d) {
		return new Triangle(points[0], points[1], points[2]);
	}
	
}
