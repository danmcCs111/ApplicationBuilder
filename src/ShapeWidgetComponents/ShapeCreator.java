package ShapeWidgetComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import Editors.ColorEditor;
import Graphics2D.CurveShape;
import ShapeEditorListeners.ClearActionListener;
import ShapeEditorListeners.ControlPointChangedListener;
import ShapeEditorListeners.DrawActionListener;
import ShapeEditorListeners.DrawInputActionListener;
import ShapeEditorListeners.DrawMouseListener;
import ShapeEditorListeners.ShapeDrawModeActionListener;
import ShapeEditorListeners.ShapeStylingActionListener;

public class ShapeCreator extends JPanel implements ShapeStylingActionListener
{
	private static final long serialVersionUID = 3005L;
	
	private static final String [] 
		CURVE_DIRECTIONS = new String [] {
			"",
			"Enter x, y", 
			"Enter x2, y2", 
			"Enter Control Point 1", 
			"Enter Control Point 2"
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
		RECTANGLE_DIRECTIONS= new String [] {
				"",
				"Enter x, y",
				"Enter x2, y2"
		};
	
	public static Dimension CONTROL_POINT_PIXEL_SIZE = new Dimension(6,6);
	
	private int 
		directionsIndex = 0, 
		controlPointSelectedIndex = -1,
		controlPointShapeSelectedIndex = -1,
		numShapes = 0;
	private ArrayList<Integer> shapeSelectedIndexes = new ArrayList<Integer>();
	private HashMap<Integer, HashMap<Integer, ArrayList<ControlPointChangedListener>>> shapeAndControlPointChangedListener = 
			new HashMap<Integer, HashMap<Integer, ArrayList<ControlPointChangedListener>>>();
	private boolean 
		mousePressed = false,
		controlPointSelected = false;
	private Point 
		mouseDragStartPoint,
		mouseDragLastPoint;
	private Point [] controlPoints;
	private ArrayList<ArrayList<Point>> 
		listControlPointsScaled = new ArrayList<ArrayList<Point>>();
	private ArrayList<Shape> 
		shapesScaled = new ArrayList<Shape>();
	private Rectangle2D selectTool;
	private Rectangle2D selectionRect;
	
	private JLabel 
		directionsLabel,
		operationLabel;
	private JButton addShape;
	private ColorEditor colorEditorTop;
	private JPanel 
		top, 
		draw,
		east;
	private ShapeCreatorEditPanel shapeCreatorEditPanel;
	private HashMap<Integer, ShapeStyling> shapeStyling = new HashMap<Integer, ShapeStyling>();
	
	private JComboBox<DrawMode> modeSelections;
	private Operation operation = Operation.Select;
	
	public ShapeCreator()
	{
		this.setLayout(new BorderLayout());
		
		top = new JPanel();
		draw = new JPanel();
		east = new JPanel();
		east.setLayout(new BorderLayout());
		Border border = BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.gray, Color.gray);
		border = BorderFactory.createTitledBorder(border, "Edit");
		east.setBorder(border);
		shapeCreatorEditPanel = new ShapeCreatorEditPanel(this);
		east.add(shapeCreatorEditPanel, BorderLayout.NORTH);
		
		directionsLabel = new JLabel();
		operationLabel = new JLabel(getOperation().getTitleText());
		JButton b = new JButton("draw");
		JButton c = new JButton("clear");
		addShape = new JButton("+ Add");
		modeSelections = new JComboBox<ShapeCreator.DrawMode>(DrawMode.values());
		modeSelections.addActionListener(new ShapeDrawModeActionListener(this));
		setMode(DrawMode.Line);//default.
		colorEditorTop = new ColorEditor();
		colorEditorTop.setComponentValue(Color.black);//TODO
		
		c.addActionListener(new ClearActionListener(this));
		addShape.addActionListener(new DrawInputActionListener(this));
		b.addActionListener(new DrawActionListener(this));
		draw.addMouseListener(new DrawMouseListener(this));
		draw.addMouseMotionListener(new DrawMouseListener(this));
		
		top.add(directionsLabel);
		top.add(b);
		top.add(c);
		top.add(operationLabel);
		top.add(addShape);
		top.add(modeSelections);
		top.add(colorEditorTop);
		this.add(top, BorderLayout.NORTH);
		this.add(east, BorderLayout.EAST);
		this.add(draw, BorderLayout.CENTER);
	}
	
