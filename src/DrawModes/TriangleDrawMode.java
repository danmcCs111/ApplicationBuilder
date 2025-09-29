package DrawModes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;

import DrawModesAbstract.DrawMode;
import ShapeWidgetComponents.Triangle;

public class TriangleDrawMode extends DrawMode 
{
	private static final String [] TRIANGLE_DIRECTIONS = new String [] {
			"",
			"Enter x, y",
			"Enter x2, y2",
			"Enter x3, y3"
	};
	
	@Override
	public String[] getDirections() {
		return TRIANGLE_DIRECTIONS;
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
		return TRIANGLE_DIRECTIONS.length;
	}

	@Override
	public Shape constructShape(Point[] points, Graphics2D g2d) {
		return new Triangle(points[0], points[1], points[2]);
	}

}
