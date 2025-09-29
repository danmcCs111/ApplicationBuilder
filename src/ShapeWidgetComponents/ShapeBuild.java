package ShapeWidgetComponents;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Graphics2D.CurveShape;
import Properties.LoggingMessages;
import WidgetComponents.ComboSelectionDialog;
import WidgetExtensions.ComboListDialogSelectedListener;
import WidgetUtility.WidgetBuildController;

public class ShapeBuild 
{
	private boolean drawComplete = false;
	private DrawModeIterator mode;
	private Point [] curvePoints;
	private Graphics2D g2d;
	
	private static final String [] //TODO
			CURVE_DIRECTIONS = new String [] {
				"",
				"Enter x, y", 
				"Enter x2, y2", 
				"Enter Control Point 1", 
				"Enter Control Point 2"
			},
			TEXT_DIRECTIONS = new String [] {
				"",
				"Enter x, y"
			},
			LINE_DIRECTIONS = new String [] {
				"",
				"Enter x, y", 
				"Enter x2, y2"
			},
			ELLIPSE_DIRECTIONS = new String [] {
				"",
				"Enter x, y",
				"Enter x2, y2"
			},
			RECTANGLE_DIRECTIONS = new String [] {
					"",
					"Enter x, y",
					"Enter x2, y2"
			},
			RECTANGLE_CUBIC_DIRECTIONS = new String [] {
					"",
					"Enter x, y",
					"Enter x2, y2",
					"Enter x3, y3",
					"Enter x4, y4",
					
					"Enter control 1, y1",
					"Enter control 1.2, y1.2",
					
					"Enter control 2, y2",
					"Enter control 2.2, y2.2",
					
					"Enter control 3, y3",
					"Enter control 3.2, y3.2",
					
					"Enter control 4, y4",
					"Enter control 4.2, y4.2"
			},
			TRIANGLE_DIRECTIONS = new String [] {
					"",
					"Enter x, y",
					"Enter x2, y2",
					"Enter x3, y3"
			},
			TRIANGLE_CUBIC_DIRECTIONS = new String [] {
					"",
					"Enter x, y",
					"Enter x2, y2",
					"Enter x3, y3",
					
					"Enter control 1, y1",
					"Enter control 1.2, y1.2",
					
					"Enter control 2, y2",
					"Enter control 2.2, y2.2",
					
					"Enter control 3, y3",
					"Enter control 3.2, y3.2"
			};
	
	public ShapeBuild(DrawModeIterator mode, Point [] curvePoints, Graphics2D g2d)
	{
		this.mode = mode;
		this.curvePoints = curvePoints;
		this.g2d = g2d;
	}
	
	public boolean isDrawComplete()
	{
		return drawComplete;
	}
	
	public Shape constructShape()
	{
		return constructShape(null);
	}
	
	private Shape constructShape(Shape shape)
	{
		JFrame frame = WidgetBuildController.getInstance().getFrame();
		switch(mode)
		{
		case Text:
			//open dialog.
			String opt = (String) JOptionPane.showInputDialog (frame, "Enter Text");
			if(opt == null || opt.isBlank()) opt=" ";
			Font font = new Font(Font.SANS_SERIF, 0, 20);
			shape = new TextShape(opt, curvePoints[0], font, g2d);
			LoggingMessages.printOut(opt + " " + g2d);
			drawComplete = true;
			break;
		case GeneralPath:
			GeneralPath gp = constructGeneralPath(frame);
			shape = gp;
			break;
		case Line:
			shape = new Line2D.Double(curvePoints[0], curvePoints[1]);
			drawComplete = true;
			break;
		case Curve:
			shape = new CurveShape(curvePoints[0], curvePoints[2], curvePoints[3], curvePoints[1]);
			drawComplete = true;
			break;
		case ellipse:
			shape = new Ellipse2D.Double(
					curvePoints[0].x, curvePoints[0].y, 
					(curvePoints[1].x - curvePoints[0].x), (curvePoints[1].y - curvePoints[0].y));
			drawComplete = true;
			break;
		case rectangle:
			shape = new Rectangle2D.Double(
					curvePoints[0].x, curvePoints[0].y, 
					(curvePoints[1].x - curvePoints[0].x), (curvePoints[1].y - curvePoints[0].y));
			drawComplete = true;
			break;
		case rectangleCubic:
			shape = new RectangleCubic(curvePoints[0], curvePoints[1], curvePoints[2], 
					curvePoints[3], curvePoints[4], curvePoints[5], curvePoints[6], curvePoints[7], curvePoints[8], curvePoints[9], curvePoints[10], curvePoints[11]);
			drawComplete = true;
			break;
		case triangle:
			shape = new Triangle(curvePoints[0], curvePoints[1], curvePoints[2]);
			drawComplete = true;
			break;
		case triangleCubic:
			shape = new TriangleCubic(curvePoints[0], curvePoints[1], curvePoints[2], 
					curvePoints[3], curvePoints[4], curvePoints[5], curvePoints[6], curvePoints[7], curvePoints[8]);
			drawComplete = true;
			break;
		default:
			break;
		}
		
		return shape;
	}
	