	public Color getColorPallette()
	{
		return (Color) this.colorEditorTop.getComponentValueObj();
	}
	
	public ShapeStyling getShapeStyling(int shapeIndex)
	{
		return this.shapeStyling.get(shapeIndex);
	}
	
	public void setShapeStyling(int shapeIndex, ShapeStyling shapeStyling)
	{
		this.shapeStyling.put(shapeIndex, shapeStyling);
	}
	
	public Point [] getControlPoints()
	{
		return this.controlPoints;
	}
	
	public void setNumberOfPoints(int numberOfPoints)
	{
		this.controlPoints = new Point[numberOfPoints];
	}
	
	public ShapeCreatorEditPanel getShapeCreatorEditPanel()
	{
		return this.shapeCreatorEditPanel;
	}
	
	public void addShapeSelectedIndex(int index)
	{
		shapeSelectedIndexes.add(index);
	}
	
	public ArrayList<Integer> getShapeSelectedIndexes()
	{
		return this.shapeSelectedIndexes;
	}
	
	protected void clearShapeSelectedIndex()
	{
		this.shapeSelectedIndexes = new ArrayList<Integer>();
	}
	
	public Rectangle2D getSelectTool()
	{
		return this.selectTool;
	}
	
	public void setSelectTool(Rectangle2D selectTool)
	{
		this.selectTool = selectTool;
	}
	
	public void setMode(DrawMode mode)
	{
		this.modeSelections.setSelectedItem(mode);
		this.setNumberOfPoints(mode.getNumberOfPoints());
	}
	
	public DrawMode getMode()
	{
		return (DrawMode) this.modeSelections.getSelectedItem();
	}
	
	public JComboBox<DrawMode> getModeSelectionCombo()
	{
		return this.modeSelections;
	}
	
	public void setOperation(Operation operation)
	{
		this.operation = operation;
		operationLabel.setText(this.operation.getTitleText());
	}
	
	public Operation getOperation()
	{
		return this.operation;
	}
	
	public JLabel getDirectionsLabel()
	{
		return this.directionsLabel;
	}
	
	public JButton getAddCurveButton()
	{
		return this.addShape;
	}
	
	public Rectangle2D getSelectionRectangle()
	{
		return this.selectionRect;
	}
	public void setSelectionRectangle(Rectangle2D selectionRect)
	{
		this.selectionRect = selectionRect;
		if(selectionRect == null)
		{
			clearShapeSelectedIndex();
		}
	}
	
	public ArrayList<Shape> getShapesScaled()
	{
		return this.shapesScaled;
	}
	
	public void setShapesScaled(ArrayList<Shape> shapesRepl)
	{
		this.shapesScaled = shapesRepl;
	}
	
	public ArrayList<ArrayList<Point>> getControlPointsScaled()
	{
		return this.listControlPointsScaled;
	}
	
	public void setControlPointsScaled(ArrayList<ArrayList<Point>> listControlPointsScaledRepl)
	{
		this.listControlPointsScaled = listControlPointsScaledRepl;
		//TODO
	}
	
	public int getDirectionsIndex()
	{
		return this.directionsIndex;
	}
	
	public void incrementDirectionsIndex(int inc)
	{
		this.directionsIndex += inc;
	}
	
	public void setDirectionsIndex(int index)
	{
		this.directionsIndex = index;
	}
	
	public int getControlPointSelectedIndex()
	{
		return this.controlPointSelectedIndex;
	}
	public void incrementControlPointSelectedIndex(int inc)
	{
		this.controlPointSelectedIndex += inc;
	}
	public void setControlPointSelectedIndex(int index)
	{
		this.controlPointSelectedIndex = index;
	}
	
