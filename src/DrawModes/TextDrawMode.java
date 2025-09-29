package DrawModes;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import DrawModesAbstract.DrawMode;
import DrawModesAbstract.DrawModeInstructions;
import Properties.LoggingMessages;
import ShapeWidgetComponents.TextShape;
import WidgetUtility.WidgetBuildController;

public class TextDrawMode extends DrawMode 
{
	@Override
	public String[] getDirections() {
		return DrawModeInstructions.TEXT_DIRECTIONS;
	}

	@Override
	public String getClassName() {
		return TextShape.class.getName();
	}

	@Override
	public String getModeText() {
		return "Text";
	}

	@Override
	public int getNumberOfPoints() {
		return getDirections().length;
	}

	@Override
	public Shape constructShape(Point[] points, Graphics2D g2d) {
		//open dialog.
		JFrame frame = WidgetBuildController.getInstance().getFrame();
		Shape shape = null;
		String opt = (String) JOptionPane.showInputDialog (frame, "Enter Text");
		if(opt == null || opt.isBlank()) opt=" ";
		Font font = new Font(Font.SANS_SERIF, 0, 20);
		shape = new TextShape(opt, points[0], font, g2d);
		LoggingMessages.printOut(opt + " " + g2d);
		return shape;
	}

}
