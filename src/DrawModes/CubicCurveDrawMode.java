package DrawModes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;

import DrawModesAbstract.DrawMode;
import Graphics2D.CurveShape;

public class CubicCurveDrawMode extends DrawMode
{
	private static final String [] CUBIC_CURVE_DIRECTIONS = new String [] {
			"",
			"Enter x, y", 
			"Enter x2, y2", 
			"Enter Control Point 1", 
			"Enter Control Point 2"
		};
	
	@Override
	public String[] getDirections() {
		return CUBIC_CURVE_DIRECTIONS;
	}

	@Override
	public String getClassName() {
		return CurveShape.class.getName();
	}

	@Override
	public String getModeText() {
		return "Curve";
	}

	@Override
	public int getNumberOfPoints() {
		return CUBIC_CURVE_DIRECTIONS.length;
	}

	@Override
	public Shape constructShape(Point [] points, Graphics2D g2d) {
		return new CurveShape(points[0], points[2], points[3], points[1]);
	}
	
}