	public int getControlPointShapeSelectedIndex()
	{
		return this.controlPointShapeSelectedIndex;
	}
	public void incrementControlPointShapeSelectedIndex(int inc)
	{
		this.controlPointShapeSelectedIndex += inc;
	}
	public void setControlPointShapeSelectedIndex(int index)
	{
		this.controlPointShapeSelectedIndex = index;
	}
	
	public int getNumShapes()
	{
		return this.numShapes;
	}
	public void incrementNumShapes(int inc)
	{
		this.numShapes += inc;
	}
	public void setNumShapes(int index)
	{
		this.numShapes = index;;
	}
	
	public Point getRelativePoint(MouseEvent e)
	{
		Point p = e.getPoint();
		return p;
	}
	
	public Point getMouseDragLastPoint()
	{
		return this.mouseDragLastPoint;
	}
	
	public void setMouseDragLastPoint(Point p)
	{
		this.mouseDragLastPoint = p;
	}
	
	public Point getMouseDragStartPoint()
	{
		return mouseDragStartPoint;
	}
	
	public void setMouseDragStartPoint(Point startPoint)
	{
		this.mouseDragStartPoint = startPoint;
	}
	
	public boolean getControlPointSelected()
	{
		return controlPointSelected;
	}
	
	public void setControlPointSelected(boolean controlPointSelected)
	{
		this.controlPointSelected = controlPointSelected;
	}
	
	public void setMousePressed(boolean mousePressed)
	{
		this.mousePressed = mousePressed;
	}
	
	public boolean getMousePressed()
	{
		return this.mousePressed;
	}
	
	public void drawAll()
	{
		
		clearAll();
		drawShapes(shapesScaled);
		if(selectionRect != null) drawShape(selectionRect, Color.gray);
		drawControlPoints(listControlPointsScaled);
	}
	
	
	public void addControlPoint(Point p)
	{
		if(listControlPointsScaled.size() <= numShapes)
		{
			listControlPointsScaled.add(new ArrayList<Point>());
		}
		listControlPointsScaled.get(numShapes).add(p);
		drawControlPoint(p);
	}
	
	public void drawShapes(ArrayList<Shape> shapes)
	{
		int count = 0;//TODO
		for(Shape s : shapes)
		{
			Color c = this.getShapeStyling(count).getColor();
			drawShape(s, c);
			count++;
		}
	}
	public void drawShape(Shape shape)
	{
		drawShape(shape, Color.black);
	}
	public void drawShape(Shape shape, Color c)
	{
		Graphics2D g2d = (Graphics2D) draw.getGraphics();
		g2d.setColor(c);
		g2d.draw(shape);
	}
	protected void drawControlPoint(Point p)
	{
		Rectangle r = new Rectangle(p);
		r.setSize(CONTROL_POINT_PIXEL_SIZE.width, CONTROL_POINT_PIXEL_SIZE.height);
		Graphics2D g2d = (Graphics2D) draw.getGraphics();
		g2d.setColor(Color.black);
		g2d.draw(r);
	}
	protected void drawControlPoints(ArrayList<ArrayList<Point>> listOfControlPoints)
	{
		for(ArrayList<Point> controlPoints : listOfControlPoints)
		{
			for(Point p : controlPoints)
			{
				drawControlPoint(p);
			}
		}
	}
	
	public void clearAll()
	{
		Graphics2D g2d = (Graphics2D) draw.getGraphics();
		draw.paint(g2d);
	}
	
	public void notifyShapeAndControlPointChangedListener(int indexShape, int indexControlPoint, ControlPointChangedListener ignoreChangedListener)
	{
		if(!this.shapeAndControlPointChangedListener.containsKey(indexShape))
			return;
		
		HashMap<Integer, ArrayList<ControlPointChangedListener>> controlPointChangedListeners = this.shapeAndControlPointChangedListener.get(indexShape);
		for(ControlPointChangedListener cpcl : controlPointChangedListeners.get(indexControlPoint))
		{
			if(!Arrays.asList(ignoreChangedListener).contains(cpcl))
			{
				cpcl.controlPointChangedNotification(indexShape, indexControlPoint);
			}
		}
	}
	
