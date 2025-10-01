package DrawModes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.util.ArrayList;

import DrawModes.GeneralPathDrawMode.DrawPaths;
import DrawModesAbstract.DrawMode;
import DrawModesAbstract.DrawModeInstructions;
import ShapeWidgetComponents.GeneralPathShape;

public class PenDrawMode extends DrawMode
{
	@Override
	public String[] getDirections() {
		return DrawModeInstructions.PEN_DIRECTIONS;
	}

	@Override
	public String getClassName() {
		return GeneralPathShape.class.getName();
	}

	@Override
	public String getModeText() {
		return "Pen";
	}

	@Override
	public int getNumberOfPoints() {
		return getDirections().length;
	}

	@Override
	public Shape constructShape(Point[] points, Graphics2D g2d) 
	{
		GeneralPathShape gps = new GeneralPathShape();
		if(points.length < 2)
		{
			return gps; 
		}
		ArrayList<DrawPaths> drawPaths = new ArrayList<DrawPaths>();
		gps.moveTo(points[0].x, points[0].y);
		drawPaths.add(DrawPaths.MoveTo);
		for(int i = 1; i < points.length-1; i++)
		{
			gps.lineTo(points[i].x, points[i].y);
			drawPaths.add(DrawPaths.LineTo);
		}
		gps.setDrawPaths(drawPaths);
		return gps;
	}

}
