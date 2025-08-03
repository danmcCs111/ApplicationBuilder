package ShapeEditorListeners;

import java.awt.Point;
import java.awt.Shape;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Editors.PointEditor;
import ShapeWidgetComponents.ShapeCreator;
import ShapeWidgetComponents.ShapeStyling;

public class ShapePointChangeListener implements ChangeListener, ControlPointChangedListener
{
	private ShapeCreator sc;
	ShapeStyling ss;
	private PointEditor pe;
	private int
		controlPointIndex;
	
	public ShapePointChangeListener(PointEditor pe, ShapeCreator shapeCreator, ShapeStyling ss, int controlPointIndex)
	{
		this.sc = shapeCreator;
		this.ss = ss;
		this.pe = pe;
		this.controlPointIndex = controlPointIndex;
		sc.addShapeAndControlPointChangedListener(ss, controlPointIndex, this);
	}
	
	@Override
	public void stateChanged(ChangeEvent e) 
	{
		Point newPoint = (Point) pe.getComponentValueObj();
		ArrayList<Point> newPoints = sc.getControlPointsForShapes().get(ss.getIndex());
		newPoints.set(controlPointIndex, newPoint);
		Shape s = sc.getShapes().get(ss.getIndex());
		Shape newShape = sc.recalculateShape(s, newPoints);
		sc.getShapes().set(ss.getIndex(), newShape);
		
		sc.notifyShapeAndControlPointChangedListener(ss, controlPointIndex, this);
		
		sc.drawAll();
	}
	
	@Override
	public void controlPointChangedNotification(ShapeStyling ss, int controlPointIndex) 
	{
		Point newPoint = sc.getControlPointsForShapes().get(ss.getIndex()).get(controlPointIndex);
		this.pe.setComponentValue(newPoint);
	}

}
