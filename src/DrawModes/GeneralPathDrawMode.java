package DrawModes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;

import DrawModesAbstract.DrawMode;
import DrawModesAbstract.DrawModeInstructions;
import WidgetComponents.ComboSelectionDialog;
import WidgetExtensions.ComboListDialogSelectedListener;
import WidgetUtility.WidgetBuildController;

public class GeneralPathDrawMode extends DrawMode 
{
	private ArrayList<DrawPaths> drawPaths = new ArrayList<DrawPaths>();
	private ArrayList<String> directions = new ArrayList<String>();
	
	public GeneralPathDrawMode()
	{
		directions.addAll(Arrays.asList(DrawModeInstructions.SINGLE_POINT_DIRECTIONS));
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
	
	public void constructInstructions()
	{
		JFrame frame = WidgetBuildController.getInstance().getFrame();
		ComboSelectionDialog csd = new ComboSelectionDialog();
		List<String> selectables = Arrays.asList(DrawPaths.getDescriptions());
		ComboListDialogSelectedListener cldsl = new ComboListDialogSelectedListener() {
			@Override
			public void selectionChosen(List<String> chosenSelection) {
				for(String sel : chosenSelection)
				{
					DrawPaths dp = DrawPaths.getDrawPath(sel);
					drawPaths.add(dp);
					directions.addAll(Arrays.asList(dp.getDirections()));
				}
			}
		};
		csd.buildAndShow(selectables, "General Path Selection", "General Path Selection", cldsl, frame);
	}

	@Override
	public Shape constructShape(Point[] points, Graphics2D g2d) 
	{
		GeneralPath gp = new GeneralPath();
		constructInstructions();
		return gp;
	}
	
	public enum DrawPaths
	{
		
		MoveTo("Move To", DrawModeInstructions.SINGLE_POINT_DIRECTIONS),
		LineTo("Line To", DrawModeInstructions.LINE_DIRECTIONS),
		QuadraticTo("Quadratic To", DrawModeInstructions.QUADRATIC_CURVE_DIRECTIONS),
		CubicTo("Cubic To", DrawModeInstructions.CUBIC_CURVE_DIRECTIONS),
		CloseTo("Close To", DrawModeInstructions.SINGLE_POINT_DIRECTIONS);
		
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
