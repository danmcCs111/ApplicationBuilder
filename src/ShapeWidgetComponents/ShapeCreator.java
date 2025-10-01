package ShapeWidgetComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JPanel;

import DrawModes.PenDrawMode;
import DrawModesAbstract.DrawMode;
import Properties.LoggingMessages;
import ShapeEditorListeners.AddShapesImportedListener;
import ShapeEditorListeners.ControlPointChangedListener;
import ShapeEditorListeners.DrawMouseListener;
import ShapeEditorListeners.ShapeDirectionsNotification;
import ShapeEditorListeners.ShapeStylingActionListener;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;
import WidgetComponents.LocationSouthBar;

public class ShapeCreator extends JPanel implements ShapeStylingActionListener, PostWidgetBuildProcessing, AddShapesImportedListener
{
	private static final long serialVersionUID = 3005L;
	
	private int 
		directionsIndex = 0, 
		controlPointSelectedIndex = -1,
		controlPointShapeSelectedIndex = -1;
	private ShapeDrawingCollection sdc;
	
	private ArrayList<ShapeStyling> shapeSelectedIndexes = new ArrayList<ShapeStyling>();
	private HashMap<ShapeStyling, HashMap<Integer, ArrayList<ControlPointChangedListener>>> shapeAndControlPointChangedListener = 
			new HashMap<ShapeStyling, HashMap<Integer, ArrayList<ControlPointChangedListener>>>();
	private boolean 
		mousePressed = false,
		controlPointSelected = false;
	private Point 
		mouseDragStartPoint,
		mouseDragLastPoint;
	private ArrayList<Point> controlPoints = new ArrayList<Point>();
	private Rectangle2D selectTool;
	private Rectangle2D selectionRect;
	
	private JPanel 
		draw;
	private ShapeCreatorEditPanel shapeCreatorEditPanel;
	private DrawMouseListener dml;
	
	private DrawMode mode;
	
	private Operation operation = Operation.Select;
	
	private Color colorPallette;
	
	private ArrayList<ShapeDirectionsNotification> shapeDirectionsNotification = new ArrayList<ShapeDirectionsNotification>();
	
	public ShapeCreator()
	{
		sdc = new ShapeDrawingCollection();//TODO
	}
	
	public void buildWidgets()
	{
		draw = new JPanel();
		dml = new DrawMouseListener(this);
		draw.addMouseListener(dml);
		draw.addMouseMotionListener(dml);
		this.add(draw, BorderLayout.CENTER);
		LocationSouthBar scsb = new LocationSouthBar(this);
		draw.addMouseMotionListener(scsb);
	}
	
	public void validateFrame()
	{
		this.getParent().validate();
	}
	
	public void clearAll()
	{
		ShapeDrawingCollectionGraphics.clearAll(draw);
	}
	
	public void drawAll()
	{
		ShapeDrawingCollectionGraphics.drawAll(draw, sdc, selectionRect);
	}
	
	public void drawGenerators(ArrayList<Shape> shapes, ArrayList<ShapeStyling> shapeStylings)
	{
		ShapeDrawingCollectionGraphics.drawGenerators(draw, sdc);
	}
	
	public void drawShapes(ArrayList<Shape> shapes, ArrayList<ShapeStyling> shapeStylings)
	{
		ShapeDrawingCollectionGraphics.drawShapes(draw, sdc);
	}
	public void drawShape(Shape shape, Color c)
	{
		ShapeDrawingCollectionGraphics.drawShape(draw, shape, c);
	}
	public void drawShape(Shape shape, ShapeStyling shapeStyling)
	{
		ShapeDrawingCollectionGraphics.drawShape(draw, shape, shapeStyling);
		
	}
	protected void drawControlPoint(Point p)
	{
		ShapeDrawingCollectionGraphics.drawControlPoint(draw, p);
	}
	protected void drawControlPoints(ArrayList<ArrayList<Point>> listOfControlPoints)
	{
		ShapeDrawingCollectionGraphics.drawControlPoints(draw, sdc);
	}
	
