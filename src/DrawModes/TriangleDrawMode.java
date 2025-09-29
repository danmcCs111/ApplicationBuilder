package DrawModes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;

import DrawModesAbstract.DrawMode;
import DrawModesAbstract.DrawModeInstructions;
import ShapeWidgetComponents.Triangle;

public class TriangleDrawMode extends DrawMode 
{
	@Override
	public String[] getDirections() {
		return DrawModeInstructions.TRIANGLE_DIRECTIONS;
	}

	@Override
	public String getClassName() {
		return Triangle.class.getName();
	}

	@Override
	public String getModeText() {
		return "Triangle";
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