	private GeneralPath constructGeneralPath(JFrame frame)
	{
		ComboSelectionDialog csd = new ComboSelectionDialog();
		List<String> selectables = Arrays.asList(GeneralPathSelections.getDescriptions());
		new ComboListDialogSelectedListener() {
			@Override
			public void selectionChosen(List<String> chosenSelection) 
			{
				for(String sel : chosenSelection)
				{
					GeneralPathSelections.getGeneralPathSelection(sel);
					
				}
			}
		};
		csd.buildAndShow(selectables, "General Path Selection", "General Path Selection", null, frame);
		GeneralPath gp = new GeneralPath();
		
		return gp;
	}
	
	public enum GeneralPathSelections
	{
		MoveTo("Move To"),
		LineTo("Line To"),
		QuadraticTo("Quadratic To"),
		CubicTo("Cubic To"),
		CloseTo("Close To");
		
		private String description;
		
		private GeneralPathSelections(String description)
		{
			this.description = description;
		}
		
		public static GeneralPathSelections getGeneralPathSelection(String description)
		{
			for(GeneralPathSelections gps : values())
			{
				if(description.equals(gps.description))
				{
					return gps;
				}
			}
			return null;
		}
		public static String [] getDescriptions()
		{
			String [] descriptions = new String [values().length];
			int i = 0;
			for(GeneralPathSelections gps : values())
			{
				descriptions[i] = gps.description;
				i++;
			}
			return descriptions;
		}
	}
	
	public enum DrawModeIterator//TODO
	{
		Text(TextShape.class.getName(), "Text", TEXT_DIRECTIONS, 1),
		GeneralPath(java.awt.geom.GeneralPath.class.getName(), "General Path", TEXT_DIRECTIONS, 1),
		Line(Line2D.class.getName(), "Line", LINE_DIRECTIONS, 2),
		Curve(CurveShape.class.getName(), "Curve", CURVE_DIRECTIONS, 4),
		ellipse(Ellipse2D.class.getName(), "Elipse", ELLIPSE_DIRECTIONS, 2),
		rectangle(Rectangle2D.class.getName(), "Rectangle", RECTANGLE_DIRECTIONS, 2),
		triangle(Triangle.class.getName(), "Triangle", TRIANGLE_DIRECTIONS, 3),
		triangleCubic(TriangleCubic.class.getName(), "Triangle Cubic", TRIANGLE_CUBIC_DIRECTIONS, 9),
		rectangleCubic(RectangleCubic.class.getName(), "Rectangle Cubic", RECTANGLE_CUBIC_DIRECTIONS, 12);
		
		private String className;
		private String modeText;
		private String [] directions;
		private DrawModeIterator drawModeGen = null;
		private int numberOfPoints;
		
		private DrawModeIterator(String className, String modeText, String [] directions, int numberOfPoints)
		{
			this.className = className;
			this.modeText = modeText;
			this.directions = directions;
			this.numberOfPoints = numberOfPoints;
		}
		
		public void setGeneralPathDrawMode(DrawModeIterator dm)
		{
			drawModeGen = dm;
		}
		
		public DrawModeIterator getGeneralPathDrawMode()
		{
			return this.drawModeGen;
		}
		
		public static DrawModeIterator getMatchingClassName(String className)
		{
			for(DrawModeIterator dm : DrawModeIterator.values())
			{
				if(dm.getClassName().equals(className))
				{
					return dm;
				}
			}
			return null;
		}
		
		public String getClassName()
		{
			return this.className;
		}
		
		public String getModeText()
		{
			return this.modeText;
		}
		public String [] getDirections()
		{
			return this.directions;
		}
		public int getNumberOfPoints()
		{
			return this.numberOfPoints;
		}
	}
}
