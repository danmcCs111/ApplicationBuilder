package ShapeEditorListeners;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JFrame;

import Properties.LoggingMessages;
import ShapeWidgetComponents.ShapeCreator;
import ShapeWidgetComponents.ShapeCreator.Operation;
import ShapeWidgetComponents.ShapeDrawingCollection;
import ShapeWidgetComponents.ShapeStyling;
import ShapeWidgetComponents.ShapeUtils.DrawMode;

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
		ArrayList<ArrayList<Point>> listControlPointsScaled = sc.getControlPointsForShapes();
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
					if(p.x >= cp.x && p.x <= cp.x + ShapeDrawingCollection.CONTROL_POINT_PIXEL_SIZE.width)
					{
						if(p.y >= cp.y && p.y <= cp.y + ShapeDrawingCollection.CONTROL_POINT_PIXEL_SIZE.height)
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
		ArrayList<Shape> shapesScaled = sc.getShapes();
		ArrayList<ArrayList<Point>> listControlPointsScaled = sc.getControlPointsForShapes();
		
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
			
			curvePoints[directionsIndex-1] = p;
			sc.addControlPoint(p);
			LoggingMessages.printOut(p + "");
			if(directionsIndex + 1 >= mode.getDirections().length)
			{
				sc.setDirectionsIndex(0);
				
				sc.constructShape(mode, curvePoints);
				
				sc.setDirectionsText("");
				
				sc.setOperation(Operation.Select);
				
				sc.drawAll();
			}
			else
			{
				sc.incrementDirectionsIndex(1);
				sc.setDirectionsText(mode.getDirections()[sc.getDirectionsIndex()]);
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
			ArrayList<Point> shapesControlPoints = sc.getControlPointsForShapes().get(index);
			Shape s = sc.getShapes().get(index);
			ArrayList<Point> newPoints = new ArrayList<Point>();
			for(Point p : shapesControlPoints)
			{
				newPoints.add(new Point(p.x - shift.x, p.y - shift.y));
			}
			for(int i = 0; i < newPoints.size(); i++)
			{
				sc.getControlPointsForShapes().get(index).set(i, newPoints.get(i));
				sc.notifyShapeAndControlPointChangedListener(index, i, this);
			}
			
			Shape newShape = sc.recalculateShape(s, newPoints);
			sc.getShapes().set(index, newShape);
			ShapeStyling ss = sc.getShapeStyling(index);
			ss.updateNumberGeneratorConfig(newShape);
			
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
	
	public void applyScalingAmount(Point shift)
	{
		for(int index : sc.getShapeSelectedIndexes())
		{
			ArrayList<Point> shapesControlPoints = sc.getControlPointsForShapes().get(index);
			Shape s = sc.getShapes().get(index);
			ArrayList<Point> newPoints = new ArrayList<Point>();
			for(Point p : shapesControlPoints)
			{
				newPoints.add(new Point(p.x - shift.x, p.y - shift.y));
			}
			for(int i = 0; i < newPoints.size(); i++)
			{
				sc.getControlPointsForShapes().get(index).set(i, newPoints.get(i));
				sc.notifyShapeAndControlPointChangedListener(index, i, this);
			}
			
			Shape newShape = sc.recalculateShape(s, newPoints);
			sc.getShapes().set(index, newShape);
			ShapeStyling ss = sc.getShapeStyling(index);
			ss.updateNumberGeneratorConfig(newShape);
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
		for(Shape s : sc.getShapes())
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
		ArrayList<Point> newPoints = sc.getControlPointsForShapes().get(shapeIndex);
		Shape s = sc.getShapes().get(shapeIndex);
		Shape newShape = sc.recalculateShape(s, newPoints);
		sc.getShapes().set(shapeIndex, newShape);
		ShapeStyling ss = sc.getShapeStyling(shapeIndex);
		ss.updateNumberGeneratorConfig(newShape);
		
		sc.drawAll();
	}
	
}
