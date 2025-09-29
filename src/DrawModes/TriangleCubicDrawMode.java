package DrawModes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;

import DrawModesAbstract.DrawMode;
import ShapeWidgetComponents.Triangle;
import ShapeWidgetComponents.TriangleCubic;

public class TriangleCubicDrawMode extends DrawMode
{
	private static final String [] TRIANGLE_CUBIC_DIRECTIONS = new String [] {
			"",
			"Enter x, y",
			"Enter x2, y2",
			"Enter x3, y3",
			
			"Enter control 1, y1",
			"Enter control 1.2, y1.2",
			
			"Enter control 2, y2",
			"Enter control 2.2, y2.2",
			
			"Enter control 3, y3",
			"Enter control 3.2, y3.2"
	};
	
	@Override
	public String[] getDirections() {
		return TRIANGLE_CUBIC_DIRECTIONS;
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
		return TRIANGLE_CUBIC_DIRECTIONS.length;
	}

	@Override
	public Shape constructShape(Point[] points, Graphics2D g2d) {
		return new Triangle(points[0], points[1], points[2]);
	}
	
}
