package DrawModes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Line2D;

import DrawModesAbstract.DrawMode;

public class LineDrawMode extends DrawMode
{
	private static final String []
		LINE_DIRECTIONS = new String [] {
			"",
			"Enter x, y", 
			"Enter x2, y2"
		};
	@Override
	public String[] getDirections() {
		return LINE_DIRECTIONS;
	}

	@Override
	public String getClassName() {
		return Line2D.class.getName();
	}

	@Override
	public String getModeText() {
		return "Line";
	}

	@Override
	public int getNumberOfPoints() {
		return getDirections().length;
	}

	@Override
	public Shape constructShape(Point[] points, Graphics2D g2d) {
		return new Line2D.Double(points[0], points[1]);
	}

}
