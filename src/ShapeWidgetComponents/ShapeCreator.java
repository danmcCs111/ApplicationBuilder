package ShapeWidgetComponents;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

import BezierCurveCalculations.AffineTransformRasterizer;
import BezierCurveCalculations.ShapePositionOnPoints;
import Properties.LoggingMessages;
import ShapeEditorListeners.ControlPointChangedListener;
import ShapeEditorListeners.DrawMouseListener;
import ShapeEditorListeners.ShapeDirectionsNotification;
import ShapeEditorListeners.ShapeStylingActionListener;
import ShapeWidgetComponents.ShapeUtils.DrawMode;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;

public class ShapeCreator extends JPanel implements ShapeStylingActionListener, PostWidgetBuildProcessing
{
	private static final long serialVersionUID = 3005L;
	
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
	
	private JPanel 
		draw;
	private ShapeCreatorEditPanel shapeCreatorEditPanel;
	private ArrayList<ShapeStyling> shapeStyling = new ArrayList<ShapeStyling>();
	private DrawMouseListener dml;
	
	private DrawMode mode;
	
	private Operation operation = Operation.Select;
	private BasicStroke 
		defaultStroke = new BasicStroke(1);
	
	private Color colorPallette;
	
	private ArrayList<ShapeDirectionsNotification> shapeDirectionsNotification = new ArrayList<ShapeDirectionsNotification>();
	
	public ShapeCreator()
	{
		
	}
	
	public void buildWidgets()
	{
		draw = new JPanel();
		setMode(DrawMode.Line);//default.
		dml = new DrawMouseListener(this);
		draw.addMouseListener(dml);
		draw.addMouseMotionListener(dml);
		this.add(draw, BorderLayout.CENTER);
	}
	
