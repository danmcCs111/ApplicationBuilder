package DrawModes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import DrawModesAbstract.DrawMode;

public class RectangleDrawMode extends DrawMode
{
	private static final String [] RECTANGLE_DIRECTIONS = new String [] {
			"",
			"Enter x, y",
			"Enter x2, y2"
	};
	
	@Override
	public String[] getDirections() {
		return RECTANGLE_DIRECTIONS;
	}

	@Override
	public String getClassName() {
		return Rectangle2D.class.getName();
	}

	@Override
	public String getModeText() {
		return "Rectangle";
	}

	@Override
	public int getNumberOfPoints() {
		return RECTANGLE_DIRECTIONS.length;
	}

	@Override
	public Shape constructShape(Point[] points, Graphics2D g2d) {
		return new Rectangle2D.Double(
				points[0].x, points[0].y, 
				(points[1].x - points[0].x), (points[1].y - points[0].y));
	}

}
