package ShapeEditorListeners;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JLabel;

import Graphics2D.CurveShape;
import Properties.LoggingMessages;
import WidgetComponents.ShapeCreator;
import WidgetComponents.ShapeCreator.Mode;

public class DrawMouseListener extends MouseAdapter 
{
	private ShapeCreator sc;
	
	public DrawMouseListener(ShapeCreator sc)
	{
		this.sc = sc;
	}
	
	@Override
	public void mousePressed(MouseEvent e) 
	{
		int directionsIndex = sc.getDirectionsIndex();
		ArrayList<ArrayList<Point>> listControlPointsScaled = sc.getControlPointsScaled();
		Point p = sc.getRelativePoint(e);
		sc.setMousePressed(true);
		sc.setControlPointSelected(false);
		
		if(directionsIndex == 0)
		{
			LoggingMessages.printOut(p + "");
			int outerCount = 0;
			for(ArrayList<Point> controlPoints : listControlPointsScaled)
			{
				int count = 0;
				for(Point cp : controlPoints)
				{
					if(p.x >= cp.x && p.x <= cp.x + ShapeCreator.CONTROL_POINT_SIZE.width)
					{
						if(p.y >= cp.y && p.y <= cp.y + ShapeCreator.CONTROL_POINT_SIZE.height)
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
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
		int directionsIndex = sc.getDirectionsIndex();
		if(directionsIndex == 0 && !sc.getControlPointSelected())
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
		
		if(controlPointSelectedIndex != -1)
		{
			Point p = sc.getRelativePoint(e);
			LoggingMessages.printOut("shape index: " + controlPointShapeSelectedIndex + " controlPoint index:" + controlPointSelectedIndex);
			listControlPointsScaled.get(controlPointShapeSelectedIndex).set(controlPointSelectedIndex, p);
			Shape s = shapesScaled.get(controlPointShapeSelectedIndex);
			ArrayList<Point> cps = listControlPointsScaled.get(controlPointShapeSelectedIndex);
			
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
			
			shapesScaled.set(controlPointShapeSelectedIndex, s);
		}
		
		if(sc.getDirectionsIndex() == 0 && !sc.getControlPointSelected())
		{
			detectBounds(sc.getSelectTool());
		}
		
		sc.drawAll();
		sc.setControlPointSelectedIndex(-1);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		Mode mode = sc.getMode();
		int directionsIndex = sc.getDirectionsIndex();
		
		if(directionsIndex > 0)
		{
			Point p = sc.getRelativePoint(e);
			Point [] curvePoints = sc.getCurvePoints();
			JLabel directionsLabel = sc.getDirectionsLabel();
			ArrayList<Shape> shapes = sc.getShapes();
			
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
				case Mode.Line:
					shape = new Line2D.Double(curvePoints[0], curvePoints[1]);
					break;
				case Mode.Curve:
					shape = new CurveShape(curvePoints[0], curvePoints[2], curvePoints[3], curvePoints[1]);
					break;
				case Mode.ellipse:
					shape = new Ellipse2D.Double(
							curvePoints[0].x, curvePoints[0].y, 
							(curvePoints[1].x - curvePoints[0].x), (curvePoints[1].y - curvePoints[0].y));
					break;
				case Mode.rectangle:
					shape = new Rectangle2D.Double(
							curvePoints[0].x, curvePoints[0].y, 
							(curvePoints[1].x - curvePoints[0].x), (curvePoints[1].y - curvePoints[0].y));
					break;
				}
				shapes.add(shape);
				sc.drawAll();
				directionsLabel.setText("");
				sc.incrementNumShapes(1);
			}
			else
			{
				sc.incrementDirectionsIndex(1);
				directionsLabel.setText(mode.getDirections()[sc.getDirectionsIndex()]);
			}
		}
	}
	
	public void detectBounds(Rectangle2D bounds)
	{
		ArrayList<Shape> selectedShapes = new ArrayList<Shape>();
		for(Shape s : sc.getShapes())
		{
			if(bounds.contains(s.getBounds()))
			{
				selectedShapes.add(s);
				LoggingMessages.printOut("Selecting: " + s.getClass().getName());
			}
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
	
}