	public void drawShapeStylingAlgorithm(Shape s, ShapeStyling ss)
	{
		ShapeDrawingCollectionGraphics.drawShapeStylingAlgorithm(draw, s, ss);
	}
	
	public void notifyShapeAndControlPointsChangedListener(ShapeStyling ss)
	{
		notifyShapeAndControlPointsChangedListener(ss, null);
	}
	public void notifyShapeAndControlPointsChangedListener(ShapeStyling ss, ControlPointChangedListener ignorechangedListener)
	{
		for(int i = 0; i < getControlPointsForShapes().get(ss.getIndex()).size(); i++)
		{
			notifyShapeAndControlPointChangedListener(ss, i, ignorechangedListener);
		}
	}
	public void notifyShapeAndControlPointChangedListener(ShapeStyling ss, int indexControlPoint, ControlPointChangedListener ignorechangedListener)
	{
		if(!this.shapeAndControlPointChangedListener.containsKey(ss))
			return;
		
		HashMap<Integer, ArrayList<ControlPointChangedListener>> controlPointChangedListeners = this.shapeAndControlPointChangedListener.get(ss);
		for(ControlPointChangedListener cpcl : controlPointChangedListeners.get(indexControlPoint))
		{
			if(ignorechangedListener != null && !ignorechangedListener.equals(cpcl))
			{
				cpcl.controlPointChangedNotification(ss, indexControlPoint);
			}
		}
		drawAll();
	}
	
	public void addShapeAndControlPointChangedListener(ShapeStyling ss, int indexControlPoint, ControlPointChangedListener changedListener)
	{
		if(!this.shapeAndControlPointChangedListener.containsKey(ss))
		{
			HashMap<Integer, ArrayList<ControlPointChangedListener>> mapControlPointListeners = new HashMap<Integer, ArrayList<ControlPointChangedListener>>();
			ArrayList<ControlPointChangedListener> controlPointChangedListeners = new ArrayList<ControlPointChangedListener>();
			controlPointChangedListeners.add(changedListener);
			mapControlPointListeners.put(indexControlPoint, controlPointChangedListeners);
			this.shapeAndControlPointChangedListener.put(ss, mapControlPointListeners);
		}
		else if(!this.shapeAndControlPointChangedListener.get(ss).containsKey(indexControlPoint))
		{
			HashMap<Integer, ArrayList<ControlPointChangedListener>> mapControlPointListeners = this.shapeAndControlPointChangedListener.get(ss);
			ArrayList<ControlPointChangedListener> controlPointChangedListeners = new ArrayList<ControlPointChangedListener>();
			controlPointChangedListeners.add(changedListener);
			mapControlPointListeners.put(indexControlPoint, controlPointChangedListeners);
		}
		else
		{
			HashMap<Integer, ArrayList<ControlPointChangedListener>> mapControlPointListeners = this.shapeAndControlPointChangedListener.get(ss);
			mapControlPointListeners.get(indexControlPoint).add(changedListener);
		}
	}
	
	public Shape recalculateShape(Shape s, ArrayList<Point> cps)
	{
		return ShapeUtils.recalculateShape(s, cps);
	}
	
	public Shape constructShape(DrawMode mode, Point [] curvePoints)
	{
		ShapeStyling shapeStyling;
		Shape shape;
		
		if(mode instanceof PenDrawMode)
		{
			ArrayList<Point> newPoints = new ArrayList<Point>(Arrays.asList(curvePoints).subList(1, curvePoints.length-1));
			shape = ShapeUtils.constructShape(mode, 
					newPoints.toArray(new Point[] {}), 
					(Graphics2D)this.draw.getGraphics());
			this.getControlPointsForShapes().set(getNumShapes(), newPoints);
			shapeStyling = new ShapeStyling(getNumShapes(), getColorPallette(), null, this);
			shapeStyling.createStrokedShape(false);
		}
		else
		{
			shape = ShapeUtils.constructShape(mode, curvePoints, (Graphics2D)this.draw.getGraphics());
			shapeStyling = new ShapeStyling(getNumShapes(), getColorPallette(), getColorPallette(), this);
		}
		sdc.addShape(shape);
		LoggingMessages.printOut(getColorPallette()+"");
		sdc.addShapeStyling(shapeStyling);
		generatePointEditor(mode, curvePoints, shapeStyling);	
		
		drawAll();
		
		return shape;
	}
	
