package ShapeEditorListeners;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;

import Graphics2D.CurveShape;
import Properties.LoggingMessages;
import ShapeWidgetComponents.RectangleCubic;
import ShapeWidgetComponents.ShapeCreator;
import ShapeWidgetComponents.ShapeCreator.DrawMode;
import ShapeWidgetComponents.ShapeCreator.Operation;
import ShapeWidgetComponents.ShapeStyling;
import ShapeWidgetComponents.Triangle;
import ShapeWidgetComponents.TriangleCubic;

public class DrawMouseListener extends MouseAdapter implements ControlPointChangedListener 
{
	private ShapeCreator sc;
	
	public DrawMouseListener(ShapeCreator sc)
	{ 
		this.sc = sc;
	}
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
		int directionsIndex = sc.getDirectionsIndex();
		if(sc.getOperation() == Operation.Select && directionsIndex == 0 && !sc.getControlPointSelected())
		{
			Point mouseDragStartPoint = sc.getMouseDragStartPoint();
			Point nextPoint = sc.getRelativePoint(e);
			Rectangle2D select = new Rectangle2D.Double(
					mouseDragStartPoint.x, mouseDragStartPoint.y, 
					(nextPoint.x - mouseDragStartPoint.x), (nextPoint.y - mouseDragStartPoint.y));
			sc.setSelectTool(select);
			
			sc.drawAll();
			sc.drawShape(select, Color.red);
		}
		else if(sc.getOperation() == Operation.Move)
		{
			Point mouseDragStartPoint = sc.getMouseDragLastPoint();
			Point nextPoint = sc.getRelativePoint(e);
			
			int diffx = mouseDragStartPoint.x - nextPoint.x;
			int diffy = mouseDragStartPoint.y - nextPoint.y;
			applyShiftAmount(new Point(diffx, diffy));
			sc.setMouseDragLastPoint(nextPoint);
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		JFrame frame = (JFrame) sc.getRootPane().getParent();
		if(sc.getSelectionRectangle() != null && sc.getSelectionRectangle().contains(sc.getRelativePoint(e)))
		{
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			sc.setOperation(Operation.Move);
		}
		//TODO detect control points
		else if(!frame.getCursor().equals(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)))
		{
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			sc.setOperation(Operation.Select);
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) 
	{
		int directionsIndex = sc.getDirectionsIndex();
		ArrayList<ArrayList<Point>> listControlPointsScaled = sc.getControlPointsScaled();
		Point p = sc.getRelativePoint(e);
		sc.setMousePressed(true);
		sc.setControlPointSelected(false);
		if(sc.getSelectionRectangle() != null && !sc.getSelectionRectangle().contains(p))
		{
			clearSelection();
		}
		
		if(directionsIndex == 0)
		{
			LoggingMessages.printOut(p + "");
			int outerCount = 0;
			for(ArrayList<Point> controlPoints : listControlPointsScaled)
			{
				int count = 0;
				for(Point cp : controlPoints)
				{
					if(p.x >= cp.x && p.x <= cp.x + ShapeCreator.CONTROL_POINT_PIXEL_SIZE.width)
					{
						if(p.y >= cp.y && p.y <= cp.y + ShapeCreator.CONTROL_POINT_PIXEL_SIZE.height)
						{
							LoggingMessages.printOut("Control Point selected!");
							sc.setControlPointSelected(true);
							sc.setControlPointSelectedIndex(count);
							sc.setControlPointShapeSelectedIndex(outerCount);
							break;
						}
					}
					count++;
				}
				outerCount++;
			}
		}
		if(!sc.getControlPointSelected())
		{
			sc.setMouseDragStartPoint(p);
			sc.setMouseDragLastPoint(p);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) 
	{
		super.mouseReleased(e);
		sc.setMousePressed(false);
		int controlPointShapeSelectedIndex = sc.getControlPointShapeSelectedIndex();
		int controlPointSelectedIndex = sc.getControlPointSelectedIndex();
		ArrayList<Shape> shapesScaled = sc.getShapesScaled();
		ArrayList<ArrayList<Point>> listControlPointsScaled = sc.getControlPointsScaled();
		
		if(sc.getOperation() == Operation.Select)
		{
			if(controlPointSelectedIndex != -1)
			{
				Point p = sc.getRelativePoint(e);
				LoggingMessages.printOut("shape index: " + controlPointShapeSelectedIndex + " controlPoint index:" + controlPointSelectedIndex);
				listControlPointsScaled.get(controlPointShapeSelectedIndex).set(controlPointSelectedIndex, p);
				Shape s = shapesScaled.get(controlPointShapeSelectedIndex);
				ArrayList<Point> cps = listControlPointsScaled.get(controlPointShapeSelectedIndex);
				
				s = sc.recalculateShape(s, cps);
				
				shapesScaled.set(controlPointShapeSelectedIndex, s);
				sc.notifyShapeAndControlPointChangedListener(controlPointShapeSelectedIndex, controlPointSelectedIndex, this);
			}
			
			if(sc.getDirectionsIndex() == 0 && !sc.getControlPointSelected() && sc.getSelectTool() != null)
			{
				detectBounds(sc.getSelectTool());
			}
		}
		
		sc.drawAll();
		sc.setControlPointSelectedIndex(-1);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		DrawMode mode = sc.getMode();
		int directionsIndex = sc.getDirectionsIndex();
		
		if(directionsIndex > 0)
		{
			Point p = sc.getRelativePoint(e);
			Point [] curvePoints = sc.getControlPoints();
			JLabel directionsLabel = sc.getDirectionsLabel();
			ArrayList<Shape> shapes = sc.getShapesScaled();
			
			curvePoints[directionsIndex-1] = p;
			sc.addControlPoint(p);
			LoggingMessages.printOut(p + "");
			if(directionsIndex + 1 >= mode.getDirections().length)
			{
				sc.setDirectionsIndex(0);
				sc.getAddCurveButton().setVisible(true);
				Shape shape = null;
				switch(mode)
				{
				case DrawMode.Line:
					shape = new Line2D.Double(curvePoints[0], curvePoints[1]);
					break;
				case DrawMode.Curve:
					shape = new CurveShape(curvePoints[0], curvePoints[2], curvePoints[3], curvePoints[1]);
					break;
				case DrawMode.ellipse:
					shape = new Ellipse2D.Double(
							curvePoints[0].x, curvePoints[0].y, 
							(curvePoints[1].x - curvePoints[0].x), (curvePoints[1].y - curvePoints[0].y));
					break;
				case DrawMode.rectangle:
					shape = new Rectangle2D.Double(
							curvePoints[0].x, curvePoints[0].y, 
							(curvePoints[1].x - curvePoints[0].x), (curvePoints[1].y - curvePoints[0].y));
					break;
				case DrawMode.rectangleCubic:
					shape = new RectangleCubic(curvePoints[0], curvePoints[1], curvePoints[2], 
							curvePoints[3], curvePoints[4], curvePoints[5], curvePoints[6], curvePoints[7], curvePoints[8], curvePoints[9], curvePoints[10], curvePoints[11]);
					break;
				case DrawMode.triangle:
					shape = new Triangle(curvePoints[0], curvePoints[1], curvePoints[2]);
					break;
				case DrawMode.triangleCubic:
					shape = new TriangleCubic(curvePoints[0], curvePoints[1], curvePoints[2], 
							curvePoints[3], curvePoints[4], curvePoints[5], curvePoints[6], curvePoints[7], curvePoints[8]);
					break;
				}
				shapes.add(shape);
				sc.addShapeAndControlPointChangedListener(sc.getNumShapes(), sc.getDirectionsIndex()-1, this);
				directionsLabel.setText("");
				sc.incrementNumShapes(1);
				sc.getAddCurveButton().setEnabled(true);
				sc.setOperation(Operation.Select);
				sc.setShapeStyling(sc.getNumShapes()-1, new ShapeStyling(sc.getNumShapes()-1, shape, sc.getColorPallette(), sc));
				sc.getShapeCreatorEditPanel().generatePointEditor(sc.getNumShapes()-1, curvePoints, mode);
				
				sc.drawAll();
			}
			else
			{
				sc.addShapeAndControlPointChangedListener(sc.getNumShapes(), sc.getDirectionsIndex()-1, this);
				sc.incrementDirectionsIndex(1);
				directionsLabel.setText(mode.getDirections()[sc.getDirectionsIndex()]);
			}
		}
	}
	
	private void clearSelection()
	{
		sc.setSelectionRectangle(null);
		sc.setSelectTool(null);
		sc.drawAll();
	}
	
	public void applyShiftAmount(Point shift)
	{
		for(int index : sc.getShapeSelectedIndexes())
		{
			ArrayList<Point> shapesControlPoints = sc.getControlPointsScaled().get(index);
			Shape s = sc.getShapesScaled().get(index);
			ArrayList<Point> newPoints = new ArrayList<Point>();
			for(Point p : shapesControlPoints)
			{
				newPoints.add(new Point(p.x - shift.x, p.y - shift.y));
			}
			for(int i = 0; i < newPoints.size(); i++)
			{
				sc.getControlPointsScaled().get(index).set(i, newPoints.get(i));
				sc.notifyShapeAndControlPointChangedListener(index, i, this);
			}
			
			Shape newShape = sc.recalculateShape(s, newPoints);
			sc.getShapesScaled().set(index, newShape);
			
		}
		
		Shape selRect = sc.getSelectionRectangle();
		ArrayList<Point> newPointsRect = new ArrayList<Point>();
		int height = selRect.getBounds().height;
		int width = selRect.getBounds().width;
		int x = selRect.getBounds().x;
		int y = selRect.getBounds().y;
		newPointsRect.add(new Point(x - shift.x, y - shift.y));
		newPointsRect.add(new Point((width - shift.x) + x, (height - shift.y) + y));
		Rectangle2D newSel = (Rectangle2D) sc.recalculateShape(selRect, newPointsRect);
		sc.setSelectionRectangle(newSel);
		
		
		sc.drawAll();
	}
	
	private void detectBounds(Rectangle2D bounds)
	{
		ArrayList<Shape> selectedShapes = new ArrayList<Shape>();
		int index = 0;
		for(Shape s : sc.getShapesScaled())
		{
			if(bounds.contains(s.getBounds()))
			{
				selectedShapes.add(s);
				sc.addShapeSelectedIndex(index);
				LoggingMessages.printOut("Selecting: " + s.getClass().getName());
			}
			index++;
		}
		Point leastXy = null;
		Point widthHeight = new Point();
		Point greatestWidthHeight = null;
		
		if(!selectedShapes.isEmpty())
		{
			for(Shape s : selectedShapes)
			{
				if(leastXy == null)
				{
					leastXy = new Point(s.getBounds().x, s.getBounds().y);
				}
				else
				{
					if(leastXy.x > s.getBounds().x)
					{
						leastXy.x = s.getBounds().x;
					}
					if(leastXy.y > s.getBounds().y)
					{
						leastXy.y = s.getBounds().y;
					}
				}
				if(greatestWidthHeight == null)
				{
					greatestWidthHeight = new Point(s.getBounds().width + s.getBounds().x, 
							s.getBounds().height + s.getBounds().y);
				}
				else
				{
					if(greatestWidthHeight.x < s.getBounds().width + s.getBounds().x)
					{
						greatestWidthHeight.x = s.getBounds().width + s.getBounds().x;
					}
					if(greatestWidthHeight.y < s.getBounds().height + s.getBounds().y)
					{
						greatestWidthHeight.y = s.getBounds().height + s.getBounds().y;
					}
				}
			}
			widthHeight.x = greatestWidthHeight.x - leastXy.x;
			widthHeight.y = greatestWidthHeight.y - leastXy.y;
			Rectangle2D selectionRect = new Rectangle2D.Double(leastXy.x, leastXy.y, widthHeight.x, widthHeight.y);
			LoggingMessages.printOut(selectionRect + "");
			sc.setSelectionRectangle(selectionRect);
			sc.drawAll();
		}
		else
		{
			sc.setSelectionRectangle(null);
		}
	}

	@Override
	public void controlPointChangedNotification(int shapeIndex, int controlPointIndex) 
	{
		ArrayList<Point> newPoints = sc.getControlPointsScaled().get(shapeIndex);
		Shape s = sc.getShapesScaled().get(shapeIndex);
		Shape newShape = sc.recalculateShape(s, newPoints);
		sc.getShapesScaled().set(shapeIndex, newShape);
		
		sc.drawAll();
	}
	
}