	public void drawAll()
	{
		clearAll();
		drawShapes(shapesScaled, shapeStyling);
		if(selectionRect != null) drawShape(selectionRect, Color.gray);
		drawControlPoints(listControlPointsScaled);
		drawGenerators(shapesScaled, shapeStyling);
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
	
	public void drawGenerators(ArrayList<Shape> shapes, ArrayList<ShapeStyling> shapeStylings)
	{
		int count = 0;
		for(Shape s : shapes)
		{
			ShapeStyling ss = shapeStylings.get(count);
			if(ss.getNumberGeneratorConfig() != null)
			{
				ss.updateNumberGeneratorConfig(s);
				drawShapeStylingAlgorithm(s, ss);
			}
			count++;
		}
	}
	
	public void drawShapes(ArrayList<Shape> shapes, ArrayList<ShapeStyling> shapeStylings)
	{
		int count = 0;
		for(Shape s : shapes)
		{
			drawShape(s, shapeStylings.get(count));
			count++;
		}
	}
	public void drawShapes(ArrayList<Shape> shapes)
	{
		for(Shape s : shapes)
		{
			drawShape(s, Color.gray);
		}
	}
	public void drawShape(Shape shape, Color c)
	{
		Graphics2D g2d = (Graphics2D)draw.getGraphics();
		if(g2d == null)
			return;
		g2d.setColor(c);
		g2d.draw(shape);
	}
	public void drawShape(Shape shape, ShapeStyling shapeStyling)
	{
		Graphics2D g2d = (Graphics2D)draw.getGraphics();
		if(g2d == null)
			return;
		Color c = shapeStyling.getDrawColor(); 
		Color fillColor = shapeStyling.getFillColor();
		Stroke stroke = shapeStyling.getStroke();
		
		if(stroke != null && !g2d.getStroke().equals(stroke) && shapeStyling.isCreateStrokedShape())
		{
			g2d.setStroke(stroke);
			shape = g2d.getStroke().createStrokedShape(shape);
		}
		else {
			g2d.setStroke(defaultStroke);
		}
		g2d.setColor(c);
		g2d.draw(shape);
		if(fillColor != null)
		{
			g2d.setColor(fillColor);
			g2d.fill(shape);
		}
		
	}
	protected void drawControlPoint(Point p)
	{
		Graphics2D g2d = (Graphics2D)draw.getGraphics();
		if(g2d == null)
			return;
		Rectangle r = new Rectangle(p);
		r.setSize(CONTROL_POINT_PIXEL_SIZE.width, CONTROL_POINT_PIXEL_SIZE.height);
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
		if(g2d != null)
			draw.paint(g2d);
	}
	
	public void drawShapeStylingAlgorithm(Shape s, ShapeStyling ss)
	{
		NumberGeneratorConfig ngConfig = ss.getNumberGeneratorConfig();
		Color selectColor = ngConfig.getFillColor();
		
		Font testFont = new Font("Serif", Font.BOLD, ngConfig.getFontSize()); //TODO
		PathIterator pi = ss.getPathIterator();
		AffineTransformRasterizer afs = ss.getAffineTransform();
		
		LoggingMessages.printOut("Number of Steps: " + afs.getNumberOfSteps());
		if(afs == null || pi == null)
		{
			return;
		}
		
		if(selectColor == null)
		{
			selectColor = Color.black;
		}
		
		ArrayList<Point> points = new ArrayList<Point>();
		points.addAll(afs.samplePoints(pi, s, (1.0/ngConfig.getNumberOfSamples())));
		LoggingMessages.printOut("Number of Steps: " + afs.getNumberOfSteps() + " size " + points.size());
		if(afs.getNumberOfSteps() == 0)//TODO bug?
			return;
		double it = ((ngConfig.getRangeValHigh() - (ngConfig.getRangeValLow()-1)) / afs.getNumberOfSteps());
		LoggingMessages.printOut(it+"");
		ShapePositionOnPoints.drawNumberSequence(points, (Graphics2D)this.getDrawPanel().getGraphics(), testFont, selectColor,
				(ngConfig.getNumberOfSamples()/it), ngConfig.getRangeValLow(), ngConfig.getRangeValHigh(), ngConfig.getStartingNumber());
		
	}
	
	public void notifyShapeAndControlPointsChangedListener(int indexShape)
	{
		notifyShapeAndControlPointsChangedListener(indexShape, null);
	}
	public void notifyShapeAndControlPointsChangedListener(int indexShape, ControlPointChangedListener ignorechangedListener)
	{
		for(int i = 0; i < listControlPointsScaled.get(indexShape).size(); i++)
		{
			notifyShapeAndControlPointChangedListener(indexShape, i, ignorechangedListener);
		}
	}
	public void notifyShapeAndControlPointChangedListener(int indexShape, int indexControlPoint, ControlPointChangedListener ignorechangedListener)
	{
		if(!this.shapeAndControlPointChangedListener.containsKey(indexShape))
			return;
		
		HashMap<Integer, ArrayList<ControlPointChangedListener>> controlPointChangedListeners = this.shapeAndControlPointChangedListener.get(indexShape);
		for(ControlPointChangedListener cpcl : controlPointChangedListeners.get(indexControlPoint))
		{
			if(ignorechangedListener != null && !ignorechangedListener.equals(cpcl))
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
		return ShapeUtils.recalculateShape(s, cps);
	}
	
	public Shape constructShape(DrawMode mode, Point [] curvePoints, ShapeStyling shapeStyling)
	{
		Shape shape = ShapeUtils.constructShape(mode, curvePoints, shapeStyling);
		
		shapesScaled.add(shape);
		this.incrementNumShapes(1);
		this.addShapeAndControlPointChangedListener(this.getNumShapes()-1, this.getDirectionsIndex()-1, dml);
		this.setShapeStyling(this.getNumShapes()-1, shapeStyling);
		this.getShapeCreatorEditPanel().generatePointEditor(this.getNumShapes()-1, curvePoints, mode, shapeStyling.getDrawColor());
		
		return shape;
	}
	
	public JPanel getDrawPanel()
	{
		return this.draw;
	}
	
	public void setDirectionsText(String text)
	{
		for(ShapeDirectionsNotification sdn : shapeDirectionsNotification)
		{
			sdn.shapeDirectionsUpdate(text);
		}
	}
	
	public void addShapeDirectionsNotification(ShapeDirectionsNotification sdn)
	{
		this.shapeDirectionsNotification.add(sdn);
	}
	
	public Color getColorPallette()
	{
		return this.colorPallette;
	}
	
	public void setColorPallette(Color c)
	{
		this.colorPallette = c;
	}
	
	public ArrayList<ShapeStyling> getShapeStylings()
	{
		return this.shapeStyling;
	}
	
	public ShapeStyling getShapeStyling(int shapeIndex)
	{
		return this.shapeStyling.get(shapeIndex);
	}
	
	public void setShapeStyling(int shapeIndex, ShapeStyling shapeStyling)
	{
		if(shapeIndex < this.shapeStyling.size())
		{
			this.shapeStyling.set(shapeIndex, shapeStyling);
		}
		else
		{
			this.shapeStyling.add(shapeStyling);
		}
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
	
	public void setShapeCreatorEditPanel(ShapeCreatorEditPanel shapeCreatorEditPanel)
	{
		this.shapeCreatorEditPanel = shapeCreatorEditPanel;
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
	
	public Operation getOperation()
	{
		return this.operation;
	}
	
	public DrawMode getMode()
	{
		return this.mode;
	}
	
	public void setMode(DrawMode mode)
	{
		this.mode = mode;
//		this.modeSelections.setSelectedItem(mode);
		this.setNumberOfPoints(mode.getNumberOfPoints());
	}
	
	public void setOperation(Operation operation)
	{
		this.operation = operation;
//		operationLabel.setText(this.operation.getTitleText());
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
	
	@Override
	public void notifyStylingChanged(int shapeStyleIndex, ShapeStyling shapeStyling) 
	{
		this.setShapeStyling(shapeStyleIndex, shapeStyling);
		this.drawAll();
	}

	@Override
	public void postExecute() 
	{
		buildWidgets();
	}
	
}