	public void generatePointEditor(DrawMode mode, Point [] curvePoints, ShapeStyling shapeStyling)
	{
		this.getShapeCreatorEditPanel().generatePointEditor(this.getNumShapes()-1, curvePoints, mode, shapeStyling.getDrawColor());
	}
	
	public JPanel getDrawPanel()
	{
		return this.draw;
	}
	
	public void addControlPoint(Point p)
	{
		if(this.getControlPointsForShapes().size() <= getNumShapes())
		{
			this.getControlPointsForShapes().add(new ArrayList<Point>());
		}
		this.getControlPointsForShapes().get(getNumShapes()).add(p);
		drawControlPoint(p);
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
		return sdc.getShapeStylings();
	}
	
	public ShapeStyling getShapeStyling(int shapeIndex)
	{
		return this.getShapeStylings().get(shapeIndex);
	}
	
	public void setShapeStyling(int shapeIndex, ShapeStyling shapeStyling)
	{
		if(shapeIndex < this.getShapeStylings().size())
		{
			this.getShapeStylings().set(shapeIndex, shapeStyling);
		}
		else
		{
			this.getShapeStylings().add(shapeStyling);
		}
	}
	
	public ArrayList<Point> getControlPoints()
	{
		return this.controlPoints;
	}
	
	public void setNumberOfPoints(int numberOfPoints)
	{
		this.controlPoints = new ArrayList<Point>();
	}
	
	public ShapeCreatorEditPanel getShapeCreatorEditPanel()
	{
		return this.shapeCreatorEditPanel;
	}
	
	public void setShapeCreatorEditPanel(ShapeCreatorEditPanel shapeCreatorEditPanel)
	{
		this.shapeCreatorEditPanel = shapeCreatorEditPanel;
	}
	
	public void addShapeSelectedIndex(ShapeStyling index)
	{
		shapeSelectedIndexes.add(index);
	}
	
	public void removeShapeSelectedIndex(ShapeStyling index)
	{
		shapeSelectedIndexes.remove(index);
	}
	
	public ArrayList<ShapeStyling> getShapeSelectedIndexes()
	{
		return this.shapeSelectedIndexes;
	}
	
	protected void clearShapeSelectedIndex()
	{
		this.shapeSelectedIndexes = new ArrayList<ShapeStyling>();
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
		this.setNumberOfPoints(mode.getNumberOfPoints());
	}
	
	public void setOperation(Operation operation)
	{
		this.operation = operation;
		for(ShapeDirectionsNotification sdn : shapeDirectionsNotification)
		{
			sdn.shapeOperationUpdate(operation);
		}
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
	
	public ShapeDrawingCollection getShapeDrawingCollection()
	{
		return this.sdc;
	}
	
	public ArrayList<Shape> getShapes()
	{
		return this.sdc.getShapes();
	}
	
	public void setShapes(ArrayList<Shape> shapesRepl)
	{
		this.sdc.setShapes(shapesRepl);
	}
	
	public ArrayList<ArrayList<Point>> getControlPointsForShapes()
	{
		return this.sdc.getShapeControlPoints();
	}
	
	public void setControlPointsForShapes(ArrayList<ArrayList<Point>> listControlPointsScaledRepl)
	{
		this.sdc.setShapeControlPoints(listControlPointsScaledRepl);
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
		this.controlPoints = new ArrayList<Point>();
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
		return this.sdc.getShapes().size();
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

	@Override
	public void notifyImported(ShapeElement se) 
	{
		generatePointEditor(se.getDrawMode(), (Point [])se.getPoints().toArray(new Point [] {}), this.getShapeStyling(getNumShapes()-1));
	}
	
}
