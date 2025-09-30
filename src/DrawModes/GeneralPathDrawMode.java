package DrawModes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;

import DrawModesAbstract.DrawMode;
import DrawModesAbstract.DrawModeInstructions;
import Properties.LoggingMessages;
import ShapeWidgetComponents.GeneralPathShape;
import WidgetComponents.ComboSelectionDialog;
import WidgetExtensions.ComboListDialogSelectedListener;
import WidgetUtility.WidgetBuildController;

public class GeneralPathDrawMode extends DrawMode 
{
	private ArrayList<String> directions = new ArrayList<String>();
	GeneralPathShape gp = new GeneralPathShape();
	
	public GeneralPathDrawMode()
	{
	}
	
	@Override
	public String[] getDirections() {
		return directions.toArray(new String[] {});
	}

	@Override
	public String getClassName() {
		return java.awt.geom.GeneralPath.class.getName();
	}

	@Override
	public String getModeText() {
		return "General Path";
	}

	@Override
	public int getNumberOfPoints() {
		return getDirections().length;
	}
	
	public void constructInstructions(GeneralPathShape gps)
	{
		JFrame frame = WidgetBuildController.getInstance().getFrame();
		ComboSelectionDialog csd = new ComboSelectionDialog();
		List<String> selectables = Arrays.asList(DrawPaths.getDescriptions());
		ComboListDialogSelectedListener cldsl = new ComboListDialogSelectedListener() {
			@Override
			public void selectionChosen(List<String> chosenSelection) 
			{
				if(chosenSelection != null && !chosenSelection.isEmpty())
				{
					for(String sel : chosenSelection)
					{
						LoggingMessages.printOut(sel);
						DrawPaths dp = DrawPaths.getDrawPath(sel);
						addDrawPath(gps, dp);
					}
					constructInstructions(gps);
				}
				else
				{
					addDrawPath(gps, DrawPaths.CloseTo);
				}
			}
		};
		csd.buildAndShow(selectables, "General Path Selection", "General Path Selection", cldsl, frame);
	}
	
	public static GeneralPathShape recalculateShape(Shape s, ArrayList<Point> cps)
	{
		GeneralPathShape gp = (GeneralPathShape) s;
		return constructGeneralPathShape(gp, cps.toArray(new Point[] {}));
	}
	
	private void addDrawPath(GeneralPathShape gps, DrawPaths dp)
	{
		gps.getDrawPaths().add(dp);
		if(dp.getDirections() != null)
		{
			for(String dir : dp.getDirections())
			{
				directions.add(dir);
			}
		}
	}
	
	private static GeneralPathShape constructGeneralPathShape(GeneralPathShape gps, Point[] points)
	{
		GeneralPathShape gp = new GeneralPathShape();
		int i = 0;
		for(DrawPaths dp : gps.getDrawPaths())
		{
			switch(dp)
			{
			case LineTo:
				gp.lineTo(points[i].x, points[i].y);
				i+=1;
				break;
			case CubicTo:
				gp.curveTo(points[i+1].x, points[i+1].y, 
						points[i+2].x, points[i+2].y, 
						points[i].x, points[i].y);
				i+=3;
				break;
			case MoveTo:
				gp.moveTo(points[i].x, points[i].y);
				i+=1;
				break;
			case QuadraticTo:
				gp.quadTo(points[i+1].x, points[i+1].y, 
						points[i].x, points[i].y);
				i+=2;
				break;
			case CloseTo:
				gp.closePath();
				break;
			default:
				break;
			}
		}
		gp.setDrawPaths(gps.getDrawPaths());
		return gp;
	}

	@Override
	public Shape constructShape(Point[] points, Graphics2D g2d) 
	{
		LoggingMessages.printOut(directions.size() + " size of generalPath");
		if(directions.size() == 0)
		{
			directions = new ArrayList<String>();
			addDrawPath(gp, DrawPaths.MoveTo);
			constructInstructions(gp);
		}
		else
		{
			gp = constructGeneralPathShape(gp, points);
		}
		return gp;
	}
	
	public enum DrawPaths
	{
		MoveTo("Move To", DrawModeInstructions.SINGLE_POINT_DIRECTIONS),
		CloseTo("Close To", null),
		LineTo("Line To", Arrays.copyOfRange(DrawModeInstructions.LINE_DIRECTIONS, 
				1, DrawModeInstructions.LINE_DIRECTIONS.length)),
		QuadraticTo("Quadratic To", Arrays.copyOfRange(DrawModeInstructions.QUADRATIC_CURVE_DIRECTIONS, 
				1, DrawModeInstructions.QUADRATIC_CURVE_DIRECTIONS.length)),
		CubicTo("Cubic To", Arrays.copyOfRange(DrawModeInstructions.CUBIC_CURVE_DIRECTIONS,
				1,DrawModeInstructions.CUBIC_CURVE_DIRECTIONS.length));
		
		private String description;
		private String [] directions;
		
		private DrawPaths(String description, String [] directions)
		{
			this.description = description;
			this.directions = directions;
		}
		
		public String getDescription()
		{
			return this.description;
		}
		
		public String [] getDirections()
		{
			return this.directions;
		}
		
		public static DrawPaths getDrawPath(String description)
		{
			for(DrawPaths dp : values())
			{
				if(dp.getDescription().equals(description))
				{
					return dp;
				}
			}
			return null;
		}
		
		public static String [] getDescriptions()
		{
			String [] descriptions = new String [values().length];
			int i = 0;
			for(DrawPaths dp : values())
			{
				descriptions[i] = dp.getDescription();
				i++;
			}
			return descriptions;
		}
	}

}
