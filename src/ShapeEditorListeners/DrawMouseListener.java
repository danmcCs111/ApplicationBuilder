package ShapeEditorListeners;

import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
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
		sc.setMousePressed(true);
		boolean mousePressed = sc.getMousePressed();
		ArrayList<ArrayList<Point>> listControlPointsScaled = sc.getControlPointsScaled();
		
		LoggingMessages.printOut(sc + "");
		if(directionsIndex == 0 && mousePressed)
		{
			Point p = sc.getRelativePoint(e);
			LoggingMessages.printOut(p + "");
			int count = 0, outerCount = 0;
			for(ArrayList<Point> controlPoints : listControlPointsScaled)
			{
				for(Point cp : controlPoints)
				{
					if(p.x >= cp.x && p.x <= cp.x + ShapeCreator.CONTROL_POINT_SIZE.width)
					{
						if(p.y >= cp.y && p.y <= cp.y + ShapeCreator.CONTROL_POINT_SIZE.height)
						{
							LoggingMessages.printOut("Control Point selected!");
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
			listControlPointsScaled.get(controlPointShapeSelectedIndex).set(controlPointSelectedIndex, p);
			Shape s = shapesScaled.get(controlPointShapeSelectedIndex);
			if(s instanceof CurveShape)
			{
				ArrayList<Point> cps = listControlPointsScaled.get(controlPointShapeSelectedIndex);
				s = new CurveShape();
				((CubicCurve2D) s).setCurve(cps.get(0), cps.get(2), cps.get(3), cps.get(1));
				shapesScaled.set(controlPointShapeSelectedIndex, s);
			}
			if(s instanceof Line2D)
			{
				ArrayList<Point> cps = listControlPointsScaled.get(controlPointShapeSelectedIndex);
				s = new Line2D.Double(cps.get(0), cps.get(1));
				shapesScaled.set(controlPointShapeSelectedIndex, s);
			}
			sc.drawAll();
		}
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
				
				if(mode == Mode.Line)
				{
					Line2D lineShape = new Line2D.Double(curvePoints[0], curvePoints[1]);
					shapes.add(lineShape);
				}
				else if(mode == Mode.Curve)
				{
					CurveShape curveShape = new CurveShape();
					curveShape.setCurve(curvePoints[0], curvePoints[2], curvePoints[3], curvePoints[1]);
					shapes.add(curveShape);
				}
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
	
}
