package DrawModes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;

import DrawModesAbstract.DrawMode;
import WidgetComponents.ComboSelectionDialog;
import WidgetExtensions.ComboListDialogSelectedListener;
import WidgetUtility.WidgetBuildController;

public class GeneralPathDrawMode extends DrawMode 
{
	private static String [] TEXT_DIRECTIONS = new String [] {
			"",
			"Enter x, y"
		};

	@Override
	public String[] getDirections() {
		return TEXT_DIRECTIONS;
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

	@Override
	public Shape constructShape(Point[] points, Graphics2D g2d) 
	{
		JFrame frame = WidgetBuildController.getInstance().getFrame();
		ComboSelectionDialog csd = new ComboSelectionDialog();
		List<String> selectables = Arrays.asList(new String[] {"Move To", "Line To", "Quadratic To", "Cubic To"});
		new ComboListDialogSelectedListener() {
			@Override
			public void selectionChosen(List<String> chosenSelection) {
				
			}
		};
		csd.buildAndShow(selectables, "General Path Selection", "General Path Selection", null, frame);
		Shape shape = new GeneralPath();
		return shape;
	}

}
