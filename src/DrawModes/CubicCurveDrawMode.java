package DrawModes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;

import DrawModesAbstract.DrawMode;
import DrawModesAbstract.DrawModeInstructions;
import Graphics2D.CurveShape;

public class CubicCurveDrawMode extends DrawMode
{
	@Override
	public String[] getDirections() {
		return DrawModeInstructions.CUBIC_CURVE_DIRECTIONS;
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
		return getDirections().length;
	}

	@Override
	public Shape constructShape(Point [] points, Graphics2D g2d) {
		return new CurveShape(points[0], points[2], points[3], points[1]);
	}
	
}