	public void addShapeAndControlPointChangedListener(int indexShape, int indexControlPoint, ControlPointChangedListener changedListener)
	{
		if(!this.shapeAndControlPointChangedListener.containsKey(indexShape))
		{
			HashMap<Integer, ArrayList<ControlPointChangedListener>> mapControlPointListeners = new HashMap<Integer, ArrayList<ControlPointChangedListener>>();
			ArrayList<ControlPointChangedListener> controlPointChangedListeners = new ArrayList<ControlPointChangedListener>();
			controlPointChangedListeners.add(changedListener);
			mapControlPointListeners.put(indexControlPoint, controlPointChangedListeners);
			this.shapeAndControlPointChangedListener.put(indexShape, mapControlPointListeners);
		}
		else if(!this.shapeAndControlPointChangedListener.get(indexShape).containsKey(indexControlPoint))
		{
			HashMap<Integer, ArrayList<ControlPointChangedListener>> mapControlPointListeners = this.shapeAndControlPointChangedListener.get(indexShape);
			ArrayList<ControlPointChangedListener> controlPointChangedListeners = new ArrayList<ControlPointChangedListener>();
			controlPointChangedListeners.add(changedListener);
			mapControlPointListeners.put(indexControlPoint, controlPointChangedListeners);
		}
		else
		{
			HashMap<Integer, ArrayList<ControlPointChangedListener>> mapControlPointListeners = this.shapeAndControlPointChangedListener.get(indexShape);
			mapControlPointListeners.get(indexControlPoint).add(changedListener);
		}
	}
	
	public Shape recalculateShape(Shape s, ArrayList<Point> cps)
	{
		if(s instanceof CurveShape)
		{
			s = new CurveShape(cps.get(0), cps.get(2), cps.get(3), cps.get(1));
		}
		else if(s instanceof Line2D)
		{
			s = new Line2D.Double(cps.get(0), cps.get(1));
		}
		else if(s instanceof Rectangle2D)
		{
			s = new Rectangle2D.Double(
					cps.get(0).x, cps.get(0).y, 
					(cps.get(1).x - cps.get(0).x), (cps.get(1).y - cps.get(0).y));
		}
		else if(s instanceof Ellipse2D)
		{
			s = new Ellipse2D.Double(
					cps.get(0).x, cps.get(0).y, 
					(cps.get(1).x - cps.get(0).x), (cps.get(1).y - cps.get(0).y));
		}
		return s;
	}
	
	public enum Operation
	{
		Move	("-----------Move-----------"),
		Resize	("----------Resize----------"),
		Select	("----------Select----------"),
		Draw	("-----------Draw-----------");
		
		private String titleText;
		
		private Operation(String titleText)
		{
			this.titleText = titleText;
		}
		public String getTitleText()
		{
			return this.titleText;
		}
	}
	
	public enum DrawMode
	{
		Line("Line", LINE_DIRECTIONS, 2),
		Curve("Curve", CURVE_DIRECTIONS, 4),
		ellipse("Elipse", ELLIPSE_DIRECTIONS, 2),
		rectangle("Rectangle", RECTANGLE_DIRECTIONS, 2);
		
		private String modeText;
		private String [] directions;
		private int numberOfPoints;
		
		private DrawMode(String modeText, String [] directions, int numberOfPoints)
		{
			this.modeText = modeText;
			this.directions = directions;
			this.numberOfPoints = numberOfPoints;
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

	@Override
	public void notifyStylingChanged(int shapeStyleIndex, ShapeStyling shapeStyling) 
	{
		this.setShapeStyling(shapeStyleIndex, shapeStyling);
		this.drawAll();
	}
	
}
