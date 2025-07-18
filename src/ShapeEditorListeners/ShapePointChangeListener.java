package ShapeEditorListeners;

import java.awt.Point;
import java.awt.Shape;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Editors.PointEditor;
import WidgetComponents.ShapeCreator;

public class ShapePointChangeListener implements ChangeListener, ControlPointChangedListener
{
	private ShapeCreator sc;
	private PointEditor pe;
	private int
		shapeIndex,
		controlPointIndex;
	
	public ShapePointChangeListener(PointEditor pe, ShapeCreator shapeCreator, int shapeIndex, int controlPointIndex)
	{
		this.sc = shapeCreator;
		this.pe = pe;
		this.shapeIndex = shapeIndex;
		this.controlPointIndex = controlPointIndex;
		sc.addShapeAndControlPointChangedListener(shapeIndex, controlPointIndex, this);
	}
	
	@Override
	public void stateChanged(ChangeEvent e) 
	{
		Point newPoint = (Point) pe.getComponentValueObj();
		ArrayList<Point> newPoints = sc.getControlPointsScaled().get(shapeIndex);
		newPoints.set(controlPointIndex, newPoint);
		Shape s = sc.getShapesScaled().get(shapeIndex);
		Shape newShape = sc.recalculateShape(s, newPoints);
		sc.getShapesScaled().set(shapeIndex, newShape);
		
		sc.notifyShapeAndControlPointChangedListener(shapeIndex, controlPointIndex, this);
		
		sc.drawAll();
	}
	
	@Override
	public void controlPointChangedNotification(int shapeIndex, int controlPointIndex) 
	{
		Point newPoint = sc.getControlPointsScaled().get(shapeIndex).get(controlPointIndex);
		this.pe.setComponentValue(newPoint);
	}

}
